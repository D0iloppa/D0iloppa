package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjSystemPrefixVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjSystemPrefixService extends AbstractService {
	public List<PrjSystemPrefixVO> getPrjSystemPrefix(PrjSystemPrefixVO prjsystemprefixvo){
		List<PrjSystemPrefixVO> getPrjSystemPrefix = selectList("prjsystemprefixSqlMap.getPrjSystemPrefix",prjsystemprefixvo);
		return getPrjSystemPrefix;
	}
}
