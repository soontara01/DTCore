package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class Sap158ItemBean {
	private String CustomerReference;
	private String ReferenceDocument;
	private String CustomerRefFromSO;
	private String Item;
	private String MaterialCode;
	private String Quantity;
	private String SalesUnit;
	private String SerialNumber;
	private String UniqueItemIdentifier;
	private String OldSerialNumber;
	private String OldUniqueItemIdentifier;
}
