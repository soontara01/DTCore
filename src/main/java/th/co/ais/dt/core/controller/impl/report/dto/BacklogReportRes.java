package th.co.ais.dt.core.controller.impl.report.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BacklogReportRes implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String resultCode;
    private String resultDescription;
    private String status;

    private List<BacklogReportBean> BacklogReportList;
}
