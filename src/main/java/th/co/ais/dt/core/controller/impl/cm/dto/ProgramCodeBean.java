package th.co.ais.dt.core.controller.impl.cm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import th.co.ais.dt.entity.util.DtMenu;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramCodeBean {
    private String menuCode;
    private String subMenuOf;
    private String displayName;

    private String resultCode;
    private String developerMessage;
    private String resultDescription;
    private List<DtMenu> programCodeList;
}
