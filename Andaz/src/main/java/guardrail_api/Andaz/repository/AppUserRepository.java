package guardrail_api.Andaz.repository;

import guardrail_api.Andaz.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}