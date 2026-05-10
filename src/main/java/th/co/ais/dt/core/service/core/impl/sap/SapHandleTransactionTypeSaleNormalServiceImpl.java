package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapOrderHCon;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHText;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCancel;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapOrderItemSerial;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostDiscountItems;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostMappingTender;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.sk.LocMapPlant;
import th.co.ais.dt.entity.so.SaleOrderTransection;
import th.co.ais.dt.entity.util.CmLocationMst;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapCancelReserveDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHPartnerFDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHTextDao;
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
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.so.ISaleOrderTransectionDao;
import th.co.ais.dt.repository.interfaces.util.ICmLocationMstDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySalesOrderTransactionUpdate;
import th.co.ais.dt.service.core.impl.sap.dto.QueryTenderBean;
import th.co.ais.dt.service.core.impl.so.dto.GetSaleOrderTransectionForCloudBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeSaleNormalServiceImpl implements ISapHandleTransactionTypeSaleNormalService {
	
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao ;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao ;
	private final IDtSapPostHeaderDao dtSapPostHeaderDao ;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao ;
	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao ;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao ;
	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao ;
	private final IDtSapPostTenderDao dtSapPostTenderDao ;
	private final ISapCallApiService sapCallApiService ;
	private final IDtSapTransactionDao dtSapTransactionDao ;
	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao ;
	private final DTConfig dTConfig;
	
	
	private final IDtSapOrderHConDao dtSapOrderHConDao ;
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderHPartnerFDao dtSapOrderHPartnerFDao;
	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	private final IDtSapOrderHTextDao dtSapOrderHTextDao ;
	
	private final ILovMasterDao lovMasterDao ;
	
	private final ILocMapPlantDao locMapPlantDao ;
	private final ICmLocationMstDao cmLocationMstDao ;
	
	private final IDtSapCancelReserveDao dtSapCancelReserveDao ;
	private final ISaleOrderTransectionDao saleOrderTransectionDao ;
	
	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(TransactionType);
	}
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction , Long fixDocTrans) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql1
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
//			List<QueryPostTransactionBean> listQueryPostTransactionBeanForDeposit = null ;
//			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getREF_NO()) && listQueryPostTransactionBean.get(0).getREF_NO().startsWith("D") ) {
//				listQueryPostTransactionBeanForDeposit = queryPostTransactionSql1(listQueryPostTransactionBean.get(0).getREF_NO(), dtSapTransaction.getCompany());
//			}
			
			// query transaction type code config 
			Long transType = Long.valueOf(dtSapTransaction.getTransactionType()) ;
			if(fixDocTrans != null) {
				transType = fixDocTrans ;
			}
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(transType) ;
			
			
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
			
			if(dtSapTransaction.getTransactionType() == 18 || dtSapTransaction.getTransactionType() == 19 ) {
				List<LovMaster> soldToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", "SAP_S4_SOLD_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;
				
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(dtSapTransaction.getSapTranId());
				List<LovMaster> soldToOneTimeByCh = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", listDtSapOrderHeader.get(0).getCustomerPurchaseOrderType(), dtSapTransaction.getCompany(), null, "Y") ;

				String soldToOneTimeSAP = soldToOneTime.get(0).getLovVal();
				if(BeanUtil.isNotEmpty(soldToOneTimeByCh)) {
					soldToOneTimeSAP = soldToOneTimeByCh.get(0).getLovVal();
				}
				
				dtSapPostHeader.setPARTNERID(soldToOneTimeSAP);  
				String origtransnumber = "" ;
				if(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER().length() <= 20) {
					origtransnumber = listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER() ;
				}else {
					origtransnumber = listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER().substring(0, 20) ;
				}
				
				dtSapPostHeader.setORIGTRANSNUMBER(origtransnumber); 
			}
			
			
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			BigDecimal sumTenderAmount = new BigDecimal(0L);
			boolean disHeader = false ;
			// DISCOUNT HEADER======================
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
				disHeader = true ;
//				DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//				dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//				dtSapPostDiscountHeader.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//				dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//				dtSapPostDiscountHeader.setREDUCTIONAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				BigDecimal discountHeaderAmount = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				sumTenderAmount = sumTenderAmount.add(discountHeaderAmount);
//				dtSapPostDiscountHeader.setCreateValue(masterValue);
//				//set herder id
//				dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//				dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
			}
			
			//END DISCOUNT HEADER======================
			
			//TENDER
			
			List<QueryTenderBean> tenders = queryTenderPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			if(BeanUtil.isNotEmpty(tenders)) {
				boolean haveAp = false ;
				for(int i = 0 ; i < tenders.size() ; i ++) {
					DtSapPostTender t = new DtSapPostTender();
					t.setTENDERSEQUENCENUMBER(String.valueOf(i+1));
					
					if(tenders.get(i).getTENDERSEQUENCENUMBER().equals("99")) {
						t.setTENDERTYPECODE(tenders.get(i).getTENDERTYPECODE());
					}else if(BeanUtil.isNotEmpty(tenders.get(i).getFOC_FLG()) && tenders.get(i).getFOC_FLG().equals("Y")) {
						List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender("FOC", null) ;
						t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
					}
					else {
						List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(i).getTENDERTYPECODE(), null) ;
						if(fDtSapPostMappingTender.size() == 1) {
							t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
						}else {
							List<DtSapPostMappingTender> sDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(i).getTENDERTYPECODE(), tenders.get(i).getDOCTYPE()) ;
							t.setTENDERTYPECODE(sDtSapPostMappingTender.get(0).getTenderTypeId());
						}	
					}
					
					
					
					if(t.getTENDERTYPECODE().equals("ZT05")) {
						haveAp = true ;
						if(disHeader) {
							BigDecimal disAp = new BigDecimal(tenders.get(i).getTENDERAMOUNT()) ;
							BigDecimal disHerder = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT()) ;
							BigDecimal summ = disAp.add(disHerder) ;
							t.setTENDERAMOUNT(summ.toString());
						}else {
							t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
						}
					}else {
						t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
					}
					
					
					BigDecimal tenderAmount = new BigDecimal(t.getTENDERAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
					t.setTENDERCURRENCY(tenders.get(i).getTENDERCURRENCY());
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
			
			BigDecimal sumSaleAmount = new BigDecimal(0L);
			List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			int itemNo = 1 ;
			for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
				
				//SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
				
				// from config
				dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
				
				// fron config ***
				String[]  Retailreasoncode = config.getRetailreasoncode().split("\\|") ;
				if(Retailreasoncode.length == 1) {
					if(el.getPRODUCT_TYPE().equals("SERVICE")) {  // type  33 , 35
						dtSapPostSalesItems.setRETAILREASONCODE("ZR01");
					}else {
						dtSapPostSalesItems.setRETAILREASONCODE(config.getRetailreasoncode());
					}
					
				}else {
					dtSapPostSalesItems.setRETAILREASONCODE((el.getFREEGOODS_WAIT_FLG().equals("Y") ? Retailreasoncode[1] : Retailreasoncode[0]) );
				}
				
				
				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				
				dtSapPostSalesItems.setITEMID(el.getITEMID());  ///////////
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				
//				if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
//					BigDecimal EXC_AMTMain = new BigDecimal(el.getEXC_AMT());
//					BigDecimal vatMain = new BigDecimal(el.getTAXAMOUNT());
//					BigDecimal EXC_AMTDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getEXC_AMT());	
//					BigDecimal SALESAMOUNT = EXC_AMTMain.subtract(EXC_AMTDeposit).add(vatMain) ;
//					dtSapPostSalesItems.setSALESAMOUNT(SALESAMOUNT.toString());
//					
//					BigDecimal PriceMain = new BigDecimal(el.getNORMALSALESAMOUNT());
//					BigDecimal PriceDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
//					BigDecimal NORMALSALESAMOUNT = PriceMain.subtract(PriceDeposit);
//					dtSapPostSalesItems.setNORMALSALESAMOUNT(NORMALSALESAMOUNT.toString());  
//					
//					BigDecimal ACTUALUNITPRICEMain = new BigDecimal(el.getACTUALUNITPRICE());
//					BigDecimal ACTUALUNITPRICE = ACTUALUNITPRICEMain.subtract(PriceDeposit);
//					
//					dtSapPostSalesItems.setACTUALUNITPRICE(ACTUALUNITPRICE.toString());   ///////////
//				}else {
					dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT());  ///////////
					dtSapPostSalesItems.setNORMALSALESAMOUNT(el.getNORMALSALESAMOUNT());  ///////////
					dtSapPostSalesItems.setACTUALUNITPRICE(el.getACTUALUNITPRICE());   ///////////
				//}
				
				BigDecimal saleAmount = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
				
				dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(el.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
				
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
					
//					if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
//						BigDecimal PriceMain = new BigDecimal(dtSapPostSalesItems.getNORMALSALESAMOUNT());
//						BigDecimal PriceMainDis = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
//						BigDecimal REDUCTIONAMOUNT = PriceMain.subtract(PriceMainDis);
//						dtSapPostDiscountItems.setREDUCTIONAMOUNT(REDUCTIONAMOUNT.toString());
//						 
//					}else {
						dtSapPostDiscountItems.setREDUCTIONAMOUNT("-"+el.getREDUCTIONAMOUNT());   ///////////
					//}
					
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
				
			
			}
			
			//adjust tender
			//if(!sumSaleAmount.equals(sumTenderAmount)) {
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
			
			//MasterValue createValue = new MasterValue();
			//createValue.setCreated(dtSapTransaction.getCreateValue().getCreated());
			//createValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			//createValue.setLastUpd(new Date());
			//createValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());;
			dtSapTransaction.getCreateValue().setLastUpd(new Date());
			dtSapTransactionDao.update(dtSapTransaction);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	@Override
	public DtSapTransaction queryDtSapTransactionById(Long sapTranId) {
	  return dtSapTransactionDao.getByLongPrimaryKey(sapTranId);	
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql1(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
			return dtSapTransactionTypeDao.queryPostTransactionSql1(docNo, company);
		}else {
			// query from on prem
			return queryPostTransactionSql1OnPrem(docNo, company);
		}	
	}
	
	private List<QueryTenderBean> queryTenderPostTransactionSql1(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return  dtSapTransactionTypeDao.queryTenderPostTransactionSql1(docNo, company);
		 }  
		  else { 
			// query from on prem
				return queryTenderPostTransactionSql1OnPrem(docNo, company);
		 }	
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql1OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql1",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	private List<QueryTenderBean> queryTenderPostTransactionSql1OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryTenderPostTransactionSql1",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryTenderBean() ;
   }

	@Override
	public List<DtSapTransaction> getDtSapTransactionStatusF() throws DataAccessException {
		return dtSapTransactionDao.getDtSapTransactionStatusF();
	}

	@Override
	public List<DtSapPostHeader> getDtSapPostHeaderBySapTranId(Long sapTranId) {
		return dtSapPostHeaderDao.getDtSapPostHeaderBySapTranId(sapTranId);
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder1819(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql1
			List<QuerySaleOrderBean> listQuerySaleOrderBean = querySaleOrderSql1819(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ,dtSapTransaction.getTransactionType());
			
			if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
				boolean dropPartnerFlg = false ;  // ZS08  click and ship
				boolean homeFlg = false ;         // ZS08  click and ship 
				
				boolean dropAisShopFlg = false ;  // Zs07 click and Collect
				boolean stockAisShopFlg = false ; // Zs07 click and Collect
				
				boolean preBookHome = false ;     // ZS10 Pre-booking&Ship
				boolean preBookDropAis = false ;  //  ZS09 Pre-booking&Collect
				
				boolean esimFlg = false ;  //  ZS0E esim
				
				boolean omniPartner_home = false ;  //   Z027 
				boolean omniPartner_shop = false ;  //   Z028
				

				if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getSapPrebookFlg()) &&
				   listQuerySaleOrderBean.get(0).getSapPrebookFlg().equals("Y")) {
					
					if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
							listQuerySaleOrderBean.get(0).getDeliveryType().equals("DROPSHOP")) {
						preBookDropAis = true ;
						dtSapOrderHeader.setSalesDocType("ZS09");
						dtSapOrderHeader.setCompleteDlv("X");
					}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
							listQuerySaleOrderBean.get(0).getDeliveryType().equals("H")) {
						preBookHome = true ;
						dtSapOrderHeader.setSalesDocType("ZS10");
					}
				}else if(listQuerySaleOrderBean.get(0).getProduct_type().equals("ESIM")) { 
					esimFlg = true ;
					dtSapOrderHeader.setSalesDocType("ZS0E");
				}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
						listQuerySaleOrderBean.get(0).getDeliveryType().equals("STOCKSHOP")) {
					stockAisShopFlg = true ;
					dtSapOrderHeader.setSalesDocType("ZS07");
				}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
						listQuerySaleOrderBean.get(0).getDeliveryType().equals("H")) {
					homeFlg = true ;
					dtSapOrderHeader.setSalesDocType("ZS08");
				}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
						listQuerySaleOrderBean.get(0).getDeliveryType().equals("DROPSHOP")) {
				   // drop ais
				  //  drop partner
					CmLocationMst cmLocationMst = cmLocationMstDao.getLocationMstByKey((Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber())));
					List<LovMaster> aisshopFlg = lovMasterDao.listLovMasterByCriteria("SAP_S4_SHOP_CONF", "SAP_S4_SHOP_CONF", cmLocationMst.getLoc_type(), cmLocationMst.getLoc_subtype(), "Y") ;

					if(BeanUtil.isNotEmpty(aisshopFlg)) {
						dropAisShopFlg = true ;
						dtSapOrderHeader.setSalesDocType("ZS07");
					}else {
						dropPartnerFlg = true ;
						dtSapOrderHeader.setSalesDocType("ZS08");
					}
				}
				
				// if service only
				boolean serviceOnlyFlg = true ;
				for(int i = 0 ; i < listQuerySaleOrderBean.size() ; i ++) {
					if(!listQuerySaleOrderBean.get(i).getProduct_type().equals("SERVICE")) {
						serviceOnlyFlg = false ;
					}
				}
				
			
				// insert header
				
				dtSapOrderHeader.setStatus("W");
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("C");
				dtSapOrderHeader.setSDDocumentCategory("C");
				
				
				
