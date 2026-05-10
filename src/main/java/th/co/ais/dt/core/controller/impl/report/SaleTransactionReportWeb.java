package th.co.ais.dt.core.controller.impl.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.MMKTRBean;
import th.co.ais.dt.controller.dto.MMKTRReportBean;
import th.co.ais.dt.controller.dto.MMKTRReportRes;
import th.co.ais.dt.controller.dto.PmCompanyOutBean;
import th.co.ais.dt.controller.dto.ProjectSaleTransBean;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.service.core.interfaces.report.MMKTRReportService;
import th.co.ais.dt.service.core.interfaces.st.SaleTransactionReportService;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class SaleTransactionReportWeb {

	final String prefixPath = "api/report/v1/saleTrans";

	private SaleTransactionReportService saleTransactionReportService;
	
	@RequestMapping(value = prefixPath + "/query-list-sale-transaction-cloud", method = RequestMethod.POST, produces = {"application/json" })
	public ResponseEntity<String> queryProjectNew(@RequestBody String in) {
		//refactor from node query-list-sale-transaction-new, dtws queryProjectNew
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
		CommonResponseBean response = new CommonResponseBean();
		try {
			ProjectSaleTransBean request = new Gson().fromJson(in, ProjectSaleTransBean.class);
			List<ProjectSaleTransBean> projectSaleTransList = saleTransactionReportService.queryProjectByCriteriaNew(request.getCompenProjectType());
			if(projectSaleTransList != null && projectSaleTransList.size() > 0) {
				response.setResultCode("20000");
				response.setResultDescription("Success");
				response.setResultObj(projectSaleTransList);
			}else {
				response.setResultCode("50000");
				response.setResultDescription("Data not found");
			}
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
		} catch (Exception e) {
			log.error("query-list-sale-transaction-new", e);
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = prefixPath + "/queryLocationOfDealer", method = RequestMethod.POST, produces = {"application/json" })
	public ResponseEntity<String> queryLocationOfDealer(@RequestBody String input) {
		log.info("Request queryLocationOfDealer(): " + input);
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
		CommonResponseBean response = new CommonResponseBean();
		try {
			ProjectSaleTransBean request = new Gson().fromJson(input, ProjectSaleTransBean.class);
			List<LocationMst> listLocation = saleTransactionReportService.queryLocationOfDealer(request.getDealerCode(), request.getLocationCode());
			if(listLocation != null && listLocation.size() > 0) {
				response.setResultCode("20000");
				response.setResultDescription("Success");
				response.setResultObj(listLocation);
			}else {
				response.setResultCode("50000");
				response.setResultDescription("Data not found");
			}
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
		} catch (Exception e) {
			log.error("queryLocationOfDealer", e);
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
}
