package coffeemeet.server.common.fixture.dto;

import coffeemeet.server.report.service.dto.GroupReportDto;
import org.instancio.Instancio;

public class TargetReportDtoFixture {

  public static GroupReportDto.Response targetReportDto() {
    return Instancio.of(GroupReportDto.Response.class)
        .create();
  }

}
