
var trOut = {
	newOrUpdate:'new',
	DCCSendDateUpdate:''
};
var origin_email_list_trOut = [];
var selectedAddType='';
var selectedUserEmail='';
var selectedEmail='';
var selectedUserId = '';
var selectPrjId = $('#selectPrjId').val();
var session_user_id = $('#session_user_id').val();
var session_user_eng_nm = $('#session_user_eng_nm').val();
var session_user_kor_nm = $('#session_user_kor_nm').val();
var init_email_list = [];
var TRdocumentListTable;
$(function(){
	context_path = $('#context_path').val();
	session_user_id = $('#session_user_id').val();
	session_user_eng_nm = $('#session_user_eng_nm').val();
	session_user_kor_nm = $('#session_user_kor_nm').val();


	$(".pop_TRSendingHistory").dialog({
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
	$("#summernoteMailOut").summernote({
		height: 500,
		lang: "ko_KR",
		toolbar: toolbar,
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체']
	});
	
	trOut_ready = true;
	$('#out_tr_from').datepicker('setDate', lastWeek());
	$('#out_tr_to').datepicker('setDate', new Date());
	$('#out_send_date').datepicker('setDate', new Date());
	$('#out_req_date').datepicker('setDate', setReqdate());

	$("#out_check").change(function(){
		if($('#out_tr_status').val()!=='Status: Issued'){
	        if($("#out_check").is(":checked")){
	        	if($('#out_itr_no').val()!==''){
		        	$('#out_chk_date').val(getToday());
		        	$('#out_tr_status').val('Status: Checked');
		        	$('#out_issue').attr('disabled',false);
	        	}
	        }else{
	        	$('#out_chk_date').val('');
	        	$('#out_tr_status').val('Status: Checking');
	        	$('#out_issue').attr('disabled',true);
	        }
		}
    });
	
	
	

	trOutContextMenu = [{
		label: "Open",
		action: function(e, row) {
			let selectedArray = trOut.trOutSelectedTable.getSelectedData();
			if(selectedArray.length===0){
				alert('open할 도서를 선택해주세요.');
			}else{
				let sfile_nm = '';
				let lock_yn = '';
				for(let i=0;i<selectedArray.length;i++){
					lock_yn += selectedArray[i].bean.lock_yn;
				}
				if(lock_yn.indexOf('Y') !== -1){
					alert('잠금 상태인 도서는 open할 수 없습니다');
				}else{
					for(let i=0;i<selectedArray.length;i++){
						location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(selectedArray[i].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[i].bean.rfile_nm.replaceAll("&","@%@"));
						fnSleep(1000);
					}
				}
			}
		}
	},
	{
		label: "Download",
		action: function(e, row) {
			let selectedArray = trOut.trOutSelectedTable.getSelectedData();
			if(selectedArray.length===0){
				alert('다운로드 받을 도서를 선택해주세요.');
			}else{
				let multiDownTbl = [];
				let lock_yn = '';
				for(let i=0;i<selectedArray.length;i++){
					lock_yn += selectedArray[i].bean.lock_yn;
				}
				if(lock_yn.indexOf('Y') !== -1){
					alert('해당 도서는 잠금 상태입니다. 잠금 상태가 아닌 도서를 선택하세요.');
				}else{
					for(let i=0;i<selectedArray.length;i++){
						multiDownTbl.push({
							FileName:selectedArray[i].bean.sfile_nm,
							RevNo:selectedArray[i].bean.rev_no,
							Title:selectedArray[i].bean.title,
							Size:formatBytes(selectedArray[i].bean.file_size,2),
							bean:selectedArray[i].bean
						});
						TrOutMultiDownTable = new Tabulator("#multiDownTbl", {
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
						trOut.TrOutMultiDownTable = TrOutMultiDownTable;
					}
					$(".pop_multiDownload").dialog("open");
					$('#fileMultiDownload').attr('onclick','TRfileMultiDownload()');
					return false;
				}
			}
		}
	},
	{
		label: "Property",
		action: function(e, row) {
			let tr_id = $('#out_selectTRID').val();
			if(tr_id===''){
				tr_id='TRADD';
				let attach = row.getData().attach;
				let doc_no = row.getData().docNo;
				let rev_no = row.getData().RevNo;
				let title = row.getData().title;
				let folder_id = row.getData().folder_id;
				contextmenuProperty(row,tr_id,folder_id,attach,doc_no,rev_no,title); 
			}else{
				let folder_id = row.getData().folder_id;
				contextmenuProperty(row,tr_id,folder_id);
			}
		}
	},
	{
		label: "Select All",
		action: function(e, row) {
			trOut.trOutSelectedTable.selectRow("visible");
			trOutContextMenu[4].disabled = false;
		}
	},
	{
		disabled: true,
		label: "Cancel Select",
		action: function(e, row) {
			trOut.trOutSelectedTable.deselectRow();
    		trOutContextMenu[4].disabled = true;
		}
	}];

	
	
	
	
	$('input[type=radio][name=out_tr_period]').change(function(){
	    if($(this).val()==='oneweek'){
	    	$('#out_tr_from').datepicker('setDate', lastWeek());
	    	$('#out_tr_to').datepicker('setDate', new Date());
	    }else if($(this).val()==='onemonth'){
	    	$('#out_tr_from').datepicker('setDate', lastMonth());
	    	$('#out_tr_to').datepicker('setDate', new Date());
	    }else{
	    	$('#out_tr_from').val('');
	    	$('#out_tr_to').val('');
	    }
	   
	});

    getTrDisciplineList();
    getDocSelTree();
	
    $("#pop_docSel").dialog({
        autoOpen: false,
        width: 1300
    }).bind('dialogclose', function(event, ui) { $('#docSelTree_doc_type').val(''); }).dialogExtend({
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
    $(".pop_issuecancel").dialog({
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
    $(".pop_issuecancelDCC").dialog({
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
	$(".pop_TrDelete").dialog({
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
	$(".pop_mailNotice").dialog({
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
	$(".pop_userSearchM").dialog({
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
	$(".pop_SendEmail").dialog({
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
    $(document).on("click", "#out_add_doc", function() {
        $("#pop_docSel").dialog("open");
        if($('#docSelTree .jstree-clicked').attr('id')!==undefined){
	        let pop_folder_id = $('#docSelTree .jstree-clicked').attr('id').replaceAll('_anchor','');
	        getTRPrjDocumentIndexList('documentselectpopup',pop_folder_id);
        }
        return false;
    });
    $(document).on("click", "#out_issue_cancel", function() {
    	$(".pop_issuecancel").dialog('open');
        return false;
    });
    $(document).on("click", "#out_delete", function() {
    	$(".pop_TrDelete").dialog('open');
        return false;
    });
	$(document).on("click", "#out_email", function() {
		$(".pop_mailNotice").dialog("open");
		getEmailList();
		let tr_id = $('#out_selectTRID').val();
		$.ajax({
		    url: 'getTrInfo.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId,
		    	tr_id:tr_id,
		    	processType:outProcessType
		    },
		    success: function onData (data) {
		    	$('#email_tr_id').val(data[0].tr_id);
		    	$('#email_trno_corrno').html('TR NO : '+data[0].tr_no);
		    	if(data[2]>0)
		    		$('#email_sent_times').html('<a class="link_class" onclick="trOutSendingHistory(`'+data[0].tr_no+'`)">This email already has been sent.('+data[2]+' times)</a>');
		    	else
		    		$('#email_sent_times').html('');
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		getOutEmailTypeMultiBox();
		return false;
	});
});
function setWhatTROut(){
	if(outProcessType==='TR'){
		$('#TRTitleHeader').html('TR OUTGOING');
		 $.ajax({
				type: 'POST',
				url: 'CoverTemplateExists.do',         
		        data:{
		        	prj_id: getPrjInfo().id,
		        	prj_nm: getPrjInfo().nm
		        },
		        async:false,
				success: function(data) {
					if(data[0]!=='파일 존재'){
						$('#tr_cover_FileInfo').html('<span style="color:red;">현재 업로드된 COVER TEMPLATE이 없습니다.</span>');
					}
					//let rfile_nm = getPrjInfo().nm+'_p_tr.xls';
					//$('#tr_cover_FileInfo').html('<a style="text-decoration:underline;color:blue;" href="'+current_server_domain+'/downloadTRCoverFile.do?prj_id='+encodeURIComponent(getPrjInfo().id)+'&rfile_nm='+encodeURIComponent(rfile_nm)+'">TR COVER TEMPLATE: '+rfile_nm+'</a>');
				
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	}else if(outProcessType==='VTR'){
		$('#TRTitleHeader').html('VTR OUTGOING');
		 $.ajax({
				type: 'POST',
				url: 'CoverTemplateExists.do',         
		        data:{
		        	prj_id: getPrjInfo().id,
		        	prj_nm: getPrjInfo().nm
		        },
		        async:false,
				success: function(data) {
					if(data[1]!=='파일 존재'){
						$('#tr_cover_FileInfo').html('<span style="color:red;">현재 업로드된 COVER TEMPLATE이 없습니다.</span>');
					}
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	}else if(outProcessType==='STR'){
		$('#TRTitleHeader').html('STR OUTGOING');
		 $.ajax({
				type: 'POST',
				url: 'CoverTemplateExists.do',         
		        data:{
		        	prj_id: getPrjInfo().id,
		        	prj_nm: getPrjInfo().nm
		        },
		        async:false,
				success: function(data) {
					if(data[2]!=='파일 존재'){
						$('#tr_cover_FileInfo').html('<span style="color:red;">현재 업로드된 COVER TEMPLATE이 없습니다.</span>');
					}
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	}
}

function getEmailList(){
	$.ajax({
	    url: 'getAllUsersVOList.do',
	    type: 'POST',
	    async:false,
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		origin_email_list_trOut.push(data[0][i].email_addr+'!!'+data[0][i].user_id);
	    		html += '<option value="'+data[0][i].email_addr+'!!'+data[0][i].user_id+'" label="'+data[0][i].user_kor_nm+'">'+data[0][i].user_kor_nm+'('+data[0][i].email_addr+')'+'</option>';
	    	}
	    	$('#hidden_receiver_list').html(html);
	    	$('#hidden_referto_list').html(html);
	    	$('#hidden_bcc_list').html(html);

	    	$("#hidden_receiver_list").selectize({
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
	    	$("#hidden_referto_list").selectize({
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
	    	$("#hidden_bcc_list").selectize({
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




function userEmailSearch(a){//a=this
	$(".pop_userSearchM").dialog("open");
	getUserEmailList();
	let add_id = $(a).attr('id');
	if(add_id==='receiver_add'){
		selectedAddType = '#receiver';
	}else if(add_id==='referto_add'){
		selectedAddType = '#referto';
	}else if(add_id==='bcc_add'){
		selectedAddType = '#bcc';
	}
	getOriginEmailList(selectedAddType);
}
function getOriginEmailList(selectedAddType){
	let emailList = '';
	if(selectedAddType === '#receiver'){
		emailList = $('#hidden_receiver_list').val();
	}else if(selectedAddType==='#referto'){
		emailList = $('#hidden_referto_list').val();
	}else if(selectedAddType==='#bcc'){
		emailList = $('#hidden_bcc_list').val();
	}
	if(emailList!==''){
		let emailArray = emailList.split(',');
		let html = '';
		for(let i=0;i<emailArray.length;i++){
			let usernameplusemailid = emailArray[i].split('(')[0] +'_' + emailArray[i].split('(')[1].split('@')[0];
			html += '<li class="'+usernameplusemailid+'">'+emailArray[i]+' <a class="btn_del" onclick="receiverDelete(`'+emailArray[i]+'`,`'+emailArray[i].split('(')[1].slice(0,-1)+'`,`'+usernameplusemailid+'`)">삭제</a></li>';
			selectedUserEmail+=emailArray[i]+',';
			selectedEmail+=emailArray[i].split('(')[1].slice(0,-1) + ',';
		}
		selectedUserEmail = selectedUserEmail.slice(0,-1);
		selectedEmail = selectedEmail.slice(0,-1);
		$('#settingUserEmailList').html(html);
	}else{
		$('#settingUserEmailList').html('');
	}
}
function getUserEmailList(){
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
	    	setUserEmailList(userList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function usersEmailSearch(){
	var searchUser = $('input[name="searchUserEmail"]:checked').val();
	var searchKeyword = $('#usersEmailSearchKeyword').val();
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
		    	setUserEmailList(userSearchEmailList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function setUserEmailList(userSearchEmailList){
    var table = new Tabulator("#userSearchEmailList", {
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
    	if(selectedUserEmail!=''){
    		selectedUserEmail += ',';
    		selectedEmail += ',';
    		selectedUserId += ',';
    	}
    	selectedUserEmail += row.getData().bean.user_kor_nm + '(' + row.getData().bean.email_addr + ')';
    	selectedEmail += row.getData().bean.email_addr;
    	selectedUserId += row.getData().bean.user_id;

    	if(selectedUserEmail!=''){
			var arraySelectedUserEmail = selectedUserEmail.split(',');
			selectedUserEmail = [...new Set(arraySelectedUserEmail)];
			var arraySelectedEmail = selectedEmail.split(',');
			selectedEmail = [...new Set(arraySelectedEmail)];
			var arraySelectedUserId = selectedUserId.split(',');
			selectedUserId = [...new Set(arraySelectedUserId)];
			let html = '';
			for(let i=0;i<selectedUserEmail.length;i++){
				let usernameplusemailid = selectedUserEmail[i].split('(')[0] + '_' + selectedUserEmail[i].split('(')[1].split('@')[0];
				html += '<li class="'+usernameplusemailid+'">'+selectedUserEmail[i]+' <a class="btn_del" onclick="receiverDelete(`'+selectedUserEmail[i]+'`,`'+selectedEmail[i]+'`,`'+usernameplusemailid+'`)">삭제</a></li>';
			}
			$('#settingUserEmailList').html(html);
			selectedUserEmail = selectedUserEmail.join(',');
			selectedEmail = selectedEmail.join(',');
			selectedUserId = selectedUserId.join(',');
			
			if(selectedAddType === '#receiver'){
				$('#hidden_receiver_list').val(selectedUserEmail);
				$('#receiver_list').val(selectedEmail);
			}else if(selectedAddType==='#referto'){
				$('#hidden_referto_list').val(selectedUserEmail);
				$('#referto_list').val(selectedEmail);
			}else if(selectedAddType==='#bcc'){
				$('#hidden_bcc_list').val(selectedUserEmail);
				$('#bcc_list').val(selectedEmail);
			}
    	}
    });
}
function receiverDelete(selectedUserEmail_i,selectedEmail_i,usernameplusemailid){
	var arraySelectedUserEmail = selectedUserEmail.split(',');
	var arraySelectedEmail = selectedEmail.split(',');
	//var arraySelectedUserId = selectedUserId.split(',');
	let filteredselectedUserEmail = arraySelectedUserEmail.filter((element) => element !== selectedUserEmail_i);
	let filteredselectedEmail = arraySelectedEmail.filter((element) => element !== selectedEmail_i);
	//let filteredselectedUserId = arraySelectedUserId.filter((element) => element !== selectedUserId_i);
	selectedUserEmail = filteredselectedUserEmail;
	selectedEmail = filteredselectedEmail;
	//selectedUserId = filteredselectedUserId;
	selectedUserEmail = selectedUserEmail.join(',');
	selectedEmail = selectedEmail.join(',');
	//selectedUserId = selectedUserId.join(',');
	if(selectedAddType === '#receiver'){
		$('#hidden_receiver_list').val(selectedUserEmail);
		$('#receiver_list').val(selectedEmail);
	}else if(selectedAddType==='#referto'){
		$('#hidden_referto_list').val(selectedUserEmail);
		$('#referto_list').val(selectedEmail);
	}else if(selectedAddType==='#bcc'){
		$('#hidden_bcc_list').val(selectedUserEmail);
		$('#bcc_list').val(selectedEmail);
	}
	$('.gray_small_box .'+usernameplusemailid).remove();
}
function userSearchClose(){
	$('#settingUserEmailList').html('');
	selectedUserEmail='';
	selectedEmail='';
	$(".pop_userSearchM").dialog("close");
}
function getOutEmailTypeMultiBox(){
	$('#outEmailTypeSelected').html('SELECT');
	$('#selectedOutEmailTypeId').val('');
	if(outProcessType==='TR'){
		email_feature = 'TR';
	}else if(outProcessType==='VTR'){
		email_feature = 'VTR';
	}else if(outProcessType==='STR'){
		email_feature = 'STR';
	}
	$('#selectedOutEmailFeature').val(email_feature);
	let receiver_email = $('#hidden_receiver_list').eq(0).data('selectize');
	let referto_email = $('#hidden_referto_list').eq(0).data('selectize');
	let bcc_email = $('#hidden_bcc_list').eq(0).data('selectize');
	receiver_email.setValue(null,false);
	referto_email.setValue(null,false);
	bcc_email.setValue(null,false);
	$('#summernoteMailOut').summernote("code", '');
	$('#out_email_subject').val('');
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
            	html += '<tr class="selectOutMailType"><td id="'+data[0][i].email_type_id+'">'+data[0][i].email_feature+'</td><td>'+data[0][i].email_type+'</td><td>'+data[0][i].email_desc+'</td></tr>'
            }
	    	$('#outEmailTypeMultiBox').html(html);
	   
	    	$('.selectOutMailType').click(function(){
	    		$('#outEmailSelectWrap').removeClass('on');
	    		$('#outEmailTypeSelected').html($(this).children().next().html());
	    		$('#selectedOutEmailFeature').val($(this).children().html());
	    		$('#selectedOutEmailTypeId').val($(this).children().attr('id'));
		    	
			    let email_type_id = $('#selectedOutEmailTypeId').val();
			    trOut.email_type_id = email_type_id;
				$.ajax({
				    url: 'getPrjEmailTypeContents.do',
				    type: 'POST',
				    data:{
				    	prj_id: selectPrjId,
				    	email_type_id:email_type_id
				    },
				    success: function onData (data) {
				    	trOut.file_link_yn = data[0].file_link_yn;
				    	trOut.file_rename_yn = data[0].file_rename_yn;
				    	trOut.file_nm_prefix = data[0].file_nm_prefix;
				    	let contents = prefixReplace(data[0].contents);
				    	let subject = prefixReplace(data[0].subject_prefix);
				    	$('#summernoteMailOut').summernote("code", contents);
				    	$('#out_email_subject').val(subject);
				    	let receiver_email = $('#hidden_receiver_list').eq(0).data('selectize');
				    	let referto_email = $('#hidden_referto_list').eq(0).data('selectize');
				    	let bcc_email = $('#hidden_bcc_list').eq(0).data('selectize');
				    	$('#sender_list').val(data[0].email_sender_addr.split('!!')[0]);
			    		let receiver_email_list = data[0].email_receiver_addr.split(';');
			    		for(let l=0;l<receiver_email_list.length;l++){
				    		if(!origin_email_list_trOut.includes(receiver_email_list[l])){
				    			receiver_email.addOption({value:receiver_email_list[l], text:receiver_email_list[l]});
				    		}
			    		}
			    		let referto_email_list = data[0].email_referto_addr.split(';');
			    		for(let l=0;l<referto_email_list.length;l++){
				    		if(!origin_email_list_trOut.includes(referto_email_list[l])){
				    			referto_email.addOption({value:referto_email_list[l], text:referto_email_list[l]});
				    		}
			    		}
			    		let bcc_email_list = data[0].email_bcc_addr.split(';');
			    		for(let l=0;l<bcc_email_list.length;l++){
				    		if(!origin_email_list_trOut.includes(bcc_email_list[l])){
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
function prefixReplace(changedata){
	let doc_list_zip = '';
	let doc_list_table = '';
	let doc_list_table_with_discipline = '';
	let doc_list_table_with_class = '';
	let doc_list_table_with_return_status = '';
	doc_list_table += '<p></p><br>';
	doc_list_table_with_discipline += '<p></p><br>';
	doc_list_table_with_class += '<p></p><br>';
	doc_list_table_with_return_status += '<p></p><br>';
	let tr_id = $('#out_selectTRID').val();
	$.ajax({
	    url: 'getTrInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:tr_id,
	    	processType:outProcessType
	    },
	    async:false,
	    success: function onData (data) {
	    	trOut.set_code_id = data[0].tr_issuepur_code_id;
	    	$('#email_tr_id').val(data[0].tr_id);
	    	$('#email_trno_corrno').html('TR NO : '+data[0].tr_no);
	    	if(data[2]>0)
	    		$('#email_sent_times').html('<a class="link_class" onclick="trOutSendingHistory(`'+data[0].tr_no+'`)">This email already has been sent.('+data[2]+' times)</a>');
	    	else
	    		$('#email_sent_times').html('');
	    	
	    	let due_date = 'NO';
	    	if(data[0].req_date!=='' && data[0].req_date!==null && data[0].req_date!==undefined){
	    		due_date = data[0].req_date;
	    	}
	    	
	    	let pk = '';
	    	let docid_revid_docno_revno = '';
	    	for(let i=0;i<data[1].length;i++){
	    		pk += data[1][i].doc_id + '&&' + data[1][i].rev_id + '@@';
	    		docid_revid_docno_revno += data[1][i].doc_id + '&&' + data[1][i].rev_id + '&&' + data[1][i].doc_no + '&&' + data[1][i].rev_no + '@@';
	    	}
	    	pk = pk.slice(0,-2);
	    	docid_revid_docno_revno = docid_revid_docno_revno.slice(0,-2);
	    	trOut.docid_revid_docno_revno = docid_revid_docno_revno;
	    	//trOut.trOutSelectedTable.setData([]);
	    	let docListData = getTRDocumentInfo(pk,'OUT','email');

	    	//downloadFiles = pk.replaceAll('&&','%%');
	    	downloadFiles = data[0].tr_no+'.'+data[3]===null?'':data[3].file_type + '@@';	//COVER FILE
	    	downloadFiles += docid_revid_docno_revno.replaceAll('&&','%%');
	    	let renameYN='N';
	    	if(trOut.file_rename_yn==='Y') renameYN='Y';
	    	let fileRenameSet = trOut.file_nm_prefix;
	    	doc_list_zip = '<a href="'+server_domain+'/fileDownloadEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles)+'">Doc_list.zip</a>';

	    	if(trOut.file_link_yn==='Y'){
		    	doc_list_table = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table_with_discipline = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Discipline</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table_with_class = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Category</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table_with_return_status = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Reutrn Status</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
		    	doc_list_table_with_discipline += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
		    	doc_list_table_with_class += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
		    	doc_list_table_with_return_status += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
	    		for(let i=0;i<docListData.length;i++){
		    		let downloadFile = docListData[i].doc_id + '_' + docListData[i].rev_id + '.' + docListData[i].file_type;
		    		doc_list_table += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(docListData[i].rfile_nm)+'&email_type_id='+encodeURIComponent(trOut.email_type_id)+'&email_id=@@email_id_date@@">'+docListData[i].doc_no+'</a></td><td>'+docListData[i].rev_no+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
		    		doc_list_table_with_discipline += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(docListData[i].rfile_nm)+'&email_type_id='+encodeURIComponent(trOut.email_type_id)+'&email_id=@@email_id_date@@">'+docListData[i].doc_no+'</a></td><td>'+docListData[i].rev_no+'</td><td>'+docListData[i].discipline+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
		    		doc_list_table_with_class += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(docListData[i].rfile_nm)+'&email_type_id='+encodeURIComponent(trOut.email_type_id)+'&email_id=@@email_id_date@@">'+docListData[i].doc_no+'</a></td><td>'+docListData[i].rev_no+'</td><td>'+docListData[i].category_code+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
		    		doc_list_table_with_return_status += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadFileEmail.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(downloadFile)+'&rfile_nm='+encodeURIComponent(docListData[i].rfile_nm)+'&email_type_id='+encodeURIComponent(trOut.email_type_id)+'&email_id=@@email_id_date@@">'+docListData[i].doc_no+'</a></td><td>'+docListData[i].rev_no+'</td><td>'+''+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
				}
		    	doc_list_table += '</tbody></table>';
		    	doc_list_table_with_discipline += '</tbody></table>';
		    	doc_list_table_with_class += '</tbody></table>';
		    	doc_list_table_with_return_status += '</tbody></table>';
	    	}else{
		    	doc_list_table = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table_with_discipline = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Discipline</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table_with_class = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Category</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table_with_return_status = '<table width="700" border="1" class="table table-bordered"><thead style="background-color:#eeeeee;"><th>No</th><th>Document No.</th><th>Rev.</th><th>Reutrn Status</th><th>Document Title</th><th>Format</th><th>Size</th></thead><tbody>';
		    	doc_list_table += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
		    	doc_list_table_with_discipline += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
		    	doc_list_table_with_class += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
		    	doc_list_table_with_return_status += '<tr style="text-align:center;"><td>*</td><td><a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">Transmittal Cover</a></td><td>*</td><td>*</td><td>'+data[3].title+'</td><td>'+data[3].file_type+'</td><td>'+formatBytes(data[3].file_size,2)+'</td></tr>'
	    		for(let i=0;i<docListData.length;i++){
		    		doc_list_table += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td>'+docListData[i].doc_no+'</td><td>'+docListData[i].rev_no+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
		    		doc_list_table_with_discipline += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td>'+docListData[i].doc_no+'</td><td>'+docListData[i].rev_no+'</td><td>'+docListData[i].discipline+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
		    		doc_list_table_with_class += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td>'+docListData[i].doc_no+'</td><td>'+docListData[i].rev_no+'</td><td>'+docListData[i].category_code+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
		    		doc_list_table_with_return_status += '<tr style="text-align:center;"><td>'+(i+1)+'</td><td>'+docListData[i].doc_no+'</td><td>'+docListData[i].rev_no+'</td><td>'+data[1][i].return_status+'</td><td>'+docListData[i].title+'</td><td>'+docListData[i].file_type+'</td><td>'+formatBytes(docListData[i].file_size,2)+'</td></tr>'
				}
		    	doc_list_table += '</tbody></table>';
		    	doc_list_table_with_discipline += '</tbody></table>';
		    	doc_list_table_with_class += '</tbody></table>';
		    	doc_list_table_with_return_status += '</tbody></table>';
		    	doc_list_table += '<p></p><br>';
		    	doc_list_table_with_discipline += '<p></p><br>';
		    	doc_list_table_with_class += '<p></p><br>';
		    	doc_list_table_with_return_status += '<p></p><br>';
	    	}
	    	changedata = changedata.replaceAll('%TR_NO%',data[0].tr_no)
	    				.replaceAll('%TR_SUBJECT%',data[0].tr_subject)
	    				.replaceAll('%TR_ISSUE_PURPOSE%',data[0].issuepurpose)
	    				.replaceAll('%TR_ISSUE_DISC_NAME%',data[0].discipline)
	    				.replaceAll('%TR_DUE_DATE%',getDateFormat_YYYYMMDD(due_date))
	    				.replaceAll('%DOC_LIST_ZIP%',doc_list_zip)
	    				.replaceAll('%DOC_LIST_TABLE%',doc_list_table)
	    				.replaceAll('%DOC_LIST_TABLE_WITH_DISCIPLINE%',doc_list_table_with_discipline)
	    				.replaceAll('%DOC_LIST_TABLE_WITH_CLASS%',doc_list_table_with_class)
	    				.replaceAll('%DOC_LIST_TABLE_WITH_RETURN_STATUS%',doc_list_table_with_return_status);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
				
	return changedata;
}
function trOutSendingHistory(tr_no){
	$.ajax({
		type: 'POST',
		url: 'getEmailSendingHistory.do',
		data:{
			prj_id:selectPrjId,
			user_data00:tr_no
		},
		success: function(data) {
			console.log(data);
			var trOutSendingHistoryTbl = [];
			for(let i=0;i<data[0].length;i++){
				trOutSendingHistoryTbl.push({
					type: data[0][i].email_type,
					sender: data[0][i].reg_id,
					date: getDateFormat(data[0][i].reg_date)
				});
			}
			setTrOutSendingHistoryData(trOutSendingHistoryTbl);
		},
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	$('.pop_TRSendingHistory').dialog('open');
}
function setTrOutSendingHistoryData(trOutSendingHistoryTbl){
    var table = new Tabulator("#trOutSendingHistoryTbl", {
        data: trOutSendingHistoryTbl,
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
function sendEmailClose(){
	$('.pop_mailNotice').dialog('close');
}
function sendEmailConfrim(){
	$(".pop_SendEmail").dialog('open');
}
function sendEmailCancel(){
	$(".pop_SendEmail").dialog('close');
}
function sendEmail(){
	let tr_no = $('#out_tr_no').val();
	let tr_id = $('#email_tr_id').val();
	let email_type_id = $('#selectedOutEmailTypeId').val();
	let subject = $('#out_email_subject').val();
	let contents = $('#summernoteMailOut').summernote("code");
	let user_data00 = $('#out_tr_no').val();
	let email_sender_addr = $('#sender_list').val();
	let receiver_email_array_before = $('#hidden_receiver_list').val();
	let receiver_email_array_after = [];
	for(let i=0;i<receiver_email_array_before.length;i++){
		receiver_email_array_after[i] = receiver_email_array_before[i].split('!!')[0];
	}
	let referto_email_array_before = $('#hidden_referto_list').val();
	let referto_email_array_after = [];
	for(let i=0;i<referto_email_array_before.length;i++){
		referto_email_array_after[i] = referto_email_array_before[i].split('!!')[0];
	}
	let bcc_email_array_before = $('#hidden_bcc_list').val();
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
			url: 'insertPrjEmail.do',
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
				docid_revid_docno_revno:trOut.docid_revid_docno_revno,
				tr_id:tr_id,
				tr_no:tr_no,
				processType:outProcessType,
				set_code_id:trOut.set_code_id
			},
			success: function(data) {
				if(data[0]>0){// && data[3]>0
					alert('E-mail sent successfully');
					$.ajax({
					    url: 'getTrInfo.do',
					    type: 'POST',
					    data:{
					    	prj_id:selectPrjId,
					    	tr_id:tr_id,
					    	processType:outProcessType
					    },
					    success: function onData (data) {
					    	$('#email_tr_id').val(data[0].tr_id);
					    	$('#email_trno_corrno').html('TR NO : '+data[0].tr_no);
					    	if(data[2]>0){
					    		$('#email_sent_times').html('<a class="link_class" onclick="trOutSendingHistory(`'+data[0].tr_no+'`)">This email already has been sent.('+data[2]+' times)</a>');
					    		$('#tr_email_send_info').html('<a class="link_class" onclick="trOutSendingHistory(`'+data[0].tr_no+'`)">This email already has been sent.('+data[2]+' times)</a>');
						    }else{
					    		$('#email_sent_times').html('');
					    		$('#tr_email_send_info').html('');
						    }
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
					$(".pop_SendEmail").dialog('close');
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
function getToday(){
	let today = new Date();
	let year = today.getFullYear();
	let month = ('0' + (today.getMonth() + 1)).slice(-2);
	let day = ('0' + today.getDate()).slice(-2);
	let dateString = year + '-' + month  + '-' + day;
	return dateString;
}
function setReqdate() {
	  var d = new Date();
	  var dayOfMonth = d.getDate();
	  d.setDate(dayOfMonth + 15);
	  return d;
}
function lastWeek() {
	  var d = new Date();
	  var dayOfMonth = d.getDate();
	  d.setDate(dayOfMonth - 7);
	  return d;
}
function lastMonth() {
	  var d = new Date();
	  var monthOfYear = d.getMonth();
	  d.setMonth(monthOfYear - 1);
	  return d;
}
function dccFolder(folderList){
	var folder = [];
	for(let i=0;i<folderList.length;i++)
		if(folderList[i].type === "DCC")
			folder.push(folderList[i]);	
	
	return folder;
	
}
function getDocSelTree(){
	let folderList = getFolderList();
	folderList = dccFolder(folderList);
	let tree = $('#docSelTree').jstree({
	     "core" : {
	    	 'data' : folderList,
	         "check_callback" : true
	       }
	    })
	    .on('dblclick.jstree', function(event) { // 더블클릭 이벤트 바인딩
	    	docSelTreeMenuActive(event)} )
	    .on('click.jstree', function(event) { // 클릭 이벤트 바인딩
	    	docSelTreeMenuActive(event)} );
	
	$('#docSelTree').jstree(true).refresh();
}
function docSelTreeMenuActive(event){
	var node = $(event.target).closest("li");

	var id = node[0].id; //id 가져오기
	var text = node[0].querySelector("a").childNodes[1].nodeValue;
	
	// 폴더메뉴의 타입
	var type;
	for (let key in folderList) {
		  const value = folderList[key]
		  if(value.id === parseInt(id)){
			  type = value.type;
			  if(type==='DCC'){
				  $('#docSelTree_discipline_code_id').val(value.discipline_code_id);
				  trOut.current_folder_discipline_code_id=value.discipline_code_id;
				  $('#docSelTree_discipline_display').val(value.discipline_display);
				  $('#docSelTree_doc_type').val(value.doc_type);
				  //current_process_type = value.process_type;
				  //current_corr_process_type = value.corr_process_type;
				  }
			  }
		  }

	if(type === "DCC") {
		popup_folder_id = id;
		popup_folder_path = $('#docSelTree').jstree().get_path(node[0], '/');
		$('#docsel_current_folder_path').val(popup_folder_path);
		getTRPrjDocumentIndexList('documentselectpopup',popup_folder_id);
		$('#TRAddDoc_Retrieve').attr('onclick','getTRPrjDocumentIndexList("documentselectpopup",'+popup_folder_id+')');
		$('#docsel_select_rev_version').attr('onchange','getTRPrjDocumentIndexList("documentselectpopup",'+popup_folder_id+')');
		$('#docsel_unread_yn').attr('onchange','getTRPrjDocumentIndexList("documentselectpopup",'+popup_folder_id+')');
		$('#docsel_auto_refresh').attr('onchange','getTRPrjDocumentIndexList("documentselectpopup",'+popup_folder_id+')');
	}
		
}
function getTrIssuePurposeList(){
	let selectPrjId = $('#selectPrjId').val();
	let set_code_type = '';
	if(outProcessType === 'TR'){
		set_code_type = 'TR ISSUE PURPOSE';
	}else if(outProcessType === 'VTR'){
		set_code_type = 'VTR ISSUE PURPOSE';
	}else if(outProcessType === 'STR'){
		set_code_type = 'STR ISSUE PURPOSE';
	}
	 $.ajax({
			type: 'POST',
			url: 'getTrIssuePurposeList.do',
			data:{
				prj_id:selectPrjId,
				set_code_type:set_code_type
			},
			success: function(data) {
				let issuepur = '';
				for(let i=0;i<data[0].length;i++){
					issuepur += '<option value="'+data[0][i].set_code_id+'">'+data[0][i].set_code+':'+data[0][i].set_desc+'</option>';
				}
				$('#out_tr_issuepur_code_id').html(issuepur);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function getTrDisciplineList(){
	let selectPrjId = $('#selectPrjId').val();
	 $.ajax({
			type: 'POST',
			url: 'getTrDisciplineList.do',
			data:{
				prj_id:selectPrjId
			},
			success: function(data) {
				let discipline = '';
				for(let i=0;i<data[0].length;i++){
					discipline += '<option value="'+data[0][i].discip_code_id+'">'+data[0][i].discip_display+'</option>';
				}
				$('#out_discipline_code_id').html(discipline);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function getTodayDate(){
	var date = new Date(); 
	var year = date.getFullYear(); 
	var month = new String(date.getMonth()+1); 
	var day = new String(date.getDate()); 

	if(month.length == 1){ 
	  month = "0" + month; 
	} 
	if(day.length == 1){ 
	  day = "0" + day; 
	}
	
	return year+'-'+month+'-'+day;
}
function out_issue(){
	setWhatTROut();
	let current_folder_doc_type = '';
	if(outProcessType === 'TR'){
		current_folder_doc_type = 'DCI';
	}else if(outProcessType === 'VTR'){
		current_folder_doc_type = 'VDCI';
	}else if(outProcessType === 'STR'){
		current_folder_doc_type = 'SDCI';
	}
    if($("#out_check").is(":checked")===false){
    	alert('Checked가 체크되어야 TR ISSUE가 가능합니다.');
    }else if($('#tr_cover_FileInfo').children().html()==='현재 업로드된 COVER TEMPLATE이 없습니다.'){
    	alert('현재 업로드된 Transmittal Cover Template 파일이 없습니다. DCC에게 문의해주세요.')
    }else{
    	let issueDocList = trOut.trOutSelectedTable.getData();
    	let alertM = '';
    	let select_discipline = $('#out_discipline_code_id').val();
    	for(let i=0;i<issueDocList.length;i++){
    		let rev_code_id = issueDocList[i].bean.rev_code_id;
    		let ext = issueDocList[i].bean.file_type;
			/*if(issueDocList[i].bean.lock_yn==='Y'){
				alertM += issueDocList[i].bean.doc_no+'는 Check Out상태이므로 Transmittal 불가합니다.\n';
			}else if(issueDocList[i].bean.doc_type!==current_folder_doc_type){
				alertM += issueDocList[i].bean.doc_no+'는 문서타입이 맞지 않습니다.\n';
			}else if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
				alertM += issueDocList[i].bean.doc_no+'는 비어있는 문서입니다.\n';
			}else if(issueDocList[i].bean.duplicationDoc !== 'N' && $('#out_itr_no').val()!==issueDocList[i].bean.duplicationDoc && issueDocList[i].bean.duplicationDoc!==''){
				alertM += issueDocList[i].bean.doc_no+' : '+ issueDocList[i].bean.duplicationDoc+' 에서 이미 첨부된 문서입니다.\n';
			}else if(issueDocList[i].bean.rev_id!==issueDocList[i].bean.rev_max){
				alertM += issueDocList[i].bean.doc_no+'는 현재 버전이 아닙니다.\n';
			}else if(select_discipline!==issueDocList[i].bean.discip_code_id){
				alertM += issueDocList[i].bean.doc_no+':Other Discipline cannot be selected!!!\n';
			}*/
			
    	}
    	if(alertM!==''){
    		alert(alertM);
    	}else{
    		let set_code = '';
    		if(outProcessType === 'TR'){
    			set_code = 'TR OUTGOING';
    		}else if(outProcessType === 'VTR'){
    			set_code = 'VTR OUTGOING';
    		}else if(outProcessType === 'STR'){
    			set_code = 'STR OUTGOING';
    		}
    		
    		let tr_no = $('#out_tr_no').val();
			let tr_id = $('#out_selectTRID').val();
	    	let chk_date = $('#out_chk_date').val().replaceAll('-','');
			$.ajax({
			    url: 'TrOutIssue.do',
			    type: 'POST',
			    data:{
			    	prj_id:selectPrjId,
			    	newOrUpdate:trOut.newOrUpdate,
			    	tr_no:tr_no,
			    	tr_id:tr_id,
			    	tr_status:'Issued',
			    	set_code_type:'CORRESPONDENCE_OUT',
			    	set_code:set_code,
			    	chk_date:chk_date,
			    	processType:outProcessType,
			    	current_folder_doc_type:current_folder_doc_type,
			    	select_discipline:select_discipline
			    },
		        beforeSend:function(){
					$('body').prepend(loading);
				},
				complete:function(){
					$('#loading').remove();
				},
			    success: function onData (data) {
			    	if(data[0]!=='이슈 가능'){
			    		alert(data[0]);
			    	}else{
				    	if(data[1]>0 && data[2]>0){
				    		alert('Issue success');
				    		$('#out_save').attr('disabled',true);
				    		$('#out_issue').attr('disabled',true);
				    		$('#out_issue_cancel').attr('disabled',false);
				    		$('#out_delete').attr('disabled',true);
				    		$('#out_email').attr('disabled',false);
				    		getTrOutSearch();
				    		getTrInfo(tr_id);
				    		
				    		//트리폴더리스트 갱신
					    	var data1 = getFolderList();
					    	folderList = data1;
							var tree = $("#tree").jstree(true);
					    	tree.settings.core.data = data1;
					    	tree.refresh();
				    	}else{
				    		alert('Issue failed');
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
function out_issueCancel(){
	let tr_id = $('#out_selectTRID').val();
	let tr_no = $('#out_tr_no').val();

	 $.ajax({
			type: 'POST',
			url: 'getIsUseDRN.do',
			data: {
		    	prj_id:selectPrjId,
		    	tr_id:tr_id,
		    	tr_no:tr_no,
		    	user_data00:tr_no,
		    	processType:outProcessType
			},
			success: function(data) {
				if(data[0]==='취소불가'){
		    		$(".pop_issuecancel").dialog('close');
		    		alert('Issue cancellation is not possible for Document sent to customers.');
		    		//alert('Issue cancellation is not possible for Document sent to customers, even if it is DCC.');
				}else if(data[0]==='ADMIN취소가능'){
		    		if(data[1]==='ADMIN 취소'){
						$.ajax({
						    url: 'updateTRIssueCancel.do',
						    type: 'POST',
						    data:{
						    	prj_id:selectPrjId,
						    	tr_id:tr_id,
						    	tr_no:tr_no,
						    	processType:outProcessType
						    },
					        beforeSend:function(){
								$('body').prepend(loading);
							},
						    success: function onData (data) {
								$('#loading').remove();
						    	if(data[0]>0 && data[1]>0 && data[2]>0){
						    		//트리폴더리스트 갱신
							    	var data1 = getFolderList();
							    	folderList = data1;
									var tree = $("#tree").jstree(true);
							    	tree.settings.core.data = data1;
							    	tree.refresh();
						    		alert('IssueCancel Completed');
						    		$(".pop_issuecancel").dialog('close');
						    		$('#out_check').prop('checked',false);
						    		$('#out_tr_no').prop('disabled',false);
						    		$('#out_tr_no').val('');
						    		$('#out_chk_date').val('');
					            	$('#out_tr_status').val('Status: Checking');
				        			$('#out_check').prop('disabled',false);
					            	$('#out_save').attr('disabled',false);
					            	$('#out_delete').attr('disabled',false);
					            	$('#out_issue').attr('disabled',false);
					            	$('#out_issue_cancel').attr('disabled',true);
						    		$('#out_email').attr('disabled',true);
						        	$('#out_tr_subject').attr('disabled',false);
						        	$('#out_discipline_code_id').css('pointer-events', 'auto');
						        	$('#out_discipline_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
						        	$('#out_discipline_code_id').css('background-color', '#fff');
						        	$('#out_tr_issuepur_code_id').css('pointer-events', 'auto');
						        	$('#out_tr_issuepur_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
						        	$('#out_tr_issuepur_code_id').css('background-color', '#fff');
						        	$('#out_send_date').attr('disabled',false);
						        	$('#out_req_date').attr('disabled',false);
						        	$('#out_tr_remark').attr('disabled',false);
						        	$('#out_add_doc').css('display','inline-block');
						        	$('#out_del_doc').css('display','inline-block');
						    		getTrOutSearch();
						    		let tr_id = trOut.trOutgoingSearchTbl.getData()[0].bean.tr_id;
						    		getTrInfo(tr_id);
						    		$('#tr_email_send_info').html('');
						    		$('#tr_cover_FileInfo').html('');
						    	}else{
						    		alert('Issue Cancel failed');
						    	}
						    },
						    error: function onError (error) {
						        console.error(error);
						    }
						});
		    		}else{
			    		$(".pop_issuecancel").dialog('close');
						$(".pop_issuecancelDCC").dialog('open');
						$('#TRIssueCancelDCC_Confirm').click(function(){
							$.ajax({
							    url: 'updateTRIssueCancel.do',
							    type: 'POST',
							    data:{
							    	prj_id:selectPrjId,
							    	tr_id:tr_id,
							    	tr_no:tr_no,
							    	processType:outProcessType
							    },
						        beforeSend:function(){
									$('body').prepend(loading);
								},
							    success: function onData (data) {
									$('#loading').remove();
									 $.ajax({
											type: 'POST',
											url: 'deleteTR.do',
											data: {
										    	prj_id:selectPrjId,
										    	tr_id:tr_id,
										    	tr_no:tr_no,
										    	processType:outProcessType
											},
											success: function(data) {
									    		//트리폴더리스트 갱신
										    	var data1 = getFolderList();
										    	folderList = data1;
												var tree = $("#tree").jstree(true);
										    	tree.settings.core.data = data1;
										    	tree.refresh();
												alert('DRN이 반려처리되고, TR이 삭제 되었습니다.');
									    		$(".pop_issuecancelDCC").dialog('close');
									    		$('#out_check').prop('checked',false);
									    		$('#out_tr_no').val('');
									    		$('#out_chk_date').val('');
								            	$('#out_tr_status').val('Status: Checking');
							        			$('#out_check').prop('disabled',false);
								            	$('#out_save').attr('disabled',false);
								            	$('#out_delete').attr('disabled',false);
								            	$('#out_issue').attr('disabled',false);
								            	$('#out_issue_cancel').attr('disabled',true);
									    		$('#out_email').attr('disabled',true);
									    		getTrOutSearch();
									    		createTR();
									    		$('#tr_email_send_info').html('');
									    		$('#tr_cover_FileInfo').html('');
											},
										    error: function onError (error) {
										        console.error(error);
										    }
									});
							    },
							    error: function onError (error) {
							        console.error(error);
							    }
							});
						});
		    		}
				}else if(data[0]==='취소가능'){
					$.ajax({
					    url: 'updateTRIssueCancel.do',
					    type: 'POST',
					    data:{
					    	prj_id:selectPrjId,
					    	tr_id:tr_id,
					    	tr_no:tr_no,
					    	processType:outProcessType
					    },
				        beforeSend:function(){
							$('body').prepend(loading);
						},
					    success: function onData (data) {
							$('#loading').remove();
					    	if(data[0]>0 && data[1]>0 && data[2]>0){
					    		//트리폴더리스트 갱신
						    	var data1 = getFolderList();
						    	folderList = data1;
								var tree = $("#tree").jstree(true);
						    	tree.settings.core.data = data1;
						    	tree.refresh();
					    		alert('IssueCancel Completed');
					    		$(".pop_issuecancel").dialog('close');
					    		$('#out_check').prop('checked',false);
					    		$('#out_tr_no').prop('disabled',false);
					    		$('#out_tr_no').val('');
					    		$('#out_chk_date').val('');
				            	$('#out_tr_status').val('Status: Checking');
			        			$('#out_check').prop('disabled',false);
				            	$('#out_save').attr('disabled',false);
				            	$('#out_delete').attr('disabled',false);
				            	$('#out_issue').attr('disabled',false);
				            	$('#out_issue_cancel').attr('disabled',true);
					    		$('#out_email').attr('disabled',true);
					        	$('#out_tr_subject').attr('disabled',false);
					        	$('#out_discipline_code_id').css('pointer-events', 'auto');
					        	$('#out_discipline_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
					        	$('#out_discipline_code_id').css('background-color', '#fff');
					        	$('#out_tr_issuepur_code_id').css('pointer-events', 'auto');
					        	$('#out_tr_issuepur_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
					        	$('#out_tr_issuepur_code_id').css('background-color', '#fff');
					        	$('#out_send_date').attr('disabled',false);
					        	$('#out_req_date').attr('disabled',false);
					        	$('#out_tr_remark').attr('disabled',false);
					        	$('#out_add_doc').css('display','inline-block');
					        	$('#out_del_doc').css('display','inline-block');
					    		getTrOutSearch();
					    		let tr_id = trOut.trOutgoingSearchTbl.getData()[0].bean.tr_id;
					    		getTrInfo(tr_id);
					    		$('#tr_email_send_info').html('');
					    		$('#tr_cover_FileInfo').html('');
					    	}else{
					    		alert('Issue Cancel failed');
					    	}
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else{
					if(data[0]==='DCC USER'){
			    		if(data[1]==='DCC 취소'){
							$.ajax({
							    url: 'updateTRIssueCancel.do',
							    type: 'POST',
							    data:{
							    	prj_id:selectPrjId,
							    	tr_id:tr_id,
							    	tr_no:tr_no,
							    	processType:outProcessType
							    },
						        beforeSend:function(){
									$('body').prepend(loading);
								},
							    success: function onData (data) {
									$('#loading').remove();
							    	if(data[0]>0 && data[1]>0 && data[2]>0){
							    		//트리폴더리스트 갱신
								    	var data1 = getFolderList();
								    	folderList = data1;
										var tree = $("#tree").jstree(true);
								    	tree.settings.core.data = data1;
								    	tree.refresh();
							    		alert('IssueCancel Completed');
							    		$(".pop_issuecancel").dialog('close');
							    		$('#out_check').prop('checked',false);
							    		$('#out_tr_no').prop('disabled',false);
							    		$('#out_tr_no').val('');
							    		$('#out_chk_date').val('');
						            	$('#out_tr_status').val('Status: Checking');
					        			$('#out_check').prop('disabled',false);
						            	$('#out_save').attr('disabled',false);
						            	$('#out_delete').attr('disabled',false);
						            	$('#out_issue').attr('disabled',false);
						            	$('#out_issue_cancel').attr('disabled',true);
							    		$('#out_email').attr('disabled',true);
							        	$('#out_tr_subject').attr('disabled',false);
							        	$('#out_discipline_code_id').css('pointer-events', 'auto');
							        	$('#out_discipline_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
							        	$('#out_discipline_code_id').css('background-color', '#fff');
							        	$('#out_tr_issuepur_code_id').css('pointer-events', 'auto');
							        	$('#out_tr_issuepur_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
							        	$('#out_tr_issuepur_code_id').css('background-color', '#fff');
							        	$('#out_send_date').attr('disabled',false);
							        	$('#out_req_date').attr('disabled',false);
							        	$('#out_tr_remark').attr('disabled',false);
							        	$('#out_add_doc').css('display','inline-block');
							        	$('#out_del_doc').css('display','inline-block');
							    		getTrOutSearch();
							    		let tr_id = trOut.trOutgoingSearchTbl.getData()[0].bean.tr_id;
							    		getTrInfo(tr_id);
							    		$('#tr_email_send_info').html('');
							    		$('#tr_cover_FileInfo').html('');
							    	}else{
							    		alert('Issue Cancel failed');
							    	}
							    },
							    error: function onError (error) {
							        console.error(error);
							    }
							});
			    		}else{
				    		$(".pop_issuecancel").dialog('close');
							$(".pop_issuecancelDCC").dialog('open');
							$('#TRIssueCancelDCC_Confirm').click(function(){
								$.ajax({
								    url: 'updateTRIssueCancel.do',
								    type: 'POST',
								    data:{
								    	prj_id:selectPrjId,
								    	tr_id:tr_id,
								    	tr_no:tr_no,
								    	processType:outProcessType
								    },
							        beforeSend:function(){
										$('body').prepend(loading);
									},
								    success: function onData (data) {
										$('#loading').remove();
										 $.ajax({
												type: 'POST',
												url: 'deleteTR.do',
												data: {
											    	prj_id:selectPrjId,
											    	tr_id:tr_id,
											    	tr_no:tr_no,
											    	processType:outProcessType
												},
												success: function(data) {
										    		//트리폴더리스트 갱신
											    	var data1 = getFolderList();
											    	folderList = data1;
													var tree = $("#tree").jstree(true);
											    	tree.settings.core.data = data1;
											    	tree.refresh();
													alert('DRN이 반려처리되고, TR이 삭제 되었습니다.');
										    		$(".pop_issuecancelDCC").dialog('close');
										    		$('#out_check').prop('checked',false);
										    		$('#out_tr_no').val('');
										    		$('#out_chk_date').val('');
									            	$('#out_tr_status').val('Status: Checking');
								        			$('#out_check').prop('disabled',false);
									            	$('#out_save').attr('disabled',false);
									            	$('#out_delete').attr('disabled',false);
									            	$('#out_issue').attr('disabled',false);
									            	$('#out_issue_cancel').attr('disabled',true);
										    		$('#out_email').attr('disabled',true);
										    		getTrOutSearch();
										    		createTR();
										    		$('#tr_email_send_info').html('');
										    		$('#tr_cover_FileInfo').html('');
												},
											    error: function onError (error) {
											        console.error(error);
											    }
										});
								    },
								    error: function onError (error) {
								        console.error(error);
								    }
								});
							});
			    		}
					}else{
						alert('This is Transmittal, which has received final DRN approval:'+data[0]
							+'. Only DCC can ISSUE CANCEL.');
			    		$(".pop_issuecancel").dialog('close');
					}
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
	});
}
function createTR(){
	$('#email_sent_times').html('');
	trOut.DCCSendDateUpdate = '';
	trOut.newOrUpdate = 'new';
	$('#out_check').prop('checked',false);
	$('#out_chk_date').val('');
	$('#out_tr_status').val('Status: Checking');
	$('#out_check').prop('disabled',false);
	$('#out_tr_subject').attr('disabled',false);
	$('#out_discipline_code_id').css('pointer-events', 'auto');
	$('#out_discipline_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
	$('#out_discipline_code_id').css('background-color', '#fff');
	$('#out_tr_issuepur_code_id').css('pointer-events', 'auto');
	$('#out_tr_issuepur_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
	$('#out_tr_issuepur_code_id').css('background-color', '#fff');
	$('#out_send_date').attr('disabled',false);
	$('#out_req_date').attr('disabled',false);
	$('#out_tr_remark').attr('disabled',false);
	$('#out_add_doc').css('display','inline-block');
	$('#out_del_doc').css('display','none');
	$('#tr_cover_FileInfo').html('');
	$('#out_selectTRID').val('');
	$('#out_save').html('Save');
	$('#out_save').attr('disabled',false);
	$('#out_issue').attr('disabled',true);
	$('#out_issue_cancel').attr('disabled',true);
	$('#out_delete').attr('disabled',true);
	$('#out_toexcel').attr('disabled',false);
	$('#out_email').attr('disabled',true);
	$('#out_tr_writer_id').val(session_user_id);
	$('#out_tr_modifier_id').val(session_user_id);
	$('#out_tr_writer_name').val(session_user_eng_nm+'('+session_user_kor_nm+')');
	$('#out_tr_modifier_name').val(session_user_eng_nm+'('+session_user_kor_nm+')');
	$('#out_tr_write_date').val(getTodayDate());
	$('#out_tr_modify_date').val(getTodayDate());
	$('#out_tr_no').val('');
	$('#out_itr_no').val('');
	$('#out_tr_subject').val('');
	$('#out_tr_issuepur_code_id').val('');
	$('#out_discipline_code_id').val('');
	$('#out_tr_remark').val('');
	$('#out_send_date').datepicker('setDate', new Date());
	$('#out_req_date').datepicker('setDate', setReqdate());

	let trOutSelectedDoc = [];
	if(trOut.trOutSelectedTable===undefined){
		setOut_selectDocument(trOutSelectedDoc);
	}else{
		trOut.trOutSelectedTable.setData([]);
	}
	setWhatTROut();
}

function trOutGoingToExcel() {
	if(outProcessType==='TR'){
		let gridData = trOutgoingSearchTbl.getData();
		
		let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
		let tr_period = $('input[type=radio][name=out_tr_period]:checked').val();
		let tr_from_search = $('#out_tr_from').val();
		let tr_to_search = $('#out_tr_to').val();
		let tr_no = $('#tr_no_search').val();
		let tr_subject = $('#tr_subject_search').val();		

		console.log("tr_period = "+tr_period);
		console.log("tr_from_search = "+tr_from_search);
		console.log("tr_to_search = "+tr_to_search);
		
		let jsonList = [];
		
		// json형태로 변환하여 배열로 만듬
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = 'Transmittal_List(Outgoing)';
		
		var f = document.excelUploadForm_TrOutGoing;
		
		f.fileNameTrOut.value = fileName;
		f.jsonRowDatasTrOut.value = jsonList;
		f.prj_idTrOut.value = selectPrjId;
		f.prj_nmTrOut.value = getPrjInfo().full_nm;
		f.inoutTrOut.value = 'OUT';
		f.tr_statusTrOut.value = tr_status;
		f.tr_periodTrOut.value = tr_period;
		f.tr_from_searchTrOut.value = tr_from_search;
		f.tr_to_searchTrOut.value = tr_to_search;
		f.tr_noTrOut.value = tr_no;
		f.tr_subjectTrOut.value = tr_subject;
		f.purposeTrOut.value = 'TR';
//		console.log("jsonList = "+jsonList);
	    f.action = "trOutGoingExcelDownload.do";
	    f.submit();
		
	}else if(outProcessType==='VTR'){
		let gridData = trOutgoingSearchTbl.getData();
		
		let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
		let tr_period = $('input[type=radio][name=out_tr_period]:checked').val();
		let tr_from_search = $('#out_tr_from').val();
		let tr_to_search = $('#out_tr_to').val();
		let tr_no = $('#tr_no_search').val();
		let tr_subject = $('#tr_subject_search').val();
		
		let jsonList = [];
		
		// json형태로 변환하여 배열로 만듬
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = 'VTransmittal_List(Outgoing)';
		
		var f = document.excelUploadForm_TrOutGoing;
		
		f.fileNameTrOut.value = fileName;
		f.jsonRowDatasTrOut.value = jsonList;
		f.prj_idTrOut.value = selectPrjId;
		f.prj_nmTrOut.value = getPrjInfo().full_nm;
		f.inoutTrOut.value = 'OUT';
		f.tr_statusTrOut.value = tr_status;
		f.tr_periodTrOut.value = tr_period;
		f.tr_from_searchTrOut.value = tr_from_search;
		f.tr_to_searchTrOut.value = tr_to_search;
		f.tr_noTrOut.value = tr_no;
		f.tr_subjectTrOut.value = tr_subject;
		f.purposeTrOut.value = 'VTR';
//		console.log("jsonList = "+jsonList);
	    f.action = "vtrOutGoingExcelDownload.do";
	    f.submit();
	}else if(outProcessType==='STR'){
		let gridData = trOutgoingSearchTbl.getData();
		
		let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
		let tr_period = $('input[type=radio][name=out_tr_period]:checked').val();
		let tr_from_search = $('#out_tr_from').val();
		let tr_to_search = $('#out_tr_to').val();
		let tr_no = $('#tr_no_search').val();
		let tr_subject = $('#tr_subject_search').val();
		
		let jsonList = [];
		
		// json형태로 변환하여 배열로 만듬
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = 'STransmittal_List(Outgoing)';
		
		var f = document.excelUploadForm_TrOutGoing;
		
		f.fileNameTrOut.value = fileName;
		f.jsonRowDatasTrOut.value = jsonList;
		f.prj_idTrOut.value = selectPrjId;
		f.prj_nmTrOut.value = getPrjInfo().full_nm;
		f.inoutTrOut.value = 'OUT';
		f.tr_statusTrOut.value = tr_status;
		f.tr_periodTrOut.value = tr_period;
		f.tr_from_searchTrOut.value = tr_from_search;
		f.tr_to_searchTrOut.value = tr_to_search;
		f.tr_noTrOut.value = tr_no;
		f.tr_subjectTrOut.value = tr_subject;
		f.purposeTrOut.value = 'STR';
//		console.log("jsonList = "+jsonList);
	    f.action = "strOutGoingExcelDownload.do";
	    f.submit();
	}
}

function out_selectDocument(){
	$('#docSelTree_doc_type').val('');
	let selectedRow = TRdocumentListTable.getSelectedData();
	let pk = '';
	for(let i=0;i<selectedRow.length;i++){
		pk += selectedRow[i].bean.doc_id + '&&' + selectedRow[i].bean.rev_id + '@@';
	}
	pk = pk.slice(0,-2);
	let alertMessage = getTRDocumentInfo(pk,'OUT','select');
	if(alertMessage!=='') alert(alertMessage);
	$('#pop_docSel').dialog('close');
}
function getTRDocumentInfo(pk,inout,getorselect){
	let alertMessage = '';
	let trOutSelectedDoc = [];
	let getTrOriginDocIndexArray = [];
	let select_discipline = $('#out_discipline_code_id').val();
	
	$.ajax({
	    url: 'getTRDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:popup_folder_id,
	    	pk:pk,
	    	inout:inout,
	    	processType:outProcessType,
        	prj_nm: getPrjInfo().nm
	    },
	    async:false,
	    success: function onData (data) {
	    	let current_folder_doc_type = '';
	    	if(outProcessType === 'TR'){
	    		current_folder_doc_type = 'DCI';
	    	}else if(outProcessType === 'VTR'){
	    		current_folder_doc_type = 'VDCI';
	    	}else if(outProcessType === 'STR'){
	    		current_folder_doc_type = 'SDCI';
	    	}
	    	for(let i=0;i<data[0].length;i++){
	    		var rev_code_id = data[0][i].rev_code_id;
	    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
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

				if($('#out_discipline_code_id option:selected').text()===''){
					$('#out_discipline_code_id').val(trOut.current_folder_discipline_code_id);
					select_discipline = $('#out_discipline_code_id').val();
				}
				
	    		if(lock_yn==='Y' && getorselect!=='get'){
	    			alertMessage += data[0][i].doc_no+'는 Check Out상태이므로 Transmittal 불가합니다.\n';
	    		}else if(data[0][i].doc_type!==current_folder_doc_type){
	    			alertMessage += data[0][i].doc_no+'는 문서타입이 맞지 않습니다.\n';
	    		}else if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
	    			alertMessage += data[0][i].doc_no+'는 비어있는 문서입니다.\n';
	    		}else if(getorselect==='select' && data[0][i].duplicationDoc !== 'N'){
	    			alertMessage += data[0][i].doc_no+' : '+ data[0][i].duplicationDoc+' 에서 이미 첨부된 문서입니다.\n';
	    		}else if(select_discipline!==data[0][i].discip_code_id && selectPrjId!=='0000000209'){	//2022-05-03 요청사항에 따른 수정.scrubber공사의 경우 discipline 달라도 첨부가능하도록 예외 처리.
	    			alertMessage += data[0][i].doc_no+':Other Discipline cannot be selected!!!\n';
				}else{
	    			trOutSelectedDoc.push({
	    				docNo: data[0][i].doc_no,
	    				attach:imgIcon,
	    				RevNo: data[0][i].rev_no,
	    				PRev: data[0][i].p_rev,
	    				category: data[0][i].category_code,
	    				size:data[0][i].size_code,
	    				title:data[0][i].title,
	    				indexString:data[0][i].doc_id+data[0][i].rev_id,
	    				bean:data[0][i]
	    			});
	    			getTrOriginDocIndexArray.push(data[0][i].doc_id+data[0][i].rev_id);
				}
	    	}
	    	if(getorselect==='get'){
	    		trOut.getTrOriginDocIndexArray = getTrOriginDocIndexArray;
	    	}
	    	if(getorselect==='email'){
	    		alertMessage = data[0];
	    	}else{
		    	if(trOut.trOutSelectedTable===undefined){
		    		setOut_selectDocument(trOutSelectedDoc);
		    	}else{
		    		let trOutSelectedDocIndexArray = [];
		    		for(let i=0;i<trOut.trOutSelectedTable.getRows().length;i++){
		    			trOutSelectedDocIndexArray.push(trOut.trOutSelectedTable.getRows()[i].getData().indexString);
		    		}
		    		for(let i=0;i<trOutSelectedDoc.length;i++){
			    		if(!trOutSelectedDocIndexArray.includes(trOutSelectedDoc[i].indexString))
			    			trOutSelectedTable.addRow(trOutSelectedDoc[i]);
		    		}
		    	}
	    	}
	    	
			if(trOut.trOutSelectedTable.getData().length!==0){
				$('#out_del_doc').css('display','inline-block');
			}
			
			if(data[1]!=='파일 존재'){
				$('#tr_cover_FileInfo').html('<span style="color:red;">현재 업로드된 COVER TEMPLATE이 없습니다.</span>');
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	return alertMessage;
}
function disciplineCheck(){
	let selectedDiscipline_code_id = trOutSelectedTable.getData()[0].bean.discip_code_id
	if($('#out_discipline_code_id').val()!==selectedDiscipline_code_id){
		$('#out_discipline_code_id').val(selectedDiscipline_code_id);
		alert('첨부된 도서의 discipline과 일치해야 합니다.');
	}
}
function setOut_selectDocument(trOutSelectedDoc){
	trOutSelectedTable = new Tabulator("#trOutSelectedDoc", {
	    data: trOutSelectedDoc,
	    index: "indexString",
	    height:"100%",
    	placeholder: "No data available",
    	rowContextMenu: setTrOutSelectedRowContextMenu, //add context menu to rows
    	selectable: true,
	    layout: "fitColumns",
	    columns: [{
	            title: "indexString",
	            field: "indexString",
	            visible: false,
	        },{
	            title: "Document No",
	            field: "docNo",
	            hozAlign: "left",
	            width:140
	        },
	        {
	            title: "Rev No",
	            field: "RevNo",
	            width: 72
	        },
	        {
	            title: "Title",
	            field: "title",
		        hozAlign: "left"
	        },
	        {
	            title: "P.Rev",
	            field: "PRev",
	            width: 54
	        },
	        {
	            title: "Attach",
	            field: "attach",
		        formatter : "html",
		        width: 40
	        },
	        {
	            title: "Category",
	            field: "category",
	            width: 74
	        },
	        {
	            title: "Size",
	            field: "size",
	            width: 54
	        }
	    ],
	});
	function setTrOutSelectedRowContextMenu(row) {
		return trOutContextMenu;
    }

	trOutSelectedTable.on("rowDblClick",function(e,row){
		location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(row.getData().bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(row.getData().bean.doc_no.replaceAll("&","@%@")+'.'+row.getData().bean.file_type);
	});
	
	trOut.trOutSelectedTable = trOutSelectedTable;
}
function out_selectedDeleteDoc(){
	let del = trOut.trOutSelectedTable.getSelectedRows();
	trOut.trOutSelectedTable.deleteRow(del);
	if(trOut.trOutSelectedTable.getData().length===0){
		$('#out_del_doc').css('display','none');
	}
}
function outgoingSave(){
	let set_code_type='CORRESPONDENCE_OUT';
	let set_val='';
	let doc_table = '';
	if(outProcessType === 'TR'){
		set_val='DOC TRANSMITTAL';
		doc_table = 'DCI';
	}else if(outProcessType === 'VTR'){
		set_val='VDOC. TRANSMITTAL';
		doc_table = 'VDCI';
	}else if(outProcessType === 'STR'){
		set_val='SITE TRANSMITTAL';
		doc_table = 'SDCI';
	}
	let tr_no = $('#out_tr_no').val();
	if(trOut.DCCSendDateUpdate!=='DCCSendDateUpdate' && tr_no!==''){
		alert('TRANSMITTAL NO는 Issue시에 입력가능합니다.');
		$('#out_tr_no').val('');
		return;
	}
	let tr_subject = $('#out_tr_subject').val();
	let tr_issuepur_code_id = $('#out_tr_issuepur_code_id').val();
	//let tr_issuepur_code_id = issuepur_code[0];
	//let issue_step = issuepur_code[1];
	let send_date = $('#out_send_date').val().replaceAll('-','');
	let req_date = $('#out_req_date').val().replaceAll('-','');
	let discipline_code_id = $('#out_discipline_code_id').val();
	let out_tr_writer_id = $('#out_tr_writer_id').val();
	let tr_remark = $('#out_tr_remark').val();
	let chk_date = $('#out_chk_date').val().replaceAll('-','');
	let tr_status = $('#out_tr_status').val().replaceAll('Status: ','');
	
	if(tr_subject===''){
		alert('SUBJECT를 기입해주세요.');
	}else if(tr_issuepur_code_id==='' || tr_issuepur_code_id===null || tr_issuepur_code_id===undefined){
		alert('ISSUE PURPOSE를 선택해주세요.');
	}else if(send_date===''){
		alert('SEND DATE를 입력해주세요.');
	}else if(req_date===''){
		alert('REQUIRED DATE를 입력해주세요.');
	}else if(discipline_code_id==='' || discipline_code_id===null || discipline_code_id===undefined){
		alert('DISCIPLINE을 선택해주세요.');
	}else if(trOut.trOutSelectedTable===undefined){
		alert('도서를 추가해야 합니다.');
	}else if(trOut.trOutSelectedTable.getRows().length===0){
		alert('도서를 추가해야 합니다.');
	}else{
		let docid_revid_revno = '';
		if(trOut.newOrUpdate!=='new'){
			let getTrChangeDocIndexArray = [];
			for(let i=0;i<trOut.trOutSelectedTable.getData().length;i++){
				getTrChangeDocIndexArray.push(trOut.trOutSelectedTable.getData()[i].indexString);
			}
			//이전에 저장됐던 Doc과 현재 update하려는 Doc 비교
			getChangedIndexArray = findUniqElem(trOut.getTrOriginDocIndexArray,getTrChangeDocIndexArray);
			for(let i=0;i<getChangedIndexArray.length;i++){
				let changedData = trOut.trOutSelectedTable.getRow(getChangedIndexArray[i]);
				if(changedData===false){
					docid_revid_revno += getChangedIndexArray[i].substring(0,10) +'&&'+ getChangedIndexArray[i].substring(10,13) + '&&' + 'tempRevNo' + '@@';
				}else{
					docid_revid_revno += changedData.getData().bean.doc_id +'&&'+ changedData.getData().bean.rev_id + '&&' + changedData.getData().bean.rev_no + '@@';
				}
			}
		}else{
			for(let i=0;i<trOut.trOutSelectedTable.getData().length;i++){
				docid_revid_revno += trOut.trOutSelectedTable.getData()[i].bean.doc_id +'&&'+ trOut.trOutSelectedTable.getData()[i].bean.rev_id + '&&' + trOut.trOutSelectedTable.getData()[i].bean.rev_no + '@@';
			}
		}
		docid_revid_revno = docid_revid_revno.slice(0,-2);
		
		$.ajax({
		    url: 'insertTrOutgoingSave.do',
		    type: 'POST',
		    data:{
		    	newOrUpdate:trOut.newOrUpdate,
		    	DCCSendDateUpdate:trOut.DCCSendDateUpdate,
		    	tr_no:tr_no,
		    	set_code_type:set_code_type,
		    	set_val:set_val,
		    	prj_id:selectPrjId,
		    	tr_subject:tr_subject,
		    	tr_issuepur_code_id:tr_issuepur_code_id,
		    	//issue_step:issue_step,
		    	doc_table:doc_table,
		    	doc_type:doc_table,
		    	send_date:send_date,
		    	req_date:req_date,
		    	discipline_code_id:discipline_code_id,
		    	out_tr_writer_id:out_tr_writer_id,
		    	tr_remark:tr_remark,
		    	docid_revid_revno:docid_revid_revno,
		    	chk_date:chk_date,
		    	tr_status:tr_status,
		    	processType:outProcessType
		    },
		    success: function onData (data) {
		    	if(data[0]==='DCCorADMIN SENDDATE UPDATE'){
		    		if(data[1]>0){
		    			alert('Save Successfully');
		    		}else{
		    			alert('Save failed');
		    		}
		    	}else{
			    	if(data[0]>0){
			    		alert('Save Successfully');
			    		getTrOutSearch();
			    		//let tr_id = trOut.trOutgoingSearchTbl.getData()[0].bean.tr_id;
			    		let tr_id = data[1];
			    		getTrInfo(tr_id);
			    		$('#out_selectTRID').val(tr_id);
			    		trOut.newOrUpdate = tr_id;
			    		$('#out_save').html('Update');
			    		$('#out_tr_no').prop('disabled',false);
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
	
	setWhatTROut();
}
//두 배열 사이의 중복 값을 제거하여 배열로 돌려주는 함수
function findUniqElem(arr1, arr2) {
	  return arr1.concat(arr2)
	    	 .filter(item => !arr1.includes(item) || !arr2.includes(item));
}
function getTrOutSearch(){
	if (selectPrjId===undefined || selectPrjId==='') {
		selectPrjId=getPrjInfo().id;
	}
	let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
	let tr_period = $('input[type=radio][name=out_tr_period]:checked').val();
	let tr_from_search = $('#out_tr_from').val().replaceAll('-','');
	let tr_to_search = $('#out_tr_to').val().replaceAll('-','');
	let tr_no = $('#tr_no_search').val();
	let tr_subject = $('#tr_subject_search').val();
	$.ajax({
	    url: 'getTrInOutSearch.do',
	    type: 'POST',
        beforeSend:function(){
			$('body').prepend(loading);
		},
		complete:function(){
			$('#loading').remove();
		},
	    data:{
	    	prj_id:selectPrjId,
	    	inout: 'OUT',
	    	tr_status:tr_status,
	    	tr_period:tr_period,
	    	tr_from_search:tr_from_search,
	    	tr_to_search:tr_to_search,
	    	tr_no:tr_no,
	    	tr_subject:tr_subject,
	    	processType:outProcessType
	    },
	    async:false,
	    success: function onData (data) {
	    	let trOutgoingSearchData = [];
	    	$('#trOutgoingSearchLength').html(data[0].length);
	    	for(let i=0;i<data[0].length;i++){
		    	trOutgoingSearchData.push({
                    No: i+1,
		    		status: data[0][i].tr_status,
		    		Transmittal:data[0][i].tr_no,
		    		ITR:data[0][i].itr_no,
		    		subject:data[0][i].tr_subject,
		    		sendDate:getDateFormat_YYYYMMDD(data[0][i].send_date),
		    		issuePur:data[0][i].issue_pur,
		    		disc:data[0][i].discip,
		    		writer:data[0][i].writer,
		    		bean:data[0][i]
		    	});
	    	}
	    	setTrInOutSearch(trOutgoingSearchData);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setTrInOutSearch(trOutgoingSearchData){
	trOutgoingSearchTbl = new Tabulator("#trOutgoingSearchTbl", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
		tooltipGenerationMode:"hover",
	    data: trOutgoingSearchData,
	    layout: "fitColumns",
	    height:500,
//		pagination : true,
//		paginationSize : 100,
	    selectable:1,
        tooltips: function (cell) {
            return cell.getValue();
        },
	    columns: [{
	            title: "No",
	            field: "No",
	            visible: false
	        },/*{formatter:"rowSelection", title:"", hozAlign:"center", headerSort:false,width:1},*/
	    	{
            	/*titleFormatter:function(cell, formatterParams, onRendered) {
	   		         var checkbox = document.createElement("input");
	   		          checkbox.id = 'trSearchAll';
	   		          checkbox.name = 'trSearchAll';
	   		          checkbox.type = 'checkbox';
	   		          return checkbox;
	   			},*/
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('trSearch');
	   		        checkbox.id = 'trSearch'+cell.getRow().getPosition();
    		        checkbox.name = 'trSearch';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			headerSort:false,
    			width:10,
    			/*headerClick:function(e, column){
    				$('input:checkbox[name="trSearchAll"]').change(function(){
    			        if($('input:checkbox[name="trSearchAll"]').is(":checked")){
            				$("input:checkbox[name='trSearch']").prop("checked", true);
    			        }else{
            				$("input:checkbox[name='trSearch']").prop("checked", false);
    			        }
    			    });
    			},*/
    			cellClick:function(e, cell){
    				//$(this).getRow().toggleSelect();
    		    }
    		},
	        {
	            title: "Status",
	            field: "status",
				width : 66
	        },
	        {
	            title: "TR No",
	            field: "Transmittal",
				width : 80
	        },
	        {
	            title: "Subject",
	            field: "subject",
				width : 150,
				hozAlign: "left"
	        },
	        {
	            title: "SendDate",
	            field: "sendDate",
	            width: 78
	        },
	        {
	            title: "ITR",
	            field: "ITR",
	            minWidth: 45
	        },
	        {
	            title: "IssuePur",
	            field: "issuePur",
				width : 72
	        },
	        {
	            title: "Discipline",
	            field: "disc",
	            minWidth: 78
	        },
	        {
	            title: "Writer",
	            field: "writer",
				width : 60,
				hozAlign: "left"
	        }
	    ]
	});

	trOutgoingSearchTbl.on("rowSelected", function(row){
		let tr_id = row.getData().bean.tr_id;
		$('#out_selectTRID').val(tr_id);
		getTrInfo(tr_id);
		trOut.newOrUpdate = tr_id;
		$('#out_save').html('Update');
    });

	trOut.trOutgoingSearchTbl = trOutgoingSearchTbl;
}
function getTrInfo(tr_id){
	$.ajax({
	    url: 'getTrInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:tr_id,
	    	processType:outProcessType
	    },
	    async:false,
	    success: function onData (data) {
  			trOut.DCCSendDateUpdate = '';
	    	if(data[3]!==undefined && data[3]!==null){
	    		$('#tr_cover_FileInfo').html('<a style="text-decoration:underline;color:blue;" href="'+current_server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'&rfile_nm='+encodeURIComponent(data[0].tr_no.replaceAll('/','-')+'.'+data[0].file_type)+'">TR COVER: '+data[0].tr_no+'.'+data[3].file_type+'</a>');
    	    }else{
	    		$('#tr_cover_FileInfo').html('');
    	    }
	    	setWhatTROut();
	    	
	    	if(data[2]>0)
	    		$('#tr_email_send_info').html('<a class="link_class" onclick="trOutSendingHistory(`'+data[0].tr_no+'`)">This email already has been sent.('+data[2]+' times)</a>');
	    	else
	    		$('#tr_email_send_info').html('');
	    	
	    	if(data[0].tr_no!==null && data[0].tr_no!=='' && data[0].tr_no!==undefined){//issue상태
	    		$('#out_tr_no').prop('disabled',true);
		    	$('#out_tr_no').val(data[0].tr_no);
		    	$('#out_issue').attr('disabled',true);
		    	$('#out_issue_cancel').attr('disabled',false);
		    	$('#out_save').attr('disabled',true);
		    	$('#out_delete').attr('disabled',true);
		    	$('#out_email').attr('disabled',false);
		    	
		    	$('#out_tr_subject').attr('disabled',true);
		    	$('#out_discipline_code_id').css('pointer-events', 'none');
		    	$('#out_discipline_code_id').css('background', 'none');
		    	$('#out_discipline_code_id').css('background-color', '#FAFAFA');
		    	$('#out_tr_issuepur_code_id').css('pointer-events', 'none');
		    	$('#out_tr_issuepur_code_id').css('background', 'none');
		    	$('#out_tr_issuepur_code_id').css('background-color', '#FAFAFA');
		    	$('#out_send_date').attr('disabled',true);
		    	$('#out_req_date').attr('disabled',true);
		    	$('#out_tr_remark').attr('disabled',true);
		    	$('#out_add_doc').css('display','none');
		    	$('#out_del_doc').css('display','none');
	    	}else{
	    		$('#out_tr_no').prop('disabled',false);
	    		$('#out_tr_no').val('');
		    	$('#out_issue').attr('disabled',false);
		    	$('#out_issue_cancel').attr('disabled',true);
		    	$('#out_save').attr('disabled',false);
		    	$('#out_delete').attr('disabled',false);
		    	$('#out_email').attr('disabled',true);

		    	$('#out_tr_subject').attr('disabled',false);
		    	$('#out_discipline_code_id').css('pointer-events', 'auto');
		    	$('#out_discipline_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
		    	$('#out_discipline_code_id').css('background-color', '#fff');
		    	$('#out_tr_issuepur_code_id').css('pointer-events', 'auto');
		    	$('#out_tr_issuepur_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
		    	$('#out_tr_issuepur_code_id').css('background-color', '#fff');
		    	$('#out_send_date').attr('disabled',false);
		    	$('#out_req_date').attr('disabled',false);
		    	$('#out_tr_remark').attr('disabled',false);
		    	$('#out_add_doc').css('display','inline-block');
		    	$('#out_del_doc').css('display','inline-block');
	    	}
	    	if(data[0].chk_date!==null && data[0].chk_date!=='' && data[0].chk_date!==undefined){
	    		$('#out_chk_date').val(to_date_format(data[0].chk_date,'-'));
		    	$('#out_check').prop('checked',true);
	    	}else{
	    		$('#out_chk_date').val('');
		    	$('#out_check').prop('checked',false);
	    	}
	    	$('#out_tr_status').val('Status: '+data[0].tr_status);
    		if(data[0].tr_status==='Issued'){
    			$('#out_check').prop('disabled',true);
    		}else{
    			$('#out_check').prop('disabled',false);
    		}
	    	$('#out_itr_no').val(data[0].itr_no);
	    	$('#out_tr_subject').val(data[0].tr_subject);
	    	$('#out_tr_issuepur_code_id').val(data[0].tr_issuepur_code_id);
	    	$('#out_send_date').val(getDateFormat_YYYYMMDD(data[0].send_date));
	    	$('#out_req_date').val(getDateFormat_YYYYMMDD(data[0].req_date));
	    	$('#out_discipline_code_id').val(data[0].discipline_code_id);
	    	$('#out_tr_writer_id').val(data[0].tr_writer_id);
	    	$('#out_tr_remark').val(data[0].tr_remark);
	    	$('#out_tr_writer_id').val(data[0].tr_writer_id);
	    	$('#out_tr_modifier_id').val(data[0].mod_id);
	    	$('#out_tr_writer_name').val(data[0].writer+'('+data[0].writer_kor+')');
	    	$('#out_tr_modifier_name').val(data[0].modifier+'('+data[0].modifier_kor+')');
	    	$('#out_tr_write_date').val(getDateFormat(data[0].reg_date));
	    	let mod_date = '';
	    	if(data[0].mod_date===null){
	    		mod_date = getDateFormat(data[0].reg_date);
	    	}else{
	    		mod_date = getDateFormat(data[0].mod_date);
	    	}
	    	$('#out_tr_modify_date').val(mod_date);

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
			//2022-04-19 소진희 기능 변경. Issued 상태일지라도 ADMIN이나 DCC는 send_date, req_date 수정가능
    		if(data[0].tr_status==='Issued' && adminAndDCCArray.includes(session_user_id)!=-1){
    			$('#out_send_date').attr('disabled',false);
		    	$('#out_req_date').attr('disabled',false);
    			$('#out_save').attr('disabled',false);
    			trOut.DCCSendDateUpdate = 'DCCSendDateUpdate';
    		}
	    	let trOutSelectedDoc = [];
	    	let getTrOriginDocIndexArray = [];
	    	
	    	for(let i=0;i<data[1].length;i++){
	    		var rev_code_id = data[1][i].rev_code_id;
	    		var lock_yn = data[1][i].lock_yn=='Y'?'Y':'N';
	    		var ext = data[1][i].file_type;
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
    			trOutSelectedDoc.push({
    				docNo: data[1][i].doc_no,
    				attach:imgIcon,
    				RevNo: data[1][i].rev_no,
    				PRev: data[1][i].p_rev,
    				category: data[1][i].category,
    				size:data[1][i].size_code,
    				title:data[1][i].title,
    				indexString:data[1][i].doc_id+data[1][i].rev_id,
    				folder_id:data[1][i].folder_id,
    				bean:data[1][i]
    			});
    			getTrOriginDocIndexArray.push(data[1][i].doc_id+data[1][i].rev_id);
	    	}
    		trOut.getTrOriginDocIndexArray = getTrOriginDocIndexArray;

    		setOut_selectDocument(trOutSelectedDoc);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function createFromPreTrOut(){
	if(trOut.trOutgoingSearchTbl.getSelectedData().length===0){
		alert('아래 테이블에서 TR을 선택해주세요.');
	}else{
		let tr_id = trOut.trOutgoingSearchTbl.getSelectedData()[0].bean.tr_id;
		getTrInfo(tr_id);
		trOut.newOrUpdate='new';
    	$('#out_tr_subject').attr('disabled',false);
    	$('#out_discipline_code_id').css('pointer-events', 'auto');
    	$('#out_discipline_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
    	$('#out_discipline_code_id').css('background-color', '#fff');
    	$('#out_tr_issuepur_code_id').css('pointer-events', 'auto');
    	$('#out_tr_issuepur_code_id').css('background', 'url(../resource/images/select.png) no-repeat right center #fff');
    	$('#out_tr_issuepur_code_id').css('background-color', '#fff');
    	$('#out_send_date').attr('disabled',false);
    	$('#out_req_date').attr('disabled',false);
    	$('#out_tr_remark').attr('disabled',false);
    	$('#out_add_doc').css('display','inline-block');
    	$('#out_del_doc').css('display','inline-block');
		$('#out_save').html('Save');
		$('#out_save').attr('disabled',false);
		$('#out_issue').attr('disabled',true);
		$('#out_issue_cancel').attr('disabled',true);
		$('#out_delete').attr('disabled',true);
		$('#out_toexcel').attr('disabled',false);
		$('#out_email').attr('disabled',true);
		$('#out_tr_writer_id').val(session_user_id);
		$('#out_tr_modifier_id').val(session_user_id);
		$('#out_tr_writer_name').val(session_user_eng_nm+'('+session_user_kor_nm+')');
		$('#out_tr_modifier_name').val(session_user_eng_nm+'('+session_user_kor_nm+')');
		$('#out_tr_write_date').val(getTodayDate());
		$('#out_tr_modify_date').val(getTodayDate());
		$('#out_tr_no').val('');
		$('#out_itr_no').val('');
		trOut.trOutSelectedTable.setData([]);
	}
}
function out_delete(){
	$.ajax({
	    url: 'deleteTR.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:trOut.newOrUpdate,
	    	processType:outProcessType
	    },
	    success: function onData (data) {
	    	if(data[0]>0 && data[1]>0){
	    		alert('Delete Completed');
	    		$(".pop_TrDelete").dialog('close');
	    		getTrOutSearch();
	    		createTR();
	    	}else{
	    		alert('Delete Failed');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function TRIssueCancelCancel(){
	$(".pop_issuecancel").dialog('close');
}
function TRIssueCancelDCCCancel(){
	$(".pop_issuecancelDCC").dialog('close');
}
function TRDeleteCancel(){
	$(".pop_TrDelete").dialog('close');
}
function trIncomingReg(){
	if($("input:checkbox[name='trSearch']:checked").length===0){
		alert('아래 테이블에서 ISSUE 상태인 TR의 체크박스를 선택해주세요.');
	}else{
		var checkedIndex = '';
		$("input:checkbox[name='trSearch']:checked").each(function(){
			checkedIndex += $(this).attr('id').replaceAll('trSearch','') + ',';
		});
		checkedIndex = checkedIndex.slice(0, -1);
		var checkedIndexArray = checkedIndex.split(',');
		var checkedTrIdString = '';
		for(i=0;i<checkedIndexArray.length;i++){
			if(trOutgoingSearchTbl.getRow((parseInt(checkedIndexArray[i])+1)).getData().status!=='Issued'){
				alert('Issued 상태가 아닌 TR은 Incoming을 등록할 수 없습니다.');
				return;
			}
			checkedTrIdString += trOutgoingSearchTbl.getRow((parseInt(checkedIndexArray[i])+1)).getData().bean.tr_id +',';
		}
		checkedTrIdString = checkedTrIdString.slice(0, -1);

		let tab_id = "incomingTab";
		let jsp_Path = '/page/trIncoming';
		let type = 'TRIncoming';
		let text ='INCOMING';
		if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
			// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
			let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
			tabList[tabIdx].folder_id = current_folder_id;
			
			tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
			
			tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
		} 	
		$('#tr_incoming_filename').val('');	//comment file 초기화
		$('#transmittal_left_incoming').css('display','block');
		//let tr_id = $('#out_selectTRID').val();
		selectTR(checkedTrIdString,'OUT');
		inProcessType = outProcessType;
	}
}
function getTRPrjDocumentIndexList(documentselect,popup_folder_id){
	var select_rev_version = $('#docsel_select_rev_version').val();
	var unread_yn=$('#docsel_unread_yn').is(':checked')==true?'Y':'N';
	let useDRNYN = useDrnCheckForThisFolder(selectPrjId,popup_folder_id);
	if(useDRNYN==='Y'){
		alert('DRN 적용 폴더의 도서는 TR Ougoing을 직접 등록할 수 없습니다.');
		TRdocumentListTable.setData([]);
		return false;
	}
	current_folder_id = popup_folder_id;
	if(TRdocumentListTable!==undefined){
		TRdocumentListTable.deselectRow();
	}
	$.ajax({
	    url: 'getPrjDocumentIndexList.do',
	    type: 'POST',
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
		    	for(i=0;i<data[0].length;i++){
		    		var Modified='';
		    		if(data[0][i].mod_date!=null){
		    			Modified = getDateFormat(data[0][i].mod_date);
		    		}else if(data[0][i].mod_date==null){
		    			Modified = getDateFormat(data[0][i].reg_date);
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
	                    Revision: data[0][i].rev_no,
	                    Title: data[0][i].title,
	                    Modified: Modified,
	                    Size: formatBytes(data[0][i].file_size, 2),
	                    bean: data[0][i]
		    		});
		    	}
		    	setTRPrjDocumentIndexList(doc_ctrl_li,documentselect);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setTRPrjDocumentIndexList(doc_ctrl_li,documentselect){
	let tableId = '#docSelTbl';
	TRdocumentListTable = new Tabulator(tableId, {
		data: doc_ctrl_li,
		layout: "fitColumns",
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
				widthGrow: 3
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
				widthGrow: 5
			},
			{
				title: "Modified",
				field: "Modified",
				width : 120
			}, {
				title: "Size",
				field: "Size",
				hozAlign: "right",
				width: 75
			}
		],
	});

	var select_rev_version = $('#docsel_select_rev_version').val();
	
	TRdocumentListTable.on("tableBuilt", function(){
		if(select_rev_version==='current'){
			TRdocumentListTable.hideColumn("status");
		}else{
			TRdocumentListTable.showColumn("status");
		}
	});
	
}

function TRdocumentControlDownload(){
	let selectedArray = TRdocumentListTable.getSelectedData();
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
				multiDownTbl.push({
					FileName:selectedArray[i].bean.rfile_nm,
					RevNo:selectedArray[i].bean.rev_no,
					Title:selectedArray[i].bean.title,
					Size:formatBytes(selectedArray[i].bean.file_size,2),
					bean:selectedArray[i].bean
				});
				var table = new Tabulator("#multiDownTbl", {
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
			$('#fileMultiDownload').attr('onclick','TRfileMultiDownload()');
			return false;
		}
	}
}
function TRfileMultiDownload(){
	var selectedArray = trOut.TrOutMultiDownTable.getData();
	var downloadFiles = '';
	var accHisDownList = [];
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
		location.href=current_server_domain+'/fileDownload.do?prj_id='+encodeURIComponent(selectPrjId)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
	}
	$(".pop_multiDownload").dialog("close");
}