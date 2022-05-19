package kr.co.doiloppa.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	
	public static Map<String, Object> getReqParamToMap(HttpServletRequest req){
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> iter = req.getParameterNames();
        while(iter.hasMoreElements()){
        	String key = iter.nextElement();
        	String value = req.getParameter(key);

        	map.put(key, value);
        }
        
        return map;
	}
}
