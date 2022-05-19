<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="./popupHeader.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd hh:mm:ss" var="curentTime"/>
<input type="hidden" id="currentTime"  value="${curentTime}">
<style>
.tbl_wrap .write tr th {
    width: 16%;
}
</style>
<div class="DocPopup">
    <div class="pop_box">
        <section ng-app="angularResizable5" id="corrsDocNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton()">Refresh</button>
                            <button class="button btn_blue" onclick="corrNewButton()" id="corrNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_corr_current_folder_path">
                        </p>
                        <div class="mb5" style="display:none;">
                            <input type="checkbox" id="popup_corr_rev_version" onchange="getPopupPrjDocumentIndexList('Corr')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="corrDocNoTbl">
                        </div>
                        <div class="rg-rightBack"></div>
                    </section>
                    <section id="pop_ed_right">
                        <div class="pg_top">
                             <div class="left">
								<button class="button btn_orange" id="corr_email" onclick="corrEmailOpen()">
									eMailing
								</button>
								<button class="button btn_orange" onclick="getCorrAccessHisList()">
									Access History
								</button>
                                    <button class="button btn_orange" id="corr_save" onclick="corrDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" id="corr_delete" onclick="docDelete()">
                                        Delete
                                    </button>
							</div>
                        </div>
                        <div style="display:flex;justify-content: space-between;">
                        <h4>Correspondence Document <i id="corr_locker" class="popup_locker"></i><span id="INOUTEORL" style="font-weight:500;color:#000;font-size:15px;margin-left:10px;"></span></h4>
                        <div style="font-size:15px;"><input type="checkbox" name="replyManager" id="replyManager" checked>&nbsp;REPLY일정관리</div>
                        </div>
                        <div class="tbl_wrap" style="overflow-x:visible;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        In/Out <i>required</i>
                                    </th>
                                    <td>
                                    	<span id="span_outgoing"><input type="radio" name="inout" value="O" style="display:none;"><label for="">OUTGOING</label></span>
                                    	<span id="span_incoming"><input type="radio" name="inout" value="I" style="display:none;"><label for="">INCOMING</label></span>
                                    </td>
                                    <th class="required">
                                        Document Date  <i>required</i>
                                    </th>
                                    <td>
                                        <input type="text" class="date" id="corr_doc_date" disabled>
                                    </td>
                                </tr>
                                <tr>
                                	<th>Creation</th>
                                    <td><input type="text" class="full popupinput" id="corr_creation" disabled></td>
                                    <th>Modification</th>
                                    <td><input type="text" class="full popupinput" id="corr_modification" disabled></td>
                                </tr>
                                <tr style="border-bottom:solid 1px #e1e1e1;">
                                    <th class="required">
                                        Corr. No <i>required</i>
                                    </th>
                                    <td colspan="3" style="border-bottom:none;display:inline-flex;justify-content: space-between;">
                                    	<input type="hidden" id="origin_corr_no">
                                    	<input type="text" id="corr_corr_no_1">
                                    	<div style="display:flex;padding:0 10%;">
	                                    	<span style="padding-top: 7px;">SENDER</span>
	                                    	<div class="multi_sel_wrap ml5" id="corrSenderSelectWrap">
	                                        	<button class="multi_sel" id="corrSenderSelected">SELECT</button>
	                                        	<input type="hidden" id="corrSelectedSenderSetCodeId">
	                                        	<div class="multi_box">
	                                            	<table id="corrSenderMultiBox">
	                                            	</table>
	                                       		</div>
	                                    	</div>
	                                    	<span style="padding-top: 7px;">RECEVIER</span>
	                                    	<div class="multi_sel_wrap ml5" id="corrReceiverSelectWrap">
	                                        	<button class="multi_sel" id="corrReceiverSelected">SELECT</button>
	                                        	<input type="hidden" id="corrSelectedReceiverSetCodeId">
	                                        	<div class="multi_box">
	                                            	<table id="corrReceiverMultiBox">
	                                            	</table>
	                                       		</div>
	                                    	</div>
	                                    	<span style="padding-top: 7px;">TYPE</span>
	                                    	<div class="multi_sel_wrap ml5" id="corrTypeSelectWrap">
	                                        	<button class="multi_sel" id="corrTypeSelected">SELECT</button>
	                                        	<input type="hidden" id="corrSelectedTypeSetCodeId">
	                                        	<div class="multi_box">
	                                            	<table id="corrTypeMultiBox">
	                                            	</table>
	                                       		</div>
	                                    	</div>
                                    	</div>
                                    </td>
                                </tr>
                                <tr id="sysCorrNoTr">
                                    <th>
                                        SYSTEM CORR. NO
                                    </th>
                                    <td colspan="3">
                                    	<input type="text" id="corr_system_corr_no" disabled>
                                    </td>
                                </tr>
                                <tr>
                                	<th rowspan="2">REFFERENCE. DOC</th>
                                	<td colspan="3">
                                		<!-- <input type="text" id="corr_corr_no_2">
	                                	<button class="button btn_blue" onclick="RefDocSearch()">Search</button> -->
                                		<span class="multi_sel_wrap" id="corrNo2SelectWrap">
                                        	<button class="multi_sel" id="corrNo2Selected">SELECT</button>
                                        	<input type="hidden" id="refSelectedDocIdRevId">
                                        	<input type="hidden" id="corrNo2SelectedTitle">
                                        	<div class="multi_box">
                                            	<table id="corrNo2MultiBox">
                                            	</table>
                                       		</div>
                                    	</span>
                                    	<span id="plusminus" style="display:none;">
                                    	<button class="btn_icon btn_blue ml5" onclick="refPlus()"><i class="fa fa-plus"></i></button><button class="btn_icon btn_blue ml5" onclick="refMinus()"><i class="fa fa-minus"></i></button>  
                                		</span>
                                	</td>
                                </tr>
                                <tr>
                                	<td colspan="3">
	                                	<input type="text" class="full" id="corr_ref_doc_no" disabled>
	                                	<input type="hidden" id="corr_ref_doc_id">
                                	</td>
                                </tr>
                                <tr id="refedDoc" style="display:none;">
                                	<th>REFERENCED. DOC</th>
                                	<td colspan="3">
	                                	<input type="text" class="full" id="saved_corr_ref_doc_no" disabled>
	                                	<input type="hidden" id="saved_corr_ref_doc_id">
                                	</td>
                                </tr>
                                <tr>
                                	<th class="required">
                                        TITLE <i>required</i>
                                    </th>
                                	<td colspan="3">
	                                	<input type="text" class="full" id="corr_title">
                                	</td>
                                </tr>
                                <tr>
                                	<th>RESPONSIBILITY</th>
                                	<td>
                                        <div class="multi_sel_wrap" id="corrResponSelectWrap">
                                        	<button class="multi_sel" id="corrResponSelected">SELECT</button>
                                        	<input type="hidden" id="corrSelectedResponSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="corrResponMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                	</td>
                                	<th id="sendReceiveDate">SEND DATE</th>
                                	<td><input type="text" class="full date" id="corr_send_recv_date"></td>
                                </tr>
                                <tr id="REPLYPERSON">
                                	<th class="required">REPLY REQ. DATE <i>required</i></th>
                                	<td><input type="text" class="date" id="corr_rep_req_date"></td>
                                	<th class="required">PERSON IN CHARGE<i>required</i></th>
                                	<td>
										<select id="corr_person_in_charge" class="full"></select>
					                </td>
                                </tr>
                                <tr>
                                	<th>File Attach</th>
                                    <td colspan="3">
                                        <form id="updateRevisionCorrForm" style="display:flex;" name="updateRevisionCorrForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
                                        	<span id="rfile_download" class="property_attach" onclick="propertydownload()"></span>
					                        <span class="filebox">
					                        	<input type="text" id="corr_uploadFileDate" disabled style="width: 190px;">
					                     	   	<input type="file" name="updateRevision_File" id="corr_attach_filename" class="upload-hidden corrDocumentFileAttach">
						                        <label for="corr_attach_filename" class="button btn_blue" id="corr_popup_attach">Attach</label>
					                    	</span>
                                        <!-- <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="corr_attachfilename" disabled="disabled">
					                        <label for="corr_attach_filename" class="button btn_blue" id="corr_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="corr_attach_filename" class="upload-hidden corrDocumentFileAttach">
					                    </span>
					                    <input type="text" id="corr_uploadFileInfo" disabled><input type="text" class="ml5" id="corr_uploadFileDate" disabled>
										<span id="rfile_download" onclick="propertydownload()" style="cursor: pointer;color:blue;text-decoration:underline;"></span> -->
					                    </form>
                                    </td>
                                </tr>
                                <tr>
                                	<th>Remark</th>
                                	<td colspan="3"><textarea class="full" rows="30" id="corr_remark"></textarea></td>
                                </tr>
                                <tr id="corrItemTr">
                                	<th>ITEM INFOMATION</th>
                                	<td style="display:flex;">
                                		<div class="multi_sel_wrap" id="corrItemSelectWrap">
                                        	<button class="multi_sel" id="corrItemSelected">SELECT</button>
                                        	<input type="hidden" id="corrSelectedItemSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="corrItemMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>
                                    	<button class="button btn_blue" id="itemMng">관리</button>
                                	</td>
                                	<th>ITEM NOTE</th>
                                	<td>
                                		<input type="text" class="full" id="corr_item_note">
                                	</td>
                                </tr>
                            </table>
                            <div id="corrHistoryDiv">
                            <h4>History</h4>
                            <input type="hidden" id="corr_ref_grp">
	                        <p class="step" id="corrHistory">
	                            <!-- <span>2021-08-13 회신</span>
	                            <span>2021-08-13 수신</span>
	                            <span>2021-08-13 회신</span>
	                            <span>2021-08-13 수신</span> -->
	                        </p>
	                        </div>
                        </div>
                    </section>
                </div>
            </div>
        </section>
		<input type="hidden" id="numberingSettingYN">
    </div>
</div>
<div class="dialog pop_itemMng" title="ITEM MANAGEMENT" style="max-width:1000px;">
    <div class="pop_box">
	    <div class="pg_top">
            <div class="right">
                <button class="button btn_purple" onclick="ITEMRefresh()">Refresh</button>
                <button class="button btn_blue" onclick="ITEMApply()">Apply</button>
            </div>
        </div>
        <div class="tbl_wrap" id="item_list_tbl">
        </div>
        <div class="pg_top">
            <div class="right">
                <button class="button btn_blue" onclick="ITEMAdd()">Add</button>
                <button class="button btn_blue" onclick="ITEMDeletePopup()">Delete</button>
            </div>
        </div>
    </div>
</div>




<div class="dialog pop_corrMailNotice" title="E-mailing Notice" style="max-width:1100px;">
    <div class="pop_box">
        <div class="pg_top">
            <div class="left">
                <span id="corr_email_docno_corrno"></span>
                <b class="blue" id="corr_email_sent_times"></b>
            </div>
            <div class="right">
                <button class="button btn_blue" onclick="corrSendEmailConfrim()">Send</button>
                <button class="button btn_purple" onclick="corrSendEmailClose()">Close</button>
            </div>
        </div>
        <!-- <span style="color:red;">※ 메일 구분자는 , 입니다. 또는 메일주소 입력 후 엔터를 눌러주세요.</span> -->
        <hr>
        <div class="tbl_wrap" style="overflow-x: visible;">
            <table class="write">
                <colgroup>
                    <col style="width:25%">
                    <col style="width:*">
                </colgroup>
                <tr>
                    <th>Type</th>
                    <td> 
						<div class="multi_sel_wrap" id="corrEmailSelectWrap">
							<button class="multi_sel" id="corrEmailTypeSelected">SELECT</button>
							<input type="hidden" id="selectedCorrEmailFeature"> <input
								type="hidden" id="selectedCorrEmailTypeId">
							<div class="multi_box">
								<table id="corrEmailTypeMultiBox">
								</table>
							</div>
						</div>
                    </td>
                </tr>
                <tr>
                    <th>Sender *</th>
                    <td><input type="text" id="corr_sender_list" value="${sessionScope.login.email_addr}" style="width:90%;"><!-- <button class="button btn_blue ml5">추가</button> --></td>
                </tr>
                <tr>
                    <th>Receiver *</th>
                    <td>
						<select id="corr_hidden_receiver_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>ReferTo</th>
                    <td>
						<select id="corr_hidden_referto_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
               	</tr>
                <tr>
                    <th>BCC</th>
                    <td>
                    	<select id="corr_hidden_bcc_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
	            </tr>
                <tr>
                    <th>Subject *</th>
                    <td><input type="text" id="corr_email_subject" style="width:100%;"></td>
                </tr>
            </table>
            <input type="hidden" id="corr_email_doc_no">
        </div>
        <div class="smartEdit" style="min-height:200px; border: solid 1px #ddd;">
            <textarea rows="500" id="corrSummernoteMailOut" cols="500" style="max-height: 300px;"></textarea>
		</div>
    </div>
