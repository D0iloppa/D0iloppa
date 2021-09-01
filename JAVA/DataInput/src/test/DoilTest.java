package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import bean.Beans;

public class DoilTest {
	
	static int cnt = 0;
	
	
	// Bean������ ���� �����ͼ�
	
	
	public static class DataSet {


		// �Ʒ� ����Ʈ���� private�� �����ϰ� ���Ϳ� ���ͷ� �޾��ִ°��� �̻����̳�
		// ������ ���� �����ؼ� �־��ִ� �뵵�� ������ Ŭ�����̱⶧���� public���� ������ ����� ����
		List<AccessPageSet> apList;
		List<UserSet> usrList;
		List<String> domSeq;			// ������ ������ (����� 1,2�� ��� | �ٸ� �ʵ�� ¦�� �̷��� �ʴ� �����ʵ�)
		
		List<String> piList;			// �������� ����
		List<String> pcList;			// �������� ����

		List<String> reasonList;		// �ٿ�ε� ����
		List<String> reasonCodeList;	// ����(�ڵ�)

		int page_Seq, priv_Seq;
		

		public DataSet(String startDate) {
			super();
			
			
			apList = new ArrayList<AccessPageSet>();
			usrList = new ArrayList<UserSet>();
			domSeq = new ArrayList<String>(); // ������ ������ (����� 1,2�� ��� | �ٸ� �ʵ�� ¦�� �̷��� �ʴ� �����ʵ�)

			piList = new ArrayList<String>(); // �������� ����
			pcList = new ArrayList<String>(); // �������� ����

			reasonList = new ArrayList<String>(); // �ٿ�ε� ����
			reasonCodeList = new ArrayList<String>(); // ����(�ڵ�)
			
			this.setFieldDomain();
			
			
			
		}

		  class AccessPageSet {
			String access_Page_Url, access_Page_Name, access_Page_Type;

			public AccessPageSet(String access_Page_Url, String access_Page_Name, String access_Page_Type) {
				super();
				this.access_Page_Url = access_Page_Url;
				this.access_Page_Name = access_Page_Name;
				this.access_Page_Type = access_Page_Type;
			}

		}

		  class UserSet {
			String access_Ip, user_Id, user_Name, dept_Id, dept_Name;

			public UserSet(String access_Ip, String user_Id, String user_Name, String dept_Id, String dept_Name) {
				super();
				this.access_Ip = access_Ip;
				this.user_Id = user_Id;
				this.user_Name = user_Name;
				this.dept_Id = dept_Id;
				this.dept_Name = dept_Name;
			}

		}

		 class PrivSet {
			String priv_Info, priv_Text;

			public PrivSet(String priv_Info, String priv_Text) {
				super();
				this.priv_Info = priv_Info;
				this.priv_Text = priv_Text;
			}

		}
		
