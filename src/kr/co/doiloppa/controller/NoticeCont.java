package kr.co.doiloppa.controller;

import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.excel.DownloadView;
import kr.co.doiloppa.model.DictionarySearchVO;
import kr.co.doiloppa.model.NoticeBoardVO;
import kr.co.doiloppa.model.ProjectNoticeVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.NoticeService;
import kr.co.doiloppa.service.SessionService;

@Controller
@RequestMapping("/*")
public class NoticeCont {
	
	protected NoticeService noticeService;
	protected SessionService sessionService;
	
	@Autowired
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	/**
	 * 1. 메소드명 : getNoticeList
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 게시판 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getNoticeList.do")
	@ResponseBody
	public Object getNoticeList() {
		List<Object> result = new ArrayList<Object>();
		List<NoticeBoardVO> getNoticeList = noticeService.getNoticeList();
		result.add(getNoticeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : checkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "checkFile.do")
	@ResponseBody
	public int checkFile(HttpServletRequest request,NoticeBoardVO noticeboardVo) throws Exception{
		int chkFile = 100;
		
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		multipartHttpServletRequest.getMultiFileMap();
		
		List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("files");
		
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
			noticeService.fileUploadNotice(noticeboardVo, request);
		}
		
		return chkFile;
	}
	
	/**
	 * 1. 메소드명 : insertNotice
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 등록시 로그인 중인 사용자 id, ip, 등록일도 같이 저장
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "insertNotice.do")
	@ResponseBody
	public NoticeBoardVO insertNoticeVO(HttpServletRequest request,NoticeBoardVO noticeboardVo) throws Exception{
		
		NoticeBoardVO tmpVo = new NoticeBoardVO();
		
		try {
			String ip = null;
			
			ip = sessionService.getIpAddress(request);
			
			
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			noticeboardVo.setReg_id(sessioninfo.getUser_id());
			noticeboardVo.setRemote_ip(ip);
			if(ip == null || ip.equals("")) {
				noticeboardVo.setRemote_ip(ip);
			}
			noticeboardVo.setReg_date(CommonConst.currentDateAndTime());
			// insert
			tmpVo = noticeService.insertNotice(noticeboardVo);

		}catch(Exception e) {
			tmpVo = null;
		}
		return tmpVo;
		
	}
	
	public static String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");
	    System.out.println("> X-FORWARDED-FOR : " + ip);

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	        System.out.println("> Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	        System.out.println(">  WL-Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	        System.out.println("> HTTP_CLIENT_IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        System.out.println("> HTTP_X_FORWARDED_FOR : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	        System.out.println("> getRemoteAddr : "+ip);
	    }
	    System.out.println("> Result : IP Address : "+ip);

	    return ip;
	}
	
	/**
	 * 1. 메소드명 : updateNotice
	 * 2. 작성일: 2021-12-13
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 수정
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "updateNotice.do")
	@ResponseBody
	public int updateNoticeVO(HttpServletRequest request,NoticeBoardVO noticeboardVo) throws Exception {
		
//		List<Object> result = new ArrayList<Object>();
		int result = 1;      // 업뎃시 0이면 업뎃 실패, 1이면 업뎃 성공
		
		try {
			
			// 로그인 유저 정보
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			noticeboardVo.setMod_id(sessioninfo.getUser_id());
			noticeboardVo.setMod_date(CommonConst.currentDateAndTime());
//			String adminYn = noticeService.checkAdminYn(noticeboardVo).getAdmin_yn();
			
			String userId = noticeService.checkUserId(noticeboardVo).getReg_id();
			String sessionId = sessioninfo.getUser_id();
			String sessionAdminYn = sessioninfo.getAdmin_yn();
//			System.out.println(sessionId);
//			System.out.println(userId);
//			System.out.println(sessionAdminYn);
			
			if(userId.equals(sessionId) || sessionAdminYn.equals("Y")) { // 유저 정보가 등록된 정보랑 같을때 result값 1이상 출력
				result = noticeService.updateNotice(noticeboardVo);
//				System.out.println("유저 수정 : "+result);
			}else {  // 권한이 없으므로 -1 출력
				result = -1;
//				System.out.println("권한 없음 : "+result);
			}
			
//			System.out.println(result);
		}catch(Exception e) {
//			System.out.println(e.getMessage());
			result = 0;
		}
//		System.out.println(result);
		return result;
		
//		System.out.println(chkuser);
		
		// update 전 유저가 관리자인지 확인
//		if(chkuser.equals("Y")) {
//			noticeService.updateNotice(noticeboardVo);
//			
//		}else if(chkid == "") { // 로그인 유저가 등록 유저랑 같은지
//			
//		}else {   // 관리자도 아니고 유저도 다르면 리저트 값 5
//			result = 5;
//		}
		
	}
	
	/**
	 * 1. 메소드명 : findNoticeList
	 * 2. 작성일: 2021-12-08
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 옵션별 검색시 검색결과 리스트
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "findNoticeList.do")
	@ResponseBody
	public Object findNoticeList(NoticeBoardVO noticeboardVo) {
		List<Object> result = new ArrayList<Object>();
		List<NoticeBoardVO> findNoticeList = noticeService.findNoticeList(noticeboardVo);
		result.add(findNoticeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : delNotice
	 * 2. 작성일: 2021-12-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 삭제
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "delNotice.do")
	@ResponseBody
	public int deleteNotice(HttpServletRequest request,NoticeBoardVO noticeboardVo) throws Exception {
//		List<Object> result = new ArrayList<Object>();
		int result = 1;
		
		List<NoticeBoardVO> getDeleteFileListVo = noticeService.getDeleteFileList(noticeboardVo);
		
		String getSfile_nm;
		String getFile_path;
		
		try {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			noticeboardVo.setReg_id(sessioninfo.getUser_id());
//			String adminYn = noticeService.checkAdminYn(noticeboardVo).getAdmin_yn();
			
			String userId = noticeService.checkUserId(noticeboardVo).getReg_id();
			String sessionId = sessioninfo.getUser_id();
			String sessionAdminYn = sessioninfo.getAdmin_yn();
//			System.out.println(sessionId);
//			System.out.println(userId);
//			System.out.println(sessionAdminYn);
			
			if(userId.equals(sessionId) || sessionAdminYn.equals("Y")) { // 유저 정보가 등록된 정보랑 같거나 관리자 확인이 Y일때 result값 1이상 출력
				
				for(int i=0;i < getDeleteFileListVo.size(); i++) {
					getSfile_nm = getDeleteFileListVo.get(i).getSfile_nm();
					getFile_path = getDeleteFileListVo.get(i).getFile_path();
					
					noticeboardVo.setFile_seq(getDeleteFileListVo.get(i).getFile_seq());
					
					File file;
					
					try {
						file = new File(UPLOAD_PATH+getFile_path+getSfile_nm);
						file.delete();
						
						noticeService.deleteFile(noticeboardVo);
						
					}catch(Exception e) {
						System.out.println("에러");						
					}
				}
				
				result = noticeService.deleteNotice(noticeboardVo);
//				System.out.println("유저 수정 : "+result);
			}else {  // 권한이 없으므로 -1 출력
				result = -1;
//				System.out.println("권한 없음 : "+result);
			}
		} catch(Exception e) {
			result=0;
//			System.out.println("에러?");
//			System.out.println(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : getNoticeBoard
	 * 2. 작성일: 2021-12-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 상세페이지, 게시판 클릭시 횟수 저장
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getNoticeBoard.do")
	@ResponseBody
	public Object updateHit(NoticeBoardVO noticeboardVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		// 조회수 update
		noticeService.updateHit(noticeboardVo);
		NoticeBoardVO getNoticeBoard = noticeService.getNoticeBoard(noticeboardVo);
		result.add(getNoticeBoard);
		return result;
		
	}
	
	/**
	 * 1. 메소드명 : getNoticeBoardUpdate
	 * 2. 작성일: 2021-12-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 수정페이지 데이터 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getNoticeBoardUpdate.do")
	@ResponseBody
	public Object getNoticeBoardUpdate(NoticeBoardVO noticeboardVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		NoticeBoardVO getNoticeBoard = noticeService.getNoticeBoard(noticeboardVo);
		result.add(getNoticeBoard);
		return result;
		
	}
	
//	/**
//	 * 1. 메소드명 : chkUpNotice
//	 * 2. 작성일: 2021-12-29
//	 * 3. 작성자: 박정우
//	 * 4. 설명: 게시판 수정페이지 권한
//	 * 5. 수정일: 
//	 */
	
