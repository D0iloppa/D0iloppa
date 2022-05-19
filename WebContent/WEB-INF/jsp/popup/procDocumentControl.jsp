<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="dialog pop_prcmCtrlNew" title="Procurement Document" style="max-width:1200px;">
    <div class="pop_box">
        <section ng-app="angularResizable3" id="venderDocNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton('PCI')">Refresh</button>
                            <button class="button btn_blue" onclick="procNewButton()" id="procNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_pci_current_folder_path">
                        </p>
                        <div class="mb5">
                            <input type="checkbox" id="popup_proc_rev_version" onchange="getPopupPrjDocumentIndexList('PCI')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="procDocNoTbl">
                        </div>
                    </section>
                    <section id="pop_ed_right">
                    <div id = "pdfDivProc">
                        <div class="pg_top">
                             <div class="left">
                                    <button class="button btn_orange" id="proc_revise" onclick="updateRevisionProc()" disabled>
                                        Revise
                                    </button>
                                    <button class="button btn_green popupDrnUp" onclick="drnHistoryOpen()">
                                        DRN History
                                    </button>
                                    <button class="button btn_green" onclick="getProcAccessHisList()">
                                        Access History
                                    </button>
                                    <button class="button btn_orange" id="proc_save" onclick="procDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" onclick="docDelete('PCI')">
                                        Delete
                                    </button>
                                </div>
                            <div class="right">
                                <button id="savePdf" class="btn_icon btn_blue" onclick="pageprint('PCI')"><i class="fas fa-print"></i><span>Print</span></button>
                                <button class="btn_icon btn_blue" onclick="procPopupClose()"><i class="fas fa-times"></i><span>Close</span></button>
                            </div>
                        </div>
                        <h4>PROCUREMENT PROCESS <i id="proc_locker" class="popup_locker"></i><i id="proc_oldversion" class="popup_oldversion"></i></h4>
                        <div class="tbl_wrap" style="display:inline;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        Doc. No <i>required</i>
                                    </th>
                                    <td><input type="text" class="full popupinput" id="proc_doc_no"></td>
                                    <th>Rev. No</th>
                                    <td>
                                    	<input type="hidden" id="proc_rev_max">
                                        <div class="multi_sel_wrap ml5" id="procRevNoSelectWrap">
                                        	<button class="multi_sel" id="procRevNoSelected">SELECT</button>
                                        	<input type="hidden" id="procSelectedRevNoSetCodeId">
                                        	<input type="hidden" id="proc_origin_rev_code_id">
                                        	<div class="multi_box">
                                            	<table id="procRevNoMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>
                                    </td>
                                </tr>
                                <tr id="procFileAttachTr">
                                    <th>File Attach</th>
                                    <td colspan="3">
                                        <!--  <button class="button btn_blue">Attach</button> -->
										<form id="updateRevisionProcForm" name="updateRevisionProcForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					                    <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="proc_attachfilename" disabled="disabled">
					                        <label for="proc_attach_filename" class="button btn_blue" id="proc_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="proc_attach_filename" class="upload-hidden procDocumentFileAttach">
					                    </span>
					                    <input type="text" id="proc_uploadFileInfo" disabled><input type="text" class="ml5" id="proc_uploadFileDate" disabled>
                                        </form>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Title <i>required</i>
                                    </th>
                                    <td colspan="3"><input type="text" class="full popupinput" id="proc_title"></td>
                                </tr>
                                <tr>
                                    <th>Creation</th>
                                    <td><input type="text" class="full popupinput" id="proc_creation" disabled></td>
                                    <th>Modification</th>
                                    <td><input type="text" class="full popupinput" id="proc_modification" disabled></td>
                                </tr>
                                <tr>
                                    <th>Discipline</th>
                                    <td><input type="hidden" id="proc_discipline_code_id"><input type="text" class="full popupinput" id="proc_discipline" disabled></td>
                                    <th class="required">
                                        Designer <i>required</i>
                                    </th>
                                    <td><input type="hidden" id="proc_designer_id"><input type="text" class="popupinput" id="proc_designer" disabled><button class="button btn_search ml5" onclick="designerSearchOpen()">검색</button></td>
                                </tr>
                                <tr>
                                    <th>WBS Code</th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="procWbsCodeSelectWrap">
                                        	<button class="multi_sel" id="procWbsCodeSelected">SELECT</button>
                                        	<input type="hidden" id="procSelectedWbsCodeId">
                                        	<div class="multi_box">
                                            	<table id="procWbsCodeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">
                                        PRE.DESIGNER <i>required</i>
                                    </th>
                                    <td><input type="hidden" id="proc_pre_designer_id"><input type="text" class="full popupinput" id="proc_pre_designer" disabled></td>
                                </tr>
                                <tr>
                                    <th>WBS Detail</th>
                                    <td colspan="3">
                                        <input type="text" style="width:20%;" id="proc_wbs_detail_1" disabled>
                                        <input type="text" style="width:75%;" id="proc_wbs_detail_2" disabled>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Remark</th>
                                    <td colspan="3">
                                        <input type="text" class="full" class="popupinput" id="proc_remark">
                                    </td>
                                </tr>
                                <tr>
                                	<th>POR NO</th>
                                	<td>
                                		<select name="" id="proc_por_no">
                                			<option value="">select</option>
                                		</select>
                                	</td>
                                	<th>PO NO</th>
                                	<td><input type="text" class="full" id="proc_po_no"></td>
                                </tr>
                                <tr>
                                	<th>POR ISSUE DATE</th>
                                	<td>
                                		<input type="text" class="date" id="proc_por_issue_date">
                                	</td>
                                	<th>PO ISSUE DATE</th>
                                	<td><input type="text" class="full" id="proc_po_issue_date"></td>
                                </tr>
                                <tr>
                                	<th>VENDOR</th>
                                	<td>
                                		<input type="text" class="full" id="proc_vendor_nm">
                                	</td>
                                	<th>VENDOR DOC. NO</th>
                                	<td><input type="text" class="full" id="proc_vendor_doc_no"></td>
                                </tr>
                            </table>
                        </div>
                        <h4>Document Progress</h4>
                        <div class="tbl_wrap" id="procDocProgress">
                        </div>
                </div>
                    </section>
                </div>
            </div>
        </section>

    </div>
</div>
