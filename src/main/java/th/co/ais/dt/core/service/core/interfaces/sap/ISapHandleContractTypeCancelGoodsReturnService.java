package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.entity.pm.CreditNote;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;

import java.util.List;

@Transactional
public interface ISapHandleContractTypeCancelGoodsReturnService {
    
    DtSapContractTrans queryDtSapContractTransByKey(Long sapContractId);

    DtSapAccrualHeader queryInfoAndInsertAccrual(DtSapContractTrans dtSapContractTrans);

    DtSapUniversalHeader queryInfoAndInsertUniversal(DtSapContractTrans dtSapContractTrans);

    DtSapContractTransLog insertSapContractTransLog(
            DtSapContractTrans dtSapContractTrans, 
            DtSapAccrualHeader dtSapAccrualHeader, 
            DtSapUniversalHeader dtSapUniversalHeader, 
            String receiptNum
    );

    DtSapAccrualHeader callCancelGoodsReturnAccrualApi(DtSapAccrualHeader dtSapAccrualHeader, String cancelReceiptNum);

    DtSapUniversalHeader callCancelGoodsReturnUniversalApi(DtSapUniversalHeader dtSapUniversalHeader, String cancelReceiptNum);

    String getReceiptNumFromCnNum(String docNo, String company);
    
    List<CreditNote> queryCnReceipt(String docNo, String company);

    void updateDtSapAccrualHeader(DtSapAccrualHeader dtSapAccrualHeader);

    void updateDtSapUniversalHeader(DtSapUniversalHeader dtSapUniversalHeader);

    void updateDtSapContractTransLog(DtSapContractTransLog dtSapContractTransLog);

    void updateDtSapContractTransAfterWorkflow(DtSapContractTrans dtSapContractTrans);


}
