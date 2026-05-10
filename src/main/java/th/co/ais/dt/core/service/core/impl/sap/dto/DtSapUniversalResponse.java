package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtSapUniversalResponse {
	private String MessageID;
	private String MessageType;
	private String PartnerName;
	private String PartnerMessageID;
	private List<DtSapUniversalResponseItem> Items;
}
