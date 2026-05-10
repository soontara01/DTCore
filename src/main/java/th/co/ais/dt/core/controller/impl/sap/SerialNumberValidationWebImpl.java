package th.co.ais.dt.core.controller.impl.sap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.ItemsSerialnumberValidation;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryProductStockRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.QueryProductStockResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.SaleOrderNoRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap285ResBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.SerialnumberValidation;
import th.co.ais.dt.core.service.core.impl.sap.dto.SerialnumberValidationCmBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.SerialnumberValidationCmValue;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.entity.util.DtSapWsSerialNumber;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@RestController
@Slf4j
@AllArgsConstructor
public class SerialNumberValidationWebImpl {

final String prefixPath = "api/sap-ws/v1";
	
	private final ISapCallApiService sapCallApiService ;
	private final LovMasterService lovMasterService ;
	private final DTConfig dTConfig;
	
	@RequestMapping(value = prefixPath +"/serialnumber-validation",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> serialnumberValidation(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		SerialnumberValidation res = null ;
		Gson gson = new Gson();
		SerialnumberValidation input = new SerialnumberValidation();
		try {
			 input = gson.fromJson(jsonRequest, SerialnumberValidation.class);

			 //res = sapCallApiService.callSerialnumberValidationApi(input);
			 res = callSerialnumberValidationApi(input);
			 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/queryProductStock",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> queryProductStock(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
//		QueryProductStockResponse queryProductStockResponse = null ;
//		CommonResponseBean commonResponseBean = new CommonResponseBean();
		Object res = null ;
		
		QueryProductStockRequest input = new QueryProductStockRequest();
		try {
			 input = gson.fromJson(jsonRequest, QueryProductStockRequest.class);
			 
			 if (BeanUtil.isEmpty(input.getLocationCode())){
				 res = new CommonResponseBean("50000", "F", "Please input LocationCode");
			 }
			 else if (BeanUtil.isEmpty(input.getCompanyCode())){
				 res = new CommonResponseBean("50000", "F", "Please input CompanyCode");
			 }
			 else if (BeanUtil.isEmpty(input.getMatCode())){
				 res = new CommonResponseBean("50000", "F", "Please input MatCode");
			 }
			 else {
				 res = sapCallApiService.callQueryProductStockApi(input);

			 }

			 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/getSaleOrderNo",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> getSaleOrderNo(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
		Object res = null ;
		
		SaleOrderNoRequest input = new SaleOrderNoRequest();
		try {
			 input = gson.fromJson(jsonRequest, SaleOrderNoRequest.class);
			 
			 if (BeanUtil.isEmpty(input.getDocNo())){
				 res = new CommonResponseBean("50000", "F", "Please input DocNo");
			 }
			 else if (BeanUtil.isEmpty(input.getCompany())){
				 res = new CommonResponseBean("50000", "F", "Please input Company");
			 }
			 else {
				 res = sapCallApiService.getSaleOrderNo(input);

			 }
			 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/queryProductStockNonSerial",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> queryProductStockNonSerial(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
		Object res = null ;
		
		QueryProductStockRequest input = new QueryProductStockRequest();
		try {
			 input = gson.fromJson(jsonRequest, QueryProductStockRequest.class);
			 
			 if (BeanUtil.isEmpty(input.getLocationCode())){
				 res = new CommonResponseBean("50000", "F", "Please input LocationCode");
			 }
			 else if (BeanUtil.isEmpty(input.getCompanyCode())){
				 res = new CommonResponseBean("50000", "F", "Please input CompanyCode");
			 }
			 else if (BeanUtil.isEmpty(input.getListMatCode())){
				 res = new CommonResponseBean("50000", "F", "Please input listMatCode");
			 }
			 else {
				 res = sapCallApiService.sapQueryStockNonSerial(input);

			 }

			 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		
	}
	
	private SerialnumberValidation callSerialnumberValidationApi(SerialnumberValidation input) {
		SerialnumberValidation ress = null ;
		boolean checkCallSerialnumberValidationCm = false;
		boolean checkCallSerialnumberValidationInvoiceDtl = false;
		try {
			HttpClientUtilDT req = new HttpClientUtilDT();
			Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
	        List<LovMaster> checkCallSap = lovMasterService.listLovMasterByCriteria("SAP_S4_SERIAL_NUMBER_CONFIG", "SAP_S4_SERIAL_NUMBER_CONFIG", null, null, "Y");
	        
	        Gson g = new Gson();
			log.info(g.toJson(input));
			
	        if(BeanUtil.isNotEmpty(checkCallSap)) {
	        	SerialnumberValidation serialnumberValidation = new SerialnumberValidation();
//	        	serialnumberValidation.setMessageDesc(input.getMessageDesc()!=null?input.getMessageDesc():"");
	        	serialnumberValidation.setMessageDesc("Search successful");
	        	serialnumberValidation.setMessageID(input.getMessageID()!=null?input.getMessageID():"");
	        	serialnumberValidation.setPartnerName(input.getPartnerName()!=null?input.getPartnerName():"");
	        	serialnumberValidation.setPartnerMessageID(input.getPartnerMessageID()!=null?input.getPartnerMessageID():"");
	        		        	
	        	List<ItemsSerialnumberValidation> itemsList = new ArrayList<ItemsSerialnumberValidation>();
	        	
	        	 if(BeanUtil.isNotEmpty(input.getItems())) {
	        		 for (ItemsSerialnumberValidation element : input.getItems()) {

	        			 DtSapWsSerialNumber dtSapWsSerialNumber =  sapCallApiService.getDtSapWsSerialNumber(element.getSerialNumber());

	        			 if(BeanUtil.isNotEmpty(dtSapWsSerialNumber)) {
			     	         ItemsSerialnumberValidation items = new ItemsSerialnumberValidation();
				     	     items.setMessageType(dtSapWsSerialNumber.getMessageType()!=null?dtSapWsSerialNumber.getMessageType():"");
				     	     items.setMessageDesc(dtSapWsSerialNumber.getMessageDesc()!=null?dtSapWsSerialNumber.getMessageDesc():"");
				     	     items.setSerialNumber(dtSapWsSerialNumber.getSerialNumber()!=null?dtSapWsSerialNumber.getSerialNumber():"");
				     	     items.setUniqueItemIdentifier(dtSapWsSerialNumber.getUniqueitemidentifier()!=null?dtSapWsSerialNumber.getUniqueitemidentifier():"");
				     	     items.setMaterial(dtSapWsSerialNumber.getMaterial()!=null?dtSapWsSerialNumber.getMaterial():"");
				     	     items.setValidFlag(dtSapWsSerialNumber.getValidFlag()!=null?dtSapWsSerialNumber.getValidFlag():"");
				     	     items.setPlant(dtSapWsSerialNumber.getPlant()!=null?dtSapWsSerialNumber.getPlant():"");
					     	 items.setStorageLocation(dtSapWsSerialNumber.getStorageLocation()!=null?dtSapWsSerialNumber.getStorageLocation():"");
					     	 items.setStockType(dtSapWsSerialNumber.getStockType()!=null?dtSapWsSerialNumber.getStockType():"");
					     	 items.setSoldTo(dtSapWsSerialNumber.getSoldTo()!=null?dtSapWsSerialNumber.getSoldTo():"");
					     	 items.setDeliveryNumber(dtSapWsSerialNumber.getDeliveryNumber()!=null?dtSapWsSerialNumber.getDeliveryNumber():"");
					     	 items.setDeliveryDate(dtSapWsSerialNumber.getDeliveryDate()!=null?dtSapWsSerialNumber.getDeliveryDate():"");
					     	 items.setBillingDocument(dtSapWsSerialNumber.getBillingDocument()!=null?dtSapWsSerialNumber.getBillingDocument():"");
					     	 items.setBillingDate(dtSapWsSerialNumber.getBillingDate()!=null?dtSapWsSerialNumber.getBillingDate():"");
					     	 items.setPaymentTerm(dtSapWsSerialNumber.getPaymentTerm()!=null?dtSapWsSerialNumber.getPaymentTerm():"");
					     	 items.setPriceListType(dtSapWsSerialNumber.getPricelistType()!=null?dtSapWsSerialNumber.getPricelistType():"");
					     	 items.setItemCat(dtSapWsSerialNumber.getItemCat()!=null?dtSapWsSerialNumber.getItemCat():"");
					     	 items.setUnitPrice(dtSapWsSerialNumber.getUnitPrice()!=null?dtSapWsSerialNumber.getUnitPrice():"");
					     	
					     	 itemsList.add(items);
	        			 }
	        			 else {
	        				 ItemsSerialnumberValidation items = new ItemsSerialnumberValidation();
				     	     items.setMessageType("S");
				     	     items.setMessageDesc("Search successful");
				     	     items.setSerialNumber(element.getSerialNumber());
				     	     items.setUniqueItemIdentifier("");
				     	     items.setMaterial("");
				     	     items.setValidFlag("0");
				     	     items.setPlant("");
					     	 items.setStorageLocation("");
					     	 items.setStockType("");
					     	 items.setSoldTo("");
					     	 items.setDeliveryNumber("");
					     	 items.setDeliveryDate("");
					     	 items.setBillingDocument("");
					     	 items.setBillingDate("");
					     	 items.setPaymentTerm("");
					     	 items.setPriceListType("");
					     	 items.setItemCat("");
					     	 items.setUnitPrice("");
					     	
					     	 itemsList.add(items);
	        			 }

					}
	        		 
	        	 }
	        	
	        	serialnumberValidation.setItems(itemsList);
	        	ress = serialnumberValidation;
	        	
	        }
	        else {
	        	
				
				List<LovMaster> apikey186 = lovMasterService.listLovMasterByCriteria("SAP_S4_API_186_KEY", "SAP_S4_API_186_KEY",
						null, null, "Y");
				
				 String url = "";
			        if(BeanUtil.isNotEmpty(apikey186.get(0).getLovAttribute03())) {
			        	 properties.put("APIKey", apikey186.get(0).getLovAttribute03());
			        	 url = dTConfig.getUrl().getSerialnumbervalidationinter();
			        }else {
			        	properties.put("x-api-key", apikey186.get(0).getLovVal());
				        url = dTConfig.getUrl().getSerialnumbervalidation();
			        }
				
				

				
				List<LovMaster> timeout = lovMasterService.listLovMasterByCriteria("SAPS4_186_TIMEOUT", "SAPS4_186_TIMEOUT",
						null, null, "Y");

				String timeoutS = "";
				if (BeanUtil.isNotEmpty(timeout)) {
					timeoutS = timeout.get(0).getLovVal();
				}
	        	
				String resSap = req.HttpClient(properties, url, g.toJson(input), "POST", timeoutS) ;
	
				if(resSap != null) {
					log.info(resSap);
					try {
						ress = g.fromJson(resSap, SerialnumberValidation.class) ;
						if(BeanUtil.isEmpty(ress.getItems())||ress.getItems().size()<=0) {
							checkCallSerialnumberValidationCm = true;	
						} else {
							if(BeanUtil.isEmpty(ress.getItems().get(0).getMaterial())) {
								checkCallSerialnumberValidationCm = true;	
							}else {
								// mat code not null but other is null
								// check invoice for cn
								if(BeanUtil.isEmpty(ress.getItems().get(0).getPlant()) && 
										BeanUtil.isEmpty(ress.getItems().get(0).getStorageLocation()) && 
										BeanUtil.isEmpty(ress.getItems().get(0).getStockType()) && 
//										BeanUtil.isEmpty(ress.getItems().get(0).getSoldTo()) && 
										BeanUtil.isEmpty(ress.getItems().get(0).getLegalEntity()) && 
//										BeanUtil.isEmpty(ress.getItems().get(0).getDeliveryNumber()) && 
//										BeanUtil.isEmpty(ress.getItems().get(0).getDeliveryDate()) &&
//										BeanUtil.isEmpty(ress.getItems().get(0).getBillingDocument()) &&
//										BeanUtil.isEmpty(ress.getItems().get(0).getBillingDate()) &&
//										BeanUtil.isEmpty(ress.getItems().get(0).getPaymentTerm()) &&
										BeanUtil.isEmpty(ress.getItems().get(0).getPriceListType())
//										BeanUtil.isEmpty(ress.getItems().get(0).getItemCat()) &&
//										BeanUtil.isEmpty(ress.getItems().get(0).getUnitPrice()) 
										) {
									checkCallSerialnumberValidationInvoiceDtl = true ;
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					checkCallSerialnumberValidationCm = true;
				}
				
				if(checkCallSerialnumberValidationCm) {
					//check flag SAP_S4_INVOICE_CM
					List<LovMaster> lovSapS4InvoiceCm = lovMasterService.listLovMasterByCriteria("SAP_S4_INVOICE_CM", "SAP_S4_INVOICE_CM", null, null, "Y");
					if(BeanUtil.isNotEmpty(lovSapS4InvoiceCm) && lovSapS4InvoiceCm.size() >0) {
						// validateInput
						SerialnumberValidation  validateInputRes = validateInput(input);
						if(BeanUtil.isNotEmpty(validateInputRes)) {
							return validateInputRes;
						}
						// call cm
						log.info("call SerialnumberValidation Cm !!!!");					
						Map<String, String> propertiesCm = new HashMap<String, String>();
						propertiesCm.put("Content-Type", "application/json");
						HttpClientUtilDT reqCm = new HttpClientUtilDT();
						propertiesCm = new HashMap<String, String>();
						propertiesCm.put("Content-Type", "application/json");
						List<LovMaster> apiKey = lovMasterService.listLovMasterByCriteria("SAP_S4_INVOICE_CM_KEY", "SAP_S4_INVOICE_CM_KEY", null, null, "Y");
						propertiesCm.put("x-api-key", apiKey.get(0).getLovVal());
						
						String timeoutCm = "";
						List<LovMaster> timeouts = lovMasterService.listLovMasterByCriteria("SAPS4_INVOICE_CM_TIMEOUT", "SAP_S4_INVOICE_CM_TIMEOUT", null, null, "Y");
						if (BeanUtil.isNotEmpty(timeouts)) {
							timeoutCm = timeout.get(0).getLovVal();
						}
						// map request
						SerialnumberValidationCmBean requestCm = new SerialnumberValidationCmBean();
						requestCm.setSystemCode("DT");
						String serialNo = "";
						if(BeanUtil.isNotEmpty(input.getItems())) {
							serialNo = BeanUtil.convertMatSapToTDM(input.getItems().get(0).getSerialNumber());
						}
						requestCm.setSerialNo(serialNo);
						String jsonReq = g.toJson(requestCm);
						log.info("request : "+jsonReq);
						String resCm = reqCm.HttpClient(propertiesCm, dTConfig.getUrl().getSerialnumbervalidationcm(), jsonReq , "POST", timeoutCm) ;
						log.info("response : "+resCm);
						
						// map data 
						ress =  mapResDataSerialnumberValidationCm(resCm,input);
						
						// Call SAP timeout
						ress.setMessageResCallSAP("Call SAP S4 timeout or connection error.");
					}
				}
				
				// invice dtl for cn same day 
				if(checkCallSerialnumberValidationInvoiceDtl) {
					ress = mapResDataSerialnumberValidationInvoiceDtl(input,ress);
				}
				
				if(BeanUtil.isNotEmpty(ress.getItems()) && BeanUtil.isNotEmpty(input.getItems())) {
					String outSerial = TDMDataUtility.getSerialNoDatabase(ress.getItems().get(0).getSerialNumber());
					String inSerial = TDMDataUtility.getSerialNoDatabase(input.getItems().get(0).getSerialNumber());
					
					if(inSerial.length() > 18) {
						outSerial = ress.getItems().get(0).getUniqueItemIdentifier();
					}
					
					if(!outSerial.equals(inSerial)) {
						
						ItemsSerialnumberValidation item = new ItemsSerialnumberValidation();
						item.setMessageType("E");
						item.setMessageDesc("SerialNumber input not equals output");
						item.setSerialNumber(input.getItems().get(0).getSerialNumber());
						item.setUniqueItemIdentifier("");
						item.setMaterial("");
						item.setValidFlag("");
						item.setPlant("");
						item.setStorageLocation("");
						item.setStockType("");

						item.setSoldTo("");
						item.setDeliveryNumber("");
						item.setDeliveryDate("");
				     	item.setBillingDocument("");
				     	item.setBillingDate("");
				     	item.setPaymentTerm("");
				     	item.setPriceListType("");
				     	item.setItemCat("");
				     	item.setUnitPrice("");
				     	item.setLegalEntity("");
				     	
						item.setResponseBy("DT");
						
						ress.setItems(Arrays.asList(item));
					}
				}
	        	
	        }
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return ress ;
	}
	
	private SerialnumberValidation mapResDataSerialnumberValidationCm(String responseCm,SerialnumberValidation input){
		SerialnumberValidation res = null;
		Gson g = new Gson();
		try {
			SerialnumberValidationCmBean resData = g.fromJson(responseCm, SerialnumberValidationCmBean.class);
			res = new SerialnumberValidation();
			List<ItemsSerialnumberValidation> listItem = new ArrayList<ItemsSerialnumberValidation>();
			res.setMessageDesc("Search successful");
			res.setMessageID(input.getMessageID()!=null?input.getMessageID():"");
			res.setPartnerName(input.getPartnerName()!=null?input.getPartnerName():"");
			res.setPartnerMessageID(input.getPartnerMessageID()!=null?input.getPartnerMessageID():"");
			
			SerialnumberValidationCmValue resValue = new SerialnumberValidationCmValue();
			String serial = "";
			String validFlag = "0";
			if(BeanUtil.isNotEmpty(resData) && BeanUtil.isNotEmpty(resData.getObjectValue())) {
				resValue = resData.getObjectValue().get(0);
				validFlag = "2";
				serial = BeanUtil.convertSerialForSap(resValue.getSERIAL_NO());
			} else {
				serial = BeanUtil.convertSerialForSap(input.getItems().get(0).getSerialNumber());
			}
			
			ItemsSerialnumberValidation item = new ItemsSerialnumberValidation();
			item.setMessageType("S");
			item.setMessageDesc("Search successful");
			item.setSerialNumber(serial);
			item.setUniqueItemIdentifier("");
			item.setMaterial(BeanUtil.isNotEmpty(resValue.getMATERIAL_CODE())?resValue.getMATERIAL_CODE():"");
			item.setValidFlag(validFlag);
			item.setPlant(BeanUtil.isNotEmpty(resValue.getPLANT_HQ())?resValue.getPLANT_HQ():"");
			item.setStorageLocation("");//
			item.setStockType("");//
			item.setSoldTo(BeanUtil.isNotEmpty(resValue.getPLANT_HQ())?resValue.getPLANT_HQ():"");
			item.setDeliveryNumber(BeanUtil.isNotEmpty(resValue.getDELIVERY_NO())?resValue.getDELIVERY_NO():"");
			item.setDeliveryDate(BeanUtil.isNotEmpty(resValue.getDELIVERY_DATE())?setDateFormatSap(resValue.getDELIVERY_DATE()):"");
	     	item.setBillingDocument(BeanUtil.isNotEmpty(resValue.getINVOICE_NO())?resValue.getINVOICE_NO():"");
	     	item.setBillingDate(BeanUtil.isNotEmpty(resValue.getINVOICE_DATE())?setDateFormatSap(resValue.getINVOICE_DATE()):"");
	     	item.setPaymentTerm(BeanUtil.isNotEmpty(resValue.getPAYMENT_TERM())?resValue.getPAYMENT_TERM():"");
	     	item.setPriceListType(BeanUtil.isNotEmpty(resValue.getPRICE_LIST_TYPE())?resValue.getPRICE_LIST_TYPE():"");
	     	item.setItemCat(BeanUtil.isNotEmpty(resValue.getITEM_CAT())?resValue.getITEM_CAT():"");
	     	item.setUnitPrice(BeanUtil.isNotEmpty(resValue.getPRICE())?resValue.getPRICE():"");
	     	item.setLegalEntity(BeanUtil.isNotEmpty(resValue.getBP_CODE())?resValue.getBP_CODE():"");
	     	item.setResponseBy("CM");
	     	
			listItem.add(item);
			res.setItems(listItem);
			
		} catch (Exception e) {
			log.error("Error mapDataSerialnumberValidationCm !!!!");
			e.printStackTrace();
			return res;
		}
		return res;
	}
	
	private SerialnumberValidation validateInput(SerialnumberValidation input) {
		SerialnumberValidation validationRes = null;
		try {
			if(BeanUtil.isEmpty(input.getItems()) || BeanUtil.isEmpty(input.getItems().get(0).getSerialNumber())) {
				validationRes = new SerialnumberValidation();
				validationRes.setMessageDesc("");
				validationRes.setMessageID("");
				validationRes.setPartnerName("");
				validationRes.setPartnerMessageID("");
				List<ItemsSerialnumberValidation>items = new ArrayList<ItemsSerialnumberValidation>();
				
				ItemsSerialnumberValidation item = new ItemsSerialnumberValidation();
				item.setMessageType("E");
				item.setMessageDesc("SerialNumber is required");
				item.setSerialNumber("");
				item.setUniqueItemIdentifier("");
				item.setMaterial("");
				item.setValidFlag("0");
				item.setPlant("");
				item.setStorageLocation("");
				item.setStockType("");
		     	item.setSoldTo("");
		     	item.setDeliveryNumber("");
		     	item.setDeliveryDate("");
		     	item.setBillingDocument("");
		     	item.setBillingDate("");
		     	item.setPaymentTerm("");
		     	item.setPriceListType("");
		     	item.setItemCat("");
		     	item.setUnitPrice("");
		     	item.setLegalEntity("");
		     	
		     	items.add(item);
		     	
		     	validationRes.setItems(items);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error validateInput !!!!");
			e.printStackTrace();
		}
		return validationRes;
	}
	
	private String setDateFormatSap (String input) {
		String dateTime = "";
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = inputFormat.parse(input);
			dateTime = outputFormat.format(date);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dateTime;
	}
	
	private SerialnumberValidation mapResDataSerialnumberValidationInvoiceDtl(SerialnumberValidation input , SerialnumberValidation ressap){
		SerialnumberValidation res = null;
		Gson g = new Gson();
		try {
			
			String serialNo = BeanUtil.lPadNumber(input.getItems().get(0).getSerialNumber(), 18);
			String plant = "";
			String sloc = "";
			String stockType = "";
			String matCode = ressap.getItems().get(0).getMaterial();
			
			// checkout CN
			List<QueryPostTransactionBean> cnInvoice = sapCallApiService.getSerialCNInvoice(serialNo);
			if(BeanUtil.isNotEmpty(cnInvoice)) {
				plant = cnInvoice.get(0).getRETAILSTOREID();
				sloc = cnInvoice.get(0).getWORKSTATIONID();
				// matCode = cnInvoice.get(0).getITEMID();
				stockType = "01";

			}else {
				// checkout cancel
				List<QueryPostTransactionBean> cabcelInvoice = sapCallApiService.getSerialCancelInvoice(serialNo);
				if (BeanUtil.isNotEmpty(cabcelInvoice)) {
					plant = cabcelInvoice.get(0).getRETAILSTOREID();
					sloc = cabcelInvoice.get(0).getWORKSTATIONID();
					// matCode = cabcelInvoice.get(0).getITEMID();
					stockType = "01";

					if (BeanUtil.isNotEmpty(cabcelInvoice.get(0).getREF_NO()) && cabcelInvoice.get(0).getREF_NO().startsWith("D")) {
						// Check CN Receipt ref DEPOSIT
						stockType = "07";
					}

				} else {
					return ressap;
				}
			}
			res = new SerialnumberValidation();
			List<ItemsSerialnumberValidation> listItem = new ArrayList<ItemsSerialnumberValidation>();
			ItemsSerialnumberValidation item = new ItemsSerialnumberValidation();
			item.setMessageType("S");
			item.setMessageDesc("Search successful");
			item.setSerialNumber(serialNo);
			item.setUniqueItemIdentifier(serialNo);
			item.setMaterial(matCode);
			item.setValidFlag("1");
			item.setPlant(plant);
			item.setStorageLocation(sloc);
			item.setStockType(stockType);
			
			
			item.setSoldTo("");
			item.setDeliveryNumber("");
			item.setDeliveryDate("");
	     	item.setBillingDocument("");
	     	item.setBillingDate("");
	     	item.setPaymentTerm("");
	     	item.setPriceListType("");
	     	item.setItemCat("");
	     	item.setUnitPrice("");
	     	item.setLegalEntity("");
	     	
	     	
			listItem.add(item);
			res.setItems(listItem);
			
		} catch (Exception e) {
			log.error("Error mapDataSerialnumberValidationCm !!!!");
			e.printStackTrace();
			return res;
		}
		return res;
	}
}
