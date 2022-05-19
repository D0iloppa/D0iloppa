package kr.co.doiloppa.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjEmailTypeContentsVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjEmailVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjEmailTypeContentsService extends AbstractService {

	protected PrjDocumentIndexService  prjdocumentindexService;
	protected PrjTrService prjtrService;
	protected PrjEmailService  prjemailService;
	protected PrjStepService prjstepService;

	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setPrjTrService(PrjTrService prjtrService) {
		this.prjtrService = prjtrService;
	}
	@Autowired
	public void setPrjEmailService(PrjEmailService prjemailService) {
		this.prjemailService = prjemailService;
	}
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}
	
    @Value("#{config['server.domain']}")
    private String server_domain;
    
	public PrjEmailTypeContentsVO getPrjEmailTypeContents(PrjEmailTypeContentsVO prjemailtypecontentsvo){
		PrjEmailTypeContentsVO getPrjEmailTypeContents = selectOne("prjemailtypecontentsSqlMap.getPrjEmailTypeContents",prjemailtypecontentsvo);
		return getPrjEmailTypeContents;
	}
	public int insertPrjEmailTypeContents(PrjEmailTypeContentsVO prjemailtypecontentsvo){
		int insertPrjEmailTypeContents = insert("prjemailtypecontentsSqlMap.insertPrjEmailTypeContents",prjemailtypecontentsvo);
		return insertPrjEmailTypeContents;
	}
	public int updatePrjEmailTypeContents(PrjEmailTypeContentsVO prjemailtypecontentsvo){
		int updatePrjEmailTypeContents = insert("prjemailtypecontentsSqlMap.updatePrjEmailTypeContents",prjemailtypecontentsvo);
		return updatePrjEmailTypeContents;
	}
	
	String file_link_yn = "";
	String file_rename_yn = "";
	String file_nm_prefix = "";
	String email_type_id = "";
	public String AutoSendEmailForDRNFinalApproval(PrjEmailTypeVO prjemailtypevo,PrjEmailTypeContentsVO prjemailtypecontentsvo,PrjTrVO prjtrvo,PrjDocumentIndexVO prjdocumentindexvo,String email_sender_user_id) throws UnsupportedEncodingException, ParseException {
		String templateContents = "";
		PrjEmailTypeVO getAutoSendTREmailTemplate = selectOne("prjemailtypecontentsSqlMap.getAutoSendTREmailTemplate",prjemailtypevo);
		if(getAutoSendTREmailTemplate!=null) {//해당 email_feature(TR/VTR/STR)에 auto_send_yn='Y'가 있으면
			prjemailtypecontentsvo.setEmail_type_id(getAutoSendTREmailTemplate.getEmail_type_id());
			email_type_id = getAutoSendTREmailTemplate.getEmail_type_id();
			PrjEmailTypeContentsVO getPrjEmailTypeContents = selectOne("prjemailtypecontentsSqlMap.getPrjEmailTypeContents",prjemailtypecontentsvo);
			if(!getPrjEmailTypeContents.getEmail_receiver_addr().equals("") && getPrjEmailTypeContents.getEmail_receiver_addr()!=null) {
				//해당 template에 receiver가 설정되어있을 경우 contents내용가져와서 replace
				file_link_yn = getPrjEmailTypeContents.getFile_link_yn();
				file_rename_yn = getPrjEmailTypeContents.getFile_rename_yn();
				file_nm_prefix = getPrjEmailTypeContents.getFile_nm_prefix();
				String contents = autoSendTemplatePrefixReplace(getPrjEmailTypeContents.getContents(),prjtrvo,prjdocumentindexvo);
				String subject = autoSendTemplatePrefixReplace(getPrjEmailTypeContents.getSubject_prefix(),prjtrvo,prjdocumentindexvo);
				String email_sender_addr = getPrjEmailTypeContents.getEmail_sender_addr().split("!!")[0];
				String[] email_receiver_addr_array = getPrjEmailTypeContents.getEmail_receiver_addr().split(";");
				String[] email_referto_addr_array = getPrjEmailTypeContents.getEmail_referto_addr().split(";");
				String[] email_bcc_addr_array = getPrjEmailTypeContents.getEmail_bcc_addr().split(";");
				String email_receiver_addr = "";
				for(int i=0;i<email_receiver_addr_array.length;i++) {
					email_receiver_addr += email_receiver_addr_array[i].split("!!")[0]+";";
				}
				String email_referto_addr = "";
				for(int i=0;i<email_referto_addr_array.length;i++) {
					email_referto_addr += email_referto_addr_array[i].split("!!")[0]+";";
				}
				String email_bcc_addr = "";
				for(int i=0;i<email_bcc_addr_array.length;i++) {
					email_bcc_addr += email_bcc_addr_array[i].split("!!")[0]+";";
				}
				email_receiver_addr = email_receiver_addr.substring(0, email_receiver_addr.length()-1);
				email_referto_addr = email_referto_addr.substring(0, email_referto_addr.length()-1);
				email_bcc_addr = email_bcc_addr.substring(0, email_bcc_addr.length()-1);
				//p_prj_email_send_info에 insert
				PrjEmailVO prjemailvo = new PrjEmailVO();
				prjemailvo.setPrj_id(prjtrvo.getPrj_id());
				PrjTrVO getTrInfo = new PrjTrVO();
				if(prjtrvo.getProcessType().equals("TR")) {
					getTrInfo = prjtrService.getTrInfo(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					getTrInfo = prjtrService.getVTrInfo(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					getTrInfo = prjtrService.getSTrInfo(prjtrvo);
				}
				String currentDateAndTime = CommonConst.currentDateAndTime();
				prjemailvo.setUser_data00(getTrInfo.getTr_no());
				prjemailvo.setEmail_type_id(email_type_id);
				prjemailvo.setEmail_id(currentDateAndTime);
				prjemailvo.setSubject(subject);
				prjemailvo.setContents(contents);
				prjemailvo.setEmail_sender_user_id(email_sender_user_id);
				prjemailvo.setEmail_sender_addr(email_sender_addr);
				prjemailvo.setEmail_receiver_addr(email_receiver_addr);
				prjemailvo.setEmail_send_date(CommonConst.currentDate());
				prjemailvo.setEmail_refer_to(email_referto_addr);
				prjemailvo.setEmail_bcc(email_bcc_addr);
				prjemailvo.setReg_id(email_sender_user_id);
				prjemailvo.setReg_date(currentDateAndTime);
				int getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
				int insertPrjEmail = prjemailService.insertPrjEmail(prjemailvo);
				

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
						if(getEmailSentTimes==0) {	//최초발송일 때 Send Date와 Required Date 업데이트
							int updateTrSendDate = prjtrService.updateTrSendDate(prjtrvo);
						}
						getTrFile = prjtrService.getTrFile(prjtrvo);
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						if(getEmailSentTimes==0) {	//최초발송일 때 Send Date와 Required Date 업데이트
							int updateVTrSendDate = prjtrService.updateVTrSendDate(prjtrvo);
						}
						getTrFile = prjtrService.getVTrFile(prjtrvo);
					}else if(prjtrvo.getProcessType().equals("STR")) {
						if(getEmailSentTimes==0) {	//최초발송일 때 Send Date와 Required Date 업데이트
							int updateSTrSendDate = prjtrService.updateSTrSendDate(prjtrvo);
						}
						getTrFile = prjtrService.getSTrFile(prjtrvo);
					}
					PrjStepVO prjstepvo = new PrjStepVO();
					prjstepvo.setPrj_id(prjemailvo.getPrj_id());
					prjstepvo.setTr_id(prjtrvo.getTr_id());
					prjstepvo.setMod_id(email_sender_user_id);
					prjstepvo.setMod_date(currentDateAndTime);
					
					for(int i=0;i<getTrFile.size();i++) {
						prjstepvo.setDoc_id(getTrFile.get(i).getDoc_id());
						prjstepvo.setRev_id(getTrFile.get(i).getRev_id());
						prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
						if(prjtrvo.getProcessType().equals("TR")) {
							PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
							prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
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
							PrjTrVO getTrInfo2 = prjtrService.getVTrInfo(prjtrvo);
							prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
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
							PrjTrVO getTrInfo2 = prjtrService.getSTrInfo(prjtrvo);
							prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
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
				

				String docid_revid_docno_revno = prjemailvo.getDocid_revid_docno_revno();
				if(docid_revid_docno_revno!=null) {
					String[] doc_list_array = docid_revid_docno_revno.split("@@");
					for(int i=0;i<doc_list_array.length;i++) {
						String[] docid_revid_docno_revno_array = doc_list_array[i].split("&&");
						prjemailvo.setDoc_id(docid_revid_docno_revno_array[0]);
						prjemailvo.setRev_id(docid_revid_docno_revno_array[1]);
						prjemailvo.setDoc_no(docid_revid_docno_revno_array[2]);
						prjemailvo.setRev_no(docid_revid_docno_revno_array[3]);
						int insertPrjEmailAttachInfo = prjemailService.insertPrjEmailAttachInfo(prjemailvo);
					}
					int insertPrjEmailReceiptInfo = prjemailService.insertPrjEmailReceiptInfo(prjemailvo);
				}
			}
		}
		
		return templateContents;
	}
	
	public String autoSendTemplatePrefixReplace(String replaceString,PrjTrVO prjtrvo,PrjDocumentIndexVO prjdocumentindexvo) throws UnsupportedEncodingException { 
		String doc_list_zip = "";
		String doc_list_table = "";
		String doc_list_table_with_discipline = "";
		String doc_list_table_with_class = "";
		String doc_list_table_with_return_status = "";
		
		PrjTrVO getTrInfo = new PrjTrVO();
		List<PrjDocumentIndexVO> getTRFileInfo = new ArrayList<PrjDocumentIndexVO>();
		if(prjtrvo.getProcessType().equals("TR")) {
			getTrInfo = prjtrService.getTrInfo(prjtrvo);
			getTRFileInfo = prjdocumentindexService.getTRFileInfo(prjdocumentindexvo);
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			getTrInfo = prjtrService.getVTrInfo(prjtrvo);
			getTRFileInfo = prjdocumentindexService.getVTRFileInfo(prjdocumentindexvo);
		}else if(prjtrvo.getProcessType().equals("STR")) {
			getTrInfo = prjtrService.getSTrInfo(prjtrvo);
			getTRFileInfo = prjdocumentindexService.getSTRFileInfo(prjdocumentindexvo);
		}
		PrjEmailVO prjemailvo = new PrjEmailVO();
		prjemailvo.setPrj_id(prjtrvo.getPrj_id());
		prjemailvo.setUser_data00(getTrInfo.getTr_no());
		int getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
		
		PrjDocumentIndexVO isExistDocNo = null;
		if(getTrInfo.getTr_no()!=null && getTrInfo.getTr_no()!="") {
			//TR Outgoing의 경우 생성된 TR COVER를 이메일 테이블에 뿌려주기 위해 doc_id를 알아와야함
			prjdocumentindexvo.setDoc_no(getTrInfo.getTr_no());
			isExistDocNo = prjdocumentindexService.isExistDocNo(prjdocumentindexvo);
		}
		if(isExistDocNo==null) {//TR COVER 없을 경우
			return "";
		}
		
    	String due_date = "NO";
    	if(!getTrInfo.getReq_date().equals("") && getTrInfo.getReq_date()!=null){
    		due_date = getTrInfo.getReq_date();
    	}
    	String pk = "";
    	String docid_revid_docno_revno = "";
    	for(int i=0;i<getTRFileInfo.size();i++){
    		pk += getTRFileInfo.get(i).getDoc_id() + "&&" + getTRFileInfo.get(i).getRev_id() + "@@";
    		docid_revid_docno_revno += getTRFileInfo.get(i).getDoc_id() + "&&" + getTRFileInfo.get(i).getRev_id() + "&&" + getTRFileInfo.get(i).getDoc_no() + "&&" + getTRFileInfo.get(i).getRev_no() + "@@";
    	}
    	pk = pk.substring(0, pk.length()-2);
    	docid_revid_docno_revno = docid_revid_docno_revno.substring(0, docid_revid_docno_revno.length()-2);
    	
    	
    	
    	List<PrjDocumentIndexVO> TRDocumentInfoList = new ArrayList<PrjDocumentIndexVO>();
		String[] doc = pk.split("@@");
		for(int i=0;i<doc.length;i++) {
			String[] docrevId = doc[i].split("&&");
			prjdocumentindexvo.setDoc_id(docrevId[0]);
			prjdocumentindexvo.setRev_id(docrevId[1]);
			PrjDocumentIndexVO getDocumentInfo = new PrjDocumentIndexVO();
			PrjDocumentIndexVO isDuplication = new PrjDocumentIndexVO();
			if(prjtrvo.getProcessType().equals("TR")) {
				getDocumentInfo = prjdocumentindexService.getDocumentInfo(prjdocumentindexvo);
				isDuplication = prjtrService.isDuplicationDoc(prjdocumentindexvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				getDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
				isDuplication = prjtrService.isDuplicationDocVTR(prjdocumentindexvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				getDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
				isDuplication = prjtrService.isDuplicationDocSTR(prjdocumentindexvo);
			}
			if(isDuplication!=null) {
				getDocumentInfo.setDuplicationDoc(isDuplication.getDuplicationDoc());
			}else{
				getDocumentInfo.setDuplicationDoc("N");
			}
			TRDocumentInfoList.add(getDocumentInfo);
		}

		String downloadFiles = "";
    	downloadFiles = getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type() + "@@";	//COVER FILE
    	downloadFiles += docid_revid_docno_revno.replaceAll("&&","%%");
    	String renameYN = "N";
    	if(file_rename_yn.equals("Y")) renameYN="Y";
    	String fileRenameSet = file_nm_prefix;

    	doc_list_zip = "<a href=\""+server_domain+"/fileDownloadEmail.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&renameYN="+URLEncoder.encode(renameYN, "UTF-8")+"&fileRenameSet="+URLEncoder.encode(fileRenameSet, "UTF-8")+"&downloadFiles="+URLEncoder.encode(downloadFiles, "UTF-8")+"\">Doc_list.zip</a>";

    	if(file_link_yn.equals("Y")) {
    		doc_list_table = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table_with_discipline = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Discipline</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table_with_class = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Category</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table_with_return_status = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Reutrn Status</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"\"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	doc_list_table_with_discipline += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	doc_list_table_with_class += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	doc_list_table_with_return_status += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	for(int i=0;i<TRDocumentInfoList.size();i++){
	    		String downloadFile = TRDocumentInfoList.get(i).getDoc_id() + "_" +TRDocumentInfoList.get(i).getRev_id() + "." + TRDocumentInfoList.get(i).getFile_type();
	    		doc_list_table += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadFileEmail.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(downloadFile, "UTF-8")+"&rfile_nm="+URLEncoder.encode(TRDocumentInfoList.get(i).getRfile_nm(), "UTF-8")+"&email_type_id="+URLEncoder.encode(email_type_id, "UTF-8")+"&email_id=@@email_id_date@@\">"+TRDocumentInfoList.get(i).getDoc_no()+"</a></td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
	    		doc_list_table_with_discipline += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadFileEmail.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(downloadFile, "UTF-8")+"&rfile_nm="+URLEncoder.encode(TRDocumentInfoList.get(i).getRfile_nm(), "UTF-8")+"&email_type_id="+URLEncoder.encode(email_type_id, "UTF-8")+"&email_id=@@email_id_date@@\">"+TRDocumentInfoList.get(i).getDoc_no()+"</a></td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getDiscipline()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
	    		doc_list_table_with_class += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadFileEmail.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(downloadFile, "UTF-8")+"&rfile_nm="+URLEncoder.encode(TRDocumentInfoList.get(i).getRfile_nm(), "UTF-8")+"&email_type_id="+URLEncoder.encode(email_type_id, "UTF-8")+"&email_id=@@email_id_date@@\">"+TRDocumentInfoList.get(i).getDoc_no()+"</a></td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getCategory_code()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
	    		doc_list_table_with_return_status += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadFileEmail.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(downloadFile, "UTF-8")+"&rfile_nm="+URLEncoder.encode(TRDocumentInfoList.get(i).getRfile_nm(), "UTF-8")+"&email_type_id="+URLEncoder.encode(email_type_id, "UTF-8")+"&email_id=@@email_id_date@@\">"+TRDocumentInfoList.get(i).getDoc_no()+"</a></td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td></td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
			}
	    	doc_list_table += "</tbody></table>";
	    	doc_list_table_with_discipline += "</tbody></table>";
	    	doc_list_table_with_class += "</tbody></table>";
	    	doc_list_table_with_return_status += "</tbody></table>";
    	}else {
	    	doc_list_table = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table_with_discipline = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Discipline</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table_with_class = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Category</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table_with_return_status = "<table width=\"700\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><th>No</th><th>Document No.</th><th>Rev.</th><th>Reutrn Status</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>";
	    	doc_list_table += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	doc_list_table_with_discipline += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	doc_list_table_with_class += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
	    	doc_list_table_with_return_status += "<tr style=\"text-align:center;\"><td>*</td><td><a style=\"text-decoration:underline;color:blue;\" href=\""+server_domain+"/downloadCommentFile.do?prj_id="+URLEncoder.encode(prjtrvo.getPrj_id(), "UTF-8")+"&sfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"&rfile_nm="+URLEncoder.encode(getTrInfo.getTr_no()+"."+isExistDocNo.getFile_type(), "UTF-8")+"\">Transmittal Cover</a></td><td>*</td><td>*</td><td>"+isExistDocNo.getTitle()+"</td><td>"+isExistDocNo.getFile_type()+"</td><td>"+CommonConst.intByte(isExistDocNo.getFile_size())+"</td></tr>";
    		for(int i=0;i<TRDocumentInfoList.size();i++){
	    		doc_list_table += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td>"+TRDocumentInfoList.get(i).getDoc_no()+"</td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
	    		doc_list_table_with_discipline += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td>"+TRDocumentInfoList.get(i).getDoc_no()+"</td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getDiscipline()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
	    		doc_list_table_with_class += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td>"+TRDocumentInfoList.get(i).getDoc_no()+"</td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getCategory_code()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
	    		doc_list_table_with_return_status += "<tr style=\"text-align:center;\"><td>"+(i+1)+"</td><td>"+TRDocumentInfoList.get(i).getDoc_no()+"</td><td>"+TRDocumentInfoList.get(i).getRev_no()+"</td><td>"+TRDocumentInfoList.get(i).getReturn_status()+"</td><td>"+TRDocumentInfoList.get(i).getTitle()+"</td><td>"+TRDocumentInfoList.get(i).getFile_type()+"</td><td>"+CommonConst.intByte(TRDocumentInfoList.get(i).getFile_size())+"</td></tr>";
			}
	    	doc_list_table += "</tbody></table>";
	    	doc_list_table_with_discipline += "</tbody></table>";
	    	doc_list_table_with_class += "</tbody></table>";
	    	doc_list_table_with_return_status += "</tbody></table>";
    	}
    	replaceString = replaceString.replaceAll("%TR_NO%",getTrInfo.getTr_no())
				.replaceAll("%TR_SUBJECT%",getTrInfo.getTr_subject())
				.replaceAll("%TR_ISSUE_PURPOSE%",getTrInfo.getIssuepurpose())
				.replaceAll("%TR_ISSUE_DISC_NAME%",getTrInfo.getDiscipline())
				.replaceAll("%TR_DUE_DATE%",CommonConst.stringdateTOYYYYMMDD(due_date))
				.replaceAll("%DOC_LIST_ZIP%",doc_list_zip)
				.replaceAll("%DOC_LIST_TABLE%",doc_list_table)
				.replaceAll("%DOC_LIST_TABLE_WITH_DISCIPLINE%",doc_list_table_with_discipline)
				.replaceAll("%DOC_LIST_TABLE_WITH_CLASS%",doc_list_table_with_class)
				.replaceAll("%DOC_LIST_TABLE_WITH_RETURN_STATUS%",doc_list_table_with_return_status);
    	
		return replaceString;
	}
}