<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="dialog pop_multiDownload" title="Multi Download" style="max-width:1000px">
	<div class="pop_box">
		<h5></h5>
		<div class="tbl_wrap" id="multiDownTbl"></div>
		<div class="line">
			<input type="checkbox" id="downloadFileRename" onchange="fileRenameCheck()"><label for="">File Rename (Variant : %DOCNO%, %REVNO%, %TITLE%)</label>
			<input type="text" class="full" id="fileRenameSet">
		</div>
		<div class="btn_wrap mt10 txt_center">
			<form name="multiDownload_form" id="multiDownload_form" class="multiFileDownloadForm" action="fileDownload.do" method="post">
			<button type="submit" class="button btn_purple" id="fileMultiDownload">Download</button>
			<button class="button btn_blue" onclick="fileDownloadClose()">Close</button>
			</form>
		</div>
		<form style="display: none;" action="/fileDownload.do" method="POST" id="downloadform">
		</form>
	</div>
</div>