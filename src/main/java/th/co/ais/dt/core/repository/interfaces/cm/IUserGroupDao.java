package th.co.ais.dt.core.repository.interfaces.cm;

import org.springframework.dao.DataAccessException;
import th.co.ais.dt.core.controller.impl.cm.dto.UserGroupBean;

import java.util.List;

public interface IUserGroupDao {
    List<UserGroupBean> queryUserGroup() throws DataAccessException;
}
