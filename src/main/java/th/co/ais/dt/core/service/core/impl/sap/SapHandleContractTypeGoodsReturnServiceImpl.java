package th.co.ais.dt.core.service.core.impl.sap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractTypeGoodsReturnService;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.entity.sap.DtSapUniversalItem;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalItemDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryContractUniversalBean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypeGoodsReturnServiceImpl implements ISapHandleContractTypeGoodsReturnService {
	
	private final IDtSapContractTransDao dtSapContractTransDao;
	private final IDtSapUniversalHeaderDao dtSapUniversalHeaderDao;
	private final IDtSapUniversalItemDao dtSapUniversalItemDao;
	private final IDtSapContractTypeDao dtSapContractTypeDao;
	private final IDtSapContractTransLogDao dtSapContractTransLogDao;
	
	private final DTConfig dTConfig;
	
	private final ILovMasterDao lovMasterDao;
	private final ISapCallApiService sapCallApiService;
	
	
	@Override
	public DtSapContractTrans queryDtSapContractTransByKey(Long sapContractId) {
		return dtSapContractTransDao.getByLongPrimaryKey(sapContractId);
	}
	
	@Override
	public DtSapUniversalHeader queryInfoAndInsertUniversal(DtSapContractTrans dtSapContractTrans) {
		try {
			// query
			List<QueryContractUniversalBean> listQueryUniversal = queryContractUniversalCn(dtSapContractTrans.getCompany(), dtSapContractTrans.getDocNo());
			if(BeanUtil.isEmpty(listQueryUniversal)) {
				return null;
			}

			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapContractTrans.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapContractTrans.getCreateValue().getLastUpdBy());
			
			
			
			//set values
			// HEADER======================
			QueryContractUniversalBean queryUniversalHeader = listQueryUniversal.get(0);
			
			DtSapUniversalHeader dtSapUniversalHeader = new DtSapUniversalHeader();
			dtSapUniversalHeader.setSapContractId(dtSapContractTrans.getSapContractId());
			dtSapUniversalHeader.setDocNo(queryUniversalHeader.getDocno()); 
			dtSapUniversalHeader.setCompanycode(queryUniversalHeader.getCompanycode());
			dtSapUniversalHeader.setDocumentdate(queryUniversalHeader.getDocumentdate());
			dtSapUniversalHeader.setPostingdate(queryUniversalHeader.getPostingdate());
			dtSapUniversalHeader.setDocType(queryUniversalHeader.getDoctype());;
			dtSapUniversalHeader.setFiscalperiod(queryUniversalHeader.getFiscalperiod());
			dtSapUniversalHeader.setCurrencykey(queryUniversalHeader.getCurrencykey());
			dtSapUniversalHeader.setLedgergroup(queryUniversalHeader.getLedgergroup());
			dtSapUniversalHeader.setReference(queryUniversalHeader.getReference());
			dtSapUniversalHeader.setDocumentheadertext(queryUniversalHeader.getDocumentheadertext());
			dtSapUniversalHeader.setReferenceHDKey1(queryUniversalHeader.getReferencehdkey1());
			dtSapUniversalHeader.setReferenceHDKey2(queryUniversalHeader.getReferencehdkey2());
			dtSapUniversalHeader.setExchangerate(queryUniversalHeader.getExchangerate());
			dtSapUniversalHeader.setBranchcode(queryUniversalHeader.getBranchcode());
			dtSapUniversalHeader.setTaxReportingDate(queryUniversalHeader.getTaxreportingdate());
			dtSapUniversalHeader.setStatus("W");
			dtSapUniversalHeader.setCreateValue(masterValue);
			
			dtSapUniversalHeaderDao.insert(dtSapUniversalHeader);
			
			
			DtSapContractTransLog saleLog = dtSapContractTransLogDao.getSapContractTransLogReceipt(dtSapContractTrans.getCompany(), dtSapUniversalHeader.getReferenceHDKey2());
			String assignment = "";
			if(saleLog != null) {
				assignment = saleLog.getAccuralObjectNumber();
			}
			List<DtSapUniversalItem> listDtSapUniversalItem = new ArrayList<DtSapUniversalItem>();
			for (QueryContractUniversalBean queryUniversalItem : listQueryUniversal) {
				DtSapUniversalItem dtSapUniversalItem = new DtSapUniversalItem();
				
				dtSapUniversalItem.setSapUniversalHeaderId(dtSapUniversalHeader.getSapUniversalHeaderId());
				dtSapUniversalItem.setPostingkey(queryUniversalItem.getPostingkey());
				dtSapUniversalItem.setAccount(queryUniversalItem.getAccount());
				dtSapUniversalItem.setAmountinDocCurrency(queryUniversalItem.getAmountindoccurrency());
				dtSapUniversalItem.setBusinessplace(queryUniversalItem.getBusinessplace());
				dtSapUniversalItem.setProfitcenter(queryUniversalItem.getProfitcenter());
				dtSapUniversalItem.setAssignment(assignment);
				dtSapUniversalItem.setItemText(queryUniversalItem.getItemtext());
				dtSapUniversalItem.setProductnumber(queryUniversalItem.getProductnumber());
				dtSapUniversalItem.setPlant(queryUniversalItem.getPlant());
				dtSapUniversalItem.setProfitCenterCOPA(queryUniversalItem.getProfitcentercopa());
				dtSapUniversalItem.setProjectUSSDCode(queryUniversalItem.getProjectussdcode());
				dtSapUniversalItem.setSalesOrganization(queryUniversalItem.getSalesorganization());
				dtSapUniversalItem.setDistributionChannel(queryUniversalItem.getDistributionchannel());
				dtSapUniversalItem.setDivision(queryUniversalItem.getDivision());
				dtSapUniversalItem.setPlant2(queryUniversalItem.getPlant2());
				
				dtSapUniversalItem.setCreateValue(masterValue);
				
				listDtSapUniversalItem.add(dtSapUniversalItem);
				
				dtSapUniversalItemDao.insertList(listDtSapUniversalItem);
			}
			
			return dtSapUniversalHeader ;
		}catch (Exception e) {
			log.error("queryInfoAndInsertUniversal", e);
			return null;
		}
	}
		
	@Override
	public DtSapUniversalHeader callUniversalApi(DtSapUniversalHeader dtSapOrderHeader) {
		DtSapUniversalHeader res = sapCallApiService.callUniversalApi(dtSapOrderHeader);
		return res;
	}
	
	@Override
	public void insertDtSapContractTransLog(DtSapContractTrans dtSapContractTrans, DtSapUniversalHeader dtSapUniversalHeader) {
		try {
			DtSapContractTransLog saleLog = dtSapContractTransLogDao.getSapContractTransLogReceipt(dtSapContractTrans.getCompany(), dtSapUniversalHeader.getReferenceHDKey2());
			
			DtSapContractTransLog log = new DtSapContractTransLog();
			log.setCompany(dtSapContractTrans.getCompany());
			log.setDocNo(dtSapContractTrans.getDocNo());
			log.setDocDate(TDMDataUtility.convertStringToDateByFormat(dtSapUniversalHeader.getDocumentdate(), "yyyy-MM-dd"));
			log.setOCategory("B0210");
			log.setOSubcate("B0211");
			log.setImeiNumberNew(null);
			log.setStatus("GOODS RETURN");
			
			if(saleLog != null) {
				log.setAccuralObjectNumber(saleLog.getAccuralObjectNumber());
				log.setMobileNo(saleLog.getMobileNo());
				log.setImeiNumberOld(saleLog.getImeiNumberNew());
			}
			
			log.setCreateValue(dtSapUniversalHeader.getCreateValue());
			
			dtSapContractTransLogDao.insertSensitiveData(log);
		}catch (Exception e) {
			log.error("insertDtSapContractTransLog", e);
		}
	}
	
	@Override
	public void updateDtSapUniversalHeader(DtSapUniversalHeader dtSapUniversalHeader) {
		try {
			dtSapUniversalHeaderDao.update(dtSapUniversalHeader);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	private List<QueryContractUniversalBean> queryContractUniversalCn(String company, String docNo){
		// query from postgres ,
		//return dtSapContractTypeDao.queryContractUniversalCn(company, docNo);
		
		// query from on prem
		return queryContractUniversalCnOnPrem(company, docNo);
	}
	
	private List<QueryContractUniversalBean> queryContractUniversalCnOnPrem(String company, String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-universal-cn", gson.toJson(in), "POST", null) ;
		Gson gsonRes = new Gson();
		CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
		
		String resultObj = gsonRes.toJson(res.getResultObj());
		Type type = new TypeToken<List<QueryContractUniversalBean>>() {}.getType();
		
		return gsonRes.fromJson(resultObj, type);
	}
	
	@Override
	public void updateDtSapContractTransAfterWorkflow(DtSapContractTrans dtSapContractTrans) {
		try {
			dtSapContractTrans.getCreateValue().setLastUpd(new Date());
			dtSapContractTransDao.update(dtSapContractTrans);
		}catch (Exception e) {
			log.error("updateDtSapContractTransAfterWorkflow", e);
		}
	}
	
}
