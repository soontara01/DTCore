package th.co.ais.dt.core.controller.impl.mt;

import java.util.HashMap;
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
import com.google.gson.JsonParseException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.PLMDeviceOfferProcessBean;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.IPLMDeviceOfferProcessService;

@RestController
@Slf4j
@AllArgsConstructor
public class PLMDeviceOfferProcessWebImpl {
	final String prefixPath = "api/mt/v1";
	
	private final IPLMDeviceOfferProcessService pLMDeviceOfferProcessService ;
	
	@RequestMapping(value = prefixPath +"/listPriceUpdate",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(PLMDeviceOfferProcessBean.class)
	public ResponseEntity<String> listPriceUpdate(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		PLMDeviceOfferProcessBean response = new PLMDeviceOfferProcessBean();
		Gson gson = new Gson();
		try {
			PLMDeviceOfferProcessBean request = gson.fromJson(jsonRequest, PLMDeviceOfferProcessBean.class);
			response = pLMDeviceOfferProcessService.validateAndGetListPriceUpdate(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(BeanUtil.isEmpty(e.getMessage()) ? "System eror" : e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = prefixPath +"/listProductUpdate",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(PLMDeviceOfferProcessBean.class)
	public ResponseEntity<String> listProductUpdate(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		PLMDeviceOfferProcessBean response = new PLMDeviceOfferProcessBean();
		Gson gson = new Gson();
		try {
			PLMDeviceOfferProcessBean request = gson.fromJson(jsonRequest, PLMDeviceOfferProcessBean.class);
			response = pLMDeviceOfferProcessService.validateAndGetListProductUpdate(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(BeanUtil.isEmpty(e.getMessage()) ? "System eror" : e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		
	}

}
