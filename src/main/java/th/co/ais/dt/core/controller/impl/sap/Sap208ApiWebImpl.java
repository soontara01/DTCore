package th.co.ais.dt.core.controller.impl.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.CreateGoodsReturnEclaim208;
import th.co.ais.dt.core.service.core.impl.sap.dto.PickingDocumentIn;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap208Bean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap208Service;
import th.co.ais.dt.entity.pm.ReceiptMst;
import th.co.ais.dt.entity.so.OrderMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.service.core.impl.sap.dto.Query208Bean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@RestController
@Slf4j
@AllArgsConstructor
public class Sap208ApiWebImpl {
	
	final String prefixPath = "api/sap-handle-208/v1";
	
	private ISap208Service sap208Service ;
	private final DTConfig dTConfig;

	@PostMapping(path = prefixPath + "/208-inspection-result-update", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<String> handle1208InspectionResultUpdate(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		String pickingRes = null;

		try {
			Sap208Bean sapReq = gson.fromJson(jsonRequest, Sap208Bean.class);
			if (sapReq != null) {
				//step 1 query order if from dt 
				List<Query208Bean> listFromDt = sap208Service.listOrderFromDT(sapReq.getORGOrder()) ;
				if(BeanUtil.isEmpty(listFromDt)) {
					return new ResponseEntity<String>(gson.toJson(new CommonResponseBean("50000", "", "NOT_FROM_DT")), httpHeaders, HttpStatus.OK);
				}
				
				// step 1.1  find receipt no
				String receiptNoAndReceiptType = findReceiptNo(listFromDt.get(0).getDoc_no(), listFromDt.get(0).getCompany());
				if(BeanUtil.isEmpty(receiptNoAndReceiptType)) {
					return new ResponseEntity<String>(gson.toJson(new CommonResponseBean("50000", "", "RECEIPT "+listFromDt.get(0).getDoc_no()+" not found on cloud")), httpHeaders, HttpStatus.OK);
				}
				
				
				String receiptNo = receiptNoAndReceiptType.split("\\|")[0] ;
				String receiptType = receiptNoAndReceiptType.split("\\|")[1] ;
				String cusNameAbb = "";
				String address1Abb = "";
				String address2Abb = "";
				if(!"FULL".equals(receiptType)) {
					OrderMst om = sap208Service.getOrderMst(receiptNo, listFromDt.get(0).getCompany());
					 cusNameAbb = om.getCustomerName();
					 address1Abb = om.getShipCustAddr1();
					 address2Abb = om.getShipCustAddr2();
					//return new ResponseEntity<String>(gson.toJson(new CommonResponseBean("50000", "", "RECEIPT  ABB "+receiptNo+" CAN NOT CN")), httpHeaders, HttpStatus.OK);
				}
				
				//step2
				List<Query208Bean> listDetailReceipt = sap208Service.listDetailReceipt(receiptNo, listFromDt.get(0).getCompany());
				if(BeanUtil.isEmpty(listDetailReceipt)) {  
					return new ResponseEntity<String>(gson.toJson(new CommonResponseBean("50000", "", "NOT FOUND ITEM CAN BE CN")), httpHeaders, HttpStatus.OK);
				}
				
				//step3  build req goods return eclaim
				CreateGoodsReturnEclaim208 reqEcliam = new CreateGoodsReturnEclaim208();
				reqEcliam.setAutoTransferFlg("N") ;
				reqEcliam.setLocationCode(listDetailReceipt.get(0).getLocation_code());
				reqEcliam.setReceiptNo(listDetailReceipt.get(0).getReceipt_num());
				reqEcliam.setCompany(listDetailReceipt.get(0).getReceipt_company());
				String cnMemo = "";
				
				reqEcliam.setCustomerName(cusNameAbb);
				reqEcliam.setAddress1(address1Abb);
				if(BeanUtil.isNotEmpty(address2Abb)) {
					reqEcliam.setAddress2(address2Abb);
				}
				
				
				List<CreateGoodsReturnEclaim208> returnItem = new ArrayList<>();
				List<CreateGoodsReturnEclaim208> cnMemoItem = new ArrayList<>();
				Map<String,String> mapSerial = new HashMap<>();
				Map<String,String> mapNonSerial = new HashMap<>();
				for(Sap208Bean itemel : sapReq.getItem()) {

					if(BeanUtil.isNotEmpty(itemel.getSerialNo()) || BeanUtil.isNotEmpty(itemel.getIUID()) ) {
						if(BeanUtil.isNotEmpty(itemel.getIUID()) && BeanUtil.isEmpty(itemel.getSerialNo())) {
							String[] AIUID = itemel.getIUID().split(",");
							
							for(String el : AIUID ) {
								mapSerial.put(el, itemel.getItemNo()  );
							}
						}else if(BeanUtil.isNotEmpty(itemel.getIUID()) && BeanUtil.isNotEmpty(itemel.getSerialNo())) {
							String[] AIUID = itemel.getIUID().split(",");
							String[] Aserial = itemel.getSerialNo().split(",");
							
							int i = 0 ;
							for(String el : AIUID ) {
								if(el.length() > 18) {
									//mapSerial.put(itemel.getIUID(), BeanUtil.convertMatSapToTDM(itemel.getMatCode()) + "|" + itemel.getItemNo()  );
									mapSerial.put(el, itemel.getItemNo()  );
								}else {
									//mapSerial.put(TDMDataUtility.getSerialNoDatabase(itemel.getSerialNo()), BeanUtil.convertMatSapToTDM(itemel.getMatCode()) + "|" + itemel.getItemNo() );
									mapSerial.put(TDMDataUtility.getSerialNoDatabase(Aserial[i]),  itemel.getItemNo() );
								}
								i++;
							}
						}
						
						else {
							String[] Aserial = itemel.getSerialNo().split(",");
							for(String el : Aserial ) {
								mapSerial.put(TDMDataUtility.getSerialNoDatabase(el),  itemel.getItemNo() );
							}
							

						}
						
						
					}else {
						//mapNonSerial.put(BeanUtil.convertMatSapToTDM(itemel.getMatCode()), BeanUtil.convertMatSapToTDM(itemel.getMatCode()) + "|" + itemel.getItemNo() ) ;
						mapNonSerial.put(BeanUtil.convertMatSapToTDM(itemel.getMatCode()), itemel.getItemNo() ) ;
					}
					
					if(BeanUtil.isNotEmpty(itemel.getSubDocument())) {
						cnMemo = itemel.getSubDocument() ; // expec  1 cnmemo
					}
				} 
				
				reqEcliam.setCnMemoNo(cnMemo) ;
				
				for(Query208Bean el : listDetailReceipt) {
					if(el.getFree_goods_flg().equals("N")) {
						if(el.getMat_type().equals("Serial")) {
							if(mapSerial.containsKey(el.getSerial_no())) {
								CreateGoodsReturnEclaim208 item = new CreateGoodsReturnEclaim208();
								item.setSerialNo(el.getSerial_no());
								item.setQty("1");
								item.setMatCode(el.getMat_code());
								returnItem.add(item);
								
								CreateGoodsReturnEclaim208 itemCn = new CreateGoodsReturnEclaim208();
								itemCn.setInvoiceSeq(el.getInv_seq());
								itemCn.setItemNo(mapSerial.get(el.getSerial_no())) ;
								cnMemoItem.add(itemCn) ;
			
							}
						}else {
							if(mapNonSerial.containsKey(el.getMat_code())) {
								CreateGoodsReturnEclaim208 item = new CreateGoodsReturnEclaim208();
								item.setSerialNo("");
								item.setQty(el.getQty());
								item.setMatCode(el.getMat_code());
								returnItem.add(item);
								
								CreateGoodsReturnEclaim208 itemCn = new CreateGoodsReturnEclaim208();
								itemCn.setInvoiceSeq(el.getInv_seq());
								itemCn.setItemNo(mapNonSerial.get(el.getMat_code())) ;
								cnMemoItem.add(itemCn) ;
							}
						}
					}else {
						// cnmemo only
						
						if(el.getMat_type().equals("Serial")) {
							if(mapSerial.containsKey(el.getSerial_no())) {
								
								
								CreateGoodsReturnEclaim208 itemCn = new CreateGoodsReturnEclaim208();
								itemCn.setInvoiceSeq(el.getInv_seq());
								itemCn.setItemNo(mapSerial.get(el.getSerial_no())) ;
								cnMemoItem.add(itemCn) ;
			
							}
						}else {
							if(mapNonSerial.containsKey(el.getMat_code())) {
								
								
								CreateGoodsReturnEclaim208 itemCn = new CreateGoodsReturnEclaim208();
								itemCn.setInvoiceSeq(el.getInv_seq());
								itemCn.setItemNo(mapNonSerial.get(el.getMat_code())) ;
								cnMemoItem.add(itemCn) ;
							}
						}
						
					}
				}
				
				reqEcliam.setReturnItems(returnItem);
				reqEcliam.setCnMemoItem(cnMemoItem) ;
				
				
				System.out.println(gson.toJson(reqEcliam));
				pickingRes = callCreateGoodsReturnEClaim(gson.toJson(reqEcliam));
				
			}
		} catch (Exception e) {
			log.error("208-inspection-result-upfate : " + jsonRequest);
			log.error("Error", e);
			
			pickingRes = gson.toJson(new CommonResponseBean("50000", "", e.getMessage()));
		}
		return new ResponseEntity<String>(pickingRes, httpHeaders, HttpStatus.OK);
	}
	
	private String callCreateGoodsReturnEClaim(String req) {
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        log.info("callCreateGoodsReturnEClaim : " + req);
        
		//String res = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/after-sale/v1/create-goods-return-e-claim", req , "POST", null) ;
		String res = c.HttpClient(properties, dTConfig.getUrl().getDtcoresaleout() + "/api/after-sale/v1/create-goods-return-e-claim", req , "POST", null) ;
		
		log.info("Response callCreateGoodsReturnEClaim : " + res);
		
		return res;
	}
	
	private String findReceiptNo(String receiptNo , String company) {
		//cloud
		return sap208Service.findReceiptNo(receiptNo, company); 
		
		//onprem
		//return sap208Service.findReceiptNoOnprem(receiptNo, company);
	}
	

	
}
