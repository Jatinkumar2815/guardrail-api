package guardrail_api.Andaz.dto;

import guardrail_api.Andaz.entity.AuthorType;
import lombok.Data;

@Data
public class CreatePostRequest {

    private AuthorType authorType;

    private Long authorId;

    private String content;
}