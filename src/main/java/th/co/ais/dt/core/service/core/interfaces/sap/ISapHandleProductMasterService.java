package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.ProductMstSapBean;

@Transactional
public interface ISapHandleProductMasterService {
	public CommonResponseBean mappingProductMaster(ProductMstSapBean request);
	public ProductMstSapBean insertProductMstFromSap(ProductMstSapBean in) throws DataAccessException;
}
