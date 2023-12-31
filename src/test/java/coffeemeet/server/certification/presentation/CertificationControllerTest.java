package coffeemeet.server.certification.presentation;

import static coffeemeet.server.common.fixture.dto.RefreshTokenFixture.refreshToken;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.companyName;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.department;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.email;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.emailDtoRequest;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.userId;
import static coffeemeet.server.common.fixture.entity.CertificationFixture.verificationCodeDtoRequest;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.certification.service.CertificationService;
import coffeemeet.server.common.config.ControllerTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;

@WebMvcTest(CertificationController.class)
class CertificationControllerTest extends ControllerTestConfig {

  @MockBean
  private CertificationService certificationService;

  @BeforeEach
  void setUp() {
    Long userId = 1L;
    RefreshToken refreshToken = refreshToken();
    given(refreshTokenQuery.getRefreshToken(anyLong())).willReturn(refreshToken);
    given(jwtTokenProvider.extractUserId(TOKEN)).willReturn(userId);
  }

  @Test
  @DisplayName("회사 인증 정보를 등록할 수 있다.")
  void registerCompanyInfoTest() throws Exception {
    // given
    String sUserId = "userId";
    String sCompanyName = "companyName";
    String sBusinessCard = "businessCard";
    String sCompanyEmail = "companyEmail";
    String sDepartment = "department";

    MockMultipartFile businessCardImage = new MockMultipartFile(
        sBusinessCard,
        "business_card.jpg",
        "image/jpeg",
        sBusinessCard.getBytes()
    );
    MockPart userId = new MockPart(sUserId, userId().getBytes());
    MockPart companyName = new MockPart(sCompanyName, companyName().getBytes());
    MockPart companyEmail = new MockPart(sCompanyEmail, email().getBytes());
    MockPart department = new MockPart(sDepartment, department().name().getBytes());

    // when, then
    mockMvc.perform(multipart("/api/v1/certification/users/me/company-info")
            .file("businessCard", businessCardImage.getBytes())
            .part(userId, companyName, companyEmail, department)
            .contentType(MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk())
        .andDo(document("certification-register",
            resourceDetails().tag("회사 인증").description("회사 정보 등록"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParts(
                partWithName(sUserId).description("유저 아이디"),
                partWithName(sCompanyName).description("회사명"),
                partWithName(sBusinessCard).description("회사 명함 이미지"),
                partWithName(sCompanyEmail).description("회사 이메일"),
                partWithName(sDepartment).description("회사 부서명")
            )
        ));
  }

  @Test
  @DisplayName("회사 인증 정보를 수정할 수 있다.")
  void updateCompanyInfoTest() throws Exception {
    // given
    String sUserId = "userId";
    String sCompanyName = "companyName";
    String sBusinessCard = "businessCard";
    String sCompanyEmail = "companyEmail";
    String sDepartment = "department";

    MockMultipartFile businessCardImage = new MockMultipartFile(
        sBusinessCard,
        "business_card.jpg",
        "image/jpeg",
        sBusinessCard.getBytes()
    );
    MockPart userId = new MockPart(sUserId, userId().getBytes());
    MockPart companyName = new MockPart(sCompanyName, companyName().getBytes());
    MockPart companyEmail = new MockPart(sCompanyEmail, email().getBytes());
    MockPart department = new MockPart(sDepartment, department().name().getBytes());

    // when, then
    mockMvc.perform(multipart("/api/v1/certification/users/me/company-info/update")
            .file("businessCard", businessCardImage.getBytes())
            .part(userId, companyName, companyEmail, department)
            .header("Authorization", TOKEN)
            .contentType(MULTIPART_FORM_DATA)
        )
        .andExpect(status().isOk())
        .andDo(document("certification-register",
            resourceDetails().tag("회사 인증").description("회사 정보 수정"),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("토큰")
            ),
            requestParts(
                partWithName(sUserId).description("유저 아이디"),
                partWithName(sCompanyName).description("회사명"),
                partWithName(sBusinessCard).description("회사 명함 이미지"),
                partWithName(sCompanyEmail).description("회사 이메일"),
                partWithName(sDepartment).description("회사 부서명")
            )
        ));
  }

  @Test
  @DisplayName("이메일을 통해 인증코드를 전송할 수 있다.")
  void sendVerificationCodeByEmailTest() throws Exception {
    // given
    String emailDtoRequest = objectMapper.writeValueAsString(emailDtoRequest());

    // when, then
    mockMvc.perform(post("/api/v1/certification/users/me/company-mail")
            .contentType(APPLICATION_JSON)
            .content(emailDtoRequest))
        .andDo(document("certification-sendVerificationCode",
                resourceDetails().tag("회사 인증").description("회사 이메일 인증을 위한 메일 전송"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("userId").description("사용자 아이디"),
                    fieldWithPath("companyEmail").description("회사 이메일")
                )
            )
        )
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("이메일을 검증을 할 수 있다.")
  void verifyEmailTest() throws Exception {
    String verificationCodeDtoRequest = objectMapper.writeValueAsString(
        verificationCodeDtoRequest());

    mockMvc.perform(post("/api/v1/certification/users/me/company-mail/verification")
            .contentType(APPLICATION_JSON)
            .content(verificationCodeDtoRequest))
        .andDo(document("certification-verifyEmail",
                resourceDetails().tag("회사 인증").description("회사 이메일 인증을 위한 코드 검증"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("userId").description("사용자 아이디"),
                    fieldWithPath("verificationCode").description("인증 코드")
                )
            )
        )
        .andExpect(status().isOk());
  }

}
