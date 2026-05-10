package th.co.ais.dt.core.service.core.interfaces.email;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IEmailService {
	public List<Object[]>  queryEmail(String processGroup) throws DataAccessException ;
	public List<Object[]> queryEmail(String processGroup, String processCode) throws DataAccessException;
	public List<Object[]>  queryPmsTdmToSap() throws DataAccessException ;
}
