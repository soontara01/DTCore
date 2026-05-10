package th.co.ais.dt.core.service.core.impl.sap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelFocSameDayService;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapPostVoid;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostVoidDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCancelFocSameDayServiceImpl implements ISapHandleTransactionTypeCancelFocSameDayService {

    private final IDtSapTransactionDao dtSapTransactionDao;
    private final IDtSapPostHeaderDao dtSapPostHeaderDao;
    private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
    private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;
    private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
    private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao;
    private final IDtSapPostTenderDao dtSapPostTenderDao;
    private final ISapCallApiService sapCallApiService;
    private final DTConfig dTConfig;
    private final IDtSapPostVoidDao dtSapPostVoidDao ;
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

            // Query sql1 cancel
            List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1Cancel(
                    dtSapTransaction.getDocNo(), dtSapTransaction.getCompany()
            );

            // Query transaction type code config
           // DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao
               //     .getByLongPrimaryKey(dtSapTransaction.getTransactionType());

            // HEADER
            DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
            dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID());
            dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());

            // By config
            dtSapPostHeader.setTRANSACTIONTYPECODE("Z001");

            dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());
            dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());
            dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
            dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());
            dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());
            dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());
            //dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());
            //dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER());
            dtSapPostHeader.setCreateValue(masterValue);
            dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
            dtSapPostHeader.setStatus("W");

            dtSapPostHeaderDao.insert(dtSapPostHeader);

            // DISCOUNT HEADER
//            if (BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
//                DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//                dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//                dtSapPostDiscountHeader.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//                dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//                dtSapPostDiscountHeader.setREDUCTIONAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//                dtSapPostDiscountHeader.setCreateValue(masterValue);
//                dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//
//                dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
//            }

            int itemNo = 1 ;
            for (QueryPostTransactionBean postTransaction : listQueryPostTransactionBean) {

                // SALES ITEM
                DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
                //dtSapPostSalesItems.setRETAILSEQUENCENUMBER(postTransaction.getRETAILSEQUENCENUMBER());
                dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));

                // By config
                dtSapPostSalesItems.setRETAILTYPECODE("ZWRT");
                dtSapPostSalesItems.setRETAILREASONCODE("ZR01");

                dtSapPostSalesItems.setITEMIDQUALIFIER(postTransaction.getITEMIDQUALIFIER());
                dtSapPostSalesItems.setITEMID(postTransaction.getITEMID());
                dtSapPostSalesItems.setRETAILQUANTITY(postTransaction.getRETAILQUANTITY());
                dtSapPostSalesItems.setSALESUNITOFMEASURE(postTransaction.getSALESUNITOFMEASURE());
                dtSapPostSalesItems.setSALESAMOUNT("0");
                dtSapPostSalesItems.setNORMALSALESAMOUNT("0");
                dtSapPostSalesItems.setPROMOTIONID(postTransaction.getPROMOTIONID());
                dtSapPostSalesItems.setBATCHID(postTransaction.getBATCHID());
                dtSapPostSalesItems.setSERIALNUMBER(postTransaction.getSERIALNUMBER());
                dtSapPostSalesItems.setACTUALUNITPRICE(postTransaction.getACTUALUNITPRICE());
                dtSapPostSalesItems.setSO_NO(postTransaction.getSO_NO());
                dtSapPostSalesItems.setITEM_NO(postTransaction.getITEM_NO());
                dtSapPostSalesItems.setIUID(postTransaction.getIUID());
                dtSapPostSalesItems.setCreateValue(masterValue);
                dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

                dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
                itemNo = itemNo +1 ;
            }

            // TENDER [ZT84]
            DtSapPostTender dtSapPostTender = new DtSapPostTender();
            dtSapPostTender.setTENDERSEQUENCENUMBER("1");
            dtSapPostTender.setTENDERTYPECODE("ZT84");
            dtSapPostTender.setTENDERAMOUNT("0");
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

    private List<QueryPostTransactionBean> queryPostTransactionSql1Cancel(String docNo, String company) {
    	List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
    	 if(BeanUtil.isEmpty(config)) {
    		// TODO: Change query to postgres when postgres support already
    	        // Query from postgres
    	         return dtSapTransactionTypeDao.queryPostTransactionSql1Cancel(company, docNo);
    	 }  
    	  else { 
    		// Query from on prem
    	        return queryPostTransactionSql1CancelOnPrem(docNo, company);
    	 }
        

        
    }

    private List<QueryPostTransactionBean> queryPostTransactionSql1CancelOnPrem(String docNo, String company) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);

        String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql1Cancel", gson.toJson(in), "POST", null);
        Gson gsonRes = new Gson();
        QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);

        return res.getListQueryPostTransactionBean();
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

            // Query sql1 cancel
            List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1Cancel(
                    dtSapTransaction.getDocNo(), dtSapTransaction.getCompany()
            );

            // Query transaction type code config
            DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao
                    .getByLongPrimaryKey(dtSapTransaction.getTransactionType());

            // HEADER
            DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
            dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID());
            dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());

            // By config
            dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());

            dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());
            dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());
            dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
            dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());
            dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());
            dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());
            //dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());
            //dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER());
            dtSapPostHeader.setCreateValue(masterValue);
            dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
            dtSapPostHeader.setStatus("W");

            dtSapPostHeaderDao.insert(dtSapPostHeader);

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

            return dtSapPostHeader;
        } catch (Exception e) {
            log.error("queryInfoAndInsertPostTransaction error ", e);
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
}
