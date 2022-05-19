package kr.co.doiloppa.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import kr.co.doiloppa.common.abstractinfo.AbstractService;
import kr.co.doiloppa.common.base.CommonConst;
import kr.co.doiloppa.model.PrjReportCorresVo;
import kr.co.doiloppa.model.ReportVo;

@Service
public class ProjectReportCorrStatusService extends AbstractService {

	public List<Object> getReportData(ReportVo reportVo) {
		List<Object> result = new ArrayList<Object>();

		String columnList = "";
		String keyList="";
		String columnInfo="";
		
		String ExcelValueData="";
		
		List<String> valueList = new ArrayList<String>();

		JSONArray columnJsonList = new JSONArray();
		JSONArray valueJsonList = new JSONArray();

		List<PrjReportCorresVo> value = new ArrayList<PrjReportCorresVo>();
		
		if ( reportVo.getReportType().equals("5_1")) { 
			//Correspondence Status
			columnList="[{\"title\":\"Correspondence No\", \"field\":\"CorrespondenceNo\", \"minWidth\":\"128\"},{\"title\":\"Title\", \"field\":\"Title\", \"minWidth\":\"200\", \"hozAlign\":\"left\"},{\"title\":\"Document Date\", \"field\":\"Date\", \"minWidth\":\"114\"},"
					+ "{\"title\":\"Responsibility\", \"field\":\"Responsibility\", \"minWidth\":\"100\"},{\"title\":\"In/Out\", \"field\":\"InOut\", \"minWidth\":\"80\"},"
					+ "{\"title\":\"Type\", \"field\":\"Type\", \"minWidth\":\"90\"},{\"title\":\"Send/Received Date\", \"field\":\"SendDate\", \"minWidth\":\"135\"},"
					+ "{\"title\":\"Reply Req. Date\", \"field\":\"ReplyReqDate\", \"minWidth\":\"116\"},"
					+ "{\"title\":\"Reference Doc. No\", \"field\":\"ReferenceDocNo\", \"minWidth\":\"126\"},{\"title\":\"Remark\", \"field\":\"Remark\", \"minWidth\":\"100\", \"hozAlign\":\"left\"},"
					+ "{\"title\":\"Sender\", \"field\":\"Sender\", \"minWidth\":\"100\"},{\"title\":\"Receiver\", \"field\":\"Receiver\", \"minWidth\":\"100\"},{\"title\":\"Person In Charge\", \"field\":\"PersonInCharge\", \"minWidth\":\"114\"},"
					+ "{\"title\":\"Creation Date\", \"field\":\"CreationDate\", \"minWidth\":\"100\"},{\"title\":\"Creator Name\", \"field\":\"CreatorName\", \"minWidth\":\"100\"}]";

			columnInfo="Correspondence No###Title###Document Date###Responsibility###In/Out###Type###Send/Received Date###Reply Req Date###"
					+ "Reference Doc. No###Remark###Sender###Receiver###PersonInCharge###Creation Date###Creator Name###";
			keyList="CorrespondenceNo###Title###Date###Responsibility###InOut###Type###SendDate###ReplyReqDate###"
					+ "ReferenceDocNo###Remark###Sender###Receiver###PersonInCharge###CreationDate###CreatorName###";
			value = selectList("prjReportCorrSqlMap.PrjReportCorrData_1",reportVo);
			
			for(int i=0; i<value.size(); ++i) {
				PrjReportCorresVo bean = value.get(i);
				String val = bean.getCorr_no()+"@@@"+bean.getTitle()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getDoc_date())+"@@@"+bean.getResponsibility()+"@@@"+bean.getInout()
				+"@@@"+bean.getType()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getSend_recv_date())+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getReplyreqdate())
				+"@@@"+bean.getRef_doc()+"@@@"+bean.getRemark()
				+"@@@"+bean.getSender()+"@@@"+bean.getReceiver()+"@@@"+bean.getPerson_in_charge()+"@@@"+CommonConst.stringdateTOYYYYMMDD(bean.getReg_date())+"@@@"+bean.getReg_id();
				valueList.add(val);
				ExcelValueData=ExcelValueData+val+"@#@#@";
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
			
			for ( int j=0; j<tmpValue.length; ++j ) {
				String key = tmpKey[j];
				String valueTT = tmpValue[j];
				String[] tmpV = valueTT.split("@,@");
				
				if ( tmpV.length > 1 ) {
					/*JSONObject objT = (JSONObject) columnJsonList.get(j);
					columnJsonList.set(j, objT);*/
				}
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

}
