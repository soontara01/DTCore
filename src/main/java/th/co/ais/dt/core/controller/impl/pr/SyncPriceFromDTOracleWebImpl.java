package th.co.ais.dt.core.controller.impl.pr;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.entity.pr.PriceMst;
import th.co.ais.dt.entity.pr.PriceMstHist;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.MasterValueHist;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.pr.PriceMstService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.SyncPriceDtBean;
import th.co.ais.dt.controller.dto.SyncPriceMst;

@RestController
@Slf4j
@AllArgsConstructor
public class SyncPriceFromDTOracleWebImpl  {
	final String prefixPath = "api/price/v1";
	
	private final PriceMstService priceMstService ;

	@RequestMapping(
			  value = prefixPath +"/sync-price-dt",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SyncPriceDtBean.class)
	public ResponseEntity<String> syncPriceDt(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		 try {
	        	
			 SyncPriceDtBean request = new SyncPriceDtBean();
			 request = gson.fromJson(jsonRequest, SyncPriceDtBean.class);
			 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
			 SimpleDateFormat formatterCre = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.US);
			 
			 List<Long> listproductId = new ArrayList<>();
			 List<PriceMst> listPriceMst = new ArrayList<>();
			 List<PriceMstHist> listPriceMstHist = new ArrayList<>();
			 
			 request.getListProductId().stream().forEach(el->{
				 listproductId.add(Long.valueOf(el));
			 });
			 
			 if(BeanUtil.isNotEmpty(request.getListPriceMst())) {
				 for(SyncPriceMst pricemst :request.getListPriceMst()) {
					PriceMst pr = new PriceMst();
					pr.setCompany(pricemst.getCompany());
					pr.setPriceGroup(pricemst.getPriceGroup());
					pr.setDescription((BeanUtil.isNotEmpty(pricemst.getDescription()) ? pricemst.getDescription() : null   ));
					pr.setProductId(Long.valueOf(pricemst.getProductId()));
					pr.setVatType((BeanUtil.isNotEmpty(pricemst.getVatType()) ? pricemst.getVatType() : null   ));
					pr.setIncVat((BeanUtil.isNotEmpty(pricemst.getIncVat()) ? new BigDecimal(pricemst.getIncVat()) : null   ));
					pr.setExcVat((BeanUtil.isNotEmpty(pricemst.getExcVat()) ? new BigDecimal(pricemst.getExcVat()) : null   ));
					pr.setVatRate((BeanUtil.isNotEmpty(pricemst.getVatRate()) ? pricemst.getVatRate() : null   ));
					pr.setVatAmt((BeanUtil.isNotEmpty(pricemst.getVatAmt()) ? new BigDecimal(pricemst.getVatAmt()) : null   ));
					pr.setPriceDiff((BeanUtil.isNotEmpty(pricemst.getPriceDiff()) ? new BigDecimal(pricemst.getPriceDiff()) : null   ));
					pr.setPriceCredit((BeanUtil.isNotEmpty(pricemst.getPriceCredit()) ? new BigDecimal(pricemst.getPriceCredit()) : null   ));
					pr.setEffectiveDt((BeanUtil.isNotEmpty(pricemst.getEffectiveDt()) ? formatter.parse(pricemst.getEffectiveDt()) : null   ));
					pr.setExpireDt((BeanUtil.isNotEmpty(pricemst.getExpireDt()) ? formatter.parse(pricemst.getExpireDt()) : null   ));
					pr.setActiveFlg("Y");
					pr.setPaymentMethod(pricemst.getPaymentMethod());
					
					
					MasterValue masterValue = new MasterValue() ;
					masterValue.setCreated(formatterCre.parse(pricemst.getCreated())) ;
					masterValue.setCreatedBy(pricemst.getCreatedBy());
					masterValue.setLastUpd(formatterCre.parse(pricemst.getLastUpd()));
					masterValue.setLastUpdBy(pricemst.getLastUpdBy());
					pr.setCreateValue(masterValue) ;
					listPriceMst.add(pr);
				 }
			 }
			 
			 if(BeanUtil.isNotEmpty(request.getListPriceMstHist())) {
				 for(SyncPriceMst pricemst :request.getListPriceMstHist()) {
					PriceMstHist pr = new PriceMstHist();
					pr.setCompany(pricemst.getCompany());
					pr.setPriceGroup(pricemst.getPriceGroup());
					pr.setDescription((BeanUtil.isNotEmpty(pricemst.getDescription()) ? pricemst.getDescription() : null   ));
					pr.setProductId(Long.valueOf(pricemst.getProductId()));
					pr.setVatType((BeanUtil.isNotEmpty(pricemst.getVatType()) ? pricemst.getVatType() : null   ));
					pr.setIncVat((BeanUtil.isNotEmpty(pricemst.getIncVat()) ? new BigDecimal(pricemst.getIncVat()) : null   ));
					pr.setExcVat((BeanUtil.isNotEmpty(pricemst.getExcVat()) ? new BigDecimal(pricemst.getExcVat()) : null   ));
					pr.setVatRate((BeanUtil.isNotEmpty(pricemst.getVatRate()) ? pricemst.getVatRate() : null   ));
					pr.setVatAmt((BeanUtil.isNotEmpty(pricemst.getVatAmt()) ? new BigDecimal(pricemst.getVatAmt()) : null   ));
					pr.setPriceDiff((BeanUtil.isNotEmpty(pricemst.getPriceDiff()) ? new BigDecimal(pricemst.getPriceDiff()) : null   ));
					pr.setPriceCredit((BeanUtil.isNotEmpty(pricemst.getPriceCredit()) ? new BigDecimal(pricemst.getPriceCredit()) : null   ));
					pr.setEffectiveDt((BeanUtil.isNotEmpty(pricemst.getEffectiveDt()) ? formatter.parse(pricemst.getEffectiveDt()) : null   ));
					pr.setExpireDt((BeanUtil.isNotEmpty(pricemst.getExpireDt()) ? formatter.parse(pricemst.getExpireDt()) : null   ));
					pr.setActiveFlg("N");
					pr.setPaymentMethod(pricemst.getPaymentMethod());
					pr.setSequence((BeanUtil.isNotEmpty(pricemst.getSequence()) ? Long.valueOf(pricemst.getSequence()) : null   ));
					
					MasterValueHist masterValue = new MasterValueHist() ;
					masterValue.setCreated(formatterCre.parse(pricemst.getCreated())) ;
					masterValue.setCreatedBy(pricemst.getCreatedBy());
					pr.setCreateValue(masterValue) ;
					listPriceMstHist.add(pr);
				 }
			 }

			 
			 priceMstService.deletePriceMstByProductId(listproductId);
			 priceMstService.deletePriceMstHistByProductId(listproductId);
			 
			 if(BeanUtil.isNotEmpty(listPriceMst)) {
				 priceMstService.insertPriceMstList(listPriceMst);
			 }
			 
             if(BeanUtil.isNotEmpty(listPriceMstHist)) {
            	 priceMstService.insertPriceMstHistList(listPriceMstHist);
			 }
			 
	        	
			} catch(Exception e) {
				log.error(e.getMessage());
				log.info(e.getMessage());
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
		    return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);
	}



}
