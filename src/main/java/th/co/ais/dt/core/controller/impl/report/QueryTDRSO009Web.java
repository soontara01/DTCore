package th.co.ais.dt.core.controller.impl.report;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO009Bean;
import th.co.ais.dt.core.service.core.interfaces.report.TDRSO009ReportService;
import th.co.ais.dt.core.util.HttpClientUtil;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.pr.IPmCompanyService;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/report/v1/TDRSO009")
@Slf4j
@AllArgsConstructor
public class QueryTDRSO009Web {
	
	private final IPmCompanyService pmCompanyService;
	private final TDRSO009ReportService tDRSO009ReportService;
	
	@PostMapping(value = "query-criteria")
	public ResponseEntity<String> queryCriteriaDetailControlShiftReport(@RequestBody String input){
		TDRSO009Bean response = new TDRSO009Bean();
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();
		Gson gson = new Gson();
		
		try {
			TDRSO009Bean request = new Gson().fromJson(input, TDRSO009Bean.class);
			if(BeanUtil.isNotEmpty(request.getAction())) {
				String message = "";
				switch (request.getAction()) {
					case "COMPANY":
						response.setResultCode("20000");
						response.setResultDescription("Success");
						response.setDeveloperMessage("Success");
						response.setCompanyList(pmCompanyService.getAllCompany());
						break;
					case "RANGE_LOCATION", "RANGE_LOCATION_LIKE":
						message = validateQueryCriteriaInput(request);
						if(BeanUtil.isEmpty(message)) {
							response.setResultCode("20000");
							response.setResultDescription("Success");
							response.setDeveloperMessage("Success");
							response.setLocationList(tDRSO009ReportService.getRangeLocation(request));
						}else {
							response.setResultCode("50000");
							response.setResultDescription(message);
							response.setDeveloperMessage(message);
						}
						break;
					case "MULTI_LOCATION":
						message = validateQueryCriteriaInput(request);
						if(BeanUtil.isEmpty(message)) {
							response.setResultCode("20000");
							response.setResultDescription("Success");
							response.setDeveloperMessage("Success");
							response.setLocationList(tDRSO009ReportService.getLocationByCriteria(request));
						}else {
							response.setResultCode("50000");
							response.setResultDescription(message);
							response.setDeveloperMessage(message);
						}
						break;
					case "MULTI_LOCATION_QUERY":
						message = validateQueryCriteriaInput(request);
						if(BeanUtil.isEmpty(message)) {
							response.setResultCode("20000");
							response.setResultDescription("Success");
							response.setDeveloperMessage("Success");
							response.setLocationList(tDRSO009ReportService.getMultiLocation(request));
						}else {
							response.setResultCode("50000");
							response.setResultDescription(message);
							response.setDeveloperMessage(message);
						}
						break;
					default:
						response.setResultCode("50000");
						response.setResultDescription("Input Action "+request.getAction()+" is not ready!!!");
						response.setDeveloperMessage("Input Action "+request.getAction()+" is not ready!!!");
						break;
				}
			}else {
				response.setResultCode("50000");
				response.setResultDescription("Please input Action");
				response.setDeveloperMessage("Please input Action");
			}
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);


	}
	
	private String validateQueryCriteriaInput(TDRSO009Bean request) {
		if (request.getAction().equalsIgnoreCase("RANGE_LOCATION") || request.getAction().equalsIgnoreCase("RANGE_LOCATION_WH") || request.getAction().equalsIgnoreCase("MULTI_LOCATION") ) {
			if(BeanUtil.isEmpty(request.getCompany())) {
				return "Please input company";
			}
		}

		return null;
	}
	
	@PostMapping(value = "validate-report-payload")
	public ResponseEntity<String> validateReportPayload(@RequestBody String input){
		
		TDRSO009Bean response = new TDRSO009Bean();
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();
		Gson gson = new Gson();

		try {
			TDRSO009Bean request = new Gson().fromJson(input, TDRSO009Bean.class);
			
			ProcessResult processResult = tDRSO009ReportService.validateInput(request);
			if(processResult.isSuccess()) {
				response.setResultCode("20000");
				response.setResultDescription(processResult.getMessage());
				response.setDeveloperMessage(processResult.getMessage());
			}else {
				response.setResultCode("50000");
				response.setResultDescription(processResult.getMessage());
				response.setDeveloperMessage(processResult.getMessage());
			}
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
}
