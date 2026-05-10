package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.StdPriceBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerService;
import th.co.ais.dt.entity.sap.DtSapOrderHCon;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHText;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCancel;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapOrderItemSerial;
import th.co.ais.dt.entity.sap.DtSapPostFinalcial;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostMappingTender;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.sk.LocMapPlant;
import th.co.ais.dt.entity.so.PreBooking;
import th.co.ais.dt.entity.so.SaleOrderTransection;
import th.co.ais.dt.entity.util.CmLocationMst;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.pr.IPriceMstDao;
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
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostFinalcialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostMappingTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTaxItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.so.IPreBookingDao;
import th.co.ais.dt.repository.interfaces.util.ICmLocationMstDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.IProductMstDao;
import th.co.ais.dt.service.core.dto.PriceAndProductMstBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryTenderBean;
import th.co.ais.dt.service.core.impl.so.dto.PreBookingFg;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeDepositServiceImpl implements ISapHandleTransactionTypeDepositPartnerService{
	
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao ;
	private final IDtSapPostHeaderDao dtSapPostHeaderDao ;
	private final ISapCallApiService sapCallApiService ;
	private final IDtSapTransactionDao dtSapTransactionDao ;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao ;
	private final IDtSapPostTenderDao dtSapPostTenderDao ;
	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao ;
	private final IDtSapPostFinalcialDao dtSapPostFinalcialDao;
	private final DTConfig dTConfig;
	
	private final IDtSapOrderHConDao dtSapOrderHConDao ;
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderHPartnerFDao dtSapOrderHPartnerFDao;
	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	private final IDtSapOrderHTextDao dtSapOrderHTextDao ;
	
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao ;
	
	private final IPriceMstDao priceMstDao ;
	private final ILovMasterDao lovMasterDao ;
	
	private final ILocMapPlantDao locMapPlantDao ;
	private final ICmLocationMstDao cmLocationMstDao ;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao ;
	private final IPreBookingDao preBookingDao ;
	private final IProductMstDao productMstDao ;
	

	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(TransactionType);

	}

	@Override
	public DtSapPostHeader query6InfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) {
		
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql6
//			List<QueryPostTransactionBean> listQueryPostTransactionBean = dtSapTransactionTypeDao.queryPostTransactionSql6(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql6(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

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
			//dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			String tenderTypeId = null;	

			//TENDER
//			List<QueryTenderBean> tenders = dtSapTransactionTypeDao.queryTenderPostTransactionSql3(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			List<QueryTenderBean> tenders = queryTenderPostTransactionSql3(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			if(BeanUtil.isNotEmpty(tenders)) {
                //for(int i = 0 ; i < tenders.size() ; i ++) {
                    DtSapPostTender t = new DtSapPostTender();
                    t.setTENDERSEQUENCENUMBER("1");
                    
                    if(tenders.get(0).getTENDERSEQUENCENUMBER().equals("99")) {
                        t.setTENDERTYPECODE(tenders.get(0).getTENDERTYPECODE());
                    }else {
                        List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(0).getTENDERTYPECODE(), null) ;
                        if(fDtSapPostMappingTender.size() == 1) {
                            t.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
                        }else {
                            List<DtSapPostMappingTender> sDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(tenders.get(0).getTENDERTYPECODE(), tenders.get(0).getDOCTYPE()) ;
                            t.setTENDERTYPECODE(sDtSapPostMappingTender.get(0).getTenderTypeId());
                        }    
                    }
                    t.setTENDERAMOUNT(tenders.get(0).getTENDERAMOUNT());
                    t.setTENDERCURRENCY(tenders.get(0).getTENDERCURRENCY());
                    t.setCreateValue(masterValue);
                    tenderTypeId = t.getTENDERTYPECODE();
					//set herder id
                    
					t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					dtSapPostTenderDao.insert(t);
				//}
			}
			//END TENDER
			
			//FINALCIAL
//			DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
//			dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//			dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
//			dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
//			dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
////			dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
//			dtSapPostFinalcial.setREFERERENCEID(tenderTypeId);
//			dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
//			dtSapPostFinalcial.setCreateValue(masterValue);
//			dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//			dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
			//END FINALCIAL
			
			List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;

			//ITEM
			//SALES ITEM
			DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
			dtSapPostSalesItems.setRETAILSEQUENCENUMBER("1");
			dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
			dtSapPostSalesItems.setITEMIDQUALIFIER("2") ;
			dtSapPostSalesItems.setITEMID(matDeposit.get(0).getLovVal()) ;
			dtSapPostSalesItems.setRETAILQUANTITY("1");
			dtSapPostSalesItems.setSALESUNITOFMEASURE(matDeposit.get(0).getLovAttribute01());
			dtSapPostSalesItems.setSALESAMOUNT(tenders.get(0).getTENDERAMOUNT()) ;
			dtSapPostSalesItems.setNORMALSALESAMOUNT("0") ;
			
			dtSapPostSalesItems.setSO_NO("");
			dtSapPostSalesItems.setITEM_NO("0020") ;
			dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
			dtSapPostSalesItems.setCreateValue(masterValue);
			dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
			
            DtSapPostTaxItems dtSapPostTaxItemsDeposit = new DtSapPostTaxItems();
            List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			BigDecimal dd = new BigDecimal(dtSapPostSalesItems.getSALESAMOUNT());
			BigDecimal dd1 = new BigDecimal(107);
			BigDecimal dd2 = new BigDecimal("7.00");
			
			BigDecimal dd3 = dd.multiply(dd2);
			BigDecimal dd4 = dd3.divide(dd1,2, RoundingMode.HALF_UP);
			
			dtSapPostTaxItemsDeposit.setTAXAMOUNT(dd4.toString());
			dtSapPostTaxItemsDeposit.setTAXSEQUENCENUMBER("1");
			dtSapPostTaxItemsDeposit.setTAXTYPECODE(taxtCode.get(0).getLovVal());
			
			dtSapPostTaxItemsDeposit.setCreateValue(masterValue);
			dtSapPostTaxItemsDeposit.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
			dtSapPostTaxItemsDao.insert(dtSapPostTaxItemsDeposit);
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	@Override
	public DtSapPostHeader query6InfoAndInsertPostTransactionDepositPartner(DtSapTransaction dtSapTransaction) {
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql6
//			List<QueryPostTransactionBean> listQueryPostTransactionBean = dtSapTransactionTypeDao.queryPostTransactionSql6(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql6(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
			
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			
			List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;
			
			dtSapPostHeader.setRETAILSTOREID(plantSloc.get(0).getLovCode()); 
			
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());  
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			//dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
            List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(listQueryPostTransactionBean.get(0).getREFERERENCEID(), null) ;
						
			//FINALCIAL
			DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
			dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//			dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
			dtSapPostFinalcial.setFITYPECODE(config.getRetailtypecode().split("[|]")[0]);
			dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
			dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
//			dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
			dtSapPostFinalcial.setREFERERENCEID(fDtSapPostMappingTender.get(0).getTenderTypeId());
