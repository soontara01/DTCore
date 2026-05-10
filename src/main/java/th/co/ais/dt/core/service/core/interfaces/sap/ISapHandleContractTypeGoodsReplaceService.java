package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;

@Transactional
public interface ISapHandleContractTypeGoodsReplaceService {

	DtSapContractTrans queryDtSapContractTransByKey(Long sapContractId);

	void updateDtSapContractTransAfterWorkflow(DtSapContractTrans dtSapContractTrans);

	List<DtSapContractTransLog> queryContractGoodsReplace(String company, String docNo);

	void insertDtSapContractTransLog(DtSapContractTrans dtSapContractTrans, DtSapContractTransLog goodsReplaceInfo);


}
