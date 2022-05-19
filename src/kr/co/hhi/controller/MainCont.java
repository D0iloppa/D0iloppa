package kr.co.hhi.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.hhi.model.DrnLineVO;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.GlobalSearchVO;
import kr.co.hhi.model.NotificationVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.ProjectBbsVO;
import kr.co.hhi.model.ProjectNoticeVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.HomeService;
import kr.co.hhi.service.NotificationService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.SessionService;
import kr.co.hhi.service.TreeService;

@Controller
@RequestMapping("/*")
public class MainCont {

	protected PrjInfoService prjinfoService;
	protected HomeService homeService;
	protected NotificationService notificationService;
	protected SessionService sessionService;
	protected TreeService treeService;
	
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	
	@Autowired
	public void setHomeService(HomeService homeService) {
		this.homeService = homeService;
	}
	
	@Autowired
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	
	/**
	 * 1. 메소드명 : main
	 * 2. 작성일: 2021-11-17
	 * 3. 작성자: 소진희
	 * 4. 설명: 메인페이지로 가는 컨트롤러
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "main.do")
	public ModelAndView main(HttpServletRequest request,PrjInfoVO prjinfovo) {
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		if(serverPort==443) {
			mv.addObject("current_server_domain", "https://"+serverName+"/");
		}else {
			if(serverPort!=80) {
				mv.addObject("current_server_domain", "http://"+serverName+":"+serverPort+"/");
			}else {
				mv.addObject("current_server_domain", "http://"+serverName+"/");
			}
		}
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		if(prjinfoService.getLastConPrj(sessioninfo)==null) {
			
			// 유저 타입에 따른 처리
			String user_type = prjinfoService.selectOne("prjinfoSqlMap.getUserType",sessioninfo.getUser_id());
			if(user_type == null) user_type = "-1";
			
			if(user_type.equals("1")) {
				prjinfovo.setPrj_id("0000000001");
				PrjInfoVO getFirstPrj = prjinfoService.getPrjInfo(prjinfovo);
				//마지막 접속시 조회한 프로젝트 정보가 없을 경우 제일 처음 프로젝트를 가져옴
				mv.addObject("getLastConPrj", getFirstPrj);
			}else {
				List<PrjInfoVO> prjList = treeService.getPrjGridList(sessioninfo.getUser_id());
				// 최초로그인시, 접근 가능한 프로젝트의 첫번째 리스트에 접근
				if(prjList.size()>0) {
					String prj_id = prjList.get(0).getPrj_id();
					prjinfovo.setPrj_id(prj_id);
					PrjInfoVO getFirstPrj = prjinfoService.getPrjInfo(prjinfovo);
					//getFirstPrj.setPrj_nm("SELECT THE PROJECT");
					mv.addObject("getLastConPrj", getFirstPrj);
				}
			}
			
			
			
			
		}else {
			//마지막 접속시 조회한 프로젝트 정보 가져옴
			PrjInfoVO getLastConPrj = new PrjInfoVO();
			if ( prjinfovo.getPrj_id() !=null && prjinfovo.getPrj_id().length() > 0 ) {
				getLastConPrj =  prjinfoService.getPrjInfo(prjinfovo);
			} else {
				getLastConPrj =  prjinfoService.getLastConPrj(sessioninfo);
			}
			 
			mv.addObject("getLastConPrj", getLastConPrj);
		}
		List<PrjInfoVO> getPrjInfoList = prjinfoService.getPrjInfoListTop(sessioninfo);
		mv.addObject("getPrjInfoList", getPrjInfoList);
		mv.setViewName("/main");
		return mv;
	}

	/**
	 * 1. 메소드명 : newTab
	 * 2. 작성일: 2021-11-23
	 * 3. 작성자: 소진희
	 * 4. 설명: 헤더-프로젝트선택-컨텍스트 메뉴-새탭으로 열기 클릭시 타게되는 컨트롤러
	 * 5. 수정일: 2022-03-29
	 */
	@RequestMapping(value = "newTab.do")
	public ModelAndView newTab(HttpServletRequest request,PrjInfoVO prjinfovo,@RequestParam(value = "prj_id") String prj_id) {
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		if(serverPort==443) {
			mv.addObject("current_server_domain", "https://"+serverName+"/");
		}else {
			if(serverPort!=80) {
				mv.addObject("current_server_domain", "http://"+serverName+":"+serverPort+"/");
			}else {
				mv.addObject("current_server_domain", "http://"+serverName+"/");
			}
		}
		PrjInfoVO getLastConPrj = new PrjInfoVO();
		prjinfovo.setPrj_id(prj_id);
		getLastConPrj =  prjinfoService.getPrjInfo(prjinfovo);
		mv.addObject("getLastConPrj", getLastConPrj);
		if(prj_id!=null) {
			mv.addObject("newTab_prj_id", prj_id);
			prjinfovo.setPrj_id(prj_id);
			PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
			mv.addObject("getPrjInfo", getPrjInfo);
		}
		List<PrjInfoVO> getPrjInfoList = prjinfoService.getPrjInfoListTop(sessioninfo);
		mv.addObject("getPrjInfoList", getPrjInfoList);
		mv.setViewName("/main");
		return mv;
	}
	
	
	/**
	 * 1. 메소드명 : headerRefresh
	 * 2. 작성일: 2021-11-29
	 * 3. 작성자: 권도일
	 * 4. 설명: db상의 프로젝트 정보가 변경되었을 경우, 프로젝트 리스트 갱신
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "headerRefresh.do")
	@ResponseBody
	public Object headerRefresh(HttpServletRequest request) {
	
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");

		List<Object> result = new ArrayList<Object>();
		
		List<PrjInfoVO> getPrjInfoList = prjinfoService.getPrjInfoList(sessioninfo);
		
		result.add(getPrjInfoList);
		
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : getBbsList
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 권도일
	 * 4. 설명: home 탭의 Bulletin board 영역
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getHomeCont.do")
	@ResponseBody
	public Object getHomeCont(ProjectNoticeVO projectnoticeVo) {
		
		
		List<Object> result = new ArrayList<Object>();
		
		List<ProjectNoticeVO> bbsTable = homeService.getBbsContents(projectnoticeVo);
		
		result.add(bbsTable);

		
		
		return result;
	}
	
	
	
	@RequestMapping(value = "getCrDelayList.do")
	@ResponseBody
	public ModelAndView getCrDelayList(NotificationVO getDataVO) {
		
		
		ModelAndView result = new ModelAndView();
		
		Calendar cal = Calendar.getInstance();

	    String today = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    getDataVO.setReg_date(today);
	    
		List<PrjDocumentIndexVO> getDelayList = homeService.getCrDelayList(getDataVO);
		
		
		result.addObject("delayList",getDelayList);
		

		
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getSummarys
	 * 2. 작성일: 2022. 3. 1.
	 * 3. 작성자: doil
	 * 4. 설명: summary들을 구함 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getSummarys.do")
	@ResponseBody
	public ModelAndView getSummarys(String prj_id , HttpServletRequest request) {
		
		ModelAndView result = new ModelAndView();

		NotificationVO getDataVO = new NotificationVO();
		getDataVO.setPrj_id(prj_id);
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String login_id = sessioninfo.getUser_id();
		
		String user_type = homeService.getUserType(login_id);
		
		Calendar cal = Calendar.getInstance();
	    // 한달전
		cal.add(Calendar.MONTH , -1);
	    String beforeMonth = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    // 복원 및 일주일 전
	    cal.add(Calendar.MONTH , 1);
	    cal.add(Calendar.DATE, -7);
	    String beforeWeek = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    // 복원 및 하루 전
	    cal.add(Calendar.DATE, 7);
	    cal.add(Calendar.DATE, -1);
	    String yesterDay = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    

		
		
		// tr status
	    List<List<NotificationVO>> todoList = new ArrayList<>();
	    if(user_type.equals("1"))
	    	todoList = homeService.getTodoList(getDataVO);
		
		
		HashMap<String,HashMap<String,Integer>> tr = new HashMap<>();
		if(user_type.equals("1"))
			tr = homeService.getTRsummary(getDataVO);
		HashMap<String,HashMap<String,Integer>> corr = new HashMap<>();
		if(user_type.equals("1"))
			corr = homeService.getCorrSummaryList(getDataVO);
		
		
		
		List<DrnLineVO> getDrnSummary = new ArrayList<>();
		if(user_type.equals("1"))
			getDrnSummary = homeService.getDrnSummary(prj_id);
		

		
	
		result.addObject("todoList",todoList);
		result.addObject("drnList",getDrnSummary);
		result.addObject("tr",tr);
		result.addObject("corr",corr);
		
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getCRTabInfo
	 * 2. 작성일: 2022. 4. 19.
	 * 3. 작성자: doil
	 * 4. 설명: home화면에서 CR 셀 클릭시 해당 탭으로 이동하기 위한 폴더id 찾기 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getCRTabInfo.do")
	@ResponseBody
	public ModelAndView getCRTabInfo(String prj_id , String crType , String inOut , HttpServletRequest request) {
		
		ModelAndView result = new ModelAndView();

		FolderInfoVO getDataVO = new FolderInfoVO();
		getDataVO.setPrj_id(prj_id);
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String login_id = sessioninfo.getUser_id();
		
		getDataVO.setUser_id(login_id);
		getDataVO.setFolder_type(crType);
		
		String setInout = null;
		
		switch(inOut) {
			case "IN":
				setInout = "%INCOMING%";
				break;
			case "OUT":
				setInout = "%OUTGOING%";
				break;
			default:
				setInout = "";
				break;
		}
		
		
		long cr_folder_id = 0;
		String cr_folder_path = null;
		
		List<FolderInfoVO> getDataVOList = homeService.selectList("homeSqlMap.getCRTabInfo",getDataVO);
		if(getDataVOList.size()>0) {
			cr_folder_id = getDataVOList.get(0).getFolder_id();
			cr_folder_path = getDataVOList.get(0).getFolder_path_str();
			getDataVO = getDataVOList.get(0);
		}
		

		
		long cr_subFolder_id = 0;
		String cr_subFolder_path = null;
		
		List<FolderInfoVO> getSubDataVO  = homeService.selectList("homeSqlMap.getCRSuvTabInfo",getDataVO);
		if(getSubDataVO.size()>0) {
			cr_subFolder_id = getSubDataVO.get(0).getFolder_id();
			cr_subFolder_path = getSubDataVO.get(0).getFolder_path_str();
		}
		
		if(cr_subFolder_id == 0) cr_subFolder_id = cr_folder_id;
		if(cr_subFolder_path == null || cr_subFolder_path.equals("")) cr_subFolder_path = cr_folder_path;
		
		result.addObject("cr_folder_id",cr_subFolder_id);
		result.addObject("cr_folder_path",cr_subFolder_path);
		
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : getDelayDocSummary
	 * 2. 작성일: 2022. 3. 1.
	 * 3. 작성자: doil
	 * 4. 설명: 제출 지연 도서목록 summary를 구함 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getDelayDocSummary.do")
	@ResponseBody
	public ModelAndView getDelayDocSummary(String prj_id) {
		
		ModelAndView result = new ModelAndView();

		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getTrStatusSummary
	 * 2. 작성일: 2022. 3. 1.
	 * 3. 작성자: doil
	 * 4. 설명: TR status
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getTrStatusSummary.do")
	@ResponseBody
	public ModelAndView getTrStatusSummary(String prj_id) {
		
		ModelAndView result = new ModelAndView();

		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getCrStatusSummary
	 * 2. 작성일: 2022. 3. 1.
	 * 3. 작성자: doil
	 * 4. 설명: CR status
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getCrStatusSummary.do")
	@ResponseBody
	public ModelAndView getCrStatusSummary(String prj_id) {
		
		ModelAndView result = new ModelAndView();

		
		return result;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 1. 메소드명 : prjHomecancleHit
	 * 2. 작성일: 2021-11-26
	 * 3. 작성자: 권도일
	 * 4. 설명: home 탭의 Bulletin board 영역
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "prjHomecancleHit.do")
	@ResponseBody
	public Object prjHomecancleHit(ProjectBbsVO projectBbsVo) {
		
		
		List<Object> result = new ArrayList<Object>();
		
		// 조회수 update
		homeService.hitcntUp(projectBbsVo);
		
		List<ProjectBbsVO> bbsTable = homeService.getBbsContentsHitup(projectBbsVo);
		
		result.add(bbsTable);		
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : getBbsList
	 * 2. 작성일: 2022-01-16
	 * 3. 작성자: 박정우
	 * 4. 설명: home 탭의 Bulletin board 행 더블 클릭시
	 * 5. 수정일: 
	 */
//	@RequestMapping(value = "getHomeContDetail.do")
//	@ResponseBody
//	public Object getHomeContDetail(ProjectBbsVO projectBbsVo) {
//		
//		
//		List<Object> result = new ArrayList<Object>();
//		
//		ProjectBbsVO getHomeContDetail = homeService.getHomeContDetail(projectBbsVo);
//		
//		result.add(getHomeContDetail);
//
//		
//		
//		return result;
//	}
}
