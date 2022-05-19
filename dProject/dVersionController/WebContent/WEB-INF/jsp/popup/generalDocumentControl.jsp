<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="dialog pop_generalDocNew" title="General Document" style="max-width:1200px;">
    <div class="pop_box">
        <section ng-app="angularResizable4" id="generalDocNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton('General')">Refresh</button>
                            <button class="button btn_blue" onclick="generalNewButton()" id="generalNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_general_current_folder_path">
                        </p>
                        <div class="mb5">
                            <input type="checkbox" id="popup_general_rev_version" onchange="getPopupPrjDocumentIndexList('General')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="generalDocNoTbl">
                        </div>
                    </section>
                    <section id="pop_ed_right">
                        <div class="pg_top">
                             <div class="left">
								<button class="button btn_orange" id="general_revise" onclick="updateRevisionGeneral()" disabled>
									Update
								</button>
								<button class="button btn_orange" onclick="getGeneralAccessHisList()">
									Access History
								</button>
                                    <button class="button btn_orange" id="general_save" onclick="generalDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" onclick="docDelete('General')">
                                        Delete
                                    </button>
							</div>
                            <div class="right">
                                <button class="btn_icon btn_blue" onclick="generalPopupClose()"><i class="fas fa-times"></i><span>Close</span></button>
                            </div>
                        </div>
                        <h4>General Document <i id="general_locker" class="popup_locker"></i><i id="general_oldversion" class="popup_oldversion"></i></h4>
                        <div class="tbl_wrap" style="display:inline;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        Doc. No <i>required</i>
                                    </th>
                                    <td><input type="text" class="full popupinput" id="general_doc_no"></td>
                                    <th>Rev. No</th>
                                    <td>
                                    	<input type="hidden" id="general_rev_max">
                                        <div class="multi_sel_wrap ml5" id="generalRevNoSelectWrap">
                                        	<button class="multi_sel" id="generalRevNoSelected">SELECT</button>
                                        	<input type="hidden" id="generalSelectedRevNoSetCodeId">
                                        	<input type="hidden" id="general_origin_rev_code_id">
                                        	<div class="multi_box">
                                            	<table id="generalRevNoMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                </tr>
                                <tr>
                                    <th>File Attach</th>
                                    <td colspan="3">
                                        <!--  <button class="button btn_blue">Attach</button> -->
										<form id="updateRevisionGeneralForm" name="updateRevisionGeneralForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					                    <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="general_attachfilename" disabled="disabled">
					                        <label for="general_attach_filename" class="button btn_blue" id="general_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="general_attach_filename" class="upload-hidden generalDocumentFileAttach">
					                    </span>
					                    <input type="text" id="general_uploadFileInfo" disabled><input type="text" class="ml5" id="general_uploadFileDate" disabled>
                                       	</form>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Title <i>required</i>
                                    </th>
                                    <td colspan="3"><input type="text" class="full popupinput" id="general_title"></td>
                                </tr>
                                <tr>
                                    <th>Creation</th>
                                    <td><input type="text" class="full popupinput" id="general_creation" disabled></td>
                                    <th>Modification</th>
                                    <td><input type="text" class="full popupinput" id="general_modification" disabled></td>
                                </tr>
                            </table>
                        </div>
                    </section>
                </div>
            </div>
        </section>

    </div>
</div>
