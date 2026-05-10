package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;

@Transactional
public interface ISapHandleContractTypeGoodsReturnService {

	DtSapContractTrans queryDtSapContractTransByKey(Long sapContractId);

	DtSapUniversalHeader queryInfoAndInsertUniversal(DtSapContractTrans dtSapContractTrans);

	DtSapUniversalHeader callUniversalApi(DtSapUniversalHeader dtSapOrderHeader);

	void updateDtSapUniversalHeader(DtSapUniversalHeader dtSapUniversalHeader);

	void insertDtSapContractTransLog(DtSapContractTrans dtSapContractTrans, DtSapUniversalHeader dtSapUniversalHeader);

	void updateDtSapContractTransAfterWorkflow(DtSapContractTrans dtSapContractTrans);


}
