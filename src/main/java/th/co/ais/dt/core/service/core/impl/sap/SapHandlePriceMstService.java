package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.ConditionValidityBean;
import th.co.ais.dt.controller.dto.SalesPricingConditionRecordBean;
import th.co.ais.dt.controller.dto.SapHandlePriceMstBean;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;
import th.co.ais.dt.entity.pr.PriceMst;
import th.co.ais.dt.entity.pr.PriceMstHist;
import th.co.ais.dt.entity.sap.DtSapMchProductPriceLog;
import th.co.ais.dt.entity.sk.RequestOrderConfig;
import th.co.ais.dt.entity.sk.RequestOrderConfigPK;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.MasterValueHist;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.pr.IPriceMstDao;
import th.co.ais.dt.repository.interfaces.pr.IPriceMstHistDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMchProductPriceLogDao;
import th.co.ais.dt.repository.interfaces.sk.IRequestOrderConfigDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.IProductMstDao;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.mail.impl.EmailSenderService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@Service
@Slf4j
@Transactional
public class SapHandlePriceMstService {

	public static final List<String> SAP_PRICE_MST_MAP = Arrays.asList("VKP0", "ZP02", "ZINT", "ZP01");
	public static final List<String> SAP_PRICE_MST_MAP_FOR_STAFF = Arrays.asList("ZP02");

	public static final String SAP_PRICE_MST_PATH = "/DTWS/api/price/v1/insertSapPriceMst";
	public static final String SAP_PRICE_MST_STAFF_PATH = "/DTWS/api/price/v1/insertSapPriceMstForStaff";

	public static final String SAP_PRICE_MST_SAVE_MST = "SAP_PRICE_MST_SAVE";
	public static final String SAP_PRICE_MST_SAVE_STAFF = "SAP_PRICE_MST_SAVE_STAFF";
	public static final String SAP_PRICE_MST_SAVE_ALL = "SAP_PRICE_MST_SAVE_ALL";

	public static final String PRICE_MST_CONFIG = "PRICE_MST_CONFIG";
	public static final String PRICE_MST_LOG = "PRICE_MST_LOG";
	public static final String DT_APP = "DTAPP";

	public static final String EMAIL_SENDER = "digitaltrading.app@ais.co.th";

	@Autowired
	private DTConfig dTConfig;

	@Autowired
	private IDtSapMchProductPriceLogDao dtSapLogDao;

	@Autowired
	private IEmailService emailService;

	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	private ILovMasterDao lovMasterDao ;
	
	@Autowired
	private IProductMstDao productMstDao ;
	
	@Autowired
	private IPriceMstDao priceMstDao ;
	
	@Autowired
	private IPriceMstHistDao priceMstHistDao ;
	
	@Autowired
	private IRequestOrderConfigDao requestOrderConfigDao ;
	
	

