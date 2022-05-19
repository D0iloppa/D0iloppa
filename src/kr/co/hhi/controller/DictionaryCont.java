package kr.co.hhi.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.model.DictionarySearchVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.DictionaryService;
import kr.co.hhi.service.PrjCodeSettingsService;
import kr.co.hhi.service.PrjInfoService;

@Controller
@RequestMapping("/*")
public class DictionaryCont {

	protected DictionaryService dictionaryService;

	@Autowired
	public void setDictionaryService(DictionaryService dictionaryService) { // 이
		this.dictionaryService = dictionaryService;
	}

	/**
	 * 1. 메소드명 : getDictionary
	 * 2. 작성일: 2021-12-02
	 * 3. 작성자: 박정우
	 * 4. 설명: 사전 정보 리스트를 가져옴
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "getDictionary.do")
	@ResponseBody
	public Object getDictionaryVO() {
		List<Object> result = new ArrayList<Object>();
		List<DictionarySearchVO> getDictionaryVO = dictionaryService.getDictionary();
		result.add(getDictionaryVO);
		return result;
	}
	
	/**
	 * 1. 메소드명 : findDictionary
	 * 2. 작성일: 2021-12-02
	 * 3. 작성자: 박정우
	 * 4. 설명: 사전 정보 리스트 검색기능
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "findDictionaryA.do")
	@ResponseBody
	public Object findDictionaryAbbr(DictionarySearchVO dictionarysearchVo){
		List<Object> result = new ArrayList<Object>();
		List<DictionarySearchVO> findDictionaryAbbrList = dictionaryService.findDictionaryAbbr(dictionarysearchVo);
		result.add(findDictionaryAbbrList);
		return result;
	}
	
	/**
	 * 1. 메소드명 : insertDictionary
	 * 2. 작성일: 2021-12-03
	 * 3. 작성자: 박정우
	 * 4. 설명: p_dictionary테이블에 새 정보를 넣음
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "insertDictionary.do")
	@ResponseBody
	public Object insertDictionaryVO(HttpServletRequest request,DictionarySearchVO dictionarysearchVo) {
		
		List<Object> result = new ArrayList<Object>();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		dictionarysearchVo.setReg_id(sessioninfo.getUser_id());
		// insert
		dictionaryService.insertDictionary(dictionarysearchVo);
		
		return result;
		
	}

	/**
	 * 1. 메소드명 : insertDictionaryExcel
	 * 2. 작성일: 2021-12-03
	 * 3. 작성자: 박정우
	 * 4. 설명: p_dictionary테이블에 엑셀로 받아온 새 정보를 넣음
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "insertDictionaryExcel.do")
	@ResponseBody
	public List<String> insertDictionaryExcel(HttpServletRequest request,DictionarySearchVO dictionarysearchVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = dictionarysearchVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				DictionarySearchVO vo = objectMapper.readValue(jsonDatas[0], DictionarySearchVO.class);
				
				if(dictionaryService.getDictionary().size() > 0) {
					// 기존 데이터 삭제
					dictionaryService.delDictionaryAll(vo);
				}
				
				for(String json : jsonDatas) {
					DictionarySearchVO tmpBean = objectMapper.readValue(json, DictionarySearchVO.class);
					if (tmpBean.getReg_id() == null) {
						tmpBean.setReg_id(reg_id);
					}else if (tmpBean.getReg_date() == null) {
						tmpBean.setReg_date(reg_date);
					}
					
					if (tmpBean.getMod_id() == null) {
						tmpBean.setMod_id(reg_id);
					}else if (tmpBean.getMod_date() == null) {
						tmpBean.setMod_date(reg_date);
					}
					// insert
					dictionaryService.insertDictionary(tmpBean);
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				status.set(0, "FAIL");
				status.add(e.getMessage());				
			}
		}
		
		return status;
	}
	
	/**
	 * 1. 메소드명 : updateDictionary
	 * 2. 작성일: 2021-12-03
	 * 3. 작성자: 박정우
	 * 4. 설명: p_dictionary테이블에 선택한 테이블 정보를 업데이트
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "updateDictionary.do")
	@ResponseBody
	public Object updateDictionary(DictionarySearchVO dictionarysearchVo) {
		
		List<Object> result = new ArrayList<Object>();
		
		// update
		dictionaryService.updateDictionary(dictionarysearchVo);
		
		return result;
		
	}
	
	/**
	 * 1. 메소드명 : getDictionaryList
	 * 2. 작성일: 2021-12-03
	 * 3. 작성자: 박정우
	 * 4. 설명: p_dictionary테이블에 선택한 테이블 삭제
	 * 5. 수정일: 
	 */
	
	@RequestMapping(value = "delDictionary.do")
	@ResponseBody
	public Object delDictionary(DictionarySearchVO dictionarysearchVo)  {
		
		List<Object> result = new ArrayList<Object>();
		
		// delete
		dictionaryService.delDictionary(dictionarysearchVo);
		
		return result;
		
	}
}