//				if(listQuerySaleOrderBean.get(0).getProduct_type().equals("ESIM")) { 
//					dtSapOrderHeader.setSalesDocType("ZS0E");
//				}else if(preBook) {
//					if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
//							listQuerySaleOrderBean.get(0).getDeliveryType().equals("DROPSHOP")) {
//						dtSapOrderHeader.setSalesDocType("ZS09");
//					}else {
//						dtSapOrderHeader.setSalesDocType("ZS10");
//					}
//					
//				}else if(dtSapTransaction.getTransactionType() == 26 ) { // STF
//					dtSapOrderHeader.setSalesDocType("ZS07");
//				}else {
//					
//					// ship to home = SZ08 , drop shop ais = SZ07, drop shop partner SZ08
//					
//					// if drop shop partner
//					
//					if(!listQuerySaleOrderBean.get(0).getHFSHCustomerNumber().equals("ONETIME_SHIPTO")) {
//						List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber()), dtSapTransaction.getCompany()) ;
//					    // loc map plant  keep ais shop only  , partner no plant
//						if(BeanUtil.isEmpty(listlocMapPlant)) {
//							dropPartnerFlg = true ;
//						}
//					}
//					
//					dtSapOrderHeader.setSalesDocType(listQuerySaleOrderBean.get(0).getSalesDocType());
////					if(listQuerySaleOrderBean.get(0).getSalesDocType().equals("ZS07") &&  dropPartnerFlg) {
////						dtSapOrderHeader.setSalesDocType("ZS08");
////					}else {
////						dtSapOrderHeader.setSalesDocType(listQuerySaleOrderBean.get(0).getSalesDocType());
////					}
//					
//				}
				
				dtSapOrderHeader.setSalesOrganiztion(listQuerySaleOrderBean.get(0).getSalesOrganiztion());
				dtSapOrderHeader.setDistributionChannel(listQuerySaleOrderBean.get(0).getDistributionChannel());
				dtSapOrderHeader.setDivision(listQuerySaleOrderBean.get(0).getDivision());
				if(serviceOnlyFlg) {
					dtSapOrderHeader.setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference()+"_S");
				}else {
					dtSapOrderHeader.setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference());
				}
				
				
				if(dtSapTransaction.getTransactionType() == 26) { // STF
					dtSapOrderHeader.setCustomerPurchaseOrderType("Z011");
					dtSapOrderHeader.setDeliveryBlock("Z4");
				}else {
					dtSapOrderHeader.setCustomerPurchaseOrderType(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType());
					
					if("Z027".equals(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType())) {
						omniPartner_home = true ;
					}else if("Z028".equals(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType())) {
						omniPartner_shop = true ;
					}
				}
				
				dtSapOrderHeader.setShippingConditions(listQuerySaleOrderBean.get(0).getShippingConditions());
				
				//
				List<SaleOrderTransection> saletrans = getSaleOrderTransectionByReceiptNo(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany()) ;
				
				String HdNew = "";
				if(BeanUtil.isNotEmpty(saletrans) ) {
					if(BeanUtil.isNotEmpty(saletrans.get(0).getCreateDoFlg()) && saletrans.get(0).getCreateDoFlg().equals("N") ) {
						dtSapOrderHeader.setDeliveryBlock("Z7");
					}else {
						if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
								listQuerySaleOrderBean.get(0).getDeliveryType().equals("STOCKSHOP")) {
							dtSapOrderHeader.setDeliveryBlock("Z4");
						}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
								listQuerySaleOrderBean.get(0).getDeliveryType().equals("DROPSHOP")) {  
							dtSapOrderHeader.setDeliveryBlock("Z4");
						}else {  // home
							// if DYNAMIC SIM
							List<LovMaster> matDynamicSim = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DYNAMIC_SIM", "SAP_S4_MAT_DYNAMIC_SIM", dtSapTransaction.getCompany(), listQuerySaleOrderBean.get(0).getMaterialNumber(), "Y") ;
							if(BeanUtil.isNotEmpty(matDynamicSim)) {
								dtSapOrderHeader.setDeliveryBlock("Z6");
							}else {
								dtSapOrderHeader.setDeliveryBlock(null);
							}
							
						}
						
					}
					
					if(BeanUtil.isNotEmpty(saletrans.get(0).getOrderCode()) && saletrans.get(0).getOrderCode().equals("1") ) {
						HdNew = saletrans.get(0).getOrderNo() + "   " + saletrans.get(0).getSoCusMobileNoOrder();
					}
				}
				
				if(dropPartnerFlg) {
					dtSapOrderHeader.setDeliveryBlock(null);
				}
				
				if(esimFlg) {
					dtSapOrderHeader.setCustomerPurchaseOrderType("Z003");
					dtSapOrderHeader.setShippingConditions("15");
					dtSapOrderHeader.setDeliveryBlock(null);
				}
				
				
				dtSapOrderHeader.setPaymentTerms(listQuerySaleOrderBean.get(0).getPaymentTerms());
				dtSapOrderHeader.setDocumentCurrency(listQuerySaleOrderBean.get(0).getDocumentCurrency());
				dtSapOrderHeader.setCustomerGroup(listQuerySaleOrderBean.get(0).getCustomerGroup());
				
				
				//if COD  C1
				boolean codFlg = false ;
				String codPrice = "";
				List<QueryTenderBean> tenders = queryTenderPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
				if(BeanUtil.isNotEmpty(tenders)) {
					for (QueryTenderBean el : tenders ) {
						if(el.getTENDERTYPECODE().equals("C1")) {
							codFlg = true ;
							codPrice = el.getTENDERAMOUNT() ;
							break ;
						}
					}
				}
				
				if(codFlg) {
					dtSapOrderHeader.setCustomerGroup1("COD");
				}
				
				dtSapOrderHeader.setCreateValue(masterValue);
				
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
			
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				//94078-4000001586  location partner _ shiptocode
				if(omniPartner_home || omniPartner_shop) {
					//get ship to code
					CmLocationMst cmLocationMst = cmLocationMstDao.getLocationMstByKey((Long.valueOf(listQuerySaleOrderBean.get(0).getSaleLocationCode())));
					
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("ZH01");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(listQuerySaleOrderBean.get(0).getSaleLocationCode()+"_"+ (BeanUtil.isNotEmpty(cmLocationMst.getAwn_code()) ? cmLocationMst.getAwn_code() : "") );
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
				}
								
				if(omniPartner_shop) {
					//POXXXXXXXXXXX_OMNI_คุณสมชาย
					// must have sale trans
					String txt = saletrans.get(0).getOrderNo() + "_OMNI_"+ dtSapTransactionTypeDao.queryCusNameWithBarX(listQuerySaleOrderBean.get(0).getHFSName1());
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("TX03");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(txt);
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
				}
				
				if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getUrlCoverPage())) {
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("ZH02");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(listQuerySaleOrderBean.get(0).getUrlCoverPage());
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
				}
				
				if(BeanUtil.isNotEmpty(HdNew)) {
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("TX03");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(HdNew);
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
				}
				
				if(codFlg) {
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("ZH04");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(codPrice);
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
				}
				
				if(dropAisShopFlg) {
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("TX03");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(listQuerySaleOrderBean.get(0).getCustomerReference());
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
//					List<LovMaster> dropaisRemark = lovMasterDao.listLovMasterByCriteria("SAP_S4_REMARK_ZS07", "SAP_S4_REMARK_ZS07", null, null, "Y") ;	
//					if(BeanUtil.isNotEmpty(dropaisRemark)) {
//						DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
//						dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
//						dtSapOrderHText.setTextID("TX03");
//						dtSapOrderHText.setTextLanguage("EN");
//						dtSapOrderHText.setTextLine(dropaisRemark.get(0).getLovAttribute01());
//						dtSapOrderHText.setCreateValue(masterValue);
//						dtSapOrderHTextDao.insert(dtSapOrderHText);
//					}
					
				}
				
				//int itt = 100/0 ;
				
				
//				if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getHAmount())) {
//					BigDecimal hAmount = new BigDecimal(listQuerySaleOrderBean.get(0).getHAmount());
//					BigDecimal h0 = new BigDecimal("0");
//					if(hAmount.compareTo(h0) > 0) {
//						DtSapOrderHCon dtSapOrderHCon = new DtSapOrderHCon();
//						dtSapOrderHCon.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
//						dtSapOrderHCon.setAmount(listQuerySaleOrderBean.get(0).getHAmount());
//						dtSapOrderHCon.setChangeMode("C");
//						dtSapOrderHCon.setConditionType("ZD02");						
//						dtSapOrderHCon.setCurrency("THB");
//						
//						dtSapOrderHCon.setCreateValue(masterValue);
//						dtSapOrderHConDao.insert(dtSapOrderHCon);
//					}
//					
//				}
				
				DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();
				
				//select * from td_lov_mst lv where lv.lov_type in ( 'SAP_S4_SOLD_TO_ONETIME' , 'SAP_S4_SHIP_TO_ONETIME' )

				//List<LovMaster> shipToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SHIP_TO_ONETIME", "SAP_S4_SHIP_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;
				List<LovMaster> soldToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", "SAP_S4_SOLD_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;
				List<LovMaster> soldToOneTimeByCh = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", dtSapOrderHeader.getCustomerPurchaseOrderType(), dtSapTransaction.getCompany(), null, "Y") ;

				String soldToOneTimeSAP = soldToOneTime.get(0).getLovVal();
				if(BeanUtil.isNotEmpty(soldToOneTimeByCh)) {
					soldToOneTimeSAP = soldToOneTimeByCh.get(0).getLovVal();
				}
				
				if(esimFlg || listQuerySaleOrderBean.get(0).getHFSHCustomerNumber().equals("ONETIME_SHIPTO")){  //  moi , prc  , ncp home
					dtSapOrderHPartnerFAG.setChangeMode("C");
					dtSapOrderHPartnerFAG.setPartnerFunction("AG");
					dtSapOrderHPartnerFAG.setCustomerNumber(soldToOneTimeSAP);  // one time sold to
					dtSapOrderHPartnerFAG.setName1(listQuerySaleOrderBean.get(0).getHFSHName1());
					dtSapOrderHPartnerFAG.setName2(listQuerySaleOrderBean.get(0).getHFSHName2());
					dtSapOrderHPartnerFAG.setName3(listQuerySaleOrderBean.get(0).getHFSHName3());
					dtSapOrderHPartnerFAG.setName4(listQuerySaleOrderBean.get(0).getHFSHName4());
					dtSapOrderHPartnerFAG.setStreet(listQuerySaleOrderBean.get(0).getHFSHStreet());
					dtSapOrderHPartnerFAG.setStreet2(listQuerySaleOrderBean.get(0).getHFSHStreet2());
					dtSapOrderHPartnerFAG.setStreet3(listQuerySaleOrderBean.get(0).getHFSHStreet3());
					dtSapOrderHPartnerFAG.setStreet4(listQuerySaleOrderBean.get(0).getHFSHStreet4());
					dtSapOrderHPartnerFAG.setOtherCity(listQuerySaleOrderBean.get(0).getHFSHOtherCity());
					dtSapOrderHPartnerFAG.setDistrict(listQuerySaleOrderBean.get(0).getHFSHDistrict());
					dtSapOrderHPartnerFAG.setCity(listQuerySaleOrderBean.get(0).getHFSHCity());
					dtSapOrderHPartnerFAG.setPostalCode(listQuerySaleOrderBean.get(0).getHFSHPostalCode());
					dtSapOrderHPartnerFAG.setRegion(listQuerySaleOrderBean.get(0).getHFSHRegion());
					dtSapOrderHPartnerFAG.setCountry(listQuerySaleOrderBean.get(0).getHFSHCountry());
					dtSapOrderHPartnerFAG.setTelephone(listQuerySaleOrderBean.get(0).getHFSHTelephone());
					dtSapOrderHPartnerFAG.setLanguageKey("EN");
					
					if(BeanUtil.isNotEmpty(dtSapOrderHeader.getCustomerPurchaseOrderType()) ) {
						if(dtSapOrderHeader.getCustomerPurchaseOrderType().equals("Z008")) {
							dtSapOrderHPartnerFAG.setTransportationZone("3001");
						}else {
							dtSapOrderHPartnerFAG.setTransportationZone("3000");
						}
					}
					
					if(esimFlg) {
						dtSapOrderHPartnerFAG.setTransportationZone("3001");
					}
					
					dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFAG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
				}else {  // ncp stock shop , ncp drop shop
					
					dtSapOrderHPartnerFAG.setChangeMode("C");
					dtSapOrderHPartnerFAG.setPartnerFunction("AG");
					dtSapOrderHPartnerFAG.setCustomerNumber(soldToOneTimeSAP);  // one time sold to
					dtSapOrderHPartnerFAG.setName1(listQuerySaleOrderBean.get(0).getHFSName1());
					dtSapOrderHPartnerFAG.setName2(listQuerySaleOrderBean.get(0).getHFSName2());
					dtSapOrderHPartnerFAG.setName3(listQuerySaleOrderBean.get(0).getHFSName3());
					dtSapOrderHPartnerFAG.setName4(listQuerySaleOrderBean.get(0).getHFSName4());
					dtSapOrderHPartnerFAG.setStreet(listQuerySaleOrderBean.get(0).getHFSStreet());
					dtSapOrderHPartnerFAG.setStreet2(listQuerySaleOrderBean.get(0).getHFSStreet2());
					dtSapOrderHPartnerFAG.setStreet3(listQuerySaleOrderBean.get(0).getHFSStreet3());
					dtSapOrderHPartnerFAG.setStreet4(listQuerySaleOrderBean.get(0).getHFSStreet4());
					dtSapOrderHPartnerFAG.setOtherCity(listQuerySaleOrderBean.get(0).getHFSOtherCity());
					dtSapOrderHPartnerFAG.setDistrict(listQuerySaleOrderBean.get(0).getHFSDistrict());
					dtSapOrderHPartnerFAG.setCity(listQuerySaleOrderBean.get(0).getHFSCity());
					dtSapOrderHPartnerFAG.setPostalCode(listQuerySaleOrderBean.get(0).getHFSPostalCode());
					dtSapOrderHPartnerFAG.setRegion(listQuerySaleOrderBean.get(0).getHFSRegion());
					dtSapOrderHPartnerFAG.setCountry(listQuerySaleOrderBean.get(0).getHFSCountry());
					dtSapOrderHPartnerFAG.setTelephone(listQuerySaleOrderBean.get(0).getHFSTelephone());
					dtSapOrderHPartnerFAG.setLanguageKey("EN");
					dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFAG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFWE.setChangeMode("C");
					dtSapOrderHPartnerFWE.setPartnerFunction("WE");
					
					if(dropPartnerFlg) {  //partner need ship to
						CmLocationMst cmLocationMst = cmLocationMstDao.getLocationMstByKey((Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber())));
						dtSapOrderHPartnerFWE.setCustomerNumber(cmLocationMst.getAwn_code()); 
					}else {
						List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber()), dtSapTransaction.getCompany()) ;
						dtSapOrderHPartnerFWE.setCustomerNumber(listlocMapPlant.get(0).getPk().getPlantCode()); 
					}
					 
					dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFWE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
				}
				
				
				
				for(int i = 0 ; i < listQuerySaleOrderBean.size() ; i ++) {
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setChangeMode("C");
					dtSapOrderItem.setSalesOrderDocument(null);
					dtSapOrderItem.setItem(BeanUtil.lPad((i+1) * 10, 4));
					
					dtSapOrderItem.setMaterialNumber(listQuerySaleOrderBean.get(i).getMaterialNumber());
					dtSapOrderItem.setQuantity(listQuerySaleOrderBean.get(i).getQuantity());
					dtSapOrderItem.setSalesUnit(listQuerySaleOrderBean.get(i).getSalesUnit());
					dtSapOrderItem.setRequestedDeliveryDate(listQuerySaleOrderBean.get(i).getReceipt_dt());
					
					if(dropPartnerFlg) {
						List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(1012L, dtSapTransaction.getCompany()) ;
						List<LovMaster> slocWh = lovMasterDao.listLovMasterByCriteria("SAP_S4_SLOC_WH_FOR_SALE_ORDER", "SAP_S4_SLOC_WH_FOR_SALE_ORDER", dtSapTransaction.getCompany(), null, "Y") ;
						dtSapOrderItem.setPlant(listlocMapPlant.get(0).getPk().getPlantCode());
						dtSapOrderItem.setStorageLocation(slocWh.get(0).getLovVal());
						
					}else if(esimFlg) {
						List<LovMaster> slocEsim = lovMasterDao.listLovMasterByCriteria("SAP_S4_ESIM_SALE",  dtSapTransaction.getCompany(),null, null, "Y") ;
						dtSapOrderItem.setPlant(slocEsim.get(0).getLovCode());
						dtSapOrderItem.setStorageLocation(slocEsim.get(0).getLovVal());
					}
					else {
						List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(i).getPlant()), dtSapTransaction.getCompany()) ;
	                    
						dtSapOrderItem.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
						if(listlocMapPlant.get(0).getWhFlg().equals("Y")) {
							List<LovMaster> slocWh = lovMasterDao.listLovMasterByCriteria("SAP_S4_SLOC_WH_FOR_SALE_ORDER", "SAP_S4_SLOC_WH_FOR_SALE_ORDER", dtSapTransaction.getCompany(), null, "Y") ;
							dtSapOrderItem.setStorageLocation(slocWh.get(0).getLovVal());
						}else {
							dtSapOrderItem.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
						}
					}
					
					
					dtSapOrderItem.setBatch(null);
					dtSapOrderItem.setBillingBlock(null);
					dtSapOrderItem.setDeliveryPriority(listQuerySaleOrderBean.get(i).getDeliveryPriority());
					dtSapOrderItem.setRoute(null);
					
					if(listQuerySaleOrderBean.get(i).getProduct_type().equals("SERVICE")) {
						dtSapOrderItem.setDeliveryPriority(null);
						dtSapOrderItem.setStorageLocation(null);
					}
					
					
					if(listQuerySaleOrderBean.get(i).getProduct_type().equals("SERVICE")) {
						dtSapOrderItem.setItemCategory("ZRS3");
					}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(i).getFoc_flg()) && listQuerySaleOrderBean.get(i).getFoc_flg().equals("Y")) {
						dtSapOrderItem.setItemCategory("ZRS2");
					}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(i).getFree_goods_flg()) && listQuerySaleOrderBean.get(i).getFree_goods_flg().equals("Y")) {
						if(dropAisShopFlg || preBookDropAis) {
							dtSapOrderItem.setItemCategory("ZRSA");
						}else {
							dtSapOrderItem.setItemCategory("ZRS2");
						}
					}else {
						
						if(stockAisShopFlg) {
							dtSapOrderItem.setItemCategory("ZRS1");
						}else if(dropPartnerFlg) {
							dtSapOrderItem.setItemCategory("ZRS1");
						}else if(homeFlg) {
							dtSapOrderItem.setItemCategory("ZRS1");
						}else if(preBookHome) {
							dtSapOrderItem.setItemCategory("ZRS1");  
						}else if(dropAisShopFlg) {
							dtSapOrderItem.setItemCategory("ZRS6");  //STO from wh
						}else if(preBookDropAis) {
							dtSapOrderItem.setItemCategory("ZRS7");   //STO from wh
						}else if(esimFlg) {
							dtSapOrderItem.setItemCategory("ZRS1");  
						}
						else {
							dtSapOrderItem.setItemCategory("ZRS1");  
						}
						
//						if(dropAisShopFlg) {
//							if(prebook) {
//								dtSapOrderItem.setItemCategory("ZRS7");
//							}else {
//								if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getDeliveryType()) &&
//										listQuerySaleOrderBean.get(0).getDeliveryType().equals("STOCKSHOP")) {
//									dtSapOrderItem.setItemCategory("ZRS1");
//								}else {
//									dtSapOrderItem.setItemCategory("ZRS6");
//								}
//							}
//							
//						}else {
//							dtSapOrderItem.setItemCategory("ZRS1");
//						}
						
					}
					
					
