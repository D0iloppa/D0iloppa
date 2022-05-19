<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="dialog pop_docEngPopup" title="Engineering Document" style="max-width:1200px;">
    <div class="pop_box">
        <section ng-app="angularResizable2" id="docCtrlNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton('DCI')" id="engRefreshButton">Refresh</button>
                            <button class="button btn_blue" onclick="popupNewButton()" id="engNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_dci_current_folder_path">
                        </p>
                        <div class="mb5 allVersionCheckDiv">
                            <input type="checkbox" id="popup_eng_rev_version" onchange="getPopupPrjDocumentIndexList('DCI')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="docNoTbl">
                        </div>
                    </section>
                    <section id="pop_ed_right">
                    <div id = "pdfDivEng">
                        <div class="pg_top">
                             <div class="left">
                                    <button class="button btn_orange popupDrnUp">
                                        DRN 상신
                                    </button>
                                    <button class="button btn_orange" id="popup_revise" onclick="updateRevision()" disabled>
                                        Revise
                                    </button>
                                    <button class="button btn_green" onclick="drnHistoryOpen()">
                                        DRN History
                                    </button>
                                    <button class="button btn_green" onclick="getEngAccessHisList()">
                                        Access History
                                    </button>
                                    <button class="button btn_orange" id="popup_save" onclick="engDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" onclick="docDelete('DCI')">
                                        Delete
                                    </button>
                                </div>
                            <div class="right">
                                <!-- <button class="btn_icon btn_blue" onclick="engDocSave()"><i class="fas fa-save"></i><span>Save</span></button>
                                <button class="btn_icon btn_blue"><i class="fas fa-trash-alt"></i><span>Delete</span></button> -->
                                <button id="savePdf" class="btn_icon btn_blue" onclick="pageprint('DCI')"><i class="fas fa-print"></i><span>Print</span></button>
                                <button class="btn_icon btn_blue" onclick="engPopupClose()"><i class="fas fa-times"></i><span>Close</span></button>
                            </div>
                        </div>
                        <h4>Engineering Process <i id="popup_locker" class="popup_locker"></i><i id="popup_oldversion" class="popup_oldversion"></i></h4>
                        <div class="tbl_wrap" style="display:inline;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        Doc. No <i>required</i>
                                    </th>
                                    <td><input type="text" class="full popupinput" id="popup_doc_no"></td>
                                    <th>Rev. No</th>
                                    <td>
                                    	<input type="hidden" id="popup_rev_max">
                                        <!-- <select name="" id="popup_rev_no"></select> -->
                                        <div class="multi_sel_wrap ml5" id="revNoSelectWrap">
                                        	<button class="multi_sel" id="revNoSelected">SELECT</button>
                                        	<input type="hidden" id="selectedRevNoSetCodeId">
                                        	<input type="hidden" id="origin_rev_code_id">
                                        	<div class="multi_box">
                                            	<table id="revNoMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th>Old Doc. No</th>
                                    <td>
                                        <input type="text" class="full popupinput" id="popup_old_doc_no" disabled>
                                    </td>
                                </tr>
                                <tr id="engFileAttachTr">
                                    <th>File Attach</th>
                                    <td colspan="5">
                                       <!--  <button class="button btn_blue">Attach</button> -->
										<form id="updateRevisionForm" name="updateRevisionForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					                    <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="eng_popup_attachfilename" disabled="disabled">
					                        <label for="eng_attach_filename" class="button btn_blue" id="eng_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="eng_attach_filename" class="upload-hidden documentFileAttach">
					                    </span>
					                    <input type="text" id="eng_pop_uploadFileInfo" disabled><input type="text" class="ml5" id="eng_pop_uploadFileDate" disabled>
                                    	</form>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Title <i>required</i>
                                    </th>
                                    <td colspan="5"><input type="text" class="full popupinput" id="popup_title"></td>
                                </tr>
                                <tr>
                                    <th>Creation</th>
                                    <td colspan="2"><input type="text" class="full popupinput" id="popup_creation" disabled></td>
                                    <th>Modification</th>
                                    <td colspan="2"><input type="text" class="full popupinput" id="popup_modification" disabled></td>
                                </tr>
                                <tr>
                                    <th>Discipline</th>
                                    <td colspan="2"><input type="hidden" id="popup_discipline_code_id"><input type="text" class="full popupinput" id="popup_discipline" disabled></td>
                                    <th class="required">
                                        Designer <i>required</i>
                                    </th>
                                    <td colspan="2"><input type="hidden" id="popup_designer_id"><input type="text" class="popupinput" id="popup_designer" disabled><button class="button btn_search ml5" onclick="designerSearchOpen()">검색</button></td>
                                </tr>
                                <tr>
                                    <th>WBS Code</th>
                                    <td colspan="2">
                                        <div class="multi_sel_wrap ml5" id="wbsCodeSelectWrap">
                                        	<button class="multi_sel" id="wbsCodeSelected">SELECT</button>
                                        	<input type="hidden" id="selectedWbsCodeId">
                                        	<div class="multi_box">
                                            	<table id="wbsCodeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">
                                        PRE.DESIGNER <i>required</i>
                                    </th>
                                    <td colspan="2"><input type="hidden" id="popup_pre_designer_id"><input type="text" class="full popupinput" id="popup_pre_designer" disabled></td>
                                </tr>
                                <tr>
                                    <th>WBS Detail</th>
                                    <td colspan="5">
                                        <input type="text" style="width:20%;" id="popup_wbs_detail_1" disabled>
                                        <input type="text" style="width:75%;" id="popup_wbs_detail_2" disabled>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Remark</th>
                                    <td colspan="5">
                                        <input type="text" class="full" class="popupinput" id="popup_remark">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">Category <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="docCategorySelectWrap">
                                        	<button class="multi_sel" id="docCategorySelected">SELECT</button>
                                        	<input type="hidden" id="selectedDocCateogrySetCodeId">
                                        	<div class="multi_box">
                                            	<table id="docCategoryMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">Size <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="docSizeSelectWrap">
                                        	<button class="multi_sel" id="docSizeSelected">SELECT</button>
                                        	<input type="hidden" id="selectedDocSizeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="docSizeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">Doc.Type <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="docTypeSelectWrap">
                                        	<button class="multi_sel" id="docTypeSelected">SELECT</button>
                                        	<input type="hidden" id="selectedDocTypeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="docTypeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <h4>Document Progress</h4>
                        <div class="tbl_wrap" id="docProgress">
                        </div>
                        <h4>Transmittal History</h4>
                        <div class="tbl_wrap" id="transHis">
                        </div>
                        <h4>DRN History</h4>
                        <p class="step" id="engDrnHistory">
                            <!-- <span>2021-08-13 결재 상신</span>
                            <span>2021-08-13 결재 상신</span>
                            <span>2021-08-13 결재 상신</span>
                            <span>2021-08-13 결재 상신</span> -->
                        </p>
                    </div>
                    </section>
                </div>
            </div>
        </section>
    </div>
</div>