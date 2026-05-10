package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ResBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap168Service;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.util.HttpClientUtilDT;

@RestController
@Slf4j
@AllArgsConstructor
public class Sap168ApiWebImpl {
final String prefixPath = "api/sap-interim/v1";
	
	private final ISapCallApiService sapCallApiService ;
	private final ISap168Service sap168Service;
	private final DTConfig dTConfig;
	
	@RequestMapping(value = prefixPath +"/168",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> goodsMovementReq(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Sap168ResBean res = new Sap168ResBean(); ;
		Gson gson = new Gson();
		Sap168ReqBean input = new Sap168ReqBean();
		try {
			 input = gson.fromJson(jsonRequest, Sap168ReqBean.class);
			 
			 res = sapCallApiService.call168Api(input);
			
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessageType("E");
		}
		
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
	}
	
	@PostMapping(path = prefixPath + "/handle-transfer-in", produces = {"application/json"}, consumes = {"application/json"})
	public ResponseEntity<String> handleSap168InterimTransferIn(@RequestParam(name="userId") String userId, @RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		Sap168ResBean sapRes = null;
		
		try {
			Sap168ReqBean sapReq = gson.fromJson(jsonRequest, Sap168ReqBean.class);
			if(sapReq != null) {
				sap168Service.callSapGoodsMovementCreate_0168(sapReq, userId);
			}
		} catch (Exception e) {
			log.error("handle-transfer-in : " + jsonRequest);
			log.error("Error", e);
		}
		return new ResponseEntity<String>(gson.toJson(sapRes), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = prefixPath +"/168-res",method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> goodsMovementRes(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Sap168ResBean res = null ;
		Gson gson = new Gson();
		try {
			res = gson.fromJson(jsonRequest, Sap168ResBean.class);
			 
			 goodsMovementResUpdate(res);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>(gson.toJson(null), httpHeaders, HttpStatus.OK);
	}
	
	private void goodsMovementResUpdate(Sap168ResBean res) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
                
		c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/168-res",gson.toJson(res) , "POST", null) ;

   }
}
