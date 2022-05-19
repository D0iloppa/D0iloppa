/**
 * 
 */
package kr.co.doiloppa.model;

public class PrjCodeSettingsVO {
	
	int count;
	String serial;
	
	
	String prj_id;
	long folder_id;
	
	String step_table;
	String set_code_type;
	String set_code_id;
	String set_code_feature;
	String set_code;
	String set_desc;
	String set_val;
	
	long set_order;
	String set_val2;
	
	String set_val3;
	
	String set_val4;
	
	String ifc_yn;
	String number_type;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String origin_code;
	String origin_order;
	String prevnext_code;
	String copy_prj_id;
	
	String[] jsonRowdatas;
	String[] deleteRows;
	
	// set_val에 id가 매핑되는 경우, 해당 id와 매핑되는 데이터의 실제 값들을 담아두는 배열 
	String[] set_valStr;
	
	String fileName;
	String prj_nm;
	
	String from_code;
	String from_code_id;
	String to_code;
	String to_code_id;
	String type_code;
	String type_code_id;
	
	String corr_from;
	String corr_to;
	String corr_type;
	String digit;
	
	
	// DRN step date 업데이트 용
	String doc_id,rev_id;
	String plan_date,actual_date,fore_date;
	String tbl;
	
	boolean overwriteMode;

	
	
	
	public long getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}

	public String getStep_table() {
		return step_table;
	}

	public void setStep_table(String step_table) {
		this.step_table = step_table;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getNumber_type() {
		return number_type;
	}

	public void setNumber_type(String number_type) {
		this.number_type = number_type;
	}

	public String getTbl() {
		return tbl;
	}

	public void setTbl(String tbl) {
		this.tbl = tbl;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPrj_id() {
		return prj_id;
	}

	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}

	public String getSet_code_type() {
		return set_code_type;
	}

	public void setSet_code_type(String set_code_type) {
		this.set_code_type = set_code_type;
	}

	public String getSet_code_id() {
		return set_code_id;
	}

	public void setSet_code_id(String set_code_id) {
		this.set_code_id = set_code_id;
	}

	public String getSet_code_feature() {
		return set_code_feature;
	}

	public void setSet_code_feature(String set_code_feature) {
		this.set_code_feature = set_code_feature;
	}

	public String getSet_code() {
		return set_code;
	}

	public void setSet_code(String set_code) {
		this.set_code = set_code;
	}

	public String getSet_desc() {
		return set_desc;
	}

	public void setSet_desc(String set_desc) {
		this.set_desc = set_desc;
	}

	public String getSet_val() {
		return set_val;
	}

	public void setSet_val(String set_val) {
		this.set_val = set_val;
	}

	public long getSet_order() {
		return set_order;
	}

	public void setSet_order(long set_order) {
		this.set_order = set_order;
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

	public String getIfc_yn() {
		return ifc_yn;
	}

	public void setIfc_yn(String ifc_yn) {
		this.ifc_yn = ifc_yn;
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

	public String getOrigin_code() {
		return origin_code;
	}

	public void setOrigin_code(String origin_code) {
		this.origin_code = origin_code;
	}

	public String getOrigin_order() {
		return origin_order;
	}

	public void setOrigin_order(String origin_order) {
		this.origin_order = origin_order;
	}

	public String getPrevnext_code() {
		return prevnext_code;
	}

	public void setPrevnext_code(String prevnext_code) {
		this.prevnext_code = prevnext_code;
	}

	public String getCopy_prj_id() {
		return copy_prj_id;
	}

	public void setCopy_prj_id(String copy_prj_id) {
		this.copy_prj_id = copy_prj_id;
	}

	public String[] getJsonRowdatas() {
		return jsonRowdatas;
	}

	public void setJsonRowdatas(String[] jsonRowdatas) {
		this.jsonRowdatas = jsonRowdatas;
	}

	public String[] getDeleteRows() {
		return deleteRows;
	}

	public void setDeleteRows(String[] deleteRows) {
		this.deleteRows = deleteRows;
	}

	public String[] getSet_valStr() {
		return set_valStr;
	}

	public void setSet_valStr(String[] set_valStr) {
		this.set_valStr = set_valStr;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPrj_nm() {
		return prj_nm;
	}

	public void setPrj_nm(String prj_nm) {
		this.prj_nm = prj_nm;
	}

	public String getFrom_code() {
		return from_code;
	}

	public void setFrom_code(String from_code) {
		this.from_code = from_code;
	}

	public String getFrom_code_id() {
		return from_code_id;
	}

	public void setFrom_code_id(String from_code_id) {
		this.from_code_id = from_code_id;
	}

	public String getTo_code() {
		return to_code;
	}

	public void setTo_code(String to_code) {
		this.to_code = to_code;
	}

	public String getTo_code_id() {
		return to_code_id;
	}

	public void setTo_code_id(String to_code_id) {
		this.to_code_id = to_code_id;
	}

	public String getType_code() {
		return type_code;
	}

	public void setType_code(String type_code) {
		this.type_code = type_code;
	}

	public String getType_code_id() {
		return type_code_id;
	}

	public void setType_code_id(String type_code_id) {
		this.type_code_id = type_code_id;
	}

	public boolean isOverwriteMode() {
		return overwriteMode;
	}

	public void setOverwriteMode(boolean overwriteMode) {
		this.overwriteMode = overwriteMode;
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

	public String getDigit() {
		return digit;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}
	
	
	
	
	
}
