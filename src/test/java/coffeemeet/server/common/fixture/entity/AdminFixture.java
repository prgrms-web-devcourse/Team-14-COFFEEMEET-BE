package coffeemeet.server.common.fixture.entity;

import static org.instancio.Select.field;

import coffeemeet.server.admin.presentation.dto.AdminCustomSlice;
import coffeemeet.server.admin.presentation.dto.AdminLoginHTTP;
import coffeemeet.server.admin.presentation.dto.ReportDeletionHTTP;
import coffeemeet.server.admin.presentation.dto.UserPunishmentHTTP;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse;
import coffeemeet.server.inquiry.service.dto.InquirySearchResponse.InquirySummary;
import java.util.List;
import org.instancio.Instancio;

public class AdminFixture {


  public static AdminLoginHTTP.Request adminLoginHTTPRequest() {
    return Instancio.of(AdminLoginHTTP.Request.class).create();
  }

  public static UserPunishmentHTTP.Request reportApprovalHTTPRequest() {
    return Instancio.of(UserPunishmentHTTP.Request.class).create();
  }

  public static ReportDeletionHTTP.Request reportRejectionHTTPRequest() {
    return Instancio.of(ReportDeletionHTTP.Request.class).create();
  }

  public static AdminCustomSlice<InquirySummary> adminCustomPageByInquiry(
      List<InquirySummary> contents, boolean hasNext) {
    return Instancio.of(AdminCustomSlice.class)
        .withTypeParameters(InquirySearchResponse.InquirySummary.class)
        .set(field(AdminCustomSlice<InquirySummary>::contents), contents)
        .set(field(AdminCustomSlice<InquirySummary>::hasNext), hasNext)
        .create();
  }

}
