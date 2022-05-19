package kr.co.hhi.model;

import java.util.ArrayList;
import java.util.List;

public class CollaborationVO {
	String dox_kind;
	String prj_id;
	String drn_id;
	String drn_no;
	String drn_title;
	String drn_prj_id;
	String status;
	String doc_title;
	long approval_id;
	String Check_sheet_id;
	String Check_sheet_title;
	String review_conclusion;
	String drn_approval_status;
	long folder_id;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String from;
	String to;
	
	ArrayList<String> statusList;
	
	
	
	// 날짜 검색 조건
	String old_day;
	String to_day;
	String admin_yn;
	
	String design;
	String review;
	String approve;
	String dcc;
	String modifier;
	
	
	
	

	
	
	
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getDcc() {
		return dcc;
	}
	public void setDcc(String dcc) {
		this.dcc = dcc;
	}
	public String getDesign() {
		return design;
	}
	public void setDesign(String design) {
		this.design = design;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public ArrayList<String> getStatusList() {
		return statusList;
	}
	public void setStatusList(ArrayList<String> statusList) {
		this.statusList = statusList;
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
	public String getDox_kind() {
		return dox_kind;
	}
	public void setDox_kind(String dox_kind) {
		this.dox_kind = dox_kind;
	}
	public String getOld_day() {
		return old_day;
	}
	public void setOld_day(String old_day) {
		this.old_day = old_day;
	}
	public String getTo_day() {
		return to_day;
	}
	public void setTo_day(String to_day) {
		this.to_day = to_day;
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
		return Check_sheet_id;
	}
	public void setCheck_sheet_id(String check_sheet_id) {
		Check_sheet_id = check_sheet_id;
	}
	public String getCheck_sheet_title() {
		return Check_sheet_title;
	}
	public void setCheck_sheet_title(String check_sheet_title) {
		Check_sheet_title = check_sheet_title;
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
	public String getAdmin_yn() {
		return admin_yn;
	}
	public void setAdmin_yn(String admin_yn) {
		this.admin_yn = admin_yn;
	}
	
	
}