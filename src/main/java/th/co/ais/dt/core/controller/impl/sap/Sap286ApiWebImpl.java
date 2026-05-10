package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

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
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap286ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap286ResBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;

@RestController
@Slf4j
@AllArgsConstructor
public class Sap286ApiWebImpl {
	final String prefixPath = "api/sap-interim/v1";

	private final ISapCallApiService sapCallApiService;

	@RequestMapping(value = prefixPath + "/286", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<String> handleTransactionTypeSaleNormal(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Sap286ResBean res = null;
		Gson gson = new Gson();
		Sap286ReqBean input = new Sap286ReqBean();
		try {
			input = gson.fromJson(jsonRequest, Sap286ReqBean.class);
			res = sapCallApiService.call286Api(input);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

	}
}
