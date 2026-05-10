package th.co.ais.dt.core.service.core.impl.report;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSK012Dao;
import th.co.ais.dt.core.service.core.interfaces.report.TDRSK012ReportService;
import th.co.ais.dt.entity.cm.LocationMst;

@Service
@AllArgsConstructor
public class TDRSK012ReportServiceImpl implements TDRSK012ReportService{
	private ITDRSK012Dao tdrsk012ReportDao;

	@Override
	public List<LocationMst> queryLocationCodeSaleAtShop(Object listCompany, String locationCode) {
		return tdrsk012ReportDao.queryLocationCodeSaleAtShop(listCompany, locationCode);
	}

}
