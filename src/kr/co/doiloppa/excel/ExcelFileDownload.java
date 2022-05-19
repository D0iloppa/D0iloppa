package kr.co.doiloppa.excel;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.Var;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.DrnLineVO;
import kr.co.doiloppa.model.GlobalSearchVO;
import kr.co.doiloppa.model.PrjCodeSettingsVO;
import kr.co.doiloppa.model.PrjDocumentIndexVO;
import kr.co.doiloppa.model.PrjEmailTypeVO;
import kr.co.doiloppa.model.PrjGroupMemberVO;
import kr.co.doiloppa.model.PrjMemberVO;
import kr.co.doiloppa.model.PrjTrVO;
import kr.co.doiloppa.model.SecurityLogVO;
import kr.co.doiloppa.model.WbsCodeControlVO;

 
public class ExcelFileDownload {
	

    // json 데이터로 응답을 보내기 위한 
    @Autowired
    MappingJackson2JsonView jsonView;

		
/**
	 * 1. 메소드명 : wbsExcelDownload
	 * 2. 작성일: 2021. 12. 28.
	 * 3. 작성자: doil
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: doil
 */
	public static void wbsExcelDownload(HttpServletResponse res, String titleList, List<WbsCodeControlVO> dataList, String excelName, String prj_Name) throws Exception {
		
		
		Workbook workbook = new XSSFWorkbook();


		Sheet sheet = workbook.createSheet("WBS code template");

		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		Row row = sheet.createRow(rowIndex++);

		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
		
		
		// 타이틀 설정
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("WBS Code Multi Register");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);
		
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);

			
		Cell dateCell = row.createCell(4);
		dateCell.setCellValue("Date : " + formatedNow );
		// 공란 입력에 따른, null point exception 방지
		row = sheet.createRow(rowIndex++);
		Cell nullCell = row.createCell(0);
		nullCell.setCellValue(" ");
		
		
		
		// 헤드 필드 설정
		row = sheet.createRow(rowIndex++);
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("WBS Code");
		cell0.setCellStyle(styleBackground);

		Cell cell1 = row.createCell(1);
		cell1.setCellValue("Description");
		cell1.setCellStyle(styleBackground);

		Cell cell2 = row.createCell(2);
		cell2.setCellValue("WgtVal.");
		cell2.setCellStyle(styleBackground);

		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Type");
		cell3.setCellStyle(styleBackground);

		Cell cell4 = row.createCell(4);
		cell4.setCellValue("Discipline");
		cell4.setCellStyle(styleBackground);

		if (dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++){
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
					System.out.println(dataList.get(i).getWbs_code() + " / " + dataList.get(i).getWbs_desc() + " / " + dataList.get(i).getWgtval() + " / " + dataList.get(i).getDoc_type() + " / " + dataList.get(i).getDiscip_display());
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getWbs_code());
					cell01.setCellStyle(styleCenter);

					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getWbs_desc());
					cell11.setCellStyle(styleCenter);

					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getWgtval());
					cell21.setCellStyle(styleCenter);

					Cell cell31 = row.createCell(3);
					cell31.setCellValue(dataList.get(i).getDoc_type());
					cell31.setCellStyle(styleCenter);

					Cell cell41 = row.createCell(4);
					cell41.setCellValue(dataList.get(i).getDiscip_display());
					cell41.setCellStyle(styleCenter);					
				}				
				
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
			
		}

		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"" + (URLEncoder.encode((excelName+".xlsx"),"UTF-8")) + "\""));
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
		System.out.println(excelName + " written successfully");
		return ;
	}
	
	
	/**
	 * 1. 메소드명 : dciIndexRegisterToExcel
	 * 2. 작성일: 2021. 12. 28.
	 * 3. 작성자: doil
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: doil
 */
	public static void dciIndexRegisterToExcel(HttpServletResponse res, List<String> fieldList, List<PrjDocumentIndexVO> dataList, String excelName, String[] prjInfo , String doc_type) throws Exception {
		
		Workbook workbook = new XSSFWorkbook();

		int[] mustInputCol = {0,1,2,3,5,6};
		int[] colWidth = {4200,7200,2600,2600,2600,1500,3000,3000,3000};
		String[] colTitle = {"DOCUMENT NO","TITLE","Doc.Type","CATEGORY","WBSCODE","SIZE","DESIGNER","REMARK","SUPPLYDOCNO"};
		
		if(doc_type.equals("MCI") || doc_type.equals("PCI")) {
			colTitle = new String[] {"DOCUMENT NO","TITLE","WBSCODE","DESIGNER","REMARK","SUPPLYDOCNO"};
			mustInputCol = new int[] {0,1,3};
			colWidth = new int[] {4200,7200,2600,3000,3000,3000};
		}
		
		Sheet sheet = workbook.createSheet("DCI INDEX REGISTER");

		int rowIndex = 0;
		
		Row row = sheet.createRow(rowIndex++);
		// colTilte은 고정, fieldList는 step에 따라 가변 필드 갯수 이므로 두 개의 값을 각각 더하여
		// 총 필드의 갯수를 구한다.
		
		
		int fieldListSize = 0;
		if(fieldList != null) fieldListSize = fieldList.size();
		
		for(int i=0;i < colTitle.length + fieldListSize ; i++)
			sheet.setColumnWidth(i, i < colWidth.length ? colWidth[i] : 2800);
		
		
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
		
		
		// 타이틀 설정
		Cell titleCell = row.createCell(1);
		titleCell.setCellValue("Engineering Document");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		
		// Discipline
		row = sheet.createRow(rowIndex++);
		Cell[] discipCell = new Cell[2];
		for(int i=0;i<discipCell.length;i++) discipCell[i] = row.createCell(i);
		discipCell[0].setCellValue("Discipline");
		discipCell[1].setCellValue(prjInfo[0]);

		
		// 프로젝트명
		row = sheet.createRow(rowIndex++);
		Cell[] projectCell = new Cell[3];
		for(int i=0;i<projectCell.length;i++) projectCell[i] = row.createCell(i<2?i:5);
		projectCell[0].setCellValue("Project");
		projectCell[1].setCellValue(prjInfo[1]);
		
		
		
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);

			
		Cell dateCell = row.createCell(4);
		dateCell.setCellValue("Date : " + formatedNow );
		row = sheet.createRow(rowIndex++);
		Cell nullCell = row.createCell(0);
		// 공란 입력에 따른, null point exception 방지
		nullCell.setCellValue(" ");
		
		// 헤드 필드 설정
		row = sheet.createRow(rowIndex++);
		Cell[] cellHead = new Cell[colTitle.length + fieldList.size()];
		for(int i=0;i<colTitle.length + fieldList.size();i++) {
			cellHead[i] = row.createCell(i);
			// 고정필드
			if(i<colTitle.length)
				cellHead[i].setCellValue(colTitle[i]);
			// 가변필드
			else {
				cellHead[i].setCellValue(fieldList.get(i - colTitle.length));
				cellHead[i].setCellStyle(styleBackground);
			}
		}
		// 필드 생성 후, cellStyle 후처리
		for(int idx : mustInputCol)
			cellHead[idx].setCellStyle(styleBackground);

		
		// 데이터 입력
		if (dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++){
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(rowIndex + i);
					String[] rowData = new String[colTitle.length + fieldList.size()];
					int rowColIdx = 0;
					rowData[rowColIdx++] = dataList.get(i).getDoc_no();
					rowData[rowColIdx++] = dataList.get(i).getTitle();
					rowData[rowColIdx++] = dataList.get(i).getDoc_type();
					rowData[rowColIdx++] = dataList.get(i).getCategory_cd();
					rowData[rowColIdx++] = dataList.get(i).getWbs_code();
					rowData[rowColIdx++] = dataList.get(i).getSize_code();
					rowData[rowColIdx++] = dataList.get(i).getDesigner_id();
					rowData[rowColIdx++] = dataList.get(i).getRemark();
					rowData[rowColIdx++] = dataList.get(i).getDoc_no();
					// 가변필드값 데이터 입력
					List<String> stepDate = dataList.get(i).getStepData();
					for(String date : stepDate) 
						rowData[rowColIdx++] = (date != null ? date : "");
					
							
					
					Cell[] dataCells = new Cell[colTitle.length + fieldList.size()];
					for(int col=0; col<dataCells.length ;col++) {
						dataCells[col] = row.createCell(col);
						dataCells[col].setCellValue(rowData[col]);
						dataCells[col].setCellStyle(styleCenter);
					}
				}				
				
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
		} 

		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"" + (URLEncoder.encode((excelName+".xlsx"),"UTF-8")) + "\""));
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
		System.out.println(excelName + " written successfully");
		return ;
	}

	/**
	 * 1. 메소드명 : pg04ExcelDownload
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */

	public static void pg04ExcelDownload(HttpServletResponse res, String titleList, List<PrjCodeSettingsVO> dataList, String excelName, String prj_Name) throws Exception {
		
//		System.out.println(dataList.get(0).getSet_code_type());
		
		// 선택된 코드 타입
		String chkCode = dataList.get(0).getSet_code_type();
		
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = null;
		
		if(chkCode.equals("DOC.SIZE") || chkCode.equals("DOC.CATEGORY") || chkCode.equals("DOC.TYPE") || chkCode.equals("VENDOR DOC.SIZE") || chkCode.equals("VTR ISSUE PURPOSE")) {
//			System.out.println("넘길 데이터 2개");
			
			if(chkCode.equals("DOC.SIZE")) {
				sheet = workbook.createSheet("설계문서 Size Code"); //시트명
			}else if(chkCode.equals("DOC.CATEGORY")) {
				sheet = workbook.createSheet("설계문서 Category Code"); //시트명
			}else if(chkCode.equals("DOC.TYPE")) {
				sheet = workbook.createSheet("서신 Type Code"); //시트명
			}else if(chkCode.equals("VENDOR DOC.SIZE")) {
				sheet = workbook.createSheet("Vendor문서 Size Code"); //시트명
			}else if(chkCode.equals("VTR ISSUE PURPOSE")) {
				sheet = workbook.createSheet("VTR Issue Purpose Code"); //시트명
			}
			
			int rowIndex = 0;
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
			Row row = sheet.createRow(rowIndex++);
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			
			CellStyle styleCenter = workbook.createCellStyle();
			styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
			styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

			CellStyle styleLeft = workbook.createCellStyle();
			styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
			styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

			CellStyle styleRight = workbook.createCellStyle();
			styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
			styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
			
			CellStyle titleStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setFontHeight((short)300);
			headerFont.setBoldweight((short)600);
			titleStyle.setFont(headerFont);
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
			
			
			CellStyle styleBackground = workbook.createCellStyle();
			styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
			styleBackground.setAlignment(CellStyle.ALIGN_CENTER);		
			
			if(chkCode.equals("DOC.SIZE")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("설계문서 Size Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("DOC.CATEGORY")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("설계문서 Category Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("DOC.TYPE")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("서신 Type Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("VENDOR DOC.SIZE")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("Vendor문서 Size Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("VTR ISSUE PURPOSE")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("VTR Issue Purpose Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}
			
			// 프로젝트명
			Cell projectCell = row.createCell(0);
			projectCell.setCellValue("Project : " + prj_Name );
			row = sheet.createRow(rowIndex++);		
			
			// Date
			LocalDate now = LocalDate.now();  
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			String formatedNow = now.format(formatter);		
			
			Cell dateCell = row.createCell(1);
			dateCell.setCellValue("Date : " + formatedNow );
			dateCell.setCellStyle(styleRight);
			row = sheet.createRow(rowIndex++);
			row = sheet.createRow(rowIndex++);		
			
			// 헤드 필드 설정
			Cell cell0 = row.createCell(0);
			cell0.setCellValue("Code");
			cell0.setCellStyle(styleBackground);

			Cell cell1 = row.createCell(1);
			cell1.setCellValue("Description");
			cell1.setCellStyle(styleBackground);		
			
			if(dataList != null) {
				try {
					for(int i=0; i<dataList.size(); i++) {
//						sheet.autoSizeColumn(i);
						row = sheet.createRow(i+rowIndex);
//						System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
						Cell cell01 = row.createCell(0);
						cell01.setCellValue(dataList.get(i).getSet_code());
						cell01.setCellStyle(styleCenter);
						
						Cell cell11 = row.createCell(1);
						cell11.setCellValue(dataList.get(i).getSet_desc());
						cell11.setCellStyle(styleCenter);
					
					}
				}catch(Exception e) {
					//System.out.println(e.getMessage());
					return;
				}
			}
			
		}else {
//			System.out.println("넘길 데이터 3개");
			
			if(chkCode.equals("VENDOR DOC.CATEGORY")) {
				sheet = workbook.createSheet("Vendor문서 Category Code"); //시트명
			}else if(chkCode.equals("TR ISSUE PURPOSE")) {
				sheet = workbook.createSheet("TR Issue Purpose Code"); //시트명
			}else if(chkCode.equals("TR RETURN STATUS")) {
				sheet = workbook.createSheet("TR Return Status Code"); //시트명
			}else if(chkCode.equals("VTR ISSUE RETURN STATUS")) {
				sheet = workbook.createSheet("VTR Issue Return Status Code"); //시트명
			}else if(chkCode.equals("STR ISSUE PURPOSE")) {
				sheet = workbook.createSheet("STR Issue Purpose Code"); //시트명
			}else if(chkCode.equals("REVISION NUMBER")) {
				sheet = workbook.createSheet("REVISON Number Code"); //시트명
			}
			
			int rowIndex = 0;
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			Row row = sheet.createRow(rowIndex++);
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			
			CellStyle styleCenter = workbook.createCellStyle();
			styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
			styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

			CellStyle styleLeft = workbook.createCellStyle();
			styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
			styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

			CellStyle styleRight = workbook.createCellStyle();
			styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
			styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
			
			CellStyle titleStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setFontHeight((short)300);
			headerFont.setBoldweight((short)600);
			titleStyle.setFont(headerFont);
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
			
			
			CellStyle styleBackground = workbook.createCellStyle();
			styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
			styleBackground.setAlignment(CellStyle.ALIGN_CENTER);		
			
			if(chkCode.equals("VENDOR DOC.CATEGORY")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("VENDOR 문서 Category Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("TR ISSUE PURPOSE")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("TR Issue Purpose Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("TR RETURN STATUS")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("TR Return Status Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("VTR ISSUE RETURN STATUS")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("VTR Issue Return Status Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("STR ISSUE PURPOSE")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("STR Issue Purpose Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}else if(chkCode.equals("REVISION NUMBER")) {
				// 타이틀 설정
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("REVISION Number Code");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
			}
			
			// 프로젝트명
			Cell projectCell = row.createCell(0);
			projectCell.setCellValue("Project : " + prj_Name );
			row = sheet.createRow(rowIndex++);		
			
			// Date
			LocalDate now = LocalDate.now();  
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
			String formatedNow = now.format(formatter);		
			
			Cell dateCell = row.createCell(2);
			dateCell.setCellValue("Date : " + formatedNow );
			dateCell.setCellStyle(styleRight);
			row = sheet.createRow(rowIndex++);
			row = sheet.createRow(rowIndex++);		
			
			// 헤드 필드 설정
			Cell cell0 = row.createCell(0);
			cell0.setCellValue("Code");
			cell0.setCellStyle(styleBackground);

			Cell cell1 = row.createCell(1);
			cell1.setCellValue("Description");
			cell1.setCellStyle(styleBackground);
			
			if(chkCode.equals("VENDOR DOC.CATEGORY") || chkCode.equals("TR RETURN STATUS") || chkCode.equals("VTR ISSUE RETURN STATUS")) {
				Cell cell2 = row.createCell(2);
				cell2.setCellValue("Step");
				cell2.setCellStyle(styleBackground);
			}else if(chkCode.equals("TR ISSUE PURPOSE")) {
				Cell cell2 = row.createCell(2);
				cell2.setCellValue("Forecast Date");
				cell2.setCellStyle(styleBackground);
			}else if(chkCode.equals("STR ISSUE PURPOSE")) {
				Cell cell2 = row.createCell(2);
				cell2.setCellValue("Revision Type");
				cell2.setCellStyle(styleBackground);
			}else if(chkCode.equals("REVISION NUMBER")) {
				Cell cell2 = row.createCell(2);
				cell2.setCellValue("Code Val");
				cell2.setCellStyle(styleBackground);
			}
			
			if(dataList != null) {
				try {
					for(int i=0; i<dataList.size(); i++) {
//						sheet.autoSizeColumn(i);
						row = sheet.createRow(i+rowIndex);
//						System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc() +"/" + dataList.get(i).getSet_val());
						Cell cell01 = row.createCell(0);
						cell01.setCellValue(dataList.get(i).getSet_code());
						cell01.setCellStyle(styleCenter);
						
						Cell cell11 = row.createCell(1);
						cell11.setCellValue(dataList.get(i).getSet_desc());
						cell11.setCellStyle(styleCenter);
						
						Cell cell21 = row.createCell(2);
						cell21.setCellValue(dataList.get(i).getSet_val());
						cell21.setCellStyle(styleCenter);
					
					}
				}catch(Exception e) {
					//System.out.println(e.getMessage());
					return;
				}
			}
			
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
		System.out.println(excelName + " written successfully");
		return ;

	}
	
	/**
	 * 1. 메소드명 : pg05ExcelDownload
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	public static void pg05ExcelDownload(HttpServletResponse res, String titleList, List<PrjCodeSettingsVO> dataList, String excelName, String prj_Name) throws Exception {
		
		// 선택된 코드 타입
//		String chkCode = dataList.get(0).getSet_code_type();
		
		Workbook workbook = new XSSFWorkbook();		
		
		Sheet sheet = null;
		
		sheet = workbook.createSheet("Correspondence Responsibility Code"); //시트명
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
		
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);			
		
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("Correspondence Responsibility Code");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);		
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);		
		
		Cell dateCell = row.createCell(1);
		dateCell.setCellValue("Date : " + formatedNow );
		dateCell.setCellStyle(styleRight);
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);		
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("Code");
		cell0.setCellStyle(styleBackground);

		Cell cell1 = row.createCell(1);
		cell1.setCellValue("Description");
		cell1.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++) {
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
//					System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getSet_code());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getSet_desc());
					cell11.setCellStyle(styleCenter);
				
				}
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
		System.out.println(excelName + " written successfully");
		return ;
		
	}
	
	/**
	 * 1. 메소드명 : pg06ExcelDownload
	 * 2. 작성일: 2022. 01. 02.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	public static void pg06ExcelDownload(HttpServletResponse res, String titleList, List<PrjCodeSettingsVO> dataList, String excelName, String prj_Name) throws Exception {
//		System.out.println(dataList.get(0).getSet_code_type());
		
		// 선택된 코드 타입
		String chkCodeT = dataList.get(0).getSet_code_type();
		
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = null;
		
		if(chkCodeT.equals("ENGINEERING STEP")) {
			sheet = workbook.createSheet("ENG Step Code");
		}else if(chkCodeT.equals("VENDOR DOC STEP")) {
			sheet = workbook.createSheet("VDR Step Code");
		}else if(chkCodeT.equals("SITE DOC STEP")) {
			sheet = workbook.createSheet("SDC Step Code");
		}else if(chkCodeT.equals("PROCUREMENT STEP")) {
			sheet = workbook.createSheet("PRO Step Code");
		}
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 12000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 10000);

		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);	
		
		if(chkCodeT.equals("ENGINEERING STEP")) {
			// 타이틀 설정
			Cell titleCell = row.createCell(0);
			titleCell.setCellValue("ENG Step Code");
			titleCell.setCellStyle(styleCenter);
			titleCell.setCellStyle(titleStyle);
			row = sheet.createRow(rowIndex++);
		}else if(chkCodeT.equals("VENDOR DOC STEP")) {
			// 타이틀 설정
			Cell titleCell = row.createCell(0);
			titleCell.setCellValue("VDR Step Code");
			titleCell.setCellStyle(styleCenter);
			titleCell.setCellStyle(titleStyle);
			row = sheet.createRow(rowIndex++);
		}else if(chkCodeT.equals("SITE DOC STEP")) {
			// 타이틀 설정
			Cell titleCell = row.createCell(0);
			titleCell.setCellValue("SDC Step Code");
			titleCell.setCellStyle(styleCenter);
			titleCell.setCellStyle(titleStyle);
			row = sheet.createRow(rowIndex++);
		}else if(chkCodeT.equals("PROCUREMENT STEP")) {
			// 타이틀 설정
			Cell titleCell = row.createCell(0);
			titleCell.setCellValue("PRO Step Code");
			titleCell.setCellStyle(styleCenter);
			titleCell.setCellStyle(titleStyle);
			row = sheet.createRow(rowIndex++);
		}
		
		// 프로젝트 명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : "+prj_Name);
		row = sheet.createRow(rowIndex++);
		
		// Date
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formatedNow = now.format(formatter);
		
		Cell dateCell = row.createCell(4);
		dateCell.setCellValue("Date : " + formatedNow );
		dateCell.setCellStyle(styleRight);
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("STEP");
		cell0.setCellStyle(styleBackground);

		Cell cell1 = row.createCell(1);
		cell1.setCellValue("IFC");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("Desc");
		cell2.setCellStyle(styleBackground);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Progress Rate(%)");
		cell3.setCellStyle(styleBackground);
		
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("Actual DATE (자동입력설정)");
		cell4.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++) {
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
//					System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
					
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getSet_code());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getIfc_yn());
					cell11.setCellStyle(styleCenter);
					
					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getSet_desc());
					cell21.setCellStyle(styleCenter);
					
					Cell cell31 = row.createCell(3);
					cell31.setCellValue(dataList.get(i).getSet_val());
					cell31.setCellStyle(styleCenter);
					
					Cell cell41 = row.createCell(4);
					cell41.setCellValue(dataList.get(i).getSet_val2());
					cell41.setCellStyle(styleCenter);
				
				}
				
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
				
//		LocalDateTime nowDate = LocalDateTime.now();
//		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//		String result = nowDate.format(dateFormat);
//		System.out.println("시간 : "+result);
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
//		System.out.println(excelName + " written successfully");
		
		return;
	}
	
	/**
	 * 1. 메소드명 : pg06IFCExcelDownload
	 * 2. 작성일: 2022. 01. 02.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */	
	public static void pg06IFCExcelDownload(HttpServletResponse res, String titleList, List<PrjCodeSettingsVO> dataList, String excelName, String prj_Name) throws Exception {
				
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("IFC Revised Reason Code"); //시트명
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
		
		// 타이틀 설정
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("IFC Revised Reason Code");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);		
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);		
		
		Cell dateCell = row.createCell(1);
		dateCell.setCellValue("Date : " + formatedNow );
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("CODE");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("Description");
		cell1.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++) {
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
//					System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getSet_code());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getSet_desc());
					cell11.setCellStyle(styleCenter);
				
				}
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
//		System.out.println(excelName + " written successfully");
		
		return;
	}
	
	/**
	 * 1. 메소드명 : pg08prUserExcelDownload
	 * 2. 작성일: 2022. 01. 02.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */	
	public static void pg08prUserExcelDownload(HttpServletResponse res, String titleList, List<PrjMemberVO> dataList, String excelName, String prj_Name) throws Exception {
		
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Project Member List"); // 시트명
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
		
		// 타이틀 설정
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("Project Member List");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);	
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);
		
		Cell dateCell = row.createCell(6);
		dateCell.setCellValue("Date : " + formatedNow );
		dateCell.setCellStyle(styleRight);
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);		
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("DCC");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("USER ID");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("KOR.NAME");
		cell2.setCellStyle(styleBackground);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("GROUP");
		cell3.setCellStyle(styleBackground);
		
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("Eng.Name");
		cell4.setCellStyle(styleBackground);
		
		Cell cell5 = row.createCell(5);
		cell5.setCellValue("User type");
		cell5.setCellStyle(styleBackground);
		
		Cell cell6 = row.createCell(6);
		cell6.setCellValue("company");
		cell6.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0;i<dataList.size();i++) {
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
//					System.out.println(dataList.get(i).getDcc_yn() +"/" + dataList.get(i).getUser_id() +"/"+ dataList.get(i).getUser_kor_nm() +"/"+ 
//					dataList.get(i).getUser_group_nm() +"/"+ dataList.get(i).getUser_eng_nm() +"/"+ dataList.get(i).getUser_type() +"/"+ dataList.get(i).getCompany_nm());					
				
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getDcc_yn());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getUser_id());
					cell11.setCellStyle(styleCenter);
					
					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getUser_kor_nm());
					cell21.setCellStyle(styleCenter);
					
					Cell cell31 = row.createCell(3);
					cell31.setCellValue(dataList.get(i).getUser_group_nm());
					cell31.setCellStyle(styleCenter);
					
					Cell cell41 = row.createCell(4);
					cell41.setCellValue(dataList.get(i).getUser_eng_nm());
					cell41.setCellStyle(styleCenter);
					
					Cell cell51 = row.createCell(5);
					cell51.setCellValue(dataList.get(i).getUser_type());
					cell51.setCellStyle(styleCenter);
					
					Cell cell61 = row.createCell(6);
					cell61.setCellValue(dataList.get(i).getCompany_nm());
					cell61.setCellStyle(styleCenter);
				}
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
//		System.out.println(excelName + " written successfully");
		
		return;
	}
	
	/**
	 * 1. 메소드명 : pg08prGroupExcelDownload
	 * 2. 작성일: 2022. 01. 02.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	
	public static void pg08prGroupExcelDownload(HttpServletResponse res, String titleList, List<PrjGroupMemberVO> dataList, String excelName, String prj_Name) throws Exception {
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Project Group Member List"); // 시트명
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);		
		
		// 타이틀 설정
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("Project Group Member List");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);	
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);
		
		Cell dateCell = row.createCell(5);
		dateCell.setCellValue("Date : " + formatedNow );
		dateCell.setCellStyle(styleRight);
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);
		
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("USER ID");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("KOR.NAME");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("GROUP");
		cell2.setCellStyle(styleBackground);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Eng.Name");
		cell3.setCellStyle(styleBackground);
		
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("User type");
		cell4.setCellStyle(styleBackground);
		
		Cell cell5 = row.createCell(5);
		cell5.setCellValue("company");
		cell5.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0;i<dataList.size();i++) {
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
					
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getUser_id());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getUser_kor_nm());
					cell11.setCellStyle(styleCenter);
					
					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getUser_group_nm());
					cell21.setCellStyle(styleCenter);
					
					Cell cell31 = row.createCell(3);
					cell31.setCellValue(dataList.get(i).getUser_eng_nm());
					cell31.setCellStyle(styleCenter);
					
					Cell cell41 = row.createCell(4);
					cell41.setCellValue(dataList.get(i).getUser_type());
					cell41.setCellStyle(styleCenter);
					
					Cell cell51 = row.createCell(5);
					cell51.setCellValue(dataList.get(i).getCompany_nm());
					cell51.setCellStyle(styleCenter);
				}
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
//		System.out.println(excelName + " written successfully");
		
		return;
	}
	
	/**
	 * 1. 메소드명 : pg08prgroupEditExcelDownload
	 * 2. 작성일: 2022. 01. 02.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */
	public static void pg08prgroupEditExcelDownload(HttpServletResponse res, String titleList, List<PrjCodeSettingsVO> dataList, String excelName, String prj_Name) throws Exception {
		
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Project Group List");
		
		int rowIndex = 0;
		
//		sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
		
//		// 타이틀 설정
//		Cell titleCell = row.createCell(0);
//		titleCell.setCellValue("Project Group List");
//		titleCell.setCellStyle(styleCenter);
//		titleCell.setCellStyle(titleStyle);
//		row = sheet.createRow(rowIndex++);
//		
//		// 프로젝트명
//		Cell projectCell = row.createCell(0);
//		projectCell.setCellValue("Project : " + prj_Name );
//		row = sheet.createRow(rowIndex++);	
//		
//		// Date
//		LocalDate now = LocalDate.now();  
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
//		String formatedNow = now.format(formatter);
//		
//		Cell dateCell = row.createCell(1);
//		dateCell.setCellValue("Date : " + formatedNow );
//		dateCell.setCellStyle(styleRight);
//		row = sheet.createRow(rowIndex++);
//		row = sheet.createRow(rowIndex++);
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("GROUP NAME");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("GROUP DESCRIPTION");
		cell1.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++) {
					row = sheet.createRow(i+rowIndex);
					
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getSet_code());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getSet_desc());
					cell11.setCellStyle(styleCenter);
				}
				
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
//		System.out.println(excelName + " written successfully");		
		
		return;
		
	}	
	
	/**
	 * 1. 메소드명 : pg10EmailExcelDownload
	 * 2. 작성일: 2022. 01. 02.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */	
	public static void pg10EmailExcelDownload(HttpServletResponse res, String titleList, List<PrjEmailTypeVO> dataList, String excelName, String prj_Name) throws Exception {
		
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = workbook.createSheet("Email Type"); //시트명
		
		int rowIndex = 0;
		
//		sheet.addMergedRegion(new CellRangeAddress(0,0,0,2));
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);		
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);		
		
		
//		// 타이틀 설정
//		Cell titleCell = row.createCell(0);
//		titleCell.setCellValue("Email Type");
//		titleCell.setCellStyle(styleCenter);
//		titleCell.setCellStyle(titleStyle);
//		row = sheet.createRow(rowIndex++);
//		
//		// 프로젝트명
//		Cell projectCell = row.createCell(0);
//		projectCell.setCellValue("Project : " + prj_Name );
//		row = sheet.createRow(rowIndex++);	
//		
//		// Date
//		LocalDate now = LocalDate.now();  
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
//		String formatedNow = now.format(formatter);
//		
//		Cell dateCell = row.createCell(2);
//		dateCell.setCellValue("Date : " + formatedNow );
//		dateCell.setCellStyle(styleRight);
//		row = sheet.createRow(rowIndex++);
//		row = sheet.createRow(rowIndex++);
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("FEATURE");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("type");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("DESCRIPTION");
		cell2.setCellStyle(styleBackground);
		
		if(dataList != null) {
			try {
				for(int i=0;i<dataList.size();i++) {
					row = sheet.createRow(i+rowIndex);
//					System.out.println(dataList.get(i).getEmail_feature() +"/"+ dataList.get(i).getEmail_type() +"/"+ dataList.get(i).getEmail_desc());
					
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getEmail_feature());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getEmail_type());
					cell11.setCellStyle(styleCenter);
					
					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getEmail_desc());
					cell21.setCellStyle(styleCenter);					
				}
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
		}
		
		String setDate = CommonConst.currentDateAndTime();
		
		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
