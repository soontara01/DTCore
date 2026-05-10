package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;


@Data
public class InsertSapTransactionAndQueueCloudBean {
	private String queueName ;
	private String docNo ;
	private String company ; 
	private String transactionType ; 
	private String userId ;
	private String docNoOptional ;
	
	//type 58
	private String materialNumber ;
	private String salesUnit ;
	private String quantity ;
	private String deliveryPriority ;
	
	//type59
	private String hFSHName1 ;
	private String hFSHName2 ;
	private String hFSHName3 ;
	private String hFSHName4 ;
	private String hFSHStreet ;
	private String hFSHStreet2 ;
	private String hFSHStreet3 ;
	private String hFSHStreet4 ;
	private String hFSHOtherCity ;
	private String hFSHDistrict ;
	private String hFSHCity ;
	private String hFSHPostalCode ;
	private String hFSHRegion ;
	private String hFSHCountry ;
	private String hFSHTelephone ;
	private String hFSHCustomerNumber ;
	
	//res
	private String sapTranId ; 
	private String errMessage ;
	private String resultCode ;

}
