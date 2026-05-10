package th.co.ais.dt.core.service.core.interfaces.report;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.core.controller.impl.report.dto.BacklogReportBean;

import java.util.List;

@Transactional
public interface IBacklogReportService {
    List<BacklogReportBean> queryBacklogSummary(BacklogReportBean request) throws DataAccessException;
}
