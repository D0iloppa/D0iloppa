<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

                           <div id="pop_docSel" class="dialog pop_docSel" title="Document Select" style="max-width:1300px;">
							    <div class="pop_box">
							        <section ng-app="angularResizable2" id="docCtrlNew_left">
							            <div class="content">
							                <div class="row" resizable r-flex="true">
							                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
							                        <nav id="docSelTree">
							                        </nav>
                       								<div class="rg-rightBack"></div>
							                    </section>
							                    <section id="pop_ed_right" style="flex:2.5;">
							                        <div class="pg_top">
							                            <div style="width:100%;">
							                                <label for="">Path</label>
							                                <input type="text" class="full" id="docsel_current_folder_path" disabled>
							                                <!-- <label for="" class="ml20">Filter</label>
							                                <input type="text" class="ml5"> -->
							                            </div>
							                        </div>
							                        <div class="pg_top">
							                            <div class="left">
							                                <select name="" id="docsel_select_rev_version" class="ml5">
							                                    <option value="current">Current Version</option>
							                                    <option value="all">All Version</option>
							                                </select>
							                                <button class="button btn_orange" onclick="out_selectDocument()">
							                                    Select Document
							                                </button>
															<button class="btn_icon btn_green" id="TRAddDoc_Retrieve">
																<i class="fas fa-sync"></i> <span>Retrieve</span>
															</button>
															<button class="btn_icon btn_green" onclick="TRdocumentControlDownload()">
																<i class="fas fa-download"></i> <span>Download</span>
															</button>
															<!-- <button class="btn_icon btn_green"onclick="TRdcMultiUpload()">
																<i class="fas fa-upload"></i> <span>Upload</span>
															</button> -->
															<button class="btn_icon btn_green" onclick="documentExcel()">
																<i class="far fa-file-excel"></i> <span>excel</span>
															</button>
							                                <input type="checkbox" class="ml10" id="docsel_unread_yn"><label for="">Unread Doc</label>
							                            </div>
							                        </div>
							                        <hr>
													<div style="min-height: 70vh;display: flex;flex-flow: column;">
														<div class="tbl_wrap icon_tbl" id="docSelTbl">
								
								                        </div>
														<!-- <div class="documentControlTblWrap" style="flex: 1 auto;"></div> -->
													</div>
							                        <%-- <%@ include file="../page/documentControl.jsp" %> --%>
							                    </section>
							                </div>
							            </div>
							        </section>
							
							    </div>
								<input type="hidden" id="docSelTree_discipline_code_id">
								<input type="hidden" id="docSelTree_discipline_display">
								<input type="hidden" id="docSelTree_doc_type">
							</div>

<div class="dialog pop_issuecancel" title="ISSUE CANCEL CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Are you sure you want to cancle this TR ISSUE?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="out_issueCancel()">Confirm</button>
                <button class="button btn_purple" onclick="TRIssueCancelCancel()">No</button>
            </div>
    </div>
</div>
<div class="dialog pop_issuecancelDCC" title="ISSUE CANCEL CONFIRM(DCC or ADMIN)" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		기존 최종승인 받았던 DRN은 반려상태로 변경되고 TR은 삭제됩니다. 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" id="TRIssueCancelDCC_Confirm">Confirm</button>
                <button class="button btn_purple" onclick="TRIssueCancelDCCCancel()">No</button>
            </div>
    </div>
</div>
<div class="dialog pop_TrDelete" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete this TR?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="out_delete()">Confirm</button>
                <button class="button btn_purple" onclick="TRDeleteCancel()">No</button>
            </div>
    </div>
</div>



