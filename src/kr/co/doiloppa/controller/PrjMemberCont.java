package kr.co.doiloppa.controller;

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

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.AES256Util;
import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.PrjInfoService;
import kr.co.doiloppa.service.PrjMemberService;
import kr.co.doiloppa.service.SessionService;
import kr.co.doiloppa.service.UsersService;

@Controller
@RequestMapping("/*")
public class PrjMemberCont {

	protected PrjMemberService prjmemberService;
	protected SessionService sessionService;
	protected PrjInfoService prjinfoService;
	
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setPrjMemberService(PrjMemberService prjmemberService) {
		this.prjmemberService = prjmemberService;
	}
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * 1. 메소드명 : getPrjMemberList
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 프로젝트 멤버 목록 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getPrjMemberList.do")
	@ResponseBody
	public Object getPrjMemberList(PrjMemberVO prjmembervo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<PrjMemberVO> getPrjMemberList = prjmemberService.getPrjMemberList(prjmembervo);
//		System.out.println(getPrjMemberList);
		List<PrjMemberVO> list = new ArrayList<PrjMemberVO>();
		for(int i=0;i<getPrjMemberList.size();i++) {
			PrjMemberVO getUsersVO = new PrjMemberVO();
			String dcc_yn = getPrjMemberList.get(i).getDcc_yn();
			String user_id = getPrjMemberList.get(i).getUser_id();
			String user_kor_nm = getPrjMemberList.get(i).getUser_kor_nm();
			String user_group_nm = getPrjMemberList.get(i).getUser_group_nm();
			String user_eng_nm = getPrjMemberList.get(i).getUser_eng_nm();
			if(getPrjMemberList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(getPrjMemberList.get(i).getOffi_tel());
				getUsersVO.setOffi_tel(offi_tel);
			}
			if(getPrjMemberList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(getPrjMemberList.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			String hld_offi_gbn = getPrjMemberList.get(i).getHld_offi_gbn();
			String company_nm = getPrjMemberList.get(i).getCompany_nm();
			String user_type = getPrjMemberList.get(i).getUser_type();
			getUsersVO.setHld_offi_gbn(hld_offi_gbn);
			getUsersVO.setDcc_yn(dcc_yn);
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setCompany_nm(company_nm);
			getUsersVO.setUser_type(user_type);
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : prjMemberApply
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 프로젝트 멤버 업데이트
	 * 5. 수정일:
	 */
	@RequestMapping(value = "prjMemberApply.do")
	@ResponseBody
	public Object prjMemberApply(HttpServletRequest request,PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjmembervo.setReg_id(sessioninfo.getUser_id());
		prjmembervo.setReg_date(CommonConst.currentDateAndTime());
		String DiffUserString = prjmembervo.getDiff_user_string();
		String[] DiffUserEach = DiffUserString.split("&&");
		for(int i=0;i<DiffUserEach.length;i++) {
			prjmembervo.setUser_id(DiffUserEach[i]);
			PrjMemberVO prjmember = prjmemberService.isUserPrjMember(prjmembervo);
			if(prjmember!=null) {	//기존 프로젝트 멤버였을 경우 해당 유저를 프로젝트 멤버에서 제외
				prjmemberService.deletePrjMember(prjmembervo);
			}else {	//기존 프로젝트 멤버가 아니었을 경우 해당 유저를 insert
				prjmemberService.insertPrjMember(prjmembervo);
			}
		}
		return result;
	}

	/**
	 * 1. 메소드명 : copyPrjMember
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 타 프로젝트 멤버리스트 복사
	 * 5. 수정일:
	 */
	@RequestMapping(value = "copyPrjMember.do")
	@ResponseBody
	public Object copyPrjMember(HttpServletRequest request,PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjmembervo.setReg_id(sessioninfo.getUser_id());
		prjmembervo.setReg_date(CommonConst.currentDateAndTime());
		prjmemberService.deleteCopyPrjMember(prjmembervo);
		prjmemberService.insertCopyPrjMember(prjmembervo);
		return result;
	}

	/**
	 * 1. 메소드명 : IsProjectMember
	 * 2. 작성일: 2022-03-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 디자이너 인수자가 해당 프로젝트 멤버인지 확인
	 * 5. 수정일:
	 */
	@RequestMapping(value = "IsProjectMember.do")
	@ResponseBody
	public Object IsProjectMember(PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		String[] prjIdArray = prjmembervo.getPrj_id().split(",");
		PrjInfoVO prjinfovo = new PrjInfoVO();
		String message = "";
		for(int i=0;i<prjIdArray.length;i++) {
			prjmembervo.setPrj_id(prjIdArray[i]);
			PrjMemberVO prjmember = prjmemberService.isUserPrjMember(prjmembervo);
			if(prjmember==null) {//프로젝트 멤버가 아닐 경우
				prjinfovo.setPrj_id(prjIdArray[i]);
				PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
				message += getPrjInfo.getPrj_no() + ",";
			}
		}
		if(!message.equals("")) message = message.substring(0, message.length()-1);
		result.add(message);
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg08prUserExcelDownload
	 * 2. 작성일: 2021. 01. 02.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg08prUserExcelDownload.do")
	@ResponseBody
	public void pg08prUserExcelDownload(PrjMemberVO prjmembervo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<PrjMemberVO> getPrjMemberList = prjmemberService.getPrjMemberList(prjmembervo);
//		System.out.println(getPrjMemberList);
		String fileName = prjmembervo.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg08prUserExcelDownload(response, "titleList",  getPrjMemberList, fileName, prjmembervo.getPrj_nm());
	}
	
}
