/* 
 * 작성자		: 권도일
 * 작성일		: 2021-11-22
 * 메소드명	: globalSearch.js
 * 설명		: 검색 제어 스크립트
 */

var docListTbl;
var trListTbl;
var drnListTbl;

var search_downloadGrid;


var tableData;

var searchKey;  // 검색어
var prj_List ="(";	// 모든 프로젝트의 id 리스트 (쿼리용 스트링 형태)

var dialog_FolderList;
var selected_Folder;
var idPath; // 선택된 폴더의 경로 id값

var search_loading = '<div id="search_loading" class="loading">Searching...<img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';

function getTree4Dialog(){
	var folderList = getFolderList();
	folderList = dccFolder(folderList);
	
	var tree = $('#tree_pop').jstree({
	    "core" : {
	   	 'data' : folderList,
	      }
	   });
	return folderList;

}

function dccFolder(folderList){
	var folder = [];
	for(let i=0;i<folderList.length;i++)
		if(folderList[i].type === "DCC")
			folder.push(folderList[i]);	
	
	return folder;
	
}

// 탭이 닫혔을 때, 다이얼로그 창을 초기화 하기 위함
function removeTabGLOBALSERCH(){
	$('.pop_doc_search').remove();
}


$(document).ready(function() {

	init();
	
	
	
	search_downloadGrid = new Tabulator("#search_docDown_downloadListGrid", {
		layout : "fitColumns",
		//placeholder : "There is No Document to download",
		selectable : false,
		maxHeight : 150,
	    pagination : true,
		paginationSize : 200,
		columns : [
			{
				title : "Project",
				field : "prj_nm",
				hozAlign: "left",
				visible : true
			},
			{
				title : "Doc",
				field : "file_type",
				visible : true,
				hozAlign: "center",
				headerSort:false,
				resizable:false,
				formatter:function(cell, formatterParams, onRendered){
				    let type = cell.getValue();
				    let imgIcon;
				    
				    switch(type){
				    	// 이미지 아이콘
				    	case "png":
				    	case "jpg":
				    	case "jpeg":
				    	case "gif":
				    		imgIcon = "<img src='../resource/images/docicon/picture.png' class='docDown_img'>";
				    		break;
				    	// pdf 아이콘
				    	case "pdf":
				    		imgIcon = "<img src='../resource/images/docicon/pdf.ico' class='docDown_img'>";
				    		break;
				    	// ppt 아이콘
				    	case "ppt":
				    	case "pptx":
				    		imgIcon = "<img src='../resource/images/docicon/ppt.ico' class='docDown_img'>";
				    		break;
				    	// 워드 아이콘	
				    	case "doc":
				    	case "docx":
				    		imgIcon = "<img src='../resource/images/docicon/doc.ico' class='docDown_img'>";
				    		break;
				    	// 엑셀 아이콘
				    	case "xls":
				    	case "xlsx":
				    	case "csv":
				    		imgIcon = "<img src='../resource/images/docicon/xls.ico' class='docDown_img'>";
				    		break;
				    	// txt 아이콘
				    	case "txt":
				    		imgIcon = "<img src='../resource/images/docicon/txt.ico' class='docDown_img'>";
				    		break;
				    	// hwp 아이콘
				    	case "hwp":
				    		imgIcon = "<img src='../resource/images/docicon/hwp.ico' class='docDown_img'>";
				    		break;
				    	// dwg 아이콘
				    	case "dwg":
				    		imgIcon = "<img src='../resource/images/docicon/dwg.ico' class='docDown_img'>";
				    		break;
				    	// zip 아이콘
				    	case "zip":
				    		imgIcon = "<img src='../resource/images/docicon/zip.ico' class='docDown_img'>";
				    		break;
				    	// 알 수 없는 확장자	
				    	default:
				    		imgIcon = "<img src='../resource/images/docicon/question-mark.png' class='docDown_img'>";
				    		break;
				    }
				    
				    return imgIcon;
				},
				width : 50
			},
			{
				title : "Doc.No",
				field : "doc_no",
				hozAlign: "left",
				visible : true
			},
			{
				title : "Revision",
				field : "rev_no",
				visible : true,
				hozAlign: "center",
				width : 80
			},
			{
				title : "Title",
				field : "title",
				hozAlign: "left",
				visible : true
			},
			{
				title : "Modified",
				field : "mod_date",
				hozAlign: "left",
				formatter:function(cell, formatterParams, onRendered){
				    let date = cell.getValue();
				    
				    if(!date || date == "") return date;
				    
				    // yyyy-MM-dd HH:mm:SS 형태로 리턴
				    date = date.substr(0,4) + "-" + date.substr(4,2) + "-" + date.substr(6,2) + " " 
				    	+ date.substr(8,2) + ":" + date.substr(10,2) + ":" + date.substr(12,2);
				    
				    return date;
				},
				visible : true,
				width: 120
			},
			{
				title : "Size",
				field : "file_size",
				hozAlign: "left",
				formatter:function(cell, formatterParams, onRendered){
				    let size = cell.getValue();
				    size = Number(size);
				    size = formatBytes(size, 2);
				    return size;
				},
				visible : true,
				width: 90
			}
		]
	});
	
	
	let defaultSet = "%DOCNO%_%REVNO%_%TITLE%";
	$("#search_docDown_fileRenameSet").attr("disabled",true);
	$("#search_docDown_fileRenameSet").val(defaultSet);
	$("#search_docDown_downloadFileRename")[0].checked = false;
	
	
	
	
	// dialog init	 
	 $("#search_docDown_renm").dialog({
	        autoOpen: false,
	        modal:true,
	        maxWidth: 1000,
	        width: 800
	  });
	
	
	
	let searchKey = $("#globalSearchInput").val();
	$("#searchKeyword").val(searchKey);
	
	search();
	
	$(".date").datepicker({ dateFormat: 'yy-mm-dd' });
});


