package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap285ResBean {
	private String MessageType ;
	private String MessageClass ;
	private String MessageNumber ;
	private String MessageDescription ;
	private String LegacyItemNumber ;
	private String MessageID ;
	private String PartnerName ;
	private String PartnerMessageID ;
	private String StockTransportOrderNumber ;
	private String DeliveryOrderNumber ;
	private String MaterialDocumentNumber ;
}
