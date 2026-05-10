package th.co.ais.dt.core.repository.interfaces.report;

import java.util.List;

import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019DataItemBean;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019SummarySaleVolumeReportBean;

public interface ITDRSO019SummarySaleVolumeReportDao {
	List<TDRSO019DataItemBean> getDataLocationTypeList() throws Exception;

	List<TDRSO019DataItemBean> getDataLocationSubTypeList(TDRSO019SummarySaleVolumeReportBean request) throws Exception;

	List<TDRSO019DataItemBean> getDataLocationRegionList(TDRSO019SummarySaleVolumeReportBean request) throws Exception;

	List<TDRSO019DataItemBean> getDataDealerDisChannelList() throws Exception;

	List<TDRSO019DataItemBean> getDataDealerRegionList(TDRSO019SummarySaleVolumeReportBean request) throws Exception;

	List<TDRSO019DataItemBean> getDataForLocation(TDRSO019SummarySaleVolumeReportBean request) throws Exception;

	List<TDRSO019DataItemBean> getDataForDealer(TDRSO019SummarySaleVolumeReportBean request) throws Exception;
}
