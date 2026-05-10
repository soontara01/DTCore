package th.co.ais.dt.core.repository.dao.hibernate.report;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO012Bean;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSO012Dao;
import th.co.ais.dt.entity.cm.LocationMst;
import th.co.ais.dt.repository.dao.hibernate.util.HibernateGenericDao;
import th.co.ais.dt.util.BeanUtil;

@Repository
@Slf4j
public class HibernateTDRSO012ReportDao extends HibernateGenericDao<LocationMst> implements ITDRSO012Dao{

	@Override
	public List<LocationMst> getRangeLocation(TDRSO012Bean request) throws DataAccessException {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.* ");
		sql.append("FROM ( ");
		sql.append("SELECT DISTINCT CLM.LOCATION_CODE AS locationCode, CLM.LOCATION_NAME AS locationName ");
		sql.append("FROM TD_SK_LOC_MAP_PLANT LOC ");
		sql.append("JOIN CM_LOCATION_MASTER CLM ON CLM.LOCATION_CODE = LOC.LOCATION_CODE ");
		sql.append("WHERE 1=1 ");
		sql.append("AND (UPPER(CLM.LOC_SUBTYPE) IN (SELECT LST.LOV_SUBTYPE FROM TD_LOV_MST LST WHERE LST.LOV_TYPE IN ('LOC_SUBTYPE_QUERY', 'LOC_SUBTYPE_QUERY_ASP')) OR CLM.LOCATION_CODE = '5002')  ");
		if(BeanUtil.isNotEmpty(request.getCompany())&&!"ALL".equalsIgnoreCase(request.getCompany())) {
			sql.append("AND LOC.COMPANY = :company  ");
		}
		if(BeanUtil.isNotEmpty(request.getAction()) && "RANGE_LOCATION_LIKE".equalsIgnoreCase(request.getAction())) {
			if(BeanUtil.isNotEmpty(request.getLocationCode())) {
				sql.append("AND CAST(CLM.LOCATION_CODE AS varchar) LIKE :locationCode  ");
			}
		}else {
			if(BeanUtil.isNotEmpty(request.getLocationCode())) {
				sql.append("AND CAST(CLM.LOCATION_CODE AS varchar) = :locationCode  ");
			}
		}

		if(BeanUtil.isNotEmpty(request.getLocationName())) {
			sql.append("AND CLM.LOCATION_NAME LIKE :locationName  ");
		}
		sql.append("UNION ALL  ");
		sql.append("SELECT DISTINCT CLM.LOCATION_CODE AS locationCode, CLM.LOCATION_NAME AS locationName  "); 
		sql.append("FROM TD_UPLOAD_LOCATION_SHIPTO LS  ");
		sql.append("JOIN CM_LOCATION_MASTER CLM  ON LS.LOCATION_CODE = CLM.LOCATION_CODE  ");
		if(BeanUtil.isNotEmpty(request.getAction()) && "RANGE_LOCATION_LIKE".equalsIgnoreCase(request.getAction())) {
			if(BeanUtil.isNotEmpty(request.getLocationCode())) {
				sql.append("AND CAST(CLM.LOCATION_CODE AS varchar) LIKE :locationCode  ");
			}
		}
		sql.append(") A ");
		sql.append("ORDER BY A.LOCATIONCODE ");
		
		NativeQuery<LocationMst> query = session.createNativeQuery(sql.toString());
		query.addScalar("locationCode", StandardBasicTypes.LONG);
		query.addScalar("locationName", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
		if(BeanUtil.isNotEmpty(request.getCompany())&&!"ALL".equalsIgnoreCase(request.getCompany())) {
			query.setParameter("company", request.getCompany());
		}
		if(BeanUtil.isNotEmpty(request.getAction()) && "RANGE_LOCATION_LIKE".equalsIgnoreCase(request.getAction())) {
			if(BeanUtil.isNotEmpty(request.getLocationCode())) {
				query.setParameter("locationCode", "%"+request.getLocationCode().get(0)+"%");
			}
		}else {
			if(BeanUtil.isNotEmpty(request.getLocationCode())) {
				query.setParameter("locationCode", request.getLocationCode().get(0));
			}
		}
		if(BeanUtil.isNotEmpty(request.getLocationName())) {
			query.setParameter("locationName", "%"+request.getLocationName().get(0)+"%");
		}
		return query.list();
	}

	@Override
	public List<LocationMst> getLocationByCriteria(TDRSO012Bean request) throws DataAccessException {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		NativeQuery<LocationMst> query = null;
		if( BeanUtil.isEmpty(request.getLocationType())    &&
			BeanUtil.isEmpty(request.getLocationSubType()) &&
			BeanUtil.isEmpty(request.getLocationRegion())  &&
			BeanUtil.isEmpty(request.getLocationProvince())
		) {
			sql.append("SELECT DISTINCT CLM.LOC_TYPE AS locType ");
			sql.append("FROM TD_SK_LOC_MAP_PLANT LMP ");
			sql.append("JOIN   CM_LOCATION_MASTER CLM ON CLM.LOCATION_CODE = LMP.LOCATION_CODE ");
			sql.append("WHERE  CLM.LOC_TYPE IS NOT NULL ");
			sql.append("ORDER BY  CLM.LOC_TYPE ");
			
			query = session.createNativeQuery(sql.toString());
			query.addScalar("locType", StandardBasicTypes.STRING);
			query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
		}else if( BeanUtil.isNotEmpty(request.getLocationType())    &&
				  BeanUtil.isEmpty(request.getLocationSubType()) &&
				  BeanUtil.isEmpty(request.getLocationRegion())  &&
				  BeanUtil.isEmpty(request.getLocationProvince()) 
				) {
			sql.append("SELECT distinct clm.loc_subtype as locSubtype FROM td_sk_loc_map_plant lmp JOIN   cm_location_master clm ON clm.location_code = lmp.location_code ");
			sql.append("WHERE  clm.loc_subtype IS NOT NULL ");
			if(!"ALL".equals(request.getCompany())) {
				sql.append("AND  lmp.company = :company ");
			}
			sql.append("AND UPPER(clm.loc_type)  = :locType ");
			sql.append("ORDER BY  clm.loc_subtype ");
			
			query = session.createNativeQuery(sql.toString());
			query.addScalar("locSubtype", StandardBasicTypes.STRING);
			query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
			if(!"ALL".equals(request.getCompany())) {
				query.setParameter("company", request.getCompany());
			}
			query.setParameter("locType", request.getLocationType().get(0));
		}else if(BeanUtil.isNotEmpty(request.getLocationType())    &&
				 BeanUtil.isNotEmpty(request.getLocationSubType()) &&
				 BeanUtil.isEmpty(request.getLocationRegion())  &&
				 BeanUtil.isEmpty(request.getLocationProvince())
				) {
			sql.append("SELECT distinct(clm.region || ',' || tlm.description) as region ");
			sql.append("FROM td_sk_loc_map_plant lmp JOIN   cm_location_master clm ON clm.location_code = lmp.location_code ");
			sql.append("JOIN   td_lov_mst tlm ON  tlm.lov_type = 'AIS_REGION' AND tlm.active_flg = 'Y' AND tlm.lov_subtype = 'ENG' AND tlm.lov_code = clm.region ");
			sql.append("WHERE  1=1 ");
			if(!"ALL".equals(request.getCompany())) {
				sql.append("AND lmp.company = :company ");
			}
			sql.append("AND  UPPER(clm.loc_type)  = :locType ");
			sql.append("AND UPPER(clm.loc_subtype) = :locSubtype ");
			sql.append("AND clm.region is not null ");
			sql.append("ORDER BY  region ");
			
			query = session.createNativeQuery(sql.toString());
			query.addScalar("region", StandardBasicTypes.STRING);
			query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
			if(!"ALL".equals(request.getCompany())) {
				query.setParameter("company", request.getCompany());
			}
			query.setParameter("locType", request.getLocationType().get(0));
			query.setParameter("locSubtype", request.getLocationSubType().get(0));
		}else if(BeanUtil.isNotEmpty(request.getLocationType())    &&
				 BeanUtil.isNotEmpty(request.getLocationSubType()) &&
				 BeanUtil.isNotEmpty(request.getLocationRegion())  &&
				 BeanUtil.isEmpty(request.getLocationProvince())
				) {
            log.info("REGION: {}", request.getLocationRegion().get(0).substring(0, request.getLocationRegion().get(0).indexOf(",")));
			
			sql.append("SELECT distinct clm.province as province ");
			sql.append("FROM td_sk_loc_map_plant lmp JOIN cm_location_master clm ON clm.location_code = lmp.location_code ");
			sql.append("WHERE 1=1 ");
			if(!"ALL".equals(request.getCompany())) {
				sql.append("AND lmp.company = :company ");
			}
			sql.append("AND UPPER(clm.loc_type) = :locType ");
			sql.append("AND UPPER(clm.loc_subtype) = :locSubtype ");
			sql.append("AND clm.region like :region ");
			sql.append("ORDER BY  clm.province ");
			
			query = session.createNativeQuery(sql.toString());
			query.addScalar("province", StandardBasicTypes.STRING);
			query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
			if(!"ALL".equals(request.getCompany())) {
				query.setParameter("company", request.getCompany());
			}
			query.setParameter("locType", request.getLocationType().get(0));
			query.setParameter("locSubtype", request.getLocationSubType().get(0));
			query.setParameter("region", request.getLocationRegion().get(0).substring(0,request.getLocationRegion().get(0).indexOf(","))+"%");
		}
		return (BeanUtil.isNotEmpty(query)) ? query.list() : null;
	}

	@Override
	public List<LocationMst> getMultiLocation(TDRSO012Bean request) throws DataAccessException {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		NativeQuery<LocationMst> query;
		sql.append("SELECT A.*  ");
		sql.append("FROM ( ");
		sql.append("SELECT DISTINCT LMP.LOCATION_CODE AS locationCode, CLM.LOCATION_NAME AS locationName ");
		sql.append("FROM TD_SK_LOC_MAP_PLANT LMP ");
		sql.append("JOIN CM_LOCATION_MASTER CLM ON CLM.LOCATION_CODE = LMP.LOCATION_CODE ");
		sql.append("WHERE  1=1 ");
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			sql.append("AND UPPER(CLM.LOC_TYPE) IN :locationType ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationSubType())) {
			sql.append("AND UPPER(CLM.LOC_SUBTYPE) IN :locationSubType ");	
		}
		if(BeanUtil.isNotEmpty(request.getLocationRegion())) {
			sql.append("AND CLM.REGION IN :locationRegion ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationProvince())) {
			sql.append("AND CLM.PROVINCE IN :locationProvince ");
		}
		sql.append(") A ");
		sql.append("ORDER BY A.LOCATIONCODE ");
		
		query = session.createNativeQuery(sql.toString());
		query.addScalar("locationCode", StandardBasicTypes.LONG);
		query.addScalar("locationName", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(LocationMst.class));
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			query.setParameterList("locationType", request.getLocationType());
		}
		if(BeanUtil.isNotEmpty(request.getLocationSubType())) {
			query.setParameterList("locationSubType", request.getLocationSubType());		
		}
		if(BeanUtil.isNotEmpty(request.getLocationRegion())) {
			query.setParameterList("locationRegion", request.getLocationRegion());
		}
		if(BeanUtil.isNotEmpty(request.getLocationProvince())) {
			query.setParameterList("locationProvince", request.getLocationProvince());
		}
		return query.list();
	}
	
}
