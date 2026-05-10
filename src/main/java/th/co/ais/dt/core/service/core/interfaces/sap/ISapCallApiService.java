package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.controller.dto.Sap282ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapAccrualResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryProductStockRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryProductStockResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.SaleOrderNoRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SaleOrderNoResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap204ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap204ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap286ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap286ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapQueryStockOutput;
import th.co.ais.dt.core.service.core.impl.sap.dto.SerialnumberValidation;
import th.co.ais.dt.entity.sap.DtSapAccrualGroupHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualItem;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.entity.util.DtSapWsSerialNumber;
import th.co.ais.dt.entity.util.TdEtlParameter;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;

@Transactional
public interface ISapCallApiService {
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader dtSapPostHeader);
	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) ;
	public Sap168ResBean call168Api(Sap168ReqBean sap168ReqBean) ;
	public Sap282ReqBean call282Api(Sap282ReqBean sap282ReqBean);
	public Sap285ResBean call285Api(Sap285ReqBean sap285ReqBean);
	public Sap204ResBean call204Api(Sap204ReqBean sap204ReqBean);
	public Sap286ResBean call286Api(Sap286ReqBean sap285ReqBean);
	public SerialnumberValidation callSerialnumberValidationApi(SerialnumberValidation input);
	public SapCancelShoppingCartResponse callCancelShoppingCart(SapCancelShoppingCartRequest sapCancelShoppingCartRequest);
	public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve) ;
	public QueryProductStockResponse callQueryProductStockApi(QueryProductStockRequest input);
	public SaleOrderNoResponse getSaleOrderNo(SaleOrderNoRequest input);
	public DtSapUniversalHeader callUniversalApi(DtSapUniversalHeader dtSapOrderHeader);
	public DtSapAccrualHeader callAccrualApi(DtSapAccrualHeader dtSapUniversalHeader);
	public DtSapUniversalHeader callCancelGoodsReturnUniversalApi(DtSapUniversalHeader dtSapUniversalHeader, String cancelReceiptNum);
	public DtSapAccrualHeader callCancelGoodsReturnAccrualApi(DtSapAccrualHeader dtSapAccrualHeader, String cancelReceiptNum);
	public DtSapWsSerialNumber getDtSapWsSerialNumber(String SerialNumber) ;
	public List<QueryPostTransactionBean> getSerialCNInvoice(String serialNo) throws DataAccessException ;
	public List<QueryPostTransactionBean> getSerialCancelInvoice(String serialNo) throws DataAccessException ;
	public DtSapAccrualResponse callAccrualSummaryApi(DtSapAccrualGroupHeader dtSapAccrualGroupHeader,DtSapAccrualHeader sapAccrualHeader,List<DtSapAccrualItem> sapAccrualItems);
	public SapCancelShoppingCartResponse callCancelReserveApiRefac(DtSapCancelReserve dtSapCancelReserve) ;
	public SapQueryStockOutput sapQueryStockNonSerial(QueryProductStockRequest input);
	
}
