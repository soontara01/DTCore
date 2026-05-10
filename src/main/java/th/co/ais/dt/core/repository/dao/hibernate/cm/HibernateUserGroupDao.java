package th.co.ais.dt.core.repository.dao.hibernate.cm;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import th.co.ais.dt.core.controller.impl.cm.dto.UserGroupBean;
import th.co.ais.dt.core.repository.interfaces.cm.IUserGroupDao;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class HibernateUserGroupDao implements IUserGroupDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<UserGroupBean> queryUserGroup() throws DataAccessException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT group_id AS groupId,    ");
            sql.append("        group_name AS groupName,");
            sql.append("        description     ");
            sql.append(" FROM td_user_group     ");
            sql.append(" WHERE active_flg = 'Y' ");
            sql.append(" ORDER BY group_id      ");

            Session session = this.getSession();
            NativeQuery<UserGroupBean> query = session.createNativeQuery(sql.toString());
            query.addScalar("groupId", StandardBasicTypes.LONG);
            query.addScalar("groupName", StandardBasicTypes.STRING);
            query.addScalar("description", StandardBasicTypes.STRING);
            query.setResultTransformer(Transformers.aliasToBean(UserGroupBean.class));

            return query.getResultList();
        } catch (Exception e) {
            log.error("getUserGroup error ", e);
            return Collections.emptyList();
        }
    }
}
