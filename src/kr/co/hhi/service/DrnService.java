package kr.co.hhi.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.common.util.FileExtFilter;
import kr.co.hhi.model.DocumentIndexVO;
import kr.co.hhi.model.DrnLineVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.model.UsersVO;

@Service
public class DrnService extends AbstractService {
	
	
    @Value("#{config['server.domain']}")
    private String SERVER;

    @Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
    
    @Value("#{config['file.upload.prj.doc.path']}")
    private String DOC_PATH;

    @Value("#{config['file.upload.prj.template.path']}")
    private String TEMPLATE_PATH;
    
    @Value("#{config['file.upload.pcfile']}")
    private String PCFILE_PATH;
    
	public List<PrjCodeSettingsVO> getStatus(DrnLineVO drnLineVO) {
		
		List<PrjCodeSettingsVO> result; 
		
		PrjCodeSettingsVO getCodeVO = new PrjCodeSettingsVO();
		getCodeVO.setPrj_id(drnLineVO.getPrj_id());
		
		switch(drnLineVO.getType()) {
			case "DCI" :
				result = selectList("prjcodesettingsSqlMap.getTrIssuePurpose",getCodeVO);
				break;
			case "VDCI" :
				result = selectList("prjcodesettingsSqlMap.getVtrIssuePurpose",getCodeVO);
				break;
			case "SDCI" :
				result = selectList("prjcodesettingsSqlMap.getStrIssuePurpose",getCodeVO);
				break;
			default :
				return null;
		}
		
		
		return result;
	}
	
	public String get_DRNType(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.get_DRNType",drnLineVO);
	}

	public List<DrnLineVO> getCheckSheetList(DrnLineVO drnLineVO) {
		return selectList("drnSqlMap.getCheckSheetList",drnLineVO);
	}
	
	
	public List<DrnLineVO> getCheckSheetItem(DrnLineVO drnLineVO) {
		return selectList("drnSqlMap.getCheckSheetItem",drnLineVO);
	}

	public String getFolderPath(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.getFolderPath",drnLineVO);
	}

	public String getMaxDrnId(DrnLineVO drnLineVO) {
		Integer max = selectOne("drnSqlMap.getMaxDrnId",drnLineVO);
		
		return String.format("%05d", max); 
	}

	public long getMaxLineId(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.getMaxLineId",drnLineVO);
	}

	public long insertDrnLine(DrnLineVO drnLineVO) {
		
		insert("drnSqlMap.insertDrnLine",drnLineVO);
		return drnLineVO.getApproval_id();
		
		
	}
	
	public String getMaxSheetId(DrnLineVO drnLineVO) {
		long max = selectOne("drnSqlMap.getMaxSheetId",drnLineVO);
		return String.format("%05d", max );
	}


	public int insertCheckSheetList(DrnLineVO drnLineVO) {
		return insert("drnSqlMap.insertCheckSheetList",drnLineVO);
	}

	public int insertCheckSheet(DrnLineVO drnLineVO) {
		return insert("drnSqlMap.insertCheckSheet",drnLineVO);		
	}


	public long getMaxItemId(DrnLineVO drnLineVO) {
		// TODO Auto-generated method stub
		return selectOne("drnSqlMap.getMaxItemId",drnLineVO);
	}

	public int insertDrnInfo(DrnLineVO drnLineVO) {
		return insert("drnSqlMap.insertDrnInfo",drnLineVO);
	}

	public String getReviewConclusion(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.getReviewConclusion",drnLineVO);
	}

	
	public DrnLineVO get_drnPage(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.get_drnPage",drnLineVO);
	}


	public List<DrnLineVO> get_checkList(DrnLineVO drnLineVO) {
		return selectList("drnSqlMap.get_checkList",drnLineVO);
	}

