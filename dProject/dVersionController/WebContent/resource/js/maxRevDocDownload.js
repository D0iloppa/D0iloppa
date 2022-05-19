var docDown = {};
var docDown_loading = '<div id="docDown_loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';

$(document).ready(function(){
	docDown_init();
});

/**
 * 페이지가 로딩될 때 최초의 init
 * @returns
 */
function docDown_init(){
	docDown.prj_id = "";
	docDown.discip_code_id = "";
	docDown.folder_id = "";
	docDown.selectedFolderInfo = {};
	
	// 그리드 init
	/* 프로젝트 선택 그리드 */
	docDown.prjGrid = new Tabulator("#prj_list_grid_docDown", {
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

	docDown.prjGrid.on("tableBuilt", function() {
		$.ajax({
			url : 'getPrjGridList.do',
			type : 'POST',
			success : function onData(data) {
				docDown.prjGrid.setData(data.model.prjList);
				docDown.prjGrid.redraw();
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	});

	docDown.prjGrid.on("rowClick", function(e, row) {
		let rowData = row.getData();
		$("#docDownload_select_div").removeClass('on');
		$("#selected_prj_docDownload").text(rowData.prj_nm);
		docDown.prj_id = rowData.prj_id;
		// 프로젝트를 선택해 줄 때마다 폴더,discip 기존 선택값들 제거 및 옵션 재구성
		setFolderAndDiscip();
		console.log(rowData.prj_id);
	});
	
	
	docDown.grid = new Tabulator("#docDownload_grid", {
		layout : "fitColumns",
		placeholder : "There is No Data",
		selectable : true,
		index : "prj_id",
		height:600,
		renderVerticalBuffer:300,
		columns : [ 
			{
				formatter:"rowSelection",
				titleFormatter:"rowSelection",
				hozAlign:"center", 
			    headerSort:false,
			    width : 10
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
				title : "Discipline",
				field : "discip_display",
				hozAlign: "left",
				width:100,
				visible : true
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
				width: 200
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
				width: 100
			}
		]
	});
	
	docDown.renameGrid = new Tabulator("#docDown_downloadListGrid", {
		layout : "fitColumns",
		placeholder : "There is No Document to download",
		selectable : false,
		maxHeight : 150,
	    pagination : true,
		paginationSize : 200,
		columns : [
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
	
	
	
	
	// dialog init
	 $("#docDown_dialog").dialog({
	        autoOpen: false,
	        modal:true,
	        maxWidth: 400,
	        width: 400
	  });
	 
	 $("#docDown_renm").dialog({
	        autoOpen: false,
	        modal:true,
	        maxWidth: 1000,
	        width: 800
	  });
	 
	let defaultSet = "%DOCNO%_%REVNO%_%TITLE%";
	$("#docDown_fileRenameSet").attr("disabled",true);
	$("#docDown_fileRenameSet").val(defaultSet);
	$("#docDown_downloadFileRename")[0].checked = false;
	
}
/**
 * 프로젝트 select 창에서 프로젝트 검색결과 그리드에 셋팅
 * @returns
 */
function prjSearch_docDown(){
	let keyword = $("#prj_search_docDown").val();
	let grid = docDown.prjGrid;

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

/**
 * 폴더선택 버튼 클릭
 * @returns
 */
function folderSelect_docDown(){
	if(docDown.prj_id == ""){
		alert("Unless select a project, you cannot choose a folder.");
		return;
	}
	
	$("#docDown_dialog").dialog("open");
}


/**
 * 폴더선택 dialog 창에서 폴더 select
 * @returns
 */
function docDownDialog_folderSelect(){

	let selectedItem = docDown.tree.jstree("get_selected");
	let isSelected = selectedItem.length > 0 ? true : false;
	if(!isSelected) {
		docDown.selectedFolderInfo = {};
		$("#docDown_folderPath").val("");
		alert("There is no selected item");
		$("#docDown_dialog").dialog("close");
		return;
	}
	docDown.selectedFolderInfo = docDown.folderList.find(item => item.folder_id == selectedItem[0]);
	
	docDown.folder_id = docDown.selectedFolderInfo.folder_id;
	$("#docDown_folderPath").val(docDown.selectedFolderInfo.folder_path_str);
	$("#docDown_dialog").dialog("close");
	
	
}

/**
 * prj_id,folder_id,discip 값을 가지고 검색
 * @returns
 */
function docDown_retrieve(){
	if(docDown.prj_id == ""){
		alert("You need to select the project");
		return;
	}
	
	if(docDown.folder_id == "" && docDown.discip_code_id == ""){
		alert("You need to select the folder or discipline");
		return;
	}
	
	
	$.ajax({
		url : 'docDownRetrieve.do',
		type : 'POST',
	    beforeSend:function(){
		    $('body').prepend(docDown_loading);
		 },
		complete:function(){
		    $('#docDown_loading').remove();
		 },
		data : {
			prj_id:docDown.prj_id ,
			folder_id:(docDown.folder_id) ? docDown.folder_id : -1 ,
			discip_code_id: docDown.discip_code_id
		},
		success : function onData(data) {
			let result = data.model;
			let cnt = result.docList.length;
			docDown.grid.setData(result.docList);
			docDown.grid.redraw();
			
			$("#docDownload_numberOfFindedDoc_label").text(cnt +" DOCUMENTS ARE REMAINING");
			
			// 리트라이브 눌렀을 때 셋팅된 값을 넘겨야 하기 때문
			docDown.settedFolderId = (docDown.folder_id) ? docDown.folder_id : -1;
			docDown.settedDiscip = docDown.discip_code_id;
			
		},
		error : function onError(error) {
			console.error(error);
		}
	});
	
	
}
function docDown_discipOnselect(){
	let val = $("#docDown_discipSelect").val();
	if(!val) {
		docDown.discip_code_id = "";
		return;
	}
	
	docDown.discip_code_id = val;
	
}

function docDown_renameDialog(){

	let selectedDocs = docDown.grid.getSelectedData();
	
	if(selectedDocs.length<1) {
		alert("Select the doc to download");
		return;
	}
	

	let jsonStrings = [];
	for(let i=0;i<selectedDocs.length;i++){
		let json = JSON.stringify(selectedDocs[i]);
		jsonStrings.push(json);
	}
	
	docDown.renameGrid.setData(selectedDocs);
	docDown.renameGrid.redraw();
	
	
	$("#docDown_totalCountLabel").text('Total ' + docDown.renameGrid.getDataCount() + ' Files');
	$("#docDown_renm").dialog("open");
}

/**
 * 그리드에서 체크한 row들을 download한다.
 * @returns
 */
function docDownload(){
	
	
	let selectedDocs = docDown.renameGrid.getData();
	
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
	$("#docDown_form [name='prj_id']").remove();
	$("#docDown_form [name='folder_id']").remove();
	$("#docDown_form [name='discip']").remove();
	$("#docDown_form [name='fileRenameSet']").remove();
	$("#docDown_form [name='jsonRowDatas']").remove();
	
	
	// submit을 위한 form data 재구성
	var form = $("#docDown_form")[0];
	
	var paramsInput_prj_id = document.createElement('input');
	paramsInput_prj_id.setAttribute('type','hidden');
	paramsInput_prj_id.setAttribute('name','prj_id');
	paramsInput_prj_id.setAttribute('value',docDown.prj_id);
	form.appendChild(paramsInput_prj_id);
	
	var filter_folder = document.createElement('input');
	filter_folder.setAttribute('type','hidden');
	filter_folder.setAttribute('name','folder_id');
	filter_folder.setAttribute('value',docDown.settedFolderId);
	form.appendChild(filter_folder);
	
	var filter_discip = document.createElement('input');
	filter_discip.setAttribute('type','hidden');
	filter_discip.setAttribute('name','discip');
	filter_discip.setAttribute('value',docDown.settedDiscip);
	form.appendChild(filter_discip);
	
	var paramsInput_fileName = document.createElement('input');
	paramsInput_fileName.setAttribute('type','hidden');
	paramsInput_fileName.setAttribute('name','fileRenameSet');
	paramsInput_fileName.setAttribute('value',$("#docDown_fileRenameSet").val());
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
	docDownload();
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
 * 프로젝트 리스트에서 선택한 프로젝트 기준으로
 * 폴더와 discip 항목 재설정
 * @returns
 */
function setFolderAndDiscip(){

	// 폴더트리와 discip 옵션값 가져옴
	$.ajax({
	    url: 'docDownGetOptions.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:docDown.prj_id
	    },
	    success: function onData (data) { 
	    	let result = data.model;
	    	// 셋팅하기 전에 클리어
	    	docDown.folderList = result.folderList;
	    	docDown.folder_id = "";
	    	docDown.selectedFolderInfo = {};
	    	$("#docDown_folderPath").val("");
	    	
	    	docDown_setDialogTree(result.folderList);
	    	
	    	// discipline
	    	docDown.discip_code_id = "";
	    	docDown.discipList = result.discipList;
	    	docDown_setDiscipOptions(result.discipList);
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	

	
	
}

/**
 * 프로젝트에 따른 폴더리스트를 받아서
 * 폴더선택 다이얼로그의 트리 재구성
 * @param folderList
 * @returns
 */
function docDown_setDialogTree(folderList){
	
	
	
	// 폴더리스트를 js트리의 형태에 맞게 재구성
	let folderList_input = [];
	
	let rootCnt = 0;
	for(let i=0;i<folderList.length;i++){
		let tmp_Node = folderList[i];
		
		let	input_Folder = { 
				"id" : tmp_Node.folder_id, 
				"parent" : tmp_Node.parent_folder_id, 
				'text' : tmp_Node.folder_nm, 
				'type' : tmp_Node.folder_type ,
				'folder_path':tmp_Node.folder_path,
				'folder_path_str':tmp_Node.folder_path_str,
				'discip_code_id':tmp_Node.discip_code_id
			};
		
		if(tmp_Node.folder_id === tmp_Node.parent_folder_id && rootCnt<1){ // 최초의 최상위 루트만 추가
			input_Folder.parent = "#";
			input_Folder.icon = "resource/images/pinwheel.png";
			input_Folder.state =  {'opened' : true,'selected' : false};
			rootCnt++;
		}
		
		folderList_input.push(input_Folder);
	}
	
	
	
	docDown.tree = $('#docDown_tree');
	
	
	docDown.tree.jstree({
	     "core" : {
	    	 "multiple" : false,
	         "check_callback" : true
	       },
	       'sort' : function(a, b) {
	           a = this.get_node(a);
	           b = this.get_node(b);
	           if(a.icon != 'resource/images/pinwheel.png'){
	        	   return (a.text > b.text) ? 1 : -1;		           
	           }	           
	       },    
	      "plugins" : [ "contextmenu" , "sort"]
	    });
	
	docDown.tree.jstree(true).settings.core.data = folderList_input;
	$('#docDown_tree').jstree("deselect_all");
	$('#docDown_tree').jstree(true).refresh();
	
}
/**
 * discipline 리스트를 받아서 discip option값 재설정
 * @param discipList
 * @returns
 */
function docDown_setDiscipOptions(discipList){
	// 옵션 재구성 전, 옵션 클리어
	$("#docDown_discipSelect option").remove();
	
	//$("#docDown_discipSelect").text("SELECT");
	
	let topElement = document.createElement("option");
	topElement.value = '';
	topElement.text = "SELECT";
	$("#docDown_discipSelect").append(topElement);
	
	// discipList만큼 옵션 추가
	for(let i=0;i<discipList.length;i++){
		let tmpElement = document.createElement("option");
		tmpElement.value = discipList[i].discip_code_id;
		tmpElement.text = discipList[i].discip_display;
		$("#docDown_discipSelect").append(tmpElement);
		
	}
}
/**
 * download rename 창 dialog close
 * @returns
 */
function docDown_fileDownloadClose(){
	 $("#docDown_renm").dialog("close");
}

function docDown_fileRenameCheck(){
	let checked = $("#docDown_downloadFileRename")[0].checked
	if(checked){
		$("#docDown_fileRenameSet").attr("disabled",false);
	}else{
		let defaultSet = "%DOCNO%_%REVNO%_%TITLE%";
		$("#docDown_fileRenameSet").attr("disabled",true);
		$("#docDown_fileRenameSet").val(defaultSet);
	}
	
}