//			dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
			dtSapPostFinalcial.setCreateValue(masterValue);
			dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
			dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
			//END FINALCIAL
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public DtSapPostHeader query7InfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) {
		
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql7
//			List<QueryPostTransactionBean> listQueryPostTransactionBean = dtSapTransactionTypeDao.queryPostTransactionSql7(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql7(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
					
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;
			dtSapPostHeader.setRETAILSTOREID(plantSloc.get(0).getLovCode()); 
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());  
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
					
					//FINALCIAL
					DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
					dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//					dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
					dtSapPostFinalcial.setFITYPECODE(config.getRetailtypecode().split("[|]")[1]);
					dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
					dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
					dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
					dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
					dtSapPostFinalcial.setCreateValue(masterValue);
					dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

					dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
					//END FINALCIAL
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	@Override
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		// TODO Auto-generated method stub
		DtSapPostHeader res = sapCallApiService.callPostTransactionApi(stSapPostHeader);
		return res ;
	}
	
	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
		// TODO Auto-generated method stub
		try {
			DtSapTransaction dtSapTransactionForUpdate = dtSapTransactionDao.getByLongPrimaryKey(dtSapTransaction.getSapTranId());
			dtSapTransactionForUpdate.setStatus(dtSapTransaction.getStatus());
			dtSapTransactionForUpdate.getCreateValue().setLastUpd(new Date());
			dtSapTransactionDao.update(dtSapTransactionForUpdate);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		// TODO Auto-generated method stub
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
	
	private List<QueryPostTransactionBean> queryPostTransactionSql6(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.queryPostTransactionSql6(docNo, company);
		 }  
		  else { 
			// query from on prem
				return queryPostTransactionSql6OnPrem(docNo, company);
		 }	
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql7(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.queryPostTransactionSql7(docNo, company);
		 }  
		  else { 
			// query from on prem
				return queryPostTransactionSql7OnPrem(docNo, company);
		 }
		
		
		
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql6Cancel(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.queryPostTransactionSql6Cancel(docNo, company);
		 }  
		  else { 
			// query from on prem
				return queryPostTransactionSql6OnPremCancel(docNo, company);
		 }
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql7Cancel(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.queryPostTransactionSql7Cancel(docNo, company);
		 }  
		  else { 
				// query from on prem
				return queryPostTransactionSql7OnPremCancel(docNo, company);
		 }

	}
	
	private List<QueryTenderBean> queryTenderPostTransactionSql3(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return  dtSapTransactionTypeDao.queryTenderPostTransactionSql3(docNo, company);
		 }  
		  else { 
			// query from on prem
				return queryTenderPostTransactionSql3OnPrem(docNo, company);
		 }
		
		
		
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql6OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql6",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	private List<QueryPostTransactionBean> queryPostTransactionSql7OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql7",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	private List<QueryTenderBean> queryTenderPostTransactionSql3OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryTenderPostTransactionSql3",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryTenderBean() ;
   }
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorderDepositshop(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql2
			List<QuerySaleOrderBean> listQuerySaleOrderBean = querySaleOrderSq2(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ,dtSapTransaction.getTransactionType());
			
			if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
			
				// insert header
				
				dtSapOrderHeader.setStatus("W");
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("C");
				dtSapOrderHeader.setSDDocumentCategory("C");
				dtSapOrderHeader.setSalesDocType("ZS09");
				dtSapOrderHeader.setSalesOrganiztion(listQuerySaleOrderBean.get(0).getSalesOrganiztion());
				dtSapOrderHeader.setDistributionChannel(listQuerySaleOrderBean.get(0).getDistributionChannel());
				dtSapOrderHeader.setDivision(listQuerySaleOrderBean.get(0).getDivision());
				dtSapOrderHeader.setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference());
				dtSapOrderHeader.setCustomerPurchaseOrderType(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType());
				dtSapOrderHeader.setShippingConditions(listQuerySaleOrderBean.get(0).getShippingConditions());
				dtSapOrderHeader.setDeliveryBlock("Z4");
				dtSapOrderHeader.setCompleteDlv("X");
				
				dtSapOrderHeader.setPaymentTerms(listQuerySaleOrderBean.get(0).getPaymentTerms());
				dtSapOrderHeader.setDocumentCurrency(listQuerySaleOrderBean.get(0).getDocumentCurrency());
				dtSapOrderHeader.setCustomerGroup(listQuerySaleOrderBean.get(0).getCustomerGroup());
				dtSapOrderHeader.setCreateValue(masterValue);
				
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
			
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getHTextLine())) {
					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHText.setTextID("TX03");
					dtSapOrderHText.setTextLanguage("EN");
					dtSapOrderHText.setTextLine(listQuerySaleOrderBean.get(0).getHTextLine());
					dtSapOrderHText.setCreateValue(masterValue);
					dtSapOrderHTextDao.insert(dtSapOrderHText);
				}
				
				//List<LovMaster> shipToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SHIP_TO_ONETIME", "SAP_S4_SHIP_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;
				List<LovMaster> soldToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", "Z004", dtSapTransaction.getCompany(), null, "Y") ;
				
				DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();				
				dtSapOrderHPartnerFAG.setChangeMode("C");
				dtSapOrderHPartnerFAG.setPartnerFunction("AG");
				dtSapOrderHPartnerFAG.setCustomerNumber(soldToOneTime.get(0).getLovVal());  // one time sold to
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
				List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber()), dtSapTransaction.getCompany()) ;
				dtSapOrderHPartnerFWE.setCustomerNumber(listlocMapPlant.get(0).getPk().getPlantCode());  
				dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
				dtSapOrderHPartnerFWE.setCreateValue(masterValue);
				dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
			
				
				
				
				//for(int i = 0 ; i < listQuerySaleOrderBean.size() ; i ++) {
				//MAIN ITEM
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setChangeMode("C");
					dtSapOrderItem.setSalesOrderDocument(null);
					dtSapOrderItem.setItem(BeanUtil.lPad((0+1) * 10, 4));
					
					dtSapOrderItem.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
					dtSapOrderItem.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
					dtSapOrderItem.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
					dtSapOrderItem.setRequestedDeliveryDate(listQuerySaleOrderBean.get(0).getDeliveryDate());
					
					dtSapOrderItem.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
					dtSapOrderItem.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
					dtSapOrderItem.setBatch(null);
					dtSapOrderItem.setBillingBlock("Z8");
					dtSapOrderItem.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
					dtSapOrderItem.setRoute(null);
					
					
					dtSapOrderItem.setItemCategory("ZRS7");

					
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);
										
					DtSapOrderItemCon dtSapOrderItemConMainPrice = new DtSapOrderItemCon() ;
					dtSapOrderItemConMainPrice.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
					dtSapOrderItemConMainPrice.setChangeMode("C");
					dtSapOrderItemConMainPrice.setConditionType("PN10");
					dtSapOrderItemConMainPrice.setAmount(listQuerySaleOrderBean.get(0).getICAmount());
					dtSapOrderItemConMainPrice.setCurrency("THB");
					dtSapOrderItemConMainPrice.setConditionPricingUnit(null);
					dtSapOrderItemConMainPrice.setConditionUnit(null);
					dtSapOrderItemConMainPrice.setCreateValue(masterValue);
					dtSapOrderItemConDao.insert(dtSapOrderItemConMainPrice);
					
					//DEPOSIT ITEM
					DtSapOrderItem dtSapOrderItemDeposit = new DtSapOrderItem();
					dtSapOrderItemDeposit.setChangeMode("C");
					dtSapOrderItemDeposit.setSalesOrderDocument(null);
					dtSapOrderItemDeposit.setItem(BeanUtil.lPad((1+1) * 10, 4));
					List<LovMaster> matDeposit = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_DEPOSIT", "SAP_S4_MAT_DEPOSIT", dtSapTransaction.getCompany(), null, "Y") ;

					dtSapOrderItemDeposit.setMaterialNumber(matDeposit.get(0).getLovVal()); 
					dtSapOrderItemDeposit.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
					dtSapOrderItemDeposit.setSalesUnit((matDeposit.get(0).getLovAttribute01())); 
					dtSapOrderItemDeposit.setRequestedDeliveryDate(listQuerySaleOrderBean.get(0).getDeliveryDate());
					
					dtSapOrderItemDeposit.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); // need to convert to be plant
					dtSapOrderItemDeposit.setStorageLocation(null);
					dtSapOrderItemDeposit.setBatch(null);
					dtSapOrderItemDeposit.setBillingBlock(null);
					dtSapOrderItemDeposit.setDeliveryPriority(null);
					dtSapOrderItemDeposit.setRoute(null);
					
					
					dtSapOrderItemDeposit.setItemCategory("ZRS3");

					
					dtSapOrderItemDeposit.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItemDeposit.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItemDeposit);
										
					DtSapOrderItemCon dtSapOrderItemConMainPriceDeposit = new DtSapOrderItemCon() ;
					dtSapOrderItemConMainPriceDeposit.setSapOrderItemId(dtSapOrderItemDeposit.getSapOrderItemId());
					dtSapOrderItemConMainPriceDeposit.setChangeMode("C");
					dtSapOrderItemConMainPriceDeposit.setConditionType("PN10");
					dtSapOrderItemConMainPriceDeposit.setAmount(listQuerySaleOrderBean.get(0).getICDAmount());
					dtSapOrderItemConMainPriceDeposit.setCurrency("THB");
					dtSapOrderItemConMainPriceDeposit.setConditionPricingUnit(null);
					dtSapOrderItemConMainPriceDeposit.setConditionUnit(null);
					dtSapOrderItemConMainPriceDeposit.setCreateValue(masterValue);
					dtSapOrderItemConDao.insert(dtSapOrderItemConMainPriceDeposit);
							
				//}
					
					// free goods item
					List<PreBookingFg> listPreBookingFg = preBookingDao.getPreBookingFg(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ) ;
					if(BeanUtil.isNotEmpty(listPreBookingFg)) {
						int itenNo = 3 ;
						for(PreBookingFg el : listPreBookingFg) {
							DtSapOrderItem dtSapOrderItemFg = new DtSapOrderItem();
							dtSapOrderItemFg.setChangeMode("C");
							dtSapOrderItemFg.setSalesOrderDocument(null);
							dtSapOrderItemFg.setItem(BeanUtil.lPad((itenNo) * 10, 4));
							
							dtSapOrderItemFg.setMaterialNumber(el.getMatCode());
							dtSapOrderItemFg.setQuantity(el.getQty());
							
							
							dtSapOrderItemFg.setSalesUnit(productMstDao.getProductMstByUniqueV2(el.getCompany(), el.getMatCode()).getUnitName());
							
							dtSapOrderItemFg.setRequestedDeliveryDate(listQuerySaleOrderBean.get(0).getDeliveryDate());
							
							dtSapOrderItemFg.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
							dtSapOrderItemFg.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
							dtSapOrderItemFg.setBatch(null);
							dtSapOrderItemFg.setBillingBlock("Z8");
							dtSapOrderItemFg.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
							dtSapOrderItemFg.setRoute(null);
							
							
							//dtSapOrderItemFg.setItemCategory("ZRS2");
							dtSapOrderItemFg.setItemCategory("ZRSA");

							
							dtSapOrderItemFg.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
							dtSapOrderItemFg.setCreateValue(masterValue);
							dtSapOrderItemDao.insert(dtSapOrderItemFg);
							itenNo +=1 ;
						}
					}
					
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	private List<QuerySaleOrderBean> querySaleOrderSq2(String docNo , String company,Long transactionType){
		List<QuerySaleOrderBean> res = null ;
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				res=  dtSapTransactionTypeDao.querySaleOrderSql2(docNo, company);
		 }  
		  else { 
			// query from on prem
				res = querySaleOrderSq2OnPrem(docNo, company);
		 }
		
		return res ;
		
	}
	
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
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorderDepositPartner(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql3
			List<QuerySaleOrderBean> listQuerySaleOrderBean = querySaleOrderSq3(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ,dtSapTransaction.getTransactionType());
			
			if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
				
				
				//check checkSpecialCredit
				String itemCat = "TAN";
				String deliveryBlock = "Z8";
				String paymentTerms = "ZM05"; 
				List<Object[]> specialCredit = dtSapTransactionTypeDao.checkSpecialCredit(dtSapTransaction.getCompany(), listQuerySaleOrderBean.get(0).getMaterialNumber(), listQuerySaleOrderBean.get(0).getToLocationCode(),listQuerySaleOrderBean.get(0).getReceipt_dt());
			    if(BeanUtil.isNotEmpty(specialCredit)) {
			    	itemCat = "ZWS1";
					deliveryBlock = "Z9";
					paymentTerms = "Z015"; 
			    }
			    
			    //item cat TAN , payment terms =ZM05
			    //item cat ZWS1  , payment terms =Z015
				
				// insert header
				
				dtSapOrderHeader.setStatus("W");
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("C");
				dtSapOrderHeader.setSDDocumentCategory("C");
				dtSapOrderHeader.setSalesDocType("ZS05");
				dtSapOrderHeader.setSalesOrganiztion(listQuerySaleOrderBean.get(0).getSalesOrganiztion());
				dtSapOrderHeader.setDistributionChannel(listQuerySaleOrderBean.get(0).getDistributionChannel());
				dtSapOrderHeader.setDivision(listQuerySaleOrderBean.get(0).getDivision());
				dtSapOrderHeader.setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference() +"_"+ String.valueOf(dtSapTransaction.getSapTranId()));
				dtSapOrderHeader.setCustomerPurchaseOrderType(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType());
				dtSapOrderHeader.setShippingConditions(listQuerySaleOrderBean.get(0).getShippingConditions());
				dtSapOrderHeader.setDeliveryBlock(deliveryBlock);  
				dtSapOrderHeader.setOrderCombination("X");
				
				//dtSapOrderHeader.setPaymentTerms(listQuerySaleOrderBean.get(0).getPaymentTerms());
				dtSapOrderHeader.setPaymentTerms(paymentTerms);
				dtSapOrderHeader.setDocumentCurrency(listQuerySaleOrderBean.get(0).getDocumentCurrency());
				dtSapOrderHeader.setCustomerGroup(null);
				dtSapOrderHeader.setCreateValue(masterValue);
				
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
			
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
//				if(BeanUtil.isNotEmpty(listQuerySaleOrderBean.get(0).getHTextLine())) {
//					DtSapOrderHText dtSapOrderHText = new DtSapOrderHText();
//					dtSapOrderHText.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
//					dtSapOrderHText.setTextID("TX03");
//					dtSapOrderHText.setTextLanguage("EN");
//					dtSapOrderHText.setTextLine(listQuerySaleOrderBean.get(0).getHTextLine());
//					dtSapOrderHText.setCreateValue(masterValue);
//					dtSapOrderHTextDao.insert(dtSapOrderHText);
//				}
				
				// location head
				CmLocationMst cm = cmLocationMstDao.getLocationMstByKey(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSCustomerNumber()));
