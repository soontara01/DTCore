package th.co.ais.dt.core.controller.impl.report.dto;

import java.util.List;

import th.co.ais.dt.entity.util.AscMst;
import th.co.ais.dt.entity.util.LovMaster;

public class TDRDS004Bean {

private static final long serialVersionUID = 1L;
	
	// Input Parameter
	private String action;
	
	private String company;
	private String userId;
	private String userLocationCode;
	private String userLocationName;
	private List<String> fromLocationCode;
	private List<String> toLocationCode;
	
	private String fromProcessDt;
	private String toProcessDt;
	private String fromReceiptDt;
	private String toReceiptDt;
	
	private String ascCode;
	private String ascName;
	private String ascType;
	
	private String report;
	private String reportType;
	private String reportTypeId;
	private String runType;
	private String runPeriod;
	private String runAtDate;
		
	// Output Parameter
	private String resultCode;
	private String resultDescription;
	private String developerMessage;
	private List<LovMaster> lovList;
	private List<AscMst> ascMst;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	public String getUserLocationCode() {
		return userLocationCode;
	}
	public void setUserLocationCode(String userLocationCode) {
		this.userLocationCode = userLocationCode;
	}
	public String getUserLocationName() {
		return userLocationName;
	}
	public void setUserLocationName(String userLocationName) {
		this.userLocationName = userLocationName;
	}
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
	public String getFromProcessDt() {
		return fromProcessDt;
	}
	public void setFromProcessDt(String fromProcessDt) {
		this.fromProcessDt = fromProcessDt;
	}
	public String getToProcessDt() {
		return toProcessDt;
	}
	public void setToProcessDt(String toProcessDt) {
		this.toProcessDt = toProcessDt;
	}
	public String getFromReceiptDt() {
		return fromReceiptDt;
	}
	public void setFromReceiptDt(String fromReceiptDt) {
		this.fromReceiptDt = fromReceiptDt;
	}
	public String getToReceiptDt() {
		return toReceiptDt;
	}
	public void setToReceiptDt(String toReceiptDt) {
		this.toReceiptDt = toReceiptDt;
	}
	public String getAscCode() {
		return ascCode;
	}
	public void setAscCode(String ascCode) {
		this.ascCode = ascCode;
	}
	public String getAscName() {
		return ascName;
	}
	public void setAscName(String ascName) {
		this.ascName = ascName;
	}
	public String getAscType() {
		return ascType;
	}
	public void setAscType(String ascType) {
		this.ascType = ascType;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getReportTypeId() {
		return reportTypeId;
	}
	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
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
	public List<LovMaster> getLovList() {
		return lovList;
	}
	public void setLovList(List<LovMaster> lovList) {
		this.lovList = lovList;
	}
	public List<AscMst> getAscMst() {
		return ascMst;
	}
	public void setAscMst(List<AscMst> ascMst) {
		this.ascMst = ascMst;
	}
}
