package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelDepositService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelSaleNormalService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelSaleSameDayService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeSaleNormalWebImpl {
	final String prefixPath = "api/sap-handle-transaction-type/v1";
	
	private final ISapHandleTransactionTypeSaleNormalService sapHandleTransactionTypeSaleNormalService ;
	private final ISapHandleTransactionTypeCancelDepositService sapHandleTransactionTypeCancelDepositService ;
	private final ISapHandleTransactionTypeCancelSaleNormalService sapHandleTransactionTypeCancelSaleNormalService;
	private final ISapHandleTransactionTypeCancelSaleSameDayService sapHandleTransactionTypeCancelSaleSameDayService;
	private ISapHandleTransactionTypeDepositPartnerService sapHandleTransactionTypeDepositPartnerService;

	
	@RequestMapping(value = prefixPath +"/sale-normal",method = RequestMethod.POST, produces = { "application/json" })
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
		   boolean checke = false ;
		   
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
		   DtSapOrderHeader dtSapOrderHeaderUnLock = null ;
		   List<DtSapCancelReserve> listDtSapCancelReserve = null ;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   if(dtSapTransaction.getTransactionType() == 18 || 
					  dtSapTransaction.getTransactionType() == 19 ||
					  dtSapTransaction.getTransactionType() == 26) {  
					   dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder1819(dtSapTransaction);
				   }else if(dtSapTransaction.getTransactionType() == 14 || dtSapTransaction.getTransactionType() == 61) {
					   dtSapOrderHeaderUnLock = sapHandleTransactionTypeSaleNormalService.unLockOrderBeforeUpdate14(dtSapTransaction) ;
					   dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder14(dtSapTransaction) ;
				   }else if(dtSapTransaction.getTransactionType() == 28 || dtSapTransaction.getTransactionType() == 62) {
					   dtSapOrderHeaderUnLock = sapHandleTransactionTypeSaleNormalService.unLockOrderBeforeUpdate28(dtSapTransaction) ;
					   dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder28(dtSapTransaction) ;
				   }else if(dtSapTransaction.getTransactionType() == 67 || dtSapTransaction.getTransactionType() == 69) {
					   dtSapOrderHeaderUnLock = sapHandleTransactionTypeSaleNormalService.unLockOrderBeforeUpdate67(dtSapTransaction) ;
					   dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder67(dtSapTransaction) ;
				   }
				   
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
			   
			    
			   
		   }
		   
		   
		   // 2. post transaction 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   if(dtSapTransaction.getTransactionType() == 14 ) {
					   if(dtSapOrderHeader!=null) {
						   dtSapPostHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertPostTransaction14(dtSapTransaction);

					   }else { // depositNo used more than 1 time , no need to create sale order 
						   dtSapPostHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertPostTransaction14WithoutSaleOrder(dtSapTransaction);
					   }
				   }else if(dtSapTransaction.getTransactionType() == 67 ) {
					   if(dtSapOrderHeader!=null) {
						   dtSapPostHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertPostTransaction(dtSapTransaction , null);

					   }else { // depositNo used more than 1 time , no need to create sale order 
						   dtSapPostHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertPostTransaction(dtSapTransaction , 1L);
					   }
				   }
				   else {
					   dtSapPostHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertPostTransaction(dtSapTransaction , null);
				   }
				   
			   }catch (Exception e) {
				e.printStackTrace();
			   }
			  
			   
		   }
		   
		   // 3. cancel reserve 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getCancelReserve()) && dtSapTransactionType.getCancelReserve().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   listDtSapCancelReserve = sapHandleTransactionTypeSaleNormalService.cancelReserveSale(dtSapTransaction) ;
			   
			    // call service cancel reserve to send input to sap and keep response from sap
		   }
		   
		   
		   //Step 3 call api sap
		   
		   if(dtSapOrderHeaderUnLock!=null) { // unlock order 
				   checke = true ;
				   
				   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderUnLock);
				
				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
					  // status = "S" ;
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
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderUnLock);
				   }catch (Exception e) {
					e.printStackTrace();
				   }  
		   }
		   
		   
		   // call service sale order to send input to sap and keep response from sap
		   if(dtSapOrderHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   checke = true ;
			   
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
		   if(dtSapPostHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   checke = true ;
			   
			   DtSapPostHeader dtSapPostHeaderApi = new DtSapPostHeader(); ;
			   
			   if(dtSapTransaction.getTransactionType() == 18 || dtSapTransaction.getTransactionType() == 19 ) {
				   if(dtSapOrderHeader != null && 
						   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
						   dtSapOrderHeader.getStatus().equals("S") &&
						   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
					   
					   sapHandleTransactionTypeSaleNormalService.updatePostTransactionBeforeCallSap(dtSapPostHeader, dtSapOrderHeader) ;
					   dtSapPostHeaderApi = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(dtSapPostHeader);
						
				   }else {
					   dtSapPostHeaderApi.setStatus("F") ;
				   }
			   }
			   else if(dtSapTransaction.getTransactionType() == 14 || 
					   dtSapTransaction.getTransactionType() == 28 ||
					   dtSapTransaction.getTransactionType() == 67) {
				   if(dtSapOrderHeader != null) {
					   
					   if(dtSapOrderHeader != null && 
							   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
							   dtSapOrderHeader.getStatus().equals("S") &&
							   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
						   
						// update post before call sap
						   sapHandleTransactionTypeSaleNormalService.updatePostTransactionBeforeCallSapType14(dtSapPostHeader, dtSapOrderHeader) ;
						   dtSapPostHeaderApi = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(dtSapPostHeader);
							
					   }else {
						   dtSapPostHeaderApi.setStatus("F") ;
					   }

				   }else {
					   dtSapPostHeaderApi = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(dtSapPostHeader);
				   }  
			   }
			   else {
				    dtSapPostHeaderApi = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(dtSapPostHeader);
			   }
			   
			   if(dtSapPostHeaderApi.getStatus().equals("S")) {
					  // status = "S" ;
					   dtSapPostHeader.setStatus("S");
				   }else {
					   status = "F";
					   dtSapPostHeader.setStatus("F");
				   } 
			   
			   
			   //update status dtSapPostHeader
			   try {
				   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(dtSapPostHeader);
			   }catch (Exception e) {
				  e.printStackTrace();
			   }
			   
		   }
		   
		   // call service cancel reserve to send input to sap and keep response from sap
		   //XXXX
		   if(BeanUtil.isNotEmpty(listDtSapCancelReserve)) {
			   for(DtSapCancelReserve el : listDtSapCancelReserve ) {
				   DtSapCancelReserve res = sapHandleTransactionTypeSaleNormalService.callCancelReserveApi(el);
				   if(res != null) {
					   el.setReserveRes(res.getReserveRes());
					   el.setStatus(res.getStatus());
					   sapHandleTransactionTypeSaleNormalService.updatedtSapCancelReserve(el);
				   }
				   
				   if(!res.getStatus().equals("S")) {
					   status = "F";
				   }
			   }
		   }
		   
		   
		   
		   // Step 4 check response from sap then update status table sap_transaction
		   if(checke ) {
			   dtSapTransaction.setStatus(status);
		   }else {
			   dtSapTransaction.setStatus("E");
		   }
		  
		   sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/batch-handle-failed",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> batchHnadleFailed(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		try {
			
		 // step 1 query sap transaction status = F
			List<DtSapTransaction> listDtSapTransaction = sapHandleTransactionTypeSaleNormalService.getDtSapTransactionStatusF();
			
			if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
				for(DtSapTransaction elDtSapTransaction : listDtSapTransaction) {
					try {
						String status = "S";
						//String statusToUpdate = "S";
						
						// step 1 query workflow
						DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionTypeByKey(Long.valueOf(elDtSapTransaction.getTransactionType()));
						
						DtSapPostHeader dtSapPostHeader = null;
						DtSapOrderHeader dtSapOrderHeader = null;
						DtSapOrderHeader dtSapOrderHeaderCNMemo = null ;
						
						boolean noSaleorder = true ;
						//boolean orderderWithCNmemoFlg = false ;
						// query sale order F and call sap
						if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
							List<DtSapOrderHeader> listDtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryDtSapOrderHeaderBySapTranId(elDtSapTransaction.getSapTranId());
						    if(BeanUtil.isNotEmpty(listDtSapOrderHeader)) { // posible max size = 2  type 17 ,22,23 size = 2 
						    	noSaleorder = false ;
						    	if(listDtSapOrderHeader.size() == 1) {
						    		if(listDtSapOrderHeader.get(0).getStatus().equals("F")) {
						    			dtSapOrderHeader = listDtSapOrderHeader.get(0) ;
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
						 				   
						 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
						 					  status = "F";
							 				  dtSapOrderHeader.setStatus("F");
						 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
						 					  dtSapOrderHeader.setStatus("S");
						 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
						 					 dtSapOrderHeader.setRes_SalesOrderDocument(saleOrder);
						 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
							 					  status = "F";
								 				  dtSapOrderHeader.setStatus("F");
							 			   } 
						 				   else {
						 					  status = "FF";
							 				  dtSapOrderHeader.setStatus("FF");
						 				   }
						 				   
						 				   
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
						    		}else {
						    			dtSapOrderHeader = listDtSapOrderHeader.get(0) ;
						    		}
						    	}else {  // size = 2 or 3
						    		if(elDtSapTransaction.getTransactionType() == 14 ||
						    				elDtSapTransaction.getTransactionType() == 24 || 
						    				elDtSapTransaction.getTransactionType() == 25 || 
											 elDtSapTransaction.getTransactionType() == 28 || 
											 elDtSapTransaction.getTransactionType() == 51 || 
											 elDtSapTransaction.getTransactionType() == 67) {  
						    			// unblock before update sale order
							    		for (int i = 0 ; i < listDtSapOrderHeader.size() ; i++  ) {
							    			if(i == 0) {  // order unblock before update sale order  ** type 51 no need to unlock
							    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

							    					DtSapOrderHeader dtSapOrderHeaderTmp = listDtSapOrderHeader.get(0) ;
									    			DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderTmp);
									    			
									 			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
									 				  // status = "S" ;
									 				  dtSapOrderHeaderTmp.setStatus("S");
									 				  dtSapOrderHeaderTmp.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
									 				  dtSapOrderHeaderTmp.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
									 				  dtSapOrderHeaderTmp.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
									 				  dtSapOrderHeaderTmp.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
									 				  dtSapOrderHeaderTmp.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
									 				  dtSapOrderHeaderTmp.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
									 				  dtSapOrderHeaderTmp.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
									 				  dtSapOrderHeaderTmp.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
									 				  dtSapOrderHeaderTmp.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
									 				  dtSapOrderHeaderTmp.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
									 				   
									 			   }else {
									 				   
									 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
									 					  status = "F";
									 					 dtSapOrderHeaderTmp.setStatus("F");
									 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
									 					  dtSapOrderHeaderTmp.setStatus("S");
									 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
									 					 dtSapOrderHeaderTmp.setRes_SalesOrderDocument(saleOrder);
									 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
										 					  status = "F";
										 					 dtSapOrderHeaderTmp.setStatus("F");
										 			   } 
									 				   else {
									 					  status = "FF";
									 					 dtSapOrderHeaderTmp.setStatus("FF");
									 				   }
									 				   
									 				   
									 				  dtSapOrderHeaderTmp.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
									 				  dtSapOrderHeaderTmp.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
									 				  dtSapOrderHeaderTmp.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
									 				  dtSapOrderHeaderTmp.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
									 				  dtSapOrderHeaderTmp.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
									 				  dtSapOrderHeaderTmp.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
									 				  dtSapOrderHeaderTmp.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
									 				  dtSapOrderHeaderTmp.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
									 				  dtSapOrderHeaderTmp.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
									 				  dtSapOrderHeaderTmp.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
									 			   } 
									 			   
									 			   //update status dtSapPostHeader
									 			   try {
									 				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderTmp);
									 			   }catch (Exception e) {
									 				e.printStackTrace();
									 			   }
									    		
									    		}
							    			}else { // i = 1
							    				//sale order for update post
							    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

							    					dtSapOrderHeader = listDtSapOrderHeader.get(i) ;  // dtSapOrderHeader  for update post tran
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
									 				   
									 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
									 					  status = "F";
									 					 dtSapOrderHeader.setStatus("F");
									 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
									 					  dtSapOrderHeader.setStatus("S");
									 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
									 					 dtSapOrderHeader.setRes_SalesOrderDocument(saleOrder);
									 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
										 					  status = "F";
										 					 dtSapOrderHeader.setStatus("F");
										 			   } 
									 				   else {
									 					  status = "FF";
									 					 dtSapOrderHeader.setStatus("FF");
									 				   }
									 				   
									 				   
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
									    		
									    		}else {
									    			dtSapOrderHeader = listDtSapOrderHeader.get(i) ;
									    		}
							    			
							    			}
								    		
								    	}
						    		}else {
							    		//orderderWithCNmemoFlg = true ;
						    			// size = 3   type 17 , 22 ,23  click and collect 
						    			// XXX cancel this flow 17,22,23  size = 2  update 15012026
						    			// size = 2   type  22 ,23  click and ship  no need un yourref
						    			if(listDtSapOrderHeader.size() == 2) {
						    	    		for (int i = 0 ; i < listDtSapOrderHeader.size() ; i++  ) {
								    			if(i == 0) {
								    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

										    			dtSapOrderHeader = listDtSapOrderHeader.get(0) ;
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
										 				   
										 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
										 					  status = "F";
											 				  dtSapOrderHeader.setStatus("F");
										 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
										 					  dtSapOrderHeader.setStatus("S");
										 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
										 					 dtSapOrderHeader.setRes_SalesOrderDocument(saleOrder);
										 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
											 					  status = "F";
												 				  dtSapOrderHeader.setStatus("F");
											 			   } 
										 				   else {
										 					  status = "FF";
											 				  dtSapOrderHeader.setStatus("FF");
										 				   }
										 				   
										 				   
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
										    		
										    		}else {
										    			dtSapOrderHeader = listDtSapOrderHeader.get(i) ;
										    		}
								    			}else { // i = 1

								    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

								    					dtSapOrderHeaderCNMemo = listDtSapOrderHeader.get(i) ;
										    			DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderCNMemo);
										    			
										 			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
										 				  // status = "S" ;
										 				  dtSapOrderHeaderCNMemo.setStatus("S");
										 				 dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
										 				dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
										 				dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
										 				dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
										 				dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
										 				dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
										 				dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
										 				dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
										 				dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
										 				dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
										 				   
										 			   }else {
										 				   
										 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
										 					  status = "F";
										 					 dtSapOrderHeaderCNMemo.setStatus("F");
										 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
										 					  dtSapOrderHeaderCNMemo.setStatus("S");
										 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
										 					 dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(saleOrder);
										 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
											 					  status = "F";
											 					 dtSapOrderHeaderCNMemo.setStatus("F");
											 			   } 
										 				   else {
										 					  status = "FF";
										 					 dtSapOrderHeaderCNMemo.setStatus("FF");
										 				   }
										 				   
										 				   
										 				  dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
										 				 dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
										 				dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
										 				dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
										 				dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
										 				dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
										 				dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
										 				dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
										 				dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
										 			   } 
										 			   
										 			   //update status dtSapPostHeader
										 			   try {
										 				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderCNMemo);
										 			   }catch (Exception e) {
										 				e.printStackTrace();
										 			   }
										    		
										    		}else {
										    			dtSapOrderHeaderCNMemo = listDtSapOrderHeader.get(i) ;
										    		}
								    			
								    			}
									    		
									    	}
						    			}else {
                                             //  listDtSapOrderHeader.size()  = 3
						    	    		for (int i = 0 ; i < listDtSapOrderHeader.size() ; i++  ) {
								    			if(i == 0) { // your ref = /
								    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

								    					DtSapOrderHeader dtSapOrderHeaderYour = listDtSapOrderHeader.get(0) ;
										    			DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderYour);
										    			
										 			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
										 				  // status = "S" ;
										 				  dtSapOrderHeaderYour.setStatus("S");
										 				 dtSapOrderHeaderYour.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
										 				dtSapOrderHeaderYour.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
										 				dtSapOrderHeaderYour.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
										 				dtSapOrderHeaderYour.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
										 				dtSapOrderHeaderYour.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
										 				dtSapOrderHeaderYour.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
										 				dtSapOrderHeaderYour.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
										 				dtSapOrderHeaderYour.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
										 				dtSapOrderHeaderYour.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
										 				dtSapOrderHeaderYour.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
										 				dtSapOrderHeaderYour.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
										 				dtSapOrderHeaderYour.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
										 				dtSapOrderHeaderYour.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
										 				dtSapOrderHeaderYour.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
										 				   
										 			   }else {
										 				   
										 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
										 					  status = "F";
										 					 dtSapOrderHeaderYour.setStatus("F");
										 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
										 					  dtSapOrderHeaderYour.setStatus("S");
										 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
										 					 dtSapOrderHeaderYour.setRes_SalesOrderDocument(saleOrder);
										 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
											 					  status = "F";
											 					 dtSapOrderHeaderYour.setStatus("F");
											 			   } 
										 				   else {
										 					  status = "FF";
										 					 dtSapOrderHeaderYour.setStatus("FF");
										 				   }
										 				   
										 				   
										 				  dtSapOrderHeaderYour.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
										 				 dtSapOrderHeaderYour.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
										 				dtSapOrderHeaderYour.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
										 				dtSapOrderHeaderYour.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
										 				dtSapOrderHeaderYour.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
										 				dtSapOrderHeaderYour.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
										 				dtSapOrderHeaderYour.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
										 				dtSapOrderHeaderYour.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
										 				dtSapOrderHeaderYour.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
										 				dtSapOrderHeaderYour.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
										 				dtSapOrderHeaderYour.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
										 				dtSapOrderHeaderYour.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
										 				dtSapOrderHeaderYour.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
										 			   } 
										 			   
										 			   //update status dtSapPostHeader
										 			   try {
										 				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderYour);
										 			   }catch (Exception e) {
										 				e.printStackTrace();
										 			   }
										    		
										    		}
								    				//else {
										    		//	dtSapOrderHeaderYour = listDtSapOrderHeader.get(i) ;
										    		//}
								    			}
								    			else if (i == 1) {
								    				 // reject
								    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

										    			dtSapOrderHeader = listDtSapOrderHeader.get(i) ;
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
										 				   
										 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
										 					  status = "F";
											 				  dtSapOrderHeader.setStatus("F");
										 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
										 					  dtSapOrderHeader.setStatus("S");
										 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
										 					 dtSapOrderHeader.setRes_SalesOrderDocument(saleOrder);
										 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
											 					  status = "F";
												 				  dtSapOrderHeader.setStatus("F");
											 			   } 
										 				   else {
										 					  status = "FF";
											 				  dtSapOrderHeader.setStatus("FF");
										 				   }
										 				   
										 				   
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
										    		
										    		}else {
										    			dtSapOrderHeader = listDtSapOrderHeader.get(i) ;
										    		}
								    			
								    			}
								    			else { // i = 2

								    				if(listDtSapOrderHeader.get(i).getStatus().equals("F")) {

								    					dtSapOrderHeaderCNMemo = listDtSapOrderHeader.get(i) ;
										    			DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderCNMemo);
										    			
										 			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
										 				  // status = "S" ;
										 				  dtSapOrderHeaderCNMemo.setStatus("S");
										 				 dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
										 				dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
										 				dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
										 				dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
										 				dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
										 				dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
										 				dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
										 				dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
										 				dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
										 				dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
										 				   
										 			   }else {
										 				   
										 				   if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_Message()) && dtSapOrderHeaderApi.getRes_Message().indexOf("GatewayTimeout") > 0 ) {
										 					  status = "F";
										 					 dtSapOrderHeaderCNMemo.setStatus("F");
										 				   }else if(BeanUtil.isNotEmpty(dtSapOrderHeaderApi.getRes_MessageDesc()) && dtSapOrderHeaderApi.getRes_MessageDesc().indexOf("already exists in document number") > 0) {
										 					  dtSapOrderHeaderCNMemo.setStatus("S");
										 					  String saleOrder = dtSapOrderHeaderApi.getRes_MessageDesc().substring(dtSapOrderHeaderApi.getRes_MessageDesc().length() - 10, dtSapOrderHeaderApi.getRes_MessageDesc().length()) ;
										 					 dtSapOrderHeaderCNMemo.setRes_SalesOrderDocument(saleOrder);
										 				   } else if(dtSapOrderHeaderApi.getStatus().equals("R")) {
											 					  status = "F";
											 					 dtSapOrderHeaderCNMemo.setStatus("F");
											 			   } 
										 				   else {
										 					  status = "FF";
										 					 dtSapOrderHeaderCNMemo.setStatus("FF");
										 				   }
										 				   
										 				   
										 				  dtSapOrderHeaderCNMemo.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
										 				 dtSapOrderHeaderCNMemo.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
										 				dtSapOrderHeaderCNMemo.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
										 				dtSapOrderHeaderCNMemo.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
										 				dtSapOrderHeaderCNMemo.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
										 				dtSapOrderHeaderCNMemo.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
										 				dtSapOrderHeaderCNMemo.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
										 				dtSapOrderHeaderCNMemo.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
										 				dtSapOrderHeaderCNMemo.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
										 				dtSapOrderHeaderCNMemo.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
										 			   } 
										 			   
										 			   //update status dtSapPostHeader
										 			   try {
										 				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderCNMemo);
										 			   }catch (Exception e) {
										 				e.printStackTrace();
										 			   }
										    		
										    		}else {
										    			dtSapOrderHeaderCNMemo = listDtSapOrderHeader.get(i) ;
										    		}
								    			
								    			}
									    		
									    	}
						    			
						    			}
						
						    		}

						    	}
						    	
						    }
						}
						
						if(BeanUtil.isNotEmpty(dtSapTransactionType.getPostTransaction()) && dtSapTransactionType.getPostTransaction().equals("Y") ) {
							// query post transaction F and call sap
							if(elDtSapTransaction.getTransactionType() == 18 || elDtSapTransaction.getTransactionType() == 19 ) {  //depen on sale order   type 18,19,14,28   17,23 ,22
								
								if(dtSapOrderHeader != null && 
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
										   dtSapOrderHeader.getStatus().equals("S") &&
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
									List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
									// listDtSapPostHeader size = 1 only
									if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
										 sapHandleTransactionTypeSaleNormalService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeader) ;
										 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
										 if(sapDtSapPostHeader.getStatus().equals("S")) { 
											 listDtSapPostHeader.get(0).setStatus("S");
										   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
											   status = "F";
											   listDtSapPostHeader.get(0).setStatus("F");
										   } else { // Status F
											   status = "FF";
											   listDtSapPostHeader.get(0).setStatus("FF");
										   }
										   
										   //update status dtSapPostHeader
										   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
									}
							
								   }
								
							}else if(elDtSapTransaction.getTransactionType() == 14 || 
									 elDtSapTransaction.getTransactionType() == 28 || 
									 elDtSapTransaction.getTransactionType() == 67) {
								if(dtSapOrderHeader != null && 
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
										   dtSapOrderHeader.getStatus().equals("S") &&
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
									List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
									// listDtSapPostHeader size = 1 only
									if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
										 sapHandleTransactionTypeSaleNormalService.updatePostTransactionBeforeCallSapType14(listDtSapPostHeader.get(0), dtSapOrderHeader) ;
										 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
										 if(sapDtSapPostHeader.getStatus().equals("S")) { 
											 listDtSapPostHeader.get(0).setStatus("S");
										   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
											   status = "F";
											   listDtSapPostHeader.get(0).setStatus("F");
										   } else { // Status F
											   status = "FF";
											   listDtSapPostHeader.get(0).setStatus("FF");
										   }
										   
										   //update status dtSapPostHeader
										   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
									}
							
								   }
								   else if(noSaleorder) {  // 14 , 67  use deposit > 1 time  
									   List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
										// listDtSapPostHeader size = 1 only
										if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
											// sapHandleTransactionTypeSaleNormalService.updatePostTransactionBeforeCallSapType14(listDtSapPostHeader.get(0), dtSapOrderHeader) ;
											 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
											 if(sapDtSapPostHeader.getStatus().equals("S")) { 
												 listDtSapPostHeader.get(0).setStatus("S");
											   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
												   status = "F";
												   listDtSapPostHeader.get(0).setStatus("F");
											   } else { // Status F
												   status = "FF";
												   listDtSapPostHeader.get(0).setStatus("FF");
											   }
											   
											   //update status dtSapPostHeader
											   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
										}
								   }
							}else if(elDtSapTransaction.getTransactionType() == 17 ) {
									//|| elDtSapTransaction.getTransactionType() == 22 || elDtSapTransaction.getTransactionType() == 23) {
								if(dtSapOrderHeaderCNMemo != null && 
										   BeanUtil.isNotEmpty(dtSapOrderHeaderCNMemo.getStatus()) && 
										   dtSapOrderHeaderCNMemo.getStatus().equals("S") &&
										   BeanUtil.isNotEmpty(dtSapOrderHeaderCNMemo.getRes_SalesOrderDocument())) {
									List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
									// listDtSapPostHeader size = 1 only
									if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
										sapHandleTransactionTypeCancelDepositService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeaderCNMemo) ;
										 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
										 if(sapDtSapPostHeader.getStatus().equals("S")) { 
											 listDtSapPostHeader.get(0).setStatus("S");
										   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
											   status = "F";
											   listDtSapPostHeader.get(0).setStatus("F");
										   } else { // Status F
											   status = "FF";
											   listDtSapPostHeader.get(0).setStatus("FF");
										   }
										   
										   //update status dtSapPostHeader
										   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
									}
							
								   }
							}else if(elDtSapTransaction.getTransactionType() == 23 ) {
							   if(dtSapOrderHeaderCNMemo != null && 
									   BeanUtil.isNotEmpty(dtSapOrderHeaderCNMemo.getStatus()) && 
									   dtSapOrderHeaderCNMemo.getStatus().equals("S") &&
									   BeanUtil.isNotEmpty(dtSapOrderHeaderCNMemo.getRes_SalesOrderDocument())) {
								List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
								// listDtSapPostHeader size = 1 only
								if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
									sapHandleTransactionTypeCancelSaleNormalService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeaderCNMemo) ;
									 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
									 if(sapDtSapPostHeader.getStatus().equals("S")) { 
										 listDtSapPostHeader.get(0).setStatus("S");
									   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
										   status = "F";
										   listDtSapPostHeader.get(0).setStatus("F");
									   } else { // Status F
										   status = "FF";
										   listDtSapPostHeader.get(0).setStatus("FF");
									   }
									   
									   //update status dtSapPostHeader
									   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
								}
						
							   }
						   }else if(elDtSapTransaction.getTransactionType() == 22 ) {
							   if(dtSapOrderHeaderCNMemo != null && 
									   BeanUtil.isNotEmpty(dtSapOrderHeaderCNMemo.getStatus()) && 
									   dtSapOrderHeaderCNMemo.getStatus().equals("S") &&
									   BeanUtil.isNotEmpty(dtSapOrderHeaderCNMemo.getRes_SalesOrderDocument())) {
								List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
								// listDtSapPostHeader size = 1 only
								if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
									sapHandleTransactionTypeCancelSaleSameDayService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeaderCNMemo) ;
									 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
									 if(sapDtSapPostHeader.getStatus().equals("S")) { 
										 listDtSapPostHeader.get(0).setStatus("S");
									   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
										   status = "F";
										   listDtSapPostHeader.get(0).setStatus("F");
									   } else { // Status F
										   status = "FF";
										   listDtSapPostHeader.get(0).setStatus("FF");
									   }
									   
									   //update status dtSapPostHeader
									   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
								}
						
							   }
						   }else if(elDtSapTransaction.getTransactionType() == 12 ) {
								if(dtSapOrderHeader != null && 
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
										   dtSapOrderHeader.getStatus().equals("S") &&
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
									List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
									// listDtSapPostHeader size = 1 only
									if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
										sapHandleTransactionTypeDepositPartnerService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeader) ;
										 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
										 if(sapDtSapPostHeader.getStatus().equals("S")) { 
											 listDtSapPostHeader.get(0).setStatus("S");
										   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
											   status = "F";
											   listDtSapPostHeader.get(0).setStatus("F");
										   } else { // Status F
											   status = "FF";
											   listDtSapPostHeader.get(0).setStatus("FF");
										   }
										   
										   //update status dtSapPostHeader
										   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
									}
							
								   }
							}
						   else if(elDtSapTransaction.getTransactionType() == 66) {
								if(dtSapOrderHeader != null && 
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
										   dtSapOrderHeader.getStatus().equals("S") &&
										   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
									List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
									// listDtSapPostHeader size = 1 only
									if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
										  sapHandleTransactionTypeCancelDepositService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeader);
										 //sapHandleTransactionTypeSaleNormalService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeader) ;
										 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
										 if(sapDtSapPostHeader.getStatus().equals("S")) { 
											 listDtSapPostHeader.get(0).setStatus("S");
										   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
											   status = "F";
											   listDtSapPostHeader.get(0).setStatus("F");
										   } else { // Status F
											   status = "FF";
											   listDtSapPostHeader.get(0).setStatus("FF");
										   }
										   
										   //update status dtSapPostHeader
										   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
									}
							
								   }
						   }
						   else if(elDtSapTransaction.getTransactionType() == 31 ) {
								//|| elDtSapTransaction.getTransactionType() == 22 || elDtSapTransaction.getTransactionType() == 23) {
							if(dtSapOrderHeader != null && 
									   BeanUtil.isNotEmpty(dtSapOrderHeader.getStatus()) && 
									   dtSapOrderHeader.getStatus().equals("S") &&
									   BeanUtil.isNotEmpty(dtSapOrderHeader.getRes_SalesOrderDocument())) {
								List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());   
								// listDtSapPostHeader size = 1 only
								if(listDtSapPostHeader.get(0).getStatus().equals("F")) {
									//sapHandleTransactionTypeCancelDepositService.updatePostTransactionBeforeCallSap(listDtSapPostHeader.get(0), dtSapOrderHeader) ;
									 DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(listDtSapPostHeader.get(0));
									 if(sapDtSapPostHeader.getStatus().equals("S")) { 
										 listDtSapPostHeader.get(0).setStatus("S");
									   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
										   status = "F";
										   listDtSapPostHeader.get(0).setStatus("F");
									   } else { // Status F
										   status = "FF";
										   listDtSapPostHeader.get(0).setStatus("FF");
									   }
									   
									   //update status dtSapPostHeader
									   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(listDtSapPostHeader.get(0));
								}
						
							   }
						}
							
							else {// not depen on sale order 
								List<DtSapPostHeader> listDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.getDtSapPostHeaderBySapTranId(elDtSapTransaction.getSapTranId());
								if(BeanUtil.isNotEmpty(listDtSapPostHeader)) {
									for(DtSapPostHeader elDtSapPostHeader : listDtSapPostHeader) {
										if(elDtSapPostHeader.getStatus().equals("F")) {
											DtSapPostHeader sapDtSapPostHeader = sapHandleTransactionTypeSaleNormalService.callPostTransactionApi(elDtSapPostHeader);
											   if(sapDtSapPostHeader.getStatus().equals("S")) { 
												   elDtSapPostHeader.setStatus("S");
											   }else if(sapDtSapPostHeader.getStatus().equals("R")) {
												   status = "F";
												   elDtSapPostHeader.setStatus("F");
											   } else { // Status F
												   status = "FF";
												   elDtSapPostHeader.setStatus("FF");
											   }
											   
											   //update status dtSapPostHeader
											   sapHandleTransactionTypeSaleNormalService.updateDtSapPostHeader(elDtSapPostHeader);
										}
										
									}
									
//									if(status.equals("F")) {
//										statusToUpdate = "FF";
//									}
								}
							}
						
						}
						
						
						
						// query reserve F and call sap
						
						// update status elDtSapTransaction
						elDtSapTransaction.setStatus(status);
						sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(elDtSapTransaction);
						
					}catch (Exception e) {
						e.printStackTrace() ;
					}
				}
			}
	
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/click-and-collect-stock-dc",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeClickAndCollectStockDC(@RequestBody String jsonRequest) {
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
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
		   DtSapOrderHeader dtSapOrderHeader = null;
		   DtSapOrderHeader dtSapOrderHeaderUnLock = null ;
		   DtSapOrderHeader dtSapOrderHeaderDeliveryBlock = null;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   if(dtSapTransaction.getTransactionType() == 24) {  
					   dtSapOrderHeaderUnLock = sapHandleTransactionTypeSaleNormalService.unLockOrderBeforeUpdate24(dtSapTransaction);
					   dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder24(dtSapTransaction);
					   dtSapOrderHeaderDeliveryBlock = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder24DeliveryBlock(dtSapTransaction);
				   }
				   
			   }catch (Exception e) {
				e.printStackTrace();
			   }
		   }
		   
		   if(dtSapOrderHeaderUnLock!=null) { // unlock order 
			   //checke = true ;
			   
			   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderUnLock);
			
			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
				  // status = "S" ;
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
				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderUnLock);
			   }catch (Exception e) {
				e.printStackTrace();
			   }  
	       }
		   //Step call api sap
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
		   
		   if(dtSapOrderHeaderDeliveryBlock!=null && dtSapOrderHeader !=null && dtSapOrderHeader.getStatus().equals("S")) {
				// call service post transaction to send input to sap and keep response from sap
				   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderDeliveryBlock);
				
				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
					  // status = "S" ;
					   dtSapOrderHeaderDeliveryBlock.setStatus("S");
					   dtSapOrderHeaderDeliveryBlock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
					   dtSapOrderHeaderDeliveryBlock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
					   dtSapOrderHeaderDeliveryBlock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   }else {
					   status = "F";
					   dtSapOrderHeaderDeliveryBlock.setStatus("F");
					   dtSapOrderHeaderDeliveryBlock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
					   dtSapOrderHeaderDeliveryBlock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
					   dtSapOrderHeaderDeliveryBlock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   } 
				   
				   //update status dtSapPostHeader
				   try {
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderDeliveryBlock);
				   }catch (Exception e) {
					e.printStackTrace();
				   }
			   }else {
				   status = "F";
				   if(dtSapOrderHeaderDeliveryBlock!=null) { 
					   dtSapOrderHeaderDeliveryBlock.setStatus("F");
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderDeliveryBlock);
				   }
				   
			   }
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/click-and-collect-stock-shop",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeClickAndCollectStockShop(@RequestBody String jsonRequest) {
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
		   DtSapTransaction dtSapTransaction = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionById(input.getSapTranId()); 
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
		   DtSapOrderHeader dtSapOrderHeader = null;
		   DtSapOrderHeader dtSapOrderHeaderUnLock = null ;
		   DtSapOrderHeader dtSapOrderHeaderDeliveryBlock = null;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				   dtSapOrderHeaderUnLock = sapHandleTransactionTypeSaleNormalService.unLockOrderBeforeUpdate25(dtSapTransaction);
				   dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder25(dtSapTransaction);
				   dtSapOrderHeaderDeliveryBlock = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder25DeliveryBlock(dtSapTransaction);
				   
			   }catch (Exception e) {
				e.printStackTrace();
			   }
		   }
		   
		   if(dtSapOrderHeaderUnLock!=null) { // unlock order 
			   //checke = true ;
			   
			   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderUnLock);
			
			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
				  // status = "S" ;
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
				   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderUnLock);
			   }catch (Exception e) {
				e.printStackTrace();
			   }  
	       }
		   
		   //Step call api sap
		   // call service sale order to send input to sap and keep response from sap
		   if(dtSapOrderHeader!=null) {
			// call service post transaction to send input to sap and keep response from sap
			   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeader);
			
			   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
				   status = "S" ;
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
		   
		   if(dtSapOrderHeaderDeliveryBlock!=null && dtSapOrderHeader !=null && dtSapOrderHeader.getStatus().equals("S")) {
				// call service post transaction to send input to sap and keep response from sap
				   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderDeliveryBlock);
				
				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
					  // status = "S" ;
					   dtSapOrderHeaderDeliveryBlock.setStatus("S");
					   dtSapOrderHeaderDeliveryBlock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
					   dtSapOrderHeaderDeliveryBlock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
					   dtSapOrderHeaderDeliveryBlock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   }else {
					   status = "F";
					   dtSapOrderHeaderDeliveryBlock.setStatus("F");
					   dtSapOrderHeaderDeliveryBlock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
					   dtSapOrderHeaderDeliveryBlock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
					   dtSapOrderHeaderDeliveryBlock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   } 
				   
				   //update status dtSapPostHeader
				   try {
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderDeliveryBlock);
				   }catch (Exception e) {
					e.printStackTrace();
				   }
			   }else {
				   status = "F";
				   if(dtSapOrderHeaderDeliveryBlock!=null) { 
					   dtSapOrderHeaderDeliveryBlock.setStatus("F");
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderDeliveryBlock);
				   }
				   
			   }
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/dynamic-sim-optimus",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeDynamicSimOptimus(@RequestBody String jsonRequest) {
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
		   DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
		   DtSapOrderHeader dtSapOrderHeader = null;
		   DtSapOrderHeader dtSapOrderHeaderDeliveryBlock = null;
		   
		   //1. sale order 
		   if(BeanUtil.isNotEmpty(dtSapTransactionType.getSaleOrder()) && dtSapTransactionType.getSaleOrder().equals("Y") ) {
			    // query sql to get infomation
			    // insert table
			   try {
				      /////////// type 24 and 51 is the same
					  dtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder24(dtSapTransaction);
					  dtSapOrderHeaderDeliveryBlock = sapHandleTransactionTypeSaleNormalService.queryInfoAndInsertSaleorder24DeliveryBlock(dtSapTransaction);

				   
			   }catch (Exception e) {
				e.printStackTrace();
			   }
		   }
		   
		   
		   //Step call api sap
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
		   
		   
		   if(dtSapOrderHeaderDeliveryBlock!=null && dtSapOrderHeader !=null && dtSapOrderHeader.getStatus().equals("S")) {
				// call service post transaction to send input to sap and keep response from sap
				   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(dtSapOrderHeaderDeliveryBlock);
				
				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
					  // status = "S" ;
					   dtSapOrderHeaderDeliveryBlock.setStatus("S");
					   dtSapOrderHeaderDeliveryBlock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
					   dtSapOrderHeaderDeliveryBlock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
					   dtSapOrderHeaderDeliveryBlock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   }else {
					   status = "F";
					   dtSapOrderHeaderDeliveryBlock.setStatus("F");
					   dtSapOrderHeaderDeliveryBlock.setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
					   dtSapOrderHeaderDeliveryBlock.setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
					   dtSapOrderHeaderDeliveryBlock.setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
					   dtSapOrderHeaderDeliveryBlock.setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
					   dtSapOrderHeaderDeliveryBlock.setRes_Message(dtSapOrderHeaderApi.getRes_Message());
				   } 
				   
				   //update status dtSapPostHeader
				   try {
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderDeliveryBlock);
				   }catch (Exception e) {
					e.printStackTrace();
				   }
			   }else {
				   status = "F";
				   if(dtSapOrderHeaderDeliveryBlock!=null) { 
					   dtSapOrderHeaderDeliveryBlock.setStatus("F");
					   sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderDeliveryBlock);
				   }
				   
			   }
		   
		   dtSapTransaction.setStatus(status);
		   sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		   
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/fix-cmr-no-billing",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> fixCmrNoBilling(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		//Gson gson = new Gson();
		try {
			
			List<Long> listCmrNoBilling = sapHandleTransactionTypeSaleNormalService.listCmrNoBilling();
		    for(Long sapId : listCmrNoBilling) {
		    	try {
		    		List<DtSapOrderHeader>  liDtSapOrderHeader = sapHandleTransactionTypeSaleNormalService.unRejectItemAndBlockOrder(sapId);
		    		String status = "FF";
		    		
		    		//expec liDtSapOrderHeader size = 2    un reject    and  delivery block
		 		   if(BeanUtil.isNotEmpty(liDtSapOrderHeader)) {
		 				// call un reject 
		 				   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(liDtSapOrderHeader.get(0));
		 				
		 				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
		 					   status = "S" ;
		 					   liDtSapOrderHeader.get(0).setStatus("S");
		 					   liDtSapOrderHeader.get(0).setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
		 					   liDtSapOrderHeader.get(0).setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
		 					   liDtSapOrderHeader.get(0).setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
		 					   liDtSapOrderHeader.get(0).setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
		 					   liDtSapOrderHeader.get(0).setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
		 					   liDtSapOrderHeader.get(0).setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
		 					   liDtSapOrderHeader.get(0).setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
		 					   liDtSapOrderHeader.get(0).setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
		 					   liDtSapOrderHeader.get(0).setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
		 					   liDtSapOrderHeader.get(0).setRes_Message(dtSapOrderHeaderApi.getRes_Message());
		 				   }else {
		 					   status = "FF";
		 					   liDtSapOrderHeader.get(0).setStatus("FF");
		 					   liDtSapOrderHeader.get(0).setRes_SalesOrderDocument(dtSapOrderHeaderApi.getRes_SalesOrderDocument());
		 					   liDtSapOrderHeader.get(0).setRes_MessageID(dtSapOrderHeaderApi.getRes_MessageID());
		 					   liDtSapOrderHeader.get(0).setRes_PartnerName(dtSapOrderHeaderApi.getRes_PartnerName());
		 					   liDtSapOrderHeader.get(0).setRes_PartnerMessageID(dtSapOrderHeaderApi.getRes_PartnerMessageID());
		 					   liDtSapOrderHeader.get(0).setRes_MessageType(dtSapOrderHeaderApi.getRes_MessageType());
		 					   liDtSapOrderHeader.get(0).setRes_MessageClass(dtSapOrderHeaderApi.getRes_MessageClass());
		 					   liDtSapOrderHeader.get(0).setRes_MessageNumber(dtSapOrderHeaderApi.getRes_MessageNumber());
		 					   liDtSapOrderHeader.get(0).setRes_MessageDesc(dtSapOrderHeaderApi.getRes_MessageDesc());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable1(dtSapOrderHeaderApi.getRes_MessageVariable1());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable2(dtSapOrderHeaderApi.getRes_MessageVariable2());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable3(dtSapOrderHeaderApi.getRes_MessageVariable3());
		 					   liDtSapOrderHeader.get(0).setRes_MessageVariable4(dtSapOrderHeaderApi.getRes_MessageVariable4());
		 					   liDtSapOrderHeader.get(0).setRes_CustomerReference(dtSapOrderHeaderApi.getCustomerReference());
		 					   liDtSapOrderHeader.get(0).setRes_Message(dtSapOrderHeaderApi.getRes_Message());
		 				   }
		 				   
		 				   try {
		 					  sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(liDtSapOrderHeader.get(0));
		 				   }catch (Exception e) {
							  e.printStackTrace();
						   }
		 				 
		 				   
		 				  if(dtSapOrderHeaderApi.getStatus().equals("S")) {
		 					// call delivery block
			 				  DtSapOrderHeader dtSapOrderHeaderApiD = sapHandleTransactionTypeSaleNormalService.callSaleorderApi(liDtSapOrderHeader.get(1));
				 				
			 				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
			 					   status = "S" ;
			 					   liDtSapOrderHeader.get(1).setStatus("S");
			 					   liDtSapOrderHeader.get(1).setRes_SalesOrderDocument(dtSapOrderHeaderApiD.getRes_SalesOrderDocument());
			 					   liDtSapOrderHeader.get(1).setRes_MessageID(dtSapOrderHeaderApiD.getRes_MessageID());
			 					   liDtSapOrderHeader.get(1).setRes_PartnerName(dtSapOrderHeaderApiD.getRes_PartnerName());
			 					   liDtSapOrderHeader.get(1).setRes_PartnerMessageID(dtSapOrderHeaderApiD.getRes_PartnerMessageID());
			 					   liDtSapOrderHeader.get(1).setRes_MessageType(dtSapOrderHeaderApiD.getRes_MessageType());
			 					   liDtSapOrderHeader.get(1).setRes_MessageClass(dtSapOrderHeaderApiD.getRes_MessageClass());
			 					   liDtSapOrderHeader.get(1).setRes_MessageNumber(dtSapOrderHeaderApiD.getRes_MessageNumber());
			 					   liDtSapOrderHeader.get(1).setRes_MessageDesc(dtSapOrderHeaderApiD.getRes_MessageDesc());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable1(dtSapOrderHeaderApiD.getRes_MessageVariable1());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable2(dtSapOrderHeaderApiD.getRes_MessageVariable2());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable3(dtSapOrderHeaderApiD.getRes_MessageVariable3());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable4(dtSapOrderHeaderApiD.getRes_MessageVariable4());
			 					   liDtSapOrderHeader.get(1).setRes_CustomerReference(dtSapOrderHeaderApiD.getCustomerReference());
			 					   liDtSapOrderHeader.get(1).setRes_Message(dtSapOrderHeaderApiD.getRes_Message());
			 				   }else {
			 					   status = "FF";
			 					   liDtSapOrderHeader.get(1).setStatus("FF");
			 					   liDtSapOrderHeader.get(1).setRes_SalesOrderDocument(dtSapOrderHeaderApiD.getRes_SalesOrderDocument());
			 					   liDtSapOrderHeader.get(1).setRes_MessageID(dtSapOrderHeaderApiD.getRes_MessageID());
			 					   liDtSapOrderHeader.get(1).setRes_PartnerName(dtSapOrderHeaderApiD.getRes_PartnerName());
			 					   liDtSapOrderHeader.get(1).setRes_PartnerMessageID(dtSapOrderHeaderApiD.getRes_PartnerMessageID());
			 					   liDtSapOrderHeader.get(1).setRes_MessageType(dtSapOrderHeaderApiD.getRes_MessageType());
			 					   liDtSapOrderHeader.get(1).setRes_MessageClass(dtSapOrderHeaderApiD.getRes_MessageClass());
			 					   liDtSapOrderHeader.get(1).setRes_MessageNumber(dtSapOrderHeaderApiD.getRes_MessageNumber());
			 					   liDtSapOrderHeader.get(1).setRes_MessageDesc(dtSapOrderHeaderApiD.getRes_MessageDesc());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable1(dtSapOrderHeaderApiD.getRes_MessageVariable1());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable2(dtSapOrderHeaderApiD.getRes_MessageVariable2());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable3(dtSapOrderHeaderApiD.getRes_MessageVariable3());
			 					   liDtSapOrderHeader.get(1).setRes_MessageVariable4(dtSapOrderHeaderApiD.getRes_MessageVariable4());
			 					   liDtSapOrderHeader.get(1).setRes_CustomerReference(dtSapOrderHeaderApiD.getCustomerReference());
			 					   liDtSapOrderHeader.get(1).setRes_Message(dtSapOrderHeaderApiD.getRes_Message());
			 				   } 
			 				   
			 				  try {
			 					  sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(liDtSapOrderHeader.get(1));
			 				   }catch (Exception e) {
								  e.printStackTrace();
							   }
		 				   }else {
		 					  try {
		 						 liDtSapOrderHeader.get(1).setStatus("FF");
			 					 sapHandleTransactionTypeSaleNormalService.updateDtSapOrderHeader(liDtSapOrderHeader.get(1));
			 				   }catch (Exception e) {
								  e.printStackTrace();
							   }
		 				   }
		 				
		 				   
		 				  
		 				   try {	 					
		 					  DtSapTransaction dtSapTransaction = sapHandleTransactionTypeSaleNormalService.queryDtSapTransactionById(liDtSapOrderHeader.get(0).getSapTranId());
		 					  dtSapTransaction.setStatus(status);
		 					  sapHandleTransactionTypeSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		 				   }catch (Exception e) {
		 					e.printStackTrace();
		 				   }
		 				   
		 				   
		 				   
		 				  if(status.equals("S")) {
		 					  // update for job resend
		 					 sapHandleTransactionTypeSaleNormalService.updateCmrForResend(sapId);
		 				  }
		 			   }
		    		
		    	}catch (Exception e) {
					e.printStackTrace();
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		
	}
	
	

}
