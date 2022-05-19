
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
import kr.co.hhi.model.PasswordPolicyVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PassWordPolicyService;

@Controller
@RequestMapping("/**")
public class PassWordPolicyCont {
	
	protected PassWordPolicyService passwordpolicyService;
	
	@Autowired
	public void setPassWordPolicyService(PassWordPolicyService passwordpolicyService) {
		this.passwordpolicyService = passwordpolicyService;
	}
	
	/**
	 * 1. 메소드명 : getDictionary
	 * 2. 작성일: 2021-12-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 사전 정보를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getpasswdPolicy.do")
	@ResponseBody
	public Object getpasswdPolicy(HttpServletRequest request,PasswordPolicyVO passwordpolicyVo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		passwordpolicyVo.setReg_id(sessioninfo.getUser_id());
		
		PasswordPolicyVO getpasswdPolicy = passwordpolicyService.getpasswdPolicy(passwordpolicyVo);
		result.add(getpasswdPolicy);
		return result;
	}
	
	/**
	 * 1. 메소드명 : updatepasswdPolicy
	 * 2. 작성일: 2021-12-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 정보를 업데이트함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "updatepasswdPolicy.do")
	@ResponseBody
	public int updatepasswdPolicy(HttpServletRequest request,PasswordPolicyVO passwordpolicyVo) {
		int chkInUp = 0;
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		PasswordPolicyVO getpasswdPolicy = passwordpolicyService.getpasswdPolicy(passwordpolicyVo);
		
		if(getpasswdPolicy == null) {
			passwordpolicyVo.setReg_id(sessioninfo.getUser_id());
			passwordpolicyVo.setReg_date(CommonConst.currentDateAndTime());
			passwordpolicyService.insertpasswdPolicy(passwordpolicyVo);
			chkInUp = 1;
		}else {
			passwordpolicyVo.setMod_id(sessioninfo.getUser_id());
			passwordpolicyVo.setMod_date(CommonConst.currentDateAndTime());
			passwordpolicyService.updatepasswdPolicy(passwordpolicyVo);
			chkInUp = 2;
		}
		return chkInUp;
	}
}


