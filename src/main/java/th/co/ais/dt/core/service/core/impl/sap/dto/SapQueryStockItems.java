package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SapQueryStockItems {
	
	String MaterialNumber;
	String MaterialDescription;
	String Plant;
	String PlantDescription;
	String StorageLocation;
	String StorageLocationDescription;
	String BaseUnit;
	String MaterialType;
	String MaterialGroup;
	String MaterialGroupDescription;
	String BrandCode;
	String BrandDescription;
	String Model;
	String Unrestricted;
	String QualityInspection;
	String Blocked;
}