//		System.out.println(excelName + " written successfully");	
		
		return;
	}	
	
	/**
	 * 1. 메소드명 : DocPathListExcelDownload
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */	
	public static void DocPathListExcelDownload(HttpServletResponse res, String titleList, List<PrjDocumentIndexVO> dataList, String excelName, String prj_Name, String pathName) throws Exception {
		
//		System.out.println(pathName);
		
		Workbook workbook = new XSSFWorkbook();		
		
		Sheet sheet = workbook.createSheet("Documnet List"); //시트명
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));		
		Row row = sheet.createRow(rowIndex++);
		
		// 컬럼 크기 조절 왜 안먹지
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);			
		
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("Documnet List : ("+pathName+")");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);		
		
		Cell dateCell = row.createCell(4);
		dateCell.setCellValue("Date : " + formatedNow );
		dateCell.setCellStyle(styleRight);
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);
		
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("Document No");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("RevNo");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("Title");
		cell2.setCellStyle(styleBackground);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Modified Date");
		cell3.setCellStyle(styleBackground);
		
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("Size");
		cell4.setCellStyle(styleBackground);
		
		if(dataList!=null) {
			try {
				
				for(int i=0;i<dataList.size();i++) {
					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
					
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getDoc_no());
					cell01.setCellStyle(styleLeft);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getRev_no());
					cell11.setCellStyle(styleCenter);
					
					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getTitle());
					cell21.setCellStyle(styleLeft);
					
					Cell cell31 = row.createCell(3);
