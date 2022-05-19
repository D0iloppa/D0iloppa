/**
 * 
 */
package kr.co.doiloppa.controller;

import java.io.IOException;

/**
 * @author doil
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.doiloppa.excel.ExcelFileDownload;
import kr.co.doiloppa.model.DocumentIndexVO;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.GlobalSearchVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.model.WbsCodeControlVO;
import kr.co.doiloppa.service.ProjectGenService;
import kr.co.doiloppa.service.SearchService;

@Controller
@RequestMapping("/*")

public class ProjectGenCont {

	protected ProjectGenService projectGenService;

	@Autowired
	public void setTreeService(ProjectGenService projectGenService) {
		this.projectGenService = projectGenService;
	}

	/**
	 * 1. 메소드명 : getStep02Table 2. 작성일: 2021-11-29 3. 작성자: 권도일 4. 설명: 프로세스 타입, 폴더매치
	 * 테이블을 그려준다. 5. 수정일:
	 */

	@RequestMapping(value = "pg02_getGrid.do")
	@ResponseBody
	public Object getStep02Table(ProcessInfoVO processInfoVo) {

		List<Object> result = new ArrayList<Object>();

		List<ProcessInfoVO> getSystemClass = projectGenService.getSystemClass(processInfoVo);
		for (ProcessInfoVO bean : getSystemClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		List<ProcessInfoVO> getManualClass = projectGenService.getManualClass(processInfoVo);
		for (ProcessInfoVO bean : getManualClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		result.add(getSystemClass);
		result.add(getManualClass);

		return result;
	}

	/**
	 * 1. 메소드명 : step02_addClass 2. 작성일: 2021-11-29 3. 작성자: 권도일 4. 설명: 프로세스 클래스 추가버튼
	 * 눌렀을 경우 db에 insert시켜주고, 갱신 정보 리턴해주는 컨트롤러 5. 수정일:
	 */

	@RequestMapping(value = "pg02_addClass.do")
	@ResponseBody
	public Object step02_addClass(ProcessInfoVO processInfoVo) {

		List<Object> result = new ArrayList<Object>();


		// insert
		String msg = projectGenService.insertManualClass(processInfoVo);

		// 추가 후 갱신된 데이터
		List<ProcessInfoVO> getSystemClass = projectGenService.getSystemClass(processInfoVo);
		for (ProcessInfoVO bean : getSystemClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		List<ProcessInfoVO> getManualClass = projectGenService.getManualClass(processInfoVo);
		for (ProcessInfoVO bean : getManualClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		result.add(getSystemClass);
		result.add(getManualClass);
		result.add(msg);

		return result;
	}

	/**
	 * 1. 메소드명 : step02_updateClass 2. 작성일: 2021-11-29 3. 작성자: 권도일 4. 설명: 프로세스 클래스
	 * 내용 update시 처리하는 메소드 5. 수정일:
	 */
	@RequestMapping(value = "pg02_updateClass.do")
	@ResponseBody
	public Object step02_updateClass(ProcessInfoVO processInfoVo) {

		List<Object> result = new ArrayList<Object>();

		// update
		String msg = projectGenService.updateClass(processInfoVo);

		// 추가 후 갱신된 데이터
		List<ProcessInfoVO> getSystemClass = projectGenService.getSystemClass(processInfoVo);
		for (ProcessInfoVO bean : getSystemClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		List<ProcessInfoVO> getManualClass = projectGenService.getManualClass(processInfoVo);
		for (ProcessInfoVO bean : getManualClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		result.add(getSystemClass);
		result.add(getManualClass);
		result.add(msg);

		return result;
	}

	/**
	 * 1. 메소드명 : step02_delClass 2. 작성일: 2021-11-29 3. 작성자: 권도일 4. 설명: 프로세스 클래스 삭제시,
	 * 처리해주는 메소드 5. 수정일:
	 */
	@RequestMapping(value = "pg02_delClass.do")
	@ResponseBody
	public Object step02_delClass(ProcessInfoVO processInfoVo) {

		List<Object> result = new ArrayList<Object>();

		// 삭제 전 validation 체크 후, 삭제할 list 재구성
		// 입력된 process_id 리스트
		String chkk = processInfoVo.getProcess_seq_list();
		String[] chkList = chkk.split(",");
		String prj_id = processInfoVo.getPrj_id();

		List<String> resultList = new ArrayList<>(); // 검증 후, 삭제 가능한 process Id의 리스트 생성
		
		for (String line : chkList) {
			if (line.length() < 1)
				continue;
			ProcessInfoVO tmpVO = new ProcessInfoVO();
			if(line.equals("undefined") || line.equals("")) continue;
			tmpVO.setProcess_id(line);
			tmpVO.setPrj_id(prj_id);
			String getFolder_id = projectGenService.selectOne("projectGenSqlMap.pg02_getFolderId", tmpVO);
			if(getFolder_id == null || getFolder_id.equals("")) getFolder_id = "0";
			long folder_id = Long.parseLong(getFolder_id);
			tmpVO.setFolder_id(folder_id);
			String getFolderPath = projectGenService.selectOne("projectGenSqlMap.getFolderPath_pg02",tmpVO);
			tmpVO.setFolder_path(getFolderPath+"%");
			long count = projectGenService.selectOne("projectGenSqlMap.isIndocAtSubFolder_pg02",tmpVO);
			
			
			/*
			tmpVO = projectGenService.pg02_getDelValidation(tmpVO);
			int count = (int) tmpVO.getFolder_id();
			*/
			// 해당 폴더에 파일 존재x 삭제가능
			if (count < 1)
				resultList.add(line);
		}

		// 삭제처리
		if (resultList.size() > 0) { // 삭제가능한 process id의 리스트가 1개 이상일 경우에만 진행
			// 쿼리문 처리를 위한 삭제할 process id 리스트의 스트링 포맷
			String processIdList = "";
			for (String processId : resultList)
				processIdList += (processId + ",");
			processIdList = processIdList.substring(0, processIdList.length() - 1); // 제일 뒤의 ,제거
			processInfoVo.setProcess_seq_list(processIdList);
			projectGenService.delManualClass(processInfoVo); // 체크하지 않고 삭제한 경우 제외
		}

		// 삭제후 갱신된 데이터
		List<ProcessInfoVO> getSystemClass = projectGenService.getSystemClass(processInfoVo);
		for (ProcessInfoVO bean : getSystemClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		List<ProcessInfoVO> getManualClass = projectGenService.getManualClass(processInfoVo);
		for (ProcessInfoVO bean : getManualClass) {
			String chk = bean.getProcess_folder_path_id();

			if (chk != null) {
				ProcessInfoVO tmp = new ProcessInfoVO();
				String pathId = bean.getProcess_folder_path_id();
				String[] ids = pathId.split("@");
				tmp.setPrj_id(bean.getPrj_id());

				String folder_String = "";
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].isEmpty())
						continue;
					long folder_id = Long.parseLong(ids[i]);
					tmp.setFolder_id(folder_id);
					String tmpPath = projectGenService.getFolderPathNm(tmp);
					//folder_String += "'" + tmpPath + "'" + ",";
					folder_String = tmpPath;
				}
				bean.setProcess_folder_path_string(folder_String);
			}
		}

		result.add(getSystemClass);
		result.add(getManualClass);
		result.add(resultList.size()); // data[2]에 삭제한 클래스의 갯수를 포함

		return result;
	}

	/**
	 * 1. 메소드명 : step02_getPath 2. 작성일: 2021-11-29 3. 작성자: 권도일 4. 설명: db상의 pathId를
	 * 실제 path String으로 리턴시켜주는 메소드 5. 수정일:
	 */
	@RequestMapping(value = "pg02_getPath.do")
	@ResponseBody
	public Object step02_getPath(ProcessInfoVO processInfoVo) {

		List<Object> result = new ArrayList<Object>();

		String pathString = processInfoVo.getProcess_folder_path_id();
		String[] paths = pathString.split("@");

		List<String> pathList = new ArrayList<>();
		if (paths[0].length() > 0)
			for (int i = 0; i < paths.length; i++) {
				processInfoVo.setFolder_id(Long.parseLong(paths[i]));
				String tmp = projectGenService.getFolderPathNm(processInfoVo);
				pathList.add(tmp);
			}

		result.add(pathList); // data[0] : path의 실제 string 경로
		result.add(pathString); // data[1] : db상에 저장된 폴더의 id들

		/*
		 * // 추가후 갱신된 데이터 List<ProcessInfoVO> getSystemClass =
		 * projectGenService.getSystemClass(processInfoVo); List<ProcessInfoVO>
		 * getManualClass = projectGenService.getManualClass(processInfoVo);
		 * 
		 * result.add(getSystemClass); result.add(getManualClass);
		 */

		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : pg02_getRvc
	 * 2. 작성일: 2022. 2. 24.
	 * 3. 작성자: doil
	 * 4. 설명: review conclusion 값을 가져온다. 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "pg02_getRvc.do")
	@ResponseBody
	public ModelAndView pg02_getRvc(ProcessInfoVO processInfoVo) {

		ModelAndView result = new ModelAndView();

		
		String rvc = projectGenService.getRvc(processInfoVo.getPrj_id());
		
		result.addObject("rvc", rvc);


		return result;
	}
	
	@RequestMapping(value = "pg02_rvcUpdate.do")
	@ResponseBody
	public ModelAndView pg02_rvcUpdate(HttpServletRequest request, PrjCodeSettingsVO code) {

		ModelAndView result = new ModelAndView();

		result.addObject("status", "SUCCESS");
		
		// 로그인 세션 정보
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		code.setReg_id(sessioninfo.getUser_id());
		code.setMod_id(sessioninfo.getUser_id());

		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		code.setReg_date(reg_date);
		code.setMod_date(reg_date);

		String rvc = projectGenService.getRvc(code.getPrj_id());
		try {
			if(rvc.equals("")) {  // rvc가 db상에 존재하지 않으면 insert
				if(projectGenService.insert("projectGenSqlMap.insertRvc",code)<1)
					throw new Exception("rvc 삽입 실패");
			}
			else { 
				if(projectGenService.update("projectGenSqlMap.updateRvc",code)<1)
					throw new Exception("rvc 업데이트 실패 실패");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			result.addObject("error",e.getStackTrace());
			result.addObject("status", "FAIL");
		}
		

		return result;
	}
	
	/**
	 * 1. 메소드명 : pg03_getSerialNo
	 * 2. 작성일: 2022. 2. 24.
	 * 3. 작성자: doil
	 * 4. 설명: email & letter의 시리얼 넘버 값을 가져옴 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "pg03_getSerialNo.do")
	@ResponseBody
	public ModelAndView pg03_getSerialNo(PrjCodeSettingsVO vo) {

		ModelAndView result = new ModelAndView();
		
		String serialNo = projectGenService.selectOne("projectGenSqlMap.pg03_getSerialNo",vo.getPrj_id());
		
		result.addObject("serial", serialNo);

		return result;
	}
	
	/**
	 * 1. 메소드명 : pg03_updateSerialNo
	 * 2. 작성일: 2022. 2. 24.
	 * 3. 작성자: doil
	 * 4. 설명: 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "pg03_updateSerialNo.do")
	@ResponseBody
	public ModelAndView pg03_updateSerialNo(PrjCodeSettingsVO vo) {

		ModelAndView result = new ModelAndView();
		
		result.addObject("status", "SUCCESS");
		try {
			if (projectGenService.update("projectGenSqlMap.pg03_updateSerialNo", vo) < 1)
				throw new Exception("serial no 설정 실패");
		} catch (Exception e) {
			result.addObject("status", "FAIL");
		}

		return result;
	}
	
	/**
	 * 1. 메소드명 : pg03_getGrid
	 * 2. 작성일: 2021-12-15
	 * 3. 작성자: 권도일
	 * 4. 설명: Correspondence 관련 값 가져오는 메소드
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg03_getGrid.do")
	@ResponseBody
	public Object pg03_getGrid(PrjCodeSettingsVO prjCodesettingsVO){
		List<Object> result = new ArrayList<Object>();
		
		// CORRESPONDENCE에 매핑되어 있는 모든 데이터를 가져옴
		List<PrjCodeSettingsVO> getData = projectGenService.pg03_getGrid(prjCodesettingsVO);
		
		// 각 그리드에 맞게끔 분류
		List<PrjCodeSettingsVO> gridType = new ArrayList<PrjCodeSettingsVO>();
		List<PrjCodeSettingsVO> gridFrom = new ArrayList<PrjCodeSettingsVO>();
		List<PrjCodeSettingsVO> gridOut = new ArrayList<PrjCodeSettingsVO>();
		List<PrjCodeSettingsVO> gridIn = new ArrayList<PrjCodeSettingsVO>();
		
		for(PrjCodeSettingsVO item : getData) 
			switch(item.getSet_code_type()) {
				case "CORRESPONDENCE_TYPE":
					gridType.add(item);
					break;
				case "CORRESPONDENCE_FROM":
					gridFrom.add(item);
					break;
				case "CORRESPONDENCE_OUT":
					gridOut.add(item);
					break;
				case "CORRESPONDENCE_IN":
					gridIn.add(item);
					break;
			}
		
		for(PrjCodeSettingsVO item : gridOut) {
			String[] valStr = new String[3];
			valStr[0] = getStrFromId(gridFrom, item.getSet_val());
			valStr[1] = getStrFromId(gridFrom, item.getSet_val2());
			valStr[2] = getStrFromId(gridType, item.getSet_val3());
			item.setSet_valStr(valStr);
		}
		
		for(PrjCodeSettingsVO item : gridIn) {
			String[] valStr = new String[3];
			valStr[0] = getStrFromId(gridFrom, item.getSet_val());
			valStr[1] = getStrFromId(gridFrom, item.getSet_val2());
			valStr[2] = getStrFromId(gridType, item.getSet_val3());
			item.setSet_valStr(valStr);
		}
		

		result.add(gridType);
		result.add(gridFrom);
		result.add(gridOut);
		result.add(gridIn);
		
		
		
		return result;
	}
	
		/**
		 * 1. 메소드명 : getStrFromId
		 * 2. 작성일: 2021. 12. 15.
		 * 3. 작성자: doil
		 * 4. 설명: code 셋팅 DB 리스트에서 id와 매핑되는 string 값을 가져온다.
		 * 5. 수정일: doil
		 */
	private String getStrFromId(List<PrjCodeSettingsVO> list, String string) {
		// TODO Auto-generated method stub
		
		for(PrjCodeSettingsVO item : list)
			if(item.getSet_code_id().equals(string)) return item.getSet_code();
		
		return null;
	}
	
	
	
	/**
	 * 1. 메소드명 : pg03_copyCode 
	 * 2. 작성일: 2021-12-15 
	 * 3. 작성자: 권도일
	 * 4. 설명: 다른 프로젝트에 저장되어 있는 값을 복사해온다. 
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg03_copyCode.do")
	@ResponseBody
	public Object pg03_copyCode(PrjCodeSettingsVO prjCodeSettingsVO){
		
		List<Object> result = new ArrayList<Object>();

		// 디폴트 프로젝트 ID인 0000000001는 다른 프로젝트로 부터 복사 붙여넣기 하지 않는다.
		if (prjCodeSettingsVO.getPrj_id().equals("0000000001")) {
			result.add("기본 프로젝트 복사 불가");
			return result;
		}
		
		// 복사 전, 현재 테이블은 비운다.
		projectGenService.pg03_ClearTbl(prjCodeSettingsVO);
		// 제거 후, 데이터를 복사해 온다.
		projectGenService.pg03_copyTbl (prjCodeSettingsVO);
		
		result.add("SUCCESS");
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg03_updateCode 
	 * 2. 작성일: 2021-12-17 
	 * 3. 작성자: 권도일 
	 * 4. 설명: 화면상의 수정 정보를 DB에 반영 
	 * 5. 수정일:
	 * 
	 * @throws JSONException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "pg03_updateCode.do")
	@ResponseBody
	@JsonIgnoreProperties(ignoreUnknown = true)
	public void pg03_updateCode(PrjCodeSettingsVO prjCodeSettingsVO)
			throws JsonParseException, JsonMappingException, IOException {
		
		// 삭제된 데이터가 있는 경우 삭제 먼저 수행
		String[] deletedRows = prjCodeSettingsVO.getDeleteRows();
		

		if(deletedRows != null) {
			PrjCodeSettingsVO tmpDelBean = prjCodeSettingsVO;
			for(String set_code_id : deletedRows) {
				System.out.println(set_code_id + " 삭제");
				tmpDelBean.setSet_code_id(set_code_id);
				int result = projectGenService.pg03_delCode(tmpDelBean);
			}
		}
		
		
		// 배열로 받은 경우
		String[] jsonDatas = prjCodeSettingsVO.getJsonRowdatas();
		if(jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			// json배열에서 하나씩 꺼내서 update or insert
			try {
				int updatedRowsCount = 0;
				
				// 그리드로부터 받은 row들을 update 한다.
				for(String json : jsonDatas) {
					// json을 VO로 변환
					System.out.println(json);
					
					PrjCodeSettingsVO tmpBean = objectMapper.readValue(json, PrjCodeSettingsVO.class);
					
					// insert, update전 앞뒤 공백 제거
					String set_code = tmpBean.getSet_code();
					String set_desc = tmpBean.getSet_desc();
					String set_val4 = tmpBean.getSet_val4();
					
					if(set_code != null)
						tmpBean.setSet_code(set_code.trim());
					if(set_desc != null)
						tmpBean.setSet_desc(set_desc.trim());
					if(set_val4 != null)
						tmpBean.setSet_val4(set_val4.trim());
					
					// 업데이트 하기 전에, 해당 데이터의 존재여부(id로 확인)에 따라 update 혹은 insert 분기
					int cnt = projectGenService.pg03_chkCodeId(tmpBean);
					
					// 해당 code_id가 존재하면 update
					if (cnt > 0) {
						// 업데이트 try
						try {
							int result = projectGenService.pg03_updateCode(tmpBean);
							// update한 row의 갯수가 0개 이하일 경우
							if (result < 1) throw new Exception(tmpBean.getSet_code_id() + " : 해당 row 업데이트 실패");
							updatedRowsCount++;
						}catch (Exception e1) { // 예외 발생시 오류 메시지 출력 후 종료
							System.out.println(e1.getMessage());
						  }		
					}
					
					// 해당 code_id가 존재하지 않으면 insert
					else {
						try {
							int result = projectGenService.pg03_insertCode(tmpBean);
							// insert한 row의 갯수가 0개 이하일 경우
							if (result < 1) throw new Exception(tmpBean.getSet_code_id() + " : 해당 row insert 실패");
							updatedRowsCount++;
						}catch(Exception e1) {
							System.out.println(e1.getMessage());
						}
					}
				}
				// 모든 row를 db에 반영 요청하고, 잘 반영되었는지 확인
				if(updatedRowsCount != jsonDatas.length) throw new Exception("update 요청 row 갯수와, 반영된 row 갯수 불일치");
				
			} catch(Exception e2){
				e2.printStackTrace();
			}
			
			
			
		}
		

	}
	
	@RequestMapping(value = "pg04_del.do")
	@ResponseBody
	public ModelAndView pg04_del(PrjCodeSettingsVO prjCodeSettingsVO) throws JsonParseException, JsonMappingException, IOException {

		ModelAndView result = new ModelAndView();
		
		String[] delJsonRows = prjCodeSettingsVO.getJsonRowdatas();
		
		List<String> delval = new ArrayList<>();
		
		if(delJsonRows != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			for(String json : delJsonRows) {
				// json을 VO로 변환
				System.out.println(json);
				
				PrjCodeSettingsVO tmpBean = objectMapper.readValue(json, PrjCodeSettingsVO.class);
				int cnt = projectGenService.selectOne("prjcodesettingsSqlMap.pg05_chkCodeId",tmpBean);
				// DB에 존재하지 않는 코드는 지나감
				if(cnt<1) continue;
				try {
					if(projectGenService.pg04_del(tmpBean) < 1)
						throw new Exception(tmpBean.getSet_code_id() + " : 해당 코드를 사용하는 도서 존재");
				} catch(Exception e) {
					result.addObject("del_validation",e.getMessage());
					delval.add(tmpBean.getSet_code_id());
				}
			}
		}
		
		result.addObject("delval",delval);
		
		return result;
	}
	
	

	/**
	 * 1. 메소드명 : pg04_getCode 2. 작성일: 2021-12-01 3. 작성자: 권도일 4. 설명: 코드 필드값 DB에서 가져오는
	 * 메소드 5. 수정일:
	 */
	@RequestMapping(value = "pg04_getCode.do")
	@ResponseBody
	public Object pg04_getCode(PrjCodeSettingsVO prjCodeSettingsVO) {

		List<Object> result = new ArrayList<Object>();

		List<PrjCodeSettingsVO> codeList = projectGenService.pg04_getCode(prjCodeSettingsVO);
		
		result.add(codeList);

		return result;
	}
	
	/**
	 * 1. 메소드명 : pg04_getStep 
	 * 2. 작성일: 2021-12-01 
	 * 3. 작성자: 권도일 
	 * 4. 설명: 그리드의 select 항목에 들어갈 STEP parameter 값들을 불러옴
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg04_getStep.do")
	@ResponseBody
	public Object pg04_getStep(PrjCodeSettingsVO prjCodeSettingsVO) {

		List<Object> result = new ArrayList<Object>();
		
		// String[] steps = {"ENGINEERING STEP","VENDER DOC STEP","SITE DOC STEP","PROCURMENT STEP"};

		List<PrjCodeSettingsVO> stepList = projectGenService.pg04_getStep(prjCodeSettingsVO);
		
		List<PrjCodeSettingsVO> engStep = new ArrayList<>();
		List<PrjCodeSettingsVO> vendorStep = new ArrayList<>();
		List<PrjCodeSettingsVO> siteStep = new ArrayList<>();
		List<PrjCodeSettingsVO> procurmentStep = new ArrayList<>();
		
		for(PrjCodeSettingsVO row : stepList) {
			switch(row.getSet_code_type()) {
				case "ENGINEERING STEP" :
					engStep.add(row);
					break;
				case "VENDOR DOC STEP" :
					vendorStep.add(row);
					break;
				case "SITE DOC STEP" :
					siteStep.add(row);
					break;
				case "PROCURMENT STEP" :
					procurmentStep.add(row);
					break;
			}
			
		}

		result.add(engStep);
		result.add(vendorStep);
		result.add(siteStep);
		result.add(procurmentStep);

		return result;
	}
	

	/**
	 * 1. 메소드명 : pg04_updateCode 2. 작성일: 2021-12-01 3. 작성자: 권도일 4. 설명: 화면상의 수정 정보를
	 * DB에 반영 5. 수정일:
	 * 
	 * @throws JSONException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@RequestMapping(value = "pg04_updateCode.do")
	@ResponseBody
	public void pg04_updateCode(PrjCodeSettingsVO prjCodeSettingsVO)
			throws JsonParseException, JsonMappingException, IOException {
		
		/*
		String[] deletedRows = prjCodeSettingsVO.getDeleteRows();
		// 삭제된 데이터가 있는 경우 삭제
		if(deletedRows != null) {
			PrjCodeSettingsVO tmpDelBean = prjCodeSettingsVO;
			for(String set_code_id : deletedRows) {
				System.out.println(set_code_id + " 삭제");
				tmpDelBean.setSet_code_id(set_code_id);
				projectGenService.pg04_delCode(tmpDelBean);
			}
		}
		*/
		
		// 배열로 받은 경우
		String[] jsonDatas = prjCodeSettingsVO.getJsonRowdatas();
		if(jsonDatas != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			// json배열에서 하나씩 꺼내서 update or insert
			for(String json : jsonDatas) {
				// json을 VO로 변환
				PrjCodeSettingsVO tmpBean = objectMapper.readValue(json, PrjCodeSettingsVO.class);
				
				//update,insert 하기전에 앞뒤 공백제거
				tmpBean.setSet_code((tmpBean.getSet_code()).trim());
				String desc = tmpBean.getSet_desc();
				if(desc!=null) desc = desc.trim();
				else if(desc == null) desc = "";
				
				tmpBean.setSet_desc(desc);
				
				// 업데이트 하기 전에, 해당 데이터의 존재여부(id로 확인)에 따라 update 혹은 insert 분기
				int cnt = projectGenService.pg04_chkCodeId(tmpBean);
				// 해당 code_id가 존재하면 update
				if (cnt > 0) projectGenService.pg04_updateCode(tmpBean);
				// 해당 code_id가 존재하지 않으면 insert
				else projectGenService.pg04_insertCode(tmpBean);
				
				
			}
			
		}
		

	}
	
	
	/**
	 * 1. 메소드명 : pg04_copyCode 
	 * 2. 작성일: 2021-12-13 
	 * 3. 작성자: 권도일
	 * 4. 설명: 화면상의 수정 정보를 DB에 반영 
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg04_copyCode.do")
	@ResponseBody
	public ModelAndView pg04_copyCode(PrjCodeSettingsVO prjCodeSettingsVO){
		
		ModelAndView result = new ModelAndView();
		
		// 디폴트 프로젝트 ID인 0000000001는 다른 프로젝트로 부터 복사 붙여넣기 하지 않는다.
		if(prjCodeSettingsVO.getPrj_id().equals("0000000001")) {
			result.addObject("error","default project can't copy");
			return result;
		}
		
		// 코드 삭제해도 되는지 체크
		int chk = 0;
		List<PrjCodeSettingsVO> codeListInThisPrj = projectGenService.pg04_getCode(prjCodeSettingsVO);
		for(PrjCodeSettingsVO item : codeListInThisPrj) {
			if(projectGenService.pg04_delVal(item)<0) {
				chk = -1;
				break;
			}
		}
		
		if(chk == -1) {
			result.addObject("status","FAIL");
			result.addObject("error","사용중인 코드가 있어서 코드복사를 실행할 수 없습니다.");
			return result;
		}
		
		// 복사 전, 현재 테이블은 비운다.
		projectGenService.pg04_ClearTbl(prjCodeSettingsVO);
		// 제거 후, 데이터를 복사해 온다.
		projectGenService.pg04_copyTbl(prjCodeSettingsVO);
		result.addObject("status","OK");
		
		return result;
		
	}
	
	/**
	 * 1. 메소드명 : pg04ExcelDownload
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: parkjw
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
 */
	@RequestMapping(value = "pg04ExcelDownload.do")
	@ResponseBody
	public void pg04ExcelDownload(PrjCodeSettingsVO prjCodeSettingsVO, HttpServletResponse response, HttpServletRequest request) throws Exception {
		List<Object> result = new ArrayList<Object>();
		
		List<PrjCodeSettingsVO> codeList = projectGenService.pg04_getCode(prjCodeSettingsVO);
//		System.out.println(prjCodeSettingsVO.getFileName());
		String fileName = prjCodeSettingsVO.getFileName();
		ExcelFileDownload excelDown = new ExcelFileDownload();
		excelDown.pg04ExcelDownload(response, "titleList",  codeList, fileName , prjCodeSettingsVO.getPrj_nm());
	}
	
	/**
	 * 
	 * 1. 메소드명 : pg07_copyCode
	 * 2. 작성일: 2022. 1. 20.
	 * 3. 작성자: doil
	 * 4. 설명: 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "pg07_copyCode.do")
	@ResponseBody
	public Object pg07_copyCode(PrjDisciplineVO prjDisciplineVO) {
		
		
		
		List<Object> result = new ArrayList<Object>();

		// 디폴트 프로젝트 ID인 0000000001는 다른 프로젝트로 부터 복사 붙여넣기 하지 않는다.
		if (prjDisciplineVO.getPrj_id().equals("0000000001")) {
			result.add("기본 프로젝트 복사 불가");
			return result;
		}
		
		// 해당 discip을 사용중인 doc가 있는지 체크한다.
		List< List<String> > folders = prjDisciplineVO.getFolders();
		PrjDisciplineVO checker = new PrjDisciplineVO();
		checker.setPrj_id(prjDisciplineVO.getPrj_id());
		if (folders != null) {
			try {
				for (List<String> folderList : folders) {
					for (String id : folderList) {
						if(id.equals("")) continue;
						String concat = "%>" + id + ">%";

						checker.setDiscip_folder_id(concat);
						checker.setFolder_id(Long.parseLong(id));
						// 테이블 삭제하기 전, 폴더 하위에 문서있는지 체크
						if (projectGenService.pg07_delValidateChk(checker) > 0) { // 존재하는 경우
							throw new Exception(id + " : 해당 폴더 delete 실패 [폴더에 파일 존재]");
						}

					}
				}
			} catch (Exception e) {
				result.add("[삭제불가] : 해당 폴더에 사용중인 문서가 존재 (" + checker.getDiscip_folder_id() + ")");
				return result;
			}
		}
		
		// 복사 전, 현재 테이블은 비운다.
		projectGenService.pg07_ClearTbl(prjDisciplineVO);
		// 제거 후, 데이터를 복사해 온다.
		projectGenService.pg07_copyTbl(prjDisciplineVO);
		
	
		result.add("SUCCESS");
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg07_getDiscp 
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 권도일 
	 * 4. 설명: DISCIPLINE 코드를 DB에서 가져오는 메소드
	 * 5. 수정일:
	 */
	@RequestMapping(value = "pg07_getDiscp.do")
	@ResponseBody
	public Object pg07_getDiscp(PrjDisciplineVO prjDisciplineVO) {
		
		
		System.out.println(prjDisciplineVO.getPrj_id());
		
		List<Object> result = new ArrayList<Object>();
		
		List<PrjDisciplineVO> codeList = projectGenService.pg07_getDiscp(prjDisciplineVO);
		if(codeList.size() < 1) return result;
		
		
		String firstCode = codeList.get(0).getDiscip_code_id();
		List<String> group = new ArrayList<>();
		List<String> groupString = new ArrayList<>();
		List<Long> groupSeq = new ArrayList<>();
		
		
		// 같은 code_id 끼리 그룹핑
		PrjDisciplineVO tmp = codeList.get(0);
		List<PrjDisciplineVO> afterList = new ArrayList<>();
		
		for(PrjDisciplineVO row : codeList) {
			if(row.getDiscip_code_id().equals(firstCode)) {
				group.add(row.getDiscip_folder_id());
				groupString.add(row.getFolder_name());
				groupSeq.add(row.getDiscip_folder_seq());
				tmp = row;
			}
			else {
				tmp.setDiscip_folder_id_List(group);
				tmp.setFolder_name_List(groupString);
				tmp.setDiscip_folder_seq_List(groupSeq);
				afterList.add(tmp);
				tmp = row;
			
				group = new ArrayList<>();
				groupString = new ArrayList<>();
				groupSeq = new ArrayList<>();			
				group.add(tmp.getDiscip_folder_id());
				groupString.add(tmp.getFolder_name());
				groupSeq.add(tmp.getDiscip_folder_seq());
				
				firstCode = tmp.getDiscip_code_id();				
			}
		}
		// 마지막 code_id는 for문에서 처리되지 않았으므로 처리
		if(codeList.size()>=1) {
		PrjDisciplineVO last = codeList.get(codeList.size()-1);
		last.setDiscip_folder_id_List(group);
		last.setFolder_name_List(groupString);
		last.setDiscip_folder_seq_List(groupSeq);
		System.out.println(group.size());
		afterList.add(last);
		}
		
		// afterList folder_seq hash처리
		for(PrjDisciplineVO row : afterList) {
			HashMap<String,Long> folder_seqId_Map = new HashMap<>();
			for(int i=0 ; i < row.getDiscip_folder_seq_List().size() ; i++) {
				long seqVal = row.getDiscip_folder_seq_List().get(i);
				String idKey = row.getDiscip_folder_id_List().get(i);
				if(idKey != null)
					folder_seqId_Map.put(idKey,seqVal);
			}
			row.setFolder_seqId_Map(folder_seqId_Map);
		}
		
		result.add(afterList);
		//result.add(codeList);
		
		return result;
	}
	
	/**
	 * 1. 메소드명 : pg07_update 
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 권도일 
	 * 4. 설명: DISCIPLINE 코드를 DB에 update
	 * 5. 수정일:
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "pg07_update.do")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ResponseBody
	public String pg07_update(PrjDisciplineVO prjDisciplineVO) throws JsonParseException, JsonMappingException, IOException {
		String json = prjDisciplineVO.getJsonRowdata();
		if(json != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			PrjDisciplineVO tmpBean = objectMapper.readValue(json, PrjDisciplineVO.class);
			
			//update,insert 전 앞뒤 공백 제거
			tmpBean.setDiscip_code((tmpBean.getDiscip_code()).trim());
			tmpBean.setDiscip_display((tmpBean.getDiscip_display()).trim());
			tmpBean.setDiscip_desc((tmpBean.getDiscip_desc()).trim());
						
			// 업데이트 하기 전에, 해당 데이터의 존재여부(id로 확인)에 따라 update 혹은 insert 분기
			int cnt = projectGenService.pg07_chkCodeId(tmpBean);
			// 해당 code_id가 존재하면 update
			try {
				int result;
				if (cnt > 0) {
					// code 업데이트
					result = projectGenService.pg07_updateCode(tmpBean);
					// 폴더리스트 cascading
					// 기존의 path와 새로 입력받은 폴더 list 비교
					List<String> oldPath = projectGenService.pg07_getOldPath(tmpBean);
					List<String> newPath = tmpBean.getDiscip_folder_id_List();
					
					PrjDisciplineVO folderBean = new PrjDisciplineVO();
					folderBean.setPrj_id(tmpBean.getPrj_id());
					folderBean.setDiscip_code_id(tmpBean.getDiscip_code_id());
					
					
					// 새로 입력받은 id 처리
					// 이미 존재하면 두고, 새로 추가된 folder_id만 insert 처리
					for(String id : newPath) {
						folderBean.setDiscip_folder_id(id);
						if(!oldPath.contains(id)) {
							List<PrjDisciplineVO> test = projectGenService.pg07_insertValidateChk(folderBean);
							
							boolean isInOtherDiscip = false ;
							
							for(PrjDisciplineVO row : test) {
								// 다른 폴더 id의 경로를 '>'로 split 하고 포함하는지 확인
								List<String> chkId = Arrays.asList( row.getFolder_name().split(">") );
								if(chkId.contains(id)) {
									isInOtherDiscip = true;
									throw new Exception(folderBean.getDiscip_folder_id() + " : 해당 폴더 insert 실패 [다른 discipline 에서 사용 중]");
								}
							}
							System.out.println(isInOtherDiscip);
							// isIn이 계속 false이면 다른 discipline과 겹치지 않음 insert해도 된다.
							if(!isInOtherDiscip)
							if(projectGenService.pg07_insertFolderId(folderBean) < 1)
								throw new Exception(folderBean.getDiscip_folder_id() + " : 해당 폴더 insert 실패");
						}
							
					}
					
					// 삭제된 값 찾음
					for(String id : oldPath) {
						folderBean.setDiscip_folder_id(id);
						if(!newPath.contains(id)) {
							String concat = "%>"+ id +">%";
							System.out.println(concat);
							
							folderBean.setDiscip_folder_id(concat);
							folderBean.setFolder_id(Long.parseLong(id));
							// 삭제하기 전, 해당 폴더 하위에 문서있는지 체크
							if(projectGenService.pg07_delValidateChk(folderBean) < 1) { // 존재하지 않는 경우
								folderBean.setDiscip_folder_id(id);
								if(projectGenService.pg07_delFolderId(folderBean) < 1)	// 삭제 후, 반영 여부 표시.
									throw new Exception(folderBean.getDiscip_folder_id() + " : 해당 폴더 delete 실패");
							}
							
							
							
						}
							
					}
					
				}					
				// 해당 code_id가 존재하지 않으면 insert
				else {
					result = projectGenService.pg07_insertCode(tmpBean);
					
					List<String> newPath = tmpBean.getDiscip_folder_id_List();
					
					PrjDisciplineVO folderBean = new PrjDisciplineVO();
					folderBean.setPrj_id(tmpBean.getPrj_id());
					folderBean.setDiscip_code_id(tmpBean.getDiscip_code_id());
					if(newPath != null)
					for(String id : newPath) {
						folderBean.setDiscip_folder_id(id);
						if(projectGenService.pg07_insertFolderId(folderBean) < 1)
							throw new Exception(folderBean.getDiscip_folder_id() + " : 해당 폴더 insert 실패");
					}
				}
					
				
				if(result<1) throw new Exception(tmpBean.getDiscip_code() + " : 해당 row 업데이트 실패");

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return e.getMessage();				
			}
						
		}
		
		return "SUCCESS";

	}
	/**
	 * 1. 메소드명 : pg07_getDiscp 
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 권도일 
	 * 4. 설명: id가 auto increment 구조가 아니기 때문에, insert를 위한 id(최대값)를 구한다. 
	 * 5. 수정일:
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "pg07_getMaxId.do")
	@ResponseBody
	public String pg07_getMaxId(PrjDisciplineVO prjDisciplineVO) {
		
		Long getMax = projectGenService.pg07_getMaxId(prjDisciplineVO); 
		if(getMax == null) return "-1";
		
		if(getMax>0) return String.format("%05d", getMax);
		
		return "-1";
	}
	/**
	 * 1. 메소드명 : pg07_getFolderPath 
	 * 2. 작성일: 2021-12-18
	 * 3. 작성자: 권도일 
	 * 4. 설명: folder id로 폴더 경로를 찾아서 넘겨준다.
	 * 5. 수정일:
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "pg07_getFolderPath.do")
	@ResponseBody
	public List<String> pg07_getFolderPath(PrjDisciplineVO prjDisciplineVO) {
		
		List<String> result = new ArrayList<>();
		
		List<String> idList = prjDisciplineVO.getDiscip_folder_id_List();
		
		for(String id : idList) {
			prjDisciplineVO.setDiscip_folder_id(id);
			String path = projectGenService.pg07_getFolderPath(prjDisciplineVO).getFolder_name();
			result.add(path);
		}
		

		return result;
	}
	/**
	 * 1. 메소드명 : pg07_delDiscip 
	 * 2. 작성일: 2021-12-21
	 * 3. 작성자: 권도일 
	 * 4. 설명: DISCIPLINE 코드를 DB에서 삭제
	 * 5. 수정일:
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "pg07_delDiscip.do")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ResponseBody
	public String pg07_delDiscip(PrjDisciplineVO prjDisciplineVO) throws JsonParseException, JsonMappingException, IOException {
		String json = prjDisciplineVO.getJsonRowdata();
		String chkDel = "SUCCESS";
		if (json != null) {
			ObjectMapper objectMapper = new ObjectMapper();

			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			PrjDisciplineVO tmpBean = objectMapper.readValue(json, PrjDisciplineVO.class);

			// 폴더를 삭제 한다.
			// 만약 해당 폴더에 파일이 있으면 삭제불가
			// 그 후, discip 코드 삭제
			try {
				List<String> folderList = tmpBean.getDiscip_folder_id_List();
				for (String id : folderList) {
					String concat = "%>" + id + ">%";
					System.out.println(concat);

					tmpBean.setDiscip_folder_id(concat);
					tmpBean.setFolder_id(Long.parseLong(id));
					// 삭제하기 전, 해당 폴더 하위에 문서있는지 체크
					if (projectGenService.pg07_delValidateChk(tmpBean) < 1) { // 존재하지 않는 경우
						tmpBean.setDiscip_folder_id(id);
						if (projectGenService.pg07_delFolderId(tmpBean) < 1) // 삭제 성공여부
							throw new Exception(tmpBean.getDiscip_folder_id() + " : 해당 폴더 delete 실패");
					} else {
						chkDel = "FAIL";
						throw new Exception(tmpBean.getDiscip_folder_id() + " : 해당 폴더 delete 실패 [폴더에 파일 존재]");
					}
				}

				// 폴더 삭제 성공...
				// 해당 discip 코드 삭제
				if (projectGenService.pg07_delDiscip(tmpBean) < 1)
					throw new Exception(tmpBean.getDiscip_folder_id() + " : 해당 discip code delete 실패");

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return e.getMessage();
			}
		}
		
		return chkDel;
	}
	
	
	
	/**
	 * 1. 메소드명 : simpleTest 
	 * 2. 작성일: 2021-12-01
	 * 3. 작성자: 권도일 
	 * 4. 설명: ajax로 간단히 통신 테스트 하는 용도
	 * 5. 수정일:
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "simpleTest.do")
	@ResponseBody
	public List<Object> simpleTest(PrjDisciplineVO prjDisciplineVO) {
		
		List<Object> result = new ArrayList<>();

		List<PrjDisciplineVO> test = projectGenService.pg07_insertValidateChk(prjDisciplineVO);
		
		String inputFolderId = prjDisciplineVO.getDiscip_folder_id();
		boolean isIn = false ; 
		for(PrjDisciplineVO row : test) {
			// 다른 폴더 id의 경로를 '>'로 split 하고 포함하는지 확인
			List<String> chkId = Arrays.asList( row.getFolder_name().split(">") );
			if(chkId.contains(inputFolderId)) {
				isIn = true;
				break;
			}
		}
		// isIn이 계속 false이면 다른 discipline과 겹치지 않음 insert해도 된다.
		
		
		
		result.add(test);
		
		return result;
	}
	
	@RequestMapping(value = "pg08_searchUser.do")
	@ResponseBody
	public ModelAndView pg08_searchUser(String keyword) {

		ModelAndView result = new ModelAndView();
		
		List<UsersVO> search = projectGenService.searchUser(keyword);
		result.addObject("search", search);

		return result;
	}

	

}
