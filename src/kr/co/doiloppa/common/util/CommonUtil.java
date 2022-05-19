package kr.co.doiloppa.common.util;

import java.util.UUID;

public class CommonUtil {
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
