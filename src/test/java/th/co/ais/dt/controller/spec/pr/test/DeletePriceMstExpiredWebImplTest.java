//package th.co.ais.dt.controller.spec.pr.test;
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
//
//@Slf4j
//public class DeletePriceMstExpiredWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void deletePriceExPired()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/delete-pricemst-expired");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{}";
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//}
