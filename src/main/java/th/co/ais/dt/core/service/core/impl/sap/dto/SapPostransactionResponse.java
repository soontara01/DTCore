package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class SapPostransactionResponse {
	private String MSGTYP ;
	private String MESSAGE ;
	private String MSGID ;
	private String PARTNER ;
	private String PARTNER_MSGID ;
	private SapPostransactionResponseItem ITEMS ;

 
}
