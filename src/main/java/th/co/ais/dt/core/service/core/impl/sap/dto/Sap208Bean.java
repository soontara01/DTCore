package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;
@Data
public class Sap208Bean {
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String CustomerPOTypeCode;
	private String CustomerPOTypeDesc;
	private String SalesOrg;
	private String DisChannel;
	private String ORGOrder;
	private String RMANumber;
	private String CustomerRef;
	private String ReturnDO;
	private String MaterialDoc;
	private List<Sap208Bean> Item; 
	
	//Item
	private String ORGSOItem;
	private String InspectionNo;
	private String ItemNo;
	private String MatCode;
	private String SerialNo;
	private String IUID;
	private String DocType;
	private String DocTypeName;
	private String SubDocument;
}
