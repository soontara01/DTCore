package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class SerialnumberValidationCmBean {
	//request
	private String systemCode;
	private String serialNo;
	
	//response
	private String resultCode;
	private String developerMessage;
	private List<SerialnumberValidationCmValue> objectValue;
}


