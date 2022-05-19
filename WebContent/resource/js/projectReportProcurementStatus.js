var p_columnJson;
var p_valueJson;
var p_column;
var p_value;
var p_reportVo;

var selectPrjId;

var v_table;

function getDisciplinePro() {
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
            $('#p_discipline').append(optionT);
	    	
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].discip_display;
	    		var value = dataTmp[i].discip_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#p_discipline').append(option);
                

	    		var option2 = $("<option value=\""+text+"\">"+value+"</option>");
                $('#p_discipline_code_id').append(option2);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getWorkStepPro() {
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type:'PROCUREMENT STEP'
	    },
	    success: function onData (data) {
	    	var dataTmp = data[0];
	    	
	    	var optionT = $("<option value=\"0\">전체</option>");
            $('#p_workStep').append(optionT);
	    	
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].set_code;
	    		var value = dataTmp[i].set_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#p_workStep').append(option);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

$( document ).ready(function() {
	
	$(".pop_detailPCIList").dialog({
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
	
	$('#p_cutOffDate').datepicker('setDate', new Date());
	$('#p_durationS').datepicker('setDate', lastWeek());
	$('#p_durationD').datepicker('setDate', new Date());
	
	$("#p_cutOffDate").prop("readonly", true);
	$("#p_durationS").prop("readonly", true);
	$("#p_durationD").prop("readonly", true);
	
	selectPrjId = $('#selectPrjId').val();
	
	getDisciplinePro();
	getWorkStepPro();
	initClearPro();
});

function initClearPro() {
	$("#p_discipline").prop('disabled',true);
	$("#p_workStep").prop('disabled',true);
	$("#p_trOrder").prop('disabled',true);
	$("#p_durationCheck").prop('disabled',true);
	$("#p_checkSheet").css("display", "none");
	
	$("#p_cutOffDate").datepicker('disable');
	$("#p_durationS").datepicker('disable');
	$("#p_durationD").datepicker('disable');
}

function downloadReportPro() {

	if(type===4 || type===6 || type===7 || type===8) $('#p_disciplineCheck').prop('checked',true);
	
	if(p_value == undefined) {
		alert("검색 후 다운로드 해주세요.");
		return;
	}else if(p_value == "") {
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
	var discipline = $("#p_discipline option:selected").text();
	var cut_off_date = $("#p_cutOffDate").val();
	var workStep = $("#p_workStep option:selected").text();
	var discipLength = $('#p_discipline option').length;
	
	var date_s = $("#p_durationS").val();
	var date_d = $("#p_durationD").val();
	
	if($('#p_disciplineCheck').is(':checked') == true) { // 데이터를 하나의 시트에 다운
		fileDownloadExcel("./procurementExcelDownload.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:p_column,
		    	valueList:p_value,
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
	}else if($('#p_disciplineCheck').is(':checked') == false) {
		fileDownloadExcel("./procurementExcelDownloadDiscip.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:p_column,
		    	valueList:p_value,
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

function RetrievePro() {

	if(type===4 || type===6 || type===7 || type===8) $('#p_disciplineCheck').prop('checked',true);
	
	$("#data_table_procurement").css("display", "none");
	
	var reportType="2_"+type;
	var reportTitle="";
	var date_s = $("#p_durationS").val();
	var date_d = $("#p_durationD").val();
	var cut_off_date = $("#p_cutOffDate").val();
	date_s = date_s.replace(/-/gi, "");
	date_d = date_d.replace(/-/gi, "");
	cut_off_date = cut_off_date.replace(/-/gi, "");
	
	var discipline = $("#p_discipline option:selected").val();
	var workStep = $("#p_workStep option:selected").val();
	var disciplineCheck = $('#p_disciplineCheck').is(':checked');
	let durationCheck = $('#p_durationCheck').is(':checked')===true?'All':'';
	let allBehindList = $('#dci_allBehindList').is(':checked')===true?'All':'';

	var discipLength = $('#p_discipline option').length;
	if ( disciplineCheck ) { disciplineCheck = "T"; }
	else { disciplineCheck="F"; }
	
	if ( type == 1 ) {
		reportTitle="PCI List";
	} else if ( type == 2 ) {
		reportTitle="PCI Detail List";
	} else if ( type == 3 ) {
		reportTitle="PCI Behind List";
	} else if ( type == 4 ) {
		reportTitle="PCI Progress Status";
	} else if ( type == 5 ) {
		reportTitle="PCI Workdone List";
	} else if ( type == 6 ) {
		reportTitle="PCI Workdone Status";
	} else if ( type == 7 ) {
		reportTitle="PCI Week Progress Graph";
	} else if ( type == 8 ) {
		reportTitle="PCI Monthly Progress Graph";
	} else if ( type == 9 ) {
		reportTitle="WBS Detail";
	} 
	
	$('.reportTitleP').text(reportTitle);

	var del_yn = "";
	if($('#pci_chkDelDoc').is(':checked') == true) {
		del_yn = "1";
	}
	
	if($('input[name="procurementRaido"]').is(':checked') != false) {
		$.ajax({
		    url: './procurementGetReportData.do',
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
		    	p_column=data[0];
		    	p_value=data[1];
		    	p_columnJson=data[2];
		    	p_valueJson=data[3];
		    	p_reportVo=data[4];
		    	size=data[5];
		    	
		    	$('#reportCntListP').html('건수 : ' + size + '개');
		    	
		    	if ( size == 0 ) {
		    		alert("Data is Empty !!");
		    		return;
		    	}
		    	
		    	$("#data_table_procurement").css("display", "block");
		    	drawDataPro(p_columnJson, p_valueJson);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else {
		alert('REPORT를 선택하고 조회바랍니다.');
	}
}

function clickReportProRadioType(typeT) {
	initClearPro();
	type=typeT;
	
	if ( typeT == 1 ) {
		$("#p_discipline").prop('disabled',false);
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 2 ) {
		$("#p_discipline").prop('disabled',false);
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 3 ) {
		$("#p_discipline").prop('disabled',false);
		$("#p_workStep").prop('disabled',false);
		$("#p_cutOffDate").datepicker('enable');
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 4 ) {
		$("#p_cutOffDate").datepicker('enable');
	} else if ( typeT == 5 ) {
		$("#p_discipline").prop('disabled',false);
		$("#p_durationS").datepicker('enable');
		$("#p_durationD").datepicker('enable');
		$("#p_durationCheck").prop('disabled',false);
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 6 ) {
		$("#p_durationS").datepicker('enable');
		$("#p_durationD").datepicker('enable');
		$("#p_durationCheck").prop('disabled',false);
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 7 ) {
		$("#p_cutOffDate").datepicker('enable');
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 8 ) {
		$("#p_cutOffDate").datepicker('enable');
		$("#p_checkSheet").css("display", "block");
	} else if ( typeT == 9 ) {
		$("#p_discipline").prop('disabled',false);
		$("#p_cutOffDate").datepicker('enable');
		$("#p_checkSheet").css("display", "block");
	}
}

//function drawDataPro(column, value) {
//	for ( var i=0; i<column.length; ++i ){
//		var obj = column[i];
//		obj.formatter = customFormatter;
//		obj.variableHeight = true;
//	}
//	
//	var table = new Tabulator(".pciList", {
//        data: value,
//        layout: "fitColumns",
//        columns: column,
//    });
//}

function drawDataPro(column, value) {
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
	
	v_table = new Tabulator("#data_table_procurement", {
        data: value,
        layout: "fitColumns",
        columns: column,
        placeholder: "No Data Set",
//		pagination : true,
//		paginationSize : 100,
        height : 600,
    });
	v_table.on("rowClick", function(e,row){
		let data = row.getData();
		//console.log("data.discipline = "+data.Discipline);
		
		var listTitle = $('.reportTitleP').text();
		
		if(listTitle == "PCI Progress Status") {
		
			var reportType="2_2";
			var reportTitle="";
			var trOrder = $("#p_trOrder option:selected").val();
			var date_s = $("#p_durationS").val();
			var date_d = $("#p_durationD").val();
			date_s = date_s.replace(/-/gi, "");
			date_d = date_d.replace(/-/gi, "");
			
			var discipline = data.Discipline;
			$('#p_discipline_code_id').val(discipline);
			let discipline_code_id = $('#p_discipline_code_id option:selected').text();
			if(discipline[0]==='Total') discipline_code_id='0';
			
			var workStep = $("#workStep option:selected").val();
			var disciplineCheck = $('#disciplineCheck').is(':checked');
		
			$.ajax({
			    url: './procurementGetReportData.do',
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
			    	p_column=data[0];
			    	p_value=data[1];
			    	p_columnJson=data[2];
			    	p_valueJson=data[3];
			    	p_reportVo=data[4];
			    	size=data[5];
			    	
			    	$(".pop_detailPCIList").dialog("open");
			    	
			    	//$('#reportCntList').html('건수 : ' + size + '개');
			    	
			    	//console.log("size = "+size);
			    	if ( size == 0 ) {
			    		alert("Data is Empty !!");
			    	}
			    	
			    	drawDataPro2(p_columnJson, p_valueJson);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
	});
}

function drawDataPro2(column, value) {
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
	
	v_table = new Tabulator(".pciList2", {
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
function removeTabPrjReportPro(){
	
}