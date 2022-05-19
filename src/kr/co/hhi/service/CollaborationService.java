package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.CollaborationVO;
import kr.co.hhi.model.PrjInfoVO;

@Service
public class CollaborationService extends AbstractService {
	
	//기안함 페이지
	public List<CollaborationVO> getCollaboList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboList = selectList("collaborationSqlMap.getCollaboList", collaborationVo);
		return getCollaboList;
	}
	
	public List<CollaborationVO> selectCollaboList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboList = selectList("collaborationSqlMap.selectCollaboList", collaborationVo);
		return selectCollaboList;
	}
	
	//작성중 페이지
	public List<CollaborationVO> getCollaboWriteList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboWriteList = selectList("collaborationSqlMap.getCollaboWriteList", collaborationVo);
		return getCollaboWriteList;
	}
	
	public List<CollaborationVO> selectCollaboWriteList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboWriteList = selectList("collaborationSqlMap.selectCollaboWriteList", collaborationVo);
		return selectCollaboWriteList;
	}
	
	//작성중 상세 페이지
	public CollaborationVO getCollaboWrite(CollaborationVO collaborationVo) {
		CollaborationVO getCollaboWrite = selectOne("collaborationSqlMap.getCollaboWrite", collaborationVo);
		return getCollaboWrite;
	}
	
	//미결 페이지
	public List<CollaborationVO> getCollaboPendingList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboPendingList = selectList("collaborationSqlMap.getCollaboPendingList", collaborationVo);
		return getCollaboPendingList;
	}
	
	public List<CollaborationVO> selectCollaboPendingList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboPendingList = selectList("collaborationSqlMap.selectCollaboPendingList", collaborationVo);
		return selectCollaboPendingList;
	}
	
	//미결 상세 페이지 
	public CollaborationVO getCollaboPending(CollaborationVO collaborationVo) {
		CollaborationVO getCollaboPending = selectOne("collaborationSqlMap.getCollaboPending", collaborationVo);
		return getCollaboPending;
	}
	
	// 예고 페이지
	public List<CollaborationVO> getCollaboPrevList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboPrevList = selectList("collaborationSqlMap.getCollaboPrevList", collaborationVo);
		return getCollaboPrevList;
	}
	
	public List<CollaborationVO> selectCollaboPrevList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboPrevList = selectList("collaborationSqlMap.selectCollaboPrevList", collaborationVo);
		return selectCollaboPrevList;
	}
	
	// 예고 상세페이지 
	public CollaborationVO getCollaboPrev(CollaborationVO collaborationVo) {
		CollaborationVO getCollaboPrev = selectOne("collaborationSqlMap.getCollaboPrev", collaborationVo);
		return getCollaboPrev;
	}
	
	// 진행 페이지
	public List<CollaborationVO> getCollaboProgList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboProgList = selectList("collaborationSqlMap.getCollaboProgList", collaborationVo);
		return getCollaboProgList;
	}
	
	public List<CollaborationVO> selectCollaboProgList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboProgList = selectList("collaborationSqlMap.selectCollaboProgList", collaborationVo);
		return selectCollaboProgList;
	}
	
	// 반려 페이지
	public List<CollaborationVO> getCollaboRejectList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboRejectList = selectList("collaborationSqlMap.getCollaboRejectList", collaborationVo);
		return getCollaboRejectList;
	}	
	
	public List<CollaborationVO> selectCollaboRejectList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboRejectList = selectList("collaborationSqlMap.selectCollaboRejectList", collaborationVo);
		return selectCollaboRejectList;
	}
	
	// 완료 페이지
	public List<CollaborationVO> getCollaboCompList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboCompList = selectList("collaborationSqlMap.getCollaboCompList", collaborationVo);
		return getCollaboCompList;
	}
	
	public List<CollaborationVO> selectCollaboCompList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboCompList = selectList("collaborationSqlMap.selectCollaboCompList", collaborationVo);
		return selectCollaboCompList;
	}
	
	// 전체현황 페이지
	public List<CollaborationVO> getCollaboWholeList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboWholeList = selectList("collaborationSqlMap.getCollaboWholeList", collaborationVo);
				
		return getCollaboWholeList;
	}
	
	public List<CollaborationVO> selectCollaboWholeList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboWholeList = selectList("collaborationSqlMap.selectCollaboWholeList", collaborationVo);
		return selectCollaboWholeList;
	}
	
	// 지연현황 페이지
	public List<CollaborationVO> getCollaboDelayedList(CollaborationVO collaborationVo) {
		List<CollaborationVO> getCollaboDelayedList = selectList("collaborationSqlMap.getCollaboDelayedList", collaborationVo);
		return getCollaboDelayedList;
	}
	
	// 지연현황 페이지
	public List<CollaborationVO> selectCollaboDelayedList(CollaborationVO collaborationVo) {
		List<CollaborationVO> selectCollaboDelayedList = selectList("collaborationSqlMap.selectCollaboDelayedList", collaborationVo);
		return selectCollaboDelayedList;
	}

	public List<PrjInfoVO> getAllPrjList() {
		return selectList("collaborationSqlMap.getAllPrjList");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
