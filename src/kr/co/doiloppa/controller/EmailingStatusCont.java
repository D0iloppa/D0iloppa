package kr.co.doiloppa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.SecurityLogVO;
import kr.co.doiloppa.service.EmailingStatusService;

@Controller
@RequestMapping("/*")
public class EmailingStatusCont {

	protected EmailingStatusService emailingStatusService;
	
	@Autowired
	public void setEmailingStatusService(EmailingStatusService emailingStatusService) {
		this.emailingStatusService = emailingStatusService;
	}
	
	/**
	 * 1. 메소드명 : getEmailSendList
	 * 2. 작성일: 2022-04-11
	 * 3. 작성자: 박정우
	 * 4. 설명: getEmailSendList 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getEmailSendList.do")
	@ResponseBody
	public Object getEmailSendList(PrjEmailTypeVO prjEmailTypeVo) {
		List<Object> result = new ArrayList<Object>();
		String from = prjEmailTypeVo.getFrom();
		from = from.replaceAll("-", "");
		String to = prjEmailTypeVo.getTo();
		to = to.replaceAll("-", "");
		
		prjEmailTypeVo.setFrom(from);
		prjEmailTypeVo.setTo(to);
		List<PrjEmailTypeVO> getEmailSendList = emailingStatusService.getEmailSendList(prjEmailTypeVo);
		result.add(getEmailSendList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getEmailReceiptList
	 * 2. 작성일: 2022-04-11
	 * 3. 작성자: 박정우
	 * 4. 설명: getEmailReceiptList 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getEmailReceiptList.do")
	@ResponseBody
	public Object getEmailReceiptList(PrjEmailTypeVO prjEmailTypeVo) {
		List<Object> result = new ArrayList<Object>();
		String from = prjEmailTypeVo.getFrom();
		from = from.replaceAll("-", "");
		String to = prjEmailTypeVo.getTo();
		to = to.replaceAll("-", "");
		
		prjEmailTypeVo.setFrom(from);
		prjEmailTypeVo.setTo(to);
		List<PrjEmailTypeVO> getEmailReceiptList = emailingStatusService.getEmailReceiptList(prjEmailTypeVo);

		result.add(getEmailReceiptList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getEmailNreceiptList
	 * 2. 작성일: 2022-04-11
	 * 3. 작성자: 박정우
	 * 4. 설명: getEmailNreceiptList 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getEmailNreceiptList.do")
	@ResponseBody
	public Object getEmailNreceiptList(PrjEmailTypeVO prjEmailTypeVo) {
		List<Object> result = new ArrayList<Object>();
		String from = prjEmailTypeVo.getFrom();
		from = from.replaceAll("-", "");
		String to = prjEmailTypeVo.getTo();
		to = to.replaceAll("-", "");
		
		prjEmailTypeVo.setFrom(from);
		prjEmailTypeVo.setTo(to);
		List<PrjEmailTypeVO> getEmailNreceiptList = emailingStatusService.getEmailNreceiptList(prjEmailTypeVo);
		result.add(getEmailNreceiptList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getEmailDownist
	 * 2. 작성일: 2022-04-11
	 * 3. 작성자: 박정우
	 * 4. 설명: getEmailDownist 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getEmailDownList.do")
	@ResponseBody
	public Object getEmailDownList(PrjEmailTypeVO prjEmailTypeVo) {
		List<Object> result = new ArrayList<Object>();
		String from = prjEmailTypeVo.getFrom();
		from = from.replaceAll("-", "");
		String to = prjEmailTypeVo.getTo();
		to = to.replaceAll("-", "");
		
		prjEmailTypeVo.setFrom(from);
		prjEmailTypeVo.setTo(to);
		
		List<PrjEmailTypeVO> getEmailDownList = emailingStatusService.getEmailDownList(prjEmailTypeVo);
		result.add(getEmailDownList);
		return result;
	}
	
	
	/**
	 * 1. 메소드명 : searchEmailingStatusExcelDownload
	 * 2. 작성일: 2022-04-30
	 * 3. 작성자: 박정우
	 * 4. 설명: 엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchEmailingStatusExcelDownload.do")
	@ResponseBody
	public void searchEmailingStatusExcelDownload(PrjEmailTypeVO prjEmailTypeVo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();

		String from_search = prjEmailTypeVo.getFrom();
		String to_search = prjEmailTypeVo.getTo();
		String from_to_search = from_search+"~"+to_search;
		
		if(from_search.equals("") && to_search.equals("")) {
			from_to_search = "ALL";
		}

		String from = prjEmailTypeVo.getFrom();
		from = from.replaceAll("-", "");
		String to = prjEmailTypeVo.getTo();
		to = to.replaceAll("-", "");
		
		prjEmailTypeVo.setFrom(from);
		prjEmailTypeVo.setTo(to);
		List<PrjEmailTypeVO> getEmailSendList = emailingStatusService.getEmailSendList(prjEmailTypeVo);
		
		String fileName = prjEmailTypeVo.getFileName();
		String prj_nm = prjEmailTypeVo.getPrj_nm();
		
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.searchEmailingStatusExcelDownload(response, "titleList", getEmailSendList, fileName , from_to_search, prj_nm);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
