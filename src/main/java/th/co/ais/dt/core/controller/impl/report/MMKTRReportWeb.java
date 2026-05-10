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
import com.google.gson.JsonSyntaxException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.MMKTRBean;
import th.co.ais.dt.controller.dto.MMKTRReportBean;
import th.co.ais.dt.controller.dto.MMKTRReportRes;
import th.co.ais.dt.controller.dto.PmCompanyOutBean;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.service.core.interfaces.report.MMKTRReportService;
import th.co.ais.dt.util.BeanUtil;

@RestController
@Slf4j
@AllArgsConstructor
public class MMKTRReportWeb {

	final String prefixPath = "api/report/v1";

	private MMKTRReportService mmktrReportService;
	
	@RequestMapping(value = prefixPath + "/check-receipt-num", method = RequestMethod.POST, produces = {"application/json" })
	@RegisterReflectionForBinding({MMKTRReportBean.class,MMKTRReportRes.class})
	public ResponseEntity<String> checkReceiptNum(@RequestBody String in) {
		log.info("Request checkReceiptNum(): " + in);
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
		MMKTRReportRes response = new MMKTRReportRes();
		try {
//			mmktrReportService = (MMKTRReportService) SpringApplicationContext.getBean("mmktrReportService");
			MMKTRReportBean request = new Gson().fromJson(in, MMKTRReportBean.class);
			if(BeanUtil.isNotEmpty(request.getReceiptNum())) {
				List<MMKTRBean> result = mmktrReportService.checkReceiptNum(request.getReceiptNum());
				if(result != null && result.size() > 0) {
					response.setMmktrBeanList(result);
					response.setResultCode("20000");
					response.setResultDesc("Success");
					response.setStatus("S");
				}
			}
		} catch (JsonSyntaxException e) {
			response.setResultCode("50000");
			response.setResultDesc("Request JSON format is incorrect");
			response.setStatus("F");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		} catch (ForceTerminateException e) {
			response.setResultCode("50000");
			response.setResultDesc(e.getMessage());
			response.setStatus("F");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDesc("Error check Receipt Num");
			response.setStatus("F");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info("Response checkReceiptNum(): " + new Gson().toJson(response));
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = prefixPath + "/check-trade-no", method = RequestMethod.POST, produces = {"application/json" })
	@RegisterReflectionForBinding({MMKTRReportBean.class,MMKTRReportRes.class})
	public ResponseEntity<String> checkTradeNo(@RequestBody String in) {
		log.info("Request checkTradeNo(): " + in);
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		
		MMKTRReportRes response = new MMKTRReportRes();
		
		try {
//			mmktrReportService = (MMKTRReportService) SpringApplicationContext.getBean("mmktrReportService");
			MMKTRReportBean request = new Gson().fromJson(in, MMKTRReportBean.class);
			if (BeanUtil.isNotEmpty(request.getTradeNo())) {
				List<MMKTRBean> result = mmktrReportService.checkTradeNo(request.getTradeNo());
				if (result != null && result.size() > 0) {
					response.setMmktrBeanList(result);
					response.setResultCode("20000");
					response.setResultDesc("Success");
					response.setStatus("S");
				}
			}
		} catch (JsonSyntaxException e) {
			response.setResultCode("50000");
			response.setResultDesc("Request JSON format is incorrect");
			response.setStatus("F");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		} catch (ForceTerminateException e) {
			response.setResultCode("50000");
			response.setResultDesc(e.getMessage());
			response.setStatus("F");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDesc("Error check Trade No");
			response.setStatus("F");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info("Response checkTradeNo(): " + new Gson().toJson(response));
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
}
