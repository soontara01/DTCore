package th.co.ais.dt.core.service.core.impl.sap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeUpdateKycService;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemSerialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.util.BeanUtil;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeUpdateKycServiceImpl implements ISapHandleTransactionTypeUpdateKycService {

    private final IDtSapTransactionDao dtSapTransactionDao;
    private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
    private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
    private final IDtSapOrderItemDao dtSapOrderItemDao;
    private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
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

    public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
        return sapCallApiService.callPostTransactionApi(stSapPostHeader);
    }

    @Override
    public DtSapOrderHeader queryInfoAndInsertSaleOrder(DtSapTransaction dtSapTransaction) {
        try {
            Date date = new Date();
            MasterValue masterValue = new MasterValue();
            masterValue.setCreated(date);
            masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
            masterValue.setLastUpd(date);
            masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());

            // Query origin saleOrder
            List<DtSapTransaction> dtSapTransactionList = dtSapOrderHeaderDao.queryOriginOrder(
                    dtSapTransaction.getDocNo(),
                    dtSapTransaction.getCompany(),
                    18L
            );

            if (BeanUtil.isEmpty(dtSapTransactionList)) {
                return new DtSapOrderHeader();
            }

            // Query saleOrder header
            List<DtSapOrderHeader> dtSapOrderHeaderList = dtSapOrderHeaderDao
                    .queryDtSapOrderHeaderBySapTranId(dtSapTransactionList.get(0).getSapTranId());

            // Insert header Only one
            DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
            dtSapOrderHeader.setStatus("W");
            dtSapOrderHeader.setPartnerName(dtSapOrderHeaderList.get(0).getPartnerName());
            dtSapOrderHeader.setChangeMode("U");
            dtSapOrderHeader.setSalesOrderDocument(dtSapOrderHeaderList.get(0).getRes_SalesOrderDocument());
            dtSapOrderHeader.setDeliveryBlock("/");
            dtSapOrderHeader.setSalesDocType("");
			dtSapOrderHeader.setSalesOrganiztion("");
			dtSapOrderHeader.setDistributionChannel("");
			dtSapOrderHeader.setDivision("");
			dtSapOrderHeader.setShippingConditions("");

            dtSapOrderHeader.setCreateValue(masterValue);
            dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());

            dtSapOrderHeaderDao.insert(dtSapOrderHeader);

            /*
            // Query receipt info sql1 FOR serial
            List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql1(
                    dtSapTransaction.getDocNo(),
                    dtSapTransaction.getCompany()
            );

            Map<String, List<String>> mapMatCodeSerial = new HashMap<>();
            for (QueryPostTransactionBean el : listQueryPostTransactionBean) {
                String itemId = el.getITEMID();
                String serialNumber = BeanUtil.isNotEmpty(el.getSERIALNUMBER()) ? el.getSERIALNUMBER() : "NON_SERIAL";

                mapMatCodeSerial
                        .computeIfAbsent(itemId, k -> new ArrayList<>())
                        .add(serialNumber);
            }

            for (QuerySaleOrderBean querySaleOrderBean : listQuerySaleOrderBean) {
                // Item
                DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
                dtSapOrderItem.setChangeMode("U");
                dtSapOrderItem.setItem(querySaleOrderBean.getItem());
                dtSapOrderItem.setMaterialNumber(querySaleOrderBean.getMaterialNumber());

                dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
                dtSapOrderItem.setCreateValue(masterValue);
                dtSapOrderItemDao.insert(dtSapOrderItem);

                // Item serial
                if (BeanUtil.isNotEmpty(mapMatCodeSerial.get(querySaleOrderBean.getMaterialNumber()))) {
                    for (String serial : mapMatCodeSerial.get(querySaleOrderBean.getMaterialNumber())) {
                        if (!serial.equals("NON_SERIAL")) {
                            DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
                            dtSapOrderItemSerial.setChangeMode("U");
                            dtSapOrderItemSerial.setNumberOfSerialNumbers(serial);
                            dtSapOrderItemSerial.setIUIDCustomerRelevant(querySaleOrderBean.getIUIDCustomerRelevant());

                            dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
                            dtSapOrderItemSerial.setCreateValue(masterValue);
                            dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
                        }
                    }
                }
            }*/

            return dtSapOrderHeader;
        } catch (Exception e) {
            log.error("queryInfoAndInsertSaleOrder error ", e);
            return null;
        }
    }

    @Override
    public DtSapOrderHeader callSaleOrderApi(DtSapOrderHeader dtSapOrderHeader) {
        return sapCallApiService.callSaleorderApi(dtSapOrderHeader);
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
    public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader) {
        try {
            dtSapOrderHeaderDao.update(dtSapOrderHeader);
        } catch (Exception e) {
            log.error("updateDtSapPostHeader error ", e);
        }
    }

    /*private List<QueryPostTransactionBean> queryPostTransactionSql1(String docNo, String company) {
        // TODO: Change query to postgres when postgres support already
        // Query from postgres
        //return dtSapTransactionTypeDao.queryPostTransactionSql1(docNo, company);

        // query from on prem
        return queryPostTransactionSql1OnPrem(docNo, company);
    }

    private List<QueryPostTransactionBean> queryPostTransactionSql1OnPrem(String docNo, String company) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        HttpClientUtilDT c = new HttpClientUtilDT();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json");

        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);

        String out = c.HttpClient(
                properties,
                dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql1",
                gson.toJson(in),
                "POST",
                null
        );
        Gson gsonRes = new Gson();
        QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);

        return res.getListQueryPostTransactionBean();
    }*/

}
