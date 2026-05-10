package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.ReserveProductBean;
import th.co.ais.dt.controller.dto.ReserveProductStockSapBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.PickingDocumentIn;
import th.co.ais.dt.core.service.core.interfaces.sap.IReserveProductStockService;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/reserve-product-stock/v1")
@Slf4j
@AllArgsConstructor
public class ReserveProductStockWebImpl {
	
	private final IReserveProductStockService reserveProductStockService;
	
	
	@PostMapping(value = "/reserveProduct", produces = {"application/json"})
    public ResponseEntity<String> reserveProduct(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
		Gson gson = new Gson();
		ReserveProductBean response = null;
        try {
        	ReserveProductBean input = gson.fromJson(jsonRequest, ReserveProductBean.class);
    		ReserveProductStockSapBean out = reserveProductStockService.callServiceReserve(input);
			response = new ReserveProductBean();
    		if(BeanUtil.isNotEmpty(out.getMessage())) {
    			response.setResultCode("50000");
    			response.setResultDescription(out.getMessage().toString());
    			response.setDeveloperMessage(out.getMessage().toString());
    			return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    		}
    		Map<String, String> shoppingCart = (Map<String, String>) out.getShoppingCart();
			response.setResultCode("20000");
			response.setResultDescription("Success");
			response.setDeveloperMessage("Success");
			response.setShoppingCartId(shoppingCart.get("id"));
        	
        } catch (Exception e) {
        	log.error("Error reserveProduct : "+e.getMessage());
        	response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
}
