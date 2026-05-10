package th.co.ais.dt.core.controller.impl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.util.AcimService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.AcimBean;
import th.co.ais.dt.controller.dto.AcimUserBean;

@RestController
@Slf4j
@AllArgsConstructor
public class AcimWebImpl {
	final String prefixPath = "api/acim";
	
	private final LovMasterService lovMasterService ;
	
	private final AcimService acimService ;
	
	@RequestMapping(value = prefixPath +"/acimuser",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding({AcimBean.class,AcimUserBean.class})
	public ResponseEntity<String> addUserAcim(@RequestHeader("x-acim-ClientId") String xAcimClientId,@RequestHeader("x-acim-req-id") String xAcimReqId ,@RequestHeader("x-acim-sp") String xAcimSp , @RequestBody String jsonReq) {
		
		//String xAcimClientId = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-ClientId")) ? headers.getRequestHeader("x-acim-ClientId").get(0) : "" ) ;
		//String xAcimReqId = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-req-id")) ? headers.getRequestHeader("x-acim-req-id").get(0) : "" ) ;
		//String xAcimSp = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-sp")) ? headers.getRequestHeader("x-acim-sp").get(0) : "" ) ;
		
		final org.springframework.http.HttpHeaders httpHeadersRes= new org.springframework.http.HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		properties.put("x-acim-version", "1.0");
		properties.put("x-acim-req-id", xAcimReqId);
		properties.put("x-acim-sp", xAcimSp);
		httpHeadersRes.setAll(properties);
		
		AcimBean res = new AcimBean();
		List<AcimUserBean> listErr = new ArrayList<>(); 
		Gson gson = new Gson();
		try {
//			AcimService acimService = (AcimService) SpringApplicationContext.getBean("acimService");
			List<AcimUserBean> request = Arrays.asList(gson.fromJson(jsonReq, AcimUserBean[].class));
			log.info("xAcimReqId == " + xAcimReqId);
			log.info("xAcimSp == " + xAcimSp);
			
			if(BeanUtil.isNotEmpty(xAcimClientId)) {
				List<LovMaster> listLovxAcimClientId = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "CLIENT_ID", null, xAcimClientId, null, "Y", null, null, null, null, null,null);
				if(BeanUtil.isEmpty(listLovxAcimClientId)) {
					res.setDesc("x-acim-ClientId parameter, is invalid");
					res.setCode("40001");
				}
			}else {
				res.setDesc("Missing parameter, x-acim-ClientId");
				res.setCode("40001");
			}
			
			if(BeanUtil.isNotEmpty(res.getCode()) && res.getCode().equals("40001")) {
				return new ResponseEntity<String>(gson.toJson(res), httpHeadersRes,400);		
			}
			
			
			if(BeanUtil.isNotEmpty(request)) {
				for(AcimUserBean el : request ) {
					if(BeanUtil.isNotEmpty(el.getUsername()) && BeanUtil.isNotEmpty(el.getRole())) {
						try {
							List<Object[]> listUser = acimService.listUserIdAndGroupName(el.getUsername(),el.getRole());
							if(BeanUtil.isNotEmpty(listUser)) {
//								AcimUserBean userErr = new AcimUserBean();
//								userErr.setUsername(el.getUsername());
//								listErr.add(userErr);
							}else {
								List<LovMaster> listLovRole = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "ACIM_ROLE", null, el.getRole(), null, "Y", null, null, null, null, null,null);
								if(BeanUtil.isNotEmpty(listLovRole)) {
									acimService.insertTdUserLogin(el.getUsername(), listLovRole.get(0).getLovCode());
								}else {
									AcimUserBean userErr = new AcimUserBean();
									userErr.setUsername(el.getUsername());
									userErr.setDesc("Role "+ el.getRole() +" not found in DT");
									listErr.add(userErr);
								}
							}
						}catch (Exception e) {
							log.info(e.getMessage());
							AcimUserBean userErr = new AcimUserBean();
							userErr.setUsername(el.getUsername());
							userErr.setDesc("invalid request");
							listErr.add(userErr);
						}
					}
				}	
			}

		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
		}
		
