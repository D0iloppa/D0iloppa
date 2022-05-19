package kr.co.hhi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author doil
 *
 */

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.FolderAuthVO;
import kr.co.hhi.model.FolderInfoVO;
import kr.co.hhi.model.PrjFolderInfoVO;
import kr.co.hhi.model.PrjInfoVO;

@Service
public class TreeService extends AbstractService {
	public List<FolderInfoVO> getFolderList(String prj_id) {
		List<FolderInfoVO> getFolderList = selectList("folderinfoSqlMap.getFolderList_old",prj_id);
		return getFolderList;
	}
	public String getFolderPath(FolderInfoVO folderinfovo) {
		String getFolderPath = selectOne("folderinfoSqlMap.getFolderPath",folderinfovo);
		return getFolderPath;
	}
	public List<FolderInfoVO> getFolderListForPg01(FolderInfoVO folderinfovo) {
		List<FolderInfoVO> getFolderListForPg01 = selectList("folderinfoSqlMap.getFolderListForPg01",folderinfovo);
		return getFolderListForPg01;
	}
	public FolderInfoVO getLastFolderId(FolderInfoVO folderinfovo) {
		FolderInfoVO getLastFolderId = selectOne("folderinfoSqlMap.getLastFolderId", folderinfovo);
		return getLastFolderId;
	}
	public FolderInfoVO getParentFolderInfo(FolderInfoVO folderinfovo) {
		FolderInfoVO getParentFolderInfo = selectOne("folderinfoSqlMap.getParentFolderInfo", folderinfovo);
		return getParentFolderInfo;
	}
	public int insertFolderInfoVO(FolderInfoVO folderinfovo) {
		return insert("folderinfoSqlMap.insertFolderInfoVO", folderinfovo);
	}
	public void updateFolderNm(FolderInfoVO folderinfovo) {
		update("folderinfoSqlMap.updateFolderNm", folderinfovo);
	}
	
	public int deleteFolder(FolderInfoVO folderinfovo) {
		FolderInfoVO getFolderInfo = selectOne("folderinfoSqlMap.getFolderInfo", folderinfovo);
		int delCnt = 0;
		if(!getFolderInfo.getFolder_type().equals("TRA")) {
			List<FolderInfoVO> subFolderList = selectList("folderinfoSqlMap.getSubFolder",folderinfovo);
			for(FolderInfoVO subFolder: subFolderList) {
				try {
					if(delete("prjdocumentindexSqlMap.deleteFolderAuth", subFolder) < 1)
						throw new Exception("auth del fail");
					delCnt++;
				}
				catch(Exception e) {
					e.printStackTrace();
					return -1;
				}
			}
		}
		
		
		// 폴더 삭제
		int deleteFolder1 = delete("folderinfoSqlMap.deleteFolder", folderinfovo);
		if(deleteFolder1 < 1) return -1;
		
		
		
		// int deleteFolderAuth = delete("prjdocumentindexSqlMap.deleteFolderAuth", folderinfovo);
		//if(deleteFolderAuth < 1) return -1;
		
		return deleteFolder1 + delCnt;
	}
	public int getIsExistDownFolder(FolderInfoVO folderinfovo) {
		int getIsExistDownFolder = selectOne("folderinfoSqlMap.getIsExistDownFolder", folderinfovo);
		return getIsExistDownFolder;
	}
	public void updateFolderPath(FolderInfoVO folderinfovo) {
		update("folderinfoSqlMap.updateFolderPath", folderinfovo);
	}
	public void updateDownFolderPath(FolderInfoVO folderinfovo) {
		update("folderinfoSqlMap.updateDownFolderPath", folderinfovo);
	}
	public List<FolderInfoVO> getFolderDiscipline(FolderInfoVO folderinfovo) {
		List<FolderInfoVO> getFolderDiscipline = selectList("folderinfoSqlMap.getFolderDiscipline", folderinfovo);
		return getFolderDiscipline;
	}
	public FolderInfoVO getTrootFolderInfo(FolderInfoVO folderinfovo) {
		FolderInfoVO getTrootFolderInfo = selectOne("folderinfoSqlMap.getTrootFolderInfo", folderinfovo);
		return getTrootFolderInfo;
	}
	public FolderInfoVO getFolderInfoFromTRAFolderNm(FolderInfoVO folderinfovo) {
		FolderInfoVO getFolderInfoFromTRAFolderNm = selectOne("folderinfoSqlMap.getFolderInfoFromTRAFolderNm", folderinfovo);
		return getFolderInfoFromTRAFolderNm;
	}
	public long insertTRAFolderInfoVO(FolderInfoVO folderinfovo) {
		insert("folderinfoSqlMap.insertTRAFolderInfoVO", folderinfovo);
		long folder_id = folderinfovo.getFolder_id();
		return folder_id;
	}
	public int deleteTROutgoingFolder(PrjFolderInfoVO prjfolderinfovo) {
		int deleteTROutgoingFolder = delete("folderinfoSqlMap.deleteTROutgoingFolder", prjfolderinfovo);
		return deleteTROutgoingFolder;
	}
	public int insertTRAFolderAuth(FolderInfoVO folderinfovo) {
		return insert("folderinfoSqlMap.insertTRAFolderAuth", folderinfovo);
	}
	public FolderInfoVO getFolderInfo(FolderInfoVO folderinfovo) {
		FolderInfoVO getFolderInfo = selectOne("folderinfoSqlMap.getFolderInfo", folderinfovo);
		return getFolderInfo;
	}
	
