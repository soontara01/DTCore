package th.co.ais.dt.core.service.core.impl.sap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.StdPriceBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.InsertSapTransactionAndQueueCloudBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.ResqueryInfoAndInsertSaleorder5859;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypePrebookNcpInfoService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeSaleNormalService;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHPartnerFDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransactionTypeDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypePrebookNcpInfoServiceImpl implements ISapHandleTransactionTypePrebookNcpInfoService {
	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao ;
//	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao ;
//	private final IDtSapPostHeaderDao dtSapPostHeaderDao ;
//	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao ;
//	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao ;
//	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao ;
//	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao ;
//	private final IDtSapPostTenderDao dtSapPostTenderDao ;
//	private final ISapCallApiService sapCallApiService ;
	private final IDtSapTransactionDao dtSapTransactionDao ;
//	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao ;
//	private final DTConfig dTConfig;
	
	
//	private final IDtSapOrderHConDao dtSapOrderHConDao ;
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderHPartnerFDao dtSapOrderHPartnerFDao;
//	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
//	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	
	private final ILovMasterDao lovMasterDao ;
	
	private final ISapHandleTransactionTypeDepositPartnerService sapHandleTransactionTypeDepositPartnerService ;
	private final ISapHandleTransactionTypeSaleNormalService sapHandleTransactionTypeSaleNormalService ;
	
	private final DTConfig dTConfig;
	
	@Override
	public ResqueryInfoAndInsertSaleorder5859 queryInfoAndInsertSaleorder58(DtSapTransaction dtSapTransaction , InsertSapTransactionAndQueueCloudBean input) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		ResqueryInfoAndInsertSaleorder5859 res = new ResqueryInfoAndInsertSaleorder5859();
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
				// query origin order type 18  BF only  and change mat only
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
				
				if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
					//query header
					List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
					
					//insert header
					//DtSapOrderHeader dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
					dtSapOrderHeaderNew.setChangeMode("U");
					dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
					dtSapOrderHeaderNew.setSalesDocType("");
					dtSapOrderHeaderNew.setSalesOrganiztion("");
					dtSapOrderHeaderNew.setDistributionChannel("");
					dtSapOrderHeaderNew.setDivision("");
					dtSapOrderHeaderNew.setShippingConditions("");
					dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
					dtSapOrderHeaderNew.setCreateValue(masterValue);
					dtSapOrderHeaderNew.setStatus("W");
					
					dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
					
					//query order  item old
					List<DtSapOrderItem> listDtSapOrderItem = dtSapOrderItemDao.queryDtSapOrderItemBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId()) ;
					DtSapOrderItem dtSapOrderItemForRollBack = new DtSapOrderItem();
					BeanUtils.copyProperties(listDtSapOrderItem.get(0), dtSapOrderItemForRollBack);
					res.setDtSapOrderItemForRollBack(dtSapOrderItemForRollBack);
					//query info sale order 3 new
					//List<QuerySaleOrderBean> listQuerySaleOrderBean = sapHandleTransactionTypeSaleNormalService.querySaleOrderSql1819(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany(), null);
					List<QuerySaleOrderBean> listQuerySaleOrderBean = new ArrayList<>();
					QuerySaleOrderBean querySaleOrderBean = new QuerySaleOrderBean();
					querySaleOrderBean.setMaterialNumber(input.getMaterialNumber()); ;
					querySaleOrderBean.setSalesUnit(input.getSalesUnit()); ;
					querySaleOrderBean.setQuantity(input.getQuantity()); ;
					//querySaleOrderBean.setDeliveryPriority(input.getDeliveryPriority()); 
					listQuerySaleOrderBean.add(querySaleOrderBean);
					
					if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
												
						String matOld = listDtSapOrderItem.get(0).getMaterialNumber() ;
						String matNew = listQuerySaleOrderBean.get(0).getMaterialNumber() ;
						
						if(!matOld.equals(matNew)) {
							// update item old
							listDtSapOrderItem.get(0).setMaterialNumber(matNew);
							listDtSapOrderItem.get(0).setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
							listDtSapOrderItem.get(0).getCreateValue().setLastUpd(date);
							listDtSapOrderItem.get(0).getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
							dtSapOrderItemDao.update(listDtSapOrderItem.get(0));
							
							// insert item new
							DtSapOrderItem dtSapOrderItemNew = new DtSapOrderItem();
							
							dtSapOrderItemNew.setChangeMode("U");
							dtSapOrderItemNew.setItem(listDtSapOrderItem.get(0).getItem());
							
							dtSapOrderItemNew.setMaterialNumber(listQuerySaleOrderBean.get(0).getMaterialNumber());
							dtSapOrderItemNew.setQuantity(listQuerySaleOrderBean.get(0).getQuantity());
							dtSapOrderItemNew.setSalesUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
							
							dtSapOrderItemNew.setPlant(listDtSapOrderItem.get(0).getPlant()); 
							dtSapOrderItemNew.setStorageLocation(listDtSapOrderItem.get(0).getStorageLocation());
							dtSapOrderItemNew.setDeliveryPriority(listDtSapOrderItem.get(0).getDeliveryPriority());
							dtSapOrderItemNew.setRequestedDeliveryDate(listDtSapOrderItem.get(0).getRequestedDeliveryDate());				
							dtSapOrderItemNew.setItemCategory(listDtSapOrderItem.get(0).getItemCategory());  
							dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
							dtSapOrderItemNew.setCreateValue(masterValue);
							dtSapOrderItemDao.insert(dtSapOrderItemNew);
							
							//query item con old
							List<DtSapOrderItemCon> listDtSapOrderItemCon =  dtSapOrderItemConDao.queryDtSapOrderItemCon(listDtSapOrderItem.get(0).getSapOrderItemId());
							
//							String priceOld = listDtSapOrderItemCon.get(0).getAmount();
//							String priceNew = "";
//							
//							//find price wh  TODO
//							//listQuerySaleOrderBean.get(0).getReceipt_dt()   YYYY-MM-DD convert to  DDMMYYYY hh24:mi
//							String pricetime = listQuerySaleOrderBean.get(0).getReceipt_dt().split("-")[2] + 
//									           listQuerySaleOrderBean.get(0).getReceipt_dt().split("-")[1] + 
//									           listQuerySaleOrderBean.get(0).getReceipt_dt().split("-")[0] + " 00:01";
//							List<StdPriceBean> price = sapHandleTransactionTypeDepositPartnerService.getListPriceByCriteria(Long.valueOf(listQuerySaleOrderBean.get(0).getProduct_id()), "EUP", pricetime, "CASH");
//
//							if(BeanUtil.isNotEmpty(price)) {
//								priceNew = price.get(0).getIncVat() ;
//							}
//							
//							if(!priceOld.equals(priceNew)) {
//								listDtSapOrderItemCon.get(0).setAmount(priceNew); 
//								listDtSapOrderItemCon.get(0).setConditionPricingUnit(listQuerySaleOrderBean.get(0).getQuantity());
//								listDtSapOrderItemCon.get(0).setConditionUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
//								listDtSapOrderItemCon.get(0).getCreateValue().setLastUpd(date);
//								listDtSapOrderItemCon.get(0).getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
//								dtSapOrderItemConDao.update(listDtSapOrderItemCon.get(0));
//							}
							
							for(DtSapOrderItemCon i :listDtSapOrderItemCon) {
								DtSapOrderItemCon dtSapOrderItemConMainPrice = new DtSapOrderItemCon() ;
								dtSapOrderItemConMainPrice.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
								dtSapOrderItemConMainPrice.setChangeMode("U");
								dtSapOrderItemConMainPrice.setConditionType(i.getConditionType());
								dtSapOrderItemConMainPrice.setAmount(i.getAmount()); // TOFDD
								dtSapOrderItemConMainPrice.setCurrency(i.getCurrency());
								dtSapOrderItemConMainPrice.setConditionPricingUnit(i.getConditionPricingUnit());
								dtSapOrderItemConMainPrice.setConditionUnit(i.getConditionUnit());
								dtSapOrderItemConMainPrice.setCreateValue(masterValue);
								dtSapOrderItemConDao.insert(dtSapOrderItemConMainPrice);
							}
							
							
							
						}
						
					}
					
				}
			
			

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		res.setDtSapOrderHeader(dtSapOrderHeaderNew);
		return res ;
	}
	
	@Override
	public ResqueryInfoAndInsertSaleorder5859 queryInfoAndInsertSaleorder59(DtSapTransaction dtSapTransaction, InsertSapTransactionAndQueueCloudBean input) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		ResqueryInfoAndInsertSaleorder5859 res = new ResqueryInfoAndInsertSaleorder5859();
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
				// query origin order type 18  
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 18L) ;
				
				if(BeanUtil.isNotEmpty(listDtSapTransaction)) {
					//query header
					List<DtSapOrderHeader> listDtSapOrderHeader =  dtSapOrderHeaderDao.queryDtSapOrderHeaderBySapTranId(listDtSapTransaction.get(0).getSapTranId()) ;
					
					//insert header
					//DtSapOrderHeader dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew = new DtSapOrderHeader();
					dtSapOrderHeaderNew.setPartnerName(listDtSapOrderHeader.get(0).getPartnerName());
					dtSapOrderHeaderNew.setChangeMode("U");
					dtSapOrderHeaderNew.setSalesOrderDocument(listDtSapOrderHeader.get(0).getRes_SalesOrderDocument());  // sap doc
					dtSapOrderHeaderNew.setSalesDocType("");
					dtSapOrderHeaderNew.setSalesOrganiztion("");
					dtSapOrderHeaderNew.setDistributionChannel("");
					dtSapOrderHeaderNew.setDivision("");
					dtSapOrderHeaderNew.setShippingConditions("");
					dtSapOrderHeaderNew.setSapTranId(dtSapTransaction.getSapTranId()); 		
					dtSapOrderHeaderNew.setCreateValue(masterValue);
					dtSapOrderHeaderNew.setStatus("W");
					
					dtSapOrderHeaderDao.insert(dtSapOrderHeaderNew);
					
					List<DtSapOrderHPartnerF> listDtSapOrderHPartnerFOld = dtSapOrderHPartnerFDao.queryDtSapOrderHPartnerFBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId());
					// listDtSapOrderHPartnerFOld size = 1 only  AG
					DtSapOrderHPartnerF dtSapOrderHPartnerForRollBack = new DtSapOrderHPartnerF();
					BeanUtils.copyProperties(listDtSapOrderHPartnerFOld.get(0), dtSapOrderHPartnerForRollBack);
					res.setDtSapOrderHPartnerForRollBack(dtSapOrderHPartnerForRollBack);
					
					// query sql1
					//List<QuerySaleOrderBean> listQuerySaleOrderBean = querySaleOrderSql1819(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() ,dtSapTransaction.getTransactionType());
					List<QuerySaleOrderBean> listQuerySaleOrderBean = new ArrayList<>();
					QuerySaleOrderBean querySaleOrderBean = new QuerySaleOrderBean();
					querySaleOrderBean.setHFSHName1(input.getHFSHName1()); ;
					querySaleOrderBean.setHFSHName2(input.getHFSHName2()); ;
					querySaleOrderBean.setHFSHName3(input.getHFSHName3()); ;
					querySaleOrderBean.setHFSHName4(input.getHFSHName4()); 
					querySaleOrderBean.setHFSHStreet(input.getHFSHStreet()); 
					querySaleOrderBean.setHFSHStreet2(input.getHFSHStreet2()); 
					querySaleOrderBean.setHFSHStreet3(input.getHFSHStreet3()); 
					querySaleOrderBean.setHFSHStreet4(input.getHFSHStreet4()); 
					querySaleOrderBean.setHFSHOtherCity(input.getHFSHOtherCity()); 
					querySaleOrderBean.setHFSHDistrict(input.getHFSHDistrict()); 
					querySaleOrderBean.setHFSHCity(input.getHFSHCity()); 
					querySaleOrderBean.setHFSHPostalCode(input.getHFSHPostalCode()); 
					querySaleOrderBean.setHFSHRegion(input.getHFSHRegion()); 
					querySaleOrderBean.setHFSHCountry(input.getHFSHCountry()); 
					querySaleOrderBean.setHFSHTelephone(input.getHFSHTelephone()); 
					querySaleOrderBean.setHFSHCustomerNumber(input.getHFSHCustomerNumber()); 
					listQuerySaleOrderBean.add(querySaleOrderBean);
					
					
					//List<LovMaster> shipToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SHIP_TO_ONETIME", "SAP_S4_SHIP_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;
					List<LovMaster> soldToOneTime = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", "SAP_S4_SOLD_TO_ONETIME", dtSapTransaction.getCompany(), null, "Y") ;

					List<LovMaster> soldToOneTimeByCh = lovMasterDao.listLovMasterByCriteria("SAP_S4_SOLD_TO_ONETIME", listDtSapOrderHeader.get(0).getCustomerPurchaseOrderType(), dtSapTransaction.getCompany(), null, "Y") ;

					String soldToOneTimeSAP = soldToOneTime.get(0).getLovVal();
					if(BeanUtil.isNotEmpty(soldToOneTimeByCh)) {
						soldToOneTimeSAP = soldToOneTimeByCh.get(0).getLovVal();
					}
					
					if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
						
						if(listQuerySaleOrderBean.get(0).getHFSHCustomerNumber().equals("ONETIME_SHIPTO")){  //  moi , prc  , ncp home
							DtSapOrderHPartnerF dtSapOrderHPartnerFAG = new DtSapOrderHPartnerF();
							dtSapOrderHPartnerFAG.setChangeMode("U");
							dtSapOrderHPartnerFAG.setPartnerFunction("AG");
							dtSapOrderHPartnerFAG.setCustomerNumber(soldToOneTimeSAP);  // one time sold to
							dtSapOrderHPartnerFAG.setName1(listQuerySaleOrderBean.get(0).getHFSHName1());
							dtSapOrderHPartnerFAG.setName2(listQuerySaleOrderBean.get(0).getHFSHName2());
							dtSapOrderHPartnerFAG.setName3(listQuerySaleOrderBean.get(0).getHFSHName3());
							dtSapOrderHPartnerFAG.setName4(listQuerySaleOrderBean.get(0).getHFSHName4());
							dtSapOrderHPartnerFAG.setStreet(listQuerySaleOrderBean.get(0).getHFSHStreet());
							dtSapOrderHPartnerFAG.setStreet2(listQuerySaleOrderBean.get(0).getHFSHStreet2());
							dtSapOrderHPartnerFAG.setStreet3(listQuerySaleOrderBean.get(0).getHFSHStreet3());
							dtSapOrderHPartnerFAG.setStreet4(listQuerySaleOrderBean.get(0).getHFSHStreet4());
							dtSapOrderHPartnerFAG.setOtherCity(listQuerySaleOrderBean.get(0).getHFSHOtherCity());
							dtSapOrderHPartnerFAG.setDistrict(listQuerySaleOrderBean.get(0).getHFSHDistrict());
							dtSapOrderHPartnerFAG.setCity(listQuerySaleOrderBean.get(0).getHFSHCity());
							dtSapOrderHPartnerFAG.setPostalCode(listQuerySaleOrderBean.get(0).getHFSHPostalCode());
							dtSapOrderHPartnerFAG.setRegion(listQuerySaleOrderBean.get(0).getHFSHRegion());
							dtSapOrderHPartnerFAG.setCountry(listQuerySaleOrderBean.get(0).getHFSHCountry());
							dtSapOrderHPartnerFAG.setTelephone(listQuerySaleOrderBean.get(0).getHFSHTelephone());
							dtSapOrderHPartnerFAG.setLanguageKey("EN");
							dtSapOrderHPartnerFAG.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
							dtSapOrderHPartnerFAG.setCreateValue(masterValue);
							dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFAG);
							
							
							listDtSapOrderHPartnerFOld.get(0).setCustomerNumber(soldToOneTimeSAP);  // one time sold to
							listDtSapOrderHPartnerFOld.get(0).setName1(listQuerySaleOrderBean.get(0).getHFSHName1());
							listDtSapOrderHPartnerFOld.get(0).setName2(listQuerySaleOrderBean.get(0).getHFSHName2());
							listDtSapOrderHPartnerFOld.get(0).setName3(listQuerySaleOrderBean.get(0).getHFSHName3());
							listDtSapOrderHPartnerFOld.get(0).setName4(listQuerySaleOrderBean.get(0).getHFSHName4());
							listDtSapOrderHPartnerFOld.get(0).setStreet(listQuerySaleOrderBean.get(0).getHFSHStreet());
							listDtSapOrderHPartnerFOld.get(0).setStreet2(listQuerySaleOrderBean.get(0).getHFSHStreet2());
							listDtSapOrderHPartnerFOld.get(0).setStreet3(listQuerySaleOrderBean.get(0).getHFSHStreet3());
							listDtSapOrderHPartnerFOld.get(0).setStreet4(listQuerySaleOrderBean.get(0).getHFSHStreet4());
							listDtSapOrderHPartnerFOld.get(0).setOtherCity(listQuerySaleOrderBean.get(0).getHFSHOtherCity());
							listDtSapOrderHPartnerFOld.get(0).setDistrict(listQuerySaleOrderBean.get(0).getHFSHDistrict());
							listDtSapOrderHPartnerFOld.get(0).setCity(listQuerySaleOrderBean.get(0).getHFSHCity());
							listDtSapOrderHPartnerFOld.get(0).setPostalCode(listQuerySaleOrderBean.get(0).getHFSHPostalCode());
							listDtSapOrderHPartnerFOld.get(0).setRegion(listQuerySaleOrderBean.get(0).getHFSHRegion());
							listDtSapOrderHPartnerFOld.get(0).setCountry(listQuerySaleOrderBean.get(0).getHFSHCountry());
							listDtSapOrderHPartnerFOld.get(0).setTelephone(listQuerySaleOrderBean.get(0).getHFSHTelephone());
							listDtSapOrderHPartnerFOld.get(0).setLanguageKey("EN");
							dtSapOrderHPartnerFDao.updateSensitiveData(listDtSapOrderHPartnerFOld.get(0));
						}
					}
				}

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		res.setDtSapOrderHeader(dtSapOrderHeaderNew);
		return res ;
	}
	
	public List<QuerySaleOrderBean> querySaleOrderSql1819(String docNo , String company,Long transactionType){
		
		List<LovMaster> config = lovMasterDao.listLovMasterByCriteria("SAP_S4_CONFIG_SQL_ONP_ONCLOUD", "SAP_S4_CONFIG_SQL_ONP_ONCLOUD", null, null, "Y") ;
    
		List<QuerySaleOrderBean> res = null ;
		if(BeanUtil.isEmpty(config)) {
			// query from postgres ,
			res=  dtSapTransactionTypeDao.querySaleOrderSql1(docNo, company);
		}else {
			// query from on prem
			res=  querySaleOrderSql1OnPrem(docNo, company);
		}
		
		return res ;
		
	}
	
	private List<QuerySaleOrderBean> querySaleOrderSql1OnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/querySaleOrderSql1",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getListQuerySaleOrderBean() ;
   }
	
	public DtSapTransaction insertSapTransaction(String docNo , String company , Long transactionType , String userId) {
    	try {
    		
    		Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(userId);
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(userId);
    		
    		DtSapTransaction dtSapTransaction = new DtSapTransaction();
    		dtSapTransaction.setCompany(company);
    		dtSapTransaction.setDocNo(docNo); 
    		dtSapTransaction.setStatus("W");
    		dtSapTransaction.setTransactionType(transactionType);
    		dtSapTransaction.setCreateValue(masterValue);    
    		dtSapTransactionDao.insert(dtSapTransaction);
    		return dtSapTransaction ;
    	}catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
    	
    	
    }
	
	public void updateDtSapOrderItem(DtSapOrderItem dtSapOrderItem) {
		try {
			dtSapOrderItemDao.update(dtSapOrderItem);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateDtSapOrderHPartnerF(DtSapOrderHPartnerF dtSapOrderHPartnerF) {
		try {
			dtSapOrderHPartnerFDao.updateSensitiveData(dtSapOrderHPartnerF);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
