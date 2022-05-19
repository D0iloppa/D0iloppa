package kr.co.doiloppa.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.doiloppa.common.exceptions.HHIFolderException;
import kr.co.doiloppa.excel.*;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.model.WbsCodeControlVO;
import kr.co.doiloppa.service.WbsCodeControlService;;

@Controller
@RequestMapping("/*")
public class WbsCodeControlCont {
	
	protected WbsCodeControlService wbscodecontrolService;
	
	@Autowired
	public void setWbsCodeControlService(WbsCodeControlService wbscodecontrolService) {
		this.wbscodecontrolService = wbscodecontrolService;
	}
	
	/**
	 * 1. 메소드명 : getWbscodeList
	 * 2. 작성일: 2021-11-17
	 * 3. 작성자: 소진희
	 * 4. 설명: 메인페이지로 가는 컨트롤러
	 * 5. 수정일: 
	 */
//	@RequestMapping(value = "main.do")
//	public ModelAndView main(HttpServletRequest request, WbsCodeControlVO wbscodecontrolVo) {
//		ModelAndView mv = new ModelAndView();
//		List<WbsCodeControlVO> getWbscodeList = wbscodecontrolService.getWbscodeList(wbscodecontrolVo);
//		mv.addObject("getWbscodeList", getWbscodeList);
//		mv.setViewName("/main");
//		return mv;
//	}
	
