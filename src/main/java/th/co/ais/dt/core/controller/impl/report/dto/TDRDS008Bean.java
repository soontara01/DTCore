package th.co.ais.dt.core.controller.impl.report.dto;

import java.util.List;

import th.co.ais.dt.entity.util.ProductMst;

public class TDRDS008Bean {
private static final long serialVersionUID = 1L;
	
	// Input Parameter
	private List<String> fromLocationCode;
	private List<String> toLocationCode;
	private String productType;
	private String receiptDateFrom;
	private String receiptDateTo;
	
	private String report; //S, D
	private String reportTypeId; //TDRSO004S1
	private String reportType;
	private String runType;
	private String runPeriod;
	private String runAtDate;
	private String userId;
	
	// Output Parameter
	private String resultCode;
	private String resultDescription;
	private String developerMessage;
	private List<ProductMst> dataList;
	
	public List<String> getFromLocationCode() {
		return fromLocationCode;
	}
	public void setFromLocationCode(List<String> fromLocationCode) {
		this.fromLocationCode = fromLocationCode;
	}
	public List<String> getToLocationCode() {
		return toLocationCode;
	}
	public void setToLocationCode(List<String> toLocationCode) {
		this.toLocationCode = toLocationCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getReceiptDateFrom() {
		return receiptDateFrom;
	}
	public void setReceiptDateFrom(String receiptDateFrom) {
		this.receiptDateFrom = receiptDateFrom;
	}
	public String getReceiptDateTo() {
		return receiptDateTo;
	}
	public void setReceiptDateTo(String receiptDateTo) {
		this.receiptDateTo = receiptDateTo;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getReportTypeId() {
		return reportTypeId;
	}
	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getRunType() {
		return runType;
	}
	public void setRunType(String runType) {
		this.runType = runType;
	}
	public String getRunPeriod() {
		return runPeriod;
	}
	public void setRunPeriod(String runPeriod) {
		this.runPeriod = runPeriod;
	}
	public String getRunAtDate() {
		return runAtDate;
	}
	public void setRunAtDate(String runAtDate) {
		this.runAtDate = runAtDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public List<ProductMst> getDataList() {
		return dataList;
	}
	public void setDataList(List<ProductMst> dataList) {
		this.dataList = dataList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
