package kr.co.doiloppa.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CrossScriptingFilter implements Filter {
	 
	public FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	//크로스 사이트 스크립트 필터 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		
		// 해당 url은 filter 작업하지 않음		
		if(uri.equals("/admin/a.do")){
			// 관리자 디자인 수정
			chain.doFilter(request,response);		
		}else{		
			chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
		}
	}
}