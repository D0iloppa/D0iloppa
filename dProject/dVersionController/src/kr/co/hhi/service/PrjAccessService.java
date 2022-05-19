package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.SecurityLogVO;

@Service
public class PrjAccessService extends AbstractService{
	public int insertAccHis(SecurityLogVO securityLogVo) {
		return insert("prjAccessSqlMap.insertAccHis", securityLogVo);
	}
	
	public SecurityLogVO getAccPrjNoNm(SecurityLogVO securityLogVo) {
		SecurityLogVO getAccPrjNoNm = selectOne("prjAccessSqlMap.getAccPrjNoNm", securityLogVo);
		return getAccPrjNoNm;
	}
	
	public SecurityLogVO getAccFolderPath(SecurityLogVO securityLogVo) {
		SecurityLogVO getAccFolderPath = selectOne("prjAccessSqlMap.getAccFolderPath", securityLogVo);
		return getAccFolderPath;
	}
	
	public List<SecurityLogVO> getAccHisList(SecurityLogVO securityLogVo) {
		List<SecurityLogVO> getAccHisList = selectList("prjAccessSqlMap.getAccHisList", securityLogVo);
		return getAccHisList;
	}
}
