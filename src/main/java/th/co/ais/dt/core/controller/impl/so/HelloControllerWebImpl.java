package th.co.ais.dt.core.controller.impl.so;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import jakarta.mail.MessagingException;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.ConfigLovMasterBean;
import th.co.ais.dt.properties.SpringProfile;
import th.co.ais.dt.queuelib.service.queue.dto.impl.DTReportBatchQueueMessage;
import th.co.ais.dt.queuelib.service.queue.impl.sender.DTQueueSenderService;
import th.co.ais.dt.repository.interfaces.iv.IGrMstDao;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.so.ITestService;
import th.co.ais.dt.service.encryption.config.EncryptionConfig;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.service.mail.impl.EmailSenderService;
import th.co.ais.dt.util.HttpClientUtilDT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Slf4j
@AllArgsConstructor
public class HelloControllerWebImpl  {
	
	final String prefixPath = "api/util/v2"; 
	 
	
	private final ITestService testService ;
	
	private final EncryptionConfig encryptionConfig;

	private final IGrMstDao iGrMstDao;

	private final EmailSenderService emailSenderService;
	
	private final DTQueueSenderService queueSender ;
	
	private final SpringProfile springProfile ;

	@GetMapping("/test")
	public String index() throws MessagingException {

		
//		String dd = dtCoreProperties.getPrivilegeUrl();
//		System.out.println(dtCoreProperties.getPrivilegeUrl());
//		System.out.println(dtCoreProperties.getPrivilegePass());
//		System.out.println(dtCoreProperties.getPrivilegeUser());
//		System.out.println(dtCoreProperties.getSffPass());
//		System.out.println(dtCoreProperties.getSffUrl());
//		System.out.println(dtCoreProperties.getSffUser());
	
		
		//testDao.getData("1", "AAA");
		
//		try {
//			testService.testhibernatesession();
//			//testService.insertDtTest1Stream(null);
//			//testService.getData(null,null);
//			testService.callProc();
//			//testService.getData("11" , "NOOM_11_11");
////			DtTest1 el = new DtTest1();
////			//el.setGdrId(12L);
////			el.setCusName("NOOM_21");
////			el.setAddress("NOOM_A_21");
////			testService.insertDtTest1(el);
//
////			List<Object[]> el = testService.getData_v3("");
////			if(el != null && el.size() > 0) {
////				for(Object[] e : el) {
////					Number id = (Number) e[0];
////					String name = (String) e[1];
////					String address = (String) e[2];
////					System.out.println(id + " " + name + " " + address);
////				}
////			}
//
//		}catch (Exception e) {
//			log.info(e.getMessage());
//		}
//
//		try {
//
//			//testService.getData("11" , "NOOM_11_11");
//
//			//int fff=9/0 ;
//
//		}catch (Exception e) {
//			log.info(e.getMessage());
//			logger.error(e.getMessage());
//
//		}
		EmailForm emailForm = new EmailForm();
		emailForm.setFrom("digitaltrading.app@ais.co.th");
		emailForm.setBody("this is the test email");
		emailForm.setToLists(Arrays.asList("soontarr@ais.co.th","soontara01@gmail.com"));
		emailForm.setSubject("this test subject");
		emailSenderService.sendEmail(emailForm);
		log.info("AAAAAAAAAAAAAAAAAAAAAAAAAA");
		//return iGrMstDao.getGoodsReturnMstByKeyString("52717").get(0).getMobileNoEncrypted();
		return "";
	}


	@RequestMapping(
			  value = prefixPath +"/greetings-with-response-entity",
			  method = RequestMethod.GET, 
			  produces = { "application/json" }
			 
			)
	public ResponseEntity<String> index2() {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
	    return new ResponseEntity<String>("{\"test\": \"Hello with ResponseEntity\"}", httpHeaders, HttpStatus.OK);
	}


