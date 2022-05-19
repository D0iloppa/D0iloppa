package kr.co.doiloppa.service;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PasswordPolicyVO;

@Service
public class PassWordPolicyService extends AbstractService {
	
	public PasswordPolicyVO getpasswdPolicy(PasswordPolicyVO passwordpolicyVo) {
		PasswordPolicyVO getpasswdPolicy = selectOne("passwordPolicySqlMap.getpasswdPolicy", passwordpolicyVo);
		return getpasswdPolicy;
	}
	
	public void updatepasswdPolicy(PasswordPolicyVO passwordpolicyVo) {
		update("passwordPolicySqlMap.updatepasswdPolicy", passwordpolicyVo);
	}
	
	public void insertpasswdPolicy(PasswordPolicyVO passwordpolicyVo) {
		insert("passwordPolicySqlMap.insertpasswdPolicy", passwordpolicyVo);
	}
}
