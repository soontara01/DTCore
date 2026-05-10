package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.controller.dto.StdPriceBean;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;

@Transactional
public interface ISapHandleTransactionTypeDepositPartnerService {
	
	  public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType ) ;
	  public DtSapPostHeader query6InfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) ;
	  public DtSapPostHeader query7InfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) ;
	  public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) ;
	  public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) ;
	  public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) ;
	  public DtSapTransaction queryDtSapTransactionById(Long sapTranId) ;
	  public DtSapOrderHeader queryInfoAndInsertSaleorderDepositshop(DtSapTransaction dtSapTransaction) ;
	  public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader) ;
	  public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) ;
	  public DtSapOrderHeader queryInfoAndInsertSaleorderDepositPartner(DtSapTransaction dtSapTransaction) ;
	//   public DtSapOrderHeader queryInfoAndUpdateSaleorderDepositPartner(DtSapTransaction dtSapTransaction);
	  public DtSapOrderHeader queryInfoAndUpdateSaleorderDepositshopType13(DtSapTransaction dtSapTransaction) ;
	  public DtSapOrderHeader queryInfoAndUpdateSaleorderDepositPartner(DtSapTransaction dtSapTransaction);
	  public List<QuerySaleOrderBean> querySaleOrderSq3(String docNo , String company,Long transactionType) ;
	  public List<StdPriceBean> getListPriceByCriteria(long productId , String groupPrice , String priceDate  , String paymentMethod) ;
	  public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) ;
	  public DtSapPostHeader query6InfoAndInsertPostTransactionCancelDepositPartner(DtSapTransaction dtSapTransaction);
	  public DtSapPostHeader query7InfoAndInsertPostTransactionCancelDepositPartner(DtSapTransaction dtSapTransaction);
	  public DtSapPostHeader query6InfoAndInsertPostTransactionDepositPartner(DtSapTransaction dtSapTransaction);
	  public DtSapOrderHeader queryInfoAndInsertSaleorderDepositPartnerUpdateOrder(DtSapTransaction dtSapTransaction);
	  public DtSapPostHeader query7InfoAndInsertPostTransactionCancelDepositPartnerDiffDealer(DtSapTransaction dtSapTransaction ) ;

}
