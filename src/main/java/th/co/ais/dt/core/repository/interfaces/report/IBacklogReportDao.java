package th.co.ais.dt.core.repository.interfaces.report;

import org.springframework.dao.DataAccessException;
import th.co.ais.dt.core.controller.impl.report.dto.BacklogReportBean;

import java.util.List;

public interface IBacklogReportDao {
    List<BacklogReportBean> queryBacklogSummary(BacklogReportBean request) throws DataAccessException;
}
