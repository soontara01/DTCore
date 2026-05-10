package th.co.ais.dt.core.controller.impl.sap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCompensationChangeSerialService;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/sap-handle-transaction-type/v1")
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCompensationChangeSerialWebImpl {

    private final ISapHandleTransactionTypeCompensationChangeSerialService sapHandleTransactionTypeCompensationChangeSerialService;

    @PostMapping(value = "/compensation-change-serial", produces = {"application/json"})
    public ResponseEntity<String> handleTransactionTypeCompensationChangeSerial(@RequestBody DtSapTransaction request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        try {
            String status = "S";

            /**
             * @STEP-0 Query sap transaction by transaction id
             */
            DtSapTransaction dtSapTransaction = sapHandleTransactionTypeCompensationChangeSerialService
                    .queryDtSapTransactionById(request.getSapTranId());

            /**
             * @STEP-1 Query workflow by transaction type
             */
            DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeCompensationChangeSerialService
                    .queryDtSapTransactionTypeByKey(dtSapTransaction.getTransactionType());

            /**
             * @STEP-2 Do workflow
             * @STEP-2.1 Sale order
             * @STEP-2.2 Post transaction
             * @STEP-2.3 Cancel reserve
             * Base on config DtSapTransactionType
             */

            // STEP-2.1 Sale order
            if ("Y".equals(dtSapTransactionType.getSaleOrder())) {
                // TODO: query sql to get information
                // TODO: insert table
            }

            /**
             * STEP-2.2 Post transaction
             * ZRGI-ZR01 for new item
             * ZRGR-ZR02 for old item
             */
            DtSapPostHeader dtSapPostHeaderGi = null;
            DtSapPostHeader dtSapPostHeaderGr = null;

            if ("Y".equals(dtSapTransactionType.getPostTransaction())) {
                // TODO: query sql to get information
                // TODO: insert table

                // Query transaction type code config
                DtSapTransactionTypeCodeConfig config = sapHandleTransactionTypeCompensationChangeSerialService
                        .queryDtSapTransactionTypeCodeConfig(dtSapTransaction.getTransactionType());

                // Split retail type and reason code
                String[] retailTypeCode = config.getRetailtypecode().split("\\|");
                String[] retailReasonCode = config.getRetailreasoncode().split("\\|");

                DtSapTransactionTypeCodeConfig newConfig = new DtSapTransactionTypeCodeConfig();
                newConfig.setTransactiontypecode(config.getTransactiontypecode());

                // Process for GI
                newConfig.setRetailtypecode(retailTypeCode[1]);
                newConfig.setRetailreasoncode(retailReasonCode[1]);
                dtSapPostHeaderGi = sapHandleTransactionTypeCompensationChangeSerialService.queryInfoAndInsertPostTransaction(dtSapTransaction, newConfig);

                // Process for GR
                newConfig.setRetailtypecode(retailTypeCode[0]);
                newConfig.setRetailreasoncode(retailReasonCode[0]);
                dtSapPostHeaderGr = sapHandleTransactionTypeCompensationChangeSerialService.queryInfoAndInsertPostTransaction(dtSapTransaction, newConfig);
            }

            // STEP-2.3 Cancel reserve
            if ("Y".equals(dtSapTransactionType.getCancelReserve())) {
                // TODO: query sql to get information
                // TODO: insert table

                // TODO: call service cancel reserve to send input to sap and keep response from sap
            }

            /**
             * @STEP-3 Call api sap
             * @STEP-3.1 Call service [sale order]
             * @STEP-3.2 Call service [post transaction]
             * @STEP-3.3 Call service [cancel reserve]
             */

            // TODO: STEP-3.1 call service [sale order] to send input to sap and keep response from sap

            // TODO: STEP-3.2 call service [post transaction] to send input to sap and keep response from sap
            if (BeanUtil.isNotNull(dtSapPostHeaderGi)) {
                DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeCompensationChangeSerialService.callPostTransactionApi(dtSapPostHeaderGi);

                if ("S".equals(dtSapPostHeaderApi.getStatus())) {
                    status = "S";
                    dtSapPostHeaderGi.setStatus("S");
                } else {
                    status = "F";
                    dtSapPostHeaderGi.setStatus("F");
                }

                // Update status dtSapPostHeader
                sapHandleTransactionTypeCompensationChangeSerialService.updateDtSapPostHeader(dtSapPostHeaderGi);
            }

            if (BeanUtil.isNotNull(dtSapPostHeaderGr)) {
                DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeCompensationChangeSerialService.callPostTransactionApi(dtSapPostHeaderGr);

                if ("S".equals(dtSapPostHeaderApi.getStatus())) {
                    status = "S";
                    dtSapPostHeaderGr.setStatus("S");
                } else {
                    status = "F";
                    dtSapPostHeaderGr.setStatus("F");
                }

                // Update status dtSapPostHeader
                sapHandleTransactionTypeCompensationChangeSerialService.updateDtSapPostHeader(dtSapPostHeaderGr);
            }

            // TODO: STEP-3.3 call service [cancel reserve] to send input to sap and keep response from sap

            /**
             * @STEP-4 Check response from sap then update status table DT_SAP_TRANSACTION
             */
            dtSapTransaction.setStatus(status);
            sapHandleTransactionTypeCompensationChangeSerialService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

        } catch (Exception e) {
            log.error("handleTransactionTypeCompensationChangeSerial error ", e);
        }

        return new ResponseEntity<>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
    }

}
