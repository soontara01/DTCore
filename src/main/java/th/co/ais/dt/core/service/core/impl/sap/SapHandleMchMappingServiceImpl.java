package th.co.ais.dt.core.service.core.impl.sap;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.DtSapMchMapping;
import th.co.ais.dt.controller.dto.MchItem;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleMchMappingService;
import th.co.ais.dt.entity.sap.DtSapMatGroupMapProduct;
import th.co.ais.dt.entity.sap.DtSapMatGroupMapType;
import th.co.ais.dt.entity.sap.DtSapMchProductPriceLog;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMatGroupMapProductDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMatGroupMapTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapMchProductPriceLogDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.dto.EmailForm;
import th.co.ais.dt.service.mail.impl.EmailSenderService;
import th.co.ais.dt.util.BeanUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SapHandleMchMappingServiceImpl implements ISapHandleMchMappingService {
    public static final String SAP_MCH_MAT_GROUP_MAP_PRODUCT = "SAP_MCH_MAT_GROUP_MAP_PRODUCT";
    public static final String HIERARCHIE_00 = "00";
    public static final String MCH_MAPPING_PRODUCT = "MCH_MAPPING_PRODUCT";
    public static final String DT_APP = "DTAPP";
    public static final String KEY_PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final String KEY_PRODUCT_SUB_TYPE = "PRODUCT_SUB_TYPE";

    private final ILovMasterDao lovMasterDao;
    private final IEmailService emailService;
    private final EmailSenderService emailSenderService;
    private final IDtSapMatGroupMapTypeDao dtSapMatGroupMapTypeDao;
    private final IDtSapMatGroupMapProductDao dtSapMatGroupMapProductDao;
    private final IDtSapMchProductPriceLogDao dtSapMchProductPriceLogDao;

    @Override
    public CommonResponseBean mappingMatGroupProduct(DtSapMchMapping request) {
        Long logId = 0L;

        try {
            // Query config lov
            LovMaster config = getMatGroupByConfig();

            List<String> matGroupConfig = Arrays.asList(config.getLovVal().split("\\|"));
            log.info("matGroupConfig: {}", matGroupConfig);

            // Filter item with level of hierarchy = 00 and material group code start with config
            List<MchItem> sapMchList = request.getITEMS().stream()
                    .filter(f -> HIERARCHIE_00.equals(f.getLevelofHierarchy()) && matGroupConfig.stream().anyMatch(f.getMaterialGroupCode()::startsWith))
                    .toList();
            log.info("mch item list: {}", sapMchList);

            if (BeanUtil.isEmpty(sapMchList)) {
                log.warn("Mch item list not matches");
                return new CommonResponseBean("20000", "Success", "Mch item list not matches");
            }

            // Insert log
            logId = insertMchMappingProductLog(request);

            Date date = new Date();
            boolean alreadyExists;
            String materialGroupLv2;
            String materialGroupLv3;
            StringBuilder resultMessage = new StringBuilder();
            List<DtSapMatGroupMapType> matGroupMapTypeList;

            List<DtSapMatGroupMapProduct> sapMatGroupMapProductList = new ArrayList<>();
            for (MchItem mchItem : sapMchList) {

                materialGroupLv3 = mchItem.getMaterialGroupCode();
                materialGroupLv2 = mchItem.getMaterialGroupCode().substring(0, 5);
                log.info("materialGroupCode : {}", materialGroupLv3);

                if (BeanUtil.isNotNull(dtSapMatGroupMapProductDao.getSapMatGroupMapProductById(materialGroupLv3))) {
                    resultMessage.append(materialGroupLv3).append(" : Already exists,");
                    continue;
                }

                // Check duplicates in list
                alreadyExists = sapMatGroupMapProductList.stream()
                        .anyMatch(i -> i.getMaterialGroup().equals(mchItem.getMaterialGroupCode()));

                if (!alreadyExists) {
                    resultMessage.append(materialGroupLv3);

                    DtSapMatGroupMapProduct matGroupMapProduct = new DtSapMatGroupMapProduct();
                    matGroupMapProduct.setMaterialGroup(materialGroupLv3);
                    matGroupMapProduct.setProductType(null);
                    matGroupMapProduct.setProductSubtype(null);
                    matGroupMapProduct.setMaterialGroupdesc(null);
                    matGroupMapProduct.setMaterialGroupdesc2(null);
                    matGroupMapProduct.setCreateValue(getCreateValue(date));

                    if (BeanUtil.isNotEmpty(mchItem.getMCHDescriptions())) {
                        matGroupMapProduct.setMaterialGroupdesc(mchItem.getMCHDescriptions().getMCMCHDescription());
                        matGroupMapProduct.setMaterialGroupdesc2(mchItem.getMCHDescriptions().getMCDescription2());
                    }

                    // Query mapping type
                    matGroupMapTypeList = dtSapMatGroupMapTypeDao.listSapMatGroupMapTypeForMappingType(materialGroupLv2, materialGroupLv3);

                    if (BeanUtil.isNotEmpty(matGroupMapTypeList)) {
                        // Mapping product type subtype
                        Map<String, String> productTypeMap = getProductTypeSubTypeByMapType(matGroupMapTypeList);
                        matGroupMapProduct.setProductType(productTypeMap.getOrDefault(KEY_PRODUCT_TYPE, null));
                        matGroupMapProduct.setProductSubtype(productTypeMap.getOrDefault(KEY_PRODUCT_SUB_TYPE, null));
                    }

                    // Verify ProductType and ProductSubtype SET response message
                    if (BeanUtil.isNull(matGroupMapProduct.getProductType()) || BeanUtil.isNull(matGroupMapProduct.getProductSubtype())) {
                        resultMessage.append(" : Product type subtype not config");
                    } else {
                        resultMessage.append(" : Success");
                    }

                    sapMatGroupMapProductList.add(matGroupMapProduct);

                    resultMessage.append(",");
                }
            }

            // Insert data
            if (BeanUtil.isNotEmpty(sapMatGroupMapProductList)) {
                dtSapMatGroupMapProductDao.insertList(sapMatGroupMapProductList);
            }

            // Update description log
            updateMchMappingProductLog(logId, "S", resultMessage.toString());

            // Sending mail mch mapping product
            sendEmail(true, resultMessage.toString());

            return new CommonResponseBean("20000", "Success", "Success");
        } catch (Exception e) {
            log.error("mappingMatGroupProduct error ", e);
            updateMchMappingProductLog(logId, "F", e.getMessage());
            sendEmail(false, e.getMessage());
            return new CommonResponseBean("50000", "Fail", e.getMessage());
        }
    }

    private LovMaster getMatGroupByConfig() {
        List<LovMaster> lovMasterList = lovMasterDao.listLovMasterByCriteria(
                SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                SAP_MCH_MAT_GROUP_MAP_PRODUCT,
                null, null, "Y"
        );

        return lovMasterList.get(0);
    }

    private static MasterValue getCreateValue(Date date) {
        MasterValue masterValue = new MasterValue();
        masterValue.setCreated(date);
        masterValue.setCreatedBy(DT_APP);
        masterValue.setLastUpd(date);
        masterValue.setLastUpdBy(DT_APP);
        return masterValue;
    }

    private Long insertMchMappingProductLog(DtSapMchMapping request) {
        Gson gson = new Gson();
        DtSapMchProductPriceLog mchProductPriceLog = new DtSapMchProductPriceLog();
        mchProductPriceLog.setServiceName(MCH_MAPPING_PRODUCT);
        mchProductPriceLog.setParaInput(gson.toJson(request));
        mchProductPriceLog.setStatus("W");
        mchProductPriceLog.setCreateValue(getCreateValue(new Date()));

        dtSapMchProductPriceLogDao.insert(mchProductPriceLog);

        return mchProductPriceLog.getTableId();
    }

    private void updateMchMappingProductLog(Long logId, String status, String resultMessage) {
        DtSapMchProductPriceLog mchProductPriceLog = dtSapMchProductPriceLogDao.getByLongPrimaryKey(logId);
        mchProductPriceLog.setStatus(status);

        String description = resultMessage;
        if ("S".equals(status) && BeanUtil.isNotEmpty(resultMessage)) {
            description = resultMessage.substring(0, resultMessage.length() - 1);
        }
        mchProductPriceLog.setDescription(description);

        dtSapMchProductPriceLogDao.update(mchProductPriceLog);
    }

    private Map<String, String> getProductTypeSubTypeByMapType(List<DtSapMatGroupMapType> matGroupMapTypeList) {
        try {
            Map<String, String> productTypeSubTypeMap = new HashMap<>();

            // Search for the highest matching material group level (lv2 -> lv1)
            DtSapMatGroupMapType mapTypeLv3 = matGroupMapTypeList.stream()
                    .filter(f -> BeanUtil.isNotNull(f.getMaterialGroupLv3()))
                    .findFirst()
                    .orElse(null);

            if (null != mapTypeLv3) {
                productTypeSubTypeMap.put(KEY_PRODUCT_TYPE, mapTypeLv3.getProductType());
                productTypeSubTypeMap.put(KEY_PRODUCT_SUB_TYPE, mapTypeLv3.getProductSubtype());
                return productTypeSubTypeMap;
            }

            DtSapMatGroupMapType mapTypeLv2 = matGroupMapTypeList.stream()
                    .filter(f -> BeanUtil.isNotNull(f.getMaterialGroupLv2()) && BeanUtil.isNull(f.getMaterialGroupLv3()))
                    .findFirst()
                    .orElse(null);

            if (null != mapTypeLv2) {
                productTypeSubTypeMap.put(KEY_PRODUCT_TYPE, mapTypeLv2.getProductType());
                productTypeSubTypeMap.put(KEY_PRODUCT_SUB_TYPE, mapTypeLv2.getProductSubtype());
                return productTypeSubTypeMap;
            }

            return productTypeSubTypeMap;
        } catch (Exception e) {
            throw new RuntimeException("Can't mapping product type subtype by map type");
        }
    }

    private void sendEmail(boolean isSuccess, String message) {
        try {
            // Build HTML body
            StringBuilder builderBody = new StringBuilder("<html><body> Dear all <br> &emsp;&emsp;&emsp;");
            builderBody.append("MCH Mapping Product Status <span style='color: ");
            builderBody.append(isSuccess ? "green;'><b>Success" : "red;'><b>Fail").append("</b></span>");

            if (isSuccess) {
                builderBody.append("<br> &emsp;");
                String[] splitMatGroup = message.split(",");
                String successMsg = String.join("<br> &emsp;", splitMatGroup);
                builderBody.append(successMsg);
            } else {
                builderBody.append("<br> &emsp; <b>Error Message :</b> ").append(message);
            }

            builderBody.append("</body></html>");

            // Setup email parameters and send
            EmailForm emailForm = new EmailForm();
            emailForm.setFrom("digitaltrading.app@ais.co.th");
            emailForm.setBody(builderBody.toString());
            emailForm.setToLists(new ArrayList<>());
            emailForm.setCcLists(new ArrayList<>());

            List<Object[]> email = emailService.queryEmail(MCH_MAPPING_PRODUCT);
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

            emailSenderService.sendEmail_html(emailForm);
            log.info("send email mch mapping product success");
        } catch (Exception e) {
            log.error("sending email mch mapping product error ", e);
        }
    }
}
