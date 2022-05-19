package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import  kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjGroupMemberVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.PrjWbsCodeVO;
import kr.co.hhi.model.UsersVO;

@Service
public class PrjWbsCodeService extends AbstractService {
	public List<PrjWbsCodeVO> getWbsCodeList(PrjWbsCodeVO prjwbscodevo){
		List<PrjWbsCodeVO> getWbsCodeList = selectList("prjwbscodeSqlMap.getWbsCodeList", prjwbscodevo);
		return getWbsCodeList;
	}
}


