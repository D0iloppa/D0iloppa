package kr.co.hhi.controller;

import java.util.ArrayList;
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

@Controller
@RequestMapping("/*")
public class PrjReportEngineeringStatusCont {
	
	protected PrjReportEngineeringStatusService prjReportEngineeringStatusService;
	
	@Autowired
	public void setPrjReportEngineeringStatusService(PrjReportEngineeringStatusService prjReportEngineeringStatusService) {
		this.prjReportEngineeringStatusService = prjReportEngineeringStatusService;
	}
	
	
	@Value("#{config['file.upload.path']}")
	private String file_upload_path;
	
	@Value("#{config['report.template.path']}")
	private String report_template_path;	
	
	/**
	 * 1. 메소드명 : engineeringExcelDownload
	 * 2. 작성일: 2022-02-21
	 * 3. 작성자: 박현준
	 * 4. 설명: 데이터를 하나의 시트에 다운
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "engineeringExcelDownload.do")
	@ResponseBody
	public void engineeringExcelDownload(ReportVo reportVo, HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		String columnList = reportVo.getColumnList();
		String value= reportVo.getValueList();
		
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
		
		int discipLength = reportVo.getDiscipLength();
		
		templatePath = file_upload_path + report_template_path;
		
		long time = System.currentTimeMillis();
		fileName =  "result_" + time + ".xlsx";
		WriteListToExcelFile.writeNoticeListToFile("1", response, templatePath, fileName, reportType, projecetName, discipline, columnList, valueList, cut_off_date, workStep, date_s, date_d, discipLength);
	}
	
	/**
	 * 1. 메소드명 : engineeringExcelDownloadDiscip
	 * 2. 작성일: 2022-02-21
	 * 3. 작성자: 박현준
	 * 4. 설명: Discipline 별 시트를 나눠서 다운
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "engineeringExcelDownloadDiscip.do")
	@ResponseBody
	public void engineeringExcelDownloadDiscip(ReportVo reportVo, HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		String columnList = reportVo.getColumnList();
		String value= reportVo.getValueList();
		
		String valueList[] = value.split("@#@#@");
		String columnList2[] = columnList.split("###");
		
		String discipList = "";
		
		int columnListCnt = 0;
		
		if(reportVo.getReportType().equals("5")) {
			for(int i=0;i<columnList2.length;i++) {
				if(columnList2[i].toUpperCase().equals("DISCIPLINE")) {
					break;
				}else {
					columnListCnt +=1;
				}
			}
		}else if(reportVo.getReportType().equals("11") || reportVo.getReportType().equals("12") || reportVo.getReportType().equals("13")) {
			columnListCnt = 0;
		}else {
			columnListCnt = 1;
		}
		
		for(int i=0;i<valueList.length;i++) {
			String disciplineList[] = valueList[i].split("@@@");
			
			discipList += disciplineList[columnListCnt] + ",";
		}
		
		discipList = discipList.substring(0, discipList.length() - 1);

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
		WriteListToExcelFile.writeNoticeListToFileDiscip("1", response, templatePath, fileName, reportType, projecetName, discipline, columnList, valueList, cut_off_date, workStep, date_s, date_d, discipList);
	}
	
	/**
	 * 1. 메소드명 : engineeringGetReportData
	 * 2. 작성일: 2022-02-22
	 * 3. 작성자: 박현준
	 * 4. 설명: 보고서 데이터 가져오기
	 * 5. 수정일: 
	 */
	@RequestMapping(value = "engineeringGetReportData.do")
	@ResponseBody
	public List<Object> engineeringGetReportData(ReportVo reportVo) throws Exception{
		List<Object> result= prjReportEngineeringStatusService.getReportData(reportVo);
		return result;
	}
}
