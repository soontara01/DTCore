package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.core.service.core.impl.sap.dto.InsertSapTransactionAndQueueCloudBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.ResqueryInfoAndInsertSaleorder5859;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapTransaction;

@Transactional
public interface ISapHandleTransactionTypePrebookNcpInfoService {
	public ResqueryInfoAndInsertSaleorder5859 queryInfoAndInsertSaleorder58(DtSapTransaction dtSapTransaction , InsertSapTransactionAndQueueCloudBean input) ;
	public ResqueryInfoAndInsertSaleorder5859 queryInfoAndInsertSaleorder59(DtSapTransaction dtSapTransaction , InsertSapTransactionAndQueueCloudBean input) ;
	public DtSapTransaction insertSapTransaction(String docNo , String company , Long transactionType , String userId)  ;
	public void updateDtSapOrderItem(DtSapOrderItem dtSapOrderItem) ;
	public void updateDtSapOrderHPartnerF(DtSapOrderHPartnerF dtSapOrderHPartnerF) ;

}
