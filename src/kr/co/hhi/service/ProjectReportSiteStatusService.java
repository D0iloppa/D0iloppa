package kr.co.hhi.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import kr.co.hhi.common.abstractinfo.AbstractService;
import kr.co.hhi.common.base.CommonConst;
import kr.co.hhi.model.PrjReportSiteVo;
import kr.co.hhi.model.ReportVo;

@Service
public class ProjectReportSiteStatusService extends AbstractService {

	public List<Object> getReportData(ReportVo reportVo) {
		List<Object> result = new ArrayList<Object>();

		String columnList = "";
		String keyList="";
		String columnInfo="";
		
		String ExcelValueData="";
		
		List<String> valueList = new ArrayList<String>();

		JSONArray columnJsonList = new JSONArray();
		JSONArray valueJsonList = new JSONArray();

		List<PrjReportSiteVo> value = new ArrayList<PrjReportSiteVo>();
		int getSiteStepCount = selectOne("prjReportSiteSqlMap.getSiteStepCount",reportVo);
		
		if ( reportVo.getReportType().equals("4_1")) { 
			//SDCI LIST
			value = selectList("prjReportSiteSqlMap.PrjReportSiteData_1",reportVo);

			columnList="[{\"title\":\"Serial No.\", \"field\":\"SerialNo\" , \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Document No\", \"field\":\"DocumentNo\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},{\"title\":\"Rev No.\", \"field\":\"RevNo\", \"minWidth\":\"68\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Issue Purpose\", \"field\":\"IssuePurpose\", \"minWidth\":\"110\"},{\"title\":\"Type\", \"field\":\"Type\", \"minWidth\":\"90\"},@@@@@@,{\"title\":\"WBS Code\", \"field\":\"WBSCode\", \"minWidth\":\"110\"},{\"title\":\"Doc. Type\", \"field\":\"DocType\", \"minWidth\":\"106\"},{\"title\":\"Size\", \"field\":\"Size\", \"minWidth\":\"65\"},{\"title\":\"Designer\", \"field\":\"Designer\", \"minWidth\":\"106\"},{\"title\":\"Latest TR No.\", \"field\":\"LatestTRNo\", \"minWidth\":\"108\"},{\"title\":\"Latest Issue Date\", \"field\":\"LatestIssueDate\", \"minWidth\":\"136\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"}]";
			columnInfo="Serial No.###Discipline###Document No###Rev No.###Title###Issue Purpose###Type###@@@@@@###WBS Code###Doc. Type###Size###Designer###Latest TR No.###Latest Issue Date###Remark";
			keyList="SerialNo###Discipline###DocumentNo###RevNo###Title###IssuePurpose###Type###@@@@@@###WBSCode###DocType###Size###Designer###LatestTRNo###LatestIssueDate###Remark";
			
			String columnString="";
			String columnInfoString="";
			String keyString="";
			
			if ( value.size() == 0 ) {
				columnList=columnList.replaceAll("@@@@@@", "empty");
				columnInfo=columnInfo.replaceAll("@@@@@@", "empty");
				keyList=keyList.replaceAll("@@@@@@", "empty");
			}
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				
				if ( i == 0 ) {
					String step = bean.getStep();
					String step_per = bean.getStep_per();
					
					String stepTmp[] = step.split("@,@");
					String step_perTmp[] = step_per.split("@,@");
					
					for ( int j=0; j<stepTmp.length; ++j ) {
						columnString=columnString+"{\"title\":\""+stepTmp[j]+"("+step_perTmp[j]+"%)\", \"field\":\""+stepTmp[j]+"("+step_perTmp[j]+"%)\", \"minWidth\":\"170\"},";
						columnInfoString=columnInfoString+stepTmp[j]+"("+step_perTmp[j]+"%)###";
						keyString=keyString+stepTmp[j]+"("+step_perTmp[j]+"%)###";
					}
					
					if ( columnString.length() > 0 ) {
						columnString=columnString.substring(0, columnString.length()-1);
						columnInfoString=columnInfoString.substring(0, columnInfoString.length()-3);
						keyString=keyString.substring(0, keyString.length()-3);
					}
					columnList=columnList.replaceAll("@@@@@@", columnString);
					columnInfo=columnInfo.replaceAll("@@@@@@", columnInfoString);
					keyList=keyList.replaceAll("@@@@@@", keyString);
				}
				
				String plan_date = bean.getPlan_date();
				String fore_date = bean.getFore_date();
				String actual_date = bean.getActual_date();
				
				String[] tmp1 = plan_date.split("@,@");
				String[] tmp2 = fore_date.split("@,@");
				String[] tmp3 = actual_date.split("@,@");
				
