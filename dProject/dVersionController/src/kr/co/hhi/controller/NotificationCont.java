package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.text.SimpleDateFormat;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.excel.ExcelFileDownload;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.HomeService;
import kr.co.hhi.service.NotificationService;
import kr.co.hhi.service.PrjCodeSettingsService;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.PrjDrnInfoHistService;
import kr.co.hhi.service.PrjDrnInfoService;
import kr.co.hhi.service.PrjEmailService;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjEmailTypeService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.PrjTrService;
import kr.co.hhi.service.ProcessInfoService;
import kr.co.hhi.service.TreeService;

@Controller
@RequestMapping("/*")
public class NotificationCont {

	protected PrjInfoService prjinfoService;
	protected TreeService treeService;
	protected ProcessInfoService processinfoService;
	
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	@Autowired
	public void setProcessInfoService(ProcessInfoService processinfoService) {
		this.processinfoService = processinfoService;
	}
	
	/**
	 * 1. 메소드명 : Notification
	 * 2. 작성일: 2022-03-02
	 * 3. 작성자: 소진희
	 * 4. 설명: Notification 메일에서 Doc No 눌렀을 때 해당 도서와 관련된 페이지를 열기위한 컨트롤러
	 * 5. 수정일:
	 */
	@RequestMapping(value = "Notification.do")
	public ModelAndView Notification(HttpServletRequest request,PrjInfoVO prjinfovo,@RequestParam(value = "prj_id", required=false) String prj_id,@RequestParam(value = "page", required=false) String page,@RequestParam(value = "folderId", required=false) String folderId) {
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(prj_id) || StringUtils.isEmpty(page) || StringUtils.isEmpty(folderId)) {
			mv.setViewName("redirect:/main.do");
		}else {
			HttpSession session = request.getSession();
			UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
			ProcessInfoVO processinfovo = new ProcessInfoVO();
			prjinfovo.setPrj_id(prj_id);
			processinfovo.setPrj_id(prj_id);
			if(page.equals("IncomingTR")) {
				processinfovo.setProcess_type("TR INCOMING");
			}else if(page.equals("IncomingVTR")) {
				processinfovo.setProcess_type("VTR INCOMING");
			}else if(page.equals("OutgoingTR")) {
				processinfovo.setProcess_type("TR OUTGOING");
			}else if(page.equals("OutgoingVTR")) {
				processinfovo.setProcess_type("VTR OUTGOING");
			}
			ProcessInfoVO getSYSTEMProcessFolderId = processinfoService.getSYSTEMProcessFolderId(processinfovo);
			if(folderId.equals("0")) {
				folderId = getSYSTEMProcessFolderId.getProcess_folder_path_id();
			}
			FolderInfoVO folderinfovo = new FolderInfoVO();
			folderinfovo.setPrj_id(prj_id);
			folderinfovo.setFolder_id(Long.parseLong(folderId));
			FolderInfoVO getFolderInfo = treeService.getFolderInfo(folderinfovo);
			String folder_path = getFolderInfo.getFolder_path();
			PrjInfoVO getSelectPrj = prjinfoService.getPrjInfo(prjinfovo);
			List<PrjInfoVO> getPrjInfoList = prjinfoService.getPrjInfoListTop(sessioninfo);
			mv.addObject("getLastConPrj", getSelectPrj);
			mv.addObject("getPrjInfoList", getPrjInfoList);
			mv.addObject("folder_path", folder_path);
			mv.addObject("prj_id", prj_id);
			mv.addObject("page", page);
			mv.addObject("folderId", folderId);
			mv.setViewName("/main");
		}
		return mv;
	}
}
