package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
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
import th.co.ais.dt.core.service.core.impl.sap.dto.InsertSapTransactionAndQueueCloudBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.properties.SpringProfile;
import th.co.ais.dt.queuelib.service.queue.dto.impl.DTSapConsumeQueueMessage;
import th.co.ais.dt.queuelib.service.queue.impl.sender.QueueSender;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.service.core.impl.so.dto.CreateReceiptResponse;
import th.co.ais.dt.service.core.impl.so.dto.DTCreateReceiptRequest;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCancelSaleNormalWebImpl {
	final String prefixPath = "api/sap-handle-transaction-type/v1";

	private final ISapHandleTransactionTypeCancelSaleNormalService sapHandleTransactionTypeCancelSaleNormalService;
	private final QueueSender queueSender;
	private final SpringProfile springProfile;

	@RequestMapping(value = prefixPath + "/cancel-sale-normal", method = RequestMethod.POST, produces = {
			"application/json" })
	public ResponseEntity<String> handleTransactionTypeCancelSaleNormal(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		DtSapTransaction input = new DtSapTransaction();
		try {

			input = gson.fromJson(jsonRequest, DtSapTransaction.class);

			String status = "S";
			boolean  checke = false ;
			// step 0 query sap transaction
			DtSapTransaction dtSapTransaction = sapHandleTransactionTypeCancelSaleNormalService.queryDtSapTransactionById(input.getSapTranId());

			// step 1 query workflow
			DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeCancelSaleNormalService
					.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));

			// step2 Do workflow

			// seq 1. sale order
			// seq 2. post transaction
			// seq 3. cancel reserve
			// base on config DtSapTransactionType

			DtSapPostHeader dtSapPostHeader = null;
			DtSapOrderHeader dtSapOrderHeader = null;
			DtSapOrderHeader dtSapOrderHeaderCNMemo = null ;

			// 1. sale order
			if ("Y".equals(dtSapTransactionType.getSaleOrder())) {
				// query sql to get infomation
				// insert table

				try {
					if(dtSapTransaction.getTransactionType() == 23 ) {
						//dtSapOrderHeader = sapHandleTransactionTypeCancelSaleNormalService.queryInfoAndInsertSaleorder(dtSapTransaction);
						dtSapOrderHeaderCNMemo = sapHandleTransactionTypeCancelSaleNormalService.queryInfoAndInsertSaleorderCNMemo(dtSapTransaction);
					 }else if(dtSapTransaction.getTransactionType() == 31) {
						 dtSapOrderHeader = sapHandleTransactionTypeCancelSaleNormalService.unBlockCNMemo(dtSapTransaction);			
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// 2. post transaction
			if ("Y".equals(dtSapTransactionType.getPostTransaction())) {
				// query sql to get infomation
				// insert table

				if(dtSapTransaction.getTransactionType() == 23 ) {
					dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService.queryInfoAndInsertPostTransactionCNMEMO(dtSapTransaction);
				}else if(dtSapTransaction.getTransactionType() == 68) {
					dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService
							.queryInfoAndInsertPostTransactionDepositWithoutOrder(dtSapTransaction);
				}else if(dtSapTransaction.getTransactionType() == 31) {
					dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService
							.queryInfoAndInsertPostTransactionEclaimNcp(dtSapTransaction);
				}else if(dtSapTransaction.getTransactionType() == 70) {
					dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService
							.queryInfoAndInsertPostTransactionCNONlyNotUpdateStock(dtSapTransaction);
				}else if(dtSapTransaction.getTransactionType() == 15) {
					dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService
							.queryInfoAndInsertPostTransaction15(dtSapTransaction);
				}
				else {
					dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService
							.queryInfoAndInsertPostTransaction(dtSapTransaction);
				}
					

			}

			// 3. cancel reserve
			if ("Y".equals(dtSapTransactionType.getCancelReserve())) {
				// query sql to get infomation
				// insert table

				// call service cancel reserve to send input to sap and keep response from sap
			}

			// Step 3 call api sap

			// call service sale order to send input to sap and keep response from sap
//			if (dtSapOrderHeader != null) {
//				// call service post transaction to send input to sap and keep response from sap
//				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelSaleNormalService
//						.callSaleorderApi(dtSapOrderHeader);
//
//				if ("S".equals(dtSapOrderHeaderrApi.getStatus())) {
//					// status = "S" ;
//					dtSapOrderHeader.setStatus("S");
//					dtSapOrderHeader.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
//
//				   dtSapOrderHeader.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
//				   dtSapOrderHeader.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
//				   dtSapOrderHeader.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
//				   dtSapOrderHeader.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
//				   dtSapOrderHeader.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
//				   dtSapOrderHeader.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
//				   dtSapOrderHeader.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
//				   dtSapOrderHeader.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
//				   dtSapOrderHeader.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
//				   dtSapOrderHeader.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
//				   dtSapOrderHeader.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
//				   dtSapOrderHeader.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
//				   dtSapOrderHeader.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
//					
//				} else {
//					status = "F";
//					dtSapOrderHeader.setStatus("F");
//					dtSapOrderHeader.setRes_SalesOrderDocument(dtSapOrderHeaderrApi.getRes_SalesOrderDocument());
//
//				   dtSapOrderHeader.setRes_MessageID(dtSapOrderHeaderrApi.getRes_MessageID());
//				   dtSapOrderHeader.setRes_PartnerName(dtSapOrderHeaderrApi.getRes_PartnerName());
//				   dtSapOrderHeader.setRes_PartnerMessageID(dtSapOrderHeaderrApi.getRes_PartnerMessageID());
//				   dtSapOrderHeader.setRes_MessageType(dtSapOrderHeaderrApi.getRes_MessageType());
//				   dtSapOrderHeader.setRes_MessageClass(dtSapOrderHeaderrApi.getRes_MessageClass());
//				   dtSapOrderHeader.setRes_MessageNumber(dtSapOrderHeaderrApi.getRes_MessageNumber());
//				   dtSapOrderHeader.setRes_MessageDesc(dtSapOrderHeaderrApi.getRes_MessageDesc());
//				   dtSapOrderHeader.setRes_MessageVariable1(dtSapOrderHeaderrApi.getRes_MessageVariable1());
//				   dtSapOrderHeader.setRes_MessageVariable2(dtSapOrderHeaderrApi.getRes_MessageVariable2());
//				   dtSapOrderHeader.setRes_MessageVariable3(dtSapOrderHeaderrApi.getRes_MessageVariable3());
//				   dtSapOrderHeader.setRes_MessageVariable4(dtSapOrderHeaderrApi.getRes_MessageVariable4());
//				   dtSapOrderHeader.setRes_CustomerReference(dtSapOrderHeaderrApi.getCustomerReference());
//				   dtSapOrderHeader.setRes_Message(dtSapOrderHeaderrApi.getRes_Message());
//				}
//
//				// update status dtSapPostHeader
//				try {
//					sapHandleTransactionTypeCancelSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeader);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			
			//CN MEMO
			if (dtSapOrderHeaderCNMemo != null && status.equals("S")) {
				checke = true ;
				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelSaleNormalService.callSaleorderApi(dtSapOrderHeaderCNMemo);

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
					sapHandleTransactionTypeCancelSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderCNMemo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//unblock cn MEMO
			if (dtSapOrderHeader != null && status.equals("S")) {
				checke = true ;
				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelSaleNormalService.callSaleorderApi(dtSapOrderHeader);

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
					sapHandleTransactionTypeCancelSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// call service post transaction to send input to sap and keep response from sap
			if (dtSapPostHeader != null && status.equals("S")) {
				checke = true ;
				if(dtSapTransaction.getTransactionType() == 23 ) {
					//update post item before call sap
					sapHandleTransactionTypeCancelSaleNormalService.updatePostTransactionBeforeCallSap(dtSapPostHeader, dtSapOrderHeaderCNMemo) ;
				 }
				
				// call service post transaction to send input to sap and keep response from sap
				DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeCancelSaleNormalService
						.callPostTransactionApi(dtSapPostHeader);

				if ("S".equals(dtSapPostHeaderApi.getStatus())) {
					status = "S";
					dtSapPostHeader.setStatus("S");
				} else {
					status = "F";
					dtSapPostHeader.setStatus("F");
				}

				// update status dtSapPostHeader
				sapHandleTransactionTypeCancelSaleNormalService.updateDtSapPostHeader(dtSapPostHeader);
			}else if(dtSapPostHeader != null && status.equals("F")) {
				checke = true ;
				status = "F";
				dtSapPostHeader.setStatus("F");
				sapHandleTransactionTypeCancelSaleNormalService.updateDtSapPostHeader(dtSapPostHeader);
			}

			// call service cancel reserve to send input to sap and keep response from sap
			// XXXX

			// Step 4 check response from sap then update status table sap_transaction

			if(checke) {
				dtSapTransaction.setStatus(status);
			}else {
				dtSapTransaction.setStatus("E");
			}
			
			sapHandleTransactionTypeCancelSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);

		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}

		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = prefixPath + "/cancel-goods-return", method = RequestMethod.POST, produces = {
	"application/json" })
		public ResponseEntity<String> handleTransactionTypeCancelGoodReturn(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		DtSapTransaction input = new DtSapTransaction();
		try {
		
			input = gson.fromJson(jsonRequest, DtSapTransaction.class);
		
			String status = "S";
			// step 0 query sap transaction
			DtSapTransaction dtSapTransaction = sapHandleTransactionTypeCancelSaleNormalService.queryDtSapTransactionById(input.getSapTranId());
		
			// step 1 query workflow
			DtSapTransactionType dtSapTransactionType = sapHandleTransactionTypeCancelSaleNormalService
					.queryDtSapTransactionTypeByKey(Long.valueOf(dtSapTransaction.getTransactionType()));
		
			// step2 Do workflow
		
			// seq 1. sale order
			// seq 2. post transaction
			// seq 3. cancel reserve
			// base on config DtSapTransactionType
		
			DtSapPostHeader dtSapPostHeader = null;
			DtSapOrderHeader dtSapOrderHeader = null;
		
			// 1. sale order
			if ("Y".equals(dtSapTransactionType.getSaleOrder())) {
				// query sql to get infomation
				// insert table
		
			}
		
			// 2. post transaction
			if ("Y".equals(dtSapTransactionType.getPostTransaction())) {
				// query sql to get infomation
				// insert table
		
				dtSapPostHeader = sapHandleTransactionTypeCancelSaleNormalService
						.CancelGoodsReturnQueryInfoAndInsertPostTransaction(dtSapTransaction);
		
			}
		
			// 3. cancel reserve
			if ("Y".equals(dtSapTransactionType.getCancelReserve())) {
				// query sql to get infomation
				// insert table
		
				// call service cancel reserve to send input to sap and keep response from sap
			}
		
			// Step 3 call api sap
		
			// call service sale order to send input to sap and keep response from sap
			if (dtSapOrderHeader != null) {
				// call service post transaction to send input to sap and keep response from sap
				
			}
		
			// call service post transaction to send input to sap and keep response from sap
			if (dtSapPostHeader != null) {
				// call service post transaction to send input to sap and keep response from sap
				DtSapPostHeader dtSapPostHeaderApi = sapHandleTransactionTypeCancelSaleNormalService
						.callPostTransactionApi(dtSapPostHeader);
		
				if ("S".equals(dtSapPostHeaderApi.getStatus())) {
					status = "S";
					dtSapPostHeader.setStatus("S");
				} else {
					status = "F";
					dtSapPostHeader.setStatus("F");
				}
		
				// update status dtSapPostHeader
				sapHandleTransactionTypeCancelSaleNormalService.updateDtSapPostHeader(dtSapPostHeader);
			}
		
			// call service cancel reserve to send input to sap and keep response from sap
			// XXXX
		
			// Step 4 check response from sap then update status table sap_transaction
		
			dtSapTransaction.setStatus(status);
			sapHandleTransactionTypeCancelSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
		
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);
		}
	
	   @RequestMapping(value = prefixPath + "/cancel-sync-type23", method = RequestMethod.POST, produces = {"application/json" })
		public ResponseEntity<String> cancelSyncType23(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		InsertSapTransactionAndQueueCloudBean input = new InsertSapTransactionAndQueueCloudBean();
		InsertSapTransactionAndQueueCloudBean ress = new InsertSapTransactionAndQueueCloudBean();
		ress.setResultCode("50000");
		try {
		
			input = gson.fromJson(jsonRequest, InsertSapTransactionAndQueueCloudBean.class);
			//input.getDocNo() = CN no
			//input.getDocNoOptional() = receipt no
			
			DtSapTransaction dtSapTransaction = sapHandleTransactionTypeCancelSaleNormalService.insertSapTransaction(input.getDocNo(), input.getCompany(), Long.valueOf(input.getTransactionType()), input.getUserId());                   
			DtSapOrderHeader dtSapOrderHeader = null;
			DtSapOrderHeader dtSapOrderHeaderUnLock = null ;
		
			//update 15012026 comment unLockOrderBeforeUpdate23
			dtSapOrderHeaderUnLock = sapHandleTransactionTypeCancelSaleNormalService.unLockOrderBeforeUpdate23(dtSapTransaction , input.getDocNoOptional());	
			dtSapOrderHeader = sapHandleTransactionTypeCancelSaleNormalService.queryInfoAndInsertSaleorder(dtSapTransaction , input.getDocNoOptional());
		
			
		
			// call service sale order to send input to sap and keep response from sap
			
			//unlock
			 if(dtSapOrderHeaderUnLock!=null) { // unlock order 
				 
				   
				   DtSapOrderHeader dtSapOrderHeaderApi = sapHandleTransactionTypeCancelSaleNormalService.callSaleorderApi(dtSapOrderHeaderUnLock);
				
				   if(dtSapOrderHeaderApi.getStatus().equals("S")) {
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
					   sapHandleTransactionTypeCancelSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeaderUnLock);
				   }catch (Exception e) {
					e.printStackTrace();
				   }  
		       }
			
			
			if (dtSapOrderHeader != null) {
				// call service post transaction to send input to sap and keep response from sap
				DtSapOrderHeader dtSapOrderHeaderrApi = sapHandleTransactionTypeCancelSaleNormalService
						.callSaleorderApi(dtSapOrderHeader);
		
				if ("S".equals(dtSapOrderHeaderrApi.getStatus())) {
					// status = "S" ;
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
				   ress.setResultCode("20000");
				   ress.setSapTranId(String.valueOf(dtSapTransaction.getSapTranId()));
				} else {
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
				   ress.setResultCode("50000");
				   String errMessage = BeanUtil.isNotEmpty(dtSapOrderHeaderrApi.getRes_Message()) ? dtSapOrderHeaderrApi.getRes_Message(): dtSapOrderHeaderrApi.getRes_MessageDesc();
				   ress.setErrMessage(errMessage);
				   dtSapTransaction.setStatus("FF");
				   sapHandleTransactionTypeCancelSaleNormalService.updateDtSapTransactionAfterWorkflow(dtSapTransaction);
				}
		
				// update status dtSapPostHeader
				try {
					sapHandleTransactionTypeCancelSaleNormalService.updateDtSapOrderHeader(dtSapOrderHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
		
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		
		return new ResponseEntity<String>(gson.toJson(ress), httpHeaders, HttpStatus.OK);
		}
	   	   	    
	    @RequestMapping(value = prefixPath +"/call-queue-type2223",method = RequestMethod.POST, produces = { "application/json" })
		@RegisterReflectionForBinding({DTCreateReceiptRequest.class,CreateReceiptResponse.class})
		public ResponseEntity<String> callQueue(@RequestBody String jsonReq) {
			final HttpHeaders httpHeaders= new HttpHeaders();
			Map<String,String> properties = new HashMap<>();
			properties.put("Content-Type", "application/json;charset=utf-8");
			httpHeaders.setAll(properties);
			Gson gson = new Gson();
			InsertSapTransactionAndQueueCloudBean input = new InsertSapTransactionAndQueueCloudBean();
			
			input = gson.fromJson(jsonReq, InsertSapTransactionAndQueueCloudBean.class);
			
			try {
		    	
        		DTSapConsumeQueueMessage in = new DTSapConsumeQueueMessage();
        		in.setSapTranId(Long.valueOf(input.getSapTranId()));
        		in.setTransactionType(input.getTransactionType());
        		in.setEnv(springProfile.getActive());
        		queueSender.sendMessage(input.getQueueName(), in).block();

		    	}catch (Exception e) {
					e.printStackTrace();
				}
			
			return new ResponseEntity<String>("{\"resultCode\":\"20000\"}", httpHeaders, HttpStatus.OK);

		}
}
