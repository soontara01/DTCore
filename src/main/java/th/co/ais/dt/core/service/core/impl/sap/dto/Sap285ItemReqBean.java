package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap285ItemReqBean {
	private String LegacySystem ;
	private String LegacyRequestNumber ;
	private String LegacyItemNumber ;
	private String Material ;
	private String ReceivingPlant ;
	private String ReceivingStorageLocation ;
	private String OrderQuantity ;
	private String OrderUnit ;
	private String IssuingStorageLocation ;
	private String ShortText ;
	private String RequirementTrackingNumber ;
	private String NameofRequisitioner ;
	private String DeliveryDate ;
	private List<Sap285ItemSerialReqBean> Serial ;
	

}
