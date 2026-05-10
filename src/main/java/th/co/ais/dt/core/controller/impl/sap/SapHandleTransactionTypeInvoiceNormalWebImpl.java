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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeInvoiceNormalService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sap-handle-transaction-type/v1")
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeInvoiceNormalWebImpl {

    private final ISapHandleTransactionTypeInvoiceNormalService sapHandleTransactionTypeInvoiceNormalService;

    @PostMapping(value = "/invoice-normal", produces = {"application/json"})
    public ResponseEntity<String> handleTransactionTypeInvoiceNormal(@RequestBody DtSapTransaction request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        try {
            String status = "S";

            /**
             * @STEP-0 Query sap transaction by transaction id
             */
            DtSapTransaction dtSapTransaction = sapHandleTransactionTypeInvoiceNormalService
                    .queryDtSapTransactionById(request.getSapTranId());

            /**
             * @STEP-1 Query workflow by transaction type
             */
            DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeInvoiceNormalService
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

            // STEP-2.2 Post transaction
            DtSapPostHeader dtSapPostHeader = null;
            List<DtSapCancelReserve> listDtSapCancelReserve = null ;

            if ("Y".equals(dtSapTransactionType.getPostTransaction())) {
                // TODO: query sql to get information
                // TODO: insert table

                dtSapPostHeader = sapHandleTransactionTypeInvoiceNormalService.queryInfoAndInsertPostTransaction(dtSapTransaction);
            }

            // STEP 3 Cancel reserve
 		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
                // TODO: query sql to get information
                // TODO: insert table
            	listDtSapCancelReserve = sapHandleTransactionTypeInvoiceNormalService.cancelReserveInvoiceNormal(dtSapTransaction);

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
            if (BeanUtil.isNotNull(dtSapPostHeader)) {
                DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeInvoiceNormalService.callPostTransactionApi(dtSapPostHeader);

                if ("S".equals(dtSapPostHeaderApi.getStatus())) {
                    status = "S";
                    dtSapPostHeader.setStatus("S");
                } else {
                    status = "F";
                    dtSapPostHeader.setStatus("F");
                }

                // Update status dtSapPostHeader
                sapHandleTransactionTypeInvoiceNormalService.updateDtSapPostHeader(dtSapPostHeader);
            }

            // call service cancel reserve to send input to sap and keep response from sap
 		   if(BeanUtil.isNotEmpty(listDtSapCancelReserve)) {
 			   for(DtSapCancelReserve el : listDtSapCancelReserve ) {
 				   DtSapCancelReserve res = sapHandleTransactionTypeInvoiceNormalService.callCancelReserveApi(el);
 				   
 				  if(res != null) {
					   el.setReserveRes(res.getReserveRes());
					   el.setStatus(res.getStatus());
					   sapHandleTransactionTypeInvoiceNormalService.updatedtSapCancelReserve(el);
				   }
 				   
 				   if(!res.getStatus().equals("S")) {
 					   status = "F";
 				   }
 			   }
 		   }

            /**
             * @STEP-4 Check response from sap then update status table DT_SAP_TRANSACTION
             */
            dtSapTransaction.setStatus(status);
            sapHandleTransactionTypeInvoiceNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

        } catch (Exception e) {
            log.error("handleTransactionTypeInvoiceNormal error ", e);
        }

        return new ResponseEntity<>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
    }
    
    @PostMapping(value = "/cancel-invoice-normal-same-day", produces = {"application/json"})
    public ResponseEntity<String> handleTransactionTypeInvoiceNormalCancelSameDay(@RequestBody DtSapTransaction request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        try {
            String status = "S";

            /**
             * @STEP-0 Query sap transaction by transaction id
             */
            DtSapTransaction dtSapTransaction = sapHandleTransactionTypeInvoiceNormalService
                    .queryDtSapTransactionById(request.getSapTranId());

            /**
             * @STEP-1 Query workflow by transaction type
             */
            DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeInvoiceNormalService
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

            // STEP-2.2 Post transaction
            DtSapPostHeader dtSapPostHeader = null;
            List<DtSapCancelReserve> listDtSapCancelReserve = null ;

            if ("Y".equals(dtSapTransactionType.getPostTransaction())) {
                // TODO: query sql to get information
                // TODO: insert table

                dtSapPostHeader = sapHandleTransactionTypeInvoiceNormalService.queryInfoAndInsertPostTransactionCancelSameDay(dtSapTransaction);
            }

            // STEP 3 Cancel reserve
 		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
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
            if (BeanUtil.isNotNull(dtSapPostHeader)) {
                DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeInvoiceNormalService.callPostTransactionApi(dtSapPostHeader);

                if ("S".equals(dtSapPostHeaderApi.getStatus())) {
                    status = "S";
                    dtSapPostHeader.setStatus("S");
                } else {
                    status = "F";
                    dtSapPostHeader.setStatus("F");
                }

                // Update status dtSapPostHeader
                sapHandleTransactionTypeInvoiceNormalService.updateDtSapPostHeader(dtSapPostHeader);
            }


            /**
             * @STEP-4 Check response from sap then update status table DT_SAP_TRANSACTION
             */
            dtSapTransaction.setStatus(status);
            sapHandleTransactionTypeInvoiceNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

        } catch (Exception e) {
            log.error("handleTransactionTypeInvoiceNormal error ", e);
        }

        return new ResponseEntity<>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
    }

}
