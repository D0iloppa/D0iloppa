<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resource/js/drnUp.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/onceLoad.js"></script>

<div id="pdfDrnDiv" class="dialog pop_collaboW" title="" style="max-width: 1200px;">
	<input type="hidden" id="drnFlag" onchange="flagOn()">
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
					<button id="drn_PrintBtn" class="button btn_blue drn_btns">Print</button>
				</div>
			</div>
			<div class="tbl_wrap">
				<table class="write">
					<col style="width: 100px">
					<col style="">
					<tr>
						<th>DRN 제목</th>
						<td><input type="text" class="drn_inputs drn_read" id="DRN_title_write" style="width: 100%;"></td>
						<th>DRN NO</th>
						<td><input type="text" class="drn_inputs" id="DRN_no_write" style="width: 100%;" readonly></td>
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
						<th>DOC NO(REV NO)</th>
						<td colspan="3"><input type="text" class="drn_inputs" id="Doc_no_write"
							style="width: 100%;" readonly></td>
						<th>STATUS</th>
						<td><select id="Status_write" class="drn_inputs" style="width: 100%;"></select></td>
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
							<button class="button btn_blue drn_file_btns drn_readMode" style="width: 50%;" onclick="drn_systemBtn()">시스템</button>
							<button class="button btn_blue drn_file_btns drn_readMode" style="width: 50%;" onclick="drn_localBtn()">PC</button>
							<button class="button btn_orange drn_file_btns drn_readMode" style="width: 50%;" onclick="drn_fileDel()">삭제</button>
							<button class="button btn_blue drn_file_btns" style="width: 50%;" onclick="drnFileDownload()">Download</button>
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
						<td><input class="drn_inputs drn_read" type="text" style="width: 100%;"
							placeholder="CHECK SHEET | "></td>
						<th class="drn_readMode">ITEM갯수</th>
						<td>
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
								<button class="button btn_blue" id="drn_allSelectBtn">적용</button>
							</div>
						</td>
					</tr>
				</table>
			</div>

			<div class="tbl_wrap">
				<div class="drn_grid" id="drn_SheetGrid"></div>
					<table class="nonTop">
						<tbody>
							<tr>
								<td style="width:20%; border-left:0.5px solid #ddd;">Review Conclusion</td>
								<td id="drn_review_conclusion"></td>
								<td style="padding: 0;"><div class="drn_grid" id="drn_ReviewConGrid" style="width: 99%;"></div>
								</td>							
							</tr>
						</tbody>						
					</table>
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
                <h4>결재자 선택
                    <button class="button btn_blue btn_small" onclick="setLine_reviewBtn()">검토</button>
                    <button class="button btn_blue btn_small" onclick="setLine_approveBtn()">결재</button>
                    <button class="button btn_blue btn_small" onclick="setLine_delBtn1()">삭제</button>
                </h4>
                <div class="tbl_wrap">
                	<div id="approverGrid"></div>
                </div>
                <h4>배포선 선택
                    <button class="button btn_blue btn_small" onclick="setLine_distributeBtn()">배포자</button>
                    <button class="button btn_blue btn_small" onclick="setLine_delBtn2()">삭제</button>
                </h4>
                <div class="tbl_wrap">
                	<div id="distributionLineGrid"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="drn_fileDialog" class="dialog" title="Document Select" style="max-width: 1200px;">
	<div class="pop_box">
		<section ng-app="angularResizable2" id="drnFile_left">
			<div class="content">
				<div class="row" resizable r-flex="true">
					<section id="drnFile_pop_ed_left" resizable r-directions="['right']"
						r-flex="true" class="p_ed_left">
						<nav id="drnFile_docSelTree"></nav>
					</section>
					<section id="drnFile_pop_ed_right">
						<div class="pg_top">
							<div class="left">
								<label for="">Path</label>
								<input type="text" id="drnFile_docsel_current_folder_path" style="width:300px" readOnly>
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
								<button class="btn_icon btn_green">
									<i class="fas fa-share-square"></i> <span>Retrieve</span>
								</button>
								<button class="btn_icon btn_green">
									<i class="fas fa-download"></i> <span>Upload</span>
								</button>
								<button class="btn_icon btn_green">
									<i class="fas fa-upload"></i> <span>Download</span>
								</button>
								<button class="btn_icon btn_green">
									<i class="far fa-file-excel"></i> <span>excel</span>
								</button>
								<input type="checkbox" class="ml10"
									id="drnFile_docsel_unread_yn"><label for="">Unread Doc</label>
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