	@RequestMapping(value = "chkUpNotice.do")
	@ResponseBody
	public int chkUpNotice(HttpServletRequest request,NoticeBoardVO noticeboardVo) throws Exception {
		int result = 1;
		
		try {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			noticeboardVo.setReg_id(sessioninfo.getUser_id());
//			String adminYn = noticeService.checkAdminYn(noticeboardVo).getAdmin_yn();
			
			String userId = noticeService.checkUserId(noticeboardVo).getReg_id();
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
	 * 1. 메소드명 : getDetailFileList
	 * 2. 작성일: 2021-01-14
	 * 3. 작성자: 박정우
	 * 4. 설명: 저장된 파일 리스트 가져오기
	 * 5. 수정일: 
	 */ 
	
	@RequestMapping(value = "getDetailFileList.do")
	@ResponseBody
	public Object getDetailFileList(NoticeBoardVO noticeboardVo) {
		List<Object> result = new ArrayList<Object>();
		List<NoticeBoardVO> getDetailFileList = noticeService.getNoticeFilelist(noticeboardVo);
		result.add(getDetailFileList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : estimateDownloadN
	 * 2. 작성일: 2021-01-14
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 파일 다운
	 * 5. 수정일: 
	 */
	@Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
	
	@RequestMapping(value = "estimateDownloadN.do")
	public ModelAndView estimateDownload(NoticeBoardVO noticeboardVo) {
		
		NoticeBoardVO getFilePathNoticeVo = noticeService.getFilePathNotice(noticeboardVo);
		
		String filePath = getFilePathNoticeVo.getFile_path();
		
		String sfileNm = noticeboardVo.getSfile_nm();
		String rfileNm = noticeboardVo.getRfile_nm();
		
		File file = new File(UPLOAD_PATH+filePath+sfileNm);
		DownloadView.setFileName(rfileNm);		
		return new ModelAndView("fileDownload","downloadFile", file);
	}
	
	/**
	 * 1. 메소드명 : deleteFile
	 * 2. 작성일: 2021-01-14
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 추가
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "deleteFileN.do")
	@ResponseBody
	public String deleteFile(NoticeBoardVO noticeboardVo) throws Exception {
		
		NoticeBoardVO getDeleteFileVo = noticeService.getDeleteFile(noticeboardVo);
		
		String filePath = getDeleteFileVo.getFile_path();
		String fileSnm = getDeleteFileVo.getSfile_nm();
		
		File file;
		
		try {
			file = new File(UPLOAD_PATH+filePath+fileSnm);
			file.delete();
			
			noticeService.deleteFile(noticeboardVo);
		}catch(Exception e) {
			System.out.println("에러");
		}
		
		return "";
	}
}
	
