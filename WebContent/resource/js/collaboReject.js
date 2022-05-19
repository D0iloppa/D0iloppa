var table;
var selectPrjId = $('#selectPrjId').val();

$(document).ready(function(){
	getCollaboRejectList();
	
	$("#todayReject").val(today());
	$("#olddayReject").val(today());
});

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

$("#search_allReject").click(function(){
	
	$("#todayReject").val(today());
	$("#olddayReject").val(today());
});

$("#search_weekReject").click(function(){
	
	$("#todayReject").val(today());
	$("#olddayReject").val(lastWeek());
});

$("#search_monthReject").click(function(){
	
	$("#todayReject").val(today());
	$("#olddayReject").val(lastMonth());
});

function getCollaboRejectList() {
	$.ajax({
		url: 'getCollaboRejectList.do',
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
	    	setCollaboRejectList(collaboList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function selectDayReject() {
	var oldDay = $("#olddayReject").val();
	var toDay = $("#todayReject").val();
	
	if(oldDay > toDay) {
		alert('검색 기간을 다시 확인해주세요.');
	}else if(oldDay == toDay) {
		
		getCollaboRejectList();
	}else {
		$.ajax({
			url: 'selectCollaboRejectList.do',
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
		    	setCollaboRejectList(collaboList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}

function setCollaboRejectList(collaboList) {
	table = new Tabulator("#collaboRejectList", {
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








































