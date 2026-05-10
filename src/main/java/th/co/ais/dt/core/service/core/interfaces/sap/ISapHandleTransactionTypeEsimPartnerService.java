package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeEsimPartnerService {
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) ;
	public DtSapTransaction queryDtSapTransactionById(Long sapTranId) ;
	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction)  ;
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) ;
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) ;
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) ;

}
