package kr.co.hhi.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.common.util.AES256Util;
import kr.co.hhi.common.util.GetSHA256;
import kr.co.hhi.model.FolderAuthVO;
import kr.co.hhi.model.PrjCodeSettingsVO;
import kr.co.hhi.model.PrjDisciplineVO;
import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjEmailTypeContentsVO;
import kr.co.hhi.model.PrjEmailTypeVO;
import kr.co.hhi.model.PrjEmailVO;
import kr.co.hhi.model.PrjEngInfoVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjGroupMemberVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.PrjMemberVO;
import kr.co.hhi.model.PrjStepVO;
import kr.co.hhi.model.PrjSystemPrefixVO;
import kr.co.hhi.model.PrjTrVO;
import kr.co.hhi.model.PrjWbsCodeVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.service.DataMigrationService;

@Controller
@RequestMapping("/*")
public class DataMigrationCont {
	protected DataMigrationService dataMigrationService;
	
	@Autowired
	public void setDataMigrationService(DataMigrationService dataMigrationService) {
		this.dataMigrationService = dataMigrationService;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getCodeSetList
	 * 2. 작성일: 2022. 01. 30.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration prj_code_settings db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCodeSetList.do")
	@ResponseBody
	public Object getCodeSetList(PrjCodeSettingsVO prjCodeSettingsVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjCodeSettingsVO> getCodeListVo = dataMigrationService.getCodeSetList(prjCodeSettingsVo);
		result.add(getCodeListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : codeSettingsSave
	 * 2. 작성일: 2022. 01. 31.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration prj_code_settings db 저장
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "codeSettingsSave.do")
	@ResponseBody
	public List<String> codeSettingsSave(HttpServletRequest request, PrjCodeSettingsVO prjCodeSettingsVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjCodeSettingsVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjCodeSettingsVO vo = objectMapper.readValue(jsonDatas[0], PrjCodeSettingsVO.class);

				if (dataMigrationService.getCodeSetList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteCodeSettings(vo);
				}
				
				for(String json : jsonDatas) {
					PrjCodeSettingsVO tmpBean = objectMapper.readValue(json, PrjCodeSettingsVO.class);
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
					//insert
					dataMigrationService.insertCodeSettings(tmpBean);
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
	 * 
	 * 1. 메소드명 : getCordocInfoList
	 * 2. 작성일: 2022. 01. 30.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_cordoc_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getCordocInfoList.do")
	@ResponseBody
	public Object getCordocInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDocumentIndexVO> getCordocInfoListVo = dataMigrationService.getCordocInfoList(prjDocumentIndexVo);
		result.add(getCordocInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : cordocInfoSave
	 * 2. 작성일: 2022. 01. 31.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration prj_cordoc_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "cordocInfoSave.do")
	@ResponseBody
	public List<String> cordocInfoSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDocumentIndexVo.getJsonRowDatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjDocumentIndexVO vo = objectMapper.readValue(jsonDatas[0], PrjDocumentIndexVO.class);

				if (dataMigrationService.getCordocInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteCordocInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
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
					//insert
					dataMigrationService.insertCordocInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getDisciplineFolderInfoList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_discipline_folder_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getDisciplineFolderInfoList.do")
	@ResponseBody
	public Object getDisciplineFolderInfoList(PrjDisciplineVO prjDisciplineVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDisciplineVO> getDisciplineFolderInfoListVo = dataMigrationService.getDisciplineFolderInfoList(prjDisciplineVo);
		result.add(getDisciplineFolderInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : disciplineFolderInfoSave
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_discipline_folder_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "disciplineFolderInfoSave.do")
	@ResponseBody
	public List<String> disciplineFolderInfoSave(HttpServletRequest request, PrjDisciplineVO prjDisciplineVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDisciplineVo.getJsonRowDatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjDisciplineVO vo = objectMapper.readValue(jsonDatas[0], PrjDisciplineVO.class);

				if (dataMigrationService.getDisciplineFolderInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteDisciplineFolderInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjDisciplineVO tmpBean = objectMapper.readValue(json, PrjDisciplineVO.class);
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
					//insert
					dataMigrationService.insertDisciplineFolderInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getDisciplineInfoList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_discipline_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getDisciplineInfoList.do")
	@ResponseBody
	public Object getDisciplineInfoList(PrjDisciplineVO prjDisciplineVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDisciplineVO> getDisciplineInfoListVo = dataMigrationService.getDisciplineInfoList(prjDisciplineVo);
		result.add(getDisciplineInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : disciplineInfoSave
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_discipline_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "disciplineInfoSave.do")
	@ResponseBody
	public List<String> disciplineInfoSave(HttpServletRequest request, PrjDisciplineVO prjDisciplineVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDisciplineVo.getJsonRowDatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjDisciplineVO vo = objectMapper.readValue(jsonDatas[0], PrjDisciplineVO.class);

				if (dataMigrationService.getDisciplineInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteDisciplineInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjDisciplineVO tmpBean = objectMapper.readValue(json, PrjDisciplineVO.class);
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
					//insert
					dataMigrationService.insertDisciplineInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getDocumentIndexList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_document_index db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getDocumentIndexList.do")
	@ResponseBody
	public List<Object> getDocumentIndexList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<Object> result = new ArrayList<Object>();
		// ModelAndView result = new ModelAndView();
		
		List<PrjDocumentIndexVO> getDocumentIndexListVo = dataMigrationService.getDocumentIndexList(prjDocumentIndexVo);
		result.add(getDocumentIndexListVo);
		//result.addObject("getDocumentIndexList",getDocumentIndexListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : documentIndexSave
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_document_index db 저장
	 * 5. 수정일:
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "documentIndexSave.do")
	@ResponseBody
	public List<String> documentIndexSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVo) throws JsonParseException, JsonMappingException, IOException {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDocumentIndexVo.getJsonRowDatas();
		
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			// 기존 데이터 삭제
			
			
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {

				PrjDocumentIndexVO vo = objectMapper.readValue(jsonDatas[0], PrjDocumentIndexVO.class);

				// 기존 데이터 삭제
				if (dataMigrationService.getDocumentIndexList(vo).size() > 0)
					dataMigrationService.deleteDocumentIndex(vo);

				for (String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
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
					//insert
					dataMigrationService.insertDocumentIndex(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEmailAttachInfoList
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEmailAttachInfoList.do")
	@ResponseBody
	public Object getEmailAttachInfoList(PrjEmailVO prjEmailVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjEmailVO> getEmailAttachInfoListVo = dataMigrationService.getEmailAttachInfoList(prjEmailVo);
		result.add(getEmailAttachInfoListVo);
		return result;
	}

	/**
	 * 
	 * 1. 메소드명 : emailAttachInfoSave
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "emailAttachInfoSave.do")
	@ResponseBody
	public List<String> emailAttachInfoSave(HttpServletRequest request, PrjEmailVO prjEmailVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjEmailVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				
				PrjEmailVO vo = objectMapper.readValue(jsonDatas[0], PrjEmailVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEmailAttachInfoList(vo).size() > 0)
					dataMigrationService.deleteEmailAttachInfo(vo);
				
				
				for(String json : jsonDatas) {
					PrjEmailVO tmpBean = objectMapper.readValue(json, PrjEmailVO.class);
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
					//insert
					dataMigrationService.insertEmailAttachInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEmailReceiptInfoList
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email_type db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEmailReceiptInfoList.do")
	@ResponseBody
	public Object getEmailReceiptInfoList(PrjEmailVO prjEmailVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjEmailVO> getEmailTypeListVo = dataMigrationService.getEmailReceiptInfoList(prjEmailVo);
		result.add(getEmailTypeListVo);
		return result;
	}

	/**
	 * 
	 * 1. 메소드명 : emailReceiptInfoSave
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "emailReceiptInfoSave.do")
	@ResponseBody
	public List<String> emailReceiptInfoSave(HttpServletRequest request, PrjEmailVO prjEmailVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjEmailVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				
				PrjEmailVO vo = objectMapper.readValue(jsonDatas[0], PrjEmailVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEmailReceiptInfoList(vo).size() > 0)
					dataMigrationService.deleteEmailReceiptInfo(vo);

				for(String json : jsonDatas) {
					PrjEmailVO tmpBean = objectMapper.readValue(json, PrjEmailVO.class);
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
					//insert
					dataMigrationService.insertEmailReceiptInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEmailSendInfoList
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email_type db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEmailSendInfoList.do")
	@ResponseBody
	public Object getEmailSendInfoList(PrjEmailVO prjEmailVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjEmailVO> getEmailTypeListVo = dataMigrationService.getEmailSendInfoList(prjEmailVo);
		result.add(getEmailTypeListVo);
		return result;
	}

	/**
	 * 
	 * 1. 메소드명 : emailSendInfoSave
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "emailSendInfoSave.do")
	@ResponseBody
	public List<String> emailSendInfoSave(HttpServletRequest request, PrjEmailVO prjEmailVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjEmailVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {

				PrjEmailVO vo = objectMapper.readValue(jsonDatas[0], PrjEmailVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEmailSendInfoList(vo).size() > 0)
					dataMigrationService.deleteEmailSendInfo(vo);
					
				
				for(String json : jsonDatas) {
					PrjEmailVO tmpBean = objectMapper.readValue(json, PrjEmailVO.class);
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
					//insert
					dataMigrationService.insertEmailSendInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEmailTypeList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email_type db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEmailTypeList.do")
	@ResponseBody
	public Object getEmailTypeList(PrjEmailTypeVO prjEmailTypeVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjEmailTypeVO> getEmailTypeListVo = dataMigrationService.getEmailTypeList(prjEmailTypeVo);
		result.add(getEmailTypeListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : emailType
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email_type db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "emailTypeSave.do")
	@ResponseBody
	public List<String> emailTypeSave(HttpServletRequest request, PrjEmailTypeVO prjEmailTypeVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjEmailTypeVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjEmailTypeVO vo = objectMapper.readValue(jsonDatas[0], PrjEmailTypeVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEmailTypeList(vo).size() > 0)
					dataMigrationService.deleteEmailType(vo);
				
				for(String json : jsonDatas) {
					PrjEmailTypeVO tmpBean = objectMapper.readValue(json, PrjEmailTypeVO.class);
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
					//insert
					dataMigrationService.insertEmailType(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEmailTypeContentsList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email_type_contents db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEmailTypeContentsList.do")
	@ResponseBody
	public Object getEmailTypeContentsList(PrjEmailTypeContentsVO prjEmailTypeContentsVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjEmailTypeContentsVO> getEmailTypeContentsListVo = dataMigrationService.getEmailTypeContentsList(prjEmailTypeContentsVo);
		result.add(getEmailTypeContentsListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : emailTypeContents
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_email_type db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "emailTypeContentsSave.do")
	@ResponseBody
	public List<String> emailTypeContentsSave(HttpServletRequest request, PrjEmailTypeContentsVO prjEmailTypeContentsVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjEmailTypeContentsVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjEmailTypeContentsVO vo = objectMapper.readValue(jsonDatas[0], PrjEmailTypeContentsVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEmailTypeContentsList(vo).size() > 0)
					dataMigrationService.deleteEmailTypeContents(vo);
				
				for(String json : jsonDatas) {
					PrjEmailTypeContentsVO tmpBean = objectMapper.readValue(json, PrjEmailTypeContentsVO.class);
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
					//insert
					dataMigrationService.insertEmailTypeContents(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEngInfoList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_eng_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEngInfoList.do")
	@ResponseBody
	public Object getEngInfoList(PrjEngInfoVO prjEngInfoVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjEngInfoVO> getEngInfoListVo = dataMigrationService.getEngInfoList(prjEngInfoVo);
		result.add(getEngInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : engInfo
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_eng_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "engInfoSave.do")
	@ResponseBody
	public List<String> engInfoSave(HttpServletRequest request, PrjEngInfoVO prjEngInfoVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjEngInfoVo.getJsonRowDatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				
				PrjEngInfoVO vo = objectMapper.readValue(jsonDatas[0], PrjEngInfoVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEngInfoList(vo).size() > 0)
					dataMigrationService.deleteEngInfo(vo);
				
				for(String json : jsonDatas) {
					PrjEngInfoVO tmpBean = objectMapper.readValue(json, PrjEngInfoVO.class);
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
					//insert
					dataMigrationService.insertEngInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getEngStepList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_eng_step db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getEngStepList.do")
	@ResponseBody
	public Object getEngStepList(PrjStepVO prjStepVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjStepVO> getEngStepListVo = dataMigrationService.getEngStepList(prjStepVo);
		result.add(getEngStepListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : engStepSave
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_eng_step db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "engStepSave.do")
	@ResponseBody
	public List<String> engStepSave(HttpServletRequest request, PrjStepVO prjStepVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjStepVo.getTableArrayData(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjStepVO vo = objectMapper.readValue(jsonDatas[0], PrjStepVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getEngStepList(vo).size() > 0)
					dataMigrationService.deleteEngStep(vo);
					
				
				for(String json : jsonDatas) {
					PrjStepVO tmpBean = objectMapper.readValue(json, PrjStepVO.class);
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
					//insert
					dataMigrationService.insertEngStep(tmpBean);
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
	 * 
	 * 1. 메소드명 : getFolderAuthList
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getFolderAuthList.do")
	@ResponseBody
	public Object getFolderAuthList(FolderAuthVO folderAuthVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<FolderAuthVO> getFolderAuthList = dataMigrationService.getFolderAuthList(folderAuthVo);
		result.add(getFolderAuthList);
		return result;
	}

	/**
	 * 
	 * 1. 메소드명 : folderAuthSave
	 * 2. 작성일: 2022. 03. 16.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "folderAuthSave.do")
	@ResponseBody
	public List<String> folderAuthSave(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = folderAuthVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				FolderAuthVO vo = objectMapper.readValue(jsonDatas[0], FolderAuthVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getFolderAuthList(vo).size() > 0)
					dataMigrationService.deleteFolderAuth(vo);
				
				
				for(String json : jsonDatas) {
					FolderAuthVO tmpBean = objectMapper.readValue(json, FolderAuthVO.class);
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
					//insert
					dataMigrationService.insertFolderAuth(tmpBean);
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
	 * 
	 * 1. 메소드명 : getFolderInfoList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getFolderInfoList.do")
	@ResponseBody
	public Object getFolderInfoList(PrjFolderInfoVO prjFolderInfoVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjFolderInfoVO> getFolderInfoListVo = dataMigrationService.getFolderInfoList(prjFolderInfoVo);
		result.add(getFolderInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : folderInfoSave
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "folderInfoSave.do")
	@ResponseBody
	public List<String> folderInfoSave(HttpServletRequest request, PrjFolderInfoVO prjFolderInfoVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjFolderInfoVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjFolderInfoVO vo = objectMapper.readValue(jsonDatas[0], PrjFolderInfoVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getFolderInfoList(vo).size() > 0)
					dataMigrationService.deleteFolderInfo(vo);
				
				
				for(String json : jsonDatas) {
					PrjFolderInfoVO tmpBean = objectMapper.readValue(json, PrjFolderInfoVO.class);
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
					//insert
					dataMigrationService.insertFolderInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getFolderInfoList
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_eng_step db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getGendocInfoList.do")
	@ResponseBody
	public Object getGendocInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDocumentIndexVO> getGendocInfoListVo = dataMigrationService.getGendocInfoList(prjDocumentIndexVo);
		result.add(getGendocInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : engStepSave
	 * 2. 작성일: 2022. 02. 02.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_eng_step db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "gendocInfoSave.do")
	@ResponseBody
	public List<String> gendocInfoSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDocumentIndexVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjDocumentIndexVO vo = objectMapper.readValue(jsonDatas[0], PrjDocumentIndexVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getGendocInfoList(vo).size() > 0)
					dataMigrationService.deleteGendocInfo(vo);
				
				for(String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
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
					//insert
					dataMigrationService.insertGendocInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getGroupMemberList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_group_member db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getGroupMemberList.do")
	@ResponseBody
	public Object getGroupMemberList(PrjGroupMemberVO prjGroupMemberVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjGroupMemberVO> getGroupMemberListVo = dataMigrationService.getGroupMemberList(prjGroupMemberVo);
		result.add(getGroupMemberListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : groupMemberSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_group_member db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "groupMemberSave.do")
	@ResponseBody
	public List<String> groupMemberSave(HttpServletRequest request, PrjGroupMemberVO prjGroupMemberVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjGroupMemberVo.getJsonRowdatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjGroupMemberVO vo = objectMapper.readValue(jsonDatas[0], PrjGroupMemberVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getGroupMemberList(vo).size() > 0)
					dataMigrationService.deleteGroupMember(vo);					
				
				
				for(String json : jsonDatas) {
					PrjGroupMemberVO tmpBean = objectMapper.readValue(json, PrjGroupMemberVO.class);
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
					//insert
					dataMigrationService.insertGroupMember(tmpBean);
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
	 * 
	 * 1. 메소드명 : getPrjInfoList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getPrjInfoList.do")
	@ResponseBody
	public Object getPrjInfoList(PrjInfoVO prjInfoVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjInfoVO> getPrjInfoListVo = dataMigrationService.getPrjInfoList(prjInfoVo);
		result.add(getPrjInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : prjInfoSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "prjInfoSave.do")
	@ResponseBody
	public List<String> prjInfoSave(HttpServletRequest request, PrjInfoVO prjInfoVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjInfoVo.getJsonRowdatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {

				PrjInfoVO vo = objectMapper.readValue(jsonDatas[0], PrjInfoVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getPrjInfoList(vo).size() > 0) {
					dataMigrationService.deletePrjInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjInfoVO tmpBean = objectMapper.readValue(json, PrjInfoVO.class);
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
					//insert
					dataMigrationService.insertPrjInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getPrjMemberList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_member db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getMigPrjMemberList.do")
	@ResponseBody
	public Object getPrjMemberList(PrjMemberVO prjMemberVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjMemberVO> getPrjMemberListVo = dataMigrationService.getPrjMemberList(prjMemberVo);
		result.add(getPrjMemberListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : prjMemberSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_member db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "prjMemberSave.do")
	@ResponseBody
	public List<String> prjMemberSave(HttpServletRequest request, PrjMemberVO prjMemberVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjMemberVo.getJsonRowdatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjMemberVO vo = objectMapper.readValue(jsonDatas[0], PrjMemberVO.class);

				if (dataMigrationService.getPrjMemberList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deletePrjMember(vo);
				}
			
				
				for(String json : jsonDatas) {
					PrjMemberVO tmpBean = objectMapper.readValue(json, PrjMemberVO.class);
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
					//insert
					dataMigrationService.insertPrjMember(tmpBean);
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
	 * 
	 * 1. 메소드명 : getProInfoList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_pro_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getProInfoList.do")
	@ResponseBody
	public Object getProInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDocumentIndexVO> getProInfoListVo = dataMigrationService.getProInfoList(prjDocumentIndexVo);
		result.add(getProInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : proInfoSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_pro_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "proInfoSave.do")
	@ResponseBody
	public List<String> proInfoSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDocumentIndexVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {

				PrjDocumentIndexVO vo = objectMapper.readValue(jsonDatas[0], PrjDocumentIndexVO.class);

				if (dataMigrationService.getProInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteProInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
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
					//insert
					dataMigrationService.insertProInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getProStepList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_pro_step db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getProStepList.do")
	@ResponseBody
	public Object getProStepList(PrjStepVO prjStepVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjStepVO> getGendocInfoListVo = dataMigrationService.getProStepList(prjStepVo);
		result.add(getGendocInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : proStepSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_pro_step db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "proStepSave.do")
	@ResponseBody
	public List<String> proStepSave(HttpServletRequest request, PrjStepVO prjStepVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjStepVo.getTableArrayData(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjStepVO vo = objectMapper.readValue(jsonDatas[0], PrjStepVO.class);
				if (dataMigrationService.getProStepList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteProStep(vo);
				}
				
				for(String json : jsonDatas) {
					PrjStepVO tmpBean = objectMapper.readValue(json, PrjStepVO.class);
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
					//insert
					dataMigrationService.insertProStep(tmpBean);
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
	 * 
	 * 1. 메소드명 : getProcessInfoList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_pro_step db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getProcessInfoList.do")
	@ResponseBody
	public Object getProcessInfoList(ProcessInfoVO processInfoVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<ProcessInfoVO> getProcessInfoListVo = dataMigrationService.getProcessInfoList(processInfoVo);
		result.add(getProcessInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : processInfoSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_pro_step db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "processInfoSave.do")
	@ResponseBody
	public List<String> processInfoSave(HttpServletRequest request, ProcessInfoVO processInfoVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = processInfoVo.getTableArrayData(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {

				ProcessInfoVO vo = objectMapper.readValue(jsonDatas[0], ProcessInfoVO.class);

				if (dataMigrationService.getProcessInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteProcessInfoList(vo);
				}
				
				for(String json : jsonDatas) {
					ProcessInfoVO tmpBean = objectMapper.readValue(json, ProcessInfoVO.class);
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
					//insert
					dataMigrationService.insertProcessInfoList(tmpBean);
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
	 * 
	 * 1. 메소드명 : getSdcInfoList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_sdc_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getSdcInfoList.do")
	@ResponseBody
	public Object getSdcInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDocumentIndexVO> getSdcInfoListVo = dataMigrationService.getSdcInfoList(prjDocumentIndexVo);
		result.add(getSdcInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : sdcInfoSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_sdc_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "sdcInfoSave.do")
	@ResponseBody
	public List<String> sdcInfoSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDocumentIndexVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {

					PrjDocumentIndexVO vo = objectMapper.readValue(jsonDatas[0], PrjDocumentIndexVO.class);
					
					if(dataMigrationService.getSdcInfoList(vo).size() > 0) {
						// 기존 데이터 삭제
						dataMigrationService.deleteSdcInfo(vo);
					}
				
				for(String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
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
					//insert
					dataMigrationService.insertSdcInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getSdcStepList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_sdc_step db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getSdcStepList.do")
	@ResponseBody
	public Object getSdcStepList(PrjStepVO prjStepVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjStepVO> getSdcStepListVo = dataMigrationService.getSdcStepList(prjStepVo);
		result.add(getSdcStepListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : sdcStepSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_sdc_step db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "sdcStepSave.do")
	@ResponseBody
	public List<String> sdcStepSave(HttpServletRequest request, PrjStepVO prjStepVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjStepVo.getTableArrayData(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
			
					PrjStepVO vo = objectMapper.readValue(jsonDatas[0], PrjStepVO.class);
					
					if(dataMigrationService.getSdcStepList(vo).size() > 0) {
						// 기존 데이터 삭제
						dataMigrationService.deleteSdcStep(vo);
					}
			
				
				for(String json : jsonDatas) {
					PrjStepVO tmpBean = objectMapper.readValue(json, PrjStepVO.class);
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
					//insert
					dataMigrationService.insertSdcStep(tmpBean);
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
	 * 
	 * 1. 메소드명 : getStrFileList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_str_file db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getStrFileList.do")
	@ResponseBody
	public Object getStrFileList(PrjTrVO prjTrVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getGendocInfoListVo = dataMigrationService.getStrFileList(prjTrVo);
		result.add(getGendocInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : strFileSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_str_file db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "strFileSave.do")
	@ResponseBody
	public List<String> strFileSave(HttpServletRequest request, PrjTrVO prjTrVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjTrVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjTrVO vo = objectMapper.readValue(jsonDatas[0], PrjTrVO.class);

				if (dataMigrationService.getStrFileList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteStrFile(vo);
				}

				for(String json : jsonDatas) {
					PrjTrVO tmpBean = objectMapper.readValue(json, PrjTrVO.class);
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
					//insert
					dataMigrationService.insertStrFile(tmpBean);
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
	 * 
	 * 1. 메소드명 : getStrInfoList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_str_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getStrInfoList.do")
	@ResponseBody
	public Object getStrInfoList(PrjTrVO prjTrVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getStrInfoListVo = dataMigrationService.getStrInfoList(prjTrVo);
		result.add(getStrInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : strInfoSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_str_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "strInfoSave.do")
	@ResponseBody
	public List<String> strInfoSave(HttpServletRequest request, PrjTrVO prjTrVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjTrVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjTrVO vo = objectMapper.readValue(jsonDatas[0], PrjTrVO.class);
				if (dataMigrationService.getStrInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteStrInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjTrVO tmpBean = objectMapper.readValue(json, PrjTrVO.class);
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
					//insert
					dataMigrationService.insertStrInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getSystemPrefixList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_system_prefix db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getSystemPrefixList.do")
	@ResponseBody
	public Object getSystemPrefixList(PrjSystemPrefixVO prjSystemPrefixVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjSystemPrefixVO> getSystemPrefixListVo = dataMigrationService.getSystemPrefixList(prjSystemPrefixVo);
		result.add(getSystemPrefixListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : systemPrefixSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_system_prefix db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "systemPrefixSave.do")
	@ResponseBody
	public List<String> systemPrefixSave(HttpServletRequest request, PrjSystemPrefixVO prjSystemPrefixVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjSystemPrefixVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
					PrjSystemPrefixVO vo = objectMapper.readValue(jsonDatas[0], PrjSystemPrefixVO.class);
					
					if(dataMigrationService.getSystemPrefixList(vo).size() > 0) {
						// 기존 데이터 삭제
						dataMigrationService.deleteSystemPrefix(vo);
					}
				
				for(String json : jsonDatas) {
					PrjSystemPrefixVO tmpBean = objectMapper.readValue(json, PrjSystemPrefixVO.class);
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
					//insert
					dataMigrationService.insertSystemPrefix(tmpBean);
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
	 * 
	 * 1. 메소드명 : getTrFileList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_tr_file db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getTrFileList.do")
	@ResponseBody
	public Object getTrFileList(PrjTrVO prjTrVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getTrFileListVo = dataMigrationService.getTrFileList(prjTrVo);
		result.add(getTrFileListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : trFileSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_tr_file db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "trFileSave.do")
	@ResponseBody
	public List<String> trFileSave(HttpServletRequest request, PrjTrVO prjTrVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjTrVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjTrVO vo = objectMapper.readValue(jsonDatas[0], PrjTrVO.class);

				if (dataMigrationService.getTrFileList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteTrFile(vo);
				}
				
				for(String json : jsonDatas) {
					PrjTrVO tmpBean = objectMapper.readValue(json, PrjTrVO.class);
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
					//insert
					dataMigrationService.insertTrFile(tmpBean);
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
	 * 
	 * 1. 메소드명 : getTrInfoList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_tr_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getTrInfoList.do")
	@ResponseBody
	public Object getTrInfoList(PrjTrVO prjTrVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getTrInfoListVo = dataMigrationService.getTrInfoList(prjTrVo);
		result.add(getTrInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : trInfoSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_tr_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "trInfoSave.do")
	@ResponseBody
	public List<String> trInfoSave(HttpServletRequest request, PrjTrVO prjTrVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjTrVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjTrVO vo = objectMapper.readValue(jsonDatas[0], PrjTrVO.class);

				if (dataMigrationService.getTrInfoList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteTrInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjTrVO tmpBean = objectMapper.readValue(json, PrjTrVO.class);
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
					//insert
					dataMigrationService.insertTrInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getVdrInfoList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vdr_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getVdrInfoList.do")
	@ResponseBody
	public Object getVdrInfoList(PrjDocumentIndexVO prjDocumentIndexVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDocumentIndexVO> getVdrInfoListVo = dataMigrationService.getVdrInfoList(prjDocumentIndexVo);
		result.add(getVdrInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : sdcInfoSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vdr_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "vdrInfoSave.do")
	@ResponseBody
	public List<String> vdrInfoSave(HttpServletRequest request, PrjDocumentIndexVO prjDocumentIndexVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjDocumentIndexVo.getJsonRowDatas();
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
			
				PrjDocumentIndexVO vo = objectMapper.readValue(jsonDatas[0], PrjDocumentIndexVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getVdrInfoList(vo).size() > 0) {
					dataMigrationService.deleteVdrInfo(vo);
				}
				
				for(String json : jsonDatas) {
					PrjDocumentIndexVO tmpBean = objectMapper.readValue(json, PrjDocumentIndexVO.class);
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
					//insert
					dataMigrationService.insertVdrInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getVdrStepList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vdr_step db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getVdrStepList.do")
	@ResponseBody
	public Object getVdrStepList(PrjStepVO prjStepVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjStepVO> getVdrStepListVo = dataMigrationService.getVdrStepList(prjStepVo);
		result.add(getVdrStepListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : vdrStepSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vdr_step db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "vdrStepSave.do")
	@ResponseBody
	public List<String> vdrStepSave(HttpServletRequest request, PrjStepVO prjStepVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjStepVo.getTableArrayData(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {

				PrjStepVO vo = objectMapper.readValue(jsonDatas[0], PrjStepVO.class);
				// 기존 데이터 삭제
				if (dataMigrationService.getVdrStepList(vo).size() > 0) {
					dataMigrationService.deleteVdrStep(vo);
				}
				
				for(String json : jsonDatas) {
					PrjStepVO tmpBean = objectMapper.readValue(json, PrjStepVO.class);
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
					//insert
					dataMigrationService.insertVdrStep(tmpBean);
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
	 * 
	 * 1. 메소드명 : getVtrFileList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vtr_file db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getVtrFileList.do")
	@ResponseBody
	public Object getVtrFileList(PrjTrVO prjTrVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getVtrFileListVo = dataMigrationService.getVtrFileList(prjTrVo);
		result.add(getVtrFileListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : vtrFileSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vtr_file db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "vtrFileSave.do")
	@ResponseBody
	public List<String> vtrFileSave(HttpServletRequest request, PrjTrVO prjTrVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjTrVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjTrVO vo = objectMapper.readValue(jsonDatas[0], PrjTrVO.class);

				if (dataMigrationService.getVtrFileList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteVtrFile(vo);
				}
				
				for(String json : jsonDatas) {
					PrjTrVO tmpBean = objectMapper.readValue(json, PrjTrVO.class);
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
					//insert
					dataMigrationService.insertVtrFile(tmpBean);
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
	 * 
	 * 1. 메소드명 : getVtrInfoList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vtr_info db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getVtrInfoList.do")
	@ResponseBody
	public Object getVtrInfoList(PrjTrVO prjTrVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjTrVO> getVtrInfoListVo = dataMigrationService.getVtrInfoList(prjTrVo);
		result.add(getVtrInfoListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : vtrInfoSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_vtr_info db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "vtrInfoSave.do")
	@ResponseBody
	public List<String> vtrInfoSave(HttpServletRequest request, PrjTrVO prjTrVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjTrVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
					PrjTrVO vo = objectMapper.readValue(jsonDatas[0], PrjTrVO.class);
					
					if(dataMigrationService.getVtrInfoList(vo).size() > 0) {
						// 기존 데이터 삭제
						dataMigrationService.deleteVtrInfo(vo);
					}
				
				for(String json : jsonDatas) {
					PrjTrVO tmpBean = objectMapper.readValue(json, PrjTrVO.class);
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
					//insert
					dataMigrationService.insertVtrInfo(tmpBean);
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
	 * 
	 * 1. 메소드명 : getMigWbsCodeList
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_wbs_code db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getMigWbsCodeList.do")
	@ResponseBody
	public Object getMigWbsCodeList(PrjWbsCodeVO prjWbsCodeVo) {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjWbsCodeVO> getMigWbsCodeListVo = dataMigrationService.getMigWbsCodeList(prjWbsCodeVo);
		result.add(getMigWbsCodeListVo);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : migWbsCodeSave
	 * 2. 작성일: 2022. 02. 05.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_prj_wbs_code db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "migWbsCodeSave.do")
	@ResponseBody
	public List<String> migWbsCodeSave(HttpServletRequest request, PrjWbsCodeVO prjWbsCodeVo) {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = prjWbsCodeVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				PrjWbsCodeVO vo = objectMapper.readValue(jsonDatas[0], PrjWbsCodeVO.class);

				if (dataMigrationService.getMigWbsCodeList(vo).size() > 0) {
					// 기존 데이터 삭제
					dataMigrationService.deleteMigWbsCode(vo);
				}
				
				for(String json : jsonDatas) {
					PrjWbsCodeVO tmpBean = objectMapper.readValue(json, PrjWbsCodeVO.class);
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
					//insert
					dataMigrationService.insertMigWbsCode(tmpBean);
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
	 * 
	 * 1. 메소드명 : getMigUsersList
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_users db정보 불러오기
	 * 5. 수정일:
	 */
	@RequestMapping(value = "getMigUsersList.do")
	@ResponseBody
	public Object getMigUsersList(UsersVO usersVo) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		List<Object> result = new ArrayList<Object>();
		
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		
		List<UsersVO> getMigUsersListVo = dataMigrationService.getMigUsersList(usersVo);
		List<UsersVO> list = new ArrayList<UsersVO>();
		// 메일이랑 전화번호 복호화
		for(int i=0; i<getMigUsersListVo.size(); i++) {
			UsersVO getUsersVO = new UsersVO();
			String user_id = getMigUsersListVo.get(i).getUser_id();
			String user_kor_nm = getMigUsersListVo.get(i).getUser_kor_nm();
			String user_group_nm = getMigUsersListVo.get(i).getUser_group_nm();
			String user_eng_nm = getMigUsersListVo.get(i).getUser_eng_nm();
			String offi_tel = getMigUsersListVo.get(i).getOffi_tel();
				if(offi_tel == null) offi_tel = "";
				   offi_tel = aes256.aesDecode(offi_tel);
			String email_addr = getMigUsersListVo.get(i).getEmail_addr();
				if(email_addr == null) email_addr = "";
				   email_addr = aes256.aesDecode(email_addr);
			String company_nm = getMigUsersListVo.get(i).getCompany_nm();
			
			String user_address = getMigUsersListVo.get(i).getUser_address();
			String user_type = getMigUsersListVo.get(i).getUser_type();
			String passwd = getMigUsersListVo.get(i).getPasswd();
			String first_login_yn = getMigUsersListVo.get(i).getFirst_login_yn();
			String hld_offi_gbn = getMigUsersListVo.get(i).getHld_offi_gbn();
			String job_tit_cd = getMigUsersListVo.get(i).getJob_tit_cd();
			String offi_res_cd = getMigUsersListVo.get(i).getOffi_res_cd();
			String admin_yn = getMigUsersListVo.get(i).getAdmin_yn();
			String sfile_nm = getMigUsersListVo.get(i).getSfile_nm();
			String rfile_nm = getMigUsersListVo.get(i).getRfile_nm();
			String file_path = getMigUsersListVo.get(i).getFile_path();
			String last_con_prj_id = getMigUsersListVo.get(i).getLast_con_prj_id();
			String remote_ip = getMigUsersListVo.get(i).getRemote_ip();
			String user_st = getMigUsersListVo.get(i).getUser_st();
			String reg_id = getMigUsersListVo.get(i).getReg_id();
			String reg_date = getMigUsersListVo.get(i).getReg_date();
			String mod_id = getMigUsersListVo.get(i).getMod_id();
			String mod_date = getMigUsersListVo.get(i).getMod_date();
			
			getUsersVO.setUser_id(user_id);
			getUsersVO.setUser_kor_nm(user_kor_nm);
			getUsersVO.setUser_group_nm(user_group_nm);
			getUsersVO.setUser_eng_nm(user_eng_nm);
			getUsersVO.setOffi_tel(offi_tel);
			getUsersVO.setEmail_addr(email_addr);
			getUsersVO.setCompany_nm(company_nm);
			
			getUsersVO.setUser_address(user_address);
			getUsersVO.setUser_type(user_type);
			getUsersVO.setPasswd(passwd);
			getUsersVO.setFirst_login_yn(first_login_yn);
			getUsersVO.setHld_offi_gbn(hld_offi_gbn);
			getUsersVO.setJob_tit_cd(job_tit_cd);
			getUsersVO.setOffi_res_cd(offi_res_cd);
			getUsersVO.setAdmin_yn(admin_yn);
			getUsersVO.setSfile_nm(sfile_nm);
			getUsersVO.setRfile_nm(rfile_nm);
			getUsersVO.setFile_path(file_path);
			getUsersVO.setLast_con_prj_id(last_con_prj_id);
			getUsersVO.setRemote_ip(remote_ip);
			getUsersVO.setUser_st(user_st);
			getUsersVO.setReg_id(reg_id);
			getUsersVO.setReg_date(reg_date);
			getUsersVO.setMod_id(mod_id);
			getUsersVO.setMod_date(mod_date);
			list.add(i, getUsersVO);
		}		
		result.add(list);
		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : migUsersSave
	 * 2. 작성일: 2022. 02. 04.
	 * 3. 작성자: jeongwoo
	 * 4. 설명: dataMigration p_users db 저장
	 * 5. 수정일:
	 */
	@RequestMapping(value = "migUsersSave.do")
	@ResponseBody
	public List<String> migUsersSave(HttpServletRequest request, UsersVO usersVo) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		String reg_id = sessioninfo.getUser_id();
		
		List<UsersVO> getMigUsersListVo = dataMigrationService.getMigUsersList(usersVo);
		
		String key = "CODEKEYVISIONDCS";
		AES256Util aes256 = new AES256Util(key);
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		
		String[] jsonDatas = usersVo.getJsonRowDatas(); //사용하던 String 배열 사용
		
		List<String> status = new ArrayList<>();
		
		status.add("SUCCESS");
		
		if (jsonDatas != null) {
			// 기존 데이터 삭제
			//dataMigrationService.deleteProStep(usersVo);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				for(String json : jsonDatas) {
					UsersVO tmpBean = objectMapper.readValue(json, UsersVO.class);
					
					// 저장된 유저 아이디 정보가 이미 있으면 F 없으면 T, T일때만 insert
					String saveUserchk = "T";
					for(int i=0; i<getMigUsersListVo.size(); i++) {
						if(tmpBean.getUser_id().toUpperCase().equals(getMigUsersListVo.get(i).getUser_id().toUpperCase())) {
							saveUserchk = "F";
						}
					}
					
					//System.out.println(tmpBean.getUser_id()+"="+saveUserchk);
					
					if(saveUserchk == "T") {
						String Id = tmpBean.getUser_id().toUpperCase();
						String newPwd = tmpBean.getPasswd();
						if(newPwd==null || newPwd.equals("")) {
							newPwd = tmpBean.getUser_id();
						}
						String aes_pwd = aes256.aesEncode(newPwd);
						String temp_pwd = Id + aes_pwd;
						System.out.println("temp_pwd = "+temp_pwd);
						tmpBean.setPasswd(GetSHA256.getHashcode(String.valueOf(temp_pwd)));
						String aes_tel = aes256.aesEncode(tmpBean.getOffi_tel());
						tmpBean.setOffi_tel(aes_tel);
						String aes_email = aes256.aesEncode(tmpBean.getEmail_addr());
						tmpBean.setEmail_addr(aes_email);
//						tmpBean.setReg_id(sessioninfo.getUser_id());
//						tmpBean.setReg_date(CommonConst.currentDateAndTime());
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
						// 유저 id 대문자로 저장
						tmpBean.setUser_id(Id);
						//insert
						dataMigrationService.insertMigUsers(tmpBean);
					}
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				status.set(0, "FAIL");
				status.add(e.getMessage());				
			}
		}
		
		return status;
	}
	
}
