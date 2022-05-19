package kr.co.hhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.hhi.common.util.WriteListToExcelFile;
import kr.co.hhi.model.ReportVo;
import kr.co.hhi.service.PrjReportEngineeringStatusService;
import kr.co.hhi.service.ProjectReportCorrStatusService;

@Controller
@RequestMapping("/*")
public class ProjectReportCorrStatusCont {

protected ProjectReportCorrStatusService projectReportCorrStatusService;
	
	@Autowired
	public void setProjectReportCorrStatusService(ProjectReportCorrStatusService projectReportCorrStatusService) {
		this.projectReportCorrStatusService = projectReportCorrStatusService;
	}
	
	@Value("#{config['file.upload.path']}")
	private String file_upload_path;
	
	@Value("#{config['report.template.path']}")
	private String report_template_path;
	
	/**
	 * 1. 메소드명 : vendorExcelDownload
	 * 2. 작성일: 2022-02-21
	 * 3. 작성자: 박현준
	 * 4. 설명: 보고서 생성
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "corrStatusExcelDownload.do")
	@ResponseBody
	public void corrStatusExcelDownload(ReportVo reportVo, HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		String columnList = reportVo.getColumnList();
		String value= reportVo.getValueList();
		if(value == null) {
			value = "";
		}
		String valueList[] = value.split("@#@#@");

		String fileName=reportVo.getFileName();
		String projecetName=reportVo.getProjecetName();
		String discipline=reportVo.getDiscipline();
		String reportType= reportVo.getReportType();
		String templatePath="";
		
		String cut_off_date = reportVo.getCut_off_date();
		String workStep = reportVo.getWorkStep();
		
		
		String date_s = reportVo.getDate_s();
		String date_d = reportVo.getDate_d();
		
		
		templatePath = file_upload_path + report_template_path;
		
		long time = System.currentTimeMillis();
		fileName =  "result_" + time + ".xlsx";
		WriteListToExcelFile.writeNoticeListToFile("5", response, templatePath, fileName, reportType, projecetName, discipline, columnList, valueList, cut_off_date, workStep, date_s, date_d, 0);
	}
	
	/**
	 * 1. 메소드명 : corrStatusGetReportData
	 * 2. 작성일: 2022-03-08
	 * 3. 작성자: 박현준
	 * 4. 설명: 보고서 데이터 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "corrStatusGetReportData.do")
	@ResponseBody
	public List<Object> corrStatusGetReportData(ReportVo reportVo) throws Exception{
		List<Object> result= projectReportCorrStatusService.getReportData(reportVo);
		return result;
	}
}
