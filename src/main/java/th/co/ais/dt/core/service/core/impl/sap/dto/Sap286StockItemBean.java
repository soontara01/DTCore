package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class Sap286StockItemBean {
    private String MaterialNumber;
    private String Plant;
    private String StorageLocation;
    private String AvailableStock;
}
