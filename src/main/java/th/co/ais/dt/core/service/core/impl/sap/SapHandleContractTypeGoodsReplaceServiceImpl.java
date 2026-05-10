package th.co.ais.dt.core.service.core.impl.sap;

import java.lang.reflect.Type;
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
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractTypeGoodsReplaceService;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTypeDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleContractTypeGoodsReplaceServiceImpl implements ISapHandleContractTypeGoodsReplaceService {
	
	private final IDtSapContractTransDao dtSapContractTransDao;
	private final IDtSapContractTypeDao dtSapContractTypeDao;
	private final IDtSapContractTransLogDao dtSapContractTransLogDao;
	
	private final DTConfig dTConfig;
	
	@Override
	public DtSapContractTrans queryDtSapContractTransByKey(Long sapContractId) {
		return dtSapContractTransDao.getByLongPrimaryKey(sapContractId);
	}
	
	@Override
	public void insertDtSapContractTransLog(DtSapContractTrans dtSapContractTrans, DtSapContractTransLog goodsReplaceInfo) {
		try {
			DtSapContractTransLog logSale = dtSapContractTransLogDao.getSapContractTransLogReceipt(goodsReplaceInfo.getCompany(), goodsReplaceInfo.getDocNo());
			
			DtSapContractTransLog log = new DtSapContractTransLog();
			log.setCompany(goodsReplaceInfo.getCompany());
			log.setDocNo(goodsReplaceInfo.getDocNo());
			log.setDocDate(goodsReplaceInfo.getDocDate());
			log.setAccuralObjectNumber(logSale.getAccuralObjectNumber());
			log.setOCategory("B0210");
			log.setOSubcate("B0211");
			log.setMobileNo(goodsReplaceInfo.getMobileNo());
			log.setImeiNumberOld(goodsReplaceInfo.getImeiNumberOld());
			log.setImeiNumberNew(goodsReplaceInfo.getImeiNumberNew());
			log.setStatus("GOODS REPLACE");
			
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy(dtSapContractTrans.getCreateValue().getCreatedBy());
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy(dtSapContractTrans.getCreateValue().getLastUpdBy());
			
			log.setCreateValue(masterValue);
			
			dtSapContractTransLogDao.insertSensitiveData(log);
		}catch (Exception e) {
			log.error("insertDtSapContractTransLog", e);
		}
	}
	
	@Override
	public List<DtSapContractTransLog> queryContractGoodsReplace(String company, String docNo){
		// query from postgres ,
		//return dtSapContractTypeDao.queryContractGoodsReplace(company, docNo);
		
		// query from on prem
		return queryContractGoodsReplaceOnPrem(company, docNo);
	}
	
	private List<DtSapContractTransLog> queryContractGoodsReplaceOnPrem(String company, String docNo) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setCompany(company);
        in.setDocNo(docNo);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/query-contract-goods-replace", gson.toJson(in), "POST", null) ;
		Gson gsonRes = new Gson();
		CommonResponseBean res = gsonRes.fromJson(out, CommonResponseBean.class);
		
		String resultObj = gsonRes.toJson(res.getResultObj());
		Type type = new TypeToken<List<DtSapContractTransLog>>() {}.getType();
		
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
