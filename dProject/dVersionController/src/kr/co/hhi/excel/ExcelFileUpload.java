package kr.co.hhi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.ChineseDateFormat.Field;

import kr.co.hhi.model.PrjDocumentIndexVO;
import kr.co.hhi.model.PrjInfoVO;
import kr.co.hhi.model.UsersVO;
import kr.co.hhi.model.WbsCodeControlVO;
 

@Controller
@RequestMapping("/*")

public class ExcelFileUpload {
	

    // json 데이터로 응답을 보내기 위한 
    @Autowired
    MappingJackson2JsonView jsonView;
    
    // app.properties에 있는 upload path
    @Value("#{config['file.upload.path']}")
	private String UPLOAD_ROOT_PATH;
    // 템플릿 저장 위치
    @Value("#{config['file.upload.prj.template.path']}")
	private String TEMPLATE_PATH;
    // 임시 저장 위치
    @Value("#{config['file.upload.tempfile']}")
	private String TEMP_PATH;
    


	
	@RequestMapping(value = "prjInfoExcelUpload.do")
	@ResponseBody
	public ModelAndView prjInfoExcelUpload(HttpServletRequest request, Model model){
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		UsersVO sessioninfo = (UsersVO) session.getAttribute("login");
		
		mv.setViewName("/main");
		return mv;
	}
	
	@RequestMapping(value = "prjInfoExcelUploadProc.do")
	@ResponseBody
	public String prjInfoExcelUploadProc(HttpServletRequest request, Model model, HttpServletResponse response) throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
	    String retVal = "sucess";
		System.out.println("#################: excel upload start");
		//엑셀 파일 업로드
		String filePath = "c:/test/WbsCode1.xlsx";
		 
		File file = new File(filePath);
	 
		List<String[]> dataList=null;
		
