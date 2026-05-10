package th.co.ais.dt.core.controller.impl.mt;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import th.co.ais.dt.entity.mt.AssignmentGroupDtl;
import th.co.ais.dt.entity.mt.AssignmentGroupMst;
import th.co.ais.dt.entity.mt.InterfaceTransLog;
import th.co.ais.dt.entity.mt.InterfaceTransLogPk;
import th.co.ais.dt.entity.mt.MappingFieldMst;
import th.co.ais.dt.entity.pr.PriceMst;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.dto.User;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.mt.IProducMasterServiceService;
import th.co.ais.dt.service.core.interfaces.mt.ProductMasterFromSapService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.util.BeanUtil;
//import th.co.ais.dt.util.JBossBackEndProperties;
import th.co.ais.dt.util.TDMDataUtility;
import th.co.ais.dt.controller.dto.CreateProductMasterFromDtItem;
import th.co.ais.dt.controller.dto.CreateProductMasterFromSapBean;
import th.co.ais.dt.controller.dto.CreateProductMasterFromSapItem;
import th.co.ais.dt.controller.dto.ProductMasterServiceBean;

@RestController
@Slf4j
@AllArgsConstructor
public class ProductMasterServiceWebImpl  {
	
	final String prefixPath = "api/productMasterSevice/v1";
	
	
	private final ProductMasterFromSapService productMasterFromSapService ;
	
	private final LovMasterService lovMasterService ;
	
	private final IProducMasterServiceService producMasterServiceService ;
	

	@RequestMapping(
			  value = prefixPath +"/create-product-master-from-sap",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(CreateProductMasterFromSapBean.class)
	public ResponseEntity<String> createProductMasterFromSap(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		CreateProductMasterFromSapBean in = new CreateProductMasterFromSapBean();
		List<CreateProductMasterFromSapItem> listErrMappingProductMsts = new ArrayList<CreateProductMasterFromSapItem>();
		Date now = new Date();
		String createdBy = "JOB_DT";
		boolean isHaveError = false;
		Gson gson = new Gson();
					
		try {
			
			try {
				in = gson.fromJson(jsonRequest, CreateProductMasterFromSapBean.class);	
			} catch(Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\",\"messageCode\":\"E0001\"}", httpHeaders, HttpStatus.OK);
			}
			
			if(in != null && BeanUtil.isNotEmpty(in.getProductMasterList())) {
				Long logSeq = productMasterFromSapService.getInterfaceTransLogSeq();
				int logNo = 1;
				for(CreateProductMasterFromSapItem el : in.getProductMasterList()) {
					try {
						
						InterfaceTransLog interfaceTransLog = this.mappingProductMstOneRow(el);
						if(interfaceTransLog.getStatus().equals("F")){
							isHaveError = true;
							el.setERR_MSG(interfaceTransLog.getMsgOutput1());
							listErrMappingProductMsts.add(el);							
						}
						
						
						interfaceTransLog = prepareLogInsert(el, interfaceTransLog, logSeq, logNo) ;
						productMasterFromSapService.insertInterfaceTransLog(interfaceTransLog);
					}catch (Exception e) {
						log.info(e.getMessage());
					} 
					
					logNo ++;
				}
								
			}
		} catch(Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);
	}
	
