package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;

@Transactional
public interface ISapHandleTransactionTypeSaleNormalService {
    public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType ) ;
    public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction, Long fixDocTrans) ;
    public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) ;
    public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) ;
    public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) ;
    public DtSapTransaction queryDtSapTransactionById(Long sapTranId) ;
    public List<DtSapTransaction>  getDtSapTransactionStatusF() throws DataAccessException ;
    public List<DtSapPostHeader> getDtSapPostHeaderBySapTranId(Long sapTranId) ;
    public DtSapOrderHeader queryInfoAndInsertSaleorder1819(DtSapTransaction dtSapTransaction) ;
    public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) ;
    public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader) ;
    public DtSapOrderHeader queryInfoAndInsertSaleorder14(DtSapTransaction dtSapTransaction) ;
    public DtSapOrderHeader queryInfoAndInsertSaleorder28(DtSapTransaction dtSapTransaction) ;
	public DtSapOrderHeader queryInfoAndInsertSaleorder24(DtSapTransaction dtSapTransaction);
	public List<QuerySaleOrderBean> querySaleOrderSql1819(String docNo , String company,Long transactionType) ;
	public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader);
	public DtSapOrderHeader queryInfoAndInsertSaleorder25(DtSapTransaction dtSapTransaction);
	public DtSapPostHeader queryInfoAndInsertPostTransaction14(DtSapTransaction dtSapTransaction) ;
	public void updatePostTransactionBeforeCallSapType14(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) ;
	public DtSapPostHeader queryInfoAndInsertPostTransaction14WithoutSaleOrder(DtSapTransaction dtSapTransaction) ;
	public List<DtSapCancelReserve> cancelReserveSale(DtSapTransaction dtSapTransaction) ;
	public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve) ;
	public List<DtSapOrderHeader>  queryDtSapOrderHeaderBySapTranId(Long sapTransId) throws DataAccessException ;
	public DtSapOrderHeader queryInfoAndInsertSaleorder67(DtSapTransaction dtSapTransaction) ;
	public void updatedtSapCancelReserve(DtSapCancelReserve dtSapCancelReserve) ;
	public List<Long> listCmrNoBilling() ; 
	public List<DtSapOrderHeader> unRejectItemAndBlockOrder(Long sapTransId);
	public void updateCmrForResend(Long sapTransId) ;
	public DtSapOrderHeader unLockOrderBeforeUpdate14(DtSapTransaction dtSapTransaction) ;
	public DtSapOrderHeader unLockOrderBeforeUpdate28(DtSapTransaction dtSapTransaction) ;
	public DtSapOrderHeader unLockOrderBeforeUpdate67(DtSapTransaction dtSapTransaction) ;
	public DtSapOrderHeader unLockOrderBeforeUpdate24(DtSapTransaction dtSapTransaction) ;
	public DtSapOrderHeader unLockOrderBeforeUpdate25(DtSapTransaction dtSapTransaction) ;
	public DtSapOrderHeader queryInfoAndInsertSaleorder25DeliveryBlock(DtSapTransaction dtSapTransaction);
	public DtSapOrderHeader queryInfoAndInsertSaleorder24DeliveryBlock(DtSapTransaction dtSapTransaction) ;

}
