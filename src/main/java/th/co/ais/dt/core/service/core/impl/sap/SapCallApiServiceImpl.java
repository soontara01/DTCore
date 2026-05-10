package th.co.ais.dt.core.service.core.impl.sap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.DeliveryOrderInbound;
import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.controller.dto.Sap282ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapAccrualRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapAccrualResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapUniversalRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapUniversalResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.ItemsSerialnumberValidation;
import th.co.ais.dt.core.service.core.impl.sap.dto.LocationDestQueryProductStock;
import th.co.ais.dt.core.service.core.impl.sap.dto.LocationSourceQueryProductStock;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryProductStockRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryProductStockResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryStockItem;
import th.co.ais.dt.core.service.core.impl.sap.dto.SaleOrderNoRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SaleOrderNoResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap204ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap204ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ItemReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ItemSerialReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap286ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap286ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapPostransactionRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapPostransactionRequestH;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapPostransactionResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapQueryStockInput;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapQueryStockOutput;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapSaleOrderRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapSaleOrderRes;
import th.co.ais.dt.core.service.core.impl.sap.dto.SearchCriteriaQueryProductStock;
import th.co.ais.dt.core.service.core.impl.sap.dto.SerialnumberValidation;
import th.co.ais.dt.core.service.core.impl.sap.dto.ShoppingCart;
import th.co.ais.dt.core.service.core.impl.sap.dto.StockItemQueryProductStock;
import th.co.ais.dt.core.service.core.impl.sap.dto.StockLevelQueryProductStock;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.entity.sap.DtSapAccrualGroupHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualItem;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapOrderHCon;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHReturn;
import th.co.ais.dt.entity.sap.DtSapOrderHText;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCancel;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapOrderItemSerial;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostDiscountItems;
import th.co.ais.dt.entity.sap.DtSapPostFinalcial;
import th.co.ais.dt.entity.sap.DtSapPostGoodsMovement;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapPostVoid;
import th.co.ais.dt.entity.sap.DtSapReserveLog;
import th.co.ais.dt.entity.sap.DtSapTransferInLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.entity.sap.DtSapUniversalItem;
import th.co.ais.dt.entity.sk.LocMapPlant;
import th.co.ais.dt.entity.util.DtSapWsQueryProductStock;
import th.co.ais.dt.entity.util.DtSapWsSerialNumber;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.entity.util.TdEtlParameter;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHPartnerFDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHReturnDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHTextDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemCancelDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemSerialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostFinalcialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostGoodsMovementDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTaxItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostVoidDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapReserveLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransferInLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalItemDao;
import th.co.ais.dt.repository.interfaces.util.IDtSapWsQueryProductStockDao;
import th.co.ais.dt.repository.interfaces.util.IDtSapWsSerialNumberDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.IProductMstDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@Service
@Slf4j
@AllArgsConstructor
public class SapCallApiServiceImpl implements ISapCallApiService {
	
	private final IDtSapPostHeaderDao dtSapPostHeaderDao ;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao ;
	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao ;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao ;
	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao ;
	private final IDtSapPostTenderDao dtSapPostTenderDao ;
	private final IDtSapPostFinalcialDao dtSapPostFinalcialDao ;
	private final IDtSapPostGoodsMovementDao dtSapPostGoodsMovementDao ;
	private final DTConfig dTConfig;
	private final ILovMasterDao lovMasterDao ;
	
	private final IDtSapOrderHConDao dtSapOrderHConDao ;
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderHPartnerFDao dtSapOrderHPartnerFDao;
	private final IDtSapOrderHTextDao dtSapOrderHTextDao ;
	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	private final IDtSapOrderHReturnDao dtSapOrderHReturnDao;
	
	private final ILocMapPlantDao locMapPlantDao ;
	private final IDtSapTransferInLogDao dtSapTransferInLogDao;
	
	private final IDtSapWsSerialNumberDao dtSapWsSerialNumberDao;
	
	private final IDtSapWsQueryProductStockDao dtSapWsQueryProductStockDao;
	
	private final IDtSapUniversalHeaderDao dtSapUniversalHeaderDao;
	private final IDtSapUniversalItemDao dtSapUniversalItemDao;
	private final IDtSapAccrualHeaderDao dtSapAccrualHeaderDao;
	private final IDtSapAccrualItemDao dtSapAccrualItemDao;
	
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private final IDtSapPostVoidDao dtSapPostVoidDao ;
    
    private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
    
    private final IProductMstDao productMstDao;
    private final IDtSapReserveLogDao dtSapReserveLogDao;
	
