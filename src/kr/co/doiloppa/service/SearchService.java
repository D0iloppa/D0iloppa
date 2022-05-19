package kr.co.doiloppa.service;

/**
 * @author doil
 *
 */

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.DocumentIndexVO;
import kr.co.doiloppa.model.GlobalSearchVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;

@Service
public class SearchService extends AbstractService {
	
	public List<GlobalSearchVO> getDocSearchList(GlobalSearchVO globalSearchVO){
		List<GlobalSearchVO> result = selectList("searchSqlMap.getDocSearchList_new2",globalSearchVO);
		return result;		
	}
	public List<GlobalSearchVO> getTRSearchList(GlobalSearchVO globalSearchVO){
		List<GlobalSearchVO> result = selectList("searchSqlMap.getTRSearchList",globalSearchVO);
		return result;		
	}
	public List<GlobalSearchVO> getDRNSearchList(GlobalSearchVO globalSearchVO){
		List<GlobalSearchVO> result = selectList("searchSqlMap.getDRNSearchList",globalSearchVO);
		return result;		
	}

	public List<GlobalSearchVO> getVTRSearchList(GlobalSearchVO glovalSearchVO) {
		return selectList("searchSqlMap.getVTRSearchList",glovalSearchVO);
	}

	public List<GlobalSearchVO> getSTRSearchList(GlobalSearchVO glovalSearchVO) {
		return selectList("searchSqlMap.getSTRSearchList",glovalSearchVO);
	}
	
	public PrjDocumentIndexVO getDocInfo(GlobalSearchVO vo) {
		return selectOne("searchSqlMap.getDocInfo",vo);
	}
}
