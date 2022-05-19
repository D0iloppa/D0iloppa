package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjGroupMemberVO;
import kr.co.doiloppa.model.PrjMemberVO;

@Service
public class OrganizationService extends AbstractService {
	public List<OrganizationVO> getOrganizationList(OrganizationVO organizationvo){
		List<OrganizationVO> getOrganizationList = selectList("organizationSqlMap.getOrganizationList", organizationvo);
		return getOrganizationList;
	}
	public List<OrganizationVO> getOrganizationListOnlyHyundae(OrganizationVO organizationvo){
		List<OrganizationVO> getOrganizationListOnlyHyundae = selectList("organizationSqlMap.getOrganizationListOnlyHyundae", organizationvo);
		return getOrganizationListOnlyHyundae;
	}
	public List<OrganizationVO> getOrganizationListCorA(OrganizationVO organizationvo){
		List<OrganizationVO> getOrganizationListCorA = selectList("organizationSqlMap.getOrganizationListCorA", organizationvo);
		return getOrganizationListCorA;
	}
	public OrganizationVO getLastOrgId(OrganizationVO organizationvo) {
		OrganizationVO getLastOrgId = selectOne("organizationSqlMap.getLastOrgId", organizationvo);
		return getLastOrgId;
	}
	public OrganizationVO getParentOrgPath(OrganizationVO organizationvo){
		OrganizationVO getParentOrgPath = selectOne("organizationSqlMap.getParentOrgPath", organizationvo);
		return getParentOrgPath;
	}
	public void insertOrganizationVO(OrganizationVO organizationvo){
		insert("organizationSqlMap.insertOrganizationVO", organizationvo);
	}
	public void updateOrgNm(OrganizationVO organizationvo){
		update("organizationSqlMap.updateOrgNm", organizationvo);
	}
	public void deleteOrg(OrganizationVO organizationvo){
		delete("organizationSqlMap.deleteOrg", organizationvo);
	}
	public int updateOrgParentIdByJstreeDND(OrganizationVO organizationvo){
		int updateOrgParentIdByJstreeDND = update("organizationSqlMap.updateOrgParentIdByJstreeDND", organizationvo);
		return updateOrgParentIdByJstreeDND;
	}
	public int updateOrgPathByJstreeDND(OrganizationVO organizationvo){
		int updateOrgPathByJstreeDND = update("organizationSqlMap.updateOrgPathByJstreeDND", organizationvo);
		return updateOrgPathByJstreeDND;
	}
	public int updateGrandsonOrgPathByJstreeDND(OrganizationVO organizationvo){
		int updateGrandsonOrgPathByJstreeDND = update("organizationSqlMap.updateGrandsonOrgPathByJstreeDND", organizationvo);
		return updateGrandsonOrgPathByJstreeDND;
	}
	public OrganizationVO getOrgGrpNmPath(OrganizationVO organizationvo) {
		OrganizationVO getOrgGrpNmPath = selectOne("organizationSqlMap.getOrgGrpNmPath", organizationvo);
		return getOrgGrpNmPath;
	}
	public List<OrganizationVO> getDownOrg(OrganizationVO organizationvo) {
		List<OrganizationVO> getDownOrg = selectList("organizationSqlMap.getDownOrg", organizationvo);
		return getDownOrg;
	}
	public int getIsUseRootOrg(OrganizationVO organizationvo) {
		int getIsUseRootOrg = selectOne("organizationSqlMap.getIsUseRootOrg", organizationvo);
		return getIsUseRootOrg;
	}
}
