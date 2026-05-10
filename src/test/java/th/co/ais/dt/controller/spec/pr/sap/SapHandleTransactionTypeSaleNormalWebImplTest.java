//package th.co.ais.dt.controller.spec.pr.sap;
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
//
//@Slf4j
//public class SapHandleTransactionTypeSaleNormalWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void deletePriceExPired()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/sale-normal");
//		//PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/deposit");
//		//PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/deposit-partner");
//		//PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/prebook-ncp-change-info-order");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"sapTranId\":2166,\"transactionType\":\"14\",\"env\":\"dev\"}";
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//	
//	@Test
//	public void batchHnadleFailed()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/batch-handle-failed");
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
//	public void sync()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/sync-prebook-ncp-change-info-order");
//		//PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/deposit");
//		//PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/deposit-partner");
//		//PostMethod method = new PostMethod("http://localhost:8090/api/sap-handle-transaction-type/v1/prebook-ncp-change-info-order");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		
//		String in = "{\"docNo\":\"4289241000002166\",\"transactionType\":\"59\",\"company\":\"AWN\",\"userId\":\"soontarr\",\"hFSHName1\":\"noom\",\"hFSHName2\":\"noom\",\"hFSHName3\":\"\",\"hFSHName4\":\"\",\"hFSHStreet\":\"\",\"hFSHStreet2\":\"\",\"hFSHStreet3\":\"\",\"hFSHStreet4\":\"\",\"hFSHOtherCity\":\"\",\"hFSHDistrict\":\"\",\"hFSHCity\":\"\",\"hFSHPostalCode\":\"\",\"hFSHRegion\":\"\",\"hFSHCountry\":\"\",\"hFSHTelephone\":\"080000\",\"hFSHCustomerNumber\":\"ONETIME_SHIPTO\"}";
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//}
//
