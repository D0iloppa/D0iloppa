package kr.co.hhi.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtil {
    /**
     * �� ���ڿ� <code>""</code>.
     */
    public static final String EMPTY = "";

    /**
     * <p>Padding�� �� �� �ִ� �ִ� ��ġ</p>
     */
    // private static final int PAD_LIMIT = 8192;

    /**
     * <p>An array of <code>String</code>s used for padding.</p>
     * <p>Used for efficient space padding. The length of each String expands as needed.</p>
     */
    /*
	private static final String[] PADDING = new String[Character.MAX_VALUE];

	static {
		// space padding is most common, start with 64 chars
		PADDING[32] = "                                                                ";
	}
     */

    /**
     * ���ڿ��� ������ ���̸� �ʰ������� �����ѱ��̿��ٰ� �ش� ���ڿ��� �ٿ��ִ� �޼���.
     * @param source �� ���ڿ� �迭
     * @param output ���ҹ��ڿ�
     * @param slength ��������
     * @return �������̷� �߶� ���Һ��ڿ� ��ģ ���ڿ�
     */
    public static String cutString(String source, String output, int slength) {
        String returnVal = null;
        if (source != null) {
            if (source.length() > slength) {
                returnVal = source.substring(0, slength) + output;
            } else
                returnVal = source;
        }
        return returnVal;
    }

    /**
     * ���ڿ��� ������ ���̸� �ʰ������� �ش� ���ڿ��� �����ϴ� �޼���
     * @param source �� ���ڿ� �迭
     * @param slength ��������
     * @return �������̷� �߶� ���Һ��ڿ� ��ģ ���ڿ�
     */
    public static String cutString(String source, int slength) {
        String result = null;
        if (source != null) {
            if (source.length() > slength) {
                result = source.substring(0, slength);
            } else
                result = source;
        }
        return result;
    }

    /**
     * String Empty 확인(null, "", "null", 길이=0)
     *
     * <pre>
     *  StringUtil.isEmpty(null)      = true
     *  StringUtil.isEmpty("")        = true
     *  StringUtil.isEmpty(" ")       = false
     *  StringUtil.isEmpty("bob")     = false
     *  StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str - String
     * @return <code>true</code> - 비었으면 true
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str)  || "null".equals(str) || str.length() == 0;
    }


    /**
     * <p>���� ���ڿ��� ���Ե� ��� ��� ����(char)�� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.remove(null, *)       = null
     * StringUtil.remove("", *)         = ""
     * StringUtil.remove("queued", 'u') = "qeed"
     * StringUtil.remove("queued", 'z') = "queued"
     * </pre>
     *
     * @param str  �Է¹޴� ���� ���ڿ�
     * @param remove  �Է¹޴� ���ڿ����� ������ ��� ���ڿ�
     * @return ���Ŵ�� ���ڿ��� ���ŵ� �Է¹��ڿ�. �Է¹��ڿ��� null�� ��� ��¹��ڿ��� null
     */
    public static String remove(String str, char remove) {
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    /**
     * <p>���ڿ� ������ �޸� character(,)�� ��� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.removeCommaChar(null)       = null
     * StringUtil.removeCommaChar("")         = ""
     * StringUtil.removeCommaChar("asdfg,qweqe") = "asdfgqweqe"
     * </pre>
     *
     * @param str �Է¹޴� ���� ���ڿ�
     * @return " , "�� ���ŵ� �Է¹��ڿ�
     *  �Է¹��ڿ��� null�� ��� ��¹��ڿ��� null
     */
    public static String removeCommaChar(String str) {
        return remove(str, ',');
    }

    /**
     * <p>���ڿ� ������ ���̳ʽ� character(-)�� ��� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.removeMinusChar(null)       = null
     * StringUtil.removeMinusChar("")         = ""
     * StringUtil.removeMinusChar("a-sdfg-qweqe") = "asdfgqweqe"
     * </pre>
     *
     * @param str  �Է¹޴� ���� ���ڿ�
     * @return " - "�� ���ŵ� �Է¹��ڿ�
     *  �Է¹��ڿ��� null�� ��� ��¹��ڿ��� null
     */
    public static String removeMinusChar(String str) {
        return remove(str, '-');
    }


    /**
     * �� ���ڿ��� ���Ե� Ư�� ���ڿ��� ���ο� ���ڿ��� ��ȯ�ϴ� �޼���
     * @param source �� ���ڿ�
     * @param subject �� ���ڿ��� ���Ե� Ư�� ���ڿ�
     * @param object ��ȯ�� ���ڿ�
     * @return sb.toString() ���ο� ���ڿ��� ��ȯ�� ���ڿ�
     */
    public static String replace(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr  = source;

        while (srcStr.indexOf(subject) >= 0) {
            preStr = srcStr.substring(0, srcStr.indexOf(subject));
            nextStr = srcStr.substring(srcStr.indexOf(subject) + subject.length(), srcStr.length());
            srcStr = nextStr;
            rtnStr.append(preStr).append(object);
        }
        rtnStr.append(nextStr);
        return rtnStr.toString();
    }

    /**
     * �� ���ڿ��� ���Ե� Ư�� ���ڿ� ù��° �Ѱ��� ���ο� ���ڿ��� ��ȯ�ϴ� �޼���
     * @param source �� ���ڿ�
     * @param subject �� ���ڿ��� ���Ե� Ư�� ���ڿ�
     * @param object ��ȯ�� ���ڿ�
     * @return sb.toString() ���ο� ���ڿ��� ��ȯ�� ���ڿ� / source Ư�����ڿ��� ��� ��� �� ���ڿ�
     */
    public static String replaceOnce(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        if (source.indexOf(subject) >= 0) {
            preStr = source.substring(0, source.indexOf(subject));
            nextStr = source.substring(source.indexOf(subject) + subject.length(), source.length());
            rtnStr.append(preStr).append(object).append(nextStr);
            return rtnStr.toString();
        } else {
            return source;
        }
    }

    /**
     * <code>subject</code>�� ���Ե� ������ ���ڸ� object�� ��ȯ�Ѵ�.
     *
     * @param source �� ���ڿ�
     * @param subject �� ���ڿ��� ���Ե� Ư�� ���ڿ�
     * @param object ��ȯ�� ���ڿ�
     * @return sb.toString() ���ο� ���ڿ��� ��ȯ�� ���ڿ�
     */
    public static String replaceChar(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr  = source;

        char chA;

        for (int i = 0; i < subject.length(); i++) {
            chA = subject.charAt(i);

            if (srcStr.indexOf(chA) >= 0) {
                preStr = srcStr.substring(0, srcStr.indexOf(chA));
                nextStr = srcStr.substring(srcStr.indexOf(chA) + 1, srcStr.length());
                srcStr = rtnStr.append(preStr).append(object).append(nextStr).toString();
            }
        }

        return srcStr;
    }

    /**
     * <p><code>str</code> �� <code>searchStr</code>�� ����(index) ��ġ�� ��ȯ.</p>
     *
     * <p>�Է°� �� <code>null</code>�� ���� ��� <code>-1</code>�� ��ȯ.</p>
     *
     * <pre>
     * StringUtil.indexOf(null, *)          = -1
     * StringUtil.indexOf(*, null)          = -1
     * StringUtil.indexOf("", "")           = 0
     * StringUtil.indexOf("aabaabaa", "a")  = 0
     * StringUtil.indexOf("aabaabaa", "b")  = 2
     * StringUtil.indexOf("aabaabaa", "ab") = 1
     * StringUtil.indexOf("aabaabaa", "")   = 0
     * </pre>
     *
     * @param str  �˻� ���ڿ�
     * @param searchStr  �˻� ����ڿ�
     * @return �˻� ���ڿ� �� �˻� ����ڿ��� �ִ� ���� ��ġ �˻���� ���ڿ��� ��ų� null�� ��� -1
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }


    /**
     * <p>����Ŭ�� decode �Լ��� ������ ����� ���� �޼����̴�.
     * <code>sourStr</code>�� <code>compareStr</code>�� ���� ������
     * <code>returStr</code>�� ��ȯ�ϸ�, �ٸ���  <code>defaultStr</code>�� ��ȯ�Ѵ�.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("����", "����", null, "bar") = null
     * StringUtil.decode("����", "����  ", "foo", null) = null
     * StringUtil.decode("����", "����", "foo", "bar") = "foo"
     * StringUtil.decode("����", "����  ", "foo", "bar") = "bar"
     * </pre>
     *
     * @param sourceStr ���� ���ڿ�
     * @param compareStr �� ��� ���ڿ�
     * @param returnStr sourceStr�� compareStr�� ���� ���� �� ��ȯ�� ���ڿ�
     * @param defaultStr sourceStr�� compareStr�� ���� �ٸ� �� ��ȯ�� ���ڿ�
     * @return sourceStr�� compareStr�� ���� ����(equal)�� �� returnStr�� ��ȯ�ϸ�,
     *         <br/>�ٸ��� defaultStr�� ��ȯ�Ѵ�.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) {
        if (sourceStr == null && compareStr == null) {
            return returnStr;
        }

        if (sourceStr == null && compareStr != null) {
            return defaultStr;
        }

        if (sourceStr.trim().equals(compareStr)) {
            return returnStr;
        }

        return defaultStr;
    }

    /**
     * <p>����Ŭ�� decode �Լ��� ������ ����� ���� �޼����̴�.
     * <code>sourStr</code>�� <code>compareStr</code>�� ���� ������
     * <code>returStr</code>�� ��ȯ�ϸ�, �ٸ���  <code>sourceStr</code>�� ��ȯ�Ѵ�.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo") = "foo"
     * StringUtil.decode("", null, "foo") = ""
     * StringUtil.decode(null, "", "foo") = null
     * StringUtil.decode("����", "����", "foo") = "foo"
     * StringUtil.decode("����", "���� ", "foo") = "����"
     * StringUtil.decode("����", "����", "foo") = "����"
     * </pre>
     *
     * @param sourceStr ���� ���ڿ�
     * @param compareStr �� ��� ���ڿ�
     * @param returnStr sourceStr�� compareStr�� ���� ���� �� ��ȯ�� ���ڿ�
     * @return sourceStr�� compareStr�� ���� ����(equal)�� �� returnStr�� ��ȯ�ϸ�,
     *         <br/>�ٸ��� sourceStr�� ��ȯ�Ѵ�.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr) {
        return decode(sourceStr, compareStr, returnStr, sourceStr);
    }

    /**
     * ��ü�� null���� Ȯ���ϰ� null�� ��� "" �� �ٲٴ� �޼���
     * @param object �� ��ü
     * @return resultVal ���ڿ�
     */
    public static String isNullToString(Object object) {
        String string = "";

        if (object != null) {
            string = object.toString().trim();
        }

        return string;
    }

    /**
     *<pre>
     * ���ڷ� ���� String�� null�� ��� &quot;&quot;�� �����Ѵ�.
     * &#064;param src null���� ���ɼ��� �ִ� String ��.
     * &#064;return ���� String�� null ���� ��� &quot;&quot;�� �ٲ� String ��.
     *</pre>
     */
    public static String nullConvert(Object src) {
	//if (src != null && src.getClass().getName().equals("java.math.BigDecimal")) {
	if (src != null && src instanceof java.math.BigDecimal) {
	    return ((BigDecimal)src).toString();
	}

	if (src == null || src.equals("null")) {
	    return "";
	} else {
	    return ((String)src).trim();
	}
    }

    /**
     *<pre>
     * ���ڷ� ���� String�� null�� ��� &quot;&quot;�� �����Ѵ�.
     * &#064;param src null���� ���ɼ��� �ִ� String ��.
     * &#064;return ���� String�� null ���� ��� &quot;&quot;�� �ٲ� String ��.
     *</pre>
     */
    public static String nullConvert(String src) {

	if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
	    return "";
	} else {
	    return src.trim();
	}
    }

    /**
     *<pre>
     * ���ڷ� ���� String�� null�� ��� &quot;0&quot;�� �����Ѵ�.
     * &#064;param src null���� ���ɼ��� �ִ� String ��.
     * &#064;return ���� String�� null ���� ��� &quot;0&quot;�� �ٲ� String ��.
     *</pre>
     */
    public static int zeroConvert(Object src) {

	if (src == null || src.equals("null")) {
	    return 0;
	} else {
	    return Integer.parseInt(((String)src).trim());
	}
    }

    /**
     *<pre>
     * ���ڷ� ���� String�� null�� ��� &quot;&quot;�� �����Ѵ�.
     * &#064;param src null���� ���ɼ��� �ִ� String ��.
     * &#064;return ���� String�� null ���� ��� &quot;&quot;�� �ٲ� String ��.
     *</pre>
     */
    public static int zeroConvert(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
		    return 0;
		} else {
		    return Integer.parseInt(src.trim());
		}
    }

    public static int parseDoubleToInt(String src) {

		if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
		    return 0;
		} else {
		    return (int)Double.parseDouble(src.trim());
		}
    }
    
    /**
     * <p>���ڿ����� {@link Character#isWhitespace(char)}�� ���ǵ�
     * ��� ��鹮�ڸ� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.removeWhitespace(null)         = null
     * StringUtil.removeWhitespace("")           = ""
     * StringUtil.removeWhitespace("abc")        = "abc"
     * StringUtil.removeWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str  ��鹮�ڰ� ���ŵ���� �� ���ڿ�
     * @return the ��鹮�ڰ� ���ŵ� ���ڿ�, null�� �ԷµǸ� <code>null</code>�� ����
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }

        return new String(chs, 0, count);
    }

    /**
     * Html �ڵ尡 �� ������ ǥ���Ҷ� �±׿� �ջ���� ���̱� ���� �޼���
     *
     * @param strString
     * @return HTML �±׸� ġȯ�� ���ڿ�
     */
    public static String checkHtmlView(String strString) {
	String strNew = "";

	try {
	    StringBuffer strTxt = new StringBuffer("");

	    char chrBuff;
	    int len = strString.length();

	    for (int i = 0; i < len; i++) {
		chrBuff = (char)strString.charAt(i);

		switch (chrBuff) {
		case '<':
		    strTxt.append("&lt;");
		    break;
		case '>':
		    strTxt.append("&gt;");
		    break;
		case '"':
		    strTxt.append("&quot;");
		    break;
		case 10:
		    strTxt.append("<br>");
		    break;
		case ' ':
		    strTxt.append("&nbsp;");
		    break;
		//case '&' :
		    //strTxt.append("&amp;");
		    //break;
		default:
		    strTxt.append(chrBuff);
		}
	    }

	    strNew = strTxt.toString();

	} catch (Exception ex) {
	    return null;
	}

	return strNew;
    }


    /**
     * ���ڿ��� ������ �и��ڿ� ���� �迭�� �����ϴ� �޼���.
     * @param source �� ���ڿ�
     * @param separator �и���
     * @return result �и��ڷ� �������� ���ڿ� �迭
     */
    public static String[] split(String source, String separator) throws NullPointerException {
        String[] returnVal = null;
        int cnt = 1;

        int index = source.indexOf(separator);
        int index0 = 0;
        while (index >= 0) {
            cnt++;
            index = source.indexOf(separator, index + 1);
        }
        returnVal = new String[cnt];
        cnt = 0;
        index = source.indexOf(separator);
        while (index >= 0) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);

        return returnVal;
    }

    /**
     * <p>{@link String#toLowerCase()}�� �̿��Ͽ� �ҹ��ڷ� ��ȯ�Ѵ�.</p>
     *
     * <pre>
     * StringUtil.lowerCase(null)  = null
     * StringUtil.lowerCase("")    = ""
     * StringUtil.lowerCase("aBc") = "abc"
     * </pre>
     *
     * @param str �ҹ��ڷ� ��ȯ�Ǿ�� �� ���ڿ�
     * @return �ҹ��ڷ� ��ȯ�� ���ڿ�, null�� �ԷµǸ� <code>null</code> ����
     */
    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    /**
     * <p>{@link String#toUpperCase()}�� �̿��Ͽ� �빮�ڷ� ��ȯ�Ѵ�.</p>
     *
     * <pre>
     * StringUtil.upperCase(null)  = null
     * StringUtil.upperCase("")    = ""
     * StringUtil.upperCase("aBc") = "ABC"
     * </pre>
     *
     * @param str �빮�ڷ� ��ȯ�Ǿ�� �� ���ڿ�
     * @return �빮�ڷ� ��ȯ�� ���ڿ�, null�� �ԷµǸ� <code>null</code> ����
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toUpperCase();
    }

    /**
     * <p>�Էµ� String�� ���ʿ��� �ι�° ���ڷ� ��޵� ����(stripChars)�� ��� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.stripStart(null, *)          = null
     * StringUtil.stripStart("", *)            = ""
     * StringUtil.stripStart("abc", "")        = "abc"
     * StringUtil.stripStart("abc", null)      = "abc"
     * StringUtil.stripStart("  abc", null)    = "abc"
     * StringUtil.stripStart("abc  ", null)    = "abc  "
     * StringUtil.stripStart(" abc ", null)    = "abc "
     * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str ������ ���ڰ� ���ŵǾ�� �� ���ڿ�
     * @param stripChars ���Ŵ�� ���ڿ�
     * @return ������ ���ڰ� ���ŵ� ���ڿ�, null�� �ԷµǸ� <code>null</code> ����
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }

        return str.substring(start);
    }


    /**
     * <p>�Էµ� String�� ���ʿ��� �ι�° ���ڷ� ��޵� ����(stripChars)�� ��� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.stripEnd(null, *)          = null
     * StringUtil.stripEnd("", *)            = ""
     * StringUtil.stripEnd("abc", "")        = "abc"
     * StringUtil.stripEnd("abc", null)      = "abc"
     * StringUtil.stripEnd("  abc", null)    = "  abc"
     * StringUtil.stripEnd("abc  ", null)    = "abc"
     * StringUtil.stripEnd(" abc ", null)    = " abc"
     * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str ������ ���ڰ� ���ŵǾ�� �� ���ڿ�
     * @param stripChars ���Ŵ�� ���ڿ�
     * @return ������ ���ڰ� ���ŵ� ���ڿ�, null�� �ԷµǸ� <code>null</code> ����
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }

        return str.substring(0, end);
    }

    /**
     * <p>�Էµ� String�� ��, �ڿ��� �ι�° ���ڷ� ��޵� ����(stripChars)�� ��� �����Ѵ�.</p>
     *
     * <pre>
     * StringUtil.strip(null, *)          = null
     * StringUtil.strip("", *)            = ""
     * StringUtil.strip("abc", null)      = "abc"
     * StringUtil.strip("  abc", null)    = "abc"
     * StringUtil.strip("abc  ", null)    = "abc"
     * StringUtil.strip(" abc ", null)    = "abc"
     * StringUtil.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str ������ ���ڰ� ���ŵǾ�� �� ���ڿ�
     * @param stripChars ���Ŵ�� ���ڿ�
     * @return ������ ���ڰ� ���ŵ� ���ڿ�, null�� �ԷµǸ� <code>null</code> ����
     */
    public static String strip(String str, String stripChars) {
	if (isEmpty(str)) {
	    return str;
	}

	String srcStr = str;
	srcStr = stripStart(srcStr, stripChars);

	return stripEnd(srcStr, stripChars);
    }

    /**
     * ���ڿ��� ������ �и��ڿ� ���� ������ ������ �迭�� �����ϴ� �޼���.
     * @param source �� ���ڿ�
     * @param separator �и���
     * @param arraylength �迭 ����
     * @return �и��ڷ� �������� ���ڿ� �迭
     */
    public static String[] split(String source, String separator, int arraylength) throws NullPointerException {
        String[] returnVal = new String[arraylength];
        int cnt = 0;
        int index0 = 0;
        int index = source.indexOf(separator);
        while (index >= 0 && cnt < (arraylength - 1)) {
            returnVal[cnt] = source.substring(index0, index);
            index0 = index + 1;
            index = source.indexOf(separator, index + 1);
            cnt++;
        }
        returnVal[cnt] = source.substring(index0);
        if (cnt < (arraylength - 1)) {
            for (int i = cnt + 1; i < arraylength; i++) {
                returnVal[i] = "";
            }
        }

        return returnVal;
    }

    /**
     * ���ڿ� A���� Z������ ���� ���ڿ��� ���ϴ� ����� ���� ���۹��ڿ��� ���Ṯ�ڿ� ������ ���� ���ڿ��� ���ϴ� ���
     *
     * @param startChr
     *            - ù ����
     * @param endChr
     *            - ����������
     * @return ������
     * @exception MyException
     * @see
     */
    public static String getRandomStr(char startChr, char endChr) {

	int randomInt;
	String randomStr = null;

	// ���۹��� �� ���Ṯ�ڸ� �ƽ�Ű���ڷ� ��ȯ�Ѵ�.
	int startInt = Integer.valueOf(startChr);
	int endInt = Integer.valueOf(endChr);

	// ���۹��ڿ��� ���Ṯ�ڿ����� Ŭ���
	if (startInt > endInt) {
	    throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
	}

	try {
	    // ���� ��ü ��
	    SecureRandom rnd = new SecureRandom();

	    do {
		// ���۹��� �� ���Ṯ�� �߿��� ���� ���ڸ� �߻��Ų��.
		randomInt = rnd.nextInt(endInt + 1);
	    } while (randomInt < startInt); // �Է¹��� ���� 'A'(65)���� ������ �ٽ� ���� ���� �߻�.

	    // ���� ���ڸ� ���ڷ� ��ȯ �� ��Ʈ������ �ٽ� ��ȯ
	    randomStr = (char)randomInt + "";
	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new RuntimeException(e);	// 2011.10.10 �������� �ļ���ġ
	}

	// �����ڿ��� ����
	return randomStr;
    }

    /**
     * ���ڿ��� �پ��� ���ڼ�(EUC-KR[KSC5601],UTF-8..)�� ����Ͽ� ���ڵ��ϴ� ��� ������ ���ڵ��Ͽ� ���� ���ڿ���
     * �����ϴ� ����� ������ String temp = new String(���ڿ�.getBytes("�ٲٱ��� ���ڵ�"),"�ٲ� ���ڵ�");
     * String temp = new String(���ڿ�.getBytes("8859_1"),"KSC5601"); => UTF-8 ����
     * EUC-KR
     *
     * @param srcString
     *            - ���ڿ�
     * @param srcCharsetNm
     *            - �� CharsetNm
     * @param charsetNm
     *            - CharsetNm
     * @return ��(��)�ڵ� ���ڿ�
     * @exception MyException
     * @see
     */
    public static String getEncdDcd(String srcString, String srcCharsetNm, String cnvrCharsetNm) {

	String rtnStr = null;

	if (srcString == null)
	    return null;

	try {
	    rtnStr = new String(srcString.getBytes(srcCharsetNm), cnvrCharsetNm);
	} catch (UnsupportedEncodingException e) {
	    rtnStr = null;
	}

	return rtnStr;
    }

