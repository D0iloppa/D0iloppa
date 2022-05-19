package kr.co.hhi.model;

public class SecurityLogVO {
	
	String prj_id;
	String doc_id;
	String rev_id;
	String reg_date;
	String prj_no;
	String prj_nm;
	String doc_no;
	String doc_title;
	long folder_id;
	String folder_path;
	long file_size;
	String reg_id;
	String reg_nm;
	String remote_ip;
	String method;
	String pgm_nm;
	
	String folder_nm;
	
	String[] jsonRowDatas;
	
	String fileName; // 저장한 파일 네임
	
	// 검색 조건, 검색어 저장
	String from;
	String to;
	String searchPrjLogAtc;
	String textPrjLogAtc;
	String searchLogAtc;
	String textLogAtc;
	String logGubun;

	public String getFolder_nm() {
		return folder_nm;
	}
	public void setFolder_nm(String folder_nm) {
		this.folder_nm = folder_nm;
	}
	public String getLogGubun() {
		return logGubun;
	}
	public void setLogGubun(String logGubun) {
		this.logGubun = logGubun;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSearchPrjLogAtc() {
		return searchPrjLogAtc;
	}
	public void setSearchPrjLogAtc(String searchPrjLogAtc) {
		this.searchPrjLogAtc = searchPrjLogAtc;
	}
	public String getTextPrjLogAtc() {
		return textPrjLogAtc;
	}
	public void setTextPrjLogAtc(String textPrjLogAtc) {
		this.textPrjLogAtc = textPrjLogAtc;
	}	
	public String getSearchLogAtc() {
		return searchLogAtc;
	}
	public void setSearchLogAtc(String searchLogAtc) {
		this.searchLogAtc = searchLogAtc;
	}
	public String getTextLogAtc() {
		return textLogAtc;
	}
	public void setTextLogAtc(String textLogAtc) {
		this.textLogAtc = textLogAtc;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
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
	public String getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
	public String getRev_id() {
		return rev_id;
	}
	public void setRev_id(String rev_id) {
		this.rev_id = rev_id;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getPrj_no() {
		return prj_no;
	}
	public void setPrj_no(String prj_no) {
		this.prj_no = prj_no;
	}
	public String getPrj_nm() {
		return prj_nm;
	}
	public void setPrj_nm(String prj_nm) {
		this.prj_nm = prj_nm;
	}
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}
	public String getDoc_title() {
		return doc_title;
	}
	public void setDoc_title(String doc_title) {
		this.doc_title = doc_title;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String getFolder_path() {
		return folder_path;
	}
	public void setFolder_path(String folder_path) {
		this.folder_path = folder_path;
	}
	public long getFile_size() {
		return file_size;
	}
	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}
	public String getReg_nm() {
		return reg_nm;
	}
	public void setReg_nm(String reg_nm) {
		this.reg_nm = reg_nm;
	}
	public String getRemote_ip() {
		return remote_ip;
	}
	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPgm_nm() {
		return pgm_nm;
	}
	public void setPgm_nm(String pgm_nm) {
		this.pgm_nm = pgm_nm;
	}	
	
	
	
}
