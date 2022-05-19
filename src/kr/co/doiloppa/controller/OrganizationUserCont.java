package kr.co.doiloppa.controller;

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
import kr.co.doiloppa.model.OrganizationUserVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.OrganizationUserService;
import kr.co.doiloppa.service.SessionService;
import kr.co.doiloppa.service.UsersService;

@Controller
@RequestMapping("/*")
public class OrganizationUserCont {

	protected OrganizationUserService organizationuserService;
	
	@Autowired
	public void setOrganizationUserService(OrganizationUserService organizationuserService) {
		this.organizationuserService = organizationuserService;
	}
	/**
	 * 1. 메소드명 : getAllUserList
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: 모든 유저 정보 리스트를 가져옴 (UserRegister페이지에서 사용)
	 * 5. 수정일: 2022-02-28 외부사용자관리 페이지에서는 고객사와 협력업체 리스트만 가져옴 (DCC가 관리)
	 */
	@RequestMapping(value = "getAllUserList.do")
	@ResponseBody
	public Object getAllUserList(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		if(organizationvo.getUserMngPage().equals("ADMIN")){
			List<UsersVO> getAllUserList = organizationuserService.getAllUserList();
			result.add(getAllUserList);
		}else {//DCC
			List<UsersVO> getAllOutUserList = organizationuserService.getAllOutUserList();
			result.add(getAllOutUserList);
		}
		return result;
	}
	/**
	 * 1. 메소드명 : getAllUserListHyundae
	 * 2. 작성일: 2022-02-22
	 * 3. 작성자: 소진희
	 * 4. 설명: 현대파워시스템에 속한 모든 유저 정보 리스트를 가져옴 (조직도 편집기에서 사용)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getAllUserListHyundae.do")
	@ResponseBody
	public Object getAllUserListHyundae() {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getAllUserListHyundae = organizationuserService.getAllUserListHyundae();
		result.add(getAllUserListHyundae);
		return result;
	}
	/**
	 * 1. 메소드명 : getAllUserListHyundaePlusDeleteUser
	 * 2. 작성일: 2022-04-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 현대파워시스템에 속한 모든 유저 정보 리스트를 가져옴 (조직도 편집기에서 사용) + 삭제된 유저도 포함 (고객사,협력업체는 제외)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getAllUserListHyundaePlusDeleteUser.do")
	@ResponseBody
	public Object getAllUserListHyundaePlusDeleteUser() {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getAllUserListHyundaePlusDeleteUser = organizationuserService.getAllUserListHyundaePlusDeleteUser();
		result.add(getAllUserListHyundaePlusDeleteUser);
		return result;
	}
	
	/**
	 * 1. 메소드명 : searchUsersHyundaeVO
	 * 2. 작성일: 2022-02-25
	 * 3. 작성자: 박정우
	 * 4. 설명: (현대)조직 정보 리스트를 가져와 화면에 트리구조로 뿌려줌
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchUsersHyundaeVO.do")
	@ResponseBody
	public Object searchUsersHyundaeVO(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchUsersVOList = organizationuserService.searchUsersHyundaeVO(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchUsersVOList.size();i++) {
			UsersVO searchUsersVO = new UsersVO();
			String reg_date = searchUsersVOList.get(i).getReg_date();
			String user_id = searchUsersVOList.get(i).getUser_id();
			String user_kor_nm = searchUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = searchUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = searchUsersVOList.get(i).getUser_eng_nm();
			long org_id = searchUsersVOList.get(i).getOrg_id();
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
	 * 1. 메소드명 : searchUsersHyundaeVOPlusDeleteUser
	 * 2. 작성일: 2022-04-05
	 * 3. 작성자: 소진희
	 * 4. 설명: (현대)조직 정보 리스트를 가져와 화면에 트리구조로 뿌려줌 + 삭제 유저 포함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchUsersHyundaeVOPlusDeleteUser.do")
	@ResponseBody
	public Object searchUsersHyundaeVOPlusDeleteUser(UsersVO usersVo) throws IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		List<Object> result = new ArrayList<Object>();
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		List<UsersVO> searchUsersVOList = organizationuserService.searchUsersHyundaeVOPlusDeleteUser(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		for(int i=0;i<searchUsersVOList.size();i++) {
			UsersVO searchUsersVO = new UsersVO();
			String reg_date = searchUsersVOList.get(i).getReg_date();
			String user_id = searchUsersVOList.get(i).getUser_id();
			String user_kor_nm = searchUsersVOList.get(i).getUser_kor_nm();
			String user_group_nm = searchUsersVOList.get(i).getUser_group_nm();
			String user_eng_nm = searchUsersVOList.get(i).getUser_eng_nm();
			long org_id = searchUsersVOList.get(i).getOrg_id();
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
	 * 1. 메소드명 : getAllOutUserList
	 * 2. 작성일: 2022-01-19
	 * 3. 작성자: 박정우
	 * 4. 설명: 모든 외부유저 정보 리스트를 가져옴 (외부사용자관리 페이지에서 사용)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getAllOutUserList.do")
	@ResponseBody
	public Object getAllOutUserList() {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getAllOutUserList = organizationuserService.getAllOutUserList();
		result.add(getAllOutUserList);
		return result;
	}
	/**
	 * 1. 메소드명 : jstreeDNDPerson
	 * 2. 작성일: 2022-01-25
	 * 3. 작성자: 소진희
	 * 4. 설명: jstree 내에서 드래그앤드롭으로 부서이동 (유저의 부서이동)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "jstreeDNDPerson.do")
	@ResponseBody
	public Object jstreeDNDPerson(OrganizationUserVO organizationuservo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		organizationuservo.setMod_id(sessioninfo.getUser_id());
		organizationuservo.setMod_date(CommonConst.currentDateAndTime());
		organizationuserService.updateOrganizationUser(organizationuservo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getOrganizationUserListAll
	 * 2. 작성일: 2022-01-25
	 * 3. 작성자: 소진희
	 * 4. 설명: 그룹에 속한 유저 정보 리스트 전체 목록을 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrganizationUserListAll.do")
	@ResponseBody
	public Object getOrganizationUserListAll(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getOrganizationUserListAll = organizationuserService.getOrganizationUserListAll(organizationvo);
		result.add(getOrganizationUserListAll);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getOrganizationUserList
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 그룹에 속한 유저 정보 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrganizationUserList.do")
	@ResponseBody
	public Object getOrganizationUserList(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getOrganizationUserList = organizationuserService.getOrganizationUserList(organizationvo);
		result.add(getOrganizationUserList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getOrganizationUserListPlusDeleteUser
	 * 2. 작성일: 2022-04-05
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 그룹에 속한 유저 정보 리스트를 가져옴 + 삭제된 유저 포함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrganizationUserListPlusDeleteUser.do")
	@ResponseBody
	public Object getOrganizationUserListPlusDeleteUser(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getOrganizationUserListPlusDeleteUser = organizationuserService.getOrganizationUserListPlusDeleteUser(organizationvo);
		result.add(getOrganizationUserListPlusDeleteUser);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getOrganizationUserList
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 박정우
	 * 4. 설명: 각 그룹에 속한 외부 유저 정보 리스트를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrganizationOutUserList.do")
	@ResponseBody
	public Object getOrganizationOutUserList(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> getOrganizationOutUserList = organizationuserService.getOrganizationOutUserList(organizationvo);
		result.add(getOrganizationOutUserList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : getOrganizationUserCnt
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 그룹에 속한 유저 정보 수를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrganizationUserCnt.do")
	@ResponseBody
	public Object getOrganizationUserCnt(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		int getOrganizationUserCnt = organizationuserService.getOrganizationUserCnt(organizationvo);
		result.add(getOrganizationUserCnt);
		return result;
	}
	/**
	 * 1. 메소드명 : searchUserList
	 * 2. 작성일: 2021-12-08
	 * 3. 작성자: 소진희
	 * 4. 설명: UserRegister 페이지에서 유저 검색 기능 담당
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchUserList.do")
	@ResponseBody
	public Object searchUserList(UsersVO usersvo) {
		List<Object> result = new ArrayList<Object>();
		List<UsersVO> searchUserList = organizationuserService.searchUserList(usersvo);
		result.add(searchUserList);
		return result;
	}

	/**
	 * 1. 메소드명 : deleteOrganizationUser
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: p_organization_user 테이블 내 해당 유저 정보 삭제(user_id와 org_id가 일치할 경우 삭제)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteOrganizationUser.do")
	@ResponseBody
	public Object deleteOrganizationUser(UsersVO usersVo,OrganizationUserVO organizationuservo) {
		List<Object> result = new ArrayList<Object>();
		String checkedUserString = usersVo.getCheckedUserString();
		String[] checkedUserEach = checkedUserString.split("@@");
		for(int i=0;i<checkedUserEach.length;i++) {
			String[] checkedUser = checkedUserEach[i].split("&&");
			organizationuservo.setUser_id(checkedUser[0]);
			organizationuservo.setOrg_id(Long.parseLong(checkedUser[1]));
			organizationuserService.deleteOrganizationUser(organizationuservo);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : AddGroupUser
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 유저의 조직그룹 추가
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "AddGroupUser.do")
	@ResponseBody
	public Object AddGroupUser(UsersVO usersVo,OrganizationUserVO organizationuservo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		organizationuservo.setReg_id(sessioninfo.getUser_id());
		organizationuservo.setReg_date(CommonConst.currentDateAndTime());
		String checkedUserString = usersVo.getCheckedUserString();
		String[] checkedUserEach = checkedUserString.split("@@");
		for(int i=0;i<checkedUserEach.length;i++) {
			String[] checkedUser = checkedUserEach[i].split("&&");
			organizationuservo.setUser_id(checkedUser[0]);
			organizationuservo.setOrg_id(Long.parseLong(checkedUser[1]));
			int count = organizationuserService.countOrganizationUser(organizationuservo);
			if(count == 0) {
				organizationuserService.insertOrganizationUser(organizationuservo);
			}
		}
		return result;
	}
	/**
	 * 1. 메소드명 : ChgGroupUser
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 유저의 조직그룹 변경
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "ChgGroupUser.do")
	@ResponseBody
	public Object ChgGroupUser(UsersVO usersVo,OrganizationUserVO organizationuservo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		organizationuservo.setReg_id(sessioninfo.getUser_id());
		organizationuservo.setReg_date(CommonConst.currentDateAndTime());
		organizationuservo.setMod_id(sessioninfo.getUser_id());
		organizationuservo.setMod_date(CommonConst.currentDateAndTime());
		String checkedUserString = usersVo.getCheckedUserString();
		String[] checkedUserEach = checkedUserString.split("@@");
		for(int i=0;i<checkedUserEach.length;i++) {
			String[] checkedUser = checkedUserEach[i].split("&&");
			organizationuservo.setUser_id(checkedUser[0]);
			organizationuservo.setOrigin_org_id(Long.parseLong(checkedUser[1]));
			organizationuservo.setOrg_id(Long.parseLong(checkedUser[2]));
			System.out.println(checkedUser[1]);
			if(checkedUser[1].equals("0")||checkedUser[1]==null) {
				organizationuserService.insertOrganizationUser(organizationuservo);
			}else {
				organizationuserService.updateOrganizationUser(organizationuservo);
			}
		}
		return result;
	}
	/**
	 * 1. 메소드명 : insertOrganizationUser
	 * 2. 작성일: 2021-12-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 유저 등록시 그룹 등록
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "insertOrganizationUser.do")
	@ResponseBody
	public Object insertOrganizationUser(UsersVO usersVo,OrganizationUserVO organizationuservo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		organizationuservo.setReg_id(sessioninfo.getUser_id());
		organizationuservo.setReg_date(CommonConst.currentDateAndTime());
		organizationuserService.insertOrganizationUser(organizationuservo);
		return result;
	}
}
