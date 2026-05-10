package th.co.ais.dt.core.controller.impl.report;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.controller.impl.report.dto.TDRDS008Bean;
import th.co.ais.dt.core.util.HttpClientUtil;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.util.ProductMstService;

@RestController
@RequestMapping("api/report/v1/TDRDS008")
@Slf4j
@AllArgsConstructor
public class QueryTDRDS008Web {

	private final ProductMstService productMstService;
	
	@PostMapping(value = "query-product-type-list")
	public ResponseEntity<String> queryProductTypeList(){
		
		Gson gson = new Gson();
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();
		TDRDS008Bean res = new TDRDS008Bean();

		try {

			ProcessResult result = productMstService.listProductType();
			res.setResultCode("20000");
			res.setResultDescription(result.getMessage());
			res.setDeveloperMessage(result.getMessage());
			res.setDataList((List<ProductMst>) result.getResultObject());
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			res.setResultCode("50000");
			res.setResultDescription("System error");
			res.setDeveloperMessage("System error");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		}
	}

}
