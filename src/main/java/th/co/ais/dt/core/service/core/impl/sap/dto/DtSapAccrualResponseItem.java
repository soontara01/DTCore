package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class DtSapAccrualResponseItem {

	private String MessageType;
	private String MessageDesc;
	private String AccrualObjectNumber;
	private String DocumentNumber;
	
}
