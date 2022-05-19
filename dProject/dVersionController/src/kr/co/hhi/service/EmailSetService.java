package kr.co.hhi.service;

import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.PrjEmailVO;

@Service
public class EmailSetService extends AbstractService {
	public PrjEmailVO getEmailSetting() {
		PrjEmailVO getEmailSetting = selectOne("emailSetSqlMap.getEmailSetting");
		return getEmailSetting;
	}
	
	public int insertEmailSetting(PrjEmailVO prjEmailVo) {
		int insertEmailSetting = insert("emailSetSqlMap.insertEmailSetting",prjEmailVo);
		return insertEmailSetting;
	}
	
	public int updateEmailSetting(PrjEmailVO prjEmailVo) {
		int updateEmailSetting = update("emailSetSqlMap.updateEmailSetting",prjEmailVo);
		return updateEmailSetting;
	}
}
