package coffeemeet.server.common.fixture.entity;

import static coffeemeet.server.common.fixture.entity.UserFixture.users;
import static org.instancio.Select.field;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.domain.EmailVerification;
import coffeemeet.server.certification.presentation.dto.EmailHTTP;
import coffeemeet.server.certification.presentation.dto.VerificationCodeHTTP;
import coffeemeet.server.certification.service.dto.PendingCertification;
import coffeemeet.server.certification.service.dto.PendingCertificationPageDto;
import coffeemeet.server.user.domain.User;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.internal.generator.domain.internet.EmailGenerator;
import org.instancio.internal.generator.lang.IntegerGenerator;
import org.instancio.internal.generator.net.URLGenerator;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CertificationFixture {

  public static Certification certification() {
    return Instancio.of(Certification.class)
        .generate(field(Certification::getBusinessCardUrl), gen -> gen.net().url().asString())
        .set(field(Certification::getCompanyEmail), new CompanyEmail(new EmailGenerator().get()))
        .create();
  }

  public static Certification certification(User user) {
    return Instancio.of(Certification.class)
        .generate(field(Certification::getBusinessCardUrl), gen -> gen.net().url().asString())
        .set(field(Certification::getCompanyEmail), new CompanyEmail(new EmailGenerator().get()))
        .set(field(Certification::getId), user.getId())
        .set(field(Certification::getUser), user)
        .create();
  }

  public static Certification certificatedCertification(User user, String companyName) {
    return Instancio.of(Certification.class)
        .generate(field(Certification::getBusinessCardUrl), gen -> gen.net().url().asString())
        .set(field(Certification::getCompanyEmail), new CompanyEmail(new EmailGenerator().get()))
        .set(field(Certification::getCompanyName), companyName)
        .set(field(Certification::getId), user.getId())
        .set(field(Certification::getUser), user)
        .set(field(Certification::isCertificated), true)
        .create();
  }

  public static List<Certification> certificatedCertifications(List<User> users,
      String companyName) {
    return users.stream()
        .map(user -> certificatedCertification(user, companyName))
        .toList();
  }

  public static List<Certification> certifications() {
    return users().stream().map(CertificationFixture::certification).toList();
  }

  public static EmailVerification emailVerification() {
    return Instancio.of(EmailVerification.class).set(field(EmailVerification::getCompanyEmail),
        new CompanyEmail(new EmailGenerator().get())).create();
  }

  public static EmailVerification emailVerification(Long userId) {
    return Instancio.of(EmailVerification.class).set(field(EmailVerification::getUserId), userId)
        .set(field(EmailVerification::getCompanyEmail),
            new CompanyEmail(new EmailGenerator().get())).create();
  }

  public static CompanyEmail companyEmail() {
    return Instancio.of(CompanyEmail.class)
        .generate(field(CompanyEmail::getValue), gen -> gen.net().email()).create();
  }

  public static String companyName() {
    return Instancio.create(String.class);
  }

  public static String userId() {
    return String.valueOf(new IntegerGenerator().range(0, 999999).get());
  }

  public static String verificationCode() {
    return String.format("%06d", new IntegerGenerator().range(0, 999999).get());
  }

  public static String businessCardUrl() {
    return String.valueOf(new URLGenerator().get());
  }

  public static String email() {
    return new EmailGenerator().get();
  }

  public static Department department() {
    return Instancio.create(Department.class);
  }

  public static EmailHTTP.Request emailDtoRequest() {
    return Instancio.of(EmailHTTP.Request.class)
        .generate(field(EmailHTTP.Request::companyEmail), gen -> gen.net().email()).create();
  }

  public static VerificationCodeHTTP.Request verificationCodeDtoRequest() {
    return Instancio.of(VerificationCodeHTTP.Request.class)
        .set(field(VerificationCodeHTTP.Request::verificationCode),
            String.format("%06d", new IntegerGenerator().range(0, 999999).get())).create();
  }

  public static Pageable pageable() {
    int page = 0;
    int size = new IntegerGenerator().range(1, 100).get();
    return PageRequest.of(page, size, Sort.by("updatedAt").ascending());
  }

  public static PendingCertificationPageDto pendingCertificationPageDto(int size) {
    return new PendingCertificationPageDto(new PageImpl<>(pendingCertifications(size)));
  }

  private static List<PendingCertification> pendingCertifications(int size) {
    return Instancio.ofList(PendingCertification.class).size(size)
        .generate(field(PendingCertification::businessCardUrl), gen -> gen.net().url().asString())
        .generate(field(PendingCertification::companyEmail), gen -> gen.net().email()).create();
  }

}
