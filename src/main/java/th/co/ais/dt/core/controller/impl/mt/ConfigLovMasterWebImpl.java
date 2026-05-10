package th.co.ais.dt.core.controller.impl.mt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.controller.dto.ConfigLovMasterBean;
import th.co.ais.dt.controller.dto.DisplayConfigLovMasterBean;


@RestController
@Slf4j
@AllArgsConstructor
public class ConfigLovMasterWebImpl {
	final String prefixPath = "api/configLovMaster/v1";
	
	private final IConfigLovMasterService configLovMasterService ;
	
	@RequestMapping(value = prefixPath +"/check-auth-user",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> checkAuthUser(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

	try {
			
			Gson gson = new Gson();
			ConfigLovMasterBean input = new ConfigLovMasterBean();
			ConfigLovMasterBean response = new ConfigLovMasterBean();
			log.info("---------- check auth user --------------");
			try {
				input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}

			if (input != null && BeanUtil.isNotEmpty(input)) {
				try {
					ProcessResult result = new ProcessResult(false, null, "");
					result = configLovMasterService.checkAuthUser(input.getUserId());
				
					if (null == result.getResultObject() && "".equals(result.getResultObject())) {
						return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);
		
					}
								
					response.setResultCode("20000");
					response.setDeveloperMessage("Success");
					response.setResultDescription("Success");
					response.setLovTypeList((List<LovMaster>) result.getResultObject());
						
				} catch (Exception e) {
					response.setDeveloperMessage(e.getMessage());
					response.setResultCode("50000");
					response.setResultDescription(e.getMessage());
				}
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);
	
			}

		} catch (Exception e) {
			log.error("queryProductMasterService", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		
	}
	
	@RequestMapping(value = prefixPath +"/query-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> queryLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

        try {
			
			Gson gson = new Gson();
			LovMaster input = new LovMaster();
			ConfigLovMasterBean response = new ConfigLovMasterBean();
			log.info("---------- queryLovMaster --------------");
			try {
				input = gson.fromJson(jsonRequest, LovMaster.class);

			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}

			if (input != null && BeanUtil.isNotEmpty(input)) {
				try {
					ProcessResult listDataActive = new ProcessResult(false, null, "");
					ProcessResult listDataInactive = new ProcessResult(false, null, "");
					ProcessResult listKeyData = new ProcessResult(false, null, "");
					List<DisplayConfigLovMasterBean> display = configLovMasterService.queryDisplayConfigLovMaster(input.getLovType());
					listDataActive = configLovMasterService.queryLovMasterActive(input,display);
					listDataInactive = configLovMasterService.queryLovMasterInactive(input,display);
					listKeyData = configLovMasterService.queryListKeyData(input.getLovType());
				
					if (null == listDataActive.getResultObject() && "".equals(listDataActive.getResultObject())) {
						return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);

					}
					if (null == listDataInactive.getResultObject() && "".equals(listDataInactive.getResultObject())) {
						return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);

					}
								
					response.setResultCode("20000");
					response.setDeveloperMessage("Success");
					response.setResultDescription("Success");
					response.setListKeyData((String) listKeyData.getResultObject());
					response.setListDataActive((List<LovMaster>) listDataActive.getResultObject());
					response.setListDataInactive((List<LovMaster>) listDataInactive.getResultObject());
						
				} catch (Exception e) {
					response.setDeveloperMessage(e.getMessage());
					response.setResultCode("50000");
					response.setResultDescription(e.getMessage());
				}

				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);

			}

		} catch (Exception e) {
			log.error("queryProductMasterService", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		
	}
	
	@RequestMapping(value = prefixPath +"/insert-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> insertLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		ConfigLovMasterBean input = new ConfigLovMasterBean();
		ConfigLovMasterBean response = new ConfigLovMasterBean();
		try {
			
			input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
			if(BeanUtil.isNotEmpty(input)) {
								
				ProcessResult result = configLovMasterService.insertLovMaster(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription("Success");
//					response.setStatus("S");
				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
//					response.setStatus("F");
				}
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				
		} catch (Exception e) {
			log.error("insert Lov Master", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/edit-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> editLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		ConfigLovMasterBean input = new ConfigLovMasterBean();
		ConfigLovMasterBean response = new ConfigLovMasterBean();
		try {
			
			input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = configLovMasterService.editLovMaster(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription("Success");
//					response.setStatus("S");
				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
//					response.setStatus("F");
				}
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				
		} catch (Exception e) {
			log.error("insert Lov Master", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/display-config-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> displayConfigLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

       try {
			
			Gson gson = new Gson();
			ConfigLovMasterBean input = new ConfigLovMasterBean();
			ConfigLovMasterBean response = new ConfigLovMasterBean();
			log.info("---------- displayConfigLovMaster --------------");
			try {
				input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}

			if (input != null && BeanUtil.isNotEmpty(input)) {
				try {
					List<DisplayConfigLovMasterBean> display = configLovMasterService.queryDisplayConfigLovMaster(input.getLovType());
					List<DisplayConfigLovMasterBean> header = configLovMasterService.queryHeaderConfigLovMaster(input.getLovType());
					List<DisplayConfigLovMasterBean> columnNew = configLovMasterService.getNewFunctionColumnByLovType(input.getLovType());

					if("EDIT".equals(input.getFlgQueryColumnEdit())) {
						Long seqNo = Long.parseLong(input.getSeqNo());
						List<DisplayConfigLovMasterBean> columnEdit = configLovMasterService.queryEditColumnConfigLovMaster(input.getLovType(), seqNo);
						response.setColumnEdit(columnEdit);
					}
					if("COPY".equals(input.getFlgQueryColumnEdit())) {
						Long seqNo = Long.parseLong(input.getSeqNo());
						List<DisplayConfigLovMasterBean> columnCopy = configLovMasterService.queryEditColumnConfigLovMaster(input.getLovType(), seqNo);
						response.setColumnCopy(columnCopy);
					}

					if (null == display && "".equals(display)) {
						return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);
					}
									
					response.setResultCode("20000");
					response.setDeveloperMessage("Success");
					response.setResultDescription("Success");
					response.setDisplay(display);
					response.setHeader(header);
					response.setColumnNew(columnNew);

						
				} catch (Exception e) {
					response.setDeveloperMessage(e.getMessage());
					response.setResultCode("50000");
					response.setResultDescription(e.getMessage());
				}
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("queryProductMasterService", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}


	@RequestMapping(value = prefixPath +"/set-flg-active-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> setFlgActiveLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		ConfigLovMasterBean input = new ConfigLovMasterBean();
		ConfigLovMasterBean response = new ConfigLovMasterBean();
		try {
			
			input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
//			log.info(input);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = configLovMasterService.setFlgLovMaster(input,true);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription("Success");
//					response.setStatus("S");
				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
//					response.setStatus("F");
				}
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				
		} catch (Exception e) {
			log.error("insert Lov Master", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/set-flg-inactive-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> setFlgInactiveLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		ConfigLovMasterBean input = new ConfigLovMasterBean();
		ConfigLovMasterBean response = new ConfigLovMasterBean();
		try {
			
			input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
//			log.info(input);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = configLovMasterService.setFlgLovMaster(input,false);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription("Success");
//					response.setStatus("S");
				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
//					response.setStatus("F");
				}
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				
		} catch (Exception e) {
			log.error("insert Lov Master", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/copy-lov-master",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ConfigLovMasterBean.class)
	public ResponseEntity<String> copyLovMaster(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		ConfigLovMasterBean input = new ConfigLovMasterBean();
		ConfigLovMasterBean response = new ConfigLovMasterBean();
		try {
			
			input = gson.fromJson(jsonRequest, ConfigLovMasterBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
			if(BeanUtil.isNotEmpty(input)) {
								
				ProcessResult result = configLovMasterService.copyLovMaster(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription("Success");
//					response.setStatus("S");
				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
//					response.setStatus("F");
				}
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				
		} catch (Exception e) {
			log.error("insert Lov Master", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
