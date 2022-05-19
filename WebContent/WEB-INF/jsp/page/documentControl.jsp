<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="${pageContext.request.contextPath}/resource/js/documentControl2.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/documentAttribute.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/checkIn.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/dciFileUpload.js"></script>
<!-- pdf 저장 !-->
<script src="${pageContext.request.contextPath}/resource/js/html2canvas.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/jspdf.min.js"></script>
 
<script src="${pageContext.request.contextPath}/resource/js/printThis.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery.flexdatalist.min.css">
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flexdatalist.min.js"></script>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd hh:mm:ss" var="curentTime"/>
<input type="hidden" id="currentTime"  value="${curentTime}">
								<div class="">
									<div class="pg_top">
										<div class="left" style="width:100%;">
											<label for="">Path</label>
											<input type="text" class="ml5 dc_current_folder_path" id="dc_current_folder_path">
											<input type="hidden" id="dc_current_folder_id" onchange="folderChange()">
											<label for="" class="ml20">Filter</label>
											<input type="text" class="ml5 dc_filter" id="dc_filter">
										</div>
									</div>
									<div class="pg_top">
										<div class="left">
											<input type="checkbox" name="docListAll">
											<select name="" class="ml5" id="select_rev_version" onchange="getPrjDocumentIndexList()">
												<option value="current" selected>Current Version</option>
												<option value="all">All Version</option>
											</select>
											<button id="drnUpBtn" class="button btn_orange" onclick="drnUpBtn()">
												<i class="fas fa-share-square"></i> DRN 상신
											</button>
											<button class="btn_icon btn_green" onclick="getPrjDocumentIndexList()">
												<i class="fas fa-sync"></i> <span>Retrieve</span>
											</button>
											<button class="btn_icon btn_green" onclick="documentControlDownload()">
												<i class="fas fa-download"></i> <span>Download</span>
											</button>
											<button class="btn_icon btn_green" onclick="dcMultiUpload()">
												<i class="fas fa-upload"></i> <span>Upload</span>
											</button>
											<button class="btn_icon btn_green" onclick="documentExcel()">
												<i class="far fa-file-excel"></i> <span>excel</span>
											</button>
											<input type="checkbox" class="ml10" id="unread_yn" onchange="getPrjDocumentIndexList()">
											<label for="">Unread Doc</label>
										</div>
									</div>
									<hr>
									<span id="drnUseYN" style="color:red;"></span>
									<div id="DCRowCount"></div>
									<div style="min-height: 70vh;display: flex;flex-flow: column;">
										<div class="tbl_wrap icon_tbl" id="doc_ctrl_li" style="margin-bottom: 0px;">
	
										</div>
										<div class="documentControlTblWrap" style="flex: 1 auto;"></div>
									</div>
								</div>
                                


  <div class="dialog pop_userSch" title="User search" style="max-width: 800px">
    <div class="pop_box">
      <div class="tbl_wrap">
        <table class="write">
          <tr>
            <th style="width: 20%">Search Condition</th>
            <td>
              <div class="line">
             	<input type="radio" name="searchDesigner" value="user_id">
              	<label for="">User ID</label>
              	<input type="radio" name="searchDesigner" value="user_kor_nm" class="ml10" checked>
              	<label for="">Kor. Name</label> 
              </div>
              <div class="line">
                <input type="text" id="designerSearchKeyword"/>
                <button class="button btn_search ml5" onclick="searchDesigner()">Search</button>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div class="tbl_wrap" id="userList3"></div>
      <button class="button btn_blue" id="designerSelectConfirm" style="float:right;">확인</button>
    </div>
  </div>
