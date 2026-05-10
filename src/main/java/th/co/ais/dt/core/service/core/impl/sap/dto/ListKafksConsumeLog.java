package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;
import th.co.ais.dt.controller.dto.ProductMstSapBean;

@Data
public class ListKafksConsumeLog {
	
	private List<String> listTableId ;
	private ProductMstSapBean body ;

}
