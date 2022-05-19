package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjDrnInfoVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjDrnInfoService extends AbstractService {
	public int updateDRNStatusRByIssueCancel(PrjDrnInfoVO prjdrninfovo){
		int updateDRNStatusRByIssueCancel = update("prjdrninfoSqlMap.updateDRNStatusRByIssueCancel",prjdrninfovo);
		return updateDRNStatusRByIssueCancel;
	}
}
