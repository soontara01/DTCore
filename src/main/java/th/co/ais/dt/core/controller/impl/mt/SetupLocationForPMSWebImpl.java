package th.co.ais.dt.core.controller.impl.mt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import lombok.AllArgsConstructor;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.ISetupLocationForPMSService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.SetupLocationForPMSBean;

@RestController
@AllArgsConstructor
public class SetupLocationForPMSWebImpl {
	final String prefixPath = "api/setupLocationForPMS/v1";
	
	private final ISetupLocationForPMSService setupLocationForPMSService ;
	
	@RequestMapping(value = prefixPath +"/save-location-for-PMS",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupLocationForPMSBean.class)
	public ResponseEntity<String> saveLocationForPMS(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupLocationForPMSBean response = new SetupLocationForPMSBean();
			try {
				SetupLocationForPMSBean input = new Gson().fromJson(jsonRequest, SetupLocationForPMSBean.class);
				if(BeanUtil.isNotEmpty(input)) {
					
					ProcessResult result = setupLocationForPMSService.saveLocationForPMS(input);
					if(result.isSuccess()) {
						response.setResultCode("20000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
						response.setLocationForPMSList((List<SetupLocationForPMSBean>) result.getResultObject());
	
					}else {
						response.setResultCode("50000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
					}
				}
				
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
					
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

			}
	}
	
	@RequestMapping(value = prefixPath +"/delete-location-for-PMS",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupLocationForPMSBean.class)
	public ResponseEntity<String> deleteLocationForPMS(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupLocationForPMSBean response = new SetupLocationForPMSBean();
			try {
				SetupLocationForPMSBean input = new Gson().fromJson(jsonRequest, SetupLocationForPMSBean.class);
				if(BeanUtil.isNotEmpty(input)) {
					
					ProcessResult result = setupLocationForPMSService.deleteLocationForPMS(input);
					if(result.isSuccess()) {
						response.setResultCode("20000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
						response.setLocationForPMSList((List<SetupLocationForPMSBean>) result.getResultObject());
	
					}else {
						response.setResultCode("50000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
					}
				}
				
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
					
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@RequestMapping(value = prefixPath +"/edit-location-for-PMS",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupLocationForPMSBean.class)
	public ResponseEntity<String> editLocationForPMS(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupLocationForPMSBean response = new SetupLocationForPMSBean();
			try {
				SetupLocationForPMSBean input = new Gson().fromJson(jsonRequest, SetupLocationForPMSBean.class);
				if(BeanUtil.isNotEmpty(input)) {
					
					ProcessResult result = setupLocationForPMSService.editLocationForPMS(input);
					if(result.isSuccess()) {
						response.setResultCode("20000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
						response.setLocationForPMSList((List<SetupLocationForPMSBean>) result.getResultObject());
	
					}else {
						response.setResultCode("50000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
					}
				}
				
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
					
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

			}
	}
	
	@RequestMapping(value = prefixPath +"/query-setup-location-for-PMS",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupLocationForPMSBean.class)
	public ResponseEntity<String> querySetupLocationForPMS(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupLocationForPMSBean response = new SetupLocationForPMSBean();
			try {
				SetupLocationForPMSBean input = new Gson().fromJson(jsonRequest, SetupLocationForPMSBean.class);
				if(BeanUtil.isNotEmpty(input)) {
					
					ProcessResult result = setupLocationForPMSService.querySetupLocationForPMS(input);
					if(result.isSuccess()) {
						response.setResultCode("20000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
						response.setLocationForPMSList((List<SetupLocationForPMSBean>) result.getResultObject());
	
					}else {
						response.setResultCode("50000");
						response.setResultDescription(result.getMessage());
	                    response.setDeveloperMessage(result.getMessage());
					}
				}
				
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
					
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"" + e.getMessage() + "\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
}
