package kr.co.hhi.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.model.CodeInfoVO;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDisciplineVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjCodeSettingsService;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.PrjEmailService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.PrjTrService;
import kr.co.hhi.service.TreeService;

@Controller
@RequestMapping("/*")
public class PrjInfoCont {

	protected PrjInfoService prjinfoService;
	protected TreeService treeService;
	protected PrjCodeSettingsService prjcodesettingsService;
	protected PrjEmailService  prjemailService;
	
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	@Autowired
	public void setPrjEmailService(PrjEmailService prjemailService) {
		this.prjemailService = prjemailService;
	}
	
	/**
	 * 1. 메소드명 : getPrjTypeList
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 project타입 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjTypeList.do")
	@ResponseBody
	public Object getPrjTypeList() {
		List<Object> result = new ArrayList<Object>();
		List<CodeInfoVO> getPrjTypeList =  prjinfoService.getPrjType();
		result.add(getPrjTypeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertCodeInfo
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 project타입 리스트를 입력
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertCodeInfo.do")
	@ResponseBody
	public void insertCodeInfo(CodeInfoVO codeInfoVo) {
		
		CodeInfoVO getsetOrderVo = prjinfoService.getMaxsetOrder();
		CodeInfoVO getCodeVo = prjinfoService.getMaxCode();
		
		String code = "";
		long set_order = 0;
		
		if(getCodeVo != null) {
			code = getCodeVo.getCode();
			set_order = getsetOrderVo.getSet_order();
		}
		
		// 처음 코드 저장할떄
		if((code == null || code == "")) {
			codeInfoVo.setCode("10");
			codeInfoVo.setSet_order(1);
			
			prjinfoService.insertCodeInfo(codeInfoVo);
		}else {
			codeInfoVo.setCode(String.valueOf(Integer.parseInt(code)+1));
			codeInfoVo.setSet_order(set_order+1);
			
			prjinfoService.insertCodeInfo(codeInfoVo);
		}
	}
	
	/**
	 * 1. 메소드명 : updateCodeInfo
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 project타입 리스트를 입력
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateCodeInfo.do")
	@ResponseBody
	public Object updateCodeInfo(CodeInfoVO codeInfoVo) {
		List<Object> result = new ArrayList<Object>();
		
		prjinfoService.updateCodeInfo(codeInfoVo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteCodeInfo
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 project타입 리스트를 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteCodeInfo.do")
	@ResponseBody
	public Object deleteCodeInfo(CodeInfoVO codeInfoVo,PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjInfoVO> getPrjListVo =  prjinfoService.getPrjList();
		
		String getCode = codeInfoVo.getCode();
		String[] getCodeArray = getCode.split(",");
		String chkCode = "";
		String delCode = "";
		String yCodeNm = "";
		for(int i=0;i<getCodeArray.length;i++) {
			prjinfovo.setPrj_type(getCodeArray[i]);
			int count = prjinfoService.getUseCodeYN(prjinfovo);
			if(count>0) {
				// 사용중인 코드
				chkCode += getCodeArray[i] + ",";
				codeInfoVo.setCode(getCodeArray[i]);
				CodeInfoVO codeNmVo = prjinfoService.getUseYcodeNm(codeInfoVo);
				yCodeNm += codeNmVo.getCode_nm() + ",";
			}else{
				//삭제
				delCode += getCodeArray[i] + ",";
				codeInfoVo.setCode(getCodeArray[i]);
				prjinfoService.deleteCodeInfo(codeInfoVo);
			}
		}
		if(chkCode.equals("") || chkCode==null) {
			result.add("");
		}else {
			yCodeNm = yCodeNm.substring(0, yCodeNm.length()-1);
			result.add(yCodeNm);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : prjCodeSettingsUpdateCode
	 * 2. 작성일: 2022-01-26
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 project타입 코드 정보 수정(순서 및 정보)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "prjCodeInfoUpdate.do")
	@ResponseBody
	public Object prjCodeInfoUpdate(CodeInfoVO codeInfoVo) {
		List<Object> result = new ArrayList<Object>();
		
		prjinfoService.prjCodeInfoUpdate(codeInfoVo);
		return result;
	}

	/**
	 * 1. 메소드명 : getPrjList
	 * 2. 작성일: 2021-11-25
	 * 3. 작성자: 소진희
	 * 4. 설명: 전체 프로젝트 리스트를 가져옴 (order by prj_id desc)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjList.do")
	@ResponseBody
	public Object getPrjList() {
		List<Object> result = new ArrayList<Object>();
		List<PrjInfoVO> getPrjList =  prjinfoService.getPrjList();
		result.add(getPrjList);
		return result;
	}

	/**
	 * 1. 메소드명 : searchDesignerProject
	 * 2. 작성일: 2022-03-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 인계자가 디자이너 또는 Correspondence도서의 책임자로 설정된 도서가 있는 프로젝트 목록을 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchDesignerProject.do")
	@ResponseBody
	public Object searchDesignerProject(PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjInfoVO> searchDesignerProject =  prjinfoService.searchDesignerProject(prjinfovo);
		result.add(searchDesignerProject);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getPrjInfo
	 * 2. 작성일: 2021-11-25
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트의 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjInfo.do")
	@ResponseBody
	public Object getPrjInfo(PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
		result.add(getPrjInfo);
		return result;
	}

	/**
	 * 1. 메소드명 : insertPrjInfo
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 새 프로젝트 생성
	 * 5. 수정일: 2022-01-27
	 * 6. 수정내용: 새 프로젝트 생성시 Default Project의 폴더정보, 프로세스 Type, Numbering 설정,
	 * 			code setting, step 프로세스, discipline 설정, 발송메일 템플릿 등을 복사하여 기본으로 가져감
	 */
	@RequestMapping(value = "insertPrjInfo.do")
	@ResponseBody
	public Object insertPrjInfo(HttpServletRequest request,PrjInfoVO prjinfovo,PrjFolderInfoVO prjfolderinfovo,PrjCodeSettingsVO prjcodesettingsvo,PrjDisciplineVO prjdisciplinevo,PrjEmailTypeVO prjemailtypevo,PrjEmailVO prjemailvo) throws Exception{
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String currentDateAndTime = CommonConst.currentDateAndTime();
		prjinfovo.setReg_id(sessioninfo.getUser_id());
		prjinfovo.setReg_date(currentDateAndTime);
		prjfolderinfovo.setReg_id(sessioninfo.getUser_id());
		prjfolderinfovo.setReg_date(currentDateAndTime);
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(currentDateAndTime);
		prjdisciplinevo.setReg_id(sessioninfo.getUser_id());
		prjdisciplinevo.setReg_date(currentDateAndTime);
		prjemailtypevo.setReg_id(sessioninfo.getUser_id());
		prjemailtypevo.setReg_date(currentDateAndTime);
		prjemailvo.setReg_id(sessioninfo.getUser_id());
		prjemailvo.setReg_date(currentDateAndTime);
		String prj_id = null;
		try {
			/////프로젝트 정보 생성
			prj_id = prjinfoService.insertPrjInfo(prjinfovo);
			
			/////Default Project의 폴더정보 복사
			prjfolderinfovo.setCreate_prj_id(prj_id);
			prjinfoService.insertCopyPrjFolder(prjfolderinfovo);
			//1번폴더의 이름 생성된 프로젝트 명으로 변경
			FolderInfoVO update = new FolderInfoVO();
			update.setPrj_id(prjfolderinfovo.getCreate_prj_id());
			String prj_nm = prjinfoService.getPrjNm(prjfolderinfovo.getCreate_prj_id());
			update.setFolder_id(1);
			update.setFolder_nm(prj_nm);
			prjinfoService.setRootFolderNm(update);
			//folder_type이 TRA이고 tra_folder_nm이 null이 아닌 폴더들 삭제하기(각 프로젝트에서 생성된 System transmittal TR Outgoing 폴더들)
			prjfolderinfovo.setPrj_id(prj_id);
			int deleteTROutgoingFolder = treeService.deleteTROutgoingFolder(prjfolderinfovo);
			
			prjinfovo.setPrj_id(prj_id);
			/////프로세스 타입 생성
			prjinfoService.initProjectProcess(prjinfovo);
			
			/////시스템 prefix 생성
			prjinfoService.initProjectSystemPrefix(prjinfovo);
			
			/////p_prj_code_settings 테이블에 있는 코드들 전부 복사
			prjcodesettingsvo.setPrj_id(prj_id);
			prjcodesettingsService.insertCopyDefaultPrjCodeSettings(prjcodesettingsvo);
			
			/////discipline 설정 복사
			prjdisciplinevo.setPrj_id(prj_id);
			prjcodesettingsService.insertCopyDefaultDisciplineInfo(prjdisciplinevo);
			prjcodesettingsService.insertCopyDefaultDisciplineFolderInfo(prjdisciplinevo);
			
			/////이메일 Type, 템플릿 설정 복사
			prjemailtypevo.setPrj_id(prj_id);
			prjemailService.insertCopyDefaultEmailType(prjemailtypevo);
			prjemailvo.setPrj_id(prj_id);
			prjemailService.insertCopyDefaultEmailTypeContents(prjemailvo);
			
			System.out.println("생성한 prj_id:"+prj_id);
			result.add(prj_id);
		}catch (Exception e) {
			result.add(prj_id);
			throw e;
		}
		return result;
	}

