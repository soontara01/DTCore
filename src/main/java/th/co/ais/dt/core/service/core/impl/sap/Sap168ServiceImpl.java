package th.co.ais.dt.core.service.core.impl.sap;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap168ResBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap168Service;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;
import th.co.ais.dt.entity.sap.DtSapTransferInLog;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.repository.interfaces.sap.IDtSapTransferInLogDao;

@Service
@Slf4j
@RequiredArgsConstructor
public class Sap168ServiceImpl implements ISap168Service{

	private final ISapCallApiService sapCallApiService;
	private final IDtSapTransferInLogDao dtSapTransferInLogDao;
	
	@Override
	public void callSapGoodsMovementCreate_0168(Sap168ReqBean request, String userId) {
		DtSapTransferInLog sapLog = null;
		Sap168ResBean response = null;
		Gson gson = new Gson();
		MasterValue createValue = null;
		try {
			sapLog = new DtSapTransferInLog();
			sapLog.setDocNo(request.getPartnerMessageID());
			sapLog.setServiceName("0168_Goods_Movement_Creation");
			sapLog.setStatus("F");
			sapLog.setParaInput(gson.toJson(request));
			
			createValue = new MasterValue();
			createValue.setCreatedBy(userId);
			createValue.setCreated(new Date());
			createValue.setLastUpdBy(userId);

			sapCallApiService.call168Api(request);
			
			sapLog.setStatus("S");
		}catch (Exception e) {
			sapLog.setStatus("F");
			log.info("callSapGoodsMovementCreate_0168", e);
		}finally {
			createValue.setLastUpd(new Date());

			sapLog.setParaOutput(gson.toJson(response));
			sapLog.setCreateValue(createValue);
			dtSapTransferInLogDao.insert(sapLog);
		}
	}
	
}
