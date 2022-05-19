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
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjEmailTypeService;

@Controller
@RequestMapping("/*")
public class PrjEmailTypeCont {

	protected PrjEmailTypeService  prjemailtypeService;
	
	@Autowired
	public void setPrjEmailTypeService(PrjEmailTypeService prjemailtypeService) {
		this.prjemailtypeService = prjemailtypeService;
	}

	/**
	 * 1. 메소드명 : getPrjEmailTypeAll
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_email_type 테이블에서 모든 email type 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjEmailTypeAll.do")
	@ResponseBody
	public Object getPrjEmailTypeAll(PrjEmailTypeVO prjemailtypevo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjEmailTypeVO> getPrjEmailTypeAll = prjemailtypeService.getPrjEmailTypeAll(prjemailtypevo);
		result.add(getPrjEmailTypeAll);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getPrjEmailType
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_email_type 테이블에서 각 feature에 해당하는 email type 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjEmailType.do")
	@ResponseBody
	public Object getPrjEmailType(PrjEmailTypeVO prjemailtypevo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjEmailTypeVO> getPrjEmailType = prjemailtypeService.getPrjEmailType(prjemailtypevo);
		result.add(getPrjEmailType);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertPrjEmailType
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_email_type 테이블에 email type 생성
	 * 5. 수정일: 2021-01-11
	 */
	@RequestMapping(value = "insertPrjEmailType.do")
	@ResponseBody
	public Object insertPrjEmailType(PrjEmailTypeVO prjemailtypevo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjemailtypevo.setReg_id(sessioninfo.getUser_id());
		prjemailtypevo.setReg_date(CommonConst.currentDateAndTime());
		PrjEmailTypeVO selectEmailType = prjemailtypeService.selectEmailType(prjemailtypevo);
		if(selectEmailType==null) {//type이 바뀌었을 경우 새로 insert
			int insertPrjEmailType = prjemailtypeService.insertPrjEmailType(prjemailtypevo);
			result.add(insertPrjEmailType);
		}else {//desc만 바뀌었을 경우 update
			prjemailtypevo.setEmail_type_id(selectEmailType.getEmail_type_id());
			prjemailtypevo.setMod_id(sessioninfo.getUser_id());
			prjemailtypevo.setMod_date(CommonConst.currentDateAndTime());
			int updatePrjEmailType = prjemailtypeService.updatePrjEmailType(prjemailtypevo);
			result.add(updatePrjEmailType);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : deletePrjEmailType
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_email_type 테이블에서 email type 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deletePrjEmailType.do")
	@ResponseBody
	public void deletePrjEmailType(PrjEmailTypeVO prjemailtypevo) {
		prjemailtypeService.deletePrjEmailType(prjemailtypevo);
	}
	
	/**
	 * 1. 메소드명 : pg10EmailExcelDownload
	 * 2. 작성일: 2022. 01. 03.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */	
	@RequestMapping(value = "pg10EmailExcelDownload.do")
	@ResponseBody
	public void pg05ExcelDownload(PrjEmailTypeVO prjemailtypevo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		List<PrjEmailTypeVO> getPrjEmailType = prjemailtypeService.getPrjEmailType(prjemailtypevo);
//		System.out.println(getPrjEmailType);
		String fileName = prjemailtypevo.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg10EmailExcelDownload(response, "titleList",  getPrjEmailType, fileName, prjemailtypevo.getPrj_nm());
	}
	
}
