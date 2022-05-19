package kr.co.hhi.common.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static JSONArray getRecursiveJson(JSONArray arr, String key, String parent) {
		
		for(int i = 0; i < arr.size(); i++) {
			
			JSONObject jo0 = JSONObject.fromObject(arr.get(i));
			
			for(int j = (i + 1); j < arr.size(); j++) {

				JSONObject jo1 = JSONObject.fromObject(arr.get(j));
				
				if(jo1.get(parent).equals(jo0.get(key))) {
					if(jo0.get("items") == null) {
						jo0.put("items", JSONArray.fromObject(jo1));
					}else {
						JSONArray arrItems = JSONArray.fromObject(jo0.get("items"));
						arrItems.add(jo1);
						jo0.put("items", arrItems);
					}
					arr.set(i, jo0);
					arr.remove(j);
					j -= 1;
				}else if(jo0.get("items") != null) {
					JSONArray arrItems = JSONArray.fromObject(jo0.get("items"));
					List<Object> list = setRecursiveJson(arrItems, jo1, key, parent);
					
					if((boolean) list.get(0)) {
						jo0.put("items", list.get(1));
						arr.set(i, jo0);
						arr.remove(j);
						j -= 1;
					}
				}
			}
		}
		
		return arr;
	}
	
	public static List<Object> setRecursiveJson(JSONArray arr, JSONObject jo1, String key, String parent){
		List<Object> result = new ArrayList<>();
		
		result.add(false);
		result.add(arr);
		
		for(int i = 0; i < arr.size(); i++) {
			
			JSONObject jo0 = JSONObject.fromObject(arr.get(i));
			
			if(jo1.get(parent).equals(jo0.get(key))) {
				if(jo0.get("items") == null) {
					jo0.put("items", JSONArray.fromObject(jo1));
				}else {
					JSONArray arrItems = JSONArray.fromObject(jo0.get("items"));
					arrItems.add(jo1);
					jo0.put("items", arrItems);
				}
				arr.set(i, jo0);
				
				result.set(0, true);
				result.set(1, arr);
			}else if(jo0.get("items") != null) {
				JSONArray arrItems = JSONArray.fromObject(jo0.get("items"));
				List<Object> list = setRecursiveJson(arrItems, jo1, key, parent);
				
				if((boolean) list.get(0)) {
					
					jo0.put("items", list.get(1));
					arr.set(i, jo0);
					
					result.set(0, true);
					result.set(1, arr);
				}
			}
		}
		
		return result;
	}
}