</div>

<div class="dialog pop_corrUserSearchM" title="E-mailing Notice" style="max-width:1100px;">
    <div class="pop_box">
        <div class="tbl_wrap">
            <table class="write">
                <tr>
                    <th>Search Condition</th>
                    <td>
                        <div class="line">
                            <input type="radio" name="corrSearchUserEmail" value="user_id">
                            <label for="">User ID</label>

                            <input type="radio" name="corrSearchUserEmail" value="user_kor_nm" class="ml10" checked>
                            <label for="">Kor. Name</label>
                        </div>
                        <div class="line">
                            <input type="text" id="corrUsersEmailSearchKeyword">
                            <button class="button btn_search ml5" onclick="corrUsersEmailSearch()">Search</button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="pg_top">
            <div class="left">
                <h5>User List</h5>
            </div>
            <div class="right">
                <button class="button btn_small btn_gray">추가</button>
            </div>
        </div>
        <div class="tbl_wrap" id="corrUserSearchEmailList">

        </div>

        <div class="pg_top">
            <div class="left">
                <h5>Email List</h5>
            </div>
            <div class="right">
                <button class="button btn_small btn_gray" onclick="corrUserSearchClose()">확인</button>
            </div>
        </div>
        <div class="gray_small_box">
            <ul class="user_name" id="corrSettingUserEmailList">
            </ul>
        </div>
    </div>
</div>
<div class="dialog pop_corrSendEmail" title="SEND E-MAIL CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Do you want to send email?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="corrSendEmail()">Confirm</button>
                <button class="button btn_purple" onclick="corrSendEmailCancel()">No</button>
            </div>
    </div>
</div>
<div class="dialog pop_drnHis" title="DRN History" style="max-width:1000px">
    <div class="pop_box">
        <div class="tbl_wrap" id="drnHis_tbl">
            
        </div>

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
<div class="dialog pop_ITEMDelete pop_doc_search" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="ITEMDelete()">Delete</button>
                <button class="button btn_purple" onclick="ITEMDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>
<div class="dialog pop_sendingHistory" title="Sending History" style="max-width:800px">
    <div class="pop_box">
        <div class="tbl_wrap" id="corrSendingHistoryTbl">
        </div>
    </div>
</div>
<input type="hidden" id="current_folder_id">
<script>
var process_type = 'Corr';
var documentListTable;
var thisTable;
var corrDocumentListTable;
var searchDesignerTable;
var DC = {};
var corr_new_or_update = 'new';
var corr_selected_doc_id = '';
var corr_selected_rev_id = '';
var trINOUT_discipline_display = '';
var corrSelectedAddType='';
var corrSelectedUserEmail='';
var corrSelectedEmail='';
var corrSelectedUserId = '';
var addOrUpdate;
var itemListTable;
var setOrderArray = new Array();
var origin_email_list_corr = [];


