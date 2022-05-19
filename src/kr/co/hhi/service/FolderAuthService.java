/**
 * 
 */
package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.FolderAuthVO;
import kr.co.hhi.model.FolderInfoVO;

/**
 * @author doil
 *
 */
@Service
public class FolderAuthService extends AbstractService {

	
	public String auth_getFolderPath(FolderAuthVO folderAuthVo) {
		// TODO Auto-generated method stub
		return selectOne("folderAuthSqlMap.auth_getFolderPath",folderAuthVo);
	}

	public List<FolderAuthVO> auth_getOrg(FolderAuthVO folderAuthVo) {
		return selectList("folderAuthSqlMap.auth_getOrg",folderAuthVo);
	}


	public List<FolderAuthVO> auth_getGr(FolderAuthVO folderAuthVo) {
		return selectList("folderAuthSqlMap.auth_getGr",folderAuthVo);
	}

	
	public List<FolderAuthVO> auth_getGrList(FolderAuthVO folderAuthVo) {
		return selectList("folderAuthSqlMap.auth_getGrList",folderAuthVo);
	}

	public List<FolderAuthVO> auth_getGrMember(FolderAuthVO folderAuthVo) {
		return selectList("folderAuthSqlMap.auth_getGrMember",folderAuthVo);
	}

	public List<FolderAuthVO> getAuthInfoList(FolderAuthVO folderAuthVo) {
		return selectList("folderAuthSqlMap.getAuthInfoList",folderAuthVo);
	}

	public FolderAuthVO getAuthInfo(FolderAuthVO folderAuthVo) {
		return selectOne("folderAuthSqlMap.getAuthInfo",folderAuthVo);
	}

	public int insertInfo(FolderAuthVO folderAuthVo) {
		long folder_id = folderAuthVo.getFolder_id();
		String auth_lvl1 = folderAuthVo.getAuth_lvl1();
		String folder_type = selectOne("folderinfoSqlMap.getFolderType",folderAuthVo);
		
		if(auth_lvl1.equals("P")) { // 개인에 대한 권한을 추가하는 경우,

			String isPrjMember = "N";
			int cnt = selectOne("folderAuthSqlMap.isPrjMember",folderAuthVo);
			if(cnt>0) isPrjMember = "Y";
			// 프로젝트 멤버가 아니라면, 해당 폴더의 프로세스 타입에 따라 권한 강제적용			
			if(isPrjMember.equals("N")) {
				// 프로세스 타입을 구함
				FolderInfoVO getProcessTypeVO = new FolderInfoVO();
				getProcessTypeVO.setFolder_id(folder_id);
				getProcessTypeVO.setPrj_id(folderAuthVo.getPrj_id());
				
				String folder_path = selectOne("folderinfoSqlMap.getFolderPath",getProcessTypeVO);
				getProcessTypeVO.setFolder_path(folder_path);
				
				String processType = selectOne("folderinfoSqlMap.getFolderProcess",getProcessTypeVO);
				
				if(processType == null) processType = "General";
				
				if(!processType.equals("General")) {
					folderAuthVo.setWrite_auth_yn("N");
					folderAuthVo.setDelete_auth_yn("N");
				}
				// Project Notice Board 폴더는 프로젝트 멤버가 아니면 쓰기/삭제 불가
				if(folder_type.equals("PBO")) {
					folderAuthVo.setWrite_auth_yn("N");
					folderAuthVo.setDelete_auth_yn("N");
				}
			}
		}
		
		return insert("folderAuthSqlMap.insertInfo",folderAuthVo);
	}

	public int updateInfo(FolderAuthVO folderAuthVo) {
		return update("folderAuthSqlMap.updateInfo",folderAuthVo);
	}

	public int deleteInfo(FolderAuthVO folderAuthVo, int first) {
		if(first == 0)  // 최상단 폴더의 권한정보 삭제
			return delete("folderAuthSqlMap.deleteInfo",folderAuthVo);
		
		else // 부모폴더의 권한을 계승한 권한만 삭제
			return delete("folderAuthSqlMap.deleteInfoInherit",folderAuthVo);
		
		
	}

	public String getOrgNm(String org_id) {
		return selectOne("folderAuthSqlMap.getOrgNm",Long.parseLong(org_id));
	}


	public String getGrpNm(String group_id) {
		return selectOne("folderAuthSqlMap.getGrpNm",group_id);
	}

	public List<FolderAuthVO> getDefaultAuth(FolderAuthVO folderAuthVo) {

		return selectList("folderAuthSqlMap.getDefaultAuth",folderAuthVo);
	}

	public FolderAuthVO getAuthMappingVal(FolderAuthVO auth) {
		String auth_lvl1 = auth.getAuth_lvl1();
		
		FolderAuthVO result = null;
		
		switch(auth_lvl1) {
			case "O" : // 조직명 찾아오기
				Long org_id = Long.parseLong(auth.getAuth_val());
				auth.setOrg_id(org_id);
				result = selectOne("folderAuthSqlMap.getOrgInfo",auth);
				auth.set_auth_val(result.getOrg_nm());
				break;
			case "G" : // 그룹id가 group label	
				result = selectOne("folderAuthSqlMap.getGrpInfo",auth);
				auth.set_auth_val(result.getGroup_name());
				break;
			case "P" :
				String user_id = auth.getAuth_val();
				auth.setUser_id(user_id);
				result = selectOne("folderAuthSqlMap.getUserInfo",auth);
				auth.set_auth_val(result.getUser_kor_nm());
				break;
		}
		
		return auth;
	}

	public List<Long> getSubFolderList(FolderAuthVO folderAuthVo) {
		String getFolderPath = selectOne("folderAuthSqlMap.getFolderPath",folderAuthVo);
		
		folderAuthVo.set_auth_val(getFolderPath+"%");
		
		return selectList("folderAuthSqlMap.getSubFolderList",folderAuthVo);
	}

	
	public FolderAuthVO getFolderAuth(FolderAuthVO folderAuthVo) {
		// TODO Auto-generated method stub
		return selectOne("folderAuthSqlMap.getFolderAuth",folderAuthVo);
	}

	
	public int insertFolderAuth(FolderAuthVO folderAuthVo) {
		return insert("folderAuthSqlMap.insertFolderAuth",folderAuthVo);
	}

	public int updateFolderAuth(FolderAuthVO folderAuthVo) {
		String auth_add = folderAuthVo.getFolder_auth_add_yn();
		
		if(auth_add.equals("N")) { // 이 폴더 권한을 추가 해제
			// 해당 폴더에 추가된 권한들을 삭제 (폴더 자체의 권한 제외)
			delete("folderAuthSqlMap.delAuthAdd",folderAuthVo);
		}
		
		
		return update("folderAuthSqlMap.updateFolderAuth",folderAuthVo);
	}

	public List<FolderAuthVO> memberSearch(FolderAuthVO folderAuthVo) {
		String key = folderAuthVo.getAuth_val();
		key = "%" + key + "%";
		// String condition = folderAuthVo.getAuth_lvl1();
		folderAuthVo.setAuth_val(key);
		
		return selectList("folderAuthSqlMap.memberSearch",folderAuthVo);
		/*
		if(condition.equals("user_id"))
			return selectList("folderAuthSqlMap.memberSearchId",key);
		else
			return selectList("folderAuthSqlMap.memberSearchNm",key);
		*/
	}


	public int clearAuth(FolderAuthVO folderAuthVo) {

		delete("folderAuthSqlMap.clearAuth",folderAuthVo);
		
		
		return 0;
		
	}


	


}
