package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class ItemsSerialnumberValidation {
	private String MessageType;
    private String MessageDesc;
    private String SerialNumber;
    private String UniqueItemIdentifier;
    private String Material;
    private String ValidFlag;
    private String Plant;
    private String StorageLocation;
    private String StockType;
    private String SoldTo;
    private String DeliveryNumber;
    private String DeliveryDate;
    private String BillingDocument;
    private String BillingDate;
    private String PaymentTerm;
    private String PriceListType;
    private String ItemCat;
    private String UnitPrice;
    private String LegalEntity;
    private String EID1;
    private String EID2;
    
    private String responseBy;

}
