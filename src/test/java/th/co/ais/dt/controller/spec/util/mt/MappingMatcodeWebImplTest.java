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
//import th.co.ais.dt.controller.dto.MappingMatcodeBean;
//@Slf4j
//public class MappingMatcodeWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void uploadMappingMatcode()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/upload-mapping-matcode");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		MappingMatcodeBean in = new MappingMatcodeBean();
//
//		List<MappingMatcodeBean> listMappingMatCode = new ArrayList<>();
//		MappingMatcodeBean el1 = new MappingMatcodeBean();
//		el1.setMatCodeAIS("NEW0SSA013G-BK14");
//		el1.setProductType("DEVICE");
//		el1.setProductSubtype("HANDSET");
//		el1.setBrand("SAMSUNG");
//		el1.setMatCodeSupplier("AZ000001");
//		el1.setMaterialDesc("TESTTT");
//		listMappingMatCode.add(el1);
//
//		MappingMatcodeBean el2 = new MappingMatcodeBean();
//		el2.setMatCodeAIS("NEW0SSA013G-BK15");
//		el2.setProductType("DEVICE");
//		el2.setProductSubtype("HANDSET");
//		el2.setBrand("SAMSUNG");
//		el2.setMatCodeSupplier("AZ000002");
//		el2.setMaterialDesc("TESTTT");
//		listMappingMatCode.add(el2);
//
//		in.setListMappingMatCode(listMappingMatCode);
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
//	public void queryMappingMatcode()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-mapping-matcode");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		MappingMatcodeBean in = new MappingMatcodeBean();
//		List<String> limat= new ArrayList<>();
//		limat.add("NEW0SSA013G-BK14");
//		List<String> limatsub= new ArrayList<>();
//		limatsub.add("AZ000001");
//		in.setListMatCode(limat);
//		in.setListMatCodeSupplier(limatsub);
//        in.setProductType("DEVICE");
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
//	public void validateMappingMatCode()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/validate-mapping-matcode");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//        MappingMatcodeBean in = new MappingMatcodeBean();
//
//		List<MappingMatcodeBean> listMappingMatCode = new ArrayList<>();
//		MappingMatcodeBean el1 = new MappingMatcodeBean();
//		el1.setMatCodeAIS("NEW0SSA013G-BK14");
//		el1.setProductType("DEVICE");
//		el1.setProductSubtype("HANDSET");
//		el1.setBrand("SAMSUNG");
//		el1.setMatCodeSupplier("AZ000001");
//		el1.setMaterialDesc("TESTTT");
//		listMappingMatCode.add(el1);
//
//		in.setListMappingMatCode(listMappingMatCode);
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
//	public void deleteMappingMatcode()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/delete-mapping-matcode");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//        MappingMatcodeBean in = new MappingMatcodeBean();
//
//		List<MappingMatcodeBean> listMappingMatCode = new ArrayList<>();
//		MappingMatcodeBean el1 = new MappingMatcodeBean();
//		el1.setMatCodeAIS("NEW0SSA013G-BK14");
//		el1.setProductType("DEVICE");
//		el1.setProductSubtype("HANDSET");
//		el1.setBrand("SAMSUNG");
//		el1.setMatCodeSupplier("AZ000001");
//		el1.setMaterialDesc("TESTTT");
//		listMappingMatCode.add(el1);
//
//
//
//		in.setListMappingMatCode(listMappingMatCode);
//		in.setUserId("soontarr");
//		in.setEdiId(1L);
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//}
