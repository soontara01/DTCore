package th.co.ais.dt.core.controller.impl.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.PickingDocumentIn;
import th.co.ais.dt.core.service.core.impl.sap.dto.Sap158Request;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap158Service;

@RestController
@Slf4j
@AllArgsConstructor
public class Sap158ApiWebImpl {
	final String prefixPath = "api/sap-interim/v1";

	private final ISap158Service sap158Service;

	@PostMapping(path = prefixPath + "/158-picking-doc-list", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<String> handle158PickingDocList(@RequestBody String jsonRequest) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		Map<String, String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		Gson gson = new Gson();
		String pickingRes = null;

		try {
			Sap158Request req = gson.fromJson(jsonRequest, Sap158Request.class);
			if (req.getBody() != null) {
				sap158Service.insertGoodsIssueStatusUpdate(req);
				sap158Service.insertDtSapGoodsIssueOrder(req);
				
				PickingDocumentIn pickingDocReq = sap158Service.createPickingDocumentRequest(req.getBody());
				if(pickingDocReq != null) {
					pickingRes = sap158Service.callPickingDocumentListOnPrem(pickingDocReq);
				}
			}
		} catch (Exception e) {
			log.error("158-picking-doc-list : " + jsonRequest);
			log.error("Error", e);
			
			pickingRes = gson.toJson(new CommonResponseBean("50000", "", e.getMessage()));
		}
		return new ResponseEntity<String>(pickingRes, httpHeaders, HttpStatus.OK);
	}
}
