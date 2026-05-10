package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelSaleSameDayService;
import th.co.ais.dt.entity.pm.ReceiptMst;
import th.co.ais.dt.entity.sap.DtSapGoodsIssueOrder;
import th.co.ais.dt.entity.sap.DtSapOrderHReturn;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCancel;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostDiscountItems;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostMappingTender;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapPostVoid;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.pm.IReceiptMstDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapGoodsIssueOrderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHPartnerFDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHReturnDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemCancelDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemSerialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostMappingTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTaxItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostVoidDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.IProductMstDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryTenderBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCancelSaleSameDayServiceImpl implements ISapHandleTransactionTypeCancelSaleSameDayService {
	
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;
	private final IDtSapPostHeaderDao dtSapPostHeaderDao;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao;
	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao;
	private final IDtSapPostTenderDao dtSapPostTenderDao;
	private final ISapCallApiService sapCallApiService;
	private final IDtSapTransactionDao dtSapTransactionDao;
	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao;
	private final DTConfig dTConfig;
	
	private final IDtSapOrderHConDao dtSapOrderHConDao ;
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderHPartnerFDao dtSapOrderHPartnerFDao;
	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	
	private final ILovMasterDao lovMasterDao ;
	private final IDtSapOrderHReturnDao dtSapOrderHReturnDao ;
	
	private final IProductMstDao productMstDao ;
	
	private final IDtSapPostVoidDao dtSapPostVoidDao ;
	
	private final IReceiptMstDao receiptMstDao ;
	private final IDtSapGoodsIssueOrderDao dtSapGoodsIssueOrderDao ;
	
	
	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(TransactionType);
	}
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql2
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1Cancel(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = null ;
			if(dtSapTransaction.getTransactionType() == 52L) {  // case replace before cancel
				config = new DtSapTransactionTypeCodeConfig();
				config.setTransactiontypecode("Z001");
				config.setRetailtypecode("ZWRT");
				config.setRetailreasoncode("ZR01");;
				;
			}else {
				config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
			}
			
//			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
//			List<DtSapPostSalesItems> listDtSapPostSalesItems = new ArrayList<>();
//			List<DtSapPostDiscountItems> listDtSapPostDiscountItems = new ArrayList<>();
//			List<DtSapPostTaxItems> listDtSapPostTaxItems = new ArrayList<>();
//			List<DtSapPostDiscountHeader> listDtSapPostDiscountHeader = new ArrayList<>();
//			List<DtSapPostTender> listDtSapPostTender = new ArrayList<>();
//			List<DtSapPostFinalcial> listDtSapPostFinalcial = new ArrayList<>();
//			List<DtSapPostGoodsMovement> listDtSapPostGoodsMovement = new ArrayList<>();
			
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID()); 
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());  
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			if (dtSapTransaction.getTransactionType() == 16L) {
				dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER());
			}else if(dtSapTransaction.getTransactionType() == 22L){
				//dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
				//dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			}
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			// DISCOUNT HEADER======================
			boolean disHeader = false ;
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
				disHeader = true ;
//				DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//				dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//				dtSapPostDiscountHeader.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//				dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//				dtSapPostDiscountHeader.setREDUCTIONAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				
//				dtSapPostDiscountHeader.setCreateValue(masterValue);
//				//set herder id
//				dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//				dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
			}
			
			//END DISCOUNT HEADER======================
			
			//TENDER
			BigDecimal sumTenderAmount = new BigDecimal(0L);
			BigDecimal sumSaleAmount = new BigDecimal(0L);
			List<QueryTenderBean> tendersTmp = queryTenderPostTransactionSql1Cancel(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
			List<QueryTenderBean> tenders = new ArrayList<>();
			if(BeanUtil.isNotEmpty(tendersTmp)) {
				int seq = 1 ;
				for (QueryTenderBean el : tendersTmp) {
					if(!el.getTENDERTYPECODE().equals("ZT83")) {
						el.setTENDERSEQUENCENUMBER(String.valueOf(seq));
						tenders.add(el);
						seq +=1;
					}
				}
			}
			
			if(BeanUtil.isNotEmpty(tenders)) {
				boolean haveAp = false ;
				for (QueryTenderBean tender : tenders) {
					DtSapPostTender t = new DtSapPostTender();
					
					t.setTENDERSEQUENCENUMBER(tender.getTENDERSEQUENCENUMBER());
					t.setTENDERTYPECODE(tender.getTENDERTYPECODE());
					
					
					if(t.getTENDERTYPECODE().equals("ZT05")) {
						haveAp = true ;
						if(disHeader) {
							BigDecimal disAp = new BigDecimal(tender.getTENDERAMOUNT()) ;
							BigDecimal disHerder = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT()) ;
							BigDecimal summ = disAp.add(disHerder) ;
							t.setTENDERAMOUNT(summ.toString());
						}else {
							t.setTENDERAMOUNT(tender.getTENDERAMOUNT());
						}
					}else {
						t.setTENDERAMOUNT(tender.getTENDERAMOUNT());
					}
					
					//t.setTENDERAMOUNT(tender.getTENDERAMOUNT());
					BigDecimal tenderAmount = new BigDecimal(t.getTENDERAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
					t.setTENDERCURRENCY(tender.getTENDERCURRENCY());
					t.setCreateValue(masterValue);
					//set herder id
					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					dtSapPostTenderDao.insert(t);
				}
				
				if(!haveAp && disHeader) {
					DtSapPostTender t = new DtSapPostTender();
					t.setTENDERSEQUENCENUMBER(String.valueOf(tenders.size()+1));
					t.setTENDERTYPECODE("ZT05");
					t.setTENDERAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
					t.setTENDERCURRENCY("THB");
					t.setCreateValue(masterValue);
					//set herder id
					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					dtSapPostTenderDao.insert(t);
					BigDecimal tenderAmount = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
				}
			}
			//END TENDER
			
			// query sql saleOrder
			List<QuerySaleOrderBean> listQuerySaleOrderBean = dtSapTransactionTypeDao.querySaleOrderSqlCancelSaleNormal(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo(), Arrays.asList(18L, 19L));
			int i = 0;
			List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			int itemNo = 1 ;
			for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
				
				//SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
				
				// from config
				String[] retailtypecode = config.getRetailtypecode().split("\\|") ;
				if(retailtypecode.length > 1) {
					dtSapPostSalesItems.setRETAILTYPECODE("Y".equals(el.getFREEGOODS_WAIT_FLG()) ? retailtypecode[1] : retailtypecode[0]);
				}else {
					dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
				}
				
				// from config ***
				String[] retailreasoncode = config.getRetailreasoncode().split("\\|") ;
				
				if(dtSapTransaction.getTransactionType() == 16 ) {
					//toSubStock is not null
					//retailreasoncode = ZR01|ZR04|ZR02
					String to_subStock = retailreasoncode[0];
					
				
					if("SERVICE".equals(el.getPRODUCT_TYPE())) {
						dtSapPostSalesItems.setRETAILREASONCODE(to_subStock);
					}else {
						dtSapPostSalesItems.setRETAILREASONCODE("Y".equals(el.getFREEGOODS_WAIT_FLG()) ? retailreasoncode[1] : to_subStock);						
					}
				}
				else {
					if(retailreasoncode.length > 1) {
						if("SERVICE".equals(el.getPRODUCT_TYPE())) {
							dtSapPostSalesItems.setRETAILREASONCODE("ZR02");
						}else {
							dtSapPostSalesItems.setRETAILREASONCODE("Y".equals(el.getFREEGOODS_WAIT_FLG()) ? retailreasoncode[1] : retailreasoncode[0]);						
						}
					}else {
						dtSapPostSalesItems.setRETAILREASONCODE("SERVICE".equals(el.getPRODUCT_TYPE()) ? "ZR02" : config.getRetailreasoncode());
					}
				}
				
				
				
				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				dtSapPostSalesItems.setITEMID(el.getITEMID());
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT());
				
				BigDecimal saleAmount = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
				
				dtSapPostSalesItems.setNORMALSALESAMOUNT(el.getNORMALSALESAMOUNT());
				dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(el.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
				dtSapPostSalesItems.setACTUALUNITPRICE(el.getACTUALUNITPRICE());
				dtSapPostSalesItems.setSO_NO(el.getSO_NO());
				dtSapPostSalesItems.setITEM_NO(el.getITEM_NO());
				
				for (QuerySaleOrderBean saleOrder : listQuerySaleOrderBean) {
					if(saleOrder.getMaterialNumber().equals(el.getITEMID())) {
						dtSapPostSalesItems.setSO_NO(saleOrder.getSalesOrderDocument());
						dtSapPostSalesItems.setITEM_NO(saleOrder.getItem());
						break;
					}
				}

				dtSapPostSalesItems.setIUID(el.getIUID());
				
				dtSapPostSalesItems.setCreateValue(masterValue);
				//set herder id
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
				itemNo = itemNo +1 ;
				
				//DISCOUNT ITEM
				if(BeanUtil.isNotEmpty(el.getDISCOUNTSEQUENCENUMBER())) {
					DtSapPostDiscountItems dtSapPostDiscountItems = new DtSapPostDiscountItems();
					dtSapPostDiscountItems.setDISCOUNTID(el.getDISCOUNTID());
					dtSapPostDiscountItems.setDISCOUNTSEQUENCENUMBER(el.getDISCOUNTSEQUENCENUMBER());
					dtSapPostDiscountItems.setDISCOUNTTYPECODE(el.getDISCOUNTTYPECODE());
					dtSapPostDiscountItems.setREDUCTIONAMOUNT(el.getREDUCTIONAMOUNT().replace("-", ""));

					dtSapPostDiscountItems.setCreateValue(masterValue);
					dtSapPostDiscountItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
					
					dtSapPostDiscountItemsDao.insert(dtSapPostDiscountItems);
				}
				
				
				//TAX ITEM
				if(BeanUtil.isNotEmpty(el.getTAXSEQUENCENUMBER())) {
					DtSapPostTaxItems dtSapPostTaxItems = new DtSapPostTaxItems();
					dtSapPostTaxItems.setTAXAMOUNT(el.getTAXAMOUNT());
					dtSapPostTaxItems.setTAXSEQUENCENUMBER(el.getTAXSEQUENCENUMBER());
					dtSapPostTaxItems.setTAXTYPECODE(taxtCode.get(0).getLovVal());
					
					dtSapPostTaxItems.setCreateValue(masterValue);
					dtSapPostTaxItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
					dtSapPostTaxItemsDao.insert(dtSapPostTaxItems);
				}
				i++;
			}
			
			if(sumSaleAmount.compareTo(sumTenderAmount) != 0) {
				DtSapPostTender t = new DtSapPostTender();
				t.setTENDERSEQUENCENUMBER(String.valueOf(tenders.size()+1));	
				t.setTENDERTYPECODE("ZT83");
				
				BigDecimal diff = sumSaleAmount.subtract(sumTenderAmount) ;
				
				t.setTENDERAMOUNT(diff.toString());
				
				t.setTENDERCURRENCY(tenders.get(0).getTENDERCURRENCY());
				t.setCreateValue(masterValue);
				//set herder id
				t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostTenderDao.insert(t);
			}
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			log.error("queryInfoAndInsertPostTransaction", e);
			return null;
		}
	}
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransactionCNMEMO(DtSapTransaction dtSapTransaction) throws DataAccessException {
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			//query deposit transactionType 12
			//List<DtSapTransaction> listDtSapTransaction12 = dtSapTransactionDao.getDtSapTransactionByTranSactionTypeAndDocNo(12L,dtSapTransaction.getDocNo());			
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
			
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			if(BeanUtil.isEmpty(listDtSapOld)) {
				listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			}
			//List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 12L) ;
			
			//set values
			// HEADER======================
			List<DtSapPostHeader> listDtSapPostHeaderType12 = dtSapPostHeaderDao.getDtSapPostHeaderBySapTranIdAndStatus(listDtSapOld.get(0).getSapTranId(),"S");
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listDtSapPostHeaderType12.get(0).getRETAILSTOREID()); 
			
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.US);  
			SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd",Locale.US); 


			//
			dtSapPostHeader.setBUSINESSDAYDATE(dt2.format(new Date()));  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listDtSapPostHeaderType12.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listDtSapPostHeaderType12.get(0).getTRANSACTIONSEQUENCENUMBER()+"_C");  
			dtSapPostHeader.setBEGINDATETIMESTAMP(dt.format(new Date()));  
			dtSapPostHeader.setENDDATETIMESTAMP(dt.format(new Date()));  
			dtSapPostHeader.setOPERATORID(listDtSapPostHeaderType12.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listDtSapPostHeaderType12.get(0).getTRANSACTIONCURRENCY());  
			
			List<LovMaster> soldToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", "SAP_S4_SOLD_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;
			
			List<DtSapOrderHeader> listDtSapOrderHeader = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId());
			
			List<LovMaster> soldToOneTimeByCh = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", listDtSapOrderHeader.get(0).getCustomerPurchaseOrderType(), dtSapTransaction.getCompany(), null, "Y") ;

			String soldToOneTimeSAP = soldToOneTime.get(0).getLovVal();
			if(BeanUtil.isNotEmpty(soldToOneTimeByCh)) {
				soldToOneTimeSAP = soldToOneTimeByCh.get(0).getLovVal();
			}
			
			dtSapPostHeader.setPARTNERID(soldToOneTimeSAP);  
			dtSapPostHeader.setORIGTRANSNUMBER(listDtSapPostHeaderType12.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			//set value SALES ITEM
			int itemNo = 1 ;
			List<DtSapPostSalesItems> listDtSapPostSalesItemsType12 = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderId(listDtSapPostHeaderType12.get(0).getSapPostHeaderId());
				for(DtSapPostSalesItems item:listDtSapPostSalesItemsType12) {
					
					DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
					//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(item.getRETAILSEQUENCENUMBER());
					dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
					dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
					dtSapPostSalesItems.setRETAILREASONCODE(item.getRETAILREASONCODE());
					dtSapPostSalesItems.setITEMIDQUALIFIER(item.getITEMIDQUALIFIER());
					dtSapPostSalesItems.setITEMID(item.getITEMID());
					dtSapPostSalesItems.setRETAILQUANTITY(item.getRETAILQUANTITY());
					dtSapPostSalesItems.setSALESUNITOFMEASURE(item.getSALESUNITOFMEASURE());
					dtSapPostSalesItems.setSALESAMOUNT(item.getSALESAMOUNT());
					dtSapPostSalesItems.setNORMALSALESAMOUNT(item.getNORMALSALESAMOUNT());
					dtSapPostSalesItems.setPROMOTIONID(item.getPROMOTIONID());
					dtSapPostSalesItems.setBATCHID(item.getBATCHID());
					dtSapPostSalesItems.setSERIALNUMBER(item.getSERIALNUMBER());
					dtSapPostSalesItems.setACTUALUNITPRICE(item.getACTUALUNITPRICE());
					dtSapPostSalesItems.setSO_NO("");
					dtSapPostSalesItems.setITEM_NO(item.getITEM_NO());
					dtSapPostSalesItems.setIUID("");
					dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					
					dtSapPostSalesItems.setCreateValue(masterValue);
					dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
					itemNo = itemNo +1 ;
					
					List<DtSapPostDiscountItems> listDtSapPostDiscountItems =  dtSapPostDiscountItemsDao.getDtSapPostDiscountItemsBySaleItemIdId(item.getSapPostSalesItemId());
					if(BeanUtil.isNotEmpty(listDtSapPostDiscountItems)) {
						DtSapPostDiscountItems dtSapPostDiscountItems = new DtSapPostDiscountItems();
						dtSapPostDiscountItems.setCreateValue(masterValue);	
						dtSapPostDiscountItems.setDISCOUNTID(listDtSapPostDiscountItems.get(0).getDISCOUNTID());
						dtSapPostDiscountItems.setDISCOUNTSEQUENCENUMBER(listDtSapPostDiscountItems.get(0).getDISCOUNTSEQUENCENUMBER());
						dtSapPostDiscountItems.setDISCOUNTTYPECODE(listDtSapPostDiscountItems.get(0).getDISCOUNTTYPECODE());
						dtSapPostDiscountItems.setREDUCTIONAMOUNT(listDtSapPostDiscountItems.get(0).getREDUCTIONAMOUNT().replace("-", ""));
						dtSapPostDiscountItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
						dtSapPostDiscountItemsDao.insert(dtSapPostDiscountItems);;
						}
					
					
					List<DtSapPostTaxItems> listDtSapPostTaxItems = dtSapPostTaxItemsDao.getDtSapPostTaxItemsBySaleItemIdId(item.getSapPostSalesItemId());
					if(BeanUtil.isNotEmpty(listDtSapPostTaxItems)) {
						DtSapPostTaxItems dtSapPostTaxItemsDeposit = new DtSapPostTaxItems();
			         	
						dtSapPostTaxItemsDeposit.setTAXAMOUNT(listDtSapPostTaxItems.get(0).getTAXAMOUNT());
						dtSapPostTaxItemsDeposit.setTAXSEQUENCENUMBER("1");
						dtSapPostTaxItemsDeposit.setTAXTYPECODE(listDtSapPostTaxItems.get(0).getTAXTYPECODE());
						
						dtSapPostTaxItemsDeposit.setCreateValue(masterValue);
						dtSapPostTaxItemsDeposit.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
						dtSapPostTaxItemsDao.insert(dtSapPostTaxItemsDeposit);
					}
					
					
				}		
			//END Sales item

			//set value TENDER
			List<DtSapPostTender> listTender = dtSapPostTenderDao.getDtSapPostTenderByHeaderId(listDtSapPostHeaderType12.get(0).getSapPostHeaderId());
				for(DtSapPostTender t:listTender) {
					DtSapPostTender newTender = new DtSapPostTender();
					newTender.setTENDERSEQUENCENUMBER(t.getTENDERSEQUENCENUMBER());
					newTender.setTENDERTYPECODE(t.getTENDERTYPECODE());
					newTender.setTENDERAMOUNT(t.getTENDERAMOUNT());
					newTender.setTENDERCURRENCY(t.getTENDERCURRENCY());
					newTender.setTENDERID(t.getTENDERID());
					newTender.setREFERENCEID(t.getREFERENCEID());
					newTender.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					
					newTender.setCreateValue(masterValue);
					dtSapPostTenderDao.insert(newTender);
				}
			//END TENDER
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		DtSapPostHeader res = sapCallApiService.callPostTransactionApi(stSapPostHeader);
		return res ;
	}

	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
		try {
			//DtSapTransaction dtSapTransactionForUpdate = dtSapTransactionDao.getByLongPrimaryKey(dtSapTransaction.getSapTranId());
			//dtSapTransactionForUpdate.setStatus(dtSapTransaction.getStatus());
			dtSapTransaction.getCreateValue().setLastUpd(new Date());
			dtSapTransactionDao.update(dtSapTransaction);
		}catch (Exception e) {
			log.error("updateDtSapTransactionAfterWorkflow", e);
		}
	}

	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		}catch (Exception e) {
			log.error("updateDtSapPostHeader", e);
		}
	}
	
	@Override
	public DtSapTransaction queryDtSapTransactionById(Long sapTranId) {
		return dtSapTransactionDao.getByLongPrimaryKey(sapTranId);
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql1CancelOnPrem(String company, String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql1Cancel",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
	}
	
	private List<QueryTenderBean> queryTenderPostTransactionSql1CancelOnPrem(String company, String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryTenderPostTransactionSql1Cancel", gson.toJson(in), "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryTenderBean() ;
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql1Cancel(String company, String docNo){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.queryPostTransactionSql1Cancel(company, docNo);
		 }  
		  else { 
			// query from on prem
				return queryPostTransactionSql1CancelOnPrem(company, docNo);
		 }	
	}
	
	private List<QueryTenderBean> queryTenderPostTransactionSql1Cancel(String company, String docNo){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return  dtSapTransactionTypeDao.queryTenderPostTransactionSql1Cancel(company, docNo);
		 }  
		  else { 
			// query from on prem
				return queryTenderPostTransactionSql1CancelOnPrem(company, docNo);
		 }	
	}
	
	//Order
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql saleOrder
			//List<QuerySaleOrderBean> listQuerySaleOrderBean = dtSapTransactionTypeDao.querySaleOrderSqlCancelSaleNormal(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo(), Arrays.asList(18L, 19L)); 
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			if(BeanUtil.isEmpty(listDtSapOld)) {
				listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			}
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			
			
			if(BeanUtil.isNotEmpty(listDtSapOld)) {
				// insert header				
				dtSapOrderHeader = new DtSapOrderHeader();
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("U");
				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHeader.setStatus("W");
				
				dtSapOrderHeader.setSalesDocType("");
				dtSapOrderHeader.setSalesOrganiztion("");
				dtSapOrderHeader.setDistributionChannel("");
				dtSapOrderHeader.setDivision("");
				dtSapOrderHeader.setShippingConditions("");
				
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				for (DtSapOrderItem el : listOrderItemOld) {					
					///
					//ITEM
					List<ProductMst> mat = productMstDao.getProductMstByUnique(dtSapTransaction.getCompany(), el.getMaterialNumber()) ;
					if(!mat.get(0).getProductType().equals("SERVICE")) {
						DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
						dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
						dtSapOrderItem.setChangeMode("U");
						dtSapOrderItem.setItem(el.getItem());
						dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
						
						dtSapOrderItem.setQuantity("");
						dtSapOrderItem.setSalesUnit("");
						dtSapOrderItem.setRequestedDeliveryDate("");
						dtSapOrderItem.setPlant("");
						dtSapOrderItem.setStorageLocation("");
						dtSapOrderItem.setBillingBlock("");
						dtSapOrderItem.setDeliveryPriority("");
						dtSapOrderItem.setItemCategory("");
			
						dtSapOrderItem.setCreateValue(masterValue);
						dtSapOrderItemDao.insert(dtSapOrderItem);
						
						////ITEM cancel
						DtSapOrderItemCancel dtSapOrderItemCancel = new DtSapOrderItemCancel();
						dtSapOrderItemCancel.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
						dtSapOrderItemCancel.setChangeMode("U");
						dtSapOrderItemCancel.setReasonforRejection("70");
						
						dtSapOrderItemCancel.setCreateValue(masterValue);
						dtSapOrderItemCancelDao.insert(dtSapOrderItemCancel);
					}
					
				}
			}
		}catch (Exception e) {
			log.error("queryInfoAndInsertSaleorder", e);
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorderCNMemo(DtSapTransaction dtSapTransaction) throws DataAccessException{
		try {
			DtSapOrderHeader dtSapOrderHeader = null;
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query cancel sales order
			//List<QuerySalesOrderTransactionUpdate> listQueryPostTransactionBean = this.querySalesOrderCancel("AWN",dtSapTransaction.getDocNo(),Long.parseLong("12"));
			
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			if(BeanUtil.isEmpty(listDtSapOld)) {
				listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			}
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			
			
			if(orderHeaderOld != null) {
				//set values
				// HEADER======================
				
				dtSapOrderHeader = new DtSapOrderHeader();
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("C");
				dtSapOrderHeader.setSDDocumentCategory("K");

				dtSapOrderHeader.setStatus("W");
				
				dtSapOrderHeader.setSalesDocType("ZC01");
				dtSapOrderHeader.setSalesOrganiztion(orderHeaderOld.getSalesOrganiztion());
				dtSapOrderHeader.setDistributionChannel(orderHeaderOld.getDistributionChannel());
				dtSapOrderHeader.setDivision(orderHeaderOld.getDivision());
				dtSapOrderHeader.setCustomerReference(orderHeaderOld.getCustomerReference());
				dtSapOrderHeader.setCustomerPurchaseOrderType(orderHeaderOld.getCustomerPurchaseOrderType());
				
				dtSapOrderHeader.setShippingConditions("");
				dtSapOrderHeader.setBillingBlock("/");
				
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				DtSapOrderHReturn dtSapOrderHReturn = new DtSapOrderHReturn();
				dtSapOrderHReturn.setChangeMode("C");
				dtSapOrderHReturn.setOrderReason("Z55");
				dtSapOrderHReturn.setReference(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHReturn.setCreateValue(masterValue);
				dtSapOrderHReturn.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
				dtSapOrderHReturnDao.insert(dtSapOrderHReturn);
				
				//END HEADER======================
							
				//List<LovMaster> planrcnmemo = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_CODE_CN_MEMO", "SAP_S4_PLANT_CODE_CN_MEMO", dtSapTransaction.getCompany(), null, "Y") ;

				for(DtSapOrderItem el : listOrderItemOld) {	
						//ITEM
						DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
						dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
						dtSapOrderItem.setChangeMode("C");
						dtSapOrderItem.setItem(el.getItem());
						dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
						
						dtSapOrderItem.setQuantity(el.getQuantity());
						dtSapOrderItem.setSalesUnit(el.getSalesUnit());
						dtSapOrderItem.setRequestedDeliveryDate("");
						dtSapOrderItem.setItemCategory("ZRC1");
						
						
						dtSapOrderItem.setPlant(el.getPlant());
						dtSapOrderItem.setStorageLocation("");
						dtSapOrderItem.setBillingBlock("");
						dtSapOrderItem.setDeliveryPriority("");
						
						dtSapOrderItem.setCreateValue(masterValue);
						dtSapOrderItemDao.insert(dtSapOrderItem);
						
				}
			}
				
			return dtSapOrderHeader;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
		
	@Override
	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader res = sapCallApiService.callSaleorderApi(dtSapOrderHeader);
		return res ;
	}
	
	@Override
	public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader) {
		try {
			dtSapOrderHeaderDao.update(dtSapOrderHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) {
		List<DtSapPostSalesItems> listDtSapPostSalesItems = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderIdForUpdate(stSapPostHeader.getSapPostHeaderId())  ;
		for(DtSapPostSalesItems i : listDtSapPostSalesItems) {  
			i.setSO_NO(dtSapOrderHeader.getRes_SalesOrderDocument());
			dtSapPostSalesItemsDao.update(i);
		}
	}
	
	public DtSapTransaction insertSapTransaction(String docNo , String company , Long transactionType , String userId) {
    	try {
    		
    		Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(userId);
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(userId);
    		
    		DtSapTransaction dtSapTransaction = new DtSapTransaction();
    		dtSapTransaction.setCompany(company);
    		dtSapTransaction.setDocNo(docNo); 
    		dtSapTransaction.setStatus("W");
    		dtSapTransaction.setTransactionType(transactionType);
    		dtSapTransaction.setCreateValue(masterValue);    
    		dtSapTransactionDao.insert(dtSapTransaction);
    		return dtSapTransaction ;
    	}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
    	
    	
    }
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransactionVoid(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql2
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1Cancel(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
						
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID()); 
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());  
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			// VOID
			
			String wId = listQueryPostTransactionBean.get(0).getWORKSTATIONID();
            List<QueryPostTransactionBean> workId =  dtSapTransactionTypeDao.getWORKSTATIONIDFORVOID(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
            if(BeanUtil.isNotEmpty(workId)) {
            	wId = workId.get(0).getWORKSTATIONID() ;
            }
			
			DtSapPostVoid dtSapPostVoid = new DtSapPostVoid();
			dtSapPostVoid.setVOIDEDRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID());
			dtSapPostVoid.setVOIDEDBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());
			dtSapPostVoid.setVOIDEDWORKSTATIONID(wId);
			dtSapPostVoid.setVOIDEDTRANSACTIONSEQUENCENUMBE(dtSapTransaction.getDocNo());
			dtSapPostVoid.setVOIDEDBEGINTIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
			dtSapPostVoid.setVOIDFLAG("");
			dtSapPostVoid.setTRANSREASONCODE(config.getRetailtypecode());
			
			dtSapPostVoid.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
			dtSapPostVoid.setCreateValue(masterValue);
			dtSapPostVoidDao.insert(dtSapPostVoid);
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			log.error("queryInfoAndInsertPostTransactionVoid", e);
			return null;
		}
	}
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransactionORVoid(DtSapTransaction dtSapTransaction) {
		List<QueryPostTransactionBean> checkReplace= checkIfReplace(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
    	if(BeanUtil.isNotEmpty(checkReplace)) {
    		return queryInfoAndInsertPostTransaction(dtSapTransaction);
    	}else {
    		return queryInfoAndInsertPostTransactionVoid(dtSapTransaction);
    	}
	}
	
	private List<QueryPostTransactionBean> checkIfReplace(String docNo, String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.checkIfReplace(docNo, company);
		 }  
		  else { 
			// query from on prem
				return checkIfReplaceOnPrem(docNo, company);
		 }	
	}
	
	private List<QueryPostTransactionBean> checkIfReplaceOnPrem(String docNo, String company) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/checkIfReplace",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
	}
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate22(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = null ;
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql saleOrder
			//List<QuerySaleOrderBean> listQuerySaleOrderBean = dtSapTransactionTypeDao.querySaleOrderSqlCancelSaleNormal(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo(), Arrays.asList(18L, 19L)); 
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			if(BeanUtil.isEmpty(listDtSapOld)) {
				listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			}
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			//List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			List<DtSapGoodsIssueOrder> listDtSapGoodsIssueOrder = dtSapGoodsIssueOrderDao.queryDtSapGoodsIssueOrder(orderHeaderOld.getCustomerReference());
			
			if(BeanUtil.isNotEmpty(listDtSapOld) && BeanUtil.isNotEmpty(listDtSapGoodsIssueOrder) &&
					(orderHeaderOld.getSalesDocType().equals("ZS07") || orderHeaderOld.getSalesDocType().equals("ZS09")  ) ) {
				// insert header				
				dtSapOrderHeader = new DtSapOrderHeader();
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("U");
				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHeader.setStatus("W");
				
				dtSapOrderHeader.setYourReference("/");
				dtSapOrderHeader.setDeliveryBlock("");
				dtSapOrderHeader.setSalesDocType("");
				dtSapOrderHeader.setSalesOrganiztion("");
				dtSapOrderHeader.setDistributionChannel("");
				dtSapOrderHeader.setDivision("");
				dtSapOrderHeader.setShippingConditions("");
				
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
			}
		}catch (Exception e) {
			log.error("queryInfoAndInsertSaleorder", e);
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransaction16(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql2
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1Cancel(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
			// find deposit item 
			List<QueryPostTransactionBean> listDeposit = queryDepositItem(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
			
			List<QueryPostTransactionBean> checkIfReplace = checkIfReplace(dtSapTransaction.getDocNo() ,dtSapTransaction.getCompany()) ;
			
			if(BeanUtil.isEmpty(listDeposit) || BeanUtil.isEmpty(listQueryPostTransactionBean)) {
				return null ;
			}
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = null ;
			if(BeanUtil.isNotEmpty(checkIfReplace)) {  // case replace before cancel
				config = new DtSapTransactionTypeCodeConfig();
				config.setTransactiontypecode("Z001");
				config.setRetailtypecode("ZWRT");
				config.setRetailreasoncode("ZR01");;
				;
			}else {
				config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
			}
			
//			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
//			List<DtSapPostSalesItems> listDtSapPostSalesItems = new ArrayList<>();
//			List<DtSapPostDiscountItems> listDtSapPostDiscountItems = new ArrayList<>();
//			List<DtSapPostTaxItems> listDtSapPostTaxItems = new ArrayList<>();
//			List<DtSapPostDiscountHeader> listDtSapPostDiscountHeader = new ArrayList<>();
//			List<DtSapPostTender> listDtSapPostTender = new ArrayList<>();
//			List<DtSapPostFinalcial> listDtSapPostFinalcial = new ArrayList<>();
//			List<DtSapPostGoodsMovement> listDtSapPostGoodsMovement = new ArrayList<>();
			
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID()); 
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());  
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			//if (dtSapTransaction.getTransactionType() == 16L) {
				dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER());
			//}else if(dtSapTransaction.getTransactionType() == 22L){
				//dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
				//dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			//}
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			// DISCOUNT HEADER======================
			boolean disHeader = false ;
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
				disHeader = true ;
//				DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//				dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//				dtSapPostDiscountHeader.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//				dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//				dtSapPostDiscountHeader.setREDUCTIONAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				
//				dtSapPostDiscountHeader.setCreateValue(masterValue);
//				//set herder id
//				dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//				dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
			}
			
			//END DISCOUNT HEADER======================
			
			//TENDER
			BigDecimal sumTenderAmount = new BigDecimal(0L);
			BigDecimal sumSaleAmount = new BigDecimal(0L);
			List<QueryTenderBean> tendersTmp = queryTenderPostTransactionSql1Cancel(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
			List<QueryTenderBean> tenders = new ArrayList<>();
			if(BeanUtil.isNotEmpty(tendersTmp)) {
				int seq = 1 ;
				for (QueryTenderBean el : tendersTmp) {
					if(!el.getTENDERTYPECODE().equals("ZT83")) {
						el.setTENDERSEQUENCENUMBER(String.valueOf(seq));
						tenders.add(el);
						seq +=1;
					}
				}
			}
			
			if(BeanUtil.isNotEmpty(tenders)) {
				boolean haveAp = false ;
				for (QueryTenderBean tender : tenders) {
					DtSapPostTender t = new DtSapPostTender();
					
					t.setTENDERSEQUENCENUMBER(tender.getTENDERSEQUENCENUMBER());
					t.setTENDERTYPECODE(tender.getTENDERTYPECODE());
					
					
					if(t.getTENDERTYPECODE().equals("ZT05")) {
						haveAp = true ;
						if(disHeader) {
							BigDecimal disAp = new BigDecimal(tender.getTENDERAMOUNT()) ;
							BigDecimal disHerder = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT()) ;
							BigDecimal summ = disAp.add(disHerder) ;
							t.setTENDERAMOUNT(summ.toString());
						}else {
							t.setTENDERAMOUNT(tender.getTENDERAMOUNT());
						}
					}else {
						t.setTENDERAMOUNT(tender.getTENDERAMOUNT());
					}
					
					//t.setTENDERAMOUNT(tender.getTENDERAMOUNT());
					BigDecimal tenderAmount = new BigDecimal(t.getTENDERAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
					t.setTENDERCURRENCY(tender.getTENDERCURRENCY());
					t.setCreateValue(masterValue);
					//set herder id
					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					dtSapPostTenderDao.insert(t);
				}
				
				if(!haveAp && disHeader) {
					DtSapPostTender t = new DtSapPostTender();
					t.setTENDERSEQUENCENUMBER(String.valueOf(tenders.size()+1));
					t.setTENDERTYPECODE("ZT05");
					t.setTENDERAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
					t.setTENDERCURRENCY("THB");
					t.setCreateValue(masterValue);
					//set herder id
					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					dtSapPostTenderDao.insert(t);
					BigDecimal tenderAmount = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
				}
			}
			//END TENDER
			
			int i = 0;
			List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			int itemNo = 1 ;
			for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
				
				//SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
				
				// from config
				String[] retailtypecode = config.getRetailtypecode().split("\\|") ;
				if(retailtypecode.length > 1) {
					dtSapPostSalesItems.setRETAILTYPECODE("Y".equals(el.getFREEGOODS_WAIT_FLG()) ? retailtypecode[1] : retailtypecode[0]);
				}else {
					dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
				}
				
				// from config ***
				String[] retailreasoncode = config.getRetailreasoncode().split("\\|") ;
				
				//if(dtSapTransaction.getTransactionType() == 16 ) {
					//toSubStock is not null
					//retailreasoncode = ZR01|ZR04|ZR02
					String to_subStock = retailreasoncode[0];
					
				
					if("SERVICE".equals(el.getPRODUCT_TYPE())) {
						dtSapPostSalesItems.setRETAILREASONCODE(to_subStock);
					}else {
						dtSapPostSalesItems.setRETAILREASONCODE("Y".equals(el.getFREEGOODS_WAIT_FLG()) ? retailreasoncode[1] : to_subStock);						
					}
				//}
//				else {
//					if(retailreasoncode.length > 1) {
//						if("SERVICE".equals(el.getPRODUCT_TYPE())) {
//							dtSapPostSalesItems.setRETAILREASONCODE("ZR02");
//						}else {
//							dtSapPostSalesItems.setRETAILREASONCODE("Y".equals(el.getFREEGOODS_WAIT_FLG()) ? retailreasoncode[1] : retailreasoncode[0]);						
//						}
//					}else {
//						dtSapPostSalesItems.setRETAILREASONCODE("SERVICE".equals(el.getPRODUCT_TYPE()) ? "ZR02" : config.getRetailreasoncode());
//					}
//				}
				
				
				
				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				dtSapPostSalesItems.setITEMID(el.getITEMID());
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				
                if(listDeposit.get(0).getITEMID().equals(el.getITEMID())) {  // item main
                	BigDecimal saleAmountBeforeDeposit = new BigDecimal(el.getSALESAMOUNT());
                	BigDecimal depositAmt = new BigDecimal(listDeposit.get(0).getSALESAMOUNT());
                	BigDecimal sumMainAndDeposit = saleAmountBeforeDeposit.add(depositAmt);
                	dtSapPostSalesItems.setSALESAMOUNT(sumMainAndDeposit.toString()); //-----------------------
				}else{
					dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT()); 
				}
				
				
				
				BigDecimal saleAmount = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
				
				dtSapPostSalesItems.setNORMALSALESAMOUNT(el.getNORMALSALESAMOUNT());
				dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(el.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
				dtSapPostSalesItems.setACTUALUNITPRICE(el.getACTUALUNITPRICE());
				dtSapPostSalesItems.setSO_NO(el.getSO_NO());
				dtSapPostSalesItems.setITEM_NO(el.getITEM_NO());
				

				dtSapPostSalesItems.setIUID(el.getIUID());
				
				dtSapPostSalesItems.setCreateValue(masterValue);
				//set herder id
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
				itemNo = itemNo +1 ;
				
				//DISCOUNT ITEM
				if(BeanUtil.isNotEmpty(el.getDISCOUNTSEQUENCENUMBER())) {
					DtSapPostDiscountItems dtSapPostDiscountItems = new DtSapPostDiscountItems();
					dtSapPostDiscountItems.setDISCOUNTID(el.getDISCOUNTID());
					dtSapPostDiscountItems.setDISCOUNTSEQUENCENUMBER(el.getDISCOUNTSEQUENCENUMBER());
					dtSapPostDiscountItems.setDISCOUNTTYPECODE(el.getDISCOUNTTYPECODE());
					dtSapPostDiscountItems.setREDUCTIONAMOUNT(el.getREDUCTIONAMOUNT().replace("-", ""));

					dtSapPostDiscountItems.setCreateValue(masterValue);
					dtSapPostDiscountItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
					
					dtSapPostDiscountItemsDao.insert(dtSapPostDiscountItems);
				}
				
				
				//TAX ITEM
				if(BeanUtil.isNotEmpty(el.getTAXSEQUENCENUMBER())) {
					DtSapPostTaxItems dtSapPostTaxItems = new DtSapPostTaxItems();
					 if(listDeposit.get(0).getITEMID().equals(el.getITEMID())) {  // item main
						BigDecimal taxSaleAmountBeforeDeposit = new BigDecimal(el.getTAXAMOUNT());
		                BigDecimal taxDepositAmt = new BigDecimal(listDeposit.get(0).getTAXAMOUNT());
		                BigDecimal taxSumMainAndDeposit = taxSaleAmountBeforeDeposit.add(taxDepositAmt);
						 dtSapPostTaxItems.setTAXAMOUNT(taxSumMainAndDeposit.toString());   //-----------------------
					 }else {
						 dtSapPostTaxItems.setTAXAMOUNT(el.getTAXAMOUNT());   
					 }
					
					dtSapPostTaxItems.setTAXSEQUENCENUMBER(el.getTAXSEQUENCENUMBER());
					dtSapPostTaxItems.setTAXTYPECODE(taxtCode.get(0).getLovVal());
					
					dtSapPostTaxItems.setCreateValue(masterValue);
					dtSapPostTaxItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
					dtSapPostTaxItemsDao.insert(dtSapPostTaxItems);
				}
				i++;
			}
			
			
			// mat deposit

			
			//SALES ITEM
			List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;
			DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
			dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
			dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
			
			String[] retailreasoncode = config.getRetailreasoncode().split("\\|") ;
			String to_subStock = retailreasoncode[0];
			dtSapPostSalesItems.setRETAILREASONCODE(to_subStock);

			dtSapPostSalesItems.setITEMIDQUALIFIER("2");
			dtSapPostSalesItems.setITEMID(matDeposit.get(0).getLovVal());
			dtSapPostSalesItems.setRETAILQUANTITY("1");
			dtSapPostSalesItems.setSALESUNITOFMEASURE(matDeposit.get(0).getLovAttribute01());
			dtSapPostSalesItems.setSALESAMOUNT(listDeposit.get(0).getSALESAMOUNT().replace("-", "")); 
			
			BigDecimal saleAmount = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
			sumSaleAmount = sumSaleAmount.add(saleAmount);
			
			dtSapPostSalesItems.setNORMALSALESAMOUNT(dtSapPostSalesItems.getSALESAMOUNT());
			//dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
			//dtSapPostSalesItems.setBATCHID(el.getBATCHID());
			//dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
			dtSapPostSalesItems.setACTUALUNITPRICE(dtSapPostSalesItems.getSALESAMOUNT());
			//dtSapPostSalesItems.setSO_NO(el.getSO_NO());
			//dtSapPostSalesItems.setITEM_NO(el.getITEM_NO());
			//dtSapPostSalesItems.setIUID(el.getIUID());
			
			dtSapPostSalesItems.setCreateValue(masterValue);
			//set herder id
			dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
			dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
			itemNo = itemNo +1 ;

			//TAX ITEM
			
				DtSapPostTaxItems dtSapPostTaxItems = new DtSapPostTaxItems();
			
				dtSapPostTaxItems.setTAXAMOUNT(listDeposit.get(0).getTAXAMOUNT().replace("-", "")); 
				dtSapPostTaxItems.setTAXSEQUENCENUMBER("1");
				dtSapPostTaxItems.setTAXTYPECODE(taxtCode.get(0).getLovVal());
				
				dtSapPostTaxItems.setCreateValue(masterValue);
				dtSapPostTaxItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
				dtSapPostTaxItemsDao.insert(dtSapPostTaxItems);
			
			
		
			
			//////////////////////////
			
			if(sumSaleAmount.compareTo(sumTenderAmount) != 0) {
				DtSapPostTender t = new DtSapPostTender();
				t.setTENDERSEQUENCENUMBER(String.valueOf(tenders.size()+1));	
				t.setTENDERTYPECODE("ZT83");
				
				BigDecimal diff = sumSaleAmount.subtract(sumTenderAmount) ;
				
				t.setTENDERAMOUNT(diff.toString());
				
				t.setTENDERCURRENCY(tenders.get(0).getTENDERCURRENCY());
				t.setCreateValue(masterValue);
				//set herder id
				t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostTenderDao.insert(t);
			}
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			log.error("queryInfoAndInsertPostTransaction", e);
			return null;
		}
	}
	
	private List<QueryPostTransactionBean> queryDepositItem(String company, String docNo){
		ReceiptMst receiptMst = receiptMstDao.getRecieptNumAndCompany(docNo, company);
        return dtSapTransactionTypeDao.queryDepositItem(company, receiptMst.getRefNo());
	}
		
}
