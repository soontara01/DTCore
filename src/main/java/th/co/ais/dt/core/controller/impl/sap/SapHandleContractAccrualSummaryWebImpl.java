package th.co.ais.dt.core.controller.impl.sap;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.controller.dto.SapContractBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractAccrualSummaryService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleTransactionContractPickingService;
import th.co.ais.dt.entity.sap.DtSapAccrualGroupHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.sap.DtSapContractTransLog;
import th.co.ais.dt.entity.sap.DtSapUniversalHeader;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.TdEtlParameter;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualGroupHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.repository.interfaces.util.ITdEtlParameterDao;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.TDMDataUtility;

@RestController
@Slf4j
@AllArgsConstructor
public class SapHandleContractAccrualSummaryWebImpl {

	final String prefixPath = "api/sap-handle-contract-type/v1";
	
	private ITdEtlParameterDao tdEtlParameterDao;
	private IDtSapAccrualGroupHeaderDao dtSapAccrualHeaderGroupDao;
	private ISapHandleContractAccrualSummaryService sapHandleContractAccrualSummaryService;
	private final ILovMasterDao lovMasterDao;
		
	@RequestMapping(value = prefixPath + "/accrual-summary", method = RequestMethod.GET, produces = {"application/json" })
	public ResponseEntity<String> handleContractAccrualSummary() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		CommonResponseBean res = new CommonResponseBean();
		Gson gson = new Gson();
		
		try {
			// query td_etl_parameter job_name ='ACCRUAL_CONTRACT'
			List<TdEtlParameter> listTdEtlParameter = tdEtlParameterDao.listTdEtlParameterByJobName("ACCRUAL_CONTRACT");
			TdEtlParameter tdEtlParameter = null;
			if(BeanUtil.isNotEmpty(listTdEtlParameter) && BeanUtil.isNotEmpty(listTdEtlParameter.get(0).getInputPar1())) {
				tdEtlParameter = listTdEtlParameter.get(0);
				String runTime = tdEtlParameter.getInputPar1();
				
				List<QueryContractAccrualBean> listQueryContractAccrualBean = null;
				//check lov เคสซ่อม
				List<LovMaster> lovReRun =  lovMasterDao.listLovMasterByCriteria("SAP_S4_ACCRUAL_RESEND", "DOC_RESEND", "Y");
				if(BeanUtil.isEmpty(lovReRun)||lovReRun.size()==0) {
					//call Insert Data ลง Temp Table 
					String insertStatus = sapHandleContractAccrualSummaryService.insertAccrualSummary(runTime);
					//query data to post sap accrual
					listQueryContractAccrualBean = dtSapAccrualHeaderGroupDao.queryTempContactAccrualSummary(runTime);
				} else {
					runTime = "RESEND";
					// query data เคสซ่อม
					listQueryContractAccrualBean = dtSapAccrualHeaderGroupDao.queryTempContactAccrualSummaryReSend();
				}
				//call sap accrual
				if(BeanUtil.isNotEmpty(listQueryContractAccrualBean)) {
					List<DtSapAccrualGroupHeader> dtSapAccrualHeaderRes = sapHandleContractAccrualSummaryService.prepareDataCallAccrualSummaryApi(listQueryContractAccrualBean,runTime);
					// update DtSapAccrualGroupHeader
					try {
						sapHandleContractAccrualSummaryService.updateDtSapAccrualGroupHeader(dtSapAccrualHeaderRes);
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				} 
				
				if(BeanUtil.isEmpty(lovReRun)||lovReRun.size()==0) {
					//update Table : td_etl_parameter job_name ='ACCRUAL_CONTRACT'
					Date timeRun = TDMDataUtility.convertStringToDateByFormat(tdEtlParameter.getInputPar1(), TDMDataUtility.SIMPLE_DATE_FORMAT_YYYYMMDD);
					Date timeNext = TDMDataUtility.shiftDateUp(timeRun,1);
					String timeRunUpdate = TDMDataUtility.toStringEngDateSimpleFormatYYYYMMDD(timeNext);
					tdEtlParameter.setInputPar1(timeRunUpdate);
					sapHandleContractAccrualSummaryService.updateTdEtlParameter(tdEtlParameter);
				}
				
				res.setResultCode("20000");
				res.setResultDescription("Success");
			} else {
				res.setResultCode("50000");
				res.setResultDescription("Please set input_par1(YYYYMMDD) in td_etl_parameter job_name ='ACCRUAL_CONTRACT'");
			}

		} catch (Exception e) {
			log.error("Error handleContractAccrualSummary");
			e.printStackTrace();
			res.setResultCode("50000");
			res.setResultDescription("Error handleContractAccrualSummary : System error");
			
		}

		return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
	}
}
