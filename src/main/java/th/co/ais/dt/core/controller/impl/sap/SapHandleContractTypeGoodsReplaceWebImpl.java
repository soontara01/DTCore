package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.List;
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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractTypeGoodsReplaceService;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypeGoodsReplaceWebImpl {
	
	final String prefixPath = "api/sap-handle-contract-type/v1";
	
	private ISapHandleContractTypeGoodsReplaceService sapHandleContractTypeGoodsReplaceService;
	
	@RequestMapping(value = prefixPath +"/goods-replace",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleContractTypeGoodsReplace(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		DtSapContractTrans input = new DtSapContractTrans();
		try {

			input = gson.fromJson(jsonRequest, DtSapContractTrans.class);

			String status = "W";
			// step 0 query sap transaction
			DtSapContractTrans dtSapContractTrans = sapHandleContractTypeGoodsReplaceService.queryDtSapContractTransByKey(input.getSapContractId());

			List<DtSapContractTransLog> listGoodsReplaceContract = sapHandleContractTypeGoodsReplaceService.queryContractGoodsReplace(dtSapContractTrans.getCompany(), dtSapContractTrans.getDocNo());

			if (BeanUtil.isEmpty(listGoodsReplaceContract)) {
				status = "N";
			} else {
				status = "S";

				//Step 3 Report dt_sap_contract_trans_log
				sapHandleContractTypeGoodsReplaceService.insertDtSapContractTransLog(dtSapContractTrans, listGoodsReplaceContract.get(0));
			}
			
			
			// Step 4 check response from sap then update status table dt_sap_contract_trans
			dtSapContractTrans.setStatus(status);
			sapHandleContractTypeGoodsReplaceService.updateDtSapContractTransAfterWorkflow(dtSapContractTrans);
		   	
		} catch (Exception e) {
			log.info("handleContractTypeGoodsReplace error : "+e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson(new SapPickingReceiptBeen("50000",e.getMessage(),e.getMessage())), httpHeaders, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}

}
