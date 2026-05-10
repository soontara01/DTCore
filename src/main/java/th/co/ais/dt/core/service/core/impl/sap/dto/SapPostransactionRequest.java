package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostFinalcial;
import th.co.ais.dt.entity.sap.DtSapPostGoodsMovement;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapPostVoid;

@Data
public class SapPostransactionRequest {
     private String PARTNER ;
     private String PARTNER_MSGID ;
     private DtSapPostHeader HEADER;
     private List<DtSapPostSalesItems> SALES_ITEMS;
     private List<DtSapPostDiscountHeader> DISCOUNT_HEADER ;
     private List<DtSapPostTender> TENDER ;
     private List<DtSapPostFinalcial> FINANCIAL ;
     private List<DtSapPostGoodsMovement> GOODS_MOVEMENT ;
     private List<DtSapPostVoid> VOID ;
}
