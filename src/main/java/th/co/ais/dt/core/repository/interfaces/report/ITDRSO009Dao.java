package th.co.ais.dt.core.repository.interfaces.report;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.dt.core.controller.impl.report.dto.TDRSO009Bean;
import th.co.ais.dt.entity.cm.LocationMst;

public interface ITDRSO009Dao {

	public List<LocationMst> getRangeLocation(TDRSO009Bean input) throws DataAccessException;
	public List<LocationMst> getLocationByCriteria(TDRSO009Bean request) throws DataAccessException;
	public List<LocationMst> getMultiLocation(TDRSO009Bean input) throws DataAccessException;
}
