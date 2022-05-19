var v_columnJson;
var v_valueJson;
var v_column;
var v_value;
var v_reportVo;

var selectPrjId;

var v_table_tt; 

function getDiscipline() {
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
            $('#discipline').append(optionT);
            
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].discip_display;
	    		var value = dataTmp[i].discip_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#discipline').append(option);
                

	    		var option2 = $("<option value=\""+text+"\">"+value+"</option>");
                $('#discipline_code_id').append(option2);
	    	}

	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getWorkStep() {
	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: selectPrjId,
	    	set_code_type:'ENGINEERING STEP'
	    },
	    success: function onData (data) {
	    	var dataTmp = data[0];
	    	
	    	var optionT = $("<option value=\"0\">전체</option>");
            $('#workStep').append(optionT);
            
	    	for ( var i =0; i<dataTmp.length; ++i ) {
	    		var text = dataTmp[i].set_code;
	    		var value = dataTmp[i].set_code_id;
	    		if ( text == '' ) continue;
	    		
	    		var option = $("<option value=\""+value+"\">"+text+"</option>");
                $('#workStep').append(option);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

$( document ).ready(function() {
	
	/*var loading = $('<div id="loading" class="loading"></div><img id="loading_img" alt="loading" src="../resource/images/loading.gif" />')
    .appendTo(document.body).hide();

	$(window)	

    .ajaxStart(function(){
    	loading.show();
	})

	.ajaxStop(function(){
		loading.hide();
	});*/
	$(".pop_detailDCIList").dialog({
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
	
	$('#cutOffDate').datepicker('setDate', new Date());
	$('#durationS').datepicker('setDate', lastWeek());
	$('#durationD').datepicker('setDate', new Date());
	
	$("#cutOffDate").prop("readonly", true);
	$("#durationS").prop("readonly", true);
	$("#durationD").prop("readonly", true);
	
	selectPrjId = $('#selectPrjId').val();
	
	getDiscipline();
	getWorkStep();
	initClear();
	
});


function initClear() {
	$("#discipline").prop('disabled',true);
	$("#workStep").prop('disabled',true);
	$("#trOrder").prop('disabled',true);
	$("#durationCheck").prop('disabled',true);
	$("#checkSheet").css("display", "none");
	$('#discipline').val("0");
	
	$("#cutOffDate").datepicker('disable');
	$("#durationS").datepicker('disable');
	$("#durationD").datepicker('disable');
}

function downloadReport() {

	if(type===6 || type===8 || type===9 || type===10) $('#disciplineCheck').prop('checked',true);
	
	if(v_value == undefined) {
		alert("검색 후 다운로드 해주세요.");
		return;
	}else if(v_value == "") {
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
	var discipline = $("#discipline option:selected").text();
	var cut_off_date = $("#cutOffDate").val();
	var workStep = $("#workStep option:selected").text();
	var discipLength = $('#discipline option').length;
	
	var date_s = $("#durationS").val();
	var date_d = $("#durationD").val();
	
	if($('#disciplineCheck').is(':checked') == true) { // 데이터를 하나의 시트에 다운
		fileDownloadExcel("./engineeringExcelDownload.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:v_column,
		    	valueList:v_value,
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
	}else if($('#disciplineCheck').is(':checked') == false) { // Discipline 별 시트를 나눠서 다운
		//console.log("시트별로 다운");
		fileDownloadExcel("./engineeringExcelDownloadDiscip.do",{
		    httpMethod: "POST",
		    traditional : true, //필수
		    data:{
		    	bMakeShee:bMakeShee,
		    	fileName:fileName,
		    	title:title,
		    	sheetName:sheetName,
		    	columnList:v_column,
		    	valueList:v_value,
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

function Retrieve() {
	
	if(type===6 || type===8 || type===9 || type===10) $('#disciplineCheck').prop('checked',true);
	
	$("#data_table_engineering").css("display", "none");
	
	var reportType="1_"+type;
	var reportTitle="";
	var trOrder = $("#trOrder option:selected").val();
	var date_s = $("#durationS").val();
	var date_d = $("#durationD").val();
	var cut_off_date = $("#cutOffDate").val();
	date_s = date_s.replace(/-/gi, "");
	date_d = date_d.replace(/-/gi, "");
	cut_off_date = cut_off_date.replace(/-/gi, "");
	
	
	var discipline = $("#discipline option:selected").val();
	var workStep = $("#workStep option:selected").val();
	var disciplineCheck = $('#disciplineCheck').is(':checked');
	let durationCheck = $('#durationCheck').is(':checked')===true?'All':'';
	let allBehindList = $('#dci_allBehindList').is(':checked')===true?'All':'';

	var discipLength = $('#discipline option').length;
	if ( disciplineCheck ) { disciplineCheck = "T"; }
	else { disciplineCheck="F"; }
	
	if ( type == 1 ) {
		reportTitle="DCI List";
	} else if ( type == 2 ) {
		reportTitle="DCI Detail List";
	} else if ( type == 3 ) {
		reportTitle="DCI History List";
	} else if ( type == 4 ) {
		reportTitle="DCI Behind List";
	} else if ( type == 5 ) {
		reportTitle="DCI Transmittal List";
	} else if ( type == 6 ) {
		reportTitle="DCI Progress Status";
	} else if ( type == 7 ) {
		reportTitle="DCI Workdone List";
	} else if ( type == 8 ) {
		reportTitle="DCI Workdone Status";
	} else if ( type == 9 ) {
		reportTitle="DCI Week Progress Graph";
	} else if ( type == 10 ) {
		reportTitle="DCI Monthly Progress Graph";
	} else if ( type == 11 ) {
		reportTitle="WBS Detail";
	} else if ( type == 12 ) {
		reportTitle="WBS Summary (IFA/IFAC)";
	} else if ( type == 13 ) {
		reportTitle="WBS Detail (IFA/IFAC)";
	} 
	$('.reportTitle').text(reportTitle);
	

	var del_yn = "";
	if($('#dci_chkDelDoc').is(':checked') == true) {
		del_yn = "1";
	}
	
	if($('input[name="enginerringRaido"]').is(':checked') != false) {
		$.ajax({
		    url: './engineeringGetReportData.do',
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
		    	v_column=data[0];
		    	v_value=data[1];
		    	v_columnJson=data[2];
		    	v_valueJson=data[3];
		    	v_reportVo=data[4];
		    	size=data[5];
		    	
		    	$('#reportCntList').html('건수 : ' + size + '개');
		    	
		    	//console.log("size = "+size);
		    	if ( size == 0 ) {
		    		alert("Data is Empty !!");
		    		return;
		    	}
		    	
		    	$("#data_table_engineering").css("display", "block");
		    	drawData(v_columnJson, v_valueJson);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else {
		alert('REPORT를 선택하고 조회바랍니다.');
	}
}

function clickReportRadioType(typeT) {
	
	initClear();
	type=typeT;
	$("input:checkbox[id='disciplineCheck']").prop("checked", false);
	
	if ( typeT == 1 ) {
		$("#discipline").prop('disabled',false);
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 2 ) {
		$("#discipline").prop('disabled',false);
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 3 ) {
		$("#discipline").prop('disabled',false);
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 4 ) {
		$("#discipline").prop('disabled',false);
		$("#workStep").prop('disabled',false);
		$("#cutOffDate").datepicker('enable');
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 5 ) {
		$("#discipline").prop('disabled',false);
		$("#durationS").datepicker('enable');
		$("#durationD").datepicker('enable');
		$("#durationCheck").prop('disabled',false);
		$("#trOrder").prop('disabled',false);
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 6 ) {
		$("#cutOffDate").datepicker('enable');
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 7 ) {
		$("#discipline").prop('disabled',false);
		$("#durationS").datepicker('enable');
		$("#durationD").datepicker('enable');
		$("#durationCheck").prop('disabled',false);
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 8 ) {
		$("#durationS").datepicker('enable');
		$("#durationD").datepicker('enable');
		$("#durationCheck").prop('disabled',false);
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 9 ) {
		$("#checkSheet").css("display", "block");
		$("#cutOffDate").datepicker('enable');
	} else if ( typeT == 10 ) {
		$("#checkSheet").css("display", "block");
		$("#cutOffDate").datepicker('enable');
	} else if ( typeT == 11 ) {
		$("#discipline").prop('disabled',false);
		$("#cutOffDate").datepicker('enable');
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 12 ) {
		$("#discipline").prop('disabled',false);
		$("#cutOffDate").datepicker('enable');
		$("#checkSheet").css("display", "block");
	} else if ( typeT == 13 ) {
		$("#discipline").prop('disabled',false);
		$("#cutOffDate").datepicker('enable');
		$("#checkSheet").css("display", "block");
	}
}

function drawData(column, value) {
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
	
	v_table_tt = new Tabulator(".dciList", {
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
	
	
	v_table_tt.on("rowClick", function(e,row){
		let data = row.getData();
		//console.log("data.discipline = "+data.Discipline);
		
		var listTitle = $('.reportTitle').text();
		
		if(listTitle == "DCI Progress Status") {
		
			var reportType="1_2";
			var reportTitle="";
			var trOrder = $("#trOrder option:selected").val();
			var date_s = $("#durationS").val();
			var date_d = $("#durationD").val();
			date_s = date_s.replace(/-/gi, "");
			date_d = date_d.replace(/-/gi, "");
			
			var discipline = data.Discipline;
			$('#discipline_code_id').val(discipline);
			let discipline_code_id = $('#discipline_code_id option:selected').text();
			if(discipline[0]==='Total') discipline_code_id='0';
			/*if(discipline == "Boiler-Basic") {
				discipline = "00001";
			}else if(discipline == "Boiler-Detail") {
				discipline = "00002";
			}else if(discipline == "Boiler-Steel Structure") {
				discipline = "00003";
			}else if(discipline == "Electrical") {
				discipline = "00004";
			}else if(discipline == "Instrumentation") {
				discipline = "00005";
			}else if(discipline == "Piping") {
				discipline = "00006";
			}else if(discipline == "Engineering Management") {
				discipline = "00007";
			}else if(discipline == "Project Management") {
				discipline = "00008";
			}else if(discipline == "Quality Management") {
				discipline = "00009";
			}else if(discipline == "Engineering Control") {
				discipline = "00010";
			}*/
			
			var workStep = $("#workStep option:selected").val();
			var disciplineCheck = $('#disciplineCheck').is(':checked');
		
			$.ajax({
			    url: './engineeringGetReportData.do',
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
			    	v_column=data[0];
			    	v_value=data[1];
			    	v_columnJson=data[2];
			    	v_valueJson=data[3];
			    	v_reportVo=data[4];
			    	size=data[5];
			    	
			    	$(".pop_detailDCIList").dialog("open");
			    	
			    	//$('#reportCntList').html('건수 : ' + size + '개');
			    	
			    	//console.log("size = "+size);
			    	if ( size == 0 ) {
			    		alert("Data is Empty !!");
			    	}
			    	
			    	drawData2(v_columnJson, v_valueJson);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
	});
}

function drawData2(column, value) {
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
	
	v_table_tt = new Tabulator(".dciList2", {
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
		maxHeight : 600
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




