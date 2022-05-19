/**
 * 
 */
package kr.co.hhi.model;
import java.util.HashMap;

public class DrnLineVO {
	
	
	
	String prj_id;
	String drn_id;
	String drn_no;
	String drn_title;
	String drn_prj_id;
	String status;
	String doc_title;
	String doc_type;
	String doc_id;
	String rev_id;
	String rev_no;
	String rev_code_id;
	String revision;
	
	long approval_id;
	String approval_date;
	String check_sheet_id;
	String check_sheet_title;
	String review_conclusion;
	String drn_approval_status;
	long folder_id;
	String folder_path;
	String reg_id;
	String reg_date;
	String mod_id;       
	String mod_date;
	String designed_id;
	String reviewed_id;
	String approved_id;
	String distribute_id;
	String dcc_user_id;
	long items_no;
	String items_nm;
	int itmes_cnt;
	long items_id;
	long items_seq;
	String check_items;
	String discipline_code_id;
	String discip_code_id,discip_code,discip_display;
	String design_check_status;
	String reviewed_check_status;
	String approved_check_status;

	
	String user_id;
	String user_kor_nm;
	String designed;
	String designer_code;
	String reviewed;
	String reviewer_code;
	String approved;
	String approver_code;
	String distributers;
	
	String dgn_sfile;
	String rev_sfile;
	String app_sfile;
	
	
	String type;
	String mode;
	String action; // 어떠한 동작을 수행할 지에 대한 값
	String rejectReason; // 반려사유
	String comment;
	
	String pageData;
	

	String rfile_nm;
	String sfile_nm;
	String file_size;
	String file_path;
	String doc_no;
	String title;
	
	HashMap<String,String> drnStatus;
	
	
	
	
	String jsonData;
	String jsonData2;
	
	// discipline
	String code;
	String display;
	String code_id;
	
	// project no관련
	String prj_no;
	String prj_nm;
	
	// 이메일에서 처리하는지 식별자
	boolean email;
	String action_id; // 작업 수행하는 사람의 id

	
	
	
	
	
	
	
	
	
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDesigner_code() {
		return designer_code;
	}

	public void setDesigner_code(String designer_code) {
		this.designer_code = designer_code;
	}

	public String getReviewer_code() {
		return reviewer_code;
	}

	public void setReviewer_code(String reviewer_code) {
		this.reviewer_code = reviewer_code;
	}

	public String getApprover_code() {
		return approver_code;
	}

	public void setApprover_code(String approver_code) {
		this.approver_code = approver_code;
	}

	public String getDiscip_code_id() {
		return discip_code_id;
	}

	public void setDiscip_code_id(String discip_code_id) {
		this.discip_code_id = discip_code_id;
	}

	public String getDiscip_code() {
		return discip_code;
	}

	public void setDiscip_code(String discip_code) {
		this.discip_code = discip_code;
	}

	public String getDiscip_display() {
		return discip_display;
	}

	public void setDiscip_display(String discip_display) {
		this.discip_display = discip_display;
	}

	public HashMap<String, String> getDrnStatus() {
		return drnStatus;
	}

	public void setDrnStatus(HashMap<String, String> drnStatus) {
		this.drnStatus = drnStatus;
	}

	public String getDesigned_id() {
		return designed_id;
	}

	public void setDesigned_id(String designed_id) {
		this.designed_id = designed_id;
	}

	public String getDgn_sfile() {
		return dgn_sfile;
	}

	public void setDgn_sfile(String dgn_sfile) {
		this.dgn_sfile = dgn_sfile;
	}

	public String getRev_sfile() {
		return rev_sfile;
	}

	public void setRev_sfile(String rev_sfile) {
		this.rev_sfile = rev_sfile;
	}

	public String getApp_sfile() {
		return app_sfile;
	}

	public void setApp_sfile(String app_sfile) {
		this.app_sfile = app_sfile;
	}

	public String getDesigned() {
		return designed;
	}

	public void setDesigned(String designed) {
		this.designed = designed;
	}


