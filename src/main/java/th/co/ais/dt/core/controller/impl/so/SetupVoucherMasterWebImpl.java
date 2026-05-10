package th.co.ais.dt.core.controller.impl.so;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.entity.so.BirthdayVoucherMovement;
import th.co.ais.dt.entity.so.VoucherMst;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.so.SetupVoucherMasterService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.DateUtils;
import th.co.ais.dt.util.TDMDataUtility;
import th.co.ais.dt.controller.dto.ListSetupVoucherMasterBean;
import th.co.ais.dt.controller.dto.SetupVoucherMasterBean;
import th.co.ais.dt.controller.dto.VoucherMovementDTO;

@RestController
@Slf4j
public class SetupVoucherMasterWebImpl {
	final String prefixPath = "api/sale/v1";
	
	private List<VoucherMovementDTO> listDto = new ArrayList<VoucherMovementDTO>();
	
	private final String NORMAL_STATUS = "N";
	private final String NORMAL = "Normal";
	private final String REDEEM = "Redeemed";
	private final String EXPIRED = "Expired";
	private final String FLAG_Y = "Y";
	private String errorMessage = null; // ********************************************
	
	private final String SAVE_ACTION = "confirmSaveVoucher";
	private final String UPDATE_ACTION = "updateVoucher";

	private final String STATUS_COMPLETE = "Complete.";
	private final String STATUS_FAIL = "Fail.";
	private final String LOAD_STATUS = "LOAD";
	private final String MODIFY_STATUS = "MODIFY";
	private final String DELETE_STATUS = "DELETE";
	private final String LOV_TYPE = "SETUP_CRM_VOUHCER";
	private final String LOV_SUB_TYPE = "STATUS";
	private final String MSG_DELETE_S = "Delete Complete.";
	private final String MSG_DELETE_N = "Delete Fail.";
	
	
	private final LovMasterService lovMasterService ;
	
	private final SetupVoucherMasterService setupVoucherMasterService ;
	
	public SetupVoucherMasterWebImpl(LovMasterService lovMasterService,SetupVoucherMasterService setupVoucherMasterService) {
		this.lovMasterService = lovMasterService ;
		this.setupVoucherMasterService = setupVoucherMasterService;
	}
	
