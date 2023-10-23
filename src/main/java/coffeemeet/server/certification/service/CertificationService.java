package coffeemeet.server.certification.service;

import coffeemeet.server.certification.domain.VerificationVo;
import coffeemeet.server.certification.repository.VerificationVoRepository;
import coffeemeet.server.common.media.EmailService;
import coffeemeet.server.common.media.S3MediaService;
import coffeemeet.server.common.util.FileUtils;
import coffeemeet.server.user.domain.CompanyEmail;
import coffeemeet.server.user.service.UserService;
import java.io.File;
import java.time.LocalDateTime;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationService {

  public static final String VERIFICATION_CODE_NOT_FOUND = "인증번호 기간이 만료되었거나 해당 유저가 인증 번호를 요청한 기록이 없습니다.";
  public static final String WRONG_VERIFICATION_CODE = "잘못된 인증번호입니다.";
  private static final RandomGenerator RANDOM_GENERATOR = RandomGenerator.getDefault();

  private final S3MediaService s3MediaService;
  private final UserService userService;
  private final EmailService emailService;
  private final VerificationVoRepository verificationVoRepository;

  public void uploadBusinessCard(long userId, File file) {
    String key = s3MediaService.generateBusinessCardKey();
    s3MediaService.upload(key, file);
    userService.updateBusinessCardUrl(userId, s3MediaService.getUrl(key));

    FileUtils.delete(file);
  }

  public void sendVerificationMail(Long userId, String email) {
    CompanyEmail companyEmail = new CompanyEmail(email);
    userService.validateDuplicatedCompanyEmail(companyEmail);

    String verificationCode = generateVerificationCode();
    emailService.sendVerificationMail(companyEmail, verificationCode);
    verificationVoRepository.save(
        new VerificationVo(userId, companyEmail, verificationCode, LocalDateTime.now()));
  }

  private String generateVerificationCode() {
    return String.format("%06d", RANDOM_GENERATOR.nextInt(1000000));
  }

  public void verifyEmail(Long userId, String verificationCode) {
    VerificationVo verificationVo = verificationVoRepository.findById(userId)
        .orElseThrow(
            () -> new IllegalArgumentException(VERIFICATION_CODE_NOT_FOUND));

    if (!verificationVo.getCode().equals(verificationCode)) {
      throw new IllegalArgumentException(WRONG_VERIFICATION_CODE);
    }

    userService.updateCompanyEmail(userId, verificationVo.getCompanyEmail());
  }
}
