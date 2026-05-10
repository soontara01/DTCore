package th.co.ais.dt.core.repository.interfaces.email;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.dt.entity.so.DtTest1;
import th.co.ais.dt.repository.dao.hibernate.util.IGenericDao;

public interface IEmailDao extends IGenericDao<DtTest1, Long> {
	public List<Object[]>  queryEmail(String processGroup) throws DataAccessException ;
	public List<Object[]>  queryPmsTdmToSap() throws DataAccessException ;
	public List<Object[]> queryEmail(String processGroup, String processCode) throws DataAccessException;
}
