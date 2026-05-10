package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.util.List;

import lombok.Data;

@Data
public class SalesPricingConditionRecord {
	
    private String conditionValidityEndDate;
    private String conditionValidityStartDate;
    private String creationDate;
    private ConditionRateValue conditionRateValue;
    private ConditionRateAmount conditionRateAmount;
    private ConditionQuantity conditionQuantity;
    private List<ConditionValidity> conditionValidity;
    private List<SalesPricingConditionRecord> SalesPricingConditionRecord;

}

