package th.co.ais.dt.core.service.core.impl.sap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartRequest;
import th.co.ais.dt.core.service.core.impl.sap.dto.SapCancelShoppingCartResponse;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCancelShoppingCartService;
import th.co.ais.dt.entity.sap.DtSapReserveLog;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.repository.interfaces.sap.IDtSapReserveLogDao;
import th.co.ais.dt.util.BeanUtil;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class SapCancelShoppingCartServiceImpl implements ISapCancelShoppingCartService {

    public static final String DT_APP = "DTAPP";

    private final SapCallApiServiceImpl sapCallApiService;
    private final IDtSapReserveLogDao dtSapReserveLogDao;

    @Override
    public SapCancelShoppingCartResponse cancelShoppingCart(SapCancelShoppingCartRequest request) {
        
        SapCancelShoppingCartResponse cancelShoppingCartResponse = sapCallApiService.callCancelShoppingCart(request);
        
        insertSapReserveLog(request, cancelShoppingCartResponse);
        
        return cancelShoppingCartResponse;
    }

    private void insertSapReserveLog(SapCancelShoppingCartRequest request, SapCancelShoppingCartResponse cancelShoppingCartResponse) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Date date = new Date();

        MasterValue masterValue = new MasterValue();
        masterValue.setCreatedBy(DT_APP);
        masterValue.setCreated(date);
        masterValue.setLastUpdBy(DT_APP);
        masterValue.setLastUpd(date);
        
        DtSapReserveLog sapReserveLog = new DtSapReserveLog();
        if (BeanUtil.isEmpty(cancelShoppingCartResponse.getMessage())) {
            sapReserveLog.setShoppingcartid(
                    BeanUtil.isNotEmpty(cancelShoppingCartResponse.getShoppingCart()) 
                            ? cancelShoppingCartResponse.getShoppingCart().getId() 
                            : null
            );
        }
        sapReserveLog.setParaInput(gson.toJson(request));
        sapReserveLog.setResponse(gson.toJson(cancelShoppingCartResponse));
        sapReserveLog.setCreateValue(masterValue);

        dtSapReserveLogDao.insert(sapReserveLog);
    }
}
