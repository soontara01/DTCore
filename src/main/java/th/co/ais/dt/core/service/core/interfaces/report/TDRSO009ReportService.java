package th.co.ais.dt.core.service.core.interfaces.report;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.core.controller.impl.report.dto.TDRSO009Bean;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.service.core.dto.ProcessResult;

@Transactional
public interface TDRSO009ReportService {
	public List<LocationMst> getRangeLocation(TDRSO009Bean input) throws DataAccessException;

	public List<LocationMst> getLocationByCriteria(TDRSO009Bean request) throws DataAccessException;
	
	public List<LocationMst> getMultiLocation(TDRSO009Bean input) throws DataAccessException;
	
	public ProcessResult validateInput(TDRSO009Bean input) throws DataAccessException;
}
