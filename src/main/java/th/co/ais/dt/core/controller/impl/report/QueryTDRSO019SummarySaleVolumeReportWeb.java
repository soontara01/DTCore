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
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019SummarySaleVolumeReportBean;
import th.co.ais.dt.core.service.core.interfaces.report.IQueryTDRSO019SummarySaleVolumeReportService;
import th.co.ais.dt.core.util.HttpClientUtil;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/report/v1/TDRSO019")
@Slf4j
@AllArgsConstructor
public class QueryTDRSO019SummarySaleVolumeReportWeb {

	private final IQueryTDRSO019SummarySaleVolumeReportService iQueryTDRSO019SummarySaleVolumeReportService;

	@PostMapping(value = "query-criteria")
	public ResponseEntity<String> queryCriteria(@RequestBody String jsonRequest){
		
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();
		Gson gson = new Gson();
		TDRSO019SummarySaleVolumeReportBean response = new TDRSO019SummarySaleVolumeReportBean();
		
		try {
			TDRSO019SummarySaleVolumeReportBean request = new Gson().fromJson(jsonRequest, TDRSO019SummarySaleVolumeReportBean.class);
			if(BeanUtil.isNotEmpty(request.getAction())) {
				switch (request.getAction()) {
				case "QUERY_CRITERIA":
					response = iQueryTDRSO019SummarySaleVolumeReportService.getDataCriteria(request);
					break;
				case "POPUP_LOCATION":
					response = iQueryTDRSO019SummarySaleVolumeReportService.validateAndGetDataForLocation(request);
					break;
				case "POPUP_DEALER":
					response = iQueryTDRSO019SummarySaleVolumeReportService.validateAndGetDataForDealer(request);
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
			response.setResultDescription(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
			response.setDeveloperMessage(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
}
