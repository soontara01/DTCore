package th.co.ais.dt.core.controller.impl.report.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BacklogReportBean implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String batchType;
    private String runAtDtm;
    private String reportExportType;
    private String userId;
    private String reportType;

    //parameter
    private String locationCode;
    private String seller;
    private String deliveryBy;
    private String orderDtFrom;
    private String orderDtTo;

    //output
    private String orderDt;
    private String waitingPrintDo;
    private String waitingGoodsIssue;
    private String waitingScanDeliveriy;
    private String backlogTotal;
}
