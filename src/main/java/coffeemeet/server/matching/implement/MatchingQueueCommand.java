package coffeemeet.server.matching.implement;

import static coffeemeet.server.common.execption.GlobalErrorCode.INTERNAL_SERVER_ERROR;

import coffeemeet.server.common.execption.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingQueueCommand {

  private final RedisTemplate<String, Long> redisTemplate;

  public void enqueueUserByCompanyName(String companyName, Long userId) {
    ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
    Boolean result = zSetOperations.add(companyName, userId, System.currentTimeMillis());
    if (result == null) {
      throw new RedisException("Redis가 Pipeline 상태이거나 Transaction 상태입니다.", INTERNAL_SERVER_ERROR);
    }
  }

  public void deleteUserByUserId(String companyName,Long userId) {
    redisTemplate.opsForZSet().remove(companyName, userId);
  }

}
