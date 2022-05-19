package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.CodeInfoVO;
import kr.co.hhi.model.DrnLineVO;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.UsersVO;

@Service
public class UsersService extends AbstractService {
	
	public List<UsersVO> getAdminList(){
		List<UsersVO> getAdminList = selectList("usersSqlMap.getAdminList");
		return getAdminList;
	}
	public List<UsersVO> showDeleteUser(){
		List<UsersVO> showDeleteUser = selectList("usersSqlMap.showDeleteUser");
		return showDeleteUser;
	}
	public List<UsersVO> searchDeletedUsersVO(UsersVO usersVo){
		List<UsersVO> searchDeletedUsersVO = selectList("usersSqlMap.searchDeletedUsersVO", usersVo);
		return searchDeletedUsersVO;
	}
	public List<UsersVO> getAllUsersVOList(){
		List<UsersVO> getAllUsersVOList = selectList("usersSqlMap.getAllUsersVOList");
		return getAllUsersVOList;
	}
	public List<UsersVO> getAllOnUsersVOList(){
		List<UsersVO> getAllOnUsersVOList = selectList("usersSqlMap.getAllOnUsersVOList");
		return getAllOnUsersVOList;
	}
	public List<UsersVO> getUsersVOList(){
		List<UsersVO> getUsersVOList = selectList("usersSqlMap.getUsersVOList");
		return getUsersVOList;
	}
	public int getExistUser(UsersVO usersVo){
		int count = selectOne("usersSqlMap.getExistUser", usersVo);
		return count;
	}
	public List<UsersVO> searchUsersVO(UsersVO usersVo){
		List<UsersVO> searchUsersVO = selectList("usersSqlMap.searchUsersVO", usersVo);
		return searchUsersVO;
	}
	public List<UsersVO> searchOrgUsersVO(UsersVO usersVo){
		List<UsersVO> searchOrgUsersVO = selectList("usersSqlMap.searchOrgUsersVO", usersVo);
		return searchOrgUsersVO;
	}
	public List<UsersVO> searchUsersVOCorA(UsersVO usersVo){
		List<UsersVO> searchUsersVOCorA = selectList("usersSqlMap.searchUsersVOCorA", usersVo);
		return searchUsersVOCorA;
	}
	public List<UsersVO> searchOutUsersVO(UsersVO usersVo){
		List<UsersVO> searchOutUsersVO = selectList("usersSqlMap.searchOutUsersVO", usersVo);
		return searchOutUsersVO;
	}
	public void updateUserSt(UsersVO usersVo){
		update("usersSqlMap.updateUserSt", usersVo);
	}
	public int deletePrjMemberByDeleteUser(UsersVO usersVo){
		return delete("usersSqlMap.deletePrjMemberByDeleteUser", usersVo);
	}
	public int deletePrjGroupMemberByDeleteUser(UsersVO usersVo){
		return delete("usersSqlMap.deletePrjGroupMemberByDeleteUser", usersVo);
	}
	public void updateUserStForOrg(OrganizationVO organizationVo){
		update("usersSqlMap.updateUserStForOrg", organizationVo);
	}
	public void userRegister(UsersVO usersVo){
		insert("usersSqlMap.userRegister", usersVo);
	}
	public void userRegisterHRInfo(UsersVO usersVo){
		insert("usersSqlMap.userRegisterHRInfo", usersVo);
	}
	public int updateUserInfo(UsersVO usersVo){
		int updateUserInfo = update("usersSqlMap.updateUserInfo", usersVo);
		return updateUserInfo;
	}
	public List<UsersVO> getJobCodeList(UsersVO usersVo) {
		return selectList("usersSqlMap.getJobCodeList",usersVo);
	}
	public List<UsersVO> getOffiCodeList(UsersVO usersVo) {
		return selectList("usersSqlMap.getOffiCodeList",usersVo);
	}
	public int updatePasswd(UsersVO usersVo){
		int updatePasswd = update("usersSqlMap.updatePasswd", usersVo);
		return updatePasswd;
	}

	public List<UsersVO> searchDrnUser(UsersVO usersVo) {
		return selectList("usersSqlMap.searchDrnUser",usersVo);
	}
	
	public List<CodeInfoVO> getUserJobTypeList() {
		List<CodeInfoVO> getUserJobTypeList = selectList("usersSqlMap.getUserJobTypeList");
		return getUserJobTypeList;
	}
	
	public CodeInfoVO getJobMaxsetOrder() {
		CodeInfoVO getJobMaxsetOrder = selectOne("usersSqlMap.getJobMaxsetOrder");
		return getJobMaxsetOrder;
	}
	
	public CodeInfoVO getJobMaxCode() {
		CodeInfoVO getJobMaxCode = selectOne("usersSqlMap.getJobMaxCode");
		return getJobMaxCode;
	}
	
	public void insertJobCodeInfo(CodeInfoVO codeInfoVo) {
		insert("usersSqlMap.insertJobCodeInfo",codeInfoVo);
	}
	
	public void insertOffiCodeInfo(CodeInfoVO codeInfoVo) {
		insert("usersSqlMap.insertOffiCodeInfo",codeInfoVo);
	}
	
	public int getUseJobCodeYN(UsersVO usersVo) {
		int getUseJobCodeYN = selectOne("usersSqlMap.getUseJobCodeYN",usersVo);
		return getUseJobCodeYN;
	}
	public int getUseOffiCodeYN(UsersVO usersVo) {
		int getUseOffiCodeYN = selectOne("usersSqlMap.getUseOffiCodeYN",usersVo);
		return getUseOffiCodeYN;
	}
		
	public UsersVO searchUserWithCode(UsersVO usersVo) {
		return selectOne("usersSqlMap.searchUserWithCode",usersVo);
	}
		/**
		 * 1. 메소드명 : getDrnRegID
		 * 2. 작성일: 2022. 3. 27.
		 * 3. 작성자: doil
		 * 4. 설명: 
		 * 5. 수정일: doil
		 */
	public String getDrnRegID(String prj_id, String drn_id) {
		DrnLineVO vo = new DrnLineVO();
		vo.setPrj_id(prj_id);
		vo.setDrn_id(drn_id);
		
		
		return selectOne("drnSqlMap.getDrnRegID",vo);
	}
}
