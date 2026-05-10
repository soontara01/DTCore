package th.co.ais.dt.core.service.core.impl.sap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.controller.dto.QuerySapContractTransLogBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionContractPickingService;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualItem;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.entity.sap.DtSapUniversalItem;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalItemDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryContractUniversalBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleTransactionContractPickingServiceImpl implements ISapHandleTransactionContractPickingService {
	
	IDtSapContractTransDao dtSapContractTransDao;
	IDtSapContractTypeDao dtSapContractTypeDao;
	IDtSapContractTransLogDao dtSapContractTransLogDao;
	IDtSapAccrualHeaderDao dtSapAccrualHeaderDao;
	IDtSapAccrualItemDao dtSapAccrualItemDao;
	IDtSapUniversalHeaderDao dtSapUniversalHeaderDao;
	IDtSapUniversalItemDao dtSapUniversalItemDao;
	ILocMapPlantDao locMapPlantDao;
	private final ISapCallApiService sapCallApiService;
	private final DTConfig dTConfig;

	@Override
	public DtSapAccrualHeader postAccrualPicking(DtSapContractTrans sapContractTrans) throws DataAccessException {
		DtSapAccrualHeader dtSapAccrualHeader = null;
		try {
			//query data from sql
//			docNo="T428924090000022";
			List<QueryContractAccrualBean> listAccrualReceipt = queryContactAccrualPingking(sapContractTrans.getDocNo(), sapContractTrans.getCompany());
			if(BeanUtil.isNotEmpty(listAccrualReceipt) && listAccrualReceipt.size()>0) {
				Date date = new Date();
				MasterValue masterValue = new MasterValue();
				masterValue.setCreated(date);
				masterValue.setCreatedBy(sapContractTrans.getCreateValue().getCreatedBy());
				masterValue.setLastUpd(date);
				masterValue.setLastUpdBy(sapContractTrans.getCreateValue().getLastUpdBy());
				//hearder
				dtSapAccrualHeader = new DtSapAccrualHeader();
				dtSapAccrualHeader.setSapContractId(sapContractTrans.getSapContractId());
				dtSapAccrualHeader.setPartnername(listAccrualReceipt.get(0).getPartnername());
				dtSapAccrualHeader.setPartnermessageid(listAccrualReceipt.get(0).getPartnermessageid());
				dtSapAccrualHeader.setCompanycode(listAccrualReceipt.get(0).getCompanycode());
				dtSapAccrualHeader.setAccrualobjectcategory(listAccrualReceipt.get(0).getAccrualobjectcategory());
				dtSapAccrualHeader.setAccrualobjectsubcategory(listAccrualReceipt.get(0).getAccrualobjectsubcategory());
				dtSapAccrualHeader.setCreateValue(masterValue);
				dtSapAccrualHeader.setStatus("W");
				dtSapAccrualHeader.setResMessagetype(null);
				dtSapAccrualHeader.setResAccrualObjectNumber(null); 
				
				dtSapAccrualHeaderDao.insert(dtSapAccrualHeader);
				//item
				List<DtSapAccrualItem> listDtSapAccrualItem = new ArrayList<DtSapAccrualItem>();
				for(QueryContractAccrualBean accrualReceipt:listAccrualReceipt) {
					DtSapAccrualItem dtSapAccrualItem = new DtSapAccrualItem();
					dtSapAccrualItem.setSapAccrualHeaderId(dtSapAccrualHeader.getSapAccrualHeaderId());
					dtSapAccrualItem.setPersonResponsible(accrualReceipt.getPersonresponsible());
					dtSapAccrualItem.setText(accrualReceipt.getText());
					dtSapAccrualItem.setStartofLife(accrualReceipt.getStartoflife());
					dtSapAccrualItem.setEndofLife(accrualReceipt.getEndoflife());
					dtSapAccrualItem.setAccrualItemType(accrualReceipt.getAccrualitemtype());
					dtSapAccrualItem.setAccrualDeferralMethod(accrualReceipt.getAccrualdeferralmethod());
					dtSapAccrualItem.setTotalAccrAmountinTransCrcy(accrualReceipt.getTotalaccramountintranscrcy());
					dtSapAccrualItem.setTransactionCurrency(accrualReceipt.getTransactioncurrency());
					dtSapAccrualItem.setProfitCenter(accrualReceipt.getProfitcenter());
					dtSapAccrualItem.setSegment(accrualReceipt.getSegment());
					dtSapAccrualItem.setDocumentNumber(accrualReceipt.getDocumentnumber());
					dtSapAccrualItem.setIMEInumber(accrualReceipt.getImeinumber());
					dtSapAccrualItem.setMaterialDescription(accrualReceipt.getMaterialdescription());
					dtSapAccrualItem.setMobilePhoneNumber(accrualReceipt.getMobilephonenumber());
					dtSapAccrualItem.setReserveField(accrualReceipt.getReservefield());
					dtSapAccrualItem.setUSSDCode(accrualReceipt.getUssdcode());
					dtSapAccrualItem.setProductnumber(accrualReceipt.getProductnumber());
					dtSapAccrualItem.setPlant(accrualReceipt.getPlant());
					dtSapAccrualItem.setProfitCenterCOPA(accrualReceipt.getProfitcentercopa());
					dtSapAccrualItem.setProjectUSSDCode(accrualReceipt.getProjectussdcode());
					dtSapAccrualItem.setSalesOrganization(accrualReceipt.getSalesorganization());
					dtSapAccrualItem.setDistributionChannel(accrualReceipt.getDistributionchannel());
					dtSapAccrualItem.setDivision(accrualReceipt.getDivision());
					dtSapAccrualItem.setCreateValue(masterValue);
					
					listDtSapAccrualItem.add(dtSapAccrualItem);
				}
				dtSapAccrualItemDao.insertList(listDtSapAccrualItem);
			}
			
		return dtSapAccrualHeader;
			
		} catch (Exception e) {
			return null;
			
		}
	}

	@Override
	public DtSapUniversalHeader postUniversalPicking(DtSapContractTrans sapContractTrans) throws DataAccessException {
		DtSapUniversalHeader dtSapUniversalHeader = null;
		try {
			
			List<QueryContractUniversalBean> listContractUniversaPicking = queryContractUniversalPicking(sapContractTrans.getCompany(), sapContractTrans.getDocNo());
			if(BeanUtil.isNotEmpty(listContractUniversaPicking) && listContractUniversaPicking.size()>0) {
				Date date = new Date();
				MasterValue masterValue = new MasterValue();
				masterValue.setCreated(date);
				masterValue.setCreatedBy(sapContractTrans.getCreateValue().getCreatedBy());
				masterValue.setLastUpd(date);
				masterValue.setLastUpdBy(sapContractTrans.getCreateValue().getLastUpdBy());
				//header
				dtSapUniversalHeader = new DtSapUniversalHeader();
				dtSapUniversalHeader.setSapContractId(sapContractTrans.getSapContractId());
				dtSapUniversalHeader.setDocNo(listContractUniversaPicking.get(0).getDocno());
				dtSapUniversalHeader.setCompanycode(listContractUniversaPicking.get(0).getCompanycode());
				dtSapUniversalHeader.setDocumentdate(listContractUniversaPicking.get(0).getDocumentdate());
				dtSapUniversalHeader.setPostingdate(listContractUniversaPicking.get(0).getPostingdate());
				dtSapUniversalHeader.setDocType(listContractUniversaPicking.get(0).getDoctype());
				dtSapUniversalHeader.setFiscalperiod(listContractUniversaPicking.get(0).getFiscalperiod());
				dtSapUniversalHeader.setCurrencykey(listContractUniversaPicking.get(0).getCurrencykey());
				dtSapUniversalHeader.setLedgergroup(listContractUniversaPicking.get(0).getLedgergroup());
				dtSapUniversalHeader.setReference(listContractUniversaPicking.get(0).getReference());
				dtSapUniversalHeader.setDocumentheadertext(listContractUniversaPicking.get(0).getDocumentheadertext());
				dtSapUniversalHeader.setReferenceHDKey1(listContractUniversaPicking.get(0).getReferencehdkey1());
				dtSapUniversalHeader.setReferenceHDKey2(listContractUniversaPicking.get(0).getReferencehdkey2());
				dtSapUniversalHeader.setExchangerate(listContractUniversaPicking.get(0).getExchangerate());
				dtSapUniversalHeader.setBranchcode(listContractUniversaPicking.get(0).getBranchcode());
				dtSapUniversalHeader.setTaxReportingDate(listContractUniversaPicking.get(0).getTaxreportingdate());
				dtSapUniversalHeader.setStatus("W");
				dtSapUniversalHeader.setMessageType(null);
				dtSapUniversalHeader.getDocumentNumber();
				dtSapUniversalHeader.setFiscalYear(null);
				dtSapUniversalHeader.setCreateValue(masterValue);
				
				dtSapUniversalHeaderDao.insert(dtSapUniversalHeader);
				
				//item
				List<DtSapUniversalItem> listDtSapUniversalItem = new ArrayList<DtSapUniversalItem>();
				for(QueryContractUniversalBean contractUniversaPicking:listContractUniversaPicking) {
					DtSapUniversalItem dtSapUniversalItem = new DtSapUniversalItem();
					dtSapUniversalItem.setSapUniversalHeaderId(dtSapUniversalHeader.getSapUniversalHeaderId());
					dtSapUniversalItem.setPostingkey(contractUniversaPicking.getPostingkey());
					dtSapUniversalItem.setAccount(contractUniversaPicking.getAccount());
					dtSapUniversalItem.setAmountinDocCurrency(contractUniversaPicking.getAmountindoccurrency());
					dtSapUniversalItem.setBusinessplace(contractUniversaPicking.getBusinessplace());
					//dtSapUniversalItem.setItemAssignment();
					dtSapUniversalItem.setItemText("Airtime Bundle with Device - Sale");
					dtSapUniversalItem.setProductnumber(contractUniversaPicking.getProductnumber());
					dtSapUniversalItem.setPlant(BeanUtil.isNotEmpty(contractUniversaPicking.getPlant()) ? contractUniversaPicking.getPlant() : contractUniversaPicking.getPlant2());
					dtSapUniversalItem.setProfitcenter(contractUniversaPicking.getProfitcentercopa());
					dtSapUniversalItem.setProfitCenterCOPA(contractUniversaPicking.getProfitcentercopa());
					dtSapUniversalItem.setProjectUSSDCode(contractUniversaPicking.getProjectussdcode());
					dtSapUniversalItem.setSalesOrganization(contractUniversaPicking.getSalesorganization());
					dtSapUniversalItem.setDistributionChannel(contractUniversaPicking.getDistributionchannel());
					dtSapUniversalItem.setDivision(contractUniversaPicking.getDivision());
					dtSapUniversalItem.setPlant2(contractUniversaPicking.getPlant2());
					dtSapUniversalItem.setCreateValue(masterValue);
					
					listDtSapUniversalItem.add(dtSapUniversalItem);
				}
				dtSapUniversalItemDao.insertList(listDtSapUniversalItem);
			}
			
		return dtSapUniversalHeader;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public DtSapContractTransLog insertSapContractTransLog(DtSapContractTrans sapContractTrans) throws DataAccessException {
		
		try {
			DtSapContractTransLog dtSapContractTransLog = null;
			List<QuerySapContractTransLogBean>  listQuerySapContractTransLog = queryDataSapContractTransLogPicking(sapContractTrans.getDocNo(), sapContractTrans.getCompany());
			if(BeanUtil.isNotEmpty(listQuerySapContractTransLog) && listQuerySapContractTransLog.size()>0) {
				
				Date date = new Date();
				MasterValue masterValue = new MasterValue();
				masterValue.setCreated(date);
				masterValue.setCreatedBy(sapContractTrans.getCreateValue().getCreatedBy());
				masterValue.setLastUpd(date);
				masterValue.setLastUpdBy(sapContractTrans.getCreateValue().getLastUpdBy());
				
				dtSapContractTransLog = new DtSapContractTransLog();
				dtSapContractTransLog.setCompany(listQuerySapContractTransLog.get(0).getReceiptCompany());
				dtSapContractTransLog.setDocNo(listQuerySapContractTransLog.get(0).getReferenceNo());
				dtSapContractTransLog.setDocDate(date);
//				dtSapContractTransLog.setAccuralObjectNumber(null);
				dtSapContractTransLog.setOCategory(listQuerySapContractTransLog.get(0).getO_catagory());
				dtSapContractTransLog.setOSubcate(listQuerySapContractTransLog.get(0).getO_sub_catagory());
				dtSapContractTransLog.setMobileNo(listQuerySapContractTransLog.get(0).getMobileNo());
				dtSapContractTransLog.setImeiNumberOld(listQuerySapContractTransLog.get(0).getSerialNo());
//				dtSapContractTransLog.setImeiNumberNew(null);
				dtSapContractTransLog.setStatus(listQuerySapContractTransLog.get(0).getStatus());			
				dtSapContractTransLog.setCreateValue(masterValue);
				dtSapContractTransLogDao.insertSensitiveData(dtSapContractTransLog);
			}
			return dtSapContractTransLog;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void updateDtSapContractTransLog(DtSapContractTransLog dtSapContractTransLog) {
		try {
			dtSapContractTransLog.getCreateValue().setLastUpd(new Date());
			dtSapContractTransLogDao.updateSensitiveData(dtSapContractTransLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<QueryContractAccrualBean> queryContactAccrualPingking(String docNo,String company){
		try {
			//on cloud
//			return dtSapContractTypeDao.queryContactAccrualPingking(docNo,company);
			
			//on prem
			return queryContactAccrualPingkingOnPrem(docNo,company);
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	private List<QueryContractAccrualBean> queryContactAccrualPingkingOnPrem(String docNo,String company) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        Type listQueryContractAccrualBean = new TypeToken<ArrayList<QueryContractAccrualBean>>() {}.getType();
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-accrual-picking",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
		String resObject = gsonRes.toJson(res.getResultObj());
		List<QueryContractAccrualBean> list = gson.fromJson(resObject,listQueryContractAccrualBean);
		return list;
   }

	private List<QueryContractUniversalBean> queryContractUniversalPicking(String company,String docNo){
		try {
			//on cloud
//			return dtSapContractTypeDao.queryContractUniversalPicking(company,docNo);
			
			//on prem
			return queryContractUniversalPickingOnPrem(company,docNo);
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	private List<QueryContractUniversalBean> queryContractUniversalPickingOnPrem(String company,String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        Type listQueryContractUniversalBean = new TypeToken<ArrayList<QueryContractUniversalBean>>() {}.getType();
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-universal-picking",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
		String resObject = gsonRes.toJson(res.getResultObj());
		List<QueryContractUniversalBean> list = gson.fromJson(resObject,listQueryContractUniversalBean);
		return list;
   }
	
	@Override
	public DtSapUniversalHeader callUniversalApi(DtSapUniversalHeader dtSapOrderHeader) {
		DtSapUniversalHeader res = sapCallApiService.callUniversalApi(dtSapOrderHeader);
		return res;
	}
	
	@Override
	public DtSapAccrualHeader callAccrualApi(DtSapAccrualHeader dtSapAccrualHeader) {
		DtSapAccrualHeader res = sapCallApiService.callAccrualApi(dtSapAccrualHeader);
		return res;
	}
	
	@Override
	public void updateDtSapUniversalHeader(DtSapUniversalHeader dtSapOrderHeader) {
		dtSapOrderHeader.getCreateValue().setLastUpd(new Date());
		dtSapUniversalHeaderDao.update(dtSapOrderHeader);
	}
	
	@Override
	public void updateDtSapAccrualHeader(DtSapAccrualHeader dtSapAccrualHeader) {
		dtSapAccrualHeader.getCreateValue().setLastUpd(new Date());
		dtSapAccrualHeaderDao.update(dtSapAccrualHeader);
	}
	
	@Override
	public void updateDtsapContractTrans(DtSapContractTrans dtSapContractTrans) {
		try {
			dtSapContractTrans.getCreateValue().setLastUpd(new Date());
			dtSapContractTransDao.update(dtSapContractTrans);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<QuerySapContractTransLogBean> queryDataSapContractTransLogPicking(String docNo,String company) {
		//on cloud
//		return dtSapContractTransLogDao.queryDataSapContractTransLogPicking(docNo, company);
		
		//on prem
		return queryDataSapContractTransLogPickingOnPrem(docNo, company);
	}
	
	private List<QuerySapContractTransLogBean> queryDataSapContractTransLogPickingOnPrem(String docNo,String company) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        Type listQueryContractUniversalBean = new TypeToken<ArrayList<QuerySapContractTransLogBean>>() {}.getType();
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-log-picking",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
		String resObject = gsonRes.toJson(res.getResultObj());
		List<QuerySapContractTransLogBean> list = gson.fromJson(resObject,listQueryContractUniversalBean);
		return list;
   }
	
}
