package kr.co.doiloppa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.DrnLineVO;
import kr.co.doiloppa.model.NotificationVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.ProjectBbsVO;
import kr.co.doiloppa.model.ProjectNoticeVO;
import kr.co.doiloppa.model.PubBbsContentsVO;

@Service
public class HomeService extends AbstractService {
	
	public List<ProjectNoticeVO> getBbsContents(ProjectNoticeVO projectnoticeVo){
		List<ProjectNoticeVO> getBbsContents = selectList("homeSqlMap.getBBS", projectnoticeVo);
		return getBbsContents;
	}
	
//	public ProjectBbsVO getHomeContDetail(ProjectBbsVO projectBbsVo) {
//		ProjectBbsVO getHomeContDetail = selectOne("homeSqlMap.getHomeContDetail", projectBbsVo);
//		return getHomeContDetail;
//	}
	
	public void hitcntUp(ProjectBbsVO projectBbsVo) {
		update("homeSqlMap.hitcntUp", projectBbsVo);
	}
	
	public List<ProjectBbsVO> getBbsContentsHitup(ProjectBbsVO projectBbsVo){
		List<ProjectBbsVO> getBbsContents = selectList("homeSqlMap.getBBSHitup", projectBbsVo);
		return getBbsContents;
	}


	public List< List<NotificationVO> > getDelayDocs() {
		List< List<NotificationVO> > result = new ArrayList<>();
		List<NotificationVO> delayEngDoc = selectList("notificationSqlMap.todoEng");
		List<NotificationVO> delayVendorDoc = selectList("notificationSqlMap.todoVendor");
		
		result.add(delayEngDoc);
		result.add(delayVendorDoc);

		return result;
	}

	public List<DrnLineVO> getDrnSummary(String prj_id) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date(); 
		String reg_date = sdf.format(now);
		
		DrnLineVO getDataBean = new DrnLineVO();
		
		getDataBean.setPrj_id(prj_id);
		getDataBean.setReg_date(reg_date);
		
		
		// 1) 정보를 구함
		List<DrnLineVO> result = selectList("homeSqlMap.getDrnsummary",getDataBean);
		
		/*
		result = result.stream()
				.filter(i->!i.getDrn_approval_status().equals("T"))
				.filter(i->!i.getDrn_approval_status().equals("R"))
				.filter(i->!i.getDrn_approval_status().equals("C"))
				.collect(Collectors.toList());
		*/
		
