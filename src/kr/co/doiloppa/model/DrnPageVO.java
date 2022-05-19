/**
 * 
 */
package kr.co.doiloppa.model;

import java.util.HashMap;

/**
 * @author doil
 *
 */
public class DrnPageVO {

	String drn_title;
	String drn_no;
	String drn_id;
	String prj_no;
	String prj_nm;
	String date; 
	String doc_no; 
	String status; 
	String doc_title; 
	String local; 
	String chksheet_nm;
	String discip_code_id;
	String[] sheetArr;
	String[] docData;
	long folder_id;
	
	String signData;	
	String sheetData;
	String check_sheet_id;
	
	
	long items_no;
    String items_nm;
    int items_cnt;
	String[] check_items;
	String[] design_check_status;
	String[] reviewed_check_status;
	String[] approved_check_status;
	
	String review_conclusion;
	
	
	
	
	
	
	public String getDiscip_code_id() {
		return discip_code_id;
	}
	public void setDiscip_code_id(String discip_code_id) {
		this.discip_code_id = discip_code_id;
	}
	public String getDrn_id() {
		return drn_id;
	}
	public void setDrn_id(String drn_id) {
		this.drn_id = drn_id;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String getCheck_sheet_id() {
		return check_sheet_id;
	}
	public void setCheck_sheet_id(String check_sheet_id) {
		this.check_sheet_id = check_sheet_id;
	}
	public String[] getDocData() {
		return docData;
	}
	public void setDocData(String[] docData) {
		this.docData = docData;
	}
	public String getSheetData() {
		return sheetData;
	}
	public void setSheetData(String sheetData) {
		this.sheetData = sheetData;
	}
	public String getReview_conclusion() {
		return review_conclusion;
	}
	public void setReview_conclusion(String review_conclusion) {
		this.review_conclusion = review_conclusion;
	}
	public String[] getSheetArr() {
		return sheetArr;
	}
	public void setSheetArr(String[] sheetArr) {
		this.sheetArr = sheetArr;
	}
	public String getDrn_title() {
		return drn_title;
	}
	public void setDrn_title(String drn_title) {
		this.drn_title = drn_title;
	}
	public String getDrn_no() {
		return drn_no;
	}
	public void setDrn_no(String drn_no) {
		this.drn_no = drn_no;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
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
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getChksheet_nm() {
		return chksheet_nm;
	}
	public void setChksheet_nm(String chksheet_nm) {
		this.chksheet_nm = chksheet_nm;
	}
	
	public String getSignData() {
		return signData;
	}
	public void setSignData(String signData) {
		this.signData = signData;
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
	public int getItems_cnt() {
		return items_cnt;
	}
	public void setItems_cnt(int items_cnt) {
		this.items_cnt = items_cnt;
	}
	public String[] getCheck_items() {
		return check_items;
	}
	public void setCheck_items(String[] check_items) {
		this.check_items = check_items;
	}
	public String[] getDesign_check_status() {
		return design_check_status;
	}
	public void setDesign_check_status(String[] design_check_status) {
		this.design_check_status = design_check_status;
	}
	public String[] getReviewed_check_status() {
		return reviewed_check_status;
	}
	public void setReviewed_check_status(String[] reviewed_check_status) {
		this.reviewed_check_status = reviewed_check_status;
	}
	public String[] getApproved_check_status() {
		return approved_check_status;
	}
	public void setApproved_check_status(String[] approved_check_status) {
		this.approved_check_status = approved_check_status;
	}
	
	
	
	
}
