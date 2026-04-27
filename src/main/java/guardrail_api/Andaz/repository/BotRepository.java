package guardrail_api.Andaz.repository;

import guardrail_api.Andaz.entity.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {
}