//					if(listQuerySaleOrderBean.get(i).getDeliveryType().equals("DROPSHOP")) {
//						if(listQuerySaleOrderBean.get(i).getProduct_type().equals("SERVICE")) {
//							dtSapOrderItem.setItemCategory("ZRS3");
//						}else {
//							if(preBook) {
//								dtSapOrderItem.setItemCategory("ZRS7");
//							}else {
//								dtSapOrderItem.setItemCategory("ZRS6");
//							}
//							
//						}
//						
//					}else if(listQuerySaleOrderBean.get(i).getDeliveryType().equals("STOCKSHOP")) {
//						if(listQuerySaleOrderBean.get(i).getProduct_type().equals("SERVICE")) {
//							dtSapOrderItem.setItemCategory("ZRS3");
//						}else {
//							dtSapOrderItem.setItemCategory("ZRS1");
//						}
//					}else if(listQuerySaleOrderBean.get(i).getDeliveryType().equals("H")) {
//						if(listQuerySaleOrderBean.get(i).getProduct_type().equals("SERVICE")) {
//							dtSapOrderItem.setItemCategory("ZRS3");
//						}else {
//							if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(i).getFoc_flg()) && listQuerySaleOrderBean.get(i).getFoc_flg().equals("Y")) {
//								dtSapOrderItem.setItemCategory("ZRS2");
//							}else if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(i).getFree_goods_flg()) && listQuerySaleOrderBean.get(i).getFree_goods_flg().equals("Y")) {
//								dtSapOrderItem.setItemCategory("ZRS2");
//							}else {
//								dtSapOrderItem.setItemCategory("ZRS1");
//							}
//							
//						}
//					}
					
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);
					
					if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(i).getNumberOfSerialNumbers())) {
						DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
						dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
						dtSapOrderItemSerial.setChangeMode("C");
						dtSapOrderItemSerial.setNumberOfSerialNumber(listQuerySaleOrderBean.get(i).getNumberOfSerialNumbers());
						dtSapOrderItemSerial.setIUIDCustomerRelevant(null);
						dtSapOrderItemSerial.setCreateValue(masterValue);
						dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);

					}
					
					if(!(dtSapOrderItem.getItemCategory().equals("ZRS2") || dtSapOrderItem.getItemCategory().equals("ZRSA")) ) {
						DtSapOrderItemCon dtSapOrderItemConMainPrice = new DtSapOrderItemCon() ;
						dtSapOrderItemConMainPrice.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
						dtSapOrderItemConMainPrice.setChangeMode("C");
						dtSapOrderItemConMainPrice.setConditionType("PN10");
						
						if(dtSapTransaction.getTransactionType() == 26) {
							dtSapOrderItemConMainPrice.setAmount(listQuerySaleOrderBean.get(i).getPrice_amt());
						}else {
							dtSapOrderItemConMainPrice.setAmount(listQuerySaleOrderBean.get(i).getICAmount());
						}
						
						dtSapOrderItemConMainPrice.setCurrency("THB");
						dtSapOrderItemConMainPrice.setConditionPricingUnit(null);
						dtSapOrderItemConMainPrice.setConditionUnit(null);
						dtSapOrderItemConMainPrice.setCreateValue(masterValue);
						dtSapOrderItemConDao.insert(dtSapOrderItemConMainPrice);
						
						if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(i).getICDAmount())) {
							DtSapOrderItemCon dtSapOrderItemConDise = new DtSapOrderItemCon() ;
							dtSapOrderItemConDise.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
							dtSapOrderItemConDise.setChangeMode("C");
							dtSapOrderItemConDise.setConditionType("ZD01");
							dtSapOrderItemConDise.setAmount(listQuerySaleOrderBean.get(i).getICDAmount());
							dtSapOrderItemConDise.setCurrency("THB");
							dtSapOrderItemConDise.setConditionPricingUnit(null);
							dtSapOrderItemConDise.setConditionUnit(null);
							dtSapOrderItemConDise.setCreateValue(masterValue);
							dtSapOrderItemConDao.insert(dtSapOrderItemConDise);
						}
					}
					
					
					
		
				}
					
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	public List<QuerySaleOrderBean> querySaleOrderSql1819(String docNo , String company,Long transactionType){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
	    
		List<QuerySaleOrderBean> res = null ;
		if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
			res=  dtSapTransactionTypeDao.querySaleOrderSql1(docNo, company);
		}else {
			// query from on prem
			res=  querySaleOrderSql1OnPrem(docNo, company);
		}
		
		return res ;
		
	}
	
	private List<SaleOrderTransection> getSaleOrderTransectionByReceiptNo(String docNo , String company ) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return getSaleOrderTransectionByReceiptNoOnCloud(docNo, company);
		 }  
		  else { 
			// query from on prem
				return getSaleOrderTransectionByReceiptNoOnprem(docNo, company);
		 }	
   }
	
	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader res = sapCallApiService.callSaleorderApi(dtSapOrderHeader);;
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
	
	private List<QuerySaleOrderBean> querySaleOrderSql1OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/querySaleOrderSql1",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQuerySaleOrderBean() ;
   }
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder28(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query doc stf 
			String  stfDoc = queryRefNoReceipt(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			
			// query origin order type 26
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(stfDoc, dtSapTransaction.getCompany() , 26L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				//query header
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				//insert header
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
				dtSapOrderHeaderNew.setDeliveryBlock("/");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
				
				//query order  item
				List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
				
				//query info receipt sql1
				List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
				
				Map<String,List<String>> mapMatCpdeSerial = new HashMap<>();
				//Map<String,Long> mapMatQty = new HashMap<>();
				//Map<String,String> mapMatUnit = new HashMap<>();
				//Map<String,BigDecimal> mapMatPrice = new HashMap<>();
				//Map<String,String> mapMatType = new HashMap<>();
				for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
					if(mapMatCpdeSerial.containsKey(el.getITEMID())) {
						if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
							mapMatCpdeSerial.get(el.getITEMID()).add(el.getSERIALNUMBER()) ;
						}else {
							mapMatCpdeSerial.get(el.getITEMID()).add("NON_SERIAL") ;
						}
						
					}else {
						List<String> listSerial = new ArrayList<>();
						if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
							listSerial.add(el.getSERIALNUMBER()) ;
						}else {
							listSerial.add("NON_SERIAL") ;
						}
						
						mapMatCpdeSerial.put(el.getITEMID(), listSerial) ;
					}
					
				}
				
				//old item
				Map<String,String> mapMatCpdeOld = new HashMap<>();
				int noItem = 0 ;
				for(DtSapOrderItem el : listDtSapOrderItem ) {
					
					if(!mapMatCpdeOld.containsKey(el.getMaterialNumber())) {
						mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
					}
					
					
					noItem = noItem + 1 ;
					DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
					dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
					dtSapOrderItemNew.setCreateValue(masterValue);
					
					dtSapOrderItemNew.setChangeMode("U");
					dtSapOrderItemNew.setItem(el.getItem());
					dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
					
					dtSapOrderItemNew.setQuantity("");
					dtSapOrderItemNew.setSalesUnit("");
					dtSapOrderItemNew.setItemCategory("");
					
					dtSapOrderItemDao.insert(dtSapOrderItemNew);
					
					//serial
					if(mapMatCpdeSerial.get(el.getMaterialNumber()) != null) {
						for(String serial : mapMatCpdeSerial.get(el.getMaterialNumber())) {
							if(!serial.equals("NON_SERIAL")) {
								DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
								dtSapOrderItemSerial.setChangeMode("U");
								dtSapOrderItemSerial.setNumberOfSerialNumber(serial);
								
								dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId()) ;
								dtSapOrderItemSerial.setCreateValue(masterValue);;
								dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
							}
						}
					}
					
					
				}
				
				//new item ex free goods ,etc
				//List<QueryPostTransactionBean> listQueryPostTransactionBean
				//for(String k : mapMatCpdeSerial.keySet()) {
				for(QueryPostTransactionBean k : listQueryPostTransactionBean) {	
					if(!mapMatCpdeOld.containsKey(k.getITEMID())) {
						noItem = noItem + 1 ;
						DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
						dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
						dtSapOrderItemNew.setCreateValue(masterValue);
						
						dtSapOrderItemNew.setChangeMode("C");
						dtSapOrderItemNew.setItem(BeanUtil.lPad((noItem) * 10, 4));
						dtSapOrderItemNew.setMaterialNumber(k.getITEMID());
						dtSapOrderItemNew.setQuantity(k.getRETAILQUANTITY());
						dtSapOrderItemNew.setSalesUnit(k.getSALESUNITOFMEASURE());
						dtSapOrderItemNew.setPlant(listDtSapOrderItem.get(0).getPlant()) ;;
						
						if(k.getPRODUCT_TYPE().equals("SERVICE")) {
							dtSapOrderItemNew.setItemCategory("ZRS3");
						}else {
							BigDecimal saleamount = new BigDecimal(k.getSALESAMOUNT());
							if(saleamount.compareTo(new BigDecimal("0")) > 0  ) {
								dtSapOrderItemNew.setItemCategory("ZRS1");
							}else {
								dtSapOrderItemNew.setItemCategory("ZRS2");
							}
						}
						
						dtSapOrderItemDao.insert(dtSapOrderItemNew);
						
						if(!dtSapOrderItemNew.getItemCategory().equals("ZRS2")) {  // not free goods
							//itemcon
							DtSapOrderItemCon dtSapOrderItemConPriceAmount = new DtSapOrderItemCon();
							dtSapOrderItemConPriceAmount.setChangeMode("C");
							dtSapOrderItemConPriceAmount.setConditionType("PN10");
							dtSapOrderItemConPriceAmount.setCurrency("THB");
							dtSapOrderItemConPriceAmount.setAmount(k.getSALESAMOUNT());
							
							dtSapOrderItemConPriceAmount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
							dtSapOrderItemConPriceAmount.setCreateValue(masterValue);
							dtSapOrderItemConDao.insert(dtSapOrderItemConPriceAmount);
							
							if(BeanUtil.isNotEmpty(k.getREDUCTIONAMOUNT()) ) {
								DtSapOrderItemCon dtSapOrderItemConPriceDiscount = new DtSapOrderItemCon();
								dtSapOrderItemConPriceDiscount.setChangeMode("C");
								dtSapOrderItemConPriceDiscount.setConditionType("ZD01");
								dtSapOrderItemConPriceDiscount.setCurrency("THB");
								dtSapOrderItemConPriceDiscount.setAmount(k.getREDUCTIONAMOUNT());
								
								dtSapOrderItemConPriceDiscount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
								dtSapOrderItemConPriceDiscount.setCreateValue(masterValue);
								dtSapOrderItemConDao.insert(dtSapOrderItemConPriceDiscount);
							}
							
						}
						
						//serial
						if(BeanUtil.isNotEmpty(k.getSERIALNUMBER())) {
							DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial() ;
							dtSapOrderItemSerial.setChangeMode("C");
							dtSapOrderItemSerial.setCreateValue(masterValue);
							dtSapOrderItemSerial.setNumberOfSerialNumber(k.getSERIALNUMBER());
							dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
							
							dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
						}
						
						
					}
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}
	
	private String queryRefNoReceipt(String receiptNo , String company) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// postgres
				return dtSapOrderHeaderDao.queryRefNoReceipt(receiptNo, company);
		 }  
		  else { 
			// query from on prem
				return queryRefNoReceiptOnPrem(receiptNo, company) ;
		 }
		
		
		
	}
	
	private String queryRefNoReceiptUsed(String refNo , String company) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// postgres
				return dtSapOrderHeaderDao.queryRefNoReceiptUsed(refNo, company);
		 }  
		  else { 
			// query from on prem
				return queryRefNoReceiptUsedOnPrem(refNo, company) ;
		 }	
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder14(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query doc deposit No 
			String  refNo = queryRefNoReceipt(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			
			// check if depositNo used more than 1 time
			String  receiptCancel = queryRefNoReceiptUsed(refNo, dtSapTransaction.getCompany()) ;
			
			if(BeanUtil.isEmpty(receiptCancel)) {
				// query origin order type 12
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(refNo, dtSapTransaction.getCompany() , 12L) ;
				
				if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
					//query header
					List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
					
					//insert header
					//DtSapOrderHeader dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
					dtSapOrderHeaderNew.setChangeMode("U");
					dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
					dtSapOrderHeaderNew.setDeliveryBlock("/");
					dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
					dtSapOrderHeaderNew.setCreateValue(masterValue);
					dtSapOrderHeaderNew.setStatus("W");
					dtSapOrderHeaderNew.setSalesDocType("");
					dtSapOrderHeaderNew.setSalesOrganiztion("");
					dtSapOrderHeaderNew.setDistributionChannel("");
					dtSapOrderHeaderNew.setDivision("");
					dtSapOrderHeaderNew.setShippingConditions("");
					
					dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
					
					
					
					
					
					//query order  item
					List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
					
					//query info receipt sql1
					List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
					
					List<QueryPostTransactionBean> listQueryPostTransactionBeanForDeposit = null ;
					if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getREF_NO()) && listQueryPostTransactionBean.get(0).getREF_NO().startsWith("D") ) {
						listQueryPostTransactionBeanForDeposit = queryPostTransactionSql1(listQueryPostTransactionBean.get(0).getREF_NO(), dtSapTransaction.getCompany());
					}
					
					
//					if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
//						BigDecimal hAmount = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//						BigDecimal h0 = new BigDecimal("0");
//						if(hAmount.compareTo(h0) > 0) {
//							DtSapOrderHCon dtSapOrderHCon = new DtSapOrderHCon();
//							dtSapOrderHCon.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
//							dtSapOrderHCon.setAmount(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//							dtSapOrderHCon.setChangeMode("U");
//							dtSapOrderHCon.setConditionType("ZD02");						
//							dtSapOrderHCon.setCurrency("THB");
//							
//							dtSapOrderHCon.setCreateValue(masterValue);
//							//dtSapOrderHConDao.insert(dtSapOrderHCon);
//						}
//						
//					}
					
					Map<String,List<String>> mapMatCpdeSerial = new HashMap<>();
					Map<String,String> mapMatDisc = new HashMap<>();
					Map<String,String> mapMatSaleAm = new HashMap<>();
					//Map<String,Long> mapMatQty = new HashMap<>();
					//Map<String,String> mapMatUnit = new HashMap<>();
					//Map<String,BigDecimal> mapMatPrice = new HashMap<>();
					//Map<String,String> mapMatType = new HashMap<>();
					for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
						if(mapMatCpdeSerial.containsKey(el.getITEMID())) {
							if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
								mapMatCpdeSerial.get(el.getITEMID()).add(el.getSERIALNUMBER()) ;
							}else {
								mapMatCpdeSerial.get(el.getITEMID()).add("NON_SERIAL") ;
							}
							
						}else {
							List<String> listSerial = new ArrayList<>();
							if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
								listSerial.add(el.getSERIALNUMBER()) ;
							}else {
								listSerial.add("NON_SERIAL") ;
							}
							
							mapMatCpdeSerial.put(el.getITEMID(), listSerial) ;
						}
						
						if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
							BigDecimal EXC_AMTMain = new BigDecimal(el.getEXC_AMT());
							BigDecimal vatMain = new BigDecimal(el.getTAXAMOUNT());
							BigDecimal EXC_AMTDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getEXC_AMT());	
							BigDecimal SALESAMOUNT = EXC_AMTMain.subtract(EXC_AMTDeposit).add(vatMain) ;
							
							BigDecimal PriceMain = new BigDecimal(el.getNORMALSALESAMOUNT());
							BigDecimal PriceDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
							BigDecimal NORMALSALESAMOUNT = PriceMain.subtract(PriceDeposit);
							
							BigDecimal REDUCTIONAMOUNT = NORMALSALESAMOUNT.subtract(SALESAMOUNT);
							
							if(REDUCTIONAMOUNT.compareTo(new BigDecimal("0")) > 0) {
								mapMatDisc.put(el.getITEMID(), REDUCTIONAMOUNT.toString()) ;
								mapMatSaleAm.put(el.getITEMID(), SALESAMOUNT.add(PriceDeposit).toString());
							}
							
							

						}
						
					}
					
					//get item receipt
					Map<String,String> mapMatCpdeReceiptFreeGoods = new HashMap<>();
					for(QueryPostTransactionBean k : listQueryPostTransactionBean) {
						BigDecimal saleamount = new BigDecimal(k.getSALESAMOUNT());
						if( !k.getPRODUCT_TYPE().equals("SERVICE") && saleamount.compareTo(new BigDecimal("0")) <= 0  ) {  // free goods 
							if(mapMatCpdeReceiptFreeGoods.containsKey(k.getITEMID())) {
								int qty = Integer.valueOf( mapMatCpdeReceiptFreeGoods.get(k.getITEMID()));
								int qtyP = Integer.valueOf(k.getRETAILQUANTITY()) ;
								mapMatCpdeReceiptFreeGoods.put(k.getITEMID(), String.valueOf(qty+qtyP));
							}else {
								mapMatCpdeReceiptFreeGoods.put(k.getITEMID(), k.getRETAILQUANTITY());
							}
						}
					}
					
					//old item
					Map<String,String> mapMatCpdeOld = new HashMap<>();
					int noItem = 0 ;
					for(DtSapOrderItem el : listDtSapOrderItem ) {
                        boolean rejectFlg = false ;  
						
//						if(!mapMatCpdeOld.containsKey(el.getMaterialNumber())) {
//							mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
//						}
						
						
						noItem = noItem + 1 ;
						DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
						dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
						dtSapOrderItemNew.setCreateValue(masterValue);
						
						//dtSapOrderItemNew.setChangeMode("U");
						
						dtSapOrderItemNew.setItem("");
						dtSapOrderItemNew.setMaterialNumber("");
						dtSapOrderItemNew.setQuantity("");
						dtSapOrderItemNew.setSalesUnit("");
						dtSapOrderItemNew.setItemCategory("");
						
						List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;

						if(matDeposit.get(0).getLovVal().equals(el.getMaterialNumber())) {  // item 0020
							dtSapOrderItemNew.setChangeMode("C");
							 
						    //dtSapOrderItemNew.setItem("0030");
							dtSapOrderItemNew.setItem(BeanUtil.lPad((listDtSapOrderItem.size()+1) * 10, 4));
							dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
							dtSapOrderItemNew.setBillingBlock("/");
							dtSapOrderItemNew.setQuantity(el.getQuantity());
							dtSapOrderItemNew.setSalesUnit(el.getSalesUnit());
							dtSapOrderItemNew.setItemCategory("ZRS3");
							dtSapOrderItemNew.setPlant(el.getPlant());
							noItem = noItem + 1 ;
							mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
						}
						else if(el.getItem().equals("0010")) {  // item 0010
							dtSapOrderItemNew.setChangeMode("U");
							dtSapOrderItemNew.setItem(el.getItem());
							dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
							dtSapOrderItemNew.setBillingBlock("/");
							mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
						}
						else if(mapMatCpdeReceiptFreeGoods.containsKey(el.getMaterialNumber()) && 
								mapMatCpdeReceiptFreeGoods.get(el.getMaterialNumber()).equals(el.getQuantity()) ) {  // update
							dtSapOrderItemNew.setChangeMode("U");
							dtSapOrderItemNew.setItem(el.getItem());
							dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
							dtSapOrderItemNew.setBillingBlock("/");
							mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
						}
						else if(mapMatCpdeReceiptFreeGoods.containsKey(el.getMaterialNumber()) && 
								!mapMatCpdeReceiptFreeGoods.get(el.getMaterialNumber()).equals(el.getQuantity()) ) {  // update
//							dtSapOrderItemNew.setChangeMode("U");
//							dtSapOrderItemNew.setItem(el.getItem());
//							dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
//							dtSapOrderItemNew.setQuantity("");
//							dtSapOrderItemNew.setSalesUnit("");
//							dtSapOrderItemNew.setRequestedDeliveryDate("");
//							dtSapOrderItemNew.setPlant("");
//							dtSapOrderItemNew.setStorageLocation("");
//							dtSapOrderItemNew.setBillingBlock("");
//							dtSapOrderItemNew.setDeliveryPriority("");
//							dtSapOrderItemNew.setItemCategory("");
//							rejectFlg = true ;
							dtSapOrderItemNew.setChangeMode("U");
							dtSapOrderItemNew.setItem(el.getItem());
							dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
							dtSapOrderItemNew.setBillingBlock("/");
							dtSapOrderItemNew.setQuantity(mapMatCpdeReceiptFreeGoods.get(el.getMaterialNumber()));
							mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
							
						}else if(!mapMatCpdeReceiptFreeGoods.containsKey(el.getMaterialNumber())){ // reject
							dtSapOrderItemNew.setChangeMode("U");
							dtSapOrderItemNew.setItem(el.getItem());
							dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
							dtSapOrderItemNew.setQuantity("");
							dtSapOrderItemNew.setSalesUnit("");
							dtSapOrderItemNew.setRequestedDeliveryDate("");
							dtSapOrderItemNew.setPlant("");
							dtSapOrderItemNew.setStorageLocation("");
							dtSapOrderItemNew.setBillingBlock("");
							dtSapOrderItemNew.setDeliveryPriority("");
							dtSapOrderItemNew.setItemCategory("");
							rejectFlg = true ;
						}
						
						
						
						dtSapOrderItemDao.insert(dtSapOrderItemNew);
						
						if(matDeposit.get(0).getLovVal().equals(el.getMaterialNumber())) {
							DtSapOrderItemCon dtSapOrderItemConPriceAmount = new DtSapOrderItemCon();
							dtSapOrderItemConPriceAmount.setChangeMode("C");
							dtSapOrderItemConPriceAmount.setConditionType("PN10");
							dtSapOrderItemConPriceAmount.setCurrency("THB");
							
							List<DtSapOrderItemCon> depositPrice = dtSapOrderItemConDao.queryDtSapOrderItemCon(el.getSapOrderItemId()) ;
							
							dtSapOrderItemConPriceAmount.setAmount("-" +depositPrice.get(0).getAmount());
							
							dtSapOrderItemConPriceAmount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
							dtSapOrderItemConPriceAmount.setCreateValue(masterValue);
							dtSapOrderItemConDao.insert(dtSapOrderItemConPriceAmount);
						}
						
						if(mapMatDisc != null && mapMatDisc.containsKey(el.getMaterialNumber())) {
							
							//sale amount
							DtSapOrderItemCon dtSapOrderItemConPriceSaleAm = new DtSapOrderItemCon();
							dtSapOrderItemConPriceSaleAm.setChangeMode("U");
							dtSapOrderItemConPriceSaleAm.setConditionType("PN10");
							dtSapOrderItemConPriceSaleAm.setCurrency("THB");
							
				
							dtSapOrderItemConPriceSaleAm.setAmount(mapMatSaleAm.get(el.getMaterialNumber()));
							//dtSapOrderItemConPriceSaleAm.setAmount("-" +mapMatDisc.get(el.getMaterialNumber()));
							
							dtSapOrderItemConPriceSaleAm.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
							dtSapOrderItemConPriceSaleAm.setCreateValue(masterValue);
							dtSapOrderItemConDao.insert(dtSapOrderItemConPriceSaleAm);
							
							
							//discount
							DtSapOrderItemCon dtSapOrderItemConPriceDiscount = new DtSapOrderItemCon();
							dtSapOrderItemConPriceDiscount.setChangeMode("U");
							dtSapOrderItemConPriceDiscount.setConditionType("ZD01");
							dtSapOrderItemConPriceDiscount.setCurrency("THB");
							dtSapOrderItemConPriceDiscount.setAmount(mapMatDisc.get(el.getMaterialNumber()));
							
							dtSapOrderItemConPriceDiscount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
							dtSapOrderItemConPriceDiscount.setCreateValue(masterValue);
							dtSapOrderItemConDao.insert(dtSapOrderItemConPriceDiscount);
						}
						
						
						//serial
						if( !rejectFlg &&  mapMatCpdeSerial.get(el.getMaterialNumber()) != null) {
							for(String serial : mapMatCpdeSerial.get(el.getMaterialNumber())) {
								if(!serial.equals("NON_SERIAL")) {
									DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
									dtSapOrderItemSerial.setChangeMode("U");
									dtSapOrderItemSerial.setNumberOfSerialNumber(serial);
									
									dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId()) ;
									dtSapOrderItemSerial.setCreateValue(masterValue);
									dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
								}
							}
						}
						
						//reject
						if(rejectFlg) {
						////ITEM cancel
							DtSapOrderItemCancel dtSapOrderItemCancel = new DtSapOrderItemCancel();
							dtSapOrderItemCancel.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
							dtSapOrderItemCancel.setChangeMode("U");
							dtSapOrderItemCancel.setReasonforRejection("70");
							
							dtSapOrderItemCancel.setCreateValue(masterValue);
							dtSapOrderItemCancelDao.insert(dtSapOrderItemCancel);
						}
						
						
					}
					
					//new item ex free goods ,etc
					//List<QueryPostTransactionBean> listQueryPostTransactionBean
					//for(String k : mapMatCpdeSerial.keySet()) {
					for(QueryPostTransactionBean k : listQueryPostTransactionBean) {	
						if(!mapMatCpdeOld.containsKey(k.getITEMID())) {
							noItem = noItem + 1 ;
							DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
							dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
							dtSapOrderItemNew.setCreateValue(masterValue);
							
							dtSapOrderItemNew.setChangeMode("C");
							dtSapOrderItemNew.setBillingBlock("/");
							dtSapOrderItemNew.setItem(BeanUtil.lPad((noItem) * 10, 4));
							dtSapOrderItemNew.setMaterialNumber(k.getITEMID());
							dtSapOrderItemNew.setQuantity(k.getRETAILQUANTITY());
							dtSapOrderItemNew.setSalesUnit(k.getSALESUNITOFMEASURE());
							dtSapOrderItemNew.setPlant(listDtSapOrderItem.get(0).getPlant()) ;
							dtSapOrderItemNew.setStorageLocation(listDtSapOrderItem.get(0).getStorageLocation());
							
							if(k.getPRODUCT_TYPE().equals("SERVICE")) {
								dtSapOrderItemNew.setItemCategory("ZRS3");
							}else {
								BigDecimal saleamount = new BigDecimal(k.getSALESAMOUNT());
								if(saleamount.compareTo(new BigDecimal("0")) > 0  ) {
									dtSapOrderItemNew.setItemCategory("ZRS1");
								}else {
									dtSapOrderItemNew.setItemCategory("ZRSA");
								}
							}
							
							dtSapOrderItemDao.insert(dtSapOrderItemNew);
							
							if(!dtSapOrderItemNew.getItemCategory().equals("ZRSA")) {  // not free goods
								//itemcon
								DtSapOrderItemCon dtSapOrderItemConPriceAmount = new DtSapOrderItemCon();
								dtSapOrderItemConPriceAmount.setChangeMode("C");
								dtSapOrderItemConPriceAmount.setConditionType("PN10");
								dtSapOrderItemConPriceAmount.setCurrency("THB");
								dtSapOrderItemConPriceAmount.setAmount(k.getSALESAMOUNT());
								
								dtSapOrderItemConPriceAmount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
								dtSapOrderItemConPriceAmount.setCreateValue(masterValue);
								dtSapOrderItemConDao.insert(dtSapOrderItemConPriceAmount);
								
								if(BeanUtil.isNotEmpty(k.getREDUCTIONAMOUNT()) ) {
									DtSapOrderItemCon dtSapOrderItemConPriceDiscount = new DtSapOrderItemCon();
									dtSapOrderItemConPriceDiscount.setChangeMode("C");
									dtSapOrderItemConPriceDiscount.setConditionType("ZD01");
									dtSapOrderItemConPriceDiscount.setCurrency("THB");
									dtSapOrderItemConPriceDiscount.setAmount(k.getREDUCTIONAMOUNT());
									
									dtSapOrderItemConPriceDiscount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
									dtSapOrderItemConPriceDiscount.setCreateValue(masterValue);
									dtSapOrderItemConDao.insert(dtSapOrderItemConPriceDiscount);
								}
								
							}
							
							//serial
							if(BeanUtil.isNotEmpty(k.getSERIALNUMBER())) {
								DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial() ;
								dtSapOrderItemSerial.setChangeMode("C");
								dtSapOrderItemSerial.setCreateValue(masterValue);
								dtSapOrderItemSerial.setNumberOfSerialNumber(k.getSERIALNUMBER());
								dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
								
								dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
							}
							
							
						}
					}
				}
			}
			

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}

	private String queryRefNoReceiptOnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryRefNoReceipt",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getRefDocNo() ;
   }
	
	private String queryRefNoReceiptUsedOnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryRefNoReceiptUsed",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getRefDocNo() ;
   }
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder24(DtSapTransaction dtSapTransaction) {

		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
			/** query origin order type 18 **/
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());
				//dtSapOrderHeaderNew.setDeliveryBlock("/");
				dtSapOrderHeaderNew.setDeliveryBlock("");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
				
				//query order  item
				List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
				
				/** query info receipt get serial **/
				List<QueryPostTransactionBean> listInvoiceDetail = queryTransactionGetSerialNo(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
				
				Map<String,List<String>> mapMatCpdeSerial = new HashMap<>();
				for(QueryPostTransactionBean el : listInvoiceDetail) {
					
					if(mapMatCpdeSerial.containsKey(el.getITEMID())) {
						if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
							mapMatCpdeSerial.get(el.getITEMID()).add(el.getSERIALNUMBER()) ;
						}else {
							mapMatCpdeSerial.get(el.getITEMID()).add("NON_SERIAL") ;
						}
						
					}else {
						List<String> listSerial = new ArrayList<>();
						if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
							listSerial.add(el.getSERIALNUMBER()) ;
						}else {
							listSerial.add("NON_SERIAL") ;
						}
						
						mapMatCpdeSerial.put(el.getITEMID(), listSerial) ;
					}
					
				}
				
				Map<String,String> mapMatCpdeOld = new HashMap<>();
				int noItem = 0 ;
				for(DtSapOrderItem el : listDtSapOrderItem ) {
					
					if(!mapMatCpdeOld.containsKey(el.getMaterialNumber())) {
						mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
					}
					
					
					noItem = noItem + 1 ;
					DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
					dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
					dtSapOrderItemNew.setCreateValue(masterValue);
					dtSapOrderItemNew.setChangeMode("U");
					dtSapOrderItemNew.setItem(el.getItem());
					dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
					
					dtSapOrderItemNew.setQuantity("");
					dtSapOrderItemNew.setSalesUnit("");
					dtSapOrderItemNew.setItemCategory("");
					
					dtSapOrderItemDao.insert(dtSapOrderItemNew);
					
					//serial
					if(mapMatCpdeSerial.get(el.getMaterialNumber()) != null) {
						for(String serial : mapMatCpdeSerial.get(el.getMaterialNumber())) {
							if(!serial.equals("NON_SERIAL")) {
								DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
								dtSapOrderItemSerial.setChangeMode("U");
								dtSapOrderItemSerial.setNumberOfSerialNumber(serial);
								
								dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId()) ;
								dtSapOrderItemSerial.setCreateValue(masterValue);;
								dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
							}
						}
					}
					
					
				}
				

				for(QueryPostTransactionBean k : listInvoiceDetail) {	
					if(!mapMatCpdeOld.containsKey(k.getITEMID())) {
						noItem = noItem + 1 ;
						DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
						dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
						dtSapOrderItemNew.setCreateValue(masterValue);
						
						dtSapOrderItemNew.setChangeMode("U");
						dtSapOrderItemNew.setItem(BeanUtil.lPad((noItem) * 10, 4));
						dtSapOrderItemNew.setMaterialNumber(k.getITEMID());
						dtSapOrderItemNew.setQuantity(k.getRETAILQUANTITY());
						dtSapOrderItemNew.setSalesUnit(k.getSALESUNITOFMEASURE());
						
						dtSapOrderItemNew.setItemCategory("");
						
						dtSapOrderItemDao.insert(dtSapOrderItemNew);
						
						
						//serial
						if(BeanUtil.isNotEmpty(k.getSERIALNUMBER())) {
							DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial() ;
							dtSapOrderItemSerial.setChangeMode("U");
							dtSapOrderItemSerial.setCreateValue(masterValue);
							dtSapOrderItemSerial.setNumberOfSerialNumber(k.getSERIALNUMBER());
							dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
							dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
						}
					}
				}
			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return dtSapOrderHeaderNew ;
	}

	private List<QueryPostTransactionBean> queryTransactionGetSerialNo(String docNo, String company) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
				// query from postgres ,
			 return dtSapTransactionTypeDao.queryTransactionGetSerialNo(docNo, company);
		 }  
		  else { 
				// query from on prem
				return queryTransactionGetSerialNoOnPrem(docNo, company);
		 }
	}
	
	private List<QueryPostTransactionBean> queryTransactionGetSerialNoOnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionGetSerialNo",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) {
		List<DtSapPostSalesItems> listDtSapPostSalesItems = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderIdForUpdate(stSapPostHeader.getSapPostHeaderId())  ;
		for(DtSapPostSalesItems i : listDtSapPostSalesItems) {
			i.setSO_NO(dtSapOrderHeader.getRes_SalesOrderDocument());
			int num = Integer.valueOf(i.getRETAILSEQUENCENUMBER()) ;
			i.setITEM_NO((BeanUtil.lPad(num * 10, 4))); // make sure that seq sale order and post transaction is same seq
			//i.setIUID(( BeanUtil.isNotEmpty(i.getSERIALNUMBER()) ? i.getSERIALNUMBER() : null ));
			dtSapPostSalesItemsDao.update(i);
		}
	}

	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder25(DtSapTransaction dtSapTransaction) {

		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
			/** query origin order type 19 **/
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());
				//dtSapOrderHeaderNew.setDeliveryBlock("/");
				dtSapOrderHeaderNew.setDeliveryBlock("");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
				
				//query order  item
				List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
				
				/** query info receipt get serial **/
				List<QueryPostTransactionBean> listInvoiceDetail = queryTransactionGetSerialNo(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
				
				Map<String,List<String>> mapMatCpdeSerial = new HashMap<>();
				for(QueryPostTransactionBean el : listInvoiceDetail) {
					
					if(mapMatCpdeSerial.containsKey(el.getITEMID())) {
						if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
							mapMatCpdeSerial.get(el.getITEMID()).add(el.getSERIALNUMBER()) ;
						}else {
							mapMatCpdeSerial.get(el.getITEMID()).add("NON_SERIAL") ;
						}
						
					}else {
						List<String> listSerial = new ArrayList<>();
						if(BeanUtil.isNotEmpty(el.getSERIALNUMBER())) {
							listSerial.add(el.getSERIALNUMBER()) ;
						}else {
							listSerial.add("NON_SERIAL") ;
						}
						
						mapMatCpdeSerial.put(el.getITEMID(), listSerial) ;
					}
					
				}
				
				for(DtSapOrderItem el : listDtSapOrderItem ) {
					
					DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
					dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
					dtSapOrderItemNew.setCreateValue(masterValue);
					dtSapOrderItemNew.setChangeMode("U");
					dtSapOrderItemNew.setItem(el.getItem());
					dtSapOrderItemNew.setMaterialNumber(el.getMaterialNumber());
					
					dtSapOrderItemNew.setQuantity("");
					dtSapOrderItemNew.setSalesUnit("");
					dtSapOrderItemNew.setItemCategory("");
					
					dtSapOrderItemDao.insert(dtSapOrderItemNew);
					
					//serial
					if(mapMatCpdeSerial.get(el.getMaterialNumber()) != null) {
						for(String serial : mapMatCpdeSerial.get(el.getMaterialNumber())) {
							if(!serial.equals("NON_SERIAL")) {
								DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
								dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId()) ;
								dtSapOrderItemSerial.setChangeMode("U");
								dtSapOrderItemSerial.setNumberOfSerialNumber(serial);
								
								dtSapOrderItemSerial.setCreateValue(masterValue);;
								dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
							}
						}
					}
				}
			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return dtSapOrderHeaderNew ;

	}
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransaction14(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql1
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			List<QueryPostTransactionBean> listQueryPostTransactionBeanForDeposit = null ;
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getREF_NO()) && listQueryPostTransactionBean.get(0).getREF_NO().startsWith("D") ) {
				listQueryPostTransactionBeanForDeposit = queryPostTransactionSql1(listQueryPostTransactionBean.get(0).getREF_NO(), dtSapTransaction.getCompany());
			}
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
			
			
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
			
			String origtransnumber = "" ;
			if(listQueryPostTransactionBeanForDeposit.get(0).getORIGTRANSNUMBER().length() <= 20) {
				origtransnumber = listQueryPostTransactionBeanForDeposit.get(0).getORIGTRANSNUMBER() ;
			}else {
				origtransnumber = listQueryPostTransactionBeanForDeposit.get(0).getORIGTRANSNUMBER().substring(0, 20) ;
			}
			
			dtSapPostHeader.setORIGTRANSNUMBER(origtransnumber);
			
			
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			BigDecimal sumTenderAmount = new BigDecimal(0L);
			boolean disHeader = false ;
			// DISCOUNT HEADER======================
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
				disHeader = true ;
