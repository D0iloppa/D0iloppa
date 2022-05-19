var folderAuth_Var;

$(document).ready(function() {
	   
	$("#auth_pop").dialog({
        autoOpen: false,
        maxWidth: 1000,
        maxHeight:1100,
        resizeable:false,
        width: "auto"
    });
	
	$("#auth_pop").css("display","");
	
	
	$(".pop_authSet").dialog({
	        autoOpen: false,
	        maxWidth: 500,
	        resizeable:false,
	        width: 500
	    }).dialogExtend({
			"closable" : true,
			"maximizable" : true,
			"minimizable" : true,
			// "collapse" : function(evt) {
			// 	$(".ui-dialog-title").hide();
			// },
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
			"minimizeLocation" : "left",
		});
	
	
	 $(".pop_deleteFolderAuthConfirm").dialog({
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
	
	
	 setTree();
});

/**
 * 폴더 권한 설정페이지
 * @returns
 */
function folderAuth(folder_id){
	
	
	
	// 관련 변수 초기화
	initFolderAuth(folder_id);
	
	//
	getFolderAuthInfo(folder_id);
	$(".pop_authSet").dialog("open");
}

/**
 * 체크박스를 라디오처럼 하나만 선택 할 수 있도록 함
 * @param chk
 * @param name
 * @returns
 */
function radio(chk,name){
	 let obj = document.getElementsByName(name);
	 
	 // 체크해제 불가능 (라디오처럼 반드시 둘 중 하나만 선택되야 함)
	 /*
	 let cnt = 0;
	 for(let i=0; i<obj.length; i++)
		 if(!obj[i].checked) cnt++;
	 
	 if(cnt == 2){
		 chk.checked = true;
		 return;
	 }
	 */
	 
	 // 둘 중 한개의 값만 체크되어지도록 처리
	 for(let i=0; i<obj.length; i++){
		 if(obj[i] != chk) obj[i].checked = false;
	 }
}


function getFolderAuthInfo(folder_id){
	
	 $.ajax({
			type: 'POST',
			url: 'getFolderAuthInfo.do',
			data:{
				prj_id : getPrjInfo().id,
				folder_id : folder_id
			},
			success: function(data) {
				let result = data.model.folder_auth;
				folderAuth_Var.folderAuth = result;
				
				if(result.parent_folder_inheri_yn == 'Y')
					$("#parent_org")[0].checked = true;
				else
					$("#parent_org")[0].checked = false;
				
				if(result.folder_auth_add_yn == 'Y'){
					$("#addAuthCheck1")[0].checked = true;
					$("#auth_orgAdd").css("display","");
				}
					
				else{
					$("#addAuthCheck1")[0].checked = false;
					$("#auth_orgAdd").css("display","none");
				}
					
				
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 
}



/**
 * folderAuth 초기화
 * @returns
 */
function initFolderAuth(folder_id){

	//console.log("folder_id = "+folder_id);
	// 변수 초기화
	folderAuth_Var = {};
	// 트리에서 선택된 곳
	folderAuth_Var.select_point = {"div":"T","val":{}};
	
	// 폴더관련 변수
	folderAuth_Var.folder_id = folder_id;
	auth_setFolderPath(folder_id);
	
	// 기본 auth_lvl1
	folderAuth_Var.auth_lvl1 = "O";
	// 폴더트리 생성
	//setTree();
	// 그리드 초기화
	initGrid();
	
	
	// 최상위 폴더는 부모 폴더의 권한을 계승 항상 체크
	/*
	$("#parent_org").click(function(){
		let isRoot = (folderList.find(i=>i.id == folder_id)).parent == '#';
		console.log(isRoot);
		if(isRoot) $("#parent_org")[0].checked = true;
	});
	*/
	// 권한정보 영역 감추기
	
	// 탭 버튼 누를 때, auth_lvl1 값 매핑
	$("#auth_orgTab").click(function(){
		folderAuth_Var.auth_lvl1 = "O";
		// getAuthInfoList(folderAuth_Var.folder_id,"O");
		let sel = $('#authOrgnz').jstree("get_selected")[0];
		if(sel){
			 $.ajax({
					type: 'POST',
					url: 'getOrgMemberListInAuth.do',
					async:false,
					data:{
						org_id:sel
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
							item.auth_lvl1 = 'P';
							item.auth_val = tmp.user_id;
							item.user_kor_nm = tmp.user_kor_nm;
							
							settingData.push(item);
						}
						
						folderAuth_Var.memberGrid.setData(settingData);
						
					
					},
				    error: function onError (error) {
				        console.error(error);
				    }
				});
		}
			
	});
	
	$("#auth_grTab").click(function(){
		folderAuth_Var.auth_lvl1 = "G";
		// getAuthInfoList(folderAuth_Var.folder_id,"G");
		let sel = $('#authGr').jstree("get_selected")[0];
		if(sel){ // 선택된 노드가 있는 경우
			$.ajax({
				type: 'POST',
				url: 'getGrpMemberListInAuth.do',
				async:false,
				data:{
					prj_id:getPrjInfo().id,
					group_id : sel
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
						item.auth_lvl1 = 'P';
						item.auth_val = tmp.user_id;
						item.user_kor_nm = tmp.user_kor_nm;
						
						settingData.push(item);
					}
					
					console.log(settingData);
					
					folderAuth_Var.memberGrid.setData(settingData);
					
				
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
			folderAuth_Var.memberGrid;
		}
		else{ // // 선택된 노드가 없는 경우 프로젝트로 검색
			$.ajax({
				type: 'POST',
				url: 'getGrpMemberListInAuth.do',
				async:false,
				data:{
					prj_id:getPrjInfo().id,
					group_id : "prj"
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
						item.auth_lvl1 = 'P';
						item.auth_val = tmp.user_id;
						item.user_kor_nm = tmp.user_kor_nm;
						
						settingData.push(item);
					}
					
					console.log(settingData);
					
					folderAuth_Var.memberGrid.setData(settingData);
					
				
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
			folderAuth_Var.memberGrid;
		}
	});
	
	// 체크박스 제어함수 셋팅
	checkControlSet();
	
	// "이 폴더 권한을 추가" 체크박스의 체크값에 따른 트리 활성화 토글
	folderAuth_Var.chkIsOn = [false];
	
	$("#addAuthCheck1")[0].checked = false;
	
	$("#auth_org_grid").css("display","none");
	// $("#auth_orgDel").css("display","");
	$("#auth_orgDel").attr("disabled",true);
	
	$("#addAuthCheck1").click(function(){
		/*
		// 그리드가 init된 경우
		if(folderAuth_Var.grid.initialized){
			if( folderAuth_Var.grid.getData().length > 0){
				$("#addAuthCheck1")[0].checked = true;
				return;
			}
				
		}
		*/
		
		folderAuth_Var.chkIsOn[0] = $("#addAuthCheck1").is(':checked');
		// $('#authOrgnz').jstree("deselect_all");
		// toggleTreeMode(folderAuth_Var.chkIsOn[0]);
		// 체크 여부에 따른 화면 셋팅
		if(folderAuth_Var.chkIsOn[0]) {
			$("#auth_org_grid").css("display","");
			$("#auth_orgAdd").css("display","");
		}
		
		// 그렇지 않은 경우
		else {
			$("#auth_org_grid").css("display","none");
			$("#auth_orgAdd").css("display","none");
		}
	});
	
	clearCheck();
	disabledToggle(true);
	

}

/**
 * 
 * @param auth_str : 체크할 대상의 구분자 (조직도,그룹)
 * @returns
 */

function setDefault(auth_str){
	
	// 초기화 하기전 체크박스 모두 해제
	$("input:checkbox[name='read_"+auth_str+"']")[0].checked = false;
	$("input:checkbox[name='read_"+auth_str+"']")[1].checked = false;
	
	$("input:checkbox[name='write_"+auth_str+"']")[0].checked = false;
	$("input:checkbox[name='write_"+auth_str+"']")[1].checked = false;
	
	$("input:checkbox[name='delete_"+auth_str+"']")[0].checked = false;
	$("input:checkbox[name='delete_"+auth_str+"']")[1].checked = false;
	// 초기화 끝
	
	// 디폴트 값으로 체크
	// 부모 폴더의 권한을 계승 체크
	if(!$(("#parent_"+auth_str) )[0].checked) $( ("#parent_"+auth_str) )[0].click();
	$("input:checkbox[name='read_"+auth_str+"'][value='"+folderAuth_Var.defaultAuth.r +"']").prop("checked",true);
	$("input:checkbox[name='write_"+auth_str+"'][value='"+ folderAuth_Var.defaultAuth.w +"']").prop("checked",true);
	$("input:checkbox[name='delete_"+auth_str+"'][value='"+ folderAuth_Var.defaultAuth.d +"']").prop("checked",true);
}
/**
 * 이 폴더 권한을 추가 체크박스의 체크여부에 따른
 * 트리 활성화 비활성화 설정
 * @param isOn
 * @returns
 */
function toggleTreeMode(isOn){
	// 조직도(1), 그룹(2) 중 어느 탭 클릭했는지 확인
	
	let treeList = ["#authOrgnz" , "#authGr"];
	
	let focusTree = $(treeList[folderAuth_Var.auth_lvl1 - 1]);
	focusTree.jstree();
	if(isOn){ // 체크됨
		focusTree[0].classList.remove("disableTree");
	}else{ // 체크 안됨
		focusTree[0].classList.add("disableTree");
	}
}

/**
 * 폴더트리 생성
 * @returns
 */
function setTree(){
	
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
						break;
					}
				}
				
				var orgTree = $('#authOrgnz').jstree({
					/*
					'conditionalselect':function(node){
						return folderAuth_Var.chkIsOn[0];
					},
					*/
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
					$("#authOrgnz").jstree("close_all");
					// 현대중공업만 열기
					$("#authOrgnz").jstree("open_node",hhiRootId);
					// 현대중공업 선택
					// $("#authOrgnz").jstree("select_node",hhiRootId);
				});
				
				orgTree.bind('select_node.jstree',function(event,data){
					
					console.log(data.node);
					
					folderAuth_Var.select_point["div"] = "T";
					let selectData = {};
					selectData.auth_lvl1 = (data.node.icon == "user_icon") ? "P" :"O" //P,G
					selectData.auth_val = data.node.id + ""; // String으로 변환
					selectData.label = data.node.text;
					folderAuth_Var.select_point["val"] = selectData;
					//getAuthInfo(selectData);
					
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
									item.auth_lvl1 = 'P';
									item.auth_val = tmp.user_id;
									item.user_kor_nm = tmp.user_kor_nm;
									
									settingData.push(item);
								}
								
								folderAuth_Var.memberGrid.setData(settingData);
								
							
							},
						    error: function onError (error) {
						        console.error(error);
						    }
						});
					
					// 그리드 선택된 요소 있을 시, deselect
					if(folderAuth_Var.grid.initialized){
						folderAuth_Var.grid.deselectRow();
					}
					
					if(folderAuth_Var.memberGrid.initialized){
						folderAuth_Var.memberGrid.deselectRow();
					}
				});
				
				// 선택해제했을 때
				orgTree.bind('deselect_node.jstree',function(event,data){
					folderAuth_Var.select_point["div"] = "";
					folderAuth_Var.select_point["val"] = {};
				});
				
				let gr = getGroupList(data.model.group , data.model.grTree);
				
				var grTree = $('#authGr').jstree({
					/*
					'conditionalselect':function(node){
						return folderAuth_Var.chkIsOn[1];
					},
					*/
					'plugins' : ['conditionalselect'],
					"core" : {
						'multiple' : false,
						'data' : gr,
						"check_callback" : true
					}
				});
				
				
				
				grTree.bind('select_node.jstree',function(event,data){
					folderAuth_Var.select_point["div"] = "T";
					let selectData = {};
					selectData.auth_lvl1 =(data.node.icon == "user_icon") ? "P" :"G" //P,G
					selectData.auth_val = data.node.id + ""; // String으로 변환
					selectData.label = "[G] " + data.node.text;
					folderAuth_Var.select_point["val"] = selectData;
					//getAuthInfo(selectData);
					
					
					$.ajax({
						type: 'POST',
						url: 'getGrpMemberListInAuth.do',
						async:false,
						data:{
							prj_id:getPrjInfo().id,
							group_id : data.node.id
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
								item.auth_lvl1 = 'P';
								item.auth_val = tmp.user_id;
								item.user_kor_nm = tmp.user_kor_nm;
								
								settingData.push(item);
							}
							
							console.log(settingData);
							
							folderAuth_Var.memberGrid.setData(settingData);
							
						
						},
					    error: function onError (error) {
					        console.error(error);
					    }
					});
					
					// 선택해제했을 때
					grTree.bind('deselect_node.jstree',function(event,data){
						folderAuth_Var.select_point["div"] = "";
						folderAuth_Var.select_point["val"] = {};
					});
					
					// 그리드 선택된 요소 있을 시, deselect
					if(folderAuth_Var.grid.initialized){
						folderAuth_Var.grid.deselectRow();
					}
					
					if(folderAuth_Var.memberGrid.initialized){
						folderAuth_Var.memberGrid.deselectRow();
					}
					
				});
				
				
				
				
				
				
			
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 
	 

	
	
}

/**
 * 체크박스 컨트롤 설정
 * @returns
 */
function checkControlSet(){
	
	
	
	let org_checkAll = document.getElementsByName("all_org");
	// all 허용
	org_checkAll[0].onclick = function(){
		// console.log("all chk");
		
		radio(this,'all_org');
		
		
		
		this.checked
		let checkList = $(".org_y");
		for(let i=1;i<checkList.length;i++){
			checkList[i].checked = this.checked;
		}
			
		
		// 거부 체크박스 체크해제
		checkList = $(".org_n");
		for(let i=0;i<checkList.length;i++)
			checkList[i].checked = false;			
	};
	
	
	// all 거부
	org_checkAll[1].onclick = function(){
		radio(this,'all_org');
		let checkList = $(".org_n");
		for(let i=1;i<checkList.length;i++){
			checkList[i].checked = this.checked;
		}
		
		// 허용 체크박스의 체크해제
		checkList = $(".org_y");
		for(let i=0;i<checkList.length;i++)
			checkList[i].checked = false;
		
	};
	
	
	let checkList = $(".org_y");
	for(let i=1;i<checkList.length;i++){
		checkList[i].onclick = function(){
			// 기본적으로 radio 기능으로 동작
			radio(this,this.name);
			// 하위 요소가 클릭되었을 시, All 체크 여부와 무관해지므로 체크해제
			org_checkAll[0].checked = false;
			org_checkAll[1].checked = false;
			if(i==1){
				let org_r = document.getElementsByName("read_org");
				if(!org_r[0].checked){ // 읽기권한을 빼면 쓰기,삭제 권한도 삭제해줘야 한다.
					let org_w = document.getElementsByName("write_org");
					org_w[0].checked = "";
					let org_d = document.getElementsByName("delete_org");
					org_d[0].checked = "";
				}
			}
			// write 허용버튼 클릭 시 read는 반드시 허용이 되어야 함
			if(i==2){
				let org_w = document.getElementsByName("write_org");
				if(!org_w[0].checked){ // 쓰기권한을 빼면 삭제 권한도 삭제해줘야 한다.
					let org_d = document.getElementsByName("delete_org");
					org_d[0].checked = "";
				}
				let org_r = document.getElementsByName("read_org");
				org_r[0].checked = true;
				org_r[1].checked = "";
			}
			// del 허용 버튼 클릭 시 read,write는 반드시 허용이 되어야 함
			if(i==3){
				let org_r = document.getElementsByName("read_org");
				org_r[0].checked = true;
				org_r[1].checked = "";
				
				let org_w = document.getElementsByName("write_org");
				org_w[0].checked = true;
				org_w[1].checked = "";
			}
		}
	}
	
	checkList = $(".org_n");
	for(let i=1;i<checkList.length;i++){
		checkList[i].onclick = function(){
			// 기본적으로 radio 기능으로 동작
			radio(this,this.name);
			// 하위 요소가 클릭되었을 시, All 체크 여부와 무관해지므로 체크해제
			org_checkAll[0].checked = false;
			org_checkAll[1].checked = false;
			
			// write 권한은 거부에 체크하더라도 읽기권한이 승인이면 체크할 수 없다.
			if(i==1){
				let org_w = document.getElementsByName("write_org");
				if(org_w[0].checked){
					org_w[0].checked = false;
					org_w[1].checked = true;
					/*
					let tmp = document.getElementsByName(this.name);
					tmp[0].checked = true;
					tmp[1].checked = false;
					*/					
				}
				
				let org_d = document.getElementsByName("delete_org");
				if(org_d[0].checked){
					org_d[0].checked = false;
					org_d[1].checked = true;			
				}
					
			}
			
			// del 권한은 write 권한이 거부이면 허용할 수 없다.
			if(i==2){
				let org_d = document.getElementsByName("delete_org");
				if(org_d[0].checked){
					org_d[0].checked = false;
					org_d[1].checked = true;			
				}
					
			}
		}
	}
	
}

function getOrgList(orgList , data){
	let member = orgList;
	
	
	for(var i=0 ; i < data.length; i++){
		var tmp_Node = data[i];
		
		var	input = { 
				"id" : tmp_Node.user_id, 
				"parent" : tmp_Node.org_id, 
				'text' : tmp_Node.user_kor_nm + "(" + tmp_Node.user_id + ")", 
				'type' : tmp_Node.org_type ,
				'icon' : "user_icon",
				'isFolder' : false,
				'folder_path':tmp_Node.org_path 
			};
		//"resource/images/memberIcon.png"
		
		member.push(input);
	}
		
	return member;
	
}

function getGroupList(group_List , members ){
	let result = [];
	
	let rootNode ={
			"id" : "prj",
			"text" : getPrjInfo().nm,
			"parent":"#",
			"icon":"resource/images/pinwheel.png",
			"state":{'opened' : true,'selected' : false}		
	}
	
	result.push(rootNode);
	
	// 그룹 리스트 부모노드로 생성
	for(let i=0 ; i<group_List.length;i++){
		let tmp = group_List[i];
		console.log();
		let treeNode ={
				"id" : tmp.group_id,
				"parent":"prj",
				'text':tmp.group_name,
				"isFolder" : true,
				"state":{'opened' : true,'selected' : false}
		}
		
		result.push(treeNode);
	}
	/*
	for(let i=0 ; i<members.length;i++){
		let tmp = members[i];

		var	memberNode = { 
				"id" : tmp.user_id +"@"+ tmp.group_id,
				"parent" : tmp.group_id, 
				'text' : tmp.user_kor_nm + "(" + tmp.user_id + ")", 
				'icon' : "user_icon",
				'state' : {'opened' : true,'selected' : false},
				'isFolder' : false
		};
		
		result.push(memberNode);
	}
	*/
	
	
	return result;
	
}

/**
 * 그리드 초기화
 * @returns
 */
function initGrid(){
	
	let nameMap ={ "O":"org" , "G":"gr"};
	
	folderAuth_Var.grid = new Tabulator("#auth_grid", {
		layout: "fitColumns",
		placeholder:"no data set",
		headerVisible:false,
		selectable:1,
		index:"index",
		height: 100,
		columns: [			
			{ title:"No",field:"index", visible:false},
			{ title:"auth_lvl1",field:"auth_lvl1", visible:false},
			{ title:"auth_val",field:"auth_val", visible:false},
			{	
				headerSort:false,
				title: "label",
				field: "label",
			    widthGrow:2
			},
			{	
				headerSort:false,
				visible:false,
				title: "tmp_auth_status",
				field: "tmp_auth_status"
			}
		],
	});
	
	folderAuth_Var.memberGrid = new Tabulator("#member_grid", {
		layout: "fitColumns",
		placeholder:"There is no member",
		headerVisible:true,
		selectable:1,
		index:"index",
		height: 350,
		columns: [			
			{ title:"No",field:"index", visible:false},
			{ title:"auth_lvl1",field:"auth_lvl1", visible:false},
			{ title:"id",field:"auth_val", visible:true},
			{	
				title: "kor.name",
				field: "user_kor_nm",
			},
			{
				title:"dept",
				field:"org_nm",
				visible:false
			}
		],
	});
	
	folderAuth_Var.grid.on("tableBuilt", function(e, row){
		getAuthInfoList(folderAuth_Var.folder_id);
		
	});
	
	folderAuth_Var.grid.on("rowClick", function(e, row){
		if(row.getTable().getSelectedData().length == 0){
			$("#auth_orgDel")[0].disabled = true;
			return;
		}
		
		if(!$("#addAuthCheck1")[0].checked){
			folderAuth_Var.grid.deselectRow();
			return;
		}
		
		disabledToggle(false);	
		
		let rowData = row.getData();
		
		$("#auth_orgDel")[0].disabled = false;
		
		$('#authOrgnz').jstree("deselect_all");
		$('#authGr').jstree("deselect_all");
		getAuthInfo(row.getData());
		
		/*
		folderAuth_Var.select_point["div"] = "G";
		folderAuth_Var.select_point["val"].auth_lvl1 = rowData.auth_lvl1;
		folderAuth_Var.select_point["val"].auth_val = rowData.auth_val;
		*/
	});
	
	folderAuth_Var.memberGrid.on("rowClick", function(e, row){
		if(folderAuth_Var.memberGrid.getSelectedData().length == 0){
			$("#auth_orgDel").attr("disabled",true);
			folderAuth_Var.select_point["div"] = "";
			folderAuth_Var.select_point["val"] = {};
			clearCheck();
			disabledToggle(true);

			return;
		}
			
		let rowData = row.getData();
		
		$('#authOrgnz').jstree("deselect_all");
		$('#authGr').jstree("deselect_all");
		

		
		folderAuth_Var.select_point["div"] = "G";
		folderAuth_Var.select_point["val"].auth_lvl1 = rowData.auth_lvl1;
		folderAuth_Var.select_point["val"].auth_val = rowData.auth_val;
		folderAuth_Var.select_point["val"].label = rowData.user_kor_nm + " (" + rowData.auth_val + ")";
		
	});
	
	/*
	folderAuth_Var.grid.on("rowSelected", function(row){
	    //row - row component for the selected row
		$("#auth_orgDel").css("display","");
	});
	*/
	
	folderAuth_Var.grid.on("rowDeselected", function(){
		//$("#auth_orgDel").css("display","none");
		$("#auth_orgDel").attr("disabled",true);
		folderAuth_Var.select_point["div"] = "";
		folderAuth_Var.select_point["val"] = {};
		clearCheck();
		disabledToggle(true);
	});

	
};
	 
	

/**
 * 폴더 패스 매핑
 * @param folder_id
 * @returns
 */
function auth_setFolderPath(folder_id){

	
	 $.ajax({
			type: 'POST',
			url: 'auth_getFolderPath.do',
			data:{
				prj_id : getPrjInfo().id,
				folder_id : folder_id
			},
			success: function(data) {
				$("#auth_folder_path").val(data.model.folderPath);
			
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 
	 
}

/**
 * org 전체 리스트 리턴
 * @returns
 */
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

/**
 * 해당 폴더의 auth info를 가져옴
 * @param folder_id
 * @returns
 */
function getAuthInfoList(folder_id){
	$("#auth_orgDel").attr("disabled",true);
	console.log("chk");
	 $.ajax({
			type: 'POST',
			url: 'getAuthInfoList.do',
			data:{
				prj_id:getPrjInfo().id,
				folder_id:folder_id
			},
			success: function(data) {
				let result = data.model["auth_list"];
				for(let i=0;i<result.length;i++){
					let item = result[i];
					
					let label = "";
					switch(item.auth_lvl1){
						case 'O':
							label = item._auth_val
							break;
						case 'G':
							label = "[G] " + item._auth_val;
							break;
						case 'P':
							label = item._auth_val + "(" + item.auth_val + ")";
							break;
					}					
					result[i].label = label;
					result[i].tmp_auth_status = {
									r:item.read_auth_yn,
									w:item.write_auth_yn,
									d:item.delete_auth_yn
							};
					
				}
				
				
				folderAuth_Var.grid.setData(result);
				/*
				if(result.length>0) 
					$("#addAuthCheck1")[0].checked = true;
					*/
				
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
}

/**
 * 추가버튼
 * @returns
 */
function authAdd(){
	
	$("#auth_pop").dialog("open");
	
	
	/*
	 // 기존코드
	 
	let node = folderAuth_Var.select_point[folderAuth_Var.auth_lvl1];
	let grid = folderAuth_Var.grid[folderAuth_Var.auth_lvl1];
	let auth_val = (node.id+"").split("@")[0];
	
	// 그리드에 해당 아이템이 존재하는지 체크
	
	
	let idxChk = grid.getData().findIndex(i => i.auth_val == auth_val); 
	if(idxChk>-1){ // 이미 존재하므로 추가하지 않음
		alert("리스트에 이미 존재합니다.");
		return;
	}
	
	
	let item = {
		auth_lvl1 : folderAuth_Var.auth_lvl1,
		auth_lvl2 : (node.isFolder) ? "G" : "P",
		auth_val : auth_val,
		label:node.text
	};
	
	grid.addData(item);
	
	
	*/
	
}

/**
 * 최상위 폴더는 부모 폴더의 권한을 계승 항상 체크
 * @returns
 */
function auth_rootCheck(){
	let isRoot = (folderList.find(i=>i.id == folderAuth_Var.folder_id)).parent == '#';
	if(isRoot) $("#parent_org")[0].checked = true;
}

function authAddtoGrid(){
	
	let selectedPoint = folderAuth_Var.select_point.val;
	if(Object.keys(selectedPoint).length == 0){
		alert("There is no selected Item");
		return;
	}
	
	let gridChk = folderAuth_Var.grid.getData();
	// 중복체크
	let findIdx = gridChk.findIndex(e => e.auth_lvl1 == selectedPoint.auth_lvl1 && e.auth_val == selectedPoint.auth_val);
	
	if(findIdx>=0){
		alert("이미 권한이 존재");
		return;
	}
	if(selectedPoint.auth_val == "prj"){
		alert("프로젝트명은 추가 불가능");
		return;
	}
		
	let item = {
			auth_lvl1 : selectedPoint.auth_lvl1,
			auth_val : selectedPoint.auth_val,
			label : selectedPoint.label,
			tmp_auth_status : {r:null,w:null,g:null}
		};
	console.log(item);
	folderAuth_Var.grid.addData(item);
	$("#auth_pop").dialog("close");
	
	let gridLastIdx = folderAuth_Var.grid.getData().length;
	let newRow = folderAuth_Var.grid.getRowFromPosition(gridLastIdx-1);
	folderAuth_Var.grid.selectRow(newRow);
	
	$("#auth_orgDel")[0].disabled = false;
	
	disabledToggle(false);	
	
	$('#authOrgnz').jstree("deselect_all");
	$('#authGr').jstree("deselect_all");
	getAuthInfo(newRow.getData());
	
}

/**
 * 삭제 버튼
 * @returns
 */
function authDel(){

	
	let data = folderAuth_Var.grid.getSelectedData()[0];
	if(!data) return;
	
	console.log(data);
	$.ajax({
		type:'POST',
		url:'deleteAuthInfo.do',
		data:{
			prj_id : getPrjInfo().id,
			folder_id : folderAuth_Var.folder_id,
			auth_lvl1 : data.auth_lvl1 ,
			auth_val : data.auth_val ,
		},
		success:function(data){
			if(data.model.status == 'SUCCESS'){
				alert("delete complete.");
				getAuthInfoList(folderAuth_Var.folder_id);
				clearCheck();
				disabledToggle(true);
			}else{
				alert(data.model.error);
			}
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
}

/**
 * 체크박스 disabled 조절
 * @param flag : true || false
 * @returns
 */
function disabledToggle(flag){
	$("input:checkbox[name=all_org]").attr("disabled",flag);
	$("input:checkbox[name=read_org]").attr("disabled",flag);
	$("input:checkbox[name=write_org]").attr("disabled",flag);
	$("input:checkbox[name=delete_org]").attr("disabled",flag);
}



/*
 * 모든 상태값 저장 
 * @returns
 */
function authApply_allData(){
	
	let authData = folderAuth_Var.grid.getData();
	
	
	let parent = $("#parent_org").is(":checked") ? "Y":"N";
	let add = $("#addAuthCheck1").is(":checked") ? "Y":"N";
	if(add == "N" && authData.length > 0){
		// alert("해당 폴더에 추가된 권한이 삭제됩니다. 계속 진행하시겠습니까?");
		$(".pop_deleteFolderAuthConfirm").dialog("open");
		return;
	}
	
	
	
	let out = [];
	// 권한이 저장되지 않은 항목이 존재하는지 체크
	for(let i=0;i<authData.length;i++){
		let tmp = authData[i];
		let r = tmp.tmp_auth_status.r;
		let w = tmp.tmp_auth_status.w;
		let d = tmp.tmp_auth_status.d;
		if(!r && !w && !d){
			alert("권한을 적용하지 않은 항목이 존재 [" + tmp.label +"]");
			return;
		}
	}
	
	
	// 모든 항목들에 권한이 적용된 경우 저장 가능한 상태
	for(let i=0;i<authData.length;i++){
		let tmp = authData[i];
		let outObj = {};
		outObj.auth_lvl1 = tmp.auth_lvl1;
		outObj.auth_val = tmp.auth_val;
		outObj.folder_id = folderAuth_Var.folder_id;
		outObj.prj_id = prj_id;
		outObj.read_auth_yn = tmp.tmp_auth_status.r;
		outObj.write_auth_yn = tmp.tmp_auth_status.w;
		outObj.delete_auth_yn = tmp.tmp_auth_status.d;
		
		out.push(JSON.stringify(outObj));
	}
	
	$.ajax({
		type:'POST',
		url:'updateAuthInfo_allData.do',
		traditional : true,
		data:{
			prj_id : getPrjInfo().id,
			folder_id : folderAuth_Var.folder_id,
			jsonRowDatas : out,
			parent_folder_inheri_yn : parent,
			folder_auth_add_yn : add
		},
		success:function(data){	
			let result = data.model;
			let isPrjMember = result.isPrjMember;
			let processType = result.processType;
			
			if(isPrjMember == 'N'){
				if(processType != 'General')
					alert("Folder authentication has been updated except for write/delete permissions.<br>Users who are not members of a project cannot be granted write/delete permissions to folders with specified the process type.");
				else
					alert("Folder auth is updated");
			}
			
			else{
				alert("Folder auth is updated");
			}
				
			
			
			clearCheck();
			disabledToggle(true);
			getFolderAuthInfo(folderAuth_Var.folder_id);
			getAuthInfoList(folderAuth_Var.folder_id);
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	
	
}


/**
 * 적용 버튼
 * @returns
 */
function authApply(){
	
	// 1) 이 폴더에 권한을 추가 체크되어 있어야 함
	/*
	let addAuth = $("#addAuthCheck1")[0];
	if(!addAuth.checked) return;
	*/
	
	// alert창에 보여줄 메시지
	
	let msg;
	
	let node = folderAuth_Var.select_point[folderAuth_Var.auth_lvl1];
	let grid = folderAuth_Var.grid[folderAuth_Var.auth_lvl1];
	
	let nameMap ={ "1":"org" , "2":"gr"};
	
	// 체크값
	
	let r = $("input:checkbox[name=read_org]:checked").val();
	let w = $("input:checkbox[name=write_org]:checked").val();
	let d = $("input:checkbox[name=delete_org]:checked").val();
	
	/*
	let r = $("input:checkbox[name=read_org]")[0].checked;
	r = r ? "Y" : "N";
	let w = $("input:checkbox[name=write_org]")[0].checked;
	w = w ? "Y" : "N";
	let d = $("input:checkbox[name=delete_org]")[0].checked;
	d = d ? "Y" : "N";
	*/
	let add = $("#addAuthCheck1").is(":checked") ? "Y":"N";
	if(add == "N" && folderAuth_Var.grid.getData().length > 0){
		// alert("해당 폴더에 추가된 권한이 삭제됩니다. 계속 진행하시겠습니까?");
		$(".pop_deleteFolderAuthConfirm").dialog("open");
		return;
	}
	
	let selected = folderAuth_Var.grid.getSelectedData()[0];
	if(!selected){
		r = null;
		w = null;
		d = null;
	}
	// 체크 여부 확인	
	
	if(selected){
		let r_check = $("input:checkbox[name=read_org]").is(":checked");
		let w_check = $("input:checkbox[name=write_org]").is(":checked");
		let d_check = $("input:checkbox[name=delete_org]").is(":checked");
		
		if(!r_check && !w_check && !d_check){
			alert("There is unchecked permission. Can't apply");
			return;
		}
		
	}
	
	/*
	if(!r || !w || !d){
		alert("There is unchecked permission. Can't apply");
		return;
	}
	*/
	
	
	let parent = $("#parent_org").is(":checked") ? "Y":"N";
	
	// 해당 폴더가 최상위 폴더이면, 부모 폴더의 권한을 계승은 항상 체크되어야 함
	let isRoot = (folderList.find(i=>i.id == folderAuth_Var.folder_id)).parent == '#';
	if(isRoot) parent = "Y";
	
	//let selected = folderAuth_Var.select_point.val;
	
	
	
	$.ajax({
		type:'POST',
		url:'updateAuthInfo.do',
		data:{
			prj_id : getPrjInfo().id,
			folder_id : folderAuth_Var.folder_id,
			auth_lvl1 : selected ? selected.auth_lvl1 : null ,
			auth_val : selected ? selected.auth_val : null ,
			read_auth_yn : r,
			write_auth_yn : w,
			delete_auth_yn : d,
			parent_folder_inheri_yn : parent,
			folder_auth_add_yn : add
		},
		success:function(data){
			if(!selected)
				msg = "Folder auth is changed";
			else
				msg = "["+selected.label+"]: auth updated.";
			
			alert(msg);
			clearCheck();
			disabledToggle(true);
			getFolderAuthInfo(folderAuth_Var.folder_id);
			getAuthInfoList(folderAuth_Var.folder_id);
			treeDataRefresh();
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

function folderAuth_delYes(){
	$(".pop_deleteFolderAuthConfirm").dialog("close");
	
	let parent = $("#parent_org").is(":checked") ? "Y":"N";
	
	$.ajax({
		type:'POST',
		url:'updateAuthInfo.do',
		data:{
			prj_id : getPrjInfo().id,
			folder_id : folderAuth_Var.folder_id,
			auth_lvl1 : null ,
			auth_val : null ,
			parent_folder_inheri_yn : parent,
			folder_auth_add_yn : "N"
		},
		success:function(data){
			msg = "Folder auth is updated";
			
			alert(msg);
			clearCheck();
			disabledToggle(true);
			getFolderAuthInfo(folderAuth_Var.folder_id);
			getAuthInfoList(folderAuth_Var.folder_id);
			
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
}

function folderAuth_delCancle() {
	$(".pop_deleteFolderAuthConfirm").dialog("close");
}

function clearCheck(){
	//$("#parent_org")[0].checked = false;
	
	let ac = $("input:checkbox[name='all_org']:checked")[0];
		if(ac) ac.checked = false;
	let rc = $("input:checkbox[name='read_org']:checked")[0];
		if(rc) rc.checked = false;
	let wc = $("input:checkbox[name='write_org']:checked")[0];
		if(wc) wc.checked = false;
	let dc = $("input:checkbox[name='delete_org']:checked")[0];
		if(dc) dc.checked = false;
}

/**
 * auth정보를 가져온다.
 * @param auth_lvl1
 * @param rowData
 * @returns
 */
function getAuthInfo(rowData){
	
	// 체크값 해제
	clearCheck();
	
	let tmp = rowData.tmp_auth_status;
	
	let r = tmp.r;
	let w = tmp.w;
	let d = tmp.d;
	
	if(r) $("input:checkbox[name='read_org'][value='"+ r +"']")[0].checked = true
	if(w) $("input:checkbox[name='write_org'][value='"+ w +"']")[0].checked = true
	if(d) $("input:checkbox[name='delete_org'][value='"+ d +"']")[0].checked = true
	
	// 해당 row의 auth값 가져오기
//	$.ajax({
//		type: 'POST',
//		url: 'getAuthInfo.do',
//		data:{
//			prj_id : getPrjInfo().id,
//			folder_id : folderAuth_Var.folder_id,
//			auth_lvl1 : rowData.auth_lvl1,
//			auth_val  : rowData.auth_val
//		},
//		success: function(data) {
//			let getAuth = data.model["info"];
//			if(!getAuth) {
//				$("#auth_orgDel").attr("disabled",false);
//				return;
//			}
//			
//			//$("#auth_orgDel").css("display","");
//			$("#auth_orgDel").attr("disabled",false);
//			let r = getAuth.read_auth_yn;
//			let w = getAuth.write_auth_yn;
//			let d = getAuth.delete_auth_yn;
//			
//			/*
//			let p = getAuth.parent_folder_inheri_yn;
//			if(p == 'Y')
//				$("#parent_org")[0].checked = true;
//			else
//				$("#parent_org")[0].checked = false;
//			*/
//			/*
//			if(r == 'Y')
//			$("input:checkbox[name='read_org'][value='Y']")[0].checked = true
//			if(w == 'Y')
//			$("input:checkbox[name='write_org'][value='Y']")[0].checked = true
//			if(d == 'Y')
//			$("input:checkbox[name='delete_org'][value='Y']")[0].checked = true
//			*/
//			if(r) $("input:checkbox[name='read_org'][value='"+ r +"']")[0].checked = true
//			if(w) $("input:checkbox[name='write_org'][value='"+ w +"']")[0].checked = true
//			if(d) $("input:checkbox[name='delete_org'][value='"+ d +"']")[0].checked = true
//			
//			
//		},
//		error: function onError (error) {
//			console.error(error);
//		}
//	});
	
 }

function addAuth_chk(obj){
	let checked = obj.checked;
	
	if(!checked){
		folderAuth_Var.select_point["div"] = "";
		folderAuth_Var.select_point["val"] = {};
		
		clearCheck();
		folderAuth_Var.grid.deselectRow();
		$(".org_y").attr("disabled",true);
		$(".org_n").attr("disabled",true);
		
	}
}

/**
 * 멤버 검색
 * @returns
 */
function authMemberSearch(){
	// 체크여부
	let check = $("input[name='searchUser']:checked")[0].value;
	// 검색을 위한 키워드
	let keyword = $("#authMemberKeyword").val();
	// 검색 시, 트리의 노드 선택여부 해제해주기 위함 
	let focusTabId = $(".as_tab_tit .on")[0].id
	
	if(focusTabId == "auth_orgTab"){// 조직도 포커스
		$('#authOrgnz').jstree("deselect_all");
	}else{ // 그룹 포커스
		$('#authGr').jstree("deselect_all");		
	}
	folderAuth_Var.select_point.val = {};
	 $.ajax({
			type: 'POST',
			url: 'searchMemberAtAuth.do',
			data:{
				auth_lvl1:check,
				auth_val:keyword
			},
			success: function(data) {
				let result = data.model.search;
				folderAuth_Var.memberGrid.setData(result);
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
}

/**
 * 권한관리 창이 떠있는 상태에서 폴더를 변경한 경우에 작동하는 메서드
 * @param folder_id
 * @returns
 */
function auth_folderChg(folder_id){
	initFolderAuth(folder_id);
	getFolderAuthInfo(folder_id);
}



function setGridTmpAuth(obj){
	let selectedRow = folderAuth_Var.grid.getSelectedRows()[0];
	
	let r = $("input[name='read_org']:checked").val() ? $("input[name='read_org']:checked").val() : null;
	let w = $("input[name='write_org']:checked").val() ? $("input[name='write_org']:checked").val() : null;
	let d = $("input[name='delete_org']:checked").val() ? $("input[name='delete_org']:checked").val() : null;
	
	//console.log(r,w,d);
	selectedRow.update({"tmp_auth_status" : {"r":r,"w":w,"d":d}});
	
}

