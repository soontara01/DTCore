package th.co.ais.dt.core.controller.impl.sap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.controller.dto.CommonResponseBean;
import th.co.ais.dt.core.service.core.impl.sap.dto.ListKafksConsumeLog;
import th.co.ais.dt.core.service.core.interfaces.sap.IKafkaConsumeLogService;
import th.co.ais.dt.core.service.core.interfaces.sap.ISap158Service;
import th.co.ais.dt.core.service.core.interfaces.sap.ISapHandleProductMasterService;
import th.co.ais.dt.entity.sap.KafksConsumeLog;
import th.co.ais.dt.entity.util.MasterValue;
import th.co.ais.dt.util.BeanUtil;

@RestController
@RequestMapping("api/kafka/v1")
@Slf4j
@RequiredArgsConstructor
public class KafkaLogWebImpl {

    private final IKafkaConsumeLogService kafkaConsumeLogService;
    private final ISapHandleProductMasterService sapHandleProductMasterService;
    private final ISap158Service sap158Service;

    @PostMapping(value = "/insert-log", produces = {"application/json"})
    public ResponseEntity<String> kafkaLog(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        response.setResultCode("20000");
        response.setResultDescription("Success");

        Gson gson = new Gson();
        try {
        	Date d = new Date();
        	KafksConsumeLog request = gson.fromJson(jsonRequest, KafksConsumeLog.class);
        	MasterValue masterValue = new MasterValue();
        	masterValue.setCreated(d);
        	masterValue.setCreatedBy("DTAPP") ;
        	masterValue.setLastUpd(d);
        	masterValue.setLastUpdBy("DTAPP");
        	request.setCreateValue(masterValue);
        	kafkaConsumeLogService.insertLog(request);
           
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
    
    
    @PostMapping(value = "/clear-log", produces = {"application/json"})
    public ResponseEntity<String> kafkaClearLog(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        response.setResultCode("20000");
        response.setResultDescription("Success");

        Gson gson = new Gson();
        try {
        	
        	kafkaConsumeLogService.clearLog();
           
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
    
    @PostMapping(value = "/manual-productmst-by-id", produces = {"application/json"})
    public ResponseEntity<String> manualById(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        response.setResultCode("20000");
        response.setResultDescription("Success");

        Gson gson = new Gson();
        try {
        	ListKafksConsumeLog request = gson.fromJson(jsonRequest, ListKafksConsumeLog.class);
        	
        	if(request != null && BeanUtil.isNotEmpty(request.getListTableId())) {
        		for(String el : request.getListTableId()) {
        			try {
        				KafksConsumeLog log = kafkaConsumeLogService.getKafksConsumeLog(Long.valueOf(el));
            			if(log != null) {
            				SapHandleProductMasterWebImpl sap = new SapHandleProductMasterWebImpl(sapHandleProductMasterService);
                			
            				ListKafksConsumeLog input = gson.fromJson(log.getParaInput(), ListKafksConsumeLog.class);
            				System.out.println(gson.toJson(input.getBody()));
            				sap.handleInsertProductMaster(gson.toJson(input.getBody()));
            			}
        			}catch (Exception e) {
						e.printStackTrace();
					}	
        		}
        	}           
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/resend-consume-goods-issue", produces = {"application/json"})
    public ResponseEntity<String> resendGoodsIssue(@RequestParam String limit) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        response.setResultCode("20000");
        response.setResultDescription("Success");

        Gson gson = new Gson();
        try {
        	List<Long> listKafkaLog = kafkaConsumeLogService.getListGoodsIssueError(limit);
        	
        	if(BeanUtil.isNotEmpty(listKafkaLog)) {
        		for(Long tableId : listKafkaLog) {
        			try {
        				KafksConsumeLog kafkaLog = kafkaConsumeLogService.getKafksConsumeLog(tableId);
            			if(kafkaLog != null) {
            				log.error("Do table_id = " + tableId);
            				
            				Sap158ApiWebImpl sap158 = new Sap158ApiWebImpl(sap158Service);
            				ResponseEntity<String> resp = sap158.handle158PickingDocList(kafkaLog.getParaInput());
            				
            				if(resp != null) {
            					kafkaConsumeLogService.updateResponse(tableId, resp.getBody());
            				}
            			}
        			}catch (Exception e) {
						log.error("Error table_id = " + tableId, e);
					}	
        		}
        	}           
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
    
    @PostMapping(value = "/clear-table-batch-tmp", produces = {"application/json"})
    public ResponseEntity<String> clearTableBatchTmp(@RequestBody String jsonRequest) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, String> properties = new HashMap<>();
        properties.put("Content-Type", "application/json;charset=utf-8");
        httpHeaders.setAll(properties);

        CommonResponseBean response = new CommonResponseBean();
        response.setResultCode("20000");
        response.setResultDescription("Success");

        Gson gson = new Gson();
        try {
        	
        	
        	kafkaConsumeLogService.deletetablebatchjobexecution();
        	kafkaConsumeLogService.deletetablebatchjobexecutioncontext();
        	kafkaConsumeLogService.deletetablebatchjobexecutionparams();
        	kafkaConsumeLogService.deletetablebatchjobinstance();
        	kafkaConsumeLogService.deletetablebatchbatchstepexecution();
        	kafkaConsumeLogService.deletetablebatchstepexecutioncontext();
           
        } catch (Exception e) {
            response.setResultCode("50000");
            response.setResultDescription(e.getMessage());
        }

        return new ResponseEntity<>(gson.toJson(response), httpHeaders, HttpStatus.OK);
    }
    
}
