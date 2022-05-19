/**
 * 
 */
package kr.co.hhi.model;

/**
 * @author doil
 *
 */
public class GlobalSearchVO {
	
	// 중복사용 필드 파라메터 주석처리
	
	/* document index */
	String prj_id;
	String doc_id;
	String rev_id;
	long doc_seq;
	long folder_id;
	String doc_no;
	String doc_type;
	String rev_no;
	String sfile_nm;
	String rfile_nm;
	String file_type;
	String file_size;
	String title;
	String del_yn;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	String modifier;
	
	/* TRANSMITTAL LIST */
//	long prj_id;
	String tr_id;
	String tr_no;
	String itr_no;
	String trType;
	String tr_subject;
	String discipline_code_id;
	String inout;
	String tr_status;
	String tr_issuepur;
	String send_date;
	String req_date;
	String tr_writer_id;
	String tr_modifier_id;
	String chk_date;
	String tr_remark;
	String tr_distribute;
	String tr_from;
	String tr_to;
	String ref_tr_no;
//	String reg_id;
//	String reg_date;
//	String mod_id;     
//	String mod_date;
	
	/* DRN LIST */
//	long prj_id;
	String drn_id;
	String drn_no;
	String drn_title;
	String drn_prj_id;
	String status;
	String doc_title;
	String approval_id;
	String check_sheet_id;
	String check_sheet_title;
	String review_conclusion;
	String drn_approval_status;
//	String reg_id;
//	String reg_date;
//	String mod_id;     
//	String mod_date;

	
	/* 검색결과 조인용 필드 */
	String prj_nm;
	String folder_path;
	String folder_nm;
	String discip_display;
	String folder_str;
	
	
	
	/* 검색용 파라메터 */
	String keyword;
	String prj_list;
	String searchOpt;
	int max_Display;
	boolean allPrjchk; // 모든 프로젝트 체크박스
	boolean periodChk; // period 체크박스
	String searchFrom, searchTo;
	
	String basic_sel_doc_no;
	String basic_input_doc_no;
	String basic_sel_title;
	String basic_input_title;
	
	String detail_sel_doc_type;
	String version;
	String detail_input_modifier;
	boolean mod_periodChk;
	String mod_searchFrom,mod_searchTo;
	
	/* 엑셀용 json string */
	String[] docJson , trJson , drnJson;
	String docJsonStr, trJsonStr, drnJsonStr;
	
	String size1,trWriter,drnWriter;


	/* getter & setter */
	
	
	