	@Override
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader dtSapPostHeader) {
		//DtSapPostHeader res = new DtSapPostHeader();
		
		
		try {
			SapPostransactionRequestH requestH = new SapPostransactionRequestH();
			SapPostransactionRequest request = new SapPostransactionRequest();
			
			DtSapPostHeader sapPostHeader = dtSapPostHeaderDao.getDtSapPostHeaderById(dtSapPostHeader.getSapPostHeaderId()) ;
			List<DtSapPostSalesItems> sapPostSalesItems = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderId(dtSapPostHeader.getSapPostHeaderId()) ;
			
			if(BeanUtil.isNotEmpty(sapPostSalesItems)) {
				for(DtSapPostSalesItems el : sapPostSalesItems) {
					List<DtSapPostDiscountItems> sapPostDiscountItems = new ArrayList<>();
					List<DtSapPostTaxItems> sapPostTaxItems = new ArrayList<>();
					List<DtSapPostDiscountItems> sapPostDiscountItemsQ = new ArrayList<>();
					sapPostDiscountItemsQ = dtSapPostDiscountItemsDao.getDtSapPostDiscountItemsBySaleItemIdId(el.getSapPostSalesItemId());
					for(DtSapPostDiscountItems edis : sapPostDiscountItemsQ) {
						sapPostDiscountItems.add(edis);
					}
					if(BeanUtil.isNotEmpty(sapPostDiscountItems)) {
						el.setDISCOUNT_ITEMS(sapPostDiscountItems);
					}
					
					List<DtSapPostTaxItems> dtSapPostTaxItemsQ = new ArrayList<>();
					dtSapPostTaxItemsQ = dtSapPostTaxItemsDao.getDtSapPostTaxItemsBySaleItemIdId(el.getSapPostSalesItemId());
					for(DtSapPostTaxItems edis : dtSapPostTaxItemsQ) {
						sapPostTaxItems.add(edis);
					}
					
					if(BeanUtil.isNotEmpty(sapPostTaxItems)) {
						el.setTAX_ITEMS(sapPostTaxItems);
					}
					el.setSapPostSalesItemId(null);
					
					int maxl = 18 ;
					if(BeanUtil.isNotEmpty(el.getSERIALNUMBER()) && el.getSERIALNUMBER().length() > maxl ){
						el.setIUID(el.getSERIALNUMBER());
						el.setSERIALNUMBER(null);
					}
				}
			}
			
			List<DtSapPostDiscountHeader> sapPostDiscountHeader = dtSapPostDiscountHeaderDao.getDtSapPostDiscountHeaderByHeaderId(dtSapPostHeader.getSapPostHeaderId());
			List<DtSapPostTender> sapPostTender = dtSapPostTenderDao.getDtSapPostTenderByHeaderId(dtSapPostHeader.getSapPostHeaderId()) ;
			List<DtSapPostFinalcial> sapPostFinalcial = dtSapPostFinalcialDao.getDtSapPostFinalcialByHeaderId(dtSapPostHeader.getSapPostHeaderId());
			List<DtSapPostGoodsMovement> dtSapPostGoodsMovement = dtSapPostGoodsMovementDao.getDtSapPostGoodsMovementByHeaderId(dtSapPostHeader.getSapPostHeaderId());
			
			List<DtSapPostVoid> dtSapPostVoid = dtSapPostVoidDao.getDtSapPostVoidByHeaderId(dtSapPostHeader.getSapPostHeaderId());
			
			request.setPARTNER("DT") ;
			request.setPARTNER_MSGID("DT" + String.valueOf(dtSapPostHeader.getSapPostHeaderId()));
			
			// replace date
			DtSapPostHeader sapPostHeaderTmp = new DtSapPostHeader();
			BeanUtils.copyProperties(sapPostHeader,sapPostHeaderTmp);
			
			Date now = new Date();
			SimpleDateFormat datetext = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			SimpleDateFormat datetext2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
			
			
			sapPostHeaderTmp.setBUSINESSDAYDATE(datetext.format(now));    //YYYY-MM-DD
			sapPostHeaderTmp.setBEGINDATETIMESTAMP(datetext2.format(now));  // YYYY-MM-DD\"T\"HH24:mi:ss
			sapPostHeaderTmp.setENDDATETIMESTAMP(datetext2.format(now))  ;  // YYYY-MM-DD\"T\"HH24:mi:ss;
			//
			
			request.setHEADER(sapPostHeaderTmp);
			if(BeanUtil.isNotEmpty(sapPostSalesItems)) {
				request.setSALES_ITEMS(sapPostSalesItems);
			}
			
			if(BeanUtil.isNotEmpty(sapPostDiscountHeader)) {
				request.setDISCOUNT_HEADER(sapPostDiscountHeader);
			}
			
			if(BeanUtil.isNotEmpty(sapPostTender)) {
				request.setTENDER(sapPostTender);
			}
			
			if(BeanUtil.isNotEmpty(sapPostFinalcial)) {
				request.setFINANCIAL(sapPostFinalcial);
			}
			
			if(BeanUtil.isNotEmpty(dtSapPostGoodsMovement)) {
				request.setGOODS_MOVEMENT(dtSapPostGoodsMovement);
			}
			
			if(BeanUtil.isNotEmpty(dtSapPostVoid)) {
				request.setVOID(dtSapPostVoid);
			}
			
			requestH.setITEMS(request);
			
			
			Gson g = new Gson();
			log.info(g.toJson(requestH));
			
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_POS_TRANS_TIMEOUT", "SAPS4_POS_TRANS_TIMEOUT", null, null, "Y");
	        List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_POST_KEY", "SAP_S4_API_POST_KEY", null, null, "Y");
	        String url = "";
	        if(BeanUtil.isNotEmpty(apiKeyy.get(0).getLovAttribute03())) {
	        	properties.put("APIKey", apiKeyy.get(0).getLovAttribute03());
	        	url = dTConfig.getUrl().getSappostransactioninter();
	        }else {
	        	properties.put("Ocp-Apim-Subscription-Key", apiKeyy.get(0).getLovVal());
		        properties.put("x-api-key", apiKeyy.get(0).getLovVal());
		        url = dTConfig.getUrl().getSappostransaction();
	        }
	        
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
			String resSap = req.HttpClient(properties, url, g.toJson(requestH), "POST", timeoutS) ;
			
//			resSap = "{\r\n"
//					+ "	\"MSGTYP\": \"S\",\r\n"
//					+ "	\"MESSAGE\": \"Posted successfull\",\r\n"
//					+ "	\"MSGID\": \"0000000000001013\",\r\n"
//					+ "	\"PARTNER\": \"\",\r\n"
//					+ "	\"PARTNER_MSGID\": \"\",\r\n"
//					+ "	\"ITEMS\": {\r\n"
//					+ "		\"MSGTYP\": \"S\",\r\n"
//					+ "		\"MESSAGE\": \"Posted successfull\",\r\n"
//					+ "		\"KEY01\": \"\",\r\n"
//					+ "		\"KEY02\": \"\",\r\n"
//					+ "		\"KEY03\": \"\",\r\n"
//					+ "		\"KEY04\": \"\"\r\n"
//					+ "	}\r\n"
//					+ "}";
			
//			String sap_lock = "{\r\n"
//					+ "	\"MSGTYP\": \"E\",\r\n"
//					+ "	\"MESSAGE\": \"Data from store L012, posting date 20251002 is locked by user DC_CPI\",\r\n"
//					+ "	\"MSGID\": \"0000000000009155\",\r\n"
//					+ "	\"PARTNER\": \"DT\",\r\n"
//					+ "	\"PARTNER_MSGID\": \"DT1234567890\",\r\n"
//					+ "	\"ITEMS\": {\r\n"
//					+ "		\"MSGTYP\": \"E\",\r\n"
//					+ "		\"MESSAGE\": \"Data from store L012, posting date 20251002 is locked by user DC_CPI\",\r\n"
//					+ "		\"KEY01\": \"\",\r\n"
//					+ "		\"KEY02\": \"\",\r\n"
//					+ "		\"KEY03\": \"\",\r\n"
//					+ "		\"KEY04\": \"\"\r\n"
//					+ "	}\r\n"
//					+ "}";
			
			if(resSap != null) {
				log.info(resSap);
				SapPostransactionResponse ress = null ;
				try {
					ress = g.fromJson(resSap, SapPostransactionResponse.class) ;
					if(ress != null) {
						
						
						if(ress.getITEMS()!=null) {
							dtSapPostHeader.setRES_ITEMS_KEY01(ress.getITEMS().getKEY01());
							dtSapPostHeader.setRES_ITEMS_KEY02(ress.getITEMS().getKEY02());
							dtSapPostHeader.setRES_ITEMS_KEY03(ress.getITEMS().getKEY03());
							dtSapPostHeader.setRES_ITEMS_KEY04(ress.getITEMS().getKEY04());
							dtSapPostHeader.setRES_ITEMS_MESSAGE(ress.getITEMS().getMESSAGE());
							dtSapPostHeader.setRES_ITEMS_MSGTYP(ress.getITEMS().getMSGTYP());
						}
						
						
						
						dtSapPostHeader.setRES_MESSAGE(ress.getMESSAGE());
						dtSapPostHeader.setRES_MSGID(ress.getMSGID());
						dtSapPostHeader.setRES_MSGTYP(ress.getMSGTYP());
						dtSapPostHeader.setRES_PARTNER(ress.getPARTNER());
						dtSapPostHeader.setRES_PARTNER_MSGID(ress.getPARTNER_MSGID());
						if(BeanUtil.isNotEmpty(ress.getMSGTYP()) && ress.getMSGTYP().equals("S") ) {
							dtSapPostHeader.setStatus("S");
						}else if(BeanUtil.isNotEmpty(ress.getMSGTYP()) && !ress.getMSGTYP().equals("S")) {
							if(ress.getMSGTYP().equals("E") && BeanUtil.isNotEmpty(ress.getMESSAGE()) && ress.getMESSAGE().indexOf("locked by user") > 0 ) {
								dtSapPostHeader.setStatus("S");
							}else {
								dtSapPostHeader.setStatus("F");
							}
							
						}else {
							dtSapPostHeader.setStatus("R");
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					dtSapPostHeader.setStatus("R");
				}
			}
			else {
				dtSapPostHeader.setStatus("R");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			dtSapPostHeader.setStatus("R");
		}
		
		return dtSapPostHeader;
	}
	
	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader dtSapOrderHeaderRes = new DtSapOrderHeader();
		try {
			SapSaleOrderRequest request = new SapSaleOrderRequest();
			//List<DtSapOrderHeader> listDtSapOrderHeader = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(dtSapOrderHeader.getSapTranId());
			DtSapOrderHeader dtSapOrderHeaderQ = dtSapOrderHeaderDao.getByLongPrimaryKey(dtSapOrderHeader.getSapOrderHeaderId());

			request.setPartnerName(dtSapOrderHeaderQ.getPartnerName()); 
			request.setPartnerMessageID("DT" +String.valueOf(dtSapOrderHeaderQ.getSapOrderHeaderId()) );
			request.setChangeMode(dtSapOrderHeaderQ.getChangeMode());
			request.setSalesOrderDocument(dtSapOrderHeaderQ.getSalesOrderDocument());
			request.setSDDocumentCategory(dtSapOrderHeaderQ.getSDDocumentCategory());
			request.setSalesDocType(dtSapOrderHeaderQ.getSalesDocType());
			request.setSalesOrganization(dtSapOrderHeaderQ.getSalesOrganiztion());
			request.setDistributionChannel(dtSapOrderHeaderQ.getDistributionChannel());
			request.setDivision(dtSapOrderHeaderQ.getDivision());
			request.setBranch(dtSapOrderHeaderQ.getBranch());
			request.setCustomerReference(dtSapOrderHeaderQ.getCustomerReference());
			request.setCustomerRefDate(dtSapOrderHeaderQ.getCustomerRefDate());
			request.setYourReference(dtSapOrderHeaderQ.getYourReference());
			request.setCustomerPurchaseOrderType(dtSapOrderHeaderQ.getCustomerPurchaseOrderType());
			request.setShippingConditions(dtSapOrderHeaderQ.getShippingConditions());
			request.setPriceListType(dtSapOrderHeaderQ.getPriceListType());
			request.setDeliveryBlock(dtSapOrderHeaderQ.getDeliveryBlock());
			request.setBillingBlock(dtSapOrderHeaderQ.getBillingBlock());
			request.setPaymentTerms(dtSapOrderHeaderQ.getPaymentTerms());
			request.setDocumentCurrency(dtSapOrderHeaderQ.getDocumentCurrency());
			request.setCustomerGroup(dtSapOrderHeaderQ.getCustomerGroup());
			request.setCustomerGroup1(dtSapOrderHeaderQ.getCustomerGroup1());
			request.setOrderCombination(dtSapOrderHeaderQ.getOrderCombination());
			request.setCompleteDlv(dtSapOrderHeaderQ.getCompleteDlv());
			
			List<DtSapOrderHPartnerF> listDtSapOrderHPartnerF = dtSapOrderHPartnerFDao.queryDtSapOrderHPartnerFBySapOrderHeaderIdForsap(dtSapOrderHeaderQ.getSapOrderHeaderId());
			if(BeanUtil.isNotEmpty(listDtSapOrderHPartnerF)) {
				request.setPartnerFunction(listDtSapOrderHPartnerF) ;;
			}
			
			List<DtSapOrderHText> listDtSapOrderHText = dtSapOrderHTextDao.queryDtSapOrderHTextBySapOrderHeaderIdForsap(dtSapOrderHeaderQ.getSapOrderHeaderId());
			if(BeanUtil.isNotEmpty(listDtSapOrderHText)) {
				request.setText(listDtSapOrderHText);
			}
			
			List<DtSapOrderHCon> listDtSapOrderHCon = dtSapOrderHConDao.queryDtSapOrderHContBySapOrderHeaderIdForsap(dtSapOrderHeaderQ.getSapOrderHeaderId());
			if(BeanUtil.isNotEmpty(listDtSapOrderHCon)) {
				request.setCondition(listDtSapOrderHCon);
			}
			
			List<DtSapOrderHReturn> listDtSapOrderHReturn = dtSapOrderHReturnDao.queryDtSapOrderHReturnBySapOrderHeaderIdForsap(dtSapOrderHeaderQ.getSapOrderHeaderId());
			if(BeanUtil.isNotEmpty(listDtSapOrderHReturn)) {
				request.setReturn(listDtSapOrderHReturn.get(0));
			}
			
			List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderIdForsap(dtSapOrderHeaderQ.getSapOrderHeaderId());
			if(BeanUtil.isNotEmpty(listDtSapOrderItem)) {
				for(DtSapOrderItem item : listDtSapOrderItem) {
					
					List<DtSapOrderItemSerial> listDtSapOrderItemSerial = dtSapOrderItemSerialDao.queryDtSapOrderItemSerialBySapOrderItemIdForsap(item.getSapOrderItemId());
					if(BeanUtil.isNotEmpty(listDtSapOrderItemSerial)) {
						int maxl = 18 ;
						for(DtSapOrderItemSerial el : listDtSapOrderItemSerial) {
							if(BeanUtil.isNotEmpty(el.getNumberOfSerialNumber()) && el.getNumberOfSerialNumber().length() > maxl ){
								el.setIUIDCustomerRelevant(el.getNumberOfSerialNumber());
								el.setNumberOfSerialNumber(null);
							}
						}
						item.setSerial(listDtSapOrderItemSerial);
					}
					
					List<DtSapOrderItemCancel> listDtSapOrderItemCancel = dtSapOrderItemCancelDao.queryDtSapOrderItemCancelBySapOrderItemIdForsap(item.getSapOrderItemId());
					if(BeanUtil.isNotEmpty(listDtSapOrderItemCancel)) {
						item.setCancel(listDtSapOrderItemCancel);
					}
					
					List<DtSapOrderItemCon> listDtSapOrderItemCon = dtSapOrderItemConDao.queryDtSapOrderItemConBySapOrderItemIdForsap(item.getSapOrderItemId());
					if(BeanUtil.isNotEmpty(listDtSapOrderItemCon)) {
						item.setCondition(listDtSapOrderItemCon);
					}
					
					item.setSapOrderItemId(null);
					
				}
				
				request.setItem(listDtSapOrderItem);
			}
			
			Gson g = new Gson();
			log.info(g.toJson(request));
			
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_ORDER_TRANS_TIMEOUT", "SAPS4_ORDER_TRANS_TIMEOUT", null, null, "Y");
	        List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_ORDER_KEY", "SAP_S4_API_ORDER_KEY", null, null, "Y");
	        
	        String url = "";
	        if(BeanUtil.isNotEmpty(apiKeyy.get(0).getLovAttribute03())) {
	        	 properties.put("APIKey", apiKeyy.get(0).getLovAttribute03());
	        	 url = dTConfig.getUrl().getSapsaleorderinter();
	        }else {
	        	properties.put("Ocp-Apim-Subscription-Key", apiKeyy.get(0).getLovVal());
		        properties.put("x-api-key", apiKeyy.get(0).getLovVal());
		        url = dTConfig.getUrl().getSapsaleorder();
	        }
	       
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
			String resSap = req.HttpClient(properties, url, g.toJson(request), "POST", timeoutS) ;
			
			if(resSap != null) {
				log.info(resSap);
				SapSaleOrderRes ress = null ;
				try {
					ress = g.fromJson(resSap, SapSaleOrderRes.class) ;
					if(ress != null) {
						
						dtSapOrderHeaderRes.setRes_MessageID(ress.getMessageID());
						dtSapOrderHeaderRes.setRes_PartnerName(ress.getPartnerName());
						dtSapOrderHeaderRes.setRes_PartnerMessageID(ress.getPartnerMessageID());
						dtSapOrderHeaderRes.setRes_MessageType(ress.getMessageType());
						dtSapOrderHeaderRes.setRes_MessageClass(ress.getMessageClass());
						dtSapOrderHeaderRes.setRes_MessageNumber(ress.getMessageNumber());
						dtSapOrderHeaderRes.setRes_MessageDesc(ress.getMessageDesc());
						dtSapOrderHeaderRes.setRes_MessageVariable1(ress.getMessageVariable1());
						dtSapOrderHeaderRes.setRes_MessageVariable2(ress.getMessageVariable2());
						dtSapOrderHeaderRes.setRes_MessageVariable3(ress.getMessageVariable3());
						dtSapOrderHeaderRes.setRes_MessageVariable4(ress.getMessageVariable4());
						dtSapOrderHeaderRes.setRes_SalesOrderDocument(ress.getSalesOrderDocument());
						dtSapOrderHeaderRes.setRes_CustomerReference(ress.getCustomerReference());
						
						if(BeanUtil.isNotEmpty(ress.getMessage())) {
							dtSapOrderHeaderRes.setRes_Message(g.toJson(ress.getMessage()));
						}
						
						if(ress.getFault() != null) {
							dtSapOrderHeaderRes.setRes_Message(g.toJson(ress.getFault()));
						}
						
						
						
						
						if(ress.getMessageType()!=null) {
							
							if(BeanUtil.isNotEmpty(ress.getMessageType()) && ress.getMessageType().equals("S") ) {
								dtSapOrderHeaderRes.setStatus("S");
							}else if(BeanUtil.isNotEmpty(ress.getMessageType()) && !ress.getMessageType().equals("S")) {
								dtSapOrderHeaderRes.setStatus("F");
							}else {
								dtSapOrderHeaderRes.setStatus("R");
							}
						}else {
							dtSapOrderHeaderRes.setStatus("R");
						}	
					}
				}catch (Exception e) {
					e.printStackTrace();
					dtSapOrderHeaderRes.setStatus("R");
				}
			}
			else {
				dtSapOrderHeaderRes.setStatus("R");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			dtSapOrderHeaderRes.setStatus("R");
		}
		
		
		//dtSapOrderHeaderRes.setStatus("S");
		//dtSapOrderHeaderRes.setRes_SalesOrderDocument("1011111119");
		return dtSapOrderHeaderRes ;
	}

	@Override
	public Sap168ResBean call168Api(Sap168ReqBean sap168ReqBean) {
		Sap168ResBean ress = null ;
		ress = call168Kafka(sap168ReqBean);
		
//		try {
//			HttpClientUtilDT req = new HttpClientUtilDT();
//			Map<String, String> properties = new HashMap<String, String>();
//	        properties.put("Content-Type", "application/json");
//	        
//	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_168_TIMEOUT", "SAPS4_168_TIMEOUT", null, null, "Y");
//	        String timeoutS = "";
//	        if(BeanUtil.isNotEmpty(timeoutS)) {
//	        	timeoutS = timeout.get(0).getLovVal() ;
//	        }
//	        
//	        Gson g = new Gson();
//			log.info(g.toJson(sap168ReqBean));
//	        
//			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSap()+ "/168", g.toJson(sap168ReqBean), "POST", timeoutS) ;
//			
//			resSap = "{\r\n"
//					+ "	\"MaterialDocument\": \"4900011111\",\r\n"
//					+ "	\"MatDocYear\": \"2024\",\r\n"
//					+ "	\"Item\": [\r\n"
//					+ "		{\r\n"
//					+ "			\"MessageType\": \"S\",\r\n"
//					+ "			\"MessageClass\": \"\",\r\n"
//					+ "			\"MessageNumber\": \"\",\r\n"
//					+ "			\"MessageDesc\": \"\"\r\n"
//					+ "		}\r\n"
//					+ "	]\r\n"
//					+ "}";
//			
////			resSap = "{\r\n"
////					+ "	\"MaterialDocument\": \"\",\r\n"
////					+ "	\"MatDocYear\": \"\",\r\n"
////					+ "	\"Item\": [\r\n"
////					+ "		{\r\n"
////					+ "			\"MessageType\": \"E\",\r\n"
////					+ "			\"MessageClass\": \"\",\r\n"
////					+ "			\"MessageNumber\": \"01\",\r\n"
////					+ "			\"MessageDesc\": \"eeeeeqqqqqq\"\r\n"
////					+ "		},\r\n"
////					+ "		{\r\n"
////					+ "			\"MessageType\": \"E\",\r\n"
////					+ "			\"MessageClass\": \"\",\r\n"
////					+ "			\"MessageNumber\": \"01\",\r\n"
////					+ "			\"MessageDesc\": \"zzzzzzzzzzzzz\"\r\n"
////					+ "		}\r\n"
////					+ "	]\r\n"
////					+ "}";
//			
//			if(resSap != null) {
//				log.info(resSap);
//				try {
//					ress = g.fromJson(resSap, Sap168ResBean.class) ;
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return ress ;
	}
	
	@Override
	public Sap282ReqBean call282Api(Sap282ReqBean sap282ReqBean) {
		Sap282ReqBean res = new Sap282ReqBean();
		DtSapTransferInLog reslog = new DtSapTransferInLog();
		try {
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        

			List<LovMaster> apikey284 = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_282_KEY", "SAP_S4_API_282_KEY",
					null, null, "Y");
			
			properties.put("APIKey", apikey284.get(0).getLovVal());
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_282_TIMEOUT", "SAPS4_282_TIMEOUT", null, null, "Y");
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
	        Sap282ReqBean in = new Sap282ReqBean();
	        Gson g = new Gson();
	        in.setPartnerName("DT");
	        in.setPartnerMessageID("D"+sap282ReqBean.getLocationCode()+sap282ReqBean.getUserId()+TDMDataUtility.toStringEngDateBySimpleFormat(new Date(), "yyyyMMddHHmmss"));
	        in.setDelivery(sap282ReqBean.getDocNo());
	        String jsonReq = g.toJson(in,Sap282ReqBean.class);
			log.info(jsonReq);
			String resSap = null;
			List<LovMaster> listLovRes = lovMasterDao.listLovMasterByCriteria("SAP_S4_MOCKUP", "INTERIM_TRANS_IN", "SAP_0282", "SAP_0282", "Y");
			if(BeanUtil.isNotEmpty(listLovRes) && listLovRes.size()>0) {
				resSap = listLovRes.get(0).getLovAttribute01();
			}
			else {
				resSap = req.HttpClient(properties, dTConfig.getUrl().getSapsale282(), jsonReq, "POST", timeoutS) ;
			}
			
			reslog.setServiceName("0282_Delivery_Order_Inbound");
			reslog.setStatus("F");
			reslog.setParaInput(jsonReq);
			reslog.setParaOutput(resSap);
			reslog.setDocNo(in.getPartnerMessageID());
			
			if(resSap != null) {
				log.info(resSap);
				try {
					DeliveryOrderInbound out = g.fromJson(resSap, DeliveryOrderInbound.class);
					if("S".equals(out.getDOheader().get(0).getMessageType().toUpperCase())) {
						String saleOrg = out.getDOheader().get(0).getSalesOrganization();
						List<LovMaster> listLov = lovMasterDao.listLovMasterByCriteria("SAP_S4_SALEORG_MAP_COMPANY", null, saleOrg, null, "Y");
						String storageLocation = out.getDOheader().get(0).getDOitem().get(0).getReceivingStorageLocation();
						List<LocMapPlant> listLovPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.parseLong(sap282ReqBean.getLocationCode()), listLov.get(0).getLovSubType());
						if(BeanUtil.isNotEmpty(listLovPlant)&& listLovPlant.size()>0 
							&& listLovPlant.get(0).getSapStorageCode().equals(storageLocation)) {
							res.setResultCode("20000");
							res.setResultDescription("Success");
							res.setData(out);
							reslog.setStatus("S");
						} else {
							res.setResultCode("50000");
							res.setResultDescription("locationCode "+sap282ReqBean.getLocationCode()+" : Data not found");
							res.setDeveloperMessage("locationCode "+sap282ReqBean.getLocationCode()+" : Data not found");
						}	
					} else {
						res.setResultCode("50000");
						res.setResultDescription(out.getDOheader().get(0).getMessageDesc());
						res.setDeveloperMessage(out.getDOheader().get(0).getMessageDesc());	
					}
				}catch (Exception e) {
					e.printStackTrace();
					res.setResultCode("50000");
					res.setResultDescription("locationCode "+sap282ReqBean.getLocationCode()+" : Data not found");
					res.setDeveloperMessage("locationCode "+sap282ReqBean.getLocationCode()+" : Data not found");
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		//insert log		
	 	MasterValue userDT = new MasterValue();
        userDT.setCreated(new Date());
        userDT.setCreatedBy(sap282ReqBean.getUserId());
        userDT.setLastUpd(new Date());
        userDT.setLastUpdBy(sap282ReqBean.getUserId());
        reslog.setCreateValue(userDT);
		dtSapTransferInLogDao.insert(reslog);
		
		return res ;
	}
	
	@Override
	public Sap285ResBean call285Api(Sap285ReqBean sap285ReqBean) {
		Sap285ResBean ress = null ;
		try {
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        
	        List<LovMaster> apikey285 = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_285_KEY", "SAP_S4_API_285_KEY",
					null, null, "Y");
			
			properties.put("APIKey", apikey285.get(0).getLovVal());
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_285_TIMEOUT", "SAPS4_285_TIMEOUT", null, null, "Y");
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getStockTransportOrderNumber())) {
	        	sap285ReqBean.setStockTransportOrderNumber("") ;
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getOurReference())) {
	        	sap285ReqBean.setOurReference("");
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getCompanyCode())) {
	        	sap285ReqBean.setCompanyCode("");
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getPurchasingDocType())) {
	        	sap285ReqBean.setPurchasingDocType("");
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getSupplyingPlant())) {
	        	sap285ReqBean.setSupplyingPlant("");
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getPurchOrganiztion())) {
	        	sap285ReqBean.setPurchOrganiztion("");
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getPurchasingGroup())) {
	        	sap285ReqBean.setPurchasingGroup("");
	        }
	        
	        if(BeanUtil.isEmpty(sap285ReqBean.getDocumentDate())) {
	        	sap285ReqBean.setDocumentDate("");
	        }
	        
	        if(BeanUtil.isNotEmpty(sap285ReqBean.getItem())) {
		        for(Sap285ItemReqBean el : sap285ReqBean.getItem()) {
		        	if(BeanUtil.isEmpty(el.getShortText())) {
		        		el.setShortText("");
		        	}
		        	
		        	if(BeanUtil.isEmpty(el.getRequirementTrackingNumber())) {
		        		el.setRequirementTrackingNumber("");
		        	}
		        	
		        	if(BeanUtil.isEmpty(el.getNameofRequisitioner())) {
		        		el.setNameofRequisitioner("");
		        	}
		        	
		        	if(BeanUtil.isEmpty(el.getDeliveryDate())) {
		        		el.setDeliveryDate("");
		        	}
		        	
		        	if(BeanUtil.isNotEmpty(el.getSerial())) {
		        		for(Sap285ItemSerialReqBean se  : el.getSerial()) {
		        			if(se.getSerialNumber().length() > 18) {
		        				se.setUniqueItemIdentifier(se.getSerialNumber());
		        				se.setSerialNumber("");	
		        			}else {
		        				se.setUniqueItemIdentifier("");
		        			}
		        		}
		        	}
		        }
	        }

	        
	        

