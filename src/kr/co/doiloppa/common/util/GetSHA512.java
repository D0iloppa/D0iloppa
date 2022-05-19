package kr.co.doiloppa.common.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GetSHA512 {
	public static String getHashcode(byte[] message) throws NoSuchAlgorithmException, IOException{
		
		MessageDigest	md = MessageDigest.getInstance("SHA-512");
        
        md.update(message);
        byte[] mb = md.digest();
        
        StringBuilder sbuilder = new StringBuilder();
        
        for (int i = 0; i < mb.length; i++) {
            byte temp = mb[i];
            String s = Integer.toHexString(new Byte(temp));
            while (s.length() < 2) {
                s = "0" + s;
            }
            s = s.substring(s.length() - 2);
            sbuilder.append(s);
        }
        
		return sbuilder.toString();
	}
	
	public static String getHashcode(String message) throws NoSuchAlgorithmException, IOException{
		return getHashcode(message.getBytes());
	}
	
	public static void main(String args[]){
		try {
			System.out.println(getHashcode("energyadmin1234lge"));
			System.out.println(getHashcode("monitoringmonitoring"));
			System.out.println(getHashcode("user021234lge"));
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