//				String locationHead = "";
				String plantlocationHead = "";
//				
//				List<CmLocationMst> listCmLocationMst = cmLocationMstDao.getLocationMstByDealerCode(cm.getDealer_code(),"Y") ;
//				locationHead = listCmLocationMst.get(0).getLocation_code().toString(); 
				
				List<LocMapPlant> listlocMapPlantH = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSCustomerNumber()), dtSapTransaction.getCompany()) ;
				plantlocationHead = listlocMapPlantH.get(0).getPk().getPlantCode();
				
				if(BeanUtil.isNotEmpty(cm.getPattern()) && cm.getPattern().equals("2")) {
					DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();				
					dtSapOrderHPartnerFAG.setChangeMode("C");
					dtSapOrderHPartnerFAG.setPartnerFunction("AG");
					dtSapOrderHPartnerFAG.setCustomerNumber(plantlocationHead);   
					dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFAG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFWE.setChangeMode("C");
					dtSapOrderHPartnerFWE.setPartnerFunction("WE");
					dtSapOrderHPartnerFWE.setCustomerNumber(plantlocationHead);  
					dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFWE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
				}else if(BeanUtil.isNotEmpty(cm.getPattern()) && cm.getPattern().equals("3")) {
					DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();				
					dtSapOrderHPartnerFAG.setChangeMode("C");
					dtSapOrderHPartnerFAG.setPartnerFunction("AG");
					dtSapOrderHPartnerFAG.setCustomerNumber(plantlocationHead);   
					dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFAG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFWE.setChangeMode("C");
					dtSapOrderHPartnerFWE.setPartnerFunction("WE");
					dtSapOrderHPartnerFWE.setCustomerNumber(cm.getAwn_code());  
					dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFWE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFRG = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFRG.setChangeMode("C");
					dtSapOrderHPartnerFRG.setPartnerFunction("RG");
					dtSapOrderHPartnerFRG.setCustomerNumber(cm.getBillTo());  
					dtSapOrderHPartnerFRG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFRG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFRG);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFRE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFRE.setChangeMode("C");
					dtSapOrderHPartnerFRE.setPartnerFunction("RE");
					dtSapOrderHPartnerFRE.setCustomerNumber(cm.getBillTo());  
					dtSapOrderHPartnerFRE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFRE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFRE);
				}else {
					DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();				
					dtSapOrderHPartnerFAG.setChangeMode("C");
					dtSapOrderHPartnerFAG.setPartnerFunction("AG");
					dtSapOrderHPartnerFAG.setCustomerNumber(plantlocationHead);   
					dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFAG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFWE.setChangeMode("C");
					dtSapOrderHPartnerFWE.setPartnerFunction("WE");
					dtSapOrderHPartnerFWE.setCustomerNumber(cm.getAwn_code());  
					dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFWE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
				}
				
				
				
			
				
				
				
				//for(int i = 0 ; i < listQuerySaleOrderBean.size() ; i ++) {
				//MAIN ITEM
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setChangeMode("C");
					dtSapOrderItem.setSalesOrderDocument(null);
					dtSapOrderItem.setItem(BeanUtil.lPad((0+1) * 10, 4));
					
					dtSapOrderItem.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
					dtSapOrderItem.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
					dtSapOrderItem.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
					dtSapOrderItem.setRequestedDeliveryDate(listQuerySaleOrderBean.get(0).getDeliveryDate());
					
					List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;
					

					dtSapOrderItem.setPlant(plantSloc.get(0).getLovCode());  
					dtSapOrderItem.setStorageLocation(plantSloc.get(0).getLovVal());
	
					dtSapOrderItem.setBatch(null);
					dtSapOrderItem.setBillingBlock(null);
					dtSapOrderItem.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
					dtSapOrderItem.setRoute(null);
					
					
					dtSapOrderItem.setItemCategory(itemCat); 
					//"ZWS2 (Credit Active)
					// ZWS1 (Special Credit)"


					
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);
										
