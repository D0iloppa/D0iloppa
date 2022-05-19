package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjEmailTypeService extends AbstractService {
	public List<PrjEmailTypeVO> getPrjEmailTypeAll(PrjEmailTypeVO prjemailtypevo){
		List<PrjEmailTypeVO> getPrjEmailTypeAll = selectList("prjemailtypeSqlMap.getPrjEmailTypeAll",prjemailtypevo);
		return getPrjEmailTypeAll;
	}
	public List<PrjEmailTypeVO> getPrjEmailType(PrjEmailTypeVO prjemailtypevo){
		List<PrjEmailTypeVO> getPrjEmailType = selectList("prjemailtypeSqlMap.getPrjEmailType",prjemailtypevo);
		return getPrjEmailType;
	}
	public int insertPrjEmailType(PrjEmailTypeVO prjemailtypevo){
		int insertPrjEmailType = insert("prjemailtypeSqlMap.insertPrjEmailType",prjemailtypevo);
		return insertPrjEmailType;
	}
	public int deletePrjEmailType(PrjEmailTypeVO prjemailtypevo){
		int deletePrjEmailType = delete("prjemailtypeSqlMap.deletePrjEmailType",prjemailtypevo);
		return deletePrjEmailType;
	}
	public String getEmailAutoSendYN(PrjEmailTypeVO prjemailtypevo){
		String getEmailAutoSendYN = selectOne("prjemailtypeSqlMap.getEmailAutoSendYN",prjemailtypevo);
		return getEmailAutoSendYN;
	}
	public PrjEmailTypeVO selectEmailType(PrjEmailTypeVO prjemailtypevo){
		PrjEmailTypeVO selectEmailType = selectOne("prjemailtypeSqlMap.selectEmailType",prjemailtypevo);
		return selectEmailType;
	}
	public int updatePrjEmailType(PrjEmailTypeVO prjemailtypevo){
		int updatePrjEmailType = insert("prjemailtypeSqlMap.updatePrjEmailType",prjemailtypevo);
		return updatePrjEmailType;
	}
}
