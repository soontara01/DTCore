package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class SearchCriteriaQueryProductStock {
	
	private String stockType;
	private List<LocationDestQueryProductStock> locationDest;
	private LocationSourceQueryProductStock locationSource;
	private StockLevelQueryProductStock stockLevel;
	private StockItemQueryProductStock stockItem;
    
}
