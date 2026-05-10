package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class SapCancelShoppingCartBean {
	 private String sapReserveNo;

	    private String resultCode;
	    private String resultDescription;
	    private String developerMessage;

}
