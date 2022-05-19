var ven_columnJson;
var ven_valueJson;
var ven_column;
var ven_value;
var ven_reportVo;

var selectPrjId;

function getDisciplineVen() {
	$.ajax({
	    url: 'pg07_getDiscp.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var dataTmp = data[0];

	    	var optionT = $("<option value=\"0\">전체</option>");
            $('#v_discipline').append(optionT);
	    	
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].discip_display;
	    		var value = dataTmp[i].discip_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#v_discipline').append(option);
                
	    		var option2 = $("<option value=\""+text+"\">"+value+"</option>");
                $('#v_discipline_code_id').append(option2);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getWorkStepVen() {
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type:'VENDOR STEP'
	    },
	    success: function onData (data) {
	    	var dataTmp = data[0];
	    	
	    	var optionT = $("<option value=\"0\">전체</option>");
            $('#v_workStep').append(optionT);
	    	
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].set_code;
	    		var value = dataTmp[i].set_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#v_workStep').append(option);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

$( document ).ready(function() {
	
	$(".pop_detailVDCIList").dialog({
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
	
	$('.date').datepicker({
		dateFormat: 'yy-mm-dd',
		language: 'kr',
		autoclose: true,
		todayHighlight: true
	});
	
	$('#v_cutOffDate').datepicker('setDate', new Date());
	$('#v_durationS').datepicker('setDate', lastWeek());
	$('#v_durationD').datepicker('setDate', new Date());
	
	$("#v_cutOffDate").prop("readonly", true);
	$("#v_durationS").prop("readonly", true);
	$("#v_durationD").prop("readonly", true);
	
	selectPrjId = $('#selectPrjId').val();
	
	getDisciplineVen();
	getWorkStepVen();
	initClearVen();
});

function initClearVen() {
	$("#v_discipline").prop('disabled',true);
	$("#v_workStep").prop('disabled',true);
	$("#v_trOrder").prop('disabled',true);
	$("#v_durationCheck").prop('disabled',true);
	$("#v_checkSheet").css("display", "none");
	$('#v_discipline').val('0');
	
	$("#v_cutOffDate").datepicker('disable');
	$("#v_durationS").datepicker('disable');
	$("#v_durationD").datepicker('disable');
}

function downloadReportVen() {

	if(type===6 || type===8 || type===9 || type===10) $('#v_disciplineCheck').prop('checked',true);

	if(ven_value == undefined) {
		alert("검색 후 다운로드 해주세요.");
		return;
	}else if(ven_value == "") {
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
	var discipline = $("#v_discipline option:selected").text();
	var cut_off_date = $("#v_cutOffDate").val();
	var workStep = $("#v_workStep option:selected").text();
	var discipLength = $('#v_discipline option').length;
	
	var date_s = $("#v_durationS").val();
	var date_d = $("#v_durationD").val();
	
	if($('#v_disciplineCheck').is(':checked') == true) { // 데이터를 하나의 시트에 다운
		fileDownloadExcel("./vendorExcelDownload.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:ven_column,
		    	valueList:ven_value,
		    	projecetName:projecetName,
		    	discipline:discipline,
		    	discipLength:discipLength,
		    	reportType:type,
		    	cut_off_date:cut_off_date,
		    	workStep:workStep,
		    	date_s:date_s,
		    	date_d:date_d
		    },
		    successCallback: function (url) {
		    },
		    failCallback: function(responesHtml, url) {
		    }
		});
	}else if($('#v_disciplineCheck').is(':checked') == false) { // Discipline 별 시트를 나눠서 다운
		fileDownloadExcel("./vendorExcelDownloadDiscip.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:ven_column,
		    	valueList:ven_value,
		    	projecetName:projecetName,
		    	discipline:discipline,
		    	discipLength:discipLength,
		    	reportType:type,
		    	cut_off_date:cut_off_date,
		    	workStep:workStep,
		    	date_s:date_s,
		    	date_d:date_d
		    },
		    successCallback: function (url) {
		    },
		    failCallback: function(responesHtml, url) {
		    }
		});
	}
}

var type="";

function RetrieveVen() {

	if(type===6 || type===8 || type===9 || type===10) $('#v_disciplineCheck').prop('checked',true);
	
	$("#data_table_vendor").css("display", "none");	
	
	var reportType="3_"+type;
	var reportTitle="";
	var trOrder = $("#v_trOrder option:selected").val();
	var date_s = $("#v_durationS").val();
	var date_d = $("#v_durationD").val();
	var cut_off_date = $("#v_cutOffDate").val();
	date_s = date_s.replace(/-/gi, "");
	date_d = date_d.replace(/-/gi, "");
	cut_off_date = cut_off_date.replace(/-/gi, "");
	
	var discipline = $("#v_discipline option:selected").val();
	var workStep = $("#v_workStep option:selected").val();
	var disciplineCheck = $('#v_disciplineCheck').is(':checked');
	let durationCheck = $('#v_durationCheck').is(':checked')===true?'All':'';
	let allBehindList = $('#vdci_allBehindList').is(':checked')===true?'All':'';

	var discipLength = $('#v_discipline option').length;
	if ( disciplineCheck ) { disciplineCheck = "T"; }
	else { disciplineCheck="F"; }
	
	if ( type == 1 ) {
		reportTitle="VDCI LIST";
	} else if ( type == 2 ) {
		reportTitle="VDCI Detail LIST";
	} else if ( type == 3 ) {
		reportTitle="VDCI History List";
	} else if ( type == 4 ) {
		reportTitle="VDCI Behind List";
	} else if ( type == 5 ) {
		reportTitle="VDCI Transmittal List";
	} else if ( type == 6 ) {
		reportTitle="VDCI Progress Status";
	} else if ( type == 7 ) {
		reportTitle="VDCI Workdone List";
	} else if ( type == 8 ) {
		reportTitle="VDCI Workdone Status";
	} else if ( type == 9 ) {
		reportTitle="Weekly Progress Graph";
	} else if ( type == 10 ) {
		reportTitle="Monthly Progress Graph";
	} else if ( type == 11 ) {
		reportTitle="WBS Detail";
	} 
	
	$('.reportTitleV').text(reportTitle);
	
	var del_yn = "";
	if($('#vdci_chkDelDoc').is(':checked') == true) {
		del_yn = "1";
	}
	
	if($('input[name="vendorStatusRaido"]').is(':checked') != false) {
		$.ajax({
		    url: './vendorGetReportData.do',
		    type: 'POST',
		    beforeSend:function(){
			    $('body').prepend(loading);
			},
			complete:function(){
			    $('#loading').remove();
			},
		    data:{
		    	prj_id: selectPrjId,
		    	del_yn: del_yn,
		    	reportType : reportType,
		    	trOrder:trOrder,
		    	date_s:date_s,
		    	date_d:date_d,
		    	cut_off_date:cut_off_date,
		    	discipline:discipline,
		    	discipLength:discipLength,
		    	workStep:workStep,
		    	disciplineCheck:disciplineCheck,
		    	durationCheck:durationCheck,
		    	allBehindList:allBehindList
		    },
		    success: function onData (data) {
		    	ven_column=data[0];
		    	ven_value=data[1];
		    	ven_columnJson=data[2];
		    	ven_valueJson=data[3];
		    	ven_reportVo=data[4];
		    	size=data[5];
		    	
		    	$('#reportCntListV').html('건수 : ' + size + '개');
		    	
		    	if ( size == 0 ) {
		    		alert("Data is Empty !!");
		    		return;
		    	}
		    	
		    	$("#data_table_vendor").css("display", "block");	
		    	drawDataVen(ven_columnJson, ven_valueJson);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else {
		alert('REPORT를 선택하고 조회바랍니다.');
	}
}

function clickReportVenRadioType(typeT) {
	initClearVen();
	type=typeT;
	
	if ( typeT == 1 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 2 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 3 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 4 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_workStep").prop('disabled',false);
		$("#v_cutOffDate").datepicker('enable');
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 5 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_durationS").datepicker('enable');
		$("#v_durationD").datepicker('enable');
		$("#v_durationCheck").prop('disabled',false);
		$("#v_trOrder").prop('disabled',false);
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 6 ) {
		$("#v_cutOffDate").datepicker('enable');
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 7 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_durationS").datepicker('enable');
		$("#v_durationD").datepicker('enable');
		$("#v_durationCheck").prop('disabled',false);
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 8 ) {
		$("#v_durationS").datepicker('enable');
		$("#v_durationD").datepicker('enable');
		$("#v_durationCheck").prop('disabled',false);
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 9 ) {
		$("#v_cutOffDate").datepicker('enable');
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 10 ) {
		$("#v_cutOffDate").datepicker('enable');
		$("#v_checkSheet").css("display", "block");
	} else if ( typeT == 11 ) {
		$("#v_discipline").prop('disabled',false);
		$("#v_cutOffDate").datepicker('enable');
		$("#v_checkSheet").css("display", "block");
	}
}

//function drawDataVen(column, value) {
//	for ( var i=0; i<column.length; ++i ){
//		var obj = column[i];
//		obj.formatter = customFormatter;
//		obj.variableHeight = true;
//	}
//	
//	var table = new Tabulator(".vdciList", {
//        data: value,
//        layout: "fitColumns",
//        columns: column,
//    });
//}

function drawDataVen(column, value) {
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
	
	var table = new Tabulator(".vdciList", {
        data: value,
        layout: "fitColumns",
        columns: column,
        placeholder: "No Data Set",
//		pagination : true,
//		paginationSize : 100,
        height : 600,
    });
	
	table.on("rowClick", function(e,row){
		let data = row.getData();
		//console.log("data.discipline = "+data.Discipline);
		
		var listTitle = $('.reportTitleV').text();
		
		if(listTitle == "VDCI Progress Status") {
		
			var reportType="3_2";
			var reportTitle="";
			var trOrder = $("#v_trOrder option:selected").val();
			var date_s = $("#v_durationS").val();
			var date_d = $("#v_durationD").val();
			date_s = date_s.replace(/-/gi, "");
			date_d = date_d.replace(/-/gi, "");
			
			var discipline = data.Discipline;
			$('#v_discipline_code_id').val(discipline);
			let discipline_code_id = $('#v_discipline_code_id option:selected').text();
			if(discipline[0]==='Total') discipline_code_id='0';
			
			var workStep = $("#workStep option:selected").val();
			var disciplineCheck = $('#disciplineCheck').is(':checked');
		
			$.ajax({
			    url: './vendorGetReportData.do',
			    type: 'POST',
			    beforeSend:function(){
				    $('body').prepend(loading);
				},
				complete:function(){
				    $('#loading').remove();
				},
			    data:{
			    	prj_id: selectPrjId,
			    	reportType : reportType,
			    	trOrder:trOrder,
			    	date_s:date_s,
			    	date_d:date_d,
			    	del_yn:'',
			    	discipline:discipline_code_id,
			    	workStep:workStep,
			    	disciplineCheck:disciplineCheck
			    },
			    success: function onData (data) {
			    	ven_column=data[0];
			    	ven_value=data[1];
			    	ven_columnJson=data[2];
			    	ven_valueJson=data[3];
			    	ven_reportVo=data[4];
			    	size=data[5];
			    	
			    	$(".pop_detailVDCIList").dialog("open");
			    	
			    	//$('#reportCntList').html('건수 : ' + size + '개');
			    	
			    	//console.log("size = "+size);
			    	if ( size == 0 ) {
			    		alert("Data is Empty !!");
			    	}
			    	
			    	drawDataVen2(ven_columnJson, ven_valueJson);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
	});
}

function drawDataVen2(column, value) {
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
	
	var table = new Tabulator(".vdciList2", {
//		cellClick : function (e , cell) {
//			var clickTbCell = cell.getData();
//			
//			console.log("clickTbCell = "+clickTbCell);
//		},
        data: value,
        layout: "fitColumns",
        columns: column,
        placeholder: "No Data Set",
//		pagination : true,
//		paginationSize : 100,
		height : 600
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
function removeTabPrjReportVen(){
	
}