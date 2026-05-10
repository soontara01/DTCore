package th.co.ais.dt.core.service.core.impl.sap;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.interfaces.sap.IKafkaConsumeLogService;
import th.co.ais.dt.entity.sap.KafksConsumeLog;
import th.co.ais.dt.repository.interfaces.sap.IKafkaConsumeLogDao;
import th.co.ais.dt.repository.interfaces.sap.IKafkaProduceLogDao;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumeLogServiceImpl implements IKafkaConsumeLogService {
	
	private final IKafkaConsumeLogDao kafkaConsumeLogDao ; 
	private final IKafkaProduceLogDao kafkaProduceLogDao;
	
	public void insertLog(KafksConsumeLog kafksConsumeLog) {
		kafkaConsumeLogDao.insert(kafksConsumeLog);
	}

	@Override
	public void clearLog() throws DataAccessException {
		kafkaConsumeLogDao.clearLog();
		kafkaProduceLogDao.clearLog();
	}
	
	@Override
	public KafksConsumeLog getKafksConsumeLog(Long tableId) {
		return kafkaConsumeLogDao.getByLongPrimaryKey(tableId);
	}
	
	@Override
	public List<Long> getListGoodsIssueError(String limitResult) {
		return kafkaConsumeLogDao.getListGoodsIssueError(limitResult);
	}

	@Override
	public void updateResponse(Long tableId, String response) {
		kafkaConsumeLogDao.updateResponse(tableId, response);
	}

	@Override
	public void deletetablebatchjobexecution() throws DataAccessException {
		kafkaConsumeLogDao.deletetablebatchjobexecution();	
	}

	@Override
	public void deletetablebatchjobexecutioncontext() throws DataAccessException {
		kafkaConsumeLogDao.deletetablebatchjobexecutioncontext();	
	}

	@Override
	public void deletetablebatchjobexecutionparams() throws DataAccessException {
		kafkaConsumeLogDao.deletetablebatchjobexecutionparams();	
	}

	@Override
	public void deletetablebatchjobinstance() throws DataAccessException {
		kafkaConsumeLogDao.deletetablebatchjobinstance();	
	}

	@Override
	public void deletetablebatchbatchstepexecution() throws DataAccessException {
		kafkaConsumeLogDao.deletetablebatchbatchstepexecution();	
	}

	@Override
	public void deletetablebatchstepexecutioncontext() throws DataAccessException {
		kafkaConsumeLogDao.deletetablebatchstepexecutioncontext();
	}

}
