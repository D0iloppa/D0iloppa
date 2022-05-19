package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.SecurityLogVO;

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
