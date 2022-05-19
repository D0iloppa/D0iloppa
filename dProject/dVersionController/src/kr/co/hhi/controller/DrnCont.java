/**
 * 
 */
package kr.co.hhi.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.FileExtFilter;
import kr.co.hhi.excel.ExcelFileDownload;
import kr.co.hhi.model.CodeInfoVO;
import kr.co.hhi.model.DrnLineVO;
import kr.co.hhi.model.DrnPageVO;
import kr.co.hhi.model.DrnPopUpVO;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDisciplineVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.DrnService;
import kr.co.hhi.service.PrjCodeSettingsService;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.PrjEmailTypeContentsService;
import kr.co.hhi.service.PrjInfoService;
import kr.co.hhi.service.PrjStepService;
import kr.co.hhi.service.PrjTrService;
import kr.co.hhi.service.TreeService;

/**
 * @author doil
 *
 */

@Controller
@RequestMapping("/*")
public class DrnCont {
	
	protected DrnService drnService;
	protected PrjInfoService prjinfoService;
	protected PrjTrService prjtrService;
	protected PrjStepService prjstepService;
	protected PrjCodeSettingsService prjcodesettingsService;
	protected PrjDocumentIndexService prjdocumentindexService;
	protected TreeService treeService;
	protected PrjEmailTypeContentsService  prjemailtypecontentsService;
	
	@Autowired
	public void setPrjInfoService(PrjInfoService prjinfoService) {
		this.prjinfoService = prjinfoService;
	}
	@Autowired
	public void setPrjTrService(PrjTrService prjtrService) {
		this.prjtrService = prjtrService;
	}
	@Autowired
	public void setPrjStepService(PrjStepService prjstepService) {
		this.prjstepService = prjstepService;
	}
	@Autowired
	public void setPrjCodeSettingsService(PrjCodeSettingsService prjcodesettingsService) {
		this.prjcodesettingsService = prjcodesettingsService;
	}
	@Autowired
	public void setPrjDocumentIndexService(PrjDocumentIndexService prjdocumentindexService) {
		this.prjdocumentindexService = prjdocumentindexService;
	}
	@Autowired
	public void setTreeService(TreeService treeService) {
		this.treeService = treeService;
	}
	@Autowired
	public void setPrjEmailTypeContentsService(PrjEmailTypeContentsService prjemailtypecontentsService) {
		this.prjemailtypecontentsService = prjemailtypecontentsService;
	}
	
