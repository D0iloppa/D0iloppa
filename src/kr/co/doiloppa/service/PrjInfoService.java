package kr.co.doiloppa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.model.CodeInfoVO;
import kr.co.doiloppa.model.FolderInfoVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDisciplineVO;
import kr.co.doiloppa.model.PrjFolderInfoVO;
import kr.co.doiloppa.model.PrjInfoVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.UsersVO;

@Service
public class PrjInfoService extends AbstractService {
	public PrjInfoVO getLastConPrj(UsersVO usersvo){
		PrjInfoVO getLastConPrj = selectOne("prjinfoSqlMap.getLastConPrj", usersvo);
		return getLastConPrj;
	}
	public List<PrjInfoVO> getPrjInfoList(UsersVO usersvo){
		List<PrjInfoVO> getPrjInfoList = selectList("prjinfoSqlMap.getPrjInfoList", usersvo);
		return getPrjInfoList;
	}
	public PrjInfoVO getPrjInfo(PrjInfoVO prjinfovo){
		PrjInfoVO getPrjInfo = selectOne("prjinfoSqlMap.getPrjInfo", prjinfovo);
		return getPrjInfo;
	}
	
	public List<PrjInfoVO> getPrjList(){
		List<PrjInfoVO> getPrjList = selectList("prjinfoSqlMap.getPrjList");
		return getPrjList;
	}
	public List<PrjInfoVO> searchDesignerProject(PrjInfoVO prjinfovo){
		List<PrjInfoVO> searchDesignerProject = selectList("prjinfoSqlMap.searchDesignerProject",prjinfovo);
		return searchDesignerProject;
	}
	public String insertPrjInfo(PrjInfoVO prjinfovo){
		insert("prjinfoSqlMap.insertPrjInfo", prjinfovo);
		String prj_id = prjinfovo.getPrj_id();
		return prj_id;
	}
	public void initProjectProcess(PrjInfoVO prjinfovo){
		insert("prjinfoSqlMap.initProjectProcess",prjinfovo);
	}
	public void insertDCCPrjMember(PrjMemberVO prjmembervo){
		insert("prjinfoSqlMap.insertDCCPrjMember", prjmembervo);
	}
	public void insertCopyPrjFolder(PrjFolderInfoVO prjfolderinfovo){
		insert("prjinfoSqlMap.insertCopyPrjFolder", prjfolderinfovo);
	}
	public void updatePrjInfo(PrjInfoVO prjinfovo){
		update("prjinfoSqlMap.updatePrjInfo", prjinfovo);
	}
	public void deleteDCCPrjMember(PrjMemberVO prjmembervo){
		delete("prjinfoSqlMap.deleteDCCPrjMember", prjmembervo);
	}
	public List<PrjMemberVO> getPrjDCC(PrjMemberVO prjmembervo){
		List<PrjMemberVO> getPrjDCC = selectList("prjinfoSqlMap.getPrjDCC", prjmembervo);
		return getPrjDCC;
	}
	public List<PrjMemberVO> getDCCs(){
		List<PrjMemberVO> getDCCs = selectList("prjinfoSqlMap.getDCCs");
		return getDCCs;
	}
	public PrjMemberVO getPrjDCCYN(PrjMemberVO prjmembervo){
		PrjMemberVO getPrjDCCYN = selectOne("prjinfoSqlMap.getPrjDCCYN", prjmembervo);
		return getPrjDCCYN;
	}
	public void updatePrjDCCYN(PrjMemberVO prjmembervo){
		update("prjinfoSqlMap.updatePrjDCCYN", prjmembervo);
	}
	
	public void pg_updatePrjInfo(PrjInfoVO prjinfovo){
		update("prjinfoSqlMap.pg_updatePrjInfo", prjinfovo);
	}
	public List<PrjDisciplineVO> getDiscipList(PrjInfoVO prjinfovo) {
		List<PrjDisciplineVO> discipList = selectList("prjinfoSqlMap.getDiscipList", prjinfovo);
		return discipList;
	}

	public String getFolderName(FolderInfoVO getFolderName) {
		return selectOne("folderinfoSqlMap.getFolderName", getFolderName);
	}

	public String getPrjNm(String prj_id) {
		return selectOne("prjinfoSqlMap.getPrjName",prj_id);
	}
	
	public int setRootFolderNm(FolderInfoVO update) {
		return update("folderinfoSqlMap.setRootFolderNm",update);		
	}
	
	public void initProjectSystemPrefix(PrjInfoVO prjinfovo) {
		insert("prjinfoSqlMap.initProjectSystemPrefix",prjinfovo);
	}
		
	public List<PrjInfoVO> getPrjInfoListTop(UsersVO usersvo){
		List<PrjInfoVO> getPrjInfoList = selectList("prjinfoSqlMap.getPrjInfoListTop", usersvo);
		return getPrjInfoList;
	}
		
	public int isProjectMember(PrjInfoVO prjinfovo) {
		List<PrjInfoVO> isIn = selectList("prjinfoSqlMap.isProjectMember",prjinfovo);
		
		return isIn.size();
	}
	
	public List<CodeInfoVO> getPrjType() {
		return selectList("prjinfoSqlMap.getPrjType");
	}
	
	public CodeInfoVO getMaxsetOrder() {
		CodeInfoVO getMaxsetOrder = selectOne("prjinfoSqlMap.getMaxsetOrder");
		return getMaxsetOrder;
	}
	
	public CodeInfoVO getMaxCode() {
		CodeInfoVO getMaxCode = selectOne("prjinfoSqlMap.getMaxCode");
		return getMaxCode;
	}
	
	public void insertCodeInfo(CodeInfoVO codeInfoVo) {
		insert("prjinfoSqlMap.insertCodeInfo",codeInfoVo);
	}
	
	public void updateCodeInfo(CodeInfoVO codeInfoVo) {
		update("prjinfoSqlMap.updateCodeInfo",codeInfoVo);
	}
	
	public void deleteCodeInfo(CodeInfoVO codeInfoVo) {
		delete("prjinfoSqlMap.deleteCodeInfo",codeInfoVo);
	}
	
	public void prjCodeInfoUpdate(CodeInfoVO codeInfoVo) {
		update("prjinfoSqlMap.prjCodeInfoUpdate",codeInfoVo);
	}
	public int getUseCodeYN(PrjInfoVO prjinfovo) {
		int getUseCodeYN = selectOne("prjinfoSqlMap.getUseCodeYN",prjinfovo);
		return getUseCodeYN;
	}
	
	public CodeInfoVO getUseYcodeNm(CodeInfoVO codeInfoVo) {
		CodeInfoVO getUseYcodeNm = selectOne("prjinfoSqlMap.getUseYcodeNm",codeInfoVo);
		return getUseYcodeNm;
	}
}