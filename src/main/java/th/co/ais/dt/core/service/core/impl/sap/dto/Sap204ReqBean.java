package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

import java.util.List;

@Data
public class Sap204ReqBean {
    private String MessageID;
    private String PartnerName;
    private String PartnerMessageID;
    private String ShipToBP;
    private String StockTransportOrderNumber;
    private String CompanyCode;
    private String LocationCode;
    private String PurchDocCategory;
    private String PurchasingDocType;
    private String PurchOrganiztion;
    private String PurchasingGroup;
    private String DocumentDate;
    //private String YourReference;
    private String SupplyingPlant;
    private String CollectiveNo;
    private String OurReference;
    private String DeliveryIndicator;
    private List<Sap204ItemReqBean> ITEM;


}