		if(BeanUtil.isNotEmpty(listErr)) {
			res.setDesc("Some Process Fail");
			res.setCode("20001");
			res.setProcessFail(listErr);
		}else {
			res.setDesc("Success");
			res.setCode("20000");
		}
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeadersRes, HttpStatus.OK);		
	}
	
	@RequestMapping(value = prefixPath +"/acimuser",method = RequestMethod.PUT, produces = { "application/json" } )
	@RegisterReflectionForBinding({AcimBean.class,AcimUserBean.class})
	public ResponseEntity<String> editUserAcim(@RequestHeader("x-acim-ClientId") String xAcimClientId,@RequestHeader("x-acim-req-id") String xAcimReqId ,@RequestHeader("x-acim-sp") String xAcimSp ,@RequestBody String jsonReq) {
		
		//String xAcimClientId = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-ClientId")) ? headers.getRequestHeader("x-acim-ClientId").get(0) : "" ) ;
		//String xAcimReqId = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-req-id")) ? headers.getRequestHeader("x-acim-req-id").get(0) : "" ) ;
		//String xAcimSp = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-sp")) ? headers.getRequestHeader("x-acim-sp").get(0) : "" ) ;
		
		final org.springframework.http.HttpHeaders httpHeadersRes= new org.springframework.http.HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		properties.put("x-acim-version", "1.0");
		properties.put("x-acim-req-id", xAcimReqId);
		properties.put("x-acim-sp", xAcimSp);
		httpHeadersRes.setAll(properties);
		
		AcimBean res = new AcimBean();
		List<AcimUserBean> listErr = new ArrayList<>(); 
		Gson gson = new Gson();
		try {
			List<AcimUserBean> request = Arrays.asList(gson.fromJson(jsonReq, AcimUserBean[].class));
			log.info("xAcimReqId == " + xAcimReqId);
			log.info("xAcimSp == " + xAcimSp);
			
			if(BeanUtil.isNotEmpty(xAcimClientId)) {
				List<LovMaster> listLovxAcimClientId = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "CLIENT_ID", null, xAcimClientId, null, "Y", null, null, null, null, null,null);
				if(BeanUtil.isEmpty(listLovxAcimClientId)) {
					res.setDesc("x-acim-ClientId parameter, is invalid");
					res.setCode("40001");
				}
			}else {
				res.setDesc("Missing parameter, x-acim-ClientId");
				res.setCode("40001");
			}
			
			if(BeanUtil.isNotEmpty(res.getCode()) && res.getCode().equals("40001")) {
				return new ResponseEntity<String>(gson.toJson(res), httpHeadersRes,400);		
			}
			
			
			
			
			
			if(BeanUtil.isNotEmpty(request)) {
				for(AcimUserBean el : request ) {
					if(BeanUtil.isNotEmpty(el.getUsername()) && BeanUtil.isNotEmpty(el.getRole()) && BeanUtil.isNotEmpty(el.getRolePast())) {
						try {
							List<Object[]> listUser = acimService.listUserIdAndGroupName(el.getUsername(),el.getRolePast());
							if(BeanUtil.isEmpty(listUser)) {
								AcimUserBean userErr = new AcimUserBean();
								userErr.setDesc("Not found user " + el.getUsername() + " role " + el.getRolePast() + " in DT" );
								userErr.setUsername(el.getUsername());
								listErr.add(userErr);
							}else {
								List<LovMaster> listLovRole = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "ACIM_ROLE", null, el.getRole(),null, "Y", null, null, null, null, null,null);
								List<LovMaster> listLovRolePast = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "ACIM_ROLE", null, el.getRolePast(),null, "Y", null, null, null, null, null,null);
								if(BeanUtil.isNotEmpty(listLovRole) && BeanUtil.isNotEmpty(listLovRolePast) ) {
									acimService.updateTdUserLogin(el.getUsername(), listLovRolePast.get(0).getLovCode(), listLovRole.get(0).getLovCode());
								}else {
									AcimUserBean userErr = new AcimUserBean();
									userErr.setUsername(el.getUsername());
									userErr.setDesc("Not found role " + el.getRolePast()+ " or role " + el.getRole() + " in DT" );
									listErr.add(userErr);
								}
							}
						}catch (Exception e) {
							log.info(e.getMessage());
							AcimUserBean userErr = new AcimUserBean();
							userErr.setUsername(el.getUsername());
							userErr.setDesc("invalid request");
							listErr.add(userErr);
						}
					}
				}	
			}

		}catch (Exception e) {
			log.info(e.getMessage());
		}
		
		if(BeanUtil.isNotEmpty(listErr)) {
			res.setDesc("Some Process Fail");
			res.setCode("20001");
			res.setProcessFail(listErr);
		}else {
			res.setDesc("Success");
			res.setCode("20000");
		}
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeadersRes, HttpStatus.OK);		

		
	}
	
	@RequestMapping(value = prefixPath +"/acimuser/del",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding({AcimBean.class,AcimUserBean.class})
	public ResponseEntity<String> deleteUserAcim(@RequestHeader("x-acim-ClientId") String xAcimClientId,@RequestHeader("x-acim-req-id") String xAcimReqId ,@RequestHeader("x-acim-sp") String xAcimSp ,@RequestBody String jsonReq) {
		
		//String xAcimClientId = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-ClientId")) ? headers.getRequestHeader("x-acim-ClientId").get(0) : "" ) ;
		//String xAcimReqId = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-req-id")) ? headers.getRequestHeader("x-acim-req-id").get(0) : "" ) ;
		//String xAcimSp = (BeanUtil.isNotEmpty(headers.getRequestHeader("x-acim-sp")) ? headers.getRequestHeader("x-acim-sp").get(0) : "" ) ;
		
		final org.springframework.http.HttpHeaders httpHeadersRes= new org.springframework.http.HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		properties.put("x-acim-version", "1.0");
		properties.put("x-acim-req-id", xAcimReqId);
		properties.put("x-acim-sp", xAcimSp);
		httpHeadersRes.setAll(properties);
		
		AcimBean res = new AcimBean();
		List<AcimUserBean> listErr = new ArrayList<>(); 
		Gson gson = new Gson();
		try {
			List<AcimUserBean> request = Arrays.asList(gson.fromJson(jsonReq, AcimUserBean[].class));
			log.info("xAcimReqId == " + xAcimReqId);
			log.info("xAcimSp == " + xAcimSp);
			
			if(BeanUtil.isNotEmpty(xAcimClientId)) {
				List<LovMaster> listLovxAcimClientId = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "CLIENT_ID", null, xAcimClientId, null ,"Y", null, null, null, null, null,null);
				if(BeanUtil.isEmpty(listLovxAcimClientId)) {
					res.setDesc("x-acim-ClientId parameter, is invalid");
					res.setCode("40001");
				}
			}else {
				res.setDesc("Missing parameter, x-acim-ClientId");
				res.setCode("40001");
			}
			
			if(BeanUtil.isNotEmpty(res.getCode()) && res.getCode().equals("40001")) {
				return new ResponseEntity<String>(gson.toJson(res), httpHeadersRes,400);		

			}
			
			
			
			
			
			if(BeanUtil.isNotEmpty(request)) {
				for(AcimUserBean el : request ) {
					if(BeanUtil.isNotEmpty(el.getUsername()) && BeanUtil.isNotEmpty(el.getRole())) {
						try {
							List<Object[]> listUser = acimService.listUserIdAndGroupName(el.getUsername(),el.getRole());
							if(BeanUtil.isEmpty(listUser)) {
//								AcimUserBean userErr = new AcimUserBean();
//								userErr.setUsername(el.getUsername());
//								listErr.add(userErr);
							}else {
								List<LovMaster> listLovRole = lovMasterService.listLovMasterByCriteria("ACIM_ROLE", "ACIM_ROLE", null, el.getRole(), null ,"Y", null, null, null, null, null,null);
								if(BeanUtil.isNotEmpty(listLovRole)) {
									acimService.deleteTdUserLogin(el.getUsername(), listLovRole.get(0).getLovCode());
								}else {
									AcimUserBean userErr = new AcimUserBean();
									userErr.setUsername(el.getUsername());
									userErr.setDesc("Role "+ el.getRole() +" not found in DT");
									listErr.add(userErr);
								}
							}
						}catch (Exception e) {
							log.info(e.getMessage());
							AcimUserBean userErr = new AcimUserBean();
							userErr.setUsername(el.getUsername());
							userErr.setDesc("invalid request");
							listErr.add(userErr);
						}
					}
				}	
			}

		}catch (Exception e) {
			log.info(e.getMessage());
		}
		
		if(BeanUtil.isNotEmpty(listErr)) {
			res.setDesc("Some Process Fail");
			res.setCode("20001");
			res.setProcessFail(listErr);
		}else {
			res.setDesc("Success");
			res.setCode("20000");
		}
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeadersRes, HttpStatus.OK);		

		
	}

}
