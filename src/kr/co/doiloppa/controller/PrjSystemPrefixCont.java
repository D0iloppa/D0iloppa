package kr.co.doiloppa.controller;

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

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.AES256Util;
import kr.co.doiloppa.common.util.GetSHA256;
import kr.co.doiloppa.model.OrganizationUserVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjSystemPrefixVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.OrganizationUserService;
import kr.co.doiloppa.service.PrjEmailTypeService;
import kr.co.doiloppa.service.PrjSystemPrefixService;
import kr.co.doiloppa.service.SessionService;
import kr.co.doiloppa.service.UsersService;

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
