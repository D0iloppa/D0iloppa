/**
 * 
 */
package kr.co.doiloppa.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author doil
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

import com.ibm.icu.text.SimpleDateFormat;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.CodeInfoVO;
import kr.co.doiloppa.model.DictionarySearchVO;
import kr.co.doiloppa.model.FolderAuthVO;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.FolderAuthService;
import kr.co.doiloppa.service.PrjCodeSettingsService;
import kr.co.doiloppa.service.PrjDocumentIndexService;
import kr.co.doiloppa.service.PrjInfoService;
import kr.co.doiloppa.service.ProcessInfoService;
import kr.co.doiloppa.service.TreeService;

@Controller
@RequestMapping("/*")

public class TreeCont {

	protected TreeService treeService;
	protected PrjInfoService prjinfoService;
	protected PrjDocumentIndexService prjdocumentindexService;
	protected ProcessInfoService processinfoService;
	protected FolderAuthService folderAuthService;
	protected PrjCodeSettingsService prjcodesettingsService;

	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setProcessInfoService(ProcessInfoService processinfoService) {
		this.processinfoService = processinfoService;
	}

	@Autowired
	public void setCollaborationService(FolderAuthService folderAuthService) {
		this.folderAuthService = folderAuthService;
	}
	
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	
	
	@RequestMapping(value = "setSession.do")
	@ResponseBody
	public void setSession(HttpServletRequest request,FolderInfoVO folderinfovo) {
		List<Object> result = new ArrayList<Object>();
		
		HttpSession session = request.getSession();
		session.setAttribute("prj_id", folderinfovo.getPrj_id());
		session.setAttribute("folder_id", folderinfovo.getFolder_id());
		
		System.out.println("prj_id:" + session.getAttribute("prj_id")+ " | " + "folder_id:" +session.getAttribute("folder_id"));
		
		
	}
	
	
	@RequestMapping(value = "tab.do", method = RequestMethod.POST)
	public ModelAndView tabCont(HttpServletRequest request,@RequestParam(value = "path") String path
			,@RequestParam(value="prjId", required=false) String prjId, @RequestParam(value="folder_id", required=false, defaultValue = "0") long folder_id) {
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		mv.setViewName(path);
		// page폴더 내의 페이지 이름을 분리해낸다.
		path = path.replace("/page/", "");


		if(path.equals("projectCreation")) {
			List<PrjInfoVO> getPrjList =  prjinfoService.getPrjList();
			mv.addObject("getPrjList", getPrjList);
			
//			PrjInfoVO prjInfo = new PrjInfoVO();
//			prjInfo.setPrj_id(prjId);
			List<CodeInfoVO> getPrjType = prjinfoService.getPrjType();
			mv.addObject("getPrjType",getPrjType);
		}else if(path.equals("drnReviewConclusion")) {
			List<PrjInfoVO> getPrjList =  prjinfoService.getPrjList();
			mv.addObject("getPrjList", getPrjList);
		}else if(path.equals("projectGeneral")) {
			List<PrjInfoVO> getPrjInfoList = prjinfoService.getPrjInfoList(sessioninfo);
			mv.addObject("getPrjInfoList", getPrjInfoList);
		}else if(path.equals("wbsCodeControl")) {
			System.out.println(prjId);
			PrjInfoVO prjInfo = new PrjInfoVO();
			prjInfo.setPrj_id(prjId);
			List<PrjDisciplineVO> getDiscipList = prjinfoService.getDiscipList(prjInfo);
			System.out.println(getDiscipList.size());
			mv.addObject("discipList",getDiscipList);
		}else if(path.equals("documentControl")) {
			PrjInfoVO prjInfo = new PrjInfoVO();
			prjInfo.setPrj_id(prjId);
			List<PrjCodeSettingsVO> getRevNo = prjdocumentindexService.getRevNo(prjInfo);
			mv.addObject("revList",getRevNo);
			List<PrjCodeSettingsVO> getIFCReason = prjdocumentindexService.getIFCReason(prjInfo);
			mv.addObject("ifcList",getIFCReason);			
		}else if(path.equals("collaboPage")) {
			FolderInfoVO getFolderName = new FolderInfoVO();
			getFolderName.setPrj_id(prjId);
			getFolderName.setFolder_id(folder_id);
			String title = prjinfoService.getFolderName(getFolderName);			
			mv.addObject("text",title);
		}
		
		
		mv.addObject("viewName", path);
		return mv;
	}
	/**
	 * 
	 * 1. 메소드명 : getFolderListForPg01
	 * 2. 작성일: 2022-03-10
	 * 3. 작성자: 소진희
	 * 4. 설명: projectGenral 01 폴더정비페이지에서 folder_type=DCC인 폴더만 가져옴  
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getFolderListForPg01.do")
	@ResponseBody
	public Object getFolderListForPg01(FolderInfoVO folderinfovo) {
		List<Object> result = new ArrayList<>();
		List<FolderInfoVO> getFolderListForPg01 = treeService.getFolderListForPg01(folderinfovo);
		result.add(getFolderListForPg01);
		return result;
	}
	/**
	 * 
	 * 1. 메소드명 : getFolderList
	 * 2. 작성일: 2022. 1. 19.
	 * 3. 작성자: doil
	 * 4. 설명: 폴더리스트에 세션정보에 따른 권한정보를 매핑하여 넘겨준다.  
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getFolderList.do")
	@ResponseBody
	public Object getFolderList(HttpServletRequest request, PrjInfoVO prjinfovo) {
		
		
		// 로그인 세션 정보		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		prjinfovo.setUser_id(sessioninfo.getUser_id());
		// 관리자 여부 확인
		String isAdmin = treeService.isAdmin(sessioninfo.getUser_id());
		if(isAdmin == null) isAdmin = "N";
		// DCC 여부 확인
		String isDcc = treeService.isDcc(prjinfovo);
		if(isDcc == null) isDcc = "N";
		
		
		FolderInfoVO infoFromSession = new FolderInfoVO();
		infoFromSession.setUser_id(sessioninfo.getUser_id());
		infoFromSession.setPrj_id(prjinfovo.getPrj_id());
		
		
		List<Object> result = new ArrayList<Object>();
		
		// 세션의 user_id, org_id, grp_id를 구해야 한다.조직과 그룹은 순수 id가 아닌 path값을 구해야함
		List<String> org_Path = treeService.getOrgPath(sessioninfo.getUser_id());
		if (org_Path.size()>0) {
			String org_Paths = "";
			for (String org_path : org_Path) {
				org_Paths += org_path + "@";
				infoFromSession.setOrg_id(org_path);
			}
			org_Paths.substring(0,org_Paths.length()-1);
			infoFromSession.setOrg_id(org_Paths);
		}
		/*
		List<String> org_Path = treeService.getOrgPath(sessioninfo.getUser_id());
		if(org_Path != null) {
			for(String org_path : org_Path) infoFromSession.setOrg_id(org_path);
		}
		*/
		
