//package th.co.ais.dt.controller.spec.util.mt;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
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
//import th.co.ais.dt.entity.util.ProductMst;
//import th.co.ais.dt.controller.dto.CreateProductMasterFromDtItem;
//import th.co.ais.dt.controller.dto.CreateProductMasterFromSapBean;
//import th.co.ais.dt.controller.dto.CreateProductMasterFromSapItem;
//import th.co.ais.dt.controller.dto.ProductMasterServiceBean;
//
//@Slf4j
//public class ProductMasterServiceWebImplTest {
//
//	HttpClient client = new HttpClient();
//	Gson gson = new Gson();
//
//	@Test
//	public void createProductSap()  throws Exception {
//		PostMethod method = new PostMethod("http://192.168.75.129:8090/DTWS/api/productMasterSevice/v1/create-product-master-from-sap");
//		//PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/productMasterSevice/v1/create-product-master-from-sap");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		//String in = "{\"username\":\"soontarr\",\"password\":\"1\",\"locationCode\":\"1100\",\"idsFlg\":\"Y\"}";
//
//		CreateProductMasterFromSapBean in = new CreateProductMasterFromSapBean();
//		List<CreateProductMasterFromSapItem> productMasterList = new ArrayList<>();
//		CreateProductMasterFromSapItem el = new CreateProductMasterFromSapItem();
//		//ACC_ASSIGN_GROUP:11|BRAND:APPLE|CHANGE_NO:|COLOR:SPACE GREY|DOCUMENT:PRO114_2TB|FLAG_DELETE:|FLAG_SERIAL:X|FORMAT:|MATERIAL_DESC:NEW 5G AP IPAD PRO11 4 2TB-SGREY 01|MATERIAL_NO:NEW0APP1146-SG01|MATRIAL_GROUP:301218|MODEL:PRO114_2TB|OLD_MATERIAL_NO:K.SIRIPORN|REF_MAT:|SALES_ORG:1201|SIM_BRAND:|TRANS_UPD_BY:SIRILUKI|TRANS_UPD_DTM:20221026|UOM:SET|VALUATION_CLASS:9101|VAT_TYPE:1|VENDOR_CODE:null|CATEGORY:null|VTWEG:14|SPART:11|PRODH:0001100003
//        el.setACC_ASSIGN_GROUP("11");
//        el.setBRAND("APPLE");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO(null);
//        el.setCOLOR("SPACE GREY");
//        el.setDOCUMENT("PRO114_2TB");
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT(null);
//        el.setMATERIAL_DESC("NEW 5G AP IPAD PRO11 4 2TB-SGREY 01Z");
//        el.setMATERIAL_NO("NEW0APP1146-SG01");
//        el.setMATRIAL_GROUP("301218");
//        el.setMODEL("PRO114_2TB");
//        el.setOLD_MATERIAL_NO("K.SIRIPORN");
//        el.setPRODH("0001100003");
//        el.setREF_MAT(null);
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND(null);
//        el.setSPART("11");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20221026");
//        el.setUOM("SET");
//        el.setVALUATION_CLASS("9101");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("14");
//		productMasterList.add(el);
//		//productMasterList.add(el);
//
//		//BUNDLE
//		//ACC_ASSIGN_GROUP:11|BRAND:RUIO|CHANGE_NO:C3|COLOR:WHITE|DOCUMENT:S10|FLAG_DELETE:|FLAG_SERIAL:X|FORMAT:
//		//|MATERIAL_DESC:NEW 4G RU S10-WHITE 02 BS|MATERIAL_NO:NEW0RU00S10-WH02BS|MATRIAL_GROUP:301292|MODEL:S10
//		//|OLD_MATERIAL_NO:VIROONSA-DVM|REF_MAT:|SALES_ORG:1201|SIM_BRAND:|TRANS_UPD_BY:SIRILUKI|
//		//TRANS_UPD_DTM:20230109|UOM:SET|VALUATION_CLASS:9101|VAT_TYPE:1|VENDOR_CODE:null|CATEGORY:null|VTWEG:99|SPART:11|PRODH:0001100002
//		 el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("11");
//        el.setBRAND("RUIO");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO("C3");
//        el.setCOLOR("WHITE");
//        el.setDOCUMENT("S10");
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT(null);
//        el.setMATERIAL_DESC("NEW 4G RU S10-WHITE 02 BS");
//        el.setMATERIAL_NO("NEW0RU00S10-WH02BS");
//        el.setMATRIAL_GROUP("301292");
//        el.setMODEL("S10");
//        el.setOLD_MATERIAL_NO("VIROONSA-DVM");
//        el.setPRODH("0001100002");
//        el.setREF_MAT(null);
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND(null);
//        el.setSPART("11");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20230109");
//        el.setUOM("SET");
//        el.setVALUATION_CLASS("9101");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("99");
//		//productMasterList.add(el);
//
//		//SIM NO REF MAT
//		//ACC_ASSIGN_GROUP:12|BRAND:C3|CHANGE_NO:C3|COLOR:3PPReplugMarathon1800BundlePocket&Homewifi|DOCUMENT:|FLAG_DELETE:|FLAG_SERIAL:X|FORMAT:C3|MATERIAL_DESC:SIM 3PP Replug Marathon1800Bundle - C3|MATERIAL_NO:C300SIM3PPREM1800B|MATRIAL_GROUP:302360|MODEL:NON POOLING|
//        //OLD_MATERIAL_NO:3GPREPAID|REF_MAT:C300SIM3PPH3M2500B|SALES_ORG:1201|SIM_BRAND:3PP|TRANS_UPD_BY:SRIKANLK|TRANS_UPD_DTM:20230109|UOM:PC|VALUATION_CLASS:9103|VAT_TYPE:1|VENDOR_CODE:null|CATEGORY:null|
//        //VTWEG:99|SPART:18|PRODH:000120000100000002
//		el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("12");
//        el.setBRAND("C3");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO("C3");
//        el.setCOLOR("3PPReplugMarathon1800BundlePocket&Homewifi");
//        el.setDOCUMENT(null);
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT("C3");
//        el.setMATERIAL_DESC("SIM 3PP Replug Marathon1800Bundle - C3");
//        el.setMATERIAL_NO("C300SIM3PPREM1800B");
//        el.setMATRIAL_GROUP("302360");
//        el.setMODEL("NON POOLING");
//        el.setOLD_MATERIAL_NO("3GPREPAID");
//        el.setPRODH("000120000100000002");
//        el.setREF_MAT("");
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND("3PP");
//        el.setSPART("18");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20230109");
//        el.setUOM("PC");
//        el.setVALUATION_CLASS("9103");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("99");
//		//productMasterList.add(el);
//
//
//		//CARD
//		//ACC_ASSIGN_GROUP:13|BRAND:100 B|CHANGE_NO:|COLOR:|DOCUMENT:100|FLAG_DELETE:|FLAG_SERIAL:|FORMAT:|MATERIAL_DESC:CASH CARD 100B 12call Theme 2023|MATERIAL_NO:8858839101434|MATRIAL_GROUP:301520|
//        //MODEL:|OLD_MATERIAL_NO:|REF_MAT:CASH100|SALES_ORG:1401|SIM_BRAND:|TRANS_UPD_BY:KAMONRAP|TRANS_UPD_DTM:20221109|UOM:PC|VALUATION_CLASS:9801|VAT_TYPE:4|VENDOR_CODE:null|CATEGORY:null|VTWEG:17|SPART:13|PRODH:000130000100000002
//        el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("13");
//        el.setBRAND("100 B");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO(null);
//        el.setCOLOR(null);
//        el.setDOCUMENT("100");
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL(null);
//        el.setFORMAT(null);
//        el.setMATERIAL_DESC("CASH CARD 100B 12call Theme 2023");
//        el.setMATERIAL_NO("8858839101434");
//        el.setMATRIAL_GROUP("301520");
//        el.setMODEL(null);
//        el.setOLD_MATERIAL_NO(null);
//        el.setPRODH("000130000100000002");
//        el.setREF_MAT("");
//        el.setSALES_ORG("1401");
//        el.setSIM_BRAND("3PP");
//        el.setSPART("13");
//        el.setTRANS_UPD_BY("KAMONRAP");
//        el.setTRANS_UPD_DTM("20221109");
//        el.setUOM("PC");
//        el.setVALUATION_CLASS("9801");
//        el.setVAT_TYPE("4");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("17");
//		//productMasterList.add(el);
//
//
//        //ACCESSORY
//		//ACC_ASSIGN_GROUP:16|BRAND:SAMSUNG|CHANGE_NO:|COLOR:|DOCUMENT:|FLAG_DELETE:|FLAG_SERIAL:X|FORMAT:|MATERIAL_DESC:SS RUGGED GADGET CASE S23|
//        //MATERIAL_NO:DOA0SSRGS23-D00|MATRIAL_GROUP:301385|MODEL:RUGGEDGADCASES23|OLD_MATERIAL_NO:NATHAROS-DVM|REF_MAT:|SALES_ORG:1201|SIM_BRAND:|TRANS_UPD_BY:SIRILUKI|
//        //TRANS_UPD_DTM:20230208|UOM:SET|VALUATION_CLASS:9301|VAT_TYPE:1|VENDOR_CODE:null|CATEGORY:null|VTWEG:99|SPART:16|PRODH:00015
//        el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("16");
//        el.setBRAND("SAMSUNG");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO(null);
//        el.setCOLOR(null);
//        el.setDOCUMENT(null);
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT(null);
//        el.setMATERIAL_DESC("SS RUGGED GADGET CASE S23");
//        el.setMATERIAL_NO("DOA0SSRGS23-D00");
//        el.setMATRIAL_GROUP("301385");
//        el.setMODEL("RUGGEDGADCASES23");
//        el.setOLD_MATERIAL_NO("NATHAROS-DVM");
//        el.setPRODH("00015");
//        el.setREF_MAT("");
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND("3PP");
//        el.setSPART("16");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20230208");
//        el.setUOM("SET");
//        el.setVALUATION_CLASS("9301");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("99");
//		//productMasterList.add(el);
//
//
//		//ESIM
//		//ACC_ASSIGN_GROUP:12|BRAND:ES|CHANGE_NO:ES|COLOR:3PPeSIMeUICCLuckyTraveller350|DOCUMENT:|FLAG_DELETE:|FLAG_SERIAL:X|
//        //FORMAT:ES|MATERIAL_DESC:SIM 3PP eSIM  NonPhysLuckyTraveller350|MATERIAL_NO:ES00SIM3PPLK350|MATRIAL_GROUP:302390|
//        //MODEL:INBOUND|OLD_MATERIAL_NO:EUICC|REF_MAT:|SALES_ORG:1201|SIM_BRAND:ESM|TRANS_UPD_BY:SRIKANLK|TRANS_UPD_DTM:20230109|
//        //UOM:PC|VALUATION_CLASS:9108|VAT_TYPE:1|VENDOR_CODE:null|CATEGORY:null|VTWEG:13|SPART:21|PRODH:0001200003
//        el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("12");
//        el.setBRAND("ES");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO("ES");
//        el.setCOLOR("3PPeSIMeUICCLuckyTraveller350");
//        el.setDOCUMENT(null);
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT("ES");
//        el.setMATERIAL_DESC("SIM 3PP eSIM  NonPhysLuckyTraveller350");
//        el.setMATERIAL_NO("ES00SIM3PPLK350");
//        el.setMATRIAL_GROUP("302390");
//        el.setMODEL("INBOUND");
//        el.setOLD_MATERIAL_NO("EUICC");
//        el.setPRODH("0001200003");
//        el.setREF_MAT("");
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND("ESM");
//        el.setSPART("21");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20230109");
//        el.setUOM("PC");
//        el.setVALUATION_CLASS("9108");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("13");
//		//productMasterList.add(el);
//
//		//SIM MAIN
//		//ACC_ASSIGN_GROUP:12|BRAND:NW|CHANGE_NO:NW|COLOR:3PPHalf3in1NWGOMOWork99|DOCUMENT:|FLAG_DELETE:|FLAG_SERIAL:X|FORMAT:NW|MATERIAL_DESC:SIM 3PP GOMO Work 99 - NW|MATERIAL_NO:NW00SIM3PPGMW99|
//		//MATRIAL_GROUP:302360|MODEL:NON POOLING|OLD_MATERIAL_NO:3GPREPAID|REF_MAT:|SALES_ORG:1201|SIM_BRAND:3PP|TRANS_UPD_BY:SRIKANLK|TRANS_UPD_DTM:20210928|UOM:PC|VALUATION_CLASS:9103|VAT_TYPE:1|
//		//VENDOR_CODE:null|CATEGORY:null|VTWEG:99|SPART:18|PRODH:000120000100000002
//		el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("12");
//        el.setBRAND("NW");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO("NW");
//        el.setCOLOR("3PPHalf3in1NWGOMOWork99");
//        el.setDOCUMENT(null);
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT("NW");
//        el.setMATERIAL_DESC("SIM 3PP GOMO Work 99 - NW");
//        el.setMATERIAL_NO("NW00SIM3PPGMW99");
//        el.setMATRIAL_GROUP("302360");
//        el.setMODEL("NON POOLING");
//        el.setOLD_MATERIAL_NO("3GPREPAID");
//        el.setPRODH("000120000100000002");
//        el.setREF_MAT("");
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND("3PP");
//        el.setSPART("18");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20230109");
//        el.setUOM("PC");
//        el.setVALUATION_CLASS("9103");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("99");
//		//productMasterList.add(el);
//
//        //ACC_ASSIGN_GROUP:12|BRAND:NW|CHANGE_NO:NW|COLOR:3PPHalf3in1NWGOMO99B|DOCUMENT:|FLAG_DELETE:|FLAG_SERIAL:X|FORMAT:NW|MATERIAL_DESC:SIM 3PP GOMO 99 - NW|MATERIAL_NO:NW00SIM3PPGOMO99|
//        //MATRIAL_GROUP:302360|MODEL:NON POOLING|OLD_MATERIAL_NO:3GPREPAID|REF_MAT:NW00SIM3PPGMW99|SALES_ORG:1201|SIM_BRAND:3PP|TRANS_UPD_BY:SRIKANLK|TRANS_UPD_DTM:20230131|UOM:PC|VALUATION_CLASS:9103|VAT_TYPE:1|VENDOR_CODE:null|CATEGORY:null|
//        //VTWEG:17|SPART:18|PRODH:000120000100000002
//        el = new CreateProductMasterFromSapItem();
//        el.setACC_ASSIGN_GROUP("12");
//        el.setBRAND("NW");
//        el.setCATEGORY(null);
//        el.setCHANGE_NO("NW");
//        el.setCOLOR("3PPHalf3in1NWGOMO99B");
//        el.setDOCUMENT(null);
//        el.setFLAG_DELETE(null);
//        el.setFLAG_SERIAL("X");
//        el.setFORMAT("NW");
//        el.setMATERIAL_DESC("SIM 3PP GOMO 99 - NW");
//        el.setMATERIAL_NO("NW00SIM3PPGOMO99AZ01");
//        el.setMATRIAL_GROUP("302360");
//        el.setMODEL("NON POOLING");
//        el.setOLD_MATERIAL_NO("3GPREPAID");
//        el.setPRODH("000120000100000002");
//        //el.setREF_MAT("NW00SIM3PPGMW99-BK");
//        //NW00SIM3PPGMW99
//        el.setSALES_ORG("1201");
//        el.setSIM_BRAND("3PP");
//        el.setSPART("18");
//        el.setTRANS_UPD_BY("SIRILUKI");
//        el.setTRANS_UPD_DTM("20230109");
//        el.setUOM("PC");
//        el.setVALUATION_CLASS("9103");
//        el.setVAT_TYPE("1");
//        el.setVENDOR_CODE(null);
//        el.setVTWEG("17");
//		//productMasterList.add(el);
//
//		in.setProductMasterList(productMasterList);
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void createProductDT()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8080/DTWS/api/productMasterSevice/v1/create-product-master-from-dt");
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		CreateProductMasterFromDtItem in = new CreateProductMasterFromDtItem();
//		List<ProductMst> listPro = new ArrayList<>();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
//		//117597,'AWN','DEVICE','HANDSET','APPLE','PRO114_512GB',NULL,NULL,NULL,NULL,NULL,'SILVER',NULL,'NEW0APP1144-SV01','Serial',
//		//'SET','Y','NEW 5G AP IPAD PRO11 4 512GB-SILVER 01','NEW 5G AP IPAD PRO11 4 512GB-SILVER 01',
//		//'2022-10-19 00:00:00',NULL,'Y','SIRILUKI','2022-10-19 00:00:00','SIRILUKI','2022-10-26 00:00:00','11','301218',
//		//'9101','','','','','N','','TDMAPP','2022-10-19 16:05:00','TDMAPP','2022-10-26 23:47:00','','','11','1100003'
//		ProductMst item = new ProductMst();
//		//item.setProductId(117597L);
//		item.setTmpProductIdFromDt("1175979");
//
//		item.setCompany("AWN");
//		item.setProductType("DEVICE");
//		item.setProductSubType("HANDSET");
//		item.setBrand("APPLE");
//		item.setModel("PRO114_512GB");
//		item.setFeature("");
//		item.setNetwork("");
//		item.setRegion("");
//		item.setSubRegion("");
//		item.setCapacity("");
//		item.setColor("SILVER");
//		item.setFaceValue("");
//		item.setMatCode("NEW0APP1144-SV01");
//		item.setMatType("Serial");
//		item.setUnitName("SET");
//		item.setVatType("Y");
//		item.setSapDescription("NEW 5G AP IPAD PRO11 4 512GB-SILVER 01");
//		item.setTdmDescription("NEW 5G AP IPAD PRO11 4 512GB-SILVER 01");
//		item.setEffectiveDateDt(dateFormat.format(new Date()));
//		item.setExpireDateDt(dateFormat.format(new Date()));
//		item.setActiveFlag("Y");
//
////		MasterValue trn = new MasterValue();
////		trn.setCreated(new Date());
////		trn.setCreatedBy("SIRILUKI");
////		trn.setLastUpd(new Date());
////		trn.setLastUpdBy("SIRILUKI");
//
//		item.setTransCreatedDt(dateFormat.format(new Date()));
//		item.setTransCreatedByDt("SIRILUKI");
//		item.setTransLastUpdDt(dateFormat.format(new Date()));
//		item.setTransLastUpdByDt("SIRILUKI");
//
//		item.setAssignmentGroup("11");
//		item.setMatGroup("301218");
//		item.setValuationClass("9101");
//		item.setRefMatCode(null);
//		item.setAttribute01("");
//		item.setAttribute02("");
//		item.setAttribute03("");
//		item.setProdTDMFlag("N");
//		item.setGrade("");
//
////		MasterValue ctr = new MasterValue();
////		ctr.setCreated(new Date());
////		ctr.setCreatedBy("TDMAPP");
////		ctr.setLastUpd(new Date());
////		ctr.setLastUpdBy("TDMAPP");
//
//		item.setCreatedDt(dateFormat.format(new Date()));
//		item.setCreatedByDt("TDMAPP");
//		item.setLastUpdDt(dateFormat.format(new Date()));
//		item.setLastUpdByDt("TDMAPP");
//
//		item.setVendorCode("VEN");
//		item.setCategory("CAT");
//		item.setDivision("11");
//		item.setProduct_hierarchy("1100003");
//
//		listPro.add(item);
//
//
//		//	 (117600,'AWN','DEVICE','HANDSET','APPLE','PRO114_2TB',NULL,NULL,NULL,NULL,NULL,'SPACE GREY',NULL,'NEW0APP1146-SG01','Serial','SET','Y','NEW 5G AP IPAD PRO11 4 2TB-SGREY 01','NEW 5G AP IPAD PRO11 4 2TB-SGREY 01','2022-10-19 00:00:00',NULL,'Y','SIRILUKI','2022-10-19 00:00:00','SIRILUKI','2022-10-26 00:00:00','11','301218','9101','','','','','N','','TDMAPP','2022-10-19 16:05:00','TDMAPP','2022-10-26 23:47:00','','','11','1100003'),
//
//		item = new ProductMst();
//		//item.setProductId(117597L);
//		item.setTmpProductIdFromDt("117600");
//
//		item.setCompany("AWN");
//		item.setProductType("DEVICE");
//		item.setProductSubType("HANDSET");
//		item.setBrand("APPLE");
//		item.setModel("PRO114_2TB");
//		item.setFeature("");
//		item.setNetwork("");
//		item.setRegion("");
//		item.setSubRegion("");
//		item.setCapacity("");
//		item.setColor("SPACE GREY");
//		item.setFaceValue("");
//		item.setMatCode("NEW0APP1146-SG01");
//		item.setMatType("Serial");
//		item.setUnitName("SET");
//		item.setVatType("Y");
//		item.setSapDescription("NEW 5G AP IPAD PRO11 4 2TB-SGREY 01");
//		item.setTdmDescription("NEW 5G AP IPAD PRO11 4 2TB-SGREY 01");
//		item.setEffectiveDateDt(dateFormat.format(new Date()));
//		item.setExpireDateDt(dateFormat.format(new Date()));
//		item.setActiveFlag("Y");
//
////		MasterValue trn = new MasterValue();
////		trn.setCreated(new Date());
////		trn.setCreatedBy("SIRILUKI");
////		trn.setLastUpd(new Date());
////		trn.setLastUpdBy("SIRILUKI");
//
//		item.setTransCreatedDt(dateFormat.format(new Date()));
//		item.setTransCreatedByDt("SIRILUKI");
//		item.setTransLastUpdDt(dateFormat.format(new Date()));
//		item.setTransLastUpdByDt("SIRILUKI");
//
//		item.setAssignmentGroup("11");
//		item.setMatGroup("301218");
//		item.setValuationClass("9101");
//		item.setRefMatCode(null);
//		item.setAttribute01("");
//		item.setAttribute02("");
//		item.setAttribute03("");
//		item.setProdTDMFlag("N");
//		item.setGrade("");
//
////		MasterValue ctr = new MasterValue();
////		ctr.setCreated(new Date());
////		ctr.setCreatedBy("TDMAPP");
////		ctr.setLastUpd(new Date());
////		ctr.setLastUpdBy("TDMAPP");
//
//		item.setCreatedDt(dateFormat.format(new Date()));
//		item.setCreatedByDt("TDMAPP");
//		item.setLastUpdDt(dateFormat.format(new Date()));
//		item.setLastUpdByDt("TDMAPP");
//
//		item.setVendorCode("VEN");
//		item.setCategory("CAT");
//		item.setDivision("11");
//		item.setProduct_hierarchy("1100003");
//
//		listPro.add(item);
//
//
//		in.setListProductMstDt(listPro);
//
//		log.info(gson.toJson(in)) ;
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(in), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void queryProductMasterServiceTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/productMasterSevice/v1/query-product-master-service");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/productMasterSevice/v1/query-product-master-service");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//
//		ProductMst request = new ProductMst();
//		request.setCompany("AWN");
//		request.setMatCode("GUMYTEST7");
//		request.setProductType("SERVICE");
//		request.setProductSubType("GUPAYVAT");
//		request.setTdmDescription("GUMYTEST705");
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void updateProductMasterServiceTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/productMasterSevice/v1/update-product-master-service");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/productMasterSevice/v1/query-product-master-service");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ProductMasterServiceBean request = new ProductMasterServiceBean();
//
//		request.setCompany("AWN");
//		request.setMatCode("GUMYTEST703");
//		request.setProductType("SERVICE");
//		request.setProductSubType("GUPAYVAT");
//		request.setDescription("GUMYTEST7033333");
//		request.setProductId("794638");
//		request.setUserId("soontarr");
//		request.setActiveFlag("Y");
//		request.setMappingCodeMobileApp("NOOM0002");
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void insertProductMasterServiceTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/productMasterSevice/v1/insert-product-master-service");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/productMasterSevice/v1/query-product-master-service");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//
//		ProductMasterServiceBean request = new ProductMasterServiceBean();
//
//		request.setCompany("AWN");
//		request.setMatCode("GUMYTEST7055");
//		request.setProductType("SERVICE");
//		request.setProductSubType("GUPAYVAT");
//		request.setDescription("GUMYTEST7055555");
//		request.setUserId("soontarr");
//		request.setActiveFlag("Y");
//		request.setMappingCodeMobileApp("NOOM000235");
//		request.setProductPriceGroup("EUP");
//		request.setValueVatType("Y");
//		request.setVatType("Y");
//		request.setVatRate("7");
//
//		request.setPriceIncVat("107");
//		request.setPriceExcVat("100");
//		request.setPriceVat("7");
//
//		request.setExpireDate("");
//		request.setEffectiveDate("");
//
//
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(request), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//	@Test
//	public void uploadProductMasterServiceTest()  throws Exception {
//		PostMethod method = new PostMethod("http://localhost:8090/DTWS/api/productMasterSevice/v1/upload-product-master-service");
//		//PostMethod method = new PostMethod("http://10.138.47.131:8080/DTWS/api/productMasterSevice/v1/query-product-master-service");
//
//		method.setRequestEntity(new StringRequestEntity("basic", "application/json", null));
//		ProductMasterServiceBean el = new ProductMasterServiceBean();
//		el.setProductSubType("GUPAYVAT");
//		el.setUserId("soontarr");
//		el.setCompany("AWN");
//		el.setProductType("SERVICE");
//		el.setProductPriceGroup("EUP");
//		el.setVatType("Y");
//		el.setVatRate("7");
//		el.setActiveFlag("Y");
//		List<ProductMasterServiceBean> items = new ArrayList<>();
//		ProductMasterServiceBean request = new ProductMasterServiceBean();
//		request.setCompany("AWN");
//		request.setMatCode("GUMYTEST7066");
//		request.setProductType("SERVICE");
//		request.setProductSubType("GUPAYVAT");
//		request.setDescription("GUMYTEST7066666");
//		request.setUserId("soontarr");
//		request.setActiveFlag("Y");
//		request.setMappingCodeMobileApp("NOOM000236");
//		request.setProductPriceGroup("EUP");
//		request.setValueVatType("Y");
//		request.setVatType("Y");
//		request.setVatRate("7");
//
//		request.setPriceIncVat("107");
//		request.setPriceExcVat("100");
//		request.setPriceVat("7");
//
//		request.setExpireDate("");
//		request.setEffectiveDate("");
//		items.add(request);
//
//		ProductMasterServiceBean request2 = new ProductMasterServiceBean();
//		request2.setCompany("AWN");
//		request2.setMatCode("GUMYTEST777");
//		request2.setProductType("SERVICE");
//		request2.setProductSubType("GUPAYVAT");
//		request2.setDescription("request27777");
//		request2.setUserId("soontarr");
//		request2.setActiveFlag("Y");
//		request2.setMappingCodeMobileApp("NOOM000237");
//		request2.setProductPriceGroup("EUP");
//		request2.setValueVatType("Y");
//		request2.setVatType("Y");
//		request2.setVatRate("7");
//
//		request2.setPriceIncVat("107");
//		request2.setPriceExcVat("100");
//		request2.setPriceVat("7");
//
//		request2.setExpireDate("");
//		request2.setEffectiveDate("");
//		items.add(request2);
//
//		el.setProductMasterList(items );
//
//		method.setRequestEntity(new StringRequestEntity(gson.toJson(el), "application/json", "UTF-8"));
//		int status = client.executeMethod(method);
//		log.info(method.getResponseBodyAsString()) ;
//		Assert.assertEquals("20000", "20000");
//		method.releaseConnection();
//	}
//
//
//}
