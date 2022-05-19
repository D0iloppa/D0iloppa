package kr.co.hhi.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.NotificationVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.UsersVO;

@Service
public class NotificationService extends AbstractService {

    @Value("#{config['server.domain']}")
    private String SERVER;

	static List<String> receiver_id = new ArrayList<>();
	public String DelayDoc() {
		String delaydoc = "<p><b><span style=\"font-size: 14px;\">1. 제출 지연 도서 현황</span></b></p><p><b>※ FORECAST DATE는 프로젝트별 설정에 따라 자동 등록 됩니다. 일자 변경이 필요할 경우 DCC와 협의하여 수정 가능합니다.</b></p><br><br><br>";
		delaydoc += "<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>DOCUMENT NO</th><th>DOCUMENT TITLE</th><th>REV NO</th><th>STEP</th><th>FORECAST DATE</th><th>DESIGNER</th><th style=\"color:red;\">DELAY DAYS</th><th>DISCIPLINE</th></tr></thead><tbody>";
		List<NotificationVO> delayEngDoc = selectList("notificationSqlMap.delayEngDoc");
		for(int i=0;i<delayEngDoc.size();i++) {
			String rev_no = delayEngDoc.get(i).getRev_no();
			if(rev_no==null) {
				rev_no="";
			}
			delaydoc += "<tr style=\"text-align:center;\"><td>"+delayEngDoc.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+delayEngDoc.get(i).getPrj_id()+"&page=DelayDoc&folderId="+delayEngDoc.get(i).getFolder_id()+"\">"+delayEngDoc.get(i).getDoc_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+delayEngDoc.get(i).getTitle()+"</td>"
					+ "<td>"+rev_no+"</td>"
							+ "<td>"+delayEngDoc.get(i).getStep()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(delayEngDoc.get(i).getFore_date())+"</td>"
					+ "<td>"+delayEngDoc.get(i).getDesigner()+"</td>"
					+ "<td style=\"color:red;\">"+delayEngDoc.get(i).getDelay_days()+"</td>"
					+ "<td>"+delayEngDoc.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(delayEngDoc.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(delayEngDoc.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(delayEngDoc.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getEngDesigner = getEngDesigner(prjdocumentindexvo);
			for(int j=0;j<getEngDesigner.size();j++) {
				receiver_id.add(getEngDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
			List<PrjDocumentIndexVO> getTROutCreator = getTROutCreator(prjdocumentindexvo);
			for(int j=0;j<getTROutCreator.size();j++) {
				receiver_id.add(getTROutCreator.get(j).getReg_id());
			}
		}
		List<NotificationVO> delayVendorDoc = selectList("notificationSqlMap.delayVendorDoc");
		for(int i=0;i<delayVendorDoc.size();i++) {
			String rev_no = delayVendorDoc.get(i).getRev_no();
			if(rev_no==null) {
				rev_no="";
			}
			delaydoc += "<tr style=\"text-align:center;\"><td>"+delayVendorDoc.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+delayVendorDoc.get(i).getPrj_id()+"&page=DelayDoc&folderId="+delayVendorDoc.get(i).getFolder_id()+"\">"+delayVendorDoc.get(i).getDoc_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+delayVendorDoc.get(i).getTitle()+"</td>"
					+ "<td>"+rev_no+"</td>"
							+ "<td>"+delayVendorDoc.get(i).getStep()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(delayVendorDoc.get(i).getFore_date())+"</td>"
					+ "<td>"+delayVendorDoc.get(i).getDesigner()+"</td>"
					+ "<td style=\"color:red;\">"+delayVendorDoc.get(i).getDelay_days()+"</td>"
					+ "<td>"+delayVendorDoc.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(delayVendorDoc.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(delayVendorDoc.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(delayVendorDoc.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getVendorDesigner = getVendorDesigner(prjdocumentindexvo);
			for(int j=0;j<getVendorDesigner.size();j++) {
				receiver_id.add(getVendorDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
			List<PrjDocumentIndexVO> getVTROutCreator = getVTROutCreator(prjdocumentindexvo);
			for(int j=0;j<getVTROutCreator.size();j++) {
				receiver_id.add(getVTROutCreator.get(j).getReg_id());
			}
		}
		List<NotificationVO> delayProcDoc = selectList("notificationSqlMap.delayProcDoc");
		for(int i=0;i<delayProcDoc.size();i++) {
			String rev_no = delayProcDoc.get(i).getRev_no();
			if(rev_no==null) {
				rev_no="";
			}
			delaydoc += "<tr style=\"text-align:center;\"><td>"+delayProcDoc.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+delayProcDoc.get(i).getPrj_id()+"&page=DelayDoc&folderId="+delayProcDoc.get(i).getFolder_id()+"\">"+delayProcDoc.get(i).getDoc_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+delayProcDoc.get(i).getTitle()+"</td>"
					+ "<td>"+rev_no+"</td>"
							+ "<td>"+delayProcDoc.get(i).getStep()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(delayProcDoc.get(i).getFore_date())+"</td>"
					+ "<td>"+delayProcDoc.get(i).getDesigner()+"</td>"
					+ "<td style=\"color:red;\">"+delayProcDoc.get(i).getDelay_days()+"</td>"
					+ "<td>"+delayProcDoc.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(delayProcDoc.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(delayProcDoc.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(delayProcDoc.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getProcDesigner = getProcDesigner(prjdocumentindexvo);
			for(int j=0;j<getProcDesigner.size();j++) {
				receiver_id.add(getProcDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
		}
		List<NotificationVO> delaySiteDoc = selectList("notificationSqlMap.delaySiteDoc");
		for(int i=0;i<delaySiteDoc.size();i++) {
			String rev_no = delaySiteDoc.get(i).getRev_no();
			if(rev_no==null) {
				rev_no="";
			}
			delaydoc += "<tr style=\"text-align:center;\"><td>"+delaySiteDoc.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+delaySiteDoc.get(i).getPrj_id()+"&page=DelayDoc&folderId="+delaySiteDoc.get(i).getFolder_id()+"\">"+delaySiteDoc.get(i).getDoc_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+delaySiteDoc.get(i).getTitle()+"</td>"
					+ "<td>"+rev_no+"</td>"
							+ "<td>"+delaySiteDoc.get(i).getStep()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(delaySiteDoc.get(i).getFore_date())+"</td>"
					+ "<td>"+delaySiteDoc.get(i).getDesigner()+"</td>"
					+ "<td style=\"color:red;\">"+delaySiteDoc.get(i).getDelay_days()+"</td>"
					+ "<td>"+delaySiteDoc.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(delaySiteDoc.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(delaySiteDoc.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(delaySiteDoc.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getSiteDesigner = getSiteDesigner(prjdocumentindexvo);
			for(int j=0;j<getSiteDesigner.size();j++) {
				receiver_id.add(getSiteDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
			List<PrjDocumentIndexVO> getSTROutCreator = getSTROutCreator(prjdocumentindexvo);
			for(int j=0;j<getSTROutCreator.size();j++) {
				receiver_id.add(getSTROutCreator.get(j).getReg_id());
			}
		}
		delaydoc += "</tbody></table><br><br><br>";

		return delaydoc;
	}
	public String Incoming() {
		String incoming = "<p><b><span style=\\\"font-size: 14px;\\\">2. INCOMING</span></b></p> (<span style=\"color:red;\">'INSUM'</span> COUNTS)\r\n" + 
				"\r\n" + 
				"<p><b>※ 아래 리스트는 RECEIVED DATE와 상관없이 본 메일을 수신한 날짜 1근무일 전 등록된 목록이 나타나도록 설계되었습니다.</b></p>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"<p><b><span style=\\\"font-size: 14px;\\\">1) CORRESPONDENCE LETTER OR E-MAIL</span></b></p> (<span style=\"color:red;\">'INCorr'</span> COUNTS)\r\n" +
				"<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>CORRESPONDENCE NO</th><th>TITLE</th><th>FROM</th><th>TO</th><th>TYPE</th><th>RECEIVED DATE</th><th>REPLY REQ. DATE</th></tr></thead><tbody>";
		List<NotificationVO> getIncomingCorr = selectList("notificationSqlMap.getIncomingCorr");
		for(int i=0;i<getIncomingCorr.size();i++) {
			incoming += "<tr style=\"text-align:center;\"><td>"+getIncomingCorr.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getIncomingCorr.get(i).getPrj_id()+"&page=IncomingCorr&folderId="+getIncomingCorr.get(i).getFolder_id()+"\">"+getIncomingCorr.get(i).getDoc_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+getIncomingCorr.get(i).getTitle()+"</td>"
					+ "<td>"+getIncomingCorr.get(i).getSender()+"</td>"
					+ "<td>"+getIncomingCorr.get(i).getReceiver()+"</td>"
					+ "<td>"+getIncomingCorr.get(i).getDoc_type()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(getIncomingCorr.get(i).getSend_recv_date())+"</td>";
					if(getIncomingCorr.get(i).getReplyreqdate().equals("") || getIncomingCorr.get(i).getReplyreqdate()==null) {
						incoming += "<td></td></tr>";
					}else {
						incoming += "<td>"+CommonConst.stringdateTOYYYYMMDD(getIncomingCorr.get(i).getReplyreqdate())+"</td></tr>";
					}
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(getIncomingCorr.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(getIncomingCorr.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(getIncomingCorr.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
		}
		incoming += "</tbody></table><br><br><br>";
		
		incoming += "<p><b><span style=\\\"font-size: 14px;\\\">2) TRANSMITTAL</span></b></p> (<span style=\"color:red;\">'INTR'</span> COUNTS)\r\n" +
				"<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>TRANSMITTAL NO</th><th>TITLE</th><th>FROM</th><th>TO</th><th>CREATOR</th><th>RECEIVED DATE</th><th>DISCIPLINE</th></tr></thead><tbody>";
		List<NotificationVO> getIncomingTR = selectList("notificationSqlMap.getIncomingTR");
		for(int i=0;i<getIncomingTR.size();i++) {
			incoming += "<tr style=\"text-align:center;\"><td>"+getIncomingTR.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getIncomingTR.get(i).getPrj_id()+"&page=IncomingTR&folderId=0\">"+getIncomingTR.get(i).getTr_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+getIncomingTR.get(i).getTitle()+"</td>"
					+ "<td>"+getIncomingTR.get(i).getSender()+"</td>"
					+ "<td>"+getIncomingTR.get(i).getReceiver()+"</td>"
					+ "<td>"+getIncomingTR.get(i).getCreator()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(getIncomingTR.get(i).getReceived_date())+"</td>"
					+ "<td>"+getIncomingTR.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(getIncomingTR.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(getIncomingTR.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(getIncomingTR.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getEngDesigner = getEngDesigner(prjdocumentindexvo);
			for(int j=0;j<getEngDesigner.size();j++) {
				receiver_id.add(getEngDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
			List<PrjDocumentIndexVO> getTROutCreator = getTROutCreator(prjdocumentindexvo);
			for(int j=0;j<getTROutCreator.size();j++) {
				receiver_id.add(getTROutCreator.get(j).getReg_id());
			}
		}
		incoming += "</tbody></table><br><br><br>";
		
		incoming += "<p><b><span style=\\\"font-size: 14px;\\\">3) VENDOR TRANSMITTAL</span></b></p> (<span style=\"color:red;\">'INVTR'</span> COUNTS)\r\n" +
				"<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>TRANSMITTAL NO</th><th>TITLE</th><th>FROM</th><th>TO</th><th>CREATOR</th><th>RECEIVED DATE</th><th>DISCIPLINE</th></tr></thead><tbody>";
		List<NotificationVO> getIncomingVTR = selectList("notificationSqlMap.getIncomingVTR");
		for(int i=0;i<getIncomingVTR.size();i++) {
			incoming += "<tr style=\"text-align:center;\"><td>"+getIncomingVTR.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getIncomingVTR.get(i).getPrj_id()+"&page=IncomingVTR&folderId=0\">"+getIncomingVTR.get(i).getTr_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+getIncomingVTR.get(i).getTitle()+"</td>"
					+ "<td>"+getIncomingVTR.get(i).getSender()+"</td>"
					+ "<td>"+getIncomingVTR.get(i).getReceiver()+"</td>"
					+ "<td>"+getIncomingVTR.get(i).getCreator()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(getIncomingVTR.get(i).getReceived_date())+"</td>"
					+ "<td>"+getIncomingVTR.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(getIncomingVTR.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(getIncomingVTR.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(getIncomingVTR.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getVendorDesigner = getVendorDesigner(prjdocumentindexvo);
			for(int j=0;j<getVendorDesigner.size();j++) {
				receiver_id.add(getVendorDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
			List<PrjDocumentIndexVO> getVTROutCreator = getVTROutCreator(prjdocumentindexvo);
			for(int j=0;j<getVTROutCreator.size();j++) {
				receiver_id.add(getVTROutCreator.get(j).getReg_id());
			}
		}
		incoming += "</tbody></table><br><br><br>";
		
		incoming = incoming.replaceAll("'INSUM'", Integer.toString(getIncomingCorr.size()+getIncomingTR.size()+getIncomingVTR.size()))
							.replaceAll("'INCorr'", Integer.toString(getIncomingCorr.size()))
							.replaceAll("'INTR'", Integer.toString(getIncomingTR.size()))
							.replaceAll("'INVTR'", Integer.toString(getIncomingVTR.size()));
		return incoming;
	}
	public String Outgoing() {
		String outgoing = "<p><b><span style=\\\"font-size: 14px;\\\">1. OUTGOING</span></b></p> (<span style=\"color:red;\">'OUTSUM'</span> COUNTS)\r\n" + 
				"\r\n" + 
				"<p><b>※ 아래 리스트는 SEND DATE와 상관없이 본 메일을 수신한 날짜 1근무일 전 등록된 목록이 나타나도록 설계되었습니다.</b></p>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"<p><b><span style=\\\"font-size: 14px;\\\">1) CORRESPONDENCE LETTER OR E-MAIL</span></b></p> (<span style=\"color:red;\">'OUTCorr'</span> COUNTS)\r\n" +
				"<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>CORRESPONDENCE NO</th><th>TITLE</th><th>FROM</th><th>TO</th><th>TYPE</th><th>SEND DATE</th><th>REPLY DUE DATE</th></tr></thead><tbody>";
		List<NotificationVO> getOutgoingCorr = selectList("notificationSqlMap.getOutgoingCorr");
		for(int i=0;i<getOutgoingCorr.size();i++) {
			outgoing += "<tr style=\"text-align:center;\"><td>"+getOutgoingCorr.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getOutgoingCorr.get(i).getPrj_id()+"&page=OutgoingCorr&folderId="+getOutgoingCorr.get(i).getFolder_id()+"\">"+getOutgoingCorr.get(i).getDoc_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+getOutgoingCorr.get(i).getTitle()+"</td>"
					+ "<td>"+getOutgoingCorr.get(i).getSender()+"</td>"
					+ "<td>"+getOutgoingCorr.get(i).getReceiver()+"</td>"
					+ "<td>"+getOutgoingCorr.get(i).getDoc_type()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(getOutgoingCorr.get(i).getSend_recv_date())+"</td>";
					if(getOutgoingCorr.get(i).getReplyreqdate().equals("") || getOutgoingCorr.get(i).getReplyreqdate()==null) {
						outgoing += "<td></td></tr>";
					}else {
						outgoing += "<td>"+CommonConst.stringdateTOYYYYMMDD(getOutgoingCorr.get(i).getReplyreqdate())+"</td></tr>";
					}
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(getOutgoingCorr.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(getOutgoingCorr.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(getOutgoingCorr.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
		}
		outgoing += "</tbody></table><br><br><br>";
		
		outgoing += "<p><b><span style=\\\"font-size: 14px;\\\">2) TRANSMITTAL</span></b></p> (<span style=\"color:red;\">'OUTTR'</span> COUNTS)\r\n" +
				"<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>TRANSMITTAL NO</th><th>TITLE</th><th>ISSUE PURPOSE</th><th>CREATOR</th><th>SEND DATE</th><th>DISCIPLINE</th></tr></thead><tbody>";
		List<NotificationVO> getOutgoingTR = selectList("notificationSqlMap.getOutgoingTR");
		for(int i=0;i<getOutgoingTR.size();i++) {
			outgoing += "<tr style=\"text-align:center;\"><td>"+getOutgoingTR.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getOutgoingTR.get(i).getPrj_id()+"&page=OutgoingTR&folderId=0\">"+getOutgoingTR.get(i).getTr_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+getOutgoingTR.get(i).getTitle()+"</td>"
					+ "<td>"+getOutgoingTR.get(i).getIssue_purpose()+"</td>"
					+ "<td>"+getOutgoingTR.get(i).getCreator()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(getOutgoingTR.get(i).getSend_date())+"</td>"
					+ "<td>"+getOutgoingTR.get(i).getDiscipline()+"</td></tr>";
		}
		outgoing += "</tbody></table><br><br><br>";
		
		outgoing += "<p><b><span style=\\\"font-size: 14px;\\\">3) VENDOR TRANSMITTAL</span></b></p> (<span style=\"color:red;\">'OUTVTR'</span> COUNTS)\r\n" +
				"<table width=\"1200\" border=\"1\" class=\"table table-bordered\"><thead style=\"background-color:#eeeeee;\"><tr><th>PROJECT NAME</th><th>TRANSMITTAL NO</th><th>TITLE</th><th>ISSUE PURPOSE</th><th>CREATOR</th><th>SEND DATE</th><th>DISCIPLINE</th></tr></thead><tbody>";
		List<NotificationVO> getOutgoingVTR = selectList("notificationSqlMap.getOutgoingVTR");
		for(int i=0;i<getOutgoingVTR.size();i++) {
			outgoing += "<tr style=\"text-align:center;\"><td>"+getOutgoingVTR.get(i).getPrj_nm()+"</td>"
					+ "<td><a style=\"text-decoration:underline;color:blue;\" href=\""+SERVER+"Notification.do?prj_id="+getOutgoingVTR.get(i).getPrj_id()+"&page=OutgoingVTR&folderId=0\">"+getOutgoingVTR.get(i).getTr_no()+"</a></td>"
					+ "<td style=\"text-align:left;\">"+getOutgoingVTR.get(i).getTitle()+"</td>"
					+ "<td>"+getOutgoingVTR.get(i).getIssue_purpose()+"</td>"
					+ "<td>"+getOutgoingVTR.get(i).getCreator()+"</td>"
					+ "<td>"+CommonConst.stringdateTOYYYYMMDD(getOutgoingVTR.get(i).getSend_date())+"</td>"
					+ "<td>"+getOutgoingVTR.get(i).getDiscipline()+"</td></tr>";
			PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
			prjdocumentindexvo.setPrj_id(getOutgoingVTR.get(i).getPrj_id());
			prjdocumentindexvo.setDoc_id(getOutgoingVTR.get(i).getDoc_id());
			prjdocumentindexvo.setRev_id(getOutgoingVTR.get(i).getRev_id());
			List<PrjDocumentIndexVO> getPrjDCC = getPrjDCC(prjdocumentindexvo);
			for(int j=0;j<getPrjDCC.size();j++) {
				receiver_id.add(getPrjDCC.get(j).getUser_id());
			}
			List<PrjDocumentIndexVO> getVendorDesigner = getVendorDesigner(prjdocumentindexvo);
			for(int j=0;j<getVendorDesigner.size();j++) {
				receiver_id.add(getVendorDesigner.get(j).getDesigner_id());
			}
			List<PrjDocumentIndexVO> getDocCreator = getDocCreator(prjdocumentindexvo);
			for(int j=0;j<getDocCreator.size();j++) {
				receiver_id.add(getDocCreator.get(j).getReg_id());
			}
			List<PrjDocumentIndexVO> getVTROutCreator = getVTROutCreator(prjdocumentindexvo);
			for(int j=0;j<getVTROutCreator.size();j++) {
				receiver_id.add(getVTROutCreator.get(j).getReg_id());
			}
		}
		outgoing += "</tbody></table><br><br><br>";
		
		outgoing = outgoing.replaceAll("'OUTSUM'", Integer.toString(getOutgoingCorr.size()+getOutgoingTR.size()+getOutgoingVTR.size()))
							.replaceAll("'OUTCorr'", Integer.toString(getOutgoingCorr.size()))
							.replaceAll("'OUTTR'", Integer.toString(getOutgoingTR.size()))
							.replaceAll("'OUTVTR'", Integer.toString(getOutgoingVTR.size()));
		return outgoing;
	}
	
	public List<PrjDocumentIndexVO> getPrjDCC(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getPrjDCC = selectList("notificationSqlMap.getPrjDCC", prjdocumentindexvo);
		return getPrjDCC;
	}
	public List<PrjDocumentIndexVO> getEngDesigner(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getEngDesigner = selectList("notificationSqlMap.getEngDesigner", prjdocumentindexvo);
		return getEngDesigner;
	}
	public List<PrjDocumentIndexVO> getVendorDesigner(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getVendorDesigner = selectList("notificationSqlMap.getVendorDesigner", prjdocumentindexvo);
		return getVendorDesigner;
	}
	public List<PrjDocumentIndexVO> getProcDesigner(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getProcDesigner = selectList("notificationSqlMap.getProcDesigner", prjdocumentindexvo);
		return getProcDesigner;
	}
	public List<PrjDocumentIndexVO> getSiteDesigner(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getSiteDesigner = selectList("notificationSqlMap.getSiteDesigner", prjdocumentindexvo);
		return getSiteDesigner;
	}
	public List<PrjDocumentIndexVO> getDocCreator(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getDocCreator = selectList("notificationSqlMap.getDocCreator", prjdocumentindexvo);
		return getDocCreator;
	}
	public List<PrjDocumentIndexVO> getTROutCreator(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getTROutCreator = selectList("notificationSqlMap.getTROutCreator", prjdocumentindexvo);
		return getTROutCreator;
	}
	public List<PrjDocumentIndexVO> getVTROutCreator(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getVTROutCreator = selectList("notificationSqlMap.getVTROutCreator", prjdocumentindexvo);
		return getVTROutCreator;
	}
	public List<PrjDocumentIndexVO> getSTROutCreator(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getSTROutCreator = selectList("notificationSqlMap.getSTROutCreator", prjdocumentindexvo);
		return getSTROutCreator;
	}
	
	public static List<String> getReceiverId() {
		//중복된 ID 제거
		Set<String> set = new HashSet<String>(receiver_id);
		List<String> receivers = new ArrayList<String>(set);
		return receivers;
	}
	public String getUserEmailAddr(PrjDocumentIndexVO prjdocumentindexvo){
		String getUserEmailAddr = selectOne("notificationSqlMap.getUserEmailAddr", prjdocumentindexvo);
		return getUserEmailAddr;
	}
	public int insertDailyNotificationEmailSendInfo(PrjEmailVO prjemailvo){
		int insertDailyNotificationEmailSendInfo = insert("notificationSqlMap.insertDailyNotificationEmailSendInfo", prjemailvo);
		return insertDailyNotificationEmailSendInfo;
	}
	public String getEmailSettingReceiver() {
		String getEmailSettingReceiver = selectOne("notificationSqlMap.getEmailSettingReceiver");
		return getEmailSettingReceiver;
	}
	
	
	

	public int isSentCorrEmail(PrjDocumentIndexVO prjdocumentindexvo){
		int isSentCorrEmail = selectOne("notificationSqlMap.isSentCorrEmail",prjdocumentindexvo);
		return isSentCorrEmail;
	}
	public List<PrjDocumentIndexVO> getCorrReplyRequest(PrjDocumentIndexVO prjdocumentindexvo){
		List<PrjDocumentIndexVO> getCorrReplyReqest = selectList("notificationSqlMap.getCorrReplyRequest",prjdocumentindexvo);
		return getCorrReplyReqest;
	}
	public PrjDocumentIndexVO getCorrInfo(PrjDocumentIndexVO prjdocumentindexvo){
		PrjDocumentIndexVO getCorrInfo = selectOne("notificationSqlMap.getCorrInfo",prjdocumentindexvo);
		return getCorrInfo;
	}
	public int insertCorrRequestNotificationEmailSendInfo(PrjEmailVO prjemailvo){
		int insertCorrRequestNotificationEmailSendInfo = insert("notificationSqlMap.insertCorrRequestNotificationEmailSendInfo", prjemailvo);
		return insertCorrRequestNotificationEmailSendInfo;
	}
	
}
