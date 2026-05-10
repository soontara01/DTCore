package th.co.ais.dt.core.repository.dao.hibernate.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import th.co.ais.dt.core.controller.impl.report.dto.BacklogReportBean;
import th.co.ais.dt.core.repository.interfaces.report.IBacklogReportDao;

import java.util.List;

@Repository
@Slf4j
public class HibernateBacklogReportDao implements IBacklogReportDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public List<BacklogReportBean> queryBacklogSummary(BacklogReportBean request) throws DataAccessException {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT seller,                                         ");
        sql.append("        delivery_by AS deliveryBy,                      ");
        sql.append("        order_dt AS orderDt,                            ");
        sql.append("        waiting_print_do AS waitingPrintDo,             ");
        sql.append("        waiting_goods_issue AS waitingGoodsIssue,       ");
        sql.append("        waiting_scan_deliveriy AS waitingScanDeliveriy, ");
        sql.append("        backlog_total AS backlogTotal                   ");
        sql.append(" FROM dt_pkg_sor_dtrso001_get_data_sum (         ");
        sql.append("    :locationCode, :seller, :deliveryBy, :orderDtFrom, :orderDtTo)  ");

        Session session = this.getSession();
        NativeQuery<BacklogReportBean> query = session.createNativeQuery(sql.toString());
        query.addScalar("seller", StandardBasicTypes.STRING);
        query.addScalar("deliveryBy", StandardBasicTypes.STRING);
        query.addScalar("orderDt", StandardBasicTypes.STRING);
        query.addScalar("waitingPrintDo", StandardBasicTypes.STRING);
        query.addScalar("waitingGoodsIssue", StandardBasicTypes.STRING);
        query.addScalar("waitingScanDeliveriy", StandardBasicTypes.STRING);
        query.addScalar("backlogTotal", StandardBasicTypes.STRING);
        query.setResultTransformer(Transformers.aliasToBean(BacklogReportBean.class));

        query.setParameter("locationCode", request.getLocationCode());
        query.setParameter("seller", request.getSeller());
        query.setParameter("deliveryBy", request.getDeliveryBy());
        query.setParameter("orderDtFrom", request.getOrderDtFrom());
        query.setParameter("orderDtTo", request.getOrderDtTo());

        return query.getResultList();
    }
}
