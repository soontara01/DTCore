package th.co.ais.dt.core.controller.impl.util;

import java.util.HashMap;
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
import th.co.ais.dt.service.core.interfaces.util.SyncDataToCloudService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.controller.dto.DtUtDataToCloudV2Bean;

@RestController
@Slf4j
@AllArgsConstructor
public class SyncDataToCloudImpl {
	final String prefixPath = "api/util/v1";
	
	private final SyncDataToCloudService syncDataToCloudService ;
	
	@RequestMapping(value = prefixPath +"/sync-invoicedtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncInvoiceDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		
		try {
			 msg = syncDataToCloudService.doSyncInvoiceDtl(in ) ;
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-compensation-partner",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncCompensationPartner( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		
		try {
			 msg = syncDataToCloudService.doSyncCompensationPartner(in) ;
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-goodsreturn-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncGoodsreturnDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		
		try {
			 msg = syncDataToCloudService.doSyncGoodsReturnDtl(in) ;
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-goodsreturn-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncGoodsreturnMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		
		try {
			 msg = syncDataToCloudService.doSyncGoodsReturnMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-creditnote",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncCreditNote( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncCreditNote(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-creditnote-item",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncCreditNoteItem( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncCreditNoteItem(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-creditnote-payment",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncCreditNotePayment( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncCreditNotePayment(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-delivery-order-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDeliveryOrderDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDeliveryOrderDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-delivery-order-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDeliveryOrderMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDeliveryOrderMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-receipt-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncReceiptDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncReceiptDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-receipt-method",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncReceiptMethod( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncReceiptMethod(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pm-receipt-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncReceiptMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncReceiptMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-product-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncProductMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncProductMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-price-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncPriceMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPriceMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-price-mst-hist",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncPriceMstHist( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPriceMstHist(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-adjust-stock-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncAdjustStockDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncAdjustStockDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-adjust-stock-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncAdjustStockMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncAdjustStockMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-do-map-ols-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoMapOlsDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDoMapOlsDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-do-map-ols-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoMapOlsMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDoMapOlsMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-ds-pickup-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDsPickUpDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDsPickUpDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-ds-pickup-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDsPickUpMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDsPickUpMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-ds-return-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDsReturnDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDsReturnDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-ds-return-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDsReturnMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDsReturnMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-esim-sap-trans",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncEsimSapTrans( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncEsimSapTrans(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trans-in-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncTransInDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransInDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trans-in-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncTransInMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransInMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trans-out-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncTransOutDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransOutDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trans-out-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncTransOutMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransOutMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trans-sub-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncTransferSubStockMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransferSubStockMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trans-sub-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncTransferSubStockDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransferSubStockDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-invoice-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncInvoiceMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncInvoiceMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-invoice-stf-payroll",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncInvoiceStaffPayroll( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncInvoiceStaffPayroll(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-manual-receipt-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncManualReceiptMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncManualReceiptMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-order-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncOrderDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncOrderDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-order-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncOrderMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncOrderMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pre-booking",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncPreBooking( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPreBooking(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-request-order-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncRequestOrderDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncRequestOrderDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-request-order-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncRequestOrderMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncRequestOrderMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-upload-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncSoUploadMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncSoUploadMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-sale-trans",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncEntrySaleTransactions( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncEntrySaleTransactions(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-omni-commission",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncOmniCommission( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncOmniCommission(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-eztax-doc",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncEztaxDoc( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncEztaxDoc(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-omni-trans-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncOmniTransactionDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncOmniTransactionDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-omni-trans-method",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncOmniTransactionMethod( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncOmniTransactionMethod(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-omni-trans-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncOmniTransactionMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncOmniTransactionMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-redeem-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncRedeemDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncRedeemDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-redeem-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncRedeemMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncRedeemMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-change-vat-receipt-trans",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncChangeVatReceiptTrans( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncChangeVatReceiptTrans(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-do-marketplace-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoMarketPlaceMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDoMarketPlaceMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-do-marketplace-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoMarketPlaceDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDoMarketPlaceDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	//public String doSyncPrivilege(DtUtDataToCloudV2Bean in) ;
	//public String doSyncUssd(DtUtDataToCloudV2Bean in) ;
	//public String doSyncLocMapPlant(DtUtDataToCloudV2Bean in) ;
	//public String doSyncsetupProductCatalogForSale(DtUtDataToCloudV2Bean in) ;
	//public String doSyncdtTpPayadvanceGroupDtl(DtUtDataToCloudV2Bean in) ;
	//public String doSyncdtTpPayadvanceGroupMst(DtUtDataToCloudV2Bean in) ;
	//public String doSynclocMapPlantExt(DtUtDataToCloudV2Bean in) ;
	//public String doSyncinterfaceDplmLog(DtUtDataToCloudV2Bean in) ;
	//public String doSyncsetupVoucherMaster(DtUtDataToCloudV2Bean in) ;
	//public String doSyncVoucherMovement(DtUtDataToCloudV2Bean in) ;
	//public String doSyncVourcherForSeibel(DtUtDataToCloudV2Bean in) ;
	//public String doSyncInterfaceContractLog(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeAirtime(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeArpu(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeBankGroupDtl(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeBankGroupMst(DtUtDataToCloudV2Bean in);
	//public String doSyncTradeChannelCustomer(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeChannel(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeCondition(DtUtDataToCloudV2Bean in);
	//public String doSyncTradeCriteria(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeDiscount(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeFoc(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeFreeGoods(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeInsSmartPay(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeInstallment(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeMst(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradePayment(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradePenaltyInstallment(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradePenaltyInterestDtl(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradePenaltyInterestMst(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradePrivilege(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeProduct(DtUtDataToCloudV2Bean in) ;
	//public String doSyncTradeSaleChannel(DtUtDataToCloudV2Bean in);

	@RequestMapping(value = prefixPath +"/sync-privilege",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncPrivilege( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPrivilege(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-cm-ussd",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncUssd( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncUssd(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-loc-map-plant",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncLocMapPlant( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncLocMapPlant(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-catalog-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncsetupProductCatalogForSale( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncsetupProductCatalogForSale(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-payadvance-group-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncdtTpPayadvanceGroupDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncdtTpPayadvanceGroupDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-payadvance-group-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncdtTpPayadvanceGroupMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncdtTpPayadvanceGroupMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-loc-map-plant-ext",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSynclocMapPlantExt( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSynclocMapPlantExt(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-interface-dplm-log",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncinterfaceDplmLog( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncinterfaceDplmLog(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-voucher-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncsetupVoucherMaster( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncsetupVoucherMaster(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-voucher-movement",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncVoucherMovement( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncVoucherMovement(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-voucher-for-seibel",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncVourcherForSeibel( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncVourcherForSeibel(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-web-interface-contract-log",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncInterfaceContractLog( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncInterfaceContractLog(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-airtime",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeAirtime( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeAirtime(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-trade-arpu",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeArpu( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeArpu(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-bank-group-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeBankGroupDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeBankGroupDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-bank-group-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeBankGroupMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeBankGroupMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-channel-customer",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeChannelCustomer( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeChannelCustomer(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-channel",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeChannel( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeChannel(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-condition",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeCondition( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeCondition(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-criteria",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeCriteria( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeCriteria(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-discount",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeDiscount( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeDiscount(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-foc",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeFoc( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeFoc(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-freegoods",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeFreeGoods( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeFreeGoods(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-ins-smart-pay",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeInsSmartPay( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeInsSmartPay(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-installment",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeInstallment( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeInstallment(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-payment",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradePayment( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradePayment(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-penelty-installment",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradePenaltyInstallment( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradePenaltyInstallment(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-penelty-interest-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradePenaltyInterestDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradePenaltyInterestDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-penelty-interest-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradePenaltyInterestMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradePenaltyInterestMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-privilege",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradePrivilege( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradePrivilege(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-product",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeProduct( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeProduct(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-trade-sale-channel",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncTradeSaleChannel( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTradeSaleChannel(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-delevery-note-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncDeliveryNoteMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDeliveryNoteMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-delevery-note-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncDeliveryNoteDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncDeliveryNoteDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-sap-inven-trans-mat",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncSapInvenTransMat( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncSapInvenTransMat(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-sap-inven-trans",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncSapInvenTrans( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncSapInvenTrans(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-project-type",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncProjectType( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncProjectTypeCloud(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	
	@RequestMapping(value = prefixPath +"/sync-sale-trans-sff",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncSaleTransSff( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncTransSffCloud(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-req-order-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncReqOrderMstCloud( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncReqOrderMstCloud(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-stock-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncDoSyncStockMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncStockMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-pen-penalty-free",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncPenPenaltyFee( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPenPenaltyFee(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pen-contract-config",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncPenContractConfig( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPenContractConfig(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-shift-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> ShiftMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncShiftMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-shift-dtl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncShiftDtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncShiftDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-acom-pickup-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncAcomPickupMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncAcomPickupMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-max-addr-mst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncMaxAddrMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncMaxAddrMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-max-addr-mst-hist",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncMaxAddrMstHist( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncMaxAddrMstHist(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-receipt-reprint-log",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncReceiptReprintLog( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncReceiptReprintLog(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-sap-location-map-gl",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncSapLocationMapGL( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncSapLocationMapGL(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-sale-order-transection",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncSaleOrderTransection( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncSaleOrderTransection(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-request-order-config",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncRequestOrderConfig( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncRequestOrderConfig(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-saleOrder-tranPay",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncSaleOrderTranPay( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncSaleOrderTranPay(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}

	@RequestMapping(value = prefixPath +"/sync-serviceLockhsmst",method = RequestMethod.POST, produces = { "application/json" } )
	@RegisterReflectionForBinding(DtUtDataToCloudV2Bean.class)
	public ResponseEntity<String> syncServiceLockHsMst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncServiceLockHsMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-runningposno",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> syncRunningposno( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncRunningPosNo(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-contractbypass",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> syncContractbypass( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncContractByPass(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-empowerment",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> syncEmpowerment( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncEmpowerment(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-cmbypass",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> syncCmbypass( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncCmByPass(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-upsaleproductmst",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> syncUpsaleproductmst( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncUpsaleProductMst(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-upsaleproductdtl",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> syncUpsaleproductdtl( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncUpsaleProductDtl(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-pen-contract-rule",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> SyncPenContractRule( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncPenContractRule(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-imei-non-ais",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> SyncImeiNonAis( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncImeiNonAis(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}
	
	@RequestMapping(value = prefixPath +"/sync-ars-relate-adp",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> SyncArsRelateAdp( @RequestBody String jsonReq) {
		
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		DtUtDataToCloudV2Bean in = new DtUtDataToCloudV2Bean();
		String msg = "";
		Gson gson = new Gson();
		try {
			in = gson.fromJson(jsonReq, DtUtDataToCloudV2Bean.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
		}
		
		try {
			 msg = syncDataToCloudService.doSyncArsRelateAdp(in);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			log.error(e.getMessage());
			msg = e.getMessage() ;
		}
		
		if(BeanUtil.isNotEmpty(msg)) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+msg+"\",\"developerMessage\":\""+msg+"\"}", httpHeaders, HttpStatus.OK);		

		}else {
			return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		

		}
	}



}
