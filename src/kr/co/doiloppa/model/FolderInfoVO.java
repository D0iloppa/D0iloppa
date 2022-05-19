/**
 * 
 */
package kr.co.doiloppa.model;

/**
 * @author doil
 *
 */
public class FolderInfoVO {
	String prj_id;
	long folder_id;
	long parent_folder_id;
	String folder_path;
	String folder_path_str;
	String folder_nm;
	String r_folder_path_nm;
	String origin_folder_path;
	
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	String discip_code_id;
	String discip_code;
	String discip_display;
	String discip_desc;
	
	String folder_type;
	String tra_folder_nm;
	
	String new_folder_nm;
	
	// 최종적으로 정해진 권한
	String auth_r,auth_w,auth_d;
	
	String user_id;
	// 유저권한
	String user_r,user_w,user_d,user_p;
	
	String org_id;
	// 조직권한
	String org_r,org_w,org_d,org_p;
	
	String grp_id;
	// 그룹권한
	String grp_r,grp_w,grp_d,grp_p;
	
	// DRN 사용여부
	String useDRN;
	
	String process_type;
	
	String inOut;
	
	
	
	public String getOrigin_folder_path() {
		return origin_folder_path;
	}
	public void setOrigin_folder_path(String origin_folder_path) {
		this.origin_folder_path = origin_folder_path;
	}
	public String getInOut() {
		return inOut;
	}
	public void setInOut(String inOut) {
		this.inOut = inOut;
	}
	public String getProcess_type() {
		return process_type;
	}
	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}
	public String getFolder_path_str() {
		return folder_path_str;
	}
	public void setFolder_path_str(String folder_path_str) {
		this.folder_path_str = folder_path_str;
	}
	public String getUseDRN() {
		return useDRN;
	}
	public void setUseDRN(String useDRN) {
		this.useDRN = useDRN;
	}
	public String getUser_p() {
		return user_p;
	}
	public void setUser_p(String user_p) {
		this.user_p = user_p;
	}
	public String getOrg_p() {
		return org_p;
	}
	public void setOrg_p(String org_p) {
		this.org_p = org_p;
	}
	public String getGrp_p() {
		return grp_p;
	}
	public void setGrp_p(String grp_p) {
		this.grp_p = grp_p;
	}
	public String getAuth_r() {
		return auth_r;
	}
	public void setAuth_r(String auth_r) {
		this.auth_r = auth_r;
	}
	public String getAuth_w() {
		return auth_w;
	}
	public void setAuth_w(String auth_w) {
		this.auth_w = auth_w;
	}
	public String getAuth_d() {
		return auth_d;
	}
	public void setAuth_d(String auth_d) {
		this.auth_d = auth_d;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_r() {
		return user_r;
	}
	public void setUser_r(String user_r) {
		this.user_r = user_r;
	}
	public String getUser_w() {
		return user_w;
	}
	public void setUser_w(String user_w) {
		this.user_w = user_w;
	}
	public String getUser_d() {
		return user_d;
	}
	public void setUser_d(String user_d) {
		this.user_d = user_d;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getOrg_r() {
		return org_r;
	}
	public void setOrg_r(String org_r) {
		this.org_r = org_r;
	}
	public String getOrg_w() {
		return org_w;
	}
	public void setOrg_w(String org_w) {
		this.org_w = org_w;
	}
	public String getOrg_d() {
		return org_d;
	}
	public void setOrg_d(String org_d) {
		this.org_d = org_d;
	}
	public String getGrp_id() {
		return grp_id;
	}
	public void setGrp_id(String grp_id) {
		this.grp_id = grp_id;
	}
	public String getGrp_r() {
		return grp_r;
	}
	public void setGrp_r(String grp_r) {
		this.grp_r = grp_r;
	}
	public String getGrp_w() {
		return grp_w;
	}
	public void setGrp_w(String grp_w) {
		this.grp_w = grp_w;
	}
	public String getGrp_d() {
		return grp_d;
	}
	public void setGrp_d(String grp_d) {
		this.grp_d = grp_d;
	}
	public String getFolder_type() {
		return folder_type;
	}
	public void setFolder_type(String folder_type) {
		this.folder_type = folder_type;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public long getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(long folder_id) {
		this.folder_id = folder_id;
	}
	public long getParent_folder_id() {
		return parent_folder_id;
	}
	public void setParent_folder_id(long parent_folder_id) {
		this.parent_folder_id = parent_folder_id;
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
	public String getR_folder_path_nm() {
		return r_folder_path_nm;
	}
	public void setR_folder_path_nm(String r_folder_path_nm) {
		this.r_folder_path_nm = r_folder_path_nm;
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
	public String getNew_folder_nm() {
		return new_folder_nm;
	}
	public void setNew_folder_nm(String new_folder_nm) {
		this.new_folder_nm = new_folder_nm;
	}
	public String getTra_folder_nm() {
		return tra_folder_nm;
	}
	public void setTra_folder_nm(String tra_folder_nm) {
		this.tra_folder_nm = tra_folder_nm;
	}
}