	public int insertDrnHist(DrnLineVO drnLineVO) {
		String action_id = drnLineVO.getAction_id();
		String[] multiActionId_Chk = action_id.split(";");
		// action_id가 다중으로 들어온 경우 (DCC가 다수인 경우 E-mail로 승인하는 경우에 해당)
		// 첫번째 유저로 할당
		if(multiActionId_Chk.length>1) { 
			action_id = multiActionId_Chk[0];
			drnLineVO.setAction_id(action_id);
		}
		return insert("drnSqlMap.insertDrnHist",drnLineVO);
	}

	public List<DrnLineVO> getSignData(DrnLineVO drnInfo) {
		return selectList("drnSqlMap.getSignData",drnInfo);
	}
	
	public DrnLineVO getApprovalData(DrnLineVO drnInfo) {
			// TODO Auto-generated method stub
		return selectOne("drnSqlMap.getApprovalData",drnInfo);
	}
	
	public List<DrnLineVO> getSysDocsData(DrnLineVO drnInfo) {
		return selectList("drnSqlMap.getSysDocsData",drnInfo);
	}
	
	public List<DrnLineVO> getPcDocsData(DrnLineVO drnInfo) {
		return selectList("drnSqlMap.getPcDocsData",drnInfo);
	}
	
	public int insertDrnDocs(DrnLineVO update) {
		return insert("drnSqlMap.insertDrnDocs",update);
	}

	public int insertDrnPCDocs(DrnLineVO update) {
		return insert("drnSqlMap.insertDrnPCDocs",update);
	}

	public String getDiscipCode(DrnLineVO drnInfo) {
		return selectOne("drnSqlMap.getDiscipCode",drnInfo);
	}

	public int generateDrnInfo(DrnLineVO update) {
		return insert("drnSqlMap.generateDrnInfo",update);
	}

	public int updateDrnInfo(DrnLineVO update) {
		String action_id = update.getAction_id();
		return update("drnSqlMap.updateDrnInfo",update);
	}
	
	public int updateDrnInfo2(DrnLineVO update) {
		String action_id = update.getAction_id();
		String[] multiActionId_Chk = action_id.split(";");
		// action_id가 다중으로 들어온 경우 (DCC가 다수인 경우 E-mail로 승인하는 경우에 해당)
		// 첫번째 유저로 할당
		if(multiActionId_Chk.length>1) { 
			action_id = multiActionId_Chk[0];
			update.setAction_id(action_id);
		}
		return update("drnSqlMap.updateDrnInfo2",update);
	}

	
	public String getKorNm(String id) {
		return selectOne("drnSqlMap.getKorNm",id);
	}

	public int getDrnSeq(String string) {
		return selectOne("drnSqlMap.getDrnSeq",string);
	}

	public int updateCheckSheetItem(DrnLineVO row) {
		return update("drnSqlMap.updateCheckSheetItem",row);
	}

	public int checkOutDoc(DrnLineVO update) {
		// TODO Auto-generated method stub
		return update("drnSqlMap.checkOutDoc",update);
	}

	public String getApprovalSignDate(DrnLineVO drnInfo) {
		// TODO Auto-generated method stub
		List<String> get = selectList("drnSqlMap.getApprovalSignDate",drnInfo);
		if(get.size()>0)
			return get.get(0);
		
		else return null;
	}


	public UsersVO getUserSignData(String id) {
		return selectOne("drnSqlMap.getUserSignData", id);
	}

	public String getProcessType(ProcessInfoVO getInfo) {
		return selectOne("drnSqlMap.getProcessType",getInfo);
	}

	public List<PrjCodeSettingsVO> getCodeList(PrjCodeSettingsVO getCode) {
		return selectList("drnSqlMap.getCodeList",getCode);
	}

	public int engStep(PrjCodeSettingsVO code) {
		return update("drnSqlMap.engStep",code);
	}

	public int vendorStep(PrjCodeSettingsVO code) {
		return update("drnSqlMap.vendorStep",code);
	}

