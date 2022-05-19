package kr.co.hhi.controller;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.CommandMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.FileExtFilter;
import kr.co.hhi.excel.DownloadView;
import kr.co.hhi.model.NoticeBoardVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.ProjectNoticeVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.ProjectNoticeService;
import kr.co.hhi.service.SessionService;

@Controller
@RequestMapping("/*")
public class ProjectNoticeCont {
	
	// app.properties에 있는 upload path
    @Value("#{config['file.upload.path']}")
	private static String UPLOAD_ROOT_PATH;
    // 템플릿 저장 위치
    @Value("#{config['file.upload.prj.template.path']}")
	private String TEMPLATE_PATH;
	
	protected ProjectNoticeService projectnoticeService;
	protected SessionService sessionService;
	protected PrjInfoService prjInfoService;
	
	@Autowired
	public void setProjectNoticeService(ProjectNoticeService projectnoticeService) {
		this.projectnoticeService = projectnoticeService;
	}
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjInfoService) {
		this.prjInfoService = prjInfoService;
	}
	/**
	 * 1. 메소드명 : getPrjName
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 박정우
	 * 4. 설명: 프로젝트 명을 가져옴
	 * 5. 수정일: 
	 */
	
//	@RequestMapping(value = "getPrjName.do")
//	@ResponseBody
//	public Object getPrjName(ProjectNoticeVO projectnoticeVo) {
//		List<Object> result = new ArrayList<Object>();
//		List<ProjectNoticeVO> getPrjName = projectnoticeService.getPrjName(projectnoticeVo);
//		result.add(getPrjName);
//		return result;
//	}
	
	/**
	 * 1. 메소드명 : getProjectNoticeList
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 게시판 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getProjectNoticeList.do")
	@ResponseBody
	public Object getProjectNoticeList(ProjectNoticeVO projectnoticeVo) {
		List<Object> result = new ArrayList<Object>();
		List<ProjectNoticeVO> getProjectNoticeList = projectnoticeService.getProjectNoticeList(projectnoticeVo);
		result.add(getProjectNoticeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : prjNoticeCheckFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "prjNoticeCheckFile.do")
	@ResponseBody
	public int prjNoticeCheckFile(MultipartHttpServletRequest mtRequest, HttpServletRequest request, ProjectNoticeVO projectnoticeVo) throws Exception{
		int chkFile = 100;
		
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		multipartHttpServletRequest.getMultiFileMap();
		
		List<MultipartFile> fileList = multipartHttpServletRequest.getFiles("files");
		
		for(MultipartFile mf : fileList) {
			File file = new File(mf.getOriginalFilename());
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChkNotice = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChkNotice = FileExtFilter.whiteFileExtIsReturnBoolean(file);
			
			if(fileUploadChkNotice == true) {
				chkFile = 100;
			}else if(fileUploadChkNotice == false) {
				chkFile = 0;
			}
			
			//System.out.println("파일 명 = "+file);
			//System.out.println("파일 확인 true or false = "+fileUploadChkNotice);
		}
		
		if(chkFile == 100) {
			projectnoticeService.fileUploadPrjNotice(projectnoticeVo, request);
		}
		
		return chkFile;
	}
	
//	/**
//	 * 1. 메소드명 : findNoticeList
//	 * 2. 작성일: 2021-12-15
//	 * 3. 작성자: 박정우
//	 * 4. 설명: 게시판 리스트 검색
//	 * 5. 수정일: 
//	 */
	@RequestMapping(value = "findProjectNotice.do")
	@ResponseBody
	public Object findProjectNoticeList(ProjectNoticeVO projectnoticeVo) {
		List<Object> result = new ArrayList<Object>();
		List<ProjectNoticeVO> findProjectNoticeList = projectnoticeService.findNoticeList(projectnoticeVo);
		result.add(findProjectNoticeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getPrjNoticeBoard
	 * 2. 작성일: 2021-12-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 상세페이지, 게시판 클릭시 횟수 저장
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getPrjNoticeBoard.do")
	@ResponseBody
	public Object updateHit(ProjectNoticeVO projectnoticeVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		// 조회수 update
		projectnoticeService.hitcntUp(projectnoticeVo);
		ProjectNoticeVO getPrjNoticeBoard = projectnoticeService.getPrjNoticeBoard(projectnoticeVo);
		result.add(getPrjNoticeBoard);
		return result;
		
	}
	
	/**
	 * 1. 메소드명 : getPrjNoticeBoardUpdate
	 * 2. 작성일: 2021-12-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 수정페이지 데이터 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getPrjNoticeBoardUpdate.do")
	@ResponseBody
	public Object getPrjNoticeBoardUpdate(ProjectNoticeVO projectnoticeVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		ProjectNoticeVO getPrjNoticeBoard = projectnoticeService.getPrjNoticeBoard(projectnoticeVo);
		result.add(getPrjNoticeBoard);
		return result;
		
	}
	
	
	/**
	 * 1. 메소드명 : getDetailPrjFileList
	 * 2. 작성일: 2021-01-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 게시판 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getDetailPrjFileList.do")
	@ResponseBody
	public Object getDetailPrjFileList(ProjectNoticeVO projectnoticeVo) {
		List<Object> result = new ArrayList<Object>();
		List<ProjectNoticeVO> getDetailPrjFileList = projectnoticeService.getPrjNoticeFileList(projectnoticeVo);
		result.add(getDetailPrjFileList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : estimateDownload
	 * 2. 작성일: 2021-01-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 파일 다운
	 * 5. 수정일: 
	 */
	@Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
	
	@RequestMapping(value = "estimateDownload.do")
	public ModelAndView estimateDownload(@RequestParam(value = "fullPath", required = false) String fullPath,@RequestParam(value = "fileName", required = false) String fileName, ProjectNoticeVO projectnoticeVo) {
//		System.out.println(fullPath+"/"+fileName);
//		C:/vdsFileupload/0000000001/noticePrj/af68c987a9a74a79b439d2b40e735e37 / data.xlsx
		
		ProjectNoticeVO getFilePath = projectnoticeService.getFilePath(projectnoticeVo);
		
		String chkPath = getFilePath.getFile_path();
		
//		System.out.println(chkPrj);
//		0000000001
		
		File file = new File(UPLOAD_PATH+chkPath+fullPath);
		DownloadView.setFileName(fileName);
		return new ModelAndView("fileDownload","downloadFile", file);
	}
	
//	/**
//	 * 1. 메소드명 : insertPrjNotice
//	 * 2. 작성일: 2021-12-24
//	 * 3. 작성자: 박정우
//	 * 4. 설명: 게시판 추가
//	 * 5. 수정일: 
//	 */
	
	@RequestMapping(value = "insertPrjNotice.do")
	@ResponseBody
	public ProjectNoticeVO insertPrjNotice(HttpServletRequest request, ProjectNoticeVO projectnoticeVo) throws Exception {
		
		ProjectNoticeVO tempVo = new ProjectNoticeVO();
		
		try {
			String ip = sessionService.getIpAddress(request);
			

					
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			projectnoticeVo.setReg_id(sessioninfo.getUser_id());
			projectnoticeVo.setRemote_ip(ip);
			if(ip == null || ip.equals("")) {
				projectnoticeVo.setRemote_ip(ip);
			}
			projectnoticeVo.setReg_date(CommonConst.currentDateAndTime());
			
			tempVo = projectnoticeService.insertPrjNotice(projectnoticeVo);
			
			
		} catch(Exception e) {
			tempVo = null;
		}
		return tempVo;
		
	}
	
	public static String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");
	    System.out.println("> X-FORWARDED-FOR : " + ip);

	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	        System.out.println("> Proxy-Client-IP : " + ip);
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	        System.out.println(">  WL-Proxy-Client-IP : " + ip);
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	        System.out.println("> HTTP_CLIENT_IP : " + ip);
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        System.out.println("> HTTP_X_FORWARDED_FOR : " + ip);
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	        System.out.println("> getRemoteAddr : "+ip);
	    }
	    System.out.println("> Result : IP Address : "+ip);

	    return ip;
	}
	
