package th.co.ais.dt.core.controller.impl.report.dto;

import java.io.Serializable;
import java.util.List;

import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.entity.iv.PmCompany;

public class TDRSO012Bean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// Input Parameter
	private String action;
	
	private String company;
	private String userId;
	private String userLocationCode;
	private String userLocationName;
	
	private List<String> fromLocationCode;
	private List<String> toLocationCode;
	private String fromLocationName;
	private String toLocationName;
	private List<String> locationCode;
	private List<String> locationTab;
	private List<String> locationName;
	private List<String> locationType;
	private List<String> locationSubType;
	private List<String> locationRegion;
	private List<String> locationProvince;
	
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
	
	private List<PmCompany> companyList;
	private List<LocationMst> locationList;
//	private List<GoodsReturnReportProductBean> productList;
	
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
	public String getFromLocationName() {
		return fromLocationName;
	}
	public void setFromLocationName(String fromLocationName) {
		this.fromLocationName = fromLocationName;
	}
	public String getToLocationName() {
		return toLocationName;
	}
	public void setToLocationName(String toLocationName) {
		this.toLocationName = toLocationName;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<PmCompany> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<PmCompany> companyList) {
		this.companyList = companyList;
	}
	public List<LocationMst> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<LocationMst> locationList) {
		this.locationList = locationList;
	}
	public List<String> getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(List<String> locationCode) {
		this.locationCode = locationCode;
	}
	public List<String> getLocationTab() {
		return locationTab;
	}
	public void setLocationTab(List<String> locationTab) {
		this.locationTab = locationTab;
	}
	public List<String> getLocationName() {
		return locationName;
	}
	public void setLocationName(List<String> locationName) {
		this.locationName = locationName;
	}
	public List<String> getLocationType() {
		return locationType;
	}
	public void setLocationType(List<String> locationType) {
		this.locationType = locationType;
	}
	public List<String> getLocationSubType() {
		return locationSubType;
	}
	public void setLocationSubType(List<String> locationSubType) {
		this.locationSubType = locationSubType;
	}
	public List<String> getLocationRegion() {
		return locationRegion;
	}
	public void setLocationRegion(List<String> locationRegion) {
		this.locationRegion = locationRegion;
	}
	public List<String> getLocationProvince() {
		return locationProvince;
	}
	public void setLocationProvince(List<String> locationProvince) {
		this.locationProvince = locationProvince;
	}

}
