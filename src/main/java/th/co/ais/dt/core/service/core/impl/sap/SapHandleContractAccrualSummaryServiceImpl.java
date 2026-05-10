package th.co.ais.dt.core.service.core.impl.sap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.QueryContractAccrualBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapAccrualResponse;
import th.co.ais.dt.core.service.core.impl.sap.dto.DtSapAccrualResponseItem;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleContractAccrualSummaryService;
import th.co.ais.dt.entity.sap.DtSapAccrualGroupHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualGroupItem;
import th.co.ais.dt.entity.sap.DtSapAccrualHeader;
import th.co.ais.dt.entity.sap.DtSapAccrualItem;
import th.co.ais.dt.entity.sap.DtSapContractTrans;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.entity.util.TdEtlParameter;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.iv.ILocMapPlantDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualGroupHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualGroupItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapAccrualItemDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTransLogDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapContractTypeDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalHeaderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapUniversalItemDao;
import th.co.ais.dt.repository.interfaces.util.ITdEtlParameterDao;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.util.BeanUtil;

@Service
@Slf4j
@AllArgsConstructor
public class SapHandleContractAccrualSummaryServiceImpl implements ISapHandleContractAccrualSummaryService{
	
	IDtSapContractTransDao dtSapContractTransDao;
	IDtSapContractTypeDao dtSapContractTypeDao;
	IDtSapContractTransLogDao dtSapContractTransLogDao;
	IDtSapAccrualGroupHeaderDao dtSapAccrualGroupHeaderDao;
	IDtSapAccrualGroupItemDao dtSapAccrualGroupItemDao;
	IDtSapAccrualItemDao dtSapAccrualItemDao;
	private ITdEtlParameterDao tdEtlParameterDao;
//	IDtSapUniversalHeaderDao dtSapUniversalHeaderDao;
//	IDtSapUniversalItemDao dtSapUniversalItemDao;
	ILocMapPlantDao locMapPlantDao;
	private final ISapCallApiService sapCallApiService;
	private final DTConfig dTConfig;
	
	@Override
	public String insertAccrualSummary(String summaryDate) throws DataAccessException {
		String inserStatus = null;
		try {
			// call Insert Table : dt_sap_accrual_group_item
			ProcessResult item = dtSapAccrualGroupItemDao.insertItemContactAccrualSummary(summaryDate);
			
			// call Insert Table : dt_sap_accrual_group_header
			ProcessResult header = dtSapAccrualGroupHeaderDao.insertHeaderContactAccrualSummary(summaryDate);
			inserStatus = "S";
			return inserStatus;
			
		} catch (Exception e) {
			inserStatus = "F";
			log.error("Error : insertAccrualSummary ");
			e.printStackTrace();
			
		}
		return inserStatus;
	}
	
