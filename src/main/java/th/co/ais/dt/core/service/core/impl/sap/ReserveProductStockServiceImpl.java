package th.co.ais.dt.core.service.core.impl.sap;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.ReserveProductBean;
import th.co.ais.dt.controller.dto.ReserveProductStockSapBean;
import th.co.ais.dt.controller.dto.ReserveShoppingCartItem;
import th.co.ais.dt.controller.dto.ReserveStockItem;
import th.co.ais.dt.core.service.core.interfaces.sap.IReserveProductStockService;
import th.co.ais.dt.entity.sap.DtSapReserveLog;
import th.co.ais.dt.entity.sk.LocMapPlant;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.ProductMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapReserveLogDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.IProductMstDao;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReserveProductStockServiceImpl implements IReserveProductStockService {
	
	private final DTConfig dTConfig;
	private final ILocMapPlantDao locMapPlantDao;
	private final ILovMasterDao lovMasterDao;
	private final IProductMstDao productMstDao;
	private final IDtSapReserveLogDao dtSapReserveLogDao;
	
	@Override
	public ReserveProductStockSapBean callServiceReserve(ReserveProductBean input) {
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		ReserveProductStockSapBean out = null;
		try {
			String todayAsString = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss",Locale.US).format(new Date());
        	List<LovMaster> apikeyqueryStock = lovMasterDao.listLovMasterByCriteria("SAP_S4_API_QUERY_STOCK_KEY", "SAP_S4_API_QUERY_STOCK_KEY",
					null, null, "Y");
			HttpClientUtilDT c = new HttpClientUtilDT();
        	Map<String, String> properties = new HashMap<String, String>();
	        properties.put("Content-Type", "application/json");
			properties.put("Ocp-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovVal());
			properties.put("x-api-key", apikeyqueryStock.get(0).getLovVal());
			properties.put("Bff-Apim-Subscription-Key", apikeyqueryStock.get(0).getLovAttribute01());
			properties.put("x-request-id", "DT-"+todayAsString);
			properties.put("x-transaction-id", "DT-"+todayAsString);
			properties.put("x-session-id", "DT-"+todayAsString);
		    
			ReserveProductStockSapBean request = this.mapRequestData(input.getReserveProductItems());
			log.info("REQUEST reserveProductStock ============> ",request.toString());
			List<LovMaster> listLovRes = lovMasterDao.listLovMasterByCriteria("MOCKUP_DATA", "RESERVE", "RESERVE_RESPONSE", "RESERVE_RESPONSE", "Y");
			String outPut = null;
			if(BeanUtil.isNotEmpty(listLovRes) && listLovRes.size()>0) {
				outPut = listLovRes.get(0).getLovAttribute01();
				out = gson.fromJson(outPut, ReserveProductStockSapBean.class);
			} else {
			    outPut = c.HttpClient(properties, dTConfig.getUrl().getSapReserve(),gson.toJson(request) , "POST", null);
			    out = gson.fromJson(outPut, ReserveProductStockSapBean.class);
			}
			// insert sapReserveLog
			DtSapReserveLog sapReserveLog = new DtSapReserveLog();
			sapReserveLog.setParaInput(gson.toJson(request));
			sapReserveLog.setResponse(outPut);
			if(BeanUtil.isNull(out.getMessage())) {
				Map<String, String> shoppingCart = (Map<String, String>) out.getShoppingCart();
				sapReserveLog.setShoppingcartid(shoppingCart.get("id"));
			}
			sapReserveLog.setXtransactionid(properties.get("x-transaction-id"));
			MasterValue createValue = new MasterValue();
			createValue.setCreatedBy("DTAPP");
			createValue.setCreated(new Date());
			createValue.setLastUpdBy("DTAPP");
			createValue.setLastUpd(new Date());
			sapReserveLog.setCreateValue(createValue);
			
			insertDtSapReserveLog(sapReserveLog);
			
		    return out;
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error callServiceReserve : "+e.getMessage());
		}
		return out;
	}
	
	private ReserveProductStockSapBean mapRequestData(List<ReserveProductBean> input){
		ReserveProductStockSapBean requestData = null;
		try {
			Date sysDate = new Date();
			requestData = new ReserveProductStockSapBean();
			requestData.setCreationDate(sysDate);
			requestData.setRequiredAvailabilityDate(sysDate);
			
			Map<String,Object> locationDest = new HashMap<String, Object>();
			List<LocMapPlant> locMapPlant = locMapPlantDao.getLocMapPlantByLocationCompany(Long.parseLong(input.get(0).getUserLocCode()), input.get(0).getCompany());
			String plantCode = "";
			if(!locMapPlant.isEmpty() && locMapPlant.size()>0) {
				plantCode = locMapPlant.get(0).getPk().getPlantCode();
			}
			locationDest.put("companyCode", input.get(0).getCompany());
			locationDest.put("valueType", "plantId");
			locationDest.put("id", plantCode);
			requestData.setLocationDest(locationDest);
			
			Map<String,Object> locationSource = new HashMap<String, Object>();
			locationSource.put("soChannelType", "CSP");
			locationSource.put("id", input.get(0).getUserLocCode());
			requestData.setLocationSource(locationSource);
			
			//reserveShoppingCartItem
			List<ReserveShoppingCartItem> listReserveShoppingCartItem = new ArrayList<ReserveShoppingCartItem>();
			for(ReserveProductBean i:input) {
				ReserveShoppingCartItem reserveShoppingCartItem = new ReserveShoppingCartItem();
				List<ReserveStockItem> listReserveStockItem = new ArrayList<ReserveStockItem>();
				ReserveStockItem reserveStockItem = new ReserveStockItem();
				reserveStockItem.setReserveItemType("");
				Map<String,Object> quantityRequested = new HashMap<String, Object>();
				quantityRequested.put("amount", Long.parseLong(i.getQty()));
				List<ProductMst> listProductMst = productMstDao.getProductMstByUnique(i.getCompany(), i.getMatCode());
				String units = "";
				if(BeanUtil.isNotEmpty(listProductMst)&&listProductMst.size()>0) {
					units = listProductMst.get(0).getUnitName();
				}
				quantityRequested.put("units", units);
				reserveStockItem.setQuantityRequested(quantityRequested);
				
				Map<String,Object> stockRequested = new HashMap<String, Object>();
				stockRequested.put("@type", "Stock");
				Map<String,Object> stockItem = new HashMap<String, Object>();
				stockItem.put("id", getFormatMatcode(i.getMatCode()));
				stockRequested.put("stockItem", stockItem);
				reserveStockItem.setStockRequested(stockRequested);
				
				listReserveStockItem.add(reserveStockItem);
				reserveShoppingCartItem.setReserveStockItem(listReserveStockItem);
				listReserveShoppingCartItem.add(reserveShoppingCartItem);
				
			}
			requestData.setReserveShoppingCartItem(listReserveShoppingCartItem);
			
			requestData.setReserveStockState("accepted");
			
			//relatedEntity
			List<Object> listRelatedEntity = new ArrayList<Object>();
			Map<String,Object> relatedEntity = new HashMap<String, Object>();
			relatedEntity.put("role", "order number");
			relatedEntity.put("@type", "RelatedEntityRefOrValue");
			Map<String,Object> entity = new HashMap<String, Object>();
			entity.put("@type", "ProductOrder");
			entity.put("id", "SALE-DT0000011111111");
			relatedEntity.put("entity", entity);
			listRelatedEntity.add(relatedEntity);
			requestData.setRelatedEntity(listRelatedEntity);
			
			//relatedParty
			List<Object> listRelatedParty = new ArrayList<Object>();
			Map<String,Object> relatedParty = new HashMap<String, Object>();
			Map<String,Object> partyOrPartyRole = new HashMap<String, Object>();
			partyOrPartyRole.put("@type", "PartyRole");
			partyOrPartyRole.put("id", input.get(0).getUserId());
			relatedParty.put("partyOrPartyRole", partyOrPartyRole);
			relatedParty.put("role", "employee");
			relatedParty.put("@type", "RelatedPartyRefOrPartyRoleRef");
			listRelatedParty.add(relatedParty);
			requestData.setRelatedParty(listRelatedParty);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error mapRequestData : "+e.getMessage());
		}
		return requestData;
	}
	
	@Override
	public void insertDtSapReserveLog(DtSapReserveLog sapReserveLog) throws DataAccessException {
		try {
			dtSapReserveLogDao.insert(sapReserveLog);
		} catch (Exception e) {
			log.error("Error insertDtSapReserveLog : "+e.getMessage());
		}
	}
	
	private String getFormatMatcode(String matCode) {
		try {
			Long.parseLong(matCode);
			while(matCode.length()!=18) {
				matCode = "0"+matCode;
			}
			return matCode;
		} catch (Exception e) {
			// TODO: handle exception
			return matCode;
		}
	}
}
