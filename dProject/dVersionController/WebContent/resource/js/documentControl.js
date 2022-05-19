/* 
 * 작성자		: 소진희
 * 작성일		: 2021-12-17 ~ 2021-12-29
 */
var context_path = $('#context_path').val();
var selectPrjId = $('#selectPrjId').val();
var session_user_id = $('#session_user_id').val();
var session_user_eng_nm = $('#session_user_eng_nm').val();
var rowMenu;
var process_type = '';
var documentListTable;
var thisTable;
var StepTable;
var popupEngDocumentListTable;
var vendorDocumentListTable;
var procDocumentListTable;
var bulkDocumentListTable;
var siteDocumentListTable;
var corrDocumentListTable;
var generalDocumentListTable;
var searchDesignerTable;
var DCEngStepTable;
var DCVendorStepTable;
var DCProcStepTable;
var DCSiteStepTable;
var DC = {};
var popup_new_or_update = 'new';
var vendor_new_or_update = 'new';
var proc_new_or_update = 'new';
var bulk_new_or_update = 'new';
var site_new_or_update = 'new';
var corr_new_or_update = 'new';
var general_new_or_update = 'new';
var eng_selected_doc_id = '';
var eng_selected_rev_id = '';
var vendor_selected_doc_id = '';
var vendor_selected_rev_id = '';
var proc_selected_doc_id = '';
var proc_selected_rev_id = '';
var site_selected_doc_id = '';
var site_selected_rev_id = '';
var corr_selected_doc_id = '';
var corr_selected_rev_id = '';
var general_selected_doc_id = '';
var general_selected_rev_id = '';
var current_folder_docType = 'General';
var trINOUT_discipline_display = '';
var checkInVar = {};
var corrSelectedAddType='';
var corrSelectedUserEmail='';
var corrSelectedEmail='';
var corrSelectedUserId = '';
var multiDownloadTable;
var init_email_list = [];
var currentSort = [];

