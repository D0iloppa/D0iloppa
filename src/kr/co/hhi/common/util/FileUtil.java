package kr.co.hhi.common.util;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.UsersVO;


@Component("fileUtil")
public class FileUtil {
	
	// app.properties에 있는 upload path
    @Value("#{config['file.upload.path']}")
	static String UPLOAD_ROOT_PATH;

    @Value("#{config['file.upload.path']}")
    private void setUploadRoot(String path) {
    	UPLOAD_ROOT_PATH = path;
    }
    
    public String getUploadRoot() {
    	return UPLOAD_ROOT_PATH;
    }
    
    // app.properties에 있는 Notice board
    @Value("#{config['file.upload.notice.path']}")
	static String UPLOAD_ROOT_Notice;

    @Value("#{config['file.upload.notice.path']}")
    private void setUploadRootNotice(String path) {
    	UPLOAD_ROOT_Notice = path;
    }
    
    public String getUploadRootNotice() {
    	return UPLOAD_ROOT_Notice;
    }
    
 // app.properties에 있는 Notice board img
    @Value("#{config['file.upload.noticeImg.path']}")
	static String UPLOAD_ROOT_NoticeImg;

    @Value("#{config['file.upload.noticeImg.path']}")
    private void setUploadRootNoticeImg(String path) {
    	UPLOAD_ROOT_NoticeImg = path;
    }
    
    public String getUploadRootNoticeImg() {
    	return UPLOAD_ROOT_NoticeImg;
    }
    
    // app.properties에 있는 Project Notice board
    @Value("#{config['file.upload.prj.notice.path']}")
	static String UPLOAD_ROOT_ProjectN;

    @Value("#{config['file.upload.prj.notice.path']}")
    private void setUploadRootProject(String path) {
    	UPLOAD_ROOT_ProjectN = path;
    }
    
    public String getUploadRootProject() {
    	return UPLOAD_ROOT_ProjectN;
    }
    
    // app.properties에 있는 Project Notice board
    @Value("#{config['file.upload.prj.noticeImg.path']}")
	static String UPLOAD_ROOT_ProjectNimg;

    @Value("#{config['file.upload.prj.noticeImg.path']}")
    private void setUploadRootProjectImg(String path) {
    	UPLOAD_ROOT_ProjectNimg = path;
    }
    
    public String getUploadRootProjectImg() {
    	return UPLOAD_ROOT_ProjectNimg;
    }
    
    // 템플릿 저장 위치
    @Value("#{config['file.upload.prj.template.path']}")
	private String TEMPLATE_PATH;
	
	private static final String filePath = UPLOAD_ROOT_PATH;
//	private static final String filePath = "C:\\test\\file\\";

	public static List<Map<String, Object>> parseInsertFileInfo_old(String getVo, HttpServletRequest request)
			throws Exception {
		
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		String[] getVoList = getVo.split(",");
		String rfile_nm = null;
		String originalFileExtension = null;
		String sfile_nm = null;
		String reg_id = null;
		String reg_date = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		File file = new File(UPLOAD_ROOT_PATH+getVoList[1]+"/noticePrj/"+"/"+(CommonConst.currentDate())+"/");
		if (file.exists() == false) {
			file.mkdirs();
		}
		while (iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			if (multipartFile.isEmpty() == false) {
				rfile_nm = multipartFile.getOriginalFilename();
				originalFileExtension = rfile_nm.substring(rfile_nm.lastIndexOf("."));
				sfile_nm = CommonUtil.getRandomString();
				reg_id = sessioninfo.getUser_id();
				reg_date = CommonConst.currentDateAndTime();
				file = new File((UPLOAD_ROOT_PATH+getVoList[1]+"/noticePrj/"+"/"+(CommonConst.currentDate())+"/") + sfile_nm);
				multipartFile.transferTo(file);
				listMap = new HashMap<String, Object>();
				listMap.put("file_path", filePath+getVoList[1]+"/noticePrj/"+"/"+(CommonConst.currentDate())+"/");
				listMap.put("bbs_seq", getVoList[0]);
				listMap.put("prj_id", getVoList[1]);
				listMap.put("rfile_nm", rfile_nm);
				listMap.put("sfile_nm", sfile_nm);
				listMap.put("file_size", multipartFile.getSize());
				listMap.put("reg_id", reg_id);
				listMap.put("reg_date", reg_date);
				list.add(listMap);
			}
		}
		return list;
	}
	
