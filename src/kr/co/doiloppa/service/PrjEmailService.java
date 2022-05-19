package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjEmailVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjEmailService extends AbstractService {
	public int insertPrjEmail(PrjEmailVO prjemailvo){
		int insertPrjEmail = insert("prjemailSqlMap.insertPrjEmail",prjemailvo);
		return insertPrjEmail;
	}
	public int insertPrjEmailAttachInfo(PrjEmailVO prjemailvo){
		int insertPrjEmailAttachInfo = insert("prjemailSqlMap.insertPrjEmailAttachInfo",prjemailvo);
		return insertPrjEmailAttachInfo;
	}
	public int getEmailSentTimes(PrjEmailVO prjemailvo){
		int getEmailSentTimes = selectOne("prjemailSqlMap.getEmailSentTimes",prjemailvo);
		return getEmailSentTimes;
	}
	public int getEmailSentTimesForDC(PrjEmailVO prjemailvo){
		int getEmailSentTimesForDC = selectOne("prjemailSqlMap.getEmailSentTimesForDC",prjemailvo);
		return getEmailSentTimesForDC;
	}
	public List<PrjEmailVO> getEmailSentHistory(PrjEmailVO prjemailvo){
		List<PrjEmailVO> getEmailSentHistory = selectList("prjemailSqlMap.getEmailSentHistory",prjemailvo);
		return getEmailSentHistory;
	}
	public PrjEmailVO getEmailFileDownBefore(PrjEmailVO prjemailvo){
		PrjEmailVO getEmailFileDownBefore = selectOne("prjemailSqlMap.getEmailFileDownBefore",prjemailvo);
		return getEmailFileDownBefore;
	}
	public int insertPrjEmailReceiptInfo(PrjEmailVO prjemailvo){
		int insertPrjEmailReceiptInfo = insert("prjemailSqlMap.insertPrjEmailReceiptInfo",prjemailvo);
		return insertPrjEmailReceiptInfo;
	}
	public int updatePrjEmailSendInfo(PrjEmailVO prjemailvo){
		int updatePrjEmailSendInfo = update("prjemailSqlMap.updatePrjEmailSendInfo",prjemailvo);
		return updatePrjEmailSendInfo;
	}
	public int updatePrjEmailAttachInfo(PrjEmailVO prjemailvo){
		int updatePrjEmailAttachInfo = update("prjemailSqlMap.updatePrjEmailAttachInfo",prjemailvo);
		return updatePrjEmailAttachInfo;
	}
	public int updatePrjEmailReceiptInfo(PrjEmailVO prjemailvo){
		int updatePrjEmailReceiptInfo = update("prjemailSqlMap.updatePrjEmailReceiptInfo",prjemailvo);
		return updatePrjEmailReceiptInfo;
	}
	public int insertCopyDefaultEmailType(PrjEmailTypeVO prjemailtypevo){
		int insertCopyDefaultEmailType = insert("prjemailSqlMap.insertCopyDefaultEmailType",prjemailtypevo);
		return insertCopyDefaultEmailType;
	}
	public int insertCopyDefaultEmailTypeContents(PrjEmailVO prjemailvo){
		int insertCopyDefaultEmailTypeContents = insert("prjemailSqlMap.insertCopyDefaultEmailTypeContents",prjemailvo);
		return insertCopyDefaultEmailTypeContents;
	}
	public int insertPasswdResetEmailSendInfo(PrjEmailVO prjemailvo) {
		int insertPasswdResetEmailSendInfo = insert("prjemailSqlMap.insertPasswdResetEmailSendInfo",prjemailvo);
		return insertPasswdResetEmailSendInfo;
	}

}
