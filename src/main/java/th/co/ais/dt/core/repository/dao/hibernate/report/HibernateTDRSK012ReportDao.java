package th.co.ais.dt.core.repository.dao.hibernate.report;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSK012Dao;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.repository.dao.hibernate.util.HibernateGenericDao;
import th.co.ais.dt.util.BeanUtil;

@Repository
@Slf4j
public class HibernateTDRSK012ReportDao extends HibernateGenericDao<LocationMst> implements ITDRSK012Dao{
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	@Override
	public List<LocationMst> queryLocationCodeSaleAtShop(Object listCompany, String locationCode)
			throws DataAccessException {
//		Session session = sessionFactory.getCurrentSession();
		
		Session session = getSession();
		StringBuilder hql = new StringBuilder();

		
		hql.append(" SELECT distinct clm.location_code as locationCode, clm.location_name as locationName");
		hql.append(" FROM td_sk_loc_map_plant lmp JOIN cm_location_master clm ON clm.location_code = lmp.location_code");
		hql.append(" WHERE 1 = 1 ");
		

		if ( BeanUtil.isNotEmpty(listCompany) ) {
			hql.append(" AND lmp.company IN (:company) ");
		}
		
		if ( BeanUtil.isNotEmpty(locationCode) ) {
			hql.append(" AND cast(clm.location_code as text) = :locationCode ");
		}
		
		hql.append(" AND (upper(clm.loc_subtype) IN ");
		hql.append(" (select lst.lov_subtype from td_lov_mst lst where lst.lov_type = 'LOC_SUBTYPE_QUERY') OR ");
		hql.append(" clm.LOCATION_CODE = '5002') ");
		hql.append(" AND lmp.active_flag = 'Y' ");
		hql.append(" ORDER BY clm.location_code ");
//		Query query = session.createSQLQuery(hql.toString())
//				.addScalar("locationCode", LongType.INSTANCE)
//				.addScalar("locationName", StringType.INSTANCE)
//				.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
		NativeQuery<LocationMst> query = session.createNativeQuery(hql.toString());
		query.addScalar("locationCode", StandardBasicTypes.LONG);
		query.addScalar("locationName", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
		
		if (listCompany != null) {
			if (listCompany instanceof String) {
				String strLovSubType = (String) listCompany;
				if (!BeanUtil.isEmpty(strLovSubType)) {
					query.setParameter("company", listCompany);
				}
			} else if (listCompany instanceof Collection<?>) {
				Collection<String> collection = (Collection<String>) listCompany;
				if (BeanUtil.isNotEmpty(collection)) {			
					query.setParameterList("company", collection);
				}
			}
		}
		if ( BeanUtil.isNotEmpty(locationCode) ) {
			query.setParameter("locationCode", locationCode);
		}
		return query.list();		
	}
}
