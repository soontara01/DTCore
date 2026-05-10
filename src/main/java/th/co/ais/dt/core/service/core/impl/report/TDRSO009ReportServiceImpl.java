package th.co.ais.dt.core.service.core.impl.report;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO009Bean;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSO009Dao;
import th.co.ais.dt.core.service.core.interfaces.report.TDRSO009ReportService;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.util.MessageMasterService;
import th.co.ais.dt.util.BeanUtil;

@Service
@AllArgsConstructor
public class TDRSO009ReportServiceImpl implements TDRSO009ReportService{

	private ITDRSO009Dao iTDRSO009Dao;
	private MessageMasterService messageMstService;

	@Override
	public List<LocationMst> getRangeLocation(TDRSO009Bean input) throws DataAccessException {
		return iTDRSO009Dao.getRangeLocation(input);
	}

	@Override
	public List<LocationMst> getLocationByCriteria(TDRSO009Bean request) throws DataAccessException {
		return iTDRSO009Dao.getLocationByCriteria(request);
	}

	@Override
	public List<LocationMst> getMultiLocation(TDRSO009Bean input) throws DataAccessException {
		return iTDRSO009Dao.getMultiLocation(input);
	}

	@Override
	public ProcessResult validateInput(TDRSO009Bean input) throws DataAccessException {
	ProcessResult processResult = new ProcessResult(false, null, null);
		
		try {
			if(BeanUtil.isNotEmpty(input.getFromLocationCode())) {
				processResult.setSuccess(true);
				processResult.setMessage("Success");
			}
			else {
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
