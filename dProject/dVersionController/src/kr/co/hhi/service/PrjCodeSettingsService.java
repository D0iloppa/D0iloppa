package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import  kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDisciplineVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjGroupMemberVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.UsersVO;

@Service
public class PrjCodeSettingsService extends AbstractService {
	public int isUseStep(PrjCodeSettingsVO prjcodesettingsvo){
		int isUseStep = selectOne("prjcodesettingsSqlMap.isUseStep", prjcodesettingsvo);
		return isUseStep;
	}
	public int isUseRevNo(PrjCodeSettingsVO prjcodesettingsvo){
		int isUseRevNo = selectOne("prjcodesettingsSqlMap.isUseRevNo", prjcodesettingsvo);
		return isUseRevNo;
	}
	public List<PrjCodeSettingsVO> getSTEPPrjCodeSettingsList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getSTEPPrjCodeSettingsList = selectList("prjcodesettingsSqlMap.getSTEPPrjCodeSettingsList", prjcodesettingsvo);
		return getSTEPPrjCodeSettingsList;
	}
	public int insertSTEPPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo){
		int insertSTEPPrjCodeSettings = insert("prjcodesettingsSqlMap.insertSTEPPrjCodeSettings", prjcodesettingsvo);
		return insertSTEPPrjCodeSettings;
	}
	public int updateSTEPPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		int updateSTEPPrjCodeSettings = update("prjcodesettingsSqlMap.updateSTEPPrjCodeSettings", prjcodesettingsvo);
		return updateSTEPPrjCodeSettings;
	}
	public void deleteSTEPPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.deleteSTEPPrjCodeSettings", prjcodesettingsvo);
	}
	public void deleteCopyPrjSTEPCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.deleteCopyPrjSTEPCodeSettings", prjcodesettingsvo);
	}
	public void insertCopyPrjSTEPCodeSettings(PrjCodeSettingsVO prjcodesettingsvo){
		insert("prjcodesettingsSqlMap.insertCopyPrjSTEPCodeSettings", prjcodesettingsvo);
	}
	public List<PrjCodeSettingsVO> getIFCPrjCodeSettingsList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getIFCPrjCodeSettingsList = selectList("prjcodesettingsSqlMap.getIFCPrjCodeSettingsList", prjcodesettingsvo);
		return getIFCPrjCodeSettingsList;
	}
	public List<PrjCodeSettingsVO> getITEMPrjCodeSettingsList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getITEMPrjCodeSettingsList = selectList("prjcodesettingsSqlMap.getITEMPrjCodeSettingsList", prjcodesettingsvo);
		return getITEMPrjCodeSettingsList;
	}
	public void insertITEMPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo){
		insert("prjcodesettingsSqlMap.insertITEMPrjCodeSettings", prjcodesettingsvo);
	}
	public void updateITEMPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		update("prjcodesettingsSqlMap.updateITEMPrjCodeSettings", prjcodesettingsvo);
	}
	public void deleteITEMPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.deleteITEMPrjCodeSettings", prjcodesettingsvo);
	}
	public void insertIFCPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo){
		insert("prjcodesettingsSqlMap.insertIFCPrjCodeSettings", prjcodesettingsvo);
	}
	public void deleteIFCPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.deleteIFCPrjCodeSettings", prjcodesettingsvo);
	}
	public void updateIFCPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		update("prjcodesettingsSqlMap.updateIFCPrjCodeSettings", prjcodesettingsvo);
	}
	public void deleteCopyPrjIFCCodeSettings(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.deleteCopyPrjIFCCodeSettings", prjcodesettingsvo);
	}
	public void insertCopyPrjIFCCodeSettings(PrjCodeSettingsVO prjcodesettingsvo){
		insert("prjcodesettingsSqlMap.insertCopyPrjIFCCodeSettings", prjcodesettingsvo);
	}
	public PrjCodeSettingsVO getTransmittalNoForm(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO getTransmittalNoForm = selectOne("prjcodesettingsSqlMap.getTransmittalNoForm", prjcodesettingsvo);
		return getTransmittalNoForm;
	}
	public List<PrjCodeSettingsVO> getPrjGroupList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getPrjGroupList = selectList("prjcodesettingsSqlMap.getPrjGroupList", prjcodesettingsvo);
		return getPrjGroupList;
	}
	public void insertPrjGroup(PrjCodeSettingsVO prjcodesettingsvo){
		insert("prjcodesettingsSqlMap.insertPrjGroup", prjcodesettingsvo);
	}
	public void updatePrjGroup(PrjCodeSettingsVO prjcodesettingsvo){
		update("prjcodesettingsSqlMap.updatePrjGroup", prjcodesettingsvo);
	}
	public void deletePrjGroup(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.deletePrjGroup", prjcodesettingsvo);
	}
	public void prjCodeSettingsUpdateCode(PrjCodeSettingsVO prjcodesettingsvo) {
		update("prjcodesettingsSqlMap.prjCodeSettingsUpdateCode", prjcodesettingsvo);
	}
	public List<PrjCodeSettingsVO> getCorrFromToList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getCorrFromToList = selectList("prjcodesettingsSqlMap.getCorrFromToList", prjcodesettingsvo);
		return getCorrFromToList;
	}
	public List<PrjCodeSettingsVO> getCorrTypeList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getCorrTypeList = selectList("prjcodesettingsSqlMap.getCorrTypeList", prjcodesettingsvo);
		return getCorrTypeList;
	}
	public List<PrjCodeSettingsVO> getCorrResponsibilityList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getCorrResponsibilityList = selectList("prjcodesettingsSqlMap.getCorrResponsibilityList", prjcodesettingsvo);
		return getCorrResponsibilityList;
	}
	public PrjCodeSettingsVO getCorrSerialAuto(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO getCorrSerialAuto = selectOne("prjcodesettingsSqlMap.getCorrSerialAuto", prjcodesettingsvo);
		return getCorrSerialAuto;
	}
	public PrjCodeSettingsVO getCorrInit(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO getCorrInit = selectOne("prjcodesettingsSqlMap.getCorrInit", prjcodesettingsvo);
		return getCorrInit;
	}
	public List<PrjCodeSettingsVO> getTrIssuePurposeList(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getTrIssuePurposeList = selectList("prjcodesettingsSqlMap.getTrIssuePurposeList", prjcodesettingsvo);
		return getTrIssuePurposeList;
	}
	public List<PrjDisciplineVO> getTrDisciplineList(PrjDisciplineVO prjdisciplinevo){
		List<PrjDisciplineVO> getTrDisciplineList = selectList("prjcodesettingsSqlMap.getTrDisciplineList", prjdisciplinevo);
		return getTrDisciplineList;
	}
	public PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = selectOne("prjcodesettingsSqlMap.isIFCStepYAndActualDateUpdateByIFCIssue", prjcodesettingsvo);
		return isIFCStepYAndActualDateUpdateByIFCIssue;
	}
	public List<PrjCodeSettingsVO> getRevisionNumberCode(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getRevisionNumberCode = selectList("prjcodesettingsSqlMap.getRevisionNumberCode", prjcodesettingsvo);
		return getRevisionNumberCode;
	}
	public PrjCodeSettingsVO getTRIssueStep(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO getTRIssueStep = selectOne("prjcodesettingsSqlMap.getTRIssueStep", prjcodesettingsvo);
		return getTRIssueStep;
	}
	public PrjCodeSettingsVO getVTRIssueStep(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO getVTRIssueStep = selectOne("prjcodesettingsSqlMap.getVTRIssueStep", prjcodesettingsvo);
		return getVTRIssueStep;
	}
	public PrjCodeSettingsVO getSTRIssueStep(PrjCodeSettingsVO prjcodesettingsvo){
		PrjCodeSettingsVO getSTRIssueStep = selectOne("prjcodesettingsSqlMap.getSTRIssueStep", prjcodesettingsvo);
		return getSTRIssueStep;
	}
	public int insertCopyDefaultPrjCodeSettings(PrjCodeSettingsVO prjcodesettingsvo){
		int insertCopyDefaultPrjCodeSettings = insert("prjcodesettingsSqlMap.insertCopyDefaultPrjCodeSettings", prjcodesettingsvo);
		return insertCopyDefaultPrjCodeSettings;
	}
	public int insertCopyDefaultDisciplineInfo(PrjDisciplineVO prjdisciplinevo){
		int insertCopyDefaultDisciplineInfo = insert("prjcodesettingsSqlMap.insertCopyDefaultDisciplineInfo", prjdisciplinevo);
		return insertCopyDefaultDisciplineInfo;
	}
	public int insertCopyDefaultDisciplineFolderInfo(PrjDisciplineVO prjdisciplinevo){
		int insertCopyDefaultDisciplineFolderInfo = insert("prjcodesettingsSqlMap.insertCopyDefaultDisciplineFolderInfo", prjdisciplinevo);
		return insertCopyDefaultDisciplineFolderInfo;
	}
	
	public List<PrjCodeSettingsVO> pg05_getCode(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> result = selectList("prjcodesettingsSqlMap.pg05_getCode", prjcodesettingsvo);
		return result;
	}
	
	public void pg05_ClearTbl(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.pg05_ClearTbl",prjcodesettingsvo);
	}
	
	public void pg05_copyTbl(PrjCodeSettingsVO prjcodesettingsvo) {
		insert("prjcodesettingsSqlMap.pg05_copyTbl",prjcodesettingsvo);
	}
	
	public void pg05_delCode(PrjCodeSettingsVO prjcodesettingsvo) {
		delete("prjcodesettingsSqlMap.pg05_delCode",prjcodesettingsvo);
	}
	
	public int pg05_chkCodeId(PrjCodeSettingsVO prjcodesettingsvo) {
		int result = selectOne("prjcodesettingsSqlMap.pg05_chkCodeId",prjcodesettingsvo);
		return result;
	}
	
	
	public void pg05_updateCode(PrjCodeSettingsVO prjcodesettingsvo) {
		update("prjcodesettingsSqlMap.pg05_updateCode", prjcodesettingsvo);
	}
	
	public int pg05_insertCode(PrjCodeSettingsVO prjcodesettingsvo) {
		String prj_id = prjcodesettingsvo.getPrj_id();
		String set_code_id = selectOne("prjcodesettingsSqlMap.pg05_getMaxCode",prj_id);
		prjcodesettingsvo.setSet_code_id(set_code_id);
		
		return insert("prjcodesettingsSqlMap.pg05_insertCode", prjcodesettingsvo);
	}

	public FolderInfoVO getDiscipObj(FolderInfoVO folderInfoVo) {
		FolderInfoVO result = null;
		
		String folder_path = selectOne("folderinfoSqlMap.getFolderPath",folderInfoVo);
		
		
		List<FolderInfoVO> getAllDiscipFolderListInProject = 
				selectList("folderinfoSqlMap.getAllDiscipFolderListInProject",folderInfoVo);
		
		for(FolderInfoVO discip: getAllDiscipFolderListInProject) {
			String discip_folder_path = discip.getFolder_path();
			if(folder_path.contains(discip_folder_path)) {
				result = discip;
				break;
			}	
		}
		
		
		
		return result;
	}
	
	public int pg05_del(PrjCodeSettingsVO prjcodesettingsvo) {
		String code_type = prjcodesettingsvo.getSet_code_type();
		
		/*
		 * switch(code_type) { case "VTR RETURN STATUS":
		 * if(isUseDocSize(prjcodesettingsvo) > 0) {
		 * System.out.println("해당 doc_size를 사용하는 도서가 있습니다."); return -1; } break;
		 * default: return -1; }
		 */
		
		return delete("prjcodesettingsSqlMap.pg05_delCode",prjcodesettingsvo);
	}
	
}


