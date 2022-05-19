
var selectPrjId = $('#selectPrjId').val();
var clickTb;
var drn_id;
var drn_no;
var drn_title;
var reg_date;
var status;
var doc_title;
var collabo_prjGrid;


var collabo_loading = '<div id="collabo_loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';

var collaboration = {};

$(document).ready(function(){
	collaboration.status = {};
	
	
	// 임지저장(T), 결재상신, 진행중(P), 완료(C), 지연현황(D), 반려(R)
	collaboration.status = {
			"기안함":0,
			"작성중":1,
			"결재함":2,
			"미결":3,
			"예고":4,
			"진행":5,
			"반려":6,
			"완료":7,
			"전체현황함":8,
			"지연현황함":9
	};
	
	collaboration.modeList = {
			"":0,
			"T":1,
			"A":2,
			"E":3,
			"1":4,
			"P":5,
			"R":6,
			"C":7,
	};
	
	collaboration.title = $("#collabo_Title").text();
	let tmp = $("#collabo_Title").text();
	if(tmp.lastIndexOf("(") > 0){ // 문자열에 "("를 포함한 경우
		collaboration.title = tmp.substr(0 , tmp.lastIndexOf("(")-1 );
	}
	
	console.log(collaboration.title);
	//
	
	collaboration.mode = collaboration.status[collaboration.title]; 
	
	
	initCollaboPage();
	getCollaboPage(null);
	
	// 모든 프로젝트 리스트를 필터 목록에 추가
	collabo_initPrjSelectGrid();
	/*
	let prjList = prj_grid.getData();
	
	$.ajax({
		type: "POST", 
		url: 'getAllPrjList_DRN_filter.do',
		data:{
		},
		success: function onData (data) {
			let prjList;
			
			if(data.model.prj_list.length>0){
				prjList = data.model.prj_list;
				
				for(let i=0;i<prjList.length;i++){
					let opt = document.createElement("option");
					opt.value = prjList[i].prj_id;
					opt.text = prjList[i].prj_no;
					$("#collabo_filterPrj").append(opt);
				}
				
			}
			
			else{
				prjList = prj_grid.getData();
				for(let i=0;i<prjList.length;i++){
					let opt = document.createElement("option");
					opt.value = prjList[i].prj_id;
					opt.text = prjList[i].prj_no;
					$("#collabo_filterPrj").append(opt);
				}
			}
			
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
	*/
	
	
	// 세션 유저의 프로젝트 리스트만 필터 목록에 추가
	/*
	let prjList = prj_grid.getData();
	for(let i=0;i<prjList.length;i++){
		let opt = document.createElement("option");
		opt.value = prjList[i].prj_id;
		opt.text = prjList[i].prj_no;
		$("#collabo_filterPrj").append(opt);
	}
	*/

});


