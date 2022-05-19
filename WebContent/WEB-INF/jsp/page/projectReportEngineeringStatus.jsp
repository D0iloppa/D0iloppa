<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!-- <style>
	/* 로딩*/
	#loading {
		height: 100%;
		left: 0px;
		position: fixed;
		_position:absolute; 
		top: 0px;
		width: 100%;
		filter:alpha(opacity=50);
		-moz-opacity:0.5;
		opacity : 0.5;
	}

	.loading {
		background-color: white;
		z-index: 199;
	}
	
	#loading_img{
		position:absolute; 
		top:50%;
		left:50%;
		height:35px;
		margin-top:-75px;	//	이미지크기
		margin-left:-75px;	//	이미지크기
		z-index: 200;
	}
</style> -->

<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>


                                <div class="pg_top">
                                    <div class="right">
                                        <button class="button btn_blue" onclick="Retrieve();">Retrieve</button>
                                        <button class="button btn_purple" onclick="downloadReport();">To Excel</button>
                                    </div>
                                </div>
                                <h4>Report 종류</h4>
                                <div class="reportEngStat">
                                    <div class="left">
                                        <span><input type="radio" id="enginerringRaido1" name="enginerringRaido" onclick="clickReportRadioType(1);"><label for="enginerringRaido1">DCI List</label></span>
                                        <span><input type="radio" id="enginerringRaido2" name="enginerringRaido" onclick="clickReportRadioType(2);"><label for="enginerringRaido2">DCI Detail List</label><input type="checkbox" class="ml10" id="dci_chkDelDoc"><label for="">Del Doc.</label></span>
                                        <span><input type="radio" id="enginerringRaido3" name="enginerringRaido" onclick="clickReportRadioType(3);"><label for="enginerringRaido3">DCI History List</label></span>
                                        <span><input type="radio" id="enginerringRaido4" name="enginerringRaido" onclick="clickReportRadioType(4);"><label for="enginerringRaido4">DCI Behind List</label><input type="checkbox" class="ml10" id="dci_allBehindList"><label for="">All Behind List</label></span>
                                        <span><input type="radio" id="enginerringRaido5" name="enginerringRaido" onclick="clickReportRadioType(5);"><label for="enginerringRaido5">DCI Transmittal List</label></span>
                                        <span><input type="radio" id="enginerringRaido6" name="enginerringRaido" onclick="clickReportRadioType(6);"><label for="enginerringRaido6">DCI Progress Status</label></span>
                                        <span><input type="radio" id="enginerringRaido7" name="enginerringRaido" onclick="clickReportRadioType(7);"><label for="enginerringRaido7">DCI Workdone List</label></span>
                                        <span><input type="radio" id="enginerringRaido8" name="enginerringRaido" onclick="clickReportRadioType(8);"><label for="enginerringRaido8">DCI Workdone Status</label></span>
                                        <span><input type="radio" id="enginerringRaido9" name="enginerringRaido" onclick="clickReportRadioType(9);"><label for="enginerringRaido9">DCI Week Progress Graph</label></span>
                                        <span><input type="radio" id="enginerringRaido10" name="enginerringRaido" onclick="clickReportRadioType(10);"><label for="enginerringRaido10">DCI Monthly Progress Graph</label></span>
                                        <span><input type="radio" id="enginerringRaido11" name="enginerringRaido" onclick="clickReportRadioType(11);"><label for="enginerringRaido11">WBS Detail</label></span>
                                    </div>
                                    <div class="right">
                                        <div class="tbl_wrap">
                                            <table class="write">
                                                <tr>
                                                    <th style="width:30%">Discipline</th>
                                                    <td style="display:flex;">
                                                        <select name="" id="discipline"></select>
                                                        <div id="checkSheet">
                                                       		<input type="checkbox" id="disciplineCheck" style="margin-left: 5px; margin-top: 0px;"><label for="">1 Sheet</label>
                                                        </div>
                                                    </td>
                                                    <td style="display:none;">
                                                        <select id="discipline_code_id"></select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Work Step</th>
                                                    <td><select name="" id="workStep"></select></td>
                                                </tr>
                                                <tr>
                                                    <th>Cut-Off Date</th>
                                                    <td><input type="text" class="date" id="cutOffDate"></td>
                                                </tr>
                                                <tr>
                                                    <th>Duration</th>
                                                    <td><input type="text" class="date" id="durationS"><input type="checkbox" class="ml5" id="durationCheck" onclick="durationCheckEngin()"><label for="">All</label><input type="text" class="date ml5" id="durationD"></td>
                                                </tr>
                                                <tr>
                                                    <th>TR Order</th>
                                                    <td><select name="" id="trOrder">
                                                    	<option value="1">TR No별</option>
                                                    	<option value="2">Document No별</option>
                                                    </select></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="sec">
	                                <h4 class="reportTitle" style="float: left;"></h4>
	                                <label id="reportCntList" style="margin-left: 10px;"></label>
	                                <div class="tbl_wrap scroll dciList" id="data_table_engineering">
									</div>
                                </div>
                                
<!-- DCI Progress Status Total Index 클릭 팝업창 -->           
<div class="dialog pop_detailDCIList" title="DCI Detail List" style="max-width:1000px;">
    <div class="pop_box">
    	<div class="tbl_wrap dciList2" id="data_table_engineering2" style="overflow:auto;">
		</div>
    </div>
</div>       