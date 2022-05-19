package kr.co.hhi.model;

public class PrjTrVO {
	String prj_id;
	String tr_id;
	String doc_id;
	String rev_id;
	String rev_no;
	String issue_step;
	String rtn_status_code_id;
	String doc_table;
	int serialLength;
	String serial;
	String set_code;
	long folder_id;
	String DCCSendDateUpdate;
	
	String old_tr_no;
	String change_tr_no;
	
	String noForm;
	String stringForm;
	String replaceForm;
	String replaceTrNo;
	String replaceSetVal;
	
	String tr_table;
	
	String[] jsonRowDatas;
	
	String tr_no;
	String itr_no;
	String tr_subject;
	String discipline_code_id;
	String inout;
	String issued;
	String tr_status;
	String tr_issuepur_code_id;
	String send_date;
	String req_date;
	String tr_writer_id;
	String tr_modifier_id;
	String tr_from;
	String tr_to;
	String chk_date;
	String tr_remark;
	String tr_distribute;
	String ref_tr_no;
	String file_type;
	
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String purpose;
	String doc_no;
	String rtn;
	String prj_no;
	String docid_revid_revno;
	String tr_period;
	String tr_from_search;
	String tr_to_search;
	String issue_pur;
	String discip;
	String writer;
	String modifier;
	String writer_kor;
	String modifier_kor;
	String mod_nm;
	String corr_from;
	String corr_to;
	String corr_type;
	int digit;
	String newOrUpdate;
	String issuepurpose;
	String issuepurpose_desc;
	String discipline;
	String return_status;
	String set_code_id;
	String set_code_type;
	String concatStr;
	String rtnUpdate;
	String processType;
	String rfile_nm;
	
	String fileName;
	String prj_nm;
	
