package th.co.ais.dt.core.controller.impl.so;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import th.co.ais.dt.entity.so.ShiftDtl;
import th.co.ais.dt.entity.so.ShiftMst;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.so.ShiftMstService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.DateUtils;
import th.co.ais.dt.util.TDMDataUtility;
import th.co.ais.dt.controller.dto.CheckShiftReq;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.OpenCloseShiftBean;
import th.co.ais.dt.controller.dto.OpenCloseShiftPaymentBean;
import th.co.ais.dt.controller.dto.QueryShiftReq;
import th.co.ais.dt.controller.dto.QueryShiftRes;

@RestController
@Slf4j
@AllArgsConstructor
public class OpenCloseShiftWebImpl {
	final String prefixPath = "api/saleout/v1";
	
	//private static final String HEADER_JSON = new StringBuilder(MediaType.APPLICATION_JSON).append(";charset=utf-8").toString();
	private static final String CONTENT = "Content-Type";
	private static final String SUCCESS = "20000";
	private static final String ERROR = "50000";
	private static final DecimalFormat format = new DecimalFormat("#,##0.00");
	private static final String FORMAT_DATE = "dd/MM/yyyy";
	private static final String FORMAT_TIME = "HH:mm:ss";
	private final String MESSAGE_001 = "Open Shift success";
	private final String MESSAGE_002 = "Can not Open Shift, Please Open Shift again!!!";
	private final String MESSAGE_003 = "%s = 0.00";
	private final String MESSAGE_004 = "Close Shift success.";
	private final String MESSAGE_005 = "Close Shift fail, Please Close Shift again!!!";
	private final String MESSAGE_006 = "You cannot Open/Close Shift menu, Please contact admin";
	private final String MESSAGE_007 = "Terminal ID : %s is not close, please go to Control Close Shift menu.";
	private final String MESSAGE_008 = "Please entry numeric only";
	private final String MESSAGE_009 = "Please entry Open Balance";
	
	private final ShiftMstService shiftMstService ;
	
	@RequestMapping(
			  value = prefixPath +"/create-open-shift",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(OpenCloseShiftBean.class)
	public ResponseEntity<String> createOpenShift(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		
		//ShiftMstService shiftMstService = (ShiftMstService) SpringApplicationContext.getBean("shiftMstService");
		OpenCloseShiftBean req = null;
		CommonResponseBean res = new CommonResponseBean();
		Gson gson = new Gson();
		Date shiftDate = new Date();
	
		try {
			
			req = gson.fromJson(jsonRequest, OpenCloseShiftBean.class);
		
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);
			
		}
			

		if (BeanUtil.isNotEmpty(req)) {
			OpenCloseShiftBean newResult = new OpenCloseShiftBean();
			String shiftId = shiftMstService.getGenShiftId(Long.parseLong(req.getLocationCode()), req.getTerminalId(), Long.parseLong(req.getShiftNumPopup()), shiftDate);
			ProcessResult result = shiftMstService.saveConfirmOpenShift(req, shiftDate, shiftId);			
			if(result.isSuccess()){				
				// Set display values.			
				newResult.setOpenBalance(format.format(convertNumString2Double(req.getOpenBalPopup())));
				newResult.setCloseBalance("");
				newResult.setOpenDate(DateUtils.toString(shiftDate, FORMAT_DATE));
				newResult.setOpenTime(DateUtils.toString(shiftDate, FORMAT_TIME));
				newResult.setStatusFlg("1");
				newResult.setShiftStatus("O");
				newResult.setShiftId(shiftId);
				newResult.setShiftNum(req.getShiftNumPopup().toString());
				newResult.setHideOpenShift(true);
				newResult.setShiftNo(shiftId);
				res.setResultCode(SUCCESS);
				res.setResultDescription(MESSAGE_001);
				res.setResultObj(newResult);
			}else{
				res.setResultCode(ERROR);
				res.setResultDescription(MESSAGE_002);
			}

		}
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
	}
	
	private Double convertNumString2Double(String num){
		Double d = 0d;
		num = num.replace(",", "");
		d = Double.parseDouble(num);
		return d;
	}
	
