package th.co.ais.dt.core.controller.impl.sap;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartResponse;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCancelShoppingCartService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/sap-product-stock/v1")
@Slf4j
@RequiredArgsConstructor
public class SapCancelShoppingCartWebImpl {
    
    private final ISapCancelShoppingCartService sapCancelShoppingCartService;
    private final ISapCallApiService sapCallApiService ;

    @PostMapping(value = "/cancel-shopping-cart", produces = {"application/json"})
    public ResponseEntity<String> cancelShoppingCart(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        Gson gson = new Gson();
        try {
            SapCancelShoppingCartRequest request = gson.fromJson(jsonRequest, SapCancelShoppingCartRequest.class);
            SapCancelShoppingCartResponse response = sapCancelShoppingCartService.cancelShoppingCart(request);

            return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("sap-cancel-shopping-cart")
	public ResponseEntity<String> sapCancelShoppingCart(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		

		SapCancelShoppingCartBean res = new SapCancelShoppingCartBean();
		res.setResultCode("20000");
		res.setResultDescription("Success");
		res.setDeveloperMessage("Success");

		Gson gson = new Gson();

		try {
			//DeviceSalesService deviceSalesService = (DeviceSalesService) SpringApplicationContext
					//.getBean("deviceSalesService");

			SapCancelShoppingCartBean request = gson.fromJson(in, SapCancelShoppingCartBean.class);

			if (BeanUtil.isEmpty(request.getSapReserveNo())) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"soId is required.\",\"developerMessage\":\"soId is required.\"}", httpHeaders, HttpStatus.OK);
			}
			
			DtSapCancelReserve dtSapCancelReserve = new DtSapCancelReserve();
			dtSapCancelReserve.setSapReserveNo(request.getSapReserveNo());
			SapCancelShoppingCartResponse ress = sapCallApiService.callCancelReserveApiRefac(dtSapCancelReserve);


			if (ress == null || ress.getShoppingCart() == null || BeanUtil.isEmpty(ress.getShoppingCart().getId())) {
				String msg = ress == null || BeanUtil.isEmpty(ress.getMessage())
						? "SAP Cancel Shopping Cart Error"
						: ress.getMessage();
				res.setResultCode("50000");
				res.setResultDescription(msg);
				res.setDeveloperMessage(msg);
			}

		} catch (Exception e) {
			log.error("sapCancelShoppingCart error " + e);
			res.setResultCode("50000");
			res.setResultDescription("SAP Cancel Shopping Cart Fail");
			res.setDeveloperMessage(e.getMessage());
		}
		return new ResponseEntity<String>((gson.toJson(res)), httpHeaders, HttpStatus.OK);
	
		
	}
}
