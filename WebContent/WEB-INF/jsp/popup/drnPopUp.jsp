<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <META http-equiv="Pragma" content="no-cache">
		<link rel="apple-touch-icon" sizes="114x114" href="${pageContext.request.contextPath}/resource/images/apple-touch-icon.png">
		<link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/resource/images/favicon-32x32.png">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery-ui.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/font.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/default.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/tabulator.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/contextmenu/jquery.contextMenu.min.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/all.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.min.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
<%-- 	   	<script src="${pageContext.request.contextPath}/resource/js/angular.min.js"></script> --%>
	   	<script src="${pageContext.request.contextPath}/resource/js/jquery-3.4.1.js"></script>
    	<script src="${pageContext.request.contextPath}/resource/js/jquery-ui.js"></script>
    	<script src="${pageContext.request.contextPath}/resource/js/tabulator.js"></script>
		<script src="${pageContext.request.contextPath}/resource/js/drnUp.js"></script>
		<script src="${pageContext.request.contextPath}/resource/js/onceLoad.js"></script>
		<script src="${pageContext.request.contextPath}/resource/js/jstree.js"></script>
		
		
		<style>
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
		</style>
		

	<script>

		var prj_id = ${prj_id};
		// var prj_id = '${prj_id}';
		var pageMode = ${pageMode};
		var docData = ${docData};
		var discipObj = ${discipObj};
		var folder_id = ${folder_id};
		var pageData = ${pageData};
		var approval = ${approval};
		var session_user_id = '${session_user_id}';
		var context_path = '${pageContext.request.contextPath}';
		
		var getter = {};
		getter.prj_info = prj_id;
        getter.pageMode =  pageMode;
		getter.docData =  docData;
		getter.discipObj =  discipObj; 
		getter.folder_id =  folder_id;
		getter.pageData =  pageData;
		getter.approval =  approval;
		getter.session_user_id = session_user_id;
		getter.context_path = context_path;
		
		
		function getParams(){
			return getter;
		}
		
	</script>
	
	
<div class="drn_popUP" style="padding:20px;">
	<div class="pop_box">
		<div class="sec">
			<div class="pg_top">
				<div id="drn_as"></div>
				<div class="right">
					<button id="drn_setLineBtn" class="button btn_blue drn_btns drn_readMode">결재선 지정</button>
					<button id="drn_tempSaveBtn" class="button btn_blue drn_btns drn_readMode">임시저장</button>
					<button id="drn_UpBtn" class="button btn_blue drn_btns drn_readMode">결재상신</button>
					<button id="drn_Accept" class="button btn_blue drn_btns drn_readMode">승인</button>
					<button id="drn_Deny" class="button btn_blue drn_btns drn_readMode">반려</button>
					<button id="drn_PrintBtn" class="button btn_blue drn_btns" style="display:none;">Print</button>
				</div>
			</div>
			<div class="tbl_wrap">
				<table class="write">
					<col style="width: 120px">
					<col style="">
					<tr>
						<th style="text-align: center;">DRN 제목</th>
						<td><input type="text" class="drn_inputs drn_read" id="DRN_title_write" disabled="true" style="width: 100%;"></td>
						<th style="text-align: center;">DRN NO</th>
						<td><input type="text" class="drn_inputs" id="DRN_no_write" style="width: 100%;" disabled="true" readonly></td>
					</tr>

				</table>
			</div>
			<div class="tbl_wrap">
				<table class="write">
					<colgroup>
						<col style="width: 120px">
						<col style="">
					</colgroup>
					<tr>
						<th style="text-align: center;">PROJECT NO</th>
						<td><input type="text" class="drn_inputs" id="Project_no_write"
							style="width: 100%;" disabled="true" readonly></td>
						<th style="text-align: center;">PROJECT NAME</th>
						<td><input type="text" class="drn_inputs" id="Project_name_write"
							style="width: 100%;" disabled="true" readonly></td>
						<th style="text-align: center;">DATE</th>
						<td><input type="text" class="drn_inputs" id="Date_write" style="width: 100%;"
							disabled="true" readonly></td>
					</tr>
					<tr>
						<th style="text-align: center;">DOC NO (REV NO)</th>
						<td colspan="3"><input type="text" class="drn_inputs" id="Doc_no_write"
							style="width: 100%;" disabled="true" readonly></td>
						<th style="text-align: center;" class="required">STATUS <i>required</i></th>
						<td id="td_status_wirte"><select id="Status_write" class="drn_inputs" style="width: 100%;"></select></td>
					</tr>
					<tr>
						<th style="text-align: center;">DOC TITLE</th>
						<td colspan="5"><input type="text" class="drn_inputs" id="Doc_title_write"
							style="width: 100%;"></td>
					</tr>
					<tr>
						<th style="text-align: center;" class="required">첨부문서 <i>required</i></th>
						<td colspan="4">
							<div class="drn_grid" id="drn_FileGrid"></div>
						</td>
						<td align="center">
							<button class="button btn_blue drn_file_btns drn_readMode" style="width: 50%;" onclick="drn_systemBtn()">시스템</button>
							<button class="button btn_blue drn_file_btns drn_readMode" style="width: 50%;" onclick="drn_localBtn()">PC</button>
