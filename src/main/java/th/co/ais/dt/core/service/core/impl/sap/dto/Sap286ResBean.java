package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap286ResBean {
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String MessageType;
	private String MessageClass;
	private String MessageNumber;
	private String MessageDescription;
	private List<Sap286StockItemBean> StockItem;
}
