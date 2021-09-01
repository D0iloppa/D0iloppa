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

	// DB 연결 관련 변수
	static String JDBC_DRIVER;
	static String DB_URL;
	static String USERNAME;
	static String PASSWORD;
	static Connection conn;

	// Bean생성을 위한 데이터셋
	public static class DataSet {

		// 아래 리스트들을 private로 선언하고 게터와 세터로 받아주는것이 이상적이나
		// 랜덤한 값을 생성해서 넣어주는 용도로 생성한 클래스이기때문에 public으로 간단히 사용할 예정
		List<AccessPageSet> apList;
		List<UserSet> usrList;
		List<String> domSeq; // 도메인 시퀀스 (현재는 1,2만 사용 | 다른 필드와 짝을 이루지 않는 독립필드)

		List<String> piList; // 개인정보 유형
		List<String> pcList; // 개인정보 내용

		List<String> reasonList; // 다운로드 이유
		List<String> reasonCodeList; // 이유(코드)

		int page_Seq, priv_Seq;

		public DataSet() {
			super();

			apList = new ArrayList<AccessPageSet>();
			usrList = new ArrayList<UserSet>();
			domSeq = new ArrayList<String>(); // 도메인 시퀀스 (현재는 1,2만 사용 | 다른 필드와 짝을 이루지 않는 독립필드)

			piList = new ArrayList<String>(); // 개인정보 유형
			pcList = new ArrayList<String>(); // 개인정보 내용

			reasonList = new ArrayList<String>(); // 다운로드 이유
			reasonCodeList = new ArrayList<String>(); // 이유(코드)

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

		void setFieldDomain() { // 각 필드별 들어갈 항목들의 페어 도메인 초기화

			// 랜덤생성할 접근페이지 범위
			this.apList.add(new AccessPageSet("/test1.do", "테스트조회", "RD"));
			this.apList.add(new AccessPageSet("/test2.do", "테스트생성", "CR"));
			this.apList.add(new AccessPageSet("/test3.do", "테스트다운로드", "DW"));
			this.apList.add(new AccessPageSet("/test4.do", "테스트업로드", "UP"));

			// 랜덤생성할 접근자 범위
			this.usrList.add(new UserSet("127.0.0.1", "so111", "소진희", "1", "개발팀"));
			this.usrList.add(new UserSet("127.0.0.2", "park123", "박현준", "1", "개발팀"));
			this.usrList.add(new UserSet("127.0.0.3", "kim123", "홍길동", "2", "기획팀"));
			this.usrList.add(new UserSet("127.0.0.4", "leejh", "이재훈", "2", "기획팀"));
			this.usrList.add(new UserSet("127.0.0.5", "sososo", "김영희", "2", "기획팀"));

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
			this.reasonList.add("타 기관 제출용");
			this.reasonList.add("업무 목적");

			this.reasonCodeList.add("2");
			this.reasonCodeList.add("1");
			this.reasonCodeList.add("1");

		}

	}

	static DataSet ds = new DataSet(); // startDate로 데이터셋 생성

	// Bean리스트
	static List<Beans.PageBean> pbList = new ArrayList<>();
	static List<Beans.PrivBean> pvList = new ArrayList<>();

	static Calendar cal;
	static int year, month, date, hour, minute, second;
	static String dateT, dateTT, dateTTT;

	// 로그를 찍는 기준 시간날짜
	static String startDate;
	static long runDate, endDate; // runDate : 시작시간으로 부터 증가된 시간으로, 진행중인 시간의 커서 | endDate : runDate의 종료값

	// 메인함수
	public static void main(String[] args) {

		try {
			init(); // 초기화
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void init() throws SQLException {
		getProperties(); // 프로퍼티 정보를 가져옴
		setCal(); // Bean 시간 생성을 위한 Calender 설정
		connectDB(); // DB와 연결

	}

	public static void getProperties() {
		ProcProperties.setFileName("po");

		JDBC_DRIVER = ProcProperties.readProperty("JDBC_DRIVER");
		DB_URL = ProcProperties.readProperty("DB_URL");
		USERNAME = ProcProperties.readProperty("USERNAME");
		PASSWORD = ProcProperties.readProperty("PASSWORD");

	}

	public static void setCal() { // 달력설정

		startDate = "20210901090000";

		Date date_Now = new Date(System.currentTimeMillis()); // 현재시간
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");

		// 시간 문자열을 정수형태로 보고, 대소비교를 통해 기준시간을 초과했는지 안했는지 확인하기위해 Long형으로 변환
		endDate = Long.parseLong(fm.format(date_Now).toString()); // 종료를 위한 현재시간

		cal = Calendar.getInstance(); // 캘린더 변수

		cal.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(startDate.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(startDate.substring(6, 8)));

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startDate.substring(8, 10)));
		cal.set(Calendar.MINUTE, Integer.parseInt(startDate.substring(10, 12)));
		cal.set(Calendar.SECOND, Integer.parseInt(startDate.substring(12, 14)));

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		date = cal.get(Calendar.DATE);
		hour = cal.get(Calendar.HOUR_OF_DAY); // 24 시간
		minute = cal.get(Calendar.MINUTE);
		second = cal.get(Calendar.SECOND);

	}

	private static void connectDB() throws SQLException {
		System.out.println("[DB연결]");

		PreparedStatement pstmt = null;

		try {

			Class.forName(JDBC_DRIVER);

			// MYsql과 커넥션 연결
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
			
			pstmt = conn.prepareStatement(
					"SELECT access_page_seq FROM logker_access_page_tbl ORDER BY access_page_seq DESC LIMIT 1");
			ResultSet rs1 = pstmt.executeQuery();
			// page_Seq의 마지막 인덱스 가져옴
			ds.page_Seq = (rs1.next()) ? rs1.getInt(1) : 0;
			System.out.println("The index of Last PageSeq : " + ds.page_Seq);

			
			pstmt = conn.prepareStatement(
					"SELECT access_priv_seq FROM logker_access_priv_tbl ORDER BY access_page_seq DESC LIMIT 1");
			ResultSet rs2 = pstmt.executeQuery();
			// priv_Seq의 마지막 인덱스 가져옴
			ds.priv_Seq = (rs2.next()) ? rs2.getInt(1) : 0;
			System.out.println("The index of Last PrivSeq : " + ds.priv_Seq);
			
			// access_priv 테이블에서 내림차순으로 정렬하면 page_Seq와 priv_Seq 모두 가져올 수 있을것으로 보이므로
			// 질의문을 한번만 던져도 될 것 같아 보임

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
			System.out.println("[DB연결 종료]");
		}

	}

	private static void bodyFunc() throws SQLException {
		Scanner sc = new Scanner(System.in);
		
		
		boolean run = true;
		int menu = -1;

		while (run) {
			System.out.println("=========================================================");
			System.out.println("1. Bean 랜덤 생성	|2. Load From Log	|3.설정	|4.종료	|");
			System.out.println("=========================================================");
			System.out.print("메뉴입력 (1~4 숫자 입력) : ");
			menu = sc.nextInt();

			switch (menu) {
				case 1:
					System.out.println("작업 시작 기준시간 : " + startDate);
					System.out.println("작업 종료 기준시간 : " + endDate);

					while (runDate <= endDate) {
						createBeans(); // 랜덤한 값으로 레코드 생성, Bean에저장
						runDate = Long
								.parseLong(String.format("%02d%02d%02d%02d%02d%02d", year, month, date, hour, minute, second)); // 시간을
																																// 값
					}
					System.out.println("\nPageBean 생성 갯수 : " + cntPage + " / PrivBean 생성 갯수 : " + cntPriv + "\n");
					
					queryJob(); //위의 작업에서 생성된 Bean리스트들을 모두 쿼리문으로 서버에 입력
					break;
					
				case 2:
					System.out.println("2번 메뉴 미구현");
					// 추후 구현해야함
					break;
					
				case 3:
					setting();
					break;
					
				case 4:
					run = false; // 메뉴를 종료
					System.out.println("AGENT 종료");
					break;
					
				default:
					System.out.println("\n잘못된 메뉴 입력\n");

			}

		}

	}

	private static void setting() {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("========= SETTING =========");
		System.out.println("현재 지정된 작업 시작 기준시간값 : " + startDate);
		System.out.println("현재 작업 종료 기준시간값 : " + endDate);
		
		
		// 입력값이 유효한지 확인하는 메소드를 추가하면 더 안정성이 좋아짐
		// 사용자가 입력포맷에 맞게 입력할거라고 가정하고 간단히 구현
		System.out.print("startDate:");
		startDate = sc.nextLine();
		System.out.print("endDate:");
		endDate = sc.nextLong();
		
		
		
		
	}

	private static void queryJob() throws SQLException {

		System.out.println("Bean 불러와서 쿼리 작업 중...");

		for (Beans.PageBean pb : pbList) {
			dataInputQuery1(pb);
		}

		for (Beans.PrivBean pv : pvList) {
			dataInputQuery2(pv);
		}

		System.out.println("쿼리작업 완료\n");

	}

	private static void dataInputQuery1(Beans.PageBean bean) throws SQLException {

		PreparedStatement pstmt = null;

		String query = "INSERT INTO logker_access_page_tbl VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // 바인드변수

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
		// Bean 생성
		Random r = new Random();

		int rand_AccessIdx = r.nextInt(ds.apList.size());
		int rand_DomIdx = r.nextInt(ds.domSeq.size());
		int rand_UserIdx = r.nextInt(ds.usrList.size());
		int rand_PrivIdx = r.nextInt(ds.piList.size());
		int rand_DRIdx = r.nextInt(ds.reasonList.size());

		addTime(); // 랜덤시간을 더해줌

		dateT = String.format("%02d-%02d-%02d %02d:%02d:%02d", year, month, date, hour, minute, second);
		dateTT = String.format("%02d%02d%02d", year, month, date);
		dateTTT = String.format("%02d", hour);

		pbList.add(randPageBean(rand_AccessIdx, rand_DomIdx, rand_UserIdx, rand_PrivIdx, rand_DRIdx));

		int rand_ConcurPriv = r.nextInt(10); // privBean은 PageBean과 1:N 관계이므로 N개의 랜덤한 갯수를 생성 (최대 10)
		for (int i = 0; i < rand_ConcurPriv; i++) {
			rand_PrivIdx = r.nextInt(ds.piList.size());
			rand_DomIdx = r.nextInt(ds.domSeq.size());
			pvList.add(randPrivBean(rand_PrivIdx, rand_DomIdx));
		}
	}

	// 시간 증가 처리 함수
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

	// PageBean 랜덤생성
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

		String query = "INSERT INTO logker_access_page_tbl VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; // 바인드변수

		/* 1. Bean을 생설할때 마다 쿼리를 날리는 방법 */
		//
		/* 2. Bean들을 List에 담아놓고 한번에 처리하는 방법 */
		// 현재 2번의 방법으로 구현함

//		new Beans().showBean(pbBean); // Bean내용 체크용 함수
		cntPage++; // bean 생성 갯수 체크를 위한 카운트 변수

		return pbBean;

	}

	// PrivBean 랜덤생성
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

		cntPriv++; // bean 생성 갯수 체크를 위한 카운트 변수

//		new Beans().showBean(pvBean); // Bean내용 보기

		return pvBean;

	}
	
	
	
	/*****************************************************/
	// 로그파일로부터 라인을 받았을 때 구분자로 구분해서 PageBean,PrivBean 생성
	// 추후 구현해야함
	static Beans.PageBean inputPageBean(String line) {
		// AccessPage Tabele을 위한 Bean
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
		// AccessPriv Table을 위한 Bean
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
