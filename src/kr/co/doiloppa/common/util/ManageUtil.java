package kr.co.doiloppa.common.util;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class ManageUtil {

	/**
	 * object to hashmap
	 * @param obj
	 * @return
	 */
	public static HashMap<String,Object> getHashMapOfObject(Object obj){
		HashMap<String,Object>	res			=	null;

		
		if(obj != null){
			ObjectMapper	m	= new ObjectMapper();
			
			res						= (HashMap<String, Object>) m.convertValue(obj, Map.class);
			
		}
		
		return res;
	}
	
}