function search_download(){
	// rename 그리드 초기화
	search_downloadGrid.setData(null);
	search_downloadGrid.redraw();
	
	let selectedList = docListTbl.getSelectedData();
	
	if(selectedList.length<1){
		alert("There is no checked Document. Please select the document to download.");
		return;
	}
	
	
	
	
	search_downloadGrid.setData(selectedList);
	search_downloadGrid.redraw();
	
	$("#search_docDown_totalCountLabel").text("Total "+ selectedList.length +" Files");
	
	 $("#search_docDown_renm").dialog("open");
}



function search_docDown_fileRenameCheck(){
	let checked = $("#search_docDown_downloadFileRename")[0].checked
	if(checked){
		$("#search_docDown_fileRenameSet").attr("disabled",false);
	}else{
		let defaultSet = "%DOCNO%_%REVNO%_%TITLE%";
		$("#search_docDown_fileRenameSet").attr("disabled",true);
		$("#search_docDown_fileRenameSet").val(defaultSet);
	}
	
}


function search_docDown_fileDownloadClose(){
	 $("#search_docDown_renm").dialog("close");
}


function search_docDownload(){
	
	
	let selectedDocs = search_downloadGrid.getData();
	
	if(selectedDocs.length<1) {
		alert("Select the doc to download");
		return;
	}
	

	let jsonStrings = [];
	for(let i=0;i<selectedDocs.length;i++){
		let json = JSON.stringify(selectedDocs[i]);
		jsonStrings.push(json);
	}
	
	// submit을 위한 formdata 재구성 전, 이전 값 클리어
	$("#search_docDown_form [name='prj_id']").remove();
	$("#search_docDown_form [name='fileRenameSet']").remove();
	$("#search_docDown_form [name='jsonRowDatas']").remove();
	
	
	// submit을 위한 form data 재구성
	var form = $("#search_docDown_form")[0];
	
	var paramsInput_fileName = document.createElement('input');
	paramsInput_fileName.setAttribute('type','hidden');
	paramsInput_fileName.setAttribute('name','fileRenameSet');
	paramsInput_fileName.setAttribute('value',$("#search_docDown_fileRenameSet").val());
	form.appendChild(paramsInput_fileName);
	
	for(let i=0;i<jsonStrings.length;i++){
		var paramsInput_json = document.createElement('input');
		paramsInput_json.setAttribute('type','hidden');
		paramsInput_json.setAttribute('name','jsonRowDatas');
		paramsInput_json.setAttribute('value',jsonStrings[i]);
		form.appendChild(paramsInput_json);
	}
	


}



