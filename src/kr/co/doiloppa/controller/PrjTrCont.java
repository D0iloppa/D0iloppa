package kr.co.doiloppa.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ibm.icu.text.SimpleDateFormat;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.common.util.FileExtFilter;
import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocTrNoChgHistVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjDrnInfoHistVO;
import kr.co.doiloppa.model.PrjDrnInfoVO;
import kr.co.doiloppa.model.PrjEmailTypeContentsVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjEmailVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.PrjStepVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.PrjCodeSettingsService;
import kr.co.doiloppa.service.PrjDocumentIndexService;
import kr.co.doiloppa.service.PrjDrnInfoHistService;
import kr.co.doiloppa.service.PrjDrnInfoService;
import kr.co.doiloppa.service.PrjEmailService;
import kr.co.doiloppa.service.PrjEmailTypeContentsService;
import kr.co.doiloppa.service.PrjEmailTypeService;
import kr.co.doiloppa.service.PrjInfoService;
import kr.co.doiloppa.service.PrjStepService;
import kr.co.doiloppa.service.PrjTrService;
import kr.co.doiloppa.service.ProcessInfoService;
import kr.co.doiloppa.service.TreeService;
import kr.co.doiloppa.service.UsersService;

@Controller
@RequestMapping("/*")
public class PrjTrCont {

	protected PrjTrService prjtrService;
	protected PrjCodeSettingsService prjcodesettingsService;
	protected PrjInfoService prjinfoService;
	protected PrjEmailService prjemailService;
	protected PrjStepService prjstepService;
	protected TreeService treeService;
	protected PrjDocumentIndexService prjdocumentindexService;
	protected ProcessInfoService processinfoService;
	protected PrjDrnInfoService prjdrninfoService;
	protected PrjDrnInfoHistService  prjdrninfohistService;
	protected UsersService  usersService;
	
	@Autowired
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	@Autowired
	public void setPrjTrService(PrjTrService prjtrService) {
		this.prjtrService = prjtrService;
	}
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setPrjEmailService(PrjEmailService prjemailService) {
		this.prjemailService = prjemailService;
	}
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setProcessInfoService(ProcessInfoService processinfoService) {
		this.processinfoService = processinfoService;
	}
	@Autowired
	public void setPrjDrnInfoService(PrjDrnInfoService prjdrninfoService) {
		this.prjdrninfoService = prjdrninfoService;
	}
	@Autowired
	public void setPrjDrnInfoHistService(PrjDrnInfoHistService prjdrninfohistService) {
		this.prjdrninfohistService = prjdrninfohistService;
	}

    @Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
    
    @Value("#{config['file.upload.prj.doc.path']}")
    private String DOC_PATH;

    @Value("#{config['file.upload.prj.template.path']}")
    private String TEMPLATE_PATH;