<div class="dialog pop_indexReg" title="Document Attribute" style="max-width:1500px">
	<div class="pop_box">
	<form id="dciUploadForm_PathList" name=dciUploadForm_PathList  enctype="multipart/form-data" method="post" onsubmit="return false;">
		<div class="pg_top">
			<div class="left">
				<input name="dci_overwrite" type="checkbox">
				<label for="">OVERWRITE</label>
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
			</div>
			<div class="right">
				<button class="button btn_blue" onclick="dci_retriveBtn()">RETRIEVE</button>
				<button class="button btn_purple" onclick="dci_fromExcelBtn()">From Excel</button>
				<button class="button btn_purple" onclick="dci_toExcelBtn()">To Excel</button>
				<button class="button btn_blue" onclick="dci_saveBtn()">Save</button>
			</div>
		</div>
		<div class="pg_top">
			<div class="left">
				<b class="left_b">folder</b>
			</div>
			<div class="right">
				<label for="">Discipline</label>
				<input id="indexReg_discip" type="text" disabled class="ml5">
			</div>
		</div>
		<div class="tbl_wrap" id="doc_attr" style="min-width: 1350px;"></div>

		<input type="file" name="dciExcelfile" style="display: none;" onchange="dciFileUp(this)" />

		</form>
	</div>
</div>


<div class="dialog pop_checkin" title="Check In" style="max-width:900px">
<form id="checkInForm" name="checkInfileUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
	<div class="pop_box">
		<div class="tbl_wrap">
			<table class="write">
				<tr>
					<th style="width:16%;">Doc No</th>
					<td class="pop_checkin_tbl"></td>
					<th>Title</th>
					<td colspan="3">
						<input type="text" class="full pop_checkin_tbl">
					</td>
				</tr>
				<tr>
					<th>Rev. No</th>
					<td class="pop_checkin_tbl" id="pop_checkin_origin_rev_no"></td>
					<th class="required">
						New Rev No <i>required</i>
					</th>
					<td>
						<select name="revList" id="checkInRevList">
							<option value=""></option>
							<c:forEach var="i" items="${revList}" varStatus="vs">
								<option value="${i.set_code_id}">${i.set_code}</option>
							</c:forEach>							
						</select>
					</td>
					
						<th class="isIFC required">IFC Revised reason <i>required</i> </th>
						<td class="isIFC">
						
								<select name="ifcList" id="checkInIFCList" onchange="ifcChg()">
									<option value=""></option>
								<c:forEach var="i" items="${ifcList}" varStatus="vs">
									<option value="${i.set_code_id}">${i.set_code}</option>
								</c:forEach>			
							</select>
							
						</td>
					
				</tr>
<!-- 				<tr> -->
<!-- 					<th>Title</th> -->
<!-- 					<td colspan="5"> -->
<!-- 						<input type="text" class="full pop_checkin_tbl"> -->
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<th class="required"><input id="checkIn_fileChgChk" onchange="checkIn_check()" class="pop_checkin_tbl" type="checkbox"> Change File <i>required</i></th>
					<td colspan="5">
						<div>
							<span class="pop_checkin_tbl"></span>
							<input type="file" name="checkIn_File" class="pop_checkin_tbl full" onchange="chgFile()"style="display:none;">
							<input type="text" name="" style="display:none;">
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="btn_wrap txt_center">
			<button class="button btn_blue" onclick="btn_checkIn()">Check-In</button>
			<button class="button btn_purple" onclick="checkInCloseBtn()">Close</button>
		</div>
	</div>
	</form>
</div>


<div class="dialog pop_Open" title="Open" style="max-width:500px">
	<div class="pop_box">
		<h4>Test-U-Bf301!3e-dd 결재 기능 설계를 위한 참고도서 (1/2)</h4>
		내용
	</div>
</div>


<div class="dialog pop_upload" title="Multi Upload" style="max-width:1000px">
	<!-- <div class="progressDiv">
    	<div class="progress">
            <div class="bar progress-bar progress-bar-info" role="progressbar" style="width: 0%">
            </div>
        </div>
    </div> -->
                                <script>
                                    $(function() {
                                        $('.bar').css('width', '30%');
                                    });
                                </script>
	<div class="pop_box">
		<div class="pg_top">
			<div class="right">
				<button class="button btn_blue ml5 dc_multiUpload" onclick="dcUpload_addBtn()">Add</button>
				<button class="button btn_blue dc_multiUpload" onclick="dcUpload_delBtn()">Delete</button>
			</div>
		</div>
		<h5 class="dc_multiUpload">0 files (0 byte)</h5>
		<div class="tbl_wrap" id="multiUploadTbl_d"></div>
		<form id="dc_multiUpload_form" method="post" enctype="multipart/form-data">
			<input type="file" multiple="multiple" name="uploadfile" class="dc_multiUpload" style="display: none;" onchange="dc_multiFileUp(this)" accept=".doc, .xlsx, image/*">
		</form>
		<div class="btn_wrap mt10 txt_center">
			<button class="button btn_purple dc_multiUpload" onclick="dcUpload_uploadBtn()">Upload</button>
			<button class="button btn_blue dc_multiUpload" onclick="dcUpload_closeBtn()">Close</button>
		</div>
	</div>