<!-- 							<button class="button btn_orange drn_file_btns drn_readMode" style="width: 50%;" onclick="drn_fileDel()">삭제</button> -->
							<button class="button btn_blue drn_file_btns" style="width: 50%; display:none;" onclick="drnFileDownload()">Download</button>
							<button id="drnFileDelBtn" class="button btn_orange drn_file_btns drn_readMode" style="display:none; width: 50%;" onclick="drn_fileDel()">삭제</button>
						</td>
					</tr>
					<tr>
						<th style="text-align: center;">로컬파일</th>
						<td colspan="4">
							<div id="localFileTd"><a id="drn_localFile_a"></a></div>
							<form id="drnUploadForm" name="drnUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
								<input type="file" class="drn_inputs" name="drnLocalFile" style="display: none;" onchange="drn_LocalFile(this)" />
							</form>
						</td>
						<td align="center">
						<button id="drnLocalFileDelBtn" class="button btn_orange drn_file_btns drn_readMode" style="display:none; width: 50%;" onclick="drn_fileDel2()">삭제</button>
						</td>
					</tr>
				</table>
			</div>

			<div class="tbl_wrap">
				<table class="write">
					<colgroup>
						<col style="width: 100px">
						<col style="">
					</colgroup>
					<tr>
						<th style="text-align: center;" class="required" colspan="2">CHECK SHEET TITLE<i>required</i></th>
						<td style="width:50%;">
							<input id="drn_check_sheet_title_input" onmouseout="checksheetMouseOut()" onmouseover="checksheetMouseOver()" class="drn_inputs drn_read" type="text" style="width: 100%;" placeholder="INPUT CHECK SHEET TITLE">
						</td>
						<th style="text-align: center;" id="drn_itemCount" class="drn_readMode">ITEM갯수</th>
						<td id="drn_itemCount_btnDiv" colspan="1">
							<button class="button btn_blue drn_btns drn_readMode" onclick="itemsAdd()">추가</button>
							<button class="button btn_orange drn_btns drn_readMode" onclick="itemsDel()">삭제</button>
						</td>
					</tr>
					<tr>
						<td colspan="3" style="border: none;">
							<button class="button btn_blue drn_btns drn_readMode" onclick="getChkSheet()">CHECK SHEET 가져오기</button>
						</td>
						<td colspan="2" style="border: none;">
							<div class = "drn_readMode"> 체크결과 일괄적용 :
								<select id ="drn_allSelect">
									<option>선택</option>
									<option value="1">O</option>
									<option value="2">X</option>
									<option value="3">△</option>
									<option value="4">N/A</option>
								</select>
								<button class="button btn_blue" id="drn_allSelectBtn" style="display:none">적용</button>
							</div>
						</td>
					</tr>
				</table>
			</div>

			<div class="tbl_wrap">
				<div class="drn_grid" id="drn_SheetGrid"></div>
				<table>
<!-- 							<colgroup> -->
<!-- 								<col style="width: 150px"> -->
<!-- 							</colgroup> -->
						<!-- <tbody style="width:1100px">-->
						<tbody style="width:100%">
<!-- 							<tr> -->
<!-- <!-- 							<td style="border-left:0.5px solid #ddd; border-right:0.5px solid #ddd" colspan="17"></td> -->
<!-- 								<td style="width:23%;border-left:0.5px solid #ddd;" ></td> -->
<!-- 								<td style="width:46.1%;" ></td> -->
<!-- 								<td style="border-right:0.5px solid #ddd" ></td> -->
<!-- 							</tr> -->
							<tr>
								<td style="width:23%; border-top:0.5px solid #ddd; border-left:0.5px solid #ddd; border-right:0.5px solid #ddd; border-bottom:0.5px solid #ddd; text-align: center;">Review Conclusion</td>
								<td style="padding-left:10px ; padding-right:10px; width:46.1%; border-top:0.5px solid #ddd; border-bottom:0.5px solid #ddd">
									<textarea style="width:100%;height: 100px;resize: none;" id="drn_review_conclusion"></textarea>
								</td>
								<td style="padding: 0; border-bottom:0.5px solid #ddd">
									<div style="margin:auto;overflow:hidden;"class="drn_grid" id="drn_ReviewConGrid"></div>
								</td>
