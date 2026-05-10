package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap285ReqBean {
	private String MessageID ;
	private String PartnerName ;
	private String PartnerMessageID ;
	private String Mode ;
	private String StockTransportOrderNumber ;
	private String CompanyCode ;
	private String PurchasingDocType ;
	private String SupplyingPlant ;
	private String PurchOrganiztion ;
	private String PurchasingGroup ;
	private String DocumentDate ;
	private String YourReference ;	
	private String OurReference ;
	private List<Sap285ItemReqBean> Item ;
	

}
