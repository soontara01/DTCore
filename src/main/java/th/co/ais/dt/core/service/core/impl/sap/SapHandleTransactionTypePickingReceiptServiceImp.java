package th.co.ais.dt.core.service.core.impl.sap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypePickingReceiptService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostDiscountItems;
import th.co.ais.dt.entity.sap.DtSapPostFinalcial;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapCancelReserveDao;
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
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypePickingReceiptServiceImp implements ISapHandleTransactionTypePickingReceiptService{
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
	private final IDtSapPostFinalcialDao dtSapPostFinalcialDao;
	private final DTConfig dTConfig;
	private final ILovMasterDao lovMasterDao ;
	private final IDtSapCancelReserveDao dtSapCancelReserveDao ;
	
	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) throws DataAccessException{
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query sql4
			List<QueryPostTransactionBean> listQueryPostTransactionBean = this.queryPostTransactionSql4(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
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
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());  
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			// DISCOUNT HEADER======================
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
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
//			List<DtSapPostTender> tenders = dtSapTransactionTypeDao.queryTenderPostTransactionSql1(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
//			List<DtSapPostTender> tenders = new ArrayList<DtSapPostTender>();
				DtSapPostTender t = new DtSapPostTender();
				t.setTENDERSEQUENCENUMBER(String.valueOf(1));
				t.setTENDERTYPECODE("ZT84");
				t.setTENDERAMOUNT("0");
				t.setTENDERCURRENCY("THB");
				t.setCreateValue(masterValue);
				//set herder id
				t.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostTenderDao.insert(t);
			//END TENDER
			List<LovMaster> taxtCode = lovMasterDao.listLovMasterByCriteria("SAP_S4_TAXTYPECODE", "SAP_S4_TAXTYPECODE", "TAXTYPECODE", null, "Y") ;
			int itemNo = 1 ;
			for(QueryPostTransactionBean el : listQueryPostTransactionBean) {
				
				//SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
				
		
				dtSapPostSalesItems.setRETAILTYPECODE(el.getRETAILTYPECODE());
				
		
				dtSapPostSalesItems.setRETAILREASONCODE(el.getRETAILREASONCODE());
				
				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				dtSapPostSalesItems.setITEMID(el.getITEMID());
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT());
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
//				if(BeanUtil.isNotEmpty(el.getDISCOUNTSEQUENCENUMBER())) {
//					DtSapPostDiscountItems dtSapPostDiscountItems = new DtSapPostDiscountItems();
//					dtSapPostDiscountItems.setDISCOUNTID(el.getDISCOUNTID());
//					dtSapPostDiscountItems.setDISCOUNTSEQUENCENUMBER(el.getDISCOUNTSEQUENCENUMBER());
//					dtSapPostDiscountItems.setDISCOUNTTYPECODE(el.getDISCOUNTTYPECODE());
//					dtSapPostDiscountItems.setREDUCTIONAMOUNT(el.getREDUCTIONAMOUNT());
//
//					dtSapPostDiscountItems.setCreateValue(masterValue);
//					dtSapPostDiscountItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
//					
//					dtSapPostDiscountItemsDao.insert(dtSapPostDiscountItems);
//				}
				
				//TAX ITEM
//				if(BeanUtil.isNotEmpty(el.getTAXSEQUENCENUMBER())) {
//					DtSapPostTaxItems dtSapPostTaxItems = new DtSapPostTaxItems();
//					dtSapPostTaxItems.setTAXAMOUNT(el.getTAXAMOUNT());
//					dtSapPostTaxItems.setTAXSEQUENCENUMBER(el.getTAXSEQUENCENUMBER());
//					dtSapPostTaxItems.setTAXTYPECODE(taxtCode.get(0).getLovVal());
//					
//					dtSapPostTaxItems.setCreateValue(masterValue);
//					dtSapPostTaxItems.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
//					dtSapPostTaxItemsDao.insert(dtSapPostTaxItems);
//				}
			
			}
			
			//FINALCIAL
//			DtSapPostFinalcial dtSapPostFinalcial = new DtSapPostFinalcial();
//			dtSapPostFinalcial.setFINUMBER(listQueryPostTransactionBean.get(0).getFINUMBER());
//			dtSapPostFinalcial.setFITYPECODE(listQueryPostTransactionBean.get(0).getFITYPECODE());
//			dtSapPostFinalcial.setAMOUNT(listQueryPostTransactionBean.get(0).getAMOUNT());
//			dtSapPostFinalcial.setFICURRENCY(listQueryPostTransactionBean.get(0).getFICURRENCY());
//			dtSapPostFinalcial.setREFERERENCEID(listQueryPostTransactionBean.get(0).getREFERERENCEID());
//			dtSapPostFinalcial.setPARTNER_NO(listQueryPostTransactionBean.get(0).getPARTNER_NO());
//			dtSapPostFinalcial.setCreateValue(masterValue);
//			dtSapPostFinalcial.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//			dtSapPostFinalcialDao.insert(dtSapPostFinalcial);
			//END FINALCIAL
			
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(TransactionType);
	}
	
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		DtSapPostHeader res = sapCallApiService.callPostTransactionApi(stSapPostHeader);
		return res ;
	}

	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
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
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql4(String docNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.queryPostTransactionSql4(docNo, company);
				
		 }  
		  else { 
			// query from on prem
				return queryPostTransactionSql4OnPrem(docNo, company);
		 }
		
		
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql4OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql4",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
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
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql4(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean) && BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getSAP_RESERVE_NO())) {
				String[] listReserve = listQueryPostTransactionBean.get(0).getSAP_RESERVE_NO().split("\\|");
				
				for(String re : listReserve ) {
					DtSapCancelReserve dtSapCancelReserve = new DtSapCancelReserve();
					dtSapCancelReserve.setCreateValue(masterValue) ;
					dtSapCancelReserve.setSapTranId(dtSapTransaction.getSapTranId());
					dtSapCancelReserve.setStatus("W");
					dtSapCancelReserve.setSapReserveNo(re);
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
	
	public void updatedtSapCancelReserve(DtSapCancelReserve dtSapCancelReserve) {
		dtSapCancelReserveDao.update(dtSapCancelReserve);
	}

}
