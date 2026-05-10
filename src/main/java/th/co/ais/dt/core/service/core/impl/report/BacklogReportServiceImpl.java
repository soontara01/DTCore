package th.co.ais.dt.core.service.core.impl.report;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import th.co.ais.dt.core.controller.impl.report.dto.BacklogReportBean;
import th.co.ais.dt.core.repository.interfaces.report.IBacklogReportDao;
import th.co.ais.dt.core.service.core.interfaces.report.IBacklogReportService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BacklogReportServiceImpl implements IBacklogReportService {
    
    private final IBacklogReportDao backlogReportDao;
    
    @Override
    public List<BacklogReportBean> queryBacklogSummary(BacklogReportBean request) throws DataAccessException {
        return backlogReportDao.queryBacklogSummary(request);
    }
}
