var systemLogTable;
var documentLogTable;

var prj_grid_securityLog;

var securityCnt;

$(function(){
	$('.date').datepicker({dateFormat: 'yy-mm-dd'});
	
	var now = new Date();
	var yesterday = new Date(now.setDate(now.getDate() - 1));
	
	$('#select_log_fdate').datepicker('setDate', yesterday);
	$('#select_log_ldate').datepicker('setDate', new Date());
	
	$("input:radio[name=logRadio]").click(function(){
		//console.log($("input:radio[name=logRadio]:checked").val());
		if($("input:radio[name=logRadio]:checked").val() == "systemLog") {
			$("#securityLog_systemSelectType").css("display","inline-block");
			$("#securityLog_documentSelectType").css("display","none");
		}else if($("input:radio[name=logRadio]:checked").val() == "documentsLog") {
			$("#securityLog_systemSelectType").css("display","none");
			$("#securityLog_documentSelectType").css("display","inline-block");
		}
	});
	initPrjSelectGridSequrityLog();
	//getSystemLogList();
	securityLogSearch();
});

function initPrjSelectGridSequrityLog(){
	prj_grid_securityLog  = new Tabulator("#prj_list_grid_securityLog", {
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
	
	prj_grid_securityLog.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_securityLog.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_securityLog.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#selectSecurityLogPrjId").val(rowData.prj_id);
		$("#selected_prj_securityLog").html(rowData.prj_nm);
		$(".multi_sel_wrap").removeClass('on');
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prj_search_securityLog') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 prjSearchSecurityLog();
			 }
		}
		
		if(document.getElementById('logSelect') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 securityLogSearch();
			 }
		}
	});
	
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjSearchSecurityLog(){
	let keyword = $("#prj_search_securityLog").val();
	let grid = prj_grid_securityLog;
	
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

function securityLog_prjAllSelect(){
	$("#selectSecurityLogPrjId").val('all');
	$("#selected_prj_securityLog").html("ALL PROJECT");
	$(".multi_sel_wrap").removeClass('on');
}

function securityLogSearch() {
	
	if($("input:radio[name=logRadio]:checked").val() == "systemLog") {
		$("#securitySysLogList").css("display","inline-block");
		$("#securityDocLogList").css("display","none");
		let sysLogPrjType = $("#selectSecurityLogPrjId").val();
		let sysfromDate = $("#select_log_fdate").val();
		let systoDate = $("#select_log_ldate").val();
		let logSysSelectType = $("#securityLog_systemSelectType").val();
		let logSysSelectText = $("#logSelect").val();
		
		securityCnt = 0;
		$.ajax({
			url: 'searchSystemLogList.do',
		    type: 'POST',
		    data:{
		    	from: sysfromDate,
		    	to: systoDate,
		    	searchPrjLogAtc: sysLogPrjType,
		    	searchLogAtc: logSysSelectType,
		    	textLogAtc: logSysSelectText
		    },
		    success: function onData (data) {
		    	var dataList = [];
		    	for(i=0;i<data[0].length;i++){
		    		securityCnt = i + 1;
		    		dataList.push({
		    			prj_id: data[0][i].prj_id,
		    			reg_date: getDateFormat(data[0][i].reg_date),
		    			pgm_nm: data[0][i].pgm_nm,
		    			method: data[0][i].method,
		    			reg_id: data[0][i].reg_id,
		    			reg_nm: data[0][i].reg_nm,
		    			remote_ip: data[0][i].remote_ip
		    		});
		    	}
		    	// 가져온 데이터 리스트를 set에 넘겨줌
		    	setSystemLogList(dataList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		
		//getSystemLogList();
	}else if($("input:radio[name=logRadio]:checked").val() == "documentsLog") {
		$("#securitySysLogList").css("display","none");
		$("#securityDocLogList").css("display","inline-block");
		//getDocumentLogList();
		let logPrjType = $("#selectSecurityLogPrjId").val();
		let fromDate = $("#select_log_fdate").val();
		let toDate = $("#select_log_ldate").val();		
		var logDocSelectType = $("#securityLog_documentSelectType").val();
		var logDocSelectText = $("#logSelect").val();
		
		$.ajax({
			url: 'getDocumentLogList.do',
		    type: 'POST',
		    data:{
		    	from: fromDate,
		    	to: toDate,
		    	searchPrjLogAtc: logPrjType,
		    	searchLogAtc: logDocSelectType,
		    	textLogAtc: logDocSelectText
		    },
		    success: function onData (data) {
		    	var dataList = [];
		    	for(i=0;i<data[0].length;i++){
		    		securityCnt = i + 1;
		    		dataList.push({
		    			prj_id: data[0][i].prj_id,
		    			doc_id: data[0][i].doc_id,
		    			rev_id: data[0][i].rev_id,
		    			reg_date: getDateFormat(data[0][i].reg_date),
		    			prj_no: data[0][i].prj_no,
		    			prj_nm: data[0][i].prj_nm,
		    			doc_no: data[0][i].doc_no,
		    			doc_title: data[0][i].doc_title,
		    			folder_id: data[0][i].folder_id,
		    			folder_path: data[0][i].folder_path,
		    			file_size: (data[0][i].file_size / 1024 / 1024).toFixed(2) + " MB",
		    			reg_id: data[0][i].reg_id,
		    			reg_nm: data[0][i].reg_nm,
		    			remote_ip: data[0][i].remote_ip,
		    			method: data[0][i].method
		    		});
		    	}
		    	// 가져온 데이터 리스트를 set에 넘겨줌
		    	setDocumentLogList(dataList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}

//function getSystemLogList() {
//	$.ajax({
//		url: 'getSystemLogList.do',
//	    type: 'POST',
//	    success: function onData (data) {
//	    	var dataList = [];
//	    	for(i=0;i<data[0].length;i++){
//	    		dataList.push({
//	    			prj_id: data[0][i].prj_id,
//	    			reg_date: getDateFormat(data[0][i].reg_date),
//	    			pgm_nm: data[0][i].pgm_nm,
//	    			method: data[0][i].method,
//	    			reg_id: data[0][i].reg_id,
//	    			reg_nm: data[0][i].reg_nm,
//	    			remote_ip: data[0][i].remote_ip
//	    		});
//	    	}
//	    	// 가져온 데이터 리스트를 set에 넘겨줌
//	    	setSystemLogList(dataList);
//	    },
//	    error: function onError (error) {
//	        console.error(error);
//	    }
//	});
//}

function setSystemLogList(dataList) {
	$('#securityLogCntList').html('건수 : ' + securityCnt + '개');
	systemLogTable = new Tabulator("#securitySysLogList", {
		selectable:1,//true
	    data: dataList,
	    layout: "fitColumns",
	    placeholder:"No Data Set",
		pagination : true,
		paginationSize : 50,
		Height : "100%",
	      columns: [{
	              title: "DATE",
	              field: "reg_date",
	              width: 130
	          },
	          {
	              title: "PGM NAME",
	              field: "pgm_nm",
	              hozAlign: "left"
	          },
	          {
	              title: "METHOD",
	              field: "method",
	              width: 88,
	              hozAlign: "left"
	          },
	          {
	              title: "USER ID",
	              field: "reg_id",
	              hozAlign: "left",
	              width: 140
	          },
	          {
	              title: "USER NAME",
	              field: "reg_nm",
	              width: 110
	          },
	          {
	              title: "IP",
	              field: "remote_ip",
	              width: 140
	          }
	          
	      ]
	});
}

function setDocumentLogList(dataList) {
	$('#securityLogCntList').html('건수 : ' + securityCnt + '개');
	documentLogTable = new Tabulator("#securityDocLogList", {
		selectable:1,//true
	    data: dataList,
	    layout: "fitColumns",
	    placeholder:"No Data Set",
		pagination : true,
		paginationSize : 50,
		Height : "100%",
	      columns: [{
	              title: "DATE",
	              field: "reg_date",
	              width: 115
	          },
	          {
	              title: "PROJECT NO",
	              field: "prj_no",
	              hozAlign: "left",
	              width: 96
	          },
	          {
	              title: "PROJECT NAME",
	              field: "prj_nm",
	              hozAlign: "left",
	              width: 110
	          },
	          {
	              title: "METHOD",
	              field: "method",
	              hozAlign: "left",
	              width: 110
	          },
	          {
	              title: "DOC. NO",
	              field: "doc_no",
	              hozAlign: "left",
	              width: 110
	          },
	          {
	              title: "DOC. TITLE",
	              field: "doc_title",
	              hozAlign: "left",
	              width: 200
	          },
	          {
	              title: "PATH",
	              field: "folder_path",
	              hozAlign: "left",
	              minWidth: 320
	          },
	          {
	              title: "SIZE(M)",
	              field: "file_size",
	              hozAlign: "right",
	              width: 70
	          },
	          {
	              title: "USER ID",
	              field: "reg_id",
	              hozAlign: "left",
	              width: 90
	          },
	          {
	              title: "USER NAME",
	              field: "reg_nm",
	              width: 92
	          },
	          {
	              title: "IP",
	              field: "remote_ip",
	              width: 100
	          }
	          
	      ]
	});
}

function seculityLog_fromExcelBtn() {
	if($("input:radio[name=logRadio]:checked").val() == "systemLog") {		
		let gridData = systemLogTable.getData();
		
		let seculityLog_form = $('#select_log_fdate').val();
		let seculityLog_to = $('#select_log_ldate').val();
		let seculityLog_prj_nm = $('#selected_prj_securityLog').html();
		let seculityLog_prj_id = $("#selectSecurityLogPrjId").val();
		
		let jsonList = [];
		
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = '보안로그(SYSTEM_LOG)';
		
		var f = document.securityLogExcelUploadForm;
		
		f.securityLog_fileName.value = fileName;
		f.securityLog_jsonRowDatas.value = jsonList;
		f.logGubun.value = $("input:radio[name=logRadio]:checked").val();
		f.securityLog_form_date.value = seculityLog_form;
		f.securityLog_to_date.value = seculityLog_to;
		f.seculityLog_prj_nm.value = seculityLog_prj_nm;
		f.securityLog_searchLogAtc.value = $("#securityLog_systemSelectType").val();
		f.securityLog_searchPrjLogAtc.value = seculityLog_prj_id
		f.securityLog_textLogAtc.value = $("#logSelect").val();
		f.action = "seculityLogSysExcelDownload.do";
		f.submit();
		
	}else if($("input:radio[name=logRadio]:checked").val() == "documentsLog") {
		let gridData = documentLogTable.getData();
		
		let seculityLog_form = $('#select_log_fdate').val();
		let seculityLog_to = $('#select_log_ldate').val();
		let seculityLog_prj_nm = $('#selected_prj_securityLog').html();
		let seculityLog_prj_id = $("#selectSecurityLogPrjId").val();
		
		let jsonList = [];
		
		for(let i=0; i<gridData.length; i++) {
			jsonList.push(JSON.stringify(gridData[i]));
		}
		
		let fileName = '보안로그(DOCUMENTS_LOG)';
		
		var f = document.securityLogExcelUploadForm;
		
		f.securityLog_fileName.value = fileName;
		f.securityLog_jsonRowDatas.value = jsonList;
		f.logGubun.value = $("input:radio[name=logRadio]:checked").val();
		f.securityLog_form_date.value = seculityLog_form;
		f.securityLog_to_date.value = seculityLog_to;
		f.seculityLog_prj_nm.value = seculityLog_prj_nm;
		f.securityLog_searchLogAtc.value = $("#securityLog_systemSelectType").val();
		f.securityLog_searchPrjLogAtc.value = seculityLog_prj_id
		f.securityLog_textLogAtc.value = $("#logSelect").val();
		f.action = "seculityLogDocExcelDownload.do";
		f.submit();
	}
}