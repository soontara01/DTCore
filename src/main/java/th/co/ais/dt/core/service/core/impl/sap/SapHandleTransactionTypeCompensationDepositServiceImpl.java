package th.co.ais.dt.core.service.core.impl.sap;

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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCompensationDepositService;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHText;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapOrderItemSerial;
import th.co.ais.dt.entity.sap.DtSapPostDiscountHeader;
import th.co.ais.dt.entity.sap.DtSapPostDiscountItems;
import th.co.ais.dt.entity.sap.DtSapPostFinalcial;
import th.co.ais.dt.entity.sap.DtSapPostGoodsMovement;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemSerialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostMappingTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTaxItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySalesOrderTransactionUpdate;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCompensationDepositServiceImpl
		implements ISapHandleTransactionTypeCompensationDepositService {

	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;

	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;

	private final IDtSapPostHeaderDao dtSapPostHeaderDao;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao;
	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao;
	private final IDtSapPostTenderDao dtSapPostTenderDao;
	private final ISapCallApiService sapCallApiService;
	private final IDtSapTransactionDao dtSapTransactionDao;
	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao;
	private final DTConfig dTConfig;

	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(TransactionType);
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

			// query sql8
			List<QueryPostTransactionBean> listQueryPostTransactionBean = this.queryPostTransactionSql8(dtSapTransaction.getDocNo());

			// query transaction type code config
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao
					.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType()));

			// HEADER======================
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			dtSapPostHeader.setRETAILSTOREID(listQueryPostTransactionBean.get(0).getRETAILSTOREID());
			dtSapPostHeader.setBUSINESSDAYDATE(listQueryPostTransactionBean.get(0).getBUSINESSDAYDATE());

			// from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());

			dtSapPostHeader.setWORKSTATIONID(listQueryPostTransactionBean.get(0).getWORKSTATIONID());
			dtSapPostHeader
					.setTRANSACTIONSEQUENCENUMBER(listQueryPostTransactionBean.get(0).getTRANSACTIONSEQUENCENUMBER());
			dtSapPostHeader.setBEGINDATETIMESTAMP(listQueryPostTransactionBean.get(0).getBEGINDATETIMESTAMP());
			dtSapPostHeader.setENDDATETIMESTAMP(listQueryPostTransactionBean.get(0).getENDDATETIMESTAMP());
			dtSapPostHeader.setOPERATORID(listQueryPostTransactionBean.get(0).getOPERATORID());
			dtSapPostHeader.setTRANSACTIONCURRENCY(listQueryPostTransactionBean.get(0).getTRANSACTIONCURRENCY());
			dtSapPostHeader.setPARTNERID(listQueryPostTransactionBean.get(0).getPARTNERID());
			dtSapPostHeader.setORIGTRANSNUMBER(listQueryPostTransactionBean.get(0).getORIGTRANSNUMBER());
			dtSapPostHeader.setCreateValue(masterValue);

			// set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			// END HEADER======================
			int itemNo = 1 ;
			for (QueryPostTransactionBean el : listQueryPostTransactionBean) {

				// SALES ITEM
				DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
				//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(el.getRETAILSEQUENCENUMBER());
				dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));

				// from config
				dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());

				// from config ***
				if (el != null && el.getFREEGOODS_WAIT_FLG() != null) {
					dtSapPostSalesItems.setRETAILREASONCODE(
							(el.getFREEGOODS_WAIT_FLG().equals("Y") ? config.getRetailreasoncode().split("\\|")[0]
									: config.getRetailreasoncode().split("\\|")[1]));
				}

				dtSapPostSalesItems.setITEMIDQUALIFIER(el.getITEMIDQUALIFIER());
				dtSapPostSalesItems.setITEMID(el.getITEMID());
				dtSapPostSalesItems.setRETAILQUANTITY(el.getRETAILQUANTITY());
				dtSapPostSalesItems.setSALESUNITOFMEASURE(el.getSALESUNITOFMEASURE());
				dtSapPostSalesItems.setSALESAMOUNT(el.getSALESAMOUNT());
				dtSapPostSalesItems.setNORMALSALESAMOUNT(el.getNORMALSALESAMOUNT());
				dtSapPostSalesItems.setPROMOTIONID(el.getPROMOTIONID());
				dtSapPostSalesItems.setBATCHID(el.getBATCHID());
				dtSapPostSalesItems.setSERIALNUMBER(el.getSERIALNUMBER());
				dtSapPostSalesItems.setACTUALUNITPRICE(el.getACTUALUNITPRICE());
				dtSapPostSalesItems.setSO_NO(el.getSO_NO());
				dtSapPostSalesItems.setITEM_NO(el.getITEM_NO());
				dtSapPostSalesItems.setIUID(el.getIUID());
				dtSapPostSalesItems.setRETAILREASONCODE(el.getRETAILREASONCODE());

				dtSapPostSalesItems.setCreateValue(masterValue);
				// set herder id
				dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
				dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
				itemNo = itemNo +1 ;

			}

			DtSapPostTender dtSapPostTender = new DtSapPostTender();

			dtSapPostTender.setTENDERAMOUNT("0");
			dtSapPostTender.setTENDERCURRENCY("THB");
			dtSapPostTender.setTENDERSEQUENCENUMBER("1");
			dtSapPostTender.setTENDERTYPECODE("ZT84");

			dtSapPostTender.setCreateValue(masterValue);
			// set herder id
			dtSapPostTender.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());

			dtSapPostTenderDao.insert(dtSapPostTender);

			return dtSapPostHeader;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorderCompensationDeposit(DtSapTransaction dtSapTransaction) {
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query update sales order
			List<QuerySalesOrderTransactionUpdate> listQuerySalesOrderUpdateBean = this.querySalesOrderUpdate(dtSapTransaction.getDocNo());
			
			//set values
			// HEADER======================
			DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
			dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapOrderHeader.setPartnerName(listQuerySalesOrderUpdateBean.get(0).getPartnerName());
			dtSapOrderHeader.setChangeMode("U");
			dtSapOrderHeader.setSalesOrderDocument(listQuerySalesOrderUpdateBean.get(0).getSalesOrderDocument());
			dtSapOrderHeader.setDeliveryBlock("");
			dtSapOrderHeader.setStatus("W");
			dtSapOrderHeader.setCreateValue(masterValue);
			dtSapOrderHeaderDao.insert(dtSapOrderHeader);
			
			//END HEADER======================
			
			// ITEM======================
			for(QuerySalesOrderTransactionUpdate el : listQuerySalesOrderUpdateBean) {
				//ITEM
				DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
				dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
				dtSapOrderItem.setChangeMode("U");
				dtSapOrderItem.setItem(el.getItem());
				dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
				dtSapOrderItem.setBillingBlock(el.getBillingBlock());
				
				dtSapOrderItem.setCreateValue(masterValue);
				dtSapOrderItemDao.insert(dtSapOrderItem);

				// ITEM serial
				DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
				dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
				dtSapOrderItemSerial.setChangeMode("U");
				dtSapOrderItemSerial.setNumberOfSerialNumber(el.getNumberofserialnumbers());
				dtSapOrderItemSerial.setIUIDCustomerRelevant(el.getIUIDCustomerRelevant());
				dtSapOrderItemSerial.setCreateValue(masterValue);
				dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
			}

			// END ITEM======================

			return dtSapOrderHeader;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		DtSapPostHeader res = sapCallApiService.callPostTransactionApi(stSapPostHeader);
		return res;
	}

	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
		try {
			DtSapTransaction dtSapTransactionForUpdate = dtSapTransactionDao
					.getByLongPrimaryKey(dtSapTransaction.getSapTranId());
			dtSapTransactionForUpdate.setStatus(dtSapTransaction.getStatus());
			dtSapTransactionForUpdate.getCreateValue().setLastUpd(new Date());
			dtSapTransactionDao.update(dtSapTransactionForUpdate);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader res = sapCallApiService.callSaleorderApi(dtSapOrderHeader);
		return res ;
	}
	
	@Override
	public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader) {
		try {
			dtSapOrderHeaderDao.update(dtSapOrderHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	@Override
	public DtSapTransaction queryDtSapTransactionById(Long sapTranId) {
		return dtSapTransactionDao.getByLongPrimaryKey(sapTranId);
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql8(String docNo){
		// query from postgres ,
//		return dtSapTransactionTypeDao.queryPostTransactionSql8(docNo);
		
		// query from on prem
		return queryPostTransactionSql8OnPrem(docNo);
	}
	
	private List<QueryPostTransactionBean> queryPostTransactionSql8OnPrem(String docNo ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql8",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }
	
	private List<QuerySalesOrderTransactionUpdate> querySalesOrderUpdate(String docNo) {

		String refDocNo = queryPrebookingReceiptNo(docNo).get(0).getREF_NO();
		String serialNumber = queryPrebookingReceiptNo(docNo).get(0).getSERIALNUMBER();
//		// query from postgres ,
//		return dtSapTransactionTypeDao.querySalesOrderTransactionCompensationDeposit(docNo);

		// query from on prem
		return dtSapTransactionTypeDao.querySalesOrderTransactionCompensationDepositOnPrem(refDocNo, serialNumber);
	}

	private List<QueryPostTransactionBean> queryPrebookingReceiptNo(String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPrebookingReceiptNo",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQueryPostTransactionBean() ;
   }

}
