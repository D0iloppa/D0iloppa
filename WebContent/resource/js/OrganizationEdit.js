var userMngPage = 'ADMIN';
var userRegisterListTable;
var orgList;
var folderList;
var AllOrGroup = 0;	//0일땐 All user 리스트, 1일땐 그룹 리스트, 2일땐 서치 리스트
var G_userRegister = {};

var userJobTypeListTbl;
var setOrderArray = new Array();
var addOrUpdate = 'update';
var code_gubun;
var typeList = [];
var session_user_id = opener.document.getElementById('session_user_id').value;
$(function(){
	$("#userJobCode_UpBtn").click(function() { stepRowUp( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	$("#userJobCode_DownBtn").click(function() { stepRowDown( function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);} ) } );
	
	$(".pop_userJobCode").dialog({
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
	$(".pop_userJobCodeDelete").dialog({
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
	$(document).on("click","#userJobCode", function() {
		$(".pop_userJobCode").dialog("open");
		getUserJobTypeList();
        return false;
	});
    $(".pop_userRegstration").dialog({
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
    $(document).on("click", "#userRegstration", function() {
        $(".pop_userRegstration").dialog("open");
        $('#reg_user_id').val('');
        $('#reg_user_kor_nm').val('');
        $('#reg_user_eng_nm').val('');
        $('#reg_email_addr_1').val('');
        $('#reg_email_addr_2').val('');
        $('#reg_offi_tel').val('');
        $('#reg_company_nm').val('');
        $('#idCheckWarning').html('');
        $('#pwdCheckWarning').html('');
        $('#pwdConfirmWarning').html('');
        $('#reg_user_type').val('');
    	$('#reg_hld_offi_gbn').val('');
    	$('#reg_job_tit_cd').val('');
    	$('#reg_offi_res_cd').val('');
        return false;
    });
	$(".pop_rootOrgDelete").dialog({
        autoOpen: false,
        Width: 700,
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
    $(".pop_userInfoUpdate").dialog({
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
    $(".pop_chgGroup").dialog({
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
    $(".pop_addGroup").dialog({
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
    $(".pop_loginInitialize").dialog({
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
	$('.pop_passwdReset').dialog({
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
    $(document).on("click", "#addGroup", function() {
    	if($("input:checkbox[name='userList']:checked").length===0){
    		alert('부서를 추가할 사용자를 선택하십시오.');
    	}else{
            $(".pop_addGroup").dialog("open");
    		$("#addGroupLevelPath").html('');
    		$('#addOrgId').val('');
            return false;
    	}
    });
    $(document).on("click", "#chgGroup", function() {
    	if($("input:checkbox[name='userList']:checked").length===0){
    		alert('부서를 변경할 사용자를 선택하십시오.');
    	}else{
            $(".pop_chgGroup").dialog("open");
    		$("#groupLevelPath").html('');
    		$('#chgOrgId').val('');
            return false;
    	}
    });
    $(".pop_deleteOrgConfirm").dialog({
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
    $(".pop_deleteOrg").dialog({
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
    $(".pop_deleteUser").dialog({
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
    $(document).on("click", ".loginInitialize", function() {
        $(".pop_loginInitialize").dialog("open");
        return false;
    });
    
    getUserList();
    getOrganizationTree();
    getAddGroupTree();
    getChgGroupTree();
    getRegGroupTree();
    getJobCodeList();
    getOffiCodeList();

});

function getJobCodeList(){
	$.ajax({
	    url: '../getJobCodeList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		html += '<option value="'+data[0][i].code+'">'+data[0][i].code_nm+'</option>';
	    	}
	    	$('.job_tit_cd').html(html);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function getOffiCodeList(){
	$.ajax({
	    url: '../getOffiCodeList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		html += '<option value="'+data[0][i].code+'">'+data[0][i].code_nm+'</option>';
	    	}
	    	$('.offi_res_cd').html(html);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function treeReload(organizationList){
	$("#userRegGroup").jstree(true).settings.core.data = organizationList;
	$("#userRegGroup").jstree(true).refresh();
	$("#userRegPopGroup").jstree(true).settings.core.data = organizationList;
	$("#userRegPopGroup").jstree(true).refresh();
	$("#addPopGroup").jstree(true).settings.core.data = organizationList;
	$("#addPopGroup").jstree(true).refresh();
	$("#chgPopGroup").jstree(true).settings.core.data = organizationList;
	$("#chgPopGroup").jstree(true).refresh();
}
function usersSearchReged(){
	var searchUser = $('input[name="searchUser"]:checked').val();
	var searchKeyword = $('#usersSearchKeyword2').val();
	if(searchKeyword=='' || searchKeyword==undefined || searchKeyword==null){
		alert('검색어를 입력해주세요.');
	}else{
		$.ajax({
		    url: '../searchUsersHyundaeVO.do',
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
			    		if(data[0][i].passwd_fail_acc_stop_cnt===0){
			    			passwd_fail_status = 0;
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
		    		console.log(data[0][i].org_id);
		    	}
		    	console.log(userLi);
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
	$.ajax({
	    url: '../getAllUserListHyundae.do',
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
		    		if(data[0][i].passwd_fail_acc_stop_cnt===0){
		    			passwd_fail_status = 0;
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
    	movableRows: true, //enable movable rows
    	movableRowsConnectedElements: "#userRegGroup .jstree-anchor", //element to receive rows
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
                width: 50
            },
            {
                title: "Group",
                field: "group",
                width: 450,
                hozAlign: "left"
            },
            {
                title: "정보 수정",
                field: "infoUpdate",
    			headerSort:false,
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var button = document.createElement("button");
    		     	button.classList.add('button');
    		     	button.classList.add('btn_gray');
    		     	button.classList.add('btn_sml');
    		     	button.classList.add('userInfoUpdate');
    		     	button.innerText = '수정';
    		     	button.id = 'userInfoUpdate'+cell.getRow().getPosition();
    		     	button.onclick = function() {userUpdateOpen(cell.getRow().getPosition());};
    		        return button;
    			}
            },
            {
                title: "비밀번호 초기화",
                field: "passwdReset",
    			headerSort:false,
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var button = document.createElement("button");
    		     	button.classList.add('button');
    		     	button.classList.add('btn_gray');
    		     	button.classList.add('btn_sml');
    		     	button.classList.add('passwdReset');
    		     	button.innerText = '초기화';
    		     	button.id = 'passwdReset'+cell.getRow().getPosition();
    		     	button.onclick = function() {passwdResetOpen(cell.getRow().getPosition());};
    		        return button;
    			}
            },
            {
                title: "상태",
                field: "loginReset",
    			formatter: function(cell, formatterParams, onRendered) {
    				let cellValue = cell.getValue();
    				if(cellValue===1){
    					let div = document.createElement("div");
    					let span = document.createElement("span");
    					span.classList.add('userStop');
    					span.innerHTML = '중지   ';
        		     	var button = document.createElement("button");
        		     	button.classList.add('button');
        		     	button.classList.add('btn_gray');
        		     	button.classList.add('loginInitialize');
        		     	button.innerText = '초기화';
        		     	button.id = 'loginInitialize'+cell.getRow().getPosition();
        		     	div.append(span);
        		     	div.append(button);
        		        return div;
    				}else{
    					let div = document.createElement("div");
    					let span = document.createElement("span");
    					span.classList.add('userNormality');
    					span.innerHTML = '정상';
    					div.append(span);
    					return div;
    				}
    			},
    			width: 90,
            },{
            	titleFormatter:function(cell, formatterParams, onRendered) {
	   		         var checkbox = document.createElement("input");
	   		          checkbox.id = 'userListAll';
	   		          checkbox.name = 'userListAll';
	   		          checkbox.type = 'checkbox';
	   		          return checkbox;
	   			},
    			formatter: function(cell, formatterParams, onRendered) {
    		     	var checkbox = document.createElement("input");
    		     	checkbox.classList.add('userList');
	   		        checkbox.id = 'userList'+cell.getRow().getPosition();
    		        checkbox.name = 'userList';
    		        checkbox.type = 'checkbox';
    		        return checkbox;
    			},
    			hozAlign:"center", 
    			headerSort:false,
    			width:10,
    			headerClick:function(e, column){
    				$('input:checkbox[name="userListAll"]').change(function(){
    			        if($('input:checkbox[name="userListAll"]').is(":checked")){
            				userRegisterListTable.selectRow();
            				$("input:checkbox[name='userList']").prop("checked", true);
    			        }else{
        					userRegisterListTable.deselectRow();
            				$("input:checkbox[name='userList']").prop("checked", false);
    			        }
    			    });
    			},
    			cellClick:function(e, cell){
    				//$(this).getRow().toggleSelect();
    		    }
    		}
        ],
    });

	//listen for row to be dropped on element
    userRegisterListTable.on("movableRowsElementDrop", function(e, element, row){
	  	//e - mouseup event object
	  	//element - node object for the element that the row was dropped onto
	  	//row - row component for the row that was moved
	
    	/*console.log(e);
    	console.log(element);
    	console.log(row);*/
    	
    	G_userRegister.element = element;
    	G_userRegister.row = row;
    	
    	if(element.childNodes[0].classList.contains('user_icon')){
    		alert('사용자를 사용자가 아닌 부서에 드래그앤 드롭해주세요.');
    	}else{
    		let checkedUserString = '';
    		checkedUserString += row.getData().userId + '&&' + row.getData().orgId +'&&' + element.id.replaceAll('_anchor','') + '@@';
    		chgGroupSave(checkedUserString);
    	}
	  	//add a li to the drop element containing the name property from the row data
	  	/*var li = document.createElement("li");
	  	li.textContent = row.getData().name;
	  	element.appendChild(li);*/
	  });
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

	function groupContextMenu(node) {
		var tree = $("#userRegGroup").jstree(true);
		
	    var items = {
		        createRoot: {
		            "label": "Add Root",
		            "action": function(data) {
		            	$("#userRegGroup").jstree("create_node", null, null, "last", function (node) {
		                    this.edit(node);
	                    	$.ajax({
	            			    url: '../createNewRootOrg.do',
	            			    type: 'POST',
	            			    data:{
	            			    	org_nm:node.text
	            			    },
	            			    success: function onData (data) {
		        			    	var data2 = getOrganizationList();
		        			    	treeReload(data2);
	            			    },
	            			    error: function onError (error) {
	            			        console.error(error);
	            			    }
	            			});
		                });
		            }
		        },
	        createItem: {
	            "label": "Add Dept",
	            "action": function(data) {
	            	newOrg = true;
	            	var inst = $.jstree.reference(data.reference),
                    obj = inst.get_node(data.reference);
	            	inst.create_node(obj, {}, "last", function (new_node) {
	            		let selected_idx = $("#userRegGroup").jstree("get_selected")[0];
	            		let org_type = $("#userRegGroup").jstree("get_node",selected_idx).original.type;
	            		new_node.data = {file: true};
	            		setTimeout(function () { inst.edit(new_node); },0);
	                   $('#userRegGroup').on('rename_node.jstree', function (e, data) {
	                    	if(data.node.parent==new_node.parent && data.text==new_node.text && newOrg===true){
		                    	$.ajax({
		            			    url: '../createNewOrg.do',
		            			    type: 'POST',
		            			    data:{
		            			    	org_parent_id:data.node.parent,
		            			    	org_nm:data.text,
		            			    	org_type:org_type
		            			    },
		            			    success: function onData (data) {
			        			    	var data2 = getOrganizationList();
			        			    	treeReload(data2);
		            			    },
		            			    error: function onError (error) {
		            			        console.error(error);
		            			    }
		            			});
	                    	}
		            	});
	                });
	            }
	        },
	        renameItem: {
	            "label": "Edit Dept",
	            "action": function(obj) {
	            	newOrg = false;
	            	tree.edit(node);
	            	var org_nm = node.text;
	            	$('#userRegGroup').on('rename_node.jstree', function (e, data) {
	            		$.ajax({
	            			    url: '../renameOrg.do',
	            			    type: 'POST',
	            			    data:{
	            			    	org_id: data.node.id,
	            			    	org_nm: org_nm,
	            			    	new_org_nm:data.text
	            			    },
	            			    success: function onData (data) {
		        			    	var data2 = getOrganizationList();
		        			    	treeReload(data2);
	            			    },
	            			    error: function onError (error) {
	            			        console.error(error);
	            			    }
	            		});
	            	});
	            }
	        },
	        deleteItem: {
	            "label": "Delete Dept",
	            "action": function(obj) {
	            	if(node.parent==='#'){//삭제하려는 노드가 루트 노드일 경우
		            	$.ajax({
	        			    url: 'deleteRootOrg.do',
	        			    type: 'POST',
	        			    data:{
	        			    	org_id: node.id
	        			    },
	        			    success: function onData (data) {
	        			    	if(data[0]>0){
		        			    	$(".pop_rootOrgDelete").dialog("open");
	        			    	}else{
		        			    	var data2 = getOrganizationList();
		        			    	treeReload(data2);
					            	if(AllOrGroup === 0){
					            		getUserList();
					            	}else if(AllOrGroup === 1){
					            		groupUserSearch();
					            	}else if(AllOrGroup === 2){
					            		usersSearchReged();
					            	}
	        			    	}
	        			    },
	        			    error: function onError (error) {
	        			        console.error(error);
	        			    }
		            	});
	            	}else{
		            	$('#selectedOrg').html(node.text);
		            	$('#selectedOrg2').html(node.text);
		            	$.ajax({
	        			    url: '../getOrganizationUserCnt.do',
	        			    type: 'POST',
	        			    data:{
	        			    	org_id: node.id
	        			    },
	        			    success: function onData (data) {
	        			    	if(data[0]>0){
	        			    		$('.pop_deleteOrgConfirm').dialog("open");
	        			    		$('#deleteOrgYes').click(function() {
	            			    		$('.pop_deleteOrgConfirm').dialog("close");
	            		            	$(".pop_deleteOrg").dialog("open");
	        			    		});
	        			    	}else{
	        		            	$(".pop_deleteOrg").dialog("open");
	        			    	}
	        			    },
	        			    error: function onError (error) {
	        			        console.error(error);
	        			    }
		            	});
		                $('#deleteSelectedOrg').click(function() {
		                	tree.delete_node(node.id);
			            	$.ajax({
		        			    url: '../deleteOrg.do',
		        			    type: 'POST',
		        			    data:{
		        			    	org_id: node.id
		        			    },
		        			    success: function onData (data) {
		        			    	var data2 = getOrganizationList();
		        			    	treeReload(data2);
		        			    	$(".pop_deleteOrg").dialog("close");
					            	if(AllOrGroup === 0){
					            		getUserList();
					            	}else if(AllOrGroup === 1){
					            		groupUserSearch();
					            	}else if(AllOrGroup === 2){
					            		usersSearchReged();
					            	}
		        			    },
		        			    error: function onError (error) {
		        			        console.error(error);
		        			    }
			            	});
		                });
		                $('#deleteOrgCancel1').click(function() {
			            	$(".pop_deleteOrgConfirm").dialog("close");
		                });
		                $('#deleteOrgCancel').click(function() {
			            	$(".pop_deleteOrg").dialog("close");
		                });
	            	}
	            }
	        },
	    };

	    let ADMINArray = [];
	    let DCCArray = [];
		  $.ajax({
			    url: '../getADMINOrDCC.do',
			    type: 'POST',
			    async:false,
			    success: function onData (data) {
				    let ADMINString = '';
				    ADMINString = data[0];
			    	ADMINArray = ADMINString.split(',');
				    let DCCString = '';
				    DCCString = data[1];
			    	DCCArray = DCCString.split(',');
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		  
		    let get_path = $('#userRegGroup').jstree().get_path(node, '>');
		    let RootNodeNameOfCurrentNode = get_path.split('>')[0];
		    console.log(RootNodeNameOfCurrentNode);
		    if(ADMINArray.includes(session_user_id)){	//ADMIN은 모든 조직 컨트롤 가능
			    if (node.parent=='#') {
			    	items.deleteItem._disabled = true;	//ROOT 노드는 삭제불가
			    }else if(node.icon==='user_icon'){
			    	items.createRoot._disabled = true;
			    	items.createItem._disabled = true;
			    	items.renameItem._disabled = true;
			    	items.deleteItem._disabled = true;
			    }else{
			    	items.createRoot._disabled = true;
			    }
		    }/*else if(DCCArray.includes(session_user_id)){	//DCC는 고객사와 협력업체만 컨트롤 가능
		    	if(RootNodeNameOfCurrentNode==='고객사' || RootNodeNameOfCurrentNode==='협력업체'){
			    	if (node.parent=='#' ) {
				    	items.deleteItem._disabled = true;
				    }else if(node.icon==='user_icon'){
				    	items.createRoot._disabled = true;
				    	items.createItem._disabled = true;
				    	items.renameItem._disabled = true;
				    	items.deleteItem._disabled = true;
				    }else{
				    	items.createRoot._disabled = true;
				    }
		    	}
		    }*/
	    return items;
	}
	
	//$('#userRegGroup').jstree(true).settings.core.data = orgList;
	var userRegGrouptree = $('#userRegGroup').jstree({
	     "core" : {
	    	 'data' : orgList,
	         "check_callback" : true
	       },
	       "plugins" : [ "contextmenu" , "dnd"],
	       'contextmenu' : {
	     	  "items" : groupContextMenu
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
	
	$('#userRegGroup').bind("move_node.jstree", function(e, d) {//e:event, d:node
		if(d.node.icon==='user_icon'){	//사람을 옮긴경우
			if(d.parent==='#'){//루트노드'로' 옮긴경우
				alert('루트노드로 옮길 수 없습니다.');
		    	var data2 = getOrganizationList();
		    	treeReload(data2);
			}else{
				$.ajax({
				    url: '../jstreeDNDPerson.do',
				    type: 'POST',
				    data:{
				    	user_id: d.node.original.user_id,
				    	origin_org_id: d.old_parent,
				    	org_id: d.parent
				    },
				    success: function onData (data) {
			        	if(AllOrGroup === 0){
			        		getUserList();
			        	}else if(AllOrGroup === 1){
			        		groupUserSearch();
			        	}else if(AllOrGroup === 2){
			        		usersSearchReged();
			        	}
    			    	var data2 = getOrganizationList();
    			    	treeReload(data2);
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
			}
		}else{//부서를 옮긴경우
			if(d.old_parent==='#'){//루트노드'를' 옮긴경우
				d.old_parent = d.node.id;
			}
			if(d.parent==='#'){//루트노드'로' 옮긴경우
				d.parent = d.node.id;
			}
			$.ajax({
			    url: '../jstreeDND.do',
			    type: 'POST',
			    data:{
			    	org_id: d.node.id,
			    	old_org_parent_id: d.old_parent,
			    	org_parent_id: d.parent
			    },
			    success: function onData (data) {
		        	if(AllOrGroup === 0){
		        		getUserList();
		        	}else if(AllOrGroup === 1){
		        		groupUserSearch();
		        	}else if(AllOrGroup === 2){
		        		usersSearchReged();
		        	}
			    	var data2 = getOrganizationList();
			    	treeReload(data2);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
	});
	
	
	$('#userRegGroup').jstree(true).refresh();
}
function deleteRootCancel(){
	$(".pop_rootOrgDelete").dialog("close");
}
function getAddGroupTree(){
	addGroupList = getOrganizationList();
	
	var addGrouptree = $('#addPopGroup').jstree({
	     "core" : {
	    	 'data' : addGroupList,
	         "check_callback" : true
	       }
	}).on('click.jstree', function(event) {
		var node = $(event.target).closest("li");
		var org_id = node[0].id;
		$('#addOrgId').val(org_id);
		var node=$('#addPopGroup').jstree("get_selected", true);
		$("#addGroupLevelPath").html($('#addPopGroup').jstree().get_path(node[0], ' > '));
	});
	
	$('#addPopGroup').jstree(true).refresh();
}
function getChgGroupTree(){
	chgGroupList = getOrganizationList();
	
	var chgGrouptree = $('#chgPopGroup').jstree({
	     "core" : {
	    	 'data' : chgGroupList,
	         "check_callback" : true
	       }
	}).on('click.jstree', function(event) {
		var node = $(event.target).closest("li");
		var org_id = node[0].id;
		$('#chgOrgId').val(org_id);
		var node=$('#chgPopGroup').jstree("get_selected", true);
		$("#groupLevelPath").html($('#chgPopGroup').jstree().get_path(node[0], ' > '));
	});
	
	$('#chgPopGroup').jstree(true).refresh();
}
function getRegGroupTree(){
	regGroupList = getOrganizationList();
	
	var regGrouptree = $('#userRegPopGroup').jstree({
	     "core" : {
	    	 'data' : regGroupList,
	         "check_callback" : true
	       }
	}).on('click.jstree', function(event) {
		var node = $(event.target).closest("li");
		var org_id = node[0].id;
		$('#regOrgId').val(org_id);
		var node=$('#userRegPopGroup').jstree("get_selected", true);
		$("#regGroupLevelPath").html($('#userRegPopGroup').jstree().get_path(node[0], ' > '));
	}).bind('loaded.jstree', function(e, data) {
		//조직도 편집기에서는 현대중공업 그룹이 기본으로 선택되게끔 함
		$('#userRegPopGroup .jstree-themeicon-custom').parent().children(1).click();
	});

	
	$('#userRegPopGroup').jstree(true).refresh();

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
	 $.ajax({
			type: 'POST',
			url: '../getOrganizationUserList.do',
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
			    		if(data[0][i].passwd_fail_acc_stop_cnt===0){
			    			passwd_fail_status = 0;
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
function deleteSelectUser(){
	if($("input:checkbox[name='userList']:checked").length===0){
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}else{
		var checkedIndex = '';
		$("input:checkbox[name='userList']:checked").each(function(){
			checkedIndex += $(this).attr('id').replaceAll('userList','') + ',';
		});
		checkedIndex = checkedIndex.slice(0, -1);
		var checkedIndexArray = checkedIndex.split(',');
		var checkedUserString = '';
		for(i=0;i<checkedIndexArray.length;i++){
			checkedUserString += userRegisterListTable.getRow((parseInt(checkedIndexArray[i])+1)).getData().userId +'&&' + userRegisterListTable.getRow((parseInt(checkedIndexArray[i])+1)).getData().orgId + '@@';
		}
		checkedUserString = checkedUserString.slice(0, -2);
		$(".pop_deleteUser").dialog("open");
		
        $('#deleteSelectedUser').on("click",function() {
        	$.ajax({
			    url: '../deleteUsersVO.do',
			    type: 'POST',
			    data:{
			    	checkedUserString: checkedUserString
			    },
			    success: function onData (data) {
		        	$.ajax({
					    url: '../deleteOrganizationUser.do',
					    type: 'POST',
					    data:{
					    	checkedUserString: checkedUserString,
					    },
					    success: function onData (data) {
					    	alert('삭제되었습니다.');
					    },
					    error: function onError (error) {
					        console.error(error);
					    }
		        	});
	            	$(".pop_deleteUser").dialog("close");
	            	if(AllOrGroup === 0){
	            		getUserList();
	            	}else if(AllOrGroup === 1){
	            		groupUserSearch();
	            	}else if(AllOrGroup === 2){
	            		usersSearchReged();
	            	}
			    	var data2 = getOrganizationList();
			    	treeReload(data2);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
        	});
        });
        $('#deleteUserCancel').click(function() {
        	$(".pop_deleteUser").dialog("close");
        });
	}
}

function addGroupSave(){
	var checkedIndex = '';
	$("input:checkbox[name='userList']:checked").each(function(){
		checkedIndex += $(this).attr('id').replaceAll('userList','') + ',';
	});
	checkedIndex = checkedIndex.slice(0, -1);
	var checkedIndexArray = checkedIndex.split(',');
	var checkedUserString = '';
	for(i=0;i<checkedIndexArray.length;i++){
		checkedUserString += userRegisterListTable.getRow((parseInt(checkedIndexArray[i])+1)).getData().userId +'&&' + $('#addOrgId').val() + '@@';
	}
	checkedUserString = checkedUserString.slice(0, -2);
	
	if($('#addOrgId').val()===''){
		//그룹선택안했을 경우
		alert('부서를 선택해주세요.');
	}else{
		//그룹선택했을 경우
		$.ajax({
		    url: '../AddGroupUser.do',
		    type: 'POST',
		    data:{
		    	checkedUserString: checkedUserString,
		    },
		    success: function onData (data) {
		    	alert('부서를 추가했습니다.');
	        	$(".pop_addGroup").dialog("close");
	        	if(AllOrGroup === 0){
	        		getUserList();
	        	}else if(AllOrGroup === 1){
	        		groupUserSearch();
	        	}else if(AllOrGroup === 2){
	        		usersSearchReged();
	        	}
		    	var data2 = getOrganizationList();
		    	treeReload(data2);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function addGroupCancel(){
	$('.pop_addGroup').dialog("close");
}
function chgGroupSave(checkedUserString){
	$.ajax({
	    url: '../ChgGroupUser.do',
	    type: 'POST',
	    data:{
	    	checkedUserString: checkedUserString,
	    },
	    success: function onData (data) {
	    	alert('부서를 변경했습니다.');
        	$(".pop_chgGroup").dialog("close");
        	if(AllOrGroup === 0){
        		getUserList();
        	}else if(AllOrGroup === 1){
        		groupUserSearch();
        	}else if(AllOrGroup === 2){
        		usersSearchReged();
        	}
	    	var data2 = getOrganizationList();
	    	treeReload(data2);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function chgGroupCancel(){
	$('.pop_chgGroup').dialog("close");
}
function InitializeSelectedUser(){
	var user_id = userRegisterListTable.getSelectedData()[0].userId;
	$.ajax({
	    url: '../loginInitialization.do',
	    type: 'POST',
	    data:{
	    	user_id: user_id,
	    },
	    success: function onData (data) {
	    	alert('비밀번호 입력오류 횟수 초기화되었습니다.');
	    	$('.pop_loginInitialize').dialog("close");
	    	getUserList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function InitializeUserCancel(){
	$('.pop_loginInitialize').dialog("close");
}
function idCheck(){
	var user_id = $('#reg_user_id').val().toUpperCase();
	$.ajax({
	    url: '../isExistUser.do',
	    type: 'POST',
	    data:{
	    	user_id: user_id,
	    },
	    success: function onData (data) {
	    	if(data[0]===1){
		    	$('#isExistUser').val('true');//이미 존재하는 아이디
		    	$('#idCheckWarning').html('이미 가입된 아이디입니다.');
		    	$('#idCheckWarning').addClass('red');
		    	$('#idCheckWarning').removeClass('color_green');
	    	}else if(user_id===''){
		    	$('#isExistUser').val('true');//이미 존재하는 아이디
		    	$('#idCheckWarning').html('아이디를 입력해주세요.');
		    	$('#idCheckWarning').addClass('red');
		    	$('#idCheckWarning').removeClass('color_green');
	    	}else{
		    	$('#isExistUser').val('false');//존재하지 않는 아이디
		    	$('#idCheckWarning').html('사용가능한 아이디입니다.');
		    	$('#idCheckWarning').removeClass('red');
		    	$('#idCheckWarning').addClass('color_green');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function pwdCheck(){
	var count = 0;
	var passwd = $('#reg_passwd').val();
	const regUpper = /[A-Z]/;
	const regLower = /[a-z]/;
	const regNum = /[\d]/;
	const regSpc = /[`~!@#$%^*()\-_=]/;
	
	if(regUpper.test(passwd)) count++;
	if(regLower.test(passwd)) count++;
	if(regNum.test(passwd)) count++;
	if(regSpc.test(passwd)) count++;
	
	if(count>=3 && (passwd.length>=10 && passwd.length<=20)){
		$('#pwdCheckWarning').html('적합');
    	$('#pwdCheckWarning').removeClass('red');
    	$('#pwdCheckWarning').addClass('color_green');
    	$('#pass_reg').val('Y');
	}else{
		$('#pwdCheckWarning').html('부적합');
    	$('#pwdCheckWarning').addClass('red');
    	$('#pwdCheckWarning').removeClass('color_green');
    	$('#pass_reg').val('N');
	}
}
function pwdConfirmCheck(){
	var passwd = $('#reg_passwd').val();
	var passwd_confirm =$('#reg_passwd_confirm').val();
	if(passwd!=passwd_confirm){
		$('#pwdConfirmWarning').html('비밀번호 불일치');
		$('#pwdConfirmWarning').addClass('red');
		$('#pwdConfirmWarning').removeClass('color_green');
	}else{
		$('#pwdConfirmWarning').html('비밀번호 일치');
		$('#pwdConfirmWarning').addClass('color_green');
		$('#pwdConfirmWarning').removeClass('red');
	}
}
function userRegSave(){
	var isExistUser = $('#isExistUser').val();
	var user_id = $('#reg_user_id').val().toUpperCase();
	var user_kor_nm = $('#reg_user_kor_nm').val();
	var user_eng_nm = $('#reg_user_eng_nm').val();
	var passwd = $('#reg_user_id').val();
	var user_type = $('#reg_user_type').val();
	var hld_offi_gbn = $('#reg_hld_offi_gbn').val();
	var job_tit_cd = $('#reg_job_tit_cd').val();
	var offi_res_cd = $('#reg_offi_res_cd').val();
	var email_addr = $('#reg_email_addr_1').val()+'@'+$('#reg_email_addr_2').val();
	var offi_tel = $('#reg_offi_tel').val();
	var company_nm = $('#reg_company_nm').val();
	var org_id = $('#regOrgId').val();

	if(company_nm===''){
		company_nm = '현대중공업파워시스템';
	}
	
	if(isExistUser==='true'){
		alert('이미 존재하는 아이디로는 유저 등록을 할 수 없습니다.');
	}else if(user_id===''){
		alert('아이디를 입력해주세요.');
	}else if(user_kor_nm===''){
		alert('이름을 입력해주세요.');
	}else if(user_eng_nm===''){
		alert('영어이름을 입력해주세요.');
	}else if(user_type==='' || user_type===null){
		alert('사용자구분을 선택해주세요.');
	}else if(hld_offi_gbn===''){
		alert('재직구분을 선택해주세요.');
	}else if(job_tit_cd===''){
		alert('직급을 선택해주세요.');
	}else if($('#reg_email_addr_1').val()=='' || $('#reg_email_addr_2').val()==''){
		alert('이메일을 입력해주세요.');
	}else{
		$.ajax({
		    url: '../userRegister.do',
		    type: 'POST',
		    data:{
		    	user_id: user_id,
		    	user_kor_nm:user_kor_nm,
		    	user_eng_nm:user_eng_nm,
		    	passwd:passwd,
		    	user_type:user_type,
		    	hld_offi_gbn:hld_offi_gbn,
		    	job_tit_cd:job_tit_cd,
		    	offi_res_cd:offi_res_cd,
		    	email_addr:email_addr,
		    	offi_tel:offi_tel,
		    	company_nm:company_nm
		    },
		    success: function onData (data) {
				if(org_id!=''){
					$.ajax({
					    url: '../insertOrganizationUser.do',
					    type: 'POST',
					    data:{
					    	user_id: user_id,
					    	org_id:org_id
					    },
					    success: function onData (data) {
					    },
					    error: function onError (error) {
					        console.error(error);
					        alert('fail!');
					    }
					});
				}
		    	alert('유저 등록에 성공했습니다.');
		    	$('.pop_userRegstration').dialog("close");
	        	if(AllOrGroup === 0){
	        		getUserList();
	        	}else if(AllOrGroup === 1){
	        		groupUserSearch();
	        	}else if(AllOrGroup === 2){
	        		usersSearchReged();
	        	}
		    	var data2 = getOrganizationList();
		    	treeReload(data2);
		    },
		    error: function onError (error) {
		        console.error(error);
		        alert('fail!');
		    }
		});
	}
}
function userUpdateSave(){
	var user_id = $('#update_user_id').val();
	var user_kor_nm = $('#update_user_kor_nm').val();
	var user_eng_nm = $('#update_user_eng_nm').val();
	var passwd = $('#update_passwd').val();
	var passwd_confirm =$('#update_passwd_confirm').val();
	var user_type = $('#update_user_type').val();
	var hld_offi_gbn = $('#update_hld_offi_gbn').val();
	var job_tit_cd = $('#update_job_tit_cd').val();
	var offi_res_cd = $('#update_offi_res_cd').val();
	var email_addr = $('#update_email_addr_1').val()+'@'+$('#update_email_addr_2').val();
	var offi_tel = $('#update_offi_tel').val();
	var company_nm = $('#update_company_nm').val();
	var org_id = $('#updateOrgId').val();

	if(company_nm===''){
		company_nm = '현대중공업파워시스템';
	}
	
	if(user_kor_nm===''){
		alert('이름을 입력해주세요.');
	}else if(user_eng_nm===''){
		alert('영어이름을 입력해주세요.');
	}else if($('#pass_reg').val()==='N'){
		alert('비밀번호 규칙에 적합하지 않습니다.');
	}else if(passwd!=passwd_confirm){
		alert('입력한 비밀번호가 다릅니다.');
	}else if(user_type==='' || user_type===null){
		alert('사용자구분을 선택해주세요.');
	}else if(hld_offi_gbn===''){
		alert('재직구분을 선택해주세요.');
	}else if(job_tit_cd===''){
		alert('직급을 선택해주세요.');
	}else if($('#update_email_addr_1').val()=='' || $('#update_email_addr_2').val()==''){
		alert('이메일을 입력해주세요.');
	}else{
		$.ajax({
		    url: '../userInfoUpdate.do',
		    type: 'POST',
		    data:{
		    	user_id: user_id,
		    	user_kor_nm:user_kor_nm,
		    	user_eng_nm:user_eng_nm,
		    	passwd:passwd,
		    	user_type:user_type,
		    	hld_offi_gbn:hld_offi_gbn,
		    	job_tit_cd:job_tit_cd,
		    	offi_res_cd:offi_res_cd,
		    	email_addr:email_addr,
		    	offi_tel:offi_tel,
		    	company_nm:company_nm
		    },
		    success: function onData (data) {
		    	alert('유저정보를 수정했습니다.');
		    	$('.pop_userInfoUpdate').dialog("close");
	        	if(AllOrGroup === 0){
	        		getUserList();
	        	}else if(AllOrGroup === 1){
	        		groupUserSearch();
	        	}else if(AllOrGroup === 2){
	        		usersSearchReged();
	        	}
		    	var data2 = getOrganizationList();
		    	treeReload(data2);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function userUpdateOpen(rowposition){
	let user_id = userRegisterListTable.getRows()[rowposition].getData().userId;
	$.ajax({
	    url: '../getUsersVO.do',
	    type: 'POST',
	    data:{
	    	user_id: user_id
	    },
	    success: function onData (data) {
	    	$('#update_user_id').val(data[0].user_id);
	    	$('#update_user_kor_nm').val(data[0].user_kor_nm);
	    	$('#update_user_eng_nm').val(data[0].user_eng_nm);
	    	$('#update_user_type').val(data[0].user_type);
	    	$('#update_hld_offi_gbn').val(data[0].hld_offi_gbn);
	    	$('#update_job_tit_cd').val(data[0].job_tit_cd);
	    	$('#update_offi_res_cd').val(data[0].offi_res_cd);
	    	let email_addr_array = data[0].email_addr.split('@');
	    	$('#update_email_addr_1').val(email_addr_array[0])+'@'+$('#update_email_addr_2').val(email_addr_array[1]);
	    	$('#update_offi_tel').val(data[0].offi_tel);
	    	$('#update_company_nm').val(data[0].company_nm);

	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	$('.pop_userInfoUpdate').dialog("open");
}
function passwdResetOpen(rowposition){
	let user_id = userRegisterListTable.getRows()[rowposition].getData().userId;
	$('.pop_passwdReset').dialog("open");
	$('#passwdResetConfirm').attr('onclick','passwdResetConfirm(`'+user_id+'`)');
}
function passwdResetConfirm(user_id){
	$('.pop_passwdResetIdIs').dialog('open');
	$.ajax({
	    url: '../passwdResetAdmin.do',
	    type: 'POST',
	    data:{
	    	user_id: user_id
	    },
	    success: function onData (data) {
	    	if(data[0]>0 && data[1]>0){
		    	alert('비밀번호가 아이디로 변경되었습니다.로그인하여 반드시 비밀번호를 변경해주세요.');
		    	$('.pop_passwdReset').dialog("close");
	    	}else{
	    		alert('Password Reset Error!');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function userRegCancel(){
	$('.pop_userRegstration').dialog("close");
}
function userUpdateCancel(){
	$('.pop_userInfoUpdate').dialog("close");
}
function passwdResetCancel(){
	$('.pop_passwdReset').dialog("close");
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
