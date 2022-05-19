package kr.co.doiloppa.model;

public class UsersVO {
	String prj_id;
	
	String user_id;
	String user_kor_nm;
	String user_eng_nm;
	String user_address;
	String user_group_nm;
	String user_type;
	String passwd;
	String company_nm;
	String first_login_yn;
	String hld_offi_gbn;
	String job_tit_cd;
	String offi_res_cd;
	String email_addr;
	String offi_tel;
	String admin_yn;
	String sfile_nm;
	String rfile_nm;
	String file_path;
	String file_size;
	String file_value;
	String last_con_prj_id;
	String user_st;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	String remote_ip;
	long connect_fail_cnt;
	
	String searchUser;
	String searchKeyword;
	String user_id_list;
	long org_id;
	String org_path;
	String org_nm;
	String org_fullNm;
	String checkedUserString;
	
	//p_password policy 테이블
	String passwrd_expired_day_use_yn;
	int passwd_expire_day;
	String passwd_fail_acc_stop_use_yn;
	int passwd_fail_acc_stop_cnt;
	
	//p_code_info 테이블
	String code_gubun;
	String code;
	String code_nm;
	
	String[] jsonRowDatas;
	
	
	public String getFile_value() {
		return file_value;
	}
	public void setFile_value(String file_value) {
		this.file_value = file_value;
	}
	public String[] getJsonRowDatas() {
		return jsonRowDatas;
	}
	public void setJsonRowDatas(String[] jsonRowDatas) {
		this.jsonRowDatas = jsonRowDatas;
	}
	public String getSfile_nm() {
		return sfile_nm;
	}
	public void setSfile_nm(String sfile_nm) {
		this.sfile_nm = sfile_nm;
	}
	public String getOrg_fullNm() {
		return org_fullNm;
	}
	public void setOrg_fullNm(String org_fullNm) {
		this.org_fullNm = org_fullNm;
	}
	public String getOrg_nm() {
		return org_nm;
	}
	public void setOrg_nm(String org_nm) {
		this.org_nm = org_nm;
	}
	public String getOrg_path() {
		return org_path;
	}
	public void setOrg_path(String org_path) {
		this.org_path = org_path;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_kor_nm() {
		return user_kor_nm;
	}
	public void setUser_kor_nm(String user_kor_nm) {
		this.user_kor_nm = user_kor_nm;
	}
	public String getUser_eng_nm() {
		return user_eng_nm;
	}
	public void setUser_eng_nm(String user_eng_nm) {
		this.user_eng_nm = user_eng_nm;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	public String getUser_group_nm() {
		return user_group_nm;
	}
	public void setUser_group_nm(String user_group_nm) {
		this.user_group_nm = user_group_nm;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getCompany_nm() {
		return company_nm;
	}
	public void setCompany_nm(String company_nm) {
		this.company_nm = company_nm;
	}
	public String getFirst_login_yn() {
		return first_login_yn;
	}
	public void setFirst_login_yn(String first_login_yn) {
		this.first_login_yn = first_login_yn;
	}
	public String getHld_offi_gbn() {
		return hld_offi_gbn;
	}
	public void setHld_offi_gbn(String hld_offi_gbn) {
		this.hld_offi_gbn = hld_offi_gbn;
	}
	public String getJob_tit_cd() {
		return job_tit_cd;
	}
	public void setJob_tit_cd(String job_tit_cd) {
		this.job_tit_cd = job_tit_cd;
	}
	public String getOffi_res_cd() {
		return offi_res_cd;
	}
	public void setOffi_res_cd(String offi_res_cd) {
		this.offi_res_cd = offi_res_cd;
	}
	public String getEmail_addr() {
		return email_addr;
	}
	public void setEmail_addr(String email_addr) {
		this.email_addr = email_addr;
	}
	public String getOffi_tel() {
		return offi_tel;
	}
	public void setOffi_tel(String offi_tel) {
		this.offi_tel = offi_tel;
	}
	public String getAdmin_yn() {
		return admin_yn;
	}
	public void setAdmin_yn(String admin_yn) {
		this.admin_yn = admin_yn;
	}
	public String getRfile_nm() {
		return rfile_nm;
	}
	public void setRfile_nm(String rfile_nm) {
		this.rfile_nm = rfile_nm;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getLast_con_prj_id() {
		return last_con_prj_id;
	}
	public void setLast_con_prj_id(String last_con_prj_id) {
		this.last_con_prj_id = last_con_prj_id;
	}
	public String getUser_st() {
		return user_st;
	}
	public void setUser_st(String user_st) {
		this.user_st = user_st;
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
	public String getRemote_ip() {
		return remote_ip;
	}
	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
	}
	public String getSearchUser() {
		return searchUser;
	}
	public void setSearchUser(String searchUser) {
		this.searchUser = searchUser;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getUser_id_list() {
		return user_id_list;
	}
	public void setUser_id_list(String user_id_list) {
		this.user_id_list = user_id_list;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getCheckedUserString() {
		return checkedUserString;
	}
	public void setCheckedUserString(String checkedUserString) {
		this.checkedUserString = checkedUserString;
	}
	public long getConnect_fail_cnt() {
		return connect_fail_cnt;
	}
	public void setConnect_fail_cnt(long connect_fail_cnt) {
		this.connect_fail_cnt = connect_fail_cnt;
	}
	public String getPasswrd_expired_day_use_yn() {
		return passwrd_expired_day_use_yn;
	}
	public void setPasswrd_expired_day_use_yn(String passwrd_expired_day_use_yn) {
		this.passwrd_expired_day_use_yn = passwrd_expired_day_use_yn;
	}
	public int getPasswd_expire_day() {
		return passwd_expire_day;
	}
	public void setPasswd_expire_day(int passwd_expire_day) {
		this.passwd_expire_day = passwd_expire_day;
	}
	public String getPasswd_fail_acc_stop_use_yn() {
		return passwd_fail_acc_stop_use_yn;
	}
	public void setPasswd_fail_acc_stop_use_yn(String passwd_fail_acc_stop_use_yn) {
		this.passwd_fail_acc_stop_use_yn = passwd_fail_acc_stop_use_yn;
	}
	public int getPasswd_fail_acc_stop_cnt() {
		return passwd_fail_acc_stop_cnt;
	}
	public void setPasswd_fail_acc_stop_cnt(int passwd_fail_acc_stop_cnt) {
		this.passwd_fail_acc_stop_cnt = passwd_fail_acc_stop_cnt;
	}
	public String getCode_gubun() {
		return code_gubun;
	}
	public void setCode_gubun(String code_gubun) {
		this.code_gubun = code_gubun;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode_nm() {
		return code_nm;
	}
	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
	}
}
