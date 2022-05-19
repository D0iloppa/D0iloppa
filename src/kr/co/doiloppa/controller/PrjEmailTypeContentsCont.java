package kr.co.doiloppa.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.AES256Util;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.common.util.GetSHA256;
import kr.co.doiloppa.model.OrganizationUserVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjEmailTypeContentsVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.OrganizationUserService;
import kr.co.doiloppa.service.PrjEmailTypeContentsService;
import kr.co.doiloppa.service.PrjEmailTypeService;
import kr.co.doiloppa.service.SessionService;
import kr.co.doiloppa.service.UsersService;

@Controller
@RequestMapping("/*")
public class PrjEmailTypeContentsCont {

	protected PrjEmailTypeContentsService  prjemailtypecontentsService;
	protected PrjEmailTypeService  prjemailtypeService;
	
	@Autowired
	public void setPrjEmailTypeContentsService(PrjEmailTypeContentsService prjemailtypecontentsService) {
		this.prjemailtypecontentsService = prjemailtypecontentsService;
	}
	@Autowired
	public void setPrjEmailTypeService(PrjEmailTypeService prjemailtypeService) {
		this.prjemailtypeService = prjemailtypeService;
	}

	/**
	 * 1. 메소드명 : getPrjEmailTypeContents
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: 이메일 템플릿 불러오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjEmailTypeContents.do")
	@ResponseBody
	public Object getPrjEmailTypeContents(PrjEmailTypeContentsVO prjemailtypecontentsvo,PrjEmailTypeVO prjemailtypevo) {
		List<Object> result = new ArrayList<Object>();
		PrjEmailTypeContentsVO getPrjEmailTypeContents = prjemailtypecontentsService.getPrjEmailTypeContents(prjemailtypecontentsvo);
		result.add(getPrjEmailTypeContents);
		String getEmailAutoSendYN = prjemailtypeService.getEmailAutoSendYN(prjemailtypevo);
		result.add(getEmailAutoSendYN);
		return result;
	}

	/**
	 * 1. 메소드명 : insertPrjEmailTypeContents
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: p_prj_email_type_contents 테이블에 이메일 템플릿 저장
	 * 5. 수정일: 2021-01-11 update기능 추가
	 */
	@RequestMapping(value = "insertPrjEmailTypeContents.do")
	@ResponseBody
	public Object insertPrjEmailTypeContents(PrjEmailTypeContentsVO prjemailtypecontentsvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjemailtypecontentsvo.setReg_id(sessioninfo.getUser_id());
		prjemailtypecontentsvo.setReg_date(CommonConst.currentDateAndTime());
		prjemailtypecontentsvo.setMod_id(sessioninfo.getUser_id());
		prjemailtypecontentsvo.setMod_date(CommonConst.currentDateAndTime());
		PrjEmailTypeContentsVO getPrjEmailTypeContents = prjemailtypecontentsService.getPrjEmailTypeContents(prjemailtypecontentsvo);
		if(getPrjEmailTypeContents==null) {
			try {
				int insertPrjEmailTypeContents = prjemailtypecontentsService.insertPrjEmailTypeContents(prjemailtypecontentsvo);
				result.add(insertPrjEmailTypeContents);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("insertPrjEmailTypeContents: {}");
			}
		}else {
			try {
				int updatePrjEmailTypeContents = prjemailtypecontentsService.updatePrjEmailTypeContents(prjemailtypecontentsvo);
				result.add(updatePrjEmailTypeContents);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("updatePrjEmailTypeContents: {}");
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : isTXTFile
	 * 2. 작성일: 2022-03-14
	 * 3. 작성자: 소진희
	 * 4. 설명: txt 파일만 업로드가능하도록 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "isTXTFile.do")
	@ResponseBody
	public int isTXTFile(MultipartHttpServletRequest mtRequest) throws Exception{
		int chkFile = 100;
		
		MultipartFile mf = mtRequest.getFile("emailTemplateFromPC");
		File file = new File(mf.getOriginalFilename());
		
		String fileChk = file.getName();
		if(fileChk.length() > 0) {
			boolean fileUploadChk = FileExtFilter.txtFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}			
		}
		return chkFile;
	}
}
