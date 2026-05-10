package th.co.ais.dt.core.service.core.impl.cm;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import th.co.ais.dt.controller.dto.UserAuthHistBean;
import th.co.ais.dt.controller.dto.UserComponentHistBean;
import th.co.ais.dt.controller.dto.UserLoginHistBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserAuthComponentBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserComponentBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserGroupBean;
import th.co.ais.dt.core.controller.impl.cm.dto.UserLoginBean;
import th.co.ais.dt.core.repository.interfaces.cm.IUserGroupDao;
import th.co.ais.dt.core.service.core.interfaces.cm.IMasterConfigService;
import th.co.ais.dt.entity.util.DtMenu;
import th.co.ais.dt.entity.util.UserAuth;
import th.co.ais.dt.entity.util.UserComponent;
import th.co.ais.dt.entity.util.UserLogin;
import th.co.ais.dt.repository.interfaces.util.IDtMenuDao;
import th.co.ais.dt.repository.interfaces.util.IUserAuthDao;
import th.co.ais.dt.repository.interfaces.util.IUserAuthHistDao;
import th.co.ais.dt.repository.interfaces.util.IUserComponentDao;
import th.co.ais.dt.repository.interfaces.util.IUserComponentHistDao;
import th.co.ais.dt.repository.interfaces.util.IUserLoginDao;
import th.co.ais.dt.repository.interfaces.util.IUserLoginHistDao;
import th.co.ais.dt.util.BeanUtil;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MasterConfigServiceImpl implements IMasterConfigService {

    private final IDtMenuDao dtMenuDao;
    private final IUserGroupDao userGroupDao;
    private final IUserLoginDao userLoginDao;
    private final IUserLoginHistDao userLoginHistDao;
    private final IUserAuthDao userAuthDao;
    private final IUserAuthHistDao userAuthHistDao;
    private final IUserComponentDao userComponentDao;
    private final IUserComponentHistDao userComponentHistDao;

    @Override
    public void validateUserAuthComponent(UserAuthComponentBean request) {
        if (BeanUtil.isNull(request.getUserAuth()) && BeanUtil.isEmpty(request.getUserComponentList())) {
            throw new ValidationException("UserAuth and UserComponent is required");
        }

        // UserAuth
        if (BeanUtil.isNotEmpty(request.getUserAuth())) {
            if (BeanUtil.isEmpty(request.getUserAuth().getGroupName())) {
                throw new ValidationException("UserAuth GroupName is required");
            }

            if (BeanUtil.isEmpty(request.getUserAuth().getProgramCode())) {
                throw new ValidationException("UserAuth ProgramCode is required");
            }
        }

        // UserComponentList
        if (BeanUtil.isNotEmpty(request.getUserComponentList())) {
            boolean isGroupNameEmpty = request.getUserComponentList().stream()
                    .anyMatch(f -> BeanUtil.isEmpty(f.getGroupName()));
            if (isGroupNameEmpty) {
                throw new ValidationException("UserComponent GroupName is required");
            }

            boolean isProgramCodeEmpty = request.getUserComponentList().stream()
                    .anyMatch(f -> BeanUtil.isEmpty(f.getProgramCode()));
            if (isProgramCodeEmpty) {
                throw new ValidationException("UserComponent ProgramCode is required");
            }
        }
    }

    @Override
    public List<UserGroupBean> queryUserGroup() throws DataAccessException {
        return userGroupDao.queryUserGroup();
    }

    @Override
    public List<DtMenu> queryProgramCode() throws DataAccessException {
        return dtMenuDao.listAllSubMenu();
    }

    @Override
    public List<UserComponent> queryUserComponent(UserComponentBean request) throws DataAccessException {
        if (BeanUtil.isNotEmpty(request.getUserId())) {
            return userComponentDao.getUserComponentByUserIdAndProgramCode(request.getUserId(), request.getProgramCode());
        }

        return userComponentDao.getUserComponentByCriteria(request.getGroupName(), request.getProgramCode());
    }

    @Override
    public void updateUserLogin(UserLoginBean request) throws DataAccessException {
        UserLogin userLogin = userLoginDao.getUserLoginByCriteria(request.getUserId(), request.getGroupName());

        if (BeanUtil.isNotEmpty(userLogin)) {
            // Insert History before update main
            UserLoginHistBean userLoginHist = new UserLoginHistBean();
            userLoginHist.setUserId(userLogin.getUserId());
            userLoginHist.setGroupName(userLogin.getGroupName());
            userLoginHist.setLocationCode(userLogin.getLocationCode());
            userLoginHist.setUserGroup(userLogin.getUserGroup());
            userLoginHist.setRoleName(userLogin.getRoleName());
            userLoginHist.setPositionByJob(userLogin.getPositionByJob());
            userLoginHist.setCreatedBy(request.getEditBy());
            userLoginHist.setCreatedDtm(new Date());
            userLoginHistDao.insertUserLoginHist(userLoginHist);

            userLoginDao.updateGroupNameByUserId(userLogin.getUserId(), userLogin.getGroupName(), request.getGroupNameNew());
        }
    }

    @Override
    public void deleteUserLogin(UserLoginBean request) throws DataAccessException {
        UserLogin userLogin = userLoginDao.getUserLoginByCriteria(request.getUserId(), request.getGroupName());

        if (BeanUtil.isNotEmpty(userLogin)) {
            // Insert History before delete main
            UserLoginHistBean userLoginHist = new UserLoginHistBean();
            userLoginHist.setUserId(userLogin.getUserId());
            userLoginHist.setGroupName(userLogin.getGroupName());
            userLoginHist.setLocationCode(userLogin.getLocationCode());
            userLoginHist.setUserGroup(userLogin.getUserGroup());
            userLoginHist.setRoleName(userLogin.getRoleName());
            userLoginHist.setPositionByJob(userLogin.getPositionByJob());
            userLoginHist.setCreatedBy(request.getEditBy());
            userLoginHist.setCreatedDtm(new Date());
            userLoginHistDao.insertUserLoginHist(userLoginHist);

            userLoginDao.delete(userLogin);
        }
    }

    @Override
    public void insertUserAuthComponent(UserAuthComponentBean request) throws DataAccessException {
        // User Auth
        if (BeanUtil.isNotEmpty(request.getUserAuth())) {
            userAuthDao.insert(request.getUserAuth());
        }

        // User Component
        if (BeanUtil.isNotEmpty(request.getUserComponentList())) {
            // Validate user component duplicated
            List<UserComponent> userComponentList = userComponentDao.getUserComponentByCriteria(
                    request.getUserComponentList().get(0).getGroupName(),
                    request.getUserComponentList().get(0).getProgramCode()
            );

            if (BeanUtil.isNotEmpty(userComponentList)) {
                userComponentDao.deleteList(userComponentList);
            }

            userComponentDao.insertList(request.getUserComponentList());
        }
    }

    @Override
    public void deleteUserAuthComponent(UserAuthComponentBean request) throws DataAccessException {
        // User Auth
        if (BeanUtil.isNotEmpty(request.getUserAuth())) {
            List<UserAuth> userAuthList = userAuthDao.listUserAuthByCriteria(
                    request.getUserAuth().getGroupName(),
                    request.getUserAuth().getProgramCode()
            );

            if (BeanUtil.isNotEmpty(userAuthList)) {
                // Insert History before delete main
                UserAuthHistBean userAuthHist = new UserAuthHistBean();
                List<UserAuthHistBean> userAuthHistList = userAuthList.stream().map(m -> {
                    userAuthHist.setGroupName(m.getGroupName());
                    userAuthHist.setProgramCode(m.getProgramCode());
                    userAuthHist.setProcFlg(m.getProcFlg());
                    userAuthHist.setAddFlg(m.getAddFlg());
                    userAuthHist.setDeleteFlg(m.getDeleteFlg());
                    userAuthHist.setEditFlg(m.getEditFlg());
                    userAuthHist.setInquiryFlg(m.getInquiryFlg());
                    userAuthHist.setOther1Flg(m.getOther1Flg());
                    userAuthHist.setOther2Flg(m.getOther2Flg());
                    userAuthHist.setOther3Flg(m.getOther3Flg());
                    userAuthHist.setCreatedBy(request.getEditBy());
                    userAuthHist.setCreatedDtm(new Date());
                    return userAuthHist;
                }).toList();

                userAuthHistList.forEach(userAuthHistDao::insertUserAuthHist);

                userAuthDao.deleteList(userAuthList);
            }

            // User Component
            List<UserComponent> userComponentList = userComponentDao.getUserComponentByCriteria(
                    request.getUserAuth().getGroupName(),
                    request.getUserAuth().getProgramCode()
            );

            if (BeanUtil.isNotEmpty(userComponentList)) {
                // Insert History before delete main
                UserComponentHistBean userComponentHist = new UserComponentHistBean();
                List<UserComponentHistBean> userComponentHistList = userComponentList.stream().map(m -> {
                    userComponentHist.setGroupName(m.getGroupName());
                    userComponentHist.setProgramCode(m.getProgramCode());
                    userComponentHist.setComponent(m.getComponent());
                    userComponentHist.setVisibleFlg(m.getVisibleFlg());
                    userComponentHist.setEnableFlg(m.getEnableFlg());
                    userComponentHist.setCreatedBy(request.getEditBy());
                    userComponentHist.setCreatedDtm(new Date());
                    return userComponentHist;
                }).toList();

                userComponentHistList.forEach(userComponentHistDao::insertUserComponentHist);

                userComponentDao.deleteList(userComponentList);
            }
        }
    }
}
