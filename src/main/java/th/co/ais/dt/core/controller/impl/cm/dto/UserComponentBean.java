package th.co.ais.dt.core.controller.impl.cm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import th.co.ais.dt.entity.util.UserComponent;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserComponentBean {
    private String groupName;
    private String programCode;
    private String component;
    private String visibleFlg;
    private String enableFlg;
    private String userId;

    private String resultCode;
    private String developerMessage;
    private String resultDescription;
    private List<UserComponent> userComponentList;
}
