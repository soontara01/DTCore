package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateGoodsReturnEclaim208 {
	
	private String locationCode ;
	private String receiptNo ;
	private String company ;
	private String autoTransferFlg ;
	
	private String customerName ;
	private String address1 ;
	private String address2 ;
	

	
	//returnItems
	private List<CreateGoodsReturnEclaim208> returnItems ;
	private String serialNo ;
	private String qty ;
	private String matCode ;
	
	private String cnMemoNo ;
	private List<CreateGoodsReturnEclaim208> cnMemoItem ;
	private String invoiceSeq ;
	private String itemNo ;
	

}