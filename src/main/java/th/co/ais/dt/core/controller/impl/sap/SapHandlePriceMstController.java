package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.SapHandlePriceMstBean;
import th.co.ais.dt.core.service.core.impl.sap.SapHandlePriceMstService;

@RestController
@RequestMapping("api/sap-handle-price-mst/v1")
@Slf4j
public class SapHandlePriceMstController {
	
	@Autowired
	private SapHandlePriceMstService sapHandlePriceMstService;

    @PostMapping("price-mst")
    public ResponseEntity<String> handleMchMappingProduct(@RequestBody SapHandlePriceMstBean jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        Gson gson = new Gson();  
        
        try {
        	log.info("save price mat : "+jsonRequest);
        	response = sapHandlePriceMstService.callPriceMstForSave(jsonRequest);
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }
        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
}
