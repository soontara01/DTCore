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
//import th.co.ais.dt.controller.dto.SetupTravellerSimBean;
//@Slf4j
//public class SetupTravellerSimWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void querySetupTravellerSim()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupTravellerSim/v1/query-setup-traveller-sim");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupTravellerSimBean in = new SetupTravellerSimBean();
//		in.setUserId("soontarr");
//		in.setServiceUsage("Outbound");
//		in.setMktSimType("Test") ;
//
//		List<String> lMat = new ArrayList<>();
//		lMat.add("NEWAPPI7256-BK01");
//		in.setListMatCode(lMat);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void saveTravellerConfMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupTravellerSim/v1/save-traveller-conf-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupTravellerSimBean in = new SetupTravellerSimBean();
//		List<SetupTravellerSimBean> travellerConfMstList = new ArrayList<>();
//
//		SetupTravellerSimBean el1 = new SetupTravellerSimBean();
//		el1.setMatCode("NEW0APP1266-SV01");
//		el1.setSharePerUnit("93.46");
//		el1.setEndDate("10/12/2023");
//		el1.setStartDate("18/05/2023");
//		el1.setSharePerUnitIncVat("100.00");
//		el1.setFreeAirtimeIncvat("30");
//		el1.setPackageDuration("30");
//		el1.setServiceUsage("domestic");
//		el1.setTradeNo("TP11100022");
//		el1.setMktSimType("Tralveller SIM");
//		travellerConfMstList.add(el1);
//
//		SetupTravellerSimBean el2 = new SetupTravellerSimBean();
//		el2.setMatCode("NEW0APP1263-SG01");
//		el2.setSharePerUnit("93.46");
//		el2.setEndDate("10/12/2023");
//		el2.setStartDate("18/05/2023");
//		el2.setSharePerUnitIncVat("100.00");
//		el2.setFreeAirtimeIncvat("30");
//		el2.setPackageDuration("30");
//		el2.setServiceUsage("domestic");
//		el2.setTradeNo("TP11100022");
//		el2.setMktSimType("Tralveller SIM");
//		travellerConfMstList.add(el2);
//
//		in.setTravellerConfMstList(travellerConfMstList);
//		in.setUserId("soontarr");
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
//	public void editSetupTravellerSim()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupTravellerSim/v1/edit-setup-traveller-sim");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupTravellerSimBean in = new SetupTravellerSimBean();
//		List<SetupTravellerSimBean> travellerConfMstList = new ArrayList<>();
//
//
//		in.setMatCode("NEW0APP1266-SV01");
//		in.setSharePerUnit("93.46");
//		in.setEndDate("10/12/2023");
//		in.setStartDate("18/05/2023");
//		in.setSharePerUnitIncVat("100.00");
//		in.setFreeAirtimeIncvat("30");
//		in.setPackageDuration("30");
//		in.setServiceUsage("domestic");
//		in.setTradeNo("TP11100025");
//		in.setMktSimType("Tralveller SIM");
//		in.setTravellerId("2");
//		//travellerConfMstList.add(el1);
//
////		SetupTravellerSimBean el2 = new SetupTravellerSimBean();
////		el2.setMatCode("NEW0APP1263-SG01");
////		el2.setSharePerUnit("93.46");
////		el2.setEndDate("10/12/2023");
////		el2.setStartDate("18/05/2023");
////		el2.setSharePerUnitIncVat("100.00");
////		el2.setFreeAirtimeIncvat("30");
////		el2.setPackageDuration("30");
////		el2.setServiceUsage("domestic");
////		el2.setTradeNo("TP11100026");
////		el2.setMktSimType("Tralveller SIM");
////		el1.setTravellerId("3");
////		travellerConfMstList.add(el2);
//
//		//in.setTravellerConfMstList(travellerConfMstList);
//		in.setUserId("soontarr");
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
//	public void validateUploadTravellerConfMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/setupTravellerSim/v1/validate-upload-traveller-conf-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupTravellerSimBean in = new SetupTravellerSimBean();
//		List<SetupTravellerSimBean> travellerConfMstList = new ArrayList<>();
//
//		SetupTravellerSimBean el1 = new SetupTravellerSimBean();
//		el1.setMatCode("NEW0APP1266-SV01");
//		el1.setSharePerUnit("93.46");
//		el1.setEndDate("10/12/2023");
//		el1.setStartDate("18/05/2023");
//		el1.setSharePerUnitIncVat("100.00");
//		el1.setFreeAirtimeIncvat("30");
//		el1.setPackageDuration("30");
//		el1.setServiceUsage("domestic");
//		el1.setTradeNo("TP11100022");
//		el1.setMktSimType("Tralveller SIM");
//		travellerConfMstList.add(el1);
//
//		SetupTravellerSimBean el2 = new SetupTravellerSimBean();
//		el2.setMatCode("NEW0APP1263-SG01");
//		el2.setSharePerUnit("93.46");
//		el2.setEndDate("10/12/2023");
//		el2.setStartDate("18/05/2023");
//		el2.setSharePerUnitIncVat("100.00");
//		el2.setFreeAirtimeIncvat("30");
//		el2.setPackageDuration("30");
//		el2.setServiceUsage("domestic");
//		el2.setTradeNo("TP11100022");
//		el2.setMktSimType("Tralveller SIM");
//		travellerConfMstList.add(el2);
//
//		in.setTravellerConfMstList(travellerConfMstList);
//		in.setUserId("soontarr");
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
