var engStepTable;
var vendorStepTable;
var siteStepTable;
var procStepTable;
var bulkStepTable;
var rvsRSTable;
var selectedPrevRowIndex;
var selectedRowIndex;
var selectedNextRowIndex;
var selectedNextNextRowIndex;
var nextnextisundefined = 0;
var selected_set_code;
var selected_set_order;
var prevnext_code;
var new_set_order = 0;
var selectPrjId = $('#selectPrjId').val();
var addOrUpdate = 'update';
var prev_set_order;
var next_set_order;
var setOrderArray = new Array();
var set_code_type;
var selectbox = [];
var current_step_table;
//var pg06 = {};
var tr_return_status_code_array = [];
var tr_return_status_code_id_array = [];
var vtr_return_status_code_array = [];
var vtr_return_status_code_id_array = [];
var str_return_status_code_array = [];
var str_return_status_code_id_array = [];
var stepList = [];

//false = 데이터 변경없을때, ture = 데이터 변경있고 저장 안했을때
var isEdited = false;

var checkedIndex = '';
var checkedUserString = '';
var deleteYN = 'N';

var prj_grid_pg06_copy;
var prj_grid_pgIFC_copy;



function saveCheck06(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			isEdited = false;
			getSTEPPrjCodeSettingsList();
	        return callback();
	    }
	    else{
	    	$("#general06 ul li a").off("click");
	        return false;
	    }
	}
	return true;
}

function pgen_step06(){
	$(".proj_general").attr('id','general06');
}

