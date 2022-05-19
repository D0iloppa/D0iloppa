package kr.co.doiloppa.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.StylesTable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.AES256Util;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.excel.Sheet2ListHandler;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.NoticeBoardVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocTrNoChgHistVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjDrnInfoVO;
import kr.co.doiloppa.model.PrjEmailVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.model.WbsCodeControlVO;
import kr.co.doiloppa.service.PrjCodeSettingsService;
import kr.co.doiloppa.service.PrjDocumentIndexService;
import kr.co.doiloppa.service.PrjEmailService;
import kr.co.doiloppa.service.PrjInfoService;
import kr.co.doiloppa.service.PrjStepService;
import kr.co.doiloppa.service.PrjTrService;
import kr.co.doiloppa.service.SessionService;
import kr.co.doiloppa.service.UsersService;
import kr.co.doiloppa.service.porServiceOracle;

@Controller
@RequestMapping("/*")
public class PrjDocumentIndexCont {

	protected PrjDocumentIndexService  prjdocumentindexService;
	protected PrjStepService  prjstepService;
	protected PrjCodeSettingsService  prjcodesettingsService;
	protected PrjTrService prjtrService;
	protected PrjEmailService  prjemailService;
	protected SessionService sessionService;
	protected UsersService  usersService;
	protected PrjInfoService prjinfoService;
	protected porServiceOracle porserviceoracle;

	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	@Autowired
	public void setPrjTrService(PrjTrService prjtrService) {
		this.prjtrService = prjtrService;
	}
	@Autowired
	public void setPrjEmailService(PrjEmailService prjemailService) {
		this.prjemailService = prjemailService;
	}
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setporServiceOracle(porServiceOracle porserviceoracle) {
		this.porserviceoracle = porserviceoracle;
	}

    @Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
    
    @Value("#{config['file.upload.prj.doc.path']}")
    private String DOC_PATH;

    @Value("#{config['file.upload.prj.template.path']}")
    private String TEMPLATE_PATH;
    
	/**
	 * 1. 메소드명 : fileDownload
	 * 2. 작성일: 2022-01-04
	 * 3. 작성자: 소진희
	 * 4. 설명: 멀티파일 다운로드 (Zip으로 압축)
	 * 5. 수정일: 
	 */
    @RequestMapping("fileDownload.do")
    public void CompressZIP(@RequestParam String prj_id,@RequestParam String renameYN,@RequestParam String fileRenameSet,@RequestParam String downloadFiles,PrjDocumentIndexVO prjdocumentindexvo, HttpServletRequest request, HttpServletResponse response, Object handler) {
    	
    	String downloadFilesString = prjdocumentindexvo.getDownloadFiles();
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
    	String[] rfiles = new String[downloadFileEach.length];
    	String[] renamefiles = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			String[] downloadFile = downloadFileEach[i].split("%%");
			prjdocumentindexvo.setDoc_id(downloadFile[0]);
			prjdocumentindexvo.setRev_id(downloadFile[1]);
			PrjDocumentIndexVO getDownloadFileInfo = prjdocumentindexService.getDownloadFileInfo(prjdocumentindexvo);
			if(prjdocumentindexvo.getRenameYN().equals("Y")) {
				String fileRenameSetting = prjdocumentindexvo.getFileRenameSet();
				String title = getDownloadFileInfo.getTitle()==null?"":getDownloadFileInfo.getTitle();
				if(getDownloadFileInfo.getRev_no()==null) {
					fileRenameSetting = fileRenameSetting.replaceAll("%DOCNO%", getDownloadFileInfo.getDoc_no())
						.replaceAll("%REVNO%", "")
						.replaceAll("%TITLE%", title);
				}else {
					fileRenameSetting = fileRenameSetting.replaceAll("%DOCNO%", getDownloadFileInfo.getDoc_no())
						.replaceAll("%REVNO%", getDownloadFileInfo.getRev_no())
						.replaceAll("%TITLE%", title);
				}
				renamefiles[i] = fileRenameSetting.replaceAll("/", "-") + "." + getDownloadFileInfo.getFile_type();
			}
			files[i] = getDownloadFileInfo.getSfile_nm().replaceAll("/", "-");
			rfiles[i] = getDownloadFileInfo.getRfile_nm();
			if(rfiles[i]==null) rfiles[i]=files[i];
			
		}
		LocalDateTime now = LocalDateTime.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    	ZipOutputStream zout = null;
    	String zipName = "VISIONdcs_"+formatedNow+".zip";		//ZIP 압축 파일명
    	String tempPath = "";

    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;		//파일 저장경로
                 
                 //ZIP파일 압축 START
                 zout = new ZipOutputStream(new FileOutputStream(tempPath + zipName));
                 byte[] buffer = new byte[1024];
                 FileInputStream in = null;
                 
                 for ( int k=0; k<files.length; k++){
                     //in = new FileInputStream("/vdsFileupload/" + files[k]);		//압축 대상 파일
                	 in = new FileInputStream(tempPath + files[k]);		//압축 대상 파일                	 
                    if(prjdocumentindexvo.getRenameYN().equals("Y")) {
            			zout.putNextEntry(new ZipEntry(renamefiles[k]));	//압축파일에 저장될 파일명(rename)
                    }else {
            			zout.putNextEntry(new ZipEntry(rfiles[k]));	//압축파일에 저장될 파일명
                    }
                    int len;
                    while((len = in.read(buffer)) > 0){
                       zout.write(buffer, 0, len);			//읽은 파일을 ZipOutputStream에 Write
                    }
                    
                    zout.closeEntry();
                    in.close();
                 }
                 
                 zout.close();
                 //ZIP파일 압축 END
                 
                 //파일다운로드 START
                 response.setContentType("application/zip");
 				 response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                 response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
                 
                 FileInputStream fis = new FileInputStream(tempPath + zipName);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ServletOutputStream so = response.getOutputStream();
                 BufferedOutputStream bos = new BufferedOutputStream(so);
                 
                 int n = 0;
                 while((n = bis.read(buffer)) > 0){
                    bos.write(buffer, 0, n);
                    bos.flush();
                 }
                 
                 if(bos != null) bos.close();
                 if(bis != null) bis.close();
                 if(so != null) so.close();
                 if(fis != null) fis.close();
                 //파일다운로드 END
                 
