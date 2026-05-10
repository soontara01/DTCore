package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;

@Transactional
public interface ISapHandleTransactionTypeDepositPartnerChangeInfoService {
	public DtSapOrderHeader queryInfoAndInsertSaleorder57(DtSapTransaction dtSapTransaction) ;

}