$(function(){
	$("#set_code_type").val("#sel_tab05").prop("selected",true);
	$("#sel_tab05").css('display','flex');
	
	$("#sel_tab06").css('display','none');
	$("#sel_tab07").css('display','none');
	$("#sel_tab08").css('display','none');
	
	$("#pg06_UpBtn").click(function() { stepRowUp( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	$("#pg06_DownBtn").click(function() { stepRowDown( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	$("#pg06_IFCUpBtn").click(function() { IFCRowUp( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	$("#pg06_IFCDownBtn").click(function() { IFCRowDown( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	
	$("#STEP_CODE_COPY_CHECKBOX").click(function(){
		if($("#STEP_CODE_COPY_CHECKBOX").is(":checked")) $(".copyDiv06").css('display','flex');
    	else $(".copyDiv06").css('display','none');
	});
	$(".copyChkdiv06").click(function(){
		$("#STEP_CODE_COPY_CHECKBOX").prop('checked', !$("#STEP_CODE_COPY_CHECKBOX").is(":checked"));
    	if($("#STEP_CODE_COPY_CHECKBOX").is(":checked")) $(".copyDiv06").css('display','flex');
    	else $(".copyDiv06").css('display','none');
    });
	
	$("#IFC_CODE_COPY_CHECKBOX").click(function(){
		if($("#IFC_CODE_COPY_CHECKBOX").is(":checked")) $(".copyDivIfc").css('display','flex');
    	else $(".copyDivIfc").css('display','none');
	});	

	$(".copyChkdivIfc").click(function(){
		$("#IFC_CODE_COPY_CHECKBOX").prop('checked', !$("#IFC_CODE_COPY_CHECKBOX").is(":checked"));
    	if($("#IFC_CODE_COPY_CHECKBOX").is(":checked")) $(".copyDivIfc").css('display','flex');
    	else $(".copyDivIfc").css('display','none');
    });
    
	pg04.grid = []; // pg04의 각 그리드를 담는 배열
	
	$(".pop_RefershPg06Confirm").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: "auto"
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
	
	$(".pop_rvsRS").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: 1000
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
	
	$('#set_code_type').each(function() {
	    //Store old value
	    $(this).data('lastValue', $(this).val());
	});
	$("#set_code_type").on("change",function(){
		var lastData = $(this).data('lastValue');

		
		if(!saveCheck06(function(){return true})) {
			$(this).val(lastData);
			
			return;
		}
		// 마지막 값을 현재 값으로 갱신
		$(this).data('lastValue', $(this).val());
	});
	
    $(document).on("click", "#rvsRS", function() {
    	getIFCPrjCodeSettingsList();
        $(".pop_rvsRS").dialog("open");
        return false;
    });
    $(".pop_IFCCodeDelete").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: "auto"
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
    $(".pop_STEPCodeDelete").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: "auto"
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
    $('#STEP_selected_prj').click(function(){
    	$('#STEP_select_div').addClass('on');
    });
    $('.STEP_prj_select').click(function(){
    	var step_selected_prj_id = $(this).attr('id').replace('STEP_prj_id_is_','');
    	$('#STEP_selectPrjId').val(step_selected_prj_id);
    	var prj_nm = $(this).children().next().html();
    	$('#STEP_selected_prj').html(prj_nm);
    	$('#STEP_select_div').removeClass('on');
    });
    $('#IFC_selected_prj').click(function(){
    	$('#IFC_multi_sel_wrap').addClass('on');
    });
    $('.IFC_prj_select').click(function(){
    	var ifc_selected_prj_id = $(this).attr('id').replace('IFC_prj_id_is_','');
    	$('#IFC_selectPrjId').val(ifc_selected_prj_id);
    	var prj_nm = $(this).children().next().html();
    	$('#IFC_selected_prj').html(prj_nm);
    	$('#IFC_multi_sel_wrap').removeClass('on');
    });
    
	Tabulator.extendModule("edit", "editors", {
	    uppercaseInput:function(cell, onRendered, success, cancel, editorParams){

	        //create and style input
	        var cellValue = cell.getValue().toUpperCase(),
	        input = document.createElement("input");

	        input.setAttribute("type", "text");

	        input.style.padding = "4px";
	        input.style.width = "100%";
	        input.style.boxSizing = "border-box";

	        input.value = cellValue;

	        onRendered(function () {
	            input.focus();
	            input.style.height = "100%";
	        });

	        function onChange(e) {
	            if (input.value != cellValue) {
	                success(input.value.toUpperCase());
	            } else {
	                cancel();
	            }
	        }

	        //submit new value on blur or change
	        input.addEventListener("change", onChange);
	        input.addEventListener("blur", onChange);

	        //submit new value on enter
	        input.addEventListener("keydown", function (e) {
	            if (e.keyCode == 13) {
	                success(input.value);
	            }

	            if (e.keyCode == 27) {
	                cancel();
	            }
	        });

	        return input;
	    },
	});
	//STEPCodeCopyEnable();
	getSTEPPrjCodeSettingsList();

    initPrjSelectGridPg06Copy();
    initPrjSelectGridPgIFCCopy();
    
});

function initPrjSelectGridPg06Copy() {
	prj_grid_pg06_copy  = new Tabulator("#prj_list_grid_pg06", {
		layout: "fitColumns",
		placeholder:"There is No Project",
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
	
	prj_grid_pg06_copy.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_pg06_copy.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_pg06_copy.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#STEP_selectPrjId").val(rowData.prj_id);
		$("#STEP_selected_prj").html(rowData.prj_nm);
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prjGen06_search') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 	prjGen06Search();
			    }
		}
	});
}

function prjGen06Search() {
	let keyword = $("#prjGen06_search").val();
	let grid = prj_grid_pg06_copy;
	
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

function initPrjSelectGridPgIFCCopy() {
	prj_grid_pgIFC_copy  = new Tabulator("#prj_list_grid_pgIFC", {
		layout: "fitColumns",
		placeholder:"There is No Project",
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
	
	prj_grid_pgIFC_copy.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_pgIFC_copy.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_pgIFC_copy.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#IFC_selectPrjId").val(rowData.prj_id);
		$("#IFC_selected_prj").html(rowData.prj_nm);
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prjGenIFC_search') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 	prjGenIFCSearch();
			    }
		}
	});
}

function prjGenIFCSearch() {
	let keyword = $("#prjGenIFC_search").val();
	let grid = prj_grid_pgIFC_copy;
	
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

function setRvsRSList(rvsRS_pop_tbl){
    rvsRSTable = new Tabulator("#rvsRS_pop_tbl", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: rvsRS_pop_tbl,
        layout: "fitColumns",
        selectable:1,//true
        height:"100%",
        placeholder:"No Data Set",
        columns: [
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('IFCList');
	   		        checkbox.id = 'IFCList'+cell.getRow().getPosition();
    		        checkbox.name = 'IFCList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    }
    		},
            {
                title: "origin",
                field: "origin",
                visible:false
            },
            {
                title: "setOrder",
                field: "setOrder",
                visible:false
            },
            {
                title: "CODE",
                field: "CODE",
                editor:"uppercaseInput",
                width: 150
            },
            {
                title: "Description",
                field: "desc",
                editor:"input",
                hozAlign: "left"
            }
        ],
    });
    
//    pg06.grid.push(rvsRSTable);
    
    rvsRSTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().ORIGIN_CODE;
    	selected_set_order = row.getData().setOrder;
    })
}
function getCodeType(){
	var get_code_type = $('#set_code_type').val();
	if(get_code_type==='#sel_tab05'){
		set_code_type = 'ENGINEERING STEP';
		
		$("#sel_tab05").css('display','flex');		
		$("#sel_tab06").css('display','none');
		$("#sel_tab07").css('display','none');
		$("#sel_tab08").css('display','none');
		$("#sel_tab09").css('display','none');
		
	}else if(get_code_type==='#sel_tab06'){
		set_code_type = 'VENDOR DOC STEP';
		
		$("#sel_tab05").css('display','none');		
		$("#sel_tab06").css('display','flex');
		$("#sel_tab07").css('display','none');
		$("#sel_tab08").css('display','none');
		$("#sel_tab09").css('display','none');
		
	}else if(get_code_type==='#sel_tab07'){
		set_code_type = 'SITE DOC STEP';
		
		$("#sel_tab05").css('display','none');		
		$("#sel_tab06").css('display','none');
		$("#sel_tab07").css('display','flex');
		$("#sel_tab08").css('display','none');
		$("#sel_tab09").css('display','none');
		
	}else if(get_code_type==='#sel_tab08'){
		set_code_type = 'PROCUREMENT STEP';
		
		$("#sel_tab05").css('display','none');		
		$("#sel_tab06").css('display','none');
		$("#sel_tab07").css('display','none');
		$("#sel_tab08").css('display','flex');
		$("#sel_tab09").css('display','none');
		
	}else if(get_code_type==='#sel_tab09'){
		set_code_type = 'BULK STEP';
		
		$("#sel_tab05").css('display','none');		
		$("#sel_tab06").css('display','none');
		$("#sel_tab07").css('display','none');
		$("#sel_tab08").css('display','none');
		$("#sel_tab09").css('display','flex');
		
	}
}
function getSTEPPrjCodeSettingsList(){
	checkedIndex = '';
	deleteYN = 'N';
	
	if(isEdited===false){
		
	getCodeType();

	$.ajax({
	    url: 'getSTEPPrjCodeSettingsList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code_type: set_code_type
	    },
	    success: function onData (data) {
	    	// 테이블 갱신시 edited 여부 초기화
	    	isEdited = false;
	    	stepList = [];
	    	for(i=0;i<data[0].length;i++){
	    		let foreDate = data[0][i].set_val3 + '@@@' + data[0][i].set_val4;
	    		stepList.push({
	    			No: i,
	    			origin: 'update',
	    			ORIGIN_STEP: data[0][i].set_code_id,
	    			setOrder: data[0][i].set_order,
	    			STEP: data[0][i].set_code,
	    	        ifc: data[0][i].ifc_yn,
	    			Description: data[0][i].set_desc,
	    	        progRate: data[0][i].set_val,
	    	        actuDate: data[0][i].set_val2,
	    	        foreDate: foreDate,
	    	        bean: data[0][i]
	    		});
	    		setOrderArray[i] = data[0][i].set_order;
	    		
	    		selectbox.push(data[0][i].set_code);
	    	}
	    	if(set_code_type==='ENGINEERING STEP'){
	    		let actual_date_array = ["DOC. CHECK IN시","DRN 승인시","TR OUTGOING ISSUE시 Send Date","TR INCOMING시 Received Date","IFC ISSUE시 Send Date","TR RETURN STATUS 임의의 값 입력시","수기입력"];
	    		 $.ajax({
	    				type: 'POST',
	    				url: 'getActualDateList.do',
	    				data:{
	    					prj_id:selectPrjId,
	    					set_desc:'TR'
	    				},
	    				success: function(data) {
	    					tr_return_status_code_array = [];
	    					tr_return_status_code_id_array = [];
	    					if(data[0]!==undefined){
	    						for(let i=0;i<data[0].length;i++){
	    							actual_date_array.push(data[0][i].set_code + "메일 발송시");
	    						}
	    					}
	    					if(data[1]!==undefined){
	    						for(let i=0;i<data[1].length;i++){
	    							actual_date_array.push(data[1][i].set_code + ' (' + data[1][i].set_desc + ") 값 입력시");
	    							tr_return_status_code_array.push(data[1][i].set_code);
	    							tr_return_status_code_id_array.push(data[1][i].set_code_id);
	    						}
	    					}
				    		setEngStepList(stepList,actual_date_array);
	    				},
	    			    error: function onError (error) {
	    			        console.error(error);
	    			    }
	    			});
	    	}else if(set_code_type==='VENDOR DOC STEP'){
	    		let actual_date_array = ["VENDOR DOC. CHECK IN시","DRN 승인시","VTR OUTGOING ISSUE시 Send Date","VTR INCOMING시 Received Date","IFC ISSUE시 Send Date","VTR RETURN STATUS 임의의 값 입력시","수기입력"];
	    		 $.ajax({
	    				type: 'POST',
	    				url: 'getActualDateList.do',
	    				data:{
	    					prj_id:selectPrjId,
	    					set_desc:'VTR'
	    				},
	    				success: function(data) {
	    					vtr_return_status_code_array = [];
	    					vtr_return_status_code_id_array = [];
	    					if(data[0]!==undefined){
	    						for(let i=0;i<data[0].length;i++){
	    							actual_date_array.push(data[0][i].set_code + "메일 발송시");
	    						}
	    					}
	    					if(data[1]!==undefined){
	    						for(let i=0;i<data[1].length;i++){
	    							actual_date_array.push(data[1][i].set_code + ' (' + data[1][i].set_desc + ") 값 입력시");
	    							vtr_return_status_code_array.push(data[1][i].set_code);
	    							vtr_return_status_code_id_array.push(data[1][i].set_code_id);
	    						}
	    					}
	    		    		setVendorStepList(stepList,actual_date_array);
	    				},
	    			    error: function onError (error) {
	    			        console.error(error);
	    			    }
	    			});
	    	}else if(set_code_type==='SITE DOC STEP'){
	    		let actual_date_array = ["SITE DOC. CHECK IN시","DRN 승인시","STR OUTGOING ISSUE시 Send Date","STR INCOMING시 Received Date","IFC ISSUE시 Send Date","STR RETURN STATUS 임의의 값 입력시","수기입력"];
	    		 $.ajax({
	    				type: 'POST',
	    				url: 'getActualDateList.do',
	    				data:{
	    					prj_id:selectPrjId,
	    					set_desc:'STR'
	    				},
	    				success: function(data) {
	    					str_return_status_code_array = [];
	    					str_return_status_code_id_array = [];
	    					if(data[0]!==undefined){
	    						for(let i=0;i<data[0].length;i++){
	    							actual_date_array.push(data[0][i].set_code + "메일 발송시");
	    						}
	    					}
	    					if(data[1]!==undefined){
	    						for(let i=0;i<data[1].length;i++){
	    							actual_date_array.push(data[1][i].set_code + ' (' + data[1][i].set_desc + ") 값 입력시");
	    							str_return_status_code_array.push(data[1][i].set_code);
	    							str_return_status_code_id_array.push(data[1][i].set_code_id);
	    						}
	    					}
	    		    		setSiteStepList(stepList,actual_date_array);
	    				},
	    			    error: function onError (error) {
	    			        console.error(error);
	    			    }
	    			});
	    	}else if(set_code_type==='PROCUREMENT STEP'){
	    		setProcStepList(stepList);
	    	}else if(set_code_type==='BULK STEP'){
	    		setBulkStepList(stepList);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	}
}
function setEngStepList(engStepList,actual_date_array){
	let step_code_array = [];
	let step_code_id_array = [];
	 $.ajax({
			type: 'POST',
			url: 'getSTEPPrjCodeSettingsList.do',
			data:{
				prj_id:selectPrjId,
				set_code_type:'ENGINEERING STEP'
			},
			async:false,
			success: function(data) {
				if(data[0]!==undefined){
					for(let i=0;i<data[0].length;i++){
						step_code_array.push(data[0][i].set_code);
						step_code_id_array.push(data[0][i].set_code_id);
					}
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	engStepTable = new Tabulator("#engStep", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: engStepList,
        layout: "fitColumns",
        selectable:false,
        height:"100%",
        placeholder:"No Data Set",
        selectableCheck : function ( row ){ 
        	let cells = row.getCells();
        	for(let i=0;i<cells.length;i++){
        		return cells[i].getField()!=='foreDate';
        	}
        },
        columns: [
        	/*{formatter:"rowSelection", title:"CK", hozAlign:"center", headerSort:false, download:false, cellClick:function(e, cell){
	            cell.getRow().toggleSelect();
	        }},*/
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('STEPList');
	   		        checkbox.id = 'STEPList'+cell.getRow().getPosition();
    		        checkbox.name = 'STEPList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center",
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    },
    		    headerSort:false
    		},
    		{
                title: "STEP",
                field: "STEP",
                editor:"uppercaseInput",
                validator: "unique",
				widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "IFC",
                field: "ifc",
                editor:"select",
                editorParams:{
                        listItemFormatter:function(value, title){
//                        	console.log(value);
                            return title;
                        },
                        values:["Y",""], //create list of values from all values contained in this column
                        sortValuesList:"desc", //if creating a list of values from values:true then choose how it should be sorted
                        //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                        elementAttributes:{
                            maxlength:"1", //set the maximum character length of the input element to 10 characters
                        },
                        verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                        multiselect:false, //allow multiple entries to be selected
                    
                },
    			widthGrow : 2,
    		    headerSort:false
            },
            {
                title: "Desc",
                field: "Description",
                editor:true,
    			widthGrow : 20,
    			hozAlign : "left",
    		    headerSort:false
            },
            {
                title: "Progress Rate(%)",
                field: "progRate",
                editor:true,
                validator:"numeric",
    			widthGrow : 3,
    		    headerSort:false,
    		    validator:["required", "max:100"]
            },
            {
                title: "Actual DATE (자동입력설정)",
                field: "actuDate",
                editor:"select",
                editorParams:{
                    listItemFormatter:function(value, title){
                        return title;
                    },
                    values: actual_date_array,
                    sortValuesList:"asc", //if creating a list of values from values:true then choose how it should be sorted
                    //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                    elementAttributes:{
                        maxlength:"10", //set the maximum character length of the input element to 10 characters
                    },
                    verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                    multiselect:false, //allow multiple entries to be selected
                },
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "FORECAST DATE 자동 적용 RULE (미설정시 수기 입력 가능)",
                field: "foreDate",
                selectable:false,
                hozAlign: "left",
				formatter: function(cell, formatterParams, onRendered) {
					let div = document.createElement("div");
					div.classList.add('foredatediv');
					
					let select = document.createElement("select");
					select.id = "forecast_select_"+cell.getRow().getPosition(true);
					select.classList.add('foredateselected');
					let option = document.createElement("option");
					option.value = "blank";
					option.text = "";
					select.append(option);
					let option2 = document.createElement("option");
					option2.value = "tr_return_status";
					option2.text = "TR RETURN STATUS";
					select.append(option2);
					for(let i=0;i<step_code_array.length;i++){
						let option3 = document.createElement("option");
						option3.value = step_code_id_array[i];
						option3.text = step_code_array[i];
						select.append(option3);
					}
					

					let span = document.createElement("span");
					span.id ='foredatespan' + cell.getRow().getPosition(true);
					select.addEventListener('change',function(){
						isEdited = true;
						if(this.value==='blank'){
							span.innerHTML = '';
						}else if(this.value==='tr_return_status'){
							span.innerHTML = '';
//							console.log('span.innerHTML:'+span.innerHTML);
							for(let i=0;i<tr_return_status_code_array.length;i++){
								let tr_return_status_checkbox = document.createElement("input");
								tr_return_status_checkbox.type = 'checkbox';
								tr_return_status_checkbox.classList.add('ml5');
								tr_return_status_checkbox.name = 'tr_return_status_checkbox'+ cell.getRow().getPosition(true);
								tr_return_status_checkbox.value = tr_return_status_code_id_array[i];
								tr_return_status_checkbox.onclick = function(){
									isEdited = true;
								}
								span.append(tr_return_status_checkbox);
								let label = document.createElement("label"); 
								label.innerHTML = tr_return_status_code_array[i];
								span.append(label);
							}
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}							
							
							span.append('  Received DATE + ');
							span.append(number);
							span.append(' Days');
						}else{
							span.innerHTML = '';
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							
							span.innerHTML = '  ACTUAL DATE + ';
							span.append(number);
							span.append(' Days');
						}
					});
					div.append(select);
					div.append(span);

					onRendered(function(){
						//dateSet
						let cellValue = cell.getValue();
						if(cellValue){
							let set_val3 = cellValue.split('@@@')[0];
							let set_val4 = cellValue.split('@@@')[1];
							let span = document.getElementById('foredatespan'+cell.getRow().getPosition(true));
							if(set_val3!=='' && set_val3!==undefined && set_val3!==null && set_val3!=='null'){
								if(set_val3.includes('!!')){
									$('#forecast_select_'+cell.getRow().getPosition(true)).val('tr_return_status');
									span.innerHTML = '';
									for(let i=0;i<tr_return_status_code_array.length;i++){
										let tr_return_status_checkbox = document.createElement("input");
										tr_return_status_checkbox.type = 'checkbox';
										tr_return_status_checkbox.classList.add('ml5');
										tr_return_status_checkbox.name = 'tr_return_status_checkbox'+ cell.getRow().getPosition(true);
										tr_return_status_checkbox.value = tr_return_status_code_id_array[i];
										tr_return_status_checkbox.onclick = function(){
											isEdited = true;
										}
										span.append(tr_return_status_checkbox);
										let label = document.createElement("label"); 
										label.innerHTML = tr_return_status_code_array[i];
										span.append(label);
									}
									let number = document.createElement("input");
									number.type = 'number';
									number.id = 'actualdateplus'+cell.getRow().getPosition(true);
									number.min = '0';
									number.max = '100';
									number.value = '0';
									number.onchange = function(){
										isEdited = true;
									}
									span.append('  Received DATE + ');
									span.append(number);
									span.append(' Days');
									let tr_return_status_string = set_val3.split('!!')[1];
									let tr_return_status_array = tr_return_status_string.split('||');
									for(let j=0;j<tr_return_status_array.length;j++){
										$('input[name="tr_return_status_checkbox'+cell.getRow().getPosition(true)+'"][value="'+tr_return_status_array[j]+'"]').prop("checked", true);
									}
									$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
								}else{
									$('#forecast_select_'+cell.getRow().getPosition(true)).val(set_val3);
									span.innerHTML = '';
									let number = document.createElement("input");
									number.type = 'number';
									number.id = 'actualdateplus'+cell.getRow().getPosition(true);
									number.min = '0';
									number.max = '100';
									number.value = '0';
									number.onchange = function(){
										isEdited = true;
									}
									span.innerHTML = '  ACTUAL DATE + ';
									span.append(number);
									span.append(' Days');
									$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
								}
							}
							div.innerHTML = '';
							div.append(select);
							div.append(span);
						}
					});
					
			        return div;
				},
				widthGrow : 45,
    		    headerSort:false
            }
            ],
        });
	engStepTable.on("cellEdited",function(cell){
		isEdited = true;
	});
	
	engStepTable.on("rowClick",function(e,row){
		let chk = e.target.getAttribute('tabulator-field');
		if(chk == null) {
			return false;
		}
		else {
			engStepTable.deselectRow();
			row.select();
		}
	});

    engStepTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().STEP;
    	selected_set_order = row.getData().setOrder;
    });
	current_step_table = engStepTable;
}
function setVendorStepList(vendorStepList,actual_date_array){
	let step_code_array = [];
	let step_code_id_array = [];
	 $.ajax({
			type: 'POST',
			url: 'getSTEPPrjCodeSettingsList.do',
			data:{
				prj_id:selectPrjId,
				set_code_type:'VENDOR DOC STEP'
			},
			async:false,
			success: function(data) {
				if(data[0]!==undefined){
					for(let i=0;i<data[0].length;i++){
						step_code_array.push(data[0][i].set_code);
						step_code_id_array.push(data[0][i].set_code_id);
					}
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	vendorStepTable = new Tabulator("#venderDocStep", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: vendorStepList,
        layout: "fitColumns",
        selectable:1,
        height:"100%",
        placeholder:"No Data Set",
        selectable:false,
        selectableCheck : function ( row ){ 
        	let cells = row.getCells();
        	for(let i=0;i<cells.length;i++){
        		return cells[i].getField()!=='foreDate';
        	}
        },
        columns: [
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('STEPList');
	   		        checkbox.id = 'STEPList'+cell.getRow().getPosition();
    		        checkbox.name = 'STEPList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center",
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    },
    		    headerSort:false
    		},
            {
                title: "STEP",
                field: "STEP",
                editor:"uppercaseInput",
                validator: "unique",
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "IFC",
                field: "ifc",
                editor:"select",
                editorParams:{
                        listItemFormatter:function(value, title){
//                        	console.log(value);
                            return title;
                        },
                        values:["Y",""], //create list of values from all values contained in this column
                        sortValuesList:"desc", //if creating a list of values from values:true then choose how it should be sorted
                        //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                        elementAttributes:{
                            maxlength:"1", //set the maximum character length of the input element to 10 characters
                        },
                        verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                        multiselect:false, //allow multiple entries to be selected
                    
                },
    			widthGrow : 2,
    		    headerSort:false
            },
            {
                title: "Desc",
                field: "Description",
                editor:true,
    			widthGrow : 20,
    			hozAlign : "left",
    		    headerSort:false
            },
            {
                title: "Progress Rate(%)",
                field: "progRate",
                editor:true,
                validator:"numeric",
    			widthGrow : 3,
    		    headerSort:false
            },
            {
                title: "Actual DATE (자동입력설정)",
                field: "actuDate",
                editor:"select",
                editorParams:{
                    listItemFormatter:function(value, title){
//                    	console.log(value);
                        return title;
                    },
                    values:actual_date_array, //create list of values from all values contained in this column
                    sortValuesList:"asc", //if creating a list of values from values:true then choose how it should be sorted
                    //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                    elementAttributes:{
                        maxlength:"10", //set the maximum character length of the input element to 10 characters
                    },
                    verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                    multiselect:false, //allow multiple entries to be selected
                },
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "FORECAST DATE 자동 적용 RULE (미설정시 수기 입력 가능)",
                field: "foreDate",
                selectable:false,
                hozAlign: "left",
				formatter: function(cell, formatterParams, onRendered) {
					let div = document.createElement("div");
					div.classList.add('foredatediv');
					
					let select = document.createElement("select");
					select.id = "forecast_select_"+cell.getRow().getPosition(true);
					select.classList.add('foredateselected');
					let option = document.createElement("option");
					option.value = "blank";
					option.text = "";
					select.append(option);
					let option2 = document.createElement("option");
					option2.value = "tr_return_status";
					option2.text = "VTR RETURN STATUS";
					select.append(option2);
					for(let i=0;i<step_code_array.length;i++){
						let option3 = document.createElement("option");
						option3.value = step_code_id_array[i];
						option3.text = step_code_array[i];
						select.append(option3);
					}

					let span = document.createElement("span");
					span.id ='foredatespan' + cell.getRow().getPosition(true);
					select.addEventListener('change',function(){
						isEdited = true;
						if(this.value==='blank'){
							span.innerHTML = '';
						}else if(this.value==='tr_return_status'){
							span.innerHTML = '';
							for(let i=0;i<vtr_return_status_code_array.length;i++){
								let tr_return_status_checkbox = document.createElement("input");
								tr_return_status_checkbox.type = 'checkbox';
								tr_return_status_checkbox.classList.add('ml5');
								tr_return_status_checkbox.name = 'tr_return_status_checkbox'+ cell.getRow().getPosition(true);
								tr_return_status_checkbox.value = vtr_return_status_code_id_array[i];
								tr_return_status_checkbox.onclick = function(){
									isEdited = true;
								}
								span.append(tr_return_status_checkbox);
								let label = document.createElement("label"); 
								label.innerHTML = vtr_return_status_code_array[i];
								span.append(label);
							}
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							span.append('  Received DATE + ');
							span.append(number);
							span.append(' Days');
						}else{
							span.innerHTML = '';
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							span.innerHTML = '  ACTUAL DATE + ';
							span.append(number);
							span.append(' Days');
						}
					});
					div.append(select);
					div.append(span);

					onRendered(function(){
						//dateSet
						let cellValue = cell.getValue();
						if(cellValue){
							let set_val3 = cellValue.split('@@@')[0];
							let set_val4 = cellValue.split('@@@')[1];
							let span = document.getElementById('foredatespan'+cell.getRow().getPosition(true));
							if(set_val3!=='' && set_val3!==undefined && set_val3!==null && set_val3!=='null'){
								if(set_val3.includes('!!')){
									$('#forecast_select_'+cell.getRow().getPosition(true)).val('tr_return_status');
									span.innerHTML = '';
									for(let i=0;i<vtr_return_status_code_array.length;i++){
										let tr_return_status_checkbox = document.createElement("input");
										tr_return_status_checkbox.type = 'checkbox';
										tr_return_status_checkbox.classList.add('ml5');
										tr_return_status_checkbox.name = 'tr_return_status_checkbox'+ cell.getRow().getPosition(true);
										tr_return_status_checkbox.value = vtr_return_status_code_id_array[i];
										tr_return_status_checkbox.onclick = function(){
											isEdited = true;
										}
										span.append(tr_return_status_checkbox);
										let label = document.createElement("label"); 
										label.innerHTML = vtr_return_status_code_array[i];
										span.append(label);
									}
									let number = document.createElement("input");
									number.type = 'number';
									number.id = 'actualdateplus'+cell.getRow().getPosition(true);
									number.min = '0';
									number.max = '100';
									number.value = '0';
									number.onchange = function(){
										isEdited = true;
									}
									span.append('  Received DATE + ');
									span.append(number);
									span.append(' Days');
									let tr_return_status_string = set_val3.split('!!')[1];
									let tr_return_status_array = tr_return_status_string.split('||');
									for(let j=0;j<tr_return_status_array.length;j++){
										$('input[name="tr_return_status_checkbox'+cell.getRow().getPosition(true)+'"][value="'+tr_return_status_array[j]+'"]').prop("checked", true);
									}
									$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
								}else{
									$('#forecast_select_'+cell.getRow().getPosition(true)).val(set_val3);
									span.innerHTML = '';
									let number = document.createElement("input");
									number.type = 'number';
									number.id = 'actualdateplus'+cell.getRow().getPosition(true);
									number.min = '0';
									number.max = '100';
									number.value = '0';
									number.onchange = function(){
										isEdited = true;
									}
									span.innerHTML = '  ACTUAL DATE + ';
									span.append(number);
									span.append(' Days');
									$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
								}
							}
							div.innerHTML = '';
							div.append(select);
							div.append(span);
						}
					});
			        return div;
				},
				widthGrow : 45,
    		    headerSort:false
            }
            ],
	});
	vendorStepTable.on("cellEdited",function(cell){
		isEdited = true;
	});
	vendorStepTable.on("rowClick",function(e,row){
		let chk = e.target.getAttribute('tabulator-field');
		if(chk == null) {
			return false;
		}
		else {
			vendorStepTable.deselectRow();
			row.select();
		}
	});
    vendorStepTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().STEP;
    	selected_set_order = row.getData().setOrder;
    })
	current_step_table = vendorStepTable;
}
function setSiteStepList(siteStepList,actual_date_array){
	let step_code_array = [];
	let step_code_id_array = [];
	 $.ajax({
			type: 'POST',
			url: 'getSTEPPrjCodeSettingsList.do',
			data:{
				prj_id:selectPrjId,
				set_code_type:'SITE DOC STEP'
			},
			async:false,
			success: function(data) {
				if(data[0]!==undefined){
					for(let i=0;i<data[0].length;i++){
						step_code_array.push(data[0][i].set_code);
						step_code_id_array.push(data[0][i].set_code_id);
					}
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	siteStepTable = new Tabulator("#siteDocStep", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: siteStepList,
        layout: "fitColumns",
        selectable:1,
        height:"100%",
        placeholder:"No Data Set",
        selectable:false,
        selectableCheck : function ( row ){ 
        	let cells = row.getCells();
        	for(let i=0;i<cells.length;i++){
        		return cells[i].getField()!=='foreDate';
        	}
        },
        columns: [
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('STEPList');
	   		        checkbox.id = 'STEPList'+cell.getRow().getPosition();
    		        checkbox.name = 'STEPList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    },
    		    headerSort:false
    		},
            {
                title: "STEP",
                field: "STEP",
                editor:"uppercaseInput",
                validator: "unique",
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "Desc",
                field: "Description",
                editor:true,
    			widthGrow : 20,
    			hozAlign : "left",
    		    headerSort:false
            },
            {
                title: "Progress Rate(%)",
                field: "progRate",
                editor:true,
                validator:"numeric",
    			widthGrow : 3,
    		    headerSort:false
            },
            {
                title: "Actual DATE (자동입력설정)",
                field: "actuDate",
                editor:"select",
                editorParams:{
                    listItemFormatter:function(value, title){
//                    	console.log(value);
                        return title;
                    },
                    values:actual_date_array, //create list of values from all values contained in this column
                    sortValuesList:"asc", //if creating a list of values from values:true then choose how it should be sorted
                    //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                    elementAttributes:{
                        maxlength:"10", //set the maximum character length of the input element to 10 characters
                    },
                    verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                    multiselect:false, //allow multiple entries to be selected
                },
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "FORECAST DATE 자동 적용 RULE (미설정시 수기 입력 가능)",
                field: "foreDate",
                selectable:false,
                hozAlign: "left",
				formatter: function(cell, formatterParams, onRendered) {
					let div = document.createElement("div");
					div.classList.add('foredatediv');
					
					let select = document.createElement("select");
					select.id = "forecast_select_"+cell.getRow().getPosition(true);
					select.classList.add('foredateselected');
					let option = document.createElement("option");
					option.value = "blank";
					option.text = "";
					select.append(option);
					let option2 = document.createElement("option");
					option2.value = "tr_return_status";
					option2.text = "STR RETURN STATUS";
					select.append(option2);
					for(let i=0;i<step_code_array.length;i++){
						let option3 = document.createElement("option");
						option3.value = step_code_id_array[i];
						option3.text = step_code_array[i];
						select.append(option3);
					}

					let span = document.createElement("span");
					span.id ='foredatespan' + cell.getRow().getPosition(true);
					select.addEventListener('change',function(){
						isEdited = true;
						if(this.value==='blank'){
							span.innerHTML = '';
						}else if(this.value==='tr_return_status'){
							span.innerHTML = '';
							for(let i=0;i<str_return_status_code_array.length;i++){
								let tr_return_status_checkbox = document.createElement("input");
								tr_return_status_checkbox.type = 'checkbox';
								tr_return_status_checkbox.classList.add('ml5');
								tr_return_status_checkbox.name = 'tr_return_status_checkbox'+ cell.getRow().getPosition(true);
								tr_return_status_checkbox.value = str_return_status_code_id_array[i];
								tr_return_status_checkbox.onclick = function(){
									isEdited = true;
								}
								span.append(tr_return_status_checkbox);
								let label = document.createElement("label"); 
								label.innerHTML = str_return_status_code_array[i];
								span.append(label);
							}
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							span.append('  Received DATE + ');
							span.append(number);
							span.append(' Days');
						}else{
							span.innerHTML = '';
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							span.innerHTML = '  ACTUAL DATE + ';
							span.append(number);
							span.append(' Days');
						}
					});
					div.append(select);
					div.append(span);

					onRendered(function(){
						//dateSet
						let cellValue = cell.getValue();
						if(cellValue){
							let set_val3 = cellValue.split('@@@')[0];
							let set_val4 = cellValue.split('@@@')[1];
							let span = document.getElementById('foredatespan'+cell.getRow().getPosition(true));
							if(set_val3!=='' && set_val3!==undefined && set_val3!==null && set_val3!=='null'){
								if(set_val3.includes('!!')){
									$('#forecast_select_'+cell.getRow().getPosition(true)).val('tr_return_status');
									span.innerHTML = '';
									for(let i=0;i<str_return_status_code_array.length;i++){
										let tr_return_status_checkbox = document.createElement("input");
										tr_return_status_checkbox.type = 'checkbox';
										tr_return_status_checkbox.classList.add('ml5');
										tr_return_status_checkbox.name = 'tr_return_status_checkbox'+ cell.getRow().getPosition(true);
										tr_return_status_checkbox.value = str_return_status_code_id_array[i];
										tr_return_status_checkbox.onclick = function(){
											isEdited = true;
										}
										span.append(tr_return_status_checkbox);
										let label = document.createElement("label"); 
										label.innerHTML = str_return_status_code_array[i];
										span.append(label);
									}
									let number = document.createElement("input");
									number.type = 'number';
									number.id = 'actualdateplus'+cell.getRow().getPosition(true);
									number.min = '0';
									number.max = '100';
									number.value = '0';
									number.onchange = function(){
										isEdited = true;
									}
									span.append('  Received DATE + ');
									span.append(number);
									span.append(' Days');
									let tr_return_status_string = set_val3.split('!!')[1];
									let tr_return_status_array = tr_return_status_string.split('||');
									for(let j=0;j<tr_return_status_array.length;j++){
										$('input[name="tr_return_status_checkbox'+cell.getRow().getPosition(true)+'"][value="'+tr_return_status_array[j]+'"]').prop("checked", true);
									}
									$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
								}else{
									$('#forecast_select_'+cell.getRow().getPosition(true)).val(set_val3);
									span.innerHTML = '';
									let number = document.createElement("input");
									number.type = 'number';
									number.id = 'actualdateplus'+cell.getRow().getPosition(true);
									number.min = '0';
									number.max = '100';
									number.value = '0';
									number.onchange = function(){
										isEdited = true;
									}
									span.innerHTML = '  ACTUAL DATE + ';
									span.append(number);
									span.append(' Days');
									$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
								}
							}
							div.innerHTML = '';
							div.append(select);
							div.append(span);
						}
					});
			        return div;
				},
				widthGrow : 45,
    		    headerSort:false
            }
            ],
	});
	siteStepTable.on("cellEdited",function(cell){
		isEdited = true;
	});
	siteStepTable.on("rowClick",function(e,row){
		let chk = e.target.getAttribute('tabulator-field');
		if(chk == null) {
			return false;
		}
		else {
			siteStepTable.deselectRow();
			row.select();
		}
	});
    siteStepTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().STEP;
    	selected_set_order = row.getData().setOrder;
    })
	current_step_table = siteStepTable;
}
function setProcStepList(procStepList){
	let step_code_array = [];
	let step_code_id_array = [];
	 $.ajax({
			type: 'POST',
			url: 'getSTEPPrjCodeSettingsList.do',
			data:{
				prj_id:selectPrjId,
				set_code_type:'PROCUREMENT STEP'
			},
			async:false,
			success: function(data) {
//				console.log(data[0]);
				if(data[0]!==undefined){
					for(let i=0;i<data[0].length;i++){
						step_code_array.push(data[0][i].set_code);
						step_code_id_array.push(data[0][i].set_code_id);
					}
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	procStepTable = new Tabulator("#ProcurementStep", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: procStepList,
        layout: "fitColumns",
        selectable:1,
        height:"100%",
        placeholder:"No Data Set",
        selectable:false,
        selectableCheck : function ( row ){ 
        	let cells = row.getCells();
        	for(let i=0;i<cells.length;i++){
        		return cells[i].getField()!=='foreDate';
        	}
        },
        columns: [
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('STEPList');
	   		        checkbox.id = 'STEPList'+cell.getRow().getPosition();
    		        checkbox.name = 'STEPList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    },
    		    headerSort:false
    		},
            {
                title: "STEP",
                field: "STEP",
                editor:"uppercaseInput",
                validator: "unique",
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "Desc",
                field: "Description",
                editor:true,
    			widthGrow : 20,
    			hozAlign : "left",
    		    headerSort:false
            },
            {
                title: "Progress Rate(%)",
                field: "progRate",
                editor:true,
                validator:"numeric",
    			widthGrow : 3,
    		    headerSort:false
            },
            {
                title: "Actual DATE (자동입력설정)",
                field: "actuDate",
                editor:"select",
                editorParams:{
                    listItemFormatter:function(value, title){
//                    	console.log(value);
                        return title;
                    },
                    values:["PROCUREMENT DOC. CHECK IN시","POR 발행시","PO 발행시", "수기입력"], //create list of values from all values contained in this column
                    sortValuesList:"asc", //if creating a list of values from values:true then choose how it should be sorted
                    //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                    elementAttributes:{
                        maxlength:"10", //set the maximum character length of the input element to 10 characters
                    },
                    verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                    multiselect:false, //allow multiple entries to be selected
                },
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "FORECAST DATE 자동 적용 RULE (미설정시 수기 입력 가능)",
                field: "foreDate",
                selectable:false,
                hozAlign: "left",
				formatter: function(cell, formatterParams, onRendered) {
					let div = document.createElement("div");
					div.classList.add('foredatediv');
					
					let select = document.createElement("select");
					select.id = "forecast_select_"+cell.getRow().getPosition(true);
					select.classList.add('foredateselected');
					let option = document.createElement("option");
					option.value = "blank";
					option.text = "";
					select.append(option);
//					console.log(step_code_array);
					for(let i=0;i<step_code_array.length;i++){
						let option3 = document.createElement("option");
						option3.value = step_code_id_array[i];
						option3.text = step_code_array[i];
						select.append(option3);
					}

					let span = document.createElement("span");
					span.id ='foredatespan' + cell.getRow().getPosition(true);
					select.addEventListener('change',function(){
						isEdited = true;
						if(this.value==='blank'){
							span.innerHTML = '';
						}else{
							span.innerHTML = '';
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							span.innerHTML = '  ACTUAL DATE + ';
							span.append(number);
							span.append(' Days');
						}
					});
					div.append(select);
					div.append(span);

					onRendered(function(){
						//dateSet
						let cellValue = cell.getValue();
						if(cellValue){
							let set_val3 = cellValue.split('@@@')[0];
							let set_val4 = cellValue.split('@@@')[1];
							let span = document.getElementById('foredatespan'+cell.getRow().getPosition(true));
							if(set_val3!=='' && set_val3!==undefined && set_val3!==null && set_val3!=='null'){
								$('#forecast_select_'+cell.getRow().getPosition(true)).val(set_val3);
								span.innerHTML = '';
								let number = document.createElement("input");
								number.type = 'number';
								number.id = 'actualdateplus'+cell.getRow().getPosition(true);
								number.min = '0';
								number.max = '100';
								number.value = '0';
								number.onchange = function(){
									isEdited = true;
								}
								span.innerHTML = '  ACTUAL DATE + ';
								span.append(number);
								span.append(' Days');
								$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
							}
							div.innerHTML = '';
							div.append(select);
							div.append(span);
						}
					});
			        return div;
				},
				widthGrow : 30,
    		    headerSort:false
            }
            ],
	});
	procStepTable.on("cellEdited",function(cell){
		isEdited = true;
	});
	procStepTable.on("rowClick",function(e,row){
		let chk = e.target.getAttribute('tabulator-field');
		if(chk == null) {
			return false;
		}
		else {
			procStepTable.deselectRow();
			row.select();
		}
	});
    procStepTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().STEP;
    	selected_set_order = row.getData().setOrder;
    })
	current_step_table = procStepTable;
}

