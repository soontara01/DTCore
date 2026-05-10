package th.co.ais.dt.core.repository.dao.hibernate.report;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019DataItemBean;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019SummarySaleVolumeReportBean;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSO019SummarySaleVolumeReportDao;
import th.co.ais.dt.service.encryption.impl.EncryptionService;
import th.co.ais.dt.util.BeanUtil;

@Repository
@Slf4j
@RequiredArgsConstructor
public class HibernateTDRSO019SummarySaleVolumeReportDao implements ITDRSO019SummarySaleVolumeReportDao {

	private final EncryptionService encryptionService;

	@PersistenceContext
	private EntityManager entityManager;

	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@Override
	public List<TDRSO019DataItemBean> getDataLocationTypeList() {
		Session session = getSession();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CLM.LOC_TYPE AS code, CLM.LOC_TYPE AS value ");
		sql.append("FROM TD_SK_LOC_MAP_PLANT LMP ");
		sql.append("JOIN   CM_LOCATION_MASTER CLM ON CLM.LOCATION_CODE = LMP.LOCATION_CODE ");
		sql.append("WHERE  CLM.LOC_TYPE IS NOT NULL ");
		sql.append("ORDER BY  CLM.LOC_TYPE ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("code", StandardBasicTypes.STRING);
		query.addScalar("value", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));
		return query.list();
	}

	@Override
	public List<TDRSO019DataItemBean> getDataLocationSubTypeList(TDRSO019SummarySaleVolumeReportBean request) {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CLM.LOC_SUBTYPE AS code, CLM.LOC_SUBTYPE AS value ");
		sql.append("FROM TD_SK_LOC_MAP_PLANT LMP ");
		sql.append("JOIN   CM_LOCATION_MASTER CLM ON CLM.LOCATION_CODE = LMP.LOCATION_CODE ");
		sql.append("WHERE  CLM.LOC_SUBTYPE IS NOT NULL ");
		if(BeanUtil.isNotEmpty(request.getCompany())) {
			sql.append("AND  LMP.COMPANY = :company ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			sql.append("AND UPPER(CLM.LOC_TYPE)  = :locationType ");
		}
		sql.append("ORDER BY  CLM.LOC_SUBTYPE ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("code", StandardBasicTypes.STRING);
		query.addScalar("value", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));
		if(BeanUtil.isNotEmpty(request.getCompany())) {
			query.setParameter("company", request.getCompany());
		}
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			query.setParameter("locationType", request.getLocationType());
		}
		return query.list();
	}

