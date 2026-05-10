package th.co.ais.dt.core.controller.impl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.AppLogBean;
import th.co.ais.dt.controller.dto.AscMstWebBean;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.ConfigForStockLocationBean;
import th.co.ais.dt.controller.dto.ConfigGoodsReplaceReq;
import th.co.ais.dt.controller.dto.DealerMstWebBean;
import th.co.ais.dt.controller.dto.FindAscCodeBeanAndRes;
import th.co.ais.dt.controller.dto.LocMapPlantLocationBean;
import th.co.ais.dt.controller.dto.LocMapPlantLocationReq;
import th.co.ais.dt.controller.dto.LocMapPlantLocationRes;
import th.co.ais.dt.controller.dto.LovMasterInput;
import th.co.ais.dt.controller.dto.LovMasterRequest;
import th.co.ais.dt.controller.dto.LovMasterResponse;
import th.co.ais.dt.controller.dto.MessageMstBean;
import th.co.ais.dt.controller.dto.ProductBean;
import th.co.ais.dt.controller.dto.ProductMasterServiceBean;
import th.co.ais.dt.controller.dto.ProductMstByUniqueBean;
import th.co.ais.dt.controller.dto.QueryLovTypeWithInBean;
import th.co.ais.dt.controller.dto.SsoInfoForMCBean;
import th.co.ais.dt.controller.dto.TmpTableRequestBean;
import th.co.ais.dt.controller.dto.UserProfileBean;
import th.co.ais.dt.core.controller.impl.util.dto.QueryPrebookingRes;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.entity.util.AscMst;
import th.co.ais.dt.entity.util.DealerMst;
import th.co.ais.dt.entity.util.DtAppToken;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MessageMaster;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.dto.SsoInfoForMCDataBean;
import th.co.ais.dt.service.core.dto.User;
import th.co.ais.dt.service.core.interfaces.cm.LocationMstService;
import th.co.ais.dt.service.core.interfaces.gm.IApplogService;
import th.co.ais.dt.service.core.interfaces.util.AscMstService;
import th.co.ais.dt.service.core.interfaces.util.DealerMstService;
import th.co.ais.dt.service.core.interfaces.util.DtAppTokenService;
import th.co.ais.dt.service.core.interfaces.util.ISsoInfoForMCService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.service.core.interfaces.util.MessageMasterService;
import th.co.ais.dt.service.core.interfaces.util.ProductMstService;
import th.co.ais.dt.service.core.interfaces.util.TmpTableService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.controller.dto.AppLogBean;
import th.co.ais.dt.controller.dto.AscMstWebBean;
import th.co.ais.dt.controller.dto.ConfigForStockLocationBean;
import th.co.ais.dt.controller.dto.ConfigGoodsReplaceReq;
import th.co.ais.dt.controller.dto.DealerMstWebBean;
import th.co.ais.dt.controller.dto.DtAppTokenBean;
import th.co.ais.dt.controller.dto.FindAscCodeBeanAndRes;
import th.co.ais.dt.controller.dto.LocMapPlantLocationBean;
import th.co.ais.dt.controller.dto.LocMapPlantLocationReq;
import th.co.ais.dt.controller.dto.LocMapPlantLocationRes;
import th.co.ais.dt.controller.dto.LovMasterInput;
import th.co.ais.dt.controller.dto.LovMasterRequest;
import th.co.ais.dt.controller.dto.LovMasterResponse;
import th.co.ais.dt.controller.dto.MessageMstBean;
import th.co.ais.dt.controller.dto.ProductBean;
import th.co.ais.dt.controller.dto.ProductMstByUniqueBean;
import th.co.ais.dt.controller.dto.QueryLovTypeWithInBean;
import th.co.ais.dt.controller.dto.SsoInfoForMCBean;
import th.co.ais.dt.controller.dto.UserProfileBean;

@RestController
@Slf4j
@AllArgsConstructor
public class UtilImpl  {
	final String prefixPath = "api/util/v1";

	private final ISsoInfoForMCService ssoService ;
	
	private final LovMasterService lovMasterService ;
	
	private final ProductMstService productMstService ;
	
	private final AscMstService ascMstService ;
	
	private final MessageMasterService messageMasterService ;
	
	private final LocationMstService locationMstService ;
	
	private final DealerMstService dealerMstService ;
	
	private final IApplogService iAppLogService;
	
	private final TmpTableService tmpTableService;
//	private final ILocationMstService mapLocationMstService;
	
	private final DtAppTokenService dtAppTokenService;

