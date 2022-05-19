package kr.co.doiloppa.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.SimpleDateFormat;

import kr.co.doiloppa.common.util.DeduplicationUtils;
import kr.co.doiloppa.model.CollaborationVO;
import kr.co.doiloppa.model.DrnPageVO;
import kr.co.doiloppa.model.FolderAuthVO;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.OrganizationVO;
import kr.co.doiloppa.model.UsersVO;
import kr.co.doiloppa.service.CollaborationService;
import kr.co.doiloppa.service.FolderAuthService;

@Controller
@RequestMapping("/*")
public class FolderAuthCont {
	
	protected FolderAuthService folderAuthService;
	
	@Autowired
	public void setCollaborationService(FolderAuthService folderAuthService) {
		this.folderAuthService = folderAuthService;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : auth_getFolderPath
	 * 2. 작성일: 2022. 1. 18.
	 * 3. 작성자: doil
	 * 4. 설명: folder_id로 폴더패스를 구한다. 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "auth_getFolderPath.do")
	@ResponseBody
	public ModelAndView auth_getFolderPath(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		String getFolderPath = folderAuthService.auth_getFolderPath(folderAuthVo);
		
		result.addObject("folderPath",getFolderPath);


		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : auth_getTrees
	 * 2. 작성일: 2022. 1. 18.
	 * 3. 작성자: doil
	 * 4. 설명: 트리에 들어갈 정보들을 가져옴 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "auth_getTrees.do")
	@ResponseBody
	public ModelAndView auth_getTrees(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		folderAuthVo.setReg_id(sessioninfo.getUser_id());
		
		List<FolderAuthVO> getOrgTreeMember = folderAuthService.auth_getOrg(folderAuthVo);
		// 그룹만 뽑아옴
		List<FolderAuthVO> getGrTree = folderAuthService.auth_getGrList(folderAuthVo);
		// 그룹에 매핑해줄 사람들 가져옴
		List<FolderAuthVO> getGrMembers = folderAuthService.auth_getGrMember(folderAuthVo);
		
		result.addObject("orgTree",getOrgTreeMember);
		result.addObject("group",getGrTree);
		result.addObject("grTree",getGrMembers);


		return result;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getAuthInfoList
	 * 2. 작성일: 2022. 1. 18.
	 * 3. 작성자: doil
	 * 4. 설명: 해당 폴더의 권한정보를 가져옴 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getAuthInfoList.do")
	@ResponseBody
	public ModelAndView getAuthInfoList(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		
		List<FolderAuthVO> getAuthList = folderAuthService.getAuthInfoList(folderAuthVo);
		if(getAuthList!=null) {
			int idx = 0;
			for(FolderAuthVO auth: getAuthList) {
				auth.setPrj_id(folderAuthVo.getPrj_id());
				// auth_lvl1에 따른 매핑값 찾음
				FolderAuthVO getMappedValue = folderAuthService.getAuthMappingVal(auth);
				getAuthList.set(idx++, getMappedValue);
			}
		}
		
		result.addObject("auth_list", getAuthList);
//		


		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getAuthInfo
	 * 2. 작성일: 2022. 1. 18.
	 * 3. 작성자: doil
	 * 4. 설명: 해당 row의 폴더권한
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getAuthInfo.do")
	@ResponseBody
	public ModelAndView getAuthInfo(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		
		FolderAuthVO getInfo = folderAuthService.getAuthInfo(folderAuthVo);
		
		
		result.addObject("info", getInfo);
		


		return result;
	}
	

	/**
	 * 
	 * 1. 메소드명 : getDefaultAuth
	 * 2. 작성일: 2022. 1. 26.
	 * 3. 작성자: doil
	 * 4. 설명: 해당 세션의 권한이 아닌, 해당 폴더에 대한 Default 권한을 가져온다. 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getDefaultAuth.do")
	@ResponseBody
	public ModelAndView getDefaultAuth(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		// 따로 할당된 권한이 있는지 체크 후, 없으면 디폴트 권한으로 return		
		List<FolderAuthVO> getInfo = folderAuthService.getDefaultAuth(folderAuthVo);

		
		result.addObject("auth", getInfo);
		
		FolderAuthVO defaultAuth = new FolderAuthVO();
		defaultAuth.setRead_auth_yn("Y");
		defaultAuth.setWrite_auth_yn("N");
		defaultAuth.setDelete_auth_yn("N");
		
		result.addObject("defaultAuth",defaultAuth);
		


		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getOrgMemberListInAuth
	 * 2. 작성일: 2022. 2. 27.
	 * 3. 작성자: doil
	 * 4. 설명: 선택한 조직에 해당되는 멤버들 리스트 출력 
	 * 5. 수정일: doil
	 */
	
	@RequestMapping(value = "getOrgMemberListInAuth.do")
	@ResponseBody
	public ModelAndView getOrgMemberListInAuth(FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		long org_id = folderAuthVo.getOrg_id();	
		OrganizationVO inputVO = new OrganizationVO();
		inputVO.setOrg_id(org_id);
		
		List<UsersVO> outputList = folderAuthService.selectList("organizationuserSqlMap.getOrganizationUserList",inputVO);
		
		
		result.addObject("list",outputList);

		return result;
	}
	
	
	/**
	 * 1. 메소드명 : getGrpMemberListInAuth
	 * 2. 작성일: 2022. 2. 27.
	 * 3. 작성자: doil
	 * 4. 설명: 선택한 그룹 내의 멤버들의 리스트를 구함 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getGrpMemberListInAuth.do")
	@ResponseBody
	public ModelAndView getGrpMemberListInAuth(FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		List<UsersVO> outputList;
		
		String grp_id = folderAuthVo.getGroup_id();
		if(grp_id.equals("prj")) 
			outputList = folderAuthService.selectList("folderAuthSqlMap.getGrpProjectMemberList",folderAuthVo);
		
		else
			outputList = folderAuthService.selectList("folderAuthSqlMap.getGrpMemberList",folderAuthVo);
		
		
		result.addObject("list",outputList);

		return result;
	}
	
	
	@RequestMapping(value = "searchMemberAtAuth.do")
	@ResponseBody
	public ModelAndView searchMemberAtAuth(FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		List<FolderAuthVO> searchMember = folderAuthService.memberSearch(folderAuthVo);
		
		result.addObject("search",searchMember);

		return result;
	}
	
	
	/**
	 * 
	 * 1. 메소드명 : getFolderAuthInfo
	 * 2. 작성일: 2022. 3. 3.
	 * 3. 작성자: doil
	 * 4. 설명: 폴더 자체의 권한 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "getFolderAuthInfo.do")
	@ResponseBody
	public ModelAndView getFolderAuthInfo(FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
		
		folderAuthVo.setAuth_lvl1("F");
		
		FolderAuthVO getFolderAuth = folderAuthService.getFolderAuth(folderAuthVo);
		
		
		result.addObject("folder_auth",getFolderAuth);

		return result;
	}
	
	@RequestMapping(value = "updateAuthInfo_allData.do")
	@ResponseBody
	public ModelAndView updateAuthInfo_allData(HttpServletRequest request, FolderAuthVO folderAuthVo) throws JsonParseException, JsonMappingException, IOException {
		
		ModelAndView result = new ModelAndView();
		
		// 로그인 세션 정보		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		folderAuthVo.setReg_id(sessioninfo.getUser_id());
		folderAuthVo.setMod_id(sessioninfo.getUser_id());
		folderAuthVo.setUser_id(sessioninfo.getUser_id());
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		folderAuthVo.setReg_date(reg_date);
		folderAuthVo.setMod_date(reg_date);
		
		
		
		String prj_id = folderAuthVo.getPrj_id();
		long folder_id = folderAuthVo.getFolder_id();
		String parent_folder_inheri_yn = folderAuthVo.getParent_folder_inheri_yn();
		String folder_auth_add_yn = folderAuthVo.getFolder_auth_add_yn();
		String[] jsonRowDatas = folderAuthVo.getJsonRowDatas();
		
		
		// 폴더의 권한 insert or update ( parent,add여부 )
		folderAuthVo.setAuth_lvl1("F");
		folderAuthVo.setAuth_val(folder_id+"");
		
		FolderAuthVO getFolderAuth = folderAuthService.getFolderAuth(folderAuthVo);
		
		// 폴더에 대한 권한 처리
		try {
			if (getFolderAuth == null) { // 해당 권한 정보가 없는 경우 insert
				if (folderAuthService.insertFolderAuth(folderAuthVo) < 1) {
					throw new Exception("insert 실패");
				}

				result.addObject("status", "insert");
			} else { // 해당 권한 정보가 있는 경우 update
				if (folderAuthService.updateFolderAuth(folderAuthVo) < 1) {
					throw new Exception("update 실패");
				}
				result.addObject("status", "update");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.addObject("error", e.getMessage());
			result.addObject("status", "FAIL");
		}

		
		// 권한 추가를 하기 전, 해당폴더의 권한 클리어
		folderAuthService.clearAuth(folderAuthVo);
		
		// 추가된 폴더권한 데이터 로딩
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		
		boolean prjMemberChk = false;
		for(String json : jsonRowDatas) {
			// 값을 읽어옴
			FolderAuthVO jsonFolderAuth = objectMapper.readValue(json, FolderAuthVO.class);
			// 그외의 저장해야할 값들 저장
			jsonFolderAuth.setPrj_id(prj_id);
			jsonFolderAuth.setFolder_id(folder_id);
			jsonFolderAuth.setReg_date(reg_date);
			jsonFolderAuth.setMod_date(reg_date);
			
			// 기존 데이터 클리어 됬으므로 insert
			try {
				if (folderAuthService.insertInfo(jsonFolderAuth) < 1) {
					throw new Exception("insert 실패");
				}
				
				
				
				result.addObject("status", "insert");
				
				String auth_lvl1 = jsonFolderAuth.getAuth_lvl1();
				if(!prjMemberChk) {
					
					if(auth_lvl1.equals("P")) {
						String isPrjMember = "N";
						int cnt = folderAuthService.selectOne("folderAuthSqlMap.isPrjMember", jsonFolderAuth);
						if (cnt > 0) isPrjMember = "Y";

						
						FolderInfoVO getProcessTypeVO = new FolderInfoVO();
						getProcessTypeVO.setFolder_id(folder_id);
						getProcessTypeVO.setPrj_id(jsonFolderAuth.getPrj_id());
						getProcessTypeVO.setUser_id(jsonFolderAuth.getAuth_val());

						String folder_path = folderAuthService.selectOne("folderinfoSqlMap.getFolderPath", getProcessTypeVO);
						getProcessTypeVO.setFolder_path(folder_path);

						String processType = folderAuthService.selectOne("folderinfoSqlMap.getFolderProcess", getProcessTypeVO);
						String folder_type = folderAuthService.selectOne("folderinfoSqlMap.getFolderType",getProcessTypeVO);
						if (processType == null) processType = "General";
						
						/*
						 * 프로세스 타입이 general인 경우에만 쓰기/삭제 권한 부여가 가능 그 외에는 부여 불가,
						 * 여기에서의 processType은 자바스크립트에서 
						 * 해당값이 General이 아니면 위의 로직이 작동했으므로 
						 * 쓰기/삭제 권한 부여 불가하다는 메세지를 출력해야 하기 때문에 
						 * processType을 General이 아닌 임의의 값 PBO라고 해줌(큰 의미X)
						 */
						 
						if(folder_type.equals("PBO")) processType = "PBO";
						
						result.addObject("isPrjMember", isPrjMember);
						result.addObject("processType", processType);
						
						prjMemberChk = true;
					}
				}
				
			}
			catch (Exception e){
				e.printStackTrace();
			}


		}
		



		return result;
	}
	
	/**
	 * 
	 * 1. 메소드명 : updateAuthInfo
	 * 2. 작성일: 2022. 1. 18.
	 * 3. 작성자: doil
	 * 4. 설명: 권한정보 insert 혹은 update 
	 * 5. 수정일: doil
	 */
	@RequestMapping(value = "updateAuthInfo.do")
	@ResponseBody
	public ModelAndView updateAuthInfo(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		
		ModelAndView result = new ModelAndView();
		
		// 로그인 세션 정보		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		folderAuthVo.setReg_id(sessioninfo.getUser_id());
		folderAuthVo.setMod_id(sessioninfo.getUser_id());
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		folderAuthVo.setReg_date(reg_date);
		folderAuthVo.setMod_date(reg_date);
		
		
		
		
		// 폴더에 대한 권한 조회
		FolderAuthVO getFolderAuth = folderAuthService.getFolderAuth(folderAuthVo);
		
		// 폴더에 대한 권한 처리
		try {
			if (getFolderAuth == null) { // 해당 권한 정보가 없는 경우 insert
				if (folderAuthService.insertFolderAuth(folderAuthVo) < 1) {
					throw new Exception("insert 실패");
				}

				result.addObject("status", "insert");
			} else { // 해당 권한 정보가 있는 경우 update
				if (folderAuthService.updateFolderAuth(folderAuthVo) < 1) {
					throw new Exception("update 실패");
				}
				result.addObject("status", "update");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.addObject("error", e.getMessage());
			result.addObject("status", "FAIL");
		}
		
		// 권한 적용 선택여부 확인 (이 폴더 권한을 추가
		if(!folderAuthVo.getAuth_lvl1().equals("")) {
			// 기존 DB 값 조회 (insert or update 결정)
			
			FolderAuthVO getInfo = folderAuthService.getAuthInfo(folderAuthVo);
			try {
				if (getInfo == null) { // 해당 권한 정보가 없는 경우 insert
					if (folderAuthService.insertInfo(folderAuthVo) < 1) {
						throw new Exception("insert 실패");
					}

					result.addObject("status", "insert");
				} else { // 해당 권한 정보가 있는 경우 update
					if (folderAuthService.updateInfo(folderAuthVo) < 1) {
						throw new Exception("update 실패");
					}
					result.addObject("status", "update");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				result.addObject("error", e.getMessage());
				result.addObject("status", "FAIL");
			}
			
		}
		
		
		
				
		
		/*
		FolderAuthVO getInfo = folderAuthService.getAuthInfo(folderAuthVo);
		
		
		
		try {
			if (getInfo == null) { // 해당 권한 정보가 없는 경우 insert
				if(folderAuthService.insertInfo(folderAuthVo) < 1) {
					throw new Exception("insert 실패");
				}					
				
				result.addObject("status", "insert");
			} else { // 해당 권한 정보가 있는 경우 update
				if(folderAuthService.updateInfo(folderAuthVo) < 1) {
					throw new Exception("update 실패");
				}
				result.addObject("status", "update");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result.addObject("error",e.getMessage());
			result.addObject("status","FAIL");
			
		
		// 해당 폴더의 하위 폴더들을 가져옴
		List<Long> getSubFolderList = folderAuthService.getSubFolderList(folderAuthVo);
		*/
		
		// cascade
		/*
		for(Long folder_id : getSubFolderList) {
			folderAuthVo.setFolder_id(folder_id);
			FolderAuthVO getInfo = folderAuthService.getAuthInfo(folderAuthVo);
			
			
			
			try {
				if (getInfo == null) { // 해당 권한 정보가 없는 경우 insert
					if(folderAuthService.insertInfo(folderAuthVo) < 1) {
						throw new Exception("insert 실패");
					}					
					
					result.addObject("status", "insert");
				} else { // 해당 권한 정보가 있는 경우 update
					if(folderAuthService.updateInfo(folderAuthVo) < 1) {
						throw new Exception("update 실패");
					}
					result.addObject("status", "update");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				result.addObject("error",e.getMessage());
				result.addObject("status","FAIL");
			}
			
			
			// 권한적용 최상위 폴더의 inherit여부는 체크값을 따름
			// 하위 폴더의 inherit요소는 Y로 셋
			folderAuthVo.setParent_folder_inheri_yn("Y");
		}
		*/
		


		return result;
	}
	
	@RequestMapping(value = "deleteAuthInfo.do")
	@ResponseBody
	public ModelAndView deleteAuthInfo(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		ModelAndView result = new ModelAndView();
	
		
		result.addObject("status", "SUCCESS");
		
		FolderAuthVO delTarget = folderAuthService.getAuthInfo(folderAuthVo);
		if(delTarget == null) {
			// DB에 존재하지 않는 값을 삭제하는 경우
			return result;
		}
		// 해당 폴더의 하위 폴더들을 가져옴
		//List<Long> getSubFolderList = folderAuthService.getSubFolderList(folderAuthVo);
		
		try {
			if (folderAuthService.deleteInfo(folderAuthVo,0) < 1) {
				throw new Exception("삭제 실패");
			}

		} catch (Exception e) {
			result.addObject("status", "FAIL");
			result.addObject("error",e.getMessage());
		}
		
		/*
		 * 
		// cascade
		int first = 0;
		for(Long folder_id : getSubFolderList) {
			folderAuthVo.setFolder_id(folder_id);
			
			FolderAuthVO getInfo = folderAuthService.getAuthInfo(folderAuthVo);
			
			if (getInfo != null) { // 프론트단에만 존재하는 데이터가 아니라 실제 DB상에 존재하는 데이터일 경우
				try {
					if (folderAuthService.deleteInfo(folderAuthVo,first++) < 1) {
						throw new Exception("삭제 실패");
					}

				} catch (Exception e) {
					result.addObject("status", "FAIL");
					result.addObject("error",e.getMessage());
				}
			} 
			
			
		}
		*/
		
		
		
		
		
		
		
		
		
		

		


		return result;
	}
	
	
	@RequestMapping(value = "defaultAuthSetAllProject.do")
	@ResponseBody
	public ModelAndView defaultAuthSetAllProject(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		
		ModelAndView result = new ModelAndView();
		result.addObject("status","SUCCESS");
		
		// 로그인 세션 정보		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		folderAuthVo.setReg_id(sessioninfo.getUser_id());
		folderAuthVo.setMod_id(sessioninfo.getUser_id());
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		folderAuthVo.setReg_date(reg_date);
		folderAuthVo.setMod_date(reg_date);
		
		List<String> prj_list = folderAuthService.selectList("folderAuthSqlMap.getPrjIdList");

		for(String prj_id : prj_list) {
			folderAuthVo.setPrj_id(prj_id);
			
			// 해당 프로젝트의 폴더 리스트를 구함 (각 폴더의 디폴트 권한을 부여하기 위함)
			List<Long> folder_id_List = folderAuthService.selectList("folderAuthSqlMap.getFolderIdList",folderAuthVo.getPrj_id());
			// 현대파워시스템의 조직도 root 아이디를 구함
			Long getHHI_orgRootId = folderAuthService.selectOne("folderAuthSqlMap.getHHI_orgRootId");
			
			// 디폴트 권한을 셋팅
			folderAuthVo.setAuth_lvl1("F");
			folderAuthVo.setParent_folder_inheri_yn("Y");
			
			
			// 각 폴더들에 부모폴더의 권한을 계승 체크
			for(Long folder_id : folder_id_List) {
				folderAuthVo.setFolder_id(folder_id);
				
				// 기존 DB 값
				FolderAuthVO isExistAuth = folderAuthService.getFolderAuth(folderAuthVo);
				
				try {
					if (isExistAuth == null) { // 해당 권한 정보가 없는 경우 insert
						if(folderAuthService.insertFolderAuth(folderAuthVo) < 1) 
							throw new Exception("insert 실패");
						System.out.println("[folder_id : " + folder_id + " ] Auth insert");
					} else { // 해당 권한 정보가 있는 경우 update
						if(folderAuthService.updateFolderAuth(folderAuthVo) < 1) 
							throw new Exception("update 실패");
						System.out.println("[folder_id : " + folder_id + " ] Auth update");
					}
				} catch (Exception e) {
					result.addObject("status","FAIL");
					e.printStackTrace();
				}
				
				
			}

			// 최상위 폴더 id에 현대중공업 추가
			folderAuthVo.setAuth_lvl1("O");
			folderAuthVo.setParent_folder_inheri_yn(null);
			folderAuthVo.setAuth_val(getHHI_orgRootId+"");
			
			folderAuthVo.setRead_auth_yn("Y");
			folderAuthVo.setWrite_auth_yn("N");
			folderAuthVo.setDelete_auth_yn("N");
			
			List<FolderInfoVO> root_folder_id_List = folderAuthService.selectList("folderAuthSqlMap.getRootFolderIdList",folderAuthVo.getPrj_id());
			
			List<String> availFolderTypeList = Arrays.asList((new String[] {"DCC","COL","TRA","PBO","REP"}));
			
			for(FolderInfoVO rootFolder : root_folder_id_List) {
				
				String folder_type = rootFolder.getFolder_type();
				// 폴더타입이 허용되는 리스트에 포함되지 않음
				if(!availFolderTypeList.contains(folder_type)) {
					continue;
				}
				
				long id = rootFolder.getFolder_id();
				folderAuthVo.setFolder_id(id);
				
				FolderAuthVO isExistAuth = folderAuthService.getAuthInfo(folderAuthVo);
				
				if(isExistAuth == null) {
					System.out.printf("[%d : inserting]\n",id);
					try {
						if(folderAuthService.insert("folderAuthSqlMap.insertInfo",folderAuthVo) < 1)
							throw new Exception("[" + id + "]: insert fail");
					}
					catch (Exception e){
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				} else {
					System.out.printf("[%d : updating]\n" , id);
					try {
						if(folderAuthService.update("folderAuthSqlMap.updateInfo",folderAuthVo) < 1)
							throw new Exception("[" + id + "]: update fail");
					}
					catch (Exception e){
						System.out.println(e.getMessage());
						e.printStackTrace();
						result.addObject("status","FAIL");
					}
				}
				
				// 권한이 추가되었으므로 해당 폴더의 권한추가 체크 여부 업데이트
				FolderAuthVO folderUpdate = folderAuthService.getFolderAuth(folderAuthVo);
				folderUpdate.setFolder_auth_add_yn("Y");
				folderAuthService.updateFolderAuth(folderUpdate);
			}
		}
		
		


		return result;
	}
	
	
	
	@RequestMapping(value = "defaultAuthSet.do")
	@ResponseBody
	public ModelAndView defaultAuthSet(HttpServletRequest request, FolderAuthVO folderAuthVo) {
		
		ModelAndView result = new ModelAndView();
		result.addObject("status","SUCCESS");
		
		// 로그인 세션 정보		
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		folderAuthVo.setReg_id(sessioninfo.getUser_id());
		folderAuthVo.setMod_id(sessioninfo.getUser_id());
		
		// 현재시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Calendar c1 = Calendar.getInstance();
		String reg_date = sdf.format(c1.getTime());
		folderAuthVo.setReg_date(reg_date);
		folderAuthVo.setMod_date(reg_date);
		
		
		// 해당 프로젝트의 폴더 리스트를 구함 (각 폴더의 디폴트 권한을 부여하기 위함)
		List<Long> folder_id_List = folderAuthService.selectList("folderAuthSqlMap.getFolderIdList",folderAuthVo.getPrj_id());
		// 현대파워시스템의 조직도 root 아이디를 구함
		Long getHHI_orgRootId = folderAuthService.selectOne("folderAuthSqlMap.getHHI_orgRootId");
		
		// 디폴트 권한을 셋팅
		folderAuthVo.setAuth_lvl1("F");
		
		folderAuthVo.setParent_folder_inheri_yn("Y");
		

		
		
		
		// 각 폴더들에 부모폴더의 권한을 계승 체크
		for(Long folder_id : folder_id_List) {
			folderAuthVo.setFolder_id(folder_id);
			
			// 기존 DB 값
			FolderAuthVO isExistAuth = folderAuthService.getFolderAuth(folderAuthVo);
			
			try {
				if (isExistAuth == null) { // 해당 권한 정보가 없는 경우 insert
					if(folderAuthService.insertFolderAuth(folderAuthVo) < 1) 
						throw new Exception("insert 실패");
					System.out.println("[folder_id : " + folder_id + " ] Auth insert");
				} else { // 해당 권한 정보가 있는 경우 update
					if(folderAuthService.updateFolderAuth(folderAuthVo) < 1) 
						throw new Exception("update 실패");
					System.out.println("[folder_id : " + folder_id + " ] Auth update");
				}
			} catch (Exception e) {
				result.addObject("status","FAIL");
				e.printStackTrace();
			}
			
			
		}

		// 최상위 폴더 id에 현대중공업 추가
		folderAuthVo.setAuth_lvl1("O");
		folderAuthVo.setParent_folder_inheri_yn(null);
		folderAuthVo.setAuth_val(getHHI_orgRootId+"");
		
		folderAuthVo.setRead_auth_yn("Y");
		folderAuthVo.setWrite_auth_yn("N");
		folderAuthVo.setDelete_auth_yn("N");
		
		List<FolderInfoVO> root_folder_id_List = folderAuthService.selectList("folderAuthSqlMap.getRootFolderIdList",folderAuthVo.getPrj_id());
		
		List<String> availFolderTypeList = Arrays.asList((new String[] {"DCC","COL","TRA","PBO","REP"}));
		
		for(FolderInfoVO rootFolder : root_folder_id_List) {
			
			String folder_type = rootFolder.getFolder_type();
			// 폴더타입이 허용되는 리스트에 포함되지 않음
			if(!availFolderTypeList.contains(folder_type)) {
				continue;
			}
			
			long id = rootFolder.getFolder_id();
			folderAuthVo.setFolder_id(id);
			
			FolderAuthVO isExistAuth = folderAuthService.getAuthInfo(folderAuthVo);
			
			if(isExistAuth == null) {
				System.out.printf("[%d : inserting]\n",id);
				try {
					if(folderAuthService.insert("folderAuthSqlMap.insertInfo",folderAuthVo) < 1)
						throw new Exception("[" + id + "]: insert fail");
				}
				catch (Exception e){
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			} else {
				System.out.printf("[%d : updating]\n" , id);
				try {
					if(folderAuthService.update("folderAuthSqlMap.updateInfo",folderAuthVo) < 1)
						throw new Exception("[" + id + "]: update fail");
				}
				catch (Exception e){
					System.out.println(e.getMessage());
					e.printStackTrace();
					result.addObject("status","FAIL");
				}
			}
			
			// 권한이 추가되었으므로 해당 폴더의 권한추가 체크 여부 업데이트
			FolderAuthVO folderUpdate = folderAuthService.getFolderAuth(folderAuthVo);
			folderUpdate.setFolder_auth_add_yn("Y");
			folderAuthService.updateFolderAuth(folderUpdate);
			
		}
		

		return result;
	}
	
	
}
