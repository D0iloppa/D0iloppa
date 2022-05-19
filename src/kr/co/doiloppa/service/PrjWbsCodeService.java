package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjFolderInfoVO;
import kr.co.doiloppa.model.PrjGroupMemberVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.PrjWbsCodeVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjWbsCodeService extends AbstractService {
	public List<PrjWbsCodeVO> getWbsCodeList(PrjWbsCodeVO prjwbscodevo){
		List<PrjWbsCodeVO> getWbsCodeList = selectList("prjwbscodeSqlMap.getWbsCodeList", prjwbscodevo);
		return getWbsCodeList;
	}
}