		 void setFieldDomain() { // �� �ʵ庰 �� �׸���� ��� ������ �ʱ�ȭ

			// ���������� ���������� ����
			this.apList.add(new AccessPageSet("/test1.do", "�׽�Ʈ��ȸ", "RD"));
			this.apList.add(new AccessPageSet("/test2.do", "�׽�Ʈ����", "CR"));
			this.apList.add(new AccessPageSet("/test3.do", "�׽�Ʈ�ٿ�ε�", "DW"));
			this.apList.add(new AccessPageSet("/test4.do", "�׽�Ʈ���ε�", "UP"));

			// ���������� ������ ����
			this.usrList.add(new UserSet("127.0.0.1", "so111", "������", "1", "������"));
			this.usrList.add(new UserSet("127.0.0.2", "park123", "������", "1", "������"));
			this.usrList.add(new UserSet("127.0.0.3", "kim123", "ȫ�浿", "2", "��ȹ��"));
			this.usrList.add(new UserSet("127.0.0.4", "leejh", "������", "2", "��ȹ��"));
			this.usrList.add(new UserSet("127.0.0.5", "sososo", "�迵��", "2", "��ȹ��"));

			this.domSeq.add("1");
			this.domSeq.add("2");
			this.domSeq.add("1");
			this.domSeq.add("1");
			this.domSeq.add("1");
			this.domSeq.add("1");
			this.domSeq.add("1");

			///////////////////////////////
			this.piList.add("1");
			this.piList.add("2");
			this.piList.add("3");
			this.piList.add("4");
			this.piList.add("5");
			this.piList.add("6");
			this.piList.add("7");
			this.piList.add("8");
			this.piList.add("9");

			this.pcList.add("910111-2131415");
			this.pcList.add("MP9837477");
			this.pcList.add("02-837483-91");
			this.pcList.add("02-6942-9900");
			this.pcList.add("011 2345 6789");
			this.pcList.add("4979-5371-4873-3542");
			this.pcList.add("test@wellconn.co.kr");
			this.pcList.add("1-5648523219");
			this.pcList.add("434-22-00661-4");
			///////////////////////////////////

			this.reasonList.add("");
			this.reasonList.add("Ÿ ��� �����");
			this.reasonList.add("���� ����");

			this.reasonCodeList.add("2");
			this.reasonCodeList.add("1");
			this.reasonCodeList.add("1");

		}

	}
	static DataSet ds = new DataSet("20210830090000"); // startDate�� �����ͼ� ����
	

	// Bean����Ʈ
	static List<Beans.PageBean> pbList = new ArrayList<>();
	static List<Beans.PrivBean> pvList = new ArrayList<>();

	static Calendar cal;
	static int year, month, date, hour, minute, second;
	static String dateT, dateTT, dateTTT;

	// �α׸� ��� ���� �ð���¥
	static String startDate;
	
	static long endDate;

	
	
	
	
	public static void main(String[] args) {
		ds.page_Seq = getLastPageSeq(); // ������ ������ ������ ��ȣ +1 ���ֱ� ����
		ds.priv_Seq = getLastPrivSeq(); // ������ ������ ������ ��ȣ +1 ���ֱ� ����

		startDate = "20210830090000";
		
		Date date_Now = new Date(System.currentTimeMillis()); // ����ð�
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
		endDate = Long.parseLong(fm.format(date_Now).toString());
		
		System.out.println(endDate);

		cal = Calendar.getInstance(); // Ķ���� ����
		long now = Long.parseLong(startDate);
		
		for (int i = 0; i < 1; i++) {
			
			createBean();
			now = Long.parseLong(String.format("%02d%02d%02d%02d%02d%02d", year, month, date, hour, minute, second));
			System.out.println(now);

		}
		
		while(now <= endDate) {
			createBean();
			now = Long.parseLong(String.format("%02d%02d%02d%02d%02d%02d", year, month, date, hour, minute, second));
			cnt++;
		}
		
		System.out.println(cnt);

	}
	
