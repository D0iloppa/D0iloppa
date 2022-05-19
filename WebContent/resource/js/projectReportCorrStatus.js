var c_columnJson;
var c_valueJson;
var c_column;
var c_value;
var c_reportVo;

$( document ).ready(function() {
	$('.date').datepicker({
		dateFormat: 'yy-mm-dd',
		language: 'kr',
		autoclose: true,
		todayHighlight: true
	});
	$('#documnet_dateS').datepicker('setDate', lastWeek());
	$('#documnet_dateD').datepicker('setDate', new Date());
});

function downloadReportCorr() {
	
	if(c_value == undefined) {
		alert("검색 후 다운로드 해주세요.");
		return;
	}else if(c_value == "") {
		alert("다운로드할 데이터가 없습니다.");
		return;
	}
	
	var bMakeShee=true;
	var fileName="";
	var title="";
	var sheetName="";
	var column;
	var value;
	var projecetName=getPrjInfo().full_nm;
	
	var date_s = $("#documnet_dateS").val();
	var date_d = $("#documnet_dateD").val();
	
	
	fileDownloadExcel("./corrStatusExcelDownload.do",{
	    httpMethod: "POST",
	    traditional : true, //필수
	    data:{
	    	bMakeShee:bMakeShee,
	    	fileName:fileName,
	    	title:title,
	    	sheetName:sheetName,
	    	columnList:c_column,
	    	valueList:c_value,
	    	projecetName:projecetName,
	    	reportType:"1",
	    	date_s:date_s,
	    	date_d:date_d
	    },
	    successCallback: function (url) {
	    },
	    failCallback: function(responesHtml, url) {
	    }
	});
}

function RetrieveCorr() {
	$("#data_table_corres").css("display", "none");
	var reportType="5_1";
	var reportTitle="";
	var date_s = $("#documnet_dateS").val();
	var date_d = $("#documnet_dateD").val();
	var chkInOut = $("input:radio[name=corrInOutRadio]:checked").val();
	date_s = date_s.replace(/-/gi, "");
	date_d = date_d.replace(/-/gi, "");
	
	reportTitle="Correspondence Status";
	$('.reportTitleC').text(reportTitle);
	
	$.ajax({
	    url: './corrStatusGetReportData.do',
	    type: 'POST',
	    beforeSend:function(){
		    $('body').prepend(loading);
		},
		complete:function(){
		    $('#loading').remove();
		},
	    data:{
	    	reportType : reportType,
	    	date_s:date_s,
	    	date_d:date_d,
	    	in_out: chkInOut
	    },
	    success: function onData (data) {
	    	c_column=data[0];
	    	c_value=data[1];
	    	c_columnJson=data[2];
	    	c_valueJson=data[3];
	    	c_reportVo=data[4];
	    	size=data[5];

	    	$('#reportCntListC').html('건수 : ' + size + '개');
	    	
	    	if ( size == 0 ) {
	    		alert("Data is Empty !!");
	    		return;
	    	}

	    	$("#data_table_corres").css("display", "block");
	    	drawDataCorr(c_columnJson, c_valueJson);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function clickReportRadioType(typeT) {
	type=typeT;
}

//function drawDataCorr(column, value) {
//	for ( var i=0; i<column.length; ++i ){
//		var obj = column[i];
//		obj.formatter = customFormatter;
//		obj.variableHeight = true;
//	}
//	
//	var table = new Tabulator(".corrStatus", {
//        data: value,
//        layout: "fitColumns",
//        columns: column,
//    });
//}

function drawDataCorr(column, value) {
	for ( var i=0; i<column.length; ++i ){
		var obj = column[i];
		
		var objTmp = obj.columns;
		if ( objTmp != undefined ) {
			for ( var j=0; j<objTmp.length; ++j ){
				obj = objTmp[j];
				
				var objTTmp = obj.columns;
				
				if ( objTTmp != undefined ) {
					for ( var k=0; k<objTTmp.length; ++k ){
						obj = objTTmp[k];
						obj.formatter = customFormatter;
						obj.variableHeight = true;
					}
				} else {
					obj.formatter = customFormatter;
					obj.variableHeight = true;
				}
			}
		} else {
			obj.formatter = customFormatter;
			obj.variableHeight = true;
		}
		
		
	}
	
	var table = new Tabulator(".corrStatus", {
        data: value,
        layout: "fitColumns",
        columns: column,
        placeholder: "No Data Set",
//		pagination : true,
//		paginationSize : 100,
        height : 600,
    });
}

function customFormatter(cell, formatterParams, onRendered) {
	const val = cell.getValue();
	const cellDiv = document.createElement('div');
	
	if ( val == null || val.undefined ) {
		return cellDiv;
	}
	for (let i = 0; i < val.length; i++){
		const valItemDiv = document.createElement('div');
		valItemDiv.textContent = val[i];
		if ( i < val.length-1 ) {
			valItemDiv.style.borderBottom = 'solid 1px #e1e1e1';
		}
		
		cellDiv.appendChild(valItemDiv);
	}
	return cellDiv;
}

// 탭 닫을시
function removeTabPrjReportCorr(){
	
}