//				DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//				dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//				dtSapPostDiscountHeader.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//				dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//				dtSapPostDiscountHeader.setREDUCTIONAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				BigDecimal discountHeaderAmount = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				sumTenderAmount = sumTenderAmount.add(discountHeaderAmount);
//				dtSapPostDiscountHeader.setCreateValue(masterValue);
//				//set herder id
//				dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//				dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
			}
			
			//END DISCOUNT HEADER======================
			
			//TENDER
			
			List<QueryTenderBean> tenders = queryTenderPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			if(BeanUtil.isNotEmpty(tenders)) {
				boolean haveAp = false ;
				for(int i = 0 ; i < tenders.size() ; i ++) {
					DtSapPostTender t = new DtSapPostTender();
					t.setTENDERSEQUENCENUMBER(String.valueOf(i+1));
					
					if(tenders.get(i).getTENDERSEQUENCENUMBER().equals("99")) {
						t.setTENDERTYPECODE(tenders.get(i).getTENDERTYPECODE());
					}else if(BeanUtil.isNotEmpty(tenders.get(i).getFOC_FLG()) && tenders.get(i).getFOC_FLG().equals("Y")) {
						List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender("FOC", null) ;
						t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
					}
					else {
						List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(i).getTENDERTYPECODE(), null) ;
						if(fDtSapPostMappingTender.size() == 1) {
							t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
						}else {
							List<DtSapPostMappingTender> sDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(i).getTENDERTYPECODE(), tenders.get(i).getDOCTYPE()) ;
							t.setTENDERTYPECODE(sDtSapPostMappingTender.get(0).getTenderTypeId());
						}	
					}
					
					
					if(t.getTENDERTYPECODE().equals("ZT05")) {
						haveAp = true ;
						if(disHeader) {
							BigDecimal disAp = new BigDecimal(tenders.get(i).getTENDERAMOUNT()) ;
							BigDecimal disHerder = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT()) ;
							BigDecimal summ = disAp.add(disHerder) ;
							t.setTENDERAMOUNT(summ.toString());
						}else {
							t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
						}
					}else {
						t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
					}
					
					
					BigDecimal tenderAmount = new BigDecimal(t.getTENDERAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
					t.setTENDERCURRENCY(tenders.get(i).getTENDERCURRENCY());
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
			
			BigDecimal sumSaleAmount = new BigDecimal(0L);
			List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			int i = 0 ;
			int itemNo = 1 ;
			for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
				
				//SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
				
				// from config
				dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
				
				// fron config ***
				String[]  Retailreasoncode = config.getRetailreasoncode().split("\\|") ;
				if(Retailreasoncode.length == 1) {
					if(el.getPRODUCT_TYPE().equals("SERVICE")) {  // type  33 , 35
						dtSapPostSalesItems.setRETAILREASONCODE("ZR01");
					}else {
						dtSapPostSalesItems.setRETAILREASONCODE(config.getRetailreasoncode());
					}
					
				}else {
					dtSapPostSalesItems.setRETAILREASONCODE((el.getFREEGOODS_WAIT_FLG().equals("Y") ? Retailreasoncode[1] : Retailreasoncode[0]) );
				}
				
				
				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				
				dtSapPostSalesItems.setITEMID(el.getITEMID());  ///////////
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				
				BigDecimal SALESAMOUNT = null ; 
				BigDecimal NORMALSALESAMOUNT = null ;
				
				boolean itemMainFlg = false ;
				
				if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
					BigDecimal EXC_AMTMain = new BigDecimal(el.getEXC_AMT());
					BigDecimal vatMain = new BigDecimal(el.getTAXAMOUNT());
					BigDecimal EXC_AMTDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getEXC_AMT());	
					 SALESAMOUNT = EXC_AMTMain.subtract(EXC_AMTDeposit).add(vatMain) ;
					//dtSapPostSalesItems.setSALESAMOUNT(SALESAMOUNT.toString());
					
					BigDecimal PriceMain = new BigDecimal(el.getNORMALSALESAMOUNT());
					BigDecimal PriceDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
					 NORMALSALESAMOUNT = PriceMain.subtract(PriceDeposit);
					 BigDecimal SALESAMOUNTDEPO = SALESAMOUNT.add(PriceDeposit) ; 
					//dtSapPostSalesItems.setSALESAMOUNT(NORMALSALESAMOUNT.toString());
					dtSapPostSalesItems.setSALESAMOUNT(SALESAMOUNTDEPO.toString());
					dtSapPostSalesItems.setNORMALSALESAMOUNT("0");  
					itemMainFlg = true ;
					//BigDecimal ACTUALUNITPRICEMain = new BigDecimal(el.getACTUALUNITPRICE());
					//BigDecimal ACTUALUNITPRICE = ACTUALUNITPRICEMain.subtract(PriceDeposit);
					
					//dtSapPostSalesItems.setACTUALUNITPRICE(ACTUALUNITPRICE.toString());   ///////////
				}else {
					dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT());  ///////////
					dtSapPostSalesItems.setNORMALSALESAMOUNT(el.getNORMALSALESAMOUNT());  ///////////
					dtSapPostSalesItems.setACTUALUNITPRICE(el.getACTUALUNITPRICE());   ///////////
				}
				
				BigDecimal saleAmount = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
				
				dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(el.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
				
				//dtSapPostSalesItems.setSO_NO(el.getSO_NO());
				//dtSapPostSalesItems.setITEM_NO(el.getITEM_NO());
				//dtSapPostSalesItems.setIUID(el.getSERIALNUMBER());
				
				dtSapPostSalesItems.setCreateValue(masterValue);
				//set herder id
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
				itemNo = itemNo +1 ;
				i = i + 1 ;
				
				//DISCOUNT ITEM
				if(BeanUtil.isNotEmpty(el.getDISCOUNTSEQUENCENUMBER())) {
					DtSapPostDiscountItems dtSapPostDiscountItems = new DtSapPostDiscountItems();
					dtSapPostDiscountItems.setDISCOUNTID(el.getDISCOUNTID());
					dtSapPostDiscountItems.setDISCOUNTSEQUENCENUMBER(el.getDISCOUNTSEQUENCENUMBER());
					dtSapPostDiscountItems.setDISCOUNTTYPECODE(el.getDISCOUNTTYPECODE());
					
					if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
						//BigDecimal PriceMain = new BigDecimal(dtSapPostSalesItems.getNORMALSALESAMOUNT());
						//BigDecimal PriceMainDis = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
						
						BigDecimal REDUCTIONAMOUNT = NORMALSALESAMOUNT.subtract(SALESAMOUNT);
						dtSapPostDiscountItems.setREDUCTIONAMOUNT("-"+REDUCTIONAMOUNT.toString());
						//sumSaleAmount = sumSaleAmount.subtract(REDUCTIONAMOUNT);
					}else {
						dtSapPostDiscountItems.setREDUCTIONAMOUNT("-"+el.getREDUCTIONAMOUNT());   ///////////
					}
					
					dtSapPostDiscountItems.setCreateValue(masterValue);
					dtSapPostDiscountItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
					
					dtSapPostDiscountItemsDao.insert(dtSapPostDiscountItems);
				}
				
				
				//TAX ITEM
				if(BeanUtil.isNotEmpty(el.getTAXSEQUENCENUMBER())) {
					DtSapPostTaxItems dtSapPostTaxItems = new DtSapPostTaxItems();
					
					if(itemMainFlg) {
						BigDecimal dd = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
						BigDecimal dd1 = new BigDecimal(107);
						BigDecimal dd2 = new BigDecimal("7.00");
						
						BigDecimal dd3 = dd.multiply(dd2);
						BigDecimal dd4 = dd3.divide(dd1,2, RoundingMode.HALF_UP);
						dtSapPostTaxItems.setTAXAMOUNT(dd4.toString());
					}else {
						dtSapPostTaxItems.setTAXAMOUNT(el.getTAXAMOUNT());
					}

					dtSapPostTaxItems.setTAXSEQUENCENUMBER(el.getTAXSEQUENCENUMBER());
					dtSapPostTaxItems.setTAXTYPECODE(taxtCode.get(0).getLovVal());
					
					dtSapPostTaxItems.setCreateValue(masterValue);
					dtSapPostTaxItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
					dtSapPostTaxItemsDao.insert(dtSapPostTaxItems);
				}
				
				
			
			}
			
			// ITEM DEPOSIT
			List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;
			
			DtSapPostSalesItems dtSapPostSalesItemsDeposit = new DtSapPostSalesItems();
			dtSapPostSalesItemsDeposit.setRETAILSEQUENCENUMBER(String.valueOf(i+1));;
			dtSapPostSalesItemsDeposit.setRETAILTYPECODE(config.getRetailtypecode());
			dtSapPostSalesItemsDeposit.setITEMIDQUALIFIER("2");
			dtSapPostSalesItemsDeposit.setITEMID(matDeposit.get(0).getLovVal());
			dtSapPostSalesItemsDeposit.setRETAILQUANTITY("-1");
			dtSapPostSalesItemsDeposit.setSALESUNITOFMEASURE(matDeposit.get(0).getLovAttribute01());
			dtSapPostSalesItemsDeposit.setSALESAMOUNT("-" +listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
			dtSapPostSalesItemsDeposit.setNORMALSALESAMOUNT("0");
			dtSapPostSalesItemsDeposit.setSO_NO(null);
			//dtSapPostSalesItemsDeposit.setITEM_NO("0030");
			dtSapPostSalesItemsDeposit.setITEM_NO(null);
			dtSapPostSalesItemsDeposit.setCreateValue(masterValue);
			//set herder id
			dtSapPostSalesItemsDeposit.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
			dtSapPostSalesItemsDao.insert(dtSapPostSalesItemsDeposit);
			
			DtSapPostTaxItems dtSapPostTaxItemsDeposit = new DtSapPostTaxItems();
			
			BigDecimal dd = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
			BigDecimal dd1 = new BigDecimal(107);
			BigDecimal dd2 = new BigDecimal("7.00");
			
			BigDecimal dd3 = dd.multiply(dd2);
			BigDecimal dd4 = dd3.divide(dd1,2, RoundingMode.HALF_UP);
			
			dtSapPostTaxItemsDeposit.setTAXAMOUNT("-" + dd4.toString());
			dtSapPostTaxItemsDeposit.setTAXSEQUENCENUMBER("1");
			dtSapPostTaxItemsDeposit.setTAXTYPECODE(taxtCode.get(0).getLovVal());
			
			dtSapPostTaxItemsDeposit.setCreateValue(masterValue);
			dtSapPostTaxItemsDeposit.setSapPostSalesItemId(dtSapPostSalesItemsDeposit.getSapPostSalesItemId());
			dtSapPostTaxItemsDao.insert(dtSapPostTaxItemsDeposit);
			
			BigDecimal depositAmt = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
			sumSaleAmount = sumSaleAmount.subtract(depositAmt);
			
			
			//adjust tender
			//if(!sumSaleAmount.equals(sumTenderAmount)) {
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
			e.printStackTrace();
			return null ;
		}
	}
	
	public void updatePostTransactionBeforeCallSapType14(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) {
		List<DtSapPostSalesItems> listDtSapPostSalesItems = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderIdForUpdate(stSapPostHeader.getSapPostHeaderId())  ;
		List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId()) ;
		
		Map<String,String> mapNatItenNo = new HashMap<>();
		Map<String,String> mapDup = new HashMap<>();
		Map<String,String> mapDupKey = new HashMap<>();
		int dupNoOrder = 1 ;
		int dupNoPost = 0 ;
		
		for(DtSapOrderItem el : listDtSapOrderItem) {
			if(mapNatItenNo.containsKey(el.getMaterialNumber())) {
				mapDupKey.put(el.getMaterialNumber(), el.getMaterialNumber());
				mapDup.put(el.getMaterialNumber() +"_"+ String.valueOf(dupNoOrder), el.getItem()) ;
				dupNoOrder += 1 ;
			}else {
				mapNatItenNo.put(el.getMaterialNumber(), el.getItem());
			}
			
		}
		
		for(DtSapPostSalesItems i : listDtSapPostSalesItems) {
			i.setSO_NO(dtSapOrderHeader.getRes_SalesOrderDocument());
			if(BeanUtil.isEmpty(i.getITEM_NO())) {
				if(mapDupKey.containsKey(i.getITEMID())) {
					if(dupNoPost == 0) {
						i.setITEM_NO(mapNatItenNo.get(i.getITEMID())); 
					}else {
						i.setITEM_NO(mapDup.get(i.getITEMID()+"_"+ String.valueOf(dupNoPost))); 
					}
					
					dupNoPost += 1 ;
				}else {
					i.setITEM_NO(mapNatItenNo.get(i.getITEMID())); 
				}
				
			}
			
			dtSapPostSalesItemsDao.update(i);
		}
	}
	
	public DtSapPostHeader queryInfoAndInsertPostTransaction14WithoutSaleOrder(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql1
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			List<QueryPostTransactionBean> listQueryPostTransactionBeanForDeposit = null ;
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getREF_NO()) && listQueryPostTransactionBean.get(0).getREF_NO().startsWith("D") ) {
				listQueryPostTransactionBeanForDeposit = queryPostTransactionSql1(listQueryPostTransactionBean.get(0).getREF_NO(), dtSapTransaction.getCompany());
			}
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(1L) ;
			
			
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
			
