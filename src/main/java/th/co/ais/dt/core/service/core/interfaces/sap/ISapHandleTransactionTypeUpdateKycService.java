package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeUpdateKycService {
    DtSapTransaction queryDtSapTransactionById(Long sapTranId);

    DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType);

    DtSapOrderHeader queryInfoAndInsertSaleOrder(DtSapTransaction dtSapTransaction);

    DtSapOrderHeader callSaleOrderApi(DtSapOrderHeader dtSapOrderHeader);

    void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);

    void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader);
}
