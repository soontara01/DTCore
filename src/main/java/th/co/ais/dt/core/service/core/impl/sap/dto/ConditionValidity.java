package th.co.ais.dt.core.service.core.impl.sap.dto;

import lombok.Data;

@Data
public class ConditionValidity {
	
	private String material;
    private String salesOrganization;
    private String conditionType;
    private String distributionChannel;
    private String plant;
    private String customer;

}