	@RequestMapping(
			  value = prefixPath +"/validateLogin",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	public ResponseEntity<String> validateLogin(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
	    //httpHeaders.setContentType(MediaType.APPLICATION_JSON );

	    
		Gson gson = new Gson();
		Map mapReq = new HashMap<>();
		Map mapRes = new HashMap<>();
		try {
			mapReq = gson.fromJson(jsonRequest, Map.class);	
		} catch(Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			
			// Validate User login 2 step 
			String username = (BeanUtil.isNotEmpty(mapReq.get("username")) ? mapReq.get("username").toString() : ""  );
			String password = (BeanUtil.isNotEmpty(mapReq.get("password")) ? mapReq.get("password").toString() : ""  );
			String locationCode = (BeanUtil.isNotEmpty(mapReq.get("locationCode")) ? mapReq.get("locationCode").toString() : ""  );
			String idsFlg = (BeanUtil.isNotEmpty(mapReq.get("idsFlg")) ? mapReq.get("idsFlg").toString() : ""  );
			// insert log
			
			
			try {
				if(idsFlg.equals("Y")) {
					
					try {
						Integer.parseInt(locationCode);
					}catch (Exception e) {
						locationCode = "" ;
					}
					
					ssoService.insertDtLogLogin(username, locationCode);
					
					if(BeanUtil.isEmpty(locationCode)) {
						locationCode = "1004" ;
					}
				}
			}catch (Exception e) {
				log.info(e.getMessage());
			}
			
			ProcessResult result = ssoService.validateLoginAndGetMenuBypassSSO(username,password,locationCode,idsFlg);
			if(result.isSuccess()) {
				mapRes.put("resultCode", "20000");
				mapRes.put("data", result.getResultObject());
				mapRes.put("resultDescription", "Success");
				mapRes.put("developerMessage", "Success");
				
			} else {
				mapRes.put("resultCode", "50000");
				mapRes.put("resultDescription", result.getMessage());
				mapRes.put("developerMessage", result.getMessage());
			}
			
		}  catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return new ResponseEntity<String>(gson.toJson(mapRes), httpHeaders, HttpStatus.OK);

	}

	@RequestMapping(
			  value = prefixPath +"/ssoinfoDT",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SsoInfoForMCBean.class)
	public ResponseEntity<String> ssoinfoDT(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {

			Gson gson = new Gson();
			SsoInfoForMCBean ssoInfobeanReq = new SsoInfoForMCBean();
			SsoInfoForMCBean ssoInfobeanRes = new SsoInfoForMCBean();

			try {
				ssoInfobeanReq = gson.fromJson(jsonRequest, SsoInfoForMCBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
			}
			
			// Query Check Flg from DT-WEB is BYPass? at Config lovMst ; if size > 0 is
			// byPass
			List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria("MAPPING_CONFIG_BYPASS_SSO_MC",ssoInfobeanReq.getFlg(), null,null, null, "Y",null,null,null,null,null,null);

			try {

				if (BeanUtil.isEmpty(ssoInfobeanReq.getUserId())) {
					ssoInfobeanRes.setDeveloperMessage("userId is required");
					ssoInfobeanRes.setResultCode("50000");
					ssoInfobeanRes.setResultDescription("userId is required");
					return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
				}
				if (BeanUtil.isEmpty(ssoInfobeanReq.getLc())) {
					ssoInfobeanRes.setDeveloperMessage("LocationCode is required");
					ssoInfobeanRes.setResultCode("50000");
					ssoInfobeanRes.setResultDescription("LocationCode is required");
					return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
				}
				if (BeanUtil.isEmpty(ssoInfobeanReq.getUserType())) {
					ssoInfobeanRes.setDeveloperMessage("userType is required");
					ssoInfobeanRes.setResultCode("50000");
					ssoInfobeanRes.setResultDescription("userType is required");
					return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
				}
				if (!"PARTNER".equals(ssoInfobeanReq.getUserType())
						&& !"EMPLOYEE".equals(ssoInfobeanReq.getUserType())) {
					ssoInfobeanRes.setDeveloperMessage("userType is not Correct");
					ssoInfobeanRes.setResultCode("50000");
					ssoInfobeanRes.setResultDescription("userType is not Correct");
					return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
				}
				if (BeanUtil.isEmpty(ssoInfobeanReq.getFlg())) {
					ssoInfobeanRes.setDeveloperMessage("flg is required");
					ssoInfobeanRes.setResultCode("50000");
					ssoInfobeanRes.setResultDescription("flg is required");
					return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
				}
				
				if(BeanUtil.isEmpty(ssoInfobeanReq.getProgramCode())) {
					ssoInfobeanRes.setDeveloperMessage("programCode is required");
					ssoInfobeanRes.setResultCode("50000");
					ssoInfobeanRes.setResultDescription("programCode is required");
					return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
				}


				if (!"Y".equals(ssoInfobeanReq.getFlg()) && listLovMaster.size() == 1) { // is byPass set Flg N
					ssoInfobeanReq.setFlg("N");
				}

				ProcessResult result = ssoService.ssoinfoDT(ssoInfobeanReq.getUserId(), ssoInfobeanReq.getLc(),
						ssoInfobeanReq.getUserType(), ssoInfobeanReq.getProgramCode(), ssoInfobeanReq.getFlg(),
						ssoInfobeanReq.getRefererURL(), ssoInfobeanReq.getFn(), ssoInfobeanReq.getLn(),
						ssoInfobeanReq.getToken() , ssoInfobeanReq.getIdsId());

				ssoInfobeanRes.setResultCode(result.isSuccess() ? "20000" : "50000");
				ssoInfobeanRes.setDeveloperMessage(result.getMessage());
				ssoInfobeanRes.setResultDescription(result.getMessage());
				ssoInfobeanRes.setDataList( (result.getResultObject() != null ? (SsoInfoForMCDataBean)result.getResultObject() : null )  );

			} catch (Exception e) {
				ssoInfobeanRes.setDeveloperMessage("Cannot Execute Sql");
				ssoInfobeanRes.setResultCode("50000");
				ssoInfobeanRes.setResultDescription("Cannot Execute Sql");
			}
			return new ResponseEntity<String>(gson.toJson(ssoInfobeanRes), httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = prefixPath +"/query-product-mst-by-unique",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({ProductBean.class,ProductMstByUniqueBean.class})
	public ResponseEntity<String> queryProductMstByUnique(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			Gson gson = new Gson();
			ProductBean proReq = new ProductBean();
			ProductMstByUniqueBean proRes = new ProductMstByUniqueBean();
			proReq = gson.fromJson(jsonRequest, ProductBean.class);
			
			ProductMst productMst = null ;
			if(BeanUtil.isNotEmpty( proReq.getMatcode()) && BeanUtil.isNotEmpty(proReq.getCompany())) {
				 productMst = productMstService.getProductMstByUnique(proReq.getCompany(), proReq.getMatcode());
			}
			
			
			//List<ProductMst> productMstList = new ArrayList<ProductMst>();
			//productMstList.add(productMst);
			
			if(productMst != null) {
				proRes.setMatType(productMst.getMatType());
				proRes.setProductMstBean(productMst);
			}else {
				proRes.setMatType("-");
			}
			proRes.setDeveloperMessage("Success");
			proRes.setResultCode("20000");
			proRes.setResultDescription("Success");
			return new ResponseEntity<String>(gson.toJson(proRes), httpHeaders, HttpStatus.OK);

		} catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/list-lov-by-criteria",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LovMasterResponse.class,LovMasterInput.class})
	public ResponseEntity<String> listLovByCriteria(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		LovMasterResponse res = new LovMasterResponse();
		LovMasterInput req;
		//lovMasterService = (LovMasterService) SpringApplicationContext.getBean("lovMasterService");

		try {
			req = gson.fromJson(jsonRequest, LovMasterInput.class);
			String lovType = req.getLovType();
			Object lovSubType = req.getLovSubType();
			Object lovCode = req.getLovCode();
			String lovVal = req.getLovVal();
			String activeFlag = req.getLovActive();
			String systemCode = req.getSystemCode();
			String systemSubCode = req.getSystemSubCode();

			if (lovType != null && lovType.length() > 0) {
				List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria(lovType, lovSubType, lovCode, lovVal,null , activeFlag, systemCode, systemSubCode, null, null, null,null);
				res.setListLovMaster(listLovMaster);
				res.setResultCount(listLovMaster.size());
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			} else {
				// List<LovMaster> listLovMaster = new ArrayList<>();
				// res.setListLovMaster(listLovMaster);
				// res.setResultCount(0);
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

			}

		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/list-lov-mst-by-criteria",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LovMasterResponse.class,LovMasterInput.class})
	public ResponseEntity<String> listLovMstByCriteria(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		LovMasterResponse res = new LovMasterResponse();
		LovMasterInput req;

		try {
			req = gson.fromJson(jsonRequest, LovMasterInput.class);
			String lovType = req.getLovType();
			Object lovSubType = req.getLovSubType();
			String activeFlag = req.getLovActive();
			String systemSubCode = req.getSystemSubCode();

			if (lovType != null && lovType.length() > 0) {
				/*String lovType, Object lovSubType, Object lovCode, String lovVal, String activeFlag, String systemCode, String systemSubCode, Long orderBy, Long prSeqNo, String attribute01*/
				List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria(lovType, lovSubType, null, null, null , activeFlag, null, systemSubCode, null, null, null,null);
				res.setListLovMaster(listLovMaster);
				res.setResultCount(listLovMaster.size());
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			} else {

				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

			}

		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/config/{action}",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LovMasterRequest.class,LovMasterResponse.class})
	public ResponseEntity<String> listConfigurationPost(@PathVariable("action") String action,@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		final String event = action;
		Gson gson = new Gson();
		LovMasterRequest lovRequest = null;
		switch (event) {
		case "lovMasterActive":
			lovRequest = gson.fromJson(jsonRequest, LovMasterRequest.class);
			return new ResponseEntity<String>(gson.toJson(lovMasterActive(lovRequest)), httpHeaders, HttpStatus.OK);
		case "":
			return new ResponseEntity<String>(gson.toJson(""), httpHeaders, HttpStatus.OK);
		default:
			break;
		}
		return null;
	}
	
	public LovMasterResponse lovMasterActive(LovMasterRequest lovRequest) {
		LovMasterResponse lovReq = null;
		if (BeanUtil.isEmpty(lovRequest.getLovType())) {
			lovReq = new LovMasterResponse("50000", "Empty LovType", "Empty LovType");
		} else {
			List<LovMaster> lov = lovMasterService.listLovMasterByCriteria(lovRequest.getLovType(), lovRequest.getLovSubType(), lovRequest.getLovCode(), lovRequest.getLovVal(), lovRequest.getDescription(), "Y",null,null,null,null,null,null) ;
			if (BeanUtil.isNotEmpty(lov)) {
				lovReq = new LovMasterResponse("20000", "Success", "Success", lov);
			} else {
				lovReq = new LovMasterResponse("50000", "Data not found.", "Data not found.");
			}

		}
		return lovReq;
	}
	
	@RequestMapping(value = prefixPath +"/config/setupLovMaster",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({ConfigGoodsReplaceReq.class,LovMasterResponse.class})
	public ResponseEntity<String> setupLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		// new Lov_mst
		//lovMasterService = (LovMasterService) SpringApplicationContext.getBean("lovMasterService");
		//ConfigGoodsReplaceService configGoodsReplaceService = (ConfigGoodsReplaceService) SpringApplicationContext.getBean("configGoodsReplaceService");
		Gson gson = new Gson();
		LovMasterResponse res = new LovMasterResponse();
		ConfigGoodsReplaceReq request = new ConfigGoodsReplaceReq();
		ProcessResult result = new ProcessResult(false, null, "");
		try {
			request = gson.fromJson(jsonRequest, ConfigGoodsReplaceReq.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {	
			List<ConfigGoodsReplaceReq> dataList = request.getDataList();
			List<LovMaster> inputConfigList = new ArrayList<>();
			
			if (BeanUtil.isNotEmpty(request)) {
				User user = new User();
				user.setId(request.getUserId());
				int runningNumber =  lovMasterService.getMaxSequenceGoodsReplaceBrand().intValueExact();
				for(ConfigGoodsReplaceReq itemConfig : dataList) {
					runningNumber++;
					LovMaster itemLov = new LovMaster();
					itemLov.setLovType(itemConfig.getLovType());
					itemLov.setLovSubType(Integer.toString(runningNumber));
					itemLov.setLovCode(itemConfig.getLovCode());
					itemLov.setLovVal(itemConfig.getLovVal());
					itemLov.setActiveFlag(itemConfig.getActiveFlag());
					itemLov.setHigh(itemConfig.getHigh());
					itemLov.setDescription(itemConfig.getDescription());
					itemLov.setSystemSource(itemConfig.getSystemSource());
					inputConfigList.add(itemLov);
				}
//				req.setLovSubType(configGoodsReplaceService.getRunning().toString());
				result =  lovMasterService.saveLovMasterList(inputConfigList, user);
				List<LovMaster> listLovMaster = new ArrayList();
//				if (result.isSuccess()) {
//					listLovMaster.add(objLovMaster);
//				}
				res.setListLovMaster(listLovMaster);
				res.setResultCount(listLovMaster.size());
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

			} else {
				// List<LovMaster> listLovMaster = new ArrayList<>();
				// res.setListLovMaster(listLovMaster);
				// res.setResultCount(0);
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

			}
		
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/list-location-code",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({ConfigGoodsReplaceReq.class,LovMasterResponse.class})
	public ResponseEntity<String> listLocationCode(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {

			Gson gson = new Gson();
			Map mapReq = new HashMap<>();
			Map mapRes = new HashMap<>();

			try {
				mapReq = gson.fromJson(jsonRequest, Map.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);
			}

			String userId = (String) mapReq.get("userId");
			
			if(BeanUtil.isEmpty(userId)) {
				mapRes.put("resultCode", "50000");
				mapRes.put("resultDescription", "userId is required");
				mapRes.put("developerMessage", "userId is required");
				return new ResponseEntity<String>(gson.toJson(mapRes), httpHeaders, HttpStatus.OK);
		
			}

			try {

				ProcessResult result = ssoService.getListLocationCode(userId);

				if (result.isSuccess()) {
					mapRes.put("resultCode", "20000");
					mapRes.put("data", result.getResultObject());
					mapRes.put("resultDescription", "Success");
					mapRes.put("developerMessage", "Success");

				} else {
					mapRes.put("resultCode", "50000");
					mapRes.put("resultDescription", result.getMessage());
					mapRes.put("developerMessage", result.getMessage());
				}

			} catch (Exception e) {
				mapRes.put("developerMessage", "Cannot Execute Sql");
				mapRes.put("resultCode", "50000");
				mapRes.put("resultDescription", "Cannot Execute Sql");
			}
			return new ResponseEntity<String>(gson.toJson(mapRes), httpHeaders, HttpStatus.OK);
		
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@RequestMapping(value = prefixPath +"/query-lov-type-with-in",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({QueryLovTypeWithInBean.class,LovMasterResponse.class})
	public ResponseEntity<String> queryLovTypeWithIn(@RequestBody String input) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		LovMasterResponse res = new LovMasterResponse();
		QueryLovTypeWithInBean req;

		try {
			req = gson.fromJson(input, QueryLovTypeWithInBean.class);
			Object lovType = req.getLovType();
			Object lovSubType = req.getLovSubType();
			String activeFlag = req.getLovActive();
			
			if (lovType != null) {
				List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria(lovType, lovSubType, null, null, null, activeFlag, null, null, null, null, null,null) ;
				res.setListLovMaster(listLovMaster);
				res.setResultCount(listLovMaster.size());
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			
			} else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

			}

		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/query-asccode-by-code",method = RequestMethod.GET, produces = { "application/json" })
	@RegisterReflectionForBinding(AscMstWebBean.class)
	public ResponseEntity<String> getAscMstByCode(@QueryParam("asccode") String asccode) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			AscMstWebBean res = new AscMstWebBean();

			Gson gson = new Gson();
			
			if(BeanUtil.isEmpty(asccode)) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}
			
							
			AscMst result = null ;
			List<AscMst> li = new ArrayList<AscMst>();
			try {
				result = ascMstService.getAscMstByCode(asccode);
				if(result != null) {
					li.add(result);
				}
				
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				res.setListAscMst(li);
			}catch (Exception e) {
				res.setDeveloperMessage(e.getMessage());
				res.setResultCode("50000");
				res.setResultDescription(e.getMessage());
			}
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	@RequestMapping(value = prefixPath +"/list-asccode-by-criteria",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({AscMstWebBean.class,FindAscCodeBeanAndRes.class})
	public ResponseEntity<String> listAscCodeByCriteria(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			
			AscMstWebBean res = new AscMstWebBean();
			FindAscCodeBeanAndRes input = new FindAscCodeBeanAndRes();
			Gson gson = new Gson();
			input = gson.fromJson(in, FindAscCodeBeanAndRes.class);
			
	
			try {
				
				List<AscMst> li  = ascMstService.listAscMstrByCriteria(input.getAscCode(), input.getAscName(), input.getPartnerType(), input.getPartnerSubType(), 
																	  input.getProvince(), input.getRegion(), input.getRole(), input.getAscClass(), 
																	  input.getAscType(),  input.getAscStatus(), input.getLocationCode() );
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				res.setListAscMst(li);
			}catch (Exception e) {
				res.setDeveloperMessage(e.getMessage());
				res.setResultCode("50000");
				res.setResultDescription(e.getMessage());
			}
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

				
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	@RequestMapping(value = prefixPath +"/MessageMst",method = RequestMethod.GET, produces = { "application/json" })
	@RegisterReflectionForBinding(MessageMstBean.class)
	public ResponseEntity<String> getMessageDesc0(@QueryParam("messageCode") String messageCode) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			String messageDesc = messageCode;
			MessageMstBean res = new MessageMstBean();
			Gson gson = new Gson();
					
			if(BeanUtil.isNotEmpty(messageCode)){
				
				
				try {
					MessageMaster messageMst = messageMasterService.getMessageMst(messageCode);
					if(messageMst != null){
						messageDesc = messageMst.getMessageThai();
					}
					
					log.info(messageMst.getMessage());
					
					res.setDeveloperMessage("Success");
					res.setResultCode("20000");
					res.setResultDescription("Success");
					res.setMessage(messageMst.getMessage());
					res.setMessageThai(messageMst.getMessageThai());
					log.info(gson.toJson(res));
				}catch (Exception e) {
					res.setDeveloperMessage(e.getMessage());
					res.setResultCode("50000");
					res.setResultDescription(e.getMessage());
				}
			}
//			return messageDesc;
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

			
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/queryCmLocationSubTypeForEsimReport",method = RequestMethod.GET, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigForStockLocationBean.class)
	public ResponseEntity<String> queryCmLocationSubTypeForEsimReport() {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		try {
			Gson gson = new Gson();
			ConfigForStockLocationBean res = new ConfigForStockLocationBean();
			
			ProcessResult result = locationMstService.listCmLocationSubTypeForEsimReport();
			if (result.isSuccess()) {
				List<LocationMst>  listLocationMst = (List<LocationMst> ) result.getResultObject();
				res.setResultCode("20000");
				res.setDeveloperMessage(result.getMessage());
				res.setResultDescription(result.getMessage());
				res.setListLocationMst(listLocationMst);
			} else {
				res.setResultCode("50000");
				res.setDeveloperMessage(result.getMessage());
				res.setResultDescription(result.getMessage());
			}
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		} catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
	}
	
	@RequestMapping(value = prefixPath +"/queryCmLocationBusinessUnitForEsimReport",method = RequestMethod.GET, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigForStockLocationBean.class)
	public ResponseEntity<String> queryCmLocationBusinessUnitForEsimReport() {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		try {
			Gson gson = new Gson();
			ConfigForStockLocationBean res = new ConfigForStockLocationBean();
			
			ProcessResult result = locationMstService.listCmLocationBusinessUnitForEsimReport();
			if (result.isSuccess()) {
				List<LocationMst>  listLocationMst = (List<LocationMst> ) result.getResultObject();
				res.setResultCode("20000");
				res.setDeveloperMessage(result.getMessage());
				res.setResultDescription(result.getMessage());
				res.setListLocationMst(listLocationMst);
			} else {
				res.setResultCode("50000");
				res.setDeveloperMessage(result.getMessage());
				res.setResultDescription(result.getMessage());
			}
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		} catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/list-dealercode-by-codeandname",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(DealerMstWebBean.class)
	public ResponseEntity<String> listDealerCodeByCodeAndName(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {

			DealerMstWebBean res = new DealerMstWebBean();

			Gson gson = new Gson();
			
			DealerMstWebBean input = new DealerMstWebBean();
			try {
				input = gson.fromJson(in, DealerMstWebBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}
			
			if(BeanUtil.isEmpty(input.getDealerCode()) && BeanUtil.isEmpty(input.getDealerName()) ) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}
			
							
			List<DealerMst> result = null ;
			try {
				result = dealerMstService.listDealerCodeByCodeAndName(input.getDealerCode(), input.getDealerName());
				
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				res.setListDealerMst(result);
			}catch (Exception e) {
				res.setDeveloperMessage(e.getMessage());
				res.setResultCode("50000");
				res.setResultDescription(e.getMessage());
			}
			
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

				

		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@RequestMapping(value = "api/master-config/v1" + "/query-user-profile", method = RequestMethod.POST, produces = {"application/json"})
	@RegisterReflectionForBinding({UserProfileBean.class})
	public ResponseEntity<String> queryUserProfile(@RequestBody String in) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		log.info("queryUserProfile1111111");
		UserProfileBean res = new UserProfileBean();
		UserProfileBean req = new UserProfileBean();
		Gson gson = new Gson();

		try {
			req = gson.fromJson(in, UserProfileBean.class);
			log.info("queryUserProfile2222222");
		} catch (Exception e) {
			log.info("queryUserProfile3333333");
			res.setResultCode("20000");
			res.setResultDescription("JSON incorrect format");
			res.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		}
		try {
			List<UserProfileBean> resultUserProfile = new ArrayList<UserProfileBean>();
			List<UserProfileBean> resultGroupName = new ArrayList<UserProfileBean>();
			log.info("queryUserProfile44444");
			if (BeanUtil.isNotEmpty(req.getUserId())) {
				log.info("queryUserProfile55555555");
				resultUserProfile = ssoService.queryUserProFile(req.getUserId());
				String groupName = resultUserProfile.stream()
						.map(UserProfileBean::getGroupName)
						.distinct()
						.collect(Collectors.joining(","));

				if (BeanUtil.isNotEmpty(groupName)) {
					log.info("queryUserProfile666666");
					resultGroupName = ssoService.queryGropName(groupName);
					log.info("queryUserProfile7777");
				}
			}
			if (BeanUtil.isNotEmpty(req.getGroupName())) {
				log.info("queryUserProfile88888888");
				resultGroupName = ssoService.queryGropName(req.getGroupName());
				String groupName = resultGroupName.stream()
						.map(UserProfileBean::getGroupName)
						.distinct()
						.collect(Collectors.joining(","));

				if (BeanUtil.isNotEmpty(groupName)) {
					resultUserProfile = ssoService.queryUserProFileByGroupName(groupName);
				}
			}
			if (BeanUtil.isNotEmpty(resultUserProfile) || BeanUtil.isNotEmpty(resultGroupName)) {
				log.info("queryUserProfile999999");
				res.setQueryUserProfileList(resultUserProfile);
				res.setQueryGroupNameList(resultGroupName);
				res.setDeveloperMessage("");
				res.setResultCode("20000");
				res.setResultDescription("");
			} else {
				log.info("queryUserProfileXXXXXXXXXX");
				res.setDeveloperMessage("Data not found.");
				res.setResultCode("50005");
				res.setResultDescription("Data not found.");
			}
			log.info("queryUserProfileaaaaaaaaaaaaa");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		} catch (ForceTerminateException e) {
			log.info("queryUserProfilebbbbbbbbbbbbbb");
			log.info(e.getMessage());
			res.setResultCode("50000");
			res.setResultDescription(e.getMessage());
			res.setDeveloperMessage(e.getMessage());
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		} catch (Exception e) {
			log.info("queryUserProfileccccccccccccc");
			log.info(e.getMessage());
			res.setResultCode("50000");
			res.setResultDescription("webImpl fail");
			res.setDeveloperMessage("webImpl fail");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = prefixPath +"/insert-appLog-queryMobile",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({AppLogBean.class})
	public ResponseEntity<String> insertLogQueryMobile(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		AppLogBean req;
		AppLogBean res = new AppLogBean();
		try {
			req = gson.fromJson(jsonRequest, AppLogBean.class);
//			logger.info(req);
//			IApplogService iAppLogService = (IApplogService) SpringApplicationContext.getBean("appLogService");
			iAppLogService.insertLogQueryMobile(req.getScreen(), req.getUserId(), req.getLocationCode(), req.getMobileNo(), null, req.getMacAddress());
			res.setDeveloperMessage("Success");
			res.setResultCode("20000");
			res.setResultDescription("Success");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = prefixPath +"/queryPopupLocation",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LocMapPlantLocationReq.class,LocMapPlantLocationRes.class})
	public ResponseEntity<String> queryPopupLocation(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
//			mapLocationMstService =  (ILocationMstService) SpringApplicationContext.getBean("mapLocationMstService");
			Gson gson = new Gson();
			LocMapPlantLocationReq request = new LocMapPlantLocationReq();
			LocMapPlantLocationRes response = new LocMapPlantLocationRes();
			request = gson.fromJson(jsonRequest, LocMapPlantLocationReq.class);
			
			if(BeanUtil.isNotEmpty(request)) {
//				ProcessResult result = mapLocationMstService.queryPopupLocation(request);
				ProcessResult result = locationMstService.queryPopupLocation(request);
				if(result.isSuccess()) {
					List<LocMapPlantLocationBean> listLoc = (List<LocMapPlantLocationBean>) result.getResultObject();
					response.setListLocMapPlantLocation(listLoc);
					response.setDeveloperMessage("Success");
					response.setResultCode("20000");
					response.setResultDescription("Success");
				}else {
					response.setDeveloperMessage(result.getMessage());
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
				}
			}
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

		} catch(Exception e) {
			e.printStackTrace();
//			return Response.status(500).entity(
//					"{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}")
//					.header("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8").build();
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/get-app-token",method = RequestMethod.GET, produces = { "application/json" })
	@RegisterReflectionForBinding(DtAppTokenBean.class)
	public ResponseEntity<String> getAppToken(@RequestParam("appName") String appName) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			Gson gson = new Gson();
			DtAppTokenBean res = new DtAppTokenBean();
			
			DtAppToken appTokenData = dtAppTokenService.getDtAppToken(appName);
			if (BeanUtil.isNotEmpty(appTokenData)) {
				res.setDeveloperMessage("Success");
				res.setResultCode("20000");
				res.setResultDescription("Success");
				res.setData(appTokenData);
			} else {
				res.setDeveloperMessage("System error");
				res.setResultCode("50000");
				res.setResultDescription("Not Found Token Data");
				res.setData(null);
			}
			
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
				
		} catch (Exception e) {
			e.printStackTrace();
//			return Response.status(500).entity(
//					"{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}")
//					.header("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8").build();
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	@RequestMapping(value = prefixPath +"/queryCmDealerMaster",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LocMapPlantLocationReq.class,LocMapPlantLocationRes.class})
	public ResponseEntity<String> queryCmDealerMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
//			Gson gson = new Gson();
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").create();
			DealerMstWebBean request = new DealerMstWebBean();
			DealerMstWebBean response = new DealerMstWebBean();
			request = gson.fromJson(jsonRequest, DealerMstWebBean.class);
			
			if(BeanUtil.isNotEmpty(request)) {
				
				List<DealerMst> listDealerMst = dealerMstService.listDealerCodeByCode(request.getDealerCode());
				if(!listDealerMst.isEmpty()) {
					response.setListDealerMst(listDealerMst);
					response.setDeveloperMessage("Success");
					response.setResultCode("20000");
					response.setResultDescription("Success");
				}
				else {
					response.setDeveloperMessage("Data not found");
					response.setResultCode("50000");
					response.setResultDescription("Data not found");
				}
			}
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/insert-dt-tmp-table",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> insertDtTmpTable(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		CommonResponseBean response = new CommonResponseBean();
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm").create();
		try {
//			Gson gson = new Gson();
			TmpTableRequestBean request = gson.fromJson(jsonRequest, TmpTableRequestBean.class);
			if(request == null || BeanUtil.isEmpty(request.getTmpKey()) || BeanUtil.isEmpty(request.getUserId()) || BeanUtil.isEmpty(request.getListData())){
				response.setResultStatus("S");
				response.setResultCode("50000");
				response.setResultDescription("Not valid require value");
			} else {
				tmpTableService.insertTmpTable(request.getListData(), request.getUserId(), request.getTmpKey());
				
				response.setResultStatus("S");
				response.setResultCode("20000");
				response.setResultDescription("Success");
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

		} catch(Exception e) {
			log.error("insertDtTmpTable", e);
			response.setResultCode("50000");
			response.setResultStatus("F");
			response.setResultDescription("System error");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/get-Product",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> getProductMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		

		//ProductMstService productMstService = (ProductMstService) SpringApplicationContext.getBean("productMstService");
		ProductMst request = new ProductMst();
		ProductMasterServiceBean response = new ProductMasterServiceBean();
		Gson gson = new Gson();
		
		try {
			request = gson.fromJson(jsonRequest, ProductMst.class);
			
			if(BeanUtil.isEmpty(request.getBrand())) {
				throw new ForceTerminateException(0, "brand is required");
			}
			if(BeanUtil.isEmpty(request.getCompany())) {
				throw new ForceTerminateException(0, "company is required");
			}
			if(BeanUtil.isEmpty(request.getModel())) {
				throw new ForceTerminateException(0, "model is required");
			}
			
			List<ProductMst> list = productMstService.listProductMstsByCriteria(request);
			if (BeanUtil.isEmpty(list)) {
				throw new ForceTerminateException(0, "data not found");
			}
			response.setResultCode("20000");
			response.setDeveloperMessage("Query Success");
			response.setResultDescription("Query Success");
			response.setListProductMsts(list);
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		} catch (ForceTerminateException e) {
			return new ResponseEntity<String>(gson.toJson(new QueryPrebookingRes("50000", e.getMessage(), e.getMessage())), httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(gson.toJson(new QueryPrebookingRes("50000", "Error queryProductMst", "Error queryProductMst")), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
}
