package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjDrnInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.UsersVO;

@Service
public class PrjDrnInfoService extends AbstractService {
	public int updateDRNStatusRByIssueCancel(PrjDrnInfoVO prjdrninfovo){
		int updateDRNStatusRByIssueCancel = update("prjdrninfoSqlMap.updateDRNStatusRByIssueCancel",prjdrninfovo);
		return updateDRNStatusRByIssueCancel;
	}
}
