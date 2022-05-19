package kr.co.doiloppa.common.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetSHA256 {
	public static String getHashcode(byte[] message) throws NoSuchAlgorithmException, IOException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(message);
		byte[] hashbytes = md.digest();
		StringBuilder sbuilder = new StringBuilder();
		for(int i = 0; i < hashbytes.length; i++) {
			sbuilder.append(String.format("%02x", hashbytes[i] & 0xff));
		}
		
		return sbuilder.toString();
	}
	
	public static String getHashcode(String message) throws NoSuchAlgorithmException, IOException{
		return getHashcode(message.getBytes());
	}
}
