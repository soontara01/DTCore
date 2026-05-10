package th.co.ais.dt.core.controller.impl.pr;

import java.util.Arrays;
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
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.pr.IPmCompanyService;
import th.co.ais.dt.service.core.interfaces.pr.SetupPriceService;
import th.co.ais.dt.controller.dto.LovMasterRequest;
import th.co.ais.dt.controller.dto.LovMasterResponse;
import th.co.ais.dt.controller.dto.PmCompanyOutBean;
import th.co.ais.dt.controller.dto.ProductMstIRes;
import th.co.ais.dt.controller.dto.SetupPriceBean;

@RestController
@Slf4j
@AllArgsConstructor
public class SetupPriceWebImpl {
	final String prefixPath = "api/setprice/v1";
	
	private final IPmCompanyService pmCompanyService ;
	
	private final SetupPriceService setupPriceService ;
	
	@RequestMapping(value = prefixPath +"/query-company",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(PmCompanyOutBean.class)
	public ResponseEntity<String> queryCompany(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			Gson gson = new Gson();
			PmCompanyOutBean  res = new PmCompanyOutBean();
			res.setResultCode("20000");
			res.setResultDescription("Success");
			res.setListCompany(pmCompanyService.getAllCompany());
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		}catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/query-price-group",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({LovMasterRequest.class,LovMasterResponse.class})
	public ResponseEntity<String> queryPriceGroup(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			       
			LovMasterRequest  input = new LovMasterRequest();
			LovMasterResponse res = new LovMasterResponse();
			Gson gson = new Gson();
			try {
				input = gson.fromJson(in, LovMasterRequest.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);

			}
			res.setResultCode("20000");
			res.setResultDescription("Success");
			res.setListLovMaster(setupPriceService.listLovMastersForPriceCorp(input.getLovType(),input.getLovSubType(),input.getActiveFlg()));
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		}catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/list-productMsts-by-criteria",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding({SetupPriceBean.class,ProductMstIRes.class})
	public ResponseEntity<String> queryPopupPerform(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {
			SetupPriceBean input = new SetupPriceBean();
			ProductMstIRes res = new ProductMstIRes();
			Gson gson = new Gson();
			try {
				input = gson.fromJson(in, SetupPriceBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
			}
			List<String> listCompany = Arrays.asList("AWN", "AIS", "AMP","WDS");
			String productType = input.getProductType();
			String productSubtype = input.getProductSubType();
			String brand = input.getBrand();
			String model = input.getModel();
			String feature =input.getFeature();
			String network = input.getNetwork();
			String region =input.getRegion();
			String subRegion =input.getSubRegion();
			String capacity =input.getCapacity();
			String color =input.getColor();
			String faceValue =input.getFaceValue();
			String matCode=input.getMatCode();
			String tdmDescription = input.getTdmDescription();
			String grade = input.getGrade();
			res.setProductMasterList(setupPriceService.listProductMstsByCriteria(listCompany, null, productType, productSubtype, brand, model, feature, network, region, subRegion, capacity, color, faceValue, "%" + matCode + "%", null, null, null, tdmDescription, null, null, grade));
			res.setResultCode("20000");
			res.setResultDescription("Success");
			res.setDeveloperMessage("Success");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			//listProductDb = mtServiceFacade.listProductMstsByCriteria(defaultCompany, null, productType, productSubType, brand, model, feature, network, region, subRegion, capacity, color, faceValue, "%" + matCode + "%", null, null, null, tdmDescription, null, null, grade);
		}catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
