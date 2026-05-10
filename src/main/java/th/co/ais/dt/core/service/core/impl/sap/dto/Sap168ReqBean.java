package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap168ReqBean {
	
	private Sap168ReqBean body ;
	private String MessageID ;
	private String PartnerName ;
	private String PartnerMessageID ;
	private String FlagTypeHeader ;
	private String DocumentDate ;
	private String PostingDate ;
	private String ReferenceDocumentNumber ;
	private List<Sap168ItemReqBean> Item ;
	
	
	private Sap168ReqBean header ;
	private String version ;
	private String timestamp ;
	private String orgService ;
	private String scope ;
	private String from ;
	private String channel ;
	private String agent ;
	private String broker ;
	private String useCase ;
	private String useCaseStep ;
	private String useCaseAge ;
	private String functionName ;
	private String messageType ;
	private String session ; 
	private String transaction ;
	private String communication ;
	//private String groupTags ;
	private String returnedError ;
	private String initUri ;
	private String initMethod ;
	private String tmfSpec ;
	private String baseApiVersion ;
	private String schemaVersion ;
	
	private Sap168ReqBean identity ;
	//private List<String> public ;
	
	
}
