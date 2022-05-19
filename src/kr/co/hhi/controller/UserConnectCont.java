package kr.co.hhi.controller;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.PasswordPolicyVO;
import kr.co.hhi.model.SecurityLogVO;
import kr.co.hhi.model.UserConnectVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PassWordPolicyService;
import kr.co.hhi.service.SecurityLogService;
import kr.co.hhi.service.SessionService;
import kr.co.hhi.service.UserConnectService;
import kr.co.hhi.service.UsersService;

@Controller
@RequestMapping("/*")
public class UserConnectCont {

	protected SecurityLogService securityLogService;
	protected UserConnectService  userconnectService;
	protected PassWordPolicyService passwordpolicyService;
	protected SessionService sessionService;
	protected UsersService  usersService;
	
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	@Autowired
	public void setSecurityLogService(SecurityLogService securityLogService) {
		this.securityLogService = securityLogService;
	}
	
	@Autowired
	public void setUserConnectService(UserConnectService userconnectService) {
		this.userconnectService = userconnectService;
	}
	
	@Autowired
	public void setPassWordPolicyService(PassWordPolicyService passwordpolicyService) {
		this.passwordpolicyService = passwordpolicyService;
	}

	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * 1. 메소드명 : loginInitialization
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: userRegister 페이지에서 진행되는 로그인 초기화
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "loginInitialization.do")
	@ResponseBody
	public void loginInitialization(UsersVO usersVo) {
		userconnectService.loginInitialization(usersVo);
	}
	
	/**
	 * 1. 메소드명 : saveUserConnect
	 * 2. 작성일: 2022-02-27
	 * 3. 작성자: 박정우
	 * 4. 설명: 최초 login시 id 정보 저장
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "saveUserConnect.do")
	@ResponseBody
	public int saveUserConnect(HttpServletRequest request, UserConnectVO userConnectVo) {
		int chkInUp = 0;
		
		
		String ip = userconnectService.getIpAddress(request);
		/*
		try {
			ip = String.valueOf(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		UserConnectVO getUserConnect = userconnectService.getUserConnect(userConnectVo);
		
		if(getUserConnect == null) {
			userConnectVo.setReg_id(userConnectVo.getUser_id());
			userConnectVo.setReg_date(CommonConst.currentDateAndTime());
			userConnectVo.setFirst_con_date(CommonConst.currentDateAndTime());
			userConnectVo.setLast_con_date(CommonConst.currentDateAndTime());
			userConnectVo.setRemote_ip(ip);
			userconnectService.insertUserConnect(userConnectVo);
			chkInUp = 1;
		}else {
			userConnectVo.setMod_id(userConnectVo.getUser_id());
			userConnectVo.setMod_date(CommonConst.currentDateAndTime());
			userConnectVo.setLast_con_date(CommonConst.currentDateAndTime());
			userConnectVo.setRemote_ip(ip);
			userconnectService.updateUserConnect(userConnectVo);
			chkInUp = 2;
		}
		return chkInUp;
	}
	
	/**
	 * 1. 메소드명 : upConnFailCnt
	 * 2. 작성일: 2022-02-27
	 * 3. 작성자: 박정우
	 * 4. 설명: 최초 login시 id 정보 저장
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "upConnFailCnt.do")
	@ResponseBody
	public int upConnFailCnt(HttpServletRequest request, UserConnectVO userConnectVo) {
		int chkFailcnt = 0;
		List<UsersVO> getAdminList = usersService.getAdminList();
		List<String> AdminList = new ArrayList<>();
		for(int i=0;i<getAdminList.size();i++) {
			AdminList.add(getAdminList.get(i).getUser_id());
		}
		if(AdminList.contains(userConnectVo.getUser_id())) {//ADMIN이면 로그인오류횟수체크안함
			return chkFailcnt;
		}
		
		UserConnectVO getUserConnect = userconnectService.getUserConnect(userConnectVo);
		
		if(getUserConnect != null) {
			userConnectVo.setConnect_fail_cnt(getUserConnect.getConnect_fail_cnt()+1);
			userConnectVo.setMod_id(userConnectVo.getUser_id());
			userConnectVo.setMod_date(CommonConst.currentDateAndTime());
			userconnectService.upConnFailCnt(userConnectVo);
		}
		
		return chkFailcnt;
	}
	
	/**
	 * 1. 메소드명 : resetDatePwd
	 * 2. 작성일: 2022-02-27
	 * 3. 작성자: 박정우
	 * 4. 설명: 비밀번호 변경시 first_con_date, connect_fail_cnt 초기화
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "resetDateFail.do")
	@ResponseBody
	public int resetDatePwd(HttpServletRequest request, UserConnectVO userConnectVo) {
		int chkFailcnt = 0;
		
		UserConnectVO getUserConnect = userconnectService.getUserConnect(userConnectVo);
		
		if(getUserConnect != null) {
			userConnectVo.setFirst_con_date(CommonConst.currentDateAndTime());
			userConnectVo.setMod_id(userConnectVo.getUser_id());
			userConnectVo.setMod_date(CommonConst.currentDateAndTime());
			userconnectService.resetDateFail(userConnectVo);
		}
		
		return chkFailcnt;
	}
	
	/**
	 * 1. 메소드명 : checkPassPolicy
	 * 2. 작성일: 2022-02-28
	 * 3. 작성자: 박정우
	 * 4. 설명: login시 passwordPolicy 체크
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "checkPassPolicy.do")
	@ResponseBody
	public int checkPassPolicy(HttpServletRequest request, UserConnectVO userConnectVo,PasswordPolicyVO passwordpolicyVo) throws ParseException {
		int chkFailcnt = 0;
		// 0 = 걸린 제한 없음, 1 = 비밀번호 유효기간 만료, 2 = 비밀번호 틀린 횟 수 초과
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		PasswordPolicyVO getpasswdPolicy = passwordpolicyService.getpasswdPolicy(passwordpolicyVo);
		UserConnectVO getUserConnect = userconnectService.getUserConnect(userConnectVo);
		
		if(getpasswdPolicy.getPasswd_expire_day_use_yn().equals("Y") && getUserConnect != null) {
			/* 최초 로그인한 날짜*/
			String pwFirstDate = getUserConnect.getFirst_con_date().substring(0,8);
			Date pwFirstDateFormat = sdf.parse(pwFirstDate);
			
			Calendar passwordFirstDate = Calendar.getInstance();
			passwordFirstDate.setTime(pwFirstDateFormat);
			/* 현재 날짜 */
			Date date = new Date();
			String dateFormat = sdf.format(date);
			Date todayDate = changeStringToDate(dateFormat);
			
			Calendar toDay = Calendar.getInstance();
			toDay.setTime(todayDate);
			
			long diffSec = (toDay.getTimeInMillis() - passwordFirstDate.getTimeInMillis()) / 1000;
			long diffDays = diffSec / (24*60*60);
			
			//System.out.println("최초 로그인 한 날 부터 "+diffDays+"일 지남");
			
			if(getpasswdPolicy.getPasswd_expire_day() < diffDays) {
				chkFailcnt = 1;
				return chkFailcnt;
			}
			
		}
		if(getpasswdPolicy.getPasswd_fail_acc_stop_use_yn().equals("Y") && getUserConnect != null) {
			// 비밀번호 초가 횟수 설정이 Y일때
			if(getpasswdPolicy.getPasswd_fail_acc_stop_cnt() <= getUserConnect.getConnect_fail_cnt()) {
				//비밀번호 틀린 횟수가 설정한 수 보다 넘었을떄
				chkFailcnt = 2;
				return chkFailcnt;
			}
		}
		
		return chkFailcnt;
	}
	
	public Date changeStringToDate(String dateInput) {
		Date resultDate = null;
		String dateFormat = "yyyyMMdd";
		DateFormat df = new SimpleDateFormat(dateFormat);
		try {
			resultDate = df.parse(dateInput);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resultDate;
	}
	
	/**
	 * 1. 메소드명 : insertLoginSystemLog
	 * 2. 작성일: 2022-03-10
	 * 3. 작성자: 박정우
	 * 4. 설명: 유저 로그인 활동 정보 저장
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertLoginSystemLog.do")
	@ResponseBody
	public void insertLoginSystemLog(HttpServletRequest request, SecurityLogVO securityLogVo, UsersVO usersVo) {
		String ip = null;
		ip = sessionService.getIpAddress(request);
		
		UsersVO getLoginUserVo = securityLogService.getLoginUser(usersVo);
		
		String getPrj_Id = getLoginUserVo.getLast_con_prj_id();
		String getReg_Nm = getLoginUserVo.getUser_kor_nm();
		
		if(getPrj_Id==null || getPrj_Id.equals("")) {
			getPrj_Id = "0000000001";
		}
		
		securityLogVo.setPrj_id(getPrj_Id);
		securityLogVo.setReg_date(CommonConst.currentDateAndTime());
		securityLogVo.setReg_id(usersVo.getUser_id());
		securityLogVo.setReg_nm(getReg_Nm);
		securityLogVo.setRemote_ip(ip);
		
		securityLogService.insertSystemLog(securityLogVo);
	}
}
