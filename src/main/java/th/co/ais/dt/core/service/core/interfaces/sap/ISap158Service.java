package th.co.ais.dt.core.service.core.interfaces.sap;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.core.service.core.impl.sap.dto.PickingDocumentIn;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap158Bean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap158Request;

@Transactional
public interface ISap158Service {

	PickingDocumentIn createPickingDocumentRequest(Sap158Bean sap158Bean);
	String callPickingDocumentListOnPrem(PickingDocumentIn pickingDocumentIn);
	void insertGoodsIssueStatusUpdate(Sap158Request request) throws DataAccessException;
	void insertDtSapGoodsIssueOrder(Sap158Request request) throws DataAccessException;

}
