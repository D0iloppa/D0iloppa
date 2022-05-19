<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resource/js/wbsCodeControl.js"></script>

<h4>WBS CODE CONTROL</h4>

<div class="pg_top">
	<!-- 
	<div class="left" style="display:flex">
		<div style="margin:auto;">Filter&nbsp;</div>
		<input type="text">
	</div>
	 -->
</div>
<form id="excelUploadForm" name="excelUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
<div class="pg_top">
	<div class="left" style="display:flex">	
		<input type="radio" name="search_radio" id="search_All" class="ml5" value="A" checked="checked"> <label class="DciChk" for="">ALL</label>&nbsp;
		<input type="radio" name="search_radio" id="search_Dci" class="ml5" value="D"> <label class="DciChk" for="">DCI</label>&nbsp;
		<input type="radio" name="search_radio" id="search_Mc" class="ml5" value="M"> <label class="McChk" for="">MCI</label>&nbsp;
		<input type="radio" name="search_radio" id="search_Mbci" class="ml5" value="B"> <label class="MciChk" for="">MBCI</label>&nbsp;
		<input type="radio" name="search_radio" id="search_Vdci" class="ml5" value="V"> <label class="VdciChk" for="">VDCI</label>&nbsp;
		<input type="radio" name="search_radio" id="search_Sdci" class="ml5" value="S"> <label class="SdciChk" for="">SDCI</label>&nbsp;&nbsp;&nbsp;	
		<input type="hidden" name="fileName" id="fileName"> 
		<input type="hidden" name="jsonRowdatas" id="jsonRowdatas">
		<input type="hidden" name="prj_id" id="prj_id_wbs"> 
		<input type="hidden" name="prj_nm" id="prj_nm_wbs">
		<input type="hidden" name="doc_type" id="doc_type_wbs">
		<input type="hidden" name="discip_code_id" id="discip_code_id_wbs">
		
		
		<div style="margin:auto;">Discipline&nbsp;</div>
		 <div class="multi_sel_wrap" id="WBS_selected_div">
		 	<input type="hidden" id="WBS_selectPrjId">
		 	<button class="multi_sel" id="WBS_selected_display">ALL</button>
		 	<div class="multi_box" id="WBS_header_multi_box">
		 		<table class="header_prj_select_table">
		 			<tr>
		 				<th>DISCIPLINE</th>
		 			</tr>
		 			<tr class="WBS_disc_select" id="WBS_discip_all">
		 					<td>ALL</td>
		 				</tr>
		 			<c:forEach var="i" items="${discipList}" varStatus="vs">
		 				<tr class="WBS_disc_select" id="WBS_discip_${i.discip_code_id}">
		 					<td>${i.discip_display}</td>
		 				</tr>
		 			</c:forEach>
		 		</table>
		 	</div>
		 </div>	
	</div>
	<div class="right">
		<button class="button btn_default" onclick="searchwbsCode()">Retrieve</button>
		<button class="button btn_default" onclick="fromExcelBtn()">From Excel</button>
		<button class="button btn_default" onclick="toExcelBtn()">To Excel</button>
		<button class="button btn_default" onclick="savewbsCode()">Save</button>
		<button class="button btn_default" onclick="deletewbsCode()">Delete</button>
	</div>
</div>

<div class="tbl_wrap boardList" id="wbsCodeList"></div>


	<input type="file" name="wbsExcelfile" style="display:none;" onchange = "fileUp(this)"/>
</form>


<div id="pop_WBSDelete" class="dialog pop_doc_search" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="WBSDelete()">Delete</button>
                <button class="button btn_purple" onclick="WBSDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>
