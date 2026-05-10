package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ReqBean;

@Transactional
public interface ISap168Service {

	void callSapGoodsMovementCreate_0168(Sap168ReqBean request, String userId);

}
