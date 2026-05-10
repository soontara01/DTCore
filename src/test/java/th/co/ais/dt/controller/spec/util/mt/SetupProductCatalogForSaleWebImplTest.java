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
//import th.co.ais.dt.controller.dto.CatalogMstBean;
//@Slf4j
//public class SetupProductCatalogForSaleWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void uploadCatalogMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/upload-catalog-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		CatalogMstBean in = new CatalogMstBean();
//		List<CatalogMstBean> catalogMstList = new ArrayList<>();
//		CatalogMstBean el1 = new CatalogMstBean() ;
//		el1.setCatalogType("IOT");
//		el1.setCatalogSubType("Tablet WIFI");
//		el1.setCategory("SPEAKER");
//		el1.setMaterialCode("33015918");
//		el1.setHtmlColor("#D9FAAC");
//		el1.setMarketingName("APPLE AIRPODS PRO WIRELESS MAGSAFE");
//		el1.setVendor("OSKA") ;
//		catalogMstList.add(el1);
//
//		CatalogMstBean el2 = new CatalogMstBean() ;
//		el2.setCatalogType("Gadget");
//		el2.setCatalogSubType("Tablet WiFi-Cellular");
//		el2.setCategory("CHARGER");
//		el2.setMaterialCode("33017770");
//		el2.setHtmlColor("#D9FAAC");
//		el2.setMarketingName("Market Place");
//		el2.setVendor("OSKA") ;
//		catalogMstList.add(el2);
//
//		in.setCatalogMstList(catalogMstList);
//		in.setUserId("soontarr");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void validateUploadCatalogMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/validate-upload-catalog-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		CatalogMstBean in = new CatalogMstBean();
//		List<CatalogMstBean> catalogMstList = new ArrayList<>();
//		CatalogMstBean el1 = new CatalogMstBean() ;
//		el1.setCatalogType("IOT");
//		el1.setCatalogSubType("Tablet WIFI");
//		el1.setCategory("SPEAKER");
//		el1.setMaterialCode("33015918");
//		el1.setHtmlColor("#D9FAAC");
//		el1.setMarketingName("APPLE AIRPODS PRO WIRELESS MAGSAFE");
//		el1.setVendor("OSKA") ;
//		catalogMstList.add(el1);
//
//		CatalogMstBean el2 = new CatalogMstBean() ;
//		el2.setCatalogType("Gadget");
//		el2.setCatalogSubType("Tablet WiFi-Cellular");
//		el2.setCategory("CHARGER");
//		el2.setMaterialCode("33017770");
//		el2.setHtmlColor("#D9FAAC");
//		el2.setMarketingName("Market Place");
//		el2.setVendor("OSKA") ;
//		catalogMstList.add(el2);
//
//		in.setCatalogMstList(catalogMstList);
//		in.setUserId("soontarr");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void editCatalogMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/edit-catalog-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		CatalogMstBean in = new CatalogMstBean();
//		in.setCatalogType("IOT");
//		in.setCatalogSubType("Tablet WIFI");
//		in.setCategory("SPEAKER");
//		in.setMaterialCode("33015918");
//		in.setHtmlColor("#D9FAAC");
//		in.setMarketingName("APPLE AIRPODS PRO WIRELESS MAGSAFE22");
//		in.setVendor("OSKA") ;
//		in.setCatalogId(1l);
//		in.setUserId("soontarr");
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryCatalogMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-catalog-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		CatalogMstBean in = new CatalogMstBean();
//		//in.setCatalogType("IOT");
//		//in.setCatalogSubType("Tablet WIFI");
//		//in.setCategory("SPEAKER");
//		//in.setMaterialCode("33015918");
//		//in.setHtmlColor("#D9FAAC");
//		in.setMarketingName("ar");
//		//in.setVendor("OSKA") ;
//		//in.setUserId("soontarr");
//
//		in.setListCatalogType(new ArrayList<>());
//		in.setListCatalogSubType(new ArrayList<>());
//		in.setListCategory(new ArrayList<>());
//		in.setListMatCode(new ArrayList<>());
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void deleteCatalogMst()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/delete-catalog-mst");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		CatalogMstBean in = new CatalogMstBean();
//		in.setCatalogId(2l);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
