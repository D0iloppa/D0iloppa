package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.FolderAuthVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDisciplineVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjEngInfoVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjGroupMemberVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjSystemPrefixVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.PrjWbsCodeVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.model.UsersVO;

@Service
public class DataMigrationService extends AbstractService {
	public List<PrjCodeSettingsVO> getCodeSetList(PrjCodeSettingsVO prjCodeSettingsVo) {
		List<PrjCodeSettingsVO> getCodeSetList = selectList("dataMigrationSqlMap.getCodeSetList",prjCodeSettingsVo);
		return getCodeSetList;
	}
	public void deleteCodeSettings(PrjCodeSettingsVO prjCodeSettingsVo) {
		delete("dataMigrationSqlMap.deleteCodeSettings",prjCodeSettingsVo);
	}
	public void insertCodeSettings(PrjCodeSettingsVO prjCodeSettingsVo) {
		insert("dataMigrationSqlMap.insertCodeSettings",prjCodeSettingsVo);
	}
	
	public List<PrjDocumentIndexVO> getCordocInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<PrjDocumentIndexVO> getCordocInfoList = selectList("dataMigrationSqlMap.getCordocInfoList",prjDocumentIndexVo);
		return getCordocInfoList;
	}
	public void deleteCordocInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		delete("dataMigrationSqlMap.deleteCordocInfo",prjDocumentIndexVo);
	}
	public void insertCordocInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		insert("dataMigrationSqlMap.insertCordocInfo",prjDocumentIndexVo);
	}
	
	public List<PrjDisciplineVO> getDisciplineFolderInfoList(PrjDisciplineVO prjDisciplineVo) {
		List<PrjDisciplineVO> getDisciplineFolderInfoList = selectList("dataMigrationSqlMap.getDisciplineFolderInfoList",prjDisciplineVo);
		return getDisciplineFolderInfoList;
	}
	public void deleteDisciplineFolderInfo(PrjDisciplineVO prjDisciplineVo) {
		delete("dataMigrationSqlMap.deleteDisciplineFolderInfo",prjDisciplineVo);
	}
	public void insertDisciplineFolderInfo(PrjDisciplineVO prjDisciplineVo) {
		insert("dataMigrationSqlMap.insertDisciplineFolderInfo",prjDisciplineVo);
	}
	
	public List<PrjDisciplineVO> getDisciplineInfoList(PrjDisciplineVO prjDisciplineVo) {
		List<PrjDisciplineVO> getDisciplineInfoList = selectList("dataMigrationSqlMap.getDisciplineInfoList",prjDisciplineVo);
		return getDisciplineInfoList;
	}
	public void deleteDisciplineInfo(PrjDisciplineVO prjDisciplineVo) {
		delete("dataMigrationSqlMap.deleteDisciplineInfo",prjDisciplineVo);
	}
	public void insertDisciplineInfo(PrjDisciplineVO prjDisciplineVo) {
		insert("dataMigrationSqlMap.insertDisciplineInfo",prjDisciplineVo);
	}
	
	public List<PrjDocumentIndexVO> getDocumentIndexList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<PrjDocumentIndexVO> getDocumentIndexList = selectList("dataMigrationSqlMap.getDocumentIndexList",prjDocumentIndexVo);
		return getDocumentIndexList;
	}
	public void deleteDocumentIndex(PrjDocumentIndexVO prjDocumentIndexVo) {
		delete("dataMigrationSqlMap.deleteDocumentIndex",prjDocumentIndexVo);
	}
	public void insertDocumentIndex(PrjDocumentIndexVO prjDocumentIndexVo) {
		insert("dataMigrationSqlMap.insertDocumentIndex",prjDocumentIndexVo);
	}
	
	public List<PrjEmailVO> getEmailAttachInfoList(PrjEmailVO prjEmailVo){
		List<PrjEmailVO> getEmailAttachInfoList = selectList("dataMigrationSqlMap.getEmailAttachInfoList",prjEmailVo);
		return getEmailAttachInfoList;
	}
	public void deleteEmailAttachInfo(PrjEmailVO prjEmailVo) {
		delete("dataMigrationSqlMap.deleteEmailAttachInfo",prjEmailVo);
	}
	public void insertEmailAttachInfo(PrjEmailVO prjEmailVo) {
		insert("dataMigrationSqlMap.insertEmailAttachInfo",prjEmailVo);
	}
	
	public List<PrjEmailVO> getEmailReceiptInfoList(PrjEmailVO prjEmailVo){
		List<PrjEmailVO> getEmailReceiptInfoList = selectList("dataMigrationSqlMap.getEmailReceiptInfoList",prjEmailVo);
		return getEmailReceiptInfoList;
	}
	public void deleteEmailReceiptInfo(PrjEmailVO prjEmailVo) {
		delete("dataMigrationSqlMap.deleteEmailReceiptInfo",prjEmailVo);
	}
	public void insertEmailReceiptInfo(PrjEmailVO prjEmailVo) {
		insert("dataMigrationSqlMap.insertEmailReceiptInfo",prjEmailVo);
	}
	
	public List<PrjEmailVO> getEmailSendInfoList(PrjEmailVO prjEmailVo) {
		List<PrjEmailVO> getEmailSendInfoList = selectList("dataMigrationSqlMap.getEmailSendInfoList",prjEmailVo);
		return getEmailSendInfoList;
	}
	public void deleteEmailSendInfo(PrjEmailVO prjEmailVo) {
		delete("dataMigrationSqlMap.deleteEmailSendInfo",prjEmailVo);
	}
	public void insertEmailSendInfo(PrjEmailVO prjEmailVo) {
		insert("dataMigrationSqlMap.insertEmailSendInfo",prjEmailVo);
	}
	
	public List<PrjEmailTypeVO> getEmailTypeList(PrjEmailTypeVO prjEmailTypeVo) {
		List<PrjEmailTypeVO> getEmailTypeList = selectList("dataMigrationSqlMap.getEmailTypeList",prjEmailTypeVo);
		return getEmailTypeList;
	}
	public void deleteEmailType(PrjEmailTypeVO prjEmailTypeVo) {
		delete("dataMigrationSqlMap.deleteEmailType",prjEmailTypeVo);
	}
	public void insertEmailType(PrjEmailTypeVO prjEmailTypeVo) {
		insert("dataMigrationSqlMap.insertEmailType",prjEmailTypeVo);
	}
	
	public List<PrjEmailTypeContentsVO> getEmailTypeContentsList(PrjEmailTypeContentsVO prjEmailTypeContentsVo) {
		List<PrjEmailTypeContentsVO> getEmailTypeContentsList = selectList("dataMigrationSqlMap.getEmailTypeContentsList",prjEmailTypeContentsVo);
		return getEmailTypeContentsList;
	}
	public void deleteEmailTypeContents(PrjEmailTypeContentsVO prjEmailTypeContentsVo) {
		delete("dataMigrationSqlMap.deleteEmailTypeContents",prjEmailTypeContentsVo);
	}
	public void insertEmailTypeContents(PrjEmailTypeContentsVO prjEmailTypeContentsVo) {
		insert("dataMigrationSqlMap.insertEmailTypeContents",prjEmailTypeContentsVo);
	}
	
	public List<PrjEngInfoVO> getEngInfoList(PrjEngInfoVO prjEngInfoVo) {
		List<PrjEngInfoVO> getEngInfoList = selectList("dataMigrationSqlMap.getEngInfoList",prjEngInfoVo);
		return getEngInfoList;
	}
	public void deleteEngInfo(PrjEngInfoVO prjEngInfoVo) {
		delete("dataMigrationSqlMap.deleteEngInfo",prjEngInfoVo);
	}
	public void insertEngInfo(PrjEngInfoVO prjEngInfoVo) {
		insert("dataMigrationSqlMap.insertEngInfo",prjEngInfoVo);
	}
	
	public List<PrjStepVO> getEngStepList(PrjStepVO prjStepVo) {
		List<PrjStepVO> getEngStepList = selectList("dataMigrationSqlMap.getEngStepList",prjStepVo);
		return getEngStepList;
	}
	public void deleteEngStep(PrjStepVO prjStepVo) {
		delete("dataMigrationSqlMap.deleteEngStep",prjStepVo);
	}
	public void insertEngStep(PrjStepVO prjStepVo) {
		insert("dataMigrationSqlMap.insertEngStep",prjStepVo);
	}
	
	public List<FolderAuthVO> getFolderAuthList(FolderAuthVO folderAuthVo) {
		List<FolderAuthVO> getFolderAuthList = selectList("dataMigrationSqlMap.getFolderAuthList",folderAuthVo);
		return getFolderAuthList;
	}
	public void deleteFolderAuth(FolderAuthVO folderAuthVo) {
		delete("dataMigrationSqlMap.deleteFolderAuth",folderAuthVo);
	}
	public void insertFolderAuth(FolderAuthVO folderAuthVo) {
		insert("dataMigrationSqlMap.insertFolderAuth",folderAuthVo);
	}
	
	public List<PrjFolderInfoVO> getFolderInfoList(PrjFolderInfoVO prjFolderInfoVo) {
		List<PrjFolderInfoVO> getFolderInfoList = selectList("dataMigrationSqlMap.getFolderInfoList",prjFolderInfoVo);
		return getFolderInfoList;
	}
	public void deleteFolderInfo(PrjFolderInfoVO prjFolderInfoVo) {
		delete("dataMigrationSqlMap.deleteFolderInfo",prjFolderInfoVo);
	}
	public void insertFolderInfo(PrjFolderInfoVO prjFolderInfoVo) {
		insert("dataMigrationSqlMap.insertFolderInfo",prjFolderInfoVo);
	}
	
	public List<PrjDocumentIndexVO> getGendocInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<PrjDocumentIndexVO> getGendocInfoList = selectList("dataMigrationSqlMap.getGendocInfoList",prjDocumentIndexVo);
		return getGendocInfoList;
	}
	public void deleteGendocInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		delete("dataMigrationSqlMap.deleteGendocInfo",prjDocumentIndexVo);
	}
	public void insertGendocInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		insert("dataMigrationSqlMap.insertGendocInfo",prjDocumentIndexVo);
	}
	
	public List<PrjGroupMemberVO> getGroupMemberList(PrjGroupMemberVO prjGroupMemberVo) {
		List<PrjGroupMemberVO> getGroupMemberList = selectList("dataMigrationSqlMap.getGroupMemberList",prjGroupMemberVo);
		return getGroupMemberList;
	}
	public void deleteGroupMember(PrjGroupMemberVO prjGroupMemberVo) {
		delete("dataMigrationSqlMap.deleteGroupMember",prjGroupMemberVo);
	}
	public void insertGroupMember(PrjGroupMemberVO prjGroupMemberVo) {
		insert("dataMigrationSqlMap.insertGroupMember",prjGroupMemberVo);
	}
	
	public List<PrjInfoVO> getPrjInfoList(PrjInfoVO prjInfoVo) {
		List<PrjInfoVO> getPrjInfoList = selectList("dataMigrationSqlMap.getPrjInfoList",prjInfoVo);
		return getPrjInfoList;
	}
	public void deletePrjInfo(PrjInfoVO prjInfoVo) {
		delete("dataMigrationSqlMap.deletePrjInfo",prjInfoVo);
	}
	public void insertPrjInfo(PrjInfoVO prjInfoVo) {
		insert("dataMigrationSqlMap.insertPrjInfo",prjInfoVo);
	}
	
	public List<PrjMemberVO> getPrjMemberList(PrjMemberVO prjMemberVo) {
		List<PrjMemberVO> getPrjMemberList = selectList("dataMigrationSqlMap.getPrjMemberList",prjMemberVo);
		return getPrjMemberList;
	}
	public void deletePrjMember(PrjMemberVO prjMemberVo) {
		delete("dataMigrationSqlMap.deletePrjMember",prjMemberVo);
	}
	public void insertPrjMember(PrjMemberVO prjMemberVo) {
		insert("dataMigrationSqlMap.insertPrjMember",prjMemberVo);
	}
	
	public List<PrjDocumentIndexVO> getProInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<PrjDocumentIndexVO> getProInfoList = selectList("dataMigrationSqlMap.getProInfoList",prjDocumentIndexVo);
		return getProInfoList;
	}
	public void deleteProInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		delete("dataMigrationSqlMap.deleteProInfo",prjDocumentIndexVo);
	}
	public void insertProInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		insert("dataMigrationSqlMap.insertProInfo",prjDocumentIndexVo);
	}
	
	public List<PrjStepVO> getProStepList(PrjStepVO prjStepVo) {
		List<PrjStepVO> getProStepList = selectList("dataMigrationSqlMap.getProStepList",prjStepVo);
		return getProStepList;
	}
	public void deleteProStep(PrjStepVO prjStepVo) {
		delete("dataMigrationSqlMap.deleteProStep",prjStepVo);
	}
	public void insertProStep(PrjStepVO prjStepVo) {
		insert("dataMigrationSqlMap.insertProStep",prjStepVo);
	}

	public List<ProcessInfoVO> getProcessInfoList(ProcessInfoVO processInfoVo) {
		List<ProcessInfoVO> getProcessInfoList = selectList("dataMigrationSqlMap.getProcessInfoList",processInfoVo);
		return getProcessInfoList;
	}
	public void deleteProcessInfoList(ProcessInfoVO processInfoVo) {
		delete("dataMigrationSqlMap.deleteProcessInfoList",processInfoVo);
	}
	public void insertProcessInfoList(ProcessInfoVO processInfoVo) {
		insert("dataMigrationSqlMap.insertProcessInfoList",processInfoVo);
	}
	public List<PrjDocumentIndexVO> getSdcInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<PrjDocumentIndexVO> getSdcInfoList = selectList("dataMigrationSqlMap.getSdcInfoList",prjDocumentIndexVo);
		return getSdcInfoList;
	}
	public void deleteSdcInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		delete("dataMigrationSqlMap.deleteSdcInfo",prjDocumentIndexVo);
	}
	public void insertSdcInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		insert("dataMigrationSqlMap.insertSdcInfo",prjDocumentIndexVo);
	}
	
	public List<PrjStepVO> getSdcStepList(PrjStepVO prjStepVo) {
		List<PrjStepVO> getSdcStepList = selectList("dataMigrationSqlMap.getSdcStepList",prjStepVo);
		return getSdcStepList;
	}
	public void deleteSdcStep(PrjStepVO prjStepVo) {
		delete("dataMigrationSqlMap.deleteSdcStep",prjStepVo);
	}
	public void insertSdcStep(PrjStepVO prjStepVo) {
		insert("dataMigrationSqlMap.insertSdcStep",prjStepVo);
	}
	
	public List<PrjTrVO> getStrFileList(PrjTrVO prjTrVo) {
		List<PrjTrVO> getStrFileList = selectList("dataMigrationSqlMap.getStrFileList",prjTrVo);
		return getStrFileList;
	}
	public void deleteStrFile(PrjTrVO prjTrVo) {
		delete("dataMigrationSqlMap.deleteStrFile",prjTrVo);
	}
	public void insertStrFile(PrjTrVO prjTrVo) {
		insert("dataMigrationSqlMap.insertStrFile",prjTrVo);
	}
	
	public List<PrjTrVO> getStrInfoList(PrjTrVO prjTrVo) {
		List<PrjTrVO> getStrInfoList = selectList("dataMigrationSqlMap.getStrInfoList",prjTrVo);
		return getStrInfoList;
	}
	public void deleteStrInfo(PrjTrVO prjTrVo) {
		delete("dataMigrationSqlMap.deleteStrInfo",prjTrVo);
	}
	public void insertStrInfo(PrjTrVO prjTrVo) {
		insert("dataMigrationSqlMap.insertStrInfo",prjTrVo);
	}
	
	public List<PrjSystemPrefixVO> getSystemPrefixList(PrjSystemPrefixVO prjSystemPrefixVo) {
		List<PrjSystemPrefixVO> getSystemPrefixList = selectList("dataMigrationSqlMap.getSystemPrefixList",prjSystemPrefixVo);
		return getSystemPrefixList;
	}
	public void deleteSystemPrefix(PrjSystemPrefixVO prjSystemPrefixVo) {
		delete("dataMigrationSqlMap.deleteSystemPrefix",prjSystemPrefixVo);
	}
	public void insertSystemPrefix(PrjSystemPrefixVO prjSystemPrefixVo) {
		insert("dataMigrationSqlMap.insertSystemPrefix",prjSystemPrefixVo);
	}
	
	public List<PrjTrVO> getTrFileList(PrjTrVO prjTrVo) {
		List<PrjTrVO> getTrFileList = selectList("dataMigrationSqlMap.getTrFileList",prjTrVo);
		return getTrFileList;
	}
	public void deleteTrFile(PrjTrVO prjTrVo) {
		delete("dataMigrationSqlMap.deleteTrFile",prjTrVo);
	}
	public void insertTrFile(PrjTrVO prjTrVo) {
		insert("dataMigrationSqlMap.insertTrFile",prjTrVo);
	}
	
	public List<PrjTrVO> getTrInfoList(PrjTrVO prjTrVo) {
		List<PrjTrVO> getTrInfoList = selectList("dataMigrationSqlMap.getTrInfoList",prjTrVo);
		return getTrInfoList;
	}
	public void deleteTrInfo(PrjTrVO prjTrVo) {
		delete("dataMigrationSqlMap.deleteTrInfo",prjTrVo);
	}
	public void insertTrInfo(PrjTrVO prjTrVo) {
		insert("dataMigrationSqlMap.insertTrInfo",prjTrVo);
	}
	
	public List<PrjDocumentIndexVO> getVdrInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<PrjDocumentIndexVO> getVdrInfoList = selectList("dataMigrationSqlMap.getVdrInfoList",prjDocumentIndexVo);
		return getVdrInfoList;
	}
	public void deleteVdrInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		delete("dataMigrationSqlMap.deleteVdrInfo",prjDocumentIndexVo);
	}
	public void insertVdrInfo(PrjDocumentIndexVO prjDocumentIndexVo) {
		insert("dataMigrationSqlMap.insertVdrInfo",prjDocumentIndexVo);
	}
	
	public List<PrjStepVO> getVdrStepList(PrjStepVO prjStepVo) {
		List<PrjStepVO> getVdrStepList = selectList("dataMigrationSqlMap.getVdrStepList",prjStepVo);
		return getVdrStepList;
	}
	public void deleteVdrStep(PrjStepVO prjStepVo) {
		delete("dataMigrationSqlMap.deleteVdrStep",prjStepVo);
	}
	public void insertVdrStep(PrjStepVO prjStepVo) {
		insert("dataMigrationSqlMap.insertVdrStep",prjStepVo);
	}
	
	public List<PrjTrVO> getVtrFileList(PrjTrVO prjTrVo) {
		List<PrjTrVO> getVtrFileList = selectList("dataMigrationSqlMap.getVtrFileList",prjTrVo);
		return getVtrFileList;
	}
	public void deleteVtrFile(PrjTrVO prjTrVo) {
		delete("dataMigrationSqlMap.deleteVtrFile",prjTrVo);
	}
	public void insertVtrFile(PrjTrVO prjTrVo) {
		insert("dataMigrationSqlMap.insertVtrFile",prjTrVo);
	}
	
	public List<PrjTrVO> getVtrInfoList(PrjTrVO prjTrVo) {
		List<PrjTrVO> getVtrInfoList = selectList("dataMigrationSqlMap.getVtrInfoList",prjTrVo);
		return getVtrInfoList;
	}
	public void deleteVtrInfo(PrjTrVO prjTrVo) {
		delete("dataMigrationSqlMap.deleteVtrInfo",prjTrVo);
	}
	public void insertVtrInfo(PrjTrVO prjTrVo) {
		insert("dataMigrationSqlMap.insertVtrInfo",prjTrVo);
	}
	
	public List<PrjWbsCodeVO> getMigWbsCodeList(PrjWbsCodeVO prjWbsCodeVo) {
		List<PrjWbsCodeVO> getMigWbsCodeList = selectList("dataMigrationSqlMap.getMigWbsCodeList",prjWbsCodeVo);
		return getMigWbsCodeList;
	}
	public void deleteMigWbsCode(PrjWbsCodeVO prjWbsCodeVo) {
		delete("dataMigrationSqlMap.deleteMigWbsCode",prjWbsCodeVo);
	}
	public void insertMigWbsCode(PrjWbsCodeVO prjWbsCodeVo) {
		insert("dataMigrationSqlMap.insertMigWbsCode",prjWbsCodeVo);
	}
	
	public List<UsersVO> getMigUsersList(UsersVO usersVo) {
		List<UsersVO> getMigUsersList = selectList("dataMigrationSqlMap.getMigUsersList",usersVo);
		return getMigUsersList;
	}
	public void insertMigUsers(UsersVO usersVo) {
		insert("dataMigrationSqlMap.insertMigUsers",usersVo);
	}
}
