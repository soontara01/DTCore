package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap168ResBean {

	private String MessageType;
	private String MessageClass;
	private String MessageNumber;
	private String MessageDesc;
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String PartnerNameOrigin;
	private String PartnerMessageIDOrigin;
	private String FlagTypeHeader;
	private String MaterialDocument;
	private String MatDocYear;
	private String DocumentDate;
	private String PostingDate;
	private String Delivery;
	private String ReferenceDocumentNumber;
	private String GRGISlip;
	private String Company;
	private List<Sap168ItemResBean> Item;

}
