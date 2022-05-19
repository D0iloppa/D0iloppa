var contextpath = $('#contextpath').val();
var selectedDCC = '';
var currentSelectDCC = '';
var selectPrjId = '';
var origin_dcc_user_list='';
var setPrjUpdate = 0;
var applyCheck = 0;

var prj_id = $('#selectPrjId').val(); 
var projectTypeListTbl;
var setOrderArray = new Array();
var addOrUpdate = 'update';
var code_gubun;
var typeList = [];
var prj_grid_creation_copy;

var prjCreationtable;

$(function(){
	 $("#prjCreationCopyCheck").change(function(){
	        if($("#prjCreationCopyCheck").is(":checked")){
	            $('#prjCreationCopyApply').css('display','inline-block');
	            $('#selected_prj_creation_copy').css('display','inline-block');
	        }else{
	            $('#prjCreationCopyApply').css('display','none');
	            $('#selected_prj_creation_copy').css('display','none');
	        }
	 });
	 
	$("#prjType_UpBtn").click(function() { stepRowUp( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	$("#prjType_DownBtn").click(function() { stepRowDown( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	
	$(".pop_dccSet").dialog({
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
	$(".pop_prjType").dialog({
		draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto"
	}).bind('dialogclose', function(event, ui) { getPrjTypeList(); }).dialogExtend({
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
	
	$(".pop_PrjTypeCodeDelete").dialog({
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
	$(document).on("click","#prjType", function() {
		$(".pop_prjType").dialog("open");
		getPrjTypeList();
        return false;
	});
    /*$(document).on("click", "#dccSet", function() {
        $(".pop_dccSet").dialog("open");
        return false;
    });*/
    initPrjSelectGridCreationCopy();
    getUserList();
    getPrjList();
	$('#prjEdit').css('display','none');
});
function OrganizationChartMultiOpenDCCSet(){
	$('#organizationUse').val('DCC 설정');
	window.open('./pop/OrganizationChartMulti.jsp','조직도','top=50, left=50, width=1500, height=650, resizable=yes');
}
function initPrjSelectGridCreationCopy(){
	prj_grid_creation_copy  = new Tabulator("#prj_list_grid_creation_copy", {
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
	
	prj_grid_creation_copy.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_creation_copy.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_creation_copy.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#selectCreationCopyPrjId").val(rowData.prj_id);
		$("#selected_prj_creation_copy").html(rowData.prj_nm);
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prj_search_creation_copy') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
			    	prjSearchCreationCopy();
			    }
		}
		if(document.getElementById('usersSearchKeyword') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 usersSearch();
			    }
		}
	});
	
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjSearchCreationCopy(){
	let keyword = $("#prj_search_creation_copy").val();
	let grid = prj_grid_creation_copy;
	
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
function removeTabPRJCREATION(){
	$(".pop_dccSet").remove();
	$(".pop_prjType").remove();
	$(".pop_PrjTypeCodeDelete").remove();
}

function getPrjTypeList() {
//	console.log(prj_id);
	$.ajax({
		url: 'getPrjTypeList.do',
	    type: 'POST',
	    success: function onData (data) {
//	    	console.log("데이타 길이 = "+data[0].length);
	    	typeList = [];
	    	var html = "";
	    	for(let i=0; i<data[0].length; i++) {
//	    		console.log(data[0][i].set_order);
	    		typeList.push({
	    			No: i,
	    			origin: 'update',
	    			ORIGIN_CODE: data[0][i].code,
	    			setOrder: data[0][i].set_order,
	    			code_id: data[0][i].code_gubun,
	    			code_nm: data[0][i].code_nm,
	    			bean: data[0][i]
	    		});
	    		setOrderArray[i] = data[0][i].set_order;
	    		code_gubun = data[0][i].code_gubun;
	    		html += '<option value="'+data[0][i].code+'">'+data[0][i].code_nm+'</option>';
	    	}
	    	$("#prj_type").html('<option value="0"></option>'+html);
	    	setPrjTypeList(typeList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function setPrjTypeList(typeList) {
	projectTypeListTbl = new Tabulator("#prjType_Grids",{
		layout: "fitColumns",
		placeholder:"No Data Set",
		data: typeList,
		selectable:1,//true
		index:"No",
		columns:[
			{
				title:"No",
				field:"No",
				visible:false
			},
			{
				title:"CK",
				formatter: function(cell, formatterParams, onRendered) {
			     	var checkbox = document.createElement("input");
			     	checkbox.classList.add('typeList');
	   		        checkbox.id = 'typeList'+cell.getRow().getPosition();
			        checkbox.name = 'typeList';
			        checkbox.type = 'checkbox';
			        checkbox.value = cell.getRow().getPosition();
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
				title: "TYPE",
				field: "code_nm",
				editor:true,
				widthGrow : 8
			},
			{
				title: "origin",
				field: "origin",
				visible: false
			},
			{
				title: "setOrder",
				field: "setOrder",
				visible: false
			}
		]
	});
}

function prjType_insert() {
	addOrUpdate = 'add';
	var setOrder = Math.max.apply(null, setOrderArray)+1;
	var addrow = {
			origin: 'add',
			setOrder: setOrder,
			TYPE: ''
	}
	
	projectTypeListTbl.addRow(addrow);
}

function prjType_cancle() {
	$(".pop_prjType").dialog("close");
	$("#prj_type option").remove();
	
	getPrjTypeList();
}

function prjType_save() {
	var selectTableData = projectTypeListTbl.getData();
	var editedCells = projectTypeListTbl.getEditedCells();
	
//	console.log("selectTableData = "+selectTableData+" editedCells = "+editedCells);
	
	var set_code_arr = [];
	for(i=0;i<selectTableData.length;i++){
		if(selectTableData[i].code_nm!=''){
//			console.log("code_nm = "+selectTableData[i].code_nm);			
			set_code_arr[i] = selectTableData[i].code_nm;
		}
	}
	var set_code_duplicate = isDuplicate(set_code_arr);
	if(set_code_duplicate){
		alert('TYPE에 중복값이 있습니다.');
	}else{
		var RowDataArray = [];
		
		for(i=0;i<editedCells.length;i++){
			var RowData = editedCells[i].getRow().getData();
			RowDataArray.push(RowData);
		}
		RowDataArray = duplicateRemoveInArray(RowDataArray);
//		console.log("RowDataArray = "+RowDataArray);
		for(i=0;i<RowDataArray.length;i++){
			var code_nm = RowDataArray[i].code_nm;
			
//			console.log("RowDataArray[i].origin = "+RowDataArray[i].origin);
			var origin_code = RowDataArray[i].ORIGIN_CODE;
			if(RowDataArray[i].origin==='update' && code_nm != ''){
				updateCodeInfo(code_nm.trim(), origin_code);
			}else if(RowDataArray[i].origin==='add' && code_nm != ''){
				insertCodeInfo(code_nm.trim(), origin_code);
			}
		}
		alert('성공적으로 저장되었습니다.');
		getPrjTypeList();
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

function insertCodeInfo(code_nm, origin_code) {
//	console.log("insert = "+origin_code);
		$.ajax({
		    url: 'insertCodeInfo.do',
		    type: 'POST',
		    async: false,
		    data: {
		    	code_nm: code_nm,
		    	origin_code: origin_code
		    },
		    success: function onData (data) {
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}

function updateCodeInfo(code_nm, origin_code) {
//	console.log("update = "+origin_code+"code_nm = "+code_nm);
	$.ajax({
	    url: 'updateCodeInfo.do',
	    type: 'POST',
	    data: {
	    	code_nm: code_nm,
	    	origin_code: origin_code,
	    	code_gubun: code_gubun
	    },
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function prjType_delete() {
	if($("input:checkbox[name='typeList']:checked").length===0){
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}else{
	    $(".pop_PrjTypeCodeDelete").dialog("open");
	}
}

function PrjTypeDeleteCancel() {
	$(".pop_PrjTypeCodeDelete").dialog("close");
}

function PrjTypeDelete() {
//	console.log("code_gubun = "+code_gubun);
	var checkedIndex = '';
	$("input:checkbox[name='typeList']:checked").each(function(){
		checkedIndex += $(this).attr('id').replaceAll('typeList','') + ',';
	});
//	console.log("checkedIndex = "+checkedIndex);
	checkedIndex = checkedIndex.slice(0, -1);
	var checkedIndexArray = checkedIndex.split(',');
	var checkedUserString = '';
//	console.log("인덱스 = "+checkedIndexArray);
	for(i=0;i<checkedIndexArray.length;i++){
		checkedUserString += projectTypeListTbl.getRow((parseInt(checkedIndexArray[i]))).getData().ORIGIN_CODE +',';
	}
	checkedUserString = checkedUserString.slice(0, -1);
//	console.log(checkedUserString);
	
	$.ajax({
		url: 'deleteCodeInfo.do',
	    type: 'POST',
	    data: {
	    	code: checkedUserString,
	    	code_gubun: code_gubun
	    },
	    success: function onData (data) {
	    	if(data[0]===''){
		    	alert('삭제되었습니다.');
	    	}else{
	    		alert(data[0]+'코드는 사용중이라 삭제할 수 없습니다.');
	    	}
	        $(".pop_PrjTypeCodeDelete").dialog("close");
	        getPrjTypeList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function stepRowUp(callback){
	
	var thisGrid = projectTypeListTbl;
	
//	console.log("thisGrid = "+thisGrid.getSelectedRows());
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getPrevRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getPrevRow().getIndex();
	
//	var selectedOrd = Number(thisGrid.getRow(selectedIndex).getData().setOrder);
//	var targetOrd = Number(thisGrid.getRow(targetIndex).getData().setOrder);
	
	selectedRow.move(targetIndex,true);
	
//	thisGrid.getRow(selectedIndex).update({setOrder:selectedOrd});
//	thisGrid.getRow(targetIndex).update({setOrder:targetOrd});
	thisGrid.getRow(selectedIndex).update({setOrder:(thisGrid.getRow(selectedIndex).getPosition()+1)+""});
	thisGrid.getRow(targetIndex).update({setOrder:(thisGrid.getRow(targetIndex).getPosition()+1)+""});
	
	thisGrid.redraw();
	
	var updateRow = [];
	updateRow.push(thisGrid.getRow(selectedIndex).getData());
	updateRow.push(thisGrid.getRow(targetIndex).getData());
	
	for(var i=0;i<updateRow.length;i++) {
		$.ajax({
		    url: 'prjCodeInfoUpdate.do',
		    type: 'POST',
		    data:{
		    	code_gubun : code_gubun,
		    	set_order : updateRow[i].setOrder,
				code :updateRow[i].bean.code
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

function stepRowDown(callback) {
	
	var thisGrid = projectTypeListTbl;
	
//	console.log("thisGrid = "+thisGrid.getSelectedRows());
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getNextRow()) return;
	
	// 선택한 row의 인덱스
	var selectedIndex = selectedRow.getIndex();
	// 바꿀 row의 인덱스 (아래)
	var targetIndex = selectedRow.getNextRow().getIndex();
	
	selectedRow.move(targetIndex,false);
	
//	var selectedOrd = Number(thisGrid.getRow(selectedIndex).getData().setOrder);
//	var targetOrd = Number(thisGrid.getRow(targetIndex).getData().setOrder);
	
	thisGrid.getRow(selectedIndex).update({No:targetIndex,setOrder:(thisGrid.getRow(selectedIndex).getPosition()+1)+""});
	thisGrid.getRow(targetIndex).update({No:selectedIndex,setOrder:(thisGrid.getRow(targetIndex).getPosition()+1)+""});
	
	thisGrid.redraw();
	
	var updateRow = [];
	updateRow.push(thisGrid.getRow(selectedIndex).getData());
	updateRow.push(thisGrid.getRow(targetIndex).getData());
	
	for(var i=0;i<updateRow.length;i++)
		
		$.ajax({
		    url: 'prjCodeInfoUpdate.do',
		    type: 'POST',
		    data:{
		    	code_gubun : code_gubun,
		    	set_order : updateRow[i].setOrder,
				code :updateRow[i].bean.code
		    },
		    success: function onData (data) {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
	callback(thisGrid,targetIndex);
}

function apply(){
	applyCheck = 1;
	$('#prjCreation').css('display','inline-block');
	$('#prjEdit').css('display','none');
	if($("input:checkbox[name=copyCheck]").is(":checked")){
		var copyprj = $('#selectCreationCopyPrjId').val();
		if(copyprj===''){
			copyprj = '0000000001';
		}
		$.ajax({
		    url: 'getPrjInfo.do',
		    type: 'POST',
		    data:{
		    	prj_id:copyprj
		    },
		    success: function onData (data) {
		    	$('#prj_no').val(data[0].prj_no);
		    	$('#prj_nm').val(data[0].prj_nm);
		    	$('#prj_full_nm').val(data[0].prj_full_nm);
		    	if(data[0].prj_hidden_yn=='Y'){
		    		$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', true);   
		    		$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', false); 
		    	}else{
		    		$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', false);   
		    		$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', true); 
		    	}
		    	$('#prj_type').val(data[0].prj_type);
		    	$('#client_code').val(data[0].client_code);
		    	$('#ship_no').val(data[0].ship_no);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		//기존 DCC 정보 가져오는 로직
		$.ajax({
		    url: 'getPrjDCC.do',
		    type: 'POST',
		    data:{
		    	prj_id:copyprj
		    },
		    success: function onData (data) {
		    	let html = '';
		    	let DCCNAMELIST = '';
		    	var DCCIDLIST = '';
		    	for(i=0;i<data[0].length;i++){
		    		html += '<li id="selectedUserIdIs_'+data[0][i].user_id+'">' + data[0][i].user_kor_nm + '(' + data[0][i].user_id + ')'
					+'&nbsp;<button onclick="deleteSelectUser(`'+data[0][i].user_id+'`,`'+data[0][i].user_kor_nm+'`)" class="btn_icon btn_blue"><i class="fas fa-times"></i><span>Close</span></button></li>';
		    		DCCNAMELIST += data[0][i].user_kor_nm+'('+data[0][i].user_id+')'+',';
		    		DCCIDLIST += data[0][i].user_id + ',';
		    	}
		    	DCCNAMELIST = DCCNAMELIST.slice(0, -1);
		    	DCCIDLIST = DCCIDLIST.slice(0, -1);
		    	$('#DCCLISTHTML').html(html);
		    	$('#DCCNAMELIST').html(DCCNAMELIST);
		    	$('#DCCIDLIST').val(DCCIDLIST);
		    	origin_dcc_user_list = DCCIDLIST;
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else{
		$('#selectPrjId').val(1);
		alert("Copy 체크박스를 체크해 주세요");
	}
}
function getUserList(){
	$.ajax({
	    url: 'getUsersVOList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var userList = [];
	    	for(i=0;i<data[0].length;i++){
	    		userList.push({
                    No: i+1,
		            userId: data[0][i].user_id,
		            KorName: data[0][i].user_kor_nm,
		            EngName: data[0][i].user_eng_nm,
		            email: data[0][i].email_addr,
		            company: data[0][i].company_nm
	    		});
	    	}
	    	setUserList(userList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function usersSearch(){
	var searchUser = $('input[name="searchUser"]:checked').val();
	var searchKeyword = $('#usersSearchKeyword').val();
		$.ajax({
		    url: 'searchUsersVO.do',
		    type: 'POST',
		    data:{
		    	searchUser:searchUser, // 체크박스 값
		    	searchKeyword:searchKeyword // 검색창 값
		    },
		    success: function onData (data) {
		    	var userList = [];
		    	if(data == null || data == "") {
		    		alert("검색하신 사용자 정보가 없습니다.");
		    		return;
		    	}
		    	for(i=0;i<data[0].length;i++){
		    		userList.push({
	                    No: i+1,
			            userId: data[0][i].user_id,
			            KorName: data[0][i].user_kor_nm,
			            EngName: data[0][i].user_eng_nm,
			            email: data[0][i].email_addr,
			            company: data[0][i].company_nm
		    		});
		    	}
		    	setUserList(userList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function setUserList(userList){
    var DCCUsertable = new Tabulator("#userList", {
        selectable:1,//true
        data: userList,
        layout: "fitColumns",
        placeholder:"No Data Set",
        height:250,
        columns: [{
                title: "No",
                field: "No",
                width: 50
            },
            {
                title: "User ID",
                field: "userId",
                hozAlign: "left"
            },
            {
                title: "Kor.Name",
                field: "KorName",
                hozAlign: "left"
            },
            {
                title: "Eng.Name",
                field: "EngName",
                hozAlign: "left"
            },
            {
                title: "E-mail",
                field: "email",
                hozAlign: "left"
            },
            {
                title: "Company",
                field: "company",
                hozAlign: "left"
            }
        ]
    });
    
    DCCUsertable.on("rowSelected", function(row){
    	let selectedDCC = $('#DCCIDLIST').val();
    	let temp = $('#DCCNAMELIST').children();
    	let selectedDCCNameArray = [];
    	for(let i=0;i<temp.length;i++){
    		selectedDCCNameArray.push($('#DCCNAMELIST').children()[i].innerHTML);
    	}
    	let selectedDCCName = selectedDCCNameArray.join(',');
    	let selectedDCCEmail = $('#DCCEMAILLIST').val();
    	//let selectedDCCIdEmail = $('#DCCIDEMAILLIST').val();
    	if(selectedDCC!=''){
	    	selectedDCC += ',';
	    	selectedDCCName += ',';
	    	selectedDCCEmail += ',';
	    	//selectedDCCIdEmail += ',';
    	}
		selectedDCC += row.getData().userId;
		selectedDCCName += row.getData().KorName;
		selectedDCCEmail += row.getData().email;
		//selectedDCCIdEmail += row.getData().userId +'('+row.getData().email+')';
		
    	if(selectedDCC!=''){
			var arraySelectedDCC = selectedDCC.split(',');
			var arraySelectedDCCName = selectedDCCName.split(',');
			var arraySelectedDCCEmail = selectedDCCEmail.split(',');
			//var arraySelectedDCCIdEmail = selectedDCCIdEmail.split(',');
			selectedDCC = [...new Set(arraySelectedDCC)];
			selectedDCCName = [...new Set(arraySelectedDCCName)];
			selectedDCCEmail = [...new Set(arraySelectedDCCEmail)];
			//selectedDCCIdEmail = [...new Set(arraySelectedDCCIdEmail)];
			
			let html = '';
			let namehtml = '';
			for(let i=0;i<selectedDCC.length;i++){
				//console.log("아이디 = "+selectedDCC[i]);
				//console.log("이메일 = "+selectedDCCEmail[i]);
				
	    		if(selectedDCCEmail[i] != undefined) {
	    			html += '<li class="'+selectedDCC[i]+'">'+selectedDCC[i] + '(' + selectedDCCEmail[i] + ')' +' <a class="btn_del" onclick="selectedDCCUserDelete(`'+selectedDCC[i]+'`,`'+selectedDCC[i]+'_'+selectedDCCName[i]+'`,`'+selectedDCCEmail[i]+'`)">삭제</a></li>';
		    		namehtml +=  '<li class="'+selectedDCC[i]+'" style="float:left; margin-left:5px; margin-top:3px;">'+arraySelectedDCCName[i]+'</li>';
				}else if(selectedDCCEmail[i] == undefined) {
					//console.log("같은 이메일 중복2 = "+selectedThisDCCEmail+','+ i);
					html += '<li class="'+selectedDCC[i]+'">'+selectedDCC[i] + '(' + selectedDCCEmail[i] + ')' +' <a class="btn_del" onclick="selectedDCCUserDelete(`'+selectedDCC[i]+'`,`'+selectedDCC[i]+'_'+selectedDCCName[i]+'`,`'+selectedDCCEmail[i]+'`)">삭제</a></li>';
				}
			}
			$('#DCCNAMELIST').html(namehtml);
			$('#DCCLIST').html(html);
			selectedDCC = selectedDCC.join(',');
			selectedDCCName = selectedDCCName.join(',');
			selectedDCCEmail = selectedDCCEmail.join(',');
			$('#DCCIDLIST').val(selectedDCC);
			//$('#DCCIDEMAILLIST').val(selectedDCCIdEmail);
			$('#DCCEMAILLIST').val(selectedDCCEmail);
    	}

    	
    });
}
/*function addUserDCC(){
	var DCCIdList = $('#DCCIDLIST').val();
	var Idlength = DCCIdList.indexOf(',');
	var DCCId = DCCIdList.split(',');
	var originDCCList = $('#DCCLIST').html();
	var originDCCList2 = $('#DCCLIST').html();
	var newDCCList='';
	var newDCCList2='';
	var Dlength = selectedDCC.indexOf(',');
	var selectDCC = selectedDCC.split(',');
	
	if(DCCIdList==''){//DCC LIST가 비어있을 때
		if(Dlength==-1){//선택된 DCC가 한개의 row면
			addDCCOne();
		}
	}else{
		if(Idlength==-1 && DCCIdList!=currentSelectDCC){//DCC LIST에 한명이 들어있을 경우 그사람이 추가하려는 DCC가 아니면
			addDCCOne();
		}else{//DCC LIST에 여러명이 들어있을 경우
			$('#DCCIDLIST').val(selectedDCC);
			for(i=0;i<selectDCC.length;i++){
					if(!DCCId.includes(selectDCC[i])){
						$.ajax({
						    url: 'getUsersVO.do',
						    type: 'POST',
						    data:{
						    	user_id:selectDCC[i]
						    },
						    success: function onData (data) {
						    	newDCCList += '<li id="DCC_li_'+data[0].user_id+'">'+data[0].user_kor_nm+'('+data[0].email_addr+')<img class="delete_dcc" src="'+contextpath+'/resource/images/so_close.png" onclick="deleteDCC(`'+data[0].user_id+'`)"></li>';
						    	$('#DCCLIST').html(originDCCList+newDCCList);
						    	
						    	newDCCList2 += '<li id="DCC_li_'+data[0].user_id+'">'+data[0].user_kor_nm+'('+data[0].email_addr+')</li>';
						    	$('#DCCLIST2').html(originDCCList2+newDCCList2);
						    },
						    error: function onError (error) {
						        console.error(error);
						    }
						});
					}
			}
		}
	}
}
function addDCCOne(){
	var originDCCList = $('#DCCLIST').html();
	var originDCCList2 = $('#DCCLIST2').html();
	var newDCCList='';
	var newDCCList2='';
	$.ajax({
	    url: 'getUsersVO.do',
	    type: 'POST',
	    data:{
	    	user_id:currentSelectDCC
	    },
	    success: function onData (data) {
//	    	console.log(data);
	    	newDCCList += '<li id="DCC_li_'+data[0].user_id+'">'+data[0].user_kor_nm+'('+data[0].email_addr+')<img class="delete_dcc" src="'+contextpath+'/resource/images/so_close.png" onclick="deleteDCC(`'+data[0].user_id+'`)"></li>';
	    	$('#DCCLIST').html(originDCCList+newDCCList);
	    	
	    	newDCCList2 += '<li id="DCC_li_'+data[0].user_id+'">'+data[0].user_kor_nm+'('+data[0].email_addr+')</li>';
	    	$('#DCCLIST2').html(originDCCList2+newDCCList2);
//	    	console.log("newDCCList = "+newDCCList+" newDCCList2 = "+newDCCList2);
	    	
			$('#DCCIDLIST').val(selectedDCC);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}*/
function getPrjList(){
	$.ajax({
	    url: 'getPrjList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var prjList = [];
	    	for(i=0;i<data[0].length;i++){
	    		prjList.push({
	    			prjId: data[0][i].prj_id,
	    			No_Tbl: (data[0].length+1)-(i+1),
                    No: data[0][i].prj_no,
                    projName: data[0][i].prj_nm,
                    projFullName: data[0][i].prj_full_nm
	    		});
	    	}
	    	setPrjList(prjList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function setPrjList(prjList){
	getUserList();
	
	prjCreationtable = new Tabulator("#projList", {
        selectable:1,
        data: prjList,
        layout: "fitColumns",
        height:250,
        columns: [{
	            title: "Prj Id",
	            field: "prjId",
	            visible: false
        	},
        	{
                title: "No",
                field: "No_Tbl",
                width: 80
            },
            {
                title: "Project No",
                field: "No",
                width: 180,
                hozAlign: "left"
            },
            {
                title: "Project Name",
                field: "projName",
                hozAlign: "left"
            },
            {
                title: "Project Full Name",
                field: "projFullName",
                hozAlign: "left"
            }
        ],
    });
    
	prjCreationtable.on("rowSelected", function(row){
		$('#prjEdit').css('display','inline-block');
		$('#prjCreation').css('display','none');
    	selectPrjId = row.getData().prjId;
		$.ajax({
		    url: 'getPrjInfo.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId
		    },
		    success: function onData (data) {
		    	$('#prj_no').val(data[0].prj_no);
		    	$('#prj_nm').val(data[0].prj_nm);
		    	$('#prj_full_nm').val(data[0].prj_full_nm);
		    	if(data[0].prj_hidden_yn=='Y'){
		    		$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', true);   
		    		$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', false); 
		    	}else{
		    		$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', false);   
		    		$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', true); 
		    	}
		    	$('#prj_type').val(data[0].prj_type);
		    	$('#client_code').val(data[0].client_code);
		    	$('#ship_no').val(data[0].ship_no);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		prjCreationtable.on("rowDeselected", function(row){
			$('#prjCreation').css('display','inline-block');
			$('#prjEdit').css('display','none');
			
			$('#DCCEMAILLIST').val("");
			$('#DCCIDLIST').val("");
			$('#prj_no').val("");
			$('#prj_nm').val("");
			$('#prj_full_nm').val("");
			$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', false);   
			$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', true);
			$('#prj_type').val("");
			$('#client_code').val("");
			$('#ship_no').val("");
			
			$('#DCCLIST').empty();
			$('#DCCNAMELIST').empty();
		});
		//기존 DCC 정보 가져오는 로직
		$.ajax({
		    url: 'getPrjDCC.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId
		    },
		    success: function onData (data) {
		    	let html = '';
		    	let DCCNAMELIST = '';
		    	var DCCIDLIST = '';
		    	for(i=0;i<data[0].length;i++){
		    		html += '<li id="selectedUserIdIs_'+data[0][i].user_id+'">' + data[0][i].user_kor_nm + '(' + data[0][i].user_id + ')'
					+'&nbsp;<button onclick="deleteSelectUser(`'+data[0][i].user_id+'`,`'+data[0][i].user_kor_nm+'`)" class="btn_icon btn_blue"><i class="fas fa-times"></i><span>Close</span></button></li>';
		    		DCCNAMELIST += data[0][i].user_kor_nm+'('+data[0][i].user_id+')'+',';
		    		DCCIDLIST += data[0][i].user_id + ',';
		    	}
		    	DCCNAMELIST = DCCNAMELIST.slice(0, -1);
		    	DCCIDLIST = DCCIDLIST.slice(0, -1);
		    	$('#DCCLISTHTML').html(html);
		    	$('#DCCNAMELIST').html(DCCNAMELIST);
		    	$('#DCCIDLIST').val(DCCIDLIST);
		    	origin_dcc_user_list = DCCIDLIST;
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
    });
}
function DCCPopupClose(){
    $(".pop_dccSet").dialog("close");
}

function newPrjCreation() {
	$('#prjEdit').css('display','none');
	$('#prjCreation').css('display','inline-block');

	$('#DCCIDLIST').val('');
	$('#DCCLISTHTML').html('');
	$('#DCCNAMELIST').html('');
	$('#prj_no').val("");
	$('#prj_nm').val("");
	$('#prj_full_nm').val("");
	$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', false);   
	$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', true);
	$('#prj_type').val("");
	$('#client_code').val("");
	$('#ship_no').val("");
	prjCreationtable.deselectRow();
}

function prjCreationSave(){
	var copy_prj_id = $('#selectCreationCopyPrjId').val();
	var prj_no = ($('#prj_no').val()).trim();
	var prj_nm = ($('#prj_nm').val()).trim();
	var prj_full_nm = ($('#prj_full_nm').val()).trim();
	var prj_hidden_yn = $('input:radio[name="prj_hidden_yn"]:checked').val();
	var prj_type = $('#prj_type').val();
	var client_code = ($('#client_code').val()).trim();
	var ship_no = ($('#ship_no').val()).trim();
	var DCCLIST = $('#DCCIDLIST').val();
	if(copy_prj_id===''){//이전공사 copy를 선택하지 않았다면 디폴트 프로젝트의 정보를 복사
		copy_prj_id = '0000000001';
	}
	
	if(prj_no.length > 10){
		alert('PROJECT NO은 10자이내여야 합니다');
	}else if(prj_no==''){
		alert('PROJECT NO를 설정해야 합니다');
	}else if(prj_nm==''){
		alert('PROJECT NAME을 설정해야 합니다');
	}else if(prj_full_nm==''){
		alert('PROJECT FULL NAME을 설정해야 합니다');
	}else if(DCCLIST==''){
		alert('DCC를 설정해야 합니다');
	}else{	//프로젝트 생성
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT CREATION",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		$.ajax({
		    url: 'insertPrjInfo.do',
		    type: 'POST',
		    data:{
		    	prj_no:prj_no,
		    	prj_nm:prj_nm,
		    	prj_full_nm:prj_full_nm,
		    	prj_hidden_yn:prj_hidden_yn,
		    	prj_type:prj_type,
		    	client_code:client_code,
		    	ship_no:ship_no,
		    	copy_prj_id:copy_prj_id
		    },
		    success: function onData (data) {
		    	var prj_id=data[0];
		    	var create_prj_id = prj_id;
		    	var Idlength = DCCLIST.indexOf(',');
		    	var DCCId = DCCLIST.split(',');

				$.ajax({
				    url: 'insertDCCPrjMember.do',
				    type: 'POST',
				    data:{
				    	prj_id:prj_id,
				    	diff_dcc_user:DCCLIST,
				    	dcc_yn:'Y'
				    },
				    success: function onData (data) {
				    	// 권한 copy
				    	$.ajax({
				 			type: 'POST',
				 			url: 'defaultAuthSet.do',
				 			data:{
				 				prj_id : create_prj_id
				 			},
				 			success: function(data) {
				 				console.log(data);			
				 			},
				 		    error: function onError (error) {
				 		        console.error(error);
				 		    }
				 		});
				    	
			    		alert('프로젝트를 생성했습니다.');
				    	getPrjList();
//				    	$.ajax({
//				    	    url: 'headerRefresh.do',
//				    	    type: 'POST',
//				    	    success: function onData (data) {
//				    	    	refreshPrjList(data);
//				    	    },
//				    	    error: function onError (error) {
//				    	        console.error(error);
//				    	    }
//				    	});
				    	
				    	
				    	
				    	
				    	
				    	 
				    	
				    	
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
	    		
	    		$('#prjEdit').css('display','none');
	    		$('#prjCreation').css('display','inline-block');
	    		
	    		$('#prj_no').val("");
	    		$('#prj_nm').val("");
	    		$('#prj_full_nm').val("");
	    		$('input:radio[name="prj_hidden_yn"][value="Y"]').prop('checked', false);   
	    		$('input:radio[name="prj_hidden_yn"][value="N"]').prop('checked', true);
	    		$('#prj_type').val("");
	    		$('#client_code').val("");
	    		$('#ship_no').val("");
	    		
	    		$('#DCCIDLIST').val('');
		    	$('#DCCLISTHTML').html('');
		    	$('#DCCNAMELIST').html('');
	    		
	    		// PROJECT LIST 테이블 갱신
		    	getPrjList();
		    	//헤더 프로젝트 리스트 갱신
		    	initPrjSelectGrid();
		    	//project creation 내 copy project 리스트 갱신
		    	initPrjSelectGridCreationCopy();
				
		    	$.ajax({
				    url: 'getPrjInfo.do',
				    type: 'POST',
				    data:{
				    	prj_id:create_prj_id
				    },
				    success: function onData (data) {
				    	var headerappend = '<tr class="prj_select" id="prj_id_is_'+create_prj_id+'"><td>'+data[0].prj_no+'</td><td>'+data[0].prj_nm+'</td><td>'+data[0].prj_full_nm+'</td></tr>';
				    	$('.header_prj_select_table').append(headerappend);
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function prjCreationEdit(){
	var prj_no = ($('#prj_no').val()).trim();
	var prj_nm = ($('#prj_nm').val()).trim();
	var prj_full_nm = ($('#prj_full_nm').val()).trim();
	var prj_hidden_yn = $('input:radio[name="prj_hidden_yn"]:checked').val();
	var prj_type = $('#prj_type').val();
	var client_code = ($('#client_code').val()).trim();
	var ship_no = ($('#ship_no').val()).trim();
	var DCCLIST = $('#DCCIDLIST').val();
	if(prj_no.length > 10){
		alert('PROJECT NO은 10자이내여야 합니다');
	}else if(prj_no==''){
		alert('PROJECT NO를 설정해야 합니다');
	}else if(prj_nm==''){
		alert('PROJECT NAME을 설정해야 합니다');
	}else if(prj_full_nm==''){
		alert('PROJECT FULL NAME을 설정해야 합니다');
	}else if(DCCLIST==''){
		alert('DCC를 설정해야 합니다');
	}else{	//프로젝트 정보 수정
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT CREATION",
		    	method: "수정"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		//console.log("selectPrjId = "+ selectPrjId);
		$.ajax({
		    url: 'updatePrjInfo.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId,
		    	prj_no:prj_no,
		    	prj_nm:prj_nm,
		    	prj_full_nm:prj_full_nm,
		    	prj_hidden_yn:prj_hidden_yn,
		    	prj_type:prj_type,
		    	client_code:client_code,
		    	ship_no:ship_no
		    },
		    success: function onData (data) {
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		var update_dcc_user_list=$('#DCCIDLIST').val();
		var origin_dcc_user_array = origin_dcc_user_list.split(',');
		var update_dcc_user_array = update_dcc_user_list.split(',');
		var diff_dcc_array = findUniqElem(origin_dcc_user_array,update_dcc_user_array);
		var diff_dcc_string = diff_dcc_array.join(',');
		console.log('diff_dcc_string:'+diff_dcc_string);
		$.ajax({
		    url: 'updateDCCPrjMember.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId,
		    	diff_dcc_user:diff_dcc_string
		    },
		    success: function onData (data) {
				alert('프로젝트 정보를 수정했습니다.');
		    	getPrjList();
    			//헤더 프로젝트 리스트 갱신
    			initPrjSelectGrid();
    			//project creation 내 copy project 리스트 갱신
    			initPrjSelectGridCreationCopy();
//		    	$.ajax({
//		    	    url: 'headerRefresh.do',
//		    	    type: 'POST',
//		    	    success: function onData (data) {
//		    	    	refreshPrjList(data);
//		    	    },
//		    	    error: function onError (error) {
//		    	        console.error(error);
//		    	    }
//		    	});
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
//두 배열 사이의 중복 값을 제거하여 배열로 돌려주는 함수
function findUniqElem(arr1, arr2) {
	  return arr1.concat(arr2)
	    	 .filter(item => !arr1.includes(item) || !arr2.includes(item));
}
function selectedDCCUserDelete(user_id,user_id_email,email){
	let arraySelectedUserId = $('#DCCIDLIST').val().split(',');
	//let arraySelectedUserKorNm = $('#DCCNAMELIST').val().split(',');
	let arraySelectedEmail = $('#DCCEMAILLIST').val().split(',');
	//let arraySelectedDCCIdEmail = $('#DCCIDEMAILLIST').val().split(',');
	let filteredselectedUserId = arraySelectedUserId.filter((element) => element !== user_id);
	//let filteredselectedUserIdUserKorNm = arraySelectedUserKorNm.filter((element) => element !== useridplususerkornm);
	let filteredselectedEmail = arraySelectedEmail.filter((element) => element !== email);
	//let filteredselectedIdEmail = arraySelectedDCCIdEmail.filter((element) => element !== user_id_email);
	/*let filteredselectedUserKorNm = [];
	for(let i=0;i<filteredselectedUserIdUserKorNm.length;i++){
		filteredselectedUserKorNm.push(filteredselectedUserIdUserKorNm[i].split('_')[1]);
	}
	console.log(filteredselectedUserKorNm);*/
	let selectedUserId = filteredselectedUserId.join(',');
	//let selectedUserKorNm = filteredselectedUserKorNm.join(',');
	let selectedEmail = filteredselectedEmail.join(',');
	//let selectedDCCIdEmail = filteredselectedIdEmail.join(',');
	$('#DCCIDLIST').val(selectedUserId);
	//$('#DCCIDEMAILLIST').val(selectedDCCIdEmail);
	$('#DCCEMAILLIST').val(selectedEmail);
	//$('#DCCNAMELIST').val(selectedUserKorNm);
	$('#projectCreation_small_box .'+user_id).remove();
	$('#DCCNAMELIST .'+user_id).remove();
}
