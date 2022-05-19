package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationUserVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class OrganizationUserService extends AbstractService {
	public List<UsersVO> getAllUserList(){
		List<UsersVO> getAllUserList = selectList("organizationuserSqlMap.getAllUserList");
		return getAllUserList;
	}
	public List<UsersVO> getAllUserListCorA(){
		List<UsersVO> getAllUserListCorA = selectList("organizationuserSqlMap.getAllUserListCorA");
		return getAllUserListCorA;
	}
	public List<UsersVO> getAllUserListHyundae(){
		List<UsersVO> getAllUserListHyundae = selectList("organizationuserSqlMap.getAllUserListHyundae");
		return getAllUserListHyundae;
	}
	public List<UsersVO> getAllUserListHyundaePlusDeleteUser(){
		List<UsersVO> getAllUserListHyundaePlusDeleteUser = selectList("organizationuserSqlMap.getAllUserListHyundaePlusDeleteUser");
		return getAllUserListHyundaePlusDeleteUser;
	}
	public List<UsersVO> searchUsersHyundaeVO(UsersVO usersVo){
		List<UsersVO> searchUsersHyundaeVO = selectList("organizationuserSqlMap.searchUsersHyundaeList", usersVo);
		return searchUsersHyundaeVO;
	}
	public List<UsersVO> searchUsersHyundaeVOPlusDeleteUser(UsersVO usersVo){
		List<UsersVO> searchUsersHyundaeVOPlusDeleteUser = selectList("organizationuserSqlMap.searchUsersHyundaeVOPlusDeleteUser", usersVo);
		return searchUsersHyundaeVOPlusDeleteUser;
	}
	
	public List<UsersVO> getAllOutUserList(){
		List<UsersVO> getAllOutUserList = selectList("organizationuserSqlMap.getAllOutUserList");
		return getAllOutUserList;
	}
	
	public List<UsersVO> getOrganizationUserListAll(OrganizationVO organizationvo){
		List<UsersVO> getOrganizationUserListAll = selectList("organizationuserSqlMap.getOrganizationUserListAll",organizationvo);
		return getOrganizationUserListAll;
	}
	
	public List<UsersVO> getOrganizationUserList(OrganizationVO organizationvo){
		List<UsersVO> getOrganizationUserList = selectList("organizationuserSqlMap.getOrganizationUserList",organizationvo);
		return getOrganizationUserList;
	}
	
	public List<UsersVO> getOrganizationUserListPlusDeleteUser(OrganizationVO organizationvo){
		List<UsersVO> getOrganizationUserListPlusDeleteUser = selectList("organizationuserSqlMap.getOrganizationUserListPlusDeleteUser",organizationvo);
		return getOrganizationUserListPlusDeleteUser;
	}
	
	public List<UsersVO> getOrganizationOutUserList(OrganizationVO organizationvo){
		List<UsersVO> getOrganizationOutUserList = selectList("organizationuserSqlMap.getOrganizationOutUserList",organizationvo);
		return getOrganizationOutUserList;
	}
	
	public int getOrganizationUserCnt(OrganizationVO organizationvo){
		int getOrganizationUserCnt = selectOne("organizationuserSqlMap.getOrganizationUserCnt",organizationvo);
		return getOrganizationUserCnt;
	}
	public List<UsersVO> searchUserList(UsersVO usersvo){
		List<UsersVO> searchUserList = selectList("organizationuserSqlMap.searchUserList", usersvo);
		return searchUserList;
	}
	public int getUserOrgCount(OrganizationUserVO organizationuservo){
		int count = selectOne("organizationuserSqlMap.getUserOrgCount", organizationuservo);
		return count;
	}
	public void deleteOrganizationUser(OrganizationUserVO organizationuservo){
		delete("organizationuserSqlMap.deleteOrganizationUser", organizationuservo);
	}
	public int countOrganizationUser(OrganizationUserVO organizationuservo){
		int count = selectOne("organizationuserSqlMap.countOrganizationUser", organizationuservo);
		return count;
	}
	public void insertOrganizationUser(OrganizationUserVO organizationuservo){
		insert("organizationuserSqlMap.insertOrganizationUser", organizationuservo);
	}
	public void updateOrganizationUser(OrganizationUserVO organizationuservo){
		update("organizationuserSqlMap.updateOrganizationUser", organizationuservo);
	}
	public void deleteOrgUser(OrganizationUserVO organizationuservo){
		delete("organizationuserSqlMap.deleteOrgUser", organizationuservo);
	}
}
