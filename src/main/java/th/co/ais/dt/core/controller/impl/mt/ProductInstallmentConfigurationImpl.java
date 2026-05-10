//package th.co.ais.dt.core.controller.impl.mt;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.gson.Gson;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import th.co.ais.dt.entity.util.ProductInstallmentMst;
//import th.co.ais.dt.service.core.dto.ProcessResult;
//import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
//import th.co.ais.dt.service.core.interfaces.mt.IProductInstallmentService;
//import th.co.ais.dt.util.BeanUtil;
////import th.co.ais.dt.util.JBossBackEndProperties;
//import th.co.ais.dt.controller.dto.ProductInstallmentBean;
//
//@RestController
//@Slf4j
//@AllArgsConstructor
//public class ProductInstallmentConfigurationImpl {
//	final String prefixPath = "api/installmentProduct/v1";
//	
//	private final IProductInstallmentService productInstallmentService ;
//	
//	@RequestMapping(value = prefixPath +"/insert-installment-product",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(ProductInstallmentBean.class)
//	public ResponseEntity<String> insertProductInstallment(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//
//		ProductInstallmentBean responseData = new ProductInstallmentBean();
//		Gson gson = new Gson();
//		ProductInstallmentBean request = new ProductInstallmentBean();
//		//IProductInstallmentService productInstallmentService = (IProductInstallmentService) SpringApplicationContext.getBean("productInstallmentService");
//		request = gson.fromJson(jsonRequest, ProductInstallmentBean.class);
//		
//		if(BeanUtil.isNotEmpty(request) && request.getListProductInstallmentBean().size() > 0) {
//			List<ProductInstallmentBean> res = new ArrayList<ProductInstallmentBean>();
//			for(ProductInstallmentBean data : request.getListProductInstallmentBean()) {
//				try {
//					
//					ProcessResult validate = productInstallmentService.validateData(data);
//					if(validate.isSuccess()) {	
//						ProcessResult result = productInstallmentService.insertProductInstallment(data, request.getUserId());
//						if(result.isSuccess()) {
//							data.setResultCode("20000");
//							data.setResultDescription("Save Completed");
//							data.setStatus("S");
//						}else {
//							data.setResultCode("50000");
//							data.setResultDescription(result.getMessage());
//							data.setStatus("F");
//						}
//					}else {
//						data.setResultCode("50000");
//						data.setResultDescription(validate.getMessage());
//						data.setStatus("F");
//					}
//					
//				} catch (Exception e) {
//					log.info(e.getMessage());
//					data.setResultCode("50000");
//					data.setResultDescription(e.getMessage());
//					data.setStatus("F");
//				}
//				res.add(data);
//			}
//			responseData.setListProductInstallmentBean(res);
//		}
//		return new ResponseEntity<String>(gson.toJson(responseData), httpHeaders, HttpStatus.OK);
//	
//	}
//	
//	@RequestMapping(value = prefixPath +"/update-installment-product",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(ProductInstallmentBean.class)
//	public ResponseEntity<String> updateProductInstallment(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//
//		ProductInstallmentBean response = new ProductInstallmentBean();
//		Gson gson = new Gson();
//		try {
//			ProductInstallmentBean request = new ProductInstallmentBean();
//			request = gson.fromJson(jsonRequest, ProductInstallmentBean.class);
//			
//			if(BeanUtil.isNotEmpty(request)) {
//				ProcessResult result = productInstallmentService.updateProductInstallment(request);
//				if(result.isSuccess()) {
//					response.setResultCode("20000");
//					response.setResultDescription("Success");
//					response.setStatus("S");
//				}else {
//					response.setResultCode("50000");
//					response.setResultDescription(result.getMessage());
//					response.setStatus("F");
//				}
//			}
//		} catch (Exception e) {
//			log.info(e.getMessage());
//			response.setResultCode("50000");
//			response.setResultDescription(e.getMessage());
//			response.setStatus("F");
//		}
//		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
//	
//	}
//	
//	@RequestMapping(value = prefixPath +"/query-installment-product",method = RequestMethod.POST, produces = { "application/json" })
//	@RegisterReflectionForBinding(ProductInstallmentBean.class)
//	public ResponseEntity<String> queryProductInstallment(@RequestBody String jsonRequest) {
//		final HttpHeaders httpHeaders= new HttpHeaders();
//		Map<String,String> properties = new HashMap<>();
//		properties.put("Content-Type", "application/json;charset=utf-8");
//		httpHeaders.setAll(properties);
//
//		ProductInstallmentBean response = new ProductInstallmentBean();
//		Gson gson = new Gson();
//		try {
//			ProductInstallmentBean request = new ProductInstallmentBean();
//			List<ProductInstallmentMst> listProductInstallment = new ArrayList<ProductInstallmentMst>();
//			request = gson.fromJson(jsonRequest, ProductInstallmentBean.class);
//			
//			if(BeanUtil.isNotEmpty(request)) {
//				listProductInstallment = productInstallmentService.queryProductInstallment(request);
//				if(listProductInstallment != null && listProductInstallment.size() > 0) {
//					response.setResultCode("20000");
//					response.setResultDescription("Success");
//					response.setStatus("S");
//					response.setListProductInstallment(listProductInstallment);
//				}else {
//					response.setResultCode("50000");
//					response.setResultDescription("Data not found");
//					response.setStatus("F");
//				}
//			}
//		} catch (Exception e) {
//			log.info(e.getMessage());
//			response.setResultCode("50000");
//			response.setResultDescription(e.getMessage());
//			response.setStatus("F");
//		}
//		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
//
//	}
//
//}
