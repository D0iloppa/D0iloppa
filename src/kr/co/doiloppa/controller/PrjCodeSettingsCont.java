package kr.co.doiloppa.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjFolderInfoVO;
import kr.co.doiloppa.model.PrjGroupMemberVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.PrjCodeSettingsService;
import kr.co.doiloppa.service.PrjDocumentIndexService;
import kr.co.doiloppa.service.PrjGroupMemberService;
import kr.co.doiloppa.service.PrjInfoService;

@Controller
@RequestMapping("/*")
public class PrjCodeSettingsCont {

	protected PrjCodeSettingsService prjcodesettingsService;
	protected PrjDocumentIndexService prjdocumentindexService;
	protected PrjGroupMemberService prjgroupmemberService;

	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setPrjGroupMemberService(PrjGroupMemberService prjgroupmemberService) {
		this.prjgroupmemberService = prjgroupmemberService;
	}

	/**
	 * 1. 메소드명 : isUseStep
	 * 2. 작성일: 2022-03-06
	 * 3. 작성자: 소진희
	 * 4. 설명: Project General 페이지에서 지우려는 해당 Step이 사용중인지 판단
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "isUseStep.do")
	@ResponseBody
	public Object isUseStep(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		String[] set_code_id_array = prjcodesettingsvo.getSet_code_id().split(",");
		String[] set_code_array = prjcodesettingsvo.getSet_code().split(",");
		String errorMessage = "현재 사용중인 스텝은 삭제할 수 없습니다. 사용중인 코드: ";
		String errorMessage2 = "현재 사용중인 스텝이 있어 다른 프로젝트 코드를 복사할 수 없습니다. 사용중인 코드: ";
		String check = "삭제 가능";
		for(int i=0;i<set_code_id_array.length;i++) {
			prjcodesettingsvo.setSet_code_id(set_code_id_array[i]);
			int isUseRevNo = prjcodesettingsService.isUseStep(prjcodesettingsvo);
			if(isUseRevNo>0) {
				errorMessage += set_code_array[i] + ",";
				errorMessage2 += set_code_array[i] + ",";
				check = "삭제 불가";
			}
		}
		errorMessage = errorMessage.substring(0, errorMessage.length()-1);
		errorMessage2 = errorMessage2.substring(0, errorMessage2.length()-1);
		if(check.equals("삭제 불가")) {
			result.add(errorMessage);
			result.add(errorMessage2);
		}else {
			result.add(check);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : isUseRevNo
	 * 2. 작성일: 2022-03-06
	 * 3. 작성자: 소진희
	 * 4. 설명: Project General 페이지에서 지우려는 해당 Revision Number가 사용중인지 판단
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "isUseRevNo.do")
	@ResponseBody
	public Object isUseRevNo(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		String[] set_code_id_array = prjcodesettingsvo.getSet_code_id().split(",");
		String[] set_code_array = prjcodesettingsvo.getSet_code().split(",");
		String errorMessage = "현재 사용중인 코드는 삭제할 수 없습니다. 사용중인 코드: ";
		String check = "삭제 가능";
		for(int i=0;i<set_code_id_array.length;i++) {
			prjcodesettingsvo.setSet_code_id(set_code_id_array[i]);
			int isUseRevNo = prjcodesettingsService.isUseRevNo(prjcodesettingsvo);
			if(isUseRevNo>0) {
				errorMessage += set_code_array[i] + ",";
				check = "삭제 불가";
			}
		}
		errorMessage = errorMessage.substring(0, errorMessage.length()-1);
		if(check.equals("삭제 불가")) {
			result.add(errorMessage);
		}else {
			result.add(check);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getSTEPPrjCodeSettingsList
	 * 2. 작성일: 2021-12-03
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 set_code_type에 맞는 코드 세팅 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getSTEPPrjCodeSettingsList.do")
	@ResponseBody
	public Object getSTEPPrjCodeSettingsList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getSTEPPrjCodeSettingsList = prjcodesettingsService.getSTEPPrjCodeSettingsList(prjcodesettingsvo);
		result.add(getSTEPPrjCodeSettingsList);
		return result;
	}

	/**
	 * 1. 메소드명 : getRevNoMultiBox
	 * 2. 작성일: 2022-02-19
	 * 3. 작성자: 소진희
	 * 4. 설명: 사용했던 리비전과 사용하지 않은 리비전을 구분하여 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getRevNoMultiBox.do")
	@ResponseBody
	public Object getRevNoMultiBox(PrjDocumentIndexVO prjdocumentindexvo,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		prjcodesettingsvo.setSet_code_type("REVISION NUMBER");
		List<PrjCodeSettingsVO> getSTEPPrjCodeSettingsList = prjcodesettingsService.getSTEPPrjCodeSettingsList(prjcodesettingsvo);
		result.add(getSTEPPrjCodeSettingsList);
		if(prjdocumentindexvo.getDoc_id()!=null) {
			List<PrjDocumentIndexVO> getUsedRevCodeId = prjdocumentindexService.getUsedRevCodeId(prjdocumentindexvo);
			if(getUsedRevCodeId!=null) {
				String usedRevCodeId = "";
				for(int i=0;i<getUsedRevCodeId.size();i++) {
					usedRevCodeId += getUsedRevCodeId.get(i).getRev_code_id() + ",";
				}
				if(usedRevCodeId.length()>0) usedRevCodeId = usedRevCodeId.substring(0, usedRevCodeId.length()-1);
				result.add(usedRevCodeId);
			}else {
				result.add("최초등록");
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertSTEPPrjCodeSettings
	 * 2. 작성일: 2021-12-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 set_code_type의 코드 정보 DB에 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertSTEPPrjCodeSettings.do")
	@ResponseBody
	public Object insertSTEPPrjCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(CommonConst.currentDateAndTime());
		
		//insert 하기전 앞뒤 공백 제거
		prjcodesettingsvo.setSet_code((prjcodesettingsvo.getSet_code()).trim());
		prjcodesettingsvo.setSet_desc((prjcodesettingsvo.getSet_desc()).trim());
		prjcodesettingsvo.setSet_val((prjcodesettingsvo.getSet_val()).trim());
		prjcodesettingsvo.setSet_val2((prjcodesettingsvo.getSet_val2()).trim());
		
		int insertSTEPPrjCodeSettings = prjcodesettingsService.insertSTEPPrjCodeSettings(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : updateIFCPrjCodeSettings
	 * 2. 작성일: 2021-12-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 set_code_type의 코드 정보 수정
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateSTEPPrjCodeSettings.do")
	@ResponseBody
	public Object updateSTEPPrjCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setMod_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setMod_date(CommonConst.currentDateAndTime());
		
		//update 하기전 앞뒤 공백 제거
		prjcodesettingsvo.setSet_code((prjcodesettingsvo.getSet_code()).trim());
		prjcodesettingsvo.setSet_desc((prjcodesettingsvo.getSet_desc()).trim());
		prjcodesettingsvo.setSet_val((prjcodesettingsvo.getSet_val()).trim());
		prjcodesettingsvo.setSet_val2((prjcodesettingsvo.getSet_val2()).trim());
		
		int updateSTEPPrjCodeSettings = prjcodesettingsService.updateSTEPPrjCodeSettings(prjcodesettingsvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteSTEPPrjCodeSettings
	 * 2. 작성일: 2021-12-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 set_code_type의 코드 정보 DB에서 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteSTEPPrjCodeSettings.do")
	@ResponseBody
	public Object deleteSTEPPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		prjcodesettingsService.deleteSTEPPrjCodeSettings(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : prjCodeSettingsUpdateCode
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 set_code_type의 코드 정보 수정(순서 및 정보)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "prjCodeSettingsUpdateCode.do")
	@ResponseBody
	public Object updateSTEPPrjCodeSetOrder(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setMod_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setMod_date(CommonConst.currentDateAndTime());
		prjcodesettingsService.prjCodeSettingsUpdateCode(prjcodesettingsvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertCopyPrjSTEPCodeSettings
	 * 2. 작성일: 2021-12-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 set_code_type별 타 프로젝트의 코드 정보 복사
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertCopyPrjSTEPCodeSettings.do")
	@ResponseBody
	public Object insertCopyPrjSTEPCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(CommonConst.currentDateAndTime());
		prjcodesettingsService.deleteCopyPrjSTEPCodeSettings(prjcodesettingsvo);
		prjcodesettingsService.insertCopyPrjSTEPCodeSettings(prjcodesettingsvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg06ExcelDownload
	 * 2. 작성일: 2021. 01. 02.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg06ExcelDownload.do")
	@ResponseBody
	public void pg06ExcelDownload(PrjCodeSettingsVO prjCodeSettingsVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getSTEPPrjCodeSettingsList = prjcodesettingsService.getSTEPPrjCodeSettingsList(prjCodeSettingsVO);
//		System.out.println(getSTEPPrjCodeSettingsList);
		String fileName = prjCodeSettingsVO.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg06ExcelDownload(response, "titleList",  getSTEPPrjCodeSettingsList, fileName , prjCodeSettingsVO.getPrj_nm());
	}
	
	/**
	 * 1. 메소드명 : getIFCPrjCodeSettingsList
	 * 2. 작성일: 2021-11-30
	 * 3. 작성자: 소진희
	 * 4. 설명: IFC 코드 세팅 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getIFCPrjCodeSettingsList.do")
	@ResponseBody
	public Object getIFCPrjCodeSettingsList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getIFCPrjCodeSettingsList = prjcodesettingsService.getIFCPrjCodeSettingsList(prjcodesettingsvo);
		result.add(getIFCPrjCodeSettingsList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getITEMPrjCodeSettingsList
	 * 2. 작성일: 2022-02-24
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence 프로젝트 번호가 SCRUBER001일 경우에 나타나는 ITEM INFORMATION 관리 목록
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getITEMPrjCodeSettingsList.do")
	@ResponseBody
	public Object getITEMPrjCodeSettingsList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getITEMPrjCodeSettingsList = prjcodesettingsService.getITEMPrjCodeSettingsList(prjcodesettingsvo);
		result.add(getITEMPrjCodeSettingsList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertITEMPrjCodeSettings
	 * 2. 작성일: 2022-02-24
	 * 3. 작성자: 소진희
	 * 4. 설명: IFC 코드 정보 DB에 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertITEMPrjCodeSettings.do")
	@ResponseBody
	public Object insertITEMPrjCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(CommonConst.currentDateAndTime());
		
		//insert 하기전 앞뒤 공백 제거
		prjcodesettingsvo.setSet_code((prjcodesettingsvo.getSet_code()).trim());
		prjcodesettingsvo.setSet_desc((prjcodesettingsvo.getSet_desc()).trim());
		
		prjcodesettingsService.insertITEMPrjCodeSettings(prjcodesettingsvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteITEMPrjCodeSettings
	 * 2. 작성일: 2022-02-24
	 * 3. 작성자: 소진희
	 * 4. 설명: Corr ITEM 정보 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteITEMPrjCodeSettings.do")
	@ResponseBody
	public Object deleteITEMPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		prjcodesettingsService.deleteITEMPrjCodeSettings(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : updateITEMPrjCodeSettings
	 * 2. 작성일: 2022-02-24
	 * 3. 작성자: 소진희
	 * 4. 설명: Corr ITEM 정보 수정
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateITEMPrjCodeSettings.do")
	@ResponseBody
	public Object updateITEMPrjCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setMod_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setMod_date(CommonConst.currentDateAndTime());
		
		//update 하기전 앞뒤 공백 제거
		prjcodesettingsvo.setSet_code((prjcodesettingsvo.getSet_code()).trim());
		prjcodesettingsvo.setSet_desc((prjcodesettingsvo.getSet_desc()).trim());
		
		prjcodesettingsService.updateITEMPrjCodeSettings(prjcodesettingsvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertIFCPrjCodeSettings
	 * 2. 작성일: 2021-12-02
	 * 3. 작성자: 소진희
	 * 4. 설명: IFC 코드 정보 DB에 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertIFCPrjCodeSettings.do")
	@ResponseBody
	public Object insertIFCPrjCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(CommonConst.currentDateAndTime());
		
		String set_code_and_set_desc_string = prjcodesettingsvo.getSet_code();
		String[] set_code_and_set_desc_array = set_code_and_set_desc_string.split(",");
		for(int i=0;i<set_code_and_set_desc_array.length;i++) {
			String[] set_code_and_set_desc = set_code_and_set_desc_array[i].split("&");
			//insert 하기전 앞뒤 공백 제거
			prjcodesettingsvo.setSet_code(set_code_and_set_desc[0].trim());
			prjcodesettingsvo.setSet_desc(set_code_and_set_desc[1].trim());
			prjcodesettingsService.insertIFCPrjCodeSettings(prjcodesettingsvo);
		}
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteIFCPrjCodeSettings
	 * 2. 작성일: 2021-12-02
	 * 3. 작성자: 소진희
	 * 4. 설명: IFC 코드 정보 DB에서 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteIFCPrjCodeSettings.do")
	@ResponseBody
	public Object deleteIFCPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		prjcodesettingsService.deleteIFCPrjCodeSettings(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : updateIFCPrjCodeSettings
	 * 2. 작성일: 2021-12-02
	 * 3. 작성자: 소진희
	 * 4. 설명: IFC 코드 정보 수정
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateIFCPrjCodeSettings.do")
	@ResponseBody
	public Object updateIFCPrjCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setMod_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setMod_date(CommonConst.currentDateAndTime());
		
		//update 하기전 앞뒤 공백 제거
		prjcodesettingsvo.setSet_code((prjcodesettingsvo.getSet_code()).trim());
		prjcodesettingsvo.setSet_desc((prjcodesettingsvo.getSet_desc()).trim());
		
		prjcodesettingsService.updateIFCPrjCodeSettings(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : insertCopyPrjIFCCodeSettings
	 * 2. 작성일: 2021-12-02
	 * 3. 작성자: 소진희
	 * 4. 설명: 타 프로젝트의 IFC 코드 정보 복사
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertCopyPrjIFCCodeSettings.do")
	@ResponseBody
	public Object insertCopyPrjIFCCodeSettings(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(CommonConst.currentDateAndTime());
		prjcodesettingsService.deleteCopyPrjIFCCodeSettings(prjcodesettingsvo);
		prjcodesettingsService.insertCopyPrjIFCCodeSettings(prjcodesettingsvo);
		return result;
	}
	/**
	 * 1. 메소드명 : getActualDataList
	 * 2. 작성일: 2022-01-12
	 * 3. 작성자: 소진희
	 * 4. 설명: Actual Date 에서 선택할 수 있는 목록 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getActualDateList.do")
	@ResponseBody
	public Object getActualDataList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		if(prjcodesettingsvo.getSet_desc().equals("TR")) {
			prjcodesettingsvo.setSet_code_type("TR ISSUE PURPOSE");
			List<PrjCodeSettingsVO> getTrIssuePurposeList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
			result.add(getTrIssuePurposeList);
			prjcodesettingsvo.setSet_code_type("TR RETURN STATUS");
			List<PrjCodeSettingsVO> getTrReturnStatusList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
			result.add(getTrReturnStatusList);
		}else if(prjcodesettingsvo.getSet_desc().equals("VTR")) {
			prjcodesettingsvo.setSet_code_type("VTR ISSUE PURPOSE");
			List<PrjCodeSettingsVO> getVtrIssuePurposeList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
			result.add(getVtrIssuePurposeList);
			prjcodesettingsvo.setSet_code_type("VTR RETURN STATUS");
			List<PrjCodeSettingsVO> getVtrReturnStatusList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
			result.add(getVtrReturnStatusList);
		}else if(prjcodesettingsvo.getSet_desc().equals("STR")) {
			prjcodesettingsvo.setSet_code_type("STR ISSUE PURPOSE");
			List<PrjCodeSettingsVO> getStrIssuePurposeList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
			result.add(getStrIssuePurposeList);
			prjcodesettingsvo.setSet_code_type("STR RETURN STATUS");
			List<PrjCodeSettingsVO> getStrReturnStatusList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
			result.add(getStrReturnStatusList);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getTrIssuePurposeList
	 * 2. 작성일: 2022-01-06
	 * 3. 작성자: 소진희
	 * 4. 설명: TR ISSUE PURPOSE 코드 세팅 목록 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTrIssuePurposeList.do")
	@ResponseBody
	public Object getTrIssuePurposeList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getTrIssuePurposeList = prjcodesettingsService.getTrIssuePurposeList(prjcodesettingsvo);
		result.add(getTrIssuePurposeList);
		return result;
	}

	/**
	 * 1. 메소드명 : getTrDisciplineList
	 * 2. 작성일: 2022-01-06
	 * 3. 작성자: 소진희
	 * 4. 설명: TR에서 사용할 Discipline 목록 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTrDisciplineList.do")
	@ResponseBody
	public Object getTrDisciplineList(PrjDisciplineVO prjdisciplinevo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDisciplineVO> getTrDisciplineList = prjcodesettingsService.getTrDisciplineList(prjdisciplinevo);
		result.add(getTrDisciplineList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg06IFCExcelDownload
	 * 2. 작성일: 2021. 01. 02.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg06IFCExcelDownload.do")
	@ResponseBody
	public void pg06IFCExcelDownload(PrjCodeSettingsVO prjCodeSettingsVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getIFCPrjCodeSettingsList = prjcodesettingsService.getIFCPrjCodeSettingsList(prjCodeSettingsVO);
//		System.out.println(getIFCPrjCodeSettingsList);
		String fileName = prjCodeSettingsVO.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg06IFCExcelDownload(response, "titleList",  getIFCPrjCodeSettingsList, fileName, prjCodeSettingsVO.getPrj_nm());
	}

	/**
	 * 1. 메소드명 : getPrjGroupList
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트의 멤버 그룹 리스트 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getPrjGroupList.do")
	@ResponseBody
	public Object getPrjGroupList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getPrjGroupList = prjcodesettingsService.getPrjGroupList(prjcodesettingsvo);
		result.add(getPrjGroupList);
		return result;
	}

	/**
	 * 1. 메소드명 : insertPrjGroup
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트에 멤버 그룹 생성
	 * 5. 수정일:
	 */
	@RequestMapping(value = "insertPrjGroup.do")
	@ResponseBody
	public Object insertPrjGroup(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setReg_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setReg_date(CommonConst.currentDateAndTime());
		prjcodesettingsService.insertPrjGroup(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : updatePrjGroup
	 * 2. 작성일: 2022-04-11
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트에 등록된 그룹 편집
	 * 5. 수정일:
	 */
	@RequestMapping(value = "updatePrjGroup.do")
	@ResponseBody
	public Object updatePrjGroup(HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjcodesettingsvo.setMod_id(sessioninfo.getUser_id());
		prjcodesettingsvo.setMod_date(CommonConst.currentDateAndTime());
		prjcodesettingsService.updatePrjGroup(prjcodesettingsvo);
		return result;
	}

	/**
	 * 1. 메소드명 : deletePrjGroup
	 * 2. 작성일: 2021-12-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 프로젝트에 멤버 그룹 삭제
	 * 5. 수정일: 2022-02-26 그룹 삭제시 해당 그룹의 멤버들도 삭제
	 */
	@RequestMapping(value = "deletePrjGroup.do")
	@ResponseBody
	public Object deletePrjGroup(PrjCodeSettingsVO prjcodesettingsvo,PrjGroupMemberVO prjgroupmembervo) {
		List<Object> result = new ArrayList<Object>();
		prjcodesettingsService.deletePrjGroup(prjcodesettingsvo);
		prjgroupmemberService.deletePrjGroupMemberAll(prjgroupmembervo);
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrFromToList
	 * 2. 작성일: 2022-01-03
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence From/To(Sender/Receiver) List 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCorrFromToList.do")
	@ResponseBody
	public Object getCorrFromToList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getCorrFromToList = prjcodesettingsService.getCorrFromToList(prjcodesettingsvo);
		result.add(getCorrFromToList);
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrTypeList
	 * 2. 작성일: 2022-01-03
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence Type List 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCorrTypeList.do")
	@ResponseBody
	public Object getCorrTypeList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getCorrTypeList = prjcodesettingsService.getCorrTypeList(prjcodesettingsvo);
		result.add(getCorrTypeList);
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrResponsibilityList
	 * 2. 작성일: 2022-01-03
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence Responsibility List 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCorrResponsibilityList.do")
	@ResponseBody
	public Object getCorrResponsibilityList(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getCorrResponsibilityList = prjcodesettingsService.getCorrResponsibilityList(prjcodesettingsvo);
		result.add(getCorrResponsibilityList);
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrSerialAuto
	 * 2. 작성일: 2022-01-03
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence Serial 자동생성을 위한 값 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCorrSerialAuto.do")
	@ResponseBody
	public Object getCorrSerialAuto(PrjCodeSettingsVO prjcodesettingsvo,PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjCodeSettingsVO getCorrSerialAuto = prjcodesettingsService.getCorrSerialAuto(prjcodesettingsvo);
		int getCorrSerialMax = prjdocumentindexService.getCorrSerialMax(prjdocumentindexvo);
		result.add(String.format("%0"+getCorrSerialAuto.getSet_val4().replaceAll("%", "d"), getCorrSerialMax+1));
		result.add(getCorrSerialMax+1);
		if(prjdocumentindexvo.getSerial() != 0) {
			int getCorrSerialExistYN = prjdocumentindexService.getCorrSerialExistYN(prjdocumentindexvo);
			result.add(getCorrSerialExistYN);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getCorrInit
	 * 2. 작성일: 2022-01-03
	 * 3. 작성자: 소진희
	 * 4. 설명: Correspondence 각 폴더별 초기값 자동세팅을 위한 정보호출
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCorrInit.do")
	@ResponseBody
	public Object getCorrInit(PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		PrjCodeSettingsVO getCorrInit = prjcodesettingsService.getCorrInit(prjcodesettingsvo);
		result.add(getCorrInit);
		return result;
	}

	
	/**
	 * 1. 메소드명 : pg05_getCode
	 * 2. 작성일: 2021-12-17
	 * 3. 작성자: 박정우
	 * 4. 설명: 코드 필드값 DB에서 가져오는 메소드
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg05_getCode.do")
	@ResponseBody
	public Object pg05_getCode(PrjCodeSettingsVO prjcodesettingsvo) {
		
		List<Object> result = new ArrayList<Object>();
		
		List<PrjCodeSettingsVO> getCorrResponList = prjcodesettingsService.pg05_getCode(prjcodesettingsvo);
		
		result.add(getCorrResponList);
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg05_copyCode
	 * 2. 작성일: 2021-12-17
	 * 3. 작성자: 박정우
	 * 4. 설명: 화면상의 수정 정보를 DB에 반영
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg05_copyCode.do")
	@ResponseBody
	public ModelAndView pg05_copyCode(PrjCodeSettingsVO prjcodesettingsvo){
		ModelAndView result = new ModelAndView();
		
		// 디폴트 프로젝트 ID인 0000000001는 다른 프로젝트로 부터 복사 붙여넣기 하지 않는다.
		if(prjcodesettingsvo.getPrj_id().equals("0000000001")) {
			result.addObject("error","default project can't copy");
			return result;
		}
		
		// 코드 삭제해도 되는지 체크
		int chk = prjcodesettingsService.selectOne("prjcodesettingsSqlMap.pg05_chkDelval",prjcodesettingsvo.getPrj_id());
		

		if (chk > 0) {
			result.addObject("status", "FAIL");
			result.addObject("error", "사용중인 코드가 있어서 코드복사를 실행할 수 없습니다.");
			return result;
		}
		// 복사 전, 현재 테이블은 비운다.
		prjcodesettingsService.pg05_ClearTbl(prjcodesettingsvo);
		// 제거 후, 데이터를 복사해 온다.
		prjcodesettingsService.pg05_copyTbl(prjcodesettingsvo);
		
		result.addObject("status","OK");
		
		return result;
	}
	
	
	@RequestMapping(value = "pg05_del.do")
	@ResponseBody
	public ModelAndView pg05_del(PrjCodeSettingsVO prjcodesettingsvo) throws JsonParseException, JsonMappingException, IOException {

		ModelAndView result = new ModelAndView();
		
		String[] delJsonRows = prjcodesettingsvo.getJsonRowdatas();
		
		if(delJsonRows != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			for(String json : delJsonRows) {
				// json을 VO로 변환
				
				PrjCodeSettingsVO tmpBean = objectMapper.readValue(json, PrjCodeSettingsVO.class);
				int cnt = prjcodesettingsService.pg05_chkCodeId(tmpBean);
				// DB에 존재하지 않는 코드는 지나감
				if(cnt<1) continue;
				
				try {
					if(prjcodesettingsService.pg05_del(tmpBean) < 1)
						throw new Exception(tmpBean.getSet_code_id() + " : del error");
				} catch(Exception e) {
					result.addObject("del_validation",e.getMessage());
				}
			}
		}
	

		return result;
	}
	
	/**
	 * 1. 메소드명 : pg05_updateCode
	 * 2. 작성일: 2021-12-17
	 * 3. 작성자: 박정우
	 * 4. 설명: 화면상의 수정 정보를 DB에 반영
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg05_updateCode.do")
	@ResponseBody
	public void pg05_updateCode(PrjCodeSettingsVO prjcodesettingsvo)
			throws JsonParseException, JsonMappingException, IOException {
		
		/*
		String[] deletedRows = prjcodesettingsvo.getDeleteRows();
		// 삭제된 데이터가 있는 경우 삭제
		if(deletedRows != null) {
			PrjCodeSettingsVO tmpDelBean = prjcodesettingsvo;
			for(String set_code_id : deletedRows) {
				System.out.println(set_code_id + " 삭제");
				tmpDelBean.setSet_code_id(set_code_id);
				prjcodesettingsService.pg05_delCode(tmpDelBean);
			}
		}
		*/
		
		// 배열로 받은 경우
		String[] jsonDatas = prjcodesettingsvo.getJsonRowdatas();
		if(jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			// json배열에서 하나씩 꺼내서 update or insert
			for(String json : jsonDatas) {
				// json을 VO로 변환
				PrjCodeSettingsVO tmpBean = objectMapper.readValue(json, PrjCodeSettingsVO.class);
				
				tmpBean.setPrj_id(prjcodesettingsvo.getPrj_id());
				tmpBean.setSet_code_type("CORRESPONDENCE RESPONSIBILITY");
				tmpBean.setSet_code_feature("C");
				
				//update,insert 하기전에 앞뒤 공백제거
				String set_code = tmpBean.getSet_code().trim();
				
				String set_desc = tmpBean.getSet_desc();
				if(set_desc == null) set_desc = "";
				else set_desc = set_desc.trim();
				
				tmpBean.setSet_code(set_code);
				tmpBean.setSet_desc(set_desc);
				
				
				// 업데이트 하기 전에, 해당 데이터의 존재여부(id로 확인)에 따라 update 혹은 insert 분기
				int cnt = prjcodesettingsService.pg05_chkCodeId(tmpBean);
				// 해당 code_id가 존재하면 update
				if (cnt > 0) prjcodesettingsService.pg05_updateCode(tmpBean);
				// 해당 code_id가 존재하지 않으면 insert
				else prjcodesettingsService.pg05_insertCode(tmpBean);
			}		
			
			
			
		}	
		
		
	}
	
	/**
	 * 1. 메소드명 : pg05ExcelDownload
	 * 2. 작성일: 2021. 12. 31.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg05ExcelDownload.do")
	@ResponseBody
	public void pg05ExcelDownload(PrjCodeSettingsVO prjCodeSettingsVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjCodeSettingsVO> getCorrResponList = prjcodesettingsService.pg05_getCode(prjCodeSettingsVO);
		String fileName = prjCodeSettingsVO.getFileName();
//		System.out.println(getCorrResponList);
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg05ExcelDownload(response, "titleList",  getCorrResponList, fileName , prjCodeSettingsVO.getPrj_nm());
	}

	/**
	 * 1. 메소드명 : pg08prgroupEditExcelDownload
	 * 2. 작성일: 2021. 01. 03.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "pg08prgroupEditExcelDownload.do")
	@ResponseBody
	public void pg08prgroupEditExcelDownload(PrjCodeSettingsVO prjCodeSettingsVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getPrjGroupList = prjcodesettingsService.getPrjGroupList(prjCodeSettingsVO);
//		System.out.println(getPrjGroupList);
		String fileName = prjCodeSettingsVO.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg08prgroupEditExcelDownload(response, "titleList",  getPrjGroupList, fileName, prjCodeSettingsVO.getPrj_nm());
	}
	
	
	
	@RequestMapping(value = "getDiscipObj.do")
	@ResponseBody
	public ModelAndView getDiscipObj(FolderInfoVO folderInfoVo) {
		ModelAndView result = new ModelAndView();
		
		
		FolderInfoVO discipObj = prjcodesettingsService.getDiscipObj(folderInfoVo);
		
		result.addObject("discipObj",discipObj);
		
		return result;
	}
	
	
}
