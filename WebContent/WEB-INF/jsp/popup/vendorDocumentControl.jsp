<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="dialog pop_vendorDocNew" title="VENDOR Document" style="max-width:1200px;">
    <div class="pop_box">
        <section ng-app="angularResizable2" id="docCtrlNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton('VDCI')" id="vendorRefreshButton">Refresh</button>
                            <button class="button btn_blue" onclick="vendorNewButton()" id="vendorNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_vdci_current_folder_path">
                        </p>
                        <div class="mb5 allVersionCheckDiv">
                            <input type="checkbox" id="popup_vendor_rev_version" onchange="getPopupPrjDocumentIndexList('VDCI')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="vendorDocNoTbl">
                        </div>
                    </section>
                    <section id="pop_ed_right">
                    <div id = "pdfDivVendor">
                        <div class="pg_top">
                             <div class="left">
                                    <button class="button btn_orange popupDrnUp">
                                        DRN 상신
                                    </button>
                                    <button class="button btn_orange" id="vendor_revise" onclick="updateRevisionVendor()" disabled>
                                        Revise
                                    </button>
                                    <button class="button btn_green" onclick="drnHistoryOpen()">
                                        DRN History
                                    </button>
                                    <button class="button btn_green" onclick="getVendorAccessHisList()">
                                        Access History
                                    </button>
                                    <button class="button btn_orange" id="vendor_save" onclick="vendorDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" onclick="docDelete('VDCI')">
                                        Delete
                                    </button>
                                </div>
                            <div class="right">
                                <button id="savePdf" class="btn_icon btn_blue" onclick="pageprint('VDCI')"><i class="fas fa-print"></i><span>Print</span></button>
                                <button class="btn_icon btn_blue" onclick="vendorPopupClose()"><i class="fas fa-times"></i><span>Close</span></button>
                            </div>
                        </div>
                        <h4>Vendor DOC. PROCESS <i id="vendor_locker" class="popup_locker"></i><i id="vendor_oldversion" class="popup_oldversion"></i></h4>
                        <div class="tbl_wrap" style="display:inline;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        Doc. No <i>required</i>
                                    </th>
                                    <td><input type="text" class="full popupinput" id="vendor_doc_no"></td>
                                    <th>Rev. No</th>
                                    <td>
                                    	<input type="hidden" id="vendor_rev_max">
                                        <div class="multi_sel_wrap ml5" id="vendorRevNoSelectWrap">
                                        	<button class="multi_sel" id="vendorRevNoSelected">SELECT</button>
                                        	<input type="hidden" id="vendorSelectedRevNoSetCodeId">
                                        	<input type="hidden" id="vendor_origin_rev_code_id">
                                        	<div class="multi_box">
                                            	<table id="vendorRevNoMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th>Old Doc. No</th>
                                    <td>
                                        <input type="text" class="full popupinput" id="vendor_old_doc_no" disabled>
                                    </td>
                                </tr>
                                <tr id="vendorFileAttachTr">
                                    <th>File Attach</th>
                                    <td colspan="5">
                                        <!--  <button class="button btn_blue">Attach</button> -->
										<form id="updateRevisionVendorForm" name="updateRevisionVendorForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					                    <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="vendor_attachfilename" disabled="disabled">
					                        <label for="vendor_attach_filename" class="button btn_blue" id="vendor_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="vendor_attach_filename" class="upload-hidden vendorDocumentFileAttach">
					                    </span>
					                    <input type="text" id="vendor_uploadFileInfo" disabled><input type="text" class="ml5" id="vendor_uploadFileDate" disabled>
                                        </form>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Title <i>required</i>
                                    </th>
                                    <td colspan="5"><input type="text" class="full popupinput" id="vendor_title"></td>
                                </tr>
                                <tr>
                                    <th>Creation</th>
                                    <td colspan="2"><input type="text" class="full popupinput" id="vendor_creation" disabled></td>
                                    <th>Modification</th>
                                    <td colspan="2"><input type="text" class="full popupinput" id="vendor_modification" disabled></td>
                                </tr>
                                <tr>
                                    <th>Discipline</th>
                                    <td colspan="2"><input type="hidden" id="vendor_discipline_code_id"><input type="text" class="full popupinput" id="vendor_discipline" disabled></td>
                                    <th class="required">
                                        Designer <i>required</i>
                                    </th>
                                    <td colspan="2"><input type="hidden" id="vendor_designer_id"><input type="text" class="popupinput" id="vendor_designer" disabled><button class="button btn_search ml5" onclick="designerSearchOpen()">검색</button></td>
                                </tr>
                                <tr>
                                    <th>WBS Code</th>
                                    <td colspan="2">
                                        <div class="multi_sel_wrap ml5" id="vendorWbsCodeSelectWrap">
                                        	<button class="multi_sel" id="vendorWbsCodeSelected">SELECT</button>
                                        	<input type="hidden" id="vendorSelectedWbsCodeId">
                                        	<div class="multi_box">
                                            	<table id="vendorWbsCodeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">
                                        PRE.DESIGNER <i>required</i>
                                    </th>
                                    <td colspan="2"><input type="hidden" id="vendor_pre_designer_id"><input type="text" class="full popupinput" id="vendor_pre_designer" disabled></td>
                                </tr>
                                <tr>
                                    <th>WBS Detail</th>
                                    <td colspan="5">
                                        <input type="text" style="width:20%;" id="vendor_wbs_detail_1" disabled>
                                        <input type="text" style="width:75%;" id="vendor_wbs_detail_2" disabled>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Remark</th>
                                    <td colspan="5">
                                        <input type="text" class="full" class="popupinput" id="vendor_remark">
                                    </td>
                                </tr>
                                <tr>
                                	<th>PROCUREMENT DOC. No</th>
                                	<td colspan="2"> 
                                		<select name="" id="vendor_po_doc_no"></select>
                                	</td>
                                	<th>DOC. TITLE</th>
                                	<td colspan="2"><input type="text" id="vendor_po_doc_title"></td>
                                </tr>
                                <tr>
                                	<th>VENDOR</th>
                                	<td colspan="2">
                                		<select name="" id="vendor_vendor_nm"></select>
                                	</td>
                                	<th>DESIGN PART</th>
                                	<td colspan="2"><input type="text" id="vendor_design_part"></td>
                                </tr>
                                <tr>
                                    <th class="required">Category <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="vendorDocCategorySelectWrap">
                                        	<button class="multi_sel" id="vendorDocCategorySelected">SELECT</button>
                                        	<input type="hidden" id="vendorSelectedDocCateogrySetCodeId">
                                        	<div class="multi_box">
                                            	<table id="vendorDocCategoryMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">Size <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="vendorDocSizeSelectWrap">
                                        	<button class="multi_sel" id="vendorDocSizeSelected">SELECT</button>
                                        	<input type="hidden" id="vendorSelectedDocSizeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="vendorDocSizeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">Doc.Type <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="vendorDocTypeSelectWrap">
                                        	<button class="multi_sel" id="vendorDocTypeSelected">SELECT</button>
                                        	<input type="hidden" id="vendorSelectedDocTypeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="vendorDocTypeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <h4>Document Progress</h4>
                        <div class="tbl_wrap" id="vendorDocProgress">
                        </div>
                        <h4>Transmittal History</h4>
                        <div class="tbl_wrap" id="vendorTransHis">
                        </div>
                    </div>
                    </section>
                </div>
            </div>
        </section>

    </div>
</div>