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
import th.co.ais.dt.controller.dto.SapContractBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionContractSaleOutService;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypeSaleOutWebImpl {
	final String prefixPath = "api/sap-handle-contract-type/v1";
	
	IDtSapContractTransDao dtSapContractTransDao;
	ISapHandleTransactionContractSaleOutService sapHandleTransactionContractSaleOutService;
	
	@RequestMapping(value = prefixPath + "/saleout", method = RequestMethod.POST, produces = {"application/json" })
	public ResponseEntity<String> handleTransactionContractSaleOut(@RequestBody String jsonRequest) {
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
			
			//Accrual(Receipt)
			DtSapAccrualHeader dtSapAccrualHeader = sapHandleTransactionContractSaleOutService.postAccrualReceipt(sapContractTrans);
			
			//Universal(Receipt)
			DtSapUniversalHeader dtSapUniversalHeader = sapHandleTransactionContractSaleOutService.postUniversalReceipt(sapContractTrans);
			
			//insert Table : dt_sap_contract_trans_log
			DtSapContractTransLog dtSapContractTransLog = sapHandleTransactionContractSaleOutService.insertSapContractTransLog(sapContractTrans);
			
			//call sap accrual
			String resAccrualObjectNumber = null;
			if(dtSapAccrualHeader == null) {
				status = "N";
			} else {
				DtSapAccrualHeader dtSapAccrualHeaderRes = sapHandleTransactionContractSaleOutService.callAccrualApi(dtSapAccrualHeader);
				if ("S".equals(dtSapAccrualHeaderRes.getStatus())) {
					status = "S";
					dtSapAccrualHeader.setStatus("S");
					resAccrualObjectNumber = dtSapAccrualHeaderRes.getResAccrualObjectNumber();
					dtSapAccrualHeader.setResAccrualObjectNumber(resAccrualObjectNumber);
				} else {
					status = "F";
					dtSapAccrualHeader.setStatus("F");
				}

				// update status dtSapAccrualHeader
				try {
					sapHandleTransactionContractSaleOutService.updateDtSapAccrualHeader(dtSapAccrualHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			
			//call sap universal
			if(dtSapUniversalHeader == null) {
				status = "N";
			} else {
				DtSapUniversalHeader dtSapUniversalHeaderRes = sapHandleTransactionContractSaleOutService.callUniversalApi(dtSapUniversalHeader);
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
					sapHandleTransactionContractSaleOutService.updateDtSapUniversalHeader(dtSapUniversalHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}	
			
			//update Table : dt_sap_contract_trans_log
			dtSapContractTransLog.setAccuralObjectNumber(resAccrualObjectNumber);
			sapHandleTransactionContractSaleOutService.updateDtSapContractTransLog(dtSapContractTransLog);
			
			//update sapContractTrans;
			sapContractTrans.setStatus(status);
			sapHandleTransactionContractSaleOutService.updateDtsapContractTrans(sapContractTrans);
			
			
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}

		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
	}
}
