var userMngPage = 'ADMIN';	//DCC가 볼 수 있는 페이지이지만 현대중공업파워시스템의 정보를 봐야하기 때문에 ADMIN. 해당페이지에서 편집은 불가
var userRegisterListTable;
var orgList;
var folderList;
var AllOrGroup = 0;	//0일땐 All user 리스트, 1일땐 그룹 리스트, 2일땐 서치 리스트
var G_userRegister = {};
var setOrderArray = new Array();
var addOrUpdate = 'update';

var organizationUse = opener.document.getElementById('organizationUse').value;
$(function(){
    getUserList();
    getOrganizationTree();
});

function treeReload(organizationList){
	$("#userRegGroup").jstree(true).settings.core.data = organizationList;
	$("#userRegGroup").jstree(true).refresh();
}
function usersSearchReged(){
	var searchUser = $('input[name="searchUser"]:checked').val();
	var searchKeyword = $('#usersSearchKeyword2').val();
	if(searchKeyword=='' || searchKeyword==undefined || searchKeyword==null){
		alert('검색어를 입력해주세요.');
	}else{
		let postUrl = '';
		if(organizationUse==='인계자'){
			postUrl = '../searchUsersHyundaeVOPlusDeleteUser.do';
		}else if(organizationUse==='인수자'){
			postUrl = '../searchUsersHyundaeVO.do';
		}
		$.ajax({
		    url: postUrl,
		    type: 'POST',
		    data:{
		    	searchUser:searchUser,
		    	searchKeyword:searchKeyword
		    },
		    success: function onData (data) {
		    	var userLi = [];
		    	if(data == null || data == "") {
		    		alert("검색하신 사용자 정보가 없습니다.");
		    		return;
		    	}
		    	for(i=0;i<data[0].length;i++){
		    		var createDate='';
		    		if(data[0][i].reg_date!=null){
		    			createDate = getDateFormat_YYYYMMDD(data[0][i].reg_date);
		    		}
		    		let passwd_fail_status = 0;
		    		if(data[0][i].passwd_fail_acc_stop_use_yn==='Y'){
			    		if(data[0][i].connect_fail_cnt >= data[0][i].passwd_fail_acc_stop_cnt){
			    			passwd_fail_status = 1;
			    		}
		    		}
		    		userLi.push({
	                    No: i+1,
	                    createDate: createDate,
			            userId: data[0][i].user_id,
			            korName: data[0][i].user_kor_nm,
			            group: data[0][i].user_group_nm,
			            orgId: data[0][i].org_id,
			            engName: data[0][i].user_eng_nm,
			            company: data[0][i].company_nm,
			            infoUpdate: data[0][i].user_id,
			            passwdReset: data[0][i].user_id,
			            loginReset: passwd_fail_status,
			            bean: data[0][i]
		    		});
		    	}
		    	setUserLi(userLi);
				AllOrGroup = 2;
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function getUserList(){
	let postUrl = '';
	if(organizationUse==='인계자'){
		postUrl = '../getAllUserListHyundaePlusDeleteUser.do';
	}else if(organizationUse==='인수자'){
		postUrl = '../getAllUserListHyundae.do';
	}
	$.ajax({
	    url: postUrl,
	    type: 'POST',
	    success: function onData (data) {
	    	var userLi = [];
	    	for(i=0;i<data[0].length;i++){
	    		var createDate='';
	    		if(data[0][i].reg_date!=null){
	    			createDate = getDateFormat_YYYYMMDD(data[0][i].reg_date);
	    		}
	    		let passwd_fail_status = 0;
	    		if(data[0][i].passwd_fail_acc_stop_use_yn==='Y'){
		    		if(data[0][i].connect_fail_cnt >= data[0][i].passwd_fail_acc_stop_cnt){
		    			passwd_fail_status = 1;
		    		}
	    		}
	    		userLi.push({
                    No: i+1,
                    createDate: createDate,
		            userId: data[0][i].user_id,
		            korName: data[0][i].user_kor_nm,
		            group: data[0][i].user_group_nm,
		            orgId: data[0][i].org_id,
		            engName: data[0][i].user_eng_nm,
		            company: data[0][i].company_nm,
		            infoUpdate: data[0][i].user_id,
		            passwdReset: data[0][i].user_id,
		            loginReset: passwd_fail_status,
		            bean: data[0][i]
	    		});
	    	}
	    	setUserLi(userLi);
			AllOrGroup = 0;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setUserLi(userLi){
    userRegisterListTable = new Tabulator("#userLi", {
    	index: "No",	//No 필드를 인덱스 필드로 설정
        data: userLi,
        layout: "fitColumns",
        selectable:1,
        height: 400,
        columns: [{
                title: "No",
                field: "No",
    		    width: 20
            },
            {
                title: "User ID",
                field: "userId",
                width: 80
            },
            {
                title: "kor. Name",
                field: "korName",
                width: 100
            },
            {
                title: "Group",
                field: "group",
                hozAlign: "left"
            }
        ],
    });
    
}
function setSelectUser(){
	if(userRegisterListTable.getSelectedData().length===0){
		alert('유저를 선택하세요.');
	}else{
		let rowData = userRegisterListTable.getSelectedData()[0];
		if(organizationUse==='인계자'){
			opener.document.getElementById('preDesignerSelectedUser').innerHTML = rowData.korName + '(' + rowData.userId + ')';
			opener.document.getElementById('preDesignerSelectedUserId').value = rowData.userId;
			opener.searchDesignerProject();
		}else if(organizationUse==='인수자'){
			opener.document.getElementById('futureDesignerSelectedUser').innerHTML = rowData.korName + '(' + rowData.userId + ')';
			opener.document.getElementById('futureDesignerSelectedUserId').value = rowData.userId;
		}
		window.close();
	}
}
function getOrganizationListPlusUser() {
	var organizations = [];
	 $.ajax({
			type: 'POST',
			url: '../getOrganizationList.do',
			data: {
				userMngPage: userMngPage
			},
			async: false,
			success: function(data) {
				var tmp_organization_List = data[0];
				
				for(var i=0 ; i < tmp_organization_List.length; ++i){
					var tmp_Node = tmp_organization_List[i];
					
					var	input_Organization = { "id" : tmp_Node.org_id, "parent" : tmp_Node.org_parent_id, 'text' : tmp_Node.org_nm, 'type' : tmp_Node.org_type ,'folder_path':tmp_Node.org_path, 'isFolder' : true};

					if(tmp_Node.org_id === tmp_Node.org_parent_id){ // 최상위 루트
						input_Organization.parent = "#";
						input_Organization.icon = "../resource/images/pinwheel.png";
						input_Organization.state =  {'opened' : true,'selected' : false};
					}
					
					organizations.push(input_Organization);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 

	 $.ajax({
			type: 'POST',
			url: '../getOrganizationUserListAll.do',
			async: false,
			success: function(data) {
				var tmp_organization_user_List_All = data[0];
				
				for(var i=0 ; i < tmp_organization_user_List_All.length; ++i){
					var tmp_Node = tmp_organization_user_List_All[i];
					
					var	input_Organization_user = { "id" : tmp_Node.user_id+tmp_Node.org_id, 'user_id':tmp_Node.user_id, "parent" : tmp_Node.org_id, 'text' : tmp_Node.user_id + '(' + tmp_Node.user_kor_nm + ')', 'type' : tmp_Node.org_type , 'folder_path' : tmp_Node.org_path, 'icon' : "user_icon", 'isFolder' : false};

					organizations.push(input_Organization_user);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 return organizations;
}
function getOrganizationList() {
	var organizations = [];
	 $.ajax({
			type: 'POST',
			url: '../getOrganizationListOnlyHyundae.do',
			async: false,
			success: function(data) {
				var tmp_organization_List = data[0];
				
				for(var i=0 ; i < tmp_organization_List.length; ++i){
					var tmp_Node = tmp_organization_List[i];
					
					var	input_Organization = { "id" : tmp_Node.org_id, "parent" : tmp_Node.org_parent_id, 'text' : tmp_Node.org_nm, 'type' : tmp_Node.org_type ,'folder_path':tmp_Node.org_path, 'isFolder' : true};

					if(tmp_Node.org_id === tmp_Node.org_parent_id){ // 최상위 루트
						input_Organization.parent = "#";
						input_Organization.icon = "../resource/images/pinwheel.png";
						input_Organization.state =  {'opened' : true,'selected' : false};
					}
					
					organizations.push(input_Organization);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 return organizations;
}
var newOrg = false;
function getOrganizationTree(){
	orgList = getOrganizationList();
	folderList = orgList;

	var userRegGrouptree = $('#userRegGroup').jstree({
	     "core" : {
	    	 'data' : orgList,
	         "check_callback" : true
	       }
	}).on('click.jstree', function(event) {
		var node = $(event.target).closest("li");
		var id = node[0].id;
		var text = node[0].querySelector("a").childNodes[1].nodeValue;
		if(!node[0].children[1].childNodes[0].classList.contains('user_icon')){
			$('#clickNodeId').val(id);
			$('#clickNodeText').val(text);
			groupUserSearch();
		}
		var node = $(event.target).closest("li");
		var org_id = node[0].id;
		$('#regOrgId').val(org_id);
		var node2=$('#userRegGroup').jstree("get_selected", true);
		$("#regGroupLevelPath").html($('#userRegGroup').jstree().get_path(node2[0], ' > '));
	});
	G_userRegister.userRegGrouptree = userRegGrouptree;
	
	$('#userRegGroup').jstree(true).refresh();
}
function allOpen(){
	G_userRegister.userRegGrouptree.jstree("open_all");
}
function allClose(){
	G_userRegister.userRegGrouptree.jstree("close_all");
}
function groupUserSearch(){
	var org_id = $('#clickNodeId').val();
	
	if(org_id == "" || org_id == null) {
		alert("검색하실 부서를 선택해주세요.");
		return;
	}
	let postUrl = '';
	if(organizationUse==='인계자'){
		postUrl = '../getOrganizationUserListPlusDeleteUser.do';
	}else if(organizationUse==='인수자'){
		postUrl = '../getOrganizationUserList.do';
	}
	 $.ajax({
			type: 'POST',
			url: postUrl,
			data:{
				org_id: org_id
			},
			success: function(data) {
		    	var userLi = [];
		    	for(i=0;i<data[0].length;i++){
		    		var createDate='';
		    		if(data[0][i].reg_date!=null){
		    			createDate = getDateFormat_YYYYMMDD(data[0][i].reg_date);
		    		}
		    		let passwd_fail_status = 0;
		    		if(data[0][i].passwd_fail_acc_stop_use_yn==='Y'){
			    		if(data[0][i].connect_fail_cnt >= data[0][i].passwd_fail_acc_stop_cnt){
			    			passwd_fail_status = 1;
			    		}
		    		}
		    		userLi.push({
	                    No: i+1,
	                    createDate: createDate,
			            userId: data[0][i].user_id,
			            korName: data[0][i].user_kor_nm,
			            group: data[0][i].user_group_nm,
			            orgId: data[0][i].org_id,
			            engName: data[0][i].user_eng_nm,
			            company: data[0][i].company_nm,
			            infoUpdate: data[0][i].user_id,
			            passwdReset: data[0][i].user_id,
			            loginReset: passwd_fail_status,
			            bean: data[0][i]
		    		});
		    	}
		    	setUserLi(userLi);
				AllOrGroup = 1;
			},
			error: function onError (error) {
			    console.error(error);
			}
		});
}
function getDateFormat_YYYYMMDD(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);

	return year+'-'+month+'-'+day;
}

//alert창 함수 오버라이딩
function alert(arg){
    //console.log(arg);
	
	$(".pop_alertDialog").dialog({
	    draggable: true,
	    autoOpen: false,
	    maxWidth:1000,
	    width:"auto",
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
	
    $(".pop_alertDialog").dialog("open");
    $("#alertText").html(arg);
}

function alertDialogOk() {
	$(".pop_alertDialog").dialog("close");
}