	public boolean isAllPrjchk() {
		return allPrjchk;
	}
	public String getDrnWriter() {
		return drnWriter;
	}
	public void setDrnWriter(String drnWriter) {
		this.drnWriter = drnWriter;
	}
	public String getTrWriter() {
		return trWriter;
	}
	public void setTrWriter(String trWriter) {
		this.trWriter = trWriter;
	}
	public String getDocJsonStr() {
		return docJsonStr;
	}
	public void setDocJsonStr(String docJsonStr) {
		this.docJsonStr = docJsonStr;
	}
	public String getTrJsonStr() {
		return trJsonStr;
	}
	public void setTrJsonStr(String trJsonStr) {
		this.trJsonStr = trJsonStr;
	}
	public String getDrnJsonStr() {
		return drnJsonStr;
	}
	public void setDrnJsonStr(String drnJsonStr) {
		this.drnJsonStr = drnJsonStr;
	}
	public String getSize1() {
		return size1;
	}
	public void setSize1(String size1) {
		this.size1 = size1;
	}
	public String[] getDocJson() {
		return docJson;
	}
	public void setDocJson(String[] docJson) {
		this.docJson = docJson;
	}
	public String[] getTrJson() {
		return trJson;
	}
	public void setTrJson(String[] trJson) {
		this.trJson = trJson;
	}
	public String[] getDrnJson() {
		return drnJson;
	}
	public void setDrnJson(String[] drnJson) {
		this.drnJson = drnJson;
	}
	public String getFolder_str() {
		return folder_str;
	}
	public void setFolder_str(String folder_str) {
		this.folder_str = folder_str;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getTr_from() {
		return tr_from;
	}
	public void setTr_from(String tr_from) {
		this.tr_from = tr_from;
	}
	public String getTr_to() {
		return tr_to;
	}
	public void setTr_to(String tr_to) {
		this.tr_to = tr_to;
	}
	public String getSearchOpt() {
		return searchOpt;
	}
	public void setSearchOpt(String searchOpt) {
		this.searchOpt = searchOpt;
	}
	public String getBasic_sel_doc_no() {
		return basic_sel_doc_no;
	}
	public void setBasic_sel_doc_no(String basic_sel_doc_no) {
		this.basic_sel_doc_no = basic_sel_doc_no;
	}
	public String getBasic_input_doc_no() {
		return basic_input_doc_no;
	}
	public void setBasic_input_doc_no(String basic_input_doc_no) {
		this.basic_input_doc_no = basic_input_doc_no;
	}
	public String getBasic_sel_title() {
		return basic_sel_title;
	}
	public void setBasic_sel_title(String basic_sel_title) {
		this.basic_sel_title = basic_sel_title;
	}
	public String getBasic_input_title() {
		return basic_input_title;
	}
	public void setBasic_input_title(String basic_input_title) {
		this.basic_input_title = basic_input_title;
	}
	public String getDetail_sel_doc_type() {
		return detail_sel_doc_type;
	}
	public void setDetail_sel_doc_type(String detail_sel_doc_type) {
		this.detail_sel_doc_type = detail_sel_doc_type;
	}
	public String getDetail_input_modifier() {
		return detail_input_modifier;
	}
	public void setDetail_input_modifier(String detail_input_modifier) {
		this.detail_input_modifier = detail_input_modifier;
	}
	public boolean isMod_periodChk() {
		return mod_periodChk;
	}
	public void setMod_periodChk(boolean mod_periodChk) {
		this.mod_periodChk = mod_periodChk;
	}
	public String getMod_searchFrom() {
		return mod_searchFrom;
	}
	public void setMod_searchFrom(String mod_searchFrom) {
		this.mod_searchFrom = mod_searchFrom;
	}
	public String getMod_searchTo() {
		return mod_searchTo;
	}
	public void setMod_searchTo(String mod_searchTo) {
		this.mod_searchTo = mod_searchTo;
	}
	public String getTrType() {
		return trType;
	}
	public void setTrType(String trType) {
		this.trType = trType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRev_id() {
		return rev_id;
	}
	public void setRev_id(String rev_id) {
		this.rev_id = rev_id;
	}
	public String getDiscipline_code_id() {
		return discipline_code_id;
	}
	public void setDiscipline_code_id(String discipline_code_id) {
		this.discipline_code_id = discipline_code_id;
	}
	public String getDrn_id() {
		return drn_id;
	}
	public void setDrn_id(String drn_id) {
		this.drn_id = drn_id;
	}
	public String getDrn_prj_id() {
		return drn_prj_id;
	}
	public void setDrn_prj_id(String drn_prj_id) {
		this.drn_prj_id = drn_prj_id;
	}
	public String getApproval_id() {
		return approval_id;
	}
	public void setApproval_id(String approval_id) {
		this.approval_id = approval_id;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getPrj_list() {
		return prj_list;
	}
	public void setPrj_list(String prj_list) {
		this.prj_list = prj_list;
	}
	public void setAllPrjchk(boolean allPrjchk) {
		this.allPrjchk = allPrjchk;
	}
	public int getMax_Display() {
		return max_Display;
	}
	public void setMax_Display(int max_Display) {
		this.max_Display = max_Display;
	}
	public String getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
	public long getDoc_seq() {
		return doc_seq;
	}
	public void setDoc_seq(long doc_seq) {
		this.doc_seq = doc_seq;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
	public String getRev_no() {
		return rev_no;
	}
	public void setRev_no(String rev_no) {
		this.rev_no = rev_no;
	}
	public String getSfile_nm() {
		return sfile_nm;
	}
	public void setSfile_nm(String sfile_nm) {
		this.sfile_nm = sfile_nm;
	}
	public String getRfile_nm() {
		return rfile_nm;
	}
	public void setRfile_nm(String rfile_nm) {
		this.rfile_nm = rfile_nm;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
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
	public String getTr_id() {
		return tr_id;
	}
	public void setTr_id(String tr_id) {
		this.tr_id = tr_id;
	}
	public String getTr_no() {
		return tr_no;
	}
	public void setTr_no(String tr_no) {
		this.tr_no = tr_no;
	}
	public String getItr_no() {
		return itr_no;
	}
	public void setItr_no(String itr_no) {
		this.itr_no = itr_no;
	}
	public String getTr_subject() {
		return tr_subject;
	}
	public void setTr_subject(String tr_subject) {
		this.tr_subject = tr_subject;
	}
	public String getInout() {
		return inout;
	}
	public void setInout(String inout) {
		this.inout = inout;
	}
	public String getTr_status() {
		return tr_status;
	}
	public void setTr_status(String tr_status) {
		this.tr_status = tr_status;
	}
	public String getTr_issuepur() {
		return tr_issuepur;
	}
	public void setTr_issuepur(String tr_issuepur) {
		this.tr_issuepur = tr_issuepur;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getReq_date() {
		return req_date;
	}
	public void setReq_date(String req_date) {
		this.req_date = req_date;
	}
	public String getTr_writer_id() {
		return tr_writer_id;
	}
	public void setTr_writer_id(String tr_writer_id) {
		this.tr_writer_id = tr_writer_id;
	}
	public String getTr_modifier_id() {
		return tr_modifier_id;
	}
	public void setTr_modifier_id(String tr_modifier_id) {
		this.tr_modifier_id = tr_modifier_id;
	}
	public String getChk_date() {
		return chk_date;
	}
	public void setChk_date(String chk_date) {
		this.chk_date = chk_date;
	}
	public String getTr_remark() {
		return tr_remark;
	}
	public void setTr_remark(String tr_remark) {
		this.tr_remark = tr_remark;
	}
	public String getTr_distribute() {
		return tr_distribute;
	}
	public void setTr_distribute(String tr_distribute) {
		this.tr_distribute = tr_distribute;
	}
	public String getRef_tr_no() {
		return ref_tr_no;
	}
	public void setRef_tr_no(String ref_tr_no) {
		this.ref_tr_no = ref_tr_no;
	}
	public String getDrn_no() {
		return drn_no;
	}
	public void setDrn_no(String drn_no) {
		this.drn_no = drn_no;
	}
	public String getDrn_title() {
		return drn_title;
	}
	public void setDrn_title(String drn_title) {
		this.drn_title = drn_title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDoc_title() {
		return doc_title;
	}
	public void setDoc_title(String doc_title) {
		this.doc_title = doc_title;
	}
	public String getCheck_sheet_id() {
		return check_sheet_id;
	}
	public void setCheck_sheet_id(String check_sheet_id) {
		this.check_sheet_id = check_sheet_id;
	}
	public String getCheck_sheet_title() {
		return check_sheet_title;
	}
	public void setCheck_sheet_title(String check_sheet_title) {
		this.check_sheet_title = check_sheet_title;
	}
	public String getReview_conclusion() {
		return review_conclusion;
	}
	public void setReview_conclusion(String review_conclusion) {
		this.review_conclusion = review_conclusion;
	}
	
	public String getDrn_approval_status() {
		return drn_approval_status;
	}
	public void setDrn_approval_status(String drn_approval_status) {
		this.drn_approval_status = drn_approval_status;
	}
	public String getPrj_nm() {
		return prj_nm;
	}
	public void setPrj_nm(String prj_nm) {
		this.prj_nm = prj_nm;
	}
	public String getFolder_path() {
		return folder_path;
	}
	public void setFolder_path(String folder_path) {
		this.folder_path = folder_path;
	}
	public String getFolder_nm() {
		return folder_nm;
	}
	public void setFolder_nm(String folder_nm) {
		this.folder_nm = folder_nm;
	}
	public String getDiscip_display() {
		return discip_display;
	}
	public void setDiscip_display(String discip_display) {
		this.discip_display = discip_display;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public boolean isPeriodChk() {
		return periodChk;
	}
	public void setPeriodChk(boolean periodChk) {
		this.periodChk = periodChk;
	}
	public String getSearchFrom() {
		return searchFrom;
	}
	public void setSearchFrom(String searchFrom) {
		this.searchFrom = searchFrom;
	}
	public String getSearchTo() {
		return searchTo;
	}
	public void setSearchTo(String searchTo) {
		this.searchTo = searchTo;
	}
	
	
	
	
	
	
	
	
	
	
	

}
