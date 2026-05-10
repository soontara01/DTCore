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
//import th.co.ais.dt.controller.dto.LovMstBean;
//import th.co.ais.dt.controller.dto.SyncLovBean;
//@Slf4j
//public class SyncLovFromDtWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void syncLovTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/sync-lov");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SyncLovBean in = new SyncLovBean();
//
//		List<LovMstBean> listLov = new ArrayList<>();
//		in.setAction("Insert");
//
//
//		LovMstBean lo1 = new LovMstBean();
//		lo1.setLovType("NOOM_LOVTYPE2") ;
//		lo1.setLovSubType("NOOM_LovSubType") ;
//		lo1.setLovCode("NOOM_CODE");
//		lo1.setLovVal("NOOM_VALUE");
//		lo1.setDescription("NOOM_Description");
//		lo1.setPrSeqNo("1");
//		lo1.setOrderBy("1");
//		lo1.setCompany("AWN");
//		lo1.setSystemCode("SC");
//		lo1.setSystemSubCode("sub");
//		lo1.setHigh("A");
//		lo1.setActiveFlag("Y");
//		lo1.setSystemSource("DT");
//		lo1.setLovAttribute01("A1");
//		lo1.setLovAttribute02("A2");
//		lo1.setLovAttribute03("A3");
//
//		lo1.setCreated("25/04/2023 15:00:01");
//		lo1.setCreatedBy("soontarr");
//		lo1.setLastUpd("25/04/2023 15:00:01");
//		lo1.setLastUpdBy("soontarr");
//		listLov.add(lo1);
//
//		LovMstBean lo2 = new LovMstBean();
//		lo2.setLovType("NOOM_LOVTYPE2") ;
//		lo2.setLovSubType("NOOM_LovSubType") ;
//		lo2.setLovCode("NOOM_CODE");
//		lo2.setLovVal("NOOM_VALUE");
//		lo2.setDescription("NOOM_Description");
//		lo2.setActiveFlag("Y");
//		lo2.setCreated("25/04/2023 15:00:01");
//		lo2.setCreatedBy("soontarr");
//		lo2.setLastUpd("25/04/2023 15:00:01");
//		lo2.setLastUpdBy("soontarr");
//		lo2.setSystemSource("DT");
//		listLov.add(lo2);
//
//
//
//		in.setListLov(listLov);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void syncLovDeleteTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/sync-lov");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SyncLovBean in = new SyncLovBean();
//
//		List<LovMstBean> listLov = new ArrayList<>();
//		in.setAction("Delete");
//
//
//		LovMstBean lo1 = new LovMstBean();
//		lo1.setLovType("NOOM_LOVTYPE2") ;
//		lo1.setLovSubType("NOOM_LovSubType") ;
//		lo1.setLovCode("NOOM_CODE");
//		lo1.setLovVal("NOOM_VALUE");
//		lo1.setDescription("NOOM_Description");
//		lo1.setPrSeqNo("1");
//		lo1.setOrderBy("1");
//		lo1.setCompany("AWN");
//		lo1.setSystemCode("SC");
//		lo1.setSystemSubCode("sub");
//		lo1.setHigh("A");
//		lo1.setActiveFlag("Y");
//		lo1.setSystemSource("DT");
//		lo1.setLovAttribute01("A1");
//		lo1.setLovAttribute02("A2");
//		lo1.setLovAttribute03("A3");
//
//		lo1.setCreated("25/04/2023 15:00:01");
//		lo1.setCreatedBy("soontarr");
//		lo1.setLastUpd("25/04/2023 15:00:01");
//		lo1.setLastUpdBy("soontarr");
//		listLov.add(lo1);
//
//		LovMstBean lo2 = new LovMstBean();
//		lo2.setLovType("NOOM_LOVTYPE2") ;
//		lo2.setLovSubType("NOOM_LovSubType") ;
//		lo2.setLovCode("NOOM_CODE");
//		lo2.setLovVal("NOOM_VALUE");
//		lo2.setDescription("NOOM_Description");
//		lo2.setActiveFlag("Y");
//		lo2.setCreated("25/04/2023 15:00:01");
//		lo2.setCreatedBy("soontarr");
//		lo2.setLastUpd("25/04/2023 15:00:01");
//		lo2.setLastUpdBy("soontarr");
//		lo2.setSystemSource("DT");
//		listLov.add(lo2);
//
//
//
//		in.setListLov(listLov);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void syncLovFromDtTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/util/v1/sync-lov-dt-cloud");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{}";
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
