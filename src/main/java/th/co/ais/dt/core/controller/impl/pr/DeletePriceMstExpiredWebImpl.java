package th.co.ais.dt.core.controller.impl.pr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.pr.PriceMstService;

@RestController
@Slf4j
@AllArgsConstructor
public class DeletePriceMstExpiredWebImpl  {
	final String prefixPath = "api/price/v1";
	
	private final PriceMstService priceMstService ;

	@RequestMapping(
			  value = prefixPath +"/delete-pricemst-expired",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	public ResponseEntity<String> DeletePricemstExpired(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
        try {
        	//formatdate  DDMMYYYY HH24:MI
        	SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy HH:mm", Locale.US);
        	priceMstService.deletePriceExpired(dateFormat.format(new Date()));
		} catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	    return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);
	
	}

}
