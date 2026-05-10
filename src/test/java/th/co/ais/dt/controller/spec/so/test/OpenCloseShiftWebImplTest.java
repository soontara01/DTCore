//package th.co.ais.dt.controller.spec.so.test;
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
//import th.co.ais.dt.controller.dto.OpenCloseShiftBean;
//import th.co.ais.dt.controller.dto.OpenCloseShiftPaymentBean;
//import th.co.ais.dt.controller.dto.QueryShiftReq;
//
//@Slf4j
//public class OpenCloseShiftWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void createOpenShift()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/saleout/v1/create-open-shift");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		OpenCloseShiftBean in = new OpenCloseShiftBean();
//		in.setLocationCode("1100");
//		in.setTerminalId("2");
//		in.setShiftNumPopup("1");
//
//		in.setUserId("soontarr");
//		in.setOpenBalPopup("10000");
//		in.setShiftOpenType("O");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void createCloseShift()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/saleout/v1/create-close-shift");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		OpenCloseShiftBean in = new OpenCloseShiftBean();
//		List<OpenCloseShiftPaymentBean> paymentList = new ArrayList<>();
//		OpenCloseShiftPaymentBean el = new OpenCloseShiftPaymentBean();
//		el.setExistsAmount("20000");
//		el.setPayType("CA");
//		el.setBalanceAmount(Double.parseDouble("10000"));
//		el.setReceiptAmount(Double.parseDouble("500"));
//		el.setCreditAmount(Double.parseDouble("0"));
//		el.setCancelCnAmount(Double.parseDouble("0"));
//		el.setWithHoldingTax(Double.parseDouble("0"));
//		el.setCompanyCode("AWN");
//		paymentList.add(el);
//
//		in.setPaymentList(paymentList);
//		in.setUserId("soontarr");
//		in.setShiftNo("11001120230512");
//		in.setTerminalId("1");
//		in.setShiftNum("1");
//		in.setOpenBalance("10000");
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
//	public void queryShiftPayments()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/saleout/v1/query-shift-payments");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		OpenCloseShiftBean in = new OpenCloseShiftBean();
//		in.setShiftId("11001120230512");
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
//	public void queryShift()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/saleout/v1/query-shift");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		QueryShiftReq in = new QueryShiftReq();
//		in.setShiftDtFrom("12/5/2023");
//		in.setShiftDtTo("12/5/2023");
//		in.setShiftStatus("1");
//		in.setLocationCode("1100");
//		in.setUserId("soontarr");
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
//	public void queryShiftDT()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/saleout/v1/query-shift");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		QueryShiftReq in = new QueryShiftReq();
//		in.setShiftDtFrom("10/8/2022");
//		in.setShiftDtTo("10/8/2022");
//		in.setShiftStatus("0");
//		in.setLocationCode("1203");
//		in.setUserId("patco804");
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
