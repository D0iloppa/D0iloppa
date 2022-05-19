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
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.OrganizationService;
import kr.co.hhi.service.OrganizationUserService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.UsersService;

@Controller
@RequestMapping("/*")
public class OrganizationCont {

	protected OrganizationService organizationService;
	protected OrganizationUserService organizationuserService;
	protected UsersService  usersService;
	protected PrjInfoService prjinfoService;

	@Autowired
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
	@Autowired
	public void setOrganizationUserService(OrganizationUserService organizationuserService) {
		this.organizationuserService = organizationuserService;
	}
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}

	/**
	 * 1. 메소드명 : getOrganizationList
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: 조직 정보 리스트를 가져와 화면에 트리구조로 뿌려줌
	 * 5. 수정일: 2022-02-28 ADMIN 페이지 / DCC 페이지 구분
	 */
	@RequestMapping(value = "getOrganizationList.do")
	@ResponseBody
	public Object getOrganizationList(HttpServletRequest request,OrganizationVO organizationvo,PrjMemberVO prjmembervo) {
		List<Object> result = new ArrayList<Object>();
		if(organizationvo.getUserMngPage().equals("ADMIN")){
			List<OrganizationVO> getOrganizationList = organizationService.getOrganizationList(organizationvo);
			result.add(getOrganizationList);
		}else {//DCC
			List<OrganizationVO> getOrganizationListCorA = organizationService.getOrganizationListCorA(organizationvo);
			result.add(getOrganizationListCorA);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getOrganizationList
	 * 2. 작성일: 2022-02-22
	 * 3. 작성자: 소진희
	 * 4. 설명: 현대파워시스템의 조직 정보 리스트를 가져와 화면에 트리구조로 뿌려줌(고객사와 협력업체는 제외) - 조직도 편집기에서 사용
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrganizationListOnlyHyundae.do")
	@ResponseBody
	public Object getOrganizationListOnlyHyundae(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<OrganizationVO> getOrganizationListOnlyHyundae = organizationService.getOrganizationListOnlyHyundae(organizationvo);
		result.add(getOrganizationListOnlyHyundae);
		return result;
	}
	/**
	 * 
	 * 1. 메소드명 : getOrganizationListInPrj
	 * 2. 작성일: 2022. 1. 11.
	 * 3. 작성자: doil
	 * 4. 설명: 프로젝트에 투입된 멤버들의 조직정보  및 서명정보
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getOrganizationListInPrj.do")
	@ResponseBody
	public Object getOrganizationListInPrj(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<OrganizationVO> getOrganizationList = organizationService.getOrganizationList(organizationvo);
		result.add(getOrganizationList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : createNewRootOrg
	 * 2. 작성일: 2022-01-24
	 * 3. 작성자: 소진희
	 * 4. 설명: p_organization테이블에 새 조직그룹 정보를 넣음 (고객사, 협력업체 등 ROOT 노드)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "createNewRootOrg.do")
	@ResponseBody
	public Object createNewRootOrg(HttpServletRequest request,OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		OrganizationVO getLastOrgId = organizationService.getLastOrgId(organizationvo);
		long newOrgId = getLastOrgId.getOrg_id()+1;
		OrganizationVO newOrgvo = new OrganizationVO();
		newOrgvo.setOrg_id(newOrgId);
		newOrgvo.setOrg_parent_id(newOrgId);
		newOrgvo.setOrg_path(Long.toString(newOrgId));
		newOrgvo.setOrg_nm(organizationvo.getOrg_nm());
		newOrgvo.setOrg_type(organizationvo.getOrg_type());
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		newOrgvo.setReg_id(sessioninfo.getUser_id());
		newOrgvo.setReg_date(CommonConst.currentDateAndTime());
		organizationService.insertOrganizationVO(newOrgvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : createNewOrg
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: p_organization테이블에 새 조직그룹 정보를 넣음
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "createNewOrg.do")
	@ResponseBody
	public Object createNewOrg(HttpServletRequest request,OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		OrganizationVO getLastOrgId = organizationService.getLastOrgId(organizationvo);
		long newOrgId = getLastOrgId.getOrg_id()+1;
		OrganizationVO getParentOrgPath = organizationService.getParentOrgPath(organizationvo);
		OrganizationVO newOrgvo = new OrganizationVO();
		newOrgvo.setOrg_id(organizationvo.getOrg_id());
		newOrgvo.setOrg_parent_id(organizationvo.getOrg_parent_id());
		if(getParentOrgPath!=null) {
			newOrgvo.setOrg_path(getParentOrgPath.getOrg_path()+">"+newOrgId);
		}else {
			newOrgvo.setOrg_path(Long.toString(newOrgId));
		}
		newOrgvo.setOrg_nm(organizationvo.getOrg_nm());
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		newOrgvo.setReg_id(sessioninfo.getUser_id());
		newOrgvo.setReg_date(CommonConst.currentDateAndTime());
		newOrgvo.setOrg_type(organizationvo.getOrg_type());
		organizationService.insertOrganizationVO(newOrgvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : renameOrg
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: p_organization 테이블의 org_nm 변경
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "renameOrg.do")
	@ResponseBody
	public Object renameOrg(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		organizationService.updateOrgNm(organizationvo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteOrg
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: p_organization 테이블 내 조직그룹정보 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteOrg.do")
	@ResponseBody
	public Object deleteOrg(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		List<OrganizationVO> getDownOrg = organizationService.getDownOrg(organizationvo);
		OrganizationVO neworganizationvo = new OrganizationVO();
		for(int i=0;i<getDownOrg.size();i++){
			neworganizationvo.setOrg_id(getDownOrg.get(i).getOrg_id());
			organizationService.deleteOrg(neworganizationvo);
			usersService.updateUserStForOrg(neworganizationvo);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteRootOrg
	 * 2. 작성일: 2022-03-06
	 * 3. 작성자: 소진희
	 * 4. 설명: p_organization 테이블 내 Root 조직 삭제해도 되는지 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteRootOrg.do")
	@ResponseBody
	public Object deleteRootOrg(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		int getIsUseRootOrg = organizationService.getIsUseRootOrg(organizationvo);
		int getOrganizationUserCnt = organizationuserService.getOrganizationUserCnt(organizationvo);
		result.add(getIsUseRootOrg+getOrganizationUserCnt);
		if(getIsUseRootOrg+getOrganizationUserCnt==0) {
			organizationService.deleteOrg(organizationvo);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : jstreeDND
	 * 2. 작성일: 2022-01-25
	 * 3. 작성자: 소진희
	 * 4. 설명: jstree 내에서 드래그앤드롭으로 부서이동
	 * 5. 수정일: 2022-02-22
	 */
	@RequestMapping(value = "jstreeDND.do")
	@ResponseBody
	public Object jstreeDND(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		long new_org_parent_id = organizationvo.getOrg_parent_id();
		//해당 부서의 원래 org_path를 가져옴
		organizationvo.setOrg_parent_id(organizationvo.getOrg_id());
		OrganizationVO originOrgPath = organizationService.getParentOrgPath(organizationvo);
		String origin_org_path = originOrgPath.getOrg_path();
		organizationvo.setOrigin_org_path(origin_org_path);
		if(organizationvo.getOrg_id()==new_org_parent_id) {	//루트노드로 바뀌었을 경우
			organizationvo.setOrg_path(Long.toString(organizationvo.getOrg_id()));
		}else {//루트노드가 아닌 하위노드일 경우 부모의 org_path를 알아와야함
			organizationvo.setOrg_parent_id(new_org_parent_id);
			OrganizationVO parentOrgPath = organizationService.getParentOrgPath(organizationvo);
			String parent_org_path = parentOrgPath.getOrg_path();
			organizationvo.setOrg_path(parent_org_path + ">" + organizationvo.getOrg_id());
		}
		//이동된 부서의 org_parent_id와 org_path 업데이트
		int updateOrgParentIdByJstreeDND = organizationService.updateOrgParentIdByJstreeDND(organizationvo);
		
		//해당 부서의 현재 org_path를 가져옴
		organizationvo.setOrg_parent_id(organizationvo.getOrg_id());
		OrganizationVO currentOrgPath = organizationService.getParentOrgPath(organizationvo);
		String current_org_path = currentOrgPath.getOrg_path();
		organizationvo.setOrg_path(current_org_path);
		//해당 org_id를 org_parent_id로 갖고있는 자식요소들 모두 update
		int updateOrgPathByJstreeDND = organizationService.updateOrgPathByJstreeDND(organizationvo);
		result.add(updateOrgParentIdByJstreeDND);
		result.add(updateOrgPathByJstreeDND);
		
		//2022-02-22 추가: 자식요소들의 하위요소들까지 모두 update
		int updateGrandsonOrgPathByJstreeDND = organizationService.updateGrandsonOrgPathByJstreeDND(organizationvo);
		return result;
	}

	/**
	 * 1. 메소드명 : getOrgGrpNmPath
	 * 2. 작성일: 2021-12-07
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 그룹의 조직 정보를 가져옴 ex)현대중공업파워시스템>IT사업부>SI사업팀>개발
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getOrgGrpNmPath.do")
	@ResponseBody
	public Object getOrgGrpNmPath(OrganizationVO organizationvo) {
		List<Object> result = new ArrayList<Object>();
		OrganizationVO getOrgGrpNmPath = organizationService.getOrgGrpNmPath(organizationvo);
		result.add(getOrgGrpNmPath);
		return result;
	}
}