//					DtSapOrderItemCon dtSapOrderItemConMainPrice = new DtSapOrderItemCon() ;
//					dtSapOrderItemConMainPrice.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
//					dtSapOrderItemConMainPrice.setChangeMode("C");
//					dtSapOrderItemConMainPrice.setConditionType("PN10");
//					
//					//find price wh  TODO
//					List<StdPriceBean> price = getListPriceByCriteria(Long.valueOf(listQuerySaleOrderBean.get(0).getProduct_id()), "EUP", listQuerySaleOrderBean.get(0).getReceipt_dt(), "CASH");
//					
//					if(BeanUtil.isNotEmpty(price)) {
//						dtSapOrderItemConMainPrice.setAmount(price.get(0).getIncVat()); // TOFDD
//					}
//					
//					
//					
//					dtSapOrderItemConMainPrice.setCurrency("THB");
//					dtSapOrderItemConMainPrice.setConditionPricingUnit(listQuerySaleOrderBean.get(0).getQuantity());
//					dtSapOrderItemConMainPrice.setConditionUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
//					dtSapOrderItemConMainPrice.setCreateValue(masterValue);
//					dtSapOrderItemConDao.insert(dtSapOrderItemConMainPrice);
					
//					//DEPOSIT ITEM
//					DtSapOrderItem dtSapOrderItemDeposit = new DtSapOrderItem();
//					dtSapOrderItemDeposit.setChangeMode("C");
//					dtSapOrderItemDeposit.setSalesOrderDocument(null);
//					dtSapOrderItemDeposit.setItem(BeanUtil.lPad((1+1) * 10, 4));
//					
//					dtSapOrderItemDeposit.setMaterialNumber("1000000999"); //TODO mat deposit to config
//					dtSapOrderItemDeposit.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
//					dtSapOrderItemDeposit.setSalesUnit("PC"); //TODO mat deposit to config
//					dtSapOrderItemDeposit.setRequestedDeliveryDate(listQuerySaleOrderBean.get(0).getReceipt_dt());
//					
//					dtSapOrderItemDeposit.setPlant(listQuerySaleOrderBean.get(0).getPlant()); // need to convert to be plant
//					dtSapOrderItemDeposit.setStorageLocation(null);
//					dtSapOrderItemDeposit.setBatch(null);
//					dtSapOrderItemDeposit.setBillingBlock(null);
//					dtSapOrderItemDeposit.setDeliveryPriority(null);
//					dtSapOrderItemDeposit.setRoute(null);
//					
//					
//					dtSapOrderItemDeposit.setItemCategory("ZRS3");
//
//					
//					dtSapOrderItemDeposit.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
//					dtSapOrderItemDeposit.setCreateValue(masterValue);
//					dtSapOrderItemDao.insert(dtSapOrderItemDeposit);
//										
//					DtSapOrderItemCon dtSapOrderItemConMainPriceDeposit = new DtSapOrderItemCon() ;
//					dtSapOrderItemConMainPriceDeposit.setSapOrderItemId(dtSapOrderItemDeposit.getSapOrderItemId());
//					dtSapOrderItemConMainPriceDeposit.setChangeMode("C");
//					dtSapOrderItemConMainPriceDeposit.setConditionType("PN10");
//					dtSapOrderItemConMainPriceDeposit.setAmount(listQuerySaleOrderBean.get(0).getICDAmount());
//					dtSapOrderItemConMainPriceDeposit.setCurrency("THB");
//					dtSapOrderItemConMainPriceDeposit.setConditionPricingUnit(null);
//					dtSapOrderItemConMainPriceDeposit.setConditionUnit(null);
//					dtSapOrderItemConMainPriceDeposit.setCreateValue(masterValue);
//					dtSapOrderItemConDao.insert(dtSapOrderItemConMainPriceDeposit);
							
				//}
					
					// free goods item
					List<PreBookingFg> listPreBookingFg = preBookingDao.getPreBookingFg(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ) ;
					if(BeanUtil.isNotEmpty(listPreBookingFg)) {
						int itenNo = 2 ;
						for(PreBookingFg el : listPreBookingFg) {
							DtSapOrderItem dtSapOrderItemfg = new DtSapOrderItem();
							dtSapOrderItemfg.setChangeMode("C");
							dtSapOrderItemfg.setSalesOrderDocument(null);
							dtSapOrderItemfg.setItem(BeanUtil.lPad((itenNo) * 10, 4));
							
							dtSapOrderItemfg.setMaterialNumber(el.getMatCode());
							dtSapOrderItemfg.setQuantity(el.getQty());
							
							dtSapOrderItemfg.setSalesUnit(productMstDao.getProductMstByUniqueV2(el.getCompany(), el.getMatCode()).getUnitName());
							
							dtSapOrderItemfg.setRequestedDeliveryDate(listQuerySaleOrderBean.get(0).getDeliveryDate());
							
							//List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;
							

							dtSapOrderItemfg.setPlant(plantSloc.get(0).getLovCode());  
							dtSapOrderItemfg.setStorageLocation(plantSloc.get(0).getLovVal());
			
							dtSapOrderItemfg.setBatch(null);
							dtSapOrderItemfg.setBillingBlock(null);
							dtSapOrderItemfg.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
							dtSapOrderItemfg.setRoute(null);
							
							dtSapOrderItemfg.setItemCategory("TANN"); 
							
							dtSapOrderItemfg.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
							dtSapOrderItemfg.setCreateValue(masterValue);
							dtSapOrderItemDao.insert(dtSapOrderItemfg);
							itenNo +=1 ;
						}
					}
					
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	public List<QuerySaleOrderBean> querySaleOrderSq3(String docNo , String company,Long transactionType){
		List<QuerySaleOrderBean> res = null ;
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				res=  dtSapTransactionTypeDao.querySaleOrderSql3(docNo, company);
		 }  
		  else { 
			// query from on prem
				res =  querySaleOrderSq3OnPrem(docNo, company);
		 }
		
		
		
		
		return res ;
		
	}
	
	public List<StdPriceBean> getListPriceByCriteria(long productId , String groupPrice , String priceDate  , String paymentMethod){
		List<StdPriceBean> res = new ArrayList<>();
		PriceAndProductMstBean el =priceMstDao.getPriceMstByCriteria(productId, groupPrice, priceDate, paymentMethod);
		if(el != null) {
				
			StdPriceBean priceBean2 = new StdPriceBean(); 
			priceBean2.setPriceId(el.getPrice_id().toString());
			priceBean2.setPriceGroup(el.getPrice_group());
			priceBean2.setIncVat( (el.getInc_vat() != null ? el.getInc_vat().toString() : "0" ) );
			priceBean2.setVatAmt((el.getVat_amt() != null ? el.getVat_amt().toString() : "0" ));
			priceBean2.setVatRate(el.getVat_rate());
			priceBean2.setExcVat((el.getExc_vat() != null ? el.getExc_vat().toString() : "0"));
			//priceBean2.setPriceDescription(lovMasterDao.getLovMasterByUnique("PRICE_GROUP", null, groupPrice).getLovVal());
			priceBean2.setCompany(el.getCompany());
			priceBean2.setMatCode(el.getMat_code());
			priceBean2.setPriceVatType(el.getVat_type());
			res.add(priceBean2);

		}
		
		return res ;
		
	}
	
	private List<QuerySaleOrderBean> querySaleOrderSq2OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/querySaleOrderSql2",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQuerySaleOrderBean() ;
   }
	
	private List<QuerySaleOrderBean> querySaleOrderSq3OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/querySaleOrderSql3",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQuerySaleOrderBean() ;
   }

	@Override
	public DtSapOrderHeader queryInfoAndUpdateSaleorderDepositPartner(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql saleOrder
//			String receiptNum = getReceiptNumFromCnNum(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo());
//			List<QuerySaleOrderBean> listQuerySaleOrderBean = dtSapTransactionTypeDao.querySaleOrderSqlCancelSaleNormal(dtSapTransaction.getCompany(), receiptNum, Arrays.asList(40L)); 
			//List<QuerySaleOrderBean> listQuerySaleOrderBean = dtSapTransactionTypeDao.querySaleOrderSqlCancelSaleNormal(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo(), Arrays.asList(40L)); 
            
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 40L) ;
            DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());

            
			if(BeanUtil.isNotEmpty(orderHeaderOld)) {
				
				// insert header
				dtSapOrderHeader.setStatus("W");
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("U");
				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHeader.setSalesDocType("");
				dtSapOrderHeader.setSalesOrganiztion("");
				dtSapOrderHeader.setDistributionChannel("");
				dtSapOrderHeader.setDivision("");
				dtSapOrderHeader.setShippingConditions("");
				//dtSapOrderHeader.setCustomerReference(orderHeaderOld.getCustomerReference()+ "C"+ String.valueOf(orderHeaderOld.getSapOrderHeaderId()) );
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
			
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				for (DtSapOrderItem el : listOrderItemOld) {
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setChangeMode("U");
					dtSapOrderItem.setSalesOrderDocument(null);
					dtSapOrderItem.setItem(el.getItem());
					dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
					dtSapOrderItem.setQuantity("");	
					dtSapOrderItem.setSalesUnit("");
					dtSapOrderItem.setItemCategory("");					
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);
					
					DtSapOrderItemCancel dtSapOrderItemCancel = new DtSapOrderItemCancel();
					dtSapOrderItemCancel.setChangeMode("U");
					dtSapOrderItemCancel.setReasonforRejection("70");
					dtSapOrderItemCancel.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
					dtSapOrderItemCancel.setCreateValue(masterValue);
					dtSapOrderItemCancelDao.insert(dtSapOrderItemCancel);
				}
										
					
					
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	private String getReceiptNumFromCnNum(String company, String docNo){
		String receiptNum = null;
		// query from postgres ,
//		CreditNote creditNote = creditNoteDao.getCreditNoteByCnNum(docNo, company);
//		receiptNum = creditNote.getReceiptNum();
		
		// query from on prem
		receiptNum =  getReceiptNumFromCnNumOnPrem(docNo, company);

		return receiptNum;
	}
	
	private String getReceiptNumFromCnNumOnPrem(String company, String docNo) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/getReceiptNumFromCnNum",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getDocNo() ;
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndUpdateSaleorderDepositshopType13(DtSapTransaction dtSapTransaction) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			

