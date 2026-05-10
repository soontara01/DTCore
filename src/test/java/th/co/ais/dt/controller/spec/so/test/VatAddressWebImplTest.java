//package th.co.ais.dt.controller.spec.so.test;
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
//import th.co.ais.dt.entity.so.VatAddress;
//import th.co.ais.dt.controller.dto.VatAddressBean;
//
//@Slf4j
//public class VatAddressWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void insertVatAddress()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/sale/v1/insertVatAddress");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/sale/v1/insertVatAddress");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		VatAddress in = new VatAddress();
//		in.setTaxId("11121213");
//		in.setCreateBy("soontarr");
//		in.setHouseNo("21");
//		in.setRoom("21");
//		in.setMobileNo("083232323");
//		in.setTelNo("083232323");
//		in.setCustomerName("asas saasas");
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryListVatAddress()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/sale/v1/queryListVatAddress");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/sale/v1/queryListVatAddress");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		VatAddressBean in = new VatAddressBean();
//		in.setTaxId("11121213");
//		//in.setMobileNo("soontarr");
//		//in.setTelNo("21");
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
