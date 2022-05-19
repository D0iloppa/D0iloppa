/**
 * 
 */
package kr.co.doiloppa.model;

import java.util.HashMap;
import java.util.List;

/**
 * @author doil
 *
 */
public class PrjDisciplineVO {
    String prj_id;
    String copy_prj_id;
    String discip_code_id;
    String discip_code;
    String discip_display;
    String discip_desc;
    long discip_order;
    String reg_id;
    String reg_date;
    String mod_id;
    String mod_date;
    
    String discip_folder_id;
    String folder_name;
    List<String> discip_folder_id_List;
    List<String> folder_name_List;
    List<Long> discip_folder_seq_List;
    List<List<String>> folders;
    HashMap<String,Long> folder_seqId_Map; 
   
    long folder_id;
    long discip_folder_seq;
    int getMax;
    
 // json 리스트
 	String[] jsonRowDatas;
    String jsonRowdata;
    
    
    
    
    
    
    
	public String[] getJsonRowDatas() {
		return jsonRowDatas;
	}


	public void setJsonRowDatas(String[] jsonRowDatas) {
		this.jsonRowDatas = jsonRowDatas;
	}


	public String getCopy_prj_id() {
		return copy_prj_id;
	}


	public void setCopy_prj_id(String copy_prj_id) {
		this.copy_prj_id = copy_prj_id;
	}


	public List<List<String>> getFolders() {
		return folders;
	}


	public void setFolders(List<List<String>> folders) {
		this.folders = folders;
	}


	public long getFolder_id() {
		return folder_id;
	}


	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}


	public HashMap<String, Long> getFolder_seqId_Map() {
		return folder_seqId_Map;
	}


	public void setFolder_seqId_Map(HashMap<String, Long> folder_seqId_Map) {
		this.folder_seqId_Map = folder_seqId_Map;
	}


	public int getGetMax() {
		return getMax;
	}

	
	public List<Long> getDiscip_folder_seq_List() {
		return discip_folder_seq_List;
	}


	public void setDiscip_folder_seq_List(List<Long> discip_folder_seq_List) {
		this.discip_folder_seq_List = discip_folder_seq_List;
	}


	public void setGetMax(int getMax) {
		this.getMax = getMax;
	}

	public String getFolder_name() {
		return folder_name;
	}

	public void setFolder_name(String folder_name) {
		this.folder_name = folder_name;
	}

	public List<String> getFolder_name_List() {
		return folder_name_List;
	}

	public void setFolder_name_List(List<String> folder_name_List) {
		this.folder_name_List = folder_name_List;
	}

	public List<String> getDiscip_folder_id_List() {
		return discip_folder_id_List;
	}

	public void setDiscip_folder_id_List(List<String> discip_folder_id_List) {
		this.discip_folder_id_List = discip_folder_id_List;
	}

	public long getDiscip_folder_seq() {
		return discip_folder_seq;
	}

	public void setDiscip_folder_seq(long discip_folder_seq) {
		this.discip_folder_seq = discip_folder_seq;
	}

	public String getDiscip_folder_id() {
		return discip_folder_id;
	}

	public void setDiscip_folder_id(String discip_folder_id) {
		this.discip_folder_id = discip_folder_id;
	}

	public String getPrj_id() {
		return prj_id;
	}

	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
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

	public String getDiscip_desc() {
		return discip_desc;
	}

	public void setDiscip_desc(String discip_desc) {
		this.discip_desc = discip_desc;
	}

	public long getDiscip_order() {
		return discip_order;
	}

	public void setDiscip_order(long discip_order) {
		this.discip_order = discip_order;
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

	public String getJsonRowdata() {
		return jsonRowdata;
	}

	public void setJsonRowdata(String jsonRowdata) {
		this.jsonRowdata = jsonRowdata;
	}

    
    
    

}
