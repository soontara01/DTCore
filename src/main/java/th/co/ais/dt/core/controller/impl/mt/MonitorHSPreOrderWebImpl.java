package th.co.ais.dt.core.controller.impl.mt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.dto.User;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.MonitorHSPreOrderService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.TDMDataUtility;
import th.co.ais.dt.controller.dto.MonitorHSPreOrderBean;

@RestController
@Slf4j
@AllArgsConstructor
public class MonitorHSPreOrderWebImpl {
	final String prefixPath = "api/master-config/v1";
	
	private static final String RESULT_SUCCESS = "Success";
	private static final String RESULT_CODE_SUCCESS = "20000";
	private static final String RESULT_CODE_ERROR = "50000";
	
	private final MonitorHSPreOrderService monitorHSPreOrderService ;
	
	@RequestMapping(value = prefixPath +"/save-data-monitor-hs-pre-order",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MonitorHSPreOrderBean.class)
	public ResponseEntity<String> saveDataMonitorHSPreOrder(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		MonitorHSPreOrderBean request = new MonitorHSPreOrderBean();
		MonitorHSPreOrderBean response = new MonitorHSPreOrderBean();
		MonitorHSPreOrderBean error = new MonitorHSPreOrderBean();
		
		List<MonitorHSPreOrderBean> listMonitorHSPreOrder = new ArrayList<MonitorHSPreOrderBean>();
		int countErrror = 0;
		
		try {		
			request = gson.fromJson(jsonRequest, MonitorHSPreOrderBean.class);
			
		}catch (Exception e) {
			response.setResultCode(RESULT_CODE_SUCCESS);
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
			
		}
		
		User user = new User(request.getUserId(), null, null);
		
		try {
			boolean validData = true;
			
			for (MonitorHSPreOrderBean validBean : request.getListMonitorHSPreOrder()) {
				error = validateBeforeSaveData(validBean, countErrror);
				if(BeanUtil.isEmpty(error.getErrorMsg())) {
					listMonitorHSPreOrder.add(validBean);
				}else {
					validData = false;
					countErrror += 1;
					listMonitorHSPreOrder.add(error);
				}
			}
			
			if(validData) {
				monitorHSPreOrderService.queryCheckDataBeforeInsertMonitorHSPreOrder(listMonitorHSPreOrder);
				ProcessResult result = monitorHSPreOrderService.saveDataMonitorHSPreOrder(listMonitorHSPreOrder, user);
				if(result.isSuccess()) {
					response.setError(String.valueOf(countErrror));
					response.setTotal(String.valueOf(request.getListMonitorHSPreOrder().size()));
					response.setResultCode(RESULT_CODE_SUCCESS);
					response.setResultDescription(RESULT_SUCCESS);
					response.setListMonitorHSPreOrder(listMonitorHSPreOrder);
				}else {
					response.setError(String.valueOf(countErrror));
					response.setTotal(String.valueOf(request.getListMonitorHSPreOrder().size()));
					response.setResultCode(RESULT_CODE_ERROR);
					response.setResultDescription(result.getMessage());
					response.setListMonitorHSPreOrder(listMonitorHSPreOrder);
				}
				
			} else {
				response.setError(String.valueOf(countErrror));
				response.setTotal(String.valueOf(request.getListMonitorHSPreOrder().size()));
				response.setResultCode(RESULT_CODE_SUCCESS);
				response.setResultDescription(RESULT_SUCCESS);
				response.setListMonitorHSPreOrder(listMonitorHSPreOrder);
			}
				
		}catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode(RESULT_CODE_ERROR);
			response.setResultDescription(e.getMessage());
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	private MonitorHSPreOrderBean validateBeforeSaveData(MonitorHSPreOrderBean request, int countErrror) {
		MonitorHSPreOrderBean err = new MonitorHSPreOrderBean();
		BeanUtils.copyProperties(request, err);
		String error = "";
		
		if (BeanUtil.isEmpty(request.getDemandForecastNo())) {
			error += "demandForecastNo is null. ";
		}
		
		if (BeanUtil.isEmpty(request.getPeriod())) {
			BeanUtils.copyProperties(request, err);
			error += "period is null. ";
		} else {
			String dateString = request.getPeriod();
			String[] resultDate = dateString.split("-", 0);
			
			try {
				TDMDataUtility.convertStringToDateTimestamp(resultDate[0].trim());
				TDMDataUtility.convertStringToDateTimestamp(resultDate[1].trim());
			} catch (Exception e) {
				error += "period incorrect date format." ;
			}	
		}
		
		if (BeanUtil.isEmpty(request.getCustomerCode())) {
			BeanUtils.copyProperties(request, err);
			error += "customerCode is null.";	
		}
		
		if (BeanUtil.isNotEmpty(request.getAllocateDate())) {
			try {
				TDMDataUtility.convertStringToDate(request.getAllocateDate());
			} catch (Exception ex) {
				error += "allocateDate incorrect date format.";
			}
		}
		
		err.setErrorMsg(error);
		return err;
	}
	
	@RequestMapping(value = prefixPath +"/query-data-monitor-hs-pre-order",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MonitorHSPreOrderBean.class)
	public ResponseEntity<String> queryDataUploadMonitorHSPreOrder(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		MonitorHSPreOrderBean request = new MonitorHSPreOrderBean();
		MonitorHSPreOrderBean response = new MonitorHSPreOrderBean();
		
		try {
			request = gson.fromJson(jsonRequest, MonitorHSPreOrderBean.class);
			
		}catch (Exception e) {
			response.setResultCode(RESULT_CODE_SUCCESS);
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
			
		}
		
		ProcessResult listResult = monitorHSPreOrderService.queryDataUploadMonitorHSPreOrder(request);
		if (listResult.isSuccess()) {
			response.setResultCode("20000");
			response.setResultDescription(listResult.getMessage());
			response.setListMonitorHSPreOrder((List<MonitorHSPreOrderBean>) listResult.getResultObject());

		} else {
			response.setResultCode("50000");
			response.setResultDescription(listResult.getMessage());
		}

		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	@RequestMapping(value = prefixPath +"/query-report-hs-pre-order",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MonitorHSPreOrderBean.class)
	public ResponseEntity<String> queryDataReportHSPreOrder(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		MonitorHSPreOrderBean request = new MonitorHSPreOrderBean();
		MonitorHSPreOrderBean response = new MonitorHSPreOrderBean();
		try {

			try {
				request = gson.fromJson(jsonRequest, MonitorHSPreOrderBean.class);
			} catch (Exception e) {
				log.info(e.getMessage());
				response.setResultCode("50000");
				response.setResultDescription("Request JSON format is incorrect");
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
			}
			if (BeanUtil.isNotEmpty(request)) {

				ProcessResult result = monitorHSPreOrderService.queryDataReportHSPreOrder(request);
				if (result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setListMonitorHSPreOrder((List<MonitorHSPreOrderBean>) result.getResultObject());

				} else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
				}
			}

		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
		}

		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	@RequestMapping(value = prefixPath +"/query-result-upload-monitor-hs-pre-order",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MonitorHSPreOrderBean.class)
	public ResponseEntity<String> queryResultUploadMonitorHSPreOrder(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		MonitorHSPreOrderBean request = new MonitorHSPreOrderBean();
		MonitorHSPreOrderBean response = new MonitorHSPreOrderBean();
		try {
			request = gson.fromJson(jsonRequest, MonitorHSPreOrderBean.class);
			
		}catch (Exception e) {
			response.setResultCode(RESULT_CODE_SUCCESS);
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
			
		}
		
		ProcessResult listResult = monitorHSPreOrderService.queryResultUploadMonitorHSPreOrder(request);
		if (listResult.isSuccess()) {
			response.setResultCode("20000");
			response.setResultDescription(listResult.getMessage());
			response.setListMonitorHSPreOrder((List<MonitorHSPreOrderBean>) listResult.getResultObject());

		} else {
			response.setResultCode("50000");
			response.setResultDescription(listResult.getMessage());
		}

		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	@RequestMapping(value = prefixPath +"/query-product-hs-pre-order",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(MonitorHSPreOrderBean.class)
	public ResponseEntity<String> queryProductHSPreOrder(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		MonitorHSPreOrderBean request = new MonitorHSPreOrderBean();
		MonitorHSPreOrderBean response = new MonitorHSPreOrderBean();
		try {
			request = gson.fromJson(jsonRequest, MonitorHSPreOrderBean.class);
			
		}catch (Exception e) {
			response.setResultCode(RESULT_CODE_SUCCESS);
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
		}
		
		ProcessResult listResult = monitorHSPreOrderService.queryProductHSPreOrder(request);
		if (listResult.isSuccess()) {
			response.setResultCode("20000");
			response.setResultDescription(listResult.getMessage());
			response.setListMonitorHSPreOrder((List<MonitorHSPreOrderBean>) listResult.getResultObject());

		} else {
			response.setResultCode("50000");
			response.setResultDescription(listResult.getMessage());
		}

		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}

}