	public FolderInfoVO getFolderAuth(FolderInfoVO folder) {
		
		// user_type을 구해서, 폴더권한과 관계없는 폴더들에 대하여 권한처리
		String user_type = selectOne("folderinfoSqlMap.getUserType", folder);
		// 현대 임직원들에 대하여 권한과 무관하게 읽기처리하는 폴더
		
		if(user_type != null && user_type.equals("1")) {
			String folder_type = selectOne("folderinfoSqlMap.isHHIUserCanReadFolder", folder);;
			// 'REP','COL','TRA' 폴더타입에 대하여 읽기권한 주기
			if(folder_type != null) {
				//folder_type.equals("REP") || folder_type.equals("COL") || folder_type.equals("TRA") 
				if(folder_type.equals("COL") || folder_type.equals("TRA")) {
					folder.setUser_r("Y");
					folder.setGrp_r("Y");
					folder.setOrg_r("Y");
					
					return folder;
				}
			}
		}
		
		
		
		
		// DB에 권한처리한 룰에 의해 적용
		String getFolder_path = selectOne("folderinfoSqlMap.getFolderPath",folder);
		String[] folder_path = getFolder_path.split(">");
		// 권한을 찾기위한 vo
		FolderAuthVO folderAuthVo = new FolderAuthVO();
		folderAuthVo.setPrj_id(folder.getPrj_id());
		
		// 개인 권한 매핑 여부
		boolean settedP = false;
		// 그룹 권한 매핑 여부
		boolean settedG = false;
		// 조직 권한 매핑 여부
		boolean settedO = false;
		
		// 겸직을 포함하여 org_path를 가져옴 (@로 split)
		String user_org_id = folder.getOrg_id();
		List<String> org_ids = new ArrayList<>(); 
		if(user_org_id != null) {
			org_ids = Arrays.asList(user_org_id.split("@"));
		}else {
			org_ids.add("-1");
		}
			
		
		
		List<String> folder_list = Arrays.asList(folder_path);
		// 폴더 depth가 가까운 곳에 우선순위를 주기 위함
		Collections.reverse(folder_list);
		
		// 해당 폴더의 부모들의  권한을 가져오는 리스트
		List<FolderAuthVO> getAuthList = new ArrayList<>();
		// 해당 폴더의 부모들에 대하여 계승하는 곳까지 순차로 올라가며 권한값 get
		for(String folder_id : folder_list) {
			//System.out.println(folder_list);
			Long chkFolder = Long.parseLong(folder_id);
			if(chkFolder == 0) break;
			folderAuthVo.setFolder_id(chkFolder);
			// 1) 폴더의 권한을 찾음 (auth_lvl1 = "F")
			folderAuthVo = selectOne("folderAuthSqlMap.getFolderAuth",folderAuthVo);

			// 해당 폴더에 추가된 권한 확인
			if(folderAuthVo == null) break;
			
			String add = folderAuthVo.getFolder_auth_add_yn();
			
			if(add != null) {
				if(add.equals("Y")) {
				// 현재 폴더에 추가되어있는 권한들을 가져옴
				List<FolderAuthVO> getAuthInThisFolder = selectList("folderAuthSqlMap.getAuthInThisFolder",folderAuthVo);
						
				for(FolderAuthVO auth : getAuthInThisFolder) getAuthList.add(auth);
				
				}
			}
			
			// 부모폴더 권한 계승여부
			// 권한을 계승하지 않으면 for문 종료
			String p = folderAuthVo.getParent_folder_inheri_yn();
			if(p == null) {
				break;
			}
			else if(p.equals("N")) {
				break;
			}
			
		}
		
		// 수집한 권한리스트를 기준으로 권한 매핑
		// 겸직의 경우까지 생각하여 해당 세션 아이디의 부서가 겸직설정되어있는 경우,
		// 부서별 권한을 매핑하고 or조건으로 처리
		
		String org_R = "";
		String org_W = "";
		String org_D = "";
		
		
		for(String o_id: org_ids) {
			
			settedO = false;
			
			for(FolderAuthVO auth : getAuthList) {
				String auth_lvl1 = auth.getAuth_lvl1();
				switch(auth_lvl1) {
				// P권한 매핑 (개인)
					case "P" :
						if(settedP) continue;
						String user_id = auth.getAuth_val();
						if(user_id.equals(folder.getUser_id())) {						
							if(folder.getUser_r() == null)
								folder.setUser_r(auth.getRead_auth_yn());
							if(folder.getUser_w() == null)
								folder.setUser_w(auth.getWrite_auth_yn());
							if(folder.getUser_d() == null)
								folder.setUser_d(auth.getDelete_auth_yn());
							settedP = true;
						}	
						break;
				// G권한 매핑 (그룹)
					case "G" :
						if(settedG) continue;
						String grp_id = auth.getAuth_val();
						String userGrp_id = folder.getGrp_id();
						if(grp_id.equals(folder.getGrp_id())) {
							if(folder.getGrp_r() == null)
								folder.setGrp_r(auth.getRead_auth_yn());
							if(folder.getGrp_w() == null)
								folder.setGrp_w(auth.getWrite_auth_yn());
							if(folder.getGrp_d() == null)
								folder.setGrp_d(auth.getDelete_auth_yn());
							settedG = true;
						}	
						break;
					// O권한 매핑 (부서)
					case "O":
						String org_id = auth.getAuth_val();
						if(o_id != null) {
							if(settedO) continue;
							String[] user_Orgs = o_id.split(">");
							List<String> org_id_list = Arrays.asList(user_Orgs);
							Collections.reverse(org_id_list);
							user_Orgs = org_id_list.toArray(user_Orgs);
							for(int i=0;i<user_Orgs.length;i++) {
								if(org_id.equals(user_Orgs[i])) {
									folder.setOrg_r(auth.getRead_auth_yn());
									folder.setOrg_w(auth.getWrite_auth_yn());
									folder.setOrg_d(auth.getDelete_auth_yn());
									settedO = true;
								}
							}
						}
							
						break;
				
				}
				
				// 부서 권한 계산 한 사이클 후, 매핑된 권한
				String mapped_org_r = folder.getOrg_r();
				if(mapped_org_r == null) mapped_org_r = "N";
				String mapped_org_w = folder.getOrg_w();
				if(mapped_org_w == null) mapped_org_w = "N";
				String mapped_org_d = folder.getOrg_d();
				if(mapped_org_d == null) mapped_org_d = "N";
				
				if(settedO) {
					org_R += mapped_org_r;
					org_W += mapped_org_w;
					org_D += mapped_org_d;
				}
				
				
			}
			
		}
		
		
		org_R = org_R.replaceAll("N", "");
		if(org_R.length()>0) org_R = org_R.substring(0,1);
		org_W = org_W.replaceAll("N", "");
		if(org_W.length()>0) org_W = org_W.substring(0,1);
		org_D = org_D.replaceAll("N", "");
		if(org_D.length()>0) org_D = org_D.substring(0,1);
		//if(org_D.length()>0) org_D = org_D.charAt(0) + "";
		
		
		// 공백으로 남으면 N으로 셋팅
		if(org_R.equals("")) org_R = "N";
		if(org_W.equals("")) org_W = "N";
		if(org_D.equals("")) org_D = "N";
		
		folder.setOrg_r(org_R);
		folder.setOrg_w(org_W);
		folder.setOrg_d(org_D);
		
		
		return folder;
	}
	
