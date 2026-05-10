package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.pm.ReceiptMst;
import th.co.ais.dt.entity.so.OrderMst;
import th.co.ais.dt.service.core.impl.sap.dto.Query208Bean;

@Transactional
public interface ISap208Service {
	public List<Query208Bean> listOrderFromDT(String saleOrderNo) ;
	public List<Query208Bean> listDetailReceipt(String receiptNo , String company)  ;
	public String findReceiptNo(String receiptNo , String company) ;
	public String findReceiptNoOnprem(String receiptNo , String company) ;
	public OrderMst getOrderMst(String receiptNo, String company) ;

}
