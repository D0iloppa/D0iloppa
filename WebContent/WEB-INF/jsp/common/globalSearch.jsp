<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resource/js/globalSearch.js"></script>	

<style>
			/* 로딩*/
			#search_loading {
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
			
		.docDown_img{
		    height: 15px;
		}
		
		</style>

<div class="line mb20">
	<div class="left">
		<label for="" class="mr5">MAX DISPLAY</label> <select name="" id="max_Display">
			<option value="50">50</option>
			<option value="100" selected>100</option>
			<option value="500">500</option>
			<option value="1000">1000</option>
		</select>
		<div class="ml20">
			<input type="checkbox" id="chkAllPrj" checked><label for="">All Project</label>
		</div>
	</div>
	<div class="right">
		<button class="button btn_default" onclick="search_excel()">TO EXCEL</button>
		<button class="button btn_purple">CANCEL</button>
		<button id="searchBtn" class="button btn_default">SEARCH</button>
	</div>
</div>
<div class="sec">
	<h4>Search Conditions</h4>
	<div class="tbl_wrap">
		<table class="write">
			<colgroup>
				<col style="width: 120px">
				<col style="">
			</colgroup>
			<tr>
				<th>Searching Word</th>
				<td>
					<select name="" id="search_Opt1">
							<option value="like">Like</option>
							<option value="=">=</option>
							<option value="start with">Start with</option>
					</select> 
					<input id="searchKeyword" type="text" value=<%=request.getParameter("tabParam") %> ></input>
				</td>
			</tr>
			<tr>
			
			<style>
			/* 체크박스 un checked시 css 적용 오버라이딩 */ 
			.periodDiv input:disabled {
  				background-color: #efefef;
  				color: #ccc;
			}
			</style>
				<th><input id="periodChk" type="checkbox"> Period</th>
				<td>
				  <div class="periodDiv">
				 	<input type="text" id="searchFrom" class="date" disabled>
				 	<span>~</span>
				 	<input type="text" id="searchTo" class="date" disabled>
				  </div>
				</td>
			</tr>
		</table>
	</div>
</div>
<div class="acco sec">
	<h4>Basic Conditions</h4>
	<div class="tbl_wrap">
		<table class="write">
			<colgroup>
				<col style="width: 100px">
				<col style="">
			</colgroup>
			<tr>
				<th>Path</th>
				<td><input readonly type="text" id="searchPath" style="width: calc(100% - 200px);">
				<button class="button btn_gray ml5 btn_sel_folder" onclick="search_folderSelectOpen()">Select
						Folder</button></td>
			</tr>
			<tr>
				<th>Doc. No.</th>
				<td><select name="" class="basic_cond" id="">
						<option value="start with">Start with</option>
						<option value="like">Like</option>
						<option value="=">=</option>						
				</select> <input class="basic_cond" type="text"></td>
			</tr>
			<tr>
				<th>Title</th>
				<td><select class="basic_cond" name="" id="">
						<option value="start with">Start with</option>
						<option value="like">Like</option>
						<option value="=">=</option>						
				</select> <input class="basic_cond" type="text"></td>
			</tr>
		</table>
	</div>
</div>
<!-- <div class="acco sec"> -->
<div class="acco sec">
	<h4>Detail Conditions</h4>
	<div class="tbl_wrap">
		<table class="write">
			<colgroup>
				<col style="width: 150px">
				<col style="">
				<col style="width: 80px">
				<col style="">
			</colgroup>
			<tr>
				<th>Doc Type</th>
				<td><select class="detail_cond" name="" id="">
					<option value="all">All</option>
					<option value="general">General</option>
					<option value="engineering">Engineering</option>
					<option value="correspondence">Correspondence</option>
					<option value="procurement">Procurement</option>
					<option value="vendor">Vendor</option>
					<option value="site">Site</option>
					<option value="bulk">Bulk</option>
				</select></td>
				<th>Version</th>
				<td><select class="detail_cond" name="" id="">
					<option value="current">Current</option>
					<option value="all">All</option>
				</select></td>
			</tr>
			<tr>
				<th>Modifier</th>
				<td colspan="3"><input type="text" class="detail_cond full"></td>
			</tr>
			<tr>
				<th><input id="modDateChk" class="detail_cond" type="checkbox"> Modified Date</th>
				<td colspan="3">
				<div class="periodDiv">
					<input type="text" id="modFrom" class="date detail_cond" disabled>
					<span>~</span>
					<input type="text" id="modTo" class="date detail_cond" disabled>
				</div>				
			</tr>
		</table>
	</div>
</div>

<p class="result_line"><span>RESULT</span></p>

<div class="sec">
	<div class="search_label_withBtn" style="display:flex; justify-content: space-between;">
		<h4 id="docListTit">Document List - 0 found</h4>
		<button id="search_docDownBtn" class="button btn_default" onclick="search_download()">Download</button>
	</div>
	<div class="tbl_wrap scroll" id="documentList">
	</div>
</div>
<div class="sec">
	<h4 id="trListTit">Transmittal List - 0 found</h4>
	<div class="tbl_wrap scroll" id="trList">
	</div>
</div>
<div class="sec">
	<h4 id="drnListTit">DRN List - 0 found</h4>
	<div class="tbl_wrap scroll" id="drnList">
	</div>
</div>

<!-- select foler 다이얼로그 -->
<div class="dialog pop_doc_search" title="Select folder">
    <div class="pop_box">
    	<nav id="tree_pop"></nav>
    </div>
    <div class="btn_group right">
        <button id="folderSelect" class="button btn_blue" onclick="search_folderSelect()">Select</button>
    </div>
</div>


<!-- download rename -->
<div id="search_docDown_renm" class="dialog pop_multiDownload_search"
	title="Doc Download List" style="max-width: 1000px">
	<div class="pop_box">
		<h5 id="search_docDown_totalCountLabel">Total 0 Files</h5>
		<div class="tbl_wrap" id="search_docDown_downloadListGrid"></div>
		<div class="line">
			<input type="checkbox" style="margin-top: auto; margin-bottom: auto;"
				id="search_docDown_downloadFileRename" onchange="search_docDown_fileRenameCheck()">
			<label style="margin-left: 10px;" for="">File Rename (Variant: %DOCNO%, %REVNO%, %TITLE%)</label>
			<input type="text" class="full" id="search_docDown_fileRenameSet">
		</div>
		<form name="search_docDown_form" id="search_docDown_form" class="fileDownloadForm" action="search_docDown_selectedItemDownload.do" method="post">
			<div class="btn_wrap mt10 txt_center">
				<button type="submit" class="button btn_purple"
					id="search_docDown_fileMultiDownload">Download</button>
				<button type="button" class="button btn_blue"
					onclick="search_docDown_fileDownloadClose();">Close</button>
			</div>
		</form>
	</div>
</div>


<!-- 엑셀 form -->
<form id="searchExcelForm" name="searchExcelForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
		<input type="hidden" name="docJson" id="docJson"> 
		<input type="hidden" name="trJson" id="trJson">
		<input type="hidden" name="drnJson" id="drnJson"> 
		<input type="hidden" name="docJsonStr" id="docJsonStr"> 
		<input type="hidden" name="trJsonStr" id="trJsonStr">
		<input type="hidden" name="drnJsonStr" id="drnJsonStr"> 
</form>