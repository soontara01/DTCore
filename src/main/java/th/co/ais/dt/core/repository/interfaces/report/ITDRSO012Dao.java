package th.co.ais.dt.core.repository.interfaces.report;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.dt.core.controller.impl.report.dto.TDRSO012Bean;
import th.co.ais.dt.entity.cm.LocationMst;

public interface ITDRSO012Dao {

	public List<LocationMst> getRangeLocation(TDRSO012Bean input) throws DataAccessException;
	public List<LocationMst> getLocationByCriteria(TDRSO012Bean request) throws DataAccessException;
	public List<LocationMst> getMultiLocation(TDRSO012Bean input) throws DataAccessException;
}
