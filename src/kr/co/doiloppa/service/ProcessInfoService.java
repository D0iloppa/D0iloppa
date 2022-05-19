package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class ProcessInfoService extends AbstractService {
	public List<ProcessInfoVO> getProcessTypeByFolderPath(ProcessInfoVO processinfovo){
		List<ProcessInfoVO> getProcessTypeByFolderPath = selectList("processinfoSqlMap.getProcessTypeByFolderPath", processinfovo);
		return getProcessTypeByFolderPath;
	}
	public ProcessInfoVO getINOUTMatchingFolderId(ProcessInfoVO processinfovo){
		ProcessInfoVO getINOUTMatchingFolderId = selectOne("processinfoSqlMap.getINOUTMatchingFolderId", processinfovo);
		return getINOUTMatchingFolderId;
	}
	public ProcessInfoVO getDRNUseYNByDocType(ProcessInfoVO processinfovo){
		ProcessInfoVO getDRNUseYNByDocType = selectOne("processinfoSqlMap.getDRNUseYNByDocType", processinfovo);
		return getDRNUseYNByDocType;
	}
	public ProcessInfoVO getFolderIsSYSTEMClass(ProcessInfoVO processinfovo){
		ProcessInfoVO getFolderIsSYSTEMClass = selectOne("processinfoSqlMap.getFolderIsSYSTEMClass", processinfovo);
		return getFolderIsSYSTEMClass;
	}
	public ProcessInfoVO getSYSTEMProcessFolderId(ProcessInfoVO processinfovo){
		ProcessInfoVO getSYSTEMProcessFolderId = selectOne("processinfoSqlMap.getSYSTEMProcessFolderId", processinfovo);
		return getSYSTEMProcessFolderId;
	}
	public int isCheckUseFolderId(ProcessInfoVO processinfovo){
		int isCheckUseFolderId = selectOne("processinfoSqlMap.isCheckUseFolderId", processinfovo);
		return isCheckUseFolderId;
	}
}
