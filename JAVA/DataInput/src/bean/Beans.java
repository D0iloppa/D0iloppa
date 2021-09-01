package bean;

import bean.Beans.PrivBean;

public class Beans {
	
	
	// PageBean
	public class PageBean {

		int access_Page_Seq;
		
		String access_Page_Url,access_Page_Name,access_Page_Type;
		String domain_Seq;
		String access_Time;
		String access_Ip,user_Id,user_Name,dept_Id,dept_Name;
		String priv_info, download_Reason;
		String int_Access_Time,string_Access_Time_Hour;
		String attachments;
		
		
		public int getAccess_Page_Seq() {
			return access_Page_Seq;
		}
		public void setAccess_Page_Seq(int access_Page_Seq) {
			this.access_Page_Seq = access_Page_Seq;
		}
		public String getAccess_Page_Url() {
			return access_Page_Url;
		}
		public void setAccess_Page_Url(String access_Page_Url) {
			this.access_Page_Url = access_Page_Url;
		}
		public String getAccess_Page_Name() {
			return access_Page_Name;
		}
		public void setAccess_Page_Name(String access_Page_Name) {
			this.access_Page_Name = access_Page_Name;
		}
		public String getAccess_Page_Type() {
			return access_Page_Type;
		}
		public void setAccess_Page_Type(String access_Page_Type) {
			this.access_Page_Type = access_Page_Type;
		}
		public String getDomain_Seq() {
			return domain_Seq;
		}
		public void setDomain_Seq(String domain_Seq) {
			this.domain_Seq = domain_Seq;
		}
		public String getAccess_Time() {
			return access_Time;
		}
		public void setAccess_Time(String access_Time) {
			this.access_Time = access_Time;
		}
		public String getAccess_Ip() {
			return access_Ip;
		}
		public void setAccess_Ip(String access_Ip) {
			this.access_Ip = access_Ip;
		}
		public String getUser_Id() {
			return user_Id;
		}
		public void setUser_Id(String user_Id) {
			this.user_Id = user_Id;
		}
		public String getUser_Name() {
			return user_Name;
		}
		public void setUser_Name(String user_Name) {
			this.user_Name = user_Name;
		}
		public String getDept_Id() {
			return dept_Id;
		}
		public void setDept_Id(String dept_Id) {
			this.dept_Id = dept_Id;
		}
		public String getDept_Name() {
			return dept_Name;
		}
		public void setDept_Name(String dept_Name) {
			this.dept_Name = dept_Name;
		}
		public String getPriv_info() {
			return priv_info;
		}
		public void setPriv_info(String priv_info) {
			this.priv_info = priv_info;
		}
		public String getDownload_Reason() {
			return download_Reason;
		}
		public void setDownload_Reason(String download_Reason) {
			this.download_Reason = download_Reason;
		}
		public String getInt_Access_Time() {
			return int_Access_Time;
		}
		public void setInt_Access_Time(String int_Access_Time) {
			this.int_Access_Time = int_Access_Time;
		}
		public String getString_Access_Time_Hour() {
			return string_Access_Time_Hour;
		}
		public void setString_Access_Time_Hour(String string_Access_Time_Hour) {
			this.string_Access_Time_Hour = string_Access_Time_Hour;
		}
		public String getAttachments() {
			return attachments;
		}
		public void setAttachments(String attachments) {
			this.attachments = attachments;
		}
		

		
	}
	public class PrivBean {
		
		int access_Priv_Seq;
		int access_Page_Seq; // PrivBean이 연결되는 페이지 시퀀스
		
		String priv_Seq,priv_Text; // 개인정보 유형(1~9), 실제 내용
		String domain_Seq;
		String int_Access_Time,string_Access_Time_Hour, access_Time;
		
	
		
