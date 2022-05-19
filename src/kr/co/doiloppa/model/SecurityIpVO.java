package kr.co.doiloppa.model;

public class SecurityIpVO {
	Long seq;
	String ip_range;
	String ip_addr;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	String ip_lvl;
	String ip_val;
	String label;
	
	String white_ip_use_yn;
	
	
	
	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getIp_lvl() {
		return ip_lvl;
	}
	public void setIp_lvl(String ip_lvl) {
		this.ip_lvl = ip_lvl;
	}
	public String getIp_val() {
		return ip_val;
	}
	public void setIp_val(String ip_val) {
		this.ip_val = ip_val;
	}
	public String getWhite_ip_use_yn() {
		return white_ip_use_yn;
	}
	public void setWhite_ip_use_yn(String white_ip_use_yn) {
		this.white_ip_use_yn = white_ip_use_yn;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public String getIp_range() {
		return ip_range;
	}
	public void setIp_range(String ip_range) {
		this.ip_range = ip_range;
	}
	public String getIp_addr() {
		return ip_addr;
	}
	public void setIp_addr(String ip_addr) {
		this.ip_addr = ip_addr;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getMod_id() {
		return mod_id;
	}
	public void setMod_id(String mod_id) {
		this.mod_id = mod_id;
	}
	public String getMod_date() {
		return mod_date;
	}
	public void setMod_date(String mod_date) {
		this.mod_date = mod_date;
	}
}
