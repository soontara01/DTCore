package th.co.ais.dt.core.service.core.impl.sap.dto;

import java.io.Serializable;

public class PickingItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String invSeq;
	private String matCode;
	private String serialNo;
	private String pickFlg;
	private String qty;
	private String bypassOptimusFlg;
	private String simSerialNo;

	public String getInvSeq() {
		return invSeq;
	}

	public void setInvSeq(String invSeq) {
		this.invSeq = invSeq;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPickFlg() {
		return pickFlg;
	}

	public void setPickFlg(String pickFlg) {
		this.pickFlg = pickFlg;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getBypassOptimusFlg() {
		return bypassOptimusFlg;
	}

	public void setBypassOptimusFlg(String bypassOptimusFlg) {
		this.bypassOptimusFlg = bypassOptimusFlg;
	}

	public String getSimSerialNo() {
		return simSerialNo;
	}

	public void setSimSerialNo(String simSerialNo) {
		this.simSerialNo = simSerialNo;
	}
}
