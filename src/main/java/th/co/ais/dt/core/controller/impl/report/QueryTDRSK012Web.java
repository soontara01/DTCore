package th.co.ais.dt.core.controller.impl.report;

import java.util.List;

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
import th.co.ais.dt.core.controller.impl.report.dto.TDRSK012Bean;
import th.co.ais.dt.core.service.core.interfaces.report.TDRSK012ReportService;
import th.co.ais.dt.core.util.HttpClientUtil;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/report/v1/TDRSK012")
@Slf4j
@AllArgsConstructor
public class QueryTDRSK012Web {

	private final TDRSK012ReportService tdrsk012Service;
	
	@PostMapping(value = "location-code-sale")
	public ResponseEntity<String> queryLocationCodeSaleAtShop(@RequestBody String input){
		TDRSK012Bean response = new TDRSK012Bean();
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();
		Gson gson = new Gson();
		try {
			TDRSK012Bean request = gson.fromJson(input, TDRSK012Bean.class);
			ProcessResult validateInput = validateInputSaveToBatch(request);
			if (validateInput.isSuccess()) {
				Object listCompany = request.getListCompany();
				String locationCode = request.getLocationCode();
				List<LocationMst> listLocation = tdrsk012Service.queryLocationCodeSaleAtShop(listCompany, locationCode);
				response.setLocationList(listLocation);
//				response.setResultCount(listLovMaster.size());
				response.setResultCode("20000");
				response.setResultDescription("Success");
				response.setDeveloperMessage("Success");
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

			}else {
				response.setResultCode("50000");
				response.setResultDescription(validateInput.getMessage());
				response.setDeveloperMessage(validateInput.getMessage());
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

		}
	}
	
	private ProcessResult validateInputSaveToBatch(TDRSK012Bean request) {
		if (BeanUtil.isEmpty(request.getListCompany()) ) {
			return new ProcessResult(false, request, "Please input Company");
		}
		return new ProcessResult(true, request, "S");
	}
}
