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
//import th.co.ais.dt.controller.dto.LovMasterRequest;
//import th.co.ais.dt.controller.dto.SetupPriceBean;
//@Slf4j
//public class SetupPriceWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void queryCompanyTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setprice/v1/query-company");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/setprice/v1/query-company");
//
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
//	@Test
//	public void queryPriceGroupTest()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setprice/v1/query-price-group");
//		PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/setprice/v1/query-price-group");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		//String in = "{}";
//		LovMasterRequest in =new LovMasterRequest();
//		in.setLovType("PRICE_GROUP");
//		in.setLovSubType("PRICE_GROUP_WS");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryPopupPerformTest()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setprice/v1/list-productMsts-by-criteria");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/setprice/v1/list-productMsts-by-criteria");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		//String in = "{}";
//		SetupPriceBean in =new SetupPriceBean();
//		in.setMatCode("NEW0APP1266-SV01");
//		in.setTdmDescription("NEW WIFI AP");
//		in.setBrand("APPLE");
//		in.setColor("SILVER");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//}
