package th.co.ais.dt.core.service.core.impl.sap;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.impl.sap.dto.PickingDocumentIn;
import th.co.ais.dt.core.service.core.impl.sap.dto.PickingItem;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap158Bean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap158ItemBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap158Request;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap158Service;
import th.co.ais.dt.entity.sap.DtSapGoodsIssueOrder;
import th.co.ais.dt.entity.sap.DtSapGoodsIssueStatusUpdateHeader;
import th.co.ais.dt.entity.util.LovMaster;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.properties.DTConfig;
import th.co.ais.dt.repository.interfaces.sap.IDtSapGoodsIssueDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapGoodsIssueOrderDao;
import th.co.ais.dt.repository.interfaces.sap.IDtSapGoodsIssueStatusUpdateHeaderDao;
import th.co.ais.dt.repository.interfaces.util.ILovMasterDao;
import th.co.ais.dt.service.core.impl.sap.dto.QueryGoodsIssueBean;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.util.HttpClientUtilDT;
import th.co.ais.dt.util.TDMDataUtility;

@Service
@Slf4j
@RequiredArgsConstructor
public class Sap158ServiceImpl implements ISap158Service{

	private final ILovMasterDao lovMasterDao;
	private final IDtSapGoodsIssueDao dtSapGoodsIssueDao;
	private final DTConfig dTConfig;
	private final IDtSapGoodsIssueStatusUpdateHeaderDao goodsIssueStatusUpdateHeaderDao;
	private final IDtSapGoodsIssueOrderDao dtSapGoodsIssueOrderDao;
	public static final String DT_APP = "DTAPP";

	@Override
	public PickingDocumentIn createPickingDocumentRequest(Sap158Bean sap158Bean) {
		PickingDocumentIn pickReq = null;
		
		if(BeanUtil.isEmpty(sap158Bean.getCustomerPOTypeCode())) {
			throw new ForceTerminateException(-1, "CustomerPOTypeCode is ignore");
		}
		
		LovMaster lov = lovMasterDao.getLovMasterByUnique("SAP_S4_GOODS_ISSUE", "CUSTOMER_PO_TYPE", sap158Bean.getCustomerPOTypeCode());
		if(lov == null) {
			throw new ForceTerminateException(-1, "CustomerPOTypeCode is ignore");
		}
		
		lov = lovMasterDao.getLovMasterByUnique("SAP_S4_SALEORG_MAP_COMPANY", null, sap158Bean.getSalesOrganization());
		String company = lov.getLovSubType();
		List<QueryGoodsIssueBean> listDtItem = dtSapGoodsIssueDao.listItemForGoodsIssue(sap158Bean.getItem().get(0).getCustomerReference(), company);
		if(BeanUtil.isEmpty(listDtItem)) {
			throw new ForceTerminateException(-1, "Not found item to picking");
		}
		
		String locationCode = listDtItem.get(0).getLocationCode();
		String mac = "";
		LovMaster lovMac = lovMasterDao.getLovMasterByUnique("SAP_S4_GOODS_ISSUE_MAC_ADDRESS", locationCode, company);
		if(lovMac != null) {
			mac = lovMac.getLovVal() ;
			//throw new ForceTerminateException(-1, "Not found config lov 'SAP_S4_GOODS_ISSUE_MAC_ADDRESS'");
		}
		
		PickingItem pickItem = null;
		List<PickingItem> listPickItem = new ArrayList<PickingItem>();
		for (Sap158ItemBean sapItem : sap158Bean.getItem()) {
			if(BeanUtil.isNotEmpty(sapItem.getSerialNumber())) {
				//SerialNo
				String[] sapSerialNoArr = sapItem.getSerialNumber().split(",");
				String[] sapUniqueItemIdArr = BeanUtil.isNotEmpty(sapItem.getUniqueItemIdentifier()) ? sapItem.getUniqueItemIdentifier().split(",") : null;
				for (QueryGoodsIssueBean dtItem : listDtItem) {
					for (int i = 0; i < sapSerialNoArr.length; i++) {
						if(sapItem.getMaterialCode().equals(dtItem.getMatCode()) && BeanUtil.isEmpty(dtItem.getSerialNo())) {
							String sapSerialNo = sapSerialNoArr[i];
							if(sapUniqueItemIdArr != null && i < sapUniqueItemIdArr.length ) {
								String sapUniqItemId = sapUniqueItemIdArr[i];
								if(sapUniqItemId != null && sapUniqItemId.length() > 18) {
									sapSerialNo = sapUniqItemId;
								}
							}
							
							pickItem = new PickingItem();
							pickItem.setInvSeq(dtItem.getInvSeq());
							pickItem.setMatCode(sapItem.getMaterialCode());
							pickItem.setSerialNo(TDMDataUtility.getSerialNoDatabase(sapSerialNo));
							pickItem.setPickFlg("Y");
							pickItem.setQty("1");
							listPickItem.add(pickItem);
						}
					}
				}
			}else {
				//NonSerial
				for (QueryGoodsIssueBean dtItem : listDtItem) {
					if(sapItem.getMaterialCode().equals(dtItem.getMatCode())) {
						pickItem = new PickingItem();
						pickItem.setInvSeq(dtItem.getInvSeq());
						pickItem.setMatCode(sapItem.getMaterialCode());
						pickItem.setSerialNo(null);
						pickItem.setPickFlg("Y");
						pickItem.setQty(String.valueOf((int)Double.parseDouble(sapItem.getQuantity())));
						listPickItem.add(pickItem);
					}
				}
			}
		}
		
		PickingDocumentIn pickingDoc = new PickingDocumentIn();
		pickingDoc.setDocNo(listDtItem.get(0).getReceiptNum());
		pickingDoc.setDocType(listDtItem.get(0).getReceiptCategory());
		pickingDoc.setCompany(listDtItem.get(0).getReceiptCompany());
		pickingDoc.setUserId("DTAPP");
		pickingDoc.setUserLocCode(locationCode); 
		pickingDoc.setUserMacAddress(mac);
		pickingDoc.setEmpCode(null);
		pickingDoc.setSapDoNo(sap158Bean.getDeliveryNumber());
		pickingDoc.setPickingItems(listPickItem);
		
		pickReq = new PickingDocumentIn();
		pickReq.setPickingDocList(Arrays.asList(pickingDoc));
		
		return pickReq;
	}

