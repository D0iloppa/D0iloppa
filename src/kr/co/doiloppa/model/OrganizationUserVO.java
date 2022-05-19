package kr.co.doiloppa.model;

public class OrganizationUserVO {
	long org_id;
	String user_id;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String new_org_nm;
	long origin_org_id;
	
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public String getNew_org_nm() {
		return new_org_nm;
	}
	public void setNew_org_nm(String new_org_nm) {
		this.new_org_nm = new_org_nm;
	}
	public long getOrigin_org_id() {
		return origin_org_id;
	}
	public void setOrigin_org_id(long origin_org_id) {
		this.origin_org_id = origin_org_id;
	}
}