	@RequestMapping(
			  value = prefixPath +"/update-voucher-master",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SetupVoucherMasterBean.class)
	public ResponseEntity<String> updateVoucherMaster(@RequestBody String jsonRequest) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		SetupVoucherMasterBean request = new SetupVoucherMasterBean();
		SetupVoucherMasterBean response = new SetupVoucherMasterBean();
		try {
			log.info(jsonRequest);
			request = gson.fromJson(jsonRequest, SetupVoucherMasterBean.class);
			if(validateUpdate(request)){
				this.updateVoucher(request,request.getUserId());
			}else {
				if(StringUtils.isNotEmpty(errorMessage)) {
					request.setErrorMessage(errorMessage);
					response.setResultCode("5000");
					response.setDeveloperMessage(request.getErrorMessage());
					response.setResultDescription(request.getErrorMessage());	
					return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				}
			}
		}catch (Exception e){
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Update Voucher Error.\",\"developerMessage\":\"Update Voucher Error.\"}", httpHeaders, HttpStatus.OK);

		}
		response.setResultCode("20000");
		response.setDeveloperMessage("success");
		response.setResultDescription("success");	
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	private boolean validateUpdate(SetupVoucherMasterBean setupVoucherMasterBean) {

		int i=0;
		errorMessage = null;
		BigDecimal number = new BigDecimal(i);
		List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria("V1_SETUP_VOUCHER_MASTER", "IGNORE_MOBILE_NO", null, setupVoucherMasterBean.getEditProjectCode(), null, "Y", null, null, null, null, null, null);

		if(BeanUtil.isEmpty(listLovMaster)){
			if (StringUtils.isEmpty(setupVoucherMasterBean.getEditMobileNo())) {
				errorMessage = "Please Input Mobile No for " + setupVoucherMasterBean.getEditProjectCode();
				return false;
			}
		}
		if (StringUtils.isNotEmpty(setupVoucherMasterBean.getEditMobileNo())) {
			String regexp = "[0][0-9]*{9}";
			if(setupVoucherMasterBean.getEditMobileNo().matches(regexp)){
				if (setupVoucherMasterBean.getEditMobileNo().length() == 10) {
					//setupVoucherMasterBean.seteMobileNo("");
				}else {
					errorMessage = "Please input mobile no. (10 digits)";
					return Boolean.FALSE;
				}
			} else {		
				errorMessage = "Please insert format mobile no 0XXXXXXXXX";
				return Boolean.FALSE;
			}
		}

		if (!StringUtils.isNotEmpty(setupVoucherMasterBean.getEditVoucherNo())) {
			errorMessage = "Please Input Voucher No";
			return Boolean.FALSE;
		}
		if (setupVoucherMasterBean.getEditValue().equals(number) || setupVoucherMasterBean.getEditValue().equals(null)) {
			errorMessage="Please Input Value";	
			return Boolean.FALSE;
		}
		if (!(setupVoucherMasterBean.getEditValue().equals(number) || setupVoucherMasterBean.getEditValue().equals(null))) {
			//setupVoucherMasterBean.seteValue("");
		}
		if (BeanUtil.isNotEmpty(setupVoucherMasterBean.getEditExpiredDate())){
			Date currDate = DateUtils.toDate(new Date());
			if (DateUtils.compareDate(new Date(setupVoucherMasterBean.getEditExpiredDate()), currDate) < 0){			
				errorMessage="Please Insert Expire date not current Date";
				return Boolean.FALSE;
			}else {
				//setupVoucherMasterBean.seteExpireDate("");
			}
		}else if (BeanUtil.isEmpty(setupVoucherMasterBean.getEditExpiredDate())) {	
			errorMessage="Please Select Expire Date";	
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	private void updateVoucher(SetupVoucherMasterBean setupVoucherMasterBean,String userId){

		VoucherMst vmt = setupVoucherMasterService.getVoucherMstByCriteria(setupVoucherMasterBean.getEditVoucherNo(), null, setupVoucherMasterBean.getEditProjectCode());
		vmt.setMobileNo(setupVoucherMasterBean.getEditMobileNo());
		vmt.setName(setupVoucherMasterBean.getEditName());
		vmt.setExpiredDt(new Date(setupVoucherMasterBean.getEditExpiredDate()));
		vmt.setValue(new BigDecimal(setupVoucherMasterBean.getEditValue()));
		vmt.setBirthMonth(setupVoucherMasterBean.getEditBirthMonth());
		vmt.setBirthYear(setupVoucherMasterBean.getEditBirthYear());
		vmt.getCreateValue().setLastUpd(new Date());
		vmt.getCreateValue().setLastUpdBy(userId);
		try{
			setupVoucherMasterService.updateVoucherMst(vmt);
			BirthdayVoucherMovement checkMvt = setupVoucherMasterService.checkVoucherMvt(vmt.getVoucherNo());
			if (null != checkMvt) {
				Long seq = 0L;
				seq = checkMvt.getSeqVoucher();
				seq = seq + 1;
				List<BirthdayVoucherMovement> listMvt = new ArrayList<BirthdayVoucherMovement>();
				BirthdayVoucherMovement voucherMvt = new BirthdayVoucherMovement();
				List<LovMaster> lovStatus = lovMasterService.listLovMasterByCriteria(LOV_TYPE, LOV_SUB_TYPE, MODIFY_STATUS, null, null, null, null, null, null, null, null, null);

				voucherMvt.setSeqVoucher(seq);
				voucherMvt.setVoucherNo(vmt.getVoucherNo());
				voucherMvt.setProjectCode(vmt.getProjectCode());
				voucherMvt.setMobileNo(vmt.getMobileNo());
				voucherMvt.setMovementType(lovStatus.get(0).getLovCode());
				voucherMvt.setMovementDate(new Date());
				voucherMvt.setCreateValue(new MasterValue());
				voucherMvt.getCreateValue().setCreated(new Date());
				voucherMvt.getCreateValue().setCreatedBy(userId);
				voucherMvt.getCreateValue().setLastUpd(new Date());
				voucherMvt.getCreateValue().setLastUpdBy(userId);
				listMvt.add(voucherMvt);
				Boolean saveCriteriaMvt = setupVoucherMasterService.insertInsuranVoucherMovement(listMvt);
				if (!saveCriteriaMvt){
					errorMessage="fail";	
				}

			}else {
				errorMessage = "checkVoucherMvt --> DATA_NOT_FOUND";
			}
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	
	@RequestMapping(
			  value = prefixPath +"/insert-voucher-master",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SetupVoucherMasterBean.class)
	public ResponseEntity<String> insertVoucherMaster(@RequestBody String jsonRequest) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		

		Gson gson = new Gson();
		SetupVoucherMasterBean request = new SetupVoucherMasterBean();
		SetupVoucherMasterBean response = new SetupVoucherMasterBean();
		SetupVoucherMasterBean saveData = new SetupVoucherMasterBean(); 
		try {
			request = gson.fromJson(jsonRequest, SetupVoucherMasterBean.class);
			
			if(BeanUtil.isNotEmpty(request.getListSetupVoucherMasterBean())){
				for(ListSetupVoucherMasterBean listDto : request.getListSetupVoucherMasterBean()) {
					if(listDto.getStatus().equalsIgnoreCase(STATUS_COMPLETE)) {
						if (BeanUtil.isNotEmpty(listDto.getBirthDay())) {
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(listDto.getBirthDay());  
							String bDate = df.format(date1);
							String[] birthday = bDate.split("/");
							String birthM;
							String birthY;
							birthM = birthday[1];
							birthY = birthday[2];
							saveData.setBirthMonth(birthM);
							saveData.setBirthYear(birthY);
						}
						saveData.setMobileNo(listDto.getMobileNo());
						saveData.setProjectCode(listDto.getProjectCode());
						saveData.setVoucherNo(listDto.getVoucherNo());
						saveData.setValue(listDto.getValue());
						saveData.setName(listDto.getName());
						saveData.setExpiredDate(listDto.getExpiredDate());
						saveData.setUserId(request.getUserId());
						if (!this.insertInsuranceVoucher(saveData)) {
							errorMessage = "Save Uncomplete";
						}else{
							errorMessage = "Save Complete";
							response.setResultCode("20000");
							response.setDeveloperMessage(errorMessage);
							response.setResultDescription(errorMessage);	
							
						}
					}	
					}
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
				}
			
			
			if (BeanUtil.isNotEmpty(request.getBirthDay())) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(request.getBirthDay());  
				String bDate = df.format(date1);
				String[] birthday = bDate.split("/");
				String birthM;
				String birthY;
				birthM = birthday[1];
				birthY = birthday[2];
				request.setBirthMonth(birthM);
				request.setBirthYear(birthY);
			}
			if (this.validateError(request, SAVE_ACTION)) {
				log.info(errorMessage);
				if (!this.insertInsuranceVoucher(request)) {
					errorMessage = "Save Uncomplete";
				}else {
					errorMessage = "Save Complete";
					response.setResultCode("20000");
					response.setDeveloperMessage(errorMessage);
					response.setResultDescription(errorMessage);	
					return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
				}
			}	
		}catch(Exception e){
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+ errorMessage +"\",\"developerMessage\":\"Save Uncomplete.\"}", httpHeaders, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\""+ errorMessage +"\",\"developerMessage\":\"Save Uncomplete.\"}", httpHeaders, HttpStatus.OK);

	}
	
	private boolean insertInsuranceVoucher(SetupVoucherMasterBean request){

		boolean ret = false;
		List<VoucherMst> voucherMsts = new ArrayList<VoucherMst>();
		VoucherMst mst = new VoucherMst();
		try {
			mst.setBirthMonth(request.getBirthMonth());
			mst.setBirthYear(request.getBirthYear());
			mst.setMobileNo(request.getMobileNo());
			mst.setName(request.getName());
			mst.setVoucherNo(request.getVoucherNo());
			mst.setValue(new BigDecimal(request.getValue()));
			mst.setProjectCode(request.getProjectCode());
			mst.setStatus(NORMAL_STATUS);
			Date expiredDate = new SimpleDateFormat("dd/MM/yyyy").parse(request.getExpiredDate());  
			mst.setExpiredDt(expiredDate);
			mst.setCreateValue(new MasterValue());
			mst.getCreateValue().setCreated(new Date());
			mst.getCreateValue().setCreatedBy(request.getUserId());
			mst.getCreateValue().setLastUpd(new Date());
			mst.getCreateValue().setLastUpdBy(request.getUserId());
			voucherMsts.add(mst);
		Boolean saveCriteriaMst = setupVoucherMasterService.manageInsuranceVoucherMst(voucherMsts, null, null);
		if (saveCriteriaMst.equals(false)) {
			return Boolean.FALSE;
		} else {
			//final LovMaster lovStatus = setupVoucherMasterService.getLovMasterByUnique(LOV_TYPE, LOV_SUB_TYPE, LOAD_STATUS);
			List<LovMaster> lovStatus = lovMasterService.listLovMasterByCriteria(LOV_TYPE, LOV_SUB_TYPE, MODIFY_STATUS, null, null, null, null, null, null, null, null, null);
			List<BirthdayVoucherMovement> listMvt = new ArrayList<BirthdayVoucherMovement>();
			BirthdayVoucherMovement voucherMvt = new BirthdayVoucherMovement();
			voucherMvt.setSeqVoucher(1L);
			voucherMvt.setVoucherNo(request.getVoucherNo());
			voucherMvt.setProjectCode(request.getProjectCode());
			voucherMvt.setMobileNo(request.getMobileNo());
			voucherMvt.setMovementType(lovStatus.get(0).getLovCode());
			voucherMvt.setMovementDate(new Date());
			voucherMvt.setCreateValue(new MasterValue());
			voucherMvt.getCreateValue().setCreated(new Date());
			voucherMvt.getCreateValue().setCreatedBy(request.getUserId());
			voucherMvt.getCreateValue().setLastUpd(new Date());
			voucherMvt.getCreateValue().setLastUpdBy(request.getUserId());
			listMvt.add(voucherMvt);
			Boolean saveCriteriaMvt = setupVoucherMasterService.insertInsuranVoucherMovement(listMvt);
			if (saveCriteriaMvt.equals(true)){
				ret = true;
			} else {
				ret = false;
			}

		}
		}catch (Exception e) {
			log.info(e.getMessage());
		}
		return ret;
	}
	
	private boolean validateError(SetupVoucherMasterBean setupVoucherMasterBean, String action){

		int i=0;
		BigDecimal number = new BigDecimal(i);
		
		try {
		
		//Config Require Mobile by ProjectCode
		//List<LovMaster> listLovMaster = setupVoucherMasterService.listLovMastersByCriteria("V1_SETUP_VOUCHER_MASTER", "IGNORE_MOBILE_NO", null, setupVoucherMasterBean.getProjectCode(),FLAG_Y, null, null, null, null);
		List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria("V1_SETUP_VOUCHER_MASTER", "IGNORE_MOBILE_NO", null, setupVoucherMasterBean.getProjectCode(), null, "Y", null, null, null, null, null, null);
		if(BeanUtil.isEmpty(listLovMaster)){
			
			if (StringUtils.isEmpty(setupVoucherMasterBean.getMobileNo())) {		
				errorMessage = "Please Input Mobile No for " + setupVoucherMasterBean.getProjectCode();
				return Boolean.FALSE;
			}
			
		}
		
		if (StringUtils.isNotEmpty(setupVoucherMasterBean.getMobileNo())) {
			String regexp = "[0][0-9]*{9}";
			if(setupVoucherMasterBean.getMobileNo().matches(regexp)){
				if (setupVoucherMasterBean.getMobileNo().length() == 10) {
					errorMessage="";
				}else {		
					errorMessage= "Please input mobile no. (10 digits)";
					return Boolean.FALSE;
				}
			} else {
				
				errorMessage = "Please insert format mobile no 0XXXXXXXXX";
				return Boolean.FALSE;
			}
		}
		
		if(action.equals(SAVE_ACTION))
		{
			if (StringUtils.isNotEmpty(setupVoucherMasterBean.getVoucherNo())) {
				if(16 == setupVoucherMasterBean.getVoucherNo().length()) {
					//String chkVocherNo = setupVoucherMasterService.checkVoucherNo(setupVoucherMasterBean.getVoucherNo());
					VoucherMst chkVocherNo = setupVoucherMasterService.getVoucherMstByCriteria(setupVoucherMasterBean.getVoucherNo(), null, null);

					if (null != chkVocherNo) {
						errorMessage="Voucher No is already in use, Please input again";
						return Boolean.FALSE;
					}else {
						errorMessage = "";
					}
				}else {
			
					errorMessage="Please input voucher no. (16 digits)";
					return Boolean.FALSE;
				}
			}
			if (!StringUtils.isNotEmpty(setupVoucherMasterBean.getVoucherNo())) {
						
				errorMessage="Please Input Voucher No";
				return Boolean.FALSE;
			}
			if (BeanUtil.isNotEmpty(setupVoucherMasterBean.getBirthDay())){
				Date currDate = DateUtils.toDate(new Date());
			    if (DateUtils.compareDate(new SimpleDateFormat("dd/MM/yyyy").parse(setupVoucherMasterBean.getBirthDay()), currDate) >= 0){		
					errorMessage="Please Insert Birth Day not current Date and Future date";
			    	return Boolean.FALSE;
			    }else {
			    	errorMessage="";
			    }
			}
			if (setupVoucherMasterBean.getValue().equals(number) || setupVoucherMasterBean.getValue().equals(null)) {
				errorMessage="Please Input Value";
				return Boolean.FALSE;
			}if (!(setupVoucherMasterBean.getValue().equals(number) || setupVoucherMasterBean.getValue().equals(null))) {
				errorMessage="";
			}
			if (BeanUtil.isNotEmpty(setupVoucherMasterBean.getExpiredDate())){
				Date currDate = DateUtils.toDate(new Date());
			    if (DateUtils.compareDate(new SimpleDateFormat("dd/MM/yyyy").parse(setupVoucherMasterBean.getExpiredDate()), currDate) <= 0){	
					errorMessage="Please Insert Expire date not current Date";
			    	return Boolean.FALSE;
			    }else {
			    	errorMessage ="";
			    }
			}else if (BeanUtil.isEmpty(setupVoucherMasterBean.getExpiredDate())) {
					
				errorMessage="Please Select Expire Date";
		    	return Boolean.FALSE;
			}
		}
		if (!StringUtils.isNotEmpty(setupVoucherMasterBean.getVoucherNo())) {			
			errorMessage="Please Input Voucher No";
			return Boolean.FALSE;
		}
		if (BeanUtil.isNotEmpty(setupVoucherMasterBean.getBirthDay())){
			Date currDate = DateUtils.toDate(new Date());
		    if (DateUtils.compareDate(new SimpleDateFormat("dd/MM/yyyy").parse(setupVoucherMasterBean.getBirthDay()), currDate) >= 0){		
				errorMessage="Please Insert Birth Day not current Date and Future date";
		    	return Boolean.FALSE;
		    }else {
		    	errorMessage = "";
		    }
		}
		if (setupVoucherMasterBean.getValue().equals(number) || setupVoucherMasterBean.getValue().equals(null)) {			
			errorMessage="Please Input Value";
			return Boolean.FALSE;
		}if (!(setupVoucherMasterBean.getValue().equals(number) || setupVoucherMasterBean.getValue().equals(null))) {
			errorMessage = "";
		}
		if (setupVoucherMasterBean.getExpiredDate() != null){
			Date currDate = DateUtils.toDate(new Date());
		    if (DateUtils.compareDate(new SimpleDateFormat("dd/MM/yyyy").parse(setupVoucherMasterBean.getExpiredDate()), currDate) <= 0){
		    		
				errorMessage="Please Insert Expire date not current Date";
		    	return Boolean.FALSE;
		    }else {
		    	errorMessage = "";
		    }
		}else if (setupVoucherMasterBean.getExpiredDate() == null) {		
			errorMessage= "Please Select Expire Date";
	    	return Boolean.FALSE;
		}
		}catch (Exception e) {
		return Boolean.TRUE;
		}	
		return Boolean.TRUE;
		}
	
	@RequestMapping(
			  value = prefixPath +"/delete-voucher-master",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SetupVoucherMasterBean.class)
	public ResponseEntity<String> deleteVoucherMaster(@RequestBody String jsonRequest) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		


		Gson gson = new Gson();
		SetupVoucherMasterBean request = new SetupVoucherMasterBean();
		SetupVoucherMasterBean response = new SetupVoucherMasterBean();
		try {
			log.info(jsonRequest);
			request = gson.fromJson(jsonRequest, SetupVoucherMasterBean.class);
			this.deleteVoucher(request.getListVoucherMovementDTO(),request.getUserId(),request.getReasonMsg());

			if(errorMessage == MSG_DELETE_N){
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"delete Voucher Error.\",\"developerMessage\":\"delete Voucher Error.\"}", httpHeaders, HttpStatus.OK);
				
			}
			if(errorMessage == MSG_DELETE_S){
				response.setResultCode("20000");
				response.setDeveloperMessage("success");
				response.setResultDescription("success");	
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);
			}

		}catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"delete Voucher Error.\",\"developerMessage\":\"delete Voucher Error.\"}", httpHeaders, HttpStatus.OK);
		}

		return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"delete Voucher Error.\",\"developerMessage\":\"delete Voucher Error.\"}", httpHeaders, HttpStatus.OK);

	
	}
	
	public boolean deleteVoucher(List<VoucherMovementDTO> listDTO,String userId, String reasonMsg){

		Long seq = 0L;
		boolean delInsuranceVoucherMst = false;
		boolean insertMov = false;
		List<VoucherMovementDTO> vmdList = listDTO;
		List<VoucherMst> deleteList = new ArrayList<VoucherMst>();
		List<BirthdayVoucherMovement> bvmList = new ArrayList<BirthdayVoucherMovement>();
		VoucherMst delVM = new VoucherMst();
		if (this.validateAndShowErrorMessage(vmdList)) {
			for(VoucherMovementDTO vm:vmdList){
				delVM = new VoucherMst();
				BirthdayVoucherMovement checkMvt = setupVoucherMasterService.checkVoucherMvt(vm.getVoucherNo());
				if (null != checkMvt) {
					seq = checkMvt.getSeqVoucher();
					seq = seq + 1;
					BirthdayVoucherMovement voucherMvt = new BirthdayVoucherMovement();
					List<LovMaster> lovStatus = lovMasterService.listLovMasterByCriteria(LOV_TYPE, LOV_SUB_TYPE, DELETE_STATUS, null, null, null, null, null, null, null, null, null);

					//final LovMaster lovStatus = setupVoucherMasterService.getLovMasterByUnique(LOV_TYPE, LOV_SUB_TYPE, DELETE_STATUS);
					voucherMvt.setSeqVoucher(seq);
					voucherMvt.setVoucherNo(checkMvt.getVoucherNo());
					voucherMvt.setProjectCode(checkMvt.getProjectCode());
					voucherMvt.setMobileNo(checkMvt.getMobileNo());
					voucherMvt.setMovementType(lovStatus.get(0).getLovCode());
					voucherMvt.setMovementDate(new Date());
					voucherMvt.setCreateValue(new MasterValue());
					voucherMvt.getCreateValue().setCreated(new Date());
					voucherMvt.getCreateValue().setCreatedBy(userId);
					voucherMvt.getCreateValue().setLastUpd(new Date());
					voucherMvt.getCreateValue().setLastUpdBy(userId);
					voucherMvt.setDeleteReason(reasonMsg);
					bvmList.add(voucherMvt);
				}
				delVM.setVoucherNo(vm.getVoucherNo());
				deleteList.add(delVM);
			}
			try{
				delInsuranceVoucherMst = setupVoucherMasterService.manageInsuranceVoucherMst(null, deleteList, null);
				insertMov = setupVoucherMasterService.insertInsuranVoucherMovement(bvmList);
			}catch(Exception e){
				delInsuranceVoucherMst = false;
				insertMov = false;
			}
			if(delInsuranceVoucherMst && insertMov){					
				errorMessage = MSG_DELETE_S;
			}
			else{	
				errorMessage = MSG_DELETE_N;
			}
			//int currentPageBfore = monitorBirthdayVoucherBean.getCurrentPage();
			try{
				//this.searchVoucher(false);
				//int currentPageAfter = monitorBirthdayVoucherBean.getTotalPage();
				//if(currentPageBfore<=currentPageAfter){
				//	monitorBirthdayVoucherBean.setCurrentPage(currentPageBfore);
				//}
			}catch(Exception e){

			}
			//this.setMonitorBirthdayVoucherBean(monitorBirthdayVoucherBean);
			//this.resetCheckBox();
			return true;
		}
		return false;
	}
	
	private boolean validateAndShowErrorMessage(List<VoucherMovementDTO> list) {
		if (TDMDataUtility.isEmpty(list)) {
			errorMessage = "Selected List null";
			return false;
		}
		return true;
	}
	
	@RequestMapping(
			  value = prefixPath +"/query-voucher-master",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SetupVoucherMasterBean.class)
	public ResponseEntity<String> searchVoucherMaster(@RequestBody String jsonRequest) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		SetupVoucherMasterBean request = new SetupVoucherMasterBean();
		SetupVoucherMasterBean response = new SetupVoucherMasterBean();
		try {
			log.info(jsonRequest);
			try {
				request = gson.fromJson(jsonRequest, SetupVoucherMasterBean.class);
				response = this.searchVoucher(request);

				if((null==response.getListVoucherMovementDTO() || response.getListVoucherMovementDTO().size()==0)){
					return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);
						}
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);	
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);
		}
		response.setResultCode("20000");
		response.setDeveloperMessage("success");
		response.setResultDescription("success");
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);	
	}
	
	private SetupVoucherMasterBean searchVoucher(SetupVoucherMasterBean bean){
	
		//listDto = setupVoucherMasterService.listCurrentVoucherMovement(dto);	//TODO
		if((null==listDto || listDto.size()==0)){
			log.info("listDTO = null");
		} else {
			Date currDate = DateUtils.toDate(new Date());
			for(VoucherMovementDTO dto : listDto){
				if(dto.getStatus().equals(NORMAL_STATUS)){
					if(DateUtils.compareDate(dto.getExpiredDate(), currDate) < 0){
						//check expired date
						dto.setStatus(EXPIRED);
						dto.setEnableEdit(false);
						//ห้ามedit
					} else {
						dto.setStatus(NORMAL);
						dto.setEnableEdit(true);
						//editได้
					}
				} else {
					dto.setStatus(REDEEM);
					dto.setEnableEdit(false);
					//ห้ามedit
				}
			}
		}
		bean.setListVoucherMovementDTO(listDto);
		return bean;
	}
	
	@RequestMapping(
			  value = prefixPath +"/check-voucher-no",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(SetupVoucherMasterBean.class)
	public ResponseEntity<String> checkVoucherNo(@RequestBody String jsonRequest) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		ProcessResult validdata = new ProcessResult(true,null,null);
		Gson gson = new Gson();
		String result = null;
		SetupVoucherMasterBean request = new SetupVoucherMasterBean();
		SetupVoucherMasterBean response = new SetupVoucherMasterBean();
		List<ListSetupVoucherMasterBean> listVoucher = new ArrayList<ListSetupVoucherMasterBean>();
		try {
			request = gson.fromJson(jsonRequest, SetupVoucherMasterBean.class);
			
			//forUploadData
			if(BeanUtil.isNotEmpty(request.getListSetupVoucherMasterBean())) {
				Map<String,String> mapVoucherNo = new HashMap();
				Long limitUpload = 2000L; // Default
				// Check Limit upload
				List<LovMaster> lovMasters = lovMasterService.listLovMasterByCriteria("SETUP_VOUCHER_MASTER", "LIMIT_UPLOAD", null, null, null, "Y", null, null, null, null, null, null);

				//List<LovMaster> lovMasters =  setupVoucherMasterService.listLovMastersByCriteria("SETUP_VOUCHER_MASTER", "LIMIT_UPLOAD", null, null, "Y", null, null, null, null);
				if(BeanUtil.isNotEmpty(lovMasters)) {
					limitUpload =  Long.parseLong(lovMasters.get(0).getLovVal());
				}
				if(request.getListSetupVoucherMasterBean().size() > limitUpload) {
					response.setResultCode("50000");
					response.setDeveloperMessage("Config limit upload is " + limitUpload);
					response.setResultDescription("Config limit upload is " + limitUpload);	
					return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				}
				for(ListSetupVoucherMasterBean listDto : request.getListSetupVoucherMasterBean()) {
					ListSetupVoucherMasterBean LsVoucher = new ListSetupVoucherMasterBean();
					
					LsVoucher.setProjectCode(request.getProjectCode());
					LsVoucher.setName(listDto.getName());
					LsVoucher.setBirthDay(listDto.getBirthDay());
					LsVoucher.setMobileNo(listDto.getMobileNo());
					LsVoucher.setVoucherNo(listDto.getVoucherNo());
					LsVoucher.setValue(listDto.getValue());
					LsVoucher.setExpiredDate(listDto.getExpiredDate());
					LsVoucher.setStatus(STATUS_COMPLETE);
					//validateUpload
					validdata = validateInputData(listDto,request.getProjectCode());
					if(!validdata.isSuccess()) {
						LsVoucher.setStatus(STATUS_FAIL);
						LsVoucher.setStatusMsg(validdata.getMessage());
					}
					//voucherNo
					//result = setupVoucherMasterService.checkVoucherNo(listDto.getVoucherNo());
					VoucherMst vmt = setupVoucherMasterService.getVoucherMstByCriteria(listDto.getVoucherNo(), null, null);
					//if(StringUtils.isNotEmpty(result)) {
					if(vmt != null) {
						LsVoucher.setStatus(STATUS_FAIL);
						LsVoucher.setStatusMsg("Duplicate Voucher No.");
					}
					
					if(mapVoucherNo.containsKey(listDto.getVoucherNo())) {
						LsVoucher.setStatus(STATUS_FAIL);
						LsVoucher.setStatusMsg("Duplicate Voucher No.");
					} else {
						if(BeanUtil.isNotEmpty(listDto.getVoucherNo())) {
							mapVoucherNo.put(listDto.getVoucherNo(), listDto.getVoucherNo());						
						}
					}
					
					listVoucher.add(LsVoucher);
				}
				response.setListSetupVoucherMasterBean(listVoucher);
				response.setResultCode("20000");
				response.setDeveloperMessage("success");
				response.setResultDescription("Check Voucher Upload Complete");	
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				
			}
			
			//forAddData
			if(StringUtils.isNotEmpty(request.getVoucherNo())) {
				//result = setupVoucherMasterService.checkVoucherNo(request.getVoucherNo());
				VoucherMst vmt = setupVoucherMasterService.getVoucherMstByCriteria(request.getVoucherNo(), null, null);
				//if(StringUtils.isNotEmpty(result)) {
				if(vmt != null) {
					response.setVoucherNo(result);
					response.setResultCode("20000");
					response.setDeveloperMessage("success");
					response.setResultDescription("Voucher No is already in use, Please input again.");	
					return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

				}else {
					return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"result empty\",\"developerMessage\":\"result empty\"}", httpHeaders, HttpStatus.OK);
				}
			}else{
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Voucher No is empty, Please input again.\",\"developerMessage\":\"Voucher No is empty, Please input again.\"}", httpHeaders, HttpStatus.OK);
	
			}
		}catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Error Check Voucher No.\",\"developerMessage\":\"Error Check Voucher No.\"}", httpHeaders, HttpStatus.OK);

		}
			
	}
	
	private ProcessResult validateInputData(ListSetupVoucherMasterBean dto,String projectCode) {
	
		boolean validData = true;
		if(BeanUtil.isEmpty(dto.getName()) && BeanUtil.isEmpty(dto.getBirthDay()) && BeanUtil.isEmpty(dto.getMobileNo()) && BeanUtil.isEmpty(dto.getVoucherNo()) && BeanUtil.isEmpty(dto.getValue()) && BeanUtil.isEmpty(dto.getExpiredDate())){
			validData = false;
			return new ProcessResult(validData, null, "Voucher No., Mobile No., Name, Issue Date, Value, Expired Date is empty");
		}
		if(BeanUtil.isEmpty(dto.getVoucherNo())){
			validData = false;
			return new ProcessResult(validData, null, "Voucher No. is empty");
		}
		if(BeanUtil.isEmpty(dto.getValue())){
			validData = false;
			return new ProcessResult(validData, null, "Value is empty");
		}
		if(BeanUtil.isEmpty(dto.getExpiredDate())){
			validData = false;
			return new ProcessResult(validData, null, "Expired Date is empty");
		}
		if(!BeanUtil.isEmpty(dto.getBirthDay())){
			if(!isDate(dto.getBirthDay(), "/")){
				validData = false;
				return new ProcessResult(validData, null, "Issue Date is wrong format");
			}
		}
		
		//Config Require Mobile by ProjectCode
		//List<LovMaster> listLovMaster = setupVoucherMasterService.listLovMastersByCriteria("V1_SETUP_VOUCHER_MASTER", "IGNORE_MOBILE_NO", null, projectCode, "Y", null, null, null, null);
		List<LovMaster> listLovMaster = lovMasterService.listLovMasterByCriteria("V1_SETUP_VOUCHER_MASTER", "IGNORE_MOBILE_NO", null, projectCode, null, "Y", null, null, null, null, null, null);
		
		if(!BeanUtil.isEmpty(dto.getVoucherNo())){
			if(16 != dto.getVoucherNo().length()){
				validData = false;
				return new ProcessResult(validData, null, "Voucher No. not equal 16 digit");
			}
		}
		
		if(!BeanUtil.isEmpty(dto.getVoucherNo())) {
			if(!dto.getVoucherNo().matches("^[a-zA-Z0-9]+$")) {
				validData = false;
				return new ProcessResult(validData, null, "Voucher No. is wrong format");
			}
		}
		
		if(BeanUtil.isNotEmpty(dto.getName()) && dto.getName().length() > 200 ) {
			validData = false;
			return new ProcessResult(validData, null, "Name more than 200 digit");
		}
		
		if(BeanUtil.isEmpty(listLovMaster)){
			if(!BeanUtil.isEmpty(dto.getMobileNo())){
				String regexp = "[0][0-9]*{9}";
				if(!dto.getMobileNo().matches(regexp)){
					validData = false;
					return new ProcessResult(validData, null, "Mobile No. is wrong format");
				}
			}else{
				validData = false;
				return new ProcessResult(validData, null, "Mobile No. is empty");
			}
		}
		
		if(StringUtils.isNotEmpty(dto.getMobileNo())){
			if(10 != dto.getMobileNo().length()){
				validData = false;
				return new ProcessResult(validData, null, "Mobile No. not equal 10 digit");
			}else{
				String regexp = "[0][0-9]*{9}";
				if(!dto.getMobileNo().matches(regexp)){
					validData = false;
					return new ProcessResult(validData, null, "Mobile No. is wrong format");
				}
			}		
		}
		

		
		if(!dto.getValue().matches("^(?!$)\\d{0,8}(?:\\.\\d{1,2})?$")) {
			validData = false;
			return new ProcessResult(validData, null, "Value is wrong format");
		}
		
		if(new BigDecimal(dto.getValue()).compareTo(BigDecimal.ZERO) == 0) {
			validData = false;
			return new ProcessResult(validData, null, "Value must more than 0");
		}
		
		if(!BeanUtil.isEmpty(dto.getExpiredDate())){
			if(!expDate(dto.getExpiredDate(), "/")){
				validData = false;
				return new ProcessResult(validData, null, "Expired Date is wrong format");
			}
		}
		return new ProcessResult(validData, null, "Validate sucess");
	}
	
	private boolean isDate(String strDate, String delimeter) {
		boolean isValid = false;
		Date sysDate = new Date();
		if(strDate.length()==10){
			try{
				 String regexp = "[0-3][0-9]/[0-1][0-9]/[0-9]*{4}";
			     boolean match = strDate.matches(regexp);
			     if(!match) throw new Exception("date not match");
			     
			     String[] fileDate = strDate.split(delimeter); 
			     Calendar c = Calendar.getInstance();
			     c.setLenient(false);
			     c.set(Integer.parseInt(fileDate[2]), Integer.parseInt(fileDate[1])-1, Integer.parseInt(fileDate[0]));
			     if(null != c.getTime() && DateUtils.before(c.getTime(), sysDate)){
			    	 isValid = true;
			     }
			}catch(Exception ex){
				return isValid;
			}
		} else if(strDate.length()==7){
			try{
			     String regexp = "[0-1][0-9]/[0-9]*{4}";
			     boolean match = strDate.matches(regexp);
			     if(!match) throw new Exception("date not match");
			     
			     String[] fileDate = strDate.split(delimeter); 
			     Calendar c = Calendar.getInstance();
			     c.setLenient(false);
			     c.set(Integer.parseInt(fileDate[1]), Integer.parseInt(fileDate[0])-1, 01);
			     if(null != c.getTime() && DateUtils.before(c.getTime(), sysDate)){
			    	 isValid = true;
			     }
			}catch(Exception ex){
				return isValid;
			}
		}
		return isValid;
	}
	
	private boolean expDate(String strDate, String delimeter) {
		boolean isValid = false;
		try{
			Date sysDate = new Date();
			 String regexp = "[0-3][0-9]/[0-1][0-9]/[0-9]*{4}";
		     boolean match = strDate.matches(regexp);
		     if(!match) throw new Exception("date not match");
		     
		     Calendar calSysDate = Calendar.getInstance();
		     calSysDate.setTime(sysDate);
		     
		     String[] fileDate = strDate.split(delimeter); 
		     Calendar c = Calendar.getInstance();		     
		     c.setLenient(false);
		     c.set(Integer.parseInt(fileDate[2]), Integer.parseInt(fileDate[1])-1, Integer.parseInt(fileDate[0]));
		     c.set(Calendar.HOUR_OF_DAY, 0);
		     c.set(Calendar.MINUTE, 0);
		     c.set(Calendar.SECOND, 0);
		     c.set(Calendar.MILLISECOND, 0);
		     if(null != c.getTime() && 4 == fileDate[2].length() && (c.get(Calendar.YEAR)-calSysDate.get(Calendar.YEAR))<10 && DateUtils.before(sysDate, c.getTime())){
		    	 isValid = true;
		     }
		}catch(Exception ex){
			return isValid;
		}
		return isValid;
	}

}
