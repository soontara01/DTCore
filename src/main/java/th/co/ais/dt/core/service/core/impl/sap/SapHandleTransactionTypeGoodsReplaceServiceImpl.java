package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeGoodsReplaceService;
import th.co.ais.dt.entity.sap.DtSapCancelReserve;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostDiscountItems;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapCancelReserveDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTaxItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeGoodsReplaceServiceImpl implements ISapHandleTransactionTypeGoodsReplaceService {

	private final IDtSapTransactionDao dtSapTransactionDao;
	private final IDtSapPostHeaderDao dtSapPostHeaderDao;
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao;
	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao;
	private final IDtSapPostTenderDao dtSapPostTenderDao;
	private final ISapCallApiService sapCallApiService;
	private final DTConfig dTConfig;
	private final IDtSapCancelReserveDao dtSapCancelReserveDao ;
	private final ILovMasterDao lovMasterDao ;

	@Override
	public DtSapTransaction queryDtSapTransactionById(Long sapTranId) {
		return dtSapTransactionDao.getByLongPrimaryKey(sapTranId);
	}

	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long transactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(transactionType);
	}

	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransaction(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());

			// Query sql5
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql5(
					dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());

			// Query transaction type code config
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao
					.getByLongPrimaryKey(dtSapTransaction.getTransactionType());

			// HEADER
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID());
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());

			// By config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());

			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID()); //null
			dtSapPostHeader
					.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID()); //null
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER()); //null
			dtSapPostHeader.setCreateValue(masterValue);
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");

			dtSapPostHeaderDao.insert(dtSapPostHeader);

			// DISCOUNT HEADER
