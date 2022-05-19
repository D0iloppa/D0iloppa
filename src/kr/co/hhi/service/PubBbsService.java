package kr.co.hhi.service;

import java.util.List;
import org.springframework.stereotype.Service;
import  kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.PubBbsContentsVO;

@Service
public class PubBbsService extends AbstractService {
	public List<PubBbsContentsVO> getPubBbsContents(){
		List<PubBbsContentsVO> getPubBbsContents = selectList("pubbbsSqlMap.getPubBbsContents");
		return getPubBbsContents;
	}
	
	public PubBbsContentsVO getPubBbsContentOne(PubBbsContentsVO pubBbsContentsVo){
		PubBbsContentsVO getPubBbsContentOne = selectOne("pubbbsSqlMap.getPubBbsContentOne", pubBbsContentsVo);
		return getPubBbsContentOne;
	}
	
	public void updateHit(PubBbsContentsVO pubBbsContentsVo) {
		update("noticeSqlMap.updateHit", pubBbsContentsVo);
	}
}
