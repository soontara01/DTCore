package th.co.ais.dt.core.service.core.interfaces.cm;

import jakarta.xml.bind.ValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import th.co.ais.dt.core.controller.impl.cm.dto.UserAuthComponentBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserLoginBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserComponentBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserGroupBean;
import th.co.ais.dt.entity.util.DtMenu;
import th.co.ais.dt.entity.util.UserComponent;

import java.util.List;

@Transactional
public interface IMasterConfigService {
    void validateUserAuthComponent(UserAuthComponentBean request) throws ValidationException;

    List<UserGroupBean> queryUserGroup() throws DataAccessException;

    List<DtMenu> queryProgramCode() throws DataAccessException;

    List<UserComponent> queryUserComponent(UserComponentBean request) throws DataAccessException;

    void updateUserLogin(UserLoginBean request) throws DataAccessException;

    void deleteUserLogin(UserLoginBean request) throws DataAccessException;

    void insertUserAuthComponent(UserAuthComponentBean request) throws DataAccessException;

    void deleteUserAuthComponent(UserAuthComponentBean request) throws DataAccessException;
}