var documentControl_loading = '<div id="documentControl_loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';
var document_gridHeight = 600;

	
	contextMenu = [{
		label: "Open",
		action: function(e, row) {
			let selectedArray = documentListTable.getSelectedData();
			var accHisDownList = [];
			if(selectedArray.length===0){
				alert('open할 도서를 선택해주세요.');
			}else{
				let sfile_nm = '';
				let lock_yn = '';
				for(let i=0;i<selectedArray.length;i++){
					lock_yn += selectedArray[i].bean.lock_yn;
				}
				if(lock_yn.indexOf('Y') !== -1){
					alert('잠금 상태가 아닌 도서를 선택하세요.');
				}else{
					if(selectedArray.length===1 && selectedArray[0].bean.file_type===null){
						alert('해당 도서는 문서가 첨부되지 않았습니다.');
						return;
					}
					for(let i=0;i<selectedArray.length;i++){
						if(selectedArray[i].bean.file_type!==null){
							if(DCCorTRA==='TRA' && selectedArray[i].bean.trans_ref_doc_id==='COVER'){
								accHisDownList.push(JSON.stringify(selectedArray[i].bean));
								location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm);
								fnSleep(1000);
							}else{
								accHisDownList.push(JSON.stringify(selectedArray[i].bean));
								let tmpRfileNm = selectedArray[i].bean.rfile_nm.replaceAll("&","@%@");
								
								location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(tmpRfileNm);
								fnSleep(1000);
							}
						}
					}
					$.ajax({
						url: 'insertAccHisList.do',
						type: 'POST',
						traditional : true,
						async: false,
						data:{
							jsonRowDatas: accHisDownList,
							prj_id: selectPrjId,
							method: 'OPEN'
						},
						success: function onData (data) {
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
					let docid_revid='';
					for(let i=0;i<selectedArray.length;i++){
						docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
					}
					docid_revid = docid_revid.slice(0,-2);
					$.ajax({
					    url: 'readDocCheck.do',
					    type: 'POST',
					    data:{
							prj_id: selectPrjId,
						    docid_revid:docid_revid
					    },
					    success: function onData (data) {
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}
			}
		}
	},
	{
		label: "Download",
		action: function(e, row) {
			let selectedArray = documentListTable.getSelectedData();
			if(selectedArray.length===0){
				alert('다운로드 받을 도서를 선택해주세요.');
			}else{
				let multiDownTbl = [];
				let lock_yn = '';
				for(let i=0;i<selectedArray.length;i++){
					lock_yn += selectedArray[i].bean.lock_yn;
				}
				if(lock_yn.indexOf('Y') !== -1){
					alert('잠금 상태가 아닌 도서를 선택하세요.');
				}else{
					for(let i=0;i<selectedArray.length;i++){
						let rfile_nm = selectedArray[i].bean.rfile_nm;
						if(DCCorTRA==='TRA' && selectedArray[i].bean.trans_ref_doc_id==='COVER'){
							rfile_nm = selectedArray[i].bean.sfile_nm;
						}
						multiDownTbl.push({
							FileName:rfile_nm,
							RevNo:selectedArray[i].bean.rev_no,
							Title:selectedArray[i].bean.title,
							Size:formatBytes(selectedArray[i].bean.file_size,2),
							bean:selectedArray[i].bean
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
					}
					$(".pop_multiDownload").dialog("open");
					$('#downloadFileRename').prop('checked',false);
					$('#fileRenameSet').val('');
					//$('#fileMultiDownload').attr('onclick','fileMultiDownload()');

					let docid_revid='';
					for(let i=0;i<selectedArray.length;i++){
						docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
					}
					docid_revid = docid_revid.slice(0,-2);
					$.ajax({
					    url: 'readDocCheck.do',
					    type: 'POST',
					    data:{
							prj_id: selectPrjId,
						    docid_revid:docid_revid
					    },
					    success: function onData (data) {
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
					return false;
				}
			}
		}
	},
	{
		label: "Edit",
		action: function(e, row) {
			let selectedArray = documentListTable.getSelectedData();
			if(selectedArray.length===0){
				alert('Edit할 도서를 선택해주세요.');
			}else{
				let sfile_nm = '';
				for(let i=0;i<selectedArray.length;i++){
					let lock_yn = selectedArray[i].bean.lock_yn;
					if(lock_yn==='Y'){
						alert('도서가 잠금 상태입니다.');
					}else{
						location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.rfile_nm.replaceAll("&","@%@"));
						fnSleep(1000);
						
						let doc_id = selectedArray[i].bean.doc_id;
						let rev_id = selectedArray[i].bean.rev_id;
						let method = "EDIT";
						let doc_no = selectedArray[i].bean.doc_no;
						let doc_title = selectedArray[i].bean.title;
						let folder_id = selectedArray[i].bean.folder_id;
						let file_size = selectedArray[i].bean.file_size;
						//console.log(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
						documentCheckOut(doc_id,rev_id,doc_no,doc_title,folder_id,file_size,method);

						
						let docid_revid='';
						for(let i=0;i<selectedArray.length;i++){
							docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
						}
						docid_revid = docid_revid.slice(0,-2);
						$.ajax({
						    url: 'readDocCheck.do',
						    type: 'POST',
						    data:{
								prj_id: selectPrjId,
							    docid_revid:docid_revid
						    },
						    success: function onData (data) {
						    },
						    error: function onError (error) {
						        console.error(error);
						    }
						});
					}
				}
			}
		}
	},
	{
		label: "Check-Out",
		action: function(e, row) {
			let selectedArray = documentListTable.getSelectedData();
			if(selectedArray.length===0){
				alert('Check-Out할 도서를 선택해주세요.');
			}else{
				for(let i=0;i<selectedArray.length;i++){
					let doc_id = selectedArray[i].bean.doc_id;
					let rev_id = selectedArray[i].bean.rev_id;
					let method = "CHECK-OUT";
					let doc_no = selectedArray[i].bean.doc_no;
					let doc_title = selectedArray[i].bean.title;
					let folder_id = selectedArray[i].bean.folder_id;
					let file_size = selectedArray[i].bean.file_size;
					//console.log(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
					documentCheckOut(doc_id,rev_id,doc_no,doc_title,folder_id,file_size,method);
					//documentCheckOut(doc_id,rev_id,method);
				}
				
				let docid_revid='';
				for(let i=0;i<selectedArray.length;i++){
					docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
				}
				docid_revid = docid_revid.slice(0,-2);
				$.ajax({
				    url: 'readDocCheck.do',
				    type: 'POST',
				    data:{
						prj_id: selectPrjId,
					    docid_revid:docid_revid
				    },
				    success: function onData (data) {
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
			}
			
			//alert('선택된 항목의 문서를 수정하기 위해 Lock 상태로 전환 (Check-in 한 문서의 수정이 필요하여 잠금상태로 변경하는 기능 - 잠금을 풀기전에는 다운로드, 수정, Transmittal 송부 안됨)');
		}
	},
	{
		label: "Check-In",
		action: function(e, row) {
			// alert('별도 Check-in 화면 팝업, 리비전, 필수값 입력, 첨부하는 화면 (문서에 수정된 내용/첨부를 반영, 새 리비전 생성할 때 사용하는 기능)');
			popCheckIn(row);
		}
	},
	{
		disabled:true,
		label: "Cancel Check-Out",
		action: function(e, row) {
			let selectedArray = documentListTable.getSelectedData();
			if(selectedArray.length===0){
				alert('Cancel Check-Out할 도서를 선택해주세요.');
			}else{
				for(let i=0;i<selectedArray.length;i++){
					let doc_id = selectedArray[i].bean.doc_id;
					let rev_id = selectedArray[i].bean.rev_id;
					let doc_no = selectedArray[i].bean.doc_no;
					let doc_title = selectedArray[i].bean.title;
					let folder_id = selectedArray[i].bean.folder_id;
					let file_size = selectedArray[i].bean.file_size;
					//console.log(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
					documentCheckOutCancel(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
					//documentCheckOutCancel(doc_id,rev_id);
				}
			}
		}
	},
	{
		label: "Property",
		action: function(e, row) {
			contextmenuProperty(row);

			let docid_revid=row.getData().bean.doc_id + '&&' + row.getData().bean.rev_id + '&&' + row.getData().bean.folder_id;
			$.ajax({
			    url: 'readDocCheck.do',
			    type: 'POST',
			    data:{
					prj_id: selectPrjId,
				    docid_revid:docid_revid
			    },
			    success: function onData (data) {
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		} 
	},
	{
		label: "Access History",
		action: function(e, row) {
			//alert('저장, Check-in, Check-in 수정, Check-Out 등 이벤트가 발생한 로그확인 화면(팝업)');
			getAccessHisList();
	        return false;
		}
	},
	{
		label: "Select All",
		action: function(e, row) {
			$("input[name='docListAll']")[0].checked = true;
    		documentListTable.selectRow("visible");
    		contextMenu[9].disabled = false;
		}
	},
	{
		disabled: true,
		label: "Cancel Select",
		action: function(e, row) {
			$("input[name='docListAll']")[0].checked = false;
    		documentListTable.deselectRow();
    		contextMenu[9].disabled = true;
		}
	},
	{
		label: "New",
		action: function(e, row) {
			contextmenuNewByProcessType();
			//contextmenuNew(row);
	        return false;
		}
	},
	{
		label: "Delete",
		action: function(e, row) {
			var doc_id = row.getData().bean.doc_id;
			var rev_id = row.getData().bean.rev_id;
			if(documentListTable.getSelectedRows().length===0){
				alert('삭제할 도서를 선택해주세요.');
			}else{
				let auth = checkFolderAuth(prj_id,current_folder_id);
				let d = (auth.d == 'Y');
				if(!d){
					alert('There is no delete permission for this folder.');
					return;
				}
				
			    $(".pop_documentDeleteConfirm").dialog('open');
				$('#deleteConfirmMessage').html("Would you like to delete documents? (If you delete document, it can't restore)");
				var doc_type = row.getData().bean.doc_type;
				$('#documentDeleteButton').attr('onclick','documentDelete(`'+doc_type+'`)');
			}
		}
	},
	{
		label: "Save document list(xls)",
		action: function(e, row) {
//			alert('해당 폴더의 DCI Index 리스트만 엑셀로 다운로드 하는 기능 (템플릿 별도제공)');
			
			var select_rev_version = $('#select_rev_version').val();
			var unread_yn=$('#unread_yn').is(':checked')==true?'Y':'N';
			
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
	},
	{
		label: "Send e-mail",
		action: function(e, row) {
			$(".pop_mailNoticeDC").dialog("open");
			getDCEmailList();
	    	let dc_receiver_list = $('#dc_receiver_list').eq(0).data('selectize');
	    	dc_receiver_list.setValue(null,false);
	    	let selectedArray = documentListTable.getSelectedData();
	    	let docid_revid_docno_revno = '';
			if(selectedArray.length===0){
				let docTable = '<p></p><br><table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
				let doc_id = row.getData().bean.doc_id;
				let rev_id = row.getData().bean.rev_id;
				docid_revid_docno_revno += doc_id + '&&' + rev_id + '&&' + row.getData().bean.doc_no +'&&'+row.getData().bean.rev_no+'@@';
		    	DC.user_data00 = doc_id + '&&' + rev_id;
				let downloadFile = doc_id + '_' + rev_id + '.' + row.getData().bean.file_type;
				docTable += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(row.getData().bean.rfile_nm)+'&email_type_id='+encodeURIComponent(0)+'&email_id=@@email_id_date@@">'+row.getData().bean.doc_no+'</a></td><td>'+row.getData().bean.rev_no+'</td><td>'+row.getData().bean.title+'</td><td>'+row.getData().bean.file_type+'</td><td>'+formatBytes(row.getData().bean.file_size,2)+'</td></tr>'
				docTable += '</tbody></table><p></p><br>';
		    	$('#summernoteMailDC').summernote("code", docTable);
			}else{
				DC.user_data00 = '';
				let docTable = '<p></p><br><table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
				for(let i=0;i<selectedArray.length;i++){
					let doc_id = selectedArray[i].bean.doc_id;
					let rev_id = selectedArray[i].bean.rev_id;
					docid_revid_docno_revno += doc_id + '&&' + rev_id + '&&' + selectedArray[i].bean.doc_no +'&&'+selectedArray[i].bean.rev_no+'@@';
			    	DC.user_data00 += doc_id + '&&' + rev_id +'@@';
					let downloadFile = doc_id + '_' + rev_id + '.' + selectedArray[i].bean.file_type;
					docTable += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.rfile_nm)+'&email_type_id='+encodeURIComponent(0)+'&email_id=@@email_id_date@@">'+selectedArray[i].bean.doc_no+'</a></td><td>'+selectedArray[i].bean.rev_no+'</td><td>'+selectedArray[i].bean.title+'</td><td>'+selectedArray[i].bean.file_type+'</td><td>'+formatBytes(selectedArray[i].bean.file_size,2)+'</td></tr>'
				}
				DC.user_data00 = DC.user_data00.slice(0,-2);
				docTable += '</tbody></table><p></p><br>';
		    	$('#summernoteMailDC').summernote("code", docTable);
			}
			docid_revid_docno_revno = docid_revid_docno_revno.slice(0,-2);
	    	DC.docid_revid_docno_revno = docid_revid_docno_revno;
			
		}
	}];
$(function(){
	


	if(current_process_type==='E-MAIL' || current_process_type==='LETTER'){
		$('#select_rev_version').css('display','none');
	}else{
		$('#select_rev_version').css('display','inline-block');
	}
	
	$('.pop_mailNoticeDC').dialog({
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
	$(".pop_corrUserSearchM").dialog({
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
	
	var toolbar = [
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
	$("#summernoteMailDC").summernote({
		height: 500,
		lang: "ko_KR",
		toolbar: toolbar,
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체']
	});
	$(".pop_SendEmailDC").dialog({
		autoOpen: false,
		maxWidth: 500,
		width: "auto"
	}).dialogExtend({
		"closable" : true,
		"maximizable" : true,
		"minimizable" : true,
		// "collapse" : function(evt) {
		// 	$(".ui-dialog-title").hide();
		// },
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
	
	$(".popupDrnUp").click(function(){
	    	let selected = thisTable.getSelectedData();
	    	if(selected.length == 0) return;
	    	
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
	    	
	    	let idx = folderList.findIndex(i => i.id == current_folder_id);
	    	let chk_doc_type = folderList[idx].doc_type
	    	
	    	console.log(chk_doc_type);
	    	
	    	let err = "";
	    	$.ajax({
	    	    url: 'drnValidationCheck.do',
	    	    type: 'POST',
	    	    async:false,
	    	    data:{
	    	    	prj_id:getPrjInfo().id,
	    	    	doc_type:chk_doc_type,
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
	    	let tabIdx = tabList.findIndex(i => i.id == "#dccTab");
	    	drnUp(0,selected,discipObj,tabList[tabIdx].folder_id);
	    });
	
	
	//Engineering 파일첨부
    var fileTarget = $('.filebox .documentFileAttach');

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
        $('#eng_pop_uploadFileInfo').val(extension+', '+formatBytes(size,2));
        $('#eng_pop_uploadFileDate').val(today.toLocaleString());
        if($('#popup_locker').html()!=='' && $('#popup_rev_max').val()===eng_selected_rev_id && $('#selectedRevNoSetCodeId').val()!=='' && $('#origin_rev_code_id').val()!==$('#selectedRevNoSetCodeId').val()){
        	$('#popup_revise').attr('disabled',false);
        }
    });
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
        $('#vendor_uploadFileInfo').val(extension+', '+formatBytes(size,2));
        $('#vendor_uploadFileDate').val(today.toLocaleString());
        if($('#vendor_locker').html()!=='' && $('#vendor_rev_max').val()===vendor_selected_rev_id && $('#vendorSelectedRevNoSetCodeId').val()!=='' && $('#vendor_origin_rev_code_id').val()!==$('#vendorSelectedRevNoSetCodeId').val()){
        	$('#vendor_revise').attr('disabled',false);
        }
    });
	//Procurment 파일첨부
    var fileTarget = $('.filebox .procDocumentFileAttach');

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
        $('#proc_uploadFileInfo').val(extension+', '+formatBytes(size,2));
        $('#proc_uploadFileDate').val(today.toLocaleString());
        if($('#proc_locker').html()!=='' && $('#proc_rev_max').val()===proc_selected_rev_id && $('#procSelectedRevNoSetCodeId').val()!=='' && $('#proc_origin_rev_code_id').val()!==$('#procSelectedRevNoSetCodeId').val()){
        	$('#proc_revise').attr('disabled',false);
        }
    });
	//Site 파일첨부
    var fileTarget = $('.filebox .siteDocumentFileAttach');

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
        $('#site_uploadFileInfo').val(extension+', '+formatBytes(size,2));
        $('#site_uploadFileDate').val(today.toLocaleString());
        if($('#site_locker').html()!=='' && $('#site_rev_max').val()===site_selected_rev_id && $('#siteSelectedRevNoSetCodeId').val()!=='' && $('#site_origin_rev_code_id').val()!==$('#siteSelectedRevNoSetCodeId').val()){
        	$('#site_revise').attr('disabled',false);
        }
    });
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
        $('#corr_uploadFileInfo').val(extension+', '+formatBytes(size,2));
        $('#corr_uploadFileDate').val(today.toLocaleString());
    });
	//일반문서(General) 파일첨부
    var fileTarget = $('.filebox .generalDocumentFileAttach');

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
        $('#general_uploadFileInfo').val(extension+', '+formatBytes(size,2));
        $('#general_uploadFileDate').val(today.toLocaleString());
        if($('#general_locker').html()!=='' && $('#general_rev_max').val()===general_selected_rev_id && $('#generalSelectedRevNoSetCodeId').val()!=='' && $('#general_origin_rev_code_id').val()!==$('#generalSelectedRevNoSetCodeId').val()){
        	$('#general_revise').attr('disabled',false);
        }
    });
    
	$('#dc_current_folder_path').val(current_folder_path);
//	console.log(current_folder_path);
	$('input:checkbox[name="docListAll"]').change(function(){
        if($('input:checkbox[name="docListAll"]').is(":checked")){
        	contextMenu[8].disabled = true; // sellect all 비활성화
        	contextMenu[9].disabled = false; // cancel select 활성화
        	console.log("체크 올");
    		documentListTable.selectRow("visible");
        }else{
        	contextMenu[8].disabled = false; // sellect all 활성화
        	contextMenu[9].disabled = true; // cancel select 비활성화
        	console.log("체크 해제");
    		documentListTable.deselectRow();
        }
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
    $(".pop_docEngPopup").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: "auto"
    }).bind('dialogclose', function(event, ui) { 
    	getPrjDocumentIndexList();
    	$('#engRefreshButton').css('display','inline-block');
    	$('#engNewButton').css('display','inline-block');
		$('.allVersionCheckDiv').css('display','block');
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
    $(".pop_vendorDocNew").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: "auto"
    }).bind('dialogclose', function(event, ui) { 
    	getPrjDocumentIndexList();
    	$('#vendorRefreshButton').css('display','inline-block');
    	$('#vendorNewButton').css('display','inline-block');
		$('.allVersionCheckDiv').css('display','block');
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
    $(".pop_prcmCtrlNew").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: "auto"
    }).bind('dialogclose', function(event, ui) { getPrjDocumentIndexList(); })
    .dialogExtend({
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
    $(".pop_siteDocNew").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: "auto"
    }).bind('dialogclose', function(event, ui) { 
    	getPrjDocumentIndexList();
    	$('#siteRefreshButton').css('display','inline-block');
    	$('#siteNewButton').css('display','inline-block');
		$('.allVersionCheckDiv').css('display','block');
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
    $(".pop_generalDocNew").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: "auto"
    }).bind('dialogclose', function(event, ui) { getPrjDocumentIndexList(); })
    .dialogExtend({
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
    $(".pop_corrsDocNew").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: "auto"
    }).bind('dialogclose', function(event, ui) { getPrjDocumentIndexList(); })
    .dialogExtend({
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
	$(".pop_multiDownload").dialog({
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
	
	$(".pop_mngAppr").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000
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
	
	$(".pop_collaboW").dialog({
	    autoOpen: false,
	    maxWidth: 1200,
	    width: 1200
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
	
	$("#drn_fileDialog").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000
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
	
	$("#drnChkSheet_Pop").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000
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
	
	
	
	

	dcc_ready = true;
	folderChange();
	
	
	
	

    $.contextMenu({
        selector: '.documentControlTblWrap', 
        callback: function(key, options) {
        	contextMenuDC(key);
        },
        build: function($triggerElement, e){
			var items_obj = new Object();
			items_obj = { items : {} };
			
			
			// 제이쿼리 컨텍스트 메뉴
			
			
			let auth = checkFolderAuth(prj_id,current_folder_id)
			let r = (auth.r == 'Y');
	    	let w = (auth.w == 'Y');
	    	let d = (auth.d == 'Y');
	    		
			
			if(documentListTable.getSelectedData()[0]===undefined){
    			items_obj = {
    			        items: {
    			            "Open": {name: "Open", disabled: true},
    			            "Download": {name: "Download", disabled: true},
    			            "Edit": {name: "Edit", disabled: true},
    			            "Check-Out": {name: "Check-Out", disabled: true},
    			            "Check-In": {name: "Check-In", disabled: true},
    			            "Cancel Check-Out": {name: "Cancel Check-Out", disabled: true},
    			            "Property": {name: "Property", disabled: true},
    			            "Access History": {name: "Access History", disabled: true},
    			            "Select All": {name: "Select All", disabled: true},
    			            "Cancel Select": {name: "Cancel Select", disabled: true},
    			            "New": {name: "New",disabled: w ? false :true},
    			            "Delete": {name: "Delete", disabled: true},
    			            "Save document list(xls)": {name: "Save document list(xls)", disabled: true},
    			            "Send e-mail": {name: "Send e-mail", disabled: true},
    			        }
        			}
        		return items_obj;
    			//return 밖으로 빼지 말 것.
			}else{
	        	let rev_code_id = documentListTable.getSelectedData()[0].bean.rev_code_id;
	    		let ext = documentListTable.getSelectedData()[0].bean.file_type;
	    		let lock_yn = documentListTable.getSelectedData()[0].bean.lock_yn;
	    		var rowCheckOutId = documentListTable.getSelectedData()[0].bean.mod_id;
	    		var DCCArray = getPrjDCC();
	    		
	    		
	    		
	    		if(lock_yn==='Y' && (rowCheckOutId===session_user_id || DCCArray.indexOf(session_user_id)!=-1)){	
	    			//체크아웃한 당사자나 DCC일때 체크아웃 도서에 나타나는 컨텍스트 메뉴
		        	items_obj = {
	    				items: {
	    					"Open": {name: "Open", disabled: r ? false :true },
	    			        "Download": {name: "Download", disabled: r ? false :true},
	    			        "Edit": {name: "Edit", disabled: r ? false :true},
	    			        "Check-Out": {name: "Check-Out", disabled: true},
	    			        "Check-In": {name: "Check-In", disabled: true},
	    			        "Cancel Check-Out": {name: "Cancel Check-Out", disabled: false},
	    			        "Property": {name: "Property", disabled: r ? false :true},
	    			        "Access History": {name: "Access History", disabled: r ? false :true},
        			        "Select All": {name: "Select All", 
        			        	disabled: $("input[name='docListAll']")[0].checked? true : false},
        			        "Cancel Select": {name: "Cancel Select", 
        			        	disabled: $("input[name='docListAll']")[0].checked? false : true},
	    			        "New": {name: "New",disabled: w ? false :true},
	    			        "Delete": {name: "Delete", disabled: d ? false :true},
	    			        "Save document list(xls)": {name: "Save document list(xls)", disabled: r ? false :true},
	    			        "Send e-mail": {name: "Send e-mail", disabled: w ? false :true},
	    			    }
	    			}
	        		return items_obj;
	    		}
	    		if(lock_yn==='Y'){	//CheckOut 상태
	    			items_obj = {
	        				items: {
	        					"Open": {name: "Open", disabled: r ? false :true},
	        			        "Download": {name: "Download", disabled: r ? false :true},
	        			        "Edit": {name: "Edit", disabled: r ? false :true},
	        			        "Check-Out": {name: "Check-Out", disabled: true},
	        			        "Check-In": {name: "Check-In", disabled: true},
	        			        "Cancel Check-Out": {name: "Cancel Check-Out", disabled: true},
	        			        "Property": {name: "Property", disabled: r ? false :true},
	        			        "Access History": {name: "Access History", disabled: r ? false :true},
	        			        "Select All": {name: "Select All", 
	        			        	disabled: $("input[name='docListAll']")[0].checked? true : false},
	        			        "Cancel Select": {name: "Cancel Select", 
	        			        	disabled: $("input[name='docListAll']")[0].checked? false : true},
	        			        "New": {name: "New",disabled: w ? false :true},
	        			        "Delete": {name: "Delete", disabled: d ? false :true},
	        			        "Save document list(xls)": {name: "Save document list(xls)", disabled: r ? false :true},
	        			        "Send e-mail": {name: "Send e-mail", disabled: w ? false :true},
	        			    }
	        			}
	        		return items_obj;
	    		}
	    		if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){	//물음표 상태
	    			items_obj = {
	        				items: {
	        					"Open": {name: "Open", disabled: r ? false :true},
	        			        "Download": {name: "Download", disabled: r ? false :true},
	        			        "Edit": {name: "Edit", disabled: r ? false :true},
	        			        "Check-Out": {name: "Check-Out", disabled: true},
	        			        "Check-In": {name: "Check-In", disabled: w ? false :true},
	        			        "Cancel Check-Out": {name: "Cancel Check-Out", disabled: true},
	        			        "Property": {name: "Property", disabled: r ? false :true},
	        			        "Access History": {name: "Access History", disabled: r ? false :true},
	        			        "Select All": {name: "Select All", 
	        			        	disabled: $("input[name='docListAll']")[0].checked? true : false},
	        			        "Cancel Select": {name: "Cancel Select", 
	        			        	disabled: $("input[name='docListAll']")[0].checked? false : true},
	        			        "New": {name: "New", disabled: w ? false :true},
	        			        "Delete": {name: "Delete", disabled: d ? false :true},
	        			        "Save document list(xls)": {name: "Save document list(xls)", disabled: r ? false :true},
	        			        "Send e-mail": {name: "Send e-mail", disabled: w ? false :true},
	        			    }
	        			}
	        		return items_obj;
	    		}else{	//첨부 상태
		        	let checkInTF = w ? false :true;
		        	if(current_process_type==='E-MAIL' || current_process_type==='LETTER'){
		        		checkInTF = true;
			        }
	    			
	    			items_obj = {
	        				items: {
	        					"Open": {name: "Open", disabled: r ? false :true},
	        			        "Download": {name: "Download", disabled: r ? false :true},
	        			        "Edit": {name: "Edit", disabled: r ? false :true},
	        			        "Check-Out": {name: "Check-Out", disabled: w ? false :true},
	        			        "Check-In": {name: "Check-In", disabled: checkInTF},
	        			        "Cancel Check-Out": {name: "Cancel Check-Out", disabled: true},
	        			        "Property": {name: "Property", disabled: r ? false :true},
	        			        "Access History": {name: "Access History", disabled: r ? false :true},
	        			        "Select All": {name: "Select All", 
	        			        	disabled: $("input[name='docListAll']")[0].checked? true : false},
	        			        "Cancel Select": {name: "Cancel Select", 
	        			        	disabled: $("input[name='docListAll']")[0].checked? false : true},
	        			        "New": {name: "New", disabled: w ? false :true},
	        			        "Delete": {name: "Delete", disabled: d ? false :true},
	        			        "Save document list(xls)": {name: "Save document list(xls)", disabled: r ? false :true},
	        			        "Send e-mail": {name: "Send e-mail", disabled: w ? false :true},
	        			    }
	        			}
	        		return items_obj;
	    		}
			}
		}
    });
});
function contextMenuDC(key){
	if(key==="Open"){
    	let selectedArray = documentListTable.getSelectedData();
		if(selectedArray.length===0){
			alert('open할 도서를 선택해주세요.');
		}else{
			let sfile_nm = '';
			let lock_yn = '';
			for(let i=0;i<selectedArray.length;i++){
				lock_yn += selectedArray[i].bean.lock_yn;
			}
			if(lock_yn.indexOf('Y') !== -1){
				alert('잠금 상태가 아닌 도서를 선택하세요.');
			}else{
				for(let i=0;i<selectedArray.length;i++){
					if(DCCorTRA==='TRA' && selectedArray[i].bean.trans_ref_doc_id==='COVER'){
						location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm);
						fnSleep(1000);
					}else{
						location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.rfile_nm.replaceAll("&","@%@"));
						fnSleep(1000);
					}
				}

				let docid_revid='';
				for(let i=0;i<selectedArray.length;i++){
					docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
				}
				docid_revid = docid_revid.slice(0,-2);
				$.ajax({
				    url: 'readDocCheck.do',
				    type: 'POST',
				    data:{
						prj_id: selectPrjId,
					    docid_revid:docid_revid
				    },
				    success: function onData (data) {
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
			}
		}
    }else if(key==="Download"){
    	let selectedArray = documentListTable.getSelectedData();
		if(selectedArray.length===0){
			alert('다운로드 받을 도서를 선택해주세요.');
		}else{
			let multiDownTbl = [];
			let lock_yn = '';
			for(let i=0;i<selectedArray.length;i++){
				lock_yn += selectedArray[i].bean.lock_yn;
			}
			if(lock_yn.indexOf('Y') !== -1){
				alert('잠금 상태가 아닌 도서를 선택하세요.');
			}else{
				if(selectedArray.length===1 && selectedArray[0].bean.file_type===null){
					alert('해당 도서는 문서가 첨부되지 않았습니다.');
					return;
				}
				for(let i=0;i<selectedArray.length;i++){
					if(selectedArray[i].bean.file_type!==null){
						let rfile_nm = selectedArray[i].bean.rfile_nm;
						if(DCCorTRA==='TRA' && selectedArray[i].bean.trans_ref_doc_id==='COVER'){
							rfile_nm = selectedArray[i].bean.sfile_nm;
						}
						multiDownTbl.push({
							FileName:rfile_nm,
							RevNo:selectedArray[i].bean.rev_no,
							Title:selectedArray[i].bean.title,
							Size:formatBytes(selectedArray[i].bean.file_size,2),
							bean:selectedArray[i].bean
						});
					}
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
				}
				$(".pop_multiDownload").dialog("open");
				$('#downloadFileRename').prop('checked',false);
				$('#fileRenameSet').val('');
				//$('#fileMultiDownload').attr('onclick','fileMultiDownload()');
				

				let docid_revid='';
				for(let i=0;i<selectedArray.length;i++){
					docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
				}
				docid_revid = docid_revid.slice(0,-2);
				$.ajax({
				    url: 'readDocCheck.do',
				    type: 'POST',
				    data:{
						prj_id: selectPrjId,
					    docid_revid:docid_revid
				    },
				    success: function onData (data) {
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
				return false;
			}
		}
    }else if(key==="Edit"){
    	let selectedArray = documentListTable.getSelectedData();
		if(selectedArray.length===0){
			alert('Edit할 도서를 선택해주세요.');
		}else{
			let sfile_nm = '';
			for(let i=0;i<selectedArray.length;i++){
				let lock_yn = selectedArray[i].bean.lock_yn;
				if(lock_yn==='Y'){
					alert('도서가 잠금 상태입니다.');
				}else{
					location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.rfile_nm.replaceAll("&","@%@"));
					fnSleep(1000);
					
					let doc_id = selectedArray[i].bean.doc_id;
					let rev_id = selectedArray[i].bean.rev_id;
					let method = "EDIT";
					let doc_no = selectedArray[i].bean.doc_no;
					let doc_title = selectedArray[i].bean.title;
					let folder_id = selectedArray[i].bean.folder_id;
					let file_size = selectedArray[i].bean.file_size;
					//console.log(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
					documentCheckOut(doc_id,rev_id,doc_no,doc_title,folder_id,file_size,method);
					

					let docid_revid='';
					for(let i=0;i<selectedArray.length;i++){
						docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
					}
					docid_revid = docid_revid.slice(0,-2);
					$.ajax({
					    url: 'readDocCheck.do',
					    type: 'POST',
					    data:{
							prj_id: selectPrjId,
						    docid_revid:docid_revid
					    },
					    success: function onData (data) {
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}
			}
		}
    }else if(key==="Check-Out"){
    	let selectedArray = documentListTable.getSelectedData();
		if(selectedArray.length===0){
			alert('Check-Out할 도서를 선택해주세요.');
		}else{
			for(let i=0;i<selectedArray.length;i++){
				let doc_id = selectedArray[i].bean.doc_id;
				let rev_id = selectedArray[i].bean.rev_id;
				let method = "CHECK-OUT";
				let doc_no = selectedArray[i].bean.doc_no;
				let doc_title = selectedArray[i].bean.title;
				let folder_id = selectedArray[i].bean.folder_id;
				let file_size = selectedArray[i].bean.file_size;
				//console.log(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
				documentCheckOut(doc_id,rev_id,doc_no,doc_title,folder_id,file_size,method);
				//documentCheckOut(doc_id,rev_id,method);
			}
			
			let docid_revid='';
			for(let i=0;i<selectedArray.length;i++){
				docid_revid += selectedArray[i].bean.doc_id + '&&' + selectedArray[i].bean.rev_id + '&&' + selectedArray[i].bean.folder_id + '@@';
			}
			docid_revid = docid_revid.slice(0,-2);
			$.ajax({
			    url: 'readDocCheck.do',
			    type: 'POST',
			    data:{
					prj_id: selectPrjId,
				    docid_revid:docid_revid
			    },
			    success: function onData (data) {
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
		//alert('선택된 항목의 문서를 수정하기 위해 Lock 상태로 전환 (Check-in 한 문서의 수정이 필요하여 잠금상태로 변경하는 기능 - 잠금을 풀기전에는 다운로드, 수정, Transmittal 송부 안됨)');
    }else if(key==="Check-In"){
    	// alert('별도 Check-in 화면 팝업, 리비전, 필수값 입력, 첨부하는 화면 (문서에 수정된 내용/첨부를 반영, 새 리비전 생성할 때 사용하는 기능)');
		popCheckIn();
    }else if(key==="Cancel Check-Out"){
    	let selectedArray = documentListTable.getSelectedData();
		if(selectedArray.length===0){
			alert('Cancel Check-Out할 도서를 선택해주세요.');
		}else{
			for(let i=0;i<selectedArray.length;i++){
				let doc_id = selectedArray[i].bean.doc_id;
				let rev_id = selectedArray[i].bean.rev_id;
				let doc_no = selectedArray[i].bean.doc_no;
				let doc_title = selectedArray[i].bean.title;
				let folder_id = selectedArray[i].bean.folder_id;
				let file_size = selectedArray[i].bean.file_size;
				//console.log(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
				documentCheckOutCancel(doc_id,rev_id,doc_no,doc_title,folder_id,file_size);
			}
		}
    }else if(key==="Property"){
    	let row = documentListTable.getSelectedRows()[0];
    	contextmenuProperty(row);

		let docid_revid=row.getData().bean.doc_id + '&&' + row.getData().bean.rev_id + '&&' + row.getData().bean.folder_id;
		$.ajax({
		    url: 'readDocCheck.do',
		    type: 'POST',
		    data:{
				prj_id: selectPrjId,
			    docid_revid:docid_revid
		    },
		    success: function onData (data) {
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
    }else if(key==="Access History"){
    	getAccessHisList();
    }else if(key==="Select All"){
    	$("input[name='docListAll']")[0].checked = true;
    	documentListTable.selectRow("visible");
    	this.data('Cancel Select', !this.data('Cancel Select'));
        return false;
    }else if(key==="Cancel Select"){
    	$("input[name='docListAll']")[0].checked = false;
    	documentListTable.deselectRow();
    	this.data('Cancel Select', !this.data('Cancel Select'));
        return false;
    }else if(key==="New"){
    	let selectedArray = documentListTable.getSelectedData()[0];
    	let row = documentListTable.getSelectedRows()[0];
    	if(selectedArray){
    		contextmenuNewByProcessType();
    		//contextmenuNew(row);
    	}else{
        	contextmenuNewByProcessType();
    	}
    }else if(key==="Delete"){
		var doc_id = documentListTable.getSelectedData()[0].bean.doc_id;
		var rev_id = documentListTable.getSelectedData()[0].bean.rev_id;
		if(documentListTable.getSelectedRows().length===0){
			alert('Choose a document.');
		}else{
		    $(".pop_documentDeleteConfirm").dialog('open');
			$('#deleteConfirmMessage').html("Would you like to delete documents? (If you delete document, it can't restore)");
			var doc_type = documentListTable.getSelectedData()[0].bean.doc_type;
			$('#documentDeleteButton').attr('onclick','documentDelete(`'+doc_type+'`)');
		}
    }else if(key==="Save document list(xls)"){
    	//alert('해당 폴더의 DCI Index 리스트만 엑셀로 다운로드 하는 기능 (템플릿 별도제공)');
		
		var select_rev_version = $('#select_rev_version').val();
		var unread_yn=$('#unread_yn').is(':checked')==true?'Y':'N';
		
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
    }else if(key==="Send e-mail"){
    	let selectedArray = documentListTable.getSelectedData();
		if(selectedArray.length===0){
			alert('Send e-mail할 도서를 선택해주세요.');
		}else{
			$(".pop_mailNoticeDC").dialog("open");
			getDCEmailList();
	    	let dc_receiver_list = $('#dc_receiver_list').eq(0).data('selectize');
	    	dc_receiver_list.setValue(null,false);
			let docid_revid_docno_revno = '';
			let docTable = '<p></p><br><table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
			for(let i=0;i<selectedArray.length;i++){
				let doc_id = selectedArray[i].bean.doc_id;
				let rev_id = selectedArray[i].bean.rev_id;
		    	docid_revid_docno_revno += doc_id + '&&' + rev_id + '&&' + selectedArray[i].bean.doc_no +'&&'+selectedArray[i].bean.rev_no+'@@';
		    	let downloadFile = doc_id + '_' + rev_id + '.' + selectedArray[i].bean.file_type;
				docTable += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.rfile_nm)+'&email_type_id='+encodeURIComponent(0)+'&email_id=@@email_id_date@@">'+selectedArray[i].bean.doc_no+'</a></td><td>'+selectedArray[i].bean.rev_no+'</td><td>'+selectedArray[i].bean.title+'</td><td>'+selectedArray[i].bean.file_type+'</td><td>'+formatBytes(selectedArray[i].bean.file_size,2)+'</td></tr>'
			}
			docid_revid_docno_revno = docid_revid_docno_revno.slice(0,-2);
	    	DC.docid_revid_docno_revno = docid_revid_docno_revno;
			
			docTable += '</tbody></table><p></p><br>';
	    	$('#summernoteMailDC').summernote("code", docTable);
		}
    	//alert('멀티선택된 항목을 메일 수신자가 다운로드 받을 수 있도록 아웃룩이 실행되면서 본문에 삽입 (템플릿 별도 제공)');
    }
}
function getDCEmailList(){
	$.ajax({
	    url: 'getAllUsersVOList.do',
	    type: 'POST',
	    async:false,
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		init_email_list.push(data[0][i].email_addr);
	    		html += '<option value="'+data[0][i].email_addr+'" label="'+data[0][i].user_kor_nm+'">'+data[0][i].user_kor_nm+'('+data[0][i].email_addr+')'+'</option>';
	    	}
	    	$('#dc_receiver_list').html(html);
	    	$('#dc_referto_list').html(html);
	    	$('#dc_bcc_list').html(html);
	    	
	    	$("#dc_receiver_list").selectize({
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
	    	$("#dc_referto_list").selectize({
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
	    	$("#dc_bcc_list").selectize({
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
function sendEmailCloseDC(){
	$('.pop_mailNoticeDC').dialog('close');
}
function sendEmailConfrimDC(){
	$(".pop_SendEmailDC").dialog('open');
}
function sendEmailCancelDC(){
	$(".pop_SendEmailDC").dialog('close');
}
function sendEmailDC(){
	let user_data00 = DC.user_data00;
	let subject = $('#DC_email_subject').val();
	let contents = $('#summernoteMailDC').summernote("code");
	let email_sender_addr = $('#dc_sender_list').val();
	let email_receiver_addr = $('#dc_receiver_list').val().join(';');
	let email_refer_to = $('#dc_referto_list').val().join(';');
	let email_bcc = $('#dc_bcc_list').val().join(';');

	if(!email_check(email_sender_addr)){
		alert("SENDER E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. SENDER는 하나의 메일주소만 설정할 수 있습니다.");
		return false;
	}
	let email_receiver_array = email_receiver_addr.split(';');
	for(let i=0;i<email_receiver_array.length;i++){
		if(!email_check(email_receiver_array[i])){
			alert("RECEIVER E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
			return false;
		}
	}
	let email_referto_array = email_refer_to.split(';');
	for(let i=0;i<email_referto_array.length;i++){
		if(!email_check(email_referto_array[i])){
			alert("REFERTO E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
			return false;
		}
	}
	let email_bcc_array = email_bcc.split(';');
	for(let i=0;i<email_bcc_array.length;i++){
		if(!email_check(email_bcc_array[i])){
			alert("BCC E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
			return false;
		}
	}
	if(email_sender_addr===null || email_sender_addr==='' || email_sender_addr===undefined){
		alert('Sender를 입력해야합니다.');
	}else if(email_receiver_addr===null || email_receiver_addr==='' || email_receiver_addr===undefined){
		alert('Receiver를 설정해주세요.');
	}else if(subject===null || subject==='' || subject===undefined){
		alert('Subject를 입력해주세요.');
	}else{
		$.ajax({
			type: 'POST',
			url: 'insertPrjEmailDC.do',
			data:{
				prj_id:selectPrjId,
				subject:subject,
				contents:contents,
				email_sender_addr:email_sender_addr,
				email_receiver_addr:email_receiver_addr,
				email_refer_to:email_refer_to,
				email_bcc:email_bcc,
				email_type_id:0,
				user_data00:user_data00,
				docid_revid_docno_revno:DC.docid_revid_docno_revno
			},
			success: function(data) {
				if(data[0]>0){
					alert('E-mail sent successfully');
					$(".pop_SendEmailDC").dialog('close');
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

function documentControlDownload(){
	let selectedArray = documentListTable.getSelectedData();
	if(selectedArray.length===0){
		alert('다운로드 받을 도서를 선택해주세요.');
	}else{
		let multiDownTbl = [];
		let lock_yn = '';
		for(let i=0;i<selectedArray.length;i++){
			lock_yn += selectedArray[i].bean.lock_yn;
		}
		if(lock_yn.indexOf('Y') !== -1){
			alert('잠금 상태가 아닌 도서를 선택하세요.');
		}else{
			if(selectedArray.length===1 && selectedArray[0].bean.file_type===null){
				alert('해당 도서는 문서가 첨부되지 않았습니다.');
				return;
			}
			for(let i=0;i<selectedArray.length;i++){
				if(selectedArray[i].bean.file_type!==null){
					multiDownTbl.push({
						FileName:selectedArray[i].bean.rfile_nm,
						RevNo:selectedArray[i].bean.rev_no,
						Title:selectedArray[i].bean.title,
						Size:formatBytes(selectedArray[i].bean.file_size,2),
						bean:selectedArray[i].bean
					});
				}
			}
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
			//$('#fileMultiDownload').attr('onclick','fileMultiDownload()');
			return false;
		}
	}
}
function folderChange(){
	if(current_folder_id!=='' && current_folder_id!==null && current_folder_id!==undefined)	//TRoutgoing페이지에서 documentContorl.js 로드할 때 해당함수 호출하는 것을 방지하기 위함
		getPrjDocumentIndexList();
}
function contextmenuNew(row){
	
	
	let auth = checkFolderAuth(prj_id,current_folder_id);
	if(auth.w == 'N'){
		alert("There is no WRITE permission for this folder");
		return;
	}
	
	popup_new_or_update = 'new';
	vendor_new_or_update = 'new';
	proc_new_or_update = 'new';
	bulk_new_or_update = 'new';
	site_new_or_update = 'new';
	corr_new_or_update = 'new';
	general_new_or_update = 'new';
	var doc_type = row.getData().bean.doc_type;
	var doc_id = row.getData().bean.doc_id;
	var rev_id = row.getData().bean.rev_id;
	$('#session_user_id').val(session_user_id);
	$('#session_user_eng_nm').val(session_user_eng_nm);
	$('#current_folder_discipline_code').val(current_folder_discipline_code);
	$('#current_folder_discipline_code_id').val(current_folder_discipline_code_id);
	$('#current_folder_discipline_display').val(current_folder_discipline_display);
	$('#current_folder_id').val(current_folder_id);
	$('#current_folder_path').val(current_folder_path);
	$('#process').val('new');
	$('#doc_type').val(doc_type);
	$('#DCCorTRA').val(DCCorTRA);
	$('#popup_new_or_update').val(popup_new_or_update);
	$('#vendor_new_or_update').val(vendor_new_or_update);
	$('#proc_new_or_update').val(proc_new_or_update);
	$('#bulk_new_or_update').val(bulk_new_or_update);
	$('#site_new_or_update').val(site_new_or_update);
	$('#corr_new_or_update').val(corr_new_or_update);
	$('#general_new_or_update').val(general_new_or_update);
	let useDRNYN = useDrnCheckForThisFolder(selectPrjId,current_folder_id);
	$('#useDRNYN').val(useDRNYN);
	
	if(doc_type==='DCI'){
		let engDocPopup = window.open('./pop/engDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='VDCI'){
		let vendorDocPopup = window.open('./pop/vendorDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='PCI'){
		let procDocPopup = window.open('./pop/procDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='MCI'){
		let bulkDocPopup = window.open('./pop/bulkDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='SDCI'){
		let siteDocPopup = window.open('./pop/siteDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='LETTER' || doc_type==='E-MAIL'){
		let selectedPrjNo = getPrjNo();
		$('#selectedPrjNo').val(selectedPrjNo);
		let corrDocPopup = window.open('./pop/corrDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes'); 
	}else{//doc_type==='General'
		let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes'); 
	}
}
function contextmenuNewByProcessType(){
	
	popup_new_or_update = 'new';
	vendor_new_or_update = 'new';
	proc_new_or_update = 'new';
	bulk_new_or_update = 'new';
	site_new_or_update = 'new';
	corr_new_or_update = 'new';
	general_new_or_update = 'new';
	$('#session_user_id').val(session_user_id);
	$('#session_user_eng_nm').val(session_user_eng_nm);
	$('#current_folder_discipline_code').val(current_folder_discipline_code);
	$('#current_folder_discipline_code_id').val(current_folder_discipline_code_id);
	$('#current_folder_discipline_display').val(current_folder_discipline_display);
	$('#current_folder_id').val(current_folder_id);
	$('#current_folder_path').val(current_folder_path);
	$('#process').val('new');
	$('#doc_type').val(doc_type);
	$('#DCCorTRA').val(DCCorTRA);
	$('#popup_new_or_update').val(popup_new_or_update);
	$('#vendor_new_or_update').val(vendor_new_or_update);
	$('#proc_new_or_update').val(proc_new_or_update);
	$('#bulk_new_or_update').val(bulk_new_or_update);
	$('#site_new_or_update').val(site_new_or_update);
	$('#corr_new_or_update').val(corr_new_or_update);
	$('#general_new_or_update').val(general_new_or_update);
	let useDRNYN = useDrnCheckForThisFolder(selectPrjId,current_folder_id);
	$('#useDRNYN').val(useDRNYN);
	if($('#docSelTree_doc_type').val()!==''){//TR OUTGOING 페이지에서 add Doc 했을 때 나오는 팝업의 경우 탭 이동하면서 섞이는 문제때문에 current_doc_type으로 변수처리 하지 않고 if else로 나눔
		if($('#docSelTree_doc_type').val()==='DCI'){
			var engDocPopup = window.open('./pop/engDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if($('#docSelTree_doc_type').val()==='VDCI'){
			var vendorDocPopup = window.open('./pop/vendorDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if($('#docSelTree_doc_type').val()==='PCI'){
			let procDocPopup = window.open('./pop/procDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if($('#docSelTree_doc_type').val()==='MCI'){
			let bulkDocPopup = window.open('./pop/bulkDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if($('#docSelTree_doc_type').val()==='SDCI'){
			let siteDocPopup = window.open('./pop/siteDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if($('#docSelTree_doc_type').val()==='Corr'){
			let selectedPrjNo = getPrjNo();
			$('#selectedPrjNo').val(selectedPrjNo);
			let corrDocPopup = window.open('./pop/corrDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes'); 
		}else{//$('#docSelTree_doc_type').val()==='' or 'General'
			let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes'); 
		}
	}else{
		if(current_doc_type==='DCI'){
			var engDocPopup = window.open('./pop/engDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if(current_doc_type==='VDCI'){
			var vendorDocPopup = window.open('./pop/vendorDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if(current_doc_type==='PCI'){
			let procDocPopup = window.open('./pop/procDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if(current_doc_type==='MCI'){
			let bulkDocPopup = window.open('./pop/bulkDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if(current_doc_type==='SDCI'){
			let siteDocPopup = window.open('./pop/siteDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}else if(current_doc_type==='Corr' && current_folder_path.toLowerCase().indexOf('trans')==-1){
			let selectedPrjNo = getPrjNo();
			$('#selectedPrjNo').val(selectedPrjNo);
			let corrDocPopup = window.open('./pop/corrDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes'); 
		}else{//current_folder_docType==='' or 'General'
			let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes'); 
		}
	}
}
function  contextmenuProperty(row,tr_id,folder_id,tradd_attach,tradd_doc_no,tradd_rev_no,tradd_title){
	
	if(tr_id){
		current_folder_id = folder_id;
		DCCorTRA = 'DCC';
	}
	if(!tr_id){
		tr_id='';
	}
	if(tr_id!=='TRADD'){
		tradd_attach='';
		tradd_doc_no='';
		tradd_rev_no='';
		tradd_title='';
	}
	popup_new_or_update = 'update';
	vendor_new_or_update = 'update';
	proc_new_or_update = 'update';
	bulk_new_or_update = 'update';
	site_new_or_update = 'update';
	corr_new_or_update = 'update';
	general_new_or_update = 'update';
	var doc_type = row.getData().bean.doc_type;
	var doc_id = row.getData().bean.doc_id;
	var rev_id = row.getData().bean.rev_id;
	var trans_ref_doc_id = row.getData().bean.trans_ref_doc_id;//TR OUTGOING 한 도서들
	// folder의 doc type과 다르게 general문서가 업로드된 경우
	// 해당 도서의 doc_id를 정해주기 위함
	general_selected_doc_id = doc_id;
	$('#session_user_id').val(session_user_id);
	$('#session_user_eng_nm').val(session_user_eng_nm);
	$('#current_folder_discipline_code').val(current_folder_discipline_code);
	$('#current_folder_discipline_code_id').val(current_folder_discipline_code_id);
	$('#current_folder_discipline_display').val(current_folder_discipline_display);
	$('#current_folder_id').val(current_folder_id);
	$('#process').val('property');
	$('#doc_type').val(doc_type);
	$('#doc_id').val(doc_id);
	$('#rev_id').val(rev_id);
	$('#trans_ref_doc_id').val(trans_ref_doc_id);
	$('#current_folder_path').val(current_folder_path);
	$('#DCCorTRA').val(DCCorTRA);
	$('#popup_new_or_update').val(popup_new_or_update);
	$('#vendor_new_or_update').val(vendor_new_or_update);
	$('#proc_new_or_update').val(proc_new_or_update);
	$('#bulk_new_or_update').val(bulk_new_or_update);
	$('#site_new_or_update').val(site_new_or_update);
	$('#corr_new_or_update').val(corr_new_or_update);
	$('#general_new_or_update').val(general_new_or_update);
	let useDRNYN = useDrnCheckForThisFolder(selectPrjId,current_folder_id);
	$('#useDRNYN').val(useDRNYN);
	$('#tr_id').val(tr_id);
	$('#tradd_attach').val(tradd_attach);
	$('#tradd_doc_no').val(tradd_doc_no);
	$('#tradd_rev_no').val(tradd_rev_no);
	$('#tradd_title').val(tradd_title);
	if(doc_type==='DCI'){
		let engDocPopup = window.open('./pop/engDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='VDCI'){
		let vendorDocPopup = window.open('./pop/vendorDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='PCI'){
		let procDocPopup = window.open('./pop/procDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='MCI'){
		let bulkDocPopup = window.open('./pop/bulkDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='SDCI'){
		let siteDocPopup = window.open('./pop/siteDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else if(doc_type==='LETTER' || doc_type==='E-MAIL'){
		let selectedPrjNo = getPrjNo();
		$('#selectedPrjNo').val(selectedPrjNo);
		let corrDocPopup = window.open('./pop/corrDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
	}else{//doc_type==='General'
		if(trans_ref_doc_id=='COVER'){//TR COVER
		    let adminAndDCCArray = [];
			  $.ajax({
				    url: 'getAdminAndDCCList.do',
				    type: 'POST',
				    data:{
				    	prj_id:selectPrjId
				    },
				    async:false,
				    success: function onData (data) {
					    let adminAndDCCString = '';
				    	adminAndDCCString = data[0];
				    	adminAndDCCArray = adminAndDCCString.split(',');
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
		    
			let session_user_id = $('#session_user_id').val();
		    if(adminAndDCCArray.includes(session_user_id)){	//Admin이나 DCC일 때만 COVER파일 수정가능
				//trans_ref_doc_id = row.getData().bean.doc_id;
				$('#trans_ref_doc_id').val(trans_ref_doc_id);
				let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
			}else{
				alert('COVER 파일은 Property를 확인할 수 없습니다.');
		    }
			
		}else{
			let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}
	}
	
	if(tr_id){
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
    			imgIcon: attach,
                docNo: doc_no,
                revNo: rev_no,
                title: title
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

function useDrnCheckForThisFolder(prj_id,folder_id){
	
	let useDrn = 'N';
	$.ajax({
		url: 'useDrnCheckForThisFolder.do',
		type: 'POST',
		traditional : true,
		async: false,
		data:{
			prj_id: prj_id,
			folder_id: folder_id
		},
		success: function onData (data) {
			useDrn = data;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	return useDrn;
}
function getPrjDocumentIndexList(documentselect,popup_folder_id){
	// 전체선택 체크박스 체크여부 클리어
	$("input[name='docListAll']").prop('checked', false);
		
	
	let folderIdx = folderList.findIndex(i => i.id == current_folder_id);
	let drnBtn = $("#drnUpBtn")[0];
	
	if(drnBtn){
		// 기본적으로는 보여줌
		//drnBtn.style["display"] = "";
		
		$.ajax({
			url: 'useDrnCheckForThisFolder.do',
			type: 'POST',
			traditional : true,
			async: false,
			data:{
				prj_id: selectPrjId,
				folder_id: current_folder_id
			},
			success: function onData (data) {
				if(data == 'Y'){
					drnBtn.style["display"] = "";
					$('#drnUseYN').html('DRN이 적용된 폴더입니다.');
				}else{
					drnBtn.style["display"] = "none";
					$('#drnUseYN').html('');
				}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		
		
		
	}
	
	var select_rev_version;
	var unread_yn;
	if(documentselect==='documentselectpopup'){
		select_rev_version = $('#docsel_select_rev_version').val();
		unread_yn=$('#docsel_unread_yn').is(':checked')==true?'Y':'N';
		let useDRNYN = useDrnCheckForThisFolder(selectPrjId,popup_folder_id);
		if(useDRNYN==='Y'){
			alert('DRN 적용 폴더의 도서는 TR Ougoing을 직접 등록할 수 없습니다.');
			documentListTable.setData([]);
			return false;
		}
		current_folder_id = popup_folder_id;
		if(documentListTable!==undefined){
			documentListTable.deselectRow();
		}
	}else if(documentselect==='drnSystemFile'){
		select_rev_version = $('#drnFile_docsel_select_rev_version').val();
		unread_yn=$('#drnFile_docsel_unread_yn').is(':checked')==true?'Y':'N';
		current_folder_id = popup_folder_id;
	}else{
		select_rev_version = $('#select_rev_version').val();
		unread_yn=$('#unread_yn').is(':checked')==true?'Y':'N';
	}
	$.ajax({
	    url: 'getPrjDocumentIndexList.do',
	    type: 'POST',
	    /*
	    beforeSend:function(){
		    $('body').prepend(documentControl_loading);
		 },
		complete:function(){
		    $('#documentControl_loading').remove();
		 },
		 */
	    async:false,
	    data:{
	    	prj_id: selectPrjId,
	    	folder_id: current_folder_id,
	    	select_rev_version: select_rev_version,
	    	unread_yn: unread_yn
	    },
	    success: function onData (data) {
	    	var doc_ctrl_li = [];
	    	if(data[0]!=undefined){
	    		$('#DCRowCount').html(data[0].length+' documents are remaining');
		    	for(i=0;i<data[0].length;i++){
		    		var Modified='';
		    		if(data[0][i].mod_date!=null){
		    			Modified = getDateFormat(data[0][i].mod_date);
		    		}
		    		var rev_code_id = data[0][i].rev_code_id;
		    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
		    		var reg_id = data[0][i].reg_id;
		    		var mod_id = data[0][i].mod_id;
		    		var ext = data[0][i].file_type;
		    		var designer = data[0][i].designer;
		    		var imgIcon = '';
					if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/question-mark.png' class='i'>";
					}else if(ext == "png" || ext == "jpg" || ext == "jpeg" || ext == "gif"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/picture.png' class='i'>";
					}else if(ext == "pdf"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/pdf.ico' class='i'>";
					}else if(ext == "ppt" || ext == "pptx"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/ppt.ico' class='i'>";
					}else if(ext == "doc" || ext == "docx"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/doc.ico' class='i'>";
					}else if(ext == "xls" || ext == "xlsx" || ext == "csv"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/xls.ico' class='i'>";
					}else if(ext == "txt"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/txt.ico' class='i'>";
					}else if(ext == "hwp"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/hwp.ico' class='i'>";
					}else if(ext == "dwg"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/dwg.ico' class='i'>";
					}else if(ext == "zip"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/zip.ico' class='i'>";
					}else{
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/general.ico' class='i'>";
					}
					if(lock_yn==='Y' && mod_id===session_user_id){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/checkout.ico' class='i'>";
					}else if(lock_yn==='Y' && mod_id!=session_user_id){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/lock.ico' class='i'>";
					}
			    	
		    		doc_ctrl_li.push({
		    			imgIcon: imgIcon,
		    			status: data[0][i].status,
	                    docNo: data[0][i].doc_no,
	                    category:data[0][i].category,
	                    Revision: data[0][i].rev_no,
	                    Title: data[0][i].title,
	                    designer:data[0][i].designer,
	                    Modified: Modified,
	                    Size: formatBytes(data[0][i].file_size, 2),
	                    bean: data[0][i]
		    		});
		    	}
		    	setPrjDocumentIndexList(doc_ctrl_li,documentselect);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjDocumentIndexList(doc_ctrl_li,documentselect){
	
	
	let tableId = '';
	if(documentselect==='documentselectpopup'){
		tableId = '#docSelTbl';
	}else if(documentselect==='drnSystemFile'){
		tableId = "#drnSystemFileGrid";
	}else{
		tableId = '#doc_ctrl_li';
	}
	documentListTable = new Tabulator(tableId, {
		data: doc_ctrl_li,
		layout: "fitColumns",
    	rowContextMenu: setRowContextMenu, //add context menu to rows
		height: "100%",
        placeholder:"No Data Set",
		selectable: true,
		selectableRangeMode:"click",
		columns: [{formatter:"rowSelection", title:"", hozAlign:"center", headerSort:false,width:1},
	        {
	            title: "Doc",
	            field: "imgIcon",
	            formatter : "html",
				width : 48
	        },
	        {
	            title: "Status",
	            field: "status",
				width : 60
	        },
	        {
				title: "Doc. No",
				field: "docNo",
				hozAlign: "left",
				tooltip:true,
				widthGrow : 3
			},
			{
				title: "Category",
				field: "category",
				visible:false,
				width : 100
			},
			{
				title: "Revision",
				field: "Revision",
				width : 70
			},
			{
				title: "Title",
				field: "Title",
				hozAlign: "left",
				tooltip:true,
				widthGrow : 5
			},
			{
				// process를 타는 부분에서만 visible 해줘야함
				title: "Designer",
				field: "designer",
				width : 120,
				visible:true
			},
			{
				title: "Modified",
				field: "Modified",
				width : 120
			},
			{
				title: "Size",
				field: "Size",
				hozAlign: "right",
				width: 75
			}
		],
	});

	var select_rev_version;
	if(documentselect==='documentselectpopup'){
		select_rev_version = $('#docsel_select_rev_version').val();
	}else if(documentselect==='drnSystemFile'){
		select_rev_version = $('#drnFile_docsel_select_rev_version').val();
	}else{
		select_rev_version = $('#select_rev_version').val();
	}
	
	documentListTable.on("tableBuilt", function(){
		var status = documentListTable.getColumn("status");
		var Revision = documentListTable.getColumn("Revision");
		if(select_rev_version==='current' || current_process_type==='E-MAIL' || current_process_type==='LETTER'){
			status.hide();
		}else{
			status.show();
		}
		
		if(current_process_type==='E-MAIL' || current_process_type==='LETTER'){
			Revision.hide();
		}else{
			Revision.show();
		}
		
		
		// 프로세스를 타지 않는 경우 designer 필드를 가려줌
		if(current_process_type == ''){
			documentListTable.getColumn("designer").updateDefinition({visible:false});
		}else{
			documentListTable.getColumn("designer").updateDefinition({visible:true});
		}
		
		if(current_doc_type == 'DCI' || current_doc_type == 'VDCI'){
			documentListTable.getColumn("category").updateDefinition({visible:true});
		}else{
			documentListTable.getColumn("category").updateDefinition({visible:false});
		}
		
		
		if(isFromHome){ // home에서 들어온 경우
			isFromHome = false; // 해당 플래그를 꺼준다.
			
			let tmp = documentListTable.getData();
			let tmp2 = tmp.filter(i=> i.bean.reg_date.substr(0,8) >= filter_date.substr(0,8));
			console.log("before",tmp,filter_date);
			
			if(isCrDelay){
				console.log("before-",tmp2);
				$.ajax({
				    url: 'getCrDelayList.do',
				    type: 'POST',
				    async:false,
				    data:{
				    	prj_id :prj_id,
				    	folder_id : current_folder_id
				    },
				    success: function onData (data) {
				    	let result = data.model.delayList;
				    	
				    	let tmp2arr = [];
				    	for(let idx=0 ; idx < result.length ; idx++){
				    		let tmp3 = tmp2.find(i => (i.bean.doc_id == result[idx].doc_id && i.bean.rev_id == result[idx].rev_id) );
				    		tmp2arr.push(tmp3);	
				    	}
				    	tmp2 = tmp2arr;
				    	isCrDelay = false;
				    },
				    
				    error: function onError (error) {
				        console.error(error);
				    }
				});
			}
			console.log("after",tmp2);
			documentListTable.setData(tmp2);
			
			
		}

		documentListTable.setSort(currentSort);
		
	});

	documentListTable.on("rowDblClick",function(e,row){
		location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(row.getData().bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(row.getData().bean.doc_no.replaceAll("&","@%@")+'.'+row.getData().bean.file_type);

	});
	
	function setRowContextMenu(row) {
		let auth = checkFolderAuth(prj_id,current_folder_id)
		let r = (auth.r == 'Y');
	    let w = (auth.w == 'Y');
	    let d = (auth.d == 'Y');
		
		let rev_code_id = row.getData().bean.rev_code_id;
		let ext = row.getData().bean.file_type;
		let lock_yn = row.getData().bean.lock_yn;
		var rowCheckOutId = row.getData().bean.mod_id;
		var DCCArray = getPrjDCC();
		if(lock_yn==='Y' && (rowCheckOutId===session_user_id || DCCArray.indexOf(session_user_id)!=-1)){
			//체크아웃한 당사자나 DCC일때 체크아웃 도서에 나타나는 컨텍스트 메뉴
			contextMenu[3].disabled = true;
			contextMenu[4].disabled = true;
			contextMenu[5].disabled = false;
			return contextMenu;
		}
		if(lock_yn==='Y'){	//CheckOut 상태
			contextMenu[3].disabled = true;
			contextMenu[4].disabled = true;
			contextMenu[5].disabled = true;
			return contextMenu;
		}
		if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){	//물음표 상태
			contextMenu[0].disabled = true;
			contextMenu[1].disabled = true;
			contextMenu[2].disabled = true;
			contextMenu[3].disabled = true;
			contextMenu[4].disabled = w ? false :true;
			contextMenu[5].disabled = true;
			contextMenu[6].disabled = r ? false :true;
			contextMenu[7].disabled = r ? false :true;
			// select all
			let isSelectAll = $("input[name='docListAll']")[0].checked;
			contextMenu[8].disabled = isSelectAll ? true : false;
			// cancel select
			contextMenu[9].disabled = isSelectAll ? false : true;
			
			contextMenu[10].disabled = w ? false :true;
			contextMenu[11].disabled = d ? false :true;
			contextMenu[12].disabled = r ? false :true;
			contextMenu[13].disabled = w ? false :true;

			if(current_process_type==='E-MAIL' || current_process_type==='LETTER'){
				contextMenu[4].disabled = true;
			}
			return contextMenu;
		}else{	//첨부 상태
			// open
			contextMenu[0].disabled = r ? false :true;
			// download
			contextMenu[1].disabled = r ? false :true;
			// edit
			contextMenu[2].disabled = r ? false :true;
			// check-out
			contextMenu[3].disabled = w ? false :true;
			// check-in
			contextMenu[4].disabled = w ? false :true;
			// cancel-check-out
			contextMenu[5].disabled = true;
			// property
			contextMenu[6].disabled = r ? false :true;
			// access history
			contextMenu[7].disabled = r ? false :true;
			// select all
			let isSelectAll = $("input[name='docListAll']")[0].checked;
			contextMenu[8].disabled = isSelectAll ? true : false;
			// cancel select
			contextMenu[9].disabled = isSelectAll ? false : true;
			// new
			contextMenu[10].disabled = w ? false :true;
			// del
			contextMenu[11].disabled = d ? false :true;
			// save xls
			contextMenu[12].disabled = r ? false :true;
			// send
			contextMenu[13].disabled = w ? false :true;
			
			if(current_process_type==='E-MAIL' || current_process_type==='LETTER'){
				contextMenu[4].disabled = true;
			}
			
			
			
			
			return contextMenu;
		}
    }
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
	//잠시 주석해 둠. 살려야 하는 기능!
	/*if(refresh){
		if(refresh!=='refresh'){
	    	if(refresh.getData().bean.folder_id===0){
	    		troutproperty=1;
	    	}
		}
	}*/
	$.ajax({
	    url: 'getPrjDocumentIndexList.do',
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
	    		if(data[0][i].mod_date!=null){
	    			Modified = getDateFormat(data[0][i].mod_date);
	    		}
	    		var rev_code_id = data[0][i].rev_code_id;
	    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
	    		var reg_id = data[0][i].reg_id;
	    		var mod_id = data[0][i].mod_id;
	    		var ext = data[0][i].file_type;
	    		var imgIcon = '';
	    		if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/question-mark.png' class='i'>";
				}else if(ext == "png" || ext == "jpg" || ext == "jpeg" || ext == "gif"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/picture.png' class='i'>";
				}else if(ext == "pdf"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/pdf.ico' class='i'>";
				}else if(ext == "ppt" || ext == "pptx"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/ppt.ico' class='i'>";
				}else if(ext == "doc" || ext == "docx"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/doc.ico' class='i'>";
				}else if(ext == "xls" || ext == "xlsx" || ext == "csv"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/xls.ico' class='i'>";
				}else if(ext == "txt"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/txt.ico' class='i'>";
				}else if(ext == "hwp"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/hwp.ico' class='i'>";
				}else if(ext == "dwg"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/dwg.ico' class='i'>";
				}else if(ext == "zip"){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/zip.ico' class='i'>";
				}else{
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/general.ico' class='i'>";
				}
				if(lock_yn==='Y' && mod_id===session_user_id){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/checkout.ico' class='i'>";
				}else if(lock_yn==='Y' && mod_id!=session_user_id){
					imgIcon = "<img src='"+context_path+"/resource/images/docicon/lock.ico' class='i'>";
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
        placeholder:"No Data Set",
		height: 400,
        selectable:1,//true
		columns: [
			{
	            title: "Doc",
	            field: "imgIcon",
	            formatter : "html",
				widthGrow : 1,
				frozen:true
	        },
	        {
				title: "Doc. No",
				field: "docNo",
				hozAlign: "left",
				widthGrow : 4,
				frozen:true
			},
	        {
				title: "Rev",
				field: "revNo",
				widthGrow : 2
			},
	        {
				title: "Title",
				field: "title",
				widthGrow : 4
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
			}else if(DCCorTRA==='TRA'){
				vendor_selected_doc_id = trans_ref_doc_id;
			}
			vendor_selected_rev_id = rev_id;
	    	getVendorDocumentInfo(vendor_selected_doc_id,vendor_selected_rev_id);
	    	getRevNoMultiBox('VDCI',doc_id);
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
function getDocumentInfo(doc_id,rev_id){
	$('#engFileAttachTr').css('display','table-row');
	popup_new_or_update = 'update';
	$.ajax({
	    url: 'getDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id,
	    	inout:''
	    },
	    success: function onData (data) {
	    	$('#revNoSelected').css('pointer-events', 'auto');
	    	$('#revNoSelected').css('background', 'url(resource/images/select.png) no-repeat right center #fff');
	    	$('#revNoSelected').css('background-color', '#fff');
			//$('#eng_popup_attach').css('opacity','50%');
			//$('#eng_popup_attach').css('pointer-events', 'none');
	    	
	    	if(data[0].lock_yn==='Y'){
	    		$('#popup_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#popup_revise').attr('disabled',true);
	    		$('#popup_save').attr('disabled',true);
	    	}else{
	    		$('#popup_locker').html('');
	    		$('#popup_save').attr('disabled',false);
	    	}
	    	$('#popup_rev_max').val(data[0].rev_max);
	    	if(data[0].rev_max!==rev_id && data[0].rev_max!=='001'){
	    		$('#popup_oldversion').html('Old Version');
	    	}else{
	    		$('#popup_oldversion').html('');
	    	}
	    	$('#popup_doc_no').val(data[0].doc_no);
	    	if(data[0].rev_code_id!==null && data[0].rev_code_id!==undefined && data[0].rev_code_id!==''){
		    	$('#popup_doc_no').attr("disabled", true);
		    	$('#revNoSelected').html(data[0].rev_no);
		    	$('#origin_rev_code_id').val(data[0].rev_code_id);
		    	$('#selectedRevNoSetCodeId').val(data[0].rev_code_id);
	    	}else{
		    	$('#popup_doc_no').attr("disabled", false);
		    	$('#revNoSelected').html('SELECT');
		    	$('#origin_rev_code_id').val('');
		    	$('#selectedRevNoSetCodeId').val('');
	    	}
	    	$('#popup_old_doc_no').val(data[0].old_doc_no);
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
		    	$('#eng_pop_uploadFileInfo').val(data[0].file_type+', '+formatBytes(data[0].file_size,2));
		    	$('#eng_pop_uploadFileDate').val(data[0].attach_file_date?getDateFormat(data[0].attach_file_date):'');
		    	$('#eng_popup_attachfilename').val(data[0].rfile_nm);
	    	}else{
		    	$('#eng_pop_uploadFileInfo').val('');
		    	$('#eng_pop_uploadFileDate').val('');
		    	$('#eng_popup_attachfilename').val('파일선택');
	    	}
	    	$('#popup_title').val(data[0].title);
	    	$('#popup_creation').val(data[0].reg_id+'('+data[0].reg_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#popup_modification').val(data[0].mod_date===null?'':data[0].mod_id+'('+data[0].mod_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#popup_discipline').val(data[0].discip_display);
		    $('#popup_discipline_code_id').val(data[0].discip_code_id);
	    	$('#popup_designer').val(data[0].designer_nm+'('+data[0].designer_id+')');
	    	$('#popup_designer_id').val(data[0].designer_id);
	    	$('#popup_pre_designer').val(data[0].pre_designer_nm+'('+data[0].prfe_designer_id+')');
	    	$('#popup_pre_designer_id').val(data[0].prfe_designer_id);
	    	if(data[0].wbs_code_id!==null && data[0].wbs_code_id!==undefined && data[0].wbs_code_id!==''){
		    	$('#wbsCodeSelected').html(data[0].wbs_code);
		    	$('#selectedWbsCodeId').val(data[0].wbs_code_id);
	    	}else{
		    	$('#wbsCodeSelected').html('SELECT');
	    	}
	    	$('#popup_wbs_detail_1').val(data[0].wgtval);
	    	$('#popup_wbs_detail_2').val(data[0].wbs_desc);
	    	$('#popup_remark').val(data[0].remark);
	    	if(data[0].category_code!==null && data[0].category_code!==undefined && data[0].category_code!==''){
		    	$('#docCategorySelected').html(data[0].category_code);
		    	$('#selectedDocCateogrySetCodeId').val(data[0].category_cd_id);
	    	}else{
		    	$('#docCategorySelected').html('SELECT');
	    	}
	    	if(data[0].size_code!==null && data[0].size_code!==undefined && data[0].size_code!==''){
		    	$('#docSizeSelected').html(data[0].size_code);
		    	$('#selectedDocSizeSetCodeId').val(data[0].size_code_id);
	    	}else{
		    	$('#docSizeSelected').html('SELECT');
	    	}
	    	if(data[0].doc_type_code!==null && data[0].doc_type_code!==undefined && data[0].doc_type_code!==''){
		    	$('#docTypeSelected').html(data[0].doc_type_code);
		    	$('#selectedDocTypeSetCodeId').val(data[0].doc_type_id);
	    	}else{
		    	$('#docTypeSelected').html('SELECT');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	getPrjStep(doc_id,rev_id,'DCI');
	getPrjTrHistory(doc_id,rev_id,'DCI');
	getPrjDrnHistory(doc_id,rev_id);
}
function getVendorDocumentInfo(doc_id,rev_id){
	$('#vendorFileAttachTr').css('display','table-row');
	vendor_new_or_update = 'update';
	$.ajax({
	    url: 'getVendorDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id,
	    	inout:''
	    },
	    success: function onData (data) {
	    	$('#vendorRevNoSelected').css('pointer-events', 'auto');
	    	$('#vendorRevNoSelected').css('background', 'url(resource/images/select.png) no-repeat right center #fff');
	    	$('#vendorRevNoSelected').css('background-color', '#fff');
			//$('#vendor_popup_attach').css('opacity','50%');
			//$('#vendor_popup_attach').css('pointer-events', 'none');
	    	
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
		    	$('#vendor_uploadFileInfo').val(data[0].file_type+', '+formatBytes(data[0].file_size,2));
		    	$('#vendor_uploadFileDate').val(data[0].attach_file_date?getDateFormat(data[0].attach_file_date):'');
		    	$('#vendor_attachfilename').val(data[0].rfile_nm);
	    	}else{
		    	$('#vendor_uploadFileInfo').val('');
		    	$('#vendor_uploadFileDate').val('');
		    	$('#vendor_attachfilename').val('파일선택');
	    	}
	    	$('#vendor_title').val(data[0].title);
	    	$('#vendor_creation').val(data[0].reg_id+'('+data[0].reg_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#vendor_modification').val(data[0].mod_date===null?'':data[0].mod_id+'('+data[0].mod_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#vendor_discipline').val(data[0].discip_display);
		    $('#vendor_discipline_code_id').val(data[0].discip_code_id);
	    	$('#vendor_designer').val(data[0].designer_nm+'('+data[0].designer_id+')');
	    	$('#vendor_designer_id').val(data[0].designer_id);
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
}
function getProcDocumentInfo(doc_id,rev_id){
	$('#procFileAttachTr').css('display','table-row');
	proc_new_or_update = 'update';
	$.ajax({
	    url: 'getProcDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id
	    },
	    success: function onData (data) {
	    	$('#procRevNoSelected').css('pointer-events', 'auto');
	    	$('#procRevNoSelected').css('background', 'url(resource/images/select.png) no-repeat right center #fff');
	    	$('#procRevNoSelected').css('background-color', '#fff');
			//$('#proc_popup_attach').css('opacity','50%');
			//$('#proc_popup_attach').css('pointer-events', 'none');
	    	
	    	if(data[0].lock_yn==='Y'){
	    		$('#proc_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#proc_revise').attr('disabled',true);
	    		$('#proc_save').attr('disabled',true);
	    	}else{
	    		$('#proc_locker').html('');
	    		$('#proc_save').attr('disabled',false);
	    	}
	    	$('#proc_rev_max').val(data[0].rev_max);
	    	if(data[0].rev_max!==rev_id && data[0].rev_max!=='001'){
	    		$('#proc_oldversion').html('Old Version');
	    	}else{
	    		$('#proc_oldversion').html('');
	    	}
	    	$('#proc_doc_no').val(data[0].doc_no);
	    	if(data[0].rev_code_id!==null && data[0].rev_code_id!==undefined && data[0].rev_code_id!==''){
		    	$('#proc_doc_no').attr("disabled", true);
		    	$('#procRevNoSelected').html(data[0].rev_no);
		    	$('#proc_origin_rev_code_id').val(data[0].rev_code_id);
		    	$('#procSelectedRevNoSetCodeId').val(data[0].rev_code_id);
	    	}else{
		    	$('#proc_doc_no').attr("disabled", false);
		    	$('#procRevNoSelected').html('SELECT');
		    	$('#proc_origin_rev_code_id').val('');
		    	$('#procSelectedRevNoSetCodeId').val('');
	    	}
	    	//$('#proc_old_doc_no').val(data[0].old_doc_no);
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
		    	$('#proc_uploadFileInfo').val(data[0].file_type+', '+formatBytes(data[0].file_size,2));
		    	$('#proc_uploadFileDate').val(data[0].attach_file_date?getDateFormat(data[0].attach_file_date):'');
		    	$('#proc_attachfilename').val(data[0].rfile_nm);
	    	}else{
		    	$('#proc_uploadFileInfo').val('');
		    	$('#proc_uploadFileDate').val('');
		    	$('#proc_attachfilename').val('파일선택');
	    	}
	    	$('#proc_title').val(data[0].title);
	    	$('#proc_creation').val(data[0].reg_id+'('+data[0].reg_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#proc_modification').val(data[0].mod_date===null?'':data[0].mod_id+'('+data[0].mod_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#proc_discipline').val(data[0].discip_display);
		    $('#proc_discipline_code_id').val(data[0].discip_code_id);
	    	$('#proc_designer').val(data[0].designer_nm+'('+data[0].designer_id+')');
	    	$('#proc_designer_id').val(data[0].designer_id);
	    	$('#proc_pre_designer').val(data[0].pre_designer_nm+'('+data[0].prfe_designer_id+')');
	    	$('#proc_pre_designer_id').val(data[0].prfe_designer_id);
	    	if(data[0].wbs_code_id!==null && data[0].wbs_code_id!==undefined && data[0].wbs_code_id!==''){
		    	$('#procWbsCodeSelected').html(data[0].wbs_code);
		    	$('#procSelectedWbsCodeId').val(data[0].wbs_code_id);
	    	}else{
		    	$('#procWbsCodeSelected').html('SELECT');
	    	}
	    	$('#proc_wbs_detail_1').val(data[0].wgtval);
	    	$('#proc_wbs_detail_2').val(data[0].wbs_desc);
	    	$('#proc_remark').val(data[0].remark);
	    	$('#proc_por_no').val(data[0].por_no);
	    	$('#proc_por_issue_date').val(data[0].por_issue_date);
	    	$('#proc_po_no').val(data[0].po_no);
	    	$('#proc_po_issue_date').val(data[0].po_issue_date);
	    	$('#proc_vendor_nm').val(data[0].vendor_nm);
	    	$('#proc_vendor_doc_no').val(data[0].vendor_doc_no);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	getPrjStep(doc_id,rev_id,'PCI');
}
function getSiteDocumentInfo(doc_id,rev_id){
	$('#siteFileAttachTr').css('display','table-row');
	site_new_or_update = 'update';
	$.ajax({
	    url: 'getSiteDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id,
	    	inout:''
	    },
	    success: function onData (data) {
	    	$('#siteRevNoSelected').css('pointer-events', 'auto');
	    	$('#siteRevNoSelected').css('background', 'url(resource/images/select.png) no-repeat right center #fff');
	    	$('#siteRevNoSelected').css('background-color', '#fff');
			//$('#site_popup_attach').css('opacity','50%');
			//$('#site_popup_attach').css('pointer-events', 'none');
	    	
	    	if(data[0].lock_yn==='Y'){
	    		$('#site_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#site_revise').attr('disabled',true);
	    		$('#site_save').attr('disabled',true);
	    	}else{
	    		$('#site_locker').html('');
	    		$('#site_save').attr('disabled',false);
	    	}
	    	$('#site_rev_max').val(data[0].rev_max);
	    	if(data[0].rev_max!==rev_id && data[0].rev_max!=='001'){
	    		$('#site_oldversion').html('Old Version');
	    	}else{
	    		$('#site_oldversion').html('');
	    	}
	    	$('#site_doc_no').val(data[0].doc_no);
	    	if(data[0].rev_code_id!==null && data[0].rev_code_id!==undefined && data[0].rev_code_id!==''){
		    	$('#site_doc_no').attr("disabled", true);
		    	$('#siteRevNoSelected').html(data[0].rev_no);
		    	$('#site_origin_rev_code_id').val(data[0].rev_code_id);
		    	$('#siteSelectedRevNoSetCodeId').val(data[0].rev_code_id);
	    	}else{
		    	$('#site_doc_no').attr("disabled", false);
		    	$('#siteRevNoSelected').html('SELECT');
		    	$('#site_origin_rev_code_id').val('');
		    	$('#siteSelectedRevNoSetCodeId').val('');
	    	}
	    	$('#site_old_doc_no').val(data[0].old_doc_no);
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
		    	$('#site_uploadFileInfo').val(data[0].file_type+', '+formatBytes(data[0].file_size,2));
		    	$('#site_uploadFileDate').val(data[0].attach_file_date?getDateFormat(data[0].attach_file_date):'');
		    	$('#site_attachfilename').val(data[0].rfile_nm);
	    	}else{
		    	$('#site_uploadFileInfo').val('');
		    	$('#site_uploadFileDate').val('');
		    	$('#site_attachfilename').val('파일선택');
	    	}
	    	$('#site_title').val(data[0].title);
	    	$('#site_creation').val(data[0].reg_id+'('+data[0].reg_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#site_modification').val(data[0].mod_date===null?'':data[0].mod_id+'('+data[0].mod_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#site_discipline').val(data[0].discip_display);
		    $('#site_discipline_code_id').val(data[0].discip_code_id);
	    	$('#site_designer').val(data[0].designer_nm+'('+data[0].designer_id+')');
	    	$('#site_designer_id').val(data[0].designer_id);
	    	$('#site_pre_designer').val(data[0].pre_designer_nm+'('+data[0].prfe_designer_id+')');
	    	$('#site_pre_designer_id').val(data[0].prfe_designer_id);
	    	if(data[0].wbs_code_id!==null && data[0].wbs_code_id!==undefined && data[0].wbs_code_id!==''){
		    	$('#siteWbsCodeSelected').html(data[0].wbs_code);
		    	$('#siteSelectedWbsCodeId').val(data[0].wbs_code_id);
	    	}else{
		    	$('#siteWbsCodeSelected').html('SELECT');
	    	}
	    	$('#site_wbs_detail_1').val(data[0].wgtval);
	    	$('#site_wbs_detail_2').val(data[0].wbs_desc);
	    	$('#site_remark').val(data[0].remark);
	    	if(data[0].category_code!==null && data[0].category_code!==undefined && data[0].category_code!==''){
		    	$('#siteDocCategorySelected').html(data[0].category_code);
		    	$('#siteSelectedDocCateogrySetCodeId').val(data[0].category_cd_id);
	    	}else{
		    	$('#siteDocCategorySelected').html('SELECT');
	    	}
	    	if(data[0].size_code!==null && data[0].size_code!==undefined && data[0].size_code!==''){
		    	$('#siteDocSizeSelected').html(data[0].size_code);
		    	$('#siteSelectedDocSizeSetCodeId').val(data[0].size_code_id);
	    	}else{
		    	$('#siteDocSizeSelected').html('SELECT');
	    	}
	    	if(data[0].doc_type_code!==null && data[0].doc_type_code!==undefined && data[0].doc_type_code!==''){
		    	$('#siteDocTypeSelected').html(data[0].doc_type_code);
		    	$('#siteSelectedDocTypeSetCodeId').val(data[0].doc_type_id);
	    	}else{
		    	$('#siteDocTypeSelected').html('SELECT');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	getPrjStep(doc_id,rev_id,'SDCI');
	getPrjTrHistory(doc_id,rev_id,'SDCI');
}
function getCorrDocumentInfo(doc_id,rev_id){
	corr_new_or_update = 'update';
	$.ajax({
	    url: 'getCorrDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id
	    },
	    success: function onData (data) {
	    	if(data[0].lock_yn==='Y'){
	    		$('#corr_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#corr_save').attr('disabled',true);
	    	}else{
	    		$('#corr_locker').html('');
	    		$('#corr_save').attr('disabled',false);
	    	}
	        $('input[name="inout"][value="'+data[0].in_out+'"]').prop('checked',true);
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
		    	$('#corr_uploadFileInfo').val(data[0].file_type+', '+formatBytes(data[0].file_size,2));
		    	$('#corr_uploadFileDate').val(data[0].attach_file_date?getDateFormat(data[0].attach_file_date):'');
		    	$('#corr_attachfilename').val(data[0].rfile_nm);
	    	}else{
		    	$('#corr_uploadFileInfo').val('');
		    	$('#corr_uploadFileDate').val('');
		    	$('#corr_attachfilename').val('파일선택');
	    	}
	    	$('#corr_doc_date').val(to_date_format(data[0].doc_date,'-'));
	    	$('#corr_creation').val(data[0].reg_id+'('+data[0].reg_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#corr_modification').val(data[0].mod_date===null?'':data[0].mod_id+'('+data[0].mod_nm+'): '+getDateFormat(data[0].mod_date));
	    	$('#corr_remark').val(data[0].remark);
	    	$('#corrSenderSelected').html(data[0].sender_code);
	    	$('#corrSelectedSenderSetCodeId').val(data[0].sender_code_id);
	    	$('#corrReceiverSelected').html(data[0].receiver_code);
	    	$('#corrSelectedReceiverSetCodeId').val(data[0].receiver_code_id);
	    	$('#corrTypeSelected').html(data[0].doc_type_code);
	    	$('#corrSelectedTypeSetCodeId').val(data[0].doc_type_code_id);
	    	$('#corr_serial_realdata').val(data[0].serial);
	    	$('#corr_corr_no_1').val(data[0].corr_no);
	    	$('#corr_ref_doc_no').val(data[0].ref_corr_doc_no);
	    	$('#corr_title').val(data[0].title);
	    	$('#corrResponSelected').html(data[0].responsibility_code?data[0].responsibility_code:'SELECT');
	    	$('#corrSelectedResponSetCodeId').val(data[0].responsibility_code_id);
	    	$('#corr_originator').val(data[0].originator);
	    	$('input:checkbox[name="corr_distribute"]').prop('checked', false);
	    	if(data[0].disact!=='' && data[0].disact!==null && data[0].disact!==undefined){
		    	var disactArray = data[0].disact.split(',');
		    	for(let i=0;i<disactArray.length;i++){
		    		$("input:checkbox[name='corr_distribute'][value='"+disactArray[i]+"']").prop("checked", true);
		    	}
	    	}
	    	$('#corr_rep_req_date').val(to_date_format(data[0].replyreqdate,'-'));
	    	$('#corr_int_dist_date').val(to_date_format(data[0].indistdate,'-'));
	    	$('#corr_act_req_date').val(to_date_format(data[0].inactdate,'-'));
	    	$('#corr_remark').val(data[0].remark);
	    	$('#corr_userdate_00').val(data[0].userdata00);
	    	$('#corr_userdate_01').val(data[0].userdata01);
	    	$('#corr_userdate_02').val(data[0].userdata02);
	    	$('#corr_userdate_03').val(data[0].userdata03);
	    	$('#corr_userdate_04').val(data[0].userdata04);
	    	//$('#corr_reference_by').val();data[0].replydocno???
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getGeneralDocumentInfo(doc_id,rev_id){
	general_new_or_update = 'update';
	$.ajax({
	    url: 'getGeneralDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:current_folder_id,
	    	doc_id:doc_id,
	    	rev_id:rev_id
	    },
	    success: function onData (data) {
	    	$('#generalRevNoSelected').css('pointer-events', 'auto');
	    	$('#generalRevNoSelected').css('background', 'url(resource/images/select.png) no-repeat right center #fff');
	    	$('#generalRevNoSelected').css('background-color', '#fff');
			//$('#general_popup_attach').css('opacity','50%');
			//$('#general_popup_attach').css('pointer-events', 'none');
	    	
	    	if(data[0].lock_yn==='Y'){
	    		$('#general_locker').html('Locker '+data[0].mod_nm+' ('+data[0].mod_id+')');
	    		$('#general_revise').attr('disabled',true);
	    		$('#general_save').attr('disabled',true);
	    	}else{
	    		$('#general_locker').html('');
	    		$('#general_save').attr('disabled',false);
	    	}
	    	$('#general_rev_max').val(data[0].rev_max);
	    	if(data[0].rev_max!==rev_id && data[0].rev_max!=='001'){
	    		$('#general_oldversion').html('Old Version');
	    	}else{
	    		$('#general_oldversion').html('');
	    	}
	    	$('#general_doc_no').val(data[0].doc_no);
	    	if(data[0].rev_code_id!==null && data[0].rev_code_id!==undefined && data[0].rev_code_id!==''){
		    	$('#general_doc_no').attr("disabled", true);
		    	$('#generalRevNoSelected').html(data[0].rev_no);
		    	$('#general_origin_rev_code_id').val(data[0].rev_code_id);
		    	$('#generalSelectedRevNoSetCodeId').val(data[0].rev_code_id);
	    	}else{
		    	$('#general_doc_no').attr("disabled", false);
		    	$('#generalRevNoSelected').html('SELECT');
		    	$('#general_origin_rev_code_id').val('');
		    	$('#generalSelectedRevNoSetCodeId').val('');
	    	}
	    	if(data[0].rfile_nm!==null && data[0].rfile_nm!==undefined && data[0].rfile_nm!==''){
		    	$('#general_uploadFileInfo').val(data[0].file_type+', '+formatBytes(data[0].file_size,2));
		    	$('#general_uploadFileDate').val(data[0].attach_file_date?getDateFormat(data[0].attach_file_date):'');
		    	$('#general_attachfilename').val(data[0].rfile_nm);
	    	}else{
		    	$('#general_uploadFileInfo').val('');
		    	$('#general_uploadFileDate').val('');
		    	$('#general_attachfilename').val('파일선택');
	    	}
	    	$('#general_title').val(data[0].title);
	    	$('#general_creation').val(data[0].reg_id+'('+data[0].reg_nm+'): '+getDateFormat(data[0].reg_date));
	    	$('#general_modification').val(data[0].mod_date===null?'':data[0].mod_id+'('+data[0].mod_nm+'): '+getDateFormat(data[0].mod_date));
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getPrjDCC(){
	var DCCArray;
	$.ajax({
	    url: 'getPrjDCC.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId
	    },
	    async:false,
	    success: function onData (data) {
	    	var DCCIDLIST = '';
	    	for(i=0;i<data[0].length;i++){
	    		DCCIDLIST += data[0][i].user_id + ',';
	    	}
	    	DCCIDLIST = DCCIDLIST.slice(0, -1);
	    	DCCArray = DCCIDLIST.split(',');
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	return DCCArray;
}
function documentCheckOut(doc_id,rev_id,doc_no,doc_title,folder_id,file_size,method){
	 $.ajax({
			type: 'POST',
			url: 'documentCheckOut.do',
			data: {
				prj_id:selectPrjId,
				doc_id:doc_id,
				rev_id:rev_id
			},
			success: function(data) {
				if(data[0]>0){
					getPrjDocumentIndexList();
					
					$.ajax({
		    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
		    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
		    		    data:{				// 이동할때 챙겨야하는 데이터
		    		    	prj_id: getPrjInfo().id,
		    		    	doc_id: doc_id,
		    		    	rev_id: rev_id,
		    		    	doc_no: doc_no,
		    		    	doc_title: doc_title,
		    		    	folder_id: folder_id,
		    		    	file_size: file_size,
		    		    	method: method
		    		    },
		    		    success: function onData (data) {
		    		    },
		    		    error: function onError (error) {
		    		        console.error(error);
		    		        alert("오류");
		    		    }
		    		});
				}else{
					alert('CheckOut failed');
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function documentCheckOutCancel(doc_id,rev_id,doc_no,doc_title,folder_id,file_size){
	 $.ajax({
			type: 'POST',
			url: 'documentCheckOutCancel.do',
			data: {
				prj_id:selectPrjId,
				doc_id:doc_id,
				rev_id:rev_id
			},
			success: function(data) {
				if(data[0]==='체크아웃취소완료'){
					getPrjDocumentIndexList();
					$.ajax({
		    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
		    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
		    		    data:{				// 이동할때 챙겨야하는 데이터
		    		    	prj_id: getPrjInfo().id,
		    		    	doc_id: doc_id,
		    		    	rev_id: rev_id,
		    		    	doc_no: doc_no,
		    		    	doc_title: doc_title,
		    		    	folder_id: folder_id,
		    		    	file_size: file_size,
		    		    	method: "CANCLE CHECK-OUT"
		    		    },
		    		    success: function onData (data) {
		    		    },
		    		    error: function onError (error) {
		    		        console.error(error);
		    		        alert("오류");
		    		    }
		    		});
				}else{
					alert('DRN 진행중인 도서는 Cancel Check-Out 할 수 없습니다. DRN NO: '+data[0]);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function documentDelete(doc_type){
	let selectRows = documentListTable.getSelectedRows().length;
	let doc_id = '';
	let rev_id = '';
	var accHisDownList = [];
	for(let i=0;i<selectRows;i++){
		doc_id += documentListTable.getSelectedData()[i].bean.doc_id + ',';
		rev_id += documentListTable.getSelectedData()[i].bean.rev_id + ',';
		accHisDownList.push(JSON.stringify(documentListTable.getSelectedData()[i].bean));
	}
	doc_id = doc_id.slice(0,-1);
	rev_id = rev_id.slice(0,-1);

	 $.ajax({
			type: 'POST',
			url: 'getUseDocTROrDRN.do',
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
							url: 'documentDelete.do',
							data: {
								prj_id:selectPrjId,
								doc_id:doc_id,
								rev_id:rev_id
							},
							success: function(data) {
								console.log("test",data);
								if(selectRows===data[0]){
									getPrjDocumentIndexList();
								    $(".pop_documentDeleteConfirm").dialog('close');
								    $.ajax({
										url: 'insertAccHisList.do',
										type: 'POST',
										traditional : true,
										async: false,
										data:{
											jsonRowDatas: accHisDownList,
											prj_id: selectPrjId,
											method: 'DELETE'
										},
										success: function onData (data) {
									    },
									    error: function onError (error) {
									        console.error(error);
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
	 $.ajax({
			type: 'POST',
			url: 'getUseDocTROrDRN.do',
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
							url: 'documentDelete.do',
							data: {
								prj_id:selectPrjId,
								doc_id:doc_id,
								rev_id:rev_id
							},
							success: function(data) {
								if(data[0]>0){
									getPrjDocumentIndexList();
							        getPopupPrjDocumentIndexList(doc_type);
								    $(".pop_documentDeleteConfirm").dialog('close');
								    alert('Delete Completed');
								    $.ajax({
										url: 'insertAccHisList.do',
										type: 'POST',
										traditional : true,
										async: false,
										data:{
											jsonRowDatas: accHisDownList,
											prj_id: selectPrjId,
											method: 'DELETE'
										},
										success: function onData (data) {
									    },
									    error: function onError (error) {
									        console.error(error);
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
		getDocumentInfo(doc_id,rev_id);
	}else if(DCCorTRA==='TRA'){
		getDocumentInfo(trans_ref_doc_id,rev_id);
	}
	getPopupPrjDocumentIndexList(type,'refresh');
}
function popupNewButton(){
	popup_new_or_update = 'new';
	getPopupPrjDocumentIndexList('DCI');
	$('#revNoSelected').css('pointer-events', 'none');
	$('#revNoSelected').css('background', 'none');
	$('#revNoSelected').css('background-color', '#eee');
	//$('#eng_popup_attach').css('opacity','50%');
	//$('#eng_popup_attach').css('pointer-events', 'none');
    getPrjStep('','','DCI','new');
	setNull();
}
function vendorNewButton(){
	vendor_new_or_update = 'new';
	getPopupPrjDocumentIndexList('VDCI');
	$('#vendorRevNoSelected').css('pointer-events', 'none');
	$('#vendorRevNoSelected').css('background', 'none');
	$('#vendorRevNoSelected').css('background-color', '#eee');
	//$('#vendor_popup_attach').css('opacity','50%');
	//$('#vendor_popup_attach').css('pointer-events', 'none');
    getPrjStep('','','VDCI','new');
	setVendorNull();
}
function procNewButton(){
	proc_new_or_update = 'new';
	getPopupPrjDocumentIndexList('PCI');
	$('#procRevNoSelected').css('pointer-events', 'none');
	$('#procRevNoSelected').css('background', 'none');
	$('#procRevNoSelected').css('background-color', '#eee');
	//$('#proc_popup_attach').css('opacity','50%');
	//$('#proc_popup_attach').css('pointer-events', 'none');
    getPrjStep('','','PCI','new');
	setProcNull();
}
function siteNewButton(){
	site_new_or_update = 'new';
	getPopupPrjDocumentIndexList('SDCI');
	$('#siteRevNoSelected').css('pointer-events', 'none');
	$('#siteRevNoSelected').css('background', 'none');
	$('#siteRevNoSelected').css('background-color', '#eee');
	//$('#site_popup_attach').css('opacity','50%');
	//$('#site_popup_attach').css('pointer-events', 'none');
    getPrjStep('','','SDCI','new');
	setSiteNull();
}
function corrNewButton(){
	corr_new_or_update = 'new';
	getPopupPrjDocumentIndexList('Corr');
	setCorrNull();
}
function generalNewButton(){
	general_new_or_update = 'new';
	getPopupPrjDocumentIndexList('General');
	$('#generalRevNoSelected').css('pointer-events', 'none');
	$('#generalRevNoSelected').css('background', 'none');
	$('#generalRevNoSelected').css('background-color', '#eee');
	//$('#general_popup_attach').css('opacity','50%');
	//$('#general_popup_attach').css('pointer-events', 'none');
	setGeneralNull();
}
function setNull(){
	$('#engFileAttachTr').css('display','none');
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#popup_doc_no').attr("disabled", false);
    $('#eng_attach_filename').val('');
	$('#popup_locker').html('');
	$('#popup_oldversion').html('');
	$('#popup_doc_no').val('');
	$('#revNoSelected').html('SELECT');
	$('#popup_revise').attr('disabled',true);
	$('#popup_save').attr('disabled',false);
	$('#selectedSetCodeId').val('');
	$('#popup_old_doc_no').val('');
	$('#popup_title').val('');
	$('#popup_creation').val(session_user_id+'('+session_user_eng_nm+'): '+ $('#currentTime').val());
	$('#popup_modification').val(null);
	if(current_folder_discipline_code_id===null || current_folder_discipline_code_id===undefined || current_folder_discipline_code_id===''){
		$('#popup_discipline_code_id').val($('#docSelTree_discipline_code_id').val());
		$('#popup_discipline').val($('#docSelTree_discipline_display').val());
	}else{
		$('#popup_discipline_code_id').val(current_folder_discipline_code_id);
		$('#popup_discipline').val(current_folder_discipline_display);
	}
	$('#popup_designer').val('');
	$('#popup_pre_designer').val('');
	$('#selectedWbsCodeId').val('');
	$('#wbsCodeSelected').html('SELECT');
	$('#popup_wbs_detail_1').val('');
	$('#popup_wbs_detail_2').val('');
	$('#popup_remark').val('');
	$('#selectedDocCateogrySetCodeId').val('');
	$('#docCategorySelected').html('SELECT');
	$('#selectedDocSizeSetCodeId').val('');
	$('#docSizeSelected').html('SELECT');
	$('#selectedDocTypeSetCodeId').val('');
	$('#docTypeSelected').html('SELECT');
	$('#eng_pop_uploadFileInfo').val('');
	$('#eng_pop_uploadFileDate').val('');
	$('#eng_popup_attachfilename').val('파일선택');
}
function setVendorNull(){
	$('#vendorFileAttachTr').css('display','none');
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#vendor_doc_no').attr("disabled", false);
    $('#vendor_attach_filename').val('');
	$('#vendor_locker').html('');
	$('#vendor_oldversion').html('');
	$('#vendor_doc_no').val('');
	$('#vendorRevNoSelected').html('SELECT');
	$('#vendor_revise').attr('disabled',true);
	$('#vendor_save').attr('disabled',false);
	$('#vendorSelectedSetCodeId').val('');
	$('#vendor_old_doc_no').val('');
	$('#vendor_title').val('');
	$('#vendor_creation').val(session_user_id+'('+session_user_eng_nm+'): '+ $('#currentTime').val());
	$('#vendor_modification').val(null);
	if(current_folder_discipline_code_id===null || current_folder_discipline_code_id===undefined || current_folder_discipline_code_id===''){
		$('#vendor_discipline_code_id').val($('#docSelTree_discipline_code_id').val());
		$('#vendor_discipline').val($('#docSelTree_discipline_display').val());
	}else{
		$('#vendor_discipline_code_id').val(current_folder_discipline_code_id);
		$('#vendor_discipline').val(current_folder_discipline_display);
	}
	$('#vendor_designer').val('');
	$('#vendor_pre_designer').val('');
	$('#vendorSelectedWbsCodeId').val('');
	$('#vendorWbsCodeSelected').html('SELECT');
	$('#vendor_wbs_detail_1').val('');
	$('#vendor_wbs_detail_2').val('');
	$('#vendor_remark').val('');
	$('#vendor_po_doc_no').val();	//나중에 고치기
	$('#vendor_po_doc_title').val('');
	$('#vendor_vendor_nm').val();	//나중에 고치기
	$('#vendor_design_part').val('');
	$('#vendorSelectedDocCateogrySetCodeId').val('');
	$('#vendorDocCategorySelected').html('SELECT');
	$('#vendorSelectedDocSizeSetCodeId').val('');
	$('#vendorDocSizeSelected').html('SELECT');
	$('#vendorSelectedDocTypeSetCodeId').val('');
	$('#vendorDocTypeSelected').html('SELECT');
	$('#vendor_uploadFileInfo').val('');
	$('#vendor_uploadFileDate').val('');
	$('#vendor_attachfilename').val('파일선택');
}
function setProcNull(){
	$('#procFileAttachTr').css('display','none');
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#proc_doc_no').attr("disabled", false);
    $('#proc_attach_filename').val('');
	$('#proc_locker').html('');
	$('#proc_oldversion').html('');
	$('#proc_doc_no').val('');
	$('#procRevNoSelected').html('SELECT');
	$('#proc_revise').attr('disabled',true);
	$('#proc_save').attr('disabled',false);
	$('#procSelectedSetCodeId').val('');
	$('#proc_title').val('');
	$('#proc_creation').val(session_user_id+'('+session_user_eng_nm+'): '+ $('#currentTime').val());
	$('#proc_modification').val(null);
	if(current_folder_discipline_code_id===null || current_folder_discipline_code_id===undefined || current_folder_discipline_code_id===''){
		$('#proc_discipline_code_id').val($('#docSelTree_discipline_code_id').val());
		$('#proc_discipline').val($('#docSelTree_discipline_display').val());
	}else{
		$('#proc_discipline_code_id').val(current_folder_discipline_code_id);
		$('#proc_discipline').val(current_folder_discipline_display);
	}
	$('#proc_designer').val('');
	$('#proc_pre_designer').val('');
	$('#procSelectedWbsCodeId').val('');
	$('#procWbsCodeSelected').html('SELECT');
	$('#proc_wbs_detail_1').val('');
	$('#proc_wbs_detail_2').val('');
	$('#proc_remark').val('');
	$('#proc_por_no').val('');
	$('#proc_po_no').val('');
	$('#proc_por_issue_date').val('');
	$('#proc_po_issue_date').val('');
	$('#proc_vendor_nm').val();
	$('#proc_vendor_doc_no').val('');
	$('#proc_uploadFileInfo').val('');
	$('#proc_uploadFileDate').val('');
	$('#proc_attachfilename').val('파일선택');
}
function setSiteNull(){
	$('#siteFileAttachTr').css('display','none');
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#site_doc_no').attr("disabled", false);
    $('#site_attach_filename').val('');
	$('#site_locker').html('');
	$('#site_oldversion').html('');
	$('#site_doc_no').val('');
	$('#siteRevNoSelected').html('SELECT');
	$('#site_revise').attr('disabled',true);
	$('#site_save').attr('disabled',false);
	$('#siteSelectedSetCodeId').val('');
	$('#site_old_doc_no').val('');
	$('#site_title').val('');
	$('#site_creation').val(session_user_id+'('+session_user_eng_nm+'): '+ $('#currentTime').val());
	$('#site_modification').val(null);
	if(current_folder_discipline_code_id===null || current_folder_discipline_code_id===undefined || current_folder_discipline_code_id===''){
		$('#site_discipline_code_id').val($('#docSelTree_discipline_code_id').val());
		$('#site_discipline').val($('#docSelTree_discipline_display').val());
	}else{
		$('#site_discipline_code_id').val(current_folder_discipline_code_id);
		$('#site_discipline').val(current_folder_discipline_display);
	}
	$('#site_designer').val('');
	$('#site_pre_designer').val('');
	$('#siteSelectedWbsCodeId').val('');
	$('#siteWbsCodeSelected').html('SELECT');
	$('#site_wbs_detail_1').val('');
	$('#site_wbs_detail_2').val('');
	$('#site_remark').val('');
	$('#siteSelectedDocCateogrySetCodeId').val('');
	$('#siteDocCategorySelected').html('SELECT');
	$('#siteSelectedDocSizeSetCodeId').val('');
	$('#siteDocSizeSelected').html('SELECT');
	$('#siteSelectedDocTypeSetCodeId').val('');
	$('#siteDocTypeSelected').html('SELECT');
	$('#site_uploadFileInfo').val('');
	$('#site_uploadFileDate').val('');
	$('#site_attachfilename').val('파일선택');
}
function setCorrNull(){
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#corr_doc_no').attr("disabled", false);
    $('#corr_attach_filename').val('');
	$('#corr_locker').html('');
	$('#corr_creation').val(session_user_id+'('+session_user_eng_nm+'): '+ $('#currentTime').val());
	$('#corr_modification').val(null);
	$('#corr_uploadFileInfo').val('');
	$('#corr_uploadFileDate').val('');
	$('#corr_attachfilename').val('파일선택');
	$('input[name="inout"]').prop('checked',false);
	$('#corr_doc_date').datepicker('setDate', new Date());
	$('#corrSelectedSenderSetCodeId').val('');
	$('#corrSenderSelected').html('SELECT');
	$('#corrSelectedReceiverSetCodeId').val('');
	$('#corrReceiverSelected').html('SELECT');
	$('#corrSelectedTypeSetCodeId').val('');
	$('#corrTypeSelected').html('SELECT');
	$('#corr_serial').val('');
	$('#corr_corr_no_1').val('');
	$('#corr_corr_no_2').val('');
	$('#corr_ref_doc_no').val('');
	$('#corr_title').val('');
	$('#corrSelectedResponSetCodeId').val('');
	$('#corrResponSelected').html('SELECT');
	$('#corr_originator').val('');
	$('input:checkbox[name="corr_distribute"]').prop('checked',false);
	$('#corr_rep_req_date').val('');
	$('#corr_int_dist_date').val('');
	$('#corr_act_req_date').val('');
	$('#corr_remark').val('');
	$('#corr_userdate_00').val('');
	$('#corr_userdate_01').val('');
	$('#corr_userdate_02').val('');
	$('#corr_userdate_03').val('');
	$('#corr_userdate_04').val('');
	$('#corr_reference_by').val('');
}
function setGeneralNull(){
	DC.sfile_nm = '';
    DC.rfile_nm = '';
    DC.file_type = '';
    DC.file_size = 0;
	$('#general_doc_no').attr("disabled", false);
    $('#general_attach_filename').val('');
	$('#general_locker').html('');
	$('#general_oldversion').html('');
	$('#general_doc_no').val('');
	$('#generalRevNoSelected').html('SELECT');
	$('#general_revise').attr('disabled',true);
	$('#general_save').attr('disabled',false);
	$('#generalSelectedSetCodeId').val('');
	$('#general_title').val('');
	$('#general_creation').val(session_user_id+'('+session_user_eng_nm+'): '+ $('#currentTime').val());
	$('#general_modification').val(null);
	$('#general_uploadFileInfo').val('');
	$('#general_uploadFileDate').val('');
	$('#general_attachfilename').val('파일선택');
}
function getRevNoMultiBox(type,doc_id){
	$.ajax({
	    url: 'getRevNoMultiBox.do',
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
        	
	    	if(type==='DCI'){
		    	$('#revNoMultiBox').html(html);
	        	$('.usedRevNo').click(function(){
	        		alert('이전에 사용했던 리비전은 다시 선택할 수 없습니다.');
	        	});
		    	
		    	$('.selectRevNo').click(function(){
		    		let origin_rev_no = $('#revNoSelected').html();
		    		$('#revNoSelectWrap').removeClass('on');
		    		$('#revNoSelected').html($(this).children().html());
		    		$('#selectedRevNoSetCodeId').val($(this).children().attr('id'));
		    		if($('#origin_rev_code_id').val()!==$('#selectedRevNoSetCodeId').val()){
		    			let origin_rev_code_id = parseInt($('#origin_rev_code_id').val());
		    			let select_rev_code_id = parseInt($('#selectedRevNoSetCodeId').val());
		    			/*if(origin_rev_code_id>select_rev_code_id){
		    				alert("It's less than previous revision - "+origin_rev_no+". You have to choose bigger than this one.");
		    				$('#revNoSelected').html(origin_rev_no);
		    				$('#selectedRevNoSetCodeId').val($('#origin_rev_code_id').val());
		    				return false;
		    			}else{*/
			    	        if($('#popup_locker').html()!==''){
				    	        $('#popup_revise').attr('disabled',true);
			    	        	$('#popup_save').attr('disabled',true);
			    	        }else{
				    	        $('#popup_revise').attr('disabled',false);
			    	        	$('#popup_save').attr('disabled',true);
			    	        }
		    			//}
		    		}else{
		    			if($('#popup_locker').html()!==''){
			    			$('#popup_save').attr('disabled',true);
		    			}else{
			    			$('#popup_save').attr('disabled',false);
		    			}
		    	        $('#popup_revise').attr('disabled',true);

		    		}
		    		
	    	        if($('#popup_locker').html()==='' && $('#popup_rev_max').val()===eng_selected_rev_id && $('#selectedRevNoSetCodeId').val()!=='' && $('#origin_rev_code_id').val()!==$('#selectedRevNoSetCodeId').val()){
	    	        	$('#popup_revise').attr('disabled',false);
	    	        }
		    	});
	    	}else if(type==='VDCI'){
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
		    			/*if(origin_rev_code_id>select_rev_code_id){
		    				alert("It's less than previous revision - "+origin_rev_no+". You have to choose bigger than this one.");
		    				$('#vendorRevNoSelected').html(origin_rev_no);
		    				$('#vendorSelectedRevNoSetCodeId').val($('#vendor_origin_rev_code_id').val());
		    				return false;
		    			}else{*/
			    	        if($('#vendor_locker').html()!==''){
				    	        $('#vendor_revise').attr('disabled',true);
			    	        	$('#vendor_save').attr('disabled',true);
			    	        }else{
				    	        $('#vendor_revise').attr('disabled',false);
			    	        	$('#vendor_save').attr('disabled',true);
			    	        }
		    			//}
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
	    	}else if(type==='PCI'){
		    	$('#procRevNoMultiBox').html(html);
	        	$('.usedRevNo').click(function(){
	        		alert('이전에 사용했던 리비전은 다시 선택할 수 없습니다.');
	        	});

		    	$('.selectRevNo').click(function(){
		    		let origin_rev_no = $('#procRevNoSelected').html();
		    		$('#procRevNoSelectWrap').removeClass('on');
		    		$('#procRevNoSelected').html($(this).children().html());
		    		$('#procSelectedRevNoSetCodeId').val($(this).children().attr('id'));
		    		if($('#proc_origin_rev_code_id').val()!==$('#procSelectedRevNoSetCodeId').val()){
		    			let origin_rev_code_id = parseInt($('#proc_origin_rev_code_id').val());
		    			let select_rev_code_id = parseInt($('#procSelectedRevNoSetCodeId').val());
		    			/*if(origin_rev_code_id>select_rev_code_id){
		    				alert("It's less than previous revision - "+origin_rev_no+". You have to choose bigger than this one.");
		    				$('#procRevNoSelected').html(origin_rev_no);
		    				$('#procSelectedRevNoSetCodeId').val($('#proc_origin_rev_code_id').val());
		    				return false;
		    			}else{*/
			    	        if($('#proc_locker').html()!==''){
				    	        $('#proc_revise').attr('disabled',true);
			    	        	$('#proc_save').attr('disabled',true);
			    	        }else{
				    	        $('#proc_revise').attr('disabled',false);
			    	        	$('#proc_save').attr('disabled',true);
			    	        }
		    			//}
		    		}else{
		    			if($('#proc_locker').html()!==''){
		    				$('#proc_save').attr('disabled',true);
		    			}else{
			    			$('#proc_save').attr('disabled',false);
		    			}
		    	        $('#proc_revise').attr('disabled',true);
		    		}
		    		
		            if($('#proc_locker').html()==='' && $('#proc_rev_max').val()===proc_selected_rev_id && $('#procSelectedRevNoSetCodeId').val()!=='' && $('#proc_origin_rev_code_id').val()!==$('#procSelectedRevNoSetCodeId').val()){
		            	$('#proc_revise').attr('disabled',false);
		            }
		    	});
	    	}else if(type==='SDCI'){
		    	$('#siteRevNoMultiBox').html(html);
	        	$('.usedRevNo').click(function(){
	        		alert('이전에 사용했던 리비전은 다시 선택할 수 없습니다.');
	        	});

		    	$('.selectRevNo').click(function(){
		    		let origin_rev_no = $('#siteRevNoSelected').html();
		    		$('#siteRevNoSelectWrap').removeClass('on');
		    		$('#siteRevNoSelected').html($(this).children().html());
		    		$('#siteSelectedRevNoSetCodeId').val($(this).children().attr('id'));
		    		if($('#site_origin_rev_code_id').val()!==$('#siteSelectedRevNoSetCodeId').val()){
		    			let origin_rev_code_id = parseInt($('#site_origin_rev_code_id').val());
		    			let select_rev_code_id = parseInt($('#siteSelectedRevNoSetCodeId').val());
		    			/*if(origin_rev_code_id>select_rev_code_id){
		    				alert("It's less than previous revision - "+origin_rev_no+". You have to choose bigger than this one.");
		    				$('#siteRevNoSelected').html(origin_rev_no);
		    				$('#siteSelectedRevNoSetCodeId').val($('#site_origin_rev_code_id').val());
		    				return false;
		    			}else{*/
			    	        if($('#site_locker').html()!==''){
				    	        $('#site_revise').attr('disabled',true);
			    	        	$('#site_save').attr('disabled',true);
			    	        }else{
				    	        $('#site_revise').attr('disabled',false);
			    	        	$('#site_save').attr('disabled',true);
			    	        }
		    			//}
		    		}else{
		    			if($('#site_locker').html()!==''){
		    				$('#site_save').attr('disabled',true);
		    			}else{
			    			$('#site_save').attr('disabled',false);
		    			}
		    	        $('#site_revise').attr('disabled',true);
		    		}
		    		
		            if($('#site_locker').html()==='' && $('#site_rev_max').val()===site_selected_rev_id && $('#siteSelectedRevNoSetCodeId').val()!=='' && $('#site_origin_rev_code_id').val()!==$('#siteSelectedRevNoSetCodeId').val()){
		            	$('#site_revise').attr('disabled',false);
		            }
		    	});
	    	}else if(type==='General'){
		    	$('#generalRevNoMultiBox').html(html);
	        	$('.usedRevNo').click(function(){
	        		alert('이전에 사용했던 리비전은 다시 선택할 수 없습니다.');
	        	});

		    	$('.selectRevNo').click(function(){
		    		let origin_rev_no = $('#generalRevNoSelected').html();
		    		$('#generalRevNoSelectWrap').removeClass('on');
		    		$('#generalRevNoSelected').html($(this).children().html());
		    		$('#generalSelectedRevNoSetCodeId').val($(this).children().attr('id'));
		    		if($('#general_origin_rev_code_id').val()!==$('#generalSelectedRevNoSetCodeId').val()){
		    			let origin_rev_code_id = parseInt($('#general_origin_rev_code_id').val());
		    			let select_rev_code_id = parseInt($('#generalSelectedRevNoSetCodeId').val());
		    			/*if(origin_rev_code_id>select_rev_code_id){
		    				alert("It's less than previous revision - "+origin_rev_no+". You have to choose bigger than this one.");
		    				$('#generalRevNoSelected').html(origin_rev_no);
		    				$('#generalSelectedRevNoSetCodeId').val($('#general_origin_rev_code_id').val());
		    				return false;
		    			}else{*/
			    	        if($('#general_locker').html()!==''){
				    	        $('#general_revise').attr('disabled',true);
			    	        	$('#general_save').attr('disabled',true);
			    	        }else{
				    	        $('#general_revise').attr('disabled',false);
			    	        	$('#general_save').attr('disabled',true);
			    	        }
		    			//}
		    		}else{
		    			if($('#general_locker').html()!==''){
		    				$('#general_save').attr('disabled',true);
		    			}else{
			    			$('#general_save').attr('disabled',false);
		    			}
		    	        $('#general_revise').attr('disabled',true);
		    		}
		    		
		            if($('#general_locker').html()==='' && $('#general_rev_max').val()===general_selected_rev_id && $('#generalSelectedRevNoSetCodeId').val()!=='' && $('#general_origin_rev_code_id').val()!==$('#generalSelectedRevNoSetCodeId').val()){
		            	$('#general_revise').attr('disabled',false);
		            }
		    	});
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getWbsCodeMultiBox(type){
	$.ajax({
	    url: 'getWbsCodeList.do',
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
    		if(type==='DCI'){
		    	$('#wbsCodeMultiBox').html(html);
		    	$('.selectWbsCode').click(function(){
		    		$('#wbsCodeSelectWrap').removeClass('on');
		    		$('#wbsCodeSelected').html($(this).children().html());
		    		$('#selectedWbsCodeId').val($(this).children().attr('id'));
			    	$('#popup_wbs_detail_1').val($(this).children().next().next().html());
			    	$('#popup_wbs_detail_2').val($(this).children().next().html());
		    	});
	    	}else if(type==='VDCI'){
		    	$('#vendorWbsCodeMultiBox').html(html);
		    	$('.selectWbsCode').click(function(){
		    		$('#vendorWbsCodeSelectWrap').removeClass('on');
		    		$('#vendorWbsCodeSelected').html($(this).children().html());
		    		$('#vendorSelectedWbsCodeId').val($(this).children().attr('id'));
		    		$('#vendor_wbs_detail_1').val($(this).children().next().next().html());
		    		$('#vendor_wbs_detail_2').val($(this).children().next().html());
		    	});
	    	}else if(type==='PCI'){
		    	$('#procWbsCodeMultiBox').html(html);
		    	$('.selectWbsCode').click(function(){
		    		$('#procWbsCodeSelectWrap').removeClass('on');
		    		$('#procWbsCodeSelected').html($(this).children().html());
		    		$('#procSelectedWbsCodeId').val($(this).children().attr('id'));
		    		$('#proc_wbs_detail_1').val($(this).children().next().next().html());
		    		$('#proc_wbs_detail_2').val($(this).children().next().html());
		    	});
	    	}else if(type==='SDCI'){
		    	$('#siteWbsCodeMultiBox').html(html);
		    	$('.selectWbsCode').click(function(){
		    		$('#siteWbsCodeSelectWrap').removeClass('on');
		    		$('#siteWbsCodeSelected').html($(this).children().html());
		    		$('#siteSelectedWbsCodeId').val($(this).children().attr('id'));
		    		$('#site_wbs_detail_1').val($(this).children().next().next().html());
		    		$('#site_wbs_detail_2').val($(this).children().next().html());
		    	});
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getDocCategoryMultiBox(type){
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type: 'DOC.CATEGORY'
	    },
	    success: function onData (data) {
            var html = '<tr><th>CODE</th><th>DESCRIPTION</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectDocCategory"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td><td>'+data[0][i].set_desc+'</td></tr>';
            }
            if(type==='DCI'){
    	    	$('#docCategoryMultiBox').html(html);
    	    	
    	    	$('.selectDocCategory').click(function(){
    	    		$('#docCategorySelectWrap').removeClass('on');
    	    		$('#docCategorySelected').html($(this).children().html());
    	    		$('#selectedDocCateogrySetCodeId').val($(this).children().attr('id'));
    	    	});
            }else if(type==='SDCI'){
    	    	$('#siteDocCategoryMultiBox').html(html);
    	    	
    	    	$('.selectDocCategory').click(function(){
    	    		$('#siteDocCategorySelectWrap').removeClass('on');
    	    		$('#siteDocCategorySelected').html($(this).children().html());
    	    		$('#siteSelectedDocCateogrySetCodeId').val($(this).children().attr('id'));
    	    	});
            }
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getVendorDocCategoryMultiBox(type){
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
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
function getDocSizeMultiBox(type){
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type: 'DOC.SIZE'
	    },
	    success: function onData (data) {
            var html = '<tr><th>SIZE</th></tr>';
            for(i=0;i<data[0].length;i++){
            	html += '<tr class="selectDocSize"><td id="'+data[0][i].set_code_id+'">'+data[0][i].set_code+'</td></tr>';
            }
            if(type==='DCI'){
		    	$('#docSizeMultiBox').html(html);
		    	
		    	$('.selectDocSize').click(function(){
		    		$('#docSizeSelectWrap').removeClass('on');
		    		$('#docSizeSelected').html($(this).children().html());
		    		$('#selectedDocSizeSetCodeId').val($(this).children().attr('id'));
		    	});
            }else if(type==='SDCI'){
		    	$('#siteDocSizeMultiBox').html(html);
		    	
		    	$('.selectDocSize').click(function(){
		    		$('#siteDocSizeSelectWrap').removeClass('on');
		    		$('#siteDocSizeSelected').html($(this).children().html());
		    		$('#siteSelectedDocSizeSetCodeId').val($(this).children().attr('id'));
		    	});
            }
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getVendorDocSizeMultiBox(){
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
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
	    url: 'getSTEPPrjCodeSettingsList.do',
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
function designerSearchOpen(){
    $(".pop_userSch").dialog('open');
    getDCAllUserList();
}
function getDCAllUserList(){
	$.ajax({
	    url: 'getAllUsersVOList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var userList = [];
	    	for(i=0;i<data[0].length;i++){
	    		userList.push({
                    No: i+1,
		            userId: data[0][i].user_id,
		            KorName: data[0][i].user_kor_nm,
		            group: '',
		            EngName: data[0][i].user_eng_nm,
		            email: data[0][i].email_addr,
		            company: data[0][i].company_nm
	    		});
	    	}
	    	setDCAllUserList(userList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function searchDesigner(){
	var searchUser = $('input[name="searchDesigner"]:checked').val();
	var searchKeyword = $('#designerSearchKeyword').val();
	if(searchKeyword=='' || searchKeyword==undefined || searchKeyword==null){
		alert('검색어를 입력해주세요.');
	}else{
		$.ajax({
		    url: 'searchUsersVO.do',
		    type: 'POST',
		    data:{
		    	searchUser:searchUser, // 라디오 버튼 값
		    	searchKeyword:searchKeyword // 검색창 값
		    },
		    success: function onData (data) {
		    	var userList = [];
		    	for(i=0;i<data[0].length;i++){
		    		userList.push({
	                    No: i+1,
			            userId: data[0][i].user_id,
			            KorName: data[0][i].user_kor_nm,
			            group: '',
			            EngName: data[0][i].user_eng_nm,
			            email: data[0][i].email_addr,
			            company: data[0][i].company_nm
		    		});
		    	}
		    	setDCAllUserList(userList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function setDCAllUserList(userList){
    searchDesignerTable = new Tabulator("#userList3", {
      data: userList,
      index: "userId",
      layout: "fitColumns",
      height: 200,
      selectable:1,
      columns: [
        {
          title: "No",
          field: "No",
        },
        {
          title: "User ID",
          field: "userId",
        },
        {
          title: "Kor.Name",
          field: "KorName",
        },
        {
          title: "Group",
          field: "group",
        },
        {
          title: "Eng.Name",
          field: "EngName",
        },
        {
          title: "E-mail",
          field: "email",
        },
        {
          title: "Company",
          field: "company",
        },
      ],
    });
}
function designerSelectConfirm(type){
	var user_id = searchDesignerTable.getSelectedData()[0].userId;
	var user_kor_nm = searchDesignerTable.getSelectedData()[0].KorName;
	if(type==='DCI'){
		$('#popup_designer_id').val(user_id);
		$('#popup_designer').val(user_kor_nm+'('+user_id+')');
		if(popup_new_or_update==='new'){
			$('#popup_pre_designer_id').val(user_id);
			$('#popup_pre_designer').val(user_kor_nm+'('+user_id+')');
		}
	}else if(type==='VDCI'){
		$('#vendor_designer_id').val(user_id);
		$('#vendor_designer').val(user_kor_nm+'('+user_id+')');
		if(vendor_new_or_update==='new'){
			$('#vendor_pre_designer_id').val(user_id);
			$('#vendor_pre_designer').val(user_kor_nm+'('+user_id+')');
		}
	}else if(type==='PCI'){
		$('#proc_designer_id').val(user_id);
		$('#proc_designer').val(user_kor_nm+'('+user_id+')');
		if(proc_new_or_update==='new'){
			$('#proc_pre_designer_id').val(user_id);
			$('#proc_pre_designer').val(user_kor_nm+'('+user_id+')');
		}
	}else if(type==='SDCI'){
		$('#site_designer_id').val(user_id);
		$('#site_designer').val(user_kor_nm+'('+user_id+')');
		if(site_new_or_update==='new'){
			$('#site_pre_designer_id').val(user_id);
			$('#site_pre_designer').val(user_kor_nm+'('+user_id+')');
		}
	}
    $(".pop_userSch").dialog('close');
}
function getCorrFromToMultiBox(senderOrReceiver){
	$.ajax({
	    url: 'getCorrFromToList.do',
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
	    url: 'getCorrTypeList.do',
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
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getCorrResponMultiBox(){
	$.ajax({
	    url: 'getCorrResponsibilityList.do',
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
function getCorrSerialAuto(not){
	let set_code_type='';
	let serial = $('#corr_serial').val()===''?0:$('#corr_serial').val();
	let inout = $('input[name="inout"]:checked').val();
	let set_code = $('#corrTypeSelected').html();
	let sender = $('#corrSenderSelected').html();
	let receiver = $('#corrReceiverSelected').html();
	let sender_code_id = $('#corrSelectedSenderSetCodeId').val();
	let receiver_code_id = $('#corrSelectedReceiverSetCodeId').val();
	let doc_type_code_id = $('#corrSelectedTypeSetCodeId').val();
	if(inout===null || inout===undefined || inout===''){
		alert('IN/OUT을 설정해주세요.');
	}else if(set_code===null || set_code===undefined || set_code===''){
		alert('TYPE을 설정해주세요.');
	}else if(sender_code_id===null || sender_code_id===undefined || sender_code_id===''){
		alert('Sender를 설정해주세요.');
	}else if(receiver_code_id===null || receiver_code_id===undefined || receiver_code_id===''){
		alert('Receiver를 설정해주세요.');
	}else{
		if(inout==='i'){
			set_code_type='CORRESPONDENCE_IN';
		}else if(inout==='O'){
			set_code_type='CORRESPONDENCE_OUT';
		}
		$.ajax({
		    url: 'getCorrSerialAuto.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	set_code_type:set_code_type,
		    	set_code:set_code,
		    	sender_code_id:sender_code_id,
		    	receiver_code_id:receiver_code_id,
		    	doc_type_code_id:doc_type_code_id,
		    	serial:serial
		    },
		    success: function onData (data) {
	            if(not){
	            	if(data[2]>0){
	            		alert('Used Correspondence No.');
	            	}else{
	            		alert('Available');
	            	}
	            }else{
			    	$('#corr_serial_realdata').val(data[1]);
			    	$('#corr_corr_no_1').val(sender+'-'+receiver+'-'+set_code+'-'+data[0]);
	            }
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function setCorrInit(){
	var set_code_type='';
	if(current_folder_path.indexOf('OUTGOING') != -1){
		$('input[name="inout"][value="O"]').prop('checked',true);
		//$('input[name="inout"][value="I"]').attr('disabled',true);
		set_code_type='CORRESPONDENCE_OUT';
	}else if(current_folder_path.indexOf('INCOMING') != -1){
		$('input[name="inout"][value="I"]').prop('checked',true);
		//$('input[name="inout"][value="O"]').attr('disabled',true);
		set_code_type='CORRESPONDENCE_IN';
	}
	var set_code = '';
	if(current_folder_path.indexOf('E-MAIL') != -1){
		set_code = 'EL';
	}else if(current_folder_path.indexOf('LETTER') != -1){
		set_code = 'LT';
	}
	if(set_code_type!='' && set_code!=''){
		 $.ajax({
				type: 'POST',
				url: 'getCorrInit.do',
				data: {
					prj_id:selectPrjId,
					set_code_type: set_code_type,
					set_code:set_code
				},
				success: function(data) {
					$('#corrSenderSelected').html(data[0].from_code);
					$('#corrSelectedSenderSetCodeId').val(data[0].from_code_id);
					$('#corrReceiverSelected').html(data[0].to_code);
					$('#corrSelectedReceiverSetCodeId').val(data[0].to_code_id);
					$('#corrTypeSelected').html(data[0].type_code);
					$('#corrSelectedTypeSetCodeId').val(data[0].type_code_id);
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	}
}
function CorrDocSearch(){
	let corr_no = $('#corr_corr_no_1').val();
	$.ajax({
			type: 'POST',
			url: 'getCorrNoSearch.do',
			data: {
				prj_id:selectPrjId,
				corr_no: corr_no
			},
			success: function(data) {
				if(data[0]!==null){
		            var html = '<tr><th>In/Out</th><th>Document No</th><th>Title</th><th>Document Date</th><th>Responsibility</th><th>Originator</th><th>Modified Time</th></tr>';
		            for(i=0;i<data[0].length;i++){
		            	html += '<tr class="selectCorrNo"><td>'+data[0][i].in_out+'</td><td>'+data[0][i].doc_no+'</td><td>'+data[0][i].title+'</td><td>'+data[0][i].doc_date+'</td><td>'+data[0][i].responsibility_code+'</td><td>'+data[0][i].originator+'</td><td>'+data[0][i].mod_date+'</td></tr>';
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
	let corr_no = $('#corr_corr_no_2').val();
	$.ajax({
			type: 'POST',
			url: 'getCorrNoSearch.do',
			data: {
				prj_id:selectPrjId,
				corr_no: corr_no
			},
			success: function(data) {
				if(data[0]!==null){
		            var html = '<tr><th>In/Out</th><th>Document No</th><th>Title</th><th>Document Date</th><th>Responsibility</th><th>Originator</th><th>Modified Time</th></tr>';
		            for(i=0;i<data[0].length;i++){
		            	html += '<tr class="selectCorrNo2"><td>'+data[0][i].in_out+'</td><td>'+data[0][i].doc_no+'</td><td>'+data[0][i].title+'</td><td>'+data[0][i].doc_date+'</td><td>'+data[0][i].responsibility_code+'</td><td>'+data[0][i].originator+'</td><td>'+data[0][i].mod_date+'</td></tr>';
		            }
			    	$('#corrNo2MultiBox').html(html);
			    	$('.selectCorrNo2').click(function(){
			    		$('#corrNo2SelectWrap').removeClass('on');
			    		$('#corrNo2Selected').html($(this).children().next().html());
			    	});
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
	});
}
function serialChange(){
	let serial = $('#corr_serial').val();
	if(serial!==''){
		$('#autoSerial').css('display','none');
		$('#notAutoSerial').css('display','inline-block');
	}else{
		$('#autoSerial').css('display','inline-block');
		$('#notAutoSerial').css('display','none');
	}
}
function refPlus(){
	let refDoc = $('#corrNo2Selected').html();
	let corr_ref_doc_no_array = $('#corr_ref_doc_no').val().split(',');
	if(refDoc!=='Search'){
		if(corr_ref_doc_no_array[0]===''){
			$('#corr_ref_doc_no').val(refDoc);
		}else{
			if(!corr_ref_doc_no_array.includes(refDoc)) corr_ref_doc_no_array.push(refDoc);
			$('#corr_ref_doc_no').val(corr_ref_doc_no_array.join(','));
		}
	}
}
function refMinus(){
	let refDoc = $('#corrNo2Selected').html();
	let corr_ref_doc_no_array = $('#corr_ref_doc_no').val().split(',');
	if(corr_ref_doc_no_array.includes(refDoc)) corr_ref_doc_no_array = corr_ref_doc_no_array.filter((element) => element !== refDoc);
	$('#corr_ref_doc_no').val(corr_ref_doc_no_array.join(','));
}
function getPrjStep(doc_id,rev_id,type,stepNew){
	var set_code_type = '';
	if(type==='DCI'){
		set_code_type = 'ENGINEERING STEP';
	}else if(type==='VDCI'){
		set_code_type = 'VENDOR DOC STEP';
	}else if(type==='PCI'){
		set_code_type = 'PROCUREMENT STEP';
	}else if(type==='SDCI'){
		set_code_type = 'SITE DOC STEP';
	}
	
	 $.ajax({
			type: 'POST',
			url: 'getPrjStep.do',
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
        		if(rowDataObject[key]!=='--' && rowDataObject[key]!=='Invalid date' && rowDataObject[key]!==undefined && rowDataObject[key]!==null && rowDataObject[key]!=='')
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
	
	var tableId = '';
	if(type==='DCI'){
		StepTable = DCEngStepTable;
		tableId = '#docProgress';
	}else if(type==='VDCI'){
		StepTable = DCVendorStepTable;
		tableId = '#vendorDocProgress';
	}else if(type==='PCI'){
		StepTable = DCProcStepTable;
		tableId = '#procDocProgress';
	}else if(type==='SDCI'){
		StepTable = DCSiteStepTable;
		tableId = '#siteDocProgress';
	}
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

	var tableId = '';
	if(type==='DCI'){
		StepTable = DCEngStepTable;
		tableId = '#docProgress';
	}else if(type==='VDCI'){
		StepTable = DCVendorStepTable;
		tableId = '#vendorDocProgress';
	}else if(type==='PCI'){
		StepTable = DCProcStepTable;
		tableId = '#procDocProgress';
	}else if(type==='SDCI'){
		StepTable = DCSiteStepTable;
		tableId = '#siteDocProgress';
	}
	StepTable = new Tabulator(tableId, {
		  data:docProgressArray,
	      layout: "fitColumns",
	      height: 135,
	      columns: defaultColumns,
	      movableColumns: false
	});
}
function getPrjTrHistory(doc_id,rev_id,type){
	 $.ajax({
			type: 'POST',
			url: 'getPrjTrHistory.do',
			data: {
				prj_id:selectPrjId,
				doc_id:doc_id,
				rev_id:rev_id
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
	var tableId = '';
	if(type==='DCI'){
		tableId = '#transHis';
	}else if(type==='VDCI'){
		tableId = '#vendorTransHis';
	}else if(type==='SDCI'){
		tableId = '#siteTransHis';
	}
    var table = new Tabulator(tableId, {
        data: transHis,
        layout: "fitColumns",
        height: 130,
        placeholder:"No Data Set",
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
function getEngPrjTrHistoryNew(){
	let transHis = [];
	setPrjTrHistory(transHis,'DCI');
}
function getVendorPrjTrHistoryNew(){
	let transHis = [];
	setPrjTrHistory(transHis,'VDCI');
}
function getSitePrjTrHistoryNew(){
	let transHis = [];
	setPrjTrHistory(transHis,'SDCI');
}
function getPrjDrnHistory(doc_id,rev_id){
	 $.ajax({
			type: 'POST',
			url: 'getPrjDrnHistory.do',
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
						status = '검토완료';
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
				$('#engDrnHistory').html(html);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function setPrjDrnHistoryNew(){
	$('#engDrnHistory').html('');
}
function engDocSave(){
	let doc_id = popup_new_or_update==='new'?'':eng_selected_doc_id;
	let rev_id = popup_new_or_update==='new'?'':eng_selected_rev_id;
	let doc_no = $('#popup_doc_no').val();
	let origin_rev_code_id = $('#origin_rev_code_id').val();
	let rev_code_id = $('#selectedRevNoSetCodeId').val();
	let folder_id = parseInt(current_folder_id);
	let title = $('#popup_title').val();
	let discip_code_id = $('#popup_discipline_code_id').val();
	let designer_id = $('#popup_designer_id').val();
	let pre_designer_id = $('#popup_pre_designer_id').val();
	let wbs_code_id = $('#selectedWbsCodeId').val();
	let remark = $('#popup_remark').val();
	let category_cd_id = $('#selectedDocCateogrySetCodeId').val();
	let size_code_id = $('#selectedDocSizeSetCodeId').val();
	let doc_type_id = $('#selectedDocTypeSetCodeId').val();
    let file_value = $('#eng_attach_filename').val();
	
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
			popup_new_or_update:popup_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:'DCI',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			title:title,
			discip_code_id:discip_code_id,
			designer_id:designer_id,
			prfe_designer_id:pre_designer_id,
			wbs_code_id:wbs_code_id,
			remark:remark,
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
		let form = $('#updateRevisionForm')[0];
	    let formData = new FormData(form);
	    
	    formData.set('prj_id',selectPrjId);
	    formData.set('popup_new_or_update',popup_new_or_update);
	    formData.set('origin_rev_code_id',origin_rev_code_id);
	    formData.set('doc_id',doc_id);
	    formData.set('rev_id',rev_id);
	    formData.set('doc_type','DCI');
	    formData.set('doc_no',doc_no);
	    formData.set('rev_code_id',rev_code_id);
	    formData.set('folder_id',folder_id);
	    formData.set('title',title);
	    formData.set('discip_code_id',discip_code_id);
	    formData.set('designer_id',designer_id);
	    formData.set('prfe_designer_id',pre_designer_id);
	    formData.set('wbs_code_id',wbs_code_id);
	    formData.set('remark',remark);
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
	    formData.set('set_code_type','ENGINEERING STEP');
	    
	    
	    $.ajax({
			type: 'POST',
	        enctype: 'multipart/form-data',
			url: 'engDocSave.do',
	        data:formData,          
	        processData: false,    
	        contentType: false,      
	        cache: false,           
	        // timeout: 600000,  
			success: function(data) {
				if(data[0] == 'FAIL'){
					let msg = data[1];
					alert(msg);
					return;
				}
				else if(data[0]>0 && data[1]>0){
					getPopupPrjDocumentIndexList('DCI');
					alert('Save Completed');
					$.ajax({
		    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
		    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
		    		    data:{				// 이동할때 챙겨야하는 데이터
		    		    	prj_id: selectPrjId,
		    		    	doc_id: doc_id,
		    		    	rev_id: rev_id,
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
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		}); 
	    
	    
	    /*
	    $.ajax({
			url: 'engDocSaveChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
		
		*/
		 /*$.ajax({
				type: 'POST',
				url: 'engDocSave.do',
				data: params,
				success: function(data) {
					if(data[0]>0 && data[1]>0){
						getPopupPrjDocumentIndexList('DCI');
						alert('Save Completed');
					}else{
						alert('save failed!');
					}
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});*/
	}
}
function updateRevision(){
	let doc_id = popup_new_or_update==='new'?'':eng_selected_doc_id;
	let rev_id = popup_new_or_update==='new'?'':eng_selected_rev_id;
	let doc_no = $('#popup_doc_no').val();
	let title = $('#popup_title').val();
	let origin_rev_code_id = $('#origin_rev_code_id').val();
	let rev_code_id = $('#selectedRevNoSetCodeId').val();
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
			popup_new_or_update:popup_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			origin_rev_id:rev_id,
			doc_type:'DCI',
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
			set_code_type:'ENGINEERING STEP'
	};

	let form = $('#updateRevisionForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',popup_new_or_update);
    formData.set('origin_rev_code_id',origin_rev_code_id);
    formData.set('doc_id',doc_id);
    formData.set('origin_rev_id',rev_id);
    formData.set('doc_type','DCI');
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
    formData.set('set_code_type','ENGINEERING STEP');
    
    let attachfilename = $('#eng_popup_attachfilename').val();
    if(attachfilename==='파일선택' || attachfilename===''){
    	alert('Attach File! 리비전이 바뀌면 첨부파일을 바꾸셔야 합니다.');
    }else{
    	$.ajax({
			url: 'updateRevisionChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'updateRevision.do',         
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,  
						success: function(data) {
							if(data[0]>0){
								getPopupPrjDocumentIndexList('DCI');
								$('#popup_save').attr('disabled',false);
								$('#popup_revise').attr('disabled',true);
								getDocumentInfo(doc_id,data[1]);
								getRevNoMultiBox('DCI',doc_id);
								alert('CheckIn Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
					    		    	doc_no: doc_no,
					    		    	doc_title: title,
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
						},
					    error: function onError (error) {
					        console.error(error);
							alert('Update Revision is failed!');
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
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
			url: 'docSaveChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        		processData:false,
	        		contentType:false,
	        		dataType:'json',
	        		cache:false,
	        		async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'vendorDocSave.do',
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,  
						success: function(data) {
							if(data[0]>0 && data[1]>0){
								getPopupPrjDocumentIndexList('VDCI');
								alert('Save Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
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
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			}
	    });
	}
}
function updateRevisionVendor(){
	let doc_id = vendor_new_or_update==='new'?'':vendor_selected_doc_id;
	let rev_id = vendor_new_or_update==='new'?'':vendor_selected_rev_id;
	let doc_no = $('#vendor_doc_no').val();
	let title = $('#vendor_title').val();
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
    
    let attachfilename = $('#vendor_attachfilename').val();
    if(attachfilename==='파일선택' || attachfilename===''){
    	alert('Attach File! 리비전이 바뀌면 첨부파일을 바꾸셔야 합니다.');
    }else{
	 $.ajax({
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'updateRevisionVendor.do',         
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,
						success: function(data) {
							if(data[0]>0){
								getPopupPrjDocumentIndexList('VDCI');
								$('#vendor_save').attr('disabled',false);
								$('#vendor_revise').attr('disabled',true);
								getDocumentInfo(doc_id,data[1]);
								getRevNoMultiBox('VDCI',doc_id);
								alert('CheckIn Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
					    		    	doc_no: doc_no,
					    		    	doc_title: title,
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
						},
					    error: function onError (error) {
					        console.error(error);
							alert('Update Revision is failed!');
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			}
		});
    }
}
function procDocSave(){
	let doc_id = proc_new_or_update==='new'?'':proc_selected_doc_id;
	let rev_id = proc_new_or_update==='new'?'':proc_selected_rev_id;
	let doc_no = $('#proc_doc_no').val();
	let origin_rev_code_id = $('#proc_origin_rev_code_id').val();
	let rev_code_id = $('#procSelectedRevNoSetCodeId').val();
	let folder_id = parseInt(current_folder_id);
	let title = $('#proc_title').val();
	let discip_code_id = $('#proc_discipline_code_id').val();
	let designer_id = $('#proc_designer_id').val();
	let pre_designer_id = $('#proc_pre_designer_id').val();
	let wbs_code_id = $('#procSelectedWbsCodeId').val();
	let remark = $('#proc_remark').val();
	let por_no = $('#proc_por_no').val();
	let po_no = $('#proc_po_no').val();
	let por_issue_date = $('#proc_por_issue_date').val();
	let po_issue_date = $('#proc_po_issue_date').val();
	let vendor_nm = $('#proc_vendor_nm').val();
	let vendor_doc_no = $('#proc_vendor_doc_no').val();
    let file_value = $('#proc_attach_filename').val();
	
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
			popup_new_or_update:proc_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:'PCI',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			title:title,
			discip_code_id:discip_code_id,
			designer_id:designer_id,
			prfe_designer_id:pre_designer_id,
			wbs_code_id:wbs_code_id,
			remark:remark,
			por_no:por_no,
			po_no:po_no,
			por_issue_date:por_issue_date,
			po_issue_date:po_issue_date,
			vendor_nm:vendor_nm,
			vendor_doc_no:vendor_doc_no,
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
	}else if(origin_rev_code_id==='' && file_value!==''){
		alert('Revision');
	}else{
		let form = $('#updateRevisionProcForm')[0];
	    let formData = new FormData(form);
	    
	    formData.set('prj_id',selectPrjId);
	    formData.set('popup_new_or_update',proc_new_or_update);
	    formData.set('origin_rev_code_id',origin_rev_code_id);
	    formData.set('doc_id',doc_id);
	    formData.set('rev_id',rev_id);
	    formData.set('doc_type','PCI');
	    formData.set('doc_no',doc_no);
	    formData.set('rev_code_id',rev_code_id);
	    formData.set('folder_id',folder_id);
	    formData.set('title',title);
	    formData.set('discip_code_id',discip_code_id);
	    formData.set('designer_id',designer_id);
	    formData.set('prfe_designer_id',pre_designer_id);
	    formData.set('wbs_code_id',wbs_code_id);
	    formData.set('remark',remark);
	    formData.set('por_no',por_no);
	    formData.set('po_no',po_no);
	    formData.set('por_issue_date',por_issue_date);
	    formData.set('po_issue_date',po_issue_date);
	    formData.set('vendor_nm',vendor_nm);
	    formData.set('vendor_doc_no',vendor_doc_no);
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
	    formData.set('set_code_type','PROCUREMENT STEP');
	    
	    $.ajax({
			url: 'docSaveChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'procDocSave.do',
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,  
						success: function(data) {
							if(data[0]>0 && data[1]>0){
								getPopupPrjDocumentIndexList('PCI');
								alert('Save Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
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
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
	}
}
function updateRevisionProc(){
	let doc_id = proc_new_or_update==='new'?'':proc_selected_doc_id;
	let rev_id = proc_new_or_update==='new'?'':proc_selected_rev_id;
	let doc_no = $('#proc_doc_no').val();
	let title = $('#proc_title').val();
	let origin_rev_code_id = $('#proc_origin_rev_code_id').val();
	let rev_code_id = $('#procSelectedRevNoSetCodeId').val();
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
			popup_new_or_update:proc_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			origin_rev_id:rev_id,
			doc_type:'PCI',
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
			set_code_type:'PROCUREMENT STEP'
	};

	let form = $('#updateRevisionProcForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',proc_new_or_update);
    formData.set('origin_rev_code_id',origin_rev_code_id);
    formData.set('doc_id',doc_id);
    formData.set('origin_rev_id',rev_id);
    formData.set('doc_type','PCI');
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
    formData.set('set_code_type','PROCUREMENT STEP');

    let attachfilename = $('#proc_attachfilename').val();
    if(attachfilename==='파일선택' || attachfilename===''){
    	alert('Attach File! 리비전이 바뀌면 첨부파일을 바꾸셔야 합니다.');
    }else{
	 $.ajax({
		 	url: 'updateRevisionChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'updateRevisionProc.do',         
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,
						success: function(data) {
							if(data[0]>0){
								getPopupPrjDocumentIndexList('PCI');
								$('#proc_save').attr('disabled',false);
								$('#proc_revise').attr('disabled',true);
								getDocumentInfo(doc_id,data[1]);
								getRevNoMultiBox('PCI',doc_id);
								alert('CheckIn Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
					    		    	doc_no: doc_no,
					    		    	doc_title: title,
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
						},
					    error: function onError (error) {
					        console.error(error);
							alert('Update Revision is failed!');
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			}
		});
    }
}
function siteDocSave(){
	let doc_id = site_new_or_update==='new'?'':site_selected_doc_id;
	let rev_id = site_new_or_update==='new'?'':site_selected_rev_id;
	let doc_no = $('#site_doc_no').val();
	let origin_rev_code_id = $('#site_origin_rev_code_id').val();
	let rev_code_id = $('#siteSelectedRevNoSetCodeId').val();
	let folder_id = parseInt(current_folder_id);
	let title = $('#site_title').val();
	let discip_code_id = $('#site_discipline_code_id').val();
	let designer_id = $('#site_designer_id').val();
	let pre_designer_id = $('#site_pre_designer_id').val();
	let wbs_code_id = $('#siteSelectedWbsCodeId').val();
	let remark = $('#site_remark').val();
	let category_cd_id = $('#siteSelectedDocCateogrySetCodeId').val();
	let size_code_id = $('#siteSelectedDocSizeSetCodeId').val();
	let doc_type_id = $('#siteSelectedDocTypeSetCodeId').val();
    let file_value = $('#site_attach_filename').val();
	
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
			popup_new_or_update:site_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:'SDCI',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			title:title,
			discip_code_id:discip_code_id,
			designer_id:designer_id,
			prfe_designer_id:pre_designer_id,
			wbs_code_id:wbs_code_id,
			remark:remark,
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
		let form = $('#updateRevisionSiteForm')[0];
	    let formData = new FormData(form);
	    
	    formData.set('prj_id',selectPrjId);
	    formData.set('popup_new_or_update',site_new_or_update);
	    formData.set('origin_rev_code_id',origin_rev_code_id);
	    formData.set('doc_id',doc_id);
	    formData.set('rev_id',rev_id);
	    formData.set('doc_type','SDCI');
	    formData.set('doc_no',doc_no);
	    formData.set('rev_code_id',rev_code_id);
	    formData.set('folder_id',folder_id);
	    formData.set('title',title);
	    formData.set('discip_code_id',discip_code_id);
	    formData.set('designer_id',designer_id);
	    formData.set('prfe_designer_id',pre_designer_id);
	    formData.set('wbs_code_id',wbs_code_id);
	    formData.set('remark',remark);
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
	    formData.set('set_code_type','SITE DOC STEP');
	    
	    $.ajax({
			url: 'docSaveChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'siteDocSave.do',
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,  
						success: function(data) {
							if(data[0]>0 && data[1]>0){
								getPopupPrjDocumentIndexList('SDCI');
								alert('Save Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
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
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
	}
}
function updateRevisionSite(){
	let doc_id = site_new_or_update==='new'?'':site_selected_doc_id;
	let rev_id = site_new_or_update==='new'?'':site_selected_rev_id;
	let doc_no = $('#site_doc_no').val();
	let title = $('#site_title').val();
	let origin_rev_code_id = $('#site_origin_rev_code_id').val();
	let rev_code_id = $('#siteSelectedRevNoSetCodeId').val();
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
			popup_new_or_update:site_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			origin_rev_id:rev_id,
			doc_type:'SDCI',
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
			set_code_type:'SITE DOC STEP'
	};

	let form = $('#updateRevisionSiteForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',site_new_or_update);
    formData.set('origin_rev_code_id',origin_rev_code_id);
    formData.set('doc_id',doc_id);
    formData.set('origin_rev_id',rev_id);
    formData.set('doc_type','SDCI');
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
    formData.set('set_code_type','SITE DOC STEP');

    let attachfilename = $('#site_popup_attachfilename').val();
    if(attachfilename==='파일선택' || attachfilename===''){
    	alert('Attach File! 리비전이 바뀌면 첨부파일을 바꾸셔야 합니다.');
    }else{
    	$.ajax({
			url: 'updateRevisionChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'updateRevisionSite.do',         
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,
						success: function(data) {
							if(data[0]>0){
								getPopupPrjDocumentIndexList('SDCI');
								$('#site_save').attr('disabled',false);
								$('#site_revise').attr('disabled',true);
								getDocumentInfo(doc_id,data[1]);
								getRevNoMultiBox('SDCI',doc_id);
								alert('CheckIn Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
					    		    	doc_no: doc_no,
					    		    	doc_title: title,
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
						},
					    error: function onError (error) {
					        console.error(error);
							alert('Update Revision is failed!');
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
    }
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
	let serial = parseInt(corrNoArray[corrNoArray.length-1]);
	let doc_no = $('#corr_corr_no_1').val();
	let corr_no = $('#corr_corr_no_1').val();
	let ref_corr_doc_no = $('#corr_ref_doc_no').val();
	let responsibility_code_id = $('#corrSelectedResponSetCodeId').val();
	let originator = $('#corr_originator').val();
	let disact = '';
	$('input:checkbox[name="corr_distribute"]:checked').each(function() {
		disact += $(this).val() + ',';
	})
	disact = disact.slice(0,-1);
	let replyreqdate = $('#corr_rep_req_date').val().replaceAll('-','');
	let indistdate = $('#corr_int_dist_date').val().replaceAll('-','');
	let inactdate = $('#corr_act_req_date').val().replaceAll('-','');
	let userdata00 = $('#corr_userdate_00').val();
	let userdata01 = $('#corr_userdate_01').val();
	let userdata02 = $('#corr_userdate_02').val();
	let userdata03 = $('#corr_userdate_03').val();
	let userdata04 = $('#corr_userdate_04').val();
	let replydocno = $('#corr_reference_by').val();//???
	
	var params = {
			prj_id:selectPrjId,
			popup_new_or_update:corr_new_or_update,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:'Corr',
			folder_id:folder_id,
			title:title,
			remark:remark,
			in_out:in_out,
			doc_date:doc_date,
			sender_code_id:sender_code_id,
			receiver_code_id:receiver_code_id,
			doc_type_code_id:doc_type_code_id,
			serial:serial,
			doc_no:doc_no,
			corr_no:corr_no,
			ref_corr_doc_no:ref_corr_doc_no,
			responsibility_code_id:responsibility_code_id,
			originator:originator,
			disact:disact,
			replyreqdate:replyreqdate,
			indistdate:indistdate,
			inactdate:inactdate,
			userdata00:userdata00,
			userdata01:userdata01,
			userdata02:userdata02,
			userdata03:userdata03,
			userdata04:userdata04,
			replydocno:replydocno
	};

	let form = $('#updateRevisionCorrForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',corr_new_or_update);
    formData.set('doc_id',doc_id);
    formData.set('rev_id',rev_id);
    formData.set('doc_type','Corr');
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
    formData.set('serial',serial);
    formData.set('doc_no',doc_no);
    formData.set('corr_no',corr_no);
    formData.set('ref_corr_doc_no',ref_corr_doc_no);
    formData.set('responsibility_code_id',responsibility_code_id);
    formData.set('originator',originator);
    formData.set('disact',disact);
    formData.set('replyreqdate',replyreqdate);
    formData.set('indistdate',indistdate);
    formData.set('inactdate',inactdate);
    formData.set('userdata00',userdata00);
    formData.set('userdata01',userdata01);
    formData.set('userdata02',userdata02);
    formData.set('userdata03',userdata03);
    formData.set('userdata04',userdata04);
    formData.set('replydocno',replydocno);
    
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
	}/*else if(ref_corr_doc_no==='' || ref_corr_doc_no===null || ref_corr_doc_no===undefined){
		alert('REF. DOC NO를 설정해주세요.');	//필수값 맞는지 확인해야함
	}*/else{
		if($('#corr_attach_filename').val()!==''){
			$.ajax({
				url: 'docSaveChkFile.do',
				type: 'POST',
				data: formData,
				enctype:'multipart/form-data',
		        processData:false,
		        contentType:false,
		        dataType:'json',
		        cache:false,
		        async: false,
				success: function onData (data) {
					if(data > 0) {
						$.ajax({
							type: 'POST',
					        enctype: 'multipart/form-data',
							url: 'corrDocSaveAttach.do',        
					        data:formData,          
					        processData: false,    
					        contentType: false,      
					        cache: false,           
					        timeout: 600000,
							success: function(data) {
								if(data[0]>0){
									getPopupPrjDocumentIndexList('Corr');
									alert('Save Completed');
									
									$.ajax({
						    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
						    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
						    		    data:{				// 이동할때 챙겨야하는 데이터
						    		    	prj_id: selectPrjId,
						    		    	doc_id: doc_id,
						    		    	rev_id: rev_id,
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
							},
						    error: function onError (error) {
						        console.error(error);
						    }
						});
					}else {
						alert("Unsupported file format. Please contact the administrator");
					}
				},
				error: function onError (error) {
			    	console.error(error);
//			        alert("등록 오류");
			    }
			});
		}else{
			 $.ajax({
					type: 'POST',
					url: 'corrDocSave.do',        
			        data:params,
					success: function(data) {
						if(data[0]>0){
							getPopupPrjDocumentIndexList('Corr');
							alert('Save Completed');
							$.ajax({
				    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
				    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
				    		    data:{				// 이동할때 챙겨야하는 데이터
				    		    	prj_id: selectPrjId,
				    		    	doc_id: doc_id,
				    		    	rev_id: rev_id,
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
					},
				    error: function onError (error) {
				        console.error(error);
				    }
				});
		}
	}
}
function generalDocSave(){
	let doc_id = general_new_or_update==='new'?'':general_selected_doc_id;
	let rev_id = general_new_or_update==='new'?'':general_selected_rev_id;
	let doc_no = $('#general_doc_no').val();
	let origin_rev_code_id = $('#general_origin_rev_code_id').val();
	let rev_code_id = $('#generalSelectedRevNoSetCodeId').val();
	let folder_id = parseInt(current_folder_id);
	let title = $('#general_title').val();
    let file_value = $('#general_attach_filename').val();
	
	var params = {
			prj_id:selectPrjId,
			popup_new_or_update:general_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			rev_id:rev_id,
			doc_type:'General',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			title:title
	};
	if(doc_no==='' || doc_no===null || doc_no===undefined){
		alert('DOC. NO를 입력해주세요.');
	}else if(title==='' || title===null || title===undefined){
		alert('TITLE을 입력해주세요.');
	}else{
		let form = $('#updateRevisionGeneralForm')[0];
	    let formData = new FormData(form);
	    
	    formData.set('prj_id',selectPrjId);
	    formData.set('popup_new_or_update',general_new_or_update);
	    formData.set('origin_rev_code_id',origin_rev_code_id);
	    formData.set('doc_id',doc_id);
	    formData.set('rev_id',rev_id);
	    formData.set('doc_type','General');
	    formData.set('doc_no',doc_no);
	    formData.set('rev_code_id',rev_code_id);
	    formData.set('folder_id',folder_id);
	    formData.set('title',title);
	    formData.set('file_value',file_value);
	    if(file_value!==''){
		    DC.sfile_nm = doc_id+'_'+rev_id+'.'+DC.file_type;
		    formData.set('sfile_nm',DC.sfile_nm);
		    formData.set('rfile_nm',DC.rfile_nm);
		    formData.set('file_type',DC.file_type);
		    formData.set('file_size',DC.file_size);
	    }
	    
	    $.ajax({
			url: 'docSaveChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'generalDocSave.do',
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,  
						success: function(data) {
							if(data[0]>0){
								getPopupPrjDocumentIndexList('General');
								alert('Save Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
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
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
	    
	}
}
function updateRevisionGeneral(){
	let doc_id = general_new_or_update==='new'?'':general_selected_doc_id;
	let rev_id = general_new_or_update==='new'?'':general_selected_rev_id;
	let doc_no = $('#general_doc_no').val();
	let title = $('#general_title').val();
	let origin_rev_code_id = $('#general_origin_rev_code_id').val();
	let rev_code_id = $('#generalSelectedRevNoSetCodeId').val();
	let folder_id = current_folder_id;
	DC.sfile_nm = doc_id + '_' + rev_id + '.' + DC.file_type;

	var params = {
			prj_id:selectPrjId,
			popup_new_or_update:general_new_or_update,
			origin_rev_code_id:origin_rev_code_id,
			doc_id:doc_id,
			origin_rev_id:rev_id,
			doc_type:'General',
			doc_no:doc_no,
			rev_code_id:rev_code_id,
			folder_id:folder_id,
			sfile_nm:DC.sfile_nm,
			rfile_nm:DC.rfile_nm,
			file_type:DC.file_type,
			file_size:DC.file_size
	};

	let form = $('#updateRevisionGeneralForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',selectPrjId);
    formData.set('popup_new_or_update',general_new_or_update);
    formData.set('origin_rev_code_id',origin_rev_code_id);
    formData.set('doc_id',doc_id);
    formData.set('origin_rev_id',rev_id);
    formData.set('doc_type','General');
    formData.set('doc_no',doc_no);
    formData.set('rev_code_id',rev_code_id);
    formData.set('folder_id',folder_id);
    formData.set('sfile_nm',DC.sfile_nm);
    formData.set('rfile_nm',DC.rfile_nm);
    formData.set('file_type',DC.file_type);
    formData.set('file_size',DC.file_size);

    let attachfilename = $('#general_attach_filename').val();
    if(attachfilename==='파일선택' || attachfilename===''){
    	alert('Attach File! 리비전이 바뀌면 첨부파일을 바꾸셔야 합니다.');
    }else{
    	$.ajax({
			//url: 'updateRevisionChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					$.ajax({
						type: 'POST',
				        enctype: 'multipart/form-data',
						url: 'updateRevisionGeneral.do',         
				        data:formData,          
				        processData: false,    
				        contentType: false,      
				        cache: false,           
				        timeout: 600000,
						success: function(data) {
							if(data[0]>0){
								getPopupPrjDocumentIndexList('General');
								$('#general_save').attr('disabled',false);
								$('#general_revise').attr('disabled',true);
								getDocumentInfo(doc_id,data[1]);
								alert('CheckIn Completed');
								
								$.ajax({
					    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
					    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
					    		    data:{				// 이동할때 챙겨야하는 데이터
					    		    	prj_id: selectPrjId,
					    		    	doc_id: doc_id,
					    		    	rev_id: rev_id,
					    		    	doc_no: doc_no,
					    		    	doc_title: title,
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
						},
					    error: function onError (error) {
					        console.error(error);
							alert('Update Revision is failed!');
					    }
					});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
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
	let doc_id = thisTable.getSelectedData()[0].bean.doc_id;
	let rev_id = thisTable.getSelectedData()[0].bean.rev_id;
	 $.ajax({
			type: 'POST',
			url: 'getPrjDrnHistoryData.do',
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
					}/*else if(status==='C'){
						status = '메일발송완료';
					}*/
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
function pageprint_old(){
	var inbody = document.body.innerHTML; // 이전 body 영역 저장
	window.onbeforeprint = function(){ // 프린트 화면 호출 전 발생하는 이벤트
	    document.body.innerHTML = document.getElementById('pop_ed_right').innerHTML; // 원하는 영역 지정
	}
	window.onafterprint = function(){ // 프린트 출력 후 발생하는 이벤트
	    document.body.innerHTML = inbody; // 이전 body 영역으로 복구
	}
	window.print();
}

function pageprint1(docType){
	if(docType === 'DCI'){
		$("#pdfDivEng").printThis({
		    importCSS: true,            // import parent page css
		    importStyle: true,         // import style tags
		    printContainer: true,   // print outer container/$.selector
		    canvas: true              // copy canvas conten		    
		    //loadCSS: "/resource/css/tabulator.css"

		});
	}else if(docType === 'VDCI'){
		$("#pdfDivVendor").printThis({
		    importCSS: true,            // import parent page css
		    importStyle: true,         // import style tags
		    printContainer: true     // print outer container/$.selector	    
		});		
	}else if(docType === 'PCI'){
		$("#pdfDivProc").printThis({
		    importCSS: true,            // import parent page css
		    importStyle: true,         // import style tags
		    printContainer: true     // print outer container/$.selector	    
		});		
	}else if(docType === 'SDCI'){
		$("#pdfDivSite").printThis({
		    importCSS: true,            // import parent page css
		    importStyle: true,         // import style tags
		    printContainer: true     // print outer container/$.selector	    
		});		
	}
	
}

function pageprint(docType){
	if(docType === 'DCI'){
	    html2canvas($('#pdfDivEng')[0]).then(function(canvas) { //저장 영역 div id
			
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
		    doc.save('Engineering Document.pdf');
	 
	    });
	}else if(docType === 'VDCI'){
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
	}else if(docType === 'PCI'){
		    html2canvas($('#pdfDivProc')[0]).then(function(canvas) { //저장 영역 div id
				
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
			    doc.save('Procurment Document.pdf');
		 
		    });	
	}else if(docType === 'SDCI'){
		    html2canvas($('#pdfDivSite')[0]).then(function(canvas) { //저장 영역 div id
				
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
			    doc.save('Site Document.pdf');
		 
		    });		
	}
}



function engPopupClose(){
    $('.pop_docEngPopup').dialog('close');
    getPrjDocumentIndexList();
}
function vendorPopupClose(){
    $('.pop_vendorDocNew').dialog('close');
    getPrjDocumentIndexList();
}
function procPopupClose(){
    $('.pop_prcmCtrlNew').dialog('close');
    getPrjDocumentIndexList();
}
function sitePopupClose(){
    $('.pop_siteDocNew').dialog('close');
    getPrjDocumentIndexList();
}
function corrPopupClose(){
    $('.pop_corrsDocNew').dialog('close');
    getPrjDocumentIndexList();
}
function generalPopupClose(){
    $('.pop_generalDocNew').dialog('close');
    getPrjDocumentIndexList();
}
function fileRenameCheck(){
	if($('#downloadFileRename').is(':checked')){
		$('#fileRenameSet').val('%DOCNO%_%REVNO%_%TITLE%');
	}else{
		$('#fileRenameSet').val('');
	}
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

function fileMultiDownload(){
	
}

function multiFileDownload(selectPrjId,renameYN,fileRenameSet,downloadFiles){
	
	// submit을 위한 formdata 재구성 전, 이전 값 클리어
	$("#multiDownload_form [name='prj_id']").remove();
	$("#multiDownload_form [name='renameYN']").remove();
	$("#multiDownload_form [name='fileRenameSet']").remove();
	$("#multiDownload_form [name='downloadFiles']").remove();
	
	var form = $("#multiDownload_form")[0];
	
	let paramsInput_prj_id = document.createElement('input');
	paramsInput_prj_id.setAttribute('type','hidden');
	paramsInput_prj_id.setAttribute('name','prj_id');
	paramsInput_prj_id.setAttribute('value',selectPrjId);
	form.appendChild(paramsInput_prj_id);
	
	let renameYN1 = document.createElement('input');
	renameYN1.setAttribute('type','hidden');
	renameYN1.setAttribute('name','renameYN');
	renameYN1.setAttribute('value',renameYN);
	form.appendChild(renameYN1);
	
	let fileRenameSet1 = document.createElement('input');
	fileRenameSet1.setAttribute('type','hidden');
	fileRenameSet1.setAttribute('name','fileRenameSet');
	fileRenameSet1.setAttribute('value',fileRenameSet);
	form.appendChild(fileRenameSet1);
	
	let downloadFiles1 = document.createElement('input');
	downloadFiles1.setAttribute('type','hidden');
	downloadFiles1.setAttribute('name','downloadFiles');
	downloadFiles1.setAttribute('value',downloadFiles);
	form.appendChild(downloadFiles1);
	
}

$(document).on("submit", "form.multiFileDownloadForm", function (e) {
	var selectedArray = multiDownloadTable.getData();
	var downloadFiles = '';
	var accHisDownList = [];
	if(selectedArray.length===0){
		alert('도서를 재선택해주세요.');
		$(".pop_multiDownload").dialog("close");
		return;
	}
	for(let i=0;i<selectedArray.length;i++){
		downloadFiles += selectedArray[i].bean.doc_id +'%%'+ selectedArray[i].bean.rev_id + '@@';
		accHisDownList.push(JSON.stringify(selectedArray[i].bean));
	}
	
	$.ajax({
		url: 'insertAccHisList.do',
		type: 'POST',
		traditional : true,
		async: false,
		data:{
			jsonRowDatas: accHisDownList,
			prj_id: selectPrjId,
			method: 'DOWN'
		},
		success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	//console.log("downloadFiles = "+downloadFiles);
	downloadFiles = downloadFiles.slice(0,-2);
	let renameYN='N';
	if($('#downloadFileRename').is(':checked')) renameYN='Y';
	let fileRenameSet = $('#fileRenameSet').val();
	if(selectedArray.length===1){
		location.href=current_server_domain+'/downloadFileRename.do?prj_id='+encodeURIComponent(selectPrjId)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
	}else{
		//location.href=current_server_domain+'/fileDownload.do?prj_id='+encodeURIComponent(selectPrjId)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
		multiFileDownload(selectPrjId,renameYN,fileRenameSet,downloadFiles);
	}
	$(".pop_multiDownload").dialog("close");
	
	$.fileDownload($(this).prop('action'), {
        successCallback: function (url) {
        	 
            //alert("성공");
        },
        preparingMessageHtml: "We are preparing download file, please wait...",
        failMessageHtml: "There was a problem generating your file, please try again.",
        httpMethod: "POST",
        data: $(this).serialize()
    });
    e.preventDefault();
});
function fileDownloadClose(){
	$(".pop_multiDownload").dialog("close");
}
fnSleep = function (delay){
    var start = new Date().getTime();
    while (start + delay > new Date().getTime());
};

function getCorrEmailTypeMultiBox(){
	$('#corrEmailTypeSelected').html('SELECT');
	$('#selectedCorrEmailTypeId').val('');
	email_feature = 'CORRESPONDENCE';
	$('#selectedCorrEmailFeature').val(email_feature);
	$('#corr_email_trno_corrno').html('');
	$('#corr_email_sent_times').html('');
	$('#corr_hidden_receiver_list').val('');
	$('#corr_receiver_list').val('');
	$('#corr_hidden_referto_list').val('');
	$('#corr_referto_list').val('');
	$('#corr_hidden_bcc_list').val('');
	$('#corr_bcc_list').val('');
	$('#corrSummernoteMailOut').summernote("code", '');
	$('#corr_email_subject').val('');
	$.ajax({
	    url: 'getPrjEmailType.do',
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
	   
	    	
	    	
	    	//Daily Notification Test
	    	let dailyNotiCont = '<p><b><span style="font-size: 14px;">1. 제출 지연 도서 현황</span></b></p><p><b>※ FORECAST DATE는 프로젝트별 설정에 따라 자동 등록 됩니다. 일자 변경이 필요할 경우 DCC와 협의하여 수정 가능합니다.</b><br><span style="font-size: 18px;">﻿</span></p><p><br></p>';
	    	
	    	$('#corrSummernoteMailOut').summernote("code", dailyNotiCont);
	    	
	    	
	    	$('.selectCorrMailType').click(function(){
	    		$('#corrEmailSelectWrap').removeClass('on');
	    		$('#corrEmailTypeSelected').html($(this).children().next().html());
	    		$('#selectedCorrEmailFeature').val($(this).children().html());
	    		$('#selectedCorrEmailTypeId').val($(this).children().attr('id'));
		    	
			    let email_type_id = $('#selectedCorrEmailTypeId').val();
			    DC.email_type_id = email_type_id;
				$.ajax({
				    url: 'getPrjEmailTypeContents.do',
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
	let doc_table = '';
	let doc_id = thisTable.getSelectedData()[0].bean.doc_id;
	let rev_id = thisTable.getSelectedData()[0].bean.rev_id;
	$.ajax({
	    url: 'getCorrDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	doc_id:doc_id,
	    	rev_id:rev_id
	    },
	    async:false,
	    success: function onData (data) {
	    	$('#corr_email_doc_no').val(data[0].doc_no);
	    	$('#corr_email_docno_corrno').html('CORR NO : '+data[0].doc_no);
	    	if(data[1]>0)
	    		$('#corr_email_sent_times').html('<a class="link_class" onclick="corrSendingHistory(`'+data[0].doc_no+'`)">This email already has been sent.('+data[1]+' times)</a>');
	    	else
	    		$('#corr_email_sent_times').html('');
	    	
	    	if(data[0].rfile_nm===null || data[0].rfile_nm===undefined || data[0].rfile_nm===''){
	    		doc_table = '첨부문서가 없습니다.';
	    		DC.docid_revid_docno = '';
	    	}else{
		    	let pk = '';
		    	let docid_revid_docno = '';
	    		pk += doc_id + '&&' + rev_id + '@@';
		    	pk = pk.slice(0,-2);
		    	docid_revid_docno += doc_id + '&&' + rev_id + '&&' + data[0].doc_no + '@@';
		    	docid_revid_docno = docid_revid_docno.slice(0,-2);
		    	DC.docid_revid_docno = docid_revid_docno;
		    	
		    	downloadFiles = docid_revid_docno.replaceAll('&&','%%');
		    	let renameYN='N';
		    	if(DC.file_rename_yn==='Y') renameYN='Y';
		    	let fileRenameSet = DC.file_nm_prefix;
		    	
		    	if(DC.file_link_yn==='Y'){
			    	doc_table = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    		let downloadFile = doc_id + '_' + rev_id + '.' + data[0].file_type;
		    		doc_table += '<tr style="text-align:center;"><td>1</td><td><a href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(data[0].rfile_nm)+'&email_type_id='+encodeURIComponent(DC.email_type_id)+'&email_id=@@email_id_date@@">'+data[0].doc_no+'</a></td><td>'+data[0].title+'</td><td>'+data[0].file_type+'</td><td>'+formatBytes(data[0].file_size,2)+'</td></tr>'
		    		doc_table += '</tbody></table>';
			   }else{
			    	doc_table = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
			    	doc_table += '<tr style="text-align:center;"><td>1</td><td>'+data[0].doc_no+'</td><td>'+data[0].title+'</td><td>'+data[0].file_type+'</td><td>'+formatBytes(data[0].file_size,2)+'</td></tr>'
			    	doc_table += '</tbody></table>';
		    	}
	    	}
	    	changedata = changedata.replaceAll('%OBJECT_NAME%',data[0].doc_no)
	    				.replaceAll('%TITLE%',data[0].title)
	    				.replaceAll('%DOC_TABLE%',doc_table)
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	return changedata;
}
function corrSendingHistory(doc_no){
	$.ajax({
		type: 'POST',
		url: 'getEmailSendingHistory.do',
		data:{
			prj_id:selectPrjId,
			user_data00:doc_no
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
		$(".pop_corrMailNotice").dialog("open");
		getCorrEmailTypeMultiBox();
		$('#corr_email_docno_corrno').html('');
		$('#corr_email_sent_times').html('');
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
	let email_sender_addr = $('#corr_sender_list').val().replaceAll(',',';');
	let email_receiver_addr = $('#corr_receiver_list').val().replaceAll(',',';');
	let user_data00 = $('#corr_email_doc_no').val();
	let email_refer_to = $('#corr_referto_list').val().replaceAll(',',';');
	let email_bcc = $('#corr_bcc_list').val().replaceAll(',',';');
	if(email_sender_addr===null || email_sender_addr==='' || email_sender_addr===undefined){
		alert('Sender를 입력해야합니다.');
	}else if(email_receiver_addr===null || email_receiver_addr==='' || email_receiver_addr===undefined){
		alert('Receiver를 설정해주세요.');
	}else if(subject===null || subject==='' || subject===undefined){
		alert('Subject를 입력해주세요.');
	}else{
		$.ajax({
			type: 'POST',
			url: 'insertPrjEmailCorr.do',
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
				doc_no:doc_no
			},
			success: function(data) {
				if(data[0]>0){// && data[2]>0 && data[3]>0
					alert('E-mail sent successfully');
					$(".pop_corrSendEmail").dialog('close');
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
	    url: 'getAllUsersVOList.do',
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
		    url: 'searchUsersVO.do',
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
function removeTabDCC(){
    $(".pop_documentDeleteConfirm").remove();
    $(".pop_userSch").remove();
    $(".pop_drnHis").remove();
    $(".pop_indexReg").remove();
    $(".pop_mailNoticeDC").remove();
}

function drnUpBtn(){
	
	let selected = documentListTable.getSelectedData();
	if(selected.length == 0) return;
	
	console.log(prj_id,Number(current_folder_id));
	let authChk = checkFolderAuth(prj_id,Number(current_folder_id));
	
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
	
	
	console.log(current_doc_type);
	let err = "";
	$.ajax({
	    url: 'drnValidationCheck.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:getPrjInfo().id,
	    	doc_type:current_doc_type,
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
	let tabIdx = tabList.findIndex(i => i.id == "#dccTab");
	
	
	
	
	drnPopUP(0,selected,discipObj,tabList[tabIdx].folder_id);
	
	
	
	// drnUp(0,selected,discipObj,tabList[tabIdx].folder_id);
}


function getEngAccessHisList() {
	$(".pop_accHis").dialog("open");
	
	let doc_id = popup_new_or_update==='new'?'':eng_selected_doc_id;
	let rev_id = popup_new_or_update==='new'?'':eng_selected_rev_id;
	let doc_no = $('#popup_doc_no').val();
	let rev_code_id = $('#revNoSelected').html();
	
	exeDown_doc_id = doc_id;
	
	if(rev_code_id == null || rev_code_id == "") {
		rev_code_id = "none";
    }
	if(doc_no == null || doc_no == "") {
		doc_no = "none";
    }
	
	$("#docNoRevNo").html(doc_no+", "+rev_code_id);
    
    $.ajax({
		url: 'getAccHisList.do',
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
		url: 'getAccHisList.do',
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
function getProcAccessHisList() {
	$(".pop_accHis").dialog("open");
	
	let doc_id = proc_new_or_update==='new'?'':proc_selected_doc_id;
	let rev_id = proc_new_or_update==='new'?'':proc_selected_rev_id;
	let doc_no = $('#proc_doc_no').val();
	let rev_code_id = $('#procRevNoSelected').html();
	
	exeDown_doc_id = doc_id;
	
	if(rev_code_id == null || rev_code_id == "") {
		rev_code_id = "none";
    }
	if(doc_no == null || doc_no == "") {
		doc_no = "none";
    }
	
	$("#docNoRevNo").html(doc_no+", "+rev_code_id);
    
    $.ajax({
		url: 'getAccHisList.do',
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
function getSiteAccessHisList() {
	$(".pop_accHis").dialog("open");
	
	let doc_id = site_new_or_update==='new'?'':site_selected_doc_id;
	let rev_id = site_new_or_update==='new'?'':site_selected_rev_id;
	let doc_no = $('#site_doc_no').val();
	let rev_code_id = $('#siteRevNoSelected').html();
	
	exeDown_doc_id = doc_id;
	
	if(rev_code_id == null || rev_code_id == "") {
		rev_code_id = "none";
    }
	if(doc_no == null || doc_no == "") {
		doc_no = "none";
    }
	
	$("#docNoRevNo").html(doc_no+", "+rev_code_id);
    
    	$.ajax({
		url: 'getAccHisList.do',
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
		url: 'getAccHisList.do',
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
function getGeneralAccessHisList() {
	$(".pop_accHis").dialog("open");
	
	let doc_id = general_new_or_update==='new'?'':general_selected_doc_id;
	let rev_id = general_new_or_update==='new'?'':general_selected_rev_id;
	let doc_no = $('#general_doc_no').val();
	let rev_code_id = $('#generalRevNoSelected').html();
	
	exeDown_doc_id = doc_id;
	
	if(rev_code_id == null || rev_code_id == "") {
		rev_code_id = "none";
    }
	if(doc_no == null || doc_no == "") {
		doc_no = "none";
    }
	
	$("#docNoRevNo").html(doc_no+", "+rev_code_id);
	
	$.ajax({
		url: 'getAccHisList.do',
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
function getPrjNo(){
	let prj_no = '';
	$.ajax({
	    url: 'getPrjInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    },
	    async:false,
	    success: function onData (data) {
	    	prj_no = data[0].prj_no;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	return prj_no;
}
function searchDocByFilter(){
	let select_rev_version = $('#select_rev_version').val();
	var unread_yn=$('#unread_yn').is(':checked')==true?'Y':'N';
	let filterKeyword = $('#dc_filter').val();
	if(select_rev_version==='current'){
		$.ajax({
		    url: 'searchDocByFilter.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	folder_id: current_folder_id,
		    	select_rev_version: select_rev_version,
		    	unread_yn: unread_yn,
		    	filterKeyword: filterKeyword
		    },
		    success: function onData (data) {
		    	var doc_ctrl_li = [];
		    	if(data[0]!=undefined){
		    		$('#DCRowCount').html(data[0].length+' documents are remaining');
			    	for(i=0;i<data[0].length;i++){
			    		var Modified='';
			    		if(data[0][i].mod_date!=null){
			    			Modified = getDateFormat(data[0][i].mod_date);
			    		}
			    		var rev_code_id = data[0][i].rev_code_id;
			    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
			    		var reg_id = data[0][i].reg_id;
			    		var mod_id = data[0][i].mod_id;
			    		var ext = data[0][i].file_type;
			    		var imgIcon = '';
						if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/question-mark.png' class='i'>";
						}else if(ext == "png" || ext == "jpg" || ext == "jpeg" || ext == "gif"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/picture.png' class='i'>";
						}else if(ext == "pdf"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/pdf.ico' class='i'>";
						}else if(ext == "ppt" || ext == "pptx"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/ppt.ico' class='i'>";
						}else if(ext == "doc" || ext == "docx"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/doc.ico' class='i'>";
						}else if(ext == "xls" || ext == "xlsx" || ext == "csv"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/xls.ico' class='i'>";
						}else if(ext == "txt"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/txt.ico' class='i'>";
						}else if(ext == "hwp"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/hwp.ico' class='i'>";
						}else if(ext == "dwg"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/dwg.ico' class='i'>";
						}else if(ext == "zip"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/zip.ico' class='i'>";
						}else{
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/general.ico' class='i'>";
						}
						if(lock_yn==='Y' && mod_id===session_user_id){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/checkout.ico' class='i'>";
						}else if(lock_yn==='Y' && mod_id!=session_user_id){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/lock.ico' class='i'>";
						}
				    	
			    		doc_ctrl_li.push({
			    			imgIcon: imgIcon,
			    			status: data[0][i].status,
		                    docNo: data[0][i].doc_no,
		                    category:data[0][i].category,
		                    Revision: data[0][i].rev_no,
		                    Title: data[0][i].title,
		                    designer:data[0][i].designer,
		                    Modified: Modified,
		                    Size: formatBytes(data[0][i].file_size, 2),
		                    bean: data[0][i]
			    		});
			    	}
			    	setPrjDocumentIndexList(doc_ctrl_li);
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else{
		$.ajax({
		    url: 'searchDocByFilterAll.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	folder_id: current_folder_id,
		    	select_rev_version: select_rev_version,
		    	unread_yn: unread_yn,
		    	filterKeyword: filterKeyword
		    },
		    success: function onData (data) {
		    	var doc_ctrl_li = [];
		    	if(data[0]!=undefined){
		    		$('#DCRowCount').html(data[0].length+' documents are remaining');
			    	for(i=0;i<data[0].length;i++){
			    		var Modified='';
			    		if(data[0][i].mod_date!=null){
			    			Modified = getDateFormat(data[0][i].mod_date);
			    		}
			    		var rev_code_id = data[0][i].rev_code_id;
			    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
			    		var reg_id = data[0][i].reg_id;
			    		var mod_id = data[0][i].mod_id;
			    		var ext = data[0][i].file_type;
			    		var imgIcon = '';
						if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/question-mark.png' class='i'>";
						}else if(ext == "png" || ext == "jpg" || ext == "jpeg" || ext == "gif"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/picture.png' class='i'>";
						}else if(ext == "pdf"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/pdf.ico' class='i'>";
						}else if(ext == "ppt" || ext == "pptx"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/ppt.ico' class='i'>";
						}else if(ext == "doc" || ext == "docx"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/doc.ico' class='i'>";
						}else if(ext == "xls" || ext == "xlsx" || ext == "csv"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/xls.ico' class='i'>";
						}else if(ext == "txt"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/txt.ico' class='i'>";
						}else if(ext == "hwp"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/hwp.ico' class='i'>";
						}else if(ext == "dwg"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/dwg.ico' class='i'>";
						}else if(ext == "zip"){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/zip.ico' class='i'>";
						}else{
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/general.ico' class='i'>";
						}
						if(lock_yn==='Y' && mod_id===session_user_id){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/checkout.ico' class='i'>";
						}else if(lock_yn==='Y' && mod_id!=session_user_id){
							imgIcon = "<img src='"+context_path+"/resource/images/docicon/lock.ico' class='i'>";
						}
				    	
			    		doc_ctrl_li.push({
			    			imgIcon: imgIcon,
			    			status: data[0][i].status,
		                    docNo: data[0][i].doc_no,
		                    category:data[0][i].category,
		                    Revision: data[0][i].rev_no,
		                    Title: data[0][i].title,
		                    designer:data[0][i].designer,
		                    Modified: Modified,
		                    Size: formatBytes(data[0][i].file_size, 2),
		                    bean: data[0][i]
			    		});
			    	}
			    	setPrjDocumentIndexList(doc_ctrl_li);
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}