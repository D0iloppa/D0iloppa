var s_columnJson;
var s_valueJson;
var s_column;
var s_value;
var s_reportVo;

var selectPrjId;

function getDisciplineSite() {
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
            $('#s_discipline').append(optionT);
	    	
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].discip_display;
	    		var value = dataTmp[i].discip_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#s_discipline').append(option);
                
                
	    		var option2 = $("<option value=\""+text+"\">"+value+"</option>");
                $('#s_discipline_code_id').append(option2);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getWorkStepSite() {
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type:'SITE STEP'
	    },
	    success: function onData (data) {
	    	var dataTmp = data[0];
	    	
	    	var optionT = $("<option value=\"0\">전체</option>");
            $('#s_workStep').append(optionT);
	    	
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].set_code;
	    		var value = dataTmp[i].set_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#s_workStep').append(option);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

$( document ).ready(function() {
	
	$(".pop_detailSDCIList").dialog({
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
	
	$('#s_cutOffDate').datepicker('setDate', new Date());
	$('#s_durationS').datepicker('setDate', setReqdate());
	$('#s_durationD').datepicker('setDate', new Date());

	$("#s_cutOffDate").prop("readonly", true);
	$("#s_durationS").prop("readonly", true);
	$("#s_durationD").prop("readonly", true);
	
	selectPrjId = $('#selectPrjId').val();
	
	getDisciplineSite();
	getWorkStepSite();	
	initClearSite();
});

function initClearSite() {
	$("#s_discipline").prop('disabled',true);
	$("#s_workStep").prop('disabled',true);
	$("#s_trOrder").prop('disabled',true);
	$("#s_durationCheck").prop('disabled',true);
	$("#s_checkSheet").css("display", "none");
	$('#s_discipline').val('0');
	
	$("#s_cutOffDate").datepicker('disable');
	$("#s_durationS").datepicker('disable');
	$("#s_durationD").datepicker('disable');
}

function downloadReportSite() {
	if(type===6 || type===8 || type===9 || type===10) $('#s_disciplineCheck').prop('checked',true);

	if(s_value == undefined) {
		alert("검색 후 다운로드 해주세요.");
		return;
	}else if(s_value == "") {
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
	var discipline = $("#s_discipline option:selected").text();
	var cut_off_date = $("#s_cutOffDate").val();
	var workStep = $("#s_workStep option:selected").text();
	var discipLength = $('#s_discipline option').length;
	
	var date_s = $("#s_durationS").val();
	var date_d = $("#s_durationD").val();
	
	if($('#s_disciplineCheck').is(':checked') == true) { // 데이터를 하나의 시트에 다운
		fileDownloadExcel("./siteExcelDownload.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:s_column,
		    	valueList:s_value,
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
	}else if($('#s_disciplineCheck').is(':checked') == false) {
		fileDownloadExcel("./siteExcelDownloadDiscip.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:s_column,
		    	valueList:s_value,
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

function RetrieveSite() {

	if(type===6 || type===8 || type===9 || type===10) $('#s_disciplineCheck').prop('checked',true);
	
	$("#data_table_site").css("display", "none");
	
	var reportType="4_"+type;
	var reportTitle="";
	var trOrder = $("#s_trOrder option:selected").val();
	var date_s = $("#s_durationS").val();
	var date_d = $("#s_durationD").val();
	var cut_off_date = $("#s_cutOffDate").val();
	date_s = date_s.replace(/-/gi, "");
	date_d = date_d.replace(/-/gi, "");
	cut_off_date = cut_off_date.replace(/-/gi, "");
	
	var discipline = $("#s_discipline option:selected").val();
	var workStep = $("#s_workStep option:selected").val();
	var disciplineCheck = $('#s_disciplineCheck').is(':checked');
	let durationCheck = $('#s_durationCheck').is(':checked')===true?'All':'';
	let allBehindList = $('#sdci_allBehindList').is(':checked')===true?'All':'';

	var discipLength = $('#s_discipline option').length;
	if ( disciplineCheck ) { disciplineCheck = "T"; }
	else { disciplineCheck="F"; }
	
	if ( type == 1 ) {
		reportTitle="SDCI LIST";
	} else if ( type == 2 ) {
		reportTitle="SDCI Detail LIST";
	} else if ( type == 3 ) {
		reportTitle="SDCI History List";
	} else if ( type == 4 ) {
		reportTitle="SDCI Behind List";
	} else if ( type == 5 ) {
		reportTitle="SDCI Transmittal List";
	} else if ( type == 6 ) {
		reportTitle="SDCI Progress Status";
	} else if ( type == 7 ) {
		reportTitle="SDCI Workdone List";
	} else if ( type == 8 ) {
		reportTitle="SDCI Workdone Status";
	} else if ( type == 9 ) {
		reportTitle="Weekly Progress Graph";
	} else if ( type == 10 ) {
		reportTitle="Monthly Progress Graph";
	} else if ( type == 11 ) {
		reportTitle="WBS Detail";
	} 
	
	$('.reportTitleS').text(reportTitle);

	var del_yn = "";
	if($('#sdci_chkDelDoc').is(':checked') == true) {
		del_yn = "1";
	}
	
	if($('input[name="siteStatusRaido"]').is(':checked') != false) {
	$.ajax({
	    url: './siteGetReportData.do',
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
	    	durationCheck:durationCheck,
	    	allBehindList:allBehindList
	    },
	    success: function onData (data) {
	    	s_column=data[0];
	    	s_value=data[1];
	    	s_columnJson=data[2];
	    	s_valueJson=data[3];
	    	s_reportVo=data[4];
	    	size=data[5];
	    	
	    	$('#reportCntListS').html('건수 : ' + size + '개');
	    	
	    	if ( size == 0 ) {
	    		alert("Data is Empty !!");
	    		return;
	    	}
	    	
	    	console.log(data[3]);
	    	$("#data_table_site").css("display", "block");
	    	drawDataSite(s_columnJson, s_valueJson);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	}else {
		alert('REPORT를 선택하고 조회바랍니다.');
	}
}

function clickReportSiteRadioType(typeT) {

	initClearSite();
	type=typeT;
	
	if ( typeT == 1 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 2 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 3 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 4 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_workStep").prop('disabled',false);
		$("#s_cutOffDate").datepicker('enable');
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 5 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_durationS").datepicker('enable');
		$("#s_durationD").datepicker('enable');
		$("#s_durationCheck").prop('disabled',false);
		$("#s_trOrder").prop('disabled',false);
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 6 ) {
		$("#s_cutOffDate").datepicker('enable');
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 7 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_durationS").datepicker('enable');
		$("#s_durationD").datepicker('enable');
		$("#s_durationCheck").prop('disabled',false);
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 8 ) {
		$("#s_durationS").datepicker('enable');
		$("#s_durationD").datepicker('enable');
		$("#s_durationCheck").prop('disabled',false);
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 9 ) {
		$("#s_cutOffDate").datepicker('enable');
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 10 ) {
		$("#s_cutOffDate").datepicker('enable');
		$("#s_checkSheet").css("display", "block");
	} else if ( typeT == 11 ) {
		$("#s_discipline").prop('disabled',false);
		$("#s_cutOffDate").datepicker('enable');
		$("#s_checkSheet").css("display", "block");
	}
}

//function drawDataSite(column, value) {
//	for ( var i=0; i<column.length; ++i ){
//		var obj = column[i];
//		obj.formatter = customFormatter;
//		obj.variableHeight = true;
//	}
//	
//	var table = new Tabulator(".sdciList", {
//        data: value,
//        layout: "fitColumns",
//        columns: column,
//    });
//}

function drawDataSite(column, value) {
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
	
	var table = new Tabulator(".sdciList", {
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
		
		var listTitle = $('.reportTitleS').text();
		
		if(listTitle == "SDCI Progress Status") {
		
			var reportType="4_2";
			var reportTitle="";
			var trOrder = $("#s_trOrder option:selected").val();
			var date_s = $("#s_durationS").val();
			var date_d = $("#s_durationD").val();
			date_s = date_s.replace(/-/gi, "");
			date_d = date_d.replace(/-/gi, "");
			
			var discipline = data.Discipline;
			$('#s_discipline_code_id').val(discipline);
			let discipline_code_id = $('#s_discipline_code_id option:selected').text();
			if(discipline[0]==='Total') discipline_code_id='0';
			
			var workStep = $("#workStep option:selected").val();
			var disciplineCheck = $('#disciplineCheck').is(':checked');
		
			$.ajax({
			    url: './siteGetReportData.do',
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
			    	s_column=data[0];
			    	s_value=data[1];
			    	s_columnJson=data[2];
			    	s_valueJson=data[3];
			    	s_reportVo=data[4];
			    	size=data[5];
			    	
			    	$(".pop_detailSDCIList").dialog("open");
			    	
			    	//$('#reportCntList').html('건수 : ' + size + '개');
			    	
			    	//console.log("size = "+size);
			    	if ( size == 0 ) {
			    		alert("Data is Empty !!");
			    	}
			    	
			    	drawDataSite2(s_columnJson, s_valueJson);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
	});
}

function drawDataSite2(column, value) {
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
	
	var table = new Tabulator(".sdciList2", {
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
function removeTabPrjReportSite(){
	
}