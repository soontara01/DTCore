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

import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeGoodsReplaceService;
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
public class SapHandleTransactionTypeGoodsReplaceWebImpl {

    private final ISapHandleTransactionTypeGoodsReplaceService sapHandleTransactionTypeGoodsReplaceService;

    @PostMapping(value = "/goods-replace", produces = {"application/json"})
    public ResponseEntity<String> handleTransactionTypeGoodsReplace(@RequestBody DtSapTransaction request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        try {
            String status = "S";

            /**
             * @STEP-0 Query sap transaction by transaction id
             */
            DtSapTransaction dtSapTransaction = sapHandleTransactionTypeGoodsReplaceService
                    .queryDtSapTransactionById(request.getSapTranId());

            /**
             * @STEP-1 Query workflow by transaction type
             */
            DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeGoodsReplaceService
                    .queryDtSapTransactionTypeByKey(dtSapTransaction.getTransactionType());

            
            /**
             * @STEP-2 Do workflow
             * @STEP-2.1 Post transaction
             * @STEP-2.2 Cancel reserve
             * Base on config DtSapTransactionType
             */

            
            // STEP-2.1 Post transaction
            DtSapPostHeader dtSapPostHeader = null;
            List<DtSapCancelReserve> listDtSapCancelReserve = null ;

            if ("Y".equals(dtSapTransactionType.getPostTransaction())) {
                // TODO: query sql to get information
                // TODO: insert table

                dtSapPostHeader = sapHandleTransactionTypeGoodsReplaceService.queryInfoAndInsertPostTransaction(dtSapTransaction);
            }

            // STEP-2.2 Cancel reserve
            if ("Y".equals(dtSapTransactionType.getCancelReserve())) {
                // TODO: query sql to get information
                // TODO: insert table
            	listDtSapCancelReserve = sapHandleTransactionTypeGoodsReplaceService.cancelReserveGoodReplace(dtSapTransaction) ;
                // TODO: call service cancel reserve to send input to sap and keep response from sap
            }

            
            /**
             * @STEP-3 Call api sap
             * @STEP-3.1 Call service [post transaction]
             * @STEP-3.2 Call service [cancel reserve]
             */


            // TODO: STEP-3.1 call service [post transaction] to send input to sap and keep response from sap
            if (BeanUtil.isNotNull(dtSapPostHeader)) {
                DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeGoodsReplaceService.callPostTransactionApi(dtSapPostHeader);

                if ("S".equals(dtSapPostHeaderApi.getStatus())) {
                    status = "S";
                    dtSapPostHeader.setStatus("S");
                } else {
                    status = "F";
                    dtSapPostHeader.setStatus("F");
                }

                // Update status dtSapPostHeader
                sapHandleTransactionTypeGoodsReplaceService.updateDtSapPostHeader(dtSapPostHeader);
            }

            // TODO: STEP-3.2 call service [cancel reserve] to send input to sap and keep response from sap
            if(BeanUtil.isNotEmpty(listDtSapCancelReserve)) {
 			   for(DtSapCancelReserve el : listDtSapCancelReserve ) {
 				   DtSapCancelReserve res = sapHandleTransactionTypeGoodsReplaceService.callCancelReserveApi(el);
 				   
 				  if(res != null) {
					   el.setReserveRes(res.getReserveRes());
					   el.setStatus(res.getStatus());
					   sapHandleTransactionTypeGoodsReplaceService.updatedtSapCancelReserve(el);
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
            sapHandleTransactionTypeGoodsReplaceService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

        } catch (Exception e) {
            log.error("handleTransactionTypeGoodsReplace error ", e);
        }

        return new ResponseEntity<>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
    }

}
