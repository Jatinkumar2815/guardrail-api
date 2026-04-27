package guardrail_api.Andaz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private guardrail_api.Andaz.entity.AuthorType authorType;

    private Long authorId;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;
}