		public String getInt_Access_Time() {
			return int_Access_Time;
		}
		public void setInt_Access_Time(String int_Access_Time) {
			this.int_Access_Time = int_Access_Time;
		}
		public int getAccess_Priv_Seq() {
			return access_Priv_Seq;
		}
		public void setAccess_Priv_Seq(int access_Priv_Seq) {
			this.access_Priv_Seq = access_Priv_Seq;
		}
		public int getAccess_Page_Seq() {
			return access_Page_Seq;
		}
		public void setAccess_Page_Seq(int access_Page_Seq) {
			this.access_Page_Seq = access_Page_Seq;
		}
		public String getPriv_Seq() {
			return priv_Seq;
		}
		public void setPriv_Seq(String priv_Seq) {
			this.priv_Seq = priv_Seq;
		}
		public String getPriv_Text() {
			return priv_Text;
		}
		public void setPriv_Text(String priv_Text) {
			this.priv_Text = priv_Text;
		}
		public String getDomain_Seq() {
			return domain_Seq;
		}
		public void setDomain_Seq(String domain_Seq) {
			this.domain_Seq = domain_Seq;
		}
		public String getString_Access_Time_Hour() {
			return string_Access_Time_Hour;
		}
		public void setString_Access_Time_Hour(String string_Access_Time_Hour) {
			this.string_Access_Time_Hour = string_Access_Time_Hour;
		}
		public String getAccess_Time() {
			return access_Time;
		}
		public void setAccess_Time(String access_Time) {
			this.access_Time = access_Time;
		}	
		
	}
	
	
	// Bean 조작 메소드
		public void showBean(Object bean) {
			if (bean instanceof Beans.PageBean) {

				Beans.PageBean pbBean = (Beans.PageBean) bean;

				System.out.println("========= [ PageBean show TEST ] =========");

				System.out.println("access_page_seq		: " + pbBean.getAccess_Page_Seq());
				System.out.println("access_page_url		: " + pbBean.getAccess_Page_Url());
				System.out.println("access_page_name	: " + pbBean.getAccess_Page_Name());
				System.out.println("access_page_type	: " + pbBean.getAccess_Page_Type());

				System.out.println("domain_seq		: " + pbBean.getDomain_Seq());
				System.out.println("access_time		: " + pbBean.getAccess_Time());

				System.out.println("access_ip		: " + pbBean.getAccess_Ip());

				System.out.println("user_id			: " + pbBean.getUser_Id());
				System.out.println("user_name		: " + pbBean.getUser_Name());
				System.out.println("dept_id			: " + pbBean.getDept_Id());
				System.out.println("dept_name		: " + pbBean.getDept_Name());

				System.out.println("priv_info		: " + pbBean.getPriv_info());

				System.out.println("download_reason		: " + pbBean.getDownload_Reason());
				System.out.println("int_access_time		: " + pbBean.getInt_Access_Time());
				System.out.println("string_access_time_hour	: " + pbBean.getString_Access_Time_Hour());
				System.out.println("attachments		: " + pbBean.getAttachments());
				return;
				
			} else if (bean instanceof Beans.PrivBean) {
				PrivBean pvBean = (PrivBean) bean;

				System.out.println("========= [ PrivBean show TEST ] =========");

				System.out.println("access_priv_seq		: " + pvBean.getAccess_Priv_Seq());
				System.out.println("access_page_seq		: " + pvBean.getAccess_Page_Seq());
				System.out.println("priv_seq		: " + pvBean.getPriv_Seq());
				System.out.println("priv_text		: " + pvBean.getPriv_Text());
				System.out.println("int_access_time		: " + pvBean.getInt_Access_Time());
				System.out.println("domain_seq		: " + pvBean.getDomain_Seq());
				System.out.println("string_access_time_hour	: " + pvBean.getString_Access_Time_Hour());
				System.out.println("access_time		: " + pvBean.getAccess_Time());

				System.out.println("=====================================\n");
				return;
				
			} else {
				System.out.println("This Data is not a type of Bean We support");
				return;
			}

		}
		
}
