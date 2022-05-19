package kr.co.doiloppa.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.UsersVO;

@Service
public class SessionService extends AbstractService {
	public UsersVO loginPost(UsersVO usersVo){
		UsersVO loginPost = selectOne("sessionSqlMap.loginPost", usersVo);
		return loginPost;
	}
	public void firstLoginPost(UsersVO usersVo){
		update("sessionSqlMap.firstLoginPost", usersVo);
	}
	public UsersVO checkUserId(UsersVO usersVo){
		UsersVO checkUserId = selectOne("sessionSqlMap.checkUserId", usersVo);
		return checkUserId;
	}
	public void updateLastConPrjId(UsersVO usersVo){
		update("sessionSqlMap.updateLastConPrjId", usersVo);
	}
	public void updateUsersVO(UsersVO usersVo){
		update("sessionSqlMap.updateUsersVO", usersVo);
	}
	public UsersVO getUsersVO(UsersVO usersVo){
		UsersVO getUsersVO = selectOne("sessionSqlMap.getUsersVO", usersVo);
		return getUsersVO;
	}
	
	public UsersVO getAdminChkYN(UsersVO usersVo) {
		UsersVO getAdminChkYN = selectOne("sessionSqlMap.getAdminChkYN", usersVo);
		return getAdminChkYN;
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
