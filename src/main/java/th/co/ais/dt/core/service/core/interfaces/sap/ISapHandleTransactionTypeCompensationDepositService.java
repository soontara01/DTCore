package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeCompensationDepositService {
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType);

	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction);

	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);

	public DtSapOrderHeader queryInfoAndInsertSaleorderCompensationDeposit(DtSapTransaction dtSapTransaction);

	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);

	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);

	public DtSapTransaction queryDtSapTransactionById(Long sapTranId);

	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader);

	public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader);
}
