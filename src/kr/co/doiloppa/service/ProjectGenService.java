package kr.co.doiloppa.service;

/**
 * @author doil
 *
 */

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.ProcessInfoVO;
import kr.co.doiloppa.model.UsersVO;


@Service
public class ProjectGenService extends AbstractService {
	
	public List<ProcessInfoVO> getSystemClass(ProcessInfoVO processInfoVO){
		List<ProcessInfoVO> result = selectList("projectGenSqlMap.getSystemClass",processInfoVO);
		return result;		
	}
	
	public List<ProcessInfoVO> getManualClass(ProcessInfoVO processInfoVO){
		List<ProcessInfoVO> result = selectList("projectGenSqlMap.getManualClass",processInfoVO);
		return result;		
	}
	
	
	
	public ProcessInfoVO pg02_getDelValidation(ProcessInfoVO processInfoVO){
		ProcessInfoVO result = selectOne("projectGenSqlMap.pg02_delValidationChk",processInfoVO);		
		return result;		
	}
	
	public String getFolderPathNm(ProcessInfoVO processInfoVO){
		String result = selectOne("projectGenSqlMap.getFolderPath",processInfoVO);
		return result;		
	}
	
	
	
	public String insertManualClass(ProcessInfoVO processInfoVO) {
		String outMsg = "OK";
		
		String prj_id = processInfoVO.getPrj_id();
		long folder_id = Long.parseLong(processInfoVO.getProcess_folder_path_id());
		processInfoVO.setFolder_id(folder_id);
		String getFolderPath = selectOne("projectGenSqlMap.getFolderPath_pg02",processInfoVO);
		processInfoVO.setFolder_path(getFolderPath+"%");
		
		// validation check
		long isIndocAtSubFolder_pg02 = selectOne("projectGenSqlMap.isIndocAtSubFolder_pg02",processInfoVO);
		
		if(isIndocAtSubFolder_pg02>0) {
			outMsg = "There are documents in the folder or subfolder. So the process type cannot be set to this folder";
			return outMsg;
		}
		
		
		
		
		String doc_type = processInfoVO.getDoc_type();
		
		
		// 해당폴더의 하위에 문서가 존재하는지 체크
		
		// 해당 프로젝트,폴더에 General이 아닌 프로세스 타입이 할당되는 경우 
		// 해당 폴더부터 하위 모든 폴더들에 대하여 해당 프로젝트의 폴더 권한정보에서  프로젝트 멤버가 아닌 'P' 유저들의 쓰기/삭제권한 update
		if(!doc_type.equals("General")) {
			// 1) 
			
		}
		
		
		
		insert("projectGenSqlMap.addProcessClass", processInfoVO);
		
		return outMsg;
	}
	
	public String updateClass(ProcessInfoVO processInfoVO) {
		String outMsg = "OK";
		
		String prj_id = processInfoVO.getPrj_id();
		
		// 현재 설정된 폴더 내에 document가 존재하는지 체크 (존재하면 변경불가)
		String exFolder_id = selectOne("projectGenSqlMap.getExFolderId_pg02",processInfoVO);
		String exProcess_type = selectOne("projectGenSqlMap.getExProcessType_pg02",processInfoVO);
		
		
		if(exFolder_id.equals("")) exFolder_id = processInfoVO.getProcess_folder_path_id();
		long folder_id = Long.parseLong(exFolder_id);
		processInfoVO.setFolder_id(folder_id);
		String getFolderPath = selectOne("projectGenSqlMap.getFolderPath_pg02",processInfoVO);
		processInfoVO.setFolder_path(getFolderPath+"%");
		long isIndocAtSubFolder_pg02 = selectOne("projectGenSqlMap.isIndocAtSubFolder_pg02",processInfoVO);
		if(isIndocAtSubFolder_pg02 > 0) {
			long tmpId = Long.parseLong(processInfoVO.getProcess_folder_path_id());
			// 폴더 아이디를 변경하려는 경우에 해당
			// 폴더아이디가 아닌 다른 값에 대하여는 validation check 하지 않아도 됨
			if(folder_id != tmpId) {
				outMsg = "There are documents in the folder or subfolder. So the process type cannot be set to this folder";
				return outMsg;
			}
			
			// 해당 프로세스에 문서가 있는 경우, process_type을 변경할 수 없다.
			String chgProcess = processInfoVO.getProcess_type();
			if(!exProcess_type.equals(chgProcess)) {
				outMsg = "There are documents in the folder or subfolder. So the process type cannot be changed";
				return outMsg;
			}
			
		}
		
		
		// 바꾸려고 하는 대상 폴더에 대한 validation check
		folder_id = Long.parseLong(processInfoVO.getProcess_folder_path_id());
		
		
		processInfoVO.setFolder_id(folder_id);
		getFolderPath = selectOne("projectGenSqlMap.getFolderPath_pg02",processInfoVO);
		processInfoVO.setFolder_path(getFolderPath+"%");
		
		isIndocAtSubFolder_pg02 = selectOne("projectGenSqlMap.isIndocAtSubFolder_pg02",processInfoVO);
		
		if(isIndocAtSubFolder_pg02>0) {
			// 폴더가 바뀌지 않은 경우에만 나머지 값 저장 가능
			if(!exFolder_id.equals(processInfoVO.getProcess_folder_path_id())) {
				outMsg = "There are documents in the folder or subfolder. So the process type cannot be set to this folder";
				return outMsg;
			}
			
			// 해당 프로세스에 문서가 있는 경우, process_type을 변경할 수 없다.
			String chgProcess = processInfoVO.getProcess_type();
			if(!exProcess_type.equals(chgProcess)) {
				outMsg = "There are documents in the folder or subfolder. So the process type cannot be changed";
				return outMsg;
			}
			
		}
		
		// 업데이트 가능한 상태
		
		update("projectGenSqlMap.updateClass", processInfoVO);
		
		return outMsg;
	}
	