var current_folder_discipline_code = opener.document.getElementById('current_folder_discipline_code').value;
var current_folder_discipline_code_id = opener.document.getElementById('current_folder_discipline_code_id').value;
var current_folder_discipline_display = opener.document.getElementById('current_folder_discipline_display').value;
var current_folder_id = opener.document.getElementById('current_folder_id').value;
var DCCorTRA = opener.document.getElementById('DCCorTRA').value;
var session_user_id = opener.document.getElementById('session_user_id').value;
var session_user_eng_nm = opener.document.getElementById('session_user_eng_nm').value;
var session_user_kor_nm = opener.document.getElementById('session_user_kor_nm').value;
var selectPrjId = opener.document.getElementById('selectPrjId').value;
var proc_new_or_update = opener.document.getElementById('proc_new_or_update').value;
var current_folder_path = opener.document.getElementById('current_folder_path').value;
var selectedPrjNo = opener.document.getElementById('selectedPrjNo').value;
var doc_type = '';
var process = opener.document.getElementById('process').value;
if(current_folder_path.indexOf('LETTER') != -1){
	doc_type = 'LETTER';
}else{
	doc_type = 'E-MAIL';
}
$(function(){
	$('#current_folder_id').val(current_folder_id);
	
	auth = checkFolderAuth(selectPrjId,current_folder_id);
	if(auth.w==='Y'){
		$('#corr_email').css('display','inline-block');
		$('#corr_save').css('display','inline-block');
		$('#corrNewButton').css('display','inline-block');
	}else{
		$('#corr_email').css('display','none');
		$('#corr_save').css('display','none');
		$('#corrNewButton').css('display','none');
	}
	if(auth.d==='Y'){
		$('#corr_delete').css('display','inline-block');
	}else{
		$('#corr_delete').css('display','none');
	}
	
	$("#replyManager").change(function(){
        if($("#replyManager").is(":checked")){
            $('#REPLYPERSON').css('display','table-row');
        }else{
            $('#REPLYPERSON').css('display','none');
        }
    });

	$("input[name='inout']:radio").change(function () {
        if(this.value==='O'){
    		if(current_folder_path.indexOf('LETTER') != -1){
    			$('#INOUTEORL').html('LETTER OUTGOING');
    		}else{
    			$('#INOUTEORL').html('E-MAIL OUTGOING');
    		}
        }else{
    		if(current_folder_path.indexOf('LETTER') != -1){
    			$('#INOUTEORL').html('LETTER INCOMING');
    		}else{
    			$('#INOUTEORL').html('E-MAIL INCOMING');
    		}
        }           
	});

	dialogInit();
	//Correspondence 파일첨부
    var fileTarget = $('.filebox .corrDocumentFileAttach');

    fileTarget.on('change', function () {
        if (window.FileReader) {
            var fullName = $(this)[0].files[0].name;
            var size = $(this)[0].files[0].size;
            var extension = $(this)[0].files[0].name.split('.').pop().toLowerCase();
        } else {
            var fullName = $(this).val().split('/').pop().split('\\').pop();
        }
       
        var fileName = fullName.replace('.'+extension, '');
        DC.rfile_nm = fullName;
        DC.file_type = extension;
        DC.file_size = size;
        
        var today = new Date();
        $('#corr_uploadFileDate').val(formatBytes(size,2)+', '+today.toLocaleString());
    });
    

	
	toolbar = [
		// 스타일
		['style',['style']],
		// 굵기, 기울임꼴, 밑줄
		['style',['bold','italic','underline']],
		// 글자 크기 설정
		['fontsize', ['fontsize']],
		// 글꼴 설정
		['fontname',['fontname']],
		// 줄간격
	    ['height', ['height']],
	    // 글자색
	    ['color', ['forecolor','color']],
	    // 글머리 기호, 번호매기기, 문단정렬
	    ['para', ['ul', 'ol', 'paragraph']],
	    // 표만들기
	    ['table', ['table']],
	    // 그림첨부, 링크만들기
	    ['insert',['picture','link']],
	    // 코드보기, 확대해서보기
	    ['view', ['fullscreen', 'codeview']]
	];
	$("#corrSummernoteMailOut").summernote({
		height: 500,
		lang: "ko_KR",
		toolbar: toolbar,
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체']
	});
	$(".pop_corrMailNotice").dialog({
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
	$(".pop_corrSendEmail").dialog({
		autoOpen: false,
		maxWidth: 500,
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
	$(".pop_sendingHistory").dialog({
		autoOpen: false,
		maxWidth: 500,
		width: 500
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
	$(".pop_itemMng").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: 800
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

    $(".pop_ITEMDelete").dialog({
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
    $(document).on("click", "#itemMng", function() {
    	getITEMPrjCodeSettingsList();
        $(".pop_itemMng").dialog("open");
        return false;
    });

	getPrjMemeberListForPersonInCharge();
	
	if(process==='new'){
    	$('#popup_corr_current_folder_path').html(current_folder_path);
        getPopupPrjDocumentIndexList(doc_type);
        getCorrFromToMultiBox('sender');
        getCorrFromToMultiBox('receiver');
        getCorrTypeMultiBox();
        getCorrResponMultiBox();
        setCorrNull();
    	setCorrInit();
		$('#corr_doc_date').datepicker('setDate', new Date());
	}else if(process==='property'){
		/* $('#refedDoc').css('display','table-row'); */
		var doc_id = opener.document.getElementById('doc_id').value;
		var rev_id = opener.document.getElementById('rev_id').value;
		corr_selected_doc_id = doc_id;
		corr_selected_rev_id = rev_id;
		let trans_ref_doc_id = opener.document.getElementById('trans_ref_doc_id').value;
    	$('#popup_corr_current_folder_path').html(current_folder_path);
        getPopupPrjDocumentIndexList(doc_type);
        getCorrFromToMultiBox('sender');
        getCorrFromToMultiBox('receiver');
        getCorrTypeMultiBox();
        getCorrResponMultiBox();
    	setCorrInit();
        if(DCCorTRA==='DCC'){
        	getCorrDocumentInfo(doc_id,rev_id);
        }else if(DCCorTRA==='TRA'){
        	getCorrDocumentInfo(trans_ref_doc_id,rev_id);
        }
		$('#corr_doc_date').datepicker('setDate', new Date());
	}

	RefDocSearch();
	getAdminAndPrjDCCSysCorrNo();
	getCorrItemMultiBox();
});
function getPopupPrjDocumentIndexList(type,refresh){
	var select_rev_version='';
	if(type==='DCI'){
		select_rev_version = $('#popup_eng_rev_version').is(':checked')===true?'all':'current';
	}else if(type==='VDCI'){
		select_rev_version = $('#popup_vendor_rev_version').is(':checked')===true?'all':'current';
	}else if(type==='PCI'){
		select_rev_version = $('#popup_proc_rev_version').is(':checked')===true?'all':'current';
	}else if(type==='SDCI'){
		select_rev_version = $('#popup_site_rev_version').is(':checked')===true?'all':'current';
	}else if(type==='LETTER' || type==='E-MAIL' || type==='Corr'){
		select_rev_version = $('#popup_corr_rev_version').is(':checked')===true?'all':'current';
	}else if(type==='General'){
		select_rev_version = $('#popup_general_rev_version').is(':checked')===true?'all':'current';
	}
	var unread_yn='N';
	let troutproperty = 0;
	current_folder_id = $('#current_folder_id').val();
	$.ajax({
	    url: '../getPrjDocumentIndexList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	folder_id: troutproperty===1?0:current_folder_id,
	    	select_rev_version: select_rev_version,
	    	unread_yn: unread_yn
	    },
	    async:false,
	    success: function onData (data) {
	    	var doc_ctrl_li = [];
	    	for(i=0;i<data[0].length;i++){
	    		var Modified='';
	    		if(data[0][i].reg_date!=null){
	    			Modified = getDateFormat(data[0][i].reg_date);
	    		}
	    		var rev_code_id = data[0][i].rev_code_id;
	    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
	    		var reg_id = data[0][i].reg_id;
	    		var mod_id = data[0][i].mod_id;
	    		var ext = data[0][i].file_type;
	    		var imgIcon = '';
	    		if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
					imgIcon = "<img src='../resource/images/docicon/question-mark.png' class='i'>";
				}else if(ext == "png" || ext == "jpg" || ext == "jpeg" || ext == "gif"){
					imgIcon = "<img src='../resource/images/docicon/picture.png' class='i'>";
				}else if(ext == "pdf"){
					imgIcon = "<img src='../resource/images/docicon/pdf.ico' class='i'>";
				}else if(ext == "ppt" || ext == "pptx"){
					imgIcon = "<img src='../resource/images/docicon/ppt.ico' class='i'>";
				}else if(ext == "doc" || ext == "docx"){
					imgIcon = "<img src='../resource/images/docicon/doc.ico' class='i'>";
				}else if(ext == "xls" || ext == "xlsx" || ext == "csv"){
					imgIcon = "<img src='../resource/images/docicon/xls.ico' class='i'>";
				}else if(ext == "txt"){
					imgIcon = "<img src='../resource/images/docicon/txt.ico' class='i'>";
				}else if(ext == "hwp"){
					imgIcon = "<img src='../resource/images/docicon/hwp.ico' class='i'>";
				}else if(ext == "dwg"){
					imgIcon = "<img src='../resource/images/docicon/dwg.ico' class='i'>";
				}else if(ext == "zip"){
					imgIcon = "<img src='../resource/images/docicon/zip.ico' class='i'>";
				}else{
					imgIcon = "<img src='../resource/images/docicon/general.ico' class='i'>";
				}
				if(lock_yn==='Y' && mod_id===session_user_id){
					imgIcon = "<img src='../resource/images/docicon/checkout.ico' class='i'>";
				}else if(lock_yn==='Y' && mod_id!=session_user_id){
					imgIcon = "<img src='../resource/images/docicon/lock.ico' class='i'>";
				}
	    		doc_ctrl_li.push({
	    			imgIcon: imgIcon,
                    docNo: data[0][i].doc_no,
                    revNo: data[0][i].rev_no,
                    title: data[0][i].title,
                    bean: data[0][i]
	    		});
	    	}
	    	if(refresh){
	    		if(refresh==='refresh'){
	    			thisTable.setData(doc_ctrl_li);
	    			thisTable.selectRow(thisTable.getRowFromPosition(0));
	    		}else{
			    	setPopupPrjDocumentIndexList(doc_ctrl_li,type);
	    		}
	    	}else{
		    	setPopupPrjDocumentIndexList(doc_ctrl_li,type);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPopupPrjDocumentIndexList(doc_ctrl_li,type){
	var tableid;
	if(type==='DCI'){
		thisTable = popupEngDocumentListTable;
		tableid = '#docNoTbl';
	}else if(type==='VDCI'){
		thisTable = vendorDocumentListTable;
		tableid = '#vendorDocNoTbl';
	}else if(type==='PCI'){
		thisTable = procDocumentListTable;
		tableid = '#procDocNoTbl';
	}else if(type==='SDCI'){
		thisTable = siteDocumentListTable;
		tableid = '#siteDocNoTbl';
	}else if(type==='LETTER' || type==='E-MAIL'){
		thisTable = corrDocumentListTable;
		tableid = '#corrDocNoTbl';
	}else if(type==='General'){
		thisTable = generalDocumentListTable;
		tableid = '#generalDocNoTbl';
	}
	thisTable = new Tabulator(tableid, {
		data: doc_ctrl_li,
		layout: "fitColumns",
		height: 600,
        selectable:1,//true
		columns: [
	        {
	            title: "",
	            field: "imgIcon",
	            formatter : "html",
				width : 20,
				frozen:true
	        },
	        {
				title: "Doc. No",
				field: "docNo",
				width : 200,
				frozen:true,
				hozAlign: "left"
			},
	        {
				title: "Rev",
				field: "revNo",
				width : 50
			},
	        {
				title: "Title",
				field: "title",
				width : 200,
				hozAlign: "left"
			}
		],
	});

	thisTable.on("rowSelected", function(row){
		var doc_id = row.getData().bean.doc_id;
		var trans_ref_doc_id = row.getData().bean.trans_ref_doc_id;
		var rev_id = row.getData().bean.rev_id;

		if(DCCorTRA==='DCC'){
			corr_selected_doc_id = doc_id;
		}else if(DCCorTRA==='TRA'){
			corr_selected_doc_id = trans_ref_doc_id;
		}
		corr_selected_rev_id = rev_id;
    	getCorrDocumentInfo(corr_selected_doc_id,corr_selected_rev_id);
	});
}
function getCorrDocumentInfo(doc_id,rev_id){
	corr_new_or_update = 'update';
	process = 'property';
	$.ajax({
	    url: '../getCorrDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id
	    },
	    success: function onData (data) {
	    	$('#corr_ref_grp').val(data[0].ref_grp);
	    	if(data[0].lock_yn==='Y'){
	    		$('#corr_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#corr_save').attr('disabled',true);
	    	}else{
	    		$('#corr_locker').html('');
	    		$('#corr_save').attr('disabled',false);
	    	}
	    	if(data[0].in_out==='O'){
				$('#span_outgoing').css('display','inline-block');
				$('#span_incoming').css('display','none');
	    	}else{
				$('#span_outgoing').css('display','none');
				$('#span_incoming').css('display','inline-block');
	    	}
	        $('input[name="inout"][value="'+data[0].in_out+'"]').prop('checked',true);
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
	    		let attach_file_date = '';
	    		if(data[0].attach_file_date!==null){
	    			attach_file_date = ', '+getDateFormat(data[0].attach_file_date);
	    		}
		    	$('#corr_uploadFileDate').val(formatBytes(data[0].file_size,2)+attach_file_date);
		    	$('#rfile_download').html(data[0].rfile_nm);
		    	$('#rfile_download').addClass('property_download');
		    	$('#rfile_download').removeClass('property_attach');
	    	}else{
		    	$('#corr_uploadFileDate').val('');
		    	$('#rfile_download').html('');
		    	$('#rfile_download').addClass('property_attach');
		    	$('#rfile_download').removeClass('property_download');
	    	}
	    	$('#corr_doc_date').val(to_date_format(data[0].doc_date,'-'));
	    	$('#corr_creation').val(data[0].reg_nm+'('+data[0].reg_kor_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#corr_modification').val(data[0].mod_date===null?'':data[0].mod_nm+'('+data[0].mod_kor_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#corr_remark').val(data[0].remark);
	    	$('#corrSenderSelected').html(data[0].sender_code);
	    	$('#corrSelectedSenderSetCodeId').val(data[0].sender_code_id);
	    	$('#corrReceiverSelected').html(data[0].receiver_code);
	    	$('#corrSelectedReceiverSetCodeId').val(data[0].receiver_code_id);
	    	$('#corrTypeSelected').html(data[0].doc_type_code);
	    	$('#corrSelectedTypeSetCodeId').val(data[0].doc_type_code_id);
	    	$('#corr_corr_no_1').val(data[0].corr_no);
	    	$('#corr_system_corr_no').val(data[0].sys_corr_no);
	    	$('#corrNo2Selected').html('SELECT');
	    	if(data[0].ref_corr_doc_id!==null && data[0].ref_corr_doc_id!==''){
	    		$.ajax({
	    		    url: '../getRefDocNoList.do',
	    		    type: 'POST',
	    		    data:{
	    		    	prj_id:selectPrjId,
	    		    	ref_corr_doc_id: data[0].ref_corr_doc_id
	    		    },
	    		    success: function onData (data) {
	    		    	$('#saved_corr_ref_doc_no').val(data[0]);
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
	    	}else{
	    		$('#saved_corr_ref_doc_no').val('');
	    	}
	    	$('#corr_ref_doc_id').val('');
	    	$('#saved_corr_ref_doc_id').val(data[0].ref_corr_doc_id);
	    	$('#corr_title').val(data[0].title);
	    	$('#corrResponSelected').html(data[0].responsibility_code?data[0].responsibility_code:'SELECT');
	    	$('#corrSelectedResponSetCodeId').val(data[0].responsibility_code_id);
	    	$('#corr_remark').val(data[0].remark);
	    	$('#corr_send_recv_date').val(to_date_format(data[0].send_recv_date,'-'));
	    	if(data[0].person_in_charge_id===null || data[0].person_in_charge_id===undefined || data[0].person_in_charge_id===''){
	    		$("#replyManager").prop("checked",false);
	    		$('#REPLYPERSON').css('display','none');
	    	}else{
	    		$("#replyManager").prop("checked",true);
				let corr_person_in_charge = $('#corr_person_in_charge').eq(0).data('selectize');
				corr_person_in_charge.setValue(data[0].person_in_charge_id,false);
	    		$('#corr_rep_req_date').val(to_date_format(data[0].replyreqdate,'-'));
	    		$('#REPLYPERSON').css('display','table-row');
	    	}
	    	$('#corrSelectedItemSetCodeId').val(data[0].item_info_code_id);
	    	$('#corrItemSelected').html(data[0].item_info_code?data[0].item_info_code:'SELECT');
	    	$('#corr_item_note').val(data[0].item_note);

	    	getCorrHistory();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function docDelete(){
	if(thisTable.getSelectedRows().length===0){
		alert('삭제할 도서를 선택하세요.');
	}else{
	    $(".pop_documentDeleteConfirm").dialog('open');
		$('#deleteConfirmMessage').html("Would you like to delete documents? (If you delete document, it can't restore)");
		$('#documentDeleteButton').attr('onclick','docDeleteConfirm()');
	}
}
function docDeleteConfirm(){
	let doc_id = thisTable.getSelectedData()[0].bean.doc_id;
	let rev_id = thisTable.getSelectedData()[0].bean.rev_id;
	let doc_type = thisTable.getSelectedData()[0].bean.doc_type;
	let doc_no = $('#corr_doc_no').val();
	let doc_title = $('#corr_title').val();
	let folder_id = current_folder_id;
	 $.ajax({
			type: 'POST',
			url: '../getUseDocTROrDRN.do',
			data: {
				prj_id:selectPrjId,
				doc_id:doc_id,
				rev_id:rev_id,
				doc_type:doc_type
			},
			success: function(data) {
				if(data[0]==='삭제불가'){
				    $(".pop_documentDeleteConfirm").dialog('close');
					alert('Document sent to customers cannot be deleted.');
				}else if(data[0]==='삭제가능'){
					 $.ajax({
							type: 'POST',
							url: '../documentDelete.do',
							data: {
								prj_id:selectPrjId,
								doc_id:doc_id,
								rev_id:rev_id
							},
							success: function(data) {
								if(data[0]>0){
									opener.getPrjDocumentIndexList();
							        $(".pop_documentDeleteConfirm").dialog('close');
								    alert('Delete Completed');
								    popupRefreshButton(doc_type);
								    
								    $.ajax({
						    			url: '../insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
						    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
						    		    data:{				// 이동할때 챙겨야하는 데이터
						    		    	prj_id: selectPrjId,
						    		    	doc_id: doc_id,
						    		    	rev_id: rev_id,
						    		    	doc_no: doc_no,
						    		    	doc_title: doc_title,
						    		    	folder_id: folder_id,
						    		    	file_size: DC.file_size,
						    		    	method: "DELETE"
						    		    },
						    		    success: function onData (data) {
						    		    },
						    		    error: function onError (error) {
						    		        console.error(error);
						    		        alert("오류");
						    		    }
						    		});
								}else{
									alert('documentDelete failed');
								}
							},
						    error: function onError (error) {
						        console.error(error);
						    }
					 });
				}else{
					alert('This is the document being used by '+data[0]);
				    $(".pop_documentDeleteConfirm").dialog('close');
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function documentDeleteCancel(){
    $(".pop_documentDeleteConfirm").dialog('close');
}
function popupRefreshButton(){
	/* $('#refedDoc').css('display','table-row'); */
	var doc_id = thisTable.getRows()[0].getData().bean.doc_id;
	var trans_ref_doc_id = thisTable.getRows()[0].getData().bean.trans_ref_doc_id;
	var rev_id = thisTable.getRows()[0].getData().bean.rev_id;
	if(DCCorTRA==='DCC'){
		getCorrDocumentInfo(doc_id,rev_id);
	}else if(DCCorTRA==='TRA'){
		getCorrDocumentInfo(trans_ref_doc_id,rev_id);
	}
	getPopupPrjDocumentIndexList(doc_type,'refresh');
	RefDocSearch();
}
function corrNewButton(){
	corr_new_or_update = 'new';
	getPopupPrjDocumentIndexList(doc_type);
	setCorrNull();
	RefDocSearch();
}
function setCorrNull(){
	$('#rfile_download').html('');
	$('#corrHistory').html('');
	/* $('#refedDoc').css('display','none'); */
	process='new';
	$("#replyManager").prop("checked",true);
    $('#REPLYPERSON').css('display','table-row');
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#corr_doc_no').attr("disabled", false);
    $('#corr_attach_filename').val('');
	$('#corr_locker').html('');
	$('#corr_creation').val(session_user_eng_nm+'('+session_user_kor_nm+'): '+ $('#currentTime').val());
	$('#corr_modification').val(session_user_eng_nm+'('+session_user_kor_nm+'): '+ $('#currentTime').val());
	$('#corr_uploadFileDate').val('');
	$('input[name="inout"]').prop('checked',false);
	$('#corr_doc_date').datepicker('setDate', new Date());
	$('#corrSelectedSenderSetCodeId').val('');
	$('#corrSenderSelected').html('SELECT');
	$('#corrSelectedReceiverSetCodeId').val('');
	$('#corrReceiverSelected').html('SELECT');
	$('#corrSelectedTypeSetCodeId').val('');
	$('#corrTypeSelected').html('SELECT');
	$('#corr_corr_no_1').val('');
	$('#corr_system_corr_no').val('');
	$('#corr_ref_doc_no').val('');
	$('#saved_corr_ref_doc_no').val('');
	$('#corr_ref_doc_id').val('');
	$('#saved_corr_ref_doc_id').val('');
	$('#corr_title').val('');
	$('#corrSelectedResponSetCodeId').val('');
	$('#corrResponSelected').html('SELECT');
	if(current_folder_path.toLowerCase().indexOf('outgoing') != -1){
		$('#sendReceiveDate').html('SEND DATE');
		$('input[name="inout"][value="O"]').prop('checked',true);
		$('#span_outgoing').css('display','inline-block');
		$('#span_incoming').css('display','none');
	}else{
		$('#sendReceiveDate').html('RECEIVED DATE');
		$('input[name="inout"][value="I"]').prop('checked',true);
		$('#span_outgoing').css('display','none');
		$('#span_incoming').css('display','inline-block');
	}
	$('#corr_send_recv_date').datepicker('setDate', new Date());
	$('#corr_rep_req_date').val('');
	let corr_person_in_charge = $('#corr_person_in_charge').eq(0).data('selectize');
	corr_person_in_charge.setValue('',false);
	$('#corr_remark').val('');
	$('#corrSelectedItemSetCodeId').val('');
	$('#corrItemSelected').html('SELECT');
	$('#corr_item_note').val('');
}
function getCorrFromToMultiBox(senderOrReceiver){
	$.ajax({
	    url: '../getCorrFromToList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
            var html = '<tr><th>CODE</th><th>DESCRIPTION</th></tr>';
    		if(senderOrReceiver==='sender'){
                for(i=0;i<data[0].length;i++){
                	html += '<tr class="selectSender"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td></tr>';
                }
		    	$('#corrSenderMultiBox').html(html);
		    	$('.selectSender').click(function(){
		    		$('#corrSenderSelectWrap').removeClass('on');
		    		$('#corrSenderSelected').html($(this).children().html());
		    		$('#corrSelectedSenderSetCodeId').val($(this).children().attr('id'));

		    		//onChange
		    		getCorrSerial();
		    	});
	    	}else if(senderOrReceiver==='receiver'){
	            for(i=0;i<data[0].length;i++){
	            	html += '<tr class="selectReceiver"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td></tr>';
	            }
		    	$('#corrReceiverMultiBox').html(html);
		    	$('.selectReceiver').click(function(){
		    		$('#corrReceiverSelectWrap').removeClass('on');
		    		$('#corrReceiverSelected').html($(this).children().html());
		    		$('#corrSelectedReceiverSetCodeId').val($(this).children().attr('id'));

		    		//onChange
		    		getCorrSerial();
		    	});
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getCorrTypeMultiBox(){
	$.ajax({
	    url: '../getCorrTypeList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
            var html = '<tr><th>CODE</th><th>DESCRIPTION</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectType"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td></tr>';
            }
	    	$('#corrTypeMultiBox').html(html);
	    	$('.selectType').click(function(){
	    		$('#corrTypeSelectWrap').removeClass('on');
	    		$('#corrTypeSelected').html($(this).children().html());
	    		$('#corrSelectedTypeSetCodeId').val($(this).children().attr('id'));
	    		
	    		//onChange
	    		getCorrSerial();
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getCorrSerial(){
	let sender = $('#corrSenderSelected').html();
	let receiver = $('#corrReceiverSelected').html();
	let set_code = $('#corrTypeSelected').html();
	let sender_code_id = $('#corrSelectedSenderSetCodeId').val();
	let receiver_code_id = $('#corrSelectedReceiverSetCodeId').val();
	let doc_type_code_id = $('#corrSelectedTypeSetCodeId').val();
	let sum = 0;
	if(sender_code_id!==null && sender_code_id!==undefined && sender_code_id!==''){
		sum += 1;
	}
	if(receiver_code_id!==null && receiver_code_id!==undefined && receiver_code_id!==''){
		sum += 1;
	}
	if(doc_type_code_id!==null && doc_type_code_id!==undefined && doc_type_code_id!==''){
		sum += 1;
	}
	
	if(sum===3){
		$.ajax({
		    url: '../getCorrSerial.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	sender_code_id:sender_code_id,
		    	receiver_code_id:receiver_code_id,
		    	doc_type_code_id:doc_type_code_id
		    },
		    success: function onData (data) {
		    	if(data[0]==='넘버링 설정 필요'){
		    		$('#numberingSettingYN').val('넘버링 설정 필요');
		    		alert('Correspondence Numbering 체계가 설정되어 있지 않습니다.DCC에게 문의해주세요.');
		    	}else{
			    	let corr_no = sender + '-' + receiver + '-' + set_code + '-' +data[0];
			    	$('#corr_corr_no_1').val(corr_no);
			    	if(process==='new'){	//update일 때는 system_corr_no가 바뀌어서는 안됨
			    		$('#corr_system_corr_no').val(corr_no);
			    	}
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function getCorrResponMultiBox(){
	$.ajax({
	    url: '../getCorrResponsibilityList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
            var html = '<tr><th>CODE</th><th>DESCRIPTION</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectRespon"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td></tr>';
            }
	    	$('#corrResponMultiBox').html(html);
	    	$('.selectRespon').click(function(){
	    		$('#corrResponSelectWrap').removeClass('on');
	    		$('#corrResponSelected').html($(this).children().html());
	    		$('#corrSelectedResponSetCodeId').val($(this).children().attr('id'));
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setCorrInit(){
	var set_code_type='';
	if(current_folder_path.toLowerCase().indexOf('outgoing') != -1){
		$('#span_outgoing').css('display','inline-block');
		$('#span_incoming').css('display','none');
		$('input[name="inout"][value="O"]').prop('checked',true);
		//$('input[name="inout"][value="I"]').attr('disabled',true);
		set_code_type='CORRESPONDENCE_OUT';
		if(current_folder_path.indexOf('LETTER') != -1){
			$('#INOUTEORL').html('LETTER OUTGOING');
		}else{
			$('#INOUTEORL').html('E-MAIL OUTGOING');
		}
	}else{
		$('#span_outgoing').css('display','none');
		$('#span_incoming').css('display','inline-block');
		$('input[name="inout"][value="I"]').prop('checked',true);
		//$('input[name="inout"][value="O"]').attr('disabled',true);
		set_code_type='CORRESPONDENCE_IN';
		if(current_folder_path.indexOf('LETTER') != -1){
			$('#INOUTEORL').html('LETTER INCOMING');
		}else{
			$('#INOUTEORL').html('E-MAIL INCOMING');
		}
	}
	
	if(selectedPrjNo==='SCRUBER001'){
		$('#corrItemTr').css('display','table-row');
	}else{
		$('#corrItemTr').css('display','none');
	}
}
function CorrDocSearch(){
	let corr_no = $('#corr_corr_no_1').val();
	$.ajax({
			type: 'POST',
			url: '../getCorrNoSearch.do',
			data: {
				prj_id:selectPrjId,
				corr_no: corr_no
			},
			success: function(data) {
				if(data[0]!==null){
		            var html = '<tr><th>In/Out</th><th>Document No</th><th>Title</th><th>Document Date</th><th>Responsibility</th><th>Modified Time</th></tr>';
		            for(i=0;i<data[0].length;i++){
		            	html += '<tr class="selectCorrNo"><td>'+data[0][i].in_out+'</td><td>'+data[0][i].doc_no+'</td><td>'+data[0][i].title+'</td><td>'+data[0][i].doc_date+'</td><td>'+data[0][i].responsibility_code+'</td><td>'+data[0][i].mod_date+'</td></tr>';
		            }
			    	$('#corrNoMultiBox').html(html);
			    	$('.selectCorrNo').click(function(){
			    		$('#corrNoSelectWrap').removeClass('on');
			    		$('#corrNoSelected').html($(this).children().next().html());
			    	});
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
	});
}
function RefDocSearch(){
	let doc_type = '';
	if(current_folder_path.indexOf('LETTER') != -1){
		doc_type = 'LETTER';
	}else{
		doc_type = 'E-MAIL';
	}
	$.ajax({
		type: 'POST',
		url: '../getCorrRefDocList.do',
		data: {
			prj_id:selectPrjId,
			doc_type: doc_type
		},
		async:false,
		success: function(data) {
			if(data[0]!==null){
				var html = '<tr><th>In/Out</th><th>Document No</th><th>Title</th><th>Document Date</th><th>Responsibility</th><th>Modified Time</th></tr>';
	            for(i=0;i<data[0].length;i++){
					let refYN = data[0][i].refYN;
					let respon_code = data[0][i].responsibility_code===null?'':data[0][i].responsibility_code;
					if(refYN==='Y'){
						html += '<tr class="usedCorrNo" id="'+data[0][i].doc_id+'"><td>'+data[0][i].in_out+'</td><td>'+data[0][i].doc_no+'</td><td>'+data[0][i].title+'</td><td>'+getDateFormat_YYYYMMDD(data[0][i].doc_date)+'</td><td>'+respon_code+'</td><td>'+getDateFormat(data[0][i].mod_date)+'</td></tr>';
		            }else{
		            	if(corr_selected_doc_id===data[0][i].doc_id && corr_selected_rev_id===data[0][i].rev_id){
		            		html += '<tr class="usedCorrNo" id="'+data[0][i].doc_id+'"><td>'+data[0][i].in_out+'</td><td>'+data[0][i].doc_no+'</td><td>'+data[0][i].title+'</td><td>'+getDateFormat_YYYYMMDD(data[0][i].doc_date)+'</td><td>'+respon_code+'</td><td>'+getDateFormat(data[0][i].mod_date)+'</td></tr>';
				        }else{
	            			html += '<tr class="selectCorrNo2" id="'+data[0][i].doc_id+'"><td>'+data[0][i].in_out+'</td><td>'+data[0][i].doc_no+'</td><td>'+data[0][i].title+'</td><td>'+getDateFormat_YYYYMMDD(data[0][i].doc_date)+'</td><td>'+respon_code+'</td><td>'+getDateFormat(data[0][i].mod_date)+'</td></tr>';
	            		}
		            }
	            }
		    	$('#corrNo2MultiBox').html(html);
		    	$('.usedCorrNo').click(function(){
	        		alert('해당도서는 현재도서이거나 다른도서에서 참조하여 다시 참조할 수 없습니다.');
	        	});
		    	
		    	$('.selectCorrNo2').click(function(){
		    		$('#corrNo2SelectWrap').removeClass('on');
		    		$('#corrNo2Selected').html($(this).children().next().html());
		    		$('#corrNo2SelectedTitle').val($(this).children().next().next().html());
		    		$('#refSelectedDocIdRevId').val($(this).attr('id'));	//Correspondence는 리비전 필요 없어서 뺐음
		    		$('#plusminus').css('display','inline-block');
		    	});
			}
		},
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function refPlus(){
	let refDoc = $('#corrNo2Selected').html();
	let refDocId = $('#refSelectedDocIdRevId').val();
	let corr_ref_doc_no_array = $('#corr_ref_doc_no').val().split(',');
	let corr_ref_doc_id_array = $('#corr_ref_doc_id').val().split('@@');
	if(refDoc!=='SELECT'){
		if(corr_ref_doc_no_array[0]===''){
			$('#corr_ref_doc_no').val(refDoc);
			$('#corr_ref_doc_id').val(refDocId);
			$('#corr_title').val('[회신] '+$('#corrNo2SelectedTitle').val());
		}else{
			if(!corr_ref_doc_no_array.includes(refDoc)) corr_ref_doc_no_array.push(refDoc);
			$('#corr_ref_doc_no').val(corr_ref_doc_no_array.join(','));
			if(!corr_ref_doc_id_array.includes(refDocId)) corr_ref_doc_id_array.push(refDocId);
			$('#corr_ref_doc_id').val(corr_ref_doc_id_array.join('@@'));
		}
	}
}
function refMinus(){
	let refDoc = $('#corrNo2Selected').html();
	let refDocId = $('#refSelectedDocIdRevId').val();
	let corr_ref_doc_no_array = $('#corr_ref_doc_no').val().split(',');
	let corr_ref_doc_id_array = $('#corr_ref_doc_id').val().split('@@');
	if(corr_ref_doc_no_array.includes(refDoc)) corr_ref_doc_no_array = corr_ref_doc_no_array.filter((element) => element !== refDoc);
	if(corr_ref_doc_id_array.includes(refDocId)) corr_ref_doc_id_array = corr_ref_doc_id_array.filter((element) => element !== refDocId);
	$('#corr_ref_doc_no').val(corr_ref_doc_no_array.join(','));
	$('#corr_ref_doc_id').val(corr_ref_doc_id_array.join('@@'));
}
function getDateFormat_YYYYMMDD(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);

	return year+'-'+month+'-'+day;
}
function corrDocSave(){
	let doc_id = corr_new_or_update==='new'?'':corr_selected_doc_id;
	let rev_id = corr_new_or_update==='new'?'':corr_selected_rev_id;
	let folder_id = parseInt(current_folder_id);
	let title = $('#corr_title').val();
	let remark = $('#corr_remark').val();
	let in_out = $('input[name="inout"]:checked').val();
	DC.sfile_nm = doc_id + '_' + rev_id + '.' + DC.file_type;
	let doc_date = $('#corr_doc_date').val().replaceAll('-','');
	let sender_code_id = $('#corrSelectedSenderSetCodeId').val();
	let receiver_code_id = $('#corrSelectedReceiverSetCodeId').val();
	let doc_type_code_id = $('#corrSelectedTypeSetCodeId').val();
	let corrNoArray = $('#corr_corr_no_1').val().split('-');
	let doc_no = $('#corr_corr_no_1').val();
	let corr_no = $('#corr_corr_no_1').val();
	let sys_corr_no = $('#corr_system_corr_no').val();
	let ref_corr_doc_no = $('#corr_ref_doc_no').val();
	let ref_corr_doc_id = '';
	if($('#corr_ref_doc_id').val()!=='' && $('#saved_corr_ref_doc_id').val()!==''){
		ref_corr_doc_id = $('#saved_corr_ref_doc_id').val() + '@@' + $('#corr_ref_doc_id').val();
	}else if($('#corr_ref_doc_id').val()==='' && $('#saved_corr_ref_doc_id').val()!==''){
		ref_corr_doc_id = $('#saved_corr_ref_doc_id').val();
	}else if($('#corr_ref_doc_id').val()!=='' && $('#saved_corr_ref_doc_id').val()===''){
		ref_corr_doc_id = $('#corr_ref_doc_id').val();
	}
	let responsibility_code_id = $('#corrSelectedResponSetCodeId').val();
	let replyreqdate = $('#corr_rep_req_date').val().replaceAll('-','');
	let send_recv_date = $('#corr_send_recv_date').val().replaceAll('-','');
	let person_in_charge_id = $('#corr_person_in_charge').val();
	let item_info_code_id = $('#corrSelectedItemSetCodeId').val();
	let item_note = $('#corr_item_note').val();
	
	let doc_type = '';
	if(current_folder_path.indexOf('LETTER') != -1){
		doc_type = 'LETTER';
	}else{
		doc_type = 'E-MAIL';
	}
	var params = {
			prj_id:selectPrjId,
			popup_new_or_update:corr_new_or_update,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:doc_type,
			folder_id:folder_id,
			title:title,
			remark:remark,
			in_out:in_out,
			doc_date:doc_date,
			sender_code_id:sender_code_id,
			receiver_code_id:receiver_code_id,
			doc_type_code_id:doc_type_code_id,
			doc_no:doc_no,
			corr_no:corr_no,
			sys_corr_no:sys_corr_no,
			ref_corr_doc_id:ref_corr_doc_id,
			responsibility_code_id:responsibility_code_id,
			replyreqdate:replyreqdate,
			send_recv_date:send_recv_date,
			person_in_charge_id:person_in_charge_id,
			item_info_code_id:item_info_code_id,
			item_note:item_note
	};

	let form = $('#updateRevisionCorrForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',corr_new_or_update);
    formData.set('doc_id',doc_id);
    formData.set('rev_id',rev_id);
    formData.set('doc_type',doc_type);
    formData.set('folder_id',folder_id);
	if($('#corr_attach_filename').val()!==''){
	    formData.set('sfile_nm',DC.sfile_nm);
	    formData.set('rfile_nm',DC.rfile_nm);
	    formData.set('file_type',DC.file_type);
	    formData.set('file_size',DC.file_size);
	}
    formData.set('title',title);
    formData.set('remark',remark);
    formData.set('in_out',in_out);
    formData.set('doc_date',doc_date);
    formData.set('sender_code_id',sender_code_id);
    formData.set('receiver_code_id',receiver_code_id);
    formData.set('doc_type_code_id',doc_type_code_id);
    formData.set('doc_no',doc_no);
    formData.set('corr_no',corr_no);
    formData.set('sys_corr_no',sys_corr_no);
    formData.set('ref_corr_doc_id',ref_corr_doc_id);
    formData.set('responsibility_code_id',responsibility_code_id);
    formData.set('replyreqdate',replyreqdate);
    formData.set('send_recv_date',send_recv_date);
    formData.set('person_in_charge_id',person_in_charge_id);
    formData.set('item_info_code_id',item_info_code_id);
    formData.set('item_note',item_note);
    let file_value =$('#corr_attach_filename').val();
    formData.set('file_value',file_value);

    let pass = true;
	if($("#replyManager").is(":checked")){
		if(replyreqdate==='' || replyreqdate===null || replyreqdate===undefined){
			alert('REPLY REQ. DATE를 설정해주세요.');
			pass = false;
		}else if(person_in_charge_id==='' || person_in_charge_id===null || person_in_charge_id===undefined){
			alert('PERSON IN CHAREGE를 설정해주세요.');
			pass = false;
		}
	}
	if(pass===true){
		if(in_out==='' || in_out===null || in_out===undefined){
			alert('IN/OUT을 설정해주세요.');
		}else if(doc_date==='' || doc_date===null || doc_date===undefined){
			alert('DOCUMENT DATE를 입력해주세요.');
		}else if(sender_code_id==='' || sender_code_id===null || sender_code_id===undefined){
			alert('SENDER를 설정해주세요.');
		}else if(receiver_code_id==='' || receiver_code_id===null || receiver_code_id===undefined){
			alert('RECEIVER를 설정해주세요.');
		}else if(doc_type_code_id==='' || doc_type_code_id===null || doc_type_code_id===undefined){
			alert('TYPE을 설정해주세요.');
		}else if(title==='' || title===null || title===undefined){
			alert('TITLE을 설정해주세요.');
		}else if(corr_no==='' || corr_no===null || corr_no===undefined){
    		alert('CORR NO를 설정해주세요.');
		}else if($('#numberingSettingYN').val()==='넘버링 설정 필요'){
    		alert('Correspondence Numbering 체계가 설정되어 있지 않습니다.DCC에게 문의해주세요.');
		}else{
			if($('#corr_attach_filename').val()!==''){
				 $.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: '../corrDocSaveAttach.do',        
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,     
				        beforeSend:function(){
							$('body').prepend(progressbar);
				        },
						complete:function(){
							$('body .progressDiv').remove();
						}, 
						xhr: function () {
				            //XMLHttpRequest 재정의 가능
				            var xhr = $.ajaxSettings.xhr();
				            xhr.upload.onprogress = function (e) {
				              //progress 이벤트 리스너 추가
				              var percent = (e.loaded * 100) / e.total;
				              $('body .progressDiv .bar').css('width', percent+'%');
				            };
				            return xhr;
				        },
						success: function(data) {
							if(data[0]==='확장자 오류'){
								alert(data[1]);
							}else if(data[0]==='DOC NO 중복'){
								alert("CORR NO가 중복되어 등록할 수 없습니다.");
							}else{
								if(data[0]>0){
									getPopupPrjDocumentIndexList(doc_type);
									getCorrDocumentInfo(data[1],data[2]);
									corr_selected_doc_id = data[1];
									corr_selected_rev_id = data[2];
									alert('Save Completed');
									
									$.ajax({
					    			url: '../insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: data[1],
					    		    	rev_id: data[2],
					    		    	doc_no: doc_no,
					    		    	doc_title: title,
					    		    	folder_id: folder_id,
					    		    	file_size: DC.file_size,
					    		    	method: "SAVE"
					    		    },
					    		    success: function onData (data) {
					    		    },
					    		    error: function onError (error) {
					    		        console.error(error);
					    		        alert("오류");
					    		    }
					    		});
								}else{
									alert('save failed!');
								}
							}
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
			}else{
				 $.ajax({
						type: 'POST',
						url: '../corrDocSave.do',        
				        data:params,
				        beforeSend:function(){
							$('body').prepend(progressbar);
				        },
						complete:function(){
							$('body .progressDiv').remove();
						}, 
						xhr: function () {
				            //XMLHttpRequest 재정의 가능
				            var xhr = $.ajaxSettings.xhr();
				            xhr.upload.onprogress = function (e) {
				              //progress 이벤트 리스너 추가
				              var percent = (e.loaded * 100) / e.total;
				              $('body .progressDiv .bar').css('width', percent+'%');
				            };
				            return xhr;
				        },
						success: function(data) {
							if(data[0]==='DOC NO 중복'){
								alert("CORR NO가 중복되어 등록할 수 없습니다.");
							}else{
								if(data[0]>0){
									getPopupPrjDocumentIndexList(doc_type);
									getCorrDocumentInfo(data[1],data[2]);
									corr_selected_doc_id = data[1];
									corr_selected_rev_id = data[2];
									alert('Save Completed');
									$.ajax({
								    			url: '../insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
								    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
								    		    data:{				// 이동할때 챙겨야하는 데이터
								    		    	prj_id: selectPrjId,
								    		    	doc_id: data[1],
								    		    	rev_id: data[2],
								    		    	doc_no: doc_no,
								    		    	doc_title: title,
								    		    	folder_id: folder_id,
								    		    	file_size: DC.file_size,
								    		    	method: "SAVE"
								    		    },
								    		    success: function onData (data) {
								    		    },
								    		    error: function onError (error) {
								    		        console.error(error);
								    		        alert("오류");
								    		    }
								    		});
								}else{
									alert('save failed!');
								}
							}
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
			}
		}
	}
}
function getCorrEmailTypeMultiBox(){
	$('#corrEmailTypeSelected').html('SELECT');
	$('#selectedCorrEmailTypeId').val('');
	email_feature = 'CORRESPONDENCE';
	$('#selectedCorrEmailFeature').val(email_feature);
	let receiver_email = $('#corr_hidden_receiver_list').eq(0).data('selectize');
	let referto_email = $('#corr_hidden_referto_list').eq(0).data('selectize');
	let bcc_email = $('#corr_hidden_bcc_list').eq(0).data('selectize');
	receiver_email.setValue(null,false);
	referto_email.setValue(null,false);
	bcc_email.setValue(null,false);
	$('#corrSummernoteMailOut').summernote("code", '');
	$('#corr_email_subject').val('');
	$.ajax({
	    url: '../getPrjEmailType.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	email_feature:email_feature
	    },
	    success: function onData (data) {
            let html = '<tr><th>FEATURE</th><th>MAIL TEMPLATE</th><th>DESCRIPTION</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectCorrMailType"><td id="'+data[0][i].email_type_id+'">'+data[0][i].email_feature+'</td><td>'+data[0][i].email_type+'</td><td>'+data[0][i].email_desc+'</td></tr>'
            }
	    	$('#corrEmailTypeMultiBox').html(html);
	   
	    	
	    	$('#corrSummernoteMailOut').summernote("code", '');
	    	
	    	
	    	$('.selectCorrMailType').click(function(){
	    		$('#corrEmailSelectWrap').removeClass('on');
	    		$('#corrEmailTypeSelected').html($(this).children().next().html());
	    		$('#selectedCorrEmailFeature').val($(this).children().html());
	    		$('#selectedCorrEmailTypeId').val($(this).children().attr('id'));
		    	
			    let email_type_id = $('#selectedCorrEmailTypeId').val();
			    DC.email_type_id = email_type_id;
				$.ajax({
				    url: '../getPrjEmailTypeContents.do',
				    type: 'POST',
				    data:{
				    	prj_id: selectPrjId,
				    	email_type_id:email_type_id
				    },
				    success: function onData (data) {
				    	DC.file_link_yn = data[0].file_link_yn;
				    	DC.file_rename_yn = data[0].file_rename_yn;
				    	DC.file_nm_prefix = data[0].file_nm_prefix;
				    	let contents = corrPrefixReplace(data[0].contents);
				    	let subject = corrPrefixReplace(data[0].subject_prefix);
				    	$('#corrSummernoteMailOut').summernote("code", contents);
				    	$('#corr_email_subject').val(subject);
				    	$('#corr_sender_list').val(data[0].email_sender_addr.split('!!')[0]);
			    		let receiver_email_list = data[0].email_receiver_addr.split(';');
			    		for(let l=0;l<receiver_email_list.length;l++){
				    		if(!origin_email_list_corr.includes(receiver_email_list[l])){
				    			receiver_email.addOption({value:receiver_email_list[l], text:receiver_email_list[l]});
				    		}
			    		}
			    		let referto_email_list = data[0].email_referto_addr.split(';');
			    		for(let l=0;l<referto_email_list.length;l++){
				    		if(!origin_email_list_corr.includes(referto_email_list[l])){
				    			referto_email.addOption({value:referto_email_list[l], text:referto_email_list[l]});
				    		}
			    		}
			    		let bcc_email_list = data[0].email_bcc_addr.split(';');
			    		for(let l=0;l<bcc_email_list.length;l++){
				    		if(!origin_email_list_corr.includes(bcc_email_list[l])){
				    			referto_email.addOption({value:bcc_email_list[l], text:bcc_email_list[l]});
				    		}
			    		}
				    	receiver_email.setValue(data[0].email_receiver_addr.split(';')[0]==''&&data[0].email_receiver_addr.split(';').length==1?null:data[0].email_receiver_addr.split(';'),false);
				    	referto_email.setValue(data[0].email_referto_addr.split(';')[0]==''&&data[0].email_referto_addr.split(';').length==1?null:data[0].email_referto_addr.split(';'),false);
				    	bcc_email.setValue(data[0].email_bcc_addr.split(';')[0]==''&&data[0].email_bcc_addr.split(';').length==1?null:data[0].email_bcc_addr.split(';'),false);
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function corrPrefixReplace(changedata){
	let doc_table = '<p></p><br>';
	let doc_id = thisTable.getSelectedData()[0].bean.doc_id;
	let rev_id = thisTable.getSelectedData()[0].bean.rev_id;
	$.ajax({
	    url: '../getCorrDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	doc_id:doc_id,
	    	rev_id:rev_id
	    },
	    async:false,
	    success: function onData (data) {
	    	$('#corr_email_doc_no').val(data[0].sys_corr_no);
	    	$('#corr_email_docno_corrno').html('CORR NO : '+data[0].doc_no);
	    	if(data[1]>0)
	    		$('#corr_email_sent_times').html('<a class="link_class" onclick="corrSendingHistory(`'+data[0].sys_corr_no+'`)">This email already has been sent.('+data[1]+' times)</a>');
	    	else
	    		$('#corr_email_sent_times').html('');
	    	
	    	if(data[0].rfile_nm===null || data[0].rfile_nm===undefined || data[0].rfile_nm===''){
	    		doc_table = '첨부문서가 없습니다.';
		    	docid_revid_docno += doc_id + '&&' + rev_id + '&&' + data[0].doc_no + '@@';
		    	docid_revid_docno = docid_revid_docno.slice(0,-2);
		    	DC.docid_revid_docno = docid_revid_docno;
				DC.docid = doc_id;
				DC.revid = rev_id;
	    	}else{
		    	let pk = '';
		    	let docid_revid_docno = '';
	    		pk += doc_id + '&&' + rev_id + '@@';
		    	pk = pk.slice(0,-2);
		    	docid_revid_docno += doc_id + '&&' + rev_id + '&&' + data[0].doc_no + '@@';
		    	docid_revid_docno = docid_revid_docno.slice(0,-2);
		    	DC.docid_revid_docno = docid_revid_docno;
				DC.docid = doc_id;
				DC.revid = rev_id;
				
		    	downloadFiles = docid_revid_docno.replaceAll('&&','%%');
		    	let renameYN='N';
		    	if(DC.file_rename_yn==='Y') renameYN='Y';
		    	let fileRenameSet = DC.file_nm_prefix;
		    	
		    	if(DC.file_link_yn==='Y'){
			    	doc_table = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    		let downloadFile = doc_id + '_' + rev_id + '.' + data[0].file_type;
		    		doc_table += '<tr style="text-align:center;"><td>1</td><td><a href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(data[0].rfile_nm)+'&email_type_id='+encodeURIComponent(DC.email_type_id)+'&email_id=@@email_id_date@@">'+data[0].doc_no+'</a></td><td>'+data[0].title+'</td><td>'+data[0].file_type+'</td><td>'+formatBytes(data[0].file_size,2)+'</td></tr>'
		    		doc_table += '</tbody></table><p></p><br>';
			   }else{
			    	doc_table = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
			    	doc_table += '<tr style="text-align:center;"><td>1</td><td>'+data[0].doc_no+'</td><td>'+data[0].title+'</td><td>'+data[0].file_type+'</td><td>'+formatBytes(data[0].file_size,2)+'</td></tr>'
			    	doc_table += '</tbody></table><p></p><br>';
		    	}
	    	}
	    	changedata = changedata.replaceAll('%OBJECT_NAME%',data[0].doc_no)
	    				.replaceAll('%TITLE%',data[0].title)
	    				.replaceAll('%DOC_TABLE%',doc_table)
	    				.replaceAll('%DOC_NO%',data[0].doc_no)
	    				.replaceAll('%DOC_TITLE%',data[0].title)
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	return changedata;
}
function getCorrHistory(){
	let ref_grp = $('#corr_ref_grp').val();
	$.ajax({
		type: 'POST',
		url: '../getCorrHistory.do',
		data:{
			prj_id:selectPrjId,
			ref_grp:ref_grp
		},
		success: function(data) {
			if(data[0]!==null && data[0]!==undefined){
				let corrHistoryHtml = '';
				for(let i=0;i<data[0].length;i++){
					if(data[0][i].in_out==='O'){
						if(data[0][i].email_send_date!==null && data[0][i].email_send_date!==''){
							corrHistoryHtml += '<span class="corrHistorySpan" onclick="getCorrDocumentInfo(\''+data[0][i].doc_id+'\',\''+data[0][i].rev_id+'\')">'+getDateFormat(data[0][i].email_send_date)+' 발송</span>';
						}else{
							corrHistoryHtml += '<span class="corrHistorySpan" onclick="getCorrDocumentInfo(\''+data[0][i].doc_id+'\',\''+data[0][i].rev_id+'\')">'+to_date_format(data[0][i].reg_date, '-')+'</span>';
						}
					}else{//in_out==='I'
						corrHistoryHtml += '<span class="corrHistorySpan" onclick="getCorrDocumentInfo(\''+data[0][i].doc_id+'\',\''+data[0][i].rev_id+'\')">'+getDateFormat(data[0][i].reg_date)+' 수신</span>';
					}
				}
				$('#corrHistory').html(corrHistoryHtml);
			}else{
				$('#corrHistory').html('');
			}
		},
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function corrSendingHistory(sys_corr_no){
	$.ajax({
		type: 'POST',
		url: '../getEmailSendingHistory.do',
		data:{
			prj_id:selectPrjId,
			user_data00:sys_corr_no
		},
		success: function(data) {
			var corrSendingHistoryTbl = [];
			for(let i=0;i<data[0].length;i++){
				corrSendingHistoryTbl.push({
					type: data[0][i].email_type,
					sender: data[0][i].reg_id,
					date: getDateFormat(data[0][i].reg_date)
				});
			}
			setCorrSendingHistoryData(corrSendingHistoryTbl);
		},
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	$('.pop_sendingHistory').dialog('open');
}
function setCorrSendingHistoryData(corrSendingHistoryTbl){
    var table = new Tabulator("#corrSendingHistoryTbl", {
        data: corrSendingHistoryTbl,
        layout: "fitColumns",
        height: 300,
        columns: [{
                title: "Type",
                field: "type"
            },
            {
                title: "Sender",
                field: "sender"
            },
            {
                title: "Date",
                field: "date"
            }
        ],
    });
}
function corrEmailOpen(){
	if(thisTable.getSelectedData().length===0){
		alert('왼쪽 DOCUMENT LIST에서 도서를 선택해주세요.');
		return false;
	}else{
		getCorrEmailList();
		getCorrEmailTypeMultiBox();
		$.ajax({
		    url: '../getCorrDocumentInfo.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId,
		    	doc_id:thisTable.getSelectedData()[0].bean.doc_id,
		    	rev_id:thisTable.getSelectedData()[0].bean.rev_id
		    },
		    async:false,
		    success: function onData (data) {
		    	$('#corr_email_doc_no').val(data[0].sys_corr_no);
		    	$('#corr_email_docno_corrno').html('CORR NO : '+data[0].doc_no);
		    	if(data[1]>0)
		    		$('#corr_email_sent_times').html('<a class="link_class" onclick="corrSendingHistory(`'+data[0].sys_corr_no+'`)">This email already has been sent.('+data[1]+' times)</a>');
		    	else
		    		$('#corr_email_sent_times').html('');
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		$(".pop_corrMailNotice").dialog("open");
		return false;
	}
}
function corrSendEmailClose(){
	$('.pop_corrMailNotice').dialog('close');
}
function corrSendEmailConfrim(){
	$(".pop_corrSendEmail").dialog('open');
}
function corrSendEmailCancel(){
	$(".pop_corrSendEmail").dialog('close');
}
function corrSendEmail(){
	let doc_no = $('#corr_email_doc_no').val();
	let email_type_id = $('#selectedCorrEmailTypeId').val();
	let subject = $('#corr_email_subject').val();
	let contents = $('#corrSummernoteMailOut').summernote("code");
	let user_data00 = $('#corr_email_doc_no').val();
	let email_sender_addr = $('#corr_sender_list').val();
	let receiver_email_array_before = $('#corr_hidden_receiver_list').val();
	let receiver_email_array_after = [];
	for(let i=0;i<receiver_email_array_before.length;i++){
		receiver_email_array_after[i] = receiver_email_array_before[i].split('!!')[0];
	}
	let referto_email_array_before = $('#corr_hidden_referto_list').val();
	let referto_email_array_after = [];
	for(let i=0;i<referto_email_array_before.length;i++){
		referto_email_array_after[i] = referto_email_array_before[i].split('!!')[0];
	}
	let bcc_email_array_before = $('#corr_hidden_bcc_list').val();
	let bcc_email_array_after = [];
	for(let i=0;i<bcc_email_array_before.length;i++){
		bcc_email_array_after[i] = bcc_email_array_before[i].split('!!')[0];
	}
	var email_receiver_addr = receiver_email_array_after.join(';');
	var email_refer_to = referto_email_array_after.join(';');
	var email_bcc = bcc_email_array_after.join(';');
	if(email_sender_addr===null || email_sender_addr==='' || email_sender_addr===undefined){
		alert('Sender를 입력해야합니다.');
	}else if(email_receiver_addr===null || email_receiver_addr==='' || email_receiver_addr===undefined){
		alert('Receiver를 설정해주세요.');
	}else if(subject===null || subject==='' || subject===undefined){
		alert('Subject를 입력해주세요.');
	}else{

		if(!email_check(email_sender_addr)){
			alert("SENDER E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. SENDER는 하나의 메일주소만 설정할 수 있습니다.");
			return false;
		}
		for(let i=0;i<receiver_email_array_after.length;i++){
			if(!email_check(receiver_email_array_after[i])){
				alert("RECEIVER E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
				return false;
			}
		}
		for(let i=0;i<referto_email_array_after.length;i++){
			if(!email_check(referto_email_array_after[i])){
				alert("REFERTO E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
				return false;
			}
		}
		for(let i=0;i<bcc_email_array_after.length;i++){
			if(!email_check(bcc_email_array_after[i])){
				alert("BCC E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
				return false;
			}
		}
		
		$.ajax({
			type: 'POST',
			url: '../insertPrjEmailCorr.do',
			data:{
				prj_id:selectPrjId,
				email_type_id:email_type_id,
				subject:subject,
				contents:contents,
				email_sender_addr:email_sender_addr,
				email_receiver_addr:email_receiver_addr,
				user_data00:user_data00,
				email_refer_to:email_refer_to,
				email_bcc:email_bcc,
				docid_revid_docno:DC.docid_revid_docno,
				doc_no:doc_no,
				doc_id:DC.docid,
				rev_id:DC.revid
			},
			success: function(data) {
				if(data[0]>0){// && data[2]>0 && data[3]>0
					alert('E-mail sent successfully');
					$(".pop_corrSendEmail").dialog('close');
					$.ajax({
					    url: '../getCorrDocumentInfo.do',
					    type: 'POST',
					    data:{
					    	prj_id:selectPrjId,
							doc_id:DC.docid,
							rev_id:DC.revid
					    },
					    async:false,
					    success: function onData (data) {
					    	$('#corr_email_doc_no').val(data[0].sys_corr_no);
					    	$('#corr_email_docno_corrno').html('CORR NO : '+data[0].doc_no);
					    	if(data[1]>0)
					    		$('#corr_email_sent_times').html('<a class="link_class" onclick="corrSendingHistory(`'+data[0].sys_corr_no+'`)">This email already has been sent.('+data[1]+' times)</a>');
					    	else
					    		$('#corr_email_sent_times').html('');
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else{
					alert('Failed to sent E-mail');
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function getCorrOriginEmailList(corrSelectedAddType){
	let emailList = '';
	if(corrSelectedAddType === '#receiver'){
		emailList = $('#corr_hidden_receiver_list').val();
	}else if(corrSelectedAddType==='#referto'){
		emailList = $('#corr_hidden_referto_list').val();
	}else if(corrSelectedAddType==='#bcc'){
		emailList = $('#corr_hidden_bcc_list').val();
	}
	if(emailList!==''){
		let emailArray = emailList.split(',');
		let html = '';
		for(let i=0;i<emailArray.length;i++){
			let usernameplusemailid = emailArray[i].split('(')[0] +'_' + emailArray[i].split('(')[1].split('@')[0];
			html += '<li class="'+usernameplusemailid+'">'+emailArray[i]+' <a class="btn_del" onclick="corrReceiverDelete(`'+emailArray[i]+'`,`'+emailArray[i].split('(')[1].slice(0,-1)+'`,`'+usernameplusemailid+'`)">삭제</a></li>';
			corrSelectedUserEmail+=emailArray[i]+',';
			corrSelectedEmail+=emailArray[i].split('(')[1].slice(0,-1) + ',';
		}
		corrSelectedUserEmail = corrSelectedUserEmail.slice(0,-1);
		corrSelectedEmail = corrSelectedEmail.slice(0,-1);
		$('#corrSettingUserEmailList').html(html);
	}else{
		$('#corrSettingUserEmailList').html('');
	}
}
function getCorrUserEmailList(){
	$.ajax({
	    url: '../getAllUsersVOList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var userList = [];
	    	for(i=0;i<data[0].length;i++){
	    		userList.push({
                    No: i+1,
		            userId: data[0][i].user_id,
		            KorName: data[0][i].user_kor_nm,
		            EngName: data[0][i].user_eng_nm,
		            email: data[0][i].email_addr,
		            company: data[0][i].company_nm,
		            bean:data[0][i]
	    		});
	    	}
	    	setCorrUserEmailList(userList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function corrUsersEmailSearch(){
	var searchUser = $('input[name="corrSearchUserEmail"]:checked').val();
	var searchKeyword = $('#corrUsersEmailSearchKeyword').val();
	if(searchKeyword=='' || searchKeyword==undefined || searchKeyword==null){
		alert('검색어를 입력해주세요.');
	}else{
		$.ajax({
		    url: '../searchUsersVO.do',
		    type: 'POST',
		    data:{
		    	searchUser:searchUser, // 체크박스 값
		    	searchKeyword:searchKeyword // 검색창 값
		    },
		    success: function onData (data) {
		    	var userSearchEmailList = [];
		    	for(i=0;i<data[0].length;i++){
		    		userSearchEmailList.push({
	                    No: i+1,
			            userId: data[0][i].user_id,
			            KorName: data[0][i].user_kor_nm,
			            EngName: data[0][i].user_eng_nm,
			            email: data[0][i].email_addr,
			            company: data[0][i].company_nm,
			            bean:data[0][i]
		    		});
		    	}
		    	setCorrUserEmailList(userSearchEmailList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function setCorrUserEmailList(userSearchEmailList){
    var table = new Tabulator("#corrUserSearchEmailList", {
        selectable:1,//true
        data: userSearchEmailList,
        layout: "fitColumns",
        height: 200,
        columns: [{
                title: "No",
                field: "No"
            },
            {
                title: "User ID",
                field: "userId"
            },
            {
                title: "Kor.Name",
                field: "KorName"
            },
            {
                title: "Group",
                field: "group"
            },
            {
                title: "Eng.Name",
                field: "EngName"
            },
            {
                title: "E-mail",
                field: "email"
            },
            {
                title: "Company",
                field: "company"
            }
        ],
    });
    
    table.on("rowSelected", function(row){
    	if(corrSelectedUserEmail!=''){
    		corrSelectedUserEmail += ',';
    		corrSelectedEmail += ',';
    		corrSelectedUserId += ',';
    	}
    	corrSelectedUserEmail += row.getData().bean.user_kor_nm + '(' + row.getData().bean.email_addr + ')';
    	corrSelectedEmail += row.getData().bean.email_addr;
    	corrSelectedUserId += row.getData().bean.user_id;

    	if(corrSelectedUserEmail!=''){
			var arraySelectedUserEmail = corrSelectedUserEmail.split(',');
			corrSelectedUserEmail = [...new Set(arraySelectedUserEmail)];
			var arraySelectedEmail = corrSelectedEmail.split(',');
			corrSelectedEmail = [...new Set(arraySelectedEmail)];
			var arraySelectedUserId = corrSelectedUserId.split(',');
			corrSelectedUserId = [...new Set(arraySelectedUserId)];
			let html = '';
			for(let i=0;i<corrSelectedUserEmail.length;i++){
				let usernameplusemailid = corrSelectedUserEmail[i].split('(')[0] + '_' + corrSelectedUserEmail[i].split('(')[1].split('@')[0];
				html += '<li class="'+usernameplusemailid+'">'+corrSelectedUserEmail[i]+' <a class="btn_del" onclick="corrReceiverDelete(`'+corrSelectedUserEmail[i]+'`,`'+corrSelectedEmail[i]+'`,`'+usernameplusemailid+'`)">삭제</a></li>';
			}
			$('#corrSettingUserEmailList').html(html);
			corrSelectedUserEmail = corrSelectedUserEmail.join(',');
			corrSelectedEmail = corrSelectedEmail.join(',');
			corrSelectedUserId = corrSelectedUserId.join(',');
			
			if(corrSelectedAddType === '#receiver'){
				$('#corr_hidden_receiver_list').val(corrSelectedUserEmail);
				$('#corr_receiver_list').val(corrSelectedEmail);
			}else if(corrSelectedAddType==='#referto'){
				$('#corr_hidden_referto_list').val(corrSelectedUserEmail);
				$('#corr_referto_list').val(corrSelectedEmail);
			}else if(corrSelectedAddType==='#bcc'){
				$('#corr_hidden_bcc_list').val(corrSelectedUserEmail);
				$('#corr_bcc_list').val(corrSelectedEmail);
			}
    	}
    });
}
function corrReceiverDelete(corrSelectedUserEmail_i,corrSelectedEmail_i,usernameplusemailid){
	var arraySelectedUserEmail = corrSelectedUserEmail.split(',');
	var arraySelectedEmail = corrSelectedEmail.split(',');
	var arraySelectedUserId = corrSelectedUserId.split(',');
	let filteredselectedUserEmail = arraySelectedUserEmail.filter((element) => element !== corrSelectedUserEmail_i);
	let filteredselectedEmail = arraySelectedEmail.filter((element) => element !== corrSelectedEmail_i);
	//let filteredselectedUserId = arraySelectedUserId.filter((element) => element !== corrSelectedUserId_i);
	corrSelectedUserEmail = filteredselectedUserEmail;
	corrSelectedEmail = filteredselectedEmail;
	//corrSelectedUserId = filteredselectedUserId;
	corrSelectedUserEmail = corrSelectedUserEmail.join(',');
	corrSelectedEmail = corrSelectedEmail.join(',');
	
	//corrSelectedUserId = corrSelectedUserId.join(',');
	if(corrSelectedAddType === '#receiver'){
		$('#corr_hidden_receiver_list').val(corrSelectedUserEmail);
		$('#corr_receiver_list').val(corrSelectedEmail);
	}else if(corrSelectedAddType==='#referto'){
		$('#corr_hidden_referto_list').val(corrSelectedUserEmail);
		$('#corr_referto_list').val(corrSelectedEmail);
	}else if(corrSelectedAddType==='#bcc'){
		$('#corr_hidden_bcc_list').val(corrSelectedUserEmail);
		$('#corr_bcc_list').val(corrSelectedEmail);
	}
	$('.gray_small_box .'+usernameplusemailid).remove();
}
function corrUserSearchClose(){
	$('#corrSettingUserEmailList').html('');
	corrSelectedUserEmail='';
	corrSelectedEmail='';
	$(".pop_corrUserSearchM").dialog("close");
}
function corrUserEmailSearch(a){//a=this
	$(".pop_corrUserSearchM").dialog("open");
	getCorrUserEmailList();
	let add_id = $(a).attr('id');
	if(add_id==='corr_receiver_add'){
		corrSelectedAddType = '#receiver';
	}else if(add_id==='corr_referto_add'){
		corrSelectedAddType = '#referto';
	}else if(add_id==='corr_bcc_add'){
		corrSelectedAddType = '#bcc';
	}
	getCorrOriginEmailList(corrSelectedAddType);
}
function getCorrAccessHisList() {
	$(".pop_accHis").dialog("open");

	let doc_id = corr_new_or_update==='new'?'':corr_selected_doc_id;
	let rev_id = corr_new_or_update==='new'?'':corr_selected_rev_id;
	let doc_no = $('#corr_doc_no').val();
	let rev_code_id = $('#corrRevNoSelected').html();
	
	exeDown_doc_id = doc_id;
	
	if(rev_code_id == null || rev_code_id == "") {
		rev_code_id = "none";
    }
	if(doc_no == null || doc_no == "") {
		doc_no = "none";
    }
	
	$("#docNoRevNo").html(doc_no+", "+rev_code_id);
    
    	$.ajax({
		url: '../getAccHisList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	doc_id: doc_id,
	    	rev_id: rev_id,
	    },
	    success: function onData (data) {
	    	var accHis_tbl = [];
	    	for(i=0;i<data[0].length;i++){
	    		accHis_tbl.push({
	    			reg_date: getDateFormat(data[0][i].reg_date),
	    			reg_nm: data[0][i].reg_nm,
	    			remote_ip: data[0][i].remote_ip,
	    			method: data[0][i].method
	    		});
	    	}
	    	setAccessHisList(accHis_tbl);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getCorrEmailList(){
	$.ajax({
	    url: '../getAllUsersVOList.do',
	    type: 'POST',
	    async:false,
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		origin_email_list_corr.push(data[0][i].email_addr+'!!'+data[0][i].user_id);
	    		html += '<option value="'+data[0][i].email_addr+'!!'+data[0][i].user_id+'" label="'+data[0][i].user_kor_nm+'">'+data[0][i].user_kor_nm+'('+data[0][i].email_addr+')'+'</option>';
	    	}
	    	$('#corr_hidden_receiver_list').html(html);
	    	$('#corr_hidden_referto_list').html(html);
	    	$('#corr_hidden_bcc_list').html(html);
	    	
	    	$("#corr_hidden_receiver_list").selectize({
	    		delimiter:',',
	    		onType: function(str) {
	    			if (str.slice(-1) === ';') { 
	    				this.createItem(str.slice(0, -1))
	    				this.removeItem(';') // in case user enters just the delimiter
	    			}
	    		},
	    		splitOn:/[,;]/,
	    		persist: false,
	    		openOnFocus:false,
				createOnBlur: true,
		        closeAfterSelect: true,
				create: true,
				maxItems:null
	    	});
	    	$("#corr_hidden_referto_list").selectize({
	    		delimiter:',',
	    		onType: function(str) {
	    			if (str.slice(-1) === ';') { 
	    				this.createItem(str.slice(0, -1))
	    				this.removeItem(';') // in case user enters just the delimiter
	    			}
	    		},
	    		splitOn:/[,;]/,
	    		persist: false,
	    		openOnFocus:false,
				createOnBlur: true,
		        closeAfterSelect: true,
				create: true,
				maxItems:null
	    	});
	    	$("#corr_hidden_bcc_list").selectize({
	    		delimiter:',',
	    		onType: function(str) {
	    			if (str.slice(-1) === ';') { 
	    				this.createItem(str.slice(0, -1))
	    				this.removeItem(';') // in case user enters just the delimiter
	    			}
	    		},
	    		splitOn:/[,;]/,
	    		persist: false,
	    		openOnFocus:false,
				createOnBlur: true,
		        closeAfterSelect: true,
				create: true,
				maxItems:null
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function to_date_format(date_str, gubun)
{
	
	if(date_str == null) return '';
	
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(4,6);
    var sDate = yyyyMMdd.substring(6,8);
    return sYear + gubun + sMonth + gubun + sDate;
}
function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}
//20211101083217같은 string 형식의 데이터를 년-월-일 시:분:초 형태로 바꿔주는 함수
function getDateFormat(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);
	const hh = datestring.substring(8,10);
	const mm = datestring.substring(10,12);
	const ss = datestring.substring(12,14);

	return year+'-'+month+'-'+day+' '+hh+':'+mm+':'+ss;
}
function dialogInit(){
    $(".pop_userSch").dialog({
        autoOpen: false,
        maxWidth: 1200,
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
    $(".pop_drnHis").dialog({
      autoOpen: false,
      maxWidth: 800,
      width: 800
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
    $(".pop_documentDeleteConfirm").dialog({
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
    $(".pop_accHis").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: 800
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
}
function setAccessHisList(accHis_tbl) {
	 accessTable = new Tabulator("#accHis_tbl", {
	        data: accHis_tbl,
	        layout: "fitColumns",
	        placeholder:"No Data Set",
	        height: 200,
	        columns: [{
	                title: "DATE",
	                field: "reg_date"
	            },
	            {
	                title: "USER NAME",
	                field: "reg_nm",
	                width: 120
	            },
	            {
	                title: "IP",
	                field: "remote_ip"
	            },
	            {
	                title: "METHOD",
	                field: "method",
	                width: 120
	            }
	        ],
	    });
}
function accessHisToExcel() {
	let gridData = accessTable.getData();
	
	let jsonList = [];
	
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length ; i++)
		jsonList.push(JSON.stringify(gridData[i]));
	
	let fileName = 'AccessHistory';
	
	var f = document.accessExcelUploadForm;
	
	f.prj_id.value = selectPrjId;
	f.doc_id.value = exeDown_doc_id;
  f.fileName.value = fileName;
  f.jsonRowdatas.value = jsonList;	   
  f.action = "../accessExcelDownload.do";
  f.submit();  
	
}
window.onunload = function () {
	opener.getPrjDocumentIndexList();
}
function getAdminAndPrjDCCSysCorrNo(){
	  $.ajax({
		    url: '../getAdminAndDCCList.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId
		    },
		    success: function onData (data) {
		        let adminAndDCCArray = [];
			    let adminAndDCCString = '';
		    	adminAndDCCString = data[0];
		    	adminAndDCCArray = adminAndDCCString.split(',');

			    if(adminAndDCCArray.includes(session_user_id)){	//Admin이나 DCC일 때만 system_corr_no 노출
				    $('#sysCorrNoTr').css('display','table-row');
			    }else{
			    	$('#sysCorrNoTr').css('display','none');
			    }
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});

}
function getPrjMemeberListForPersonInCharge(){
	$.ajax({
	    url: '../getPrjMemberList.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId
	    },
	    async:false,
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		let dcc = '';
	    		if(data[0][i].dcc_yn==='Y'){
	    			dcc = ', DCC';
	    		}
	    		html += '<option value="'+data[0][i].user_id+'">'+data[0][i].user_kor_nm+'('+data[0][i].user_id+dcc+')</option>';
	    	}
	    	$('#corr_person_in_charge').html(html);
	    	$("#corr_person_in_charge").selectize({
	    		persist: false,
	    		openOnFocus:false,
				create: false,
				maxItems:1,
			    sortField: [
			        {
			            field: 'text',
			            direction: 'asc'
			        }
			    ]
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getITEMPrjCodeSettingsList(){
	$.ajax({
	    url: '../getITEMPrjCodeSettingsList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var item_list_tbl = [];
	    	for(i=0;i<data[0].length;i++){
	    		item_list_tbl.push({
	    			No: i,
	    			origin: 'update',
	    			ORIGIN_CODE: data[0][i].set_code_id,
	    			setOrder: data[0][i].set_order,
	    			CODE: data[0][i].set_code,
	    			desc: data[0][i].set_desc,
	    			client: data[0][i].set_val,
	    			bean: data[0][i]
	    		});
	    		setOrderArray[i] = data[0][i].set_order;
	    	}
	    	setItemList(item_list_tbl);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setItemList(item_list_tbl){
    itemListTable = new Tabulator("#item_list_tbl", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: item_list_tbl,
        layout: "fitColumns",
        selectable:1,//true
        height:"100%",
        placeholder:"No Data Set",
        columns: [
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('ITEMList');
	   		        checkbox.id = 'ITEMList'+cell.getRow().getPosition();
    		        checkbox.name = 'ITEMList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    }
    		},
            {
                title: "origin",
                field: "origin",
                visible:false
            },
            {
                title: "setOrder",
                field: "setOrder",
                visible:false
            },
            {
                title: "ITEM NO",
                field: "CODE",
                editor:"input"
            },
            {
                title: "Description",
                field: "desc",
                editor:"input"
            },
            {
                title: "CLIENT",
                field: "client",
                editor:"input"
            }
        ],
    });
    
    itemListTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().ORIGIN_CODE;
    })
}
function ITEMAdd(){
	addOrUpdate = 'add';
	var addrow =  { origin: 'add', CODE: '', desc: '', client: ''};
	itemListTable.addRow( addrow );
}
function ITEMRefresh(){
	getITEMPrjCodeSettingsList();
}
function ITEMApply(){
	var editedCells = itemListTable.getEditedCells();
	var set_code_arr = [];
	var selectTableData = itemListTable.getData();
	for(i=0;i<selectTableData.length;i++){
		if(selectTableData[i].CODE!=''){
			set_code_arr[i] = selectTableData[i].CODE.toUpperCase();
		}
	}
	var set_code_duplicate = isDuplicate(set_code_arr);
	if(set_code_duplicate){
		alert('ITEM NO에 중복값이 있습니다.');
	}else{
		var RowDataArray = [];
		for(i=0;i<editedCells.length;i++){
			var RowData = editedCells[i].getRow().getData();
			RowDataArray.push(RowData);
		}
		RowDataArray = duplicateRemoveInArray(RowDataArray);
		for(i=0;i<RowDataArray.length;i++){
	    	var set_code = RowDataArray[i].CODE.toUpperCase();
	    	var set_desc = RowDataArray[i].desc;
	    	var client = RowDataArray[i].client;
	    	var origin_code = RowDataArray[i].ORIGIN_CODE;
	    	if(RowDataArray[i].origin==='update' && set_code!='' && set_desc!='' && client!=''){
	    		updateITEMPrjCodeSettings(selectPrjId,set_code,set_desc,origin_code,client);
	    	}else if(RowDataArray[i].origin==='add' && set_code!='' && set_desc!='' && client!=''){
	    		insertITEMPrjCodeSettings(selectPrjId,set_code,set_desc,client);
	    	}
		}
		alert('Save Successfully');
		getITEMPrjCodeSettingsList();
	}
}
function isDuplicate(arr)  {
	  const isDup = arr.some(function(x) {
	    return arr.indexOf(x) !== arr.lastIndexOf(x);
	  });
	                         
	  return isDup;
}
function duplicateRemoveInArray(arr){
	var setObj = new Set(arr);
	var setArr = [...setObj];
	return setArr;
}
function insertITEMPrjCodeSettings(selectPrjId,set_code,set_desc,client){
	$.ajax({
	    url: '../insertITEMPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code: set_code,
	    	set_desc: set_desc,
	    	set_val: client
	    },
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function updateITEMPrjCodeSettings(selectPrjId,set_code,set_desc,origin_code,client){
	$.ajax({
	    url: '../updateITEMPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code: set_code,
	    	set_desc: set_desc,
	    	origin_code: origin_code,
	    	set_val: client
	    },
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function ITEMDeletePopup(){
	if($("input:checkbox[name='ITEMList']:checked").length===0){
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}else{
	    $(".pop_ITEMDelete").dialog("open");
	}
}
function ITEMDeleteCancel(){
    $(".pop_ITEMDelete").dialog("close");
}
function ITEMDelete(){
	var checkedIndex = '';
	$("input:checkbox[name='ITEMList']:checked").each(function(){
		checkedIndex += $(this).attr('id').replaceAll('ITEMList','') + ',';
	});
	checkedIndex = checkedIndex.slice(0, -1);
	var checkedIndexArray = checkedIndex.split(',');
	var checkedCodeIdString = '(';
	for(i=0;i<checkedIndexArray.length;i++){
		checkedCodeIdString += "'" + itemListTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_CODE +"'" +',';
	}
	checkedCodeIdString = checkedCodeIdString.slice(0, -1)+')';
	$.ajax({
	    url: '../deleteITEMPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code_id: checkedCodeIdString
	    },
	    success: function onData (data) {
	    	alert('삭제되었습니다.');
	        $(".pop_ITEMDelete").dialog("close");
	    	getITEMPrjCodeSettingsList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getCorrItemMultiBox(){
	$.ajax({
	    url: '../getITEMPrjCodeSettingsList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
            var html = '<tr><th>ITEM NO</th><th>DESCRIPTION</th><th>CLIENT</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectItem"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td><td>'+data[0][i].set_val+'</td></tr>';
            }
	    	$('#corrItemMultiBox').html(html);
	    	$('.selectItem').click(function(){
	    		$('#corrItemSelectWrap').removeClass('on');
	    		$('#corrItemSelected').html($(this).children().html());
	    		$('#corrSelectedItemSetCodeId').val($(this).children().attr('id'));
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function to_date_format(date_str, gubun)
{
	
	if(date_str == null) return '';
	
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(4,6);
    var sDate = yyyyMMdd.substring(6,8);
    return sYear + gubun + sMonth + gubun + sDate;
}
//이메일 정규식 체크
function email_check(email) {
	if(email===''){
		return true;
	}else{
		var reg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		return reg.test(email);
	}
}
function propertydownload(){
	$.ajax({
	    url: '../getCorrDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:corr_selected_doc_id,
	    	rev_id:corr_selected_rev_id,
	    	inout:''
	    },
	    success: function onData (data) {
			let multiDownTbl = [];
			let lock_yn = data[0].lock_yn;
			if(lock_yn==='Y'){
				alert('잠겨있는 도서는 다운받을 수 없습니다.');
			}else{
		    	multiDownTbl.push({
		    		FileName:data[0].rfile_nm,
		    		RevNo:data[0].rev_no,
		    		Title:data[0].title,
		    		Size:formatBytes(data[0].file_size,2),
		    		bean:data[0]
		    	});

		    	multiDownloadTable = new Tabulator("#multiDownTbl", {
		    		data: multiDownTbl,
		    		layout: "fitColumns",
		    		columns: [{
		    				title: "File Name",
		    				field: "FileName"
		    			},
		    			{
		    				title: "Rev No",
		    				field: "RevNo"
		    			},
		    			{
		    				title: "Title",
		    				field: "Title"
		    			},
		    			{
		    				title: "Size",
		    				field: "Size"
		    			}
		    		],
		    	});
		    	$(".pop_multiDownload").dialog("open");
		    	$('#downloadFileRename').prop('checked',false);
		    	$('#fileRenameSet').val('');
		    	$('#fileMultiDownload').attr('onclick','fileMultiDownload()');
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
</script>