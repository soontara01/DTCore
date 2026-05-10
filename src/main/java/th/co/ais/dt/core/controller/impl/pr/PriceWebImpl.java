package th.co.ais.dt.core.controller.impl.pr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.service.core.dto.GroupPriceBean;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.pr.PriceMstService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.service.core.interfaces.util.ProductMstService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.controller.dto.ListGroupPriceResponse;
import th.co.ais.dt.controller.dto.QueryPriceBean;
import th.co.ais.dt.controller.dto.StdPriceBean;

@RestController
@Slf4j
@AllArgsConstructor
public class PriceWebImpl {
	final String prefixPath = "api/price/v1";
	private static final String SUCCESS = "20000";
	private static final String ERROR = "50000";
	
	private final PriceMstService priceMstService ;
	
	private final ProductMstService productMstService ;
	
	private final LovMasterService lovMasterService ;
	
	@RequestMapping(value = prefixPath +"/query-price-product",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(QueryPriceBean.class)
	public ResponseEntity<String> queryPriceProduct(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {

			QueryPriceBean res = new QueryPriceBean();

			Gson gson = new Gson();
			QueryPriceBean input = new QueryPriceBean();
			try {
				input = gson.fromJson(in, QueryPriceBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
			}
			
			//boolean dataIncorrect = false;
			
			if(input != null && 
			   BeanUtil.isNotEmpty(input.getPriceDate()) && 
			   BeanUtil.isNotEmpty(input.getPriceExpireDate()) && 
			   BeanUtil.isNotEmpty(input.getProductId()) &&
			   BeanUtil.isNotEmpty(input.getGroupPrice())){
				
			   callPrice(input, res, gson) ;
				
			   return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
						
			}
			else if(input != null && 
					   BeanUtil.isNotEmpty(input.getPriceDate()) && 
					   BeanUtil.isNotEmpty(input.getPriceExpireDate()) && 
					   BeanUtil.isNotEmpty(input.getMatCode()) &&
					   BeanUtil.isNotEmpty(input.getCompany()) &&
					   BeanUtil.isNotEmpty(input.getGroupPrice())) {
				callPrice(input, res, gson) ;
				 return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
				
			}
			else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);
			}


		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	private  void callPrice (QueryPriceBean input , QueryPriceBean res , Gson gson) {
		//QueryPriceBean res = new QueryPriceBean();
		List<StdPriceBean> result = null ;
		try {
			SimpleDateFormat forTmpprice = new SimpleDateFormat("ddMMyyyy HH:mm", new Locale("en", "US"));
			SimpleDateFormat forTmp = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("en", "US"));
			
			Date DPriceDate = forTmp.parse(input.getPriceDate());
			Date DPriceExpireDate = forTmp.parse(input.getPriceExpireDate());
			
			String PriceDate = forTmpprice.format(DPriceDate);
			String PriceExpireDate = forTmpprice.format(DPriceExpireDate);
			
			if(BeanUtil.isNotEmpty(input.getBackwardFlg()) && input.getBackwardFlg().equals("Y")) {
				String pId = input.getProductId() ;
				if(BeanUtil.isEmpty(pId)) {
					ProductMst productMst = productMstService.getProductMstByUnique(input.getCompany(), input.getMatCode()) ;
					if(productMst != null) {
						pId = String.valueOf(productMst.getProductId());
					}else {
						pId = "";
					}
				}
				result = priceMstService.listSalePriceBackwardByCriteria(input.getGroupPrice(), pId , PriceDate, PriceExpireDate);
			}else {
				String pId = input.getProductId() ;
				if(BeanUtil.isEmpty(pId)) {
					ProductMst productMst = productMstService.getProductMstByUnique(input.getCompany(), input.getMatCode()) ;
					if(productMst != null) {
						pId = String.valueOf(productMst.getProductId());
					}else {
						pId = "";
					}
				}
				result = priceMstService.getListPriceByCriteria(input.getGroupPrice(), pId, PriceDate);
			}
			
			res.setDeveloperMessage("Success");
			res.setResultCode("20000");
			res.setResultDescription("Success");
			if(BeanUtil.isEmpty(result)) {
				result = new ArrayList<>();
			}
			res.setPriceList(result);
		}catch (Exception e) { 
			res.setDeveloperMessage(e.getMessage());
			res.setResultCode("50000");
			res.setResultDescription(e.getMessage());
		}
		
		//return res ;
		
	}
	
	@RequestMapping(value = prefixPath +"/getGroupPrice",method = RequestMethod.GET, produces = { "application/json" })
	@RegisterReflectionForBinding(ListGroupPriceResponse.class)
	public ResponseEntity<String> getListGroupPrice(@QueryParam("locationCode") String locationCode,@QueryParam("saleChannel") String saleChannel) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		try {
			ListGroupPriceResponse response = null;
			Gson gson = new Gson();
			if(BeanUtil.isEmpty(locationCode) || BeanUtil.isEmpty(saleChannel)) {
				response = new ListGroupPriceResponse(ERROR, "Please check locationCode or saleChannel is Empty", "Please check locationCode or saleChannel is Empty");
			}else {
				try { 
					List<GroupPriceBean> listGroupPrice = new ArrayList<>();
					List<LovMaster> listLov =lovMasterService.listLovMasterByCriteria("CHANEL_GROUP_PRICE_NEW", "CHANEL_GROUP_PRICE_NEW", saleChannel, null, "Y");
					
					if(BeanUtil.isNotEmpty(listLov)) {
						for(LovMaster el : listLov) {
							GroupPriceBean groupPrice = new GroupPriceBean();
							groupPrice.setCompany("AWN");
							groupPrice.setPriceGroupCode(el.getLovVal());
							groupPrice.setPriceGroupName(el.getDescription());
							listGroupPrice.add(groupPrice);
						}
						
						for(LovMaster el : listLov) {
							GroupPriceBean groupPrice = new GroupPriceBean();
							groupPrice.setCompany("WDS");
							groupPrice.setPriceGroupCode(el.getLovVal());
							groupPrice.setPriceGroupName(el.getDescription());
							listGroupPrice.add(groupPrice);
						}
					}
					
//					//List<GroupPriceBean> listGroupPrice = priceMstService.getListGroupPrice(locationCode, saleChannel);
//					GroupPriceBean groupPriceBeanBUAWN = new GroupPriceBean();
//					groupPriceBeanBUAWN.setCompany("AWN");
//					groupPriceBeanBUAWN.setPriceGroupCode("BU");
//					groupPriceBeanBUAWN.setPriceGroupName("Corporate Price");
//					GroupPriceBean groupPriceBeanEUPAWN = new GroupPriceBean();
//					groupPriceBeanEUPAWN.setCompany("AWN");
//					groupPriceBeanEUPAWN.setPriceGroupCode("EUP");
//					groupPriceBeanEUPAWN.setPriceGroupName("End User Price");
//					GroupPriceBean groupPriceBeanSTAFFAWN = new GroupPriceBean();
//					groupPriceBeanSTAFFAWN.setCompany("AWN");
//					groupPriceBeanSTAFFAWN.setPriceGroupCode("STAFF");
//					groupPriceBeanSTAFFAWN.setPriceGroupName("Staff Price");
//					
//					GroupPriceBean groupPriceBeanBUWDS = new GroupPriceBean();
//					groupPriceBeanBUWDS.setCompany("WDS");
//					groupPriceBeanBUWDS.setPriceGroupCode("BU");
//					groupPriceBeanBUWDS.setPriceGroupName("Corporate Price");
//					GroupPriceBean groupPriceBeanEUPWDS = new GroupPriceBean();
//					groupPriceBeanEUPWDS.setCompany("WDS");
//					groupPriceBeanEUPWDS.setPriceGroupCode("EUP");
//					groupPriceBeanEUPWDS.setPriceGroupName("End User Price");
//					GroupPriceBean groupPriceBeanSTAFFWDS = new GroupPriceBean();
//					groupPriceBeanSTAFFWDS.setCompany("WDS");
//					groupPriceBeanSTAFFWDS.setPriceGroupCode("STAFF");
//					groupPriceBeanSTAFFWDS.setPriceGroupName("Staff Price");
//	
//					
//					
//					listGroupPrice.add(groupPriceBeanBUAWN);
//					listGroupPrice.add(groupPriceBeanEUPAWN);
//					listGroupPrice.add(groupPriceBeanSTAFFAWN);
//					listGroupPrice.add(groupPriceBeanBUWDS);
//					listGroupPrice.add(groupPriceBeanEUPWDS);
//					listGroupPrice.add(groupPriceBeanSTAFFWDS);
					if(BeanUtil.isEmpty(listGroupPrice)) {
						response = new ListGroupPriceResponse(SUCCESS, "success", "success",new ArrayList<GroupPriceBean>());
					}else {
						response = new ListGroupPriceResponse(SUCCESS, "success", "success",listGroupPrice);
					}
				}catch (Exception e) {
					response = new ListGroupPriceResponse(ERROR, e.getMessage(), e.getMessage());
				}	
			}
			
			return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

		}catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
		
	}
	

}