<!-- 								<td colspan="3" style="border-left:0.5px solid #ddd;">Review Conclusion</td> -->
<!-- 								<td colspan="9" style="" id="drn_review_conclusion"></td> -->
<!-- 								<td colspan="4" style="padding: 0;"> -->
<!-- 									<div style="margin:auto;overflow:hidden;"class="drn_grid" id="drn_ReviewConGrid"></div> -->
<!-- 								</td> -->
<!-- 								<td style="width:5%;border-right:0.5px solid #ddd;"></td>					 -->
							</tr>
						</tbody>						
					</table>
			</div>
			
			<div class="">
					
				<p>※ ○ : Acceptable, X : Not Acceptable, △ : Conditionally
					acceptable as commented , N/A : Not Applicable</p>
			</div>
		</div>
	</div>
</div>

<!-- 결재선 지정 모달창 -->
<div class="dialog pop_mngAppr" title="결재선 지정" style="max-width:1000px">
    <div class="pop_box">
        <div class="pg_top">
            <div class="left">
                <label for="">이름</label>
                <input id="drn_nm_search"type="text"> <button class="button btn_search" onclick="drnUserSearch()">검색</button>
            </div>
            <div class="right">
                <button class="button btn_blue" onclick="setLine_apply()">적용</button>
                <button class="button btn_blue" onclick="setLine_close()">닫기</button>
            </div>
        </div>
        <hr>
        <div class="appr">
            <div class="left">
                <nav id="mngAppr_userGroupTree">
                </nav>
            </div>
            <div class="right">
                <div id="apprUser_Grid" class="tbl_wrap apprUserList"></div>
                <div style="display:flex;">
                	<h4>결재자 선택</h4>
					<input style="margin-top:0px;margin-left:5px" type="radio" name="drn_apprUser_Sel" value="1" checked="checked"> REVIEWER
					<input style="margin-top:0px;" type="radio" name="drn_apprUser_Sel" value="2">APPROVER
					<h4 style="margin-left:5px;">
                    <button class="button btn_blue btn_small" onclick="setLine_addBtn()">적용</button>
<!--                     <button class="button btn_blue btn_small" onclick="setLine_approveBtn()">결재</button> -->
                    <button class="button btn_blue btn_small" onclick="setLine_delBtn1()">해제</button>
                    </h4>
                </div>
                <div class="tbl_wrap">
                	<div id="approverGrid"></div>
                </div>
                <h4>배포선 선택
                    <button class="button btn_blue btn_small" onclick="setLine_distributeBtn()">추가</button>
                    <button class="button btn_blue btn_small" onclick="setLine_delBtn2()">삭제</button>
                </h4>
                <div class="tbl_wrap">
                	<div id="distributionLineGrid"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="drn_fileDialog" class="dialog" title="Document Select" style="max-width: 1300px;">
	<div class="pop_box">
		<section ng-app="angularResizable2" id="drnFile_left">
			<div class="content">
				<div class="row" resizable r-flex="true">
					<section id="drnFile_pop_ed_left" resizable r-directions="['right']"
						r-flex="true" class="p_ed_left">
						<nav id="drnFile_docSelTree"></nav>
					</section>
					<section id="drnFile_pop_ed_right" style="flex:2.5;">
						<div class="pg_top">
							<div class="left">
								<label for="">Path</label>
								<input type="text" id="drnFile_docsel_current_folder_path" style="width:600px" readOnly>
							</div>
						</div>
						<div class="pg_top">
							<div class="left">
								<select name="" id="drnFile_docsel_select_rev_version" class="ml5">
									<option value="current">Current Version</option>
									<option value="all">All Version</option>
								</select>
								<button class="button btn_orange" onclick="drn_systemFileSelect()">
									Select Document</button>
								<button class="btn_icon btn_green" onclick="getPrjDocumentIndexList()">
									<i class="fas fa-share-square"></i> <span>Retrieve</span>
								</button>
								<button class="btn_icon btn_green" disabled onclick="dcMultiUpload()">
									<i class="fas fa-upload"></i> <span>Upload</span>
								</button>
								<button class="btn_icon btn_green" onclick="documentControlDownload()">
									<i class="fas fa-download"></i> <span>Download</span>
								</button>
								<button class="btn_icon btn_green" onclick="documentExcel()">
									<i class="far fa-file-excel"></i> <span>excel</span>
								</button>
								<div>
									<input type="checkbox" class="ml10" id="drnFile_docsel_unread_yn">
									<label for="">Unread Doc</label>
								</div>
								
							</div>
						</div>
						<hr>
						<div style="min-height: 70vh;display: flex;flex-flow: column;">
							<div class="tbl_wrap icon_tbl" id="drnSystemFileGrid"></div>
							<div class="documentControlTblWrap" style="flex: 1 auto;"></div>
						</div>
						
					</section>
				</div>
			</div>
		</section>
		
		
		<div style="display:none">
		<form id="dciUploadForm_PathList" 
			name=dciUploadForm_PathList  
			enctype="multipart/form-data" 
			method="post" onsubmit="return false;">
				<input type="hidden" name="fileName" id="fileName_PathList"> 
				<input type="hidden" name="discipline" id="discipline">
				<input type="hidden" name="prj_id" id="prj_id_PathList"> 
				<input type="hidden" name="prj_nm" id="prj_nm_PathList">
				<input type="hidden" name="folder_id" id="folder_id_PathList">
				<input type="hidden" name="current_folder_path" id="current_folder_path_PathList">
				<input type="hidden" name="select_rev_version" id="select_rev_version_PathList">
				<input type="hidden" name="doc_type" id="doc_type_PathList">
				<input type="hidden" name="unread_yn" id="unread_yn_PathList">
				<input type="hidden" name="jsonString" id="jsonString">
				<input type="hidden" name="jsonRowdatas" id="jsonRowdatas_PathList">
		</form>
	</div>
		
		
	</div>
