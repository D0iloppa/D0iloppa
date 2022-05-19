package kr.co.hhi.common.base;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestHeader;

public class CommonConst {	
	
	public static final int VERTICAL_VIEW_NUMBER = 10;
	public static final int VERTICAL_VIEW_NUMBER_POPUP = 10;
	public static final int HORIZONTAL_VIEW_NUMBER = 5;
	public static final int HORIZONTAL_VIEW_NUMBER_POPUP = 5;
	public static final int WARN_VERTICAL_VIEW_NUMBER = 10;
	public static final int SET_SESSION_TIME = 60*60; //(초단위 : 60 * 10 = 10분)

	public static String downloadNameEncoding(HttpServletRequest request,String resourceOriginalName) throws UnsupportedEncodingException {
		String downloadName = "";
		String userAgent = request.getHeader("User-Agent");
		// IE 브라우저의 경우(IE 브라우저의 엔진 이름 = "Trident")
		if(userAgent.contains("Trident")) {
			downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
		// Edge 브라우저의 경우
		} else if(userAgent.contains("Edge")) {
			downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
		// Chrome 브라우저의 경우
		} else {
		    downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
		}
		return downloadName;
	}
	/**
	 * 1. 메소드명 : currentDateAndTime
	 * 2. 작성일: 2021-11-17
	 * 3. 작성자: 소진희
	 * 4. 설명: 현재 날짜와 시간을 20211117032347 형태의 String으로 만들어줌
	 * 5. 수정일: 
	 */
	public static String currentDateAndTime(){
		LocalDateTime seoulNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String currentDateAndTime = seoulNow.format(formatter);
		return currentDateAndTime;
	}
	/**
	 * 1. 메소드명 : currentDate
	 * 2. 작성일: 2022-01-11
	 * 3. 작성자: 박정우
	 * 4. 설명: 현재 날짜와 시간을 202111 형태의 String으로 만들어줌
	 * 5. 수정일: 
	 */
	public static String currentDate(){
		LocalDate seoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		String currentDate = seoulNow.format(formatter);
		return currentDate;
	}
	/**
	 * 1. 메소드명 : stringdateTODate
	 * 2. 작성일: 2022-01-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 20211117032347 형태 -> 2021-11-17 03:23:47 형태
	 * 5. 수정일: 
	 */
	public static String stringdateTODate(String date){
		
		if(date == null || date == "") {
			return "";
		}else {
			String yyyy = date.substring(0, 4);
			String MM = date.substring(4, 6);
			String dd = date.substring(6, 8);
			String HH = date.substring(8, 10);
			String mm = date.substring(10, 12);
			String ss = date.substring(12, 14);
			
			return yyyy+"-"+MM+"-"+dd+" "+HH+":"+mm+":"+ss;
		}
	}
	/**
	 * 1. 메소드명 : stringdateTOYYYYMMDD
	 * 2. 작성일: 2022-02-16
	 * 3. 작성자: 소진희
	 * 4. 설명: 20211117 형태 -> 2021-11-17 형태
	 * 5. 수정일: 
	 */
	public static String stringdateTOYYYYMMDD(String date){
		
		if(date == null || date == "") {
			return "";
		}else if(date.length()==6) {
			String yyyy = date.substring(0, 4);
			String MM = date.substring(4, 6);
			
			return yyyy+"-"+MM;
		} else if(date.length()==8 || date.length()==14){
			String yyyy = date.substring(0, 4);
			String MM = date.substring(4, 6);
			String dd = date.substring(6, 8);
			
			return yyyy+"-"+MM+"-"+dd;
		}else {
			return date;
		}
	}
	
	/**
	 * 1. 메소드명 : intByte
	 * 2. 작성일: 2022-01-05
	 * 3. 작성자: 소진희
	 * 4. 설명: long 형의 데이터를 받아 각 크기에 맞는 형태로 변경
	 * 5. 수정일: 
	 */
	public static String intByte(long bytelong){
		String bytes = Long.toString(bytelong);
		String retFormat = "0";
        Double size = Double.parseDouble(bytes);

         String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };
         
        if (bytes.equals("0")) {
        	retFormat += " " + s[0];
        } else {
             int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
             DecimalFormat df = new DecimalFormat("#,###.##");
             double ret = ((size / Math.pow(1024, Math.floor(idx))));
             retFormat = df.format(ret) + " " + s[idx];
        }

          return retFormat;
	}

	/**
	 * 1. 메소드명 : random5
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 소진희
	 * 4. 설명: 5자리의 랜덤 숫자를 생성하는 메서드
	 * 5. 수정일: 
	 */
	public static int random5() {
		int authNo = (int)(Math.random() * (99999 - 10000 + 1)) + 10000;
		return authNo;
	}

	/**
	 * 1. 메소드명 : isPwdRule
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 소진희
	 * 4. 설명: 영문, 숫자, 특수문자,공백 체크 - 허용되지 않는 문자 체크
	 * 5. 수정일: 
	 */
	public static boolean isPwdRule( String pwd ) {
		char [] s = pwd.toCharArray();
		char [] symbol = { '`', '~', '!', '@', '#', '$', '%', '^', '*', '(', ')', '-', '_', '=' };
		
		for( int i=0; i<s.length; i++ ) {
			if((s[i]>='0' && s[i]<='9') || (s[i]>='a' && s[i]<='z') || (s[i]>='A' && s[i]<='Z')) {
				continue;
			} else if( s[i] == ' ' ) {
				return false;
			} else {
				for( int j=0; j<symbol.length; j++ ) {
					if( s[i] == symbol[j] ) return true; 
				}
				return false;
			}
		}
		return true;
	}

	
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress=request.getRemoteAddr();
		 
    	 if(ipAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
    	     InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
    	     ipAddress=inetAddress.getHostAddress();
    	 }
    	 return ipAddress;
	}

}
