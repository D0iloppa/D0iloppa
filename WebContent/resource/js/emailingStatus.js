var emailing_loading = '<div id="emailing_loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';

var emailingStatusTable;
var emailingStatusDownTable;

var prj_grid_emailingStatus;

var select_prj_id_emailingStatus;

$(function(){
	$('.date').datepicker({dateFormat: 'yy-mm-dd'});
	
	var now = new Date();
	var yesterday = new Date(now.setDate(now.getDate() - 1));
	
	$('#select_eMailingStatus_fdate').datepicker('setDate', new Date());
	$('#select_eMailingStatus_ldate').datepicker('setDate', new Date());
	
//	if($("input:radio[name=eMailingStatusRadio]").checked) {
//		
//	}
	$("input:radio[name=eMailingStatusRadio]").click(function(){
		//console.log($("input:radio[name=logRadio]:checked").val());
		if($("input:radio[name=eMailingStatusRadio]:checked").val() == "all") {
			$('#select_eMailingStatus_fdate').datepicker('setDate', '');
			$('#select_eMailingStatus_ldate').datepicker('setDate', '');
		}else if($("input:radio[name=eMailingStatusRadio]:checked").val() == "oneWeek") {
			$('#select_eMailingStatus_fdate').datepicker('setDate', lastWeek());
			$('#select_eMailingStatus_ldate').datepicker('setDate', new Date());
		}else if($("input:radio[name=eMailingStatusRadio]:checked").val() == "oneMonth") {
			$('#select_eMailingStatus_fdate').datepicker('setDate', lastMonth());
			$('#select_eMailingStatus_ldate').datepicker('setDate', new Date());
		}
	});
	
	
	// 다이얼로그 생성
	$("#emailing_detailDialog").dialog({
        draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto",
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
	

	if($('#eMailingStatus_selectType').val() == 'sendCurrent') {		
		$('#searchEmailingStatus_toExcel').css("display","")		
	}else {
		$('#searchEmailingStatus_toExcel').css("display","nones");
	}
	
	initPrjSelectGridEmailingStatus();
	//getSystemLogList();
	getEmailSendList();
});

function initPrjSelectGridEmailingStatus() {
	prj_grid_emailingStatus  = new Tabulator("#prj_list_grid_emailingStatus", {
		layout: "fitColumns",
		placeholder:"There is No Data",
		selectable:1,
		index:"prj_id",
		height: 180,
		columns: [			
			{ title:"index",field:"prj_id",visible:false},
			{
				title: "Project No",
				field: "prj_no",
				headerSort:false,
			},
			{
				title: "Project Name",
				field: "prj_nm",
				headerSort:false,
			},
			{
				title: "Project Full Name",
				field: "prj_full_nm",
				headerSort:false,
			},
		]
	}); 
	
	prj_grid_emailingStatus.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_emailingStatus.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_emailingStatus.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#selectEmailingStatusPrjId").val(rowData.prj_id);
		$("#selected_prj_emailingStatus").html(rowData.prj_nm);
		$(".multi_sel_wrap").removeClass('on');
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prj_search_emailingStatus') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 prjSearchEmailingStatus();
			 }
		}
	});
	
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjSearchEmailingStatus(){
	let keyword = $("#prj_search_emailingStatus").val();
	let grid = prj_grid_emailingStatus;
	
	$.ajax({
	    url: 'prjSearch.do',
	    type: 'POST',
	    data:{
	    	keyword : keyword
	    },
	    success: function onData (data) {
	    	grid.setData(data.model.prjList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function emailingStatus_prjAllSelect(){
	$("#selectEmailingStatusPrjId").val('all');
	$("#selected_prj_emailingStatus").html("ALL PROJECT");
	$(".multi_sel_wrap").removeClass('on');
}


function searchEmailingStatus() {
	var emailingStatus_selectType = $('#eMailingStatus_selectType').val();
	select_prj_id_emailingStatus = $('#selectEmailingStatusPrjId').val();
	
	if(select_prj_id_emailingStatus === "select") {
		alert("프로젝트를 선택하고 조회 바랍니다.");
	}else {
		if(emailingStatus_selectType === 'sendCurrent') {
			getEmailSendList();
			$('#emailingSatusList').css('display','');
			$('#emailingDownList').css('display','none');	
			$('#searchEmailingStatus_toExcel').css("display","");
		}else if(emailingStatus_selectType === 'receiveCurrent') {
			getEmailReceiptList();
			$('#emailingSatusList').css('display','');
			$('#emailingDownList').css('display','none');
		}else if(emailingStatus_selectType === 'nReceiveCurrent') {
			getEmailNreceiptList();
			$('#emailingSatusList').css('display','');
			$('#emailingDownList').css('display','none');
		}else if(emailingStatus_selectType === 'downCurrent') {
			getEmailDownList();
			$('#emailingSatusList').css('display','none');
			$('#emailingDownList').css('display','');	
			$('#searchEmailingStatus_toExcel').css("display","none");
		}
	}
}

function getEmailSendList() {
	select_prj_id_emailingStatus = $('#selectEmailingStatusPrjId').val();
	
	let fromDate = $('#select_eMailingStatus_fdate').val();
	let toDate = $('#select_eMailingStatus_ldate').val();
	
	$.ajax({
	    url: 'getEmailSendList.do',
	    type: 'POST',
	    beforeSend:function(){
		    $('body').prepend(loading);
		 },
		complete:function(){
		    $('#loading').remove();
		 },
	    data:{
	    	prj_id:select_prj_id_emailingStatus,
	    	from:fromDate,
	    	to:toDate
	    },
	    success: function onData (data) {
	    	/*
	    	var dataList = [];
	    	var email_receiverd_date;
	    	let send_date;
	    	for(i=0;i<data[0].length;i++){
	    		var email_receiverd_dateChk = data[0][i].email_receiverd_date
	    		if(data[0][i].email_receiverd_date!=null && email_receiverd_dateChk.replace(' ','')!=''){
	    			email_receiverd_date = getDateFormat(data[0][i].email_receiverd_date);
	    		}else{
	    			email_receiverd_date = '';
	    		}
	    		if(data[0][i].tr_send_date!=null && data[0][i].tr_send_date!=''){
	    			send_date = getDateFormat_YYYYMMDD(data[0][i].tr_send_date);
	    		}else{
	    			send_date = '';
	    		}
	    		dataList.push({
		            email_feature: data[0][i].email_feature,
		            email_type: data[0][i].email_type,
		            operator :data[0][i].operator,
		            tr_no: data[0][i].user_data00,
		            send_date: send_date,
		            tr_subject: data[0][i].tr_subject,
		            email_send_date: getDateFormat(data[0][i].email_send_date),
		            email_received_date: email_receiverd_date,
		            mail_subject: data[0][i].subject,
		            email_sender_addr: data[0][i].email_sender_addr,
		            email_receiver_addr: data[0][i].email_receiver_addr,
		            email_refer_to: data[0][i].email_refer_to,
		            email_bcc: data[0][i].email_bcc
	    		});
	    	}
	    	*/
	    	setEmailSendList(data[0]);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getEmailReceiptList() {
	select_prj_id_emailingStatus = $('#selectEmailingStatusPrjId').val();
	
	let fromDate = $('#select_eMailingStatus_fdate').val();
	let toDate = $('#select_eMailingStatus_ldate').val();
	
	$.ajax({
	    url: 'getEmailReceiptList.do',
	    type: 'POST',
	    data:{
	    	prj_id:select_prj_id_emailingStatus,
	    	from:fromDate,
	    	to:toDate
	    },
	    success: function onData (data) {
	    	var dataList = [];
	    	var email_receiverd_date;
	    	var send_date;
	    	for(i=0;i<data[0].length;i++){
	    		var email_receiverd_dateChk = data[0][i].email_receiverd_date
	    		if(data[0][i].email_receiverd_date!=null && email_receiverd_dateChk.replace(' ','')!=''){
	    			email_receiverd_date = getDateFormat(data[0][i].email_receiverd_date);
	    		}else{
	    			email_receiverd_date = '';
	    		}
	    		if(data[0][i].tr_send_date!=null && data[0][i].tr_send_date!=''){
	    			send_date = getDateFormat_YYYYMMDD(data[0][i].tr_send_date);
	    		}else{
	    			send_date = '';
	    		}
	    		
	    		dataList.push({
	    			email_feature: data[0][i].email_feature,
		            email_type: data[0][i].email_type,
		            tr_no: data[0][i].user_data00,
		            send_date: send_date,
		            tr_subject: data[0][i].tr_subject,
		            email_send_date: getDateFormat(data[0][i].email_send_date),
		            email_received_date: email_receiverd_date,
		            mail_subject: data[0][i].subject,
		            email_sender_addr: data[0][i].email_sender_addr,
		            email_receiver_addr: data[0][i].email_receiver_addr,
		            email_refer_to: data[0][i].email_refer_to,
		            email_bcc: data[0][i].email_bcc
	    		});
	    	}
	    	setEmailSendList(dataList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getEmailNreceiptList() {
select_prj_id_emailingStatus = $('#selectEmailingStatusPrjId').val();
	
	let fromDate = $('#select_eMailingStatus_fdate').val();
	let toDate = $('#select_eMailingStatus_ldate').val();
	
	$.ajax({
	    url: 'getEmailNreceiptList.do',
	    type: 'POST',
	    data:{
	    	prj_id:select_prj_id_emailingStatus,
	    	from:fromDate,
	    	to:toDate
	    },
	    success: function onData (data) {
	    	var dataList = [];
	    	var email_receiverd_date;
	    	var send_date;
	    	for(i=0;i<data[0].length;i++){
	    		var email_receiverd_dateChk = data[0][i].email_receiverd_date
	    		if(data[0][i].email_receiverd_date!=null && email_receiverd_dateChk.replace(' ','')!=''){
	    			email_receiverd_date = getDateFormat(data[0][i].email_receiverd_date);
	    		}else{
	    			email_receiverd_date = '';
	    		}
	    		if(data[0][i].tr_send_date!=null && data[0][i].tr_send_date!=''){
	    			send_date = getDateFormat_YYYYMMDD(data[0][i].tr_send_date);
	    		}else{
	    			send_date = '';
	    		}
	    		dataList.push({
		            email_feature: data[0][i].email_feature,
		            email_type: data[0][i].email_type,
		            tr_no: data[0][i].user_data00,
		            send_date: getDateFormat_YYYYMMDD(data[0][i].tr_send_date),
		            tr_subject: data[0][i].tr_subject,
		            email_send_date: send_date,
		            email_received_date: email_receiverd_date,
		            mail_subject: data[0][i].subject,
		            email_sender_addr: data[0][i].email_sender_addr,
		            email_receiver_addr: data[0][i].email_receiver_addr,
		            email_refer_to: data[0][i].email_refer_to,
		            email_bcc: data[0][i].email_bcc
	    		});
	    	}
	    	setEmailSendList(dataList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getEmailDownList() {
	select_prj_id_emailingStatus = $('#selectEmailingStatusPrjId').val();

	let fromDate = $('#select_eMailingStatus_fdate').val();
	let toDate = $('#select_eMailingStatus_ldate').val();
	
	$.ajax({
	    url: 'getEmailDownList.do',
	    type: 'POST',
	    beforeSend:function(){
		    $('body').prepend(loading);
		 },
		complete:function(){
		    $('#loading').remove();
		 },
	    data:{
	    	prj_id:select_prj_id_emailingStatus,
	    	from:fromDate,
	    	to:toDate
	    },
	    success: function onData (data) {
	    	setEmailDownList(data[0])
	    	/*
	    	var dataList = [];
	    	var chkEsend;
	    	var chkDown;
	    	for(i=0;i<data[0].length;i++){
	    		if(data[0][i].email_send_date == null) {
	    			chkEsend = '';
	    		}else if(data[0][i].email_send_date != null) {
	    			chkEsend = getDateFormat(data[0][i].email_send_date);
	    		}
	    		if(data[0][i].down_date == null) {
	    			chkDown = '';
	    		}else if(data[0][i].down_date != null) {
	    			chkDown = getDateFormat(data[0][i].down_date);
	    		}
	    		dataList.push({
		            email_type: data[0][i].email_type,
		            tr_no: data[0][i].tr_no,
		            mail_subject: data[0][i].subject,
		            mail_send_date: chkEsend,
		            doc_no: data[0][i].doc_no,
		            rev_no: data[0][i].rev_no,
		            download_date: chkDown,
		            ip: data[0][i].down_ip
	    		});
	    	}
	    	if(emailingStatusDownTable == null || emailingStatusDownTable == undefined) {
		    	setEmailDownList(dataList);
	    	}else {
	    		emailingStatusDownTable.replaceData(dataList);
	    	}
	    	*/
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function setEmailSendList(dataList) {
	emailingStatusTable = new Tabulator("#emailingSatusList", {
        selectable:1,//true
        data: dataList,
        layout: "fitColumns",
        placeholder:"No Data Set",
        height:450,
        columns: [
        	{
                title: "Project",
                field: "prj_nm",
                hozAlign: "left",
                width: "80"
            },
        	{
                title: "FEATURE",
                field: "email_feature",
                hozAlign: "left",
                width: "80"
            },
            {
                title: "TYPE",
                field: "email_type",
                hozAlign: "left",
                width: "100"
            },
            {
                title: "OPERATOR",
                field: "operator",
                hozAlign: "left",
                width: "100"
            },
            {
                title: "MAIL SUBJECT",
                field: "subject",
                hozAlign: "left",
                width: "138"
            },
            {
                title: "DOC NO/TR NO/CORR NO",
                field: "user_data00",
                hozAlign: "left",
                width: "120"
            },
            {
                title: "TR SEND DATE",
                field: "tr_send_date",
                hozAlign: "left",
                formatter:function(cell, formatterParams, onRendered){
                    let cellVal = cell.getValue();
                    if(!cellVal) return cellVal;
                    else if(cellVal.length == 8){
                    	return cellVal.substr(0,4)+'-'+cellVal.substr(4,2)+'-'+cellVal.substr(6,2);
                    }else return cellVal;
                    
                },
                width: "105"
            },
            {
                title: "TR SUBJECT",
                field: "tr_subject",
                hozAlign: "left",
                width: "120"
            },
            {
                title: "MAIL SEND DATE",
                field: "email_send_date",
                hozAlign: "left",
                formatter:function(cell, formatterParams, onRendered){
                    let cellVal = cell.getValue();
                    if(!cellVal) return cellVal;
                    else if(cellVal.length<8) return cellVal;
                    return getDateFormat(cellVal)
                },
                width: "120"
            },
            {
                title: "MAIL RECEIVED DATE",
                field: "email_receiverd_date",
                hozAlign: "left",
                width: "138",
                formatter:function(cell, formatterParams, onRendered){
                    let cellVal = cell.getValue();
                    if(!cellVal) return cellVal;
                    else if(cellVal.length<8) return cellVal;
                    return getDateFormat(cellVal)
                },
            },
            {
                title: "MAIL SENDER",
                field: "email_sender_addr",
                hozAlign: "left",
                width: "138"
            },
            {
                title: "MAIL RECEIVER",
                field: "email_receiver_addr",
                hozAlign: "left",
                width: "138"
            },
            {
                title: "MAIL REFER",
                field: "email_refer_to",
                hozAlign: "left",
                width: "138"
            },
            {
                title: "MAIL BCC",
                field: "email_bcc",
                hozAlign: "left",
                width: "138"
            }
        ]
    });
	
	
	
	
	
	
	emailingStatusTable.on("rowClick",function(e,row){
		showDetailEmail(row.getData());
		$("#emailing_detailDialog").dialog("open");
	});
	
	
	
}

function setEmailDownList(dataList) {
	emailingStatusDownTable = new Tabulator("#emailingDownList", {
        selectable:1,//true
        data: dataList,
        layout: "fitColumns",
        placeholder:"No Data Set",
        //height:"100%",
        height:432,
        columns: [
        	{
                title: "PROJECT",
                field: "prj_nm",
                hozAlign: "left"
            },
        	{
                title: "TYPE",
                field: "email_type",
                hozAlign: "left"
            },
            {
                title: "DOC NO/TR NO/CORR NO",
                field: "tr_no",
                hozAlign: "left"
            },
            {
                title: "MAIL SUBJECT",
                field: "subject",
                hozAlign: "left"
            },
            {
                title: "MAIL SEND DATE",
                field: "email_send_date",
                hozAlign: "left",
                formatter:function(cell, formatterParams, onRendered){
                    let cellVal = cell.getValue();
                    if(!cellVal) return cellVal;
                    else if(cellVal.length<8) return cellVal;
                    return getDateFormat(cellVal)
                }
            },
            {
                title: "DOC NO",
                field: "doc_no",
                hozAlign: "left"
            },
            {
                title: "REV NO",
                field: "rev_no",
                hozAlign: "left"
            },
            {
                title: "DOWNLOAD DATE",
                field: "down_date",
                hozAlign: "left",
                formatter:function(cell, formatterParams, onRendered){
                    let cellVal = cell.getValue();
                    if(!cellVal) return cellVal;
                    else if(cellVal.length<8) return cellVal;
                    return getDateFormat(cellVal)
                }
            },
            {
                title: "IP",
                field: "down_ip",
                hozAlign: "left"
            }
        ]
    });
}


function showDetailEmail(data) {
	console.log(data);
	// 정보를 셋팅하기전에 기존 정보 클리어
	clear_detailPage();
	
	$("#emailing_pop_subject").val(data.subject);
	$("#emailing_pop_operator").val(data.operator);
	$("#emailing_pop_receiver").val(data.email_receiver_addr);
	$("#emailing_pop_sendDate").val(getDateFormat(data.email_send_date));
	$("#emailing_pop_contents").html(data.contents);
	
}


function clear_detailPage(){
	$("#emailing_pop_subject").val("");
	$("#emailing_pop_operator").val("");
	$("#emailing_pop_receiver").val("");
	$("#emailing_pop_sendDate").val("");
	$("#emailing_pop_contents").html("");
}

function closeDetailEmail(){
	$("#emailing_detailDialog").dialog("close");
}

function searchEmailingStatus_toExcelBtn() {
	let gridData = emailingStatusTable.getData();
	
	let jsonList = [];
	
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}

	let fileName = 'E-MAILING STATUS(발송현황)';
	
	var f = document.searchEmailingStatusExcelUploadForm;

	let searchEmailingStatus_form = $('#select_eMailingStatus_fdate').val();
	let searchEmailingStatus_to = $('#select_eMailingStatus_ldate').val();
	let searchEmailingStatus_prj_nm = $('#selected_prj_emailingStatus').html();
	
	select_prj_id_emailingStatus = $('#selectEmailingStatusPrjId').val();
	
	f.searchEmailingStatus_fileName.value = fileName;
	f.searchEmailingStatus_jsonRowDatas.value = jsonList;
	f.searchEmailingStatus_form_date.value = searchEmailingStatus_form;
	f.searchEmailingStatus_to_date.value = searchEmailingStatus_to;
	f.searchEmailingStatus_prj_nm.value = searchEmailingStatus_prj_nm;
	f.searchEmailingStatus_prj_id.value = select_prj_id_emailingStatus;
	f.action = "searchEmailingStatusExcelDownload.do";
	f.submit();
}











