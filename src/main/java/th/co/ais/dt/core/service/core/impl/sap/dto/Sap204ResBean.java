package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class Sap204ResBean {
    private String MessageType;
    private String MessageClass;
    private String MessageNumber;
    private String MessageDesc;
    private String MessageID;
    private String PartnerName;
    private String PartnerMessageID;
    private String StockTransportOrder;
    private String DeliveryOrder;

}
