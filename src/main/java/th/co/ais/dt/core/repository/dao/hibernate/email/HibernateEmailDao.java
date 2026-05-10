package th.co.ais.dt.core.repository.dao.hibernate.email;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.repository.interfaces.email.IEmailDao;
import th.co.ais.dt.entity.so.DtTest1;
import th.co.ais.dt.repository.dao.hibernate.util.HibernateGenericDao;
import th.co.ais.dt.util.BeanUtil;

@Repository
@Slf4j
public class HibernateEmailDao extends HibernateGenericDao<DtTest1>  implements IEmailDao {
	public List<Object[]>  queryEmail(String processGroup) throws DataAccessException {
    	try {
    		
    		EntityManager entityManager = getSession(); 
     		    					
    		List<Object[]> el = entityManager.createNativeQuery(" select * from td_emailing_process tep where tep.process_group = :processGroup  ")
    				.setParameter("processGroup", processGroup)
    				.getResultList() ;
   
			return el;
    		
    	}catch (Exception e) {
			log.info(e.getMessage());
			return null ;
		}
    	
    }
	
	@Override
	public List<Object[]>  queryEmail(String processGroup, String processCode) throws DataAccessException {
    	try {
    		StringBuffer sql = new StringBuffer();
    		sql.append("select * from td_emailing_process tep where tep.process_group = :processGroup");
    		if(BeanUtil.isNotEmpty(processCode)) {
    			sql.append(" and tep.process_code = :processCode");
    		}
    		
    		EntityManager entityManager = getSession(); 
    		Query query = entityManager.createNativeQuery(sql.toString());
    		query.setParameter("processGroup", processGroup);
    		if(BeanUtil.isNotEmpty(processCode)) {
    			query.setParameter("processCode", processCode);
    		}
    		
			List<Object[]> el = query.getResultList() ;
   
			return el;
    		
    	}catch (Exception e) {
			log.info(e.getMessage());
			return null ;
		}
    	
    }
	
	
	public List<Object[]>  queryPmsTdmToSap() throws DataAccessException {
    	try {
    		
    		EntityManager entityManager = getSession(); 
    		
            StringBuffer sqlQ = new StringBuffer();
    		
            sqlQ.append("	select A.company,	");
            sqlQ.append("	A.location_code ,	");
            sqlQ.append("	A.process_dt,	");
            sqlQ.append("	A.receipt_dt,	");
            sqlQ.append("	A.receipt_no,	");
            sqlQ.append("	A.tdm_doc_ref,	");
            sqlQ.append("	A.sap_doc_no,	");
            sqlQ.append("	A.status,	");
            sqlQ.append("	A.message	");
            sqlQ.append("	from (	");
            sqlQ.append("	select distinct cast(h.count_doc as text) count_doc,	");
            sqlQ.append("	h.xblnr,	");
            sqlQ.append("	h.company, 	");
            sqlQ.append("	h.location_code , 	");
            sqlQ.append("	to_char(h.process_dt, 'dd/mm/yyyy') as process_dt, 	");
            sqlQ.append("	to_char(h.receipt_dt, 'dd/mm/yyyy') as receipt_dt, 	");
            sqlQ.append("	d.doc_no as receipt_no, 	");
            sqlQ.append("	h.xblnr as tdm_doc_ref, 	");
            sqlQ.append("	h.BELNR as sap_doc_no,	");
            sqlQ.append("	h.flag as status,	");
            sqlQ.append("	h.message	");
            sqlQ.append("	from dt_sap_data_figl_header h inner join	");
            sqlQ.append("	dt_sap_data_figl_detail d on h.running = d.running	");
            sqlQ.append("	and h.company = d.company	");
            sqlQ.append("	and h.process_dt = d.process_dt	");
            sqlQ.append("	where h.process_dt = date(current_timestamp - interval '1 DAY')	");
            //sqlQ.append("	where h.process_dt = to_date('27/03/2024','DD/MM/YYYY')	");
            sqlQ.append("	order by h.xblnr,h.company	");
            sqlQ.append("	) A	");


     		    					
    		List<Object[]> el = entityManager.createNativeQuery(sqlQ.toString())
    				//.setParameter("processGroup", processGroup)
    				.getResultList() ;
   
			return el;
    		
    	}catch (Exception e) {
			log.info(e.getMessage());
			return null ;
		}
    	
    }
}
