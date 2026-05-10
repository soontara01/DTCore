package th.co.ais.dt.core.controller.impl.util;

import java.util.HashMap;
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
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.SyncLovBean;


@RestController
@Slf4j
@AllArgsConstructor
public class SyncLovFromDtWebImpl {
	final String prefixPath = "api/util/v1";
	
	private final LovMasterService lovMasterService ;
	
	@RequestMapping(value = prefixPath +"/sync-lov",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(SyncLovBean.class)
	public ResponseEntity<String> syncLov( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		SyncLovBean in = new SyncLovBean();
		
		Gson gson = new Gson();
		
		try {
			in = gson.fromJson(jsonReq, SyncLovBean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		
		try {
			if(in.getAction().equals("Insert")) {
				lovMasterService.syncSaveLovMasterList(in.getListLov().get(0).getLovType() , in.getListLov());
			}else if (in.getAction().equals("Delete")) {
				lovMasterService.syncDeleteLovMasterList(in.getListLov().get(0).getLovType());
			}
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
		}
		
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		
	}

}
