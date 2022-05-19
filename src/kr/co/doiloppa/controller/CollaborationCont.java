package kr.co.doiloppa.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.doiloppa.model.CollaborationVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.CollaborationService;

@Component
@Controller
@RequestMapping("/*")
public class CollaborationCont {
	
	protected CollaborationService collaborationService;
	
	@Autowired
	public void setCollaborationService(CollaborationService collaborationService) {
		this.collaborationService = collaborationService;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getCollaboList
	 * 2. 작성일: 2022. 1. 14.
	 * 3. 작성자: doil
	 * 4. 설명: 상태값, 조건에 따른 검색
	 * 5. 수정일: doil
	 */
	
	@RequestMapping(value = "getCollaboList.do")
	@ResponseBody
	public ModelAndView getCollaboList(HttpServletRequest request, CollaborationVO collaborationVo) {
		ModelAndView result = new ModelAndView();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());

		
		String from = collaborationVo.getFrom();
		from = from.replaceAll("-", "");
		String to = collaborationVo.getTo();
		to = to.replaceAll("-", "");
		
		collaborationVo.setFrom(from);
		collaborationVo.setTo(to);
		
		ArrayList<String> statusList =  new ArrayList<>(Arrays.asList(collaborationVo.getStatus().split(",")));
		collaborationVo.setStatusList(statusList);
		
		
		List<CollaborationVO> getCollaboList = collaborationService.getCollaboList(collaborationVo);
		result.addObject("getCollaboList",getCollaboList);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getAllPrjList_DRN_filter
	 * 2. 작성일: 2022. 4. 2.
	 * 3. 작성자: doil
	 * 4. 설명: DRN 전체현황함 프로젝트 select에 모든 프로젝트 리스트를 추가함 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getAllPrjList_DRN_filter.do")
	@ResponseBody
	public ModelAndView getAllPrjList_DRN_filter() {
		ModelAndView result = new ModelAndView();
		
		List<PrjInfoVO> allPrj = collaborationService.getAllPrjList();
		
		result.addObject("prj_list",allPrj);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getCollaboGrid
	 * 2. 작성일: 2022. 1. 17.
	 * 3. 작성자: doil
	 * 4. 설명: 상태에 따른 콜라보 리스트 검색 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getCollaboGrid.do")
	@ResponseBody
	public ModelAndView getCollaboGrid(HttpServletRequest request, CollaborationVO collaborationVo) {
		ModelAndView result = new ModelAndView();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());

		
		String from = collaborationVo.getFrom();
		from = from.replaceAll("-", "");
		String to = collaborationVo.getTo();
		to = to.replaceAll("-", "");
		String admin_yn = "";
		admin_yn = sessioninfo.getAdmin_yn();
		
		collaborationVo.setFrom(from);
		collaborationVo.setTo(to);
		collaborationVo.setAdmin_yn(admin_yn);
		
		List<CollaborationVO> getCollaboList = null;
		String[] cases = {"기안함","작성중","미결","예고","진행","반려","완료","전체현황함","지연현황함"};
		int i;
		for(i=0;i<cases.length;i++)
			if(collaborationVo.getStatus().contains(cases[i])) break;
		
		switch(i) {
			case 0 :
				getCollaboList = collaborationService.getCollaboList(collaborationVo);
				break;
			case 1	:
				getCollaboList = collaborationService.getCollaboWriteList(collaborationVo);
				break;
			case 2	:
				getCollaboList = collaborationService.getCollaboPendingList(collaborationVo);
				break;
			case 3	:
				getCollaboList = collaborationService.getCollaboPrevList(collaborationVo);
				break;
			case 4	:
				getCollaboList = collaborationService.getCollaboProgList(collaborationVo);
				break;
			case 5	:
				getCollaboList = collaborationService.getCollaboRejectList(collaborationVo);
				break;
			case 6	:
				getCollaboList = collaborationService.getCollaboCompList(collaborationVo);
				break;
			case 7	:
				getCollaboList = collaborationService.getCollaboWholeList(collaborationVo);
				break;
			case 8	:
				getCollaboList = collaborationService.getCollaboDelayedList(collaborationVo);
				break;
		}
		
		
		result.addObject("getCollaboList",getCollaboList);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getCollaboCount
	 * 2. 작성일: 2022. 2. 6.
	 * 3. 작성자: doil
	 * 4. 설명: DRN 처리해야할 리스트의 갯수를 메뉴에 표시 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getCollaboCount.do")
	@ResponseBody
	public ModelAndView getCollaboCount(HttpServletRequest request, CollaborationVO collaborationVo) {
		ModelAndView result = new ModelAndView();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());


		String admin_yn = "";
		admin_yn = sessioninfo.getAdmin_yn();

		collaborationVo.setAdmin_yn(admin_yn);
		collaborationVo.setFrom("");
		collaborationVo.setTo("");
		
		List<CollaborationVO> getCollaboList = null;
		// status별 카운트를 구하기 위함
		String[] titles = {"작성중","미결","예고","진행","반려","완료","전체현황함","지연현황함"};
		
		int titlesIdx = 0;
		int count = 0;
		// 기안함
		getCollaboList = collaborationService.getCollaboList(collaborationVo);
		//result.addObject(titles[titlesIdx++],getCollaboList.size());
		// 작성중
		getCollaboList = collaborationService.getCollaboWriteList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		// 미결
		getCollaboList = collaborationService.getCollaboPendingList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		count += getCollaboList.size();
		// 예고
		getCollaboList = collaborationService.getCollaboPrevList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		count += getCollaboList.size();
		// 진행
		getCollaboList = collaborationService.getCollaboProgList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		count += getCollaboList.size();
		// 반려
		getCollaboList = collaborationService.getCollaboRejectList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		count += getCollaboList.size();
		// 완료
		getCollaboList = collaborationService.getCollaboCompList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		count += getCollaboList.size();
		// 결재함
		//result.addObject(titles[titlesIdx++],count);
		
		getCollaboList = collaborationService.getCollaboWholeList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());
		
		getCollaboList = collaborationService.getCollaboDelayedList(collaborationVo);
		result.addObject(titles[titlesIdx++],getCollaboList.size());

		return result;
	}
	
