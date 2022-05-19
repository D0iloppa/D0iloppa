package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PrjGroupMemberVO;
import kr.co.doiloppa.model.PrjMemberVO;

@Service
public class PrjGroupMemberService extends AbstractService {
	public List<PrjGroupMemberVO> getPrjGroupMemberList(PrjGroupMemberVO prjgroupmembervo){
		List<PrjGroupMemberVO> getPrjGroupMemberList = selectList("prjGroupMemberSqlMap.getPrjGroupMemberList", prjgroupmembervo);
		return getPrjGroupMemberList;
	}
	public int isUserPrjGroupMember(PrjGroupMemberVO prjgroupmembervo){
		int isUserPrjGroupMember = selectOne("prjGroupMemberSqlMap.isUserPrjGroupMember", prjgroupmembervo);
		return isUserPrjGroupMember;
	}
	public void deletePrjGroupMember(PrjGroupMemberVO prjgroupmembervo){
		delete("prjGroupMemberSqlMap.deletePrjGroupMember",prjgroupmembervo);
	}
	public void deletePrjGroupMemberAll(PrjGroupMemberVO prjgroupmembervo){
		delete("prjGroupMemberSqlMap.deletePrjGroupMemberAll",prjgroupmembervo);
	}
	public void insertPrjGroupMember(PrjGroupMemberVO prjgroupmembervo){
		insert("prjGroupMemberSqlMap.insertPrjGroupMember",prjgroupmembervo);
	}
}
