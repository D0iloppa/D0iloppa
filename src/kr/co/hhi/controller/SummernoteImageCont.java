package kr.co.hhi.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.ProjectNoticeVO;

@Controller
@RequestMapping("/*")

public class SummernoteImageCont {

	// app.properties에 있는 upload path
    @Value("#{config['file.upload.path']}")
	private String UPLOAD_ROOT_PATH;
    
    @Value("#{config['file.upload.noticeImg.path']}")
	private String NOTICEIMG_PATH;
    
    @Value("#{config['file.upload.prj.noticeImg.path']}")
	private String PRJ_NOTICEIMG_PATH;

    // notice
	@RequestMapping(value = "getSummernoteImage.do")
	public void getSummernoteImage(@RequestParam(value="sfile_nm", required=true) String sfile_nm, HttpServletResponse response)
            throws IOException {
        // 파일 정보를 찾고
        StringBuilder sb = new StringBuilder("file:///"+UPLOAD_ROOT_PATH+NOTICEIMG_PATH+sfile_nm);
        
        URL fileUrl = new URL(sb.toString());
        // file URL을 생성하고 
        
        IOUtils.copy(fileUrl.openStream(), response.getOutputStream());
        // IOUtils.copy는 input에서 output으로 encoding 맞춰서 복사하는 메소드다
        // openStream으로 fileUrl의 통로( 입력 스트림 )를 열고 respons의 outputStream에 복사하면 끝
 
    }
	
	// Project notice
	@RequestMapping(value = "getSummernotePrjImage.do")
	public void getSummernotePrjImage(@RequestParam(value="sfile_nm", required=true) String sfile_nm, HttpServletResponse response)
            throws IOException {
		String[] fName_PrjId = sfile_nm.split(";");
        // 파일 정보를 찾고
        StringBuilder sb = new StringBuilder("file:///"+UPLOAD_ROOT_PATH+fName_PrjId[1]+PRJ_NOTICEIMG_PATH+fName_PrjId[0]);
        
        URL fileUrl = new URL(sb.toString());
        // file URL을 생성하고 
        
        IOUtils.copy(fileUrl.openStream(), response.getOutputStream());
        // IOUtils.copy는 input에서 output으로 encoding 맞춰서 복사하는 메소드다
        // openStream으로 fileUrl의 통로( 입력 스트림 )를 열고 respons의 outputStream에 복사하면 끝
 
    }
		
    /**
	 * 1. 메소드명 : noticeSummernoteImg
	 * 2. 작성일: 2021. 12. 23.
	 * 3. 작성자: doil
	 * 4. 설명: 	엑셀파일을 불러와서 지원하지 않는 형식은 예외 처리하고, 
	 * 5. 수정일: doil
     * @throws IOException 
 */
    @RequestMapping(method=RequestMethod.POST, value="uploadSummernoteImageFile.do", produces = "app/json")
	@ResponseBody
	public Object uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		List<Object> result = new ArrayList<>();
		String fileRoot = UPLOAD_ROOT_PATH + NOTICEIMG_PATH + (CommonConst.currentDate())+"/";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
				
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		
		File targetFile = new File(fileRoot + savedFileName);	
		
		File file = new File(UPLOAD_ROOT_PATH + NOTICEIMG_PATH +(CommonConst.currentDate())+"/");
		if(file.exists() == false) {
			file.mkdirs();
		}
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			result.add((CommonConst.currentDate())+"/"+savedFileName);
		} catch (IOException e) {
			FileUtils.cleanDirectory(targetFile);	//저장된 파일 삭제
			result.add("error");
			e.printStackTrace();
		}
		
		return result;
	}
    
    /**
	 * 1. 메소드명 : prjNoticeSummernoteImg
	 * 2. 작성일: 2021. 12. 23.
	 * 3. 작성자: doil
	 * 4. 설명: 	엑셀파일을 불러와서 지원하지 않는 형식은 예외 처리하고, 
	 * 5. 수정일: doil
     * @throws IOException 
 */
    @RequestMapping(method=RequestMethod.POST, value="uploadSummernotePrjImageFile.do", produces = "app/json")
	@ResponseBody
	public Object uploadSummernotePrjImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request, ProjectNoticeVO projectnoticeVo) throws IOException {
		List<Object> result = new ArrayList<>();
		String fileRoot = UPLOAD_ROOT_PATH + projectnoticeVo.getPrj_id() + PRJ_NOTICEIMG_PATH + (CommonConst.currentDate())+"/";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
				
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		
		File targetFile = new File(fileRoot + savedFileName);
		
		File file = new File(UPLOAD_ROOT_PATH + projectnoticeVo.getPrj_id() + PRJ_NOTICEIMG_PATH +(CommonConst.currentDate())+"/");
		if(file.exists() == false) {
			file.mkdirs();
		}
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			result.add((CommonConst.currentDate())+"/"+savedFileName);
		} catch (IOException e) {
			FileUtils.cleanDirectory(targetFile);	//저장된 파일 삭제
			result.add("error");
			e.printStackTrace();
		}
		
		return result;
	}
    
    private static void copyInputStreamToFile(InputStream inputStream, File file) throws FileNotFoundException, IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
