package kr.co.hhi.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.excel.ExcelFileDownload;
import kr.co.hhi.model.PrjGroupMemberVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjGroupMemberService;
import kr.co.hhi.service.PrjMemberService;
import kr.co.hhi.service.SessionService;
import kr.co.hhi.service.UsersService;

@Controller
@RequestMapping("/*")
public class PrjGroupMemberCont {

	protected PrjGroupMemberService prjgroupmemberService;
	protected SessionService  sessionService;
	
	@Autowired
	public void setPrjGroupMemberService(PrjGroupMemberService prjgroupmemberService) {
		this.prjgroupmemberService = prjgroupmemberService;
	}
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * 1. 메소드명 : getPrjGroupMemberList
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 프로젝트 그룹 멤버 목록 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getPrjGroupMemberList.do")
	@ResponseBody
	public Object getPrjGroupMemberList(PrjGroupMemberVO prjgroupmembervo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjGroupMemberVO> getPrjGroupMemberList=prjgroupmemberService.getPrjGroupMemberList(prjgroupmembervo);
		result.add(getPrjGroupMemberList);
		return result;
	}

	/**
	 * 1. 메소드명 : prjGroupMemberApply
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 프로젝트 그룹 멤버 업데이트
	 * 5. 수정일:
	 */
	@RequestMapping(value = "prjGroupMemberApply.do")
	@ResponseBody
	public Object prjGroupMemberApply(HttpServletRequest request,PrjGroupMemberVO prjgroupmembervo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjgroupmembervo.setReg_id(sessioninfo.getUser_id());
		prjgroupmembervo.setReg_date(CommonConst.currentDateAndTime());
		String DiffUserString = prjgroupmembervo.getDiff_user_string();
		String[] DiffUserEach = DiffUserString.split("&&");
		for(int i=0;i<DiffUserEach.length;i++) {
			prjgroupmembervo.setUser_id(DiffUserEach[i]);
			int count = prjgroupmemberService.isUserPrjGroupMember(prjgroupmembervo);
			if(count==0) {	//기존 프로젝트 그룹 멤버가 아니었을 경우 해당 유저를 insert
				prjgroupmemberService.insertPrjGroupMember(prjgroupmembervo);
			}else {	//기존 프로젝트 그룹 멤버였을 경우 해당 유저를 프로젝트 멤버에서 제외
				prjgroupmemberService.deletePrjGroupMember(prjgroupmembervo);
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg08prGroupExcelDownload
	 * 2. 작성일: 2021. 01. 02.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg08prGroupExcelDownload.do")
	@ResponseBody
	public void pg08prGroupExcelDownload(PrjGroupMemberVO prjgroupmembervo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<PrjGroupMemberVO> getPrjGroupMemberList=prjgroupmemberService.getPrjGroupMemberList(prjgroupmembervo);
//		System.out.println(getPrjGroupMemberList);
		String fileName = prjgroupmembervo.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg08prGroupExcelDownload(response, "titleList",  getPrjGroupMemberList, fileName, prjgroupmembervo.getPrj_nm());
	}
}
