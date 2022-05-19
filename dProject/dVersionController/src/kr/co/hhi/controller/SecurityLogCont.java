package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.excel.ExcelFileDownload;
import kr.co.hhi.model.SecurityLogVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjAccessService;
import kr.co.hhi.service.SecurityLogService;
import kr.co.hhi.service.SessionService;

@Controller
@RequestMapping("/*")
public class SecurityLogCont {

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
	
	/**
	 * 1. 메소드명 : getSystemLogList
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getSystemLogList.do")
	@ResponseBody
	public Object getSystemLogList(SecurityLogVO securityLogVo) {
		List<Object> result = new ArrayList<Object>();
		List<SecurityLogVO> getSystemLogList = securityLogService.getSystemLogList(securityLogVo);
		result.add(getSystemLogList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertSystemLog
	 * 2. 작성일: 2022-03-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 유저 활동 정보 저장
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertSystemLog.do")
	@ResponseBody
	public void insertSystemLog(HttpServletRequest request, SecurityLogVO securityLogVo) {
		String ip = null; 
		ip = sessionService.getIpAddress(request);
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");

		securityLogVo.setReg_date(CommonConst.currentDateAndTime());
		securityLogVo.setReg_id(sessioninfo.getUser_id());
		securityLogVo.setReg_nm(sessioninfo.getUser_kor_nm());
		securityLogVo.setRemote_ip(ip);
		
		SecurityLogVO chkInsertVo = securityLogService.getCheckSystemLog(securityLogVo);
		String chkPrj_id = chkInsertVo.getPrj_id();
		String chkReg_date = chkInsertVo.getReg_date();
		String chkPgm_nm = chkInsertVo.getPgm_nm();

		// 중복값 체크 후 insert
		if(chkPrj_id.equals(securityLogVo.getPrj_id()) && chkReg_date.equals(securityLogVo.getReg_date()) && chkPgm_nm.equals(securityLogVo.getPgm_nm())) {
			//System.out.println("중복됨");
		}else {
			securityLogService.insertSystemLog(securityLogVo);
		}
	}
	
	/**
	 * 1. 메소드명 : getDocumentLogList
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchSystemLogList.do")
	@ResponseBody
	public Object searchSystemLogList(SecurityLogVO securityLogVo) {
		List<Object> result = new ArrayList<Object>();
		String from = securityLogVo.getFrom();
		from = from.replaceAll("-", "");
		String to = securityLogVo.getTo();
		to = to.replaceAll("-", "");
		
		securityLogVo.setFrom(from);
		securityLogVo.setTo(to);
		List<SecurityLogVO> searchSystemLogList = securityLogService.searchSystemLogList(securityLogVo);
		result.add(searchSystemLogList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getDocumentLogList
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getDocumentLogList.do")
	@ResponseBody
	public Object getDocumentLogList(SecurityLogVO securityLogVo) {
		List<Object> result = new ArrayList<Object>();
		//securityLogVo.setTextPrjLogAtc(securityLogVo.getSearchPrjLogAtc());
		String from = securityLogVo.getFrom();
		from = from.replaceAll("-", "");
		String to = securityLogVo.getTo();
		to = to.replaceAll("-", "");
		
		securityLogVo.setFrom(from);
		securityLogVo.setTo(to);
		List<SecurityLogVO> getDocumentLogList = securityLogService.getDocumentLogList(securityLogVo);
		result.add(getDocumentLogList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : seculityLogSysExcelDownload
	 * 2. 작성일: 2021. 03. 13.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "seculityLogSysExcelDownload.do")
	@ResponseBody
	public void seculityLogSysExcelDownload(SecurityLogVO securityLogVo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();

		String from_search = securityLogVo.getFrom();
		String to_search = securityLogVo.getTo();
		String from_to_search = from_search+"~"+to_search;

		String from = securityLogVo.getFrom();
		from = from.replaceAll("-", "");
		String to = securityLogVo.getTo();
		to = to.replaceAll("-", "");
		
		securityLogVo.setFrom(from);
		securityLogVo.setTo(to);
		List<SecurityLogVO> searchSystemLogList = securityLogService.searchSystemLogList(securityLogVo);
		//System.out.println(prjtrvo.getJsonRowDatas());
		String fileName = securityLogVo.getFileName();
		String prj_nm = securityLogVo.getPrj_nm();
		String logGubun = securityLogVo.getLogGubun();
		
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.seculityLogExcelDownload(response, "titleList", searchSystemLogList, fileName , from_to_search, prj_nm, logGubun);
	}
	/**
	 * 1. 메소드명 : seculityLogDocExcelDownload
	 * 2. 작성일: 2021. 03. 13.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "seculityLogDocExcelDownload.do")
	@ResponseBody
	public void seculityLogDocExcelDownload(SecurityLogVO securityLogVo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();

		String from_search = securityLogVo.getFrom();
		String to_search = securityLogVo.getTo();
		String from_to_search = from_search+"~"+to_search;
		
		String from = securityLogVo.getFrom();
		from = from.replaceAll("-", "");
		String to = securityLogVo.getTo();
		to = to.replaceAll("-", "");
		
		securityLogVo.setFrom(from);
		securityLogVo.setTo(to);
		List<SecurityLogVO> getDocumentLogList = securityLogService.getDocumentLogList(securityLogVo);
		//System.out.println(prjtrvo.getJsonRowDatas());
		String fileName = securityLogVo.getFileName();
		String prj_nm = securityLogVo.getSearchPrjLogAtc();
		String logGubun = securityLogVo.getLogGubun();
		
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.seculityLogExcelDownload(response, "titleList", getDocumentLogList, fileName , from_to_search, prj_nm, logGubun);
	}
}
