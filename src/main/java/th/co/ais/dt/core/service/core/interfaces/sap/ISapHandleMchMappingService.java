package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.DtSapMchMapping;

@Transactional
public interface ISapHandleMchMappingService {
    CommonResponseBean mappingMatGroupProduct(DtSapMchMapping dtSapMchMapping);
}
