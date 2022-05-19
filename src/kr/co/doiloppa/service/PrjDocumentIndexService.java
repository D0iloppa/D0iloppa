package kr.co.doiloppa.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.model.CodeInfoVO;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocTrNoChgHistVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.model.WbsCodeControlVO;

@Service
public class PrjDocumentIndexService extends AbstractService {
	public List<PrjDocumentIndexVO> getPrjDocumentIndexList(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getPrjDocumentIndexList = selectList("prjdocumentindexSqlMap.getPrjDocumentIndexList", prjdocumentindexvo);
		return getPrjDocumentIndexList;
	}
	public List<PrjDocumentIndexVO> getPrjDocumentIndexListAll(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getPrjDocumentIndexListAll = selectList("prjdocumentindexSqlMap.getPrjDocumentIndexListAll", prjdocumentindexvo);
		return getPrjDocumentIndexListAll;
	}
	public List<PrjDocumentIndexVO> searchDocByFilter(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> searchDocByFilter = selectList("prjdocumentindexSqlMap.searchDocByFilter", prjdocumentindexvo);
		return searchDocByFilter;
	}
	public List<PrjDocumentIndexVO> searchDocByFilterAll(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> searchDocByFilterAll = selectList("prjdocumentindexSqlMap.searchDocByFilterAll", prjdocumentindexvo);
		return searchDocByFilterAll;
	}
	public int insertDocNoChgHist(PrjDocTrNoChgHistVO prjdoctrnochghistvo){
		int insertDocNoChgHist = insert("prjdocumentindexSqlMap.insertDocNoChgHist",prjdoctrnochghistvo);
		return insertDocNoChgHist;
	}
	public List<PrjDocTrNoChgHistVO> getDocNoChgHist(PrjDocTrNoChgHistVO prjdoctrnochghistvo){
		List<PrjDocTrNoChgHistVO> getDocNoChgHist = selectList("prjdocumentindexSqlMap.getDocNoChgHist", prjdoctrnochghistvo);
		return getDocNoChgHist;
	}
	public int getFolderIsContanisDoc(PrjDocumentIndexVO prjdocumentindexvo){
		int getFolderIsContanisDoc = selectOne("prjdocumentindexSqlMap.getFolderIsContanisDoc", prjdocumentindexvo);
		return getFolderIsContanisDoc;
	}
	public List<PrjDocumentIndexVO> getPoNoIsNull(){
		List<PrjDocumentIndexVO> getPoNoIsNull = selectList("prjdocumentindexSqlMap.getPoNoIsNull");
		return getPoNoIsNull;
	}
	public List<PrjDocumentIndexVO> getBulkPoNoIsNull(){
		List<PrjDocumentIndexVO> getBulkPoNoIsNull = selectList("prjdocumentindexSqlMap.getBulkPoNoIsNull");
		return getBulkPoNoIsNull;
	}
	public int updatePoNo(PrjDocumentIndexVO prjdocumentindexvo){
		int updatePoNo = update("prjdocumentindexSqlMap.updatePoNo",prjdocumentindexvo);
		return updatePoNo;
	}
	public int updateBulkPoNo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateBulkPoNo = update("prjdocumentindexSqlMap.updateBulkPoNo",prjdocumentindexvo);
		return updateBulkPoNo;
	}
	public String getDocNo(PrjDocumentIndexVO prjdocumentindexvo){
		String getDocNo = selectOne("prjdocumentindexSqlMap.getDocNo", prjdocumentindexvo);
		return getDocNo;
	}
	public int changeDocNo(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDocNo = update("prjdocumentindexSqlMap.changeDocNo",prjdocumentindexvo);
		return changeDocNo;
	}
	public int changeVendorPoDocNo(PrjDocumentIndexVO prjdocumentindexvo){
		int changeVendorPoDocNo = update("prjdocumentindexSqlMap.changeVendorPoDocNo",prjdocumentindexvo);
		return changeVendorPoDocNo;
	}
	public int DRNisReject(PrjDocumentIndexVO prjdocumentindexvo){
		int DRNisReject = selectOne("prjdocumentindexSqlMap.DRNisReject", prjdocumentindexvo);
		return DRNisReject;
	}
	public int documentCheckOut(PrjDocumentIndexVO prjdocumentindexvo){
		int documentCheckOut = update("prjdocumentindexSqlMap.documentCheckOut",prjdocumentindexvo);
		return documentCheckOut;
	}
	public int documentCheckOutCancel(PrjDocumentIndexVO prjdocumentindexvo){
		int documentCheckOutCancel = update("prjdocumentindexSqlMap.documentCheckOutCancel",prjdocumentindexvo);
		return documentCheckOutCancel;
	}
	public int documentDelete(PrjDocumentIndexVO prjdocumentindexvo){
		int documentDelete = update("prjdocumentindexSqlMap.documentDelete",prjdocumentindexvo);
		return documentDelete;
	}
	public int changeDesignerEng(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDesignerEng = update("prjdocumentindexSqlMap.changeDesignerEng",prjdocumentindexvo);
		return changeDesignerEng;
	}
	public int changeDesignerVendor(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDesignerVendor = update("prjdocumentindexSqlMap.changeDesignerVendor",prjdocumentindexvo);
		return changeDesignerVendor;
	}
	public int changeDesignerProc(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDesignerProc = update("prjdocumentindexSqlMap.changeDesignerProc",prjdocumentindexvo);
		return changeDesignerProc;
	}
	public int changeDesignerBulk(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDesignerBulk = update("prjdocumentindexSqlMap.changeDesignerBulk",prjdocumentindexvo);
		return changeDesignerBulk;
	}
	public int changeDesignerSite(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDesignerSite = update("prjdocumentindexSqlMap.changeDesignerSite",prjdocumentindexvo);
		return changeDesignerSite;
	}
	public int changeDesignerCorr(PrjDocumentIndexVO prjdocumentindexvo){
		int changeDesignerCorr = update("prjdocumentindexSqlMap.changeDesignerCorr",prjdocumentindexvo);
		return changeDesignerCorr;
	}
	public PrjDocumentIndexVO getDownloadFileInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getDownloadFileInfo = selectOne("prjdocumentindexSqlMap.getDownloadFileInfo", prjdocumentindexvo);
		return getDownloadFileInfo;
	}
	public List<PrjDocumentIndexVO> getTRFileInfo(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getTRFileInfo = selectList("prjdocumentindexSqlMap.getTRFileInfo", prjdocumentindexvo);
		return getTRFileInfo;
	}
	public List<PrjDocumentIndexVO> getVTRFileInfo(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getVTRFileInfo = selectList("prjdocumentindexSqlMap.getVTRFileInfo", prjdocumentindexvo);
		return getVTRFileInfo;
	}
	public List<PrjDocumentIndexVO> getSTRFileInfo(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getSTRFileInfo = selectList("prjdocumentindexSqlMap.getSTRFileInfo", prjdocumentindexvo);
		return getSTRFileInfo;
	}
	public PrjDocumentIndexVO getDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getDocumentInfo = selectOne("prjdocumentindexSqlMap.getDocumentInfo", prjdocumentindexvo);
		return getDocumentInfo;
	}
	public PrjDocumentIndexVO getVendorDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getVendorDocumentInfo = selectOne("prjdocumentindexSqlMap.getVendorDocumentInfo", prjdocumentindexvo);
		return getVendorDocumentInfo;
	}
	public PrjDocumentIndexVO getProcDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getProcDocumentInfo = selectOne("prjdocumentindexSqlMap.getProcDocumentInfo", prjdocumentindexvo);
		return getProcDocumentInfo;
	}
	public PrjDocumentIndexVO getBulkDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getBulkDocumentInfo = selectOne("prjdocumentindexSqlMap.getBulkDocumentInfo", prjdocumentindexvo);
		return getBulkDocumentInfo;
	}
	public PrjDocumentIndexVO getSiteDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getSiteDocumentInfo = selectOne("prjdocumentindexSqlMap.getSiteDocumentInfo", prjdocumentindexvo);
		return getSiteDocumentInfo;
	}
	public PrjDocumentIndexVO getRefGrp(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getRefGrp = selectOne("prjdocumentindexSqlMap.getRefGrp",prjdocumentindexvo);
		return getRefGrp;
	}
	public int updateRefGrp(PrjDocumentIndexVO prjdocumentindexvo){
		int updateRefGrp = update("prjdocumentindexSqlMap.updateRefGrp",prjdocumentindexvo);
		return updateRefGrp;
	}
	public PrjDocumentIndexVO getCorrDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getCorrDocumentInfo = selectOne("prjdocumentindexSqlMap.getCorrDocumentInfo", prjdocumentindexvo);
		return getCorrDocumentInfo;
	}
	public PrjDocumentIndexVO getGeneralDocumentInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getGeneralDocumentInfo = selectOne("prjdocumentindexSqlMap.getGeneralDocumentInfo", prjdocumentindexvo);
		return getGeneralDocumentInfo;
	}
	public String getCorrDocNo(PrjDocumentIndexVO prjdocumentindexvo){
		String getCorrDocNo = selectOne("prjdocumentindexSqlMap.getCorrDocNo", prjdocumentindexvo);
		return getCorrDocNo;
	}
	public int getCorrSerialLength(PrjDocumentIndexVO prjdocumentindexvo){
		int getCorrSerialLength = selectOne("prjdocumentindexSqlMap.getCorrSerialLength",prjdocumentindexvo);
		return getCorrSerialLength;
	}
	public String getCorrSerial(PrjDocumentIndexVO prjdocumentindexvo){
		String serial = selectOne("prjdocumentindexSqlMap.getCorrSerial",prjdocumentindexvo);
		return serial;
	}
	public String insertDocumentIndex(PrjDocumentIndexVO prjdocumentindexvo){
		insert("prjdocumentindexSqlMap.insertDocumentIndex",prjdocumentindexvo);
		String doc_id = prjdocumentindexvo.getDoc_id();
		return doc_id;
	}
	public String insertDocumentIndexTRCover(PrjDocumentIndexVO prjdocumentindexvo){
		insert("prjdocumentindexSqlMap.insertDocumentIndexTRCover",prjdocumentindexvo);
		String doc_id = prjdocumentindexvo.getDoc_id();
		return doc_id;
	}
	public int insertCopyDocumentIndex(PrjDocumentIndexVO prjdocumentindexvo){
		int insertCopyDocumentIndex = insert("prjdocumentindexSqlMap.insertCopyDocumentIndex",prjdocumentindexvo);
		return insertCopyDocumentIndex;
	}
	public int updateDocumentIndex(PrjDocumentIndexVO prjdocumentindexvo){
		int updateDocumentIndex = update("prjdocumentindexSqlMap.updateDocumentIndex",prjdocumentindexvo);
		return updateDocumentIndex;
	}
	public int updateDocumentIndexFileAttach(PrjDocumentIndexVO prjdocumentindexvo){
		int updateDocumentIndexFileAttach = update("prjdocumentindexSqlMap.updateDocumentIndexFileAttach",prjdocumentindexvo);
		return updateDocumentIndexFileAttach;
	}
	public int updateDocumentIndexFileAttachProcAndBulk(PrjDocumentIndexVO prjdocumentindexvo){
		int updateDocumentIndexFileAttachProcAndBulk = update("prjdocumentindexSqlMap.updateDocumentIndexFileAttachProcAndBulk",prjdocumentindexvo);
		return updateDocumentIndexFileAttachProcAndBulk;
	}
	public String insertDocumentIndexForCorr(PrjDocumentIndexVO prjdocumentindexvo){
		insert("prjdocumentindexSqlMap.insertDocumentIndexForCorr",prjdocumentindexvo);
		String doc_id = prjdocumentindexvo.getDoc_id();
		return doc_id;
	}
	public int updateDocumentIndexForCorr(PrjDocumentIndexVO prjdocumentindexvo){
		int updateDocumentIndex = update("prjdocumentindexSqlMap.updateDocumentIndexForCorr",prjdocumentindexvo);
		return updateDocumentIndex;
	}
	public String updateRevisionOfDocumentIndex(PrjDocumentIndexVO prjdocumentindexvo){
		insert("prjdocumentindexSqlMap.updateRevisionOfDocumentIndex",prjdocumentindexvo);
		String rev_id = prjdocumentindexvo.getRev_id();
		return rev_id;
	}
	public int insertEngInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertEngInfo = insert("prjdocumentindexSqlMap.insertEngInfo",prjdocumentindexvo);
		return insertEngInfo;
	}
	public int updateEngInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateEngInfo = update("prjdocumentindexSqlMap.updateEngInfo",prjdocumentindexvo);
		return updateEngInfo;
	}
	public int insertEngInfoForUpdateRevision(PrjDocumentIndexVO prjdocumentindexvo){
		int insertEngInfoForUpdateRevision = insert("prjdocumentindexSqlMap.insertEngInfoForUpdateRevision",prjdocumentindexvo);
		return insertEngInfoForUpdateRevision;
	}
	public int insertVendorInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertVendorInfo = insert("prjdocumentindexSqlMap.insertVendorInfo",prjdocumentindexvo);
		return insertVendorInfo;
	}
	public int updateVendorInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateVendorInfo = update("prjdocumentindexSqlMap.updateVendorInfo",prjdocumentindexvo);
		return updateVendorInfo;
	}
	public int insertVendorInfoForUpdateRevision(PrjDocumentIndexVO prjdocumentindexvo){
		int insertVendorInfoForUpdateRevision = insert("prjdocumentindexSqlMap.insertVendorInfoForUpdateRevision",prjdocumentindexvo);
		return insertVendorInfoForUpdateRevision;
	}
	public int insertProcInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertProcInfo = insert("prjdocumentindexSqlMap.insertProcInfo",prjdocumentindexvo);
		return insertProcInfo;
	}
	public int updateProcInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateProcInfo = update("prjdocumentindexSqlMap.updateProcInfo",prjdocumentindexvo);
		return updateProcInfo;
	}
	public int insertProcInfoForUpdateRevision(PrjDocumentIndexVO prjdocumentindexvo){
		int insertProcInfoForUpdateRevision = insert("prjdocumentindexSqlMap.insertProcInfoForUpdateRevision",prjdocumentindexvo);
		return insertProcInfoForUpdateRevision;
	}
	public int insertBulkInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertBulkInfo = insert("prjdocumentindexSqlMap.insertBulkInfo",prjdocumentindexvo);
		return insertBulkInfo;
	}
	public int updateBulkInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateBulkInfo = update("prjdocumentindexSqlMap.updateBulkInfo",prjdocumentindexvo);
		return updateBulkInfo;
	}
	public int insertBulkInfoForUpdateRevision(PrjDocumentIndexVO prjdocumentindexvo){
		int insertBulkInfoForUpdateRevision = insert("prjdocumentindexSqlMap.insertBulkInfoForUpdateRevision",prjdocumentindexvo);
		return insertBulkInfoForUpdateRevision;
	}
	public int insertSiteInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertSiteInfo = insert("prjdocumentindexSqlMap.insertSiteInfo",prjdocumentindexvo);
		return insertSiteInfo;
	}
	public int updateSiteInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateSiteInfo = update("prjdocumentindexSqlMap.updateSiteInfo",prjdocumentindexvo);
		return updateSiteInfo;
	}
	public int insertSiteInfoForUpdateRevision(PrjDocumentIndexVO prjdocumentindexvo){
		int insertSiteInfoForUpdateRevision = insert("prjdocumentindexSqlMap.insertSiteInfoForUpdateRevision",prjdocumentindexvo);
		return insertSiteInfoForUpdateRevision;
	}
	public int insertCorrInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertCorrInfo = insert("prjdocumentindexSqlMap.insertCorrInfo",prjdocumentindexvo);
		return insertCorrInfo;
	}
	public int updateCorrInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateCorrInfo = update("prjdocumentindexSqlMap.updateCorrInfo",prjdocumentindexvo);
		return updateCorrInfo;
	}
	public int updateCorrInfoSendDateByFirstEmailing(PrjDocumentIndexVO prjdocumentindexvo){
		int updateCorrInfoSendDateByFirstEmailing = update("prjdocumentindexSqlMap.updateCorrInfoSendDateByFirstEmailing",prjdocumentindexvo);
		return updateCorrInfoSendDateByFirstEmailing;
	}
	public int insertGeneralInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int insertGeneralInfo = insert("prjdocumentindexSqlMap.insertGeneralInfo",prjdocumentindexvo);
		return insertGeneralInfo;
	}
	public int updateGeneralInfo(PrjDocumentIndexVO prjdocumentindexvo){
		int updateGeneralInfo = update("prjdocumentindexSqlMap.updateGeneralInfo",prjdocumentindexvo);
		return updateGeneralInfo;
	}
	public int insertGeneralInfoForUpdateRevision(PrjDocumentIndexVO prjdocumentindexvo){
		int insertGeneralInfoForUpdateRevision = insert("prjdocumentindexSqlMap.insertGeneralInfoForUpdateRevision",prjdocumentindexvo);
		return insertGeneralInfoForUpdateRevision;
	}
	public int getCorrSerialMax(PrjDocumentIndexVO prjdocumentindexvo){
		int getCorrSerialMax = selectOne("prjdocumentindexSqlMap.getCorrSerialMax",prjdocumentindexvo);
		return getCorrSerialMax;
	}
	public List<PrjDocumentIndexVO> getCorrRefDocList(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getCorrRefDocList = selectList("prjdocumentindexSqlMap.getCorrRefDocList", prjdocumentindexvo);
		return getCorrRefDocList;
	}
	public List<PrjDocumentIndexVO> getCorrHistory(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getCorrHistory = selectList("prjdocumentindexSqlMap.getCorrHistory", prjdocumentindexvo);
		return getCorrHistory;
	}
	public List<PrjDocumentIndexVO> getCorrNoSearch(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getCorrNoSearch = selectList("prjdocumentindexSqlMap.getCorrNoSearch", prjdocumentindexvo);
		return getCorrNoSearch;
	}
	public int getCorrSerialExistYN(PrjDocumentIndexVO prjdocumentindexvo){
		int getCorrSerialExistYN = selectOne("prjdocumentindexSqlMap.getCorrSerialExistYN",prjdocumentindexvo);
		return getCorrSerialExistYN;
	}
	public int deleteDocumentInTRAFolder(PrjDocumentIndexVO prjdocumentindexvo){
		int deleteDocumentInTRAFolder = delete("prjdocumentindexSqlMap.deleteDocumentInTRAFolder",prjdocumentindexvo);
		return deleteDocumentInTRAFolder;
	}
	public List<PrjDocumentIndexVO> getUsedRevCodeId(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getUsedRevCodeId = selectList("prjdocumentindexSqlMap.getUsedRevCodeId",prjdocumentindexvo);
		return getUsedRevCodeId;
	}
	
	public String getDisciplineDisplay(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<String> result = selectList("prjdocumentindexSqlMap.getDiscipline_DRN",prjDocumentIndexVO);
		
		// 찾은 폴더가 없는 경우 null return
		if(result.size() == 0) return "";
		// 여러 discipline의 상위폴더의 경우 null return
		if(result.size()>1) return "";
		
		return result.get(0);
	}
	public int updateDCI(PrjDocumentIndexVO prjDocumentIndexVO) {
		
		PrjDocumentIndexVO vo = new PrjDocumentIndexVO();
		
		String category_cd = prjDocumentIndexVO.getCategory_cd_id();
		String size_code = prjDocumentIndexVO.getSize_code_id();
		String doc_type = prjDocumentIndexVO.getDoc_type_id();
		String wbs_code = prjDocumentIndexVO.getWbs_code_id();
		
		vo.setTbl_type(prjDocumentIndexVO.getTbl_type());
		vo.setDesigner_id(prjDocumentIndexVO.getDesigner_id());
		vo.setCategory_cd(category_cd);
		vo.setSize_code(size_code);
		vo.setDoc_type(doc_type);
		vo.setWbs_code(wbs_code);
		vo.setRemark(prjDocumentIndexVO.getRemark());
		vo.setPrj_id(prjDocumentIndexVO.getPrj_id());
		vo.setDoc_id(prjDocumentIndexVO.getDoc_id());
		vo.setRev_id(prjDocumentIndexVO.getRev_id());
		
		int updateDCI = 0;
		if(vo.getTbl_type().contains("p_prj_mdc_info") || vo.getTbl_type().contains("p_prj_pro_info")) {
			// mci는 category_cd_id 없음
			updateDCI = update("prjdocumentindexSqlMap.updateMCI", vo);
		}else {
			updateDCI = update("prjdocumentindexSqlMap.updateDCI", vo);
		}
		
		
		return updateDCI;
	}

	public List<PrjDocumentIndexVO> getDciList(PrjDocumentIndexVO prjDocumentIndexVO) {
	
		String doc_type = prjDocumentIndexVO.getDoc_type();

		switch (doc_type) {
			case "DCI":
				return selectList("prjdocumentindexSqlMap.getDciList_DCI", prjDocumentIndexVO);
			case "VDCI":
				return selectList("prjdocumentindexSqlMap.getDciList_VDCI", prjDocumentIndexVO);
			case "SDCI":
				return selectList("prjdocumentindexSqlMap.getDciList_SDCI", prjDocumentIndexVO);
			case "PCI":
				return selectList("prjdocumentindexSqlMap.getDciList_PCI", prjDocumentIndexVO);
			case "MCI":
				return selectList("prjdocumentindexSqlMap.getDciList_MCI", prjDocumentIndexVO);
			default:
				break;
		}

		return null;
		
		
	}
	
	public List<String> getStepField(PrjDocumentIndexVO prjDocumentIndexVO) {
		String docType = prjDocumentIndexVO.getDoc_type();
		String tbl = "";
		
		switch(docType) {
		case "DCI":
			tbl = "ENGINEERING STEP";
			break;
		case "VDCI":
			tbl = "VENDOR DOC STEP";
			break;
		case "SDCI":
			tbl = "SITE DOC STEP";
			break;
		case "PCI":
			tbl = "PROCUREMENT STEP";
			break;
		case "MCI":
			tbl = "BULK STEP";
			break;
		default :
			return null;
		}
		
		
		prjDocumentIndexVO.setSet_code_type(tbl);
		List<String> result = selectList("prjdocumentindexSqlMap.getStepField",prjDocumentIndexVO); 
		
		return result;
	}
	
	public List<PrjDocumentIndexVO> getStepDate(PrjDocumentIndexVO prjDocumentIndexVO) {
		String docType = prjDocumentIndexVO.getDoc_type();
		String tbl = "";
		String code_type = "";
		
		switch(docType) {
		case "DCI":
			tbl = "p_prj_eng_step";
			code_type = "ENGINEERING STEP";
			break;
		case "VDCI":
			tbl = "p_prj_vdr_step";
			code_type = "VENDOR DOC STEP";
			break;
		case "SDCI":
			tbl = "p_prj_sdc_step";
			code_type = "SITE DOC STEP";
			break;
		case "PCI":
			tbl = "p_prj_pro_step";
			code_type = "PROCUREMENT STEP";
			break;
		case "MCI":
			tbl = "p_prj_mdc_step";
			code_type = "BULK STEP";
			break;
		
		}
		
		prjDocumentIndexVO.setTbl_type(tbl);
		prjDocumentIndexVO.setSet_code_type(code_type);
		
		return selectList("prjdocumentindexSqlMap.getStepDate",prjDocumentIndexVO);
	}

	public int getMaxDocId(PrjDocumentIndexVO prjDocumentIndexVO) {
		Integer result = selectOne("prjdocumentindexSqlMap.getMaxDocId",prjDocumentIndexVO);
		if(result == null) return 1;
		return result;
	}

	public List<PrjDocumentIndexVO> isInDocNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectList("prjdocumentindexSqlMap.isInDocNo",prjDocumentIndexVO);
	}
	
	public List<PrjDocumentIndexVO> isInDocNoOtherFolder(PrjDocumentIndexVO prjDocumentIndexVO) {
		
		String doc_type = prjDocumentIndexVO.getDoc_type();
		
		switch(doc_type) {
			case "DCI":
			case "VDCI":
			case "SDCI":
				return selectList("prjdocumentindexSqlMap.isInDocNoOtherFolder_Primary",prjDocumentIndexVO);
			
			// MCI, PCI 타입은 폴더내에서의 doc_no만 따짐 이외에는 상관 X
			case "MCI":
			case "PCI":
				List<PrjDocumentIndexVO> outList = new ArrayList<>();
				return outList;
				//return selectList("prjdocumentindexSqlMap.isInDocNoOtherFolder_Secondary",prjDocumentIndexVO);
			
			default:
				break;
		}
		
		
		return selectList("prjdocumentindexSqlMap.isInDocNoOtherFolder",prjDocumentIndexVO);
	}

	public int insertDCI(PrjDocumentIndexVO prjDocumentIndexVO) {
		String category = prjDocumentIndexVO.getCategory_cd_id();
		if(category == null) prjDocumentIndexVO.setCategory_cd_id(prjDocumentIndexVO.getCategory_cd());
		String size = prjDocumentIndexVO.getSize_code_id();
		if(size == null) prjDocumentIndexVO.setSize_code_id(prjDocumentIndexVO.getSize_code());
		
		if(prjDocumentIndexVO.getTbl_type().contains("p_prj_mdc_info") || prjDocumentIndexVO.getTbl_type().contains("p_prj_pro_info")) {
			return insert("prjdocumentindexSqlMap.insertMCI", prjDocumentIndexVO);
		}
		
		return insert("prjdocumentindexSqlMap.insertDCI", prjDocumentIndexVO);
	}

	public int insertDCI2(PrjDocumentIndexVO prjDocumentIndexVO) {
		return insert("prjdocumentindexSqlMap.insertDCI2", prjDocumentIndexVO);
	}
	
	public int insertDCI_step(PrjDocumentIndexVO prjDocumentIndexVO) {
  		return insert("prjdocumentindexSqlMap.insertDCI_step", prjDocumentIndexVO);
	}
	
	public List<PrjCodeSettingsVO> getRevNo(PrjInfoVO prjInfo) {
		return selectList("prjdocumentindexSqlMap.getRevNo",prjInfo);
	}
	
	public List<PrjCodeSettingsVO> getIFCReason(PrjInfoVO prjInfo) {
		return selectList("prjdocumentindexSqlMap.getIFCReason",prjInfo);
	}

	public List<String> getUsedRevNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectList("prjdocumentindexSqlMap.getUsedRevNo",prjDocumentIndexVO);
	}

	public List<WbsCodeControlVO> getWBS(WbsCodeControlVO wbsCodeControlVO) { 
		return selectList("wbscodecontrolSqlMap.getWbscodeListAll",wbsCodeControlVO);
	}
	
	public List<PrjCodeSettingsVO> getDocSize(PrjCodeSettingsVO codeVo) {
		return selectList("prjdocumentindexSqlMap.getDocSize",codeVo);
	}

	public List<PrjCodeSettingsVO> getDocCate(PrjCodeSettingsVO codeVo) {
		return selectList("prjdocumentindexSqlMap.getDocCate",codeVo);
	}
	public List<PrjCodeSettingsVO> getDocType(PrjCodeSettingsVO codeVo) {
		// TODO Auto-generated method stub
		return selectList("prjdocumentindexSqlMap.getDocType",codeVo);
	}
	
	public List<String> getStepCode(PrjDocumentIndexVO prjDocumentIndexVO) {
		String docType = prjDocumentIndexVO.getDoc_type();
		String tbl = "";
		
		switch(docType) {
		case "DCI":
			tbl = "ENGINEERING STEP";
			break;
		case "VDCI":
			tbl = "VENDOR DOC STEP";
			break;
		case "SDCI":
			tbl = "SITE DOC STEP";
			break;
		case "PCI":
			tbl = "PROCUREMENT STEP";
			break;
		case "MCI":
			tbl = "BULK STEP";
			break;
		
		}
		prjDocumentIndexVO.setSet_code_type(tbl);
		List<String> result = selectList("prjdocumentindexSqlMap.getStepCode",prjDocumentIndexVO);
		
		return result;
	}
	
	public UsersVO isInDesigner(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectOne("prjdocumentindexSqlMap.isInDesigner",prjDocumentIndexVO);		
	}
	
	public int fileUpload(PrjDocumentIndexVO prjDocumentIndexVO) {
		String doc_no = prjDocumentIndexVO.getDoc_no();
		if(doc_no.length()>30) doc_no = doc_no.substring(0,30);
		prjDocumentIndexVO.setDoc_no(doc_no);
		return insert("prjdocumentindexSqlMap.fileUpload", prjDocumentIndexVO);		
	}
	
	public int checkIn(PrjDocumentIndexVO prjDocumentIndexVO) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();

		String attach_file_date = sdf.format(c1.getTime());
		
		prjDocumentIndexVO.setAttach_file_date(attach_file_date);
		
		// 로그인한 유저
		String mod_id = prjDocumentIndexVO.getReg_id();
		prjDocumentIndexVO.setMod_id(mod_id);
		
		String reg_id = selectOne("prjdocumentindexSqlMap.getRegId",prjDocumentIndexVO);
		if(reg_id != null && !reg_id.equals("")) prjDocumentIndexVO.setReg_id(reg_id);
		String doc_type = prjDocumentIndexVO.getDoc_type();
		if(doc_type.equals("")) prjDocumentIndexVO.setDoc_type("General");
		
		return insert("prjdocumentindexSqlMap.checkIn", prjDocumentIndexVO);
	}
	
	public int getMaxRevId(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectOne("prjdocumentindexSqlMap.getMaxRevId",prjDocumentIndexVO);
	}
	
	public List<ProcessInfoVO> getProcessDocType(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectList("prjdocumentindexSqlMap.getProcessDocType",prjDocumentIndexVO);
	}
	public int isReadDoc(PrjDocumentIndexVO prjDocumentIndexVO) {
		int isReadDoc = selectOne("prjdocumentindexSqlMap.isReadDoc",prjDocumentIndexVO);
		return isReadDoc;
	}
	public int insertReadDoc(PrjDocumentIndexVO prjDocumentIndexVO) {
		int insertReadDoc = insert("prjdocumentindexSqlMap.insertReadDoc",prjDocumentIndexVO);
		return insertReadDoc;
	}
	public int isDuplicateDocNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		int isDuplicateDocNo = selectOne("prjdocumentindexSqlMap.isDuplicateDocNo",prjDocumentIndexVO);
		return isDuplicateDocNo;
	}
	public int isDuplicateDocNoChange(PrjDocumentIndexVO prjDocumentIndexVO) {
		int isDuplicateDocNoChange = selectOne("prjdocumentindexSqlMap.isDuplicateDocNoChange",prjDocumentIndexVO);
		return isDuplicateDocNoChange;
	}
	public int isDuplicateOtherDocNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		int isDuplicateOtherDocNo = selectOne("prjdocumentindexSqlMap.isDuplicateOtherDocNo",prjDocumentIndexVO);
		return isDuplicateOtherDocNo;
	}
	public int isDuplicateOtherDocNoChange(PrjDocumentIndexVO prjDocumentIndexVO) {
		int isDuplicateOtherDocNoChange = selectOne("prjdocumentindexSqlMap.isDuplicateOtherDocNoChange",prjDocumentIndexVO);
		return isDuplicateOtherDocNoChange;
	}
	public PrjDocumentIndexVO getDocIdByDocNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		PrjDocumentIndexVO getDocIdByDocNo = selectOne("prjdocumentindexSqlMap.getDocIdByDocNo",prjDocumentIndexVO);
		return getDocIdByDocNo;
	}
	public PrjDocumentIndexVO isExistDocNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		PrjDocumentIndexVO isExistDocNo = selectOne("prjdocumentindexSqlMap.isExistDocNo",prjDocumentIndexVO);
		return isExistDocNo;
	}
	public List<PrjDocumentIndexVO> isExistsDocNo(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<PrjDocumentIndexVO> isExistsDocNo = selectList("prjdocumentindexSqlMap.isExistsDocNo",prjDocumentIndexVO);
		return isExistsDocNo;
	}
	public List<PrjDocumentIndexVO> getProcList(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<PrjDocumentIndexVO> getProcList = selectList("prjdocumentindexSqlMap.getProcList",prjDocumentIndexVO);
		return getProcList;
	}
	public int deleteDocumentIndexBydeleteTrIncoming(PrjDocumentIndexVO prjdocumentindexvo){
		int deleteDocumentIndexBydeleteTrIncoming = insert("prjdocumentindexSqlMap.deleteDocumentIndexBydeleteTrIncoming",prjdocumentindexvo);
		return deleteDocumentIndexBydeleteTrIncoming;
	}

	public String getDiscipCodeId(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectOne("prjdocumentindexSqlMap.getDiscipCodeId",prjDocumentIndexVO);
	}
	public String getDocument_DocType(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectOne("prjdocumentindexSqlMap.getDocument_DocType",prjDocumentIndexVO);
	}
	public int updateDCI_step(PrjDocumentIndexVO tmpBean) {
		return update("prjdocumentindexSqlMap.updateDCI_step",tmpBean);
	}

	public int fileUploadInfoTbl(PrjDocumentIndexVO prjDocumentIndexVO) {
		String tbl_type = prjDocumentIndexVO.getDoc_type();
		
		switch(tbl_type) {
		case "DCI":
			tbl_type =  "p_prj_eng_info";
			break;
		case "VDCI":
			tbl_type = "p_prj_vdr_info";
			break;
		case "SDCI":
			tbl_type = "p_prj_sdc_info";
			break;
		case "PCI":
			tbl_type = "p_prj_pro_info";
			break;
		case "General":
			
			return insert("prjdocumentindexSqlMap.insertGenDoc", prjDocumentIndexVO);
		}
		
		prjDocumentIndexVO.setDesigner_id(prjDocumentIndexVO.getReg_id());
		prjDocumentIndexVO.setPrfe_designer_id(prjDocumentIndexVO.getReg_id());
		
		prjDocumentIndexVO.setTbl_type(tbl_type);
		
		
		return insert("prjdocumentindexSqlMap.insertDCI", prjDocumentIndexVO);
		
	}

	public List<PrjDocumentIndexVO> getRevNoList(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectList("prjdocumentindexSqlMap.getRevNoList",prjDocumentIndexVO);
	}

	public int checkIn2(PrjDocumentIndexVO prjDocumentIndexVO) {
		String tbl_type = prjDocumentIndexVO.getTbl_type();
		switch(tbl_type) {
			case "DCI":
				return insert("prjdocumentindexSqlMap.checkInDCI",prjDocumentIndexVO);
			case "VDCI":
				return insert("prjdocumentindexSqlMap.checkInVDCI",prjDocumentIndexVO);
			case "SDCI":
				return insert("prjdocumentindexSqlMap.checkInSDCI",prjDocumentIndexVO);
			case "PCI":
				return insert("prjdocumentindexSqlMap.checkInPCI",prjDocumentIndexVO);
			case "MCI":
				return insert("prjdocumentindexSqlMap.checkInMCI",prjDocumentIndexVO);
			case "General":
				return insert("prjdocumentindexSqlMap.checkInGEN",prjDocumentIndexVO);
			default :
				return insert("prjdocumentindexSqlMap.checkInGEN",prjDocumentIndexVO);
		}
		
	}
	
	public PrjDocumentIndexVO getPreInfo(PrjDocumentIndexVO prjDocumentIndexVO) {
		String tbl_type = prjDocumentIndexVO.getTbl_type();
		switch(tbl_type) {
		case "DCI":
			prjDocumentIndexVO.setTbl_type("p_prj_eng_info");
			return selectOne("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "VDCI":
			prjDocumentIndexVO.setTbl_type("p_prj_vdr_info");
			return selectOne("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "SDCI":
			prjDocumentIndexVO.setTbl_type("p_prj_sdc_info");
			return selectOne("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "PCI":
			prjDocumentIndexVO.setTbl_type("p_prj_pro_info");
			return selectOne("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "MCI":
			prjDocumentIndexVO.setTbl_type("p_prj_mdc_info");
			return selectOne("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "General":
			prjDocumentIndexVO.setTbl_type("p_prj_gendoc_info");
			return selectOne("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		default :
			return prjDocumentIndexVO;
		}
	}
	
	public List<PrjDocumentIndexVO> getPreStepInfo(PrjDocumentIndexVO prjDocumentIndexVO) {
		String tbl_type = prjDocumentIndexVO.getDoc_type();

		switch(tbl_type) {
		case "DCI":
			prjDocumentIndexVO.setTbl_type("p_prj_eng_step");
			return selectList("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "VDCI":
			prjDocumentIndexVO.setTbl_type("p_prj_vdr_step");
			return selectList("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "SDCI":
			prjDocumentIndexVO.setTbl_type("p_prj_sdc_step");
			return selectList("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		case "PCI":
			prjDocumentIndexVO.setTbl_type("p_prj_pro_step");
			return selectList("prjdocumentindexSqlMap.getPreInfo",prjDocumentIndexVO);
		default :
			return null;
		}
	}
	
	public int insertNextStep(PrjDocumentIndexVO step) {
		String tbl_type = step.getTbl_type();
		
		switch(tbl_type) {
		case "DCI":
			return insert("prjdocumentindexSqlMap.insertEngNextStep",step);
		case "VDCI":
			return insert("prjdocumentindexSqlMap.insertVdrNextStep",step);
		case "SDCI":
			return insert("prjdocumentindexSqlMap.insertSdcNextStep",step);
		case "PCI":
			return insert("prjdocumentindexSqlMap.insertProNextStep",step);
		default :
			return 0;
		}
	}

	public PrjDocumentIndexVO getActualDateRuleAtCheckIn(PrjDocumentIndexVO step) {
		return selectOne("prjdocumentindexSqlMap.getActualDateRuleAtCheckIn",step);
	}
	

	public String getActualDate4ForeDate(List<PrjDocumentIndexVO> preStepInfo, String code_id) {
		String actual_date = null;
		for(PrjDocumentIndexVO step: preStepInfo) {
			if(step.getStep_code_id().equals(code_id)) {
				actual_date = step.getActual_date();
				break;
			}
		}
		
		return actual_date;
	}
		
	public String isIFCStep(PrjDocumentIndexVO prjdocumentindexvo) {
		
		return selectOne("prjdocumentindexSqlMap.isIFCStep",prjdocumentindexvo);
	}
		
	public ModelAndView authCheck(PrjDocumentIndexVO prjDocumentIndexVO) {
		ModelAndView result = new ModelAndView();

		PrjInfoVO prjinfovo = new PrjInfoVO();
		prjinfovo.setUser_id(prjDocumentIndexVO.getReg_id());
		prjinfovo.setPrj_id(prjDocumentIndexVO.getPrj_id());
		prjinfovo.setFolder_id(prjDocumentIndexVO.getFolder_id());
		
	
		// 관리자 여부 확인
		String isAdmin = selectOne("folderinfoSqlMap.isAdmin",prjinfovo.getUser_id());
		if(isAdmin == null) isAdmin = "N";
		// DCC 여부 확인
		String isDcc = selectOne("folderinfoSqlMap.isDcc",prjinfovo);
		if(isDcc == null) isDcc = "N";
		
		
		FolderInfoVO infoFromSession = new FolderInfoVO();
		infoFromSession.setUser_id(prjinfovo.getUser_id());
		infoFromSession.setPrj_id(prjinfovo.getPrj_id());
		infoFromSession.setFolder_id(prjinfovo.getFolder_id());

		// 세션의 user_id, org_id, grp_id를 구해야 한다
		// 조직과 그룹은 순수 id가 아닌 path값을 구해야함

		List<String> org_Path = selectList("folderinfoSqlMap.getOrgPath",infoFromSession.getUser_id());

		if (org_Path != null) {
			for (String org_path : org_Path) {
				infoFromSession.setOrg_id(org_path);
			}
		}

		List<String> grp_Id = selectList("folderinfoSqlMap.getGrpPath",infoFromSession);
		if (grp_Id != null) {
			for (String grp_id : grp_Id) {
				infoFromSession.setGrp_id(grp_id);
			}
		}
		FolderInfoVO getFolderWithAuth = selectOne("folderinfoSqlMap.getFolderWithAuth",infoFromSession);

		String useDRN = "N";
		List<String> isUsingDRN = selectList("prjinfoSqlMap.isUsingDRN",getFolderWithAuth); 
		
		if (isUsingDRN.size() > 0)
			useDRN = isUsingDRN.get(0);

		if (useDRN == null)
			useDRN = "N";

		// 대문자로 캐스팅
		useDRN = useDRN.toUpperCase();

		getFolderWithAuth.setUseDRN(useDRN);

		// 매핑된 권한이 없을 때의 기본 권한
		String auth_r = "N";
		String auth_w = "N";
		String auth_d = "N";
		
		
		// 권한 체크
		// 1) 해당 폴더에 셋팅된 유저권한이 있는지 확인
		// 2) 해당 폴더에 셋팅된 조직권한이 있는지 확인
		// 3) 해당 폴더에 셋팅된 그룹권한이 있는지 확인
	    // 읽기권한
		if(getFolderWithAuth.getUser_r() != null) { auth_r = getFolderWithAuth.getUser_r(); }
		else if(getFolderWithAuth.getOrg_r() != null) { auth_r = getFolderWithAuth.getOrg_r(); }
		else if(getFolderWithAuth.getGrp_r() != null) { auth_r = getFolderWithAuth.getGrp_r(); }
		// 쓰기권한
		if(getFolderWithAuth.getUser_w() != null) { auth_w = getFolderWithAuth.getUser_w(); }
		else if(getFolderWithAuth.getOrg_w() != null) { auth_w = getFolderWithAuth.getOrg_w(); }
		else if(getFolderWithAuth.getGrp_w() != null) { auth_w = getFolderWithAuth.getGrp_w(); }
		// 삭제권한
		if(getFolderWithAuth.getUser_d() != null) { auth_d = getFolderWithAuth.getUser_d(); }
		else if(getFolderWithAuth.getOrg_d() != null) { auth_d = getFolderWithAuth.getOrg_d(); }
		else if(getFolderWithAuth.getGrp_d() != null) { auth_d = getFolderWithAuth.getGrp_d(); }
		
		// default 프로젝트의 경우 DCC 폴더에 읽기권한 부여
		String prj_id = getFolderWithAuth.getPrj_id();
		String folder_type = getFolderWithAuth.getFolder_type();
		if (prj_id.equals("0000000001")) {
			if (folder_type.equals("DCC") || folder_type.equals("COL") || folder_type.equals("TRA"))
				auth_r = "Y";
			else
				auth_r = "N";
		}

		getFolderWithAuth.setAuth_r(auth_r);
		getFolderWithAuth.setAuth_w(auth_w);
		getFolderWithAuth.setAuth_d(auth_d);

		if (isDcc.equals("Y")) { // 로그인한 유저가 dcc인 경우
			if (!folder_type.equals("CON")) {
				getFolderWithAuth.setAuth_r("Y");
				getFolderWithAuth.setAuth_w("Y");
				getFolderWithAuth.setAuth_d("Y");
			} else if (getFolderWithAuth.getFolder_nm().equals("SYSTEM CONFIGURATION")) { // "CON" 타입이면, SYS CON(루트)과
																							// User Register 메뉴만 허용
				getFolderWithAuth.setAuth_r("Y");
				getFolderWithAuth.setAuth_w("Y");
				getFolderWithAuth.setAuth_d("Y");
			} else if (getFolderWithAuth.getFolder_nm().equals("USER REGISTER")) { // "CON" 타입이면, SYS CON(루트)과 User
																					// Register 메뉴만 허용
				getFolderWithAuth.setAuth_r("Y");
				getFolderWithAuth.setAuth_w("Y");
				getFolderWithAuth.setAuth_d("Y");
			}

		}

		if (isAdmin.equals("Y")) { // 로그인한 유저가 시스템 관리자인 경우
			getFolderWithAuth.setAuth_r("Y");
			getFolderWithAuth.setAuth_w("Y");
			getFolderWithAuth.setAuth_d("Y");
		}

		result.addObject("auth_check_r", getFolderWithAuth.getAuth_r());
		result.addObject("auth_check_w", getFolderWithAuth.getAuth_w());
		result.addObject("auth_check_d", getFolderWithAuth.getAuth_d());
		
		
		return result;
	}
	
	public boolean checkTRout(HttpServletRequest request , PrjTrVO vo) {
		String doc_table = vo.getDoc_table();
		int count = 0;
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		String login_id = sessioninfo.getUser_id();
		PrjInfoVO dccChk = new PrjInfoVO();
		dccChk.setPrj_id(vo.getPrj_id());
		dccChk.setUser_id(login_id);
		
		String isDcc = selectOne("folderinfoSqlMap.isDcc",dccChk);
		
		// dcc일 경우 무조건 pass
		if(isDcc != null) {
			if(isDcc.equals("Y")) return true;
		}
		
		
		
		switch(doc_table) {
			case "DCI":
				count = selectOne("prjdocumentindexSqlMap.checkTRout",vo);
				break;
				
			case "VDCI":
				count = selectOne("prjdocumentindexSqlMap.checkVTRout",vo);
				break;
				
			case "SDCI":
				count = selectOne("prjdocumentindexSqlMap.checkSTRout",vo);
				break;
			
			default: // 제너럴
				return true;
		}
		
		if(count < 1)
			return false;
		
		
		return true;
	}

	public String useDrnCheckForThisFolder(PrjDocumentIndexVO prjDocumentIndexVO) {
		
		String useDrn = "N";
		// drn 설정된 process를 찾음
		List<String> useDrnFolderList = selectList("prjdocumentindexSqlMap.getUseDrnFolderList",prjDocumentIndexVO.getPrj_id());
		List<String> splitedUseDrnFolderList = new ArrayList<>();
		for(String process_folder_path_id : useDrnFolderList) {
			String[] split = process_folder_path_id.split(",");
			for(String folder_id : split) {
				splitedUseDrnFolderList.add(folder_id);
			}
		}
		
		List<String> notUseDrnFolderList = selectList("prjdocumentindexSqlMap.getNotUseDrnFolderList",prjDocumentIndexVO.getPrj_id());
		List<String> splitedNotUseDrnFolderList = new ArrayList<>();
		for(String process_folder_path_id : notUseDrnFolderList) {
			String[] split = process_folder_path_id.split(",");
			for(String folder_id : split) {
				splitedNotUseDrnFolderList.add(folder_id);
			}
		}
		
		/*
		// DB정보 확인
		System.out.printf("[%s:useDrnList  = %s ]\n" ,prjDocumentIndexVO.getPrj_id(),splitedUseDrnFolderList);
		System.out.printf("[%s:notUseDrnList  = %s ]\n" ,prjDocumentIndexVO.getPrj_id(),splitedNotUseDrnFolderList);
		*/
		
		
		
		
		// drn 설정된 process의 하위 폴더인지 체크
		String this_folder_path = selectOne("prjdocumentindexSqlMap.getFolderPathOfThisFolder",prjDocumentIndexVO);
		// 폴더패스가 없는 경우는 없지만, 없는 경우는 drn 매핑을 해줄 수 없다.
		if(this_folder_path == null) return "N";
		
		// 폴더 패스를 구함
		List<String> check = Arrays.asList(this_folder_path.split(">"));
		// 폴더패스 순서 반전 (역순으로 확인하여 depth가 가장 가까운 곳의 매핑값이 해당 폴더의 결정값) 
		Collections.reverse(check);
		
		for(String folder_id_str : check) {
			
			if(splitedUseDrnFolderList.contains(folder_id_str)) {
				useDrn = "Y";
				break;
			}
			
			if(splitedNotUseDrnFolderList.contains(folder_id_str)) {
				useDrn = "N";
				break;
			}
				
		}
		
		
		
		
		return useDrn;
	}
	
	public ModelAndView indexRegisterValidationCheck(PrjDocumentIndexVO prjDocumentIndexVO, boolean overWrite, String reg_id, String info_tbl, String[] jsonDatas) {
		
		ModelAndView result = new ModelAndView();
		
		ObjectMapper objectMapper = new ObjectMapper();
		int maxId = getMaxDocId(prjDocumentIndexVO);
		String discip_code_id = getDiscipCodeId(prjDocumentIndexVO);
		String doc_type = prjDocumentIndexVO.getDoc_type();
		
		// 매핑되지 않는 data들 무시
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			for (String json : jsonDatas) {
				PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
				tmpBean.setPrj_id(prjDocumentIndexVO.getPrj_id());
				tmpBean.setFolder_id(prjDocumentIndexVO.getFolder_id());
				tmpBean.setReg_id(reg_id);
				tmpBean.setPrfe_designer_id(tmpBean.getDesigner_id());
				tmpBean.setTbl_type(info_tbl);
				tmpBean.setDoc_type(doc_type);
				
				// designer 체크
				UsersVO chkDesigner = isInDesigner(tmpBean);
				if (chkDesigner == null)
					throw new Exception("[" + tmpBean.getDesigner_id() + "] : There is no user who has that name in project");
				
				// 폴더내 doc_no 체크
				List<PrjDocumentIndexVO> existDocList = isInDocNo(tmpBean);
				// 다른 폴더내의 doc_no 체크 
				List<PrjDocumentIndexVO> existDocListOtherFolder = isInDocNoOtherFolder(tmpBean);
				
				// overWrite모드가 아닐 때, 삽입하려는 doc_no가 폴더에 존재하면 무조건 삽입불가
				if(!overWrite) 
				if(existDocList.size() > 0) {
					throw new Exception("["+ tmpBean.getDoc_no() + "]: There is a document with same doc_no, so can't regist");
				}
				
					
				
				// 다른 폴더에 동일한 doc_no가 존재하는 경우에는 overWrite도 불가
				if(existDocListOtherFolder.size() > 0) {
					throw new Exception("["+ tmpBean.getDoc_no() + "]: There is a document with same doc_no in other folder, so can't regist");
				}
					

			}
		} catch (Exception e) {
			String error = e.getMessage();
			result.addObject("error",error);
			result.addObject("status",false);
			return result;
		}
		
		result.addObject("status",true);
		return result;
	}

	public String getFolderPath(PrjDocumentIndexVO prjDocumentIndexVO) {
		return selectOne("prjdocumentindexSqlMap.getFolderPathOfThisFolder" , prjDocumentIndexVO);
	}
		/**
		 * 1. 메소드명 : getRegDate
		 * 2. 작성일: 2022. 3. 26.
		 * 3. 작성자: doil
		 * 4. 설명: 
		 * 5. 수정일: doil
		 */
	public String getRegDate(PrjDocumentIndexVO prjDocumentIndexVO) {
		List<String> getRegDate = selectList("prjdocumentindexSqlMap.getPrevRegDate" , prjDocumentIndexVO);
		if(getRegDate.size()>0) return getRegDate.get(0);
		return null;
	}
	
	

	public boolean docChkFile(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjDocumentIndexVO) throws Exception{
		boolean chkFile = true;
		if(prjDocumentIndexVO.getFile_value()!=null) {
			if(!prjDocumentIndexVO.getFile_value().equals("")) {	//file Attach 할 경우
				MultipartFile mf = mtRequest.getFile("updateRevision_File");
				File file = new File(mf.getOriginalFilename());
				chkFile = FileExtFilter.whiteFileExtIsReturnBoolean(file);
			}
		}
		return chkFile;
	}
	
	public int chkFile(MultipartHttpServletRequest mtRequest , String getFileName) throws Exception{
		int chkFile = 0;
		

		
		MultipartFile mf = mtRequest.getFile(getFileName);
		File file = new File(mf.getOriginalFilename());
		// true면 등록 불가 false면 등록 가능한 확장자
		// boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
		// true면 등록 가능 false면 등록 불가능한 확장자
		boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
			
		if(fileUploadChk == true) {
			chkFile = 100;
		}else if(fileUploadChk == false) {
			chkFile = 0;
		}
		
		return chkFile;
	}

	public int chkFiles(MultipartHttpServletRequest mtRequest , String getFileName) {
		
		int chkFile = 0;
		
		List<MultipartFile> fileList = mtRequest.getFiles(getFileName);
		
		for(MultipartFile file : fileList) {
			String file_name = file.getOriginalFilename();
			File _file = new File(file_name);
			boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(_file);
			
			if(!fileUploadChk) {
				chkFile = 0;
				return chkFile;
			}
		}
		
		chkFile = 100;
		
		return chkFile;
	}

	
	public List<FolderInfoVO> getFolderList4docDown(String prj_id) {
		return selectList("prjdocumentindexSqlMap.getFolderList4docDown",prj_id);
	}
	
	public List<FolderInfoVO> getDiscipList(String prj_id) {
		return selectList("prjdocumentindexSqlMap.getDiscipList",prj_id);
	}

	public List<PrjDocumentIndexVO> getDocList(PrjDocumentIndexVO prjdocumentindexvo) {
		// 빈문자열,null값 클리어
		String prj_id = prjdocumentindexvo.getPrj_id();
		long folder_id = prjdocumentindexvo.getFolder_id();
		String discip_code_id = prjdocumentindexvo.getDiscip_code_id();
		if(discip_code_id == null || discip_code_id.equals("")) {
			discip_code_id = null;
			prjdocumentindexvo.setDiscip_code_id(discip_code_id);
			return selectList("prjdocumentindexSqlMap.getDocDownList_onlyFolder",prjdocumentindexvo);
		}
		
		return selectList("prjdocumentindexSqlMap.getDocDownList",prjdocumentindexvo);
	}

	public List<String> getDiscipListInFolder(String prj_id, long folder_id) {
		
		PrjDocumentIndexVO getDiscipListVo = new PrjDocumentIndexVO();
		getDiscipListVo.setPrj_id(prj_id);
		getDiscipListVo.setFolder_id(folder_id);
		
		List<String> outDiscipList = new ArrayList<>();
		outDiscipList = selectList("prjdocumentindexSqlMap.getDiscipDisplayList",getDiscipListVo);
		
		
		return outDiscipList;
	}

	public String getDiscipDisplay(PrjDocumentIndexVO prjdocumentindexvo) {
			// TODO Auto-generated method stub
			return selectOne("prjdocumentindexSqlMap.getDiscipDisplay",prjdocumentindexvo);
	}
	


	public List<PrjCodeSettingsVO> getVDocCate(PrjCodeSettingsVO codeVo) {
		return selectList("prjdocumentindexSqlMap.getVDocCate",codeVo);
	}
	public List<PrjCodeSettingsVO> getVDocSize(PrjCodeSettingsVO codeVo) {
		return selectList("prjdocumentindexSqlMap.getVDocSize",codeVo);
	}
	
	

	
}
