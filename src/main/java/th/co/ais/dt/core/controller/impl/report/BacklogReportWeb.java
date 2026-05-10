package th.co.ais.dt.core.controller.impl.report;

import com.google.gson.Gson;
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
import th.co.ais.dt.core.controller.impl.report.dto.BacklogReportBean;
import th.co.ais.dt.core.controller.impl.report.dto.BacklogReportRes;
import th.co.ais.dt.core.service.core.interfaces.report.IBacklogReportService;
import th.co.ais.dt.util.BeanUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/report/v1")
@Slf4j
@AllArgsConstructor
public class BacklogReportWeb {

    private final IBacklogReportService backlogReportService;

    @PostMapping(value = "/query-Backlog-Summary", produces = {"application/json"})
    @RegisterReflectionForBinding({BacklogReportBean.class, BacklogReportRes.class})
    public ResponseEntity<String> queryBacklogSummary(@RequestBody BacklogReportBean request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        Gson gson = new Gson();
        BacklogReportRes response = new BacklogReportRes();

        try {
            List<BacklogReportBean> results = backlogReportService.queryBacklogSummary(request);

            if (BeanUtil.isNotEmpty(results)) {
                response.setResultCode("20000");
                response.setResultDescription("Success");
                response.setStatus("S");
                response.setBacklogReportList(results);
            } else {
                response.setResultCode("50000");
                response.setResultDescription("Data not found");
                response.setStatus("F");
            }
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription("System error");
            response.setStatus("F");
            return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("Response queryBacklogSummary(): {}", gson.toJson(response));
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
}
