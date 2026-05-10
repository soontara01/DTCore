package th.co.ais.dt.core.controller.impl.so;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import th.co.ais.dt.entity.so.VatAddress;
import th.co.ais.dt.exception.ForceTerminateException;
import th.co.ais.dt.service.core.dto.ProcessResult;
import th.co.ais.dt.service.core.interfaces.mt.IConfigLovMasterService;
import th.co.ais.dt.service.core.interfaces.so.VatAddressService;
import th.co.ais.dt.util.BeanUtil;
import th.co.ais.dt.controller.dto.VatAddressBean;

@RestController
@Slf4j
@AllArgsConstructor
public class VatAddressWebImpl {
	final String prefixPath = "api/sale/v1";
	
	private final VatAddressService vatAddressService ;
	
	@RequestMapping(value = prefixPath +"/queryListVatAddress",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(VatAddressBean.class)
	public ResponseEntity<String> queryListVatAddress(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		try {

			//VatAddressService vatAddressService = (VatAddressService) SpringApplicationContext.getBean("vatAddressService");
			VatAddressBean res = new VatAddressBean();
			VatAddressBean input = new VatAddressBean();

			Gson gson = new Gson();
			try {
				input = gson.fromJson(in, VatAddressBean.class);
			} catch (Exception e) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

			}

			if (input != null && 
					(BeanUtil.isNotEmpty(input.getTaxId()) || 
					BeanUtil.isNotEmpty(input.getMobileNo()) || 
					BeanUtil.isNotEmpty(input.getTelNo()))) {

				List<VatAddress> result = null;
				try {
					result = vatAddressService.getListVatAddressByCriteria(input.getTaxId(), input.getMobileNo(), input.getTelNo());
					
					if(BeanUtil.isNotEmpty(result)) {
						res.setDeveloperMessage("Success");
						res.setResultCode("20000");
						res.setResultDescription("Success");
						res.setVatAddressList(result);
					}else {
						res.setDeveloperMessage("Data not found");
						res.setResultCode("50000");
						res.setResultDescription("Data not found");
					}
					
				} catch (Exception e) {
					res.setDeveloperMessage(e.getMessage());
					res.setResultCode("50000");
					res.setResultDescription(e.getMessage());
				}

				return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

			} else {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"System error\",\"developerMessage\":\"System error\"}", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value = prefixPath +"/insertVatAddress",method = RequestMethod.POST, produces = { "application/json" })
	@RegisterReflectionForBinding(VatAddressBean.class)
	public ResponseEntity<String> insertVatAddress(@RequestBody String in) {
		final HttpHeaders httpHeaders= new HttpHeaders();
		Map<String,String> properties = new HashMap<>();
		properties.put("Content-Type", "application/json;charset=utf-8");
		httpHeaders.setAll(properties);
		
		VatAddressBean res = new VatAddressBean();
		VatAddress input = new VatAddress();
		Gson gson = new Gson();
		try {
			input = gson.fromJson(in, VatAddress.class);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"JSON incorrect format\",\"developerMessage\":\"JSON incorrect format\"}", httpHeaders, HttpStatus.OK);

		}

		if (in != null) {
			if(BeanUtil.isEmpty(input.getTaxId()) && BeanUtil.isEmpty(input.getCreateBy())) {
				return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Tax ID and Create By is empty\",\"developerMessage\":\"Tax Id and Create By is empty\"}", httpHeaders, HttpStatus.OK);

			}else {
				// call pkg
				ProcessResult result = new ProcessResult(false, null, "");
				try {
					boolean dupicate = vatAddressService.isDupicateVatAddressByHouseNoAndRoom(input.getTaxId(), input.getHouseNo(), input.getRoom());
					if(dupicate) {
						res.setDeveloperMessage("Data is dupicate");
						res.setResultCode("20000");
						res.setResultDescription("Success");
						return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

					}else {
						result = vatAddressService.insertVatAddress(input);
					}
				}catch(ForceTerminateException e) {
					result = (ProcessResult) e.getObj();
				}
				
				if (result.isSuccess()) {
					// success
					res.setDeveloperMessage("Success");
					res.setResultCode("20000");
					res.setResultDescription("Success");
					return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);
				} else {
					// error
					res.setDeveloperMessage(result.getMessage());
					res.setResultCode("50000");
					res.setResultDescription(result.getMessage());
					return new ResponseEntity<String>(gson.toJson(res), httpHeaders, HttpStatus.OK);

				}
				
			}
		} else {
			return new ResponseEntity<String>("{\"resultCode\":\"50000\",\"resultDescription\":\"Data incorrect\",\"developerMessage\":\"Data incorrect\"}", httpHeaders, HttpStatus.OK);
		}

	}

}
