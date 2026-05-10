package th.co.ais.dt.core.controller.impl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.mail.impl.EmailSenderService;


@RestController
@Slf4j
@AllArgsConstructor
public class SendMailFromBatch {
	
	final String prefixPath = "api/send-mail";
	private final EmailSenderService emailSenderService;
	private final IEmailService emailService;
	
	@RequestMapping(value = prefixPath +"/summary-to-pms",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> summaryToPms( @RequestBody String jsonReq)  throws MessagingException {

		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		

		EmailForm emailForm = new EmailForm();
		emailForm.setFrom("digitaltrading.app@ais.co.th");
		emailForm.setBody(jsonReq);
		
		List<Object[]> email = emailService.queryEmail("PMS_RECEIPT_SUMMARY");
		Object[] e = email.get(0);
		
		String to = (String) e[4];
		String sub = (String) e[3];
		
		String[] toArr = to.split(";");
		emailForm.setToLists(new ArrayList<>());
		for(String el : toArr) {
			emailForm.getToLists().add(el);
		}
		
		//emailForm.setToLists(Arrays.asList("soontarr@ais.co.th",""));
		//emailForm.setCcLists(Arrays.asList("soontara01@gmail.com"));
		emailForm.setSubject(sub);
		
		emailSenderService.sendEmail(emailForm) ;
	
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		
	}
	
	@RequestMapping(value = prefixPath +"/pms-tdm-to-sap",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> pmsTdmToSap (@RequestBody String jsonReq )  throws MessagingException {
		//@RequestMapping(value = prefixPath +"/pms-tdm-to-sap",method = RequestMethod.POST, produces = { "application/json" } )
		//public ResponseEntity<String> pmsTdmToSap ( @RequestBody String jsonReq)  throws MessagingException {	

		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		

		EmailForm emailForm = new EmailForm();
		emailForm.setFrom("digitaltrading.app@ais.co.th");
		
		
		List<Object[]> email = emailService.queryEmail("PMS_TDM_TO_SAP");
		Object[] e = email.get(0);
		
		String to = (String) e[4];
		String sub = (String) e[3];
		
		String[] toArr = to.split(";");
		emailForm.setToLists(new ArrayList<>());
		for(String el : toArr) {
			emailForm.getToLists().add(el);
		}
		
		emailForm.setSubject(sub);
		
		List<Object[]> data = emailService.queryPmsTdmToSap() ;
		ByteArrayOutputStream b;
		try {
			b = genReportCsv("COMPANY|LOCATION_CODE|PROCESS_DATE|RECEIPT_DATE|RECEIPT_NO|TDM_DOC_REFERENCE|SAP_DOC_NO|STATUS|ERROR_MESSAGE" , data);
			emailForm.setBytes(b.toByteArray()) ;
		} catch (IOException e1) {
			log.info(e1.getMessage());
			e1.printStackTrace();
		}
		
		
		
		SimpleDateFormat datetext = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
		String runDate = datetext.format(new Date());
		
		String fileName = "Report_interface_TDM_to_SAP_daily_" + runDate + ".csv";
		emailForm.setFileName(fileName);
		emailForm.setBody("Report_interface_TDM_to_SAP_daily");
		
		emailSenderService.sendEmail_html_attachment(emailForm);
	
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		
	}
	
	private ByteArrayOutputStream genReportCsv(String header , List<Object[]> reportData) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(239);
		output.write(187);
		output.write(191);
		CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output,"UTF-8"), CSVFormat.DEFAULT);

		
		//String herderList[] = "COMPANY|LOCATION_CODE|PROCESS_DATE|RECEIPT_DATE|RECEIPT_NO|TDM_DOC_REFERENCE|SAP_DOC_NO|STATUS|ERROR_MESSAGE".split("\\|");
		String herderList[] =header.split("\\|");
		
		StringBuffer HERDER_CSV = new StringBuffer();

		for (int i = 0; i < herderList.length; i++) {
			if (i == herderList.length - 1) {
				
				HERDER_CSV.append( herderList[i]);
			} else {
				HERDER_CSV.append(herderList[i]);
				HERDER_CSV.append(",");
			}

		}

		printer.printRecord(HERDER_CSV.toString());

		for (Object[] object : reportData) {
			StringBuffer DETAIL_CSV = new StringBuffer();

			Object el[] =  object;

			for (int i = 0; i < el.length; i++) {
				if (i == el.length - 1) {
					if (el[i] != null) {
						DETAIL_CSV.append(el[i].toString());
					}
				} else {
					if (el[i] != null) {
						DETAIL_CSV.append(el[i].toString() );
						DETAIL_CSV.append(",");
					} else {
						DETAIL_CSV.append(",");
					}
				}

			}
			printer.printRecord(DETAIL_CSV.toString());
		}

		printer.flush();
		return output;

	}

}
