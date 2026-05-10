package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class SerialnumberValidation {
	private String MessageDesc;
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String MessageResCallSAP;
	private List<ItemsSerialnumberValidation>  Items;
}
