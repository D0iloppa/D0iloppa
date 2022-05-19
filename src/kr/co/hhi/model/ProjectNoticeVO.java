package kr.co.hhi.model;

public class ProjectNoticeVO {
	String prj_id;
	long bbs_seq;
	long parent_no;
	long order_no;
	long depth_no;
	String title;
	String contents;
	long hit_cnt;
	String remote_ip;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	String code_nm;
	// 로그인 상태 유저 정보 저장
	String user_kor_nm;
	String user_id;
	// 검색조건, 검색어 저장
	String searchAtc;
	String textAtc;
	// 프로젝트 명
	String prj_nm;
	
	//프로젝트 게시판 파일 정보 저장
	int file_seq;
	String rfile_nm;
	String sfile_nm;
	Long file_size;
	String file_path;
	
	String fullPath;
	
	// 프로젝트 게시판 전체공지 or 프로젝트 공지 구분
	String contents_type;
	
	
	public String getCode_nm() {
		return code_nm;
	}
	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
	}
	public String getContents_type() {
		return contents_type;
	}
	public void setContents_type(String contents_type) {
		this.contents_type = contents_type;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public int getFile_seq() {
		return file_seq;
	}
	public void setFile_seq(int file_seq) {
		this.file_seq = file_seq;
	}
	public String getRfile_nm() {
		return rfile_nm;
	}
	public void setRfile_nm(String rfile_nm) {
		this.rfile_nm = rfile_nm;
	}
	public String getSfile_nm() {
		return sfile_nm;
	}
	public void setSfile_nm(String sfile_nm) {
		this.sfile_nm = sfile_nm;
	}
	public Long getFile_size() {
		return file_size;
	}
	public void setFile_size(Long file_size) {
		this.file_size = file_size;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getPrj_nm() {
		return prj_nm;
	}
	public void setPrj_nm(String prj_nm) {
		this.prj_nm = prj_nm;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getUser_kor_nm() {
		return user_kor_nm;
	}
	public void setUser_kor_nm(String user_kor_nm) {
		this.user_kor_nm = user_kor_nm;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSearchAtc() {
		return searchAtc;
	}
	public void setSearchAtc(String searchAtc) {
		this.searchAtc = searchAtc;
	}
	public String getTextAtc() {
		return textAtc;
	}
	public void setTextAtc(String textAtc) {
		this.textAtc = textAtc;
	}
	public long getBbs_seq() {
		return bbs_seq;
	}
	public void setBbs_seq(long bbs_seq) {
		this.bbs_seq = bbs_seq;
	}
	public long getParent_no() {
		return parent_no;
	}
	public void setParent_no(long parent_no) {
		this.parent_no = parent_no;
	}
	public long getOrder_no() {
		return order_no;
	}
	public void setOrder_no(long order_no) {
		this.order_no = order_no;
	}
	public long getDepth_no() {
		return depth_no;
	}
	public void setDepth_no(long depth_no) {
		this.depth_no = depth_no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public long getHit_cnt() {
		return hit_cnt;
	}
	public void setHit_cnt(long hit_cnt) {
		this.hit_cnt = hit_cnt;
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
