package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class Sap204ItemReqBean {
	private String PurchaseOrderItem ;
	private String DeletionIndicator ;
	private String ShortText ;
	private String Material ;
	private String Plant ;
	private String StorageLocation ;
	private String ReqTrackingNumber ;
	private String OrderQuantity ;
	private String OrderUnit ;
	private String StockType ;
	private String ItemCategory ;
	private String AcctAssignmentCat ;
	private String ConfirmationControlKey ;
	private String Supplier ;
	private String SCSupplier ;
	private String ReturnItem ;
	private String IssueStorageLocation ;
	private String LegacySystem ;
	private String LegacyRequestNumber ;
	private String LegacyItemNumber ;
	//private String PricePerUnit ;
	private String DeliveryDate ;
	private String shipToBP;

}