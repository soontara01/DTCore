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
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.IMappingMatcodeService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.MappingMatcodeBean;

@RestController
@Slf4j
@AllArgsConstructor
public class MappingMatcodeWebImpl {
	final String prefixPath = "api/master-config/v1";
	
	private final IMappingMatcodeService mappingMatcodeService ;
	
	
	@RequestMapping(value = prefixPath +"/query-mapping-matcode",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MappingMatcodeBean.class)
	public ResponseEntity<String> queryMappingMatcode(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		MappingMatcodeBean input = new MappingMatcodeBean();
		MappingMatcodeBean response = new MappingMatcodeBean();
		//IMappingMatcodeService mappingMatcodeService = (IMappingMatcodeService) SpringApplicationContext.getBean("mappingMatcodeService");
		Gson gson = new Gson();
		try {
			
			try {
				input = gson.fromJson(jsonRequest, MappingMatcodeBean.class);
			} catch (Exception e) {
				log.info(e.getMessage());
				response.setResultCode("50000");
				response.setResultDescription("Request JSON format is incorrect");
				response.setDeveloperMessage("Request JSON format is incorrect");
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
			}
			if(BeanUtil.isNotEmpty(input)) {

				ProcessResult result = mappingMatcodeService.queryMappingMatcode(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setListMappingMatCode((List<MappingMatcodeBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
				}
			}
				
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/delete-mapping-matcode",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MappingMatcodeBean.class)
	public ResponseEntity<String> deleteMappingMatcode(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		MappingMatcodeBean input = new MappingMatcodeBean();
		MappingMatcodeBean response = new MappingMatcodeBean();
		Gson gson = new Gson();
		try {
			input = gson.fromJson(jsonRequest, MappingMatcodeBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = mappingMatcodeService.deleteMappingMatcode(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setListMappingMatCode((List<MappingMatcodeBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	
		
	}
	
	@RequestMapping(value = prefixPath +"/upload-mapping-matcode",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MappingMatcodeBean.class)
	public ResponseEntity<String> uploadMappingMatcode(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		MappingMatcodeBean input = new MappingMatcodeBean();
		MappingMatcodeBean response = new MappingMatcodeBean();
		Gson gson = new Gson();
		try {
			input = gson.fromJson(jsonRequest, MappingMatcodeBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = mappingMatcodeService.uploadMappingMatcode(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setListMappingMatCode((List<MappingMatcodeBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setListMappingMatCode((List<MappingMatcodeBean>) result.getResultObject());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/validate-mapping-matcode",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MappingMatcodeBean.class)
	public ResponseEntity<String> validateMappingMatCode(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		MappingMatcodeBean input = new MappingMatcodeBean();
		MappingMatcodeBean response = new MappingMatcodeBean();
		Gson gson = new Gson();
		try {
			input = gson.fromJson(jsonRequest, MappingMatcodeBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = mappingMatcodeService.validateMappingMatcode(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setListMappingMatCode((List<MappingMatcodeBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setListMappingMatCode((List<MappingMatcodeBean>) result.getResultObject());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		
	}

}