	public String getRev_code_id() {
		return rev_code_id;
	}

	public void setRev_code_id(String rev_code_id) {
		this.rev_code_id = rev_code_id;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getAction_id() {
		return action_id;
	}

	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
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

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRev_no() {
		return rev_no;
	}

	public void setRev_no(String rev_no) {
		this.rev_no = rev_no;
	}

	public String getApproved_check_status() {
		return approved_check_status;
	}

	public void setApproved_check_status(String approved_check_status) {
		this.approved_check_status = approved_check_status;
	}


	
	public String getReviewed_check_status() {
		return reviewed_check_status;
	}

	public void setReviewed_check_status(String reviewed_check_status) {
		this.reviewed_check_status = reviewed_check_status;
	}

	public String getDesign_check_status() {
		return design_check_status;
	}

	public void setDesign_check_status(String design_check_status) {
		this.design_check_status = design_check_status;
	}

	public String getDistributers() {
		return distributers;
	}

	public void setDistributers(String distributers) {
		this.distributers = distributers;
	}

	public String getReviewed() {
		return reviewed;
	}

	public void setReviewed(String reviewed) {
		this.reviewed = reviewed;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getDoc_no() {
		return doc_no;
	}

	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getCode_id() {
		return code_id;
	}

	public void setCode_id(String code_id) {
		this.code_id = code_id;
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

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
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

	public String getApproval_date() {
		return approval_date;
	}

	public void setApproval_date(String approval_date) {
		this.approval_date = approval_date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPageData() {
		return pageData;
	}

	public void setPageData(String pageData) {
		this.pageData = pageData;
	}

	public String getJsonData2() {
		return jsonData2;
	}

	public void setJsonData2(String jsonData2) {
		this.jsonData2 = jsonData2;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPrj_id() {
		return prj_id;
	}

	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

	public String getDrn_id() {
		return drn_id;
	}

	public void setDrn_id(String drn_id) {
		this.drn_id = drn_id;
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

	public String getDrn_prj_id() {
		return drn_prj_id;
	}

	public void setDrn_prj_id(String drn_prj_id) {
		this.drn_prj_id = drn_prj_id;
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

	public long getApproval_id() {
		return approval_id;
	}

	public void setApproval_id(long approval_id) {
		this.approval_id = approval_id;
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

	public String getReviewed_id() {
		return reviewed_id;
	}

	public void setReviewed_id(String reviewed_id) {
		this.reviewed_id = reviewed_id;
	}

	public String getApproved_id() {
		return approved_id;
	}

	public void setApproved_id(String approved_id) {
		this.approved_id = approved_id;
	}

	public String getDistribute_id() {
		return distribute_id;
	}

	public void setDistribute_id(String distribute_id) {
		this.distribute_id = distribute_id;
	}

	public String getDcc_user_id() {
		return dcc_user_id;
	}

	public void setDcc_user_id(String dcc_user_id) {
		this.dcc_user_id = dcc_user_id;
	}

	public long getItems_no() {
		return items_no;
	}

	public void setItems_no(long items_no) {
		this.items_no = items_no;
	}

	public String getItems_nm() {
		return items_nm;
	}

	public void setItems_nm(String items_nm) {
		this.items_nm = items_nm;
	}

	public int getItmes_cnt() {
		return itmes_cnt;
	}

	public void setItmes_cnt(int itmes_cnt) {
		this.itmes_cnt = itmes_cnt;
	}

	public long getItems_id() {
		return items_id;
	}

	public void setItems_id(long items_id) {
		this.items_id = items_id;
	}

	public long getItems_seq() {
		return items_seq;
	}

	public void setItems_seq(long items_seq) {
		this.items_seq = items_seq;
	}

	public String getCheck_items() {
		return check_items;
	}

	public void setCheck_items(String check_items) {
		this.check_items = check_items;
	}

	public String getDiscipline_code_id() {
		return discipline_code_id;
	}

	public void setDiscipline_code_id(String discipline_code_id) {
		this.discipline_code_id = discipline_code_id;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
	

	
	

}
