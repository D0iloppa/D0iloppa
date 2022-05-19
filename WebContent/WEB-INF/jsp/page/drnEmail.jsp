<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<html>
	<head>
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
	   	<script src="${pageContext.request.contextPath}/resource/js/jquery-3.4.1.js"></script>
    	<script src="${pageContext.request.contextPath}/resource/js/jquery-ui.js"></script>
    	<script src="${pageContext.request.contextPath}/resource/js/tabulator.js"></script>
	    <script src="${pageContext.request.contextPath}/resource/js/drnEmail.js"></script>
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
	</head>
	
	<body>
		
	<div class = "_drn_Meta">
		<div id="_drn_auth" style="display:none;">${drn_auth}</div>
		<div id="_server_domail" style="display:none;">${server_domail}</div>
		<div id="_prj_id" style="display:none;">${prj_id}</div>
		<div id="_drn_id" style="display:none;">${drn_id}</div>
		<div id="_user_id" style="display:none;">${user_id}</div>
		<div id="_dccUser" style="display:none;">${dccUser}</div>
		<div id="_drnInfo" style="display:none;">${drnInfo}</div>
		<div id="_drnDocs" style="display:none;">${drnDocs}</div>
		<div id="_doc_type" style="display:none;">${doc_type}</div>
		<div id="_discip" style="display:none;">${discip}</div>
		<div id="_status" style="display:none;">${status}</div>
		<div id="_checkSheetList" style="display:none;">${checkSheetList}</div>
		<div id="_checkSheetItems" style="display:none;">${checkSheetItems}</div>
		<div id="_approvalLine" style="display:none;">${approvalLine}</div>
		<div id="_ds_date" style="display:none;">${ds_date}</div>
		<div id="_ds_info" style="display:none;">${ds_info}</div>
		<div id="_rv_date" style="display:none;">${rv_date}</div>
		<div id="_rv_info" style="display:none;">${rv_info}</div>
		<div id="_ap_date" style="display:none;">${ap_date}</div>
		<div id="_ap_info" style="display:none;">${ap_info}</div>
	</div>
	
	<div id="pdfDrnDiv" class="email_drn">
		<div class="sec">
			<div class="pg_top">
				<div class="right">
					<button id="drn_Accept" class="button btn_blue drn_btns">승인</button>
					<button id="drn_Deny" class="button btn_blue drn_btns">반려</button>
					<button id="drn_PrintBtn" class="button btn_blue drn_btns" onclick="drnPrint()">Print</button>
				</div>
			</div>
			<div class="tbl_wrap">
				<table class="write">
					<col style="width: 120px">
					<col style="">
					<tr>
						<th>DRN 제목</th>
						<td><input type="text" class="drn_inputs" id="DRN_title_write" style="width: 100%;" readonly></td>
						<th>DRN NO</th>
						<td><input type="text" class="drn_inputs" id="DRN_no_write" style="width: 100%;" readonly></td>
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
						<th>PROJECT NO</th>
						<td><input type="text" class="drn_inputs" id="Project_no_write"
							style="width: 100%;" readonly></td>
						<th>PROJECT NAME</th>
						<td><input type="text" class="drn_inputs" id="Project_name_write"
							style="width: 100%;" readonly></td>
						<th>DATE</th>
						<td><input type="text" class="drn_inputs" id="Date_write" style="width: 100%;"
							readonly></td>
					</tr>
					<tr>
						<th>DOC NO (REV NO)</th>
						<td colspan="3"><input type="text" class="drn_inputs" id="Doc_no_write"
							style="width: 100%;" readonly></td>
						<th>STATUS</th>
						<td>
						<input type="text" id="Status_write" class="drn_inputs" style="width:100%;" readonly>
<!-- 							<select type="text" id="Status_write" class="drn_inputs" style="width:100%;" ></select> -->
						</td>
					</tr>
					<tr>
						<th>DOC TITLE</th>
						<td colspan="5"><input type="text" class="drn_inputs" id="Doc_title_write"
							style="width: 100%;" readonly></td>
					</tr>
					<tr>
						<th>첨부문서</th>
						<td colspan="4">
							<div class="drn_grid" id="drn_FileGrid"></div>
						</td>
						<td>
<!-- 							<button class="button btn_blue drn_file_btns" style="width: 50%;" onclick="drnFileDownload()">Download</button> -->
						</td>
					</tr>
					<tr>
						<th>로컬파일</th>
						<td colspan="4">
							<div id="localFileTd"></div>
							<form id="drnUploadForm" name="drnUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
								<input type="file" class="drn_inputs" name="drnLocalFile" style="display: none;" onchange="drn_LocalFile(this)" />
							</form>
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
						<th colspan="2">CHECK SHEET TITLE</th>
						<td style="width:63%;"><input id="check_sheet_title" class="drn_inputs" type="text" style="width: 100%;" readonly></td>
						<td colspan="2" style="border: none;">
							<div id="chk_all_div" style="margin-left: 3%;"> 체크결과 일괄적용 :
								<select id ="drn_allSelect">
									<option>선택</option>
									<option value="1">O</option>
									<option value="2">X</option>
									<option value="3">△</option>
									<option value="4">N/A</option>
								</select>
<!-- 								<button class="button btn_blue" id="drn_allSelectBtn">적용</button> -->
							</div>
						</td>
					</tr>
				</table>
			</div>

			<div class="tbl_wrap">
				<div class="drn_grid" id="drn_SheetGrid" style="margin:auto;width:100%;"></div>
					<table>
						<tbody style="width:100%">
<!-- 							<tr> -->
<!-- 								<td style="border-left:0.5px solid #ddd; border-right:0.5px solid #ddd" colspan="17"></td> -->
<!-- 							</tr> -->
							<tr>
<!-- 								<td colspan="3" style="border-left:0.5px solid #ddd;">Review Conclusion</td> -->
<!-- 								<td colspan="9" style="" id="drn_review_conclusion"></td> -->
<!-- 								<td colspan="4" style="padding: 0;"><div class="drn_grid" id="drn_ReviewConGrid" style="margin: auto; width: 98%;"></div></td> -->
<!-- 								<td style="width:5%;border-right:0.5px solid #ddd;"></td> -->
								<td style="width:19%; border-top:0.5px solid #ddd; border-left:0.5px solid #ddd; border-right:0.5px solid #ddd; border-bottom:0.5px solid #ddd; text-align: center;">Review Conclusion</td>
								<td style="width:52.3%;border-top:0.5px solid #ddd; border-bottom:0.5px solid #ddd" id="drn_review_conclusion"></td>
								<td style="padding: 0; border-bottom:0.5px solid #ddd">
									<div style="margin:auto;overflow:hidden;"class="drn_grid" id="drn_ReviewConGrid"></div>
								</td>					
							</tr>
						</tbody>						
					</table>
				<p>※ ○ : Acceptable, X : Not Acceptable, △ : Conditionally
					acceptable as commented , N/A : Not Applicable</p>
			</div>
		</div>
	</div>


	<!-- 코멘트창 -->
	<div class="dialog" id="drn_comment" title="comment"
		style="max-width: 1000px;">
		<div class="pop_box">
			<div style="font-weight: bold; font-size: 11px; margin: 15px;"
				id="commentText">Type the comment</div>
			<div>
				<textarea rows="3" id="drn_comment_text"></textarea>
			</div>
			<div class="right" style="margin: 15px; float: right;">
				<button class="button btn_blue" id="commentOk">확인</button>
				<button class="button btn_orange" id="commentCancel">취소</button>
			</div>
		</div>
	</div>

</body>
</html>