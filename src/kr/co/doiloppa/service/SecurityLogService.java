package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.SecurityLogVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class SecurityLogService extends AbstractService {
	public UsersVO getLoginUser(UsersVO usersVo) {
		UsersVO getLoginUser = selectOne("securityLogSqlMap.getLoginUser",usersVo);
		return getLoginUser;
	}
	
	public SecurityLogVO getCheckSystemLog(SecurityLogVO securityLogVo) {
		SecurityLogVO getCheckSystemLog = selectOne("securityLogSqlMap.getCheckSystemLog",securityLogVo);
		return getCheckSystemLog;
	}
	
	public SecurityLogVO getFolder_nm(SecurityLogVO securityLogVo) {
		SecurityLogVO getFolder_nm = selectOne("securityLogSqlMap.getFolder_nm",securityLogVo);
		return getFolder_nm;
	}
	
	public List<SecurityLogVO> getSystemLogList(SecurityLogVO securityLogVo) {
		List<SecurityLogVO> getSystemLogList = selectList("securityLogSqlMap.getSystemLogList",securityLogVo);
		return getSystemLogList;
	}
	
	public void insertSystemLog(SecurityLogVO securityLogVo) {
		insert("securityLogSqlMap.insertSystemLog", securityLogVo);
	}
	
	public List<SecurityLogVO> searchSystemLogList(SecurityLogVO securityLogVo) {
		List<SecurityLogVO> searchSystemLogList = selectList("securityLogSqlMap.searchSystemLogList",securityLogVo);
		return searchSystemLogList;
	}
	
	public List<SecurityLogVO> getDocumentLogList(SecurityLogVO securityLogVo) {
		List<SecurityLogVO> getDocumentLogList = selectList("securityLogSqlMap.getDocumentLogList",securityLogVo);
		return getDocumentLogList;
	}
}
