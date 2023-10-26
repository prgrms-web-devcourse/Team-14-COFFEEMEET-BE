package coffeemeet.server.certification.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "email_verification", timeToLive = 360)
public class EmailVerification {

  @Id
  private Long userId;
  private CompanyEmail companyEmail;
  private String code;
  private LocalDateTime createdAt;

  public EmailVerification(Long userId, CompanyEmail companyEmail, String code) {
    this.userId = userId;
    this.companyEmail = companyEmail;
    this.code = code;
    this.createdAt = LocalDateTime.now();
  }

}
