package th.co.ais.dt.core.controller.impl.sap;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractTypeCancelGoodsReturnService;
import th.co.ais.dt.entity.pm.CreditNote;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sap-handle-contract-type/v1")
@Slf4j
@RequiredArgsConstructor
public class SapHandleContractTypeCancelGoodsReturnWebImpl {

    private final ISapHandleContractTypeCancelGoodsReturnService sapHandleContractTypeCancelGoodsReturnService;

    @PostMapping(value = "/cancel-goods-return", produces = {"application/json"})
    public ResponseEntity<String> handleContractTypeCancelGoodsReturn(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        Gson gson = new Gson();
        try {
            DtSapContractTrans request = gson.fromJson(jsonRequest, DtSapContractTrans.class);

            // Get sap contract trans
            DtSapContractTrans dtSapContractTrans = sapHandleContractTypeCancelGoodsReturnService
                    .queryDtSapContractTransByKey(request.getSapContractId());

            // Get ReceiptNum From CnNum
            String receiptNum = sapHandleContractTypeCancelGoodsReturnService
                    .getReceiptNumFromCnNum(dtSapContractTrans.getDocNo(), dtSapContractTrans.getCompany());

            // Accrual
            DtSapAccrualHeader dtSapAccrualHeader = sapHandleContractTypeCancelGoodsReturnService
                    .queryInfoAndInsertAccrual(dtSapContractTrans);

            // Universal
            DtSapUniversalHeader dtSapUniversalHeader = sapHandleContractTypeCancelGoodsReturnService
                    .queryInfoAndInsertUniversal(dtSapContractTrans);
            
            // Query CN By Receipt
            List<CreditNote> creditNotes = sapHandleContractTypeCancelGoodsReturnService
                    .queryCnReceipt(receiptNum, dtSapContractTrans.getCompany());

            // Set Receipt num FOR Cancel
            String cancelReceiptNum = receiptNum + "_" + creditNotes.size();

            String status = "N";

            // Call sap accrual
            if (BeanUtil.isNotNull(dtSapAccrualHeader)) {
                DtSapAccrualHeader dtSapAccrualHeaderRes = sapHandleContractTypeCancelGoodsReturnService
                        .callCancelGoodsReturnAccrualApi(dtSapAccrualHeader, cancelReceiptNum);
                
                if ("S".equals(dtSapAccrualHeaderRes.getStatus())) {
                    status = "S";
                    dtSapAccrualHeader.setStatus("S");
                    dtSapAccrualHeader.setResAccrualObjectNumber(dtSapAccrualHeaderRes.getResAccrualObjectNumber());
                } else {
                    status = "F";
                    dtSapAccrualHeader.setStatus("F");
                }

                // Update status dtSapAccrualHeader
                sapHandleContractTypeCancelGoodsReturnService.updateDtSapAccrualHeader(dtSapAccrualHeader);
            }

            // Call sap universal
            if (BeanUtil.isNotNull(dtSapUniversalHeader)) {
                DtSapUniversalHeader dtSapUniversalHeaderRes = sapHandleContractTypeCancelGoodsReturnService
                        .callCancelGoodsReturnUniversalApi(dtSapUniversalHeader, cancelReceiptNum);
                
                if ("S".equals(dtSapUniversalHeaderRes.getStatus())) {
                    status = "S";
                    dtSapUniversalHeader.setStatus("S");
                } else {
                    status = "F";
                    dtSapUniversalHeader.setStatus("F");
                }
                dtSapUniversalHeader.setMessageType(dtSapUniversalHeaderRes.getMessageType());
                dtSapUniversalHeader.setDocumentNumber(dtSapUniversalHeaderRes.getDocumentNumber());
                dtSapUniversalHeader.setFiscalYear(dtSapUniversalHeaderRes.getFiscalYear());

                // Update status dtSapUniversalHeader
                sapHandleContractTypeCancelGoodsReturnService.updateDtSapUniversalHeader(dtSapUniversalHeader);
            }

            // Insert sap contract trans log
            if ("S".equals(status)) {
                sapHandleContractTypeCancelGoodsReturnService.insertSapContractTransLog(dtSapContractTrans, dtSapAccrualHeader, dtSapUniversalHeader, receiptNum);
            }

            // Update contract trans
            dtSapContractTrans.setStatus(status);
            sapHandleContractTypeCancelGoodsReturnService.updateDtSapContractTransAfterWorkflow(dtSapContractTrans);

        } catch (Exception e) {
            log.error("handleContractTypeCancelGoodsReturn error ", e);
            return new ResponseEntity<>(new Gson().toJson(
                    new CommonResponseBean("50000", e.getMessage(), e.getMessage())
            ), httpHeaders, HttpStatus.OK);
        }

        return new ResponseEntity<>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
    }
}
