<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <META http-equiv="Pragma" content="no-cache">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>도서관리시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/default.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/tabulator.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/contextmenu/jquery.contextMenu.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/all.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/selectize.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
    <!--jstree-->
    <script src="${pageContext.request.contextPath}/resource/js/jquery-3.4.1.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery-ui.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jstree.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/contextmenu/jquery.contextMenu.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/contextmenu/jquery.ui.position.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/all.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jqueryui.dialog.fullmode.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/script.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery_dialogextend.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery_dialogextend_min.js"></script>
    <%-- <script src="${pageContext.request.contextPath}/resource/js/contextmenu.js"></script> --%><!-- 공통JS -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/tabulatormodule/sheetjs/xlsx.full.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tabulator.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/moment.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/selectize.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/summernote-lite.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/lang/summernote-ko-KR.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/summernote/summernote-lite.css">
<!-- pdf 저장 -->
<script src="${pageContext.request.contextPath}/resource/js/html2canvas.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/jspdf.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/printThis.js"></script>

<script src="${pageContext.request.contextPath}/resource/js/authCheck.js"></script>
<style>
.progressDiv {
	width: 100%;
	height: 100%;
	top: 0px;
	left: 0px;
	position: absolute;
	background-color: rgba(255, 255, 255, 0.5);
	z-index: 199;
}
.progress{
	width:100%;
	z-index: 200;
    top: 50%;
    position: relative;
}
</style>
</head>
<html>

<div class="dialog pop_multiDownload" title="Multi Download" style="max-width:1000px">
	<div class="pop_box">
		<h5></h5>
		<div class="tbl_wrap" id="multiDownTbl"></div>
		<div class="line">
			<input type="checkbox" id="downloadFileRename" onchange="fileRenameCheck()"><label for="">File Rename (Variant : %DOCNO%, %REVNO%, %TITLE%)</label>
			<input type="text" class="full" id="fileRenameSet">
		</div>
		<div class="btn_wrap mt10 txt_center">
			<button class="button btn_purple" id="fileMultiDownload">Download</button>
			<button class="button btn_blue" onclick="fileDownloadClose()">Close</button>
		</div>
		<form style="display: none;" action="/fileDownload.do" method="POST" id="downloadform">
		</form>
	</div>
</div>
<div class="dialog pop_alertDialog" title="" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;" id="alertText">
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="alertDialogOk()">확인</button>
            </div>
    </div>
</div>
</html>
<script src="${pageContext.request.contextPath}/resource/js/angular.min.js"></script>
<script>
var loading = '<div id="loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';
var progressbar = '<div class="progressDiv"><div class="progress"><div class="bar progress-bar progress-bar-info" role="progressbar" style="width: 0%"></div></div></div>';

$(function(){
	$(".pop_multiDownload").dialog({
		autoOpen: false,
		maxWidth: 1000,
		width: "auto"
	}).dialogExtend({
		"closable" : true,
		"maximizable" : true,
		"minimizable" : true,
		"minimize" : function(evt) {
			$(".ui-widget.ui-widget-content:not(.ui-datepicker)").addClass("position");
		},
		"maximize" : function(evt) {
			$(".ui-dialog .ui-dialog-content").addClass("height_reset");
			$(this).closest(".ui-dialog").css("height","100%");
			$(this).closest(".ui-widget.ui-widget-content").addClass("wd100");
		},
		"beforeMinimize" : function(evt) {
			$(".ui-dialog-title").show();
		},
		"beforeMaximize" : function(evt) {
			$(".ui-widget.ui-widget-content:not(.ui-datepicker)").removeClass("position");
			$(this).closest(".ui-dialog").css("height","auto");
			$(".ui-dialog .ui-dialog-content").removeClass("height_reset");
			$(".ui-dialog-title").show();
		},
		"beforeRestore" : function(evt) {
			$(".ui-widget.ui-widget-content:not(.ui-datepicker)").removeClass("position");
			$(this).closest(".ui-dialog").css("height","auto");
			$(".ui-dialog .ui-dialog-content").removeClass("height_reset");
			$(".ui-dialog-title").show();
		},
		"minimizeLocation" : "left"
	});
});

function fileRenameCheck(){
	if($('#downloadFileRename').is(':checked')) $('#fileRenameSet').val('%DOCNO%_%REVNO%_%TITLE%');
}
var multiDownloadTable;

var server_domain = opener.document.getElementById('server_domain').value;
var current_server_domain = opener.document.getElementById('current_server_domain').value;
function fileMultiDownload(){
	
	var selectedArray = multiDownloadTable.getData();
	var downloadFiles = '';
	var accHisDownList = [];
	for(let i=0;i<selectedArray.length;i++){
		downloadFiles += selectedArray[i].bean.doc_id +'%%'+ selectedArray[i].bean.rev_id + '@@';
		accHisDownList.push(JSON.stringify(selectedArray[i].bean));
	}
	$.ajax({
		url: '../insertAccHisList.do',
		type: 'POST',
		traditional : true,
		async: false,
		data:{
			jsonRowDatas: accHisDownList,
			prj_id: selectPrjId,
			method: 'DOWN'
		},
		success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	//console.log("downloadFiles = "+downloadFiles);
	downloadFiles = downloadFiles.slice(0,-2);
	let renameYN='N';
	if($('#downloadFileRename').is(':checked')) renameYN='Y';
	let fileRenameSet = $('#fileRenameSet').val();
	if(selectedArray.length===1){
		location.href=current_server_domain+'/downloadFileRename.do?prj_id='+encodeURIComponent(selectPrjId)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
	}else{
		location.href=current_server_domain+'/fileDownload.do?prj_id='+encodeURIComponent(selectPrjId)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
	}
	$(".pop_multiDownload").dialog("close");
}
function fileDownloadClose(){
	$(".pop_multiDownload").dialog("close");
}
fnSleep = function (delay){
    var start = new Date().getTime();
    while (start + delay > new Date().getTime());
};

//alert창 함수 오버라이딩
function alert(arg){
 //console.log(arg);
	
	$(".pop_alertDialog").dialog({
	    draggable: true,
	    autoOpen: false,
	    maxWidth:1000,
	    width:"auto",
	}).dialogExtend({
		"closable" : true,
		"maximizable" : true,
		"minimizable" : true,
		"minimize" : function(evt) {
			$(".ui-widget.ui-widget-content:not(.ui-datepicker)").addClass("position");
		},
		"maximize" : function(evt) {
			$(".ui-dialog .ui-dialog-content").addClass("height_reset");
			$(this).closest(".ui-dialog").css("height","100%");
			$(this).closest(".ui-widget.ui-widget-content").addClass("wd100");
		},
		"beforeMinimize" : function(evt) {
			$(".ui-dialog-title").show();
		},
		"beforeMaximize" : function(evt) {
			$(".ui-widget.ui-widget-content:not(.ui-datepicker)").removeClass("position");
			$(this).closest(".ui-dialog").css("height","auto");
			$(".ui-dialog .ui-dialog-content").removeClass("height_reset");
			$(".ui-dialog-title").show();
		},
		"beforeRestore" : function(evt) {
			$(".ui-widget.ui-widget-content:not(.ui-datepicker)").removeClass("position");
			$(this).closest(".ui-dialog").css("height","auto");
			$(".ui-dialog .ui-dialog-content").removeClass("height_reset");
			$(".ui-dialog-title").show();
		},
		"minimizeLocation" : "left"
	});
	
 $(".pop_alertDialog").dialog("open");
 $("#alertText").html(arg);
}
function alertDialogOk() {
	$(".pop_alertDialog").dialog("close");
	return true;
}
</script>