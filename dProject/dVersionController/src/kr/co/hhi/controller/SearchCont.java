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
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author doil
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.excel.ExcelFileDownload;
import kr.co.hhi.model.DocumentIndexVO;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.GlobalSearchVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.SecurityLogVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.SearchService;

@Controller
@RequestMapping("/*")

public class SearchCont {

	protected SearchService searchService;

	@Autowired
	public void setTreeService(SearchService searchService) {
		this.searchService = searchService;
	}
	
	
    @Value("#{config['file.upload.path']}")
    private String UPLOAD_PATH;
    
    @Value("#{config['file.upload.prj.doc.path']}")
    private String DOC_PATH;

    @Value("#{config['file.upload.prj.template.path']}")
    private String TEMPLATE_PATH;
    
	
	/**
	 * 1. 메소드명 : getSearchResult
	 * 2. 작성일: 2021-11-22
	 * 3. 작성자: 권도일
	 * 4. 설명: 검색수행
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "search.do")
	@ResponseBody
	public Object getSearchResult(GlobalSearchVO globalSearchVO) {
		
		
		String keyword = globalSearchVO.getKeyword();
		keyword = keyword.trim(); // 공백제거
		
		String search_opt = globalSearchVO.getSearchOpt();
		switch(search_opt) {
			case "like":
				keyword = "%"+ keyword + "%";
				break;
			case "start with":
				keyword += "%";
				break;
			default :
				break;
		}
		// search option에 따른 keyword값 설정
		globalSearchVO.setKeyword(keyword);
		
		// 필터링 옵션값 구함
		String basic_doc_no_input= globalSearchVO.getBasic_input_doc_no();
		String basic_doc_no_opt = globalSearchVO.getBasic_sel_doc_no();
		String basic_title_input = globalSearchVO.getBasic_input_title();
		String basic_title_opt = globalSearchVO.getBasic_sel_title();
		
		String detail_doc_type = globalSearchVO.getDetail_sel_doc_type();
		String detail_doc_type2 = globalSearchVO.getDetail_sel_doc_type();
		String modifier = globalSearchVO.getDetail_input_modifier();
		
		boolean detail_mod_chk = globalSearchVO.isMod_periodChk();
		String mod_from = globalSearchVO.getMod_searchFrom();
		String mod_to = globalSearchVO.getMod_searchTo();
		
		if(!basic_doc_no_input.equals("")) {
			switch(basic_doc_no_opt) {
				case "start with":
					basic_doc_no_input += "%";
					break;
				case "like":
					basic_doc_no_input = "%" + basic_doc_no_input + "%";
					break;
				default : 
					break;
			}
			globalSearchVO.setBasic_input_doc_no(basic_doc_no_input);
		}
		
		// basic : title
		if(!basic_title_input.equals("")) {
			switch(basic_title_opt) {
				case "start with":
					basic_title_input += "%";
					break;
				case "like":
					basic_title_input = "%" + basic_title_input + "%";
					break;
				default : 
					break;
			}
			globalSearchVO.setBasic_input_title(basic_title_input);
		}
		
		// detail : doc_type
		switch(detail_doc_type){
			case "all":
				break;
			case "general":
				detail_doc_type = "'General'";
				break;
			case "engineering":
				detail_doc_type = "'DCI'";
				break;
			case "correspondence":
				detail_doc_type = "'LETTER' or A.doc_type = 'E-MAIL'";
				break;
			case "procurement":
				detail_doc_type = "'PCI'";
				break;
			case "vendor":
				detail_doc_type = "'VDCI'";
				break;
			case "site":
				detail_doc_type = "'SDCI'";
				break;
			case "bulk":
				detail_doc_type = "'MCI'";
				break;
		}
		globalSearchVO.setDetail_sel_doc_type(detail_doc_type);
		
		if(!modifier.equals("")) {
			String upper_modi = modifier.toUpperCase();
			globalSearchVO.setModifier(upper_modi);
		}
		
		if(detail_mod_chk) {
			mod_from += "000000"; 
			mod_to += "235959";
			globalSearchVO.setMod_searchFrom(mod_from);
			globalSearchVO.setMod_searchTo(mod_to);
		}
		
		
		// Global Search 검색작업 수행
		// document List, transmittal List, DRN List 검색 수행
		int max = globalSearchVO.getMax_Display();
		List<Object> result = new ArrayList<Object>();
		
		List<GlobalSearchVO> docSearchList = searchService.getDocSearchList(globalSearchVO);
		
		boolean keywordChk = true;
		
		switch(keyword) {
			case "%":
			case "%%":

				/*
				// modifier가 있는 경우에는 해당 키워드로 검색될 수 있도록 처리해주기 위함
				String tmpModifier = globalSearchVO.getModifier();
				if(tmpModifier == null || tmpModifier.equals("")) keywordChk = false;
				*/
				