	@Autowired
	public void setDrnService(DrnService drnService) {
		this.drnService = drnService;
	}

	
	@RequestMapping(value = "localFileChk.do")
	@ResponseBody
	public ModelAndView localFileChk(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		// ajax체크할 때, 해당 파라메터는 sfile_nm으로 가져옴(기존 vo의 사용되지 않는 변수 활용)
		String file_nm = drnLineVO.getSfile_nm();
		
		DrnLineVO vo = drnService.selectOne("drnSqlMap.getDrnLocalFileInfo",drnLineVO);
		mv.addObject("status",false);
		
		String err_msg = "";
		
		if(vo == null) { // DB상에서 조회된 값이 없으면 return (예외처리)
			err_msg = "There is no local file inquired on DB.";
			mv.addObject("err_msg",err_msg);
			return mv;
		}
		
    	String rfile_nm = vo.getRfile_nm();
		
		// 실제 DB상의 파일이 있더라도 
		// 기존 DB에 있는 파일의 rfile_nm과 현재 클릭한 파일의 이름이 다르다면 데이터 불일치하므로 return
		// 임시저장에서 파일을 교체한 경우 해당 현상이 나타날 수 있음
		if(!file_nm.equals(rfile_nm)) {
			err_msg = "This file has not been uploaded to the server yet.";
			mv.addObject("err_msg",err_msg);
			return mv;
		}
		
		
		mv.addObject("status",true);		

		return mv;
	}
	
	
	@RequestMapping(value = "drnExpireTempDelete.do")
	@ResponseBody
	public void drnExpireTempDelete(DrnLineVO drnLineVO) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, -10);
		String now = sdf.format(cal.getTime());
		
		// 만료된 DRN 임시저장 파일 수집
		List<DrnLineVO> collector  = drnService.getExpiredTempSaveDrns(now);
		
		
		for(DrnLineVO item : collector) {
			try {
				
				// 해당 drn에 첨부된 doc들 체크아웃 해제
				List<DrnLineVO> docList = drnService.getDocsFromDrn(item);
				for(DrnLineVO doc : docList) {
					if(drnService.cancelCheckOut(doc) < 1) throw new Exception("CheckOut 해제 실패");					
				}
			
				
				int delCnt = drnService.delExpiredTempSaveDrns(item);
				if(delCnt < 1) throw new Exception("DRN 삭제 실패");
				System.out.println("*["+ delCnt +" rows deleted]" );
				
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		
		System.out.println("["+ now +"]:expiredDrnTempSaveDel");
	}
	
	@RequestMapping("drnLocalFileDownload.do")
	 public void drnLocalFileDownload(@RequestParam String prj_id,@RequestParam String drn_id,@RequestParam String file_nm,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		DrnLineVO getter = new DrnLineVO();
		getter.setPrj_id(prj_id);
		getter.setDrn_id(drn_id);
		// DB상에서 검색
		DrnLineVO vo = drnService.selectOne("drnSqlMap.getDrnLocalFileInfo",getter);
		
		if(vo == null) { // DB상에서 조회된 값이 없으면 return (예외처리)
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = response.getWriter(); 
			writer.println("<script>alert('There is no local file inquired on DB.')</script>");
			writer.close();

			return;
		}
		
		
		String sfile_nm = vo.getSfile_nm();
    	String rfile_nm = vo.getRfile_nm();
		String tempPath = UPLOAD_PATH + prj_id + PCFILE_PATH; //파일 저장경로
		
		// 실제 DB상의 파일이 있더라도 
		// 기존 DB에 있는 파일의 rfile_nm과 현재 클릭한 파일의 이름이 다르다면 데이터 불일치하므로 return
		// 임시저장에서 파일을 교체한 경우 해당 현상이 나타날 수 있음
		if(!file_nm.equals(rfile_nm)) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = response.getWriter(); 
			writer.println("<script>alert('This file has not been uploaded to the server yet.')</script>");
			writer.close();
			return;
		}
		
    	
		try {
			// 파일다운로드 START
			response.setContentType("application/zip");
            rfile_nm = CommonConst.downloadNameEncoding(request, rfile_nm);
			response.addHeader("Content-Disposition", "attachment;filename=" + rfile_nm);

			FileInputStream fis = new FileInputStream(tempPath + sfile_nm);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ServletOutputStream so = response.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(so);
			byte[] buffer = new byte[1024];

			int n = 0;
			while ((n = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, n);
				bos.flush();
			}

			if (bos != null)
				bos.close();
			if (bis != null)
				bis.close();
			if (so != null)
				so.close();
			if (fis != null)
				fis.close();
			// 파일다운로드 END
			
		} catch (IOException e) {
			// Exception
			e.printStackTrace();
		}
    	
    }
	
	/**
	 * 1. 메소드명 : fileDownload
	 * 2. 작성일: 2022-01-04
	 * 3. 작성자: 소진희
	 * 4. 설명: 멀티파일 다운로드 (Zip으로 압축)
	 * 5. 수정일: 
	 */
    @RequestMapping("drnFileDownload.do")
    public void CompressZIP(@RequestParam String prj_id,@RequestParam String drn_id,@RequestParam String renameYN,@RequestParam String fileRenameSet,@RequestParam String downloadFiles,PrjDocumentIndexVO prjdocumentindexvo, HttpServletRequest request, HttpServletResponse response, Object handler) {
    	String downloadFilesString = prjdocumentindexvo.getDownloadFiles();
    	String[] downloadFileEach = downloadFilesString.split("@@");
    	String[] files = new String[downloadFileEach.length];
		for(int i=0;i<downloadFileEach.length;i++) {
			String[] downloadFile = downloadFileEach[i].split("%%");
			prjdocumentindexvo.setDoc_id(downloadFile[0]);
			prjdocumentindexvo.setRev_id(downloadFile[1]);
			PrjDocumentIndexVO getDownloadFileInfo = prjdocumentindexService.getDownloadFileInfo(prjdocumentindexvo);
			files[i] = getDownloadFileInfo.getSfile_nm();
			
		}
		
		DrnLineVO getPCfiles = new DrnLineVO();
		getPCfiles.setPrj_id(prj_id);
		getPCfiles.setDrn_id(drn_id);
		
		
		List<DrnLineVO> pc_files = drnService.getPcDocsData(getPCfiles);
		
		
    	ZipOutputStream zout = null;
    	String zipName = fileRenameSet+".zip";		//ZIP 압축 파일명
    	String tempPath = "";

    	if (files.length > 0) {
              try{
                 tempPath = UPLOAD_PATH + prjdocumentindexvo.getPrj_id() + DOC_PATH;		//파일 저장경로
                 
                 //ZIP파일 압축 START
                 zout = new ZipOutputStream(new FileOutputStream(tempPath + zipName));
                 byte[] buffer = new byte[1024];
                 FileInputStream in = null;
                 
                 for ( int k = 0; k<files.length; k++){
                     //in = new FileInputStream("/vdsFileupload/" + files[k]);		//압축 대상 파일
                	 in = new FileInputStream(tempPath + files[k]);		//압축 대상 파일 
         			zout.putNextEntry(new ZipEntry(files[k]));	//압축파일에 저장될 파일명
                    int len;
                    while((len = in.read(buffer)) > 0){
                       zout.write(buffer, 0, len);			//읽은 파일을 ZipOutputStream에 Write
                    }
                    
                    zout.closeEntry();
                    in.close();
                 }
                 
                String local_path = UPLOAD_PATH + prj_id +PCFILE_PATH;
                if(pc_files != null) // localfile이 존재하는 경우에만 진행
				for (int i = 0; i < pc_files.size(); i++) {
					in = new FileInputStream(local_path + pc_files.get(i).getSfile_nm()); // 압축 대상 파일
					zout.putNextEntry(new ZipEntry(pc_files.get(i).getRfile_nm())); // 압축파일에 저장될 파일명
					int len;
					while ((len = in.read(buffer)) > 0) 
						zout.write(buffer, 0, len); // 읽은 파일을 ZipOutputStream에 Write
					
					zout.closeEntry();
					in.close();
				}

                 zout.close();
                 //ZIP파일 압축 END
                 
                 //파일다운로드 START
                 response.setContentType("application/zip");
                 zipName = CommonConst.downloadNameEncoding(request, zipName);
                 response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
                 
                 FileInputStream fis = new FileInputStream(tempPath + zipName);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ServletOutputStream so = response.getOutputStream();
                 BufferedOutputStream bos = new BufferedOutputStream(so);

                 int n = 0;
                 while((n = bis.read(buffer)) > 0){
                    bos.write(buffer, 0, n);
                    bos.flush();
                 }
                 
                 if(bos != null) bos.close();
                 if(bis != null) bis.close();
                 if(so != null) so.close();
                 if(fis != null) fis.close();
                 
      			Path filePath = Paths.get(tempPath + zipName);
      			//tempPath폴더에 생성된 zip파일 삭제
      			Files.deleteIfExists(filePath);
                 //파일다운로드 END
              }catch(IOException e){
                 //Exception
            	  e.printStackTrace();
              }finally{
                 if (zout != null){
                    zout = null;
                 }
              }
    	}
    }
	
	/**
	 * 
	 * 1. 메소드명 : get_as
	 * 2. 작성일: 2022. 1. 27.
	 * 3. 작성자: doil
	 * 4. 설명: approval status 조회
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "get_as.do")
	@ResponseBody
	public ModelAndView get_as(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		
		DrnLineVO getAs = drnService.selectOne("drnSqlMap.get_as", drnLineVO);
		
		mv.addObject("approval_Status",getAs.getDrn_approval_status());		

		return mv;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getThisDRNFolderId
	 * 2. 작성일: 2022. 3. 6.
	 * 3. 작성자: doil
	 * 4. 설명: drn_id로 folder_id를 구함
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getThisDRNFolderId.do")
	@ResponseBody
	public ModelAndView getThisDRNFolderId(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		
		Long folder_id = drnService.selectOne("drnSqlMap.getThisDRNFolderId", drnLineVO);
		
		mv.addObject("folder_id",folder_id);		

		return mv;
	}
	
	
	
	
	/**
	 * 
	 * 1. 메소드명 : drnUseCheck
	 * 2. 작성일: 2022. 2. 15.
	 * 3. 작성자: doil
	 * 4. 설명: 해당폴더가 DRN 사용하는지 체크 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "drnUseCheck.do")
	@ResponseBody
	public ModelAndView drnUseCheck(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		
		DrnLineVO getAs = drnService.selectOne("drnSqlMap.get_as", drnLineVO);
		
		mv.addObject("approval_Status",getAs.getDrn_approval_status());		

		return mv;
	}
	
	
	@RequestMapping(value = "getLocalFile.do")
	@ResponseBody
	public ModelAndView getLocalFile(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("local_file_chk","NO");
		
		DrnLineVO getLocalFile = drnService.selectOne("drnSqlMap.getLocalFile", drnLineVO);
		
		if(getLocalFile != null) {
			mv.addObject("local_file",getLocalFile);
			mv.addObject("local_file_chk","OK");
		}
					

		return mv;
	}

    @Value("#{config['file.upload.signImg.path']}")
    private String SIGN_PATH;

	
	@RequestMapping(value = "getSignImage.do")
	public void getSignImage(@RequestParam(value="sfile_nm", required=true) String sfile_nm, HttpServletResponse response)
            throws IOException {
        // 파일 정보를 찾고
        StringBuilder sb = new StringBuilder("file:///"+UPLOAD_PATH+SIGN_PATH+sfile_nm);
        
        URL fileUrl = new URL(sb.toString());
        // file URL을 생성하고 
        
        IOUtils.copy(fileUrl.openStream(), response.getOutputStream());
        // IOUtils.copy는 input에서 output으로 encoding 맞춰서 복사하는 메소드다
        // openStream으로 fileUrl의 통로( 입력 스트림 )를 열고 respons의 outputStream에 복사하면 끝
 
    }
	/**
	 * 
	 * 1. 메소드명 : get_dccUser
	 * 2. 작성일: 2022. 1. 13.
	 * 3. 작성자: doil
	 * 4. 설명: DCC 리스트를 가져옴 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "get_dccUser.do")
	@ResponseBody
	public ModelAndView get_dccUser(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		
		List<DrnLineVO> dccUser = drnService.selectList("drnSqlMap.get_dccUser", drnLineVO);
		
		mv.addObject("dccUser",dccUser);		

		return mv;
	}
	
	/**
	 * 
	 * 1. 메소드명 : searchCheckSheet
	 * 2. 작성일: 2022. 1. 21.
	 * 3. 작성자: doil
	 * 4. 설명: 체크시트 검색창 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "searchCheckSheet.do")
	@ResponseBody
	public ModelAndView searchCheckSheet(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		
		List<DrnLineVO> checkSheetList = drnService.selectList("drnSqlMap.searchCheckSheet", drnLineVO);
		
		mv.addObject("list",checkSheetList);		

		return mv;
	}
	
	
	@RequestMapping(value = "getRejectPoint.do")
	@ResponseBody
	public ModelAndView getRejectPoint(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		
		List<DrnLineVO> drnHist = drnService.getRejectPoint(drnLineVO);
		
		mv.addObject("drnHist",drnHist);
		

		return mv;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getStatus
	 * 2. 작성일: 2022. 1. 12.
	 * 3. 작성자: doil
	 * 4. 설명: status 값을 가져옴 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getStatus.do")
	@ResponseBody
	public Object getStatus(DrnLineVO drnLineVO) {
		
		List<Object> result = new ArrayList<>();
		result.add("FAIL");
		
		String folder_path = drnService.getFolderPath(drnLineVO);
		if(folder_path == null) {
			return result;
		}
		
		
		List<String> folder_pathSplit = Arrays.asList(folder_path.split(">")); 
		Collections.reverse(folder_pathSplit);
		
		String type = null;
		
		for(String folder_id : folder_pathSplit ) {
			if(type == null) {
				DrnLineVO tmp = new DrnLineVO();
				tmp.setPrj_id(drnLineVO.getPrj_id());
				tmp.setFolder_id(Long.parseLong(folder_id));
				type = drnService.get_DRNType(tmp);
				if(type != null) break;
			}			
		}
		
		if(type == null) return result;
		
		
		System.out.println("process type : " + type);
		

		
		
		drnLineVO.setType(type);
		List<PrjCodeSettingsVO> status = drnService.getStatus(drnLineVO);
		
		if(status == null) return result;
	
		
		
		result.set(0, "SUCCESS");
		
		result.add(status);	
		

		return result;
	}
	/**
	 * 
	 * 1. 메소드명 : get_checkList
	 * 2. 작성일: 2022. 1. 12.
	 * 3. 작성자: doil
	 * 4. 설명: 체크 시트 불러오기 모달창에 저장된 체크시트를 뿌려줌 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "get_checkList.do")
	@ResponseBody
	public Object get_checkList(DrnLineVO drnLineVO) {
		
		List<Object> result = new ArrayList<>();
		
		
		List<DrnLineVO> chkList = drnService.get_checkList(drnLineVO);
		
		result.add(chkList);	
		

		return result;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getCheckSheet
	 * 2. 작성일: 2022. 1. 12.
	 * 3. 작성자: doil
	 * 4. 설명: 체크시트 아이디로 체크리스트를 가져온다.
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getCheckSheetList.do")
	@ResponseBody
	public ModelAndView getCheckSheetList(DrnLineVO drnLineVO) {
		
		ModelAndView result = new ModelAndView();
		
		// 체크시트 메타데이터
		List<DrnLineVO> chkSheet = drnService.getCheckSheetList(drnLineVO);
		
		int no_count = chkSheet.size();
		
		// 체크시트 실제 디테일 값들
		List<Object> detailData = new ArrayList<>();
		for(int i=1;i<=no_count;i++) {
			drnLineVO.setItems_no(i);
			List<DrnLineVO> chkSheetItem = drnService.getCheckSheetItem(drnLineVO);
			detailData.add(chkSheetItem);
		}
		
		String title = drnLineVO.getCheck_sheet_title();  
		result.addObject("check_sheet_title",title);
		result.addObject("check_sheet", chkSheet);
		result.addObject("check_sheet_items",detailData);
		

		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : get_review_conclusion
	 * 2. 작성일: 2022. 1. 13.
	 * 3. 작성자: doil
	 * 4. 설명: review_conclusion 값을 가져온다. 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "get_review_conclusion.do")
	@ResponseBody
	public ModelAndView get_review_conclusion(DrnLineVO drnLineVO) {
		ModelAndView result = new ModelAndView();
		
		// 프로젝트에 설정된 기본 review conclusion
		String rvc = drnService.getReviewConclusion(drnLineVO);
		// drn_id가 발급된 이후, 해당 drn에 할당된 review conclusion
		String drn_id = drnLineVO.getDrn_id();
		String drnRvc = null;
		
		if(!drn_id.equals("-1"))
			drnRvc = drnService.getDrnReviewConclusion(drnLineVO);
		
		if(drnRvc == null)
			result.addObject("rvc", rvc);
		else
			result.addObject("rvc",drnRvc);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getApprovalStatus
	 * 2. 작성일: 2022. 1. 20.
	 * 3. 작성자: doil
	 * 4. 설명: approval status를 구하는 함수 
	 * 5. 수정일: doil
	 */
	
	@RequestMapping(value = "getApprovalStatus.do")
	@ResponseBody
	public ModelAndView getApprovalStatus(DrnLineVO drnLineVO) {
		ModelAndView result = new ModelAndView();
		
		String approval_status = drnService.getApprovalStatus(drnLineVO);
		result.addObject("approval_status", approval_status);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : get_drnVar
	 * 2. 작성일: 2022. 3. 17.
	 * 3. 작성자: doil
	 * 4. 설명: page data를 가져오기 위해서 필요한 var들을 가져오는 함수 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "get_drnVar.do")
	@ResponseBody
	public ModelAndView get_drnVar(DrnLineVO drnLineVO) {
		ModelAndView result = new ModelAndView();
		
		long approval_id = drnService.selectOne("drnSqlMap.getApproval_id",drnLineVO);
		String check_sheet_id = drnService.selectOne("drnSqlMap.getCheck_Sheet_Id",drnLineVO);
		long folder_id = (Long) drnService.selectList("drnSqlMap.getFolder_id",drnLineVO).get(0);
		String status = drnService.selectOne("drnSqlMap.getStatus",drnLineVO);
		String drn_no = drnService.selectOne("drnSqlMap.getDrn_no",drnLineVO);
		String check_sheet_title = drnService.selectOne("drnSqlMap.getCheck_sheet_title",drnLineVO);
		
		
		result.addObject("approval_id", approval_id);
		result.addObject("check_sheet_id", check_sheet_id);
		result.addObject("folder_id",folder_id);
		result.addObject("status",status);
		result.addObject("drn_no",drn_no);
		result.addObject("check_sheet_title",check_sheet_title);
		
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : get_drnPage
	 * 2. 작성일: 2022. 1. 13.
	 * 3. 작성자: doil
	 * 4. 설명: DB상에서 DRN 페이지 정보를 가져온다. 
	 * 5. 수정일: doil
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "get_drnPage.do")
	@ResponseBody
	public ModelAndView get_drnPage(DrnLineVO drnLineVO) throws JsonParseException, JsonMappingException, IOException {
		ModelAndView result = new ModelAndView();
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		DrnLineVO drnInfo = objectMapper.readValue(drnLineVO.getJsonData(), DrnLineVO.class);
		result.addObject("drnInfo", drnInfo);
		

		
		/* 체크시트 연동 */
		// 체크시트 메타데이터
		List<DrnLineVO> chkSheet = drnService.getCheckSheetList(drnInfo);

		int no_count = chkSheet.size();

		// 체크시트 실제 디테일 값들
		List<Object> detailData = new ArrayList<>();
		for (int i = 1; i <= no_count; i++) {
			drnInfo.setItems_no(i);
			List<DrnLineVO> chkSheetItem = drnService.getCheckSheetItem(drnInfo);
			detailData.add(chkSheetItem);
		}

		String title = drnInfo.getCheck_sheet_title();
		result.addObject("check_sheet_title", title);
		result.addObject("check_sheet", chkSheet);
		result.addObject("check_sheet_items", detailData);
		
		
		

		
		
		/* 결재선 get */
		DrnLineVO approvalLine = drnService.getApprovalData(drnInfo);
		approvalLine = drnService.getJobCode(approvalLine);
		String[] distributer;
		if(approvalLine != null) {
			distributer = approvalLine.getDistribute_id().split("@");
			String distributer_nm = "";
			for(String line : distributer)
				distributer_nm += drnService.getKorNm(line) + "@";
			
			distributer_nm = distributer_nm.substring(0,distributer_nm.length()-1);
			approvalLine.setDistributers(distributer_nm);
			
			/* 서명일자 get */
			DrnLineVO getter = new DrnLineVO();
			getter.setPrj_id(drnInfo.getPrj_id());
			getter.setDrn_id(drnInfo.getDrn_id());
			getter.setDrn_approval_status("A");
			String designSignDate = drnService.getApprovalSignDate(getter);
			UsersVO designSign = drnService.getUserSignData(drnInfo.getReg_id());
			result.addObject("ds_date",designSignDate);
			result.addObject("ds_info",designSign);
			
			
			getter.setDrn_approval_status("1");
			String reviewSignDate = drnService.getApprovalSignDate(getter);
			UsersVO reviewSign = drnService.getUserSignData(approvalLine.getReviewed_id());
			result.addObject("rv_date",reviewSignDate);
			result.addObject("rv_info",reviewSign);
			
			getter.setDrn_approval_status("C");
			String approveSignDate = drnService.getApprovalSignDate(getter);
			UsersVO approveSign = drnService.getUserSignData(approvalLine.getApproved_id());
			result.addObject("ap_date",approveSignDate);
			result.addObject("ap_info",approveSign);
			
			
			
		}
		
		

		
			
		
		/* 첨부파일 get */
		List<DrnLineVO> system_docs = drnService.getSysDocsData(drnInfo);
		List<DrnLineVO> pc_docs = drnService.getPcDocsData(drnInfo);
		
		
		
		// result.addObject("drnInfo", drnInfo);
		// result.addObject("signData",signData);
		result.addObject("lineInfo",approvalLine);
		result.addObject("docData", system_docs);
		result.addObject("pc_docData", pc_docs);
		
		return result;
	}
	
	

    @Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
    
    
    @Value("#{config['file.upload.prj.doc.path']}")
    private String DOC_PATH;

    @Value("#{config['file.upload.prj.template.path']}")
    private String TEMPLATE_PATH;
    
    @Value("#{config['file.upload.pcfile']}")
    private String PCFILE_PATH;
    
    /**
	 * 1. 메소드명 : drnUpLineChkFile
	 * 2. 작성일: 2021-02-16
	 * 3. 작성자: 박정우
	 * 4. 설명: 등록전 파일 확장자 체크
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "drnUpLineChkFile.do")
	@ResponseBody
	public int drnUpLineChkFile(MultipartHttpServletRequest mtRequest, DrnLineVO drnLineVO) throws Exception{
		int chkFile = 100;
		
		//List<MultipartFile> fileList = ((MultipartRequest) request).getFiles("uploadfile");
		MultipartFile mf = mtRequest.getFile("drnLocalFile");
		File file = new File(mf.getOriginalFilename());
		
		String fileChk = file.getName();
		if(fileChk.length() > 0) {
			// true면 등록 불가 false면 등록 가능한 확장자
//			boolean fileUploadChk = FileExtFilter.badFileExtIsReturnBoolean(file);
			// true면 등록 가능 false면 등록 불가능한 확장자
			boolean fileUploadChk = FileExtFilter.whiteFileExtIsReturnBoolean(file);
				
			if(fileUploadChk == true) {
				chkFile = 100;
			}else if(fileUploadChk == false) {
				chkFile = 0;
			}			
		}
			
			//System.out.println("파일 명 = "+file);
			//System.out.println("파일 확인 true or false = "+fileUploadChk);
		
		return chkFile;
	}
    
	
	@RequestMapping(value = "drnPopup.do", method = RequestMethod.POST)
	public ModelAndView drnPopup(DrnPopUpVO popupVO,HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		String prj_id = popupVO.getPrj_id();
		int pageMode = popupVO.getPageMode();
		String docDataJson =popupVO.getDocData();
		String discipObjJson = popupVO.getDiscipObj();
		long folder_id = popupVO.getFolder_id();
		String pageDataJson = popupVO.getPageData();
		String approvalJson = popupVO.getApproval();
		String session_user_id = sessioninfo.getUser_id();
		
		
		
		mv.setViewName("/popup/drnPopUp");
		mv.addObject("prj_id", prj_id);
		mv.addObject("pageMode", pageMode);
		mv.addObject("docData", docDataJson);
		mv.addObject("discipObj", discipObjJson);
		mv.addObject("folder_id", folder_id);
		mv.addObject("pageData", pageDataJson);
		mv.addObject("approval", approvalJson);
		mv.addObject("session_user_id",session_user_id);
		
		
		
		return mv;
	}

    
	/**
	 * 
	 * 1. 메소드명 : drnUpLine
	 * 2. 작성일: 2022. 1. 12.
	 * 3. 작성자: doil
	 * 4. 설명: mode에 따른 drn상신 처리 
	 * 5. 수정일: doil
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws InvalidFormatException 
	 */
	
	@RequestMapping(value = "drnUpLine.do", method = RequestMethod.POST)
	@ResponseBody
	@JsonIgnoreProperties(ignoreUnknown = true)
	public ModelAndView drnUpLine(MultipartHttpServletRequest mtRequest, DrnLineVO drnLineVO,HttpServletRequest request,PrjInfoVO prjinfovo,PrjTrVO prjtrvo,PrjCodeSettingsVO prjcodesettingsvo,PrjStepVO prjstepvo) throws JsonParseException, JsonMappingException, IOException, JSONException, ParseException, InvalidFormatException {
		
		ModelAndView result = new ModelAndView();

		
		MultipartFile file = mtRequest.getFile("drnLocalFile");
		
		List<String> errors = new ArrayList<>();
		
		try {
			boolean localFileChk = drnService.drnLocalFileChk(mtRequest);
			if(!localFileChk) throw new Exception("This local file is unsupported file format. Please contact the administrator");
		} catch (Exception e1) {
			result.addObject("status","FAIL");
			result.addObject("error_msg",e1.getMessage());
			return result;
		}
		
		result.addObject("status", "fail");
		
		// update 하기 위한 vo
		DrnLineVO update = new DrnLineVO();
		
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Calendar c1 = Calendar.getInstance();

		String reg_date = sdf.format(c1.getTime());
		
		
		
		// 데이터 로딩
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	
		
		String pageDataStr = drnLineVO.getPageData();
		
		DrnPageVO pageData = objectMapper.readValue(pageDataStr, DrnPageVO.class);
		
		String prj_no = pageData.getPrj_no();
		String prj_nm = pageData.getPrj_nm();
		String date = pageData.getDate() + "000000";
		String doc_no = pageData.getDoc_no();
		String doc_title = pageData.getDoc_title();
		String sheet_nm = pageData.getChksheet_nm();
		String discip_code_id = pageData.getDiscip_code_id();
		
		String[] sheetJson = pageData.getSheetArr();
		
		JSONObject[] sheetData = new JSONObject[sheetJson.length];
		
		for(int i=0 ; i<sheetJson.length ; i++) 
			sheetData[i] = new JSONObject(sheetJson[i]);
		
		String signDataStr = pageData.getSignData();
		JSONObject signData = new JSONObject(signDataStr);
		String status = pageData.getStatus();
		long folder_id = pageData.getFolder_id();
		
		// doc 정보 가져옴
		String[] docJson = pageData.getDocData();
		JSONObject[] docData = new JSONObject[docJson.length];
		for(int i=0;i<docJson.length;i++)
			docData[i] = new JSONObject(docJson[i]);
		
		
		System.out.println();
		
		// 이메일에서 처리하기 위한 아이디
		String action_id = drnLineVO.getUser_id();
		
  	
		String reg_id = signData.getString("design");
		String review_id = signData.getString("review");
		String approved_id = signData.getString("approve");
		String distribute_id = signData.getString("distributer");
		String dcc_user_id = signData.getString("dcc");
		long approval_id = signData.getLong("approval_id");
		
		// sheetData 파싱
		String check_sheet_id = pageData.getCheck_sheet_id();
		
		String drn_id = pageData.getDrn_id();
		
		// DRN DB 업데이트 순서
		// 임시 저장,DRN상신의 경우 :
		// 1) 체크시트 DB업데이트
		// 2) 체크시트 id 매핑
		// 3) line 설정
		// =========================
		// 4) DRN info 업데이트
		// 5) DRN 부수정보 cascade
		
		// action값에 따른 분기처리
		// 임지저장(T), 결재상신, 진행중(P), 완료(C), 지연현황(D), 반려(R)
		String action = drnLineVO.getAction();
		 
		
		
		update.setPrj_id(drnLineVO.getPrj_id());
		update.setDrn_no(pageData.getDrn_no());
		update.setDrn_title(pageData.getDrn_title());
		update.setDrn_prj_id(drnLineVO.getPrj_id());
		
		update.setCheck_sheet_title(pageData.getChksheet_nm());
		
		update.setStatus(status);
		update.setReview_conclusion(pageData.getReview_conclusion());
		update.setDrn_approval_status(action);
		update.setDoc_title(pageData.getDoc_title());
		
		update.setFolder_id(folder_id);
		update.setApproval_id(approval_id);
		update.setApproval_date(date);
		update.setReg_id(reg_id);
		update.setReg_date(reg_date);
		update.setMod_date(reg_date );
		update.setAction_id(action_id);
		
		 
		String localFileExistChk = drnLineVO.getRfile_nm();
		if (action.equals("T")) { // 임시저장
			
		
			try {
				// drn id가 존재하지 않으면 완전히 새롭게 추가
				if(drn_id == null) {
					// DRN ID 생성
					drn_id = drnService.getMaxDrnId(drnLineVO);
					update.setDrn_id(drn_id);
					// id만 생성하기 때문에 처음 폴더는 매핑해주지 않는다.
					update.setStatus("-1");
					//update.setFolder_id(-1);
					update.setFolder_id(drnLineVO.getFolder_id());
					
					// seq발번
					int seq = drnService.getDrnSeq(update.getDrn_no()+"%");
 					String drnNo = update.getDrn_no() + String.format("%03d", seq + 1);
 					update.setDrn_no(drnNo);
					
 					// 1) drn_id 생성
					if(drnService.generateDrnInfo(update) < 1) {
						throw new Exception("DRN 정보 생성 불가");
					}
				}else {
					update.setDrn_id(drn_id);
				}
				
				

					

				
				try { // approval line 생성
					if (review_id.equals("") || approved_id.equals("")) {
						throw new Exception("결재자 인원 미설정");
					}
					// 해당 drn_id에 approval_id가 할당되어져 있지 않은 경우, approval_id insert
					Long _approval_id = drnService.approvalIdCheck(update);
					if(_approval_id == null || _approval_id == -1) {
						drnLineVO.setReviewed_id(review_id);
						drnLineVO.setApproved_id(approved_id);
						drnLineVO.setDistribute_id(distribute_id);
						drnLineVO.setReg_id(reg_id);
						drnLineVO.setDcc_user_id(dcc_user_id);
						drnLineVO.setReg_date(reg_date);
						drnLineVO.setMod_date(reg_id);
						drnLineVO.setMod_id(reg_id);

						approval_id = drnService.insertDrnLine(drnLineVO);
						if (approval_id < 1)
							throw new Exception("결재선 정보 반영실패");
						
						update.setApproval_id(approval_id);
					}
					// 무조건 해당 approval_id에 update(값이 바뀌었을 수도 있기 때문)
					try {
						drnLineVO.setApproval_id(approval_id);
						drnLineVO.setReviewed_id(review_id);
						drnLineVO.setApproved_id(approved_id);
						drnLineVO.setDistribute_id(distribute_id);
						drnLineVO.setReg_id(reg_id);
						drnLineVO.setReg_date(reg_date);
						drnLineVO.setMod_id(reg_id);
						drnLineVO.setMod_date(reg_date);
						drnLineVO.setDcc_user_id(dcc_user_id);
						if (drnService.updateDrnLine(drnLineVO) < 1)
							throw new Exception("결재선 정보 update실패");
					} catch (Exception e) {
						e.printStackTrace();
						errors.add(e.getMessage());
						result.addObject("errors", errors);
					}
					
					
					
				} catch(Exception e) {
					System.out.println("[APPROVAL LINE ERROR]:" + e.getMessage());
					errors.add(e.getMessage());
				}
				

				try { // 체크시트 생성
					
					drnService.clearCheckSheet(update);
						
						check_sheet_id = drnService.getMaxSheetId(drnLineVO);

						update.setCheck_sheet_id(check_sheet_id);

						update.setFolder_id(drnLineVO.getFolder_id());

						for (int i = 0; i < sheetData.length; i++) {
							update.setItems_no(sheetData[i].getInt("items_no"));
							update.setItems_nm(sheetData[i].getString("items_nm"));
							update.setItmes_cnt(sheetData[i].getInt("items_cnt"));
							update.setReg_date(reg_date);

							if (drnService.insertCheckSheetList(update) < 1)
								throw new Exception(check_sheet_id + " : 체크시트 DB insert 실패");

							JSONArray check_items_List = sheetData[i].getJSONArray("check_items");
							JSONArray discipline_code_id_List = sheetData[i].getJSONArray("discipline_code_id");
							JSONArray design_check_status_List = sheetData[i].getJSONArray("design_check_status");
							JSONArray reviewed_check_status_List = sheetData[i].getJSONArray("reviewed_check_status");
							JSONArray approved_check_status_List = sheetData[i].getJSONArray("approved_check_status");

							for (int j = 0; j < sheetData[i].getInt("items_cnt"); j++) {
								String check_items = check_items_List.getString(j);
								//if(check_items.length()>100) check_items = check_items.substring(0,100);
								String discipline_code_id = discipline_code_id_List.getString(j);
								String design_check_status = (String) design_check_status_List.get(j);
								String reviewed_check_status = (String) reviewed_check_status_List.get(j);
								String approved_check_status = (String) approved_check_status_List.get(j);
								

								long item_id = drnService.getMaxItemId(drnLineVO);

								update.setItems_id(item_id);
								update.setItems_seq(j + 1);
								update.setCheck_items(check_items);
								update.setDiscipline_code_id(discipline_code_id);
								update.setDesign_check_status(design_check_status);
								update.setReviewed_check_status(reviewed_check_status);
								update.setApproved_check_status(approved_check_status);
								update.setReg_id(reg_id);

								if (drnService.insertCheckSheet(update) < 1)
									throw new Exception(item_id + " : 체크시트 아이템 DB insert 실패");
							}

							System.out.println();

						}

					

				} catch (Exception e) {
					System.out.println("[CHECKSHEET ERROR]:" + e.getMessage());
					errors.add(e.getMessage());
				}
				
				try { // doc 파일 insert
					
					// 해당 drn_id로 기입력된 doc파일 clear
					drnService.clearDoc(update);
					
					
					for (int i = 0; i < docData.length; i++) {

						String _doc_id = docData[i].getString("doc_id");
						String _rev_id = docData[i].getString("rev_id");
						String _reg_id = docData[i].getString("reg_id");
						String _reg_date = docData[i].getString("reg_date");
						long _folder_id = docData[i].getLong("folder_id");

						update.setDoc_id(_doc_id);
						update.setRev_id(_rev_id);
						update.setReg_id(reg_id);
						update.setReg_date(reg_date);
						update.setFolder_id(_folder_id);

						if (drnService.insertDrnDocs(update) < 1) throw new Exception("DRN ATTACH FILE 삽입 실패");
										
						update.setReg_id(reg_id);
						// 해당 문서 체크아웃
						if( drnService.checkOutDoc(update) < 1 ) throw new Exception("해당 문서 check-out 실패");
						
					}
					if(file != null && file.getSize() > 0) {
						file.getName();
						
						System.out.println(file.getSize());
						
						
						
						String[] getExtension = file.getOriginalFilename().split("\\.");
						String sfile_nm = drn_id + "." + getExtension[1]; 
						update.setRfile_nm(file.getOriginalFilename());
						update.setSfile_nm(sfile_nm);
						update.setFile_size(file.getSize()+"");
						update.setFile_path(PCFILE_PATH + sfile_nm);
						update.setReg_id(reg_id);
						update.setReg_date(reg_date);
						
						if(drnService.insertDrnPCDocs(update) < 1) throw new Exception("PC FILE 첨부 실패");
						// 파일 업로드
						String path =  UPLOAD_PATH + update.getPrj_id() +PCFILE_PATH;
						File dir = new File(path);
						dir.mkdirs();
						File uploadTarget = new File(path + sfile_nm);
						
						file.transferTo(uploadTarget);
					} else if(localFileExistChk.equals("")) {
						// 로컬파일을 첨부하지 않음
						// 기 첨부된 파일,DB가 있다면 제거
						drnService.clearLocalFile(update);
					}

					
					 
					 
				} catch (Exception e) {
					System.out.println("[DOC ERROR]:" + e.getMessage());
					e.printStackTrace();
					errors.add(e.getMessage());
				}
				
				
				try { // drnInfo 업데이트 및 history 생성
					/*
					int seq = drnService.getDrnSeq(update.getDrn_no()+"%");
 					String drnNo = update.getDrn_no() + String.format("%03d", seq + 1);
 					update.setDrn_no(drnNo);
 					*/
					
					update.setStatus(status!=null ? status : "-1");
					update.setReg_id(reg_id);
					update.setReg_date(reg_date);
					
					if (drnService.updateDrnInfo(update) < 1) throw new Exception("drn info 업데이트 실패");
					
					if (drnService.insertDrnHist(update) < 1) throw new Exception("drn history 삽입 실패");
					
				
					
					
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					errors.add("[FINAL STEP ERROR]:" + e.getMessage());
				}
				

			} catch (Exception e) { 
				System.out.println("[DRN INFO INIT ERROR]:" + e.getMessage());
				
				errors.add(e.getMessage());
				result.addObject("errors",errors);
				return result;
			}
			
			
		}
		
		else if (action.equals("A")) { // 결재상신
			// drn id 생성
			boolean directInsert = false;
			try {
				System.out.println(drn_id);

				if(drn_id == null) { // 새로이 추가하는 drn
					// 임시저장에서 불러온 값이 아닌, 바로 작성 후 결재상신
					directInsert = true;
					System.out.println("[drn id 추가]");
					drn_id = drnService.getMaxDrnId(drnLineVO);
					update.setDrn_id(drn_id);
					update.setApproval_id(-1);
					update.setStatus("-1");
					//update.setFolder_id(-1);
					update.setFolder_id(drnLineVO.getFolder_id());
					// 1) drn_id만 생성
					if (drnService.generateDrnInfo(update) < 1) {
						throw new Exception("DRN 정보 생성 불가");
					}
				}
				
				update.setDrn_id(drn_id); 
				Long _approval_id = drnService.approvalIdCheck(update);
				
				// 해당 drn_id에 approval line 미존재시, approval line 생성
				if(_approval_id == null || _approval_id == -1) {
					try {
						drnLineVO.setReviewed_id(review_id);
						drnLineVO.setApproved_id(approved_id);
						drnLineVO.setDistribute_id(distribute_id);
						drnLineVO.setReg_id(reg_id);
						drnLineVO.setDcc_user_id(dcc_user_id);
						drnLineVO.setReg_date(reg_date);
						drnLineVO.setMod_date(reg_id);
						drnLineVO.setMod_id(reg_id);

						approval_id = drnService.insertDrnLine(drnLineVO);
						if (approval_id < 1)
							throw new Exception("결재선 정보 반영실패");

						
					} catch (Exception e) {
						errors.add(e.getMessage());
						result.addObject("errors", errors);
					}
				}
				
				update.setApproval_id(approval_id);
				// 무조건 해당 approval_id에 update(값이 바뀌었을 수도 있기 때문)
				try {
					drnLineVO.setApproval_id(approval_id);
					drnLineVO.setReviewed_id(review_id);
					drnLineVO.setApproved_id(approved_id);
					drnLineVO.setDistribute_id(distribute_id);
					drnLineVO.setReg_id(reg_id);
					drnLineVO.setReg_date(reg_date);
					drnLineVO.setMod_id(reg_id);
					drnLineVO.setMod_date(reg_date);
					drnLineVO.setDcc_user_id(dcc_user_id);
					if (drnService.updateDrnLine(drnLineVO) < 1)
						throw new Exception("결재선 정보 update실패");
				} catch (Exception e) {
					e.printStackTrace();
					errors.add(e.getMessage());
					result.addObject("errors", errors);
				}
				
				
				try { // 체크시트 생성 및 반영
					// 체크시트 클리어
					drnService.clearCheckSheet(update);
					
					check_sheet_id = drnService.getMaxSheetId(drnLineVO);
					
					update.setCheck_sheet_id(check_sheet_id);
					
					update.setFolder_id(drnLineVO.getFolder_id());

					for (int i = 0; i < sheetData.length; i++) {
						update.setItems_no(sheetData[i].getInt("items_no"));
						update.setItems_nm(sheetData[i].getString("items_nm"));
						update.setItmes_cnt(sheetData[i].getInt("items_cnt"));
						update.setReg_date(reg_date);

						if (drnService.insertCheckSheetList(update) < 1)
							throw new Exception(check_sheet_id + " : 체크시트 DB insert 실패");

						JSONArray check_items_List = sheetData[i].getJSONArray("check_items");
						JSONArray discipline_code_id_List = sheetData[i].getJSONArray("discipline_code_id");
						JSONArray design_check_status_List = sheetData[i].getJSONArray("design_check_status");
						JSONArray reviewed_check_status_List = sheetData[i].getJSONArray("reviewed_check_status");
						JSONArray approved_check_status_List = sheetData[i].getJSONArray("approved_check_status");

						for (int j = 0; j < sheetData[i].getInt("items_cnt"); j++) {
							String check_items = check_items_List.getString(j);
							//if(check_items.length()>100) check_items = check_items.substring(0,100);
							String discipline_code_id = discipline_code_id_List.getString(j);
							String design_check_status = (String) design_check_status_List.get(j);
							String reviewed_check_status = (String) reviewed_check_status_List.get(j);
							String approved_check_status = (String) approved_check_status_List.get(j);

							long item_id = drnService.getMaxItemId(drnLineVO);

							update.setItems_id(item_id);
							update.setItems_seq(j + 1);
							update.setCheck_items(check_items);
							update.setDiscipline_code_id(discipline_code_id);
							update.setDesign_check_status(design_check_status);
							update.setReviewed_check_status(reviewed_check_status);
							update.setApproved_check_status(approved_check_status);
							update.setReg_id(reg_id);

							if (drnService.insertCheckSheet(update) < 1)
								throw new Exception(item_id + " : 체크시트 아이템 DB insert 실패");
						}

					}
				} catch (Exception e) {
					System.out.println("[CHECKSHEET ERROR]:" + e.getMessage());
					errors.add(e.getMessage());
				}
				
				try { // doc 파일 insert
					
					// 해당 drn_id로 기입력된 doc파일 clear
					drnService.clearDoc(update);
					
					for (int i = 0; i < docData.length; i++) {

						String _doc_id = docData[i].getString("doc_id");
						String _rev_id = docData[i].getString("rev_id");
						String _reg_id = docData[i].getString("reg_id");
						String _reg_date = docData[i].getString("reg_date");
						long _folder_id = docData[i].getLong("folder_id");

						update.setDoc_id(_doc_id);
						update.setRev_id(_rev_id);
						update.setReg_id(reg_id);
						update.setReg_date(reg_date);
						update.setFolder_id(_folder_id);
						
						/*
						// db상에 존재하면 continue
						if(drnService.isExistDoc(update)) continue;
						*/
						
						if (drnService.insertDrnDocs(update) < 1) throw new Exception("DRN ATTACH FILE 삽입 실패");
						
						update.setReg_id(reg_id);
						// 해당 문서 체크아웃
						if( drnService.checkOutDoc(update) < 1 ) throw new Exception("해당 문서 check-out 실패");
						
					}
					
					if(file != null && file.getSize() > 0) {
						file.getName();
						
						System.out.println(file.getSize());
						
						
						
						String[] getExtension = file.getOriginalFilename().split("\\.");
						String sfile_nm = drn_id + "." + getExtension[1]; 
						update.setRfile_nm(file.getOriginalFilename());
						update.setSfile_nm(sfile_nm);
						update.setFile_size(file.getSize()+"");
						update.setFile_path(PCFILE_PATH + sfile_nm);
						update.setReg_id(reg_id);
						update.setReg_date(reg_date);
						// 임시저장때 저장된 파일이 있는지 확인
						DrnLineVO isUploadedPCDocs = drnService.isUploadedPCDocs(update);
						if(isUploadedPCDocs != null){ // 임시저장때 등록된 파일이 있음
							String org_nm = isUploadedPCDocs.getRfile_nm();
							if(!org_nm.equals(update.getRfile_nm())) { // 임시저장때의 파일과 다른 경우
								if(drnService.updateDrnPCDocs(update) < 1) throw new Exception("PC FILE 교체 실패");
							}
						}
						else if(drnService.insertDrnPCDocs(update) < 1) throw new Exception("PC FILE 첨부 실패");
						// 파일 업로드
						String path =  UPLOAD_PATH + update.getPrj_id() +PCFILE_PATH;
						File dir = new File(path);
						dir.mkdirs();
						File uploadTarget = new File(path + sfile_nm);
						
						file.transferTo(uploadTarget);
					} else if(localFileExistChk.equals("")) {
						// 로컬파일을 첨부하지 않음
						// 기 첨부된 파일,DB가 있다면 제거
						drnService.clearLocalFile(update);
					}
					
				} catch (Exception e) {
					System.out.println("[DOC ERROR]:" + e.getMessage());
					errors.add(e.getMessage());
				}
				
				try { // drn Info 업데이트 및 history 생성
					// drn no에 들어갈 seq no 생성
					// 같은 drn-no를 기준으로 1부터 차례로 증가
					String drnNo = update.getDrn_no();
					if(directInsert) { // update가 아닌 경우
						int seq = drnService.getDrnSeq(update.getDrn_no()+"%");
						drnNo += String.format("%03d", seq+1);
					}
 					 
					update.setDrn_no(drnNo);
					update.setStatus(status);
					update.setReg_date(reg_date);
					if (drnService.updateDrnInfo(update) < 1) throw new Exception("drn info 업데이트 실패");
					
					if (drnService.insertDrnHist(update) < 1) throw new Exception("drn history 삽입 실패");
					
					if (drnService.sendEmail(update) < 1) throw new Exception("Email 전송 실패");

				
				} catch (Exception e) {
					System.out.println(e.getMessage());
					errors.add("[FINAL STEP ERROR]:" + e.getMessage());
				}

			} catch (Exception e) {
				System.out.println("[DRN INFO INIT ERROR]:" + e.getStackTrace());

				errors.add(e.getMessage());
				result.addObject("errors", errors);
				return result;
			}

		} else { // 그 외의 모든 경우 해당 drn 업데이트, 히스토리는 insert
			
			try {
				
				update.setDrn_id(drn_id);
				update.setCheck_sheet_id(check_sheet_id);
				
				if(action.equals("C")) {
					// 최종승인 하기 전, TR커버가 있는지 체크
					ProcessInfoVO getInfo = new ProcessInfoVO();
					getInfo.setPrj_id(update.getPrj_id());
					getInfo.setFolder_id(folder_id);
					String doc_type_TRCOVER = drnService.getProcessType(getInfo);
					Boolean fileExists = false;
					if(doc_type_TRCOVER.equals("DCI")) {
						File f = new File(UPLOAD_PATH+update.getPrj_id()+TEMPLATE_PATH+prj_nm+"_p_tr.xls");
						if(f.exists()) {
							fileExists = true;
						}
					}else if(doc_type_TRCOVER.equals("VDCI")) {
						File vf = new File(UPLOAD_PATH+update.getPrj_id()+TEMPLATE_PATH+prj_nm+"_p_vtr.xls");
						if(vf.exists()) {
							fileExists = true;
						}
					}else if(doc_type_TRCOVER.equals("SDCI")) {
						File sf = new File(UPLOAD_PATH+update.getPrj_id()+TEMPLATE_PATH+prj_nm+"_p_str.xls");
						if(sf.exists()) {
							fileExists = true;
						}
					}
					if(!fileExists) {
						result.addObject("err_msg","Can't submit it because there is no TR cover.");
						return result;
					}
				}
				
				// DB상에서 체크시트 정보 가져오기
				List<DrnLineVO> getCheckSheetItems = drnService.getCheckSheetItemList(update);
				 
				
				int idx = 0; // 순차적 접근
				// drn페이지에서 가져온 업데이트 정보
				for (int i = 0; i < sheetData.length; i++) {
					
					JSONArray check_items_List = sheetData[i].getJSONArray("check_items");
					JSONArray discipline_code_id_List = sheetData[i].getJSONArray("discipline_code_id");
					JSONArray design_check_status_List = sheetData[i].getJSONArray("design_check_status");
					JSONArray reviewed_check_status_List = sheetData[i].getJSONArray("reviewed_check_status");
					JSONArray approved_check_status_List = sheetData[i].getJSONArray("approved_check_status");

					for (int j = 0; j < sheetData[i].getInt("items_cnt"); j++) {
						String check_items = check_items_List.getString(j);
						String discipline_code_id = discipline_code_id_List.getString(j);
						String design_check_status = (String) design_check_status_List.get(j);
						String reviewed_check_status = (String) reviewed_check_status_List.get(j);
						String approved_check_status = (String) approved_check_status_List.get(j);
						

						DrnLineVO row = getCheckSheetItems.get(idx++);
								
						// 모드에 따른 값 셋팅
						// row.setDesign_check_status(action<0 ? null :design_check_status);
						row.setDesign_check_status(design_check_status);
						row.setReviewed_check_status(reviewed_check_status);
						row.setApproved_check_status(approved_check_status);
						row.setItems_no(i+1);
						row.setItems_seq(j+1);
						
						
						if(drnService.updateCheckSheetItem(row) < 1) {
							throw new Exception(idx + "체크시트 아이템 업데이트 실패");
						}
							
					}

				}
				
				
			} catch (Exception e) {
				System.out.println("[CHECK SHEET ERROR]" + e.getMessage());
				errors.add(e.getMessage());
			}
			
			
			try {
				
				update.setStatus(status);
				update.setCheck_sheet_id(check_sheet_id);
				update.setDrn_id(drn_id);
				update.setMod_id(reg_id);
				
				if (drnService.updateDrnInfo2(update) < 1) throw new Exception("drn info 업데이트 실패");
				update.setReg_id(reg_id);

				if(action_id == null) update.setAction_id(reg_id); // action_id가 없으면 기안자
				if (drnService.insertDrnHist(update) < 1) throw new Exception("drn history 삽입 실패");
				
				String rejectReason = drnLineVO.getRejectReason();
				String comment = drnLineVO.getComment();
				// 반려사유 존재시 set
				if(rejectReason != null) update.setRejectReason(rejectReason);
				if(comment != null) update.setComment(comment);
				
				if (drnService.sendEmail(update) < 1) throw new Exception("Email 전송 실패");
				
			} catch(Exception e) {
				System.out.println(e.getMessage());
				errors.add("[FINAL STEP ERROR]:" + e.getMessage());
			}
			
			if(action.equals("R")) { // DRN 반려시 checkout 해제
				// DRN의 문서들 CHECKOUT 해제
				for (int i=0; i<docData.length; i++) {
 					DrnLineVO cancelCheck = new DrnLineVO();
 					String doc_id = docData[i].getString("doc_id");
 					String rev_id = docData[i].getString("rev_id");
 					cancelCheck.setPrj_id(update.getPrj_id());
 					cancelCheck.setDoc_id(doc_id);
 					cancelCheck.setRev_id(rev_id);
 					cancelCheck.setReg_id(reg_id);
 					cancelCheck.setReg_date(reg_date);
 					try {
 						if(drnService.cancelCheckOut(cancelCheck)<1)
 							throw new Exception("check out cancel 실패");
 					} catch(Exception e) {
 						
 					}
 				}
				
			}
			
			
 			if(action.equals("C")) { // DRN 최종승인
 				String tr_id_g = "";
 				DrnLineVO drnInfo4TR = drnService.getDrnInfo(update);
 				
 				status = drnInfo4TR.getStatus();
 				// DRN 최종승인 시, reg_date가 변경됨
 				// drnService.setRegDate(drnInfo4TR);
 				
 				for (int i=0; i<docData.length; i++) {
 					DrnLineVO cancelCheck = new DrnLineVO();
 					String doc_id = docData[i].getString("doc_id");
 					String rev_id = docData[i].getString("rev_id");
 					cancelCheck.setPrj_id(update.getPrj_id());
 					cancelCheck.setDoc_id(doc_id);
 					cancelCheck.setRev_id(rev_id);
 					cancelCheck.setReg_id(reg_id);
 					cancelCheck.setReg_date(reg_date);
 					try {
 						if(drnService.cancelCheckOut(cancelCheck)<1)
 							throw new Exception("check out cancel 실패");
 					} catch(Exception e) {
 						
 					}
 				}
 				
				ProcessInfoVO getInfo = new ProcessInfoVO();
				getInfo.setPrj_id(update.getPrj_id());
				getInfo.setFolder_id(folder_id);
				
				// 1) 문서의 ENG,VENDEOR,SITE 타입을 알아낸다.
				String doc_type = drnService.getProcessType(getInfo);
				String doc_type_TR = doc_type;
				String tbl_type = "p_prj_eng_step";
				String set_code_type = "";
				String set_val3 = "";
				if(doc_type.equals("DCI")) {
					doc_type = "ENGINEERING STEP%";
					tbl_type = "p_prj_eng_step";
					set_code_type = "TR ISSUE PURPOSE%";
				} else if(doc_type.equals("VDCI")) { //
					doc_type = "VENDOR DOC STEP%";
					tbl_type = "p_prj_vdr_step";
					set_code_type = "VTR ISSUE PURPOSE%";
				} else if(doc_type.equals("SDCI")) {
					doc_type = "SITE DOC STEP%";
					tbl_type = "p_prj_sdc_step";
					set_code_type = "STR ISSUE PURPOSE%";
				}
				
				
				
				//TR OUTGOING INSERT
				try {
				// 기안자 id 구하기
				String design_id = drnService.getDesigner(update);
				
				String currentDateAndTime = CommonConst.currentDateAndTime();
 				HttpSession session = request.getSession();
 				UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
				prjtrvo.setTr_writer_id(design_id);
				prjtrvo.setTr_modifier_id(design_id);
				prjtrvo.setReg_id(design_id);
				prjtrvo.setReg_date(currentDateAndTime);
				prjtrvo.setMod_id(design_id);
				prjtrvo.setMod_date(currentDateAndTime);
				prjstepvo.setMod_id(design_id);
				prjstepvo.setMod_date(currentDateAndTime);
				PrjInfoVO getPrjInfo = prjinfoService.getPrjInfo(prjinfovo);
				prjtrvo.setPrj_no(getPrjInfo.getPrj_no());
				prjtrvo.setInout("OUT");
				prjtrvo.setTr_from("HHI");
				prjtrvo.setTr_to("OWNER");
				prjtrvo.setChk_date(currentDateAndTime.substring(0,8));
		    	prjtrvo.setTr_writer_id(design_id);
		    	prjtrvo.setTr_status("Issued");
		    	prjtrvo.setSend_date(currentDateAndTime.substring(0,8));
				prjtrvo.setTr_subject(update.getDrn_no());
				prjtrvo.setTr_issuepur_code_id(status);
				
				discip_code_id = drnService.getDiscipCode(update);
				prjtrvo.setDiscipline_code_id(discip_code_id);
				
		    	
		    	FolderInfoVO folderinfovo = new FolderInfoVO();
		    	PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
		    	
		    	folderinfovo.setPrj_id(drnLineVO.getPrj_id());
				folderinfovo.setReg_id(design_id);
				folderinfovo.setReg_date(currentDateAndTime);
				prjdocumentindexvo.setPrj_id(drnLineVO.getPrj_id());
				prjdocumentindexvo.setReg_id(design_id);
				prjdocumentindexvo.setReg_date(currentDateAndTime);
				
				SimpleDateFormat dtFormat2 = new SimpleDateFormat("yyyyMMdd");
				Calendar cal2 = Calendar.getInstance();
				Date dt2 = dtFormat2.parse(currentDateAndTime.substring(0,8));
				cal2.setTime(dt2);
				cal2.add(Calendar.DATE, 14);
				String req_date = dtFormat2.format(cal2.getTime());
		    	prjtrvo.setReq_date(req_date);

				String set_code = "";
				if(doc_type_TR.equals("DCI")) {
	    			set_code = "TR OUTGOING";
				} else if(doc_type_TR.equals("VDCI")) {
	    			set_code = "VTR OUTGOING";
				} else if(doc_type_TR.equals("SDCI")) {
	    			set_code = "STR OUTGOING";
				}
				prjcodesettingsvo.setSet_code(set_code);
				prjcodesettingsvo.setSet_code_type("CORRESPONDENCE_OUT");
				prjtrvo.setSet_code(set_code);
				prjtrvo.setSet_code_type("CORRESPONDENCE_OUT");

				PrjCodeSettingsVO getTransmittalNoForm = prjcodesettingsService.getTransmittalNoForm(prjcodesettingsvo);
				String noForm = getTransmittalNoForm.getSet_val();
				//String numberForm = noForm.replaceAll("[^0-9]","");	//설정 포맷에서 숫자만 뽑음
				String[] noFormArray = noForm.split("-");
				String REGEX = "[0-9]+";
				String numberForm = "";
				for(int i=0;i<noFormArray.length;i++) {
					if(noFormArray[i].matches(REGEX)) {//문자열에 숫자만 있는 경우
						numberForm = noFormArray[i];
					}
				}
				prjtrvo.setSerialLength(numberForm.length());
				int startNumDigit = noForm.indexOf(numberForm);
				int endNumDigit = startNumDigit+numberForm.length();
				StringBuffer sb = new StringBuffer(noForm);
				String stringForm = sb.replace(startNumDigit,endNumDigit,"%").toString();	//숫자부분 %로 대체
				String replaceForm = stringForm.replace("%", "");
				prjtrvo.setStringForm(stringForm);
				prjtrvo.setReplaceForm(replaceForm);
				String serial = "";
				if(doc_type_TR.equals("DCI")) {
					serial = prjtrService.getTRSerialNo(prjtrvo);
				}else if(doc_type_TR.equals("VDCI")) {
					serial = prjtrService.getVTRSerialNo(prjtrvo);
				}else if(doc_type_TR.equals("SDCI")) {
					serial = prjtrService.getSTRSerialNo(prjtrvo);
				}
				if(serial==null || serial.equals("")) {
					serial = numberForm;
				}
				stringForm = stringForm.replace("%", serial);
				prjtrvo.setTr_no(stringForm);
				
				String tr_id = "";
				String doc_table = "";
				String tr_no = "";
				String tra_nm_prefix = "";
				PrjTrVO getTrInfo = new PrjTrVO();
				
				if(doc_type_TR.equals("DCI")) {
					tr_id = prjtrService.insertTrInfoForDRN(prjtrvo);
					tr_id_g = tr_id;
					doc_table = "DCI";
					getTrInfo = prjtrService.getTrInfo(prjtrvo);
					tr_no = getTrInfo.getTr_no();
					folderinfovo.setTra_folder_nm("TROOT");
					tra_nm_prefix = "TROOT&&";
				} else if(doc_type_TR.equals("VDCI")) {
					tr_id = prjtrService.insertVTrInfoForDRN(prjtrvo);
					tr_id_g = tr_id;
					doc_table = "VDCI";
					getTrInfo = prjtrService.getVTrInfo(prjtrvo);
					tr_no = getTrInfo.getTr_no();
					folderinfovo.setTra_folder_nm("VROOT");
					tra_nm_prefix = "VROOT&&";
				} else if(doc_type_TR.equals("SDCI")) {
					tr_id = prjtrService.insertSTrInfoForDRN(prjtrvo);
					tr_id_g = tr_id;
					doc_table = "SDCI";
					getTrInfo = prjtrService.getSTrInfo(prjtrvo);
					tr_no = getTrInfo.getTr_no();
					folderinfovo.setTra_folder_nm("SROOT");
					tra_nm_prefix = "SROOT&&";
				}
				prjtrvo.setTr_id(tr_id);
				prjstepvo.setTr_id(tr_id);
		    	prjtrvo.setDoc_table(doc_table);
		    	
				//Sysmtem Link Transmittal에 연월로 해당 첨부문서를 집어넣을 폴더를 생성
				String folder_nm = currentDateAndTime.substring(0, 4) + "-" + currentDateAndTime.substring(4, 6);
				FolderInfoVO getTrootFolderInfo = treeService.getTrootFolderInfo(folderinfovo);
				folderinfovo.setParent_folder_id(getTrootFolderInfo.getFolder_id());
				folderinfovo.setFolder_path(getTrootFolderInfo.getFolder_path() + ">");
				folderinfovo.setFolder_nm(folder_nm);
				folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,6));
				FolderInfoVO getFolderInfoFromTRAFolderNm_YYYYMM = treeService.getFolderInfoFromTRAFolderNm(folderinfovo);
				if(getFolderInfoFromTRAFolderNm_YYYYMM!=null) {	//해당 연월 폴더가 이미 있으면 그 폴더의 정보를 가져옴
					getFolderInfoFromTRAFolderNm_YYYYMM.getFolder_path();
					folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,8));
					FolderInfoVO getFolderInfoFromTRAFolderNm_YYYYMMDD = treeService.getFolderInfoFromTRAFolderNm(folderinfovo);
					if(getFolderInfoFromTRAFolderNm_YYYYMMDD!=null){//해당 연월일 폴더가 있으면 그 폴더의 정보를 가져옴
						folderinfovo.setFolder_id(getFolderInfoFromTRAFolderNm_YYYYMMDD.getFolder_id());
						FolderInfoVO getFolderInfo_YYYYMMDD = treeService.getFolderInfo(folderinfovo);
						folderinfovo.setParent_folder_id(getFolderInfo_YYYYMMDD.getFolder_id());
						folderinfovo.setFolder_path(getFolderInfo_YYYYMMDD.getFolder_path() + ">");
						folderinfovo.setFolder_nm(tr_no);
						folderinfovo.setTra_folder_nm(tra_nm_prefix+tr_no);
						long tr_folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
						folderinfovo.setFolder_id(tr_folder_id);
						prjdocumentindexvo.setFolder_id(tr_folder_id);
					}else {	//해당 연월일 폴더가 없을 경우 폴더 생성
						folderinfovo.setParent_folder_id(getFolderInfoFromTRAFolderNm_YYYYMM.getFolder_id());
						folderinfovo.setFolder_path(getFolderInfoFromTRAFolderNm_YYYYMM.getFolder_path() + ">");
						folderinfovo.setFolder_nm(currentDateAndTime.substring(0, 4) + "-" + currentDateAndTime.substring(4, 6) + "-" + currentDateAndTime.substring(6, 8));
						folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,8));
						long folder_id_1 = treeService.insertTRAFolderInfoVO(folderinfovo);
						folderinfovo.setFolder_id(folder_id_1);
						FolderInfoVO getFolderInfo_YYYYMMDD = treeService.getFolderInfo(folderinfovo);
						folderinfovo.setParent_folder_id(getFolderInfo_YYYYMMDD.getFolder_id());
						folderinfovo.setFolder_path(getFolderInfo_YYYYMMDD.getFolder_path() + ">");
						folderinfovo.setFolder_nm(tr_no);
						folderinfovo.setTra_folder_nm(tra_nm_prefix+tr_no);
						long tr_folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
						folderinfovo.setFolder_id(tr_folder_id);
						prjdocumentindexvo.setFolder_id(tr_folder_id);
					}
				}else {	//해당 연월 폴더가 없을 경우 폴더 생성
					long folder_id_1 = treeService.insertTRAFolderInfoVO(folderinfovo);
					folderinfovo.setFolder_id(folder_id_1);
					FolderInfoVO getFolderInfo_YYYYMM = treeService.getFolderInfo(folderinfovo);
					folderinfovo.setParent_folder_id(getFolderInfo_YYYYMM.getFolder_id());
					folderinfovo.setFolder_path(getFolderInfo_YYYYMM.getFolder_path() + ">");
					folderinfovo.setFolder_nm(currentDateAndTime.substring(0, 4) + "-" + currentDateAndTime.substring(4, 6) + "-" + currentDateAndTime.substring(6, 8));
					folderinfovo.setTra_folder_nm(tra_nm_prefix+currentDateAndTime.substring(0,8));
					long folder_id_new = treeService.insertTRAFolderInfoVO(folderinfovo);
					folderinfovo.setFolder_id(folder_id_new);
					FolderInfoVO getFolderInfo_YYYYMMDD = treeService.getFolderInfo(folderinfovo);
					folderinfovo.setParent_folder_id(getFolderInfo_YYYYMMDD.getFolder_id());
					folderinfovo.setFolder_path(getFolderInfo_YYYYMMDD.getFolder_path() + ">");
					folderinfovo.setFolder_nm(tr_no);
					folderinfovo.setTra_folder_nm(tra_nm_prefix+tr_no);
					long tr_folder_id = treeService.insertTRAFolderInfoVO(folderinfovo);
					folderinfovo.setFolder_id(tr_folder_id);
					prjdocumentindexvo.setFolder_id(tr_folder_id);
				}
				
				
				
				try {
					for (int i=0; i<docData.length; i++) {
						String doc_id = docData[i].getString("doc_id");
						String rev_id = docData[i].getString("rev_id");
						
						DrnLineVO getRevNo = new DrnLineVO();
						getRevNo.setPrj_id(drnLineVO.getPrj_id());
						getRevNo.setDoc_id(doc_id);
						getRevNo.setRev_id(rev_id);
						
						String rev_no = drnService.getRevNo(getRevNo);
						
						prjtrvo.setRev_no(rev_no);
						prjtrvo.setDoc_id(doc_id);
						prjtrvo.setRev_id(rev_id);
						if(doc_type_TR.equals("DCI")) {							
							int insertTrFile = prjtrService.insertTrFile(prjtrvo);
						} else if(doc_type_TR.equals("VDCI")) {
							int insertTrFile = prjtrService.insertVTrFile(prjtrvo);
						} else if(doc_type_TR.equals("SDCI")) {
							int insertTrFile = prjtrService.insertSTrFile(prjtrvo);
						}
					}

				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("DRN승인시 - insertTrOutgoing: {}");
				}

				List<PrjTrVO> getTrFile = new ArrayList<PrjTrVO>();

				if(doc_type_TR.equals("DCI")) {
					getTrFile = prjtrService.getTrFile(prjtrvo);
				} else if(doc_type_TR.equals("VDCI")) {
					getTrFile = prjtrService.getVTrFile(prjtrvo);
				} else if(doc_type_TR.equals("SDCI")) {
					getTrFile = prjtrService.getSTrFile(prjtrvo);
				}
				for(int i=0;i<getTrFile.size();i++) {
					prjdocumentindexvo.setDoc_id(getTrFile.get(i).getDoc_id());
					prjdocumentindexvo.setRev_id(getTrFile.get(i).getRev_id());
					int insertCopyDocumentIndex = prjdocumentindexService.insertCopyDocumentIndex(prjdocumentindexvo);
				}

				/////TR COVER 생성
				PrjInfoVO getPrjInfo2 = prjinfoService.getPrjInfo(prjinfovo);
				String trfilename = "";
				if(doc_type_TR.equals("DCI")) {
					trfilename = UPLOAD_PATH+prjtrvo.getPrj_id()+TEMPLATE_PATH+getPrjInfo2.getPrj_nm()+"_p_tr.xls";
				} else if(doc_type_TR.equals("VDCI")) {
					trfilename = UPLOAD_PATH+prjtrvo.getPrj_id()+TEMPLATE_PATH+getPrjInfo2.getPrj_nm()+"_p_vtr.xls";
				} else if(doc_type_TR.equals("SDCI")) {
					trfilename = UPLOAD_PATH+prjtrvo.getPrj_id()+TEMPLATE_PATH+getPrjInfo2.getPrj_nm()+"_p_str.xls";
				}
				InputStream inp = new FileInputStream(trfilename);
				Workbook wb = WorkbookFactory.create(inp);
				Sheet sheet = wb.getSheetAt(0);
				//J7 DATE
				Row row = sheet.getRow(6);
				Cell cell = row.getCell(9);
				if(cell==null) cell = row.createCell(3);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(currentDateAndTime.substring(0, 4)+"-"+currentDateAndTime.substring(4, 6)+"-"+currentDateAndTime.substring(6, 8));
				//E11 TR NO
				Row row2 = sheet.getRow(10);
				Cell cell2 = row2.getCell(4);
				cell2.setCellType(Cell.CELL_TYPE_STRING);
				cell2.setCellValue(tr_no);
				//K11 Discipline
				Row row3 = sheet.getRow(10);
				Cell cell3 = row3.getCell(10);
				cell3.setCellType(Cell.CELL_TYPE_STRING);
				cell3.setCellValue(getTrInfo.getDiscipline());
				//C13 Subject
				Row row4 = sheet.getRow(12);
				Cell cell4 = row4.getCell(2);
				cell4.setCellType(Cell.CELL_TYPE_STRING);
				cell4.setCellValue(getTrInfo.getTr_subject());
				//A17 ISSUE CODE SET
				int issueRow = 0;
				int issueCell = 0;
				String issueText = "";
				String issueDesc = "";
				issueText = getTrInfo.getIssuepurpose();
				issueDesc = getTrInfo.getIssuepurpose_desc();
				if(doc_type_TR.equals("DCI")) {
					Row row5 = sheet.getRow(16);
					Cell cell5 = row5.getCell(2);
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					cell5.setCellValue(issueText+"("+issueDesc+")");
				}else if(doc_type_TR.equals("VDCI")) {
					Row row5 = sheet.getRow(16);
					Cell cell5 = row5.getCell(2);
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					cell5.setCellValue(issueText+"("+issueDesc+")");
				}else if(doc_type_TR.equals("SDCI")) {
					Row row5 = sheet.getRow(12);
					Cell cell5 = row5.getCell(2);
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					cell5.setCellValue(issueText+"("+issueDesc+")");
				}
				//A22~ 첨부된 Doc 문서 정보 리스트 보여주기
				int docListStartRow = 21;
				int docListStartCell = 0;
				if(doc_type_TR.equals("SDCI")) {
					docListStartRow = 15;
				}
				for(int i=0;i<getTrFile.size();i++) {
					prjdocumentindexvo.setDoc_id(getTrFile.get(i).getDoc_id());
					prjdocumentindexvo.setRev_id(getTrFile.get(i).getRev_id());
					PrjDocumentIndexVO getDocumentInfo = new PrjDocumentIndexVO();
					if(doc_type_TR.equals("DCI")) {
						getDocumentInfo = prjdocumentindexService.getDocumentInfo(prjdocumentindexvo);
					}else if(doc_type_TR.equals("VDCI")) {
						getDocumentInfo = prjdocumentindexService.getVendorDocumentInfo(prjdocumentindexvo);
					}else if(doc_type_TR.equals("SDCI")) {
						getDocumentInfo = prjdocumentindexService.getSiteDocumentInfo(prjdocumentindexvo);
					}
					if(doc_type_TR.equals("DCI") || doc_type_TR.equals("VDCI")) {
						//No.
						Row docRow = sheet.getRow(docListStartRow);
						Cell cell_A = docRow.getCell(docListStartCell);
						cell_A.setCellType(Cell.CELL_TYPE_STRING);
						cell_A.setCellValue(i+1);
						//Drawing and Document No.
						Cell cell_B = docRow.getCell(docListStartCell+1);
						cell_B.setCellType(Cell.CELL_TYPE_STRING);
						cell_B.setCellValue(getDocumentInfo.getDoc_no());
						//Rev. No.
						Cell cell_C = docRow.getCell(docListStartCell+2);
						cell_C.setCellType(Cell.CELL_TYPE_STRING);
						cell_C.setCellValue(getDocumentInfo.getRev_no());
						//Category
						Cell cell_D = docRow.getCell(docListStartCell+3);
						cell_D.setCellType(Cell.CELL_TYPE_STRING);
						cell_D.setCellValue(getDocumentInfo.getCategory_code());
						//Size
						Cell cell_E = docRow.getCell(docListStartCell+4);
						cell_E.setCellType(Cell.CELL_TYPE_STRING);
						cell_E.setCellValue(getDocumentInfo.getSize_code());
						//Description/Title
						Cell cell_F = docRow.getCell(docListStartCell+5);
						cell_F.setCellType(Cell.CELL_TYPE_STRING);
						cell_F.setCellValue(getDocumentInfo.getTitle());
						//Remark
						Cell cell_M = docRow.getCell(docListStartCell+12);
						cell_M.setCellType(Cell.CELL_TYPE_STRING);
						cell_M.setCellValue(getDocumentInfo.getRemark());
					}else if(doc_type_TR.equals("SDCI")) {
						//No.
						Row docRow = sheet.getRow(docListStartRow);
						Cell cell_A = docRow.getCell(docListStartCell);
						cell_A.setCellType(Cell.CELL_TYPE_STRING);
						cell_A.setCellValue(i+1);
						//Document No.
						Cell cell_B = docRow.getCell(docListStartCell+1);
						cell_B.setCellType(Cell.CELL_TYPE_STRING);
						cell_B.setCellValue(getDocumentInfo.getDoc_no());
						//Rev. No.
						Cell cell_C = docRow.getCell(docListStartCell+2);
						cell_C.setCellType(Cell.CELL_TYPE_STRING);
						cell_C.setCellValue(getDocumentInfo.getRev_no());
						//Size
						Cell cell_E = docRow.getCell(docListStartCell+3);
						cell_E.setCellType(Cell.CELL_TYPE_STRING);
						cell_E.setCellValue(getDocumentInfo.getSize_code());
						//Description/Title
						Cell cell_F = docRow.getCell(docListStartCell+4);
						cell_F.setCellType(Cell.CELL_TYPE_STRING);
						cell_F.setCellValue(getDocumentInfo.getTitle());
						//Design Charger
						Cell cell_M = docRow.getCell(docListStartCell+12);
						cell_M.setCellType(Cell.CELL_TYPE_STRING);
						cell_M.setCellValue(getDocumentInfo.getRemark());
					}

					if(docListStartRow==30) {
						docListStartRow=45;//2페이지로 넘김
						//E45(2페이지) TR_no
						Row page2_trno_row = sheet.getRow(44);
						Cell trno_cell = page2_trno_row.getCell(4);
						trno_cell.setCellType(Cell.CELL_TYPE_STRING);
						trno_cell.setCellValue(getTrInfo.getTr_no());
					}
					if(docListStartRow==75) {
						docListStartRow=79;//3페이지로 넘김
						//E79(3페이지) TR_no
						Row page3_trno_row = sheet.getRow(78);
						Cell trno_cell = page3_trno_row.getCell(4);
						trno_cell.setCellType(Cell.CELL_TYPE_STRING);
						trno_cell.setCellValue(getTrInfo.getTr_no());
					}
					if(docListStartRow==109) {
						docListStartRow=113;//4페이지로 넘김
						//E113(4페이지) TR_no
						Row page4_trno_row = sheet.getRow(112);
						Cell trno_cell = page4_trno_row.getCell(4);
						trno_cell.setCellType(Cell.CELL_TYPE_STRING);
						trno_cell.setCellValue(getTrInfo.getTr_no());
					}
					if(docListStartRow==143) {
						docListStartRow=147;//5페이지로 넘김
						//E147(5페이지) TR_no
						Row page5_trno_row = sheet.getRow(146);
						Cell trno_cell = page5_trno_row.getCell(4);
						trno_cell.setCellType(Cell.CELL_TYPE_STRING);
						trno_cell.setCellValue(getTrInfo.getTr_no());
					}
					
					if(doc_type_TR.contains("VDCI")) {
						//VTR일 경우 C42에 HPS Internal Distribution
					}else if(doc_type_TR.contains("SDCI")) {
					}
					docListStartRow++;
				}

				if(doc_type_TR.equals("DCI") || doc_type_TR.equals("VDCI")) {
					//A33 TR Remarks
					Row row5 = sheet.getRow(32);
					Cell cell5 = row5.getCell(2);
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					cell5.setCellValue(getTrInfo.getTr_remark());
				}else if(doc_type_TR.equals("SDCI")) {
					//A27 TR Remarks
					Row row5 = sheet.getRow(26);
					Cell cell5 = row5.getCell(0);
					cell5.setCellType(Cell.CELL_TYPE_STRING);
					cell5.setCellValue(getTrInfo.getTr_remark());
				}
				
				String replaceTrNo = getTrInfo.getTr_no().replaceAll("/","-").replaceAll("%","-");
				prjdocumentindexvo.setDoc_no(getTrInfo.getTr_no());
				prjdocumentindexvo.setTitle(getTrInfo.getTr_subject());
				prjdocumentindexvo.setFile_type("xls");
				prjdocumentindexvo.setFile_size(0);
				prjdocumentindexvo.setAttach_file_date(currentDateAndTime);
				prjdocumentindexvo.setRev_code_id("00000");
				prjdocumentindexvo.setDoc_type("General");
				prjdocumentindexvo.setSfile_nm(replaceTrNo+".xls");
				prjdocumentindexvo.setRfile_nm(replaceTrNo+".xls");
				String doc_id = prjdocumentindexService.insertDocumentIndexTRCover(prjdocumentindexvo);
				// Write the output to a file

				String uploadPath = UPLOAD_PATH+ prjtrvo.getPrj_id() + DOC_PATH;
				File dir = new File(uploadPath);
				dir.mkdirs();
				FileOutputStream fileOut = new FileOutputStream(uploadPath + replaceTrNo +".xls");
				wb.write(fileOut);
				fileOut.close();
				/////TR COVER 생성 및 DB 저장 완료

				} catch(Exception e) {
					System.out.print("[TR Error]:");
					e.printStackTrace();					
				}

				String design_id = drnService.getDesigner(update);
				String currentDateAndTime = CommonConst.currentDateAndTime();
				
				prjstepvo.setSet_val2("DRN 승인시");
				prjstepvo.setPrj_id(drnLineVO.getPrj_id());
				prjstepvo.setMod_id(design_id);
				prjstepvo.setMod_date(currentDateAndTime);
				prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
				// doc_type에 따라 분기 태워야함 (ENG,VENDOR,Site.. )
				for (int i=0; i<docData.length; i++) {
					String doc_id = docData[i].getString("doc_id");
					String rev_id = docData[i].getString("rev_id");
					prjstepvo.setDoc_id(doc_id);
					prjstepvo.setRev_id(rev_id);
					
					if(doc_type_TR.equals("DCI")) {
						int updateActualDateByTRDRNAPPROVAL = prjstepService.updateActualDateByTRDRNAPPROVAL(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateEngStepCodeId = prjstepService.getUpdateEngStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateEngStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateEngStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateEngStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
							if(!getForeDateRuleByEngStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByEngStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
								PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
								if(getActualDateEngStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					} else if(doc_type_TR.equals("VDCI")) {
						int updateActualDateByVTRDRNAPPROVAL = prjstepService.updateActualDateByVTRDRNAPPROVAL(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = prjstepService.getUpdateVendorStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateVendorStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateVendorStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateVendorStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
							if(!getForeDateRuleByVendorStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
								PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
								if(getActualDateVendorStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					} else if(doc_type_TR.equals("SDCI")) {
						int updateActualDateBySTRDRNAPPROVAL = prjstepService.updateActualDateBySTRDRNAPPROVAL(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = prjstepService.getUpdateSiteStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateSiteStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateSiteStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateSiteStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
							if(!getForeDateRuleBySiteStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
								PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
								if(getActualDateSiteStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}
				}
				
				
				
				// 2) DRN 상신시 처리해야하는 STEP이 있는지 CODE테이블에서 검색
				/*System.out.println(doc_type);
				
				String code_type_target = "";
				if(doc_type.contains("ENGINEERING STEP")) {
					code_type_target = "ENGINEERING STEP";
				} else if(doc_type.contains("VENDOR DOC STEP")) {
					code_type_target = "VENDOR DOC STEP";
				} else if(doc_type.contains("SITE DOC STEP")) {
					code_type_target = "SITE DOC STEP";
				}
				
				prjstepvo.setSet_code_type(code_type_target);
				 // 2-1) STEP코드 검색
				List<PrjCodeSettingsVO> getCodeList = drnService.getStep(prjstepvo);
				 // set_val2 부분(조건) DRN 승인시 처리해야하는 코드를 검색 및 처리
				for(PrjCodeSettingsVO code : getCodeList) {
					if (code.getSet_val2().equals("DRN 승인시")) { // DRN 승인시로 셋팅되어있는 경우 진행
						
						// set_val3 부분에 다른 값이 들어가있는지 확인
						String target = code.getSet_code_id(); // 기본적으로는 자기 자신
						String code_set_val3 = code.getSet_val3();
						boolean useForeDate = false;
						if(code_set_val3 != null) {
							// 공백이 들어가 있는 경우 actual date만 업데이트
							if(code_set_val3.equals("")) {
								
							}else if(code_set_val3.contains("RETURN STATUS")) {
								
							}else if(code_set_val3.length() == 5) { // 5자리의 코드 아이디를 물고 있는 경우
								// fore_date 업데이트
								useForeDate = true;
							}
						}
						
						
						
						String actual_date = reg_date.substring(0, 8);

						SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

						Calendar cal = Calendar.getInstance();
						Date dt = dtFormat.parse(actual_date);
						cal.setTime(dt);

						// 해당 DRN에 첨부된 모든 DOC들에 대하여 처리
						try {
							for (int i = 0; i < docData.length; i++) {
								String getDoc_id = docData[i].getString("doc_id");
								String getRev_id = docData[i].getString("rev_id");
								code.setTbl(tbl_type);	
								code.setDoc_id(getDoc_id);
								code.setRev_id(getRev_id);

								// actual date + set_val4
								String actualDateFromTarget;
								if(useForeDate) {
									code.setSet_code(code_set_val3);
									actualDateFromTarget = drnService.getActualDate(code);
									Date fore_dt = dtFormat.parse(actualDateFromTarget);
									cal.setTime(fore_dt);
									cal.add(Calendar.DATE, Integer.parseInt(code.getSet_val4()));									
								}
									
								String fore_date = dtFormat.format(cal.getTime());
								code.setActual_date(actual_date);
								// fore_date를 업데이트 해야되는 경우에만 매핑
								code.setFore_date(null);
								if(useForeDate) 
									code.setFore_date(fore_date);
								
								code.setMod_id(reg_id);
								code.setMod_date(reg_date);
								
								code.setSet_code_id(code.getSet_code_id());
								
								if (drnService.engStep(code) < 1)
									throw new Exception(code.getTbl() + " : update 실패");

								
								
							}

						} catch (Exception e) {
							System.out.println(e.getMessage());
							errors.add("[STEP UPDATE ERROR]:" + e.getMessage());
						}
					}
					
				}*/

				PrjStepVO prjstep = new PrjStepVO();
				prjstep.setPrj_id(drnLineVO.getPrj_id());
				prjstep.setTr_id(tr_id_g);
				prjstep.setMod_id(design_id);
				prjstep.setMod_date(currentDateAndTime);
				for (int i = 0; i < docData.length; i++) {
					String getDoc_id = docData[i].getString("doc_id");
					String getRev_id = docData[i].getString("rev_id");
					prjstepvo.setDoc_id(getDoc_id);
					prjstepvo.setRev_id(getRev_id);
					prjstepvo.setActual_date(currentDateAndTime.substring(0, 8));
					//2022-04-12 소진희 작성
					//TR ISSUE시 Actual Date 업데이트 -> 없애기로 함(2022-04-22)
					/*if(doc_type_TR.equals("DCI")) {
						PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
						prjstepvo.setSet_val2("TR ISSUE시");
						int updateEngStepActualDateByMailSending = prjstepService.updateEngStepActualDateByMailSending(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateEngStepCodeId = prjstepService.getUpdateEngStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateEngStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateEngStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateEngStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
							if(!getForeDateRuleByEngStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByEngStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
								PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
								if(getActualDateEngStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}else if(doc_type_TR.equals("VDCI")){
						PrjTrVO getTrInfo2 = prjtrService.getVTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
						prjstepvo.setSet_val2("VTR ISSUE시");
						int updateVendorStepActualDateByMailSending = prjstepService.updateVendorStepActualDateByMailSending(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = prjstepService.getUpdateVendorStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateVendorStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateVendorStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateVendorStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
							if(!getForeDateRuleByVendorStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
								PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
								if(getActualDateVendorStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}else if(doc_type_TR.equals("SDCI")){
						PrjTrVO getTrInfo2 = prjtrService.getSTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
						prjstepvo.setSet_val2("STR ISSUE시");
						int updateSiteStepActualDateByMailSending = prjstepService.updateSiteStepActualDateByMailSending(prjstepvo);
						//ForecastDate 업데이트
						List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = prjstepService.getUpdateSiteStepCodeId(prjstepvo);
						for(int k=0;k<getUpdateSiteStepCodeId.size();k++) {
							prjstepvo.setSet_code_id(getUpdateSiteStepCodeId.get(k).getSet_code_id());
							prjstepvo.setSet_val3(getUpdateSiteStepCodeId.get(k).getSet_code_id());
							PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
							if(!getForeDateRuleBySiteStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4())!=0) {
								SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
								Calendar cal = Calendar.getInstance();
								prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
								PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
								if(getActualDateSiteStepByStepCodeId!=null) {
									Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
									cal.setTime(dt);
									if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
										cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
										prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
										prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
									}
								}
							}
						}
					}*/
					

					//2022-04-22 소진희 작성
					//TR OUTGOING ISSUE시 Send Date로 Actual Date를 업데이트 (IFC, IAB 제외)
					if(doc_type_TR.equals("DCI")) {
						PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setActual_date(getTrInfo2.getSend_date());
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
						if(!getCodeByTrIssuePurpose.getSet_code().equals("IFC") && !getCodeByTrIssuePurpose.getSet_code().equals("IAB")) {
							prjstepvo.setSet_val2("TR OUTGOING ISSUE시 Send Date");
							int updateEngStepActualDateByMailSending = prjstepService.updateEngStepActualDateByMailSending(prjstepvo);
							//ForecastDate 업데이트
							List<PrjCodeSettingsVO> getUpdateEngStepCodeId = prjstepService.getUpdateEngStepCodeId(prjstepvo);
							for(int k=0;k<getUpdateEngStepCodeId.size();k++) {
								prjstepvo.setSet_code_id(getUpdateEngStepCodeId.get(k).getSet_code_id());
								prjstepvo.setSet_val3(getUpdateEngStepCodeId.get(k).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
								if(!getForeDateRuleByEngStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByEngStep.getSet_val4())!=0) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
									PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
									if(getActualDateEngStepByStepCodeId!=null) {
										Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
										cal.setTime(dt);
										if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
											cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
											prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
											prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
										}
									}
								}
							}
						}
					}else if(doc_type_TR.equals("VDCI")){
						PrjTrVO getTrInfo2 = prjtrService.getVTrInfo(prjtrvo);
						prjstepvo.setActual_date(getTrInfo2.getSend_date());
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
						if(!getCodeByVTrIssuePurpose.getSet_code().equals("IFC") && !getCodeByVTrIssuePurpose.getSet_code().equals("IAB")) {
							prjstepvo.setSet_val2("VTR OUTGOING ISSUE시 Send Date");
							int updateVendorStepActualDateByMailSending = prjstepService.updateVendorStepActualDateByMailSending(prjstepvo);
							//ForecastDate 업데이트
							List<PrjCodeSettingsVO> getUpdateVendorStepCodeId = prjstepService.getUpdateVendorStepCodeId(prjstepvo);
							for(int k=0;k<getUpdateVendorStepCodeId.size();k++) {
								prjstepvo.setSet_code_id(getUpdateVendorStepCodeId.get(k).getSet_code_id());
								prjstepvo.setSet_val3(getUpdateVendorStepCodeId.get(k).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
								if(!getForeDateRuleByVendorStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4())!=0) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
									PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
									if(getActualDateVendorStepByStepCodeId!=null) {
										Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
										cal.setTime(dt);
										if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
											cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
											prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
											prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
										}
									}
								}
							}
						}
					}else if(doc_type_TR.equals("SDCI")){
						PrjTrVO getTrInfo2 = prjtrService.getSTrInfo(prjtrvo);
						prjstepvo.setActual_date(getTrInfo2.getSend_date());
						prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
						if(!getCodeBySTrIssuePurpose.getSet_code().equals("IFC") && !getCodeBySTrIssuePurpose.getSet_code().equals("IAB")) {
							prjstepvo.setSet_val2("STR OUTGOING ISSUE시 Send Date");
							int updateSiteStepActualDateByMailSending = prjstepService.updateSiteStepActualDateByMailSending(prjstepvo);
							//ForecastDate 업데이트
							List<PrjCodeSettingsVO> getUpdateSiteStepCodeId = prjstepService.getUpdateSiteStepCodeId(prjstepvo);
							for(int k=0;k<getUpdateSiteStepCodeId.size();k++) {
								prjstepvo.setSet_code_id(getUpdateSiteStepCodeId.get(k).getSet_code_id());
								prjstepvo.setSet_val3(getUpdateSiteStepCodeId.get(k).getSet_code_id());
								PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
								if(!getForeDateRuleBySiteStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4())!=0) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
									PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
									if(getActualDateSiteStepByStepCodeId!=null) {
										Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
										cal.setTime(dt);
										if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
											cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
											prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
											prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
										}
									}
								}
							}
						}
					}
					

					//2022-04-21 소진희 작성
					//IFC ISSUE시 Send Date로 Actual Date 업데이트
					if(doc_type_TR.equals("DCI")){
						PrjTrVO getTrInfo3 = prjtrService.getTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo3.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByTrIssuePurpose = prjstepService.getCodeByTrIssuePurpose(prjstepvo);
						if(getCodeByTrIssuePurpose.getSet_code().equals("IFC")) {	//IFC ISSUE PURPORSE를 선택했을 경우
							PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
							prjstepvo.setActual_date(getTrInfo2.getSend_date());
							prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
							PrjCodeSettingsVO ifcCodeYNVO = new PrjCodeSettingsVO();
							ifcCodeYNVO.setPrj_id(getTrInfo2.getPrj_id());
							ifcCodeYNVO.setSet_code_type("ENGINEERING STEP");
							ifcCodeYNVO.setSet_val2("IFC ISSUE시 Send Date");
							PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = prjcodesettingsService.isIFCStepYAndActualDateUpdateByIFCIssue(ifcCodeYNVO);
							if(isIFCStepYAndActualDateUpdateByIFCIssue!=null) {
								prjstepvo.setStep_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
								int updateEngStepActualDateByIFCIssue = prjstepService.updateEngStepActualDateByIFCIssue(prjstepvo);
								//ForecastDate 업데이트
								prjstepvo.setSet_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
								prjstepvo.setSet_val3(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_val3());
								PrjCodeSettingsVO getForeDateRuleByEngStep = prjstepService.getForeDateRuleByEngStep(prjstepvo);
								if(!getForeDateRuleByEngStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByEngStep.getSet_val4())!=0) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									prjstepvo.setStep_code_id(getForeDateRuleByEngStep.getSet_val3());
									PrjStepVO getActualDateEngStepByStepCodeId = prjstepService.getActualDateEngStepByStepCodeId(prjstepvo);
									if(getActualDateEngStepByStepCodeId!=null) {
										Date dt = dtFormat.parse(getActualDateEngStepByStepCodeId.getActual_date());
										cal.setTime(dt);
										if(!getForeDateRuleByEngStep.getSet_val4().equals("")) {
											cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByEngStep.getSet_val4()));
											prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
											prjstepService.updateEngStepForeDateByStepActualDateUpdate(prjstepvo);
										}
									}
								}
							}
						}
					}else if(doc_type_TR.equals("VDCI")){	
						PrjTrVO getTrInfo3 = prjtrService.getVTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo3.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeByVTrIssuePurpose = prjstepService.getCodeByVTrIssuePurpose(prjstepvo);
						if(getCodeByVTrIssuePurpose.getSet_code().equals("IFC")) {	//IFC ISSUE PURPORSE를 선택했을 경우
							PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
							prjstepvo.setActual_date(getTrInfo2.getSend_date());
							prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
							PrjCodeSettingsVO ifcCodeYNVO = new PrjCodeSettingsVO();
							ifcCodeYNVO.setPrj_id(getTrInfo2.getPrj_id());
							ifcCodeYNVO.setSet_code_type("VENDOR DOC STEP");
							ifcCodeYNVO.setSet_val2("IFC ISSUE시 Send Date");
							PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = prjcodesettingsService.isIFCStepYAndActualDateUpdateByIFCIssue(ifcCodeYNVO);
							if(isIFCStepYAndActualDateUpdateByIFCIssue!=null) {
								prjstepvo.setStep_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
								int updateVendorStepActualDateByIFCIssue = prjstepService.updateVendorStepActualDateByIFCIssue(prjstepvo);
								//ForecastDate 업데이트
								prjstepvo.setSet_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
								prjstepvo.setSet_val3(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_val3());
								PrjCodeSettingsVO getForeDateRuleByVendorStep = prjstepService.getForeDateRuleByVendorStep(prjstepvo);
								if(!getForeDateRuleByVendorStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4())!=0) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									prjstepvo.setStep_code_id(getForeDateRuleByVendorStep.getSet_val3());
									PrjStepVO getActualDateVendorStepByStepCodeId = prjstepService.getActualDateVendorStepByStepCodeId(prjstepvo);
									if(getActualDateVendorStepByStepCodeId!=null) {
										Date dt = dtFormat.parse(getActualDateVendorStepByStepCodeId.getActual_date());
										cal.setTime(dt);
										if(!getForeDateRuleByVendorStep.getSet_val4().equals("")) {
											cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleByVendorStep.getSet_val4()));
											prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
											prjstepService.updateVendorStepForeDateByStepActualDateUpdate(prjstepvo);
										}
									}
								}
							}
						}
					}else if(doc_type_TR.equals("SDCI")){
						PrjTrVO getTrInfo3 = prjtrService.getSTrInfo(prjtrvo);
						prjstepvo.setSet_code_id(getTrInfo3.getTr_issuepur_code_id());
						PrjCodeSettingsVO getCodeBySTrIssuePurpose = prjstepService.getCodeBySTrIssuePurpose(prjstepvo);
						if(getCodeBySTrIssuePurpose.getSet_code().equals("IFC")) {	//IFC ISSUE PURPORSE를 선택했을 경우
							PrjTrVO getTrInfo2 = prjtrService.getTrInfo(prjtrvo);
							prjstepvo.setActual_date(getTrInfo2.getSend_date());
							prjstepvo.setSet_code_id(getTrInfo2.getTr_issuepur_code_id());
							PrjCodeSettingsVO ifcCodeYNVO = new PrjCodeSettingsVO();
							ifcCodeYNVO.setPrj_id(getTrInfo2.getPrj_id());
							ifcCodeYNVO.setSet_code_type("SITE DOC STEP");
							ifcCodeYNVO.setSet_val2("IFC ISSUE시 Send Date");
							PrjCodeSettingsVO isIFCStepYAndActualDateUpdateByIFCIssue = prjcodesettingsService.isIFCStepYAndActualDateUpdateByIFCIssue(ifcCodeYNVO);
							if(isIFCStepYAndActualDateUpdateByIFCIssue!=null) {
								prjstepvo.setStep_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
								int updateSiteStepActualDateByIFCIssue = prjstepService.updateSiteStepActualDateByIFCIssue(prjstepvo);
								//ForecastDate 업데이트
								prjstepvo.setSet_code_id(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_code_id());
								prjstepvo.setSet_val3(isIFCStepYAndActualDateUpdateByIFCIssue.getSet_val3());
								PrjCodeSettingsVO getForeDateRuleBySiteStep = prjstepService.getForeDateRuleBySiteStep(prjstepvo);
								if(!getForeDateRuleBySiteStep.getSet_val4().equals("") && Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4())!=0) {
									SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
									Calendar cal = Calendar.getInstance();
									prjstepvo.setStep_code_id(getForeDateRuleBySiteStep.getSet_val3());
									PrjStepVO getActualDateSiteStepByStepCodeId = prjstepService.getActualDateSiteStepByStepCodeId(prjstepvo);
									if(getActualDateSiteStepByStepCodeId!=null) {
										Date dt = dtFormat.parse(getActualDateSiteStepByStepCodeId.getActual_date());
										cal.setTime(dt);
										if(!getForeDateRuleBySiteStep.getSet_val4().equals("")) {
											cal.add(Calendar.DATE, Integer.parseInt(getForeDateRuleBySiteStep.getSet_val4()));
											prjstepvo.setFore_date(dtFormat.format(cal.getTime()));
											prjstepService.updateSiteStepForeDateByStepActualDateUpdate(prjstepvo);
										}
									}
								}
							}
						}
					}
				}
				
				// 3) 검색된 코드들에 매핑된 step값들에 actual date와 forecast date 업데이트
				
				
				//2022-03-28 소진희 작성
				//DRN 최종승인시 자동발송메일템플릿 설정되어있을 경우 email 테이블에 insert
				PrjEmailTypeVO premailtypevo = new PrjEmailTypeVO();
				PrjEmailTypeContentsVO prjemailtypecontentsvo = new PrjEmailTypeContentsVO();
				PrjDocumentIndexVO prjdocumentindexvo = new PrjDocumentIndexVO();
				premailtypevo.setPrj_id(drnLineVO.getPrj_id());
				prjemailtypecontentsvo.setPrj_id(drnLineVO.getPrj_id());
				prjdocumentindexvo.setPrj_id(drnLineVO.getPrj_id());
				prjdocumentindexvo.setTr_id(tr_id_g);
				
				if(doc_type_TR.equals("DCI")) {
					premailtypevo.setEmail_feature("TR");
					prjtrvo.setProcessType("TR");
				}else if(doc_type_TR.equals("VDCI")) {
					premailtypevo.setEmail_feature("VTR");
					prjtrvo.setProcessType("VTR");
				}else if(doc_type_TR.equals("SDCI")) {
					premailtypevo.setEmail_feature("STR");
					prjtrvo.setProcessType("STR");
				}

				prjemailtypecontentsService.AutoSendEmailForDRNFinalApproval(premailtypevo, prjemailtypecontentsvo, prjtrvo, prjdocumentindexvo, design_id);
			}
			
			
			
		}
		

		result.addObject("status", "SUCCESS");

		return result;
	}
	
	
	@RequestMapping(value = "/drnEmail.do")
	public ModelAndView drnEmail(HttpServletRequest request,@RequestParam(value="prj_id", required=true) String prj_id,
			@RequestParam(value="drn_id", required=true) String drn_id,@RequestParam(value="user_id", required=true) String user_id) throws JsonProcessingException {
		ModelAndView mv = new ModelAndView();

		
		
		
		// 1) DRN 조회
		DrnLineVO drnInfo = new DrnLineVO();
		drnInfo.setPrj_id(prj_id);
		drnInfo.setDrn_id(drn_id);
		try {
			drnInfo = drnService.getDrnInfo(drnInfo);
			if(drnInfo == null) throw new Exception("해당 DRN 미존재");
		}
		catch(Exception e) {
			// DRN이 존재하지 않는 경우 해당 페이지를 뿌려주지 않는다.
			return null;
		}
		
		mv.setViewName("/page/drnEmail");
		
		mv.addObject("prj_id",prj_id);
		mv.addObject("drn_id",drn_id);
		mv.addObject("user_id",user_id);
	
		
		ObjectMapper mapper = new ObjectMapper();

		String drnInfo_json = mapper.writeValueAsString(drnInfo);
		
		mv.addObject("drnInfo",drnInfo_json);
		
		
		List<DrnLineVO> dccUser = drnService.selectList("drnSqlMap.get_dccUser", drnInfo);
		String dccUser_json = mapper.writeValueAsString(dccUser);
		mv.addObject("dccUser",dccUser_json);	

		
		
		
		// 2) DRN DOC 조회 (+ Discipline)
		List<DrnLineVO> docs = new ArrayList<>();
		docs = drnService.getDocsInfo(drnInfo);
		// DRN은 같은 폴더의 DOC들만 올리기 때문에 하나의 DOC_TYPE만 보면 된다.
		String doc_type = docs.get(0).getDoc_type();
		// 해당 문서가 있는 폴더의 DISCIPLINE을 구함
		long folder_id = docs.get(0).getFolder_id();
		PrjDocumentIndexVO prjDocumentIndexVO = new PrjDocumentIndexVO();
		prjDocumentIndexVO.setPrj_id(prj_id);
		prjDocumentIndexVO.setFolder_id(folder_id);
		String concat = "%>" + folder_id + ">%";
		prjDocumentIndexVO.setConcatFolderPath(concat);
		String discip = prjdocumentindexService.getDisciplineDisplay(prjDocumentIndexVO);
		mv.addObject("discip", discip);		
		
		String docs_json = mapper.writeValueAsString(docs);
		mv.addObject("drnDocs",docs_json);
		mv.addObject("doc_type",doc_type);
		
		
		// 3) status 조회
		drnInfo.setDoc_type(doc_type);
		DrnLineVO status = drnService.getStatusCode(drnInfo);
		mv.addObject("status",status.getCode() + "&" + status.getCode_id());
		
		// 4) 체크시트 조회
		List<DrnLineVO> checkSheetList = new ArrayList<>();
		checkSheetList = drnService.getCheckSheetList(drnInfo);
		String checkSheetList_json = mapper.writeValueAsString(checkSheetList);
		mv.addObject("checkSheetList",checkSheetList_json);
		
		List<List<DrnLineVO>> checkSheetItems = new ArrayList<>();
		for(DrnLineVO check_no :checkSheetList) {
			List<DrnLineVO> tmp = drnService.getCheckSheetItem(check_no);
			checkSheetItems.add(tmp);
		}
		String checkSheetItems_json = mapper.writeValueAsString(checkSheetItems);
		mv.addObject("checkSheetItems",checkSheetItems_json);
		
		// 5) 결재선 조회
		DrnLineVO approvalLine = drnService.getApprovalData(drnInfo);
		approvalLine = drnService.getJobCode(approvalLine);
		String[] distributer;
		distributer = approvalLine.getDistribute_id().split("@");
		String distributer_nm = "";
		for(String line : distributer)
			distributer_nm += drnService.getKorNm(line) + "@";
		
		distributer_nm = distributer_nm.substring(0,distributer_nm.length()-1);
		approvalLine.setDistributers(distributer_nm);
		String approvalLine_json = mapper.writeValueAsString(approvalLine);
		mv.addObject("approvalLine", approvalLine_json);
		
		/* 서명일자 get */
		DrnLineVO getter = new DrnLineVO();
		getter.setPrj_id(drnInfo.getPrj_id());
		getter.setDrn_id(drnInfo.getDrn_id());
		getter.setDrn_approval_status("A");
		String designSignDate = drnService.getApprovalSignDate(getter);
		UsersVO designSign = drnService.getUserSignData(drnInfo.getReg_id());
		String designSign_json = mapper.writeValueAsString(designSign);
		mv.addObject("ds_date",designSignDate);
		mv.addObject("ds_info",designSign_json);
		
			
		getter.setDrn_approval_status("1");
		String reviewSignDate = drnService.getApprovalSignDate(getter);
		UsersVO reviewSign = drnService.getUserSignData(approvalLine.getReviewed_id());
		String reviewSign_json = mapper.writeValueAsString(reviewSign);
		mv.addObject("rv_date",reviewSignDate);
		mv.addObject("rv_info",reviewSign_json);
			
		getter.setDrn_approval_status("C");
		String approveSignDate = drnService.getApprovalSignDate(getter);
		UsersVO approveSign = drnService.getUserSignData(approvalLine.getApproved_id());
		String approveSign_json = mapper.writeValueAsString(approveSign);
		mv.addObject("ap_date",approveSignDate);
		mv.addObject("ap_info",approveSign_json);
						
			
		
		
		
		
		
		// 링크를 통해 들어온 id와, 현재 결재단계에서 진행해야할 id가일치하는지 확인
		String target_id = "";
		String read_only = "DISTRIBUTE_READONLY"; // read_only를 위한 key값
		
		String as = drnInfo.getDrn_approval_status();
		if(!user_id.equals(read_only)) { // read_only_mode가 아닌 경우,
			String[] _user_id = user_id.split(";");
			switch(as) {
			case "A": // DCC ID와 비교해야함
				boolean dccChk = false;
				for(DrnLineVO chk : dccUser) {
					target_id = chk.getUser_id();
					for(String id : _user_id) {
						if(chk.getUser_id().equals(id))  dccChk = true;
					}
				}
				if(!dccChk) {
					System.out.printf("[user_id : %s | target_id : %s] 지정된 결재선과 다른 id.\n",user_id,target_id);
					return null;
				}
				break;
			case "E": // review id와 비교해야함
				boolean reviewChk = false;
				target_id = reviewSign.getUser_id();
				for(String id : _user_id) 
					if(id.equals(target_id))  reviewChk = true;
				
				if(!reviewChk) {
					System.out.printf("[user_id : %s | target_id : %s] 지정된 결재선과 다른 id.\n",user_id,target_id);
					return null;
				}
				break;
			case "1": // approve id와 비교해야함
				boolean approveChk = false;
				target_id = approveSign.getUser_id();
				for(String id : _user_id) 
					if(id.equals(target_id))  approveChk = true;
				
				if(!approveChk) {
					System.out.printf("[user_id : %s | target_id : %s] 지정된 결재선과 다른 id.\n",user_id,target_id);
					return null;
				}
				break;
			default : 
				return null;
			}
			mv.addObject("drn_auth","true");
		}
		
		
		
		mv.addObject("target_id",target_id);
		
		
		
		return mv;
	}
	
	@RequestMapping(value = "drnPrint.do" , method = RequestMethod.POST)
	public ModelAndView drnPrint(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/page/drnPrint");
		
		String prj_id = drnLineVO.getPrj_id();
		String drn_id = drnLineVO.getDrn_id();
		
		
		
		mv.addObject("prj_id",prj_id);
		mv.addObject("drn_id",drn_id);
		
		
		return mv;
		
	}
	
	@RequestMapping(value = "getDrnPrint.do" , method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getDrnPrint(DrnLineVO drnLineVO) {
		
		ModelAndView mv = new ModelAndView();

		
		
		DrnLineVO drnInfo = drnService.getDrnInfo(drnLineVO);
		
		
		
		List<DrnLineVO> docList = drnService.getDocsInfo(drnInfo);
		DrnLineVO getDocType = docList.get(0);
		getDocType.setPrj_id(drnLineVO.getPrj_id());
		String doc_type = drnService.getDocTypeFromDoc(docList.get(0));

		
		switch(doc_type) {
			case "DCI" : 
				drnInfo.setType("TR ISSUE PURPOSE");
				break;
			case "VDCI" :
				drnInfo.setType("VTR ISSUE PURPOSE");
				break;
			case "SDCI" :
				drnInfo.setType("STR ISSUE PURPOSE");
				break;
			default: // 이 외의 상황은 오류
				mv.addObject("error","doc_type error:" + doc_type);
				return mv;
		}
		
		
		String status = drnService.selectOne("getStatusValue",drnInfo);
		
		
		
		List<DrnLineVO> itemList = drnService.getCheckSheetList(drnInfo);
		List<DrnLineVO> itemInList = drnService.getCheckSheetItemList(drnInfo);
		DrnLineVO approval = drnService.getApprovalData(drnInfo);
		approval = drnService.getJobCode(approval);
		
		
		mv.addObject("drn_info",drnInfo);
		mv.addObject("docList",docList);
		mv.addObject("status",status);
		mv.addObject("itemList",itemList);
		mv.addObject("itemInList",itemInList);
		mv.addObject("approval",approval);
		
		
		
		return mv;
		
	}
	
	@RequestMapping(value = "drnValidationCheck.do")
	@ResponseBody
	public ModelAndView drnValidationCheck(DrnLineVO drnLineVO,HttpServletRequest request) {
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String session_user_id = sessioninfo.getUser_id();
		

		
		ModelAndView result = new ModelAndView();
		
		String prj_id = drnLineVO.getPrj_id();
		drnLineVO.setAction_id(session_user_id);
		String isAdmin = drnService.isAdmin(drnLineVO);
		String isDcc = drnService.isDcc(drnLineVO);
		
		String doc_type = drnLineVO.getDoc_type();
		
		String docArr = drnLineVO.getJsonData();
		
		String[] docInfo = docArr.split(",");
		
		for(String item : docInfo) {
			String[] doc = item.split("@");
			drnLineVO.setDoc_id(doc[0]);
			drnLineVO.setRev_id(doc[1]);
			
			// rev_code_id가 있는지 확인
			String rev_code_id = drnService.getDocRevCode(drnLineVO);
			if(rev_code_id == null) {
				result.addObject("check", "NO");
				// 오류가 되는 문서의 정보
				result.addObject("error_msg","Document that haven't been set up for revision can't DRN submit");
				return result;
			}
			
			
			// 해당 도서에 대한 DRN 사용이력 조회
			// 기존에 올렸던 DRN이 있더라도 상태값이 R이라면 괜찮다.
			List<DrnLineVO> drnChk = drnService.drnUsedChk(drnLineVO);
			for(DrnLineVO drnHist : drnChk) {
				String as = drnHist.getDrn_approval_status();
				
				if(as.equals("C")) {
					// 상태값
					result.addObject("check", "NO");
					// 오류가 되는 문서의 정보
					result.addObject("error_doc",drnHist);
					result.addObject("error_msg","This document has been finally approved(DRN)");
					return result;
				}
				
				// admin도 아니고, dcc도 아니면 일반적인 프로세스를 따름
				if (isAdmin.equals("N") && isDcc.equals("N")) {
					if (as.equals("R")) {
						// 상태값
						result.addObject("check", "NO");
						// 오류가 되는 문서의 정보
						result.addObject("error_doc", drnHist);
						result.addObject("error_msg","This document has been finally rejected(DRN). Please set the new revision for drafting DRN ");
						return result;
					}
				}
				
			}
			
			
			PrjTrVO prjtrvo = new PrjTrVO();
			prjtrvo.setPrj_id(prj_id);
			prjtrvo.setDoc_id(doc[0]);
			prjtrvo.setRev_id(doc[1]);
			
			List<PrjTrVO> getTR = null;
			 
			String tr_text = "";
			
			if(doc_type != null)
			switch(doc_type) {
				case "DCI":
					getTR = prjtrService.getUseDocInTR(prjtrvo);
					tr_text = "TR";
					break;
				case "VDCI":
					getTR = prjtrService.getUseDocInVTR(prjtrvo);
					tr_text = "VTR";
					break;
				case "SDCI":
					getTR = prjtrService.getUseDocInSTR(prjtrvo);
					tr_text = "STR";
					break;
				default:
					break;
			}
			
			if(getTR!=null) {
				if(getTR.size()>0) {
					// 상태값
					result.addObject("check", "NO");
					// 오류가 되는 문서의 정보
					result.addObject("error_doc",doc[0]);
					result.addObject("error_msg","["+ tr_text +"  for this document has been created. | TR NO : "+getTR.get(0).getTr_no()+" ]");
					return result;
				}
			}
			
		}
		
		
		
		result.addObject("check", "OK");
		
	
		
		return result;
	}

	

}
