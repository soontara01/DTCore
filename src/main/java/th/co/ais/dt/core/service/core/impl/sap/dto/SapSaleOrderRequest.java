package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;
import th.co.ais.dt.entity.sap.DtSapOrderHCon;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHReturn;
import th.co.ais.dt.entity.sap.DtSapOrderHText;
import th.co.ais.dt.entity.sap.DtSapOrderItem;

@Data
public class SapSaleOrderRequest {
	
	private String PartnerName ;
	private String PartnerMessageID ;
	private String ChangeMode ;
	private String SalesOrderDocument ;
	private String SDDocumentCategory ;
	private String SalesDocType ;
	private String SalesOrganization ;
	private String DistributionChannel ;
	private String Division ;
	private String Branch ;
	private String CustomerReference ;
	private String CustomerRefDate ;
	private String YourReference ;
	private String CustomerPurchaseOrderType ;
	private String ShippingConditions ;
	private String PriceListType ;
	private String DeliveryBlock ;
	private String BillingBlock ;
	private String PaymentTerms ;
	private String DocumentCurrency ;
	private String CustomerGroup ;
	private String CustomerGroup1 ;
	private DtSapOrderHReturn Return ;
	private String CompleteDlv ;
	private String OrderCombination ;
	
	private List<DtSapOrderHPartnerF> PartnerFunction ;
	private List<DtSapOrderHText> Text ;
	private List<DtSapOrderHCon> Condition  ;
	private List<DtSapOrderItem> Item   ;

}
