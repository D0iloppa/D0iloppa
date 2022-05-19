package kr.co.doiloppa.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjFolderInfoVO;
import kr.co.doiloppa.model.PrjGroupMemberVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.PrjWbsCodeVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.PrjCodeSettingsService;
import kr.co.doiloppa.service.PrjInfoService;
import kr.co.doiloppa.service.PrjWbsCodeService;

@Controller
@RequestMapping("/*")
public class PrjWbsCodeCont {

	protected PrjWbsCodeService prjwbscodeService;

	@Autowired
	public void setPrjWbsCodeService(PrjWbsCodeService prjwbscodeService) {
		this.prjwbscodeService = prjwbscodeService;
	}

	/**
	 * 1. 메소드명 : getWbsCodeList
	 * 2. 작성일: 2021-12-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 프로젝트의 WBS code 리스트를 불러옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getWbsCodeList.do")
	@ResponseBody
	public Object getWbsCodeList(PrjWbsCodeVO prjwbscodevo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjWbsCodeVO> getWbsCodeList = prjwbscodeService.getWbsCodeList(prjwbscodevo);
		result.add(getWbsCodeList);
		return result;
	}
}