	@Override
	public String callPickingDocumentListOnPrem(PickingDocumentIn pickingDocumentIn) {
		Gson gson = new Gson();
		HttpClientUtilDT c = new HttpClientUtilDT();
		Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");

        String req = gson.toJson(pickingDocumentIn);
        
        log.info("Request picking-doc-list : " + req);
        
		//String res = c.HttpClient(properties, dTConfig.getUrl().getDtws() + "/DTWS/api/after-sale/v1/picking-doc-list", req , "POST", null) ;
		String res = c.HttpClient(properties, dTConfig.getUrl().getDtcoresaleout() + "/api/after-sale/v1/picking-doc-list", req , "POST", null) ;
		
		log.info("Response picking-doc-list : " + res);
		
		return res;
	}
	
	@Override
	public void insertGoodsIssueStatusUpdate(Sap158Request request) throws DataAccessException {
		Date date = new Date();
		DtSapGoodsIssueStatusUpdateHeader goodsIssue = new DtSapGoodsIssueStatusUpdateHeader();
		try {

			LovMaster checkType = lovMasterDao.getLovMasterByUnique("GOODS_ISSUE_STATUS_UPDATE",
					"GOODS_ISSUE_STATUS_UPDATE", request.getBody().getCustomerPOTypeCode());
			String customerRef = request.getBody().getItem().get(0).getCustomerReference();

			Instant instant = Instant.parse(request.getHeader().getTimestamp());
			Date dateIns = Date.from(instant);

			if (BeanUtil.isNotEmpty(checkType) && BeanUtil.isNotEmpty(customerRef)) {
				MasterValue masterValue = new MasterValue();
				masterValue.setCreatedBy(DT_APP);
				masterValue.setCreated(date);
				masterValue.setLastUpdBy(DT_APP);
				masterValue.setLastUpd(date);

				goodsIssue.setCustomerReference(customerRef);
				goodsIssue.setDeliveryNumber(
						request.getBody().getDeliveryNumber() != null ? request.getBody().getDeliveryNumber() : "");
				goodsIssue.setGoodsissuedate(dateIns);
				goodsIssue.setCustomerreffromso(request.getBody().getItem().get(0).getCustomerRefFromSO() != null ? request.getBody().getItem().get(0).getCustomerRefFromSO() : "");
				goodsIssue.setCreateValue(masterValue);

				goodsIssueStatusUpdateHeaderDao.insertOrUpdate(goodsIssue);
			}

		} catch (Exception e) {
			log.error("insertGoodsIssueStatusUpdate", e);
		}

	}
	
	@Override
	public void insertDtSapGoodsIssueOrder(Sap158Request request) throws DataAccessException {
		Date date = new Date();
		DtSapGoodsIssueOrder o = null;
		try {
			LovMaster lov = lovMasterDao.getLovMasterByUnique("SAP_S4_GOODS_ISSUE", "PO_TYPE_INSERT_ORDER", request.getBody().getCustomerPOTypeCode());
			if(lov == null) {
				log.info("CustomerPOTypeCode is ignore to insert into dt_sap_goods_issue_order");
				return;
			}

			MasterValue masterValue = new MasterValue();
			masterValue.setCreatedBy(DT_APP);
			masterValue.setCreated(date);
			masterValue.setLastUpdBy(DT_APP);
			masterValue.setLastUpd(date);
			
			String deliverynumber = request.getBody().getDeliveryNumber();
			
			Instant instant = Instant.parse(request.getHeader().getTimestamp());
			Date goodsissuedate = Date.from(instant);
			
			List<Sap158ItemBean> listItem = request.getBody().getItem();
			
			if( BeanUtil.isNotEmpty(listItem)) {
				for (Sap158ItemBean item : listItem) {
					o = new DtSapGoodsIssueOrder();
					o.setCustomerreference(BeanUtil.isEmpty(item.getCustomerReference()) ? null : item.getCustomerReference());
					o.setCustomerreffromso(BeanUtil.isEmpty(item.getCustomerRefFromSO()) ? null : item.getCustomerRefFromSO());
					o.setDeliverynumber(deliverynumber);
					o.setGoodsissuedate(goodsissuedate);
					o.setCreateValue(masterValue);					
					dtSapGoodsIssueOrderDao.insert(o);
				}
			}

		} catch (Exception e) {
			log.error("insertDtSapGoodsIssueOrder", e);
		}

	}
}
