package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.ProductMstSapBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleProductMasterService;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleProductMasterWebImpl {
	
	final String prefixPath = "api/sap-handle-product-master/v1";
	
	ISapHandleProductMasterService sapHandleProductMasterService;
	
    @PostMapping(value = prefixPath + "/insert-product-master", produces = {"application/json"})
    public ResponseEntity<String> handleInsertProductMaster(@RequestBody String jsonRequest) {
    	Gson gson = new Gson();
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        CommonResponseBean response = new CommonResponseBean();

        try {
        	//log.info("Sap MappingProductMaster >>>>>>>>>> jsonRequest : "+jsonRequest);
        	ProductMstSapBean req = gson.fromJson(jsonRequest, ProductMstSapBean.class);
        	response = sapHandleProductMasterService.mappingProductMaster(req);
        } catch (Exception e) {
        	 response.setResultCode("50000");
             response.setResultStatus("F");
             response.setResultDescription(e.getMessage());
        	return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
    
}
