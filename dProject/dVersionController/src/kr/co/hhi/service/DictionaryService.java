package kr.co.hhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.model.DictionarySearchVO;
import kr.co.hhi.model.ProcessInfoVO;

@Service
public class DictionaryService extends AbstractService {
	
	public List<DictionarySearchVO> getDictionary() {
		List<DictionarySearchVO> getDictionary = selectList("dictionarySqlMap.getDictionary");
		return getDictionary;
	}
	
	public List<DictionarySearchVO> findDictionaryAbbr(DictionarySearchVO dictionarysearchVo){
		List<DictionarySearchVO> findDictionaryAbbr = selectList("dictionarySqlMap.findDictionaryAbbr",dictionarysearchVo);
		return findDictionaryAbbr;	
	}
	
	public void insertDictionary(DictionarySearchVO dictionarysearchVo) { // 인서트를 실행 시켜 줌
		insert("dictionarySqlMap.insertDictionary", dictionarysearchVo);
	}
	
	public void updateDictionary(DictionarySearchVO dictionarysearchVo) { // 업데이트를 실행 시켜 줌
		update("dictionarySqlMap.updateDictionary", dictionarysearchVo);
	}
	
	public void delDictionary(DictionarySearchVO dictionarysearchVo) {
		delete("dictionarySqlMap.deleteDictionary",dictionarysearchVo);
	}
	
	public void delDictionaryAll(DictionarySearchVO dictionarysearchVo) {
		delete("dictionarySqlMap.deleteDictionaryAll",dictionarysearchVo);
	}
}
