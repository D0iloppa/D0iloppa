var table;
var selectPrjId = $('#selectPrjId').val();

$(document).ready(function(){
	getCollaboProgList();
	
	$("#todayProg").val(today());
	$("#olddayProg").val(today());
	
	$(".pop_collaboW").dialog({
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
	$(".pop_collaboW").remove();
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

$("#search_allProg").click(function(){
	
	$("#todayProg").val(today());
	$("#olddayProg").val(today());
});

$("#search_weekProg").click(function(){
	
	$("#todayProg").val(today());
	$("#olddayProg").val(lastWeek());
});

$("#search_monthProg").click(function(){
	
	$("#todayProg").val(today());
	$("#olddayProg").val(lastMonth());
});

function getCollaboProgList() {
	$.ajax({
		url: 'getCollaboProgList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var collaboList = [];
	    	for(i=0;i<data[0].length;i++){
	    		collaboList.push({
	    			drn_id: 'TPC2008-BB-20210228-'+data[0][i].drn_id,
	    			drn_title: data[0][i].drn_title,
	    			reg_date: getDateFormat(data[0][i].reg_date)
	    		});
	    	}
	    	setCollaboProgList(collaboList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function selectDayProg() {
	var oldDay = $("#olddayProg").val();
	var toDay = $("#todayProg").val();
	
	if(oldDay > toDay) {
		alert('검색 기간을 다시 확인해주세요.');
	}else if(oldDay == toDay) {
		getCollaboProgList();
	}else {
		$.ajax({
			url: 'selectCollaboProgList.do',
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
		    			drn_id: 'TPC2008-BB-20210228-'+data[0][i].drn_id,
		    			drn_title: data[0][i].drn_title,
		    			reg_date: getDateFormat(data[0][i].reg_date)
		    		});
		    	}
		    	setCollaboProgList(collaboList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}

function setCollaboProgList(collaboList) {
	table = new Tabulator("#collaboProgList", {
		selectable:1,//true
        data: collaboList,
        layout: "fitColumns",
        placeholder: "No Data Set",
        height:"100%",
        columns: [{
        		title: "DRN No",
        		field: "drn_id"
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
}



























