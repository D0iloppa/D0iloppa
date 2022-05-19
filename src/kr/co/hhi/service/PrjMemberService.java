package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.PrjMemberVO;

@Service
public class PrjMemberService extends AbstractService {
	public List<PrjMemberVO> getPrjMemberList(PrjMemberVO prjmembervo){
		List<PrjMemberVO> getPrjMemberList = selectList("prjMemberSqlMap.getPrjMemberList", prjmembervo);
		return getPrjMemberList;
	}
	public PrjMemberVO isUserPrjMember(PrjMemberVO prjmembervo){
		PrjMemberVO isUserPrjMember = selectOne("prjMemberSqlMap.isUserPrjMember", prjmembervo);
		return isUserPrjMember;
	}
	public void insertPrjMember(PrjMemberVO prjmembervo){
		insert("prjMemberSqlMap.insertPrjMember", prjmembervo);
	}
	public void deletePrjMember(PrjMemberVO prjmembervo){
		delete("prjMemberSqlMap.deletePrjMember", prjmembervo);
	}
	public void deleteCopyPrjMember(PrjMemberVO prjmembervo){
		delete("prjMemberSqlMap.deleteCopyPrjMember", prjmembervo);
	}
	public void insertCopyPrjMember(PrjMemberVO prjmembervo){
		insert("prjMemberSqlMap.insertCopyPrjMember", prjmembervo);
	}
}
