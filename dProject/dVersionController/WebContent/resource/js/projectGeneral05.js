/**
 * Project General 페이지 내에서,
 * 다섯 번째 스텝 : 코드값 정의
 * @param void 
 * @returns prj_info
 */
//pg05_에서 사용될 전역변수
var pg05 = { isFirst : true };
var selectPrjId = $('#selectPrjId').val();
var tmpGrid05;
var prj_grid_pg05_copy;

var isEdited = false;


function saveCheck05(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			isEdited = false;
			let idx = $("#pg05_codeType").find(":selected").index();
			getPg05Grid(idx)
	        return callback();
	    }
	    else{
	        return false;
	    }
	}
	return true;
}

function pgen_step05(){
	$(".proj_general").attr('id','general05');
	
	$("#pg05_codeType").val("#sel_CorRespon").prop("selected",true);
	$("#sel_CorRespon").css('display','flex');	
	
	// 해당 섹션 들어오면 무조건 delFlag를 없애준다.
	// delFalg는 del버튼을 눌렀을 경우의 제어를 위한 플래그
//	console.log("pg05");
	pg05.delFlag = false;
	
	if(!pg05.isFirst) return;
	
	pg05.isFirst = false;
	
	// 헤더에서 프로젝트 정보를 가져옴
	pg05.prjInfo = getPrjInfo();
	
	pg05.CodeType = [];
	for(var i=0;i<$("#pg05_codeType")[0].length;i++){
		var tmp = $("#pg05_codeType")[0][i].text;
		pg05.CodeType.push(tmp);
	}
	
	let idx = $("#pg05_codeType").find(":selected").index();
//	saveCheck05( function(){getPg05Grid(0)} );
	getPg05Grid(idx);

	// copy 체크박스 작동부분
	$('#PG05_selected_prj').click(function(){
    	$('#PG05_select_div').addClass('on');
    });
    $('.PG05_prj_select').click(function(){
    	var PG05_selected_prj_id = $(this).attr('id').replace('PG05_prj_id_is_','');
    	$('#PG05_selectPrjId').val(PG05_selected_prj_id);
    	var prj_nm = $(this).children().next().html();
    	$('#PG05_selected_prj').html(prj_nm);
    	$('#PG05_select_div').removeClass('on');
    });
    
    
    $("#pg05_copyChk").click(function(){
    	if($("#pg05_copyChk").is(":checked")) $(".copyDiv05").css('display','flex');
    	else $(".copyDiv05").css('display','none');
    });
    
    $(".copyChkdiv05").click(function(){
    	$("#pg05_copyChk").prop('checked', !$("#pg05_copyChk").is(":checked"));
    	if($("#pg05_copyChk").is(":checked")) $(".copyDiv05").css('display','flex');
    	else $(".copyDiv05").css('display','none');
    });
    
	pg05.btnList = $(".pg05_Btn");
	
	// COPY 버튼
	$(pg05.btnList[0]).click(function(){ pg05_Copy(); });
	// refresh
	$(pg05.btnList[1]).click(function(){
		if(isEdited == true) {
			$(".pop_RefershPg05Confirm").dialog("open");
//			if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
//				isEdited = false;
//				let idx = $("#pg05_codeType").find(":selected").index();
////				saveCheck05( function(){getPg05Grid(0)} );
//				getPg05Grid(idx);
//		    }
//		    else{
//		        return false;
//		    }
		}else if(isEdited == false) {
			let idx = $("#pg05_codeType").find(":selected").index();
//			saveCheck05( function(){getPg05Grid(0)} );
			getPg05Grid(idx);
		}
	});
	// apply
	$(pg05.btnList[2]).click(function(){ pg05_Apply(); });
	//Excel
	$(pg05.btnList[3]).click(function(){ pg05_ToExcel(); });
	// up
	$(pg05.btnList[4]).click(function(){ pg05_Up(function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);})});
	// down
	$(pg05.btnList[5]).click(function(){ pg05_Down(function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);})});
	// add
	$(pg05.btnList[6]).click(function(){ pg05_Add()});
	// delete
	$(pg05.btnList[7]).click(function(){ pg05_Del()});
	
	pg05.grid = []; // pg05의 각 그리드를 담는 배열
	initPg05Grid(function(i){getPg05Grid(i)});
	
	$("#pg05_codeType").on("change",function(){
		var i = $("#pg05_codeType").find(":selected").index();
		getPg05Grid(i);
	});
	
	$(".pop_RefershPg05Confirm").dialog({
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
	
	$(".pop_deletePg05Confirm").dialog({
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
    
//    getCorrResponList();
	
	initPrjSelectGridPg05Copy();
}

function refreshCode05Yes() {
	$(".pop_RefershPg05Confirm").dialog("close");
	isEdited = false;
	let idx = $("#pg05_codeType").find(":selected").index();
//	saveCheck05( function(){getPg05Grid(0)} );
	getPg05Grid(idx);
}

function refreshCode05Cancel() {
	$(".pop_RefershPg05Confirm").dialog("close");
}

function initPrjSelectGridPg05Copy() {
	prj_grid_pg05_copy  = new Tabulator("#prj_list_grid_pg05", {
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
	
	prj_grid_pg05_copy.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_pg05_copy.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_pg05_copy.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#PG05_selectPrjId").val(rowData.prj_id);
		$("#PG05_selected_prj").html(rowData.prj_nm);
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prjGen05_search') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 	prjGen05Search();
			    }
		}
	});
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjGen05Search() {
	let keyword = $("#prjGen05_search").val();
	let grid = prj_grid_pg05_copy;
	
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

// 각 그리드를 생성하고 전역변수로 할당시켜줌
function initPg05Grid(callback) {
	var firstCodeType = $("#pg05_codeType").find(":selected").index();
	//console.log($(".pg05_Grids").length);
	for(let i=0;i<$(".pg05_Grids").length;i++) {
		tmpGrid05 = new Tabulator(('#'+$(".pg05_Grids")[i].id), {
			layout: "fitColumns",
			placeholder: "No Data Set",
			index: "index",
			columns: [
				{
					title: "init",
					field: "init",
					hozAlign:"left",
			        headerSort:false
				}
			]
		});
		
		pg05.grid.push(tmpGrid05);
		// 그리드 생성 후, 콜백함수
		pg05.grid[i].on("tableBuilt", function(){setFieldPg05Grid(i)} );
	}
	
	callback(firstCodeType);
	
}

function setFieldPg05Grid(i) {
	// 그리드의 기본적인 필드셋
	
	// code_feature는 code를 의미하는 "C" value를 의미하는 "V"로 나뉜다.
	// code_type에 따라 C 혹은 V를 결정지어주어야 한다.

	pg05.grid[i].set_code_feature = "C";
	
	var tmpCol = [
		{title:"index", field:"index", visible:false},
		{	// 체크 박스
			// 커스텀 포매터
			title:"CK",
			formatter: function(cell, formatterParams, onRendered) {
				const data = cell.getRow().getData();
				
				var checkbox = document.createElement("input");
				checkbox.name = 'pg05_gridChk'+i;
				checkbox.type = 'checkbox';
				checkbox.value = data.index;
				
				return checkbox;
			},
			hozAlign:"center", 
			headerSort:false,
			width:10,
			download:false,
			cellClick:function(e, cell){
		        cell.getRow().toggleSelect();
		    }
		},
		{
			title: "Code",
			width:80,
			field: "set_code",
			editor: "input",
	        hozAlign:"left",
	        headerSort:false
		},
		{
			title: "Description",
			field: "set_desc",
			editor: "input",
			editable:true,
			cellEdited:function(cell){
	        	console.log(cell.isEdited());
	        	console.log("hi");	            
	        	},
	        hozAlign:"left",
	        headerSort:false
		},		
	];
	
	switch (i) {
	  case 0:
		  /*
		  var testCol ={
			        title: "test",
			        field: "test",
			        hozAlign:"left",
			        headerSort:false
			    };
		  tmpCol.push(testCol);
		  */
		  break;
	  default:
		  break;
	}
	
	 pg05.grid[i].setColumns(tmpCol);
	
	// 셀 edit 확인
	// 셀의 수정 값을 해당 row의 bean 값에도 반영해준다.
	// 반영된 bean은 apply 시, DB 업데이트할 정보들 이다.	
	pg05.grid[i].on("cellEdited",function(cell){
		var fieldName = cell.getField();
    	var tmpBean = cell.getData().bean;
    	tmpBean[fieldName] = cell.getData()[fieldName];
    	cell.getRow().update({bean:tmpBean});
    	
    	isEdited = true;
	});
}

function getPg05Grid(i) {
	// esc 눌렀을 경우, 선택해제 시키는 이벤트 바인딩
	$(document).keydown(function(event) {
	    if ( event.keyCode == 27 || event.which == 27 ) {
	    	pg05.grid[i].deselectRow();
	    }
	});
	
	pg05.delFalg = false;
	
	$.ajax({
		url: 'pg05_getCode.do',
	    type: 'POST',
	    data:{
	    	prj_id:pg05.prjInfo.id,
	    	set_code_type:pg05.CodeType[i]
	    },
	    success: function onData (data) {
	    	// 테이블 갱신시 edited 여부 초기화
	    	isEdited = false;
	    	// 테이블 갱신
	    	var inputData = [];
//	    	 console.log(data[0]);
	    	for(var i=0;i<data[0].length;i++){
	    		//console.log("i = "+data[0][i].set_code);
	    		var tmp ={};
	    		tmp.index = i;
	    		tmp.bean = data[0][i];
	    		tmp.set_code = data[0][i].set_code;
	    		tmp.set_desc = data[0][i].set_desc;
	    		inputData.push(tmp);
	    	}
	    	//console.log("inputData = "+inputData.length);
	    	// 코드타입에서 선택된 인덱스와 그리드를 일치시켜준다.
	    	var gridIndex = $("#pg05_codeType").find(":selected").index();
	    	
//	    	console.log(inputData);
//	    	pg05.grid[gridIndex].setData(inputData);
	    	
	    	if(pg05.grid[gridIndex].initialized) {
	    		pg05.grid[gridIndex].setData(inputData);
	    	}else {
	    		pg05.grid[gridIndex].on("tableBuilt",function(){
	    			this.setData(inputData);
	    		});
	    	}
	    	
	    	// 그리드 row 클릭 이벤트 바인딩
	    	pg05.grid[gridIndex].on("rowClick", function(e, row){
	    		// row 선택 전, 모든 선택된 row 해제
	    		// pg05.grid[gridIndex].deselectRow();
	    		this.deselectRow();
	    		// 더블클릭한 row 선택 활성화
	    		row.select();
	    		//row.toggleSelect();
	    	});
	    	// set_order에 의한 정렬
	    	//pg05.grid[gridIndex].setSort("index","asc");
	    	
	    	pg05.grid[gridIndex].redraw();
	    	
	    	if(gridIndex == 0) {
	    		$("#sel_CorRespon").css('display','flex');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function pg05_Copy() {
	
	var thisPrjId = pg05.prjInfo.id;
	var fromPrjId = $("#PG05_selectPrjId").val();
	var idx = $("#pg05_codeType").find(":selected").index();
	var code_type = pg05.CodeType[idx];
	
	if(fromPrjId == null || fromPrjId == "") {
		alert("Copy할 대상 프로젝트를 선택해 주세요");
	}else {
		$.ajax({
			url: 'pg05_copyCode.do',
		    type: 'POST',
		    traditional : true,
		    data:{
		    	prj_id: thisPrjId,
		    	copy_prj_id: fromPrjId,
		    	set_code_type:code_type	    	
		    },
		    success: function onData (data) {
		    	if(data.model.status == "OK"){
			    	alert('Copy가 완료 되었습니다.');
			    	var idx = $("#pg05_codeType").find(":selected").index();
			    	getPg05Grid(idx);
		    	}
		    	else{
		    		alert(data.model.error);
		    		var idx = $("#pg05_codeType").find(":selected").index();
			    	getPg05Grid(idx);
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}

function pg05_ToExcel() {	
	
	var gridIndex = $("#pg05_codeType").find(":selected").index();
	var set_code_type = pg05.CodeType[gridIndex];
	
	let gridData = pg05.grid[0].getData();
	
	let jsonList = [];
	
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	let fileName = '';
	
	if(set_code_type === "CORRESPONDENCE RESPONSIBILITY") {
		fileName = "Correspondence_Responsibility_Code";
	}
//	console.log(jsonList);
	
	var f = document.excelUploadForm_pg05;
	
//	console.log(set_code_type);
	
	f.prj_id05.value = prj_id;
	f.prj_nm05.value = getPrjInfo().full_nm;
    f.fileName05.value = fileName;
    f.jsonRowdatas05.value = jsonList;
    f.set_code_type05.value = set_code_type;
    f.action = "pg05ExcelDownload.do";
    f.submit();
}

function pg05_Up(callback) {
	//isEdited = true;
	
//	console.log("up버튼 클릭");
	var gridIndex = $("#pg05_codeType").find(":selected").index();
	var thisGrid = pg05.grid[gridIndex];
	
//	console.log("thisGrid = "+thisGrid.getData());
	
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getPrevRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getPrevRow().getIndex();
	
	var selectedBean = thisGrid.getRow(selectedIndex).getData().bean;
	var selectedOrd = Number(selectedBean.set_order);
	var targetBean = thisGrid.getRow(targetIndex).getData().bean;
	var targetOrd = Number(targetBean.set_order);
	
	selectedBean.set_order = targetOrd;
	targetBean.set_order = selectedOrd;
	
	thisGrid.getRow(selectedIndex).update({bean:selectedBean});
	thisGrid.getRow(targetIndex).update({bean:targetBean});
	
	var updateRows = [];
	
	updateRows.push( JSON.stringify(thisGrid.getRow(selectedIndex).getData().bean) );
	updateRows.push( JSON.stringify(thisGrid.getRow(targetIndex).getData().bean) );
	
	selectedRow.move(targetIndex,true);
	
	$.ajax({
	    url: 'pg05_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg05.prjInfo.id,
	    	jsonRowdatas:updateRows
	    },
	    success: function onData (data) {
	    	thisGrid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	callback(thisGrid,selectedIndex);
}

function pg05_Down(callback) {
	
//	console.log("Down 버튼 누름");
	var gridIndex = $("#pg05_codeType").find(":selected").index();
	var thisGrid = pg05.grid[gridIndex];
	
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getNextRow()) return;
	
	// 선택한 row의 인덱스
	var selectedIndex = selectedRow.getIndex();
	// 바꿀 row의 인덱스 (아래)
	var targetIndex = selectedRow.getNextRow().getIndex();
	
	// 바뀐 row의 실제 값들을 반영해야 하기 때문에 만듬
	var selectedBean = thisGrid.getRow(selectedIndex).getData().bean;
	var selectedOrd = Number(selectedBean.set_order);
	var targetBean = thisGrid.getRow(targetIndex).getData().bean;
	var targetOrd = Number(targetBean.set_order);
	
	selectedBean.set_order = targetOrd;
	targetBean.set_order = selectedOrd;
	
	thisGrid.getRow(selectedIndex).update({bean:selectedBean});
	thisGrid.getRow(targetIndex).update({bean:targetBean});
	
	var updateRows = [];
	
	updateRows.push( JSON.stringify(thisGrid.getRow(selectedIndex).getData().bean) );
	updateRows.push( JSON.stringify(thisGrid.getRow(targetIndex).getData().bean) );
	
	// 그리드 상에서 이동 (true는 up, false는 down)
	selectedRow.move(targetIndex,false);
	
	$.ajax({
	    url: 'pg05_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg05.prjInfo.id,
	    	jsonRowdatas:updateRows
	    },
	    success: function onData (data) {
	    	thisGrid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	callback(thisGrid,selectedIndex);
	
}

function pg05_Apply(){
	
	var gridIndex = $("#pg05_codeType").find(":selected").index();
	var thisGrid = pg05.grid[gridIndex];
	
	var gridList = thisGrid.getData();
	
	// 비어있는 그리드는 제외
	if(gridList.length < 1) return;
	
	var changedList = thisGrid.getEditedCells();
	var changedData = [];
	for(var i = 0 ; i<changedList.length ; i++){
		var tmpIdx = changedList[i].getData().index
		changedData.push(tmpIdx);
	}
	
	// 중복제거
	changedData = changedData.filter((val, idx) => { return changedData.indexOf(val) === idx; });
	// 변화가 있는 row들 json으로 변환
	var tmpStrings = [];
	var tmpGridData = thisGrid.getData();
	
	for(var i=0;i<changedData.length;i++){
		let idx = tmpGridData.findIndex(x=> x.index == changedData[i]);
		if(!tmpGridData[idx].bean.set_code){
			alert("code_val을 입력하지 않은 row 존재");
			return;
		}
		tmpStrings.push( JSON.stringify(tmpGridData[idx].bean) );
	}
	
	/*
	// 삭제된 row의 set_code_id를 배열로 넘겨준다.
	var deletedIds = [];
	if(pg05.delFlag)
	for(var i=0;i<thisGrid.deletedIds.length;i++) {
		// 삭제된 row의 code id
		deletedIds.push(thisGrid.deletedIds[i]);
	}
	*/
	
	$.ajax({ // p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "PROJECT GENERAL > LETTER, E-MAIL 코드값 정의",
	    	method: "저장"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'pg05_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg05.prjInfo.id,
	    	set_code_type:pg05.CodeType[gridIndex],
	    	jsonRowdatas:tmpStrings
	    	// deleteRows:deletedIds
	    },
	    success: function onData (data) {
	    	alert("Successfully saved");
	    	isEdited = false;
	    	getPg05Grid(gridIndex);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	thisGrid.deletedIdxList = [];
	
//	alert('성공적으로 저장/삭제가 완료 되었습니다.');
	
	// 삭제 후, 반영 완료하였으니 delFlag 다시 초기화
	pg05.delFlag = false;
}

function pg05_Add() {
	isEdited = true;
	
	let gridIndex = $("#pg05_codeType").find(":selected").index();
	let thisGrid = pg05.grid[gridIndex];
	
	let lastIndex = thisGrid.getData().length;
	let newSetOrd = pg05_getMaxOrder(thisGrid)+1;
	
	let tmp = {index:lastIndex,set_code:"",set_val:"",bean:{}};

	tmp.bean.set_order = newSetOrd;
	tmp.bean.set_code_feature = thisGrid.set_code_feature;
	tmp.isNew = true;
	
	
	thisGrid.addData(tmp,false,lastIndex);
	thisGrid.redraw();

}
//현재 그리드에서 code값의 최대값을 찾는다.
function pg05_getMaxId(grid) {
	
	var max = 0;
	var gridData = grid.getData();
	
	for(var i=0 ; i < gridData.length ; i++) {
		var tmpBean = gridData[i].bean;
		if(Number(tmpBean.set_code_id) > max) max = Number(tmpBean.set_code_id);
	}
	
	return String(max).padStart(5,"0");
	
}

function pg05_getMaxOrder(grid) {
	
	var max = 0;
	var gridData = grid.getData();
	
	for(var i=0 ; i < gridData.length ; i++) {
		var tmpBean = gridData[i].bean;
		if(tmpBean.set_order > max) max = tmpBean.set_order;
	}
	
	return max;
}

function pg05_Del() {
		
		$(".pop_deletePg05Confirm").dialog("open");

	    $('#deleteCode05Cancel').click(function() {
	    	$(".pop_deletePg05Confirm").dialog("close");
	    });
}

function deleteCode05Yes() {
	var gridIndex = $("#pg05_codeType").find(":selected").index();
	var thisGrid = pg05.grid[gridIndex];

	
	thisGrid.deletedIdxList = []; // 삭제된 row의 index
	thisGrid.deletedIds = []; // 삭제된 row의 실제 code_id
	// 체크박스 selector name
	var chkString = "input:checkbox[name='pg05_gridChk" + gridIndex + "']:checked";
	var chk = $(chkString);

	var delChkIdxList = [];

	if(chk.length ===0) {
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
		return;
	}
	

	for (var i = 0; i < chk.length; i++) {
		var idx = Number(chk[i].value); // index 숫자로 변환
		delChkIdxList.push(idx);
	}
	
	let delRowsJSON = [];
	for (let i = 0; i < delChkIdxList.length; i++) {
		let tmp = thisGrid.getRowFromPosition(delChkIdxList[i]).getData();
		delRowsJSON.push(JSON.stringify(tmp.bean));
	}

	$('.pop_deletePg05Confirm').dialog("close");
	$.ajax({ // p_prj_sys_log에 정보 저장
		url : 'insertSystemLog.do',
		type : 'POST',
		data : {
			prj_id : $('#selectPrjId').val(),
			pgm_nm : "PROJECT GENERAL > LETTER, E-MAIL 코드값 정의",
			method : "삭제"
		},
		success : function onData() {

		},
		error : function onError(error) {
			console.error(error);
		}
	});

	$.ajax({
		type : 'POST',
		url : 'pg05_del.do',
		traditional : true,
		data : {
			jsonRowdatas : delRowsJSON
		},
		async : false,
		success : function(data) {
			pg05.grid[0].deleteRow(delChkIdxList);
			alert("Delete complete.");
		},
		error : function onError(error) {
			console.error(error);
		}
	});
	
}

// function getCorrResponList() {
// $.ajax({
// url: 'getCorrResponList.do',
//	    type: 'POST',
////	    data: {
////	    	prj_id: selectPrjId,
////	    	set_code_type: set_code_type
////	    }
//	    success: function onData (data) {
//	    	var corrResponList = [];
//	    	for(i=0;i<data[0].length;i++) {
//	    		corrResponList.push({
//	    			No: i,
//	    			origin: 'update',
//	    			ORIGIN_STEP: data[0][i].set_code_id,
//	    			setOrder: data[0][i].set_order,
//	    			STEP: data[0][i].set_code,
//	    	        ifc: data[0][i].ifc_yn,
//	    			Description: data[0][i].set_desc,
//	    	        progRate: data[0][i].set_val,
//	    	        actuDate: data[0][i].set_val2,
//	    	        bean: data[0][i]
//	    		});
//	    	}
//	    	setCorrRespon(corrResponList);
//	    }
//	});
//}
//
//function setCorrRespon(corrResponList) {
//	corrResponTable = new Tabulator("#corrRespon", {
//    	index: "No",	//No 필드를 인덱스 필드로 설정
//        data: corrResponList,
//        layout: "fitColumns",
//        selectable:1,
//        heigth:"100%",
//        columns: [
//        	{
//        		title: "No",
//                field: "No",
//                visible:false
//        	},
//        	{
//        		title: "CK",
//        		formatter: function(cell, formatterParams, onRendered) {
//        			var checkbox = document.createElement("input");
//        			checkbox.classList.add('CodeList');
//        			checkbox.id = 'CodeList'+cell.getRow().getPosition();
//        			checkbox.name = 'CodeList';
//        			checkbox.type = 'checkbox';
//        			return checkbox;
//        		},
//        		hozAling: "center",
//        		headerSort:false,
//        		width:10,
//        		cellClick:function(e, cell) {
//        			cell.getRow().toggleSelect();
//        		}
//        	},
//        	{
//        		title: "Code",
//        		field: "code"
//        	},
//        	{
//        		title: "Description",
//        		field: "description"
//        	}
//        ]	
//        	
//        	
//	});    	
//}