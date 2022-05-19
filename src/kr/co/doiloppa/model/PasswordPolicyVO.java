package kr.co.doiloppa.model;

public class PasswordPolicyVO {
	String passwd_expire_day_use_yn;
	long passwd_expire_day;
	String passwd_fail_acc_stop_use_yn;
	long passwd_fail_acc_stop_cnt;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	public String getPasswd_expire_day_use_yn() {
		return passwd_expire_day_use_yn;
	}
	public void setPasswd_expire_day_use_yn(String passwd_expire_day_use_yn) {
		this.passwd_expire_day_use_yn = passwd_expire_day_use_yn;
	}
	public long getPasswd_expire_day() {
		return passwd_expire_day;
	}
	public void setPasswd_expire_day(long passwd_expire_day) {
		this.passwd_expire_day = passwd_expire_day;
	}
	public String getPasswd_fail_acc_stop_use_yn() {
		return passwd_fail_acc_stop_use_yn;
	}
	public void setPasswd_fail_acc_stop_use_yn(String passwd_fail_acc_stop_use_yn) {
		this.passwd_fail_acc_stop_use_yn = passwd_fail_acc_stop_use_yn;
	}
	public long getPasswd_fail_acc_stop_cnt() {
		return passwd_fail_acc_stop_cnt;
	}
	public void setPasswd_fail_acc_stop_cnt(long passwd_fail_acc_stop_cnt) {
		this.passwd_fail_acc_stop_cnt = passwd_fail_acc_stop_cnt;
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
