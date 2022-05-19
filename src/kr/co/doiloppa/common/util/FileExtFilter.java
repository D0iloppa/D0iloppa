package kr.co.doiloppa.common.util;

import java.io.File;
import java.io.IOException;

public class FileExtFilter {

	  /**

     * 파일의 확장자를 체크하여 필터링된 확장자를 포함한 파일인 경우에 예외를 발생한다.

     * @param file

     * */

    public static void badFileExtIsReturnException(File file) {

        String fileName = file.getName();

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());

        final String[] BAD_EXTENSION = { "jsp", "php", "asp", "html", "perl", "bat", "js", "exe","com", "cmd","css" };

 

        try {

            int len = BAD_EXTENSION.length;

            for (int i = 0; i < len; i++) {

                if (ext.equalsIgnoreCase(BAD_EXTENSION[i])) {

                    // 불량 확장자가 존재할떄 IOExepction 발생

                    throw new IOException("BAD EXTENSION FILE UPLOAD");

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

 

    /**

     * 파일의 확장자를 체크하여 필터링된 확장자를 포함한 파일인 경우에 true를 리턴한다.

     * @param file

     * */

    public static boolean badFileExtIsReturnBoolean(File file) {

        String fileName = file.getName();

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());

        final String[] BAD_EXTENSION = { "jsp", "php", "asp", "html", "perl", "exe", "com", "cmd", "js", "css","bat"};

        int len = BAD_EXTENSION.length;

        for (int i = 0; i < len; i++) {

            if (ext.equalsIgnoreCase(BAD_EXTENSION[i])) {

                return true; // 불량 확장자가 존재할때..

            }

        }

        return false;

    }
    
    /**

     * 파일의 확장자를 체크하여 필터링된 확장자를 포함한 파일인 경우에 true를 리턴한다.

     * @param file

     * */

    public static boolean whiteFileExtIsReturnBoolean(File file) {

        String fileName = file.getName().toLowerCase();
        //System.out.println("fileName 확장자 = "+fileName);
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
       
        final String[] WHITE_EXTENSION = {
 		"xlsx", "xlsm", "xlsb", "xltx", "xltm", "xls", "xml", "xlam", "xla", "xlw", "xlr",
 		"txt", "png", "bmp", "gif", "jpg", "jpeg", "tif", "pdf", "ppt", "pptx", "pptm", "doc", "docm", "docx",
 		"hwpx", "hwp","zip","msg", "dwg", "dgn", "rar", "7z"};
        
        int len = WHITE_EXTENSION.length;

        for (int i = 0; i < len; i++) {

            if (ext.equalsIgnoreCase(WHITE_EXTENSION[i])) {

                return true; // WHITE 확장자가 존재할때

            }

        }

        return false;

    }
    
    // 엑셀파일만 저장 가능하도록
    public static boolean excelFileExtIsReturnBoolean(File file) {

        String fileName = file.getName().toLowerCase();
        //System.out.println("fileName 확장자 = "+fileName);
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
       
        final String[] excel_EXTENSION = {
 		"xlsx", "xlsm", "xlsb", "xltx", "xltm", "xls", "xml", "xlam", "xla", "xlw", "xlr"};
        
        int len = excel_EXTENSION.length;

        for (int i = 0; i < len; i++) {

            if (ext.equalsIgnoreCase(excel_EXTENSION[i])) {

                return true; // excel 확장자일때

            }

        }

        return false;

    }
    

    /**

     * 파일의 확장자를 체크하여 txt 파일인 경우에 true를 리턴한다.

     * @param file

     * */

    public static boolean txtFileExtIsReturnBoolean(File file) {

        String fileName = file.getName().toLowerCase();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
       
        final String[] TXT_EXTENSION = {"txt"};
        
        int len = TXT_EXTENSION.length;

        for (int i = 0; i < len; i++) {

            if (ext.equalsIgnoreCase(TXT_EXTENSION[i])) {

                return true; // TXT파일일경우 true 리턴

            }

        }

        return false;

    }
}
