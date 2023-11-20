package coffeemeet.server.report.implement;

import coffeemeet.server.report.domain.Report;
import coffeemeet.server.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ReportCommand {

  private final ReportQuery reportQuery;
  private final ReportRepository reportRepository;

  public void createReport(Report report) {
    reportRepository.save(report);
  }

  public void processReport(Long reportId) {
    Report report = reportQuery.getReportById(reportId);
    report.processed();
  }

}