//					System.out.println("dataList.get(i).getReg_date():"+dataList.get(i).getReg_date());
					cell31.setCellValue(CommonConst.stringdateTODate(dataList.get(i).getReg_date()));
					cell31.setCellStyle(styleCenter);
					
					Cell cell41 = row.createCell(4);
//					System.out.println("dataList.get(i).getFile_size():"+ dataList.get(i).getFile_size());
					cell41.setCellValue(CommonConst.intByte(dataList.get(i).getFile_size()));
					cell41.setCellStyle(styleRight);
				}
				
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
			
			String setDate = CommonConst.currentDateAndTime();
			
			res.reset();
			res.setContentType("ms-vnd/excel");
			res.setHeader("Set-Cookie", "fileDownload=true; path=/");
			res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
			res.setHeader("Content-Description", "JSP Generated Data"); 
			res.setHeader("Content-Transfer-Encoding", "binary;"); 
			res.setHeader("Pragma", "no-cache;"); 
			res.setHeader("Expires", "-1;");		
			ServletOutputStream sw = res.getOutputStream();
			workbook.write(sw);
			sw.close();
//			System.out.println(excelName + " written successfully");
			
			return;
		}
	}
	
	/**
	 * 1. 메소드명 : DocPathListAllExcelDownload
	 * 2. 작성일: 2021. 12. 30.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
	 */	
	public static void DocPathListAllExcelDownload(HttpServletResponse res, String titleList, List<PrjDocumentIndexVO> dataList, String excelName, String prj_Name, String pathName) throws Exception {
		
//		System.out.println(pathName);
		
		Workbook workbook = new XSSFWorkbook();		
		
		Sheet sheet = workbook.createSheet("Documnet List"); //시트명
		
		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));		
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);			
		
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("Documnet List : ("+pathName+")");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);
		
		// 프로젝트명
		Cell projectCell = row.createCell(0);
		projectCell.setCellValue("Project : " + prj_Name );
		row = sheet.createRow(rowIndex++);
		
		// Date
		LocalDate now = LocalDate.now();  
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		String formatedNow = now.format(formatter);		
		
		Cell dateCell = row.createCell(5);
		dateCell.setCellValue("Date : " + formatedNow );
		dateCell.setCellStyle(styleRight);
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);
		
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("Status");
		cell0.setCellStyle(styleBackground);
		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("Document No");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("RevNo");
		cell2.setCellStyle(styleBackground);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Title");
		cell3.setCellStyle(styleBackground);
		
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("Modified Date");
		cell4.setCellStyle(styleBackground);
		
		Cell cell5 = row.createCell(5);
		cell5.setCellValue("Size");
		cell5.setCellStyle(styleBackground);
		
		if(dataList!=null) {
			try {
				
				for(int i=0;i<dataList.size();i++) {
					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
					
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getStatus());
					cell01.setCellStyle(styleCenter);
					
					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getDoc_no());
					cell11.setCellStyle(styleLeft);
					
					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getRev_no());
					cell21.setCellStyle(styleCenter);
					
					Cell cell31 = row.createCell(3);
					cell31.setCellValue(dataList.get(i).getTitle());
					cell31.setCellStyle(styleLeft);
					
					Cell cell41 = row.createCell(4);