//			String origtransnumber = "" ;
//			if(listQueryPostTransactionBeanForDeposit.get(0).getORIGTRANSNUMBER().length() <= 20) {
//				origtransnumber = listQueryPostTransactionBeanForDeposit.get(0).getORIGTRANSNUMBER() ;
//			}else {
//				origtransnumber = listQueryPostTransactionBeanForDeposit.get(0).getORIGTRANSNUMBER().substring(0, 20) ;
//			}
//			
//			dtSapPostHeader.setORIGTRANSNUMBER(origtransnumber);
			
			
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			BigDecimal sumTenderAmount = new BigDecimal(0L);
			boolean disHeader = false ;
			// DISCOUNT HEADER======================
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
				disHeader = true ;
//				DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//				dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//				dtSapPostDiscountHeader.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//				dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//				dtSapPostDiscountHeader.setREDUCTIONAMOUNT("-"+listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				BigDecimal discountHeaderAmount = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				sumTenderAmount = sumTenderAmount.add(discountHeaderAmount);
//				dtSapPostDiscountHeader.setCreateValue(masterValue);
//				//set herder id
//				dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//				dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
			}
			
			//END DISCOUNT HEADER======================
			
			//TENDER
			
			List<QueryTenderBean> tenders = queryTenderPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			if(BeanUtil.isNotEmpty(tenders)) {
				boolean haveAp = false ;
				for(int i = 0 ; i < tenders.size() ; i ++) {
					DtSapPostTender t = new DtSapPostTender();
					t.setTENDERSEQUENCENUMBER(String.valueOf(i+1));
					
					if(tenders.get(i).getTENDERSEQUENCENUMBER().equals("99")) {
						t.setTENDERTYPECODE(tenders.get(i).getTENDERTYPECODE());
					}else if(BeanUtil.isNotEmpty(tenders.get(i).getFOC_FLG()) && tenders.get(i).getFOC_FLG().equals("Y")) {
						List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender("FOC", null) ;
						t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
					}
					else {
						List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(i).getTENDERTYPECODE(), null) ;
						if(fDtSapPostMappingTender.size() == 1) {
							t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
						}else {
							List<DtSapPostMappingTender> sDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(i).getTENDERTYPECODE(), tenders.get(i).getDOCTYPE()) ;
							t.setTENDERTYPECODE(sDtSapPostMappingTender.get(0).getTenderTypeId());
						}	
					}
					
					
					if(t.getTENDERTYPECODE().equals("ZT05")) {
						haveAp = true ;
						if(disHeader) {
							BigDecimal disAp = new BigDecimal(tenders.get(i).getTENDERAMOUNT()) ;
							BigDecimal disHerder = new BigDecimal(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT()) ;
							BigDecimal summ = disAp.add(disHerder) ;
							t.setTENDERAMOUNT(summ.toString());
						}else {
							t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
						}
					}else {
						t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
					}
					
					
					BigDecimal tenderAmount = new BigDecimal(t.getTENDERAMOUNT());
					sumTenderAmount = sumTenderAmount.add(tenderAmount);
					t.setTENDERCURRENCY(tenders.get(i).getTENDERCURRENCY());
					t.setCreateValue(masterValue);
					//set herder id
					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					dtSapPostTenderDao.insert(t);
					
