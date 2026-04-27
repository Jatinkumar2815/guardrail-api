package guardrail_api.Andaz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationSweeper {

    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void sweepPendingNotifications() {

        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            List<String> messages = redisTemplate.opsForList().range(key, 0, -1);

            if (messages == null || messages.isEmpty()) {
                continue;
            }

            String userId = key.split(":")[1];
            int count = messages.size();

            System.out.println(
                    "Summarized Push Notification for User " + userId +
                            ": " + messages.get(0) +
                            " and " + (count - 1) +
                            " others interacted with your posts."
            );

            redisTemplate.delete(key);
        }
    }
}