//					System.out.println("dataList.get(i).getReg_date():"+dataList.get(i).getReg_date());
					cell41.setCellValue(CommonConst.stringdateTODate(dataList.get(i).getReg_date()));
					cell41.setCellStyle(styleCenter);
					
					Cell cell51 = row.createCell(5);
//					System.out.println("dataList.get(i).getFile_size():"+ dataList.get(i).getFile_size());
					cell51.setCellValue(CommonConst.intByte(dataList.get(i).getFile_size()));
					cell51.setCellStyle(styleRight);
				}
				
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
			
			String setDate = CommonConst.currentDateAndTime();
			
			res.reset();
			res.setContentType("ms-vnd/excel");
			res.setHeader("Set-Cookie", "fileDownload=true; path=/");
			res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
			res.setHeader("Content-Description", "JSP Generated Data"); 
			res.setHeader("Content-Transfer-Encoding", "binary;"); 
			res.setHeader("Pragma", "no-cache;"); 
			res.setHeader("Expires", "-1;");		
			ServletOutputStream sw = res.getOutputStream();
			workbook.write(sw);
			sw.close();
//			System.out.println(excelName + " written successfully");
			
			return;
		}
	}

	public void drnPrint(HttpServletResponse response, List<Object> db_infos) {

//		Workbook workbook = new XSSFWorkbook();		
//		
//		Sheet sheet = workbook.createSheet("DRN"); //시트명
//		
//		int rowIndex = 0;
//		
//		sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));		
//		Row row = sheet.createRow(rowIndex++);
//		
//		sheet.setColumnWidth(0, 8000);
//		sheet.setColumnWidth(1, 4000);
//		sheet.setColumnWidth(2, 10000);
//		sheet.setColumnWidth(3, 6000);
//		sheet.setColumnWidth(4, 4000);
//		
//		CellStyle styleCenter = workbook.createCellStyle();
//		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
//		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
//
//		CellStyle styleLeft = workbook.createCellStyle();
//		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
//		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
//
//		CellStyle styleRight = workbook.createCellStyle();
//		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
//		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
//		
//		CellStyle titleStyle = workbook.createCellStyle();
//		Font headerFont = workbook.createFont();
//		headerFont.setFontHeight((short)300);
//		headerFont.setBoldweight((short)600);
//		titleStyle.setFont(headerFont);
//		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
//		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
//		
//		
//		CellStyle styleBackground = workbook.createCellStyle();
//		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);			
//		
//		Cell titleCell = row.createCell(0);
//		titleCell.setCellValue("Documnet List : ("+pathName+")");
//		titleCell.setCellStyle(styleCenter);
//		titleCell.setCellStyle(titleStyle);
//		row = sheet.createRow(rowIndex++);
//		
//		// 프로젝트명
//		Cell projectCell = row.createCell(0);
//		projectCell.setCellValue("Project : " + prj_Name );
//		row = sheet.createRow(rowIndex++);
//		
//		// Date
//		LocalDate now = LocalDate.now();  
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
//		String formatedNow = now.format(formatter);		
//		
//		Cell dateCell = row.createCell(4);
//		dateCell.setCellValue("Date : " + formatedNow );
//		dateCell.setCellStyle(styleRight);
//		row = sheet.createRow(rowIndex++);
//		row = sheet.createRow(rowIndex++);
//		
//		Cell cell0 = row.createCell(0);
//		cell0.setCellValue("Document No");
//		cell0.setCellStyle(styleBackground);
//		
//		Cell cell1 = row.createCell(1);
//		cell1.setCellValue("RevNo");
//		cell1.setCellStyle(styleBackground);
//		
//		Cell cell2 = row.createCell(2);
//		cell2.setCellValue("Title");
//		cell2.setCellStyle(styleBackground);
//		
//		Cell cell3 = row.createCell(3);
//		cell3.setCellValue("Modified Date");
//		cell3.setCellStyle(styleBackground);
//		
//		Cell cell4 = row.createCell(4);
//		cell4.setCellValue("Size");
//		cell4.setCellStyle(styleBackground);
//		
//		if(dataList!=null) {
//			try {
//				
//				for(int i=0;i<dataList.size();i++) {
//					sheet.autoSizeColumn(i);
//					row = sheet.createRow(i+rowIndex);
//					
//					Cell cell01 = row.createCell(0);
//					cell01.setCellValue(dataList.get(i).getDoc_no());
//					cell01.setCellStyle(styleCenter);
//					
//					Cell cell11 = row.createCell(1);
//					cell11.setCellValue(dataList.get(i).getRev_no());
//					cell11.setCellStyle(styleCenter);
//					
//					Cell cell21 = row.createCell(2);
//					cell21.setCellValue(dataList.get(i).getTitle());
//					cell21.setCellStyle(styleCenter);
//					
//					Cell cell31 = row.createCell(3);
////					System.out.println("dataList.get(i).getReg_date():"+dataList.get(i).getReg_date());
//					cell31.setCellValue(CommonConst.stringdateTODate(dataList.get(i).getReg_date()));
//					cell31.setCellStyle(styleCenter);
//					
//					Cell cell41 = row.createCell(4);
////					System.out.println("dataList.get(i).getFile_size():"+ dataList.get(i).getFile_size());
//					cell41.setCellValue(CommonConst.intByte(dataList.get(i).getFile_size()));
//					cell41.setCellStyle(styleCenter);
//				}
//				
//			}catch(Exception e) {
//				//System.out.println(e.getMessage());
//				return;
//			}
//			
//			String setDate = CommonConst.currentDateAndTime();
//			
//			res.reset();
//			res.setContentType("ms-vnd/excel");
//			res.setHeader("Set-Cookie", "fileDownload=true; path=/");
//			res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
//			res.setHeader("Content-Description", "JSP Generated Data"); 
//			res.setHeader("Content-Transfer-Encoding", "binary;"); 
//			res.setHeader("Pragma", "no-cache;"); 
//			res.setHeader("Expires", "-1;");		
//			ServletOutputStream sw = res.getOutputStream();
//			workbook.write(sw);
//			sw.close();
//			
//			return;
//		}
		
	}
	
	/**
	 * 1. 메소드명 : trOutGoingExcelDownload
	 * 2. 작성일: 2021. 02. 14.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시, TR, STR, VTR, IN OUT 다같이 사용
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 2021. 02. 15.
	 */
	public static void trOutGoingExcelDownload(HttpServletResponse res, String titleList, List<PrjTrVO> dataList, String excelName, String chkTrStrVtr, String chkTrInOut, String from_to_search, String prj_Name) throws Exception {
		
		
		// 선택된 코드 타입
				
				Workbook workbook = new XSSFWorkbook();
				
				Sheet sheet = null;
				
				if(chkTrInOut.equals("IN")) {
					if(chkTrStrVtr.equals("TR")) {
						sheet = workbook.createSheet("Transmittal List(InComing)"); //시트명
					}else if(chkTrStrVtr.equals("VTR")) {
						sheet = workbook.createSheet("VTransmittal List(InComing)"); //시트명
					}else if(chkTrStrVtr.equals("STR")) {
						sheet = workbook.createSheet("STransmittal List(InComing)"); //시트명
					}
					
					int rowIndex = 0;
					
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
					
					Row row = sheet.createRow(rowIndex++);
					
					sheet.setColumnWidth(0, 6000);
					sheet.setColumnWidth(1, 6000);
					sheet.setColumnWidth(2, 6000);
					sheet.setColumnWidth(3, 6000);
					sheet.setColumnWidth(4, 6000);
					
					CellStyle styleCenter = workbook.createCellStyle();
					styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleLeft = workbook.createCellStyle();
					styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
					styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleRight = workbook.createCellStyle();
					styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
					styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					CellStyle titleStyle = workbook.createCellStyle();
					Font headerFont = workbook.createFont();
					headerFont.setFontHeight((short)300);
					headerFont.setBoldweight((short)600);
					titleStyle.setFont(headerFont);
					titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					
					CellStyle styleBackground = workbook.createCellStyle();
					styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleBackground.setAlignment(CellStyle.ALIGN_CENTER);			
					
					if(chkTrStrVtr.equals("TR")) {
						
						Cell titleCell = row.createCell(0);
						titleCell.setCellValue("Transmittal List(InComing)");
						titleCell.setCellStyle(styleCenter);
						titleCell.setCellStyle(titleStyle);
						row = sheet.createRow(rowIndex++);
					}else if(chkTrStrVtr.equals("VTR")) {
						
						Cell titleCell = row.createCell(0);
						titleCell.setCellValue("VTransmittal List(InComing)");
						titleCell.setCellStyle(styleCenter);
						titleCell.setCellStyle(titleStyle);
						row = sheet.createRow(rowIndex++);
					}else if(chkTrStrVtr.equals("STR")) {
						
						Cell titleCell = row.createCell(0);
						titleCell.setCellValue("STransmittal List(InComing)");
						titleCell.setCellStyle(styleCenter);
						titleCell.setCellStyle(titleStyle);
						row = sheet.createRow(rowIndex++);
					}
					
					// 프로젝트명
					Cell projectCell = row.createCell(0);
					projectCell.setCellValue("Project : " + prj_Name );
					row = sheet.createRow(rowIndex++);		
					
					// Date
					LocalDate now = LocalDate.now();  
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
					String formatedNow = now.format(formatter);		
					
					Cell dateCell = row.createCell(4);
					dateCell.setCellValue("Period : " + from_to_search );
					dateCell.setCellStyle(styleRight);
					row = sheet.createRow(rowIndex++);
					row = sheet.createRow(rowIndex++);		
					
					// 헤드 필드 설정
					Cell cell0 = row.createCell(0);
					cell0.setCellValue("Transmittal No");
					cell0.setCellStyle(styleBackground);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue("Subject");
					cell1.setCellStyle(styleBackground);
					
					Cell cell2 = row.createCell(2);
					cell2.setCellValue("Received Date");
					cell2.setCellStyle(styleBackground);
					
					Cell cell3 = row.createCell(3);
					cell3.setCellValue("Writer");
					cell3.setCellStyle(styleBackground);
					
					Cell cell7 = row.createCell(4);
					cell7.setCellValue("TR Remark");
					cell7.setCellStyle(styleBackground);
					
					if(dataList != null) {
						try {
							for(int i=0; i<dataList.size(); i++) {
//								sheet.autoSizeColumn(i);
								row = sheet.createRow(i+rowIndex);
//								System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
								Cell cell01 = row.createCell(0);
								cell01.setCellValue(dataList.get(i).getTr_no());
								cell01.setCellStyle(styleCenter);
								
								Cell cell11 = row.createCell(1);
								cell11.setCellValue(dataList.get(i).getTr_subject());
								cell11.setCellStyle(styleCenter);
								
								Cell cell21 = row.createCell(2);
								cell21.setCellValue(dataList.get(i).getSend_date());
								cell21.setCellStyle(styleCenter);
								
								Cell cell31 = row.createCell(3);
								cell31.setCellValue(dataList.get(i).getWriter());
								cell31.setCellStyle(styleCenter);
								
								Cell cell41 = row.createCell(4);
								cell41.setCellValue(dataList.get(i).getTr_remark());
								cell41.setCellStyle(styleCenter);
								
							}
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							return;
						}
					}
					
					String setDate = CommonConst.currentDateAndTime();
					
					res.reset();
					res.setContentType("ms-vnd/excel");
					res.setHeader("Set-Cookie", "fileDownload=true; path=/");
					res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
					res.setHeader("Content-Description", "JSP Generated Data"); 
					res.setHeader("Content-Transfer-Encoding", "binary;"); 
					res.setHeader("Pragma", "no-cache;"); 
					res.setHeader("Expires", "-1;");		
					ServletOutputStream sw = res.getOutputStream();
					workbook.write(sw);
					sw.close();
					System.out.println(excelName + " written successfully");
					return ;
					
				}else if(chkTrInOut.equals("OUT")) {	
					if(chkTrStrVtr.equals("TR")) {
						sheet = workbook.createSheet("Transmittal List(Outgoing)"); //시트명
					}else if(chkTrStrVtr.equals("VTR")) {
						sheet = workbook.createSheet("VTransmittal List(Outgoing)"); //시트명
					}else if(chkTrStrVtr.equals("STR")) {
						sheet = workbook.createSheet("STransmittal List(Outgoing)"); //시트명
					}
					
					int rowIndex = 0;
					
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,7));
					
					Row row = sheet.createRow(rowIndex++);
					
					sheet.setColumnWidth(0, 6000);
					sheet.setColumnWidth(1, 6000);
					sheet.setColumnWidth(2, 6000);
					sheet.setColumnWidth(3, 6000);
					sheet.setColumnWidth(4, 6000);
					sheet.setColumnWidth(5, 6000);
					sheet.setColumnWidth(6, 6000);
					sheet.setColumnWidth(7, 6000);
					
					CellStyle styleCenter = workbook.createCellStyle();
					styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleLeft = workbook.createCellStyle();
					styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
					styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleRight = workbook.createCellStyle();
					styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
					styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					CellStyle titleStyle = workbook.createCellStyle();
					Font headerFont = workbook.createFont();
					headerFont.setFontHeight((short)300);
					headerFont.setBoldweight((short)600);
					titleStyle.setFont(headerFont);
					titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					
					CellStyle styleBackground = workbook.createCellStyle();
					styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleBackground.setAlignment(CellStyle.ALIGN_CENTER);			
					
					if(chkTrStrVtr.equals("TR")) {						

						Cell titleCell = row.createCell(0);
						titleCell.setCellValue("Transmittal List(Outgoing)");
						titleCell.setCellStyle(styleCenter);
						titleCell.setCellStyle(titleStyle);
						row = sheet.createRow(rowIndex++);
					}else if(chkTrStrVtr.equals("VTR")) {
						
						Cell titleCell = row.createCell(0);
						titleCell.setCellValue("VTransmittal List(Outgoing)");
						titleCell.setCellStyle(styleCenter);
						titleCell.setCellStyle(titleStyle);
						row = sheet.createRow(rowIndex++);
					}else if(chkTrStrVtr.equals("STR")) {
						

						Cell titleCell = row.createCell(0);
						titleCell.setCellValue("STransmittal List(Outgoing)");
						titleCell.setCellStyle(styleCenter);
						titleCell.setCellStyle(titleStyle);
						row = sheet.createRow(rowIndex++);
					}
					
					// 프로젝트명
					Cell projectCell = row.createCell(0);
					projectCell.setCellValue("Project : " + prj_Name );
					row = sheet.createRow(rowIndex++);		
					
					// Date
					LocalDate now = LocalDate.now();  
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
					String formatedNow = now.format(formatter);		
					
					Cell dateCell = row.createCell(7);
					dateCell.setCellValue("Period : " + from_to_search );
					dateCell.setCellStyle(styleRight);
					row = sheet.createRow(rowIndex++);
					row = sheet.createRow(rowIndex++);		
					
					// 헤드 필드 설정
					Cell cell0 = row.createCell(0);
					cell0.setCellValue("Status");
					cell0.setCellStyle(styleBackground);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue("Transmittal No");
					cell1.setCellStyle(styleBackground);
					
					Cell cell2 = row.createCell(2);
					cell2.setCellValue("iTR");
					cell2.setCellStyle(styleBackground);
					
					Cell cell3 = row.createCell(3);
					cell3.setCellValue("Subject");
					cell3.setCellStyle(styleBackground);
					
					Cell cell4 = row.createCell(4);
					cell4.setCellValue("SendDate");
					cell4.setCellStyle(styleBackground);
					
					Cell cell5 = row.createCell(5);
					cell5.setCellValue("IssuePur");
					cell5.setCellStyle(styleBackground);
					
					Cell cell6 = row.createCell(6);
					cell6.setCellValue("Discipline");
					cell6.setCellStyle(styleBackground);
					
					Cell cell7 = row.createCell(7);
					cell7.setCellValue("Writer");
					cell7.setCellStyle(styleBackground);
					
					if(dataList != null) {
						try {
							for(int i=0; i<dataList.size(); i++) {
//								sheet.autoSizeColumn(i);
								row = sheet.createRow(i+rowIndex);
//								System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
								Cell cell01 = row.createCell(0);
								cell01.setCellValue(dataList.get(i).getTr_status());
								cell01.setCellStyle(styleCenter);
								
								Cell cell11 = row.createCell(1);
								cell11.setCellValue(dataList.get(i).getTr_no());
								cell11.setCellStyle(styleCenter);
								
								Cell cell21 = row.createCell(2);
								cell21.setCellValue(dataList.get(i).getItr_no());
								cell21.setCellStyle(styleCenter);
								
								Cell cell31 = row.createCell(3);
								cell31.setCellValue(dataList.get(i).getTr_subject());
								cell31.setCellStyle(styleCenter);
								
								Cell cell41 = row.createCell(4);
								cell41.setCellValue(dataList.get(i).getSend_date());
								cell41.setCellStyle(styleCenter);
								
								Cell cell51 = row.createCell(5);
								cell51.setCellValue(dataList.get(i).getIssue_pur());
								cell51.setCellStyle(styleCenter);
								
								Cell cell61 = row.createCell(6);
								cell61.setCellValue(dataList.get(i).getDiscip());
								cell61.setCellStyle(styleCenter);
								
								Cell cell71 = row.createCell(7);
								cell71.setCellValue(dataList.get(i).getWriter());
								cell71.setCellStyle(styleCenter);
								
							}
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							return;
						}
					}
					
					String setDate = CommonConst.currentDateAndTime();
					
					res.reset();
					res.setContentType("ms-vnd/excel");
					res.setHeader("Set-Cookie", "fileDownload=true; path=/");
					res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
					res.setHeader("Content-Description", "JSP Generated Data"); 
					res.setHeader("Content-Transfer-Encoding", "binary;"); 
					res.setHeader("Pragma", "no-cache;"); 
					res.setHeader("Expires", "-1;");		
					ServletOutputStream sw = res.getOutputStream();
					workbook.write(sw);
					sw.close();
					System.out.println(excelName + " written successfully");
					return ;
				}
		
	}
	
	/**
	 * 1. 메소드명 : accessExcelDownload
	 * 2. 작성일: 2022. 02. 20.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시,
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일: 
 */
	public static void accessExcelDownload(HttpServletResponse res, String titleList, List<SecurityLogVO> dataList, String excelName) throws Exception {
		
		
		Workbook workbook = new XSSFWorkbook();


		Sheet sheet = workbook.createSheet("Access History");

		int rowIndex = 0;
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		Row row = sheet.createRow(rowIndex++);

		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
		
		
		// 타이틀 설정
		Cell titleCell = row.createCell(0);
		titleCell.setCellValue("Access History");
		titleCell.setCellStyle(styleCenter);
		titleCell.setCellStyle(titleStyle);
		row = sheet.createRow(rowIndex++);
		
		// 프로젝트명
//		Cell projectCell = row.createCell(0);
//		projectCell.setCellValue("Project : " + prj_Name );
//		row = sheet.createRow(rowIndex++);
		
		
		// Date
//		LocalDate now = LocalDate.now();  
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
//		String formatedNow = now.format(formatter);

			
		//Cell dateCell = row.createCell(4);
		//dateCell.setCellValue("Date : " + formatedNow );
		// 공란 입력에 따른, null point exception 방지
		row = sheet.createRow(rowIndex++);
		Cell nullCell = row.createCell(0);
		nullCell.setCellValue(" ");
		
		
		
		// 헤드 필드 설정
		row = sheet.createRow(rowIndex++);
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("Date");
		cell0.setCellStyle(styleBackground);

		Cell cell1 = row.createCell(1);
		cell1.setCellValue("User Name");
		cell1.setCellStyle(styleBackground);

		Cell cell2 = row.createCell(2);
		cell2.setCellValue("IP");
		cell2.setCellStyle(styleBackground);

		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Method");
		cell3.setCellStyle(styleBackground);

		if (dataList != null) {
			try {
				for(int i=0; i<dataList.size(); i++){
//					sheet.autoSizeColumn(i);
					row = sheet.createRow(i+rowIndex);
					//System.out.println(dataList.get(i).getWbs_code() + " / " + dataList.get(i).getWbs_desc() + " / " + dataList.get(i).getWgtval() + " / " + dataList.get(i).getDoc_type() + " / " + dataList.get(i).getDiscip_display());
					Cell cell01 = row.createCell(0);
					cell01.setCellValue(dataList.get(i).getReg_date());
					cell01.setCellStyle(styleCenter);

					Cell cell11 = row.createCell(1);
					cell11.setCellValue(dataList.get(i).getReg_nm());
					cell11.setCellStyle(styleCenter);

					Cell cell21 = row.createCell(2);
					cell21.setCellValue(dataList.get(i).getRemote_ip());
					cell21.setCellStyle(styleCenter);

					Cell cell31 = row.createCell(3);
					cell31.setCellValue(dataList.get(i).getMethod());
					cell31.setCellStyle(styleCenter);			
				}				
				
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
			
		}

		res.reset();
		res.setContentType("ms-vnd/excel");
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"" + (URLEncoder.encode((excelName+".xlsx"),"UTF-8")) + "\""));
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");		
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
		//System.out.println(excelName + " written successfully");
		return ;
	}
	
	/**
	 * 1. 메소드명 : seculityLogExcelDownload
	 * 2. 작성일: 2022. 03. 13.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일:
	 */
	public static void seculityLogExcelDownload(HttpServletResponse res, String titleList, List<SecurityLogVO> searchLogList, String excelName, String from_to_search, String prj_Name, String logAtc) throws Exception {
		
		
		// 선택된 코드 타입
				
				Workbook workbook = new XSSFWorkbook();
				
				Sheet sheet = null;
				
				if(logAtc.equals("systemLog")) {
					
					int rowIndex = 0;
					
					sheet = workbook.createSheet("보안로그(SYSTEM LOG)"); //시트명
					
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
					
					Row row = sheet.createRow(rowIndex++);
					
					sheet.setColumnWidth(0, 6000);
					sheet.setColumnWidth(1, 8000);
					sheet.setColumnWidth(2, 3000);
					sheet.setColumnWidth(3, 4000);
					sheet.setColumnWidth(4, 4000);
					sheet.setColumnWidth(5, 6000);
					
					CellStyle styleCenter = workbook.createCellStyle();
					styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleLeft = workbook.createCellStyle();
					styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
					styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleRight = workbook.createCellStyle();
					styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
					styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					CellStyle titleStyle = workbook.createCellStyle();
					Font headerFont = workbook.createFont();
					headerFont.setFontHeight((short)300);
					headerFont.setBoldweight((short)600);
					titleStyle.setFont(headerFont);
					titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					
					CellStyle styleBackground = workbook.createCellStyle();
					styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
						
					Cell titleCell = row.createCell(0);
					titleCell.setCellValue("보안로그(SYSTEM LOG)");
					titleCell.setCellStyle(styleCenter);
					titleCell.setCellStyle(titleStyle);
					row = sheet.createRow(rowIndex++);
					
					// 프로젝트명
					Cell projectCell = row.createCell(0);
					projectCell.setCellValue("Project : " + prj_Name );
					row = sheet.createRow(rowIndex++);		
					
					// Date
					LocalDate now = LocalDate.now();  
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
					String formatedNow = now.format(formatter);		
					
					Cell dateCell = row.createCell(5);
					dateCell.setCellValue("Period : " + from_to_search );
					dateCell.setCellStyle(styleRight);
					row = sheet.createRow(rowIndex++);
					row = sheet.createRow(rowIndex++);		
					
					// 헤드 필드 설정
					Cell cell0 = row.createCell(0);
					cell0.setCellValue("DATE");
					cell0.setCellStyle(styleBackground);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue("PGM NAME");
					cell1.setCellStyle(styleBackground);
					
					Cell cell2 = row.createCell(2);
					cell2.setCellValue("METHOD");
					cell2.setCellStyle(styleBackground);
					
					Cell cell3 = row.createCell(3);
					cell3.setCellValue("USER ID");
					cell3.setCellStyle(styleBackground);
					
					Cell cell4 = row.createCell(4);
					cell4.setCellValue("USER NAME");
					cell4.setCellStyle(styleBackground);
					
					Cell cell5 = row.createCell(5);
					cell5.setCellValue("IP");
					cell5.setCellStyle(styleBackground);
					
					if(searchLogList != null) {
						try {
							for(int i=0; i<searchLogList.size(); i++) {
//								sheet.autoSizeColumn(i);
								row = sheet.createRow(i+rowIndex);
//								System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
								Cell cell01 = row.createCell(0);
								cell01.setCellValue(searchLogList.get(i).getReg_date());
								cell01.setCellStyle(styleCenter);
								
								Cell cell11 = row.createCell(1);
								cell11.setCellValue(searchLogList.get(i).getPgm_nm());
								cell11.setCellStyle(styleLeft);
								
								Cell cell21 = row.createCell(2);
								cell21.setCellValue(searchLogList.get(i).getMethod());
								cell21.setCellStyle(styleLeft);
								
								Cell cell31 = row.createCell(3);
								cell31.setCellValue(searchLogList.get(i).getReg_id());
								cell31.setCellStyle(styleLeft);
								
								Cell cell41 = row.createCell(4);
								cell41.setCellValue(searchLogList.get(i).getReg_nm());
								cell41.setCellStyle(styleCenter);

								Cell cell51 = row.createCell(5);
								cell51.setCellValue(searchLogList.get(i).getRemote_ip());
								cell51.setCellStyle(styleCenter);
								
							}
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							return;
						}
					}
					
					String setDate = CommonConst.currentDateAndTime();
					
					res.reset();
					res.setContentType("ms-vnd/excel");
					res.setHeader("Set-Cookie", "fileDownload=true; path=/");
					res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
					res.setHeader("Content-Description", "JSP Generated Data"); 
					res.setHeader("Content-Transfer-Encoding", "binary;"); 
					res.setHeader("Pragma", "no-cache;"); 
					res.setHeader("Expires", "-1;");		
					ServletOutputStream sw = res.getOutputStream();
					workbook.write(sw);
					sw.close();
					System.out.println(excelName + " written successfully");
					return ;
					
				}else if(logAtc.equals("documentsLog")) {
					
					int rowIndex = 0;
					
					sheet = workbook.createSheet("보안로그(DOCUMENTS LOG)"); //시트명
					
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,10));
					
					Row row = sheet.createRow(rowIndex++);
					
					sheet.setColumnWidth(0, 6000);
					sheet.setColumnWidth(1, 6000);
					sheet.setColumnWidth(2, 6000);
					sheet.setColumnWidth(3, 6000);
					sheet.setColumnWidth(4, 6000);
					sheet.setColumnWidth(5, 6000);
					sheet.setColumnWidth(6, 6000);
					sheet.setColumnWidth(7, 6000);
					sheet.setColumnWidth(8, 6000);
					sheet.setColumnWidth(9, 6000);
					sheet.setColumnWidth(10, 6000);
					
					CellStyle styleCenter = workbook.createCellStyle();
					styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleLeft = workbook.createCellStyle();
					styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
					styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

					CellStyle styleRight = workbook.createCellStyle();
					styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
					styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					CellStyle titleStyle = workbook.createCellStyle();
					Font headerFont = workbook.createFont();
					headerFont.setFontHeight((short)300);
					headerFont.setBoldweight((short)600);
					titleStyle.setFont(headerFont);
					titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
					titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
					
					
					CellStyle styleBackground = workbook.createCellStyle();
					styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
					styleBackground.setAlignment(CellStyle.ALIGN_CENTER);				

					Cell titleCell = row.createCell(0);
					titleCell.setCellValue("보안로그(DOCUMENTS LOG)");
					titleCell.setCellStyle(styleCenter);
					titleCell.setCellStyle(titleStyle);
					row = sheet.createRow(rowIndex++);
					
					// 프로젝트명
					Cell projectCell = row.createCell(0);
					projectCell.setCellValue("Project : " + prj_Name );
					row = sheet.createRow(rowIndex++);		
					
					// Date
					LocalDate now = LocalDate.now();  
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
					String formatedNow = now.format(formatter);		
					
					Cell dateCell = row.createCell(10);
					dateCell.setCellValue("Period : " + from_to_search );
					dateCell.setCellStyle(styleRight);
					row = sheet.createRow(rowIndex++);
					row = sheet.createRow(rowIndex++);		
					
					// 헤드 필드 설정
					Cell cell0 = row.createCell(0);
					cell0.setCellValue("DATE");
					cell0.setCellStyle(styleBackground);

					Cell cell1 = row.createCell(1);
					cell1.setCellValue("PROJECT NO");
					cell1.setCellStyle(styleBackground);
					
					Cell cell2 = row.createCell(2);
					cell2.setCellValue("PROJECT NAME");
					cell2.setCellStyle(styleBackground);
					
					Cell cell3 = row.createCell(3);
					cell3.setCellValue("METHOD");
					cell3.setCellStyle(styleBackground);
					
					Cell cell4 = row.createCell(4);
					cell4.setCellValue("DOC. NO");
					cell4.setCellStyle(styleBackground);
					
					Cell cell5 = row.createCell(5);
					cell5.setCellValue("DOC. TITLE");
					cell5.setCellStyle(styleBackground);
					
					Cell cell6 = row.createCell(6);
					cell6.setCellValue("PATH");
					cell6.setCellStyle(styleBackground);
					
					Cell cell7 = row.createCell(7);
					cell7.setCellValue("SIZE(M)");
					cell7.setCellStyle(styleBackground);
					
					Cell cell8 = row.createCell(8);
					cell8.setCellValue("USER ID");
					cell8.setCellStyle(styleBackground);
					
					Cell cell9 = row.createCell(9);
					cell9.setCellValue("USER NAME");
					cell9.setCellStyle(styleBackground);
					
					Cell cell10 = row.createCell(10);
					cell10.setCellValue("IP");
					cell10.setCellStyle(styleBackground);
					
					if(searchLogList != null) {
						try {
							for(int i=0; i<searchLogList.size(); i++) {
//								sheet.autoSizeColumn(i);
								row = sheet.createRow(i+rowIndex);
//								System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
								Cell cell01 = row.createCell(0);
								cell01.setCellValue(searchLogList.get(i).getReg_date());
								cell01.setCellStyle(styleCenter);
								
								Cell cell11 = row.createCell(1);
								cell11.setCellValue(searchLogList.get(i).getPrj_no());
								cell11.setCellStyle(styleLeft);
								
								Cell cell21 = row.createCell(2);
								cell21.setCellValue(searchLogList.get(i).getPrj_nm());
								cell21.setCellStyle(styleLeft);
								
								Cell cell31 = row.createCell(3);
								cell31.setCellValue(searchLogList.get(i).getMethod());
								cell31.setCellStyle(styleLeft);
								
								Cell cell41 = row.createCell(4);
								cell41.setCellValue(searchLogList.get(i).getDoc_no());
								cell41.setCellStyle(styleLeft);
								
								Cell cell51 = row.createCell(5);
								cell51.setCellValue(searchLogList.get(i).getDoc_title());
								cell51.setCellStyle(styleLeft);
								
								Cell cell61 = row.createCell(6);
								cell61.setCellValue(searchLogList.get(i).getFolder_path());
								cell61.setCellStyle(styleLeft);
								
								Cell cell71 = row.createCell(7);
								cell71.setCellValue(CommonConst.intByte(searchLogList.get(i).getFile_size()));
								cell71.setCellStyle(styleRight);
								
								Cell cell81 = row.createCell(8);
								cell81.setCellValue(searchLogList.get(i).getReg_id());
								cell81.setCellStyle(styleCenter);
								
								Cell cell91 = row.createCell(9);
								cell91.setCellValue(searchLogList.get(i).getReg_nm());
								cell91.setCellStyle(styleCenter);
								
								Cell cell101 = row.createCell(10);
								cell101.setCellValue(searchLogList.get(i).getRemote_ip());
								cell101.setCellStyle(styleCenter);
								
							}
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							return;
						}
					}
					
					String setDate = CommonConst.currentDateAndTime();
					
					res.reset();
					res.setContentType("ms-vnd/excel");
					res.setHeader("Set-Cookie", "fileDownload=true; path=/");
					res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
					res.setHeader("Content-Description", "JSP Generated Data"); 
					res.setHeader("Content-Transfer-Encoding", "binary;"); 
					res.setHeader("Pragma", "no-cache;"); 
					res.setHeader("Expires", "-1;");		
					ServletOutputStream sw = res.getOutputStream();
					workbook.write(sw);
					sw.close();
					System.out.println(excelName + " written successfully");
					return ;
				}
		
	}
	
	/**
	 * 1. 메소드명 : seculityLogExcelDownload
	 * 2. 작성일: 2022. 03. 13.
	 * 3. 작성자: 박정우
	 * 4. 설명:	엑셀 다운로드 버튼 누를 시
	 * 			그리드 데이터를 포함한 템플릿 생성 및 엑셀 파일 생성 
	 * 5. 수정일:
	 */
	public static void searchEmailingStatusExcelDownload(HttpServletResponse res, String titleList, List<PrjEmailTypeVO> getEmailSendList, String excelName, String from_to_search, String prj_Name) throws Exception {
		
		
		// 선택된 코드 타입
				
				Workbook workbook = new XSSFWorkbook();
				
				Sheet sheet = null;
					

				int rowIndex = 0;
				
				sheet = workbook.createSheet("E-MAILING STATUS(발송현황)"); //시트명
				
				sheet.addMergedRegion(new CellRangeAddress(0,0,0,13));
				
				Row row = sheet.createRow(rowIndex++);
				
				sheet.setColumnWidth(0, 6000);
				sheet.setColumnWidth(1, 4000);
				sheet.setColumnWidth(2, 4000);
				sheet.setColumnWidth(3, 4000);
				sheet.setColumnWidth(4, 6000);
				sheet.setColumnWidth(5, 6000);
				sheet.setColumnWidth(6, 6000);
				sheet.setColumnWidth(7, 6000);
				sheet.setColumnWidth(8, 6000);
				sheet.setColumnWidth(9, 6000);
				sheet.setColumnWidth(10, 6000);
				sheet.setColumnWidth(11, 6000);
				sheet.setColumnWidth(12, 6000);
				sheet.setColumnWidth(13, 6000);
				
				CellStyle styleCenter = workbook.createCellStyle();
				styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
				styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

				CellStyle styleLeft = workbook.createCellStyle();
				styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
				styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

				CellStyle styleRight = workbook.createCellStyle();
				styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
				styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
				CellStyle titleStyle = workbook.createCellStyle();
				Font headerFont = workbook.createFont();
				headerFont.setFontHeight((short)300);
				headerFont.setBoldweight((short)600);
				titleStyle.setFont(headerFont);
				titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
				titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
				
				
				CellStyle styleBackground = workbook.createCellStyle();
				styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
				styleBackground.setAlignment(CellStyle.ALIGN_CENTER);
					
				Cell titleCell = row.createCell(0);
				titleCell.setCellValue("E-MAILING STATUS(발송현황)");
				titleCell.setCellStyle(styleCenter);
				titleCell.setCellStyle(titleStyle);
				row = sheet.createRow(rowIndex++);
				
				// 프로젝트명
				Cell projectCell = row.createCell(0);
				projectCell.setCellValue("Project : " + prj_Name );
				row = sheet.createRow(rowIndex++);		
				
				// Date
				LocalDate now = LocalDate.now();  
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
				String formatedNow = now.format(formatter);		
				
				Cell dateCell = row.createCell(13);
				dateCell.setCellValue("DURATION : " + from_to_search );
				dateCell.setCellStyle(styleRight);
				row = sheet.createRow(rowIndex++);
				row = sheet.createRow(rowIndex++);		
				
				// 헤드 필드 설정
				Cell cell0 = row.createCell(0);
				cell0.setCellValue("PROJECT");
				cell0.setCellStyle(styleBackground);

				Cell cell1 = row.createCell(1);
				cell1.setCellValue("FEATURE");
				cell1.setCellStyle(styleBackground);
				
				Cell cell2 = row.createCell(2);
				cell2.setCellValue("TYPE");
				cell2.setCellStyle(styleBackground);
				
				Cell cell3 = row.createCell(3);
				cell3.setCellValue("OPERATOR");
				cell3.setCellStyle(styleBackground);
				
				Cell cell4 = row.createCell(4);
				cell4.setCellValue("MAIL SUBJECT");
				cell4.setCellStyle(styleBackground);
				
				Cell cell5 = row.createCell(5);
				cell5.setCellValue("DOC NO/TR NO/ CORR NO");
				cell5.setCellStyle(styleBackground);
				
				Cell cell6 = row.createCell(6);
				cell6.setCellValue("TR SEND DATE");
				cell6.setCellStyle(styleBackground);
				
				Cell cell7 = row.createCell(7);
				cell7.setCellValue("TR SUBJECT");
				cell7.setCellStyle(styleBackground);
				
				Cell cell8 = row.createCell(8);
				cell8.setCellValue("MAIL SEND DATE");
				cell8.setCellStyle(styleBackground);
				
				Cell cell9 = row.createCell(9);
				cell9.setCellValue("MAIL RECEIVED DATE");
				cell9.setCellStyle(styleBackground);
				
				Cell cell10 = row.createCell(10);
				cell10.setCellValue("MAIL SENDER");
				cell10.setCellStyle(styleBackground);
				
				Cell cell511 = row.createCell(11);
				cell511.setCellValue("MAIL RECEIVER");
				cell511.setCellStyle(styleBackground);
				
				Cell cell12 = row.createCell(12);
				cell12.setCellValue("MAIL REFER");
				cell12.setCellStyle(styleBackground);
				
				Cell cell13 = row.createCell(13);
				cell13.setCellValue("MAIL BCC");
				cell13.setCellStyle(styleBackground);
				
				if(getEmailSendList != null) {
					try {
						for(int i=0; i<getEmailSendList.size(); i++) {
//							sheet.autoSizeColumn(i);
							row = sheet.createRow(i+rowIndex);
//							System.out.println(dataList.get(i).getSet_code() +"/" + dataList.get(i).getSet_desc());
							Cell cell01 = row.createCell(0);
							cell01.setCellValue(getEmailSendList.get(i).getPrj_nm());
							cell01.setCellStyle(styleLeft);
							
							Cell cell11 = row.createCell(1);
							cell11.setCellValue(getEmailSendList.get(i).getEmail_feature());
							cell11.setCellStyle(styleLeft);
							
							Cell cell21 = row.createCell(2);
							cell21.setCellValue(getEmailSendList.get(i).getEmail_type());
							cell21.setCellStyle(styleLeft);
							
							Cell cell31 = row.createCell(3);
							cell31.setCellValue(getEmailSendList.get(i).getOperator());
							cell31.setCellStyle(styleLeft);
							
							Cell cell41 = row.createCell(4);
							cell41.setCellValue(getEmailSendList.get(i).getSubject());
							cell41.setCellStyle(styleLeft);

							Cell cell51 = row.createCell(5);
							cell51.setCellValue(getEmailSendList.get(i).getUser_data00());
							cell51.setCellStyle(styleLeft);

							Cell cell61 = row.createCell(6);
							cell61.setCellValue(getEmailSendList.get(i).getTr_send_date());
							cell61.setCellStyle(styleCenter);

							Cell cell71 = row.createCell(7);
							cell71.setCellValue(getEmailSendList.get(i).getTr_subject());
							cell71.setCellStyle(styleLeft);

							Cell cell81 = row.createCell(8);
							cell81.setCellValue(getEmailSendList.get(i).getEmail_send_date());
							cell81.setCellStyle(styleCenter);

							Cell cell91 = row.createCell(9);
							cell91.setCellValue(getEmailSendList.get(i).getEmail_receiverd_date());
							cell91.setCellStyle(styleCenter);

							Cell cell101 = row.createCell(10);
							cell101.setCellValue(getEmailSendList.get(i).getEmail_sender_addr());
							cell101.setCellStyle(styleLeft);

							Cell cell111 = row.createCell(11);
							cell111.setCellValue(getEmailSendList.get(i).getEmail_receiver_addr());
							cell111.setCellStyle(styleLeft);

							Cell cell121 = row.createCell(12);
							cell121.setCellValue(getEmailSendList.get(i).getEmail_refer_to());
							cell121.setCellStyle(styleLeft);

							Cell cell131 = row.createCell(13);
							cell131.setCellValue(getEmailSendList.get(i).getEmail_bcc());
							cell131.setCellStyle(styleLeft);
							
						}
					}catch(Exception e) {
						//System.out.println(e.getMessage());
						return;
					}
					
					String setDate = CommonConst.currentDateAndTime();
					
					res.reset();
					res.setContentType("ms-vnd/excel");
					res.setHeader("Set-Cookie", "fileDownload=true; path=/");
					res.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName+"("+setDate+")"+".xlsx"),"UTF-8")) + "\"");
					res.setHeader("Content-Description", "JSP Generated Data"); 
					res.setHeader("Content-Transfer-Encoding", "binary;"); 
					res.setHeader("Pragma", "no-cache;"); 
					res.setHeader("Expires", "-1;");		
					ServletOutputStream sw = res.getOutputStream();
					workbook.write(sw);
					sw.close();
					System.out.println(excelName + " written successfully");
					return ;
				}
		
	}

		/**
		 * 1. 메소드명 : search_excelDownLoad
		 * 2. 작성일: 2022. 5. 10.
		 * 3. 작성자: doil
		 * 4. 설명:  search 엑셀 다운로드
		 * 5. 수정일: doil
		 * @throws IOException 
		 */
	public void search_excelDownLoad(HttpServletResponse response, List<GlobalSearchVO> docList, List<GlobalSearchVO> trList, List<GlobalSearchVO> drnList) throws IOException {
		
		// 현재시간
		String nowDateStr = CommonConst.currentDateAndTime();
			
		String excelName = "newPeros_GlobalSearchResult";
		Workbook workbook = new XSSFWorkbook();
		
		Sheet sheet = null;
			
		// document List 시트 생성
		int rowIndex = 0;
		
		sheet = workbook.createSheet("DOCUMENT LIST"); //시트명
		
		//sheet.addMergedRegion(new CellRangeAddress(0,0,0,7));
		
		Row row = sheet.createRow(rowIndex++);
		
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 6000);
		sheet.setColumnWidth(7, 6000);
		
		CellStyle styleCenter = workbook.createCellStyle();
		styleCenter.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		styleCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleLeft = workbook.createCellStyle();
		styleLeft.setAlignment(CellStyle.ALIGN_LEFT); // 가운데 정렬
		styleLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬

		CellStyle styleRight = workbook.createCellStyle();
		styleRight.setAlignment(CellStyle.ALIGN_RIGHT); // 가운데 정렬
		styleRight.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		CellStyle titleStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setFontHeight((short)300);
		headerFont.setBoldweight((short)600);
		titleStyle.setFont(headerFont);
		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); // 가운데 정렬
		titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 높이 가운데 정렬
		
		
		CellStyle styleBackground = workbook.createCellStyle();
		styleBackground.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleBackground.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleBackground.setAlignment(CellStyle.ALIGN_CENTER);	
		
		// 헤드 필드 설정
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("PROJECT");
		cell0.setCellStyle(styleBackground);

		Cell cell1 = row.createCell(1);
		cell1.setCellValue("Folder Path");
		cell1.setCellStyle(styleBackground);
		
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("Document No.");
		cell2.setCellStyle(styleBackground);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellValue("Title");
		cell3.setCellStyle(styleBackground);
		
		Cell cell4 = row.createCell(4);
		cell4.setCellValue("Modifier");
		cell4.setCellStyle(styleBackground);
		
		Cell cell5 = row.createCell(5);
		cell5.setCellValue("Size");
		cell5.setCellStyle(styleBackground);
		
		Cell cell6 = row.createCell(6);
		cell6.setCellValue("Modified Date");
		cell6.setCellStyle(styleBackground);
		
		Cell cell7 = row.createCell(7);
		cell7.setCellValue("Document Type");
		cell7.setCellStyle(styleBackground);
	
		try {
			int docRow = 0;
			for (GlobalSearchVO rowData : docList) {
				row = sheet.createRow((docRow++) + rowIndex);

				Cell cell01 = row.createCell(0);
				cell01.setCellValue(rowData.getPrj_nm());
				cell01.setCellStyle(styleLeft);

				Cell cell11 = row.createCell(1);
				cell11.setCellValue(rowData.getFolder_str());
				cell11.setCellStyle(styleLeft);

				Cell cell21 = row.createCell(2);
				cell21.setCellValue(rowData.getDoc_no());
				cell21.setCellStyle(styleLeft);

				Cell cell31 = row.createCell(3);
				cell31.setCellValue(rowData.getTitle());
				cell31.setCellStyle(styleLeft);

				Cell cell41 = row.createCell(4);
				cell41.setCellValue(rowData.getModifier());
				cell41.setCellStyle(styleLeft);

				Cell cell51 = row.createCell(5);
				cell51.setCellValue(rowData.getSize1());
				cell51.setCellStyle(styleLeft);

				Cell cell61 = row.createCell(6);
				String dateStr = getDateFormat(rowData.getMod_date());
				cell61.setCellValue(dateStr);
				cell61.setCellStyle(styleCenter);

				Cell cell71 = row.createCell(7);
				cell71.setCellValue(rowData.getDoc_type());
				cell71.setCellStyle(styleLeft);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
			
			
			
		// TR List 시트 생성
		rowIndex = 0;

		sheet = workbook.createSheet("TRANSMITTAL LIST"); // 시트명

		row = sheet.createRow(rowIndex++);

		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 8000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 6000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);

		// 헤드 필드 설정
		cell0 = row.createCell(0);
		cell0.setCellValue("PROJECT");
		cell0.setCellStyle(styleBackground);

		cell1 = row.createCell(1);
		cell1.setCellValue("TR Type");
		cell1.setCellStyle(styleBackground);

		cell2 = row.createCell(2);
		cell2.setCellValue("IN OUT");
		cell2.setCellStyle(styleBackground);

		cell3 = row.createCell(3);
		cell3.setCellValue("TR No.");
		cell3.setCellStyle(styleBackground);

		cell4 = row.createCell(4);
		cell4.setCellValue("TR Subject");
		cell4.setCellStyle(styleBackground);

		cell5 = row.createCell(5);
		cell5.setCellValue("Discipline");
		cell5.setCellStyle(styleBackground);

		cell6 = row.createCell(6);
		cell6.setCellValue("Send Date");
		cell6.setCellStyle(styleBackground);

		cell7 = row.createCell(7);
		cell7.setCellValue("TR Writer");
		cell7.setCellStyle(styleBackground);

		Cell cell8 = row.createCell(8);
		cell8.setCellValue("TR from");
		cell8.setCellStyle(styleBackground);

		Cell cell9 = row.createCell(9);
		cell9.setCellValue("tr to");
		cell9.setCellStyle(styleBackground);

		try {
			int docRow = 0;
			for (GlobalSearchVO rowData : trList) {
				row = sheet.createRow((docRow++) + rowIndex);

				Cell cell01 = row.createCell(0);
				cell01.setCellValue(rowData.getPrj_nm());
				cell01.setCellStyle(styleLeft);

				Cell cell11 = row.createCell(1);
				cell11.setCellValue(rowData.getTrType());
				cell11.setCellStyle(styleLeft);

				Cell cell21 = row.createCell(2);
				cell21.setCellValue(rowData.getInout());
				cell21.setCellStyle(styleLeft);

				Cell cell31 = row.createCell(3);
				cell31.setCellValue(rowData.getTr_no());
				cell31.setCellStyle(styleLeft);

				Cell cell41 = row.createCell(4);
				cell41.setCellValue(rowData.getTr_subject());
				cell41.setCellStyle(styleLeft);

				Cell cell51 = row.createCell(5);
				cell51.setCellValue(rowData.getDiscip_display());
				cell51.setCellStyle(styleLeft);

				Cell cell61 = row.createCell(6);
				String dateStr = getDateFormat(rowData.getSend_date());
				cell61.setCellValue(dateStr);
				cell61.setCellStyle(styleLeft);

				Cell cell71 = row.createCell(7);
				cell71.setCellValue(rowData.getTrWriter());
				cell71.setCellStyle(styleLeft);

				Cell cell81 = row.createCell(8);
				cell81.setCellValue(rowData.getTr_from());
				cell81.setCellStyle(styleLeft);

				Cell cell91 = row.createCell(9);
				cell91.setCellValue(rowData.getTr_to());
				cell91.setCellStyle(styleLeft);

			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
			
			
		// DRN List 시트 생성
		rowIndex = 0;

		sheet = workbook.createSheet("DRN LIST"); // 시트명

		row = sheet.createRow(rowIndex++);

		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 8000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 4000);

		// 헤드 필드 설정
		cell0 = row.createCell(0);
		cell0.setCellValue("PROJECT");
		cell0.setCellStyle(styleBackground);

		cell1 = row.createCell(1);
		cell1.setCellValue("DRN NO");
		cell1.setCellStyle(styleBackground);

		cell2 = row.createCell(2);
		cell2.setCellValue("DRN SUBJECT");
		cell2.setCellStyle(styleBackground);

		cell3 = row.createCell(3);
		cell3.setCellValue("Discipline");
		cell3.setCellStyle(styleBackground);

		cell4 = row.createCell(4);
		cell4.setCellValue("Reg date");
		cell4.setCellStyle(styleBackground);

		cell5 = row.createCell(5);
		cell5.setCellValue("Approval Status");
		cell5.setCellStyle(styleBackground);

		cell6 = row.createCell(6);
		cell6.setCellValue("DRN Drafter");
		cell6.setCellStyle(styleBackground);

		try {
			int docRow = 0;
			for (GlobalSearchVO rowData : drnList) {
				row = sheet.createRow((docRow++) + rowIndex);

				Cell cell01 = row.createCell(0);
				cell01.setCellValue(rowData.getPrj_nm());
				cell01.setCellStyle(styleLeft);

				Cell cell11 = row.createCell(1);
				cell11.setCellValue(rowData.getDrn_no());
				cell11.setCellStyle(styleLeft);

				Cell cell21 = row.createCell(2);
				cell21.setCellValue(rowData.getDrn_title());
				cell21.setCellStyle(styleLeft);

				Cell cell31 = row.createCell(3);
				cell31.setCellValue(rowData.getDiscip_display());
				cell31.setCellStyle(styleLeft);

				Cell cell41 = row.createCell(4);
				String dateStr = getDateFormat(rowData.getReg_date());
				cell41.setCellValue(dateStr);
				cell41.setCellStyle(styleLeft);

				Cell cell51 = row.createCell(5);
				String as = rowData.getDrn_approval_status();
				
				switch(as){
		        	case "T":
		        		as = "임시저장";
	        			break;
	        		case "A":
	        			as = "검토중";
	        			break;
	        		case "E":
	        		case "1":
	        			as = "결재중";
	        			break;
	        		case "C":
	        			as = "결재완료";
	        			break;
	        		case "R":
	        			as = "반려됨";
	        			break;
	        		default:
	        			break;
				}
				cell51.setCellValue(as);
				cell51.setCellStyle(styleLeft);

				Cell cell61 = row.createCell(6);
				cell61.setCellValue(rowData.getDrnWriter());
				cell61.setCellStyle(styleLeft);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		
		
		response.reset();
		response.setContentType("ms-vnd/excel");
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + (URLEncoder.encode((excelName + "(" + nowDateStr + ")" + ".xlsx"), "UTF-8")) + "\"");
		response.setHeader("Content-Description", "JSP Generated Data");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		ServletOutputStream sw = response.getOutputStream();
		workbook.write(sw);
		sw.close();
		System.out.println(excelName + " written successfully");
		return;
			
	}
	
	public static String getDateFormat(String input) {
		
		String output = input;

		if(input == null) output = "-";
		
		if(input!=null && input.length() == 14)
			output = input.substring(0,4) + "-" +input.substring(4,6) + "-" + input.substring(6,8) + " " + input.substring(8,10) + ":" + input.substring(10,12) + ":" + input.substring(12,14);
		else if(input!=null && input.length() == 8)
			output = input.substring(0,4) + "-" +input.substring(4,6) + "-" + input.substring(6,8);
		
		return output;
	}
	
}


