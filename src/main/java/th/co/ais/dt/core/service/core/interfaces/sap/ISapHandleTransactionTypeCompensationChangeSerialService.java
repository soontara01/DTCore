package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;

@Transactional
public interface ISapHandleTransactionTypeCompensationChangeSerialService {
    DtSapTransaction queryDtSapTransactionById(Long sapTranId);

    DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType);

    DtSapTransactionTypeCodeConfig queryDtSapTransactionTypeCodeConfig(Long transactionType);

    DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction, DtSapTransactionTypeCodeConfig config);

    DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader);

    void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction);

    void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader);
}