	@Override
	public List<TDRSO019DataItemBean> getDataLocationRegionList(TDRSO019SummarySaleVolumeReportBean request) {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT A.REGION AS code, A.REGION AS value ");
		sql.append("FROM CM_LOCATION_MASTER A ");
		sql.append("WHERE A.LOC_TYPE IS NOT NULL ");
		sql.append("AND A.LOC_SUBTYPE IS NOT NULL ");
		sql.append("AND A.REGION IS NOT NULL ");
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			sql.append("AND UPPER(A.LOC_TYPE) = :locationType ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationSubType())) {
			sql.append("AND UPPER(A.LOC_SUBTYPE) IN :locationSubType ");
		}
		sql.append("ORDER BY  A.REGION ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("code", StandardBasicTypes.STRING);
		query.addScalar("value", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			query.setParameter("locationType", request.getLocationType());
		}
		if(BeanUtil.isNotEmpty(request.getLocationSubType())) {
			query.setParameterList("locationSubType", request.getLocationSubType());
		}

		return query.list();
	}

	@Override
	public List<TDRSO019DataItemBean> getDataDealerDisChannelList() {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT A.SAP_DIST_CHANNEL AS code, A.SAP_DIST_CHANNEL AS value ");
		sql.append("FROM TD_CHANNEL_SAP_MAP_LOC A ");
		sql.append("ORDER BY A.SAP_DIST_CHANNEL ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("code", StandardBasicTypes.STRING);
		query.addScalar("value", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));
		return query.list();
	}

	@Override
	public List<TDRSO019DataItemBean> getDataDealerRegionList(TDRSO019SummarySaleVolumeReportBean request) {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT A.REGION AS code, A.REGION AS value ");
		sql.append("FROM CM_DEALER_MASTER A ,TD_CHANNEL_SAP_MAP_LOC B  ");
		sql.append("WHERE A.TYPE = B.PARTNER_TYPE AND A.SUB_TYPE = B.PARTNER_SUBTYPE ");
		if(BeanUtil.isNotEmpty(request.getDisbutionChannel())) {
			sql.append("AND B.SAP_DIST_CHANNEL IN :disbutionChannel ");
		}
		sql.append("AND A.REGION IS NOT NULL ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("code", StandardBasicTypes.STRING);
		query.addScalar("value", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));
		if(BeanUtil.isNotEmpty(request.getDisbutionChannel())) {
			query.setParameterList("disbutionChannel", request.getDisbutionChannel());
		}
		return query.list();
	}

	@Override
	public List<TDRSO019DataItemBean> getDataForLocation(TDRSO019SummarySaleVolumeReportBean request) {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT LM.LOCATION_CODE AS locationCode, LM.LOCATION_NAME AS locationName ");
		sql.append("FROM CM_LOCATION_MASTER LM ");
		sql.append("WHERE 1 = 1 ");
		sql.append("AND (  LM.AIS_STATUS = 'Active Partner' OR  LM.DPC_TRAD_STATUS = 'Active Partner') ");
		sql.append("AND LM.PAR_OU_ID IS NOT NULL ");
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			sql.append("AND UPPER(LM.LOC_TYPE) = :locationType ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationSubType())) {
			sql.append("AND UPPER(LM.LOC_SUBTYPE) IN :locationSubType ");		
		}
		if(BeanUtil.isNotEmpty(request.getLocationRegion())) {
			sql.append("AND UPPER(LM.REGION) = :locationRegion ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationCode())) {
		    sql.append("AND CAST(LM.LOCATION_CODE AS TEXT) LIKE :locationCode ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationName())) {
			sql.append("AND LM.LOCATION_NAME LIKE :locationName ");
		}
		sql.append("ORDER BY LM.LOCATION_CODE ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("locationCode", StandardBasicTypes.STRING);
		query.addScalar("locationName", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));
		if(BeanUtil.isNotEmpty(request.getLocationType())) {
			query.setParameter("locationType", request.getLocationType());
		}
		if(BeanUtil.isNotEmpty(request.getLocationSubType())) {
			query.setParameterList("locationSubType", request.getLocationSubType());	
		}
		if(BeanUtil.isNotEmpty(request.getLocationRegion())) {
			query.setParameter("locationRegion", request.getLocationRegion());
		}
		if(BeanUtil.isNotEmpty(request.getLocationCode())) {
			query.setParameter("locationCode", "%"+request.getLocationCode()+"%");
		}
		if(BeanUtil.isNotEmpty(request.getLocationName())) {
			query.setParameter("locationName", "%"+request.getLocationName()+"%");
		}
		return query.list();
	}

	@Override
	public List<TDRSO019DataItemBean> getDataForDealer(TDRSO019SummarySaleVolumeReportBean request) {
		Session session = getSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.DEALER_CODE AS dealerCode, ");
		sql.append(encryptionService.getDecryptColumnQuery("A.COMPANY_NAME", "companyName") );
		sql.append("FROM CM_DEALER_MASTER A ");
		sql.append("WHERE 1 = 1 ");
		if(BeanUtil.isNotEmpty(request.getDisbutionChannel())) {
			sql.append("AND A.DIST_CHANNEL IN :disLocationChannel ");
		}
		if(BeanUtil.isNotEmpty(request.getLocationRegion())) {
			sql.append("AND A.REGION = :locationRegion ");		
		}
		if(BeanUtil.isNotEmpty(request.getDealerCode())) {
			sql.append("AND A.DEALER_CODE LIKE :dealerCode ");
		}
		if(BeanUtil.isNotEmpty(request.getCompanyName())) {
			sql.append("AND	")
					.append(encryptionService.getDecryptColumnWhere("A.COMPANY_NAME"))
					.append(" LIKE :companyName	");
		}
		sql.append("ORDER BY A.DEALER_CODE ");
		
		NativeQuery<TDRSO019DataItemBean> query = session.createNativeQuery(sql.toString());
		query.addScalar("dealerCode", StandardBasicTypes.STRING);
		query.addScalar("companyName", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(TDRSO019DataItemBean.class));

		query.setParameter("dataEncryptionKey", encryptionService.getDataEncryptionKey());
		
		if(BeanUtil.isNotEmpty(request.getDisbutionChannel())) {
			query.setParameterList("disLocationChannel", request.getDisbutionChannel());
		}
		if(BeanUtil.isNotEmpty(request.getLocationRegion())) {
			query.setParameter("locationRegion", request.getLocationRegion());	
		}
		if(BeanUtil.isNotEmpty(request.getDealerCode())) {
			query.setParameter("dealerCode", "%"+request.getDealerCode()+"%");
		}
		if(BeanUtil.isNotEmpty(request.getCompanyName())) {
			query.setParameter("companyName", "%"+request.getCompanyName()+"%");
		}
		return query.list();
	}

}
