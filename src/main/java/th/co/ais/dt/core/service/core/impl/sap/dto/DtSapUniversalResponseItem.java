package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class DtSapUniversalResponseItem {
	private String MessageType;
	private String Groupnumber;
	private String Line;
	private String DocumentNumber;
	private String CompanyCode;
	private String FiscalYear;
	private String Reference;
	private String DocumentHeaderText;
	private String RefKeyHD1;
	private String RefKeyHD2;
	private String EntryDate;
	private String TimeEntry;	
	private String Message;
	private String ItemText;
}
