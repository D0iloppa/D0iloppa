<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<style>
.docDown_img{
    height: 15px;
}
#docDownload_table th, td {
    border: 1px solid #eee;
  }
</style>

<h4>DOC 최종본 DOWNLOAD</h4>
<div class="pg_top">
	<div class="right">
		<button class="button btn_default" onclick="docDown_retrieve()">Retrieve</button>
	</div>
</div>
<div class="sec">
	<div class="tbl_wrap" style="overflow-x: unset">
		<table class="write">
			<tr>
				<th>PROJECT</th>
				<td>
					<div class="multi_sel_wrap" id="docDownload_select_div">
						<button class="multi_sel" id="selected_prj_docDownload">SELECT</button>
						<div class="multi_box" id="docDownload_multi_box">
							<div class="prj_select_grid">
								<div class="prj_search" style="padding: 10px;">
									<div class="left">
										<label style="color: #000;" for="">Project Search</label>
										<input id="prj_search_docDown" type="text">
										<button class="button btn_search" onclick="prjSearch_docDown()">검색</button>
									</div>
								</div>
								<div id="prj_list_grid_docDown"></div>
							</div>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<th>FOLDER</th>
				<td style="display:flex;">
					<input readonly id="docDown_folderPath" type="text" style="width: 50%; margin-right: 15px;">
					<button class="button btn_default" onclick="folderSelect_docDown()">폴더선택</button>
				</td>
			</tr>
			<tr>
				<th>DISCIPLINE</th>
				<td>
					<select id="docDown_discipSelect" style="min-width: 75px;" onChange="docDown_discipOnselect()"></select>
				</td>
			</tr>

		</table>
	</div>
	<div>
		<div style="display: flex;">
			<h4 id="docDownload_numberOfFindedDoc_label">0 documents are remaining</h4>
			<button class="button btn_default" onclick="docDown_renameDialog()" style="margin-left: auto;">Download</button>
		</div>
		<!-- 테이블 -->
<!-- 		<div class="tbl_wrap"> -->
<!-- 			<table id="docDownload_table"> -->
<!-- 				<thead> -->
<!-- 					<tr> -->
<!-- 						<th class="docDown_chk">chk</th> -->
<!-- 						<th class="docDown_docIcon">Doc</th> -->
<!-- 						<th class="docDown_docNo">Doc.No</th> -->
<!-- 						<th class="docDown_revNo">Revision</th> -->
<!-- 						<th class="docDown_title">Title</th> -->
<!-- 						<th class="docDown_modified">Modified</th> -->
<!-- 						<th class="docDown_size">Size</th> -->
<!-- 					</tr> -->
<!-- 				</thead> -->
<!-- 				<tbody id="docDownload_tableBody"> -->
<!-- 				</tbody> -->
<!-- 			</table> -->
<!-- 		</div> -->
		
		<div class="tbl_wrap table_grids" id="docDownload_grid"></div>
	</div>
	
	<!--folder 선택 dialog -->
	<div id="docDown_dialog" class="dialog" title="Select folder">
		<div class="pop_box">
			<nav id="docDown_tree"></nav>
		</div>
		<div class="btn_group right">
			<button id="docDown_folderSelect" class="button btn_blue" onclick="docDownDialog_folderSelect()">Select</button>
		</div>
	</div>
	
	<!-- download rename -->
	<div id="docDown_renm" class="dialog pop_multiDownload_maxRev" title="Doc Download List" style="max-width:1000px">
	<div class="pop_box">
		<h5 id="docDown_totalCountLabel">Total 0 Files</h5>
		<div class="tbl_wrap" id="docDown_downloadListGrid"></div>
		<div class="line">
			<input type="checkbox" style="margin-top:auto; margin-bottom:auto;" id="docDown_downloadFileRename" onchange="docDown_fileRenameCheck()">
			<label style="margin-left:10px;" for="">File Rename (Variant : %DOCNO%, %REVNO%, %TITLE%)</label>
			<input type="text" class="full" id="docDown_fileRenameSet">
		</div>
			<form name="docDown_form" id="docDown_form" class="fileDownloadForm" action="docDown_selectedItemDownload.do" method="post">
				<div class="btn_wrap mt10 txt_center">
					<button type="submit" class="button btn_purple" id="docDown_fileMultiDownload">Download</button>
					<button type="button" class="button btn_blue" onclick="docDown_fileDownloadClose();">Close</button>
				</div>
			</form>
		</div>
	</div>
</div>