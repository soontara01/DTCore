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
////import th.co.ais.dt.util.JBossBackEndProperties;
//import th.co.ais.dt.controller.dto.TDSPR001SetupPriceBean;
//import th.co.ais.dt.service.core.dto.SetupPriceResponse;
//
//@Slf4j
//public class TDSPR001SetupPriceWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void queryCriteria()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/TDSPR001/query-criteria");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
//
//		request.setAction("QUERY_CRITERIA");
//		List<String> listCom = new ArrayList<>();
//		listCom.add("AWN");
//		List<String> listMat = new ArrayList<>();
//		listMat.add("NEW0APP1266-SV01");
//		request.setCompany(listCom);
//		//request.setMatCodeList(listMat);
//		//request.setProductType("DEVICE");
//		//request.setProductModel("PRO126_2TB");
//
//
//		//QUERY_CRITERIA
//		//NEW_CRITERIA
//		//PRODUCT_CRITERIA
//		//QUERY_PRODUCT
//		//QUERY_PRODUCT_NEW
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryPrice()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/query-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
//		List<String> listCom = new ArrayList<>();
//		listCom.add("AWN");
//		List<String> listMat = new ArrayList<>();
//		//NW00SIM3PPGOMO99
//		//NW00SIM3PPGMW99
//		listMat.add("NW00SIM3PPGOMO99");
//		List<String> listPriceGroup = new ArrayList<>();
//		listPriceGroup.add("EUP");
//
//
//
//
//
//
//		request.setCompany(listCom);
//		request.setPriceGroupList(listPriceGroup) ;
//		//request.setPaymentMethod("ALL");
//		request.setProductType("SIM");
//		request.setMatCodeList(listMat);
//		request.setEffectiveDate("21/02/2023 10:40") ;
//		request.setExpireDate("21/02/2023 10:45") ;
//		request.setPaymentMethod("CASH");
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void viewPrice()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/view-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
//
//		request.setProductId("");
//
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void checkProduct()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/check-product");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
//		List<String> listCom = new ArrayList<>();
//		listCom.add("AWN");
//
//
//		List<String> listMat = new ArrayList<>();
//		//NW00SIM3PPGOMO99
//		//NW00SIM3PPGMW99
//		//listMat.add("NW00SIM3PPGOMO99");
//		listMat.add("NW00SIM3PPGMW99");
//
//
//		request.setCompany(listCom);
//		request.setMatCodeList(listMat);
//
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void getDataPriceOfProduct()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/get-data-price-of-product");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean request = new TDSPR001SetupPriceBean();
//		List<String> listCom = new ArrayList<>();
//		listCom.add("AWN");
//
//
//		List<String> listMat = new ArrayList<>();
//		//NW00SIM3PPGOMO99
//		//NW00SIM3PPGMW99
//		listMat.add("NW00SIM3PPGOMO99");
//		//listMat.add("NW00SIM3PPGMW99");
//
//		List<String> listPriceGroup = new ArrayList<>();
//		listPriceGroup.add("EUP");
//
//
//		request.setCompany(listCom);
//		request.setMatCodeList(listMat);
//		request.setPriceGroupList(listPriceGroup) ;
//		request.setPaymentMethod("CASH");
//
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void savePrice()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/TDSPR001/save-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean in = new TDSPR001SetupPriceBean();
//		List<String> liCom = new ArrayList<>();
//		liCom.add("AWN");
//
//		List<String> liMat = new ArrayList<>();
//		liMat.add("NEW0SSA013G-BK15");
//		//liMat.add("NW00SIM3PPGMW99");
//
//
//		List<String> liGroupprice = new ArrayList<>();
//		liGroupprice.add("STAFF");
//
//		in.setUserId("soontarr");
//		in.setMatCodeList(liMat);
//		in.setPriceGroupList(liGroupprice);
//		in.setCompany(liCom) ;
//		in.setVatRate("7.00");
//		in.setDescription("TESTT");
//		in.setPriceIncVat("300");
//		in.setPriceDiffCash("50");
//		in.setPriceDiffCredit("30");
//		in.setPaymentMethod("CASH");
//		in.setEffectiveDate("03/03/2023 15:50");
//		//in.setExpireDate("23/02/2023 14:29");
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
//	public void updatePrice()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/price/v1/TDSPR001/update-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean in = new TDSPR001SetupPriceBean();
//		List<String> liCom = new ArrayList<>();
//		liCom.add("AWN");
//
//		List<String> liprice = new ArrayList<>();
//		liprice.add("112");
//
//
//
//
//		in.setUserId("soontarr");
//		in.setPriceId(liprice);
//		//in.setMatCodeList(liMat);
//		//in.setPriceGroupList(liGroupprice);
//		in.setCompany(liCom) ;
//		in.setVatRate("7.00");
//		in.setDescription("TESTT--test");
//		in.setPriceIncVat("500");
//		//in.setPriceDiffCash("50");
//		//in.setPriceDiffCredit("30");
//		//in.setPaymentMethod("CASH");
//		in.setEffectiveDate("3/3/2023 16:00");
//		//in.setExpireDate("23/02/2023 14:42");
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
//	public void deletePrice()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/delete-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean in = new TDSPR001SetupPriceBean();
//
//
//		List<String> liprice = new ArrayList<>();
//		liprice.add("47");
//		liprice.add("1");
//		//liprice.add("76");
//		//liprice.add("77");
//		//liprice.add("78");
//
//		List<String> liGroupprice = new ArrayList<>();
//		liGroupprice.add("DIRECTSALE");
//
//		in.setPriceId(liprice);
//		in.setPriceGroupList(liGroupprice);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void getPriceId()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/get-priceId");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		TDSPR001SetupPriceBean in = new TDSPR001SetupPriceBean();
//		List<SetupPriceResponse> priceForGetPriceIdIn = new ArrayList<>();
//		SetupPriceResponse item = new SetupPriceResponse();
//
//		item.setPriceGroup("DIRECTSALE");
//		item.setProductId("14");
//		item.setPaymentMethod("CASH");
//		item.setEffectiveDate("23/02/2023 20:00");
//		item.setExpireDate("23/02/2023 20:59");
//		//item.setPriceId(pri.getPriceId().toString());
//		priceForGetPriceIdIn.add(item);
//
//		item = new SetupPriceResponse();
//
//		item.setPriceGroup("DIRECTSALE");
//		item.setProductId("14");
//		item.setPaymentMethod("CASH");
//		item.setEffectiveDate("24/02/2023 02:10");
//		item.setExpireDate("");
//		//item.setPriceId(pri.getPriceId().toString());
//		priceForGetPriceIdIn.add(item);
//
//		in.setPriceForGetPriceIdIn(priceForGetPriceIdIn);
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
//	public void savePriceDTJboss()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/save-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"confirm\":false,\"company\":[\"AWN\"],\"matCodeList\":[\"NEW0SSA013G-BK15\"],\"priceGroupList\":[\"STAFF\"],\"paymentMethod\":\"CASH\",\"description\":\"aaaa\",\"priceVatType\":\"Y\",\"priceVate\":\"80.73\",\"priceIncVat\":\"1234.00\",\"priceExcVat\":\"1153.27\",\"vatRate\":\"7.00\",\"priceDiffCash\":\"12\",\"priceDiffCredit\":\"34\",\"effectiveDate\":\"04/03/2023 13:48\",\"expireDate\":\"04/03/2023 15:48\",\"userId\":\"soontarr\",\"testCaseName\":\"SAVE-PRICE-TDSPR001\"}\r\n"
//				+ "";
//
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void updatePriceDTJboss()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/update-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in ="{\"confirm\":false,\"company\":[\"AWN\"],\"paymentMethod\":\"CASH\",\"priceGroupList\":[\"STAFF\"],\"description\":\"aaass\",\"matCodeList\":[\"NEW0SSA013G-BK15\"],\"priceId\":[\"277635\"],\"priceVatType\":\"Y\",\"priceIncVat\":\"32033.00\",\"priceExcVat\":\"18722.43\",\"vatRate\":\"7.00\",\"priceVate\":\"1310.57\",\"priceDiffCash\":\"\",\"priceDiffCredit\":\"\",\"effectiveDate\":\"4/3/2023 11:00\",\"expireDate\":\"\",\"effectiveDateOld\":\"4/3/2023 13:00\",\"expireDateOld\":\"\",\"userId\":\"soontarr\"}\r\n"
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
//	public void deletePriceDTJboss()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/price/v1/TDSPR001/delete-price");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in ="{\"confirm\":false,\"company\":[\"AWN\"],\"paymentMethod\":\"CASH\",\"priceGroupList\":[\"STAFF\"],\"description\":\"aaass\",\"matCodeList\":[\"NEW0SSA013G-BK15\"],\"priceId\":[\"277636\"],\"priceVatType\":\"Y\",\"priceIncVat\":\"32033.00\",\"priceExcVat\":\"18722.43\",\"vatRate\":\"7.00\",\"priceVate\":\"1310.57\",\"priceDiffCash\":\"\",\"priceDiffCredit\":\"\",\"effectiveDate\":\"4/3/2023 11:00\",\"expireDate\":\"\",\"effectiveDateOld\":\"4/3/2023 13:00\",\"expireDateOld\":\"\",\"userId\":\"soontarr\"}\r\n"
//				+ "";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//}
