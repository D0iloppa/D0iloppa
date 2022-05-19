package kr.co.hhi.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.common.util.FileExtFilter;
import kr.co.hhi.model.OrganizationVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDocTrNoChgHistVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjDrnInfoVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.UsersVO;

@Service
public class PrjTrService extends AbstractService {
	public int getUsedTRIncoming(PrjDocumentIndexVO prjdocumentindexvo){
		int getUsedTRIncoming = selectOne("prjtrSqlMap.getUsedTRIncoming", prjdocumentindexvo);
		return getUsedTRIncoming;
	}
	public int getUsedVTRIncoming(PrjDocumentIndexVO prjdocumentindexvo){
		int getUsedVTRIncoming = selectOne("prjtrSqlMap.getUsedVTRIncoming", prjdocumentindexvo);
		return getUsedVTRIncoming;
	}
	public int getUsedSTRIncoming(PrjDocumentIndexVO prjdocumentindexvo){
		int getUsedSTRIncoming = selectOne("prjtrSqlMap.getUsedSTRIncoming", prjdocumentindexvo);
		return getUsedSTRIncoming;
	}
	public List<PrjTrVO> getPrjTrHistory(PrjTrVO prjtrvo){
		List<PrjTrVO> getPrjTrHistory = selectList("prjtrSqlMap.getPrjTrHistory", prjtrvo);
		return getPrjTrHistory;
	}
	public List<PrjTrVO> getPrjVTrHistory(PrjTrVO prjtrvo){
		List<PrjTrVO> getPrjVTrHistory = selectList("prjtrSqlMap.getPrjVTrHistory", prjtrvo);
		return getPrjVTrHistory;
	}
	public List<PrjTrVO> getPrjSTrHistory(PrjTrVO prjtrvo){
		List<PrjTrVO> getPrjSTrHistory = selectList("prjtrSqlMap.getPrjSTrHistory", prjtrvo);
		return getPrjSTrHistory;
	}
	public int isDuplicateTrNo(PrjTrVO prjtrvo){
		int isDuplicateTrNo = selectOne("prjtrSqlMap.isDuplicateTrNo",prjtrvo);
		return isDuplicateTrNo;
	}
	public int isDuplicateVTrNo(PrjTrVO prjtrvo){
		int isDuplicateVTrNo = selectOne("prjtrSqlMap.isDuplicateVTrNo",prjtrvo);
		return isDuplicateVTrNo;
	}
	public int isDuplicateSTrNo(PrjTrVO prjtrvo){
		int isDuplicateSTrNo = selectOne("prjtrSqlMap.isDuplicateSTrNo",prjtrvo);
		return isDuplicateSTrNo;
	}
	public int isDuplicateTrNoByUpdate(PrjTrVO prjtrvo){
		int isDuplicateTrNoByUpdate = selectOne("prjtrSqlMap.isDuplicateTrNoByUpdate",prjtrvo);
		return isDuplicateTrNoByUpdate;
	}
	public int isDuplicateVTrNoByUpdate(PrjTrVO prjtrvo){
		int isDuplicateVTrNoByUpdate = selectOne("prjtrSqlMap.isDuplicateVTrNoByUpdate",prjtrvo);
		return isDuplicateVTrNoByUpdate;
	}
	public int isDuplicateSTrNoByUpdate(PrjTrVO prjtrvo){
		int isDuplicateSTrNoByUpdate = selectOne("prjtrSqlMap.isDuplicateSTrNoByUpdate",prjtrvo);
		return isDuplicateSTrNoByUpdate;
	}
	public String insertTrInfo(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertTrInfo",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertTrInfoIncoming(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertTrInfoIncoming",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertTrInfoIncomingNotAuto(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertTrInfoIncomingNotAuto",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertVTrInfo(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertVTrInfo",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertVTrInfoIncoming(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertVTrInfoIncoming",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertVTrInfoIncomingNotAuto(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertVTrInfoIncomingNotAuto",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertSTrInfo(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertSTrInfo",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertSTrInfoIncoming(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertSTrInfoIncoming",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public String insertSTrInfoIncomingNotAuto(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertSTrInfoIncomingNotAuto",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	public int updateTrInfo(PrjTrVO prjtrvo){
		int updateTrInfo = update("prjtrSqlMap.updateTrInfo",prjtrvo);
		return updateTrInfo;
	}
	public int updateTrInfoIncoming(PrjTrVO prjtrvo){
		int updateTrInfoIncoming = update("prjtrSqlMap.updateTrInfoIncoming",prjtrvo);
		return updateTrInfoIncoming;
	}
	public int updateTrSendDateByDCC(PrjTrVO prjtrvo){
		int updateTrSendDateByDCC = update("prjtrSqlMap.updateTrSendDateByDCC",prjtrvo);
		return updateTrSendDateByDCC;
	}
	public int updateVTrSendDateByDCC(PrjTrVO prjtrvo){
		int updateVTrSendDateByDCC = update("prjtrSqlMap.updateVTrSendDateByDCC",prjtrvo);
		return updateVTrSendDateByDCC;
	}
	public int updateSTrSendDateByDCC(PrjTrVO prjtrvo){
		int updateSTrSendDateByDCC = update("prjtrSqlMap.updateSTrSendDateByDCC",prjtrvo);
		return updateSTrSendDateByDCC;
	}
	public int updateVTrInfo(PrjTrVO prjtrvo){
		int updateVTrInfo = update("prjtrSqlMap.updateVTrInfo",prjtrvo);
		return updateVTrInfo;
	}
	public int updateVTrInfoIncoming(PrjTrVO prjtrvo){
		int updateVTrInfoIncoming = update("prjtrSqlMap.updateVTrInfoIncoming",prjtrvo);
		return updateVTrInfoIncoming;
	}
	public int updateSTrInfo(PrjTrVO prjtrvo){
		int updateSTrInfo = update("prjtrSqlMap.updateSTrInfo",prjtrvo);
		return updateSTrInfo;
	}
	public int updateSTrInfoIncoming(PrjTrVO prjtrvo){
		int updateSTrInfoIncoming = update("prjtrSqlMap.updateSTrInfoIncoming",prjtrvo);
		return updateSTrInfoIncoming;
	}
	public int isExistTrNo(PrjTrVO prjtrvo){
		int isExistTrNo = selectOne("prjtrSqlMap.isExistTrNo",prjtrvo);
		return isExistTrNo;
	}
	public PrjTrVO getChangingTrInfo(PrjTrVO prjtrvo){
		PrjTrVO getChangingTrInfo = selectOne("prjtrSqlMap.getChangingTrInfo",prjtrvo);
		return getChangingTrInfo;
	}
	public int insertTrNoChgHist(PrjDocTrNoChgHistVO prjdoctrnochghistvo){
		int insertTrNoChgHist = insert("prjtrSqlMap.insertTrNoChgHist",prjdoctrnochghistvo);
		return insertTrNoChgHist;
	}
	public int changeTrNo(PrjTrVO prjtrvo){
		int changeTrNo = update("prjtrSqlMap.changeTrNo",prjtrvo);
		return changeTrNo;
	}
	public List<PrjDocTrNoChgHistVO> getTrNoChgHist(PrjDocTrNoChgHistVO prjdoctrnochghistvo){
		List<PrjDocTrNoChgHistVO> getTrNoChgHist = selectList("prjtrSqlMap.getTrNoChgHist",prjdoctrnochghistvo);
		return getTrNoChgHist;
	}
	public int insertTrFile(PrjTrVO prjtrvo){
		int insertTrFile = insert("prjtrSqlMap.insertTrFile",prjtrvo);
		return insertTrFile;
	}
	public int selectTrFile(PrjTrVO prjtrvo){
		int selectTrFile = selectOne("prjtrSqlMap.selectTrFile",prjtrvo);
		return selectTrFile;
	}
	public int insertVTrFile(PrjTrVO prjtrvo){
		int insertVTrFile = insert("prjtrSqlMap.insertVTrFile",prjtrvo);
		return insertVTrFile;
	}
	public int selectVTrFile(PrjTrVO prjtrvo){
		int selectVTrFile = selectOne("prjtrSqlMap.selectVTrFile",prjtrvo);
		return selectVTrFile;
	}
	public int insertSTrFile(PrjTrVO prjtrvo){
		int insertSTrFile = insert("prjtrSqlMap.insertSTrFile",prjtrvo);
		return insertSTrFile;
	}
	public int selectSTrFile(PrjTrVO prjtrvo){
		int selectSTrFile = selectOne("prjtrSqlMap.selectSTrFile",prjtrvo);
		return selectSTrFile;
	}
	public int deleteTrFile(PrjTrVO prjtrvo){
		int deleteTrFile = delete("prjtrSqlMap.deleteTrFile",prjtrvo);
		return deleteTrFile;
	}
	public int deleteTrInfo(PrjTrVO prjtrvo){
		int deleteTrInfo = delete("prjtrSqlMap.deleteTrInfo",prjtrvo);
		return deleteTrInfo;
	}
	public int deleteTrFileAll(PrjTrVO prjtrvo){
		int deleteTrFileAll = delete("prjtrSqlMap.deleteTrFileAll",prjtrvo);
		return deleteTrFileAll;
	}
	public int deleteVTrFile(PrjTrVO prjtrvo){
		int deleteVTrFile = delete("prjtrSqlMap.deleteVTrFile",prjtrvo);
		return deleteVTrFile;
	}
	public int deleteVTrInfo(PrjTrVO prjtrvo){
		int deleteVTrInfo = delete("prjtrSqlMap.deleteVTrInfo",prjtrvo);
		return deleteVTrInfo;
	}
	public int deleteVTrFileAll(PrjTrVO prjtrvo){
		int deleteVTrFileAll = delete("prjtrSqlMap.deleteVTrFileAll",prjtrvo);
		return deleteVTrFileAll;
	}
	public int deleteSTrFile(PrjTrVO prjtrvo){
		int deleteSTrFile = delete("prjtrSqlMap.deleteSTrFile",prjtrvo);
		return deleteSTrFile;
	}
	public int deleteSTrInfo(PrjTrVO prjtrvo){
		int deleteSTrInfo = delete("prjtrSqlMap.deleteSTrInfo",prjtrvo);
		return deleteSTrInfo;
	}
	public int deleteSTrFileAll(PrjTrVO prjtrvo){
		int deleteSTrFileAll = delete("prjtrSqlMap.deleteSTrFileAll",prjtrvo);
		return deleteSTrFileAll;
	}
	public List<PrjTrVO> getTrInOutSearch(PrjTrVO prjtrvo){
		List<PrjTrVO> getTrInOutSearch = selectList("prjtrSqlMap.getTrInOutSearch", prjtrvo);
		return getTrInOutSearch;
	}
	public List<PrjTrVO> getVTrInOutSearch(PrjTrVO prjtrvo){
		List<PrjTrVO> getVTrInOutSearch = selectList("prjtrSqlMap.getVTrInOutSearch", prjtrvo);
		return getVTrInOutSearch;
	}
	public List<PrjTrVO> getSTrInOutSearch(PrjTrVO prjtrvo){
		List<PrjTrVO> getSTrInOutSearch = selectList("prjtrSqlMap.getSTrInOutSearch", prjtrvo);
		return getSTrInOutSearch;
	}
	public PrjTrVO getTrInfo(PrjTrVO prjtrvo){
		PrjTrVO getTrInfo = selectOne("prjtrSqlMap.getTrInfo", prjtrvo);
		return getTrInfo;
	}
	public List<PrjTrVO> getTrFile(PrjTrVO prjtrvo){
		List<PrjTrVO> getTrFile = selectList("prjtrSqlMap.getTrFile", prjtrvo);
		return getTrFile;
	}
	public List<PrjTrVO> getUseDocInTR(PrjTrVO prjtrvo){
		List<PrjTrVO> getUseDocInTR = selectList("prjtrSqlMap.getUseDocInTR",prjtrvo);
		return getUseDocInTR;
	}
	public List<PrjTrVO> getUseDocInVTR(PrjTrVO prjtrvo){
		List<PrjTrVO> getUseDocInVTR = selectList("prjtrSqlMap.getUseDocInVTR",prjtrvo);
		return getUseDocInVTR;
	}
	public List<PrjTrVO> getUseDocInSTR(PrjTrVO prjtrvo){
		List<PrjTrVO> getUseDocInSTR = selectList("prjtrSqlMap.getUseDocInSTR",prjtrvo);
		return getUseDocInSTR;
	}
	public PrjDrnInfoVO getUseDocInDRN(PrjDrnInfoVO prjdrninfovo){
		PrjDrnInfoVO getUseDocInDRN = selectOne("prjtrSqlMap.getUseDocInDRN",prjdrninfovo);
		return getUseDocInDRN;
	}
	public PrjDrnInfoVO getUseDocInDRNisnotRorC(PrjDrnInfoVO prjdrninfovo){
		PrjDrnInfoVO getUseDocInDRNisnotRorC = selectOne("prjtrSqlMap.getUseDocInDRNisnotRorC",prjdrninfovo);
		return getUseDocInDRNisnotRorC;
	}
	public PrjDrnInfoVO getIsUseDRN(PrjTrVO prjtrvo){
		PrjDrnInfoVO getIsUseDRN = selectOne("prjtrSqlMap.getIsUseDRN",prjtrvo);
		return getIsUseDRN;
	}
	public PrjTrVO getVTrInfo(PrjTrVO prjtrvo){
		PrjTrVO getVTrInfo = selectOne("prjtrSqlMap.getVTrInfo", prjtrvo);
		return getVTrInfo;
	}
	public List<PrjTrVO> getVTrFile(PrjTrVO prjtrvo){
		List<PrjTrVO> getVTrFile = selectList("prjtrSqlMap.getVTrFile", prjtrvo);
		return getVTrFile;
	}
	public PrjTrVO getSTrInfo(PrjTrVO prjtrvo){
		PrjTrVO getSTrInfo = selectOne("prjtrSqlMap.getSTrInfo", prjtrvo);
		return getSTrInfo;
	}
	public List<PrjTrVO> getSTrFile(PrjTrVO prjtrvo){
		List<PrjTrVO> getSTrFile = selectList("prjtrSqlMap.getSTrFile", prjtrvo);
		return getSTrFile;
	}
	public String getTRSerialNo(PrjTrVO prjtrvo){
		String getTRSerialNo = selectOne("prjtrSqlMap.getTRSerialNo", prjtrvo);
		return getTRSerialNo;
	}
	public String getVTRSerialNo(PrjTrVO prjtrvo){
		String getVTRSerialNo = selectOne("prjtrSqlMap.getVTRSerialNo", prjtrvo);
		return getVTRSerialNo;
	}
	public String getSTRSerialNo(PrjTrVO prjtrvo){
		String getSTRSerialNo = selectOne("prjtrSqlMap.getSTRSerialNo", prjtrvo);
		return getSTRSerialNo;
	}
	public int updateTRIssueCancel(PrjTrVO prjtrvo){
		int updateTRIssueCancel = update("prjtrSqlMap.updateTRIssueCancel",prjtrvo);
		return updateTRIssueCancel;
	}
	public int deleteTREmailHistory(PrjTrVO prjtrvo){
		int deleteTREmailHistory = delete("prjtrSqlMap.deleteTREmailHistory",prjtrvo);
		return deleteTREmailHistory;
	}
	public int updateTRNOandIssued(PrjTrVO prjtrvo){
		int updateTRNOandIssued = update("prjtrSqlMap.updateTRNOandIssued",prjtrvo);
		return updateTRNOandIssued;
	}
	public int updateVTRIssueCancel(PrjTrVO prjtrvo){
		int updateVTRIssueCancel = update("prjtrSqlMap.updateVTRIssueCancel",prjtrvo);
		return updateVTRIssueCancel;
	}
	public int updateVTRNOandIssued(PrjTrVO prjtrvo){
		int updateVTRNOandIssued = update("prjtrSqlMap.updateVTRNOandIssued",prjtrvo);
		return updateVTRNOandIssued;
	}
	public int updateSTRIssueCancel(PrjTrVO prjtrvo){
		int updateSTRIssueCancel = update("prjtrSqlMap.updateSTRIssueCancel",prjtrvo);
		return updateSTRIssueCancel;
	}
	public int updateSTRNOandIssued(PrjTrVO prjtrvo){
		int updateSTRNOandIssued = update("prjtrSqlMap.updateSTRNOandIssued",prjtrvo);
		return updateSTRNOandIssued;
	}
	public PrjDocumentIndexVO isDuplicationDocInIssue(PrjDocumentIndexVO prdocumentindexvo){
		PrjDocumentIndexVO isDuplicationDocInIssue = selectOne("prjtrSqlMap.isDuplicationDocInIssue",prdocumentindexvo);
		return isDuplicationDocInIssue;
	}
	public PrjDocumentIndexVO isDuplicationDocVTRInIssue(PrjDocumentIndexVO prdocumentindexvo){
		PrjDocumentIndexVO isDuplicationDocVTRInIssue = selectOne("prjtrSqlMap.isDuplicationDocVTRInIssue",prdocumentindexvo);
		return isDuplicationDocVTRInIssue;
	}
	public PrjDocumentIndexVO isDuplicationDocSTRInIssue(PrjDocumentIndexVO prdocumentindexvo){
		PrjDocumentIndexVO isDuplicationDocSTRInIssue = selectOne("prjtrSqlMap.isDuplicationDocSTRInIssue",prdocumentindexvo);
		return isDuplicationDocSTRInIssue;
	}
	public PrjDocumentIndexVO isDuplicationDoc(PrjDocumentIndexVO prdocumentindexvo){
		PrjDocumentIndexVO isDuplicationDoc = selectOne("prjtrSqlMap.isDuplicationDoc",prdocumentindexvo);
		return isDuplicationDoc;
	}
	public PrjDocumentIndexVO isDuplicationDocVTR(PrjDocumentIndexVO prdocumentindexvo){
		PrjDocumentIndexVO isDuplicationDocVTR = selectOne("prjtrSqlMap.isDuplicationDocVTR",prdocumentindexvo);
		return isDuplicationDocVTR;
	}
	public PrjDocumentIndexVO isDuplicationDocSTR(PrjDocumentIndexVO prdocumentindexvo){
		PrjDocumentIndexVO isDuplicationDocSTR = selectOne("prjtrSqlMap.isDuplicationDocSTR",prdocumentindexvo);
		return isDuplicationDocSTR;
	}
	public int updateTrSendDate(PrjTrVO prjtrvo){
		int updateTrSendDate = update("prjtrSqlMap.updateTrSendDate",prjtrvo);
		return updateTrSendDate;
	}
	public int updateVTrSendDate(PrjTrVO prjtrvo){
		int updateVTrSendDate = update("prjtrSqlMap.updateVTrSendDate",prjtrvo);
		return updateVTrSendDate;
	}
	public int updateSTrSendDate(PrjTrVO prjtrvo){
		int updateSTrSendDate = update("prjtrSqlMap.updateSTrSendDate",prjtrvo);
		return updateSTrSendDate;
	}
	public PrjTrVO getIssueCodeByMailSending(PrjTrVO prjtrvo){
		PrjTrVO getIssueCodeByMailSending = selectOne("prjtrSqlMap.getIssueCodeByMailSending", prjtrvo);
		return getIssueCodeByMailSending;
	}
	public PrjTrVO getIssueCodeByMailSendingVTR(PrjTrVO prjtrvo){
		PrjTrVO getIssueCodeByMailSendingVTR = selectOne("prjtrSqlMap.getIssueCodeByMailSendingVTR", prjtrvo);
		return getIssueCodeByMailSendingVTR;
	}
	public PrjTrVO getIssueCodeByMailSendingSTR(PrjTrVO prjtrvo){
		PrjTrVO getIssueCodeByMailSendingSTR = selectOne("prjtrSqlMap.getIssueCodeByMailSendingSTR", prjtrvo);
		return getIssueCodeByMailSendingSTR;
	}
	public List<PrjTrVO> getSearchTRNo(PrjTrVO prjtrvo){
		List<PrjTrVO> getSearchTRNo = selectList("prjtrSqlMap.getSearchTRNo", prjtrvo);
		return getSearchTRNo;
	}
	public List<PrjTrVO> getSearchVTRNo(PrjTrVO prjtrvo){
		List<PrjTrVO> getSearchVTRNo = selectList("prjtrSqlMap.getSearchVTRNo", prjtrvo);
		return getSearchVTRNo;
	}
	public List<PrjTrVO> getSearchSTRNo(PrjTrVO prjtrvo){
		List<PrjTrVO> getSearchSTRNo = selectList("prjtrSqlMap.getSearchSTRNo", prjtrvo);
		return getSearchSTRNo;
	}
	public int updateRtnStatus(PrjTrVO prjtrvo){
		int updateRtnStatus = update("prjtrSqlMap.updateRtnStatus",prjtrvo);
		return updateRtnStatus;
	}
	public int updateRtnStatusVTR(PrjTrVO prjtrvo){
		int updateRtnStatusVTR = update("prjtrSqlMap.updateRtnStatusVTR",prjtrvo);
		return updateRtnStatusVTR;
	}
	public int updateRtnStatusSTR(PrjTrVO prjtrvo){
		int updateRtnStatusSTR = update("prjtrSqlMap.updateRtnStatusSTR",prjtrvo);
		return updateRtnStatusSTR;
	}

	public boolean incomingChkFile(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjDocumentIndexVO) throws Exception{
		boolean chkFile = true;
		if(prjDocumentIndexVO.getFile_value()!=null) {
			if(!prjDocumentIndexVO.getFile_value().equals("")) {	//file Attach 할 경우
				MultipartFile mf = mtRequest.getFile("trIncoming_File");
				File file = new File(mf.getOriginalFilename());
				chkFile = FileExtFilter.whiteFileExtIsReturnBoolean(file);
			}
		}
		return chkFile;
	}
	
	public String insertTrInfoForDRN(PrjTrVO prjtrvo){
		insert("prjtrSqlMap.insertTrInfoForDRN",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	
	public String insertVTrInfoForDRN(PrjTrVO prjtrvo) {
		insert("prjtrSqlMap.insertVTrInfoForDRN",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	
	public String insertSTrInfoForDRN(PrjTrVO prjtrvo) {
		insert("prjtrSqlMap.insertSTrInfoForDRN",prjtrvo);
		String tr_id = prjtrvo.getTr_id();
		return tr_id;
	}
	
	public int trCoverSaveChkFile(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjdocumentindexvo) throws Exception{
		int chkFile = 100;
		if(prjdocumentindexvo.getDoc_type().equals("TR")) {
			MultipartFile mf = mtRequest.getFile("trCover_File");
			File file = new File(mf.getOriginalFilename());
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.excelFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}
		}else if(prjdocumentindexvo.getDoc_type().equals("VTR")) {
			MultipartFile mf = mtRequest.getFile("vtrCover_File");
			File file = new File(mf.getOriginalFilename());
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.excelFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}
		}else if(prjdocumentindexvo.getDoc_type().equals("STR")) {
			MultipartFile mf = mtRequest.getFile("strCover_File");
			File file = new File(mf.getOriginalFilename());
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.excelFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}
		}
		return chkFile;
	}
}
