package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import bean.Beans;
import bean.Beans.PageBean;
import bean.Beans.PrivBean;
import util.ProcProperties;

public class DataInputMain {

	static int cntPage = 0, cntPriv = 0;

	// DB ���� ���� ����
	static String JDBC_DRIVER;
	static String DB_URL;
	static String USERNAME;
	static String PASSWORD;
	static Connection conn;

	// Bean������ ���� �����ͼ�
	public static class DataSet {

		// �Ʒ� ����Ʈ���� private�� �����ϰ� ���Ϳ� ���ͷ� �޾��ִ°��� �̻����̳�
		// ������ ���� �����ؼ� �־��ִ� �뵵�� ������ Ŭ�����̱⶧���� public���� ������ ����� ����
		List<AccessPageSet> apList;
		List<UserSet> usrList;
		List<String> domSeq; // ������ ������ (����� 1,2�� ��� | �ٸ� �ʵ�� ¦�� �̷��� �ʴ� �����ʵ�)

		List<String> piList; // �������� ����
		List<String> pcList; // �������� ����

		List<String> reasonList; // �ٿ�ε� ����
		List<String> reasonCodeList; // ����(�ڵ�)

		int page_Seq, priv_Seq;

		public DataSet() {
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

	static DataSet ds = new DataSet(); // startDate�� �����ͼ� ����

	// Bean����Ʈ
	static List<Beans.PageBean> pbList = new ArrayList<>();
	static List<Beans.PrivBean> pvList = new ArrayList<>();

	static Calendar cal;
	static int year, month, date, hour, minute, second;
	static String dateT, dateTT, dateTTT;

	// �α׸� ��� ���� �ð���¥
	static String startDate;
	static long runDate, endDate; // runDate : ���۽ð����� ���� ������ �ð�����, �������� �ð��� Ŀ�� | endDate : runDate�� ���ᰪ

	// �����Լ�
	public static void main(String[] args) {

		try {
			init(); // �ʱ�ȭ
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void init() throws SQLException {
		getProperties(); // ������Ƽ ������ ������
		setCal(); // Bean �ð� ������ ���� Calender ����
		connectDB(); // DB�� ����

	}

	public static void getProperties() {
		ProcProperties.setFileName("po");

		JDBC_DRIVER = ProcProperties.readProperty("JDBC_DRIVER");
		DB_URL = ProcProperties.readProperty("DB_URL");
		USERNAME = ProcProperties.readProperty("USERNAME");
		PASSWORD = ProcProperties.readProperty("PASSWORD");

	}

	public static void setCal() { // �޷¼���

		startDate = "20210901090000";

		Date date_Now = new Date(System.currentTimeMillis()); // ����ð�
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");

		// �ð� ���ڿ��� �������·� ����, ��Һ񱳸� ���� ���ؽð��� �ʰ��ߴ��� ���ߴ��� Ȯ���ϱ����� Long������ ��ȯ
		endDate = Long.parseLong(fm.format(date_Now).toString()); // ���Ḧ ���� ����ð�

		cal = Calendar.getInstance(); // Ķ���� ����

		cal.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(startDate.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(startDate.substring(6, 8)));

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startDate.substring(8, 10)));
		cal.set(Calendar.MINUTE, Integer.parseInt(startDate.substring(10, 12)));
		cal.set(Calendar.SECOND, Integer.parseInt(startDate.substring(12, 14)));

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		date = cal.get(Calendar.DATE);
		hour = cal.get(Calendar.HOUR_OF_DAY); // 24 �ð�
		minute = cal.get(Calendar.MINUTE);
		second = cal.get(Calendar.SECOND);

	}

	private static void connectDB() throws SQLException {
		System.out.println("[DB����]");

		PreparedStatement pstmt = null;

		try {

			Class.forName(JDBC_DRIVER);

			// MYsql�� Ŀ�ؼ� ����
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			
			pstmt = conn.prepareStatement(
					"SELECT access_page_seq FROM logker_access_page_tbl ORDER BY access_page_seq DESC LIMIT 1");
			ResultSet rs1 = pstmt.executeQuery();
			// page_Seq�� ������ �ε��� ������
			ds.page_Seq = (rs1.next()) ? rs1.getInt(1) : 0;
			System.out.println("The index of Last PageSeq : " + ds.page_Seq);

			
			pstmt = conn.prepareStatement(
					"SELECT access_priv_seq FROM logker_access_priv_tbl ORDER BY access_page_seq DESC LIMIT 1");
			ResultSet rs2 = pstmt.executeQuery();
			// priv_Seq�� ������ �ε��� ������
			ds.priv_Seq = (rs2.next()) ? rs2.getInt(1) : 0;
			System.out.println("The index of Last PrivSeq : " + ds.priv_Seq);
			
			// access_priv ���̺��� ������������ �����ϸ� page_Seq�� priv_Seq ��� ������ �� ���������� ���̹Ƿ�
			// ���ǹ��� �ѹ��� ������ �� �� ���� ����

			System.out.println();

			bodyFunc();


			System.out.println();

		} catch (SQLException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
			conn.close();
			System.out.println("[DB���� ����]");
		}

	}

	private static void bodyFunc() throws SQLException {
		Scanner sc = new Scanner(System.in);
		
		
		boolean run = true;
		int menu = -1;

		while (run) {
			System.out.println("=========================================================");
			System.out.println("1. Bean ���� ����	|2. Load From Log	|3.����	|4.����	|");
			System.out.println("=========================================================");
			System.out.print("�޴��Է� (1~4 ���� �Է�) : ");
			menu = sc.nextInt();

			switch (menu) {
				case 1:
					System.out.println("�۾� ���� ���ؽð� : " + startDate);
					System.out.println("�۾� ���� ���ؽð� : " + endDate);

					while (runDate <= endDate) {
						createBeans(); // ������ ������ ���ڵ� ����, Bean������
						runDate = Long
								.parseLong(String.format("%02d%02d%02d%02d%02d%02d", year, month, date, hour, minute, second)); // �ð���
																																// ��
					}
					System.out.println("\nPageBean ���� ���� : " + cntPage + " / PrivBean ���� ���� : " + cntPriv + "\n");
					
					queryJob(); //���� �۾����� ������ Bean����Ʈ���� ��� ���������� ������ �Է�
					break;
					
				case 2:
					System.out.println("2�� �޴� �̱���");
					// ���� �����ؾ���
					break;
					
				case 3:
					setting();
					break;
					
				case 4:
					run = false; // �޴��� ����
					System.out.println("AGENT ����");
					break;
					
				default:
					System.out.println("\n�߸��� �޴� �Է�\n");

			}

		}

	}

	private static void setting() {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("========= SETTING =========");
		System.out.println("���� ������ �۾� ���� ���ؽð��� : " + startDate);
		System.out.println("���� �۾� ���� ���ؽð��� : " + endDate);
		
		
		// �Է°��� ��ȿ���� Ȯ���ϴ� �޼ҵ带 �߰��ϸ� �� �������� ������
		// ����ڰ� �Է����˿� �°� �Է��ҰŶ�� �����ϰ� ������ ����
		System.out.print("startDate:");
		startDate = sc.nextLine();
		System.out.print("endDate:");
		endDate = sc.nextLong();
		
		
		
		
	}

	private static void queryJob() throws SQLException {

		System.out.println("Bean �ҷ��ͼ� ���� �۾� ��...");

		for (Beans.PageBean pb : pbList) {
			dataInputQuery1(pb);
		}

		for (Beans.PrivBean pv : pvList) {
			dataInputQuery2(pv);
		}

		System.out.println("�����۾� �Ϸ�\n");

	}

	private static void dataInputQuery1(Beans.PageBean bean) throws SQLException {

		PreparedStatement pstmt = null;

		String query = "INSERT INTO logker_access_page_tbl VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // ���ε庯��

		pstmt = conn.prepareStatement(query);

		pstmt.setInt(1, bean.getAccess_Page_Seq());
		pstmt.setString(2, bean.getAccess_Page_Url());
		pstmt.setString(3, bean.getAccess_Page_Name());
		pstmt.setString(4, bean.getAccess_Page_Type());
		pstmt.setString(5, bean.getDomain_Seq());
		pstmt.setString(6, bean.getAccess_Time());
		pstmt.setString(7, bean.getAccess_Ip());
		pstmt.setString(8, bean.getUser_Id());
		pstmt.setString(9, bean.getUser_Name());
		pstmt.setString(10, bean.getDept_Id());
		pstmt.setString(11, bean.getDept_Name());
		pstmt.setString(12, bean.getPriv_info());
		pstmt.setString(13, bean.getDownload_Reason());
		pstmt.setString(14, bean.getInt_Access_Time());
		pstmt.setString(15, bean.getString_Access_Time_Hour());
		pstmt.setString(16, bean.getAttachments());

		pstmt.executeUpdate();

	}

	private static void dataInputQuery2(Beans.PrivBean bean) throws SQLException {

		PreparedStatement pstmt = null;

		String query = "insert into logker_access_priv_tbl values (?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(query);

		pstmt.setInt(1, bean.getAccess_Priv_Seq());
		pstmt.setInt(2, bean.getAccess_Page_Seq());
		pstmt.setString(3, bean.getPriv_Seq());
		pstmt.setString(4, bean.getPriv_Text());
		pstmt.setString(5, bean.getInt_Access_Time());
		pstmt.setString(6, bean.getDomain_Seq());
		pstmt.setString(7, bean.getString_Access_Time_Hour());
		pstmt.setString(8, bean.getAccess_Time());

		pstmt.executeUpdate();

	}

	private static void createBeans() {
		// Bean ����
		Random r = new Random();

		int rand_AccessIdx = r.nextInt(ds.apList.size());
		int rand_DomIdx = r.nextInt(ds.domSeq.size());
		int rand_UserIdx = r.nextInt(ds.usrList.size());
		int rand_PrivIdx = r.nextInt(ds.piList.size());
		int rand_DRIdx = r.nextInt(ds.reasonList.size());

		addTime(); // �����ð��� ������

		dateT = String.format("%02d-%02d-%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
		dateTT = String.format("%02d%02d%02d", year, month, date);
		dateTTT = String.format("%02d", hour);

		pbList.add(randPageBean(rand_AccessIdx, rand_DomIdx, rand_UserIdx, rand_PrivIdx, rand_DRIdx));

		int rand_ConcurPriv = r.nextInt(10); // privBean�� PageBean�� 1:N �����̹Ƿ� N���� ������ ������ ���� (�ִ� 10)
		for (int i = 0; i < rand_ConcurPriv; i++) {
			rand_PrivIdx = r.nextInt(ds.piList.size());
			rand_DomIdx = r.nextInt(ds.domSeq.size());
			pvList.add(randPrivBean(rand_PrivIdx, rand_DomIdx));
		}
	}

	// �ð� ���� ó�� �Լ�
	private static void addTime() {

		Random r = new Random();
		int randSec = r.nextInt(59) + 1;

		second += randSec;
		if (second >= 60) {
			second = second % 60;
			minute++;
		}
		if (minute >= 60) {
			minute = 0;
			hour++;
		}
		if (hour >= 24) {
			hour = 0;
			date++;
		}

	}

	// PageBean ��������
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

		/* 1. Bean�� �����Ҷ� ���� ������ ������ ��� */
		//
		/* 2. Bean���� List�� ��Ƴ��� �ѹ��� ó���ϴ� ��� */
		// ���� 2���� ������� ������

//		new Beans().showBean(pbBean); // Bean���� üũ�� �Լ�
		cntPage++; // bean ���� ���� üũ�� ���� ī��Ʈ ����

		return pbBean;

	}

	// PrivBean ��������
	static Beans.PrivBean randPrivBean(int rand_PrivIdx, int rand_DomIdx) {
		Beans.PrivBean pvBean = new Beans().new PrivBean();

		pvBean.setAccess_Priv_Seq(++ds.priv_Seq);
		pvBean.setAccess_Page_Seq(ds.page_Seq);
		pvBean.setPriv_Seq(ds.piList.get(rand_PrivIdx));
		pvBean.setPriv_Text(ds.pcList.get(rand_PrivIdx));
		pvBean.setInt_Access_Time(dateTT);
		pvBean.setDomain_Seq(ds.domSeq.get(rand_DomIdx));
		pvBean.setString_Access_Time_Hour(dateTTT);
		pvBean.setAccess_Time(dateT);

		cntPriv++; // bean ���� ���� üũ�� ���� ī��Ʈ ����

//		new Beans().showBean(pvBean); // Bean���� ����

		return pvBean;

	}
	
	
	
	/*****************************************************/
	// �α����Ϸκ��� ������ �޾��� �� �����ڷ� �����ؼ� PageBean,PrivBean ����
	// ���� �����ؾ���
	static Beans.PageBean inputPageBean(String line) {
		// AccessPage Tabele�� ���� Bean
		Beans.PageBean pbBean = new Beans().new PageBean();

//			pbBean.setAccess_Page_Seq(++ds.page_Seq);
//			pbBean.setAccess_Page_Url();
//			pbBean.setAccess_Page_Name();
//			pbBean.setAccess_Page_Type();

//			pbBean.setDomain_Seq());
//			pbBean.setAccess_Time();

//			pbBean.setAccess_Ip();

//			pbBean.setUser_Id();
//			pbBean.setUser_Name();
//			pbBean.setDept_Id();
//			pbBean.setDept_Name();

//			pbBean.setPriv_info();

//			pbBean.setDownload_Reason();
//			pbBean.setInt_Access_Time();
//			pbBean.setString_Access_Time_Hour();
//			pbBean.setAttachments();

		return pbBean;

	}

	static Beans.PrivBean inputPrivBean(String line) {
		// AccessPriv Table�� ���� Bean
		Beans.PrivBean pvBean = new Beans().new PrivBean();

//			pvBean.setAccess_Priv_Seq(++ds.priv_Seq);
//			pvBean.setAccess_Page_Seq();
//			pvBean.setPriv_Seq();
//			pvBean.setPriv_Text();
//			pvBean.setInt_Access_Time();
//			pvBean.setDomain_Seq();
//			pvBean.setString_Access_Time_Hour();
//			pvBean.setAccess_Time();

		return pvBean;

	}

}
