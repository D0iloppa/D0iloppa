package kr.co.hhi.model;

public class UserConnectVO {
	String user_id;
	String first_con_date;
	String last_con_date;
	long connect_no;
	long connect_fail_cnt;
	String session_id;
	String remote_ip;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFirst_con_date() {
		return first_con_date;
	}
	public void setFirst_con_date(String first_con_date) {
		this.first_con_date = first_con_date;
	}
	public String getLast_con_date() {
		return last_con_date;
	}
	public void setLast_con_date(String last_con_date) {
		this.last_con_date = last_con_date;
	}
	public long getConnect_no() {
		return connect_no;
	}
	public void setConnect_no(long connect_no) {
		this.connect_no = connect_no;
	}
	public long getConnect_fail_cnt() {
		return connect_fail_cnt;
	}
	public void setConnect_fail_cnt(long connect_fail_cnt) {
		this.connect_fail_cnt = connect_fail_cnt;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getRemote_ip() {
		return remote_ip;
	}
	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
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
