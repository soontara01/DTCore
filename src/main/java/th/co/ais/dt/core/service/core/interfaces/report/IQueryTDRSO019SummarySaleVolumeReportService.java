package th.co.ais.dt.core.service.core.interfaces.report;

import org.springframework.transaction.annotation.Transactional;

import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019SummarySaleVolumeReportBean;

@Transactional
public interface IQueryTDRSO019SummarySaleVolumeReportService {

	public TDRSO019SummarySaleVolumeReportBean getDataCriteria(TDRSO019SummarySaleVolumeReportBean request);

	public TDRSO019SummarySaleVolumeReportBean validateAndGetDataForLocation(TDRSO019SummarySaleVolumeReportBean request);

	public TDRSO019SummarySaleVolumeReportBean validateAndGetDataForDealer(TDRSO019SummarySaleVolumeReportBean request);

}
