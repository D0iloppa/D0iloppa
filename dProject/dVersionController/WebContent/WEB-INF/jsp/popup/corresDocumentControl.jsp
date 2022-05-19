<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="dialog pop_corrsDocNew" title="Correspondence Document" style="max-width:1500px;">
    <div class="pop_box">
        <section ng-app="angularResizable5" id="corrsDocNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton('Corr')">Refresh</button>
                            <button class="button btn_blue" onclick="corrNewButton()" id="corrNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_corr_current_folder_path">
                        </p>
                        <div class="mb5">
                            <input type="checkbox" id="popup_corr_rev_version" onchange="getPopupPrjDocumentIndexList('Corr')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="corrDocNoTbl">
                        </div>
                    </section>
                    <section id="pop_ed_right">
                        <div class="pg_top">
                             <div class="left">
								<button class="button btn_orange" onclick="corrEmailOpen()">
									eMailing
								</button>
								<button class="button btn_orange" onclick="getCorrAccessHisList()">
									Access History
								</button>
                                    <button class="button btn_orange" id="corr_save" onclick="corrDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" onclick="docDelete('Corr')">
                                        Delete
                                    </button>
							</div>
                            <div class="right">
                                <button class="btn_icon btn_blue" onclick="corrPopupClose()"><i class="fas fa-times"></i><span>Close</span></button>
                            </div>
                        </div>
                        <h4>Correspondence Document <i id="corr_locker" class="popup_locker"></i></h4>
                        <div class="tbl_wrap" style="display:inline;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        In/Out <i>required</i>
                                    </th>
                                    <td>
                                    	<span><input type="radio" name="inout" value="O"><label for="">Outgoing</label></span>
                                    	<span><input type="radio" name="inout" value="I"><label for="">Incoming</label></span>
                                    </td>
                                    <th class="required">
                                        Document Date  <i>required</i>
                                    </th>
                                    <td>
                                        <input type="text" class="date" id="corr_doc_date">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Sender <i>required</i>
                                    </th>
                                    <td>
                                    	<!-- <select name="" id="corr_sender"></select> -->
                                        <div class="multi_sel_wrap ml5" id="corrSenderSelectWrap">
                                        	<button class="multi_sel" id="corrSenderSelected">SELECT</button>
                                        	<input type="hidden" id="corrSelectedSenderSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="corrSenderMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">
                                        Receiver <i>required</i>
                                    </th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="corrReceiverSelectWrap">
                                        	<button class="multi_sel" id="corrReceiverSelected">SELECT</button>
                                        	<input type="hidden" id="corrSelectedReceiverSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="corrReceiverMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Type <i>required</i>
                                    </th>
                                    <td>
                                    	<!-- <select name="" id="corr_type"></select> -->
                                        <div class="multi_sel_wrap ml5" id="corrTypeSelectWrap">
                                        	<button class="multi_sel" id="corrTypeSelected">SELECT</button>
                                        	<input type="hidden" id="corrSelectedTypeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="corrTypeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">
                                        Serial <i>required</i>
                                    </th>
                                    <td>
                                        <span class="tblSchSpan"></span><input type="hidden" id="corr_serial_realdata"><input type="text" class="search" id="corr_serial" onKeyup="serialChange()"><a href="#" id="autoSerial" class="button btn_blue ml5" onclick="getCorrSerialAuto()">Auto</a><a href="#" id="notAutoSerial" class="button btn_blue ml5" style="display:none;" onclick="getCorrSerialAuto('not')">Check</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required" rowspan="2">
                                        Corr. No <i>required</i>
                                    </th>
                                    <td colspan="3">
                                    	<input type="hidden" id="origin_corr_no">
                                    	<input type="text" id="corr_corr_no_1">
                                    	<button class="button btn_blue" onclick="CorrDocSearch()">Cor. Search</button>
                                        <span class="multi_sel_wrap ml5" id="corrNoSelectWrap">
                                        	<button class="multi_sel" id="corrNoSelected">Search</button>
                                        	<div class="multi_box">
                                            	<table id="corrNoMultiBox">
                                            	</table>
                                       		</div>
                                    	</span>  
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                    	<input type="text" id="corr_corr_no_2">
										<button class="button btn_blue" onclick="RefDocSearch()">Ref. Search</button>
                                        <span class="multi_sel_wrap ml5" id="corrNo2SelectWrap">
                                        	<button class="multi_sel" id="corrNo2Selected">Search</button>
                                        	<div class="multi_box">
                                            	<table id="corrNo2MultiBox">
                                            	</table>
                                       		</div>
                                    	</span>  
                                    	<button class="btn_icon btn_blue ml5" onclick="refPlus()"><i class="fa fa-plus"></i></button><button class="btn_icon btn_blue ml5" onclick="refMinus()"><i class="fa fa-minus"></i></button>
                                    </td>
                                </tr>
                                <tr>
                                	<th>Ref. Doc No</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_ref_doc_no" disabled></td>
                                </tr>
                                <tr>
                                	<th>File Attach</th>
                                    <td colspan="3">
                                        <form id="updateRevisionCorrForm" name="updateRevisionCorrForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
                                        <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="corr_attachfilename" disabled="disabled">
					                        <label for="corr_attach_filename" class="button btn_blue" id="corr_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="corr_attach_filename" class="upload-hidden corrDocumentFileAttach">
					                    </span>
					                    <input type="text" id="corr_uploadFileInfo" disabled><input type="text" class="ml5" id="corr_uploadFileDate" disabled>
										</form>
                                    </td>
                                </tr>
                                <tr>
                                	<th>Title</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_title"></td>
                                </tr>
                                <tr>
                                	<th>Creation</th>
                                    <td><input type="text" class="full popupinput" id="corr_creation" disabled></td>
                                    <th>Modification</th>
                                    <td><input type="text" class="full popupinput" id="corr_modification" disabled></td>
                                </tr>
                                <tr>
                                	<th>Responsibility</th>
                                	<td>
                                        <div class="multi_sel_wrap ml5" id="corrResponSelectWrap">
                                        	<button class="multi_sel" id="corrResponSelected">SELECT</button>
                                        	<input type="hidden" id="corrSelectedResponSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="corrResponMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                	</td>
                                	<th>Originator</th>
                                	<td><input type="text" class="full" id="corr_originator"></td>
                                </tr>
                                <tr>
                                	<th>Distribute</th>
                                	<td colspan="3">
                                		<span><input type="checkbox" name="corr_distribute" value="BB"><label for="">BB</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="TE"><label for="">TE</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="BM"><label for="">BM</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="BN"><label for="">BN</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="BP"><label for="">BP</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="BS"><label for="">BS</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="CA"><label for="">CA</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DA"><label for="">DA</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DB"><label for="">DB</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DC"><label for="">DC</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DD"><label for="">DD</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DE"><label for="">DE</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DF"><label for="">DF</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DR"><label for="">DR</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DI"><label for="">DI</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DL"><label for="">DL</label></span>
                                		<span><input type="checkbox" name="corr_distribute" value="DM"><label for="">DM</label></span>
                                	</td>
                                </tr>
                                <tr>
                                	<th>Rep. Req. Date</th>
                                	<td><input type="text" class="date" id="corr_rep_req_date"></td>
                                	<th>Int. Dist Date</th>
                                	<td><input type="text" class="date" id="corr_int_dist_date"></td>
                                </tr>
                                <tr>
                                	<th>Act. Req. Date</th>
                                	<td colspan="3"><input type="text" class="date" id="corr_act_req_date"></td>
                                </tr>
                                <tr>
                                	<th>Remark</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_remark"></td>
                                </tr>
                                <tr>
                                	<th>UserDate-00</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_userdate_00"></td>
                                </tr>
                                <tr>
                                	<th>UserDate-01</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_userdate_01"></td>
                                </tr>
                                <tr>
                                	<th>UserDate-02</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_userdate_02"></td>
                                </tr>
                                <tr>
                                	<th>UserDate-03</th>
                                	<td colspan="3"><input type="text" class="full" id="corr_userdate_03"></td>
                                </tr>
                                <tr>
                                	<th>Reference By</th>
                                	<td colspan="3">
                                		<select name="" id="corr_reference_by"></select>
                                	</td>
                                </tr>
                            </table>
                        </div>
                    </section>
                </div>
            </div>
        </section>

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
        <hr>
        <div class="tbl_wrap">
            <table class="write">
                <colgroup>
                    <col style="width:25%">
                    <col style="width:*">
                </colgroup>
                <tr>
                    <th>Type</th>
                    <td> 
						<div class="multi_sel_wrap ml5" id="corrEmailSelectWrap">
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
                    <td><input type="text" id="corr_receiver_list" style="width:90%;"><input type="hidden" id="corr_hidden_receiver_list"><button class="button btn_blue ml5 corrUserEmailSearch" id="corr_receiver_add" onclick="corrUserEmailSearch(this)">추가</button></td>
                </tr>
                <tr>
                    <th>ReferTo</th>
                    <td><input type="text" id="corr_referto_list" style="width:90%;"><input type="hidden" id="corr_hidden_referto_list"><button class="button btn_blue ml5 corrUserEmailSearch" id="corr_referto_add" onclick="corrUserEmailSearch(this)">추가</button></td>
                </tr>
                <tr>
                    <th>BCC</th>
                    <td><input type="text" id="corr_bcc_list" style="width:90%;"><input type="hidden" id="corr_hidden_bcc_list"><button class="button btn_blue ml5 corrUserEmailSearch"id="corr_bcc_add" onclick="corrUserEmailSearch(this)">추가</button></td>
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