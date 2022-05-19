package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.text.SimpleDateFormat;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.excel.ExcelFileDownload;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.PrjCodeSettingsService;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.PrjEmailService;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjEmailTypeService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.PrjTrService;
import kr.co.hhi.service.TreeService;

@Controller
@RequestMapping("/*")
public class PrjEmailCont {

	protected PrjEmailService  prjemailService;
	protected PrjTrService prjtrService;
	protected TreeService treeService;
	protected PrjStepService prjstepService;
	protected PrjDocumentIndexService prjdocumentindexService;
	protected PrjCodeSettingsService prjcodesettingsService;
	
	@Autowired
	public void setPrjEmailService(PrjEmailService prjemailService) {
		this.prjemailService = prjemailService;
	}
	@Autowired
	public void setPrjTrService(PrjTrService prjtrService) {
		this.prjtrService = prjtrService;
	}
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	
	/**
	 * 1. 메소드명 : insertPrjEmail
	 * 2. 작성일: 2022-01-11
	 * 3. 작성자: 소진희
	 * 4. 설명: email 전송하기 위해 db에 데이터 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "insertPrjEmail.do")
	@ResponseBody
	public Object insertPrjEmailType(PrjEmailVO prjemailvo,PrjTrVO prjtrvo,HttpServletRequest request,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjemailvo.setReg_id(sessioninfo.getUser_id());
		String currentDateAndTime = CommonConst.currentDateAndTime();
		prjemailvo.setReg_date(currentDateAndTime);
		prjemailvo.setEmail_id(currentDateAndTime);
		prjemailvo.setEmail_sender_user_id(sessioninfo.getUser_id());
		prjemailvo.setEmail_send_date(currentDateAndTime);
		try {
			int getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
			int insertPrjEmail = prjemailService.insertPrjEmail(prjemailvo);
			result.add(insertPrjEmail);
			
			if(insertPrjEmail>0) {
				prjtrvo.setSend_date(currentDateAndTime.substring(0, 8));
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				Calendar calendar = Calendar.getInstance();
				Date date = dateFormat.parse(currentDateAndTime.substring(0, 8));
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 15);
				prjtrvo.setReq_date(dateFormat.format(calendar.getTime()));
				
				List<PrjTrVO> getTrFile = new ArrayList<PrjTrVO>();
				if(prjtrvo.getProcessType().equals("TR")) {
					/*if(getEmailSentTimes==0) {	//최초발송일 때 Send Date와 Required Date 업데이트
						int updateTrSendDate = prjtrService.updateTrSendDate(prjtrvo);
						result.add(updateTrSendDate);
					}*/
					getTrFile = prjtrService.getTrFile(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					/*if(getEmailSentTimes==0) {	//최초발송일 때 Send Date와 Required Date 업데이트
						int updateVTrSendDate = prjtrService.updateVTrSendDate(prjtrvo);
						result.add(updateVTrSendDate);
					}*/
					getTrFile = prjtrService.getVTrFile(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					/*if(getEmailSentTimes==0) {	//최초발송일 때 Send Date와 Required Date 업데이트
						int updateSTrSendDate = prjtrService.updateSTrSendDate(prjtrvo);
						result.add(updateSTrSendDate);
					}*/
					getTrFile = prjtrService.getSTrFile(prjtrvo);
				}
				PrjStepVO prjstepvo = new PrjStepVO();
				prjstepvo.setPrj_id(prjemailvo.getPrj_id());
				prjstepvo.setTr_id(prjtrvo.getTr_id());
				prjstepvo.setMod_id(sessioninfo.getUser_id());
				prjstepvo.setMod_date(currentDateAndTime);
				
				for(int i=0;i<getTrFile.size();i++) {
					prjstepvo.setDoc_id(getTrFile.get(i).getDoc_id());
					prjstepvo.setRev_id(getTrFile.get(i).getRev_id());
					prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
					if(prjtrvo.getProcessType().equals("TR")) {
						PrjTrVO getTrInfo = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
						prjstepvo.setSet_val2(getCodeByTrIssuePurpose.getSet_code()+"메일 발송시");
						int updateEngStepActualDateByMailSending = prjstepService.updateEngStepActualDateByMailSending(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateEngStepCodeId = prjstepService.getUpdateEngStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateEngStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateEngStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateEngStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
							if(!getForeDateRuleByEngStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByEngStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
								PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
								if(getActualDateEngStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}else if(prjtrvo.getProcessType().equals("VTR")){
						PrjTrVO getTrInfo = prjtrService.getVTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
						prjstepvo.setSet_val2(getCodeByVTrIssuePurpose.getSet_code()+"메일 발송시");
						int updateVendorStepActualDateByMailSending = prjstepService.updateVendorStepActualDateByMailSending(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = prjstepService.getUpdateVendorStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateVendorStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateVendorStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateVendorStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
							if(!getForeDateRuleByVendorStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
								PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
								if(getActualDateVendorStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}else if(prjtrvo.getProcessType().equals("STR")){
						PrjTrVO getTrInfo = prjtrService.getSTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
						prjstepvo.setSet_val2(getCodeBySTrIssuePurpose.getSet_code()+"메일 발송시");
						int updateSiteStepActualDateByMailSending = prjstepService.updateSiteStepActualDateByMailSending(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = prjstepService.getUpdateSiteStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateSiteStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateSiteStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateSiteStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
							if(!getForeDateRuleBySiteStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
								PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
								if(getActualDateSiteStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("insertPrjEmail: {}");
		}
		String docid_revid_docno_revno = prjemailvo.getDocid_revid_docno_revno();
		if(docid_revid_docno_revno!=null) {
			String[] doc_list_array = docid_revid_docno_revno.split("@@");
			int count = 0;
			for(int i=0;i<doc_list_array.length;i++) {
				String[] docid_revid_docno_revno_array = doc_list_array[i].split("&&");
				prjemailvo.setDoc_id(docid_revid_docno_revno_array[0]);
				prjemailvo.setRev_id(docid_revid_docno_revno_array[1]);
				prjemailvo.setDoc_no(docid_revid_docno_revno_array[2]);
				prjemailvo.setRev_no(docid_revid_docno_revno_array[3]);
				int insertPrjEmailAttachInfo = prjemailService.insertPrjEmailAttachInfo(prjemailvo);
				count+=insertPrjEmailAttachInfo;
			}
			if(count!=doc_list_array.length) result.add(0);
			else result.add(1);
	
			int insertPrjEmailReceiptInfo = prjemailService.insertPrjEmailReceiptInfo(prjemailvo);
			result.add(insertPrjEmailReceiptInfo);
		}
		return result;
	}
	
	

	
	/**
	 * 1. 메소드명 : insertPrjEmailCorr
	 * 2. 작성일: 2022-01-26
	 * 3. 작성자: 소진희
	 * 4. 설명: email 전송하기 위해 db에 데이터 저장 (Correspondence)
	 * 5. 수정일:
	 */
	@RequestMapping(value = "insertPrjEmailCorr.do")
	@ResponseBody
	public Object insertPrjEmailCorr(PrjEmailVO prjemailvo,PrjTrVO prjtrvo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjemailvo.setReg_id(sessioninfo.getUser_id());
		String currentDateAndTime = CommonConst.currentDateAndTime();
		prjemailvo.setReg_date(currentDateAndTime);
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(currentDateAndTime);
		prjemailvo.setEmail_id(currentDateAndTime);
		prjemailvo.setEmail_sender_user_id(sessioninfo.getUser_id());
		prjemailvo.setEmail_send_date(currentDateAndTime);
		try {
			int getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
			int insertPrjEmail = prjemailService.insertPrjEmail(prjemailvo);
			result.add(insertPrjEmail);
			if(getEmailSentTimes==0 && insertPrjEmail>0) {
				prjdocumentindexvo.setSend_recv_date(currentDateAndTime.substring(0, 8));
				prjdocumentindexService.updateCorrInfoSendDateByFirstEmailing(prjdocumentindexvo);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("insertPrjEmailCorr: {}");
		}
		String docid_revid_docno = prjemailvo.getDocid_revid_docno();
		if(docid_revid_docno!=null) {
			String[] doc_list_array = docid_revid_docno.split("@@");
			int count = 0;
			for(int i=0;i<doc_list_array.length;i++) {
				String[] docid_revid_docno_revno_array = doc_list_array[i].split("&&");
				prjemailvo.setDoc_id(docid_revid_docno_revno_array[0]);
				prjemailvo.setRev_id(docid_revid_docno_revno_array[1]);
				prjemailvo.setDoc_no(docid_revid_docno_revno_array[2]);
				prjemailvo.setRev_no("");
				int insertPrjEmailAttachInfo = prjemailService.insertPrjEmailAttachInfo(prjemailvo);
				count+=insertPrjEmailAttachInfo;
			}
			if(count!=doc_list_array.length) result.add(0);
			else result.add(1);
	
			int insertPrjEmailReceiptInfo = prjemailService.insertPrjEmailReceiptInfo(prjemailvo);
			result.add(insertPrjEmailReceiptInfo);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertPrjEmailDC
	 * 2. 작성일: 2022-03-03
	 * 3. 작성자: 소진희
	 * 4. 설명: email 전송하기 위해 db에 데이터 저장 (Document Control 각 도서)
	 * 5. 수정일:
	 */
	@RequestMapping(value = "insertPrjEmailDC.do")
	@ResponseBody
	public Object insertPrjEmailDC(PrjEmailVO prjemailvo,PrjTrVO prjtrvo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjemailvo.setReg_id(sessioninfo.getUser_id());
		String currentDateAndTime = CommonConst.currentDateAndTime();
		prjemailvo.setReg_date(currentDateAndTime);
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(currentDateAndTime);
		prjemailvo.setEmail_id(currentDateAndTime);
		prjemailvo.setEmail_sender_user_id(sessioninfo.getUser_id());
		prjemailvo.setEmail_send_date(currentDateAndTime);
		try {
			int insertPrjEmail = prjemailService.insertPrjEmail(prjemailvo);
			result.add(insertPrjEmail);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("insertPrjEmailDC: {}");
		}
		String docid_revid_docno_revno = prjemailvo.getDocid_revid_docno_revno();
		if(docid_revid_docno_revno!=null) {
			String[] doc_list_array = docid_revid_docno_revno.split("@@");
			int count = 0;
			for(int i=0;i<doc_list_array.length;i++) {
				String[] docid_revid_docno_revno_array = doc_list_array[i].split("&&");
				prjemailvo.setDoc_id(docid_revid_docno_revno_array[0]);
				prjemailvo.setRev_id(docid_revid_docno_revno_array[1]);
				prjemailvo.setDoc_no(docid_revid_docno_revno_array[2]);
				prjemailvo.setRev_no(docid_revid_docno_revno_array[3]);
				int insertPrjEmailAttachInfo = prjemailService.insertPrjEmailAttachInfo(prjemailvo);
				count+=insertPrjEmailAttachInfo;
			}
			if(count!=doc_list_array.length) result.add(0);
			else result.add(1);
	
			int insertPrjEmailReceiptInfo = prjemailService.insertPrjEmailReceiptInfo(prjemailvo);
			result.add(insertPrjEmailReceiptInfo);
		}
		return result;
	}
}
