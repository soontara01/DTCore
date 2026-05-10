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
//import th.co.ais.dt.controller.dto.SetupLocationForPMSBean;
//@Slf4j
//public class SetupLocationForPMSWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void saveLocationForPMS()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupLocationForPMS/v1/save-location-for-PMS");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupLocationForPMSBean in = new SetupLocationForPMSBean();
//		in.setUserId("soontarr");
//
//
//		List<SetupLocationForPMSBean> locationForPMSList = new ArrayList<>();
//		SetupLocationForPMSBean el1 = new SetupLocationForPMSBean();
//		el1.setCompany("AWN");
//		el1.setLocationCode("1100");
//		el1.setArSummary("3000000121");
//		el1.setZoneArea("BKK");
//		el1.setArPledging("A1");
//
//		locationForPMSList.add(el1);
//
//		SetupLocationForPMSBean el2 = new SetupLocationForPMSBean();
//		el2.setCompany("AWN");
//		el2.setLocationCode("20006");
//		el2.setArSummary("3000000121");
//		el2.setZoneArea("BKK");
//		el2.setArPledging("A1");
//
//		locationForPMSList.add(el2);
//
//		in.setLocationForPMSList(locationForPMSList);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void deleteLocationForPMS()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupLocationForPMS/v1/delete-location-for-PMS");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupLocationForPMSBean in = new SetupLocationForPMSBean();
//		in.setUserId("soontarr");
//		in.setCompany("AWN") ;
//		in.setLocationCode("1100") ;
//
//
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
//	public void editLocationForPMS()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupLocationForPMS/v1/edit-location-for-PMS");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupLocationForPMSBean in = new SetupLocationForPMSBean();
//		in.setUserId("soontarr");
//
//		in.setCompany("AWN");
//		in.setLocationCode("20006");
//		in.setArSummary("3000000121");
//		in.setZoneArea("BKK");
//		in.setArPledging("A2");
//
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
//	public void querySetupLocationForPMS()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupLocationForPMS/v1/query-setup-location-for-PMS");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupLocationForPMSBean in = new SetupLocationForPMSBean();
//		in.setUserId("soontarr");
//
//		in.setCompany("AWN");
//		in.setLocationCode("20006");
//		in.setArSummary("3000000121");
//		in.setZoneArea("BKK");
//		//in.setArPledging("A2");
//
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