function setBulkStepList(bulkStepList){
	let step_code_array = [];
	let step_code_id_array = [];
	 $.ajax({
			type: 'POST',
			url: 'getSTEPPrjCodeSettingsList.do',
			data:{
				prj_id:selectPrjId,
				set_code_type:'BULK STEP'
			},
			async:false,
			success: function(data) {
				if(data[0]!==undefined){
					for(let i=0;i<data[0].length;i++){
						step_code_array.push(data[0][i].set_code);
						step_code_id_array.push(data[0][i].set_code_id);
					}
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	bulkStepTable = new Tabulator("#BulkStep", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: bulkStepList,
        layout: "fitColumns",
        selectable:1,
        height:"100%",
        placeholder:"No Data Set",
        selectable:false,
        selectableCheck : function ( row ){ 
        	let cells = row.getCells();
        	for(let i=0;i<cells.length;i++){
        		return cells[i].getField()!=='foreDate';
        	}
        },
        columns: [
            {
                title: "No",
                field: "No",
                visible:false
            },
	        {
	        	title:"CK",
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('STEPList');
	   		        checkbox.id = 'STEPList'+cell.getRow().getPosition();
    		        checkbox.name = 'STEPList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			vertAlign:"middle",
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    },
    		    headerSort:false
    		},
            {
                title: "STEP",
                field: "STEP",
                editor:"uppercaseInput",
                validator: "unique",
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "Desc",
                field: "Description",
                editor:true,
    			widthGrow : 20,
    			hozAlign : "left",
    		    headerSort:false
            },
            {
                title: "Progress Rate(%)",
                field: "progRate",
                editor:true,
                validator:"numeric",
    			widthGrow : 3,
    		    headerSort:false
            },
            {
                title: "Actual DATE (자동입력설정)",
                field: "actuDate",
                editor:"select",
                editorParams:{
                    listItemFormatter:function(value, title){
//                    	console.log(value);
                        return title;
                    },
                    values:["BULK DOC. CHECK IN시","POR 발행시","PO 발행시", "수기입력"], //create list of values from all values contained in this column
                    sortValuesList:"asc", //if creating a list of values from values:true then choose how it should be sorted
                    //defaultValue:null, //set the value that should be selected by default if the cells value is undefined
                    elementAttributes:{
                        maxlength:"10", //set the maximum character length of the input element to 10 characters
                    },
                    verticalNavigation:"hybrid", //navigate to new row when at the top or bottom of the selection list
                    multiselect:false, //allow multiple entries to be selected
                },
    			widthGrow : 10,
    		    headerSort:false
            },
            {
                title: "FORECAST DATE 자동 적용 RULE (미설정시 수기 입력 가능)",
                field: "foreDate",
                selectable:false,
                hozAlign: "left",
				formatter: function(cell, formatterParams, onRendered) {
					let div = document.createElement("div");
					div.classList.add('foredatediv');
					
					let select = document.createElement("select");
					select.id = "forecast_select_"+cell.getRow().getPosition(true);
					select.classList.add('foredateselected');
					let option = document.createElement("option");
					option.value = "blank";
					option.text = "";
					select.append(option);
					for(let i=0;i<step_code_array.length;i++){
						let option3 = document.createElement("option");
						option3.value = step_code_id_array[i];
						option3.text = step_code_array[i];
						select.append(option3);
					}

					let span = document.createElement("span");
					span.id ='foredatespan' + cell.getRow().getPosition(true);
					select.addEventListener('change',function(){
						isEdited = true;
						if(this.value==='blank'){
							span.innerHTML = '';
						}else{
							span.innerHTML = '';
							let number = document.createElement("input");
							number.type = 'number';
							number.id = 'actualdateplus'+cell.getRow().getPosition(true);
							number.min = '0';
							number.max = '100';
							number.value = '0';
							number.onchange = function(){
								isEdited = true;
							}
							span.innerHTML = '  ACTUAL DATE + ';
							span.append(number);
							span.append(' Days');
						}
					});
					div.append(select);
					div.append(span);

					onRendered(function(){
						//dateSet
						let cellValue = cell.getValue();
						if(cellValue){
							let set_val3 = cellValue.split('@@@')[0];
							let set_val4 = cellValue.split('@@@')[1];
							let span = document.getElementById('foredatespan'+cell.getRow().getPosition(true));
							if(set_val3!=='' && set_val3!==undefined && set_val3!==null && set_val3!=='null'){
								$('#forecast_select_'+cell.getRow().getPosition(true)).val(set_val3);
								span.innerHTML = '';
								let number = document.createElement("input");
								number.type = 'number';
								number.id = 'actualdateplus'+cell.getRow().getPosition(true);
								number.min = '0';
								number.max = '100';
								number.value = '0';
								number.onchange = function(){
									isEdited = true;
								}
								span.innerHTML = '  ACTUAL DATE + ';
								span.append(number);
								span.append(' Days');
								$('#actualdateplus'+cell.getRow().getPosition(true)).val(set_val4);
							}
							div.innerHTML = '';
							div.append(select);
							div.append(span);
						}
					});
			        return div;
				},
				widthGrow : 30,
    		    headerSort:false
            }
            ],
	});
	bulkStepTable.on("cellEdited",function(cell){
		isEdited = true;
	});
	bulkStepTable.on("rowClick",function(e,row){
		let chk = e.target.getAttribute('tabulator-field');
		if(chk == null) {
			return false;
		}
		else {
			bulkStepTable.deselectRow();
			row.select();
		}
	});
	bulkStepTable.on("rowSelected", function(row){
    	selected_set_code = row.getData().STEP;
    	selected_set_order = row.getData().setOrder;
    })
	current_step_table = bulkStepTable;
}
function STEPCodeCopyEnable(){
    $("#STEP_CODE_COPY_CHECKBOX").change(function(){
        if($("#STEP_CODE_COPY_CHECKBOX").is(":checked")){
    		$('#STEP_select_div').css('display','block');
        }else{
    		$('#STEP_select_div').css('display','none');
        }
    });
}
function STEPCodeCopy(){
	var STEP_CODE_COPY_CHECKBOX = $('#STEP_CODE_COPY_CHECKBOX').is(":checked");
	var STEPselectPrjId = $('#STEP_selectPrjId').val();
	
	if(STEPselectPrjId == null || STEPselectPrjId == "") {
		alert("Copy할 대상 프로젝트를 선택해 주세요");
	}else {
		if(STEP_CODE_COPY_CHECKBOX){
			$("input:checkbox[name='STEPList']").each(function(){
				checkedIndex += $(this).attr('id').replaceAll('STEPList','') + ',';
			});
			checkedIndex = checkedIndex.slice(0, -1);
			var checkedIndexArray = checkedIndex.split(',');
			checkedUserString = '(';
			var get_code_type = $('#set_code_type').val();
			let checkStepCodeIdString = '';
			let checkStepCodeString = '';
			let step_table = '';
			for(i=0;i<checkedIndexArray.length;i++){
				if(get_code_type==='#sel_tab05'){
					step_table = 'p_prj_eng_step';
					if(engStepTable.getData().length!==0){
						checkStepCodeIdString += engStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
						checkStepCodeString += engStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
					}
				}else if(get_code_type==='#sel_tab06'){
					step_table = 'p_prj_vdr_step';
					if(vendorStepTable.getData().length!==0){
						checkStepCodeIdString += vendorStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
						checkStepCodeString += vendorStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
					}
				}else if(get_code_type==='#sel_tab07'){
					step_table = 'p_prj_sdc_step';
					if(siteStepTable.getData().length!==0){
						checkStepCodeIdString += siteStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
						checkStepCodeString += siteStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
					}
				}else if(get_code_type==='#sel_tab08'){
					step_table = 'p_prj_pro_step';
					if(procStepTable.getData().length!==0){
						checkStepCodeIdString += procStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
						checkStepCodeString += procStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
					}
				}else if(get_code_type==='#sel_tab09'){
					step_table = 'p_prj_mdc_step';
					if(bulkStepTable.getData().length!==0){
						checkStepCodeIdString += bulkStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
						checkStepCodeString += bulkStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
					}
				}
			}
			checkStepCodeIdString = checkStepCodeIdString.slice(0, -1);
			checkStepCodeString = checkStepCodeString.slice(0, -1);
			$.ajax({
				url: 'isUseStep.do',
				type: 'POST',
				data: {
				prj_id: selectPrjId,
				set_code_id: checkStepCodeIdString,
				set_code: checkStepCodeString,
				step_table: step_table
			},
			success: function onData (data) {
				if(data[0]==='삭제 가능'){
					$.ajax({
					    url: 'insertCopyPrjSTEPCodeSettings.do',
					    type: 'POST',
					    data: {
					    	prj_id: selectPrjId,
					    	copy_prj_id: STEPselectPrjId,
					    	set_code_type: set_code_type
					    },
					    success: function onData (data) {
							isEdited = false;
							checkedIndex = '';
							deleteYN = 'N';
							getSTEPPrjCodeSettingsList();
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
					});
				}else{
					alert(data[1]);
				}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
			});
		}
	}
}
function stepToExcel(){
	var get_code_type = $('#set_code_type').val();
	
	let gridData = "";
	
	if(set_code_type == 'ENGINEERING STEP') {
		gridData = engStepTable.getData();
	}else if(set_code_type == 'VENDOR DOC STEP') {
		gridData = vendorStepTable.getData();
	}else if(set_code_type == 'SITE DOC STEP') {
		gridData = siteStepTable.getData();
	}else if(set_code_type == 'PROCUREMENT STEP') {
		gridData = procStepTable.getData();
	}else if(set_code_type == 'BULK STEP') {
		gridData = bulkStepTable.getData();
	}
	
//	console.log(gridData);
	
	let jsonList = [];
	
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	let fileName = '';
	
	if(set_code_type == 'ENGINEERING STEP') {
		fileName = 'ENG_Step_Code';
	}else if(set_code_type == 'VENDOR DOC STEP') {
		fileName = 'VDR_Ste_Code';
	}else if(set_code_type == 'SITE DOC STEP') {
		fileName = 'SDC_Step_Code';
	}else if(set_code_type == 'PROCUREMENT STEP') {
		fileName = 'PRO_Step_Code';
	}else if(set_code_type == 'BULK STEP') {
		fileName = 'MDC_Step_Code';
	}
	
//	console.log(jsonList);
	
	var f = document.excelUploadForm_pg06;
	
//	console.log(set_code_type);
	
	f.prj_id06.value = selectPrjId;
	f.prj_nm06.value = getPrjInfo().full_nm;
    f.fileName06.value = fileName;
    f.jsonRowdatas06.value = jsonList;
    f.set_code_type06.value = set_code_type;
    f.action = "pg06ExcelDownload.do";
    f.submit();
}

function stepRowUp(callback){
	var get_code_type = $('#set_code_type').val();
	var thisGrid;
	if(get_code_type==='#sel_tab05'){
		thisGrid = engStepTable;
	}else if(get_code_type==='#sel_tab06'){
		thisGrid = vendorStepTable;
	}else if(get_code_type==='#sel_tab07'){
		thisGrid = siteStepTable;
	}else if(get_code_type==='#sel_tab08'){
		thisGrid = procStepTable;
	}else if(get_code_type==='#sel_tab09'){
		thisGrid = bulkStepTable;
	}
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getPrevRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getPrevRow().getIndex();
	
	selectedRow.move(targetIndex,true);
	
	thisGrid.getRow(selectedIndex).update({setOrder:(thisGrid.getRow(selectedIndex).getPosition()+1)+""});
	thisGrid.getRow(targetIndex).update({setOrder:(thisGrid.getRow(targetIndex).getPosition()+1)+""});
	
	thisGrid.redraw();
	
	var updateRow = [];
	updateRow.push(thisGrid.getRow(selectedIndex).getData());
	updateRow.push(thisGrid.getRow(targetIndex).getData());
	
	for(var i=0;i<updateRow.length;i++)
		$.ajax({
		    url: 'prjCodeSettingsUpdateCode.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	set_order : updateRow[i].setOrder,
				set_code_type : updateRow[i].bean.set_code_type,
				set_code_id :updateRow[i].bean.set_code_id
		    },
		    success: function onData (data) {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
	callback(thisGrid,selectedIndex);
}
function stepRowDown(callback){
	var get_code_type = $('#set_code_type').val();
	var thisGrid;
	if(get_code_type==='#sel_tab05'){
		thisGrid = engStepTable;
	}else if(get_code_type==='#sel_tab06'){
		thisGrid = vendorStepTable;
	}else if(get_code_type==='#sel_tab07'){
		thisGrid = siteStepTable;
	}else if(get_code_type==='#sel_tab08'){
		thisGrid = procStepTable;
	}else if(get_code_type==='#sel_tab09'){
		thisGrid = bulkStepTable;
	}
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getNextRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getNextRow().getIndex();
	
	selectedRow.move(targetIndex,false);
	
	thisGrid.getRow(selectedIndex).update({No:targetIndex,setOrder:(thisGrid.getRow(selectedIndex).getPosition()+1)+""});
	thisGrid.getRow(targetIndex).update({No:selectedIndex,setOrder:(thisGrid.getRow(targetIndex).getPosition()+1)+""});
	
	thisGrid.redraw();
	
	var updateRow = [];
	updateRow.push(thisGrid.getRow(selectedIndex).getData());
	updateRow.push(thisGrid.getRow(targetIndex).getData());
	
	for(var i=0;i<updateRow.length;i++)
		$.ajax({
		    url: 'prjCodeSettingsUpdateCode.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	set_order : updateRow[i].setOrder,
				set_code_type : updateRow[i].bean.set_code_type,
				set_code_id :updateRow[i].bean.set_code_id
		    },
		    success: function onData (data) {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
	callback(thisGrid,targetIndex);
}
function STEPAdd(){
	isEdited = true;
	let currentStepTable;
	var get_code_type = $('#set_code_type').val();
	var setOrder = Math.max.apply(null, setOrderArray)+1;
	if(get_code_type==='#sel_tab05'){
		currentStepTable = engStepTable;
	}else if(get_code_type==='#sel_tab06'){
		currentStepTable = vendorStepTable;
	}else if(get_code_type==='#sel_tab07'){
		currentStepTable = siteStepTable;
	}else if(get_code_type==='#sel_tab08'){
		currentStepTable = procStepTable;
	}else if(get_code_type==='#sel_tab09'){
		currentStepTable = bulkStepTable;
	}
	let no = currentStepTable.getData().length;
	var addrow =  { No: no, origin: 'add', setOrder: setOrder, STEP: '', ifc: '', Description: '', progRate:'', actuDate:''};
	currentStepTable.addRow(addrow);
}
function STEPCodeApply(){
	isEdited = false;
	
	if(deleteYN == 'Y') {
		deleteSTEPPrjCodeSettings();
	}
	var get_code_type = $('#set_code_type').val();
	var selectTableData;
	var editedCells;
	if(get_code_type==='#sel_tab05'){
		selectTableData = engStepTable.getData();
		editedCells = engStepTable.getEditedCells();
	}else if(get_code_type==='#sel_tab06'){
		selectTableData = vendorStepTable.getData();
		editedCells = vendorStepTable.getEditedCells();
	}else if(get_code_type==='#sel_tab07'){
		selectTableData = siteStepTable.getData();
		editedCells = siteStepTable.getEditedCells();
	}else if(get_code_type==='#sel_tab08'){
		selectTableData = procStepTable.getData();
		editedCells = procStepTable.getEditedCells();
	}else if(get_code_type==='#sel_tab09'){
		selectTableData = bulkStepTable.getData();
		editedCells = bulkStepTable.getEditedCells();
	}
	var set_code_arr = [];
	for(i=0;i<selectTableData.length;i++){
		if(selectTableData[i].STEP!=''){
		set_code_arr[i] = selectTableData[i].STEP.toUpperCase();
		}
	}
	var set_code_duplicate = isDuplicate(set_code_arr);
	if(set_code_duplicate){
		alert('STEP에 중복값이 있습니다.');
	}else{
    	$.ajax({ // p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT GENERAL > STEP 프로세스 정의",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
    	
		var RowDataArray = [];
		/*for(i=0;i<editedCells.length;i++){
			var RowData = editedCells[i].getRow().getData();
			RowDataArray.push(RowData);
		}*/
		//formatter로 인해 editedCells 감지 못하는 문제 때문에 전체 for문 돌리는 것으로 바꿈
		var foredate_array = $('.foredateselected');
		for(let i=0;i<foredate_array.length;i++){
			let changeRowPosition = parseInt(foredate_array[i].id.replaceAll('forecast_select_',''));
			let RowData = current_step_table.getRowFromPosition(changeRowPosition).getData();
			RowDataArray.push(RowData);
		}
		RowDataArray = duplicateRemoveInArray(RowDataArray);
		for(i=0;i<RowDataArray.length;i++){
			let progRateSum = 0;
			for(j=0;j<selectTableData.length;j++){
		    	var prograte = selectTableData[j].progRate;
		    	progRateSum += parseInt(prograte);
			}
			if(progRateSum!=100){
				alert('Progress의 합계값은 반드시 100이 되어야 합니다. 현재 합계: '+progRateSum);
				break;
			}
	    	var set_code = RowDataArray[i].STEP.toUpperCase();
	    	var set_desc = RowDataArray[i].Description;
	    	var set_order = RowDataArray[i].setOrder;
	    	var ifc_yn = RowDataArray[i].ifc;
	    	var set_val = RowDataArray[i].progRate;
	    	var set_val2 = RowDataArray[i].actuDate;
	    	var fore_date = $('#forecast_select_'+i).val();
	    	var set_val3 = '';
	    	var set_val4 = '';
	    	if(fore_date==='blank'){
	    		set_val3 = '';
	    		set_val4 = '';
	    	}else if(fore_date==='tr_return_status'){
	    		set_val3 = $('#forecast_select_'+i+' option:selected').text() + '!!';
	    		$('input[name="tr_return_status_checkbox'+i+'"]:checked').each(function() {
				    set_val3 += $(this).val() + '||';
				});
				set_val3.slice(0,-2);
				
				set_val4 += $('#actualdateplus'+i).val();
	    	}else{//Foredate에서 각 STEP을 선택했을 경우
	    		set_val3 = fore_date;
				set_val4 += $('#actualdateplus'+i).val();
	    	}
	    	
	    	var origin_step = RowDataArray[i].ORIGIN_STEP;
	    	if(RowDataArray[i].origin==='update' && set_code!='' && set_desc!='' && set_val!='' && set_val2!=''){
	    		updateSTEPPrjCodeSettings(selectPrjId,set_code,set_desc,origin_step,ifc_yn,set_val,set_val2,set_val3,set_val4);
	    	}else if(RowDataArray[i].origin==='add' && set_code!='' && set_desc!='' && set_val!='' && set_val2!=''){
	    		insertSTEPPrjCodeSettings(selectPrjId,set_code,set_desc,set_order,ifc_yn,set_val,set_val2,set_val3,set_val4);
	    	}
		}
		let progRateSum = 0;
		for(j=0;j<selectTableData.length;j++){
	    	var prograte = selectTableData[j].progRate;
	    	progRateSum += parseInt(prograte);
		}
		if(progRateSum==100){
			alert('successfully saved');
			getSTEPPrjCodeSettingsList();
		}
	}
}
function isDuplicate(arr)  {
	  const isDup = arr.some(function(x) {
	    return arr.indexOf(x) !== arr.lastIndexOf(x);
	  });
	                         
	  return isDup;
}
function duplicateRemoveInArray(arr){
	var setObj = new Set(arr);
	var setArr = [...setObj];
	return setArr;
}
function insertSTEPPrjCodeSettings(selectPrjId,set_code,set_desc,set_order,ifc_yn,set_val,set_val2,set_val3,set_val4){
	$.ajax({
	    url: 'insertSTEPPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code_type: set_code_type,
	    	set_code: set_code,
	    	set_desc: set_desc,
	    	set_order: set_order,
	    	ifc_yn:ifc_yn,
	    	set_val:set_val,
	    	set_val2:set_val2,
	    	set_val3:set_val3,
	    	set_val4:set_val4
	    },
	    async:false,
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function updateSTEPPrjCodeSettings(selectPrjId,set_code,set_desc,origin_step,ifc_yn,set_val,set_val2,set_val3,set_val4){
	$.ajax({
	    url: 'updateSTEPPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code_type: set_code_type,
	    	set_code: set_code,
	    	set_desc: set_desc,
	    	origin_code: origin_step,
	    	ifc_yn:ifc_yn,
	    	set_val:set_val,
	    	set_val2:set_val2,
	    	set_val3:set_val3,
	    	set_val4:set_val4
	    },
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function deleteSTEPPrjCodeSettings() {
	$.ajax({
    url: 'deleteSTEPPrjCodeSettings.do',
    type: 'POST',
    async: false,
    data: {
    	prj_id: selectPrjId,
    	set_code_id: checkedUserString,
    	set_code_type: set_code_type
    },
    success: function onData (data) {
    	checkedIndex = '';
    	deleteYN = 'N';
    },
    error: function onError (error) {
        console.error(error);
    }
});
}
//function STEPDeletePopup(){
//	isEdited = true;
//	if($("input:checkbox[name='STEPList']:checked").length===0){
//		alert('삭제할 데이터를 선택 후 삭제하십시오.');
//	}else{
//	    //$(".pop_STEPCodeDelete").dialog("open");
//	}
//}
//function STEPDeleteCancel(){
//    $(".pop_STEPCodeDelete").dialog("close");
//}
function STEPDelete(){
	isEdited = true;
	
	if($("input:checkbox[name='STEPList']:checked").length===0){
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}else{
		$("input:checkbox[name='STEPList']:checked").each(function(){
			checkedIndex += $(this).attr('id').replaceAll('STEPList','') + ',';
		});
		$.ajax({ // p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT GENERAL > STEP 프로세스 정의",
		    	method: "삭제"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		checkedIndex = checkedIndex.slice(0, -1);
		var checkedIndexArray = checkedIndex.split(',');
		checkedUserString = '(';
		var get_code_type = $('#set_code_type').val();
		deleteYN = 'Y';
		let checkStepCodeIdString = '';
		let checkStepCodeString = '';
		let step_table = '';
		for(i=0;i<checkedIndexArray.length;i++){
			if(get_code_type==='#sel_tab05'){
				step_table = 'p_prj_eng_step';
				checkStepCodeIdString += engStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
				checkStepCodeString += engStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
			}else if(get_code_type==='#sel_tab06'){
				step_table = 'p_prj_vdr_step';
				checkStepCodeIdString += vendorStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
				checkStepCodeString += vendorStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
			}else if(get_code_type==='#sel_tab07'){
				step_table = 'p_prj_sdc_step';
				checkStepCodeIdString += siteStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
				checkStepCodeString += siteStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
			}else if(get_code_type==='#sel_tab08'){
				step_table = 'p_prj_pro_step';
				checkStepCodeIdString += procStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
				checkStepCodeString += procStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
			}else if(get_code_type==='#sel_tab09'){
				step_table = 'p_prj_mdc_step';
				checkStepCodeIdString += bulkStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code_id +',';
				checkStepCodeString += bulkStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().bean.set_code +',';
			}
		}
		checkStepCodeIdString = checkStepCodeIdString.slice(0, -1);
		checkStepCodeString = checkStepCodeString.slice(0, -1);
		$.ajax({
			url: 'isUseStep.do',
			type: 'POST',
			data: {
			prj_id: selectPrjId,
			set_code_id: checkStepCodeIdString,
			set_code: checkStepCodeString,
			step_table: step_table
		},
		success: function onData (data) {
			if(data[0]==='삭제 가능'){
				for(i=0;i<checkedIndexArray.length;i++){
					if(get_code_type==='#sel_tab05'){
						console.log(engStepTable.getRow(parseInt(checkedIndexArray[i])).getData().bean.set_code_id);
						checkedUserString += "'" + engStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_STEP +"'" +',';
						engStepTable.deleteRow(engStepTable.getRow((parseInt(checkedIndexArray[i]))));
					}else if(get_code_type==='#sel_tab06'){
						checkedUserString += "'" + vendorStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_STEP +"'" +',';
						vendorStepTable.deleteRow(vendorStepTable.getRow((parseInt(checkedIndexArray[i]))));
					}else if(get_code_type==='#sel_tab07'){
						checkedUserString += "'" + siteStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_STEP +"'" +',';
						siteStepTable.deleteRow(siteStepTable.getRow((parseInt(checkedIndexArray[i]))));
					}else if(get_code_type==='#sel_tab08'){
						checkedUserString += "'" + procStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_STEP +"'" +',';
						procStepTable.deleteRow(procStepTable.getRow((parseInt(checkedIndexArray[i]))));
					}else if(get_code_type==='#sel_tab09'){
						checkedUserString += "'" + bulkStepTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_STEP +"'" +',';
						procStepTable.deleteRow(bulkStepTable.getRow((parseInt(checkedIndexArray[i]))));
					}
				}
				checkedUserString = checkedUserString.slice(0, -1)+')';
			}else{
				alert(data[0]);
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
		
	}
	
	// delete 버튼 누를 시 바로 반영 해야 하면 아래 코드 주석해제 및 코드작성
//	$.ajax({
//	    url: 'deleteSTEPPrjCodeSettings.do',
//	    type: 'POST',
//	    data: {
//	    	prj_id: selectPrjId,
//	    	set_code_id: checkedUserString,
//	    	set_code_type: set_code_type
//	    },
//	    success: function onData (data) {
//	    	alert('삭제되었습니다.');
//	        $(".pop_STEPCodeDelete").dialog("close");
//	    	getSTEPPrjCodeSettingsList();
//	    },
//	    error: function onError (error) {
//	        console.error(error);
//	    }
//	});
}
function STEPCodeRefresh(){
	if(isEdited == true) {
		$(".pop_RefershPg06Confirm").dialog("open");
//		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
//			isEdited = false;
//			checkedIndex = '';
//			deleteYN = 'N';
//			getSTEPPrjCodeSettingsList();
//	    }
//	    else{
//	        return false;
//	    }
	}else if(isEdited == false) {
		getSTEPPrjCodeSettingsList();
	}
}

function refreshCode06Yes() {
	$(".pop_RefershPg06Confirm").dialog("close");
	isEdited = false;
	checkedIndex = '';
	deleteYN = 'N';
	getSTEPPrjCodeSettingsList();
}

function refreshCode06Cancel() {
	$(".pop_RefershPg06Confirm").dialog("close");
}

function getIFCPrjCodeSettingsList(){
	$.ajax({
	    url: 'getIFCPrjCodeSettingsList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var rvsRS_pop_tbl = [];
	    	for(i=0;i<data[0].length;i++){
	    		rvsRS_pop_tbl.push({
	    			No: i,
	    			origin: 'update',
	    			ORIGIN_CODE: data[0][i].set_code_id,
	    			setOrder: data[0][i].set_order,
	    			CODE: data[0][i].set_code,
	    			desc: data[0][i].set_desc,
	    			bean: data[0][i]
	    		});
	    		setOrderArray[i] = data[0][i].set_order;
	    	}
	    	setRvsRSList(rvsRS_pop_tbl);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function IFCToExcel(){
	
//	rvsRSTable.download("xlsx", "data.xlsx", {sheetName:"IFC REVISED REASON"});
	
	let gridData = rvsRSTable.getData();
//	console.log(gridData);
	
	let jsonList = [];
	
	// json 형태로 변환하여 배열로 만듬
	for(let i=0;i<gridData.length;i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'IFC_Revised_Reason';
	
//	console.log(jsonList);
	
	var f = document.excelUploadForm_pg06_Ifc;
	
	f.prj_id06_Ifc.value = selectPrjId;
	f.prj_nm06_Ifc.value = getPrjInfo().full_nm;
    f.fileName06_Ifc.value = fileName;
   	f.jsonRowdatas06_Ifc.value = jsonList;
    f.action = "pg06IFCExcelDownload.do";
   	f.submit();
}
function IFCCodeCopy(){
	var IFC_CODE_COPY_CHECKBOX = $('#IFC_CODE_COPY_CHECKBOX').is(":checked");
	var IFCselectPrjId = $('#IFC_selectPrjId').val();
	if(IFCselectPrjId == null || IFCselectPrjId == "") {
		alert("Copy할 대상 프로젝트를 선택해 주세요");
	}else {
		if(IFC_CODE_COPY_CHECKBOX){
			$.ajax({
			    url: 'insertCopyPrjIFCCodeSettings.do',
			    type: 'POST',
			    data: {
			    	prj_id: selectPrjId,
			    	copy_prj_id: IFCselectPrjId
			    },
			    success: function onData (data) {
			    	getIFCPrjCodeSettingsList();
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}else {
			alert("Copy 체크박스를 선택해 주세요");
		}
	}
}
function IFCRowUp(callback){
	var thisGrid = rvsRSTable;
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getPrevRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getPrevRow().getIndex();
	
	selectedRow.move(targetIndex,true);
	
	thisGrid.getRow(selectedIndex).update({setOrder:(thisGrid.getRow(selectedIndex).getPosition()+1)+""});
	thisGrid.getRow(targetIndex).update({setOrder:(thisGrid.getRow(targetIndex).getPosition()+1)+""});
	
	thisGrid.redraw();
	
	var updateRow = [];
	updateRow.push(thisGrid.getRow(selectedIndex).getData());
	updateRow.push(thisGrid.getRow(targetIndex).getData());
	
	for(var i=0;i<updateRow.length;i++){
		$.ajax({
		    url: 'prjCodeSettingsUpdateCode.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	set_order : updateRow[i].setOrder,
				set_code_type : 'IFC REVISED REASON',
				set_code_id :updateRow[i].bean.set_code_id
		    },
		    success: function onData (data) {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
	
	callback(thisGrid,selectedIndex);
}
function IFCRowDown(callback){
	var thisGrid = rvsRSTable;
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getNextRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getNextRow().getIndex();
	
	selectedRow.move(targetIndex,false);
	
	thisGrid.getRow(selectedIndex).update({No:targetIndex,setOrder:(thisGrid.getRow(selectedIndex).getPosition()+1)+""});
	thisGrid.getRow(targetIndex).update({No:selectedIndex,setOrder:(thisGrid.getRow(targetIndex).getPosition()+1)+""});
	
	thisGrid.redraw();
	
	var updateRow = [];
	updateRow.push(thisGrid.getRow(selectedIndex).getData());
	updateRow.push(thisGrid.getRow(targetIndex).getData());
	
	for(var i=0;i<updateRow.length;i++)
		$.ajax({
		    url: 'prjCodeSettingsUpdateCode.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	set_order : updateRow[i].setOrder,
				set_code_type : 'IFC REVISED REASON',
				set_code_id :updateRow[i].bean.set_code_id
		    },
		    success: function onData (data) {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
	callback(thisGrid,targetIndex);
}
function IFCAdd(){
	addOrUpdate = 'add';
	var setOrder = Math.max.apply(null, setOrderArray)+1;
	var addrow =  { origin: 'add', setOrder: setOrder, CODE: '', desc: ''};
	rvsRSTable.addRow( addrow );
}
function IFCCodeRefresh(){
	getIFCPrjCodeSettingsList();
}
function IFCCodeApply(){
	var editedCells = rvsRSTable.getEditedCells();
	var set_code_arr = [];
	var selectTableData = rvsRSTable.getData();
	let descriptionisnull = '';
	for(i=0;i<selectTableData.length;i++){
		if(selectTableData[i].CODE!=''){
			set_code_arr[i] = selectTableData[i].CODE.toUpperCase();
		}
		
		if(selectTableData[i].CODE!=='' && selectTableData[i].desc===''){
			descriptionisnull = 'Description을 입력하지 않은 CODE는 저장할 수 없습니다.';
		}
	}
	var set_code_duplicate = isDuplicate(set_code_arr);
	if(set_code_duplicate){
		alert('CODE에 중복값이 있습니다.');
	}else if(descriptionisnull!==''){
		alert(descriptionisnull);
	}else{
		var RowDataArray = [];
		for(i=0;i<editedCells.length;i++){
			var RowData = editedCells[i].getRow().getData();
			RowDataArray.push(RowData);
		}
		RowDataArray = duplicateRemoveInArray(RowDataArray);
		let insertCode = '';
		let descisnull = '';
		
		$.ajax({ // p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT GENERAL > STEP 프로세스 정의 > IFC REVISED REASON",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		for(i=0;i<RowDataArray.length;i++){
	    	var set_code = RowDataArray[i].CODE.toUpperCase();
	    	var set_desc = RowDataArray[i].desc;
	    	var set_order = RowDataArray[i].setOrder;
	    	var origin_code = RowDataArray[i].ORIGIN_CODE;
	    	if(RowDataArray[i].origin==='update' && set_code!='' && set_desc!=''){
	    		updateIFCPrjCodeSettings(selectPrjId,set_code,set_desc,origin_code);
	    	}else if(RowDataArray[i].origin==='add' && set_code!='' && set_desc!=''){
	    		insertCode += set_code+'&'+set_desc+',';
	    	}
		}
		if(insertCode!==''){
			insertCode = insertCode.slice(0,-1);		
    		insertIFCPrjCodeSettings(selectPrjId,insertCode);
		}
		alert('Save Successfully');
		getIFCPrjCodeSettingsList();
	}
}
function insertIFCPrjCodeSettings(selectPrjId,insertCode){
	$.ajax({
	    url: 'insertIFCPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code: insertCode
	    },
	    async:false,
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function updateIFCPrjCodeSettings(selectPrjId,set_code,set_desc,origin_code){
	$.ajax({
	    url: 'updateIFCPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code: set_code,
	    	set_desc: set_desc,
	    	origin_code: origin_code
	    },
	    async:false,
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function IFCDeletePopup(){
	if($("input:checkbox[name='IFCList']:checked").length===0){
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}else{
	    $(".pop_IFCCodeDelete").dialog("open");
	}
}
function IFCDeleteCancel(){
    $(".pop_IFCCodeDelete").dialog("close");
}
function IFCDelete(){
	var checkedIndex = '';
	$("input:checkbox[name='IFCList']:checked").each(function(){
		checkedIndex += $(this).attr('id').replaceAll('IFCList','') + ',';
	});
	checkedIndex = checkedIndex.slice(0, -1);
	var checkedIndexArray = checkedIndex.split(',');
	var checkedCodeIdString = '(';
	for(i=0;i<checkedIndexArray.length;i++){
		checkedCodeIdString += "'" + rvsRSTable.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_CODE +"'" +',';
	}
	checkedCodeIdString = checkedCodeIdString.slice(0, -1)+')';
	
	$.ajax({ // p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "PROJECT GENERAL > STEP 프로세스 정의 > IFC REVISED REASON",
	    	method: "삭제"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'deleteIFCPrjCodeSettings.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code_id: checkedCodeIdString
	    },
	    success: function onData (data) {
	    	alert('삭제되었습니다.');
	        $(".pop_IFCCodeDelete").dialog("close");
	    	getIFCPrjCodeSettingsList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}