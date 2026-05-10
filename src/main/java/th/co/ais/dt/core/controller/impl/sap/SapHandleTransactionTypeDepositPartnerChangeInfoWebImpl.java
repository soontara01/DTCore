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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerChangeInfoService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeDepositPartnerChangeInfoWebImpl {
	final String prefixPath = "api/sap-handle-transaction-type/v1";
	
	private final ISapHandleTransactionTypeDepositPartnerChangeInfoService sapHandleTransactionTypeDepositPartnerChangeInfoService ;
	private final ISapHandleTransactionTypeSaleNormalService sapHandleTransactionTypeSaleNormalService ;
	
	@RequestMapping(value = prefixPath +"/deposit-partner-change-info-order",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeDepositPartnerChangeInfo(@RequestBody String jsonRequest) {
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
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionById(input.getSapTranId());
		   
		   
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapPostHeader dtSapPostHeader = null ;
		   DtSapOrderHeader dtSapOrderHeader = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   if(dtSapTransaction.getTransactionType() == 57 ) {  
					   dtSapOrderHeader = sapHandleTransactionTypeDepositPartnerChangeInfoService.queryInfoAndInsertSaleorder57(dtSapTransaction);
				   }
				   
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
			   
			    
			   
		   }
		   
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			  
			  
			   
		   }
		   
		   // 3. cancel reserve 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   
			    // call service cancel reserve to send input to sap and keep response from sap
		   }
		   
		   
		   //Step 3 call api sap
		   
		   
		   // call service sale order to send input to sap and keep response from sap
		   if(dtSapOrderHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeader);
			
			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
				  // status = "S" ;
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
			   }else {
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
			   
			   //update status dtSapPostHeader
			   try {
				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeader);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
		   }
		   
		   // call service post transaction to send input to sap and keep response from sap

		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
}
