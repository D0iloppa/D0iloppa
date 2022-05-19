package kr.co.doiloppa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.OrganizationUserVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.SecurityIpVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class SecurityService extends AbstractService {
	public SecurityIpVO getIpUseYN() {
		SecurityIpVO getIpUseYN = selectOne("securityIpSqlMap.getIpUseYN");
		return getIpUseYN;
	}
	public int insertIpUseYN(SecurityIpVO securityIpVo) {
		return insert("securityIpSqlMap.insertIpUseYN",securityIpVo);
	}
	public int updateIpUseYN(SecurityIpVO securityIpVo) {
		return insert("securityIpSqlMap.updateIpUseYN",securityIpVo);
	}
	public List<SecurityIpVO> getSecurityIpList() {
		List<SecurityIpVO> getSecurityIpList = selectList("securityIpSqlMap.getSecurityIpList");
		return getSecurityIpList;
	}
	
	public List<SecurityIpVO> getSecurityUser_Id_IpList(String user_id) {
		List<SecurityIpVO> getSecurityIpList = selectList("securityIpSqlMap.getSecurityUser_Id_IpList",user_id);
		return getSecurityIpList;
	}
	
	public int insertIp(SecurityIpVO securityIpVo) {
		return insert("securityIpSqlMap.insertIp",securityIpVo);
	}
	
	public int upSecurityIp(SecurityIpVO securityIpVo) {
		return update("securityIpSqlMap.upSecurityIp",securityIpVo);
	}
	
	public int deleteSecurityIp(SecurityIpVO securityIpVo) {
		return delete("securityIpSqlMap.deleteSecurityIp",securityIpVo);
	}

	
	public List<SecurityIpVO> valueMapping(List<SecurityIpVO> getSecurityIpList) {
		
		int idx = 0;
		
		for(SecurityIpVO row : getSecurityIpList) {
			String ip_lvl = row.getIp_lvl();
			String label = "";
			
			switch(ip_lvl) {
				case "P": // 개인인 경우
					// 아이디(이름) 형태
					UsersVO getUserInfo = selectOne("securityIpSqlMap.getUserInfo",row.getIp_val());
					label = String.format("%s(%s)", getUserInfo.getUser_id(),getUserInfo.getUser_kor_nm());
					break;
				case "O":
					// 조직 라벨
					Long org_id = Long.parseLong(row.getIp_val());
					String org_label = selectOne("securityIpSqlMap.getOrgLabel",org_id);
					label = org_label;
					break;
			}
			
			row.setLabel(label);
			getSecurityIpList.set(idx++, row);
		}
		
		
		return getSecurityIpList;
	}
		
	public List<Long> getOrgList(String user_id) {
		List<OrganizationVO> orgList = selectList("securityIpSqlMap.getOrgList",user_id);
		// 해당 org의 상위 id 리스트도 추가해준다.
		List<Long> result = new ArrayList<>();
		for(OrganizationVO row:orgList) {
			String org_path = row.getOrg_path();
			String[] org_split = org_path.split(">");
			for(String org_id : org_split) {
				Long _org_id = Long.parseLong(org_id);
				result.add(_org_id);
			}
		}
		
		// 중복 제거
		result = result.parallelStream().distinct().collect(Collectors.toList());

		return result;
	}

	public List<SecurityIpVO> getSecurityOrgIpList(Long org_id) {
		// 스트링 형으로 변환
		String _org_id = org_id + "";
		
		return selectList("securityIpSqlMap.getSecurityOrgIpList",_org_id);
	}
}
