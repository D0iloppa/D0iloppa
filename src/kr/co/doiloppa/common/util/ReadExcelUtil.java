package kr.co.doiloppa.common.util;




import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import	org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ReadExcelUtil {

	
	public static void main(String args[]){
		System.out.println(getReadExcel(1,1,"D:/down/test.xls"));
	}
	
	/**
	 * ���� ������ �о String���� ��ȯ �Ѵ�
	 * ���а��� | (������) ��
	 * @param _start_row  0���� ū ���� ��� ���� �ű� row���� �о�� ù��° row�� Ÿ��Ʋ�� �� ���� �����Ƿ� ù��° row�� ���� �ʵ��� �ϱ� ����
	 * @param _end_cell  0���� ū ���� ��� ���� �ű� cell������ �д´� 
	 * @param _path		���� ������ �ִ� ���
	 * @return
	 */
	public static String getReadExcel(int _start_row, int _end_cell, String _path){
		String	res	=	"";
		int		start_row	=	0;
		int		end_cell	=	0;
		
		try {
			POIFSFileSystem		fs			= new POIFSFileSystem(new FileInputStream(_path));
			
			//��ũ���� ����!      
			Workbook		workbook	= WorkbookFactory.create(fs);
//			HSSFWorkbook		workbook	= new HSSFWorkbook(fs);
			int					sheetNum	= workbook.getNumberOfSheets();

			for (int k = 0; k < sheetNum; k++) {

				Sheet sheet = workbook.getSheetAt(k);
//				HSSFSheet		sheet	= workbook.getSheetAt(k);
				int				rows	= sheet.getPhysicalNumberOfRows();
				
				// 0���� ū ���� ��� ���� �ű� row���� �о��
				// ù��° row�� Ÿ��Ʋ�� �� ���� �����Ƿ� ù��° row�� ���� �ʵ��� �ϱ� ����
				if(_start_row>=0){
					start_row	=	_start_row;
				}
				
				for (int r = start_row; r < rows; r++) {
				  // ��Ʈ�� ���� ���� �ϳ��� ����
					Row row   = sheet.getRow(r);
//				  HSSFRow row   = sheet.getRow(r);
	
				  if (row != null ) { 
				   
				   if( _end_cell >= 0 ){
					   end_cell		=	_end_cell;
				   }else{
					   end_cell		=	row.getPhysicalNumberOfCells();	
				   }
	
				   for (short c = 0; c < end_cell; c++) {
				    // �࿡���� ���� �ϳ��� �����Ͽ� �� Ÿ�Կ� ���� ó��
					 Cell cell  = row.getCell(c);
//				    HSSFCell cell  = row.getCell(c);
						if (cell != null) { 
							String value = null; 
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_FORMULA :
//								case HSSFCell.CELL_TYPE_FORMULA :
									//value = "FORMULA value=" + cell.getCellFormula();
									value =  cell.getCellFormula();
								break;
								case Cell.CELL_TYPE_NUMERIC :
//								case HSSFCell.CELL_TYPE_NUMERIC :
									//value = "NUMERIC value=" + cell.getNumericCellValue(); //double
									value =  ""+cell.getNumericCellValue() ; //double
								break;
								case Cell.CELL_TYPE_STRING :
//								case HSSFCell.CELL_TYPE_STRING :
									//value = "STRING value=" + cell.getStringCellValue(); //String
									value =  cell.getStringCellValue(); //String
								break;
								case Cell.CELL_TYPE_BLANK :
//								case HSSFCell.CELL_TYPE_BLANK :
									value = null;
								break;
								case Cell.CELL_TYPE_BOOLEAN :
//								case HSSFCell.CELL_TYPE_BOOLEAN :
									//value = "BOOLEAN value=" + cell.getBooleanCellValue(); //boolean
									value =  ""+ cell.getBooleanCellValue(); //boolean
								break;
								case Cell.CELL_TYPE_ERROR :
//								case HSSFCell.CELL_TYPE_ERROR :
									//value = "ERROR value=" + cell.getErrorCellValue(); // byte
									value = ""+cell.getErrorCellValue(); // byte
								break;
								default :
							} // switch
							
							res	+= value+"|";

					} // if
				   } // for
				  } // if
				} // for
			} // for			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return res;
		
	}
	
	
	
	
	/**
	 * ���� ������ �о String���� ��ȯ �Ѵ�
	 * ���а��� | (������) ��
	 * @param _start_row  0���� ū ���� ��� ���� �ű� row���� �о�� ù��° row�� Ÿ��Ʋ�� �� ���� �����Ƿ� ù��° row�� ���� �ʵ��� �ϱ� ����
	 * @param _end_cell  0���� ū ���� ��� ���� �ű� cell������ �д´� 
	 * @param _path		���� ������ �ִ� ���
	 * @return
	 */
	public static List<String> getReadExcelToList(int _start_row, int _end_cell, String _path){
		List<String> resArray	=	new	ArrayList<String>();
		
		int		start_row	=	0;
		int		end_cell	=	0;
		
		try {
//			POIFSFileSystem		fs			= new POIFSFileSystem(new FileInputStream(_path));
//			OPCPackage fs = OPCPackage.open(new FileInputStream(_path));

			
			//��ũ���� ����!   
//			Workbook workbook = WorkbookFactory.create(fs);
//			Workbook workbook2 = new XSSFWorkbook();
			Workbook workbook = new XSSFWorkbook(new FileInputStream(_path));
//			HSSFWorkbook		workbook	= new HSSFWorkbook(fs);
			int					sheetNum	= workbook.getNumberOfSheets();

			for (int k = 0; k < sheetNum; k++) {

				Sheet sheet = workbook.getSheetAt(k);
//				XSSFSheet sheet = workbook.getSheetAt(k);
//				HSSFSheet		sheed	= workbook.getSheetAt(k);
				int				rows	= sheet.getPhysicalNumberOfRows();
				
				// 0���� ū ���� ��� ���� �ű� row���� �о��
				// ù��° row�� Ÿ��Ʋ�� �� ���� �����Ƿ� ù��° row�� ���� �ʵ��� �ϱ� ����
				if(_start_row>=0){
					start_row	=	_start_row;
				}
				
				for (int r = start_row; r < rows; r++) {
				  // ��Ʈ�� ���� ���� �ϳ��� ����
					Row row = sheet.getRow(r);
//					XSSFRow row = sheet.getRow(r);
//				  HSSFRow row   = sheet.getRow(r);

				  //������������ �ӽ÷� ���� �Ѵ�.
				  String			parse_str	=	"";
					
				  if (row != null ) { 
				   
				   if( _end_cell >= 0 ){
					   end_cell		=	_end_cell;
				   }else{
					   end_cell		=	row.getPhysicalNumberOfCells();	
				   }
	
				   for (short c = 0; c < end_cell; c++) {
				    // �࿡���� ���� �ϳ��� �����Ͽ� �� Ÿ�Կ� ���� ó��
					   Cell cell = row.getCell(c);
//					   XSSFCell cell = row.getCell(c);
//				    HSSFCell cell  = row.getCell(c);
						if (cell != null) { 
							String value = null; 
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA :
//								case XSSFCell.CELL_TYPE_FORMULA :
//								case HSSFCell.CELL_TYPE_FORMULA :
									//value = "FORMULA value=" + cell.getCellFormula();
									value =  cell.getCellFormula();
								break;
								case Cell.CELL_TYPE_NUMERIC :
//								case XSSFCell.CELL_TYPE_NUMERIC :
//								case HSSFCell.CELL_TYPE_NUMERIC :
									//value = "NUMERIC value=" + cell.getNumericCellValue(); //double
									value =  ""+cell.getNumericCellValue() ; //double
								break;
								case Cell.CELL_TYPE_STRING :
//								case XSSFCell.CELL_TYPE_STRING :
//								case HSSFCell.CELL_TYPE_STRING :
									//value = "STRING value=" + cell.getStringCellValue(); //String
									value =  cell.getStringCellValue(); //String
								break;
								case Cell.CELL_TYPE_BLANK :
//								case XSSFCell.CELL_TYPE_BLANK :
//								case HSSFCell.CELL_TYPE_BLANK :
									value = null;
								break;
								case Cell.CELL_TYPE_BOOLEAN :
//								case XSSFCell.CELL_TYPE_BOOLEAN :
//								case HSSFCell.CELL_TYPE_BOOLEAN :
									//value = "BOOLEAN value=" + cell.getBooleanCellValue(); //boolean
									value =  ""+ cell.getBooleanCellValue(); //boolean
								break;
								case Cell.CELL_TYPE_ERROR :
//								case XSSFCell.CELL_TYPE_ERROR :
//								case HSSFCell.CELL_TYPE_ERROR :
									//value = "ERROR value=" + cell.getErrorCellValue(); // byte
									value = ""+cell.getErrorCellValue(); // byte
								break;
								default :
							} // switch
							
							//������ ���� ������ ���� ���ƶ�
							if(c == (end_cell-1)){
								parse_str	+= value;
							}else{
								parse_str	+= value+"|";	
							}
							

					} // if
				   } // for
				  } // if
				  
				  resArray.add(parse_str);
				  
				} // start_row for
			} // for			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return resArray;
		
	}
	
}
