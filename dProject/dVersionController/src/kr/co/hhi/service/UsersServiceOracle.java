package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.common.abstractinfo.AbstractService2;
import kr.co.hhi.model.CodeInfoVO;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.UsersVO;

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
