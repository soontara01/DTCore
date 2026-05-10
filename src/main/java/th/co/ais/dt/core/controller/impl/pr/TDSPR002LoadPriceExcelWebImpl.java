package th.co.ais.dt.core.controller.impl.pr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.pr.ITDSPR002LoadPriceExcelService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.TDSPR002LoadPriceExcelBean;
import th.co.ais.dt.exception.ForceTerminateException;

@RestController
@Slf4j
@AllArgsConstructor
public class TDSPR002LoadPriceExcelWebImpl  {
	final String prefixPath = "api/price/v1/TDSPR002";
	
	private final ITDSPR002LoadPriceExcelService iTDSPR002LoadPriceExcelService ;

	@RequestMapping(
			  value = prefixPath +"/query-criteria",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR002LoadPriceExcelBean.class)
	public ResponseEntity<String> queryCriteria(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		TDSPR002LoadPriceExcelBean request = new TDSPR002LoadPriceExcelBean();
		TDSPR002LoadPriceExcelBean response = new TDSPR002LoadPriceExcelBean();
		Gson gson = new Gson();
		try {
			request =gson.fromJson(jsonRequest, TDSPR002LoadPriceExcelBean.class);
			if(BeanUtil.isNotEmpty(request.getAction())) {
				switch (request.getAction()) {
					case "QUERY_CRITERIA":
						response = iTDSPR002LoadPriceExcelService.queryCriteriaForTDSPR002(request);
						break;
					default:
						response.setResultCode("50000");
						response.setResultDescription("Input Action incorrect, Please try again [EX. QUERY_CRITERIA]");
						response.setDeveloperMessage("Input Action incorrect, Please try again [EX. QUERY_CRITERIA]");
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
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(
			  value = prefixPath +"/validate-upload",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR002LoadPriceExcelBean.class)
	public ResponseEntity<String> validateUpload(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR002LoadPriceExcelBean request = new TDSPR002LoadPriceExcelBean();
		TDSPR002LoadPriceExcelBean response = new TDSPR002LoadPriceExcelBean();
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR002LoadPriceExcelBean.class);
			response = iTDSPR002LoadPriceExcelService.validateUpload(request);
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(
			  value = prefixPath +"/save-upload",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(TDSPR002LoadPriceExcelBean.class)
	public ResponseEntity<String> saveUpload(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		TDSPR002LoadPriceExcelBean request = new TDSPR002LoadPriceExcelBean();
		TDSPR002LoadPriceExcelBean response = new TDSPR002LoadPriceExcelBean();
		List<String> listKey = null ;
		boolean insertkeyFlg = false ;
		Gson gson = new Gson();
		try {
			request = gson.fromJson(jsonRequest, TDSPR002LoadPriceExcelBean.class);
			//insert keytrans
			
			listKey = request.getPriceOkList().stream().map(el->{
				return "SETUP_PRICE_SAVE_MATCODE=" + el.getCompany() + "_"+ el.getMatCode()+ "_"+ el.getPriceGroup()+ "_" + el.getPaymentMethod();
			}).distinct().collect(Collectors.toList());
			
			iTDSPR002LoadPriceExcelService.insertCheckDupTrans(listKey);
			insertkeyFlg = true ;
			
			// not validate request because this request have to call api validate-upload first
			 response = iTDSPR002LoadPriceExcelService.saveUpdate(request.getPriceOkList(),request.getUserId());
		} catch (JsonParseException je) {
			response.setResultCode("50000");
			response.setResultDescription("JSON incorrect format");
			response.setDeveloperMessage("JSON incorrect format");
		} catch(ForceTerminateException f) {
			response.setResultCode("50000");
			response.setResultDescription(f.getMessage());
			response.setDeveloperMessage(f.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setDeveloperMessage(e.getMessage());
		} finally {
			if(insertkeyFlg) {
				try {
					iTDSPR002LoadPriceExcelService.deleteCheckDupTrans(listKey);
				}catch (Exception e) {
					log.error(e.getMessage());
				}
				
			}
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}

}
