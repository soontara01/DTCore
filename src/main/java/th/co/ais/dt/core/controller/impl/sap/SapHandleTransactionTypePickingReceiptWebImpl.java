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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelSaleNormalService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypePickingReceiptService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;


@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypePickingReceiptWebImpl {

	final String prefixPath = "api/sap-handle-transaction-type/v1";
	
	private final ISapHandleTransactionTypePickingReceiptService sapHandleTransactionTypePickingReceiptService ;
	private final ISapHandleTransactionTypeCancelSaleNormalService sapHandleTransactionTypeCancelSaleNormalService;
	
	@RequestMapping(value = prefixPath +"/picking-receipt",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypePickingReceipt(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		DtSapTransaction input = new DtSapTransaction();
		try {
			
		   input = gson.fromJson(jsonRequest, DtSapTransaction.class);
		   
		   String status = "W";
		   // step 0 query sap transaction
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeCancelSaleNormalService.queryDtSapTransactionById(input.getSapTranId());
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypePickingReceiptService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapPostHeader dtSapPostHeader = null ;
		   List<DtSapCancelReserve> listDtSapCancelReserve = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   
			    
			   
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   dtSapPostHeader = sapHandleTransactionTypePickingReceiptService.queryInfoAndInsertPostTransaction(dtSapTransaction);
			   
		   }
		   
		   // 3. cancel reserve 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   listDtSapCancelReserve = sapHandleTransactionTypePickingReceiptService.cancelReserveSale(dtSapTransaction) ;
			   
			    // call service cancel reserve to send input to sap and keep response from sap
		   }
		   
		   
		   //Step 3 call api sap
		   
		   
		   // call service sale order to send input to sap and keep response from sap
		   ///XXXX
		   
		   // call service post transaction to send input to sap and keep response from sap
		   if(dtSapPostHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypePickingReceiptService.callPostTransactionApi(dtSapPostHeader);
			
			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
				   status = "S" ;
				   dtSapPostHeader.setStatus("S");
			   }else {
				   status = "F";
				   dtSapPostHeader.setStatus("F");
			   } 
			   
			   //update status dtSapPostHeader
			   sapHandleTransactionTypePickingReceiptService.updateDtSapPostHeader(dtSapPostHeader);
		   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   if(BeanUtil.isNotEmpty(listDtSapCancelReserve)) {
			   for(DtSapCancelReserve el : listDtSapCancelReserve ) {
				   DtSapCancelReserve res = sapHandleTransactionTypePickingReceiptService.callCancelReserveApi(el);
				   if(res != null) {
					   el.setReserveRes(res.getReserveRes());
					   el.setStatus(res.getStatus());
					   sapHandleTransactionTypePickingReceiptService.updatedtSapCancelReserve(el);
				   }
				  
				   if(!res.getStatus().equals("S")) {
					   status = "F";
				   }
			   }
		   }
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypePickingReceiptService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			log.info("handleTransactionTypePickingReceipt error : "+e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson(new SapPickingReceiptBeen("50000",e.getMessage(),e.getMessage())), httpHeaders, HttpStatus.OK);
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
}
