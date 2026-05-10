package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeMonitorAndApproveService {
	public DtSapOrderHeader queryInfoAndInsertSalesOrderTransactionUpdate(DtSapTransaction dtSapTransaction) throws DataAccessException;
    public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType );
    public DtSapOrderHeader callPostTransactionApi(DtSapOrderHeader dtSapOrderHeader);
    public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);
    public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader);
    }
