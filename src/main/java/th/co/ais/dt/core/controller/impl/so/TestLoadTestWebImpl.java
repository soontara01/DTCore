package th.co.ais.dt.core.controller.impl.so;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.LoadtestBean;
import th.co.ais.dt.controller.dto.LoadtestServiceBean;
import th.co.ais.dt.controller.dto.LovMasterInput;
import th.co.ais.dt.controller.dto.LovMasterResponse;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.service.core.interfaces.test.LoadtestService;



@RestController
@Slf4j
@AllArgsConstructor
public class TestLoadTestWebImpl {
	
	final String prefixPath = "api/test-loadtest/v1";
	
	private final LoadtestService loadtestService ;
	
	@GetMapping(prefixPath + "/api1")
	public @ResponseBody ResponseEntity<?> api1() {
		Gson gson = new Gson();
		
		loadtestService.api1() ;

		return ResponseEntity.ok().body("ok");

	}
	
	@GetMapping(prefixPath +"/api2")
	public @ResponseBody ResponseEntity<?> api2() {
		Gson gson = new Gson();
		
		loadtestService.api2() ;

		return ResponseEntity.ok().body("ok");

	}
	
	@GetMapping(prefixPath +"/api3")
	public @ResponseBody ResponseEntity<?> api3() {
		Gson gson = new Gson();
		
		loadtestService.api3() ;

		return ResponseEntity.ok().body("ok");

	}
	
	@GetMapping(prefixPath +"/api4")
	public @ResponseBody ResponseEntity<?> api4() {
		Gson gson = new Gson();
		
		loadtestService.api4() ;

		return ResponseEntity.ok().body("ok");

	}
	
	@GetMapping(prefixPath +"/api5")
	public @ResponseBody ResponseEntity<?> api5() {
		Gson gson = new Gson();
		
		loadtestService.api5() ;

		return ResponseEntity.ok().body("ok");

	}
	
	@GetMapping(prefixPath +"/api6")
	public @ResponseBody ResponseEntity<?> api6() {
		Gson gson = new Gson();
		
		List<LovMaster> el = loadtestService.api6() ;

		return ResponseEntity.ok().body(gson.toJson(el));

	}
	
	@GetMapping(prefixPath +"/api7")
	public @ResponseBody ResponseEntity<?> api7() {
		Gson gson = new Gson();
		
		loadtestService.api7() ;

		return ResponseEntity.ok().body("ok");

	}
	
	@RequestMapping(value = prefixPath +"/api8",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LoadtestBean.class})
	public ResponseEntity<String> api8(@RequestBody String jsonRequest) {
		Gson gson = new Gson();
		LoadtestBean in = new LoadtestBean();
		in = gson.fromJson(jsonRequest, LoadtestBean.class);
		
		
		log.info("api8======== in == " + in.getT1());
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		LoadtestServiceBean inService = new LoadtestServiceBean();
		inService.setService3("99999999999999999999999999999999999999999999999999999999");
		loadtestService.api8(inService) ;

		return new ResponseEntity<String>("ok", httpHeaders, HttpStatus.OK);

	}
	
	@GetMapping(prefixPath +"/api9")
	public @ResponseBody ResponseEntity<?> api9() {
		
		for(int i = 0 ; i < 1000 ; i++) {
			log.info("api9======== in == " + i);
		}
		
		return ResponseEntity.ok().body("ok");

	}
	
	@GetMapping(prefixPath +"/api10")
	public @ResponseBody ResponseEntity<?> api10() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0 ; i < 10 ; i++) {
			log.info("api9======== in == " + i);
		}
		
		return ResponseEntity.ok().body("ok");

	}


}
