<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
								<div class="pg_top">
                                    <div class="right">
                                        <button class="button btn_blue" onclick="RetrieveVen();">Retrieve</button>
                                        <button class="button btn_purple" onclick="downloadReportVen();">To Excel</button>
                                    </div>
                                </div>
                                <h4>Report 종류</h4>
                                <div class="reportEngStat">
                                    <div class="left">
                                        <span><input type="radio" id="vendorStatusRaido1" name="vendorStatusRaido" onclick="clickReportVenRadioType(1);"><label for="vendorStatusRaido1">VDCI List</label></span>
                                        <span><input type="radio" id="vendorStatusRaido2" name="vendorStatusRaido" onclick="clickReportVenRadioType(2);"><label for="vendorStatusRaido2">VDCI Detail List</label><input type="checkbox" class="ml10" id="vdci_chkDelDoc"><label for="">Del Doc.</label></span>
                                        <span><input type="radio" id="vendorStatusRaido3" name="vendorStatusRaido" onclick="clickReportVenRadioType(3);"><label for="vendorStatusRaido3">VDCI History List</label></span>
                                        <span><input type="radio" id="vendorStatusRaido4" name="vendorStatusRaido" onclick="clickReportVenRadioType(4);"><label for="vendorStatusRaido4">VDCI Behind List</label><input type="checkbox" class="ml10" id="vdci_allBehindList"><label for="">All Behind List</label></span>
                                        <span><input type="radio" id="vendorStatusRaido5" name="vendorStatusRaido" onclick="clickReportVenRadioType(5);"><label for="vendorStatusRaido5">VDCI Transmittal List</label></span>
                                        <span><input type="radio" id="vendorStatusRaido6" name="vendorStatusRaido" onclick="clickReportVenRadioType(6);"><label for="vendorStatusRaido6">VDCI Progress Status</label></span>
                                        <span><input type="radio" id="vendorStatusRaido7" name="vendorStatusRaido" onclick="clickReportVenRadioType(7);"><label for="vendorStatusRaido7">VDCI Workdone List</label></span>
                                        <span><input type="radio" id="vendorStatusRaido8" name="vendorStatusRaido" onclick="clickReportVenRadioType(8);"><label for="vendorStatusRaido8">VDCI Workdone Status</label></span>
                                        <span><input type="radio" id="vendorStatusRaido9" name="vendorStatusRaido" onclick="clickReportVenRadioType(9);"><label for="vendorStatusRaido9">VDCI Week Progress Graph</label></span>
                                        <span><input type="radio" id="vendorStatusRaido10" name="vendorStatusRaido" onclick="clickReportVenRadioType(10);"><label for="vendorStatusRaido10">VDCI Monthly Progress Graph</label></span>
                                        <span><input type="radio" id="vendorStatusRaido11" name="vendorStatusRaido" onclick="clickReportVenRadioType(11);"><label for="vendorStatusRaido11">WBS Detail</label></span>
                                    </div>
                                    <div class="right">
                                        <div class="tbl_wrap">
                                            <table class="write">
                                                <tr>
                                                    <th style="width:30%">Discipline</th>
                                                    <td style="display:flex;">
                                                        <select name="" id="v_discipline"></select>
                                                        <div id="v_checkSheet">
                                                        	<input type="checkbox" id="v_disciplineCheck" style="margin-left: 5px; margin-top: 0px;"><label for="">1 Sheet</label>
                                                        </div>
                                                    </td>
                                                    <td style="display:none;">
                                                        <select id="v_discipline_code_id"></select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Work Step</th>
                                                    <td><select name="" id="v_workStep"></select></td>
                                                </tr>
                                                <tr>
                                                    <th>Cut-Off Date</th>
                                                    <td><input type="text" class="date" id="v_cutOffDate"></td>
                                                </tr>
                                                <tr>
                                                    <th>Duration</th>
                                                    <td><input type="text" class="date" id="v_durationS"><input type="checkbox" class="ml5" id="v_durationCheck" onclick="durationCheckVen()"><label for="">All</label><input type="text" class="date ml5" id="v_durationD"></td>
                                             	</tr>
                                                <tr>
                                                    <th>TR Order</th>
                                                     <td><select name="" id="v_trOrder">
                                                    	<option value="1">TR No별</option>
                                                    	<option value="2">Document No별</option>
                                                    </select></td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="sec">
	                                 <h4 class="reportTitleV" style="float: left;"></h4>
		                             <label id="reportCntListV" style="margin-left: 10px;"></label>
	                                 <div class="tbl_wrap vdciList" id="data_table_vendor">
                                 	 </div>
                                </div>
                                
                                <!-- VDCI Progress Status Total Index 클릭 팝업창 -->           
<div class="dialog pop_detailVDCIList" title="VDCI Detail List" style="max-width:1000px;">
    <div class="pop_box">
    	<div class="tbl_wrap vdciList2" id="data_table_vendor" style="overflow:auto;">
		</div>
    </div>
</div>   