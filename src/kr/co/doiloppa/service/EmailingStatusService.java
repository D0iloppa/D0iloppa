package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PrjEmailTypeVO;

@Service
public class EmailingStatusService extends AbstractService {
	public List<PrjEmailTypeVO> getEmailSendList(PrjEmailTypeVO prjEmailTypeVo) {
		
		String prj_id = prjEmailTypeVo.getPrj_id();
		if(prj_id.equals("all")) {
			List<PrjEmailTypeVO> getEmailSendList = selectList("emailingStatusSqlMap.getEmailSendList_all",prjEmailTypeVo);
			return getEmailSendList;
			
		}else {
			List<PrjEmailTypeVO> getEmailSendList = selectList("emailingStatusSqlMap.getEmailSendList",prjEmailTypeVo);
			return getEmailSendList;
		}
	}
	public List<PrjEmailTypeVO> getEmailReceiptList(PrjEmailTypeVO prjEmailTypeVo) {
		List<PrjEmailTypeVO> getEmailReceiptList = selectList("emailingStatusSqlMap.getEmailReceiptList",prjEmailTypeVo);
		return getEmailReceiptList;
	}
	public List<PrjEmailTypeVO> getEmailNreceiptList(PrjEmailTypeVO prjEmailTypeVo) {
		List<PrjEmailTypeVO> getEmailNreceiptList = selectList("emailingStatusSqlMap.getEmailNreceiptList",prjEmailTypeVo);
		return getEmailNreceiptList;
	}
	public List<PrjEmailTypeVO> getEmailDownList(PrjEmailTypeVO prjEmailTypeVo) {
		String prj_id = prjEmailTypeVo.getPrj_id();
		
		if(prj_id.equals("all")) {
			List<PrjEmailTypeVO> getEmailDownList = selectList("emailingStatusSqlMap.getEmailDownList_all",prjEmailTypeVo);
			return getEmailDownList;
			
		}else {
			List<PrjEmailTypeVO> getEmailDownList = selectList("emailingStatusSqlMap.getEmailDownList",prjEmailTypeVo);
			return getEmailDownList;
		}
	}
}
