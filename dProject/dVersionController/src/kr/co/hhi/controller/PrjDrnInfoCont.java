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

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDrnInfoVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjDrnInfoService;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjEmailTypeService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.PrjTrService;

@Controller
@RequestMapping("/*")
public class PrjDrnInfoCont {

	protected PrjDrnInfoService  prjdrninfoService;
	
	@Autowired
	public void setPrjDrnInfoService(PrjDrnInfoService prjdrninfoService) {
		this.prjdrninfoService = prjdrninfoService;
	}
}
