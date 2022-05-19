package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDrnInfoHistVO;
import kr.co.hhi.model.PrjDrnInfoVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjDrnInfoHistService;
import kr.co.hhi.service.PrjDrnInfoService;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjEmailTypeService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.PrjTrService;

@Controller
@RequestMapping("/*")
public class PrjDrnInfoHistCont {

	protected PrjDrnInfoHistService  prjdrninfohistService;
	
	@Autowired
	public void setPrjDrnInfoHistService(PrjDrnInfoHistService prjdrninfohistService) {
		this.prjdrninfohistService = prjdrninfohistService;
	}

	/**
	 * 1. 메소드명 : getPrjDrnHistory
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 도서의 Drn History 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjDrnHistory.do")
	@ResponseBody
	public Object getPrjDrnHistory(PrjDrnInfoHistVO prjdrninfohistvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDrnInfoHistVO> getPrjDrnHistory = prjdrninfohistService.getPrjDrnHistory(prjdrninfohistvo);
		result.add(getPrjDrnHistory);
		return result;
	}

	/**
	 * 1. 메소드명 : getPrjDrnHistoryData
	 * 2. 작성일: 2021-12-24
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 도서의 Drn History 상세 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjDrnHistoryData.do")
	@ResponseBody
	public Object getEngPrjDrnHistoryData(PrjDrnInfoHistVO prjdrninfohistvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDrnInfoHistVO> getPrjDrnHistoryData = prjdrninfohistService.getPrjDrnHistoryData(prjdrninfohistvo);
		result.add(getPrjDrnHistoryData);
		return result;
	}
}
