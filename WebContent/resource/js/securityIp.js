var set_ip_range = $("#ip_type option:selected").val();
var securityIpTable;

var security_Ip = {};


$(function(){
	getIpUseYN();
	security_Ip.select_point = {};
	
	$(".pop_securityIpDeleteConfirm").dialog({
		autoOpen: false,
		maxWidth: 1000,
		width: "auto"
	}).bind("dialogclose", function(event, ui) {
		securityIpTable = null;
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
	getSecurityIpList();
	
	$("#ipOrg_pop").dialog({
		autoOpen: false,
        maxWidth: 1000,
        maxHeight:1100,
        resizeable:false,
        width: "auto"
	});
	
	// 찾아보기 다이얼로그 내부 트리,그리드 init
	setTree2();
	setMemberGrid();
	
	
	// 탭 버튼 누를 때 아무것도 처리 안함
	$("#ip_orgTab").click(function(){
			return;
	});
	
	
	
	
});

$("#ip_type").change(function(){
	set_ip_range = $("#ip_type option:selected").val();
	$("#ipRange").val(set_ip_range);
	
	if(set_ip_range == "SOLO") {
		$(".btwIpChk").css("display","none");
		$(".oneIpChk").css("display","revert");
	}else if(set_ip_range == "BETWEEN") {
		$(".btwIpChk").css("display","revert");
		$(".oneIpChk").css("display","none");
	}
});
function getSecurityIpList() {
	$.ajax({
		url: 'getSecurityIpList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	console.log(data);
	    	var ipList = [];
	    	var changeIpLange;
	    	for(i=0;i<data[0].length;i++){
	    		if(data[0][i].ip_range == "SOLO") {
	    			changeIpLange = "단독 IP";
	    		}else {
	    			changeIpLange = "대역폭 IP";
	    		}
	    		
	    		ipList.push({
	    			seq: data[0][i].seq,
	    			get_org_ip_range: data[0][i].ip_range,
	    			ip_range: changeIpLange,
	    			ip_addr: data[0][i].ip_addr,
	    			ip_lvl:data[0][i].ip_lvl,
	    			ip_val:data[0][i].ip_val,
	    			label:data[0][i].label,
	    			reg_id: data[0][i].reg_id,
	    			reg_date: getDateFormat(data[0][i].reg_date),
	    		});
	    	}
	    	setSecurityIpList(ipList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function setSecurityIpList(ipList) {
	securityIpTable = new Tabulator("#ipList", {
        selectable:1,
        data: ipList,
        layout: "fitColumns",
        placeholder:"No Data Set",
        height: 230,
        columns: [{
	            title: "SEQ",
	            field: "seq",
	            visible:false
        	},
        	{
	            title: "get_org_ip_range",
	            field: "get_org_ip_range",
	            visible:false
        	},
        	{
	            title: "ip_lvl",
	            field: "ip_lvl",
	            visible:false,
	            width: 100
        	},
        	{
	            title: "ip_val",
	            field: "ip_val",
	            visible:false,
	            width: 150
        	},
        	{
	            title: "기본인증 IP",
	            field: "label",
	            visible:true,
	            width: 150
        	},
        	{
                title: "생성타입",
                field: "ip_range",
                width: 150,
                visible:false
            },
        	{
                title: "IP 주소",
                field: "ip_addr",
                hozAlign: "left"
            },
            {
                title: "등록자",
                field: "reg_id",
                width: 160,
                hozAlign: "left"
            },
            {
                title: "등록일",
                field: "reg_date",
                width: 170
            }
        ],
    });
	securityIpTable.on("rowSelected",function(row){
		var clickTb = row.getData();
		var ipAddr = clickTb.ip_addr;
		var chkSEaddr = ipAddr.split('~');
		
		$("#ipSeq").val(clickTb.seq);
		$("#ipRange").val(clickTb.get_org_ip_range);
		console.log("chkSEaddr = "+chkSEaddr);
		
		if(chkSEaddr.length == 1) {
			var splitAddr = chkSEaddr[0].split('.');
			// $("#one_ip1").val(splitAddr[0]);
			// $("#one_ip2").val(splitAddr[1]);
			// $("#one_ip3").val(splitAddr[2]);
			// $("#one_ip4").val(splitAddr[3]);
			$("#start_ip1").val(splitAddr[0]);
			$("#start_ip2").val(splitAddr[1]);
			$("#start_ip3").val(splitAddr[2]);
			$("#start_ip4").val(splitAddr[3]);
			
			$("#finish_ip1").val("");
			$("#finish_ip2").val("");
			$("#finish_ip3").val("");
			$("#finish_ip4").val("");
			
		}else {
			var stratSplitAddr = chkSEaddr[0].split('.');
			console.log("stratSplitAddr = "+stratSplitAddr[0]);
			$("#start_ip1").val(stratSplitAddr[0]);
			$("#start_ip2").val(stratSplitAddr[1]);
			$("#start_ip3").val(stratSplitAddr[2]);
			$("#start_ip4").val(stratSplitAddr[3]);
			
			var exitSplitAddr = chkSEaddr[1].split('.');
			$("#finish_ip1").val(exitSplitAddr[0]);
			$("#finish_ip2").val(exitSplitAddr[1]);
			$("#finish_ip3").val(exitSplitAddr[2]);
			$("#finish_ip4").val(exitSplitAddr[3]);
		}
		
		$("#ip_type").val(clickTb.get_org_ip_range).prop("selected", true);
		
		/*
		if(clickTb.get_org_ip_range == "SOLO") {
			$(".btwIpChk").css("display","none");
			$(".oneIpChk").css("display","revert");
		}else if(clickTb.get_org_ip_range == "BETWEEN") {
			$(".btwIpChk").css("display","revert");
			$(".oneIpChk").css("display","none");
		}
		*/
		
		$("#ip_val").val(clickTb.label);
		security_Ip.select_point["div"] = "G";
		security_Ip.select_point["val"] = {
				ip_val : clickTb.ip_val,
				ip_lvl : clickTb.ip_lvl,
				label : clickTb.label
		};
		// 해당 시퀀스가 없으면 새로 추가한 item
		security_Ip.seq = clickTb.seq;
		
		$("#ipEdit").css("display","inline-flex");
		$("#ipCreation").css("display","none");
	});
	securityIpTable.on("rowDeselected",function(row){
		securityIpClear();
		security_Ip.select_point["div"] = "";
		security_Ip.select_point["val"] = {};
		security_Ip.seq = -1;
	});
	
}

function newIpCreation() {
	securityIpClear();
	security_Ip.select_point["div"] = "";
	security_Ip.select_point["val"] = {};
	security_Ip.seq = -1;
	securityIpTable.deselectRow();
}
function getIpUseYN(){
	$.ajax({
		url: 'getIpUseYN.do',
		type: 'POST',
		success: function onData (data) {
			if(data==='Y'){
				$("input[name='ipUse'][value='Y']").attr("checked", true);
			}else{
				$("input[name='ipUse'][value='N']").attr("checked", true);
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function ipUseYNSave(){
	let ipUseYN = $("input[name='ipUse']:checked").val();
	$.ajax({
		url: 'ipUseYNSave.do',
		type: 'POST',
		data:{
			white_ip_use_yn: ipUseYN
		},
		success: function onData (data) {
			if(data>0){
				alert('적용에 성공했습니다.');
			}else{
				alert('적용에 실패했습니다.');
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function ipCreationSave() {
	/*
	if(set_ip_range == "SOLO") {
		// 단독 아이피 저장
		var set_ip_addr = $("#one_ip1").val()+"."+$("#one_ip2").val()+"."
							+$("#one_ip3").val()+"."+$("#one_ip4").val()
		
		$.ajax({
			url: 'insertIp.do',
			type: 'POST',
			data:{
				ip_range: set_ip_range,
				ip_addr: set_ip_addr
			},
			success: function onData (data) {
				if(data < 0) {
		    		alert("등록 오류");
		    	}else {
					alert('저장이 완료되었습니다.');
					getSecurityIpList();
					securityIpClear();
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		        alert("등록 오류");
		    }
		});
	}
	*/
	//else if(set_ip_range == "BETWEEN") {
		// 대역 아이피 저장
	
	
	
	// 저장할 value값 있는지 체크
	
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "IP보안",
	    	method: "저장"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
		var set_ip_addr = $("#start_ip1").val()+"."+$("#start_ip2").val()+"."
				+$("#start_ip3").val()+"."+$("#start_ip4").val();
		// 끝 IP 필드를 다 채운 경우, 범위로 넘겨준다.
		if($("#finish_ip1").val() != "" &&
			$("#finish_ip2").val() != "" &&
			$("#finish_ip3").val() != "" &&
			$("#finish_ip4").val() != ""
			){
			set_ip_addr += "~"
				+$("#finish_ip1").val()+"."+$("#finish_ip2").val()+"."
				+$("#finish_ip3").val()+"."+$("#finish_ip4").val()
		}
				
		
		
		if(!security_Ip.select_point["val"].ip_val){
			alert("IP정책을 적용할 대상을 입력하지 않았습니다.");
			return;
		}
			
//		if(
//			$("#start_ip1").val() == "" ||
//			$("#start_ip2").val() == "" ||
//			$("#start_ip3").val() == "" ||
//			$("#start_ip4").val() == "" ||
//			$("#finish_ip1").val() == "" ||
//			$("#finish_ip2").val() == "" ||
//			$("#finish_ip3").val() == "" ||
//			$("#finish_ip4").val() == ""
//		 ){
			
		if(
			$("#start_ip1").val() == "" ||
			$("#start_ip2").val() == "" ||
			$("#start_ip3").val() == "" ||
			$("#start_ip4").val() == "" 
			){
			alert("필수 입력 필드에 IP를 입력하지 않으면 저장할 수 없습니다.");
			return;
		}
		
		
				
		$.ajax({
			url: 'insertIp.do',
			type: 'POST',
			data:{
				ip_lvl : security_Ip.select_point["val"].ip_lvl,
				ip_val : security_Ip.select_point["val"].ip_val,
				ip_range: "BETWEEN",
				ip_addr: set_ip_addr,
				seq:security_Ip.seq
			},
			success: function onData (data) {
				if(data < 0) {
					alert("등록 오류");
				}else {
					alert('저장이 완료되었습니다.');
					getSecurityIpList();
					securityIpClear();
				}
			},
			error: function onError (error) {
				console.error(error);
				alert("등록 오류");
			}
		});
	// }
}

function ipCreationEdit() {
	var set_ipSeq = $("#ipSeq").val();
	// var set_ipRange = $("#ipRange").val();
	//console.log("set_ipRange = "+set_ipRange);
	//console.log("seq = "+set_ipSeq);
	/*
	if(set_ipRange == "SOLO") {
		// 단독 아이피 저장
		var set_ip_addr = $("#one_ip1").val()+"."+$("#one_ip2").val()+"."
							+$("#one_ip3").val()+"."+$("#one_ip4").val();
		
		$.ajax({
			url: 'upSecurityIp.do',
			type: 'POST',
			data:{
				ip_range: set_ipRange,
				ip_addr: set_ip_addr,
				seq: set_ipSeq
			},
			success: function onData (data) {
				if(data < 0) {
					alert("수정 오류");
				}else {
					alert('수정이 완료되었습니다.');
					getSecurityIpList();
				}
			},
			error: function onError (error) {
				console.error(error);
			}
		});
		
	}*/
	//else if(set_ipRange == "BETWEEN") {
		// 대역 아이피 저장
	
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "IP보안",
	    	method: "수정"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	if(!security_Ip.select_point["val"].ip_val){
		alert("필수 입력 필드에 IP를 입력하지 않으면 저장할 수 없습니다.");
		return;
	}
		
	if(
		$("#start_ip1").val() == "" ||
		$("#start_ip2").val() == "" ||
		$("#start_ip3").val() == "" ||
		$("#start_ip4").val() == ""
	 ){
		alert("IP를 입력하지 않으면 저장할 수 없습니다.");
		return;
	}
	
	
	var set_ip_addr = $("#start_ip1").val()+"."+$("#start_ip2").val()+"."
	+$("#start_ip3").val()+"."+$("#start_ip4").val();
	// 끝 IP 필드를 다 채운 경우, ip값을 range로 넘겨준다.
	if($("#finish_ip1").val() != "" &&
	   $("#finish_ip2").val() != "" &&
	   $("#finish_ip3").val() != "" &&
	   $("#finish_ip4").val() != ""
	){
		set_ip_addr += "~"
			+$("#finish_ip1").val()+"."+$("#finish_ip2").val()+"."
			+$("#finish_ip3").val()+"."+$("#finish_ip4").val()
		}
		
		$.ajax({
			url: 'upSecurityIp.do',
			type: 'POST',
			data:{
				ip_lvl : security_Ip.select_point["val"].ip_lvl,
				ip_val : security_Ip.select_point["val"].ip_val,
				ip_range: "BETWEEN",
				ip_addr: set_ip_addr,
				seq: security_Ip.seq
			},
			success: function onData (data) {
				if(data < 0) {
					alert("수정 오류");
				}else {
					alert('수정이 완료되었습니다.');
					getSecurityIpList();
				}
			},
			error: function onError (error) {
				console.error(error);
			}
		});
	// }
}

function ipDelete() {
	$(".pop_securityIpDeleteConfirm").dialog("open");
}

function securityIpDeleteConfirm() {
	var set_ipSeq = $("#ipSeq").val();
	//console.log("seq = "+set_ipSeq);
	
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "IP보안",
	    	method: "삭제"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
		url: 'deleteSecurityIp.do',
		type: 'POST',
		data:{
			seq: set_ipSeq
		},
		success: function onData (data) {
			if(data < 0) {
				alert("삭제 오류");
			}else {
				alert('삭제가 완료되었습니다.');
				getSecurityIpList();
				securityIpClear();
			}
		},
		error: function onError (error) {
			console.error(error);
		}
	});
	
	$(".pop_securityIpDeleteConfirm").dialog("close");
}

function securityIpDeleteCancel() {
	$(".pop_securityIpDeleteConfirm").dialog("close");
}

// new, save, delete시 입력값 초기화
function securityIpClear() {
	$("#ipEdit").css("display","none");
	$("#ipCreation").css("display","inline-flex");
	
	/*
	$("#ip_type").val("BETWEEN").prop("selected", true);
	$(".btwIpChk").css("display","revert");
	$(".oneIpChk").css("display","none");
	*/
	
	$("#ipSeq").val("");
	$("#ipRange").val("");
	$("#ip_val").val("");
	
	$("#one_ip1").val("");
	$("#one_ip2").val("");
	$("#one_ip3").val("");
	$("#one_ip4").val("");
	
	$("#start_ip1").val("");
	$("#start_ip2").val("");
	$("#start_ip3").val("");
	$("#start_ip4").val("");
	
	$("#finish_ip1").val("");
	$("#finish_ip2").val("");
	$("#finish_ip3").val("");
	$("#finish_ip4").val("");
}

// 탭 닫을떄 실행
function removeTabSecurityIp() {
	$(".pop_securityIpDeleteConfirm").remove();
}


function ipValSearch() {
	$("#ipOrg_pop").dialog("open");
}



function setTree2(){
	
	 $.ajax({
			type: 'POST',
			url: 'auth_getTrees.do',
			async:false,
			data:{
				prj_id : getPrjInfo().id,
			},
			success: function(data) {
				
				
				let orgList = getOrganizationList(); 
				let member = orgList;
				//let member = getOrgList(orgList , data.model.orgTree);
				
				
				let hhiRootId = '0';

				// 최초의 Root Id는 현대중공업 id
				for(let i=0;i<member.length;i++){
					let tmp = member[i];
					if(tmp.parent == "#") {
						hhiRootId = tmp.id;
						security_Ip.hhiRootId = hhiRootId;
						break;
					}
				}
				
				var orgTree = $('#ipOrgnz').jstree({
					'plugins' : ['conditionalselect'],
				     "core" : {
				    	 'multiple' : false,
				    	 'data' : member,
				    	 "check_callback" : true,
				    	 'expand_selected_onload' : hhiRootId, 
				       }
				});

				
				orgTree.bind("ready.jstree",function(event){
					// 모든 폴더 닫기
					$("#ipOrgnz").jstree("close_all");
					// 현대중공업만 열기
					$("#ipOrgnz").jstree("open_node",hhiRootId);
				});
				
				orgTree.bind('select_node.jstree',function(event,data){
					
					console.log(data.node);
					
					
					 $.ajax({
							type: 'POST',
							url: 'getOrgMemberListInAuth.do',
							async:false,
							data:{
								org_id:data.node.id
							},
							success: function(data) {
								let list = data.model.list;
								// id값으로 중복제거
								list = list.filter((target, idx, arr)=>{
								    return arr.findIndex((item) => item.user_id === target.user_id) === idx
								});
								
								let settingData = [];
								
								for(let i=0;i<list.length;i++){
									let tmp = list[i];
									
									let item = {};
									item.ip_lvl = 'P';
									item.ip_val = tmp.user_id;
									item.label = tmp.user_kor_nm;
									//item.user_kor_nm = tmp.user_kor_nm;
									
									settingData.push(item);
								}
								
								security_Ip.grid.setData(settingData);
								
							
							},
						    error: function onError (error) {
						        console.error(error);
						    }
						});
					
					// 그리드 선택된 요소 있을 시, deselect
					if(security_Ip.grid.initialized){
						security_Ip.grid.deselectRow();
					}
					
					security_Ip.select_point["div"] = "T";
					security_Ip.select_point["val"] = {
							ip_val : data.node.id,
							ip_lvl : 'O',
							label : data.node.text
					};
					
					
				});
				
				// 선택해제했을 때
				orgTree.bind('deselect_node.jstree',function(event,data){
					security_Ip.select_point["div"] = "";
					security_Ip.select_point["val"] = {};
				});
				
				
				
				
			
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 
	 

	
	
}

function getOrganizationList() {
	var organizations = [];
	 $.ajax({
			type: 'POST',
			url: 'getOrganizationList.do',
			data:{
				userMngPage : 'ADMIN'
			},
			async: false,
			success: function(data) {
				var tmp_organization_List = data[0];
				
				for(var i=0 ; i < tmp_organization_List.length; ++i){
					var tmp_Node = tmp_organization_List[i];
					
					var	input_Organization = { 
							"id" : tmp_Node.org_id, 
							"parent" : tmp_Node.org_parent_id, 
							'text' : tmp_Node.org_nm, 
							'type' : tmp_Node.org_type ,
							'folder_path':tmp_Node.org_path,
							'state' : {'opened' : true,'selected' : false},
							'isFolder' : true
					};

					if(tmp_Node.org_id === tmp_Node.org_parent_id){ // 최상위 루트
						input_Organization.parent = "#";
						input_Organization.icon = "resource/images/pinwheel.png";
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

function setMemberGrid(){
	security_Ip.grid = new Tabulator("#ip_member_grid", {
		layout: "fitColumns",
		placeholder:"no data set",
		//headerVisible:false,
		selectable:1,
		index:"index",
		height: 200,
		columns: [			
			{ title:"No",field:"index", visible:false},
			{ title:"id",field:"ip_val", visible:true},
			{ title:"ip_lvl",field:"ip_lvl", visible:false},
			{
				title: "user_kor_nm",
				field: "label"
			}
		],
	});
	security_Ip.grid.on("tableBuilt",function(e,row){
		$.ajax({
			type: 'POST',
			url: 'getOrgMemberListInAuth.do',
			async:false,
			data:{
				org_id:security_Ip.hhiRootId
			},
			success: function(data) {
				let list = data.model.list;
				// id값으로 중복제거
				list = list.filter((target, idx, arr)=>{
				    return arr.findIndex((item) => item.user_id === target.user_id) === idx
				});
				
				let settingData = [];
				
				for(let i=0;i<list.length;i++){
					let tmp = list[i];
					
					let item = {};
					item.ip_lvl = 'P';
					item.ip_val = tmp.user_id;
					item.label = tmp.user_kor_nm;
					//item.user_kor_nm = tmp.user_kor_nm;
					
					settingData.push(item);
				}
				
				security_Ip.grid.setData(settingData);
				
			
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	security_Ip.grid.on("rowClick",function(e, row){
		if(row.getTable().getSelectedData().length == 0){
			security_Ip.select_point["div"] = "";
			security_Ip.select_point["val"] = {};
			
			return;
		}
		
		$('#ipOrgnz').jstree("deselect_all");
		
		let rowData = row.getData();
		
		security_Ip.select_point["div"] = "G";
		security_Ip.select_point["val"] = {
				ip_val : rowData.ip_val,
				ip_lvl : rowData.ip_lvl,
				label : rowData.label
		};
		
		
		
	});
	
	
}
$(document).keydown(function(event) {
	 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
	let focusEle = document.activeElement;
	if(document.getElementById('ipMemberKeyword') == focusEle){
		if(event.keyCode == 13 || event.which == 13) {
			ipMemberSearch();
		}
	}
});

function ipMemberSearch(){
	let check = $("input[name='ip_searchUser']:checked")[0].value;
	// 검색을 위한 키워드
	let keyword = $("#ipMemberKeyword").val();
	security_Ip.select_point.val = {};
	
	
	
	$.ajax({
		type: 'POST',
		url: 'searchMemberAtAuth.do',
		data:{
			auth_lvl1:check,
			auth_val:keyword
		},
		success: function(data) {
			let result = [];
			
			for(let i=0;i<data.model.search.length;i++){
				let tmp = {};
				tmp.ip_lvl = data.model.search[i].auth_lvl1;
				tmp.ip_val = data.model.search[i].auth_val;
				tmp.label = data.model.search[i].user_kor_nm;
				result.push(tmp);				
			}
			
			security_Ip.grid.setData(result);
		},
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	
}


function ipAddtoGrid(){
	let out = security_Ip.select_point;
	if(out.div == ''){
		alert("IP권한을 추가할 대상을 선택해주세요.");
		return;
	}

	// 조직도 창 닫기
	$("#ipOrg_pop").dialog("close");
	
	// 출력할 문구
	let txt = "";
	
	if(out["val"].ip_lvl == 'P'){ // 개인을 선택
		txt = out["val"].ip_val;
	}
	else{ // 조직을 선택
		txt = out["val"].label;
	}
	
	$("#ip_val").val(txt);
}

