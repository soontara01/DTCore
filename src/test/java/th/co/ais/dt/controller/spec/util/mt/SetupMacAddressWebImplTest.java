//package th.co.ais.dt.controller.spec.util.mt;
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
//import th.co.ais.dt.controller.dto.SetupMacAddressBean;
//@Slf4j
//public class SetupMacAddressWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void queryMacAddressTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setup-mac-address/v1/query-mac-address");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupMacAddressBean in  = new SetupMacAddressBean();
//		in.setCompany("AWN");
//		in.setLocation("1177");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void insertMacAddressTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setup-mac-address/v1/insert-mac-address");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupMacAddressBean in  = new SetupMacAddressBean();
//		in.setCompany("AWN");
//		in.setLocationCode("1203");
//		in.setMacAddress("max-00001");
//		in.setPosNo("pos-0003");
//		in.setPosSystem("TDM");
//		in.setUserId("soontarr");
//		in.setSn("sn");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryLocationLikeTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setup-mac-address/v1/query-location-mac-address");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupMacAddressBean in  = new SetupMacAddressBean();
//		in.setLocationCode("111111111111");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryLocationNameByLocationCodeTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setup-mac-address/v1/query-location-name-by-location-code");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/setup-mac-address/v1/query-location-name-by-location-code");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupMacAddressBean in  = new SetupMacAddressBean();
//		in.setLocationCode("1100");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryLocationByKeyTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setup-mac-address/v1/query-location-by-key");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/setup-mac-address/v1/query-location-by-key");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupMacAddressBean in  = new SetupMacAddressBean();
//		in.setLocationCode("1100");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void updateMacAddressTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setup-mac-address/v1/update-mac-address");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupMacAddressBean in  = new SetupMacAddressBean();
////		in.setRowId("FC820E23AD311419E04400144FF86678");
////		in.setCompany("AWN");
////		in.setLocationCode("1175");
////		in.setMacAddress("00-23-24-1E-C6-F2");
////		in.setPosNo("E20150115000001");
////		in.setPosSystem("TDM");
////		in.setUserId("soontarr");
////		in.setSn("sn");
////		in.setActiveStatus("ACTIVE");
//
//
//		in.setRowId("15");
//		in.setCompany("AWN");
//		in.setLocationCode("1203");
//		in.setMacAddress("max-00001");
//		in.setPosNo("pos-000345");
//		in.setPosSystem("TDM");
//		in.setUserId("soontarr");
//		in.setSn("sn");
//		in.setActiveStatus("ACTIVE");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//
//}
