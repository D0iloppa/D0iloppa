package kr.co.hhi.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.common.util.GetSHA256;
import kr.co.hhi.model.OrganizationUserVO;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjSystemPrefixVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.OrganizationUserService;
import kr.co.hhi.service.PrjEmailTypeService;
import kr.co.hhi.service.PrjSystemPrefixService;
import kr.co.hhi.service.SessionService;
import kr.co.hhi.service.UsersService;

@Controller
@RequestMapping("/*")
public class PrjSystemPrefixCont {

	protected PrjSystemPrefixService  prjsystemprefixService;
	
	@Autowired
	public void setPrjSystemPrefixService(PrjSystemPrefixService prjsystemprefixService) {
		this.prjsystemprefixService = prjsystemprefixService;
	}
	
	/**
	 * 1. 메소드명 : getPrjSystemPrefix
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_system_prefix 테이블에서 각 type에 해당하는 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjSystemPrefix.do")
	@ResponseBody
	public Object getPrjSystemPrefix(PrjSystemPrefixVO prjsystemprefixvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjSystemPrefixVO> getPrjSystemPrefix = prjsystemprefixService.getPrjSystemPrefix(prjsystemprefixvo);
		result.add(getPrjSystemPrefix);
		return result;
	}
}