	/**
	 * 1. 메소드명 : insertDCCPrjMember
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 새 프로젝트 생성시 프로젝트 멤버 테이블에 DCC 할당
	 * 5. 수정일: 2022-02-08
	 */
	@RequestMapping(value = "insertDCCPrjMember.do")
	@ResponseBody
	public Object insertDCCPrjMember(HttpServletRequest request,PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjmembervo.setReg_id(sessioninfo.getUser_id());
		prjmembervo.setReg_date(CommonConst.currentDateAndTime());
		String[] diff_dcc_user_array = prjmembervo.getDiff_dcc_user().split(",");
		for(int i=0;i<diff_dcc_user_array.length;i++) {
			prjmembervo.setUser_id(diff_dcc_user_array[i]);
			prjinfoService.insertDCCPrjMember(prjmembervo);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : copyFolderTemplate
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 새 프로젝트 생성시 프로젝트의 기본 템플릿 or 선택한 프로젝트의 폴더 구조를 복사하여 저장
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertCopyPrjFolder.do")
	@ResponseBody
	public Object insertCopyPrjFolder(HttpServletRequest request,PrjFolderInfoVO prjfolderinfovo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjfolderinfovo.setReg_id(sessioninfo.getUser_id());
		prjfolderinfovo.setReg_date(CommonConst.currentDateAndTime());
		prjinfoService.insertCopyPrjFolder(prjfolderinfovo);
		
		// 1번폴더 프로젝트 명으로 변경
		FolderInfoVO update = new FolderInfoVO();
		update.setPrj_id(prjfolderinfovo.getCreate_prj_id());
		String prj_nm = prjinfoService.getPrjNm(prjfolderinfovo.getCreate_prj_id());
		update.setFolder_id(1);
		update.setFolder_nm(prj_nm);
		prjinfoService.setRootFolderNm(update);
		
		
		return result;
	}

	/**
	 * 1. 메소드명 : updatePrjInfo
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 프로젝트 정보 수정
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updatePrjInfo.do")
	@ResponseBody
	public Object updatePrjInfo(HttpServletRequest request,PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjinfovo.setMod_id(sessioninfo.getUser_id());
		prjinfovo.setMod_date(CommonConst.currentDateAndTime());
		prjinfoService.updatePrjInfo(prjinfovo);
		return result;
	}

	/**
	 * 1. 메소드명 : getPrjDCC
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트의 DCC 정보를 가져옴
	 * 5. 수정일: 2022-01-27
	 */
	@RequestMapping(value = "getPrjDCC.do")
	@ResponseBody
	public Object getPrjDCC(PrjMemberVO prjmembervo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<PrjMemberVO> getPrjDCC = prjinfoService.getPrjDCC(prjmembervo);
		List<PrjMemberVO> list = new ArrayList<PrjMemberVO>();
		for(int i=0;i<getPrjDCC.size();i++) {
			PrjMemberVO getUsersVO = new PrjMemberVO();
			String user_id = getPrjDCC.get(i).getUser_id();
			String user_kor_nm = getPrjDCC.get(i).getUser_kor_nm();
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			if(getPrjDCC.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(getPrjDCC.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : updateDCCPrjMember
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트의 DCC 변경
	 * 5. 수정일: 2022-02-08
	 */
	@RequestMapping(value = "updateDCCPrjMember.do")
	@ResponseBody
	public Object updateDCCPrjMember(HttpServletRequest request,PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjmembervo.setReg_id(sessioninfo.getUser_id());
		prjmembervo.setReg_date(CommonConst.currentDateAndTime());
		prjmembervo.setMod_id(sessioninfo.getUser_id());
		prjmembervo.setMod_date(CommonConst.currentDateAndTime());
		
		String[] diff_dcc_user_array = prjmembervo.getDiff_dcc_user().split(",");
		for(int i=0;i<diff_dcc_user_array.length;i++) {
			prjmembervo.setDiff_dcc_user(diff_dcc_user_array[i]);
			PrjMemberVO prjmember = prjinfoService.getPrjDCCYN(prjmembervo);
			if(prjmember!=null) {
				String dcc_yn = prjmember.getDcc_yn();
				if(dcc_yn.equals("Y")) { //해당 user_id가 기존에 DCC였다면 dcc_yn값을 'N'로 변경
					prjmembervo.setDcc_yn("N");
				}else if(dcc_yn.equals("N")) { //해당 user_id가 기존에 DCC가 아니었다면 dcc_yn값을 'Y'로 변경
					prjmembervo.setDcc_yn("Y");
				}
				prjinfoService.updatePrjDCCYN(prjmembervo);
			}else {	//기존 프로젝트 멤버가 아니었을 경우 해당 유저를 DCC로 insert
				String diff_dcc_user = prjmembervo.getDiff_dcc_user();
				if(diff_dcc_user!=null && !diff_dcc_user.equals("")) {
					prjmembervo.setUser_id(diff_dcc_user);
					prjmembervo.setDcc_yn("Y");
					prjinfoService.insertDCCPrjMember(prjmembervo);					
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 1. 메소드명 : pg_UpdatePrjInfo
	 * 2. 작성일: 2021-11-29
	 * 3. 작성자: 권도일
	 * 4. 설명: Project General 프로젝트 정보 반영
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg_UpdatePrjInfo.do")
	@ResponseBody
	public Object pg_UpdatePrjInfo(HttpServletRequest request,PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjinfoService.pg_updatePrjInfo(prjinfovo);
		return result;
	}
}