function collabo_initPrjSelectGrid() {
	collabo_prjGrid = new Tabulator("#prj_list_grid_collabo", {
		layout : "fitColumns",
		placeholder : "There is No Project",
		selectable : 1,
		index : "prj_id",
		height : 180,
		columns : [ {
			title : "index",
			field : "prj_id",
			visible : false
		}, {
			title : "Project No",
			field : "prj_no",
			headerSort : false,
		}, {
			title : "Project Name",
			field : "prj_nm",
			headerSort : false,
		}, {
			title : "Project Full Name",
			field : "prj_full_nm",
			headerSort : false,
		}, ]
	});

	collabo_prjGrid.on("tableBuilt", function() {
		$.ajax({
			url : 'getPrjGridList.do',
			type : 'POST',
			success : function onData(data) {
				collabo_prjGrid.setData(data.model.prjList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	});

	collabo_prjGrid.on("rowClick", function(e, row) {
		let rowData = row.getData();
		$("#collabo_selectPrjId").val(rowData.prj_id);
		$("#collabo_selected_prj").html(rowData.prj_no);
		$("#collabo_select_div").removeClass('on');
		collabo_prjFilter();

	});

	$(document).keydown(function(event) {
		// 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if (document.getElementById('collabo_search') == focusEle) {
			if (event.keyCode == 13 || event.which == 13) {
				prjSearchCollabo();
			}
		}
	});
}

function prjSearchCollabo() {
	let keyword = $("#collabo_search").val();
	let grid = collabo_prjGrid;

	$.ajax({
		url : 'prjSearch.do',
		type : 'POST',
		data : {
			keyword : keyword 
		},
		success : function onData(data) {
			grid.setData(data.model.prjList);
			grid.redraw();
		},
		error : function onError(error) {
			console.error(error);
		}
	});
}


function collabo_prjAllSelect(){
	$("#collabo_selectPrjId").val('all');
	$("#collabo_selected_prj").html("All Project");
	$("#collabo_select_div").removeClass('on');
	collabo_prjFilter();
}
/**
 * DRN 상신 버튼
 * @returns
 */
function drnClick(){
	
	// @params : pageMode,docData,discipObj,folder_id,pageData
	// drnUp(0);
	
	alert("Please create it at DOCUMENT CONTROL.");
	
}


function collabo_prjFilter(){
	// let prj_val = $("#collabo_filterPrj").val();
	let prj_val = $("#collabo_selectPrjId").val();
	if(collaboration.grid.initialized){
		if(prj_val == "all")
			collaboration.grid.setFilter(false);
		else
			collaboration.grid.setFilter("prj_id", "=", prj_val);
	}
	collaboration.grid.redraw();
}


function initCollaboPage(){
	let table = new Tabulator("#collaboList", {
		selectable:1,//true
        layout: "fitColumns",
        placeholder: "No Data Set",
//      pagination : true,
//		paginationSize : 100,
        height:600,
        columns: [{
        		title: "DRN NO",
        		field: "drn_no",
        		width: 250,
        		hozAlign: "left"
        	},
        	{
        		title: "DRN TITLE",
        		field: "drn_title",
        		hozAlign: "left"
        	},
        	{
        		title: "PERSON IN CHARGE",
        		field: "person_in_charge",
        		hozAlign: "left",
        		width:140,
        		formatter:function(cell, formatterParams, onRendered){
				    let rowData = cell.getData();
				    
				    let approval_status = rowData.drn_approval_status;
				    let person_in_charge = "";
				    switch(approval_status){
				    	case "T" : 
				    		person_in_charge = rowData.design;
				    		break;
				    	case "A":
				    		person_in_charge = rowData.dcc;
				    		break;
				    	case "E":
				    		person_in_charge = rowData.review;
				    		break;
				    	case "1":
				    	case "C":
				    		person_in_charge = rowData.approve;
				    		break;
				    	case "R":
				    		if(rowData.modifier == "" || rowData.modifier == null)
				    			person_in_charge = "DCC";
				    		else
				    			person_in_charge = rowData.modifier;
				    		break;
				    }
				    if(person_in_charge && person_in_charge != "")
				    	person_in_charge = person_in_charge.trim();
				    
				    
				    return person_in_charge;
				},
        	},
        	{
        		title: "STATUS",
        		field: "drn_approval_status",
        		hozAlign: "left",
        		visible:false,
        		width:110,
        		formatter:function(cell, formatterParams, onRendered){
				    let status = cell.getValue();
				    
				    switch(status){
					    case "T" :
					    	status = "임시저장";
					    	break;
					    case "A" : 
							status = "DCC 검토중";
					    	break;
					    case "E" : 
							status = "REVIEW 결재중";
					    	break;
					    case "1" : 
					    	status = "APPROVE 결재중";
					    	break;
					    case "C" :
					    	status = "결재완료";
					    	break;
					    case "R" : 
					    	status = "반려";
					    	break;
					    default :
					    	status = status;
					    	break;
				    }
				    return status;
				},
        		visible:true
        	},
        	{
        		title: "기안일",
        		field: "reg_date",
        		dir:"desc",
        		width: 120,
        		formatter:function(cell, formatterParams, onRendered){
        		    	
        			const tmp = cell.getValue();

        		    return tmp.substr(0,4) + '-' + tmp.substr(4,2) + '-' + tmp.substr(6,2) + ' ' + tmp.substr(8,2) + ':'  + tmp.substr(10,2) + ':' +tmp.substr(12,2)
        		},
        	}
        ]
	});
	
	
	collaboration.grid = table;
	
	
	collaboration.grid.on("tableBuilt",function(){
		getDrnList();
	});
	
	
	collaboration.grid.on("rowClick", function(e, row){
		
		let rowData = row.getData();
		let _prj_id = rowData.prj_id;
		//rowData.folder_id = folder_id;
		console.log(_prj_id);
		
		$.ajax({
			type: "POST", 
			url: 'get_drnPage.do',
			data:{
				jsonData:JSON.stringify(rowData)
			},
			success: function onData (data) {
				// console.log(data.model.drnInfo.drn_approval_status);
				
				// 폴더의 인덱스 확인
				// let idx = folderList.findIndex(i=>i.id == rowData.folder_id);
				
				
				// let folder_id = getFolderId(rowData.drn_id);
				let discipObj = getDiscipObj(rowData.folder_id,rowData.prj_id);
				
				
		    	let docData = data.model.docData;
		    	let pageData = data.model;
		    	//let mode = collaboration.modeList[data.model.drnInfo.drn_approval_status];
		    	//let mode = collaboration.status[collaboration.title];
		    	
		    	let tmp = $("#collabo_Title").text();
		    	if(tmp.lastIndexOf("(") > 0) // 문자열에 "("를 포함한 경우
		    		tmp = tmp.substr(0 , tmp.lastIndexOf("(")-1 );
		    	
		    	let mode = collaboration.status[tmp] ;
		    	if( !(mode == 1 || mode == 3) ) mode = "read";

		    	drnPopUP(mode , docData , discipObj , rowData.folder_id, pageData,null,_prj_id);
		    	//drnUp(mode , docData , discipObj , rowData.folder_id, pageData);
		    	// getDrnList();
			},
			error: function onError (error) {
		        console.error(error);
		    }
		});
		
		
		
	});
	
	
}





function getCollaboPage(title){
	/*
	if(title.includes("전체현황함")){
		let col = collaboration.grid.getColumn("drn_approval_status");
		col.updateDefinition({visible:true});
	}else{
		let col = collaboration.grid.getColumn("drn_approval_status");
		col.updateDefinition({visible:false});
	}
	*/
	
	
	if(title!=null)
		$("#collabo_Title").text(title);
		
	collaboration.title = $("#collabo_Title").text();
	if(collaboration.title !="기안함"){
		$("#collaboDrnBtn").css("display","none");
	}else if(collaboration.title =="기안함"){
		$("#collaboDrnBtn").css("display","inline-block");
	}
	
	$("#collaboFrom").val("");
	$("#collaboTo").val("");
	
	getDrnList();

}

function getDrnList(){
	
	
	let from = $("#collaboFrom").val();
	let to = $("#collaboTo").val();
	//$("#collabo_filterPrj").val("all");
	$("#collabo_selectPrjId").val("all");
	$("#collabo_selected_prj").html("All Project");
	
	
	
	$.ajax({
		url: 'getCollaboGrid.do',
	    type: 'POST',
	    beforeSend:function(){
		    $('body').prepend(collabo_loading);
		 },
		complete:function(){
		    $('#collabo_loading').remove();
		 },
	    data: {
	    	prj_id: getPrjInfo().id,
	    	status: collaboration.title,
	    	from:from ,
	    	to: to
	    },
	    success: function onData (data) {
	    	$('#collabo_loading').remove();
	    	
	    	collaboration.grid.setData(data.model.getCollaboList);
	    	collaboration.grid.setFilter(false);
	    	collaboration.grid.redraw();
	    	
	    	if(collaboration.title.includes("전체현황함")){
	    		let col = collaboration.grid.getColumn("drn_approval_status");
	    		col.updateDefinition({visible:true});
	    		let col2 = collaboration.grid.getColumn("person_in_charge");
	    		col2.updateDefinition({visible:true});
	    		// 프로젝트 선택 div 영역 활성화
	    		$("#collabo_filterPrjDiv")[0].style["display"] = "flex";
	    	}else{
	    		let col = collaboration.grid.getColumn("drn_approval_status");
	    		col.updateDefinition({visible:false});
	    		let col2 = collaboration.grid.getColumn("person_in_charge");
	    		col2.updateDefinition({visible:false});
	    		
	    		// 프로젝트 선택 div 영역 비활성화
	    		if(collaboration.title.includes("지연현황함"))
	    			$("#collabo_filterPrjDiv")[0].style["display"] = "flex";
	    		else
	    			$("#collabo_filterPrjDiv")[0].style["display"] = "none";
	    	}
	    	
	    	folderList = getDRNsCount(folderList);
	    	let tmp_title = $("#collabo_Title").text();
	    	let tree_tmp = $("#tree").jstree(true).settings.core.data;
	    	let cnt = collaboration.grid.getData().length;
	    	
	    	if(tmp_title.includes("작성중")){
	    		tmp_title = "작성중";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    		
	    	}else if(tmp_title.includes("미결")){
	    		tmp_title = "미결";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}else if(tmp_title.includes("예고")){
	    		tmp_title = "예고";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}else if(tmp_title.includes("진행")){
	    		tmp_title = "진행";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}else if(tmp_title.includes("반려")){
	    		tmp_title = "반려";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}else if(tmp_title.includes("완료")){
	    		tmp_title = "완료";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}else if(tmp_title.includes("전체현황함")){
	    		tmp_title = "전체현황함";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}else if(tmp_title.includes("지연현황함")){
	    		tmp_title = "지연현황함";
	    		let id = tree_tmp.find(i=>i.text.includes(tmp_title)).id;
	    		
	    		if(cnt>0) tmp_title += (" " + "("+cnt+")"); 
	    		$("#tree").jstree(true)._model.data[id].text = tmp_title;
	    	}
	    	
	    	
	    	
	    	$("#collabo_Title").text(tmp_title);
	    	
	    	$("#tree").jstree(true).settings.core.data = folderList;
	    	$("#tree").jstree(true).redraw(true);
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	/*
	$.ajax({
		url: 'getCollaboList.do',
	    type: 'POST',
	    data: {
	    	prj_id: getPrjInfo().id,
	    	status: status,
	    	from:from ,
	    	to: to
	    },
	    success: function onData (data) {
	    	collaboration.grid.setData(data.model.getCollaboList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	*/
	
}

function getFolderId(drn_id){
	let folder_id = -1;
	
	$.ajax({
		url: 'getThisDRNFolderId.do',
	    type: 'POST',
	    async:false,
	    data: {
	    	prj_id: getPrjInfo().id,
	    	drn_id:drn_id
	    },
	    success: function onData (data) {
	    	folder_id = data.model.folder_id;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	return folder_id;
	
}

function getDiscipObj(folder_id,prj_id){
	
	let discipObj = {};

	
	
	$.ajax({
		url: 'getDiscipObj.do',
	    type: 'POST',
	    async:false,
	    data: {
	    	prj_id: prj_id,
	    	folder_id:folder_id
	    },
	    success: function onData (data) {
	    	let result = data.model.discipObj;
	    	discipObj.code = result.discip_code;
	    	discipObj.display = result.discip_display;
	    	discipObj.code_id = result.discip_code_id;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	return discipObj;
}


//날짜 객체를 받아서 문자열로 리턴
function getDateStr(myDate) {
	
	let year = myDate.getFullYear()
	let month =myDate.getMonth() + 1
	let day = myDate.getDate();
	
	return (year + '-' + (month+"").padStart(2,'0') + '-' + (day+"").padStart(2,'0'))
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


$("#search_allW").click(function(){
	$("#collaboFrom").val("");
	$("#collaboTo").val("");
});

$("#search_weekW").click(function(){
	$("#collaboFrom").val(lastWeek());
	$("#collaboTo").val(today());
});

$("#search_monthW").click(function(){
	$("#collaboFrom").val(lastMonth());
	$("#collaboTo").val(today());
});









