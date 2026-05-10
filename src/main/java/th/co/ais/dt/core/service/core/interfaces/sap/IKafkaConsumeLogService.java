package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.sap.KafksConsumeLog;

@Transactional
public interface IKafkaConsumeLogService {
	public void insertLog(KafksConsumeLog kafksConsumeLog) ;
	public void  clearLog() throws DataAccessException ;
	public KafksConsumeLog getKafksConsumeLog(Long tableId) ;
	public List<Long> getListGoodsIssueError(String limitResult);
	public void updateResponse(Long tableId, String response);
    public void  deletetablebatchjobexecution() throws DataAccessException ;
	public void  deletetablebatchjobexecutioncontext() throws DataAccessException ;
	public void  deletetablebatchjobexecutionparams() throws DataAccessException ;
	public void  deletetablebatchjobinstance() throws DataAccessException ;
	public void  deletetablebatchbatchstepexecution() throws DataAccessException ;
	public void  deletetablebatchstepexecutioncontext() throws DataAccessException ;

}
