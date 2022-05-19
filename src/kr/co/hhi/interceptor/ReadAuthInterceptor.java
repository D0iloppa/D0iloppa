package kr.co.hhi.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.TreeService;

public class ReadAuthInterceptor implements HandlerInterceptor {
    
	
	protected PrjDocumentIndexService  prjdocumentindexService;
	protected TreeService treeService;
	
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	
    	System.out.println("읽기 권한 확인중........");
    
    	
    	
    	HttpSession session = request.getSession();
    	
    	if(session.getAttribute("login") == null) {
        	System.out.println("세션 없음");
        	PrintWriter printwriter = response.getWriter();
    		printwriter.print("noSession");
    		printwriter.flush();
    		printwriter.close();
    		return false;
    		
    		/*
    		response.sendRedirect(request.getContextPath()+"/login.do");
    		return false;
    		*/
    	}
    	
    	
    	Long folder_id = (Long) session.getAttribute("folder_id");
    	String prj_id = (String) session.getAttribute("prj_id");
    	UsersVO usersVo = (UsersVO)session.getAttribute("login");
    	

    	
    	if(folder_id == -1 || folder_id == 0) { // home tab은 r권한 따지지 않음
    		return true;
    	}
    	
    	
    	String isAdmin = treeService.isAdmin(usersVo.getUser_id());
		if(isAdmin == null) isAdmin = "N";
		
		// DCC 여부 확인
		PrjInfoVO prjInfoVo = new PrjInfoVO();
    	prjInfoVo.setPrj_id(prj_id);
    	prjInfoVo.setUser_id(usersVo.getUser_id());
    	
		String isDcc = treeService.isDcc(prjInfoVo);
		if(isDcc == null) isDcc = "N";
    	
    	

    	
    	/*
    	PrjDocumentIndexVO prjDocumentIndexVO = new PrjDocumentIndexVO();
    	prjDocumentIndexVO.setFolder_id(folder_id);
    	prjDocumentIndexVO.setPrj_id(prj_id);
    	prjDocumentIndexVO.setReg_id(usersVo.getUser_id());
    	*/
    	
    	
    	FolderInfoVO checkFolderAuth = new FolderInfoVO();
    	checkFolderAuth.setFolder_id(folder_id);
    	checkFolderAuth.setPrj_id(prj_id);
    	checkFolderAuth.setUser_id(usersVo.getUser_id());
    	
    	List<String> org_Path = treeService.getOrgPath(checkFolderAuth.getUser_id());
		if (org_Path.size() > 0) {
			String org_Paths = "";
			for (String org_path : org_Path) {
				org_Paths += org_path + "@";
				checkFolderAuth.setOrg_id(org_path);
			}
			org_Paths.substring(0,org_Paths.length()-1);
			checkFolderAuth.setOrg_id(org_Paths);
		}

		List<String> grp_Id = treeService.getGrpPath(checkFolderAuth);
		if (grp_Id.size() > 0) {
			for (String grp_id : grp_Id) {
				checkFolderAuth.setGrp_id(grp_id);
			}
		}
    	
    	// 해당 폴더에 걸려있는 권한 리스트를 가져옴
		checkFolderAuth = treeService.getFolderAuth(checkFolderAuth);
		// 가져온 권한 리스트를 기준으로 해당 폴더에 대한 권한값 최종 셋팅
		checkFolderAuth = treeService.folderAuthCheck(checkFolderAuth, isAdmin, isDcc);
    	
		
    	String auth_r = checkFolderAuth.getAuth_r();
    	
    	if(auth_r.equals("Y")) return true;
    	else {
    		PrintWriter printwriter = response.getWriter();
    		printwriter.print("<script>alert('There is no Read Permission');</script>");

    		printwriter.flush();

    		printwriter.close();

    		return false;
    	}
    	
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println("read 권한 확인완료");
    	
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("read 권한");
		
	}
    
}