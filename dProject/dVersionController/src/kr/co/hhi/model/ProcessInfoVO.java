/**
 * 
 */
package kr.co.hhi.model;

/**
 * @author doil
 *
 */
public class ProcessInfoVO {
	
	String prj_id;
	String doc_type;
	String process_id;
	String class_lvl;
	String process_type;
	String process_desc;
	String process_folder_path_id;
	String process_folder_path_string;
	
	long folder_id; // 폴더 아이디 매칭용
	String folder_path;
	String drn_use_yn;
	String process_order;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String process_seq_list; // 체크박스에서 체크된 process_seq들의 문자열
	String folder_path_nm;
	
	String[] tableArrayData;
	
	
	public String[] getTableArrayData() {
		return tableArrayData;
	}
	public void setTableArrayData(String[] tableArrayData) {
		this.tableArrayData = tableArrayData;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
	public String getProcess_id() {
		return process_id;
	}
	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}
	public String getProcess_folder_path_string() {
		return process_folder_path_string;
	}
	public void setProcess_folder_path_string(String process_folder_path_string) {
		this.process_folder_path_string = process_folder_path_string;
	}
	public String getFolder_path() {
		return folder_path;
	}
	public void setFolder_path(String folder_path) {
		this.folder_path = folder_path;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String getProcess_seq_list() {
		return process_seq_list;
	}
	public void setProcess_seq_list(String process_seq_list) {
		this.process_seq_list = process_seq_list;
	}
	
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getClass_lvl() {
		return class_lvl;
	}
	public void setClass_lvl(String class_lvl) {
		this.class_lvl = class_lvl;
	}
	public String getProcess_type() {
		return process_type;
	}
	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}
	public String getProcess_desc() {
		return process_desc;
	}
	public void setProcess_desc(String process_desc) {
		this.process_desc = process_desc;
	}
	public String getProcess_folder_path_id() {
		return process_folder_path_id;
	}
	public void setProcess_folder_path_id(String process_folder_path_id) {
		this.process_folder_path_id = process_folder_path_id;
	}
	public String getDrn_use_yn() {
		return drn_use_yn;
	}
	public void setDrn_use_yn(String drn_use_yn) {
		this.drn_use_yn = drn_use_yn;
	}
	public String getProcess_order() {
		return process_order;
	}
	public void setProcess_order(String process_order) {
		this.process_order = process_order;
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
	public String getFolder_path_nm() {
		return folder_path_nm;
	}
	public void setFolder_path_nm(String folder_path_nm) {
		this.folder_path_nm = folder_path_nm;
	}
	
	

}
