package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjDrnInfoHistVO;
import kr.co.doiloppa.model.PrjDrnInfoVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.UsersVO;

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
