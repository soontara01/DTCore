package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.SapPickingReceiptBeen;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractTypeCancelService;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypeCancelWebImpl {
	
	final String prefixPath = "api/sap-handle-contract-type/v1";
	
	private ISapHandleContractTypeCancelService sapHandleContractTypeCancelService;
	
	@RequestMapping(value = prefixPath +"/cancel",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleContractTypeCancel(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		DtSapContractTrans input = new DtSapContractTrans();
		try {

			input = gson.fromJson(jsonRequest, DtSapContractTrans.class);

			String status = "N";
			// step 0 query sap transaction
			DtSapContractTrans dtSapContractTrans = sapHandleContractTypeCancelService.queryDtSapContractTransByKey(input.getSapContractId());

			// step1 Universal
			DtSapUniversalHeader dtSapUniversalHeader = sapHandleContractTypeCancelService.queryInfoAndInsertUniversal(dtSapContractTrans);

			// Step 2 call api sap
			if (dtSapUniversalHeader == null) {
				status = "N";
			} else {
				DtSapUniversalHeader dtSapUniversalHeaderRes = sapHandleContractTypeCancelService.callUniversalApi(dtSapUniversalHeader);

				if ("S".equals(dtSapUniversalHeaderRes.getStatus())) {
					status = "S";

					dtSapUniversalHeader.setStatus("S");
					dtSapUniversalHeader.setMessageType(dtSapUniversalHeaderRes.getMessageType());
					dtSapUniversalHeader.setDocumentNumber(dtSapUniversalHeaderRes.getDocumentNumber());
					dtSapUniversalHeader.setFiscalYear(dtSapUniversalHeaderRes.getFiscalYear());
				} else {
					status = "F";

					dtSapUniversalHeader.setStatus("F");
					dtSapUniversalHeader.setMessageType(dtSapUniversalHeaderRes.getMessageType());
					dtSapUniversalHeader.setDocumentNumber(dtSapUniversalHeaderRes.getDocumentNumber());
					dtSapUniversalHeader.setFiscalYear(dtSapUniversalHeaderRes.getFiscalYear());
				}

				// update status dtSapUniversalHeader
				try {
					sapHandleContractTypeCancelService.updateDtSapUniversalHeader(dtSapUniversalHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}

				//Step 3 Report dt_sap_contract_trans_log
				sapHandleContractTypeCancelService.insertDtSapContractTransLog(dtSapContractTrans, dtSapUniversalHeader);
			}
			
			
		   // Step 4 check response from sap then update status table dt_sap_contract_trans
		   dtSapContractTrans.setStatus(status);
		   sapHandleContractTypeCancelService.updateDtSapContractTransAfterWorkflow(dtSapContractTrans);
		   	
				
		} catch (Exception e) {
			log.info("handleContractTypeCancel error : "+e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson(new SapPickingReceiptBeen("50000",e.getMessage(),e.getMessage())), httpHeaders, HttpStatus.OK);
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}

}
