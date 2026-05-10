package th.co.ais.dt.core.service.core.impl.sap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap208Service;
import th.co.ais.dt.entity.pm.ReceiptDtl;
import th.co.ais.dt.entity.pm.ReceiptMst;
import th.co.ais.dt.entity.so.InvoiceDtl;
import th.co.ais.dt.entity.so.OrderMst;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.pm.IReceiptDtlDao;
import th.co.ais.dt.repository.interfaces.pm.IReceiptMstDao;
import th.co.ais.dt.repository.interfaces.sap.I208Dao;
import th.co.ais.dt.repository.interfaces.so.IInvoiceDtlDao;
import th.co.ais.dt.repository.interfaces.so.IOrderMstDao;
import th.co.ais.dt.service.core.impl.sap.dto.Query208Bean;
import th.co.ais.dt.service.core.impl.sap.dto.QueryPostTransactionReqResBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;

@Service
@Slf4j
@RequiredArgsConstructor
public class Sap208ServiceImpl implements ISap208Service {
	
	private final I208Dao i208Dao ;
	private final IReceiptMstDao receiptMstDao ;
	private final DTConfig dTConfig;
	
	private final IInvoiceDtlDao invoiceDtlDao ;
	private final IReceiptDtlDao receiptDtlDao ;
	private final IOrderMstDao orderMstDao ;

	@Override
	public List<Query208Bean> listOrderFromDT(String saleOrderNo) {
		return i208Dao.listOrderFromDT(saleOrderNo);
	}

	@Override
	public List<Query208Bean> listDetailReceipt(String receiptNo, String company) {
		return i208Dao.listDetailReceipt(receiptNo, company);
	}
	
	@Override
	public String findReceiptNo(String receiptNo , String company) {
		String resr = null ;
		String res = receiptNo ;
		boolean found = true ;
		
		ReceiptMst ori = receiptMstDao.getRecieptNumAndCompany(receiptNo, company) ;
		resr = receiptNo + "|" + ori.getReceiptType() ;
		
		while (found) {
			List<ReceiptMst> el = receiptMstDao.getRecieptNumByRefNo(res, company) ;
			if(BeanUtil.isNotEmpty(el)) {
				res = el.get(0).getReceiptNum() ;
				resr = el.get(0).getReceiptNum() + "|" + el.get(0).getReceiptType() ;
			}else {
				found = false ;
			}
			
		}

		return resr ;
	}
	
	@Override
	public String findReceiptNoOnprem(String receiptNo , String company) {
		String resr = null ;
		String res = receiptNo ;
		boolean found = true ;
		
		ReceiptMst ori = receiptMstDao.getRecieptNumAndCompany(receiptNo, company) ;
		resr = receiptNo + "|" + ori.getReceiptType() ;
		
		while (found) {
			String el = queryReceiptByRefNoOnPrem(res, company) ;
			if(BeanUtil.isNotEmpty(el)) {
				res = el.split("\\|")[0] ;
				resr = el ;
			}else {
				found = false ;
			}
			
		}

		return resr ;
	}
	
	private String queryReceiptByRefNoOnPrem(String docNo , String company ) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        
        QueryPostTransactionReqResBean in = new QueryPostTransactionReqResBean();
        in.setDocNo(docNo);
        in.setCompany(company);
        
		String out = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/saleout/v1/queryReceiptByRefNo",gson.toJson(in) , "POST", null) ;
		Gson gsonRes = new Gson();
		QueryPostTransactionReqResBean res = gsonRes.fromJson(out, QueryPostTransactionReqResBean.class);
		return res.getRefDocNo() ;
   }
	
	@Override
	public OrderMst getOrderMst(String receiptNo, String company) {
		ReceiptMst receiptMst = receiptMstDao.getRecieptNumAndCompany(receiptNo, company);
		ReceiptDtl receiptDtl = receiptDtlDao.getReceiptDtlByKey(receiptMst.getReceiptId(), 1L);
		InvoiceDtl invoiceDtl = invoiceDtlDao.getInvoiceDtlByKey(receiptDtl.getInvId(), 1L);
		return orderMstDao.getByLongPrimaryKeySensitiveData(invoiceDtl.getOrderId());
				//getByLongPrimaryKey(invoiceDtl.getOrderId());
	}

}