	public static List<Map<String, Object>> parseInsertFileInfo(String getVo, HttpServletRequest request)
			throws Exception {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		multipartHttpServletRequest.getMultiFileMap();
		
		String[] getVoList = getVo.split(",");
		String rfile_nm = null;
		String originalFileExtension = null;
		String sfile_nm = null;
		String reg_id = null;
		String reg_date = null;
		String mod_id = null;
		String mod_date = null;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;
		
		List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("files");
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			
		File file = new File(UPLOAD_ROOT_PATH+getVoList[1]+UPLOAD_ROOT_ProjectN+(CommonConst.currentDate())+"/");
		if (file.exists() == false) { // 폴더 경로 없으면 새로 생성
			file.mkdirs();
		}
		
		for (MultipartFile mf : fileList) {		
				rfile_nm = mf.getOriginalFilename();
				originalFileExtension = rfile_nm.substring(rfile_nm.lastIndexOf("."));
				sfile_nm = CommonUtil.getRandomString();
				reg_id = sessioninfo.getUser_id();
				reg_date = CommonConst.currentDateAndTime();
				mod_id = sessioninfo.getUser_id();
				mod_date = CommonConst.currentDateAndTime();
				
//				if(originalFileExtension.equals(".png") || originalFileExtension.equals(".jpg")) {
//					file = new File((UPLOAD_ROOT_PATH+getVoList[1]+UPLOAD_ROOT_ProjectNimg+(CommonConst.currentDate())+"/") + sfile_nm);
//				}
				file = new File((UPLOAD_ROOT_PATH+getVoList[1]+UPLOAD_ROOT_ProjectN+(CommonConst.currentDate())+"/") + sfile_nm);
				
				mf.transferTo(file);
				listMap = new HashMap<String, Object>();
				
//				if(originalFileExtension.equals(".png") || originalFileExtension.equals(".jpg")) {
//					listMap.put("file_path", "/"+getVoList[1]+UPLOAD_ROOT_ProjectNimg+(CommonConst.currentDate())+"/");
//				}
				listMap.put("file_path", "/"+getVoList[1]+UPLOAD_ROOT_ProjectN+(CommonConst.currentDate())+"/");
				
				listMap.put("bbs_seq", getVoList[0]);
				listMap.put("prj_id", getVoList[1]);
				listMap.put("rfile_nm", rfile_nm);
				listMap.put("sfile_nm", sfile_nm);
				listMap.put("file_size", mf.getSize());
				listMap.put("reg_id", reg_id);
				listMap.put("reg_date", reg_date);
				listMap.put("mod_id", mod_id);
				listMap.put("mod_date", mod_date);
				list.add(listMap);
			}
		//}
		return list;
	}
	
	public static List<Map<String, Object>> parseInsertFileInfoNotice(String getVo, HttpServletRequest request)
			throws Exception {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		multipartHttpServletRequest.getMultiFileMap();
		
//		String getVO = getVo;
		String rfile_nm = null;
		String originalFileExtension = null;
		String sfile_nm = null;
		String reg_id = null;
		String reg_date = null;
		String mod_id = null;
		String mod_date = null;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;
		
		List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("files");
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		File file = new File(UPLOAD_ROOT_PATH+UPLOAD_ROOT_Notice+(CommonConst.currentDate())+"/");
		if(file.exists() == false) {
			file.mkdirs();
		}
		
		for(MultipartFile mf : fileList) {
			rfile_nm = mf.getOriginalFilename();
			originalFileExtension = rfile_nm.substring(rfile_nm.lastIndexOf("."));
			sfile_nm = CommonUtil.getRandomString();
			reg_id = sessioninfo.getUser_id();
			reg_date = CommonConst.currentDateAndTime();
			mod_id = sessioninfo.getUser_id();
			mod_date = CommonConst.currentDateAndTime();
			
//			if(originalFileExtension.equals(".png") || originalFileExtension.equals(".jpg")) {
//				file = new File(UPLOAD_ROOT_PATH+UPLOAD_ROOT_NoticeImg+(CommonConst.currentDate())+"/"+sfile_nm);
//			}
			file = new File(UPLOAD_ROOT_PATH+UPLOAD_ROOT_Notice+(CommonConst.currentDate())+"/"+sfile_nm);
			
			mf.transferTo(file);
			listMap = new HashMap<String, Object>();
//			if(originalFileExtension.equals(".png") || originalFileExtension.equals(".jpg")) {
//				listMap.put("file_path", UPLOAD_ROOT_NoticeImg+(CommonConst.currentDate())+"/");
//			}
			listMap.put("file_path", UPLOAD_ROOT_Notice+(CommonConst.currentDate())+"/");

			listMap.put("bbs_seq", getVo);
			listMap.put("rfile_nm", rfile_nm);
			listMap.put("sfile_nm", sfile_nm);
			listMap.put("file_size", mf.getSize());
			listMap.put("reg_id", reg_id);
			listMap.put("reg_date", reg_date);
			listMap.put("mod_id", mod_id);
			listMap.put("mod_date", mod_date);
			list.add(listMap);
		}
		
		return list;
	}
	
//	public static boolean parseDeleteFileInfo(String getVo, HttpServletRequest request)
//			throws Exception {
//		
//		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
//		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
//		MultipartFile multipartFile = null;
//		multipartHttpServletRequest.getMultiFileMap();
//		
//		String[] getVoList = getVo.split(",");
//		
//		File file = new File(filePath+getVoList[1]+"/noticePrj/"+(CommonConst.currentDate())+"/");
//		
//		return false;				
//		
//	}
	
}
