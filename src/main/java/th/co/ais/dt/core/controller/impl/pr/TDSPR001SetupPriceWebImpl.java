package th.co.ais.dt.core.controller.impl.pr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.service.core.dto.SetupPriceResponse;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.pr.ITDSPR001SetupPriceService;
import th.co.ais.dt.service.core.interfaces.pr.ITDSPR002LoadPriceExcelService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.TDSPR001SetupPriceBean;
import th.co.ais.dt.controller.dto.TDSPR002LoadPriceExcelBean;
import th.co.ais.dt.exception.ForceTerminateException;

@RestController
@Slf4j
@AllArgsConstructor
public class TDSPR001SetupPriceWebImpl  {
	final String prefixPath = "api/price/v1/TDSPR001";
	
	private final ITDSPR001SetupPriceService iTDSPR001SetupPriceService ;

	private final ITDSPR002LoadPriceExcelService iTDSPR002LoadPriceExcelService ;

	@RequestMapping(
			  value = prefixPath +"/query-criteria",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> queryCriteria(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			if(BeanUtil.isNotEmpty(request.getAction())) {
				switch (request.getAction()) {
					case "QUERY_CRITERIA":
						response = iTDSPR001SetupPriceService.queryCriteriaForTDSPR001(request);
						break;
					case "NEW_CRITERIA":
						response = iTDSPR001SetupPriceService.newCriteriaForTDSPR001();
						break;
					case "PRODUCT_CRITERIA":
						response = iTDSPR001SetupPriceService.productCriteriaForTDSPR001(request);
						break;
					case "QUERY_PRODUCT":
						response = iTDSPR001SetupPriceService.querySearchProduct(request);
						break;
					case "QUERY_PRODUCT_NEW":
						response = iTDSPR001SetupPriceService.querySearchProductNew(request);
						break;
					default:
						response.setResultCode("50000");
						response.setResultDescription("Input Action incorrect, Please try again [EX. QUERY_CRITERIA/ NEW_CRITERIA/ ...]");
						response.setDeveloperMessage("Input Action incorrect, Please try again [EX. QUERY_CRITERIA/ NEW_CRITERIA/ ...]");
						break;
				}
			}else {
				response.setResultCode("50000");
				response.setResultDescription("Please input Action");
				response.setDeveloperMessage("Please input Action");
			}
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(
			  value = prefixPath +"/query-price",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> queryPrice(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			response = iTDSPR001SetupPriceService.queryPriceList(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

	@RequestMapping(
			  value = prefixPath +"/view-price",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> viewPrice(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			response = iTDSPR001SetupPriceService.viewPriceList(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		
	}

	@RequestMapping(
			  value = prefixPath +"/check-product",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> checkProduct(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			response = iTDSPR001SetupPriceService.checkProductList(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(
			  value = prefixPath +"/get-data-price-of-product",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> getDataPriceOfProduct(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			response = iTDSPR001SetupPriceService.getDataPriceOfProduct(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

	@RequestMapping(
			  value = prefixPath +"/save-price",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding({TDSPR001SetupPriceBean.class,TDSPR002LoadPriceExcelBean.class})
	public ResponseEntity<String> savePrice(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR002LoadPriceExcelBean response = new TDSPR002LoadPriceExcelBean();
		List<String> listKey = null ;
		boolean insertkeyFlg = false ;
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			
			// prepare TDSPR002LoadPriceExcelBean
			TDSPR002LoadPriceExcelBean newRequest = new TDSPR002LoadPriceExcelBean();
			
//			request.getPriceGroupList()
//			request.getMatCodeList
			
			
//			request.getDescription
//			request.getPriceVatType()
//			request.getPriceIncVat()
//			request.getPriceExcVat()
//			request.getPriceVate()
//			request.getVatRate()
//			request.getPriceVatType()
//			request.getPriceDiffCash()
//			request.getPriceDiffCredit()
//			request.getEffectiveDate()
//			request.getExpireDate()
//			request.getUserId()
//			request.getPaymentMethod()
			newRequest.setCompany(request.getCompany().get(0)); 
			newRequest.setVatRate(request.getVatRate());
			newRequest.setExcelList(new ArrayList<>());
			
			
			 for(String matcode : request.getMatCodeList()) {
				 for(String groupPrice : request.getPriceGroupList()) {
					 SetupPriceResponse item = new SetupPriceResponse();
						item.setPriceGroup(groupPrice);
						item.setMatCode(matcode);
						item.setCompany(request.getCompany().get(0));
						item.setDescription(request.getDescription());
						item.setVatType(null);
						item.setVatRate(request.getVatRate());
						item.setIncVat(request.getPriceIncVat());
						item.setExcVat(null);
						item.setPriceVat(null);
						item.setPriceDiffCash(request.getPriceDiffCash());
						item.setPriceDiffCredit(request.getPriceDiffCredit());
						item.setEffectiveDate(request.getEffectiveDate());
						item.setExpireDate(request.getExpireDate());
						item.setPaymentMethod(request.getPaymentMethod());
						
						newRequest.getExcelList().add(item);
				 }
				 
			 }
			
//			request.getMatCodeList().stream().forEach(matEl->{
//
//				
//				request.getPriceGroupList().stream().forEach(groupEl->{
//					SetupPriceResponse item = new SetupPriceResponse();
//					item.setPriceGroup(groupEl);
//					item.setMatCode(matEl);
//					item.setCompany(request.getCompany().get(0));
//					item.setDescription(request.getDescription());
//					item.setVatType(null);
//					item.setVatRate(null);
//					item.setIncVat(request.getPriceIncVat());
//					item.setExcVat(jsonRequest);
//					item.setPriceVat(null);
//					item.setPriceDiffCash(request.getPriceDiffCash());
//					item.setPriceDiffCredit(request.getPriceDiffCredit());
//					item.setEffectiveDate(request.getEffectiveDate());
//					item.setExpireDate(request.getExpireDate());
//					item.setPaymentMethod(request.getPaymentMethod());
//					
//					newRequest.getExcelList().add(item);
//				});
//			});
			
			TDSPR002LoadPriceExcelBean responsevalidate = iTDSPR002LoadPriceExcelService.validateUpload(newRequest);
			
			if(BeanUtil.isNotEmpty(responsevalidate.getResultCode()) && (!responsevalidate.getResultCode().equals("20000") || BeanUtil.isNotEmpty(responsevalidate.getPriceFailList()) ) ) {
				
				if(responsevalidate.getResultCode().equals("50000")) {
					return new ResponseEntity<String>(gson.toJson(responsevalidate), httpHeaders, HttpStatus.OK);
				}
				
				StringBuffer err = new StringBuffer();
				responsevalidate.getPriceFailList().stream().forEach(el->{
					err.append(el.getErrorMsg() + "|") ;
				});
				
				throw new Exception(err.toString());
			}
			
			listKey = responsevalidate.getPriceOkList().stream().map(el->{
				return "SETUP_PRICE_SAVE_MATCODE=" + el.getCompany() + "_"+ el.getMatCode()+ "_"+ el.getPriceGroup()+ "_" + el.getPaymentMethod();
			}).distinct().collect(Collectors.toList());
			
			iTDSPR002LoadPriceExcelService.insertCheckDupTrans(listKey);
			insertkeyFlg = true ;
			
			response = iTDSPR002LoadPriceExcelService.saveUpdate(responsevalidate.getPriceOkList(),request.getUserId());
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			//log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}finally {
			if(insertkeyFlg) {
				try {
					iTDSPR002LoadPriceExcelService.deleteCheckDupTrans(listKey);
				}catch (Exception e) {
					log.error(e.getMessage());
				}
				
			}
		}
		
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

	@RequestMapping(
			  value = prefixPath +"/update-price",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding({TDSPR001SetupPriceBean.class,TDSPR002LoadPriceExcelBean.class})
	public ResponseEntity<String> updatePrice(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR002LoadPriceExcelBean response = new TDSPR002LoadPriceExcelBean();
		List<String> listKey = null ;
		boolean insertkeyFlg = false ;
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			
			// prepare TDSPR002LoadPriceExcelBean
			TDSPR002LoadPriceExcelBean newRequest = new TDSPR002LoadPriceExcelBean();
			
//			request.getPriceGroupList()
//			request.getMatCodeList
			
			
//			request.getDescription
//			request.getPriceVatType()
//			request.getPriceIncVat()
//			request.getPriceExcVat()
//			request.getPriceVate()
//			request.getVatRate()
//			request.getPriceVatType()
//			request.getPriceDiffCash()
//			request.getPriceDiffCredit()
//			request.getEffectiveDate()
//			request.getExpireDate()
//			request.getUserId()
//			request.getPaymentMethod()
			
			newRequest.setVatRate(request.getVatRate());
			newRequest.setExcelList(new ArrayList<>());
			
			
			 for(String priceId : request.getPriceId()) {
				 List<Object[]> priceOld = iTDSPR002LoadPriceExcelService.getPriceMstJoinProductMst(Long.valueOf(priceId));
				 if(priceOld!=null) {
					 
					 newRequest.setCompany(priceOld.get(0)[0].toString()); 
					 SetupPriceResponse item = new SetupPriceResponse();
					    item.setPriceId(priceId);
						item.setPriceGroup(priceOld.get(0)[1].toString());
						item.setMatCode(priceOld.get(0)[3].toString());
						item.setCompany(priceOld.get(0)[0].toString());
						item.setDescription((BeanUtil.isNotEmpty(request.getDescription()) ? request.getDescription() : ( priceOld.get(0)[2] != null ? priceOld.get(0)[2].toString() : null ) ) );  //
						item.setVatType(null);          
						item.setVatRate((BeanUtil.isNotEmpty(request.getVatRate()) ? request.getVatRate() : ( priceOld.get(0)[5] != null ? priceOld.get(0)[5].toString() : null ) ) );          //  
						item.setIncVat((BeanUtil.isNotEmpty(request.getPriceIncVat()) ? request.getPriceIncVat() : ( priceOld.get(0)[7] != null ? priceOld.get(0)[7].toString() : null ) ) );       //
						item.setExcVat(null);
						item.setPriceVat(null);
						item.setPriceDiffCash((BeanUtil.isNotEmpty(request.getPriceDiffCash()) ? request.getPriceDiffCash() : ( priceOld.get(0)[8] != null ? priceOld.get(0)[8].toString() : null ) ) );  //
						item.setPriceDiffCredit((BeanUtil.isNotEmpty(request.getPriceDiffCredit()) ? request.getPriceDiffCredit() : ( priceOld.get(0)[9] != null ? priceOld.get(0)[9].toString() : null ) )); //
						item.setEffectiveDate((BeanUtil.isNotEmpty(request.getEffectiveDate()) ? request.getEffectiveDate() : ( priceOld.get(0)[10] != null ? priceOld.get(0)[10].toString() : null ) ));  //
						item.setExpireDate((BeanUtil.isNotEmpty(request.getExpireDate()) ? request.getExpireDate() : ( priceOld.get(0)[11] != null ? priceOld.get(0)[11].toString() : null ) ));        //
						item.setPaymentMethod(priceOld.get(0)[4].toString());
						
						newRequest.getExcelList().add(item);
				 } 
			 }
						
			TDSPR002LoadPriceExcelBean responsevalidate = iTDSPR002LoadPriceExcelService.validateUpload(newRequest);
			
			if(BeanUtil.isNotEmpty(responsevalidate.getResultCode()) && (!responsevalidate.getResultCode().equals("20000") || BeanUtil.isNotEmpty(responsevalidate.getPriceFailList()) ) ) {
				
				if(responsevalidate.getResultCode().equals("50000")) {
					return new ResponseEntity<String>(gson.toJson(responsevalidate), httpHeaders, HttpStatus.OK);
				}
				
				StringBuffer err = new StringBuffer();
				responsevalidate.getPriceFailList().stream().forEach(el->{
					err.append(el.getErrorMsg() + "|") ;
				});
				
				throw new Exception(err.toString());
			}
			
			listKey = responsevalidate.getPriceOkList().stream().map(el->{
				return "SETUP_PRICE_SAVE_MATCODE=" + el.getCompany() + "_"+ el.getMatCode()+ "_"+ el.getPriceGroup()+ "_" + el.getPaymentMethod();
			}).distinct().collect(Collectors.toList());
			
			iTDSPR002LoadPriceExcelService.insertCheckDupTrans(listKey);
			insertkeyFlg = true ;
			
//			 responsevalidate.getPriceOkList().stream().forEach(el->{
//				 el.setFromUpdatePrice("Y");
//			 }) ;
			
			response = iTDSPR002LoadPriceExcelService.saveUpdate(responsevalidate.getPriceOkList(),request.getUserId());
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			//log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}finally {
			if(insertkeyFlg) {
				try {
					iTDSPR002LoadPriceExcelService.deleteCheckDupTrans(listKey);
				}catch (Exception e) {
					log.error(e.getMessage());
				}
				
			}
		}
		
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

	@RequestMapping(
			  value = prefixPath +"/delete-price",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> deletePrice(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		// delete price effective_dt > current date only
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			response = iTDSPR002LoadPriceExcelService.deletePrice(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

	@RequestMapping(
			  value = prefixPath +"/get-priceId",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR001SetupPriceBean.class)
	public ResponseEntity<String> getPriceId(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
		TDSPR001SetupPriceBean response = new TDSPR001SetupPriceBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR001SetupPriceBean.class);
			response = iTDSPR002LoadPriceExcelService.getPriceId(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

}
