package kr.co.hhi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.ProcessInfoVO;
import kr.co.hhi.service.PrjDocumentIndexService;
import kr.co.hhi.service.ProcessInfoService;

@Controller
@RequestMapping("/*")
public class ProcessInfoCont {

	protected ProcessInfoService processinfoService;
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processinfoService) {
		this.processinfoService = processinfoService;
	}

	/**
	 * 1. 메소드명 : getProcessTypeByFolderPath
	 * 2. 작성일: 2021-12-17
	 * 3. 작성자: 소진희
	 * 4. 설명: Document Control 페이지에서 property 구분을 위한 각 폴더별 프로세스 타입 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "getProcessTypeByFolderPath.do")
	@ResponseBody
	public Object getProcessTypeByFolderPath(ProcessInfoVO processinfovo) {
		List<Object> result = new ArrayList<Object>();
		List<ProcessInfoVO> getProcessTypeByFolderPath = processinfoService.getProcessTypeByFolderPath(processinfovo);
		result.add(getProcessTypeByFolderPath);
		return result;
	}
}