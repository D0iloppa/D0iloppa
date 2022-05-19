package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjStepService extends AbstractService {
	public List<PrjCodeSettingsVO> getPrjStepCode(PrjCodeSettingsVO prjcodesettingsvo){
		List<PrjCodeSettingsVO> getPrjStepCode = selectList("prjstepSqlMap.getPrjStepCode", prjcodesettingsvo);
		return getPrjStepCode;
	}
	public List<PrjStepVO> getPrjStep(PrjStepVO prjstepvo){
		List<PrjStepVO> getPrjStep = selectList("prjstepSqlMap.getPrjStep", prjstepvo);
		return getPrjStep;
	}
	public List<PrjStepVO> getVendorPrjStep(PrjStepVO prjstepvo){
		List<PrjStepVO> getVendorPrjStep = selectList("prjstepSqlMap.getVendorPrjStep", prjstepvo);
		return getVendorPrjStep;
	}
	public List<PrjStepVO> getProcPrjStep(PrjStepVO prjstepvo){
		List<PrjStepVO> getProcPrjStep = selectList("prjstepSqlMap.getProcPrjStep", prjstepvo);
		return getProcPrjStep;
	}
	public List<PrjStepVO> getBulkPrjStep(PrjStepVO prjstepvo){
		List<PrjStepVO> getBulkPrjStep = selectList("prjstepSqlMap.getBulkPrjStep", prjstepvo);
		return getBulkPrjStep;
	}
	public List<PrjStepVO> getSitePrjStep(PrjStepVO prjstepvo){
		List<PrjStepVO> getSitePrjStep = selectList("prjstepSqlMap.getSitePrjStep", prjstepvo);
		return getSitePrjStep;
	}
	public int getStepByStepCodeId(PrjStepVO prjstepvo){
		int getStepByStepCodeId = selectOne("prjstepSqlMap.getStepByStepCodeId", prjstepvo);
		return getStepByStepCodeId;
	}
	public int insertPrjStep(PrjStepVO prjstepvo){
		int insertPrjStep = insert("prjstepSqlMap.insertPrjStep", prjstepvo);
		return insertPrjStep;
	}
	public int updatePrjStep(PrjStepVO prjstepvo){
		int updatePrjStep = update("prjstepSqlMap.updatePrjStep", prjstepvo);
		return updatePrjStep;
	}
	public int insertPrjStepForUpdateRevision(PrjStepVO prjstepvo){
		int insertPrjStepForUpdateRevision = insert("prjstepSqlMap.insertPrjStepForUpdateRevision", prjstepvo);
		return insertPrjStepForUpdateRevision;
	}
	public int insertStartActualDateByFirstRevision(PrjStepVO prjstepvo){
		int insertStartActualDateByFirstRevision = update("prjstepSqlMap.insertStartActualDateByFirstRevision", prjstepvo);
		return insertStartActualDateByFirstRevision;
	}
	public PrjCodeSettingsVO getInfoByTRReturnSpecificValue(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getInfoByTRReturnSpecificValue = selectOne("prjstepSqlMap.getInfoByTRReturnSpecificValue", prjstepvo);
		return getInfoByTRReturnSpecificValue;
	}
	public PrjCodeSettingsVO getInfoByVTRReturnSpecificValue(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getInfoByVTRReturnSpecificValue = selectOne("prjstepSqlMap.getInfoByVTRReturnSpecificValue", prjstepvo);
		return getInfoByVTRReturnSpecificValue;
	}
	public PrjCodeSettingsVO getInfoBySTRReturnSpecificValue(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getInfoBySTRReturnSpecificValue = selectOne("prjstepSqlMap.getInfoBySTRReturnSpecificValue", prjstepvo);
		return getInfoBySTRReturnSpecificValue;
	}
	public int updateActualDateByAnyTRReturn(PrjStepVO prjstepvo){
		int updateActualDateByAnyTRReturn = update("prjstepSqlMap.updateActualDateByAnyTRReturn", prjstepvo);
		return updateActualDateByAnyTRReturn;
	}
	public int getVendorStepByStepCodeId(PrjStepVO prjstepvo){
		int getVendorStepByStepCodeId = selectOne("prjstepSqlMap.getVendorStepByStepCodeId", prjstepvo);
		return getVendorStepByStepCodeId;
	}
	public int updateActualDateByTRReturnSpecificValue(PrjStepVO prjstepvo){
		int updateActualDateByTRReturnSpecificValue = update("prjstepSqlMap.updateActualDateByTRReturnSpecificValue", prjstepvo);
		return updateActualDateByTRReturnSpecificValue;
	}
	public int updateActualDateByVTRReturnSpecificValue(PrjStepVO prjstepvo){
		int updateActualDateByVTRReturnSpecificValue = update("prjstepSqlMap.updateActualDateByVTRReturnSpecificValue", prjstepvo);
		return updateActualDateByVTRReturnSpecificValue;
	}
	public int updateActualDateBySTRReturnSpecificValue(PrjStepVO prjstepvo){
		int updateActualDateBySTRReturnSpecificValue = update("prjstepSqlMap.updateActualDateBySTRReturnSpecificValue", prjstepvo);
		return updateActualDateBySTRReturnSpecificValue;
	}
	public int updateActualDateByTRDRNAPPROVAL(PrjStepVO prjstepvo){
		int updateActualDateByTRDRNAPPROVAL = update("prjstepSqlMap.updateActualDateByTRDRNAPPROVAL", prjstepvo);
		return updateActualDateByTRDRNAPPROVAL;
	}
	public int updateActualDateByVTRDRNAPPROVAL(PrjStepVO prjstepvo){
		int updateActualDateByVTRDRNAPPROVAL = update("prjstepSqlMap.updateActualDateByVTRDRNAPPROVAL", prjstepvo);
		return updateActualDateByVTRDRNAPPROVAL;
	}
	public int updateActualDateBySTRDRNAPPROVAL(PrjStepVO prjstepvo){
		int updateActualDateBySTRDRNAPPROVAL = update("prjstepSqlMap.updateActualDateBySTRDRNAPPROVAL", prjstepvo);
		return updateActualDateBySTRDRNAPPROVAL;
	}
	public int insertVendorPrjStep(PrjStepVO prjstepvo){
		int insertVendorPrjStep = insert("prjstepSqlMap.insertVendorPrjStep", prjstepvo);
		return insertVendorPrjStep;
	}
	public int updateVendorPrjStep(PrjStepVO prjstepvo){
		int updateVendorPrjStep = update("prjstepSqlMap.updateVendorPrjStep", prjstepvo);
		return updateVendorPrjStep;
	}
	public int insertVendorPrjStepForUpdateRevision(PrjStepVO prjstepvo){
		int insertVendorPrjStepForUpdateRevision = insert("prjstepSqlMap.insertVendorPrjStepForUpdateRevision", prjstepvo);
		return insertVendorPrjStepForUpdateRevision;
	}
	public int insertVendorStartActualDateByFirstRevision(PrjStepVO prjstepvo){
		int insertVendorStartActualDateByFirstRevision = update("prjstepSqlMap.insertVendorStartActualDateByFirstRevision", prjstepvo);
		return insertVendorStartActualDateByFirstRevision;
	}
	public int updateActualDateByAnyVTRReturn(PrjStepVO prjstepvo){
		int updateActualDateByAnyVTRReturn = update("prjstepSqlMap.updateActualDateByAnyVTRReturn", prjstepvo);
		return updateActualDateByAnyVTRReturn;
	}
	public int getProcStepByStepCodeId(PrjStepVO prjstepvo){
		int getProcStepByStepCodeId = selectOne("prjstepSqlMap.getProcStepByStepCodeId", prjstepvo);
		return getProcStepByStepCodeId;
	}
	public int insertProcPrjStep(PrjStepVO prjstepvo){
		int insertProcPrjStep = insert("prjstepSqlMap.insertProcPrjStep", prjstepvo);
		return insertProcPrjStep;
	}
	public int updateProcPrjStep(PrjStepVO prjstepvo){
		int updateProcPrjStep = update("prjstepSqlMap.updateProcPrjStep", prjstepvo);
		return updateProcPrjStep;
	}
	public int insertProcPrjStepForUpdateRevision(PrjStepVO prjstepvo){
		int insertProcPrjStepForUpdateRevision = insert("prjstepSqlMap.insertProcPrjStepForUpdateRevision", prjstepvo);
		return insertProcPrjStepForUpdateRevision;
	}
	public int insertProcStartActualDateByFirstRevision(PrjStepVO prjstepvo){
		int insertProcStartActualDateByFirstRevision = update("prjstepSqlMap.insertProcStartActualDateByFirstRevision", prjstepvo);
		return insertProcStartActualDateByFirstRevision;
	}
	public int getBulkStepByStepCodeId(PrjStepVO prjstepvo){
		int getBulkStepByStepCodeId = selectOne("prjstepSqlMap.getBulkStepByStepCodeId", prjstepvo);
		return getBulkStepByStepCodeId;
	}
	public int insertBulkPrjStep(PrjStepVO prjstepvo){
		int insertBulkPrjStep = insert("prjstepSqlMap.insertBulkPrjStep", prjstepvo);
		return insertBulkPrjStep;
	}
	public int updateBulkPrjStep(PrjStepVO prjstepvo){
		int updateBulkPrjStep = update("prjstepSqlMap.updateBulkPrjStep", prjstepvo);
		return updateBulkPrjStep;
	}
	public int insertBulkPrjStepForUpdateRevision(PrjStepVO prjstepvo){
		int insertBulkPrjStepForUpdateRevision = insert("prjstepSqlMap.insertBulkPrjStepForUpdateRevision", prjstepvo);
		return insertBulkPrjStepForUpdateRevision;
	}
	public int insertBulkStartActualDateByFirstRevision(PrjStepVO prjstepvo){
		int insertBulkStartActualDateByFirstRevision = update("prjstepSqlMap.insertBulkStartActualDateByFirstRevision", prjstepvo);
		return insertBulkStartActualDateByFirstRevision;
	}
	public int getSiteStepByStepCodeId(PrjStepVO prjstepvo){
		int getSiteStepByStepCodeId = selectOne("prjstepSqlMap.getSiteStepByStepCodeId", prjstepvo);
		return getSiteStepByStepCodeId;
	}
	public int insertSitePrjStep(PrjStepVO prjstepvo){
		int insertSitePrjStep = insert("prjstepSqlMap.insertSitePrjStep", prjstepvo);
		return insertSitePrjStep;
	}
	public int updateSitePrjStep(PrjStepVO prjstepvo){
		int updateSitePrjStep = update("prjstepSqlMap.updateSitePrjStep", prjstepvo);
		return updateSitePrjStep;
	}
	public int insertSitePrjStepForUpdateRevision(PrjStepVO prjstepvo){
		int insertSitePrjStepForUpdateRevision = insert("prjstepSqlMap.insertSitePrjStepForUpdateRevision", prjstepvo);
		return insertSitePrjStepForUpdateRevision;
	}
	public int insertSiteStartActualDateByFirstRevision(PrjStepVO prjstepvo){
		int insertSiteStartActualDateByFirstRevision = update("prjstepSqlMap.insertSiteStartActualDateByFirstRevision", prjstepvo);
		return insertSiteStartActualDateByFirstRevision;
	}
	public int updateActualDateByAnySTRReturn(PrjStepVO prjstepvo){
		int updateActualDateByAnySTRReturn = update("prjstepSqlMap.updateActualDateByAnySTRReturn", prjstepvo);
		return updateActualDateByAnySTRReturn;
	}
	public int updateProcActualDateBySetPORorPO(PrjStepVO prjstepvo){
		int updateProcActualDateBySetPORorPO = update("prjstepSqlMap.updateProcActualDateBySetPORorPO", prjstepvo);
		return updateProcActualDateBySetPORorPO;
	}
	public int updateBulkActualDateBySetPORorPO(PrjStepVO prjstepvo){
		int updateBulkActualDateBySetPORorPO = update("prjstepSqlMap.updateBulkActualDateBySetPORorPO", prjstepvo);
		return updateBulkActualDateBySetPORorPO;
	}
	public int updateTrIdOnEngStepTable(PrjStepVO prjstepvo){
		int updateTrIdOnEngStepTable = update("prjstepSqlMap.updateTrIdOnEngStepTable", prjstepvo);
		return updateTrIdOnEngStepTable;
	}
	public List<PrjCodeSettingsVO> getTrReviewStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getTrReviewStepCodeId = selectList("prjstepSqlMap.getTrReviewStepCodeId", prjstepvo);
		return getTrReviewStepCodeId;
	}
	public PrjCodeSettingsVO getForeDateRuleForTrReview(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleForTrReview = selectOne("prjstepSqlMap.getForeDateRuleForTrReview", prjstepvo);
		return getForeDateRuleForTrReview;
	}
	public int updateRtnStatusByTrIncoming(PrjStepVO prjstepvo){
		int updateRtnStatusByTrIncoming = update("prjstepSqlMap.updateRtnStatusByTrIncoming", prjstepvo);
		return updateRtnStatusByTrIncoming;
	}
	public int updateReviewForeDateByTrIncoming(PrjStepVO prjstepvo){
		int updateReviewForeDateByTrIncoming = update("prjstepSqlMap.updateReviewForeDateByTrIncoming", prjstepvo);
		return updateReviewForeDateByTrIncoming;
	}
	public int updateRtnStatusByTrIncomingDelete(PrjStepVO prjstepvo){
		int updateRtnStatusByTrIncomingDelete = update("prjstepSqlMap.updateRtnStatusByTrIncomingDelete", prjstepvo);
		return updateRtnStatusByTrIncomingDelete;
	}
	public int updateVTrIdOnVdrStepTable(PrjStepVO prjstepvo){
		int updateVTrIdOnVdrStepTable = update("prjstepSqlMap.updateVTrIdOnVdrStepTable", prjstepvo);
		return updateVTrIdOnVdrStepTable;
	}
	public int updateRtnStatusByVTrIncoming(PrjStepVO prjstepvo){
		int updateRtnStatusByVTrIncoming = update("prjstepSqlMap.updateRtnStatusByVTrIncoming", prjstepvo);
		return updateRtnStatusByVTrIncoming;
	}
	public List<PrjCodeSettingsVO> getVTrReviewStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getVTrReviewStepCodeId = selectList("prjstepSqlMap.getVTrReviewStepCodeId", prjstepvo);
		return getVTrReviewStepCodeId;
	}
	public PrjCodeSettingsVO getForeDateRuleForVTrReview(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleForVTrReview = selectOne("prjstepSqlMap.getForeDateRuleForVTrReview", prjstepvo);
		return getForeDateRuleForVTrReview;
	}
	public int updateReviewForeDateByVTrIncoming(PrjStepVO prjstepvo){
		int updateReviewForeDateByVTrIncoming = update("prjstepSqlMap.updateReviewForeDateByVTrIncoming", prjstepvo);
		return updateReviewForeDateByVTrIncoming;
	}
	public int updateRtnStatusByVTrIncomingDelete(PrjStepVO prjstepvo){
		int updateRtnStatusByVTrIncomingDelete = update("prjstepSqlMap.updateRtnStatusByVTrIncomingDelete", prjstepvo);
		return updateRtnStatusByVTrIncomingDelete;
	}
	public int updateSTrIdOnSdcStepTable(PrjStepVO prjstepvo){
		int updateSTrIdOnSdcStepTable = update("prjstepSqlMap.updateSTrIdOnSdcStepTable", prjstepvo);
		return updateSTrIdOnSdcStepTable;
	}
	public int updateRtnStatusBySTrIncoming(PrjStepVO prjstepvo){
		int updateRtnStatusBySTrIncoming = update("prjstepSqlMap.updateRtnStatusBySTrIncoming", prjstepvo);
		return updateRtnStatusBySTrIncoming;
	}
	public List<PrjCodeSettingsVO> getSTrReviewStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getSTrReviewStepCodeId = selectList("prjstepSqlMap.getSTrReviewStepCodeId", prjstepvo);
		return getSTrReviewStepCodeId;
	}
	public PrjCodeSettingsVO getForeDateRuleForSTrReview(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleForSTrReview = selectOne("prjstepSqlMap.getForeDateRuleForSTrReview", prjstepvo);
		return getForeDateRuleForSTrReview;
	}
	public int updateReviewForeDateBySTrIncoming(PrjStepVO prjstepvo){
		int updateReviewForeDateBySTrIncoming = update("prjstepSqlMap.updateReviewForeDateBySTrIncoming", prjstepvo);
		return updateReviewForeDateBySTrIncoming;
	}
	public int updateRtnStatusBySTrIncomingDelete(PrjStepVO prjstepvo){
		int updateRtnStatusBySTrIncomingDelete = update("prjstepSqlMap.updateRtnStatusBySTrIncomingDelete", prjstepvo);
		return updateRtnStatusBySTrIncomingDelete;
	}
	public PrjCodeSettingsVO getIssuePurposeByTRDoc(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getIssuePurposeByTRDoc = selectOne("prjstepSqlMap.getIssuePurposeByTRDoc", prjstepvo);
		return getIssuePurposeByTRDoc;
	}
	public PrjCodeSettingsVO getIssuePurposeByVTRDoc(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getIssuePurposeByVTRDoc = selectOne("prjstepSqlMap.getIssuePurposeByVTRDoc", prjstepvo);
		return getIssuePurposeByVTRDoc;
	}
	public PrjCodeSettingsVO getIssuePurposeBySTRDoc(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getIssuePurposeBySTRDoc = selectOne("prjstepSqlMap.getIssuePurposeBySTRDoc", prjstepvo);
		return getIssuePurposeBySTRDoc;
	}
	public PrjCodeSettingsVO getCodeByTrIssuePurpose(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getCodeByTrIssuePurpose = selectOne("prjstepSqlMap.getCodeByTrIssuePurpose", prjstepvo);
		return getCodeByTrIssuePurpose;
	}
	public PrjCodeSettingsVO getCodeByVTrIssuePurpose(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getCodeByVTrIssuePurpose = selectOne("prjstepSqlMap.getCodeByVTrIssuePurpose", prjstepvo);
		return getCodeByVTrIssuePurpose;
	}
	public PrjCodeSettingsVO getCodeBySTrIssuePurpose(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getCodeBySTrIssuePurpose = selectOne("prjstepSqlMap.getCodeBySTrIssuePurpose", prjstepvo);
		return getCodeBySTrIssuePurpose;
	}
	public List<PrjCodeSettingsVO> getUpdateVendorStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = selectList("prjstepSqlMap.getUpdateVendorStepCodeId", prjstepvo);
		return getUpdateVendorStepCodeId;
	}
	public List<PrjCodeSettingsVO> getUpdateSiteStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = selectList("prjstepSqlMap.getUpdateSiteStepCodeId", prjstepvo);
		return getUpdateSiteStepCodeId;
	}
	public List<PrjCodeSettingsVO> getUpdateProcStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateProcStepCodeId = selectList("prjstepSqlMap.getUpdateProcStepCodeId", prjstepvo);
		return getUpdateProcStepCodeId;
	}
	public List<PrjCodeSettingsVO> getUpdateBulkStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = selectList("prjstepSqlMap.getUpdateBulkStepCodeId", prjstepvo);
		return getUpdateBulkStepCodeId;
	}
	public List<PrjCodeSettingsVO> getUpdateEngStepCodeId(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateEngStepCodeId = selectList("prjstepSqlMap.getUpdateEngStepCodeId", prjstepvo);
		return getUpdateEngStepCodeId;
	}
	public PrjStepVO getActualDateEngStepByStepCodeId(PrjStepVO prjstepvo){
		PrjStepVO getActualDateEngStepByStepCodeId = selectOne("prjstepSqlMap.getActualDateEngStepByStepCodeId", prjstepvo);
		return getActualDateEngStepByStepCodeId;
	}
	public PrjStepVO getActualDateVendorStepByStepCodeId(PrjStepVO prjstepvo){
		PrjStepVO getActualDateVendorStepByStepCodeId = selectOne("prjstepSqlMap.getActualDateVendorStepByStepCodeId", prjstepvo);
		return getActualDateVendorStepByStepCodeId;
	}
	public PrjStepVO getActualDateSiteStepByStepCodeId(PrjStepVO prjstepvo){
		PrjStepVO getActualDateSiteStepByStepCodeId = selectOne("prjstepSqlMap.getActualDateSiteStepByStepCodeId", prjstepvo);
		return getActualDateSiteStepByStepCodeId;
	}
	public PrjStepVO getActualDateProcStepByStepCodeId(PrjStepVO prjstepvo){
		PrjStepVO getActualDateProcStepByStepCodeId = selectOne("prjstepSqlMap.getActualDateProcStepByStepCodeId", prjstepvo);
		return getActualDateProcStepByStepCodeId;
	}
	public PrjStepVO getActualDateBulkStepByStepCodeId(PrjStepVO prjstepvo){
		PrjStepVO getActualDateBulkStepByStepCodeId = selectOne("prjstepSqlMap.getActualDateBulkStepByStepCodeId", prjstepvo);
		return getActualDateBulkStepByStepCodeId;
	}
	public int updateEngStepActualDateByIFCIssue(PrjStepVO prjstepvo){
		int updateEngStepActualDateByIFCIssue = update("prjstepSqlMap.updateEngStepActualDateByIFCIssue", prjstepvo);
		return updateEngStepActualDateByIFCIssue;
	}
	public int updateVendorStepActualDateByIFCIssue(PrjStepVO prjstepvo){
		int updateVendorStepActualDateByIFCIssue = update("prjstepSqlMap.updateVendorStepActualDateByIFCIssue", prjstepvo);
		return updateVendorStepActualDateByIFCIssue;
	}
	public int updateSiteStepActualDateByIFCIssue(PrjStepVO prjstepvo){
		int updateSiteStepActualDateByIFCIssue = update("prjstepSqlMap.updateSiteStepActualDateByIFCIssue", prjstepvo);
		return updateSiteStepActualDateByIFCIssue;
	}
	public int updateEngStepActualDateByMailSending(PrjStepVO prjstepvo){
		int updateEngStepActualDateByMailSending = update("prjstepSqlMap.updateEngStepActualDateByMailSending", prjstepvo);
		return updateEngStepActualDateByMailSending;
	}
	public int updateVendorStepActualDateByMailSending(PrjStepVO prjstepvo){
		int updateVendorStepActualDateByMailSending = update("prjstepSqlMap.updateVendorStepActualDateByMailSending", prjstepvo);
		return updateVendorStepActualDateByMailSending;
	}
	public int updateSiteStepActualDateByMailSending(PrjStepVO prjstepvo){
		int updateSiteStepActualDateByMailSending = update("prjstepSqlMap.updateSiteStepActualDateByMailSending", prjstepvo);
		return updateSiteStepActualDateByMailSending;
	}
	public int updateEngStepForeDateByStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateEngStepForeDateByStepActualDateUpdate = update("prjstepSqlMap.updateEngStepForeDateByStepActualDateUpdate", prjstepvo);
		return updateEngStepForeDateByStepActualDateUpdate;
	}
	public int updateVendorStepForeDateByStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateVendorStepForeDateByStepActualDateUpdate = update("prjstepSqlMap.updateVendorStepForeDateByStepActualDateUpdate", prjstepvo);
		return updateVendorStepForeDateByStepActualDateUpdate;
	}
	public int updateSiteStepForeDateByStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateSiteStepForeDateByStepActualDateUpdate = update("prjstepSqlMap.updateSiteStepForeDateByStepActualDateUpdate", prjstepvo);
		return updateSiteStepForeDateByStepActualDateUpdate;
	}
	public int updateProcStepForeDateByStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateProcStepForeDateByStepActualDateUpdate = update("prjstepSqlMap.updateProcStepForeDateByStepActualDateUpdate", prjstepvo);
		return updateProcStepForeDateByStepActualDateUpdate;
	}
	public int updateBulkStepForeDateByStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateBulkStepForeDateByStepActualDateUpdate = update("prjstepSqlMap.updateBulkStepForeDateByStepActualDateUpdate", prjstepvo);
		return updateBulkStepForeDateByStepActualDateUpdate;
	}
	public PrjCodeSettingsVO getForeDateRuleByEngStep(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleByEngStep = selectOne("prjstepSqlMap.getForeDateRuleByEngStep", prjstepvo);
		return getForeDateRuleByEngStep;
	}
	public PrjCodeSettingsVO getForeDateRuleByVendorStep(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleByVendorStep = selectOne("prjstepSqlMap.getForeDateRuleByVendorStep", prjstepvo);
		return getForeDateRuleByVendorStep;
	}
	public PrjCodeSettingsVO getForeDateRuleBySiteStep(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleBySiteStep = selectOne("prjstepSqlMap.getForeDateRuleBySiteStep", prjstepvo);
		return getForeDateRuleBySiteStep;
	}
	public PrjCodeSettingsVO getForeDateRuleByProcStep(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleByProcStep = selectOne("prjstepSqlMap.getForeDateRuleByProcStep", prjstepvo);
		return getForeDateRuleByProcStep;
	}
	public PrjCodeSettingsVO getForeDateRuleByBulkStep(PrjStepVO prjstepvo){
		PrjCodeSettingsVO getForeDateRuleByBulkStep = selectOne("prjstepSqlMap.getForeDateRuleByBulkStep", prjstepvo);
		return getForeDateRuleByBulkStep;
	}
	public List<PrjCodeSettingsVO> getUpdateCodeIdByTRReturnValue(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateCodeIdByTRReturnValue = selectList("prjstepSqlMap.getUpdateCodeIdByTRReturnValue", prjstepvo);
		return getUpdateCodeIdByTRReturnValue;
	}
	public List<PrjCodeSettingsVO> getUpdateCodeIdByVTRReturnValue(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateCodeIdByVTRReturnValue = selectList("prjstepSqlMap.getUpdateCodeIdByVTRReturnValue", prjstepvo);
		return getUpdateCodeIdByVTRReturnValue;
	}
	public List<PrjCodeSettingsVO> getUpdateCodeIdBySTRReturnValue(PrjStepVO prjstepvo){
		List<PrjCodeSettingsVO> getUpdateCodeIdBySTRReturnValue = selectList("prjstepSqlMap.getUpdateCodeIdBySTRReturnValue", prjstepvo);
		return getUpdateCodeIdBySTRReturnValue;
	}
	public int updateEngStepForeDateByTRStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateEngStepForeDateByTRStepActualDateUpdate = update("prjstepSqlMap.updateEngStepForeDateByTRStepActualDateUpdate", prjstepvo);
		return updateEngStepForeDateByTRStepActualDateUpdate;
	}
	public int updateEngStepForeDateByVTRStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateEngStepForeDateByVTRStepActualDateUpdate = update("prjstepSqlMap.updateEngStepForeDateByVTRStepActualDateUpdate", prjstepvo);
		return updateEngStepForeDateByVTRStepActualDateUpdate;
	}
	public int updateEngStepForeDateBySTRStepActualDateUpdate(PrjStepVO prjstepvo){
		int updateEngStepForeDateBySTRStepActualDateUpdate = update("prjstepSqlMap.updateEngStepForeDateBySTRStepActualDateUpdate", prjstepvo);
		return updateEngStepForeDateBySTRStepActualDateUpdate;
	}
	public String insertEngStepByCheckIn(PrjStepVO prjstepvo){
		insert("prjstepSqlMap.insertEngStepByCheckIn",prjstepvo);
		String rev_id = prjstepvo.getRev_id();
		return rev_id;
	}
	public String insertVendorStepByCheckIn(PrjStepVO prjstepvo){
		insert("prjstepSqlMap.insertVendorStepByCheckIn",prjstepvo);
		String rev_id = prjstepvo.getRev_id();
		return rev_id;
	}
	public String insertSiteStepByCheckIn(PrjStepVO prjstepvo){
		insert("prjstepSqlMap.insertSiteStepByCheckIn",prjstepvo);
		String rev_id = prjstepvo.getRev_id();
		return rev_id;
	}
	public String insertProcStepByCheckIn(PrjStepVO prjstepvo){
		insert("prjstepSqlMap.insertProcStepByCheckIn",prjstepvo);
		String rev_id = prjstepvo.getRev_id();
		return rev_id;
	}
	public String insertBulkStepByCheckIn(PrjStepVO prjstepvo){
		insert("prjstepSqlMap.insertBulkStepByCheckIn",prjstepvo);
		String rev_id = prjstepvo.getRev_id();
		return rev_id;
	}

	public PrjCodeSettingsVO getForeDateRuleForTrReview2(PrjStepVO prjstepvo) {
		PrjCodeSettingsVO getForeDateRuleForTrReview = selectOne("prjstepSqlMap.getForeDateRuleForTrReview2", prjstepvo);
		return getForeDateRuleForTrReview;
	}
}
