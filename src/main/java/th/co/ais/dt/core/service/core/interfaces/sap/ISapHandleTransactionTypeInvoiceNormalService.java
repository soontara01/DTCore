package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeInvoiceNormalService {
    DtSapTransaction queryDtSapTransactionById(Long sapTranId);

    DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType);

    DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction);

    DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);

    void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);

    void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);
    
    public List<DtSapCancelReserve> cancelReserveInvoiceNormal(DtSapTransaction dtSapTransaction) ;
    
    public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve) ;
    
    public void updatedtSapCancelReserve(DtSapCancelReserve dtSapCancelReserve) ;
    
    public DtSapPostHeader queryInfoAndInsertPostTransactionCancelSameDay(DtSapTransaction dtSapTransaction)  ;
}