	public int siteStep(PrjCodeSettingsVO code) {
		return update("drnSqlMap.siteStep",code);
	}

	public String getSetVal3(PrjCodeSettingsVO getCode) {
		return selectOne("drnSqlMap.getSetVal3",getCode);
	}

	public List<DrnLineVO> getCheckSheetItemList(DrnLineVO update) {
		// TODO Auto-generated method stub
		return selectList("drnSqlMap.getCheckSheetItemList",update);
	}

	public String getApprovalStatus(DrnLineVO drnLineVO) {
		// TODO Auto-generated method stub
		return selectOne("drnSqlMap.getApprovalStatus",drnLineVO);
	}

	public String getRevNo(DrnLineVO getRevNo) {
		return selectOne("drnSqlMap.getRevNo",getRevNo);
	}

	public int cancelCheckOut(DrnLineVO cancelCheck) {
		return update("drnSqlMap.cancelCheckOut",cancelCheck);
	}

	public List<DrnLineVO> getExpiredTempSaveDrns(String now) {
		return selectList("drnSqlMap.getExpiredTempSaveDrns",now);
	}

	public int delExpiredTempSaveDrns(DrnLineVO item) {
		return delete("drnSqlMap.delExpiredTempSaveDrns",item);
	}

	public List<DrnLineVO> getDocsFromDrn(DrnLineVO item) {
		return selectList("drnSqlMap.getDocsFromDrn",item);
	}

	public int delAttchFile(DrnLineVO doc) {
		return delete("drnSqlMap.delAttchFile",doc);		
	}


	public String getDesigner(DrnLineVO update) {
		return selectOne("drnSqlMap.getDesigner",update);
	}
	
	public DrnLineVO getDrnInfo(DrnLineVO drnInfo) {
		// TODO Auto-generated method stub
		return selectOne("drnSqlMap.getDrnInfo",drnInfo);
	}

	public List<DrnLineVO> getDocsInfo(DrnLineVO drnInfo) {
		return selectList("drnSqlMap.getDocsInfo",drnInfo);
	}

	public DrnLineVO getStatusCode(DrnLineVO drnInfo) {
		String type = null;
		
		switch(drnInfo.getDoc_type()) {
			case "DCI":
				type = "TR";
				break;
			case "VDCI":
				type = "VTR";
				break;
			case "SDCI":
				type = "STR";
				break;
		}
		
		type += " ISSUE PURPOSE";
		
		drnInfo.setType(type);
		return selectOne("drnSqlMap.getStatusCode",drnInfo);
	}

	public String getSetVal2(PrjCodeSettingsVO getCode) {
		return selectOne("drnSqlMap.getSetVal2",getCode);
	}

	 // app.properties에 있는 upload path
    @Value("#{config['sys.email.main.account']}")
	private String SENDER_EMAIL;
    
    @Value("#{config['sys.email.main.sender.id']}")
	private String SENDER_ID;
    
	public int sendEmail(DrnLineVO update) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		// 이메일 전송
		PrjEmailVO email = new PrjEmailVO();
		String server = SERVER;
		
		email.setEmail_type("drn");
		email.setEmail_id(update.getPrj_id()+"@"+update.getDrn_id());
		
		String subject = String.format("[%s]",update.getDrn_no());
		String content = "Doc title : %s <br>"
					   + "해당 도서가 [%s]로 부터 DRN 결재가 요청되었습니다.<br>";
				
		String contents = "";
		String link = "";
		
		
		email.setEmail_sender_user_id(update.getReg_id());
		
		
		email.setReg_date(update.getReg_date());
		email.setEmail_send_date(update.getReg_date());
		email.setSend_yn("N");
		
