package kr.co.hhi.interceptor;

import java.io.PrintWriter;
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

public class DeleteAuthInterceptor implements HandlerInterceptor {
    
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
    	System.out.println("삭제 권한 확인중........");
    
    	
    	

    	HttpSession session = request.getSession();
    	
    	Long folder_id = (Long) session.getAttribute("folder_id");
    	String prj_id = (String) session.getAttribute("prj_id");
    	UsersVO usersVo = (UsersVO)session.getAttribute("login");
    	
    	
    	
    	
    	String isAdmin = treeService.isAdmin(usersVo.getUser_id());
		if(isAdmin == null) isAdmin = "N";
		
		// DCC 여부 확인
		PrjInfoVO prjInfoVo = new PrjInfoVO();
    	prjInfoVo.setPrj_id(prj_id);
    	prjInfoVo.setUser_id(usersVo.getUser_id());
    	
		String isDcc = treeService.isDcc(prjInfoVo);
		if(isDcc == null) isDcc = "N";
    	
    	
    	if(usersVo == null) {
        	System.out.println("권한 없음");
    		response.sendRedirect(request.getContextPath());
    		return false;
    	}
    	
    	
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
		if (org_Path.size()>0) {
			for (String org_path : org_Path) {
				checkFolderAuth.setOrg_id(org_path);
			}
		}

		List<String> grp_Id = treeService.getGrpPath(checkFolderAuth);
		if (grp_Id.size()>0) {
			for (String grp_id : grp_Id) {
				checkFolderAuth.setGrp_id(grp_id);
			}
		}
    	
    	
		checkFolderAuth = treeService.getFolderAuth(checkFolderAuth);
		checkFolderAuth = treeService.folderAuthCheck(checkFolderAuth, isAdmin, isDcc);
    	
		
    	String auth_d = checkFolderAuth.getAuth_w();
    	
    	if(auth_d.equals("Y")) return true;
    	else {
    		PrintWriter printwriter = response.getWriter();
    		printwriter.print("D_AUTH NO");

    		printwriter.flush();

    		printwriter.close();

    		return false;
    	}
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println("del 권한 확인완료");
    	
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("del 권한");
		
	}
    
}