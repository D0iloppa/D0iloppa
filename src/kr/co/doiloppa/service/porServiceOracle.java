package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.abstractinfo.AbstractService2;
import kr.co.doiloppa.model.CodeInfoVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class porServiceOracle extends AbstractService2 {

	/*
	 * 실서버 적용시 
	 * porSqlMapOracle.getPORInfoList
	 * 로컬서버 적용시
	 * porSqlMapOracle.getPORInfoList_test
	 */
	public List<PrjDocumentIndexVO> getPORInfoList(PrjInfoVO prjinfovo){
		List<PrjDocumentIndexVO> getPORInfoList = selectList("porSqlMapOracle.getPORInfoList",prjinfovo);
		return getPORInfoList;
	}
	/*
	 * 실서버 적용시 
	 * porSqlMapOracle.getPORInfo
	 * 로컬서버 적용시
	 * porSqlMapOracle.getPORInfo_test
	 */
	public PrjDocumentIndexVO getPORInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getPORInfo = selectOne("porSqlMapOracle.getPORInfo",prjdocumentindexvo);
		return getPORInfo;
	}
}