	private static void createBean() {
		// Bean ����
		Random r = new Random();

		int rand_AccessIdx = r.nextInt(ds.apList.size());
		int rand_DomIdx = r.nextInt(ds.domSeq.size());
		int rand_UserIdx = r.nextInt(ds.usrList.size());
		int rand_PrivIdx = r.nextInt(ds.piList.size());
		int rand_DRIdx = r.nextInt(ds.reasonList.size());

		// �ð�����

		int randT = r.nextInt(59) + 1; // 1~4xm

		cal.add(Calendar.SECOND, randT);
//		cal.set(second, cal.get(Calendar.SECOND)+randT);

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		date = cal.get(Calendar.DATE);
		hour = cal.get(Calendar.HOUR);
		minute = cal.get(Calendar.MINUTE);
		second = cal.get(Calendar.SECOND);
		
		addTime();
//
//		cal.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0, 4)));
//		cal.set(Calendar.MONTH, Integer.parseInt(startDate.substring(4, 6)) - 1);
//		cal.set(Calendar.DATE, Integer.parseInt(startDate.substring(6, 8)));
//
//		cal.set(Calendar.HOUR, Integer.parseInt(startDate.substring(8, 10)));
//		cal.set(Calendar.MINUTE, Integer.parseInt(startDate.substring(10, 12)));
//		cal.set(Calendar.SECOND, cal.get(Calendar.SECOND)+Integer.parseInt(startDate.substring(12, 14)));
		
		

		dateT = String.format("%02d-%02d-%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
		dateTT = String.format("%02d%02d%02d", year, month, date);
		dateTTT = String.format("%02d", hour);

		///////////////////////////

		pbList.add(randPageBean(rand_AccessIdx, rand_DomIdx, rand_UserIdx, rand_PrivIdx, rand_DRIdx));
		pvList.add(randPrivBean(rand_PrivIdx, rand_DomIdx));
	}

	private static void addTime() {
		
		Random r = new Random();
		int randSec = r.nextInt(59) + 1; 
		
		second+=randSec;
		if(second>=60) {
			second = second % 60;
			minute++;
		}
		if(minute>=60) {
			minute = 0;
			hour++;
		}
		if(hour>=24) {
			hour = 0;
			date++;
		}
		
	}

	static int getLastPageSeq() {
		int lastIdx;

		lastIdx = 42; // ������ ������ ���ϴ� ���ǹ� ������ �����;���

		return lastIdx;

	}

	static int getLastPrivSeq() {
		int lastIdx;

		lastIdx = 100; // ������ ������ ���ϴ� ���ǹ� ������ �����;���

		return lastIdx;

	}

	
	
	// PageBean ����
	static Beans.PageBean randPageBean(int rand_AccessIdx, int rand_DomIdx, int rand_UserIdx, int rand_PrivIdx,
			int rand_DRIdx) {

		Beans.PageBean pbBean = new Beans().new PageBean();

		pbBean.setAccess_Page_Seq(++ds.page_Seq);
		pbBean.setAccess_Page_Url(ds.apList.get(rand_AccessIdx).access_Page_Url);
		pbBean.setAccess_Page_Name(ds.apList.get(rand_AccessIdx).access_Page_Name);
		pbBean.setAccess_Page_Type(ds.apList.get(rand_AccessIdx).access_Page_Type);

		pbBean.setDomain_Seq(ds.domSeq.get(rand_DomIdx));
		pbBean.setAccess_Time(dateT);

		pbBean.setAccess_Ip(ds.usrList.get(rand_UserIdx).access_Ip);

		pbBean.setUser_Id(ds.usrList.get(rand_UserIdx).user_Id);
		pbBean.setUser_Name(ds.usrList.get(rand_UserIdx).user_Name);
		pbBean.setDept_Id(ds.usrList.get(rand_UserIdx).dept_Id);
		pbBean.setDept_Name(ds.usrList.get(rand_UserIdx).dept_Name);

		pbBean.setPriv_info(ds.piList.get(rand_PrivIdx) + ":1");

		pbBean.setDownload_Reason(ds.reasonList.get(rand_DRIdx));
		pbBean.setInt_Access_Time(dateTT);
		pbBean.setString_Access_Time_Hour(dateTTT);
		pbBean.setAttachments(ds.reasonCodeList.get(rand_DRIdx));

		String query = "INSERT INTO logker_access_page_tbl VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // ���ε庯��

		new Beans().showBean(pbBean);

		return pbBean;

	}
	// PrivBean ����
	static Beans.PrivBean randPrivBean(int rand_PrivIdx, int rand_DomIdx) {
		Beans.PrivBean pvBean = new Beans().new PrivBean();

		pvBean.setAccess_Priv_Seq(ds.priv_Seq);
		pvBean.setAccess_Page_Seq(ds.page_Seq);
		pvBean.setPriv_Seq(ds.piList.get(rand_PrivIdx));
		pvBean.setPriv_Text(ds.pcList.get(rand_PrivIdx));
		pvBean.setInt_Access_Time(dateTT);
		pvBean.setDomain_Seq(ds.domSeq.get(rand_DomIdx));
		pvBean.setString_Access_Time_Hour(dateTTT);
		pvBean.setAccess_Time(dateT);
		
		new Beans().showBean(pvBean);


		return pvBean;

	}


}
