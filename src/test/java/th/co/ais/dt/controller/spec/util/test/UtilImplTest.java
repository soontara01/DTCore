//package th.co.ais.dt.controller.spec.util.test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.json.JSONObject;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.Gson;
//
//import lombok.extern.slf4j.Slf4j;
//import th.co.ais.dt.controller.dto.ConfigGoodsReplaceReq;
//import th.co.ais.dt.controller.dto.DealerMstWebBean;
//import th.co.ais.dt.controller.dto.FindAscCodeBeanAndRes;
//import th.co.ais.dt.controller.dto.LovMasterInput;
//import th.co.ais.dt.controller.dto.LovMasterRequest;
//import th.co.ais.dt.controller.dto.ProductBean;
//import th.co.ais.dt.controller.dto.QueryLovTypeWithInBean;
//import th.co.ais.dt.controller.dto.UserProfileBean;
//
//@Slf4j
//public class UtilImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void validateLoginTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/validateLogin");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/validateLogin");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"username\":\"soontarr\",\"password\":\"1\",\"locationCode\":\"1100\",\"idsFlg\":\"Y\"}";
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void ssoinfoDTTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/ssoinfoDT");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/ssoinfoDT");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		Map req = new HashMap<>();
//		req.put("userId", "soontarr");
//		req.put("lc", "1012");
//		req.put("ssoGroupId", "1012");
//		req.put("ssoSubModuleId", "Y");
//		req.put("userType", "EMPLOYEE");
//		req.put("programCode", "TDSSK004");
//		req.put("flg", "N");
//		req.put("refererURL", "Y");
//		req.put("fn", "Y");
//		req.put("ln", "Y");
//		req.put("token", "Kk5tcsTFwlL79yyG2nT7qvR3yynDL4cLsTzwSRLWckQL4FBMhWnT!-743966069!1559023205157,tmp,999,999,null");
//		req.put("idsId", "Y");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(req), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryProductMstByUniqueTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/query-product-mst-by-unique");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/query-product-mst-by-unique");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		ProductBean proReq = new ProductBean();
//		proReq.setCompany("AWN");
//		proReq.setMatcode("NEW0APP1146-SG01") ;
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listLovByCriteriaTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/list-lov-by-criteria");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/list-lov-by-criteria");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		LovMasterInput proReq = new LovMasterInput();
//		proReq.setLovType("E_RECIPT") ;
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listLovMstByCriteriaTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/list-lov-mst-by-criteria");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/list-lov-by-criteria");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		LovMasterInput proReq = new LovMasterInput();
//		proReq.setLovType("MAPPING_CONFIG_BYPASS_SSO_MC") ;
//		proReq.setLovActive("Y");
//		proReq.setSystemCode("SS");
//
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listConfigurationPostTest()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/config/lovMasterActive");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/config/lovMasterActive");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		LovMasterRequest proReq = new LovMasterRequest();
//		proReq.setLovType("MAPPING_PRO_URL_DT") ;
//		//proReq.setLovSubType("BYPASSSSO");
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void setupLovMasterTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/config/setupLovMaster");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/util/v1/config/lovMasterActive");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		List<ConfigGoodsReplaceReq> listEl = new ArrayList<>();
//		ConfigGoodsReplaceReq proReq = new ConfigGoodsReplaceReq();
//
//		ConfigGoodsReplaceReq elItem = new ConfigGoodsReplaceReq();
//		elItem.setLovType("GOODS_REPLACE_BRAND_CON");
//		elItem.setLovCode("SAMSUNG");
//		elItem.setLovVal("A705F");
//		elItem.setActiveFlag("Y");
//		elItem.setHigh("N");
//		elItem.setDescription("W3");
//		elItem.setSystemSource("TDM");
//
//		listEl.add(elItem);
//
//		proReq.setUserId("soontarr");
//		proReq.setDataList(listEl);
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listLocationCodeTest()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/list-location-code");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/list-location-code");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		List<ConfigGoodsReplaceReq> listEl = new ArrayList<>();
//		ConfigGoodsReplaceReq proReq = new ConfigGoodsReplaceReq();
//
//	    String in = "{\"userId\":\"soontarr\"}";
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryLovTypeWithInTest()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/query-lov-type-with-in");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/query-lov-type-with-in");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		List<ConfigGoodsReplaceReq> listEl = new ArrayList<>();
//		QueryLovTypeWithInBean proReq = new QueryLovTypeWithInBean();
//		proReq.setLovActive("Y");
//
//		List<String> liType = new ArrayList<>();
//		liType.add("DT_PASS_USER");
//		liType.add("E_RECIPT");
//		List<String> liSubType = new ArrayList<>();
//		liSubType.add("TIME_JOB_WAIT") ;
//		liSubType.add("DT_PASS_USER") ;
//		proReq.setLovType(liType) ;
//		proReq.setLovSubType(liSubType);
//
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void getAscMstByCodeTest()  throws Exception {
//		GetMethod method = new GetMethod("http://localhost:8090/DTWS/api/util/v1/query-asccode-by-code?asccode=003739");
//		//GetMethod method = new GetMethod("http://192.168.75.129:8090/DTWS/api/util/v1/query-asccode-by-code?asccode=7866181");
//
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listAscCodeByCriteriaTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/list-asccode-by-criteria");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/list-asccode-by-criteria");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		List<ConfigGoodsReplaceReq> listEl = new ArrayList<>();
//		FindAscCodeBeanAndRes proReq = new FindAscCodeBeanAndRes();
//		//proReq.setAscCode("003739");
//		 proReq.setAscName("นางสาวญาณิษา คำทอง");
//		//proReq.setAscType("aaa");
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void getMessageDesc0Test()  throws Exception {
//		//GetMethod method = new GetMethod("http://localhost:8090/DTWS/api/util/v1/MessageMst?messageCode=IN0080");
//		GetMethod method = new GetMethod("http://192.168.75.129:8090/DTWS/api/util/v1/MessageMst?messageCode=IN0080");
//
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryCmLocationSubTypeForEsimReportTest()  throws Exception {
//		//GetMethod method = new GetMethod("http://localhost:8090/DTWS/api/util/v1/queryCmLocationSubTypeForEsimReport");
//		GetMethod method = new GetMethod("http://192.168.75.129:8090/DTWS/api/util/v1/queryCmLocationSubTypeForEsimReport");
//
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryCmLocationBusinessUnitForEsimReportTest()  throws Exception {
//		//GetMethod method = new GetMethod("http://localhost:8090/DTWS/api/util/v1/queryCmLocationBusinessUnitForEsimReport");
//		GetMethod method = new GetMethod("http://10.138.47.131:8080/DTWS/api/util/v1/queryCmLocationBusinessUnitForEsimReport");
//
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void listDealerCodeByCodeAndNameTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/util/v1/list-dealercode-by-codeandname");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/list-dealercode-by-codeandname");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		DealerMstWebBean proReq = new DealerMstWebBean();
//		proReq.setDealerCode("200033327");
//		proReq.setDealerName("[1400004609] ดีโมบาย1,2 โดย คุณนิชาภัทร สาลีวรรณ อ.ศรีเมืองใหม่ จ.อุบลราชธานี") ;
//
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryUserProfile()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-user-profile");
//		//PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/util/v1/list-dealercode-by-codeandname");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		UserProfileBean proReq = new UserProfileBean();
//		proReq.setUserId("soontarr");
//
//
//		log.info(gson.toJson(proReq));
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	void queryUserProfile_GroupNameExist_Success() throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/api/master-config/v1/query-user-profile");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		UserProfileBean proReq = new UserProfileBean();
//		proReq.setUserId("jirayusi");
//		log.info(gson.toJson(proReq));
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString());
//
//		JSONObject response = new JSONObject(method.getResponseBodyAsString());
//
//		Assert.assertEquals("20000", response.getString("resultCode"));
//		Assert.assertFalse(response.getString("queryGroupNameList").isEmpty());
//
//		method.releaseConnection();
//	}
//
//	@Test
//	void queryUserProfile_UserIdNotExist_DataNotFound() throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/api/master-config/v1/query-user-profile");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		UserProfileBean proReq = new UserProfileBean();
//		proReq.setUserId("userIdNotExist");
//		log.info(gson.toJson(proReq));
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString());
//
//		JSONObject response = new JSONObject(method.getResponseBodyAsString());
//
//		Assert.assertEquals("50005", response.getString("resultCode"));
//		Assert.assertEquals("Data not found.", response.getString("developerMessage"));
//		Assert.assertEquals("Data not found.", response.getString("resultDescription"));
//
//		method.releaseConnection();
//	}
//
//	@Test
//	void queryUserProfile_GroupNameNotExist_DataNotFound() throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/api/master-config/v1/query-user-profile");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		UserProfileBean proReq = new UserProfileBean();
//		proReq.setUserId("groupNameNotExist");
//		log.info(gson.toJson(proReq));
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(proReq), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString());
//
//		JSONObject response = new JSONObject(method.getResponseBodyAsString());
//
//		Assert.assertEquals("50005", response.getString("resultCode"));
//		Assert.assertEquals("Data not found.", response.getString("developerMessage"));
//		Assert.assertEquals("Data not found.", response.getString("resultDescription"));
//
//		method.releaseConnection();
//	}
//}
