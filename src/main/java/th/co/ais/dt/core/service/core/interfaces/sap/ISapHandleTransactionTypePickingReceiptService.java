package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypePickingReceiptService {
	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) throws DataAccessException;
    public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType );
    public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);
    public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);
    public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);
    public List<DtSapCancelReserve> cancelReserveSale(DtSapTransaction dtSapTransaction);
    public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve);
    public void updatedtSapCancelReserve(DtSapCancelReserve dtSapCancelReserve) ;
}
