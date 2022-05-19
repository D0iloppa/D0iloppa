<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="dialog pop_siteDocNew" title="Site Document" style="max-width:1200px;">
    <div class="pop_box">
        <section ng-app="angularResizable2" id="docCtrlNew_left">
            <div class="content">
                <div class="row" resizable r-flex="true">
                    <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                        <h4>Document List</h4>
                        <div class="btn_wrap">
                            <button class="button btn_blue" onclick="popupRefreshButton('SDCI')" id="siteRefreshButton">Refresh</button>
                            <button class="button btn_blue" onclick="siteNewButton()" id="siteNewButton">New</button>
                        </div>
                        <p class="info mt10 mb5" id="popup_sdci_current_folder_path">
                        </p>
                        <div class="mb5 allVersionCheckDiv">
                            <input type="checkbox" id="popup_site_rev_version" onchange="getPopupPrjDocumentIndexList('SDCI')">
                            <label for="">All Version</label>
                        </div>
                        <div class="tbl_wrap icon_tbl" id="siteDocNoTbl">
                        </div>
                    </section>
                    <section id="pop_ed_right">
                    <div id = "pdfDivSite">
                        <div class="pg_top">
                             <div class="left">
                                    <button class="button btn_orange popupDrnUp">
                                        DRN 상신
                                    </button>
                                    <button class="button btn_orange" id="site_revise" onclick="updateRevisionSite()" disabled>
                                        Revise
                                    </button>
                                    <button class="button btn_green" onclick="drnHistoryOpen()">
                                        DRN History
                                    </button>
                                    <button class="button btn_green" onclick="getSiteAccessHisList()">
                                        Access History
                                    </button>
                                    <button class="button btn_orange" id="site_save" onclick="siteDocSave()">
                                        Save
                                    </button>
                                    <button class="button btn_orange" onclick="docDelete('SDCI')">
                                        Delete
                                    </button>
                                </div>
                            <div class="right">
                                <button id="savePdf" class="btn_icon btn_blue" onclick="pageprint('SDCI')"><i class="fas fa-print"></i><span>Print</span></button>
                                <button class="btn_icon btn_blue" onclick="sitePopupClose()"><i class="fas fa-times"></i><span>Close</span></button>
                            </div>
                        </div>
                        <h4>SITE DOC. Process <i id="site_locker" class="popup_locker"></i><i id="site_oldversion" class="popup_oldversion"></i></h4>
                        <div class="tbl_wrap" style="display:inline;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        Doc. No <i>required</i>
                                    </th>
                                    <td><input type="text" class="full popupinput" id="site_doc_no"></td>
                                    <th>Rev. No</th>
                                    <td>
                                    	<input type="hidden" id="site_rev_max">
                                        <div class="multi_sel_wrap ml5" id="siteRevNoSelectWrap">
                                        	<button class="multi_sel" id="siteRevNoSelected">SELECT</button>
                                        	<input type="hidden" id="siteSelectedRevNoSetCodeId">
                                        	<input type="hidden" id="site_origin_rev_code_id">
                                        	<div class="multi_box">
                                            	<table id="siteRevNoMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th>Old Doc. No</th>
                                    <td>
                                        <input type="text" class="full popupinput" id="site_old_doc_no" disabled>
                                    </td>
                                </tr>
                                <tr id="siteFileAttachTr">
                                    <th>File Attach</th>
                                    <td colspan="5">
                                        <form id="updateRevisionSiteForm" name="updateRevisionSiteForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
                                        <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="site_popup_attachfilename" disabled="disabled">
					                        <label for="site_attach_filename" class="button btn_blue" id="site_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="site_attach_filename" class="upload-hidden siteDocumentFileAttach">
					                    </span>
					                    <input type="text" id="site_uploadFileInfo" disabled><input type="text" class="ml5" id="site_uploadFileDate" disabled>
										</form>
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">
                                        Title <i>required</i>
                                    </th>
                                    <td colspan="5"><input type="text" class="full popupinput" id="site_title"></td>
                                </tr>
                                <tr>
                                    <th>Creation</th>
                                    <td colspan="2"><input type="text" class="full popupinput" id="site_creation" disabled></td>
                                    <th>Modification</th>
                                    <td colspan="2"><input type="text" class="full popupinput" id="site_modification" disabled></td>
                                </tr>
                                <tr>
                                    <th>Discipline</th>
                                    <td colspan="2"><input type="hidden" id="site_discipline_code_id"><input type="text" class="full popupinput" id="site_discipline" disabled></td>
                                    <th class="required">
                                        Designer <i>required</i>
                                    </th>
                                    <td colspan="2"><input type="hidden" id="site_designer_id"><input type="text" class="popupinput" id="site_designer" disabled><button class="button btn_search ml5" onclick="designerSearchOpen()">검색</button></td>
                                </tr>
                                <tr>
                                    <th>WBS Code</th>
                                    <td colspan="2">
                                        <div class="multi_sel_wrap ml5" id="siteWbsCodeSelectWrap">
                                        	<button class="multi_sel" id="siteWbsCodeSelected">SELECT</button>
                                        	<input type="hidden" id="siteSelectedWbsCodeId">
                                        	<div class="multi_box">
                                            	<table id="siteWbsCodeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">
                                        PRE.DESIGNER <i>required</i>
                                    </th>
                                    <td colspan="2"><input type="hidden" id="site_pre_designer_id"><input type="text" class="full popupinput" id="site_pre_designer" disabled></td>
                                </tr>
                                <tr>
                                    <th>WBS Detail</th>
                                    <td colspan="5">
                                        <input type="text" style="width:20%;" id="site_wbs_detail_1" disabled>
                                        <input type="text" style="width:75%;" id="site_wbs_detail_2" disabled>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Remark</th>
                                    <td colspan="5">
                                        <input type="text" class="full" class="popupinput" id="site_remark">
                                    </td>
                                </tr>
                                <tr>
                                    <th class="required">Category <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="siteDocCategorySelectWrap">
                                        	<button class="multi_sel" id="siteDocCategorySelected">SELECT</button>
                                        	<input type="hidden" id="siteSelectedDocCateogrySetCodeId">
                                        	<div class="multi_box">
                                            	<table id="siteDocCategoryMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">Size <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="siteDocSizeSelectWrap">
                                        	<button class="multi_sel" id="siteDocSizeSelected">SELECT</button>
                                        	<input type="hidden" id="siteSelectedDocSizeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="siteDocSizeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                    <th class="required">Doc.Type <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap ml5" id="siteDocTypeSelectWrap">
                                        	<button class="multi_sel" id="siteDocTypeSelected">SELECT</button>
                                        	<input type="hidden" id="siteSelectedDocTypeSetCodeId">
                                        	<div class="multi_box">
                                            	<table id="siteDocTypeMultiBox">
                                            	</table>
                                       		</div>
                                    	</div>  
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <h4>Document Progress</h4>
                        <div class="tbl_wrap" id="siteDocProgress">
                        </div>
                        <h4>Transmittal History</h4>
                        <div class="tbl_wrap" id="siteTransHis">
                        </div>
                </div>
                    </section>
                </div>
            </div>
        </section>

    </div>
</div>