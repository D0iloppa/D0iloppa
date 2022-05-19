package kr.co.doiloppa.model;

import java.util.List;


public class PrjDocumentIndexVO {

	String tr_info_table;
	String tr_file_table;
	String tr_issue_set_code_type;
	
	String prj_nm;
	String prj_id;
	String prj_no;
	String doc_id;
	String rev_id;
	long folder_id;
	String doc_no;
	String doc_type;
	String rev_code_id;
	String sfile_nm;
	String rfile_nm;
	String file_type;
	long file_size;
	String title;
	String del_yn;
	String unread_yn;
	String lock_yn;
	String attach_file_date;
	String last_rev_yn;
	String trans_ref_doc_id;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	String status;
	
	int corrLength;
	
	String docid_revid;
	String change_doc_no;

	String filterKeyword;
	String tr_id;
	String issue_step;
	String return_status;
	String discip;
	String category;
	String current_folder_doc_type;
	String select_discipline;
	
	String discip_code_id;
	String designer_id;
	String designer;
	String prfe_designer_id;
	String wbs_code_id;
	String wbs_code;
	String wbs_desc;
	String wgtval;
	String discipline;
	String remark;
	String category_cd_id;
	String size_code_id;
	String doc_type_id;
	String discip_display;
	
	String po_doc_no;
	String po_doc_title;
	String vendor_nm;
	String vendor;
	String design_part;
	String por_no;
	String po_no;
	String por_issue_date;
	String po_issue_date;
	String vendor_doc_no;
	String in_out;
	String doc_date;
	String sender_code_id;
	String receiver_code_id;
	String doc_type_code_id;
	String sender_code;
	String receiver_code;
	double serial;
	String corr_no;
	String ref_corr_doc_no;
	String ref_corr_doc_id;
	String responsibility_code_id;
	String responsibility_code;
	String originator;
	String disact;
	String replyreqdate;
	String indistdate;
	String inactdate;
	String userdata00;
	String userdata01;
	String userdata02;
	String userdata03;
	String userdata04;
	String replydocno;
	String sys_corr_no;
	String send_recv_date;
	String person_in_charge_id;
	String item_info_code_id;
	String item_info_code;
	String item_note;
	long ref_grp;
	String refYN;
	String email_send_date;
	
	String p_rev;
	String pk;
	String inout;
	
	String rev_no;
	String select_rev_version;
	String reg_nm;
	String mod_nm;
	String reg_kor_nm;
	String mod_kor_nm;
	String discip_code;
	String category_code;
	String category_display;
	String size_code;
	String doc_type_code;
	String rev_max;
	String designer_nm;
	String pre_designer_nm;
	String popup_new_or_update;
	String origin_rev_code_id;
	String old_doc_no;
	String origin_rev_id;
	String file_value;
	
	String user_id;
	
	// sqlMap으로 concat해서 넘길 데이터
	String concatFolderPath;
	// json 리스트
	String[] jsonRowDatas;
	String jsonString;
	
	// date
	String step_code;
	String plan_date;
	String actual_date;
	String fore_date;
	
	String ifcReason_Code;
	String category_cd;
	String fileName; // 저장한 파일 네임
	String current_folder_path; // 현재 Path 정보 저장
	String process_type;
	
	
	List<String> stepData;
	
	boolean overwriteMode;
	

	String renameYN;
	String fileRenameSet;
	String downloadFiles;
	String duplicationDoc;
	String rtn_status_code_id;
	
	String tbl_type;
	String set_code_type;
	String set_code;
	String set_code_id;
	

	String step_code_id;

	String rtn_status;
	String step_seq;
	
	String set_val,set_val2,set_val3,set_val4;
	
	//access
	String remote_ip;
	String method;
	
	String folder_path_str;
	String folder_str;
	
	
	
