package guardrail_api.Andaz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private guardrail_api.Andaz.entity.Post post;

    @ManyToOne
    private Comment parentComment;

    @Enumerated(EnumType.STRING)
    private guardrail_api.Andaz.entity.AuthorType authorType;

    private Long authorId;

    @Column(length = 2000)
    private String content;

    private int depthLevel;

    private LocalDateTime createdAt;
}