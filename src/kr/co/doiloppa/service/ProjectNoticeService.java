package kr.co.doiloppa.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.util.FileUtil;
import kr.co.doiloppa.model.ProjectNoticeVO;

@Service
public class ProjectNoticeService extends AbstractService {
	
//	public List<ProjectNoticeVO> getPrjName(ProjectNoticeVO projectnoticeVo) {
//		List<ProjectNoticeVO> getPrjName = selectOne("projectNoticeSqlMap.getPrjName", projectnoticeVo);
//		return getPrjName;
//	}
	
	public ProjectNoticeVO checkPrjUserId(ProjectNoticeVO projectnoticeVo) {
		ProjectNoticeVO checkPrjUserId = selectOne("projectNoticeSqlMap.checkPrjUserId", projectnoticeVo);
		return checkPrjUserId;		
	}
	
	public List<ProjectNoticeVO> getProjectNoticeList(ProjectNoticeVO projectnoticeVo) {
		List<ProjectNoticeVO> getProjectNoticeList = selectList("projectNoticeSqlMap.getProjectNoticeList", projectnoticeVo);
		return getProjectNoticeList;
	}
	
	public List<ProjectNoticeVO> findNoticeList(ProjectNoticeVO projectnoticeVo) {
		List<ProjectNoticeVO> findNoticeList = selectList("projectNoticeSqlMap.findNoticeList", projectnoticeVo);
		return findNoticeList;
	}
	
	public ProjectNoticeVO getPrjNoticeBoard(ProjectNoticeVO projectnoticeVo) {
		ProjectNoticeVO getPrjNoticeBoard = selectOne("projectNoticeSqlMap.getPrjNoticeBoard", projectnoticeVo);
		return getPrjNoticeBoard;
	}
	
	public ProjectNoticeVO insertPrjNotice(ProjectNoticeVO projectnoticeVo) {
		ProjectNoticeVO insertPrjNotice = selectOne("projectNoticeSqlMap.insertPrjNotice", projectnoticeVo);
		return insertPrjNotice;
	}
	
	public List<ProjectNoticeVO> getPrjNoticeFileList(ProjectNoticeVO projectnoticeVo) {
		List<ProjectNoticeVO> getPrjNoticeFileList = selectList("projectNoticeSqlMap.getPrjNoticeFileList", projectnoticeVo);
		return getPrjNoticeFileList;
	}
	
	public ProjectNoticeVO getFilePath(ProjectNoticeVO projectnoticeVo) {
		ProjectNoticeVO getFilePath = selectOne("projectNoticeSqlMap.getFilePath", projectnoticeVo);
		return getFilePath;
	}
	
	public ProjectNoticeVO getDeleteFile(ProjectNoticeVO projectnoticeVo) {
		ProjectNoticeVO getDeleteFile = selectOne("projectNoticeSqlMap.getDeleteFile", projectnoticeVo);
		return getDeleteFile;
	}
	
	public List<ProjectNoticeVO> getDeleteFileList(ProjectNoticeVO projectnoticeVo) {
		List<ProjectNoticeVO> getDeleteFileList = selectList("projectNoticeSqlMap.getDeleteFileList", projectnoticeVo);
		return getDeleteFileList;
	}
	
	public void hitcntUp(ProjectNoticeVO projectnoticeVo) {
		update("projectNoticeSqlMap.hitcntUp", projectnoticeVo);
	}
	
	public int updatePrjNotice(ProjectNoticeVO projectnoticeVo) {
		return update("projectNoticeSqlMap.updatePrjNotice", projectnoticeVo);
	}
	
	public int deletePrjNotice(ProjectNoticeVO projectnoticeVo) {
		return delete("projectNoticeSqlMap.deletePrjNotice", projectnoticeVo);
	}
	
	public List<Map<String, Object>> fileUploadPrjNotice(ProjectNoticeVO projectnoticeVo, HttpServletRequest request) throws Exception {
		List<Map<String, Object>> List = FileUtil.parseInsertFileInfo((Long.toString(projectnoticeVo.getBbs_seq())+","+projectnoticeVo.getPrj_id()),request);
		
		for(int i = 0; i < List.size(); i++) {
			insert("projectNoticeSqlMap.fileUploadPrjNotice", List.get(i));
		}
		return List;
	}
	
	public void deleteFile(ProjectNoticeVO projectnoticeVo, HttpServletRequest request) {
		delete("projectNoticeSqlMap.deleteFile", projectnoticeVo);
	}
	
	public void deleteFileList(ProjectNoticeVO projectnoticeVo, HttpServletRequest request) {
		delete("projectNoticeSqlMap.deleteFile", projectnoticeVo);
	}
	
}
