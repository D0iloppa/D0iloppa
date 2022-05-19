package kr.co.doiloppa.model;

public class CodeInfoVO {
	String code_gubun;
	String code;
	String code_nm;
	long set_order;
	
	String origin_code;
	
	public String getOrigin_code() {
		return origin_code;
	}
	public void setOrigin_code(String origin_code) {
		this.origin_code = origin_code;
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
	public long getSet_order() {
		return set_order;
	}
	public void setSet_order(long set_order) {
		this.set_order = set_order;
	}
	
	
}