	@Override
	public List<DtSapAccrualGroupHeader> prepareDataCallAccrualSummaryApi(List<QueryContractAccrualBean> listQueryContractAccrualBean,String runTime) {
		List<DtSapAccrualGroupHeader> listDtSapAccrualGroupHeader = new ArrayList<DtSapAccrualGroupHeader>();
		try {
			if(BeanUtil.isNotEmpty(listQueryContractAccrualBean)) {
				DtSapAccrualHeader sapAccrualHeader = null;
				if(runTime.equals("RESEND")) {
					List<String> partnermessageIds = listQueryContractAccrualBean.stream().map(QueryContractAccrualBean::getPartnermessageid).distinct().collect(Collectors.toList());
					listDtSapAccrualGroupHeader = dtSapAccrualGroupHeaderDao.queryDtSapAccrualHeaderGroupByPartnermessageid(partnermessageIds);
				}
				else {
//					List<String> partnermessageids = listQueryContractAccrualBean.stream().map(QueryContractAccrualBean::getPartnermessageid).distinct().collect(Collectors.toList());
					listDtSapAccrualGroupHeader = dtSapAccrualGroupHeaderDao.queryDtSapAccrualHeaderGroupByprocessDate(runTime);
				}
					for(DtSapAccrualGroupHeader groupHeader:listDtSapAccrualGroupHeader) {
			        	List<DtSapAccrualItem> sapAccrualItems = new ArrayList<DtSapAccrualItem>();
			        	for(QueryContractAccrualBean queryContractAccrualBean:listQueryContractAccrualBean) {
			        		if(groupHeader.getPartnermessageid().equals(queryContractAccrualBean.getPartnermessageid())) {
			        			sapAccrualHeader = setDtSapAccrualHeader(queryContractAccrualBean);
			        			sapAccrualItems.add(setDtSapAccrualItem(queryContractAccrualBean,sapAccrualHeader));							
			        		}
			        	}
			        	// call sap Accrual
			        	DtSapAccrualResponse ress = sapCallApiService.callAccrualSummaryApi(groupHeader,sapAccrualHeader,sapAccrualItems);
						
			        	//update data AccrualGroupHeader
			        	if(ress != null && BeanUtil.isNotEmpty(ress.getItem()) && ress.getItem().size()>0) {
//							dtSapAccrualGroupHeader.setStatus(ress.getStatus());
			        		groupHeader.setResMessagetype(ress.getMessageType());
			        		groupHeader.setResAccrualObjectNumber(ress.getAccrualObject());
			        		groupHeader.getCreateValue().setLastUpd(new Date());
			        		
							if(BeanUtil.isNotEmpty(ress.getItem())) {
								groupHeader.setResAccrualObjectNumber(ress.getItem().get(0).getAccrualObjectNumber());
							}
							
							if(BeanUtil.isNotEmpty(ress.getMessageType()) && ress.getMessageType().equals("S") ) {
								groupHeader.setStatus("S");
							}else if(BeanUtil.isNotEmpty(ress.getMessageType()) && !ress.getMessageType().equals("S")) {
								groupHeader.setStatus("F");
							}else {
								groupHeader.setStatus("R");
							}
							
						  	//update data AccrualGroupItem
							for(DtSapAccrualResponseItem resItem:ress.getItem()) {
								ProcessResult result = dtSapAccrualGroupItemDao.updateDtSapAccrualGroupItemAfterCallSapAccrual(resItem.getMessageType(), resItem.getAccrualObjectNumber(), resItem.getDocumentNumber(), ress.getPartnerMessageID());
							}
			        	}
			        }
			}
		} catch (Exception e) {
			log.error("Error : prepareDataCallAccrualSummaryApi");
			e.printStackTrace();
			// TODO: handle exception
		}
		return listDtSapAccrualGroupHeader;
	}
	
	private DtSapAccrualHeader setDtSapAccrualHeader(QueryContractAccrualBean queryContractAccrualBean) {
		DtSapAccrualHeader dtSapAccrualHeader = null;
		try {
			Date date = new Date();
			MasterValue masterValue = new MasterValue();
			masterValue.setCreated(date);
			masterValue.setCreatedBy("DTAPP");
			masterValue.setLastUpd(date);
			masterValue.setLastUpdBy("DTAPP");
			//hearder
			dtSapAccrualHeader = new DtSapAccrualHeader();
//			dtSapAccrualHeader.setSapContractId("");
			dtSapAccrualHeader.setPartnername(queryContractAccrualBean.getPartnername());
			dtSapAccrualHeader.setPartnermessageid(queryContractAccrualBean.getPartnermessageid());
			dtSapAccrualHeader.setCompanycode(queryContractAccrualBean.getCompanycode());
			dtSapAccrualHeader.setAccrualobjectcategory(queryContractAccrualBean.getAccrualobjectcategory());
			dtSapAccrualHeader.setAccrualobjectsubcategory(queryContractAccrualBean.getAccrualobjectsubcategory());
			dtSapAccrualHeader.setCreateValue(masterValue);
//			dtSapAccrualHeader.setStatus("W");
			dtSapAccrualHeader.setResMessagetype(null);
			dtSapAccrualHeader.setResAccrualObjectNumber(null); 
		} catch (Exception e) {
			log.error("Error : setDtSapAccrualHeader");
			e.printStackTrace();
			// TODO: handle exception
		}
		return dtSapAccrualHeader;
	}
	
