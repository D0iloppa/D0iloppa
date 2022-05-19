package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjSystemPrefixVO;
import kr.co.hhi.model.UsersVO;

@Service
public class PrjSystemPrefixService extends AbstractService {
	public List<PrjSystemPrefixVO> getPrjSystemPrefix(PrjSystemPrefixVO prjsystemprefixvo){
		List<PrjSystemPrefixVO> getPrjSystemPrefix = selectList("prjsystemprefixSqlMap.getPrjSystemPrefix",prjsystemprefixvo);
		return getPrjSystemPrefix;
	}
}
