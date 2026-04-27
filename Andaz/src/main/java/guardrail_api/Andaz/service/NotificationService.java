package guardrail_api.Andaz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final StringRedisTemplate redisTemplate;

    public void handleBotNotification(Long userId, String message) {

        String cooldownKey = "user:" + userId + ":notif_cooldown";
        String pendingListKey = "user:" + userId + ":pending_notifs";

        Boolean firstNotification = redisTemplate.opsForValue()
                .setIfAbsent(cooldownKey, "sent", Duration.ofMinutes(15));

        if (Boolean.TRUE.equals(firstNotification)) {
            System.out.println("Push Notification Sent to User " + userId + ": " + message);
        } else {
            redisTemplate.opsForList().rightPush(pendingListKey, message);
        }
    }
}