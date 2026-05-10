package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SapQueryStockOutput {

	String MessageID;
	String PartnerName;	
	String PartnerMessageID;
	String MessageType;
	String MessageClass;
	String MessageNumber;
	String MessageDescription;
	List<SapQueryStockItems> StockItem;
	
	
	//response to jboss
	String resSap;
	String quantity;
}
