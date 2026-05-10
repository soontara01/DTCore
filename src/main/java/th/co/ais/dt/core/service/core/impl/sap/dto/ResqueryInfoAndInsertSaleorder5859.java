package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;

@Data
public class ResqueryInfoAndInsertSaleorder5859 {
         private DtSapOrderHeader dtSapOrderHeader ;
         private DtSapOrderItem dtSapOrderItemForRollBack ;
         DtSapOrderHPartnerF dtSapOrderHPartnerForRollBack ;
}
