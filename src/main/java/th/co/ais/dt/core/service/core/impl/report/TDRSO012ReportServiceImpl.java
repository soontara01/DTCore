package th.co.ais.dt.core.service.core.impl.report;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO012Bean;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSO012Dao;
import th.co.ais.dt.core.service.core.interfaces.report.TDRSO012ReportService;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.util.MessageMasterService;
import th.co.ais.dt.util.BeanUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class TDRSO012ReportServiceImpl implements TDRSO012ReportService {

    private MessageMasterService messageMstService;
    private ITDRSO012Dao iTDRSO012Dao;

    @Override
    public List<LocationMst> getRangeLocation(TDRSO012Bean input) throws DataAccessException {
        return iTDRSO012Dao.getRangeLocation(input);
    }

    @Override
    public List<LocationMst> getLocationByCriteria(TDRSO012Bean request) throws DataAccessException {
        return iTDRSO012Dao.getLocationByCriteria(request);
    }

    @Override
    public List<LocationMst> getMultiLocation(TDRSO012Bean input) throws DataAccessException {
        return iTDRSO012Dao.getMultiLocation(input);
    }

    @Override
    public ProcessResult validateInput(TDRSO012Bean input) throws DataAccessException {

        ProcessResult processResult = new ProcessResult(false, null, null);

        try {
            if (BeanUtil.isNotEmpty(input.getFromLocationCode())) {
                processResult.setSuccess(true);
                processResult.setMessage("Success");
            } else {
                processResult.setSuccess(false);
                processResult.setMessage(messageMstService.convertMessage("TP0001", "Location", null, null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            processResult.setSuccess(false);
            processResult.setMessage(e.getMessage());
        }
        return processResult;
    }
}
