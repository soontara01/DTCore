package th.co.ais.dt.core.service.core.impl.sap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.StdPriceBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerChangeInfoService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionTypeDepositPartnerService;
import th.co.ais.dt.entity.sap.DtSapOrderHPartnerF;
import th.co.ais.dt.entity.sap.DtSapOrderHeader;
import th.co.ais.dt.entity.sap.DtSapOrderItem;
import th.co.ais.dt.entity.sap.DtSapOrderItemCon;
import th.co.ais.dt.entity.sap.DtSapOrderItemSerial;
import th.co.ais.dt.entity.sap.DtSapTransaction;
import th.co.ais.dt.entity.sk.LocMapPlant;
import th.co.ais.dt.entity.util.CmLocationMst;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.pr.IPriceMstDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHPartnerFDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHTextDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemCancelDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemConDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapOrderItemSerialDao;
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
import th.co.ais.dt.repository.interfaces.util.ICmLocationMstDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QuerySaleOrderBean;
import th.co.ais.dt.util.BeanUtil;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionTypeDepositPartnerChangeInfoServiceImpl implements ISapHandleTransactionTypeDepositPartnerChangeInfoService {
//	private final IDtSapTransactionTypeDao dtSapTransactionTypeDao ;
//	private final IDtSapTransactionTypeCodeConfigDao dtSapTransactionTypeCodeConfigDao ;
//	private final IDtSapPostHeaderDao dtSapPostHeaderDao ;
//	private final IDtSapPostSalesItemsDao dtSapPostSalesItemsDao ;
//	private final IDtSapPostDiscountItemsDao dtSapPostDiscountItemsDao ;
//	private final IDtSapPostTaxItemsDao dtSapPostTaxItemsDao ;
//	private final IDtSapPostDiscountHeaderDao dtSapPostDiscountHeaderDao ;
//	private final IDtSapPostTenderDao dtSapPostTenderDao ;
//	private final ISapCallApiService sapCallApiService ;
//	private final IDtSapTransactionDao dtSapTransactionDao ;
//	private final IDtSapPostMappingTenderDao dtSapPostMappingTenderDao ;
//	private final DTConfig dTConfig;
	
	
//	private final IDtSapOrderHConDao dtSapOrderHConDao ;
	private final IDtSapOrderHeaderDao dtSapOrderHeaderDao;
	private final IDtSapOrderHPartnerFDao dtSapOrderHPartnerFDao;
//	private final IDtSapOrderItemCancelDao dtSapOrderItemCancelDao;
	private final IDtSapOrderItemConDao dtSapOrderItemConDao;
	private final IDtSapOrderItemDao dtSapOrderItemDao;
//	private final IDtSapOrderItemSerialDao dtSapOrderItemSerialDao;
	
//	private final ILovMasterDao lovMasterDao ;
	private final ILocMapPlantDao locMapPlantDao ;
	private final ICmLocationMstDao cmLocationMstDao ;
	
	private final ISapHandleTransactionTypeDepositPartnerService sapHandleTransactionTypeDepositPartnerService ;
	
	@Override
	public DtSapOrderHeader queryInfoAndInsertSaleorder57(DtSapTransaction dtSapTransaction) {
		DtSapOrderHeader dtSapOrderHeaderNew = null ;
		try {
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapTransaction.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
			
			
				// query origin order type 40
				List<DtSapTransaction> listDtSapTransaction =  dtSapOrderHeaderDao.queryOriginOrder(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany() , 40L) ;
				
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
					
					//query order  function old
					List<DtSapOrderHPartnerF> listDtSapOrderHPartnerF = dtSapOrderHPartnerFDao.queryDtSapOrderHPartnerFBySapOrderHeaderId(listDtSapOrderHeader.get(0).getSapOrderHeaderId());
					
					//query info sale order 3 new
					List<QuerySaleOrderBean> listQuerySaleOrderBean = sapHandleTransactionTypeDepositPartnerService.querySaleOrderSq3(dtSapTransaction.getDocNo(), dtSapTransaction.getCompany(), null);
					
					
					if(BeanUtil.isNotEmpty(listQuerySaleOrderBean)) {
						
						String ShipToOld = "";
						DtSapOrderHPartnerF dtSapOrderHPartnerFOld = null ;
						for(DtSapOrderHPartnerF el : listDtSapOrderHPartnerF) {
							if(el.getPartnerFunction().equals("WE")) {
								ShipToOld = el.getCustomerNumber();
								dtSapOrderHPartnerFOld = el ;
							}
						}
						
						//String shiptoNew = listQuerySaleOrderBean.get(0).getHFSHCustomerNumber();
						//listQuerySaleOrderBean.get(0).getHFSCustomerNumber();
						
						//List<LocMapPlant> listlocMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSCustomerNumber()), dtSapTransaction.getCompany()) ;
						//String plantNew  = listlocMapPlant.get(0).getPk().getPlantCode();
						CmLocationMst cm = cmLocationMstDao.getLocationMstByKey(Long.valueOf(listQuerySaleOrderBean.get(0).getHFSCustomerNumber()));
						String shipToNew = cm.getAwn_code() ;
						
						if(!shipToNew.equals(ShipToOld)) {
							// update partner old 
							dtSapOrderHPartnerFOld.setCustomerNumber(shipToNew);
							dtSapOrderHPartnerFOld.getCreateValue().setLastUpd(date);
							dtSapOrderHPartnerFOld.getCreateValue().setLastUpdBy(dtSapTransaction.getCreateValue().getLastUpdBy());
							dtSapOrderHPartnerFDao.update(dtSapOrderHPartnerFOld);
							
							// insert partner new
							DtSapOrderHPartnerF dtSapOrderHPartnerFWE = new DtSapOrderHPartnerF();
							dtSapOrderHPartnerFWE.setChangeMode("U");
							dtSapOrderHPartnerFWE.setPartnerFunction("WE");
							dtSapOrderHPartnerFWE.setCustomerNumber(shipToNew);  
							dtSapOrderHPartnerFWE.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
							dtSapOrderHPartnerFWE.setCreateValue(masterValue);
							dtSapOrderHPartnerFDao.insertSensitiveData(dtSapOrderHPartnerFWE);
						}
						
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
							dtSapOrderItemNew.setDeliveryPriority(listQuerySaleOrderBean.get(0).getDeliveryPriority());
													
							dtSapOrderItemNew.setItemCategory(listDtSapOrderItem.get(0).getItemCategory());  
							dtSapOrderItemNew.setSapOrderHeaderId(dtSapOrderHeaderNew.getSapOrderHeaderId());
							dtSapOrderItemNew.setCreateValue(masterValue);
							dtSapOrderItemDao.insert(dtSapOrderItemNew);
							
//							//query item con old
//							List<DtSapOrderItemCon> listDtSapOrderItemCon =  dtSapOrderItemConDao.queryDtSapOrderItemCon(listDtSapOrderItem.get(0).getSapOrderItemId());
//							
//							String priceOld = listDtSapOrderItemCon.get(0).getAmount();
//							String priceNew = "";
//							
//							//find price wh  TODO
//							List<StdPriceBean> price = sapHandleTransactionTypeDepositPartnerService.getListPriceByCriteria(Long.valueOf(listQuerySaleOrderBean.get(0).getProduct_id()), "EUP", listQuerySaleOrderBean.get(0).getReceipt_dt(), "CASH");
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
//							
//							DtSapOrderItemCon dtSapOrderItemConMainPrice = new DtSapOrderItemCon() ;
//							dtSapOrderItemConMainPrice.setSapOrderItemId(dtSapOrderItemNew.getSapOrderItemId());
//							dtSapOrderItemConMainPrice.setChangeMode("U");
//							dtSapOrderItemConMainPrice.setConditionType("PN10");
//							dtSapOrderItemConMainPrice.setAmount(priceNew); // TOFDD
//							dtSapOrderItemConMainPrice.setCurrency("THB");
//							dtSapOrderItemConMainPrice.setConditionPricingUnit(listQuerySaleOrderBean.get(0).getQuantity());
//							dtSapOrderItemConMainPrice.setConditionUnit(listQuerySaleOrderBean.get(0).getSalesUnit());
//							dtSapOrderItemConMainPrice.setCreateValue(masterValue);
//							dtSapOrderItemConDao.insert(dtSapOrderItemConMainPrice);
							
							
						}
						
					}
					
				}
			
			

			
		}catch (Exception e) {
			e.printStackTrace();
			//throw new ForceTerminateException(0, null) ;
		}
		
		return dtSapOrderHeaderNew ;
	}
}
