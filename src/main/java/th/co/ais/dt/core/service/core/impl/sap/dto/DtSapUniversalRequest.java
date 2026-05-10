package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;
import th.co.ais.dt.entity.sap.DtSapUniversalItem;

@Data
public class DtSapUniversalRequest {
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String TotalRecords;
	private String DocNo;
	private String Companycode;
	private String Documentdate;
	private String Postingdate;
	private String DocType;
	private String Fiscalperiod;
	private String Currencykey;
	private String Ledgergroup;
	private String Reference;
	private String Documentheadertext;
	private String ReferenceHDKey1;
	private String ReferenceHDKey2;
	private String Exchangerate;
	private String Branchcode;
	private String TaxReportingDate;
	private String status;
	private String MessageType;
	private String DocumentNumber;
	private String FiscalYear;
	private List<DtSapUniversalItem> Items;
}