		OPCPackage opc = null;
        try {
			opc = OPCPackage.openOrCreate(file);
			XSSFReader xssfReader = null;
			
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
		    ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			while(itr.hasNext()) {
				InputStream sheetStream = itr.next();
				InputSource sheetSource = new InputSource(sheetStream);
				
				//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
				//String[]배열을 몇개 사용할지 숫자
				Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 18);
				ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
				
				SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = null;
				saxParser = saxFactory.newSAXParser();
				//sax parse방식의 xmlReader를 생성
				XMLReader sheetParser = saxParser.getXMLReader();
				//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
				
				sheetParser.setContentHandler(handler);
				//위에서  sheet별로 생성한 inputSource를 parsing합니다.
				//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
				
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				file.delete();
				
				long start = System.currentTimeMillis(); //시작하는 시점 계산 
				
				List<String[]> excelDatas = sheet2ListHandler.getRows(); 
				
				for ( String[] dataRow : excelDatas ) { 					
					for (int j=0;j<dataRow.length; j++) {
						System.out.println("========= datarow["+j+"]======:"+dataRow[j]);						
					}
				}
											
				long end = System.currentTimeMillis(); //프로그램이 끝나는 시점 계산 
				//System.out.println( "실행 시간 : " + ( end - start ) / 1000.0 + "초" ); //실행 시간 계산 및 출력
												

			}
			
			
		} catch (InvalidFormatException e) {
			retVal = "failed";
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return retVal;
	}
	
	/**
		 * 1. 메소드명 : wbsExelUpload
		 * 2. 작성일: 2021. 12. 23.
		 * 3. 작성자: doil
		 * 4. 설명: 	엑셀파일을 불러와서 지원하지 않는 형식은 예외 처리하고, 
		 * 5. 수정일: doil
	 */
	@RequestMapping(value = "wbsExcelUpload.do", method = RequestMethod.POST)
	@ResponseBody 
	public ModelAndView wbsExcelUpload(@RequestParam("wbsExcelfile") MultipartFile file, HttpServletRequest request, WbsCodeControlVO wbscodecontrolVo){
	
		// 응답용 객체를 생성하고, jsonView 를 사용
		ModelAndView model = new ModelAndView();
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());


		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
			
		
		String root_path = request.getSession().getServletContext().getRealPath("/");
		
		String upload_path = UPLOAD_ROOT_PATH + wbscodecontrolVo.getPrj_id() + TEMPLATE_PATH;
		String filename = file.getOriginalFilename();
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
				opc = OPCPackage.openOrCreate(uploadedFile);
				XSSFReader xssfReader = null;
				
				xssfReader = new XSSFReader(opc);
				XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
				
				StylesTable styles = xssfReader.getStylesTable();
				
			    ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
				dataList = new ArrayList<String[]>();
				
				while(itr.hasNext()) {
					InputStream sheetStream = itr.next();
					InputSource sheetSource = new InputSource(sheetStream);
					
					//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
					//String[]배열을 몇개 사용할지 숫자
					//data, column수
					Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 5);
					ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
					
					SAXParserFactory saxFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = null;
					saxParser = saxFactory.newSAXParser();
					//sax parse방식의 xmlReader를 생성
					XMLReader sheetParser = saxParser.getXMLReader();
					//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
					
					sheetParser.setContentHandler(handler);
					//위에서  sheet별로 생성한 inputSource를 parsing합니다.
					//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
					
					sheetParser.parse(sheetSource);
					sheetStream.close();
					opc.close();
					uploadedFile.delete();
					
					
					List<String[]> excelDatas = sheet2ListHandler.getRows(); 
					
					
					// 엑셀파일을 읽어들이는데 성공하더라도,
					// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
					// head에 "WBS Code" 필드를 가진 경우에만 데이터를 읽도록 처리
					boolean findHead = false;
					
					List<String[]> result = new ArrayList<>();
					for(int i=0;i<excelDatas.size();i++) {
						String[] dataRow = excelDatas.get(i);
						if(dataRow[0].equals("WBS Code")) {
							findHead = true;
							continue;
						}							
						if(findHead) 
							result.add(dataRow);							
					}
					
					if(result.size() < 1) {
						model.addObject("status","엑셀에 읽어들일 data가 없습니다.");
						return model;
					}
					
					model.addObject("dataFromExcel",result);
					
													

				}
				
				
			
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		}
		model.addObject("status", "SUCCESS");
		
		return model;
	}

	
	@RequestMapping(value = "dciTemplateUpload.do", method = RequestMethod.POST)
	@ResponseBody 
	public ModelAndView dciTemplateUpload(MultipartHttpServletRequest mtRequest){
		
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("dciExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		


		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
			
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		//String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		String upload_path = UPLOAD_ROOT_PATH + TEMP_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
				opc = OPCPackage.openOrCreate(uploadedFile);
				XSSFReader xssfReader = null;
				
				xssfReader = new XSSFReader(opc);
				XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
				
				StylesTable styles = xssfReader.getStylesTable();
				
			    ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
				dataList = new ArrayList<String[]>();
				
				while(itr.hasNext()) {
					InputStream sheetStream = itr.next();
					InputSource sheetSource = new InputSource(sheetStream);
					
					//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
					//String[]배열을 몇개 사용할지 숫자
					//data, column수
					Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 20);
					ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
					
					SAXParserFactory saxFactory = SAXParserFactory.newInstance();
					SAXParser saxParser = null;
					saxParser = saxFactory.newSAXParser();
					//sax parse방식의 xmlReader를 생성
					XMLReader sheetParser = saxParser.getXMLReader();
					//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
					
					sheetParser.setContentHandler(handler);
					//위에서  sheet별로 생성한 inputSource를 parsing합니다.
					//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
					
					sheetParser.parse(sheetSource);
					sheetStream.close();
					opc.close();
					uploadedFile.delete();
					
					
					List<String[]> excelDatas = sheet2ListHandler.getRows(); 
					
					
					// 엑셀파일을 읽어들이는데 성공하더라도,
					// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
					// head에 "DOCUMENT NO" 필드를 가진 경우에만 데이터를 읽도록 처리
					boolean findHead = false;
					
					List<String[]> result = new ArrayList<>();
					//System.out.println("엑셀 row 수 : " + excelDatas.size());
					for(int i=0;i<excelDatas.size();i++) {
						//System.out.println(i+"");
						String[] dataRow = excelDatas.get(i);
						if(!findHead && dataRow[0].equals("DOCUMENT NO")) {
							findHead = true;
							continue;
						}							
						if(findHead && !dataRow[0].equals("")) 
							result.add(dataRow);
						
						//System.out.println(dataRow[0]);
					}
					
					if(result.size() < 1) {
						model.addObject("status","엑셀에 읽어들일 data가 없습니다.");
						return model;
					}
					
					System.out.println("결과값 파싱 완료");
					model.addObject("dataFromExcel",result);
					
						

				}
				
				
			
			
		} catch (Exception e) {
			System.out.println("실패");
			System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		
		
		
		model.addObject("status", "SUCCESS");
		
		return model;
	}
	
	
	/**
	*
	*
	*
	* DATA MIGRATION 엑셀 업로드 
	*
	*
	*
	*/
	@RequestMapping(value = "migCodeSettingsUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migCodeSettingsUpload(MultipartHttpServletRequest mtRequest){
		
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			/* 다중 시트
			 * while(itr.hasNext()) {
			 * 
			 * }
			 */
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 17);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("set_code_type") && dataRow[5].toLowerCase().equals("set_desc")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migCordocInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migCordocInfoUpload(MultipartHttpServletRequest mtRequest){
		
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 35);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[12].toLowerCase().equals("ref_corr_doc_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migDisciplineFolderInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migDisciplineFolderInfoUpload(MultipartHttpServletRequest mtRequest){
		
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList = null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 9);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("discip_code_id") && dataRow[2].toLowerCase().equals("discip_folder_seq")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
			
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		return model;
	}
	
	@RequestMapping(value = "migDisciplineInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migDisciplineInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 10);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("discip_code_id") && dataRow[2].toLowerCase().equals("discip_code")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migDocumentIndexUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migDocumentIndexUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 22);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//List<PrjDocumentIndexVO> result2 = new ArrayList<>();
			
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("folder_id") && dataRow[4].toLowerCase().equals("doc_no")) {
					findHead = true;
					continue;
				}
				if(findHead) 
					result.add(dataRow);
				/*
				if(findHead) {
					int idx = 0;
					PrjDocumentIndexVO vo = new PrjDocumentIndexVO();
					vo.setPrj_id(dataRow[idx++]);
					
					// result.add(dataRow);
					result2.add(vo);
				}
				*/
					
					
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEmailAttachInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEmailAttachInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 14);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("email_type_id") && dataRow[2].toLowerCase().equals("email_id") && dataRow[3].toLowerCase().equals("doc_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEmailReceiptInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEmailReceiptInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 9);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("email_type_id") && dataRow[2].toLowerCase().equals("email_id") && dataRow[3].toLowerCase().equals("email_receiver_addr")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEmailSendInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEmailSendInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 18);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("email_type_id") && dataRow[2].toLowerCase().equals("email_id") && dataRow[3].toLowerCase().equals("subject")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEmailTypeUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEmailTypeUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 10);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("email_type_id") && dataRow[2].toLowerCase().equals("email_type") && dataRow[3].toLowerCase().equals("email_feature")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEmailTypeConUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEmailTypeConUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 16);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			uploadedFile.delete();
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("email_type_id") && dataRow[2].toLowerCase().equals("email_sender_addr") && dataRow[10].toLowerCase().equals("contents")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEngInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEngInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 16);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("folder_id") && dataRow[4].toLowerCase().equals("discip_code_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migEngStepUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migEngStepUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 15);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("step_code_id") && dataRow[4].toLowerCase().equals("plan_date")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}

	@RequestMapping(value = "migFolderAuthUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migFolderAuthUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 13);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("folder_id") && dataRow[2].toLowerCase().equals("auth_lvl1") && dataRow[3].toLowerCase().equals("auth_val") && dataRow[4].toLowerCase().equals("folder_auth_add_yn")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migFolderInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migFolderInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 12);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("folder_id") && dataRow[2].toLowerCase().equals("parent_folder_id") && dataRow[3].toLowerCase().equals("folder_path") && dataRow[4].toLowerCase().equals("folder_nm")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migGendocInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migGendocInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 8);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("folder_id") && dataRow[4].toLowerCase().equals("reg_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migGroupMemberUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migGroupMemberUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 8);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			uploadedFile.delete();
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("group_id") && dataRow[2].toLowerCase().equals("user_id") && dataRow[3].toLowerCase().equals("group_desc") && dataRow[4].toLowerCase().equals("reg_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migPrjInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migPrjInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 13);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("prj_no") && dataRow[2].toLowerCase().equals("prj_nm") && dataRow[3].toLowerCase().equals("prj_full_nm") && dataRow[4].toLowerCase().equals("prj_hidden_yn")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migPrjMemberUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migPrjMemberUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 7);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("user_id") && dataRow[2].toLowerCase().equals("dcc_yn") && dataRow[3].toLowerCase().equals("reg_id") && dataRow[4].toLowerCase().equals("reg_date")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migProInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migProInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 19);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("folder_id") && dataRow[4].toLowerCase().equals("discip_code_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migProStepUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migProStepUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 15);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("step_code_id") && dataRow[4].toLowerCase().equals("plan_date")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migProcessInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migProcessInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 13);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("process_id") && dataRow[2].toLowerCase().equals("class_lvl") && dataRow[3].toLowerCase().equals("process_type") && dataRow[4].toLowerCase().equals("process_desc")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migSdcInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migSdcInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 16);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("folder_id") && dataRow[4].toLowerCase().equals("discip_code_id")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migSdcStepUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migSdcStepUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 15);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("step_code_id") && dataRow[4].toLowerCase().equals("plan_date")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migStrFileUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migStrFileUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 12);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("tr_id") && dataRow[2].toLowerCase().equals("doc_id") && dataRow[3].toLowerCase().equals("rev_id") && dataRow[4].toLowerCase().equals("rev_no")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migStrInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migStrInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 24);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("tr_id") && dataRow[2].toLowerCase().equals("tr_no") && dataRow[3].toLowerCase().equals("itr_no") && dataRow[4].toLowerCase().equals("tr_subject")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migSystemPrefixUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migSystemPrefixUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 9);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("system_prefix_type") && dataRow[2].toLowerCase().equals("system_prefix_id") && dataRow[3].toLowerCase().equals("prefix") && dataRow[4].toLowerCase().equals("prefix_desc")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migTrFileUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migTrFileUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 12);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("tr_id") && dataRow[2].toLowerCase().equals("doc_id") && dataRow[3].toLowerCase().equals("rev_id") && dataRow[4].toLowerCase().equals("rev_no")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migTrInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migTrInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 24);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("tr_id") && dataRow[2].toLowerCase().equals("tr_no") && dataRow[3].toLowerCase().equals("itr_no") && dataRow[4].toLowerCase().equals("tr_subject")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migVdrInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migVdrInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 20);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[9].toLowerCase().equals("po_doc_no") && dataRow[10].toLowerCase().equals("po_doc_title")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migVdrStepUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migVdrStepUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 15);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("doc_id") && dataRow[2].toLowerCase().equals("rev_id") && dataRow[3].toLowerCase().equals("step_code_id") && dataRow[4].toLowerCase().equals("plan_date")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migVtrFileUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migVtrFileUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 12);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("tr_id") && dataRow[2].toLowerCase().equals("doc_id") && dataRow[3].toLowerCase().equals("rev_id") && dataRow[4].toLowerCase().equals("rev_no")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migVtrInfoUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migVtrInfoUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 24);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("tr_id") && dataRow[2].toLowerCase().equals("tr_no") && dataRow[3].toLowerCase().equals("itr_no") && dataRow[4].toLowerCase().equals("tr_subject")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migWbsCodeUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migWbsCodeUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();

			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 20);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("prj_id") && dataRow[1].toLowerCase().equals("wbs_code_id") && dataRow[2].toLowerCase().equals("wbs_code") && dataRow[3].toLowerCase().equals("wbs_desc") && dataRow[4].toLowerCase().equals("doc_no")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	@RequestMapping(value = "migUsersUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView migUsersUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("migExcelfile");
		
		//System.out.println("UPLOADED FILENAME : " + file.getOriginalFilename());
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 26);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(!findHead && dataRow[0].toLowerCase().equals("user_id") && dataRow[1].toLowerCase().equals("user_kor_nm") && dataRow[2].toLowerCase().equals("user_eng_nm") && dataRow[3].toLowerCase().equals("user_address") && dataRow[4].toLowerCase().equals("user_group_nm")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
	
	// DICTIONARY SHEARCH 엑셀 업로드
	@RequestMapping(value = "dictionaryUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView dictionaryUpload(MultipartHttpServletRequest mtRequest){
		ModelAndView model = new ModelAndView();
		MultipartFile file = mtRequest.getFile("dictionaryExcelfile");
		
		// 업로드한 파일의 확장자가 xlsx가 아닌경우
		List<String> canDoExtensionList = Arrays.asList(new String [] {"xlsx"}); 
		String[] getExtension = file.getOriginalFilename().split("\\.");
		String extension = getExtension[getExtension.length-1];
		
		//System.out.println("EXTENSION : " + extension);
		
		if(!canDoExtensionList.contains(extension)) {
			model.addObject("status", "지원하지 않는 파일 형식");
			return model;
		}
		
		String root_path = mtRequest.getSession().getServletContext().getRealPath("/");
		String prj_id = (String) mtRequest.getParameter("prj_id");
		
		String filename = file.getOriginalFilename();
		String upload_path = UPLOAD_ROOT_PATH + prj_id + TEMPLATE_PATH;
		
		// 파일 업로드
		File dir = new File(upload_path);
		dir.mkdirs();
		File uploadTarget = new File(upload_path + filename);
		try {
			// /resource/upload/경로에 파일 업로드
			file.transferTo(uploadTarget);
			// 업로드된 파일을 지목
			File uploadedFile = new File(upload_path + filename);
			
			List<String[]> dataList=null;
			
			OPCPackage opc = null;
			opc = OPCPackage.openOrCreate(uploadedFile);
			
			XSSFReader xssfReader = null;
			xssfReader = new XSSFReader(opc);
			XSSFReader.SheetIterator itr = (SheetIterator) xssfReader.getSheetsData();
			
			StylesTable styles = xssfReader.getStylesTable();
			
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
			dataList = new ArrayList<String[]>();
			
			InputStream sheetStream = itr.next();
			InputSource sheetSource = new InputSource(sheetStream);
			//Sheet2ListHandler은 엑셀 Data를 가져와서 SheetContentHandler(Interface)를  재정의 해서 만든 Class
			//String[]배열을 몇개 사용할지 숫자
			//data, column수
			Sheet2ListHandler sheet2ListHandler = new Sheet2ListHandler(dataList, 4);
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheet2ListHandler, true);
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = null;
			saxParser = saxFactory.newSAXParser();
			//sax parse방식의 xmlReader를 생성
			XMLReader sheetParser = saxParser.getXMLReader();
			//xml reader에 row와 cell 이벤트를 생성하는 핸들러르 설정한 후
			
			sheetParser.setContentHandler(handler);
			//위에서  sheet별로 생성한 inputSource를 parsing합니다.
			//이 과정에서 handler는 row와 cell 이벤트를 생성하고 생성된 이벤트는 Sheet2ListHandler가 받아서 처리 합니다.
			
			try {
				sheetParser.parse(sheetSource);
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}catch (Exception e) {
				sheetStream.close();
				opc.close();
				uploadedFile.delete();
			}
			
			List<String[]> excelDatas = sheet2ListHandler.getRows();
			// 엑셀파일을 읽어들이는데 성공하더라도,
			// 관련없는 엑셀파일을 읽은 경우가 있을 수 있어서
			// head에 "prj_id" 필드를 가진 경우에만 데이터를 읽도록 처리
			boolean findHead = false;
			
			List<String[]> result = new ArrayList<>();
			//System.out.println("엑셀 row 수 : " + excelDatas.size());
			for(int i=0;i<excelDatas.size();i++) {
				//System.out.println(i+"");
				String[] dataRow = excelDatas.get(i);
				if(dataRow[3].toLowerCase().equals("description")) {
					findHead = true;
					continue;
				}							
				if(findHead) 
					result.add(dataRow);
				
//				System.out.println(dataRow[0]);
			}
			
			if(result.size() < 1) {
				model.addObject("status","테이블 컬럼 형식이 맞지않는 파일입니다.");
				return model;
			}
			//System.out.println("결과값 파싱 완료");
			model.addObject("dataFromExcel",result);
		}catch (Exception e) {
			//System.out.println("실패");
			//System.out.println(e.getMessage());
			model.addObject("status", e.getMessage());
			return model;
		}
		model.addObject("status","SUCCESS");
		
		return model;
	}
}


