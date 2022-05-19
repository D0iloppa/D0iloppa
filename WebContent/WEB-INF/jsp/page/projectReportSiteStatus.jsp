<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<div class="pg_top">
                                    <div class="right">
                                        <button class="button btn_blue" onclick="RetrieveSite();">Retrieve</button>
                                        <button class="button btn_purple" onclick="downloadReportSite();">To Excel</button>
                                    </div>
                                </div>
                                <h4>Report 종류</h4>
                                <div class="reportEngStat">
                                    <div class="left">
                                        <span><input type="radio" id="siteStatusRaido1" name="siteStatusRaido" onclick="clickReportSiteRadioType(1);"><label for="siteStatusRaido1">SDCI List</label></span>
                                        <span><input type="radio" id="siteStatusRaido2" name="siteStatusRaido" onclick="clickReportSiteRadioType(2);"><label for="siteStatusRaido2">SDCI Detail List</label><input type="checkbox" class="ml10" id="sdci_chkDelDoc"><label for="">Del Doc.</label></span>
                                        <span><input type="radio" id="siteStatusRaido3" name="siteStatusRaido" onclick="clickReportSiteRadioType(3);"><label for="siteStatusRaido3">SDCI History List</label></span>
                                        <span><input type="radio" id="siteStatusRaido4" name="siteStatusRaido" onclick="clickReportSiteRadioType(4);"><label for="siteStatusRaido4">SDCI Behind List</label><input type="checkbox" class="ml10" id="sdci_allBehindList"><label for="">All Behind List</label></span>
                                        <span><input type="radio" id="siteStatusRaido5" name="siteStatusRaido" onclick="clickReportSiteRadioType(5);"><label for="siteStatusRaido5">SDCI Transmittal List</label></span>
                                        <span><input type="radio" id="siteStatusRaido6" name="siteStatusRaido" onclick="clickReportSiteRadioType(6);"><label for="siteStatusRaido6">SDCI Progress Status</label></span>
                                        <span><input type="radio" id="siteStatusRaido7" name="siteStatusRaido" onclick="clickReportSiteRadioType(7);"><label for="siteStatusRaido7">SDCI Workdone List</label></span>
                                        <span><input type="radio" id="siteStatusRaido8" name="siteStatusRaido" onclick="clickReportSiteRadioType(8);"><label for="siteStatusRaido8">SDCI Workdone Status</label></span>
                                        <span><input type="radio" id="siteStatusRaido9" name="siteStatusRaido" onclick="clickReportSiteRadioType(9);"><label for="siteStatusRaido9">SDCI Week Progress Graph</label></span>
                                        <span><input type="radio" id="siteStatusRaido10" name="siteStatusRaido" onclick="clickReportSiteRadioType(10);"><label for="siteStatusRaido10">SDCI Monthly Progress Graph</label></span>
                                        <span><input type="radio" id="siteStatusRaido11" name="siteStatusRaido" onclick="clickReportSiteRadioType(11);"><label for="siteStatusRaido11">WBS Detail</label></span>
                                    </div>
                                    <div class="right">
                                        <div class="tbl_wrap">
                                            <table class="write">
                                                <tr>
                                                    <th style="width:30%">Discipline</th>
                                                    <td style="display:flex;">
                                                        <select name="" id="s_discipline"></select>
                                                        <div id="s_checkSheet">
                                                        	<input type="checkbox" id="s_disciplineCheck" style="margin-left: 5px;margin-top: 0px;"><label for="">1 Sheet</label>
                                                        </div>
                                                    </td>
                                                    <td style="display:none;">
                                                        <select id="s_discipline_code_id"></select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Work Step</th>
                                                    <td><select name="" id="s_workStep"></select></td>
                                                </tr>
                                                <tr>
                                                    <th>Cut-Off Date</th>
                                                    <td><input type="text" class="date" id="s_cutOffDate"></td>
                                                </tr>
                                                <tr>
                                                    <th>Duration</th>
                                                    <td><input type="text" class="date" id="s_durationS"><input type="checkbox" class="ml5" id="s_durationCheck" onclick="durationCheckSite()"><label for="">All</label><input type="text" class="date ml5" id="s_durationD"></td>
                                                </tr>
                                                <tr>
                                                    <th>TR Order</th>
                                                    <td><select name="" id="s_trOrder">
                                                    	<option value="1">TR No별</option>
                                                    	<option value="2">Document No별</option>
                                                    </select></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="sec">
	                                <h4 class="reportTitleS" style="float: left;"></h4>
	                                <label id="reportCntListS" style="margin-left: 10px;"></label>
	                                <div class="tbl_wrap sdciList" id="data_table_site">
	
	                                </div>
                                </div>
                                
<div class="dialog pop_detailSDCIList" title="SDCI Detail List" style="max-width:1000px;">
    <div class="pop_box">
    	<div class="tbl_wrap sdciList2" id="data_table_site" style="overflow:auto;">
		</div>
    </div>
</div>                                   