		// approval_status
		String as = update.getDrn_approval_status();
		String sender_id = SENDER_ID;
		String sender_email = SENDER_EMAIL;
		String sender_name;
		String receiver_id = "";
		String receiver_email = "";
		
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		
		DrnLineVO approvalVO = selectOne("drnSqlMap.getApprovalData",update);
		List<DrnLineVO> dccUser = selectList("drnSqlMap.get_dccUser",update);
		
		
		String comment = "";
		boolean readOnly = false;
		boolean sendAll = false;
		switch(as) {
			case "A" : // 기안자 > dcc
				sender_name = selectOne("drnSqlMap.getSenderNm",update.getReg_id());
				subject = "[DRN 승인 요청]" + subject;
				content = String.format(content, update.getDoc_title() , sender_name);
				String _receiver_id = "";
				String _receiver_email = "";
				// dcc의 경우에는 n명이므로 모든 dcc에게 전달하는 구조로 구현
				for(DrnLineVO dcc : dccUser) {
					receiver_id = dcc.getUser_id();
					receiver_email = selectOne("drnSqlMap.getEmailAddr",receiver_id);
					receiver_email = aes256.aesDecode(receiver_email);
					
					_receiver_id += receiver_id + ";";
					_receiver_email += receiver_email + ";";
				}
				// 마지막 쉼표(,) 자르기
				_receiver_email = _receiver_email.substring(0,_receiver_email.length()-1);
				_receiver_id = _receiver_id.substring(0,_receiver_id.length()-1);
				
				
				email.setEmail_sender_user_id(sender_id);
				email.setEmail_sender_addr(sender_email);
				email.setEmail_receiver_addr(_receiver_email);
				
				link = String.format(server + "drnEmail.do?prj_id=%s&drn_id=%s&user_id=%s",update.getPrj_id(),update.getDrn_id(),_receiver_id);
				email.setSubject(subject);
				contents = String.format("<a href='%s'>검토 바로가기</a><br>", link);
				email.setContents(content +"<br>" + contents);
				
				try {
					if(insert("drnSqlMap.sendEmail",email)<0)
						throw new Exception("이메일 DB입력 실패");
				} catch(Exception e) {
					System.out.println(e.getMessage());
					System.out.println(e.getStackTrace());
				}
				
						
				return 1;
				
			case "E" : // dcc > 리뷰어
				String dcc = update.getAction_id().split(";")[0];
				sender_name = selectOne("drnSqlMap.getSenderNm",dcc);
				content = String.format(content, update.getDoc_title() , sender_name);
				
				receiver_id = approvalVO.getReviewed_id();
				receiver_email = selectOne("drnSqlMap.getEmailAddr",receiver_id);
				receiver_email = aes256.aesDecode(receiver_email);
				subject = "[DRN 승인 요청]" + subject;
				comment = "Comment : " + update.getComment();
				break;
			case "1" : // 리뷰어 > 결재자
				sender_name = selectOne("drnSqlMap.getSenderNm",update.getAction_id());
				receiver_id = approvalVO.getApproved_id();
				content = String.format(content, update.getDoc_title() , sender_name);
				receiver_email = selectOne("drnSqlMap.getEmailAddr",receiver_id);
				receiver_email = aes256.aesDecode(receiver_email);
				subject = "[DRN 승인 요청]" + subject;
				comment = "Comment : " + update.getComment();
				break;
			case "C" : // 결재자 > 기안자
				sender_name = selectOne("drnSqlMap.getSenderNm",update.getAction_id());
				receiver_id = approvalVO.getReg_id();
				content = "Doc title : %s <br>"
						   + "해당 도서가 [%s]로 부터 DRN 결재가 완료되었습니다.<br>";
				content = String.format(content, update.getDoc_title() , sender_name);
				
				receiver_email = selectOne("drnSqlMap.getEmailAddr",receiver_id);
				receiver_email = aes256.aesDecode(receiver_email);
				subject = "[DRN 결재 완료]" + subject;
				comment = "Comment : " + update.getComment();
				readOnly = true;
				sendAll = true;
				break;				
			case "R" : // 거절한 사람 > 기안자
				sender_name = selectOne("drnSqlMap.getSenderNm",update.getAction_id());
				receiver_id = update.getReg_id();
				content = "Doc title : %s <br>"
						   + "해당 도서가 [%s]로 부터 DRN 결재가 반려 되었습니다.<br>";
				content = String.format(content, update.getDoc_title() , sender_name);
				
				receiver_email = selectOne("drnSqlMap.getEmailAddr",receiver_id);
				receiver_email = aes256.aesDecode(receiver_email);
				subject = "[DRN 결재 반려]" + subject;
				comment = "반려 사유 : " + update.getRejectReason();
				readOnly = true;
				sendAll = true;
				break;
		
		}
		