	/**
	 * 1. 메소드명 : selectCollaboList
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 박정우
	 * 4. 설명: 기안함 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboList.do")
	@ResponseBody
	public Object selectCollaboList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboList = collaborationService.selectCollaboList(collaborationVo);
		result.add(selectCollaboList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboWriteList
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 박정우
	 * 4. 설명: 작성중 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboWriteList.do")
	@ResponseBody
	public Object getCollaboWriteList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboWriteList = collaborationService.getCollaboWriteList(collaborationVo);
		result.add(getCollaboWriteList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : selectCollaboWriteList
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 박정우
	 * 4. 설명: 작성중 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboWriteList.do")
	@ResponseBody
	public Object selectCollaboWriteList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboWriteList = collaborationService.selectCollaboWriteList(collaborationVo);
		result.add(selectCollaboWriteList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboWrite
	 * 2. 작성일: 2021-12-29
	 * 3. 작성자: 박정우
	 * 4. 설명: 작성중 상세 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboWrite.do")
	@ResponseBody
	public Object getCollaboWrite(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		CollaborationVO getCollaboWrite = collaborationService.getCollaboWrite(collaborationVo);
		result.add(getCollaboWrite);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboPendingList
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 박정우
	 * 4. 설명: 미결 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboPendingList.do")
	@ResponseBody
	public Object getCollaboPendingList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboPendingList = collaborationService.getCollaboPendingList(collaborationVo);
		result.add(getCollaboPendingList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : selectCollaboPendingList
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 박정우
	 * 4. 설명: 미결 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboPendingList.do")
	@ResponseBody
	public Object selectCollaboPendingList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboPendingList = collaborationService.selectCollaboPendingList(collaborationVo);
		result.add(selectCollaboPendingList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboPending
	 * 2. 작성일: 2021-12-30
	 * 3. 작성자: 박정우
	 * 4. 설명: 미결 상세 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	 @RequestMapping(value = "getCollaboPending.do")
		@ResponseBody
		public Object getCollaboPending(HttpServletRequest request,CollaborationVO collaborationVo) {
			List<Object> result = new ArrayList<Object>();
			
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			collaborationVo.setReg_id(sessioninfo.getUser_id());
			
			CollaborationVO getCollaboPending = collaborationService.getCollaboPending(collaborationVo);
			result.add(getCollaboPending);
			return result;
		}
	
	/**
	 * 1. 메소드명 : getCollaboPrevList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 예고 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboPrevList.do")
	@ResponseBody
	public Object getCollaboPrevList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboPrevList = collaborationService.getCollaboPrevList(collaborationVo);
		result.add(getCollaboPrevList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : selectCollaboPrevList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 예고 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboPrevList.do")
	@ResponseBody
	public Object selectCollaboPrevList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboPrevList = collaborationService.selectCollaboPrevList(collaborationVo);
		result.add(selectCollaboPrevList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboPrev
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 박정우
	 * 4. 설명: 예고 상세 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	 @RequestMapping(value = "getCollaboPrev.do")
		@ResponseBody
		public Object getCollaboPrev(HttpServletRequest request,CollaborationVO collaborationVo) {
			List<Object> result = new ArrayList<Object>();
			
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			collaborationVo.setReg_id(sessioninfo.getUser_id());
			
			CollaborationVO getCollaboPrev = collaborationService.getCollaboPrev(collaborationVo);
			result.add(getCollaboPrev);
			return result;
		}
	
	/**
	 * 1. 메소드명 : getCollaboPrevList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 진행 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboProgList.do")
	@ResponseBody
	public Object getCollaboProgList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboProgList = collaborationService.getCollaboProgList(collaborationVo);
		result.add(getCollaboProgList);
		return result;
	}	
	
	/**
	 * 1. 메소드명 : selectCollaboProgList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 진행 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboProgList.do")
	@ResponseBody
	public Object selectCollaboProgList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboProgList = collaborationService.selectCollaboProgList(collaborationVo);
		result.add(selectCollaboProgList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboPrevList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 반려 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboRejectList.do")
	@ResponseBody
	public Object getCollaboRejectList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboRejectList = collaborationService.getCollaboRejectList(collaborationVo);
		result.add(getCollaboRejectList);
		return result;
	}	
	
	/**
	 * 1. 메소드명 : selectCollaboProgList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 반려 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboRejectList.do")
	@ResponseBody
	public Object selectCollaboRejectList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboRejectList = collaborationService.selectCollaboRejectList(collaborationVo);
		result.add(selectCollaboRejectList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboPrevList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 완료 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboCompList.do")
	@ResponseBody
	public Object getCollaboCompList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboCompList = collaborationService.getCollaboCompList(collaborationVo);
		result.add(getCollaboCompList);
		return result;
	}	
	
	/**
	 * 1. 메소드명 : selectCollaboProgList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 반려 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboCompList.do")
	@ResponseBody
	public Object selectCollaboCompList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboCompList = collaborationService.selectCollaboCompList(collaborationVo);
		result.add(selectCollaboCompList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboWholeList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 전체현황 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboWholeList.do")
	@ResponseBody
	public Object getCollaboWholeList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboWholeList = collaborationService.getCollaboWholeList(collaborationVo);
		result.add(getCollaboWholeList);
		return result;
	}	
	
	/**
	 * 1. 메소드명 : selectCollaboWholeList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 전체현황 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboWholeList.do")
	@ResponseBody
	public Object selectCollaboWholeList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboWholeList = collaborationService.selectCollaboWholeList(collaborationVo);
		result.add(selectCollaboWholeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getCollaboDelayedList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 지연현황 페이지 등록된 결제 리스트 를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getCollaboDelayedList.do")
	@ResponseBody
	public Object getCollaboDelayedList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> getCollaboDelayedList = collaborationService.getCollaboDelayedList(collaborationVo);
		result.add(getCollaboDelayedList);
		return result;
	}	
	
	/**
	 * 1. 메소드명 : selectCollaboDelayedList
	 * 2. 작성일: 2021-12-23
	 * 3. 작성자: 박정우
	 * 4. 설명: 지연현황 페이지 등록된 결제 리스트 를 검색함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "selectCollaboDelayedList.do")
	@ResponseBody
	public Object selectCollaboDelayedList(HttpServletRequest request,CollaborationVO collaborationVo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		collaborationVo.setReg_id(sessioninfo.getUser_id());
		
		List<CollaborationVO> selectCollaboDelayedList = collaborationService.selectCollaboDelayedList(collaborationVo);
		result.add(selectCollaboDelayedList);
		return result;
	}
	
	
	
}