	/**
	 * 1. 메소드명 : getWbscodeList
	 * 2. 작성일: 2021-12-20
	 * 3. 작성자: 박정우
	 * 4. 설명: 저장된 코드 정보를 가져옴
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getWbscodeList_wbs.do")
	@ResponseBody
	public Object getWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		List<WbsCodeControlVO> getWbscodeList = wbscodecontrolService.getWbscodeList(wbscodecontrolVo);
		System.out.println(getWbscodeList);
		result.add(getWbscodeList);
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : updateWbscodeList
	 * 2. 작성일: 2021-12-21
	 * 3. 작성자: 박정우
	 * 4. 설명: 수정한 코드 정보를 업데이트 함
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "updateWbscodeList.do")
	@ResponseBody
	public Object updateWbscodeList(HttpServletRequest request,WbsCodeControlVO wbscodecontrolVo) {
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		wbscodecontrolVo.setMod_id(sessioninfo.getUser_id());
		
		wbscodecontrolService.updateWbscodeList(wbscodecontrolVo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : deleteWbscodeList
	 * 2. 작성일: 2021-12-21
	 * 3. 작성자: 박정우
	 * 4. 설명: 수정한 코드 정보를 업데이트 함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "deleteWbscodeList.do")
	@ResponseBody
	public Object deleteWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		List<Object> result = new ArrayList<Object>();
		wbscodecontrolService.deleteWbscodeList(wbscodecontrolVo);
		return result;
	}
	
	/**
	 * 1. 메소드명 : searchWbscodeList
	 * 2. 작성일: 2021-12-21
	 * 3. 작성자: 박정우
	 * 4. 설명: 코드정보를 검색함
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "searchWbscodeList.do")
	@ResponseBody
	public Object searchWbscodeList(WbsCodeControlVO wbscodecontrolVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		List<WbsCodeControlVO> searchWbscodeList = wbscodecontrolService.searchWbscodeList(wbscodecontrolVo);
		
		result.add(searchWbscodeList);
		
		return result;
	}

	/**
	 * 1. 메소드명 : updateWBScode
	 * 2. 작성일: 2021. 12. 24.
	 * 3. 작성자: doil
	 * 4. 설명:  update해야할지 insert해야할지 구분해서 DB에 반영
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "updateWBScode.do")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ResponseBody
	public String updateWBScode(WbsCodeControlVO wbscodecontrolVo) {
		
		long  maxId = wbscodecontrolService.getWBSMaxId(wbscodecontrolVo);
		
		String[] jsonDatas = wbscodecontrolVo.getJsonRowdatas();
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			// 매핑되지 않는 data들 무시
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			try {
				// row별로 discipline 폴더 체크
				for(String json : jsonDatas) {
					WbsCodeControlVO tmpBean = objectMapper.readValue(json, WbsCodeControlVO.class);
					
					//update,insert 전 공백 제거
					tmpBean.setWbs_code((tmpBean.getWbs_code()).trim());
					tmpBean.setWbs_desc((tmpBean.getWbs_desc()).trim());
					// wbs code id가 존재하면 update
					if(tmpBean.getWbs_code_id() != null) {
						if(wbscodecontrolService.updateWBS(tmpBean) < 1)
								throw new Exception("["+ tmpBean.getWbs_code() + "]: 코드 업데이트 실패");
						System.out.println(tmpBean.getWbs_code_id() + " : updated");
						
					}else { // wbs code id가 없으면 insert
						// insert할 code_id를 설정
						tmpBean.setWbs_code_id(String.format("%05d", maxId++));
						if(wbscodecontrolService.insertWBS(tmpBean) < 1)
							throw new Exception("["+ tmpBean.getWbs_code() + "]: 코드 삽입 실패");
						System.out.println(tmpBean.getWbs_code_id() + " : insert");
					}
					
					
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				return e.getMessage();
			}
		}
		
		
		
		return "SUCCESS";
	}
	
	/**
	 * 1. 메소드명 : delWBScode
	 * 2. 작성일: 2021-12-22
	 * 3. 작성자: 권도일
	 * 4. 설명: WBS CODE를 삭제. 삭제 전, 해당 discipline에 매핑된 폴더들에 문서가 존재하는지 체크 후, 삭제처리
	 * 5. 수정일: 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "delWBScode.do")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ResponseBody
	public String delWBScode(WbsCodeControlVO wbscodecontrolVo) throws JsonParseException, JsonMappingException, IOException {
		
		// 배열로 삭제할 row의 데이터들을 가져온다.
		// 해당 row의 wbs_code_id 뿐만 아니라, discipline id와 여기에 매핑된 폴더들의 id도 알아야 하기 때문
		String[] jsonDatas = wbscodecontrolVo.getJsonRowdatas();
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			// 매핑되징 않는 data들 무시
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			try {
				// row별로 discipline 폴더 체크
				for(String json : jsonDatas) {
					// json을 VO로 변환
					// System.out.println(json);
					
					WbsCodeControlVO tmpBean = objectMapper.readValue(json, WbsCodeControlVO.class);
					// 해당 discipline의 폴더 리스트 검색
					List<Long> folderList = wbscodecontrolService.getDiscipFolder(tmpBean);
					if(folderList != null)
					for(Long id :folderList) {
						PrjDisciplineVO discipBean = new PrjDisciplineVO();
						String folder_path = "%>" + id + ">%";
						discipBean.setPrj_id(tmpBean.getPrj_id());
						discipBean.setFolder_name(folder_path);
						discipBean.setFolder_id(id);
						// 해당 폴더에 파일 있는지 검색
						// 1개 이상이면 파일이 존재한다는 뜻
						if(wbscodecontrolService.checkFolder(discipBean) > 0) 
							throw new HHIFolderException("해당 CODE는 삭제할 수 없습니다. :[" + tmpBean.getDiscip_display() + " 관련 폴더에 파일 존재]");	
					} //삭제 가능
					if(wbscodecontrolService.deleteWbscodeList(tmpBean) < 0 )
						throw new Exception("해당 CODE 삭제 실패 :["+tmpBean.getWbs_code()+","+tmpBean.getWbs_code_id()+"]");
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				return e.getMessage();
			}
		}
		

		
		return "SUCCESS";
	}
	
	
	/**
	 * 1. 메소드명 : wbsExcelDownload
	 * 2. 작성일: 2021. 12. 28.
	 * 3. 작성자: doil
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "wbsExcelDownload.do")
	@ResponseBody
	public void wbsExcelDownload(WbsCodeControlVO wbsCodeControlVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		
		List<WbsCodeControlVO> getWbscodeList = wbscodecontrolService.getWbscodeList(wbsCodeControlVO);
		System.out.println(getWbscodeList);
		ExcelFileDownload excelDown = new ExcelFileDownload();
 		excelDown.wbsExcelDownload(response, "titleList",  getWbscodeList, wbsCodeControlVO.getFileName() , wbsCodeControlVO.getPrj_nm());
	}	
	
	
	
}
