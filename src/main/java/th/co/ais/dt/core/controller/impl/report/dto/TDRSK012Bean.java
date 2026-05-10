package th.co.ais.dt.core.controller.impl.report.dto;

import java.io.Serializable;
import java.util.List;

import th.co.ais.dt.entity.cm.LocationMst;

public class TDRSK012Bean implements Serializable  {
	
	// Input Parameter
	private String locationCode;
	private List<String> listCompany;
	
	// Output Parameter
	private String resultCode;
	private String resultDescription;
	private String developerMessage;
	private List<LocationMst> locationList;

	

	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public List<String> getListCompany() {
		return listCompany;
	}
	public void setListCompany(List<String> listCompany) {
		this.listCompany = listCompany;
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
	public List<LocationMst> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<LocationMst> locationList) {
		this.locationList = locationList;
	}

}
