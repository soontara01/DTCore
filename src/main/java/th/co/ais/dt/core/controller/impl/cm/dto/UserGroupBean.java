package th.co.ais.dt.core.controller.impl.cm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGroupBean {
    private Long groupId;
    private String groupName;
    private String description;

    private String resultCode;
    private String developerMessage;
    private String resultDescription;
    private List<UserGroupBean> userGroupList;
}
