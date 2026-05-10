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
//import th.co.ais.dt.entity.util.LovMaster;
//import th.co.ais.dt.controller.dto.ConfigLovMasterBean;
//@Slf4j
//public class ConfigLovMasterWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void checkAuthUser()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/check-auth-user");
//		PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/check-auth-user");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ConfigLovMasterBean in = new ConfigLovMasterBean();
//		in.setUserId("nunts411");
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
//	public void queryLovMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/query-lov-master");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/query-lov-master");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		LovMaster in = new LovMaster();
//		in.setLovType("SALE_CHANNEL");
//
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
//	public void insertLovMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/insert-lov-master");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/insert-lov-master");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ConfigLovMasterBean in = new ConfigLovMasterBean();
//		in.setUserId("soontarr");
//		in.setLovType("SALE_CHANNEL");
//		in.setLovCode("STP");
//		in.setLovVal("STP");
//		in.setDescription("Stock Point");
//		in.setOrderBy("8");
//		in.setCompany("WDS");
//		in.setSystemCode("SO");
//		in.setSystemSource("TDM");
//
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
//	public void editLovMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/edit-lov-master");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/insert-lov-master");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ConfigLovMasterBean in = new ConfigLovMasterBean();
//		in.setUserId("soontarr");
//		in.setLovType("SALE_CHANNEL");
//		in.setLovCode("STP");
//		in.setLovVal("STP");
//		in.setDescription("Stock Pointsss");
//		in.setOrderBy("8");
//		in.setCompany("WDS");
//		in.setSystemCode("SO");
//		in.setSystemSource("TDM");
//		in.setSeqNo("2913");
//
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
//	public void displayConfigLovMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/display-config-lov-master");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/display-config-lov-master");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ConfigLovMasterBean in = new ConfigLovMasterBean();
//		in.setUserId("soontarr");
//		in.setLovType("SALE_CHANNEL");
//		in.setLovCode("STP");
//		in.setLovVal("STP");
//		in.setDescription("Stock Pointsss");
//		in.setOrderBy("8");
//		in.setCompany("WDS");
//		in.setSystemCode("SO");
//		in.setSystemSource("TDM");
//		in.setSeqNo("2913");
//		//in.setSeqNo("132724");
//
//		in.setFlgQueryColumnEdit("EDIT");
//
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
//	public void setFlgActiveLovMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/set-flg-active-lov-master");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/display-config-lov-master");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ConfigLovMasterBean in = new ConfigLovMasterBean();
//
//		List<String> l = new ArrayList<>();
//		l.add("2913") ;
//		l.add("11070");
//		in.setSeqNoList(l);
//		in.setLovType("SALE_CHANNEL");
//		in.setUserId("soontarr");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void setFlgInactiveLovMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/configLovMaster/v1/set-flg-inactive-lov-master");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/configLovMaster/v1/display-config-lov-master");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ConfigLovMasterBean in = new ConfigLovMasterBean();
//
//		List<String> l = new ArrayList<>();
//		l.add("2913") ;
//		l.add("11070");
//		in.setSeqNoList(l);
//		in.setLovType("SALE_CHANNEL");
//		in.setUserId("soontarr");
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