//	/**
//	 * 1. 메소드명 : updatePrjNotice
//	 * 2. 작성일: 2021-12-15
//	 * 3. 작성자: 박정우
//	 * 4. 설명: 게시판 상세페이지 닫을때 조회수 업데이트
//	 * 5. 수정일: 
//	 */
	
	@RequestMapping(value = "updatePrjNotice.do")
	@ResponseBody
	public int updatePrjNotice(HttpServletRequest request, ProjectNoticeVO projectnoticeVo) throws Exception {
		int result = 1;	// 값이 1이상이면 성공, 0이면 실패
		
		try {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			projectnoticeVo.setMod_id(sessioninfo.getUser_id());
			projectnoticeVo.setMod_date(CommonConst.currentDateAndTime());
//			String adminYn = noticeService.checkAdminYn(noticeboardVo).getAdmin_yn();
			
			String userId = projectnoticeService.checkPrjUserId(projectnoticeVo).getReg_id();
			String sessionId = sessioninfo.getUser_id();
			String sessionAdminYn = sessioninfo.getAdmin_yn();
//			System.out.println(sessionId);
//			System.out.println(userId);
//			System.out.println(sessionAdminYn);
			
//			List<ProjectNoticeVO> findProjectNoticeList = projectnoticeService.findNoticeList(projectnoticeVo);
			
			if(userId.equals(sessionId) || sessionAdminYn.equals("Y")) { // 유저 정보가 등록된 정보랑 같거나 관리자 확인이 Y일때 result값 1이상 출력
				result = projectnoticeService.updatePrjNotice(projectnoticeVo);
			}else {
				result= -1;
			}
			
//			System.out.println(result);
		} catch(Exception e) {
			result=0;
//			System.out.println(e.getMessage());
		}
		return result;
		
	}
	
