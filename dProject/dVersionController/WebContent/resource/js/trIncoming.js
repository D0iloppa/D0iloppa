var trIn = {
	newOrUpdate:'new'
};
var getOutTRTable;
var selectedAddType2='';
var auth_trIncoming;
$(function(){
	if ( selectPrjId != 'undefined' && selectPrjId != undefined) {
		auth_trIncoming = checkFolderAuth(selectPrjId,current_folder_id);
	}
	
	context_path = $('#context_path').val();
	session_user_id = $('#session_user_id').val();
	session_user_eng_nm = $('#session_user_eng_nm').val();

    var fileTarget = $('#tr_incoming_filename');

    fileTarget.on('change', function () {
        if (window.FileReader) {
            var fullName = $(this)[0].files[0].name;
            var size = $(this)[0].files[0].size;
            var extension = $(this)[0].files[0].name.split('.').pop().toLowerCase();
        } else {
            var fullName = $(this).val().split('/').pop().split('\\').pop();
        }
       
        trIn.rfile_nm = fullName;
        trIn.file_type = extension;
        trIn.file_size = size;
        var today = new Date();
        $('#trIncoming_uploadFileDate').val(formatBytes(size,2)+', '+today.toLocaleString());
    });

	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('ref_doc_no') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 getSearchTR();
			 }
		}
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
	$("#summernoteMailIn").summernote({
		height: 500,
		lang: "ko_KR",
		toolbar: toolbar,
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체']
	});
	
	trIn_ready = true;
	$('#in_tr_from').datepicker('setDate', lastMonth());
	$('#in_tr_to').datepicker('setDate', new Date());
	$('#in_send_date').datepicker('setDate', new Date());
	$('#in_req_date').datepicker('setDate', setReqdate());

	$('input[type=radio][name=in_tr_period]').change(function(){
	    if($(this).val()==='oneweek'){
	    	$('#in_tr_from').datepicker('setDate', lastWeek());
	    	$('#in_tr_to').datepicker('setDate', new Date());
	    }else if($(this).val()==='onemonth'){
	    	$('#in_tr_from').datepicker('setDate', lastMonth());
	    	$('#in_tr_to').datepicker('setDate', new Date());
	    }else{
	    	$('#in_tr_from').val('');
	    	$('#in_tr_to').val('');
	    }
	   
	});

	

	trInContextMenu = [{
		label: "Open",
		action: function(e, row) {
			let selectedArray = trIn.trInSelectedTable.getSelectedData();
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
			let selectedArray = trIn.trInSelectedTable.getSelectedData();
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
						TrInMultiDownTable = new Tabulator("#multiDownTbl", {
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
						trIn.TrInMultiDownTable = TrInMultiDownTable;
					}
					$(".pop_multiDownload").dialog("open");
					$('#fileMultiDownload').attr('onclick','TRInfileMultiDownload()');
					return false;
				}
			}
		}
	},
	{
		label: "Property",
		action: function(e, row) {
			let tr_id = $('#in_selectTRID').val();
			if(tr_id===''){
				tr_id='TRADD';
				let attach = row.getData().attach;
				let doc_no = row.getData().docNo;
				let rev_no = row.getData().RevNo;
				let title = row.getData().title;
				let folder_id = row.getData().bean.folder_id;
				contextmenuProperty(row,tr_id,folder_id,attach,doc_no,rev_no,title); 
			}else{
				contextmenuProperty(row,tr_id);
			}
		}
	},
	{
		label: "Select All",
		action: function(e, row) {
			trIn.trInSelectedTable.selectRow("visible");
			trInContextMenu[4].disabled = false;
		}
	},
	{
		disabled: true,
		label: "Cancel Select",
		action: function(e, row) {
			trIn.trInSelectedTable.deselectRow();
    		trInContextMenu[4].disabled = true;
		}
	}];
	
    $(".pop_searchTR").dialog({
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
	$(".pop_TrDelete2").dialog({
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
    $(document).on("click", "#in_add_doc", function() {
        $(".pop_searchTR").dialog("open");
        $('#ref_doc_no').val('');
        $('#selectTR').val('');
        $('#selectTR').html('');
    	var TrNoList = [];
    	setTrNoList(TrNoList);
        return false;
    });
    $(document).on("click", "#in_delete", function() {
    	$(".pop_TrDelete2").dialog('open');
        return false;
    });
});
function setWhatTRIn(){
	if(inProcessType==='TR'){
		$('#TRInTitleHeader').html('TR INCOMING');
	}else if(inProcessType==='VTR'){
		$('#TRInTitleHeader').html('VTR INCOMING');
	}else if(inProcessType==='STR'){
		$('#TRInTitleHeader').html('STR INCOMING');
	}
}
function TRInDeleteCancel(){
	$(".pop_TrDelete2").dialog('close');
}
function in_delete(){
	let tr_no = $('#in_tr_no').val();
	$.ajax({
	    url: 'deleteTRIn.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:trIn.newOrUpdate,
	    	doc_no:tr_no,
	    	processType:inProcessType
	    },
	    success: function onData (data) {
	    	if(data[0]>0 && data[1]>0){
	    		alert('Delete Completed');
	    		$(".pop_TrDelete2").dialog('close');
	    		getTrInSearch();
	    		createTRIn();
	    	}else{
	    		alert('Delete Failed');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getTrInSearch(){
	if (selectPrjId===undefined || selectPrjId==='') {
		selectPrjId=getPrjInfo().id;
	}
	let tr_period = $('input[type=radio][name=in_tr_period]:checked').val();
	let tr_from_search = $('#in_tr_from').val().replaceAll('-','');
	let tr_to_search = $('#in_tr_to').val().replaceAll('-','');
	let tr_no = $('#tr_in_no_search').val();
	let tr_subject = $('#tr_in_subject_search').val();
	$.ajax({
	    url: 'getTrInOutSearch.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	inout: 'IN',
	    	tr_status:'all',
	    	tr_period:tr_period,
	    	tr_from_search:tr_from_search,
	    	tr_to_search:tr_to_search,
	    	tr_no:tr_no,
	    	tr_subject:tr_subject,
	    	processType:inProcessType
	    },
	    async:false,
	    success: function onData (data) {
	    	let trIncomingSearchData = [];
	    	if(data[0]===undefined){
		    	$('#trIncomingSearchLength').html('0');
	    	}else{
		    	$('#trIncomingSearchLength').html(data[0].length);
		    	for(let i=0;i<data[0].length;i++){
			    	trIncomingSearchData.push({
			    		TransmittalNo:data[0][i].tr_no,
			    		subject:data[0][i].tr_subject,
			    		receivedDate:getDateFormat_YYYYMMDD(data[0][i].send_date),
			    		writer:data[0][i].writer,
			    		TrRemark:data[0][i].tr_remark,
			    		bean:data[0][i]
			    	});
		    	}
	    	}
	    	console.log(trIncomingSearchData);
	    	setTrInSearch(trIncomingSearchData);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setTrInSearch(trIncomingSearchData){
	trIncomingSearchTbl = new Tabulator("#trIncomingSearchTbl", {
	    data: trIncomingSearchData,
	    layout: "fitColumns",
	    selectable:1,
	    height:500,
//	    pagination : true,
//		paginationSize : 100,
	    columns: [
	        {
	            title: "TR No",
	            field: "TransmittalNo",
				width : 80
	        },
	        {
	            title: "Subject",
	            field: "subject",
				width : 150,
				hozAlign: "left"
	        },
	        {
	            title: "Received Date",
	            field: "receivedDate",
				width : 102
	        },
	        {
	            title: "Writer",
	            field: "writer",
				width : 60,
				hozAlign: "left"
	        },
	        {
	            title: "TR Remark",
	            field: "TrRemark",
	            width: 82
	        }
	    ]
	});

	trIncomingSearchTbl.on("rowSelected", function(row){
		let tr_id = row.getData().bean.tr_id;
		$('#in_selectTRID').val(tr_id);
		selectTR(tr_id);
		trIn.newOrUpdate = tr_id;
		$('#in_save').html('Update');
    });

	trIn.trIncomingSearchTbl = trIncomingSearchTbl;
}
function getTrInDisciplineList(){
	$.ajax({
	    url: 'getTrDisciplineList.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId
	    },
	    success: function onData (data) {
	    	let html = '';
	    	for(let i=0;i<data[0].length;i++){
	    		html += '<input type="checkbox" name="TRINDiscip" value="'+data[0][i].discip_code_id+'"><label for="">'+data[0][i].discip_code+'</label>';
	    	}
	    	$('#trInDiscipline').html(html);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function searchTRCancel(){
	$(".pop_searchTR").dialog('close');
}
function createTRIn(){
	trIn.newOrUpdate = 'new';
	$('#in_selectTRID').val('');
	$('#in_save').html('Save');
	$('#in_tr_no').val('');
	//$('#tr_incoming_attachfilename').val('');
	$('#tr_incoming_uploadFileInfo').html('');
	$('#trIncoming_uploadFileDate').val('');
	$('#in_tr_subject').val('');
	$('#in_note').val('');
	$('#in_received_date').datepicker('setDate', new Date());
	$('#in_tr_writer_id').val(session_user_id);
	$('#in_tr_writer_name').val(session_user_eng_nm + '('+session_user_kor_nm+')');
	$('#in_tr_write_date').val(getToday());
	$('#in_tr_modifier_id').val(session_user_id);
	$('#in_tr_modify_date').val(getToday());
	$('#in_tr_modifier_name').val(session_user_eng_nm + '('+session_user_kor_nm+')');
	$('input[name="TRINDiscip"]').prop('checked',false);
	if(trIn.trInSelectedTable!==undefined) trIn.trInSelectedTable.setData([]);
}

function trInComingToExcel() {
	//console.log("엑셀 다운로드");
	if(inProcessType==='TR'){
		let gridData = trIncomingSearchTbl.getData();
		
		//let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
		let tr_period = $('input[type=radio][name=in_tr_period]:checked').val();
		let tr_from_search = $('#in_tr_from').val();
		let tr_to_search = $('#in_tr_to').val();
		let tr_no = $('#tr_in_no_search').val();
		let tr_subject = $('#tr_in_subject_search').val();
		
		let jsonList = [];
		
		// json형태로 변환하여 배열로 만듬
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = 'Transmittal_List(InComing)';
		
		var f = document.excelUploadForm_TrInComing;
		
		f.fileNameTrIn.value = fileName;
		f.jsonRowDatasTrIn.value = jsonList;
		f.prj_idTrIn.value = selectPrjId;
		f.prj_nmTrIn.value = getPrjInfo().full_nm;
		f.inoutTrIn.value = 'IN';
		f.tr_statusTrIn.value = 'all';
		f.tr_periodTrIn.value = tr_period;
		f.tr_from_searchTrIn.value = tr_from_search;
		f.tr_to_searchTrIn.value = tr_to_search;
		f.tr_noTrIn.value = tr_no;
		f.tr_subjectTrIn.value = tr_subject;
		f.purposeTrIn.value = 'TR';
//		console.log("jsonList = "+jsonList);
	    f.action = "trOutGoingExcelDownload.do";
	    f.submit();
		
	}else if(inProcessType==='VTR'){
		let gridData = trIncomingSearchTbl.getData();
		
		//let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
		let tr_period = $('input[type=radio][name=in_tr_period]:checked').val();
		let tr_from_search = $('#in_tr_from').val();
		let tr_to_search = $('#in_tr_to').val();
		let tr_no = $('#tr_in_no_search').val();
		let tr_subject = $('#tr_in_subject_search').val();
		
		let jsonList = [];
		
		// json형태로 변환하여 배열로 만듬
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = 'VTransmittal_List(InComing)';
		
		var f = document.excelUploadForm_TrInComing;
		
		f.fileNameTrIn.value = fileName;
		f.jsonRowDatasTrIn.value = jsonList;
		f.prj_idTrIn.value = selectPrjId;
		f.prj_nmTrIn.value = getPrjInfo().full_nm;
		f.inoutTrIn.value = 'IN';
		f.tr_statusTrIn.value = 'all';
		f.tr_periodTrIn.value = tr_period;
		f.tr_from_searchTrIn.value = tr_from_search;
		f.tr_to_searchTrIn.value = tr_to_search;
		f.tr_noTrIn.value = tr_no;
		f.tr_subjectTrIn.value = tr_subject;
		f.purposeTrIn.value = 'VTR';
//		console.log("jsonList = "+jsonList);
	    f.action = "vtrOutGoingExcelDownload.do";
	    f.submit();
	}else if(inProcessType==='STR'){
		let gridData = trIncomingSearchTbl.getData();
		
		//let tr_status = $('input[type=radio][name=out_tr_status]:checked').val();
		let tr_period = $('input[type=radio][name=in_tr_period]:checked').val();
		let tr_from_search = $('#in_tr_from').val();
		let tr_to_search = $('#in_tr_to').val();
		let tr_no = $('#tr_in_no_search').val();
		let tr_subject = $('#tr_in_subject_search').val();
		
		let jsonList = [];
		
		// json형태로 변환하여 배열로 만듬
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = 'STransmittal_List(InComing)';
		
		var f = document.excelUploadForm_TrInComing;
		
		f.fileNameTrIn.value = fileName;
		f.jsonRowDatasTrIn.value = jsonList;
		f.prj_idTrIn.value = selectPrjId;
		f.prj_nmTrIn.value = getPrjInfo().full_nm;
		f.inoutTrIn.value = 'IN';
		f.tr_statusTrIn.value = 'all';
		f.tr_periodTrIn.value = tr_period;
		f.tr_from_searchTrIn.value = tr_from_search;
		f.tr_to_searchTrIn.value = tr_to_search;
		f.tr_noTrIn.value = tr_no;
		f.tr_subjectTrIn.value = tr_subject;
		f.purposeTrIn.value = 'STR';
//		console.log("jsonList = "+jsonList);
	    f.action = "strOutGoingExcelDownload.do";
	    f.submit();
	}
}

function getSearchTR(){
	let searchKeyword = $('#ref_doc_no').val();
	$.ajax({
	    url: 'getSearchTRNo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	doc_no:searchKeyword,
	    	processType:inProcessType
	    },
	    success: function onData (data) {
	    	var TrNoList = [];
	    	for(i=0;i<data[0].length;i++){
	    		TrNoList.push({
	    			tr_id: data[0][i].tr_id,
		    		status: data[0][i].tr_status,
		    		Transmittal:data[0][i].tr_no,
		    		ITR:data[0][i].itr_no,
		    		subject:data[0][i].tr_subject,
		    		sendDate:getDateFormat_YYYYMMDD(data[0][i].send_date),
		    		issuePur:data[0][i].issue_pur,
		    		disc:data[0][i].discip,
		    		writer:data[0][i].reg_id,
		    		bean:data[0][i]
	    		});
	    	}
	    	setTrNoList(TrNoList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setTrNoList(TrNoList){
	getOutTRTable = new Tabulator("#TrNoList", {
        selectable:1,
        data: TrNoList,
        placeholder:"No Data Available",
        layout: "fitColumns",
        height: 200,
        columns: [
        	{
	            title: "tr_id",
	            field: "tr_id",
	            visible: false
        	},
        	{
	            title: "Status",
	            field: "status",
				width : 66
	        },
	        {
	            title: "TR No",
	            field: "Transmittal",
	            hozAlign: "left",
				width : 150
	        },
	        {
	            title: "Subject",
	            field: "subject",
				hozAlign: "left"
	        },
	        {
	            title: "ITR",
	            field: "ITR",
	            width: 150
	        },
	        {
	            title: "SendDate",
	            field: "sendDate",
	            width: 78
	        },
	        {
	            title: "IssuePur",
	            field: "issuePur",
				width : 70
	        },
	        {
	            title: "Discipline",
	            field: "disc",
	            width: 80
	        },
	        {
	            title: "Writer",
	            field: "writer",
				width : 60,
				hozAlign: "left"
	        }
        ],
    });
}
function selectTRonclick(){
	let tr_id = getOutTRTable.getSelectedData()[0].tr_id;
	let tr_status = getOutTRTable.getSelectedData()[0].status;

	let incomingTF = false;
	let getTRFileInfo;
	$.ajax({
	    url: 'getOutRegIncomingTrInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:tr_id,
	    	processType:inProcessType
	    },
	    async:false,
	    success: function onData (data) {
	    	if(data[1]==='Incoming 불가'){
	    		alert('선택하신 TR은 이미 모든 도서가 Incoming이 등록된 Transmittal입니다.');
	    	}else if(data[1]==='Incoming 등록 불가한 도서 있음'){
	    		alert('해당 TR의 Documents 중 이미 Incoming 등록된 도서는 제외하고 추가됩니다.');
	    		incomingTF = true;
	    		getTRFileInfo = data[2];
	    	}else{
	    		incomingTF = true;
	    		getTRFileInfo = data[1];
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	if(incomingTF===true){
		if(tr_status!=='Issued'){
			alert('Issued 상태의 TR만 선택할 수 있습니다.');
		}else{
			$("#selectTR").val(tr_id);
			AddInSelectTR(tr_id,'AddINSelectTR',getTRFileInfo);
		}
	}
}
function selectTR(tr_id,outreg){
	if(trIn.trInSelectedTable!==undefined){
		trIn.trInSelectedTable.setData([]);
	}
	if(!tr_id) tr_id = $('#in_selectTRID').val();
	let postUrl = 'getTrInfo.do';
	if(outreg==='OUT'){
		inProcessType = outProcessType;
		postUrl = 'getOutRegIncomingTrInfo.do';
	}
	$.ajax({
	    url: postUrl,
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:tr_id,
	    	processType:inProcessType
	    },
	    success: function onData (data) {
	    	if(outreg==='OUT' && data[1]==='Incoming 불가'){
	    		alert('선택하신 TR은 이미 Incoming이 등록된 Transmittal입니다.');
	    		return;
	    	}else if(outreg==='OUT' && data[1]==='Incoming 등록 불가한 도서 있음'){
	    		alert('해당 TR의 Documents 중 이미 Incoming 등록된 도서는 제외하고 추가됩니다.');
	    		data[1]=data[2];
	    	}
	    	
	    	if(data[0].tr_no!==null && data[0].tr_no!==''){
		    	$('#in_tr_no').val(data[0].tr_no);
    	    	if(data[3]!==null && data[3]!==undefined && outreg!=='OUT'){
    	    		let attach_file_date = '';
    	    		if(data[3].attach_file_date!==null){
    	    			attach_file_date = ', '+getDateFormat(data[3].attach_file_date);
    	    		}
    		    	$('#trIncoming_uploadFileDate').val(formatBytes(data[3].file_size,2)+attach_file_date);
    		    	//$('#tr_incoming_attachfilename').val('');
    		    	let sfile_nm = data[3].doc_id+'_001.'+data[3].file_type;
    		    	$('#tr_incoming_uploadFileInfo').html('<a style="text-decoration:underline;color:blue;" href="'+current_server_domain+'/downloadCommentFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(sfile_nm)+'&rfile_nm='+encodeURIComponent(data[0].tr_no+'.'+data[3].file_type)+'">'+data[0].tr_no+'.'+data[3].file_type+'</a>');
    	    	}else{
    		    	$('#trIncoming_uploadFileDate').val('');
    		    	//$('#tr_incoming_attachfilename').val('');
    		    	$('#tr_incoming_uploadFileInfo').html('');
    	    	}
	    	}else{
		    	$('#in_tr_no').val('');
		    	//$('#tr_incoming_attachfilename').val('파일선택');
		    	$('#tr_incoming_uploadFileInfo').html('');
		    	$('#trIncoming_uploadFileDate').val('');
	    	}
	    	$('#in_tr_subject').val(data[0].tr_subject);
	    	let tr_distribute = data[0].tr_distribute;
	    	if(tr_distribute!==null && tr_distribute!==undefined){
		    	let tr_distribute_array = tr_distribute.split(',');
		    	$('input[name="TRINDiscip"]').prop('checked',false);
		    	for(let i=0;i<tr_distribute_array.length;i++){
			    	$('input[name="TRINDiscip"][value="'+tr_distribute_array[i]+'"]').prop('checked',true);
		    	}
	    	}
	    	if(!outreg){
	    		$('#in_received_date').val(getDateFormat_YYYYMMDD(data[0].send_date));
		    	$('#in_tr_writer_id').val(data[0].tr_writer_id);
		    	$('#in_tr_writer_name').val(data[0].writer + '('+data[0].writer_kor+')');
		    	$('#in_tr_write_date').val(getDateFormat(data[0].reg_date));
		    	$('#in_tr_modifier_id').val(data[0].tr_modifier_id);
		    	$('#in_tr_modifier_name').val(data[0].modifier + '('+data[0].modifier_kor+')');
		    	$('#in_tr_modify_date').val(getDateFormat(data[0].mod_date));
		    	$('#in_note').val(data[0].tr_remark);
	    	}
	    	
	    	let trInSelectedDoc = [];
	    	let getTrOriginDocIndexArray = [];
	    	for(let i=0;i<data[1].length;i++){
	    		var rev_code_id = data[1][i].rev_code_id;
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
				trInSelectedDoc.push({
					docNo: data[1][i].doc_no,
					discipline:data[1][i].discip,
					attach:imgIcon,
					RevNo: data[1][i].rev_no,
					rtnstatus: data[1][i].rtn_status_code_id,
					category: data[1][i].category,
					title:data[1][i].title,
					indexString:data[1][i].doc_id+'&&'+data[1][i].rev_id+'&&'+data[1][i].rev_no,
					No:i,
					bean:data[1][i]
				});
				getTrOriginDocIndexArray.push(data[1][i].doc_id+data[1][i].rev_id+'@@'+i);
	    	}
	    	trIn.getTrOriginDocIndexArray = getTrOriginDocIndexArray;
	    	
	    	if(trIn.trInSelectedTable===undefined){
	    		setIn_selectDocument(trInSelectedDoc);
	    	}else{
	    		if(trIn.trInSelectedTable.getRows()!==undefined){
		    		let trInSelectedDocIndexArray = [];
		    		for(let i=0;i<trIn.trInSelectedTable.getRows().length;i++){
		    			trInSelectedDocIndexArray.push(trIn.trInSelectedTable.getRows()[i].getData().indexString);
		    		}
		    		for(let i=0;i<trInSelectedDoc.length;i++){
			    		if(!trInSelectedDocIndexArray.includes(trInSelectedDoc[i].indexString))
			    			trInSelectedTable.addRow(trInSelectedDoc[i]);
		    		}
	    		}
	    	}
	    	
	    	if(outreg==='OUT'){
				$('#tr_incoming_uploadFileInfo').html('');
		    	$('#trIncoming_uploadFileDate').val('');
				$('#in_tr_no').val('');
				getTrInSearch();
				$('#in_del_doc').css('display','inline-block');
	    	}
	    	
			if(trIn.trInSelectedTable.getData().length!==0){
				$('#in_del_doc').css('display','inline-block');
			}

			if(outreg!=='OUT'){
				if(auth_trIncoming.w !== 'Y'){
					$('#in_del_doc').css('display','none');
				}
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function AddInSelectTR(tr_id,outreg,getTRFileInfo){
	$.ajax({
	    url: 'getTrFile.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	tr_id:tr_id,
	    	processType:inProcessType
	    },
	    success: function onData (data) {
	    	if(getTRFileInfo){
	    		data[0]=getTRFileInfo;
	    	}
	    	let pk = '';
	    	for(let i=0;i<data[0].length;i++){
	    		pk += data[0][i].doc_id + '&&' + data[0][i].rev_id + '@@';
	    	}
	    	pk = pk.slice(0,-2);
	    	$(".pop_searchTR").dialog('close');
	    	getTRInDocumentInfo(pk,'OUT','get');

			$('#in_del_doc').css('display','inline-block');
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getTRInDocumentInfo(pk,inout,getorselect){
	let trInSelectedDoc = [];
	let getTrOriginDocIndexArray = [];
	$.ajax({
	    url: 'getTRDocumentInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId,
	    	folder_id:popup_folder_id,
	    	pk:pk,
	    	inout:inout,
	    	processType:inProcessType
	    },
	    async:false,
	    success: function onData (data) {
	    	for(let i=0;i<data[0].length;i++){
	    		var rev_code_id = data[0][i].rev_code_id;
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
				trInSelectedDoc.push({
					docNo: data[0][i].doc_no,
					discipline:data[0][i].discipline,
					attach:imgIcon,
					RevNo: data[0][i].rev_no,
					rtnstatus: data[0][i].rtn_status_code_id,
					category: data[0][i].category_code,
					title:data[0][i].title,
					indexString:data[0][i].doc_id+'&&'+data[0][i].rev_id+'&&'+data[0][i].rev_no,
					No:i,
					bean:data[0][i]
				});
				getTrOriginDocIndexArray.push(data[0][i].doc_id+data[0][i].rev_id+'@@'+i);
	    	}
	    	if(getorselect==='get'){
	    		trIn.getTrOriginDocIndexArray = getTrOriginDocIndexArray;
	    	}
    		
	    	if(trIn.trInSelectedTable===undefined){
	    		setIn_selectDocument(trInSelectedDoc);
	    	}else{
	    		if(trIn.trInSelectedTable.getRows()!==undefined){
		    		let trInSelectedDocIndexArray = [];
		    		for(let i=0;i<trIn.trInSelectedTable.getRows().length;i++){
		    			trInSelectedDocIndexArray.push(trIn.trInSelectedTable.getRows()[i].getData().indexString);
		    		}
		    		for(let i=0;i<trInSelectedDoc.length;i++){
			    		if(!trInSelectedDocIndexArray.includes(trInSelectedDoc[i].indexString))
			    			trInSelectedTable.addRow(trInSelectedDoc[i]);
		    		}
	    		}
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setIn_selectDocument(trInSelectedDoc){
	let set_code_type = '';
	if(inProcessType==='TR'){
		set_code_type = 'TR RETURN STATUS';
	}else if(inProcessType==='VTR'){
		set_code_type = 'VTR RETURN STATUS';
	}else if(inProcessType==='STR'){
		set_code_type = 'STR RETURN STATUS';
	}
	trInSelectedTable = new Tabulator("#trInSelectedDoc", {
	    data: trInSelectedDoc,
	    index: "indexString",
	    height:"100%",
    	placeholder: "No data available",
    	rowContextMenu: setTrInSelectedRowContextMenu, //add context menu to rows
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
				width: 140
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
	            title: "Rtn Status",
	            field: "rtnstatus",
				formatter: function(cell, formatterParams, onRendered) {
					let cellData = cell.getData();
					let select = document.createElement("select");
					select.id = cellData.indexString;
					select.classList.add('rtnstatus');
					select.classList.add('rtnstatus'+cell.getRow().getPosition(true));
					let cellValue = cell.getValue();
					$.ajax({
					    url: 'getSTEPPrjCodeSettingsList.do',
					    type: 'POST',
					    data:{
					    	prj_id:selectPrjId,
					    	set_code_type:set_code_type
					    },
					    success: function onData (data) {
							let option = document.createElement("option");
							option.value = '';
							option.text = '';
							select.append(option);
					    	for(let i=0;i<data[0].length;i++){
								let option = document.createElement("option");
								option.value = data[0][i].set_code_id;
								option.text = data[0][i].set_code+': '+data[0][i].set_desc;
								select.append(option);
					    	}

							console.log(cellValue);
							//dateSet
							if(cellValue){
								$('.rtnstatus'+cell.getRow().getPosition(true)).val(cellValue);
							}
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});

					return select;
				},
    			width: 250
            },
	        {
	            title: "Discipline",
	            field: "discipline",
	            width: 78
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
	        }
	    ],
	});
	function setTrInSelectedRowContextMenu(row) {
		return trInContextMenu;
    }

	trInSelectedTable.on("rowDblClick",function(e,row){
		location.href=current_server_domain+'/downloadFile.do?prj_id='+encodeURIComponent(selectPrjId)+'&sfile_nm='+encodeURIComponent(row.getData().bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(row.getData().bean.doc_no.replaceAll("&","@%@")+'.'+row.getData().bean.file_type);
	});
	
	trIn.trInSelectedTable = trInSelectedTable;
}
function in_selectedDeleteDoc(){
	let del = trIn.trInSelectedTable.getSelectedRows();
	trIn.trInSelectedTable.deleteRow(del);
	if(trIn.trInSelectedTable.getData().length===0){
		$('#in_del_doc').css('display','none');
	}
}

function getRtnStatus(){
	let result = "";
	let rtn = $(".rtnstatus");
	for(let i=0;i<rtn.length;i++){
		let tmp = rtn[i]; 
		result += tmp.id + "&&"  + tmp.value + "@@"; 
	}
	result = result.slice(0,-2);
	return result;
}
function incomingSave(){
	let tr_no = $('#in_tr_no').val();
	let tr_subject = $('#in_tr_subject').val();
	let send_date = $('#in_received_date').val().replaceAll('-','');
	let tr_writer_id = $('#in_tr_writer_id').val();
	let tr_modifier_id = $('#in_tr_modifier_id').val();
	let tr_remark = $('#in_note').val();
	let tr_distribute = '';
	let rtnStatus = getRtnStatus();
	
	$('input[name="TRINDiscip"]:checked').each(function() {
		tr_distribute += $(this).val() +',';
	});
	tr_distribute = tr_distribute.slice(0,-1);

	let count = 0;
	let length = trIn.trInSelectedTable.getRows().length;
	let rtnstatusArray = $('.rtnstatus');
	for(let i=0;i<rtnstatusArray.length;i++){
		if(rtnstatusArray[i].value!=='' && rtnstatusArray[i].value!==undefined){
			count++;
		}
	}
	
	let docid_revid_revno = '';
	if(length===count){
		docid_revid_revno = getRtnStatus();
	}
	
	if(tr_subject==='' || tr_subject===null || tr_subject===undefined){
		alert('SUBJECT를 입력해주세요.');
	}else if(send_date==='' || send_date==='--' || send_date===null || send_date===undefined){
		alert('RECEIVED DATE를 입력해주세요.');
	}else if(length!==count){
		alert('Rtn Status를 입력해주세요.');
	}else{
		let form = $('#trIncomingForm')[0];
	    let formData = new FormData(form);
	    formData.set('newOrUpdate',trIn.newOrUpdate);
	    formData.set('prj_id',selectPrjId);
	    formData.set('tr_no',tr_no);
	    formData.set('tr_subject',tr_subject);
	    formData.set('send_date',send_date);
	    formData.set('tr_writer_id',tr_writer_id);
	    formData.set('tr_modifier_id',tr_modifier_id);
	    formData.set('tr_remark',tr_remark);
	    formData.set('tr_distribute',tr_distribute);
	    formData.set('docid_revid_revno',docid_revid_revno);
	    formData.set('rfile_nm',trIn.rfile_nm);
	    formData.set('file_type',trIn.file_type);
	    formData.set('file_size',trIn.file_size);
		let current_folder_doc_type = '';
		let set_code = '';
		if(inProcessType === 'TR'){
			current_folder_doc_type = 'DCI';
			set_code = 'TR INCOMING';
		}else if(inProcessType === 'VTR'){
			current_folder_doc_type = 'VDCI';
			set_code = 'VTR INCOMING';
		}else if(inProcessType === 'STR'){
			current_folder_doc_type = 'SDCI';
			set_code = 'STR INCOMING';
		}
	    formData.set('doc_table',current_folder_doc_type);
	    formData.set('processType',inProcessType);
	    formData.set('set_code_type','CORRESPONDENCE_IN');
	    formData.set('set_code',set_code);
	    
	    let file_value = $('#tr_incoming_filename').val();
	    formData.set('file_value',file_value);
	    if(file_value===''){
	   	 $.ajax({
				type: 'POST',
				url: 'incomingSave.do',         
		        data:{
		        	newOrUpdate:trIn.newOrUpdate,
		        	prj_id:selectPrjId,
		        	tr_no:tr_no,
		        	tr_subject:tr_subject,
		        	send_date:send_date,
		        	tr_writer_id:tr_writer_id,
		        	tr_modifier_id:tr_modifier_id,
		        	tr_remark:tr_remark,
		        	tr_distribute:tr_distribute,
		        	docid_revid_revno:docid_revid_revno,
		        	doc_table:current_folder_doc_type,
			    	processType:inProcessType,
			    	set_code_type:'CORRESPONDENCE_IN',
			    	set_code:set_code
		        },
				success: function(data) {
			    	if(data[0]!=='저장 가능'){
			    		alert(data[0]);
			    	}else{
				    	if(data[1]>0){
				    		alert('Save Successfully');
				    		getTrInSearch();
				    		selectTR(data[2]);
				    		trIn.newOrUpdate = data[2];
				    	}else{
				    		alert('save failed! 모든 도서를 저장하는것에 실패했습니다.');
				    	}
			    	}
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	    }else{
			 $.ajax({
					type: 'POST',
			        enctype: 'multipart/form-data',
					url: 'incomingSaveAttach.do',         
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
					    	if(data[0]!=='저장 가능'){
					    		alert(data[0]);
					    	}else{
						    	if(data[1]>0){
						    		alert('Save Successfully');
						    		getTrInSearch();
						    		selectTR(data[2]);
						    		trIn.newOrUpdate = data[2];
						    	}else{
						    		alert('save failed! 모든 도서를 저장하는것에 실패했습니다.');
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
}
function TRInfileMultiDownload(){
	var selectedArray = trIn.TrInMultiDownTable.getData();
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