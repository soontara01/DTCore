package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class Sap168ItemReqBean {
	private String Item;
	private String Material;
	private String Plant;
	private String StorageLocation;
	private String MovementType;
	private String Quantity;
	private String UnitofMeasurement;
	private String GoodsRecipient;
	private String PurchaseOrder;
	private String PurchaserOrderItem;
	private List<Sap168ItemSerialReqBean> Serial;

}
