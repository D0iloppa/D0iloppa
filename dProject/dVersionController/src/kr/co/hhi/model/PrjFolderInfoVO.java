package kr.co.hhi.model;

public class PrjFolderInfoVO {
	String prj_id;
	long folder_id;
	long parent_folder_id;
	String folder_path;
	String folder_nm;
	String r_folder_path_nm;
	String folder_type;
	String tra_folder_nm;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String create_prj_id;
	String copy_prj_id;
	
	String[] jsonRowDatas;
	
	
	public String[] getJsonRowDatas() {
		return jsonRowDatas;
	}
	public void setJsonRowDatas(String[] jsonRowDatas) {
		this.jsonRowDatas = jsonRowDatas;
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
	public long getParent_folder_id() {
		return parent_folder_id;
	}
	public void setParent_folder_id(long parent_folder_id) {
		this.parent_folder_id = parent_folder_id;
	}
	public String getFolder_path() {
		return folder_path;
	}
	public void setFolder_path(String folder_path) {
		this.folder_path = folder_path;
	}
	public String getFolder_nm() {
		return folder_nm;
	}
	public void setFolder_nm(String folder_nm) {
		this.folder_nm = folder_nm;
	}
	public String getR_folder_path_nm() {
		return r_folder_path_nm;
	}
	public void setR_folder_path_nm(String r_folder_path_nm) {
		this.r_folder_path_nm = r_folder_path_nm;
	}
	public String getFolder_type() {
		return folder_type;
	}
	public void setFolder_type(String folder_type) {
		this.folder_type = folder_type;
	}
	public String getTra_folder_nm() {
		return tra_folder_nm;
	}
	public void setTra_folder_nm(String tra_folder_nm) {
		this.tra_folder_nm = tra_folder_nm;
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
	public String getCreate_prj_id() {
		return create_prj_id;
	}
	public void setCreate_prj_id(String create_prj_id) {
		this.create_prj_id = create_prj_id;
	}
	public String getCopy_prj_id() {
		return copy_prj_id;
	}
	public void setCopy_prj_id(String copy_prj_id) {
		this.copy_prj_id = copy_prj_id;
	}
}