				keywordChk = false;
				break;
			default:
				keywordChk = true;
				break;
		}
		
		// TR 리스트
		List<GlobalSearchVO> allTrSearchList = new ArrayList<>();
		if(keywordChk) {
			List<GlobalSearchVO> trSearchList = searchService.getTRSearchList(globalSearchVO);
			allTrSearchList.addAll(trSearchList);
			List<GlobalSearchVO> vtrSearchList = searchService.getVTRSearchList(globalSearchVO);
			allTrSearchList.addAll(vtrSearchList);
			List<GlobalSearchVO> strSearchList = searchService.getSTRSearchList(globalSearchVO);
			allTrSearchList.addAll(strSearchList);
		}
		
		
		// TR 필터링
		// detail : doc_type
			switch(detail_doc_type2){
					case "all":
						break;
					case "engineering":
						allTrSearchList = allTrSearchList.stream()
								.filter(i -> i.getTrType() != null)
								.filter(i -> i.getTrType().equals("TR")).collect(Collectors.toList());
						break;
					case "vendor":
						allTrSearchList = allTrSearchList.stream()
								.filter(i -> i.getTrType() != null)
								.filter(i -> i.getTrType().equals("VTR")).collect(Collectors.toList());
						break;
					case "site":
						allTrSearchList = allTrSearchList.stream()
								.filter(i -> i.getTrType() != null)
								.filter(i -> i.getTrType().equals("STR")).collect(Collectors.toList());
						break;
					default:
						allTrSearchList.clear();
						break;
			}
		
		// max display 만큼 잘라줌
		if(allTrSearchList.size()>max)
			allTrSearchList = allTrSearchList.subList(0, max);
		
		
		List<GlobalSearchVO> drnSearchList = searchService.getDRNSearchList(globalSearchVO);
		// DRN 필터링
		
		// max display 만큼 잘라줌
		if(drnSearchList.size()>max)
			drnSearchList = drnSearchList.subList(0, max);
	
		
		result.add(docSearchList);
		result.add(allTrSearchList);
		result.add(drnSearchList);
		
		
		return result;
	}
	
	
	@RequestMapping(value = "search_getDownloadList.do")
	@ResponseBody
	public ModelAndView search_getDownloadList(GlobalSearchVO globalSearchVO) throws Exception {
	
		ModelAndView mv = new ModelAndView();
		String[] docJson = globalSearchVO.getDocJson();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		// 선택된 item 배열로 전환		
		List<GlobalSearchVO> docList = new ArrayList<>();
		
		for(String item : docJson) {
			GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
			docList.add(row);
		}
		
		
		List<PrjDocumentIndexVO> resultList = new ArrayList<>();
		
		for(GlobalSearchVO item : docList) {
			PrjDocumentIndexVO getDocInfo = searchService.getDocInfo(item);
			if(getDocInfo != null)
				resultList.add(getDocInfo);
		}
		
		mv.addObject("docList", resultList);
		
		return mv;
	}
	
	
	
	@RequestMapping(value = "search_excel.do")
	@ResponseBody
	public void search_excel(GlobalSearchVO globalSearchVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
	
		
		String[] docJson = globalSearchVO.getDocJson();
		String[] trJson = globalSearchVO.getTrJson();
		String[] drnJson = globalSearchVO.getDrnJson();
		
		String[] docJson2 = globalSearchVO.getDocJsonStr().split("@,@");
		String[] trJson2 = globalSearchVO.getTrJsonStr().split("@,@");
		String[] drnJson2 = globalSearchVO.getDrnJsonStr().split("@,@");
		
		/*
		JSONParser parser = new JSONParser();		
		JSONArray docJsonArr = (JSONArray)parser.parse(docJson);
		JSONArray trJsonArr = (JSONArray)parser.parse(trJson);
		JSONArray drnJsonArr = (JSONArray)parser.parse(drnJson);
		*/
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

				
		List<GlobalSearchVO> docList = new ArrayList<>();
		List<GlobalSearchVO> trList = new ArrayList<>();
		List<GlobalSearchVO> drnList = new ArrayList<>();
		
		/*
		for(String item : docJson) {
			GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
			docList.add(row);
		}
		
		for(String item : trJson) {
			GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
			trList.add(row);
		}
		
		for(String item : drnJson) {
			GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
			drnList.add(row);
		}
		*/
		
		for(String item : docJson2) {
			if(!item.equals("") && item != null) {
				GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
				docList.add(row);
			}
		}
		
		for(String item : trJson2) {
			if(!item.equals("") && item != null) {
				GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
				trList.add(row);
			}
		}
		
		for(String item : drnJson2) {
			if(!item.equals("") && item != null) {
				GlobalSearchVO row = objectMapper.readValue(item, GlobalSearchVO.class);
				drnList.add(row);
			}
		}
		
		
		
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.search_excelDownLoad(response, docList,trList,drnList);
		
	}
	
	
	@RequestMapping(value = "search_docDown_selectedItemDownload.do")
	public void search_docDown_selectedItemDownload(PrjDocumentIndexVO prjdocumentindexvo , HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		//PrintWriter writer = response.getWriter(); 
		
		List<PrjDocumentIndexVO> selectedDocs = new ArrayList<>();
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		
		String[] jsonRowDatas = prjdocumentindexvo.getJsonRowDatas();
		String fileNameSet = prjdocumentindexvo.getFileRenameSet();
		if(fileNameSet==null) fileNameSet = "";
		
		// 다운로드를 위해 json 파싱하여 selectedDocs 리스트 생성
		for (String json : jsonRowDatas) {
			try {
				PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
				selectedDocs.add(tmpBean);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
		String[] invalidName = {"\\\\","/",":","[*]","[?]","\"","<",">","[|]"};
		
		System.out.printf("다운로드 할 문서의 갯수 : %d\n",selectedDocs.size());
		if(selectedDocs.size() == 1) {
			PrjDocumentIndexVO doc = selectedDocs.get(0);
			String tempPath = "";
			
			String sfile_nm = doc.getSfile_nm();
			String rfile_nm = doc.getRfile_nm();
			String[] renameVar = {"%DOCNO%", "%REVNO%", "%TITLE%"};
			String tmpFilenm = fileNameSet;
			// null의 경우 치환
			String doc_no = doc.getDoc_no();
			if(doc_no == null) doc_no= "";
			String rev_no = doc.getRev_no();
			if(rev_no == null) rev_no= "";
			String title = doc.getTitle();
			if(title == null) title= "";
			// 각각의 rename 변수들에 따라 치환 시켜준다.%DOCNO%, %REVNO%, %TITLE%
			int renameIdx = 0;
			tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],doc_no);
			tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],rev_no);
			tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],title);
			
			
			for(String invalid:invalidName) tmpFilenm = tmpFilenm.replaceAll(invalid, "_");
			tmpFilenm = URLEncoder.encode((tmpFilenm),"UTF-8");
			
			// rename set으로 파일명 치환 후, 확장자를 붙여줘야함
			String ext = doc.getFile_type();
			
			String outFileNm = tmpFilenm + "." + ext;
			String prj_id = doc.getPrj_id();

			try {
				tempPath = UPLOAD_PATH + prj_id + DOC_PATH;; // 파일이 저장되어 있는 경로
				
				// 파일 다운로드
				response.setContentType("application/zip");
				outFileNm = CommonConst.downloadNameEncoding(request, outFileNm);
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.addHeader("Content-Disposition", "attachment;filename=" + outFileNm);
				
				
				FileInputStream fis = new FileInputStream(tempPath + sfile_nm);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ServletOutputStream so = response.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(so);

				int n = 0;
				byte[] buffer = new byte[1024];
				
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
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}else if(selectedDocs.size() > 1) { // 선택된 파일의 갯수가 2개이상 - 압축하여 다운로드
			
			// 다운받는 날 기준으로 압축파일명 셋팅
			LocalDateTime now = LocalDateTime.now();
			String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	    	ZipOutputStream zout = null;
	    	
	    	// 파일명 : 프로젝트명_discipline_날짜 형태
	    	//String zipName = "newPEROS_docDownload_"+formatedNow+".zip";		//ZIP 압축 파일명 (실제 다운로드 될 파일)
	    	String zipName = "GlobalSearchDocDownload_"+formatedNow+".zip";		//ZIP 압축 파일명 (실제 다운로드 될 파일)
	    	zipName = CommonConst.downloadNameEncoding(request, zipName);
			String tempPath = "";
			
			String[] renameVar = {"%DOCNO%", "%REVNO%", "%TITLE%"};
			
			try {
				tempPath = UPLOAD_PATH + "%prj_id%" + DOC_PATH; // 파일이 저장되어 있는 경로 (프로젝트아이디 별로 변경해줘야 하므로 임시 형태)

				// ZIP파일 압축 START
				zout = new ZipOutputStream(new FileOutputStream(UPLOAD_PATH + "temp/" + zipName));
				byte[] buffer = new byte[1024];
				FileInputStream in = null;
				
				// 파일다운로드 START
				response.setContentType("application/zip");
				response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
				
				// 파일의 갯수 만큼 읽어서 압축파일로 만들어준다.				
				for(PrjDocumentIndexVO file : selectedDocs) {
					String sfile_nm = file.getSfile_nm();
					String rfile_nm = file.getRfile_nm();
					String tmpFilenm = fileNameSet;
					String folder_nm = file.getFolder_str();
					
					// null의 경우 치환
					String doc_no = file.getDoc_no();
					if(doc_no == null) doc_no= "";
					String rev_no = file.getRev_no();
					if(rev_no == null) rev_no= "";
					String title = file.getTitle();
					if(title == null) title= "";
					// 각각의 rename 변수들에 따라 치환 시켜준다.%DOCNO%, %REVNO%, %TITLE%
					int renameIdx = 0;
					tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],doc_no);
					tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],rev_no);
					tmpFilenm = tmpFilenm.replaceAll(renameVar[renameIdx++],title);
					// '/'는 폴더구분자이므로 치환 필요 (윈도우 파일명으로 쓸 수 없는 파일명들 모두 치환 필요)
					for(String invalid:invalidName) tmpFilenm = tmpFilenm.replaceAll(invalid, "_");
					
					// rename set으로 파일명 치환 후, 확장자를 붙여줘야함
					String ext = file.getFile_type();
					String realPath = tempPath.replace("%prj_id%", file.getPrj_id());
					File fileExists = new File(realPath + sfile_nm);
					//파일이 존재 하지 않을때는 zip에서 제외시킴
					if (fileExists.exists() == true) {
						in = new FileInputStream(realPath + sfile_nm); // 실제 저장된 압축 대상 파일
						zout.putNextEntry(new ZipEntry(folder_nm+"/"+tmpFilenm+"."+ext)); // 압축파일에 저장시킬 파일명
						int len;
						while ((len = in.read(buffer)) > 0) {
							zout.write(buffer, 0, len); // 읽은 파일을 ZipOutputStream에 Write
						}

						zout.closeEntry();
						in.close();
					}
					
				}

				zout.close();
				// ZIP파일 압축 END
				

				FileInputStream fis = new FileInputStream(UPLOAD_PATH + "temp/" + zipName);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ServletOutputStream so = response.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(so);
				
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

				Path filePath = Paths.get(UPLOAD_PATH + "temp/" + zipName);
				// tempPath폴더에 생성된 zip파일 삭제
				Files.deleteIfExists(filePath);
			} catch (IOException e) {
				// Exception 발생시 에러처리
				e.printStackTrace();
			} finally {
				if (zout != null) {
					zout = null; // zout 소멸되지 않았으면 소멸시켜줌
				}
			}
			
		}else { // 오류
			System.out.println("예외 오류 발생");
		}
		
		
	}
	

	
}
