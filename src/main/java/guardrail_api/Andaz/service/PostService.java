package guardrail_api.Andaz.service;

import guardrail_api.Andaz.dto.CreateCommentRequest;
import guardrail_api.Andaz.dto.CreatePostRequest;
import guardrail_api.Andaz.entity.AuthorType;
import guardrail_api.Andaz.entity.Comment;
import guardrail_api.Andaz.entity.Post;
import guardrail_api.Andaz.repository.CommentRepository;
import guardrail_api.Andaz.repository.PostRepository;
import guardrail_api.Andaz.entity.AuthorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final guardrail_api.Andaz.service.RedisGuardrailService redisGuardrailService;
    private final guardrail_api.Andaz.service.NotificationService notificationService;

    public Post createPost(CreatePostRequest request) {

        Post post = new Post();
        post.setAuthorType(request.getAuthorType());
        post.setAuthorId(request.getAuthorId());
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(Long postId, CreateCommentRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment parentComment = null;
        int depthLevel = 1;

        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            depthLevel = parentComment.getDepthLevel() + 1;
        }

        redisGuardrailService.checkVerticalDepth(depthLevel);

        if (request.getAuthorType() == AuthorType.BOT) {

            redisGuardrailService.checkHorizontalBotCap(postId);

            if (request.getTargetHumanId() != null) {
                redisGuardrailService.checkBotHumanCooldown(
                        request.getAuthorId(),
                        request.getTargetHumanId()
                );
            }

            redisGuardrailService.incrementViralityScore(postId, 1);

            if (post.getAuthorType() == AuthorType.USER) {
                notificationService.handleBotNotification(
                        post.getAuthorId(),
                        "Bot " + request.getAuthorId() + " replied to your post"
                );
            }

        } else {
            redisGuardrailService.incrementViralityScore(postId, 50);
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setParentComment(parentComment);
        comment.setAuthorType(request.getAuthorType());
        comment.setAuthorId(request.getAuthorId());
        comment.setContent(request.getContent());
        comment.setDepthLevel(depthLevel);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public String likePost(Long postId, Long userId) {

        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        redisGuardrailService.incrementViralityScore(postId, 20);

        return "Post liked by user " + userId;
    }
}