$(document).on("submit", "form.fileDownloadForm", function (e) {
	search_docDownload();
    $.fileDownload($(this).prop('action'), {
        successCallback: function (url) {
        	 
            //alert("성공");
        },
        preparingMessageHtml: "We are preparing download file, please wait...",
        failMessageHtml: "There was a problem generating your file, please try again.",
        httpMethod: "POST",
        data: $(this).serialize()
    });
    e.preventDefault();
});


/**
 * All project 체크박스 체크시,
 * 모든 프로젝트의 아이디를 구해준다.
 * @param void 
 * @returns prj_id list
 */
function getAllPrj_IDs(){
	var tmp_List = [];
	for(let i=0 ; i < $(".prj_select").length; i++){
		var tmp = $(".prj_select")[i].id;
		tmp = tmp.replace("prj_id_is_",""); // 앞의 접두어 제거, 순수 prj id만 구함
		tmp = parseInt(tmp); // int형으로 변환
		tmp_List.push(tmp);	// prj리스트에 추가
		prj_List += (i != $(".prj_select").length-1 ? tmp+"," : tmp);
	}
	prj_List += ")";	
	
}



function search_excel(){
	// 데이터 get
	let docData = docListTbl.getData();
	let trData = trListTbl.getData();
	let drnData = drnListTbl.getData();
	
	// json array 변환
	let docArr = [];
	let docStr = "";
	for(let i=0;i<docData.length;i++){
		let tmp = JSON.stringify(docData[i]);
		docStr += (tmp + "@,@");
		docArr.push(tmp);
	}
	let trArr = [];
	let trStr = "";
	for(let i=0;i<trData.length;i++){
		let tmp = JSON.stringify(trData[i]);
		trStr += (tmp + "@,@");
		trArr.push(tmp);
	}
	let drnArr = [];
	let drnStr = "";
	for(let i=0;i<drnData.length;i++){
		let tmp = JSON.stringify(drnData[i]);
		drnStr += (tmp + "@,@");
		drnArr.push(tmp);
	}
	
	
	
    var f = document.searchExcelForm; 

    
    f.docJson.value = docArr;
    f.trJson.value = trArr;
    f.drnJson.value = drnArr;
    f.docJsonStr.value = docStr;
    f.trJsonStr.value = trStr;
    f.drnJsonStr.value = drnStr;
    f.action = "search_excel.do";
    f.submit();   
	
	
	/*
	$.ajax({
	    url: 'search_excel.do',
	    type: 'POST',
	    traditional : true,
	    beforeSend:function(){
		    $('body').prepend(search_loading);
		 },
		complete:function(){
		    $('#search_loading').remove();
		 },
	    data:{
	    	docJson:docArr,
	    	trJson:trArr,
	    	drnJson:drnArr
	    },
	    success: function onData (data) {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	*/ 
	
	
}