</div>


<!-- 멀티다운로드  다이얼로그 -->
<div class="dialog pop_multiDownload" title="Multi Download" style="max-width:1000px">
	<div class="pop_box">
		<h5></h5>
		<div class="tbl_wrap" id="multiDownTbl"></div>
		<div class="line">
			<input type="checkbox" id="downloadFileRename" onchange="fileRenameChk()"><label for="">File Rename (Variant : %DOCNO%, %REVNO%, %TITLE%)</label>
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


<!-- 파일업로드 -->
<div class="dialog pop_upload" title="Multi Upload" style="max-width:1000px">
	<div class="pop_box">
		<div class="pg_top">
			<div class="right">
				<button class="button btn_blue ml5 dc_multiUpload" onclick="dcUpload_addBtn()">Add</button>
				<button class="button btn_blue dc_multiUpload" onclick="dcUpload_delBtn()">Delete</button>
			</div>
		</div>
		<!-- 2 files (2.93MB) -->
		<h5 class="dc_multiUpload">0 files (0 byte)</h5>
		<div class="tbl_wrap" id="multiUploadTbl"></div>
		<form id="dc_multiUpload_form" method="post" enctype="multipart/form-data">
			<input type="file" multiple="multiple" name="uploadfile" class="dc_multiUpload" style="display: none;" onchange="dc_multiFileUp(this)" accept=".doc, .xlsx, image/*">
		</form>
		<div class="btn_wrap mt10 txt_center">
			<button class="button btn_purple dc_multiUpload" onclick="dcUpload_uploadBtn()">Upload</button>
			<button class="button btn_blue dc_multiUpload" onclick="dcUpload_closeBtn()">Close</button>
		</div>
	</div>
</div>





<!-- 체크시트 가져오기 모달창 -->
<div id="drnChkSheet_Pop"class="dialog" title="체크시트" style="max-width:1000px">
    <div class="pop_box">
        <div class="pg_top">
            <div class="left">
                <label for="">CHECK SHEET TITLE</label>
                <input id="drn_chk_search"type="text"> <button class="button btn_search" onclick="checkSheetSearch()">검색</button>
            </div>
            <div class="right">
                <button class="button btn_blue" onclick="setLine_apply2()">적용</button>
                <button class="button btn_blue" onclick="setLine_close2()">닫기</button>
            </div>
        </div>
        <div>
        	<div id="chk_modal_grid"></div>
        </div>
    </div>
</div>




<!-- alert창을 dialog로 바꿈 -->
<div class="dialog pop_alertDialog" title="" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;" id="alertText">
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="alertDialogOk()">확인</button>
            </div>
    </div>
</div>

<!-- 코멘트창 -->
<div class="dialog" id ="drn_comment" title="Type the comment" style="max-width:1000px;">
    <div class="pop_box">
<!--     	<div style="font-weight:bold;font-size:11px;margin:15px;" id="commentText"> -->
<!--         </div> -->
        <div>
        	<textarea rows="3" id="drn_comment_text"></textarea>
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" id="commentOk">확인</button>
                <button class="button btn_orange" id="commentCancel">취소</button>
            </div>
    </div>
</div>