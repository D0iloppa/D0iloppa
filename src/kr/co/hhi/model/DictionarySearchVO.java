package kr.co.hhi.model;

public class DictionarySearchVO {
	String abbr;
	long abbr_id;
	String eng_full_nm;
	String kor_full_nm;
	String dic_desc;
	String reg_id;
	String reg_date;
	String mod_id;
	
	String abbr_list;
	String searchAbbr;
	String searchEng;
	String searchKor;
	String searchDesc;
	
	// 로그인 상태 유저 정보 저장
	String user_id;
	
	String[] jsonRowdatas;
		
	public String[] getJsonRowdatas() {
		return jsonRowdatas;
	}
	public void setJsonRowdatas(String[] jsonRowdatas) {
		this.jsonRowdatas = jsonRowdatas;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSearchAbbr() {
		return searchAbbr;
	}
	public void setSearchAbbr(String searchAbbr) {
		this.searchAbbr = searchAbbr;
	}
	public String getSearchEng() {
		return searchEng;
	}
	public void setSearchEng(String searchEng) {
		this.searchEng = searchEng;
	}
	public String getSearchKor() {
		return searchKor;
	}
	public void setSearchKor(String searchKor) {
		this.searchKor = searchKor;
	}
	public String getSearchDesc() {
		return searchDesc;
	}
	public void setSearchDesc(String searchDesc) {
		this.searchDesc = searchDesc;
	}
	public String getAbbr_list() {
		return abbr_list;
	}
	public void setAbbr_list(String abbr_list) {
		this.abbr_list = abbr_list;
	}
	
	public long getAbbr_id() {
		return abbr_id;
	}
	public void setAbbr_id(long abbr_id) {
		this.abbr_id = abbr_id;
	}
	String mod_date;
	
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getEng_full_nm() {
		return eng_full_nm;
	}
	public void setEng_full_nm(String eng_full_nm) {
		this.eng_full_nm = eng_full_nm;
	}
	public String getKor_full_nm() {
		return kor_full_nm;
	}
	public void setKor_full_nm(String kor_full_nm) {
		this.kor_full_nm = kor_full_nm;
	}
	public String getDic_desc() {
		return dic_desc;
	}
	public void setDic_desc(String dic_desc) {
		this.dic_desc = dic_desc;
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
