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
import kr.co.hhi.model.SecurityIpVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.SecurityService;


@Controller
@RequestMapping("/*")
public class SecurityCont {
	protected SecurityService securityService;
	
	@Autowired
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * 1. 메소드명 : getIpUseYN
	 * 2. 작성일: 2022-03-08
	 * 3. 작성자: 소진희
	 * 4. 설명: IP 보안 사용여부 가져오기
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getIpUseYN.do")
	@ResponseBody
	public String getIpUseYN(HttpServletRequest request, SecurityIpVO securityIpVo) {
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		securityIpVo.setReg_id(sessioninfo.getUser_id());
		securityIpVo.setReg_date(CommonConst.currentDateAndTime());
		securityIpVo.setMod_id(sessioninfo.getUser_id());
		securityIpVo.setMod_date(CommonConst.currentDateAndTime());
		SecurityIpVO getIpUseYN = securityService.getIpUseYN();
		String result = "";
		if(getIpUseYN!=null) {
			result = getIpUseYN.getWhite_ip_use_yn();
		}else {
			result = "N";
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : ipUseYNSave
	 * 2. 작성일: 2022-03-08
	 * 3. 작성자: 소진희
	 * 4. 설명: IP 보안 사용여부 저장
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "ipUseYNSave.do")
	@ResponseBody
	public int ipUseYNSave(HttpServletRequest request, SecurityIpVO securityIpVo) {
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		securityIpVo.setReg_id(sessioninfo.getUser_id());
		securityIpVo.setReg_date(CommonConst.currentDateAndTime());
		securityIpVo.setMod_id(sessioninfo.getUser_id());
		securityIpVo.setMod_date(CommonConst.currentDateAndTime());
		SecurityIpVO getIpUseYN = securityService.getIpUseYN();
		int result = 0;
		try {
			if(getIpUseYN==null) {
				result = securityService.insertIpUseYN(securityIpVo);
			}else {
				result = securityService.updateIpUseYN(securityIpVo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : getSecurityIpList
	 * 2. 작성일: 2022-02-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 ip 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getSecurityIpList.do")
	@ResponseBody
	public Object getSecurityIpList(SecurityIpVO securityIpVo) {
		List<Object> result = new ArrayList<Object>();
		List<SecurityIpVO> getSecurityIpList = securityService.getSecurityIpList();
		
		getSecurityIpList = securityService.valueMapping(getSecurityIpList);
		
		
		
		result.add(getSecurityIpList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertIp
	 * 2. 작성일: 2022-02-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록된 ip 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "insertIp.do")
	@ResponseBody
	public int insertIp(HttpServletRequest request, SecurityIpVO securityIpVo) {
		int saveChk = 0;
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		securityIpVo.setReg_id(sessioninfo.getUser_id());
		securityIpVo.setReg_date(CommonConst.currentDateAndTime());
		saveChk = securityService.insertIp(securityIpVo);
		return saveChk;
	}
	
	/**
	 * 1. 메소드명 : upSecurityIp
	 * 2. 작성일: 2022-02-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 선택한 ip update
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "upSecurityIp.do")
	@ResponseBody
	public int upSecurityIp(HttpServletRequest request, SecurityIpVO securityIpVo) {
		int saveChk = 0;
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		securityIpVo.setMod_id(sessioninfo.getUser_id());
		securityIpVo.setMod_date(CommonConst.currentDateAndTime());
		saveChk = securityService.upSecurityIp(securityIpVo);
		return saveChk;
	}
	
	/**
	 * 1. 메소드명 : deleteSecurityIp
	 * 2. 작성일: 2022-02-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 선택한 ip 삭제
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "deleteSecurityIp.do")
	@ResponseBody
	public int deleteSecurityIp(SecurityIpVO securityIpVo) {
		int saveChk = 0;
		saveChk = securityService.deleteSecurityIp(securityIpVo);
		return saveChk;
	}
}
