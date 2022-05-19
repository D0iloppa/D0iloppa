package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.PrjCodeSettingsVO;

@Service
public class DrnReviewConclusionService extends AbstractService {
	public List<PrjCodeSettingsVO> drnRgetCodeList(PrjCodeSettingsVO prjCodeSettingsVo) {
		List<PrjCodeSettingsVO> drnR_getCode = selectList("drnReviewConclusionSqlMap.drnRgetCodeList", prjCodeSettingsVo);
		return drnR_getCode;
	}
	
	public PrjCodeSettingsVO drnRgetCode(PrjCodeSettingsVO prjCodeSettingsVo) {
		PrjCodeSettingsVO drnRgetCode = selectOne("drnReviewConclusionSqlMap.drnRgetCode", prjCodeSettingsVo);
		return drnRgetCode;
	}
	
	public void drnRgetCodeInsert(PrjCodeSettingsVO prjCodeSettingsVo) {
		insert("drnReviewConclusionSqlMap.drnRgetCodeInsert", prjCodeSettingsVo);
	}
	
	public void drnRgetCodeUp(PrjCodeSettingsVO prjCodeSettingsVo) {
		update("drnReviewConclusionSqlMap.drnRgetCodeUp", prjCodeSettingsVo);
	}
	
	public void drnRCopyCode(PrjCodeSettingsVO prjCodeSettingsVo) {
		insert("drnReviewConclusionSqlMap.drnRCopyCode", prjCodeSettingsVo);
	}
	
	public PrjCodeSettingsVO drnRchkDesc(PrjCodeSettingsVO prjCodeSettingsVo) {
		PrjCodeSettingsVO drnRchkDescVo = selectOne("drnReviewConclusionSqlMap.drnRchkDesc", prjCodeSettingsVo);
		return drnRchkDescVo;
	}
}
