package kr.co.hhi.model;

import java.util.HashMap;

public class PrjStepVO {
	String prj_id;
	String doc_id;
	String rev_id;
	String step_code_id;
	String plan_date;
	String actual_date;
	String fore_date;
	String tr_id;
	String rtn_status;
	long step_seq;
	long folder_id;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String set_val2;
	String set_val3;
	String set_val4;
	
	String step;
	String set_code_type;
	String tr_no;
	String[] tableArrayData;
	String stepCodeIdArray;
	String planDateArray;
	String ActualDateArray;
	String ForeDateArray;
	String origin_rev_id;
	String set_code_id;
	
	String type;
	
	HashMap<String, HashMap<String,String> > steps; 
	
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
	public String getStep_code_id() {
		return step_code_id;
	}
	public void setStep_code_id(String step_code_id) {
		this.step_code_id = step_code_id;
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
	public String getTr_id() {
		return tr_id;
	}
	public void setTr_id(String tr_id) {
		this.tr_id = tr_id;
	}
	public String getRtn_status() {
		return rtn_status;
	}
	public void setRtn_status(String rtn_status) {
		this.rtn_status = rtn_status;
	}
	public long getStep_seq() {
		return step_seq;
	}
	public void setStep_seq(long step_seq) {
		this.step_seq = step_seq;
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
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getSet_code_type() {
		return set_code_type;
	}
	public void setSet_code_type(String set_code_type) {
		this.set_code_type = set_code_type;
	}
	public HashMap<String, HashMap<String, String>> getSteps() {
		return steps;
	}
	public void setSteps(HashMap<String, HashMap<String, String>> steps) {
		this.steps = steps;
	}
	public String getTr_no() {
		return tr_no;
	}
	public void setTr_no(String tr_no) {
		this.tr_no = tr_no;
	}
	public String[] getTableArrayData() {
		return tableArrayData;
	}
	public void setTableArrayData(String[] tableArrayData) {
		this.tableArrayData = tableArrayData;
	}
	public String getStepCodeIdArray() {
		return stepCodeIdArray;
	}
	public void setStepCodeIdArray(String stepCodeIdArray) {
		this.stepCodeIdArray = stepCodeIdArray;
	}
	public String getPlanDateArray() {
		return planDateArray;
	}
	public void setPlanDateArray(String planDateArray) {
		this.planDateArray = planDateArray;
	}
	public String getActualDateArray() {
		return ActualDateArray;
	}
	public void setActualDateArray(String actualDateArray) {
		ActualDateArray = actualDateArray;
	}
	public String getForeDateArray() {
		return ForeDateArray;
	}
	public void setForeDateArray(String foreDateArray) {
		ForeDateArray = foreDateArray;
	}
	public String getOrigin_rev_id() {
		return origin_rev_id;
	}
	public void setOrigin_rev_id(String origin_rev_id) {
		this.origin_rev_id = origin_rev_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSet_code_id() {
		return set_code_id;
	}
	public void setSet_code_id(String set_code_id) {
		this.set_code_id = set_code_id;
	}
}
