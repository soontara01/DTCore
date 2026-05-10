package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerService;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/sap-handle-transaction-type/v1")
public class SapHandleTransactionTypeDepositController {

	@Autowired
	private ISapHandleTransactionTypeDepositPartnerService sapHandleTransactionTypeDepositPartnerService;

	@PostMapping("deposit")
	public ResponseEntity<String> handleTransactionTypeDepositShop(@RequestBody DtSapTransaction input) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			String status = "S";
			boolean checke = false ;
			// step 0 query sap transaction
			DtSapTransaction dtSapTransaction = sapHandleTransactionTypeDepositPartnerService
					.queryDtSapTransactionById(input.getSapTranId());

			// step 1 query workflow
			DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeDepositPartnerService
					.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));

			// step2 Do workflow
			DtSapPostHeader dtSapPostHeader = null;
			DtSapOrderHeader dtSapOrderHeader = null ;

			// sale order
			if (BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder())
					&& dtSapTransactionType.getSaleOrder().equals("Y")) {
				// query sql to get infomation
				// insert table
				try {
					dtSapOrderHeader = sapHandleTransactionTypeDepositPartnerService.queryInfoAndInsertSaleorderDepositshop(dtSapTransaction);
				}catch (Exception e) {
					e.printStackTrace();
				}
				

			}
			// post transaction
			if (BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction())
					&& dtSapTransactionType.getPostTransaction().equals("Y")) {
				// query sql to get infomation
				// insert table
				try {
				    dtSapPostHeader = sapHandleTransactionTypeDepositPartnerService
						.query6InfoAndInsertPostTransaction(dtSapTransaction);
				}catch (Exception e) {
					e.printStackTrace();
				}

			}
			
			// call service sale order to send input to sap and keep response from sap
			if (dtSapOrderHeader != null) {
				 checke = true ;
				// call service post transaction to send input to sap and keep response from sap
				DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeDepositPartnerService
						.callSaleorderApi(dtSapOrderHeader);

				if (dtSapOrderHeaderApi.getStatus().equals("S")) {
					//status = "S";
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
				} else {
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

				// update status dtSapPostHeader
				try {
				sapHandleTransactionTypeDepositPartnerService.updateDtSapOrderHeader(dtSapOrderHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			// call service post transaction to send input to sap and keep response from sap
			if (dtSapPostHeader != null) {
				 checke = true ;
				// call service post transaction to send input to sap and keep response from sap
				
				DtSapPostHeader dtSapPostHeaderApi = new DtSapPostHeader(); ;
				
				   if(dtSapOrderHeader != null && 
						   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
						   dtSapOrderHeader.getStatus().equals("S") &&
						   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
					   
					   // update post before call sap
					   sapHandleTransactionTypeDepositPartnerService.updatePostTransactionBeforeCallSap(dtSapPostHeader, dtSapOrderHeader) ;
					   
					   dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader);
						
				   }else {
					   dtSapPostHeaderApi.setStatus("F") ;
				   }

				if (dtSapPostHeaderApi.getStatus().equals("S")) {
					//status = "S";
					dtSapPostHeader.setStatus("S");
				} else {
					status = "F";
					dtSapPostHeader.setStatus("F");
				}

				// update status dtSapPostHeader
				try {
				sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			// Step 4 check response from sap then update status table sap_transaction

			if( checke) {
				dtSapTransaction.setStatus(status);
			}else {
				dtSapTransaction.setStatus("E");
			}
			
			sapHandleTransactionTypeDepositPartnerService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);

	}
	
	@PostMapping("deposit-partner")
	public ResponseEntity<String> handleTransactionTypeDepositPartner(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		DtSapTransaction input = new DtSapTransaction();
		try {
			
		   input = gson.fromJson(jsonRequest, DtSapTransaction.class);
		   
		   String status = "S";
		   boolean checke = false ;
		   
		// step 0 query sap transaction
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionById(input.getSapTranId());
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapPostHeader dtSapPostHeader = null ;
		   DtSapPostHeader dtSapPostHeader2 = null ;
		   DtSapOrderHeader dtSapOrderHeader = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try { 
				   dtSapOrderHeader = sapHandleTransactionTypeDepositPartnerService.queryInfoAndInsertSaleorderDepositPartner(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
 
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   dtSapPostHeader = sapHandleTransactionTypeDepositPartnerService.query6InfoAndInsertPostTransactionDepositPartner(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			  }
			   
              try {
            	  dtSapPostHeader2  = sapHandleTransactionTypeDepositPartnerService.query7InfoAndInsertPostTransaction(dtSapTransaction);
			   }catch (Exception e) {
				   e.printStackTrace();
			  }
			   
			   
			   
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

			if (dtSapOrderHeader != null) {
				checke = true ;
				// call service post transaction to send input to sap and keep response from sap
				DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeDepositPartnerService
						.callSaleorderApi(dtSapOrderHeader);

				if (dtSapOrderHeaderApi.getStatus().equals("S")) {
					//status = "S";
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
				} else {
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

				// update status dtSapPostHeader
				try {
				sapHandleTransactionTypeDepositPartnerService.updateDtSapOrderHeader(dtSapOrderHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		   
		   
		   
		   
		   // call service post transaction to send input to sap and keep response from sap
		   if(dtSapPostHeader!=null) {
			   checke = true ;
			// call service post transaction to send input to sap and keep response from sap
			   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader);
			
			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
				   //status = "S" ;
				   dtSapPostHeader.setStatus("S");
			   }else {
				   status = "F";
				   dtSapPostHeader.setStatus("F");
			   } 
			   
			   //update status dtSapPostHeader
			   try {
				   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
		   }
		   
		   if(dtSapPostHeader2!=null) {
			   checke = true ;
				// call service post transaction to send input to sap and keep response from sap
				   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader2);
				
				   if(dtSapPostHeaderApi.getStatus().equals("S")) {
					   //status = "S" ;
					   dtSapPostHeader2.setStatus("S");
				   }else {
					   status = "F";
					   dtSapPostHeader2.setStatus("F");
				   } 
				   
				   //update status dtSapPostHeader
				   try {
					   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader2);
				   }catch (Exception e) {
					e.printStackTrace();
				   }
				   
			   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   if(checke) {
			   dtSapTransaction.setStatus(status);
		   }else {
			   dtSapTransaction.setStatus("E");
		   }
		   
		   sapHandleTransactionTypeDepositPartnerService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@PostMapping("cancel-deposit-partner")
	public ResponseEntity<String> handleTransactionTypeCancelDepositPartner(@RequestBody String jsonRequest) {
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
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionById(input.getSapTranId());
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapPostHeader dtSapPostHeader = null ;
		   DtSapPostHeader dtSapPostHeader2 = null ;
		   DtSapOrderHeader dtSapOrderHeader = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try { // for typr 65 only
				   dtSapOrderHeader = sapHandleTransactionTypeDepositPartnerService.queryInfoAndUpdateSaleorderDepositPartner(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
 
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   dtSapPostHeader = sapHandleTransactionTypeDepositPartnerService.query6InfoAndInsertPostTransactionCancelDepositPartner(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			  }
			   
              try {
            	  dtSapPostHeader2  = sapHandleTransactionTypeDepositPartnerService.query7InfoAndInsertPostTransactionCancelDepositPartner(dtSapTransaction);
			   }catch (Exception e) {
				   e.printStackTrace();
			  }
			   
			   
			   
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

			if (dtSapOrderHeader != null) {
				// call service post transaction to send input to sap and keep response from sap
				DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeDepositPartnerService
						.callSaleorderApi(dtSapOrderHeader);

				if (dtSapOrderHeaderApi.getStatus().equals("S")) {
					//status = "S";
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
				} else {
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

				// update status dtSapPostHeader
				try {
				sapHandleTransactionTypeDepositPartnerService.updateDtSapOrderHeader(dtSapOrderHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		   
		   
		   
		   
		   // call service post transaction to send input to sap and keep response from sap
		   if(dtSapPostHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader);
			
			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
				   //status = "S" ;
				   dtSapPostHeader.setStatus("S");
			   }else {
				   status = "F";
				   dtSapPostHeader.setStatus("F");
			   } 
			   
			   //update status dtSapPostHeader
			   try {
				   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
		   }
		   
		   if(dtSapPostHeader2!=null) {
				// call service post transaction to send input to sap and keep response from sap
				   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader2);
				
				   if(dtSapPostHeaderApi.getStatus().equals("S")) {
					   //status = "S" ;
					   dtSapPostHeader2.setStatus("S");
				   }else {
					   status = "F";
					   dtSapPostHeader2.setStatus("F");
				   } 
				   
				   //update status dtSapPostHeader
				   try {
					   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader2);
				   }catch (Exception e) {
					e.printStackTrace();
				   }
				   
			   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeDepositPartnerService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	
	@PostMapping("change-info-order")
	public ResponseEntity<String> handleTransactionTypeDepositShopChangeMat(@RequestBody DtSapTransaction input) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

//		Gson gson = new Gson();
//		DtSapTransaction input = new DtSapTransaction();
		try {
			
//		   input = gson.fromJson(jsonRequest, DtSapTransaction.class);
		   
		   String status = "S";
		   
		// step 0 query sap transaction
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionById(input.getSapTranId());
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
//		   DtSapPostHeader dtSapPostHeader = null ;
		   DtSapOrderHeader dtSapOrderHeader = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   dtSapOrderHeader = sapHandleTransactionTypeDepositPartnerService.queryInfoAndUpdateSaleorderDepositshopType13(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
 
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    
			   
			   
			   
		   }
		   
		   // 3. cancel reserve 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
			    
		   }
		   
		   
		   //Step 3 call api sap
		   
		   
		   // call service sale order to send input to sap and keep response from sap
		   ///XXXX

			if (dtSapOrderHeader != null) {
				// call service post transaction to send input to sap and keep response from sap
				DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeDepositPartnerService
						.callSaleorderApi(dtSapOrderHeader);

				if (dtSapOrderHeaderApi.getStatus().equals("S")) {
					//status = "S";
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
				} else {
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

				// update status dtSapPostHeader
				try {
				sapHandleTransactionTypeDepositPartnerService.updateDtSapOrderHeader(dtSapOrderHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		   
		   
		   
		   
		   // call service post transaction to send input to sap and keep response from sap
//		   if(dtSapPostHeader!=null) {
//			// call service post transaction to send input to sap and keep response from sap
//			   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader);
//			
//			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
//				   //status = "S" ;
//				   dtSapPostHeader.setStatus("S");
//			   }else {
//				   status = "F";
//				   dtSapPostHeader.setStatus("F");
//			   } 
//			   
//			   //update status dtSapPostHeader
//			   try {
//				   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader);
//			   }catch (Exception e) {
//				e.printStackTrace();
//			   }
//			  
//		   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeDepositPartnerService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@PostMapping("deposit-partner-change-info-order-diff-dealer")
	public ResponseEntity<String> handleTransactionTypeDepositPartnerChangeInfoDiffDealer(@RequestBody String jsonRequest) {
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
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionById(input.getSapTranId());
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeDepositPartnerService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapPostHeader dtSapPostHeader = null ;
		   DtSapPostHeader dtSapPostHeader2 = null ;
		   DtSapOrderHeader dtSapOrderHeader = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   dtSapOrderHeader = sapHandleTransactionTypeDepositPartnerService.queryInfoAndInsertSaleorderDepositPartnerUpdateOrder(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
 
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {  // DN dealer A
				   dtSapPostHeader = sapHandleTransactionTypeDepositPartnerService.query7InfoAndInsertPostTransactionCancelDepositPartnerDiffDealer(dtSapTransaction);
			   }catch (Exception e) {
				e.printStackTrace();
			  }
			   
              try {  // CN dealer B
            	  dtSapPostHeader2  = sapHandleTransactionTypeDepositPartnerService.query7InfoAndInsertPostTransaction(dtSapTransaction);
			   }catch (Exception e) {
				   e.printStackTrace();
			  }
			   
			   
			   
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

			if (dtSapOrderHeader != null) {
				// call service post transaction to send input to sap and keep response from sap
				DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeDepositPartnerService
						.callSaleorderApi(dtSapOrderHeader);

				if (dtSapOrderHeaderApi.getStatus().equals("S")) {
					//status = "S";
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
				} else {
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

				// update status dtSapPostHeader
				try {
				sapHandleTransactionTypeDepositPartnerService.updateDtSapOrderHeader(dtSapOrderHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		   
		   
		   
		   
		   // call service post transaction to send input to sap and keep response from sap
		   if(dtSapPostHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader);
			
			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
				   //status = "S" ;
				   dtSapPostHeader.setStatus("S");
			   }else {
				   status = "F";
				   dtSapPostHeader.setStatus("F");
			   } 
			   
			   //update status dtSapPostHeader
			   try {
				   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader);
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
		   }
		   
		   if(dtSapPostHeader2!=null) {
				// call service post transaction to send input to sap and keep response from sap
				   DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeDepositPartnerService.callPostTransactionApi(dtSapPostHeader2);
				
				   if(dtSapPostHeaderApi.getStatus().equals("S")) {
					   //status = "S" ;
					   dtSapPostHeader2.setStatus("S");
				   }else {
					   status = "F";
					   dtSapPostHeader2.setStatus("F");
				   } 
				   
				   //update status dtSapPostHeader
				   try {
					   sapHandleTransactionTypeDepositPartnerService.updateDtSapPostHeader(dtSapPostHeader2);
				   }catch (Exception e) {
					e.printStackTrace();
				   }
				   
			   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeDepositPartnerService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
}