		/*
		result = result.stream().filter(i->!i.getDrn_approval_status().equals("T")).collect(Collectors.toList());
		result = result.stream().filter(i->!i.getDrn_approval_status().equals("R")).collect(Collectors.toList());
		result = result.stream().filter(i->!i.getDrn_approval_status().equals("C")).collect(Collectors.toList());
		*/
		// reg_id 는 도서의 디자이너가 아닌, drn기안자를 의미
		// 도서에 대한 디자이너를 찾아야 함
		int idx = 0;
		for(DrnLineVO item :result) {
			
			String as = item.getDrn_approval_status();
			DrnLineVO statusUserInfo = null;
			HashMap<String,String> drnStatus = new HashMap<>();
			String drn_status_str = null;
			
			switch(as) {
				case "T": // 임시저장
					statusUserInfo = selectOne("homeSqlMap.statusUserInfo",item.getReg_id());
					drn_status_str = "임시저장";
					break;
				case "A": // 검토중
					String dcc_id = selectOne("homeSqlMap.getDccUser",prj_id);
					statusUserInfo = selectOne("homeSqlMap.statusUserInfo",dcc_id);
					drn_status_str = "DCC 검토중";
					break;
				case "E": // 결재중
					statusUserInfo = selectOne("homeSqlMap.statusUserInfo",item.getReviewed_id());
					drn_status_str = "REVIEW 결재중";
					break;
				case "1": // 결재중
					statusUserInfo = selectOne("homeSqlMap.statusUserInfo",item.getApproved_id());
					drn_status_str = "APPROVE 결재중";
					break;
				case "C": // 결재완료
					statusUserInfo = selectOne("homeSqlMap.statusUserInfo",item.getApproved_id());
					drn_status_str = "결재완료";
					break;
				case "R": // 반려됨
					String reject_id = selectOne("homeSqlMap.getRejectId",item);
					statusUserInfo = selectOne("homeSqlMap.statusUserInfo",reject_id);
					drn_status_str = "반려";
					break;
				default:
					continue;
			}
			
			
			
			// 도서의 설계자
			DrnLineVO designer = selectOne("homeSqlMap.getDesigner",item);
			// drn 기안자 
			//DrnLineVO designer = selectOne("homeSqlMap.getDrnDesigner",item);
			
			
			if(designer != null) {
				item.setDesigned_id(designer.getDesigned_id());
				item.setDesigned(designer.getUser_kor_nm());
				item.setType(designer.getType());
			}
			
			
			if(statusUserInfo != null) {
				drnStatus.put("name", statusUserInfo.getUser_kor_nm()); // 이름
				drnStatus.put("code", statusUserInfo.getType()); // 직책
			}
			
			if(drn_status_str != null)
				drnStatus.put("status", drn_status_str); // 상태
			
			item.setDrnStatus(drnStatus);

			result.set(idx++, item);

		}
		
		
		return result;
		
	}

		/**
		 * 1. 메소드명 : getTRsummary
		 * 2. 작성일: 2022. 3. 2.
		 * 3. 작성자: doil
		 * 4. 설명: 
		 * 5. 수정일: doil
		 */
	public HashMap<String, HashMap<String, Integer>> getTRsummary(NotificationVO getDataVO) {
		
		HashMap<String,HashMap<String,Integer>> tr = new HashMap<>();
		
		
		Calendar cal = Calendar.getInstance();
	    // 한달전
		cal.add(Calendar.MONTH , -1);
	    String beforeMonth = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    // 복원 및 일주일 전
	    cal.add(Calendar.MONTH , 1);
	    cal.add(Calendar.DATE, -7);
	    String beforeWeek = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    // 복원 및 하루 전
	    cal.add(Calendar.DATE, 7);
	    cal.add(Calendar.DATE, -1);
	    String yesterDay = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	    cal.add(Calendar.DATE, 1);
	    String today = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		
		
		HashMap<String,Integer> trOut = new HashMap<>();
		HashMap<String,Integer> trIn = new HashMap<>();
		HashMap<String,Integer> vtrOut = new HashMap<>();
		HashMap<String,Integer> vtrIn = new HashMap<>();
		HashMap<String,Integer> strOut = new HashMap<>();
		HashMap<String,Integer> strIn = new HashMap<>();
		
		// 한달전
		getDataVO.setReg_date(beforeMonth);
		trOut.put("oneMonth",selectOne("homeSqlMap.getOutgoingTR",getDataVO));
		trIn.put("oneMonth",selectOne("homeSqlMap.getIncomingTR",getDataVO));
		vtrOut.put("oneMonth",selectOne("homeSqlMap.getOutgoingVTR",getDataVO));
		vtrIn.put("oneMonth",selectOne("homeSqlMap.getIncomingVTR",getDataVO));
		strOut.put("oneMonth",selectOne("homeSqlMap.getOutgoingSTR",getDataVO));
		strIn.put("oneMonth",selectOne("homeSqlMap.getIncomingSTR",getDataVO));
		
		getDataVO.setReg_date(beforeWeek);
		trOut.put("oneWeek",selectOne("homeSqlMap.getOutgoingTR",getDataVO));
		trIn.put("oneWeek",selectOne("homeSqlMap.getIncomingTR",getDataVO));
		vtrOut.put("oneWeek",selectOne("homeSqlMap.getOutgoingVTR",getDataVO));
		vtrIn.put("oneWeek",selectOne("homeSqlMap.getIncomingVTR",getDataVO));
		strOut.put("oneWeek",selectOne("homeSqlMap.getOutgoingSTR",getDataVO));
		strIn.put("oneWeek",selectOne("homeSqlMap.getIncomingSTR",getDataVO));
		
		getDataVO.setReg_date(yesterDay);
		trOut.put("yesterday",selectOne("homeSqlMap.getOutgoingTR",getDataVO));
		trIn.put("yesterday",selectOne("homeSqlMap.getIncomingTR",getDataVO));
		vtrOut.put("yesterday",selectOne("homeSqlMap.getOutgoingVTR",getDataVO));
		vtrIn.put("yesterday",selectOne("homeSqlMap.getIncomingVTR",getDataVO));
		strOut.put("yesterday",selectOne("homeSqlMap.getOutgoingSTR",getDataVO));
		strIn.put("yesterday",selectOne("homeSqlMap.getIncomingSTR",getDataVO));
		
		getDataVO.setReg_date(today);
		trOut.put("today",selectOne("homeSqlMap.getOutgoingTR",getDataVO));
		trIn.put("today",selectOne("homeSqlMap.getIncomingTR",getDataVO));
		vtrOut.put("today",selectOne("homeSqlMap.getOutgoingVTR",getDataVO));
		vtrIn.put("today",selectOne("homeSqlMap.getIncomingVTR",getDataVO));
		strOut.put("today",selectOne("homeSqlMap.getOutgoingSTR",getDataVO));
		strIn.put("today",selectOne("homeSqlMap.getIncomingSTR",getDataVO));
		

		tr.put("TR-OUT", trOut);
		tr.put("TR-IN", trIn);
		tr.put("VTR-OUT", vtrOut);
		tr.put("VTR-IN", vtrIn);
		tr.put("STR-OUT", strOut);
		tr.put("STR-IN", strIn);
		
		return tr;
	}


		public List<List<NotificationVO>> getTodoList(NotificationVO getDataVO) {
			List<List<NotificationVO>> result = new ArrayList<>();
			
			List<NotificationVO> engList = selectList("homeSqlMap.getEngTodoList",getDataVO);
			//engList = engList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) <= 7).collect(Collectors.toList());
			//engList = engList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) >= -7).collect(Collectors.toList());
			
			List<NotificationVO> vendorList = selectList("homeSqlMap.getVdrTodoList",getDataVO);
			//vendorList = vendorList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) <= 7).collect(Collectors.toList());
			vendorList = vendorList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) >= -7).collect(Collectors.toList());
			
			List<NotificationVO> siteList = selectList("homeSqlMap.getSdcTodoList",getDataVO);
			//siteList = siteList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) <= 7).collect(Collectors.toList());
			siteList = siteList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) >= -7).collect(Collectors.toList());
			
			List<NotificationVO> procumentList = selectList("homeSqlMap.getProTodoList",getDataVO);
			//procumentList = procumentList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) <= 7).collect(Collectors.toList());
			procumentList = procumentList.stream().filter(item -> Integer.parseInt(item.getDelay_days()) >= -7).collect(Collectors.toList());
			
			result.add(engList);
			result.add(vendorList);
			result.add(siteList);
			result.add(procumentList);
			
			return result;
		}
		
		public HashMap<String, HashMap<String, Integer>> getCorrSummaryList(NotificationVO getDataVO) {
			
			HashMap<String,HashMap<String,Integer>> corr = new HashMap<>();
			
			
			// 리스트업에 필요한 날짜 기준점 생성
			Calendar cal = Calendar.getInstance();
			
			// 한달 전
			cal.add(Calendar.MONTH , -1);
		    String beforeMonth = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    // 복원 및 일주일 전
		    cal.add(Calendar.MONTH , 1);
		    cal.add(Calendar.DATE, -7);
		    String beforeWeek = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    // 복원 및 하루 전
		    cal.add(Calendar.DATE, 7);
		    cal.add(Calendar.DATE, -1);
		    String yesterDay = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    cal.add(Calendar.DATE, 1);
		    String today = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    
		    
		    
		    getDataVO.setReg_date(beforeMonth);
		    getDataVO.setIn_out("O");
			List<NotificationVO> oneMonth_Out = selectList("homeSqlMap.getCorrList",getDataVO);
			List<NotificationVO> letter_Out = oneMonth_Out.stream().filter(i->i.getProcess_type().equals("LETTER")).collect(Collectors.toList());
			List<NotificationVO> email_Out = oneMonth_Out.stream().filter(i->i.getProcess_type().equals("E-MAIL")).collect(Collectors.toList());
			
			
			
			getDataVO.setIn_out("I");
			List<NotificationVO> oneMonth_In = selectList("homeSqlMap.getCorrList",getDataVO);
			List<NotificationVO> letter_In = oneMonth_In.stream().filter(i->i.getProcess_type().equals("LETTER")).collect(Collectors.toList());
			List<NotificationVO> email_In = oneMonth_In.stream().filter(i->i.getProcess_type().equals("E-MAIL")).collect(Collectors.toList());
		
			

			HashMap<String,Integer> l_corrOut = new HashMap<>();

			l_corrOut.put("oneMonth",letter_Out.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeMonth)).collect(Collectors.toList()).size());
			l_corrOut.put("oneWeek",letter_Out.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeWeek)).collect(Collectors.toList()).size());
			l_corrOut.put("today",letter_Out.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(today)).collect(Collectors.toList()).size());
			l_corrOut.put("delay",letter_Out.stream().filter(i->i.getReplyreqdate() != null)
						.filter(i->!i.getReplyreqdate().equals("")).filter(i-> Integer.parseInt(i.getReplyreqdate()) < Integer.parseInt(today) ).collect(Collectors.toList()).size());
			
			HashMap<String,Integer> l_corrIn = new HashMap<>();
			l_corrIn.put("oneMonth",letter_In.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeMonth)).collect(Collectors.toList()).size());
			l_corrIn.put("oneWeek",letter_In.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeWeek)).collect(Collectors.toList()).size());
			l_corrIn.put("today",letter_In.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(today)).collect(Collectors.toList()).size());
			l_corrIn.put("delay",letter_In.stream().filter(i->i.getReplyreqdate() != null)
						.filter(i->!i.getReplyreqdate().equals("")).filter(i-> Integer.parseInt(i.getReplyreqdate()) < Integer.parseInt(today) ).collect(Collectors.toList()).size());
			
			HashMap<String,Integer> e_corrOut = new HashMap<>();
			e_corrOut.put("oneMonth",email_Out.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeMonth)).collect(Collectors.toList()).size());
			e_corrOut.put("oneWeek",email_Out.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeWeek)).collect(Collectors.toList()).size());
			e_corrOut.put("today",email_Out.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(today)).collect(Collectors.toList()).size());
			e_corrOut.put("delay",email_Out.stream().filter(i->i.getReplyreqdate() != null)
						.filter(i->!i.getReplyreqdate().equals("")).filter(i-> Integer.parseInt(i.getReplyreqdate()) < Integer.parseInt(today) ).collect(Collectors.toList()).size());
			
			HashMap<String,Integer> e_corrIn = new HashMap<>();
			e_corrIn.put("oneMonth",email_In.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeMonth)).collect(Collectors.toList()).size());
			e_corrIn.put("oneWeek",email_In.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(beforeWeek)).collect(Collectors.toList()).size());
			e_corrIn.put("today",email_In.stream().filter(i-> Integer.parseInt(i.getReg_date().substring(0,8)) >= Integer.parseInt(today)).collect(Collectors.toList()).size());
			e_corrIn.put("delay",email_In.stream().filter(i->i.getReplyreqdate() != null)
						.filter(i->!i.getReplyreqdate().equals("")).filter(i-> Integer.parseInt(i.getReplyreqdate()) < Integer.parseInt(today) ).collect(Collectors.toList()).size());
			
			
			
			
			/*
			getDataVO.setReg_date(today);
			getDataVO.setIn_out("O");
			getDataVO.setIn_out("I");
			 */
			
			corr.put("L_OUT", l_corrOut);
			corr.put("E_OUT", e_corrOut);
			corr.put("L_IN", l_corrIn);
			corr.put("E_IN", e_corrIn);
			
			return corr;
		}


		public HashMap<String, HashMap<String, Integer>> getCorrSummary(NotificationVO getDataVO) {
			HashMap<String,HashMap<String,Integer>> corr = new HashMap<>();
			
			
			Calendar cal = Calendar.getInstance();
		    // 한달전
			cal.add(Calendar.MONTH , -1);
		    String beforeMonth = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    // 복원 및 일주일 전
		    cal.add(Calendar.MONTH , 1);
		    cal.add(Calendar.DATE, -7);
		    String beforeWeek = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    // 복원 및 하루 전
		    cal.add(Calendar.DATE, 7);
		    cal.add(Calendar.DATE, -1);
		    String yesterDay = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		    cal.add(Calendar.DATE, 1);
		    String today = new java.text.SimpleDateFormat("yyyyMMdd").format(cal.getTime());
			
			
			HashMap<String,Integer> corrOut = new HashMap<>();
			HashMap<String,Integer> corrIn = new HashMap<>();
			
			// 한달전
			getDataVO.setReg_date(beforeMonth);
			getDataVO.setIn_out("O");
			corrOut.put("oneMonth",selectOne("homeSqlMap.getCorrStatus",getDataVO));
			List<NotificationVO> oneMonth_Out = selectOne("homeSqlMap.getCorrList",getDataVO);			
			
			
			getDataVO.setIn_out("I");
			corrIn.put("oneMonth",selectOne("homeSqlMap.getCorrStatus",getDataVO));
			List<NotificationVO> oneMonth_In = selectOne("homeSqlMap.getCorrList",getDataVO);
			
			getDataVO.setReg_date(beforeWeek);
			getDataVO.setIn_out("O");
			corrOut.put("oneWeek",selectOne("homeSqlMap.getCorrStatus",getDataVO));
			List<NotificationVO> oneWeek_Out = selectOne("homeSqlMap.getCorrList",getDataVO);
			
			
			getDataVO.setIn_out("I");
			corrIn.put("oneWeek",selectOne("homeSqlMap.getCorrStatus",getDataVO));
			List<NotificationVO> oneWeek_In = selectOne("homeSqlMap.getCorrList",getDataVO);
			
			getDataVO.setReg_date(today);
			getDataVO.setIn_out("O");
			corrOut.put("today",selectOne("homeSqlMap.getCorrStatus",getDataVO));
			List<NotificationVO> toDay_Out = selectOne("homeSqlMap.getCorrList",getDataVO);
			corrOut.put("delay",selectOne("homeSqlMap.getCrDelay",getDataVO));
			getDataVO.setIn_out("I");
			List<NotificationVO> toDay_In = selectOne("homeSqlMap.getCorrList",getDataVO);
			corrIn.put("today",selectOne("homeSqlMap.getCorrStatus",getDataVO));
			corrIn.put("delay",selectOne("homeSqlMap.getCrDelay",getDataVO));
			
			
			

			corr.put("OUT", corrOut);
			corr.put("IN", corrIn);
			
			return corr;
		}


		public List<PrjDocumentIndexVO> getCrDelayList(NotificationVO getDataVO) {
			return selectList("homeSqlMap.getCrDelayList",getDataVO);
		}

		
		public String getUserType(String login_id) {
			return selectOne("homeSqlMap.getUserType",login_id);
		}
}
