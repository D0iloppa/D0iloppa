package kr.co.hhi.model;

public class WbsCodeControlVO {
	String prj_id;
	String wbs_code_id;
	String wbs_code;
	String wbs_desc;
	String doc_no;
	String wgtval;
	String doc_type;
	String discipline;
	long code_seq;
	String gad;
	String distribute;
	String information;
	String siteact;
	String sitecc;
	String disact;
	String discc;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	// 조인한테이블 데이터
	String discip_display;
	String discip_code_id;
	
	// 검색 조건
	String searchChkbox;
	String search_display;
	
	long folder_id;
	
	String[] jsonRowdatas;
	
	String fileName;
	String prj_nm;


	
	
	public String getDiscip_code_id() {
		return discip_code_id;
	}
	public void setDiscip_code_id(String discip_code_id) {
		this.discip_code_id = discip_code_id;
	}
	public String getPrj_nm() {
		return prj_nm;
	}
	public void setPrj_nm(String prj_nm) {
		this.prj_nm = prj_nm;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String[] getJsonRowdatas() {
		return jsonRowdatas;
	}
	public void setJsonRowdatas(String[] jsonRowdatas) {
		this.jsonRowdatas = jsonRowdatas;
	}
	public String getSearch_display() {
		return search_display;
	}
	public void setSearch_display(String search_display) {
		this.search_display = search_display;
	}
	public String getSearchChkbox() {
		return searchChkbox;
	}
	public void setSearchChkbox(String searchChkbox) {
		this.searchChkbox = searchChkbox;
	}
	public String getDiscip_display() {
		return discip_display;
	}
	public void setDiscip_display(String discip_display) {
		this.discip_display = discip_display;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getWbs_code_id() {
		return wbs_code_id;
	}
	public void setWbs_code_id(String wbs_code_id) {
		this.wbs_code_id = wbs_code_id;
	}
	public String getWbs_code() {
		return wbs_code;
	}
	public void setWbs_code(String wbs_code) {
		this.wbs_code = wbs_code;
	}
	public String getWbs_desc() {
		return wbs_desc;
	}
	public void setWbs_desc(String wbs_desc) {
		this.wbs_desc = wbs_desc;
	}
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}
	public String getWgtval() {
		return wgtval;
	}
	public void setWgtval(String wgtval) {
		this.wgtval = wgtval;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public long getCode_seq() {
		return code_seq;
	}
	public void setCode_seq(long code_seq) {
		this.code_seq = code_seq;
	}
	public String getGad() {
		return gad;
	}
	public void setGad(String gad) {
		this.gad = gad;
	}
	public String getDistribute() {
		return distribute;
	}
	public void setDistribute(String distribute) {
		this.distribute = distribute;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getSiteact() {
		return siteact;
	}
	public void setSiteact(String siteact) {
		this.siteact = siteact;
	}
	public String getSitecc() {
		return sitecc;
	}
	public void setSitecc(String sitecc) {
		this.sitecc = sitecc;
	}
	public String getDisact() {
		return disact;
	}
	public void setDisact(String disact) {
		this.disact = disact;
	}
	public String getDiscc() {
		return discc;
	}
	public void setDiscc(String discc) {
		this.discc = discc;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
