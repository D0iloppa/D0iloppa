package kr.co.doiloppa.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.UserConnectVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class UserConnectService extends AbstractService {
	public void loginInitialization(UsersVO usersVo){
		update("userconnectSqlMap.loginInitialization", usersVo);
	}
	
	public UserConnectVO getUserConnect(UserConnectVO userConnectVo) {
		UserConnectVO getUserConnect = selectOne("userconnectSqlMap.getUserConnect",userConnectVo);
		return getUserConnect;
	}
	
	public void insertUserConnect(UserConnectVO userConnectVo) {
		insert("userconnectSqlMap.insertUserConnect",userConnectVo);
	}
	
	public void updateUserConnect(UserConnectVO userConnectVo) {
		update("userconnectSqlMap.updateUserConnect",userConnectVo);
		update("userconnectSqlMap.updateUserRemoteIp",userConnectVo);
	}
	
	public void resetDateFail(UserConnectVO userConnectVo) {
		update("userconnectSqlMap.resetDateFail",userConnectVo);
	}

	public void resetConnectFailCnt(UserConnectVO userConnectVo) {
		update("userconnectSqlMap.resetConnectFailCnt",userConnectVo);
	}
	
	public void upConnFailCnt(UserConnectVO userConnectVo) {
		update("userconnectSqlMap.upConnFailCnt",userConnectVo);
	}

	
	public String getIpAddress(HttpServletRequest request) {
		String ipAddress=request.getRemoteAddr();
		 
    	 if(ipAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")){
    	     InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
    	     ipAddress=inetAddress.getHostAddress();
    	 }
    	 return ipAddress;
	}
}
