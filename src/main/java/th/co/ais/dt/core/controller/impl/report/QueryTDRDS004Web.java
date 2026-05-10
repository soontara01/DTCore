package th.co.ais.dt.core.controller.impl.report;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.controller.impl.report.dto.TDRDS004Bean;
import th.co.ais.dt.core.util.HttpClientUtil;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.util.AscMstService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/report/v1/TDRDS004")
@Slf4j
@AllArgsConstructor
public class QueryTDRDS004Web {
	
	private final LovMasterService lovMasterService;
	private final AscMstService ascMstService;

	@PostMapping(value = "query-criteria")
	public ResponseEntity<String> queryAsc(@RequestBody String input){
		TDRDS004Bean request = new TDRDS004Bean();
		TDRDS004Bean response = new TDRDS004Bean();
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();

        Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
		try {
			request = new Gson().fromJson(input, TDRDS004Bean.class);
			ProcessResult validateQuery = validateInputCriteria(request);
			if(validateQuery.isSuccess()) {
				switch (request.getAction()) {
				case "AscType":
					response.setResultCode("20000");
					response.setResultDescription("Success");
					response.setDeveloperMessage("Success");
					response.setLovList(lovMasterService.listLovMasterByCriteria("ASC_TYPE", null, null, null, null, "Y"));
					break;
				case "AscQuery":
					response.setResultCode("20000");
					response.setResultDescription("Success");
					response.setDeveloperMessage("Success");
					response.setAscMst(ascMstService.listAscMstByCriteria(request.getAscCode(), request.getAscName(), null, null, null, null, null, null, request.getAscType(), null, null));
					break;
				default:
					response.setResultCode("50000");
					response.setResultDescription("Something went wrong, Please try again");
					response.setDeveloperMessage("Something went wrong, Please try again");
					break;
				}
			}else {
				response.setResultCode("50000");
				response.setResultDescription(validateQuery.getMessage());
				response.setDeveloperMessage(validateQuery.getMessage());
			}
		}catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		}catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	private ProcessResult validateInputCriteria(TDRDS004Bean request) {
		if(BeanUtil.isEmpty(request.getAction())) {
			return new ProcessResult(false, request, "Please input Action");
		}else {
			if(request.getAction().equalsIgnoreCase("AscQuery")) {
				if(BeanUtil.isEmpty(request.getAscCode()) && BeanUtil.isEmpty(request.getAscName()) && BeanUtil.isEmpty(request.getAscType())) {
					return new ProcessResult(false, request, "Please input parameter at least 1");
				}
			}
		}
		return new ProcessResult(true, request, "S");
	}
	
}
