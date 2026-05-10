package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class DtSapAccrualResponse {

	private String MessageType;
	private String MessageDesc;
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String Status;
	private String StatusDescription;
	private String Number;
	private String CompanyCode;
	private String AccrualObject;
	private String LegacyDocument;
	private List<DtSapAccrualResponseItem> Item;
}
