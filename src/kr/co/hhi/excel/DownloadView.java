package kr.co.hhi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView {

	public void Download() { 
		setContentType("application/download; utf-8"); 
	}
	
	//파일 다운로드
	
	public String getCurrentTimeStamp() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}
			
	static String realFileName="";
			
	public static void setFileName(String realFileNameT) {
		realFileName = realFileNameT;
	}
	
	/*
	 * @Override protected void renderMergedOutputModel_old(Map paramMap,
	 * HttpServletRequest request, HttpServletResponse response) throws Exception {
	 * 
	 * File file = (File)paramMap.get("downloadFile");
	 * response.setContentType(getContentType());
	 * response.setContentLength((int)file.length());
	 * 
	 * Map<String, String> map =new HashMap<String, String>(); map.put("fileNm",
	 * file.getName());
	 * 
	 * //해당 변수명으로 파일이 생성됨 //String
	 * ori_fileName="downFile_"+getCurrentTimeStamp()+".hwp"; //String
	 * ori_fileName=file.getName(); String ori_fileName=realFileName; ori_fileName =
	 * URLEncoder.encode(ori_fileName,"UTF-8").replaceAll("\\+", "%20");
	 * 
	 * response.setHeader("Content-Disposition", "attachment; filename=\"" +
	 * ori_fileName + "\";");
	 * 
	 * response.setHeader("Content-Transfer-Encoding", "binary"); OutputStream out =
	 * response.getOutputStream();
	 * 
	 * FileInputStream fis = null;
	 * 
	 * try { fis = new FileInputStream(file); FileCopyUtils.copy(fis, out); }
	 * catch(Exception e){ e.printStackTrace(); } finally{ if(fis != null){ try{
	 * fis.close(); } catch(Exception e){
	 * 
	 * } } }
	 * 
	 * out.flush(); }
	 */
	
	@Override
	protected void renderMergedOutputModel(Map paramMap, HttpServletRequest request, 
			HttpServletResponse response) throws Exception { 
		
		File file = (File)paramMap.get("downloadFile"); 
		response.setContentType(getContentType()); 
		response.setContentLength((int)file.length()); 
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileNm", file.getName());
		
		//해당 변수명으로 파일이 생성됨
		//String ori_fileName="downFile_"+getCurrentTimeStamp()+".hwp";
		//String ori_fileName=file.getName();
		String ori_fileName=realFileName;
		ori_fileName = URLEncoder.encode(ori_fileName, "UTF-8")/* .replaceAll("\\+", "%20") */;
		ori_fileName = URLDecoder.decode(ori_fileName, "ISO8859_1");
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + ori_fileName);
		
		response.setHeader("Content-Transfer-Encoding", "binary"); 
		OutputStream out = response.getOutputStream();
		
		FileInputStream fis = null;
		
		try { 
			fis = new FileInputStream(file); 
			FileCopyUtils.copy(fis, out); 
		} catch(Exception e){ 
			e.printStackTrace(); 
		} finally{ 
			if(fis != null){ 
				try{ 
					fis.close(); 
				} catch(Exception e){
					
				} 
			}
		}
		
		out.flush();
	}	
	
}
