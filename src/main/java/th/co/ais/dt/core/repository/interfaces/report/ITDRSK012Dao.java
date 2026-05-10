package th.co.ais.dt.core.repository.interfaces.report;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.dt.entity.cm.LocationMst;

public interface ITDRSK012Dao {
	public List<LocationMst> queryLocationCodeSaleAtShop(Object listCompany, String locationCode)throws DataAccessException;

}