		List<String> grp_Id = treeService.getGrpPath(infoFromSession);
		if(grp_Id != null) {
			for(String grp_id : grp_Id) infoFromSession.setGrp_id(grp_id);				
		}
		
		
		// 목록 보기 권한
		List<FolderInfoVO> getFolderList2 = treeService.getFolderVisible(infoFromSession,isAdmin,isDcc);
		
		result.add(getFolderList2); // data[0] 보여지는 폴더 리스트
		result.add(isAdmin); // data[1] dcc여부
		result.add(isDcc); // data[2] dcc여부
		
		
		return result;
	}
	
	@RequestMapping(value = "checkFolderAuth.do")
	@ResponseBody
	public ModelAndView checkFolderAuth(HttpServletRequest request , PrjInfoVO prjinfovo) {
		
		ModelAndView result = new ModelAndView();
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String login_id = sessioninfo.getUser_id();
		
		prjinfovo.setUser_id(sessioninfo.getUser_id());
		// 관리자 여부 확인
		String isAdmin = treeService.isAdmin(sessioninfo.getUser_id());
		if(isAdmin == null) isAdmin = "N";
		// DCC 여부 확인
		String isDcc = treeService.isDcc(prjinfovo);
		if(isDcc == null) isDcc = "N";
		
		FolderInfoVO infoFromSession = new FolderInfoVO();
		infoFromSession.setUser_id(sessioninfo.getUser_id());
		infoFromSession.setPrj_id(prjinfovo.getPrj_id());
		infoFromSession.setFolder_id(prjinfovo.getFolder_id());
		
		// 세션의 user_id, org_id, grp_id를 구해야 한다
		// 조직과 그룹은 순수 id가 아닌 path값을 구해야함
		List<String> org_Path = treeService.getOrgPath(sessioninfo.getUser_id());
		if (org_Path.size()>0) {
			String org_Paths = "";
			for (String org_path : org_Path) {
				org_Paths += org_path + "@";
				infoFromSession.setOrg_id(org_path);
			}
			org_Paths.substring(0,org_Paths.length()-1);
			infoFromSession.setOrg_id(org_Paths);
		}
		/*
		List<String> org_Path = treeService.getOrgPath(sessioninfo.getUser_id());
		if (org_Path != null) {
			for (String org_path : org_Path) {
				infoFromSession.setOrg_id(org_path);
			}
		}
		*/
    	

		List<String> grp_Id = treeService.getGrpPath(infoFromSession);
		if (grp_Id != null) {
			for (String grp_id : grp_Id) {
				infoFromSession.setGrp_id(grp_id);
			}
		}
		// FolderInfoVO getFolderWithAuth = treeService.getFolderWithAuth(infoFromSession);
		FolderInfoVO getFolderWithAuth = treeService.getFolderAuth(infoFromSession);
		String useDRN = "N";
		List<String> isUsingDRN = treeService.isUsingDRN(getFolderWithAuth);
		if(isUsingDRN.size()>0)
			useDRN = isUsingDRN.get(0);
		
		if(useDRN==null) useDRN = "N";
		
		// 대문자로 캐스팅
		useDRN = useDRN.toUpperCase();
		
		
		getFolderWithAuth.setUseDRN(useDRN);
		
		getFolderWithAuth = treeService.folderAuthCheck(getFolderWithAuth, isAdmin, isDcc);
		
		// 해당 프로젝트의 멤버인지 체크
		// 관리자의 경우 프로젝트의 멤버로 할당되지 않았더라도 프로젝트의 멤버로 취급
		String isPrjMember = treeService.isPrjMember(infoFromSession);
		if(isPrjMember.equals("N")) {
			// 프로젝트의 멤버가 아니라면 권한과 무관하게 쓰기/삭제 권한은 X
			String process_type = treeService.getFolderProcess(infoFromSession);
			// 폴더의 프로세스 타입이 할당되지 않은 General의 경우에는 쓰기/삭제 권한 부여 가능
			if(!process_type.equals("General")) {
				getFolderWithAuth.setAuth_w("N");
				getFolderWithAuth.setAuth_d("N");
			}
			// 폴더의 타입에 따른 쓰기/삭제 권한 처리
			String folder_type = treeService.selectOne("folderinfoSqlMap.getFolderType",infoFromSession);
			if(folder_type.equals("PBO")) {
				getFolderWithAuth.setAuth_w("N");
				getFolderWithAuth.setAuth_d("N");
			}
			
		}
		
		result.addObject("auth_check_r",getFolderWithAuth.getAuth_r());
		result.addObject("auth_check_w",getFolderWithAuth.getAuth_w());
		result.addObject("auth_check_d",getFolderWithAuth.getAuth_d());
		result.addObject("isAdmin",isAdmin);
		result.addObject("isDcc",isDcc);
		
		System.out.printf("(%d@%s)%s:(Admin:%s,DCC:%s)[r:%s w:%s d:%s]\n",infoFromSession.getFolder_id(),infoFromSession.getPrj_id(),login_id,isAdmin,isDcc,getFolderWithAuth.getAuth_r(),getFolderWithAuth.getAuth_w(),getFolderWithAuth.getAuth_d());
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : createNewFolder
	 * 2. 작성일: 2021-11-17
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_folder_info테이블에 새 폴더 정보를 넣음
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "createNewFolder.do")
	@ResponseBody
	public Object createNewFolder(HttpServletRequest request,FolderInfoVO folderinfovo) {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		FolderInfoVO getLastFolderId = treeService.getLastFolderId(folderinfovo);
		long newFolderId = getLastFolderId.getFolder_id()+1;
		FolderInfoVO getParentFolderInfo = treeService.getParentFolderInfo(folderinfovo);
		FolderInfoVO newfolderinfovo = new FolderInfoVO();
		newfolderinfovo.setPrj_id(folderinfovo.getPrj_id());
		newfolderinfovo.setFolder_id(newFolderId);
		newfolderinfovo.setParent_folder_id(folderinfovo.getParent_folder_id());
		if(getParentFolderInfo!=null) {
			newfolderinfovo.setFolder_type(getParentFolderInfo.getFolder_type());
			newfolderinfovo.setFolder_path(getParentFolderInfo.getFolder_path()+">"+newFolderId);
		}else {
			newfolderinfovo.setFolder_path(Long.toString(newFolderId));
		}
		newfolderinfovo.setFolder_nm(folderinfovo.getFolder_nm());
		//newfolderinfovo.setR_folder_path_nm(r_folder_path_nm);
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		newfolderinfovo.setReg_id(sessioninfo.getUser_id());
		newfolderinfovo.setReg_date(currentDateAndTime);
		try {
			if(treeService.insertFolderInfoVO(newfolderinfovo) < 1)
				throw new Exception("create folder failed");
		}
		catch(Exception e) {
			e.printStackTrace();
			return result;
		}
		
		
		// 폴더권한 생성
		try {
			FolderAuthVO folderAuthVo = new FolderAuthVO();
			folderAuthVo.setPrj_id(folderinfovo.getPrj_id());
			folderAuthVo.setFolder_id(newFolderId);
			folderAuthVo.setAuth_lvl1("F");
			folderAuthVo.setAuth_val(newFolderId+"");
			folderAuthVo.setReg_id(sessioninfo.getUser_id());
			folderAuthVo.setReg_date(currentDateAndTime);
			folderAuthVo.setParent_folder_inheri_yn("Y");
			
			System.out.println("[folder_id : " + newFolderId + " ] Auth insert");
			
			if(folderAuthService.insertFolderAuth(folderAuthVo) < 1) 
				throw new Exception("insert 실패");
			
		} catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		
		
		
		
		return result;
	}
	/**
	 * 
	 * 1. 메소드명 : getPrjGridList
	 * 2. 작성일: 2022. 1. 21.
	 * 3. 작성자: doil
	 * 4. 설명: 프로젝트 리스트 그리드
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getPrjGridList.do")
	@ResponseBody
	public ModelAndView getPrjGridList(HttpServletRequest request) {
		
		ModelAndView result = new ModelAndView();
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String login_id = sessioninfo.getUser_id();
		
		// admin 여부
		String isAdmin = treeService.isAdmin(login_id);
		if(isAdmin == null) isAdmin = "N";
		
		// admin이 아니면, 할당된 프로젝트 리스트만 공개
		List<PrjInfoVO> prjList;
		if(isAdmin.equals("Y"))
			prjList = treeService.getPrjGridAdminList();
		else
			//prjList = treeService.getPrjGridAdminList();
			prjList = treeService.getPrjGridList(login_id);
		
		result.addObject("prjList",prjList);
		
		return result;
	}
	
	@RequestMapping(value = "prjSearch.do")
	@ResponseBody
	public ModelAndView prjSearch(HttpServletRequest request , PrjInfoVO search) {
		
		ModelAndView result = new ModelAndView();
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String login_id = sessioninfo.getUser_id();
		
		// admin 여부
		search.setIsAdmin(treeService.isAdmin(login_id) != null ? treeService.isAdmin(login_id) : "N");
		String isAdmin = search.getIsAdmin();
		// login id set
		search.setUser_id(login_id);
		
		// admin이 아니면, 할당된 프로젝트 리스트에서만 검색
		List<PrjInfoVO> prjList;
		if(isAdmin.equals("Y")) prjList = treeService.getPrjGridAdminList();
		else prjList = treeService.getPrjGridList(login_id);
		
		String keyword = search.getKeyword().toUpperCase();
		
		prjList = prjList.stream()
				.filter(item -> ( item.getPrj_no().toUpperCase().contains(keyword) 
						|| item.getPrj_nm().toUpperCase().contains(keyword) 
						|| item.getPrj_full_nm().toUpperCase().contains(keyword) ) )
				.collect(Collectors.toList());
		
		result.addObject("prjList",prjList);
		
		return result;
	}
	

	
	
	
	/**
	 * 1. 메소드명 : renameFolder
	 * 2. 작성일: 2021-11-17
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_folder_info 테이블의 folder_nm 변경
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "renameFolder.do")
	@ResponseBody
	public Object renameFolder(FolderInfoVO folderinfovo) {
		List<Object> result = new ArrayList<Object>();
		treeService.updateFolderNm(folderinfovo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteFolder
	 * 2. 작성일: 2021-11-17
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_folder_info 테이블 내 folder 삭제
	 * 5. 수정일: 2022-02-20 시스템 폴더이거나 해당 폴더 또는 하위 폴더가 도서를 포함하고 있을 경우 삭제 불가 처리
	 */
	@RequestMapping(value = "deleteFolder.do")
	@ResponseBody
	public Object deleteFolder(FolderInfoVO folderinfovo,ProcessInfoVO processinfovo,PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		processinfovo.setProcess_folder_path_id(Long.toString(processinfovo.getFolder_id()));
		ProcessInfoVO getFolderIsSYSTEMClass = processinfoService.getFolderIsSYSTEMClass(processinfovo);
		int isCheckUseFolderId = processinfoService.isCheckUseFolderId(processinfovo);
		int getFolderIsContanisDoc = prjdocumentindexService.getFolderIsContanisDoc(prjdocumentindexvo);
		if(getFolderIsSYSTEMClass!=null) {
			result.add("시스템 폴더는 지울 수 없습니다.");
		}else if(isCheckUseFolderId>0){
			result.add("해당 폴더 또는 하위 폴더가 프로세스 또는 Discipline이 매칭되어있어 삭제할 수 없습니다.");
		}else if(getFolderIsContanisDoc>0){
			result.add("해당 폴더가 도서를 포함하고 있어 지울 수 없습니다.");
		}else {
			treeService.deleteFolder(folderinfovo);
			result.add("삭제 가능");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : moveFolder
	 * 2. 작성일: 2021-11-19
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_folder_info 테이블 내 folder의 parent_folder 변경 (폴더 이동)
	 * 5. 수정일: 2022-03-04
	 */
	@RequestMapping(value = "moveFolder.do")
	@ResponseBody
	public Object moveFolder(FolderInfoVO folderinfovo,ProcessInfoVO processinfovo) {
		List<Object> result = new ArrayList<Object>();
		processinfovo.setProcess_folder_path_id(Long.toString(processinfovo.getFolder_id()));
		ProcessInfoVO getFolderIsSYSTEMClass = processinfoService.getFolderIsSYSTEMClass(processinfovo);
		int isCheckUseFolderId = processinfoService.isCheckUseFolderId(processinfovo);
		if(getFolderIsSYSTEMClass!=null) {
			result.add("시스템 폴더는 이동할 수 없습니다.");
		}else if(isCheckUseFolderId>0){
			result.add("해당 폴더 또는 하위 폴더가 프로세스가 매칭되어있어 이동할 수 없습니다.");
		}else {
			String getFolderPath = treeService.getFolderPath(folderinfovo);
			FolderInfoVO getParentFolderInfo = treeService.getParentFolderInfo(folderinfovo);
			folderinfovo.setFolder_path(getParentFolderInfo.getFolder_path()+">"+folderinfovo.getFolder_id());
			treeService.updateFolderPath(folderinfovo);
			//하위 폴더도 folder_path 업데이트
			folderinfovo.setOrigin_folder_path(getFolderPath);
			treeService.updateDownFolderPath(folderinfovo);
			result.add("이동 가능");
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : getFolderDiscipline
	 * 2. 작성일: 2021-12-20
	 * 3. 작성자: 소진희
	 * 4. 설명: Discipline 정보 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getFolderDiscipline.do")
	@ResponseBody
	public Object getFolderDiscipline(FolderInfoVO folderinfovo) {
		List<Object> result = new ArrayList<Object>();
		List<FolderInfoVO> getFolderDiscipline = treeService.getFolderDiscipline(folderinfovo);
		result.add(getFolderDiscipline);
		return result;
	}
	
	
	
	
	@RequestMapping(value = "getTodoPageVar.do")
	@ResponseBody
	public ModelAndView getTodoPageVar(PrjDocumentIndexVO vo) {
		ModelAndView result = new ModelAndView();
		
		FolderInfoVO folderInfoVo = new FolderInfoVO();
		folderInfoVo.setPrj_id(vo.getPrj_id());
		folderInfoVo.setFolder_id(vo.getFolder_id());
		
		
		
		// discip
		FolderInfoVO discipObj = prjcodesettingsService.getDiscipObj(folderInfoVo);

		String folder_path = prjcodesettingsService.selectOne("folderinfoSqlMap.getFolderPath",folderInfoVo);
		// folder_path
		String folder_pathStr = prjcodesettingsService.selectOne("folderinfoSqlMap.getFolderPathStr",folderInfoVo);
		List<String> fp = Arrays.asList(folder_path.split(">"));
		// folder_path 역순으로 뒤집어서 doc_type 찾을때까지 for문

		Collections.reverse(fp);
		// doc_type
		String doc_type = null;
		for(String folder_id : fp) {
			folderInfoVo.setFolder_id(Long.parseLong(folder_id));
			List<String> getDoc_type = prjcodesettingsService.selectList("folderinfoSqlMap.getDocType",folderInfoVo); 
			if(getDoc_type.size()>0) {
				doc_type = getDoc_type.get(0);
				break;
			}
		}

		
		
		
		result.addObject("discip",discipObj);
		result.addObject("folder_path",folder_pathStr);
		result.addObject("doc_type",doc_type);
		
		return result;
	}
	
	
}
