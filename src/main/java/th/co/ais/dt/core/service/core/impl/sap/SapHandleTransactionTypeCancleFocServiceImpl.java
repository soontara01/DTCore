package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancleFocService;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostMappingTender;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostMappingTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
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
public class SapHandleTransactionTypeCancleFocServiceImpl implements ISapHandleTransactionTypeCancleFocService {

	private final IDtSapTransactionDao dtSapTransactionDao;
	private final IDtSapPostHeaderDao dtSapPostHeaderDao;
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
	private final IDtSapPostTenderDao dtSapPostTenderDao;
	private final ISapCallApiService sapCallApiService;
	private final DTConfig dTConfig;
	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao ;
	private final ILovMasterDao lovMasterDao ;

	@Override
	public DtSapTransaction queryDtSapTransactionById(Long sapTranId) {
		return dtSapTransactionDao.getByLongPrimaryKey(sapTranId);
	}

	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(transactionType);
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

			// Query sql2
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql2(
					dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			// Query transaction type code config
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao
					.getByLongPrimaryKey(dtSapTransaction.getTransactionType());
			
			List<QueryPostTransactionBean> toSubStock = returnToGoodsOrBad(dtSapTransaction.getCompany(), dtSapTransaction.getDocNo()) ;


//			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
//			List<DtSapPostSalesItems> listDtSapPostSalesItems = new ArrayList<>();
//			List<DtSapPostDiscountItems> listDtSapPostDiscountItems = new ArrayList<>();
//			List<DtSapPostTaxItems> listDtSapPostTaxItems = new ArrayList<>();
//			List<DtSapPostDiscountHeader> listDtSapPostDiscountHeader = new ArrayList<>();
//			List<DtSapPostTender> listDtSapPostTender = new ArrayList<>();
//			List<DtSapPostFinalcial> listDtSapPostFinalcial = new ArrayList<>();
//			List<DtSapPostGoodsMovement> listDtSapPostGoodsMovement = new ArrayList<>();

			// HEADER
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID());
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());

			// By config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());

			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID()); //null
			dtSapPostHeader
					.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());
			//dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID()); //null
			//dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); //null
			dtSapPostHeader.setCreateValue(masterValue);
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");

			dtSapPostHeaderDao.insert(dtSapPostHeader);

			// Split retail type and reason code
			String[] retailTypeCode = config.getRetailtypecode().split("\\|");
			String[] retailReasonCode = config.getRetailreasoncode().split("\\|");

			BigDecimal sumSaleAmount = new BigDecimal(0L);
			int itemNo = 1 ;
			for (QueryPostTransactionBean postTransaction : listQueryPostTransactionBean) {

				// SALES ITEM OLD
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(postTransaction.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));

				// By config
				dtSapPostSalesItems.setRETAILTYPECODE(retailTypeCode[0]);
				
				String to_subStock = "";
				if(toSubStock!= null && 
				   BeanUtil.isNotEmpty(toSubStock.get(0).getTRANSACTIONSEQUENCENUMBER()) &&
				   toSubStock.get(0).getTRANSACTIONSEQUENCENUMBER().equals("BD")
				   ) {
					to_subStock = retailReasonCode[1];  // ZR02
				}else {
					to_subStock = retailReasonCode[0];  // ZR01
				}
				
				dtSapPostSalesItems.setRETAILREASONCODE(to_subStock);

				dtSapPostSalesItems.setITEMIDQUALIFIER(postTransaction.getITEMIDQUALIFIER());
				dtSapPostSalesItems.setITEMID(postTransaction.getITEMID());
				dtSapPostSalesItems.setRETAILQUANTITY(postTransaction.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(postTransaction.getSALESUNITOFMEASURE());
				dtSapPostSalesItems.setSALESAMOUNT(postTransaction.getSALESAMOUNT());
				dtSapPostSalesItems.setNORMALSALESAMOUNT(postTransaction.getNORMALSALESAMOUNT());
//				dtSapPostSalesItems.setPROMOTIONID(postTransaction.getPROMOTIONID());
//				dtSapPostSalesItems.setBATCHID(postTransaction.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(postTransaction.getSERIALNUMBER());
				dtSapPostSalesItems.setACTUALUNITPRICE(postTransaction.getACTUALUNITPRICE());
//				dtSapPostSalesItems.setSO_NO(postTransaction.getSO_NO());
//				dtSapPostSalesItems.setITEM_NO(postTransaction.getITEM_NO());
//				dtSapPostSalesItems.setIUID(postTransaction.getIUID());
				dtSapPostSalesItems.setCreateValue(masterValue);
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
				itemNo = itemNo +1 ;

				// Sum sale amount for TENDER
				BigDecimal saleAmount = new BigDecimal(
						dtSapPostSalesItems.getSALESAMOUNT() + dtSapPostSalesItems.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
			}

			// TENDER [FOC]
			List<DtSapPostMappingTender> fDtSapPostMappingTender = dtSapPostMappingTenderDao.queryDtSapPostMappingTender("FOC", null) ;
			
			DtSapPostTender dtSapPostTender = new DtSapPostTender();
			dtSapPostTender.setTENDERSEQUENCENUMBER("1");
			dtSapPostTender.setTENDERTYPECODE(fDtSapPostMappingTender.get(0).getTenderTypeId());
			dtSapPostTender.setTENDERAMOUNT(sumSaleAmount.toString());
			dtSapPostTender.setTENDERCURRENCY("THB");
			dtSapPostTender.setCreateValue(masterValue);
			dtSapPostTender.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

			dtSapPostTenderDao.insert(dtSapPostTender);

			return dtSapPostHeader;
		} catch (Exception e) {
			log.error("queryInfoAndInsertPostTransaction error ", e);
			return null;
		}
	}

	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		return sapCallApiService.callPostTransactionApi(stSapPostHeader);
	}

	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
		try {
			dtSapTransaction.getCreateValue().setLastUpd(new Date());

			dtSapTransactionDao.update(dtSapTransaction);
		} catch (Exception e) {
			log.error("updateDtSapTransactionAfterWorkflow error ", e);
		}
	}

	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		} catch (Exception e) {
			log.error("updateDtSapPostHeader error ", e);
		}
	}

	private List<QueryPostTransactionBean> queryPostTransactionSql2(String docNo, String company) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// TODO: Change query to postgres when postgres support already
				// Query from postgres
				return dtSapTransactionTypeDao.queryPostTransactionSql2(company, docNo);
		 }  
		  else { 
			// Query from on prem
				return queryPostTransactionSql2OnPrem(docNo, company);
		 }
		

		
	}

	private List<QueryPostTransactionBean> queryPostTransactionSql2OnPrem(String docNo, String company) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json");

		QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
		in.setDocNo(docNo);
		in.setCompany(company);

		String out = c.HttpClient(properties,
				dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql2", gson.toJson(in), "POST",
				null);
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);

		return res.getListQueryPostTransactionBean();
	}
	
	private List<QueryPostTransactionBean> returnToGoodsOrBad(String company, String docNo){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
				return dtSapTransactionTypeDao.returnToGoodsOrBad(company, docNo);
		 }else {
			// query from on prem
				return returnToGoodsOrBadOnPrem(company, docNo);
		 }	
	}
	
	private List<QueryPostTransactionBean> returnToGoodsOrBadOnPrem(String company, String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/goods-return-substock",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
	}

}
