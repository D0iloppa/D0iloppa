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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjEmailTypeService;
import kr.co.hhi.service.PrjStepService;

@Controller
@RequestMapping("/*")
public class PrjStepCont {

	protected PrjStepService  prjstepService;
	
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}

	/**
	 * 1. 메소드명 : getPrjStep
	 * 2. 작성일: 2021-12-21
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_code_settings 테이블에서 각 코드 타입에 맞는 STEP 정보 가져오기
	 * 5. 수정일: 2021-12-27
	 */
	@RequestMapping(value = "getPrjStep.do")
	@ResponseBody
	public Object getPrjStep(PrjCodeSettingsVO prjcodesettingsvo,PrjStepVO prjstepvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> getPrjStepCode = prjstepService.getPrjStepCode(prjcodesettingsvo);
		List<PrjStepVO> getPrjStep = new ArrayList<PrjStepVO>();
		
		if(prjstepvo.getType().equals("DCI")) {
			getPrjStep = prjstepService.getPrjStep(prjstepvo);
		}else if(prjstepvo.getType().equals("VDCI")) {
			getPrjStep = prjstepService.getVendorPrjStep(prjstepvo);
		}else if(prjstepvo.getType().equals("PCI")) {
			getPrjStep = prjstepService.getProcPrjStep(prjstepvo);
		}else if(prjstepvo.getType().equals("MCI")) {
			getPrjStep = prjstepService.getBulkPrjStep(prjstepvo);
		}else if(prjstepvo.getType().equals("SDCI")) {
			getPrjStep = prjstepService.getSitePrjStep(prjstepvo);
		}
		
		HashMap<String, HashMap<String,String>> steps = new HashMap<>();
		for(PrjCodeSettingsVO bean : getPrjStepCode) {
			String key = bean.getSet_code_id();
			steps.put(key, new HashMap<String,String>());
		}
		
		for(PrjStepVO row : getPrjStep) {
			String key = row.getStep_code_id();
			if(key!=null) {
				steps.get(key).put("plan_date",row.getPlan_date());
				steps.get(key).put("actual_date",row.getActual_date());
				steps.get(key).put("fore_date",row.getFore_date());
				steps.get(key).put("tr_no",row.getTr_no());
			}
		}
		
		result.add(getPrjStepCode);
		result.add(getPrjStep);
		result.add(steps);
		
		return result;
	}
}
