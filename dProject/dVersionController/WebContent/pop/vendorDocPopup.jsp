<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ include file="./popupHeader.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd hh:mm:ss" var="curentTime"/>
<input type="hidden" id="currentTime"  value="${curentTime}">
<div class="DocPopup">
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
                        <div class="rg-rightBack"></div>
                    </section>
                    <section id="pop_ed_right">
                    <div id = "pdfDivVendor">
                        <div class="pg_top">
                             <div class="left">
                                    <button class="button btn_orange popupDrnUp" id="vendorDrnUp" onclick="drnUpBtn()">
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
                                    <button class="button btn_orange" id="vendor_delete" onclick="docDelete('VDCI')">
                                        Delete
                                    </button>
                                </div>
                            <div class="right">
                                <button id="savePdf" class="btn_icon btn_blue" onclick="pageprint('VDCI')"><i class="fas fa-print"></i><span>Print</span></button>
                                <!-- <button class="btn_icon btn_blue" onclick="vendorPopupClose()"><i class="fas fa-times"></i><span>Close</span></button> -->
                            </div>
                        </div>
                        <h4>Vendor DOC. PROCESS <i id="vendor_locker" class="popup_locker"></i><i id="vendor_oldversion" class="popup_oldversion"></i></h4>
                        <div class="tbl_wrap" style="overflow-x:visible;">
                            <table class="write">
                                <tr>
                                    <th class="required">
                                        Doc. No <i>required</i>
                                    </th>
                                    <td><input type="text" class="full popupinput" id="vendor_doc_no"></td>
                                    <th>Rev. No</th>
                                    <td>
                                    	<input type="hidden" id="vendor_rev_max">
                                        <div class="multi_sel_wrap" id="vendorRevNoSelectWrap">
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
										<form id="updateRevisionVendorForm" style="display:flex;" name="updateRevisionVendorForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					                    	<span id="rfile_download" class="property_attach" onclick="propertydownload()"></span>
					                        <span class="filebox">
					                        	<input type="text" id="vendor_uploadFileDate" disabled style="width: 190px;">
					                     	   	<input type="file" name="updateRevision_File" id="vendor_attach_filename" class="upload-hidden vendorDocumentFileAttach">
						                        <label for="vendor_attach_filename" class="button btn_blue" id="vendor_popup_attach">Attach</label>
					                    	</span>
					                    <!-- <span class="filebox">
					                        <input class="upload-name" value="파일선택" id="vendor_attachfilename" disabled="disabled">
					                        <label for="vendor_attach_filename" class="button btn_blue" id="vendor_popup_attach">Attach</label>
					                        <input type="file" name="updateRevision_File" id="vendor_attach_filename" class="upload-hidden vendorDocumentFileAttach">
					                    </span>
					                    <input type="text" id="vendor_uploadFileInfo" disabled><input type="text" class="ml5" id="vendor_uploadFileDate" disabled>
                                        <span id="rfile_download" onclick="propertydownload()" style="cursor: pointer;color:blue;text-decoration:underline;"></span> -->
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
                                	<td colspan="2">
										<select id="vendor_designer_id" class="full" onChange="desginerChange()"></select>
					                </td>
								</tr>
                                <tr>
                                    <th>WBS Code</th>
                                    <td colspan="2">
                                        <div class="multi_sel_wrap" id="vendorWbsCodeSelectWrap">
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
                                    	<textarea class="full" rows="30" id="vendor_remark"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                	<th>PROCUREMENT DOC. No</th>
                                	<td colspan="2"> 
                                		<!-- <select name="" id="vendor_po_doc_no"></select> -->
                                        <div class="multi_sel_wrap" id="vendorProcDocSelectWrap">
                                        	<button class="multi_sel" id="vendorProcDocSelected">SELECT</button>
                                        	<input type="hidden" id="vendor_po_doc_no">
                                        	<div class="multi_box">
                                            	<table id="vendorProcDocMultiBox">
                                            	</table>
                                       		</div>
                                    	</div> 
                                	</td>
                                	<th>DOC. TITLE</th>
                                	<td colspan="2"><input type="text" id="vendor_po_doc_title"></td>
                                </tr>
                                <tr>
                                	<th>VENDOR</th>
                                	<td colspan="2">
                                		<input type="text" id="vendor_vendor_nm">
                                	</td>
                                	<th>DESIGN PART</th>
                                	<td colspan="2"><input type="text" id="vendor_design_part"></td>
                                </tr>
                                <tr>
                                    <th class="required">Category <i>required</i></th>
                                    <td>
                                        <div class="multi_sel_wrap" id="vendorDocCategorySelectWrap">
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
                                        <div class="multi_sel_wrap" id="vendorDocSizeSelectWrap">
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
                                        <div class="multi_sel_wrap" id="vendorDocTypeSelectWrap">
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
                        <h4>DRN History</h4>
                        <p class="step" id="vendorDrnHistory">
                        </p>
                    </div>
                    </section>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="dialog pop_drnHis" title="DRN History" style="max-width:1000px">
    <div class="pop_box">
        <div class="tbl_wrap" id="drnHis_tbl">
            
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
<input type="hidden" id="current_folder_id">
<script>
var process_type = 'VDCI';
var documentListTable;
var thisTable;
var StepTable;
var vendorDocumentListTable;
var searchDesignerTable;
var DCVendorStepTable;
var DC = {};
var vendor_new_or_update = 'new';
var vendor_selected_doc_id = '';
var vendor_selected_rev_id = '';
var trINOUT_discipline_display = '';

