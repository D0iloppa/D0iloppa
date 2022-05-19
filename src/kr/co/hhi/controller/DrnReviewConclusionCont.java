package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.DrnReviewConclusionService;
import kr.co.hhi.service.PrjCodeSettingsService;
import kr.co.hhi.service.PrjDocumentIndexService;

@Controller
@RequestMapping("/*")
public class DrnReviewConclusionCont {
	
	protected DrnReviewConclusionService drnReviewConclusionService;
	protected PrjCodeSettingsService prjcodesettingsService;
	
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	@Autowired
	public void setPrjDocumentIndexService(DrnReviewConclusionService drnReviewConclusionService) {
		this.drnReviewConclusionService = drnReviewConclusionService;
	}
	
	/**
	 * 1. 메소드명 : drnR_getCodeList
	 * 2. 작성일: 2021-01-17
	 * 3. 작성자: 박정우
	 * 4. 설명: 전체 drn review 코드를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "drnR_getCodeList.do")
	@ResponseBody
	public Object drnRgetCodeList(PrjCodeSettingsVO prjCodeSettingsVo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjCodeSettingsVO> drnR_getCode =  drnReviewConclusionService.drnRgetCodeList(prjCodeSettingsVo);
		result.add(drnR_getCode);
		return result;
	}
	
	/**
	 * 1. 메소드명 : drnR_getCode
	 * 2. 작성일: 2021-01-17
	 * 3. 작성자: 박정우
	 * 4. 설명: drn review 코드를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "drnR_getCode.do")
	@ResponseBody
	public Object drnRgetCode(PrjCodeSettingsVO prjCodeSettingsVo) {
		List<Object> result = new ArrayList<Object>();
		PrjCodeSettingsVO drnR_getCode =  drnReviewConclusionService.drnRgetCode(prjCodeSettingsVo);
		result.add(drnR_getCode);
		return result;
	}
	
	/**
	 * 1. 메소드명 : drnR_getCodeUp
	 * 2. 작성일: 2021-01-17
	 * 3. 작성자: 박정우
	 * 4. 설명: drn review 코드 업데이트
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "drnR_getCodeUp.do")
	@ResponseBody
	public int drnR_getCodeUp(HttpServletRequest request, PrjCodeSettingsVO prjCodeSettingsVo) {
		
		int result = 0;
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		String chkCodeId = prjCodeSettingsVo.getSet_code_id();
		
		//저장된 값이 없을때 새로 저장
		if(chkCodeId == null) {
			prjCodeSettingsVo.setReg_id(sessioninfo.getUser_id());
			prjCodeSettingsVo.setReg_date(CommonConst.currentDateAndTime());
			
			drnReviewConclusionService.drnRgetCodeInsert(prjCodeSettingsVo);
			result = 1;
		}else {
			prjCodeSettingsVo.setMod_id(sessioninfo.getUser_id());
			prjCodeSettingsVo.setMod_date(CommonConst.currentDateAndTime());
			
			drnReviewConclusionService.drnRgetCodeUp(prjCodeSettingsVo);
			result = 2;
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : drnR_getCodeCopy
	 * 2. 작성일: 2021-01-17
	 * 3. 작성자: 박정우
	 * 4. 설명: drn review 코드 업데이트
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "drnR_getCodeCopy.do")
	@ResponseBody
	public int drnR_getCodeCopy(HttpServletRequest request, PrjCodeSettingsVO prjCodeSettingsVo) {
		
		int result = 0;
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		prjCodeSettingsVo.setReg_id(sessioninfo.getUser_id());
		prjCodeSettingsVo.setReg_date(CommonConst.currentDateAndTime());
		
		// 디폴트 프로젝트 ID인 0000000001는 다른 프로젝트로 부터 복사 붙여넣기 하지 않는다.
		if(prjCodeSettingsVo.getPrj_id().equals("0000000001") || prjCodeSettingsVo.getPrj_id().equals(prjCodeSettingsVo.getCopy_prj_id())) {
			result = 1;
			return result;
		}
		
		PrjCodeSettingsVO chkDescVo = drnReviewConclusionService.drnRchkDesc(prjCodeSettingsVo);
		
		if(chkDescVo == null || chkDescVo.equals("")) {
			result = -1;
			return result;
		}
		// 복사 전, 현재 테이블은 비운다.
		prjcodesettingsService.pg05_ClearTbl(prjCodeSettingsVo);
		// 제거 후, 데이터를 복사해 온다.
		prjcodesettingsService.pg05_copyTbl(prjCodeSettingsVo);
		
		return result;
	}
}
