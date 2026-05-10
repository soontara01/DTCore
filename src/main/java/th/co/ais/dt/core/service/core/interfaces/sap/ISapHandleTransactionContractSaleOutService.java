package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;

@Transactional
public interface ISapHandleTransactionContractSaleOutService {
	public DtSapAccrualHeader postAccrualReceipt(DtSapContractTrans sapContractTrans) throws DataAccessException;
	public DtSapUniversalHeader postUniversalReceipt(DtSapContractTrans sapContractTrans) throws DataAccessException;
	public DtSapContractTransLog insertSapContractTransLog(DtSapContractTrans sapContractTrans) throws DataAccessException;
	public DtSapUniversalHeader callUniversalApi(DtSapUniversalHeader dtSapOrderHeader);
	public DtSapAccrualHeader callAccrualApi(DtSapAccrualHeader dtSapAccrualHeader);
	public void updateDtSapUniversalHeader(DtSapUniversalHeader dtSapOrderHeader);
	public void updateDtSapAccrualHeader(DtSapAccrualHeader dtSapAccrualHeader);
	public void updateDtSapContractTransLog(DtSapContractTransLog dtSapContractTransLog);
	public void updateDtsapContractTrans(DtSapContractTrans dtSapContractTrans);
	
}
