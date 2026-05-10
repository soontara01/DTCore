//package th.co.ais.dt.controller.spec.util.test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.PutMethod;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.Gson;
//
//import lombok.extern.slf4j.Slf4j;
//import th.co.ais.dt.controller.dto.AcimUserBean;
//
//@Slf4j
//public class AcimWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void addUserAcimTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/acim/acimuser");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		method.setRequestHeader("x-acim-ClientId", "pq6a34pbi-crwz90kdr");
//		method.setRequestHeader("x-acim-req-id", "x-acim-req-id-111");
//		method.setRequestHeader("x-acim-sp", "x-acim-sp-111");
//
//		List<AcimUserBean> request = new ArrayList<>();
//		AcimUserBean el = new AcimUserBean();
//		el.setUsername("soontarr3");
//		el.setRole("Warehouse (Outbound)");
//		request.add(el);
//
//		AcimUserBean el2 = new AcimUserBean();
//		el2.setUsername("soontarr4");
//		el2.setRole("Warehouse (Outbound)");
//		request.add(el2);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void editUserAcimTest()  throws Exception {
//		PutMethod method = new PutMethod("http://localhost:8090/DTWS/api/acim/acimuser");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		method.setRequestHeader("x-acim-ClientId", "pq6a34pbi-crwz90kdr");
//		method.setRequestHeader("x-acim-req-id", "x-acim-req-id-111");
//		method.setRequestHeader("x-acim-sp", "x-acim-sp-111");
//
//		List<AcimUserBean> request = new ArrayList<>();
//		AcimUserBean el = new AcimUserBean();
//		el.setUsername("soontarr3");
//		el.setRole("Warehouse (Outbound)");
//		el.setRolePast("Outsource - Online Store");
//		request.add(el);
//
//		AcimUserBean el2 = new AcimUserBean();
//		el2.setUsername("soontarr4");
//		el2.setRole("Outsource - Online Store");
//		el2.setRolePast("Warehouse (Outbound)");
//		request.add(el2);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void deleteUserAcimTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/acim/acimuser/del");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		method.setRequestHeader("x-acim-ClientId", "pq6a34pbi-crwz90kdr");
//		method.setRequestHeader("x-acim-req-id", "x-acim-req-id-111");
//		method.setRequestHeader("x-acim-sp", "x-acim-sp-111");
//
//		List<AcimUserBean> request = new ArrayList<>();
//		AcimUserBean el = new AcimUserBean();
//		el.setUsername("soontarr3");
//		el.setRole("Warehouse (Outbound)");
//		request.add(el);
//
//		AcimUserBean el2 = new AcimUserBean();
//		el2.setUsername("soontarr4");
//		el2.setRole("Outsource - Online Store");
//		request.add(el2);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//
//}
