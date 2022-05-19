package kr.co.doiloppa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDrnInfoVO;
import kr.co.doiloppa.model.PrjEmailTypeContentsVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.PrjDrnInfoService;
import kr.co.doiloppa.service.PrjEmailTypeContentsService;
import kr.co.doiloppa.service.PrjEmailTypeService;
import kr.co.doiloppa.service.PrjStepService;
import kr.co.doiloppa.service.PrjTrService;

@Controller
@RequestMapping("/*")
public class PrjDrnInfoCont {

	protected PrjDrnInfoService  prjdrninfoService;
	
	@Autowired
	public void setPrjDrnInfoService(PrjDrnInfoService prjdrninfoService) {
		this.prjdrninfoService = prjdrninfoService;
	}
}
