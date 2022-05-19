package kr.co.doiloppa.model;

public class PrjEmailTypeContentsVO {
	String prj_id;
	String email_type_id;
	String email_sender_addr;
	String email_receiver_addr;
	String email_referto_addr;
	String email_bcc_addr;
	String file_link_yn;
	String file_rename_yn;
	String file_nm_prefix;
	String subject_prefix;
	String contents;
	String file_direct_attach_type;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String[] jsonRowdatas;
	
	public String[] getJsonRowdatas() {
		return jsonRowdatas;
	}
	public void setJsonRowdatas(String[] jsonRowdatas) {
		this.jsonRowdatas = jsonRowdatas;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getEmail_type_id() {
		return email_type_id;
	}
	public void setEmail_type_id(String email_type_id) {
		this.email_type_id = email_type_id;
	}
	public String getEmail_sender_addr() {
		return email_sender_addr;
	}
	public void setEmail_sender_addr(String email_sender_addr) {
		this.email_sender_addr = email_sender_addr;
	}
	public String getEmail_receiver_addr() {
		return email_receiver_addr;
	}
	public void setEmail_receiver_addr(String email_receiver_addr) {
		this.email_receiver_addr = email_receiver_addr;
	}
	public String getEmail_referto_addr() {
		return email_referto_addr;
	}
	public void setEmail_referto_addr(String email_referto_addr) {
		this.email_referto_addr = email_referto_addr;
	}
	public String getEmail_bcc_addr() {
		return email_bcc_addr;
	}
	public void setEmail_bcc_addr(String email_bcc_addr) {
		this.email_bcc_addr = email_bcc_addr;
	}
	public String getFile_link_yn() {
		return file_link_yn;
	}
	public void setFile_link_yn(String file_link_yn) {
		this.file_link_yn = file_link_yn;
	}
	public String getFile_rename_yn() {
		return file_rename_yn;
	}
	public void setFile_rename_yn(String file_rename_yn) {
		this.file_rename_yn = file_rename_yn;
	}
	public String getFile_nm_prefix() {
		return file_nm_prefix;
	}
	public void setFile_nm_prefix(String file_nm_prefix) {
		this.file_nm_prefix = file_nm_prefix;
	}
	public String getSubject_prefix() {
		return subject_prefix;
	}
	public void setSubject_prefix(String subject_prefix) {
		this.subject_prefix = subject_prefix;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getFile_direct_attach_type() {
		return file_direct_attach_type;
	}
	public void setFile_direct_attach_type(String file_direct_attach_type) {
		this.file_direct_attach_type = file_direct_attach_type;
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