	public String getFolder_str() {
		return folder_str;
	}
	public void setFolder_str(String folder_str) {
		this.folder_str = folder_str;
	}
	public String getDesigner() {
		return designer;
	}
	public void setDesigner(String designer) {
		this.designer = designer;
	}
	public String getFolder_path_str() {
		return folder_path_str;
	}
	public void setFolder_path_str(String folder_path_str) {
		this.folder_path_str = folder_path_str;
	}
	public String getTr_issue_set_code_type() {
		return tr_issue_set_code_type;
	}
	public void setTr_issue_set_code_type(String tr_issue_set_code_type) {
		this.tr_issue_set_code_type = tr_issue_set_code_type;
	}
	public String getTr_info_table() {
		return tr_info_table;
	}
	public void setTr_info_table(String tr_info_table) {
		this.tr_info_table = tr_info_table;
	}
	public String getTr_file_table() {
		return tr_file_table;
	}
	public void setTr_file_table(String tr_file_table) {
		this.tr_file_table = tr_file_table;
	}
	public String getChange_doc_no() {
		return change_doc_no;
	}
	public void setChange_doc_no(String change_doc_no) {
		this.change_doc_no = change_doc_no;
	}
	public String getPrj_no() {
		return prj_no;
	}
	public void setPrj_no(String prj_no) {
		this.prj_no = prj_no;
	}
	public String getReg_kor_nm() {
		return reg_kor_nm;
	}
	public void setReg_kor_nm(String reg_kor_nm) {
		this.reg_kor_nm = reg_kor_nm;
	}
	public String getMod_kor_nm() {
		return mod_kor_nm;
	}
	public void setMod_kor_nm(String mod_kor_nm) {
		this.mod_kor_nm = mod_kor_nm;
	}
	public String getDocid_revid() {
		return docid_revid;
	}
	public void setDocid_revid(String docid_revid) {
		this.docid_revid = docid_revid;
	}
	public String getProcess_type() {
		return process_type;
	}
	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}
	public String getFilterKeyword() {
		return filterKeyword;
	}
	public void setFilterKeyword(String filterKeyword) {
		this.filterKeyword = filterKeyword;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getEmail_send_date() {
		return email_send_date;
	}
	public void setEmail_send_date(String email_send_date) {
		this.email_send_date = email_send_date;
	}
	public String getRefYN() {
		return refYN;
	}
	public void setRefYN(String refYN) {
		this.refYN = refYN;
	}
	public long getRef_grp() {
		return ref_grp;
	}
	public void setRef_grp(long ref_grp) {
		this.ref_grp = ref_grp;
	}
	public String getRef_corr_doc_id() {
		return ref_corr_doc_id;
	}
	public void setRef_corr_doc_id(String ref_corr_doc_id) {
		this.ref_corr_doc_id = ref_corr_doc_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getItem_info_code() {
		return item_info_code;
	}
	public void setItem_info_code(String item_info_code) {
		this.item_info_code = item_info_code;
	}
	public String getSys_corr_no() {
		return sys_corr_no;
	}
	public void setSys_corr_no(String sys_corr_no) {
		this.sys_corr_no = sys_corr_no;
	}
	public String getSend_recv_date() {
		return send_recv_date;
	}
	public void setSend_recv_date(String send_recv_date) {
		this.send_recv_date = send_recv_date;
	}
	public String getPerson_in_charge_id() {
		return person_in_charge_id;
	}
	public void setPerson_in_charge_id(String person_in_charge_id) {
		this.person_in_charge_id = person_in_charge_id;
	}
	public String getItem_info_code_id() {
		return item_info_code_id;
	}
	public void setItem_info_code_id(String item_info_code_id) {
		this.item_info_code_id = item_info_code_id;
	}
	public String getItem_note() {
		return item_note;
	}
	public void setItem_note(String item_note) {
		this.item_note = item_note;
	}
	public int getCorrLength() {
		return corrLength;
	}
	public void setCorrLength(int corrLength) {
		this.corrLength = corrLength;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getStep_code_id() {
		return step_code_id;
	}
	public void setStep_code_id(String step_code_id) {
		this.step_code_id = step_code_id;
	}
	public String getRtn_status() {
		return rtn_status;
	}
	public void setRtn_status(String rtn_status) {
		this.rtn_status = rtn_status;
	}
	public String getStep_seq() {
		return step_seq;
	}
	public void setStep_seq(String step_seq) {
		this.step_seq = step_seq;
	}
	public String getSet_val() {
		return set_val;
	}
	public void setSet_val(String set_val) {
		this.set_val = set_val;
	}
	public String getSet_val2() {
		return set_val2;
	}
	public void setSet_val2(String set_val2) {
		this.set_val2 = set_val2;
	}
	public String getSet_val3() {
		return set_val3;
	}
	public void setSet_val3(String set_val3) {
		this.set_val3 = set_val3;
	}
	public String getSet_val4() {
		return set_val4;
	}
	public void setSet_val4(String set_val4) {
		this.set_val4 = set_val4;
	}
	public String getCurrent_folder_doc_type() {
		return current_folder_doc_type;
	}
	public void setCurrent_folder_doc_type(String current_folder_doc_type) {
		this.current_folder_doc_type = current_folder_doc_type;
	}
	public String getSelect_discipline() {
		return select_discipline;
	}
	public void setSelect_discipline(String select_discipline) {
		this.select_discipline = select_discipline;
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
	public String getRev_code_id() {
		return rev_code_id;
	}
	public void setRev_code_id(String rev_code_id) {
		this.rev_code_id = rev_code_id;
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
	public long getFile_size() {
		return file_size;
	}
	public void setFile_size(long file_size) {
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
	public String getUnread_yn() {
		return unread_yn;
	}
	public void setUnread_yn(String unread_yn) {
		this.unread_yn = unread_yn;
	}
	public String getLock_yn() {
		return lock_yn;
	}
	public void setLock_yn(String lock_yn) {
		this.lock_yn = lock_yn;
	}
	public String getAttach_file_date() {
		return attach_file_date;
	}
	public void setAttach_file_date(String attach_file_date) {
		this.attach_file_date = attach_file_date;
	}
	public String getLast_rev_yn() {
		return last_rev_yn;
	}
	public void setLast_rev_yn(String last_rev_yn) {
		this.last_rev_yn = last_rev_yn;
	}
	public String getTrans_ref_doc_id() {
		return trans_ref_doc_id;
	}
	public void setTrans_ref_doc_id(String trans_ref_doc_id) {
		this.trans_ref_doc_id = trans_ref_doc_id;
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
	public String getIssue_step() {
		return issue_step;
	}
	public void setIssue_step(String issue_step) {
		this.issue_step = issue_step;
	}
	public String getReturn_status() {
		return return_status;
	}
	public void setReturn_status(String return_status) {
		this.return_status = return_status;
	}
	public String getDiscip() {
		return discip;
	}
	public void setDiscip(String discip) {
		this.discip = discip;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDiscip_code_id() {
		return discip_code_id;
	}
	public void setDiscip_code_id(String discip_code_id) {
		this.discip_code_id = discip_code_id;
	}
	public String getDesigner_id() {
		return designer_id;
	}
	public void setDesigner_id(String designer_id) {
		this.designer_id = designer_id;
	}
	public String getPrfe_designer_id() {
		return prfe_designer_id;
	}
	public void setPrfe_designer_id(String prfe_designer_id) {
		this.prfe_designer_id = prfe_designer_id;
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
	public String getWgtval() {
		return wgtval;
	}
	public void setWgtval(String wgtval) {
		this.wgtval = wgtval;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCategory_cd_id() {
		return category_cd_id;
	}
	public void setCategory_cd_id(String category_cd_id) {
		this.category_cd_id = category_cd_id;
	}
	public String getSize_code_id() {
		return size_code_id;
	}
	public void setSize_code_id(String size_code_id) {
		this.size_code_id = size_code_id;
	}
	public String getDoc_type_id() {
		return doc_type_id;
	}
	public void setDoc_type_id(String doc_type_id) {
		this.doc_type_id = doc_type_id;
	}
	public String getDiscip_display() {
		return discip_display;
	}
	public void setDiscip_display(String discip_display) {
		this.discip_display = discip_display;
	}
	public String getPo_doc_no() {
		return po_doc_no;
	}
	public void setPo_doc_no(String po_doc_no) {
		this.po_doc_no = po_doc_no;
	}
	public String getPo_doc_title() {
		return po_doc_title;
	}
	public void setPo_doc_title(String po_doc_title) {
		this.po_doc_title = po_doc_title;
	}
	public String getVendor_nm() {
		return vendor_nm;
	}
	public void setVendor_nm(String vendor_nm) {
		this.vendor_nm = vendor_nm;
	}
	public String getDesign_part() {
		return design_part;
	}
	public void setDesign_part(String design_part) {
		this.design_part = design_part;
	}
	public String getPor_no() {
		return por_no;
	}
	public void setPor_no(String por_no) {
		this.por_no = por_no;
	}
	public String getPo_no() {
		return po_no;
	}
	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}
	public String getPor_issue_date() {
		return por_issue_date;
	}
	public void setPor_issue_date(String por_issue_date) {
		this.por_issue_date = por_issue_date;
	}
	public String getPo_issue_date() {
		return po_issue_date;
	}
	public void setPo_issue_date(String po_issue_date) {
		this.po_issue_date = po_issue_date;
	}
	public String getVendor_doc_no() {
		return vendor_doc_no;
	}
	public void setVendor_doc_no(String vendor_doc_no) {
		this.vendor_doc_no = vendor_doc_no;
	}
	public String getIn_out() {
		return in_out;
	}
	public void setIn_out(String in_out) {
		this.in_out = in_out;
	}
	public String getDoc_date() {
		return doc_date;
	}
	public void setDoc_date(String doc_date) {
		this.doc_date = doc_date;
	}
	public String getSender_code_id() {
		return sender_code_id;
	}
	public void setSender_code_id(String sender_code_id) {
		this.sender_code_id = sender_code_id;
	}
	public String getReceiver_code_id() {
		return receiver_code_id;
	}
	public void setReceiver_code_id(String receiver_code_id) {
		this.receiver_code_id = receiver_code_id;
	}
	public String getDoc_type_code_id() {
		return doc_type_code_id;
	}
	public void setDoc_type_code_id(String doc_type_code_id) {
		this.doc_type_code_id = doc_type_code_id;
	}
	public String getSender_code() {
		return sender_code;
	}
	public void setSender_code(String sender_code) {
		this.sender_code = sender_code;
	}
	public String getReceiver_code() {
		return receiver_code;
	}
	public void setReceiver_code(String receiver_code) {
		this.receiver_code = receiver_code;
	}
	public double getSerial() {
		return serial;
	}
	public void setSerial(double serial) {
		this.serial = serial;
	}
	public String getCorr_no() {
		return corr_no;
	}
	public void setCorr_no(String corr_no) {
		this.corr_no = corr_no;
	}
	public String getRef_corr_doc_no() {
		return ref_corr_doc_no;
	}
	public void setRef_corr_doc_no(String ref_corr_doc_no) {
		this.ref_corr_doc_no = ref_corr_doc_no;
	}
	public String getResponsibility_code_id() {
		return responsibility_code_id;
	}
	public void setResponsibility_code_id(String responsibility_code_id) {
		this.responsibility_code_id = responsibility_code_id;
	}
	public String getResponsibility_code() {
		return responsibility_code;
	}
	public void setResponsibility_code(String responsibility_code) {
		this.responsibility_code = responsibility_code;
	}
	public String getOriginator() {
		return originator;
	}
	public void setOriginator(String originator) {
		this.originator = originator;
	}
	public String getDisact() {
		return disact;
	}
	public void setDisact(String disact) {
		this.disact = disact;
	}
	public String getReplyreqdate() {
		return replyreqdate;
	}
	public void setReplyreqdate(String replyreqdate) {
		this.replyreqdate = replyreqdate;
	}
	public String getIndistdate() {
		return indistdate;
	}
	public void setIndistdate(String indistdate) {
		this.indistdate = indistdate;
	}
	public String getInactdate() {
		return inactdate;
	}
	public void setInactdate(String inactdate) {
		this.inactdate = inactdate;
	}
	public String getUserdata00() {
		return userdata00;
	}
	public void setUserdata00(String userdata00) {
		this.userdata00 = userdata00;
	}
	public String getUserdata01() {
		return userdata01;
	}
	public void setUserdata01(String userdata01) {
		this.userdata01 = userdata01;
	}
	public String getUserdata02() {
		return userdata02;
	}
	public void setUserdata02(String userdata02) {
		this.userdata02 = userdata02;
	}
	public String getUserdata03() {
		return userdata03;
	}
	public void setUserdata03(String userdata03) {
		this.userdata03 = userdata03;
	}
	public String getUserdata04() {
		return userdata04;
	}
	public void setUserdata04(String userdata04) {
		this.userdata04 = userdata04;
	}
	public String getReplydocno() {
		return replydocno;
	}
	public void setReplydocno(String replydocno) {
		this.replydocno = replydocno;
	}
	public String getP_rev() {
		return p_rev;
	}
	public void setP_rev(String p_rev) {
		this.p_rev = p_rev;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getInout() {
		return inout;
	}
	public void setInout(String inout) {
		this.inout = inout;
	}
	public String getRev_no() {
		return rev_no;
	}
	public void setRev_no(String rev_no) {
		this.rev_no = rev_no;
	}
	public String getSelect_rev_version() {
		return select_rev_version;
	}
	public void setSelect_rev_version(String select_rev_version) {
		this.select_rev_version = select_rev_version;
	}
	public String getReg_nm() {
		return reg_nm;
	}
	public void setReg_nm(String reg_nm) {
		this.reg_nm = reg_nm;
	}
	public String getMod_nm() {
		return mod_nm;
	}
	public void setMod_nm(String mod_nm) {
		this.mod_nm = mod_nm;
	}
	public String getDiscip_code() {
		return discip_code;
	}
	public void setDiscip_code(String discip_code) {
		this.discip_code = discip_code;
	}
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getCategory_display() {
		return category_display;
	}
	public void setCategory_display(String category_display) {
		this.category_display = category_display;
	}
	public String getSize_code() {
		return size_code;
	}
	public void setSize_code(String size_code) {
		this.size_code = size_code;
	}
	public String getDoc_type_code() {
		return doc_type_code;
	}
	public void setDoc_type_code(String doc_type_code) {
		this.doc_type_code = doc_type_code;
	}
	public String getRev_max() {
		return rev_max;
	}
	public void setRev_max(String rev_max) {
		this.rev_max = rev_max;
	}
	public String getDesigner_nm() {
		return designer_nm;
	}
	public void setDesigner_nm(String designer_nm) {
		this.designer_nm = designer_nm;
	}
	public String getPre_designer_nm() {
		return pre_designer_nm;
	}
	public void setPre_designer_nm(String pre_designer_nm) {
		this.pre_designer_nm = pre_designer_nm;
	}
	public String getPopup_new_or_update() {
		return popup_new_or_update;
	}
	public void setPopup_new_or_update(String popup_new_or_update) {
		this.popup_new_or_update = popup_new_or_update;
	}
	public String getOrigin_rev_code_id() {
		return origin_rev_code_id;
	}
	public void setOrigin_rev_code_id(String origin_rev_code_id) {
		this.origin_rev_code_id = origin_rev_code_id;
	}
	public String getOld_doc_no() {
		return old_doc_no;
	}
	public void setOld_doc_no(String old_doc_no) {
		this.old_doc_no = old_doc_no;
	}
	public String getOrigin_rev_id() {
		return origin_rev_id;
	}
	public void setOrigin_rev_id(String origin_rev_id) {
		this.origin_rev_id = origin_rev_id;
	}
	public String getFile_value() {
		return file_value;
	}
	public void setFile_value(String file_value) {
		this.file_value = file_value;
	}
	public String getConcatFolderPath() {
		return concatFolderPath;
	}
	public void setConcatFolderPath(String concatFolderPath) {
		this.concatFolderPath = concatFolderPath;
	}
	
	
	public String[] getJsonRowDatas() {
		return jsonRowDatas;
	}
	public void setJsonRowDatas(String[] jsonRowDatas) {
		this.jsonRowDatas = jsonRowDatas;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public String getStep_code() {
		return step_code;
	}
	public void setStep_code(String step_code) {
		this.step_code = step_code;
	}
	public String getPlan_date() {
		return plan_date;
	}
	public void setPlan_date(String plan_date) {
		this.plan_date = plan_date;
	}
	public String getActual_date() {
		return actual_date;
	}
	public void setActual_date(String actual_date) {
		this.actual_date = actual_date;
	}
	public String getFore_date() {
		return fore_date;
	}
	public void setFore_date(String fore_date) {
		this.fore_date = fore_date;
	}
	public String getIfcReason_Code() {
		return ifcReason_Code;
	}
	public void setIfcReason_Code(String ifcReason_Code) {
		this.ifcReason_Code = ifcReason_Code;
	}
	public String getCategory_cd() {
		return category_cd;
	}
	public void setCategory_cd(String category_cd) {
		this.category_cd = category_cd;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCurrent_folder_path() {
		return current_folder_path;
	}
	public void setCurrent_folder_path(String current_folder_path) {
		this.current_folder_path = current_folder_path;
	}
	public List<String> getStepData() {
		return stepData;
	}
	public void setStepData(List<String> stepData) {
		this.stepData = stepData;
	}
	public boolean isOverwriteMode() {
		return overwriteMode;
	}
	public void setOverwriteMode(boolean overwriteMode) {
		this.overwriteMode = overwriteMode;
	}
	public String getRenameYN() {
		return renameYN;
	}
	public void setRenameYN(String renameYN) {
		this.renameYN = renameYN;
	}
	public String getFileRenameSet() {
		return fileRenameSet;
	}
	public void setFileRenameSet(String fileRenameSet) {
		this.fileRenameSet = fileRenameSet;
	}
	public String getDownloadFiles() {
		return downloadFiles;
	}
	public void setDownloadFiles(String downloadFiles) {
		this.downloadFiles = downloadFiles;
	}
	public String getDuplicationDoc() {
		return duplicationDoc;
	}
	public void setDuplicationDoc(String duplicationDoc) {
		this.duplicationDoc = duplicationDoc;
	}
	public String getRtn_status_code_id() {
		return rtn_status_code_id;
	}
	public void setRtn_status_code_id(String rtn_status_code_id) {
		this.rtn_status_code_id = rtn_status_code_id;
	}
	public String getTbl_type() {
		return tbl_type;
	}
	public void setTbl_type(String tbl_type) {
		this.tbl_type = tbl_type;
	}
	public String getSet_code_type() {
		return set_code_type;
	}
	public void setSet_code_type(String set_code_type) {
		this.set_code_type = set_code_type;
	}
	public String getSet_code() {
		return set_code;
	}
	public void setSet_code(String set_code) {
		this.set_code = set_code;
	}
	public String getSet_code_id() {
		return set_code_id;
	}
	public void setSet_code_id(String set_code_id) {
		this.set_code_id = set_code_id;
	}
	
	
	
}
