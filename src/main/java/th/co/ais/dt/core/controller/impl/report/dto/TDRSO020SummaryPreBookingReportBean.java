package th.co.ais.dt.core.controller.impl.report.dto;

import java.io.Serializable;
import java.util.List;

public class TDRSO020SummaryPreBookingReportBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Input parameter
	private String company;
	private String locationCode;
	private String shipFromLocation;
	private String shipToLocation;
	private List<String> shipLocationList;
	private String processDateFrom;
	private String processDateTo;
	private String statusReportType;
	private String channelType;
	private String documentType;
	private String report;
	private String reportType;
	private String runType;
	private Boolean runPeriod;
	private String runAtDate;
	private String reportTypeId;
	private String userId;
	
	// Output parameter
	private String resultCode;
	private String resultDescription;
	private String developerMessage;
	private List<DataCriteriaBean> companyList;
	private List<DataCriteriaBean> statusReportTypeList;
	private List<DataCriteriaBean> channelTypeList;
	private List<DataCriteriaBean> documentTypeList;
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getShipFromLocation() {
		return shipFromLocation;
	}
	public void setShipFromLocation(String shipFromLocation) {
		this.shipFromLocation = shipFromLocation;
	}
	public String getShipToLocation() {
		return shipToLocation;
	}
	public void setShipToLocation(String shipToLocation) {
		this.shipToLocation = shipToLocation;
	}
	public List<String> getShipLocationList() {
		return shipLocationList;
	}
	public void setShipLocationList(List<String> shipLocationList) {
		this.shipLocationList = shipLocationList;
	}
	public String getProcessDateFrom() {
		return processDateFrom;
	}
	public void setProcessDateFrom(String processDateFrom) {
		this.processDateFrom = processDateFrom;
	}
	public String getProcessDateTo() {
		return processDateTo;
	}
	public void setProcessDateTo(String processDateTo) {
		this.processDateTo = processDateTo;
	}
	public String getStatusReportType() {
		return statusReportType;
	}
	public void setStatusReportType(String statusReportType) {
		this.statusReportType = statusReportType;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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
	public String getRunType() {
		return runType;
	}
	public void setRunType(String runType) {
		this.runType = runType;
	}
	public Boolean getRunPeriod() {
		return runPeriod;
	}
	public void setRunPeriod(Boolean runPeriod) {
		this.runPeriod = runPeriod;
	}
	public String getRunAtDate() {
		return runAtDate;
	}
	public void setRunAtDate(String runAtDate) {
		this.runAtDate = runAtDate;
	}
	public String getReportTypeId() {
		return reportTypeId;
	}
	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
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
	public List<DataCriteriaBean> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<DataCriteriaBean> companyList) {
		this.companyList = companyList;
	}
	public List<DataCriteriaBean> getStatusReportTypeList() {
		return statusReportTypeList;
	}
	public void setStatusReportTypeList(List<DataCriteriaBean> statusReportTypeList) {
		this.statusReportTypeList = statusReportTypeList;
	}
	public List<DataCriteriaBean> getChannelTypeList() {
		return channelTypeList;
	}
	public void setChannelTypeList(List<DataCriteriaBean> channelTypeList) {
		this.channelTypeList = channelTypeList;
	}
	public List<DataCriteriaBean> getDocumentTypeList() {
		return documentTypeList;
	}
	public void setDocumentTypeList(List<DataCriteriaBean> documentTypeList) {
		this.documentTypeList = documentTypeList;
	}
	
	public class DataCriteriaBean {
		
		private String code;
		private String value;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}

}
