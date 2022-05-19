package kr.co.hhi.common.schedule;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.model.DrnLineVO;
import kr.co.hhi.model.NotificationVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.service.DrnService;
import kr.co.hhi.service.NotificationService;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.PrjEmailService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.porServiceOracle;

@Component
public class Schedule {
	
	protected DrnService drnService;
	protected NotificationService notificationService;
	protected PrjDocumentIndexService prjdocumentindexService;
	protected porServiceOracle porServiceOracle;
	protected PrjStepService prjstepService;
	protected PrjInfoService prjinfoService;
	
	@Autowired
	public void setDrnService(DrnService drnService) {
		this.drnService = drnService;
	}
	@Autowired
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setporServiceOracle(porServiceOracle porServiceOracle) {
		this.porServiceOracle = porServiceOracle;
	}
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}

    @Value("#{config['sys.email.main.account']}")
    private String MAIL_ACCOUNT;

    @Value("#{config['sys.email.main.sender.id']}")
    private String MAIL_ID;

    @Value("#{config['server.domain']}")
    private String SERVER;

    
	/**
	 * 
	 * 1. 메소드명 : expiredDrnTempSaveDel
	 * 2. 작성일: 2022. 1. 21.
	 * 3. 작성자: doil
	 * 4. 설명: 임시저장 상태로 10일 넘게 저장되어 있는 DRN 삭제 및 checkOut cancel
	 * 5. 수정일: doil
	 * @throws ParseException 
	 */
	public void expiredDrnTempSaveDel() throws ParseException {
		// 현재시간 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, -10);
		String now = sdf.format(cal.getTime());
		
		// 만료된 DRN 임시저장 파일 수집
		List<DrnLineVO> collector  = drnService.getExpiredTempSaveDrns(now);
		
		
		for(DrnLineVO item : collector) {
			try {
				
				// 해당 drn에 첨부된 doc들 체크아웃 해제
				List<DrnLineVO> docList = drnService.getDocsFromDrn(item);
				for(DrnLineVO doc : docList) {
					if(drnService.cancelCheckOut(doc) < 1) throw new Exception("CheckOut 해제 실패");					
				}
			
				
				int delCnt = drnService.delExpiredTempSaveDrns(item);
				if(delCnt < 1) throw new Exception("DRN 삭제 실패");
				System.out.println("*["+ delCnt +" rows deleted]" );
				
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		
		System.out.println("["+ now +"]:expiredDrnTempSaveDel");
		
		
		
	}

	/**
	 * 1. 메소드명 : dailyNotification
	 * 2. 작성일: 2022
	 * 3. 작성자: 소진희
	 * 4. 설명: 새벽 4시마다 제출 지연 도서등의 관련자들에게 메일알림을 보냄
	 * 5. 수정일: 
	 */
	public void dailyNotification() throws ParseException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String currentDateAndTime = CommonConst.currentDateAndTime();
		PrjEmailVO prjemailvo = new PrjEmailVO();
		prjemailvo.setEmail_type("notification");
		prjemailvo.setEmail_id("notification"+currentDateAndTime.substring(0,8));
		prjemailvo.setSubject("[VISION DCS SYSTEM] DAILY NOTIFICATION "+CommonConst.stringdateTOYYYYMMDD(currentDateAndTime));
		String delaydoc = notificationService.DelayDoc();
		String incoming = notificationService.Incoming();
		String outgoing = notificationService.Outgoing();
		String contents = delaydoc + incoming + outgoing;
		prjemailvo.setContents(contents);
		prjemailvo.setEmail_sender_user_id(MAIL_ID);
		prjemailvo.setEmail_sender_addr(MAIL_ACCOUNT);
		List<String> receivers = notificationService.getReceiverId();
		System.out.println(receivers);
		PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
		List<String> receiver_email = new ArrayList<>();
		for(int i=0;i<receivers.size();i++) {
			prjdocumentindexvo.setUser_id(receivers.get(i));
			String encodedEmailAddr = notificationService.getUserEmailAddr(prjdocumentindexvo);
			String email_addr = aes256.aesDecode(encodedEmailAddr);
			receiver_email.add(email_addr);
		}
		String getEmailSettingReceiver = notificationService.getEmailSettingReceiver();
		String[] getEmailSettingReceiverArray = getEmailSettingReceiver.split(";");
		for(int i=0;i<getEmailSettingReceiverArray.length;i++) {
			receiver_email.add(getEmailSettingReceiverArray[i]);
		}
		Set<String> set = new HashSet<String>(receiver_email);
		List<String> receiver_email_addr_list = new ArrayList<String>(set);
		String email_receiver_addr = String.join(";", receiver_email_addr_list);
		System.out.println("email_receiver_addr: "+email_receiver_addr);
		prjemailvo.setEmail_receiver_addr(email_receiver_addr);
		prjemailvo.setEmail_send_date(currentDateAndTime);
		prjemailvo.setSend_yn("N");
		prjemailvo.setReg_id("ADMIN");
		prjemailvo.setReg_date(currentDateAndTime);
		prjemailvo.setMod_id("ADMIN");
		prjemailvo.setMod_date(currentDateAndTime);
		int insertDailyNotificationEmailSendInfo = notificationService.insertDailyNotificationEmailSendInfo(prjemailvo);
		if(insertDailyNotificationEmailSendInfo==0) System.out.println(CommonConst.stringdateTOYYYYMMDD(currentDateAndTime)+" Daily Notification 발송 실패");
	}

	/**
	 * 1. 메소드명 : updatePorNo
	 * 2. 작성일: 2022-03-21
	 * 3. 작성자: 소진희
	 * 4. 설명: POR NO는 있는데 PO NO가 없는 도서를 1시간마다 찾아서 PO NO 정보가 있을 경우 업데이트해줌 (Procurement) 매시 정각 실행
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	public void updatePoNo() throws ParseException{
		List<PrjDocumentIndexVO> getPoNoIsNull = prjdocumentindexService.getPoNoIsNull();
		PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
		String currentDate = CommonConst.currentDateAndTime().substring(0, 8);
		PrjStepVO prjstepvo = new PrjStepVO();
		prjstepvo.setMod_id("SYSTEM");
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());
		PrjInfoVO setPrjInfo = new PrjInfoVO();
		for(int i=0;i<getPoNoIsNull.size();i++) {
			setPrjInfo.setPrj_id(getPoNoIsNull.get(i).getPrj_id());
			PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(setPrjInfo);
			prjdocumentindexvo.setPrj_no(getPrjInfo.getPrj_no());
			prjdocumentindexvo.setPor_no(getPoNoIsNull.get(i).getPor_no());
			PrjDocumentIndexVO getPORInfo = porServiceOracle.getPORInfo(prjdocumentindexvo);
			prjdocumentindexService.updatePoNo(getPORInfo);

			//PO 발행시 Actual Date 저장
			prjstepvo.setPrj_id(getPoNoIsNull.get(i).getPrj_id());
			prjstepvo.setDoc_id(getPoNoIsNull.get(i).getDoc_id());
			prjstepvo.setRev_id(getPoNoIsNull.get(i).getRev_id());
			prjstepvo.setSet_val2("PO 발행시");
			prjstepvo.setActual_date(getPORInfo.getPo_issue_date());
			int updateProcActualDateBySetPORorPO = prjstepService.updateProcActualDateBySetPORorPO(prjstepvo);
			if(updateProcActualDateBySetPORorPO>0) {
				//ForecastDate 업데이트
				List<PrjCodeSettingsVO> getUpdateProcStepCodeId = prjstepService.getUpdateProcStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateProcStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateProcStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateProcStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByProcStep = prjstepService.getForeDateRuleByProcStep(prjstepvo);
					if(!getForeDateRuleByProcStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByProcStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByProcStep.getSet_val3());
						PrjStepVO getActualDateProcStepByStepCodeId = prjstepService.getActualDateProcStepByStepCodeId(prjstepvo);
						if(getActualDateProcStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateProcStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByProcStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByProcStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateProcStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 1. 메소드명 : updateBulkPoNo
	 * 2. 작성일: 2022-03-26
	 * 3. 작성자: 소진희
	 * 4. 설명: POR NO는 있는데 PO NO가 없는 도서를 1시간마다 찾아서 PO NO 정보가 있을 경우 업데이트해줌 (Bulk) 매시 5분 실행
	 * 5. 수정일: 
	 * @throws ParseException 
	 */
	public void updateBulkPoNo() throws ParseException{
		List<PrjDocumentIndexVO> getBulkPoNoIsNull = prjdocumentindexService.getBulkPoNoIsNull();
		PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
		String currentDate = CommonConst.currentDateAndTime().substring(0, 8);
		PrjStepVO prjstepvo = new PrjStepVO();
		prjstepvo.setMod_id("SYSTEM");
		prjstepvo.setMod_date(CommonConst.currentDateAndTime());
		PrjInfoVO setPrjInfo = new PrjInfoVO();
		for(int i=0;i<getBulkPoNoIsNull.size();i++) {
			setPrjInfo.setPrj_id(getBulkPoNoIsNull.get(i).getPrj_id());
			PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(setPrjInfo);
			prjdocumentindexvo.setPrj_no(getPrjInfo.getPrj_no());
			prjdocumentindexvo.setPor_no(getBulkPoNoIsNull.get(i).getPor_no());
			PrjDocumentIndexVO getPORInfo = porServiceOracle.getPORInfo(prjdocumentindexvo);
			prjdocumentindexService.updateBulkPoNo(getPORInfo);

			//PO 발행시 Actual Date 저장
			prjstepvo.setPrj_id(getBulkPoNoIsNull.get(i).getPrj_id());
			prjstepvo.setDoc_id(getBulkPoNoIsNull.get(i).getDoc_id());
			prjstepvo.setRev_id(getBulkPoNoIsNull.get(i).getRev_id());
			prjstepvo.setSet_val2("PO 발행시");
			prjstepvo.setActual_date(getPORInfo.getPo_issue_date());
			int updateBulkActualDateBySetPORorPO = prjstepService.updateBulkActualDateBySetPORorPO(prjstepvo);
			if(updateBulkActualDateBySetPORorPO>0) {
				//ForecastDate 업데이트
				List<PrjCodeSettingsVO> getUpdateBulkStepCodeId = prjstepService.getUpdateBulkStepCodeId(prjstepvo);
				for(int k=0;k<getUpdateBulkStepCodeId.size();k++) {
					prjstepvo.setSet_val3(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					prjstepvo.setSet_code_id(getUpdateBulkStepCodeId.get(k).getSet_code_id());
					PrjCodeSettingsVO getForeDateRuleByBulkStep = prjstepService.getForeDateRuleByBulkStep(prjstepvo);
					if(!getForeDateRuleByBulkStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4())!=0) {
						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
						Calendar cal = Calendar.getInstance();
						prjstepvo.setStep_code_id(getForeDateRuleByBulkStep.getSet_val3());
						PrjStepVO getActualDateBulkStepByStepCodeId = prjstepService.getActualDateBulkStepByStepCodeId(prjstepvo);
						if(getActualDateBulkStepByStepCodeId!=null) {
							Date dt = dtFormat.parse(getActualDateBulkStepByStepCodeId.getActual_date());
							cal.setTime(dt);
							if(!getForeDateRuleByBulkStep.getSet_val4().equals("")) {
								cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByBulkStep.getSet_val4()));
								prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
								prjstepService.updateBulkStepForeDateByStepActualDateUpdate(prjstepvo);
							}
						}
					}
				}
			}
		}
	}


	/**
	 * 1. 메소드명 : corrReplyNotification
	 * 2. 작성일: 2022-04-02
	 * 3. 작성자: 소진희
	 * 4. 설명: correspondence Incoming 도서에 일정관리를 체크하고 회신요구일자를 기록한 경우 해당도서의 책임자에게 하루전날 이메일 발송
	 * 5. 수정일: 
	 */
	public void corrReplyNotification() throws ParseException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		String currentDateAndTime = CommonConst.currentDateAndTime();
		PrjEmailVO prjemailvo = new PrjEmailVO();
		prjemailvo.setEmail_type("corrreplynoti");
		prjemailvo.setEmail_id("corrreply"+currentDateAndTime.substring(0,8));
		PrjDocumentIndexVO prjdocumentindexvo2 = new PrjDocumentIndexVO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(currentDateAndTime.substring(0,8));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1); // 다음날(1일 후)
        String resultDate = sdf.format(cal.getTime());
        String replyreqdate = resultDate.substring(0, 8);
		prjdocumentindexvo2.setReplyreqdate(replyreqdate);
		List<PrjDocumentIndexVO> getCorrReplyRequest = notificationService.getCorrReplyRequest(prjdocumentindexvo2);
		for(int i=0;i<getCorrReplyRequest.size();i++) {
			PrjDocumentIndexVO corrReplyRequest = getCorrReplyRequest.get(i);
			int count = notificationService.isSentCorrEmail(corrReplyRequest);
			if(count==0) {
				PrjDocumentIndexVO getCorrInfo = notificationService.getCorrInfo(corrReplyRequest);
				prjemailvo.setSubject("[VISION DCS SYSTEM] "+getCorrInfo.getDoc_no()+" 도서 회신 요구일자 임박");
				prjemailvo.setContents("<a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getCorrInfo.getPrj_id()+"&page=IncomingCorr&folderId="+getCorrInfo.getFolder_id()+"\">"+getCorrInfo.getDoc_no()+" 도서 회신하러 가기</a></td>");
				prjemailvo.setEmail_sender_user_id(MAIL_ID);
				prjemailvo.setEmail_sender_addr(MAIL_ACCOUNT);
				PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO(); 
				prjdocumentindexvo.setUser_id(corrReplyRequest.getPerson_in_charge_id());
				String encodedEmailAddr = notificationService.getUserEmailAddr(prjdocumentindexvo);
				String email_addr = aes256.aesDecode(encodedEmailAddr);
				prjemailvo.setEmail_receiver_addr(email_addr);
				prjemailvo.setEmail_send_date(currentDateAndTime);
				prjemailvo.setSend_yn("N");
				prjemailvo.setReg_id("ADMIN");
				prjemailvo.setReg_date(currentDateAndTime);
				prjemailvo.setMod_id("ADMIN");
				prjemailvo.setMod_date(currentDateAndTime);
				int insertCorrRequestNotificationEmailSendInfo = notificationService.insertCorrRequestNotificationEmailSendInfo(prjemailvo);			
			}
		}
	}
}