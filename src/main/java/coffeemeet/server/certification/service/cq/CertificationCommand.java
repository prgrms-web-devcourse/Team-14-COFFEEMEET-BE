package coffeemeet.server.certification.service.cq;

import coffeemeet.server.certification.domain.Certification;
import coffeemeet.server.certification.domain.CompanyEmail;
import coffeemeet.server.certification.domain.Department;
import coffeemeet.server.certification.repository.CertificationRepository;
import coffeemeet.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CertificationCommand {

  private final CertificationRepository certificationRepository;

  public void newCertification(CompanyEmail companyEmail, String businessCardUrl,
      Department department, User user) {
    certificationRepository.save(
        Certification.builder()
            .companyEmail(companyEmail)
            .businessCardUrl(businessCardUrl)
            .department(department)
            .user(user)
            .build()
    );
  }

}