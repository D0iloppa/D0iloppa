package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjDrnInfoHistVO;
import kr.co.hhi.model.PrjDrnInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.UsersVO;

@Service
public class PrjDrnInfoHistService extends AbstractService {
	public List<PrjDrnInfoHistVO> getPrjDrnHistory(PrjDrnInfoHistVO prjdrninfohistvo){
		List<PrjDrnInfoHistVO> getPrjDrnHistory = selectList("prjdrninfohistSqlMap.getPrjDrnHistory", prjdrninfohistvo);
		return getPrjDrnHistory;
	}
	public List<PrjDrnInfoHistVO> getPrjDrnHistoryData(PrjDrnInfoHistVO prjdrninfohistvo){
		List<PrjDrnInfoHistVO> getPrjDrnHistoryData = selectList("prjdrninfohistSqlMap.getPrjDrnHistoryData", prjdrninfohistvo);
		return getPrjDrnHistoryData;
	}
	public int updateDrnHistoryByIssueCancel(PrjDrnInfoHistVO prjdrninfohistvo){
		int updateDrnHistoryByIssueCancel = insert("prjdrninfohistSqlMap.updateDrnHistoryByIssueCancel", prjdrninfohistvo);
		return updateDrnHistoryByIssueCancel;
	}
}
