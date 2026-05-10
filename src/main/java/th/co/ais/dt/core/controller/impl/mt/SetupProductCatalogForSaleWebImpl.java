package th.co.ais.dt.core.controller.impl.mt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.ISetupProductCatalogForSaleService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.CatalogMstBean;

@RestController
@Slf4j
@AllArgsConstructor
public class SetupProductCatalogForSaleWebImpl {
	final String prefixPath = "api/master-config/v1";
	
	private final ISetupProductCatalogForSaleService setupProductCatalogForSaleService ;
	
	@RequestMapping(value = prefixPath +"/query-catalog-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(CatalogMstBean.class)
	public ResponseEntity<String> queryCatalogMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		CatalogMstBean input = new CatalogMstBean();
		CatalogMstBean response = new CatalogMstBean();
		
		try {
			
			input = gson.fromJson(jsonRequest, CatalogMstBean.class);
			
			if(BeanUtil.isNotEmpty(input)) {

				ProcessResult result = setupProductCatalogForSaleService.queryCatalogMst(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
				}
			}
				
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}

		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = prefixPath +"/edit-catalog-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(CatalogMstBean.class)
	public ResponseEntity<String> editCatalogMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		CatalogMstBean input = new CatalogMstBean();
		CatalogMstBean response = new CatalogMstBean();
		
		try {
			input = gson.fromJson(jsonRequest, CatalogMstBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = setupProductCatalogForSaleService.editCatalogMst(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = prefixPath +"/delete-catalog-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(CatalogMstBean.class)
	public ResponseEntity<String> deleteCatalogMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		CatalogMstBean input = new CatalogMstBean();
		CatalogMstBean response = new CatalogMstBean();
		
		try {
			input = gson.fromJson(jsonRequest, CatalogMstBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = setupProductCatalogForSaleService.deleteCatalogMst(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = prefixPath +"/upload-catalog-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(CatalogMstBean.class)
	public ResponseEntity<String> uploadCatalogMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		CatalogMstBean input = new CatalogMstBean();
		CatalogMstBean response = new CatalogMstBean();
		
		try {
			input = gson.fromJson(jsonRequest, CatalogMstBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = setupProductCatalogForSaleService.uploadCatalogMst(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}
		
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = prefixPath +"/validate-upload-catalog-mst",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(CatalogMstBean.class)
	public ResponseEntity<String> validateUploadCatalogMst(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		Gson gson = new Gson();
		CatalogMstBean input = new CatalogMstBean();
		CatalogMstBean response = new CatalogMstBean();
		
		try {
			input = gson.fromJson(jsonRequest, CatalogMstBean.class);
			if(BeanUtil.isNotEmpty(input)) {
				ProcessResult result = setupProductCatalogForSaleService.validateUploadCatalogMst(input);
				if(result.isSuccess()) {
					response.setResultCode("20000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());

				}else {
					response.setResultCode("50000");
					response.setResultDescription(result.getMessage());
					response.setDeveloperMessage(result.getMessage());
					response.setCatalogMstList((List<CatalogMstBean>) result.getResultObject());
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription("Request JSON format is incorrect");
			response.setDeveloperMessage("Request JSON format is incorrect");
		}

		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
	}
}
