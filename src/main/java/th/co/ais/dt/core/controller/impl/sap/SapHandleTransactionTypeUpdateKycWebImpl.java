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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeUpdateKycService;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/sap-handle-transaction-type/v1")
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeUpdateKycWebImpl {

    private final ISapHandleTransactionTypeUpdateKycService sapHandleTransactionTypeUpdateKycService;

    @PostMapping(value = "/update-kyc", produces = {"application/json"})
    public ResponseEntity<String> handleTransactionTypeUpdateKyc(@RequestBody DtSapTransaction request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        try {
            String status = "S";

            /**
             * @STEP-0 Query sap transaction by transaction id
             */
            DtSapTransaction dtSapTransaction = sapHandleTransactionTypeUpdateKycService
                    .queryDtSapTransactionById(request.getSapTranId());

            /**
             * @STEP-1 Query workflow by transaction type
             */
            DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeUpdateKycService
                    .queryDtSapTransactionTypeByKey(dtSapTransaction.getTransactionType());

            /**
             * @STEP-2 Do workflow
             * @STEP-2.1 Sale order
             * Base on config DtSapTransactionType
             */

            // STEP-2.1 Sale order
            DtSapOrderHeader dtSapOrderHeader = null;

            if ("Y".equals(dtSapTransactionType.getSaleOrder())) {
                // query sql to get information AND insert table
                dtSapOrderHeader = sapHandleTransactionTypeUpdateKycService.queryInfoAndInsertSaleOrder(dtSapTransaction);
            }

            /**
             * @STEP-3 Call api sap
             * @STEP-3.1 Call service [sale order]
             */

            // TODO: STEP-3.1 call service [sale order] to send input to sap and keep response from sap
            if (BeanUtil.isNotNull(dtSapOrderHeader)) {
                // call service post transaction to send input to sap and keep response from sap
                DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeUpdateKycService.callSaleOrderApi(dtSapOrderHeader);

                if ("S".equals(dtSapOrderHeaderApi.getStatus())) {
                    dtSapOrderHeader.setStatus("S");
                    dtSapOrderHeader.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
 				   dtSapOrderHeader.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
 				   dtSapOrderHeader.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
 				   dtSapOrderHeader.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
 				   dtSapOrderHeader.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
 				   dtSapOrderHeader.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
 				   dtSapOrderHeader.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
 				   dtSapOrderHeader.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
 				   dtSapOrderHeader.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
 				   dtSapOrderHeader.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
 				   dtSapOrderHeader.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
 				   dtSapOrderHeader.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
 				   dtSapOrderHeader.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
 				   dtSapOrderHeader.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
                } else {
                    status = "F";
                    dtSapOrderHeader.setStatus("F");
                    dtSapOrderHeader.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
 				   dtSapOrderHeader.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
 				   dtSapOrderHeader.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
 				   dtSapOrderHeader.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
 				   dtSapOrderHeader.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
 				   dtSapOrderHeader.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
 				   dtSapOrderHeader.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
 				   dtSapOrderHeader.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
 				   dtSapOrderHeader.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
 				   dtSapOrderHeader.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
 				   dtSapOrderHeader.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
 				   dtSapOrderHeader.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
 				   dtSapOrderHeader.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
 				   dtSapOrderHeader.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
                }

                // update status dtSapPostHeader
                sapHandleTransactionTypeUpdateKycService.updateDtSapOrderHeader(dtSapOrderHeader);
            }

            /**
             * @STEP-4 Check response from sap then update status table DT_SAP_TRANSACTION
             */
            dtSapTransaction.setStatus(status);
            sapHandleTransactionTypeUpdateKycService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

        } catch (Exception e) {
            log.error("handleTransactionTypeUpdateKyc error ", e);
        }

        return new ResponseEntity<>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
    }

}
