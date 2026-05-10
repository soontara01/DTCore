//package th.co.ais.dt.controller.spec.pr.test;
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
//import th.co.ais.dt.controller.dto.TDSPR002LoadPriceExcelBean;
//import th.co.ais.dt.service.core.dto.SetupPriceResponse;
//
//@Slf4j
//public class TDSPR002LoadPriceExcelWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void queryCriteria()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/TDSPR001/query-criteria");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		//TDSPR002LoadPriceExcelBean in = new TDSPR002LoadPriceExcelBean();
//		//in.setAction("QUERY_CRITERIA")
//
//
//		String in = "{\"action\":\"QUERY_CRITERIA\"}";
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void validateupload()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/TDSPR002/validate-upload");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR002LoadPriceExcelBean request = new TDSPR002LoadPriceExcelBean();
//		request.setCompany("AWN");
//		request.setVatRate("7.00");
//		request.setUserId("soontarr");
//		List<SetupPriceResponse> li = new ArrayList<>();
//		SetupPriceResponse el = new SetupPriceResponse();
//		el.setMatCode("NW00SIM3PPGMW99");
//		el.setPriceGroup("DIRECTSALE");
//		el.setIncVat("20000");
//		el.setPriceDiffCash(null);
//		el.setPriceDiffCredit(null);
//		el.setEffectiveDate("24/02/2023 01:00");
//		el.setExpireDate("24/02/2023 01:59");
//		el.setPaymentMethod("CASH");
//		el.setDescription(null);
//
//		li.add(el);
//		request.setExcelList(li);
//
//
//		String in = "{\"action\":\"QUERY_CRITERIA\"}";
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void validateuploadMulti()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR002/validate-upload");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR002LoadPriceExcelBean request = new TDSPR002LoadPriceExcelBean();
//		request.setCompany("AWN");
//		request.setVatRate("7.00");
//		request.setUserId("soontarr");
//		List<SetupPriceResponse> li = new ArrayList<>();
//		SetupPriceResponse el = new SetupPriceResponse();
//		el.setMatCode("NW00SIM3PPGMW99");
//		el.setPriceGroup("EUP");
//		el.setIncVat("");
//		el.setPriceDiffCash(null);
//		el.setPriceDiffCredit(null);
//		el.setEffectiveDate("26/02/2023 15:11");
//		el.setExpireDate("26/02/2023 15:30");
//		el.setPaymentMethod("CASH");
//		el.setDescription(null);
//		li.add(el);
//
//		el = new SetupPriceResponse();
//		el.setMatCode("NW00SIM3PPGMW99");
//		el.setPriceGroup("EUP");
//		el.setIncVat("20000");
//		el.setPriceDiffCash(null);
//		el.setPriceDiffCredit(null);
//		el.setEffectiveDate("");
//		el.setExpireDate("");
//		el.setPaymentMethod("CASH");
//		el.setDescription(null);
//		li.add(el);
//
//
//		request.setExcelList(li);
//
//
//		String in = "{\"action\":\"QUERY_CRITERIA\"}";
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void saveupload()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/TDSPR002/save-upload");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR002LoadPriceExcelBean request = new TDSPR002LoadPriceExcelBean();
//		request.setUserId("soontarr");
//		List<SetupPriceResponse> li = new ArrayList<>();
//		SetupPriceResponse el = new SetupPriceResponse();
//		el.setMatCode("NEW0SSA013G-BK15");
//		el.setCompany("AWN");
//		el.setPriceGroup("STAFF");
//		el.setDescription("TEST");
//		el.setProductId("793842");
//		el.setVatType("Y");
//		el.setIncVat("300");
//		el.setExcVat("18691.59");
//		el.setVatRate("7.00");
//		el.setPriceVat("1308.41");
//		el.setPriceDiffCash("1000");
//		el.setPriceDiffCredit("2000");
////		el.setEffectiveDate("18/02/2023 13:00");
//		el.setEffectiveDate("02/03/2023 17:48");
//		el.setExpireDate("05/03/2023 17:47");
//
//		//el.setExpireDate("");
//		el.setPaymentMethod("CASH");
//		el.setPriceId("107");
//
//		li.add(el);
//
//		el = new SetupPriceResponse();
//		el.setMatCode("NW00SIM3PPGMW99");
//		el.setCompany("AWN");
//		el.setPriceGroup("DIRECTSALE");
//		el.setDescription("TEST");
//		el.setProductId("14");
//		el.setVatType("Y");
//		el.setIncVat("500");
//		el.setExcVat("18691.59");
//		el.setVatRate("7.00");
//		el.setPriceVat("1308.41");
//		el.setPriceDiffCash(null);
//		el.setPriceDiffCredit(null);
//		el.setEffectiveDate("24/02/2023 2:10");
//    	el.setExpireDate("");
////		el.setEffectiveDate("18/02/2023 13:10");
////		el.setExpireDate("18/02/2023 14:00");
//		el.setPaymentMethod("CASH");
//		//li.add(el);
//
//		request.setPriceOkList(li);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void saveuploadDTJboss()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR002/save-upload");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"priceOkList\":[{\"productId\":\"793842\",\"company\":\"AWN\",\"priceGroup\":\"EUP\",\"description\":\"\",\"matCode\":\"NEW0SSA013G-BK15\",\"paymentMethod\":\"CASH\",\"vatType\":\"Y\",\"vatRate\":\"7.00\",\"excVat\":\"23364.49\",\"priceVat\":\"1635.51\",\"incVat\":\"25000\",\"effectiveDate\":\"4/3/2023 12:00\",\"editCurPriceFlg\":false,\"selected\":false},{\"productId\":\"793842\",\"company\":\"AWN\",\"priceGroup\":\"DIRECTSALE\",\"description\":\"\",\"matCode\":\"NEW0SSA013G-BK15\",\"paymentMethod\":\"CASH\",\"vatType\":\"Y\",\"vatRate\":\"7.00\",\"excVat\":\"18691.59\",\"priceVat\":\"1308.41\",\"incVat\":\"20000\",\"effectiveDate\":\"4/3/2023 12:00\",\"editCurPriceFlg\":false,\"selected\":false},{\"productId\":\"793842\",\"company\":\"AWN\",\"priceGroup\":\"VIP\",\"description\":\"\",\"matCode\":\"NEW0SSA013G-BK15\",\"paymentMethod\":\"CASH\",\"vatType\":\"Y\",\"vatRate\":\"7.00\",\"excVat\":\"14018.69\",\"priceVat\":\"981.31\",\"incVat\":\"15000\",\"effectiveDate\":\"4/3/2023 12:00\",\"editCurPriceFlg\":false,\"selected\":false}],\"userId\":\"soontarr\",\"testCaseName\":\"SAVE-UPDATE-TDSPR002\"}\r\n"
//				+ "";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void syncPrice()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/sync-price-dt");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\r\n"
//				+ "	\"listProductId\": [\r\n"
//				+ "		\"793842\"\r\n"
//				+ "	],\r\n"
//				+ "	\"listPriceMst\": [\r\n"
//				+ "		{\r\n"
//				+ "			\"company\": \"AWN\",\r\n"
//				+ "			\"priceGroup\": \"EUP\",\r\n"
//				+ "			\"productId\": \"793842\",\r\n"
//				+ "			\"vatType\": \"Y\",\r\n"
//				+ "			\"incVat\": \"25000\",\r\n"
//				+ "			\"excVat\": \"23364.49\",\r\n"
//				+ "			\"vatRate\": \"7.00\",\r\n"
//				+ "			\"vatAmt\": \"1635.51\",\r\n"
//				+ "			\"effectiveDt\": \"04/03/2023 12:00\",\r\n"
//				+ "			\"activeFlg\": \"N\",\r\n"
//				+ "			\"paymentMethod\": \"CASH\",\r\n"
//				+ "			\"created\": \"04/03/2023 11:55:07\",\r\n"
//				+ "			\"createdBy\": \"soontarr\",\r\n"
//				+ "			\"lastUpd\": \"04/03/2023 11:52:51\",\r\n"
//				+ "			\"lastUpdBy\": \"SOONTARR\"\r\n"
//				+ "		},\r\n"
//				+ "		{\r\n"
//				+ "			\"company\": \"AWN\",\r\n"
//				+ "			\"priceGroup\": \"DIRECTSALE\",\r\n"
//				+ "			\"productId\": \"793842\",\r\n"
//				+ "			\"vatType\": \"Y\",\r\n"
//				+ "			\"incVat\": \"20000\",\r\n"
//				+ "			\"excVat\": \"18691.59\",\r\n"
//				+ "			\"vatRate\": \"7.00\",\r\n"
//				+ "			\"vatAmt\": \"1308.41\",\r\n"
//				+ "			\"effectiveDt\": \"04/03/2023 12:00\",\r\n"
//				+ "			\"activeFlg\": \"N\",\r\n"
//				+ "			\"paymentMethod\": \"CASH\",\r\n"
//				+ "			\"created\": \"04/03/2023 11:55:07\",\r\n"
//				+ "			\"createdBy\": \"soontarr\",\r\n"
//				+ "			\"lastUpd\": \"04/03/2023 11:52:51\",\r\n"
//				+ "			\"lastUpdBy\": \"SOONTARR\"\r\n"
//				+ "		},\r\n"
//				+ "		{\r\n"
//				+ "			\"company\": \"AWN\",\r\n"
//				+ "			\"priceGroup\": \"VIP\",\r\n"
//				+ "			\"productId\": \"793842\",\r\n"
//				+ "			\"vatType\": \"Y\",\r\n"
//				+ "			\"incVat\": \"15000\",\r\n"
//				+ "			\"excVat\": \"14018.69\",\r\n"
//				+ "			\"vatRate\": \"7.00\",\r\n"
//				+ "			\"vatAmt\": \"981.31\",\r\n"
//				+ "			\"effectiveDt\": \"04/03/2023 12:00\",\r\n"
//				+ "			\"activeFlg\": \"N\",\r\n"
//				+ "			\"paymentMethod\": \"CASH\",\r\n"
//				+ "			\"created\": \"04/03/2023 11:55:07\",\r\n"
//				+ "			\"createdBy\": \"soontarr\",\r\n"
//				+ "			\"lastUpd\": \"04/03/2023 11:52:51\",\r\n"
//				+ "			\"lastUpdBy\": \"SOONTARR\"\r\n"
//				+ "		}\r\n"
//				+ "	],\r\n"
//				+ "	\"listPriceMstHist\": [\r\n"
//				+ "		{\r\n"
//				+ "			\"company\": \"AWN\",\r\n"
//				+ "			\"priceGroup\": \"EUP\",\r\n"
//				+ "			\"description\": \"zz\",\r\n"
//				+ "			\"productId\": \"793842\",\r\n"
//				+ "			\"vatType\": \"Y\",\r\n"
//				+ "			\"incVat\": \"25000\",\r\n"
//				+ "			\"excVat\": \"23364.49\",\r\n"
//				+ "			\"vatRate\": \"7.00\",\r\n"
//				+ "			\"vatAmt\": \"1635.51\",\r\n"
//				+ "			\"priceDiff\": \"10\",\r\n"
//				+ "			\"priceCredit\": \"10\",\r\n"
//				+ "			\"effectiveDt\": \"04/03/2023 12:00\",\r\n"
//				+ "			\"expireDt\": \"04/03/2023 12:00\",\r\n"
//				+ "			\"activeFlg\": \"N\",\r\n"
//				+ "			\"paymentMethod\": \"CASH\",\r\n"
//				+ "			\"sequence\": \"1\",\r\n"
//				+ "			\"created\": \"04/03/2023 11:52:51\",\r\n"
//				+ "			\"createdBy\": \"SOONTARR\"\r\n"
//				+ "		}\r\n"
//				+ "	]\r\n"
//				+ "}";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void callsyncDataToCloudDTJbossDTJboss()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/util/v1/sync-data-dt-cloud");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{}";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
