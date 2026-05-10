package th.co.ais.dt.core.service.core.impl.sap.dto;



import java.util.List;

import lombok.Data;

@Data
public class QueryProductStockResponse {
	
	//res salebff
	private List<QueryStockItem> queryStockItem;
	
	//res sap car
	private QueryProductStockResponse ATP ;
	private List<QueryProductStockResponse> ATP_RESULT_ITEM ;
	private List<QueryProductStockResponse> AVAILABILITY ;
	private String QUANTITY ;
}