//	        
//	        UniqueItemIdentifier
	        
	        Gson g = new Gson();
			log.info(g.toJson(sap285ReqBean));
	        
			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapsale285(), g.toJson(sap285ReqBean), "POST", timeoutS) ;
			
//			resSap = "{\r\n"
//					+ "	\"MessageType\": \"S\",\r\n"
//					+ "	\"MessageClass\": \"\",\r\n"
//					+ "	\"MessageNumber\": \"\",\r\n"
//					+ "	\"MessageDescription\": \"\",\r\n"
//					+ "	\"LegacyItemNumber\": \"\",\r\n"
//					+ "	\"MessageID\": \"\",\r\n"
//					+ "	\"PartnerName\": \"\",\r\n"
//					+ "	\"PartnerMessageID\": \"\",\r\n"
//					+ "	\"StockTransportOrderNumber\": \"6020000012\",\r\n"
//					+ "	\"DeliveryOrderNumber\": \"2300000162\",\r\n"
//					+ "	\"MaterialDocumentNumber\": \"5000000399\"\r\n"
//					+ "}";
			
//			resSap = "{\r\n"
//					+ "	\"MessageType\": \"E\",\r\n"
//					+ "	\"MessageClass\": \"\",\r\n"
//					+ "	\"MessageNumber\": \"\",\r\n"
//					+ "	\"MessageDescription\": \"ASASASASASASASASASAS\",\r\n"
//					+ "	\"LegacyItemNumber\": \"\",\r\n"
//					+ "	\"MessageID\": \"\",\r\n"
//					+ "	\"PartnerName\": \"\",\r\n"
//					+ "	\"PartnerMessageID\": \"\"\r\n"
//
//					+ "}";
			
			if(resSap != null) {
				log.info(resSap);
				try {
					ress = g.fromJson(resSap, Sap285ResBean.class) ;
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return ress ;
	}

	@Override
	public Sap204ResBean call204Api(Sap204ReqBean sap204ReqBean) {
		Sap204ResBean response = new Sap204ResBean();
		try {
			HttpClientUtilDT req = new HttpClientUtilDT();
			Gson gson = new Gson();
			Map<String, String> properties = new HashMap<>();
			properties.put("Content-Type", "application/json");
			
			List<LovMaster> apikey204 = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_204_KEY", "SAP_S4_API_204_KEY",
					null, null, "Y");
			
			properties.put("x-api-key", apikey204.get(0).getLovVal());

			List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_204_TIMEOUT", "SAPS4_204_TIMEOUT",
					null, null, "Y");

			String timeoutS = "";
			if (BeanUtil.isNotEmpty(timeout)) {
				timeoutS = timeout.get(0).getLovVal();
			}

			String reqSap = gson.toJson(sap204ReqBean);
			log.info("call SAP[204] request: {}", reqSap);

			LovMaster lov = lovMasterDao.getLovMasterByUnique("SAP_S4_MOCKUP", "INTERIM_REQUEST_GOODS", "SAP_0204");

			String resSap;
			if (BeanUtil.isNotEmpty(lov)) {
				resSap = lov.getLovAttribute01();
			} else {
				resSap = req.HttpClient(properties, dTConfig.getUrl().getSapsale204(), reqSap, "POST", timeoutS);
			}
			log.info("call SAP[204] response: {}", resSap);

			if (BeanUtil.isNotEmpty(resSap)) {
				try {
					response = gson.fromJson(resSap, Sap204ResBean.class);
				} catch (Exception e) {
					log.error("can't convert response SAP[204] ", e);
					throw e;
				}
			}
		} catch (Exception e) {
			log.error("call204Api error ", e);
			throw e;
		}

		return response;
	}
	
	@Override
	public Sap286ResBean call286Api(Sap286ReqBean sap286ReqBean) {
		Sap286ResBean response = null;
		try {
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("Content-Type", "application/json");
			Gson gson = new Gson();
			
			List<LovMaster> apikey286 = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_286_KEY", "SAP_S4_API_286_KEY",
					null, null, "Y");
			
			properties.put("x-api-key", apikey286.get(0).getLovVal());
			
			List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_286_TIMEOUT", "SAPS4_286_TIMEOUT",
					null, null, "Y");

			String timeoutS = "";
			if (BeanUtil.isNotEmpty(timeout)) {
				timeoutS = timeout.get(0).getLovVal();
			}

			String reqSap = gson.toJson(sap286ReqBean);
			log.info("call SAP[286] request: {}", reqSap);

			LovMaster lov = lovMasterDao.getLovMasterByUnique("SAP_S4_MOCKUP", "INTERIM_STOCK_BALANCE", "SAP_0286");

			String resSap;
			if (BeanUtil.isNotEmpty(lov)) {
				resSap = lov.getLovAttribute01();
			} else {
				resSap = req.HttpClient(properties, dTConfig.getUrl().getSapsale286(), reqSap, "POST", timeoutS);
			}
			log.info("call SAP[286] response: {}", resSap);

			if (BeanUtil.isNotEmpty(resSap)) {
				try {
					response = gson.fromJson(resSap, Sap286ResBean.class);
				} catch (Exception e) {
					log.error("can't convert response SAP[286] ", e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("call286Api error ", e);
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public SerialnumberValidation callSerialnumberValidationApi(SerialnumberValidation input) {
		SerialnumberValidation ress = null ;
		try {
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        List<LovMaster> checkCallSap = lovMasterDao.listLovMasterByCriteria("SAP_S4_SERIAL_NUMBER_CONFIG", "SAP_S4_SERIAL_NUMBER_CONFIG", null, null, "Y");
	        
	        Gson g = new Gson();
			log.info(g.toJson(input));
			
	        if(BeanUtil.isNotEmpty(checkCallSap)) {
	        	SerialnumberValidation serialnumberValidation = new SerialnumberValidation();
//	        	serialnumberValidation.setMessageDesc(input.getMessageDesc()!=null?input.getMessageDesc():"");
	        	serialnumberValidation.setMessageDesc("Search successful");
	        	serialnumberValidation.setMessageID(input.getMessageID()!=null?input.getMessageID():"");
	        	serialnumberValidation.setPartnerName(input.getPartnerName()!=null?input.getPartnerName():"");
	        	serialnumberValidation.setPartnerMessageID(input.getPartnerMessageID()!=null?input.getPartnerMessageID():"");
	        		        	
	        	List<ItemsSerialnumberValidation> itemsList = new ArrayList<ItemsSerialnumberValidation>();
	        	
	        	 if(BeanUtil.isNotEmpty(input.getItems())) {
	        		 for (ItemsSerialnumberValidation element : input.getItems()) {

	        			 DtSapWsSerialNumber dtSapWsSerialNumber =  dtSapWsSerialNumberDao.getDtSapWsSerialNumber(element.getSerialNumber());

	        			 if(BeanUtil.isNotEmpty(dtSapWsSerialNumber)) {
			     	         ItemsSerialnumberValidation items = new ItemsSerialnumberValidation();
				     	     items.setMessageType(dtSapWsSerialNumber.getMessageType()!=null?dtSapWsSerialNumber.getMessageType():"");
				     	     items.setMessageDesc(dtSapWsSerialNumber.getMessageDesc()!=null?dtSapWsSerialNumber.getMessageDesc():"");
				     	     items.setSerialNumber(dtSapWsSerialNumber.getSerialNumber()!=null?dtSapWsSerialNumber.getSerialNumber():"");
				     	     items.setUniqueItemIdentifier(dtSapWsSerialNumber.getUniqueitemidentifier()!=null?dtSapWsSerialNumber.getUniqueitemidentifier():"");
				     	     items.setMaterial(dtSapWsSerialNumber.getMaterial()!=null?dtSapWsSerialNumber.getMaterial():"");
				     	     items.setValidFlag(dtSapWsSerialNumber.getValidFlag()!=null?dtSapWsSerialNumber.getValidFlag():"");
				     	     items.setPlant(dtSapWsSerialNumber.getPlant()!=null?dtSapWsSerialNumber.getPlant():"");
					     	 items.setStorageLocation(dtSapWsSerialNumber.getStorageLocation()!=null?dtSapWsSerialNumber.getStorageLocation():"");
					     	 items.setStockType(dtSapWsSerialNumber.getStockType()!=null?dtSapWsSerialNumber.getStockType():"");
					     	 items.setSoldTo(dtSapWsSerialNumber.getSoldTo()!=null?dtSapWsSerialNumber.getSoldTo():"");
					     	 items.setDeliveryNumber(dtSapWsSerialNumber.getDeliveryNumber()!=null?dtSapWsSerialNumber.getDeliveryNumber():"");
					     	 items.setDeliveryDate(dtSapWsSerialNumber.getDeliveryDate()!=null?dtSapWsSerialNumber.getDeliveryDate():"");
					     	 items.setBillingDocument(dtSapWsSerialNumber.getBillingDocument()!=null?dtSapWsSerialNumber.getBillingDocument():"");
					     	 items.setBillingDate(dtSapWsSerialNumber.getBillingDate()!=null?dtSapWsSerialNumber.getBillingDate():"");
					     	 items.setPaymentTerm(dtSapWsSerialNumber.getPaymentTerm()!=null?dtSapWsSerialNumber.getPaymentTerm():"");
					     	 items.setPriceListType(dtSapWsSerialNumber.getPricelistType()!=null?dtSapWsSerialNumber.getPricelistType():"");
					     	 items.setItemCat(dtSapWsSerialNumber.getItemCat()!=null?dtSapWsSerialNumber.getItemCat():"");
					     	 items.setUnitPrice(dtSapWsSerialNumber.getUnitPrice()!=null?dtSapWsSerialNumber.getUnitPrice():"");
					     	
					     	 itemsList.add(items);
	        			 }
	        			 else {
	        				 ItemsSerialnumberValidation items = new ItemsSerialnumberValidation();
				     	     items.setMessageType("S");
				     	     items.setMessageDesc("Search successful");
				     	     items.setSerialNumber(element.getSerialNumber());
				     	     items.setUniqueItemIdentifier("");
				     	     items.setMaterial("");
				     	     items.setValidFlag("0");
				     	     items.setPlant("");
					     	 items.setStorageLocation("");
					     	 items.setStockType("");
					     	 items.setSoldTo("");
					     	 items.setDeliveryNumber("");
					     	 items.setDeliveryDate("");
					     	 items.setBillingDocument("");
					     	 items.setBillingDate("");
					     	 items.setPaymentTerm("");
					     	 items.setPriceListType("");
					     	 items.setItemCat("");
					     	 items.setUnitPrice("");
					     	
					     	 itemsList.add(items);
	        			 }

					}
	        		 
	        	 }
	        	
	        	serialnumberValidation.setItems(itemsList);
	        	ress = serialnumberValidation;
	        	
	        }
	        else {
	        	
				
				List<LovMaster> apikey186 = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_186_KEY", "SAP_S4_API_186_KEY",
						null, null, "Y");
				
				properties.put("x-api-key", apikey186.get(0).getLovVal());
				
				List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_186_TIMEOUT", "SAPS4_186_TIMEOUT",
						null, null, "Y");

				String timeoutS = "";
				if (BeanUtil.isNotEmpty(timeout)) {
					timeoutS = timeout.get(0).getLovVal();
				}
	        	
				String resSap = req.HttpClient(properties, dTConfig.getUrl().getSerialnumbervalidation(), g.toJson(input), "POST", timeoutS) ;
	
				if(resSap != null) {
					log.info(resSap);
					try {
						ress = g.fromJson(resSap, SerialnumberValidation.class) ;
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
	        	
	        }
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return ress ;
	}
	
	public Sap168ResBean call168Kafka(Sap168ReqBean sap168ReqBean) {
		Sap168ResBean ress = new Sap168ResBean() ;
		try {
			Sap168ReqBean input = new Sap168ReqBean();
			Sap168ReqBean header = new Sap168ReqBean();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
			
			
			header.setVersion("5.0"); 
			header.setTimestamp(sdf.format(new Date())) ; //
			header.setOrgService("DT") ;
			header.setScope("global");
			header.setFrom("DT");
			header.setChannel("");
			header.setAgent("");
			header.setBroker("");
			header.setUseCase("");
			header.setUseCaseStep("");
			header.setUseCaseAge("");
			header.setFunctionName("GoodsMovement.Single");
			header.setMessageType("event");
			header.setSession(sap168ReqBean.getPartnerMessageID());  //
			header.setTransaction(sap168ReqBean.getPartnerMessageID()); //
			header.setCommunication("unicast");
			//header.setGroupTags(null);
			header.setReturnedError("");
			header.setInitUri("");
			header.setInitMethod("");
			header.setTmfSpec("none");
			header.setBaseApiVersion("none");
			header.setSchemaVersion("none");
			
			input.setHeader(header);
			input.setBody(sap168ReqBean);
			

	        
	        Gson g = new Gson();
			log.info(g.toJson(input));
			
			RecordHeaders headerm = new RecordHeaders();
			headerm.add(new RecordHeader("env", dTConfig.getKafka().getEnv().getBytes())) ;
	        ProducerRecord<String , String> record = new ProducerRecord<>("sap.GoodsMovement.Single.In",null,"key1",g.toJson(input),headerm) ;
			
			this.kafkaTemplate.send(record);
			ress.setMessageType("S");			
		}catch (Exception e) {
			e.printStackTrace();
			ress.setMessageType("E");
		}
		
		return ress ;
	}

	@Override
	public SapCancelShoppingCartResponse callCancelShoppingCart(SapCancelShoppingCartRequest sapCancelShoppingCartRequest) {
		SapCancelShoppingCartResponse response = new SapCancelShoppingCartResponse();
		try {
			HttpClientUtilDT httpClient = new HttpClientUtilDT();
			Gson gson = new Gson();
			Map<String, String> properties = new HashMap<>();
			properties.put("Content-Type", "application/json");

			/*List<LovMaster> apikeyCancelShoppingCart = lovMasterDao.listLovMasterByCriteria(
					"SAP_S4_API_KEY_CANCEL_SHOPPING_CART",
					"SAP_S4_API_KEY_CANCEL_SHOPPING_CART",
					null, null, "Y"
			);
			properties.put("x-api-key", apikeyCancelShoppingCart.get(0).getLovVal());*/

			List<LovMaster> lovTimeout = lovMasterDao.listLovMasterByCriteria(
					"SAPS4_CANCEL_SHOPPING_TIMEOUT",
					"SAPS4_CANCEL_SHOPPING_TIMEOUT",
					null, null, "Y"
			);

			String timeout = "";
			if (BeanUtil.isNotEmpty(lovTimeout)) {
				timeout = lovTimeout.get(0).getLovVal();
			}

			String reqSap = gson.toJson(sapCancelShoppingCartRequest).replace("\"type\"", "\"@type\"");
			log.info("call callCancelShoppingCart request: {}", reqSap);

			LovMaster lov = lovMasterDao.getLovMasterByUnique("SAP_S4_MOCKUP", "CANCEL_SHOPPING_CART", "CANCEL_SHOPPING_CART");

			String resSap;
			if (BeanUtil.isNotEmpty(lov)) {
				resSap = lov.getLovAttribute01();
			} else {
				String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(new Date());
	        	List<LovMaster> apikeyqueryStock = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_QUERY_STOCK_KEY", "SAP_S4_API_QUERY_STOCK_KEY",
						null, null, "Y");
				
				properties.put("Ocp-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovVal());
				properties.put("x-api-key", apikeyqueryStock.get(0).getLovVal());
				properties.put("Bff-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovAttribute01());
				properties.put("x-request-id", "DT-"+todayAsString);
				properties.put("x-transaction-id", "DT-"+todayAsString);
				properties.put("x-session-id", "DT-"+todayAsString);
				
				resSap = httpClient.HttpClient(properties, dTConfig.getUrl().getCancelshoppingcart(), reqSap, "POST", timeout);
				//resSap = "";
			}
			log.info("call callCancelShoppingCart response: {}", resSap);

			if (BeanUtil.isNotEmpty(resSap)) {
				try {
					response = gson.fromJson(resSap, SapCancelShoppingCartResponse.class);
				} catch (Exception e) {
					log.error("can't convert callCancelShoppingCart response ", e);
					throw e;
				}
			}
		} catch (Exception e) {
			log.error("callCancelShoppingCart error ", e);
			throw e;
		}

		return response;
	}
	
	@Override
	public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve) {
		DtSapCancelReserve res = new DtSapCancelReserve();
		res.setStatus("S");
		
		
		try {
			//2025-01-06T10:12:24.597Z
			String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(new Date());
			String cancellationReason = "cancel_from_dt" ;
			String effectiveCancellationDate = todayAsString;
			String requestedCancellationDate = todayAsString;
			ShoppingCart shoppingCart = ShoppingCart.builder().build();
			shoppingCart.setId(dtSapCancelReserve.getSapReserveNo());
			
			SapCancelShoppingCartRequest req = SapCancelShoppingCartRequest.builder().build() ;
			req.setCancellationReason(cancellationReason) ;
			req.setEffectiveCancellationDate(effectiveCancellationDate);
			req.setRequestedCancellationDate(requestedCancellationDate);
			req.setShoppingCart(shoppingCart);
			SapCancelShoppingCartResponse resp = callCancelShoppingCart(req);
			Gson gson = new Gson();
			res.setReserveRes(gson.toJson(resp)) ;;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res ;
	}

	@Override
	public QueryProductStockResponse callQueryProductStockApi(QueryProductStockRequest input) {
		QueryProductStockResponse ress = null ;
		try {
//			HttpClientUtilDT req = new HttpClientUtilDT();
//			Map<String, String> properties = new HashMap<String, String>();
//	        properties.put("Content-Type", "application/json");
	        List<LovMaster> checkCallSap = lovMasterDao.listLovMasterByCriteria("QUERY_PRODUCT_STOCK_CONFIG", "QUERY_PRODUCT_STOCK_CONFIG", null, null, "Y");
	        
	        Gson g = new Gson();
			log.info(g.toJson(input));
			
	        if(BeanUtil.isNotEmpty(checkCallSap)) {
	        	QueryProductStockResponse queryProductStockResponse = new QueryProductStockResponse();
	        	
//	        	queryProductStockResponse.setCompletedQueryStockDate(null)
//	        	queryProductStockResponse.setCreationDate(input.getCreationDate()!=null?input.getCreationDate():"");
//	        	queryProductStockResponse.setInstantSyncCheck(null)
//	        	queryProductStockResponse.setRequestedAvailabilityDate(input.getRequestedAvailabilityDate()!=null?input.getRequestedAvailabilityDate():"");
//	        	queryProductStockResponse.setRequestedQueryStockDate(input.getRequestedQueryStockDate()!=null?input.getRequestedQueryStockDate():"");
	        		        	
	        	List<QueryStockItem> queryStockItemList = new ArrayList<QueryStockItem>();
	        	QueryStockItem queryStock = new QueryStockItem();
	        	
	        	DtSapWsQueryProductStock dtSapWsQueryProductStock = dtSapWsQueryProductStockDao.getDtSapWsQueryProductStockDao(input.getLocationCode(), input.getCompanyCode(), input.getMatCode());
	        	
		       	 if(BeanUtil.isNotEmpty(dtSapWsQueryProductStock)) {
		       		queryStock.setQuantity(dtSapWsQueryProductStock.getQty());
		       	 }
		       	 else {
			       	queryStock.setQuantity("0");
		       	 }
		        	
		       	 queryStockItemList.add(queryStock);
		       	 queryProductStockResponse.setQueryStockItem(queryStockItemList);
		       	 
		       	 ress = queryProductStockResponse;
		        	
		    }
	        else {
	        	
	        	ProductMst productMst = productMstDao.getProductMstByUniqueV2(input.getCompanyCode(), input.getMatCode());
	        	if(productMst.getProductType().equals("SERVICE")) {
	        		ress = new QueryProductStockResponse();
	        		List<QueryStockItem> queryStockItem = new ArrayList<>();
					QueryStockItem qty = new QueryStockItem();
					qty.setQuantity("100");
					queryStockItem.add(qty);
					ress.setQueryStockItem(queryStockItem);
					return ress ;
	        	}
	        	
	        	
	        	
	        	List<LovMaster> checkCallSapD = lovMasterDao.listLovMasterByCriteria("SAP_S4_QUERY_STOCK_SAP_C", "SAP_S4_QUERY_STOCK_SAP_C", null, null, "Y");
	        	
	        	if(BeanUtil.isNotEmpty(checkCallSapD)){
	        		//callStocksap car
	        		ress = callStockSapCar(input);
	        	}else {
	        		//callStockSalebff
		        	ress = callStockSalebff(input) ;
	        	}	
	        }
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return ress ;
	}

	@Override
	public SaleOrderNoResponse getSaleOrderNo(SaleOrderNoRequest input) {
		
		SaleOrderNoResponse response = new SaleOrderNoResponse(); 
		String sapTranId = dtSapOrderHeaderDao.getSapTranId(input.getDocNo(), input.getCompany());
		if(BeanUtil.isNotEmpty(sapTranId)) {
	        String saleOrderNo = dtSapOrderHeaderDao.getSaleOrderNo(sapTranId);
			if(BeanUtil.isNotEmpty(saleOrderNo)) {
				response.setSaleOrderNo(saleOrderNo);
				
			}
			else {
				response.setSaleOrderNo(null);
			}
		}
		else {
			response.setSaleOrderNo(null);
		}
        
		return response;
	}


	@Override
	public DtSapUniversalHeader callUniversalApi(DtSapUniversalHeader dtSapUniversalHeader) {
		try {
			DtSapUniversalRequest request = new DtSapUniversalRequest();
			
			DtSapUniversalHeader sapUniversalHeader = dtSapUniversalHeaderDao.getByLongPrimaryKey(dtSapUniversalHeader.getSapUniversalHeaderId()) ;
			List<DtSapUniversalItem> sapUniversalItems = dtSapUniversalItemDao.queryDtSapUniversalItemByHeaderId(sapUniversalHeader.getSapUniversalHeaderId()) ;
			
			dtSapUniversalHeader.setPartnerMessageId("DT" + String.valueOf(dtSapUniversalHeader.getSapUniversalHeaderId()));			
			dtSapUniversalHeader.setReference("DT" + String.valueOf(dtSapUniversalHeader.getSapUniversalHeaderId()));
			
			request.setPartnerName("DT");
			request.setPartnerMessageID(dtSapUniversalHeader.getPartnerMessageId());
			request.setTotalRecords("1");
			request.setDocNo(sapUniversalHeader.getDocNo());
			request.setCompanycode(sapUniversalHeader.getCompanycode());
			request.setDocumentdate(sapUniversalHeader.getDocumentdate());
			request.setPostingdate(sapUniversalHeader.getPostingdate());
			request.setDocType(sapUniversalHeader.getDocType());
			request.setFiscalperiod(sapUniversalHeader.getFiscalperiod());
			request.setCurrencykey(sapUniversalHeader.getCurrencykey());
			request.setLedgergroup(sapUniversalHeader.getLedgergroup());
			request.setReference(dtSapUniversalHeader.getReference());
			request.setDocumentheadertext(sapUniversalHeader.getDocumentheadertext());
			request.setReferenceHDKey1(sapUniversalHeader.getReferenceHDKey1());
			request.setReferenceHDKey2(sapUniversalHeader.getReferenceHDKey2());
			request.setExchangerate(sapUniversalHeader.getExchangerate());
			request.setBranchcode(sapUniversalHeader.getBranchcode());
			request.setTaxReportingDate(sapUniversalHeader.getTaxReportingDate());
			request.setItems(sapUniversalItems);
			
			
			Gson g = new Gson();
			log.info(g.toJson(request));
			
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_UNIVERSAL_TRANS_TIMEOUT", "SAPS4_UNIVERSAL_TRANS_TIMEOUT", null, null, "Y");
	        List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_UNIVERSAL_KEY", "SAP_S4_API_UNIVERSAL_KEY", null, null, "Y");
	        
	        //properties.put("Ocp-Apim-Subscription-Key", apiKeyy.get(0).getLovVal());
	        properties.put("APIKey", apiKeyy.get(0).getLovVal());
	        
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapuniversal(), g.toJson(request), "POST", timeoutS) ;
			
			if(resSap != null) {
				log.info(resSap);
				dtSapUniversalHeader.setRes_message(resSap);
				
				DtSapUniversalResponse ress = null ;
				try {
					ress = g.fromJson(resSap, DtSapUniversalResponse.class) ;
					
					if(ress != null) {
						dtSapUniversalHeader.setMessageType(ress.getMessageType());
						if(BeanUtil.isNotEmpty(ress.getItems())){
							dtSapUniversalHeader.setDocumentNumber(ress.getItems().get(0).getDocumentNumber());
							dtSapUniversalHeader.setFiscalYear(ress.getItems().get(0).getFiscalYear());
						}
						if(BeanUtil.isNotEmpty(ress.getMessageType()) && ress.getMessageType().equals("S")) {
							dtSapUniversalHeader.setStatus("S");
						}else if(BeanUtil.isNotEmpty(ress.getMessageType()) && !ress.getMessageType().equals("S")) {
							dtSapUniversalHeader.setStatus("F");
						}else {
							dtSapUniversalHeader.setStatus("R");
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					dtSapUniversalHeader.setStatus("R");
				}
			} else {
				dtSapUniversalHeader.setStatus("R");
			}

		} catch (Exception e) {
			e.printStackTrace();
			dtSapUniversalHeader.setStatus("R");
		}
		
		return dtSapUniversalHeader;
	}
	
	@Override
	public DtSapAccrualHeader callAccrualApi(DtSapAccrualHeader dtSapAccrualHeader) {
		try {
			DtSapAccrualRequest request = new DtSapAccrualRequest();
			
			DtSapAccrualHeader sapAccrualHeader = dtSapAccrualHeaderDao.getByLongPrimaryKey(dtSapAccrualHeader.getSapAccrualHeaderId()) ;
			List<DtSapAccrualItem> sapAccrualItems = dtSapAccrualItemDao.queryDtSapAccrualItemByHeaderId(dtSapAccrualHeader.getSapAccrualHeaderId()) ;
			
			for(DtSapAccrualItem sapAccrualItem:sapAccrualItems) {
				sapAccrualItem.setOpeningPostingDate(sapAccrualItem.getStartofLife());
			}
			
			request.setPartnerName("DT") ;
			request.setPartnerMessageID("DT" + String.valueOf(sapAccrualHeader.getSapAccrualHeaderId()));
			request.setCompanycode(sapAccrualHeader.getCompanycode());
			request.setAccrualObjectCategory(sapAccrualHeader.getAccrualobjectcategory());
			request.setAccrualObjectSubcategory(sapAccrualHeader.getAccrualobjectsubcategory());
			request.setItem(sapAccrualItems);
			
			
			Gson g = new Gson();
			log.info(g.toJson(request));
			
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_ACCRUAL_TRANS_TIMEOUT", "SAPS4_ACCRUAL_TRANS_TIMEOUT", null, null, "Y");
	        List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_ACCRUAL_KEY", "SAP_S4_API_ACCRUAL_KEY", null, null, "Y");
	        
//	        properties.put("Ocp-Apim-Subscription-Key", apiKeyy.get(0).getLovVal());
	        properties.put("APIKey", apiKeyy.get(0).getLovVal());
	        
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapaccrual(), g.toJson(request), "POST", timeoutS) ;
			
			if(resSap != null) {
				log.info(resSap);
				dtSapAccrualHeader.setRes_message(resSap);
				
				DtSapAccrualResponse ress = null ;
				try {
					ress = g.fromJson(resSap, DtSapAccrualResponse.class) ;
					if(ress != null) {
						dtSapAccrualHeader.setStatus(ress.getStatus());
						dtSapAccrualHeader.setResMessagetype(ress.getMessageType());
						dtSapAccrualHeader.setResAccrualObjectNumber(ress.getAccrualObject());
						
						if(BeanUtil.isNotEmpty(ress.getItem())) {
							dtSapAccrualHeader.setResAccrualObjectNumber(ress.getItem().get(0).getAccrualObjectNumber());
						}
						
						if(BeanUtil.isNotEmpty(ress.getMessageType()) && ress.getMessageType().equals("S") ) {
							dtSapAccrualHeader.setStatus("S");
						}else if(BeanUtil.isNotEmpty(ress.getMessageType()) && !ress.getMessageType().equals("S")) {
							dtSapAccrualHeader.setStatus("F");
						}else {
							dtSapAccrualHeader.setStatus("R");
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					dtSapAccrualHeader.setStatus("R");
				}
			} else {
				dtSapAccrualHeader.setStatus("R");
			}

		} catch (Exception e) {
			e.printStackTrace();
			dtSapAccrualHeader.setStatus("R");
		}
		
		
		return dtSapAccrualHeader;
	}

	@Override
	public DtSapUniversalHeader callCancelGoodsReturnUniversalApi(DtSapUniversalHeader dtSapUniversalHeader, String cancelReceiptNum) {
		try {
			DtSapUniversalRequest request = new DtSapUniversalRequest();

			DtSapUniversalHeader sapUniversalHeader = dtSapUniversalHeaderDao.getByLongPrimaryKey(dtSapUniversalHeader.getSapUniversalHeaderId());
			List<DtSapUniversalItem> sapUniversalItems = dtSapUniversalItemDao.queryDtSapUniversalItemByHeaderId(dtSapUniversalHeader.getSapUniversalHeaderId());

			dtSapUniversalHeader.setPartnerMessageId("DT" + dtSapUniversalHeader.getSapUniversalHeaderId());
			dtSapUniversalHeader.setReference("DT" + dtSapUniversalHeader.getSapUniversalHeaderId());

			request.setPartnerName("DT");
			request.setPartnerMessageID(dtSapUniversalHeader.getPartnerMessageId());
			request.setTotalRecords("1");
			request.setDocNo(sapUniversalHeader.getDocNo());
			request.setCompanycode(sapUniversalHeader.getCompanycode());
			request.setDocumentdate(sapUniversalHeader.getDocumentdate());
			request.setPostingdate(sapUniversalHeader.getPostingdate());
			request.setDocType(sapUniversalHeader.getDocType());
			request.setFiscalperiod(sapUniversalHeader.getFiscalperiod());
			request.setCurrencykey(sapUniversalHeader.getCurrencykey());
			request.setLedgergroup(sapUniversalHeader.getLedgergroup());
			request.setReference(dtSapUniversalHeader.getReference());
			request.setDocumentheadertext(cancelReceiptNum);
			request.setReferenceHDKey1(sapUniversalHeader.getReferenceHDKey1());
			request.setReferenceHDKey2(sapUniversalHeader.getReferenceHDKey2());
			request.setExchangerate(sapUniversalHeader.getExchangerate());
			request.setBranchcode(sapUniversalHeader.getBranchcode());
			request.setTaxReportingDate(sapUniversalHeader.getTaxReportingDate());
			request.setItems(sapUniversalItems);


			Gson g = new Gson();
			log.info("callCancelGoodsReturnUniversal request: {}", g.toJson(request));

			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<>();
			properties.put("Content-Type", "application/json");

			List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_UNIVERSAL_TRANS_TIMEOUT", "SAPS4_UNIVERSAL_TRANS_TIMEOUT", null, null, "Y");
			List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_UNIVERSAL_KEY", "SAP_S4_API_UNIVERSAL_KEY", null, null, "Y");

			properties.put("APIKey", apiKeyy.get(0).getLovVal());

			String timeoutS = "";
			if (BeanUtil.isNotEmpty(timeout)) {
				timeoutS = timeout.get(0).getLovVal();
			}
			
			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapuniversal(), g.toJson(request), "POST", timeoutS);
			log.info("callCancelGoodsReturnUniversal response: {}", resSap);

			if (resSap != null) {
				dtSapUniversalHeader.setRes_message(resSap);

				try {
					DtSapUniversalResponse res = g.fromJson(resSap, DtSapUniversalResponse.class);

					if (res != null) {
						dtSapUniversalHeader.setMessageType(res.getMessageType());
						if (BeanUtil.isNotEmpty(res.getItems())) {
							dtSapUniversalHeader.setDocumentNumber(res.getItems().get(0).getDocumentNumber());
							dtSapUniversalHeader.setFiscalYear(res.getItems().get(0).getFiscalYear());
						}
						if (BeanUtil.isNotEmpty(res.getMessageType()) && res.getMessageType().equals("S")) {
							dtSapUniversalHeader.setStatus("S");
						} else if (BeanUtil.isNotEmpty(res.getMessageType()) && !res.getMessageType().equals("S")) {
							dtSapUniversalHeader.setStatus("F");
						} else {
							dtSapUniversalHeader.setStatus("R");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					dtSapUniversalHeader.setStatus("R");
				}
			} else {
				dtSapUniversalHeader.setStatus("R");
			}
		} catch (Exception e) {
			e.printStackTrace();
			dtSapUniversalHeader.setStatus("R");
		}

		return dtSapUniversalHeader;
	}

	@Override
	public DtSapAccrualHeader callCancelGoodsReturnAccrualApi(DtSapAccrualHeader dtSapAccrualHeader, String cancelReceiptNum) {
		try {
			DtSapAccrualRequest request = new DtSapAccrualRequest();

			DtSapAccrualHeader sapAccrualHeader = dtSapAccrualHeaderDao.getByLongPrimaryKey(dtSapAccrualHeader.getSapAccrualHeaderId());
			List<DtSapAccrualItem> sapAccrualItems = dtSapAccrualItemDao.queryDtSapAccrualItemByHeaderId(dtSapAccrualHeader.getSapAccrualHeaderId());

			for (DtSapAccrualItem sapAccrualItem : sapAccrualItems) {
				sapAccrualItem.setDocumentNumber(cancelReceiptNum);
				sapAccrualItem.setReserveField(cancelReceiptNum);
				sapAccrualItem.setOpeningPostingDate(sapAccrualItem.getStartofLife());
			}

			request.setPartnerName("DT");
			request.setPartnerMessageID("DT" + sapAccrualHeader.getSapAccrualHeaderId());
			request.setCompanycode(sapAccrualHeader.getCompanycode());
			request.setAccrualObjectCategory(sapAccrualHeader.getAccrualobjectcategory());
			request.setAccrualObjectSubcategory(sapAccrualHeader.getAccrualobjectsubcategory());
			request.setItem(sapAccrualItems);


			Gson g = new Gson();
			log.info("callCancelGoodsReturnAccrual request: {}", g.toJson(request));

			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<>();
			properties.put("Content-Type", "application/json");

			List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_ACCRUAL_TRANS_TIMEOUT", "SAPS4_ACCRUAL_TRANS_TIMEOUT", null, null, "Y");
			List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_ACCRUAL_KEY", "SAP_S4_API_ACCRUAL_KEY", null, null, "Y");

			properties.put("APIKey", apiKeyy.get(0).getLovVal());

			String timeoutS = "";
			if (BeanUtil.isNotEmpty(timeout)) {
				timeoutS = timeout.get(0).getLovVal();
			}
			
			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapaccrual(), g.toJson(request), "POST", timeoutS);
			log.info("callCancelGoodsReturnAccrual response: {}", resSap);

			if (resSap != null) {
				dtSapAccrualHeader.setRes_message(resSap);

				try {
					DtSapAccrualResponse res = g.fromJson(resSap, DtSapAccrualResponse.class);
					if (res != null) {
						dtSapAccrualHeader.setStatus(res.getStatus());
						dtSapAccrualHeader.setResMessagetype(res.getMessageType());
						dtSapAccrualHeader.setResAccrualObjectNumber(res.getAccrualObject());

						if (BeanUtil.isNotEmpty(res.getItem())) {
							dtSapAccrualHeader.setResAccrualObjectNumber(res.getItem().get(0).getAccrualObjectNumber());
						}

						if (BeanUtil.isNotEmpty(res.getMessageType()) && res.getMessageType().equals("S")) {
							dtSapAccrualHeader.setStatus("S");
						} else if (BeanUtil.isNotEmpty(res.getMessageType()) && !res.getMessageType().equals("S")) {
							dtSapAccrualHeader.setStatus("F");
						} else {
							dtSapAccrualHeader.setStatus("R");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					dtSapAccrualHeader.setStatus("R");
				}
			} else {
				dtSapAccrualHeader.setStatus("R");
			}
		} catch (Exception e) {
			e.printStackTrace();
			dtSapAccrualHeader.setStatus("R");
		}

		return dtSapAccrualHeader;
	}
	
	public DtSapWsSerialNumber getDtSapWsSerialNumber(String SerialNumber) {
		return dtSapWsSerialNumberDao.getDtSapWsSerialNumber(SerialNumber);
	}
	
	private QueryProductStockResponse callStockSalebff(QueryProductStockRequest input) {
		QueryProductStockResponse ress = null ;
		HttpClientUtilDT req = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        Gson g = new Gson();
    	
    	//
    	String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(new Date());
    	List<LovMaster> apikeyqueryStock = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_QUERY_STOCK_KEY", "SAP_S4_API_QUERY_STOCK_KEY",
				null, null, "Y");
		
		properties.put("Ocp-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovVal());
		properties.put("x-api-key", apikeyqueryStock.get(0).getLovVal());
		properties.put("Bff-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovAttribute01());
		properties.put("x-request-id", "DT-"+todayAsString);
		properties.put("x-transaction-id", "DT-"+todayAsString);
		properties.put("x-session-id", "DT-"+todayAsString);
		
		List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_QUERY_STOCK_TIMEOUT", "SAPS4_QUERY_STOCK_TIMEOUT",
				null, null, "Y");

		String timeoutS = "";
		if (BeanUtil.isNotEmpty(timeout)) {
			timeoutS = timeout.get(0).getLovVal();
		}
		
		List<LocMapPlant> listLovPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(input.getLocationCode()), input.getCompanyCode());

		
		QueryProductStockRequest inputSap = new QueryProductStockRequest();
		
		inputSap.setCreationDate(todayAsString) ;
		inputSap.setRequestedAvailabilityDate(todayAsString);
		inputSap.setRequestedQueryStockDate(todayAsString);
		
		List<SearchCriteriaQueryProductStock> listSearchCriteriaQueryProductStock = new ArrayList<>();
		SearchCriteriaQueryProductStock searchCriteriaQueryProductStock = new SearchCriteriaQueryProductStock();
		searchCriteriaQueryProductStock.setStockType("AIS");
		
		
		List<LocationDestQueryProductStock> locationDest = new ArrayList<>();
		LocationDestQueryProductStock locationDestQueryProductStock = new LocationDestQueryProductStock();
		locationDestQueryProductStock.setCompanyCode(input.getCompanyCode());
		locationDestQueryProductStock.setValueType("plantId");
		locationDestQueryProductStock.setId(listLovPlant.get(0).getPk().getPlantCode()); //////////
		locationDest.add(locationDestQueryProductStock);	
		
		searchCriteriaQueryProductStock.setLocationDest(locationDest);
		
		LocationSourceQueryProductStock locationSource = new LocationSourceQueryProductStock();
		locationSource.setId(input.getLocationCode());
		
		searchCriteriaQueryProductStock.setLocationSource(locationSource);
		
		StockLevelQueryProductStock stockLevel = new StockLevelQueryProductStock();
		stockLevel.setUnit(input.getUnitName());
		
		searchCriteriaQueryProductStock.setStockLevel(stockLevel);

		StockItemQueryProductStock stockItem = new StockItemQueryProductStock();
		stockItem.setId(BeanUtil.lPadNumber(input.getMatCode(), 18));
		
		searchCriteriaQueryProductStock.setStockItem(stockItem);
		listSearchCriteriaQueryProductStock.add(searchCriteriaQueryProductStock);
		inputSap.setSearchCriteria(listSearchCriteriaQueryProductStock);
		
		log.info(g.toJson(inputSap));
		String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapqueryproductstock(), g.toJson(inputSap), "POST", timeoutS) ;

		if(resSap != null) {
			log.info(resSap);
			try {
				ress = g.fromJson(resSap, QueryProductStockResponse.class) ;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
       return ress ;
	}
	
	private QueryProductStockResponse callStockSapCar(QueryProductStockRequest input) {
		QueryProductStockResponse ress = null ;
		HttpClientUtilDT req = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        Gson g = new Gson();
    	
    	//
    	//String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(new Date());
    	List<LovMaster> apikeyqueryStock = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_CAR_STOCK_KEY", "SAP_S4_API_CAR_STOCK_KEY",
				null, null, "Y");
		
		properties.put("APIKey", apikeyqueryStock.get(0).getLovVal());

		
		List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_QUERY_STOCK_CAR_TIMEOUT", "SAPS4_QUERY_STOCK_CAR_TIMEOUT",
				null, null, "Y");

		String timeoutS = "";
		if (BeanUtil.isNotEmpty(timeout)) {
			timeoutS = timeout.get(0).getLovVal();
		}
		
		List<LocMapPlant> listLovPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(input.getLocationCode()), input.getCompanyCode());

		String plantCode = listLovPlant.get(0).getPk().getPlantCode() ;
		String unitSap = input.getUnitName() ;
		String matCode = BeanUtil.lPadNumber(input.getMatCode(), 18) ;
		
		QueryProductStockRequest inputSap = new QueryProductStockRequest();
		
		QueryProductStockRequest matCodeSap = new QueryProductStockRequest();
		matCodeSap.setItemID(matCode);
		matCodeSap.setUnitSap(unitSap);
		List<QueryProductStockRequest> ARTICLE_ID = new ArrayList<>();
		ARTICLE_ID.add(matCodeSap);
		inputSap.setARTICLE_ID(ARTICLE_ID);
		
		List<QueryProductStockRequest> ItemSourceList = new ArrayList<>();
		QueryProductStockRequest ItemSource = new QueryProductStockRequest();
		ItemSource.setSource(plantCode);
		ItemSourceList.add(ItemSource);
		inputSap.setItemSource(ItemSourceList);
		

		
		log.info(g.toJson(inputSap));
		String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapcarquerystock(), g.toJson(inputSap), "POST", timeoutS) ;

		if(resSap != null) {
			log.info(resSap);
			try {
				ress = g.fromJson(resSap, QueryProductStockResponse.class) ;
				
				//System.out.print("11.0".replace(".00", "").replace(".0", ""));
				
				//ress.getATP().getATP_RESULT_ITEM().get(0).getAVAILABILITY().get(0).getQUANTITY().replace(".00", "").replace(".0", "") ;
				
				List<QueryStockItem> queryStockItem = new ArrayList<>();
				QueryStockItem qty = new QueryStockItem();
				qty.setQuantity(ress.getATP().getATP_RESULT_ITEM().get(0).getAVAILABILITY().get(0).getQUANTITY().replace(".00", "").replace(".0", ""));
				queryStockItem.add(qty);
				ress.setQueryStockItem(queryStockItem);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
       return ress ;
	}

	@Override
	public List<QueryPostTransactionBean> getSerialCNInvoice(String serialNo) throws DataAccessException {
		return dtSapTransactionTypeDao.getSerialCNInvoice(serialNo);
	}

	@Override
	public List<QueryPostTransactionBean> getSerialCancelInvoice(String serialNo) throws DataAccessException {
		return dtSapTransactionTypeDao.getSerialCancelInvoice(serialNo);
	}
	
	@Override
	public DtSapAccrualResponse callAccrualSummaryApi(DtSapAccrualGroupHeader dtSapAccrualGroupHeader,DtSapAccrualHeader sapAccrualHeader,List<DtSapAccrualItem> sapAccrualItems) {
		DtSapAccrualResponse ress = null ;
		try {
			DtSapAccrualRequest request = new DtSapAccrualRequest();
			
//			for(DtSapAccrualItem sapAccrualItem:sapAccrualItems) {
//				sapAccrualItem.setOpeningPostingDate(sapAccrualItem.getStartofLife());
//			}
			
			request.setPartnerName("DT") ;
			request.setPartnerMessageID(sapAccrualHeader.getPartnermessageid());
			request.setCompanycode(sapAccrualHeader.getCompanycode());
			request.setAccrualObjectCategory(sapAccrualHeader.getAccrualobjectcategory());
			request.setAccrualObjectSubcategory(sapAccrualHeader.getAccrualobjectsubcategory());
			request.setItem(sapAccrualItems);
			
			
			Gson g = new Gson();
			log.info(g.toJson(request));
			
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        
	        List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAPS4_ACCRUAL_TRANS_TIMEOUT", "SAPS4_ACCRUAL_TRANS_TIMEOUT", null, null, "Y");
	        List<LovMaster> apiKeyy = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_ACCRUAL_KEY", "SAP_S4_API_ACCRUAL_KEY", null, null, "Y");
	        
//	        properties.put("Ocp-Apim-Subscription-Key", apiKeyy.get(0).getLovVal());
	        properties.put("APIKey", apiKeyy.get(0).getLovVal());
	        
	        String timeoutS = "";
	        if(BeanUtil.isNotEmpty(timeout)) {
	        	timeoutS = timeout.get(0).getLovVal() ;
	        }
//	        String resSap = "{\"MessageType\":\"S\",\"MessageDesc\":\"\",\"MessageID\":\"CA831F3EF6444B0BB679C9BBC9E814AB\",\"PartnerName\":\"DT\",\"PartnerMessageID\":\"DT_CN150520250001\",\"Status\":\"P\",\"StatusDescription\":\"In Process\",\"Number\":\"1\",\"CompanyCode\":\"1200\",\"AccrualObject\":\"T0100\",\"LegacyDocument\":\"\",\"Item\":[{\"MessageType\":\"S\",\"MessageDesc\":\"Document 1090000074 was posted in company code 1200;Accrual object with external reference T0100     0000000000005000000001/1 created successfully\",\"AccrualObjectNumber\":\"0000000000005000000001\",\"AccrualItemType\":\"ZL10\",\"Ledger\":\"\",\"FiscalYear\":\"\",\"StartDate\":\"\",\"EndDate\":\"\",\"AccrualDeferralMethod\":\"LINEAR_DAY\",\"TotalAccrAmountinTransCrcy\":\"3738.32\",\"TransactionCurrency\":\"THB\",\"BusinessArea\":\"\",\"ProfitCenter\":\"11010100\",\"CostCenter\":\"\",\"Order\":\"\",\"WBSElement\":\"\",\"Network\":\"\",\"Segment\":\"A100\",\"SalesOrder\":\"\",\"SalesOrderItem\":\"\",\"OffsettingCompanyCode\":\"\",\"SampleParameter\":\"\",\"BPCode\":\"\",\"ContractNumber\":\"\",\"BPName\":\"\",\"DocumentNumber\":\"CN1114AWN150520251\",\"IMEInumber\":\"\",\"MaterialDescription\":\"\",\"MobilePhoneNumber\":\"\",\"ReserveField\":\"\",\"Year\":\"\",\"USSDCode\":\"\",\"TradingPartner\":\"\",\"OffsettingAccountforOpeningPosting\":\"\",\"AccrAcctforOpenPosting\":\"\",\"DocumentTypeforOpeningPosting\":\"\",\"AccrAccountforPeriodicPosting\":\"\",\"OffsettingAccountforPeriodicPosting\":\"\",\"DocumentTypeforPeriodicPosting\":\"\",\"AccrAcctforClosingPosting\":\"\",\"OffsettingAccountforClosingPosting\":\"\",\"DocumentTypeforClosingPosting\":\"\",\"OverwriteAllowed\":\"\",\"Productnumber\":\"1000000960\",\"Plant\":\"L512\",\"ProfitCenterCOPA\":\"11010100\",\"ProjectUSSDCode\":\"\",\"SalesDistrict\":\"\",\"SalesOrderNumber\":\"\",\"SalesOrdItem\":\"\",\"Customer\":\"\",\"SalesOffice\":\"\",\"SalesGroup\":\"\",\"SalesOrganization\":\"1200\",\"DistributionChannel\":\"12\",\"Division\":\"00\",\"Costcenter\":\"\"},{\"MessageType\":\"S\",\"MessageDesc\":\"Document 1090000075 was posted in company code 1200;Accrual object with external reference T0100     0000000000005000000002/1 created successfully\",\"AccrualObjectNumber\":\"0000000000005000000002\",\"AccrualItemType\":\"ZL10\",\"Ledger\":\"\",\"FiscalYear\":\"\",\"StartDate\":\"\",\"EndDate\":\"\",\"AccrualDeferralMethod\":\"LINEAR_DAY\",\"TotalAccrAmountinTransCrcy\":\"2803.74\",\"TransactionCurrency\":\"THB\",\"BusinessArea\":\"\",\"ProfitCenter\":\"11010100\",\"CostCenter\":\"\",\"Order\":\"\",\"WBSElement\":\"\",\"Network\":\"\",\"Segment\":\"A100\",\"SalesOrder\":\"\",\"SalesOrderItem\":\"\",\"OffsettingCompanyCode\":\"\",\"SampleParameter\":\"\",\"BPCode\":\"\",\"ContractNumber\":\"\",\"BPName\":\"\",\"DocumentNumber\":\"CN1136AWN150520252\",\"IMEInumber\":\"\",\"MaterialDescription\":\"\",\"MobilePhoneNumber\":\"\",\"ReserveField\":\"\",\"Year\":\"\",\"USSDCode\":\"\",\"TradingPartner\":\"\",\"OffsettingAccountforOpeningPosting\":\"\",\"AccrAcctforOpenPosting\":\"\",\"DocumentTypeforOpeningPosting\":\"\",\"AccrAccountforPeriodicPosting\":\"\",\"OffsettingAccountforPeriodicPosting\":\"\",\"DocumentTypeforPeriodicPosting\":\"\",\"AccrAcctforClosingPosting\":\"\",\"OffsettingAccountforClosingPosting\":\"\",\"DocumentTypeforClosingPosting\":\"\",\"OverwriteAllowed\":\"\",\"Productnumber\":\"1000000960\",\"Plant\":\"A999\",\"ProfitCenterCOPA\":\"11010100\",\"ProjectUSSDCode\":\"\",\"SalesDistrict\":\"\",\"SalesOrderNumber\":\"\",\"SalesOrdItem\":\"\",\"Customer\":\"\",\"SalesOffice\":\"\",\"SalesGroup\":\"\",\"SalesOrganization\":\"1200\",\"DistributionChannel\":\"12\",\"Division\":\"00\",\"Costcenter\":\"\"},{\"MessageType\":\"S\",\"MessageDesc\":\"Document 1090000076 was posted in company code 1200;Accrual object with external reference T0100     0000000000005000000003/1 created successfully\",\"AccrualObjectNumber\":\"0000000000005000000003\",\"AccrualItemType\":\"ZL10\",\"Ledger\":\"\",\"FiscalYear\":\"\",\"StartDate\":\"\",\"EndDate\":\"\",\"AccrualDeferralMethod\":\"LINEAR_DAY\",\"TotalAccrAmountinTransCrcy\":\"1869.16\",\"TransactionCurrency\":\"THB\",\"BusinessArea\":\"\",\"ProfitCenter\":\"11010100\",\"CostCenter\":\"\",\"Order\":\"\",\"WBSElement\":\"\",\"Network\":\"\",\"Segment\":\"A100\",\"SalesOrder\":\"\",\"SalesOrderItem\":\"\",\"OffsettingCompanyCode\":\"\",\"SampleParameter\":\"\",\"BPCode\":\"\",\"ContractNumber\":\"\",\"BPName\":\"\",\"DocumentNumber\":\"CN1136AWN150520253\",\"IMEInumber\":\"\",\"MaterialDescription\":\"\",\"MobilePhoneNumber\":\"\",\"ReserveField\":\"\",\"Year\":\"\",\"USSDCode\":\"\",\"TradingPartner\":\"\",\"OffsettingAccountforOpeningPosting\":\"\",\"AccrAcctforOpenPosting\":\"\",\"DocumentTypeforOpeningPosting\":\"\",\"AccrAccountforPeriodicPosting\":\"\",\"OffsettingAccountforPeriodicPosting\":\"\",\"DocumentTypeforPeriodicPosting\":\"\",\"AccrAcctforClosingPosting\":\"\",\"OffsettingAccountforClosingPosting\":\"\",\"DocumentTypeforClosingPosting\":\"\",\"OverwriteAllowed\":\"\",\"Productnumber\":\"1000000960\",\"Plant\":\"A999\",\"ProfitCenterCOPA\":\"11010100\",\"ProjectUSSDCode\":\"\",\"SalesDistrict\":\"\",\"SalesOrderNumber\":\"\",\"SalesOrdItem\":\"\",\"Customer\":\"\",\"SalesOffice\":\"\",\"SalesGroup\":\"\",\"SalesOrganization\":\"1200\",\"DistributionChannel\":\"12\",\"Division\":\"00\",\"Costcenter\":\"\"},{\"MessageType\":\"S\",\"MessageDesc\":\"Document 1090000077 was posted in company code 1200;Accrual object with external reference T0100     0000000000005000000004/1 created successfully\",\"AccrualObjectNumber\":\"0000000000005000000004\",\"AccrualItemType\":\"ZL10\",\"Ledger\":\"\",\"FiscalYear\":\"\",\"StartDate\":\"\",\"EndDate\":\"\",\"AccrualDeferralMethod\":\"LINEAR_DAY\",\"TotalAccrAmountinTransCrcy\":\"934.58\",\"TransactionCurrency\":\"THB\",\"BusinessArea\":\"\",\"ProfitCenter\":\"11010100\",\"CostCenter\":\"\",\"Order\":\"\",\"WBSElement\":\"\",\"Network\":\"\",\"Segment\":\"A100\",\"SalesOrder\":\"\",\"SalesOrderItem\":\"\",\"OffsettingCompanyCode\":\"\",\"SampleParameter\":\"\",\"BPCode\":\"\",\"ContractNumber\":\"\",\"BPName\":\"\",\"DocumentNumber\":\"CN1136AWN150520254\",\"IMEInumber\":\"\",\"MaterialDescription\":\"\",\"MobilePhoneNumber\":\"\",\"ReserveField\":\"\",\"Year\":\"\",\"USSDCode\":\"\",\"TradingPartner\":\"\",\"OffsettingAccountforOpeningPosting\":\"\",\"AccrAcctforOpenPosting\":\"\",\"DocumentTypeforOpeningPosting\":\"\",\"AccrAccountforPeriodicPosting\":\"\",\"OffsettingAccountforPeriodicPosting\":\"\",\"DocumentTypeforPeriodicPosting\":\"\",\"AccrAcctforClosingPosting\":\"\",\"OffsettingAccountforClosingPosting\":\"\",\"DocumentTypeforClosingPosting\":\"\",\"OverwriteAllowed\":\"\",\"Productnumber\":\"1000000960\",\"Plant\":\"A999\",\"ProfitCenterCOPA\":\"11010100\",\"ProjectUSSDCode\":\"\",\"SalesDistrict\":\"\",\"SalesOrderNumber\":\"\",\"SalesOrdItem\":\"\",\"Customer\":\"\",\"SalesOffice\":\"\",\"SalesGroup\":\"\",\"SalesOrganization\":\"1200\",\"DistributionChannel\":\"12\",\"Division\":\"00\",\"Costcenter\":\"\"},{\"MessageType\":\"S\",\"MessageDesc\":\"Document 1090000078 was posted in company code 1200;Accrual object with external reference T0100     0000000000005000000005/1 created successfully\",\"AccrualObjectNumber\":\"0000000000005000000005\",\"AccrualItemType\":\"ZL10\",\"Ledger\":\"\",\"FiscalYear\":\"\",\"StartDate\":\"\",\"EndDate\":\"\",\"AccrualDeferralMethod\":\"LINEAR_DAY\",\"TotalAccrAmountinTransCrcy\":\"3738.32\",\"TransactionCurrency\":\"THB\",\"BusinessArea\":\"\",\"ProfitCenter\":\"11010100\",\"CostCenter\":\"\",\"Order\":\"\",\"WBSElement\":\"\",\"Network\":\"\",\"Segment\":\"A100\",\"SalesOrder\":\"\",\"SalesOrderItem\":\"\",\"OffsettingCompanyCode\":\"\",\"SampleParameter\":\"\",\"BPCode\":\"\",\"ContractNumber\":\"\",\"BPName\":\"\",\"DocumentNumber\":\"CN1136AWN150520255\",\"IMEInumber\":\"\",\"MaterialDescription\":\"\",\"MobilePhoneNumber\":\"\",\"ReserveField\":\"\",\"Year\":\"\",\"USSDCode\":\"\",\"TradingPartner\":\"\",\"OffsettingAccountforOpeningPosting\":\"\",\"AccrAcctforOpenPosting\":\"\",\"DocumentTypeforOpeningPosting\":\"\",\"AccrAccountforPeriodicPosting\":\"\",\"OffsettingAccountforPeriodicPosting\":\"\",\"DocumentTypeforPeriodicPosting\":\"\",\"AccrAcctforClosingPosting\":\"\",\"OffsettingAccountforClosingPosting\":\"\",\"DocumentTypeforClosingPosting\":\"\",\"OverwriteAllowed\":\"\",\"Productnumber\":\"1000000960\",\"Plant\":\"A999\",\"ProfitCenterCOPA\":\"11010100\",\"ProjectUSSDCode\":\"\",\"SalesDistrict\":\"\",\"SalesOrderNumber\":\"\",\"SalesOrdItem\":\"\",\"Customer\":\"\",\"SalesOffice\":\"\",\"SalesGroup\":\"\",\"SalesOrganization\":\"1200\",\"DistributionChannel\":\"12\",\"Division\":\"00\",\"Costcenter\":\"\"}]}";
			String resSap = req.HttpClient(properties, dTConfig.getUrl().getSapaccrual(), g.toJson(request), "POST", timeoutS) ;
			if(resSap != null) {
				log.info(resSap);
				dtSapAccrualGroupHeader.setResMessage(resSap);
//				dtSapAccrualGroupHeader.setResMessage(g.toJson(request));
				try {
					ress = g.fromJson(resSap, DtSapAccrualResponse.class) ;
				}catch (Exception e) {
					log.error("Error : callAccrualSummaryApi");
					e.printStackTrace();
					dtSapAccrualGroupHeader.setStatus("R");
				}
			} else {
				dtSapAccrualGroupHeader.setStatus("R");
			}

		} catch (Exception e) {
			e.printStackTrace();
			dtSapAccrualGroupHeader.setStatus("R");
		}
		
		
		return ress;
	}
	
	@Override
	public SapCancelShoppingCartResponse callCancelReserveApiRefac(DtSapCancelReserve dtSapCancelReserve) {
		SapCancelShoppingCartResponse res = new SapCancelShoppingCartResponse();

		
		
		try {
			//2025-01-06T10:12:24.597Z
			String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(new Date());
			String cancellationReason = "cancel_from_dt" ;
			String effectiveCancellationDate = todayAsString;
			String requestedCancellationDate = todayAsString;
			ShoppingCart shoppingCart = ShoppingCart.builder().build();
			shoppingCart.setId(dtSapCancelReserve.getSapReserveNo());
			shoppingCart.setType("ShoppingCart");
			
			SapCancelShoppingCartRequest req = SapCancelShoppingCartRequest.builder().build() ;
			req.setCancellationReason(cancellationReason) ;
			req.setEffectiveCancellationDate(effectiveCancellationDate);
			req.setRequestedCancellationDate(requestedCancellationDate);
			req.setShoppingCart(shoppingCart);
			req.setType("CancelShoppingCart");
			res = callCancelShoppingCart(req);
		
			insertSapReserveLog(req,res);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res ;
	}
	
	
    private void insertSapReserveLog(SapCancelShoppingCartRequest request, SapCancelShoppingCartResponse cancelShoppingCartResponse) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Date date = new Date();

        MasterValue masterValue = new MasterValue();
        masterValue.setCreatedBy("DTAPP");
        masterValue.setCreated(date);
        masterValue.setLastUpdBy("DTAPP");
        masterValue.setLastUpd(date);
        
        DtSapReserveLog sapReserveLog = new DtSapReserveLog();
        if (BeanUtil.isEmpty(cancelShoppingCartResponse.getMessage())) {
            sapReserveLog.setShoppingcartid(
                    BeanUtil.isNotEmpty(cancelShoppingCartResponse.getShoppingCart()) 
                            ? cancelShoppingCartResponse.getShoppingCart().getId() 
                            : null
            );
        }
        sapReserveLog.setParaInput(gson.toJson(request));
        sapReserveLog.setResponse(gson.toJson(cancelShoppingCartResponse));
        sapReserveLog.setCreateValue(masterValue);

        dtSapReserveLogDao.insert(sapReserveLog);
    }
    
	public SapQueryStockOutput sapQueryStockNonSerial(QueryProductStockRequest input) {
		SapQueryStockOutput ress = null;
		HttpClientUtilDT req = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        Gson g = new Gson();
    	String todayAsString = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US).format(new Date());
    	
    	String url = dTConfig.getUrl().getSapquerystocknonserial();
    	
    	List<LovMaster> apikeyqueryStock = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_NON_SER_KEY", "SAP_S4_API_NON_SER_KEY",
				null, null, "Y");
		if(BeanUtil.isNotEmpty(apikeyqueryStock)){
			properties.put("x-api-key", apikeyqueryStock.get(0).getLovVal());
		}
//		properties.put("Ocp-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovVal());
//		properties.put("Bff-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovAttribute01());
//		properties.put("x-request-id", "DT-"+todayAsString);
//		properties.put("x-transaction-id", "DT-"+todayAsString);
//		properties.put("x-session-id", "DT-"+todayAsString);
		
		List<LovMaster> timeout = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_NON_SER_TIMEOUT", "SAP_S4_API_NON_SER_TIMEOUT",
				null, null, "Y");

		String timeoutS = "";
		if (BeanUtil.isNotEmpty(timeout)) {
			timeoutS = timeout.get(0).getLovVal();
		}
		
		SapQueryStockInput inputSap = new SapQueryStockInput();
		
    	inputSap.setPartnerName("DT");
		inputSap.setPartnerMessageID("DT"+todayAsString);
		inputSap.setMaterialNumber(input.getListMatCode());
		inputSap.setNoZero("X");
		List<LocMapPlant> listLovPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(input.getLocationCode()), input.getCompanyCode());
		if(BeanUtil.isNotEmpty(listLovPlant)) {
			List<String> listPlant = new ArrayList<String>();
			List<String> listStorage = new ArrayList<String>();
			listPlant.add(listLovPlant.get(0).getPk().getPlantCode());
			listStorage.add(listLovPlant.get(0).getSapStorageCode());
			
			inputSap.setPlant(listPlant);
			inputSap.setStorageLocation(listStorage);
		}
		
		log.info(g.toJson(inputSap));
		String resSap = req.HttpClient(properties, url, g.toJson(inputSap), "POST", timeoutS);
		log.info(resSap);
		if(resSap != null) {
			try {
				ress = g.fromJson(resSap, SapQueryStockOutput.class) ;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
       return ress ;
	}
}
