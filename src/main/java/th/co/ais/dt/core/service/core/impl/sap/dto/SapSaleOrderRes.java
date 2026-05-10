package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class SapSaleOrderRes {
	
	  private String MessageID ;
	  private String PartnerName;
	  private String PartnerMessageID;
	  private String MessageType;
	  private String MessageClass;
	  private String MessageNumber;
	  private String MessageDesc;
	  private String MessageVariable1;
	  private String MessageVariable2;
	  private String MessageVariable3;
	  private String MessageVariable4;
	  private String SalesOrderDocument;
	  private String CustomerReference;
	  private List<SapSaleOrderRes> Message ;
	  private SapSaleOrderRes fault ;
	  private String faultstring ;
	  private SapSaleOrderRes detail ;
	  private String errorcode ;

}
