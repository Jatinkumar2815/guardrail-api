package guardrail_api.Andaz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisGuardrailService {

    private final StringRedisTemplate redisTemplate;

    public void incrementViralityScore(Long postId, int points) {
        String key = "post:" + postId + ":virality_score";
        redisTemplate.opsForValue().increment(key, points);
    }

    public void checkHorizontalBotCap(Long postId) {
        String key = "post:" + postId + ":bot_count";

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count > 100) {
            redisTemplate.opsForValue().decrement(key);
            throw new RuntimeException("Bot reply limit exceeded. Maximum 100 bot replies allowed.");
        }
    }

    public void checkVerticalDepth(int depthLevel) {
        if (depthLevel > 20) {
            throw new RuntimeException("Comment depth limit exceeded. Maximum depth allowed is 20.");
        }
    }

    public void checkBotHumanCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;

        Boolean allowed = redisTemplate.opsForValue()
                .setIfAbsent(key, "locked", Duration.ofMinutes(10));

        if (Boolean.FALSE.equals(allowed)) {
            throw new RuntimeException("Bot cooldown active. Same bot cannot interact with same human within 10 minutes.");
        }
    }
}