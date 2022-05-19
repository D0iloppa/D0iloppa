package kr.co.doiloppa.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.UsersVO;

public class SessionInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	System.out.println("세션 확인중........");
    	HttpSession session = request.getSession();
    	if(session.getAttribute("login") == null) {
        	System.out.println("세션 없음");
    		response.sendRedirect(request.getContextPath()+"/login.do");
    		return false;
    	}else {
			UsersVO usersVo = (UsersVO)session.getAttribute("login");
			session.setMaxInactiveInterval(CommonConst.SET_SESSION_TIME);	//초단위 (60*10=10분)
    	}
        return true;
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println("세션 확인완료");
    	
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("로그인 상태");
		
	}
    
}