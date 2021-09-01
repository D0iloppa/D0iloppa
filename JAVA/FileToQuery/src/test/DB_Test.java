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
	
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 			// ����̹�
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/wellconn";	// DB�ּ�
	static final String USERNAME = "root"; 									// ����� ���̵�
	static final String PASSWORD = "Wellconn!1"; 							// ��ȣ
	
	static List<PrivBean> priv_List = new ArrayList<>(); // ���κ� ���� ó���� ���� BeanŬ���� ����Ʈ

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		// ������ �ҷ���
		System.out.println("���� �ε�...");
		fileLoad();
		
		// ������ �� �ҷ��Դ��� Ȯ�ο�
		System.out.println("[PrivBean�� ���� : " + priv_List.size() + "]\n");
		
		
		
		// DB����
		System.out.println("[DB����]");
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		
		
		try {

			String query = "INSERT INTO priv_log VALUES(?,?,?,?,?,?,?,?,?)"; // ���ε庯��

			Class.forName(JDBC_DRIVER);
				
			// MYsql�� Ŀ�ؼ� ����
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			pstmt = conn.prepareStatement(query);
			
			
			for(PrivBean pb : priv_List) {
				
				// ��ϵ� bean���� preparedStatement�� �Է�
				pstmt.setString(1,pb.getDate());
				pstmt.setString(2,pb.getTime());
				pstmt.setString(3,pb.getId());
				pstmt.setString(4,pb.getNm());
				pstmt.setString(5,pb.getDpt());
				pstmt.setString(6,pb.getIp());
				pstmt.setString(7,pb.getUrl());
				pstmt.setString(8,pb.getResult());
				pstmt.setString(9,pb.getDomain());
				
				// ���κ��� ������ ����
				
				System.out.println("������ ���� : "
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
			System.out.println("[DB���� ����]");
		}
		
		


	}
	
	
	static void fileLoad() {
		List<Path> fileList = new ArrayList<>();
				
		
		try {
			// ���丮 �� ���ϸ���Ʈ �ҷ�����
			Files.walk(Paths.get("C:/Users/defin/Desktop/DATA")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) fileList.add(filePath);			    
			});
			
			// ���ϸ���Ʈ �� �߰��Ǿ����� Ȯ��
			// for(Path list : fileList) System.out.println(list);
			
			// �� ���Ϻ��� ��� ��Ʈ�� �ҷ����̱�
			for(Path list : fileList) { // ��� ���ϸ���Ʈ�� ���Ͽ� �۾� ����
//				System.out.println("[" + list + "]");
				URI work_Path = list.toUri(); // ���� �ҷ��� �����н�
				List<String> lines = Files.readAllLines(Paths.get(work_Path));
				for(String line : lines) {
					String[] splited_Str = line.split("@@@"); // �����ڷ� ���� �����ؼ� �迭�� ����
					
					if(splited_Str.length == 9) { // 9���� ���ڰ� ��� �����ϴ� ��쿡�� ���� ó��
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
						
						priv_List.add(pb); // ������ �� �߰�
					}
					
							
				}
					
				System.out.println();
			
				
				// �۾� �Ϸ��� ���ϻ���
				File file = new File(list.toString());
				if(file.exists()) file.delete();
			
			}
			
		 
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}
