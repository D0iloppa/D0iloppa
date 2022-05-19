var table;
var selectPrjId = $('#selectPrjId').val();

$(document).ready(function(){
	getCollaboList();
	
	$("#today").val(today());
	$("#oldday").val(today());
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
//function yester() {
//	var d = new Date();
//	var dayOfMonth = d.getDate();
//	d.setDate(dayOfMonth - 1);
//	return getDateStr(d);
//}

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

$("#search_all").click(function(){
	
	$("#today").val(today());
	$("#oldday").val(today());
});

$("#search_week").click(function(){
	
	$("#today").val(today());
	$("#oldday").val(lastWeek());
});

$("#search_month").click(function(){
	
	$("#today").val(today());
	$("#oldday").val(lastMonth());
});

function getCollaboList() {
	$.ajax({
		url: 'getCollaboList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var collaboList = [];
	    	for(i=0;i<data[0].length;i++){
	    		collaboList.push({
	    			dox_kind: data[0][i].dox_kind,
	    			drn_title: data[0][i].drn_title,
	    			reg_date: getDateFormat(data[0][i].reg_date)
	    		});
	    	}
	    	setCollaboList(collaboList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function selectDay() {
	var oldDay = $("#oldday").val();
	var toDay = $("#today").val();
	
//	var all_radio = $("#search_all").click().val();
//	console.log(all_radio);
	
	if(oldDay > toDay) {
		alert('검색 기간을 다시 확인해주세요.');
	}else if(oldDay == toDay) {
		getCollaboList();
	}else{
		
		$.ajax({
			url: 'selectCollaboList.do',
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
		    			dox_kind: data[0][i].dox_kind,
		    			drn_title: data[0][i].drn_title,
		    			reg_date: getDateFormat(data[0][i].reg_date)
		    		});
		    	}
		    	setCollaboList(collaboList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
	}
}

function setCollaboList(collaboList) {
	table = new Tabulator("#collaboDreftList", {
		selectable:1,//true
        data: collaboList,
        layout: "fitColumns",
        placeholder: "No Data Set",
        height:"100%",
        columns: [{
        		title: "종류",
        		field: "dox_kind"
        	},
        	{
        		title: "Title",
        		field: "drn_title"
        	},
        	{
        		title: "등록일",
        		field: "reg_date"
        	}
        ]
	});
}

function drnClick() {
	alert('Document Control에서 생성 바랍니다.');
}






























