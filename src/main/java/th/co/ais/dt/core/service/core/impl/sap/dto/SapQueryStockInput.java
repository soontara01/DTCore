package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SapQueryStockInput {
	
	    String MessageID;
	    String PartnerName;
	    String PartnerMessageID;
	    List<String> MaterialNumber;
	    List<String> Plant;
	    List<String> StorageLocation;
	    List<String> MaterialType;
	    List<String> MaterialGroup;
	    List<String> BrandCode;
	    List<String> Model;
	    String Special_Stock_Key;
	    String NoZero;
}
