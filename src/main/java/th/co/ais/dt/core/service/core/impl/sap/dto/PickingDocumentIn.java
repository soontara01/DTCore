package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.io.Serializable;
import java.util.List;

public class PickingDocumentIn implements Serializable {

	private static final long serialVersionUID = 1L;

	private String docNo;
	private String docType;
	private String company;
	private String userId;
	private String userLocCode;
	private String userMacAddress;
	private String empCode;
	private String shipCostFlg;
	private String doNoFrom;
	private String doNoTo;
	private List<String> doNoList;
	private List<PickingItem> pickingItems;
	private List<PickingDocumentIn> pickingDocList;
	private String keyDtAndOutPut;

	private String shoppingCartId;
	private String sapDoNo;

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserLocCode() {
		return userLocCode;
	}

	public void setUserLocCode(String userLocCode) {
		this.userLocCode = userLocCode;
	}

	public String getUserMacAddress() {
		return userMacAddress;
	}

	public void setUserMacAddress(String userMacAddress) {
		this.userMacAddress = userMacAddress;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getShipCostFlg() {
		return shipCostFlg;
	}

	public void setShipCostFlg(String shipCostFlg) {
		this.shipCostFlg = shipCostFlg;
	}

	public String getDoNoFrom() {
		return doNoFrom;
	}

	public void setDoNoFrom(String doNoFrom) {
		this.doNoFrom = doNoFrom;
	}

	public String getDoNoTo() {
		return doNoTo;
	}

	public void setDoNoTo(String doNoTo) {
		this.doNoTo = doNoTo;
	}

	public List<String> getDoNoList() {
		return doNoList;
	}

	public void setDoNoList(List<String> doNoList) {
		this.doNoList = doNoList;
	}
	
	public List<PickingItem> getPickingItems() {
		return pickingItems;
	}

	public void setPickingItems(List<PickingItem> pickingItems) {
		this.pickingItems = pickingItems;
	}

	public List<PickingDocumentIn> getPickingDocList() {
		return pickingDocList;
	}

	public void setPickingDocList(List<PickingDocumentIn> pickingDocList) {
		this.pickingDocList = pickingDocList;
	}

	public String getKeyDtAndOutPut() {
		return keyDtAndOutPut;
	}

	public void setKeyDtAndOutPut(String keyDtAndOutPut) {
		this.keyDtAndOutPut = keyDtAndOutPut;
	}
	
	public String getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(String shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public String getSapDoNo() {
		return sapDoNo;
	}

	public void setSapDoNo(String sapDoNo) {
		this.sapDoNo = sapDoNo;
	}

	
}