var doc_type = opener.document.getElementById('doc_type').value;
var current_folder_discipline_code = opener.document.getElementById('current_folder_discipline_code').value;
var current_folder_discipline_code_id = opener.document.getElementById('current_folder_discipline_code_id').value;
var current_folder_discipline_display = opener.document.getElementById('current_folder_discipline_display').value;
var current_folder_id = opener.document.getElementById('current_folder_id').value;
var DCCorTRA = opener.document.getElementById('DCCorTRA').value;
var session_user_id = opener.document.getElementById('session_user_id').value;
var session_user_eng_nm = opener.document.getElementById('session_user_eng_nm').value;
var session_user_kor_nm = opener.document.getElementById('session_user_kor_nm').value;
var selectPrjId = opener.document.getElementById('selectPrjId').value;
var vendor_new_or_update = opener.document.getElementById('vendor_new_or_update').value;
var current_folder_path = opener.document.getElementById('current_folder_path').value;
var useDRNYN = opener.document.getElementById('useDRNYN').value;
var tr_id = opener.document.getElementById('tr_id').value;
var tradd_attach = opener.document.getElementById('tradd_attach').value;
var tradd_doc_no = opener.document.getElementById('tradd_doc_no').value;
var tradd_rev_no = opener.document.getElementById('tradd_rev_no').value;
var tradd_title = opener.document.getElementById('tradd_title').value;
$(function(){
	$('#current_folder_id').val(current_folder_id);
	dialogInit();
	getPrjMemeberListForDesigner();
	getProcListMultiBox();

	auth = checkFolderAuth(selectPrjId,current_folder_id);
	if(auth.w==='Y'){
		if(useDRNYN==='Y'){
			$('#vendorDrnUp').css('display','inline-block');
		}else{
			$('#vendorDrnUp').css('display','none');
		}
		$('#vendor_revise').css('display','inline-block');
		$('#vendor_save').css('display','inline-block');
		$('#vendorNewButton').css('display','inline-block');
	}else{
		$('#vendorDrnUp').css('display','none');
		$('#vendor_revise').css('display','none');
		$('#vendor_save').css('display','none');
		$('#vendorNewButton').css('display','none');
	}
	if(auth.d==='Y'){
		$('#vendor_delete').css('display','inline-block');
	}else{
		$('#vendor_delete').css('display','none');
	}

	//Vendor 파일첨부
    var fileTarget = $('.filebox .vendorDocumentFileAttach');

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
        $('#vendor_uploadFileDate').val(formatBytes(size,2)+', '+today.toLocaleString());
        if($('#vendor_locker').html()!=='' && $('#vendor_rev_max').val()===vendor_selected_rev_id && $('#vendorSelectedRevNoSetCodeId').val()!=='' && $('#vendor_origin_rev_code_id').val()!==$('#vendorSelectedRevNoSetCodeId').val()){
        	$('#vendor_revise').attr('disabled',false);
        }
    });
    
	var process = opener.document.getElementById('process').value;
	if(process==='new'){
    	$('#popup_vdci_current_folder_path').html(current_folder_path);
    	$('#vendorRevNoSelected').css('pointer-events', 'none');
    	$('#vendorRevNoSelected').css('background', 'none');
    	$('#vendorRevNoSelected').css('background-color', '#FAFAFA');
    	$('#vendorRevNoSelected').css('color', '#FAFAFA');
    	getRevNoMultiBox('VDCI');
    	getWbsCodeMultiBox('VDCI');
    	getVendorDocCategoryMultiBox();
    	getVendorDocSizeMultiBox();
    	getDocTypeMultiBox('VDCI');
        getPopupPrjDocumentIndexList('VDCI');
        getPrjStep('','','VDCI','new');
        getVendorPrjTrHistoryNew();
        setPrjDrnHistoryNew();
        setVendorNull();
	}else if(process==='property'){
		var doc_id = opener.document.getElementById('doc_id').value;
		var rev_id = opener.document.getElementById('rev_id').value;
		vendor_selected_doc_id = doc_id;
		vendor_selected_rev_id = rev_id;
		let trans_ref_doc_id = opener.document.getElementById('trans_ref_doc_id').value;
    	$('#popup_vdci_current_folder_path').html(current_folder_path);
    	$('#vendorRevNoSelected').css('pointer-events', 'auto');
    	$('#vendorRevNoSelected').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
    	$('#vendorRevNoSelected').css('background-color', '#fff');
    	$('#vendorRevNoSelected').css('color', '#000');
    	getRevNoMultiBox('VDCI',doc_id);
    	getWbsCodeMultiBox('VDCI');
    	getVendorDocCategoryMultiBox();
    	getVendorDocSizeMultiBox();
    	getDocTypeMultiBox('VDCI');
        getPopupPrjDocumentIndexList('VDCI','D');
        if(DCCorTRA==='DCC'){
	        getPrjStep(doc_id,rev_id,'VDCI');
	    	getVendorDocumentInfo(doc_id,rev_id);
	        getPrjTrHistory(doc_id,rev_id,'VDCI');
        }else if(DCCorTRA==='TRA'){
            getPrjStep(trans_ref_doc_id,rev_id,'VDCI');
            getVendorDocumentInfo(trans_ref_doc_id,rev_id);
            getPrjTrHistory(trans_ref_doc_id,rev_id,'VDCI');
        }

		if(tr_id!==''){
			$('.allVersionCheckDiv').css('display','none');
			if(doc_type==='DCI'){
		    	$('#engRefreshButton').css('display','none');
				$('#engNewButton').css('display','none');
				$('#popup_dci_current_folder_path').html('');
			}else if(doc_type==='VDCI'){
		    	$('#vendorRefreshButton').css('display','none');
				$('#vendorNewButton').css('display','none');
				$('#popup_vdci_current_folder_path').html('');
			}else if(doc_type==='SDCI'){
		    	$('#siteRefreshButton').css('display','none');
				$('#siteNewButton').css('display','none');
				$('#popup_sdci_current_folder_path').html('');
			}
			if(tr_id==='TRADD'){
				let trDocData = [];
				trDocData.push({
	    			imgIcon: tradd_attach,
	                docNo: tradd_doc_no,
	                revNo: tradd_rev_no,
	                title: tradd_title
	    		});
				setPopupPrjDocumentIndexList(trDocData,doc_type);
			}else{
				if(doc_type==='DCI'){
					outProcessType='TR';
				}else if(doc_type==='VDCI'){
					outProcessType='VTR';
				}else if(doc_type==='SDCI'){
					outProcessType='STR';
				}
				$.ajax({
				    url: '../getTrInfo.do',
				    type: 'POST',
				    data:{
				    	prj_id:selectPrjId,
				    	tr_id:tr_id,
				    	processType:outProcessType
				    },
				    async:false,
				    success: function onData (data) {
						if(doc_type==='DCI'){
							$('#popup_dci_current_folder_path').html('TR: '+data[0].tr_no);
						}else if(doc_type==='VDCI'){
							$('#popup_vdci_current_folder_path').html('VTR: '+data[0].tr_no);
						}else if(doc_type==='SDCI'){
							$('#popup_sdci_current_folder_path').html('STR: '+data[0].tr_no);
						}
						
						let trDocData = [];
				    	for(i=0;i<data[1].length;i++){
				    		var rev_code_id = data[1][i].rev_code_id;
				    		var lock_yn = data[1][i].lock_yn=='Y'?'Y':'N';
				    		var mod_id = data[1][i].mod_id;
				    		var ext = data[1][i].file_type;
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
							trDocData.push({
				    			imgIcon: imgIcon,
			                    docNo: data[1][i].doc_no,
			                    revNo: data[1][i].rev_no,
			                    title: data[1][i].title,
			                    bean: data[1][i]
				    		});
				    	}
						setPopupPrjDocumentIndexList(trDocData,doc_type);
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
			}
		}
	}
});

function drnUpBtn(){
	
	let selected = thisTable.getSelectedData();
	if(selected.length == 0) return;
	
	let authChk = opener.checkFolderAuth(selectPrjId,Number(current_folder_id));
	
	if(authChk.w == 'N'){
		alert("There is no Write permission");
		return;
	}
	
	
	
	if(current_folder_discipline_code.length == 0){
		alert("Can't submit this document to DRN");
		return;
	}
	
	// 잠기지 않은 doc만 넘겨준다.
	selected = selected.filter( (element) => element.bean.lock_yn !== 'Y' );
	
	if(selected.length == 0) {
		alert("Can't submit locked document to DRN");
		return;	
	}
	let validationChk = "";
	for(let i=0;i<selected.length;i++)
		validationChk += selected[i].bean.doc_id + "@" +selected[i].bean.rev_id + ",";
	
	validationChk = validationChk.substr(0,validationChk.length-1);
	
	
	let err = "";
	$.ajax({
	    url: '../drnValidationCheck.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:selectPrjId,
	    	doc_type:doc_type,
	    	jsonData:validationChk
	    },
	    success: function onData (data) {
	    	validationChk = data.model.check;
	    	if(data.model.check =="NO"){
	    		let tmp = data.model.error_doc
	    		let msg = data.model.error_msg
	    		err = msg;
	    	}
	    		
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	if(validationChk == "NO") {
		alert(err);
		return;
	}
	
	let discipObj = {};
	discipObj.code = current_folder_discipline_code;
	discipObj.display =current_folder_discipline_display;
	discipObj.code_id = current_folder_discipline_code_id;
	
	opener.drnPopUP(0,selected,discipObj,current_folder_id);
}
function getRevNoMultiBox(type,doc_id){
	$.ajax({
	    url: '../getRevNoMultiBox.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	doc_id:doc_id,
	    	set_code_type: 'REVISION NUMBER'
	    },
	    success: function onData (data) {
	    	if(doc_id){
		    	let usedRevCodeIdString = '';
		    	if(data[1]!=='최초등록'){
		    		usedRevCodeIdString = data[1];
		    	}
		    	usedRevCodeIdArray = usedRevCodeIdString.split(',');
	    	}
            var html = '<tr><th>RevNo</th><th>Desc.</th><th>revtype</th><th>seq</th></tr>';
            for(i=0;i<data[0].length;i++){
            	let used = false;
            	if(doc_id){
	            	for(let j=0;j<usedRevCodeIdArray.length;j++){
	                	if(data[0][i].set_code_id===usedRevCodeIdArray[j]){
	                		used = true;
	                	}
	            	}
            	}
            	if(used){
            		html += '<tr class="usedRevNo"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td><td>'+data[0][i].set_val+'</td><td>'+data[0][i].set_code_id+'</td></tr>';
                }else{
            		html += '<tr class="selectRevNo"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td><td>'+data[0][i].set_val+'</td><td>'+data[0][i].set_code_id+'</td></tr>';
                }
            }
        	

	    	$('#vendorRevNoMultiBox').html(html);
        	$('.usedRevNo').click(function(){
        		alert('이전에 사용했던 리비전은 다시 선택할 수 없습니다.');
        	});
	    	
	    	$('.selectRevNo').click(function(){
	    		let origin_rev_no = $('#vendorRevNoSelected').html();
	    		$('#vendorRevNoSelectWrap').removeClass('on');
	    		$('#vendorRevNoSelected').html($(this).children().html());
	    		$('#vendorSelectedRevNoSetCodeId').val($(this).children().attr('id'));
	    		if($('#vendor_origin_rev_code_id').val()!==$('#vendorSelectedRevNoSetCodeId').val()){
	    			let origin_rev_code_id = parseInt($('#vendor_origin_rev_code_id').val());
	    			let select_rev_code_id = parseInt($('#vendorSelectedRevNoSetCodeId').val());
	    			   if($('#vendor_locker').html()!==''){
			    	        $('#vendor_revise').attr('disabled',true);
		    	        	$('#vendor_save').attr('disabled',true);
		    	        }else{
			    	        $('#vendor_revise').attr('disabled',false);
		    	        	$('#vendor_save').attr('disabled',true);
		    	        }
	    		}else{
	    			if($('#vendor_locker').html()!==''){
	    				$('#vendor_save').attr('disabled',true);
	    			}else{
		    			$('#vendor_save').attr('disabled',false);
	    			}
	    	        $('#vendor_revise').attr('disabled',true);
	    		}
	    		
	            if($('#vendor_locker').html()==='' && $('#vendor_rev_max').val()===vendor_selected_rev_id && $('#vendorSelectedRevNoSetCodeId').val()!=='' && $('#vendor_origin_rev_code_id').val()!==$('#vendorSelectedRevNoSetCodeId').val()){
	            	$('#vendor_revise').attr('disabled',false);
	            }
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getWbsCodeMultiBox(type){
	$.ajax({
	    url: '../getWbsCodeList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	discip_code_id:current_folder_discipline_code_id
	    },
	    success: function onData (data) {
            var html = '<tr><th>WBS</th><th>DESCRIPTION</th><th>WGTVAL</th><th>DISCIPLINE</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectWbsCode"><td id="'+data[0][i].wbs_code_id+'">'+data[0][i].wbs_code+'</td><td>'+data[0][i].wbs_desc+'</td><td>'+data[0][i].wgtval+'</td><td>'+data[0][i].discip_display+'</td></tr>';
            }
	    	$('#wbsCodeMultiBox').html(html);
	    	$('.selectWbsCode').click(function(){
	    		$('#wbsCodeSelectWrap').removeClass('on');
	    		$('#wbsCodeSelected').html($(this).children().html());
	    		$('#selectedWbsCodeId').val($(this).children().attr('id'));
		    	$('#popup_wbs_detail_1').val($(this).children().next().next().html());
		    	$('#popup_wbs_detail_2').val($(this).children().next().html());
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getVendorDocCategoryMultiBox(type){
	$.ajax({
	    url: '../getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type: 'VENDOR DOC.CATEGORY'
	    },
	    success: function onData (data) {
            var html = '<tr><th>CODE</th><th>DESCRIPTION</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectDocCategory"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td></tr>';
            }
	    	$('#vendorDocCategoryMultiBox').html(html);
	    	
	    	$('.selectDocCategory').click(function(){
	    		$('#vendorDocCategorySelectWrap').removeClass('on');
	    		$('#vendorDocCategorySelected').html($(this).children().html());
	    		$('#vendorSelectedDocCateogrySetCodeId').val($(this).children().attr('id'));
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getVendorDocSizeMultiBox(){
	$.ajax({
	    url: '../getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type: 'VENDOR DOC.SIZE'
	    },
	    success: function onData (data) {
            var html = '<tr><th>SIZE</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectDocSize"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td></tr>';
            }
	    	$('#vendorDocSizeMultiBox').html(html);
	    	
	    	$('.selectDocSize').click(function(){
	    		$('#vendorDocSizeSelectWrap').removeClass('on');
	    		$('#vendorDocSizeSelected').html($(this).children().html());
	    		$('#vendorSelectedDocSizeSetCodeId').val($(this).children().attr('id'));
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getDocTypeMultiBox(type){
	$.ajax({
	    url: '../getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type: 'DOC.TYPE'
	    },
	    success: function onData (data) {
            var html = '<tr><th>SIZE</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectDocType"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td></tr>';
            }
            if(type==='DCI'){
		    	$('#docTypeMultiBox').html(html);
		    	
		    	$('.selectDocType').click(function(){
		    		$('#docTypeSelectWrap').removeClass('on');
		    		$('#docTypeSelected').html($(this).children().html());
		    		$('#selectedDocTypeSetCodeId').val($(this).children().attr('id'));
		    	});
            }else if(type==='VDCI'){
            	$('#vendorDocTypeMultiBox').html(html);
		    	
		    	$('.selectDocType').click(function(){
		    		$('#vendorDocTypeSelectWrap').removeClass('on');
		    		$('#vendorDocTypeSelected').html($(this).children().html());
		    		$('#vendorSelectedDocTypeSetCodeId').val($(this).children().attr('id'));
		    	});
            }else if(type==='SDCI'){
            	$('#siteDocTypeMultiBox').html(html);
		    	
		    	$('.selectDocType').click(function(){
		    		$('#siteDocTypeSelectWrap').removeClass('on');
		    		$('#siteDocTypeSelected').html($(this).children().html());
		    		$('#siteSelectedDocTypeSetCodeId').val($(this).children().attr('id'));
		    	});
            }
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function desginerChange(){
	let vendor_designer_id = $('#vendor_designer_id').text();
	if(vendor_new_or_update==='new'){
		$('#vendor_pre_designer_id').val($('#vendor_designer_id').val());
		$('#vendor_pre_designer').val(vendor_designer_id);
	}
}
function getPrjMemeberListForDesigner(){
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
	    	$('#vendor_designer_id').html(html);

	    	$("#vendor_designer_id").selectize({
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
function getPrjStep(doc_id,rev_id,type,stepNew){
	var set_code_type = 'VENDOR DOC STEP';
	
	 $.ajax({
			type: 'POST',
			url: '../getPrjStep.do',
			data: {
				type:type,
				prj_id:selectPrjId,
				set_code_type: set_code_type,
				doc_id:doc_id,
				rev_id:rev_id
			},
			async:false,
			success: function(data) {
				if(stepNew==='new'){
					prjStepNew(data,type);
				}else{
					setPrjStep(data,type);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
var dateEditor = function(cell, onRendered, success, cancel){
    //cell - the cell component for the editable cell
    //onRendered - function to call when the editor has been rendered
    //success - function to call to pass the successfuly updated value to Tabulator
    //cancel - function to call to abort the edit and return to a normal cell

    //create and style input
    var cellValue = cell.getValue();//moment(cell.getValue(), "YYYY-MM-DD").format("YYYY-MM-DD");
    input = document.createElement("input");

    input.setAttribute("type", "date");
    input.setAttribute("max", "9999-12-31");

    input.style.padding = "4px";
    input.style.width = "100%";
    input.style.boxSizing = "border-box";

    input.value = cellValue;

    onRendered(function(){
        input.focus();
        input.style.height = "100%";
    });

    function onChange(){
        if(input.value != cellValue){
            success(moment(input.value, "YYYY-MM-DD").format("YYYY-MM-DD"));
        	let rowDataObject = cell.getRow().getData();
        	var rowDataArray = [];
        	for (var key in rowDataObject) {
        		if(rowDataObject[key].trim()!=='--' && rowDataObject[key].trim()!=='Invalid date' && rowDataObject[key]!==undefined && rowDataObject[key]!==null && rowDataObject[key].trim()!=='')
        			rowDataArray.push(rowDataObject[key]);
        	}
        	rowDataArray.splice(0,1);//배열의 첫번째 요소 제거(plan date 등의 타이틀)
        	if(JSON.stringify(rowDataArray) !== JSON.stringify(rowDataArray.sort())){
        		cell.setValue(moment('', "YYYY-MM-DD").format("YYYY-MM-DD"),true);
        		alert('Check Date! It must be greater than the previous step date.');
        	}
        }else{
            cancel();
        }
    }

    //submit new value on blur or change
    //input.addEventListener("change", onChange); change 될 때마다 체크하면 값 입력못함
    input.addEventListener("blur", onChange);

    //submit new value on enter
    input.addEventListener("keydown", function(e){
        if(e.keyCode == 13){
            onChange();
        }

        if(e.keyCode == 27){
            cancel();
            cell.setValue('');
        }
    });

    return input;
};
var editCheck = function(cell){
	var editboolean = true;
	var data = cell.getRow().getData().notitle;
    if(data==='TR NO'){
    	editboolean = false;
    }
    return editboolean;
}
function setPrjStep(data,type){
	var defaultColumns = [];
	defaultColumns.push({title:"", field:"notitle", visible:true , headerSort:false});
	for(let i=0;i<data[0].length;i++)
		defaultColumns.push(
				{
					title:data[0][i].set_code+'('+data[0][i].set_val+'%)', 
					field:data[0][i].set_code_id,
					visible:true,
					hozAlign:"middle", 
					headerSort:false,
					editor:dateEditor,
					editable:editCheck
				}
		);

	var docProgressArray = [];
	for(i=0;i<4;i++){
		let object = {};
		for(j=0;j<data[1].length;j++){
			if(i===0){
				object['notitle']='Plan Date';
				object[data[1][j].step_code_id] = data[2][data[1][j].step_code_id]==undefined?'':to_date_format(data[2][data[1][j].step_code_id].plan_date,'-');
			}
			if(i===1){
				object['notitle']='Actual Date';
				object[data[1][j].step_code_id] = data[2][data[1][j].step_code_id]==undefined?'':to_date_format(data[2][data[1][j].step_code_id].actual_date,'-');
			}
			if(i===2){
				object['notitle']='Fore Date';
				object[data[1][j].step_code_id] = data[2][data[1][j].step_code_id]==undefined?'':to_date_format(data[2][data[1][j].step_code_id].fore_date,'-');
			}
			if(i===3){
				object['notitle']='TR NO';
				object[data[1][j].step_code_id] = data[2][data[1][j].step_code_id]==undefined?'':data[2][data[1][j].step_code_id].tr_no;
			}
		}
		docProgressArray.push(object);
	}

	StepTable = DCVendorStepTable;
	var tableId = '#vendorDocProgress';
	
	StepTable = new Tabulator(tableId, {
		  data:docProgressArray,
	      layout: "fitColumns",
	      height: 135,
	      columns: defaultColumns,
	      movableColumns: false
	});
}
function prjStepNew(data,type){
	var defaultColumns = [];
	defaultColumns.push({title:"", field:"notitle", visible:true , headerSort:false});
	for(let i=0;i<data[0].length;i++)
		defaultColumns.push(
				{
					title:data[0][i].set_code+'('+data[0][i].set_val+'%)', 
					field:data[0][i].set_code_id,
					visible:true,
					hozAlign:"middle", 
					headerSort:false,
					editor:dateEditor,
					editable:editCheck
				}
		);

	var docProgressArray = [];
	for(i=0;i<4;i++){
		let object = {};
		for(j=0;j<data[0].length;j++){
			if(i===0){
				object['notitle']='Plan Date';
				object[data[1][j].step_code_id] = '';
			}
			if(i===1){
				object['notitle']='Actual Date';
				object[data[1][j].step_code_id] = '';
			}
			if(i===2){
				object['notitle']='Fore Date';
				object[data[1][j].step_code_id] = '';
			}
			if(i===3){
				object['notitle']='TR NO';
				object[data[1][j].step_code_id] = '';
			}
		}
		docProgressArray.push(object);
	}

	StepTable = DCVendorStepTable;
	var tableId = '#vendorDocProgress';
	
	StepTable = new Tabulator(tableId, {
		  data:docProgressArray,
	      layout: "fitColumns",
	      height: 135,
	      columns: defaultColumns,
	      movableColumns: false
	});
}
function getProcListMultiBox(){
	$.ajax({
	    url: '../getProcList.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId
	    },
	    success: function onData (data) {
            var html = '<tr><th>DOC. NO</th><th>DOC. TITLE</th><th>VENDOR</th></tr>';
            for(i=0;i<data[0].length;i++){
            	let doc_no = data[0][i].doc_no;
            	let title = data[0][i].title;
            	let vendor_nm = data[0][i].vendor_nm;
            	if(doc_no===null || doc_no===undefined || doc_no===''){
            		doc_no = '';
            	}
            	if(title===null || title===undefined || title===''){
            		title = '';
            	}
            	if(vendor_nm===null || vendor_nm===undefined || vendor_nm===''){
            		vendor_nm = '';
            	}
            	html += '<tr class="selectProcDoc"><td id="'+doc_no+'">'+doc_no+'</td><td>'+title+'</td><td>'+vendor_nm+'</td></tr>';
            }
	    	$('#vendorProcDocMultiBox').html(html);
	    	$('.selectProcDoc').click(function(){
	    		$('#vendorProcDocSelectWrap').removeClass('on');
	    		$('#vendorProcDocSelected').html($(this).children().html());
	    		$('#vendor_po_doc_no').val($(this).children().attr('id'));
	    		$('#vendor_po_doc_title').val($(this).children().next().html());
	    		$('#vendor_vendor_nm').val($(this).children().next().next().html());
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getPrjTrHistory(doc_id,rev_id,type){
	 $.ajax({
			type: 'POST',
			url: '../getPrjTrHistory.do',
			data: {
				prj_id:selectPrjId,
				doc_id:doc_id,
				rev_id:rev_id,
				doc_table:type
			},
			success: function(data) {
				let transHis = [];
				for(let i=0;i<data[0].length;i++){
					transHis.push({
				        trno: data[0][i].tr_no,
				        trSubject: data[0][i].tr_subject,
				        inout: data[0][i].inout,
				        purpose: data[0][i].purpose,
				        senddate: to_date_format(data[0][i].send_date,'-'),
				        docno: data[0][i].doc_no,
				        rev: data[0][i].rev_no,
				        rtn: data[0][i].rtn
					});
				}
				setPrjTrHistory(transHis,type);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function setPrjTrHistory(transHis,type){
	var tableId = '#vendorTransHis';
    var table = new Tabulator(tableId, {
        data: transHis,
        layout: "fitColumns",
        height: 130,
        columns: [{
        		title: "TR No.",
	        	field: "trno"
            },
            {
                title: "TR Subject",
                field: "trSubject"
            },
            {
                title: "In/Out",
                field: "inout"
            },
            {
                title: "Purpose",
                field: "purpose"
            },
            {
                title: "Send Date",
                field: "senddate"
            },
            {
                title: "Doc. No",
                field: "docno"
            },
            {
                title: "REV.",
                field: "rev"
            },
            {
                title: "RTN.",
                field: "rtn"
            }
        ],
    });
}
function getVendorPrjTrHistoryNew(){
	let transHis = [];
	setPrjTrHistory(transHis,'VDCI');
}
function getPrjDrnHistory(doc_id,rev_id){
	if(useDRNYN==='Y'){
	 $.ajax({
			type: 'POST',
			url: '../getPrjDrnHistory.do',
			data: {
				prj_id:selectPrjId,
				folder_id:current_folder_id,
				doc_id:doc_id,
				rev_id:rev_id
			},
			success: function(data) {
				let html = '';//'<span>2021-08-13 결재 상신</span>';
				for(let i=0;i<data[0].length;i++){
					let status = data[0][i].status;
					let drn_approval_status = data[0][i].drn_approval_status;
					if(drn_approval_status==='T'){
						status = '임시저장';
					}else if(drn_approval_status==='A'){
						status = '결재상신';
					}else if(drn_approval_status==='E'){
						status = 'DCC 검토완료';
					}else if(drn_approval_status==='1'){
						status = 'REVIEWER 승인';
					}else if(drn_approval_status==='C'){
						status = 'APPROVAL 승인';
					}else if(drn_approval_status==='R'){
						status = '반려';
					}/*else if(status==='C'){
						status = '메일발송완료';
					}*/
					if(status !== '임시저장') html += '<span>'+to_date_format(data[0][i].reg_date,'-')+' '+status+'</span>';
				}
				$('#vendorDrnHistory').html(html);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else{
		$('#vendorDrnHistory').html('DRN PROCESS 미적용');
	}
}
function setPrjDrnHistoryNew(){
	if(useDRNYN==='Y'){
		$('#vendorDrnHistory').html('');
	}else{
		$('#vendorDrnHistory').html('DRN PROCESS 미적용');
	}
}
function vendorDocSave(){
	let doc_id = vendor_new_or_update==='new'?'':vendor_selected_doc_id;
	let rev_id = vendor_new_or_update==='new'?'':vendor_selected_rev_id;
	let doc_no = $('#vendor_doc_no').val();
	let origin_rev_code_id = $('#vendor_origin_rev_code_id').val();
	let rev_code_id = $('#vendorSelectedRevNoSetCodeId').val();
	let folder_id = parseInt(current_folder_id);
	let title = $('#vendor_title').val();
	let discip_code_id = $('#vendor_discipline_code_id').val();
	let designer_id = $('#vendor_designer_id').val();
	let pre_designer_id = $('#vendor_pre_designer_id').val();
	let wbs_code_id = $('#vendorSelectedWbsCodeId').val();
	let remark = $('#vendor_remark').val();
	let po_doc_no = $('#vendor_po_doc_no').val();
	let po_doc_title = $('#vendor_po_doc_title').val();
	let vendor_nm = $('#vendor_vendor_nm').val();
	let design_part = $('#vendor_design_part').val();
	let category_cd_id = $('#vendorSelectedDocCateogrySetCodeId').val();
	let size_code_id = $('#vendorSelectedDocSizeSetCodeId').val();
	let doc_type_id = $('#vendorSelectedDocTypeSetCodeId').val();
    let file_value = $('#vendor_attach_filename').val();
	
	var stepCodeIdArray = [];
	for(let i=0;i<StepTable.getColumns().length;i++){
		let fieldName = StepTable.getColumnDefinitions()[i].field;
		if(fieldName !== 'notitle') stepCodeIdArray.push(fieldName);
	}
	var planDateArray = [];
	var ActualDateArray = [];
	var ForeDateArray = [];
	var tableArrayData = StepTable.getData();
	for(let i=0;i<StepTable.getRows().length-1;i++){
		for(let j=0;j<stepCodeIdArray.length;j++){
			let columnData = tableArrayData[i][stepCodeIdArray[j]];
			if(columnData!=='Invalid date' && columnData!==undefined){
				if(i===0){
					planDateArray.push(columnData.replaceAll('-', '')); 
				}else if(i===1){
					ActualDateArray.push(columnData.replaceAll('-', ''));
				}else if(i===2){
					ForeDateArray.push(columnData.replaceAll('-', ''));
				}
			}else{
				if(i===0){
					planDateArray.push('');
				}else if(i===1){
					ActualDateArray.push('');
				}else if(i===2){
					ForeDateArray.push('');
				}
			}
		}
	}
	/* for(let i=0;i<planDateArray.length;i++){
		if(planDateArray[i]===''){
			alert('Plan Date는 필수입력사항입니다.');
			return;
		}
	} */
	var params = {
			prj_id:selectPrjId,
			popup_new_or_update:vendor_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:'VDCI',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			title:title,
			discip_code_id:discip_code_id,
			designer_id:designer_id,
			prfe_designer_id:pre_designer_id,
			wbs_code_id:wbs_code_id,
			remark:remark,
			po_doc_no:po_doc_no,
			po_doc_title:po_doc_title,
			vendor_nm:vendor_nm,
			design_part:design_part,
			category_cd_id:category_cd_id,
			size_code_id:size_code_id,
			doc_type_id:doc_type_id,
			stepCodeIdArray:JSON.stringify(stepCodeIdArray),
			planDateArray:JSON.stringify(planDateArray),
			ActualDateArray:JSON.stringify(ActualDateArray),
			ForeDateArray:JSON.stringify(ForeDateArray)
	};
	if(doc_no==='' || doc_no===null || doc_no===undefined){
		alert('DOC. NO를 입력해주세요.');
	}else if(title==='' || title===null || title===undefined){
		alert('TITLE을 입력해주세요.');
	}else if(designer_id==='' || designer_id===null || designer_id===undefined){
		alert('DESIGNER를 설정해주세요.');
	}else if(pre_designer_id==='' || pre_designer_id===null || pre_designer_id===undefined){
		alert('PRE.DESIGNER를 설정해주세요.');
	}else if(category_cd_id==='' || category_cd_id===null || category_cd_id===undefined){
		alert('CATEGORY를 설정해주세요.');
	}else if(size_code_id==='' || size_code_id===null || size_code_id===undefined){
		alert('SIZE를 설정해주세요.');
	}else if(doc_type_id==='' || doc_type_id===null || doc_type_id===undefined){
		alert('DOC.TYPE을 설정해주세요.');
	}else if(origin_rev_code_id==='' && file_value!==''){
		alert('Revision');
	}else{
		let form = $('#updateRevisionVendorForm')[0];
	    let formData = new FormData(form);
	    
	    formData.set('prj_id',selectPrjId);
	    formData.set('popup_new_or_update',vendor_new_or_update);
	    formData.set('origin_rev_code_id',origin_rev_code_id);
	    formData.set('doc_id',doc_id);
	    formData.set('rev_id',rev_id);
	    formData.set('doc_type','VDCI');
	    formData.set('doc_no',doc_no);
	    formData.set('rev_code_id',rev_code_id);
	    formData.set('folder_id',folder_id);
	    formData.set('title',title);
	    formData.set('discip_code_id',discip_code_id);
	    formData.set('designer_id',designer_id);
	    formData.set('prfe_designer_id',pre_designer_id);
	    formData.set('wbs_code_id',wbs_code_id);
	    formData.set('remark',remark);
	    formData.set('po_doc_no',po_doc_no);
	    formData.set('po_doc_title',po_doc_title);
	    formData.set('vendor_nm',vendor_nm);
	    formData.set('design_part',design_part);
	    formData.set('category_cd_id',category_cd_id);
	    formData.set('size_code_id',size_code_id);
	    formData.set('doc_type_id',doc_type_id);
	    formData.set('file_value',file_value);
	    if(file_value!==''){
		    DC.sfile_nm = doc_id+'_'+rev_id+'.'+DC.file_type;
		    formData.set('sfile_nm',DC.sfile_nm);
		    formData.set('rfile_nm',DC.rfile_nm);
		    formData.set('file_type',DC.file_type);
		    formData.set('file_size',DC.file_size);
	    }
	    formData.set('stepCodeIdArray',JSON.stringify(stepCodeIdArray));
	    formData.set('planDateArray',JSON.stringify(planDateArray));
	    formData.set('ActualDateArray',JSON.stringify(ActualDateArray));
	    formData.set('ForeDateArray',JSON.stringify(ForeDateArray));
	    formData.set('set_code_type','VENDOR DOC STEP');

		 $.ajax({
				type: 'POST',
		        enctype: 'multipart/form-data',
				url: '../vendorDocSave.do',
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
						if(DCCorTRA==='DCC'){
							alert("DOC NO가 중복되어 저장할 수 없습니다.");
						}else{//TRA폴더
							alert("SYSTEM LINK 폴더에서 참조하는 도서 정보는 수정할 수 없습니다.");
						}
					}else{
						if(data[0]==='파일 수정 불가'){
							alert('Only ADMIN and DCC can modify a file without modifying the Revision.');
						}else if(data[0]==='DRN 반려 파일 수정 불가'){
							alert('In the case of DRN-returned Documents are Only ADMIN and DCC can modify a file without modifying the Revision.');
						}else{
							if(data[0]>0){
								getPopupPrjDocumentIndexList('VDCI');
								getVendorDocumentInfo(data[1],data[2]);
								vendor_selected_doc_id = data[1];
								vendor_selected_rev_id = data[2];
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
					}
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	}
}
function updateRevisionVendor(){
	let doc_id = vendor_new_or_update==='new'?'':vendor_selected_doc_id;
	let rev_id = vendor_new_or_update==='new'?'':vendor_selected_rev_id;
	let doc_no = $('#vendor_doc_no').val();
	let doc_title = $('#vendor_title').val();
	let origin_rev_code_id = $('#vendor_origin_rev_code_id').val();
	let rev_code_id = $('#vendorSelectedRevNoSetCodeId').val();
	let folder_id = current_folder_id;
	DC.sfile_nm = doc_id + '_' + rev_id + '.' + DC.file_type;

	var stepCodeIdArray = [];
	for(let i=0;i<StepTable.getColumns().length;i++){
		let fieldName = StepTable.getColumnDefinitions()[i].field;
		if(fieldName !== 'notitle') stepCodeIdArray.push(fieldName);
	}
	var planDateArray = [];
	var ActualDateArray = [];
	var ForeDateArray = [];
	var tableArrayData = StepTable.getData();
	for(let i=0;i<StepTable.getRows().length-1;i++){
		for(let j=0;j<stepCodeIdArray.length;j++){
			let columnData = tableArrayData[i][stepCodeIdArray[j]];
			if(columnData!=='Invalid date' && columnData!==undefined){
				if(i===0){
					planDateArray.push(columnData.replaceAll('-', '')); 
				}else if(i===1){
					ActualDateArray.push(columnData.replaceAll('-', ''));
				}else if(i===2){
					ForeDateArray.push(columnData.replaceAll('-', ''));
				}
			}else{
				if(i===0){
					planDateArray.push('');
				}else if(i===1){
					ActualDateArray.push('');
				}else if(i===2){
					ForeDateArray.push('');
				}
			}
		}
	}
	var params = {
			prj_id:selectPrjId,
			popup_new_or_update:vendor_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			origin_rev_id:rev_id,
			doc_type:'VDCI',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			sfile_nm:DC.sfile_nm,
			rfile_nm:DC.rfile_nm,
			file_type:DC.file_type,
			file_size:DC.file_size,
			stepCodeIdArray:JSON.stringify(stepCodeIdArray),
			planDateArray:JSON.stringify(planDateArray),
			ActualDateArray:JSON.stringify(ActualDateArray),
			ForeDateArray:JSON.stringify(ForeDateArray),
			set_code_type:'VENDOR DOC STEP'
	};

	let form = $('#updateRevisionVendorForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',vendor_new_or_update);
    formData.set('origin_rev_code_id',origin_rev_code_id);
    formData.set('doc_id',doc_id);
    formData.set('origin_rev_id',rev_id);
    formData.set('doc_type','VDCI');
    formData.set('doc_no',doc_no);
    formData.set('rev_code_id',rev_code_id);
    formData.set('folder_id',folder_id);
    formData.set('sfile_nm',DC.sfile_nm);
    formData.set('rfile_nm',DC.rfile_nm);
    formData.set('file_type',DC.file_type);
    formData.set('file_size',DC.file_size);
    formData.set('stepCodeIdArray',JSON.stringify(stepCodeIdArray));
    formData.set('planDateArray',JSON.stringify(planDateArray));
    formData.set('ActualDateArray',JSON.stringify(ActualDateArray));
    formData.set('ForeDateArray',JSON.stringify(ForeDateArray));
    formData.set('set_code_type','VENDOR DOC STEP');
    let file_value = $('#vendor_attach_filename').val();
    formData.set('file_value',file_value);
    
    if(file_value===''){
    	alert('Attach File! 리비전이 바뀌면 첨부파일을 바꾸셔야 합니다.');
    }else{
		 $.ajax({
				type: 'POST',
		        enctype: 'multipart/form-data',
				url: '../updateRevisionVendor.do',         
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
					}else{
						if(data[0]>0){
							getPopupPrjDocumentIndexList('VDCI');
							$('#vendor_save').attr('disabled',false);
							$('#vendor_revise').attr('disabled',true);
							getVendorDocumentInfo(doc_id,data[1]);
							getRevNoMultiBox('VDCI',doc_id);
							alert('CheckIn Completed');
							vendor_selected_rev_id = data[1];
							
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
				    		    	method: "CHECK-IN"
				    		    },
				    		    success: function onData (data) {
				    		    },
				    		    error: function onError (error) {
				    		        console.error(error);
				    		        alert("오류");
				    		    }
				    		});
						}else{
							alert('Update Revision is failed!');
						}
					}
				},
			    error: function onError (error) {
			        console.error(error);
					alert('Update Revision is failed!');
			    }
			});
    }
}
function drnHistoryOpen(){
	if(thisTable.getSelectedRows().length===0){
		alert('도서를 선택해주세요');
	}else{
		getPrjDrnHistoryData();
	    $(".pop_drnHis").dialog('open');
	}
}
function getPrjDrnHistoryData(){
	let rev_id = thisTable.getSelectedData()[0].bean.rev_id;
	let doc_id = '';
	if(DCCorTRA==='DCC'){
		doc_id = thisTable.getSelectedData()[0].bean.doc_id;
	}else if(DCCorTRA==='TRA'){
		doc_id = thisTable.getSelectedData()[0].bean.trans_ref_doc_id;
	}
	 $.ajax({
			type: 'POST',
			url: '../getPrjDrnHistoryData.do',
			data: {
				prj_id:selectPrjId,
				doc_id:doc_id,
				rev_id:rev_id,
				folder_id:current_folder_id
			},
			success: function(data) {
				var drnHis_tbl = [];
				for(let i=0;i<data[0].length;i++){
					let status = data[0][i].status;
					let drn_approval_status = data[0][i].drn_approval_status;
					if(drn_approval_status==='T'){
						status = '임시저장';
					}else if(drn_approval_status==='A'){
						status = '결재상신';
					}else if(drn_approval_status==='E'){
						status = '검토완료';
					}else if(drn_approval_status==='1'){
						status = 'REVIEWER 승인';
					}else if(drn_approval_status==='C'){
						status = 'APPROVAL 승인';
					}else if(drn_approval_status==='R'){
						status = '반려';
					}
					drnHis_tbl.push({
						DRNNo: data[0][i].drn_no,
						docNo: data[0][i].doc_no,
						revision: data[0][i].revision,
						Discipline: current_folder_discipline_display,
						drafter: data[0][i].drafter,
						status: status,
						date: getDateFormat(data[0][i].reg_date)
					});
				}
				setPrjDrnHistoryData(drnHis_tbl);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function setPrjDrnHistoryData(drnHis_tbl){
    var table = new Tabulator("#drnHis_tbl", {
        data: drnHis_tbl,
        layout: "fitColumns",
        height: 300,
        columns: [{
                title: "drnno",
                field: "DRNNo"
            },
            {
                title: "Document No",
                field: "docNo"
            },
            {
                title: "revision",
                field: "revision"
            },
            {
                title: "discipline",
                field: "Discipline"
            },
            {
                title: "기안자",
                field: "drafter"
            },
            {
                title: "상태",
                field: "status"
            },
            {
                title: "기안일",
                field: "date"
            }
        ],
    });
}
function pageprint(docType){
    html2canvas($('#pdfDivVendor')[0]).then(function(canvas) { //저장 영역 div id
		
	    // 캔버스를 이미지로 변환
	    var imgData = canvas.toDataURL('image/png');
		     
	    var imgWidth = 190; // 이미지 가로 길이(mm) / A4 기준 210mm
	    var pageHeight = imgWidth * 1.414;  // 출력 페이지 세로 길이 계산 A4 기준
	    var imgHeight = canvas.height * imgWidth / canvas.width;
	    var heightLeft = imgHeight;
	    var margin = 10; // 출력 페이지 여백설정
	    var doc = new jsPDF('p', 'mm');
	    var position = 0;
	       
	    // 첫 페이지 출력
	    doc.addImage(imgData, 'PNG', margin, position, imgWidth, imgHeight);
	    heightLeft -= pageHeight;
	         
	    // 한 페이지 이상일 경우 루프 돌면서 출력
	    while (heightLeft >= 20) {
	        position = heightLeft - imgHeight;
	        doc.addPage();
	        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
	        heightLeft -= pageHeight;
	    }
	 
	    // 파일 저장
	    doc.save('Vendor Document.pdf');
 
    });
}

function vendorPopupClose(){
    $('.pop_vendorDocNew').dialog('close');
    getPrjDocumentIndexList();
}
function documentExcel() {
//	console.log(documentListTable.getData());
	
	var select_rev_version = $('#select_rev_version').val();
	var unread_yn=$('#unread_yn').is(':checked')==true?'Y':'N';
	
//	console.log(select_rev_version);
	
	let gridData = documentListTable.getData();
	
	let jsonList = [];
	
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'Document_List';
	
	var f = document.dciUploadForm_PathList;
	
	f.prj_id_PathList.value = selectPrjId;
	f.folder_id_PathList.value = current_folder_id;
	f.prj_nm_PathList.value = getPrjInfo().full_nm;
	f.current_folder_path_PathList.value = current_folder_path;
	f.select_rev_version_PathList.value = select_rev_version;
	f.unread_yn_PathList.value = unread_yn;
	f.fileName_PathList.value = fileName;
	f.jsonRowdatas_PathList.value = jsonList;
	f.action = "DocPathListExcelDownload.do";
    f.submit();
	
	
	
	
	
}

function drnUpBtn(){
	
	let selected = thisTable.getSelectedData();
	if(selected.length == 0) return;
	
	let authChk = opener.checkFolderAuth(selectPrjId,Number(current_folder_id));
	
	if(authChk.w == 'N'){
		alert("There is no Write permission");
		return;
	}
	
	
	
	if(current_folder_discipline_code.length == 0){
		alert("Can't submit this document to DRN");
		return;
	}
	
	// 잠기지 않은 doc만 넘겨준다.
	selected = selected.filter( (element) => element.bean.lock_yn !== 'Y' );
	
	if(selected.length == 0) {
		alert("Can't submit locked document to DRN");
		return;	
	}
	let validationChk = "";
	for(let i=0;i<selected.length;i++)
		validationChk += selected[i].bean.doc_id + "@" +selected[i].bean.rev_id + ",";
	
	validationChk = validationChk.substr(0,validationChk.length-1);
	
	
	let err = "";
	$.ajax({
	    url: '../drnValidationCheck.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:selectPrjId,
	    	doc_type:doc_type,
	    	jsonData:validationChk
	    },
	    success: function onData (data) {
	    	validationChk = data.model.check;
	    	if(data.model.check =="NO"){
	    		let tmp = data.model.error_doc
	    		let msg = data.model.error_msg
	    		err = msg;
	    	}
	    		
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	if(validationChk == "NO") {
		alert(err);
		return;
	}
	
	let discipObj = {};
	discipObj.code = current_folder_discipline_code;
	discipObj.display =current_folder_discipline_display;
	discipObj.code_id = current_folder_discipline_code_id;
	
	opener.drnPopUP(0,selected,discipObj,current_folder_id);
}
function getVendorAccessHisList() {
	$(".pop_accHis").dialog("open");

	let doc_id = vendor_new_or_update==='new'?'':vendor_selected_doc_id;
	let rev_id = vendor_new_or_update==='new'?'':vendor_selected_rev_id;
	let doc_no = $('#vendor_doc_no').val();
	let rev_code_id = $('#vendorRevNoSelected').html();
	
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
	}else if(type==='Corr'){
		select_rev_version = $('#popup_corr_rev_version').is(':checked')===true?'all':'current';
	}else if(type==='General'){
		select_rev_version = $('#popup_general_rev_version').is(':checked')===true?'all':'current';
	}
	var unread_yn='N';
	let troutproperty = 0;
	if(tr_id!==''){
		troutproperty=1;
	}
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
	}else if(type==='Corr'){
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
		if(type==='DCI'){
			if(DCCorTRA==='DCC'){
				eng_selected_doc_id = doc_id;
			}else if(DCCorTRA==='TRA'){
				eng_selected_doc_id = trans_ref_doc_id;
			}
			eng_selected_rev_id = rev_id;
	    	getDocumentInfo(eng_selected_doc_id,eng_selected_rev_id);
	    	getRevNoMultiBox('DCI',doc_id);
		}else if(type==='VDCI'){
			if(DCCorTRA==='DCC'){
				vendor_selected_doc_id = doc_id;
				vendor_selected_rev_id = rev_id;
		    	getVendorDocumentInfo(vendor_selected_doc_id,vendor_selected_rev_id);
		    	getRevNoMultiBox('VDCI',doc_id);
			}else if(DCCorTRA==='TRA'){
				if(selected_doc_type==='VDCI'){
					vendor_selected_doc_id = trans_ref_doc_id;
					vendor_selected_rev_id = rev_id;
			    	getVendorDocumentInfo(vendor_selected_doc_id,vendor_selected_rev_id);
			    	getRevNoMultiBox('VDCI',doc_id);
				}else{
					if(trans_ref_doc_id==='COVER'){
						alert('COVER파일은 property를 확인할 수 없습니다.');
					}else{
						alert('해당 문서는 Vendor 타입 문서가 아닙니다.');
					}
				}
			}
		}else if(type==='PCI'){
			if(DCCorTRA==='DCC'){
				proc_selected_doc_id = doc_id;
			}else if(DCCorTRA==='TRA'){
				proc_selected_doc_id = trans_ref_doc_id;
			}
			proc_selected_rev_id = rev_id;
	    	getProcDocumentInfo(proc_selected_doc_id,proc_selected_rev_id);
	    	getRevNoMultiBox('PCI',doc_id);
		}else if(type==='SDCI'){
			if(DCCorTRA==='DCC'){
				site_selected_doc_id = doc_id;
			}else if(DCCorTRA==='TRA'){
				site_selected_doc_id = trans_ref_doc_id;
			}
			site_selected_rev_id = rev_id;
	    	getSiteDocumentInfo(site_selected_doc_id,site_selected_rev_id);
	    	getRevNoMultiBox('SDCI',doc_id);
		}else if(type==='Corr'){
			if(DCCorTRA==='DCC'){
				corr_selected_doc_id = doc_id;
			}else if(DCCorTRA==='TRA'){
				corr_selected_doc_id = trans_ref_doc_id;
			}
			corr_selected_rev_id = rev_id;
	    	getCorrDocumentInfo(corr_selected_doc_id,corr_selected_rev_id);
	    	getRevNoMultiBox('Corr',doc_id);
		}else if(type==='General'){
			if(DCCorTRA==='DCC'){
				general_selected_doc_id = doc_id;
			}else if(DCCorTRA==='TRA'){
				general_selected_doc_id = trans_ref_doc_id;
			}
			general_selected_rev_id = rev_id;
	    	getGeneralDocumentInfo(general_selected_doc_id,general_selected_rev_id);
	    	getRevNoMultiBox('General',doc_id);
		}
	});
}
function getVendorDocumentInfo(doc_id,rev_id){
    $('#vendor_attach_filename').prop('disabled',false);
	$('#vendor_popup_attach').removeClass('label_disabled');
	vendor_new_or_update = 'update';
	console.log(doc_id,rev_id);
	$.ajax({
	    url: '../getVendorDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id,
	    	inout:''
	    },
	    success: function onData (data) {
	    	current_folder_id = data[0].folder_id;
	    	$('#vendorRevNoSelected').css('pointer-events', 'auto');
	    	$('#vendorRevNoSelected').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
	    	$('#vendorRevNoSelected').css('background-color', '#fff');
	    	$('#vendorRevNoSelected').css('color', '#000');
	    	
	    	if(data[0].lock_yn==='Y'){
	    		$('#vendor_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#vendor_revise').attr('disabled',true);
	    		$('#vendor_save').attr('disabled',true);
	    	}else{
	    		$('#vendor_locker').html('');
	    		$('#vendor_save').attr('disabled',false);
	    	}
	    	$('#vendor_rev_max').val(data[0].rev_max);
	    	if(data[0].rev_max!==rev_id && data[0].rev_max!=='001'){
	    		$('#vendor_oldversion').html('Old Version');
	    	}else{
	    		$('#vendor_oldversion').html('');
	    	}
	    	$('#vendor_doc_no').val(data[0].doc_no);
	    	if(data[0].rev_code_id!==null && data[0].rev_code_id!==undefined && data[0].rev_code_id!==''){
		    	$('#vendor_doc_no').attr("disabled", true);
		    	$('#vendorRevNoSelected').html(data[0].rev_no);
		    	$('#vendor_origin_rev_code_id').val(data[0].rev_code_id);
		    	$('#vendorSelectedRevNoSetCodeId').val(data[0].rev_code_id);
	    	}else{
		    	$('#vendor_doc_no').attr("disabled", false);
		    	$('#vendorRevNoSelected').html('SELECT');
		    	$('#vendor_origin_rev_code_id').val('');
		    	$('#vendorSelectedRevNoSetCodeId').val('');
	    	}
	    	$('#vendor_old_doc_no').val(data[0].old_doc_no);
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
	    		let attach_file_date = '';
	    		if(data[0].attach_file_date!==null){
	    			attach_file_date = ', '+getDateFormat(data[0].attach_file_date);
	    		}
		    	$('#vendor_uploadFileDate').val(formatBytes(data[0].file_size,2)+attach_file_date);
		    	$('#rfile_download').html(data[0].rfile_nm);
		    	$('#rfile_download').addClass('property_download');
		    	$('#rfile_download').removeClass('property_attach');
	    	}else{
		    	$('#vendor_uploadFileDate').val('');
		    	$('#rfile_download').html('');
		    	$('#rfile_download').addClass('property_attach');
		    	$('#rfile_download').removeClass('property_download');
	    	}
	    	$('#vendor_title').val(data[0].title);
	    	$('#vendor_creation').val(data[0].reg_nm+'('+data[0].reg_kor_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#vendor_modification').val(data[0].mod_date===null?'':data[0].mod_nm+'('+data[0].mod_kor_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#vendor_discipline').val(data[0].discip_display);
		    $('#vendor_discipline_code_id').val(data[0].discip_code_id);
			let vendor_designer_id = $('#vendor_designer_id').eq(0).data('selectize');
			vendor_designer_id.setValue(data[0].designer_id,false);
	    	$('#vendor_pre_designer').val(data[0].pre_designer_nm+'('+data[0].prfe_designer_id+')');
	    	$('#vendor_pre_designer_id').val(data[0].prfe_designer_id);
	    	if(data[0].wbs_code_id!==null && data[0].wbs_code_id!==undefined && data[0].wbs_code_id!==''){
		    	$('#vendorWbsCodeSelected').html(data[0].wbs_code);
		    	$('#vendorSelectedWbsCodeId').val(data[0].wbs_code_id);
	    	}else{
		    	$('#vendorWbsCodeSelected').html('SELECT');
	    	}
	    	$('#vendor_wbs_detail_1').val(data[0].wgtval);
	    	$('#vendor_wbs_detail_2').val(data[0].wbs_desc);
	    	$('#vendor_remark').val(data[0].remark);
    		$('#vendorProcDocSelected').html(data[0].po_doc_no);
    		$('#vendor_po_doc_no').val(data[0].po_doc_no);
    		$('#vendor_po_doc_title').val(data[0].po_doc_title);
    		$('#vendor_vendor_nm').val(data[0].vendor_nm);
	    	if(data[0].category_code!==null && data[0].category_code!==undefined && data[0].category_code!==''){
		    	$('#vendorDocCategorySelected').html(data[0].category_code);
		    	$('#vendorSelectedDocCateogrySetCodeId').val(data[0].category_cd_id);
	    	}else{
		    	$('#vendorDocCategorySelected').html('SELECT');
	    	}
	    	if(data[0].size_code!==null && data[0].size_code!==undefined && data[0].size_code!==''){
		    	$('#vendorDocSizeSelected').html(data[0].size_code);
		    	$('#vendorSelectedDocSizeSetCodeId').val(data[0].size_code_id);
	    	}else{
		    	$('#vendorDocSizeSelected').html('SELECT');
	    	}
	    	if(data[0].doc_type_code!==null && data[0].doc_type_code!==undefined && data[0].doc_type_code!==''){
		    	$('#vendorDocTypeSelected').html(data[0].doc_type_code);
		    	$('#vendorSelectedDocTypeSetCodeId').val(data[0].doc_type_id);
	    	}else{
		    	$('#vendorDocTypeSelected').html('SELECT');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	getPrjStep(doc_id,rev_id,'VDCI');
	getPrjTrHistory(doc_id,rev_id,'VDCI');
	getPrjDrnHistory(doc_id,rev_id);
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
function docDelete(type){
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
	let doc_no = $('#vendor_doc_no').val();
	let doc_title = $('#vendor_title').val();
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
function popupRefreshButton(type){
    $('#popup_revise').attr('disabled',true);
    $('#vendor_revise').attr('disabled',true);
    $('#proc_revise').attr('disabled',true);
    $('#site_revise').attr('disabled',true);
    $('#general_revise').attr('disabled',true);
	var doc_id = thisTable.getRows()[0].getData().bean.doc_id;
	var trans_ref_doc_id = thisTable.getRows()[0].getData().bean.trans_ref_doc_id;
	var rev_id = thisTable.getRows()[0].getData().bean.rev_id;
	if(DCCorTRA==='DCC'){
		if(type==='DCI'){
			getDocumentInfo(doc_id,rev_id);
		}else if(type==='VDCI'){
			getVendorDocumentInfo(doc_id,rev_id);
		}else if(type==='PCI'){
			getProcDocumentInfo(doc_id,rev_id);
		}else if(type==='SDCI'){
			getSiteDocumentInfo(doc_id,rev_id);
		}else if(type==='Corr'){
			getCorrDocumentInfo(doc_id,rev_id);
		}else if(type==='General'){
			getGeneralDocumentInfo(doc_id,rev_id);
		}
	}else if(DCCorTRA==='TRA'){
		if(type==='DCI'){
			getDocumentInfo(trans_ref_doc_id,rev_id);
		}else if(type==='VDCI'){
			getVendorDocumentInfo(trans_ref_doc_id,rev_id);
		}else if(type==='PCI'){
			getProcDocumentInfo(trans_ref_doc_id,rev_id);
		}else if(type==='SDCI'){
			getSiteDocumentInfo(trans_ref_doc_id,rev_id);
		}else if(type==='Corr'){
			getCorrDocumentInfo(trans_ref_doc_id,rev_id);
		}else if(type==='General'){
			getGeneralDocumentInfo(trans_ref_doc_id,rev_id);
		}
	}
	getPopupPrjDocumentIndexList(type,'refresh');
}
function vendorNewButton(){
	vendor_new_or_update = 'new';
	getPopupPrjDocumentIndexList('VDCI');
	$('#vendorRevNoSelected').css('pointer-events', 'none');
	$('#vendorRevNoSelected').css('background', 'none');
	$('#vendorRevNoSelected').css('background-color', '#FAFAFA');
	$('#vendorRevNoSelected').css('color', '#FAFAFA');
	getRevNoMultiBox('VDCI');
    getPrjStep('','','VDCI','new');
	setVendorNull();
    getVendorPrjTrHistoryNew();
    setPrjDrnHistoryNew();
}
function setVendorNull(){
	$('#rfile_download').html('');
	$('#vendor_popup_attach').addClass('label_disabled');
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#vendor_doc_no').attr("disabled", false);
    $('#vendor_attach_filename').val('');
    $('#vendor_attach_filename').prop('disabled',true);
	$('#vendor_locker').html('');
	$('#vendor_oldversion').html('');
	$('#vendor_doc_no').val('');
	$('#vendorRevNoSelected').html('SELECT');
	$('#vendor_revise').attr('disabled',true);
	$('#vendor_save').attr('disabled',false);
	$('#vendorSelectedSetCodeId').val('');
	$('#vendor_old_doc_no').val('');
	$('#vendor_title').val('');
	$('#vendor_creation').val(session_user_eng_nm+'('+session_user_kor_nm+'): '+ $('#currentTime').val());
	$('#vendor_modification').val(session_user_eng_nm+'('+session_user_kor_nm+'): '+ $('#currentTime').val());
	if(current_folder_discipline_code_id===null || current_folder_discipline_code_id===undefined || current_folder_discipline_code_id===''){
		$('#vendor_discipline_code_id').val($('#docSelTree_discipline_code_id').val());
		$('#vendor_discipline').val($('#docSelTree_discipline_display').val());
	}else{
		$('#vendor_discipline_code_id').val(current_folder_discipline_code_id);
		$('#vendor_discipline').val(current_folder_discipline_display);
	}
	let vendor_designer_id = $('#vendor_designer_id').eq(0).data('selectize');
	vendor_designer_id.setValue('',false);
	$('#vendor_pre_designer_id').val('');
	$('#vendor_pre_designer').val('');
	$('#vendorSelectedWbsCodeId').val('');
	$('#vendorWbsCodeSelected').html('SELECT');
	$('#vendor_wbs_detail_1').val('');
	$('#vendor_wbs_detail_2').val('');
	$('#vendor_remark').val('');
	$('#vendorProcDocSelected').html('SELECT');
	$('#vendor_po_doc_no').val('');
	$('#vendor_po_doc_title').val('');
	$('#vendor_vendor_nm').val('');
	$('#vendor_design_part').val('');
	$('#vendorSelectedDocCateogrySetCodeId').val('');
	$('#vendorDocCategorySelected').html('SELECT');
	$('#vendorSelectedDocSizeSetCodeId').val('');
	$('#vendorDocSizeSelected').html('SELECT');
	$('#vendorSelectedDocTypeSetCodeId').val('');
	$('#vendorDocTypeSelected').html('SELECT');
	$('#vendor_uploadFileDate').val('');
}
function propertydownload(){

	$.ajax({
	    url: '../getVendorDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:vendor_selected_doc_id,
	    	rev_id:vendor_selected_rev_id,
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