package th.co.ais.dt.core.service.core.impl.sap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractTypeCancelGoodsReturnService;
import th.co.ais.dt.entity.pm.CreditNote;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualItem;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.entity.sap.DtSapUniversalItem;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalItemDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryContractUniversalBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypeCancelGoodsReturnServiceImpl implements ISapHandleContractTypeCancelGoodsReturnService {

    public static final String CANCEL_GOODS_RETURN = "CANCEL GOODS RETURN";
    
    private final DTConfig dTConfig;
    private final ISapCallApiService sapCallApiService;

    private final IDtSapContractTransDao dtSapContractTransDao;
    private final IDtSapContractTypeDao dtSapContractTypeDao;
    private final IDtSapUniversalHeaderDao dtSapUniversalHeaderDao;
    private final IDtSapUniversalItemDao dtSapUniversalItemDao;
    private final IDtSapAccrualHeaderDao dtSapAccrualHeaderDao;
    private final IDtSapAccrualItemDao dtSapAccrualItemDao;
    private final IDtSapContractTransLogDao dtSapContractTransLogDao;

    @Override
    public DtSapContractTrans queryDtSapContractTransByKey(Long sapContractId) {
        return dtSapContractTransDao.getByLongPrimaryKey(sapContractId);
    }

    @Override
    public DtSapAccrualHeader queryInfoAndInsertAccrual(DtSapContractTrans dtSapContractTrans) {
        try {
            List<QueryContractAccrualBean> listContractAccrualReceipt = queryContractCancelGoodsReturnAccrual(dtSapContractTrans.getDocNo(), dtSapContractTrans.getCompany());
            if (BeanUtil.isEmpty(listContractAccrualReceipt)) {
                return null;
            }

            MasterValue masterValue = getMasterValue(dtSapContractTrans);

            // Header
            DtSapAccrualHeader dtSapAccrualHeader = new DtSapAccrualHeader();
            dtSapAccrualHeader.setSapContractId(dtSapContractTrans.getSapContractId());
            dtSapAccrualHeader.setPartnername(listContractAccrualReceipt.get(0).getPartnername());
            dtSapAccrualHeader.setPartnermessageid(listContractAccrualReceipt.get(0).getPartnermessageid());
            dtSapAccrualHeader.setCompanycode(listContractAccrualReceipt.get(0).getCompanycode());
            dtSapAccrualHeader.setAccrualobjectcategory(listContractAccrualReceipt.get(0).getAccrualobjectcategory());
            dtSapAccrualHeader.setAccrualobjectsubcategory(listContractAccrualReceipt.get(0).getAccrualobjectsubcategory());
            dtSapAccrualHeader.setCreateValue(masterValue);
            dtSapAccrualHeader.setStatus("W");
            dtSapAccrualHeader.setResMessagetype(null);
            dtSapAccrualHeader.setResAccrualObjectNumber(null);
            dtSapAccrualHeaderDao.insert(dtSapAccrualHeader);

            // Item
            List<DtSapAccrualItem> dtSapAccrualItemList = new ArrayList<>();
            for (QueryContractAccrualBean contractAccrual : listContractAccrualReceipt) {
                DtSapAccrualItem dtSapAccrualItem = new DtSapAccrualItem();
                dtSapAccrualItem.setSapAccrualHeaderId(dtSapAccrualHeader.getSapAccrualHeaderId());
                dtSapAccrualItem.setPersonResponsible(contractAccrual.getPersonresponsible());
                dtSapAccrualItem.setText(contractAccrual.getText());
                dtSapAccrualItem.setStartofLife(contractAccrual.getStartoflife());
                dtSapAccrualItem.setEndofLife(contractAccrual.getEndoflife());
                dtSapAccrualItem.setAccrualItemType(contractAccrual.getAccrualitemtype());
                dtSapAccrualItem.setAccrualDeferralMethod(contractAccrual.getAccrualdeferralmethod());
                dtSapAccrualItem.setTotalAccrAmountinTransCrcy(contractAccrual.getTotalaccramountintranscrcy());
                dtSapAccrualItem.setTransactionCurrency(contractAccrual.getTransactioncurrency());
                dtSapAccrualItem.setProfitCenter(contractAccrual.getProfitcenter());
                dtSapAccrualItem.setSegment(contractAccrual.getSegment());
                dtSapAccrualItem.setDocumentNumber(contractAccrual.getDocumentnumber());
                dtSapAccrualItem.setIMEInumber(contractAccrual.getImeinumber());
                dtSapAccrualItem.setMaterialDescription(contractAccrual.getMaterialdescription());
                dtSapAccrualItem.setMobilePhoneNumber(contractAccrual.getMobilephonenumber());
                dtSapAccrualItem.setReserveField(contractAccrual.getReservefield());
                dtSapAccrualItem.setUSSDCode(contractAccrual.getUssdcode());
                dtSapAccrualItem.setProductnumber(contractAccrual.getProductnumber());
                dtSapAccrualItem.setPlant(contractAccrual.getPlant());
                dtSapAccrualItem.setProfitCenterCOPA(contractAccrual.getProfitcentercopa());
                dtSapAccrualItem.setProjectUSSDCode(contractAccrual.getProjectussdcode());
                dtSapAccrualItem.setSalesOrganization(contractAccrual.getSalesorganization());
                dtSapAccrualItem.setDistributionChannel(contractAccrual.getDistributionchannel());
                dtSapAccrualItem.setDivision(contractAccrual.getDivision());
                dtSapAccrualItem.setCreateValue(masterValue);

                dtSapAccrualItemList.add(dtSapAccrualItem);
            }
            dtSapAccrualItemDao.insertList(dtSapAccrualItemList);

            return dtSapAccrualHeader;
        } catch (Exception e) {
            log.error("queryInfoAndInsertAccrualReceipt error ", e);
            return null;

        }
    }

    @Override
    public DtSapUniversalHeader queryInfoAndInsertUniversal(DtSapContractTrans dtSapContractTrans) {
        try {
            List<QueryContractUniversalBean> listContractUniversalReceipt = queryContractCancelGoodsReturnUniversal(dtSapContractTrans.getDocNo(), dtSapContractTrans.getCompany());
            if (BeanUtil.isEmpty(listContractUniversalReceipt)) {
                return null;
            }

            MasterValue masterValue = getMasterValue(dtSapContractTrans);

            // Header
            DtSapUniversalHeader dtSapUniversalHeader = new DtSapUniversalHeader();
            dtSapUniversalHeader.setSapContractId(dtSapContractTrans.getSapContractId());
            dtSapUniversalHeader.setDocNo(listContractUniversalReceipt.get(0).getDocno());
            dtSapUniversalHeader.setCompanycode(listContractUniversalReceipt.get(0).getCompanycode());
            dtSapUniversalHeader.setDocumentdate(listContractUniversalReceipt.get(0).getDocumentdate());
            dtSapUniversalHeader.setPostingdate(listContractUniversalReceipt.get(0).getPostingdate());
            dtSapUniversalHeader.setDocType(listContractUniversalReceipt.get(0).getDoctype());
            dtSapUniversalHeader.setFiscalperiod(listContractUniversalReceipt.get(0).getFiscalperiod());
            dtSapUniversalHeader.setCurrencykey(listContractUniversalReceipt.get(0).getCurrencykey());
            dtSapUniversalHeader.setLedgergroup(listContractUniversalReceipt.get(0).getLedgergroup());
            dtSapUniversalHeader.setReference(listContractUniversalReceipt.get(0).getReference());
            dtSapUniversalHeader.setDocumentheadertext(null);
            dtSapUniversalHeader.setReferenceHDKey1(listContractUniversalReceipt.get(0).getReferencehdkey1());
            dtSapUniversalHeader.setReferenceHDKey2(listContractUniversalReceipt.get(0).getReferencehdkey2());
            dtSapUniversalHeader.setExchangerate(listContractUniversalReceipt.get(0).getExchangerate());
            dtSapUniversalHeader.setBranchcode(listContractUniversalReceipt.get(0).getBranchcode());
            dtSapUniversalHeader.setTaxReportingDate(listContractUniversalReceipt.get(0).getTaxreportingdate());
            dtSapUniversalHeader.setStatus("W");
            dtSapUniversalHeader.setMessageType(null);
            dtSapUniversalHeader.setDocumentNumber(null);
            dtSapUniversalHeader.setFiscalYear(null);
            dtSapUniversalHeader.setCreateValue(masterValue);

            dtSapUniversalHeaderDao.insert(dtSapUniversalHeader);

            // Item
            List<DtSapUniversalItem> dtSapUniversalItemList = new ArrayList<>();
            for (QueryContractUniversalBean contractUniversal : listContractUniversalReceipt) {
                DtSapUniversalItem dtSapUniversalItem = new DtSapUniversalItem();
                dtSapUniversalItem.setSapUniversalHeaderId(dtSapUniversalHeader.getSapUniversalHeaderId());
                dtSapUniversalItem.setPostingkey(contractUniversal.getPostingkey());
                dtSapUniversalItem.setAccount(contractUniversal.getAccount());
                dtSapUniversalItem.setAmountinDocCurrency(contractUniversal.getAmountindoccurrency());
                dtSapUniversalItem.setBusinessplace(contractUniversal.getBusinessplace());
                //dtSapUniversalItem.setItemAssignment();
                dtSapUniversalItem.setItemText(null);
                dtSapUniversalItem.setProductnumber(contractUniversal.getProductnumber());
                dtSapUniversalItem.setPlant(contractUniversal.getPlant());
                dtSapUniversalItem.setProfitCenterCOPA(contractUniversal.getProfitcentercopa());
                dtSapUniversalItem.setProjectUSSDCode(contractUniversal.getProjectussdcode());
                dtSapUniversalItem.setSalesOrganization(contractUniversal.getSalesorganization());
                dtSapUniversalItem.setDistributionChannel(contractUniversal.getDistributionchannel());
                dtSapUniversalItem.setDivision(contractUniversal.getDivision());
                dtSapUniversalItem.setPlant2(contractUniversal.getPlant2());
                dtSapUniversalItem.setCreateValue(masterValue);

                dtSapUniversalItemList.add(dtSapUniversalItem);
            }
            dtSapUniversalItemDao.insertList(dtSapUniversalItemList);

            return dtSapUniversalHeader;
        } catch (Exception e) {
            log.error("queryInfoAndInsertUniversalReceipt error ", e);
            return null;
        }
    }

    @Override
    public DtSapContractTransLog insertSapContractTransLog(
            DtSapContractTrans dtSapContractTrans, DtSapAccrualHeader dtSapAccrualHeader, DtSapUniversalHeader dtSapUniversalHeader, String receiptNum
    ) {
        try {
            DtSapContractTransLog saleLog = dtSapContractTransLogDao
                    .getSapContractTransLogReceipt(dtSapContractTrans.getCompany(), receiptNum);

            MasterValue masterValue = getMasterValue(dtSapContractTrans);

            DtSapContractTransLog dtSapContractTransLog = new DtSapContractTransLog();
            dtSapContractTransLog.setCompany(dtSapContractTrans.getCompany());
            dtSapContractTransLog.setDocNo(dtSapContractTrans.getDocNo());
            dtSapContractTransLog.setOCategory("B0210");
            dtSapContractTransLog.setOSubcate("B0211");
            dtSapContractTransLog.setImeiNumberOld(null);
            dtSapContractTransLog.setStatus(CANCEL_GOODS_RETURN);
            dtSapContractTransLog.setCreateValue(masterValue);
            
            if (BeanUtil.isNotNull(dtSapUniversalHeader)) {
                dtSapContractTransLog.setDocDate(TDMDataUtility.convertStringToDateByFormat(dtSapUniversalHeader.getDocumentdate(), "yyyy-MM-dd"));
            } else {
                dtSapContractTransLog.setDocDate(new Date());
            }

            if (saleLog != null) {
                dtSapContractTransLog.setAccuralObjectNumber(saleLog.getAccuralObjectNumber());
                dtSapContractTransLog.setMobileNo(saleLog.getMobileNo());
                dtSapContractTransLog.setImeiNumberOld(saleLog.getImeiNumberNew());
            }

            if (BeanUtil.isNotNull(dtSapAccrualHeader)) {
                dtSapContractTransLog.setAccuralObjectNumber(dtSapAccrualHeader.getResAccrualObjectNumber());
            }
            
            dtSapContractTransLogDao.insert(dtSapContractTransLog);
            
            return dtSapContractTransLog;
        } catch (Exception e) {
            log.error("insertSapContractTransLog error ", e);
            return null;
        }
    }

    private static MasterValue getMasterValue(DtSapContractTrans dtSapContractTrans) {
        Date date = new Date();
        MasterValue masterValue = new MasterValue();
        masterValue.setCreated(date);
        masterValue.setCreatedBy(dtSapContractTrans.getCreateValue().getCreatedBy());
        masterValue.setLastUpd(date);
        masterValue.setLastUpdBy(dtSapContractTrans.getCreateValue().getLastUpdBy());
        return masterValue;
    }

    @Override
    public DtSapAccrualHeader callCancelGoodsReturnAccrualApi(DtSapAccrualHeader dtSapAccrualHeader, String cancelReceiptNum) {
        return sapCallApiService.callCancelGoodsReturnAccrualApi(dtSapAccrualHeader, cancelReceiptNum);
    }

    @Override
    public DtSapUniversalHeader callCancelGoodsReturnUniversalApi(DtSapUniversalHeader dtSapOrderHeader, String cancelReceiptNum) {
        return sapCallApiService.callCancelGoodsReturnUniversalApi(dtSapOrderHeader, cancelReceiptNum);
    }

    @Override
    public String getReceiptNumFromCnNum(String docNo, String company){
        // TODO: Change query to postgres when postgres support already
        // Query from postgres
        // CreditNote creditNote = creditNoteDao.getCreditNoteByCnNum(docNo, company);
        // receiptNum = creditNote.getReceiptNum();

        // query from on prem
        return getReceiptNumFromCnNumOnPrem(docNo, company);
    }

    @Override
    public void updateDtSapAccrualHeader(DtSapAccrualHeader dtSapAccrualHeader) {
        try {
            dtSapAccrualHeaderDao.update(dtSapAccrualHeader);
        } catch (Exception e) {
            log.error("updateDtSapAccrualHeader error ", e);
            throw e;
        }
    }

    @Override
    public void updateDtSapUniversalHeader(DtSapUniversalHeader dtSapUniversalHeader) {
        try {
            dtSapUniversalHeaderDao.update(dtSapUniversalHeader);
        } catch (Exception e) {
            log.error("updateDtSapUniversalHeader error ", e);
            throw e;
        }
    }

    @Override
    public void updateDtSapContractTransLog(DtSapContractTransLog dtSapContractTransLog) {
        try {
            dtSapContractTransLog.getCreateValue().setLastUpd(new Date());
            dtSapContractTransLogDao.update(dtSapContractTransLog);
        } catch (Exception e) {
            log.error("updateDtSapContractTransLog", e);
            throw e;
        }
    }

    @Override
    public void updateDtSapContractTransAfterWorkflow(DtSapContractTrans dtSapContractTrans) {
        try {
            dtSapContractTrans.getCreateValue().setLastUpd(new Date());
            dtSapContractTransDao.update(dtSapContractTrans);
        } catch (Exception e) {
            log.error("updateDtSapContractTransAfterWorkflow", e);
            throw e;
        }
    }

    private String getReceiptNumFromCnNumOnPrem(String docNo, String company) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);

        String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/getReceiptNumFromCnNum", gson.toJson(in), "POST", null);
        Gson gsonRes = new Gson();
        QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
        return res.getDocNo();
    }

    private List<QueryContractAccrualBean> queryContractCancelGoodsReturnAccrual(String docNo, String company) {
        // TODO: Change query to postgres when postgres support already
        // Query from postgres
        // return dtSapContractTypeDao.queryContractCancelGoodsReturnAccrual(docNo, company);

        // Query from on prem
        return queryContractCancelGoodsReturnAccrualOnPrem(docNo, company);
    }

    private List<QueryContractAccrualBean> queryContractCancelGoodsReturnAccrualOnPrem(String docNo, String company) {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);

        String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-cancel-goods-return-accrual", gson.toJson(in), "POST", null);

        Gson gsonRes = new Gson();
        CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
        String resObject = gsonRes.toJson(res.getResultObj());

        Type type = new TypeToken<ArrayList<QueryContractAccrualBean>>() {
        }.getType();

        return gson.fromJson(resObject, type);
    }

    private List<QueryContractUniversalBean> queryContractCancelGoodsReturnUniversal(String docNo, String company) {
        // TODO: Change query to postgres when postgres support already
        // Query from postgres
        // return dtSapContractTypeDao.queryContractCancelGoodsReturnUniversal(docNo, company);

        // Query from on prem
        return queryContractCancelGoodsReturnUniversalOnPrem(docNo, company);
    }

    private List<QueryContractUniversalBean> queryContractCancelGoodsReturnUniversalOnPrem(String docNo, String company) {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);

        String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-cancel-goods-return-universal", gson.toJson(in), "POST", null);

        Gson gsonRes = new Gson();
        CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
        String resObject = gsonRes.toJson(res.getResultObj());

        Type listQueryContractUniversalBean = new TypeToken<ArrayList<QueryContractUniversalBean>>() {
        }.getType();

        return gson.fromJson(resObject, listQueryContractUniversalBean);
    }

    @Override
    public List<CreditNote> queryCnReceipt(String docNo, String company) {
        // Query from on prem
        return queryCnReceiptOnPrem(docNo, company);
    }

    private List<CreditNote> queryCnReceiptOnPrem(String docNo, String company) {

        Gson gson = new Gson();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);

        String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-cn-receipt", gson.toJson(in), "POST", null);

        CommonResponseBean res = gson.fromJson(out, CommonResponseBean.class);
        String resObject = gson.toJson(res.getResultObj());

        Type listCreditNote = new TypeToken<ArrayList<CreditNote>>() {
        }.getType();

        return gson.fromJson(resObject, listCreditNote);
    }

}
