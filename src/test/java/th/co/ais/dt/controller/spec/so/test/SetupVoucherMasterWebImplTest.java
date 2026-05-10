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
//import th.co.ais.dt.controller.dto.ListSetupVoucherMasterBean;
//import th.co.ais.dt.controller.dto.SetupVoucherMasterBean;
//import th.co.ais.dt.controller.dto.VoucherMovementDTO;
//
//@Slf4j
//public class SetupVoucherMasterWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void insertVoucherMaster1()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/sale/v1/insert-voucher-master");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupVoucherMasterBean in = new SetupVoucherMasterBean();
//		List<ListSetupVoucherMasterBean> listSetupVoucherMasterBean = new ArrayList<>();
//		ListSetupVoucherMasterBean el1 = new ListSetupVoucherMasterBean();
//		el1.setStatus("Complete.");
//		el1.setBirthDay("10/03/2023");
//		el1.setMobileNo("0890349097");
//		el1.setProjectCode("p1");
//		el1.setVoucherNo("BD0889139753150721");
//		el1.setValue("900");
//		el1.setName("noom-test");
//		el1.setExpiredDate("10/10/2023");
//		listSetupVoucherMasterBean.add(el1);
//
//		ListSetupVoucherMasterBean el2 = new ListSetupVoucherMasterBean();
//		el2.setStatus("Complete.");
//		el2.setBirthDay("10/03/2023");
//		el2.setMobileNo("0890349097");
//		el2.setProjectCode("p1");
//		el2.setVoucherNo("Vex1112");
//		el2.setValue("9009");
//		el2.setName("noom-test");
//		el2.setExpiredDate("10/10/2023");
//		listSetupVoucherMasterBean.add(el2);
//
//
//		//in.setListSetupVoucherMasterBean(listSetupVoucherMasterBean) ;
//		in.setUserId("soontarr");
//		in.setStatus("Complete.");
//		in.setBirthDay("10/03/2023");
//		in.setMobileNo("0890349097");
//		in.setProjectCode("Voucher The 1");
//		in.setVoucherNo("BD08891397531507");
//		in.setValue("900");
//		in.setName("noom-test");
//		in.setExpiredDate("10/10/2023");
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
//	public void updateVoucherMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/sale/v1/update-voucher-master");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupVoucherMasterBean in = new SetupVoucherMasterBean();
//
//		in.setUserId("soontarr");
//		in.setStatus("Complete.");
//		in.setBirthDay("10/03/2023");
//		in.setEditMobileNo("0890349098");
//		in.setEditProjectCode("Voucher The 1");
//		in.setEditVoucherNo("BD08891397531507");
//		in.setEditValue("9000");
//		in.setEditName("noom-test");
//		in.setEditExpiredDate("10/10/2023");
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
//	public void deleteVoucherMaster()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/sale/v1/delete-voucher-master");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupVoucherMasterBean in = new SetupVoucherMasterBean();
//		List<VoucherMovementDTO> listEl = new ArrayList<>();
//
//		in.setUserId("soontarr");
//
//		in.setReasonMsg("TESTTTT");
//
//
//		VoucherMovementDTO el1 = new VoucherMovementDTO();
//		el1.setVoucherNo("BD08891397531507") ;
//		listEl.add(el1);
//
//		in.setListVoucherMovementDTO(listEl) ;
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
//	public void checkVoucherNo()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/sale/v1/check-voucher-no");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		SetupVoucherMasterBean in = new SetupVoucherMasterBean();
//		List<ListSetupVoucherMasterBean> listSetupVoucherMasterBean = new ArrayList<>();
//		ListSetupVoucherMasterBean el1 = new ListSetupVoucherMasterBean();
////		el1.setStatus("Complete.");
////		el1.setBirthDay("10/03/2023");
////		el1.setMobileNo("0890349097");
////		el1.setProjectCode("p1");
////		el1.setVoucherNo("BD0889139753150721");
////		el1.setValue("900");
////		el1.setName("noom-test");
////		el1.setExpiredDate("10/10/20in.setUserId("soontarr");
//		el1.setStatus("Complete.");
//		el1.setBirthDay("10/03/2023");
//		el1.setMobileNo("0890349098");
//		el1.setProjectCode("Voucher The 1");
//		el1.setVoucherNo("BD08891397531506");
//		el1.setValue("9000");
//		el1.setName("noom-test");
//		el1.setExpiredDate("10/10/2023");
//
//
//		listSetupVoucherMasterBean.add(el1);
//
//
//
//		in.setListSetupVoucherMasterBean(listSetupVoucherMasterBean) ;
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
//}