	@RequestMapping(
			  value = prefixPath +"/create-close-shift",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding({OpenCloseShiftBean.class,CommonResponseBean.class})
	public ResponseEntity<String> createCloseShift(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		OpenCloseShiftBean req = null;
		CommonResponseBean res = new CommonResponseBean();
//		OpenCloseShiftBean res = null;
		Gson gson = new Gson();
		Date closeDate = new Date();
		
		try {
			
			req = gson.fromJson(jsonRequest, OpenCloseShiftBean.class);
		
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}


		try {
			saveUpdatePerform(req);
			OpenCloseShiftBean newResult = new OpenCloseShiftBean();
			ProcessResult result = shiftMstService.createCloseShift(req.getShiftMst(), req.getShiftDtl());
			if(result.isSuccess()){
				// If save success..
				OpenCloseShiftBean bean = setTotalPaymentList(req.getPaymentList());
				newResult.setCloseBalance(format.format(req.getShiftMst().getCloseBalAmt()));
				newResult.setCloseDate(DateUtils.toString(closeDate, FORMAT_DATE));
				newResult.setCloseTime(DateUtils.toString(closeDate, FORMAT_TIME));
				newResult.setDisableExistAmt(true);
				newResult.setDisableSave(true);
				newResult.setCloseUser(req.getUserId());
				newResult.setShiftStatus("C");
				newResult.setStatusFlg("0");
				newResult.setPaymentList(req.getPaymentList());
				newResult.setTotalExistsAmount(bean.getTotalExistsAmount());
				newResult.setTotalBalanceAmount(bean.getTotalBalanceAmount());
				newResult.setTotalReceiptAmount(bean.getTotalReceiptAmount());
				newResult.setTotalCreditAmount(bean.getTotalCreditAmount());
				newResult.setTotalCancelCnAmount(bean.getTotalCancelCnAmount());
				newResult.setTotalWithHoldingTax(bean.getTotalWithHoldingTax());
				newResult.setTotalDifferentAmount(bean.getTotalDifferentAmount());
				res.setResultCode(SUCCESS);
				res.setResultDescription(MESSAGE_004);
				res.setResultObj(newResult);
			}else{
				newResult.setShiftStatus("O");
				newResult.setDisableExistAmt(false);
				newResult.setDisableSave(false);
				res.setResultCode(ERROR);
				res.setResultDescription(MESSAGE_005);
				res.setResultObj(newResult);
			}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
	}
	
	public OpenCloseShiftBean saveUpdatePerform(OpenCloseShiftBean req) {
//		OpenCloseShiftBean openCloseShiftBean= new OpenCloseShiftBean();
		List<OpenCloseShiftPaymentBean> paymentList = req.getPaymentList();
		Date closeDate = new Date();
		
		// Detail.
		List<ShiftDtl> shiftDtlList = new ArrayList<ShiftDtl>();
		ShiftDtl shiftDtl = null;
		int index = 0;
		int count = 0;
		
		for(OpenCloseShiftPaymentBean bean : paymentList){
			// Check type input.
			if(!this.isNumber(bean.getExistsAmount())){
				req.setResultDescription(MESSAGE_008);
				return req;
			}else{
				// Check input zero.
				//&&!(openCloseShiftBean.isDisableExistAmt()
				if(convertNumString2Double(bean.getExistsAmount()) == 0 ){
					req.setResultDescription(String.format(MESSAGE_003, bean.getPayTypeDescription()));
				}else{
					count++;
				}
			}
		}
		
		for(OpenCloseShiftPaymentBean bean : paymentList){
			shiftDtl = new ShiftDtl();
			shiftDtl.setCreated(closeDate);
			shiftDtl.setCreatedBy(req.getUserId());
			shiftDtl.setShiftId(req.getShiftNo());
			shiftDtl.setPayType(bean.getPayType());
			shiftDtl.setExistAmt(convertNumString2Double(bean.getExistsAmount()));
			shiftDtl.setRecvBalAmt(parseToDouble(bean.getBalanceAmount()));
			shiftDtl.setRecptAmt(parseToDouble(bean.getReceiptAmount()));
			shiftDtl.setCnAmt(parseToDouble(bean.getCreditAmount()));
			shiftDtl.setCanCnAmt(parseToDouble(bean.getCancelCnAmount()));
			shiftDtl.setWtaxAmt(parseToDouble(bean.getWithHoldingTax()));
			shiftDtl.setDiffAmt(convertNumString2Double(bean.getExistsAmount()) - bean.getBalanceAmount());
			req.getPaymentList().get(index).setDifferentAmount(shiftDtl.getDiffAmt());
			shiftDtl.setCompany(bean.getCompanyCode());
			shiftDtlList.add(shiftDtl);
			index++;
		}
	
		
		// Master
		log.info("shift No ===>"+req.getShiftNo());
		ShiftMst shiftMst = shiftMstService.getShiftMstByShiftId(req.getShiftNo());
		shiftMst.setCloseUserId(req.getUserId());
		shiftMst.setShiftNum(parseToLong(req.getShiftNum()));
		shiftMst.setCloseTerminalId(req.getTerminalId());
		shiftMst.setCloseDtm(closeDate);
		Double closeBalAmt = 0D;
		for(OpenCloseShiftPaymentBean bean : req.getPaymentList()){
			closeBalAmt += convertNumString2Double(bean.getExistsAmount());
		}
		shiftMst.setCloseBalAmt(closeBalAmt - convertNumString2Double(req.getOpenBalance()));
		shiftMst.setStatusFlg("0");
		shiftMst.getCreateValue().setLastUpd(closeDate);
		shiftMst.getCreateValue().setLastUpdBy(req.getUserId());
		
		
		req.setShiftMst(shiftMst);
		req.setShiftDtl(shiftDtlList);

		return req;
	}
	
	private boolean isNumber(String obj){
		try{
			if(obj.indexOf(",") > -1){
				obj = obj.replace(",", "");
			}
			Double.parseDouble(obj);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	private Double parseToDouble(Object obj){
		Double val = 0d;
		try {
			val = Double.parseDouble(obj.toString());
		} catch (Exception e) {
			val = 0d;
		}
		return val;
	}
	
	private Long parseToLong(Object obj){
		Long val = 0l;
		try{
			val = Long.parseLong(obj.toString());
		}catch(Exception e){
			val = 0l;
		}
		return val;
	}
	
	private OpenCloseShiftBean setTotalPaymentList(List<OpenCloseShiftPaymentBean> paymentList){
		OpenCloseShiftBean openCloseShiftBean = new OpenCloseShiftBean();
		Double totExistAmt = 0D;
		Double totBalAmt = 0D;
		Double totRcptAmt = 0D;
		Double totCrdAmt = 0D;
		Double totCanAmt = 0D;
		Double totWTaxAmt = 0D;
		Double totDiffAmt = 0D;
		for(OpenCloseShiftPaymentBean bean : paymentList){
			totExistAmt += convertNumString2Double(bean.getExistsAmount());
			totBalAmt += parseToDouble(bean.getBalanceAmount());
			totRcptAmt += parseToDouble(bean.getReceiptAmount());
			totCrdAmt += parseToDouble(bean.getCreditAmount());
			totCanAmt += parseToDouble(bean.getCancelCnAmount());
			totWTaxAmt += parseToDouble(bean.getWithHoldingTax());
			totDiffAmt += parseToDouble(bean.getDifferentAmount());
		}
		openCloseShiftBean.setTotalExistsAmount(totExistAmt);
		openCloseShiftBean.setTotalBalanceAmount(totBalAmt);
		openCloseShiftBean.setTotalReceiptAmount(totRcptAmt);
		openCloseShiftBean.setTotalCreditAmount(totCrdAmt);
		openCloseShiftBean.setTotalCancelCnAmount(totCanAmt);
		openCloseShiftBean.setTotalWithHoldingTax(totWTaxAmt);
		openCloseShiftBean.setTotalDifferentAmount(totDiffAmt);
		
		return openCloseShiftBean;
		
	}
	
	@RequestMapping(
			  value = prefixPath +"/query-shift-payments",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding({OpenCloseShiftBean.class,CommonResponseBean.class})
	public ResponseEntity<String> queryShiftPayments(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
//		QueryShiftReq req = null;
		OpenCloseShiftBean req = null;
		CommonResponseBean res = new CommonResponseBean();
		
		try {
			req = gson.fromJson(jsonRequest, OpenCloseShiftBean.class);
		} catch (Exception e) {
			res = new CommonResponseBean("50000", "F", "JSON incorrect format");
			
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		}
		
		try {
			
//			List<QueryShiftRes> listShift = shiftMstService.listQueryShift(shiftDtFrom, shiftDtTo, req.getShiftStatus(), req.getLocationCode(), req.getUserId());
			
			List<OpenCloseShiftPaymentBean> listShiftPayments = shiftMstService.listShiftPayments(req.getShiftId());
			
			res = new CommonResponseBean("20000", "S", "success");
			res.setResultObj(listShiftPayments);
			
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("queryShiftPayments", e);
			res = new CommonResponseBean("50000", "F", "System error");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			
		}
		
		
	}
	
	@RequestMapping(
			  value = prefixPath +"/query-shift",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding({QueryShiftReq.class,CommonResponseBean.class})
	public ResponseEntity<String> queryShift(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		Gson gson = new Gson();
		QueryShiftReq req = null;
		CommonResponseBean res = new CommonResponseBean();
		
		try {
			req = gson.fromJson(jsonRequest, QueryShiftReq.class);
		} catch (Exception e) {
			res = new CommonResponseBean("50000", "F", "JSON incorrect format");
			
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		}
		
		try {
			Date shiftDtFrom = null;
			Date shiftDtTo = null;
			if(BeanUtil.isNotEmpty(req.getShiftDtFrom())) {
				shiftDtFrom = TDMDataUtility.adjustTimeToStartOfDate(TDMDataUtility.convertStringToDate(req.getShiftDtFrom()));
			}
			if(BeanUtil.isNotEmpty(req.getShiftDtTo())) {
				shiftDtTo = TDMDataUtility.adjustTimeToEndOfDate(TDMDataUtility.convertStringToDate(req.getShiftDtTo()));
			}
			if(shiftDtFrom != null && shiftDtTo != null) {
				if(DateUtils.compareDate(shiftDtFrom, shiftDtTo) > 0) {
					res = new CommonResponseBean("50000", "F", "Shift Date From can't be more than Shift Date To");
					return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
					//throw new Exception("Shift Date From can't be more than Shift Date To");
				}
			}
			
			List<QueryShiftRes> listShift = shiftMstService.listQueryShift(shiftDtFrom, shiftDtTo, req.getShiftStatus(), req.getLocationCode(), req.getUserId());
			
			res = new CommonResponseBean("20000", "S", "success");
			res.setResultObj(listShift);
			
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("queryShift", e);
			res = new CommonResponseBean("50000", "F", "System error");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		}
		
		
	}
	
	@RequestMapping(
			  value = prefixPath +"/query-set-default-value",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding({CheckShiftReq.class,CommonResponseBean.class})
	public ResponseEntity<String> querySetDefaultValue(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);

		CheckShiftReq req = null;
		CommonResponseBean res = new CommonResponseBean();
		Gson gson = new Gson();
		
		try {
			
			req = gson.fromJson(jsonRequest, CheckShiftReq.class);
		
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);
		}

		if (BeanUtil.isEmpty(req.getPhysicalAddress())) {
			res.setResultCode(ERROR);
			res.setResultDescription("Please input physicalAddress.");
		} else if (BeanUtil.isEmpty(req.getLocationCode())) {
			res.setResultCode(ERROR);
			res.setResultDescription("Please input locationCode.");
		} else {
			ProcessResult result = shiftMstService.checkShiftForOpenCloseShift(req);
			if(result.isSuccess()) {
				res.setResultCode(SUCCESS);
				res.setResultDescription(result.getMessage());
				res.setResultObj(result.getResultObject());				
			}else {
				res.setResultCode(ERROR);
				res.setResultDescription(result.getMessage());
				res.setResultObj(result.getResultObject());	
			}
		}
		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
		
		
	}


}
