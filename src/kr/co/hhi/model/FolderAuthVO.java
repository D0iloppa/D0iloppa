/**
 * 
 */
package kr.co.hhi.model;


public class FolderAuthVO {
	String prj_id; 
	long folder_id; 
	String auth_lvl1; 
	String _auth_val; 
	String auth_val; 
	String group_desc;
	String group_name; 
	String folder_auth_add_yn; 
	String parent_folder_inheri_yn; 
	String read_auth_yn; 
	String write_auth_yn; 
	String delete_auth_yn; 
	String reg_id; 
	String reg_date; 
	String mod_id; 
	String mod_date;
	
	long org_id; 
	long org_parent_id; 
	String org_nm;
	String org_full_nm;
	String org_type; 
	String org_path; 
	String user_type;
	String user_id; 
	String user_kor_nm;	
	String group_id;
	
	String[] jsonRowDatas;
	
	
	

	
	
	
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String[] getJsonRowDatas() {
		return jsonRowDatas;
	}
	public void setJsonRowDatas(String[] jsonRowDatas) {
		this.jsonRowDatas = jsonRowDatas;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getUser_kor_nm() {
		return user_kor_nm;
	}
	public void setUser_kor_nm(String user_kor_nm) {
		this.user_kor_nm = user_kor_nm;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getOrg_full_nm() {
		return org_full_nm;
	}
	public void setOrg_full_nm(String org_full_nm) {
		this.org_full_nm = org_full_nm;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String getAuth_lvl1() {
		return auth_lvl1;
	}
	public void setAuth_lvl1(String auth_lvl1) {
		this.auth_lvl1 = auth_lvl1;
	}
	
	public String get_auth_val() {
		return _auth_val;
	}
	public void set_auth_val(String _auth_val) {
		this._auth_val = _auth_val;
	}
	public String getAuth_val() {
		return auth_val;
	}
	public void setAuth_val(String auth_val) {
		this.auth_val = auth_val;
	}
	public String getGroup_desc() {
		return group_desc;
	}
	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}
	public String getFolder_auth_add_yn() {
		return folder_auth_add_yn;
	}
	public void setFolder_auth_add_yn(String folder_auth_add_yn) {
		this.folder_auth_add_yn = folder_auth_add_yn;
	}
	public String getParent_folder_inheri_yn() {
		return parent_folder_inheri_yn;
	}
	public void setParent_folder_inheri_yn(String parent_folder_inheri_yn) {
		this.parent_folder_inheri_yn = parent_folder_inheri_yn;
	}
	public String getRead_auth_yn() {
		return read_auth_yn;
	}
	public void setRead_auth_yn(String read_auth_yn) {
		this.read_auth_yn = read_auth_yn;
	}
	public String getWrite_auth_yn() {
		return write_auth_yn;
	}
	public void setWrite_auth_yn(String write_auth_yn) {
		this.write_auth_yn = write_auth_yn;
	}
	public String getDelete_auth_yn() {
		return delete_auth_yn;
	}
	public void setDelete_auth_yn(String delete_auth_yn) {
		this.delete_auth_yn = delete_auth_yn;
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
