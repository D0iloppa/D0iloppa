var prjAllUserTable;
var prjAllUserTable2;
var prjAssignedTable;
var prjAssignedTable2;
var groupEditTable;
var origin_prj_member = [];
var origin_prj_group_member = [];

//false = 데이터 변경없을때, ture = 데이터 변경있고 저장 안했을때
var isEdited = false;

$(document).on("click", "#general08 ul li a", function () {
	
    var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
    
    if(!saveCheck08(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

function saveCheck08(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			isEdited = false;
			getPrjAssignedList();
			selectPrjGroupChange();
	        return callback();
	    }
	    else{
	        return false;
	    }
	}
	return true;
}

$(function(){
    $(".pop_groupEdit").dialog({
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
    $(document).on("click", "#groupEdit", function() {
        $(".pop_groupEdit").dialog("open");
        return false;
    });
    $(".pop_PrjGroupDelete").dialog({
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
    
    $("#prjmember_copy_checkbox").click(function(){
    	if($("#prjmember_copy_checkbox").is(":checked")) $(".copyDiv08").css('display','flex');
    	else $(".copyDiv08").css('display','none');
    });
    
    $(".copyChkdiv08").click(function(){
    	$("#prjmember_copy_checkbox").prop('checked', !$("#prjmember_copy_checkbox").is(":checked"));
    	if($("#prjmember_copy_checkbox").is(":checked")) $(".copyDiv08").css('display','flex');
    	else $(".copyDiv08").css('display','none');
    });
    
    getAllUserList();
    getPrjAssignedList();
    prjListLoad();
    getPrjNo();
    getPrjGroupList();
    selectPrjGroupChange();
    
    // 엔터키 form submit 방지
	$(document).keydown(function(event) {
		if ( event.keyCode == 13 || event.which == 13 ) {return false;}
	});
	
    
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('pg08_user_search') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 pg08_userSearch();
			    }
		}	   
	});
});


/**
 * 유저 검색
 * @returns
 */
function pg08_userSearch(){
	let thisGrid = prjAllUserTable;
	let keyword = $("#pg08_user_search").val();
	
	
	$.ajax({
	    url: 'pg08_searchUser.do',
	    type: 'POST',
	    data:{
	    	keyword : ('%'+keyword+'%')
	    },
	    success: function onData (data) {
	    	var search = [];
	    	for(let i=0;i<data.model.search.length;i++){
	    		let tmp = data.model.search[i];
	    		search.push({
	    			dcc: 'N',
	    			userid: tmp.user_id,
	    			kname: tmp.user_kor_nm,
	    			group: '',//data[0][i].user_group_nm,
	    			ename: tmp.user_eng_nm,
	    			usertype: tmp.user_type,
	    			company: tmp.company_nm,
	    			bean: tmp
	    		});
	    	}
	    	prjAllUserTable.setData(search);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}


function getAllUserList(){
	$.ajax({
	    url: 'getAllUsersVOList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var prjAllUser = [];
	    	for(i=0;i<data[0].length;i++){
	    		prjAllUser.push({
	    			dcc: 'N',
	    			userid: data[0][i].user_id,
	    			kname: data[0][i].user_kor_nm,
	    			group: '',//data[0][i].user_group_nm,
	    			ename: data[0][i].user_eng_nm,
	    			usertype: data[0][i].user_type,
	    			company: data[0][i].company_nm,
	    			bean: data[0][i]
	    		});
	    	}
	    	setPrjAllUserList(prjAllUser);
	    	setPrjAllUserList2(prjAllUser);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjAllUserList(prjAllUser){
    prjAllUserTable = new Tabulator("#prjAllUser", {
		height: "100%",
    	index: "userid",
        data: prjAllUser,
        layout: "fitColumns",
        selectable:true,
        columns: [{
                title: "USER ID",
                field: "userid"
            },
            {
                title: "KOR.NAME",
                field: "kname"
            },
            {
                title: "GROUP",
                field: "group"
            }, {
                title: "Eng.Name",
                field: "ename"
            },
            {
                title: "User type",
                field: "usertype"
            },
            {
                title: "company",
                field: "company"
            }
        ],
    });
}
function setPrjAllUserList2(prjAllUser){
    prjAllUserTable2 = new Tabulator("#prjAllUser2", {
		height: "100%",
    	index: "userid",
        data: prjAllUser,
        layout: "fitColumns",
        selectable:1,
        columns: [{
                title: "USER ID",
                field: "userid"
            },
            {
                title: "KOR.NAME",
                field: "kname"
            },
            {
                title: "GROUP",
                field: "group"
            }, {
                title: "Eng.Name",
                field: "ename"
            },
            {
                title: "User type",
                field: "usertype"
            },
            {
                title: "company",
                field: "company"
            }
        ],
    });
}
function getPrjAssignedList(){
	origin_prj_member=[];
	$.ajax({
	    url: 'getPrjMemberList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var prjAssigned = [];
	    	for(i=0;i<data[0].length;i++){
	    		prjAssigned.push({
	    			dcc: data[0][i].dcc_yn,
	    			userid: data[0][i].user_id,
	    			kname: data[0][i].user_kor_nm,
	    			group: '',//data[0][i].user_group_nm,
	    			ename: data[0][i].user_eng_nm,
	    			usertype: data[0][i].user_type,
	    			company: data[0][i].company_nm,
	    			bean: data[0][i]
	    		});
	    		origin_prj_member.push(data[0][i].user_id);
	    	}
	    	setPrjAssignedList(prjAssigned);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjAssignedList(prjAssigned){
	prjAssignedTable = new Tabulator("#prgAssigned", {
		height: "100%",
    	index: "userid",
        data: prjAssigned,
        layout: "fitColumns",
        selectable:1,
        columns: [{
                title: "DCC",
                field: "dcc"
            }, {
                title: "USER ID",
                field: "userid"
            },
            {
                title: "KOR.NAME",
                field: "kname"
            },
            {
                title: "GROUP",
                field: "group"
            }, {
                title: "Eng.Name",
                field: "ename"
            },
            {
                title: "User type",
                field: "usertype"
            },
            {
                title: "company",
                field: "company"
            }
        ],
    });
}
function prjMemberAssignment(){
	var origin_user_list = [];
	for(i=0;i<prjAssignedTable.getRows().length;i++){
		origin_user_list[i] = prjAssignedTable.getColumn("userid").getCells()[i].getValue();
	}
	for(let i=0;i<prjAllUserTable.getSelectedData().length;i++){
		origin_user_list[prjAssignedTable.getRows().length+(i+1)] = prjAllUserTable.getSelectedData()[i].userid;
		var user_list_duplicate = isDuplicate(origin_user_list);
		if(!user_list_duplicate){
			var selectedUser = prjAllUserTable.getRow(prjAllUserTable.getSelectedData()[i].userid).getData();
			prjAssignedTable.addRow(selectedUser);
			isEdited = true;
		}
	}
}
function prjMemberRemove(){
	prjAssignedTable.deleteRow(prjAssignedTable.getSelectedData()[0].userid);
	isEdited = true;
}
function prjUserApply(){
	isEdited = false;
	var new_prj_member = [];
	for(i=0;i<prjAssignedTable.getRows().length;i++){
		new_prj_member[i] = prjAssignedTable.getColumn("userid").getCells()[i].getValue();
	}
	var diff_member_array = findUniqElem(origin_prj_member,new_prj_member);
	if(diff_member_array.length===0){
		alert('There are no data to save.');
	}else{
		var diff_user_string = '';
		for(i=0;i<diff_member_array.length;i++){
			diff_user_string += diff_member_array[i]+'&&';
		}
		diff_user_string = diff_user_string.slice(0,-2);

		$.ajax({
		    url: 'prjMemberApply.do',
		    type: 'POST',
		    data: {
		    	prj_id: selectPrjId,
		    	diff_user_string: diff_user_string
		    },
		    success: function onData (data) {
				alert('프로젝트 멤버를 수정하였습니다.');
				getPrjAssignedList();
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
function copyPrjMember(){
	if($('#prjmember_copy_checkbox').is(":checked")){
		var copy_prj_id = $('#selectCopyPrjMemeber').val();
		if(copy_prj_id!=selectPrjId){
			$.ajax({
			    url: 'copyPrjMember.do',
			    type: 'POST',
			    data: {
			    	prj_id: selectPrjId,
			    	copy_prj_id: copy_prj_id
			    },
			    success: function onData (data) {
					getPrjAssignedList();
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
	}
}
function prjListLoad(){
	$.ajax({
	    url: 'headerRefresh.do',
	    type: 'POST',
	    success: function onData (data) {
	    	getAllPrjList(data);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getAllPrjList(data){

	var html = '<table class="prjMember_prj_select_table"><tr><th>Project No</th>'
		+ '<th>Project Name</th><th>Project Full Name</th></tr>';
	
	for(var i=0;i<data[0].length;i++){
		html += '<tr class="prj_select prjmember_select_tr" id="prjMember_prj_id_is_'+data[0][i].prj_id+'">'
		+ '<td>'+data[0][i].prj_no+'</td>'
		+ '<td class="prj_nm">'+data[0][i].prj_nm+'</td>'
		+ '<td>'+data[0][i].prj_full_nm+'</td>'
		+ '</tr>';
	}
	$('#prjMember_multi_box').html(html);

	$('#prjmember_select_prj').click(function(){
		$('#prjMember_multi_sel_wrap').addClass('on');
	});
	$('.prjmember_select_tr').click(function(){
		var prjMember_select_prj_id = $(this).attr('id').replace('prjMember_prj_id_is_','');
		$('#selectCopyPrjMemeber').val(prjMember_select_prj_id);
		$("#prjmember_select_prj").text($(this).children().next().html());
		$('#prjMember_multi_sel_wrap').removeClass('on');
	});
}
function getPrjNo(){
	$.ajax({
	    url: 'getPrjInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectPrjId
	    },
	    success: function onData (data) {
	    	$('#selectPrjNo').val(data[0].prj_no+'_');
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function saveGroup(){
	var selectPrjNo = $('#selectPrjNo').val();
	var saveGroupName = $('#saveGroupName').val();
	var groupDesc = $('#groupDesc').val();
	if(saveGroupName===''){
		alert('그룹 이름을 입력해주세요.');
	}else if(groupDesc===''){
		alert('그룹 설명을 입력해주세요.')
	}else{
		$.ajax({
		    url: 'insertPrjGroup.do',
		    type: 'POST',
		    data:{
		    	prj_id:selectPrjId,
		    	set_code:selectPrjNo+saveGroupName,
		    	set_desc:groupDesc
		    },
		    success: function onData (data) {
		    	getPrjGroupList();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function getPrjGroupList(){
	$.ajax({
	    url: 'getPrjGroupList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    async:false,
	    success: function onData (data) {
	    	var tblGroupEdit = [];
	    	var html = '';
	    	for(i=0;i<data[0].length;i++){
	    		tblGroupEdit.push({
	    			groupname: data[0][i].set_code,
	    			groupdesc: data[0][i].set_desc,
	    			set_code_id: data[0][i].set_code_id
	    		});
	    		html += '<option value="'+data[0][i].set_code+'">'+data[0][i].set_code+'</option>';
	    	}
	    	setPrjGroupList(tblGroupEdit);
	    	$('#selectPrjGroupList').html(html);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjGroupList(tblGroupEdit){
	groupEditTable = new Tabulator("#tblGroupEdit", {
		height: "100%",
    	index: "groupname",
        data: tblGroupEdit,
        layout: "fitColumns",
        selectable:1,
	    columns: [{
	        title: "GROUP NAME",
	        field: "groupname"
	    }, {
	        title: "GROUP DESCRIPTION",
	        field: "groupdesc"
	    }, {
	        title: "set_code_id",
	        field: "set_code_id",
	        visible: false
	    }],
	});
}
function deleteGroupPopup(){
	if(groupEditTable.getSelectedData().length == 1){
		$(".pop_PrjGroupDelete").dialog("open");
	}else{
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}
}
function deleteGroupCancel(){
    $(".pop_PrjGroupDelete").dialog("close");
}
function deleteGroup(){
	var set_code_id = groupEditTable.getSelectedData()[0].set_code_id;
	$.ajax({
	    url: 'deletePrjGroup.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	set_code_id: set_code_id
	    },
	    success: function onData (data) {
	        $(".pop_PrjGroupDelete").dialog("close");
	        alert('삭제되었습니다');
	    	getPrjGroupList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function groupToExcel(){
//	groupEditTable.download("xlsx", "data.xlsx", {sheetName:"Project Group List"});
	
	let gridData = groupEditTable.getData();
//	console.log(gridData);
	
	let jsonList = [];
	
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'Project_Group_List';
	
	var f = document.excelUploadForm_pg08_groupEdit;
	
	f.prj_id08_groupEdit.value = selectPrjId;
	f.prj_nm08_groupEdit.value = getPrjInfo().full_nm;
    f.fileName08_groupEdit.value = fileName;
   	f.jsonRowdatas08_groupEdit.value = jsonList;
    f.action = "pg08prgroupEditExcelDownload.do";
   	f.submit();
	
}
function selectPrjGroupChange(){
	var group_id = $('#selectPrjGroupList').val();
	origin_prj_group_member=[];
	$.ajax({
	    url: 'getPrjGroupMemberList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId,
	    	group_id: group_id
	    },
	    success: function onData (data) {
	    	var prjAssigned2 = [];
	    	for(i=0;i<data[0].length;i++){
	    		prjAssigned2.push({
	    			userid: data[0][i].user_id,
	    			kname: data[0][i].user_kor_nm,
	    			group: '',//data[0][i].user_group_nm,
	    			ename: data[0][i].user_eng_nm,
	    			usertype: data[0][i].user_type,
	    			company: data[0][i].company_nm,
	    			bean: data[0][i]
	    		});
	    		origin_prj_group_member.push(data[0][i].user_id);
	    	}
	    	setPrjGroupAssignedList(prjAssigned2);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjGroupAssignedList(prjAssigned2){
	prjAssignedTable2 = new Tabulator("#prgAssigned2", {
		height: "100%",
    	index: "userid",
        data: prjAssigned2,
        layout: "fitColumns",
        selectable:1,
        columns: [{
                title: "USER ID",
                field: "userid"
            },
            {
                title: "KOR.NAME",
                field: "kname"
            },
            {
                title: "GROUP",
                field: "group"
            }, {
                title: "Eng.Name",
                field: "ename"
            },
            {
                title: "User type",
                field: "usertype"
            },
            {
                title: "company",
                field: "company"
            }
        ],
    });
}
function prjGroupMemberAssignment(){
	var origin_user_list = [];
	for(i=0;i<prjAssignedTable2.getRows().length;i++){
		origin_user_list[i] = prjAssignedTable2.getColumn("userid").getCells()[i].getValue();
	}
	origin_user_list[prjAssignedTable2.getRows().length+1] = prjAllUserTable2.getSelectedData()[0].userid;
	var user_list_duplicate = isDuplicate(origin_user_list);
	if(!user_list_duplicate){
		var selectedUser = prjAllUserTable2.getRow(prjAllUserTable2.getSelectedData()[0].userid).getData();
		prjAssignedTable2.addRow(selectedUser);
		isEdited = true;
	}
}
function prjGroupMemberRemove(){
	prjAssignedTable2.deleteRow(prjAssignedTable2.getSelectedData()[0].userid);
	isEdited = true;
}
function prjGroupUserApply(){
	isEdited = false;
	var group_id = $('#selectPrjGroupList').val();
	var new_prj_member = [];
	for(i=0;i<prjAssignedTable2.getRows().length;i++){
		new_prj_member[i] = prjAssignedTable2.getColumn("userid").getCells()[i].getValue();
	}
	var diff_member_array = findUniqElem(origin_prj_group_member,new_prj_member);
	if(diff_member_array.length===0){
		alert('There are no data to save.');
	}else{
		var diff_user_string = '';
		for(i=0;i<diff_member_array.length;i++){
			diff_user_string += diff_member_array[i]+'&&';
		}
		diff_user_string = diff_user_string.slice(0,-2);
		
		$.ajax({
		    url: 'prjGroupMemberApply.do',
		    type: 'POST',
		    data: {
		    	prj_id: selectPrjId,
		    	group_id: group_id,
		    	diff_user_string: diff_user_string
		    },
		    success: function onData (data) {
				alert('프로젝트 그룹 멤버를 수정하였습니다.');
				selectPrjGroupChange();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function prjMemberToExcel(){
//	prjAssignedTable.download("xlsx", "data.xlsx", {sheetName:"Project Member List"});
	
	let gridData = prjAssignedTable.getData();	
	console.log(gridData);
	
	let jsonList = [];
	
	for (let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'Project_Member_List';
	
	var f = document.excelUploadForm_pg08_prUser;
	
	f.prj_id08_prUser.value = selectPrjId;
	f.prj_nm08_prUser.value = getPrjInfo().full_nm;
    f.fileName08_prUser.value = fileName;
   	f.jsonRowdatas08_prUser.value = jsonList;
    f.action = "pg08prUserExcelDownload.do";
    f.submit();	
}
function prjGroupMemberToExcel(){
//	prjAssignedTable2.download("xlsx", "data.xlsx", {sheetName:"Project Group Member List"});
	
	let gridData = prjAssignedTable2.getData();
	console.log(gridData);
	
	let jsonList = [];
	
	for (let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'Project_Group_Member_List';
	
	var group_id = $('#selectPrjGroupList').val();
	
	var f = document.excelUploadForm_pg08_prGroup;
	
	f.prj_id08_prGroup.value = selectPrjId;
	f.prj_nm08_prGroup.value = getPrjInfo().full_nm;
    f.fileName08_prGroup.value = fileName;
    f.group_id08_prGroup.value = group_id;
   	f.jsonRowdatas08_prGroup.value = jsonList;
    f.action = "pg08prGroupExcelDownload.do";
    f.submit();	
}