	public void delManualClass(ProcessInfoVO processInfoVO) {
		delete("projectGenSqlMap.pg02_deleteClass", processInfoVO);
	}
	
	
	public List<PrjCodeSettingsVO> pg03_getGrid(PrjCodeSettingsVO prjCodesettingsVO) {
		List<PrjCodeSettingsVO> result = selectList("projectGenSqlMap.pg03_getGrid",prjCodesettingsVO);
		return result;
	}
	
	public int pg03_ClearTbl(PrjCodeSettingsVO prjCodeSettingsVO) {
		return delete("projectGenSqlMap.pg03_ClearTbl",prjCodeSettingsVO);
	}
	
	public int pg03_copyTbl(PrjCodeSettingsVO prjCodeSettingsVO) {
		return insert("projectGenSqlMap.pg03_copyCode",prjCodeSettingsVO);
	}
	
	public int pg03_delCode(PrjCodeSettingsVO prjCodeSettingsVO) {
		return delete("projectGenSqlMap.pg03_delCode", prjCodeSettingsVO);
	}
	
	public int pg03_chkCodeId(PrjCodeSettingsVO prjCodeSettingsVO) {
		int result = selectOne("projectGenSqlMap.pg04_chkCodeId",prjCodeSettingsVO);
		return result;
	}
	
	public int pg03_updateCode(PrjCodeSettingsVO prjCodeSettingsVO) {
		return update("projectGenSqlMap.pg04_updateCode", prjCodeSettingsVO);
	}
	public int pg03_insertCode(PrjCodeSettingsVO prjCodeSettingsVO) {
		return insert("projectGenSqlMap.pg04_insertCode",prjCodeSettingsVO);
	}

	public List<PrjCodeSettingsVO> pg04_getCode(PrjCodeSettingsVO prjCodeSettingsVO){
		List<PrjCodeSettingsVO> result = selectList("projectGenSqlMap.pg04_getCode",prjCodeSettingsVO);
		return result;		
	}
	public List<PrjCodeSettingsVO> pg04_getStep(PrjCodeSettingsVO prjCodeSettingsVO) {
		List<PrjCodeSettingsVO> result = selectList("projectGenSqlMap.pg04_getStep",prjCodeSettingsVO);
		return result;
	}
	
	public void pg04_updateCode(PrjCodeSettingsVO prjCodeSettingsVO) {
		update("projectGenSqlMap.pg04_updateCode", prjCodeSettingsVO);
	}
	
	public void pg04_delCode(PrjCodeSettingsVO prjCodeSettingsVO) {
		delete("projectGenSqlMap.pg04_delCode", prjCodeSettingsVO);
	}
	
	public int pg04_insertCode(PrjCodeSettingsVO prjCodeSettingsVO) {
		String set_code_id = selectOne("prjcodesettingsSqlMap.pg04_getMaxCode",prjCodeSettingsVO);
		prjCodeSettingsVO.setSet_code_id(set_code_id);
		
		return insert("projectGenSqlMap.pg04_insertCode",prjCodeSettingsVO);
	}
	
	public int pg04_chkCodeId(PrjCodeSettingsVO prjCodeSettingsVO) {
		int result = selectOne("projectGenSqlMap.pg04_chkCodeId",prjCodeSettingsVO);
		return result;
	}
	
	public void pg04_ClearTbl(PrjCodeSettingsVO prjCodeSettingsVO) {
		delete("projectGenSqlMap.pg04_ClearTbl",prjCodeSettingsVO);
	}
	
