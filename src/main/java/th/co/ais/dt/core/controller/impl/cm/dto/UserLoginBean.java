package th.co.ais.dt.core.controller.impl.cm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginBean {
    private String userId;
    private String groupName;
    private String groupNameNew;
    private String editBy;

    private String resultCode;
    private String developerMessage;
    private String resultDescription;
}