//	/**
//	 * 1. 메소드명 : deletePrjNotice
//	 * 2. 작성일: 2021-12-24
//	 * 3. 작성자: 박정우
//	 * 4. 설명: 게시판 삭제
//	 * 5. 수정일: 
//	 */
	
	@RequestMapping(value = "deletePrjNotice.do")
	@ResponseBody
	public int deletePrjNotice(HttpServletRequest request, ProjectNoticeVO projectnoticeVo) {
				
		int result = 1;
		
		List<ProjectNoticeVO> getDeleteFileList = projectnoticeService.getDeleteFileList(projectnoticeVo);
		
		String getSfile_nm;
		String getFile_path;
		
		try {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			projectnoticeVo.setReg_id(sessioninfo.getUser_id());
//			String adminYn = noticeService.checkAdminYn(noticeboardVo).getAdmin_yn();
			
			String userId = projectnoticeService.checkPrjUserId(projectnoticeVo).getReg_id();
			String sessionId = sessioninfo.getUser_id();
			String sessionAdminYn = sessioninfo.getAdmin_yn();
//			System.out.println(sessionId);
//			System.out.println(userId);
//			System.out.println(sessionAdminYn);
			
			if(userId.equals(sessionId) || sessionAdminYn.equals("Y")) { // 유저 정보가 등록된 정보랑 같거나 관리자 확인이 Y일때 result값 1이상 출력
				
				for(int i=0;i < getDeleteFileList.size();i++) {
//					System.out.println("i : "+i);
					getSfile_nm = getDeleteFileList.get(i).getSfile_nm();
					getFile_path = getDeleteFileList.get(i).getFile_path();
					
					projectnoticeVo.setFile_seq(getDeleteFileList.get(i).getFile_seq());
					
					File file;
					
					try {
						file = new File(UPLOAD_PATH+getFile_path+getSfile_nm);
						file.delete();
						
						projectnoticeService.deleteFileList(projectnoticeVo, request);
					}catch(Exception e) {
						System.out.println("에러");
					}									
				}
				
				result = projectnoticeService.deletePrjNotice(projectnoticeVo);
			}else {
				result=-1;
			}
			
//			System.out.println(result);
		} catch(Exception e) {
			
			result=0;
//			System.out.println(e.getMessage());
		}
		return result;
		
	}
	
	/**
	 * 1. 메소드명 : chkPrjUpNotice
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 수정페이지 권한
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "chkPrjUpNotice.do")
	@ResponseBody
	public int chkPrjUpNotice(HttpServletRequest request, ProjectNoticeVO projectnoticeVo) throws Exception {
		int result = 1;
		
		try {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			projectnoticeVo.setReg_id(sessioninfo.getUser_id());
//			String adminYn = noticeService.checkAdminYn(noticeboardVo).getAdmin_yn();
			
			String userId = projectnoticeService.checkPrjUserId(projectnoticeVo).getReg_id();
			String sessionId = sessioninfo.getUser_id();
			String sessionAdminYn = sessioninfo.getAdmin_yn();
			
			if(userId.equals(sessionId) || sessionAdminYn.equals("Y")) { // 유저 정보가 등록된 정보랑 같거나 관리자 확인이 Y일때 result값 1출력
				result = 1;
			}else {
				result=-1;
			}
			
		} catch(Exception e) {
			
			result=0;
//			System.out.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : checkAdminDccPrjNotice
	 * 2. 작성일: 2022-03-21
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 전체 공지 권한
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "checkAdminDccPrjNotice.do")
	@ResponseBody
	public int checkAdminDccPrjNotice(HttpServletRequest request,UsersVO usersvo, ProjectNoticeVO projectnoticeVo, PrjMemberVO prjmembervo) throws Exception {
		int result = 1;
		
		try {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			projectnoticeVo.setReg_id(sessioninfo.getUser_id());

			String sessionId = sessioninfo.getUser_id();
			UsersVO getAdminChkYN = sessionService.getAdminChkYN(usersvo);
			String sessionAdminYn =  getAdminChkYN.getAdmin_yn();
			prjmembervo.setDiff_dcc_user(sessionId);
			prjmembervo.setPrj_id(projectnoticeVo.getPrj_id());
			
			PrjMemberVO dccYn = prjInfoService.getPrjDCCYN(prjmembervo);
			
			if(sessionAdminYn != null && sessionAdminYn.equals("Y")) { // 관리자,DCC 확인이 Y일때 result값 1이상 출력
				result = 1;
			}else if(dccYn != null && dccYn.getDcc_yn().equals("Y")) {
				result = 2;
			}else {
				result=-1;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			result=0;
//			System.out.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteFile
	 * 2. 작성일: 2021-01-13
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 추가
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "deleteFile.do")
	@ResponseBody
	public String deleteFile(HttpServletRequest request, ProjectNoticeVO projectnoticeVo) throws Exception {
		
		ProjectNoticeVO getDeleteFile = projectnoticeService.getDeleteFile(projectnoticeVo);
		
		String filePath = getDeleteFile.getFile_path();
		String fileSnm = getDeleteFile.getSfile_nm();
		
//		System.out.println(getDeleteFile.getSfile_nm());
		
		File file;
		
		try {
			file = new File(UPLOAD_PATH+filePath+fileSnm);
			file.delete();
			
			projectnoticeService.deleteFile(projectnoticeVo, request);
		}catch(Exception e) {
			System.out.println("에러");
		}
		
		
		return "";
	}
	
	
	
}
