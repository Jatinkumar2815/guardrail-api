package guardrail_api.Andaz.dto;

import guardrail_api.Andaz.entity.AuthorType;
import lombok.Data;

@Data
public class CreateCommentRequest {

    private AuthorType authorType;

    private Long authorId;

    private Long parentCommentId;

    private Long targetHumanId;

    private String content;
}