</div>

<div class="dialog pop_accHis" title="Access History" style="width:1200px">
	<form id="accessExcelUploadForm" name="accessExcelUploadForm" enctype="multipart/form-data" method="post" onsubmit="return false;">
    <div class="pop_box">
    	<div class="pg_top">
    		<input type="hidden" name="fileName" id="fileName"> 
			<input type="hidden" name="jsonRowdatas" id="jsonRowdatas">
			<input type="hidden" name="prj_id" id="prj_id">
			<input type="hidden" name="doc_id" id="doc_id">
			<div class="left">
				<h5 id="docNoRevNo"></h5>
			</div>
	    	<div class="right">
	    		<button class="button btn_green" onclick="accessHisToExcel()">
					To Excel
				</button>
	    	</div>
    	</div>
        <div class="tbl_wrap" id="accHis_tbl">
            
        </div>

    </div>
    </form>
</div>

<div class="dialog pop_drnHis" title="DRN History" style="max-width:800px">
    <div class="pop_box">
        <div class="tbl_wrap" id="drnHis_tbl">
            
        </div>

    </div>
</div>

<div class="dialog pop_sendingHistory" title="Sending History" style="max-width:800px">
    <div class="pop_box">
        <div class="tbl_wrap" id="corrSendingHistoryTbl">
        </div>
    </div>
</div>

<div class="dialog pop_mailNoticeDC" title="E-mailing Notice" style="max-width:1100px;">
    <div class="pop_box">
        <div class="pg_top">
            <div class="left">
                <span id="email_trno_corrno"></span>
                <b class="blue" id="email_sent_times"></b>
            </div>
            <div class="right">
                <button class="button btn_blue" onclick="sendEmailConfrimDC()">Send</button>
                <button class="button btn_purple" onclick="sendEmailCloseDC()">Close</button>
            </div>
        </div>
        <hr>
        <div class="tbl_wrap" style="overflow-x:visible;">
            <table class="write">
                <colgroup>
                    <col style="width:25%">
                    <col style="width:*">
                </colgroup>
                <tr>
                    <th>Sender *</th>
                    <td><input type="text" id="dc_sender_list" value="${sessionScope.login.email_addr}" disabled style="width:90%;"><!-- <button class="button btn_blue ml5">추가</button> --></td>
                </tr>
                <tr>
                    <th>Receiver *</th>
                    <td>
						<select id="dc_receiver_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>ReferTo</th>
                    <td>
						<select id="dc_referto_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>BCC</th>
                    <td>
						<select id="dc_bcc_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>Subject *</th>
                    <td><input type="text" id="DC_email_subject" style="width:100%;"></td>
                </tr>
            </table>
            <input type="hidden" id="email_tr_id">
        </div>
        <div class="smartEdit" style="min-height:200px; border: solid 1px #ddd;">
            <textarea rows="500" id="summernoteMailDC" cols="500" style="max-height: 300px;"></textarea>
		</div>
    </div>
</div>
<div class="dialog pop_SendEmailDC" title="SEND E-MAIL CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Do you want to send email?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="sendEmailDC()">Confirm</button>
                <button class="button btn_purple" onclick="sendEmailCancelDC()">No</button>
            </div>
    </div>
</div>





<!-- 문서삭제 확인팝업 -->
<div class="dialog pop_documentDeleteConfirm" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;" id="deleteConfirmMessage">
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" id="documentDeleteButton" onclick="docDeleteConfirm()">Delete</button>
                <button class="button btn_purple" onclick="documentDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>


<script>
window.addEventListener('focus', function() {
	currentSort = documentListTable.getSorters();
	if(!checkInFlag) searchDocByFilter();
}, false);

var dc_filter = document.getElementById("dc_filter");
dc_filter.addEventListener("keyup", function(event) {
  if (event.keyCode === 13) {
    event.preventDefault();
    searchDocByFilter();
  }
});
</script>