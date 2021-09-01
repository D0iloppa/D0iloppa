package test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataTest {

	public static void main(String[] args) {
		
		/**********/
		// 접근 페이지 페어
		
		List<String> listA = new ArrayList<String>();	// 접근 페이지 url
		List<String> listB = new ArrayList<String>();	// 접근 페이지 이름
		List<String> listC = new ArrayList<String>();	// 접근 타입
		
		listA.add("/test1.do");
		listA.add("/test2.do");
		listA.add("/test3.do");
		listA.add("/test4.do");
		
		listB.add("테스트조회");
		listB.add("테스트생성");
		listB.add("테스트다운로드");
		listB.add("테스트업로드");
		
		listC.add("RD");
		listC.add("CR");
		listC.add("DW");
		listC.add("UP");
		
		
		/**********/
		
		List<String> listD = new ArrayList<String>(); // 도메인 시퀀스 (현재는 1,2만 사용 | 다른 필드와 짝을 이루지 않는 독립필드)
		
		listD.add("1");
		listD.add("2");
		listD.add("1");
		listD.add("1");
		listD.add("1");
		listD.add("1");
		listD.add("1");
		listD.add("1");
		listD.add("1");
		
		/**********/
		
		// 각 인덱스별로 페어
		List<String> listE = new ArrayList<String>();	// 아이피
		List<String> listF = new ArrayList<String>();	// 아이디
		List<String> listFF = new ArrayList<String>();	// 이름
		List<String> listG = new ArrayList<String>();	// 부서코드
		List<String> listH = new ArrayList<String>();	// 부서이름
		
		listE.add("127.0.0.1");
		listE.add("127.0.0.2");
		listE.add("127.0.0.3");
		listE.add("127.0.0.4");
		listE.add("127.0.0.5");
		
		listF.add("so111");
		listF.add("park123");
		listF.add("kim123");
		listF.add("leejh");
		listF.add("sososo");
		
		listFF.add("소진희");
		listFF.add("박현준");
		listFF.add("홍길동");
		listFF.add("이재훈");
		listFF.add("김영희");
		
		listG.add("1");
		listG.add("1");
		listG.add("2");
		listG.add("2");
		listG.add("2");
		
		listH.add("개발팀");
		listH.add("개발팀");
		listH.add("기획팀");
		listH.add("기획팀");
		listH.add("기획팀");
		
		/**********/
		
		List<String> listI = new ArrayList<String>();	// 개인정보 유형
		List<String> listJ = new ArrayList<String>();	// 개인정보 내용
		
		listI.add("1");
		listI.add("2");
		listI.add("3");
		listI.add("4");
		listI.add("5");
		listI.add("6");
		listI.add("7");
		listI.add("8");
		listI.add("9");
		
		listJ.add("910111-2131415");
		listJ.add("MP9837477");
		listJ.add("02-837483-91");
		listJ.add("02-6942-9900");
		listJ.add("011 2345 6789");
		listJ.add("4979-5371-4873-3542");
		listJ.add("test@wellconn.co.kr");
		listJ.add("1-5648523219");
		listJ.add("434-22-00661-4");
		
		
		
		
		List<String> listK = new ArrayList<String>();	// 다운로드 이유
		List<String> listL= new ArrayList<String>();	// 이유(코드)
		
		listK.add("");
		listK.add("타 기관 제출용");
		listK.add("업무 목적");
		
		listL.add("2");
		listL.add("1");
		listL.add("1");
		
		
		
		
		
		
		

		String query1="";
		String query2="";
		
		int page_seq=42; // 페이지 시퀀스 마지막 번호 +1
		int priv_seq=100; // 프리브 시퀀스 마지막 번호 + 1
		
		String startDate = "20210830090000";
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.YEAR, Integer.parseInt(startDate.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(startDate.substring(4, 6)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(startDate.substring(6, 8)));

		cal.set(Calendar.HOUR, Integer.parseInt(startDate.substring(8, 10)));
		cal.set(Calendar.MINUTE, Integer.parseInt(startDate.substring(10, 12)));
		cal.set(Calendar.SECOND, Integer.parseInt(startDate.substring(12, 14)));
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		String text="";
		                                                        
		for ( int i=0; i<100; ++i ) {
			ThreadLocalRandom tlr = ThreadLocalRandom.current();
			 int randT = tlr.nextInt(1, 4);

			 cal.add(Calendar.MINUTE, randT);
			 
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			date = cal.get(Calendar.DATE);
			hour = cal.get(Calendar.HOUR);
			minute = cal.get(Calendar.MINUTE);
			second = cal.get(Calendar.SECOND);
			
			
			 int rand1 = tlr.nextInt(0, listA.size());
			 int rand2 = tlr.nextInt(0, listD.size());
			 int rand3 = tlr.nextInt(0, listE.size());
			 int rand4 = tlr.nextInt(0, listI.size());
			 int rand5 = tlr.nextInt(0, listK.size());
			 
			 String dateT =String.format("%02d-%02d-%02d %02d:%02d:%02d", year,month,date,hour,minute,second);
			 String dateTT =String.format("%02d%02d%02d", year,month,date);
			 String dateTTT =String.format("%02d", hour);
			 
			query1=  "insert into logker_access_page_tbl values ('"+page_seq+"','"+listA.get(rand1)+"','"+listB.get(rand1)+"','"+listC.get(rand1)+"','"+listD.get(rand2)+"','"+dateT+"','"+listE.get(rand3)+"','"+listF.get(rand3)+"','"+listFF.get(rand3)+"','"+listG.get(rand3)+"','"+listH.get(rand3)+"','"+
						listI.get(rand4)+":1','"+listK.get(rand5)+"','"+dateTT+"','"+dateTTT+"','"+listL.get(rand5)+"')";
			query2=  "insert into logker_access_priv_tbl values ('"+priv_seq+"','"+page_seq+"','"+listI.get(rand4)+"','"+listJ.get(rand4)+"','"+dateTT+"','"+listD.get(rand2)+"','"+dateTTT+"','"+dateT+"')";
			
			
			++page_seq;
			++priv_seq;

			System.out.println(query1);
			System.out.println(query2);
			System.out.println();
			
			text=text+query1+"\r\nGO\r\n"+query2+"\r\nGO\r\n";
			
		}
		
		
//			File file = new File("F:\\query.txt");
//
//			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//				 writer.append(text);
//			} catch (IOException e) {
//			    e.printStackTrace();
//			}
	}

}
