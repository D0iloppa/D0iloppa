package kr.co.doiloppa.controller;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.SecurityLogVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.model.WbsCodeControlVO;
import kr.co.doiloppa.service.PrjAccessService;
import kr.co.doiloppa.service.SecurityLogService;
import kr.co.doiloppa.service.SessionService;

@Controller
@RequestMapping("/*")
public class PrjAccessCont {

	protected SecurityLogService securityLogService;	
	protected PrjAccessService prjAccessService;
	protected SessionService sessionService;
	
	@Autowired
	public void setSecurityLogService(SecurityLogService securityLogService) {
		this.securityLogService = securityLogService;
	}
	
	@Autowired
	public void setPrjAccessService(PrjAccessService prjAccessService) {
		this.prjAccessService = prjAccessService;
	}
	
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
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
	 * 1. 메소드명 : insertAccHis
	 * 2. 작성일: 2022-02-18
	 * 3. 작성자: 박정우
	 * 4. 설명: 로그인 중인 사용자 id, ip, 등록일도 같이 저장
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "insertAccHis.do")
	@ResponseBody
	public SecurityLogVO insertAccHis(HttpServletRequest request,SecurityLogVO securityLogVo) throws Exception{
		
		SecurityLogVO tmpVo = new SecurityLogVO();
		
		try {
			String ip = null;
			ip = sessionService.getIpAddress(request);
//			try {
//				System.out.println(Inet4Address.getLocalHost().getHostAddress());
//				ip = String.valueOf(Inet4Address.getLocalHost().getHostAddress());
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			System.out.println(ip);
			if(securityLogVo.getDoc_id() != "" || securityLogVo.getDoc_id() != null) {
				HttpSession session = request.getSession();
				UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
				securityLogVo.setReg_id(sessioninfo.getUser_id());
				securityLogVo.setReg_nm(sessioninfo.getUser_kor_nm());
				securityLogVo.setRemote_ip(ip);
				securityLogVo.setReg_date(CommonConst.currentDateAndTime());
				// prj_no, prj_nm 가져오기
				String prjNoVo = prjAccessService.getAccPrjNoNm(securityLogVo).getPrj_no();
				String prjNmVo = prjAccessService.getAccPrjNoNm(securityLogVo).getPrj_nm();
				securityLogVo.setPrj_nm(prjNmVo);
				securityLogVo.setPrj_no(prjNoVo);
				// folder_path 가져오기
				SecurityLogVO folderPathVo = prjAccessService.getAccFolderPath(securityLogVo);
				//securityLogVo.setFolder_path(folderPathVo.getFolder_path());
				
				// 폴더 Path 1>2 에서 DEFAULT PROJECT > CORRESPONDENCE 식으로 변환
				String fileNm = "";
				String[] arrayFilePath = folderPathVo.getFolder_path().split(">");
				for(int j=0; j<arrayFilePath.length; j++) {
					securityLogVo.setFolder_id(Long.parseLong(arrayFilePath[j]));
					fileNm += securityLogService.getFolder_nm(securityLogVo).getFolder_nm()+" > ";
				}
				fileNm = fileNm.substring(0, fileNm.length()-2);
				
				securityLogVo.setFolder_path(fileNm);
				// insert
				prjAccessService.insertAccHis(securityLogVo);
						
//				System.out.println(result);
			}
		}catch(Exception e) {
//			System.out.println(e.getMessage());
			tmpVo = null;
		}
		return tmpVo;
		
	}
	
	/**
	 * 1. 메소드명 : insertAccHisList
	 * 2. 작성일: 2022-02-18
	 * 3. 작성자: 박정우
	 * 4. 설명: 로그인 중인 사용자 id, ip, 등록일도 같이 저장
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "insertAccHisList.do")
	@ResponseBody
	public void insertAccHisList(HttpServletRequest request, SecurityLogVO securityLogVo) throws Exception{
		
		try {
			String ip = null;
			ip = sessionService.getIpAddress(request);
//			try {
//				System.out.println(Inet4Address.getLocalHost().getHostAddress());
//				ip = String.valueOf(Inet4Address.getLocalHost().getHostAddress());
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			System.out.println(ip);
			
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			
			//ystem.out.println("JSON 데이타 = "+prjDocumentIndexVo.getJsonRowDatas());
			
			String[] jsonDatas = securityLogVo.getJsonRowDatas();
			
			if (jsonDatas != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					// json 데이타 하나씩 꺼내서 사용
					for(String json : jsonDatas) {
//						SecurityLogVO tmpBean = objectMapper.readValue(json, SecurityLogVO.class);
						PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
						if(tmpBean.getDoc_id() != "" || tmpBean.getDoc_id() != null) {
							
							securityLogVo.setReg_id(sessioninfo.getUser_id());
							securityLogVo.setReg_nm(sessioninfo.getUser_kor_nm());
							securityLogVo.setRemote_ip(ip);
							securityLogVo.setReg_date(CommonConst.currentDateAndTime());
							
							securityLogVo.setDoc_id(tmpBean.getDoc_id());
							securityLogVo.setRev_id(tmpBean.getRev_id());
							securityLogVo.setDoc_no(tmpBean.getDoc_no());
							securityLogVo.setFolder_id(tmpBean.getFolder_id());
							securityLogVo.setDoc_title(tmpBean.getTitle());
							securityLogVo.setFile_size(tmpBean.getFile_size());
							// prj_no, prj_nm 가져오기
							String prjNoVo = prjAccessService.getAccPrjNoNm(securityLogVo).getPrj_no();
							String prjNmVo = prjAccessService.getAccPrjNoNm(securityLogVo).getPrj_nm();
							securityLogVo.setPrj_nm(prjNmVo);
							securityLogVo.setPrj_no(prjNoVo);
							// folder_path 가져오기
							SecurityLogVO folderPathVo = prjAccessService.getAccFolderPath(securityLogVo);
							//securityLogVo.setFolder_path(folderPathVo.getFolder_path());
							
							// 폴더 Path 1>2 에서 DEFAULT PROJECT > CORRESPONDENCE 식으로 변환
							String fileNm = "";
							String[] arrayFilePath = folderPathVo.getFolder_path().split(">");
							for(int j=0; j<arrayFilePath.length; j++) {
								securityLogVo.setFolder_id(Long.parseLong(arrayFilePath[j]));
								fileNm += securityLogService.getFolder_nm(securityLogVo).getFolder_nm()+" > ";
							}
							fileNm = fileNm.substring(0, fileNm.length()-2);
							
							securityLogVo.setFolder_path(fileNm);
							// insert
							prjAccessService.insertAccHis(securityLogVo);
						}
					}
				}catch(Exception e) {
					System.out.println(e.getMessage());		
				}
			}
		}catch(Exception e) {
//			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * 1. 메소드명 : getAccHisList
	 * 2. 작성일: 2022-02-18
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 게시판 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getAccHisList.do")
	@ResponseBody
	public Object getAccHisList(SecurityLogVO securityLogVo) {
		List<Object> result = new ArrayList<Object>();
		List<SecurityLogVO> getAccHisList = prjAccessService.getAccHisList(securityLogVo);
		result.add(getAccHisList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : accessExcelDownload
	 * 2. 작성일: 2022. 02. 20.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "accessExcelDownload.do")
	@ResponseBody
	public void accessExcelDownload(SecurityLogVO securityLogVo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		List<SecurityLogVO> getAccesscodeList = prjAccessService.getAccHisList(securityLogVo);
		System.out.println(getAccesscodeList);
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.accessExcelDownload(response, "titleList",  getAccesscodeList, securityLogVo.getFileName());
	}
}
