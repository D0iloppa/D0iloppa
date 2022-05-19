package kr.co.hhi.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {
	
    /**
     * ���Ͱ��� <BR> �ױ׷� ��ȯ�ϴ� �޼ҵ� <br>
     * : �Խ����� ���� ��ȸ�� <pre> �ױ׸� ������� �ʾ����� �������� �ذ�<br>
     *
     * 
     */
    public static String htmlBR(String comment){
        if(comment == null || "".equals(comment)) return "&nbsp;";
        
    	int length = comment.length();
    	StringBuffer buffer = new StringBuffer();
    	
    	for (int i = 0; i < length; ++i)
    	{
    		String comp = comment.substring(i, i+1);
    		// ���� �����͸� ���̱׷��̼� �� ������ ���Ͱ��� �˻��ϱ� ���� �����Ͽ���.
    		// cgi �� �����͸� ������ : \n
    		// �ڹٷ� �����͸� ������ :\r\n
    		if ("\r".compareTo(comp) == 0 || "\n".compareTo(comp) == 0)
    		{
    		    if("\r".compareTo(comp) == 0){
    			comp = comment.substring(++i, i+1);
    			if ("\n".compareTo(comp) == 0)
                            buffer.append("</BR>\r");
                    }else{
                        buffer.append("</BR>\r");
                    }
    		}
                buffer.append(comp);
    	}
    	return buffer.toString();
    }
    
	public static String clearXSSMinimum(String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}
		
		String returnValue = value;

		returnValue = returnValue.replaceAll("&", "&amp;");
		returnValue = returnValue.replaceAll("<", "&lt;");
		returnValue = returnValue.replaceAll(">", "&gt;");
		returnValue = returnValue.replaceAll("\"", "&#34;");
		returnValue = returnValue.replaceAll("\'", "&#39;");
		return returnValue;
	}

    public static String retagOff(String s) {
    	String res	=	"";
    	
    	if( (s != null) && (!"null".equals(s)) ){
    		res	=	s;
    		
    		res	=	res.replaceAll("&amp;", "&");
    		res	=	res.replaceAll("&gt;", ">");
    		res	=	res.replaceAll("&lt;", "<");
    		res	=	res.replaceAll("&#39;", "'");
    		res	=	res.replaceAll("&quot;", "\"");
    	}
        return res;
    }
    
    /**
     * Html�ױ׸� ���ְ� ���ڿ��� �����Ѵ�
     * @param _str
     * @return
     */
    	public static String cutHtmltag(String _str){
    		
    		String res	=	"";
    		
    		if(_str != null && !"".equals(_str)){
                //Regex regHTML = new Regex("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>");
                //Regex regBR = new Regex("<[bB][rR](\\s)?(/)?>", RegexOptions.IgnoreCase);
                
    			Pattern p = Pattern.compile("\\<(\\/?)(\\w+)*([^<>]*)>");
    			//Pattern p = Pattern.compile("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>");
    			Matcher m = p.matcher(_str);
    			res = m.replaceAll("");
    			
    			//res	=	res.replaceAll(" " ,"").replaceAll("\n\r","").replaceAll("\n","").replaceAll("\r","");
    			res	=	res.replaceAll("\n\r","").replaceAll("\n","").replaceAll("\r","");
    		}
    		

    		
    		return res;
    		
    	}
    	
	public static String clearXSSMaximum(String value) {
		String returnValue = value;
		returnValue = clearXSSMinimum(returnValue);

		returnValue = returnValue.replaceAll("%00", null);

		returnValue = returnValue.replaceAll("%", "&#37;");

		// \\. => .

		returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
		returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\
		returnValue = returnValue.replaceAll("\\./", ""); // ./
		returnValue = returnValue.replaceAll("%2F", "");

		return returnValue;
	}

	public static String filePathBlackList(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
		returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\

		return returnValue;
	}

	/**
	 * ��Ⱥ� ��������� ���� ��ġ ���.
	 *
	 * @param value
	 * @return
	 */
	public static String filePathReplaceAll(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("/", "");
		returnValue = returnValue.replaceAll("\\", "");
		returnValue = returnValue.replaceAll("\\.\\.", ""); // ..
		returnValue = returnValue.replaceAll("&", "");

		return returnValue;
	}

	public static String filePathWhiteList(String value) {
		return value; // TODO
	}
	
	 public static boolean isIPAddress(String str) {
		Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		
		return ipPattern.matcher(str).matches();
    }
	 
	 public static String removeCRLF(String parameter) {
		 return parameter.replaceAll("\r", "").replaceAll("\n", "");
	 }
	 
	 public static String removeSQLInjectionRisk(String parameter) {
		 return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("%", "").replaceAll(";", "").replaceAll("-", "").replaceAll("\\+", "").replaceAll(",", "");
	 }
	 
	 public static String removeOSCmdRisk(String parameter) {
		 return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("|", "").replaceAll(";", "");
	 }
	 
	 /**
	  * host url ������ ����
	  * ex http://www.test.com/aaa/ddd.jsp �ϰ�� => http://www.test.com �� ��ȯ
	  * @param request
	  * @return
	  */
	 public static String getHostUrl(HttpServletRequest request){
		 
		 String res	=	"";
		 StringBuffer temp	=	new StringBuffer();
		 
		 temp	=	request.getRequestURL(); 
		 
		 if( temp!=null && !"".equals(temp.toString())){
			 res	=	temp.toString();
			 
			// 'Http://' ������ '/' �� ��ġ��ã�ƾ� �ϹǷ� 8
			 res	=	res.substring(0, res.indexOf("/", 8));
		 }
		 
		 return res;
	 }
	 

}
