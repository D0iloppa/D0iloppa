package kr.co.doiloppa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.PrjEmailVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.EmailSetService;

@Controller
@RequestMapping("/*")
public class EmailSetCont {
	
	protected EmailSetService emailSetService;
	
	@Autowired
	public void setTreeService(EmailSetService emailSetService) {
		this.emailSetService = emailSetService;
	}
	
	/**
	 * 1. 메소드명 : getEmailSetting
	 * 2. 작성일: 2022-02-14
	 * 3. 작성자: 박정우
	 * 4. 설명: Email Setting List 가져오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEmailSetting.do")
	@ResponseBody
	public Object getEmailSetting(PrjEmailVO prjEmailVo) {
		List<Object> result = new ArrayList<Object>();
		PrjEmailVO getEmailSetting = emailSetService.getEmailSetting();
//		if(getEmailSetting != null) {
//			String[] getEmailSettingList = getEmailSetting.getEmail_receiver_addr().split("\\s*,\\s*");
//			String[] getEmailIdSettingList = getEmailSetting.getEmail_user_id().split("\\s*,\\s*");
//			//System.out.println("getEmailSetting = "+getEmailSettingList.length);
//			result.add(getEmailSettingList);
//		}
		result.add(getEmailSetting);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertEmailSetting
	 * 2. 작성일: 2022-02-14
	 * 3. 작성자: 박정우
	 * 4. 설명: p_email_setting 테이블에 데이터 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "insertEmailSetting.do")
	@ResponseBody
	public int insertEmailSetting(HttpServletRequest request,PrjEmailVO prjEmailVo) {
		int chkData = 0;
		
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjEmailVo.setReg_id(sessioninfo.getUser_id());
		prjEmailVo.setReg_date(CommonConst.currentDateAndTime());
		
		PrjEmailVO getEmailSetting = emailSetService.getEmailSetting();
		
		// 저장할때 데이터가 없으면 insert 있으면 update 처리
		if(getEmailSetting!=null) {
			prjEmailVo.setMod_id(sessioninfo.getUser_id());
			prjEmailVo.setMod_date(CommonConst.currentDateAndTime());
			int updateChk = emailSetService.updateEmailSetting(prjEmailVo);
			//System.out.println("데이터 있음");
			chkData = 1;
		}else {
			int insertChk = emailSetService.insertEmailSetting(prjEmailVo);
			//System.out.println("데이터 없음");
			chkData = 2;
		}
		
		return chkData;
	}
}