		email.setSubject(subject);
		

		email.setEmail_sender_user_id(sender_id);
		email.setEmail_sender_addr(sender_email);
		String refer_to = "";
		
		if(sendAll) {
			receiver_email = ""; // 초기화
			String designer,reviewer,approver;
			String [] distributer;
			
			designer = update.getReg_id(); // 기안자
			receiver_email += aes256.aesDecode( selectOne("drnSqlMap.getEmailAddr",designer)) + ";";
			
			reviewer = approvalVO.getReviewed_id(); // 검토자
			receiver_email += aes256.aesDecode( selectOne("drnSqlMap.getEmailAddr",reviewer)) + ";";
			
			approver = approvalVO.getApproved_id(); // 결재자
			receiver_email += aes256.aesDecode( selectOne("drnSqlMap.getEmailAddr",approver));
			
			// 배포선 설정
			String distribute_id = approvalVO.getDistribute_id();
			if(!distribute_id.equals("")) {
				distributer = approvalVO.getDistribute_id().split("@");
				
				for(String id : distributer) 
					refer_to += aes256.aesDecode( selectOne("drnSqlMap.getEmailAddr",id)) + ";";
				
				if(refer_to.length()>0)
					refer_to = refer_to.substring(0,refer_to.length()-1);
			}
									
		}
		
		
		email.setEmail_receiver_addr(receiver_email);
		email.setReg_id(sender_id);
		if(refer_to.length()>0) email.setEmail_refer_to(refer_to);
		
		link = String.format(server + "drnEmail.do?prj_id=%s&drn_id=%s&user_id=%s",update.getPrj_id(),update.getDrn_id(),receiver_id);
		// 
		String refer_link = String.format(server + "drnEmail.do?prj_id=%s&drn_id=%s&user_id=%s",update.getPrj_id(),update.getDrn_id(),"DISTRIBUTE_READONLY");
		
		contents = String.format("%s<br>%s<br> <br><a style='text-decoration: underline; color: blue;' href='%s'>DRN 결재 바로가기</a><br>", content, comment,link);
		email.setContents(contents);
		
		contents = String.format("%s<br><a style='text-decoration: underline; color: blue;' href='%s'>DRN 내용 확인</a><br>", comment,refer_link);
		if(readOnly) email.setContents(content +"<br>" + contents);
		
		try {
			if(insert("drnSqlMap.sendEmail",email)<0)
				throw new Exception("이메일 DB입력 실패");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			return -1;
		}
		
		
		
