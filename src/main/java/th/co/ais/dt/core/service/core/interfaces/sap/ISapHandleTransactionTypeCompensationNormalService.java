package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;

@Transactional
public interface ISapHandleTransactionTypeCompensationNormalService {
    DtSapTransaction queryDtSapTransactionById(Long sapTranId);

    DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType);

    DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction);

    DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);

    void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);

    void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);
}
