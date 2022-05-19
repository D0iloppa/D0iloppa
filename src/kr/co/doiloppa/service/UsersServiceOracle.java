package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.abstractinfo.AbstractService2;
import kr.co.doiloppa.model.CodeInfoVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class UsersServiceOracle extends AbstractService2 {
	
	/*
	 * 실서버 적용시 
	 * usersSqlMapOracle.getHRInformation
	 * 로컬서버 적용시
	 * usersSqlMapOracle.getHRInformation_test
	 */
	public List<UsersVO> getHRInformation(){
		List<UsersVO> getHRInformation = selectList("usersSqlMapOracle.getHRInformation");
		return getHRInformation;
	}
}
