package test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import bean.PrivBean;

public class DB_Test {
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 			// 드라이버
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/wellconn";	// DB주소
	static final String USERNAME = "root"; 									// 사용자 아이디
	static final String PASSWORD = "Wellconn!1"; 							// 암호
	
	static List<PrivBean> priv_List = new ArrayList<>(); // 라인별 쿼리 처리를 위한 Bean클래스 리스트

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		// 파일을 불러옴
		System.out.println("파일 로딩...");
		fileLoad();
		
		// 라인을 잘 불러왔는지 확인용
		System.out.println("[PrivBean의 갯수 : " + priv_List.size() + "]\n");
		
		
		
		// DB연결
		System.out.println("[DB연결]");
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		
		
		try {

			String query = "INSERT INTO priv_log VALUES(?,?,?,?,?,?,?,?,?)"; // 바인드변수

			Class.forName(JDBC_DRIVER);
				
			// MYsql과 커넥션 연결
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			pstmt = conn.prepareStatement(query);
			
			
			for(PrivBean pb : priv_List) {
				
				// 등록된 bean에서 preparedStatement로 입력
				pstmt.setString(1,pb.getDate());
				pstmt.setString(2,pb.getTime());
				pstmt.setString(3,pb.getId());
				pstmt.setString(4,pb.getNm());
				pstmt.setString(5,pb.getDpt());
				pstmt.setString(6,pb.getIp());
				pstmt.setString(7,pb.getUrl());
				pstmt.setString(8,pb.getResult());
				pstmt.setString(9,pb.getDomain());
				
				// 라인별로 쿼리문 실행
				
				System.out.println("쿼리문 실행 : "
				+ String.format("INSERT INTO priv_log VALUES('%s' , '%s' , '%s' , '%s ' , '%s' , '%s' , '%s' , '%s' , '%s' )",
						pb.getDate(),pb.getTime(),pb.getId(),pb.getNm(),pb.getDpt(),pb.getIp(),pb.getUrl(),pb.getResult(),pb.getDomain()));
				
				pstmt.executeUpdate();
				
			}
			
			System.out.println();
			

		}catch(SQLException e) {
			e.printStackTrace();

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			pstmt.close();
			conn.close();
			System.out.println("[DB연결 종료]");
		}
		
		


	}
	
	
	static void fileLoad() {
		List<Path> fileList = new ArrayList<>();
				
		
		try {
			// 디렉토리 내 파일리스트 불러오기
			Files.walk(Paths.get("C:/Users/defin/Desktop/DATA")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) fileList.add(filePath);			    
			});
			
			// 파일리스트 잘 추가되었는지 확인
			// for(Path list : fileList) System.out.println(list);
			
			// 각 파일별로 모든 스트링 불러들이기
			for(Path list : fileList) { // 모든 파일리스트에 대하여 작업 수행
//				System.out.println("[" + list + "]");
				URI work_Path = list.toUri(); // 현재 불러온 파일패스
				List<String> lines = Files.readAllLines(Paths.get(work_Path));
				for(String line : lines) {
					String[] splited_Str = line.split("@@@"); // 구분자로 문자 구분해서 배열로 저장
					
					if(splited_Str.length == 9) { // 9개의 인자가 모두 존재하는 경우에만 쿼리 처리
						PrivBean pb = new PrivBean();
						
						pb.setDate(splited_Str[0]);
						pb.setTime(splited_Str[1]);
						pb.setId(splited_Str[2]);
						pb.setNm(splited_Str[3]);
						pb.setDpt(splited_Str[4]);
						pb.setIp(splited_Str[5]);
						pb.setUrl(splited_Str[6]);
						pb.setResult(splited_Str[7]);
						pb.setDomain(splited_Str[8]);
						
						priv_List.add(pb); // 쿼리문 빈 추가
					}
					
							
				}
					
				System.out.println();
			
				
				// 작업 완료후 파일삭제
				File file = new File(list.toString());
				if(file.exists()) file.delete();
			
			}
			
		 
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
