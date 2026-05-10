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
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.ISetupTravellerSimService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.SetupTravellerSimBean;

@RestController
@Slf4j
@AllArgsConstructor
public class SetupTravellerSimWebImpl {
	final String prefixPath = "api/setupTravellerSim/v1";
	
	private final ISetupTravellerSimService setupTravellerSimService ;
	
	@RequestMapping(value = prefixPath +"/query-setup-traveller-sim",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupTravellerSimBean.class)
	public ResponseEntity<String> querySetupTravellerSim(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupTravellerSimBean input = new SetupTravellerSimBean();
		SetupTravellerSimBean response = new SetupTravellerSimBean();
		
		try {
			
			input = gson.fromJson(jsonRequest, SetupTravellerSimBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
			if(BeanUtil.isNotEmpty(input)) {
				List<String> listMatCode = input.getListMatCode();			
				String serviceUsage = input.getServiceUsage();
				String mktSimType = input.getMktSimType();

				if ((listMatCode == null && BeanUtil.isEmpty(listMatCode)) && (serviceUsage == null && BeanUtil.isEmpty(serviceUsage)) && (mktSimType == null && BeanUtil.isEmpty(mktSimType))) {
					return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Mat Code or Service Usage or Marketing SIM Type is require\",\"developerMessage\":\"Mat Code or Service Usage or Marketing SIM Type is require\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

				}
				
				ProcessResult result = setupTravellerSimService.querySetupTravellerSim(listMatCode, serviceUsage, mktSimType);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setTravellerConfMstList((List<SetupTravellerSimBean>) result.getResultObject());

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
	
	@RequestMapping(value = prefixPath +"/edit-setup-traveller-sim",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupTravellerSimBean.class)
	public ResponseEntity<String> editSetupTravellerSim(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupTravellerSimBean input = new SetupTravellerSimBean();
		SetupTravellerSimBean response = new SetupTravellerSimBean();
		
		try {
			
			input = gson.fromJson(jsonRequest, SetupTravellerSimBean.class);

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		try {
			
			if(BeanUtil.isNotEmpty(input)) {
				
				ProcessResult result = setupTravellerSimService.editSetupTravellerSim(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
//					response.setTravellerConfMstList((List<SetupTravellerSimBean>) result.getResultObject());

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
	
	@RequestMapping(value = prefixPath +"/save-traveller-conf-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupTravellerSimBean.class)
	public ResponseEntity<String> saveTravellerConfMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupTravellerSimBean input = new SetupTravellerSimBean();
		SetupTravellerSimBean response = new SetupTravellerSimBean();
		
		try {
			input = gson.fromJson(jsonRequest, SetupTravellerSimBean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}
		
		try {	
			if(BeanUtil.isNotEmpty(input)) {
				
				ProcessResult result = setupTravellerSimService.saveTravellerConfMst(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
//					response.setTravellerConfMstList((List<SetupTravellerSimBean>) result.getResultObject());

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
	
	@RequestMapping(value = prefixPath +"/validate-upload-traveller-conf-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(SetupTravellerSimBean.class)
	public ResponseEntity<String> validateUploadTravellerConfMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		SetupTravellerSimBean input = new SetupTravellerSimBean();
		SetupTravellerSimBean response = new SetupTravellerSimBean();
		
		try {
			input = gson.fromJson(jsonRequest, SetupTravellerSimBean.class);
			try {
				ProcessResult resultQueryServiceLockHsMst = setupTravellerSimService.validateUploadTravellerConfMst(input);
				if (resultQueryServiceLockHsMst.isSuccess()) {
					log.info("success");
					response.setResultCode("20000");
					response.setResultDescription("Success Validate Data");
					response.setDeveloperMessage("Success Validate Data");
					response.setTravellerConfMstList((List<SetupTravellerSimBean>) resultQueryServiceLockHsMst.getResultObject());
				} else {
					response.setResultCode("50000");
					response.setResultDescription(resultQueryServiceLockHsMst.getMessage());
					response.setDeveloperMessage(resultQueryServiceLockHsMst.getMessage());
					response.setTravellerConfMstList((List<SetupTravellerSimBean>)resultQueryServiceLockHsMst.getResultObject());
				}
			} catch (Exception e) {
				log.info(e.getMessage());
				response.setResultCode("50000");
				response.setResultDescription("System Error.");
				response.setDeveloperMessage("System Error.");
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
