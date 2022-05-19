/**
 * 
 */
package kr.co.doiloppa.model;

/**
 * @author doil
 *
 */
public class ProjectBbsVO {
	
	String prj_id;
	long bbs_seq;
	long parent_no;
	long order_no;
	long depth_no;
	String title;
	String contents;
	long hit_cnt;
	String remote_ip;
	String reg_id;
	String reg_date;
	String mod_id;
	String mod_date;
	
	
	
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public long getBbs_seq() {
		return bbs_seq;
	}
	public void setBbs_seq(long bbs_seq) {
		this.bbs_seq = bbs_seq;
	}
	public long getParent_no() {
		return parent_no;
	}
	public void setParent_no(long parent_no) {
		this.parent_no = parent_no;
	}
	public long getOrder_no() {
		return order_no;
	}
	public void setOrder_no(long order_no) {
		this.order_no = order_no;
	}
	public long getDepth_no() {
		return depth_no;
	}
	public void setDepth_no(long depth_no) {
		this.depth_no = depth_no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public long getHit_cnt() {
		return hit_cnt;
	}
	public void setHit_cnt(long hit_cnt) {
		this.hit_cnt = hit_cnt;
	}
	public String getRemote_ip() {
		return remote_ip;
	}
	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
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
	
	

}