	/**
	 * 
	 * 1. 메소드명 : getFolderListWithAuth_2
	 * 2. 작성일: 2022. 3. 3.
	 * 3. 작성자: doil
	 * 4. 설명: 폴더권한 rule에 의한 권한 매핑 
	 * 5. 수정일: doil
	 * @param getFolderLists 
	 */
	public List<FolderInfoVO> getFolderListWithAuth_2(List<FolderInfoVO> folderList, FolderInfoVO sessionInfo) {
		
		List<FolderInfoVO> result = new ArrayList<>();
		
		// 권한을 찾기위한 vo
		FolderAuthVO folderAuthVo = new FolderAuthVO();
		folderAuthVo.setPrj_id(sessionInfo.getPrj_id());
		
		// 검색된 모든 폴더들에 대하여 수행
		int idx = 0;
		for(FolderInfoVO folder : folderList) {
			
			String[] folder_path = folder.getFolder_path().split(">");
			
			List<String> folder_list = Arrays.asList(folder_path);
			Collections.reverse(folder_list);
			if(folder_list.size() == 0) continue;
			// 해당 폴더의 부모들의  권한을 가져오는 리스트
			List<FolderAuthVO> getAuthList = new ArrayList<>();
			// 해당 폴더의 부모들에 대하여 계승하는 곳까지 순차로 올라가며 권한값 get
			for(String folder_id : folder_list) {
				System.out.println(folder_id);
				Long chkFolder = Long.parseLong(folder_id);
				if(chkFolder == 0) break;
				folderAuthVo.setFolder_id(chkFolder);
				// 1) 폴더의 권한을 찾음 (auth_lvl1 = "F")
				folderAuthVo = selectOne("folderAuthSqlMap.getFolderAuth",folderAuthVo);

				// 해당 폴더에 추가된 권한 확인
				if(folderAuthVo == null) break;
				
				String add = folderAuthVo.getFolder_auth_add_yn();
				if(add != null) {
					if(add.equals("Y")) {
						// 현재 폴더에 추가되어있는 권한들을 가져옴
						List<FolderAuthVO> getAuthInThisFolder = selectList("folderAuthSqlMap.getAuthInThisFolder",folderAuthVo);
						
						for(FolderAuthVO auth : getAuthInThisFolder)
							getAuthList.add(auth);
					}
				}
				
				// 부모폴더 권한 계승여부
				// 권한을 계승하지 않으면 for문 종료
				String p = folderAuthVo.getParent_folder_inheri_yn();
				if(p == null) {
					break;
				}
				else if(p.equals("N")) {
					break;
				}
				
			}
			
			// 수집한 권한리스트를 기준으로 권한 매핑
			for(FolderAuthVO auth : getAuthList) {
				String auth_lvl1 = auth.getAuth_lvl1();
				switch(auth_lvl1) {
				// P권한 매핑 (개인)
					case "P" :
						String user_id = auth.getAuth_val();
						if(user_id.equals(sessionInfo.getUser_id())) {
							folder.setUser_r(auth.getRead_auth_yn());
							folder.setUser_w(auth.getWrite_auth_yn());
							folder.setUser_d(auth.getDelete_auth_yn());
						}	
						break;
				// G권한 매핑 (그룹)
					case "G" :
						String grp_id = auth.getAuth_val();
						if(grp_id.equals(sessionInfo.getGrp_id())) {
							folder.setGrp_r(auth.getRead_auth_yn());
							folder.setGrp_w(auth.getWrite_auth_yn());
							folder.setGrp_d(auth.getDelete_auth_yn());
						}	
						break;
				// O권한 매핑 (조직)
					case "O":
						//long org_id = Long.parseLong(auth.getAuth_val()); 
						String org_id = auth.getAuth_val();
						if(org_id.equals(sessionInfo.getGrp_id())) {
							folder.setOrg_r(auth.getRead_auth_yn());
							folder.setOrg_w(auth.getWrite_auth_yn());
							folder.setOrg_d(auth.getDelete_auth_yn());
						}	
						break;				
				}
			}
			
			
			// folderList.set(, folder);
		}
		
		
		
		
		
		return result;
	}