//			if (BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER())) {
//				DtSapPostDiscountHeader dtSapPostDiscountHeader = new DtSapPostDiscountHeader();
//				dtSapPostDiscountHeader.setDISCOUNTID(listQueryPostTransactionBean.get(0).getHDISCOUNTID());
//				dtSapPostDiscountHeader
//						.setDISCOUNTSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getHDISCOUNTSEQUENCENUMBER());
//				dtSapPostDiscountHeader.setDISCOUNTTYPECODE(listQueryPostTransactionBean.get(0).getHDISCOUNTTYPECODE());
//				dtSapPostDiscountHeader.setREDUCTIONAMOUNT(listQueryPostTransactionBean.get(0).getHREDUCTIONAMOUNT());
//				dtSapPostDiscountHeader.setCreateValue(masterValue);
//				dtSapPostDiscountHeader.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
//
//				dtSapPostDiscountHeaderDao.insert(dtSapPostDiscountHeader);
//			}

			// Split retail type and reason code
			String[] retailTypeCode = config.getRetailtypecode().split("\\|");
			String[] retailReasonCode = config.getRetailreasoncode().split("\\|");

			BigDecimal sumSaleAmount = new BigDecimal(0L);
			int count = 0;
			for (QueryPostTransactionBean postTransaction : listQueryPostTransactionBean) {

				count++;
				// SALES ITEM OLD
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(count));

				// By config
				dtSapPostSalesItems.setRETAILTYPECODE(retailTypeCode[0]);
				dtSapPostSalesItems.setRETAILREASONCODE(retailReasonCode[0]);

				dtSapPostSalesItems.setITEMIDQUALIFIER(postTransaction.getITEMIDQUALIFIER());
				dtSapPostSalesItems.setITEMID(postTransaction.getITEMID());
				dtSapPostSalesItems.setRETAILQUANTITY("-"+postTransaction.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(postTransaction.getSALESUNITOFMEASURE());
				dtSapPostSalesItems.setSALESAMOUNT(postTransaction.getSALESAMOUNT());
				dtSapPostSalesItems.setNORMALSALESAMOUNT(postTransaction.getNORMALSALESAMOUNT());
				dtSapPostSalesItems.setPROMOTIONID(postTransaction.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(postTransaction.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(postTransaction.getSERIALNUMBER());
				dtSapPostSalesItems.setACTUALUNITPRICE(postTransaction.getACTUALUNITPRICE());
				dtSapPostSalesItems.setSO_NO(postTransaction.getSO_NO());
				dtSapPostSalesItems.setITEM_NO(postTransaction.getITEM_NO());
				dtSapPostSalesItems.setIUID(postTransaction.getIUID());
				dtSapPostSalesItems.setCreateValue(masterValue);
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);

				count++;
				// SALES ITEM NEW
				DtSapPostSalesItems dtSapPostSalesItemsNew = new DtSapPostSalesItems();
				dtSapPostSalesItemsNew.setRETAILSEQUENCENUMBER(String.valueOf(count));

				// By config
				dtSapPostSalesItemsNew.setRETAILTYPECODE(retailTypeCode[1]);
				dtSapPostSalesItemsNew.setRETAILREASONCODE(retailReasonCode[1]);

				dtSapPostSalesItemsNew.setITEMIDQUALIFIER(postTransaction.getITEMIDQUALIFIER_NEW());
				dtSapPostSalesItemsNew.setITEMID(postTransaction.getITEMID_NEW());
				dtSapPostSalesItemsNew.setRETAILQUANTITY(postTransaction.getRETAILQUANTITY_NEW());
				dtSapPostSalesItemsNew.setSALESUNITOFMEASURE(postTransaction.getSALESUNITOFMEASURE_NEW());
				dtSapPostSalesItemsNew.setSALESAMOUNT(postTransaction.getSALESAMOUNT_NEW());
				dtSapPostSalesItemsNew.setNORMALSALESAMOUNT(postTransaction.getNORMALSALESAMOUNT_NEW());
				dtSapPostSalesItemsNew.setPROMOTIONID(postTransaction.getPROMOTIONID_NEW());
				dtSapPostSalesItemsNew.setBATCHID(postTransaction.getBATCHID_NEW());
				dtSapPostSalesItemsNew.setSERIALNUMBER(postTransaction.getSERIALNUMBER_NEW());
				dtSapPostSalesItemsNew.setACTUALUNITPRICE(postTransaction.getACTUALUNITPRICE_NEW());
				dtSapPostSalesItemsNew.setSO_NO(postTransaction.getSO_NO_NEW());
				dtSapPostSalesItemsNew.setITEM_NO(postTransaction.getITEM_NO_NEW());
				dtSapPostSalesItemsNew.setIUID(postTransaction.getIUID_NEW());
				dtSapPostSalesItemsNew.setCreateValue(masterValue);
				dtSapPostSalesItemsNew.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

				dtSapPostSalesItemsDao.insert(dtSapPostSalesItemsNew);

				// Sum sale amount for TENDER
				BigDecimal saleAmount = new BigDecimal(
						dtSapPostSalesItems.getSALESAMOUNT() + dtSapPostSalesItemsNew.getSALESAMOUNT());
				sumSaleAmount = sumSaleAmount.add(saleAmount);
			}

			// TENDER [ZT84]
			DtSapPostTender dtSapPostTender = new DtSapPostTender();
			dtSapPostTender.setTENDERSEQUENCENUMBER("1");
			dtSapPostTender.setTENDERTYPECODE("ZT84");
			dtSapPostTender.setTENDERAMOUNT(sumSaleAmount.toString());
			dtSapPostTender.setTENDERCURRENCY("THB");
			dtSapPostTender.setCreateValue(masterValue);
			dtSapPostTender.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

			dtSapPostTenderDao.insert(dtSapPostTender);

			return dtSapPostHeader;
		} catch (Exception e) {
			log.error("queryInfoAndInsertPostTransaction error ", e);
			return null;
		}
	}

	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		return sapCallApiService.callPostTransactionApi(stSapPostHeader);
	}

	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
		try {
			dtSapTransaction.getCreateValue().setLastUpd(new Date());

			dtSapTransactionDao.update(dtSapTransaction);
		} catch (Exception e) {
			log.error("updateDtSapTransactionAfterWorkflow error ", e);
		}
	}

	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		} catch (Exception e) {
			log.error("updateDtSapPostHeader error ", e);
		}
	}

	private List<QueryPostTransactionBean> queryPostTransactionSql5(String docNo, String company) {
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// TODO: Change query to postgres when postgres support already
				// Query from postgres
				return dtSapTransactionTypeDao.queryPostTransactionSql5(docNo, company);
		 }  
		  else { 
			// Query from on prem
				return queryPostTransactionSql5OnPrem(docNo, company);
		 }
		

		
	}

	private List<QueryPostTransactionBean> queryPostTransactionSql5OnPrem(String docNo, String company) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json");

		QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
		in.setDocNo(docNo);
		in.setCompany(company);

		String out = c.HttpClient(properties,
				dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql5", gson.toJson(in), "POST",
				null);
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);

		return res.getListQueryPostTransactionBean();
	}
	
	@Override
	public List<DtSapCancelReserve> cancelReserveGoodReplace(DtSapTransaction dtSapTransaction) {
		List<DtSapCancelReserve> res = new ArrayList<>();
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// Query sql5
			List<QueryPostTransactionBean> listQueryPostTransactionBean = queryPostTransactionSql5(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			if(BeanUtil.isNotEmpty(listQueryPostTransactionBean) && BeanUtil.isNotEmpty(listQueryPostTransactionBean.get(0).getSAP_RESERVE_NO())) {
				String[] listReserve = listQueryPostTransactionBean.get(0).getSAP_RESERVE_NO().split("\\|");
				
				for(String re : listReserve ) {
					DtSapCancelReserve dtSapCancelReserve = new DtSapCancelReserve();
					dtSapCancelReserve.setCreateValue(masterValue) ;
					dtSapCancelReserve.setSapTranId(dtSapTransaction.getSapTranId());
					dtSapCancelReserve.setStatus("W");
					dtSapCancelReserve.setSapReserveNo(re) ;
					dtSapCancelReserveDao.insert(dtSapCancelReserve);
					
					res.add(dtSapCancelReserve);
				}
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res ;

	}
	
	public DtSapCancelReserve callCancelReserveApi(DtSapCancelReserve dtSapCancelReserve) {
		DtSapCancelReserve res = sapCallApiService.callCancelReserveApi(dtSapCancelReserve);
		return res ;
	}
	
	public void updatedtSapCancelReserve(DtSapCancelReserve dtSapCancelReserve) {
		dtSapCancelReserveDao.update(dtSapCancelReserve);
	}

}
