package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeCancelDepositService {
	public DtSapOrderHeader queryInfoAndInsertSalesOrderTransactionCancel(DtSapTransaction dtSapTransaction) throws DataAccessException;
    public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType );
    public DtSapOrderHeader callPostTransactionApi(DtSapOrderHeader dtSapOrderHeader);
    public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader);
    public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);
    public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader);
    public DtSapPostHeader queryInfoAndInsertPostTransactionDepositCancel(DtSapTransaction dtSapTransaction) throws DataAccessException;
    public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);
    public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);
    public DtSapOrderHeader queryInfoAndInsertSalesOrderTransactionCancelCNMemo(DtSapTransaction dtSapTransaction) throws DataAccessException ;
    public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader)  ;
    public DtSapOrderHeader unLockOrderBeforeUpdate17(DtSapTransaction dtSapTransaction) throws DataAccessException ;
}
