package th.co.ais.dt.core.service.core.interfaces.sap;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.entity.sap.DtSapAccrualGroupHeader;
import th.co.ais.dt.entity.util.TdEtlParameter;

@Transactional
public interface ISapHandleContractAccrualSummaryService {
	public String insertAccrualSummary(String summaryDate) throws DataAccessException;
	public List<DtSapAccrualGroupHeader> prepareDataCallAccrualSummaryApi(List<QueryContractAccrualBean> listQueryContractAccrualBean,String runTime);
	public void updateDtSapAccrualGroupHeader(List<DtSapAccrualGroupHeader> dtSapAccrualGroupHeaders);
	public void updateTdEtlParameter(TdEtlParameter tdEtlParameter);
}
