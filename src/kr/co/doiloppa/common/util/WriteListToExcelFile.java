package kr.co.doiloppa.common.util;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;


public class WriteListToExcelFile {

	static int idx=0;
	
	public static void writeNoticeListToFile(String report, HttpServletResponse response, String templatePath, String fileName, String reportType, String projecetName, String discipline, String columnList, String[] valueList,  String cut_off_date, String workStep, String date_s, String date_d,int discipLength) throws Exception{
		String sheetName = "";
		InputStream fis = null;
		int endPoint = 0; 
		try {
        	templatePath = templatePath+"report_type_"+report+"_"+reportType+".xlsx";
			fis = new FileInputStream(templatePath);
			
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			//******** SET FONT  ******** //
			Font font_bold_14 = workbook.createFont();
			font_bold_14.setFontName("Times New Roman"); 
			font_bold_14.setFontHeight((short)(14*20)); 
			font_bold_14.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    	
	    	Font font_bold_12 = workbook.createFont();
	    	font_bold_12.setFontName("Times New Roman"); 
	    	font_bold_12.setFontHeight((short)(12*20)); 
	    	font_bold_12.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    	
	    	Font font_bold_11 = workbook.createFont();
	    	font_bold_11.setFontName("Times New Roman"); 
	    	font_bold_11.setFontHeight((short)(11*20)); 
	    	font_bold_11.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    	
	    	Font font_bold_10 = workbook.createFont();
	    	font_bold_10.setFontName("Times New Roman"); 
	    	font_bold_10.setFontHeight((short)(10*20)); 
			
	    	CellStyle left_14_bold = workbook.createCellStyle();
	    	left_14_bold.setAlignment(CellStyle.ALIGN_LEFT);
	    	left_14_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	left_14_bold.setFont(font_bold_14);
			
	    	CellStyle left_12_bold = workbook.createCellStyle();
	    	left_12_bold.setAlignment(CellStyle.ALIGN_LEFT);
	    	left_12_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	left_12_bold.setFont(font_bold_12);
	    	
	    	CellStyle right_12_bold = workbook.createCellStyle();
	    	right_12_bold.setAlignment(CellStyle.ALIGN_RIGHT);
	    	right_12_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	right_12_bold.setFont(font_bold_12);
			
	    	CellStyle center_11_bold = workbook.createCellStyle();
	    	center_11_bold.setAlignment(CellStyle.ALIGN_CENTER);
	    	center_11_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	center_11_bold.setFont(font_bold_11);
	    	center_11_bold.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	center_11_bold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	center_11_bold.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	center_11_bold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	
	    	
	    	CellStyle center_10 = workbook.createCellStyle();
	    	center_10.setAlignment(CellStyle.ALIGN_CENTER);
	    	center_10.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	center_10.setFont(font_bold_10);
	    	center_10.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	center_10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	center_10.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	center_10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			
	    	//******** SET FONT END ******** //
	    	
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			
			int startRow=0;
			boolean bChart=false;
			boolean discipFlag = false;
			int discipInt = 0;
			
			if(report.equals("1")) {
				if ( report.equals("1") && reportType.equals("1")) { // DCI List
					workbook.setSheetName(0, "DCI_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("1") && reportType.equals("2")) {
					workbook.setSheetName(0, "DCI_DETAIL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("1") &&  reportType.equals("3")) {
					workbook.setSheetName(0, "DCI_HISTORY_LIST");
					startRow=7; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("1") &&  reportType.equals("4")) {
					workbook.setSheetName(0, "DCI_BEHIND_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("1") && reportType.equals("5")) {
					workbook.setSheetName(0, "DCI_TRANSMITTAL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(13).setCellStyle(right_12_bold);
				} else if ( report.equals("1") &&  reportType.equals("6")) { 
					workbook.setSheetName(0, "DCI_PROGRESS_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				}  else if ( report.equals("1") &&  reportType.equals("7")) { 
					workbook.setSheetName(0, "DCI_WORKDONE_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("1") && reportType.equals("8")) { 
					workbook.setSheetName(0, "DCI_WORKDONE_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("1") && reportType.equals("9")) { 
					sheetName = "DCI_WEEK_PROGRESS_GRAPH";
					workbook.setSheetName(0, "DCI_WEEK_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
					
					 bChart = true;
					 discipFlag = true;
					 
				} else if ( report.equals("1") && reportType.equals("10")) { 
					sheetName = "DCI_MONTHLY_PROGRESS_GRAPH";
					workbook.setSheetName(0, "DCI_MONTHLY_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
					
					 bChart=true;
				} else if ( report.equals("1") && reportType.equals("11")) { 
					workbook.setSheetName(0, "WBS_DETAIL");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
				} else if ( report.equals("1") && reportType.equals("12")) { 
					workbook.setSheetName(0, "WBS_SUMMARY");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(7).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(7).setCellStyle(right_12_bold);
				} else if ( report.equals("1") && reportType.equals("13")) { 
					workbook.setSheetName(0, "WBS_DETAIL(IFA/FAC)");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(7).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(7).setCellStyle(right_12_bold);
				}
			}else if(report.equals("2")) {
				if ( report.equals("2") && reportType.equals("1")) { // PCI List
					workbook.setSheetName(0, "PCI_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("2") && reportType.equals("2")) { // PCI Detail List
					workbook.setSheetName(0, "PCI_DETAIL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("2") &&  reportType.equals("3")) { // PCI Behind List
					workbook.setSheetName(0, "PCI_BEHIND_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("2") &&  reportType.equals("4")) { // PCI Progress Status
					workbook.setSheetName(0, "PCI_PROGRESS_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
					
				} else if ( report.equals("2") &&  reportType.equals("5")) { // DCI Workdone List
					workbook.setSheetName(0, "PCI_WORKDONE_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("2") && reportType.equals("6")) { // DCI Workdone Status
					workbook.setSheetName(0, "PCI_WORKDONE_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
					
				} else if ( report.equals("2") && reportType.equals("7")) { // Weekly Progress Graph
					sheetName = "PCI_WEEK_PROGRESS_GRAPH";
					workbook.setSheetName(0, "PCI_WEEK_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);

					 bChart=true;
				} else if ( report.equals("2") && reportType.equals("8")) { // Monthly Progress Graph
					sheetName = "PCI_MONTHLY_PROGRESS_GRAPH";
					workbook.setSheetName(0, "PCI_MONTHLY_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
					
					 bChart=true;
					
				} else if ( report.equals("2") && reportType.equals("9")) { // WBS Detail
					workbook.setSheetName(0, "WBS_DETAIL");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
				}
			}else if(report.equals("3")) {
				if ( report.equals("3") && reportType.equals("1")) { // VDCI List
					workbook.setSheetName(0, "VDCI_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("3") && reportType.equals("2")) { // VDCI Detail List
					workbook.setSheetName(0, "VDCI_DETAIL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("3") &&  reportType.equals("3")) { // VDCI History List
					workbook.setSheetName(0, "VDCI_HISTORY_LIST");
					startRow=7; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("3") &&  reportType.equals("4")) { // VDCI Behind List
					workbook.setSheetName(0, "VDCI_BEHIND_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("3") && reportType.equals("5")) { // VDCI Transmittal List
					workbook.setSheetName(0, "VDCI_TRANSMITTAL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(13).setCellStyle(right_12_bold);
				} else if ( report.equals("3") &&  reportType.equals("6")) { // VDCI Progress Status
					workbook.setSheetName(0, "VDCI_PROGRESS_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(10).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				}  else if ( report.equals("3") &&  reportType.equals("7")) { // VDCI Workdone List
					workbook.setSheetName(0, "VDCI_WORKDONE_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("3") && reportType.equals("8")) { // VDCI Workdone Status
					workbook.setSheetName(0, "VDCI_WORKDONE_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
					
				} else if ( report.equals("3") && reportType.equals("9")) { // Weekly Progress Graph
					sheetName = "VDCI_WEEK_PROGRESS_GRAPH";
					workbook.setSheetName(0, "VDCI_WEEK_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
					
					 bChart=true;
				} else if ( report.equals("3") && reportType.equals("10")) { // Monthly Progress Graph
					sheetName = "VDCI_MONTHLY_PROGRESS_GRAPH";
					workbook.setSheetName(0, "VDCI_MONTHLY_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
					
					 bChart=true;
				} else if ( report.equals("3") && reportType.equals("11")) { // WBS Detail
					workbook.setSheetName(0, "WBS_DETAIL");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
				}
			}else if(report.equals("4")) {
				if ( report.equals("4") && reportType.equals("1")) { // SDCI List
					workbook.setSheetName(0, "SDCI_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("4") && reportType.equals("2")) { // SDCI Detail List
					workbook.setSheetName(0, "SDCI_DETAIL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("4") &&  reportType.equals("3")) { // SDCI History List
					workbook.setSheetName(0, "SDCI_HISTORY_LIST");
					startRow=7; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				} else if ( report.equals("4") &&  reportType.equals("4")) { // SDCI Behind List
					workbook.setSheetName(0, "SDCI_BEHIND_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("4") && reportType.equals("5")) { // SDCI Transmittal List
					workbook.setSheetName(0, "SDCI_TRANSMITTAL_LIST");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(13).setCellStyle(right_12_bold);
				} else if ( report.equals("4") &&  reportType.equals("6")) { // SDCI Progress Status
					workbook.setSheetName(0, "SDCI_PROGRESS_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				}else if ( report.equals("4") &&  reportType.equals("7")) { // SDCI Workdone List
					workbook.setSheetName(0, "SDCI_WORKDONE_LIST");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("4") && reportType.equals("8")) { // SDCI Workdone Status
					workbook.setSheetName(0, "SDCI_WORKDONE_STATUS");
					startRow=5; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
				} else if ( report.equals("4") && reportType.equals("9")) { // Weekly Progress Graph
					sheetName = "SDCI_WEEK_PROGRESS_GRAPH";
					workbook.setSheetName(0, "SDCI_WEEK_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
					
					 bChart=true;
				} else if ( report.equals("4") && reportType.equals("10")) { // Monthly Progress Graph
					sheetName = "SDCI_MONTHLY_PROGRESS_GRAPH";
					workbook.setSheetName(0, "SDCI_MONTHLY_PROGRESS_GRAPH");
					startRow=17; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
					
					 bChart=true;
					
				} else if ( report.equals("4") && reportType.equals("11")) { // WBS Detail
					workbook.setSheetName(0, "WBS_DETAIL");
					startRow=6; 
					sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
					sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
					sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
					
					sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
					sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
					sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
				}
			}else if(report.equals("5")) { // Correspondence Status
				workbook.setSheetName(0, "CORRESPONDENCE_STATUS");
				startRow=5; 
				sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
				sheet.getRow(3).createCell(0).setCellValue("Discipline : "+"");
				
				sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
				sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
				sheet.getRow(3).createCell(25).setCellValue("Duration : "+date_s +"~"+date_d);
			}
			
			String tmpColumnList[] = columnList.split("###");
			
			for ( int i=0; i<tmpColumnList.length; ++i ) {
				String cloumn = tmpColumnList[i];
				
				if ( sheet.getRow(startRow-1) == null ) { sheet.createRow(startRow-1);}
				sheet.getRow(startRow-1).createCell(i).setCellValue(cloumn);
				sheet.getRow(startRow-1).getCell(i).setCellStyle(center_11_bold);
			}
			
			for ( int i=0; i<valueList.length; ++i) {
				String tmpString = valueList[i];
				String tmp[] = tmpString.split("@@@");
				
				int maxK=0;
				int maxCnt=0;
				
				for ( int j=0; j< tmp.length; ++j ) {
					String tmpT = tmp[j];
					if ( tmpT == null || tmpT.equals("null")) tmpT="";
					
					String tmptT[] = tmpT.split("@,@");
					if ( maxCnt <=  tmptT.length ) { maxCnt =  tmptT.length; } 
				}
								
				for ( int j=0; j< tmp.length; ++j ) {
					String tmpT = tmp[j];
					if ( tmpT == null || tmpT.equals("null")) tmpT="";
					
					String tmptT[] = tmpT.split("@,@");
					for ( int k=0; k<tmptT.length; ++k ) {
						
						if ( sheet.getRow(startRow+i+k) == null ) {
							sheet.createRow(startRow+i+k);
						}
						
						
						
						
						
						
						sheet.getRow(startRow+i+k).createCell(j).setCellValue(tmptT[k]);
						sheet.getRow(startRow+i+k).getCell(j).setCellStyle(center_10);
						
						endPoint = (startRow+i+k);
						
						if(bChart) {
							String numericChk = tmptT[k];
							boolean isNumeric = false;
							float tmpFloat = 0;
							if(numericChk.contains("%")) {
								tmpFloat = Float.parseFloat(numericChk.replaceAll("%",""));
								isNumeric = true;
							}
							
							if(isNumeric) {
								XSSFCellStyle cellStyle = workbook.createCellStyle();
						        XSSFDataFormat format = workbook.createDataFormat();
						        cellStyle.setDataFormat(format.getFormat("#0.0%"));
						        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
						    	cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
						    	cellStyle.setFont(font_bold_10);
						    	cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
						    	cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
						    	cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
						    	cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
						        
								sheet.getRow(startRow+i+k).getCell(j).setCellValue(tmpFloat / 100);
								sheet.getRow(startRow+i+k).getCell(j).setCellType(Cell.CELL_TYPE_NUMERIC);
								sheet.getRow(startRow+i+k).getCell(j).setCellStyle(cellStyle);
							}
							
							
							
						}
						
						
						
					}
					
					if ( tmptT.length < maxCnt ) {
						sheet.addMergedRegion(new CellRangeAddress(startRow+i, (startRow+i+maxCnt-1 ), j, j));
					}
					
					if ( maxK <= tmptT.length-1 ) maxK = tmptT.length-1;
				}
				
				startRow = startRow + maxK;
			}
			
			if ( bChart ) {
				sheet = setChartPos(sheet,endPoint-2,reportType,sheetName, discipLength);
			}

			response.reset();
			response.setHeader("Set-Cookie", "fileDownload=true; path=/"); 
		    response.setHeader("Content-Disposition", String.format("attachment; filename=\""+fileName+"\"")); 
		    workbook.write(response.getOutputStream());
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			fis.close();
		}

        System.out.println(fileName + " written successfully");
    }
	
	//Discipline 시트별 엑셀 다운
	public static void writeNoticeListToFileDiscip(String report, HttpServletResponse response, String templatePath, String fileName, String reportType, String projecetName, String discipline, String columnList, String[] valueList,  String cut_off_date, String workStep, String date_s, String date_d, String discipList) throws Exception{
		String sheetName = "";
		InputStream fis = null;
		
        try {
        	String[] disciplineList = discipList.split(",");
        	
        	List<String> disciplineList2 = new ArrayList<String>();
        	
        	for(int i=0;i<disciplineList.length;i++) {
        		if(!disciplineList2.contains(disciplineList[i])) {	// list에 포함되어있는지 아닌지 체크
        			disciplineList2.add(disciplineList[i]);		// 해당 값이 없으면 넣기
        		}
        	}
        	
        	templatePath = templatePath+"report_type_"+report+"_"+reportType+".xlsx";
			fis = new FileInputStream(templatePath);
			
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			//******** SET FONT  ******** //
			Font font_bold_14 = workbook.createFont();
			font_bold_14.setFontName("Times New Roman"); 
			font_bold_14.setFontHeight((short)(14*20)); 
			font_bold_14.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    	
	    	Font font_bold_12 = workbook.createFont();
	    	font_bold_12.setFontName("Times New Roman"); 
	    	font_bold_12.setFontHeight((short)(12*20)); 
	    	font_bold_12.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    	
	    	Font font_bold_11 = workbook.createFont();
	    	font_bold_11.setFontName("Times New Roman"); 
	    	font_bold_11.setFontHeight((short)(11*20)); 
	    	font_bold_11.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    	
	    	Font font_bold_10 = workbook.createFont();
	    	font_bold_10.setFontName("Times New Roman"); 
	    	font_bold_10.setFontHeight((short)(10*20)); 
			
	    	CellStyle left_14_bold = workbook.createCellStyle();
	    	left_14_bold.setAlignment(CellStyle.ALIGN_LEFT);
	    	left_14_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	left_14_bold.setFont(font_bold_14);
			
	    	CellStyle left_12_bold = workbook.createCellStyle();
	    	left_12_bold.setAlignment(CellStyle.ALIGN_LEFT);
	    	left_12_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	left_12_bold.setFont(font_bold_12);
	    	
	    	CellStyle right_12_bold = workbook.createCellStyle();
	    	right_12_bold.setAlignment(CellStyle.ALIGN_RIGHT);
	    	right_12_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	right_12_bold.setFont(font_bold_12);
			
	    	CellStyle center_11_bold = workbook.createCellStyle();
	    	center_11_bold.setAlignment(CellStyle.ALIGN_CENTER);
	    	center_11_bold.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	center_11_bold.setFont(font_bold_11);
	    	center_11_bold.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	center_11_bold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	center_11_bold.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	center_11_bold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	
	    	
	    	CellStyle center_10 = workbook.createCellStyle();
	    	center_10.setAlignment(CellStyle.ALIGN_CENTER);
	    	center_10.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	    	center_10.setFont(font_bold_10);
	    	center_10.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	center_10.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	center_10.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	center_10.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			
	    	//******** SET FONT END ******** //
	    	
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			
			int startRow=0;
			boolean bChart=false;
			
			
			if(report.equals("1")) {
				if ( report.equals("1") && reportType.equals("1")) { // DCI List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
					
				} else if ( report.equals("1") && reportType.equals("2")) {
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("1") &&  reportType.equals("3")) {
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=7; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=7; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("1") &&  reportType.equals("4")) {
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("1") && reportType.equals("5")) {
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(13).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(13).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("1") &&  reportType.equals("6")) { 
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(0, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
					
				} else if ( report.equals("1") &&  reportType.equals("7")) {
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("1") && reportType.equals("8")) {
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					} 
				} else if ( report.equals("1") && reportType.equals("9")) { 
					
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
						}
						bChart=true;
					} 
				} else if ( report.equals("1") && reportType.equals("10")) { 
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
						}
						bChart=true;
					} 
				} else if ( report.equals("1") && reportType.equals("11")) { 
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("1") && reportType.equals("12")) { 
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(7).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(7).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(7).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(7).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("1") && reportType.equals("13")) { 
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(7).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(7).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(7).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(7).setCellStyle(right_12_bold);
						}
					}
				}
			}else if(report.equals("2")) {
				if ( report.equals("2") && reportType.equals("1")) { // PCI List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("2") && reportType.equals("2")) { // PCI Detail List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("2") &&  reportType.equals("3")) { // PCI Behind List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("2") &&  reportType.equals("5")) { // DCI Workdone List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("2") && reportType.equals("9")) { // WBS Detail
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}
					}
				}
			}else if(report.equals("3")) {
				if ( report.equals("3") && reportType.equals("1")) { // VDCI List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("3") && reportType.equals("2")) { // VDCI Detail List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("3") &&  reportType.equals("3")) { // VDCI History List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=7; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=7; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("3") &&  reportType.equals("4")) { // VDCI Behind List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("3") && reportType.equals("5")) { // VDCI Transmittal List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(13).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(13).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("3") &&  reportType.equals("6")) { // VDCI Progress Status
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(10).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(10).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("3") &&  reportType.equals("7")) { // VDCI Workdone List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("3") &&  reportType.equals("8")) { // VDCI Workdone Status
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("3") &&  reportType.equals("9")) { // VDCI Weekly Progress Graph
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
						}
						 bChart=true;
					}
				} else if ( report.equals("3") &&  reportType.equals("10")) { // VDCI Monthly Progress Graph
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
						}
						 bChart=true;
					}
				} else if ( report.equals("3") && reportType.equals("11")) { // WBS Detail
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}
					}
				}
			}else if(report.equals("4")) {
				if ( report.equals("4") && reportType.equals("1")) { // SDCI List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("4") && reportType.equals("2")) { // SDCI Detail List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("4") &&  reportType.equals("3")) { // SDCI History List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=7; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=7; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
						}
					}
				} else if ( report.equals("4") &&  reportType.equals("4")) { // SDCI Behind List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("4") && reportType.equals("5")) { // SDCI Transmittal List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(13).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(13).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(13).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("4") &&  reportType.equals("6")) { // SDCI Progress Status
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				}  else if ( report.equals("4") &&  reportType.equals("7")) { // SDCI Workdone List
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(4).setCellValue("Work Step : "+workStep);
							sheet2.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(4).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=5; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(11).setCellValue("Duration : "+date_s +"~"+date_d);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(11).setCellStyle(right_12_bold);
						}
					}
				} else if ( report.equals("4") &&  reportType.equals("9")) { // VDCI Weekly Progress Graph
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(19).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(19).setCellStyle(right_12_bold);
						}
						 bChart=true;
					}
				} else if ( report.equals("4") &&  reportType.equals("10")) { // VDCI Monthly Progress Graph
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=17; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+discipline);
							sheet.getRow(3).createCell(27).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(27).setCellStyle(right_12_bold);
						}
						 bChart=true;
					}
				} else if ( report.equals("4") && reportType.equals("11")) { // WBS Detail
					for(int i=0;i<disciplineList2.size();i++) {
						if(i == 0) {
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}else if(i > 0) {
							XSSFSheet sheet2 = workbook.cloneSheet(0);
							workbook.setSheetName(i, disciplineList2.get(i));
							startRow=6; 
							sheet2.getRow(2).createCell(0).setCellValue("Project : "+projecetName);
							sheet2.getRow(3).createCell(0).setCellValue("Discipline : "+disciplineList2.get(i));
							sheet2.getRow(3).createCell(5).setCellValue("Cut-off date : "+cut_off_date);
							
							sheet2.getRow(2).getCell(0).setCellStyle(left_14_bold);
							sheet2.getRow(3).getCell(0).setCellStyle(left_12_bold);
							sheet2.getRow(3).getCell(5).setCellStyle(right_12_bold);
						}
					}
				}
			}
			
			String tmpColumnList[] = columnList.split("###");
			
			for ( int i=0; i<tmpColumnList.length; ++i ) { //타이틀 부분
				String cloumn = tmpColumnList[i];

				if ( sheet.getRow(startRow-1) == null ) { sheet.createRow(startRow-1);}
				
				sheet.getRow(startRow-1).createCell(i).setCellValue(cloumn);
				sheet.getRow(startRow-1).getCell(i).setCellStyle(center_11_bold);
			}
			
			
			
			
			
			int sheetPos=0;
			int firstStartRow=0;
			String keyTmp="";
			int posTmp=0;
			int serialNo=0;
			
			
			for ( int i=0; i<valueList.length; ++i) {
				String tmpString = valueList[i];
				String tmp[] = tmpString.split("@@@");
				
				if ( firstStartRow == 0 ) { firstStartRow=startRow; } 
				
				
				for(int j=sheetPos;j<disciplineList2.size();++j) {
					String key = disciplineList2.get(j);
					int idx= tmpString.indexOf(key);
					
					keyTmp=key;
					if ( idx > -1 ) {
						break;
					} else {
						startRow=firstStartRow; 
						serialNo = 0;
						sheetPos= j+1;
						sheet = workbook.getSheetAt(sheetPos);
						posTmp=0;
						
						for ( int m=0; m<tmpColumnList.length; ++m ) {
							String cloumn = tmpColumnList[m];
							
							
							sheet.getRow(startRow-1).createCell(m).setCellValue(cloumn);
							sheet.getRow(startRow-1).getCell(m).setCellStyle(center_11_bold);
							if ( sheet.getRow(startRow-1) == null ) { sheet.createRow(startRow-1);}
						}
						
					}
				}
				
				//System.out.println("keyTmp  : "+keyTmp+", DRAW POS : " + sheetPos +", startRow : " +startRow);
				
				
				int maxK=0;
				int maxCnt=0;
				
				for ( int j=0; j< tmp.length; ++j ) {
					String tmpT = tmp[j];
					if(!tmpColumnList[0].equals("")) {
						if(tmpColumnList[j].equals("SerialNo") || tmpColumnList[j].equals("Serial No") || tmpColumnList[j].equals("Serial No.") || tmpColumnList[j].equals("Serial.No")) {
							serialNo += 0;
							tmpT = Integer.toString(serialNo);
						}
					}else if(tmpColumnList[0].equals("") && reportType.equals("3") && !report.equals("2") && j == 0) {
						serialNo += 0;
						tmpT = Integer.toString(serialNo);
					}
					
					if ( tmpT == null || tmpT.equals("null")) tmpT="";
					
					String tmptT[] = tmpT.split("@,@");
					if ( maxCnt <=  tmptT.length ) { maxCnt =  tmptT.length; } 
				}
								
				for ( int j=0; j< tmp.length; ++j ) {
					String tmpT = tmp[j];
					
					if(!tmpColumnList[0].equals("")) {
						if(tmpColumnList[j].equals("SerialNo") || tmpColumnList[j].equals("Serial No") || tmpColumnList[j].equals("Serial No.") || tmpColumnList[j].equals("Serial.No")) {
							serialNo += 1;
							tmpT = Integer.toString(serialNo);
						}
					}else if(tmpColumnList[0].equals("") && reportType.equals("3") && !report.equals("2") && j == 0) {
						serialNo += 1;
						tmpT = Integer.toString(serialNo);
					}
					
					if ( tmpT == null || tmpT.equals("null")) tmpT="";
					
					String tmptT[] = tmpT.split("@,@");
					
					if ( tmptT.length < maxCnt ) { // 셀 병합
						sheet.addMergedRegion(new CellRangeAddress(startRow+posTmp, (startRow+posTmp+maxCnt-1 ), j, j));
					}
					
					if ( maxK <= tmptT.length-1 ) maxK = tmptT.length-1;
					
					for ( int k=0; k<tmptT.length; ++k ) { // 셀 스타일 적용
						
						if ( sheet.getRow(startRow+posTmp+k) == null ) {
							sheet.createRow(startRow+posTmp+k);
						}
						
						sheet.getRow(startRow+posTmp+k).createCell(j).setCellValue(tmptT[k]);
						sheet.getRow(startRow+posTmp+k).getCell(j).setCellStyle(center_10);
					}
				}
				
				startRow = startRow + maxK;
				++posTmp;
			}
			
			if ( bChart ) {
				sheet = setChartPos(sheet,startRow-1,reportType,sheetName, disciplineList.length);
			}

			response.reset();
			response.setHeader("Set-Cookie", "fileDownload=true; path=/"); 
		    response.setHeader("Content-Disposition", String.format("attachment; filename=\""+fileName+"\"")); 
		    workbook.write(response.getOutputStream());
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			fis.close();
		}

        System.out.println(fileName + " written successfully");
    }
	
	public static XSSFSheet setChartPos (XSSFSheet sheet, int pos, String reportType, String sheetName, int discipLength) {
		//pos += discipLength;
		//pos = endPoint - 2;
		
		try {
			XSSFDrawing drawing = ((XSSFSheet)sheet).createDrawingPatriarch();
			List<XSSFChart> chartList = drawing.getCharts();

			for ( int i=0; i<chartList.size(); ++i ) {
				XSSFChart   chart = chartList.get(i);
				CTChart cc = chart.getCTChart();
				CTPlotArea ctPlotArea = cc.getPlotArea();
				
				CTLineChart chartListTmp=ctPlotArea.getLineChartArray(0);
				
				List<CTLineSer> ctLineSerList = chartListTmp.getSerList();
				
				String posString="";
				if ( reportType.equals("9") ) { posString=":$T$";};
				if ( reportType.equals("10") ) { posString=":$AB$";};
				
				for ( int j=0; j<ctLineSerList.size(); ++j ) {
					sheet.getRow(pos);
					CTLineSer tmp = ctLineSerList.get(j);
					
					tmp.getTx().getStrRef().setF(sheetName+"!$E$"+(pos+1));
					tmp.getVal().getNumRef().setF(sheetName+"!$F$"+(pos+1)+posString+(pos+1));
					
					ctLineSerList.set(j, tmp);
					System.out.printf("[%d] : %s : [%s]\n",j,ctLineSerList.get(j).getTx().getStrRef().getF(),ctLineSerList.get(j).getVal().getNumRef().getF());
					
					pos++;
					
				}
				
				ctPlotArea.setLineChartArray(0, chartListTmp);
				chartListTmp=ctPlotArea.getLineChartArray(0);
				
				ctLineSerList = chartListTmp.getSerList();
				
				for ( int j=0; j<ctLineSerList.size(); ++j ) {
					sheet.getRow(pos);
					CTLineSer tmp = ctLineSerList.get(j);
					
					ctLineSerList.set(j, tmp);
					System.out.printf("[%d] : %s : [%s]\n",j,ctLineSerList.get(j).getTx().getStrRef().getF(),ctLineSerList.get(j).getVal().getNumRef().getF());
				}
				
				System.out.println("DEBUG");
				
				return sheet;
			}
			
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
//	private void copySheet(HSSFSheet from, HSSFSheet to) {
//        HSSFRow firstRow = from.getRow(0);
//        HSSFRow secondRow = from.getRow(1);
//        HSSFRow thirdRow = from.getRow(2);
//        
//        HSSFRow firstRow2 = to.createRow(0);
//        HSSFRow secondRow2 = to.createRow(1);
//        HSSFRow thirdRow2 = to.createRow(2);
//        
//        Iterator<Cell> iterator = firstRow.cellIterator();
//        short col = 0;
//        while(iterator.hasNext())
//            addCell(firstRow2, col++, iterator.next().getStringCellValue());
//        
//        col = 0;
//        iterator = secondRow.cellIterator();
//        while(iterator.hasNext())
//            addCell(secondRow2, col++, iterator.next().getStringCellValue());
//
//        col = 0;
//        iterator = thirdRow.cellIterator();
//        while(iterator.hasNext())
//            addCell(thirdRow2, col++, iterator.next().getStringCellValue());
//    }
//
//	private void addCell(HSSFRow firstRow2, short s, String stringCellValue) {
//		// TODO Auto-generated method stub
//		
//	}
	
	
	
	
}
