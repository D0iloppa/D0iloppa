package kr.co.hhi.model;

public class PrjSystemPrefixVO {
	String prj_id;
	String system_prefix_type;
	String system_prefix_id;
	String prefix;
	String prefix_desc;
	
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String[] jsonRowDatas;
	
	public String[] getJsonRowDatas() {
		return jsonRowDatas;
	}
	public void setJsonRowDatas(String[] jsonRowDatas) {
		this.jsonRowDatas = jsonRowDatas;
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
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getSystem_prefix_type() {
		return system_prefix_type;
	}
	public void setSystem_prefix_type(String system_prefix_type) {
		this.system_prefix_type = system_prefix_type;
	}
	public String getSystem_prefix_id() {
		return system_prefix_id;
	}
	public void setSystem_prefix_id(String system_prefix_id) {
		this.system_prefix_id = system_prefix_id;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getPrefix_desc() {
		return prefix_desc;
	}
	public void setPrefix_desc(String prefix_desc) {
		this.prefix_desc = prefix_desc;
	}
}
