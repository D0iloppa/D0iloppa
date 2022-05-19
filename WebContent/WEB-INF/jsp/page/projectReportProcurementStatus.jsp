<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>


                                <div class="pg_top">
                                    <div class="right">
                                        <button class="button btn_blue" onclick="RetrievePro();">Retrieve</button>
                                        <button class="button btn_purple" onclick="downloadReportPro();">To Excel</button>
                                    </div>
                                </div>
                                <h4>Report 종류</h4>
                                <div class="reportEngStat">
                                    <div class="left">
                                        <span><input type="radio" id="procurementRaido1" name="procurementRaido" onclick="clickReportProRadioType(1);"><label for="procurementRaido1">PCI List</label></span>
                                        <span><input type="radio" id="procurementRaido2" name="procurementRaido" onclick="clickReportProRadioType(2);"><label for="procurementRaido2">PCI Detail List</label><input type="checkbox" class="ml10" id="pci_chkDelDoc"><label for="">Del Doc.</label></span>
                                        <span><input type="radio" id="procurementRaido3" name="procurementRaido" onclick="clickReportProRadioType(3);"><label for="procurementRaido3">PCI Behind List<input type="checkbox" class="ml10" id="pci_allBehindList"><label for="">All Behind List</label></span>
                                        <span><input type="radio" id="procurementRaido4" name="procurementRaido" onclick="clickReportProRadioType(4);"><label for="procurementRaido4">PCI Progress Status</label></span>
                                        <span><input type="radio" id="procurementRaido5" name="procurementRaido" onclick="clickReportProRadioType(5);"><label for="procurementRaido5">PCI Workdone List</label></span>
                                        <span><input type="radio" id="procurementRaido6" name="procurementRaido" onclick="clickReportProRadioType(6);"><label for="procurementRaido6">PCI Workdone Status</label></span>
                                        <span><input type="radio" id="procurementRaido7" name="procurementRaido" onclick="clickReportProRadioType(7);"><label for="procurementRaido7">PCI Week Progress Graph</label></span>
                                        <span><input type="radio" id="procurementRaido8" name="procurementRaido" onclick="clickReportProRadioType(8);"><label for="procurementRaido8">PCI Monthly Progress Graph</label></span>
                                        <span><input type="radio" id="procurementRaido9" name="procurementRaido" onclick="clickReportProRadioType(9);"><label for="procurementRaido9">WBS Detail</label></span>
                                    </div>
                                    <div class="right">
                                        <div class="tbl_wrap">
                                            <table class="write">
                                                <tr>
                                                    <th style="width:30%">Discipline</th>
                                                    <td style="display:flex;">
                                                        <select name="" id="p_discipline"></select>
                                                        <div id="p_checkSheet">
                                                        	<input type="checkbox" id="p_disciplineCheck" style="margin-left: 5px; margin-top: 0px;"><label for="">1 Sheet</label>
                                                        </div>
                                                    </td>
                                                    <td style="display:none;">
                                                        <select id="p_discipline_code_id"></select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Work Step</th>
                                                    <td><select name="" id="p_workStep"></select></td>
                                                </tr>
                                                <tr>
                                                    <th>Cut-Off Date</th>
                                                    <td><input type="text" class="date" id="p_cutOffDate"></td>
                                                </tr>
                                                <tr>
                                                    <th>Duration</th>
                                                     <td><input type="text" class="date" id="p_durationS"><input type="checkbox" class="ml5" id="p_durationCheck" onclick="durationCheckProcu()"><label for="">All</label><input type="text" class="date ml5" id="p_durationD"></td>
                                             	</tr>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="sec">
                                 	<h4 class="reportTitleP" style="float: left;"></h4>
	                                <label id="reportCntListP" style="margin-left: 10px;"></label>
                                	<div class="tbl_wrap pciList" id="data_table_procurement">
                                	</div>
                                </div>
                                
                                <!-- PCI Progress Status Total Index 클릭 팝업창 -->           
<div class="dialog pop_detailPCIList" title="PCI Detail List" style="max-width:1000px;">
    <div class="pop_box">
    	<div class="tbl_wrap pciList2" id="data_table_procurement" style="overflow:auto;">
		</div>
    </div>
</div>    