//			String  refNo = queryRefNoReceipt(dtSapTransaction.getDocNo(),dtSapTransaction.getCompany());
			// query sql2
			List<QuerySaleOrderBean> listQuerySaleOrderBean = querySaleOrderSq2(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ,dtSapTransaction.getTransactionType());

			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 12L) ;
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			List<DtSapOrderHPartnerF> listPartnerFOld	= dtSapOrderHPartnerFDao.queryDtSapOrderHPartnerFBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
			
				// insert header
				
				dtSapOrderHeader.setStatus("W");
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("U");
//				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getSalesOrderDocument());
				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getRes_SalesOrderDocument());
//				dtSapOrderHeader.setSDDocumentCategory("C");
				dtSapOrderHeader.setSalesDocType("");
				dtSapOrderHeader.setSalesOrganiztion("");
				dtSapOrderHeader.setDistributionChannel("");
				dtSapOrderHeader.setDivision("");
				dtSapOrderHeader.setShippingConditions("");
//				dtSapOrderHeader.setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference());
//				dtSapOrderHeader.setCustomerPurchaseOrderType(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType());

//				dtSapOrderHeader.setDeliveryBlock("Z4");
//				
//				dtSapOrderHeader.setPaymentTerms(listQuerySaleOrderBean.get(0).getPaymentTerms());
//				dtSapOrderHeader.setDocumentCurrency(listQuerySaleOrderBean.get(0).getDocumentCurrency());
//				dtSapOrderHeader.setCustomerGroup(listQuerySaleOrderBean.get(0).getCustomerGroup());
				dtSapOrderHeader.setCreateValue(masterValue);
				
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				
				DtSapOrderHPartnerF dtSapHPartnerFOld = null;
				for (DtSapOrderHPartnerF PartnerFOld : listPartnerFOld) {
		            if (PartnerFOld.getPartnerFunction().equals("WE")) {
		            	dtSapHPartnerFOld = PartnerFOld;
		                break;  
		            }
		        }
				
				boolean changeLocation = false ;
				
				List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber()), dtSapTransaction.getCompany()) ;
				
				if(dtSapHPartnerFOld != null && !dtSapHPartnerFOld.getCustomerNumber().equals(listlocMapPlant.get(0).getPk().getPlantCode())) {
				//if(dtSapHPartnerFOld != null && dtSapHPartnerFOld.getCustomerNumber() != listlocMapPlant.get(0).getPk().getPlantCode()) {
					changeLocation = true ;
					DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFWE.setChangeMode("U");
					dtSapOrderHPartnerFWE.setPartnerFunction("WE");
					
					//List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber()), dtSapTransaction.getCompany()) ;

					dtSapOrderHPartnerFWE.setCustomerNumber(listlocMapPlant.get(0).getPk().getPlantCode());  
					dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFWE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
					
					dtSapHPartnerFOld.setCustomerNumber(listlocMapPlant.get(0).getPk().getPlantCode());
					dtSapOrderHPartnerFDao.updateSensitiveData(dtSapHPartnerFOld);
				}
				
				//for(int i = 0 ; i < listQuerySaleOrderBean.size() ; i ++) {
				//MAIN ITEM
				DtSapOrderItem dtSapOrderItemOld = null;
				DtSapOrderItem dtSapOrderItemOldDeposit = null;
				for (DtSapOrderItem orderItemOld : listOrderItemOld) {
		            if (orderItemOld.getItem().equals("0010")) {
		            	dtSapOrderItemOld = orderItemOld;
		            }
		            
		            if (orderItemOld.getItem().equals("0020")) {
		            	dtSapOrderItemOldDeposit = orderItemOld;
		            }
		        }
								
				if(changeLocation || (dtSapOrderItemOld != null && !dtSapOrderItemOld.getMaterialNumber().equals(listQuerySaleOrderBean.get(0).getMaterialNumber()))) {
				//if(changeLocation || (dtSapOrderItemOld != null && dtSapOrderItemOld.getMaterialNumber() != listQuerySaleOrderBean.get(0).getMaterialNumber())) {	
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setChangeMode("U");
					dtSapOrderItem.setSalesOrderDocument(null);
					dtSapOrderItem.setItem(BeanUtil.lPad((0+1) * 10, 4));
					
					dtSapOrderItem.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
					dtSapOrderItem.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
					dtSapOrderItem.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
					dtSapOrderItem.setRequestedDeliveryDate(LocalDateTime.now().format(formatter));
					
					//List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getPlant()), dtSapTransaction.getCompany()) ;

					dtSapOrderItem.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
					dtSapOrderItem.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
					dtSapOrderItem.setBatch(null);
					dtSapOrderItem.setBillingBlock("Z8");
					dtSapOrderItem.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
					dtSapOrderItem.setRoute(null);
					
					
					dtSapOrderItem.setItemCategory("ZRS7");
					
					
					DtSapOrderItem dtSapOrderItemDeposit = new DtSapOrderItem();
					dtSapOrderItemDeposit.setChangeMode("U");
					dtSapOrderItemDeposit.setSalesOrderDocument(null);
					dtSapOrderItemDeposit.setItem(dtSapOrderItemOldDeposit.getItem());
					dtSapOrderItemDeposit.setMaterialNumber(dtSapOrderItemOldDeposit.getMaterialNumber()); 
					dtSapOrderItemDeposit.setQuantity(dtSapOrderItemOldDeposit.getQuantity());
					dtSapOrderItemDeposit.setSalesUnit(dtSapOrderItemOldDeposit.getSalesUnit()); 
					dtSapOrderItemDeposit.setRequestedDeliveryDate(dtSapOrderItemOldDeposit.getRequestedDeliveryDate());
					
					dtSapOrderItemDeposit.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); // need to convert to be plant
					dtSapOrderItemDeposit.setStorageLocation(null);
					dtSapOrderItemDeposit.setBatch(null);
					dtSapOrderItemDeposit.setBillingBlock(null);
					dtSapOrderItemDeposit.setDeliveryPriority(null);
					dtSapOrderItemDeposit.setRoute(null);
					
					
					dtSapOrderItemDeposit.setItemCategory(dtSapOrderItemOldDeposit.getItemCategory());

					
					dtSapOrderItemDeposit.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItemDeposit.setCreateValue(masterValue);
					
					
					//update old
					dtSapOrderItemOldDeposit.setPlant(listlocMapPlant.get(0).getPk().getPlantCode());
					dtSapOrderItemDao.update(dtSapOrderItemOldDeposit);
					
					dtSapOrderItemOld.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
					dtSapOrderItemOld.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
					dtSapOrderItemOld.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
					dtSapOrderItemOld.setRequestedDeliveryDate(LocalDateTime.now().format(formatter));
					dtSapOrderItemOld.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
					dtSapOrderItemOld.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
					dtSapOrderItemOld.setBatch(null);
					dtSapOrderItemOld.setBillingBlock("Z8");
					dtSapOrderItemOld.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
					dtSapOrderItemOld.setRoute(null);
					dtSapOrderItemOld.setItemCategory("ZRS7");
					dtSapOrderItemDao.update(dtSapOrderItemOld);
					//===============
					
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);
					dtSapOrderItemDao.insert(dtSapOrderItemDeposit);
					
					DtSapOrderItemCon dtSapOrderItemConMainPrice = new DtSapOrderItemCon() ;
					dtSapOrderItemConMainPrice.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
					dtSapOrderItemConMainPrice.setChangeMode("U");
					dtSapOrderItemConMainPrice.setConditionType("PN10");
					dtSapOrderItemConMainPrice.setAmount(listQuerySaleOrderBean.get(0).getICAmount());
					dtSapOrderItemConMainPrice.setCurrency("THB");
					dtSapOrderItemConMainPrice.setConditionPricingUnit(null);
					dtSapOrderItemConMainPrice.setConditionUnit(null);
					dtSapOrderItemConMainPrice.setCreateValue(masterValue);
					dtSapOrderItemConDao.insert(dtSapOrderItemConMainPrice);
				}
				
				// free goods
				if(changeLocation) {
					for (DtSapOrderItem orderItemOld : listOrderItemOld) {
						if( !orderItemOld.getItem().equals("0010") && !orderItemOld.getItem().equals("0020") ) {
							//DtSapOrderItem dtSapOrderItemOldFg =  orderItemOld ;
							
							DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
							dtSapOrderItem.setChangeMode("U");
							dtSapOrderItem.setSalesOrderDocument(null);
							dtSapOrderItem.setItem(orderItemOld.getItem());
							
							dtSapOrderItem.setMaterialNumber(orderItemOld.getMaterialNumber());
							dtSapOrderItem.setQuantity(orderItemOld.getQuantity());
							dtSapOrderItem.setSalesUnit(orderItemOld.getSalesUnit());
							dtSapOrderItem.setRequestedDeliveryDate(LocalDateTime.now().format(formatter));
							
							//List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getPlant()), dtSapTransaction.getCompany()) ;

							dtSapOrderItem.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
							dtSapOrderItem.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
							dtSapOrderItem.setBatch(null);
							dtSapOrderItem.setBillingBlock("Z8");
							dtSapOrderItem.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
							dtSapOrderItem.setRoute(null);
							
							
							dtSapOrderItem.setItemCategory("ZRS2");
							
							
							//orderItemOld.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
							//orderItemOld.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
							//orderItemOld.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
							orderItemOld.setRequestedDeliveryDate(LocalDateTime.now().format(formatter));
							orderItemOld.setPlant(listlocMapPlant.get(0).getPk().getPlantCode()); 
							orderItemOld.setStorageLocation(listlocMapPlant.get(0).getSapStorageCode());
							orderItemOld.setBatch(null);
							orderItemOld.setBillingBlock("Z8");
							orderItemOld.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
							orderItemOld.setRoute(null);
							orderItemOld.setItemCategory("ZRS2");
							dtSapOrderItemDao.update(orderItemOld);
							
							dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
							dtSapOrderItem.setCreateValue(masterValue);
							dtSapOrderItemDao.insert(dtSapOrderItem);
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
	
	public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) {
		List<DtSapPostSalesItems> listDtSapPostSalesItems = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderIdForUpdate(stSapPostHeader.getSapPostHeaderId())  ;
		for(DtSapPostSalesItems i : listDtSapPostSalesItems) {  // size = 1 only
			i.setSO_NO(dtSapOrderHeader.getRes_SalesOrderDocument());
			dtSapPostSalesItemsDao.update(i);
		}
	}

	@Override
	public DtSapPostHeader query6InfoAndInsertPostTransactionCancelDepositPartner(DtSapTransaction dtSapTransaction) {
		
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql6
//			List<QueryPostTransactionBean> listQueryPostTransactionBean = dtSapTransactionTypeDao.queryPostTransactionSql6(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql6Cancel(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
					
			Date today = Calendar.getInstance().getTime();        
			String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(today);
			
			String BUSINESSDAYDATE = new SimpleDateFormat("YYYY-MM-dd",Locale.US).format(today);
			
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;
			dtSapPostHeader.setRETAILSTOREID(plantSloc.get(0).getLovCode()); 
			dtSapPostHeader.setBUSINESSDAYDATE(BUSINESSDAYDATE);  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER()+"_CC");  
			dtSapPostHeader.setBEGINDATETIMESTAMP(todayAsString);  
			dtSapPostHeader.setENDDATETIMESTAMP(todayAsString);  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
            List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender(listQueryPostTransactionBean.get(0).getREFERERENCEID(), null) ;

					//FINALCIAL
					DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
					dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//					dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
					dtSapPostFinalcial.setFITYPECODE(config.getRetailtypecode().split("[|]")[0]);
					dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
					dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
//					dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
					dtSapPostFinalcial.setREFERERENCEID(fDtSapPostMappingTender.get(0).getTenderTypeId());
//					dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
					dtSapPostFinalcial.setCreateValue(masterValue);
					dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

					dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
					//END FINALCIAL
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}


	
	}

	@Override
	public DtSapPostHeader query7InfoAndInsertPostTransactionCancelDepositPartner(DtSapTransaction dtSapTransaction ) {
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql7
//			List<QueryPostTransactionBean> listQueryPostTransactionBean = dtSapTransactionTypeDao.queryPostTransactionSql7(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql7Cancel(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
					
			Date today = Calendar.getInstance().getTime();        
			String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(today);
			String BUSINESSDAYDATE = new SimpleDateFormat("YYYY-MM-dd",Locale.US).format(today);
			
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;

			dtSapPostHeader.setRETAILSTOREID(plantSloc.get(0).getLovCode()); 
			dtSapPostHeader.setBUSINESSDAYDATE(BUSINESSDAYDATE);  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(todayAsString);  
			dtSapPostHeader.setENDDATETIMESTAMP(todayAsString);  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
					
					//FINALCIAL
					DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
					dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//					dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
					dtSapPostFinalcial.setFITYPECODE(config.getRetailtypecode().split("[|]")[1]);
					dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
					dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
//					dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
					dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
					dtSapPostFinalcial.setCreateValue(masterValue);
					dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

					dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
					//END FINALCIAL
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorderDepositPartnerUpdateOrder(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query origin order type 40
			String docNoOri = "" ;
			List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
			 if(BeanUtil.isEmpty(config)) {
				  docNoOri = queryOriginDocNo(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany()) ;
			 }  
			  else { 
				  docNoOri = queryOriginDocNoOnPrem(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany()) ;
			 }
			
			
			
			List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(docNoOri, dtSapTransaction.getCompany() , 40L) ;
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				
				//query header
				List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
				
				// query sql3
				List<QuerySaleOrderBean> listQuerySaleOrderBean = querySaleOrderSq3(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ,dtSapTransaction.getTransactionType());
				
				if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
					
					
					//check checkSpecialCredit
					String itemCat = "TAN";
					String deliveryBlock = "";
					List<Object[]> specialCredit = dtSapTransactionTypeDao.checkSpecialCredit(dtSapTransaction.getCompany(), listQuerySaleOrderBean.get(0).getMaterialNumber(), listQuerySaleOrderBean.get(0).getToLocationCode(),listQuerySaleOrderBean.get(0).getReceipt_dt());
				    if(BeanUtil.isNotEmpty(specialCredit)) {
				    	itemCat = "ZWS1";
						deliveryBlock = "Z2";
				    }
					
					// insert header
					
				    dtSapOrderHeader.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());			    
					dtSapOrderHeader.setStatus("W");
					dtSapOrderHeader.setPartnerName("DT");
					dtSapOrderHeader.setChangeMode("U");
					dtSapOrderHeader.setSDDocumentCategory("C");
					dtSapOrderHeader.setSalesDocType("ZS05");
					dtSapOrderHeader.setSalesOrganiztion(listQuerySaleOrderBean.get(0).getSalesOrganiztion());
					dtSapOrderHeader.setDistributionChannel(listQuerySaleOrderBean.get(0).getDistributionChannel());
					dtSapOrderHeader.setDivision(listQuerySaleOrderBean.get(0).getDivision());
					dtSapOrderHeader.setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference());
					dtSapOrderHeader.setCustomerPurchaseOrderType(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType());
					dtSapOrderHeader.setShippingConditions(listQuerySaleOrderBean.get(0).getShippingConditions());
					dtSapOrderHeader.setDeliveryBlock(deliveryBlock);   
					
					//dtSapOrderHeader.setPaymentTerms(listQuerySaleOrderBean.get(0).getPaymentTerms());
					dtSapOrderHeader.setPaymentTerms("Z015");
					dtSapOrderHeader.setDocumentCurrency(listQuerySaleOrderBean.get(0).getDocumentCurrency());
					dtSapOrderHeader.setCustomerGroup(null);
					dtSapOrderHeader.setCreateValue(masterValue);
					
					dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				
					dtSapOrderHeaderDao.insert(dtSapOrderHeader);
					
					// update old dtSapOrderHeader
			    
					listDtSapOrderHeader.get(0).setSalesOrganiztion(listQuerySaleOrderBean.get(0).getSalesOrganiztion());
					listDtSapOrderHeader.get(0).setDistributionChannel(listQuerySaleOrderBean.get(0).getDistributionChannel());
					listDtSapOrderHeader.get(0).setDivision(listQuerySaleOrderBean.get(0).getDivision());
					listDtSapOrderHeader.get(0).setCustomerReference(listQuerySaleOrderBean.get(0).getCustomerReference());
					listDtSapOrderHeader.get(0).setCustomerPurchaseOrderType(listQuerySaleOrderBean.get(0).getCustomerPurchaseOrderType());
					listDtSapOrderHeader.get(0).setShippingConditions(listQuerySaleOrderBean.get(0).getShippingConditions());
					listDtSapOrderHeader.get(0).setDeliveryBlock(deliveryBlock);   
					listDtSapOrderHeader.get(0).setDocumentCurrency(listQuerySaleOrderBean.get(0).getDocumentCurrency());
					listDtSapOrderHeader.get(0).getCreateValue().setLastUpd(date);
					listDtSapOrderHeader.get(0).getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
					dtSapOrderHeaderDao.update(listDtSapOrderHeader.get(0));
					
					
					//query order  function old
					List<DtSapOrderHPartnerF> listDtSapOrderHPartnerF = dtSapOrderHPartnerFDao.queryDtSapOrderHPartnerFBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId());
					
					
					// location head
					CmLocationMst cm = cmLocationMstDao.getLocationMstByKey(Long.valueOf(listQuerySaleOrderBean.get(0).getToLocationCode()));
					//String locationHead = "";
					String plantlocationHead = "";
					
					