	public String getDCCSendDateUpdate() {
		return DCCSendDateUpdate;
	}
	public void setDCCSendDateUpdate(String dCCSendDateUpdate) {
		DCCSendDateUpdate = dCCSendDateUpdate;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getNoForm() {
		return noForm;
	}
	public void setNoForm(String noForm) {
		this.noForm = noForm;
	}
	public String getReplaceTrNo() {
		return replaceTrNo;
	}
	public void setReplaceTrNo(String replaceTrNo) {
		this.replaceTrNo = replaceTrNo;
	}
	public String getReplaceSetVal() {
		return replaceSetVal;
	}
	public void setReplaceSetVal(String replaceSetVal) {
		this.replaceSetVal = replaceSetVal;
	}
	public String getReplaceForm() {
		return replaceForm;
	}
	public void setReplaceForm(String replaceForm) {
		this.replaceForm = replaceForm;
	}
	public String getOld_tr_no() {
		return old_tr_no;
	}
	public void setOld_tr_no(String old_tr_no) {
		this.old_tr_no = old_tr_no;
	}
	public String getChange_tr_no() {
		return change_tr_no;
	}
	public void setChange_tr_no(String change_tr_no) {
		this.change_tr_no = change_tr_no;
	}
	public String getTr_table() {
		return tr_table;
	}
	public void setTr_table(String tr_table) {
		this.tr_table = tr_table;
	}
	public String getWriter_kor() {
		return writer_kor;
	}
	public void setWriter_kor(String writer_kor) {
		this.writer_kor = writer_kor;
	}
	public String getModifier_kor() {
		return modifier_kor;
	}
	public void setModifier_kor(String modifier_kor) {
		this.modifier_kor = modifier_kor;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public String getStringForm() {
		return stringForm;
	}
	public void setStringForm(String stringForm) {
		this.stringForm = stringForm;
	}
	public String getSet_code() {
		return set_code;
	}
	public void setSet_code(String set_code) {
		this.set_code = set_code;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public int getSerialLength() {
		return serialLength;
	}
	public void setSerialLength(int serialLength) {
		this.serialLength = serialLength;
	}
	public String getPrj_nm() {
		return prj_nm;
	}
	public void setPrj_nm(String prj_nm) {
		this.prj_nm = prj_nm;
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
	public String getTr_id() {
		return tr_id;
	}
	public void setTr_id(String tr_id) {
		this.tr_id = tr_id;
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
	public String getRev_no() {
		return rev_no;
	}
	public void setRev_no(String rev_no) {
		this.rev_no = rev_no;
	}
	public String getIssue_step() {
		return issue_step;
	}
	public void setIssue_step(String issue_step) {
		this.issue_step = issue_step;
	}
	public String getRtn_status_code_id() {
		return rtn_status_code_id;
	}
	public void setRtn_status_code_id(String rtn_status_code_id) {
		this.rtn_status_code_id = rtn_status_code_id;
	}
	public String getDoc_table() {
		return doc_table;
	}
	public void setDoc_table(String doc_table) {
		this.doc_table = doc_table;
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
	public String getDiscipline_code_id() {
		return discipline_code_id;
	}
	public void setDiscipline_code_id(String discipline_code_id) {
		this.discipline_code_id = discipline_code_id;
	}
	public String getInout() {
		return inout;
	}
	public void setInout(String inout) {
		this.inout = inout;
	}
	public String getIssued() {
		return issued;
	}
	public void setIssued(String issued) {
		this.issued = issued;
	}
	public String getTr_status() {
		return tr_status;
	}
	public void setTr_status(String tr_status) {
		this.tr_status = tr_status;
	}
	public String getTr_issuepur_code_id() {
		return tr_issuepur_code_id;
	}
	public void setTr_issuepur_code_id(String tr_issuepur_code_id) {
		this.tr_issuepur_code_id = tr_issuepur_code_id;
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
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}
	public String getRtn() {
		return rtn;
	}
	public void setRtn(String rtn) {
		this.rtn = rtn;
	}
	public String getPrj_no() {
		return prj_no;
	}
	public void setPrj_no(String prj_no) {
		this.prj_no = prj_no;
	}
	public String getDocid_revid_revno() {
		return docid_revid_revno;
	}
	public void setDocid_revid_revno(String docid_revid_revno) {
		this.docid_revid_revno = docid_revid_revno;
	}
	public String getTr_period() {
		return tr_period;
	}
	public void setTr_period(String tr_period) {
		this.tr_period = tr_period;
	}
	public String getTr_from_search() {
		return tr_from_search;
	}
	public void setTr_from_search(String tr_from_search) {
		this.tr_from_search = tr_from_search;
	}
	public String getTr_to_search() {
		return tr_to_search;
	}
	public void setTr_to_search(String tr_to_search) {
		this.tr_to_search = tr_to_search;
	}
	public String getIssue_pur() {
		return issue_pur;
	}
	public void setIssue_pur(String issue_pur) {
		this.issue_pur = issue_pur;
	}
	public String getDiscip() {
		return discip;
	}
	public void setDiscip(String discip) {
		this.discip = discip;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getMod_nm() {
		return mod_nm;
	}
	public void setMod_nm(String mod_nm) {
		this.mod_nm = mod_nm;
	}
	public String getCorr_from() {
		return corr_from;
	}
	public void setCorr_from(String corr_from) {
		this.corr_from = corr_from;
	}
	public String getCorr_to() {
		return corr_to;
	}
	public void setCorr_to(String corr_to) {
		this.corr_to = corr_to;
	}
	public String getCorr_type() {
		return corr_type;
	}
	public void setCorr_type(String corr_type) {
		this.corr_type = corr_type;
	}
	public int getDigit() {
		return digit;
	}
	public void setDigit(int digit) {
		this.digit = digit;
	}
	public String getNewOrUpdate() {
		return newOrUpdate;
	}
	public void setNewOrUpdate(String newOrUpdate) {
		this.newOrUpdate = newOrUpdate;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getIssuepurpose() {
		return issuepurpose;
	}
	public void setIssuepurpose(String issuepurpose) {
		this.issuepurpose = issuepurpose;
	}
	public String getIssuepurpose_desc() {
		return issuepurpose_desc;
	}
	public void setIssuepurpose_desc(String issuepurpose_desc) {
		this.issuepurpose_desc = issuepurpose_desc;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getReturn_status() {
		return return_status;
	}
	public void setReturn_status(String return_status) {
		this.return_status = return_status;
	}
	public String getSet_code_id() {
		return set_code_id;
	}
	public void setSet_code_id(String set_code_id) {
		this.set_code_id = set_code_id;
	}
	public String getSet_code_type() {
		return set_code_type;
	}
	public void setSet_code_type(String set_code_type) {
		this.set_code_type = set_code_type;
	}
	public String getConcatStr() {
		return concatStr;
	}
	public void setConcatStr(String concatStr) {
		this.concatStr = concatStr;
	}
	public String getRtnUpdate() {
		return rtnUpdate;
	}
	public void setRtnUpdate(String rtnUpdate) {
		this.rtnUpdate = rtnUpdate;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getRfile_nm() {
		return rfile_nm;
	}
	public void setRfile_nm(String rfile_nm) {
		this.rfile_nm = rfile_nm;
	}
}
