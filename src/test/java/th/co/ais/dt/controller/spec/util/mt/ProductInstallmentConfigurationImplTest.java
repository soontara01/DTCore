//package th.co.ais.dt.controller.spec.util.mt;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.Gson;
//
//import lombok.extern.slf4j.Slf4j;
//import th.co.ais.dt.controller.dto.ProductInstallmentBean;
//@Slf4j
//public class ProductInstallmentConfigurationImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void insertProductInstallment()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/installmentProduct/v1/insert-installment-product");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ProductInstallmentBean in = new ProductInstallmentBean();
//		in.setUserId("soontarr");
//		List<ProductInstallmentBean> listProductInstallmentBean = new ArrayList<>();
//
//		ProductInstallmentBean el1 = new ProductInstallmentBean();
//		el1.setChannel("AIS");
//		el1.setBankCode("KBANK");
//		el1.setProductGroup("BS");
//		el1.setActiveFlg("Y");
//		el1.setCreditCardType("P");
//		el1.setProductType("DEVICE");
//		el1.setProductSubType("HANDSET");
//		el1.setBrand("SAMSUNG");
//		el1.setModel("A505X");
//		el1.setStartDate("16/5/2023");
//		el1.setEndDate("16/5/2024");
//		el1.setPaymentTerm("03");
//		el1.setInterateAssort("02");
//		listProductInstallmentBean.add(el1);
//
//		in.setListProductInstallmentBean(listProductInstallmentBean);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void updateProductInstallment()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/installmentProduct/v1/update-installment-product");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		//ProductInstallmentBean in = new ProductInstallmentBean();
//		//in.setUserId("soontarr");
//		//List<ProductInstallmentBean> listProductInstallmentBean = new ArrayList<>();
//
//		ProductInstallmentBean el1 = new ProductInstallmentBean();
//		el1.setChannel("AIS");
//		el1.setBankCode("KBANK");
//		el1.setProductGroup("BS");
//		el1.setActiveFlg("Y");
//		el1.setCreditCardType("P");
//		el1.setProductType("DEVICE");
//		el1.setProductSubType("HANDSET");
//		el1.setBrand("SAMSUNG");
//		el1.setModel("A505X");
//		el1.setStartDate("16/5/2023");
//		el1.setEndDate("16/5/2024");
//		el1.setPaymentTerm("04");
//		el1.setInterateAssort("02");
//		el1.setUserId("soontarr");
//		el1.setProductInstId(329L);
//		//listProductInstallmentBean.add(el1);
//
//
//
//
//
//		//in.setListProductInstallmentBean(listProductInstallmentBean);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(el1), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryProductInstallment()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/installmentProduct/v1/query-installment-product");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/installmentProduct/v1/query-installment-product");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ProductInstallmentBean el1 = new ProductInstallmentBean();
//		el1.setChannel("AIS");
//		el1.setBankCode("KBNK");
//		el1.setProductGroup("BS");
//		el1.setActiveFlg("Y");
//		el1.setCreditCardType("P");
//		el1.setProductType("DEVICE");
//		el1.setProductSubType("HANDSET");
//		el1.setBrand("SAMSUNG");
//		el1.setModel("A505X");
//		el1.setStartDate("16/5/2023");
//		el1.setEndDate("16/5/2024");
//		el1.setPaymentTerm("04");
//		el1.setInterateAssort("02");
//		el1.setUserId("soontarr");
//		//el1.setProductInstId(329L);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(el1), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
