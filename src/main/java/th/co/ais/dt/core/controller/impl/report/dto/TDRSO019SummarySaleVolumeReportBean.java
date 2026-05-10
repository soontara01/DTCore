package th.co.ais.dt.core.controller.impl.report.dto;

import java.io.Serializable;
import java.util.List;

public class TDRSO019SummarySaleVolumeReportBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Input parameter
	private String action;
	private String company;
	private String reportType;
	private String reportTypeId;
	private String runType;
	private String runAtDate;
	private String startDate;
	private String endDate;
	private List<String> productTypeList;
	private List<String> productSubTypeList;
	private List<String> productBrandList;
	private String locationCode;
	private String locationName;
	private String locationType;
	private List<String> locationSubType;
	private String locationRegion;
	private List<String> locationCodeList;
	private List<String> disbutionChannel;
	private String disbutionRegion;
	private String dealerCode;
	private String companyName;
	private List<String> dealerCodeList;
	private String userId;
	
	// Output parameter
	private String resultCode;
	private String resultDescription;
	private String developerMessage;
	private List<TDRSO019DataItemBean> companyList;
	private List<TDRSO019DataItemBean> locationTypeList;
	private List<TDRSO019DataItemBean> locationSubTypeList;
	private List<TDRSO019DataItemBean> locationRegionList;
	private List<TDRSO019DataItemBean> dealerDisChannelList;
	private List<TDRSO019DataItemBean> dealerRegionList;
	private List<TDRSO019DataItemBean> locationList;
	private List<TDRSO019DataItemBean> dealerList;

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

	public String getRunAtDate() {
		return runAtDate;
	}

	public void setRunAtDate(String runAtDate) {
		this.runAtDate = runAtDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<String> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<String> productTypeList) {
		this.productTypeList = productTypeList;
	}

	public List<String> getProductSubTypeList() {
		return productSubTypeList;
	}

	public void setProductSubTypeList(List<String> productSubTypeList) {
		this.productSubTypeList = productSubTypeList;
	}

	public List<String> getProductBrandList() {
		return productBrandList;
	}

	public void setProductBrandList(List<String> productBrandList) {
		this.productBrandList = productBrandList;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public List<String> getLocationSubType() {
		return locationSubType;
	}

	public void setLocationSubType(List<String> locationSubType) {
		this.locationSubType = locationSubType;
	}

	public String getLocationRegion() {
		return locationRegion;
	}

	public void setLocationRegion(String locationRegion) {
		this.locationRegion = locationRegion;
	}

	public List<String> getDisbutionChannel() {
		return disbutionChannel;
	}

	public void setDisbutionChannel(List<String> disbutionChannel) {
		this.disbutionChannel = disbutionChannel;
	}

	public String getDisbutionRegion() {
		return disbutionRegion;
	}

	public void setDisbutionRegion(String disbutionRegion) {
		this.disbutionRegion = disbutionRegion;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<String> getLocationCodeList() {
		return locationCodeList;
	}

	public void setLocationCodeList(List<String> locationCodeList) {
		this.locationCodeList = locationCodeList;
	}

	public List<String> getDealerCodeList() {
		return dealerCodeList;
	}

	public void setDealerCodeList(List<String> dealerCodeList) {
		this.dealerCodeList = dealerCodeList;
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

	public List<TDRSO019DataItemBean> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<TDRSO019DataItemBean> companyList) {
		this.companyList = companyList;
	}

	public List<TDRSO019DataItemBean> getLocationTypeList() {
		return locationTypeList;
	}

	public void setLocationTypeList(List<TDRSO019DataItemBean> locationTypeList) {
		this.locationTypeList = locationTypeList;
	}

	public List<TDRSO019DataItemBean> getLocationSubTypeList() {
		return locationSubTypeList;
	}

	public void setLocationSubTypeList(List<TDRSO019DataItemBean> locationSubTypeList) {
		this.locationSubTypeList = locationSubTypeList;
	}

	public List<TDRSO019DataItemBean> getLocationRegionList() {
		return locationRegionList;
	}

	public void setLocationRegionList(List<TDRSO019DataItemBean> locationRegionList) {
		this.locationRegionList = locationRegionList;
	}

	public List<TDRSO019DataItemBean> getDealerDisChannelList() {
		return dealerDisChannelList;
	}

	public void setDealerDisChannelList(List<TDRSO019DataItemBean> dealerDisChannelList) {
		this.dealerDisChannelList = dealerDisChannelList;
	}

	public List<TDRSO019DataItemBean> getDealerRegionList() {
		return dealerRegionList;
	}

	public void setDealerRegionList(List<TDRSO019DataItemBean> dealerRegionList) {
		this.dealerRegionList = dealerRegionList;
	}

	public List<TDRSO019DataItemBean> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<TDRSO019DataItemBean> locationList) {
		this.locationList = locationList;
	}

	public List<TDRSO019DataItemBean> getDealerList() {
		return dealerList;
	}

	public void setDealerList(List<TDRSO019DataItemBean> dealerList) {
		this.dealerList = dealerList;
	}

}