	private InterfaceTransLog prepareLogInsert(CreateProductMasterFromSapItem mappingProductMst, InterfaceTransLog interfaceTransLog, Long requestId , int logNo){
		StringBuilder remark = null;
		
		try {
			InterfaceTransLogPk interfaceTransLogPk = new InterfaceTransLogPk() ;
			interfaceTransLogPk.setLogSeq(requestId);
			interfaceTransLogPk.setLogNo(Long.valueOf(Integer.valueOf(logNo)));
			interfaceTransLog.setPk(interfaceTransLogPk);
			remark = new StringBuilder();
			
			remark.append("ACC_ASSIGN_GROUP:"+mappingProductMst.getACC_ASSIGN_GROUP());
			remark.append("|BRAND:"+mappingProductMst.getBRAND());
			remark.append("|CHANGE_NO:"+mappingProductMst.getCHANGE_NO());
			remark.append("|COLOR:"+mappingProductMst.getCOLOR());
			remark.append("|DOCUMENT:"+mappingProductMst.getDOCUMENT());
			remark.append("|FLAG_DELETE:"+mappingProductMst.getFLAG_DELETE());
			remark.append("|FLAG_SERIAL:"+mappingProductMst.getFLAG_SERIAL());
			remark.append("|FORMAT:"+mappingProductMst.getFORMAT());
			remark.append("|MATERIAL_DESC:"+mappingProductMst.getMATERIAL_DESC());
			remark.append("|MATERIAL_NO:"+mappingProductMst.getMATERIAL_NO());
			remark.append("|MATRIAL_GROUP:"+mappingProductMst.getMATRIAL_GROUP());
			remark.append("|MODEL:"+mappingProductMst.getMODEL());
			remark.append("|OLD_MATERIAL_NO:"+mappingProductMst.getOLD_MATERIAL_NO());
			remark.append("|REF_MAT:"+mappingProductMst.getREF_MAT());
			remark.append("|SALES_ORG:"+mappingProductMst.getSALES_ORG());
			remark.append("|SIM_BRAND:"+mappingProductMst.getSIM_BRAND());
			remark.append("|TRANS_UPD_BY:"+mappingProductMst.getTRANS_UPD_BY());
			remark.append("|TRANS_UPD_DTM:"+mappingProductMst.getTRANS_UPD_DTM());
			remark.append("|UOM:"+mappingProductMst.getUOM());
			remark.append("|VALUATION_CLASS:"+mappingProductMst.getVALUATION_CLASS());
			remark.append("|VAT_TYPE:"+mappingProductMst.getVAT_TYPE());
			remark.append("|VENDOR_CODE:"+mappingProductMst.getVENDOR_CODE());
			remark.append("|CATEGORY:"+mappingProductMst.getCATEGORY());
			remark.append("|VTWEG:"+mappingProductMst.getVTWEG());
			remark.append("|SPART:"+mappingProductMst.getSPART());
			remark.append("|PRODH:"+mappingProductMst.getPRODH());
			
			interfaceTransLog.setMsgInput1(remark.toString());
			interfaceTransLog.setCreateValue(new MasterValue());
			interfaceTransLog.getCreateValue().setCreatedBy("TDMAPP");
			interfaceTransLog.getCreateValue().setCreated(new Date());
			
			this.splitMsgInput(interfaceTransLog);
			this.splitMsgOutput(interfaceTransLog);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		
		return interfaceTransLog;		
	}
	
	private void splitMsgInput(InterfaceTransLog interfaceTransLog){
		String logInput = interfaceTransLog.getMsgInput1();
		int logSize = 4000;
		
		if(logInput.length()<logSize){
			interfaceTransLog.setMsgInput1(logInput);
		}else{
			interfaceTransLog.setMsgInput1(this.subString(logInput, logSize));
			if(logInput.length()<2*logSize){
				interfaceTransLog.setMsgInput2(logInput.substring(logSize, logInput.length()));
			}else{
				interfaceTransLog.setMsgInput2(logInput.substring(logSize, 2*logSize));
			}
		}
	}
	
	private void splitMsgOutput(InterfaceTransLog interfaceTransLog){
		String logOutput = interfaceTransLog.getMsgOutput1();
		int logSize = 4000;
		
		if(logOutput.length()<logSize){
			interfaceTransLog.setMsgOutput1(logOutput);
		}else{
			interfaceTransLog.setMsgOutput1(this.subString(logOutput, logSize));
			if(logOutput.length()<2*logSize){
				interfaceTransLog.setMsgOutput2(logOutput.substring(logSize, logOutput.length()));
			}else{
				interfaceTransLog.setMsgOutput2(logOutput.substring(logSize, 2*logSize));
			}
		}
	}
	
	private String subString(String input, int maxLength){
		String output = null;
		try {
			if(input!=null){
				output = (input.length()>maxLength)?input.substring(0, maxLength):input;
			}
		} catch (Exception e) {
			output = null;
		}
		return output;
	}
	
	private InterfaceTransLog mappingProductMstOneRow(CreateProductMasterFromSapItem item) {
		InterfaceTransLog logd = new InterfaceTransLog();
		StringBuffer error = new StringBuffer();
		try {
			ProductMst productMst = new ProductMst();
			//step1 validate ACC_ASSIGN_GROUP / MATRIAL_GROUP / SALES_ORG / MATERIAL_NO / MATRIAL_GROUP / VAT_TYPE / FLAG_SERIAL / FLAG_DELETE / TRANS_UPD_DTM / GRADE
			//step2 validate dup mat code and company
			//step3 validate ref mat
			//step4 insert or update matcode and checkout ref mat and create price

			error = validate_step1(item, productMst) ;
			
			if(BeanUtil.isNotEmpty(error.toString())) {
				logd.setStatus("F");
				logd.setMsgOutput1(error.toString());
				return logd ;
			}
			
            error = validate_step2(item, productMst) ;
			
			if(BeanUtil.isNotEmpty(error.toString())) {
				logd.setStatus("F");
				logd.setMsgOutput1(error.toString());
				return logd ;
			}
			
            error = validate_step3(item, productMst) ;
			
			if(BeanUtil.isNotEmpty(error.toString())) {
				logd.setStatus("F");
				logd.setMsgOutput1(error.toString());
				return logd ;
			}
			
			error = validate_step4(productMst,logd);
			
			if(BeanUtil.isNotEmpty(error.toString())) {
				logd.setStatus("F");
				logd.setMsgOutput1(error.toString());
				return logd ;
			}
			
		}catch (Exception e) {
			log.info(e.getMessage());
			logd.setStatus("F");
			logd.setMsgOutput1(error.toString() + "|" + e.getMessage());
		}
		return logd ;
	}
	
	//step1 validate ACC_ASSIGN_GROUP / MATRIAL_GROUP / SALES_ORG / MATERIAL_NO / VAT_TYPE / FLAG_SERIAL / FLAG_DELETE / TRANS_UPD_DTM /GRADE
	private StringBuffer validate_step1(CreateProductMasterFromSapItem item , ProductMst productMst) {
		StringBuffer error = new StringBuffer();
		
		try {
			if(BeanUtil.isEmpty(item.getACC_ASSIGN_GROUP())) {
				error.append("[ACC_ASSIGN_GROUP : ACC_ASSIGN_GROUP is null]");
			}else {
				//set product type /subtype
				List<AssignmentGroupMst> listAssignmentGroupMst = productMasterFromSapService.getAssignmentGroupMstByASSIGNMENT_GROUP_CODE(item.getACC_ASSIGN_GROUP().trim());
				if(BeanUtil.isNotEmpty(listAssignmentGroupMst)) {
					
					for(AssignmentGroupMst assignmentGroupMst : listAssignmentGroupMst){
						if(assignmentGroupMst.getMatGroupIncFlg()!=null && assignmentGroupMst.getMatGroupIncFlg().equals("A")){
							productMst.setAssignmentGroup(assignmentGroupMst.getAssignmentGroupCode());
							productMst.setProductType(assignmentGroupMst.getProductType()); 
							productMst.setProductSubType(assignmentGroupMst.getProductSubtype()); 
							productMst.setMatGroup(item.getMATRIAL_GROUP());
							break;
						}else if(assignmentGroupMst.getMatGroupIncFlg()!=null && assignmentGroupMst.getMatGroupIncFlg().equals("Y")){
							
							if(BeanUtil.isEmpty(item.getMATRIAL_GROUP())) {
								error.append("[MATRIAL_GROUP : MATRIAL_GROUP is null]\"");
							}else {
								AssignmentGroupDtl assignmentGroupDtl = productMasterFromSapService.getAssignmentGroupDtlByUnique(assignmentGroupMst.getAssignmentGroupID(), item.getMATRIAL_GROUP().trim());
								if(assignmentGroupDtl!=null){
									productMst.setAssignmentGroup(assignmentGroupMst.getAssignmentGroupCode());
									productMst.setProductType(assignmentGroupMst.getProductType()); 
									productMst.setProductSubType(assignmentGroupMst.getProductSubtype()); 
									productMst.setMatGroup(item.getMATRIAL_GROUP());
									break;
								}
							}	
						}
//						else if(assignmentGroupMst.getMatGroupIncFlg()!=null && assignmentGroupMst.getMatGroupIncFlg().equals("N")){
//							AssignmentGroupDtl assignmentGroupDtl = assignmentGroupDtlDao.getAssignmentGroupDtlByUnique(assignmentGroupMst.getAssignmentGroupID(), mappingProductMst.getMATRIAL_GROUP());
//							if(assignmentGroupDtl==null){
//								productMst.setAssignmentGroup(assignmentGroupMst.getAssignmentGroupCode());
//								productMst.setProductType(assignmentGroupMst.getProductType()); 
//								productMst.setProductSubType(assignmentGroupMst.getProductSubtype()); 
//								break;
//							}
//						}
					}
					
					if(BeanUtil.isEmpty(productMst.getProductType())) {
						error.append("[ACC_ASSIGN_GROUP and MATRIAL_GROUP not match]");
					}
					
					
				}else {
					error.append("[ACC_ASSIGN_GROUP : ACC_ASSIGN_GROUP not found in TDM System]");
				}
			}
			
			if(BeanUtil.isEmpty(item.getSALES_ORG())) {
				error.append("[SALES_ORG : SALES_ORG is null]");
			}else {
				List<LovMaster> listLovCompany = lovMasterService.listLovMasterByCriteria("CONVERT_COMPANY",null, item.getSALES_ORG().trim(),null, null, "Y",null,null,null,null,null,null);
                if(BeanUtil.isNotEmpty(listLovCompany)) {
                	productMst.setCompany(listLovCompany.get(0).getLovVal());
                }else {
                	error.append("[SALES_ORG : SALES_ORG not found in TDM System]");
                }
			}
			
			if(BeanUtil.isEmpty(item.getMATERIAL_NO())) {
				error.append("[MATERIAL_NO : MATERIAL_NO is null]");
			}
			
			if(BeanUtil.isEmpty(item.getFLAG_SERIAL())) {
				productMst.setMatType("Non Serial");
			}else if(BeanUtil.isNotEmpty(item.getFLAG_SERIAL()) && item.getFLAG_SERIAL().trim().equals("X") ) {
				productMst.setMatType("Serial");
			}else {
				error.append("[FLAG_SERIAL : FLAG_SERIAL is invalid input]");
			}
			
			if(BeanUtil.isEmpty(item.getFLAG_DELETE())) {
				productMst.setActiveFlag("Y");
			}else if(BeanUtil.isNotEmpty(item.getFLAG_DELETE()) && item.getFLAG_DELETE().trim().equals("X") ) {
				productMst.setActiveFlag("N");
			}else {
				error.append("[FLAG_DELETE : FLAG_DELETE is invalid input]");
			}
			
			if(BeanUtil.isEmpty(item.getTRANS_UPD_DTM())) {
				error.append("[TRANS_UPD_DTM : TRANS_UPD_DTM is null]");
			}else {
				try {
					if(item.getTRANS_UPD_DTM().length()!=8){
						error.append("[TRANS_UPD_DTM : TRANS_UPD_DTM is format yyyyMMdd Only]");					
					}else{
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);	
						
						MasterValue userSAP = new MasterValue();
						userSAP.setCreated(dateFormat.parse(item.getTRANS_UPD_DTM()));
						userSAP.setCreatedBy(item.getTRANS_UPD_BY());
						userSAP.setLastUpd(dateFormat.parse(item.getTRANS_UPD_DTM()));
						userSAP.setLastUpdBy(item.getTRANS_UPD_BY());
						productMst.setTransValue(userSAP);
					}					
				} catch (Exception e) {
					error.append("[TRANS_UPD_DTM : TRANS_UPD_DTM is format yyyyMMdd Only]");

				}
			}
			
			if(BeanUtil.isEmpty(item.getVAT_TYPE())) {
				error.append("[VAT_TYPE : VAT_TYPE is null]");
			}else {
				List<LovMaster> listLovVattype = lovMasterService.listLovMasterByCriteria("VAT_TYPE",null, item.getVAT_TYPE().trim(),null, null, "Y",null,null,null,null,null,null);
                if(BeanUtil.isNotEmpty(listLovVattype)) {
                	productMst.setVatType(listLovVattype.get(0).getLovVal());
                }else {
                	error.append("[VAT_TYPE : VAT_TYPE not found in TDM System]");
                }
			}
			
			
			
//			productMst.setVendorCode(item.getVENDOR_CODE());
//			productMst.setCategory(item.getCATEGORY());
			
			
			productMst.setDivision((BeanUtil.isNotEmpty(item.getSPART()) ? item.getSPART().trim() : "") );
			productMst.setProduct_hierarchy((BeanUtil.isNotEmpty(item.getPRODH()) ? item.getPRODH().trim() : ""));
			productMst.setMatCode((BeanUtil.isNotEmpty(item.getMATERIAL_NO()) ? item.getMATERIAL_NO().trim() : "") );
			productMst.setUnitName((BeanUtil.isNotEmpty(item.getUOM()) ? item.getUOM().trim() : "") );
			productMst.setSapDescription((BeanUtil.isNotEmpty(item.getMATERIAL_DESC()) ? item.getMATERIAL_DESC().trim() : "") );
			productMst.setTdmDescription((BeanUtil.isNotEmpty(item.getMATERIAL_DESC()) ? item.getMATERIAL_DESC().trim() : "") );
			productMst.setValuationClass((BeanUtil.isNotEmpty(item.getVALUATION_CLASS()) ? item.getVALUATION_CLASS().trim() : "") );
			productMst.setRefMatCode((BeanUtil.isNotEmpty(item.getREF_MAT()) ? item.getREF_MAT().trim() : "") );
			productMst.setProdTDMFlag("N");
			
			if(BeanUtil.isNotEmpty(productMst.getMatCode()) && productMst.getMatCode().length() > 3 ) {
				String prefixMatCode = productMst.getMatCode().substring(0, 3);
				List<LovMaster> listLovgrade = lovMasterService.listLovMasterByCriteria("PREFIX_MAT_GRADE",null, prefixMatCode,null, null, "Y",null,null,null,null,null,null);
				
				if(BeanUtil.isNotEmpty(listLovgrade)){
					productMst.setGrade(listLovgrade.get(0).getLovVal());
				}
			}
            
			if (productMst.getProductType().equals("DEVICE") && productMst.getProductSubType().equals("HANDSET")) {
				if (item.getCHANGE_NO() != null && item.getCHANGE_NO().length() > 0) {
					productMst.setProductSubType("HANDSET BUNDLE");
				}
			}
			

			productMst.setBrand(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "BRAND", 30));
			productMst.setModel(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "MODEL", 30));
			productMst.setColor(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "COLOR", 30));
			productMst.setNetwork(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "NETWORK", 30));
			productMst.setRegion(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "REGION", 30));
			productMst.setSubRegion(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "SUB_REGION", 30));
			productMst.setCapacity(getResultFieldForAdd(item, productMst.getProductType(), productMst.getProductSubType(), "CAPACITY", 30));
	
            if(productMst.getActiveFlag().equals("N")) {
            	productMst.setExpireDate(productMst.getTransValue().getCreated());
            }
            
            if(BeanUtil.isEmpty(productMst.getBrand()) && BeanUtil.isNotEmpty(productMst.getModel()) ) {
            	productMst.setBrand("N/A");
            }
            
            MasterValue userDT = new MasterValue();
            userDT.setCreated(new Date());
            userDT.setCreatedBy("TDMAPP");
            userDT.setLastUpd(new Date());
            userDT.setLastUpdBy("TDMAPP");
			productMst.setCreateValue(userDT);
			
		}catch (Exception e) {
			log.info(e.getMessage());
			error.append("|" + e.getMessage());
		}
		
		return error ;
		
	}
	
	//step2 validate dup mat code and company
	private StringBuffer validate_step2(CreateProductMasterFromSapItem item , ProductMst productMst) {
		StringBuffer error = new StringBuffer();
		

		try {
			
			List<ProductMst> listPro = productMasterFromSapService.getProductMstByUnique(productMst.getCompany(), productMst.getMatCode()) ;
			
			if(BeanUtil.isNotEmpty(listPro)) { // update
				
				if( !(productMst.getProductType().equals(listPro.get(0).getProductType()) && 
						productMst.getProductSubType().equals(listPro.get(0).getProductSubType())) ) {
					error.append("[Can't change ACC_ASSIGN_GROUP]");
				}else {
					Long productId = listPro.get(0).getProductId() ;
					String SAP_CREATE_BY =(listPro.get(0).getTransValue() != null ? listPro.get(0).getTransValue().getCreatedBy() : null ) ;
					Date SAP_CREATE_DATE =(listPro.get(0).getTransValue() != null ? listPro.get(0).getTransValue().getCreated() : null) ;
					
					if(BeanUtil.isNotEmpty(listPro.get(0).getAttribute03())  && listPro.get(0).getAttribute03().equals("SIM") ) {
						productMst.setBrand(listPro.get(0).getBrand());
						productMst.setModel(listPro.get(0).getModel());
					}
					
					
					//set old velues
					//productMst.setTdmDescription(listPro.get(0).getTdmDescription());
					productMst.setAttribute01(listPro.get(0).getAttribute01());
					productMst.setAttribute02(listPro.get(0).getAttribute02());
					productMst.setAttribute03(listPro.get(0).getAttribute03());
					productMst.setEffectiveDate(listPro.get(0).getEffectiveDate());
					
					
					productMst.setProductId(productId);
					productMst.getTransValue().setCreated(SAP_CREATE_DATE);
					productMst.getTransValue().setCreatedBy(SAP_CREATE_BY);
					
					productMst.getCreateValue().setCreated( listPro.get(0).getCreateValue().getCreated());
					productMst.getCreateValue().setCreatedBy(listPro.get(0).getCreateValue().getCreatedBy());
				}
				
			}
			else { // insert
				productMst.setEffectiveDate(productMst.getTransValue().getCreated());	
			}
			
		}catch (Exception e) {
			log.info(e.getMessage());
			error.append("|" + e.getMessage());
		}
		
		return error ;
	}
	
	// step3 validate ref mat
	private StringBuffer validate_step3(CreateProductMasterFromSapItem item , ProductMst productMst) {
		StringBuffer error = new StringBuffer();
		try {
			if(BeanUtil.isNotEmpty(productMst.getRefMatCode()) ) {
				List<ProductMst> listPro = productMasterFromSapService.getProductMstByUnique(productMst.getCompany(), productMst.getRefMatCode()) ;
				if(BeanUtil.isEmpty(listPro)) {
					error.append("[Ref matcode :"+item.getREF_MAT()+" not found]");
				}
			}
		}catch (Exception e) {
			log.info(e.getMessage());
			error.append("|" + e.getMessage());
		}
		
		return error ;
	}
	
	private StringBuffer validate_step4(ProductMst productMst , InterfaceTransLog logd) {
		StringBuffer error = new StringBuffer();
		
		try {
			ProcessResult save = productMasterFromSapService.doProductMstAndPrice(productMst);
			if(save.isSuccess()) {
				ProductMst ProductMstRe = (ProductMst)save.getResultObject();
				if(ProductMstRe.getValidateResult().equals("I")) {
					logd.setOperationType("INSERT");
					logd.setMsgOutput1("SUCCESS : Insert Product_ID:"+ProductMstRe.getProductId());
				}else {
					logd.setOperationType("UPDATE");
					logd.setMsgOutput1("SUCCESS : Edit Product_ID:"+ProductMstRe.getProductId());
				}
				logd.setStatus("S");
			}else {
				logd.setStatus("F");
				error.append("|" + save.getMessage());
			}
			
		}catch (Exception e) {
			log.info(e.getMessage());
			error.append("|" + e.getMessage());
			logd.setStatus("F");
		}
		
		
		logd.setMasterOperationId((productMst.getProductId()!=null ? productMst.getProductId().toString() : null ));
		
		
		return error ;
		}
	
	private String getResultFieldForAdd(CreateProductMasterFromSapItem mappingProductMst,String productType, String productSubType, String fieldName , int maxLength ) {
		
		 //String sequence,int maxLength
		 
		MappingFieldMst mappingFieldMst = productMasterFromSapService.getMappingFieldMstByKey(productType, productSubType, fieldName) ;
		
		String sequence = "";
		if (mappingFieldMst != null) {
			sequence = mappingFieldMst.getSequence();
		} else {
			sequence = "0";
		}
		
		String resultStr = "";
		int seqInt = Integer.valueOf(sequence) ;
		switch (seqInt) {
			//case 1: resultStr = mappingProductMst.getMATERIAL_NO().trim();						break;
			//case 2: resultStr = mappingProductMst.getSALES_ORG().trim();						break;
			//case 3: resultStr = mappingProductMst.getMATRIAL_GROUP().trim();					break;
			case 4: resultStr =(mappingProductMst.getBRAND() != null ? mappingProductMst.getBRAND().trim() : null ) ;				break;  
			case 5: resultStr = (mappingProductMst.getMODEL() != null ? mappingProductMst.getMODEL().trim() : null  );				break;  
			case 6: resultStr = (mappingProductMst.getCOLOR() != null ? mappingProductMst.getCOLOR().trim() : null ) ;				break;  
			case 7: resultStr = (mappingProductMst.getOLD_MATERIAL_NO() != null ? mappingProductMst.getOLD_MATERIAL_NO().trim() : null );	    break;
			case 8: resultStr = (mappingProductMst.getCHANGE_NO() != null ? mappingProductMst.getCHANGE_NO().trim() : null ) ;			break;
			//case 9: resultStr = mappingProductMst.getUOM().trim();				break;
			//case 10: resultStr = mappingProductMst.getDOCUMENT().trim();			break;
			case 11: resultStr = (mappingProductMst.getFORMAT() != null ? mappingProductMst.getFORMAT().trim() : null ) ;			break;
			//case 12: resultStr = mappingProductMst.getMATERIAL_DESC().trim();					break;
			//case 13: resultStr = mappingProductMst.getVALUATION_CLASS().trim();					break;
			//case 14: resultStr = mappingProductMst.getACC_ASSIGN_GROUP().trim();				break;
			//case 15: resultStr = mappingProductMst.getFLAG_SERIAL().trim();						break;
			//case 16: resultStr = mappingProductMst.getREF_MAT().trim();							break;
			case 17: resultStr = (mappingProductMst.getSIM_BRAND() != null ? mappingProductMst.getSIM_BRAND().trim() : null  ) ;			break;
			//case 18: resultStr = mappingProductMst.getFLAG_DELETE().trim();						break;
			//case 19: resultStr = mappingProductMst.getVAT_TYPE().trim();						break;
			//case 20: resultStr = mappingProductMst.getTRANS_UPD_BY().trim();					break;
			//case 21: resultStr = mappingProductMst.getTRANS_UPD_DTM().trim();					break;// format yyyyMMdd
			default: resultStr = null;															break;
		}
		return maxLength!=0?this.subString(resultStr, maxLength):resultStr;
	}

	@RequestMapping(
			  value = prefixPath +"/create-product-master-from-dt",
			  method = RequestMethod.POST, 
			  produces = { "application/json" }
			 
			)
	@RegisterReflectionForBinding(CreateProductMasterFromDtItem.class)
	public ResponseEntity<String> createProductMasterFromDt(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
		Gson gson = new Gson();
		
		try {
			CreateProductMasterFromDtItem in = new CreateProductMasterFromDtItem();
			in = gson.fromJson(jsonRequest, CreateProductMasterFromDtItem.class);	
			
			if(BeanUtil.isNotEmpty(in.getListProductMstDt())) {
				for(ProductMst el : in.getListProductMstDt()) {
					try {
						List<ProductMst> listPro = productMasterFromSapService.getProductMstByUnique(el.getCompany(), el.getMatCode()) ;
                        if(BeanUtil.isNotEmpty(listPro)) {
                        	
                        	if(!String.valueOf(listPro.get(0).getProductId()).equals(el.getTmpProductIdFromDt())) {
                        		throw new Exception("product Id not match");
                        	}
                        	
                        	MasterValue userDT = new MasterValue();
                            userDT.setCreated(listPro.get(0).getCreateValue().getCreated());
                            userDT.setCreatedBy(listPro.get(0).getCreateValue().getCreatedBy());
                            userDT.setLastUpd(new Date());
                            userDT.setLastUpdBy("TDMAPP");
                        	el.setProductId(listPro.get(0).getProductId());
                        	el.setCreateValue(userDT);
                        	
                        	el.setEffectiveDate((BeanUtil.isNotEmpty(el.getEffectiveDateDt()) ? dateFormat.parse(el.getEffectiveDateDt()) : null  ));
                        	el.setExpireDate((BeanUtil.isNotEmpty(el.getExpireDateDt()) ? dateFormat.parse(el.getExpireDateDt()) : null  ));
                        	
                        	MasterValue transDT = new MasterValue();
                        	transDT.setCreated((BeanUtil.isNotEmpty(el.getTransCreatedDt()) ? dateFormat.parse(el.getTransCreatedDt()) : null  ));
                        	transDT.setCreatedBy(el.getTransCreatedByDt());
                        	transDT.setLastUpd((BeanUtil.isNotEmpty(el.getTransLastUpdDt()) ? dateFormat.parse(el.getTransLastUpdDt()) : null  ));
                        	transDT.setLastUpdBy(el.getTransCreatedByDt());
                        	el.setTransValue(transDT);

                        }else {
                        	el.setProductId(null);
                        	MasterValue userDT = new MasterValue();
                            userDT.setCreated(new Date());
                            userDT.setCreatedBy("TDMAPP");
                            userDT.setLastUpd(new Date());
                            userDT.setLastUpdBy("TDMAPP");
                        	//el.setProductId(listPro.get(0).getProductId());
                        	el.setCreateValue(userDT);
                        	
                        	el.setEffectiveDate((BeanUtil.isNotEmpty(el.getEffectiveDateDt()) ? dateFormat.parse(el.getEffectiveDateDt()) : null  ));
                        	el.setExpireDate((BeanUtil.isNotEmpty(el.getExpireDateDt()) ? dateFormat.parse(el.getExpireDateDt()) : null  ));
                        	
                        	MasterValue transDT = new MasterValue();
                        	transDT.setCreated((BeanUtil.isNotEmpty(el.getTransCreatedDt()) ? dateFormat.parse(el.getTransCreatedDt()) : null  ));
                        	transDT.setCreatedBy(el.getTransCreatedByDt());
                        	transDT.setLastUpd((BeanUtil.isNotEmpty(el.getTransLastUpdDt()) ? dateFormat.parse(el.getTransLastUpdDt()) : null  ));
                        	transDT.setLastUpdBy(el.getTransCreatedByDt());
                        	el.setTransValue(transDT);
                        }
						
						ProcessResult save = productMasterFromSapService.doProductMstAndPrice(el);
						if(save.isSuccess()) {
							
						}else {
							log.info(save.getMessage());
						}
						
					}catch (Exception e) {
						log.info(e.getMessage());
					}
				}
			}
	
		}catch (Exception e) {
			log.info(e.getMessage());
		}
		
		return new ResponseEntity<String>("{\"resultCode\":\"20000\",\"resultDescription\":\"Success\",\"developerMessage\":\"Success\"}", httpHeaders, HttpStatus.OK);

	}
	
	@RequestMapping(value = prefixPath +"/query-product-master-service",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ProductMasterServiceBean.class)
	public ResponseEntity<String> queryProductMasterService(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		try {
			
			ProductMst req = new ProductMst();
			ProductMasterServiceBean response = new ProductMasterServiceBean();
			log.info("---------- Search product master --------------");
			try {
				req = gson.fromJson(jsonRequest, ProductMst.class);

			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}

			if (req != null && BeanUtil.isNotEmpty(req)) {
				try {

					List<ProductMasterServiceBean> res = producMasterServiceService.serachProductMasterService(req);
				
					if (null == res && "".equals(res)) {
						return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data not found.\",\"developerMessage\":\"Data not found.\"}", httpHeaders, HttpStatus.OK);
					}
									
					response.setResultCode("20000");
					response.setDeveloperMessage("Success");
					response.setResultDescription("Success");
					response.setProductMasterList(res);
						
				} catch (Exception e) {
					response.setDeveloperMessage(e.getMessage());
					response.setResultCode("50000");
					response.setResultDescription(e.getMessage());
				}
				return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error("queryProductMasterService", e);
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@RequestMapping(value = prefixPath +"/update-product-master-service",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ProductMasterServiceBean.class)
	public ResponseEntity<String> updateProductMasterService(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		ProductMasterServiceBean req = new ProductMasterServiceBean();
		ProductMasterServiceBean res = new ProductMasterServiceBean();
		ProcessResult result = new ProcessResult(false,null,null);
		Gson gson = new Gson();
		log.info("---------- update product master -----------");
		
		
		try {
			
			req = gson.fromJson(jsonRequest, ProductMasterServiceBean.class);	
			
		} catch (Exception e) {
			res.setResultCode("20000");
			res.setResultDescription("JSON incorrect format");
			res.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		}
		
		try {
			
			boolean validUpdate = false;
			validUpdate = validateUpdateProductMaster(req, res);
			log.info("validUpdate ProductMaster:" + validUpdate);
			if(validUpdate) {
				result = producMasterServiceService.updateProductMasterService(req);
			} else {
				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

			}
			if(result.isSuccess()) {
				log.info("update success");
				res.setResultCode("20000");
				res.setDeveloperMessage(result.getMessage());
				res.setResultDescription(result.getMessage());
			} else {
				log.info("update fail");
				res.setResultCode("50000");
				res.setDeveloperMessage(result.getMessage());
				res.setResultDescription(result.getMessage());
			}
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

			
		} catch(ForceTerminateException e) {			
			res.setResultCode("50000");
			res.setResultDescription(e.getMessage());
			res.setDeveloperMessage(e.getMessage());
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

			
		} catch(Exception e) {
			log.error("updateProductMasterService", e);
			res.setResultCode("50000");
			res.setResultDescription("webImpl fail");
			res.setDeveloperMessage("webImpl fail");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
	}
	
    private boolean validateUpdateProductMaster(ProductMasterServiceBean req,ProductMasterServiceBean res){
		
		if(BeanUtil.isEmpty(req.getCompany())) {
			res.setResultCode("50000");
			res.setResultDescription("Company is null");
			res.setDeveloperMessage("Company is null");
			return false;			
		}
		if(BeanUtil.isEmpty(req.getProductType())) {
			res.setResultCode("50000");
			res.setResultDescription("ProductType is null");
			res.setDeveloperMessage("ProductType is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getProductSubType())) {
			res.setResultCode("50000");
			res.setResultDescription("ProductSubType is null");
			res.setDeveloperMessage("ProductSubType is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getMatCode())) {
			res.setResultCode("50000");
			res.setResultDescription("MatCode is null");
			res.setDeveloperMessage("MatCode is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getActiveFlag())) {
			res.setResultCode("50000");
			res.setResultDescription("ActiveFlag is null");
			res.setDeveloperMessage("ActiveFlag is null");
			return false;
		}
		if(req.getMatCode().length() > 50) {
			res.setResultCode("50000");
			res.setResultDescription("Max length of MatCode is 50 characters.");
			res.setDeveloperMessage("Max length of MatCode is 50 characters.");
			return false;
		}
		if(BeanUtil.isNotEmpty(req.getDescription()) && req.getDescription().length() > 300) {
			res.setResultCode("50000");
			res.setResultDescription("Max length of Description is 300 characters.");
			res.setDeveloperMessage("Max length of Description is 300 characters.");
			return false;
		}
		
		return true;
	}
    
	@RequestMapping(value = prefixPath +"/insert-product-master-service",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ProductMasterServiceBean.class)
	public ResponseEntity<String> insertProductMasterService(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
				
		ProductMasterServiceBean req = new ProductMasterServiceBean();
		ProductMasterServiceBean res = new ProductMasterServiceBean();		
		Gson gson = new Gson();
		
		log.info("--------------------insert product master --------------");
		
		
		try {
			
			req = gson.fromJson(jsonRequest, ProductMasterServiceBean.class);	

			
		} catch (Exception e) {
			res.setResultCode("20000");
			res.setResultDescription("JSON incorrect format");
			res.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		}
		
		User user = new User(req.getUserId(), null, null); 
		List<LovMaster> listValuationClass  = lovMasterService.listLovMasterByCriteria("TDSMT014_VALUATION_CLASS", req.getProductSubType(), null, null, null, "Y", null, null, null, null, null, null) ;
	
		try {

			boolean validInsert = false;
			validInsert = validateInputProductMaster(req, res);
			log.info("validUpdate ProductMaster:" + validInsert);
			Date effective = (BeanUtil.isEmpty(req.getEffectiveDate())) ? new Date() : TDMDataUtility.convertStringToDateHHmm(req.getEffectiveDate()); 
			Date expire = (BeanUtil.isEmpty(req.getExpireDate())) ? null : TDMDataUtility.convertStringToDateHHmm(req.getExpireDate()); 
			
			if(validInsert) {				

				//set Data								
				//ProductMst
				List<ProductMst> listProductMst = new ArrayList<ProductMst>();
				ProductMst productMst = new ProductMst();
				
				if(BeanUtil.isNotEmpty(req.getProductId())){
					productMst.setProductId(Long.valueOf(req.getProductId()));
				}				
				productMst.setCompany(req.getCompany());
				productMst.setProductType(req.getProductType());
				productMst.setProductSubType(req.getProductSubType());
				productMst.setBrand(null);
				productMst.setModel(null);
				productMst.setFeature(null);
				productMst.setNetwork(null);
				productMst.setRegion(null);
				productMst.setSubRegion(null);
				productMst.setCapacity(null);
				productMst.setColor(null);
				productMst.setFaceValue(null);
				productMst.setMatCode(req.getMatCode().trim().toUpperCase());
				productMst.setMatType("Non Serial");
				productMst.setUnitName("PC");
				productMst.setVatType(req.getValueVatType());
				productMst.setSapDescription(req.getDescription());
				productMst.setTdmDescription(req.getDescription());
				productMst.setEffectiveDate(new Date());
				productMst.setExpireDate(null);
				productMst.setActiveFlag(req.getActiveFlag());	 
				productMst.setAssignmentGroup("N/A");
				productMst.setMatGroup(null);
				productMst.setValuationClass(listValuationClass.get(0).getLovCode().toString());
				productMst.setRefMatCode(null);
				productMst.setAttribute01(null);
				productMst.setAttribute02(null);
				productMst.setAttribute03(null);
				productMst.setProdTDMFlag("Y");
				productMst.setGrade(null);				
				listProductMst.add(productMst);
				
				//PriceMst
				List<PriceMst> listPriceMstArr = new ArrayList<PriceMst>();
				PriceMst priceMst = new PriceMst();
				priceMst.setCompany(req.getCompany());
				priceMst.setPaymentMethod("CASH");
				priceMst.setPriceGroup(req.getProductPriceGroup());
				priceMst.setDescription(req.getDescription());
				priceMst.setVatType(req.getVatType());
				priceMst.setExcVat(new BigDecimal(req.getPriceExcVat()));
				priceMst.setVatRate(req.getVatRate());
				priceMst.setVatAmt(new BigDecimal(req.getPriceVat()));
				priceMst.setPriceDiff(null);
				priceMst.setPriceCredit(null);
				priceMst.setEffectiveDt(effective);
				priceMst.setExpireDt(expire);
				priceMst.setActiveFlg("N");
				listPriceMstArr.add(priceMst);	
				
				try{
					ProcessResult updateProduct = producMasterServiceService.insertProductMst(req.getMappingCodeMobileApp(), req.getPriceIncVat(), productMst, priceMst, user);
					if(updateProduct.isSuccess()){
						log.info("update success");
						res.setResultCode("20000");
						res.setDeveloperMessage(updateProduct.getMessage());
						res.setResultDescription(updateProduct.getMessage());
	
					}else{
						log.info("update fail");
						res.setResultCode("50000");
						res.setDeveloperMessage(updateProduct.getMessage());
						res.setResultDescription(updateProduct.getMessage());
					}	
					
				}catch (ForceTerminateException e) {
					return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);


				}
				
			} else {				
				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

				
			}

			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

					
		} catch(Exception e) {
			log.error("insertProductMasterService", e);
			res.setResultCode("50000");
			res.setResultDescription("webImpl fail");
			res.setDeveloperMessage("webImpl fail");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
    private boolean validateInputProductMaster(ProductMasterServiceBean req,ProductMasterServiceBean res){
		
		if(BeanUtil.isEmpty(req.getCompany())) {
			res.setResultCode("50000");
			res.setResultDescription("Company is null");
			res.setDeveloperMessage("Company is null");
			return false;			
		}
		if(BeanUtil.isEmpty(req.getProductType())) {
			res.setResultCode("50000");
			res.setResultDescription("ProductType is null");
			res.setDeveloperMessage("ProductType is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getProductSubType())) {
			res.setResultCode("50000");
			res.setResultDescription("ProductSubType is null");
			res.setDeveloperMessage("ProductSubType is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getMatCode())) {
			res.setResultCode("50000");
			res.setResultDescription("MatCode is null");
			res.setDeveloperMessage("MatCode is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getDescription())) {
			res.setResultCode("50000");
			res.setResultDescription("Description is null");
			res.setDeveloperMessage("Description is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getProductPriceGroup())) {
			res.setResultCode("50000");
			res.setResultDescription("ProductPriceGroup is null");
			res.setDeveloperMessage("ProductPriceGroup is null");
			return false;
		}
		if(BeanUtil.isEmpty(req.getActiveFlag())) {
			res.setResultCode("50000");
			res.setResultDescription("ActiveFlag is null");
			res.setDeveloperMessage("ActiveFlag is null");
			return false;
		}
			
		if(req.getMatCode().length() > 50) {
			res.setResultCode("50000");
			res.setResultDescription("Max length of MatCode is 50 characters.");
			res.setDeveloperMessage("Max length of MatCode is 50 characters.");
			return false;
		}
		if(BeanUtil.isNotEmpty(req.getDescription()) && req.getDescription().length() > 300) {
			res.setResultCode("50000");
			res.setResultDescription("Max length of Description is 300 characters.");
			res.setDeveloperMessage("Max length of Description is 300 characters.");
			return false;
		}
		return true;
	}
    
	@RequestMapping(value = prefixPath +"/upload-product-master-service",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(ProductMasterServiceBean.class)
	public ResponseEntity<String> uploadProductMasterService(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		ProductMasterServiceBean req = new ProductMasterServiceBean();
		ProductMasterServiceBean res = new ProductMasterServiceBean();		
		Gson gson = new Gson();
		
		try {
			req = gson.fromJson(jsonRequest, ProductMasterServiceBean.class);	
		} catch (Exception e) {
			res.setResultCode("20000");
			res.setResultDescription("JSON incorrect format");
			res.setDeveloperMessage("JSON incorrect format");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

		}
		
		User user = new User(req.getUserId(), null, null);
		List<LovMaster> listValuationClass  = lovMasterService.listLovMasterByCriteria("TDSMT014_VALUATION_CLASS", req.getProductSubType(), null, null, null, "Y", null, null, null, null, null, null) ;

		try {
			String valuationClass = listValuationClass.get(0).getLovCode().toString();
			ProductMst productMst = null;
			PriceMst priceMst = null;
			List<ProductMst> listProductMst = new ArrayList<ProductMst>();
			String msgErr = null;
			for (ProductMasterServiceBean item : req.getProductMasterList()) {
				item = prepareDataUpload(req, item);
				msgErr = validateInputProductMaster(item);
				if(BeanUtil.isEmpty(msgErr)) {
					productMst = new ProductMst();
					productMst.setCompany(item.getCompany());
					productMst.setProductType(item.getProductType());
					productMst.setProductSubType(item.getProductSubType());
					productMst.setBrand(null);
					productMst.setModel(null);
					productMst.setFeature(null);
					productMst.setNetwork(null);
					productMst.setRegion(null);
					productMst.setSubRegion(null);
					productMst.setCapacity(null);
					productMst.setColor(null);
					productMst.setFaceValue(null);
					productMst.setMatCode(item.getMatCode().trim().toUpperCase());
					productMst.setMatType("Non Serial");
					productMst.setUnitName("PC");
					productMst.setVatType(item.getVatType());
					productMst.setSapDescription(item.getDescription());
					productMst.setTdmDescription(item.getDescription());
					productMst.setEffectiveDate(new Date());
					productMst.setExpireDate(null);
					productMst.setActiveFlag(item.getActiveFlag());	 
					productMst.setAssignmentGroup("N/A");
					productMst.setMatGroup(null);
					productMst.setValuationClass(valuationClass);
					productMst.setRefMatCode(null);
					productMst.setAttribute01(null);
					productMst.setAttribute02(null);
					productMst.setAttribute03(null);
					productMst.setProdTDMFlag("Y");
					productMst.setGrade(null);				
					listProductMst.add(productMst);
					
					if(BeanUtil.isNotEmpty(item.getPriceIncVat())) {
						//PriceMst
						Date effective = (BeanUtil.isEmpty(item.getEffectiveDate())) ? new Date() : TDMDataUtility.convertStringToDateHHmm(item.getEffectiveDate()); 
						Date expire = (BeanUtil.isEmpty(item.getExpireDate())) ? null : TDMDataUtility.convertStringToDateHHmm(item.getExpireDate());
						List<PriceMst> listPriceMstArr = new ArrayList<PriceMst>();
						priceMst = new PriceMst();
						priceMst.setCompany(item.getCompany());
						priceMst.setPaymentMethod("CASH");
						priceMst.setPriceGroup(item.getProductPriceGroup());
						priceMst.setDescription(item.getDescription());
						priceMst.setVatType(item.getVatType());
						priceMst.setExcVat(new BigDecimal(item.getPriceExcVat()));
						priceMst.setVatRate(item.getVatRate());
						priceMst.setVatAmt(new BigDecimal(item.getPriceVat()));
						priceMst.setPriceDiff(null);
						priceMst.setPriceCredit(null);
						priceMst.setEffectiveDt(effective);
						priceMst.setExpireDt(expire);
						priceMst.setActiveFlg("N");
						listPriceMstArr.add(priceMst);
					}
					try{
						ProcessResult updateProduct = producMasterServiceService.insertProductMst(item.getMappingCodeMobileApp(), item.getPriceIncVat(), productMst, priceMst, user);
						if(updateProduct.isSuccess()){
							log.info("update success");
							item.setProductId(String.valueOf((Long)updateProduct.getResultObject()));
							item.setResultCode("20000");
							item.setResultDescription(updateProduct.getMessage());
		
						}else{
							log.info("update fail");
							item.setResultCode("50000");
							item.setResultDescription(updateProduct.getMessage());
						}	
						
					}catch (ForceTerminateException e) {
						item.setResultCode("50000");
						item.setResultDescription(e.getMessage());
					}
				}else {
					item.setResultCode("50000");
					item.setResultDescription(msgErr);
				}
			}
			res.setResultCode("20000");
			res.setProductMasterList(req.getProductMasterList());
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

					
		} catch(Exception e) {
			log.error("uploadProductMasterService", e);
			res.setResultCode("50000");
			res.setResultDescription("Upload fail");
			res.setDeveloperMessage("Upload fail");
			return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	private ProductMasterServiceBean prepareDataUpload(ProductMasterServiceBean req, ProductMasterServiceBean item) {
		item.setCompany(req.getCompany());
		item.setProductType(req.getProductType());
		item.setProductSubType(req.getProductSubType());
		item.setProductPriceGroup(req.getProductPriceGroup());
		item.setVatType(req.getVatType());
		item.setVatRate(req.getVatRate());
		item.setActiveFlag(req.getActiveFlag());
		return item;
	}
	
	private String validateInputProductMaster(ProductMasterServiceBean item){
		if(BeanUtil.isEmpty(item.getCompany())) {
			return "Company is null";			
		}
		if(BeanUtil.isEmpty(item.getProductType())) {
			return "ProductType is null";
		}
		if(BeanUtil.isEmpty(item.getProductSubType())) {
			return "ProductSubType is null";
		}
		if(BeanUtil.isEmpty(item.getMatCode())) {
			return "MatCode is null";
		}
		if(BeanUtil.isEmpty(item.getDescription())) {
			return "Description is null";
		}
		if(BeanUtil.isEmpty(item.getProductPriceGroup())) {
			return "ProductPriceGroup is null";
		}
		if(BeanUtil.isEmpty(item.getActiveFlag())) {
			return "ActiveFlag is null";
		}
		if(item.getMatCode().length() > 50) {
			return "Max length of MatCode is 50 characters.";
		}
		if(item.getDescription().length() > 300) {
			return "Max length of Description is 300 characters.";
		}
		return null;
	}

}