//					List<CmLocationMst> listCmLocationMst = cmLocationMstDao.getLocationMstByDealerCode(cm.getDealer_code(),"Y") ;
//					locationHead = listCmLocationMst.get(0).getLocation_code().toString(); 
					
					List<LocMapPlant> listlocMapPlantH = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getToLocationCode()), dtSapTransaction.getCompany()) ;
					plantlocationHead = listlocMapPlantH.get(0).getPk().getPlantCode();
					
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();				
					dtSapOrderHPartnerFAG.setChangeMode("U");
					dtSapOrderHPartnerFAG.setPartnerFunction("AG");
					//dtSapOrderHPartnerFAG.setCustomerNumber(listQuerySaleOrderBean.get(0).getHFSCustomerNumber());   
					dtSapOrderHPartnerFAG.setCustomerNumber(plantlocationHead); 
					dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFAG.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
					
					DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
					dtSapOrderHPartnerFWE.setChangeMode("U");
					dtSapOrderHPartnerFWE.setPartnerFunction("WE");
					//dtSapOrderHPartnerFWE.setCustomerNumber(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber());  
					dtSapOrderHPartnerFWE.setCustomerNumber(cm.getAwn_code());  
					dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderHPartnerFWE.setCreateValue(masterValue);
					dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
					
					
					// update old F
					for(DtSapOrderHPartnerF fel : listDtSapOrderHPartnerF) {
						if(fel.getPartnerFunction().equals("AG")) {
							fel.setCustomerNumber(listQuerySaleOrderBean.get(0).getHFSCustomerNumber());
							fel.getCreateValue().setLastUpd(date);
							fel.getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
							dtSapOrderHPartnerFDao.updateSensitiveData(fel);
						}else {
							fel.setCustomerNumber(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber());
							fel.getCreateValue().setLastUpd(date);
							fel.getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
							dtSapOrderHPartnerFDao.updateSensitiveData(fel);
						}
					}
				
					
					//query order  item old
					List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
					
					//for(int i = 0 ; i < listQuerySaleOrderBean.size() ; i ++) {
					//MAIN ITEM
						DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
						dtSapOrderItem.setChangeMode("U");
						dtSapOrderItem.setSalesOrderDocument(null);
						dtSapOrderItem.setItem(BeanUtil.lPad((0+1) * 10, 4));
						
						dtSapOrderItem.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
						dtSapOrderItem.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
						dtSapOrderItem.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
						dtSapOrderItem.setRequestedDeliveryDate(null);
						
						
						List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;
						dtSapOrderItem.setPlant(plantSloc.get(0).getLovCode());   
						
						
						dtSapOrderItem.setStorageLocation(plantSloc.get(0).getLovVal());
		
						dtSapOrderItem.setBatch(null);
						dtSapOrderItem.setBillingBlock(null);
						dtSapOrderItem.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
						dtSapOrderItem.setRoute(null);
						
						
						dtSapOrderItem.setItemCategory(itemCat); 
						//"ZWS2 (Credit Active)
						// ZWS1 (Special Credit)"


						
						dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
						dtSapOrderItem.setCreateValue(masterValue);
						dtSapOrderItemDao.insert(dtSapOrderItem);
						
						
						// update old item						
						listDtSapOrderItem.get(0).setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
						listDtSapOrderItem.get(0).setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
						listDtSapOrderItem.get(0).setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());						
						//listDtSapOrderItem.get(0).setPlant(listlocMapPlant.get(0).getPk().getPlantCode());   // plant wh
						//listDtSapOrderItem.get(0).setStorageLocation(slocWh.get(0).getLovVal());
						listDtSapOrderItem.get(0).setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
						listDtSapOrderItem.get(0).setItemCategory(itemCat); 
											
						listDtSapOrderItem.get(0).getCreateValue().setLastUpd(date);
						listDtSapOrderItem.get(0).getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
						dtSapOrderItemDao.update(listDtSapOrderItem.get(0));
						
				}
			}
			

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeader ;
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql6OnPremCancel(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql6Cancel",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	private List<QueryPostTransactionBean> queryPostTransactionSql7OnPremCancel(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql7Cancel",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	private String queryOriginDocNo(String docNo , String company) {
		List<PreBooking> listPreBooking =  preBookingDao.getPreBookingByreceiptNum(docNo, company) ;
		
		String prebookNo = listPreBooking.get(0).getPreBookingNo() ;
		String receiptNum = "";
		
		int i = 1 ;
		while(true) {
			List<PreBooking> listPreBookingTmp =  preBookingDao.getPreBookingByPreBookNo(prebookNo+"_"+String.valueOf(i), company);
			i+=1 ;
			
			if(BeanUtil.isEmpty(listPreBookingTmp)) {
				break ;
			}else {
				receiptNum = listPreBookingTmp.get(0).getReceiptNum() ;
			}
		}
		
		return receiptNum ;
	}
	
	private String queryOriginDocNoOnPrem(String docNo , String company) {
		
		String prebookNo = queryPrebookingOri(docNo, null, company);
		String receiptNum = "";
		
		int i = 1 ;
		while(true) {
			//List<PreBooking> listPreBookingTmp =  preBookingDao.getPreBookingByPreBookNo(prebookNo+"_"+String.valueOf(i), company);
			String pre = queryPrebookingOri(null, prebookNo+"_"+String.valueOf(i), company);
			i+=1 ;
			
			if(BeanUtil.isEmpty(pre)) {
				break ;
			}else {
				receiptNum = pre ;
			}
		}
		
		return receiptNum ;
	}
	
	@Override
	public DtSapPostHeader query7InfoAndInsertPostTransactionCancelDepositPartnerDiffDealer(DtSapTransaction dtSapTransaction ) {
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql7
//			List<QueryPostTransactionBean> listQueryPostTransactionBean = dtSapTransactionTypeDao.queryPostTransactionSql7(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			String docNoOri = "";
			List<LovMaster> configsap = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
			 if(BeanUtil.isEmpty(configsap)) {
				docNoOri = queryOriginDocNo(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			 }  
			  else { 
				docNoOri = queryOriginDocNoOnPrem(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			 }
			
			
			
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql7Cancel(docNoOri, dtSapTransaction.getCompany());

			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
					
			Date today = Calendar.getInstance().getTime();        
			String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(today);
			
			//set values
			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			List<LovMaster> plantSloc = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_DEPOSIT_PARTNER", dtSapTransaction.getCompany(), null, null, "Y") ;

			dtSapPostHeader.setRETAILSTOREID(plantSloc.get(0).getLovCode()); 
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());  
			dtSapPostHeader.setBEGINDATETIMESTAMP(todayAsString);  
			dtSapPostHeader.setENDDATETIMESTAMP(todayAsString);  
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());  
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
					
					//FINALCIAL
					DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
					dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//					dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
					dtSapPostFinalcial.setFITYPECODE(config.getRetailtypecode().split("[|]")[1]);
					dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
					dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
//					dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
					dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
					dtSapPostFinalcial.setCreateValue(masterValue);
					dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

					dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
					//END FINALCIAL
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	private String queryPrebookingOri(String docNo , String prebookNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        if(BeanUtil.isNotEmpty(docNo)) {
        	in.setDocNo(docNo);
        }else {
        	in.setPrebookNo(prebookNo);
        }
        
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-prebooking-ori",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getRefDocNo() ;
   }

}
