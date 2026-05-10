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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeEsimPartnerService;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeEsimPartnerWebImpl {
	final String prefixPath = "api/sap-handle-transaction-type/v1";
	
	private final ISapHandleTransactionTypeEsimPartnerService sapHandleTransactionTypeEsimPartnerService ;
	
	@RequestMapping(value = prefixPath +"/esim-partner",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeSaleNormal(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		DtSapTransaction input = new DtSapTransaction();
		try {
			
		   input = gson.fromJson(jsonRequest, DtSapTransaction.class);
		   
		   String status = "S";
		   
		  // step 0 query sap transaction
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeEsimPartnerService.queryDtSapTransactionById(input.getSapTranId());
		   
		   
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeEsimPartnerService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapPostHeader dtSapPostHeader = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   
			    
			   
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   dtSapPostHeader = sapHandleTransactionTypeEsimPartnerService.queryInfoAndInsertPostTransaction(dtSapTransaction);
			   
		   }
		   
		   // 3. cancel reserve 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   
			    // call service cancel reserve to send input to sap and keep response from sap
		   }
		   
		   
		   //Step 3 call api sap
		   
		   
		   // call service sale order to send input to sap and keep response from sap
		   ///XXXX
		   
		   // call service post transaction to send input to sap and keep response from sap
		   if(dtSapPostHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeEsimPartnerService.callPostTransactionApi(dtSapPostHeader);
			
			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
				   status = "S" ;
				   dtSapPostHeader.setStatus("S");
			   }else {
				   status = "F";
				   dtSapPostHeader.setStatus("F");
			   } 
			   
			   //update status dtSapPostHeader
			   sapHandleTransactionTypeEsimPartnerService.updateDtSapPostHeader(dtSapPostHeader);
		   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeEsimPartnerService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
}
