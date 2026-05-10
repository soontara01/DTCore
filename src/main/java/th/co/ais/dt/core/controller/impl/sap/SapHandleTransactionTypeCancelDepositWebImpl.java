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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelDepositService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelSaleNormalService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeMonitorAndApproveService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCancelDepositWebImpl {


	final String prefixPath = "api/sap-handle-transaction-type/v1";
	
	private final ISapHandleTransactionTypeCancelDepositService sapHandleTransactionTypeCancelDepositService ;
	private final ISapHandleTransactionTypeCancelSaleNormalService sapHandleTransactionTypeCancelSaleNormalService;
	
	@RequestMapping(value = prefixPath +"/cancel-deposit-ais-shop",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeCancelDepositAisShop(@RequestBody String jsonRequest) {
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
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeCancelSaleNormalService.queryDtSapTransactionById(input.getSapTranId()); // 17
		   
		   // step 1 query workflow
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeCancelDepositService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
			
		   // step2 Do workflow 
		   
			   // seq  1. sale order 
			   // seq  2. post transaction 
			   // seq  3. cancel reserve
			   // base on config DtSapTransactionType
		   
		   DtSapOrderHeader dtSapOrderHeader = null ;
		   DtSapOrderHeader dtSapOrderHeaderCNMemo = null ;
		   DtSapPostHeader dtSapPostHeader = null;	
		   DtSapOrderHeader dtSapOrderHeaderUnLock = null ;
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   if(dtSapTransaction.getTransactionType() == 17) {  // 66  no need to reject sale order 
				   //update 15012026 commen unLockOrderBeforeUpdate17
				   dtSapOrderHeaderUnLock = sapHandleTransactionTypeCancelDepositService.unLockOrderBeforeUpdate17(dtSapTransaction);	
				   dtSapOrderHeader = sapHandleTransactionTypeCancelDepositService.queryInfoAndInsertSalesOrderTransactionCancel(dtSapTransaction);	
			   }
			   
			   dtSapOrderHeaderCNMemo = sapHandleTransactionTypeCancelDepositService.queryInfoAndInsertSalesOrderTransactionCancelCNMemo(dtSapTransaction);	
			   
		   }
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   dtSapPostHeader = sapHandleTransactionTypeCancelDepositService.queryInfoAndInsertPostTransactionDepositCancel(dtSapTransaction);	    
			   
		   }
		   
		   // 3. cancel reserve 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   
			   
			    // call service cancel reserve to send input to sap and keep response from sap
		   }
		   
		   
		   //Step 3 call api sap
		   
		   if(dtSapOrderHeaderUnLock!=null) { // unlock order 
			 
			   
			   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeCancelDepositService.callSaleorderApi(dtSapOrderHeaderUnLock);
			
			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
				   status = "S" ;
				   dtSapOrderHeaderUnLock.setStatus("S");
				   dtSapOrderHeaderUnLock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
				   dtSapOrderHeaderUnLock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
				   dtSapOrderHeaderUnLock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
				   dtSapOrderHeaderUnLock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
				   dtSapOrderHeaderUnLock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
				   dtSapOrderHeaderUnLock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
				   dtSapOrderHeaderUnLock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
				   dtSapOrderHeaderUnLock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
				   dtSapOrderHeaderUnLock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
				   dtSapOrderHeaderUnLock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   
			   }else {
				   status = "F";
				   dtSapOrderHeaderUnLock.setStatus("F");
				   dtSapOrderHeaderUnLock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
				   dtSapOrderHeaderUnLock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
				   dtSapOrderHeaderUnLock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
				   dtSapOrderHeaderUnLock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
				   dtSapOrderHeaderUnLock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
				   dtSapOrderHeaderUnLock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
				   dtSapOrderHeaderUnLock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
				   dtSapOrderHeaderUnLock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
				   dtSapOrderHeaderUnLock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
				   dtSapOrderHeaderUnLock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
				   dtSapOrderHeaderUnLock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
			   } 
			   
			   //update status dtSapPostHeader
			   try {
				   sapHandleTransactionTypeCancelDepositService.updateDtSapOrderHeader(dtSapOrderHeaderUnLock);
			   }catch (Exception e) {
				e.printStackTrace();
			   }  
	       }
		   
		   
		   // call service Sale Order to send input to sap and keep response from sap
			if (dtSapOrderHeader != null) {
				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelDepositService.callSaleorderApi(dtSapOrderHeader);

				if ("S".equals(dtSapOrderHeaderrApi.getStatus())) {
					status = "S" ;
					
				   dtSapOrderHeader.setStatus("S");
				   dtSapOrderHeader.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
				   dtSapOrderHeader.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
				   dtSapOrderHeader.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
				   dtSapOrderHeader.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
				   dtSapOrderHeader.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
				   dtSapOrderHeader.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
				   dtSapOrderHeader.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
				   dtSapOrderHeader.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
				   dtSapOrderHeader.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
				   dtSapOrderHeader.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
				   dtSapOrderHeader.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
				   dtSapOrderHeader.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
				   dtSapOrderHeader.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
				   dtSapOrderHeader.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
				} else {
					status = "F";
					//dtSapOrderHeader.setStatus("F");
					
				   dtSapOrderHeader.setStatus("F");
				   dtSapOrderHeader.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
				   dtSapOrderHeader.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
				   dtSapOrderHeader.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
				   dtSapOrderHeader.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
				   dtSapOrderHeader.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
				   dtSapOrderHeader.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
				   dtSapOrderHeader.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
				   dtSapOrderHeader.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
				   dtSapOrderHeader.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
				   dtSapOrderHeader.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
				   dtSapOrderHeader.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
				   dtSapOrderHeader.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
				   dtSapOrderHeader.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
				   dtSapOrderHeader.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
				}

				// update status dtSapPostHeader
				try {
					sapHandleTransactionTypeCancelDepositService.updateDtSapOrderHeader(dtSapOrderHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			//CN MEMO
			if (dtSapOrderHeaderCNMemo != null && status.equals("S")) {
				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelDepositService.callSaleorderApi(dtSapOrderHeaderCNMemo);

				if ("S".equals(dtSapOrderHeaderrApi.getStatus())) {
					status = "S" ;
					
					dtSapOrderHeaderCNMemo.setStatus("S");
					dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
					dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
					dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
					dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
					dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
					dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
					dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
					dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
					dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
					dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
				} else {
					status = "F";
					//dtSapOrderHeader.setStatus("F");
					
					dtSapOrderHeaderCNMemo.setStatus("F");
					dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
					dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
					dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
					dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
					dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
					dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
					dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
					dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
					dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
					dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
				}

				// update status dtSapPostHeader
				try {
					sapHandleTransactionTypeCancelDepositService.updateDtSapOrderHeader(dtSapOrderHeaderCNMemo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(dtSapTransaction.getTransactionType() == 66 && dtSapOrderHeaderCNMemo != null ) {

				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelDepositService.callSaleorderApi(dtSapOrderHeaderCNMemo);

				if ("S".equals(dtSapOrderHeaderrApi.getStatus())) {
					status = "S" ;
					
					dtSapOrderHeaderCNMemo.setStatus("S");
					dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
					dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
					dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
					dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
					dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
					dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
					dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
					dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
					dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
					dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
				} else {
					status = "F";
					//dtSapOrderHeader.setStatus("F");
					
					dtSapOrderHeaderCNMemo.setStatus("F");
					dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
					dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
					dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
					dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
					dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
					dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
					dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
					dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
					dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
					dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
					dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
				}

				// update status dtSapPostHeader
				try {
					sapHandleTransactionTypeCancelDepositService.updateDtSapOrderHeader(dtSapOrderHeaderCNMemo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			
			
			// call service Post Transaction to send input to sap and keep response from sap
			if (dtSapPostHeader != null ) {
				
				if(status.equals("S")) {
					//update post item before call sap
					sapHandleTransactionTypeCancelDepositService.updatePostTransactionBeforeCallSap(dtSapPostHeader, dtSapOrderHeaderCNMemo) ;
					
					// call service post transaction to send input to sap and keep response from sap
					DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeCancelDepositService
							.callPostTransactionApi(dtSapPostHeader);

					if (dtSapPostHeaderApi.getStatus().equals("S")) {
						status = "S";
						dtSapPostHeader.setStatus("S");
					} else {
						status = "F";
						dtSapPostHeader.setStatus("F");
					}
				}else {
					status = "F";
					dtSapPostHeader.setStatus("F");
				}
				

				// update status dtSapPostHeader
				try {
					sapHandleTransactionTypeCancelDepositService.updateDtSapPostHeader(dtSapPostHeader);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		      
		   // Step 4 check response from sap then update status table sap_transaction
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeCancelSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			log.info("handleTransactionTypePickingReceipt error : "+e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson(new SapPickingReceiptBeen("50000",e.getMessage(),e.getMessage())), httpHeaders, HttpStatus.OK);
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}


}
