package th.co.ais.dt.core.controller.impl.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO020SummaryPreBookingReportBean;
import th.co.ais.dt.core.controller.impl.report.dto.TDRSO020SummaryPreBookingReportBean.DataCriteriaBean;
import th.co.ais.dt.core.util.HttpClientUtil;
import th.co.ais.dt.entity.iv.PmCompany;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.service.core.interfaces.pr.IPmCompanyService;
import th.co.ais.dt.service.core.interfaces.util.LovMasterService;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/report/v1/TDRSO020")
@Slf4j
@AllArgsConstructor
public class QueryTDRSO020SummaryPreBookingReportWeb {

	private IPmCompanyService iPmCompanyService;
	private LovMasterService iLovMasterService;
	
	@PostMapping(value = "query-criteria")
	public ResponseEntity<String> queryCriteria(){
		
		TDRSO020SummaryPreBookingReportBean response = new TDRSO020SummaryPreBookingReportBean();
		final HttpHeaders httpHeaders = HttpClientUtil.getHttpHeaders();
		Gson gson = new Gson();
		
		try {
			List<DataCriteriaBean> companyList = prepareCompanyList(iPmCompanyService.getAllCompany());
			List<DataCriteriaBean> statusReportTypeList = prepareDataCriteria(iLovMasterService.listLovMasterByCriteria("SUMMARY_PRE_STATUS", null, null, null, "Y", null, null, null, null, null));
			List<DataCriteriaBean> channelTypeList = prepareDataCriteria(iLovMasterService.listLovMasterByCriteria("SALE_CHANNEL", null, null, null, "Y", null, null, null, null, null));
			List<DataCriteriaBean> documentTypeList = prepareDataCriteria(iLovMasterService.listLovMasterByCriteria("DOCUMENT_TYPE_PRE", null, null, null, "Y", null, null, null, null, null));
			response.setResultCode("20000");
			response.setResultDescription("Success");
			response.setDeveloperMessage("Success");
			response.setCompanyList(companyList);
			response.setStatusReportTypeList(statusReportTypeList);
			response.setChannelTypeList(channelTypeList);
			response.setDocumentTypeList(documentTypeList);
		} catch (Exception e) {
			String error = BeanUtil.isEmpty(e.getMessage()) ? "System error" : e.getMessage();
			e.printStackTrace();
			response.setResultCode("50000");
			response.setResultDescription(error);
			response.setDeveloperMessage(error);
		}
		return new ResponseEntity<String>(gson.toJson(response), httpHeaders, HttpStatus.OK);

	}
	
	private List<DataCriteriaBean> prepareCompanyList(List<PmCompany> dataList) {
		List<DataCriteriaBean> prepareList = new ArrayList<DataCriteriaBean>();
		for (PmCompany item : dataList) {
			DataCriteriaBean element = new TDRSO020SummaryPreBookingReportBean().new DataCriteriaBean();
			element.setCode(item.getCompanyAbbr());
			element.setValue(item.getCompanyAbbr());
			prepareList.add(element);
		}
		return prepareList;
	}
	
	private List<DataCriteriaBean> prepareDataCriteria(List<LovMaster> dataList) {
		List<DataCriteriaBean> prepareList = new ArrayList<DataCriteriaBean>();
		for (LovMaster item : dataList) {
			DataCriteriaBean element = new TDRSO020SummaryPreBookingReportBean().new DataCriteriaBean();
			element.setCode(item.getLovCode());
			element.setValue(item.getDescription());
			prepareList.add(element);
		}
		return prepareList;
	}
	
}