	private DtSapAccrualItem setDtSapAccrualItem(QueryContractAccrualBean queryContractAccrualBean,DtSapAccrualHeader sapAccrualHeader) {
		DtSapAccrualItem dtSapAccrualItem = null;
		try {
				dtSapAccrualItem = new DtSapAccrualItem();
//				dtSapAccrualItem.setSapAccrualHeaderId("");
				dtSapAccrualItem.setPersonResponsible(queryContractAccrualBean.getPersonresponsible());
				dtSapAccrualItem.setText(queryContractAccrualBean.getItemtext());
				dtSapAccrualItem.setStartofLife(queryContractAccrualBean.getStartoflife());
				dtSapAccrualItem.setEndofLife(queryContractAccrualBean.getEndoflife());
				dtSapAccrualItem.setAccrualItemType(queryContractAccrualBean.getAccrualitemtype());
				dtSapAccrualItem.setAccrualDeferralMethod(queryContractAccrualBean.getAccrualdeferralmethod());
				dtSapAccrualItem.setTotalAccrAmountinTransCrcy(queryContractAccrualBean.getTotalaccramountintranscrcy());
				dtSapAccrualItem.setTransactionCurrency(queryContractAccrualBean.getTransactioncurrency());
				dtSapAccrualItem.setProfitCenter(queryContractAccrualBean.getProfitcenter());
				dtSapAccrualItem.setSegment(queryContractAccrualBean.getSegment());
				dtSapAccrualItem.setDocumentNumber(queryContractAccrualBean.getDocumentnumber());
				dtSapAccrualItem.setIMEInumber(queryContractAccrualBean.getImeinumber());
				dtSapAccrualItem.setMaterialDescription(queryContractAccrualBean.getMaterialdescription());
				dtSapAccrualItem.setMobilePhoneNumber(queryContractAccrualBean.getMobilephonenumber());
				dtSapAccrualItem.setReserveField(queryContractAccrualBean.getReservefield());
				dtSapAccrualItem.setUSSDCode(queryContractAccrualBean.getUssdcode());
				dtSapAccrualItem.setProductnumber(queryContractAccrualBean.getProductnumber());
				dtSapAccrualItem.setPlant(queryContractAccrualBean.getPlant());
				dtSapAccrualItem.setProfitCenterCOPA(queryContractAccrualBean.getProfitcentercopa());
				dtSapAccrualItem.setProjectUSSDCode(queryContractAccrualBean.getProjectussdcode());
				dtSapAccrualItem.setSalesOrganization(queryContractAccrualBean.getSalesorganization());
				dtSapAccrualItem.setDistributionChannel(queryContractAccrualBean.getDistributionchannel());
				dtSapAccrualItem.setDivision(queryContractAccrualBean.getDivision());
				dtSapAccrualItem.setCreateValue(sapAccrualHeader.getCreateValue());
				dtSapAccrualItem.setOpeningPostingDate(queryContractAccrualBean.getOpeningpostingdate());
				
		} catch (Exception e) {
			log.error("Error : setDtSapAccrualItem");
			e.printStackTrace();
			// TODO: handle exception
		}
		return dtSapAccrualItem;
	}
	
	@Override
	public void updateDtSapAccrualGroupHeader(List<DtSapAccrualGroupHeader> dtSapAccrualGroupHeaders) {
		try {
			dtSapAccrualGroupHeaderDao.updateList(dtSapAccrualGroupHeaders);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	@Override
	public void updateTdEtlParameter(TdEtlParameter tdEtlParameter) {
		try {
			tdEtlParameterDao.update(tdEtlParameter);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
}