				for ( int j=0; j<tmp1.length; ++j ) {
					System.out.println("tmp1["+j+"]"+tmp1[j]);
				}
				String val = (i+1) +"@@@"+bean.getDiscipline()+"@@@"+bean.getDoc_no()+"@@@"+bean.getRev_no()+"@@@"+bean.getTitle()+"@@@"+bean.getTr_issuepur()+"@@@"+bean.getType()+"@@@";
				for ( int j=0; j<tmp1.length; ++j ) {
					val = val + CommonConst.stringdateTOYYYYMMDD(tmp1[j])+"@,@"+CommonConst.stringdateTOYYYYMMDD(tmp2[j])+"@,@"+CommonConst.stringdateTOYYYYMMDD(tmp3[j])+"@@@";
				}
				val = val + bean.getWbs_code()+"@@@"+bean.getDoc_type()+"@@@"+bean.getDoc_size()+"@@@"+bean.getDeginer()+"@@@"+bean.getTr_no()+"@@@"+bean.getSend_date()+"@@@"+bean.getRemark();
				valueList.add(val);
				ExcelValueData=ExcelValueData+val+"@#@#@";
			}
			
		} else if ( reportVo.getReportType().equals("4_2")) { 
			//SDCI Detail LIST
			columnList="[{\"title\":\"Ser.No\", \"field\":\"SerNo\", \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Document No\", \"field\":\"DocumentNo\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},{\"title\":\"Rev No\", \"field\":\"RevNo\", \"minWidth\":\"68\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Doc.Type\", \"field\":\"DocType\", \"minWidth\":\"106\"},{\"title\":\"Category\", \"field\":\"Category\", \"minWidth\":\"80\"},{\"title\":\"Paper Size\", \"field\":\"PaperSize\", \"minWidth\":\"84\"},{\"title\":\"WBS Code\", \"field\":\"WBSCode\", \"minWidth\":\"110\"},{\"title\":\"Designer\", \"field\":\"Designer\", \"minWidth\":\"106\"},{\"title\":\"Transmittal No\", \"field\":\"TransmittalNo\", \"minWidth\":\"102\"},{\"title\":\"Issued RevNo\", \"field\":\"IssuedRevNo\", \"minWidth\":\"100\"},{\"title\":\"Issued Date\", \"field\":\"IssuedDate\", \"minWidth\":\"90\"},{\"title\":\"Return Status\", \"field\":\"ReturnStatus\", \"minWidth\":\"102\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"}";
	    	columnInfo="Ser.No###Discipline###Document No###Rev No###Title###Doc.Type###Category###Paper Size###WBSCode###Designer###Transmittal No###Issued RevNo###Issued Date###Return Status###Remark###";
	    	keyList="SerNo###Discipline###DocumentNo###RevNo###Title###DocType###Category###PaperSize###WBSCode###Designer###TransmittalNo###IssuedRevNo###IssuedDate###ReturnStatus###Remark###";

	    	if(reportVo.getDel_yn().equals("1")) {
	    		columnList += ",{\"title\":\"DELETE YN\", \"field\":\"del_yn\", \"minWidth\":\"80\"}";
	    		columnInfo += "del_yn###";
	    		keyList += "del_yn###";
	    	}
	    	columnList += ",@@@@@@]";
	    	columnInfo += "@@@@@@";
	    	keyList += "@@@@@@";
	    	
	    	value = selectList("prjReportSiteSqlMap.PrjReportSiteData_2",reportVo);
	    	
	    	String columnString1="";
	    	String columnString2="";
	    	
	    	String columnInfoString1="";
	    	String columnInfoString2="";
	    	
			String keyString1="";
			String keyString2="";
			
			if ( value.size() == 0 ) {
				columnList=columnList.replaceAll("@@@@@@", "empty");
				keyList=keyList.replaceAll("@@@@@@", "empty");
			}
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				
				if ( i == 0 ) {
					String step = bean.getStep();
					String stepTmp[] = step.split("@,@");
					
					for ( int j=0; j<stepTmp.length; ++j ) {
						columnString1=columnString1+"{\"title\":\""+stepTmp[j]+" Plan\", \"field\":\""+stepTmp[j]+"Plan\", \"minWidth\":\"170\"},";
						columnString2=columnString2+"{\"title\":\""+stepTmp[j]+" Actual\", \"field\":\""+stepTmp[j]+"Actual\", \"minWidth\":\"170\"},";
						
						columnInfoString1=columnInfoString1+stepTmp[j]+" Plan###";
						columnInfoString2=columnInfoString2+stepTmp[j]+" Actual###";
						
						keyString1=keyString1+stepTmp[j]+"Plan###";
						keyString2=keyString2+stepTmp[j]+"Actual###";
					}
					
					if ( columnString1.length() > 0 ) {
						columnString1=columnString1.substring(0, columnString1.length()-1);
						columnString2=columnString2.substring(0, columnString2.length()-1);
						
						columnInfoString1=columnInfoString1.substring(0, columnInfoString1.length()-3);
						columnInfoString2=columnInfoString2.substring(0, columnInfoString2.length()-3);
						
						keyString1=keyString1.substring(0, keyString1.length()-3);
						keyString2=keyString2.substring(0, keyString2.length()-3);
						
						columnString1=columnString1+"@@@@@@";
						
						columnInfoString1=columnInfoString1+"###@@@@@@";
						keyString1=keyString1+"###@@@@@@";
					}
					columnList=columnList.replaceAll("@@@@@@", columnString1);
					columnList=columnList.replaceAll("@@@@@@", columnString2);
					
					columnInfo=columnInfo.replaceAll("@@@@@@", columnInfoString1);
					columnInfo=columnInfo.replaceAll("@@@@@@", columnInfoString2);
					
					keyList=keyList.replaceAll("@@@@@@", keyString1);
					keyList=keyList.replaceAll("@@@@@@", keyString2);
				}

				String plan_date = bean.getPlan_date();
				String fore_date = bean.getFore_date();
				String actual_date = bean.getActual_date();

				String[] tmp1 = plan_date.split("@,@");
				String[] tmp2 = fore_date.split("@,@");
				String[] tmp3 = actual_date.split("@,@");

				String val = (i+1) +"@@@"+bean.getDiscipline()+"@@@"+bean.getDoc_no()+"@@@"+bean.getRev_no()+"@@@"+bean.getTitle()+"@@@"+bean.getDoc_type()+"@@@"+bean.getCategory()+"@@@"+bean.getDoc_size()+"@@@"+bean.getWbs_code()+"@@@"+bean.getDeginer()+"@@@"+bean.getTr_no()+"@@@"+bean.getIssued_rev_no()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getIssued_send_date())+"@@@"+bean.getRtn_status_code()+"@@@"+bean.getRemark()+"@@@";

		    	if(reportVo.getDel_yn().equals("1")) {
		    		val += bean.getDel_yn()+"@@@";
		    	}
		    	
				for ( int j=0; j<tmp1.length; ++j ) {
					val = val + CommonConst.stringdateTOYYYYMMDD(tmp1[j])+"@@@";
				}
				
				for ( int j=0; j<tmp3.length; ++j ) {
					val = val + CommonConst.stringdateTOYYYYMMDD(tmp3[j])+"@@@";
				}
				
				if ( tmp1.length > 0 ) {
					val = val.substring(0, val.length()-3);
				}
				valueList.add(val);
				ExcelValueData=ExcelValueData+val+"@#@#@";
			}
			
		} else if ( reportVo.getReportType().equals("4_3")) { 
			//SDCI History List
			columnList="[{\"title\":\"SerialNo\", \"field\":\"serialNo\", \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Documnet No\", \"field\":\"documentNo\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},"
					+ "{\"title\":\"Title\", \"field\":\"title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Work Step\", \"field\":\"workStep\", \"minWidth\":\"100\"},";
			for(int keyIdx = 0; keyIdx<20; keyIdx++) {
				columnList+= String.format(
						"{\"title\":\"Rev.%d\", \"columns\":[ {\"title\":\"Issue\", \"columns\":[ {\"title\":\"TR No\", \"field\":\"trNoI%d\", \"minWidth\":\"60\"}]},"
						+ "{\"title\":\"Return\", \"columns\":[{\"title\":\"TR No\", \"field\":\"trNoR%d\", \"minWidth\":\"60\"}]},{\"title\":\"RevNo\", \"field\":\"revNo%d\", \"minWidth\":\"60\"}]},"
						, keyIdx,keyIdx,keyIdx,keyIdx);
			}	
			columnList += "]";
			
			keyList="serialNo###discipline###documentNo###title###workStep";
			for(int keyIdx = 0; keyIdx<20; keyIdx++) {
				keyList+= String.format("###trNoI%d###trNoR%d###revNo%d", keyIdx,keyIdx,keyIdx);
			}
			
			
			columnInfo="";
			value = selectList("prjReportSiteSqlMap.PrjReportSiteData_3",reportVo);

			Map<String,List<PrjReportSiteVo>> groupByDocNoList = new HashMap<>();
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				
				String doc_no = bean.getDoc_no();
				
				List<PrjReportSiteVo> tmpList = groupByDocNoList.get(doc_no);
				if(tmpList == null) tmpList = new ArrayList<>();
				tmpList.add(bean);
				
				groupByDocNoList.put(doc_no, tmpList);

			}
			
			//int serialNo = 1;
			value = new ArrayList<>();
			for (Map.Entry<String, List<PrjReportSiteVo>> entry : groupByDocNoList.entrySet()) {
			    
				
			    List<PrjReportSiteVo> docList = entry.getValue();
			    PrjReportSiteVo metaVo = docList.get(0);
			    
			    //String row = (serialNo++) +"@@@"+metaVo.getDiscipline()+"@@@"+metaVo.getDoc_no()+"@@@"+metaVo.getTitle()+"@@@"+metaVo.getStep()+"@@@";
			    String row = "@@@"+metaVo.getDiscipline()+"@@@"+metaVo.getDoc_no()+"@@@"+metaVo.getTitle()+"@@@"+"@%workstep%@"+"@@@";
			    
			   int existCnt = 0;
			   int maxStepcode = -1;
			   String maxStep = "";
			   
			   for(int revIdx=0;revIdx<20;revIdx++) {
				   if(revIdx >= docList.size()) continue;
				   else {
					   PrjReportSiteVo item = docList.get(revIdx);
					   if((item.getIssue_tr_no()!=null && !item.getIssue_tr_no().equals("-")) || (item.getReturn_tr_no()!=null && !item.getReturn_tr_no().equals("-"))) {
						   row += item.getIssue_tr_date()+"@,@"+item.getIssue_tr_no()+"@@@"+item.getReturn_tr_date()+"@,@"+item.getReturn_tr_no()+"@@@"+item.getRev_no()+"@@@";
						   int get_stepOrder = item.getStep_order();
						   if(get_stepOrder > maxStepcode) {
							   maxStepcode = get_stepOrder;
							   maxStep = item.getStep();
						   }
						   existCnt++;
					   }
				   }
			   }
			   row = row.replaceAll("@%workstep%@", maxStep);
			   
			   for(int remainIdx=0;remainIdx < 20-existCnt ; remainIdx++) {
				   row += "-@,@-@@@-@,@-@@@-@@@";
			   }
			  
			    
				valueList.add(row);
				value.add(metaVo);
			}
			
			Collections.sort(valueList);
			
			int rowIdx = 0;
			for(String row : valueList) {
				row = (rowIdx+1) + row;
				valueList.set(rowIdx++, row);
				ExcelValueData=ExcelValueData+row+"@#@#@";
			}
		
		} else if ( reportVo.getReportType().equals("4_4")) { 
			//SDCI Behind List
			value = selectList("prjReportSiteSqlMap.PrjReportSiteData_4",reportVo);

			columnList="[{\"title\":\"Serial No\", \"field\":\"SerialNo\", \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Document No\", \"field\":\"DocumentNo\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},{\"title\":\"Rev No\", \"field\":\"RevNo\", \"minWidth\":\"68\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Category\", \"field\":\"Category\", \"minWidth\":\"80\"},{\"title\":\"Plan Date\", \"field\":\"PlanDate\", \"minWidth\":\"80\"},{\"title\":\"Actual Date\", \"field\":\"ActualDate\", \"minWidth\":\"80\"},{\"title\":\"Fore Date\", \"field\":\"ForeDate\", \"minWidth\":\"80\"},{\"title\":\"Delay Days\", \"field\":\"DelayDays\", \"minWidth\":\"90\"},{\"title\":\"WBS Code\", \"field\":\"WBSCode\", \"minWidth\":\"110\"},{\"title\":\"Designer\", \"field\":\"Designer\", \"minWidth\":\"106\"},{\"title\":\"Work Step\", \"field\":\"WorkStep\", \"minWidth\":\"84\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"}]";
			columnInfo="Serial No###Discipline###Document No###Rev No###Title###Category###Plan Date###Actual Date###Fore Date###Delay Days###WBS Code###Designer###Work Step###Remark";
			keyList="SerialNo###Discipline###DocumentNo###RevNo###Title###Category###PlanDate###ActualDate###ForeDate###DelayDays###WBSCode###Designer###WorkStep###Remark";
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				String val = (i+1) +"@@@"+bean.getDiscipline()+"@@@"+bean.getDoc_no()+"@@@"+bean.getRev_no()+"@@@"+bean.getTitle()+"@@@"+bean.getCategory()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getPlan_date())+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getActual_date())+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getFore_date())+"@@@"+bean.getDelay_days()+"@@@"+bean.getWbs_code()+"@@@"+bean.getDeginer()+"@@@"+bean.getWork_step()+"@@@"+bean.getRemark();
				valueList.add(val);
				ExcelValueData=ExcelValueData+val+"@#@#@";
			}
		} else if ( reportVo.getReportType().equals("4_5")) { 
			//SDCI Transmittal List
			
			value = selectList("prjReportSiteSqlMap.PrjReportSiteData_5",reportVo);
			
			if ( reportVo.getTrOrder().equals("1")) {
				columnList="[{\"title\":\"TR No\", \"field\":\"TRNo\", \"minWidth\":\"80\"},{\"title\":\"Rev No\", \"field\":\"RevNo\", \"minWidth\":\"68\"},{\"title\":\"TR Date\", \"field\":\"TRDate\", \"minWidth\":\"80\"},{\"title\":\"IN / OUT\", \"field\":\"IN/OUT\", \"minWidth\":\"80\"},{\"title\":\"TR Issue purpose\", \"field\":\"TRIssuepurpose\", \"minWidth\":\"114\"},{\"title\":\"Return Status\", \"field\":\"ReturnStatus\", \"minWidth\":\"96\"},{\"title\":\"Originator\", \"field\":\"Originator\", \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"TR Remark\", \"field\":\"TRRemark\", \"minWidth\":\"82\"},{\"title\":\"Document No\", \"field\":\"DocumentNo\", \"minWidth\":\"98\", \"hozAlign\":\"left\"},{\"title\":\"Current RevNo\", \"field\":\"CurrentRevNo\", \"minWidth\":\"102\"},{\"title\":\"Category\", \"field\":\"Category\", \"minWidth\":\"80\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"}]";
				columnInfo="TR No###Rev No###TR Date###IN / OUT###TR Issue purpose###Return Status###Originator###Discipline###TR Remark###Document No###Current RevNo###Category###Title###Remark";
				keyList="TRNo###RevNo###TRDate###IN/OUT###TRIssuepurpose###ReturnStatus###Originator###Discipline###TRRemark###DocumentNo###CurrentRevNo###Category###Title###Remark";
				
				for ( int i=0; i<value.size(); ++i ) {
					PrjReportSiteVo bean = value.get(i);
					String val = bean.getTr_no()+"@@@"+bean.getRev_no()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getTr_date())+"@@@"+bean.getInout()+"@@@"+bean.getTr_issuepur()+"@@@"+bean.getRtn_status_code()+"@@@"+bean.getOriginator()+"@@@"+bean.getDiscipline()+"@@@"+bean.getTr_remark()+"@@@"+bean.getDoc_no()+"@@@"+bean.getCurrentRevNo()+"@@@"+bean.getCategory()+"@@@"+bean.getTitle()+"@@@"+bean.getRemark();
					valueList.add(val);
					ExcelValueData=ExcelValueData+val+"@#@#@";
				}
			} else {
				columnList="[{\"title\":\"Document No\", \"field\":\"DocumentNo\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},{\"title\":\"Current RevNo\", \"field\":\"CurrentRevNo\", \"minWidth\":\"102\"},{\"title\":\"Category\", \"field\":\"Category\", \"minWidth\":\"80\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"TR No\", \"field\":\"TRNo\", \"minWidth\":\"80\"},{\"title\":\"Rev No\", \"field\":\"RevNo\", \"minWidth\":\"68\"},{\"title\":\"TR Date\", \"field\":\"TRDate\", \"minWidth\":\"80\"},{\"title\":\"IN / OUT\", \"field\":\"IN/OUT\", \"minWidth\":\"80\"},{\"title\":\"TR Issue Purpose\", \"field\":\"TRIssuePurpose\", \"minWidth\":\"114\"},{\"title\":\"Return Status\", \"field\":\"ReturnStatus\", \"minWidth\":\"96\"},{\"title\":\"Orginator\", \"field\":\"Orginator\", \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"TR Remark\", \"field\":\"TRRemark\", \"minWidth\":\"82\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"}]";
				columnInfo="Document No###Current RevNo###Category###Title###TR No###Rev No###TR Date###IN / OUT###TR Issue Purpose###Return Status###Orginator###Discipline###TR Remark###Remark";
				keyList="DocumentNo###CurrentRevNo###Category###Title###TRNo###RevNo###TRDate###IN/OUT###TRIssuePurpose###ReturnStatus###Orginator###Discipline###TRRemark###Remark";
			
				for ( int i=0; i<value.size(); ++i ) {
					PrjReportSiteVo bean = value.get(i);
					String val = bean.getDoc_no()+"@@@"+bean.getCurrentRevNo()+"@@@"+bean.getCategory()+"@@@"+bean.getTitle()+"@@@"+bean.getTr_no()+"@@@"+bean.getRev_no()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getTr_date())+"@@@"+bean.getInout()+"@@@"+bean.getTr_issuepur()+"@@@"+bean.getRtn_status_code()+"@@@"+bean.getOriginator()+"@@@"+bean.getDiscipline()+"@@@"+bean.getTr_remark()+"@@@"+bean.getRemark();
					valueList.add(val);
					ExcelValueData=ExcelValueData+val+"@#@#@";
				}
			}
		} else if ( reportVo.getReportType().equals("4_6")) { 
			//SDCI Progress Status
			columnList="[{\"title\":\"Code\", \"field\":\"Code\", \"minWidth\":\"60\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Total Index\", \"field\":\"TotalIndex\", \"minWidth\":\"84\"},{\"title\":\"Weight Factor\", \"field\":\"WeightFactor\", \"minWidth\":\"100\"},{\"title\":\"Weight Value\", \"field\":\"WeightValue\", \"minWidth\":\"100\"},{\"title\":\"Progress\", \"field\":\"Progress\", \"minWidth\":\"72\"},{\"title\":\"\", \"field\":\"\", \"minWidth\":\"72\"},@@@@@@]";			
			columnInfo="Code###Discipline###Total Index###Weight Factor###Weight Value###Progress######@@@@@@";
	    	keyList="Code###Discipline###TotalIndex###WeightFactor###WeightValue###Progress######@@@@@@";

	    	value = selectList("prjReportSiteSqlMap.PrjReportSiteData_6_0",reportVo);
			
	    	int sum1=0;
			int sum2=0;
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo val = value.get(i);
				sum1 =sum1 + Integer.parseInt(val.getTotal_index());
			}

			String columnList1="";
			
			String column1="";
			String key1="";

			int tot_totalIdxCnt = 0;
			int tot_weightValueCnt = 0;
			int[][] tot_StepCnt = new int[getSiteStepCount][4];
			float[] tot_progress = new float[4];
			
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo val = value.get(i);
				reportVo.setDiscip_code(val.getDiscip_code());
				
				List<PrjReportSiteVo> value2 = selectList("prjReportSiteSqlMap.PrjReportSiteData_6",reportVo);

				List<PrjReportSiteVo> value3 = selectList("prjReportSiteSqlMap.PrjReportSiteData_6_1",reportVo);
				
				Map<String, Integer> cntData = new HashMap<String ,Integer>();
				Map<String, Integer> cutOffCntData = new HashMap<String ,Integer>();
				Map<String, Integer> perSize = new HashMap<String ,Integer>();
				Map<String, Integer> wgtMap = new HashMap<String ,Integer>();
				
				column1="";
				columnList1="";
				key1="";
				
				int per=0;
				
				//int[] minWidthList = {96,96,124,170,130};
				
				for ( int j=0; j<value2.size(); ++j ) {
					PrjReportSiteVo valTmp = value2.get(j);
					String key=valTmp.getWork_step();
					String cnt=valTmp.getCheckCnt();
					String cutOffCnt = valTmp.getCutOffCnt();
					String wbsCodeKey = valTmp.getWbs_code();
					
					if ( column1.indexOf(key) == -1 ) {
						column1 = column1+key+"("+valTmp.getStep_per()+"%)###";
						key1=key1+key+"###";
						per=per+Integer.parseInt(valTmp.getStep_per());
						columnList1=columnList1+"{\"title\":\""+key+"("+per+"%)"+"\", \"field\":\""+key+"\", \"minWidth\":\"170\"},";
						perSize.put(key, per);
					}
					
					Integer tmpCutOffCnt = cutOffCntData.get(key);
					if(tmpCutOffCnt==null) tmpCutOffCnt = 0;
					
					if ( cntData.get(key) == null ) {
						cntData.put(key, Integer.parseInt(cnt));
						cutOffCntData.put(key, tmpCutOffCnt + Integer.parseInt(cutOffCnt));
					} else {
						Integer cntTmp = cntData.get(key);
						cntData.put(key, cntTmp+Integer.parseInt(cnt));
						cutOffCntData.put(key, tmpCutOffCnt + Integer.parseInt(cutOffCnt));
					}
					
					wgtMap.put(wbsCodeKey, Integer.parseInt(valTmp.getWgtval()));
					
					
				}
				
				int tmp_wgtValue = 0;
				for(Map.Entry<String, Integer> entry : wgtMap.entrySet()) {
					tmp_wgtValue += entry.getValue();
				}
				
				
				val.setWgtval(tmp_wgtValue+"");
				//System.out.println(value.get(i).getDiscipline() + " : " + tmp_wgtValue);

				String tmpKey[] = key1.split("###");
				
				String REPLACEPOS1="";
				String REPLACEPOS2="";
				
				float progress1=0;
				float progress2=0;
				float progress3=0;
				float progress4=0;

				int total_cnt = value3.size();
				
				for ( int j=0; j<tmpKey.length; ++j ) {
					String key = tmpKey[j];
					
					int cntDataTmp=cntData.get(key);
					int cutOffCntDataTmp=cutOffCntData.get(key);
					int perSizeTmp=perSize.get(key);
					
					int cnt1=cntDataTmp+cutOffCntDataTmp;
					int cnt2=cntDataTmp;
					int cnt3=cnt2-cnt1;
					int cnt4=cutOffCntDataTmp;

					int step_percent = Integer.parseInt(value2.get(j).getStep_per());
					progress1=(float) (progress1+((float)cnt1/total_cnt)*(step_percent/100.0));
					progress2=(float) (progress2+((float)cnt2/total_cnt)*(step_percent/100.0));
					progress3=(float) (progress3+((float)cnt3/total_cnt)*(step_percent/100.0));
					progress4=(float) (progress4+((float)cnt4/total_cnt)*(step_percent/100.0));
					
					REPLACEPOS2=REPLACEPOS2+cnt1+"@,@"+cnt2+"@,@"+cnt3+"@,@"+cnt4+"@@@";
				}
				
				int totalCnt =Integer.parseInt(val.getTotal_index());
				REPLACEPOS1=(String.format("%.1f",progress1*100))+"%@,@"+(String.format("%.1f",progress2*100))+"%@,@"+(String.format("%.1f",progress3*100))+"%@,@"+(String.format("%.1f",progress4*100))+"%";
				tot_progress[0] += (progress1 * 100);
				tot_progress[1] += progress2 * 100;
				tot_progress[2] += progress3 * 100;
				tot_progress[3] += progress4 * 100;
				
				
				if ( REPLACEPOS2.length() > 0 ) {  REPLACEPOS2 = REPLACEPOS2.substring(0, REPLACEPOS2.length()-3); }
				
				// total에 찍히는 부분 계산
				tot_totalIdxCnt += total_cnt;
				tot_weightValueCnt += Integer.parseInt(val.getWgtval());
				String[] stepsCnt = REPLACEPOS2.split("@@@");
				int stepIdx = 0;
				for(String step : stepsCnt) {
					String[] cnts = step.split("@,@");
					for(int cntIdx=0;cntIdx<cnts.length;cntIdx++) {
						tot_StepCnt[stepIdx][cntIdx] += Integer.parseInt(cnts[cntIdx]);
					}
					stepIdx++;
				}
				
				String weightFactorStr = String.format("%.1f",Float.parseFloat(val.getWgtval())*100/sum2) ;
				
				
				String ss = val.getDiscip_code()+"@@@"+val.getDiscipline()+"@@@"+total_cnt+"@@@"+"weightFactorStr"+"%@@@"+val.getWgtval()+"@@@REPLACEPOS1@@@Plan@,@Actual@,@Balance@,@Behind@@@REPLACEPOS2";
				ss=ss.replaceAll("REPLACEPOS1", REPLACEPOS1);
				ss=ss.replaceAll("REPLACEPOS2", REPLACEPOS2);
				
				valueList.add(ss);
				ExcelValueData=ExcelValueData+ss+"@#@#@";
				
				
			}
			int discipCnt = valueList.size();
			for(int pIdx = 0; pIdx<4; pIdx++) {
				tot_progress[pIdx] = tot_progress[pIdx] / discipCnt;
			}
			
			String tot_progressStr =(String.format("%.1f", tot_progress[0]))+"%@,@"+(String.format("%.1f", tot_progress[1]))+"%@,@"+(String.format("%.1f", tot_progress[2]))+"%@,@"+(String.format("%.1f", tot_progress[3]))+"%";
			String totalStr = " @@@Total@@@"+tot_totalIdxCnt+"@@@100%@@@"+tot_weightValueCnt+"@@@"+tot_progressStr+"@@@Plan@,@Actual@,@Balance@,@Behind@@@";

			for(int totalIdx = 0 ; totalIdx<getSiteStepCount;totalIdx++) {
				String tmp = "";
				for(int jj=0;jj<4;jj++) {
					tmp+= tot_StepCnt[totalIdx][jj] + "@,@";
				}
				tmp = tmp.substring(0,tmp.length()-3);
				totalStr+=(tmp+"@@@");
			}
			totalStr = totalStr.substring(0,totalStr.length()-3);
			
			int valIdx = 0;
			for(String line : valueList) {
				String[] cellList = line.split("@@@");
				
				float tmpWgtFactor = (float) ((Float.parseFloat(cellList[4]) / tot_weightValueCnt) * 100.0);
				if(Float.isNaN(tmpWgtFactor)) tmpWgtFactor=0;
				String replaceNumber = String.format("%.1f", tmpWgtFactor);
				
				
				line = line.replaceAll("weightFactorStr",replaceNumber);
				valueList.set(valIdx++, line);
			}
			
			
			valueList.add(totalStr);
			ExcelValueData=ExcelValueData+totalStr+"@#@#@";
			
			if ( columnList1.length() > 0 ) {  columnList1 = columnList1.substring(0, columnList1.length()-1); }
			if ( column1.length() > 0 ) {  column1 = column1.substring(0, column1.length()-3); }
			if ( key1.length() > 0 ) {  key1 = key1.substring(0, key1.length()-3); }
			
			columnList=columnList.replaceAll("@@@@@@", columnList1);
			columnInfo=columnInfo.replaceAll("@@@@@@", column1);
			keyList=keyList.replaceAll("@@@@@@", key1);
		} else if ( reportVo.getReportType().equals("4_7")) { 
			//SDCI Workdone List
			columnList="[{\"title\":\"Serial No\", \"field\":\"SerialNo\" , \"minWidth\":\"80\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Work Step\", \"field\":\"WorkStep\", \"minWidth\":\"90\"},{\"title\":\"Document No\", \"field\":\"DocumentNo\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},{\"title\":\"Rev No\", \"field\":\"RevNo\", \"minWidth\":\"68\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Category\", \"field\":\"Category\", \"minWidth\":\"80\"},{\"title\":\"Plan Date\", \"field\":\"PlanDate\", \"minWidth\":\"80\"},{\"title\":\"Actual Date\", \"field\":\"ActualDate\", \"minWidth\":\"86\"},{\"title\":\"Transmittal No\", \"field\":\"TransmittalNo\", \"minWidth\":\"102\"},{\"title\":\"Designer\", \"field\":\"Designer\", \"minWidth\":\"106\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"}]";
			columnInfo="Serial No###Discipline###Work Step###Document No###Rev No###Title###Category###Plan Date###Actual Date###Transmittal No###Designer###Remark";
			keyList="SerialNo###Discipline###WorkStep###DocumentNo###RevNo###Title###Category###PlanDate###ActualDate###TransmittalNo###Designer###Remark";
		
			value = selectList("prjReportSiteSqlMap.PrjReportSiteData_7",reportVo);
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				String val = (i+1) +"@@@"+bean.getDiscipline()+"@@@"+bean.getWork_step()+"@@@"+bean.getDoc_no()+"@@@"+bean.getRev_no()+"@@@"+bean.getTitle()+"@@@"+bean.getCategory()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getPlan_date())+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getActual_date())+"@@@"+bean.getTr_no()+"@@@"+bean.getDeginer()+"@@@"+bean.getRemark();
				valueList.add(val);
				ExcelValueData=ExcelValueData+val+"@#@#@";
			}
			
		} else if ( reportVo.getReportType().equals("4_8")) { 
			//SDCI Workdone Status
			columnList="[{\"title\":\"Work Step\", \"field\":\"WorkStep\", \"minWidth\":\"90\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Total Index\", \"field\":\"TotalIndex\", \"minWidth\":\"84\"},"
					+ "{\"title\":\"Weight Factor\", \"field\":\"WeightFactor\", \"minWidth\":\"100\"},{\"title\":\"Weight Value\", \"field\":\"WeightValue\", \"minWidth\":\"100\"},"
					+ "{\"title\":\"Increased Progress\", \"field\":\"IncreasedProgress\", \"minWidth\":\"124\"},{\"title\":\"\", \"field\":\"\", \"minWidth\":\"72\"},@@@@@@]";
			columnInfo="Work Step###Discipline###Total Index###Weight Factor###Weight Value###Increased Progress######@@@@@@";
	    	keyList="WorkStep###Discipline###TotalIndex###WeightFactor###WeightValue###IncreasedProgress######@@@@@@";
			
	    	value = selectList("prjReportSiteSqlMap.PrjReportSiteData_8_0",reportVo);

			int sum1=0;
			int sum2=0;
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo val = value.get(i);
				sum1 =sum1 + Integer.parseInt(val.getTotal_index());
			}

			String columnList1="";
			
			String column1="";
			String key1="";

			int tot_totalIdxCnt = 0;
			int tot_weightValueCnt = 0;
			int[][] tot_StepCnt = new int[getSiteStepCount][3];
			float[] tot_progress = new float[3];
			
			
			int query2size = 0;
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo val = value.get(i);
				reportVo.setDiscip_code(val.getDiscip_code());
				
				List<PrjReportSiteVo> value2 = selectList("prjReportSiteSqlMap.PrjReportSiteData_8",reportVo);
				query2size = value2.size();
				
				Map<String, Integer> plan_cnt_data = new HashMap<String ,Integer>();
				Map<String, Integer> fore_cnt_data = new HashMap<String ,Integer>();
				Map<String, Integer>  actual_cnt_data = new HashMap<String ,Integer>();
				Map<String, Integer> perSize = new HashMap<String ,Integer>();
				Map<String, Integer> wgtMap = new HashMap<String ,Integer>();
				
				column1="";
				columnList1="";
				key1="";
				
				int per=0;
				
				for ( int j=0; j<value2.size(); ++j ) {
					PrjReportSiteVo valTmp = value2.get(j);
					String key=valTmp.getWork_step();
					String cnt=valTmp.getCheckCnt();
					String cutOffCnt = valTmp.getCutOffCnt();
					String wbsCodeKey = valTmp.getWbs_code();
					
					if ( column1.indexOf(key) == -1 ) {
						column1 = column1+key+"("+valTmp.getStep_per()+"%)###";
						key1=key1+key+"###";
						per=per+Integer.parseInt(valTmp.getStep_per());
						columnList1=columnList1+"{\"title\":\""+key+"("+per+"%)"+"\", \"field\":\""+key+"\", \"minWidth\":\"170\"},";
						perSize.put(key, per);
					}
					
					if ( plan_cnt_data.get(key) == null ) {
						plan_cnt_data.put(key, Integer.parseInt(valTmp.getPlan_cnt()));
						fore_cnt_data.put(key, Integer.parseInt(valTmp.getFore_cnt()));
						actual_cnt_data.put(key, Integer.parseInt(valTmp.getActual_cnt()));
					} else {
						
						plan_cnt_data.put(key, plan_cnt_data.get(key)+Integer.parseInt(valTmp.getPlan_cnt()));
						fore_cnt_data.put(key, fore_cnt_data.get(key)+Integer.parseInt(valTmp.getFore_cnt()));
						actual_cnt_data.put(key, actual_cnt_data.get(key)+Integer.parseInt(valTmp.getActual_cnt()));
					}

					wgtMap.put(wbsCodeKey, Integer.parseInt(valTmp.getWgtval()));
					
				}
				
				int tmp_wgtValue = 0;
				for(Map.Entry<String, Integer> entry : wgtMap.entrySet()) tmp_wgtValue += entry.getValue();
				
				val.setWgtval(tmp_wgtValue+"");
				
				String tmpKey[] = key1.split("###");
				
				String REPLACEPOS1="";
				String REPLACEPOS2="";
				
				float progress1=0;
				float progress2=0;
				float progress3=0;

				int total_cnt = Integer.parseInt(val.getTotal_index()) / getSiteStepCount;
				
				for ( int j=0; j<tmpKey.length; ++j ) {
					String key = tmpKey[j];
					
					int cnt1=plan_cnt_data.get(key);
					int cnt2=actual_cnt_data.get(key);;
					int cnt3=fore_cnt_data.get(key);
	
					int step_percent = Integer.parseInt(value2.get(j).getStep_per());
					progress1=(float) (progress1+((float)cnt1/total_cnt)*(step_percent/100.0));
					progress2=(float) (progress2+((float)cnt2/total_cnt)*(step_percent/100.0));
					progress3=(float) (progress3+((float)cnt3/total_cnt)*(step_percent/100.0));
					
					REPLACEPOS2=REPLACEPOS2+cnt1+"@,@"+cnt2+"@,@"+cnt3+"@@@";
				}
				
				int totalCnt =Integer.parseInt(val.getTotal_index());
				REPLACEPOS1=(String.format("%.1f",progress1*100))+"%@,@"+(String.format("%.1f",progress2*100))+"%@,@"+(String.format("%.1f",progress3*100))+"%";
				tot_progress[0] += progress1 * 100;
				tot_progress[1] += progress2 * 100;
				tot_progress[2] += progress3 * 100;
				
				if ( REPLACEPOS2.length() > 0 ) {  REPLACEPOS2 = REPLACEPOS2.substring(0, REPLACEPOS2.length()-3); }


				// total에 찍히는 부분 계산
				tot_totalIdxCnt += total_cnt;
				tot_weightValueCnt += Integer.parseInt(val.getWgtval());
				String[] stepsCnt = REPLACEPOS2.split("@@@");
				int stepIdx = 0;
				for(String step : stepsCnt) {
					String[] cnts = step.split("@,@");
					for(int cntIdx=0;cntIdx<cnts.length;cntIdx++) {
						tot_StepCnt[stepIdx][cntIdx] += Integer.parseInt(cnts[cntIdx]);
					}
					stepIdx++;
				}

				String weightFactorStr = String.format("%.1f",Float.parseFloat(val.getWgtval())*100/sum2) ;
				
				String ss = val.getDiscip_code()+"@@@"+val.getDiscipline()+"@@@"+total_cnt+"@@@"+"weightFactorStr"+"%@@@"+val.getWgtval()+"@@@REPLACEPOS1@@@Plan@,@Actual@,@Forecast@@@REPLACEPOS2";
				ss=ss.replaceAll("REPLACEPOS1", REPLACEPOS1);
				ss=ss.replaceAll("REPLACEPOS2", REPLACEPOS2);
				
				valueList.add(ss);
				ExcelValueData=ExcelValueData+ss+"@#@#@";
			}

			int discipCnt = valueList.size();
			for(int pIdx = 0; pIdx<3; pIdx++) {
				tot_progress[pIdx] = tot_progress[pIdx] / discipCnt;
			}

			if(query2size>0) {
				String tot_progressStr =(String.format("%.1f", tot_progress[0]))+"%@,@"+(String.format("%.1f", tot_progress[1]))+"%@,@"+(String.format("%.1f", tot_progress[2]))+"%";
				String totalStr = " @@@Total@@@"+tot_totalIdxCnt+"@@@100%@@@"+tot_weightValueCnt+"@@@"+tot_progressStr+"@@@Plan@,@Actual@,@Forecast@@@";
	
				for(int totalIdx = 0 ; totalIdx<getSiteStepCount;totalIdx++) {
					String tmp = "";
					for(int jj=0;jj<3;jj++) {
						tmp+= tot_StepCnt[totalIdx][jj] + "@,@";
					}
					tmp = tmp.substring(0,tmp.length()-3);
					totalStr+=(tmp+"@@@");
				}
				totalStr = totalStr.substring(0,totalStr.length()-3);

				int valIdx = 0;
				for(String line : valueList) {
					String[] cellList = line.split("@@@");
					
					float tmpWgtFactor = (float) ((Float.parseFloat(cellList[4]) / tot_weightValueCnt) * 100.0);
					if(Float.isNaN(tmpWgtFactor)) tmpWgtFactor=0;
					String replaceNumber = String.format("%.1f", tmpWgtFactor);
					
					
					line = line.replaceAll("weightFactorStr",replaceNumber);
					valueList.set(valIdx++, line);
				}
				
				valueList.add(totalStr);
				ExcelValueData=ExcelValueData+totalStr+"@#@#@";
			}
			if ( columnList1.length() > 0 ) {  columnList1 = columnList1.substring(0, columnList1.length()-1); }
			if ( column1.length() > 0 ) {  column1 = column1.substring(0, column1.length()-3); }
			if ( key1.length() > 0 ) {  key1 = key1.substring(0, key1.length()-3); }
			
			columnList=columnList.replaceAll("@@@@@@", columnList1);
			columnInfo=columnInfo.replaceAll("@@@@@@", column1);
			keyList=keyList.replaceAll("@@@@@@", key1);
		} else if ( reportVo.getReportType().equals("4_9")) { 
			//Weekly Progress Graph
			columnList="[{\"title\":\"Code\", \"field\":\"Code\", \"minWidth\":\"60\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"W/F\", \"field\":\"WF\", \"minWidth\":\"50\"},{\"title\":\"Weight Value\", \"field\":\"WeightValue\", \"minWidth\":\"96\"}";
			columnInfo="Code###Discipline###W/F###WeightValue";
	    	keyList="Code###Discipline###WF###WeightValue";
	    	
	    	columnList=columnList+"{\"title\":\"\", \"columns\":[{\"title\":\"\", \"field\":\"empty1\", \"minWidth\":\"60\"}]}";
	    	columnInfo=columnInfo+"###";
	    	keyList=keyList+"###empty1";
	    	
	    	value = selectList("prjReportSiteSqlMap.PrjReportSiteData_9_0",reportVo);
			
			int pos=-28;
			
			String mapKeyList="";
			
			HashMap<String,Integer> keyMap = new HashMap<String,Integer>();

			reportVo.setStep_cnt(getSiteStepCount);
			
			String[] date_s_array = new String[15];
			for ( int i=0; i<15; ++i ) {
				String dateTmp = getDateWeek(reportVo.getCut_off_date(), pos);
				date_s_array[i] = dateTmp;
				
				if ( i == 0 ) { reportVo.setDate_s(dateTmp); }
				if ( i == 14 ) { reportVo.setDate_d(dateTmp); }
				
				columnList=columnList+",{\"title\":\""+(i+1)+"\", \"columns\":[{\"title\":\""+CommonConst.stringdateTOYYYYMMDD(dateTmp)+"\", \"field\":\"date"+(i+1)+"\", \"minWidth\":\"90\"}]}";
		    	columnInfo=columnInfo+"###"+CommonConst.stringdateTOYYYYMMDD(dateTmp);
		    	keyList=keyList+"###date"+(i+1);
		    	
				mapKeyList=mapKeyList+dateTmp+"@,@";
				pos=pos+7;
				
				keyMap.put(CommonConst.stringdateTOYYYYMMDD(dateTmp), i);
			}
			
			columnList=columnList+"]";
			
			if (mapKeyList.length() > 0 ) { mapKeyList = mapKeyList.substring(0, mapKeyList.length()-1); } 
			
			int sum2=0;
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo val = value.get(i);
				sum2 =sum2 + Integer.parseInt(val.getWgtval());
			}
			
			int totalIndexSumCount = 0;

			List<PrjReportSiteVo> value2 = selectList("prjReportSiteSqlMap.PrjReportSiteData_9_1",reportVo);

			int[] weightValue_array = new int[value.size()];
			for(int j=0;j<value2.size();j++) {
				weightValue_array[j] = Integer.parseInt(value2.get(j).getWgtval());
			}
			
			int weightValue_sum = 0;
			for(int j=0;j<weightValue_array.length;j++) {
				weightValue_sum += weightValue_array[j];
			}

			float[] weightFactor_array = new float[value.size()];
			float weightFactor_sum = 0;
			for(int j=0;j<weightValue_array.length;j++) {
				weightFactor_array[j] = (float) ((weightValue_array[j]*100.0)/weightValue_sum);
				weightFactor_sum += (float) ((weightValue_array[j]*100.0)/weightValue_sum);
			}
			float[][] TotalList = new float[15][3];
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				reportVo.setDiscip_code(bean.getDiscip_code());
				
				int total_index = Integer.parseInt(bean.getTotal_index()) / getSiteStepCount;
				
				List<List<PrjReportSiteVo>> rowList = new ArrayList<>();
				
				for(int l=0;l<15;l++) {
					reportVo.setDate_s(date_s_array[l]);
					List<PrjReportSiteVo> value1 = selectList("prjReportSiteSqlMap.PrjReportSiteData_9",reportVo);
					rowList.add(value1);
					
				}
				String sss=bean.getDiscip_code()+"@@@"+bean.getDiscipline()+"@@@"+String.format("%.1f", weightFactor_array[i])+"%@@@"+weightValue_array[i]+"@@@Plan@,@Actual@,@Forcast@@@";
				
				totalIndexSumCount += (Integer.parseInt(bean.getTotal_index())/getSiteStepCount);
				
				for ( int j=0; j<15 ; ++j ) {
					List<PrjReportSiteVo> getWeekItem = rowList.get(j);
					
					int count=0;
					for(PrjReportSiteVo dateStep : getWeekItem) {
						//String date_step = dateStep.getStep();
						int date_cnt = Integer.parseInt(dateStep.getActual_cnt());
						int date_total = dateStep.getTotal();
						if(date_total == 0 ) date_total = 1;
						float stepProgress = ( date_cnt / (float) date_total) * 100 ;
						sss += String.format("%.1f", stepProgress)+"%@,@";
						TotalList[j][count] += stepProgress;
						count++;
					}
					
					sss=sss.substring(0, sss.length()-3);
					sss += "@@@";
				}
				
				
				sss=sss.substring(0, sss.length()-3);
				valueList.add(sss);
				ExcelValueData=ExcelValueData+sss+"@#@#@";
			}
			
			//합계
			String sssT=" @@@Total@@@"+String.format("%.1f", weightFactor_sum)+"%@@@"+weightValue_sum+"@@@Plan@,@Actual@,@Forcast@@@";
			
			for(int i=0;i<15;i++){
				sssT=sssT+String.format("%.1f",TotalList[i][0]/value.size())+"%"+"@,@"+String.format("%.1f",TotalList[i][1]/value.size())+"%"+"@,@"+String.format("%.1f",TotalList[i][2]/value.size())+"%"+"@@@";
			}
			sssT=sssT.substring(0, sssT.length()-3);
			valueList.add(sssT);
			ExcelValueData=ExcelValueData+sssT+"@#@#@";
		}  else if ( reportVo.getReportType().equals("4_10")) { 
			//Monthly Progress Graph
			columnList="[{\"title\":\"Code\", \"field\":\"Code\", \"minWidth\":\"60\"},{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"W/F\", \"field\":\"WF\", \"minWidth\":\"50\"},{\"title\":\"Weight Value\", \"field\":\"WeightValue\", \"minWidth\":\"96\"}";
			columnInfo="Code###Discipline###W/F###WeightValue";
	    	keyList="Code###Discipline###WF###WeightValue";
	    	
	    	columnList=columnList+"{\"title\":\"\", \"columns\":[{\"title\":\"\", \"field\":\"empty1\", \"minWidth\":\"60\"}]}";
	    	columnInfo=columnInfo+"###";
	    	keyList=keyList+"###empty1";
	    	
	    	value = selectList("prjReportSiteSqlMap.PrjReportSiteData_10_0",reportVo);
			
			int pos=0;
			
			String mapKeyList="";
			
			HashMap<String,Integer> keyMap = new HashMap<String,Integer>();

			reportVo.setStep_cnt(getSiteStepCount);
			
			String[] date_s_array = new String[24];
			for ( int i=0; i<24; ++i ) {
				String dateTmp = getDataMonth(reportVo.getCut_off_date(), pos);
				date_s_array[i] = dateTmp;
				if ( i == 0 ) { reportVo.setDate_s(dateTmp); }
				if ( i == 23 ) { reportVo.setDate_d(dateTmp); }
				
				columnList=columnList+",{\"title\":\""+(i+1)+"\", \"columns\":[{\"title\":\""+CommonConst.stringdateTOYYYYMMDD(dateTmp)+"\", \"field\":\"date"+(i+1)+"\", \"minWidth\":\"76\"}]}";
		    	columnInfo=columnInfo+"###"+CommonConst.stringdateTOYYYYMMDD(dateTmp);
		    	keyList=keyList+"###date"+(i+1);
		    	
				mapKeyList=mapKeyList+dateTmp+"@,@";
				pos=pos+1;
				
				keyMap.put(CommonConst.stringdateTOYYYYMMDD(dateTmp), i);
			}
			
			columnList=columnList+"]";
			
			if (mapKeyList.length() > 0 ) { mapKeyList = mapKeyList.substring(0, mapKeyList.length()-1); } 
			
			int sum2=0;
			
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo val = value.get(i);
				sum2 =sum2 + Integer.parseInt(val.getWgtval());
			}
			
			int totalIndexSumCount = 0;

			List<PrjReportSiteVo> value2 = selectList("prjReportSiteSqlMap.PrjReportSiteData_10_1",reportVo);

			int[] weightValue_array = new int[value.size()];
			for(int j=0;j<value2.size();j++) {
				weightValue_array[j] = Integer.parseInt(value2.get(j).getWgtval());
			}
			
			int weightValue_sum = 0;
			for(int j=0;j<weightValue_array.length;j++) {
				weightValue_sum += weightValue_array[j];
			}

			float[] weightFactor_array = new float[value.size()];
			float weightFactor_sum = 0;
			for(int j=0;j<weightValue_array.length;j++) {
				weightFactor_array[j] = (float) ((weightValue_array[j]*100.0)/weightValue_sum);
				weightFactor_sum += (float) ((weightValue_array[j]*100.0)/weightValue_sum);
			}
			float[][] TotalList = new float[24][3];
			for ( int i=0; i<value.size(); ++i ) {
				PrjReportSiteVo bean = value.get(i);
				reportVo.setDiscip_code(bean.getDiscip_code());
				
				int total_index = Integer.parseInt(bean.getTotal_index()) / getSiteStepCount;
				
				List<List<PrjReportSiteVo>> rowList = new ArrayList<>();
				
				for(int l=0;l<24;l++) {
					reportVo.setDate_s(date_s_array[l]);
					List<PrjReportSiteVo> value1 = selectList("prjReportSiteSqlMap.PrjReportSiteData_9",reportVo);
					rowList.add(value1);
					
				}
				String sss=bean.getDiscip_code()+"@@@"+bean.getDiscipline()+"@@@"+String.format("%.1f", weightFactor_array[i])+"%@@@"+weightValue_array[i]+"@@@Plan@,@Actual@,@Forcast@@@";
				
				totalIndexSumCount += (Integer.parseInt(bean.getTotal_index())/getSiteStepCount);
				
				for ( int j=0; j<24 ; ++j ) {
					List<PrjReportSiteVo> getWeekItem = rowList.get(j);
					
					int count=0;
					for(PrjReportSiteVo dateStep : getWeekItem) {
						//String date_step = dateStep.getStep();
						int date_cnt = Integer.parseInt(dateStep.getActual_cnt());
						int date_total = dateStep.getTotal();
						if(date_total == 0 ) date_total = 1;
						float stepProgress = ( date_cnt / (float) date_total) * 100 ;
						sss += String.format("%.1f", stepProgress)+"%@,@";
						TotalList[j][count] += stepProgress;
						count++;
					}
					
					sss=sss.substring(0, sss.length()-3);
					sss += "@@@";
				}
				
				
				sss=sss.substring(0, sss.length()-3);
				valueList.add(sss);
				ExcelValueData=ExcelValueData+sss+"@#@#@";
			}
			
			//합계
			String sssT=" @@@Total@@@"+String.format("%.1f", weightFactor_sum)+"%@@@"+weightValue_sum+"@@@Plan@,@Actual@,@Forcast@@@";
			
			for(int i=0;i<24;i++){
				sssT=sssT+String.format("%.1f",TotalList[i][0]/value.size())+"%"+"@,@"+String.format("%.1f",TotalList[i][1]/value.size())+"%"+"@,@"+String.format("%.1f",TotalList[i][2]/value.size())+"%"+"@@@";
				
			}
			sssT=sssT.substring(0, sssT.length()-3);
			valueList.add(sssT);
			ExcelValueData=ExcelValueData+sssT+"@#@#@";
			
		}  else if ( reportVo.getReportType().equals("4_11")) { 
			//WBS Detail
			columnList="[{\"title\":\"Discipline\", \"field\":\"Discipline\", \"minWidth\":\"90\", \"hozAlign\":\"left\"},{\"title\":\"Code\", \"field\":\"Code\", \"minWidth\":\"60\"},{\"title\":\"Description\", \"field\":\"Description\", \"minWidth\":\"90\"},{\"title\":\"Weight Value\", \"columns\":[{\"title\":\"Total\", \"field\":\"Total\"},{\"title\":\"Plan\", \"field\":\"Plan\"},{\"title\":\"Plan\", \"field\":\"Earned\"}]},{\"title\":\"Progress\", \"field\":\"Progress\"}]";
			columnInfo="";
	    	keyList="Discipline###Code###Description###Total###Plan###Earned###Progress";
			
	    	//WBS LIST
	    	value = selectList("prjReportSiteSqlMap.PrjReportSiteData_11_0",reportVo);
	    	
	    	//해당 WBS LIST 쿼리
	    	for ( int i=0; i<value.size(); ++i ) {
	    		PrjReportSiteVo val = value.get(i);
	    		reportVo.setWbs_code(val.getWbs_code());

	    		List<PrjReportSiteVo> valueT = selectList("prjReportSiteSqlMap.PrjReportSiteData_11",reportVo);
	    	
	    		Integer cnt1 = Integer.parseInt(val.getCheckCnt());
	    		Integer cnt2 = Integer.parseInt(val.getPlan_cnt()); 
	    		
	    		String ss = val.getDiscipline()+"@@@"+val.getWbs_code()+"@@@"+val.getWbs_desc()+"@@@"+val.getCheckCnt()+"@@@"+val.getPlan_cnt()+"@@@"+val.getActual_cnt()+"@@@"+(cnt2*100)/cnt1+"%";
	    		valueList.add(ss);
				ExcelValueData=ExcelValueData+ss+"@#@#@";
				
	    	}
		}
		
		JSONParser parser = new JSONParser();
		try {
			columnJsonList = (JSONArray)parser.parse(columnList);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for ( int i=0; i<valueList.size(); ++i ) {
			String valueT = valueList.get(i);
			
			JSONObject obj = new JSONObject();
			
			String[] tmpKey = keyList.split("###");
			String[] tmpValue = valueT.split("@@@");
			
			for(int k=0;k<tmpKey.length;k++) {
				System.out.println("tmpKey["+k+"]"+tmpKey[k]);
			}
			for(int k=0;k<tmpValue.length;k++) {
				System.out.println("tmpValue["+k+"]"+tmpValue[k]);
			}
			for ( int j=0; j<tmpValue.length; ++j ) {
				String key = tmpKey[j];
				String valueTT = tmpValue[j];
				String[] tmpV = valueTT.split("@,@");
				
				obj.put(key, tmpV);
			}
			valueJsonList.add(obj);
		}
		
		if ( ExcelValueData.length() > 0 ) ExcelValueData = ExcelValueData.substring(0, ExcelValueData.length()-5);
		
		result.add(columnInfo); 
		result.add(ExcelValueData); 
		result.add(columnJsonList);
		result.add(valueJsonList); 
		result.add(reportVo);
		result.add(value.size());
		
		return result;
	}

	public static String getDateWeek(String date, int interval) {
		
		int year = Integer.parseInt(date.substring(0,4));
		int month = Integer.parseInt(date.substring(4,6));
		int day = Integer.parseInt(date.substring(6,8));
		
		
		Calendar cal = Calendar.getInstance();

		cal.set(year, month-1, day);
		cal.add(Calendar.DATE, interval);

		java.util.Date weekago = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

		return formatter.format(weekago);
	}
	
	public static String getDataMonth(String date, int interval) {
		
		int year = Integer.parseInt(date.substring(0,4));
		int month = Integer.parseInt(date.substring(4,6));
		int day = Integer.parseInt(date.substring(6,8));
		
		
		Calendar cal = Calendar.getInstance();

		cal.set(year, month-1 + interval, day);

		java.util.Date weekago = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

		return formatter.format(weekago);
	}
}
