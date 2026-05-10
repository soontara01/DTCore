package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeCancelDepositService;
import th.co.ais.dt.entity.sap.DtSapGoodsIssueOrder;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHReturn;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCancel;
import th.co.ais.dt.entity.sap.DtSapPostFinalcial;
import th.co.ais.dt.entity.sap.DtSapPostHeader;
import th.co.ais.dt.entity.sap.DtSapPostMappingTender;
import th.co.ais.dt.entity.sap.DtSapPostSalesItems;
import th.co.ais.dt.entity.sap.DtSapPostTaxItems;
import th.co.ais.dt.entity.sap.DtSapPostTender;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sap.DtSapTransactionType;
import th.co.ais.dt.entity.sap.DtSapTransactionTypeCodeConfig;
import th.co.ais.dt.entity.sk.LocMapPlant;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapGoodsIssueOrderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHReturnDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemCancelDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostDiscountItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostFinalcialDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostMappingTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostSalesItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTaxItemsDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapPostTenderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeCodeConfigDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySalesOrderTransactionUpdate;
import th.co.ais.dt.service.core.impl.sap.dto.QueryTenderBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeCancelDepositServiceImp implements ISapHandleTransactionTypeCancelDepositService{

	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao ;
	private final ISapCallApiService sapCallApiService ;
	private final IDtSapTransactionDao dtSapTransactionDao ;
	private final DTConfig dTConfig;
	
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
	private final IDtSapPostFinalcialDao dtSapPostFinalcialDao;
	private final IDtSapPostHeaderDao dtSapPostHeaderDao;
	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao;
	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao;
	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao;
	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao;
	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao;
	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao;
	private final IDtSapPostTenderDao dtSapPostTenderDao;
	private final IDtSapOrderHReturnDao dtSapOrderHReturnDao ;
	private final ILovMasterDao lovMasterDao ;
	private final ILocMapPlantDao locMapPlantDao ;
	private final IDtSapGoodsIssueOrderDao dtSapGoodsIssueOrderDao ;
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSalesOrderTransactionCancel(DtSapTransaction dtSapTransaction) throws DataAccessException{
		try {
			DtSapOrderHeader dtSapOrderHeader = null;
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query cancel sales order
			//List<QuerySalesOrderTransactionUpdate> listQueryPostTransactionBean = this.querySalesOrderCancel("AWN",dtSapTransaction.getDocNo(),Long.parseLong("12"));
			
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 12L) ;
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			
			
			if(orderHeaderOld != null) {
				//set values
				// HEADER======================
				dtSapOrderHeader = new DtSapOrderHeader();
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("U");
				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHeader.setStatus("W");
				
				dtSapOrderHeader.setSalesDocType("");
				dtSapOrderHeader.setSalesOrganiztion("");
				dtSapOrderHeader.setDistributionChannel("");
				dtSapOrderHeader.setDivision("");
				dtSapOrderHeader.setShippingConditions("");
				//dtSapOrderHeader.setCustomerReference(orderHeaderOld.getCustomerReference() +"C"+ String.valueOf(orderHeaderOld.getSapOrderHeaderId()));
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				//END HEADER======================
				// item 0010 only and free goods	
				// 0020 is mat deposit
				for(DtSapOrderItem el : listOrderItemOld) {	
					if(!el.getItem().equals("0020")) {
						//ITEM
						DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
						dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
						dtSapOrderItem.setChangeMode("U");
						dtSapOrderItem.setItem(el.getItem());
						dtSapOrderItem.setMaterialNumber(el.getMaterialNumber());
						
						dtSapOrderItem.setQuantity("");
						dtSapOrderItem.setSalesUnit("");
						dtSapOrderItem.setRequestedDeliveryDate("");
						dtSapOrderItem.setPlant("");
						dtSapOrderItem.setStorageLocation("");
						dtSapOrderItem.setBillingBlock("");
						dtSapOrderItem.setDeliveryPriority("");
						dtSapOrderItem.setItemCategory("");
			
						dtSapOrderItem.setCreateValue(masterValue);
						dtSapOrderItemDao.insert(dtSapOrderItem);
						
						////ITEM cancel
						DtSapOrderItemCancel dtSapOrderItemCancel = new DtSapOrderItemCancel();
						dtSapOrderItemCancel.setSapOrderItemId(dtSapOrderItem.getSapOrderItemId());
						dtSapOrderItemCancel.setChangeMode("U");
						dtSapOrderItemCancel.setReasonforRejection("70");
						
						dtSapOrderItemCancel.setCreateValue(masterValue);
						dtSapOrderItemCancelDao.insert(dtSapOrderItemCancel);
					}
						
				}
			}
				
			return dtSapOrderHeader;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSalesOrderTransactionCancelCNMemo(DtSapTransaction dtSapTransaction) throws DataAccessException{
		try {
			DtSapOrderHeader dtSapOrderHeader = null;
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query cancel sales order
			//List<QuerySalesOrderTransactionUpdate> listQueryPostTransactionBean = this.querySalesOrderCancel("AWN",dtSapTransaction.getDocNo(),Long.parseLong("12"));
			
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 12L) ;
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			List<QueryPostTransactionReqResBean>  cnLocation= getlocationCn(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
			String plantCnLocation = listOrderItemOld.get(1).getPlant();
			
			if(BeanUtil.isNotEmpty(cnLocation)) {
				if(!cnLocation.get(0).getCnLocation().equals(cnLocation.get(0).getReceiptLocation())) {
					List<LocMapPlant> locmapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(cnLocation.get(0).getCnLocation()), dtSapTransaction.getCompany());
					plantCnLocation = locmapPlant.get(0).getPk().getPlantCode() ;
				}
			}
			
			if(orderHeaderOld != null) {
				//set values
				// HEADER======================
				
				dtSapOrderHeader = new DtSapOrderHeader();
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("C");
				dtSapOrderHeader.setSDDocumentCategory("K");

				dtSapOrderHeader.setStatus("W");
				
				dtSapOrderHeader.setSalesDocType("ZC01");
				dtSapOrderHeader.setSalesOrganiztion(orderHeaderOld.getSalesOrganiztion());
				dtSapOrderHeader.setDistributionChannel(orderHeaderOld.getDistributionChannel());
				dtSapOrderHeader.setDivision(orderHeaderOld.getDivision());
				dtSapOrderHeader.setCustomerReference(orderHeaderOld.getCustomerReference());
				//dtSapOrderHeader.setCustomerReference(orderHeaderOld.getCustomerReference() +"C"+ String.valueOf(orderHeaderOld.getSapOrderHeaderId()));
				dtSapOrderHeader.setCustomerPurchaseOrderType(orderHeaderOld.getCustomerPurchaseOrderType());
				
				dtSapOrderHeader.setShippingConditions("");
				dtSapOrderHeader.setBillingBlock("/");
				
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
				
				DtSapOrderHReturn dtSapOrderHReturn = new DtSapOrderHReturn();
				dtSapOrderHReturn.setChangeMode("C");
				dtSapOrderHReturn.setOrderReason("Z55");
				dtSapOrderHReturn.setReference(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHReturn.setCreateValue(masterValue);
				dtSapOrderHReturn.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
				dtSapOrderHReturnDao.insert(dtSapOrderHReturn);
				
				//END HEADER======================
							
				//List<LovMaster> planrcnmemo = lovMasterDao.listLovMasterByCriteria("SAP_S4_PLANT_CODE_CN_MEMO", "SAP_S4_PLANT_CODE_CN_MEMO", dtSapTransaction.getCompany(), null, "Y") ;

				//for(DtSapOrderItem el : listOrderItemOld) {	 // mat deposit only
						//ITEM
						DtSapOrderItem dtSapOrderItem = new DtSapOrderItem();
						dtSapOrderItem.setSapOrderHeaderId(dtSapOrderHeader.getSapOrderHeaderId());
						dtSapOrderItem.setChangeMode("C");
						dtSapOrderItem.setItem("0020");
						dtSapOrderItem.setMaterialNumber(listOrderItemOld.get(1).getMaterialNumber());
						
						dtSapOrderItem.setQuantity(listOrderItemOld.get(1).getQuantity());
						dtSapOrderItem.setSalesUnit(listOrderItemOld.get(1).getSalesUnit());
						dtSapOrderItem.setRequestedDeliveryDate("");
						dtSapOrderItem.setItemCategory("ZRC1");
						
						
						dtSapOrderItem.setPlant(plantCnLocation); 
						dtSapOrderItem.setStorageLocation("");
						dtSapOrderItem.setBillingBlock("");
						dtSapOrderItem.setDeliveryPriority("");
						
						dtSapOrderItem.setCreateValue(masterValue);
						dtSapOrderItemDao.insert(dtSapOrderItem);
						
				//}
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
	
	@Override
	public DtSapOrderHeader callPostTransactionApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader res = sapCallApiService.callSaleorderApi(dtSapOrderHeader);
		return res ;
	}
	
	@Override
	public DtSapOrderHeader callSaleorderApi(DtSapOrderHeader dtSapOrderHeader) {
		DtSapOrderHeader res = sapCallApiService.callSaleorderApi(dtSapOrderHeader);
		return res;
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
	
	private List<QuerySalesOrderTransactionUpdate> querySalesOrderCancel(String company,String docNo ,Long transactionType){
		// query from postgres ,
		return dtSapTransactionTypeDao.querySalesOrderTransactionCancelDepositShop(company, docNo, transactionType);
		
		// query from on prem
//		return querySalesOrderCancelOnprem(docNo, company);
	}
	
//	private List<QueryPostTransactionBean> querySalesOrderCancelOnprem(String company,String docNo ,Long transactionType) {
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

	@Override
	public DtSapPostHeader queryInfoAndInsertPostTransactionDepositCancel(DtSapTransaction dtSapTransaction) throws DataAccessException {
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			//query deposit transactionType 12
			//List<DtSapTransaction> listDtSapTransaction12 = dtSapTransactionDao.getDtSapTransactionByTranSactionTypeAndDocNo(12L,dtSapTransaction.getDocNo());			
			
			// query transaction type code config 
			DtSapTransactionTypeCodeConfig config = dtSapTransactionTypeCodeConfigDao.getByLongPrimaryKey(Long.valueOf(dtSapTransaction.getTransactionType())) ;
			
			
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 12L) ;
			
			//set values
			// HEADER======================
			List<DtSapPostHeader> listDtSapPostHeaderType12 = dtSapPostHeaderDao.getDtSapPostHeaderBySapTranIdAndStatus(listDtSapOld.get(0).getSapTranId(),"S");
			
			
			List<QueryPostTransactionReqResBean>  cnLocation= getlocationCn(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany());
			
            String plantCnLocation = listDtSapPostHeaderType12.get(0).getRETAILSTOREID();
			String refNo = listDtSapPostHeaderType12.get(0).getTRANSACTIONSEQUENCENUMBER()+"_C" ;
			if(BeanUtil.isNotEmpty(cnLocation)) {
				refNo = cnLocation.get(0).getRefDocNo();
				if(!cnLocation.get(0).getCnLocation().equals(cnLocation.get(0).getReceiptLocation())) {
					List<LocMapPlant> locmapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(cnLocation.get(0).getCnLocation()), dtSapTransaction.getCompany());
					plantCnLocation = locmapPlant.get(0).getPk().getPlantCode() ;
				}
			}
			
			DtSapPostHeader dtSapPostHeader = new DtSapPostHeader();
			
			dtSapPostHeader.setRETAILSTOREID(plantCnLocation); 
			
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.US);  
			SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd",Locale.US); 


			//
			dtSapPostHeader.setBUSINESSDAYDATE(dt2.format(new Date()));  
			
			//from config
			dtSapPostHeader.setTRANSACTIONTYPECODE(config.getTransactiontypecode());  
			
			dtSapPostHeader.setWORKSTATIONID(listDtSapPostHeaderType12.get(0).getWORKSTATIONID());  
			dtSapPostHeader.setTRANSACTIONSEQUENCENUMBER(refNo);  
			dtSapPostHeader.setBEGINDATETIMESTAMP(dt.format(new Date()));  
			dtSapPostHeader.setENDDATETIMESTAMP(dt.format(new Date()));  
			dtSapPostHeader.setOPERATORID(listDtSapPostHeaderType12.get(0).getOPERATORID());  
			dtSapPostHeader.setTRANSACTIONCURRENCY(listDtSapPostHeaderType12.get(0).getTRANSACTIONCURRENCY());  
			
			List<LovMaster> soldToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", "Z004", dtSapTransaction.getCompany(), null, "Y") ;

			
			dtSapPostHeader.setPARTNERID(soldToOneTime.get(0).getLovVal());  
			dtSapPostHeader.setORIGTRANSNUMBER(listDtSapPostHeaderType12.get(0).getORIGTRANSNUMBER()); 
			dtSapPostHeader.setCreateValue(masterValue);
			
			//set sap trans id
			dtSapPostHeader.setSapTranId(dtSapTransaction.getSapTranId());
			dtSapPostHeader.setStatus("W");
			dtSapPostHeaderDao.insert(dtSapPostHeader);
			//END HEADER======================
			
			//set value SALES ITEM
			int itemNo = 1 ;
			List<DtSapPostSalesItems> listDtSapPostSalesItemsType12 = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderId(listDtSapPostHeaderType12.get(0).getSapPostHeaderId());
				for(DtSapPostSalesItems item:listDtSapPostSalesItemsType12) {
					
					DtSapPostSalesItems dtSapPostSalesItems = new DtSapPostSalesItems();
					//dtSapPostSalesItems.setRETAILSEQUENCENUMBER(item.getRETAILSEQUENCENUMBER());
					dtSapPostSalesItems.setRETAILSEQUENCENUMBER(String.valueOf(itemNo));
					dtSapPostSalesItems.setRETAILTYPECODE(config.getRetailtypecode());
					dtSapPostSalesItems.setRETAILREASONCODE(item.getRETAILREASONCODE());
					dtSapPostSalesItems.setITEMIDQUALIFIER(item.getITEMIDQUALIFIER());
					dtSapPostSalesItems.setITEMID(item.getITEMID());
					dtSapPostSalesItems.setRETAILQUANTITY(item.getRETAILQUANTITY());
					dtSapPostSalesItems.setSALESUNITOFMEASURE(item.getSALESUNITOFMEASURE());
					dtSapPostSalesItems.setSALESAMOUNT(item.getSALESAMOUNT());
					dtSapPostSalesItems.setNORMALSALESAMOUNT(item.getNORMALSALESAMOUNT());
					dtSapPostSalesItems.setPROMOTIONID(item.getPROMOTIONID());
					dtSapPostSalesItems.setBATCHID(item.getBATCHID());
					dtSapPostSalesItems.setSERIALNUMBER(item.getSERIALNUMBER());
					dtSapPostSalesItems.setACTUALUNITPRICE(item.getACTUALUNITPRICE());
					dtSapPostSalesItems.setSO_NO("");
					dtSapPostSalesItems.setITEM_NO("");
					dtSapPostSalesItems.setIUID("");
					dtSapPostSalesItems.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					
					dtSapPostSalesItems.setCreateValue(masterValue);
					dtSapPostSalesItemsDao.insert(dtSapPostSalesItems);
					itemNo = itemNo +1 ;
					
					
					List<DtSapPostTaxItems> listDtSapPostTaxItems = dtSapPostTaxItemsDao.getDtSapPostTaxItemsBySaleItemIdId(item.getSapPostSalesItemId());
					if(BeanUtil.isNotEmpty(listDtSapPostTaxItems)) {
						DtSapPostTaxItems dtSapPostTaxItemsDeposit = new DtSapPostTaxItems();
			         	
						dtSapPostTaxItemsDeposit.setTAXAMOUNT(listDtSapPostTaxItems.get(0).getTAXAMOUNT());
						dtSapPostTaxItemsDeposit.setTAXSEQUENCENUMBER("1");
						dtSapPostTaxItemsDeposit.setTAXTYPECODE(listDtSapPostTaxItems.get(0).getTAXTYPECODE());
						
						dtSapPostTaxItemsDeposit.setCreateValue(masterValue);
						dtSapPostTaxItemsDeposit.setSapPostSalesItemId(dtSapPostSalesItems.getSapPostSalesItemId());
						dtSapPostTaxItemsDao.insert(dtSapPostTaxItemsDeposit);
					}
					
					
				}		
			//END Sales item

			//set value TENDER
			List<DtSapPostTender> listTender = dtSapPostTenderDao.getDtSapPostTenderByHeaderId(listDtSapPostHeaderType12.get(0).getSapPostHeaderId());
				for(DtSapPostTender t:listTender) {
					DtSapPostTender newTender = new DtSapPostTender();
					newTender.setTENDERSEQUENCENUMBER(t.getTENDERSEQUENCENUMBER());
					newTender.setTENDERTYPECODE(t.getTENDERTYPECODE());
					newTender.setTENDERAMOUNT(t.getTENDERAMOUNT());
					newTender.setTENDERCURRENCY(t.getTENDERCURRENCY());
					newTender.setTENDERID(t.getTENDERID());
					newTender.setREFERENCEID(t.getREFERENCEID());
					newTender.setSapPostHeaderId(dtSapPostHeader.getSapPostHeaderId());
					
					newTender.setCreateValue(masterValue);
					dtSapPostTenderDao.insert(newTender);
				}
			//END TENDER
			
			return dtSapPostHeader ;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	@Override
	public DtSapPostHeader callPostTransactionApi(DtSapPostHeader stSapPostHeader) {
		DtSapPostHeader res = sapCallApiService.callPostTransactionApi(stSapPostHeader);
		return res ;
	}
	
	@Override
	public void updateDtSapPostHeader(DtSapPostHeader dtSapPostHeader) {
		try {
			dtSapPostHeaderDao.update(dtSapPostHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updatePostTransactionBeforeCallSap(DtSapPostHeader stSapPostHeader , DtSapOrderHeader dtSapOrderHeader) {
		List<DtSapPostSalesItems> listDtSapPostSalesItems = dtSapPostSalesItemsDao.getDtSapPostSalesItemsByHeaderIdForUpdate(stSapPostHeader.getSapPostHeaderId())  ;
		for(DtSapPostSalesItems i : listDtSapPostSalesItems) {  // size = 1 only
			i.setSO_NO(dtSapOrderHeader.getRes_SalesOrderDocument());
			i.setITEM_NO("0020"); 
			dtSapPostSalesItemsDao.update(i);
		}
	}
	
	private List<QueryPostTransactionReqResBean> getlocationCn(String receiptNo , String company){
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
		 if(BeanUtil.isEmpty(config)) {
			// postgres
				return dtSapOrderHeaderDao.getLocationCn(receiptNo, company);
		 }  
		  else { 
			// query from on prem
				return getLocationCnOnPrem(receiptNo, company) ;
		 }
		
		
		
		
	}
	
	private List<QueryPostTransactionReqResBean> getLocationCnOnPrem(String receiptNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(receiptNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/get-location-cn",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		
		List<QueryPostTransactionReqResBean> ress = null ;
		if(res != null && BeanUtil.isNotEmpty(res.getCnLocation())) {
			ress = new ArrayList<>();
			QueryPostTransactionReqResBean el = new QueryPostTransactionReqResBean();
			el.setCnLocation(res.getCnLocation());
			el.setReceiptLocation(res.getReceiptLocation());
			el.setRefDocNo(res.getRefDocNo());
			ress.add(el);
		}
		return ress ;
   }
	
	@Override
	public DtSapOrderHeader unLockOrderBeforeUpdate17(DtSapTransaction dtSapTransaction) throws DataAccessException{
		try {
			DtSapOrderHeader dtSapOrderHeader = null;
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			// query cancel sales order
			//List<QuerySalesOrderTransactionUpdate> listQueryPostTransactionBean = this.querySalesOrderCancel("AWN",dtSapTransaction.getDocNo(),Long.parseLong("12"));
			
			List<DtSapTransaction> listDtSapOld =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 12L) ;
			DtSapOrderHeader orderHeaderOld = dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapOld.get(0).getSapTranId()).get(0);
			//List<DtSapOrderItem> listOrderItemOld = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(orderHeaderOld.getSapOrderHeaderId());
			
			List<DtSapGoodsIssueOrder> listDtSapGoodsIssueOrder = dtSapGoodsIssueOrderDao.queryDtSapGoodsIssueOrder(orderHeaderOld.getCustomerReference());
			
			if(orderHeaderOld != null && BeanUtil.isNotEmpty(listDtSapGoodsIssueOrder)) {
				//set values
				// HEADER======================
				dtSapOrderHeader = new DtSapOrderHeader();
				dtSapOrderHeader.setSapTranId(dtSapTransaction.getSapTranId());
				dtSapOrderHeader.setPartnerName("DT");
				dtSapOrderHeader.setChangeMode("U");
				dtSapOrderHeader.setSalesOrderDocument(orderHeaderOld.getRes_SalesOrderDocument());
				dtSapOrderHeader.setStatus("W");
				
				dtSapOrderHeader.setYourReference("/");
				dtSapOrderHeader.setDeliveryBlock("");
				dtSapOrderHeader.setSalesDocType("");
				dtSapOrderHeader.setSalesOrganiztion("");
				dtSapOrderHeader.setDistributionChannel("");
				dtSapOrderHeader.setDivision("");
				dtSapOrderHeader.setShippingConditions("");
				//dtSapOrderHeader.setCustomerReference(orderHeaderOld.getCustomerReference() +"C"+ String.valueOf(orderHeaderOld.getSapOrderHeaderId()));
				dtSapOrderHeader.setCreateValue(masterValue);
				dtSapOrderHeaderDao.insert(dtSapOrderHeader);
		
			}
				
			return dtSapOrderHeader;
		}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}

}