	public CommonResponseBean callPriceMstForSave(SapHandlePriceMstBean req) {
		CommonResponseBean response = new CommonResponseBean();
		boolean isHaveCallSave = false;
		boolean isHaveCallSaveStaff = false;
		Long priceMstId = null;
		Long priceMstStaffId = null;
		String payload = req.toString();
		log.info("payload: " + payload);
		SapHandlePriceMstBean mstRes = null, staffRes = null;
		
		try {
			String callSave = checkCallSave(req.getSalesPricingConditionRecord());
			if (callSave != null) {
				if (callSave.equals(SAP_PRICE_MST_SAVE_ALL)) {
					isHaveCallSave = true;
					isHaveCallSaveStaff = true;
				} else if (callSave.equals(SAP_PRICE_MST_SAVE_MST)) {
					isHaveCallSave = true;
				}
			}

			if (isHaveCallSave) {
				priceMstId = insertOrUpdateLog(req, null, "SAVE_PRICE_MST");
				//mstRes = callPriceMstOnPrem(req, SAP_PRICE_MST_PATH);
				mstRes = callPriceMstCloud(req);
				if (mstRes.getResultCode().equals("20000")) {
					if (isHaveCallSaveStaff) {
						priceMstStaffId = insertOrUpdateLog(req, null, "SAVE_PRICE_STAFF");
						//staffRes = callPriceMstOnPrem(req, SAP_PRICE_MST_STAFF_PATH);
						staffRes = callPriceMstSTFCloud(req);					
						if (staffRes.getResultCode().equals("50000")) {
							sendEmailFailedSave(
									SAP_PRICE_MST_STAFF_PATH + "(50000): " + staffRes.getDeveloperMessage());
						}
					}

				} else {
					sendEmailFailedSave(SAP_PRICE_MST_PATH + "(50000): " + mstRes.getDeveloperMessage());
				}
				response.setResultCode("20000");
				response.setResultDescription("Success");
			} else {
				sendEmailFailedSave("ConditionType not Match.");
				response.setResultCode("50000");
				response.setResultDescription("ConditionType not match.");
				response.setResultStatus("Failed");

				Long priceCoreId = insertOrUpdateLog(req, null, "SAVE_PRICE_CORE");
				SapHandlePriceMstBean err = new SapHandlePriceMstBean();
				err.setResultCode("50000");
				err.setDeveloperMessage("ConditionType not match.");
				err.setResultDescription("ConditionType not match.");
				insertOrUpdateLog(err, priceCoreId, null);

			}

		} catch (Exception e) {
			log.error(e.getMessage());
			sendEmailFailedSave("Error: " + e.getMessage());
			SapHandlePriceMstBean err = new SapHandlePriceMstBean();
			err.setResultCode("50000");
			err.setDeveloperMessage(e.getMessage());
			err.setResultDescription(e.getMessage());
			if (isHaveCallSave && priceMstId != null) {
				insertOrUpdateLog(err, priceMstId, null);
			}
			if (isHaveCallSaveStaff && priceMstStaffId != null) {
				insertOrUpdateLog(err, priceMstStaffId, null);
			}

			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setResultStatus("Failed");
			return response;
		}

		try {
			if (isHaveCallSave && priceMstId != null) {
				insertOrUpdateLog(mstRes, priceMstId, null);
			}
			if (isHaveCallSaveStaff && priceMstStaffId != null) {
				insertOrUpdateLog(staffRes, priceMstStaffId, null);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			response.setResultCode("50000");
			response.setResultDescription(e.getMessage());
			response.setResultStatus("Failed");
			return response;
		}
		return response;
	}

	private String checkCallSave(List<SalesPricingConditionRecordBean> dataCheck) {
		boolean isPriceMst = false;
		boolean isPriceStaff = false;
		for (SalesPricingConditionRecordBean data : dataCheck) {
			List<ConditionValidityBean> listValidity = data.getConditionValidity();
			for (ConditionValidityBean validity : listValidity) {
				if (mapPriceMst(validity.getConditionType(), SAP_PRICE_MST_MAP)) {
					isPriceMst = true;
				}
				if (mapPriceMst(validity.getConditionType(), SAP_PRICE_MST_MAP_FOR_STAFF)) {
					isPriceStaff = true;
				}
				if (isPriceMst && isPriceStaff) {
					break;
				}
			}
			if (isPriceMst && isPriceStaff) {
				break;
			}
		}
		
		if (isPriceMst && isPriceStaff) {
			return SAP_PRICE_MST_SAVE_ALL;
		} else if (isPriceMst) {
			return SAP_PRICE_MST_SAVE_MST;
		} else if (isPriceStaff) {
			return SAP_PRICE_MST_SAVE_STAFF;
		}
		return null;
	}

	private SapHandlePriceMstBean callPriceMstOnPrem(SapHandlePriceMstBean req, String path) {
		Gson gson = new Gson();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json");
		log.info("call api dtws : " + path);
		log.info("json : " + gson.toJson(req));
		String res = c.HttpClient(properties, dTConfig.getUrl().getDtws() + path, gson.toJson(req), "POST", null);
		log.info(res);

		return gson.fromJson(res, SapHandlePriceMstBean.class);
	}

	private void sendEmailFailedSave(String text) {
		log.info("price mst sending email : " + text);
		try {
			EmailForm emailForm = new EmailForm();
			emailForm.setFrom(EMAIL_SENDER);
			emailForm.setBody("Price Mst Save Errors : " + text);
			emailForm.setToLists(new ArrayList<>());
			emailForm.setCcLists(new ArrayList<>());
			List<Object[]> email = emailService.queryEmail(PRICE_MST_CONFIG);
			if (BeanUtil.isNotEmpty(email)) {
				Object[] e = email.get(0);

				String subject = (String) e[3];
				String to = (String) e[4];
				String cc = (String) e[5];

				String[] toArr = to.split(";");
				for (String el : toArr) {
					emailForm.getToLists().add(el);
				}

				String[] ccArr = cc.split(";");
				for (String el : ccArr) {
					emailForm.getCcLists().add(el);
				}

				emailForm.setSubject(subject);

				emailSenderService.sendEmail(emailForm);
				log.info("send email price mst success");
			} else {
				log.info("No have email for send : " + text);
			}
		} catch (Exception e) {
			log.error("sending email price mst error ", e);
		}
	}

	private Long insertOrUpdateLog(SapHandlePriceMstBean request, Long ID, String pretext) {
		Gson gson = new Gson();
		DtSapMchProductPriceLog PriceMstLog = null;
		String status = "W";

		String inputLog = gson.toJson(request).replace("\"value\"", "\"$\"").replace("\"valueUnit\"", "\"@valueUnit\"")
				.replace("\"curren4cyCode\"", "\"@curren4cyCode\"").replace("\"unitCode\"", "\"@unitCode\"");

		if (request == null) {
			request = new SapHandlePriceMstBean();
		}
		if (request.getResultCode() != null) {
			if (request.getResultCode().equals("20000")) {
				status = "S";
			} else if (request.getResultCode().equals("50000")) {
				status = "F";
			}
		} else if (ID != null && request.getResultCode() == null) {
			status = "F";
			request.setDeveloperMessage("Can't save price Mst");
		}

		if (ID != null) {
			PriceMstLog = dtSapLogDao.getByLongPrimaryKey(ID);
			PriceMstLog.setStatus(status);
			PriceMstLog.setDescription(request.getDeveloperMessage());
			dtSapLogDao.insert(PriceMstLog);
		} else {
			PriceMstLog = new DtSapMchProductPriceLog();
			PriceMstLog.setServiceName(PRICE_MST_LOG);
			if (pretext != null) {
				PriceMstLog.setParaInput(pretext + ": " + inputLog);
			} else {
				PriceMstLog.setParaInput(inputLog);
			}
			PriceMstLog.setStatus(status);
			PriceMstLog.setCreateValue(getCreateValue(new Date()));
			dtSapLogDao.insert(PriceMstLog);
		}

		return PriceMstLog.getTableId();
	}

	private static MasterValue getCreateValue(Date date) {
		MasterValue masterValue = new MasterValue();
		masterValue.setCreated(date);
		masterValue.setCreatedBy(DT_APP);
		masterValue.setLastUpd(date);
		masterValue.setLastUpdBy(DT_APP);
		return masterValue;
	}

	private boolean mapPriceMst(String mapData, List<String> listForMap) {
		for (String string : listForMap) {
			if (mapData.equals(string)) {
				log.info("find type : " + mapData);
				return true;
			}
		}
		return false;
	}
	
	private SapHandlePriceMstBean callPriceMstCloud(SapHandlePriceMstBean input) {
		SapHandlePriceMstBean res = new SapHandlePriceMstBean();
		ProcessResult processResult = new ProcessResult(false, null, "");
		
		String message = validateDate(input);
		if (message.length() > 0) {
			res.setResultCode("50000");
			res.setResultDescription(message);
			res.setDeveloperMessage(message);
			return res ;
		} else {
			
			List<SalesPricingConditionRecordBean> listPrice = validateRequest(input);
			if (BeanUtil.isNotEmpty(listPrice)) {
				try {
					processResult = insertPriceMst(listPrice);
					if (!processResult.isSuccess()) {
						
						res.setResultCode("50000");
						res.setResultDescription("Insert Price master is fail");
						res.setDeveloperMessage(processResult.getMessage());
						return res ;
						//return Response.status(200).entity("{\"resultCode\":\"50000\",\"resultDescription\":\"Insert Price master is fail\",\"developerMessage\":\""+ processResult.getMessage() + "\"}")
							//	.header("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8").build();
					}
				} catch (Exception e) {
					e.printStackTrace();
					String msg = e.getMessage() == null || e.getMessage() == "" ? "Internal error." : e.getMessage();
					
					res.setResultCode("50000");
					res.setResultDescription("Fail");
					res.setDeveloperMessage(msg);
					//return Response.status(200).entity("{\"resultCode\":\"50000\",\"resultDescription\":\"Fail\",\"developerMessage\":\""+ msg + "\"}")
						//	.header("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8").build();
				}
			}
		}

		res.setResultCode("20000");
		res.setResultDescription("Success");
		res.setDeveloperMessage("Success");
		return res ;
		//return Response.status(200).entity("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}").header("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8").build();
	
		
		
		//return res ;
		
	}
	
	private String validateDate(SapHandlePriceMstBean request){
		String message = "";
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		inputFormat.setLenient(false);
		
		for (SalesPricingConditionRecordBean salePrice : request.getSalesPricingConditionRecord()) {
			
			for(ConditionValidityBean validity : salePrice.getConditionValidity()) {
				Date effectiveDate;
				Date expDate;
				
				String validityStartDate = (BeanUtil.isEmpty(validity.getConditionValidityStartDate()) || validity.getConditionValidityStartDate() == null) ? salePrice.getConditionValidityStartDate():validity.getConditionValidityStartDate();
				String validityEndDate = (BeanUtil.isEmpty(validity.getConditionValidityEndDate()) || validity.getConditionValidityEndDate() == null) ? salePrice.getConditionValidityEndDate():validity.getConditionValidityEndDate();
				try {
					effectiveDate = inputFormat.parse(validityStartDate);
					expDate = inputFormat.parse(validityEndDate);
					if(expDate.before(effectiveDate)) {
						message = "End date should be more than Start date.";
					}
				} catch (ParseException e) {
					e.printStackTrace();
					message = "Start date or End date is wrong format.";
				}
			}
		}
		return message;
	}
	
	private List<SalesPricingConditionRecordBean> validateRequest(SapHandlePriceMstBean request) {
		List<SalesPricingConditionRecordBean> listSalePrice = new ArrayList<SalesPricingConditionRecordBean>();

		for (SalesPricingConditionRecordBean salePrice : request.getSalesPricingConditionRecord()) {
			ArrayList<ConditionValidityBean> validity = new ArrayList<ConditionValidityBean>();

			for (ConditionValidityBean checkData : salePrice.getConditionValidity()) {
				if ((null == checkData.getPlant() || checkData.getPlant().isEmpty()) 
						&& (null == checkData.getCustomer() || checkData.getCustomer().isEmpty())) {
					
					if((null != checkData.getMaterial() && !checkData.getMaterial().isEmpty())
							&& (null != checkData.getSalesOrganization() && !checkData.getSalesOrganization().isEmpty())) {
						validity.add(checkData);
					}
				}
			}
			
			Map<ConditionValidityBean, Long> counts = validity.stream().collect(Collectors.groupingBy(o -> {
				ConditionValidityBean ress =  new ConditionValidityBean();
				ress.setMaterial(o.getMaterial());
				ress.setSalesOrganization(o.getSalesOrganization());
				ress.setConditionType(o.getConditionType()) ;
				ress.setDistributionChannel("");
				ress.setPlant("");
				ress.setCustomer("");
				ress.setOrderQuantityUnit(o.getOrderQuantityUnit());
				ress.setConditionValidityEndDate(o.getConditionValidityEndDate());
				ress.setConditionValidityStartDate(o.getConditionValidityStartDate());
				return ress ;

				//return new ConditionValidityBean(o.getMaterial(), o.getSalesOrganization(), o.getConditionType(), "", "", "", o.getOrderQuantityUnit(), o.getConditionValidityEndDate(), o.getConditionValidityStartDate());
			}, Collectors.counting()));
			
			List<ConditionValidityBean> uniqueList = counts.entrySet().stream()
		            .filter(entry -> entry.getValue() == 1 || entry.getValue() > 1)
		            .map(Map.Entry::getKey)
		            .collect(Collectors.toList());

			salePrice.getConditionValidity().removeAll(salePrice.getConditionValidity());
			salePrice.setConditionValidity((ArrayList<ConditionValidityBean>) uniqueList);
			listSalePrice.add(salePrice);
		}
		return listSalePrice;
	}
	
    private ProcessResult insertPriceMst(List<SalesPricingConditionRecordBean> listPrice) throws DataAccessException {
		
		List<PriceMst> listPriceMst = new ArrayList<PriceMst>();
		for (SalesPricingConditionRecordBean salePrice : listPrice) {
			for (ConditionValidityBean checkData : salePrice.getConditionValidity()) {
				
				List<LovMaster> lovMaster = lovMasterDao.listLovMasterByCriteria(
						"SAP_S4_SALEORG_MAP_COMPANY",
						null,
						null,
						checkData.getSalesOrganization(),
						null
				);
				
				if(null == lovMaster || lovMaster.isEmpty()) {
					return new ProcessResult(false, null, "No have data to insert");
				}
				
				ProductMst product = productMstDao.getProductMstByUniqueV2(lovMaster.get(0).getLovSubType(), checkData.getMaterial());
				String priceGroup = getPriceGroup(checkData.getConditionType());
				
				if(BeanUtil.isNotEmpty(product) && !priceGroup.isEmpty()) {
					
					if(checkData.getOrderQuantityUnit().equalsIgnoreCase(product.getUnitName())) {
						BigDecimal price = new BigDecimal(salePrice.getConditionRateAmount().getValue());
						BigDecimal quantity = new BigDecimal(salePrice.getConditionQuantity().getValue());
						BigDecimal priceIncVat = new BigDecimal(0.00);
						BigDecimal priceExcVat = new BigDecimal(0.00);
						BigDecimal vatAmt = new BigDecimal(0.00);
						String vatRate = null;
						
						if(product.getVatType().equalsIgnoreCase("Y")) {
							priceIncVat = price.divide(quantity, 2, RoundingMode.HALF_UP);
							priceExcVat = (price.multiply(new BigDecimal(100)).divide(new BigDecimal(107), 2, RoundingMode.HALF_UP)).divide(quantity);
							vatAmt = priceIncVat.subtract(priceExcVat);
							vatRate = "7.00";
						} else {
							priceIncVat = price.divide(quantity, 2, RoundingMode.HALF_UP);
							priceExcVat = priceIncVat;
							vatRate = "0.00";
						}
						
						
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
				        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
				        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

						String formatStartDate;
						Date finalEffectiveDate = null;
						Date finalExpireDate = null;
						
						try {
							String validityStartDate = (BeanUtil.isEmpty(checkData.getConditionValidityStartDate()) || checkData.getConditionValidityStartDate() == null) ? salePrice.getConditionValidityStartDate():checkData.getConditionValidityStartDate();
							String validityEndDate = (BeanUtil.isEmpty(checkData.getConditionValidityEndDate()) || checkData.getConditionValidityEndDate() == null) ? salePrice.getConditionValidityEndDate():checkData.getConditionValidityEndDate();
							
							Date effectiveDate = inputFormat.parse(validityStartDate);
							formatStartDate = outputFormat.format(effectiveDate);
							finalEffectiveDate = outputFormat.parse(formatStartDate);
							
							String expDate = null == validityEndDate || BeanUtil.isEmpty(validityEndDate) ? "9999-12-31" : validityEndDate;
							Date expireDate = inputFormat.parse(expDate);
							String formatEndDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(expireDate) + " 23:59:59";
							finalExpireDate = formatter.parse(formatEndDate);
			
						} catch (ParseException e) {
							log.error("Failed to parse date", e);
							return new ProcessResult(false, null, "Failed to parse date: " + e.getMessage());
						}

						// validate duplicate effective date
						Date today = new Date();
						PriceMst priceMstUnique = priceMstDao.getPriceMstByUnique(
								lovMaster.get(0).getLovSubType(),
								product.getProductId(),
								priceGroup,
								formatStartDate,
								"CASH"
						);

						if (BeanUtil.isNotEmpty(priceMstUnique)) {
							if (!finalEffectiveDate.after(today)) {
								// duplicate + EffectiveDate <= today → skip
								continue;
							} else {
								// duplicate + EffectiveDate > today → delete old and insert new
								priceMstDao.delete(priceMstUnique);
							}
						}
						
						PriceMst priceMst = new PriceMst();
						priceMst.setActiveFlg("Y");
						priceMst.setCompany(lovMaster.get(0).getLovSubType());
						priceMst.setPriceGroup(priceGroup);
						priceMst.setProductId(product.getProductId());
						priceMst.setVatType(product.getVatType());
						priceMst.setIncVat(priceIncVat);
						priceMst.setExcVat(priceExcVat);
						priceMst.setVatAmt(vatAmt);
						priceMst.setVatRate(vatRate);
						priceMst.setEffectiveDt(finalEffectiveDate);
						priceMst.setExpireDt(finalExpireDate);
						priceMst.setPaymentMethod("CASH");
						priceMst.setCreateValue(getCreateValue(today));
						priceMst.setPriceId(priceMstDao.getSeq());
						
						listPriceMst.add(priceMst);
					}
				}
			}
		}

		if (listPriceMst.isEmpty()) {
			return new ProcessResult(
					false,
					null,
					"No have data to insert (ConditionType, OrderQuantityUnit or EffectiveDate not match)"
			);
		}

		try {
			priceMstDao.insertList(listPriceMst);
			updateExpPriceMat(listPriceMst);
		} catch (Exception e) {
			e.printStackTrace();
			return new ProcessResult(false, e.getMessage(), "Fail");
		}
		return new ProcessResult(true, null, "Success");
	}
    
    private String getPriceGroup(String conditionType) {
        return switch (conditionType) {
            case "VKP0" -> "EUP";
            case "ZP02" -> "STAFF";
            case "ZINT" -> "BU";
            case "ZP01" -> "DLPWH";
            default -> "";
        };
	}

	private void updateExpPriceMat(List<PriceMst> listPriceMst) {
		Date today = new Date();

		Calendar cal = Calendar.getInstance(Locale.US);
		cal.set(9999, Calendar.DECEMBER, 31, 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 0);
		final Date DEFAULT_EXPIRE_DATE = cal.getTime();

		for (PriceMst newPrice : listPriceMst) {

			List<PriceMst> toUpdate = new ArrayList<>();
			List<PriceMst> toDelete = new ArrayList<>();
			List<PriceMstHist> toHistory = new ArrayList<>();

			Date newEffective = newPrice.getEffectiveDt();
			Date newExpire = newPrice.getExpireDt();

			// query existing price and exclude new price
			List<PriceMst> existingPrices = priceMstDao.listPriceMstByEffectiveDate(
					newPrice.getCompany(),
					newPrice.getPriceGroup(),
					newPrice.getProductId(),
					newPrice.getPaymentMethod()
			).stream().filter(pm -> !pm.getPriceId().equals(newPrice.getPriceId())).toList();

			for (PriceMst existing : existingPrices) {
				Date effDate = existing.getEffectiveDt();
				Date expDate = existing.getExpireDt() != null ? existing.getExpireDt() : DEFAULT_EXPIRE_DATE;

				// 1. Expired already -> move to history
				if (expDate.before(today)) {
					toHistory.add(buildHistory(existing));
					toDelete.add(existing);
					continue;
				}

				// overlap (inclusive): effDate <= newExpire && expDate >= newEffective
				boolean overlaps = !effDate.after(newExpire) && !expDate.before(newEffective);
				boolean oldStartsBeforeNew = effDate.before(newEffective);
				boolean oldEndsAfterNew = expDate.after(newExpire);

				if (!overlaps) {
					// 2. No overlap -> do nothing
					// old: [------]
					// new:           [------]
					toUpdate.add(existing);
					continue;
				}

				if (oldStartsBeforeNew) {
					// 3. old เริ่มก่อน new → ตัด expire
					// old: [----------]
					// new:       [----------]
					// res: [----]
					existing.setExpireDt(toEndOfDay(newEffective));
					toUpdate.add(existing);

				} else if (oldEndsAfterNew) {
					// 4. ทับซ้อนด้านหลัง → เลื่อน effective
					// old:       [----------]
					// new: [----------]
					// res:             [----]
					existing.setEffectiveDt(plusOneDay(newExpire));
					toUpdate.add(existing);

				} else {
					// 5. new overlap old all -> remove
					// old:   [------]
					// new: [----------]
					toDelete.add(existing);
				}
			}

			priceMstDao.updateList(toUpdate);

			if (BeanUtil.isNotEmpty(toDelete)) {
				priceMstDao.deleteList(toDelete);
			}

			if (BeanUtil.isNotEmpty(toHistory)) {
				priceMstHistDao.insertList(toHistory);
			}
		}
	}

	private PriceMstHist buildHistory(PriceMst priceMst) {
		PriceMstHist hist = new PriceMstHist();
		try {
			TDMDataUtility.copyProperties(hist, priceMst);
		} catch (Exception e) {
			log.error("Failed to copy PriceMst to history", e);
			e.printStackTrace();
		}
		MasterValueHist masterValue = new MasterValueHist();
		masterValue.setCreated(new Date());
		masterValue.setCreatedBy(DT_APP);
		hist.setSequence(1L);
		hist.setCreateValue(masterValue);
		hist.setPriceHistId(priceMstHistDao.getSeq());
		return hist;
	}

	private Date plusOneDay(Date date) {
		// plus 1 day and set time to 00:00:00 
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date toEndOfDay(Date date) {
		// remove 1 day and set time to 23:59:59
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
    
    private SapHandlePriceMstBean callPriceMstSTFCloud(SapHandlePriceMstBean input) {
		SapHandlePriceMstBean res = new SapHandlePriceMstBean();
		try {
			ProcessResult processResult = insertPriceMstForStaff(input);
			if (!processResult.isSuccess()) {
				res.setResultCode("50000");
				res.setResultDescription("Insert Price master for staff is fail");
				res.setDeveloperMessage("Insert Price master for staff is fail");
				return res ;
			}
			
		}catch (Exception e) {
			res.setResultCode("50000");
			res.setResultDescription("Fail");
			res.setDeveloperMessage(e.getMessage());
			return res ;
		}
		
		res.setResultCode("20000");
		res.setResultDescription("Success");
		res.setDeveloperMessage("Success");
		return res ;
	}
    
    private ProcessResult insertPriceMstForStaff(SapHandlePriceMstBean listPrice) throws ParseException {
		String status = "";

		if (listPrice.getSalesPricingConditionRecord().size() <= 0) {
			return new ProcessResult(false, null, "No have data to insert");
		}

		for (SalesPricingConditionRecordBean salePrice : listPrice.getSalesPricingConditionRecord()) {

			for (ConditionValidityBean checkData : salePrice.getConditionValidity()) {
				String matCode = checkData.getMaterial();
				String saleOrganize = checkData.getSalesOrganization();

				List<LovMaster> lovMaster = lovMasterDao.listLovMasterByCriteria("SAP_S4_SALEORG_MAP_COMPANY", null,
						null, saleOrganize, null);
				ProductMst product = productMstDao.getProductMstByUniqueV2(lovMaster.get(0).getLovSubType(), matCode);
				//ProductMst product = productMstDao.getProductMstByUnique(lovMaster.get(0).getLovSubType(), matCode);

				if (BeanUtil.isNotEmpty(product)) {
					if ("ZP02".equals(checkData.getConditionType()) && checkData.getOrderQuantityUnit().equalsIgnoreCase(product.getUnitName())) {
						if (requestOrderConfigDao.listRequstOrderConfig(checkData.getMaterial()).isEmpty()) {
							try {
								RequestOrderConfig requestOrderConfig = new RequestOrderConfig();

								RequestOrderConfigPK pk = new RequestOrderConfigPK();
								pk.setMatCode(checkData.getMaterial());
								pk.setLocationCode(1501L);
								pk.setSubStock("STF");

								requestOrderConfig.setMaxReqPerDay(500L);
								requestOrderConfig.setActiveFlg("Y");
								requestOrderConfig.setCreateValue(getCreateValue(new Date()));
								requestOrderConfig.setPk(pk);
								requestOrderConfigDao.insert(requestOrderConfig);
								status = "s";
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							if (!status.equals("s")) {
								status = "f";
							}
						}
					}
				}
			}
		}

		if (status.equals("s")) {
			return new ProcessResult(true, null, "Success");
		} else {
			return new ProcessResult(false, null, "Fail");
		}
	}

}
