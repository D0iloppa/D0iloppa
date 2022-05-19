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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.common.util.GetSHA256;
import kr.co.hhi.model.CodeInfoVO;
import kr.co.hhi.model.OrganizationUserVO;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.OrganizationUserService;
import kr.co.hhi.service.PrjEmailService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.SessionService;
import kr.co.hhi.service.UsersService;
import kr.co.hhi.service.UsersServiceOracle;

@Controller
@RequestMapping("/*")
public class UsersCont {

	protected UsersService  usersService;
	protected SessionService  sessionService;
	protected OrganizationUserService organizationuserService;
	protected PrjEmailService  prjemailService;
	protected PrjInfoService prjinfoService;
	protected UsersServiceOracle usersServiceOracle;
	
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	@Autowired
	public void setUsersServiceOracle(UsersServiceOracle usersServiceOracle) {
		this.usersServiceOracle = usersServiceOracle;
	}
	@Autowired
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}
	@Autowired
	public void setOrganizationUserService(OrganizationUserService organizationuserService) {
		this.organizationuserService = organizationuserService;
	}
	@Autowired
	public void setPrjEmailService(PrjEmailService prjemailService) {
		this.prjemailService = prjemailService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}

    @Value("#{config['sys.email.main.account']}")
    private String MAIL_ACCOUNT;

    @Value("#{config['sys.email.main.sender.id']}")
    private String MAIL_ID;
	
	/**
	 * 1. 메소드명 : getAdminAndDCCList
	 * 2. 작성일: 2022-02-16
	 * 3. 작성자: 소진희
	 * 4. 설명: Admin과 DCC List를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getAdminAndDCCList.do")
	@ResponseBody
	public Object getAdminList(PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getAdminList =  usersService.getAdminList();
		List<PrjMemberVO> getPrjDCC = prjinfoService.getPrjDCC(prjmembervo);
		String AdminAndDCC = "";
		for(int i=0;i<getAdminList.size();i++) {
			AdminAndDCC += getAdminList.get(i).getUser_id() + ",";
		}
		for(int i=0;i<getPrjDCC.size();i++) {
			AdminAndDCC += getPrjDCC.get(i).getUser_id() + ",";
		}
		AdminAndDCC = AdminAndDCC.substring(0, AdminAndDCC.length()-1);
		result.add(AdminAndDCC);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getADMINs
	 * 2. 작성일: 2022-02-20
	 * 3. 작성자: 소진희
	 * 4. 설명: 유저등록 페이지에서 조직을 control 가능한 ADMIN 목록과 
	 * 			유저등록 페이지에서 고객사와 협력업체만 control 가능한 모든 프로젝트의 DCC 목록을 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getADMINOrDCC.do")
	@ResponseBody
	public Object getADMINs() {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getAdminList =  usersService.getAdminList();
		String ADMINs = "";
		for(int i=0;i<getAdminList.size();i++) {
			ADMINs += getAdminList.get(i).getUser_id() + ",";
		}
		ADMINs = ADMINs.substring(0, ADMINs.length()-1);
		result.add(ADMINs);

		List<PrjMemberVO> getDCCs = prjinfoService.getDCCs();
		String DCCs = "";
		for(int i=0;i<getDCCs.size();i++) {
			DCCs += getDCCs.get(i).getUser_id() + ",";
		}
		DCCs = DCCs.substring(0, DCCs.length()-1);
		result.add(DCCs);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getUserJobTypeList
	 * 2. 작성일: 2022-01-27
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 JOB타입 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getUserJobTypeList.do")
	@ResponseBody
	public Object getUserJobTypeList() {
		List<Object> result = new ArrayList<Object>();
		List<CodeInfoVO> getUserJobTypeList =  usersService.getUserJobTypeList();
		result.add(getUserJobTypeList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertCodeInfo
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 JOB타입 리스트를 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertJobCodeInfo.do")
	@ResponseBody
	public void insertJobCodeInfo(CodeInfoVO codeInfoVo) {
		
		CodeInfoVO getsetOrderVo = usersService.getJobMaxsetOrder();
		CodeInfoVO getCodeVo = usersService.getJobMaxCode();
		
		String code = "";
		long set_order = 0;
		
		if(getCodeVo != null) {
			code = getCodeVo.getCode();
			set_order = getsetOrderVo.getSet_order();
		}
		
		// 처음 코드 저장할떄
		if((code == null || code == "")) {
			codeInfoVo.setCode("10");
			codeInfoVo.setSet_order(1);
			
			usersService.insertJobCodeInfo(codeInfoVo);
		}else {
			codeInfoVo.setCode(String.valueOf(Integer.parseInt(code)+1));
			codeInfoVo.setSet_order(set_order+1);
			
			usersService.insertJobCodeInfo(codeInfoVo);
		}
	}
	
	/**
	 * 1. 메소드명 : insertOffiCodeInfo
	 * 2. 작성일: 2022-03-08
	 * 3. 작성자: 소진희
	 * 4. 설명: 타입정의에 OFFI(직책)타입 리스트를 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertOffiCodeInfo.do")
	@ResponseBody
	public void insertOffiCodeInfo(CodeInfoVO codeInfoVo) {
		usersService.insertOffiCodeInfo(codeInfoVo);
	}
	
	/**
	 * 1. 메소드명 : deleteOffiCodeInfo
	 * 2. 작성일: 2022-03-08
	 * 3. 작성자: 소진희
	 * 4. 설명: p_code_info 테이블에 직책코드 삭제 전 사용중인지 체크하여 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteOffiCodeInfo.do")
	@ResponseBody
	public Object deleteOffiCodeInfo(CodeInfoVO codeInfoVo, UsersVO usersVo) {
		List<Object> result = new ArrayList<Object>();
		
		String getCode = codeInfoVo.getCode();
		String[] getCodeArray = getCode.split(",");
		String chkCode = "";
		String delCode = "";
		String yCodeNm = "";
		
		for(int i=0;i<getCodeArray.length;i++) {
			usersVo.setOffi_res_cd(getCodeArray[i]);
			int count = usersService.getUseOffiCodeYN(usersVo);
			if(count>0) {
				// 사용중인 코드
				chkCode += getCodeArray[i] + ",";
				codeInfoVo.setCode(getCodeArray[i]);
				CodeInfoVO codeNmVo = prjinfoService.getUseYcodeNm(codeInfoVo);
				yCodeNm += codeNmVo.getCode_nm() + ",";
			}else{
				//삭제
				delCode += getCodeArray[i] + ",";
				codeInfoVo.setCode(getCodeArray[i]);
				prjinfoService.deleteCodeInfo(codeInfoVo);
			}
		}
		if(chkCode.equals("") || chkCode==null) {
			result.add("");
		}else {
			yCodeNm = yCodeNm.substring(0, yCodeNm.length()-1);
			result.add(yCodeNm);
		}		
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteJobCodeInfo
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 박정우
	 * 4. 설명: 타입정의에 project타입 리스트를 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteJobCodeInfo.do")
	@ResponseBody
	public Object deleteJobCodeInfo(CodeInfoVO codeInfoVo, UsersVO usersVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<UsersVO> getUserListVo = usersService.getUsersVOList();
		
		String getCode = codeInfoVo.getCode();
		String[] getCodeArray = getCode.split(",");
		String chkCode = "";
		String delCode = "";
		String yCodeNm = "";
		
		for(int i=0;i<getCodeArray.length;i++) {
			usersVo.setJob_tit_cd(getCodeArray[i]);
			int count = usersService.getUseJobCodeYN(usersVo);
			if(count>0) {
				// 사용중인 코드
				chkCode += getCodeArray[i] + ",";
				codeInfoVo.setCode(getCodeArray[i]);
				CodeInfoVO codeNmVo = prjinfoService.getUseYcodeNm(codeInfoVo);
				yCodeNm += codeNmVo.getCode_nm() + ",";
			}else{
				//삭제
				delCode += getCodeArray[i] + ",";
				codeInfoVo.setCode(getCodeArray[i]);
				prjinfoService.deleteCodeInfo(codeInfoVo);
			}
		}
		if(chkCode.equals("") || chkCode==null) {
			result.add("");
		}else {
			yCodeNm = yCodeNm.substring(0, yCodeNm.length()-1);
			result.add(yCodeNm);
		}		
		return result;
	}

	/**
	 * 1. 메소드명 : getAllUsersVOList
	 * 2. 작성일: 2022-03-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 당사임직원,협력업체,고객사 등 모든 유저 검색 (삭제된 유저 제외)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getAllUsersVOList.do")
	@ResponseBody
	public Object getAllUsersVOList(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> getUsersVOList = usersService.getAllUsersVOList();
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<getUsersVOList.size();i++) {
			UsersVO getUsersVO = new UsersVO();
			String user_id = getUsersVOList.get(i).getUser_id();
			String user_kor_nm = getUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = getUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = getUsersVOList.get(i).getUser_eng_nm();
			if(getUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(getUsersVOList.get(i).getOffi_tel());
				getUsersVO.setOffi_tel(offi_tel);
			}
			if(getUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(getUsersVOList.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = getUsersVOList.get(i).getCompany_nm();
			String hld_offi_gbn = getUsersVOList.get(i).getHld_offi_gbn();
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setCompany_nm(company_nm);
			getUsersVO.setHld_offi_gbn(hld_offi_gbn);
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : getAllOnUsersVOList
	 * 2. 작성일: 2022-04-11
	 * 3. 작성자: 소진희
	 * 4. 설명: 당사임직원,협력업체,고객사 등 모든 유저 검색 (삭제된 유저 제외) + 퇴직자 제외
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getAllOnUsersVOList.do")
	@ResponseBody
	public Object getAllOnUsersVOList(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> getUsersVOList = usersService.getAllOnUsersVOList();
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<getUsersVOList.size();i++) {
			UsersVO getUsersVO = new UsersVO();
			String user_id = getUsersVOList.get(i).getUser_id();
			String user_kor_nm = getUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = getUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = getUsersVOList.get(i).getUser_eng_nm();
			if(getUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(getUsersVOList.get(i).getOffi_tel());
				getUsersVO.setOffi_tel(offi_tel);
			}
			if(getUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(getUsersVOList.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = getUsersVOList.get(i).getCompany_nm();
			String hld_offi_gbn = getUsersVOList.get(i).getHld_offi_gbn();
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setCompany_nm(company_nm);
			getUsersVO.setHld_offi_gbn(hld_offi_gbn);
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : getUsersVOList
	 * 2. 작성일: 2022-03-06
	 * 3. 작성자: 소진희
	 * 4. 설명: 당사임직원 검색 (삭제된 유저 제외)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getUsersVOList.do")
	@ResponseBody
	public Object getUsersVOList(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> getUsersVOList = usersService.getUsersVOList();
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<getUsersVOList.size();i++) {
			UsersVO getUsersVO = new UsersVO();
			String user_id = getUsersVOList.get(i).getUser_id();
			String user_kor_nm = getUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = getUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = getUsersVOList.get(i).getUser_eng_nm();
			if(getUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(getUsersVOList.get(i).getOffi_tel());
				getUsersVO.setOffi_tel(offi_tel);
			}
			if(getUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(getUsersVOList.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = getUsersVOList.get(i).getCompany_nm();
			String user_type = getUsersVOList.get(i).getUser_type();
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setCompany_nm(company_nm);
			getUsersVO.setUser_type(user_type);
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : showDeleteUser
	 * 2. 작성일: 2022-02-26
	 * 3. 작성자: 소진희
	 * 4. 설명: 삭제된 사용자 보기 (User Register 페이지 Admin이 조회하는 기능)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "showDeleteUser.do")
	@ResponseBody
	public Object showDeleteUser(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> showDeleteUser = usersService.showDeleteUser();
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<showDeleteUser.size();i++) {
			UsersVO getUsersVO = new UsersVO();
			String user_id = showDeleteUser.get(i).getUser_id();
			String user_kor_nm = showDeleteUser.get(i).getUser_kor_nm();
			String user_group_nm = showDeleteUser.get(i).getUser_group_nm();
			String user_eng_nm = showDeleteUser.get(i).getUser_eng_nm();
			if(showDeleteUser.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(showDeleteUser.get(i).getOffi_tel());
				getUsersVO.setOffi_tel(offi_tel);
			}
			if(showDeleteUser.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(showDeleteUser.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = showDeleteUser.get(i).getCompany_nm();
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setCompany_nm(company_nm);
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : searchDeletedUsersVO
	 * 2. 작성일: 2022-04-21
	 * 3. 작성자: 소진희
	 * 4. 설명: 삭제된 사용자 검색 (User Register 페이지 Admin이 조회하는 기능)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchDeletedUsersVO.do")
	@ResponseBody
	public Object searchDeletedUsersVO(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchDeletedUsersVO = usersService.searchDeletedUsersVO(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchDeletedUsersVO.size();i++) {
			UsersVO getUsersVO = new UsersVO();
			String user_id = searchDeletedUsersVO.get(i).getUser_id();
			String user_kor_nm = searchDeletedUsersVO.get(i).getUser_kor_nm();
			String user_group_nm = searchDeletedUsersVO.get(i).getUser_group_nm();
			String user_eng_nm = searchDeletedUsersVO.get(i).getUser_eng_nm();
			if(searchDeletedUsersVO.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(searchDeletedUsersVO.get(i).getOffi_tel());
				getUsersVO.setOffi_tel(offi_tel);
			}
			if(searchDeletedUsersVO.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(searchDeletedUsersVO.get(i).getEmail_addr());
				getUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = searchDeletedUsersVO.get(i).getCompany_nm();
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setCompany_nm(company_nm);
			list.add(i, getUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : searchUsersVO
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 소진희
	 * 4. 설명: 전체 유저 검색
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchUsersVO.do")
	@ResponseBody
	public Object searchUsersVO(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchUsersVOList = usersService.searchUsersVO(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchUsersVOList.size();i++) {
			UsersVO searchUsersVO = new UsersVO();
			String reg_date = searchUsersVOList.get(i).getReg_date();
			String user_id = searchUsersVOList.get(i).getUser_id();
			String user_kor_nm = searchUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = searchUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = searchUsersVOList.get(i).getUser_eng_nm();
			if(searchUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(searchUsersVOList.get(i).getOffi_tel());
				searchUsersVO.setOffi_tel(offi_tel);
			}
			if(searchUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(searchUsersVOList.get(i).getEmail_addr());
				searchUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = searchUsersVOList.get(i).getCompany_nm();
			long connect_fail_cnt = searchUsersVOList.get(i).getConnect_fail_cnt();
			int passwd_fail_acc_stop_cnt = searchUsersVOList.get(i).getPasswd_fail_acc_stop_cnt();
			searchUsersVO.setReg_date(reg_date);
			searchUsersVO.setUser_id(user_id);
			searchUsersVO.setUser_kor_nm(user_kor_nm);
			searchUsersVO.setUser_group_nm(user_group_nm);
			searchUsersVO.setUser_eng_nm(user_eng_nm);
			searchUsersVO.setCompany_nm(company_nm);
			searchUsersVO.setConnect_fail_cnt(connect_fail_cnt);
			searchUsersVO.setPasswd_fail_acc_stop_cnt(passwd_fail_acc_stop_cnt);
			list.add(i, searchUsersVO);
		}
		result.add(list);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : searchUsersVOwithCode
	 * 2. 작성일: 2022. 3. 27.
	 * 3. 작성자: doil
	 * 4. 설명: drn에서 유저정보 가져오는 함수 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "searchUsersVOwithCode.do")
	@ResponseBody
	public Object searchUsersVOwithCode(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		
		String prj_id = usersVo.getPrj_id();
		String drn_id = usersVo.getCode();
		if(prj_id != null && drn_id != null) { // prj_id와 drn_id를 넘긴경우 session_id가 아닌 DB에 저장된 값을 가져와서 넘겨줌
			String reg_id = usersService.getDrnRegID(prj_id,drn_id);
			usersVo.setUser_id(reg_id);
		}
		
		
		
		UsersVO search = usersService.searchUserWithCode(usersVo);
		
		result.add(search);
		return result;
	}

	/**
	 * 1. 메소드명 : searchUsersVO
	 * 2. 작성일: 2022-02-28
	 * 3. 작성자: 소진희
	 * 4. 설명: 전체 유저 검색 (조직별로 한사람이 여러 로우 나올 수 있음)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchOrgUsersVO.do")
	@ResponseBody
	public Object searchOrgUsersVO(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchUsersVOList = usersService.searchOrgUsersVO(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchUsersVOList.size();i++) {
			UsersVO searchUsersVO = new UsersVO();
			String reg_date = searchUsersVOList.get(i).getReg_date();
			String user_id = searchUsersVOList.get(i).getUser_id();
			String user_kor_nm = searchUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = searchUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = searchUsersVOList.get(i).getUser_eng_nm();
			long org_id = searchUsersVOList.get(i).getOrg_id();
			if(searchUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(searchUsersVOList.get(i).getOffi_tel());
				searchUsersVO.setOffi_tel(offi_tel);
			}
			if(searchUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(searchUsersVOList.get(i).getEmail_addr());
				searchUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = searchUsersVOList.get(i).getCompany_nm();
			long connect_fail_cnt = searchUsersVOList.get(i).getConnect_fail_cnt();
			int passwd_fail_acc_stop_cnt = searchUsersVOList.get(i).getPasswd_fail_acc_stop_cnt();
			searchUsersVO.setReg_date(reg_date);
			searchUsersVO.setUser_id(user_id);
			searchUsersVO.setUser_kor_nm(user_kor_nm);
			searchUsersVO.setUser_group_nm(user_group_nm);
			searchUsersVO.setUser_eng_nm(user_eng_nm);
			searchUsersVO.setOrg_id(org_id);
			searchUsersVO.setCompany_nm(company_nm);
			searchUsersVO.setConnect_fail_cnt(connect_fail_cnt);
			searchUsersVO.setPasswd_fail_acc_stop_cnt(passwd_fail_acc_stop_cnt);
			list.add(i, searchUsersVO);
		}
		result.add(list);
		return result;
	}

	/**
	 * 1. 메소드명 : searchUsersVOCorA
	 * 2. 작성일: 2022-02-28
	 * 3. 작성자: 소진희
	 * 4. 설명: 고객사와 협력업체 유저 검색
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchUsersVOCorA.do")
	@ResponseBody
	public Object searchUsersVOCorA(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchUsersVOList = usersService.searchUsersVOCorA(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchUsersVOList.size();i++) {
			UsersVO searchUsersVO = new UsersVO();
			String reg_date = searchUsersVOList.get(i).getReg_date();
			String user_id = searchUsersVOList.get(i).getUser_id();
			String user_kor_nm = searchUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = searchUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = searchUsersVOList.get(i).getUser_eng_nm();
			long org_id = searchUsersVOList.get(i).getOrg_id();
			if(searchUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(searchUsersVOList.get(i).getOffi_tel());
				searchUsersVO.setOffi_tel(offi_tel);
			}
			if(searchUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(searchUsersVOList.get(i).getEmail_addr());
				searchUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = searchUsersVOList.get(i).getCompany_nm();
			long connect_fail_cnt = searchUsersVOList.get(i).getConnect_fail_cnt();
			int passwd_fail_acc_stop_cnt = searchUsersVOList.get(i).getPasswd_fail_acc_stop_cnt();
			searchUsersVO.setReg_date(reg_date);
			searchUsersVO.setUser_id(user_id);
			searchUsersVO.setUser_kor_nm(user_kor_nm);
			searchUsersVO.setUser_group_nm(user_group_nm);
			searchUsersVO.setUser_eng_nm(user_eng_nm);
			searchUsersVO.setOrg_id(org_id);
			searchUsersVO.setCompany_nm(company_nm);
			searchUsersVO.setConnect_fail_cnt(connect_fail_cnt);
			searchUsersVO.setPasswd_fail_acc_stop_cnt(passwd_fail_acc_stop_cnt);
			list.add(i, searchUsersVO);
		}
		result.add(list);
		return result;
	}
	
	@RequestMapping(value = "searchOutUsersVO.do")
	@ResponseBody
	public Object searchOutUsersVO(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchOutUsersVOList = usersService.searchOutUsersVO(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchOutUsersVOList.size();i++) {
			UsersVO searchUsersVO = new UsersVO();
			String user_id = searchOutUsersVOList.get(i).getUser_id();
			String user_kor_nm = searchOutUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = searchOutUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = searchOutUsersVOList.get(i).getUser_eng_nm();
			long org_id = searchOutUsersVOList.get(i).getOrg_id();
			if(searchOutUsersVOList.get(i).getOffi_tel()!=null) {
				String offi_tel = aes256.aesDecode(searchOutUsersVOList.get(i).getOffi_tel());
				searchUsersVO.setOffi_tel(offi_tel);
			}
			if(searchOutUsersVOList.get(i).getEmail_addr()!=null) {
				String email_addr = aes256.aesDecode(searchOutUsersVOList.get(i).getEmail_addr());
				searchUsersVO.setEmail_addr(email_addr);
			}
			String company_nm = searchOutUsersVOList.get(i).getCompany_nm();
			searchUsersVO.setUser_id(user_id);
			searchUsersVO.setUser_kor_nm(user_kor_nm);
			searchUsersVO.setUser_group_nm(user_group_nm);
			searchUsersVO.setUser_eng_nm(user_eng_nm);
			searchUsersVO.setOrg_id(org_id);
			searchUsersVO.setCompany_nm(company_nm);
			list.add(i, searchUsersVO);
		}
		result.add(list);
		return result;
	}
	
	
	@RequestMapping(value = "searchDrnUser.do")
	@ResponseBody
	public Object searchDrnUser(UsersVO usersVo){
		List<Object> result = new ArrayList<Object>();
		
		// 해당 이름으로 검색된 모든 유저의 리스트
		List<UsersVO> searchUsersVOList = usersService.searchDrnUser(usersVo);
		

		result.add(searchUsersVOList);
		return result;
	}

	@RequestMapping(value = "getUsersVO.do")
	@ResponseBody
	public Object getUsersVO(UsersVO usersVo) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		UsersVO getUsersVO = sessionService.getUsersVO(usersVo);
		if(getUsersVO!=null) {
			if(getUsersVO.getOffi_tel()!=null) {
				String dec_tel = aes256.aesDecode(getUsersVO.getOffi_tel());
				getUsersVO.setOffi_tel(dec_tel);
			}
			if(getUsersVO.getEmail_addr()!=null) {
				String dec_email = aes256.aesDecode(getUsersVO.getEmail_addr());
				getUsersVO.setEmail_addr(dec_email);
			}
		}
		result.add(getUsersVO);
		return result;
	}

	/**
	 * 1. 메소드명 : passwdReset
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 소진희
	 * 4. 설명: 임시 비밀번호 생성하여 해당 유저의 이메일로 보냄
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "passwdReset.do")
	@ResponseBody
	public Object passwdReset(HttpServletRequest request,UsersVO usersvo,PrjEmailVO prjemailvo) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String Id = usersvo.getUser_id();
		Id = Id.toUpperCase();
		String newPwd = Id + Integer.toString(CommonConst.random5());
		System.out.println("newPwd: " + newPwd);
		String aes_pwd = aes256.aesEncode(newPwd);
		String temp_pwd = Id + aes_pwd;
		usersvo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		usersvo.setReg_id("ADMIN");
		usersvo.setReg_date(CommonConst.currentDateAndTime());
		
		prjemailvo.setEmail_id(currentDateAndTime);
		prjemailvo.setSubject("VisionDCS 임시 비밀번호 안내");
		prjemailvo.setContents(Id+"님의 임시 비밀번호는 "+newPwd+" 입니다.");

		prjemailvo.setEmail_sender_user_id(MAIL_ID);
		prjemailvo.setEmail_sender_addr(MAIL_ACCOUNT);
		usersvo.setUser_id(Id);
		UsersVO getReceiverUserVO = sessionService.getUsersVO(usersvo);
		if(getReceiverUserVO!=null) {
			String dec_email = aes256.aesDecode(getReceiverUserVO.getEmail_addr());
			prjemailvo.setEmail_receiver_addr(dec_email);//비밀번호를 초기화하는 사용자의 이메일 정보가져와서 넣기
		}
		prjemailvo.setEmail_send_date(currentDateAndTime);
		prjemailvo.setReg_id("ADMIN");
		prjemailvo.setReg_date(CommonConst.currentDateAndTime());
		try {
			int updatePasswd = usersService.updatePasswd(usersvo);
			result.add(updatePasswd);
			int insertPasswdResetEmailSendInfo = prjemailService.insertPasswdResetEmailSendInfo(prjemailvo);
			result.add(insertPasswdResetEmailSendInfo);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("passwdReset : {}");
		}
		return result;
	}

	/**
	 * 1. 메소드명 : passwdResetAdmin
	 * 2. 작성일: 2022-02-19
	 * 3. 작성자: 소진희
	 * 4. 설명: User Register 페이지에서 Admin이 해당 유저의 비밀번호를 초기화할 경우 임시비밀번호는 아이디와 같음.(비밀번호 변경시 메일이 오지않을때를 대비하기 위함) 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "passwdResetAdmin.do")
	@ResponseBody
	public Object passwdResetAdmin(HttpServletRequest request,UsersVO usersvo,PrjEmailVO prjemailvo) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String Id = usersvo.getUser_id();
		Id = Id.toUpperCase();
		String newPwd = Id;
		System.out.println("newPwd: " + newPwd);
		String aes_pwd = aes256.aesEncode(newPwd);
		String temp_pwd = Id + aes_pwd;
		usersvo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		usersvo.setReg_id("ADMIN");
		usersvo.setReg_date(CommonConst.currentDateAndTime());
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		prjemailvo.setEmail_id(currentDateAndTime);
		prjemailvo.setSubject("VisionDCS 임시 비밀번호 안내");
		prjemailvo.setContents(sessioninfo.getUser_id() +"이 "+Id+"님의 비밀번호를 아이디로 초기화 하였습니다. 임시 비밀번호는 "+newPwd+" 입니다. 로그인하여 반드시 비밀번호를 변경해주세요.");

		prjemailvo.setEmail_sender_user_id(MAIL_ID);
		prjemailvo.setEmail_sender_addr(MAIL_ACCOUNT);
		usersvo.setUser_id(Id);
		UsersVO getReceiverUserVO = sessionService.getUsersVO(usersvo);
		if(getReceiverUserVO!=null) {
			String dec_email = aes256.aesDecode(getReceiverUserVO.getEmail_addr());
			prjemailvo.setEmail_receiver_addr(dec_email);//비밀번호를 초기화하는 사용자의 이메일 정보가져와서 넣기
		}
		prjemailvo.setEmail_send_date(currentDateAndTime);
		prjemailvo.setReg_id("ADMIN");
		prjemailvo.setReg_date(CommonConst.currentDateAndTime());
		try {
			int updatePasswd = usersService.updatePasswd(usersvo);
			result.add(updatePasswd);
			int insertPasswdResetEmailSendInfo = prjemailService.insertPasswdResetEmailSendInfo(prjemailvo);
			result.add(insertPasswdResetEmailSendInfo);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("passwdReset : {}");
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getJobCodeList
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 소진희
	 * 4. 설명: 직책코드 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getJobCodeList.do")
	@ResponseBody
	public Object getJobCodeList(UsersVO usersVo){
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getJobCodeList = usersService.getJobCodeList(usersVo);
		result.add(getJobCodeList);
		return result;
	}

	/**
	 * 1. 메소드명 : getOffiCodeList
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 소진희
	 * 4. 설명: 직책코드 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOffiCodeList.do")
	@ResponseBody
	public Object getOffiCodeList(UsersVO usersVo){
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getOffiCodeList = usersService.getOffiCodeList(usersVo);
		result.add(getOffiCodeList);
		return result;
	}

	/**
	 * 1. 메소드명 : deleteUsersVO
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: p_users 테이블 내 해당 유저 상태 D(삭제)로 변경(실제 삭제X)
	 * 5. 수정일: 2022-03-16 유저 삭제시 project member와 group member에서도 삭제
	 */
	@RequestMapping(value = "deleteUsersVO.do")
	@ResponseBody
	public Object deleteUsersVO(UsersVO usersVo,OrganizationUserVO organizationuservo) {
		List<Object> result = new ArrayList<Object>();
		String checkedUserString = usersVo.getCheckedUserString();
		String[] checkedUserEach = checkedUserString.split("@@");
		for(int i=0;i<checkedUserEach.length;i++) {
			String[] checkedUser = checkedUserEach[i].split("&&");
			organizationuservo.setUser_id(checkedUser[0]);
			int getUserOrgCount = organizationuserService.getUserOrgCount(organizationuservo);
			usersVo.setUser_id(checkedUser[0]);
			if(getUserOrgCount<=1) {
				usersService.updateUserSt(usersVo);
				usersService.deletePrjMemberByDeleteUser(usersVo);
				usersService.deletePrjGroupMemberByDeleteUser(usersVo);
			}
		}
		return result;
	}

	/**
	 * 1. 메소드명 : isExistUser
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: p_users 테이블에 존재하는 user_id인지 판단
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "isExistUser.do")
	@ResponseBody
	public Object isExistUser(UsersVO usersVo,OrganizationUserVO organizationuservo) {
		List<Object> result = new ArrayList<Object>();
		usersVo.setUser_id(usersVo.getUser_id().toUpperCase());
		int count = usersService.getExistUser(usersVo);
		result.add(count);
		return result;
	}

	/**
	 * 1. 메소드명 : userRegister
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: p_users 테이블 유저 등록
	 * 5. 수정일: 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "userRegister.do")
	@ResponseBody
	public Object userRegister(HttpServletRequest request,UsersVO usersvo) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String Id = usersvo.getUser_id();
		Id = Id.toUpperCase();
		usersvo.setUser_id(Id);
		String newPwd = usersvo.getPasswd();
		String aes_pwd = aes256.aesEncode(newPwd);
		String temp_pwd = Id + aes_pwd;
		usersvo.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
		String aes_tel = aes256.aesEncode(usersvo.getOffi_tel());
		usersvo.setOffi_tel(aes_tel);
		String aes_email = aes256.aesEncode(usersvo.getEmail_addr());
		usersvo.setEmail_addr(aes_email);
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		usersvo.setReg_id(sessioninfo.getUser_id());
		usersvo.setReg_date(CommonConst.currentDateAndTime());
		usersService.userRegister(usersvo);
		return result;
	}


	/**
	 * 1. 메소드명 : userInfoUpdate
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 소진희
	 * 4. 설명: p_users 테이블 유저 정보 업데이트
	 * 5. 수정일: 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "userInfoUpdate.do")
	@ResponseBody
	public Object userInfoUpdate(HttpServletRequest request,UsersVO usersvo) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String Id = usersvo.getUser_id();
		String aes_tel = aes256.aesEncode(usersvo.getOffi_tel());
		usersvo.setOffi_tel(aes_tel);
		String aes_email = aes256.aesEncode(usersvo.getEmail_addr());
		usersvo.setEmail_addr(aes_email);
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		usersvo.setReg_id(sessioninfo.getUser_id());
		usersvo.setReg_date(CommonConst.currentDateAndTime());
		int updateUserInfo = usersService.updateUserInfo(usersvo);
		result.add(updateUserInfo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getHRInformation
	 * 2. 작성일: 2022-03-21
	 * 3. 작성자: 소진희
	 * 4. 설명: 인사정보 가져오기
	 * 5. 수정일: 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "getHRInformation.do")
	@ResponseBody
	public Object getHRInformation(UsersVO usersVo,HttpServletRequest request) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getHRInformation =  usersServiceOracle.getHRInformation();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		int count = 0;
		for(int i=0;i<getHRInformation.size();i++) {
			usersVo.setUser_id(getHRInformation.get(i).getUser_id());
			int getExistUser = usersService.getExistUser(usersVo);
			if(getExistUser==0) {	//존재하지 않는 사번일 경우 insert
				count++;
				String Id = getHRInformation.get(i).getUser_id();
				Id = Id.toUpperCase();
				getHRInformation.get(i).setUser_id(Id);
				String newPwd = getHRInformation.get(i).getPasswd();
				String aes_pwd = aes256.aesEncode(newPwd);
				String temp_pwd = Id + aes_pwd;
				getHRInformation.get(i).setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
				if(getHRInformation.get(i).getOffi_tel()!=null) {
					String aes_tel = aes256.aesEncode(getHRInformation.get(i).getOffi_tel());
					getHRInformation.get(i).setOffi_tel(aes_tel);
				}
				if(getHRInformation.get(i).getEmail_addr()!=null) {
					String aes_email = aes256.aesEncode(getHRInformation.get(i).getEmail_addr());
					getHRInformation.get(i).setEmail_addr(aes_email);
				}
				usersService.userRegisterHRInfo(getHRInformation.get(i));
			}
		}
		result.add(count);
		return result;
	}
}
