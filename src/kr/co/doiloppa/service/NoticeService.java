package kr.co.doiloppa.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.common.util.FileUtil;
import kr.co.doiloppa.model.NoticeBoardVO;
import kr.co.doiloppa.model.ProjectNoticeVO;

@Service
public class NoticeService extends AbstractService {
	
	public NoticeBoardVO checkAdminYn(NoticeBoardVO noticeboardVo) {
		NoticeBoardVO checkAdminYn = selectOne("noticeSqlMap.checkAdminYn", noticeboardVo);
		return checkAdminYn;		
	}
	
	public NoticeBoardVO checkUserId(NoticeBoardVO noticeboardVo) {
		NoticeBoardVO checkUserId = selectOne("noticeSqlMap.checkUserId", noticeboardVo);
		return checkUserId;		
	}
	
	public List<NoticeBoardVO> getNoticeList() {
		List<NoticeBoardVO> getNoticeList = selectList("noticeSqlMap.getNoticeList");
		return getNoticeList;
	}
	
	public List<NoticeBoardVO> findNoticeList(NoticeBoardVO noticeboardVo) {
		List<NoticeBoardVO> findNoticeList = selectList("noticeSqlMap.findNoticeList", noticeboardVo);
		return findNoticeList;
	}
	
	public NoticeBoardVO insertNotice(NoticeBoardVO noticeboardVo) {
		NoticeBoardVO insertNotice = selectOne("noticeSqlMap.insertNotice", noticeboardVo);
		return insertNotice;
	}
	
	public int updateNotice(NoticeBoardVO noticeboardVo) {
		return update("noticeSqlMap.updateNotice", noticeboardVo);
	}
	
	public int deleteNotice(NoticeBoardVO noticeboardVo) {
		return delete("noticeSqlMap.deleteNotice", noticeboardVo);
	}
	
	public void updateHit(NoticeBoardVO noticeboardVo) {
		update("noticeSqlMap.updateHit", noticeboardVo);
	}
	
	public NoticeBoardVO getNoticeBoard(NoticeBoardVO noticeboardVo) {
		NoticeBoardVO getNoticeBoard = selectOne("noticeSqlMap.getNoticeBoard", noticeboardVo);
		return getNoticeBoard;		
	}
	
	public List<NoticeBoardVO> getNoticeFilelist(NoticeBoardVO noticeboardVo) {
		List<NoticeBoardVO> getNoticeFilelist = selectList("noticeSqlMap.getNoticeFilelist", noticeboardVo);
		return getNoticeFilelist;
	}
	
	public NoticeBoardVO getFilePathNotice(NoticeBoardVO noticeboardVo) {
		NoticeBoardVO getFilePathNotice = selectOne("noticeSqlMap.getFilePathNotice", noticeboardVo);
		return getFilePathNotice;
	}
	
	public List<Map<String, Object>> fileUploadNotice(NoticeBoardVO noticeboardVo, HttpServletRequest request) throws Exception {
		List<Map<String, Object>> List = FileUtil.parseInsertFileInfoNotice(Long.toString(noticeboardVo.getBbs_seq()),request);
				
		for(int i = 0; i < List.size(); i++) {
			insert("noticeSqlMap.fileUploadNotice", List.get(i));
		}
		return List;
	}
	
	public NoticeBoardVO getDeleteFile(NoticeBoardVO noticeboardVo) {
		NoticeBoardVO getDeleteFile = selectOne("noticeSqlMap.getDeleteFile", noticeboardVo);
		return getDeleteFile;
	}
	
	public void deleteFile(NoticeBoardVO noticeboardVo) {
		delete("noticeSqlMap.deleteFile", noticeboardVo);
	}
	
	public List<NoticeBoardVO> getDeleteFileList(NoticeBoardVO noticeboardVo) {
		List<NoticeBoardVO> getDeleteFileList = selectList("noticeSqlMap.getDeleteFileList", noticeboardVo);
		return getDeleteFileList;
	}
}
