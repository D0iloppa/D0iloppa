package kr.co.hhi.common.util;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static final int BUFF_SIZE = 2048;
	
	public static void ExcelfileCreate(HttpServletResponse res, String titleList, String columnList, List<Map<String, Object>> dataList, String excelName) throws Exception{
		
		
		//workbook�� ���� 
		XSSFWorkbook workbook=new XSSFWorkbook();
		//sheet���� 
		XSSFSheet sheet=workbook.createSheet(excelName);
		//������ �� 
		XSSFRow row=null;
		//������ �� 
		XSSFCell cell=null;
		
		Font headerFont = workbook.createFont();
		headerFont.setColor(HSSFColor.WHITE.index);
		
		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerStyle.setFont(headerFont);

		XSSFCellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		

		//�Ķ���ͷ� �Ѿ�� titleList�� ù��° row�� set.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
		row = sheet.createRow(0);
//		sheet.autoSizeColumn(0);
		String[] titleArr=StringUtil.split(titleList, "|");
		for (int i = 0; i < titleArr.length-1; i++){
			cell = row.createCell(i);
			cell.setCellValue(titleArr[i]);
			cell.setCellStyle(headerStyle);
		}
		
		//�Ķ���ͷ� �Ѿ�� columnList�� �迭�� �����
		String[] columnArr=StringUtil.split(columnList, "|");

		//�Ķ���ͷ� �Ѿ�� data���� cell�� set
		for(int i=0; i<dataList.size(); i++){
//			sheet.autoSizeColumn(i);
			row = sheet.createRow(i+1);
			for(int j=0; j<columnArr.length-1; j++){
				cell = row.createCell(j);
				Object val = dataList.get(i).get(columnArr[j]);
				if(val != null){
					cell.setCellValue(val.toString());					
				}
				cell.setCellStyle(dataStyle);
			}
		}

		for(int i=0; i<columnArr.length-1; i++){
			sheet.autoSizeColumn((short)i);
			sheet.setColumnWidth(i, (sheet.getColumnWidth(i))+1024 );  // ���ٸ����δ� �÷��� width �� �����Ͽ� �� �÷��� ��.
		}
		res.setHeader("Set-Cookie", "fileDownload=true; path=/");
		
		res.setHeader("Content-Disposition","attachment;filename=" + (URLEncoder.encode((excelName+".xlsx"),"UTF-8")));
		res.setHeader("Content-Type", "application/vnd.ms-excel; charset=MS949");
		res.setHeader("Content-Description", "JSP Generated Data"); 
		res.setHeader("Content-Transfer-Encoding", "binary;"); 
		res.setHeader("Pragma", "no-cache;"); 
		res.setHeader("Expires", "-1;");
		ServletOutputStream sw = res.getOutputStream();
		workbook.write(sw);
		sw.close();
		
		
		return ;
	}
	
}