function search(){
	
	var search_Key = $("#searchKeyword").val();
	var search_Opt = $("#search_Opt1").val();
	var max_Display = $("#max_Display").val();
	
	/*
	if(search_Opt === "like")
		search_Key = "%" + search_Key + "%";
	else if(search_Opt === "start with")
		search_Key += "%";
	*/
	var allPrjchk = $("#chkAllPrj").is(":checked");
	var periodChk = $("#periodChk").is(':checked');
	
	
	let basic_input =  $("input.basic_cond");
	let basic_sel =  $("select.basic_cond");
	
	
	let detail_input = $("input.detail_cond");
	let detail_sel = $("select.detail_cond");

	
	if(periodChk){
		var from = $("#searchFrom").val();
		from = from.replaceAll('-',''); 
		from = from.substr(0,4) + from.substr(4,2) + from.substr(6,2) + "000000";
		var to = $("#searchTo").val();
		to =  to.replaceAll('-','');
		to =  to.substr(0,4) + to.substr(4,2) + to.substr(6,2) + "235959";
	}
	
	var mod_from = detail_input[2].value;
	var mod_to = detail_input[3].value;
	
	if($("#modDateChk")[0].checked){
		mod_from = mod_from.replaceAll('-',''); 
		mod_from = mod_from.substr(0,4) + mod_from.substr(4,2) + mod_from.substr(6,2);
		mod_to =  mod_to.replaceAll('-','');
		mod_to =  mod_to.substr(0,4) + mod_to.substr(4,2) + mod_to.substr(6,2);
	}
	
	
	
	// 검색어가 입력되지 않은 경우 예외처리
	// (keyword || doc_no || title) 최소한 셋 중 한 개는 입력되어야 함.
	if(!search_Key && !basic_input[0].value && !basic_input[1].value){
		alert("You have to input a Searching word or Document.No or Title");
		return;
	}
	
	
	
	

	$.ajax({
	    url: 'search.do',
	    type: 'POST',
	    beforeSend:function(){
		    $('body').prepend(search_loading);
		 },
		complete:function(){
		    $('#search_loading').remove();
		 },
	    data:{
	    	prj_id:prj_id,
	    	prj_list : prj_List,
	    	keyword : search_Key,
	    	max_Display : max_Display,
	    	searchOpt : search_Opt,
	    	allPrjchk : allPrjchk,
	    	periodChk : periodChk,
	    	searchFrom : from,
	    	searchTo : to,
	    	folder_path : idPath,
	    	
	    	// basic cond
	    	basic_sel_doc_no : basic_sel[0].value,
	    	basic_input_doc_no : basic_input[0].value ? basic_input[0].value : null,
	    	basic_sel_title : basic_sel[1].value,
	    	basic_input_title : basic_input[1].value ? basic_input[1].value : null,
	    	// detail cond
	    	detail_sel_doc_type : detail_sel[0].value,
	    	version : detail_sel[1].value,
	    	detail_input_modifier : detail_input[0].value ? detail_input[0].value : null,
	    	mod_periodChk : $("#modDateChk")[0].checked,
	    	mod_searchFrom : mod_from, //detail_input[2].value,
	    	mod_searchTo : mod_to // detail_input[3].value	    		
	    },
	    success: function onData (data) {
	    	
	    	console.log(data);
	    	/*
	    	 * data0: document index 테이블 쿼리결과
	    	 * data1: tr테이블 쿼리결과
	    	 * data2: drn 테이블 쿼리결과
	    	 */
	    	docSearchList(data[0]);
	    	trSearchList(data[1]);
	    	drnSearchList(data[2]);
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}
/**
 * byte 용량을 환산하여 반환
 * 용량의 크기에 따라 MB, KB, byte 단위로 환산함
 * @param fileSize  byte 값
 * @returns {String}
 */
function getByte(fileSize) {
	var bytes = parseInt(fileSize);
	 
	// 단위
    var s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];

    var e = Math.floor(Math.log(bytes)/Math.log(1024));

    if(e == "-Infinity") return "0 "+s[0]; 
    else return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];
}

/**
 * document List에 검색 결과를 출력해주는 함수
 * @param data  
 * @returns result
 */
function docSearchList(data){
	
	var result = [];
	
	for(var i=0; i<data.length; i++){
		let path = data[i].folder_path;
		let pathArr = path ? path.split(">") : [];
		/*
		let pathString = "/";
		for(let j=0;j<pathArr.length;j++)
			pathString += (j != pathArr.length-1 ? getFolderName(pathArr[j],1)+"/" : getFolderName(pathArr[j],1));
		*/
		
		let tmp = data[i];
		
		tmp.project = (data[i].prj_nm?data[i].prj_nm:"");
		tmp.folder =  (data[i].folder_nm?data[i].folder_str:"-"),
		tmp.docNo = (data[i].doc_no?data[i].doc_no:"-"),
		tmp.title = data[i].title?data[i].title:"-",
		tmp.modifier = data[i].modifier? data[i].modifier:"-",
		tmp.size1 = data[i].file_size?getByte(data[i].file_size):"-",
		tmp.modifiedDate = (data[i].mod_date?data[i].mod_date:"-"),
		tmp.size2 = data[i].file_size?data[i].file_size:"-",
		tmp.docType = data[i].doc_type?data[i].doc_type:"-"		
		
		/*
		var tmp ={
				project: (data[i].prj_nm?data[i].prj_nm:"-"),
				folder: (data[i].folder_nm?pathString:"-"),
				docNo: (data[i].doc_no?data[i].doc_no:"-"),
				title: data[i].title?data[i].title:"-",
				modifier: data[i].mod_id? data[i].mod_id:"-",
				size1: data[i].file_size?getByte(data[i].file_size):"-",
				modifiedDate: (data[i].mod_date?data[i].mod_date:"-"),
				size2:data[i].file_size?data[i].file_size:"-",
				docType: data[i].doc_type?data[i].doc_type:"-"		
			};
		*/
		result.push(tmp);
		
	}
	
	
	docListTbl.setData(result);
	docListTbl.redraw();
	$("#docListTit").text("DOCUMENT LIST - " + data.length + " FOUND");	
}

/**
 * TRANSMITTAL List에 검색 결과를 출력해주는 함수
 * @param data  
 * @returns result
 */
function trSearchList(tr_tbl){
	
	var result = [];
	
	// trFrom, trTo 아직 미정
	// TR결과 push
	for(var i=0; i<tr_tbl.length; i++){
		var byte = tr_tbl[i].file_size;
		
		let tmp = tr_tbl[i];
		tmp.project= tr_tbl[i].prj_nm,
		tmp.tr_type= tr_tbl[i].trType,
		tmp.inOut=tr_tbl[i].inout,
		tmp.trNo=tr_tbl[i].tr_no,
		tmp.trSubject=tr_tbl[i].tr_subject,
		tmp.discipline=tr_tbl[i].discip_display,
		tmp.sendDate=tr_tbl[i].send_date,
		tmp.trWriter=tr_tbl[i].modifier,
		tmp.trFrom=tr_tbl[i].tr_from,
		tmp.trTo=tr_tbl[i].tr_to				
	
		result.push(tmp);		
	}
	
	
	
	trListTbl.setData(result);
	trListTbl.redraw();
	$("#trListTit").text("TRANSMITTAL LIST - " + result.length + " FOUND");	
}

/**
 * DRN List에 검색 결과를 출력해주는 함수
 * @param data  
 * @returns result
 */
function drnSearchList(data){
	
	var result = [];
	
	for(var i=0; i<data.length; i++){
		var byte = data[i].file_size;
		let tmp = data[i];
		tmp.project = data[i].prj_nm;
		tmp.drnNo = data[i].drn_no;
		tmp.drnSubject = data[i].drn_title;
		tmp.discipline = data[i].discip_display;
		tmp.reg_date = (data[i].reg_date ? data[i].reg_date : "-");
		tmp.drnWriter = data[i].modifier;
		
		/*
		var tmp ={
				project: data[i].prj_nm,
				drnNo: data[i].drn_no,
				drnSubject: data[i].drn_title,
				discipline: data[i].discip_display,
				sendDate: data[i].send_date,
				drnWriter:data[i].reg_id				
			};
		*/
		result.push(tmp);		
	}
	
	
	drnListTbl.setData(result);
	drnListTbl.redraw();
	$("#drnListTit").text("DRN LIST - " + data.length + " FOUND");	
}



/**
 * Global Search 페이지 시작시,
 * 검색결과가 나타나는 그리드 영역 초기화
 * @param   
 * @returns result
 */
function init(){
	
	
	
	
	// 페이지 로드시 처음 테이블 구성
	docListTbl = mkDocListTbl();
	trListTbl = mkTrListTbl();
	drnListTbl = mkDrnListTbl();
	
	// 모든 프로젝트 아이디들의 리스트 스트링으로 생성
	getAllPrj_IDs();
	// search버튼에 온클릭 속성 부여
	$("#searchBtn").click(function() { search() } );
	// Searching word input박스에 엔터입력시 검색 이벤트
	$("#searchKeyword").keypress(function() { if( event.keyCode==13 ) search() } );
	// All project 버튼 체크 이벤트
	$("#chkAllPrj").change(function(){
        //if();
    });
	
	/* period 체크박스 체크 이벤트  */
	 $("#periodChk").change(function(){
	        if($("#periodChk").is(":checked")){
	        	$("#searchFrom").attr("disabled",false);
	        	$("#searchTo").attr("disabled",false);
	        }else{
	        	$("#searchFrom").attr("disabled",true);
	        	$("#searchTo").attr("disabled",true);
	        }
	 });
	 
	 $("#modDateChk").change(function(){
	        if($("#modDateChk").is(":checked")){
	        	$("#modFrom").attr("disabled",false);
	        	$("#modTo").attr("disabled",false);
	        }else{
	        	$("#modFrom").attr("disabled",true);
	        	$("#modTo").attr("disabled",true);
	        }
	 });
	 
	 /* 다이얼로그 */
	 $(".pop_doc_search").dialog({
		    draggable: true,
		    autoOpen: false
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
	 
	 	/*
		$(document).on("click", ".btn_sel_folder", function() {
		    $(".pop_doc_search").dialog("open");
		    dialog_FolderList = getTree4Dialog();
		    
		    
		    return false;
		});
		*/
		/*
		$(document).on("click", "#folderSelect", function() {
		    $(".pop_doc_search").dialog("close");
		    if(!$(".jstree-clicked")[0].id) return false;
		    
		    var id = $(".jstree-clicked")[0].id;
		    id = id.replace("_anchor","");
		    var path = getFolderPath(id);
		    $("#searchPath").val(path);
		    return false;
		});
		*/

}


function search_folderSelectOpen(){
    $(".pop_doc_search").dialog("open");
    dialog_FolderList = getTree4Dialog();
    
    
    return false;
}

function search_folderSelect(){
	  	$(".pop_doc_search").dialog("close");
	  	
	  	var id = $("#tree_pop").jstree("get_selected")[0];
	  	
	    if(!id) return false;
	    /*
	    var id = $(".jstree-clicked")[0].id;
	    id = id.replace("_anchor","");
	    
	    id = $("#tree_pop").jstree("get_selected")[0];
	    */
	    var path = getFolderPath(id);
	    $("#searchPath").val(path);
	    return true;
}

function getFolderPath(id){
	var index = -1;
	
	for(let i=0;i<dialog_FolderList.length;i++){
		var tmp = dialog_FolderList[i];
		if(tmp.id == id) {
			index = i
			break;
		}
	}
	
	// 선택된 폴더의 실제 경로의 id값들 (db검색용)
	idPath = dialog_FolderList[index].folder_path;
	var pathArr = idPath.split(">");
	idPath += "%"; // 해당 값으로 시작하는 값 검색하기 위해 접미사 "%" 추가
	var pathString = ""; // 화면상에 path값 text 보여주기 위한 변수
	for(let i=0;i<pathArr.length;i++) 	// id들 text로 매칭해서 변환
		pathString += (i != pathArr.length-1 ? getFolderName(pathArr[i])+" > " : getFolderName(pathArr[i]));
	
		
	return pathString;
}

function getFolderName(id,opt){
	if(!opt){
		for(let i=0;i<dialog_FolderList.length;i++){
			var tmp = dialog_FolderList[i];
			if(tmp.id == id) return tmp.text;		
		}		
	}
	else{
		for(let i=0;i<folderList.length;i++){
			var tmp = folderList[i];
			if(tmp.id == id) return tmp.text;		
		}		
	}
	
}

// Document List 테이블 생성
function mkDocListTbl(){
	var tbl = new Tabulator("#documentList", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		columns: [
			{
				formatter:"rowSelection",
				titleFormatter:"rowSelection",
				hozAlign:"center", 
			    headerSort:false,
			    width : 10
			},
			{
		        title: "Project",
		        field: "project",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        widthGrow:2
		    },
		    {
		        title: "Folder Path",
		        field: "folder_str",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        widthGrow:3
		    },
		    {
		        title: "Document No.",
		        field: "docNo",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        widthGrow:2
		    },
		    {
		        title: "Title",
		        field: "title",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        widthGrow:2
		    },
		    {
		        title: "Modifier",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        field: "modifier"
		    },
		    {
		        title: "Size",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        field: "size1"
		    },
		    {
		        title: "Modified<br>Date",
		        field: "modifiedDate",
		        hozAlign: "left",
		        formatter:function(cell, formatterParams, onRendered){
  		           let date = cell.getValue();
 		           if(date != '-')
 		        	   date = date.substr(0,4) + '-' + date.substr(4,2) + '-' + date.substr(6,2) + ' ' + date.substr(8,2) + ":" + date.substr(10,2) + ":" + date.substr(12,2);
 		        	   
 		           return date;
 		        }
		    },
		    {
		        title: "Size(Byte)",
		        hozAlign: "left",
		        titleFormatter:function(cell, formatterParams, onRendered){
		        	let tmp = document.createElement("div");
		        	tmp.classList.add('search_grid_header');
		        	tmp.innerText = cell.getValue();
		        	return tmp;
		        },
		        field: "size2",
		        visible:false
		    },
		    {
		        title: "Document<br>Type",
		        field: "docType",
		        hozAlign: "left"
		    }	    
		],
		});
	
	tbl.on("rowClick", function(e, row){
		let data = row.getData();
		
		let params = {};
		$.ajax({
			url: 'getTodoPageVar.do',
			type: 'POST',
			traditional : true,
			async: false,
			data:{
				prj_id: data.prj_id,
				folder_id: Number(data.folder_id),
				doc_id:data.doc_id,
				rev_id:data.rev_id
			},
			success: function onData (data) {
				params = data.model;
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		if(!params){
			alert("해당 도서에 접근할 수 없습니다.");
			return;
		}
		/*
		if(!params.discip){
			alert("해당 도서에 접근할 수 없습니다.");
			return;
		}
		*/
		
		let auth = checkFolderAuth(data.prj_id,data.folder_id);
		console.log(auth);
		
		if(auth.r != "Y"){
			alert("해당 도서에 읽기 권한이 없습니다.");
			return;
		}
		
		$("#selectPrjId").val(data.prj_id);
		$('#session_user_id').val(session_user_id);
		$('#session_user_eng_nm').val(session_user_eng_nm);
		$('#current_folder_discipline_code').val(params.discip ?  params.discip.discip_code : "");
		$('#current_folder_discipline_code_id').val(params.discip ? params.discip.discip_code_id : "");
		$('#current_folder_discipline_display').val(params.discip ? params.discip.discip_display : "");
		$('#current_folder_id').val(Number(data.folder_id));
		$('#current_folder_path').val(params.folder_path);
		$('#process').val('property');
		$('#doc_type').val(params.doc_type);
		$('#DCCorTRA').val("DCC");
		$('#popup_new_or_update').val("update");
		$('#vendor_new_or_update').val("update");
		$('#proc_new_or_update').val("update");
		$('#site_new_or_update').val("update");
		$('#corr_new_or_update').val("update");
		$('#general_new_or_update').val("update");
		$("#doc_id").val(data.doc_id);
		$("#rev_id").val(data.rev_id);
		let useDRNYN = useDrnCheckForThisFolder(data.prj_id,Number(data.folder_id));
		$('#useDRNYN').val(useDRNYN);
		
		switch(params.doc_type){
			case 'DCI':
				let engDocPopup = window.open('./pop/engDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
				break;
			case 'VDCI':
				vendorDocPopup = window.open('./pop/vendorDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
				break;
			case 'PCI':
				let procDocPopup = window.open('./pop/procDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
				break;
			case 'SDCI':
				let siteDocPopup = window.open('./pop/siteDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
				break;
			case 'LETTER':
			case 'E-MAIL':
				let corrDocPopup = window.open('./pop/corrDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
				break;
			default:
				let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
		}
		
		$("#selectPrjId").val(getPrjInfo().id);
		
		});
	
	return tbl;
}

function mkTrListTbl(){
	var tbl = new Tabulator("#trList", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		columns: [{
		        title: "Project",
		        hozAlign: "left",
		        field: "project"
		    },
		    {
		        title: "TR Type",
		        hozAlign: "left",
		        field: "tr_type"
		    },
		    {
		        title: "IN OUT",
		        hozAlign: "left",
		        field: "inOut"
		    },
		    {
		        title: "TR NO.",
		        hozAlign: "left",
		        field: "trNo"
		    },
		    {
		        title: "TR SUBJECT",
		        hozAlign: "left",
		        field: "trSubject"
		    },
		    {
		        title: "DISCIPLINE",
		        hozAlign: "left",
		        field: "discipline"
		    },
		    {
		        title: "SEND DATE",
		        hozAlign: "left",
		        field: "sendDate",
		        formatter:function(cell, formatterParams, onRendered){
	  		           let date = cell.getValue();
	 		           if(date != '-')
	 		        	   date = date.substr(0,4) + '-' + date.substr(4,2) + '-' + date.substr(6,2);
	 		        	   
	 		           return date;
	 		       }
		    },
		    {
		        title: "TR WRITER",
		        hozAlign: "left",
		        field: "trWriter"
		    },
		    {
		        title: "TR FROM",
		        hozAlign: "left",
		        field: "trFrom"
		    },
		    {
		        title: "TR TO",
		        hozAlign: "left",
		        field: "trTo"
		    }	    
		],
		});
	return tbl;
}

function mkDrnListTbl(){
	var tbl = new Tabulator("#drnList", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		columns: [{
		        title: "Project",
		        hozAlign: "left",
		        field: "project"
		    },
		    {
		        title: "DRN NO.",
		        hozAlign: "left",
		        field: "drnNo"
		    },
		    {
		        title: "DRN SUBJECT",
		        hozAlign: "left",
		        field: "drnSubject"
		    },
		    {
		        title: "DISCIPLINE",
		        hozAlign: "left",
		        field: "discipline"
		    },
		    {
		        title: "REG DATE",
		        hozAlign: "left",
		        field: "reg_date",
		        formatter: function(cell, formatterParams, onRendered){
		        	let date = cell.getValue();
		        	// out = out.substr(0,4) + "-" + out.substr(4,2) + "-" + out.substr(6,2);
		        	
		        	 if(date != '-')
	 		            date = date.substr(0,4) + '-' + date.substr(4,2) + '-' + date.substr(6,2) + ' ' + date.substr(8,2) + ":" + date.substr(10,2) + ":" + date.substr(12,2);
	 		        	   
		        	return date;
		        }
		    },
		    {
		        title: "Approval Status",
		        hozAlign: "left",
		        field: "drn_approval_status",
		        formatter: function(cell, formatterParams, onRendered){
		        	let out = cell.getValue();
		        	switch(out){
			        	case "T":
			        		out = "임시저장";
		        			break;
		        		case "A":
		        			out = "검토중";
		        			break;
		        		case "E":
		        		case "1":
		        			out = "결재중"
		        			break;
		        		case "C":
		        			out = "결재완료"
		        			break;
		        		case "R":
		        			out = "반려됨"
		        			break;
		        		default:
		        			out = out;
		        		break;
		        	}
		        	
		        	return out;
		        }
		    }	,
		    {
		        title: "DRN Drafter",
		        hozAlign: "left",
		        field: "drnWriter"
		    }	    
		],
		});
	
	
	tbl.on("rowClick", function(e, row){
 	    //e - the click event object
 	    //row - row component
 		let rowData = row.getData();
 		
 		$.ajax({
			type: "POST", 
			url: 'get_drnVar.do',
			async:false,
			data:{
				prj_id: rowData.prj_id,
				drn_id: rowData.drn_id
			},
			success: function onData (data) {
				rowData.approval_id = Number(data.model.approval_id);
				rowData.check_sheet_id = data.model.check_sheet_id;
				rowData.folder_id = data.model.folder_id;
				rowData.status = data.model.status;
				rowData.drn_no = data.model.drn_no;
				rowData.check_sheet_title = data.model.check_sheet_title;
			},
			error: function onError (error) {
		        console.error(error);
		    }
		});
 		
 		
 		$.ajax({
			type: "POST", 
			url: 'get_drnPage.do',
			data:{
				jsonData:JSON.stringify(rowData)
			},
			success: function onData (data) {
				let discipObj = getDiscipObj(rowData.folder_id);
				
				
		    	let docData = data.model.docData;
		    	let pageData = data.model;
		    	drnPopUP("read" , docData , discipObj , rowData.folder_id, pageData);

			},
			error: function onError (error) {
		        console.error(error);
		    }
		});
 		
 	});
	
	
	return tbl;
}

