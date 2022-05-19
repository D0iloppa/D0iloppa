package kr.co.doiloppa.model;

import java.util.ArrayList;
import java.util.List;

public class ReportVo {
	boolean bMakeSheet;
	String  fileName;
	String title;
	String sheetName;
	String imagePath;
	String column;
	String value;
	String projecetName;
	String discipline;
	String midleText;
	String rightText;
	String reportType="";
	String columnList="";
	String trOrder="";
	String prj_id="";
	String date_s="";
	String date_d="";
	String cut_off_date;
	String workStep;
	String disciplineCheck;
	String durationCheck;
	String allBehindList;
	int discipLength;
	
	String valueList;
	String discip_code;
	String wbs_code;
	
	String in_out;
	String del_yn;
	
	String planActualFore;
	int workdone_cnt;
	int step_cnt;
	
	
	public int getDiscipLength() {
		return discipLength;
	}
	public void setDiscipLength(int discipLength) {
		this.discipLength = discipLength;
	}
	public int getStep_cnt() {
		return step_cnt;
	}
	public void setStep_cnt(int step_cnt) {
		this.step_cnt = step_cnt;
	}
	public int getWorkdone_cnt() {
		return workdone_cnt;
	}
	public void setWorkdone_cnt(int workdone_cnt) {
		this.workdone_cnt = workdone_cnt;
	}
	public String getPlanActualFore() {
		return planActualFore;
	}
	public void setPlanActualFore(String planActualFore) {
		this.planActualFore = planActualFore;
	}
	public String getAllBehindList() {
		return allBehindList;
	}
	public void setAllBehindList(String allBehindList) {
		this.allBehindList = allBehindList;
	}
	public String getDurationCheck() {
		return durationCheck;
	}
	public void setDurationCheck(String durationCheck) {
		this.durationCheck = durationCheck;
	}
	
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getIn_out() {
		return in_out;
	}
	public void setIn_out(String in_out) {
		this.in_out = in_out;
	}
	public String getWbs_code() {
		return wbs_code;
	}
	public void setWbs_code(String wbs_code) {
		this.wbs_code = wbs_code;
	}
	public String getDiscip_code() {
		return discip_code;
	}
	public void setDiscip_code(String discip_code) {
		this.discip_code = discip_code;
	}
	public String getWorkStep() {
		return workStep;
	}
	public void setWorkStep(String workStep) {
		this.workStep = workStep;
	}
	public String getDisciplineCheck() {
		return disciplineCheck;
	}
	public void setDisciplineCheck(String disciplineCheck) {
		this.disciplineCheck = disciplineCheck;
	}
	public String getCut_off_date() {
		return cut_off_date;
	}
	public void setCut_off_date(String cut_off_date) {
		this.cut_off_date = cut_off_date;
	}
	public String getPrj_id() {
		return prj_id;
	}
	public void setPrj_id(String prj_id) {
		this.prj_id = prj_id;
	}
	public String getDate_s() {
		return date_s;
	}
	public void setDate_s(String date_s) {
		this.date_s = date_s;
	}
	public String getDate_d() {
		return date_d;
	}
	public void setDate_d(String date_d) {
		this.date_d = date_d;
	}
	public String getTrOrder() {
		return trOrder;
	}
	public void setTrOrder(String trOrder) {
		this.trOrder = trOrder;
	}
	public String getValueList() {
		return valueList;
	}
	public void setValueList(String valueList) {
		this.valueList = valueList;
	}
	
	public String getColumnList() {
		return columnList;
	}
	public void setColumnList(String columnList) {
		this.columnList = columnList;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public void setRightText(String rightText) {
		this.rightText = rightText;
	}
	public boolean isbMakeSheet() {
		return bMakeSheet;
	}
	public void setbMakeSheet(boolean bMakeSheet) {
		this.bMakeSheet = bMakeSheet;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getProjecetName() {
		return projecetName;
	}
	public void setProjecetName(String projecetName) {
		this.projecetName = projecetName;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getMidleText() {
		return midleText;
	}
	public void setMidleText(String midleText) {
		this.midleText = midleText;
	}
	public String getRightText() {
		return rightText;
	}
	public void setRightTex(String rightText) {
		this.rightText = rightText;
	}
}
