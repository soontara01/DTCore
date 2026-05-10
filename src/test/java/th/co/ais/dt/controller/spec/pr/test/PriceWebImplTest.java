//package th.co.ais.dt.controller.spec.pr.test;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;
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
//import th.co.ais.dt.controller.dto.QueryPriceBean;
//@Slf4j
//public class PriceWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void queryPriceProductTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/query-price-product");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/setprice/v1/query-company");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		QueryPriceBean in = new QueryPriceBean();
//		in.setProductId("2000000");
//		//in.setMatCode("NW00SIM3PPGMW99");
//		//in.setCompany("AWN");
//		in.setGroupPrice("EUP");
//		in.setPriceDate("21042023 00:01");
//		in.setPriceExpireDate("21042023 23:59");
//		in.setBackwardFlg("");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void getListGroupPriceTest()  throws Exception {
//		GetMethod method = new GetMethod("http://localhost:8090/DTWS/api/price/v1/getGroupPrice?locationCode=1100&saleChannel=BRN");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/setprice/v1/query-company");
//
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//}
