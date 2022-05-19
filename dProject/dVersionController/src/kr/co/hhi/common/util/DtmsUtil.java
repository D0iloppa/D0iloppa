package kr.co.hhi.common.util;

public class DtmsUtil {
	
	public static float getPolicyStatePercent(
			String po1_state
			,String po2_state
			,String po3_state
			,String po4_state
			,String po5_state
			,String po6_state
			,String po7_state
			,String po8_state) 
	{
		
		float	result	=	0;
		
		
		if("PF3".contentEquals(po1_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po2_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po3_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po4_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po5_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po6_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po7_state)) {
			result	=	result + 100;
		}
		if("PF3".contentEquals(po8_state)) {
			result	=	result + 100;
		}
		
		
		result	=	result / 8;
		
		return result;
	}
}