		return 1;
	}


	public Long approvalIdCheck(DrnLineVO update) {
		return selectOne("drnSqlMap.approvalIdCheck",update);
	}
	
	public boolean isExistDoc(DrnLineVO update) {
		DrnLineVO chk = selectOne("drnSqlMap.isExistDoc",update);
		if(chk!=null) return true;
		else return false;
	}

	public List<DrnLineVO> getRejectPoint(DrnLineVO drnLineVO) {
		return selectList("drnSqlMap.getRejectPoint",drnLineVO);
	}

	public DrnLineVO isUploadedPCDocs(DrnLineVO update) {
		return selectOne("drnSqlMap.isUploadedPCDocs",update);
	}


	public int updateDrnPCDocs(DrnLineVO update) {
		return update("drnSqlMap.updateDrnPCDocs",update);
	}

	public List<PrjCodeSettingsVO> getUpdateActualCodeByDRN(PrjCodeSettingsVO getCode) {
		return selectList("drnSqlMap.getUpdateActualCodeByDRN",getCode);
	}

	public List<DrnLineVO> drnUsedChk(DrnLineVO drnLineVO) {
		return selectList("drnSqlMap.drnUsedChk",drnLineVO);
	}


	public List<DrnLineVO> trUsedChk(DrnLineVO drnLineVO) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<PrjCodeSettingsVO> getStep(PrjStepVO prjstepvo) {
		return selectList("drnSqlMap.getStep",prjstepvo);
	}

		
	public String getActualDate(PrjCodeSettingsVO code) {
		return selectOne("drnSqlMap.getActualDate",code);
	}


	public String getDocRevCode(DrnLineVO drnLineVO) {
		String rev_code = selectOne("drnSqlMap.getDocRevCode",drnLineVO);
		return rev_code;
	}

		
	public boolean checkTrCover(DrnLineVO update) {
		int cnt = 0;
		String trfilename1,trfilename2,trfilename3;
		PrjInfoVO prjInfo = new PrjInfoVO();
		prjInfo.setPrj_id(update.getPrj_id());
		
		prjInfo = selectOne("prjinfoSqlMap.getPrjInfo",prjInfo);
		
		
		

		trfilename1 = UPLOAD_PATH+prjInfo.getPrj_id()+TEMPLATE_PATH+prjInfo.getPrj_nm()+"_p_tr.xls";
		trfilename2 = UPLOAD_PATH+prjInfo.getPrj_id()+TEMPLATE_PATH+prjInfo.getPrj_nm()+"_p_vtr.xls";
		trfilename3 = UPLOAD_PATH+prjInfo.getPrj_id()+TEMPLATE_PATH+prjInfo.getPrj_nm()+"_p_str.xls";
		
		InputStream inp1;
		InputStream inp2;
		InputStream inp3;
		
		try {
			inp1 = new FileInputStream(trfilename1);
			cnt++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			inp2 = new FileInputStream(trfilename2);
			cnt++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			inp3 = new FileInputStream(trfilename3);
			cnt++;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 커버 파일 1개도 존재하지 않음
		if(cnt<1) return false;
		// 커버파일 존재
		else return true;
	}


	public DrnLineVO getJobCode(DrnLineVO approval) {
		if(approval == null) return null;
		
		String desinger_id,reviewer_id,approver_id;
		
		// 각 사람별로 직급코드 검색 및 셋팅
		desinger_id = approval.getReg_id();
		if(desinger_id != null) {
			String job_code = selectOne("drnSqlMap.getJobCode",desinger_id);
			if(job_code == null) job_code = "";
			approval.setDesigner_code(job_code);
			/*
			String name = approval.getDesigned();
			name += (" " + job_code);
			name = name.trim();
			approval.setDesigned(name);
			*/
		}
		
		reviewer_id = approval.getReviewed_id();
		if(reviewer_id != null) {
			String job_code = selectOne("drnSqlMap.getJobCode",reviewer_id);
			if(job_code == null) job_code = "";
			approval.setReviewer_code(job_code);
			/*
			String name = approval.getReviewed();
			name += (" " + job_code);
			name = name.trim();
			approval.setReviewed(name);
			*/
		}
		
		approver_id = approval.getApproved_id();
		if(approver_id != null) {
			String job_code = selectOne("drnSqlMap.getJobCode",approver_id);
			if(job_code == null) job_code = "";
			approval.setApprover_code(job_code);
			/*
			String name = approval.getApproved();
			name += (" " + job_code);
			name = name.trim();
			approval.setApproved(name);
			*/
		}
		
		
		
		
		return approval;
	}


	public int clearDoc(DrnLineVO update) {
		
		// docClear
		List<DrnLineVO> prevDocs = selectList("drnSqlMap.clearDocChk",update);

		// 해당 문서들 체크아웃 해제
		for(DrnLineVO doc : prevDocs) {
			update("drnSqlMap.clearDoc_cancelCheckOut",doc);
		}
		
		int res = delete("drnSqlMap.clearDoc",update);
		
		return 1;
	}

	public int clearCheckSheet(DrnLineVO update) {
		
		// 체크시트 클리어
		int clearCheckSheetChk = selectOne("drnSqlMap.clearCheckSheetChk",update);
		int res1 = delete("drnSqlMap.clearCheckSheet",update);
		
		int clearCheckSheetItemChk = selectOne("drnSqlMap.clearCheckSheetItemChk",update);
		int res2 = delete("drnSqlMap.clearCheckSheetItem",update);
				
		return 1;
		
	}

	public int clearLocalFile(DrnLineVO update) {
		
		
		
		// String[] getExtension = file.getOriginalFilename().split("\\.");
		String prj_id = update.getPrj_id();
		String drn_id = update.getDrn_id();
		
		String sfile_nm = drn_id;
		
		// 파일서버 물리저장공간에 해당 파일이 있는지 확인 및 제거
		String path =  UPLOAD_PATH + update.getPrj_id() +PCFILE_PATH;
		File dir = new File(path);
		File[] tmp = dir.listFiles();
		List<File> fileInDir = new ArrayList<>();
		if(tmp!=null)
			fileInDir = Arrays.asList(dir.listFiles());
		// 해당 drn_id로 업로드된 파일 필터
		fileInDir = fileInDir.stream().filter(item -> (item.getName().split("\\.")[0]).equals(drn_id) ).collect(Collectors.toList());
		
		
		for(File file : fileInDir) file.delete();
		
		// DB정보도 클리어
		int res = delete("drnSqlMap.clearLocalFile",update);
		
		return 1;
		
		
	}

	public int updateDrnLine(DrnLineVO drnLineVO) {
		String action_id = drnLineVO.getAction_id();
		return update("drnSqlMap.updateDrnLine",drnLineVO);
	}


	public String getDocTypeFromDoc(DrnLineVO drnLineVO) {
		
		String path = selectOne("drnSqlMap.getDocFolderPath",drnLineVO);
		String[] get_folder_path = path.split(">");
		List<String> folder_path = Arrays.asList(get_folder_path);
		Collections.reverse(folder_path);
		
		String doc_type = null;
		for(String id : folder_path) {
			long folder_id = Long.parseLong(id);
			drnLineVO.setFolder_id(folder_id);
			doc_type = selectOne("drnSqlMap.get_DRNType",drnLineVO);
			if(doc_type != null) break;
		}
		
		
		return doc_type;
	}

	public String isAdmin(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.isAdmin",drnLineVO);
	}


	public String isDcc(DrnLineVO drnLineVO) {
		return selectOne("drnSqlMap.isDcc",drnLineVO);
	}

	public boolean drnLocalFileChk(MultipartHttpServletRequest mtRequest) throws Exception{
		boolean fileUploadChk = true;
		
		//List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("uploadfile");
		MultipartFile mf = mtRequest.getFile("drnLocalFile");
		File file = new File(mf.getOriginalFilename());
		
		String fileChk = file.getName();
		if(fileChk != null && !fileChk.equals("")) {
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
		
		}
			
			//System.out.println("파일 명 = "+file);
			//System.out.println("파일 확인 true or false = "+fileUploadChk);
		
		return fileUploadChk;
	}


	public String getDrnReviewConclusion(DrnLineVO drnLineVO) {

		return selectOne("drnSqlMap.getDrnReviewConclusion",drnLineVO);
	}
	
	
}
