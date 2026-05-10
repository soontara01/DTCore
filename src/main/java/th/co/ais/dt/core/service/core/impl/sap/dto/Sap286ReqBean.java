package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class Sap286ReqBean {
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String MaterialNumber;
	private String Plant;
	private String StorageLocation;
	private String MaterialType;
	private String MaterialGroup;
	private String BrandCode;
	private String Model;
	private String CheckingRule;
	private String NoZero;
}
