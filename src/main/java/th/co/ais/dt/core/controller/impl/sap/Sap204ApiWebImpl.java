package th.co.ais.dt.core.controller.impl.sap;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap204ReqBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap204ResBean;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapCallApiService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
public class Sap204ApiWebImpl {
    final String prefixPath = "api/sap-interim/v1";

    private final ISapCallApiService sapCallApiService;

    @RequestMapping(value = prefixPath + "/204", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<String> sapInterim204(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);
        Sap204ResBean res = null;
        Gson gson = new Gson();
        try {
            Sap204ReqBean request = gson.fromJson(jsonRequest, Sap204ReqBean.class);

            res = sapCallApiService.call204Api(request);
        } catch (Exception e) {
            log.error("sapInterim204 error ", e);
        }

        return new ResponseEntity<>(gson.toJson(res), httpHeaders, HttpStatus.OK);

    }
}
