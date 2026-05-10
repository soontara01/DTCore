package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.controller.dto.ReserveProductBean;
import th.co.ais.dt.controller.dto.ReserveProductStockSapBean;
import th.co.ais.dt.entity.sap.DtSapReserveLog;

@Transactional
public interface IReserveProductStockService {
	public ReserveProductStockSapBean callServiceReserve(ReserveProductBean input);
	public void insertDtSapReserveLog(DtSapReserveLog reserveLog) throws DataAccessException;
}
