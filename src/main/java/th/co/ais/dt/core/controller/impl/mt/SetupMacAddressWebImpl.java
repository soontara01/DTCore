//package th.co.ais.dt.core.controller.impl.mt;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.gson.Gson;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import th.co.ais.dt.entity.cm.LocationMst;
//import th.co.ais.dt.entity.so.MaxAddrMst;
//import th.co.ais.dt.exception.ForceTerminateException;
//import th.co.ais.dt.service.core.dto.ProcessResult;
//import th.co.ais.dt.service.core.interfaces.cm.LocationMstService;
//import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
//import th.co.ais.dt.service.core.interfaces.mt.ISetupMacAddressService;
//import th.co.ais.dt.util.BeanUtil;
////import th.co.ais.dt.util.JBossBackEndProperties;
//import th.co.ais.dt.controller.dto.SetupMacAddressBean;
//
//@RestController
//@Slf4j
//@AllArgsConstructor
//public class SetupMacAddressWebImpl {
//	
//   final String prefixPath = "api/setup-mac-address/v1";
//	
//	private static final String RESULT_CODE_SUCCESS = "20000";
//	private static final String RESULT_CODE_ERROR = "50000";
//	private static final String RESULT_CODE_ERROR_EMPTY = "50005";
//	
//	private final ISetupMacAddressService setupMacAddressService ;
//	
//	private final LocationMstService locationMstService ;
//	
//	@RequestMapping(value = prefixPath +"/query-mac-address",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(SetupMacAddressBean.class)
//	public ResponseEntity<String> queryMacAddress(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//
//		SetupMacAddressBean req = new SetupMacAddressBean();
//		SetupMacAddressBean res = new SetupMacAddressBean();
//		Gson gson = new Gson();
//		try {
//			req = gson.fromJson(jsonRequest, SetupMacAddressBean.class);
//
//		} catch (Exception e) {
//			res.setResultCode(RESULT_CODE_SUCCESS);
//			res.setResultDescription("JSON incorrect format");
//			res.setDeveloperMessage("JSON incorrect format");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		}
//		try {
//			if (BeanUtil.isNotEmpty(req)) {
//				boolean validInput = false;				
//				validInput = validateQueryMacAddress(req);
//				if(!validInput) {
//					res.setResultCode(RESULT_CODE_SUCCESS);
//					res.setResultDescription("invalid input");
//					res.setDeveloperMessage("invalid input");
//					return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//				}
//				List<MaxAddrMst> result = new ArrayList<MaxAddrMst>();
//				result = setupMacAddressService.queryMacAddress(req);
//				if (BeanUtil.isNotEmpty(result)) {
//					res.setListResult(result);
//					res.setDeveloperMessage("");
//					res.setResultCode(RESULT_CODE_SUCCESS);
//					res.setResultDescription("");
//				} else {
//					res.setDeveloperMessage("Data not found.");
//					res.setResultCode(RESULT_CODE_ERROR_EMPTY);
//					res.setResultDescription("Data not found.");
//				}
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch (ForceTerminateException e) {
//			log.info(e.getMessage());
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription(e.getMessage());
//			res.setDeveloperMessage(e.getMessage());
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch (Exception e) {
//			log.info(e.getMessage());
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("webImpl fail");
//			res.setDeveloperMessage("webImpl fail");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}
//		
//	}
//	
//	private boolean validateQueryMacAddress(SetupMacAddressBean req){
//		if(BeanUtil.isEmpty(req.getCompany())
//				&& BeanUtil.isEmpty(req.getLocation())
//				&& BeanUtil.isEmpty(req.getMacAddress())
//				&& BeanUtil.isEmpty(req.getPosNo())
//				&& BeanUtil.isEmpty(req.getActiveStatus())
//				) {
//			return false;
//		}
//		return true;			
//	}
//	
//	@RequestMapping(value = prefixPath +"/insert-mac-address",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(SetupMacAddressBean.class)
//	public ResponseEntity<String> insertMacAddress(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//
//		SetupMacAddressBean req = new SetupMacAddressBean();
//		SetupMacAddressBean res = new SetupMacAddressBean();
//		ProcessResult result = new ProcessResult(false,null,null);
//		Gson gson = new Gson();
//		try {
//			req = gson.fromJson(jsonRequest, SetupMacAddressBean.class);						
//		} catch (Exception e) {
//			res.setResultCode(RESULT_CODE_SUCCESS);
//			res.setResultDescription("JSON incorrect format");
//			res.setDeveloperMessage("JSON incorrect format");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		}
//		try {
//			boolean validInsert = false;
//			validInsert = validateInsertMacAddress(req, res);
//			log.info("validInsert MacAddress:" + validInsert);
//			if(validInsert) {
//				setupMacAddressService.insertMacAddress(req, result);
//			} else {
//				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//			}
//			if(result.isSuccess()) {
//				log.info("save success");
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setDeveloperMessage(result.getMessage());
//				res.setResultDescription(result.getMessage());
//			} else {
//				log.info("save fail");
//				res.setResultCode(RESULT_CODE_ERROR);
//				res.setDeveloperMessage(result.getMessage());
//				res.setResultDescription(result.getMessage());
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(ForceTerminateException e) {			
//			log.info("saveMacAddress ForceTerminateException");
//			res.setResultCode(RESULT_CODE_ERROR);
//			if(BeanUtil.isEmpty(result.getMessage())) {
//				res.setResultDescription(e.getMessage());				
//				res.setDeveloperMessage(e.getMessage());
//			} else {
//				res.setResultDescription(result.getMessage());				
//				res.setDeveloperMessage(result.getMessage());
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			log.info("saveMacAddress Exception");
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("webImpl fail");
//			res.setDeveloperMessage("webImpl fail");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}
//	}
//	
//   private boolean validateInsertMacAddress(SetupMacAddressBean req,SetupMacAddressBean res){
//		
//		if(BeanUtil.isEmpty(req.getCompany())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("company is null");
//			res.setDeveloperMessage("company is null");
//			return false;			
//		}
//		if(BeanUtil.isEmpty(req.getLocationCode())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("locationCode is null");
//			res.setDeveloperMessage("locationCode is null");
//			return false;
//		}
//		if(BeanUtil.isEmpty(req.getPosNo())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("posNo is null");
//			res.setDeveloperMessage("posNo is null");
//			return false;
//		}
//		if(BeanUtil.isEmpty(req.getMacAddress())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("macAddress is null");
//			res.setDeveloperMessage("macAddress is null");
//			return false;
//		}
//		return true;
//	}
//   
//	@RequestMapping(value = prefixPath +"/query-location-mac-address",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(SetupMacAddressBean.class)
//	public ResponseEntity<String> queryLocationLike(@RequestBody String jsonRequest) {
//   	final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//		
//		SetupMacAddressBean req = new SetupMacAddressBean();
//		SetupMacAddressBean res = new SetupMacAddressBean();		
//		Gson gson = new Gson();
//		try {
//			req = gson.fromJson(jsonRequest, SetupMacAddressBean.class);						
//		} catch (Exception e) {
//			res.setResultCode(RESULT_CODE_SUCCESS);
//			res.setResultDescription("JSON incorrect format");
//			res.setDeveloperMessage("JSON incorrect format");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		}
//		try {
//			boolean validInput = false;
//			validInput = validateInputQuery(req, res);
//			log.info("validInput QueryLocation:" + validInput);
//			if(!validInput) {
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setResultDescription("invalid criteria");
//				res.setDeveloperMessage("invalid criteria");
//				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//			}
//			List<LocationMst> result = new ArrayList<LocationMst>();
//			result = setupMacAddressService.queryLocationCodeLike(null, null, null, req.getLocationCode(), req.getLocationName());
//			if (BeanUtil.isNotEmpty(result)) {
//				res.setLocationList(result);
//				res.setDeveloperMessage("");
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setResultDescription("");
//			} else {
//				res.setDeveloperMessage("Data not found.");
//				res.setResultCode(RESULT_CODE_ERROR_EMPTY);
//				res.setResultDescription("Data not found.");
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			log.info("queryLocationLike Exception");
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("webImpl fail");
//			res.setDeveloperMessage("webImpl fail");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}				
//	}
//	
//	private boolean validateInputQuery(SetupMacAddressBean req,SetupMacAddressBean res) {
//		if(BeanUtil.isEmpty(req.getLocationCode()) && BeanUtil.isEmpty(req.getLocationName())) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//	
//	@RequestMapping(value = prefixPath +"/query-location-name-by-location-code",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(SetupMacAddressBean.class)
//	public ResponseEntity<String> queryLocationNameByLocationCode(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//		
//		SetupMacAddressBean req = new SetupMacAddressBean();
//		SetupMacAddressBean res = new SetupMacAddressBean();		
//		Gson gson = new Gson();
//		try {
//			req = gson.fromJson(jsonRequest, SetupMacAddressBean.class);						
//		} catch (Exception e) {
//			res.setResultCode(RESULT_CODE_SUCCESS);
//			res.setResultDescription("JSON incorrect format");
//			res.setDeveloperMessage("JSON incorrect format");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		}
//		
//		try {
//			if(BeanUtil.isEmpty(req.getLocationCode())) {
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setResultDescription("invalid criteria");
//				res.setDeveloperMessage("invalid criteria");
//				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//			}
//			String locationName = "";
//			try {
//				locationName = locationMstService.getLocatioNameByLocationCode(Long.parseLong(req.getLocationCode()));				
//			} catch(Exception e) {
//				
//			}
//			if(BeanUtil.isEmpty(locationName)) {
//				res.setDeveloperMessage("Data not found.");
//				res.setResultCode(RESULT_CODE_ERROR_EMPTY);
//				res.setResultDescription("Data not found.");
//				res.setLocationName("");
//			} else {
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setLocationName(locationName);
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			log.info("queryLocationNameByLocationCode Exception");
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("webImpl fail");
//			res.setDeveloperMessage("webImpl fail");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}				
//	}
//	
//	@RequestMapping(value = prefixPath +"/query-location-by-key",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(SetupMacAddressBean.class)
//	public ResponseEntity<String> queryLocationByKey(@RequestBody String jsonRequest) {
//		
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//		
//		SetupMacAddressBean req = new SetupMacAddressBean();
//		SetupMacAddressBean res = new SetupMacAddressBean();		
//		Gson gson = new Gson();
//		try {
//			req = gson.fromJson(jsonRequest, SetupMacAddressBean.class);						
//		} catch (Exception e) {
//			res.setResultCode(RESULT_CODE_SUCCESS);
//			res.setResultDescription("JSON incorrect format");
//			res.setDeveloperMessage("JSON incorrect format");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		}
//		
//		try {
//			if(BeanUtil.isEmpty(req.getLocationCode())) {
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setResultDescription("invalid criteria");
//				res.setDeveloperMessage("invalid criteria");
//				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//			}
//			LocationMst locationMst = null; 
//			try {
//				locationMst = locationMstService.getLocationMstByKey(Long.parseLong(req.getLocationCode()));				
//			} catch(Exception e) {
//				
//			}
//			if(BeanUtil.isNull(locationMst)) {
//				res.setDeveloperMessage("Data not found.");
//				res.setResultCode(RESULT_CODE_ERROR_EMPTY);
//				res.setResultDescription("Data not found.");
//			} else {
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setLocationResult(locationMst);
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			log.info("queryLocationNameByLocationCode Exception");
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("webImpl fail");
//			res.setDeveloperMessage("webImpl fail");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}	
//	}
//	
//	@RequestMapping(value = prefixPath +"/update-mac-address",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(SetupMacAddressBean.class)
//	public ResponseEntity<String> updateMacAddress(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//		
//		SetupMacAddressBean req = new SetupMacAddressBean();
//		SetupMacAddressBean res = new SetupMacAddressBean();
//		ProcessResult result = new ProcessResult(false,null,null);
//		Gson gson = new Gson();
//		try {
//			req = gson.fromJson(jsonRequest, SetupMacAddressBean.class);						
//		} catch (Exception e) {
//			res.setResultCode(RESULT_CODE_SUCCESS);
//			res.setResultDescription("JSON incorrect format");
//			res.setDeveloperMessage("JSON incorrect format");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		}
//		try {
//			boolean validUpdate = false;
//			validUpdate = validateUpdateMacAddress(req, res);
//			log.info("validUpdate MacAddress:" + validUpdate);
//			if(validUpdate) {
//				setupMacAddressService.updateMacAddress(req, result);
//			} else {
//				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//			}
//			if(result.isSuccess()) {
//				log.info("update success");
//				res.setResultCode(RESULT_CODE_SUCCESS);
//				res.setDeveloperMessage(result.getMessage());
//				res.setResultDescription(result.getMessage());
//			} else {
//				log.info("update fail");
//				res.setResultCode(RESULT_CODE_ERROR);
//				res.setDeveloperMessage(result.getMessage());
//				res.setResultDescription(result.getMessage());
//			}
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(ForceTerminateException e) {			
//			log.info("updateMacAddress ForceTerminateException");
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription(e.getMessage());
//			res.setDeveloperMessage(e.getMessage());
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
//
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			log.info("updateMacAddress Exception");
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("webImpl fail");
//			res.setDeveloperMessage("webImpl fail");
//			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}
//	}
//	
//   private boolean validateUpdateMacAddress(SetupMacAddressBean req,SetupMacAddressBean res){
//		
//		if(BeanUtil.isEmpty(req.getCompany())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("company is null");
//			res.setDeveloperMessage("company is null");
//			return false;			
//		}
//		if(BeanUtil.isEmpty(req.getLocationCode())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("locationCode is null");
//			res.setDeveloperMessage("locationCode is null");
//			return false;
//		}
//		if(BeanUtil.isEmpty(req.getPosNo())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("posNo is null");
//			res.setDeveloperMessage("posNo is null");
//			return false;
//		}
//		if(BeanUtil.isEmpty(req.getMacAddress())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("macAddress is null");
//			res.setDeveloperMessage("macAddress is null");
//			return false;
//		}
//		if(BeanUtil.isEmpty(req.getActiveStatus())) {
//			res.setResultCode(RESULT_CODE_ERROR);
//			res.setResultDescription("activeStatus is null");
//			res.setDeveloperMessage("activeStatus is null");
//			return false;
//		}
//		return true;
//	}
//
//}
