package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeCancelSaleSameDayService {
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType);

	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction);

	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);

	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);

	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);

	public DtSapTransaction queryDtSapTransactionById(Long sapTranId);

	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader);

	public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader);

	public DtSapOrderHeader queryInfoAndInsertSaleorder(DtSapTransaction dtSapTransaction);
	
	public DtSapOrderHeader queryInfoAndInsertSaleorderCNMemo(DtSapTransaction dtSapTransaction) throws DataAccessException ;
	
	public DtSapPostHeader queryInfoAndInsertPostTransactionCNMEMO(DtSapTransaction dtSapTransaction) throws DataAccessException ;
	
	public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) ;
	
	public DtSapTransaction insertSapTransaction(String docNo , String company , Long transactionType , String userId) ;
	
	public DtSapPostHeader queryInfoAndInsertPostTransactionVoid(DtSapTransaction dtSapTransaction) ;
	
	public DtSapPostHeader queryInfoAndInsertPostTransactionORVoid(DtSapTransaction dtSapTransaction) ;
	
	public DtSapOrderHeader unLockOrderBeforeUpdate22(DtSapTransaction dtSapTransaction) ;
	
	public DtSapPostHeader queryInfoAndInsertPostTransaction16(DtSapTransaction dtSapTransaction) ;
}