//					t.setTENDERAMOUNT(tenders.get(i).getTENDERAMOUNT());
//					BigDecimal tenderAmount = new BigDecimal(tenders.get(i).getTENDERAMOUNT());
//					sumTenderAmount = sumTenderAmount.add(tenderAmount);
//					t.setTENDERCURRENCY(tenders.get(i).getTENDERCURRENCY());
//					t.setCreateValue(masterValue);
//					//set herder id
//					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//					dtSapPostTenderDao.insert(t);
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
			
			BigDecimal sumSaleAmount = new BigDecimal(0L);
			List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			int i = 0 ;
			int itemNo = 1 ;
			for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
				
				//SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
				
				// from config
				dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
				
				// fron config ***
				String[]  Retailreasoncode = config.getRetailreasoncode().split("\\|") ;
				if(Retailreasoncode.length == 1) {
					if(el.getPRODUCT_TYPE().equals("SERVICE")) {  // type  33 , 35
						dtSapPostSalesItems.setRETAILREASONCODE("ZR01");
					}else {
						dtSapPostSalesItems.setRETAILREASONCODE(config.getRetailreasoncode());
					}
					
				}else {
					dtSapPostSalesItems.setRETAILREASONCODE((el.getFREEGOODS_WAIT_FLG().equals("Y") ? Retailreasoncode[1] : Retailreasoncode[0]) );
				}
				
				
				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				
				dtSapPostSalesItems.setITEMID(el.getITEMID());  ///////////
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				
				BigDecimal SALESAMOUNT = null ; 
				BigDecimal NORMALSALESAMOUNT = null ;
				
				if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
					BigDecimal EXC_AMTMain = new BigDecimal(el.getEXC_AMT());
					BigDecimal vatMain = new BigDecimal(el.getTAXAMOUNT());
					BigDecimal EXC_AMTDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getEXC_AMT());	
					 SALESAMOUNT = EXC_AMTMain.subtract(EXC_AMTDeposit).add(vatMain) ;
					dtSapPostSalesItems.setSALESAMOUNT(SALESAMOUNT.toString());
					
					BigDecimal PriceMain = new BigDecimal(el.getNORMALSALESAMOUNT());
					BigDecimal PriceDeposit = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
					 NORMALSALESAMOUNT = PriceMain.subtract(PriceDeposit);
					
					//dtSapPostSalesItems.setSALESAMOUNT(NORMALSALESAMOUNT.toString());
					dtSapPostSalesItems.setNORMALSALESAMOUNT(NORMALSALESAMOUNT.toString());  
					
					BigDecimal ACTUALUNITPRICEMain = new BigDecimal(el.getACTUALUNITPRICE());
					BigDecimal ACTUALUNITPRICE = ACTUALUNITPRICEMain.subtract(PriceDeposit);
					
					dtSapPostSalesItems.setACTUALUNITPRICE(ACTUALUNITPRICE.toString());   ///////////
				}else {
					dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT());  ///////////
					dtSapPostSalesItems.setNORMALSALESAMOUNT(el.getNORMALSALESAMOUNT());  ///////////
					dtSapPostSalesItems.setACTUALUNITPRICE(el.getACTUALUNITPRICE());   ///////////
				}
				
				BigDecimal saleAmount = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
				
				dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(el.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
				
				//dtSapPostSalesItems.setSO_NO(el.getSO_NO());
				//dtSapPostSalesItems.setITEM_NO(el.getITEM_NO());
				//dtSapPostSalesItems.setIUID(el.getSERIALNUMBER());
				
				dtSapPostSalesItems.setCreateValue(masterValue);
				//set herder id
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
				itemNo = itemNo +1 ;
				i = i + 1 ;
				
				//DISCOUNT ITEM
				if(BeanUtil.isNotEmpty(el.getDISCOUNTSEQUENCENUMBER())) {
					DtSapPostDiscountItems dtSapPostDiscountItems = new DtSapPostDiscountItems();
					dtSapPostDiscountItems.setDISCOUNTID(el.getDISCOUNTID());
					dtSapPostDiscountItems.setDISCOUNTSEQUENCENUMBER(el.getDISCOUNTSEQUENCENUMBER());
					dtSapPostDiscountItems.setDISCOUNTTYPECODE(el.getDISCOUNTTYPECODE());
					
					if(listQueryPostTransactionBeanForDeposit != null && listQueryPostTransactionBeanForDeposit.get(0).getITEMID().equals(el.getITEMID())) {
						//BigDecimal PriceMain = new BigDecimal(dtSapPostSalesItems.getNORMALSALESAMOUNT());
						//BigDecimal PriceMainDis = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
						
						BigDecimal REDUCTIONAMOUNT = NORMALSALESAMOUNT.subtract(SALESAMOUNT);
						dtSapPostDiscountItems.setREDUCTIONAMOUNT("-"+REDUCTIONAMOUNT.toString());
						//sumSaleAmount = sumSaleAmount.subtract(REDUCTIONAMOUNT);
					}else {
						dtSapPostDiscountItems.setREDUCTIONAMOUNT("-"+el.getREDUCTIONAMOUNT());   ///////////
					}
					
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
				
			
			}
			
//			// ITEM DEPOSIT
//			DtSapPostSalesItems dtSapPostSalesItemsDeposit = new DtSapPostSalesItems();
//			dtSapPostSalesItemsDeposit.setRETAILSEQUENCENUMBER(String.valueOf(i+1));;
//			dtSapPostSalesItemsDeposit.setRETAILTYPECODE(config.getRetailtypecode());
//			dtSapPostSalesItemsDeposit.setITEMIDQUALIFIER("2");
//			dtSapPostSalesItemsDeposit.setITEMID("1000000999");
//			dtSapPostSalesItemsDeposit.setRETAILQUANTITY("-1");
//			dtSapPostSalesItemsDeposit.setSALESUNITOFMEASURE("PC");
//			dtSapPostSalesItemsDeposit.setSALESAMOUNT("-" +listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
//			dtSapPostSalesItemsDeposit.setNORMALSALESAMOUNT("0");
//			dtSapPostSalesItemsDeposit.setSO_NO(null);
//			dtSapPostSalesItemsDeposit.setITEM_NO("0030");
//			dtSapPostSalesItemsDeposit.setCreateValue(masterValue);
//			//set herder id
//			dtSapPostSalesItemsDeposit.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//			dtSapPostSalesItemsDao.insert(dtSapPostSalesItemsDeposit);
			
			//BigDecimal depositAmt = new BigDecimal(listQueryPostTransactionBeanForDeposit.get(0).getSALESAMOUNT());
			//sumSaleAmount = sumSaleAmount.subtract(depositAmt);
			
			
			//adjust tender
			//if(!sumSaleAmount.equals(sumTenderAmount)) {
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
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public List<DtSapCancelReserve> cancelReserveSale(DtSapTransaction dtSapTransaction) {
		List<DtSapCancelReserve> res = new ArrayList<>();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql1
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean) && BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getSAP_RESERVE_NO())) {
				String[] listReserve = listQueryPostTransactionBean.get(0).getSAP_RESERVE_NO().split("\\|");
				
				for(String re : listReserve ) {
					DtSapCancelReserve dtSapCancelReserve = new DtSapCancelReserve();
					dtSapCancelReserve.setCreateValue(masterValue) ;
					dtSapCancelReserve.setSapTranId(dtSapTransaction.getSapTranId());
					dtSapCancelReserve.setStatus("W");
					dtSapCancelReserve.setSapReserveNo(re) ;;
					dtSapCancelReserveDao.insert(dtSapCancelReserve);
					
					res.add(dtSapCancelReserve);
				}
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res ;

	}
	
	public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve) {
		DtSapCancelReserve res = sapCallApiService.callCancelReserveApi(dtSapCancelReserve);
		return res ;
	}

	@Override
	public List<DtSapOrderHeader> queryDtSapOrderHeaderBySapTranId(Long sapTransId) throws DataAccessException {
		return dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(sapTransId);
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder67(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query doc deposit No 
			String  refNo = queryRefNoReceipt2(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			
			// check if depositNo used more than 1 time
			String  receiptCancel = queryRefNoReceiptUsed(refNo, dtSapTransaction.getCompany()) ;
			
			if(BeanUtil.isEmpty(receiptCancel)) {
				// query origin order type 12
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(refNo, dtSapTransaction.getCompany() , 12L) ;
				
				if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
					//query header
					List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
					
					//insert header
					//DtSapOrderHeader dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
					dtSapOrderHeaderNew.setChangeMode("U");
					dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
					dtSapOrderHeaderNew.setDeliveryBlock("/");
					dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
					dtSapOrderHeaderNew.setCreateValue(masterValue);
					dtSapOrderHeaderNew.setStatus("W");
					dtSapOrderHeaderNew.setSalesDocType("");
					dtSapOrderHeaderNew.setSalesOrganiztion("");
					dtSapOrderHeaderNew.setDistributionChannel("");
					dtSapOrderHeaderNew.setDivision("");
					dtSapOrderHeaderNew.setShippingConditions("");
					
					dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
					
					//query order  item
					List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
					
					//query info receipt sql1
					List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
					
					//get item receipt
					Map<String,String> mapMatCpdeReceiptFreeGoods = new HashMap<>();
					for(QueryPostTransactionBean k : listQueryPostTransactionBean) {
						BigDecimal saleamount = new BigDecimal(k.getSALESAMOUNT());
						if( !k.getPRODUCT_TYPE().equals("SERVICE") && saleamount.compareTo(new BigDecimal("0")) <= 0  ) {  // free goods 
							if(mapMatCpdeReceiptFreeGoods.containsKey(k.getITEMID())) {
								int qty = Integer.valueOf( mapMatCpdeReceiptFreeGoods.get(k.getITEMID()));
								int qtyP = Integer.valueOf(k.getRETAILQUANTITY()) ;
								mapMatCpdeReceiptFreeGoods.put(k.getITEMID(), String.valueOf(qty+qtyP));
							}else {
								mapMatCpdeReceiptFreeGoods.put(k.getITEMID(), k.getRETAILQUANTITY());
							}
						}
					}
					
					
//					//old item
//					Map<String,String> mapMatCpdeOld = new HashMap<>();
//					int noItem = 0 ;
//					for(DtSapOrderItem el : listDtSapOrderItem ) {
//						
//						List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;
//
//						if(matDeposit.get(0).getLovVal().equals(el.getMaterialNumber())) {
//							continue ;
//						}
//						s
//						
//						if(!mapMatCpdeOld.containsKey(el.getMaterialNumber())) {
//							mapMatCpdeOld.put(el.getMaterialNumber(), el.getMaterialNumber()) ;
//						}						
//					}
					
					
					/////////////////////////////////////////////////////
					
					//old item
					Map<String,String> mapMatCpdeOld = new HashMap<>();
					//Map<String,String> mapMatReject = new HashMap<>();
					int noItem = listDtSapOrderItem.size() ;
					for(DtSapOrderItem el : listDtSapOrderItem ) {
                       // boolean rejectFlg = false ;  
												
						List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;

						if(matDeposit.get(0).getLovVal().equals(el.getMaterialNumber())) {  // item 0020
							continue ;
						}
						else if(el.getItem().equals("0010")) {  // item 0010
							mapMatCpdeOld.put(el.getMaterialNumber(), el.getItem()) ;
						}
						else if(mapMatCpdeReceiptFreeGoods.containsKey(el.getMaterialNumber())) {  // update
							mapMatCpdeOld.put(el.getMaterialNumber(), el.getItem()) ;
						}
						else if(!mapMatCpdeReceiptFreeGoods.containsKey(el.getMaterialNumber())){ // reject
						//	mapMatReject.put(el.getMaterialNumber(), el.getItem());
							
							//ITEM
							DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
							dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
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
					
					/////////////////////////////////////////////////////
					
					//new item ex free goods ,etc
					//List<QueryPostTransactionBean> listQueryPostTransactionBean
					//for(String k : mapMatCpdeSerial.keySet()) {
					for(QueryPostTransactionBean k : listQueryPostTransactionBean) {	
						if(!mapMatCpdeOld.containsKey(k.getITEMID())) {
							noItem = noItem + 1 ;
							DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
							dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
							dtSapOrderItemNew.setCreateValue(masterValue);
							
							dtSapOrderItemNew.setChangeMode("C");
							dtSapOrderItemNew.setBillingBlock("/");
							dtSapOrderItemNew.setItem(BeanUtil.lPad((noItem) * 10, 4));
							dtSapOrderItemNew.setMaterialNumber(k.getITEMID());
							dtSapOrderItemNew.setQuantity(k.getRETAILQUANTITY());
							dtSapOrderItemNew.setSalesUnit(k.getSALESUNITOFMEASURE());
							dtSapOrderItemNew.setPlant(listDtSapOrderItem.get(0).getPlant()) ;
							dtSapOrderItemNew.setStorageLocation(listDtSapOrderItem.get(0).getStorageLocation());
							
							if(k.getPRODUCT_TYPE().equals("SERVICE")) {
								dtSapOrderItemNew.setItemCategory("ZRS3");
							}else {
								BigDecimal saleamount = new BigDecimal(k.getSALESAMOUNT());
								if(saleamount.compareTo(new BigDecimal("0")) > 0  ) {
									dtSapOrderItemNew.setItemCategory("ZRS1");
								}else {
									dtSapOrderItemNew.setItemCategory("ZRSA");
								}
							}
							
							dtSapOrderItemDao.insert(dtSapOrderItemNew);
							
							if(!dtSapOrderItemNew.getItemCategory().equals("ZRSA")) {  // not free goods
								//itemcon
								DtSapOrderItemCon dtSapOrderItemConPriceAmount = new DtSapOrderItemCon();
								dtSapOrderItemConPriceAmount.setChangeMode("C");
								dtSapOrderItemConPriceAmount.setConditionType("PN10");
								dtSapOrderItemConPriceAmount.setCurrency("THB");
								dtSapOrderItemConPriceAmount.setAmount(k.getSALESAMOUNT());
								
								dtSapOrderItemConPriceAmount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
								dtSapOrderItemConPriceAmount.setCreateValue(masterValue);
								dtSapOrderItemConDao.insert(dtSapOrderItemConPriceAmount);
								
								if(BeanUtil.isNotEmpty(k.getREDUCTIONAMOUNT()) ) {
									DtSapOrderItemCon dtSapOrderItemConPriceDiscount = new DtSapOrderItemCon();
									dtSapOrderItemConPriceDiscount.setChangeMode("C");
									dtSapOrderItemConPriceDiscount.setConditionType("ZD01");
									dtSapOrderItemConPriceDiscount.setCurrency("THB");
									dtSapOrderItemConPriceDiscount.setAmount(k.getREDUCTIONAMOUNT());
									
									dtSapOrderItemConPriceDiscount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
									dtSapOrderItemConPriceDiscount.setCreateValue(masterValue);
									dtSapOrderItemConDao.insert(dtSapOrderItemConPriceDiscount);
								}
								
							}
							
							//serial
							if(BeanUtil.isNotEmpty(k.getSERIALNUMBER())) {
								DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial() ;
								dtSapOrderItemSerial.setChangeMode("C");
								dtSapOrderItemSerial.setCreateValue(masterValue);
								dtSapOrderItemSerial.setNumberOfSerialNumber(k.getSERIALNUMBER());
								dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
								
								dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
							}
							
							
						}else { 
							if(mapMatCpdeOld.get(k.getITEMID()).equals("0010")) { // mat main								
								DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
								dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
								dtSapOrderItemNew.setCreateValue(masterValue);
								
								dtSapOrderItemNew.setChangeMode("U");
								dtSapOrderItemNew.setBillingBlock("/");
								dtSapOrderItemNew.setItem("0010");
								dtSapOrderItemNew.setMaterialNumber(k.getITEMID());
								
								
								dtSapOrderItemNew.setQuantity("");
								dtSapOrderItemNew.setSalesUnit("");
								dtSapOrderItemNew.setItemCategory("");
								
								dtSapOrderItemDao.insert(dtSapOrderItemNew);
								
								//itemcon
								DtSapOrderItemCon dtSapOrderItemConPriceAmount = new DtSapOrderItemCon();
								dtSapOrderItemConPriceAmount.setChangeMode("U");
								dtSapOrderItemConPriceAmount.setConditionType("PN10");
								dtSapOrderItemConPriceAmount.setCurrency("THB");
								dtSapOrderItemConPriceAmount.setAmount(k.getSALESAMOUNT());
								
								dtSapOrderItemConPriceAmount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
								dtSapOrderItemConPriceAmount.setCreateValue(masterValue);
								dtSapOrderItemConDao.insert(dtSapOrderItemConPriceAmount);
								
								if(BeanUtil.isNotEmpty(k.getREDUCTIONAMOUNT()) ) {
									DtSapOrderItemCon dtSapOrderItemConPriceDiscount = new DtSapOrderItemCon();
									dtSapOrderItemConPriceDiscount.setChangeMode("U");
									dtSapOrderItemConPriceDiscount.setConditionType("ZD01");
									dtSapOrderItemConPriceDiscount.setCurrency("THB");
									dtSapOrderItemConPriceDiscount.setAmount(k.getREDUCTIONAMOUNT());
									
									dtSapOrderItemConPriceDiscount.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());	
									dtSapOrderItemConPriceDiscount.setCreateValue(masterValue);
									dtSapOrderItemConDao.insert(dtSapOrderItemConPriceDiscount);
								}
									
								
								
								//serial
								if(BeanUtil.isNotEmpty(k.getSERIALNUMBER())) {
									DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial() ;
									dtSapOrderItemSerial.setChangeMode("U");
									dtSapOrderItemSerial.setCreateValue(masterValue);
									dtSapOrderItemSerial.setNumberOfSerialNumber(k.getSERIALNUMBER());
									dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
									
									dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
								}
							}else { // free goods in old order update
								
								DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
								dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
								dtSapOrderItemNew.setCreateValue(masterValue);
								
								//dtSapOrderItemNew.setChangeMode("U");
								
								dtSapOrderItemNew.setItem("");
								dtSapOrderItemNew.setMaterialNumber("");
								dtSapOrderItemNew.setQuantity("");
								dtSapOrderItemNew.setSalesUnit("");
								dtSapOrderItemNew.setItemCategory("");
								
								dtSapOrderItemNew.setChangeMode("U");
								dtSapOrderItemNew.setItem(mapMatCpdeOld.get(k.getITEMID()));
								dtSapOrderItemNew.setMaterialNumber(k.getITEMID());
								dtSapOrderItemNew.setBillingBlock("/");
								dtSapOrderItemNew.setQuantity(mapMatCpdeReceiptFreeGoods.get(k.getITEMID()));
								
								dtSapOrderItemDao.insert(dtSapOrderItemNew);
								
								//serial
								if(BeanUtil.isNotEmpty(k.getSERIALNUMBER())) {
									DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial() ;
									dtSapOrderItemSerial.setChangeMode("U");
									dtSapOrderItemSerial.setCreateValue(masterValue);
									dtSapOrderItemSerial.setNumberOfSerialNumber(k.getSERIALNUMBER());
									dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
									
									dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
								}
								
								
							}	
						
						}
					}
					
				}
			}
			

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}
	
	private String queryRefNoReceipt2(String receiptNo , String company) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// postgres
				return dtSapOrderHeaderDao.queryRefNoReceipt2(receiptNo, company);
		 }  
		  else { 
			// query from on prem
				return queryRefNo2ReceiptOnPrem(receiptNo, company) ;
		 }	
	}
	
	private String queryRefNo2ReceiptOnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryRefNo2Receipt",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getRefDocNo() ;
   }
	
	public void updatedtSapCancelReserve(DtSapCancelReserve dtSapCancelReserve) {
		dtSapCancelReserveDao.update(dtSapCancelReserve);
	}
	
	private List<SaleOrderTransection> getSaleOrderTransectionByReceiptNoOnprem(String docNo , String company ) {
        try {
        	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    		HttpClientUtilDT c = new HttpClientUtilDT();
    		Map<String, String> properties = new HashMap<String, String>();
            properties.put("Content-Type", "application/json");
            
            GetSaleOrderTransectionForCloudBean in = new GetSaleOrderTransectionForCloudBean();
            in.setReceiptNo(docNo) ;
            in.setCompany(company);
            
    		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/create-receipt/v1/get-saleorder-transection-byreceiptno-for-cloud",gson.toJson(in) , "POST", null) ;
    		Gson gsonRes = new Gson();
    		GetSaleOrderTransectionForCloudBean res = gsonRes.fromJson(out, GetSaleOrderTransectionForCloudBean.class);
    		return res.getListSaleOrderTransection() ;
        }catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
		
   }
	
	private List<SaleOrderTransection> getSaleOrderTransectionByReceiptNoOnCloud(String docNo , String company ) {
		return saleOrderTransectionDao.listSaleOrderTransactionByCriteria(null, company, null, null, docNo, null);
   }

	@Override
	public List<Long> listCmrNoBilling() {
		return dtSapTransactionDao.listCmrNoBilling();
	}
	
	@Override
	public List<DtSapOrderHeader> unRejectItemAndBlockOrder(Long sapTransId) {
		List<DtSapOrderHeader> res = new ArrayList<>();
		
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy("DTApp");
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy("DTApp");
			
			//List<DtSapOrderHeader> orderCMR = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(sapTransId);
			List<DtSapOrderHeader> orderCMR = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranIdWithOutyourreference(sapTransId);
			// expec size = 2 only
			if(BeanUtil.isNotEmpty(orderCMR) && orderCMR.size() == 2 && BeanUtil.isNotEmpty(orderCMR.get(0).getSalesOrderDocument())) {
				DtSapTransaction dtSapTransaction = new DtSapTransaction();
				dtSapTransaction.setCompany("-");
				dtSapTransaction.setDocNo(orderCMR.get(0).getSalesOrderDocument());
				dtSapTransaction.setStatus("W");
				dtSapTransaction.setTransactionType(71L);
				dtSapTransaction.setCreateValue(masterValue);
				
				dtSapTransactionDao.insert(dtSapTransaction);
				
				Long sapid = dtSapTransaction.getSapTranId() ;
				// 1  insert un reject
				// 2   delivery block
				
				// insert header	 insert un reject
				DtSapOrderHeader dtSapOrderHeaderUnReject = new DtSapOrderHeader();
				dtSapOrderHeaderUnReject.setSapTranId(sapid);
				dtSapOrderHeaderUnReject.setPartnerName("DT");
				dtSapOrderHeaderUnReject.setChangeMode("U");
				dtSapOrderHeaderUnReject.setSalesOrderDocument(orderCMR.get(0).getSalesOrderDocument());
				dtSapOrderHeaderUnReject.setStatus("W");
				
				dtSapOrderHeaderUnReject.setSalesDocType("");
				dtSapOrderHeaderUnReject.setSalesOrganiztion("");
				dtSapOrderHeaderUnReject.setDistributionChannel("");
				dtSapOrderHeaderUnReject.setDivision("");
				dtSapOrderHeaderUnReject.setShippingConditions("");
				dtSapOrderHeaderUnReject.setDeliveryBlock("");	
				
				MasterValue masterValue1 = new MasterValue();
				masterValue1.setCreated(date);
				masterValue1.setCreatedBy("DTApp");
				masterValue1.setLastUpd(date);
				masterValue1.setLastUpdBy("DTApp");
				dtSapOrderHeaderUnReject.setCreateValue(masterValue1);
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderUnReject);
				res.add(dtSapOrderHeaderUnReject);
				
				// insert header	 insert delivery block
				DtSapOrderHeader dtSapOrderHeaderZ7 = new DtSapOrderHeader();
				dtSapOrderHeaderZ7.setSapTranId(sapid);
				dtSapOrderHeaderZ7.setPartnerName("DT");
				dtSapOrderHeaderZ7.setChangeMode("U");
				dtSapOrderHeaderZ7.setSalesOrderDocument(orderCMR.get(0).getSalesOrderDocument());
				dtSapOrderHeaderZ7.setStatus("W");
				
				dtSapOrderHeaderZ7.setSalesDocType("");
				dtSapOrderHeaderZ7.setSalesOrganiztion("");
				dtSapOrderHeaderZ7.setDistributionChannel("");
				dtSapOrderHeaderZ7.setDivision("");
				dtSapOrderHeaderZ7.setShippingConditions("");
				dtSapOrderHeaderZ7.setDeliveryBlock("Z7");	
				
				MasterValue masterValue2 = new MasterValue();
				masterValue2.setCreated(date);
				masterValue2.setCreatedBy("DTApp");
				masterValue2.setLastUpd(date);
				masterValue2.setLastUpdBy("DTApp");
				dtSapOrderHeaderZ7.setCreateValue(masterValue2);
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderZ7);
				res.add(dtSapOrderHeaderZ7);
				
				List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderCMR.get(0).getSapOrderHeaderId());;
			    if(BeanUtil.isNotEmpty(listDtSapOrderItem)) {
					for (DtSapOrderItem el : listDtSapOrderItem) {					
						///
						//ITEM
						DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
						dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeaderUnReject.getSapOrderHeaderId());
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
			
						MasterValue masterValue3 = new MasterValue();
						masterValue3.setCreated(date);
						masterValue3.setCreatedBy("DTApp");
						masterValue3.setLastUpd(date);
						masterValue3.setLastUpdBy("DTApp");
						dtSapOrderItem.setCreateValue(masterValue3);
						dtSapOrderItemDao.insert(dtSapOrderItem);
						
						////ITEM cancel
						DtSapOrderItemCancel dtSapOrderItemCancel = new DtSapOrderItemCancel();
						dtSapOrderItemCancel.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
						dtSapOrderItemCancel.setChangeMode("U");
						dtSapOrderItemCancel.setReasonforRejection("/");
						
						MasterValue masterValue4 = new MasterValue();
						masterValue4.setCreated(date);
						masterValue4.setCreatedBy("DTApp");
						masterValue4.setLastUpd(date);
						masterValue4.setLastUpdBy("DTApp");
						dtSapOrderItemCancel.setCreateValue(masterValue4);
						dtSapOrderItemCancelDao.insert(dtSapOrderItemCancel);
						
					}
			    }
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return res ;
	}
	
	@Override
	public void updateCmrForResend(Long sapTransId) {
		
		try {
			List<DtSapOrderHeader> orderCMR = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(sapTransId);
			List<DtSapPostHeader>  postCN =   dtSapPostHeaderDao.getDtSapPostHeaderBySapTranId(sapTransId) ;
			
			DtSapTransaction dtSapTransaction  = dtSapTransactionDao.getByLongPrimaryKey(sapTransId);
			
			if(BeanUtil.isNotEmpty(orderCMR)) {
				for(DtSapOrderHeader el : orderCMR ) {
					el.setStatus("F");
					el.getCreateValue().setLastUpd(new Date());
					el.getCreateValue().setLastUpdBy("DTApp");
					dtSapOrderHeaderDao.update(el);;
				}
			}
			
            if(BeanUtil.isNotEmpty(postCN)) {
            	for(DtSapPostHeader el : postCN ) {
					el.setStatus("F");
					el.getCreateValue().setLastUpd(new Date());
					el.getCreateValue().setLastUpdBy("DTApp");
					dtSapPostHeaderDao.update(el);
				}
			}
            
            if(BeanUtil.isNotEmpty(dtSapTransaction)) {
            	
            	dtSapTransactionDao.updateWithAddTime(dtSapTransaction.getSapTranId());;
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
	}
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate14(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query doc deposit No 
			String  refNo = queryRefNoReceipt(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			
			// check if depositNo used more than 1 time
			String  receiptCancel = queryRefNoReceiptUsed(refNo, dtSapTransaction.getCompany()) ;
			
			if(BeanUtil.isEmpty(receiptCancel)) {
				// query origin order type 12
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(refNo, dtSapTransaction.getCompany() , 12L) ;
				
				if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
					//query header
					List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
					
					//insert header
					//DtSapOrderHeader dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
					dtSapOrderHeaderNew.setChangeMode("U");
					dtSapOrderHeaderNew.setStatus("W");
					dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
					dtSapOrderHeaderNew.setYourReference("/");
					dtSapOrderHeaderNew.setSalesDocType("");
					dtSapOrderHeaderNew.setSalesOrganiztion("");
					dtSapOrderHeaderNew.setDistributionChannel("");
					dtSapOrderHeaderNew.setDivision("");
					dtSapOrderHeaderNew.setShippingConditions("");
					
					dtSapOrderHeaderNew.setDeliveryBlock("");
					dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
					dtSapOrderHeaderNew.setCreateValue(masterValue);

					dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);

			}
		}
				
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate28(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query doc stf 
			String  stfDoc = queryRefNoReceipt(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			
			// query origin order type 26
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(stfDoc, dtSapTransaction.getCompany() , 26L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				//query header
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				//insert header
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
				dtSapOrderHeaderNew.setYourReference("/");
				dtSapOrderHeaderNew.setDeliveryBlock("");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate67(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query doc deposit No 
			String  refNo = queryRefNoReceipt2(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			
			// check if depositNo used more than 1 time
			String  receiptCancel = queryRefNoReceiptUsed(refNo, dtSapTransaction.getCompany()) ;
			
			if(BeanUtil.isEmpty(receiptCancel)) {
				// query origin order type 12
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(refNo, dtSapTransaction.getCompany() , 12L) ;
				
				if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
					//query header
					List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
					
					//insert header
					//DtSapOrderHeader dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
					dtSapOrderHeaderNew.setChangeMode("U");
					dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
					dtSapOrderHeaderNew.setYourReference("/");
					dtSapOrderHeaderNew.setDeliveryBlock("");
					dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
					dtSapOrderHeaderNew.setCreateValue(masterValue);
					dtSapOrderHeaderNew.setStatus("W");
					dtSapOrderHeaderNew.setSalesDocType("");
					dtSapOrderHeaderNew.setSalesOrganiztion("");
					dtSapOrderHeaderNew.setDistributionChannel("");
					dtSapOrderHeaderNew.setDivision("");
					dtSapOrderHeaderNew.setShippingConditions("");
					
					dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
										
				}
			}
			

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate24(DtSapTransaction dtSapTransaction) {

		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
			/** query origin order type 18 **/
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
				dtSapOrderHeaderNew.setYourReference("/");
				dtSapOrderHeaderNew.setDeliveryBlock("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);

			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return dtSapOrderHeaderNew ;
	}
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate25(DtSapTransaction dtSapTransaction) {

		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
			/** query origin order type 19 **/
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				
				dtSapOrderHeaderNew = new DtSapOrderHeader();				
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
				dtSapOrderHeaderNew.setYourReference("/");
				dtSapOrderHeaderNew.setDeliveryBlock("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
				
			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return dtSapOrderHeaderNew ;

	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder25DeliveryBlock(DtSapTransaction dtSapTransaction) {

		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
			/** query origin order type 19 **/
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 19L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());
				dtSapOrderHeaderNew.setDeliveryBlock("/");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return dtSapOrderHeaderNew ;

	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder24DeliveryBlock(DtSapTransaction dtSapTransaction) {

		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
			/** query origin order type 18 **/
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				
				dtSapOrderHeaderNew = new DtSapOrderHeader();
				dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
				dtSapOrderHeaderNew.setChangeMode("U");
				dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());
				dtSapOrderHeaderNew.setDeliveryBlock("/");
				dtSapOrderHeaderNew.setSalesDocType("");
				dtSapOrderHeaderNew.setSalesOrganiztion("");
				dtSapOrderHeaderNew.setDistributionChannel("");
				dtSapOrderHeaderNew.setDivision("");
				dtSapOrderHeaderNew.setShippingConditions("");
				dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
				dtSapOrderHeaderNew.setCreateValue(masterValue);
				dtSapOrderHeaderNew.setStatus("W");
				dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
				
			}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		return dtSapOrderHeaderNew ;
	}
	
}
