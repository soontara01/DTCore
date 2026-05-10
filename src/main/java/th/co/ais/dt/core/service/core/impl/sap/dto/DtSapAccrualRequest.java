package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;
import th.co.ais.dt.entity.sap.DtSapAccrualItem;

@Data
public class DtSapAccrualRequest {
	private String MessageID;
	private String PartnerName;
	private String PartnerMessageID;
	private String Companycode;
	private String AccrualObjectCategory;
	private String AccrualObjectSubcategory;
	private List<DtSapAccrualItem> Item;
}
