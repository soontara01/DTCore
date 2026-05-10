package th.co.ais.dt.core.service.core.impl.sap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.ProductMstSapBean;
import th.co.ais.dt.controller.dto.SapBasicText;
import th.co.ais.dt.controller.dto.SapCharacteristic;
import th.co.ais.dt.controller.dto.SapItemProduct;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleProductMasterService;
import th.co.ais.dt.entity.sap.DtSapMatGroupMapProduct;
import th.co.ais.dt.entity.sap.DtSapMchProductPriceLog;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMatGroupMapProductDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMchProductPriceLogDao;
import th.co.ais.dt.repository.interfaces.sk.IStockMstDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.IProductMstDao;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.core.impl.sap.dto.MappingMatGroupProductReqResBean;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.service.mail.impl.EmailSenderService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.controller.dto.SapSales;
import th.co.ais.dt.controller.dto.SapUOM;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleProductMasterServiceImpl implements ISapHandleProductMasterService {
	
    private final ILovMasterDao lovMasterDao;
    private final IDtSapMchProductPriceLogDao dtSapMchProductPriceLogDao;
    private final DTConfig dTConfig;
    private final EmailSenderService emailSenderService;
    private final IEmailService emailService;
    private final IProductMstDao productMstDao;
    private final IDtSapMatGroupMapProductDao sapMatGroupMapProductDao;
    private final LovMasterService lovMasterService;
    private final IStockMstDao stockMstDao;
    
    public static final String SAP_MCH_MAT_GROUP_MAP_PRODUCT = "SAP_MCH_MAT_GROUP_MAP_PRODUCT";
    public static final String HIERARCHIE_00 = "00";
    public static final String MAPPING_PRODUCT_MASTER = "MAPPING_PRODUCT_MASTER";
    public static final String DT_APP = "DTAPP";


    @Override
    public CommonResponseBean mappingProductMaster(ProductMstSapBean request) {
//    	String urlInsertProductMaster = dTConfig.getUrl().getDtws() + "/DTWS/api/productMasterSevice/v1/insertSapProductMst";
//    	String urlInsertLovProduct = dTConfig.getUrl().getDtws() + "/DTWS/api/master-config/v1/create-lov-product";
//    	String urlInsertStock = dTConfig.getUrl().getDtws() + "/DTWS/api/master-config/v1/create-stock-product";
    	CommonResponseBean response = new CommonResponseBean();
    	
        try {
            boolean checkInsert = true;
            String massage = null;
            Map<String, List<Long>> listData = new HashMap<String, List<Long>>();
            Long logId = null;
            
            // Call service insert ProductMaster
//            MappingMatGroupProductReqResBean responseInsertProductMst = insertOnPrem(request,urlInsertProductMaster,null);
            ProductMstSapBean responseInsertProductMst = insertProductMstFromSap(request);
            if(responseInsertProductMst == null || !"20000".equals(responseInsertProductMst.getResultCode())) {
            	checkInsert = false;
            	massage = responseInsertProductMst.getResultDescription();
            } else {
                // Insert log
            	responseInsertProductMst.getData();
            	listData = (Map<String, List<Long>>) responseInsertProductMst.getData();
            	massage = responseInsertProductMst.getData().toString();
            	if(listData.get("lov").size()>0) {
            		logId = insertMchMappingProductLog(request,massage);
            	}
            }
            if(checkInsert && listData.get("lov").size()>0) {
            	// Call service insert LovProduct
//            	MappingMatGroupProductReqResBean responseInsertLovProduct = insertOnPrem(request,urlInsertLovProduct,listData.get("lov"));
            	CommonResponseBean responseInsertLovProduct = insertLovProduct(listData.get("lov"));
            	if(responseInsertLovProduct == null || !"20000".equals(responseInsertLovProduct.getResultCode())) {
            		checkInsert = false;
            		massage = "Error InsertLovProduct";
            	}
            }
            if(checkInsert && listData.get("stock").size()>0) {
            	// Call service insert Stock
//            	MappingMatGroupProductReqResBean responseInsertStock = insertOnPrem(request,urlInsertStock,listData.get("stock"));
            	CommonResponseBean responseInsertStock = insertStockProduct(listData.get("stock"));
            	if(responseInsertStock == null || !"20000".equals(responseInsertStock.getResultCode())) {
            		checkInsert = false;
            		massage = "Error InsertStock";
            	}
            }
           

            // Update log
            if (checkInsert) {
            	if(BeanUtil.isNotEmpty(logId)) {
            		 if("S".equals(responseInsertProductMst.getResultStatus())) {
                    	updateMchMappingProductLog(logId,"S",massage);
                    }
                    else {
                    	massage = responseInsertProductMst.getResultDescription();
                    	updateMchMappingProductLog(logId,"F",massage);
                    }
            	}
                response.setResultCode("20000");
                response.setResultStatus("S");
                response.setResultDescription("Success");
                
            }
            else {
//                updateMchMappingProductLog(logId,"F",massage);
                response.setResultCode("50000");
                response.setResultStatus("F");
                response.setResultDescription(massage);
            }
            if("Y".equals(responseInsertProductMst.getSendMail())) {
            	this.sendEmail(massage);
            }

        } catch (Exception e) {
            log.error("mappingProductMaster error ", e);
//            this.sendEmail(null);
        }
        return response;
    }

    private static MasterValue getCreateValue(Date date) {
        MasterValue masterValue = new MasterValue();
        masterValue.setCreated(date);
        masterValue.setCreatedBy(DT_APP);
        masterValue.setLastUpd(date);
        masterValue.setLastUpdBy(DT_APP);
        return masterValue;
    }

    private Long insertMchMappingProductLog(ProductMstSapBean request,String massage) {
    	Gson gson = new Gson();
        DtSapMchProductPriceLog mchProductPriceLog = new DtSapMchProductPriceLog();
        mchProductPriceLog.setServiceName(MAPPING_PRODUCT_MASTER);
        mchProductPriceLog.setParaInput(gson.toJson(request));
        mchProductPriceLog.setStatus("W");
        mchProductPriceLog.setCreateValue(getCreateValue(new Date()));
        mchProductPriceLog.setDescription(massage);

        dtSapMchProductPriceLogDao.insert(mchProductPriceLog);

        return mchProductPriceLog.getTableId();
    }

    private void updateMchMappingProductLog(Long logId,String status,String errorMsg) {
        DtSapMchProductPriceLog mchProductPriceLog = dtSapMchProductPriceLogDao.getByLongPrimaryKey(logId);
        mchProductPriceLog.setStatus(status);
        mchProductPriceLog.setDescription(errorMsg);
        mchProductPriceLog.getCreateValue().setLastUpd(new Date());

        dtSapMchProductPriceLogDao.update(mchProductPriceLog);
    }

    private MappingMatGroupProductReqResBean insertOnPrem(ProductMstSapBean request,String url,List<Long> listData) {
        Gson gson = new Gson();
    	HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        String jsonReq = gson.toJson(request);

//        MappingMatGroupProductReqResBean req = MappingMatGroupProductReqResBean.builder()
//                .ITEMS(sapMchList)
//                .build();
        
        if(BeanUtil.isNotEmpty(listData) && listData.size()>0) {
        	jsonReq = gson.toJson(listData);
        }
        
		String out = c.HttpClient(properties,url,jsonReq,"POST", null) ;
		log.info("call Jboss ====> "+ url + " | res : "+ out);
        return gson.fromJson(out, MappingMatGroupProductReqResBean.class);
    }
    
    private void sendEmail(String masage){
        try {
            EmailForm emailForm = new EmailForm();
            emailForm.setFrom("digitaltrading.app@ais.co.th");
            emailForm.setToLists(new ArrayList<>());

            List<Object[]> email = emailService.queryEmail(MAPPING_PRODUCT_MASTER);
            
            Object[] e = email.get(0);
            String subject = (String) e[3];
            String process_desc = (String) e[2];
            String to = (String) e[4];

            String[] toArr = to.split(";");
            for (String el : toArr) {
                emailForm.getToLists().add(el);
            }
            
            if(BeanUtil.isNotEmpty(process_desc)) {
                emailForm.setBody(process_desc);
            } else {
            	emailForm.setBody(masage);
            }
            
            emailForm.setSubject(subject);

            emailSenderService.sendEmail(emailForm);
            log.info("send email mapping product master success");
        } catch (Exception e) {
            log.error("sending email mapping product master error ", e);
        }
    }
    
	@Override
	public ProductMstSapBean insertProductMstFromSap(ProductMstSapBean in) throws DataAccessException {
		ProductMstSapBean res = new ProductMstSapBean();
		res.setSendMail("F");
		try {
			List<Long> listInsertLov = new ArrayList<Long>();
			List<Long> listInsertStock = new ArrayList<Long>();
			List<ProductMst> listProductMsts = new ArrayList<ProductMst>();
			List<ProductMst> listProductMstsIns = new ArrayList<ProductMst>();
			List<String> listMatGrpError = new ArrayList<String>();

			// set productMst
			for(SapItemProduct item : in.getITEMS()) {		
				//Check MatGrp เฉพาะที่มีใน Table MCH Config && DocType : FG Only
				List<DtSapMatGroupMapProduct> listMCH = sapMatGroupMapProductDao.listSapMatGroupMapProductByCriteria(item.getBasicData().getMatGrp(), null, null);
				if(BeanUtil.isNotEmpty(listMCH) && listMCH.size()>0 && ("FG".equals(item.getBasicData().getDocType()) || BeanUtil.isEmpty(item.getBasicData().getDocType()))) {
					if(BeanUtil.isEmpty(listMCH.get(0).getProductType()) || BeanUtil.isEmpty(listMCH.get(0).getProductSubtype())) {
						listMatGrpError.add(item.getBasicData().getMatGrp());
						continue;
					}
					List<String> listLocationIns = new ArrayList<String>();
					for(SapSales sale:item.getSales()) {
						if(BeanUtil.isEmpty(sale.getSaleOrg())) {
							res.setResultCode("50000");
							res.setResultStatus("F");
							res.setDeveloperMessage("matNo : "+item.getBasicData().getMatNo()+ " Error SaleOrg is Empty");
							res.setResultDescription("matNo : "+item.getBasicData().getMatNo()+ " Error SaleOrg is Empty");
							res.setSendMail("Y");
							return res;
						}
						ProductMst productMst = new ProductMst();
						productMst.setMatCode(BeanUtil.convertMatSapToTDM(item.getBasicData().getMatNo()));
						List<LovMaster> listConfigCompany = lovMasterDao.listLovMasterByCriteria("SAP_S4_SALEORG_MAP_COMPANY",null,sale.getSaleOrg(),null,"Y");
						if(BeanUtil.isNotEmpty(listConfigCompany) && listConfigCompany.size()>0) {
							for(LovMaster lov:listConfigCompany) {
								productMst.setCompany(lov.getLovSubType());
								productMst.setDistCh(sale.getDistCh());
							}
							productMst = this.prepareDataProductMst(item, productMst);
							listProductMsts.add(productMst);

							if(!listLocationIns.contains(sale.getSaleOrg())) {
								listProductMstsIns.add(productMst);
								listLocationIns.add(sale.getSaleOrg());
							}
						}
					}
					for(ProductMst pMs:listProductMstsIns) {
						pMs = this.insertOrUpdateProductMst(pMs);
						listInsertLov.add(pMs.getProductId());
						for(ProductMst pMs2:listProductMsts) {
							if(pMs.getCompany().equals(pMs2.getCompany()) && "14".equals(pMs2.getDistCh())) {
								listInsertStock.add(pMs.getProductId());
							}
						}
					}
				}
			}
			
			Map<String,List<Long>> data = new HashMap<String, List<Long>>();
			listInsertLov = listInsertLov.stream().distinct().collect(Collectors.toList());
			listInsertStock= listInsertStock.stream().distinct().collect(Collectors.toList());
			data.put("lov", listInsertLov);
			data.put("stock", listInsertStock);
			
			if(BeanUtil.isNotEmpty(listInsertLov)||listInsertLov.size()>0) {
				if(BeanUtil.isNotEmpty(listMatGrpError)||listMatGrpError.size()>0) {
					res.setResultCode("20000");
					res.setResultStatus("F");
					res.setDeveloperMessage("matGrp : "+listMatGrpError+" Error ProductType or ProductSubtype is empty");
					res.setResultDescription("matGrp : "+listMatGrpError+" Error ProductType or ProductSubtype is empty");
					res.setSendMail("Y");
					res.setData(data);	
				} else {
					res.setResultCode("20000");
					res.setResultStatus("S");
					res.setDeveloperMessage("Success");
					res.setResultDescription("Success");
					res.setData(data);
				}
			} else {
				res.setResultCode("50000");
				res.setResultStatus("F");
				if(BeanUtil.isNotEmpty(listMatGrpError)||listMatGrpError.size()>0) {
					res.setDeveloperMessage("matGrp : "+listMatGrpError+" Error ProductType or ProductSubtype is empty");
					res.setResultDescription("matGrp : "+listMatGrpError+" Error ProductType or ProductSubtype is empty");
					res.setSendMail("Y");
				} else {
					res.setDeveloperMessage("Error insertProductMaster");
					res.setResultDescription("Error insertProductMaster");
				}
				res.setData(data);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			res.setResultCode("50000");
			res.setResultStatus("F");
			res.setDeveloperMessage(e.getMessage());
			res.setResultDescription(e.getMessage());
			res.setSendMail("Y");
			//mail กรณี error
		}	
		return res;
	}
	
	private ProductMst insertOrUpdateProductMst(ProductMst productMst) {
		ProductMst productPrepare = null;
		try {
			List<ProductMst> oldPMs = productMstDao.getProductMstByUnique(productMst.getCompany(),productMst.getMatCode());
			if(BeanUtil.isNotEmpty(oldPMs)) {
				//update
				ProductMst oldPMstmp = oldPMs.get(0);
				oldPMstmp = this.prepareUpdateProductMst(oldPMstmp,productMst);
				productMstDao.update(oldPMstmp);
				productPrepare = oldPMstmp;
			}
			else {
				//insert
				productMstDao.insert(productMst);
				productPrepare = productMst;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return productPrepare;
	}
	
	private ProductMst prepareDataProductMst (SapItemProduct item,ProductMst productMst) {
		try {
			Date sysDate = new Date();
			productMst.setBrand(item.getBasicData().getBrandDesc());
			productMst.setModel(item.getBasicData().getIndStdDesc());
			
			ProductMst productMstOld = null;
			if(BeanUtil.isNotEmpty(item.getBasicData().getOldMatNo())) {
				List<ProductMst> listProductMstOld = productMstDao.getProductMstByUnique(productMst.getCompany(), item.getBasicData().getOldMatNo());
				productMstOld = listProductMstOld.get(0);
			}
			
			if(BeanUtil.isNotEmpty(productMstOld)) {
				//get from productMstOld
				productMst.setProductType(productMstOld.getProductType());
				productMst.setProductSubType(productMstOld.getProductSubType());
			}
			else {
				//get from table dt_sap_mat_group_map_product
				if(BeanUtil.isNotEmpty(item.getBasicData().getMatGrp())) {
					List<DtSapMatGroupMapProduct> listMCH = sapMatGroupMapProductDao.listSapMatGroupMapProductByCriteria(item.getBasicData().getMatGrp(), null, null);
					productMst.setProductType(listMCH.get(0).getProductType());
					productMst.setProductSubType(listMCH.get(0).getProductSubtype());
				}
			}
		
			if("00".equals(item.getBasicData().getMatCat())) {
				productMst.setColor(item.getBasicData().getBasicMat());
			}
			if("Z001".equals(item.getLogistic().get(0).getSerialNoProfile()) 
			|| "Z002".equals(item.getLogistic().get(0).getSerialNoProfile())) {
				productMst.setMatType("Serial");
			}
			else {
				productMst.setMatType("Non Serial");

			}
			
			for(SapUOM uom:item.getUOM()) {
				if(BeanUtil.isNotEmpty(uom.getIndUOMBaseUnit()) && "X".equals(uom.getIndUOMBaseUnit().toUpperCase())) {
					productMst.setUnitName(uom.getAltUOM());
					break;
				}
			}
			productMst.setVatType(item.getBasicData().getTaxClass().equals("0") ? "N":"Y");
			productMst.setSapDescription(item.getBasicData().getMatDescTH());
			productMst.setTdmDescription(item.getBasicData().getMatDescEN());
			productMst.setEffectiveDate(sysDate); //cast insert
			productMst.setActiveFlag(item.getBasicData().getDFClntLevel().equals(null) || item.getBasicData().getDFClntLevel().equals("") ? "Y":"N");
			productMst.setAssignmentGroup(item.getSales().get(0).getAccountAssignGrp());
			productMst.setMatGroup(item.getBasicData().getMatGrp());
			productMst.setValuationClass(item.getBasicData().getMatGrp());
			
			if(BeanUtil.isNotEmpty(item.getBasicData().getMatType())) {
				List<LovMaster> listConfigGrade = lovMasterDao.listLovMasterByCriteria("SAP_S4_MATTYPE_GRADE",null,item.getBasicData().getMatType(),null,"Y");
				if(BeanUtil.isNotEmpty(listConfigGrade) && listConfigGrade.size()>0) {
					productMst.setGrade(listConfigGrade.get(0).getLovVal());

				}	
			}
			
			productMst.setCategory(item.getBasicData().getGenItemCatGrp());
			productMst.setDivision(item.getBasicData().getDivision());
			productMst.setProduct_hierarchy(item.getBasicData().getProdHierarchy());
			productMst.setOldMatNo(item.getBasicData().getOldMatNo());
			if(BeanUtil.isNotEmpty(item.getBasicText())) {
				productMst.setCommercialName("");
				for(SapBasicText sapBasicText:item.getBasicText()) {
					if("EN".equals(sapBasicText.getLanguage())) {
						productMst.setCommercialName(sapBasicText.getBasicDataTxt());
						break;
					} else if("TH".equals(sapBasicText.getLanguage())) {
						productMst.setCommercialName(sapBasicText.getBasicDataTxt());
					}
				}
				// check lov matGroup setSapDescription
				List<LovMaster> listConfigMatGroup = lovMasterDao.listLovMasterByCriteria("SAP_S4_PRODUCT_DESC",null,item.getBasicData().getMatGrp(),null,"Y");
				if(BeanUtil.isNotEmpty(listConfigMatGroup) && BeanUtil.isNotEmpty(productMst.getCommercialName())) {
					productMst.setSapDescription(productMst.getCommercialName());
				}
			} else {
				productMst.setCommercialName("");
			}
			productMst.setEANUPC(item.getUOM().get(0).getEANUPC());
			productMst.setBrandCode(item.getBasicData().getBrand());
			
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(sysDate);
			masterValue.setCreatedBy("DTAPP");
			masterValue.setLastUpd(sysDate);
			masterValue.setLastUpdBy("DTAPP");
			productMst.setCreateValue(masterValue);
			
			if(BeanUtil.isNotEmpty(item.getCharacteristic())) {
				this.getCharacteristicData(item, productMst);
			}	
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error prepareDataProductMst");
			e.printStackTrace();
		}
		
		
		return productMst;
	}
	
	private ProductMst prepareUpdateProductMst (ProductMst oldProductMst,ProductMst productMst) {
		try {
			
			List<LovMaster> configMatNotUpdate = null ;
			if(oldProductMst !=  null && BeanUtil.isNotEmpty(oldProductMst.getCompany()) && BeanUtil.isNotEmpty(oldProductMst.getMatCode())) {
				configMatNotUpdate = lovMasterDao.listLovMasterByCriteria("SAP_S4_MAT_CANNOT_UPDATE","SAP_S4_MAT_CANNOT_UPDATE",oldProductMst.getCompany(),oldProductMst.getMatCode(),"Y");

			}

			
			Date sysDate = new Date();
			oldProductMst.setCompany(productMst.getCompany());
			oldProductMst.setProductType(productMst.getProductType());
			oldProductMst.setProductSubType(productMst.getProductSubType());
			
			if(BeanUtil.isNotEmpty(configMatNotUpdate)) {
				//oldProductMst.setBrand(productMst.getBrand());
				//oldProductMst.setModel(productMst.getModel());
			}else {
				oldProductMst.setBrand(productMst.getBrand());
				oldProductMst.setModel(productMst.getModel());
			}
			
			//oldProductMst.setBrand(productMst.getBrand());
			//oldProductMst.setModel(productMst.getModel());
			oldProductMst.setColor(productMst.getColor());
			oldProductMst.setCapacity(productMst.getCapacity());
			oldProductMst.setMatCode(productMst.getMatCode());
			oldProductMst.setMatType(productMst.getMatType());
			oldProductMst.setUnitName(productMst.getUnitName());
			oldProductMst.setVatType(productMst.getVatType());
			oldProductMst.setSapDescription(productMst.getSapDescription());
			oldProductMst.setTdmDescription(productMst.getTdmDescription());
	
			oldProductMst.setActiveFlag(productMst.getActiveFlag());
			oldProductMst.setAssignmentGroup(productMst.getAssignmentGroup());
			oldProductMst.setMatGroup(productMst.getMatGroup());
			oldProductMst.setValuationClass(productMst.getValuationClass());
			oldProductMst.setGrade(productMst.getGrade());
			oldProductMst.setCategory(productMst.getCategory());
			oldProductMst.setDivision(productMst.getDivision());
			oldProductMst.setProduct_hierarchy(productMst.getProduct_hierarchy());
			oldProductMst.setOldMatNo(productMst.getOldMatNo());
			oldProductMst.setCommercialName(productMst.getCommercialName());
			oldProductMst.setEANUPC(productMst.getEANUPC());
			oldProductMst.setMainModel(productMst.getMainModel());
			oldProductMst.setFreeGoods(productMst.getFreeGoods());
			oldProductMst.setBrandCode(productMst.getBrandCode());
			
			oldProductMst.getCreateValue().setLastUpd(sysDate);
			oldProductMst.getCreateValue().setLastUpdBy("DTAPP");
		
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error prepareUpdateProductMst");
			e.printStackTrace();
		}
		return oldProductMst;
	}
	
	private ProductMst getCharacteristicData(SapItemProduct sapItem,ProductMst productMst) {
		for(SapCharacteristic item:sapItem.getCharacteristic()) {

			String valueZMAINMODEL = getCharacteristicValue("ZMAINMODEL", item);
			if(BeanUtil.isNotEmpty(valueZMAINMODEL)) {
				productMst.setMainModel(valueZMAINMODEL);
			}
			
			String valueZDEVCAPACITY = getCharacteristicValue("ZDEV_CAPACITY", item);
			if(BeanUtil.isNotEmpty(valueZDEVCAPACITY)) {
				productMst.setCapacity(valueZDEVCAPACITY);
			}
			
			String valueZFREEGOODS = getCharacteristicValue("ZFREEGOODS", item);
			if(BeanUtil.isNotEmpty(valueZFREEGOODS)) {
				productMst.setFreeGoods(valueZFREEGOODS);
			}
			
		}
		//set color matCat 02
		if("02".equals(sapItem.getBasicData().getMatCat())) {
			for(SapCharacteristic item:sapItem.getCharacteristic()) {
				String valueZDEVCOLOR = getCharacteristicValue("ZDEV_COLOR", item);
				if(BeanUtil.isNotEmpty(valueZDEVCOLOR)) {
					productMst.setColor(valueZDEVCOLOR);
				}
			}
		}
		
		return productMst;
	}
	
	private String getCharacteristicValue(String key,SapCharacteristic item) {
		String value = null;
		try {
			if(key.equals(item.getChar1())) {
				value = item.getChar1Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar2())) {
				value = item.getChar2Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar3())) {
				value = item.getChar3Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar4())) {
				value = item.getChar4Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar5())) {
				value = item.getChar5Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar6())) {
				value = item.getChar6Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar7())) {
				value = item.getChar7Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar8())) {
				value = item.getChar8Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar9())) {
				value = item.getChar9Val().get(0).get("Value");
			}
			
			if(key.equals(item.getChar10())) {
				value = item.getChar10Val().get(0).get("Value");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return value;
	}
	
	private CommonResponseBean insertLovProduct(List<Long> in) throws SQLException {
		CommonResponseBean response = null;
		try {			
			lovMasterService.createLovProduct(in);
			response = new CommonResponseBean("20000", "S", "Success");
			return response;
		}catch(Exception e) {
			log.error("createLovProduct", e);
			response = new CommonResponseBean("50000", "F", "System error");
			return response;
		}
		
	}
	
	private CommonResponseBean insertStockProduct(List<Long> in) {
		CommonResponseBean response = null;
		try {
			stockMstDao.createStockProduct(in);
			response = new CommonResponseBean("20000", "S", "Success");
			return response;
		}catch(Exception e) {
			log.error("createLovProduct", e);
			response = new CommonResponseBean("50000", "F", "System error");
			return response;
		}
	}
    
}
