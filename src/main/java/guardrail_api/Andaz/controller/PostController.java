package guardrail_api.Andaz.controller;

import guardrail_api.Andaz.dto.CreateCommentRequest;
import guardrail_api.Andaz.dto.CreatePostRequest;
import guardrail_api.Andaz.dto.LikeRequest;
import guardrail_api.Andaz.entity.Comment;
import guardrail_api.Andaz.entity.Post;
import guardrail_api.Andaz.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest request) {
        return postService.createPost(request);
    }

    @PostMapping("/{postId}/comments")
    public Comment addComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request
    ) {
        return postService.addComment(postId, request);
    }

    @PostMapping("/{postId}/like")
    public String likePost(
            @PathVariable Long postId,
            @RequestBody LikeRequest request
    ) {
        return postService.likePost(postId, request.getUserId());
    }
}