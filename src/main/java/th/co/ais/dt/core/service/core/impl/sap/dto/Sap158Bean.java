package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap158Bean {

	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String SalesOrganization;
	private String DistributionChannel;
	private String CustomerPOTypeCode;
	private String CustomerPurchaseOrderType;
	private String DeliveryNumber;
	private String DeliveryDate;
	private List<Sap158ItemBean> Item; 
}
