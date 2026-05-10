package th.co.ais.dt.core.service.core.impl.sap;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeMonitorAndApproveService;
import th.co.ais.dt.entity.cm.CompensationPartner;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapOrderItemSerial;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.cm.ICompensationPartnerDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemSerialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySalesOrderTransactionUpdate;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeMonitorAndApproveServiceImp implements ISapHandleTransactionTypeMonitorAndApproveService {
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao ;
	private final ISapCallApiService sapCallApiService ;
	private final IDtSapTransactionDao dtSapTransactionDao ;
	private final DTConfig dTConfig;
	
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
//	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	
	private final ICompensationPartnerDao compensationPartnerDao;
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSalesOrderTransactionUpdate(DtSapTransaction dtSapTransaction) throws DataAccessException{
		try {

			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			CompensationPartner compensationPartner = this.queryCompensationPartner(dtSapTransaction.getDocNo());

			// query update sales order
			List<QuerySalesOrderTransactionUpdate> listQueryPostTransactionBean = this.querySalesOrderUpdate(compensationPartner.getPrebookingReceiptNo());
			
			//set values
			// HEADER======================
			DtSapOrderHeader dtSapOrderHeader = new DtSapOrderHeader();
			dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapOrderHeader.setPartnerName(listQueryPostTransactionBean.get(0).getPartnerName());
			dtSapOrderHeader.setChangeMode("U");
			dtSapOrderHeader.setSalesOrderDocument(listQueryPostTransactionBean.get(0).getSalesOrderDocument());
			dtSapOrderHeader.setDeliveryBlock("");
			dtSapOrderHeader.setStatus("W");
			dtSapOrderHeader.setCreateValue(masterValue);
			dtSapOrderHeaderDao.insert(dtSapOrderHeader);
			
			//END HEADER======================
						
			for(QuerySalesOrderTransactionUpdate el : listQueryPostTransactionBean) {	
				if(BeanUtil.isNotEmpty(el.getItem())&& "0030".equals(el.getItem())) {
					//ITEM
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setChangeMode("U");
					dtSapOrderItem.setItem(el.getItem());
					dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
					dtSapOrderItem.setQuantity(el.getQuantity());
					dtSapOrderItem.setSalesUnit(el.getSalesUnit());
					dtSapOrderItem.setItemCategory(el.getItemcategory());
					dtSapOrderItem.setPlant(el.getPlant());

					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);
					
					////ITEM con
					DtSapOrderItemCon dtSapOrderItemCon = new DtSapOrderItemCon();
					dtSapOrderItemCon.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
					dtSapOrderItemCon.setChangeMode("U");
					dtSapOrderItemCon.setConditionType(el.getConditionType());
					dtSapOrderItemCon.setAmount(el.getAmount());
					dtSapOrderItemCon.setCurrency("THB");
					dtSapOrderItemCon.setCreateValue(masterValue);				
					dtSapOrderItemConDao.insert(dtSapOrderItemCon);
				}
				else {
					//ITEM
					DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
					dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
					dtSapOrderItem.setChangeMode("U");
					dtSapOrderItem.setItem(el.getItem());
					dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
					dtSapOrderItem.setBillingBlock(el.getBillingBlock());
					
					dtSapOrderItem.setCreateValue(masterValue);
					dtSapOrderItemDao.insert(dtSapOrderItem);

					//ITEM serial
					DtSapOrderItemSerial dtSapOrderItemSerial = new DtSapOrderItemSerial();
					dtSapOrderItemSerial.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
					dtSapOrderItemSerial.setChangeMode("U");
//					dtSapOrderItemSerial.setNumberOfSerialNumbers(el.getNumberofserialnumbers());
					dtSapOrderItemSerial.setNumberOfSerialNumber(compensationPartner.getImei());
					dtSapOrderItemSerial.setIUIDCustomerRelevant(el.getIUIDCustomerRelevant());
					dtSapOrderItemSerial.setCreateValue(masterValue);
					dtSapOrderItemSerialDao.insert(dtSapOrderItemSerial);
				}
			}
				
			return dtSapOrderHeader;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public DtSapTransactionType queryDtSapTransactionTypeByKey(Long TransactionType) {
		return dtSapTransactionTypeDao.getByLongPrimaryKey(TransactionType);
	}
	
	public DtSapOrderHeader callPostTransactionApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader res = sapCallApiService.callSaleorderApi(dtSapOrderHeader);
		return res ;
	}

	@Override
	public void updateDtSapTransactionAfterWorkflow(DtSapTransaction dtSapTransaction) {
		try {
			DtSapTransaction dtSapTransactionForUpdate = dtSapTransactionDao.getByLongPrimaryKey(dtSapTransaction.getSapTranId());
			dtSapTransactionForUpdate.setStatus(dtSapTransaction.getStatus());
			dtSapTransactionForUpdate.getCreateValue().setLastUpd(new Date());
			dtSapTransactionDao.update(dtSapTransactionForUpdate);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void updateDtSapOrderHeader(DtSapOrderHeader dtSapOrderHeader) {
		try {
			dtSapOrderHeaderDao.update(dtSapOrderHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	private List<QuerySalesOrderTransactionUpdate> querySalesOrderUpdate(String docNo){
		// query from postgres ,
		return dtSapTransactionTypeDao.querySalesOrderTransactionUpdate(docNo);
		
		// query from on prem
//		return queryPostTransactionSql4OnPrem(docNo, company);
	}
	
//	private List<QueryPostTransactionBean> queryPostTransactionSql4OnPrem(String docNo , String company ) {
//
//		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
//		HttpClientUtilDT c = new HttpClientUtilDT();
//		Map<String, String> properties = new HashMap<String, String>();
//        properties.put("Content-Type", "application/json");
//        
//        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
//        in.setDocNo(docNo);
//        in.setCompany(company);
//        
//		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql4",gson.toJson(in) , "POST", null) ;
//		Gson gsonRes = new Gson();
//		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
//		return res.getListQueryPostTransactionBean() ;
//   }
	
	private CompensationPartner queryCompensationPartner(String docNo){
		// query from postgres ,
		return compensationPartnerDao.getByStringPrimaryKeySensitiveData(docNo);
		
		// query from on prem
//		return queryPostTransactionSql4OnPrem(docNo, company);
	}
	
	private CompensationPartner queryCompensationPartnerOnPrem(String docNo) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryPostTransactionSql4",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		CompensationPartner res = gsonRes.fromJson(out, CompensationPartner.class);
		return res;
   }

}
