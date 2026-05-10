package th.co.ais.dt.core.controller.impl.util;

import com.google.gson.Gson;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.ais.dt.controller.dto.LocMapPlantLocationMstBean;
import th.co.ais.dt.controller.dto.LocMapPlantLocationMstInOutBean;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.cm.LocationMstService;
import th.co.ais.dt.util.BeanUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/util/v1")
@Slf4j
@AllArgsConstructor
public class LocationMstWebImpl {

    private final LocationMstService locationMstService;

    @PostMapping(value = "/listMapLocationByCriteria", produces = {"application/json"})
    @RegisterReflectionForBinding({LocMapPlantLocationMstInOutBean.class})
    public ResponseEntity<String> listMapLocationByCriteria(@RequestBody LocMapPlantLocationMstInOutBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        Gson gson = new Gson();
        LocMapPlantLocationMstInOutBean response = new LocMapPlantLocationMstInOutBean();

        try {
            if (BeanUtil.isEmpty(request.getCompany())) {
                throw new ValidationException("Company is null");
            }

            Date activeDate = null;
            if (!BeanUtil.isEmpty(request.getActiveDate())) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                activeDate = formatter.parse(request.getActiveDate());
            }

            ProcessResult result = locationMstService.listMapLocationByCriteria(
                    request.getLocationCode(),
                    request.getCompany(),
                    request.getLocationName(),
                    request.getLocSubtype(),
                    activeDate);

            if (!result.isSuccess()) {
                throw new ValidationException(result.getMessage());
            }

            List<LocMapPlantLocationMstBean> listLocMapPlantLocation = (List<LocMapPlantLocationMstBean>) result.getResultObject();

            response.setResultCode("20000");
            response.setResultDescription(result.getMessage());
            response.setDeveloperMessage(result.getMessage());
            response.setListLocMapPlantLocation(listLocMapPlantLocation);
            return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
            response.setDeveloperMessage(e.getMessage());
            return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
        }
    }
}
