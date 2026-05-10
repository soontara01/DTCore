package th.co.ais.dt.core.service.core.impl.sap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCompensationChangeSerialService;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.HttpClientUtilDT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCompensationChangeSerialServiceImpl implements ISapHandleTransactionTypeCompensationChangeSerialService {

    private final IDtSapTransactionDao dtSapTransactionDao;
    private final IDtSapPostHeaderDao dtSapPostHeaderDao;
    private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
    private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;
    private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
    private final IDtSapPostTenderDao dtSapPostTenderDao;
    private final ISapCallApiService sapCallApiService;
    private final DTConfig dTConfig;

    @Override
    public DtSapTransaction queryDtSapTransactionById(Long sapTranId) {
        return dtSapTransactionDao.getByLongPrimaryKey(sapTranId);
    }

    @Override
    public DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType) {
        return dtSapTransactionTypeDao.getByLongPrimaryKey(transactionType);
    }

    @Override
    public DtSapTransactionTypeCodeConfig queryDtSapTransactionTypeCodeConfig(Long transactionType) {
        return dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(transactionType);
    }

    @Override
    public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction, DtSapTransactionTypeCodeConfig config) {
        try {
            Date date = new Date();
            MasterValue masterValue = new MasterValue();
            masterValue.setCreated(date);
            masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
            masterValue.setLastUpd(date);
            masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());

            // Query sql3
            List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql8(dtSapTransaction.getDocNo());

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

            dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());
            dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());
            dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
            dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());
            dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());
            dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());
            dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());
            dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER());
            dtSapPostHeader.setCreateValue(masterValue);
            dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
            dtSapPostHeader.setStatus("W");

            dtSapPostHeaderDao.insert(dtSapPostHeader);

            int itemNo = 1 ;
            for (QueryPostTransactionBean postTransaction : listQueryPostTransactionBean) {

                // SALES ITEM
                DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
               // dtSapPostSalesItems.setRETAILSEQUENCENUMBER(postTransaction.getRETAILSEQUENCENUMBER());
                dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
                
                // By config
                dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
                dtSapPostSalesItems.setRETAILREASONCODE(config.getRetailreasoncode());

                dtSapPostSalesItems.setITEMIDQUALIFIER(postTransaction.getITEMIDQUALIFIER());
                dtSapPostSalesItems.setITEMID(postTransaction.getITEMID());
                dtSapPostSalesItems.setRETAILQUANTITY(postTransaction.getRETAILQUANTITY());
                dtSapPostSalesItems.setSALESUNITOFMEASURE(postTransaction.getSALESUNITOFMEASURE());
                dtSapPostSalesItems.setSALESAMOUNT(postTransaction.getSALESAMOUNT());
                dtSapPostSalesItems.setNORMALSALESAMOUNT(postTransaction.getNORMALSALESAMOUNT());
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

    private List<QueryPostTransactionBean> queryPostTransactionSql8(String docNo) {
        // TODO: Change query to postgres when postgres support already
        // Query from postgres
        // return dtSapTransactionTypeDao.queryPostTransactionSql8(docNo);

        // Query from on prem
        return queryPostTransactionSql8OnPrem(docNo);
    }

    private List<QueryPostTransactionBean> queryPostTransactionSql8OnPrem(String docNo) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);

        String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql8", gson.toJson(in), "POST", null);
        Gson gsonRes = new Gson();
        QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);

        return res.getListQueryPostTransactionBean();
    }

}
