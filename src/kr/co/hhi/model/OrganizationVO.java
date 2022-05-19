package kr.co.hhi.model;

public class OrganizationVO {
	long org_id;
	long org_parent_id;
	String org_nm;
	String org_type;
	String org_path;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String new_org_nm;
	String org_group_nm_path;
	
	long old_org_parent_id;
	String origin_org_path;
	
	String userMngPage;
	
	
	public String getUserMngPage() {
		return userMngPage;
	}
	public void setUserMngPage(String userMngPage) {
		this.userMngPage = userMngPage;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public long getOrg_parent_id() {
		return org_parent_id;
	}
	public void setOrg_parent_id(long org_parent_id) {
		this.org_parent_id = org_parent_id;
	}
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	public String getOrg_type() {
		return org_type;
	}
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}
	public String getOrg_path() {
		return org_path;
	}
	public void setOrg_path(String org_path) {
		this.org_path = org_path;
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
	public String getOrg_group_nm_path() {
		return org_group_nm_path;
	}
	public void setOrg_group_nm_path(String org_group_nm_path) {
		this.org_group_nm_path = org_group_nm_path;
	}
	public long getOld_org_parent_id() {
		return old_org_parent_id;
	}
	public void setOld_org_parent_id(long old_org_parent_id) {
		this.old_org_parent_id = old_org_parent_id;
	}
	public String getOrigin_org_path() {
		return origin_org_path;
	}
	public void setOrigin_org_path(String origin_org_path) {
		this.origin_org_path = origin_org_path;
	}
}