	public List<FolderInfoVO> getFolderListWithAuth(FolderInfoVO bean) {
		return selectList("folderinfoSqlMap.getFolderListWithAuth",bean);
	}
	
	public FolderInfoVO getFolderWithAuth(FolderInfoVO bean) {
		return selectOne("folderinfoSqlMap.getFolderWithAuth",bean);
	}
	
	public List<String> getOrgPath(String user_id) {
		return selectList("folderinfoSqlMap.getOrgPath",user_id);
	}

	public List<String> getGrpPath(FolderInfoVO bean) {
		return selectList("folderinfoSqlMap.getGrpPath",bean);
	}
	
	public String isAdmin(String user_id) {
		return selectOne("folderinfoSqlMap.isAdmin",user_id);
	}
	
	public String isDcc(PrjInfoVO prjinfovo) {
		return selectOne("folderinfoSqlMap.isDcc",prjinfovo);
	}
	
	
    public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key) {
        return list.stream()
            .filter(deduplication(key))
            .collect(Collectors.toList());
    }

    public static <T> Predicate<T> deduplication(Function<? super T, ?> key) {
        final Set<Object> set = ConcurrentHashMap.newKeySet();
        return predicate -> set.add(key.apply(predicate));
    }

	
	
	public List<PrjInfoVO> getPrjGridList(String login_id) {
		
		PrjInfoVO getListVO = new PrjInfoVO();
		List<String> org_ids = selectList("prjinfoSqlMap.getOrgIds",login_id);
		String org_id = "";
		if(org_ids != null)
		for(String path :org_ids) org_id += (path + ">");
		
		getListVO.setUser_id(login_id);
		getListVO.setOrg_id(org_id);
		
		List<PrjInfoVO> prjListByAuth = selectList("prjinfoSqlMap.getPrjGridListByAuth",getListVO);
		
		// P권한에 대하여 최상위 폴더의 읽기 권한이 N으로 되어있는 프로젝트의 리스트
		List<String> p_prj_id;
		
		// P권한에 대하여 최상위 폴더의 읽기 권한이 N으로 되어있는 프로젝트의 리스트
		List<String> g_prj_id;
		
		List<PrjInfoVO> prjMemberList = null;
		// 프로젝트의 멤버는 권한과 관계없이 프로젝트 접근가능
		String user_type = selectOne("prjinfoSqlMap.getUserType",login_id);
		if(user_type == null) user_type = "3";
		
		if(user_type.equals("1"))
			prjMemberList = selectList("prjinfoSqlMap.getPrjGridList",login_id);
		else
			prjMemberList = selectList("prjinfoSqlMap.getPrjGridList_out",login_id);
		
		//prjListByAuth.addAll(prjMemberList);
		for(PrjInfoVO item :prjMemberList) {
			if(item != null) prjListByAuth.add(item);
		}
		

		// 중복제거
		prjListByAuth = this.deduplication(prjListByAuth, PrjInfoVO::getPrj_id);
		
		return prjListByAuth;

	}
	
	public List<PrjInfoVO> getPrjGridAdminList() {
		return selectList("prjinfoSqlMap.getPrjGridAdminList");
	}
	
	public List<PrjInfoVO> prjSearch(PrjInfoVO search) {
		return selectList("prjinfoSqlMap.prjSearch",search);
	}
	
	public List<String> isUsingDRN(FolderInfoVO item) {
		return selectList("prjinfoSqlMap.isUsingDRN",item);
	}
	
	/**
	 * 
	 * 1. 메소드명 : folderAuthCheck
	 * 2. 작성일: 2022. 3. 3.
	 * 3. 작성자: doil
	 * 4. 설명: 해당 폴더에 대한 유저의 권한을 계산 
	 * 5. 수정일: doil
	 */
	public FolderInfoVO folderAuthCheck(FolderInfoVO item, String isAdmin, String isDcc){

			
			// 기본적으로는 useDRN은 N으로 셋팅
			String useDRN = "N";
			List<String> isUsingDRN = isUsingDRN(item);
			if(isUsingDRN.size()>0)
				useDRN = isUsingDRN.get(0);
			
			if(useDRN==null) useDRN = "N";
			
			// 대문자로 캐스팅
			useDRN = useDRN.toUpperCase();
			
			
			item.setUseDRN(useDRN);

			

			// 매핑된 권한이 없을 때의 기본 권한
			String auth_r = "N";
			String auth_w = "N";
			String auth_d = "N";
			

			
			
			// 권한 체크
			// 모두 null 이면 실제 DB에 해당 값이 존재하지 않음
			// 1) 해당 폴더에 셋팅된 유저권한이 있는지 확인
			boolean isUserAuthEx = true;
			if(item.getUser_r() == null && item.getUser_w() == null && item.getUser_d() == null)
				isUserAuthEx = false;
			// 2) 해당 폴더에 셋팅된 그룹권한이 있는지 확인
			boolean isGrpAuthEx = true;
			if(item.getGrp_r() == null && item.getGrp_w() == null && item.getGrp_d() == null)
				isGrpAuthEx = false;
			// 3) 해당 폴더에 셋팅된 조직권한이 있는지 확인
			boolean isOrgAuthEx = true;
			if(item.getOrg_r() == null && item.getOrg_w() == null && item.getOrg_d() == null)
				isOrgAuthEx = false;

			
			
			
			// P->G->O 우선순위
			if(isUserAuthEx) {
				auth_r = item.getUser_r();
				auth_w = item.getUser_w();
				auth_d = item.getUser_d();
			}else if(isGrpAuthEx) {
				auth_r = item.getGrp_r();
				auth_w = item.getGrp_w();
				auth_d = item.getGrp_d();
			}else if(isOrgAuthEx) {
				auth_r = item.getOrg_r();
				auth_w = item.getOrg_w();
				auth_d = item.getOrg_d();
			}
			
			
			/*
 		    // 읽기권한
			if(item.getUser_r() != null) { auth_r = item.getUser_r(); }
			else if(item.getOrg_r() != null) { auth_r = item.getOrg_r(); }
			else if(item.getGrp_r() != null) { auth_r = item.getGrp_r(); }
			// 쓰기권한
			if(item.getUser_w() != null) { auth_w = item.getUser_w(); }
			else if(item.getOrg_w() != null) { auth_w = item.getOrg_w(); }
			else if(item.getGrp_w() != null) { auth_w = item.getGrp_w(); }
			// 삭제권한
			if(item.getUser_d() != null) { auth_d = item.getUser_d(); }
			else if(item.getOrg_d() != null) { auth_d = item.getOrg_d(); }
			else if(item.getGrp_d() != null) { auth_d = item.getGrp_d(); }
			*/ 
			
			// default 프로젝트의 경우 DCC 폴더에 읽기권한 부여
			String prj_id = item.getPrj_id();
			String folder_type = selectOne("folderinfoSqlMap.getFolderType",item);
		
			
			// null일 경우 거부와 동일
			if(auth_r == null) auth_r = "N";
			if(auth_w == null) auth_w = "N";
			if(auth_d == null) auth_d = "N";
			
			item.setAuth_r(auth_r);
			item.setAuth_w(auth_w);
			item.setAuth_d(auth_d);
			
			if(isDcc.equals("Y")) { // 로그인한 유저가 dcc인 경우
				if(!folder_type.equals("CON")) {
					item.setAuth_r("Y");
					item.setAuth_w("Y");
					item.setAuth_d("Y");
				}
			}
			
			if(isAdmin.equals("Y")) { // 로그인한 유저가 시스템 관리자인 경우
				item.setAuth_r("Y");
				item.setAuth_w("Y");
				item.setAuth_d("Y");
			}
			
			List<String> mustRead = Arrays.asList(new String[] {"TRA","COL"});
			
			
			if(mustRead.contains(folder_type)) {
				item.setAuth_r("Y");
				//item.setAuth_w("Y");
				//item.setAuth_d("Y");
			}
	
			
		
		
		return item;
	}
	

	public List<FolderInfoVO> folderAuthMapping(List<FolderInfoVO> getFolderLists, String isAdmin, String isDcc) {

		for(int i=0; i<getFolderLists.size(); i++) {
			
			FolderInfoVO item = getFolderLists.get(i);
			
			// 기본적으로는 useDRN은 N으로 셋팅
			String useDRN = "N";
			List<String> isUsingDRN = isUsingDRN(item);
			if(isUsingDRN.size()>0)
				useDRN = isUsingDRN.get(0);
			
			if(useDRN==null) useDRN = "N";
			
			// 대문자로 캐스팅
			useDRN = useDRN.toUpperCase();
			
			
			item.setUseDRN(useDRN);

			

			// 매핑된 권한이 없을 때의 기본 권한
			String auth_r = "N";
			String auth_w = "N";
			String auth_d = "N";
			

			
			
			// 권한 체크
			// 1) 해당 폴더에 셋팅된 유저권한이 있는지 확인
			// 2) 해당 폴더에 셋팅된 조직권한이 있는지 확인
			// 3) 해당 폴더에 셋팅된 그룹권한이 있는지 확인
 		    // 읽기권한
			if(item.getUser_r() != null) { auth_r = item.getUser_r(); }
			else if(item.getOrg_r() != null) { auth_r = item.getOrg_r(); }
			else if(item.getGrp_r() != null) { auth_r = item.getGrp_r(); }
			// 쓰기권한
			if(item.getUser_w() != null) { auth_w = item.getUser_w(); }
			else if(item.getOrg_w() != null) { auth_w = item.getOrg_w(); }
			else if(item.getGrp_w() != null) { auth_w = item.getGrp_w(); }
			// 삭제권한
			if(item.getUser_d() != null) { auth_d = item.getUser_d(); }
			else if(item.getOrg_d() != null) { auth_d = item.getOrg_d(); }
			else if(item.getGrp_d() != null) { auth_d = item.getGrp_d(); }
			 
			
			// default 프로젝트의 경우 DCC 폴더에 읽기권한 부여
			String prj_id = item.getPrj_id();
			String folder_type = item.getFolder_type();
			/*
			if(prj_id.equals("0000000001")) {
				if(folder_type.equals("DCC") || folder_type.equals("COL") || folder_type.equals("TRA")) auth_r = "Y";
				else auth_r = "N";
			}
			*/
			
			//  if(folder_type.equals("DCC") || folder_type.equals("COL") || folder_type.equals("TRA")) auth_r = "Y";
			
			
			item.setAuth_r(auth_r);
			item.setAuth_w(auth_w);
			item.setAuth_d(auth_d);
			
			if(isDcc.equals("Y")) { // 로그인한 유저가 dcc인 경우
				if(!folder_type.equals("CON")) {
					item.setAuth_r("Y");
					item.setAuth_w("Y");
					item.setAuth_d("Y");
				}
				/*
				else if(item.getFolder_nm().equals("SYSTEM CONFIGURATION")) { // "CON" 타입이면, SYS CON(루트)과 User Register 메뉴만 허용
					item.setAuth_r("Y");
					item.setAuth_w("Y");
					item.setAuth_d("Y");					
				}else if(item.getFolder_nm().equals("USER REGISTER")) { // "CON" 타입이면, SYS CON(루트)과 User Register 메뉴만 허용
					item.setAuth_r("Y");
					item.setAuth_w("Y");
					item.setAuth_d("Y");					
				}
				*/
			}
			
			if(isAdmin.equals("Y")) { // 로그인한 유저가 시스템 관리자인 경우
				item.setAuth_r("Y");
				item.setAuth_w("Y");
				item.setAuth_d("Y");
			}
	
			
			getFolderLists.set(i, item);
		}
		
		
		return getFolderLists;
	}

	
	
	public List<FolderInfoVO> getFolderVisible(FolderInfoVO infoFromSession, String isAdmin, String isDcc) {
		// TODO Auto-generated method stub
		String user_type = selectOne("folderinfoSqlMap.getUserType",infoFromSession);
		String prj_id = infoFromSession.getPrj_id();
		List<FolderInfoVO> folderList = null;
		
		switch(user_type) {
		case "1":
			if(isAdmin.equals("Y")) { // 어드민의 경우 모든 폴더 검색
				folderList = selectList("folderinfoSqlMap.getAdminFolderList",infoFromSession);
			}
			else if(isDcc.equals("Y")) {
				folderList = selectList("folderinfoSqlMap.getDccFolderList",infoFromSession);
			}
			else { // 일반 현대파워시스템 일원의 목록
				folderList = selectList("folderinfoSqlMap.getHHIUserFolderList",infoFromSession);
				String isPrjMember = isPrjMember(infoFromSession);
				if(!prj_id.equals("0000000001") && !isPrjMember.equals("Y")) {
					// 프로젝트 멤버가 아니라면, TRA타입의 폴더리스트는 제외 (디폴트 프로젝트 제외)
					folderList = folderList.stream()
											.filter(i-> !i.getFolder_type().equals("TRA"))
											.collect(Collectors.toList());
				}
			}
			
			
			break;
		case "2": // 협력업체, 고객사 목록보기 규칙이 달라짐
		case "3":
			String outUser_grp = infoFromSession.getGrp_id();
			if(outUser_grp != null) {
				infoFromSession.setGrp_id(outUser_grp);	
			}else infoFromSession.setGrp_id("-1");	
			
			
			String outUser_org = infoFromSession.getOrg_id();
			if(outUser_org != null) {
				String[] outUser_orgs = outUser_org.split("@");
				infoFromSession.setOrg_id(outUser_orgs[0]);	
			}else infoFromSession.setOrg_id("-1");	
			
			folderList = selectList("folderinfoSqlMap.getOutterUserFolderList",infoFromSession);
			List<FolderInfoVO> folderList2 = new ArrayList<>();
			
			for(FolderInfoVO folder : folderList) {
				String folder_path = folder.getFolder_path();
				String[] split_path = folder_path.split(">");
				for(String id : split_path) {
					long folder_id = Long.parseLong(id);
					folder.setFolder_id(folder_id);
					FolderInfoVO getFolder = selectOne("folderinfoSqlMap.getOutterUserFolderListInPath",folder);
					folderList2.add(getFolder);
				}
				// 해당 폴더의 하위목록(권한을 계승하는 폴더까지)
				/*
				String lastFolder = split_path[split_path.length-1];
				long lastFolder_id = Long.parseLong(lastFolder);
				*/
				
				// 해당 폴더의 하위목록(권한을 계승하는 폴더까지)				
				folder_path += ">%";
				folder.setFolder_path(folder_path);
				List<FolderInfoVO> getSubPath = selectList("folderinfoSqlMap.getOutterUserSubFolderListInPath",folder);
				for(FolderInfoVO subFolder : getSubPath) {
					String inheritYN = selectOne("folderinfoSqlMap.getOutterUserSubFolderListInPath_inheritYN",subFolder);
					if(inheritYN != null && inheritYN.equals("Y")) folderList2.add(subFolder);						
				}
			}
			
			//folderList2.stream().distinct().collect(Collectors.toList());
			// 새로 계산한 폴더리스트를 리턴
			folderList = new ArrayList<>();
			folderList = folderList2;
			
			
			break;
		}
		
		// 기본적으로는 useDRN은 N으로 셋팅
		// folder_id 1의 폴더명은 프로젝트 네임
		PrjInfoVO prjInfoVo = new PrjInfoVO();
		prjInfoVo.setPrj_id(infoFromSession.getPrj_id());
		
		prjInfoVo = selectOne("prjinfoSqlMap.getPrjInfo",prjInfoVo);
		/*
		if(prjInfoVo == null) {
			 String prj_nm = prjInfoVo.getPrj_nm();
			if(prj_nm == null)
			 	prj_nm = "";
		}
		*/
		
		
		
		/*int idx = 0;
		for(FolderInfoVO item :folderList) {
			long folder_id = item.getFolder_id();
			// 최상위  폴더 (folder_id가 1인 경우, 폴더명은 프로젝트 이름으로 셋팅
			if(folder_id == 1) 
				item.setFolder_nm(prj_nm);
			
			
			String useDRN = "N";
			List<String> isUsingDRN = isUsingDRN(item);
			if(isUsingDRN.size()>0)
				useDRN = isUsingDRN.get(0);
			
			if(useDRN==null) useDRN = "N";
			
			// 대문자로 캐스팅
			useDRN = useDRN.toUpperCase();
			
			
			item.setUseDRN(useDRN);
			
			folderList.set(idx++, item);
		}*/
		
		
		
		return folderList;
	}

	public String isPrjMember(FolderInfoVO infoFromSession) {
		// 해당 유저가 admin이라면 프로젝트에 들어가지 않더라도 프로젝트 멤버처럼 취급
		String isAdmin = selectOne("prjinfoSqlMap.isAdmin",infoFromSession.getUser_id());
		if(isAdmin != null && isAdmin.equals("Y")) return "Y";
		
		String isMember = "N";
		int cnt = selectOne("prjinfoSqlMap.isPrjMember",infoFromSession);
		if(cnt>0) isMember = "Y";
		
		return isMember;
	}

	public String getFolderProcess(FolderInfoVO infoFromSession) {
		String folder_path = selectOne("folderinfoSqlMap.getFolderPath",infoFromSession);
		infoFromSession.setFolder_path(folder_path);
		String processType = selectOne("folderinfoSqlMap.getFolderProcess",infoFromSession);
		if(processType == null) processType = "General";
		
		return processType;
	}
}
