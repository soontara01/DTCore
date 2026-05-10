package th.co.ais.dt.core.controller.impl.sap;

import java.util.Date;
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
import th.co.ais.dt.controller.dto.SapContractBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionContractPickingService;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypePickingWebImpl {
	final String prefixPath = "api/sap-handle-contract-type/v1";
	
	IDtSapContractTransDao dtSapContractTransDao;
	private final ISapHandleTransactionContractPickingService sapHandleTransactionContractPickingService ;
	
	@RequestMapping(value = prefixPath + "/picking", method = RequestMethod.POST, produces = {"application/json" })
	public ResponseEntity<String> handleTransactionContractPicking(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SapContractBean input = gson.fromJson(jsonRequest, SapContractBean.class);
		String status = "W";
		try {
			//get sapContractTransaction
			DtSapContractTrans sapContractTrans = dtSapContractTransDao.getByLongPrimaryKey(Long.parseLong(input.getSapContractId()));
			
			//Accrual(Picking)
			DtSapAccrualHeader dtSapAccrualHeader = sapHandleTransactionContractPickingService.postAccrualPicking(sapContractTrans);
			
			//Universal(Picking)
			DtSapUniversalHeader dtSapUniversalHeader = sapHandleTransactionContractPickingService.postUniversalPicking(sapContractTrans);
			
			//insert Table : dt_sap_contract_trans_log
			DtSapContractTransLog dtSapContractTransLog = sapHandleTransactionContractPickingService.insertSapContractTransLog(sapContractTrans);
			
			
			//call sap accrual
			if(dtSapAccrualHeader == null) {
				status = "N";
			} else {
				DtSapAccrualHeader dtSapAccrualHeaderRes = sapHandleTransactionContractPickingService.callAccrualApi(dtSapAccrualHeader);
				if ("S".equals(dtSapAccrualHeaderRes.getStatus())) {
					status = "S";
					dtSapAccrualHeader.setStatus("S");
					dtSapAccrualHeader.setResAccrualObjectNumber(dtSapAccrualHeaderRes.getResAccrualObjectNumber());
				} else {
					status = "F";
					dtSapAccrualHeader.setStatus("F");
				}

				// update status dtSapAccrualHeader
				try {
					sapHandleTransactionContractPickingService.updateDtSapAccrualHeader(dtSapAccrualHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			
			//call sap universal
			if(dtSapUniversalHeader == null) {
				status = "N";
			} else {
				DtSapUniversalHeader dtSapUniversalHeaderRes = sapHandleTransactionContractPickingService.callUniversalApi(dtSapUniversalHeader);
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
					sapHandleTransactionContractPickingService.updateDtSapUniversalHeader(dtSapUniversalHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			
			//update Table : dt_sap_contract_trans_log
			dtSapContractTransLog.setAccuralObjectNumber(dtSapAccrualHeader.getResAccrualObjectNumber());
			sapHandleTransactionContractPickingService.updateDtSapContractTransLog(dtSapContractTransLog);
			
			//update sapContractTrans;
			sapContractTrans.setStatus(status);
			sapHandleTransactionContractPickingService.updateDtsapContractTrans(sapContractTrans);
			
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}

		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
	}

}
