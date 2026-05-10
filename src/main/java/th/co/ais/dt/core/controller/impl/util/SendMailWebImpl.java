package th.co.ais.dt.core.controller.impl.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.ConfigVoucherMasterBean;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.service.mail.impl.EmailSenderService;
import th.co.ais.dt.util.BeanUtil;


@RestController
@Slf4j
@AllArgsConstructor
public class SendMailWebImpl {
	
	final String prefixPath = "api/send-mail";
	private final EmailSenderService emailSenderService;
	private final IEmailService emailService;
	private final LovMasterService lovMasterService;
	
	@RequestMapping(value = prefixPath +"/send-mail-util",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> sendMailUtil( @RequestBody String jsonReq)  throws MessagingException {

		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		

		Gson gson = new Gson();
		EmailForm emailForm = gson.fromJson(jsonReq, EmailForm.class);
		
		if(BeanUtil.isEmpty(emailForm.getToLists()) || BeanUtil.isEmpty(emailForm.getSubject())) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Require toLists, subject\",\"developerMessage\":\"Require toLists, subject\"}", httpHeaders, HttpStatus.OK);
		}

		if(BeanUtil.isNotEmpty(emailForm.getBodyBase64())) {
			byte[] decodedBytes = Base64.getDecoder().decode(emailForm.getBodyBase64());
			String mailBody = new String(decodedBytes, StandardCharsets.UTF_8);
			emailForm.setBody(mailBody);
			
			log.info("Email Subject : " + emailForm.getSubject());
			log.info(mailBody);
			
		}
		if(BeanUtil.isNotEmpty(emailForm.getFileBase64())) {
			emailForm.setBytes(Base64.getDecoder().decode(emailForm.getFileBase64()));
		}
		
		emailSenderService.sendEmailUtil(emailForm);
	
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		
	}
	
	@RequestMapping(value = prefixPath +"/send-mail-config-voucher",method = RequestMethod.POST, produces = { "application/json" } )
	public ResponseEntity<String> sendMailNewVoucher( @RequestBody String jsonReq)  throws MessagingException {

		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		ConfigVoucherMasterBean req = gson.fromJson(jsonReq, ConfigVoucherMasterBean.class);
		
		String mailConfig = "XXX";
		if("NEW".equals(req.getAction())) {
			mailConfig = "NEW_VOUCHER_MAIL";
		}else if("APPROVE".equals(req.getAction())) {
			mailConfig = "APPROVED_VOUCHER_MAIL";
		}
		
		List<Object[]> email = emailService.queryEmail("CONFIG_VOUCHER_MASTER", mailConfig);
		List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria("CONFIG_VOUCHER_MASTER", mailConfig, "Y");
		
		if(BeanUtil.isNotEmpty(email) && BeanUtil.isNotEmpty(listLovMaster)) {
			EmailForm emailForm = new EmailForm();
	        
	        Object[] e = email.get(0);
	        String subject = (String) e[3];
	        //String process_desc = (String) e[2];
	        String to = (String) e[4];
	        String cc = (String) e[5];
	
	        emailForm.setToLists(Arrays.asList(to.split(";")));
	        if(BeanUtil.isNotEmpty(cc)) {
	        	emailForm.setCcLists(Arrays.asList(cc.split(";")));
	        }
	        
	        subject = subject.replaceAll("&projectCode", req.getProjectCode());
	        emailForm.setSubject(subject);
	        
	        String mailContent = listLovMaster.get(0).getLovAttribute01();
	        mailContent = mailContent.replaceAll("&projectCode", req.getProjectCode());
	        mailContent = mailContent.replaceAll("&projectPrefix", req.getProjectPrefix());
	        mailContent = mailContent.replaceAll("&mktUser", req.getMarketingOwner());
	        mailContent = mailContent.replaceAll("&tender", req.getTender());
			
	        emailForm.setBody(mailContent);
	        emailForm.setHtmlFlg("Y");
			emailSenderService.sendEmailUtil(emailForm);
		}
	
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);		
	}
	
}
