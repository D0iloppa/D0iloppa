var table;
var selectPrjId = $('#selectPrjId').val();
var clickTb;
var drn_id;
var drn_no;
var drn_title;
var reg_date;
var status;
var doc_title;

$(document).ready(function(){
	getCollaboPrevList();
	
	$("#todayPrev").val(today());
	$("#olddayPrev").val(today());
	
	$(".pop_collaboPre").dialog({
		draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto"
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
});

function removeTabCOL(){
	$(".pop_collaboPre").remove();
}

//날짜 객체를 받아서 문자열로 리턴
function getDateStr(myDate) {
	return (myDate.getFullYear() + '-' + (myDate.getMonth() + 1) + '-' + myDate.getDate())
}

/* 오늘 날짜를 문자열로 반환 */
function today() {
  var d = new Date();
  return getDateStr(d);
}

/* 어제 날짜 반환*/
function yester() {
	var d = new Date();
	var dayOfMonth = d.getDate();
	d.setDate(dayOfMonth - 1);
	return getDateStr(d);
}

/* 오늘로부터 1주일전 날짜 반환 */
function lastWeek() {
  var d = new Date();
  var dayOfMonth = d.getDate();
  d.setDate(dayOfMonth - 7);
  return getDateStr(d);
}

/* 오늘로부터 1달전 날짜 반환 */
function lastMonth() {
	  var d = new Date();
	  var monthOfYear = d.getMonth();
	  d.setMonth(monthOfYear - 1);
	  return getDateStr(d);
}

$("#search_allPrev").click(function(){
	
	$("#todayPrev").val(today());
	$("#olddayPrev").val(today());
});

$("#search_weekPrev").click(function(){
	
	$("#todayPrev").val(today());
	$("#olddayPrev").val(lastWeek());
});

$("#search_monthPrev").click(function(){
	
	$("#todayPrev").val(today());
	$("#olddayPrev").val(lastMonth());
});

function getCollaboPrevList() {
	
	$.ajax({
		url: 'getCollaboPrevList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var collaboList = [];
	    	for(i=0;i<data[0].length;i++){
	    		collaboList.push({
	    			drn_id: data[0][i].drn_id,
	    			drn_no: data[0][i].drn_no,
	    			drn_title: data[0][i].drn_title,
	    			reg_date: getDateFormat(data[0][i].reg_date),
	    			status: data[0][i].status,
	    			doc_title: data[0][i].doc_title
	    		});
	    	}
	    	setCollaboPrevList(collaboList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

//검색버튼 클릭시
function selectDayPrev() {
	var oldDay = $("#olddayPrev").val();
	var toDay = $("#todayPrev").val();
	
	if(oldDay > toDay) {
		alert('검색 기간을 다시 확인해주세요.');
	}else if(oldDay == toDay) {
		getCollaboPrevList();
	}else {
		$.ajax({
			url: 'selectCollaboPrevList.do',
		    type: 'POST',
		    data: {
		    	old_day: oldDay,
		    	to_day: toDay,
		    	prj_id: selectPrjId
		    },
		    success: function onData (data) {
		    	var collaboList = [];
		    	for(i=0;i<data[0].length;i++){
		    		collaboList.push({
		    			drn_id: data[0][i].drn_id,
		    			drn_no: data[0][i].drn_no,
		    			drn_title: data[0][i].drn_title,
		    			reg_date: getDateFormat(data[0][i].reg_date),
		    			status: data[0][i].status,
		    			doc_title: data[0][i].doc_title
		    		});
		    	}
		    	setCollaboPrevList(collaboList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}

function setCollaboPrevList(collaboList) {
	table = new Tabulator("#collaboPrevList", {
		selectable:1,//true
        data: collaboList,
        layout: "fitColumns",
        placeholder: "No Data Set",
        height:"100%",
        columns: [{
        		title: "DRN No",
        		field: "drn_no"
        	},
        	{
        		title: "DRN Title",
        		field: "drn_title"
        	},
        	{
        		title: "기안일",
        		field: "reg_date"
        	}
        ]
	});
	table.on("rowDblClick", function(e, row){
		console.log(row.getData());
		$(".pop_collaboPre").dialog("open");
		
		clickTb = row.getData();
		
		drn_id = clickTb.drn_id;
		drn_no = clickTb.drn_no;
		drn_title = clickTb.drn_title;
		reg_date = clickTb.reg_date;
		status = clickTb.reg_date;
		doc_title = clickTb.reg_date;
		
		// 팝업창에서 탭 연결로 바꾸게 될 때 사용
//		var writeDetailTab = {"id":"writeDetailTab","label":"작성중 DRN 상세페이지","jsp":"/page/cWritingDetail"};
//		addTab(writeDetailTab.jsp , writeDetailTab.label , writeDetailTab.id);
		
		$.ajax({
			url: 'getCollaboPrev.do',
			data:{
				prj_id: selectPrjId,
				drn_id: drn_id
			},
			success: function onData (data) {
				$('#DRN_title_previ').val(data[0].drn_title);
				$('#DRN_no_previ').val(data[0].drn_no);
//				$('#Project_no_previ').val(data[0].drn_no);
//				$('#Project_name_previ').val(data[0].drn_no);
				$('#Date_previ').val(getDateFormat(data[0].reg_date));
//				$('#Doc_no_previ').val(data[0].reg_date);
				$('#Status_previ').val(data[0].status);
				$('#Doc_title_previ').val(data[0].doc_title);
			},
			error: function onError (error) {
		        console.error(error);
		    }
		});
	});
}



































