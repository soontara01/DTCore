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
//import th.co.ais.dt.controller.dto.PLMDeviceOfferProcessBean;
//@Slf4j
//public class PLMDeviceOfferProcessWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void listPriceUpdateTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/mt/v1/listPriceUpdate");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		PLMDeviceOfferProcessBean in = new PLMDeviceOfferProcessBean();
//		List<String> pp = new ArrayList<>();
//		pp.add("STAFF");
//
//		List<String> ppp = new ArrayList<>();
//		ppp.add("DEVICE");
//		in.setGroupPriceList(pp);
//		in.setProductTypeList(ppp);
//		in.setStartDate("01/01/2023");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listProductUpdate()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/mt/v1/listProductUpdate");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		PLMDeviceOfferProcessBean in = new PLMDeviceOfferProcessBean();
//		List<String> pp = new ArrayList<>();
//		pp.add("STAFF");
//
//		List<String> ppp = new ArrayList<>();
//		ppp.add("DEVICE");
//		in.setGroupPriceList(pp);
//		in.setProductTypeList(ppp);
//		in.setStartDate("27/02/2023");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//}
