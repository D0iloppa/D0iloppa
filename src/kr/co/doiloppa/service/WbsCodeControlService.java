package kr.co.doiloppa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.WbsCodeControlVO;

@Service
public class WbsCodeControlService extends AbstractService {
	
	public List<WbsCodeControlVO> getWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		String doc_type = wbscodecontrolVo.getDoc_type();
		List<WbsCodeControlVO> getWbscodeList = new ArrayList<>();
		
		if(doc_type.equals("A"))
			getWbscodeList = selectList("wbscodecontrolSqlMap.getWbscodeListAll", wbscodecontrolVo);
		else
			getWbscodeList = selectList("wbscodecontrolSqlMap.getWbscodeList", wbscodecontrolVo);
		
		for(int i=0; i<getWbscodeList.size(); i++) {
			if(getWbscodeList.get(i).getDoc_type() == null) {
				wbscodecontrolVo.setDoc_type("");
			}
		}
		
		String discip_code_id = wbscodecontrolVo.getDiscip_code_id();
		String doc_type_chk = wbscodecontrolVo.getDoc_type();
		if(!discip_code_id.equals("all") && !doc_type_chk.equals(""))
			getWbscodeList = getWbscodeList.stream().filter(i->i.getDiscip_code_id().equals(discip_code_id)).collect(Collectors.toList());
		
		
		return getWbscodeList;
	}
	
	public void updateWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		update("wbscodecontrolSqlMap.updateWbscodeList", wbscodecontrolVo);
	}
	
	public int deleteWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		return delete("wbscodecontrolSqlMap.deleteWbscodeList", wbscodecontrolVo);
	}
	
	public List<WbsCodeControlVO> searchWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		List<WbsCodeControlVO> searchWbscodeList = selectList("wbscodecontrolSqlMap.searchWbscodeList", wbscodecontrolVo);
		return searchWbscodeList;
	}

	public List<Long> getDiscipFolder(WbsCodeControlVO wbscodecontrolVo) {
		return selectList("wbscodecontrolSqlMap.getDiscipFolder",wbscodecontrolVo);	
	}


	public int checkFolder(PrjDisciplineVO discipBean) {
		// TODO Auto-generated method stub
		return selectOne("wbscodecontrolSqlMap.checkFolder",discipBean);
	}


	public long getWBSMaxId(WbsCodeControlVO wbscodecontrolVo) {
		// TODO Auto-generated method stub
		return selectOne("wbscodecontrolSqlMap.getWBSMaxId",wbscodecontrolVo);
	}


	public int updateWBS(WbsCodeControlVO wbscodecontrolVo) {
		return update("wbscodecontrolSqlMap.updateWBS",wbscodecontrolVo);
	}
	
	public int insertWBS(WbsCodeControlVO wbscodecontrolVo) {
		return insert("wbscodecontrolSqlMap.insertWBS",wbscodecontrolVo);
	}
}
