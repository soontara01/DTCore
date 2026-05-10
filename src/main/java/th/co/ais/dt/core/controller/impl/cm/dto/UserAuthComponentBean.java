package th.co.ais.dt.core.controller.impl.cm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import th.co.ais.dt.entity.util.UserAuth;
import th.co.ais.dt.entity.util.UserComponent;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuthComponentBean {
    UserAuth userAuth;
    List<UserComponent> userComponentList;
    String editBy;

    private String resultCode;
    private String developerMessage;
    private String resultDescription;
}