	public void pg04_copyTbl(PrjCodeSettingsVO prjCodeSettingsVO) {
		insert("projectGenSqlMap.pg04_copyCode",prjCodeSettingsVO);
	}
	
	public int pg07_ClearTbl(PrjDisciplineVO prjDisciplineVO) {
		int cnt = delete("projectGenSqlMap.pg07_ClearDiscip",prjDisciplineVO);
		cnt += delete("projectGenSqlMap.pg07_ClearDiscipFolder",prjDisciplineVO);
		return cnt;
	}
	
	public int pg07_copyTbl(PrjDisciplineVO prjDisciplineVO) {
		int cnt = insert("projectGenSqlMap.pg07_copyDiscipCode",prjDisciplineVO);
		cnt += insert("projectGenSqlMap.pg07_copyDiscipFolder",prjDisciplineVO);
		return cnt;
	}

	public List<PrjDisciplineVO> pg07_getDiscp(PrjDisciplineVO prjDisciplineVO) {
		List<PrjDisciplineVO> result = selectList("projectGenSqlMap.pg07_getDiscp",prjDisciplineVO);
		return result;
	}

	public int pg07_chkCodeId(PrjDisciplineVO prjDisciplineVO) {
		return selectOne("projectGenSqlMap.pg07_chkCodeId",prjDisciplineVO);
	}
	
	public int pg07_updateCode(PrjDisciplineVO prjDisciplineVO) {
		return update("projectGenSqlMap.pg07_updateCode", prjDisciplineVO);
	}
	
	public int pg07_insertCode(PrjDisciplineVO prjDisciplineVO) {
		return insert("projectGenSqlMap.pg07_insertCode",prjDisciplineVO);
	}

	public Long pg07_getMaxId(PrjDisciplineVO prjDisciplineVO) {
//		PrjDisciplineVO result = selectOne("projectGenSqlMap.pg07_getMaxId",prjDisciplineVO);
//		return result.getGetMax();
		return selectOne("projectGenSqlMap.pg07_getMaxId",prjDisciplineVO);
	}


	public PrjDisciplineVO pg07_getFolderPath(PrjDisciplineVO prjDisciplineVO) {
		return selectOne("projectGenSqlMap.pg07_getFolderPath",prjDisciplineVO);
	}


	public List<String> pg07_getOldPath(PrjDisciplineVO prjDisciplineVO) {
		return selectList("projectGenSqlMap.pg07_getOldPath",prjDisciplineVO);
	}
	
	public int pg07_delFolderId(PrjDisciplineVO prjDisciplineVO) {
		return delete("projectGenSqlMap.pg07_delFolderId",prjDisciplineVO);
	}
	
	public int pg07_insertFolderId(PrjDisciplineVO prjDisciplineVO) {
		return insert("projectGenSqlMap.pg07_insertFolderId",prjDisciplineVO);
	}
	
	public int pg07_delValidateChk(PrjDisciplineVO prjDisciplineVO) {
		return selectOne("projectGenSqlMap.pg07_delValidateChk",prjDisciplineVO);
	}
	
	public List<PrjDisciplineVO> pg07_insertValidateChk(PrjDisciplineVO prjDisciplineVO) {
		return selectList("projectGenSqlMap.pg07_insertValidateChk",prjDisciplineVO);
	}
	
	public int pg07_delDiscip(PrjDisciplineVO prjDisciplineVO) {
		return delete("projectGenSqlMap.pg07_delDiscip",prjDisciplineVO);
	}

	public List<UsersVO> searchUser(String keyword) {
		return selectList("projectGenSqlMap.searchUser",keyword);
	}


	public String getRvc(String prj_id) {
		return selectOne("projectGenSqlMap.getRvc",prj_id);
	}
	
	public int isUseRevNo(PrjCodeSettingsVO prjcodesettingsvo){
		return selectOne("prjcodesettingsSqlMap.isUseRevNo", prjcodesettingsvo);
	}
	
	public int isUseDocSize(PrjCodeSettingsVO prjcodesettingsvo) {
		return selectOne("prjcodesettingsSqlMap.isUseDocSize", prjcodesettingsvo);
	}

