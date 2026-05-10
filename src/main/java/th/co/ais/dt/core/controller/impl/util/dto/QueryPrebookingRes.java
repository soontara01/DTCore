package th.co.ais.dt.core.controller.impl.util.dto;

import lombok.Data;
import th.co.ais.dt.entity.so.PreBooking;

@Data
public class QueryPrebookingRes {
	
	private String resultCode;
	private String resultDescription;
	private String developerMessage;
	private PreBooking prebookingInfo;

	public QueryPrebookingRes() {

	}

	public QueryPrebookingRes(String resultCode, String resultDescription, String developerMessage) {
		this.resultCode = resultCode;
		this.resultDescription = resultDescription;
		this.developerMessage = developerMessage;
	}

}
