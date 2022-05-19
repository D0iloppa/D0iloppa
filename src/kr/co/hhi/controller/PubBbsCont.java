package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.model.PubBbsContentsVO;
import kr.co.hhi.service.PubBbsService;

@Controller
@RequestMapping("/*")
public class PubBbsCont {

	protected PubBbsService pubbbsService;

	@Autowired
	public void setPubBbsService(PubBbsService pubbbsService) {
		this.pubbbsService = pubbbsService;
	}
	
	@RequestMapping(value = "getPubBbsContents.do")
	@ResponseBody
	public Object getPubBbsContents(PubBbsContentsVO pubBbsContentsVo) {
		List<Object> result = new ArrayList<Object>();
		
		if(pubBbsContentsVo.getBbs_seq() > 0) {
			// 조회수 update
			pubbbsService.updateHit(pubBbsContentsVo);
			PubBbsContentsVO getPubBbsContOne =  pubbbsService.getPubBbsContentOne(pubBbsContentsVo);
			result.add(getPubBbsContOne);
		}else {
			List<PubBbsContentsVO> getPubBbsContents =  pubbbsService.getPubBbsContents();
			result.add(getPubBbsContents);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : getLoginDetailFileList
	 * 2. 작성일: 2021-01-15
	 * 3. 작성자: 박정우
	 * 4. 설명: 게시판 상세페이지, 게시판 클릭시 횟수 저장
	 * 5. 수정일: 
	 */
	
//	@RequestMapping(value = "getLoginDetailFileList.do")
//	@ResponseBody
//	public Object getLoginDetailFileList(PubBbsContentsVO pubBbsContentsVo) {
//		List<Object> result = new ArrayList<Object>();
//		
//		PubBbsContentsVO getLoginDetailFileList =  pubbbsService.getPubBbsContentOne(pubBbsContentsVo);
//		result.add(getLoginDetailFileList);
//		
//		return result;
//	}
}