	public int pg04_del(PrjCodeSettingsVO vo) {
		String code_type = vo.getSet_code_type();
		
		
		
		switch(code_type) {
			case "REVISION NUMBER": // 해당 리비전 넘버를 사용하는 도서가 있는지 체크
				if(isUseRevNo(vo) > 0) {
					System.out.println("해당 리비전 넘버를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "DOC.SIZE":
				if(isUseDocSize(vo) > 0) {
					System.out.println("해당 doc_size를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VENDOR DOC.SIZE":
				if(isUseVDocSize(vo) > 0) {
					System.out.println("해당 doc_size를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "TR ISSUE PURPOSE":
				if(isUseTRIssuePurpose(vo) > 0) {
					System.out.println("해당 TR ISSUE PURPOSE를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VTR ISSUE PURPOSE":
				if(isUseVTRIssuePurpose(vo) > 0) {
					System.out.println("해당 VTR ISSUE PURPOSE를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "STR ISSUE PURPOSE":
				if(isUseSTRIssuePurpose(vo) > 0) {
					System.out.println("해당 STR ISSUE PURPOSE를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "DOC.CATEGORY":
				if(isUseDocCate(vo) > 0) {
					System.out.println("해당 doc_cate를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VENDOR DOC.CATEGORY":
				if(isUseVDocCate(vo) > 0) {
					System.out.println("해당 doc_cate를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "TR RETURN STATUS":
				if(isUseTRReturnSt(vo) > 0) {
					System.out.println("해당 TR RETYRB STATUS를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VTR RETURN STATUS":
				if(isUseVTRReturnSt(vo) > 0) {
					System.out.println("해당 VTR RETYRB STATUS를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "DOC.TYPE":
				if(isUseDocType(vo) > 0) {
					System.out.println("해당 VTR RETYRB STATUS를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			default:
				return -1;
		}
		
		
		
		return delete("projectGenSqlMap.pg04_delCode",vo);
	}

						

	private int isUseDocType(PrjCodeSettingsVO vo) {
		int total = 0;
		int engCnt = selectOne("prjcodesettingsSqlMap.isUseDocType", vo);
		total += engCnt;
		int vdrCnt = selectOne("prjcodesettingsSqlMap.isUseVDocType", vo);
		total += vdrCnt;
		int sdcCnt = selectOne("prjcodesettingsSqlMap.isUseSDocType", vo);
		total += sdcCnt;
		
		return total;
	}

	private int isUseVTRReturnSt(PrjCodeSettingsVO vo) {
		return selectOne("prjcodesettingsSqlMap.isUseVTRReturnSt", vo);
	}

	private int isUseTRReturnSt(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseTRReturnSt", vo);
	}

	private int isUseVDocCate(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseVDocCate", vo);
	}
	
	private int isUseDocCate(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseDocCate", vo);
	}
	private int isUseSTRIssuePurpose(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseSTRIssuePurpose", vo);
	}

	private int isUseVTRIssuePurpose(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseVTRIssuePurpose", vo);
	}

	private int isUseTRIssuePurpose(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseTRIssuePurpose", vo);
	}

	private int isUseVDocSize(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		return selectOne("prjcodesettingsSqlMap.isUseVDocSize", vo);
	}


	public int pg04_delVal(PrjCodeSettingsVO vo) {
		// TODO Auto-generated method stub
		String code_type = vo.getSet_code_type();
		

		switch(code_type) {
			case "REVISION NUMBER": // 해당 리비전 넘버를 사용하는 도서가 있는지 체크
				if(isUseRevNo(vo) > 0) {
					System.out.println("해당 리비전 넘버를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "DOC.SIZE":
				if(isUseDocSize(vo) > 0) {
					System.out.println("해당 doc_size를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VENDOR DOC.SIZE":
				if(isUseVDocSize(vo) > 0) {
					System.out.println("해당 doc_size를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "TR ISSUE PURPOSE":
				if(isUseTRIssuePurpose(vo) > 0) {
					System.out.println("해당 TR ISSUE PURPOSE를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VTR ISSUE PURPOSE":
				if(isUseVTRIssuePurpose(vo) > 0) {
					System.out.println("해당 VTR ISSUE PURPOSE를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "STR ISSUE PURPOSE":
				if(isUseSTRIssuePurpose(vo) > 0) {
					System.out.println("해당 STR ISSUE PURPOSE를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "DOC.CATEGORY":
				if(isUseDocCate(vo) > 0) {
					System.out.println("해당 doc_cate를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VENDOR DOC.CATEGORY":
				if(isUseVDocCate(vo) > 0) {
					System.out.println("해당 doc_cate를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "TR RETURN STATUS":
				if(isUseTRReturnSt(vo) > 0) {
					System.out.println("해당 TR RETURN STATUS를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "VTR RETURN STATUS":
				if(isUseVTRReturnSt(vo) > 0) {
					System.out.println("해당 VTR RETURN STATUS를 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			case "DOC.TYPE":
				if(isUseDocType(vo) > 0) {
					System.out.println("해당 DOC TYPE을 사용하는 도서가 있습니다.");
					return -1;
				}
				break;
			default:
				return 1;
		}
		
		return 1;
		
	}


	


}