	@RequestMapping(
			  value = "/test-post",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	public ResponseEntity<String> testPost(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
	    log.info(in);
	    return new ResponseEntity<String>("{\"test\": \"Hello with ResponseEntity\"}", httpHeaders, HttpStatus.OK);
	}


	@RequestMapping(
			  value = "/test-post2",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	public ResponseEntity<String> testPost2(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
	    log.info(in);
//	    try {
//			//logger.info(InetAddress.getLocalHost().getHostName());
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			log.info(e.getMessage());
//		}
	    return new ResponseEntity<String>("{\"test\": \"Hello with ResponseEntity\"}", httpHeaders, HttpStatus.OK);
	}


	@RequestMapping(
			  value = "/DTWS/api/sale/v1/get-e-receipt",
			  method = RequestMethod.GET, 
			  produces = { "application/json" }
			)
	public ResponseEntity<byte[]> testPdf(@QueryParam("documentNo") String documentNo ,@QueryParam("company") String company ,@QueryParam("documentType") String documentType) {
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_PDF);
	    log.info(documentNo + " " + company + " " + documentType );
	    byte[] decodedBytes = Base64.getDecoder().decode("") ;

	    return new ResponseEntity<byte[]>(decodedBytes, httpHeaders, HttpStatus.OK);
	}
	
	@GetMapping("/send-mail")
	public String sendMail() throws MessagingException {

		String text = "<html>\r\n"
	    		+ "<style>\r\n"
	    		+ "table, th, td {\r\n"
	    		+ " border:1px solid black;\r\n"
	    		+ " width: 500px;\r\n"
	    		+ " text-align: center; }\r\n"
	    		+ "</style>\r\n"
	    		+ "<body>\r\n"
	    		+ "<head>\r\n"
	    		+ "<meta charset=\"UTF-8\">\r\n"
	    		+ "</head>\r\n"
	    		+ "<p>ขอส่งผล ReconcileAAAAAAAAAA e-Receipt ระหว่าง DT & EZ Tax ประจำวันที่ 11/04/2024</p>\r\n"
	    		+ "\r\n"
	    		+ "\r\n"
	    		+ "<table>\r\n"
	    		+ "<tr>\r\n"
	    		+ "<th> DT </th>\r\n"
	    		+ "<th> EZTax(Success) </th>\r\n"
	    		+ "<th> EZTax(Error) </th>\r\n"
	    		+ "</tr>\r\n"
	    		+ "<tr>\r\n"
	    		+ "<td style = \"text-align: center\" >4672</td> <td style = \"text-align: center\" >4672</td> <td style = \"text-align: center\" >0</td> </tr> </table> </body> </html>\r\n"
	    		+ ""; 
		

		EmailForm emailForm = new EmailForm();
		emailForm.setFrom("digitaltrading.app@ais.co.th");
		emailForm.setBody(text);
		emailForm.setToLists(Arrays.asList("soontarr@ais.co.th"));
		emailForm.setCcLists(Arrays.asList("soontara01@gmail.com"));
		emailForm.setSubject("this test subject");
		
//		HttpClientUtilDT http = new HttpClientUtilDT();
//		Map<String, String> properties = new HashMap<String, String>();
//        properties.put("Content-Type", "application/json");
//		
//		
//		String url = "http://localhost:8091/dtreport/api/test/pdf2";
//		String  url2 = "http://localhost:8090/test-post";
//		byte[] out = http.HttpClientFile(properties, url, null, "GET", null);
//		
		
		
		//File f= new File("D:\\data\\tdm_stock_on_hand_all_20240411.tar.gz") ;
    	
//		try {
//			//byte[] bytes = Files.readAllBytes(f.toPath());
//			//byte[] bytes = out.getBytes() ;
//			emailForm.setBytes(out);
//			emailForm.setFileName("tdm_stock_on_hand_all_20240411.pdf") ;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
		//emailSenderService.sendEmail_html_attachment(emailForm);
		emailSenderService.sendEmail_html(emailForm);
		log.info("AAAAAAAAAAAAAAAAAAAAAAAAAA");
	
		return "";
	}
	
	@GetMapping("/sendq")
	//@RegisterReflectionForBinding(DTReportBatchQueueMessage.class)
	public @ResponseBody ResponseEntity<?> sendq() {
		
		DTReportBatchQueueMessage in = new DTReportBatchQueueMessage();
		in.setJobId("noom-666");
		in.setEnv(springProfile.getActive());
		queueSender.sendMessage("dt-report-debug", in).block();
		
		
		queueSender.sendScheduledMessage("dt-report-debug", in, LocalDateTime.now()).block();

		return ResponseEntity.ok().body("ok");

	}




}
