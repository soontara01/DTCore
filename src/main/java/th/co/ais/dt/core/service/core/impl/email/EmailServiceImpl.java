package th.co.ais.dt.core.service.core.impl.email;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.repository.interfaces.email.IEmailDao;
import th.co.ais.dt.core.service.core.interfaces.email.IEmailService;

@Service
@Slf4j
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {
	
	private final IEmailDao emailDao ;
	
	@Override
	public List<Object[]> queryEmail(String processGroup) throws DataAccessException {
		return emailDao.queryEmail(processGroup);
	}
	
	@Override
	public List<Object[]> queryEmail(String processGroup, String processCode) throws DataAccessException {
		return emailDao.queryEmail(processGroup, processCode);
	}

	@Override
	public List<Object[]> queryPmsTdmToSap() throws DataAccessException {
		return emailDao.queryPmsTdmToSap();
	}

}