	/**
	 * 1. 메소드명 : changeTrNo
	 * 2. 작성일: 2022-04-13
	 * 3. 작성자: 소진희
	 * 4. 설명: TR NO 변경 (ADMIN 페이지)
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "changeTrNo.do")
	@ResponseBody
	public Object changeTrNo(HttpServletRequest request,PrjTrVO prjtrvo,PrjDocTrNoChgHistVO prjdoctrnochghistvo) {
		List<Object> result = new ArrayList<Object>();
		int isExistTrNo = prjtrService.isExistTrNo(prjtrvo);
		if(isExistTrNo>0) {//변경할 TR NO가 이미 있는 경우
			result.add(prjtrvo.getChange_tr_no()+" is already exists.");
		}else {
			PrjTrVO getChangingTrInfo = prjtrService.getChangingTrInfo(prjtrvo);
			int changeTrNo = prjtrService.changeTrNo(prjtrvo);
			if(changeTrNo>0) {//변경한 경우
				prjdoctrnochghistvo.setLog_date(CommonConst.currentDateAndTime());
				prjdoctrnochghistvo.setPrj_id(prjtrvo.getPrj_id());
				prjdoctrnochghistvo.setTr_id(getChangingTrInfo.getTr_id());
				prjdoctrnochghistvo.setOld_tr_no(prjtrvo.getOld_tr_no());
				prjdoctrnochghistvo.setChg_tr_no(prjtrvo.getChange_tr_no());
				prjdoctrnochghistvo.setI_tr_no(getChangingTrInfo.getItr_no());
				HttpSession session = request.getSession();
				UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
				prjdoctrnochghistvo.setReg_id(sessioninfo.getUser_id());
				String remote_ip = CommonConst.getIpAddress(request);
				prjdoctrnochghistvo.setRemote_ip(remote_ip);
				prjtrService.insertTrNoChgHist(prjdoctrnochghistvo);
				result.add("Transmittal NO has Changed");
			}else {//old_tr_no에 해당하는 tr_no가 없어서 변경 못한 경우
				result.add(prjtrvo.getOld_tr_no()+" is not exists.");
			}
		}
		return result;
	}
	/**
	 * 1. 메소드명 : getTrNoChgHist
	 * 2. 작성일: 2022-04-13
	 * 3. 작성자: 소진희
	 * 4. 설명: TR NO 변경된 내역 보기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTrNoChgHist.do")
	@ResponseBody
	public Object getTrNoChgHist(HttpServletRequest request,PrjTrVO prjtrvo,PrjDocTrNoChgHistVO prjdoctrnochghistvo) {
		List<Object> result = new ArrayList<Object>();
		List<PrjDocTrNoChgHistVO> getTrNoChgHist = prjtrService.getTrNoChgHist(prjdoctrnochghistvo);
		result.add(getTrNoChgHist);
		return result;
	}
	/**
	 * 1. 메소드명 : getPrjTrHistory
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 소진희
	 * 4. 설명: 각 도서의 Transmittal history 리스트 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getPrjTrHistory.do")
	@ResponseBody
	public Object getPrjTrHistory(PrjTrVO prjtrvo) {
		List<Object> result = new ArrayList<Object>();
		if(prjtrvo.getDoc_table().equals("DCI")){
			List<PrjTrVO> getPrjTrHistory = prjtrService.getPrjTrHistory(prjtrvo);
			result.add(getPrjTrHistory);
		}else if(prjtrvo.getDoc_table().equals("VDCI")){
			List<PrjTrVO> getPrjVTrHistory = prjtrService.getPrjVTrHistory(prjtrvo);
			result.add(getPrjVTrHistory);
		}else if(prjtrvo.getDoc_table().equals("SDCI")){
			List<PrjTrVO> getPrjSTrHistory = prjtrService.getPrjSTrHistory(prjtrvo);
			result.add(getPrjSTrHistory);
		}
		return result;
	}
	/**
	 * 1. 메소드명 : insertTrOutgoingSave
	 * 2. 작성일: 2022-01-08
	 * 3. 작성자: 소진희
	 * 4. 설명: TR OUTGOING 내용 저장 (동시에 ITransmittal No 생성)
	 * 5. 수정일: 2022-01-10 TR OUTGOING 내용 업데이트 추가
	 * @throws ParseException 
	 */
	@RequestMapping(value = "insertTrOutgoingSave.do")
	@ResponseBody
	public Object insertTrOutgoingSave(HttpServletRequest request,PrjInfoVO prjinfovo,PrjTrVO prjtrvo,PrjCodeSettingsVO prjcodesettingsvo,PrjStepVO prjstepvo,ProcessInfoVO processinfovo) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjtrvo.setTr_writer_id(sessioninfo.getUser_id());
		prjtrvo.setTr_modifier_id(sessioninfo.getUser_id());
		prjtrvo.setReg_id(sessioninfo.getUser_id());
		prjtrvo.setReg_id(sessioninfo.getUser_id());
		prjtrvo.setReg_date(currentDateAndTime);
		prjtrvo.setMod_id(sessioninfo.getUser_id());
		prjtrvo.setMod_date(currentDateAndTime);
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(currentDateAndTime);
		PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
		prjtrvo.setPrj_no(getPrjInfo.getPrj_no());
		prjtrvo.setInout("OUT");
		prjtrvo.setTr_from("HHI");
		prjtrvo.setTr_to("OWNER");
		if(prjtrvo.getDCCSendDateUpdate().equals("DCCSendDateUpdate")) {
			String tr_id = prjtrvo.getNewOrUpdate();
			prjtrvo.setTr_id(tr_id);
			int updateTrSendDate = 0;
			if(prjtrvo.getProcessType().equals("TR")) {
				updateTrSendDate = prjtrService.updateTrSendDate(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				updateTrSendDate = prjtrService.updateVTrSendDate(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				updateTrSendDate = prjtrService.updateSTrSendDate(prjtrvo);
			}
			result.add("DCCorADMIN SENDDATE UPDATE");
			result.add(updateTrSendDate);
			
			
			//TR OUTGOING ISSUE시 Send Date를 Actual Date로 업데이트
			String update_tr_id = prjtrvo.getNewOrUpdate();
			prjtrvo.setTr_id(update_tr_id);
			List<PrjTrVO> getUpdateTrInfo = new ArrayList<PrjTrVO>();
			if(prjtrvo.getProcessType().equals("TR")) {
				getUpdateTrInfo = prjtrService.getTrFile(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				getUpdateTrInfo = prjtrService.getVTrFile(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				getUpdateTrInfo = prjtrService.getSTrFile(prjtrvo);
			}
			for(int i=0;i<getUpdateTrInfo.size();i++) {
				prjstepvo.setDoc_id(getUpdateTrInfo.get(i).getDoc_id());
				prjstepvo.setRev_id(getUpdateTrInfo.get(i).getRev_id());
				if(prjtrvo.getProcessType().equals("TR")) {
					PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
					prjstepvo.setActual_date(getTrInfo2.getSend_date());
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
					prjstepvo.setSet_val2("TR OUTGOING ISSUE시 Send Date");
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
					prjstepvo.setActual_date(getTrInfo2.getSend_date());
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
					prjstepvo.setSet_val2("VTR OUTGOING ISSUE시 Send Date");
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
					prjstepvo.setActual_date(getTrInfo2.getSend_date());
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
					prjstepvo.setSet_val2("STR OUTGOING ISSUE시 Send Date");
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
			return result;
		}
		if(prjtrvo.getNewOrUpdate().equals("new")) {
			String tr_id = "";
			if(prjtrvo.getProcessType().equals("TR")) {
				tr_id = prjtrService.insertTrInfo(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				tr_id = prjtrService.insertVTrInfo(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				tr_id = prjtrService.insertSTrInfo(prjtrvo);
			}
			prjtrvo.setTr_id(tr_id);
			prjstepvo.setTr_id(tr_id);
			try {
				int count = 0;
				String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
				for(int i=0;i<docList.length;i++) {
					String[] docinfo = docList[i].split("&&");
					prjtrvo.setDoc_id(docinfo[0]);
					prjtrvo.setRev_id(docinfo[1]);
					prjtrvo.setRev_no(docinfo[2]);
					if(prjtrvo.getProcessType().equals("TR")) {
						int insertTrFile = prjtrService.insertTrFile(prjtrvo);
						count += insertTrFile;
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
						count += insertTrFile;
					}else if(prjtrvo.getProcessType().equals("STR")) {
						int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
						count += insertTrFile;
					}
				}
				if(count!=docList.length) result.add(0);
				else result.add(1);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("insertTrOutgoingSave - insertTrFile: {}");
			}
			result.add(tr_id);
		}else {//update
			String tr_id = prjtrvo.getNewOrUpdate();
			prjtrvo.setTr_id(tr_id);
			if(prjtrvo.getTr_no().equals("")) {
				prjtrvo.setTr_no(null);
			}
			if(prjtrvo.getProcessType().equals("TR")) {
				int updateTrInfo = prjtrService.updateTrInfo(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				int updateTrInfo = prjtrService.updateVTrInfo(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				int updateTrInfo = prjtrService.updateSTrInfo(prjtrvo);
			}
			if(prjtrvo.getDocid_revid_revno()!="") {
			String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
			int count = 0;
				for(int i=0;i<docList.length;i++) {
					String[] docinfo = docList[i].split("&&");
					prjtrvo.setDoc_id(docinfo[0]);
					prjtrvo.setRev_id(docinfo[1]);
					prjtrvo.setRev_no(docinfo[2]);
					if(prjtrvo.getProcessType().equals("TR")) {
						int selectTrFile = prjtrService.selectTrFile(prjtrvo);
						if(selectTrFile>0) {	//기존에 있던 도서
							int deleteTrFile = prjtrService.deleteTrFile(prjtrvo);
							count += deleteTrFile;
						}else {	//기존에 없던 도서
							int insertTrFile = prjtrService.insertTrFile(prjtrvo);
							count += insertTrFile;
						}
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						int selectTrFile = prjtrService.selectVTrFile(prjtrvo);
						if(selectTrFile>0) {	//기존에 있던 도서
							int deleteTrFile = prjtrService.deleteVTrFile(prjtrvo);
							count += deleteTrFile;
						}else {	//기존에 없던 도서
							int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
							count += insertTrFile;
						}
					}else if(prjtrvo.getProcessType().equals("STR")) {
						int selectTrFile = prjtrService.selectSTrFile(prjtrvo);
						if(selectTrFile>0) {	//기존에 있던 도서
							int deleteTrFile = prjtrService.deleteSTrFile(prjtrvo);
							count += deleteTrFile;
						}else {	//기존에 없던 도서
							int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
							count += insertTrFile;
						}
					}
				}
				if(count!=docList.length) result.add(0);
				else result.add(1);
			}
			result.add(tr_id);
		}
		return result;
	}
	/**
	 * 1. 메소드명 : trOutGoingExcelDownload
	 * 2. 작성일: 2021. 02. 14.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "trOutGoingExcelDownload.do")
	@ResponseBody
	public void trOutGoingExcelDownload(PrjTrVO prjtrvo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getTrOutGoingList = prjtrService.getTrInOutSearch(prjtrvo);
		//System.out.println(prjtrvo.getJsonRowDatas());
		String fileName = prjtrvo.getFileName();
		String chkTrStrVtr = prjtrvo.getPurpose();
		String chkTrInOut = prjtrvo.getInout();
		
		String chkTrPeriod = prjtrvo.getTr_period();
		String from_search = prjtrvo.getTr_from_search();
		String to_search = prjtrvo.getTr_to_search();
		String from_to_search;
		if(chkTrPeriod.equals("all")) {
			from_to_search = "ALL";
		}else {
			/*
			 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 * LocalDate form_date = LocalDate.parse(from_search, formatter); LocalDate
			 * to_date = LocalDate.parse(to_search, formatter);
			 */

			from_to_search = from_search+"~"+to_search;
		}
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.trOutGoingExcelDownload(response, "titleList",  getTrOutGoingList, fileName , chkTrStrVtr, chkTrInOut, from_to_search, prjtrvo.getPrj_nm());
	}
	/**
	 * 1. 메소드명 : vtrOutGoingExcelDownload
	 * 2. 작성일: 2021. 02. 14.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "vtrOutGoingExcelDownload.do")
	@ResponseBody
	public void vtrOutGoingExcelDownload(PrjTrVO prjtrvo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getVTrOutGoingList = prjtrService.getVTrInOutSearch(prjtrvo);
		//System.out.println(prjtrvo.getJsonRowDatas());
		String fileName = prjtrvo.getFileName();
		String chkTrStrVtr = prjtrvo.getPurpose();
		String chkTrInOut = prjtrvo.getInout();
		
		String chkTrPeriod = prjtrvo.getTr_period();
		String from_search = prjtrvo.getTr_from_search();
		String to_search = prjtrvo.getTr_to_search();
		String from_to_search;
		if(chkTrPeriod.equals("all")) {
			from_to_search = "ALL";
		}else {
			/*
			 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 * LocalDate form_date = LocalDate.parse(from_search, formatter); LocalDate
			 * to_date = LocalDate.parse(to_search, formatter);
			 */

			from_to_search = from_search+"~"+to_search;
		}
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.trOutGoingExcelDownload(response, "titleList",  getVTrOutGoingList, fileName , chkTrStrVtr, chkTrInOut, from_to_search, prjtrvo.getPrj_nm());
	}
	/**
	 * 1. 메소드명 : strOutGoingExcelDownload
	 * 2. 작성일: 2021. 02. 14.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "strOutGoingExcelDownload.do")
	@ResponseBody
	public void strOutGoingExcelDownload(PrjTrVO prjtrvo, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getSTrOutGoingList = prjtrService.getSTrInOutSearch(prjtrvo);
		//System.out.println(prjtrvo.getJsonRowDatas());
		String fileName = prjtrvo.getFileName();
		String chkTrStrVtr = prjtrvo.getPurpose();
		String chkTrInOut = prjtrvo.getInout();
		
		String chkTrPeriod = prjtrvo.getTr_period();
		String from_search = prjtrvo.getTr_from_search();
		String to_search = prjtrvo.getTr_to_search();
		String from_to_search;
		if(chkTrPeriod.equals("all")) {
			from_to_search = "ALL";
		}else {
			/*
			 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 * LocalDate form_date = LocalDate.parse(from_search, formatter); LocalDate
			 * to_date = LocalDate.parse(to_search, formatter);
			 */

			from_to_search = from_search+"~"+to_search;
		}
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.trOutGoingExcelDownload(response, "titleList",  getSTrOutGoingList, fileName , chkTrStrVtr, chkTrInOut, from_to_search, prjtrvo.getPrj_nm());
	}
	/**
	 * 1. 메소드명 : getTrInOutSearch
	 * 2. 작성일: 2022-01-08
	 * 3. 작성자: 소진희
	 * 4. 설명: TR OUTGOING 또는 INCOMING 리스트 검색
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTrInOutSearch.do")
	@ResponseBody
	public Object getTrInOutSearch(PrjInfoVO prjinfovo,PrjTrVO prjtrvo,PrjCodeSettingsVO prjcodesettingsvo) {
		List<Object> result = new ArrayList<Object>();
		if(prjtrvo.getProcessType().equals("TR")) {
			List<PrjTrVO> getPrjInfo = prjtrService.getTrInOutSearch(prjtrvo);
			result.add(getPrjInfo);
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			List<PrjTrVO> getPrjInfo = prjtrService.getVTrInOutSearch(prjtrvo);
			result.add(getPrjInfo);
		}else if(prjtrvo.getProcessType().equals("STR")) {
			List<PrjTrVO> getPrjInfo = prjtrService.getSTrInOutSearch(prjtrvo);
			result.add(getPrjInfo);
		}
		return result;
	}
	/**
	 * 1. 메소드명 : getTrInfo
	 * 2. 작성일: 2022-01-08
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 TR에 대한 정보와 첨부문서 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTrInfo.do")
	@ResponseBody
	public Object getTrInfo(PrjInfoVO prjinfovo,PrjTrVO prjtrvo,PrjEmailVO prjemailvo,PrjCodeSettingsVO prjcodesettingsvo,PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		PrjTrVO getTrInfo = new PrjTrVO();
		if(prjtrvo.getProcessType().equals("TR")) {
			getTrInfo = prjtrService.getTrInfo(prjtrvo);
			result.add(getTrInfo);
			//List<PrjTrVO> getTrFile = prjtrService.getTrFile(prjtrvo);
			//result.add(getTrFile);
			List<PrjDocumentIndexVO> getTRFileInfo = prjdocumentindexService.getTRFileInfo(prjdocumentindexvo);
			result.add(getTRFileInfo);
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			getTrInfo = prjtrService.getVTrInfo(prjtrvo);
			result.add(getTrInfo);
			//List<PrjTrVO> getTrFile = prjtrService.getVTrFile(prjtrvo);
			//result.add(getTrFile);
			List<PrjDocumentIndexVO> getVTRFileInfo = prjdocumentindexService.getVTRFileInfo(prjdocumentindexvo);
			result.add(getVTRFileInfo);
		}else if(prjtrvo.getProcessType().equals("STR")) {
			getTrInfo = prjtrService.getSTrInfo(prjtrvo);
			result.add(getTrInfo);
			//List<PrjTrVO> getTrFile = prjtrService.getSTrFile(prjtrvo);
			//result.add(getTrFile);
			List<PrjDocumentIndexVO> getSTRFileInfo = prjdocumentindexService.getSTRFileInfo(prjdocumentindexvo);
			result.add(getSTRFileInfo);
		}
		int getEmailSentTimes = 0;
		if(getTrInfo!=null) {
			prjemailvo.setUser_data00(getTrInfo.getTr_no());
			getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
		}
		result.add(getEmailSentTimes);
		
		if(getTrInfo!=null && getTrInfo.getTr_no()!=null && getTrInfo.getTr_no()!="") {
			//TR Incoming의 경우 페이지에서 생성된 해당 TR의 정보를 가져올 때 comment file이 있는지 체크
			//TR Outgoing의 경우 생성된 TR COVER를 이메일 테이블에 뿌려주기 위해 doc_id를 알아와야함
			prjdocumentindexvo.setDoc_no(getTrInfo.getTr_no());
			PrjDocumentIndexVO isExistDocNo = prjdocumentindexService.isExistDocNo(prjdocumentindexvo);
			result.add(isExistDocNo);
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getOutRegIncomingTrInfo
	 * 2. 작성일: 2022-04-04
	 * 3. 작성자: 소진희
	 * 4. 설명: TR OUTGOING에서 Issued된 TR 선택(다중선택가능) 후 Incoming Register하려할 때 선택한 TR들의 정보(첨부한 도서들) 등을 불러옴
	 * 5. 수정일: 2022-05-18 이태돈 부장님 요청에 따른 수정. 한번 Incoming 등록된 도서라도 (리비전올리지않고) 다시 Incoming 등록할 수 있도록 수정.
	 * 			위의 수정사항 보류.
	 */
	@RequestMapping(value = "getOutRegIncomingTrInfo.do")
	@ResponseBody
	public Object getOutRegIncomingTrInfo(PrjInfoVO prjinfovo,PrjTrVO prjtrvo,PrjEmailVO prjemailvo,PrjCodeSettingsVO prjcodesettingsvo,PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		String[] tr_ids = prjtrvo.getTr_id().split(",");
		List<PrjDocumentIndexVO> getTRFileInfo = new ArrayList<PrjDocumentIndexVO>();
		for(int i=0;i<tr_ids.length;i++) {
			prjtrvo.setTr_id(tr_ids[i]);
			prjdocumentindexvo.setTr_id(tr_ids[i]);
			
			PrjTrVO getTrInfo = new PrjTrVO();
			if(prjtrvo.getProcessType().equals("TR")) {
				getTrInfo = prjtrService.getTrInfo(prjtrvo);
				result.add(getTrInfo);
				List<PrjDocumentIndexVO> getTRFile = prjdocumentindexService.getTRFileInfo(prjdocumentindexvo);
				int count = 0;
				for(int j=0;j<getTRFile.size();j++) {
					int getUsedTRIncoming = prjtrService.getUsedTRIncoming(getTRFile.get(j));
					if(getUsedTRIncoming>0) {//이미 incoming이 등록된 도서는 또 다시 등록할 수 없음
						count += 1;
					}else {//같은 TR안에 묶여있을지라도 incoming이 한번도 등록되지 않은 도서는 incoming 등록 가능해야함
						getTRFileInfo.add(getTRFile.get(j));
					}
				}
				if(count==getTRFile.size()) {//TR에 묶인 도서들 모두가 incoming 등록됐을 때
					result.add("Incoming 불가");
					return result;
				}else if(count>0){//TR에 묶인 도서들 중 하나라도 incoming 등록된 적이 있을 때
					result.add("Incoming 등록 불가한 도서 있음");
					result.add(getTRFileInfo);
				}else{//TR에 묶인 도서들 모두가 한번도 incoming 등록되지 않았을 때
					result.add(getTRFileInfo);
				}
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				getTrInfo = prjtrService.getVTrInfo(prjtrvo);
				result.add(getTrInfo);
				List<PrjDocumentIndexVO> getTRFile = prjdocumentindexService.getVTRFileInfo(prjdocumentindexvo);
				int count = 0;
				for(int j=0;j<getTRFile.size();j++) {
					int getUsedVTRIncoming = prjtrService.getUsedVTRIncoming(getTRFile.get(j));
					if(getUsedVTRIncoming>0) {//이미 incoming이 등록된 도서는 또 다시 등록할 수 없음
						count += 1;
					}else {//같은 TR안에 묶여있을지라도 incoming이 한번도 등록되지 않은 도서는 incoming 등록 가능해야함
						getTRFileInfo.add(getTRFile.get(j));
					}
				}
				if(count==getTRFile.size()) {//TR에 묶인 도서들 모두가 incoming 등록됐을 때
					result.add("Incoming 불가");
					return result;
				}else if(count>0){//TR에 묶인 도서들 중 하나라도 incoming 등록된 적이 있을 때
					result.add("Incoming 등록 불가한 도서 있음");
					result.add(getTRFileInfo);
				}else{//TR에 묶인 도서들 모두가 한번도 incoming 등록되지 않았을 때
					result.add(getTRFileInfo);
				}
			}else if(prjtrvo.getProcessType().equals("STR")) {
				getTrInfo = prjtrService.getSTrInfo(prjtrvo);
				result.add(getTrInfo);
				List<PrjDocumentIndexVO> getTRFile = prjdocumentindexService.getSTRFileInfo(prjdocumentindexvo);
				int count = 0;
				for(int j=0;j<getTRFile.size();j++) {
					int getUsedSTRIncoming = prjtrService.getUsedSTRIncoming(getTRFile.get(j));
					if(getUsedSTRIncoming>0) {//이미 incoming이 등록된 도서는 또 다시 등록할 수 없음
						count += 1;
					}else {//같은 TR안에 묶여있을지라도 incoming이 한번도 등록되지 않은 도서는 incoming 등록 가능해야함
						getTRFileInfo.add(getTRFile.get(j));
					}
				}
				if(count==getTRFile.size()) {//TR에 묶인 도서들 모두가 incoming 등록됐을 때
					result.add("Incoming 불가");
					return result;
				}else if(count>0){//TR에 묶인 도서들 중 하나라도 incoming 등록된 적이 있을 때
					result.add("Incoming 등록 불가한 도서 있음");
					result.add(getTRFileInfo);
				}else{//TR에 묶인 도서들 모두가 한번도 incoming 등록되지 않았을 때
					result.add(getTRFileInfo);
				}
			}
	
			int getEmailSentTimes = 0;
			if(getTrInfo.getTr_no()!=null) {
				prjemailvo.setUser_data00(getTrInfo.getTr_no());
				getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
			}
			result.add(getEmailSentTimes);
			
			if(getTrInfo.getTr_no()!=null && getTrInfo.getTr_no()!="") {
				//TR Incoming의 경우 페이지에서 생성된 해당 TR의 정보를 가져올 때 comment file이 있는지 체크
				//TR Outgoing의 경우 생성된 TR COVER를 이메일 테이블에 뿌려주기 위해 doc_id를 알아와야함
				prjdocumentindexvo.setDoc_no(getTrInfo.getTr_no());
				PrjDocumentIndexVO isExistDocNo = prjdocumentindexService.isExistDocNo(prjdocumentindexvo);
				result.add(isExistDocNo);
			}

		}
		return result;
	}
	/**
	 * 1. 메소드명 : TrOutIssue
	 * 2. 작성일: 2022-01-09
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 TR의 상태 Issued로 변경하며 TRANSMAITTAL NO 생성
	 * 5. 수정일: 2022-01-14,
	 * 			2021-01-21 TR COVER 생성 추가
	 * @throws ParseException 
	 */
	@RequestMapping(value = "TrOutIssue.do")
	@ResponseBody
	public Object TrOutIssue(PrjInfoVO prjinfovo,PrjTrVO prjtrvo,FolderInfoVO folderinfovo,PrjCodeSettingsVO prjcodesettingsvo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		folderinfovo.setReg_id(sessioninfo.getUser_id());
		folderinfovo.setReg_date(currentDateAndTime);
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(currentDateAndTime);

		List<PrjTrVO> getTrFiles = new ArrayList<PrjTrVO>();
		if(prjtrvo.getProcessType().equals("TR")) {
			getTrFiles = prjtrService.getTrFile(prjtrvo);
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			getTrFiles = prjtrService.getVTrFile(prjtrvo);
		}else if(prjtrvo.getProcessType().equals("STR")) {
			getTrFiles = prjtrService.getSTrFile(prjtrvo);
		}
		String dontIssueString = "";
		
		PrjCodeSettingsVO getTransmittalNoFormat = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
		if(getTransmittalNoFormat==null || getTransmittalNoFormat.getSet_val()==null) {
			dontIssueString += prjtrvo.getProcessType() + " Outgoing Numbering 설정이 안되어 있어 Issue할 수 없습니다. DCC에게 문의해주세요.";
		}
		if(!prjtrvo.getTr_no().equals("")) {//넘버링 설정이 안돼있어도 tr_no를 수기로 입력했다면 저장 가능 상태로 바꿔줌
			dontIssueString = "";
			
			if(prjtrvo.getProcessType().equals("TR")) {
				int isDuplicateTrNo = prjtrService.isDuplicateTrNo(prjtrvo);
				if(isDuplicateTrNo>0) {
					result.add("TR NO가 중복되어 저장할 수 없습니다.");
					return result;
				}
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				int isDuplicateVTrNo = prjtrService.isDuplicateVTrNo(prjtrvo);
				if(isDuplicateVTrNo>0) {
					result.add("TR NO가 중복되어 저장할 수 없습니다.");
					return result;
				}
			}else if(prjtrvo.getProcessType().equals("STR")) {
				int isDuplicateSTrNo = prjtrService.isDuplicateSTrNo(prjtrvo);
				if(isDuplicateSTrNo>0) {
					result.add("TR NO가 중복되어 저장할 수 없습니다.");
					return result;
				}
			}
		}
		
		for(int i=0;i<getTrFiles.size();i++) {
			prjdocumentindexvo.setDoc_id(getTrFiles.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(getTrFiles.get(i).getRev_id());

			if(prjtrvo.getProcessType().equals("TR")) {
				PrjDocumentIndexVO isDuplication = prjtrService.isDuplicationDocInIssue(prjdocumentindexvo);
				if((isDuplication!=null && isDuplication.getDuplicationDoc()!=null))
					dontIssueString += getTrFiles.get(i).getDoc_no()+" : "+isDuplication.getDuplicationDoc() +" 에서 이미 첨부된 문서입니다.\n";
				PrjDocumentIndexVO getDocumentInfo = prjdocumentindexService.getDocumentInfo(prjdocumentindexvo);
				if(getDocumentInfo.getLock_yn()!=null && getDocumentInfo.getLock_yn().equals("Y"))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 Check Out상태이므로 Transmittal 불가합니다.\n";
				if(!getDocumentInfo.getRev_id().equals(getDocumentInfo.getRev_max()))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 현재 버전이 아닙니다.\n";
				if(!prjdocumentindexvo.getCurrent_folder_doc_type().equals(getDocumentInfo.getDoc_type()))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 문서타입이 맞지 않습니다.\n";
				if(!prjdocumentindexvo.getSelect_discipline().equals(getDocumentInfo.getDiscip_code_id()) && !prjdocumentindexvo.getPrj_id().equals("0000000209"))
					//2022-05-03 요청사항에 따른 수정.scrubber공사의 경우 discipline 달라도 첨부가능하도록 예외 처리.
					dontIssueString += getTrFiles.get(i).getDoc_no()+":Other Discipline cannot be selected!!!\n";
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				PrjDocumentIndexVO isDuplication = prjtrService.isDuplicationDocVTRInIssue(prjdocumentindexvo);
				if((isDuplication!=null && isDuplication.getDuplicationDoc()!=null))
					dontIssueString += getTrFiles.get(i).getDoc_no()+" : "+isDuplication.getDuplicationDoc() +" 에서 이미 첨부된 문서입니다.\n";
				PrjDocumentIndexVO getDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
				if(getDocumentInfo.getLock_yn()!=null && getDocumentInfo.getLock_yn().equals("Y"))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 Check Out상태이므로 Transmittal 불가합니다.\n";
				if(!getDocumentInfo.getRev_id().equals(getDocumentInfo.getRev_max()))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 현재 버전이 아닙니다.\n";
				if(!prjdocumentindexvo.getCurrent_folder_doc_type().equals(getDocumentInfo.getDoc_type()))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 문서타입이 맞지 않습니다.\n";
				if(!prjdocumentindexvo.getSelect_discipline().equals(getDocumentInfo.getDiscip_code_id()) && !prjdocumentindexvo.getPrj_id().equals("0000000209"))
					//2022-05-03 요청사항에 따른 수정.scrubber공사의 경우 discipline 달라도 첨부가능하도록 예외 처리.
					dontIssueString += getTrFiles.get(i).getDoc_no()+":Other Discipline cannot be selected!!!\n";
			}else if(prjtrvo.getProcessType().equals("STR")) {
				PrjDocumentIndexVO isDuplication = prjtrService.isDuplicationDocSTRInIssue(prjdocumentindexvo);
				if((isDuplication!=null && isDuplication.getDuplicationDoc()!=null))
					dontIssueString += getTrFiles.get(i).getDoc_no()+" : "+isDuplication.getDuplicationDoc() +" 에서 이미 첨부된 문서입니다.\n";
				PrjDocumentIndexVO getDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
				if(getDocumentInfo.getLock_yn()!=null && getDocumentInfo.getLock_yn().equals("Y"))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 Check Out상태이므로 Transmittal 불가합니다.\n";
				if(!getDocumentInfo.getRev_id().equals(getDocumentInfo.getRev_max()))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 현재 버전이 아닙니다.\n";
				if(!prjdocumentindexvo.getCurrent_folder_doc_type().equals(getDocumentInfo.getDoc_type()))
					dontIssueString += getTrFiles.get(i).getDoc_no()+"는 문서타입이 맞지 않습니다.\n";
				if(!prjdocumentindexvo.getSelect_discipline().equals(getDocumentInfo.getDiscip_code_id()) && !prjdocumentindexvo.getPrj_id().equals("0000000209"))
					//2022-05-03 요청사항에 따른 수정.scrubber공사의 경우 discipline 달라도 첨부가능하도록 예외 처리.
					dontIssueString += getTrFiles.get(i).getDoc_no()+":Other Discipline cannot be selected!!!\n";
			}
		}
		if(!dontIssueString.equals("")) {
			result.add(dontIssueString);
		}else {
			result.add("이슈 가능");
		}
		
		

		if(dontIssueString.equals("")) {
		try {
			if(prjtrvo.getTr_no().equals("")) {//tr_no 자동입력설정시
				PrjCodeSettingsVO getTransmittalNoForm = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
				String noForm = getTransmittalNoForm.getSet_val();
				//String numberForm = noForm.replaceAll("[^0-9]","");	//설정 포맷에서 숫자만 뽑음
				String[] noFormArray = noForm.split("-");
				String REGEX = "[0-9]+";
				String numberForm = "";
				for(int i=0;i<noFormArray.length;i++) {
					if(noFormArray[i].matches(REGEX)) {//문자열에 숫자만 있는 경우
						numberForm = noFormArray[i];
					}
				}
				prjtrvo.setSerialLength(numberForm.length());
				int startNumDigit = noForm.indexOf(numberForm);
				int endNumDigit = startNumDigit+numberForm.length();
				StringBuffer sb = new StringBuffer(noForm);
				String stringForm = sb.replace(startNumDigit,endNumDigit,"%").toString();	//숫자부분 %로 대체
				String replaceForm = stringForm.replace("%", "");
				prjtrvo.setStringForm(stringForm);
				prjtrvo.setReplaceForm(replaceForm);
				String serial = "";
				if(prjtrvo.getProcessType().equals("TR")) {
					serial = prjtrService.getTRSerialNo(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					serial = prjtrService.getVTRSerialNo(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					serial = prjtrService.getSTRSerialNo(prjtrvo);
				}
				if(serial==null || serial.equals("")) {
					serial = numberForm;
				}
				stringForm = stringForm.replace("%", serial);
				
				prjtrvo.setTr_no(stringForm);
			}
			
			int updateTRNOandIssued = 0;
			PrjTrVO getTrInfo = new PrjTrVO();
			String tr_no = "";
			String tra_nm_prefix = "";
			if(prjtrvo.getProcessType().equals("TR")) {
				updateTRNOandIssued = prjtrService.updateTRNOandIssued(prjtrvo);
				getTrInfo = prjtrService.getTrInfo(prjtrvo);
				tr_no = getTrInfo.getTr_no();
				folderinfovo.setTra_folder_nm("TROOT");
				tra_nm_prefix = "TROOT&&";
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				updateTRNOandIssued = prjtrService.updateVTRNOandIssued(prjtrvo);
				getTrInfo = prjtrService.getVTrInfo(prjtrvo);
				tr_no = getTrInfo.getTr_no();
				folderinfovo.setTra_folder_nm("VROOT");
				tra_nm_prefix = "VROOT&&";
			}else if(prjtrvo.getProcessType().equals("STR")) {
				updateTRNOandIssued = prjtrService.updateSTRNOandIssued(prjtrvo);
				getTrInfo = prjtrService.getSTrInfo(prjtrvo);
				tr_no = getTrInfo.getTr_no();
				folderinfovo.setTra_folder_nm("SROOT");
				tra_nm_prefix = "SROOT&&";
			}
			
			//Sysmtem Link Transmittal에 연월로 해당 첨부문서를 집어넣을 폴더를 생성
			String folder_nm = currentDateAndTime.substring(0, 4) + "-" + currentDateAndTime.substring(4, 6);
			FolderInfoVO getTrootFolderInfo = treeService.getTrootFolderInfo(folderinfovo);
			folderinfovo.setParent_folder_id(getTrootFolderInfo.getFolder_id());
			folderinfovo.setFolder_path(getTrootFolderInfo.getFolder_path() + ">");
			folderinfovo.setFolder_nm(folder_nm);
			folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,6));
			FolderInfoVO getFolderInfoFromTRAFolderNm_YYYYMM = treeService.getFolderInfoFromTRAFolderNm(folderinfovo);
			if(getFolderInfoFromTRAFolderNm_YYYYMM!=null) {	//해당 연월 폴더가 이미 있으면 그 폴더의 정보를 가져옴
				getFolderInfoFromTRAFolderNm_YYYYMM.getFolder_path();
				folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,8));
				FolderInfoVO getFolderInfoFromTRAFolderNm_YYYYMMDD = treeService.getFolderInfoFromTRAFolderNm(folderinfovo);
				if(getFolderInfoFromTRAFolderNm_YYYYMMDD!=null){//해당 연월일 폴더가 있으면 그 폴더의 정보를 가져옴
					folderinfovo.setFolder_id(getFolderInfoFromTRAFolderNm_YYYYMMDD.getFolder_id());
					FolderInfoVO getFolderInfo_YYYYMMDD = treeService.getFolderInfo(folderinfovo);
					folderinfovo.setParent_folder_id(getFolderInfo_YYYYMMDD.getFolder_id());
					folderinfovo.setFolder_path(getFolderInfo_YYYYMMDD.getFolder_path() + ">");
					folderinfovo.setFolder_nm(tr_no);
					folderinfovo.setTra_folder_nm(tra_nm_prefix+tr_no);
					long tr_folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
					folderinfovo.setFolder_id(tr_folder_id);
					//treeService.insertTRAFolderAuth(folderinfovo);
					prjdocumentindexvo.setFolder_id(tr_folder_id);
				}else {	//해당 연월일 폴더가 없을 경우 폴더 생성
					folderinfovo.setParent_folder_id(getFolderInfoFromTRAFolderNm_YYYYMM.getFolder_id());
					folderinfovo.setFolder_path(getFolderInfoFromTRAFolderNm_YYYYMM.getFolder_path() + ">");
					folderinfovo.setFolder_nm(currentDateAndTime.substring(0, 4) + "-" + currentDateAndTime.substring(4, 6) + "-" + currentDateAndTime.substring(6, 8));
					folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,8));
					long folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
					folderinfovo.setFolder_id(folder_id);
					//treeService.insertTRAFolderAuth(folderinfovo);
					FolderInfoVO getFolderInfo_YYYYMMDD = treeService.getFolderInfo(folderinfovo);
					folderinfovo.setParent_folder_id(getFolderInfo_YYYYMMDD.getFolder_id());
					folderinfovo.setFolder_path(getFolderInfo_YYYYMMDD.getFolder_path() + ">");
					folderinfovo.setFolder_nm(tr_no);
					folderinfovo.setTra_folder_nm(tra_nm_prefix+tr_no);
					long tr_folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
					folderinfovo.setFolder_id(tr_folder_id);
					//treeService.insertTRAFolderAuth(folderinfovo);
					prjdocumentindexvo.setFolder_id(tr_folder_id);
				}
			}else {	//해당 연월 폴더가 없을 경우 폴더 생성
				long folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
				folderinfovo.setFolder_id(folder_id);
				//treeService.insertTRAFolderAuth(folderinfovo);
				FolderInfoVO getFolderInfo_YYYYMM = treeService.getFolderInfo(folderinfovo);
				folderinfovo.setParent_folder_id(getFolderInfo_YYYYMM.getFolder_id());
				folderinfovo.setFolder_path(getFolderInfo_YYYYMM.getFolder_path() + ">");
				folderinfovo.setFolder_nm(currentDateAndTime.substring(0, 4) + "-" + currentDateAndTime.substring(4, 6) + "-" + currentDateAndTime.substring(6, 8));
				folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,8));
				long folder_id_new = treeService.insertTRAFolderInfoVO(folderinfovo);
				folderinfovo.setFolder_id(folder_id_new);
				//treeService.insertTRAFolderAuth(folderinfovo);
				FolderInfoVO getFolderInfo_YYYYMMDD = treeService.getFolderInfo(folderinfovo);
				folderinfovo.setParent_folder_id(getFolderInfo_YYYYMMDD.getFolder_id());
				folderinfovo.setFolder_path(getFolderInfo_YYYYMMDD.getFolder_path() + ">");
				folderinfovo.setFolder_nm(tr_no);
				folderinfovo.setTra_folder_nm(tra_nm_prefix+tr_no);
				long tr_folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
				folderinfovo.setFolder_id(tr_folder_id);
				//treeService.insertTRAFolderAuth(folderinfovo);
				prjdocumentindexvo.setFolder_id(tr_folder_id);
			}
			
			List<PrjTrVO> getTrFile = new ArrayList<PrjTrVO>();
			PrjStepVO prjstepvo = new PrjStepVO();
			prjstepvo.setPrj_id(prjdocumentindexvo.getPrj_id());
			prjstepvo.setTr_id(prjtrvo.getTr_id());
			prjstepvo.setMod_id(sessioninfo.getUser_id());
			prjstepvo.setMod_date(currentDateAndTime);
			if(prjtrvo.getProcessType().equals("TR")) {
				getTrFile = prjtrService.getTrFile(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				getTrFile = prjtrService.getVTrFile(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				getTrFile = prjtrService.getSTrFile(prjtrvo);
			}
			for(int i=0;i<getTrFile.size();i++) {
				prjdocumentindexvo.setDoc_id(getTrFile.get(i).getDoc_id());
				prjdocumentindexvo.setRev_id(getTrFile.get(i).getRev_id());
				int insertCopyDocumentIndex = prjdocumentindexService.insertCopyDocumentIndex(prjdocumentindexvo);
				result.add(insertCopyDocumentIndex);
				

				//TR ISSUE시 Actual Date 업데이트 -> 없애기로 함(2022-04-22)
				/*prjstepvo.setDoc_id(getTrFile.get(i).getDoc_id());
				prjstepvo.setRev_id(getTrFile.get(i).getRev_id());
				prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
				if(prjtrvo.getProcessType().equals("TR")) {
					PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
					prjstepvo.setSet_val2("TR ISSUE시");
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
					prjstepvo.setSet_val2("VTR ISSUE시");
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
					prjstepvo.setSet_val2("STR ISSUE시");
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
				}*/

				//TR OUTGOING ISSUE시 Send Date를 Actual Date로 업데이트 (IFC, IAB 제외)
				prjstepvo.setDoc_id(getTrFile.get(i).getDoc_id());
				prjstepvo.setRev_id(getTrFile.get(i).getRev_id());
				if(prjtrvo.getProcessType().equals("TR")) {
					PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
					prjstepvo.setActual_date(getTrInfo2.getSend_date());
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
					if(!getCodeByTrIssuePurpose.getSet_code().equals("IFC") && !getCodeByTrIssuePurpose.getSet_code().equals("IAB")) {
						prjstepvo.setSet_val2("TR OUTGOING ISSUE시 Send Date");
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
					}
				}else if(prjtrvo.getProcessType().equals("VTR")){
					PrjTrVO getTrInfo2 = prjtrService.getVTrInfo(prjtrvo);
					prjstepvo.setActual_date(getTrInfo2.getSend_date());
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
					if(!getCodeByVTrIssuePurpose.getSet_code().equals("IFC") && !getCodeByVTrIssuePurpose.getSet_code().equals("IAB")) {
						prjstepvo.setSet_val2("VTR OUTGOING ISSUE시 Send Date");
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
					}
				}else if(prjtrvo.getProcessType().equals("STR")){
					PrjTrVO getTrInfo2 = prjtrService.getSTrInfo(prjtrvo);
					prjstepvo.setActual_date(getTrInfo2.getSend_date());
					prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
					if(!getCodeBySTrIssuePurpose.getSet_code().equals("IFC") && !getCodeBySTrIssuePurpose.getSet_code().equals("IAB")) {
						prjstepvo.setSet_val2("STR OUTGOING ISSUE시 Send Date");
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

				//IFC ISSUE시 Send Date로 Actual Date 업데이트		
				prjstepvo.setDoc_id(getTrFile.get(i).getDoc_id());
				prjstepvo.setRev_id(getTrFile.get(i).getRev_id());
				if(prjtrvo.getProcessType().equals("TR")) {
					PrjTrVO getTrInfo3 = prjtrService.getTrInfo(prjtrvo);
					prjstepvo.setSet_code_id(getTrInfo3.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
					if(getCodeByTrIssuePurpose.getSet_code().equals("IFC")) {	//IFC ISSUE PURPORSE를 선택했을 경우
						PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setActual_date(getTrInfo2.getSend_date());
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO ifcCodeYNVO = new PrjCodeSettingsVO();
						ifcCodeYNVO.setPrj_id(getTrInfo2.getPrj_id());
						ifcCodeYNVO.setSet_code_type("ENGINEERING STEP");
						ifcCodeYNVO.setSet_val2("IFC ISSUE시 Send Date");
						PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = prjcodesettingsService.isIFCStepYAndActualDateUpdateByIFCIssue(ifcCodeYNVO);
						if(isIFCStepYAndActualDateUpdateByIFCIssue!=null) {
							prjstepvo.setStep_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
							int updateEngStepActualDateByIFCIssue = prjstepService.updateEngStepActualDateByIFCIssue(prjstepvo);
							//ForecastDate 업데이트
							prjstepvo.setSet_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
							prjstepvo.setSet_val3(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_val3());
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
					}
				}else if(prjtrvo.getProcessType().equals("VTR")){	
					PrjTrVO getTrInfo3 = prjtrService.getVTrInfo(prjtrvo);
					prjstepvo.setSet_code_id(getTrInfo3.getTr_issuepur_code_id());		
					PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
					if(getCodeByVTrIssuePurpose.getSet_code().equals("IFC")) {	//IFC ISSUE PURPORSE를 선택했을 경우
						PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setActual_date(getTrInfo2.getSend_date());
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO ifcCodeYNVO = new PrjCodeSettingsVO();
						ifcCodeYNVO.setPrj_id(getTrInfo2.getPrj_id());
						ifcCodeYNVO.setSet_code_type("VENDOR DOC STEP");
						ifcCodeYNVO.setSet_val2("IFC ISSUE시 Send Date");
						PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = prjcodesettingsService.isIFCStepYAndActualDateUpdateByIFCIssue(ifcCodeYNVO);
						if(isIFCStepYAndActualDateUpdateByIFCIssue!=null) {
							prjstepvo.setStep_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
							int updateVendorStepActualDateByIFCIssue = prjstepService.updateVendorStepActualDateByIFCIssue(prjstepvo);
							//ForecastDate 업데이트
							prjstepvo.setSet_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
							prjstepvo.setSet_val3(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_val3());
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
					}
				}else if(prjtrvo.getProcessType().equals("STR")){
					PrjTrVO getTrInfo3 = prjtrService.getSTrInfo(prjtrvo);
					prjstepvo.setSet_code_id(getTrInfo3.getTr_issuepur_code_id());
					PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
					if(getCodeBySTrIssuePurpose.getSet_code().equals("IFC")) {	//IFC ISSUE PURPORSE를 선택했을 경우
						PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setActual_date(getTrInfo2.getSend_date());
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO ifcCodeYNVO = new PrjCodeSettingsVO();
						ifcCodeYNVO.setPrj_id(getTrInfo2.getPrj_id());
						ifcCodeYNVO.setSet_code_type("SITE DOC STEP");
						ifcCodeYNVO.setSet_val2("IFC ISSUE시 Send Date");
						PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = prjcodesettingsService.isIFCStepYAndActualDateUpdateByIFCIssue(ifcCodeYNVO);
						if(isIFCStepYAndActualDateUpdateByIFCIssue!=null) {
							prjstepvo.setStep_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
							int updateSiteStepActualDateByIFCIssue = prjstepService.updateSiteStepActualDateByIFCIssue(prjstepvo);
							//ForecastDate 업데이트
							prjstepvo.setSet_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
							prjstepvo.setSet_val3(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_val3());
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
			result.add(updateTRNOandIssued);
			
			
			/////TR COVER 생성
			PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
			String trfilename = "";
			if(prjtrvo.getProcessType().equals("TR")) {
				trfilename = UPLOAD_PATH+prjtrvo.getPrj_id()+TEMPLATE_PATH+getPrjInfo.getPrj_nm()+"_p_tr.xls";
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				trfilename = UPLOAD_PATH+prjtrvo.getPrj_id()+TEMPLATE_PATH+getPrjInfo.getPrj_nm()+"_p_vtr.xls";
			}else if(prjtrvo.getProcessType().equals("STR")) {
				trfilename = UPLOAD_PATH+prjtrvo.getPrj_id()+TEMPLATE_PATH+getPrjInfo.getPrj_nm()+"_p_str.xls";
			}
			InputStream inp = new FileInputStream(trfilename);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			//J7 DATE
			Row row = sheet.getRow(6);
			Cell cell = row.getCell(9);
			if(cell==null) cell = row.createCell(3);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(currentDateAndTime.substring(0, 4)+"-"+currentDateAndTime.substring(4, 6)+"-"+currentDateAndTime.substring(6, 8));
			//E11 TR NO
			Row row2 = sheet.getRow(10);
			Cell cell2 = row2.getCell(4);
			cell2.setCellType(Cell.CELL_TYPE_STRING);
			cell2.setCellValue(tr_no);
			//K11 Discipline
			Row row3 = sheet.getRow(10);
			Cell cell3 = row3.getCell(10);
			cell3.setCellType(Cell.CELL_TYPE_STRING);
			cell3.setCellValue(getTrInfo.getDiscipline());
			//C13 Subject
			Row row4 = sheet.getRow(12);
			Cell cell4 = row4.getCell(2);
			cell4.setCellType(Cell.CELL_TYPE_STRING);
			cell4.setCellValue(getTrInfo.getTr_subject());
			//A17 ISSUE CODE SET
			int issueRow = 0;
			int issueCell = 0;
			String issueText = "";
			String issueDesc = "";
			issueText = getTrInfo.getIssuepurpose();
			issueDesc = getTrInfo.getIssuepurpose_desc();
			if(prjtrvo.getProcessType().equals("TR")) {
				Row row5 = sheet.getRow(16);
				Cell cell5 = row5.getCell(2);
				cell5.setCellType(Cell.CELL_TYPE_STRING);
				cell5.setCellValue(issueText+"("+issueDesc+")");
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				Row row5 = sheet.getRow(16);
				Cell cell5 = row5.getCell(2);
				cell5.setCellType(Cell.CELL_TYPE_STRING);
				cell5.setCellValue(issueText+"("+issueDesc+")");
			}else if(prjtrvo.getProcessType().equals("STR")) {
				Row row5 = sheet.getRow(12);
				Cell cell5 = row5.getCell(2);
				cell5.setCellType(Cell.CELL_TYPE_STRING);
				cell5.setCellValue(issueText+"("+issueDesc+")");
			}
			/*if(prjtrvo.getProcessType().equals("TR")) {
				if(issueText.equals("IFA")) {
					issueRow = 16;
					issueCell = 0;
				}else if(issueText.equals("IFI")) {
					issueRow = 17;
					issueCell = 0;
				}else if(issueText.equals("IFC")) {
					issueRow = 16;
					issueCell = 4;
				}else if(issueText.equals("AB")) {
					issueRow = 17;
					issueCell = 4;
				}
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				//추후 수정 필요(general에서 생성한 코드값과 TR Cover Template에 들어있는 코드값이 맞지않음. 이 경우 TR COVER 생성시 ISSUE 코드 매칭이 안될 수 있음
				if(issueText.equals("IFA")) {
					issueRow = 16;
					issueCell = 0;
				}else if(issueText.equals("IFI")) {
					issueRow = 17;
					issueCell = 0;
				}else if(issueText.equals("IFR")) {
					issueRow = 16;
					issueCell = 4;
				}else if(issueText.equals("IFC")) {
					issueRow = 17;
					issueCell = 4;
				}
			}else if(prjtrvo.getProcessType().equals("STR")) {
				if(issueText.equals("IFI")) {
					issueRow = 12;
					issueCell = 0;
				}else if(issueText.equals("IFC")) {
					issueRow = 12;
					issueCell = 4;
				}
			}
			if(issueRow!=0) {
				Row row5 = sheet.getRow(issueRow);
				Cell cell5 = row5.getCell(issueCell);
				cell5.setCellType(Cell.CELL_TYPE_STRING);
				cell5.setCellValue("■");
			}*/
			//A22~ 첨부된 Doc 문서 정보 리스트 보여주기
			int docListStartRow = 21;
			int docListStartCell = 0;
			if(prjtrvo.getProcessType().equals("STR")) {
				docListStartRow = 15;
			}
			for(int i=0;i<getTrFile.size();i++) {
				prjdocumentindexvo.setDoc_id(getTrFile.get(i).getDoc_id());
				prjdocumentindexvo.setRev_id(getTrFile.get(i).getRev_id());
				PrjDocumentIndexVO getDocumentInfo = new PrjDocumentIndexVO();
				if(prjtrvo.getProcessType().equals("TR")) {
					getDocumentInfo = prjdocumentindexService.getDocumentInfo(prjdocumentindexvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					getDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					getDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
				}
				if(prjtrvo.getProcessType().equals("TR") || prjtrvo.getProcessType().equals("VTR")) {
					//No.
					Row docRow = sheet.getRow(docListStartRow);
					Cell cell_A = docRow.getCell(docListStartCell);
					cell_A.setCellType(Cell.CELL_TYPE_STRING);
					cell_A.setCellValue(i+1);
					//Drawing and Document No.
					Cell cell_B = docRow.getCell(docListStartCell+1);
					cell_B.setCellType(Cell.CELL_TYPE_STRING);
					cell_B.setCellValue(getDocumentInfo.getDoc_no());
					//Rev. No.
					Cell cell_C = docRow.getCell(docListStartCell+2);
					cell_C.setCellType(Cell.CELL_TYPE_STRING);
					cell_C.setCellValue(getDocumentInfo.getRev_no());
					//Category
					Cell cell_D = docRow.getCell(docListStartCell+3);
					cell_D.setCellType(Cell.CELL_TYPE_STRING);
					cell_D.setCellValue(getDocumentInfo.getCategory_code());
					//Size
					Cell cell_E = docRow.getCell(docListStartCell+4);
					cell_E.setCellType(Cell.CELL_TYPE_STRING);
					cell_E.setCellValue(getDocumentInfo.getSize_code());
					//Description/Title
					Cell cell_F = docRow.getCell(docListStartCell+5);
					cell_F.setCellType(Cell.CELL_TYPE_STRING);
					cell_F.setCellValue(getDocumentInfo.getTitle());
					//Remark
					Cell cell_M = docRow.getCell(docListStartCell+12);
					cell_M.setCellType(Cell.CELL_TYPE_STRING);
					cell_M.setCellValue(getDocumentInfo.getRemark());
				}else if(prjtrvo.getProcessType().equals("STR")) {
					//No.
					Row docRow = sheet.getRow(docListStartRow);
					Cell cell_A = docRow.getCell(docListStartCell);
					cell_A.setCellType(Cell.CELL_TYPE_STRING);
					cell_A.setCellValue(i+1);
					//Document No.
					Cell cell_B = docRow.getCell(docListStartCell+1);
					cell_B.setCellType(Cell.CELL_TYPE_STRING);
					cell_B.setCellValue(getDocumentInfo.getDoc_no());
					//Rev. No.
					Cell cell_C = docRow.getCell(docListStartCell+2);
					cell_C.setCellType(Cell.CELL_TYPE_STRING);
					cell_C.setCellValue(getDocumentInfo.getRev_no());
					//Size
					Cell cell_E = docRow.getCell(docListStartCell+3);
					cell_E.setCellType(Cell.CELL_TYPE_STRING);
					cell_E.setCellValue(getDocumentInfo.getSize_code());
					//Description/Title
					Cell cell_F = docRow.getCell(docListStartCell+4);
					cell_F.setCellType(Cell.CELL_TYPE_STRING);
					cell_F.setCellValue(getDocumentInfo.getTitle());
					//Design Charger
					Cell cell_M = docRow.getCell(docListStartCell+12);
					cell_M.setCellType(Cell.CELL_TYPE_STRING);
					cell_M.setCellValue(getDocumentInfo.getRemark());
				}

				if(docListStartRow==30) {
					docListStartRow=45;//2페이지로 넘김
					//E45(2페이지) TR_no
					Row page2_trno_row = sheet.getRow(44);
					Cell trno_cell = page2_trno_row.getCell(4);
					trno_cell.setCellType(Cell.CELL_TYPE_STRING);
					trno_cell.setCellValue(getTrInfo.getTr_no());
				}
				if(docListStartRow==75) {
					docListStartRow=79;//3페이지로 넘김
					//E79(3페이지) TR_no
					Row page3_trno_row = sheet.getRow(78);
					Cell trno_cell = page3_trno_row.getCell(4);
					trno_cell.setCellType(Cell.CELL_TYPE_STRING);
					trno_cell.setCellValue(getTrInfo.getTr_no());
				}
				if(docListStartRow==109) {
					docListStartRow=113;//4페이지로 넘김
					//E113(4페이지) TR_no
					Row page4_trno_row = sheet.getRow(112);
					Cell trno_cell = page4_trno_row.getCell(4);
					trno_cell.setCellType(Cell.CELL_TYPE_STRING);
					trno_cell.setCellValue(getTrInfo.getTr_no());
				}
				if(docListStartRow==143) {
					docListStartRow=147;//5페이지로 넘김
					//E147(5페이지) TR_no
					Row page5_trno_row = sheet.getRow(146);
					Cell trno_cell = page5_trno_row.getCell(4);
					trno_cell.setCellType(Cell.CELL_TYPE_STRING);
					trno_cell.setCellValue(getTrInfo.getTr_no());
				}
				
				if(prjtrvo.getProcessType().equals("VTR")) {
					//VTR일 경우 C42에 HPS Internal Distribution
				}else if(prjtrvo.getProcessType().equals("STR")) {
				}
				docListStartRow++;
			}

			if(prjtrvo.getProcessType().equals("TR") || prjtrvo.getProcessType().equals("VTR")) {
				//A33 TR Remarks
				Row row5 = sheet.getRow(32);
				Cell cell5 = row5.getCell(2);
				cell5.setCellType(Cell.CELL_TYPE_STRING);
				cell5.setCellValue(getTrInfo.getTr_remark());
			}else if(prjtrvo.getProcessType().equals("STR")) {
				//A27 TR Remarks
				Row row5 = sheet.getRow(26);
				Cell cell5 = row5.getCell(0);
				cell5.setCellType(Cell.CELL_TYPE_STRING);
				cell5.setCellValue(getTrInfo.getTr_remark());
			}
			
			String replaceTrNo = getTrInfo.getTr_no().replaceAll("/","-").replaceAll("%","-");
			prjdocumentindexvo.setDoc_no(getTrInfo.getTr_no());
			prjdocumentindexvo.setTitle(getTrInfo.getTr_subject());
			prjdocumentindexvo.setFile_type("xls");
			prjdocumentindexvo.setFile_size(0);
			prjdocumentindexvo.setAttach_file_date(currentDateAndTime);
			prjdocumentindexvo.setRev_code_id("00000");
			prjdocumentindexvo.setDoc_type("General");
			prjdocumentindexvo.setSfile_nm(replaceTrNo+".xls");
			prjdocumentindexvo.setRfile_nm(replaceTrNo+".xls");
			String doc_id = prjdocumentindexService.insertDocumentIndexTRCover(prjdocumentindexvo);
			// Write the output to a file

			String uploadPath = UPLOAD_PATH+ prjtrvo.getPrj_id() + DOC_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			FileOutputStream fileOut = new FileOutputStream(uploadPath + replaceTrNo +".xls");
			wb.write(fileOut);
			fileOut.close();
			/////TR COVER 생성 및 DB 저장 완료

		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("TrOutIssue: {}");
		}

		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateTRIssueCancel
	 * 2. 작성일: 2022-01-10
	 * 3. 작성자: 소진희
	 * 4. 설명: Issue 해제시 tr_no,chk_date 삭제 (update set tr_no='') 및 tr_status 값 변경
	 * 5. 수정일: 2022-01-14
	 * 2022-04-04 추가수정: Issue 해제시 Email 보냈던 내역도 삭제
	 */
	@RequestMapping(value = "updateTRIssueCancel.do")
	@ResponseBody
	public Object updateTRIssueCancel(PrjTrVO prjtrvo,FolderInfoVO folderinfovo,PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		try {
			if(prjtrvo.getProcessType().equals("TR")) {
				int updateTRIssueCancel = prjtrService.updateTRIssueCancel(prjtrvo);
				result.add(updateTRIssueCancel);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				int updateTRIssueCancel = prjtrService.updateVTRIssueCancel(prjtrvo);
				result.add(updateTRIssueCancel);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				int updateTRIssueCancel = prjtrService.updateSTRIssueCancel(prjtrvo);
				result.add(updateTRIssueCancel);
			}
			
			//해당 TR과 관련된 이메일 전송 내역 삭제
			prjtrService.deleteTREmailHistory(prjtrvo);

			String tra_nm_prefix = "";
			if(prjtrvo.getProcessType().equals("TR")) {
				tra_nm_prefix = "TROOT&&";
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				tra_nm_prefix = "VROOT&&";
			}else if(prjtrvo.getProcessType().equals("STR")) {
				tra_nm_prefix = "SROOT&&";
			}
			//issue로 인해 생성됐던 폴더와 documentIndex 지우기
			folderinfovo.setTra_folder_nm(tra_nm_prefix+prjtrvo.getTr_no());
			FolderInfoVO getFolderInfoFromTRAFolderNm = treeService.getFolderInfoFromTRAFolderNm(folderinfovo);
			folderinfovo.setFolder_id(getFolderInfoFromTRAFolderNm.getFolder_id());
			prjdocumentindexvo.setFolder_id(getFolderInfoFromTRAFolderNm.getFolder_id());
			int deleteDocumentInTRAFolder = prjdocumentindexService.deleteDocumentInTRAFolder(prjdocumentindexvo);
			result.add(deleteDocumentInTRAFolder);
			int deleteFolder = treeService.deleteFolder(folderinfovo);
			result.add(deleteFolder);
			
			//삭제된 TRA폴더의 형제폴더가 없으면 바로위의 부모 폴더도 삭제하기
			folderinfovo.setParent_folder_id(getFolderInfoFromTRAFolderNm.getParent_folder_id());
			int count = treeService.getIsExistDownFolder(folderinfovo);
			if(count==0) {	//부모폴더(ex.2022-03-22)의 하위 폴더가 없으면
				folderinfovo.setFolder_id(getFolderInfoFromTRAFolderNm.getParent_folder_id());
				FolderInfoVO getFolderInfo = treeService.getFolderInfo(folderinfovo);	//지우기전에 부모폴더id를 알아내야함
				treeService.deleteFolder(folderinfovo);

				folderinfovo.setParent_folder_id(getFolderInfo.getParent_folder_id());
				int count2 = treeService.getIsExistDownFolder(folderinfovo);
				if(count2==0) {	//부모의 부모폴더(ex.2022-03)의 하위폴더가 없으면
					folderinfovo.setFolder_id(getFolderInfo.getParent_folder_id());
					treeService.deleteFolder(folderinfovo);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("updateTRIssueCancel: {}");
		}
		return result;
	}
	/**
	 * 1. 메소드명 : deleteTR
	 * 2. 작성일: 2022-01-11
	 * 3. 작성자: 소진희
	 * 4. 설명: TR 삭제
	 * 5. 수정일: 2022-02-20
	 */
	@RequestMapping(value = "deleteTR.do")
	@ResponseBody
	public Object deleteTR(HttpServletRequest request,PrjTrVO prjtrvo,PrjDrnInfoVO prjdrninfovo,PrjDrnInfoHistVO prjdrninfohistvo) {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjdrninfohistvo.setReg_id(sessioninfo.getUser_id());
		prjdrninfohistvo.setReg_date(currentDateAndTime);
		try {
			if(prjtrvo.getProcessType().equals("TR")) {
				PrjTrVO getTrInfo = prjtrService.getTrInfo(prjtrvo);
				prjtrvo.setTr_subject(getTrInfo.getTr_subject());
				PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(prjtrvo);
				if(getIsUseDRN!=null) { //DRN 승인돼서 생성된 TR일 경우 DCC가 ISSUE CANCEL할 경우 TR을 삭제하며 DRN도 반려로 변경해줌
					prjdrninfovo.setDrn_id(getIsUseDRN.getDrn_id());
					prjdrninfoService.updateDRNStatusRByIssueCancel(prjdrninfovo);
					//DRN HISTORY 테이블에도 (TR ISSUE CANCEL로 인한) 반려로 insert
					prjdrninfohistvo.setDrn_id(getIsUseDRN.getDrn_id());
					prjdrninfohistService.updateDrnHistoryByIssueCancel(prjdrninfohistvo);
				}
				
				int deleteTrInfo = prjtrService.deleteTrInfo(prjtrvo);
				int deleteTrFileAll = prjtrService.deleteTrFileAll(prjtrvo);
				result.add(deleteTrInfo);
				result.add(deleteTrFileAll);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				PrjTrVO getVTrInfo = prjtrService.getVTrInfo(prjtrvo);
				PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getVTrInfo);
				if(getIsUseDRN!=null) { //DRN 승인돼서 생성된 TR일 경우 DCC가 ISSUE CANCEL할 경우 TR을 삭제하며 DRN도 반려로 변경해줌
					prjdrninfovo.setDrn_id(getIsUseDRN.getDrn_id());
					prjdrninfoService.updateDRNStatusRByIssueCancel(prjdrninfovo);
					prjdrninfohistvo.setDrn_id(getIsUseDRN.getDrn_id());
					prjdrninfohistService.updateDrnHistoryByIssueCancel(prjdrninfohistvo);
				}

				int deleteTrInfo = prjtrService.deleteVTrInfo(prjtrvo);
				int deleteTrFileAll = prjtrService.deleteVTrFileAll(prjtrvo);
				result.add(deleteTrInfo);
				result.add(deleteTrFileAll);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				PrjTrVO getSTrInfo = prjtrService.getSTrInfo(prjtrvo);
				PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getSTrInfo);
				if(getIsUseDRN!=null) { //DRN 승인돼서 생성된 TR일 경우 DCC가 ISSUE CANCEL할 경우 TR을 삭제하며 DRN도 반려로 변경해줌
					prjdrninfovo.setDrn_id(getIsUseDRN.getDrn_id());
					prjdrninfoService.updateDRNStatusRByIssueCancel(prjdrninfovo);
					prjdrninfohistvo.setDrn_id(getIsUseDRN.getDrn_id());
					prjdrninfohistService.updateDrnHistoryByIssueCancel(prjdrninfohistvo);
				}

				int deleteTrInfo = prjtrService.deleteSTrInfo(prjtrvo);
				int deleteTrFileAll = prjtrService.deleteSTrFileAll(prjtrvo);
				result.add(deleteTrInfo);
				result.add(deleteTrFileAll);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("deleteTR: {}");
		}
		return result;
	}
	/**
	 * 1. 메소드명 : deleteTRIn
	 * 2. 작성일: 2022-01-18
	 * 3. 작성자: 소진희
	 * 4. 설명: TR Incoming 삭제
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteTRIn.do")
	@ResponseBody
	public Object deleteTRIn(PrjTrVO prjtrvo,PrjStepVO prjstepvo,PrjDocumentIndexVO prjdocumentindexvo) {
		List<Object> result = new ArrayList<Object>();
		try {
			if(prjtrvo.getProcessType().equals("TR")) {
				int deleteTrInfo = prjtrService.deleteTrInfo(prjtrvo);
				int deleteTrFileAll = prjtrService.deleteTrFileAll(prjtrvo);
				int updateRtnStatusByTrIncomingDelete = prjstepService.updateRtnStatusByTrIncomingDelete(prjstepvo);
				int deleteDocumentIndexBydeleteTrIncoming = prjdocumentindexService.deleteDocumentIndexBydeleteTrIncoming(prjdocumentindexvo);
				result.add(deleteTrInfo);
				result.add(deleteTrFileAll);
				result.add(updateRtnStatusByTrIncomingDelete);
				result.add(deleteDocumentIndexBydeleteTrIncoming);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				int deleteTrInfo = prjtrService.deleteVTrInfo(prjtrvo);
				int deleteTrFileAll = prjtrService.deleteVTrFileAll(prjtrvo);
				int updateRtnStatusByTrIncomingDelete = prjstepService.updateRtnStatusByVTrIncomingDelete(prjstepvo);
				int deleteDocumentIndexBydeleteTrIncoming = prjdocumentindexService.deleteDocumentIndexBydeleteTrIncoming(prjdocumentindexvo);
				result.add(deleteTrInfo);
				result.add(deleteTrFileAll);
				result.add(updateRtnStatusByTrIncomingDelete);
				result.add(deleteDocumentIndexBydeleteTrIncoming);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				int deleteTrInfo = prjtrService.deleteSTrInfo(prjtrvo);
				int deleteTrFileAll = prjtrService.deleteSTrFileAll(prjtrvo);
				int updateRtnStatusByTrIncomingDelete = prjstepService.updateRtnStatusBySTrIncomingDelete(prjstepvo);
				int deleteDocumentIndexBydeleteTrIncoming = prjdocumentindexService.deleteDocumentIndexBydeleteTrIncoming(prjdocumentindexvo);
				result.add(deleteTrInfo);
				result.add(deleteTrFileAll);
				result.add(updateRtnStatusByTrIncomingDelete);
				result.add(deleteDocumentIndexBydeleteTrIncoming);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("deleteTRIn: {}");
		}
		return result;
	}
	/**
	 * 1. 메소드명 : getSearchTRNo
	 * 2. 작성일: 2022-01-17
	 * 3. 작성자: 소진희
	 * 4. 설명: TR Incoming페이지에서 Add Doc 시 doc_no로 tr_no 검색
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getSearchTRNo.do")
	@ResponseBody
	public Object getSearchTRNo(PrjTrVO prjtrvo) {
		List<Object> result = new ArrayList<Object>();
		if(prjtrvo.getProcessType().equals("TR")) {
			List<PrjTrVO> getSearchTRNo = prjtrService.getSearchTRNo(prjtrvo);
			result.add(getSearchTRNo);
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			List<PrjTrVO> getSearchTRNo = prjtrService.getSearchVTRNo(prjtrvo);
			result.add(getSearchTRNo);
		}else if(prjtrvo.getProcessType().equals("STR")) {
			List<PrjTrVO> getSearchTRNo = prjtrService.getSearchSTRNo(prjtrvo);
			result.add(getSearchTRNo);
		}
		return result;
	}
	/**
	 * 1. 메소드명 : getTrFile
	 * 2. 작성일: 2022-01-17
	 * 3. 작성자: 소진희
	 * 4. 설명: 해당 TR에 첨부된 파일들을 가져오기위함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getTrFile.do")
	@ResponseBody
	public Object getTrFile(PrjTrVO prjtrvo) {
		List<Object> result = new ArrayList<Object>();
		if(prjtrvo.getProcessType().equals("TR")) {
			List<PrjTrVO> getTrFile = prjtrService.getTrFile(prjtrvo);
			result.add(getTrFile);
		}else if(prjtrvo.getProcessType().equals("VTR")) {
			List<PrjTrVO> getTrFile = prjtrService.getVTrFile(prjtrvo);
			result.add(getTrFile);
		}else if(prjtrvo.getProcessType().equals("STR")) {
			List<PrjTrVO> getTrFile = prjtrService.getSTrFile(prjtrvo);
			result.add(getTrFile);
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : incomingSave
	 * 2. 작성일: 2022-01-17
	 * 3. 작성자: 소진희
	 * 4. 설명: TR Incoming 저장하며 TR NO 생성
	 * 5. 수정일: 2022-02-05 TR Return Status 값 입력시 Actual Date 업데이트
	 * @throws ParseException 
	 */
	@RequestMapping(value = "incomingSave.do")
	@ResponseBody
	public Object incomingSave(PrjTrVO prjtrvo,PrjStepVO prjstepvo,PrjCodeSettingsVO prjcodesettingsvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjtrvo.setReg_id(sessioninfo.getUser_id());
		prjtrvo.setReg_date(currentDateAndTime);
		prjtrvo.setMod_id(sessioninfo.getUser_id());
		prjtrvo.setMod_date(currentDateAndTime);
		prjstepvo.setMod_id(sessioninfo.getUser_id());
		prjstepvo.setMod_date(currentDateAndTime);

		String dontSaveString = "";
		PrjCodeSettingsVO getTransmittalNoFormat = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
		if(getTransmittalNoFormat==null || getTransmittalNoFormat.getSet_val()==null) {
			dontSaveString += prjtrvo.getProcessType() +" Incoming Numbering 설정이 안되어 있어 TR NO를 생성할 수 없습니다. DCC에게 문의하거나 수기입력 하십시오.";
		}
		if(!prjtrvo.getTr_no().equals("")) {//넘버링 설정이 안돼있어도 tr_no를 수기로 입력했다면 저장 가능 상태로 바꿔줌
			dontSaveString = "";

			if(prjtrvo.getNewOrUpdate().equals("new")) {
				if(prjtrvo.getProcessType().equals("TR")) {
					int isDuplicateTrNo = prjtrService.isDuplicateTrNo(prjtrvo);
					if(isDuplicateTrNo>0) {
						result.add("TR NO가 중복되어 저장할 수 없습니다.");
						return result;
					}
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					int isDuplicateVTrNo = prjtrService.isDuplicateVTrNo(prjtrvo);
					if(isDuplicateVTrNo>0) {
						result.add("TR NO가 중복되어 저장할 수 없습니다.");
						return result;
					}
				}else if(prjtrvo.getProcessType().equals("STR")) {
					int isDuplicateSTrNo = prjtrService.isDuplicateSTrNo(prjtrvo);
					if(isDuplicateSTrNo>0) {
						result.add("TR NO가 중복되어 저장할 수 없습니다.");
						return result;
					}
				}
			}else {//update일 때
				if(prjtrvo.getProcessType().equals("TR")) {
					int isDuplicateTrNo = prjtrService.isDuplicateTrNoByUpdate(prjtrvo);
					if(isDuplicateTrNo>0) {
						result.add("TR NO가 중복되어 저장할 수 없습니다.");
						return result;
					}
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					int isDuplicateVTrNo = prjtrService.isDuplicateVTrNoByUpdate(prjtrvo);
					if(isDuplicateVTrNo>0) {
						result.add("TR NO가 중복되어 저장할 수 없습니다.");
						return result;
					}
				}else if(prjtrvo.getProcessType().equals("STR")) {
					int isDuplicateSTrNo = prjtrService.isDuplicateSTrNoByUpdate(prjtrvo);
					if(isDuplicateSTrNo>0) {
						result.add("TR NO가 중복되어 저장할 수 없습니다.");
						return result;
					}
				}
			}
		}
		
		if(!dontSaveString.equals("")) {
			result.add(dontSaveString);
			return result;
		}else {
			result.add("저장 가능");
		}

		if(dontSaveString.equals("")) {
			if(prjtrvo.getNewOrUpdate().equals("new")) {	//new일 때만 발번. update시에는 발번 안함
				if(prjtrvo.getTr_no().equals("")) {//tr_no 자동입력설정시
					prjcodesettingsvo.setSet_code_type("CORRESPONDENCE_IN");
					if(prjtrvo.getProcessType().equals("TR")) {
						prjcodesettingsvo.setSet_val("DOC TRANSMITTAL");
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						prjcodesettingsvo.setSet_val("VDOC. TRANSMITTAL");
					}else if(prjtrvo.getProcessType().equals("STR")) {
						prjcodesettingsvo.setSet_val("SITE TRANSMITTAL");
					}
					PrjCodeSettingsVO getTransmittalNoForm = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
					String noForm = getTransmittalNoForm.getSet_val();
					//삭제 String numberForm = noForm.replaceAll("[^0-9]","");	//설정 포맷에서 숫자만 뽑음
					String[] noFormArray = noForm.split("-");
					String REGEX = "[0-9]+";
					String numberForm = "";
					for(int i=0;i<noFormArray.length;i++) {
						if(noFormArray[i].matches(REGEX)) {//문자열에 숫자만 있는 경우
							numberForm = noFormArray[i];
						}
					}
					prjtrvo.setSerialLength(numberForm.length());
					int startNumDigit = noForm.indexOf(numberForm);
					int endNumDigit = startNumDigit+numberForm.length();
					StringBuffer sb = new StringBuffer(noForm);
					String stringForm = sb.replace(startNumDigit,endNumDigit,"%").toString();	//숫자부분 %로 대체
					String replaceForm = stringForm.replace("%", "");
					prjtrvo.setStringForm(stringForm);
					prjtrvo.setReplaceForm(replaceForm);
					String serial = "";
					if(prjtrvo.getProcessType().equals("TR")) {
						serial = prjtrService.getTRSerialNo(prjtrvo);
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						serial = prjtrService.getVTRSerialNo(prjtrvo);
					}else if(prjtrvo.getProcessType().equals("STR")) {
						serial = prjtrService.getSTRSerialNo(prjtrvo);
					}
					if(serial==null || serial.equals("")) {
						serial = numberForm;
					}
					stringForm = stringForm.replace("%", serial);
					
					prjtrvo.setTr_no(stringForm);
				}
			}
			
			if(prjtrvo.getNewOrUpdate().equals("new")) {
				String tr_id = "";
				if(prjtrvo.getProcessType().equals("TR")) {
					if(!prjtrvo.getTr_no().equals("")) {//tr_no 수기입력시
						tr_id = prjtrService.insertTrInfoIncomingNotAuto(prjtrvo);
					}else {//tr_no 자동입력설정시
						tr_id = prjtrService.insertTrInfoIncoming(prjtrvo);
					}
					prjtrvo.setTr_id(tr_id);
					prjstepvo.setTr_id(tr_id);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					if(!prjtrvo.getTr_no().equals("")) {
						tr_id = prjtrService.insertVTrInfoIncomingNotAuto(prjtrvo);
					}else {
						tr_id = prjtrService.insertVTrInfoIncoming(prjtrvo);
					}
					prjtrvo.setTr_id(tr_id);
					prjstepvo.setTr_id(tr_id);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					if(!prjtrvo.getTr_no().equals("")) {
						tr_id = prjtrService.insertSTrInfoIncomingNotAuto(prjtrvo);
					}else {
						tr_id = prjtrService.insertSTrInfoIncoming(prjtrvo);
					}
					prjtrvo.setTr_id(tr_id);
					prjstepvo.setTr_id(tr_id);
				}
				try {
					int count = 0;
					String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
					for(int i=0;i<docList.length;i++) {
						String[] docinfo = docList[i].split("&&");
						prjtrvo.setDoc_id(docinfo[0]);
						prjtrvo.setRev_id(docinfo[1]);
						prjtrvo.setRev_no(docinfo[2]);
						prjtrvo.setRtn_status_code_id(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							int insertTrFile = prjtrService.insertTrFile(prjtrvo);
							count += insertTrFile;
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
							count += insertTrFile;
						}else if(prjtrvo.getProcessType().equals("STR")) {
							int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
							count += insertTrFile;
						}
						prjstepvo.setDoc_id(docinfo[0]);
						prjstepvo.setRev_id(docinfo[1]);
						prjstepvo.setRtn_status(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("TR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
							}
							
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
							/*List<PrjCodeSettingsVO> getUpdateCodeIdByTRReturnValue = prjstepService.getUpdateCodeIdByTRReturnValue(prjstepvo);
							for(int k=0;k<getUpdateCodeIdByTRReturnValue.size();k++) {
								prjstepvo.setSet_code_id(getUpdateCodeIdByTRReturnValue.get(k).getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								cal.add(Calendar.DATE, Integer.parseInt(getUpdateCodeIdByTRReturnValue.get(k).getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
							}*/
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByVTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("VTR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyVTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getVTrReviewStepCodeId = prjstepService.getVTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getVTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getVTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForVTrReview = prjstepService.getForeDateRuleForVTrReview(prjstepvo);
								if(getForeDateRuleForVTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForVTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForVTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByVTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoByVTRReturnSpecificValue = prjstepService.getInfoByVTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByVTRReturnSpecificValue.getSet_code()+" ("+getInfoByVTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByVTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByVTRDoc = prjstepService.getIssuePurposeByVTRDoc(prjstepvo);
							if(getIssuePurposeByVTRDoc!=null){
								prjstepvo.setSet_code_id(getIssuePurposeByVTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByVTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByVTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByVTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusBySTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("STR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnySTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getSTrReviewStepCodeId = prjstepService.getSTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getSTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getSTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForSTrReview = prjstepService.getForeDateRuleForSTrReview(prjstepvo);
								if(getForeDateRuleForSTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForSTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForSTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateBySTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoBySTRReturnSpecificValue = prjstepService.getInfoBySTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoBySTRReturnSpecificValue.getSet_code()+" ("+getInfoBySTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateBySTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeBySTRDoc = prjstepService.getIssuePurposeBySTRDoc(prjstepvo);
							if(getIssuePurposeBySTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeBySTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeBySTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeBySTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateBySTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
						
						//TR INCOMING시 Received Date를 Actual Date로 업데이트
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("TR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "TR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("VTR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "VTR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("STR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "STR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
					if(count!=docList.length) result.add(0);
					else result.add(1);
					
					result.add(tr_id);
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("incomingSave - insertTrFile: {}");
				}
			}else {//update
				int updateSuccessCount = 0;
				String tr_id = prjtrvo.getNewOrUpdate();
				prjtrvo.setTr_id(tr_id);
				
				//기존 해당 TR에 있던 파일 조회
				List<PrjTrVO> getTrFile = new ArrayList<PrjTrVO>();
				if(prjtrvo.getProcessType().equals("TR")) {
					getTrFile = prjtrService.getTrFile(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					getTrFile = prjtrService.getVTrFile(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					getTrFile = prjtrService.getSTrFile(prjtrvo);
				}
				List<String> originDocList = new ArrayList<String>();
				List<String> changeDocList = new ArrayList<String>();
				for(int i=0;i<getTrFile.size();i++) {
					originDocList.add(getTrFile.get(i).getDoc_id() + "&&" + getTrFile.get(i).getRev_id());
				}
				if(prjtrvo.getDocid_revid_revno()!="") {
				String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
					for(int i=0;i<docList.length;i++) {
						String[] docinfo = docList[i].split("&&");
						changeDocList.add(docinfo[0] + "&&" + docinfo[1]);
					}
				}
				TreeSet<String> A = new TreeSet<>(originDocList);
				TreeSet<String> B = new TreeSet<>(changeDocList);
				A.removeAll(B);
		        String[] deleteDocArray = A.toArray(new String[A.size()]);
		        int deletecount = 0;
				for(int j=0;j<deleteDocArray.length;j++) {
					String[] deleteDoc = deleteDocArray[j].split("&&");
					prjtrvo.setDoc_id(deleteDoc[0]);
					prjtrvo.setRev_id(deleteDoc[1]);
					//기존파일과 변화된파일 비교하여 삭제된 도서 delete
					if(prjtrvo.getProcessType().equals("TR")) {
						int deleteTrFile = prjtrService.deleteTrFile(prjtrvo);
						deletecount += deleteTrFile;
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						int deleteTrFile = prjtrService.deleteVTrFile(prjtrvo);
						deletecount += deleteTrFile;
					}else if(prjtrvo.getProcessType().equals("STR")) {
						int deleteTrFile = prjtrService.deleteSTrFile(prjtrvo);
						deletecount += deleteTrFile;
					}
					prjstepvo.setDoc_id(deleteDoc[0]);
					prjstepvo.setRev_id(deleteDoc[1]);
					prjstepvo.setRtn_status(null);
					if(prjtrvo.getProcessType().equals("TR")) {
						prjstepService.updateRtnStatusByTrIncoming(prjstepvo);
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						prjstepService.updateRtnStatusByVTrIncoming(prjstepvo);
					}else if(prjtrvo.getProcessType().equals("STR")) {
						prjstepService.updateRtnStatusBySTrIncoming(prjstepvo);
					}
				}
				int deleteSuccessCount = 0;
				if(deleteDocArray.length==deletecount) {
					deleteSuccessCount = 1;
				}else {
					deleteSuccessCount = 0;
				}
				int changeSuccessCount = 0;
				if(prjtrvo.getProcessType().equals("TR")) {
					int updateTrInfoIncoming = prjtrService.updateTrInfoIncoming(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					int updateTrInfoIncoming = prjtrService.updateVTrInfoIncoming(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					int updateTrInfoIncoming = prjtrService.updateSTrInfoIncoming(prjtrvo);
				}
				if(prjtrvo.getDocid_revid_revno()!="") {
					int changecount = 0;
					String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
					for(int i=0;i<docList.length;i++) {
						String[] docinfo = docList[i].split("&&");
						prjtrvo.setDoc_id(docinfo[0]);
						prjtrvo.setRev_id(docinfo[1]);
						prjtrvo.setRev_no(docinfo[2]);
						prjtrvo.setRtn_status_code_id(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							int selectTrFile = prjtrService.selectTrFile(prjtrvo);
							if(selectTrFile>0) {	//기존에 있던 도서 update
								int updateRtnStatus = prjtrService.updateRtnStatus(prjtrvo);
								changecount += updateRtnStatus;
							}else {	//기존에 없던 도서 insert
								int insertTrFile = prjtrService.insertTrFile(prjtrvo);
								changecount += insertTrFile;
							}
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							int selectTrFile = prjtrService.selectVTrFile(prjtrvo);
							if(selectTrFile>0) {	//기존에 있던 도서 update
								int updateRtnStatus = prjtrService.updateRtnStatusVTR(prjtrvo);
								changecount += updateRtnStatus;
							}else {	//기존에 없던 도서 insert
								int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
								changecount += insertTrFile;
							}
						}else if(prjtrvo.getProcessType().equals("STR")) {
							int selectTrFile = prjtrService.selectSTrFile(prjtrvo);
							if(selectTrFile>0) {	//기존에 있던 도서 update
								int updateRtnStatus = prjtrService.updateRtnStatusSTR(prjtrvo);
								changecount += updateRtnStatus;
							}else {	//기존에 없던 도서 insert
								int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
								changecount += insertTrFile;
							}
						}
						prjstepvo.setDoc_id(docinfo[0]);
						prjstepvo.setRev_id(docinfo[1]);
						prjstepvo.setTr_id(tr_id);
						prjstepvo.setRtn_status(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("TR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "TR RETURN STATUS 임의의 값 입력시"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByVTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("VTR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyVTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getVTrReviewStepCodeId = prjstepService.getVTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getVTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getVTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForVTrReview = prjstepService.getForeDateRuleForVTrReview(prjstepvo);
								if(getForeDateRuleForVTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForVTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForVTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByVTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoByVTRReturnSpecificValue = prjstepService.getInfoByVTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByVTRReturnSpecificValue.getSet_code()+" ("+getInfoByVTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByVTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByVTRDoc = prjstepService.getIssuePurposeByVTRDoc(prjstepvo);
							if(getIssuePurposeByVTRDoc!=null){
								prjstepvo.setSet_code_id(getIssuePurposeByVTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByVTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByVTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByVTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusBySTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("STR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnySTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getSTrReviewStepCodeId = prjstepService.getSTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getSTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getSTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForSTrReview = prjstepService.getForeDateRuleForSTrReview(prjstepvo);
								if(getForeDateRuleForSTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForSTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForSTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateBySTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoBySTRReturnSpecificValue = prjstepService.getInfoBySTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoBySTRReturnSpecificValue.getSet_code()+" ("+getInfoBySTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateBySTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeBySTRDoc = prjstepService.getIssuePurposeBySTRDoc(prjstepvo);
							if(getIssuePurposeBySTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeBySTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeBySTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeBySTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateBySTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
						
						
						//TR INCOMING시 Received Date를 Actual Date로 업데이트
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("TR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "TR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("VTR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "VTR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("STR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "STR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
					if(docList.length==changecount) {
						changeSuccessCount = 1;
					}else {
						changeSuccessCount = 0;
					}
				}
				if(deleteSuccessCount==1 && changeSuccessCount==1) {
					result.add(1);
				}else {
					result.add(0);
				}
				
				result.add(tr_id);
			}
		}
		return result;
	}
	
	/**
	 * 1. 메소드명 : incomingSaveAttachChkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "incomingSaveAttachChkFile.do")
	@ResponseBody
	public int incomingSaveAttachChkFile(MultipartHttpServletRequest mtRequest,PrjDocumentIndexVO prjDocumentIndexVO) throws Exception{
		int chkFile = 100;
		if(!prjDocumentIndexVO.getFile_value().equals("")) {	//file Attach 할 경우
			MultipartFile mf = mtRequest.getFile("trIncoming_File");
			File file = new File(mf.getOriginalFilename());
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}
		}
			
		return chkFile;
	}
	
	/**
	 * 1. 메소드명 : trCoverSaveChkFile
	 * 2. 작성일: 2021-03-07
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "trCoverSaveChkFile.do")
	@ResponseBody
	public int trCoverSaveChkFile(MultipartHttpServletRequest mtRequest,PrjInfoVO prjinfovo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) throws Exception{
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
	
	/**
	 * 1. 메소드명 : incomingSaveAttach
	 * 2. 작성일: 2022-01-18
	 * 3. 작성자: 소진희
	 * 4. 설명: TR Incoming 저장하며 TR NO 생성 (파일 첨부 포함)
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "incomingSaveAttach.do")
	@ResponseBody
	public Object incomingSaveAttach(MultipartHttpServletRequest mtRequest,PrjCodeSettingsVO prjcodesettingsvo,PrjStepVO prjstepvo,PrjTrVO prjtrvo,ProcessInfoVO processinfovo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		String currentDateAndTime = CommonConst.currentDateAndTime();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjtrvo.setReg_id(sessioninfo.getUser_id());
		prjtrvo.setReg_date(currentDateAndTime);
		prjtrvo.setMod_id(sessioninfo.getUser_id());
		prjtrvo.setMod_date(currentDateAndTime);
		prjdocumentindexvo.setReg_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setReg_date(currentDateAndTime);
		prjdocumentindexvo.setMod_id(sessioninfo.getUser_id());
		prjdocumentindexvo.setMod_date(currentDateAndTime);

		try {
			boolean chkFile = prjtrService.incomingChkFile(mtRequest, prjdocumentindexvo);
			if(!chkFile) throw new Exception("Unsupported file format. Please contact the DCC");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			result.add("확장자 오류");
			result.add("Unsupported file format. Please contact the DCC");
			return result;
		}
		
		String dontSaveString = "";
		PrjCodeSettingsVO getTransmittalNoFormat = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
		if(getTransmittalNoFormat==null || getTransmittalNoFormat.getSet_val()==null) {
			dontSaveString += prjtrvo.getProcessType() +" Incoming Numbering 설정이 안되어 있어 TR NO를 생성할 수 없습니다. DCC에게 문의하거나 수기입력 하십시오.";
		}
		if(!prjtrvo.getTr_no().equals("")) {//넘버링 설정이 안돼있어도 tr_no를 수기로 입력했다면 저장 가능 상태로 바꿔줌
			dontSaveString = "";
		}
		
		if(!dontSaveString.equals("")) {
			result.add(dontSaveString);
		}else {
			result.add("저장 가능");
		}

		if(dontSaveString.equals("")) {
				if(prjtrvo.getNewOrUpdate().equals("new")) {	//new일 때만 발번
				prjcodesettingsvo.setSet_code_type("CORRESPONDENCE_IN");
				if(prjtrvo.getProcessType().equals("TR")) {
					prjcodesettingsvo.setSet_val("DOC TRANSMITTAL");
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					prjcodesettingsvo.setSet_val("VDOC. TRANSMITTAL");
				}else if(prjtrvo.getProcessType().equals("STR")) {
					prjcodesettingsvo.setSet_val("SITE TRANSMITTAL");
				}
				PrjCodeSettingsVO getTransmittalNoForm = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
				String noForm = getTransmittalNoForm.getSet_val();
				//String numberForm = noForm.replaceAll("[^0-9]","");	//설정 포맷에서 숫자만 뽑음
				String[] noFormArray = noForm.split("-");
				String REGEX = "[0-9]+";
				String numberForm = "";
				for(int i=0;i<noFormArray.length;i++) {
					if(noFormArray[i].matches(REGEX)) {//문자열에 숫자만 있는 경우
						numberForm = noFormArray[i];
					}
				}
				prjtrvo.setSerialLength(numberForm.length());
				int startNumDigit = noForm.indexOf(numberForm);
				int endNumDigit = startNumDigit+numberForm.length();
				StringBuffer sb = new StringBuffer(noForm);
				String stringForm = sb.replace(startNumDigit,endNumDigit,"%").toString();	//숫자부분 %로 대체
				String replaceForm = stringForm.replace("%", "");
				prjtrvo.setStringForm(stringForm);
				prjtrvo.setReplaceForm(replaceForm);
				String serial = "";
				if(prjtrvo.getProcessType().equals("TR")) {
					serial = prjtrService.getTRSerialNo(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					serial = prjtrService.getVTRSerialNo(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					serial = prjtrService.getSTRSerialNo(prjtrvo);
				}
				if(serial==null || serial.equals("")) {
					serial = numberForm;
				}
				stringForm = stringForm.replace("%", serial);
				
				prjtrvo.setTr_no(stringForm);
			}
				
			if(prjtrvo.getNewOrUpdate().equals("new")) {
				String tr_id = "";
				if(prjtrvo.getProcessType().equals("TR")) {
					tr_id = prjtrService.insertTrInfoIncoming(prjtrvo);
					prjtrvo.setTr_id(tr_id);
					prjstepvo.setTr_id(tr_id);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					tr_id = prjtrService.insertVTrInfoIncoming(prjtrvo);
					prjtrvo.setTr_id(tr_id);
					prjstepvo.setTr_id(tr_id);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					tr_id = prjtrService.insertSTrInfoIncoming(prjtrvo);
					prjtrvo.setTr_id(tr_id);
					prjstepvo.setTr_id(tr_id);
				}
				try {
					int count = 0;
					String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
					for(int i=0;i<docList.length;i++) {
						String[] docinfo = docList[i].split("&&");
						prjtrvo.setDoc_id(docinfo[0]);
						prjtrvo.setRev_id(docinfo[1]);
						prjtrvo.setRev_no(docinfo[2]);
						prjtrvo.setRtn_status_code_id(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							int insertTrFile = prjtrService.insertTrFile(prjtrvo);
							count += insertTrFile;
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
							count += insertTrFile;
						}else if(prjtrvo.getProcessType().equals("STR")) {
							int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
							count += insertTrFile;
						}
						prjstepvo.setDoc_id(docinfo[0]);
						prjstepvo.setRev_id(docinfo[1]);
						prjstepvo.setRtn_status(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("TR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByVTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("VTR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyVTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getVTrReviewStepCodeId = prjstepService.getVTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getVTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getVTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForVTrReview = prjstepService.getForeDateRuleForVTrReview(prjstepvo);
								if(getForeDateRuleForVTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForVTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForVTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByVTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoByVTRReturnSpecificValue = prjstepService.getInfoByVTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByVTRReturnSpecificValue.getSet_code()+" ("+getInfoByVTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByVTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByVTRDoc = prjstepService.getIssuePurposeByVTRDoc(prjstepvo);
							if(getIssuePurposeByVTRDoc!=null){
								prjstepvo.setSet_code_id(getIssuePurposeByVTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByVTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByVTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByVTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusBySTrIncoming(prjstepvo);
							prjstepvo.setSet_val2("STR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnySTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getSTrReviewStepCodeId = prjstepService.getSTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getSTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getSTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForSTrReview = prjstepService.getForeDateRuleForSTrReview(prjstepvo);
								if(getForeDateRuleForSTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForSTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForSTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateBySTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoBySTRReturnSpecificValue = prjstepService.getInfoBySTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoBySTRReturnSpecificValue.getSet_code()+" ("+getInfoBySTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateBySTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeBySTRDoc = prjstepService.getIssuePurposeBySTRDoc(prjstepvo);
							if(getIssuePurposeBySTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeBySTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeBySTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeBySTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateBySTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
						

						
						//TR INCOMING시 Received Date를 Actual Date로 업데이트
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("TR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "TR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("VTR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "VTR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("STR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "STR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
					if(count!=docList.length) result.add(0);
					else result.add(1);
					
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("incomingSaveAttach - insertTrFile: {}");
				}
				result.add(tr_id);
			}else {//update
				int updateSuccessCount = 0;
				String tr_id = prjtrvo.getNewOrUpdate();
				prjtrvo.setTr_id(tr_id);
				
				//기존 해당 TR에 있던 파일 조회
				List<PrjTrVO> getTrFile = new ArrayList<PrjTrVO>();
				if(prjtrvo.getProcessType().equals("TR")) {
					getTrFile = prjtrService.getTrFile(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					getTrFile = prjtrService.getVTrFile(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					getTrFile = prjtrService.getSTrFile(prjtrvo);
				}
				List<String> originDocList = new ArrayList<String>();
				List<String> changeDocList = new ArrayList<String>();
				for(int i=0;i<getTrFile.size();i++) {
					originDocList.add(getTrFile.get(i).getDoc_id() + "&&" + getTrFile.get(i).getRev_id());
				}
				if(prjtrvo.getDocid_revid_revno()!="") {
				String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
					for(int i=0;i<docList.length;i++) {
						String[] docinfo = docList[i].split("&&");
						changeDocList.add(docinfo[0] + "&&" + docinfo[1]);
					}
				}
				TreeSet<String> A = new TreeSet<>(originDocList);
				TreeSet<String> B = new TreeSet<>(changeDocList);
				A.removeAll(B);
		        String[] deleteDocArray = A.toArray(new String[A.size()]);
		        int deletecount = 0;
				for(int j=0;j<deleteDocArray.length;j++) {
					String[] deleteDoc = deleteDocArray[j].split("&&");
					prjtrvo.setDoc_id(deleteDoc[0]);
					prjtrvo.setRev_id(deleteDoc[1]);
					//기존파일과 변화된파일 비교하여 삭제된 도서 delete
					if(prjtrvo.getProcessType().equals("TR")) {
						int deleteTrFile = prjtrService.deleteTrFile(prjtrvo);
						deletecount += deleteTrFile;
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						int deleteTrFile = prjtrService.deleteVTrFile(prjtrvo);
						deletecount += deleteTrFile;
					}else if(prjtrvo.getProcessType().equals("STR")) {
						int deleteTrFile = prjtrService.deleteSTrFile(prjtrvo);
						deletecount += deleteTrFile;
					}
					prjstepvo.setDoc_id(deleteDoc[0]);
					prjstepvo.setRev_id(deleteDoc[1]);
					prjstepvo.setRtn_status(null);
					if(prjtrvo.getProcessType().equals("TR")) {
						prjstepService.updateRtnStatusByTrIncoming(prjstepvo);
					}else if(prjtrvo.getProcessType().equals("VTR")) {
						prjstepService.updateRtnStatusByVTrIncoming(prjstepvo);
					}else if(prjtrvo.getProcessType().equals("STR")) {
						prjstepService.updateRtnStatusBySTrIncoming(prjstepvo);
					}
				}
				int deleteSuccessCount = 0;
				if(deleteDocArray.length==deletecount) {
					deleteSuccessCount = 1;
				}else {
					deleteSuccessCount = 0;
				}
				int changeSuccessCount = 0;
				if(prjtrvo.getProcessType().equals("TR")) {
					int updateTrInfoIncoming = prjtrService.updateTrInfoIncoming(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					int updateTrInfoIncoming = prjtrService.updateVTrInfoIncoming(prjtrvo);
				}else if(prjtrvo.getProcessType().equals("STR")) {
					int updateTrInfoIncoming = prjtrService.updateSTrInfoIncoming(prjtrvo);
				}
				if(prjtrvo.getDocid_revid_revno()!="") {
					int changecount = 0;
					String[] docList = prjtrvo.getDocid_revid_revno().split("@@");
					for(int i=0;i<docList.length;i++) {
						String[] docinfo = docList[i].split("&&");
						prjtrvo.setDoc_id(docinfo[0]);
						prjtrvo.setRev_id(docinfo[1]);
						prjtrvo.setRev_no(docinfo[2]);
						prjtrvo.setRtn_status_code_id(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							int selectTrFile = prjtrService.selectTrFile(prjtrvo);
							if(selectTrFile>0) {	//기존에 있던 도서 update
								int updateRtnStatus = prjtrService.updateRtnStatus(prjtrvo);
								changecount += updateRtnStatus;
							}else {	//기존에 없던 도서 insert
								int insertTrFile = prjtrService.insertTrFile(prjtrvo);
								changecount += insertTrFile;
							}
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							int selectTrFile = prjtrService.selectVTrFile(prjtrvo);
							if(selectTrFile>0) {	//기존에 있던 도서 update
								int updateRtnStatus = prjtrService.updateRtnStatusVTR(prjtrvo);
								changecount += updateRtnStatus;
							}else {	//기존에 없던 도서 insert
								int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
								changecount += insertTrFile;
							}
						}else if(prjtrvo.getProcessType().equals("STR")) {
							int selectTrFile = prjtrService.selectSTrFile(prjtrvo);
							if(selectTrFile>0) {	//기존에 있던 도서 update
								int updateRtnStatus = prjtrService.updateRtnStatusSTR(prjtrvo);
								changecount += updateRtnStatus;
							}else {	//기존에 없던 도서 insert
								int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
								changecount += insertTrFile;
							}
						}
						prjstepvo.setDoc_id(docinfo[0]);
						prjstepvo.setRev_id(docinfo[1]);
						prjstepvo.setTr_id(tr_id);
						prjstepvo.setRtn_status(docinfo[3]);
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("TR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusByVTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("VTR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnyVTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getVTrReviewStepCodeId = prjstepService.getVTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getVTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getVTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForVTrReview = prjstepService.getForeDateRuleForVTrReview(prjstepvo);
								if(getForeDateRuleForVTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForVTrReview.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForVTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByVTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoByVTRReturnSpecificValue = prjstepService.getInfoByVTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByVTRReturnSpecificValue.getSet_code()+" ("+getInfoByVTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByVTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByVTRDoc = prjstepService.getIssuePurposeByVTRDoc(prjstepvo);
							if(getIssuePurposeByVTRDoc!=null){
								prjstepvo.setSet_code_id(getIssuePurposeByVTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByVTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByVTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByVTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")) {
							prjstepvo.setSet_val2("%값 입력시%");
							prjstepService.updateRtnStatusBySTrIncoming(prjstepvo);
							prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
							prjstepvo.setSet_val2("STR RETURN STATUS 임의의 값 입력시");
							prjstepService.updateActualDateByAnySTRReturn(prjstepvo);
	
							List<PrjCodeSettingsVO> getSTrReviewStepCodeId = prjstepService.getSTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getSTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getSTrReviewStepCodeId.get(j).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleForSTrReview = prjstepService.getForeDateRuleForSTrReview(prjstepvo);
								if(getForeDateRuleForSTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForSTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForSTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateBySTrIncoming(prjstepvo);
									}
								}
							}
							PrjCodeSettingsVO getInfoBySTRReturnSpecificValue = prjstepService.getInfoBySTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoBySTRReturnSpecificValue.getSet_code()+" ("+getInfoBySTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateBySTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeBySTRDoc = prjstepService.getIssuePurposeBySTRDoc(prjstepvo);
							if(getIssuePurposeBySTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeBySTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeBySTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeBySTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateBySTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
						

						
						//TR INCOMING시 Received Date를 Actual Date로 업데이트
						if(prjtrvo.getProcessType().equals("TR")) {
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("TR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "TR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("VTR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("VTR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "VTR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}else if(prjtrvo.getProcessType().equals("STR")){
							prjstepvo.setActual_date(prjtrvo.getSend_date());//incoming에선 send_date가 received date임
							prjstepvo.setSet_val2("STR INCOMING시 Received Date");
							prjstepService.updateActualDateByAnyTRReturn(prjstepvo);
							
							// "STR INCOMING시 Received Date"로 설정된 스텝 코드 가져옴
							List<PrjCodeSettingsVO> getTrReviewStepCodeId = prjstepService.getTrReviewStepCodeId(prjstepvo);
							for(int j=0;j<getTrReviewStepCodeId.size();j++) {
								prjstepvo.setSet_code_id(getTrReviewStepCodeId.get(j).getSet_code_id());
								// fore date 적용 규칙을 가져옴
								PrjCodeSettingsVO getForeDateRuleForTrReview = prjstepService.getForeDateRuleForTrReview(prjstepvo);
								if(getForeDateRuleForTrReview!=null) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									Date dt = dtFormat.parse(prjtrvo.getSend_date());
									cal.setTime(dt);
									if(!getForeDateRuleForTrReview.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleForTrReview.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateReviewForeDateByTrIncoming(prjstepvo);
									}
								}
								
							}
							PrjCodeSettingsVO getInfoByTRReturnSpecificValue = prjstepService.getInfoByTRReturnSpecificValue(prjstepvo);
							prjstepvo.setSet_val2(getInfoByTRReturnSpecificValue.getSet_code()+" ("+getInfoByTRReturnSpecificValue.getSet_desc()+") 값 입력시");
							prjstepService.updateActualDateByTRReturnSpecificValue(prjstepvo);
							//ForecastDate Update
							PrjCodeSettingsVO getIssuePurposeByTRDoc = prjstepService.getIssuePurposeByTRDoc(prjstepvo);
							if(getIssuePurposeByTRDoc!=null) {
								prjstepvo.setSet_code_id(getIssuePurposeByTRDoc.getSet_code_id());
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								Date dt = dtFormat.parse(prjtrvo.getSend_date());
								cal.setTime(dt);
								if(!getIssuePurposeByTRDoc.getSet_val4().equals("")) {
									cal.add(Calendar.DATE, Integer.parseInt(getIssuePurposeByTRDoc.getSet_val4()));
									prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
									prjstepService.updateEngStepForeDateByTRStepActualDateUpdate(prjstepvo);
								}
							}
						}
					}
					if(docList.length==changecount) {
						changeSuccessCount = 1;
					}else {
						changeSuccessCount = 0;
					}
				}
				if(deleteSuccessCount==1 && changeSuccessCount==1) {
					result.add(1);
				}else {
					result.add(0);
				}
				result.add(tr_id);
			}
			PrjTrVO getTrInfo = new PrjTrVO();
			if(prjtrvo.getProcessType().equals("TR")) {
				getTrInfo = prjtrService.getTrInfo(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				getTrInfo = prjtrService.getVTrInfo(prjtrvo);
			}else if(prjtrvo.getProcessType().equals("STR")) {
				getTrInfo = prjtrService.getSTrInfo(prjtrvo);
			}
			ProcessInfoVO getINOUTMatchingFolderId = processinfoService.getINOUTMatchingFolderId(processinfovo);
			prjdocumentindexvo.setFolder_id(Long.parseLong(getINOUTMatchingFolderId.getProcess_folder_path_id()));
			prjdocumentindexvo.setDoc_no(getTrInfo.getTr_no());
			prjdocumentindexvo.setDoc_type("General");
			prjdocumentindexvo.setTitle(getTrInfo.getTr_no());
			PrjDocumentIndexVO isExistDocNo = prjdocumentindexService.isExistDocNo(prjdocumentindexvo);
			if(isExistDocNo!=null) {	//해당 TR에 Comment File을 이미 업로드 했을 경우
				String doc_id = isExistDocNo.getDoc_id();
				prjdocumentindexvo.setDoc_id(doc_id);
				prjdocumentindexvo.setRev_id("001");
				prjdocumentindexService.updateDocumentIndexForCorr(prjdocumentindexvo);
				Path filePath = Paths.get(UPLOAD_PATH+doc_id+"_001."+prjdocumentindexvo.getFile_type());
				try {
					//기존 파일이 존재하면 삭제
					Files.deleteIfExists(filePath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MultipartFile file = mtRequest.getFile("trIncoming_File");
				String uploadPath = UPLOAD_PATH + prjtrvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				File uploadTarget = new File(uploadPath + doc_id+"_001."+prjdocumentindexvo.getFile_type());
				try {
					// 업로드 경로에 파일 업로드
					file.transferTo(uploadTarget);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {	//해당 TR에 Comment File을 처음 업로드 하는 경우
				String doc_id = prjdocumentindexService.insertDocumentIndexForCorr(prjdocumentindexvo);
				//파일 업로드
				MultipartFile file = mtRequest.getFile("trIncoming_File");
				String uploadPath = UPLOAD_PATH + prjtrvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				File uploadTarget = new File(uploadPath + doc_id+"_001."+prjdocumentindexvo.getFile_type());
				try {
					// 업로드 경로에 파일 업로드
					file.transferTo(uploadTarget);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 1. 메소드명 : CoverTemplateExists
	 * 2. 작성일: 2022-02-08
	 * 3. 작성자: 소진희
	 * 4. 설명: Transmittal Cover Template이 존재하는지 확인하여 존재여부를 반환함
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "CoverTemplateExists.do")
	@ResponseBody
	public Object CoverTemplateExists(PrjInfoVO prjinfovo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		File f = new File(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_tr.xls");
		if(f.exists()) {
		     result.add("파일 존재");
		} else {
		     result.add("파일 없음");           
		}
		File vf = new File(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_vtr.xls");
		if(vf.exists()) {
		     result.add("파일 존재");
		} else {
		     result.add("파일 없음");           
		}
		File sf = new File(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_str.xls");
		if(sf.exists()) {
		     result.add("파일 존재");
		} else {
		     result.add("파일 없음");           
		}
		return result;
	}

	
	/**
	 * 1. 메소드명 : TRCoverUpload
	 * 2. 작성일: 2022-02-08
	 * 3. 작성자: 소진희
	 * 4. 설명: Transmittal Cover Upload
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "TRCoverUpload.do")
	@ResponseBody
	public Object TRCoverUpload(MultipartHttpServletRequest mtRequest,PrjInfoVO prjinfovo,PrjDocumentIndexVO prjdocumentindexvo,HttpServletRequest request) throws ParseException {
		List<Object> result = new ArrayList<Object>();
		
		try {
			int ext_chk = prjtrService.trCoverSaveChkFile(mtRequest, prjdocumentindexvo);
			if(ext_chk < 1) throw new Exception("Unsupported file format. Please contact the administrator");
		} catch (Exception e2) {
			result.add("FAIL");
			result.add(e2.getMessage());
			
			return result;
		}
		
		if(prjdocumentindexvo.getDoc_type().equals("TR")) {
			Path filePath = Paths.get(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_tr."+prjdocumentindexvo.getFile_type());
			try {
				//기존 파일이 존재하면 삭제
				Files.deleteIfExists(filePath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MultipartFile file = mtRequest.getFile("trCover_File");
			String uploadPath = UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjinfovo.getPrj_nm()+"_p_tr."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(prjdocumentindexvo.getDoc_type().equals("VTR")) {
			Path filePath = Paths.get(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_vtr."+prjdocumentindexvo.getFile_type());
			try {
				//기존 파일이 존재하면 삭제
				Files.deleteIfExists(filePath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MultipartFile file = mtRequest.getFile("vtrCover_File");
			String uploadPath = UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjinfovo.getPrj_nm()+"_p_vtr."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(prjdocumentindexvo.getDoc_type().equals("STR")) {
			Path filePath = Paths.get(UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH+prjinfovo.getPrj_nm()+"_p_str."+prjdocumentindexvo.getFile_type());
			try {
				//기존 파일이 존재하면 삭제
				Files.deleteIfExists(filePath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MultipartFile file = mtRequest.getFile("strCover_File");
			String uploadPath = UPLOAD_PATH+prjinfovo.getPrj_id()+TEMPLATE_PATH;
			File dir = new File(uploadPath);
			dir.mkdirs();
			File uploadTarget = new File(uploadPath + prjinfovo.getPrj_nm()+"_p_str."+prjdocumentindexvo.getFile_type());
			try {
				// 업로드 경로에 파일 업로드
				file.transferTo(uploadTarget);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 1. 메소드명 : downloadTRCoverTemplateFile
	 * 2. 작성일: 2022-02-08
	 * 3. 작성자: 소진희
	 * 4. 설명: TR COVER Template 파일 다운받기
	 * 5. 수정일: 
	 */
    @RequestMapping("/downloadTRCoverTemplateFile.do")
    public void downloadCommentFile(@RequestParam String rfile_nm,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String downloadFilesString = rfile_nm;
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			files[i] = downloadFileEach[i];
		}
    	String tempPath = "";
    	String prj_id = request.getParameter("prj_id");
    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prj_id +TEMPLATE_PATH;//"c:/vdsFileupload/";		//파일 저장경로

                 for ( int k=0; k<files.length; k++){
                 //파일다운로드 START
	                 response.setContentType("application/zip");
	                 rfile_nm = CommonConst.downloadNameEncoding(request, rfile_nm);
	                 response.addHeader("Content-Disposition", "attachment;filename=" + rfile_nm);
	                 
	                 FileInputStream fis = new FileInputStream(tempPath + files[k]);
	                 BufferedInputStream bis = new BufferedInputStream(fis);
	                 ServletOutputStream so = response.getOutputStream();
	                 BufferedOutputStream bos = new BufferedOutputStream(so);
	                 byte[] buffer = new byte[1024];
	                 
	                 int n = 0;
	                 while((n = bis.read(buffer)) > 0){
	                    bos.write(buffer, 0, n);
	                    bos.flush();
	                 }
	                 
	                 if(bos != null) bos.close();
	                 if(bis != null) bis.close();
	                 if(so != null) so.close();
	                 if(fis != null) fis.close();
	                 //파일다운로드 END
                 }
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }
    	}
    }
    
	/**
	 * 1. 메소드명 : getUseDocTROrDRN
	 * 2. 작성일: 2022-02-11
	 * 3. 작성자: 소진희
	 * 4. 설명: 도서 삭제 전에 TR이나 DRN에서 사용중인 도서인지 판단함
	 * 5. 수정일: 2022-03-17 또한 고객에게 한번이라도 보내진 도서일 경우 삭제불가 처리
	 * @throws ParseException 
	 */
	@RequestMapping(value = "getUseDocTROrDRN.do")
	@ResponseBody
	public Object getUseDocTROrDRN(HttpServletRequest request,PrjTrVO prjtrvo,PrjEmailVO prjemailvo,PrjDrnInfoVO prjdrninfovo,PrjMemberVO prjmembervo,PrjDocumentIndexVO prjdocumentindexvo){
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjmembervo.setDiff_dcc_user(sessioninfo.getUser_id());
		PrjMemberVO getPrjDCCYN = prjinfoService.getPrjDCCYN(prjmembervo);
		prjemailvo.setUser_data00(prjdocumentindexvo.getDoc_id()+"&&"+prjdocumentindexvo.getRev_id());
		int getEmailSentTimes = prjemailService.getEmailSentTimesForDC(prjemailvo);
		if((sessioninfo.getAdmin_yn()!=null && sessioninfo.getAdmin_yn().equals("Y")) || (getPrjDCCYN!=null && getPrjDCCYN.getDcc_yn().equals("Y"))) {
		//2022-04-28 요청에 따른 수정. ADMIN이나 DCC는 일반 메일 발송되었더라도 삭제 가능
			result.add("삭제가능");
		}else if(getEmailSentTimes>0) {
			result.add("삭제불가");
		}else {
			if(prjdocumentindexvo.getDoc_type().equals("DCI")) {
				List<PrjTrVO> getUseDocInTR = prjtrService.getUseDocInTR(prjtrvo);
				PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
				if(getUseDocInTR.size()!=0) {
					String useDoc = "";
					for(int i=0;i<getUseDocInTR.size();i++) {
						useDoc += getUseDocInTR.get(i).getTr_no() + ",";
					}
					useDoc = useDoc.substring(0, useDoc.length()-1);
					result.add(useDoc);
				}else if(getUseDocInDRN!=null) { 
					result.add(getUseDocInDRN.getDrn_no());
				} else {
				     result.add("삭제가능");           
				}
			}else if(prjdocumentindexvo.getDoc_type().equals("VDCI")) {
				List<PrjTrVO> getUseDocInVTR = prjtrService.getUseDocInVTR(prjtrvo);
				PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
				if(getUseDocInVTR.size()!=0) {
					String useDoc = "";
					for(int i=0;i<getUseDocInVTR.size();i++) {
						useDoc += getUseDocInVTR.get(i).getTr_no() + ",";
					}
					useDoc = useDoc.substring(0, useDoc.length()-1);
					result.add(useDoc);
				}else if(getUseDocInDRN!=null) { 
					result.add(getUseDocInDRN.getDrn_no());
				} else {
				     result.add("삭제가능");           
				}
			}else if(prjdocumentindexvo.getDoc_type().equals("SDCI")) {
				List<PrjTrVO> getUseDocInSTR = prjtrService.getUseDocInSTR(prjtrvo);
				PrjDrnInfoVO getUseDocInDRN = prjtrService.getUseDocInDRN(prjdrninfovo);
				if(getUseDocInSTR.size()!=0) {
					String useDoc = "";
					for(int i=0;i<getUseDocInSTR.size();i++) {
						useDoc += getUseDocInSTR.get(i).getTr_no() + ",";
					}
					useDoc = useDoc.substring(0, useDoc.length()-1);
					result.add(useDoc);
				}else if(getUseDocInDRN!=null) { 
					result.add(getUseDocInDRN.getDrn_no());
				} else {
				     result.add("삭제가능");           
				}
			}else {
			     result.add("삭제가능");
			}
		}
		return result;
	}

	/**
	 * 1. 메소드명 : getUseDocDRN
	 * 2. 작성일: 2022-02-11
	 * 3. 작성자: 소진희
	 * 4. 설명: TR Issue Cancel 전에 DRN에서 사용중인 도서인지 판단함
	 * 5. 수정일: 2022-02-20
	 * 			DRN에서 최종승인을 받아 생긴 TR일 경우 
	 * 			최종승인(C) 받았던 DRN은 반려(R) 상태로 변경되고 TR Issue는 취소됨 (DCC만 가능한 기능!!)
	 * 			2022-03-17
	 * 			단, 메일까지 발송된 도서는 DCC도 Issue 취소 불가(사용자,DCC,ADMIN 모두 불가)
	 * @throws ParseException 
	 */
	@RequestMapping(value = "getIsUseDRN.do")
	@ResponseBody
	public Object getUseDocDRN(HttpServletRequest request,PrjEmailVO prjemailvo,PrjTrVO prjtrvo,PrjMemberVO prjmembervo,PrjDrnInfoVO prjdrninfovo){
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		prjmembervo.setDiff_dcc_user(sessioninfo.getUser_id());
		PrjMemberVO getPrjDCCYN = prjinfoService.getPrjDCCYN(prjmembervo);
		prjdrninfovo.setMod_id(sessioninfo.getUser_id());
		prjdrninfovo.setMod_date(CommonConst.currentDateAndTime());
		int getEmailSentTimes = prjemailService.getEmailSentTimes(prjemailvo);
		if(getEmailSentTimes>0) {
			if((sessioninfo.getAdmin_yn()!=null && sessioninfo.getAdmin_yn().equals("Y")) || (getPrjDCCYN!=null && getPrjDCCYN.getDcc_yn().equals("Y"))){ //ADMIN Y일 경우 메일 발송되었더라도 이슈 취소 가능
				//2022-04-22 요청에 따른 수정. DCC도 메일 발송되었더라도 이슈 취소 가능
				result.add("ADMIN취소가능");
				if(prjtrvo.getProcessType().equals("TR")) {
					PrjTrVO getTrInfo = prjtrService.getTrInfo(prjtrvo);
					PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getTrInfo);
					if(getIsUseDRN!=null) { //DRN 승인 도서의 경우 반려처리로 넘김
						result.add(getIsUseDRN.getDrn_no());
					} else {	//기본 TR 생성일 경우 ISSUE CANCEL
					     result.add("ADMIN 취소");
					}
				}else if(prjtrvo.getProcessType().equals("VTR")) {
					PrjTrVO getVTrInfo = prjtrService.getVTrInfo(prjtrvo);
					PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getVTrInfo);
					if(getIsUseDRN!=null) { //DRN 승인 도서의 경우 반려처리로 넘김
						result.add(getIsUseDRN.getDrn_no());
					} else {	//기본 TR 생성일 경우 ISSUE CANCEL
					     result.add("ADMIN 취소");
					}
				}else if(prjtrvo.getProcessType().equals("STR")) {
					PrjTrVO getSTrInfo = prjtrService.getSTrInfo(prjtrvo);
					PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getSTrInfo);
					if(getIsUseDRN!=null) { //DRN 승인 도서의 경우 반려처리로 넘김
						result.add(getIsUseDRN.getDrn_no());
					} else {	//기본 TR 생성일 경우 ISSUE CANCEL
					     result.add("ADMIN 취소");
					}
				}
			}else {
				result.add("취소불가");
			}
		}else {
			if(prjtrvo.getProcessType().equals("TR")) {
				PrjTrVO getTrInfo = prjtrService.getTrInfo(prjtrvo);
				PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getTrInfo);
				if((getPrjDCCYN!=null && getPrjDCCYN.getDcc_yn().equals("Y")) || (sessioninfo.getAdmin_yn().equals("Y"))) { //DCC나 ADMIN일 경우 ISSUE CANCEL 가능
					result.add("DCC USER");
					if(getIsUseDRN!=null) { //DRN 승인 도서의 경우 반려처리로 넘김
						result.add(getIsUseDRN.getDrn_no());
					} else {	//기본 TR 생성일 경우 ISSUE CANCEL
					     result.add("DCC 취소");
					}
				}else {
					if(getIsUseDRN!=null) { //DCC가 아닐 경우 ISSUE CANCEL 불가
						result.add(getIsUseDRN.getDrn_no());
					} else {
					     result.add("취소가능");
					}
				}
			}else if(prjtrvo.getProcessType().equals("VTR")) {
				PrjTrVO getVTrInfo = prjtrService.getVTrInfo(prjtrvo);
				PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getVTrInfo);
				if((getPrjDCCYN!=null && getPrjDCCYN.getDcc_yn().equals("Y")) || (sessioninfo.getAdmin_yn().equals("Y"))) { //DCC일 경우 ISSUE CANCEL 가능
					result.add("DCC USER");
					if(getIsUseDRN!=null) { //DRN 승인 도서의 경우 반려처리로 넘김
						result.add(getIsUseDRN.getDrn_no());
					} else {	//기본 TR 생성일 경우 ISSUE CANCEL
					     result.add("DCC 취소");
					}
				}else {
					if(getIsUseDRN!=null) { //DCC가 아닐 경우 ISSUE CANCEL 불가
						result.add(getIsUseDRN.getDrn_no());
					} else {
					     result.add("취소가능");
					}
				}
			}else if(prjtrvo.getProcessType().equals("STR")) {
				PrjTrVO getSTrInfo = prjtrService.getSTrInfo(prjtrvo);
				PrjDrnInfoVO getIsUseDRN = prjtrService.getIsUseDRN(getSTrInfo);
				if((getPrjDCCYN!=null && getPrjDCCYN.getDcc_yn().equals("Y")) || (sessioninfo.getAdmin_yn().equals("Y"))) { //DCC나 ADMIN일 경우 ISSUE CANCEL 가능
					result.add("DCC USER");
					if(getIsUseDRN!=null) { //DRN 승인 도서의 경우 반려처리로 넘김
						result.add(getIsUseDRN.getDrn_no());
					} else {	//기본 TR 생성일 경우 ISSUE CANCEL
					     result.add("DCC 취소");
					}
				}else {
					if(getIsUseDRN!=null) { //DCC가 아닐 경우 ISSUE CANCEL 불가
						result.add(getIsUseDRN.getDrn_no());
					} else {
					     result.add("취소가능");
					}
				}
			}
		}
		return result;
	}
}
