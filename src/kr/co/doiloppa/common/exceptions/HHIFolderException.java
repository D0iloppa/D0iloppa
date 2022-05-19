/**
 * 
 */
package kr.co.doiloppa.common.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * @author doil
 * 
 *	삭제하려는 폴더에 파일이 존재할 때 발생하는 Exception
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Directory Not Found")
public class HHIFolderException extends Exception {
	public HHIFolderException() {}
	public HHIFolderException(String message) {
		super(message);
	}

}
