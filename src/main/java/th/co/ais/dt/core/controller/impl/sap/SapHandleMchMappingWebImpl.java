package th.co.ais.dt.core.controller.impl.sap;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.controller.dto.DtSapMchMapping;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleMchMappingService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/sap-handle-mch-mapping/v1")
@Slf4j
@RequiredArgsConstructor
public class SapHandleMchMappingWebImpl {

    private final ISapHandleMchMappingService sapHandleMchMappingService;

    @PostMapping(value = "/product", produces = {"application/json"})
    public ResponseEntity<String> handleMchMappingProduct(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        Gson gson = new Gson();
        try {
            DtSapMchMapping request = gson.fromJson(jsonRequest, DtSapMchMapping.class);
            response = sapHandleMchMappingService.mappingMatGroupProduct(request);
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
}
