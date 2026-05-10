package th.co.ais.dt.core.service.core.interfaces.report;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.entity.cm.LocationMst;

@Transactional
public interface TDRSK012ReportService {
	public List<LocationMst> queryLocationCodeSaleAtShop(Object listCompany, String locationCode);

}
