package kr.co.doiloppa.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;


 

public class Sheet2ListHandler implements SheetContentsHandler{
	
	private boolean firstCellRow = false;
	private int currentCol = -1;
	
	//collenction 객체
	private List<String[]> rows;
	
	//collection에 추가될 객체 startRow애서 초기화 함
	private String[] row;
	//collection내 객체를 String[]로 잡았기 때문에 배열의 길이를 생성시 받도록 설계
	private int columnCnt = 0;
	//cell 이벤트 처리시 해당 cell의 데이터가 배열 어디에 저장되어야 할지 가리키는 pointer
	private int currColNum = 0;
	//외부 collection과 배열 size를 받기 위해 추가한 부분
	
	public Sheet2ListHandler(List<String[]> rows, int columnsCnt) {
		this.rows = rows;
		this.columnCnt = columnsCnt;		
	}
	
	//Row의 시작 부분에서 발생하는 이벤트를  처리하는 method
	@Override
	public void startRow(int rowNum) {
		this.row = new String[columnCnt];
		currColNum = 0;
	}
	
	//Row의 끝에서 발생한느 이벤츠를 처리하는 method
	public void endRow(int rowNum) {
		//cell 이벤트에서 담아놓은  row String[]를 collection 추가
		//데이터가 하나도 없는 row는 collection 추가하지 않도록 조건 추가
				
		boolean addFlag = false;
		for(String data:row) {
			if(!"".equals(data)) {
				addFlag = true;
			}
		}
		if(addFlag)rows.add(row);
	}
	
	//cell이벤트 발생시 해당 cell의 주소와 값을 받아옴	
	public void cell(String cellReference, String formattedValue, XSSFComment comment) {
		//엣셀 내용중 비어있는 셀은 제외학로 쓰여지기 빼문에 아래와 같이 체크후 빈셀은 ""로 채워서 자리를 채워줌
		int thisCol = (new CellReference(cellReference)).getCol();
		int missedCols = thisCol - currentCol - 1;
		for(int i=0;i<missedCols;i++) {
			row[currColNum++] = "";
		}
		currentCol = thisCol;
		row[currColNum++] = formattedValue == null ? "":formattedValue;
		//System.out.println("#################: row[currColNum]"+row[currColNum]);
		
	}

 
	@Override
	public void headerFooter(String arg0, boolean arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cell(String cellReference, String formattedValue) {
		// TODO Auto-generated method stub
		//엣셀 내용중 비어있는 셀은 제외학로 쓰여지기 빼문에 아래와 같이 체크후 빈셀은 ""로 채워서 자리를 채워줌
		int thisCol = (new CellReference(cellReference)).getCol();
		int missedCols = thisCol - currentCol - 1;
		for(int i=0;i<missedCols;i++) {
			row[currColNum++] = "";
		}
		currentCol = thisCol;
		row[currColNum++] = formattedValue == null ? "":formattedValue;
		//System.out.println("#################: row[currColNum]"+row[currColNum-1]);		
		
	}

	@Override
	public void endRow() {
		// TODO Auto-generated method stub
		//cell 이벤트에서 담아놓은  row String[]를 collection 추가
		//데이터가 하나도 없는 row는 collection 추가하지 않도록 조건 추가
				
		boolean addFlag = false;
		for(String data:row) {
			if(!"".equals(data)) {
				addFlag = true;
			}
		}
		if(addFlag)rows.add(row);		
		
	}
  

	public List<String[]> getRows() { return rows; }

	 
	
 	
		
	 
}