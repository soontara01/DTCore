package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class QueryProductStockRequest {
	
	//input sale bff
	private String locationCode;
	private String companyCode;
	private String matCode;
	private String unitName;
	
	private String creationDate;
	private String requestedAvailabilityDate;
	private String requestedQueryStockDate;
	private List<SearchCriteriaQueryProductStock> searchCriteria;
	
	
	//input sap car
	private List<QueryProductStockRequest> ARTICLE_ID ;
	private List<QueryProductStockRequest> ItemSource ;
	
	private String ItemID ;
	private String unitSap ;
	private String Source ;
	private List<String> listMatCode;
}