/**
     * Ư�����ڸ� �� ������ ���������� ���̱� ���� Ư�����ڸ� ó��('<' -> & lT)�ϴ� ����̴�
     * @param 	srcString 		- '<'
     * @return 	��ȯ���ڿ�('<' -> "&lt"
     * @exception MyException
     * @see
     */
    public static String getSpclStrCnvr(String srcString) {

	String rtnStr = null;

	try {
	    StringBuffer strTxt = new StringBuffer("");

	    char chrBuff;
	    int len = srcString.length();

	    for (int i = 0; i < len; i++) {
		chrBuff = (char)srcString.charAt(i);

		switch (chrBuff) {
		case '<':
		    strTxt.append("&lt;");
		    break;
		case '>':
		    strTxt.append("&gt;");
		    break;
		case '&':
		    strTxt.append("&amp;");
		    break;
		default:
		    strTxt.append(chrBuff);
		}
	    }

	    rtnStr = strTxt.toString();

	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new RuntimeException(e);	// 2011.10.10 �������� �ļ���ġ
	}

	return rtnStr;
    }

    /**
     * ������ø����̼ǿ��� �������� ����ϱ� ���� �ý��ۿ���17�ڸ���TIMESTAMP���� ���ϴ� ���
     *
     * @param
     * @return Timestamp ��
     * @exception MyException
     * @see
     */
    public static String getTimeStamp() {

	String rtnStr = null;

	// ���ڿ��� ��ȯ�ϱ� ���� ���� ����(�⵵-��-�� ��:��:��:��(�������� ��))
	String pattern = "yyyyMMddhhmmssSSS";

	try {
	    SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
	    Timestamp ts = new Timestamp(System.currentTimeMillis());

	    rtnStr = sdfCurrent.format(ts.getTime());
	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new RuntimeException(e);	// 2011.10.10 �������� �ļ���ġ
	}

	return rtnStr;
    }

    /**
     * html�� Ư�����ڸ� ǥ���ϱ� ����
     *
     * @param srcString
     * @return String
     * @exception Exception
     * @see
     */
    public static String getHtmlStrCnvr(String srcString) {

    	String tmpString = srcString;

		try
		{
			tmpString = tmpString.replaceAll("&lt;", "<");
			tmpString = tmpString.replaceAll("&gt;", ">");
			tmpString = tmpString.replaceAll("&amp;", "&");
			tmpString = tmpString.replaceAll("&nbsp;", " ");
			tmpString = tmpString.replaceAll("&apos;", "\'");
			tmpString = tmpString.replaceAll("&quot;", "\"");
		}
		catch (Exception ex)
		{
		    //ex.printStackTrace();
		    throw new RuntimeException(ex);	// 2011.10.10 �������� �ļ���ġ
		}

		return  tmpString;

	}

    /**
     * <p>��¥ ����� ���ڿ� ���ο� ���̳ʽ� character(-)�� �߰��Ѵ�.</p>
     *
     * <pre>
     *   StringUtil.addMinusChar("20100901") = "2010-09-01"
     * </pre>
     *
     * @param date  �Է¹޴� ���ڿ�
     * @return " - "�� �߰��� �Է¹��ڿ�
     */
	public static String addMinusChar(String date) {
		if(date.length() == 8)
		    return date.substring(0,4).concat("-").concat(date.substring(4,6)).concat("-").concat(date.substring(6,8));
		else return "";
	}
	
	
    /**
     * <p>���ڿ��� ���� �����ڷ� ���� �� �迭������ ��ȯ. (join�� �ݴ��� ����� ��)</p>
     *
     * @param    ��� ���ڿ�.
     * @param    ������.
     * @return   ���ڿ� ��ū�迭.
     */
    public final static String[] splitTkenizer(String s, String delimiter) {

        Vector<String> v = new Vector<String>();
        StringTokenizer st = new StringTokenizer(s, delimiter);
        while(st.hasMoreTokens())
            v.addElement(st.nextToken());

        String array[] = new String[v.size()];
        v.copyInto(array);

        return(array);
    }

    /**
     * 엑셀파일명을 반환 한다.
     * @param _userAgent
     * @param _filename
     * @return
     */
    public static String getExcelFilename(String _userAgent, String _filename){
    	String res	=	"";
    	
    	try{
    		if (_userAgent.indexOf("MSIE") > -1) {
    			res = URLEncoder.encode(_filename, "utf-8");
    		}
    		else if(_userAgent.indexOf("rv:11.0")>-1){
    			res = URLEncoder.encode(_filename, "utf-8");
    		}
    		else {
    			res = new String(_filename.getBytes("utf-8"), "ISO-8859-1");
    		}    		
    	}catch(Exception e){
    		
    	}
    	

		
    	return res;
    }
    
    
    /**
     * ascii 값을 이용한 랜덤으로 문자열 추출
     * 비밀번호 같은데서 사용할 경우 40~122 이 사용할 문자열 
     * @param maxlen 최대 문자열 길이
     * @param s_index 시작값
     * @param l_index 끝값
     * @return
     */
    public static String getRandomString(int maxlen, int s_index, int l_index){
		String	res	=	"";
		
		Random r = new Random();
		
		while(true){
			if(res.length()>=10){
				break;
			}
			
			int	r_int	=	r.nextInt(122);
			
			if(r_int>=40){
				
				if(r_int != 42 && r_int != 43 && r_int != 44 && r_int != 47
						&& r_int != 58 && r_int != 59 
						&& r_int != 60 && r_int != 61 && r_int != 62 && r_int != 63
						&& r_int != 92 && r_int != 95 && r_int != 96){
					
					res	=	res + (char)r_int;
				}
				
			}
			
		}
		
		return res;
    }
    
}
