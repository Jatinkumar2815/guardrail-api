package guardrail_api.Andaz.repository;

import guardrail_api.Andaz.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}