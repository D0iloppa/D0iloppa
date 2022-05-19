package kr.co.doiloppa.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.AES256Util;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.common.util.GetSHA256;
import kr.co.doiloppa.model.OrganizationUserVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.SecurityIpVO;
import kr.co.doiloppa.model.UserConnectVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.PrjInfoService;
import kr.co.doiloppa.service.PubBbsService;
import kr.co.doiloppa.service.SecurityService;
import kr.co.doiloppa.service.SessionService;
import kr.co.doiloppa.service.TreeService;
import kr.co.doiloppa.service.UserConnectService;

@Controller
@RequestMapping("/*")
public class SessionCont {

	protected SessionService sessionService;
	protected PubBbsService pubbbsService;
	protected PrjInfoService prjinfoService;
	protected TreeService treeService;
	protected SecurityService securityService;
	protected UserConnectService userconnectService;
	@Autowired
	public void setUserConnectService(UserConnectService userconnectService) {
		this.userconnectService = userconnectService;
	}
	
	@Autowired
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	@Autowired
	public void setPubBbsService(PubBbsService pubbbsService) {
		this.pubbbsService = pubbbsService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}

    @Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
    
    @Value("#{config['file.upload.signImg.path']}")
    private String SIGN_PATH;

	
	@RequestMapping(value = "login.do")
	public ModelAndView start() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/login/login");
		return mv;
	}
	
	@RequestMapping(value = "loginPost.do")
	public ModelAndView loginPOST(HttpServletRequest request, HttpServletResponse response, UsersVO usersVo) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		HttpSession session = request.getSession();

		ModelAndView mv = new ModelAndView();

		String Id = usersVo.getUser_id();
		Id = Id.toUpperCase();
		usersVo.setUser_id(Id);
		String Pwd = usersVo.getPasswd();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String aes_pwd = aes256.aesEncode(Pwd);
		String temp_pwd = Id + aes_pwd;
		usersVo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		
		UsersVO user_info = sessionService.loginPost(usersVo);
		if(user_info==null) {
			mv.addObject("viewName", "login");
			mv.addObject("userinfonoexist", "일치하는 정보가 없습니다");
			mv.setViewName("/login/login");
			return mv;
		}
		if(user_info.getOffi_tel()!=null) {
			String dec_tel = aes256.aesDecode(user_info.getOffi_tel());
			user_info.setOffi_tel(dec_tel);
		}
		if(user_info.getEmail_addr()!=null) {
			String dec_email = aes256.aesDecode(user_info.getEmail_addr());
			user_info.setEmail_addr(dec_email);
		}

		String id = request.getParameter("user_id");
		String saveId = request.getParameter("saveId");
		if(saveId!=null) {
            Cookie c = new Cookie("saveId",id);
            c.setMaxAge(60*60*24*7); //7일간 저장
            response.addCookie(c);
        }else {
            Cookie c = new Cookie("saveId",id);
            c.setMaxAge(0);
            response.addCookie(c);
        }
		
		session.setAttribute("login", user_info);
		session.setMaxInactiveInterval(CommonConst.SET_SESSION_TIME);	//초단위 (60*10=10분)
		UserConnectVO userConnectVo = new UserConnectVO();
		userConnectVo.setUser_id(Id);
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		userConnectVo.setMod_id(sessioninfo.getUser_id());
		userConnectVo.setMod_date(CommonConst.currentDateAndTime());
		userconnectService.resetConnectFailCnt(userConnectVo);
		mv.setViewName("redirect:/main.do");
		System.out.println("로그인 완료");
		return mv;
	}
	
	// 등록이 된 IP 인지 체크
		public static String getClientIP(HttpServletRequest request) {
		    String ip = request.getHeader("X-Forwarded-For");
		    System.out.println("> X-FORWARDED-FOR : " + ip);

		    if (ip == null) {
		        ip = request.getHeader("Proxy-Client-IP");
		        System.out.println("> Proxy-Client-IP : " + ip);
		    }
		    if (ip == null) {
		        ip = request.getHeader("WL-Proxy-Client-IP");
		        System.out.println(">  WL-Proxy-Client-IP : " + ip);
		    }
		    if (ip == null) {
		        ip = request.getHeader("HTTP_CLIENT_IP");
		        System.out.println("> HTTP_CLIENT_IP : " + ip);
		    }
		    if (ip == null) {
		        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		        System.out.println("> HTTP_X_FORWARDED_FOR : " + ip);
		    }
		    if (ip == null) {
		        ip = request.getRemoteAddr();
		        System.out.println("> getRemoteAddr : "+ip);
		    }
		    System.out.println("> Result : IP Address : "+ip);

		    return ip;
		}
		/**
		 * IP를 long타입으로 변환
	      * @param ipAddress
	      * @return
	      */
	     public static long ipToLong(String ipAddress) {
	         String[] ipAddressTemp = ipAddress.split("\\.");
	         long ipAddressLong = (Long.parseLong(ipAddressTemp[0]) << 24) + 
							(Long.parseLong(ipAddressTemp[1]) << 16) + 
							(Long.parseLong(ipAddressTemp[2]) << 8) + 
							(Long.parseLong(ipAddressTemp[3]));
	         return ipAddressLong;
	     }

	     @RequestMapping(value = "getChcekIpAddrTest.do")
	     @ResponseBody
	     
	     public String getChcekIpAddrTest(HttpServletRequest request,SecurityIpVO securityIpVo, UsersVO usersVo) {
	    	 
	    	 StringBuffer requestUrl = request.getRequestURL();
	    	 String url = requestUrl.toString();
	    	 String uri = request.getRequestURI();
	    	 url= url.replaceAll(uri, "");
	    	 System.out.println( url);
	    	 
	    	 String ss = request.getRequestURI();
	    	 
	    	 
	    	 String ipAddress=request.getRemoteAddr();
	    	 if(ipAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
	    	     InetAddress inetAddress = null;
				try {
					inetAddress = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
	    	     ipAddress=inetAddress.getHostAddress();
	    	 }
	    	 return ipAddress;
	    	 
	     }
	     
		@RequestMapping(value = "getChcekIpAddr.do")
		@ResponseBody
		public int getChcekIpAddr(HttpServletRequest request,SecurityIpVO securityIpVo, UsersVO usersVo)  {
			// can't pass : 0 , pass: 1
			
			// ip체크 사용 "N"일 경우
			SecurityIpVO getIpUseYN = securityService.getIpUseYN();
			if(getIpUseYN == null || getIpUseYN.getWhite_ip_use_yn().equals("N"))  return 1;
			
			// 로그인한 유저의 ip
			String ip = sessionService.getIpAddress(request);
			// 로그인한 유저의 아이디
			String user_id = usersVo.getUser_id();
			
			// ip체크 사용 "Y"일 경우
			
			// 어드민 여부 따짐
			UsersVO getAdminChkYN = sessionService.getAdminChkYN(usersVo);
			
			// 어드민인 경우 ip체크하지 않고 pass
			if(getAdminChkYN != null && getAdminChkYN.getAdmin_yn().equals("Y")) {
				return 1;
			}
			
			Long ipAddressLong = ipToLong(ip);
			System.out.println("IP > " + ip);

			// ip white리스트를 가져옴
			
			// 해당 유저의 조직정보 가져옴
			List<Long> getOrgList = securityService.getOrgList(user_id);
			
			for (Long org_id : getOrgList) { // 각 org_id별로 ip white_List를 가져옴
				List<SecurityIpVO> getSecurityOrgIpList = securityService.getSecurityOrgIpList(org_id);
				for (SecurityIpVO ipVo : getSecurityOrgIpList) {
					String[] ipRange = ipVo.getIp_addr().split("~");
					if(ipRange.length<2) { // ip가 하나만 들어가있는 경우의 예외처리 (시작과 끝을 동일하게 줌)
						if (ipAddressLong >= ipToLong(ipRange[0]) && ipAddressLong <= ipToLong(ipRange[0])) {
							return 1;
						}
					}
					// 일반적으로 ip가 2개 들어가있는 상황
					else if (ipAddressLong >= ipToLong(ipRange[0]) && ipAddressLong <= ipToLong(ipRange[1])) {
						return 1;
					}

				}
			}
			
			
			List<SecurityIpVO> getSecurityIpList = securityService.getSecurityUser_Id_IpList(user_id);
			
			for(SecurityIpVO ipVo : getSecurityIpList) {
				
				String[] ipRange = ipVo.getIp_addr().split("~");
				if (ipRange.length < 2) { // ip가 하나만 들어가있는 경우의 예외처리 (시작과 끝을 동일하게 줌)
					if (ipAddressLong >= ipToLong(ipRange[0]) && ipAddressLong <= ipToLong(ipRange[0])) {
						return 1;
					}
				}

				// 일반적으로 ip가 2개 들어가있는 상황
				else if (ipAddressLong >= ipToLong(ipRange[0]) && ipAddressLong <= ipToLong(ipRange[1])) {
					return 1;
				}
			}
			
			
			
			
			
			// 해당 유저의 id에 적용된 white_list 검색
			
			/*
			List<SecurityIpVO> getSecurityIpList = securityService.getSecurityIpList();
			
			for(SecurityIpVO ipVo : getSecurityIpList) {
				if (ipVo.getIp_range().equals("BETWEEN")) {
					String[] ipRange = ipVo.getIp_addr().split("~");
					if (ipAddressLong >= ipToLong(ipRange[0]) && ipAddressLong <= ipToLong(ipRange[1])) {
						//System.out.printf("[WHITE IP RANGE %s ~ %s] : %s\n" , ipToLong(ipRange[0]), ipToLong(ipRange[1]),ipAddressLong);
						return 1;
					}
						
				} else {
					if (ipAddressLong == ipToLong(ipVo.getIp_addr())) {
						//System.out.printf("[WHITE IP %s] : %s\n",ipAddressLong);
						return 1;
					}
				}
			}
			*/
			
			// 화이트리스트에 없으면 0 리턴
			
			return 0;
		}
	
	@RequestMapping(value = "getFirstLoginYn.do")
	@ResponseBody
	public Object getFirstLoginYn(UsersVO usersVo) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		String Id = usersVo.getUser_id();
		Id = Id.toUpperCase();
		usersVo.setUser_id(Id);
		String Pwd = usersVo.getPasswd();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String aes_pwd = aes256.aesEncode(Pwd);
		String temp_pwd = Id + aes_pwd;
		usersVo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		
		UsersVO user_info = sessionService.loginPost(usersVo);
		
		String result="";
		
		if(user_info != null) {
			result = user_info.getFirst_login_yn();
		} 
		
		return result;
	}
	
	@RequestMapping(value = "sessionCheck.do")
	@ResponseBody
	public Object sessionCheck(HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		result.add(sessioninfo);
		return result;
	}
	
	@RequestMapping(value = "getUserPw.do")
	@ResponseBody
	public Object getUserPw(UsersVO usersVo) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();

		String Id = usersVo.getUser_id();
		Id = Id.toUpperCase();
		usersVo.setUser_id(Id);
		String Pwd = usersVo.getPasswd();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String aes_pwd = aes256.aesEncode(Pwd);
		String temp_pwd = Id + aes_pwd;
		usersVo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		
		UsersVO user_info = sessionService.loginPost(usersVo);
		if(user_info==null) {
			result.add("불일치");
		}else {
			result.add("일치");
		}
		return result;
	}

	@RequestMapping(value = "invalidPwd.do")
	@ResponseBody
	public Object invalidPwd(UsersVO usersVo) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		if(!CommonConst.isPwdRule(usersVo.getPasswd())) {
			result.add("부적합");
		}else {
			result.add("적합");
		}
		return result;
	}
	
	@RequestMapping(value = "firstLoginPost.do")
	public ModelAndView firstLoginPost(HttpServletRequest request, HttpServletResponse response, UsersVO usersVo) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		ModelAndView mv = new ModelAndView();
		String Id = usersVo.getUser_id();
		Id = Id.toUpperCase();
		String newPwd = usersVo.getPasswd();
		usersVo.setUser_id(Id);
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String aes_pwd = aes256.aesEncode(newPwd);
		String temp_pwd = Id + aes_pwd;
		usersVo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		sessionService.firstLoginPost(usersVo);
		
		//업데이트한 정보로 세션에 다시저장
		UsersVO pusers = sessionService.loginPost(usersVo);
		if(pusers.getOffi_tel()!=null) {
			String dec_tel = aes256.aesDecode(pusers.getOffi_tel());
			pusers.setOffi_tel(dec_tel);
		}
		if(pusers.getEmail_addr()!=null) {
			String dec_email = aes256.aesDecode(pusers.getEmail_addr());
			pusers.setEmail_addr(dec_email);
		}
		HttpSession session = request.getSession();
		session.setAttribute("login", pusers);
		session.setMaxInactiveInterval(CommonConst.SET_SESSION_TIME);	//초단위 (60*10=10분)
		UserConnectVO userConnectVo = new UserConnectVO();
		userConnectVo.setUser_id(Id);
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		userConnectVo.setMod_id(sessioninfo.getUser_id());
		userConnectVo.setMod_date(CommonConst.currentDateAndTime());
		userconnectService.resetConnectFailCnt(userConnectVo);
		mv.addObject("viewName", "main");
		mv.setViewName("redirect:/main.do");
		return mv;
	}

	@RequestMapping(value = "checkUserId.do")
	@ResponseBody
	public Object checkUserId(UsersVO usersVo) throws IOException{
		List<Object> result = new ArrayList<Object>();
		usersVo.setUser_id(usersVo.getUser_id().toUpperCase());
		UsersVO pusers = sessionService.checkUserId(usersVo);
		if(pusers != null) {
			result.add("존재");
		}else {
			result.add("부존재");
		}
		return result;
	}

	@RequestMapping(value = "logout.do")
	public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException{
		Object obj = session.getAttribute("login");
		if(obj!=null) {
			UsersVO userinfo = (UsersVO) obj;
			
			session.removeAttribute("login");
			session.invalidate();
			System.out.println("로그아웃 완료");
		}
		response.sendRedirect(request.getContextPath()+"/login.do");
	}
	@RequestMapping(value = "updateLastConPrjId.do")
	@ResponseBody
	public Object updateLastConPrjId(HttpServletRequest request, UsersVO usersVo){
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String Id = sessioninfo.getUser_id();
		usersVo.setUser_id(Id);
		sessionService.updateLastConPrjId(usersVo);
		return result;
	}

	@RequestMapping(value = "getLastConPrjId.do")
	@ResponseBody
	public Object getLastConPrjId(HttpServletRequest request,PrjInfoVO prjinfovo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		String isAdmin = treeService.isAdmin(sessioninfo.getUser_id());
		if(isAdmin == null) isAdmin = "N";
		
		PrjInfoVO getLastConPrj =  prjinfoService.getLastConPrj(sessioninfo);
		if(getLastConPrj==null) {
			String user_type = prjinfoService.selectOne("prjinfoSqlMap.getUserType",sessioninfo.getUser_id());
			if(user_type == null) user_type = "-1";
			
			if(user_type.equals("1")) {
			//마지막 접속시 조회한 프로젝트 정보가 없을 경우 제일 처음 프로젝트를 가져옴
				prjinfovo.setPrj_id("0000000001");
				PrjInfoVO getFirstPrj = prjinfoService.getPrjInfo(prjinfovo);
				getLastConPrj = getFirstPrj;
			}else {
				List<PrjInfoVO> prjList = treeService.getPrjGridList(sessioninfo.getUser_id());
				// 최초로그인시, 접근 가능한 프로젝트의 첫번째 리스트에 접근
				if(prjList.size()>0) {
					String prj_id = prjList.get(0).getPrj_id();
					prjinfovo.setPrj_id(prj_id);
					PrjInfoVO getFirstPrj = prjinfoService.getPrjInfo(prjinfovo);
					getFirstPrj.setPrj_nm("SELECT THE PROJECT");
					getLastConPrj = getFirstPrj;
				}
				// 접근 가능한 리스트가 없는 경우 -1 리턴
				else {
					getLastConPrj = new PrjInfoVO();
					getLastConPrj.setPrj_id("-1");
					getLastConPrj.setLast_con_prj_id("-1");
					getLastConPrj.setPrj_nm("THERE IS NO PROJECT TO ACCESS");
				}
			}
		}else {
			// 기존 프로젝트의 읽기 권한이 없어진 경우
			if(isAdmin.equals("N")) {
				String lastPrj_id = getLastConPrj.getLast_con_prj_id();
				List<PrjInfoVO> prjList = treeService.getPrjGridList(sessioninfo.getUser_id());
				if(prjList.size()>0) {
					
					PrjInfoVO isInPrj = prjList.stream().filter( i -> i.getPrj_id().equals(lastPrj_id)).findFirst().get();
					// 마지막 접속한 프로젝트가 접근가능한 리스트에서 사라진 경우
					if(isInPrj == null) {
						String prj_id = prjList.get(0).getPrj_id();
						prjinfovo.setPrj_id(prj_id);
						PrjInfoVO getFirstPrj = prjinfoService.getPrjInfo(prjinfovo);
						getFirstPrj.setPrj_nm("SELECT THE PROJECT");
						getLastConPrj = getFirstPrj;
					}
				}
				// 접근 가능한 리스트가 없는 경우 -1 리턴
				else {
					getLastConPrj = new PrjInfoVO();
					getLastConPrj.setPrj_id("-1");
					getLastConPrj.setLast_con_prj_id("-1");
					getLastConPrj.setPrj_nm("THERE IS NO PROJECT TO ACCESS");
				}
			}
			
			// admin이 아니며, 마지막 접속했던 프로젝트에서 제외된 경우
			/*
			if(isAdmin.equals("N")) {
				if(prjinfoService.isProjectMember(getLastConPrj)<1) {
					prjinfovo.setPrj_id("0000000001");
					PrjInfoVO getFirstPrj = prjinfoService.getPrjInfo(prjinfovo);
					getLastConPrj = getFirstPrj;
				}
			}
			*/			
		}
		
		result.add(getLastConPrj);
		return result;
	}

	@RequestMapping(value = "updateUsersVO.do")
	@ResponseBody
	public Object updateUsersVO(MultipartHttpServletRequest mtRequest,HttpServletRequest request, UsersVO usersVo) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String aes_tel = aes256.aesEncode(usersVo.getOffi_tel());
		usersVo.setOffi_tel(aes_tel);
		String aes_email = aes256.aesEncode(usersVo.getEmail_addr());
		usersVo.setEmail_addr(aes_email);
		sessionService.updateUsersVO(usersVo);
		//사용자정보 수정후 수정된 정보 세션에 다시 저장
		UsersVO getUsersVO = sessionService.getUsersVO(usersVo);
		String dec_tel = aes256.aesDecode(getUsersVO.getOffi_tel());
		String dec_email = aes256.aesDecode(getUsersVO.getEmail_addr());
		getUsersVO.setOffi_tel(dec_tel);
		getUsersVO.setEmail_addr(dec_email);
		HttpSession session = request.getSession();
		session.setAttribute("login", getUsersVO);

		if(!usersVo.getFile_value().equals("")) {	//file Attach 할 경우
			//파일 업로드
			MultipartFile file = mtRequest.getFile("updateUserInfo_File");
			String uploadPath = UPLOAD_PATH + SIGN_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + usersVo.getSfile_nm());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateUserVOSaveChkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "updateUserVOSaveChkFile.do")
	@ResponseBody
	public int updateUserVOSaveChkFile(MultipartHttpServletRequest mtRequest,HttpServletRequest request, UsersVO usersVo) throws Exception{
		int chkFile = 100;
		MultipartFile mf = mtRequest.getFile("updateUserInfo_File");
		File file = new File(mf.getOriginalFilename());
		// true면 등록 불가 false면 등록 가능한 확장자
//		boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
		// true면 등록 가능 false면 등록 불가능한 확장자
		String fileChk = file.getName();
		if(fileChk.length() > 0) {
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}			
		}
		return chkFile;
	}


	/**
	 * 1. 메소드명 : SignExists
	 * 2. 작성일: 2022-02-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 개인 전자 서명 파일 있는지 체크
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "SignExists.do")
	@ResponseBody
	public Object CoverTemplateExists(UsersVO usersVo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		UsersVO getUsersVO = sessionService.getUsersVO(usersVo);
		File f = new File(UPLOAD_PATH+SIGN_PATH+getUsersVO.getSfile_nm());
		if(f.exists()) {
		     result.add("서명 존재");
		} else {
		     result.add("서명 없음");           
		}
		result.add(getUsersVO);
		return result;
	}
	/**
	 * 1. 메소드명 : downloadSignFile
	 * 2. 작성일: 2022-02-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 서명 파일 다운받기
	 * 5. 수정일: 
	 */
    @RequestMapping("/downloadSignFile.do")
    public void downloadSignFile(@RequestParam String sfile_nm,@RequestParam String rfile_nm,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String downloadFilesString = sfile_nm;
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			files[i] = downloadFileEach[i];
		}
    	String tempPath = "";
    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH+SIGN_PATH;

                 for ( int k=0; k<files.length; k++){
                 //파일다운로드 START
	                 response.setContentType("application/zip");
	                 rfile_nm = CommonConst.downloadNameEncoding(request, rfile_nm);
	                 response.addHeader("Content-Disposition", "attachment;filename=" + rfile_nm);
	                 
	                 FileInputStream fis = new FileInputStream(tempPath + files[k]);
	                 BufferedInputStream bis = new BufferedInputStream(fis);
	                 ServletOutputStream so = response.getOutputStream();
	                 BufferedOutputStream bos = new BufferedOutputStream(so);
	                 byte[] buffer = new byte[1024];
	                 
	                 int n = 0;
	                 while((n = bis.read(buffer)) > 0){
	                    bos.write(buffer, 0, n);
	                    bos.flush();
	                 }
	                 
	                 if(bos != null) bos.close();
	                 if(bis != null) bis.close();
	                 if(so != null) so.close();
	                 if(fis != null) fis.close();
	                 //파일다운로드 END
                 }
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }
    	}
    }
}
