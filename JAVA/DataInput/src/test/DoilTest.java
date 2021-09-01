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
	
	
	// Bean생성을 위한 데이터셋
	
	
	public static class DataSet {


		// 아래 리스트들을 private로 선언하고 게터와 세터로 받아주는것이 이상적이나
		// 랜덤한 값을 생성해서 넣어주는 용도로 생성한 클래스이기때문에 public으로 간단히 사용할 예정
		List<AccessPageSet> apList;
		List<UserSet> usrList;
		List<String> domSeq;			// 도메인 시퀀스 (현재는 1,2만 사용 | 다른 필드와 짝을 이루지 않는 독립필드)
		
		List<String> piList;			// 개인정보 유형
		List<String> pcList;			// 개인정보 내용

		List<String> reasonList;		// 다운로드 이유
		List<String> reasonCodeList;	// 이유(코드)

		int page_Seq, priv_Seq;
		

		public DataSet(String startDate) {
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
	static DataSet ds = new DataSet("20210830090000"); // startDate로 데이터셋 생성
	

	// Bean리스트
	static List<Beans.PageBean> pbList = new ArrayList<>();
	static List<Beans.PrivBean> pvList = new ArrayList<>();

	static Calendar cal;
	static int year, month, date, hour, minute, second;
	static String dateT, dateTT, dateTTT;

	// 로그를 찍는 기준 시간날짜
	static String startDate;
	
	static long endDate;

	
	
	
	
	public static void main(String[] args) {
		ds.page_Seq = getLastPageSeq(); // 페이지 시퀀스 마지막 번호 +1 해주기 위함
		ds.priv_Seq = getLastPrivSeq(); // 프리브 시퀀스 마지막 번호 +1 해주기 위함

		startDate = "20210830090000";
		
		Date date_Now = new Date(System.currentTimeMillis()); // 현재시간
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
		endDate = Long.parseLong(fm.format(date_Now).toString());
		
		System.out.println(endDate);

		cal = Calendar.getInstance(); // 캘린더 변수
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
		// Bean 생성
		Random r = new Random();

		int rand_AccessIdx = r.nextInt(ds.apList.size());
		int rand_DomIdx = r.nextInt(ds.domSeq.size());
		int rand_UserIdx = r.nextInt(ds.usrList.size());
		int rand_PrivIdx = r.nextInt(ds.piList.size());
		int rand_DRIdx = r.nextInt(ds.reasonList.size());

		// 시간설정

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

		lastIdx = 42; // 마지막 시퀀스 구하는 질의문 던져서 가져와야함

		return lastIdx;

	}

	static int getLastPrivSeq() {
		int lastIdx;

		lastIdx = 100; // 마지막 시퀀스 구하는 질의문 던져서 가져와야함

		return lastIdx;

	}

	
	
	// PageBean 생성
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

		new Beans().showBean(pbBean);

		return pbBean;

	}
	// PrivBean 생성
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
