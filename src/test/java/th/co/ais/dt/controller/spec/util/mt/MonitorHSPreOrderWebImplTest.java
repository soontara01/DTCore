//package th.co.ais.dt.controller.spec.util.mt;
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
//public class MonitorHSPreOrderWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void queryResultUploadMonitorHSPreOrder()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-result-upload-monitor-hs-pre-order");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/master-config/v1/query-result-upload-monitor-hs-pre-order");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"fileName\":\"\",\"fromDate\":\"05/07/2023\",\"toDate\":\"07/07/2023\"}";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryDataUploadMonitorHSPreOrder()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-data-monitor-hs-pre-order");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/master-config/v1/query-data-monitor-hs-pre-order");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"fileName\":\"11 Jul 23_Upload_Monitor_Handset_Pre_order    _tanasatc_11072023102006.xlsx\",\"fromDate\":\"11/07/2023\",\"toDate\":\"11/07/2023\"}";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void saveDataMonitorHSPreOrder()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/save-data-monitor-hs-pre-order");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/master-config/v1/save-data-monitor-hs-pre-order");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"fileName\":\"sdsdsdsdqqq\",\"userId\":\"soontarr\",\"userLocation\":\"1100\",\"listMonitorHSPreOrder\":[{\"fileName\":\"07 Jul 23_Upload_Monitor_Handset_Pre_order   _tanasatc_07072023120222_export (2)_soontarr_14072023142713.xlsx\",\"fileNameSeq\":1,\"region\":\"CB\",\"customerGroup\":\"Franchise – TWZ\",\"company\":\"AWN\",\"demandForecastNo\":\"P2306281100000057\",\"period\":\"28/06/2023 23:00:00 - 29/06/2023 22:59:59\",\"customerCode\":\"1100000057\",\"customerName\":\"บจ.ใต้ร่มธรรม คอมมูนิเคชั่นส์\",\"matCode\":\"NEW0SSF721K-BP01\",\"productType\":\"DEVICE\",\"brand\":\"SAMSUNG\",\"model\":\"ZFLIP4_8/128\",\"matDescription\":\"\",\"qty\":\"1\",\"unitQty\":\"เครื่อง\",\"type\":\"Trading\",\"status\":\"สินค้า Shortage : ไม่ทราบกำหนดสินค้าเข้า\",\"allocateQty\":\"0\",\"allocateDate\":\"07/07/2023\"},{\"fileName\":\"07 Jul 23_Upload_Monitor_Handset_Pre_order   _tanasatc_07072023120222_export (2)_soontarr_14072023142713.xlsx\",\"fileNameSeq\":2,\"region\":\"CB\",\"customerGroup\":\"Franchise – AIS\",\"company\":\"AWN\",\"demandForecastNo\":\"P2306281100002911\",\"period\":\"28/06/2023 23:00:00 - 29/06/2023 22:59:59\",\"customerCode\":\"1100002911\",\"customerName\":\"บมจ.เอส พี วี ไอ\",\"matCode\":\"NEW0AP0AR53-SG01I\",\"productType\":\"DEVICE\",\"brand\":\"APPLE\",\"model\":\"AIR5WF_256GB\",\"matDescription\":\"\",\"qty\":\"5\",\"unitQty\":\"เครื่อง\",\"type\":\"Trading\",\"status\":\"สินค้า Shortage : ภายใน 14 Jul 23\",\"allocateQty\":\"0\",\"allocateDate\":\"07/07/2023\"},{\"fileName\":\"07 Jul 23_Upload_Monitor_Handset_Pre_order   _tanasatc_07072023120222_export (2)_soontarr_14072023142713.xlsx\",\"fileNameSeq\":3,\"region\":\"CB\",\"customerGroup\":\"Franchise – AIS\",\"company\":\"AWN\",\"demandForecastNo\":\"P2306281100002911\",\"period\":\"28/06/2023 23:00:00 - 29/06/2023 22:59:59\",\"customerCode\":\"1100002911\",\"customerName\":\"บมจ.เอส พี วี ไอ\",\"matCode\":\"NEW0AP00132-BL01\",\"productType\":\"DEVICE\",\"brand\":\"APPLE\",\"model\":\"IP13_128GB\",\"matDescription\":\"\",\"qty\":\"5\",\"unitQty\":\"เครื่อง\",\"type\":\"Trading\",\"status\":\"สินค้าจัดสรรได้ครบตาม PreOrder\",\"allocateQty\":\"5\",\"allocateDate\":\"07/07/2023\"},{\"fileName\":\"07 Jul 23_Upload_Monitor_Handset_Pre_order   _tanasatc_07072023120222_export (2)_soontarr_14072023142713.xlsx\",\"fileNameSeq\":4,\"region\":\"CB\",\"customerGroup\":\"Franchise – AIS\",\"company\":\"AWN\",\"demandForecastNo\":\"P2306281100002911\",\"period\":\"28/06/2023 23:00:00 - 29/06/2023 22:59:59\",\"customerCode\":\"1100002911\",\"customerName\":\"บมจ.เอส พี วี ไอ\",\"matCode\":\"NEW0AP014P2-DP01\",\"productType\":\"DEVICE\",\"brand\":\"APPLE\",\"model\":\"IP14P_128GB\",\"matDescription\":\"\",\"qty\":\"15\",\"unitQty\":\"เครื่อง\",\"type\":\"Trading\",\"status\":\"สินค้าติดโควต้ารอการ Reallocate\",\"allocateQty\":\"0\",\"allocateDate\":\"07/07/2023\"},{\"fileName\":\"07 Jul 23_Upload_Monitor_Handset_Pre_order   _tanasatc_07072023120222_export (2)_soontarr_14072023142713.xlsx\",\"fileNameSeq\":5,\"region\":\"CB\",\"customerGroup\":\"Franchise – AIS\",\"company\":\"AWN\",\"demandForecastNo\":\"P2306281100002911\",\"period\":\"28/06/2023 23:00:00 - 29/06/2023 22:59:59\",\"customerCode\":\"1100002911\",\"customerName\":\"บมจ.เอส พี วี ไอ\",\"matCode\":\"NEW0AP014P2-BK01\",\"productType\":\"DEVICE\",\"brand\":\"APPLE\",\"model\":\"IP14P_128GB\",\"matDescription\":\"\",\"qty\":\"5\",\"unitQty\":\"เครื่อง\",\"type\":\"Trading\",\"status\":\"สินค้าจัดสรรได้ครบตาม PreOrder\",\"allocateQty\":\"5\",\"allocateDate\":\"07/07/2023\"}]}";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryDataReportHSPreOrder()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-report-hs-pre-order");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/master-config/v1/query-report-hs-pre-order");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		String in = "{\"customerCode\":\"1100002911\",\"fromDate\":\"29/06/2023\",\"toDate\":\"29/06/2023\",\"brand\":\"\",\"model\":\"\"}";
//
//		method.setRequestEntity(new StringRequestEntity(in, "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryProductHSPreOrder()  throws Exception {
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/master-config/v1/query-product-hs-pre-order");
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/master-config/v1/query-product-hs-pre-order");
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
