package th.co.ais.dt.core.service.core.impl.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019DataItemBean;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO019SummarySaleVolumeReportBean;
import th.co.ais.dt.core.repository.interfaces.report.ITDRSO019SummarySaleVolumeReportDao;
import th.co.ais.dt.core.service.core.interfaces.report.IQueryTDRSO019SummarySaleVolumeReportService;
import th.co.ais.dt.entity.iv.PmCompany;
import th.co.ais.dt.repository.interfaces.iv.IPmCompanyDao;
import th.co.ais.dt.util.BeanUtil;

@Service
@AllArgsConstructor
public class QueryTDRSO019SummarySaleVolumeReportServiceImpl implements IQueryTDRSO019SummarySaleVolumeReportService {

	private final IPmCompanyDao pmCompanyDao;
	private final ITDRSO019SummarySaleVolumeReportDao tdrso019Dao;

	@Override
	public TDRSO019SummarySaleVolumeReportBean getDataCriteria(TDRSO019SummarySaleVolumeReportBean request) {
		TDRSO019SummarySaleVolumeReportBean response = new  TDRSO019SummarySaleVolumeReportBean();
		try {
			response.setResultCode("20000");
			response.setResultDescription("Success");
			response.setDeveloperMessage("Success");
			response.setCompanyList(getDataAndPrepareCompanyList());
			response.setLocationTypeList(tdrso019Dao.getDataLocationTypeList());
			response.setLocationSubTypeList(tdrso019Dao.getDataLocationSubTypeList(request));
			response.setLocationRegionList(tdrso019Dao.getDataLocationRegionList(request));
			response.setDealerDisChannelList(tdrso019Dao.getDataDealerDisChannelList());
			response.setDealerRegionList(tdrso019Dao.getDataDealerRegionList(request));
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
			response.setDeveloperMessage(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
		}
		return response;
	}
	
	@Override
	public TDRSO019SummarySaleVolumeReportBean validateAndGetDataForLocation(TDRSO019SummarySaleVolumeReportBean request) {
		TDRSO019SummarySaleVolumeReportBean response = new  TDRSO019SummarySaleVolumeReportBean();
		try {
			validateInputLocation(request);
			response.setResultCode("20000");
			response.setResultDescription("Success");
			response.setDeveloperMessage("Success");
			response.setLocationList(tdrso019Dao.getDataForLocation(request));
		} catch (ValidationException ve) {
			response.setResultCode("50000");
			response.setResultDescription(ve.getMessage());
			response.setDeveloperMessage(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
			response.setDeveloperMessage(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
		}
		return response;
	}

	@Override
	public TDRSO019SummarySaleVolumeReportBean validateAndGetDataForDealer(TDRSO019SummarySaleVolumeReportBean request) {
		TDRSO019SummarySaleVolumeReportBean response = new  TDRSO019SummarySaleVolumeReportBean();
		try {
			validateInputDealer(request);
			response.setDealerList(tdrso019Dao.getDataForDealer(request));
			response.setResultCode(response.getDealerList().isEmpty() ? "50000" : "20000");
			response.setResultDescription(response.getDealerList().isEmpty() ? "Data not Found" : "Success");
			response.setDeveloperMessage(response.getDealerList().isEmpty() ? "Data not Found" : "Success");
		} catch (ValidationException ve) {
			response.setResultCode("50000");
			response.setResultDescription(ve.getMessage());
			response.setDeveloperMessage(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
			response.setDeveloperMessage(BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage());
		}
		return response;
	}

	private void validateInputLocation(TDRSO019SummarySaleVolumeReportBean request) {
		if(BeanUtil.isEmpty(request.getLocationCode()) && BeanUtil.isEmpty(request.getLocationName())) {
			throw new ValidationException("Please input locationCode or locationName");
		}
	}

	private void validateInputDealer(TDRSO019SummarySaleVolumeReportBean request) {
		if(BeanUtil.isEmpty(request.getDealerCode()) && BeanUtil.isEmpty(request.getCompanyName())) {
			throw new ValidationException("Please input dealerCode or companyName");
		}
	}

	private List<TDRSO019DataItemBean> getDataAndPrepareCompanyList() {
		List<TDRSO019DataItemBean> dataList = new ArrayList<TDRSO019DataItemBean>();
		List<PmCompany> companyList = pmCompanyDao.getAllCompany();
		for (PmCompany pmCompany : companyList) {
			TDRSO019DataItemBean element = new TDRSO019DataItemBean();
			element.setCode(pmCompany.getCompanyAbbr());
			element.setValue(pmCompany.getCompanyAbbr());
			dataList.add(element);
		}
		return dataList;
	}
}