<div class="dialog pop_mailNotice" title="E-mailing Notice" style="max-width:1100px;">
    <div class="pop_box">
        <div class="pg_top">
            <div class="left">
                <span id="email_trno_corrno"></span>
                <b class="blue" id="email_sent_times"></b>
            </div>
            <div class="right">
                <button class="button btn_blue" onclick="sendEmailConfrim()">Send</button>
                <button class="button btn_purple" onclick="sendEmailClose()">Close</button>
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
                    <th>Type</th>
                    <td> 
						<div class="multi_sel_wrap ml5" id="outEmailSelectWrap">
							<button class="multi_sel" id="outEmailTypeSelected">SELECT</button>
							<input type="hidden" id="selectedOutEmailFeature"> <input
								type="hidden" id="selectedOutEmailTypeId">
							<div class="multi_box">
								<table id="outEmailTypeMultiBox">
								</table>
							</div>
						</div>
                    </td>
                </tr>
                <tr>
                    <th>Sender *</th>
                    <td><input type="text" id="sender_list" value="${sessionScope.login.email_addr}" style="width:90%;"><!-- <button class="button btn_blue ml5">추가</button> --></td>
                </tr>
                <tr>
                    <th>Receiver *</th>
                    <td>
						<select id="hidden_receiver_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>ReferTo</th>
                    <td>
						<select id="hidden_referto_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>BCC</th>
                    <td>
						<select id="hidden_bcc_list" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                </td>
                </tr>
                <tr>
                    <th>Subject *</th>
                    <td><input type="text" id="out_email_subject" style="width:100%;"></td>
                </tr>
            </table>
            <input type="hidden" id="email_tr_id">
        </div>
        <div class="smartEdit" style="min-height:200px; border: solid 1px #ddd;">
            <textarea rows="500" id="summernoteMailOut" cols="500" style="max-height: 300px;"></textarea>
		</div>
    </div>
</div>

<div class="dialog pop_userSearchM" title="E-mailing Notice" style="max-width:1100px;">
    <div class="pop_box">
        <div class="tbl_wrap">
            <table class="write">
                <tr>
                    <th>Search Condition</th>
                    <td>
                        <div class="line">
                            <input type="radio" name="searchUserEmail" value="user_id">
                            <label for="">User ID</label>

                            <input type="radio" name="searchUserEmail" value="user_kor_nm" class="ml10" checked>
                            <label for="">Kor. Name</label>
                        </div>
                        <div class="line">
                            <input type="text" id="usersEmailSearchKeyword">
                            <button class="button btn_search ml5" onclick="usersEmailSearch()">Search</button>
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
        <div class="tbl_wrap" id="userSearchEmailList">

        </div>

        <div class="pg_top">
            <div class="left">
                <h5>Email List</h5>
            </div>
            <div class="right">
                <button class="button btn_small btn_gray" onclick="userSearchClose()">확인</button>
            </div>
        </div>
        <div class="gray_small_box">
            <ul class="user_name" id="settingUserEmailList">
            </ul>
        </div>
    </div>
</div>
<div class="dialog pop_SendEmail" title="SEND E-MAIL CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Do you want to send email?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="sendEmail()">Confirm</button>
                <button class="button btn_purple" onclick="sendEmailCancel()">No</button>
            </div>
    </div>
</div>
<div class="dialog pop_TRSendingHistory" title="Sending History" style="max-width:800px">
    <div class="pop_box">
        <div class="tbl_wrap" id="trOutSendingHistoryTbl">
        </div>
    </div>
</div>

<div class="dialog TRpop_upload" title="Multi Upload" style="max-width:1000px">
	<div class="pop_box">
		<div class="pg_top">
			<div class="right">
				<button class="button btn_blue ml5 TRdc_multiUpload" onclick="TRdcUpload_addBtn()">Add</button>
				<button class="button btn_blue TRdc_multiUpload" onclick="TRdcUpload_delBtn()">Delete</button>
			</div>
		</div>
		<!-- 2 files (2.93MB)-->
		<h5 class="TRdc_multiUpload">0 files (0 byte)</h5>
		<div class="tbl_wrap" id="multiUploadTbl"></div>
		<form id="TRdc_multiUpload_form" method="post" enctype="multipart/form-data">
			<input type="file" multiple="multiple" name="TRuploadfile" class="TRdc_multiUpload" style="display: none;" onchange="dc_multiFileUp(this)" accept=".doc, .xlsx, image/*">
		</form>
		<div class="btn_wrap mt10 txt_center">
			<button class="button btn_purple TRdc_multiUpload" onclick="TRdcUpload_uploadBtn()">Upload</button>
			<button class="button btn_blue TRdc_multiUpload" onclick="TRdcUpload_closeBtn()">Close</button>
		</div>
	</div>
</div>