       			Path filePath = Paths.get(tempPath + zipName);
       			//tempPath폴더에 생성된 zip파일 삭제
       			Files.deleteIfExists(filePath);
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }finally{
                 if (zout != null){
                    zout = null;
                 }
              }
    	}
    }

	/**
	 * 1. 메소드명 : fileDownloadEmail
	 * 2. 작성일: 2022-01-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 멀티파일 다운로드 (Zip으로 압축)
	 * 5. 수정일: 
	 */
    @RequestMapping("fileDownloadEmail.do")
    public void fileDownloadEmail(@RequestParam String prj_id,@RequestParam String renameYN,@RequestParam String fileRenameSet,@RequestParam String downloadFiles,PrjDocumentIndexVO prjdocumentindexvo, HttpServletRequest request, HttpServletResponse response, Object handler) {
    	String downloadFilesString = prjdocumentindexvo.getDownloadFiles();
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
    	String[] rfiles = new String[downloadFileEach.length];
    	String[] renamefiles = new String[downloadFileEach.length];
    	files[0] = downloadFileEach[0];//0번째는 COVER FILE
    	rfiles[0] = downloadFileEach[0];
		renamefiles[0] = downloadFileEach[0];
		for(int i=1;i<downloadFileEach.length;i++) {
			String[] downloadFile = downloadFileEach[i].split("%%");
			prjdocumentindexvo.setDoc_id(downloadFile[0]);
			prjdocumentindexvo.setRev_id(downloadFile[1]);
			PrjDocumentIndexVO getDownloadFileInfo = prjdocumentindexService.getDownloadFileInfo(prjdocumentindexvo);
			if(prjdocumentindexvo.getRenameYN().equals("Y")) {
				String title = getDownloadFileInfo.getTitle()==null?"":getDownloadFileInfo.getTitle();
				String fileRenameSetting = prjdocumentindexvo.getFileRenameSet();
				fileRenameSetting = fileRenameSetting.replaceAll("%DOCNO%", downloadFile[2]).replaceAll("%DOC_NO%", downloadFile[2])
					.replaceAll("%REVNO%", downloadFile[3]).replaceAll("%REV_NO%", downloadFile[3])
					.replaceAll("%TITLE%", title);
				renamefiles[i] = fileRenameSetting + "." + getDownloadFileInfo.getFile_type();
			}
			files[i] = getDownloadFileInfo.getSfile_nm();
			rfiles[i] = getDownloadFileInfo.getRfile_nm();
			
		}
		LocalDateTime now = LocalDateTime.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    	ZipOutputStream zout = null;
    	String zipName = "VISIONdcs_"+formatedNow+".zip";		//ZIP 압축 파일명
    	String tempPath = "";

    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;		//파일 저장경로
                 
                 //ZIP파일 압축 START
                 zout = new ZipOutputStream(new FileOutputStream(tempPath + zipName));
                 byte[] buffer = new byte[1024];
                 FileInputStream in = null;
                 
                 for ( int k=0; k<files.length; k++){
                     //in = new FileInputStream("/vdsFileupload/" + files[k]);		//압축 대상 파일
                	 in = new FileInputStream(tempPath + files[k]);		//압축 대상 파일                	 
                    if(prjdocumentindexvo.getRenameYN().equals("Y")) {
            			zout.putNextEntry(new ZipEntry(renamefiles[k]));	//압축파일에 저장될 파일명(rename)
                    }else {
            			zout.putNextEntry(new ZipEntry(rfiles[k]));	//압축파일에 저장될 파일명
                    }
                    int len;
                    while((len = in.read(buffer)) > 0){
                       zout.write(buffer, 0, len);			//읽은 파일을 ZipOutputStream에 Write
                    }
                    
                    zout.closeEntry();
                    in.close();
                 }
                 
                 zout.close();
                 //ZIP파일 압축 END
                 
                 //파일다운로드 START
                 response.setContentType("application/zip");
 				 response.setHeader("Set-Cookie", "fileDownload=true; path=/");
                 response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
                 
                 FileInputStream fis = new FileInputStream(tempPath + zipName);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ServletOutputStream so = response.getOutputStream();
                 BufferedOutputStream bos = new BufferedOutputStream(so);
                 
                 int n = 0;
                 while((n = bis.read(buffer)) > 0){
                    bos.write(buffer, 0, n);
                    bos.flush();
                 }
                 
                 if(bos != null) bos.close();
                 if(bis != null) bis.close();
                 if(so != null) so.close();
                 if(fis != null) fis.close();
                 //파일다운로드 END

       			Path filePath = Paths.get(tempPath + zipName);
       			//tempPath폴더에 생성된 zip파일 삭제
       			Files.deleteIfExists(filePath);
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }finally{
                 if (zout != null){
                    zout = null;
                 }
              }
    	}
    }
	/**
	 * 1. 메소드명 : downloadFileRename
	 * 2. 작성일: 2022-03-14
	 * 3. 작성자: 소진희
	 * 4. 설명: 파일 다운로드 (각각) + 리네임 기능 포함
	 * 5. 수정일: 
	 */
    @RequestMapping("/downloadFileRename.do")
    public void downloadFileRename(PrjDocumentIndexVO prjdocumentindexvo,@RequestParam String renameYN,@RequestParam String fileRenameSet,@RequestParam String downloadFiles,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String downloadFilesString = prjdocumentindexvo.getDownloadFiles();
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
    	String[] rfiles = new String[downloadFileEach.length];
    	String[] renamefiles = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			String[] downloadFile = downloadFileEach[i].split("%%");
			prjdocumentindexvo.setDoc_id(downloadFile[0]);
			prjdocumentindexvo.setRev_id(downloadFile[1]);
			PrjDocumentIndexVO getDownloadFileInfo = prjdocumentindexService.getDownloadFileInfo(prjdocumentindexvo);
			if(prjdocumentindexvo.getRenameYN().equals("Y")) {
				String fileRenameSetting = prjdocumentindexvo.getFileRenameSet();
				String title = getDownloadFileInfo.getTitle()==null?"":getDownloadFileInfo.getTitle();
				if(getDownloadFileInfo.getRev_no()==null) {
					fileRenameSetting = fileRenameSetting.replaceAll("%DOCNO%", getDownloadFileInfo.getDoc_no())
						.replaceAll("%REVNO%", "")
						.replaceAll("%TITLE%", title);
				}else {
					fileRenameSetting = fileRenameSetting.replaceAll("%DOCNO%", getDownloadFileInfo.getDoc_no())
						.replaceAll("%REVNO%", getDownloadFileInfo.getRev_no())
						.replaceAll("%TITLE%", title);
				}
				renamefiles[i] = fileRenameSetting.replaceAll("/", "-") + "." + getDownloadFileInfo.getFile_type();
			}
			files[i] = getDownloadFileInfo.getSfile_nm().replaceAll("/", "-");
			rfiles[i] = getDownloadFileInfo.getRfile_nm();
			
		}
    	String tempPath = "";
    	String prj_id = request.getParameter("prj_id");
    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prj_id + DOC_PATH;//"c:/vdsFileupload/";		//파일 저장경로

                 for ( int k=0; k<files.length; k++){
                 //파일다운로드 START
	                 response.setContentType("application/zip");
	                 if(prjdocumentindexvo.getRenameYN().equals("Y")) {
			                //renamefiles[k] = new String(renamefiles[k].getBytes("UTF-8"), "ISO-8859-1");
	                	 	renamefiles[k] = CommonConst.downloadNameEncoding(request, renamefiles[k]);
			                response.addHeader("Content-Disposition", "attachment;filename=" + renamefiles[k]);
	                    }else {
	                    	rfiles[k] = CommonConst.downloadNameEncoding(request, rfiles[k]);
			                response.addHeader("Content-Disposition", "attachment;filename=" + rfiles[k]);
	                    }
	                 
	                 FileInputStream fis = new FileInputStream(tempPath + files[k]);
	                 BufferedInputStream bis = new BufferedInputStream(fis);
	                 ServletOutputStream so = response.getOutputStream();
	                 BufferedOutputStream bos = new BufferedOutputStream(so);
	                 byte[] buffer = new byte[1024];
	                 
	                 int n = 0;
	                 while((n = bis.read(buffer)) > 0){
	                    bos.write(buffer, 0, n);
	                    bos.flush();
	                 }
	                 
	                 if(bos != null) bos.close();
	                 if(bis != null) bis.close();
	                 if(so != null) so.close();
	                 if(fis != null) fis.close();
	                 //파일다운로드 END
                 }
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }
    	}
    }
	/**
	 * 1. 메소드명 : downloadFile
	 * 2. 작성일: 2022-01-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 파일 다운로드 (각각)
	 * 5. 수정일: 
	 */
    @RequestMapping("/downloadFile.do")
    public void downloadFile(@RequestParam String sfile_nm,@RequestParam String rfile_nm,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String downloadFilesString = sfile_nm;
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			files[i] = downloadFileEach[i];
		}
    	String tempPath = "";
    	String prj_id = request.getParameter("prj_id");
    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prj_id + DOC_PATH;//"c:/vdsFileupload/";		//파일 저장경로

                 for ( int k=0; k<files.length; k++){
                 //파일다운로드 START
                	 rfile_nm = rfile_nm.replaceAll("@%@", "&");
	                 response.setContentType("application/zip");
	                 rfile_nm = CommonConst.downloadNameEncoding(request, rfile_nm.replaceAll("/", "-"));
	                 response.addHeader("Content-Disposition", "attachment;filename=" + rfile_nm);
	                 
	                 FileInputStream fis = new FileInputStream(tempPath + files[k]);
	                 BufferedInputStream bis = new BufferedInputStream(fis);
	                 ServletOutputStream so = response.getOutputStream();
	                 BufferedOutputStream bos = new BufferedOutputStream(so);
	                 byte[] buffer = new byte[1024];
	                 
	                 int n = 0;
	                 while((n = bis.read(buffer)) > 0){
	                    bos.write(buffer, 0, n);
	                    bos.flush();
	                 }
	                 
	                 if(bos != null) bos.close();
	                 if(bis != null) bis.close();
	                 if(so != null) so.close();
	                 if(fis != null) fis.close();
	                 //파일다운로드 END
                 }
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }
    	}
    }
	/**
	 * 1. 메소드명 : downloadCommentFile
	 * 2. 작성일: 2022-01-27
	 * 3. 작성자: 소진희
	 * 4. 설명: TR INCOMING COMMENT FILE 실제 파일명으로 다운받기
	 * 5. 수정일: 
	 */
    @RequestMapping("/downloadCommentFile.do")
    public void downloadCommentFile(@RequestParam String sfile_nm,@RequestParam String rfile_nm,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String downloadFilesString = sfile_nm;
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			files[i] = downloadFileEach[i];
		}
    	String tempPath = "";
    	String prj_id = request.getParameter("prj_id");
    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prj_id + DOC_PATH;//"c:/vdsFileupload/";		//파일 저장경로

                 for ( int k=0; k<files.length; k++){
                 //파일다운로드 START
	                 response.setContentType("application/zip");
	                 rfile_nm = CommonConst.downloadNameEncoding(request, rfile_nm);
	                 response.addHeader("Content-Disposition", "attachment;filename=" + rfile_nm);
	                 
	                 FileInputStream fis = new FileInputStream(tempPath + files[k]);
	                 BufferedInputStream bis = new BufferedInputStream(fis);
	                 ServletOutputStream so = response.getOutputStream();
	                 BufferedOutputStream bos = new BufferedOutputStream(so);
	                 byte[] buffer = new byte[1024];
	                 
	                 int n = 0;
	                 while((n = bis.read(buffer)) > 0){
	                    bos.write(buffer, 0, n);
	                    bos.flush();
	                 }
	                 
	                 if(bos != null) bos.close();
	                 if(bis != null) bis.close();
	                 if(so != null) so.close();
	                 if(fis != null) fis.close();
	                 //파일다운로드 END
                 }
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }
    	}
    }
	/**
	 * 1. 메소드명 : downloadFileEmail
	 * 2. 작성일: 2022-01-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 이메일로 보내진 파일 (각각) 다운로드시 다운받은 날짜, 아이피 등을 기록
	 * 5. 수정일: 
	 */
    @RequestMapping("/downloadFileEmail.do")
    public void downloadFileEmail(@RequestParam String sfile_nm,@RequestParam String rfile_nm,@RequestParam String email_type_id,@RequestParam String email_id,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String downloadFilesString = sfile_nm;
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			files[i] = downloadFileEach[i];
		}
    	String tempPath = "";
    	String prj_id = request.getParameter("prj_id");
    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prj_id + DOC_PATH;//"c:/vdsFileupload/";		//파일 저장경로

                 for ( int k=0; k<files.length; k++){
                 //파일다운로드 START
	                 response.setContentType("application/zip");
	                 rfile_nm = CommonConst.downloadNameEncoding(request, rfile_nm);
	                 response.addHeader("Content-Disposition", "attachment;filename=" + rfile_nm);
	                 
	                 FileInputStream fis = new FileInputStream(tempPath + files[k]);
	                 BufferedInputStream bis = new BufferedInputStream(fis);
	                 ServletOutputStream so = response.getOutputStream();
	                 BufferedOutputStream bos = new BufferedOutputStream(so);
	                 byte[] buffer = new byte[1024];
	                 
	                 int n = 0;
	                 while((n = bis.read(buffer)) > 0){
	                    bos.write(buffer, 0, n);
	                    bos.flush();
	                 }
	                 
	                 if(bos != null) bos.close();
	                 if(bis != null) bis.close();
	                 if(so != null) so.close();
	                 if(fis != null) fis.close();
	                 //파일다운로드 END
                 }
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }
    	}


		String down_ip = sessionService.getIpAddress(request);
        
    	//파일 다운로드 이력 저장
    	PrjEmailVO prjemailvo = new PrjEmailVO();
    	String currentDateAndTime = CommonConst.currentDateAndTime();
    	prjemailvo.setPrj_id(prj_id);
    	prjemailvo.setEmail_type_id(email_type_id);
    	prjemailvo.setEmail_id(email_id);
    	PrjEmailVO getEmailFileDownBefore = prjemailService.getEmailFileDownBefore(prjemailvo);
    	if(getEmailFileDownBefore==null || getEmailFileDownBefore.getEmail_receiverd_date()==null) {
        	prjemailvo.setEmail_receiverd_date(currentDateAndTime);
        	prjemailvo.setMod_date(currentDateAndTime);
        	String[] doc_info = sfile_nm.split("_");
        	prjemailvo.setDoc_id(doc_info[0]);
        	prjemailvo.setRev_id(doc_info[1].substring(0,3));
        	prjemailvo.setDown_ip(down_ip);
        	prjemailvo.setDown_date(currentDateAndTime);
        	prjemailService.updatePrjEmailAttachInfo(prjemailvo);
        	prjemailService.updatePrjEmailReceiptInfo(prjemailvo);
        	prjemailService.updatePrjEmailSendInfo(prjemailvo);
    	}
    }
    
	/**
	 * 1. 메소드명 : getPrjDocumentIndexList
	 * 2. 작성일: 2021-12-17
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 프로젝트와 해당 폴더에 등록된 리비전 최상위인 도서 리스트들을 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjDocumentIndexList.do")
	@ResponseBody
	public Object getPrjDocumentIndexList(PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		String select_rev_version = prjdocumentindexvo.getSelect_rev_version();
		if(select_rev_version!=null) {
			if(select_rev_version.equals("current")) {
				List<PrjDocumentIndexVO> getPrjDocumentIndexList = prjdocumentindexService.getPrjDocumentIndexList(prjdocumentindexvo);
				result.add(getPrjDocumentIndexList);
			}else { //select_rev_version.equals("all")
				List<PrjDocumentIndexVO> getPrjDocumentIndexListAll = prjdocumentindexService.getPrjDocumentIndexListAll(prjdocumentindexvo);
				result.add(getPrjDocumentIndexListAll);
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : documentCheckOut
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 문서 checkout(lock걸기)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "documentCheckOut.do")
	@ResponseBody
	public Object documentCheckOut(PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		try {
			int documentCheckOut = prjdocumentindexService.documentCheckOut(prjdocumentindexvo);
			result.add(documentCheckOut);
		}catch(Exception e) {
			System.out.println("documentCheckOut Exception: {}");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : documentCheckOutCancel
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 문서 checkout 해제(lock풀기)
	 * 5. 수정일: 2022-02-14
	 * 			Cancel Check-Out 전에 해당 도서가 DRN 진행 중인지 판단한 후 DRN 진행중이 아닐 경우 Cancel Check-Out 가능
	 */
	@RequestMapping(value = "documentCheckOutCancel.do")
	@ResponseBody
	public Object documentCheckOutCancel(PrjDocumentIndexVO prjdocumentindexvo,PrjDrnInfoVO prjdrninfovo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		PrjDrnInfoVO getUseDocInDRNisnotRorC = prjtrService.getUseDocInDRNisnotRorC(prjdrninfovo);
		try {
			if(getUseDocInDRNisnotRorC!=null) {//DRN 진행중
				result.add(getUseDocInDRNisnotRorC.getDrn_no());
			}else {
				int documentCheckOutCancel = prjdocumentindexService.documentCheckOutCancel(prjdocumentindexvo);
				result.add("체크아웃취소완료");
			}
		}catch(Exception e) {
			System.out.println("documentCheckOutCancel Exception: {}");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : documentDelete
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 문서 삭제(del_yn 값 'Y'로 업데이트) or 해당 리비전만 삭제(del_yn 값 'Y'로 업데이트)
	 * 5. 수정일: 2021-12-24
	 */
	@RequestMapping(value = "documentDelete.do")
	@ResponseBody
	public Object documentDelete(PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());

		int count = 0;
		String[] doc_id_array = prjdocumentindexvo.getDoc_id().split(",");
		String[] rev_id_array = prjdocumentindexvo.getRev_id().split(",");
		for(int i=0;i<doc_id_array.length;i++) {
			prjdocumentindexvo.setDoc_id(doc_id_array[i]);
			prjdocumentindexvo.setRev_id(rev_id_array[i]);
			try {
				int documentDelete = prjdocumentindexService.documentDelete(prjdocumentindexvo);
				count += documentDelete;
			}catch(Exception e) {
				System.out.println("documentDelete Exception: {}");
			}
		}
		result.add(count);
		return result;
	}
	
	/**
	 * 1. 메소드명 : readDocCheck
	 * 2. 작성일: 2022-03-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 유저마다 해당 도서 읽었는지 체크하여 읽지 않은 도서이면 p_prj_document_index_read 테이블에 insert하여 읽었다고 표시`
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "readDocCheck.do")
	@ResponseBody
	public Object readDocCheck(PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		String[] doc = prjdocumentindexvo.getDocid_revid().split("@@");
		for(int i=0;i<doc.length;i++) {
			String[] docrevId = doc[i].split("&&");
			prjdocumentindexvo.setDoc_id(docrevId[0]);
			prjdocumentindexvo.setRev_id(docrevId[1]);
			prjdocumentindexvo.setFolder_id(Long.parseLong(docrevId[2]));
			int isReadDoc = prjdocumentindexService.isReadDoc(prjdocumentindexvo);
			if(isReadDoc==0) {	//읽지 않은 도서이면 read테이블에 추가
				prjdocumentindexService.insertReadDoc(prjdocumentindexvo);
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : getDocumentInfo
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Engineering)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getDocumentInfo.do")
	@ResponseBody
	public Object getDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getDocumentInfo = prjdocumentindexService.getDocumentInfo(prjdocumentindexvo);
		result.add(getDocumentInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getTRDocumentInfo
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Engineering TR,Vendor VTR,Site STR)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTRDocumentInfo.do")
	@ResponseBody
	public Object getTRDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo,PrjTrVO prjtrvo,PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> TRDocumentInfoList = new ArrayList<PrjDocumentIndexVO>();
		String[] doc = prjdocumentindexvo.getPk().split("@@");
		for(int i=0;i<doc.length;i++) {
			String[] docrevId = doc[i].split("&&");
			prjdocumentindexvo.setDoc_id(docrevId[0]);
			prjdocumentindexvo.setRev_id(docrevId[1]);
			PrjDocumentIndexVO getDocumentInfo = new PrjDocumentIndexVO();
			PrjDocumentIndexVO isDuplication = new PrjDocumentIndexVO();
			if(prjtrvo.getProcessType().equals("TR")) {
				getDocumentInfo = prjdocumentindexService.getDocumentInfo(prjdocumentindexvo);
				isDuplication = prjtrService.isDuplicationDoc(prjdocumentindexvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				getDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
				isDuplication = prjtrService.isDuplicationDocVTR(prjdocumentindexvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				getDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
				isDuplication = prjtrService.isDuplicationDocSTR(prjdocumentindexvo);
			}
			if(isDuplication!=null) {
				getDocumentInfo.setDuplicationDoc(isDuplication.getDuplicationDoc());
			}else{
				getDocumentInfo.setDuplicationDoc("N");
			}
			TRDocumentInfoList.add(getDocumentInfo);
		}
		result.add(TRDocumentInfoList);

		if(prjtrvo.getProcessType().equals("TR")) {
			File f = new File(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_tr.xls");
			if(f.exists()) {
			     result.add("파일 존재");
			} else {
			     result.add("파일 없음");           
			}
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			File vf = new File(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_vtr.xls");
			if(vf.exists()) {
			     result.add("파일 존재");
			} else {
			     result.add("파일 없음");           
			}
		}else if(prjtrvo.getProcessType().equals("STR")) {
			File sf = new File(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_str.xls");
			if(sf.exists()) {
			     result.add("파일 존재");
			} else {
			     result.add("파일 없음");           
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : getVendorDocumentInfo
	 * 2. 작성일: 2021-12-27
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Vendor)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getVendorDocumentInfo.do")
	@ResponseBody
	public Object getVendorDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getVendorDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
		result.add(getVendorDocumentInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getVTRDocumentInfo
	 * 2. 작성일: 2022-01-19
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Vendor TR)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getVTRDocumentInfo.do")
	@ResponseBody
	public Object getVTRDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo,PrjTrVO prjtrvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> TRDocumentInfoList = new ArrayList<PrjDocumentIndexVO>();
		String[] doc = prjdocumentindexvo.getPk().split("@@");
		for(int i=0;i<doc.length;i++) {
			String[] docrevId = doc[i].split("&&");
			prjdocumentindexvo.setDoc_id(docrevId[0]);
			prjdocumentindexvo.setRev_id(docrevId[1]);
			PrjDocumentIndexVO getDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
			PrjDocumentIndexVO isDuplication = prjtrService.isDuplicationDocVTR(prjdocumentindexvo);
			if(isDuplication!=null) {
				getDocumentInfo.setDuplicationDoc(isDuplication.getDuplicationDoc());
			}else{
				getDocumentInfo.setDuplicationDoc("N");
			}
			TRDocumentInfoList.add(getDocumentInfo);
		}
		result.add(TRDocumentInfoList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getProcDocumentInfo
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Procurement)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getProcDocumentInfo.do")
	@ResponseBody
	public Object getProcDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getProcDocumentInfo = prjdocumentindexService.getProcDocumentInfo(prjdocumentindexvo);
		result.add(getProcDocumentInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getBulkDocumentInfo
	 * 2. 작성일: 2022-03-25
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Bulk)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getBulkDocumentInfo.do")
	@ResponseBody
	public Object getBulkDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getBulkDocumentInfo = prjdocumentindexService.getBulkDocumentInfo(prjdocumentindexvo);
		result.add(getBulkDocumentInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getSiteDocumentInfo
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Site)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getSiteDocumentInfo.do")
	@ResponseBody
	public Object getSiteDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getSiteDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
		result.add(getSiteDocumentInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getSTRDocumentInfo
	 * 2. 작성일: 2022-01-19
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Site TR)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getSTRDocumentInfo.do")
	@ResponseBody
	public Object getSTRDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo,PrjTrVO prjtrvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> TRDocumentInfoList = new ArrayList<PrjDocumentIndexVO>();
		String[] doc = prjdocumentindexvo.getPk().split("@@");
		for(int i=0;i<doc.length;i++) {
			String[] docrevId = doc[i].split("&&");
			prjdocumentindexvo.setDoc_id(docrevId[0]);
			prjdocumentindexvo.setRev_id(docrevId[1]);
			PrjDocumentIndexVO getDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
			PrjDocumentIndexVO isDuplication = prjtrService.isDuplicationDocSTR(prjdocumentindexvo);
			if(isDuplication!=null) {
				getDocumentInfo.setDuplicationDoc(isDuplication.getDuplicationDoc());
			}else{
				getDocumentInfo.setDuplicationDoc("N");
			}
			TRDocumentInfoList.add(getDocumentInfo);
		}
		result.add(TRDocumentInfoList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCorrDocumentInfo
	 * 2. 작성일: 2022-01-02
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (Correspondence)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getCorrDocumentInfo.do")
	@ResponseBody
	public Object getCorrDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo,PrjEmailVO prjemailvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getCorrDocumentInfo = prjdocumentindexService.getCorrDocumentInfo(prjdocumentindexvo);
		result.add(getCorrDocumentInfo);
		prjemailvo.setUser_data00(getCorrDocumentInfo.getSys_corr_no());
		int getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
		result.add(getEmailSentTimes);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCorrHistory
	 * 2. 작성일: 2022-03-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 Correspondence 도서의 회신,수신 이력을 가져옴
	 * 			회신: Outgoing 이메일 보냈을 경우,
	 * 			수신: Incoming save했을 경우
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getCorrHistory.do")
	@ResponseBody
	public Object getCorrHistoryList(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> getCorrHistory = prjdocumentindexService.getCorrHistory(prjdocumentindexvo);
		result.add(getCorrHistory);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getEmailSendingHistory
	 * 2. 작성일: 2022-02-07
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 tr_no 또는 corr_no 문서가 이메일로 보내진 기록을 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getEmailSendingHistory.do")
	@ResponseBody
	public Object getEmailSendingHistory(PrjDocumentIndexVO prjdocumentindexvo,PrjEmailVO prjemailvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjEmailVO> getEmailSentHistory = prjemailService.getEmailSentHistory(prjemailvo);
		result.add(getEmailSentHistory);
		return result;
	}
	
	/**
	 * 1. 메소드명 : changeDocNo
	 * 2. 작성일: 2022-04-13
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트에 Doc No로 조회하여 Doc No 변경 + Vendor po_doc_no 데이터도 있을 경우 함께 변경
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "changeDocNo.do")
	@ResponseBody
	public Object changeDocNo(HttpServletRequest request,PrjDocumentIndexVO prjdocumentindexvo,PrjDocTrNoChgHistVO prjdoctrnochghistvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> isExistsDocNo = prjdocumentindexService.isExistsDocNo(prjdocumentindexvo);
		if(isExistsDocNo!=null && isExistsDocNo.size()>0) {//변경할 DOC NO가 이미 있는 경우
			result.add(prjdocumentindexvo.getChange_doc_no()+" is already exists.");
		}else {
			int changeDocNo = prjdocumentindexService.changeDocNo(prjdocumentindexvo);
			int changeVendorPoDocNo = prjdocumentindexService.changeVendorPoDocNo(prjdocumentindexvo);
			if(changeDocNo>0) {//변경한 경우
				//prjdocumentindexvo.setDoc_no(prjdocumentindexvo.getOld_doc_no());
				prjdoctrnochghistvo.setLog_date(CommonConst.currentDateAndTime());
				prjdoctrnochghistvo.setPrj_id(prjdocumentindexvo.getPrj_id());
				prjdoctrnochghistvo.setOld_doc_no(prjdocumentindexvo.getOld_doc_no());
				prjdoctrnochghistvo.setChg_doc_no(prjdocumentindexvo.getChange_doc_no());
				HttpSession session = request.getSession();
				UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
				prjdoctrnochghistvo.setReg_id(sessioninfo.getUser_id());
				String remote_ip = CommonConst.getIpAddress(request);
				prjdoctrnochghistvo.setRemote_ip(remote_ip);
				prjdocumentindexService.insertDocNoChgHist(prjdoctrnochghistvo);
				result.add("DOC NO has Changed");
			}else {//old_doc_no에 해당하는 doc_no가 없어서 변경 못한 경우
				result.add(prjdocumentindexvo.getOld_doc_no()+" is not exists.");
			}
		}
		return result;
	}
	/**
	 * 1. 메소드명 : getDocNoChgHist
	 * 2. 작성일: 2022-04-13
	 * 3. 작성자: 소진희
	 * 4. 설명: DOC NO 변경된 내역 보기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getDocNoChgHist.do")
	@ResponseBody
	public Object getDocNoChgHist(HttpServletRequest request,PrjTrVO prjtrvo,PrjDocTrNoChgHistVO prjdoctrnochghistvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocTrNoChgHistVO> getDocNoChgHist = prjdocumentindexService.getDocNoChgHist(prjdoctrnochghistvo);
		result.add(getDocNoChgHist);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getGeneralDocumentInfo
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 도서의 정보를 가져옴 (일반문서)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getGeneralDocumentInfo.do")
	@ResponseBody
	public Object getGeneralDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjDocumentIndexVO getGeneralDocumentInfo = prjdocumentindexService.getGeneralDocumentInfo(prjdocumentindexvo);
		result.add(getGeneralDocumentInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : changeDesigner
	 * 2. 작성일: 2022-03-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트 도서의 디자이너 또는 (Correspondece의 경우) 책임자를 인수인계함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "changeDesigner.do")
	@ResponseBody
	public Object changeDesigner(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		String[] prjIdArray = prjdocumentindexvo.getPrj_id().split(",");
		try {
			for(int i=0;i<prjIdArray.length;i++) {
				prjdocumentindexvo.setPrj_id(prjIdArray[i]);
				prjdocumentindexService.changeDesignerEng(prjdocumentindexvo);
				prjdocumentindexService.changeDesignerVendor(prjdocumentindexvo);
				prjdocumentindexService.changeDesignerProc(prjdocumentindexvo);
				prjdocumentindexService.changeDesignerBulk(prjdocumentindexvo);
				prjdocumentindexService.changeDesignerSite(prjdocumentindexvo);
				prjdocumentindexService.changeDesignerCorr(prjdocumentindexvo);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("changeDesigner Exception: {}");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : docSaveChkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 2022-02-19 소진희 file Attach를 할 때만 체크하도록 수정
	 */
	@RequestMapping(value = "docSaveChkFile.do")
	@ResponseBody
	public int docSaveChkFile(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjDocumentIndexVO) throws Exception{
		int chkFile = 100;
		if(prjDocumentIndexVO.getFile_value()!=null) {
			if(!prjDocumentIndexVO.getFile_value().equals("")) {	//file Attach 할 경우
				//List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("uploadfile");
				
				MultipartFile mf = mtRequest.getFile("updateRevision_File");
				File file = new File(mf.getOriginalFilename());
				// true면 등록 불가 false면 등록 가능한 확장자
//				boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
				// true면 등록 가능 false면 등록 불가능한 확장자
				boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
					
				if(fileUploadChk == true) {
					chkFile = 100;
				}else if(fileUploadChk == false) {
					chkFile = 0;
				}
			}
		}
		return chkFile;
	}
	
	/**
	 * 1. 메소드명 : engDocSave
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 소진희
	 * 4. 설명: ENGINEERING 도서 목록 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "engDocSave.do", method = RequestMethod.POST)
	@ResponseBody
	public Object engDocSave(MultipartHttpServletRequest mtRequest,PrjDrnInfoVO prjdrninfovo,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,PrjMemberVO prjmembervo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		
		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateDocNo = prjdocumentindexService.isDuplicateDocNo(prjdocumentindexvo);
			if(isDuplicateDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertEngInfo = prjdocumentindexService.insertEngInfo(prjdocumentindexvo);
				result.add(insertEngInfo);
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					//새로 추가하는 도서의 경우 fore_date 입력안되어있으면 fore_date = plan_date
					if(fore_date.equals("") || fore_date.equals("--")) {
						fore_date = plan_date;
					}
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updatePrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertPrjStep(prjstepvo);
					}
				}
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("insertDocumentIndex,insertEngInfo Exception: {}");
			}
		}else {//update일 때 revision을 올리지 않고 정보만 수정할 경우 
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateDocNoChange = prjdocumentindexService.isDuplicateDocNoChange(prjdocumentindexvo);
			if(isDuplicateDocNoChange>0) {
				//리비전이 없을 때만 DOC NO를 변경할 수 있기 때문에 리비전 없을 때만 체크
				result.add("DOC NO 중복");
				return result;
			}
			try {
				if(prjdocumentindexvo.getFile_value().equals("")) {	//file Attach 안 할 경우
					int updateDocumentIndex = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				}else {	//file Attach 할 경우
					String ADMINANDDCCString = getADMINAndDCCString(prjmembervo);
					String[] ADMINANDDCCArray = ADMINANDDCCString.split(",");

					int DRNisReject = prjdocumentindexService.DRNisReject(prjdocumentindexvo);
					if(!Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) && DRNisReject>0) {//DRN 반려된 도서의 경우 일반사용자는 리비전을 올리지 않고 파일수정 불가
						result.add("DRN 반려 파일 수정 불가");
					}else {//반려된 도서일지라도 ADMIN이나 DCC는 리비전올리지 않고 파일 첨부 수정가능
						PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
						if(Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) || getUseDocInDRN==null){	//ADMIN이나 DCC일 경우에만 리비전올리지않고 파일 수정가능, 또한 DRN 태우기 전까지는 일반사용자도 권한을 가지고 있으면 리비전올리지않고 파일 수정가능
							int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
						
							//파일 업로드
							MultipartFile file = mtRequest.getFile("updateRevision_File");
							String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
							File dir = new File(uploadPath);
							dir.mkdirs();
							File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+prjdocumentindexvo.getRev_id()+"."+prjdocumentindexvo.getFile_type());
							try {
								// 업로드 경로에 파일 업로드
								file.transferTo(uploadTarget);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							result.add("파일 수정 불가");
						}
					}
				}
				int updateEngInfo = prjdocumentindexService.updateEngInfo(prjdocumentindexvo);
				result.add(updateEngInfo);
				prjstepvo.setDoc_id(prjdocumentindexvo.getDoc_id());
				prjstepvo.setRev_id(prjdocumentindexvo.getRev_id());
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updatePrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertPrjStep(prjstepvo);
					}
				}
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateEngInfo Exception: {}");
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateRevision
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서의 revision no 업데이트 (Engineering)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateRevision.do")
	@ResponseBody
	public Object updateRevision(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setAttach_file_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		try {
			String rev_id = prjdocumentindexService.updateRevisionOfDocumentIndex(prjdocumentindexvo);
			prjdocumentindexvo.setRev_id(rev_id);
			prjstepvo.setRev_id(rev_id);

			//파일 업로드
			MultipartFile file = mtRequest.getFile("updateRevision_File");
			String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+rev_id+"."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int insertEngInfoForUpdateRevision = prjdocumentindexService.insertEngInfoForUpdateRevision(prjdocumentindexvo);
			result.add(insertEngInfoForUpdateRevision);
			List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
			List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
			List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
			List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
			for(int i=0;i<step_code_id_array.size();i++) {
				String step_code_id = step_code_id_array.get(i);
				prjstepvo.setStep_code_id(step_code_id);
				String plan_date = plan_date_array.get(i);
				String actual_date = actual_date_array.get(i);
				String fore_date = fore_date_array.get(i);
				prjstepvo.setPlan_date(plan_date);
				prjstepvo.setActual_date(actual_date);
				prjstepvo.setFore_date(fore_date);
				prjstepService.insertPrjStepForUpdateRevision(prjstepvo);
			}

			if(prjdocumentindexvo.getOrigin_rev_code_id().equals("")) {
				//최초 리비전일 경우 Actual Date 저장
				prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
				prjstepService.insertStartActualDateByFirstRevision(prjstepvo);
				//ForecastDate 업데이트
				prjstepvo.setSet_val2("DOC. CHECK IN시");
				List<PrjCodeSettingsVO> getUpdateEngStepCodeId = prjstepService.getUpdateEngStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateEngStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateEngStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateEngStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
					if(!getForeDateRuleByEngStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByEngStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
						PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
						if(getActualDateEngStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
			
			result.add(rev_id);
		}catch(Exception e) {
			System.out.println("updateRevision Exception: {}");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : vendorDocSave
	 * 2. 작성일: 2021-12-27
	 * 3. 작성자: 소진희
	 * 4. 설명: VENDOR 도서 목록 추가
	 * 5. 수정일: 2022-01-26
	 */
	@RequestMapping(value = "vendorDocSave.do")
	@ResponseBody
	public Object vendorDocSave(MultipartHttpServletRequest mtRequest,PrjDrnInfoVO prjdrninfovo,PrjDocumentIndexVO prjdocumentindexvo,PrjMemberVO prjmembervo,PrjStepVO prjstepvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateDocNo = prjdocumentindexService.isDuplicateDocNo(prjdocumentindexvo);
			if(isDuplicateDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertVendorInfo = prjdocumentindexService.insertVendorInfo(prjdocumentindexvo);
				result.add(insertVendorInfo);
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getVendorStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					//새로 추가하는 도서의 경우 fore_date 입력안되어있으면 fore_date = plan_date
					if(fore_date.equals("") || fore_date.equals("--")) {
						fore_date = plan_date;
					}
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateVendorPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertVendorPrjStep(prjstepvo);
					}
				}
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				System.out.println("insertDocumentIndex,insertVendorInfo Exception: {}");
			}
		}else {//update일 때 revision을 올리지 않고 정보만 수정할 경우
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateDocNoChange = prjdocumentindexService.isDuplicateDocNoChange(prjdocumentindexvo);
			if(isDuplicateDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				if(prjdocumentindexvo.getFile_value().equals("")) {	//file Attach 안 할 경우
					int updateDocumentIndex = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				}else {	//file Attach 할 경우
					String ADMINANDDCCString = getADMINAndDCCString(prjmembervo);
					String[] ADMINANDDCCArray = ADMINANDDCCString.split(",");

					int DRNisReject = prjdocumentindexService.DRNisReject(prjdocumentindexvo);
					if(!Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) && DRNisReject>0) {//DRN 반려된 도서의 경우 일반사용자는 리비전을 올리지 않고 파일수정 불가
						result.add("DRN 반려 파일 수정 불가");
					}else {//반려된 도서일지라도 ADMIN이나 DCC는 리비전올리지 않고 파일 첨부 수정가능
						PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
						if(Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) || getUseDocInDRN==null){	//ADMIN이나 DCC일 경우에만 리비전올리지않고 파일 수정가능, 또한 DRN 태우기 전까지는 일반사용자도 권한을 가지고 있으면 리비전올리지않고 파일 수정가능
							int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
							
							//파일 업로드
							MultipartFile file = mtRequest.getFile("updateRevision_File");
							String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
							File dir = new File(uploadPath);
							dir.mkdirs();
							File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+prjdocumentindexvo.getRev_id()+"."+prjdocumentindexvo.getFile_type());
							try {
								// 업로드 경로에 파일 업로드
								file.transferTo(uploadTarget);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else {
							result.add("파일 수정 불가");
						}
					}
				}
				int updateEngInfo = prjdocumentindexService.updateVendorInfo(prjdocumentindexvo);
				result.add(updateEngInfo);
				prjstepvo.setDoc_id(prjdocumentindexvo.getDoc_id());
				prjstepvo.setRev_id(prjdocumentindexvo.getRev_id());
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getVendorStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateVendorPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertVendorPrjStep(prjstepvo);
					}
				}
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateVendorInfo Exception: {}");
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateRevisionVendor
	 * 2. 작성일: 2021-12-27
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서의 revision no 업데이트 (Vendor)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateRevisionVendor.do")
	@ResponseBody
	public Object updateRevisionVendor(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,PrjCodeSettingsVO prjcodesettingsvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setAttach_file_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		try {
			String rev_id = prjdocumentindexService.updateRevisionOfDocumentIndex(prjdocumentindexvo);
			prjdocumentindexvo.setRev_id(rev_id);
			prjstepvo.setRev_id(rev_id);
			

			//파일 업로드
			MultipartFile file = mtRequest.getFile("updateRevision_File");
			String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+rev_id+"."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int insertVendorInfoForUpdateRevision = prjdocumentindexService.insertVendorInfoForUpdateRevision(prjdocumentindexvo);
			result.add(insertVendorInfoForUpdateRevision);
			List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
			List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
			List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
			List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
			for(int i=0;i<step_code_id_array.size();i++) {
				String step_code_id = step_code_id_array.get(i);
				prjstepvo.setStep_code_id(step_code_id);
				String plan_date = plan_date_array.get(i);
				String actual_date = actual_date_array.get(i);
				String fore_date = fore_date_array.get(i);
				prjstepvo.setPlan_date(plan_date);
				prjstepvo.setActual_date(actual_date);
				prjstepvo.setFore_date(fore_date);
				prjstepService.insertVendorPrjStepForUpdateRevision(prjstepvo);
			}

			if(prjdocumentindexvo.getOrigin_rev_code_id().equals("")) {
				//최초 리비전일 경우 Start Actual Date 저장
				prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
				prjstepService.insertVendorStartActualDateByFirstRevision(prjstepvo);
				//ForecastDate 업데이트
				prjstepvo.setSet_val2("VENDOR DOC. CHECK IN시");
				List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = prjstepService.getUpdateVendorStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateVendorStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateVendorStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateVendorStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
					if(!getForeDateRuleByVendorStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
						PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
						if(getActualDateVendorStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
			result.add(rev_id);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("updateRevisionVendor Exception: {}");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : procDocSave
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: PROCUREMENT 도서 목록 추가
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "procDocSave.do")
	@ResponseBody
	public Object procDocSave(MultipartHttpServletRequest mtRequest,PrjDrnInfoVO prjdrninfovo,PrjMemberVO prjmembervo,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
			if(isDuplicateOtherDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertProcInfo = prjdocumentindexService.insertProcInfo(prjdocumentindexvo);
				result.add(insertProcInfo);
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getProcStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					//새로 추가하는 도서의 경우 fore_date 입력안되어있으면 fore_date = plan_date
					if(fore_date.equals("") || fore_date.equals("--")) {
						fore_date = plan_date;
					}
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateProcPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertProcPrjStep(prjstepvo);
					}
				}
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				System.out.println("insertDocumentIndex,insertProcInfo Exception: {}");
			}
		}else {//update일 때 revision을 올리지 않고 정보만 수정할 경우
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNoChange = prjdocumentindexService.isDuplicateOtherDocNoChange(prjdocumentindexvo);
			if(isDuplicateOtherDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				if(prjdocumentindexvo.getFile_value().equals("")) {	//file Attach 안 할 경우
					int updateDocumentIndex = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				}else {	//file Attach 할 경우
					PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
					String ADMINANDDCCString = getADMINAndDCCString(prjmembervo);
					String[] ADMINANDDCCArray = ADMINANDDCCString.split(",");
					if(Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) || getUseDocInDRN==null){	//ADMIN이나 DCC일 경우에만 리비전올리지않고 파일 수정가능, 또한 DRN 태우기 전까지는 일반사용자도 권한을 가지고 있으면 리비전올리지않고 파일 수정가능
						int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
						
						//파일 업로드
						MultipartFile file = mtRequest.getFile("updateRevision_File");
						String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
						File dir = new File(uploadPath);
						dir.mkdirs();
						File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+prjdocumentindexvo.getRev_id()+"."+prjdocumentindexvo.getFile_type());
						try {
							// 업로드 경로에 파일 업로드
							file.transferTo(uploadTarget);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						result.add("파일 수정 불가");
					}
				}
				int updateProcInfo = prjdocumentindexService.updateProcInfo(prjdocumentindexvo);
				result.add(updateProcInfo);
				prjstepvo.setDoc_id(prjdocumentindexvo.getDoc_id());
				prjstepvo.setRev_id(prjdocumentindexvo.getRev_id());
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getProcStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateProcPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertProcPrjStep(prjstepvo);
					}
				}
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateProcInfo Exception: {}");
			}
		}

		if(!prjdocumentindexvo.getPor_no().equals("")) {
			//POR 발행시 Actual Date 저장
			prjstepvo.setSet_val2("POR 발행시");
			prjstepvo.setActual_date(prjdocumentindexvo.getPor_issue_date());
			int updateProcActualDateBySetPORorPO = prjstepService.updateProcActualDateBySetPORorPO(prjstepvo);
			//ForecastDate 업데이트
			if(updateProcActualDateBySetPORorPO>0) {	//Actual Date가 업데이트 되었을 경우 해당 스텝의 Forecast Date Rule도 보고 자동 적용시킴
				List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
					if(!getForeDateRuleByProcStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByProcStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
						PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
						if(getActualDateProcStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
		if(!prjdocumentindexvo.getPo_no().equals("")) {
			//PO 발행시 Actual Date 저장
			prjstepvo.setSet_val2("PO 발행시");
			prjstepvo.setActual_date(prjdocumentindexvo.getPo_issue_date());
			int updateProcActualDateBySetPORorPO = prjstepService.updateProcActualDateBySetPORorPO(prjstepvo);
			if(updateProcActualDateBySetPORorPO>0) {
				//ForecastDate 업데이트
				List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
					if(!getForeDateRuleByProcStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByProcStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
						PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
						if(getActualDateProcStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateRevisionProc
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서의 revision no 업데이트 (Procurement)
	 * 5. 수정일: 2022-03-26 Procurement 도서의 경우 도서를 새로 등록할 때부터 파일 첨부가 가능한데, 이때 리비전을 반드시 올려야 함
	 * 			따라서, updateRevisionProc.do로 들어와도 단순 리비전업데이트가 아닌 새 도서등록일 경우의 처리를 해줘야 함
	 */
	@RequestMapping(value = "updateRevisionProc.do")
	@ResponseBody
	public Object updateRevisionProc(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,PrjCodeSettingsVO prjcodesettingsvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setAttach_file_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		try {
			if(prjdocumentindexvo.getPopup_new_or_update().equals("new")){
				//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
				//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
				int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
				if(isDuplicateOtherDocNo>0) {
					result.add("DOC NO 중복");
					return result;
				}
				
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertProcInfo = prjdocumentindexService.insertProcInfo(prjdocumentindexvo);
				result.add(insertProcInfo);
				result.add("001");
				result.add(doc_id);

				//파일 업로드
				prjdocumentindexvo.setSfile_nm(doc_id+"_001."+prjdocumentindexvo.getFile_type());
				prjdocumentindexvo.setRev_id("001");
				int updateDocumentIndexFileAttachProcAndBulk = prjdocumentindexService.updateDocumentIndexFileAttachProcAndBulk(prjdocumentindexvo);
				MultipartFile file = mtRequest.getFile("updateRevision_File");
				String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_001."+prjdocumentindexvo.getFile_type());
				try {
					// 업로드 경로에 파일 업로드
					file.transferTo(uploadTarget);
				} catch (Exception e) {
					e.printStackTrace();
				}
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getProcStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateProcPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertProcPrjStep(prjstepvo);
					}
				}
			}else {
				String rev_id = prjdocumentindexService.updateRevisionOfDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setRev_id(rev_id);
				prjstepvo.setRev_id(rev_id);
	
				System.out.println("rev_id:"+rev_id);
				//파일 업로드
				MultipartFile file = mtRequest.getFile("updateRevision_File");
				String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+rev_id+"."+prjdocumentindexvo.getFile_type());
				try {
					// 업로드 경로에 파일 업로드
					file.transferTo(uploadTarget);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int insertProcInfoForUpdateRevision = prjdocumentindexService.insertProcInfoForUpdateRevision(prjdocumentindexvo);
				result.add(insertProcInfoForUpdateRevision);
				result.add(rev_id);

				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					prjstepService.insertProcPrjStepForUpdateRevision(prjstepvo);
				}
			}

			if(prjdocumentindexvo.getOrigin_rev_code_id().equals("")) {
				//최초 리비전일 경우 Start Actual Date 저장
				prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
				prjstepService.insertProcStartActualDateByFirstRevision(prjstepvo);
				//ForecastDate 업데이트
				prjstepvo.setSet_val2("PROCUREMENT DOC. CHECK IN시");
				List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
					if(!getForeDateRuleByProcStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByProcStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
						PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
						if(getActualDateProcStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}

			if(!prjdocumentindexvo.getPor_no().equals("")) {
				//POR 발행시 Actual Date 저장
				prjstepvo.setSet_val2("POR 발행시");
				prjstepvo.setActual_date(prjdocumentindexvo.getPor_issue_date());
				int updateProcActualDateBySetPORorPO = prjstepService.updateProcActualDateBySetPORorPO(prjstepvo);
				//ForecastDate 업데이트
				if(updateProcActualDateBySetPORorPO>0) {	//Actual Date가 업데이트 되었을 경우 해당 스텝의 Forecast Date Rule도 보고 자동 적용시킴
					List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
						if(!getForeDateRuleByProcStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByProcStep.getSet_val4())!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
							PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
							if(getActualDateProcStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				}
			}
			if(!prjdocumentindexvo.getPo_no().equals("")) {
				//PO 발행시 Actual Date 저장
				prjstepvo.setSet_val2("PO 발행시");
				prjstepvo.setActual_date(prjdocumentindexvo.getPo_issue_date());
				int updateProcActualDateBySetPORorPO = prjstepService.updateProcActualDateBySetPORorPO(prjstepvo);
				if(updateProcActualDateBySetPORorPO>0) {
					//ForecastDate 업데이트
					List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
						if(!getForeDateRuleByProcStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByProcStep.getSet_val4())!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
							PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
							if(getActualDateProcStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				}
			}
		}catch(Exception e) {
			System.out.println("updateRevisionProc Exception: {}");
		}
		return result;
	}

	
	/**
	 * 1. 메소드명 : bulkDocSave
	 * 2. 작성일: 2022-03-25
	 * 3. 작성자: 소진희
	 * 4. 설명: BULK 도서 목록 추가
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "bulkDocSave.do")
	@ResponseBody
	public Object bulkDocSave(MultipartHttpServletRequest mtRequest,PrjDrnInfoVO prjdrninfovo,PrjMemberVO prjmembervo,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
			if(isDuplicateOtherDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertBulkInfo = prjdocumentindexService.insertBulkInfo(prjdocumentindexvo);
				result.add(insertBulkInfo);
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getBulkStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					//새로 추가하는 도서의 경우 fore_date 입력안되어있으면 fore_date = plan_date
					if(fore_date.equals("") || fore_date.equals("--")) {
						fore_date = plan_date;
					}
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateBulkPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertBulkPrjStep(prjstepvo);
					}
				}
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				System.out.println("insertDocumentIndex,insertBulkInfo Exception: {}");
			}
		}else {//update일 때 revision을 올리지 않고 정보만 수정할 경우
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNoChange = prjdocumentindexService.isDuplicateOtherDocNoChange(prjdocumentindexvo);
			if(isDuplicateOtherDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				if(prjdocumentindexvo.getFile_value().equals("")) {	//file Attach 안 할 경우
					int updateDocumentIndex = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				}else {	//file Attach 할 경우
					PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
					String ADMINANDDCCString = getADMINAndDCCString(prjmembervo);
					String[] ADMINANDDCCArray = ADMINANDDCCString.split(",");
					if(Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) || getUseDocInDRN==null){	//ADMIN이나 DCC일 경우에만 리비전올리지않고 파일 수정가능, 또한 DRN 태우기 전까지는 일반사용자도 권한을 가지고 있으면 리비전올리지않고 파일 수정가능
						int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
						
						//파일 업로드
						MultipartFile file = mtRequest.getFile("updateRevision_File");
						String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
						File dir = new File(uploadPath);
						dir.mkdirs();
						File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+prjdocumentindexvo.getRev_id()+"."+prjdocumentindexvo.getFile_type());
						try {
							// 업로드 경로에 파일 업로드
							file.transferTo(uploadTarget);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						result.add("파일 수정 불가");
					}
				}
				int updateBulkInfo = prjdocumentindexService.updateBulkInfo(prjdocumentindexvo);
				result.add(updateBulkInfo);
				prjstepvo.setDoc_id(prjdocumentindexvo.getDoc_id());
				prjstepvo.setRev_id(prjdocumentindexvo.getRev_id());
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getBulkStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateBulkPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertBulkPrjStep(prjstepvo);
					}
				}
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateBulkInfo Exception: {}");
			}
		}

		if(!prjdocumentindexvo.getPor_no().equals("")) {
			//POR 발행시 Actual Date 저장
			prjstepvo.setSet_val2("POR 발행시");
			prjstepvo.setActual_date(prjdocumentindexvo.getPor_issue_date());
			int updateBulkActualDateBySetPORorPO = prjstepService.updateBulkActualDateBySetPORorPO(prjstepvo);
			//ForecastDate 업데이트
			if(updateBulkActualDateBySetPORorPO>0) {	//Actual Date가 업데이트 되었을 경우 해당 스텝의 Forecast Date Rule도 보고 자동 적용시킴
				List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
					if(!getForeDateRuleByBulkStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
						PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
						if(getActualDateBulkStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
		if(!prjdocumentindexvo.getPo_no().equals("")) {
			//PO 발행시 Actual Date 저장
			prjstepvo.setSet_val2("PO 발행시");
			prjstepvo.setActual_date(prjdocumentindexvo.getPo_issue_date());
			int updateBulkActualDateBySetPORorPO = prjstepService.updateBulkActualDateBySetPORorPO(prjstepvo);
			if(updateBulkActualDateBySetPORorPO>0) {
				//ForecastDate 업데이트
				List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
					if(!getForeDateRuleByBulkStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
						PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
						if(getActualDateBulkStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateRevisionBulk
	 * 2. 작성일: 2022-03-25
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서의 revision no 업데이트 (Bulk)
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "updateRevisionBulk.do")
	@ResponseBody
	public Object updateRevisionBulk(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,PrjCodeSettingsVO prjcodesettingsvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setAttach_file_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		try {
			if(prjdocumentindexvo.getPopup_new_or_update().equals("new")){
				//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
				//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
				int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
				if(isDuplicateOtherDocNo>0) {
					result.add("DOC NO 중복");
					return result;
				}
				
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertBulkInfo = prjdocumentindexService.insertBulkInfo(prjdocumentindexvo);
				result.add(insertBulkInfo);
				result.add("001");
				result.add(doc_id);

				//파일 업로드
				prjdocumentindexvo.setSfile_nm(doc_id+"_001."+prjdocumentindexvo.getFile_type());
				prjdocumentindexvo.setRev_id("001");
				int updateDocumentIndexFileAttachProcAndBulk = prjdocumentindexService.updateDocumentIndexFileAttachProcAndBulk(prjdocumentindexvo);
				MultipartFile file = mtRequest.getFile("updateRevision_File");
				String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_001."+prjdocumentindexvo.getFile_type());
				try {
					// 업로드 경로에 파일 업로드
					file.transferTo(uploadTarget);
				} catch (Exception e) {
					e.printStackTrace();
				}
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getBulkStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateBulkPrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertBulkPrjStep(prjstepvo);
					}
				}
			}else {
				String rev_id = prjdocumentindexService.updateRevisionOfDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setRev_id(rev_id);
				prjstepvo.setRev_id(rev_id);
	
				System.out.println("rev_id:"+rev_id);
				//파일 업로드
				MultipartFile file = mtRequest.getFile("updateRevision_File");
				String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+rev_id+"."+prjdocumentindexvo.getFile_type());
				try {
					// 업로드 경로에 파일 업로드
					file.transferTo(uploadTarget);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int insertBulkInfoForUpdateRevision = prjdocumentindexService.insertBulkInfoForUpdateRevision(prjdocumentindexvo);
				result.add(insertBulkInfoForUpdateRevision);
				result.add(rev_id);
				
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					prjstepService.insertBulkPrjStepForUpdateRevision(prjstepvo);
				}
			}

			if(prjdocumentindexvo.getOrigin_rev_code_id().equals("")) {
				//최초 리비전일 경우 Start Actual Date 저장
				prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
				prjstepService.insertBulkStartActualDateByFirstRevision(prjstepvo);
				//ForecastDate 업데이트
				prjstepvo.setSet_val2("BULK DOC. CHECK IN시");
				List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
					if(!getForeDateRuleByBulkStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
						PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
						if(getActualDateBulkStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}catch(Exception e) {
			System.out.println("updateRevisionBulk Exception: {}");
		}
		if(!prjdocumentindexvo.getPor_no().equals("")) {
			//POR 발행시 Actual Date 저장
			prjstepvo.setSet_val2("POR 발행시");
			prjstepvo.setActual_date(prjdocumentindexvo.getPor_issue_date());
			int updateBulkActualDateBySetPORorPO = prjstepService.updateBulkActualDateBySetPORorPO(prjstepvo);
			//ForecastDate 업데이트
			if(updateBulkActualDateBySetPORorPO>0) {	//Actual Date가 업데이트 되었을 경우 해당 스텝의 Forecast Date Rule도 보고 자동 적용시킴
				List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
					if(!getForeDateRuleByBulkStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
						PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
						if(getActualDateBulkStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
		if(!prjdocumentindexvo.getPo_no().equals("")) {
			//PO 발행시 Actual Date 저장
			prjstepvo.setSet_val2("PO 발행시");
			prjstepvo.setActual_date(prjdocumentindexvo.getPo_issue_date());
			int updateBulkActualDateBySetPORorPO = prjstepService.updateBulkActualDateBySetPORorPO(prjstepvo);
			if(updateBulkActualDateBySetPORorPO>0) {
				//ForecastDate 업데이트
				List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
					if(!getForeDateRuleByBulkStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
						PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
						if(getActualDateBulkStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : siteDocSave
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: SITE 도서 목록 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "siteDocSave.do")
	@ResponseBody
	public Object siteDocSave(MultipartHttpServletRequest mtRequest,PrjDrnInfoVO prjdrninfovo,PrjMemberVO prjmembervo,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			int isDuplicateDocNo = prjdocumentindexService.isDuplicateDocNo(prjdocumentindexvo);
			if(isDuplicateDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertSiteInfo = prjdocumentindexService.insertSiteInfo(prjdocumentindexvo);
				result.add(insertSiteInfo);
				prjstepvo.setDoc_id(doc_id);
				prjstepvo.setRev_id("001");
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getSiteStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					//새로 추가하는 도서의 경우 fore_date 입력안되어있으면 fore_date = plan_date
					if(fore_date.equals("") || fore_date.equals("--")) {
						fore_date = plan_date;
					}
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateSitePrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertSitePrjStep(prjstepvo);
					}
				}
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				System.out.println("insertDocumentIndex,insertSiteInfo Exception: {}");
			}
		}else {//update일 때 revision을 올리지 않고 정보만 수정할 경우
			int isDuplicateDocNoChange = prjdocumentindexService.isDuplicateDocNoChange(prjdocumentindexvo);
			if(isDuplicateDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				if(prjdocumentindexvo.getFile_value().equals("")) {	//file Attach 안 할 경우
					int updateDocumentIndex = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				}else {	//file Attach 할 경우
					String ADMINANDDCCString = getADMINAndDCCString(prjmembervo);
					String[] ADMINANDDCCArray = ADMINANDDCCString.split(",");

					int DRNisReject = prjdocumentindexService.DRNisReject(prjdocumentindexvo);
					if(!Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) && DRNisReject>0) {//DRN 반려된 도서의 경우 일반사용자는 리비전을 올리지 않고 파일수정 불가
						result.add("DRN 반려 파일 수정 불가");
					}else {//반려된 도서일지라도 ADMIN이나 DCC는 리비전올리지 않고 파일 첨부 수정가능
						PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
						if(Arrays.asList(ADMINANDDCCArray).contains(sessioninfo.getUser_id()) || getUseDocInDRN==null){	//ADMIN이나 DCC일 경우에만 리비전올리지않고 파일 수정가능, 또한 DRN 태우기 전까지는 일반사용자도 권한을 가지고 있으면 리비전올리지않고 파일 수정가능
							int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
							
							//파일 업로드
							MultipartFile file = mtRequest.getFile("updateRevision_File");
							String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
							File dir = new File(uploadPath);
							dir.mkdirs();
							File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+prjdocumentindexvo.getRev_id()+"."+prjdocumentindexvo.getFile_type());
							try {
								// 업로드 경로에 파일 업로드
								file.transferTo(uploadTarget);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else {
							result.add("파일 수정 불가");
						}
					}
				}
				int updateSiteInfo = prjdocumentindexService.updateSiteInfo(prjdocumentindexvo);
				result.add(updateSiteInfo);
				prjstepvo.setDoc_id(prjdocumentindexvo.getDoc_id());
				prjstepvo.setRev_id(prjdocumentindexvo.getRev_id());
				List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
				List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
				List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
				List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
				for(int i=0;i<step_code_id_array.size();i++) {
					String step_code_id = step_code_id_array.get(i);
					prjstepvo.setStep_code_id(step_code_id);
					int count = prjstepService.getSiteStepByStepCodeId(prjstepvo);
					String plan_date = plan_date_array.get(i);
					String actual_date = actual_date_array.get(i);
					String fore_date = fore_date_array.get(i);
					prjstepvo.setPlan_date(plan_date);
					prjstepvo.setActual_date(actual_date);
					prjstepvo.setFore_date(fore_date);
					if(count>0) {//이미 해당 step_code_id에 대한 row가 있으면 update
						int updatePrjStep = prjstepService.updateSitePrjStep(prjstepvo);
					}else {//해당 step_code_id에 대한 row가 없으면 insert
						int insertPrjStep = prjstepService.insertSitePrjStep(prjstepvo);
					}
				}
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateSiteInfo Exception: {}");
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateRevisionSite
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서의 revision no 업데이트 (Site)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateRevisionSite.do")
	@ResponseBody
	public Object updateRevisionSite(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,PrjStepVO prjstepvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setAttach_file_date(CommonConst.currentDateAndTime());
		prjstepvo.setReg_id(sessioninfo.getUser_id());
		prjstepvo.setReg_date(CommonConst.currentDateAndTime());
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		try {
			String rev_id = prjdocumentindexService.updateRevisionOfDocumentIndex(prjdocumentindexvo);
			prjdocumentindexvo.setRev_id(rev_id);
			prjstepvo.setRev_id(rev_id);

			//파일 업로드
			MultipartFile file = mtRequest.getFile("updateRevision_File");
			String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+rev_id+"."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int insertSiteInfoForUpdateRevision = prjdocumentindexService.insertSiteInfoForUpdateRevision(prjdocumentindexvo);
			result.add(insertSiteInfoForUpdateRevision);
			List<String> step_code_id_array = new Gson().fromJson( prjstepvo.getStepCodeIdArray(), List.class );
			List<String> plan_date_array = new Gson().fromJson( prjstepvo.getPlanDateArray(), List.class );
			List<String> actual_date_array = new Gson().fromJson( prjstepvo.getActualDateArray(), List.class );
			List<String> fore_date_array = new Gson().fromJson( prjstepvo.getForeDateArray(), List.class );
			for(int i=0;i<step_code_id_array.size();i++) {
				String step_code_id = step_code_id_array.get(i);
				prjstepvo.setStep_code_id(step_code_id);
				String plan_date = plan_date_array.get(i);
				String actual_date = actual_date_array.get(i);
				String fore_date = fore_date_array.get(i);
				prjstepvo.setPlan_date(plan_date);
				prjstepvo.setActual_date(actual_date);
				prjstepvo.setFore_date(fore_date);
				prjstepService.insertSitePrjStepForUpdateRevision(prjstepvo);
			}

			if(prjdocumentindexvo.getOrigin_rev_code_id().equals("")) {
				//최초 리비전일 경우 Actual Date 저장
				prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
				prjstepService.insertSiteStartActualDateByFirstRevision(prjstepvo);
				//ForecastDate 업데이트
				prjstepvo.setSet_val2("SITE DOC. CHECK IN시");
				List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = prjstepService.getUpdateSiteStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateSiteStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateSiteStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateSiteStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
					if(!getForeDateRuleBySiteStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
						PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
						if(getActualDateSiteStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
			result.add(rev_id);
		}catch(Exception e) {
			System.out.println("updateRevisionSite Exception: {}");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : corrDocSave
	 * 2. 작성일: 2022-01-13
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence 도서 목록 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "corrDocSave.do")
	@ResponseBody
	public Object corrDocSave(PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
			if(isDuplicateOtherDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndexForCorr(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				if(prjdocumentindexvo.getRef_corr_doc_id()==null || prjdocumentindexvo.getRef_corr_doc_id().equals("")) {
					prjdocumentindexvo.setRef_grp(Long.parseLong(doc_id));
				}else {
					HashMap<String,List<String>> returnValue = recursive(prjdocumentindexvo,new ArrayList<String>(),new ArrayList<String>());
					List<String> refGrpArray = returnValue.get("refGrpArray");
					List<Long> refGrpLongArray = new ArrayList<Long>();
					for(String refgrp : refGrpArray) {
						refGrpLongArray.add(Long.parseLong(refgrp));
					}
					long min_ref = Collections.min(refGrpLongArray);
					prjdocumentindexvo.setRef_grp(min_ref);

					//관련된 도서들 모두 ref_grp을 위에 구한 min_ref로 업데이트
					List<String> refDocArray = returnValue.get("refDocArray");
					List<String> refDocUniqueArray = new ArrayList<String>();
					refDocUniqueArray = refDocArray.stream().distinct().collect(Collectors.toList());
					for(int k=0;k<refDocUniqueArray.size();k++) {
						String ref_doc_id = refDocUniqueArray.get(k);
						PrjDocumentIndexVO prjrefdocvo = new PrjDocumentIndexVO();
						prjrefdocvo.setPrj_id(prjdocumentindexvo.getPrj_id());
						prjrefdocvo.setDoc_id(ref_doc_id);
						prjrefdocvo.setRef_grp(min_ref);
						prjdocumentindexService.updateRefGrp(prjrefdocvo);
					}
				}
				int insertCorrInfo = prjdocumentindexService.insertCorrInfo(prjdocumentindexvo);
				result.add(insertCorrInfo);
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("insertDocumentIndex,insertCorrInfo Exception: {}");
			}
		}else {//update일 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNoChange = prjdocumentindexService.isDuplicateOtherDocNoChange(prjdocumentindexvo);
			if(isDuplicateOtherDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				int updateDocumentIndexForCorr = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				int updateCorrInfo = prjdocumentindexService.updateCorrInfo(prjdocumentindexvo);
				result.add(updateCorrInfo);
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
				
				if(prjdocumentindexvo.getRef_corr_doc_id()==null || prjdocumentindexvo.getRef_corr_doc_id().equals("")) {
					prjdocumentindexvo.setRef_grp(Long.parseLong(prjdocumentindexvo.getDoc_id()));
				}else {
					HashMap<String,List<String>> returnValue = recursive(prjdocumentindexvo,new ArrayList<String>(),new ArrayList<String>());
					List<String> refGrpArray = returnValue.get("refGrpArray");
					List<Long> refGrpLongArray = new ArrayList<Long>();
					for(String refgrp : refGrpArray) {
						refGrpLongArray.add(Long.parseLong(refgrp));
					}
					long min_ref = Collections.min(refGrpLongArray);
					prjdocumentindexvo.setRef_grp(min_ref);

					//관련된 도서들 모두 ref_grp을 위에 구한 min_ref로 업데이트
					List<String> refDocArray = returnValue.get("refDocArray");
					List<String> refDocUniqueArray = new ArrayList<String>();
					refDocUniqueArray = refDocArray.stream().distinct().collect(Collectors.toList());
					for(int k=0;k<refDocUniqueArray.size();k++) {
						String ref_doc_id = refDocUniqueArray.get(k);
						PrjDocumentIndexVO prjrefdocvo = new PrjDocumentIndexVO();
						prjrefdocvo.setPrj_id(prjdocumentindexvo.getPrj_id());
						prjrefdocvo.setDoc_id(ref_doc_id);
						prjrefdocvo.setRef_grp(min_ref);
						prjdocumentindexService.updateRefGrp(prjrefdocvo);
					}
				}
				prjdocumentindexService.updateRefGrp(prjdocumentindexvo);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("updateDocumentIndex,updateCorrInfo Exception: {}");
			}
		}
		return result;
	}
	
	public HashMap<String,List<String>> recursive(PrjDocumentIndexVO prjdocumentindexvo,List<String> refGrpArray,List<String> refDocArray) {
		HashMap<String,List<String>> returnValue = new HashMap<String,List<String>>();
		String[] ref_doc_array = prjdocumentindexvo.getRef_corr_doc_id().split("@@");
		for(int i=0;i<ref_doc_array.length;i++) {
			refDocArray.add(ref_doc_array[i]);
			String ref_doc_id = ref_doc_array[i];
			PrjDocumentIndexVO prjrefdocvo = new PrjDocumentIndexVO();
			prjrefdocvo.setPrj_id(prjdocumentindexvo.getPrj_id());
			prjrefdocvo.setDoc_id(ref_doc_id);
			PrjDocumentIndexVO getRefGrp = prjdocumentindexService.getRefGrp(prjrefdocvo);
			if(getRefGrp.getRef_corr_doc_id()==null || getRefGrp.getRef_corr_doc_id().equals("")) {
				refGrpArray.add(Long.toString(getRefGrp.getRef_grp()));
			}else {
				String[] ref_doc_array_sub = getRefGrp.getRef_corr_doc_id().split("@@");
				for(int j=0;j<ref_doc_array_sub.length;j++)
					refDocArray.add(ref_doc_array_sub[j]);
				
				recursive(getRefGrp,refGrpArray,refDocArray);
			}
		}
		System.out.println("refGrpArray:"+refGrpArray);
		System.out.println("refDocArray:"+refDocArray);
		returnValue.put("refGrpArray", refGrpArray);
		returnValue.put("refDocArray", refDocArray);
		return returnValue;
    }
	
	/**
	 * 1. 메소드명 : corrDocSaveAttach
	 * 2. 작성일: 2022-01-02
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence 도서 목록 추가 (파일 첨부 포함)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "corrDocSaveAttach.do")
	@ResponseBody
	public Object corrDocSaveAttach(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
			if(isDuplicateOtherDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndexForCorr(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				if(prjdocumentindexvo.getRef_corr_doc_id()==null || prjdocumentindexvo.getRef_corr_doc_id().equals("")) {
					prjdocumentindexvo.setRef_grp(Long.parseLong(doc_id));
				}else {
					HashMap<String,List<String>> returnValue = recursive(prjdocumentindexvo,new ArrayList<String>(),new ArrayList<String>());
					List<String> refGrpArray = returnValue.get("refGrpArray");
					List<Long> refGrpLongArray = new ArrayList<Long>();
					for(String refgrp : refGrpArray) {
						refGrpLongArray.add(Long.parseLong(refgrp));
					}
					long min_ref = Collections.min(refGrpLongArray);
					prjdocumentindexvo.setRef_grp(min_ref);

					//관련된 도서들 모두 ref_grp을 위에 구한 min_ref로 업데이트
					List<String> refDocArray = returnValue.get("refDocArray");
					List<String> refDocUniqueArray = new ArrayList<String>();
					refDocUniqueArray = refDocArray.stream().distinct().collect(Collectors.toList());
					for(int k=0;k<refDocUniqueArray.size();k++) {
						String ref_doc_id = refDocUniqueArray.get(k);
						PrjDocumentIndexVO prjrefdocvo = new PrjDocumentIndexVO();
						prjrefdocvo.setPrj_id(prjdocumentindexvo.getPrj_id());
						prjrefdocvo.setDoc_id(ref_doc_id);
						prjrefdocvo.setRef_grp(min_ref);
						prjdocumentindexService.updateRefGrp(prjrefdocvo);
					}
				}
				int insertCorrInfo = prjdocumentindexService.insertCorrInfo(prjdocumentindexvo);
				result.add(insertCorrInfo);
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("insertDocumentIndex,insertCorrInfo Exception: {}");
			}
		}else {//update일 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNoChange = prjdocumentindexService.isDuplicateOtherDocNoChange(prjdocumentindexvo);
			if(isDuplicateOtherDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				int updateDocumentIndexForCorr = prjdocumentindexService.updateDocumentIndexForCorr(prjdocumentindexvo);
				int updateCorrInfo = prjdocumentindexService.updateCorrInfo(prjdocumentindexvo);
				result.add(updateCorrInfo);
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
				
				if(prjdocumentindexvo.getRef_corr_doc_id()==null || prjdocumentindexvo.getRef_corr_doc_id().equals("")) {
					prjdocumentindexvo.setRef_grp(Long.parseLong(prjdocumentindexvo.getDoc_id()));
				}else {
					HashMap<String,List<String>> returnValue = recursive(prjdocumentindexvo,new ArrayList<String>(),new ArrayList<String>());
					List<String> refGrpArray = returnValue.get("refGrpArray");
					List<Long> refGrpLongArray = new ArrayList<Long>();
					for(String refgrp : refGrpArray) {
						refGrpLongArray.add(Long.parseLong(refgrp));
					}
					long min_ref = Collections.min(refGrpLongArray);
					prjdocumentindexvo.setRef_grp(min_ref);

					//관련된 도서들 모두 ref_grp을 위에 구한 min_ref로 업데이트
					List<String> refDocArray = returnValue.get("refDocArray");
					List<String> refDocUniqueArray = new ArrayList<String>();
					refDocUniqueArray = refDocArray.stream().distinct().collect(Collectors.toList());
					for(int k=0;k<refDocUniqueArray.size();k++) {
						String ref_doc_id = refDocUniqueArray.get(k);
						PrjDocumentIndexVO prjrefdocvo = new PrjDocumentIndexVO();
						prjrefdocvo.setPrj_id(prjdocumentindexvo.getPrj_id());
						prjrefdocvo.setDoc_id(ref_doc_id);
						prjrefdocvo.setRef_grp(min_ref);
						prjdocumentindexService.updateRefGrp(prjrefdocvo);
					}
				}
				prjdocumentindexService.updateRefGrp(prjdocumentindexvo);
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateCorrInfo Exception: {}");
			}
		}

		//파일 업로드
		MultipartFile file = mtRequest.getFile("updateRevision_File");
		String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
		File dir = new File(uploadPath);
		dir.mkdirs();
		File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_001."+prjdocumentindexvo.getFile_type());
		try {
			// 업로드 경로에 파일 업로드
			file.transferTo(uploadTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : generalDocSave
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 일반 도서 목록 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "generalDocSave.do")
	@ResponseBody
	public Object generalDocSave(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		if(prjdocumentindexvo.getPopup_new_or_update().equals("new")) {//새로 추가할 때
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNo = prjdocumentindexService.isDuplicateOtherDocNo(prjdocumentindexvo);
			if(isDuplicateOtherDocNo>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				String doc_id = prjdocumentindexService.insertDocumentIndex(prjdocumentindexvo);
				prjdocumentindexvo.setDoc_id(doc_id);
				int insertGeneralInfo = prjdocumentindexService.insertGeneralInfo(prjdocumentindexvo);
				result.add(insertGeneralInfo);

				//file Attach 할 경우 (General Type은 새로 등록할때부터 File Attach 가능)
				if(!prjdocumentindexvo.getFile_value().equals("")) {
					prjdocumentindexvo.setSfile_nm(doc_id+"_001."+prjdocumentindexvo.getFile_type());
					prjdocumentindexvo.setRev_id("001");
					int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
					
					//파일 업로드
					MultipartFile file = mtRequest.getFile("updateRevision_File");
					String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
					File dir = new File(uploadPath);
					dir.mkdirs();
					File uploadTarget = new File(uploadPath + doc_id+"_001."+prjdocumentindexvo.getFile_type());
					try {
						// 업로드 경로에 파일 업로드
						file.transferTo(uploadTarget);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				result.add(doc_id);
				result.add("001");
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("insertDocumentIndex,insertGeneralInfo Exception: {}");
			}
		}else {//update일 때 revision을 올리지 않고 정보만 수정할 경우
			//DCI, VDCI, SDCI의 경우 (doc_type='DCI' or doc_type='VDCI' or doc_type='SDCI')인 조건에서 하나라도 DOC NO가 존재하면 중복 등록 안됨
			//PCI, MCI, Corr, General의 경우 동일 폴더내에서만 DOC NO가 중복이 있는지 체크함
			int isDuplicateOtherDocNoChange = prjdocumentindexService.isDuplicateOtherDocNoChange(prjdocumentindexvo);
			if(isDuplicateOtherDocNoChange>0) {
				result.add("DOC NO 중복");
				return result;
			}
			
			try {
				if(prjdocumentindexvo.getFile_value().equals("")) {	//file Attach 안 할 경우
					int updateDocumentIndex = prjdocumentindexService.updateDocumentIndex(prjdocumentindexvo);
				}else {	//file Attach 할 경우
					if(prjdocumentindexvo.getTrans_ref_doc_id().equals("COVER")) {
						prjdocumentindexvo.setSfile_nm(prjdocumentindexvo.getDoc_no().replaceAll("/", "-")+"."+prjdocumentindexvo.getFile_type());
						prjdocumentindexvo.setRfile_nm(prjdocumentindexvo.getDoc_no().replaceAll("/", "-")+"."+prjdocumentindexvo.getFile_type());
					}
					int updateDocumentIndexFileAttach = prjdocumentindexService.updateDocumentIndexFileAttach(prjdocumentindexvo);
					
					//파일 업로드
					MultipartFile file = mtRequest.getFile("updateRevision_File");
					String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
					File dir = new File(uploadPath);
					dir.mkdirs();
					if(prjdocumentindexvo.getTrans_ref_doc_id().equals("COVER")) {
						File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_no().replaceAll("/", "-")+"."+prjdocumentindexvo.getFile_type());
						try {
							// 업로드 경로에 파일 업로드
							file.transferTo(uploadTarget);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+ "_" + prjdocumentindexvo.getRev_id() + "." +prjdocumentindexvo.getFile_type());
						try {
							// 업로드 경로에 파일 업로드
							file.transferTo(uploadTarget);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				int updateGeneralInfo = prjdocumentindexService.updateGeneralInfo(prjdocumentindexvo);
				result.add(updateGeneralInfo);
				result.add(prjdocumentindexvo.getDoc_id());
				result.add(prjdocumentindexvo.getRev_id());
			}catch(Exception e) {
				System.out.println("updateDocumentIndex,updateGeneralInfo Exception: {}");
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateRevisionGeneral
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서의 revision no 업데이트 (General)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateRevisionGeneral.do")
	@ResponseBody
	public Object updateRevisionGeneral(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(CommonConst.currentDateAndTime());
		prjdocumentindexvo.setAttach_file_date(CommonConst.currentDateAndTime());

		try {
			boolean chkFile = prjdocumentindexService.docChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		try {
			String rev_id = prjdocumentindexService.updateRevisionOfDocumentIndex(prjdocumentindexvo);
			prjdocumentindexvo.setRev_id(rev_id);

			//파일 업로드
			MultipartFile file = mtRequest.getFile("updateRevision_File");
			String uploadPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjdocumentindexvo.getDoc_id()+"_"+rev_id+"."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int insertGeneralInfoForUpdateRevision = prjdocumentindexService.insertGeneralInfoForUpdateRevision(prjdocumentindexvo);
			result.add(insertGeneralInfoForUpdateRevision);
			result.add(rev_id);
		}catch(Exception e) {
			System.out.println("updateRevisionGeneral Exception: {}");
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getProcList
	 * 2. 작성일: 2022-04-05
	 * 3. 작성자: 소진희
	 * 4. 설명: Vendor Property에서 선택할 Procurement 도서 목록을 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getProcList.do")
	@ResponseBody
	public Object getProcList(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> getProcList = prjdocumentindexService.getProcList(prjdocumentindexvo);
		result.add(getProcList);
		return result;
	}

	/**
	 * 1. 메소드명 : getRefDocNoList
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 소진희
	 * 4. 설명: ref_corr_doc_id 필드값을 이용하여 화면에 보여줄 참조도서 No를 구함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getRefDocNoList.do")
	@ResponseBody
	public Object getRefDocNoList(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		String ref_doc_no_list = "";
		String[] ref_corr_doc_array = prjdocumentindexvo.getRef_corr_doc_id().split("@@");
		for(int i=0;i<ref_corr_doc_array.length;i++){
			String ref_doc_id = ref_corr_doc_array[i];
			prjdocumentindexvo.setDoc_id(ref_doc_id);
			String refDocNo = prjdocumentindexService.getCorrDocNo(prjdocumentindexvo);
			ref_doc_no_list += refDocNo + ",";
		}
		ref_doc_no_list = ref_doc_no_list.substring(0, ref_doc_no_list.length()-1);
		result.add(ref_doc_no_list);
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrRefDocList
	 * 2. 작성일: 2022-02-24
	 * 3. 작성자: 소진희
	 * 4. 설명: doc_type(LETTER,E-MAIL)에 따라 INCOMING/OUTGOING Reference Doc List를 가져옴 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getCorrRefDocList.do")
	@ResponseBody
	public Object getCorrRefDocList(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> getCorrRefDocList = prjdocumentindexService.getCorrRefDocList(prjdocumentindexvo);
		result.add(getCorrRefDocList);
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrSerial
	 * 2. 작성일: 2022-02-24
	 * 3. 작성자: 소진희
	 * 4. 설명: 사용자의 Sender, Receiver, Type 입력에 따른 Corr No 자동 생성
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getCorrSerial.do")
	@ResponseBody
	public Object getCorrSerial(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		try {
			int getCorrSerialLength = prjdocumentindexService.getCorrSerialLength(prjdocumentindexvo);
			prjdocumentindexvo.setCorrLength(getCorrSerialLength);
			String getCorrSerial = prjdocumentindexService.getCorrSerial(prjdocumentindexvo);
			result.add(getCorrSerial);
		}catch(Exception e) {
			result.add("넘버링 설정 필요");
			System.out.println("getCorrSerial Exception: {}");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrNoSearch
	 * 2. 작성일: 2022-01-03
	 * 3. 작성자: 소진희
	 * 4. 설명: corr_no값을 이용해 존재하는 Correspondence 도서 검색
	 * 5. 수정일: 이제 안쓸듯. 추후 삭제
	 */
	@RequestMapping(value = "getCorrNoSearch.do")
	@ResponseBody
	public Object getCorrNoSearch(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> getCorrNoSearch = prjdocumentindexService.getCorrNoSearch(prjdocumentindexvo);
		result.add(getCorrNoSearch);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : dciGetDiscip
	 * 2. 작성일: 2021. 12. 28.
	 * 3. 작성자: doil
	 * 4. 설명:	해당 폴더의 discipline을 구한다. 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "dciGetDiscip.do")
	@ResponseBody
	public Object dciGetDiscip(PrjDocumentIndexVO prjDocumentIndexVO) {
		// 결과를 담을 리스트
		List<Object> result = new ArrayList<>();
		
		long folder_id = prjDocumentIndexVO.getFolder_id();
		String concat = "%>" + folder_id + ">%";
		prjDocumentIndexVO.setConcatFolderPath(concat);
		String discip = prjdocumentindexService.getDisciplineDisplay(prjDocumentIndexVO);
		System.out.println(discip);
		if(discip.equals("")) {
			result.add(" ");
			result.add("FAIL");
			return result;
		}
			
		result.add(discip);
		
		result.add("SUCCESS");
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : dciTemplateSave
	 * 2. 작성일: 2021. 12. 28.
	 * 3. 작성자: doil
	 * 4. 설명:  dci register에서 save 버튼 눌렀을 경우, 작동
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "dciTemplateSave.do")
	@ResponseBody
	public ModelAndView dciTemplateSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVO) {
		
		ModelAndView result = new ModelAndView();
		
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String tbl_type = "";
		String info_tbl = "";
		String save_doc_type = prjDocumentIndexVO.getDoc_type();
		switch (save_doc_type) {
			case "DCI":
				tbl_type = "ENGINEERING STEP";
				info_tbl = "p_prj_eng_info";
				break;
			case "VDCI":
				tbl_type = "VENDOR DOC STEP";
				info_tbl = "p_prj_vdr_info";
				break;
			case "SDCI":
				tbl_type = "SITE DOC STEP";
				info_tbl = "p_prj_sdc_info";
				break;
			case "PCI":
				tbl_type = "PROCUREMENT STEP";
				info_tbl = "p_prj_pro_info";
				break;
			case "MCI":
				tbl_type = "BULK STEP";
				info_tbl = "p_prj_mdc_info";
				break;
		}
		
		prjDocumentIndexVO.setTbl_type(tbl_type);
		
		String[] jsonDatas = prjDocumentIndexVO.getJsonRowDatas();
		boolean overWrite = prjDocumentIndexVO.isOverwriteMode();
		
		List<String> status = new ArrayList<>();
		
		List<String> stepCodes = prjdocumentindexService.getStepCode(prjDocumentIndexVO);
		
		//status.add("SUCCESS");
		result.addObject("status","SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			int maxId = prjdocumentindexService.getMaxDocId(prjDocumentIndexVO);
			String discip_code_id = prjdocumentindexService.getDiscipCodeId(prjDocumentIndexVO);
			String doc_type = prjdocumentindexService.getDocument_DocType(prjDocumentIndexVO);
			
			// 매핑되지 않는 data들 무시
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			
			
			// validation check
			ModelAndView check = prjdocumentindexService.indexRegisterValidationCheck(prjDocumentIndexVO,overWrite,reg_id,info_tbl,jsonDatas);
			
			boolean checkStatus = (boolean) check.getModel().get("status");
			if(!checkStatus) {
				String error = (String) check.getModel().get("error");
				result.addObject("status","FAIL");
				result.addObject("error",error);
				// status.set(0, "FAIL");
				
				return result;
			}
			
			
			
			try {
				for(String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
					tmpBean.setPrj_id(prjDocumentIndexVO.getPrj_id());
					tmpBean.setFolder_id(prjDocumentIndexVO.getFolder_id());
					tmpBean.setReg_id(reg_id);
					tmpBean.setPrfe_designer_id(tmpBean.getDesigner_id());
					tmpBean.setReg_date(reg_date);
					tmpBean.setTbl_type(info_tbl);
					UsersVO chkDesigner = prjdocumentindexService.isInDesigner(tmpBean);
					if(chkDesigner == null) throw new Exception("["+tmpBean.getDesigner_id()+"] : There is no user who has that name in project");
					List<PrjDocumentIndexVO> existDocList = prjdocumentindexService.isInDocNo(tmpBean);
					tmpBean.setDesigner_id(chkDesigner.getUser_id());
					tmpBean.setPrfe_designer_id(chkDesigner.getUser_id());
					
					// overWrite 모드
					if(overWrite) {
						// 입력한 doc_no가 기존 DB에 이미 존재하지 않는다면 update
						//if(existDocList.get(0).getDoc_id() != null) {
						if(existDocList.size() > 0) {
							PrjDocumentIndexVO existDoc = existDocList.get(0);
							tmpBean.setDoc_id(existDocList.get(0).getDoc_id());
							tmpBean.setRev_id(existDocList.get(0).getRev_id());
							// lock 여부 y/n을 무조건 대문자로 변환
							
							//String lock = existDocList.get(0).getLock_yn().toUpperCase();
							String lock = existDoc.getLock_yn();
							if(lock == null || lock.equals("")) lock = "N"; 
							lock = lock.toUpperCase();
							// 후속처리(lock처리) 진행되었으면 update 불가
							if(lock.equals("Y") || lock.equals("y")) {
								System.out.println("[" + tmpBean.getDoc_no() + "] : This document is locked | can't update");
								throw new Exception("["+ tmpBean.getDoc_no() + "]: This document is locked | can't update");
								//continue;								
							}
							// overwrite 가능한 상태 (update)
							else {
								if(prjdocumentindexService.updateDCI(tmpBean) < 1)
									throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI 업데이트 실패");
								System.out.println(tmpBean.getDoc_no() + " : updated");
								// step table 지정
								switch (prjDocumentIndexVO.getDoc_type()) {
									case "DCI":
										tbl_type = "p_prj_eng_step";
										break;
									case "VDCI":
										tbl_type = "p_prj_vdr_step";
										break;
									case "SDCI":
										tbl_type = "p_prj_sdc_step";
										break;
									case "PCI":
										tbl_type = "p_prj_pro_step";
										break;
									case "MCI":
										tbl_type = "p_prj_mdc_step";
										break;
								}
								
								int stepIdx = 0;
								for(String step_code : stepCodes) {
									String plan_date = tmpBean.getStepData().get(stepIdx++);
									tmpBean.setStep_code(step_code);
									tmpBean.setPlan_date(plan_date);
									// 처음에는 plan date = fore date
									tmpBean.setFore_date(plan_date);
									tmpBean.setTbl_type(tbl_type);
									if(prjdocumentindexService.updateDCI_step(tmpBean) < 1)
										throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI stepDate update 실패");
								}
							}
						}
						else {
						// insert할 doc_id 생성
						tmpBean.setDoc_id(String.format("%010d", maxId++));
						tmpBean.setRev_id("001");
						// insert 수행
						tmpBean.setDiscip_code_id(discip_code_id);
						
						
						
						if(prjdocumentindexService.insertDCI(tmpBean) < 1)
							throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI eng 삽입 실패");
						tmpBean.setDoc_type(save_doc_type);
						if(prjdocumentindexService.insertDCI2(tmpBean) < 1)
							throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI document 삽입 실패");

						
						
						int stepIdx = 0;
						for(String step_code : stepCodes) {
							String plan_date = tmpBean.getStepData().get(stepIdx++);
							tmpBean.setStep_code(step_code);
							tmpBean.setPlan_date(plan_date);
							tmpBean.setFore_date(plan_date);
							if(prjdocumentindexService.insertDCI_step(tmpBean) < 1)
								throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI stepDate 삽입 실패");
						}
						System.out.println(tmpBean.getDoc_no() + " : inserted");
						}
					}
					else {
						// 일반모드 (insert만 수행)
						//if(existDocList.get(0).getDoc_id() != null)
						if(existDocList.size() > 0) 
							throw new Exception("["+ tmpBean.getDoc_no() + "]: 동일 DOC_NO가 존재하여, 삽입 실패");
						else {
							// insert할 doc_id 생성
							tmpBean.setDoc_id(String.format("%010d", maxId++));
 							tmpBean.setRev_id("001");
 							
 							
 							switch (prjDocumentIndexVO.getDoc_type()) {
							case "DCI":
								tbl_type = "p_prj_eng_step";
								break;
							case "VDCI":
								tbl_type = "p_prj_vdr_step";
								break;
							case "SDCI":
								tbl_type = "p_prj_sdc_step";
								break;
							case "PCI":
								tbl_type = "p_prj_pro_step";
								break;
							case "MCI":
								tbl_type = "p_prj_mdc_step";
								break;
						}
 							
							// insert 수행
 							tmpBean.setDiscip_code_id(discip_code_id);
 							
							if(prjdocumentindexService.insertDCI(tmpBean) < 1)
								throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI Info 삽입 실패");
							
							tmpBean.setDoc_type(save_doc_type);
 							if(prjdocumentindexService.insertDCI2(tmpBean) < 1)
								throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI document 삽입 실패");
							
							int stepIdx = 0;
							for(String step_code : stepCodes) {
								String plan_date = tmpBean.getStepData().get(stepIdx++);
								tmpBean.setStep_code(step_code);
								tmpBean.setPlan_date(plan_date);
								// 처음에는 plan date = fore date
								tmpBean.setFore_date(plan_date);
								tmpBean.setTbl_type(tbl_type);
								if(prjdocumentindexService.insertDCI_step(tmpBean) < 1)
									throw new Exception("["+ tmpBean.getDoc_no() + "]: DCI stepDate 삽입 실패");
							}
							System.out.println(tmpBean.getDoc_no() + " : inserted");
						}
						
					}
					
				}
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				result.addObject("status","FAIL");
				
				//status.set(0, "FAIL");
				// 실패한 메시지들을 담아줌
				status.add(e.getMessage());				
			}
		}
		
		result.addObject("error", status);
		return result;
	}
	/**
	 * 
	 * 1. 메소드명 : dciExcelDownload
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: doil
	 * 4. 설명: to Excel 버튼 눌렀을 경우 엑셀 템플릿 생성 및 파일 다운로드 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "dciExcelDownload.do")
	@ResponseBody
	public void dciExcelDownload(PrjDocumentIndexVO prjDocumentIndexVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> getDciList = new ArrayList<>();
		
		String json = prjDocumentIndexVO.getJsonString();
		
		List<String> jsonDatas = Arrays.asList( json.split("}") );
		ObjectMapper objectMapper = new ObjectMapper();
		
		// 매핑되지 않는 data들 무시
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		if(jsonDatas.size()>1)
		for(String line : jsonDatas) {
			line = line.substring(1,line.length());
			if(line.length()<1) continue;
			line += '}';
			PrjDocumentIndexVO tmpBean = objectMapper.readValue(line, PrjDocumentIndexVO.class);
			getDciList.add(tmpBean);
		}		
				
		// 해당폴더의 document 리스트를 구한다.
		//List<PrjDocumentIndexVO> getDciList = prjdocumentindexService.getDciList(prjDocumentIndexVO);
		
		ExcelFileDownload excelDown = new ExcelFileDownload();
		String[] prjInfo = new String[2];
		prjInfo[0] = prjDocumentIndexVO.getDiscipline();
		prjInfo[1] = prjDocumentIndexVO.getPrj_nm();
		// step필드 생성
		List<String> fieldList = prjdocumentindexService.getStepField(prjDocumentIndexVO);
		
		// 각 row의 step 필드값은 plandate으로 매핑
		int idx=0;
		for(PrjDocumentIndexVO row : getDciList) {
			// 문서의 doc_type
			String doc_type = row.getDoc_type();
			// ENG,VDC,SDC 등 doc_type ( 테이블 검색을 위함)
			row.setDoc_type(prjDocumentIndexVO.getDoc_type());
			List<PrjDocumentIndexVO> stepDateList = prjdocumentindexService.getStepDate(row);
			List<String> stepDates = new ArrayList<>();
			for(PrjDocumentIndexVO step : stepDateList) 
					stepDates.add(step.getPlan_date()); 
			row.setDoc_type(doc_type);
			row.setStepData(stepDates);
			getDciList.set(idx++, row);
		}
		
		String fileName = prjDocumentIndexVO.getFileName();
		excelDown.dciIndexRegisterToExcel(response, fieldList, getDciList, fileName , prjInfo , prjDocumentIndexVO.getDoc_type());
	}
	
	/**
	 * 
	 * 1. 메소드명 : getFieldList
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: doil
	 * 4. 설명: 필드 리스트 반환 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getDCIFieldList.do")
	@ResponseBody
	public List<String> getFieldList(PrjDocumentIndexVO prjDocumentIndexVO){
		List<String> result = prjdocumentindexService.getStepField(prjDocumentIndexVO); 
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : useDrnCheckForThisFolder
	 * 2. 작성일: 2022. 3. 5.
	 * 3. 작성자: doil
	 * 4. 설명: 프로젝트,폴더아이디를 기준으로 drn 사용 여부 확인 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "useDrnCheckForThisFolder.do")
	@ResponseBody
	public String useDrnCheckForThisFolder(PrjDocumentIndexVO prjDocumentIndexVO){
		String useDrnYN = prjdocumentindexService.useDrnCheckForThisFolder(prjDocumentIndexVO); 
		return useDrnYN;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getDCIParams
	 * 2. 작성일: 2022. 1. 3.
	 * 3. 작성자: doil
	 * 4. 설명: code_id 기준으로 각 매핑되는 코드값을 가져온다. 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getDCIParams.do")
	@ResponseBody
	public Object getDCIParams(PrjDocumentIndexVO prjDocumentIndexVO){
		List<Object> result = new ArrayList<>();
		
		boolean isVdoc = false;
		String folder_docType = prjDocumentIndexVO.getDoc_type();
		if(folder_docType.equals("VDCI")) isVdoc = true;
		
		WbsCodeControlVO wbsVo = new WbsCodeControlVO();
		PrjCodeSettingsVO codeVo = new PrjCodeSettingsVO();
		
		wbsVo.setPrj_id(prjDocumentIndexVO.getPrj_id());
		codeVo.setPrj_id(prjDocumentIndexVO.getPrj_id());
		
		/*
		List<PrjCodeSettingsVO> getDocCate = prjdocumentindexService.getDocCate(codeVo);
		if(isVdoc) getDocCate = prjdocumentindexService.getVDocCate(codeVo);
		List<PrjCodeSettingsVO> getDocSize = prjdocumentindexService.getDocSize(codeVo);
		if(isVdoc) getDocSize = prjdocumentindexService.getVDocSize(codeVo);
		*/
		
		List<PrjCodeSettingsVO> getDocCate = null;
		List<PrjCodeSettingsVO> getDocSize = null;
		
		switch(folder_docType) {
			case "VDCI":
				getDocCate = prjdocumentindexService.getVDocCate(codeVo);
				getDocSize = prjdocumentindexService.getVDocSize(codeVo);
				break;
			default:
				getDocCate = prjdocumentindexService.getDocCate(codeVo);
				getDocSize = prjdocumentindexService.getDocSize(codeVo);
				break;
		}
		
		
		List<PrjCodeSettingsVO> getDocType = prjdocumentindexService.getDocType(codeVo);
		List<WbsCodeControlVO> getWBS = prjdocumentindexService.getWBS(wbsVo);
		
		
		result.add(getDocCate);
		result.add(getDocSize);
		result.add(getDocType);
		result.add(getWBS);
		
		return result;
	}
	/**
	 * 
	 * 1. 메소드명 : getProcessDocType
	 * 2. 작성일: 2022. 1. 6.
	 * 3. 작성자: doil
	 * 4. 설명: 해당 폴더에 매핑된 프로세스 타입 return 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getProcessDocType.do")
	@ResponseBody
	public Object getProcessDocType(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<Object> result = new ArrayList<>();
		// List<ProcessInfoVO> getProcess = prjdocumentindexService.getProcessDocType(prjDocumentIndexVO);
		
		
		// 해당 폴더의 폴더패스를 구함
		String thisFolderPath = prjdocumentindexService.getFolderPath(prjDocumentIndexVO);
				
		List<PrjDocumentIndexVO> getProcessType = prjdocumentindexService.selectList("prjdocumentindexSqlMap.getFolderPathOfProcessType",prjDocumentIndexVO);
		
		String process_doc_type = "General";
		
		for(PrjDocumentIndexVO process : getProcessType) {
			String doc_type = process.getDoc_type();
			String folder_path = process.getCurrent_folder_path();
			
			if(thisFolderPath.contains(folder_path)) {
				process_doc_type = doc_type;
				break;
			}
		}
		
		result.add(process_doc_type);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : checkProcessType
	 * 2. 작성일: 2022. 3. 25.
	 * 3. 작성자: doil
	 * 4. 설명: 프로세스를 타는 폴더가 아니면 index register 동작 불가
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "checkProcessType.do")
	@ResponseBody
	public ModelAndView checkProcessType(PrjDocumentIndexVO prjDocumentIndexVO) {
		ModelAndView result = new ModelAndView();
		
		result.addObject("process_type",null);
		result.addObject("status",false);
		
		// 해당 폴더의 폴더패스를 구함
		String thisFolderPath = prjdocumentindexService.getFolderPath(prjDocumentIndexVO);
		
		List<PrjDocumentIndexVO> getProcessType = prjdocumentindexService.selectList("prjdocumentindexSqlMap.getFolderPathOfProcessType",prjDocumentIndexVO);
		
		for(PrjDocumentIndexVO process : getProcessType) {
			String process_type = process.getProcess_type();
			String folder_path = process.getCurrent_folder_path();
			
			if(thisFolderPath.contains(folder_path)) {
				result.addObject("process_type",process_type);
				result.addObject("status",true);
				break;
			}
		}
		
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : process_bulk_badwordChg
	 * 2. 작성일: 2022. 3. 25.
	 * 3. 작성자: doil
	 * 4. 설명: BULK 오타 수정 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "process_bulk_badwordChg.do")
	@ResponseBody
	public void process_bulk_badwordChg() {
		prjdocumentindexService.update("prjdocumentindexSqlMap.process_bulk_badwordChg","BULK MATERIAL (BMC)");
		prjdocumentindexService.update("prjdocumentindexSqlMap.bmcToMCI","MCI");
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getDCIRegisterList
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: doil
	 * 4. 설명: dci index register 모달창 db정보 불러오기
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getDCIRegisterList.do")
	@ResponseBody
	public Object getDCIRegisterList(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<Object> result = new ArrayList<Object>();
		String doc_type = prjDocumentIndexVO.getDoc_type();
		String prj_id = prjDocumentIndexVO.getPrj_id();
		// 해당폴더의 document 리스트를 구한다.
		List<PrjDocumentIndexVO> getDciList = prjdocumentindexService.getDciList(prjDocumentIndexVO);
		// ExcelFileDownload excelDown = new ExcelFileDownload();
		String[] prjInfo = new String[2];
		prjInfo[0] = prjDocumentIndexVO.getDiscip_display();
		prjInfo[1] = prjDocumentIndexVO.getPrj_nm();
		// step필드 생성
		List<String> fieldList = prjdocumentindexService.getStepField(prjDocumentIndexVO);
		result.add(fieldList);
		
		// 각 row의 step 필드값은 plandate으로 매핑
		int idx = 0;
		for(PrjDocumentIndexVO row : getDciList) {
			row.setDoc_type(doc_type);
			row.setPrj_id(prj_id);
			List<PrjDocumentIndexVO> stepDateList = prjdocumentindexService.getStepDate(row);
			List<String> stepDates = new ArrayList<>();
			for(PrjDocumentIndexVO step : stepDateList) stepDates.add(step.getPlan_date());
			row.setStepData(stepDates);
			getDciList.set(idx++, row);
		}
		
		result.add(getDciList);
		
		return result;
	}
	
	@RequestMapping(value = "getUsedRevNo.do")
	@ResponseBody
	public Object getUsedRevNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<Object> result = new ArrayList<Object>();
		
		// 사용했던 rev no
		List<String> getUsedRevNo = prjdocumentindexService.getUsedRevNo(prjDocumentIndexVO);
		// rev no list
		List<PrjDocumentIndexVO> revNoList = prjdocumentindexService.getRevNoList(prjDocumentIndexVO);
		
		
		result.add(getUsedRevNo);
		result.add(revNoList);
		
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : checkInChkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "checkInChkFile.do")
	@ResponseBody
	public int checkInChkFile(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjDocumentIndexVO) throws Exception{
		int chkFile = 0;
		
		
		//List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("uploadfile");
		
		MultipartFile mf = mtRequest.getFile("checkIn_File");
		File file = new File(mf.getOriginalFilename());
		// true면 등록 불가 false면 등록 가능한 확장자
//		boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
		// true면 등록 가능 false면 등록 불가능한 확장자
		boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
			
		if(fileUploadChk == true) {
			chkFile = 100;
		}else if(fileUploadChk == false) {
			chkFile = 0;
		}
			
		
		return chkFile;
	}
    
	@RequestMapping(value = "checkIn.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView checkIn(MultipartHttpServletRequest mtRequest, PrjDocumentIndexVO prjDocumentIndexVO,PrjStepVO prjstepvo) throws ParseException {

		ModelAndView model = new ModelAndView();
		model.addObject("status", "SUCCESS");
		
		try {
			int chkFile = prjdocumentindexService.chkFile(mtRequest,"checkIn_File");
			if(chkFile < 1) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			model.addObject("status","FAIL");
			model.addObject("fail",e2.getMessage());
			return model;
		}
		
		
		// 리비전 넘버를 선택하지 않고 진행한 경우 예외처리
		String check_rev_code_id = prjDocumentIndexVO.getRev_code_id();
		if(check_rev_code_id == null || check_rev_code_id.equals("")) {
			model.addObject("status", "FAIL");
			model.addObject("fail","you have to choose the revision number");
			return model;
		}
		// 세션 유저
		String session_id = prjDocumentIndexVO.getReg_id();
		
		
		HttpSession session = mtRequest.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String login_id = sessioninfo.getUser_id();
		prjDocumentIndexVO.setReg_id(login_id);
		// 권한체크 (쓰기권한)
		// ModelAndView auth = prjdocumentindexService.authCheck(prjDocumentIndexVO);
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();

		String reg_date = sdf.format(c1.getTime());
		

		
		System.out.println(prjDocumentIndexVO.getPrj_id());
		
		MultipartFile file = mtRequest.getFile("checkIn_File");

		System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());

		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String uploadPath = UPLOAD_PATH + prjDocumentIndexVO.getPrj_id() + DOC_PATH;
		
		String doc_type = prjDocumentIndexVO.getDoc_type();
		List<ProcessInfoVO> getProcessDocType = prjdocumentindexService.getProcessDocType(prjDocumentIndexVO);
		
		if(getProcessDocType.size() == 1) doc_type = getProcessDocType.get(0).getDoc_type();
		
		prjDocumentIndexVO.setDoc_type(doc_type);
		String old_rev = prjDocumentIndexVO.getRev_id();
		Integer max_rev = Integer.parseInt(old_rev);
		max_rev += 1;
			
		// 확장자
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length - 1];
		prjDocumentIndexVO.setFile_type(extension);

		String rfile_nm = file.getOriginalFilename();
		rfile_nm = rfile_nm.replaceAll("\\(","");
		rfile_nm = rfile_nm.replaceAll("\\)","");
		prjDocumentIndexVO.setRfile_nm(rfile_nm);
		String sFile_nm = prjDocumentIndexVO.getDoc_id() + "_" + String.format("%03d", max_rev) + "." + extension;
		prjDocumentIndexVO.setSfile_nm(sFile_nm);

		// 파일 업로드
		File dir = new File(uploadPath);
		dir.mkdirs();
		File uploadTarget = new File(uploadPath + sFile_nm);
		// 기존 데이터 받아오기
		try {
			// 업로드 경로에 파일 업로드
			file.transferTo(uploadTarget);

			long file_size = file.getSize();
			prjDocumentIndexVO.setFile_size(file_size);
			prjDocumentIndexVO.setDel_yn("N");
			String prev_reg_date = prjdocumentindexService.getRegDate(prjDocumentIndexVO);
			if(prev_reg_date == null) {
				prjDocumentIndexVO.setReg_date(reg_date);
			}
			else {
				prjDocumentIndexVO.setReg_date(prev_reg_date);
			}
			prjDocumentIndexVO.setMod_date(reg_date);
			int new_rev_id = prjdocumentindexService.getMaxRevId(prjDocumentIndexVO);
			prjDocumentIndexVO.setRev_max(String.format("%03d", new_rev_id));

			try {
				if (prjdocumentindexService.checkIn(prjDocumentIndexVO) < 1)
					throw new Exception("DB업로드 실패");
				prjDocumentIndexVO.setTbl_type(doc_type);
				PrjDocumentIndexVO preInfo = prjdocumentindexService.getPreInfo(prjDocumentIndexVO);
				if(preInfo == null) preInfo = new PrjDocumentIndexVO();
				preInfo.setTbl_type(doc_type);
				preInfo.setRev_id(String.format("%03d", new_rev_id));
				
				
				if (prjdocumentindexService.checkIn2(preInfo) < 1)
					throw new Exception("DB업로드 실패");
			} catch (Exception e1) {
				e1.printStackTrace();
				model.addObject("fail", e1.getMessage());
				model.addObject("status", "FAIL");
				return model;
			}

			
			//prjDocumentIndexVO.setRev_id(String.format("%03d", Integer.parseInt(prjDocumentIndexVO.getRev_id())+1));
			//System.out.println("rev_id:"+prjDocumentIndexVO.getRev_id());
			
			if(prjDocumentIndexVO.getOrigin_rev_code_id().equals("")) {
				if(doc_type.equals("DCI")) {
					String rev_id = prjstepService.insertEngStepByCheckIn(prjstepvo);
					prjstepvo.setRev_id(rev_id);
					// eng info에 인서트
//					prjDocumentIndexVO.setRev_id(rev_id);
//					prjdocumentindexService.insertEngInfoForUpdateRevision(prjDocumentIndexVO);
					
					
					//최초 리비전일 경우 Actual Date 저장
					prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
					prjstepService.insertStartActualDateByFirstRevision(prjstepvo);
					
					//ForecastDate 업데이트
					prjstepvo.setSet_val2("DOC. CHECK IN시");
					List<PrjCodeSettingsVO> getUpdateEngStepCodeId = prjstepService.getUpdateEngStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateEngStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateEngStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateEngStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
						String set_val4 = getForeDateRuleByEngStep.getSet_val4();
						if(set_val4.equals("")) set_val4 = "0";
						if(Integer.parseInt(set_val4)!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
							PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
							if(getActualDateEngStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				} else if(doc_type.equals("VDCI")) {
					String rev_id = prjstepService.insertVendorStepByCheckIn(prjstepvo);
					prjstepvo.setRev_id(rev_id);
					
					// vendor info에 인서트
//					prjDocumentIndexVO.setRev_id(rev_id);
//					prjdocumentindexService.insertVendorInfoForUpdateRevision(prjDocumentIndexVO);
					
					//최초 리비전일 경우 Start Actual Date 저장
					prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
					prjstepService.insertVendorStartActualDateByFirstRevision(prjstepvo);
					//ForecastDate 업데이트
					prjstepvo.setSet_val2("VENDOR DOC. CHECK IN시");
					List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = prjstepService.getUpdateVendorStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateVendorStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateVendorStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateVendorStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
						String set_val4 = getForeDateRuleByVendorStep.getSet_val4();
						if(set_val4.equals("")) set_val4 = "0";
						if(Integer.parseInt(set_val4)!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
							PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
							if(getActualDateVendorStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				} else if(doc_type.equals("SDCI")) {
					String rev_id = prjstepService.insertSiteStepByCheckIn(prjstepvo);
					prjstepvo.setRev_id(rev_id);
					
					// eng step에 인서트
//					prjDocumentIndexVO.setRev_id(rev_id);
//					prjdocumentindexService.insertSiteInfoForUpdateRevision(prjDocumentIndexVO);
					
					//최초 리비전일 경우 Actual Date 저장
					prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
					prjstepService.insertSiteStartActualDateByFirstRevision(prjstepvo);
					//ForecastDate 업데이트
					prjstepvo.setSet_val2("SITE DOC. CHECK IN시");
					List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = prjstepService.getUpdateSiteStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateSiteStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateSiteStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateSiteStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
						String set_val4 = getForeDateRuleBySiteStep.getSet_val4();
						if(set_val4.equals("")) set_val4 = "0";
						if(Integer.parseInt(set_val4)!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
							PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
							if(getActualDateSiteStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				} else if(doc_type.equals("PCI")) {
					String rev_id = prjstepService.insertProcStepByCheckIn(prjstepvo);
					prjstepvo.setRev_id(rev_id);
					
					// pci info에 인서트
//					prjDocumentIndexVO.setRev_id(rev_id);
//					prjdocumentindexService.insertProcInfoForUpdateRevision(prjDocumentIndexVO);
					
					//최초 리비전일 경우 Start Actual Date 저장
					prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
					prjstepService.insertProcStartActualDateByFirstRevision(prjstepvo);
					//ForecastDate 업데이트
					prjstepvo.setSet_val2("PROCUREMENT DOC. CHECK IN시");
					List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
						String set_val4 = getForeDateRuleByProcStep.getSet_val4();
						if(set_val4.equals("")) set_val4 = "0";
						if(Integer.parseInt(set_val4)!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
							PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
							if(getActualDateProcStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				} else if(doc_type.equals("MCI")) {
					String rev_id = prjstepService.insertBulkStepByCheckIn(prjstepvo);
					prjstepvo.setRev_id(rev_id);
					
					// pci info에 인서트
//					prjDocumentIndexVO.setRev_id(rev_id);
//					prjdocumentindexService.insertProcInfoForUpdateRevision(prjDocumentIndexVO);
					
					//최초 리비전일 경우 Start Actual Date 저장
					prjstepvo.setActual_date(CommonConst.currentDateAndTime().substring(0, 8));
					prjstepService.insertBulkStartActualDateByFirstRevision(prjstepvo);
					//ForecastDate 업데이트
					prjstepvo.setSet_val2("BULK DOC. CHECK IN시");
					List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
					for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
						prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
						prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
						PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
						String set_val4 = getForeDateRuleByBulkStep.getSet_val4();
						if(set_val4.equals("")) set_val4 = "0";
						if(Integer.parseInt(set_val4)!=0) {
							SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
							Calendar cal = Calendar.getInstance();
							prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
							PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
							if(getActualDateBulkStepByStepCodeId!=null) {
								Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
								cal.setTime(dt);
								if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
				} 
			}
			else { // 기존 step 정보가 존재하는 경우
				prjDocumentIndexVO.setRev_id(old_rev);
				String tbl_type = prjDocumentIndexVO.getDoc_type();
				
				switch(tbl_type) {
				case "DCI":
					tbl_type = "p_prj_eng_step";
					break;
				case "VDCI":
					tbl_type = "p_prj_vdr_step";
					break;
				case "SDCI":
					tbl_type = "p_prj_sdc_step";
					break;
				case "PCI":
					tbl_type = "p_prj_pro_step";
					break;
				case "MCI":
					tbl_type = "p_prj_mdc_step";
					break;
				default :
					break;
				}
				
				List<PrjDocumentIndexVO> preStepInfo = prjdocumentindexService.getPreStepInfo(prjDocumentIndexVO);
				String step_rev_id = String.format("%03d", new_rev_id);
				if(preStepInfo != null) 
					for(PrjDocumentIndexVO step : preStepInfo) {
						step.setRev_id(step_rev_id);
						step.setTbl_type(doc_type);
						
						// date 룰 가져옴
						PrjDocumentIndexVO date_rule = prjdocumentindexService.getActualDateRuleAtCheckIn(step);
						if(date_rule != null) {
							// actual date 룰 적용
							if(date_rule.getSet_val2().contains("DOC. CHECK IN시")) {
								step.setActual_date(reg_date.substring(0,8));
							}
							
							String fore_date_rule = date_rule.getSet_val3();
							if(!fore_date_rule.equals("")) { // 공백이 아닌경우 공백이면 rule 없음
								if(fore_date_rule.contains("TR RETURN STATUS")) {
									
								}
								else { // code_id로 매핑되있는 경우
									// 계산하기 전의 fore date
									String _fore_date = prjdocumentindexService.getActualDate4ForeDate(preStepInfo,fore_date_rule);
									if(_fore_date != null) {
										Calendar cal = Calendar.getInstance();

										SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
										Date dt = dtFormat.parse(_fore_date);
										
										cal.setTime(dt);
										
										cal.add(Calendar.DATE, Integer.parseInt(date_rule.getSet_val4()));
										_fore_date = dtFormat.format(cal.getTime());
										step.setFore_date(_fore_date);
									}
									
								}
							}
						}
						
						
						try {
							if(prjdocumentindexService.insertNextStep(step)<1)
								throw new Exception("step insert fail");
						}
						catch(Exception e){
							e.printStackTrace();
							model.addObject("status", "FAIL");
							model.addObject("error", "step date insert fail");
						}
						
					}
				
		
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			model.addObject("status", "FAIL");
		}

		
		
		

		return model;
	}
	
	
	@RequestMapping(value = "isIFCStep.do")
	@ResponseBody
	public ModelAndView isIFCStep(PrjDocumentIndexVO prjdocumentindexvo) {
		ModelAndView model = new ModelAndView();
		
		String tbl_target = prjdocumentindexvo.getDoc_type();
		String set_code_type = "";
		String tr_info_table = "";
		String tr_file_table = "";
		String tr_issue_set_code_type = "";
		
		switch(tbl_target) {
			case "DCI":
				tbl_target = "p_prj_eng_step";
				set_code_type = "ENGINEERING STEP";
				tr_issue_set_code_type = "TR ISSUE PURPOSE";
				tr_info_table = "p_prj_tr_info";
				tr_file_table = "p_prj_tr_file";
				break;
			case "VDCI":
				tbl_target = "p_prj_vdr_step";
				set_code_type = "VENDOR DOC STEP";
				tr_issue_set_code_type = "VTR ISSUE PURPOSE";
				tr_info_table = "p_prj_vtr_info";
				tr_file_table = "p_prj_vtr_file";
				break;
			case "SDCI":
				tbl_target = "p_prj_sdc_step";
				set_code_type = "SITE DOC STEP";
				tr_issue_set_code_type = "STR ISSUE PURPOSE";
				tr_info_table = "p_prj_str_info";
				tr_file_table = "p_prj_str_file";
				break;
			case "PCI":
				tbl_target = "p_prj_pro_step";
				set_code_type = "PROCUREMENT STEP";
				break;
			case "MCI":
				tbl_target = "p_prj_mdc_step";
				set_code_type = "BULK STEP";
				break;
			default :
				return model;
		}
		
		prjdocumentindexvo.setTbl_type(tbl_target);
		prjdocumentindexvo.setSet_code_type(set_code_type);
		prjdocumentindexvo.setTr_info_table(tr_info_table);
		prjdocumentindexvo.setTr_file_table(tr_file_table);
		prjdocumentindexvo.setTr_issue_set_code_type(tr_issue_set_code_type);
		
		String isIFC = prjdocumentindexService.isIFCStep(prjdocumentindexvo);
		
		model.addObject("IFC",isIFC);
		
		if(isIFC.equals("Y")) {
			PrjInfoVO prjInfo = new PrjInfoVO();
			prjInfo.setPrj_id(prjdocumentindexvo.getPrj_id());
			List<PrjCodeSettingsVO> ifcList = prjdocumentindexService.getIFCReason(prjInfo);
			model.addObject("IFC_List",ifcList);
		}
		
		return model;
	}
	
	
	/**
	 * 1. 메소드명 : DocPathListExcelDownload
	 * 2. 작성일: 2021. 01. 04.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "DocPathListExcelDownload.do")
	@ResponseBody
	public void DocPathListExcelDownload(PrjDocumentIndexVO prjdocumentindexvo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		String select_rev_version = prjdocumentindexvo.getSelect_rev_version();
		String fileName = prjdocumentindexvo.getFileName();
		String getPath = prjdocumentindexvo.getCurrent_folder_path();
		
//		System.out.println(getPath+"/"+fileName+"/"+select_rev_version);
		
		
		if(select_rev_version.equals("current")) {
			List<PrjDocumentIndexVO> getPrjDocumentIndexList = prjdocumentindexService.getPrjDocumentIndexList(prjdocumentindexvo);
			
//			System.out.println(getPrjDocumentIndexList);
			ExcelFileDownload excelDown = new ExcelFileDownload();
			excelDown.DocPathListExcelDownload(response, "titleList",  getPrjDocumentIndexList, fileName, prjdocumentindexvo.getPrj_nm(), getPath);
			
		}else { //select_rev_version.equals("all")
			List<PrjDocumentIndexVO> getPrjDocumentIndexListAll = prjdocumentindexService.getPrjDocumentIndexListAll(prjdocumentindexvo);
			ExcelFileDownload excelDown = new ExcelFileDownload();
			excelDown.DocPathListAllExcelDownload(response, "titleList",  getPrjDocumentIndexListAll, fileName, prjdocumentindexvo.getPrj_nm(), getPath);
		}
//		System.out.println(getPrjMemberList);
	}
	
	/**
	 * 1. 메소드명 : multiUploadChkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "multiUploadChkFile.do")
	@ResponseBody
	public int multiUploadChkFile(HttpServletRequest request,PrjDocumentIndexVO prjDocumentIndexVO) throws Exception{
		int chkFile = 0;
		
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		multipartHttpServletRequest.getMultiFileMap();
		
		List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("uploadfile");
		
		for(MultipartFile mf : fileList) {
			File file = new File(mf.getOriginalFilename());
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
			
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}
			
			//System.out.println("파일 명 = "+file);
			//System.out.println("파일 확인 true or false = "+fileUploadChk);
		}
		
		return chkFile;
	}

	@RequestMapping(value = "multiUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView multiUpload(MultipartHttpServletRequest mtRequest, PrjDocumentIndexVO prjDocumentIndexVO ) {
				
		ModelAndView model = new ModelAndView();
		
		File filePath = new File(UPLOAD_PATH + prjDocumentIndexVO.getPrj_id() + DOC_PATH);
		if (filePath.exists() == false) {
			filePath.mkdirs();
		}	
		
		List<MultipartFile> fileList = mtRequest.getFiles("uploadfile");
		
		
		// 파일 체크
		try {
			int chkFile = prjdocumentindexService.chkFiles(mtRequest,"uploadfile");
			if(chkFile < 1) throw new Exception("There is unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			model.addObject("status","FAIL");
			model.addObject("fail",e2.getMessage());
			return model;
		}
		
		
		String titleStrings = prjDocumentIndexVO.getJsonString();
		String[] titleList = titleStrings.split("@@");
		
		String doc_type = "General";
		
		
		
		if(doc_type == null) {
			doc_type = "General";
			List<ProcessInfoVO> getProcessDocType = prjdocumentindexService.getProcessDocType(prjDocumentIndexVO);
		
			if(getProcessDocType.size() == 1) doc_type = getProcessDocType.get(0).getDoc_type();
		}
			
		if(!doc_type.equals("General")) {
			model.addObject("status", "FAIL");
			model.addObject("error","Can't upload general file to this folder.");
			return model;
		}
		
		prjDocumentIndexVO.setDoc_type(doc_type);
		
		// 응답용 객체를 생성하고, jsonView 를 사용
	

		// root패스
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		
		String uploadPath = "";
		
		int fileIdx = 0;
		for(MultipartFile file : fileList) {
			// 파일명은 DOC_ID+REV_ID ex)0000000001_001.ppt
			uploadPath = UPLOAD_PATH + prjDocumentIndexVO.getPrj_id() + DOC_PATH;
			
			String[] getExtension = file.getOriginalFilename().split("\\.");
			String extension = getExtension[getExtension.length-1];
			prjDocumentIndexVO.setFile_type(extension);
			
			int maxDocId = prjdocumentindexService.getMaxDocId(prjDocumentIndexVO);
			prjDocumentIndexVO.setDoc_no(file.getOriginalFilename().split("\\.")[0]);
			String newDocId = String.format("%010d", maxDocId);
			prjDocumentIndexVO.setDoc_id(newDocId);
			String rev_id = "001";
			prjDocumentIndexVO.setRev_id(rev_id);
			String rfile_nm = file.getOriginalFilename();
				
			prjDocumentIndexVO.setTitle(titleList[fileIdx++]);
			prjDocumentIndexVO.setRfile_nm(rfile_nm);
			
			String sfile_nm = newDocId + "_" + rev_id + "." + extension;
			prjDocumentIndexVO.setSfile_nm(sfile_nm);
			
			System.out.println(uploadPath);
			System.out.println(sfile_nm);
			
			// 파일 업로드
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + sfile_nm);
			// 파일 저장 경로에 업로드 수행
			try {
				file.transferTo(uploadTarget);		
				
				long file_size = file.getSize();
				String reg_date = CommonConst.currentDateAndTime();
				prjDocumentIndexVO.setAttach_file_date(reg_date);
				prjDocumentIndexVO.setReg_date(reg_date);
				prjDocumentIndexVO.setFile_size(file_size);
				prjDocumentIndexVO.setDel_yn("N");
				// 업로드 성공 시, DB에 insert
				try {
					if(prjdocumentindexService.fileUpload(prjDocumentIndexVO)<1)
						throw new Exception("DB업로드 실패");
					if(prjdocumentindexService.fileUploadInfoTbl(prjDocumentIndexVO)<1)
						throw new Exception("DB업로드 실패");
				}catch(Exception e1) {
					model.addObject("fail", e1.getMessage());
					model.addObject("error",e1.getStackTrace());
					return model;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			
			
			
		}
		
		model.addObject("status", "SUCCESS");
		
		return model;
	}
	
	
	@RequestMapping(value = "drn_getDocInfo.do")
	@ResponseBody
	public Object drn_getDocInfo(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<Object> result = new ArrayList<Object>();
		
	
		
		return result;
	}

	public String getADMINAndDCCString(PrjMemberVO prjmembervo) {
		List<UsersVO> getAdminList =  usersService.getAdminList();
		List<PrjMemberVO> getPrjDCC = prjinfoService.getPrjDCC(prjmembervo);
		String AdminAndDCC = "";
		for(int i=0;i<getAdminList.size();i++) {
			AdminAndDCC += getAdminList.get(i).getUser_id() + ",";
		}
		for(int i=0;i<getPrjDCC.size();i++) {
			AdminAndDCC += getPrjDCC.get(i).getUser_id() + ",";
		}
		AdminAndDCC = AdminAndDCC.substring(0, AdminAndDCC.length()-1);
		
		return AdminAndDCC;
	}
	
	/**
	 * 1. 메소드명 : getPORInfoList
	 * 2. 작성일: 2022-03-21
	 * 3. 작성자: 소진희
	 * 4. 설명: Procurement, Bulk Property 화면에서 POR NO 설정할수 있도록 콤보박스로 리스트 나타냄
	 * 5. 수정일: 2022-04-05 해당 프로젝트(PRJ NO)에 해당하는 POR LIST만 보기
	 */
	@RequestMapping(value = "getPORInfoList.do")
	@ResponseBody
	public Object getPORInfoList(PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
		prjinfovo.setPrj_no(getPrjInfo.getPrj_no());
		List<PrjDocumentIndexVO> getPORInfoList = porserviceoracle.getPORInfoList(prjinfovo);
		result.add(getPORInfoList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : searchDocByFilter
	 * 2. 작성일: 2022-03-22
	 * 3. 작성자: 소진희
	 * 4. 설명: Document Contorl 페이지에서 Current Version일 때 filter로 도서 검색
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchDocByFilter.do")
	@ResponseBody
	public Object searchDocByFilter(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> searchDocByFilter = prjdocumentindexService.searchDocByFilter(prjdocumentindexvo);
		result.add(searchDocByFilter);
		return result;
	}
	/**
	 * 1. 메소드명 : searchDocByFilterAll
	 * 2. 작성일: 2022-03-22
	 * 3. 작성자: 소진희
	 * 4. 설명: Document Contorl 페이지에서 All Version일 때 filter로 도서 검색
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchDocByFilterAll.do")
	@ResponseBody
	public Object searchDocByFilterAll(PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocumentIndexVO> searchDocByFilterAll = prjdocumentindexService.searchDocByFilterAll(prjdocumentindexvo);
		result.add(searchDocByFilterAll);
		return result;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : docDownGetOptions
	 * 2. 작성일: 2022. 4. 13.
	 * 3. 작성자: doil
	 * 4. 설명: docDownload 페이지에서 folder tree의 아이템과 discipline의 옵션값을 가져오기 위한 함수 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "docDownGetOptions.do")
	@ResponseBody
	public ModelAndView docDownGetOptions(PrjDocumentIndexVO prjdocumentindexvo) {
		ModelAndView result = new ModelAndView();
		
		// 선택된 프로젝트 아이디에 따라 폴더리스트,discip 결정
		String prj_id = prjdocumentindexvo.getPrj_id();
		
		
		List<FolderInfoVO> folderList = prjdocumentindexService.getFolderList4docDown(prj_id);
		List<FolderInfoVO> discipList = prjdocumentindexService.getDiscipList(prj_id);
		
		result.addObject("folderList",folderList);
		result.addObject("discipList",discipList);
		
		

		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : docDownRetrieve
	 * 2. 작성일: 2022. 4. 13.
	 * 3. 작성자: doil
	 * 4. 설명: retrieve 버튼 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "docDownRetrieve.do")
	@ResponseBody
	public ModelAndView docDownRetrieve(PrjDocumentIndexVO prjdocumentindexvo) {
		ModelAndView result = new ModelAndView();

		List<PrjDocumentIndexVO> docList = prjdocumentindexService.getDocList(prjdocumentindexvo);
		result.addObject("docList",docList);

		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : docDown_selectedItemDownload
	 * 2. 작성일: 2022. 4. 13.
	 * 3. 작성자: doil
	 * 4. 설명:  docDown 페이지에서 체크한 아이템들을 다운로드 함
	 * 5. 수정일: doil
	 * @throws IOException 
	 */
	@RequestMapping(value = "docDown_selectedItemDownload.do")
	public void docDown_selectedItemDownload(PrjDocumentIndexVO prjdocumentindexvo , HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		//PrintWriter writer = response.getWriter(); 
		
		List<PrjDocumentIndexVO> selectedDocs = new ArrayList<>();
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		
		String prj_id = prjdocumentindexvo.getPrj_id();
		String prj_nm = prjinfoService.getPrjNm(prj_id);
		
		
		long folder_id = prjdocumentindexvo.getFolder_id();
		String discip = prjdocumentindexvo.getDiscip();
		if(discip.equals("")) {
			// discip을 필터걸지 않은 경우, 폴더아이디에 매칭된 discipline을 가져옴 
			//(해당 폴더에 매핑된 하나의 discipline일 수도 있고, 하위에 여러개가 존재하는 리스트일 수 있음)
			List<String> discipList = prjdocumentindexService.getDiscipListInFolder(prj_id,folder_id);
			/*
			for(String discipDisplay : discipList)
				discip += discipDisplay + ",";
			
			if(discip.length()>0)
				discip = discip.substring(0, discip.length()-1);
			*/
			
			if(discipList.size()==1) {
				discip = discipList.get(0);
			}else if(discipList.size()>1) {
				discip = discipList.get(0) + "("+(discipList.size()-1)+" more)";
			}
			
		} else {
			discip = prjdocumentindexService.getDiscipDisplay(prjdocumentindexvo); 
		}
		
		
		String[] jsonRowDatas = prjdocumentindexvo.getJsonRowDatas();
		String fileNameSet = prjdocumentindexvo.getFileRenameSet();
		if(fileNameSet==null) fileNameSet = "";
		
		// 다운로드를 위해 json 파싱하여 selectedDocs 리스트 생성
		for (String json : jsonRowDatas) {
			try {
				PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
				selectedDocs.add(tmpBean);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		String[] invalidName = {"\\\\","/",":","[*]","[?]","\"","<",">","[|]"};
		
		System.out.printf("다운로드 할 문서의 갯수 : %d\n",selectedDocs.size());
		if(selectedDocs.size() == 1) {
			PrjDocumentIndexVO doc = selectedDocs.get(0);
			String tempPath = "";
			
			String sfile_nm = doc.getSfile_nm();
			String rfile_nm = doc.getRfile_nm();
			String[] renameVar = {"%DOCNO%", "%REVNO%", "%TITLE%"};
			String tmpFilenm = fileNameSet;
			// null의 경우 치환
			String doc_no = doc.getDoc_no();
			if(doc_no == null) doc_no= "";
			String rev_no = doc.getRev_no();
			if(rev_no == null) rev_no= "";
			String title = doc.getTitle();
			if(title == null) title= "";
			// 각각의 rename 변수들에 따라 치환 시켜준다.%DOCNO%, %REVNO%, %TITLE%
			int renameIdx = 0;
			tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],doc_no);
			tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],rev_no);
			tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],title);
			
			
			for(String invalid:invalidName) tmpFilenm = tmpFilenm.replaceAll(invalid, "_");
			tmpFilenm = URLEncoder.encode((tmpFilenm),"UTF-8");
			
			// rename set으로 파일명 치환 후, 확장자를 붙여줘야함
			String ext = doc.getFile_type();
			
			String outFileNm = tmpFilenm + "." + ext;

			try {
				tempPath = UPLOAD_PATH + prj_id + DOC_PATH; // 파일 저장경로
				
				// 파일 다운로드
				response.setContentType("application/zip");
				outFileNm = CommonConst.downloadNameEncoding(request, outFileNm);
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.addHeader("Content-Disposition", "attachment;filename=" + outFileNm);
				
				
				FileInputStream fis = new FileInputStream(tempPath + sfile_nm);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ServletOutputStream so = response.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(so);

				int n = 0;
				byte[] buffer = new byte[1024];
				
				while ((n = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, n);
					bos.flush();
				}

				if (bos != null)
					bos.close();
				if (bis != null)
					bis.close();
				if (so != null)
					so.close();
				if (fis != null)
					fis.close();
				// 파일다운로드 END
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}else if(selectedDocs.size() > 1) { // 선택된 파일의 갯수가 2개이상 - 압축하여 다운로드
			
			// 다운받는 날 기준으로 압축파일명 셋팅
			LocalDateTime now = LocalDateTime.now();
			String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	    	ZipOutputStream zout = null;
	    	
	    	// 파일명 : 프로젝트명_discipline_날짜 형태
	    	//String zipName = "newPEROS_docDownload_"+formatedNow+".zip";		//ZIP 압축 파일명 (실제 다운로드 될 파일)
	    	String zipName = prj_nm+"_"+discip+"_"+formatedNow+".zip";		//ZIP 압축 파일명 (실제 다운로드 될 파일)
	    	zipName = CommonConst.downloadNameEncoding(request, zipName);
			String tempPath = "";
			
			String[] renameVar = {"%DOCNO%", "%REVNO%", "%TITLE%"};
			
			try {
				tempPath = UPLOAD_PATH + prj_id + DOC_PATH; // 파일 저장경로

				// ZIP파일 압축 START
				zout = new ZipOutputStream(new FileOutputStream(tempPath + zipName));
				byte[] buffer = new byte[1024];
				FileInputStream in = null;
				
				// 파일다운로드 START
				response.setContentType("application/zip");
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
				
				// 파일의 갯수 만큼 읽어서 압축파일로 만들어준다.				
				for(PrjDocumentIndexVO file : selectedDocs) {
					String sfile_nm = file.getSfile_nm();
					String rfile_nm = file.getRfile_nm();
					String tmpFilenm = fileNameSet;
					String folder_nm = file.getFolder_path_str();
					
					// null의 경우 치환
					String doc_no = file.getDoc_no();
					if(doc_no == null) doc_no= "";
					String rev_no = file.getRev_no();
					if(rev_no == null) rev_no= "";
					String title = file.getTitle();
					if(title == null) title= "";
					// 각각의 rename 변수들에 따라 치환 시켜준다.%DOCNO%, %REVNO%, %TITLE%
					int renameIdx = 0;
					tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],doc_no);
					tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],rev_no);
					tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],title);
					// '/'는 폴더구분자이므로 치환 필요 (윈도우 파일명으로 쓸 수 없는 파일명들 모두 치환 필요)
					for(String invalid:invalidName) tmpFilenm = tmpFilenm.replaceAll(invalid, "_");
					
					// rename set으로 파일명 치환 후, 확장자를 붙여줘야함
					String ext = file.getFile_type();
					File fileExists = new File(tempPath + sfile_nm);
					//파일이 존재 하지 않을때는 zip에서 제외시킴
					if (fileExists.exists() == true) {
						in = new FileInputStream(tempPath + sfile_nm); // 실제 저장된 압축 대상 파일
						zout.putNextEntry(new ZipEntry(folder_nm+"/"+tmpFilenm+"."+ext)); // 압축파일에 저장시킬 파일명
						int len;
						while ((len = in.read(buffer)) > 0) {
							zout.write(buffer, 0, len); // 읽은 파일을 ZipOutputStream에 Write
						}

						zout.closeEntry();
						in.close();
					}
					
				}

				zout.close();
				// ZIP파일 압축 END
				

				FileInputStream fis = new FileInputStream(tempPath + zipName);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ServletOutputStream so = response.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(so);
				
				int n = 0;
				while ((n = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, n);
					bos.flush();
				}

				if (bos != null)
					bos.close();
				if (bis != null)
					bis.close();
				if (so != null)
					so.close();
				if (fis != null)
					fis.close();
				// 파일다운로드 END

				Path filePath = Paths.get(tempPath + zipName);
				// tempPath폴더에 생성된 zip파일 삭제
				Files.deleteIfExists(filePath);
			} catch (IOException e) {
				// Exception 발생시 에러처리
				e.printStackTrace();
			} finally {
				if (zout != null) {
					zout = null; // zout 소멸되지 않았으면 소멸시켜줌
				}
			}
			
		}else { // 오류
			// 로딩바 제거
			/*
			response.setContentType("text/html; charset=UTF-8");
			writer.println("<script> $('#docDown_loading').remove(); </script>");
			writer.close();
			*/
		}
		
		
	}
	
	
	
}
