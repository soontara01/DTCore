package th.co.ais.dt.core.controller.impl.cm;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.controller.impl.cm.dto.ProgramCodeBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserAuthComponentBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserComponentBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserGroupBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserLoginBean;
import th.co.ais.dt.core.service.core.interfaces.cm.IMasterConfigService;
import th.co.ais.dt.entity.util.DtMenu;
import th.co.ais.dt.entity.util.UserComponent;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/master-config/v1")
@Slf4j
@AllArgsConstructor
public class MasterConfigImpl {

    private final IMasterConfigService masterConfigService;

    @GetMapping(value = "/query-user-group", produces = {"application/json"})
    @RegisterReflectionForBinding({UserGroupBean.class})
    public ResponseEntity<UserGroupBean> queryUserGroup() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        UserGroupBean response = new UserGroupBean();

        try {
            List<UserGroupBean> userGroupList = masterConfigService.queryUserGroup();

            response.setUserGroupList(userGroupList);
            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription("System error");
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/query-program-code", produces = {"application/json"})
    @RegisterReflectionForBinding({ProgramCodeBean.class})
    public ResponseEntity<ProgramCodeBean> getProgramCode() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        ProgramCodeBean response = new ProgramCodeBean();

        try {
            List<DtMenu> programCodeList = masterConfigService.queryProgramCode();

            response.setProgramCodeList(programCodeList);
            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription("System error");
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/query-user-component", produces = {"application/json"})
    @RegisterReflectionForBinding({UserComponentBean.class})
    public ResponseEntity<UserComponentBean> queryUserComponent(@RequestBody UserComponentBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        UserComponentBean response = new UserComponentBean();

        try {
            List<UserComponent> userComponentList = masterConfigService.queryUserComponent(request);

            response.setUserComponentList(userComponentList);
            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription("System error");
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/update-user-login", produces = {"application/json"})
    @RegisterReflectionForBinding({UserLoginBean.class})
    public ResponseEntity<UserLoginBean> updateUserLogin(@RequestBody UserLoginBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        UserLoginBean response = new UserLoginBean();

        try {
            if (BeanUtil.isEmpty(request.getUserId())) {
                throw new ValidationException("UserId is required");
            }

            if (BeanUtil.isEmpty(request.getGroupName())) {
                throw new ValidationException("GroupName is required");
            }

            if (BeanUtil.isEmpty(request.getGroupNameNew())) {
                throw new ValidationException("GroupNameNew is required");
            }

            masterConfigService.updateUserLogin(request);

            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/delete-user-login", produces = {"application/json"})
    @RegisterReflectionForBinding({UserLoginBean.class})
    public ResponseEntity<UserLoginBean> deleteUserLogin(@RequestBody UserLoginBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        UserLoginBean response = new UserLoginBean();

        try {
            if (BeanUtil.isEmpty(request.getUserId())) {
                throw new ValidationException("UserId is required");
            }

            if (BeanUtil.isEmpty(request.getGroupName())) {
                throw new ValidationException("GroupName is required");
            }

            masterConfigService.deleteUserLogin(request);

            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/insert-user-auth-component", produces = {"application/json"})
    @RegisterReflectionForBinding({UserAuthComponentBean.class})
    public ResponseEntity<UserAuthComponentBean> insertUserAuthComponent(@RequestBody UserAuthComponentBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        UserAuthComponentBean response = new UserAuthComponentBean();

        try {
            masterConfigService.validateUserAuthComponent(request);

            masterConfigService.insertUserAuthComponent(request);

            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/delete-user-auth-component", produces = {"application/json"})
    @RegisterReflectionForBinding({UserAuthComponentBean.class})
    public ResponseEntity<UserAuthComponentBean> deleteUserAuthComponent(@RequestBody UserAuthComponentBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        UserAuthComponentBean response = new UserAuthComponentBean();

        try {
            masterConfigService.validateUserAuthComponent(request);

            masterConfigService.deleteUserAuthComponent(request);

            response.setResultCode("20000");
            response.setResultDescription("Success");
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }
}
