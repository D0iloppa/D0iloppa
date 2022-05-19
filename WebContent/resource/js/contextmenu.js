/*공통 js*/
var prj_id='0000000001';
var noPrj = false;
var checkInFlag = false;
/* 
 * 작성자		: 소진희
 * 작성일		: 2021-12-17
 * 변수명		: current_folder_id,current_folder_id
 * 설명		: documentControl에서 현재 폴더 아이디와 폴더패스를 알기 위해 전역변수 추가
 */
/* 
 * 작성자		: 소진희
 * 작성일		: 2022-03-30
 * 설명		: server_domain=app.properties에서 불러오는 서버 도메인(https). 외부로 나갈 이메일 다운로드 링크에 쓰이는 변수
 * 			current_server_domain=사용자의 접속 환경에 따라 달라짐(내부-http/외부-https). 다운로드 링크에 쓰이는 변수
 */
var server_domain;
var current_server_domain;
var current_folder_id;
var popup_folder_id;
var current_folder_path;
var dcc_ready = false;
var trOut_ready = false;
var trIn_ready = false;
var processTypeByFolderPath;
var current_process_type = 'General';
var current_doc_type = 'General';
var current_FolderDiscipline;
var folder_discipline_code;
var current_folder_discipline_code;
var current_folder_discipline_code_id;
var current_folder_discipline_display;
var current_folder_docType ='General';
var DCCorTRA = '';
var outProcessType = '';
var inProcessType = '';
var prj_grid;
var userInfo = {};

var loading = '<div id="loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';
var progressbar = '<div class="progressDiv"><div class="progress"><div class="bar progress-bar progress-bar-info" role="progressbar" style="width: 0%"></div></div></div>';
$(function(){
	
	initPrjSelectGrid();
	
	server_domain = $('#server_domain').val();
	current_server_domain = $('#current_server_domain').val();
	//마지막 조회했던 프로젝트 정보 (prj_id,prj_nm) 가져옴
	// var prj_nm = 
	var prj_info = getLastConPrjId();
	prj_id = prj_info.prj_id;
	let prj_nm = prj_info.last_con_prj_name;
	
	if($('#newTab_prj_id').val()!=''){
		prj_id=$('#newTab_prj_id').val();
		prj_nm=$('#newTab_prj_nm').val();
		var url = location.origin + '/main.do';
		history.pushState({}, null, url);
	}
	$('#selected_prj').html(prj_nm);
	
	// 헤더 프로젝트 리스트 온클릭 속성 부여
	headerPrjClick();
	$('#selectPrjId').val(prj_id);
	

	
	//팝업 초기화
    $(".pop_myinfo").dialog({
        draggable: false,
        autoOpen : false
    });
    $(".pop_deletefolder").dialog({
        draggable: false,
        autoOpen : false
    });
    $(document).on("click", ".myinfo", function() {
        $(".pop_myinfo").dialog("open");
    	var user_id = $('#user_id').val();
        $.ajax({
    	    url: 'SignExists.do',
    	    type: 'POST',
    	    data:{
    	    	user_id: user_id
    	    },
    	    success: function onData (data) {
    			if(data[0]==='서명 존재'){
    				$('#signexists').html('<img style="width:150px;height:80px;display: block;margin:0 auto;" src="'+current_server_domain+'/getSignImage.do?sfile_nm='+data[1].sfile_nm+'">');
    			}else{
    		    	$('#signexists').html('<a style="color:red;">업로드된 서명이 없습니다.<br>서명을 업로드 해주세요.</a>')
    			}
    	    },
    	    error: function onError (error) {
    	        console.error(error);
    	    }
    	});
        return false;
    });

    $(document).on("click", ".headerDic", function() {

    	var tab_id = "dictTab";
    	type = 'PCO2';
    	text = 'DICTIONARY VIEW';
		//tab_id = 'emailSettingTab';
		jsp_Path = '/page/dictionarysearchView';
    	
    	
    	if(addTab(jsp_Path,text,tab_id, type, null, id)){ // 탭추가
			// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
			let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
			tabList[tabIdx].folder_id = id;
			
			
			
			tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
			
			tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
				
		}
    	
    });
    
	//일반문서(General) 파일첨부
    var fileTarget = $('.filebox .userinfoDocumentFileAttach');

    fileTarget.on('change', function () {
        if (window.FileReader) {
            var fullName = $(this)[0].files[0].name;
            var size = $(this)[0].files[0].size;
            var extension = $(this)[0].files[0].name.split('.').pop().toLowerCase();
        } else {
            var fullName = $(this).val().split('/').pop().split('\\').pop();
        }
       
        var fileName = fullName.replace('.'+extension, '');
        userInfo.rfile_nm = fullName;
        userInfo.file_type = extension;
        userInfo.file_size = size;
        
    });

    
    $('#updateUsersVO').click(function() {
    	var user_id = $('#user_id').val();
    	var user_kor_nm = $('#user_kor_nm').val();
    	var user_eng_nm = $('#user_eng_nm').val();
    	var email_addr = $('#email_addr0').val() + '@' + $('#email_addr1').val();
    	var offi_tel = $('#offi_tel').val();
    	let file_value = $('#userinfo_attach_filename').val();
        
		let form = $('#updateUserInfoForm')[0];
	    let formData = new FormData(form);
	    formData.set('user_id',user_id);
	    formData.set('user_kor_nm',user_kor_nm);
	    formData.set('user_eng_nm',user_eng_nm);
	    formData.set('email_addr',email_addr);
	    formData.set('offi_tel',offi_tel);
	    formData.set('file_value',file_value);
	    if(file_value!==''){
	    	userInfo.sfile_nm = user_id+'.'+userInfo.file_type;
		    formData.set('sfile_nm',userInfo.sfile_nm);
		    formData.set('rfile_nm',userInfo.rfile_nm);
		    formData.set('file_type',userInfo.file_type);
		    formData.set('file_size',userInfo.file_size);
	    }
	    
	    $.ajax({
			url: 'updateUserVOSaveChkFile.do',
			type: 'POST',
			data: formData,
			enctype:'multipart/form-data',
	        processData:false,
	        contentType:false,
	        dataType:'json',
	        cache:false,
	        async: false,
			success: function onData (data) {
				if(data > 0) {
					 $.ajax({
							type: 'POST',
					        enctype: 'multipart/form-data',
							url: 'updateUsersVO.do',
					        data:formData,          
					        processData: false,    
					        contentType: false,      
					        cache: false,           
					        timeout: 600000,  
							success: function(data) {
				    	    	alert('modified this info');
				    	    	location.reload();
							},
						    error: function onError (error) {
						        console.error(error);
						    }
						});
				}else {
					alert("Unsupported file format. Please contact the administrator");
				}
			},
			error: function onError (error) {
		    	console.error(error);
//		        alert("등록 오류");
		    }
		});
    });
    
    getTree();
    
    
    /*
    $.contextMenu({
        selector: '.prj_select_grid', 
        callback: function(key, options) {
            var newTab_prj_id = $(this).attr('id').replace('prj_id_is_','');
            window.open("/main.do?prj_id="+newTab_prj_id);
        },
        items: {
            "newTab": {name: "새탭으로 열기"}
        }
    });
    
    */
});

/**
 * 헤더 프로젝트 리스트 그리드 init
 * @returns
 */
function initPrjSelectGrid(){
	
	prj_grid  = new Tabulator("#prj_list_grid", {
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
		],
		rowContextMenu:[
		 {
            label:"새 탭으로 열기",
            action:function(e, row){
            
            	 var frmObj = $('<form>', {'id': 'fm_formIO', 'action': 'main.do', 'method':'POST', 'target':'_blank'});
            	 var inpObj = $('<input>', {'id':'prj_id', 'name':'prj_id', 'value':row.getData().prj_id});
            	 
            	 frmObj.append(inpObj);
            	 $(document.body).append(frmObj);
            
            	/*headerPrjChange(row.getData().prj_id);
            
            	 $("#prj_id").val(row.getData().prj_id);
            	 $('#fm_formIO').submit();*/
                
                window.open("/newTab.do?prj_id="+row.getData().prj_id);
            }
        }
		]
	}); 
	
	prj_grid.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	let prjList = data.model.prjList;
		    	prjList = prjList.sort((a, b) => a.prj_id - b.prj_id);
		    	
		    	prj_grid.setData(prjList);
		    	prj_grid.redraw();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#selectPrjId").val(rowData.prj_id);
		$("#selected_prj").html(rowData.prj_nm);
		headerPrjChange(rowData.prj_id);
		
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prj_search') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
			    	prjSearch();
			    }
		}	   
	});
	
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjSearch(){
	let keyword = $("#prj_search").val();
	let grid = prj_grid;
	
	$.ajax({
	    url: 'prjSearch.do',
	    type: 'POST',
	    data:{
	    	keyword : keyword
	    },
	    success: function onData (data) {
	    	let prjList = data.model.prjList;
	    	prjList = prjList.sort((a, b) => a.prj_id - b.prj_id);
	    	
	    	grid.setData(prjList);
	    	grid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// 헤더 프로젝트 리스트 온클릭 속성 부여
function headerPrjClick(){
	$('.prj_select').click(function() {
		var select_prj_id = $(this).attr('id').replace('prj_id_is_','');
		prj_id = select_prj_id;
		headerPrjChange(prj_id);
    });
}
//마지막으로 조회한 프로젝트 정보 저장하는 함수
function headerPrjChange(headerPrjSelect){
	$.ajax({
	    url: 'updateLastConPrjId.do',
	    type: 'POST',
	    data:{
	    	last_con_prj_id: headerPrjSelect
	    },
	    success: function onData (data) {
	    	location.reload();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}
//마지막으로 조회한 프로젝트 정보 가져오는 함수
function getLastConPrjId(){
	var last_con_prj_name='';
	
	let result = {};
	
	$.ajax({
	    url: 'getLastConPrjId.do',
	    type: 'POST',
        async: false,
	    success: function onData (data) {
	    	if(data[0]){
	    		result.prj_id = (data[0].last_con_prj_id ? data[0].last_con_prj_id : '0000000001');
	    		result.last_con_prj_name = data[0].prj_nm;
	    		
	    		
	    		// 접근 가능한 프로젝트가 없는 경우
	    		if(result.prj_id == '-1'){
	    			console.log("접근 가능한 프로젝트가 없음");
	    			noPrj = true;
	    		}
	    	}
	    	else{
	    		result.prj_id = '0000000001';
	    		result.last_con_prj_name = 'Default project';
	    	}
	    	
	    	if($('#NotiPrjId').val()!=='' && $('#NotiPage').val()!=='' && $('#NotiFolderId').val()!==''){
	    		result.prj_id = $('#NotiPrjId').val();
	    		result.last_con_prj_name = $('#NotiPrjNm').val();
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	return result;
}
function logout(){
	var headerPrjSelect = prj_id;
	
	if(noPrj){
		headerPrjSelect=null;
	}
	$.ajax({
	    url: 'updateLastConPrjId.do',
	    type: 'POST',
	    data:{
	    	last_con_prj_id: headerPrjSelect
	    },
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	var userId = $("#session_user_id").val();
	if(userId != "ADMIN"){
		$.ajax({
			url: 'saveUserConnect.do',
		    type: 'POST',
		    async: false,
		    data: {
		    	user_id:userId
		    },
		    success: function onData (data) {
		    	// 최초 로그인시 p_user_connect에 아이디,최초 로그인 날짜등 insert, update
		    	//console.log("data = "+data);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
	}
	location.href="logout.do";
}
//20211101083217같은 string 형식의 데이터를 년-월-일 시:분:초 형태로 바꿔주는 함수
function getDateFormat(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);
	const hh = datestring.substring(8,10);
	const mm = datestring.substring(10,12);
	const ss = datestring.substring(12,14);

	return year+'-'+month+'-'+day+' '+hh+':'+mm+':'+ss;
}
function getDateFormat_YYYYMMDD(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);

	return year+'-'+month+'-'+day;
}
function to_date_format(date_str, gubun)
{
	
	if(date_str == null) return '';
	
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(4,6);
    var sDate = yyyyMMdd.substring(6,8);
    return sYear + gubun + sMonth + gubun + sDate;
}
function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

//이메일 정규식 체크
function email_check(email) {
	if(email===''){
		return true;
	}else{
		var reg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		return reg.test(email);
	}
}





/* 
 * 작성자		: 권도일
 * 작성일		: 2021-11-10
 * 메소드명	: globalSearch(),getTree(), getFolderList()
 * 설명		: 글로벌서치 수행, 트리 그려주는 함수
 */

function globalSearch(keyword){
	
	// 검색어 미입력시 미작동
	if(keyword.length < 1) {
		alert("Type a search keyword");
		return;
	}
	
	tab_id = "globalSearchTab";
	text = "Search";
	jsp_Path = "/common/globalSearch";
	
	if(addTab(jsp_Path,text,tab_id,"SEARCH",keyword)){ // 탭추가
		// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
		tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
		tabOn("#"+tab_id); // 새로 추가된 탭 활성화				
	} 	
	
	$("#searchKeyword").val(keyword);
	if(search) search();
}
var treeCheck = 0;
var folderList;
var isNew = false;

function treeRefresh(){
	folderList = getFolderList();
	$("#tree").jstree(true).settings.core.data = folderList;
	$("#tree").jstree(true).refresh();	
}

function treeDataRefresh(){
	folderList = getFolderList();
	$("#tree").jstree(true).settings.core.data = folderList;
}


function getTree(){
	folderList = getFolderList();

	var pasteDisable = 1;
	var moveFolderId = 0;
	function jstreeContextMenu(node) {
		
		var tree = $("#tree").jstree(true);
		
		
		let  _prj_id = getPrjInfo().id;
		
		$.ajax({
		    url: 'setSession.do',
		    type: 'POST',
		    data: {
		    	prj_id:_prj_id,
		    	folder_id:node.id
		    },
			async: false,
		    success: function onData (data) {
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		
		let auth = checkFolderAuth(_prj_id,node.id);
		
		console.log(auth);
		
	    // 모든 곳에 적용되는 컨텍스트메뉴 items
	    var items = {
	        thisItem: {
	            "label": 'Folder : '+node.text,
	            "separator_before" : false,
	            "separator_Default projectafter" : true,
	            "action": function(obj) {}
	        },
	        createItem: {
	            "label": "New",
	            "action": function(data) {
	            	isNew = true;
	            	var inst = $.jstree.reference(data.reference),
                    obj = inst.get_node(data.reference);
	            	inst.create_node(obj, {}, "last", function (new_node) {
                    	treeCheck = 1;
	            		new_node.data = {file: true};
	            		setTimeout(function () { inst.edit(new_node); },0);
	                   $('#tree').on('rename_node.jstree', function (e, data) {
	                    	if(data.node.parent==new_node.parent && data.text==new_node.text && isNew===true){
		                    	$.ajax({
		            			    url: 'createNewFolder.do',
		            			    type: 'POST',
		            			    data:{
		            			    	prj_id: prj_id,
		            			    	parent_folder_id:data.node.parent,
		            			    	folder_nm:data.text
		            			    },
		            			    success: function onData (data) {
			        			    	var data1 = getFolderList();
			        			    	tree.settings.core.data = data1;
			        			    	tree.refresh();
				                    	treeCheck = 0;
				                    	folderList = getFolderList();
		            			    },
		            			    error: function onError (error) {
		            			        console.error(error);
		            			        treeCheck = 0;
		            			    }
		            			});
	                    	}
		            	});
	            		
	                });
	            }
	        },
	        renameItem: {
	            "label": "Rename",
	            "action": function(obj) { 
	            	isNew = false;
	            	tree.edit(node);
	            	var folder_nm = node.text;
	            	$('#tree').on('rename_node.jstree', function (e, data) {
	            		$.ajax({
	            			    url: 'renameFolder.do',
	            			    type: 'POST',
	            			    data:{
	            			    	prj_id: prj_id,
	            			    	folder_id: data.node.id,
	            			    	folder_nm: folder_nm,
	            			    	new_folder_nm:data.text
	            			    },
	            			    success: function onData (data) {
	            			    	folderList = getFolderList();
	            			    },
	            			    error: function onError (error) {
	            			        console.error(error);
	            			    }
	            		});
	            	});
	            }
	        },
	        deleteItem: {
	            "label": "Delete",
	            "action": function(obj) {
	            	$('#selectedFolder').html(node.text);
	            	$(".pop_deletefolder").dialog("open");
	                $('#deletedSelectedFolder').click(function() {
		            	$.ajax({
	        			    url: 'deleteFolder.do',
	        			    type: 'POST',
	        			    data:{
	        			    	prj_id: prj_id,
	        			    	folder_id: node.id
	        			    },
	        			    success: function onData (data) {
	        			    	if(data[0]!='삭제 가능'){
	        			    		alert(data[0]);
		    		            	$(".pop_deletefolder").dialog("close");
	        			    	}else{
	        	                	tree.delete_node(node.id);
		        			    	var data = getFolderList();
		        			    	tree.settings.core.data = data;
		        			    	tree.refresh();	
		    		            	$(".pop_deletefolder").dialog("close");
		    		            	folderList = getFolderList();
	        			    	}
	        			    },
	        			    error: function onError (error) {
	        			        console.error(error);
	        			    }
		            	});
	                });
	                $('#deletedCancel').click(function() {
		            	$(".pop_deletefolder").dialog("close");
	                });
	            }
	        },
	        cutItem: {
	            "label": "Cut",
	            "action": function(obj) {
	            	moveFolderId = node.id;
	            	pasteDisable = 0; //paste contextmenu 활성화
	            }
	        },
	        pasteItem: {
	            "label": "Paste",
	            "action": function(obj) { 
	            	if(moveFolderId!=0){
	            		$.ajax({
	        			    url: 'moveFolder.do',
	        			    type: 'POST',
	        			    data:{
	        			    	prj_id: prj_id,
	        			    	folder_id: moveFolderId,
	        			    	parent_folder_id: node.original.id
	        			    },
	        			    success: function onData (data) {
	        			    	pasteDisable = 1; //paste contextmenu 비활성화
	        			    	if(data[0]==='이동 가능'){
		        			    	if (moveFolderId!=node.original.id){
			        			    	var data = getFolderList();
			        			    	tree.settings.core.data = data;
			        			    	tree.refresh();	
			        			    	folderList = getFolderList();
		        			    	}
	        			    	}else{
	        			    		alert(data[0]);
	        			    	}
	        			    },
	        			    error: function onError (error) {
	        			        console.error(error);
	        			    }
		            	});
	            	}
	            }
	        },
	        /**
	         * DCI 업로드 템플릿 index Register
	         */
	        indexRegisterItem: {
	            "label": "Index Register",
	            "action": function(obj) {

	            	// 자식 노드가 없는 최상위 노드의 경우 처리
	            	// if(node[0].querySelector("a").getAttribute( 'aria-level' ) < 2) return;
	            	
	            	let id = obj.reference.attr("id"); //id 가져오기
	            	id = Number(id.replace("_anchor",""));
	            	let text = "Document Control";
	            	//옮기지 말것
	    			current_folder_id = id;
	    			
	    		  	let jsp_Path = "/page/documentControl";
	            	let tab_id = "dccTab";
	            	
	            	// 폴더메뉴의 타입
	            	let type;
	            	for (let key in folderList) {
	            			
	            		  const value = folderList[key]
	            		  if(value.id === parseInt(id)){
	            			  type = value.type;
	            			  if(type==='DCC'){
	            				  current_process_type = value.process_type;
	            				  current_doc_type = value.doc_type;
	            				  current_corr_process_type = value.corr_process_type;
	            				  current_folder_discipline_code_id = value.discipline_code_id;
	            				  current_folder_discipline_display = value.discipline_display;
	            				  current_folder_discipline_code = value.discipline_code;
	            			  }
	            		  }
	            	}
	            	
	            	
	    			let tabIdx = tabList.findIndex(i => i.id == "#dccTab");
	    			if(tabIdx<0) {
	    				// Document Control 창 활성화 작업해야함
	    				
    		    		/*
    		    		let tab_id = "dccTab";
	    				let jsp_Path = '/page/documentControl';
	    				let type = 'DCC';
	    				let text ='Document Control';
    		    		
	    				
	    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
	    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
	    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
	    					tabList[tabIdx].folder_id = current_folder_id;
	    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
	    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
	    				}
    		    		
    		    		$.ajax({
	    				    url: 'setSession.do',
	    				    type: 'POST',
	    				    data: {
	    				    	prj_id:prj_id,
	    				    	folder_id:current_folder_id
	    				    },
	    					async: false,
	    				    success: function onData (data) {
	    				    },
	    				    error: function onError (error) {
	    				        console.error(error);
	    				    }
	    				});
	    				*/
    		    		
    		    		return;
	    			}
	    			
	    			tabList[tabIdx].folder_id = id;
	    			
	    			let tmpPathArr = $('#tree').jstree("get_path",id);
	    			current_folder_path = ""; 
	    			let tmpPathStr = "";
	    			for(let i=0;i<tmpPathArr.length;i++)
	    				tmpPathStr += tmpPathArr[i] + "/"
	    			current_folder_path = tmpPathStr.substr(0,tmpPathStr.length-1);
	    			$('#dc_current_folder_path').val(current_folder_path);
	            	
	            
	            	
	            	
	            	$(".pop_indexReg").dialog({modal:true,resizable:false,width: 1400}).dialogExtend({
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
	            	
	            	
	            	if(addTab(jsp_Path,text,tab_id, type)){ // 탭추가
	        			// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
	        			tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
	        			tabOn("#"+tab_id, type, dciIndexRegister(id)); // 새로 추가된 탭 활성화
	        		}else if(tabList.length < maxTabSize)
	        				tabOn("#"+tab_id, type, dciIndexRegister(id));
	        		
	            	
	            }
	        },
	        authControlItem: {
	            "label": "권한관리",
	            "action": function(obj) {  
	            	folderAuth(Number(node.id));
	            }
	        }
	    };
	    
	    let adminAndDCCArray = [];
		  $.ajax({
			    url: 'getAdminAndDCCList.do',
			    type: 'POST',
			    data:{
			    	prj_id:getPrjInfo().id
			    },
			    async:false,
			    success: function onData (data) {
				    let adminAndDCCString = '';
			    	adminAndDCCString = data[0];
			    	adminAndDCCArray = adminAndDCCString.split(',');
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	    
		let session_user_id = $('#session_user_id').val();
	    if(adminAndDCCArray.includes(session_user_id)){	//Admin이나 DCC일 때만 트리메뉴 수정가능
		    if (node.parent=='#' && node.original.type=='DCC') {
		    	items.createItem._disabled = false;
		    	items.renameItem._disabled = true;
		    	items.deleteItem._disabled = true;
		    	items.cutItem._disabled = true;
		    	items.pasteItem._disabled = true;
		    }else if (node.original.type=='DCC') {
		    	if(pasteDisable==1){
		    		items.pasteItem._disabled = true;
		    	}else{
			    	items.pasteItem._disabled = false;
		    	}	    	
		    }else if (node.original.type=='TRA') {
		    	items.authControlItem._disabled = true;
		    	
		    	if(auth.isAdmin == "Y"){
		    		items.createItem._disabled = false;
			    	items.renameItem._disabled = false;
			    	items.deleteItem._disabled = false;
		    	}else{
		    		items.createItem._disabled = true;
			    	items.renameItem._disabled = true;
			    	items.deleteItem._disabled = true;
		    	}
		    	
		    	items.cutItem._disabled = true;
		    	
		    	
		    	if(pasteDisable==1){
		    		items.pasteItem._disabled = true;
		    	}else{
			    	items.pasteItem._disabled = false;
		    	}
		    }else if (node.original.type=='PBO' || node.original.type=='REP') {
		    	items.pasteItem._disabled = true;
		    	items.createItem._disabled = true;
		    	items.renameItem._disabled = true;
		    	items.deleteItem._disabled = true;
		    	items.cutItem._disabled = true;
		    }
		    else{
		    	delete items.thisItem;
		    	delete items.createItem;
		    	delete items.renameItem;
		    	delete items.deleteItem;
		    	delete items.cutItem;
		    	delete items.pasteItem;
		    	delete items.indexRegisterItem;
		    	delete items.authControlItem;
		    }
		    
	    
	    }
	    else{
	    	if(auth.w == 'N'){
	    		items.createItem._disabled= true;
	    		items.renameItem._disabled= true;
	    		items.cutItem._disabled= true;

	    		items.authControlItem._disabled =true;
	    		items.indexRegisterItem._disabled = true;
	    		
	    		if(pasteDisable==1){
		    		items.pasteItem._disabled = true;
		    	}else{
			    	items.pasteItem._disabled = false;
		    	}	    	
	    		
	    	} else{
	    		items.createItem._disabled= false;
	    		items.renameItem._disabled= false;
	    		items.cutItem._disabled= false;
	    		
	    		if(pasteDisable==1){
		    		items.pasteItem._disabled = true;
		    	}else{
			    	items.pasteItem._disabled = false;
		    	}	
	    		
	    		items.authControlItem._disabled = true;
	    		items.indexRegisterItem._disabled = false;
	    	}
	    	
	    	if(auth.d == 'N'){
	    		items.deleteItem._disabled= true;   		
	    	}
	    	// items = {};
	    	
		    if(node.original.type=='PBO' || node.original.type=='REP'){
		    	delete items.thisItem;
		    	delete items.createItem;
		    	delete items.renameItem;
		    	delete items.deleteItem;
		    	delete items.cutItem;
		    	delete items.pasteItem;
		    	delete items.indexRegisterItem;
		    	delete items.authControlItem;
		    }
		    if(node.original.type=='COL'){
		    	delete items.thisItem;
		    	delete items.createItem;
		    	delete items.renameItem;
		    	delete items.deleteItem;
		    	delete items.cutItem;
		    	delete items.pasteItem;
		    	delete items.indexRegisterItem;
		    	delete items.authControlItem;
		    }
	    	
	    	
	    	
	    }
	    
	    
		// index Register 할 수있는 폴더인지 체크
    	$.ajax({
		    url: 'checkProcessType.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id,
		    	folder_id:node.id
		    },
		    async:false,
		    success: function onData (data) {
		    	let result = data.model;
		    	if(!result.status) {
		    		items.indexRegisterItem._disabled = true;
		    	}
		    	else{
		    		if(auth.w == 'Y')
		    			items.indexRegisterItem._disabled = false;
		    	}
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
    	
	    return items;
	}
	
	// $('#tree').jstree(true).settings.core.data = folderList;
	let emptyFolder = {};
	emptyFolder.id = 1;
	emptyFolder.parent = '#';
	emptyFolder.text = "There is no folder with read permission to access"
	var tree = $('#tree').jstree({
	     "core" : {
	    	 'data' : folderList.length>0?folderList:emptyFolder,
	         "check_callback" : true
	       },
	      "plugins" : [ "contextmenu",'sort' ],
	      'contextmenu' : {
	    	  "items" : jstreeContextMenu
	       },'sort' : function(a, b) {
	           a = this.get_node(a);
	           b = this.get_node(b);
	           if(a.icon != 'resource/images/pinwheel.png' && a.original.type == 'DCC'){
	        	   return (a.text > b.text) ? 1 : -1;		           
	           }	           
	       }	           	       
	    })
	    .on('dblclick.jstree', function(event) { // 더블클릭 이벤트 바인딩
	    	var node = $(event.target).closest("li");
	    	var iconOcl = $(event.target).closest("i");
	    	
	    	// 자식 노드가 없는 최상위 노드의 경우 처리
	    	// if(node[0].querySelector("a").getAttribute( 'aria-level' ) < 2) return;
	    	
	    	var id = node[0].id; //id 가져오기
	    	var isLeaf = node[0].classList.contains("jstree-leaf"); // leaf노드인지 확인
	    	var text = node[0].querySelector("a").childNodes[1].nodeValue;

	    	try {
		    	var isOcl = iconOcl[0].classList.contains("jstree-ocl"); // ocl노드인지 확인
			} catch (e) {
				// jstree-ocl(+ 모양 버튼) 클릭 안했을때만 실행
		    	treeMenuActive(event);
		    }
	    	} )
	    .on('click.jstree', function(event) { // 클릭 이벤트 바인딩
	    	var node = $(event.target).closest("li");
	    	var iconOcl = $(event.target).closest("i");

	    	// 자식 노드가 없는 최상위 노드의 경우 처리
	    	// if(node[0].querySelector("a").getAttribute( 'aria-level' ) < 2) return;
	    	
	    	var id = node[0].id; //id 가져오기
	    	var isLeaf = node[0].classList.contains("jstree-leaf"); // leaf노드인지 확인
	    	var text = node[0].querySelector("a").childNodes[1].nodeValue;
	    	

	    	try {
		    	var isOcl = iconOcl[0].classList.contains("jstree-ocl"); // ocl노드인지 확인
			} catch (e) {
				// jstree-ocl(+ 모양 버튼) 클릭 안했을때만 실행
		    	treeMenuActive(event);
		    }
			} )
	    .on('loaded.jstree', function(e, data) {
	    	let folder_id = $('#NotiFolderId').val();
			if(folder_id!=''){
				selectPrjId = $('#NotiPrjId').val();
				notificationDocumentControl();
			}
			//Notification 페이지 로드 후 다시 Notification 페이지가 열리지 않도록 value값을 빈 문자열로 바꿔주고 주소창의 파라미터도 없애줌
			$('#NotiFolderId').val('');
			$('#NotiPrjId').val('');
			$('#NotiPage').val('');
			$('#NotiFolderId').val('');
			history.replaceState({}, null, location.pathname);
		});
	
	$('#tree').jstree(true).refresh();
	
}
/**
 * 
 * @param directAccess
 * @returns
 */
function treeMenuActive2(directAccess){
	let data = directAccess;
	$.ajax({
	    url: 'setSession.do',
	    type: 'POST',
	    data: {
	    	prj_id:prj_id,
	    	folder_id:data.folder_id
	    },
		async: false,
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	let  _prj_id = getPrjInfo().id;
	let auth = checkFolderAuth(_prj_id,id);

	if(auth.r == 'N'){
		alert("You don't have access to this folder.Please contact DCC");
		return;
	}
	
	// 폴더메뉴의 타입
	var type;
	for (let key in folderList) {
		  const value = folderList[key]
		  if(value.id === parseInt(id)){
			  type = value.type;
			  if(type==='DCC'){
				  current_process_type = value.process_type;
				  current_doc_type = value.doc_type;
				  current_corr_process_type = value.corr_process_type;
				  }
			  $.ajax({
				    url: 'getProcessDocType.do',
				    type: 'POST',
				    data:{
				    	prj_id:getPrjInfo().id,
				    	folder_id:value.id
				    },
				    async:false,
				    success: function onData (data) {
				    	current_folder_docType = data[0];
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
				   
				  current_folder_discipline_code_id = value.discipline_code_id;
				  current_folder_discipline_display = value.discipline_display;
				  current_folder_discipline_code = value.discipline_code;
			  }
		  }
	if(node[0].parentElement.parentElement.id==='tree'){
		if(text!=='PROJECT NOTICE BOARD'){//'PROJECT NOTICE BOARD'를 제외한 루트노드 addTab 안함
			return false;
		}
	}
	
	
	
	var jsp_Path = "/common/empty"; // include 시켜줄 jsp 경로
	
	var tab_id = "tab_"+id;
	
	// DCC타입의 메뉴는 단일 탭 처리
	if(type === "DCC") {
		if((current_process_type==='TR OUTGOING'||current_process_type==='VTR OUTGOING'||current_process_type==='STR OUTGOING') && current_process_type!=='LETTER' && current_process_type!=='E-MAIL'){
			tab_id = "outgoingTab";
			jsp_Path = '/page/trOutgoing';
			type = 'TROutgoing';

			if(current_process_type==='TR OUTGOING'){
				outProcessType = 'TR';
			}else if(current_process_type==='VTR OUTGOING'){
				outProcessType = 'VTR';
			}else if(current_process_type==='STR OUTGOING'){
				outProcessType = 'STR';
			}

			if(auth.w==='Y'){
				$('#out_incomingreg').css('display','inline-block');
				$('#out_frompreTR').css('display','inline-block');
				$('#out_createTR').css('display','inline-block');
				$('#out_save').css('display','inline-block');
				$('#out_issue').css('display','inline-block');
				$('#out_email').css('display','inline-block');
				$('#out_add_doc').css('display','inline-block');
				$('#out_del_doc').css('display','inline-block');
			}else{
				$('#out_incomingreg').css('display','none');
				$('#out_frompreTR').css('display','none');
				$('#out_createTR').css('display','none');
				$('#out_save').css('display','none');
				$('#out_issue').css('display','none');
				$('#out_email').css('display','none');
				$('#out_add_doc').css('display','none');
				$('#out_del_doc').css('display','none');

		    	$('#out_tr_subject').attr('disabled',true);
		    	$('#out_discipline_code_id').css('pointer-events', 'none');
		    	$('#out_discipline_code_id').css('background', 'none');
		    	$('#out_discipline_code_id').css('background-color', '#FAFAFA');
		    	$('#out_tr_issuepur_code_id').css('pointer-events', 'none');
		    	$('#out_tr_issuepur_code_id').css('background', 'none');
		    	$('#out_tr_issuepur_code_id').css('background-color', '#FAFAFA');
		    	$('#out_send_date').attr('disabled',true);
		    	$('#out_req_date').attr('disabled',true);
		    	$('#out_tr_remark').attr('disabled',true);
			}
			if(auth.d==='Y'){
				$('#out_delete').css('display','inline-block');
				$('#out_issue_cancel').css('display','inline-block');
			}else{
				$('#out_delete').css('display','none');
				$('#out_issue_cancel').css('display','none');
			}
		}else if((current_process_type==='TR INCOMING'||current_process_type==='VTR INCOMING'||current_process_type==='STR INCOMING') && current_process_type!=='LETTER' && current_process_type!=='E-MAIL'){
			tab_id = "incomingTab";
			jsp_Path = '/page/trIncoming';
			type = 'TRIncoming';
			$('#tr_incoming_filename').val('');	//comment file 초기화

			if(current_process_type==='TR INCOMING'){
				inProcessType = 'TR';
			}else if(current_process_type==='VTR INCOMING'){
				inProcessType = 'VTR';
			}else if(current_process_type==='STR INCOMING'){
				inProcessType = 'STR';
			}
			
			if(auth.w==='Y'){
				$('#in_createTR').css('display','inline-block');
				$('#in_save').css('display','inline-block');
				$('#in_add_doc').css('display','inline-block');
				$('#in_del_doc').css('display','inline-block');
			}else{
				$('#in_createTR').css('display','none');
				$('#in_save').css('display','none');
				$('#in_add_doc').css('display','none');
				$('#in_del_doc').css('display','none');
				
				$('#tr_incoming_attach').css('display','none');
				$('#in_tr_no').attr('disabled',true);
				$('#in_tr_subject').attr('disabled',true);
				$('#in_received_date').attr('disabled',true);
				$('#in_note').attr('disabled',true);
			}
			if(trIn.trInSelectedTable===undefined || trIn.trInSelectedTable.getData().length===0){
				$('#in_del_doc').css('display','none');
			}
			if(auth.d==='Y'){
				$('#in_delete').css('display','inline-block');
			}else{
				$('#in_delete').css('display','none');
			}
		}/*else if(current_process_type==='CORRESPONDENCE'){
			//탭 열지 않음
			tab_id = "corrRoot";
			jsp_Path = '/page/documentControl';
			type = 'corrRoot';
		}*/else{

			tab_id = "dccTab";
			text = "Document Control";
			jsp_Path = '/page/documentControl';
			DCCorTRA = 'DCC';
			//옮기지 말것
			current_folder_id = id;
			
			let tabIdx = tabList.findIndex(i => i.id == "#dccTab");
			if(tabIdx>-1) tabList[tabIdx].folder_id = id;
			console.log("hi");

			current_folder_path = $('#tree').jstree().get_path(node[0], '/');
			$('#dc_current_folder_path').val(current_folder_path);
			

		}
	}
	
	if(type === "COL") { // COLLABORATION (전자결재)

		
		if(text === 'COLLABORATION (전자결재)') return;
		
		if(text.includes('결재함')) return;
		
		tab_id = "colTab";
		text = "전자결재";
		jsp_Path = '/page/collaboPage';
		
	}
	
	if(type === "REP") { // REPORT
		if(text === 'ENGINEERING STATUS') {
			tab_id = "prjReportEngTab";
			jsp_Path = '/page/projectReportEngineeringStatus';
		}else if(text === 'PROCUREMENT STATUS') {
			tab_id = "prjReportProTap";
			jsp_Path = '/page/projectReportProcurementStatus';
		}else if(text === 'VENDOR STATUS') {
			tab_id = "prjReprotVenTab";
			jsp_Path = '/page/projectReportVendorStatus';
		}else if(text === 'SITE STATUS') {
			tab_id = "prjReprotSiteTab";
			jsp_Path = '/page/projectReportSiteStatus';
		}else if(text === 'CORRESPONDENCE STATUS') {
			tab_id = "prjReprotCorrTab";
			jsp_Path = '/page/projectReportCorrStatus';
		}
	}
	
	// DCS타입은 Document Search
	if(type === "DCS") {
		tab_id = "docSearchTab";
		text = "Document Search";
		jsp_Path = "/common/docSearch";
	}
	
	if(type === 'PBO'){ // Project notice board
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT NOTICE BOARD",
		    	method: "조회"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		tab_id = "prjNoticeBoardTab";
		jsp_Path = '/page/projectNoticeboard';
	}
	
	if(type === 'PCO'){ // Project configuration
		if(text === 'PROJECT GENERAL') {
			$.ajax({ // 로그인시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "PROJECT GENERAL > 폴더정비",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'projectGENTab';
			jsp_Path = '/page/projectGeneral';
		}
		if(text === '외부 사용자관리') {
			window.open('./pop/outUserMng.jsp','외부 사용자 관리','top=50, left=50, width=1500, height=750, resizable=yes');
			return false;
			/*tab_id = 'userRegisterTab';
			jsp_Path = '/page/userRegister';
			userMngPage = 'DCC';*/
		}
		if(text === 'ETC') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "PROJECT ETC",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'projectETCTab';
			jsp_Path = '/page/projectETC';
		}
		if(text === 'DICTIONARY') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "DICTIONARY",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			jsp_Path = '/page/dictionarysearch';
		}
		if(text === 'WBS CODE CONTROL') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "WBS CODE CONTROL",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			jsp_Path = '/page/wbsCodeControl';
		}
		if(text === 'DAILY EMAILING SETTING') {
			tab_id = 'emailSettingTab';
			jsp_Path = '/page/emailSetting';
		}
		if(text === 'E-MAILING STATUS') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "E-MAILING STATUS",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'emailingStatusTab';
			jsp_Path = '/page/emailingStatus';
		}
		if(text === 'DOCNO TRNO CHANGE') {
			tab_id = 'docTrNoChangeTab';
			jsp_Path = '/page/docTrNoChange';
		}
		if(text === 'DRN REVIEW CONCLUSION') jsp_Path = '/page/drnReviewConclusion';
		// if(text === '외부사용자 관리') jsp_Path = "/common/empty";
		// if(text === 'ETC') jsp_Path = "/common/empty";
	}
	
	if(type === 'CON'){ // System configuration
		if(text === 'USER REGISTER') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "USER REGISTER",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'userRegisterTab';
			jsp_Path = '/page/userRegister';
			userMngPage = 'ADMIN';
		}
		if(text === 'PROJECT CREATION') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "PROJECT CREATION",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'projectCreationTab';
			jsp_Path = '/page/projectCreation';
		}
		if(text === 'PASSWORD POLICY') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "PASSWORD POLICY",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			jsp_Path = '/page/passwordPolicy';
		}
		if(text === 'NOTICE BOARD') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "NOTICE BOARD",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'noticeBoardTab';
			jsp_Path = '/page/noticeBoard';
		}
		if(text === 'DATA MIGRATION') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "DATA MIGRATION",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			jsp_Path = '/page/dataMigration';
		}
		if(text === 'IP보안') {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "IP보안",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = 'securityIpTab';
			jsp_Path = '/page/securityIp';
		}
		if(text === '보안로그') {
			tab_id = 'securityLogTab';
			jsp_Path = '/page/securityLog';
		}
		if(text === 'ORGANIZATION CHART') {
			window.open('./pop/OrganizationEdit.jsp','조직도 편집기','top=50, left=50, width=1500, height=750, resizable=yes');
			return false;
		}
	}

	if(type === 'TRA'){ // System Link
		tab_id = "dccTab";
		text = "Document Control (System Link)";
		jsp_Path = '/page/documentControl';
		DCCorTRA = 'TRA';
		current_folder_id = id;
		current_folder_path = $('#tree').jstree().get_path(node[0], '/');
		$('#dc_current_folder_path').val(current_folder_path);
	}
	
	if(addTab(jsp_Path,text,tab_id, type, null, id)){ // 탭추가
		// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
		let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
		tabList[tabIdx].folder_id = id;
		
		tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
		
		if(type === "COL"){
			let title = node[0].querySelector("a").childNodes[1].nodeValue;
			getCollaboPage(title);
		}
		
		tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
			
	} 	
	
	if(type === "DCC" && dcc_ready==true) {
		folderChange();
	}
	if(type === "COL"){
		let title = node[0].querySelector("a").childNodes[1].nodeValue;
		getCollaboPage(title);
	}
	
	
	
	
}
/* 
 * 작성자		: 권도일
 * 작성일		: 2021-12-22
 * 함수명		: treeMenuActive
 * 설명		: jsTree 상에서 클릭, 더블클릭 이벤트가 동일하므로 공통 이벤트로 분리
 */
function treeMenuActive(event,directAccess){
	
	if(!event && directAccess){
		// 이벤트가 null일때 바로 id로 탭추가
		treeMenuActive2(directAccess);
		return;
	}
	
	// 더블 클릭한 노드의 정보
	var node = $(event.target).closest("li");
	

	// 자식 노드가 없는 최상위 노드의 경우 처리
	// if(node[0].querySelector("a").getAttribute( 'aria-level' ) < 2) return;
	
	var id = node[0].id; //id 가져오기
	var isLeaf = node[0].classList.contains("jstree-leaf"); // leaf노드인지 확인
	var text = node[0].querySelector("a").childNodes[1].nodeValue;
	
	
	let folder_auth_dialog = $(".pop_authSet").dialog()[0].parentNode["style"].display;
	
	// 다이얼로그가 켜져있는 경우에만 작동
	if(folder_auth_dialog == ''){
		let folder_info = folderList.find(i=>i.id == id);
		let f_type = folder_info.type;
		// 폴더변경 했을 때, 폴더권한 메뉴가 작동하는 타입
		let auth_using_type = ['DCC','PBO','REP'];
		// 해당 폴더의 타입이 폴더권한을 적용하는 타입에 해당되는 경우에만 메소드 동작
		if(auth_using_type.includes(f_type)) auth_folderChg(id);
	}
	
	// 세션에 값 저장
	$.ajax({
	    url: 'setSession.do',
	    type: 'POST',
	    data: {
	    	prj_id:prj_id,
	    	folder_id:id
	    },
		async: false,
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	let  _prj_id = getPrjInfo().id;
	let auth = checkFolderAuth(_prj_id,id);

	if(auth.r == 'N'){
		alert("You don't have access to this folder.Please contact DCC");
		return;
	}
	
	// 폴더메뉴의 타입
	var type;
	for (let key in folderList) {
		  const value = folderList[key]
		  if(value.id === parseInt(id)){
			  type = value.type;
			  if(type==='DCC'){
				  current_process_type = value.process_type;
				  current_doc_type = value.doc_type;
				  current_corr_process_type = value.corr_process_type;
				  }
			  $.ajax({
				    url: 'getProcessDocType.do',
				    type: 'POST',
				    data:{
				    	prj_id:getPrjInfo().id,
				    	folder_id:value.id
				    },
				    async:false,
				    success: function onData (data) {
				    	current_folder_docType = data[0];
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
				   
				  current_folder_discipline_code_id = value.discipline_code_id;
				  current_folder_discipline_display = value.discipline_display;
				  current_folder_discipline_code = value.discipline_code;
			  }
		  }
	if(node[0].parentElement.parentElement.id==='tree'){
		if(text!=='PROJECT NOTICE BOARD'){//'PROJECT NOTICE BOARD'를 제외한 루트노드 addTab 안함
			return false;
		}
	}
	
//	if(isOcl){
//		return;
//	}
	
	// 단말노드일 시, 액션
	//if(isLeaf){
		var jsp_Path = "/common/empty"; // include 시켜줄 jsp 경로
		
		var tab_id = "tab_"+id;
		
		// DCC타입의 메뉴는 단일 탭 처리
		if(type === "DCC") {
			if((current_process_type==='TR OUTGOING'||current_process_type==='VTR OUTGOING'||current_process_type==='STR OUTGOING') && current_process_type!=='LETTER' && current_process_type!=='E-MAIL'){
				tab_id = "outgoingTab";
				jsp_Path = '/page/trOutgoing';
				type = 'TROutgoing';

				if(current_process_type==='TR OUTGOING'){
					outProcessType = 'TR';
				}else if(current_process_type==='VTR OUTGOING'){
					outProcessType = 'VTR';
				}else if(current_process_type==='STR OUTGOING'){
					outProcessType = 'STR';
				}

				if(auth.w==='Y'){
					$('#out_incomingreg').css('display','inline-block');
					$('#out_frompreTR').css('display','inline-block');
					$('#out_createTR').css('display','inline-block');
					$('#out_save').css('display','inline-block');
					$('#out_issue').css('display','inline-block');
					$('#out_email').css('display','inline-block');
					$('#out_add_doc').css('display','inline-block');
					$('#out_del_doc').css('display','inline-block');
				}else{
					$('#out_incomingreg').css('display','none');
					$('#out_frompreTR').css('display','none');
					$('#out_createTR').css('display','none');
					$('#out_save').css('display','none');
					$('#out_issue').css('display','none');
					$('#out_email').css('display','none');
					$('#out_add_doc').css('display','none');
					$('#out_del_doc').css('display','none');

			    	$('#out_tr_subject').attr('disabled',true);
			    	$('#out_discipline_code_id').css('pointer-events', 'none');
			    	$('#out_discipline_code_id').css('background', 'none');
			    	$('#out_discipline_code_id').css('background-color', '#FAFAFA');
			    	$('#out_tr_issuepur_code_id').css('pointer-events', 'none');
			    	$('#out_tr_issuepur_code_id').css('background', 'none');
			    	$('#out_tr_issuepur_code_id').css('background-color', '#FAFAFA');
			    	$('#out_send_date').attr('disabled',true);
			    	$('#out_req_date').attr('disabled',true);
			    	$('#out_tr_remark').attr('disabled',true);
				}
				if(auth.d==='Y'){
					$('#out_delete').css('display','inline-block');
					$('#out_issue_cancel').css('display','inline-block');
				}else{
					$('#out_delete').css('display','none');
					$('#out_issue_cancel').css('display','none');
				}
			}else if((current_process_type==='TR INCOMING'||current_process_type==='VTR INCOMING'||current_process_type==='STR INCOMING') && current_process_type!=='LETTER' && current_process_type!=='E-MAIL'){
				tab_id = "incomingTab";
				jsp_Path = '/page/trIncoming';
				type = 'TRIncoming';

				if(current_process_type==='TR INCOMING'){
					inProcessType = 'TR';
				}else if(current_process_type==='VTR INCOMING'){
					inProcessType = 'VTR';
				}else if(current_process_type==='STR INCOMING'){
					inProcessType = 'STR';
				}

				if(auth.w==='Y'){
					$('#in_createTR').css('display','inline-block');
					$('#in_save').css('display','inline-block');
					$('#in_add_doc').css('display','inline-block');
					$('#in_del_doc').css('display','inline-block');
				}else{
					$('#in_createTR').css('display','none');
					$('#in_save').css('display','none');
					$('#in_add_doc').css('display','none');
					$('#in_del_doc').css('display','none');
					
					$('#tr_incoming_attach').css('display','none');
					$('#in_tr_no').attr('disabled',true);
					$('#in_tr_subject').attr('disabled',true);
					$('#in_received_date').attr('disabled',true);
					$('#in_note').attr('disabled',true);
				}
				if(trIn.trInSelectedTable===undefined || trIn.trInSelectedTable.getData().length===0){
					$('#in_del_doc').css('display','none');
				}
				if(auth.d==='Y'){
					$('#in_delete').css('display','inline-block');
				}else{
					$('#in_delete').css('display','none');
				}
			}/*else if(current_process_type==='CORRESPONDENCE'){
				//탭 열지 않음
				tab_id = "corrRoot";
				jsp_Path = '/page/documentControl';
				type = 'corrRoot';
			}*/else{

				tab_id = "dccTab";
				text = "Document Control";
				jsp_Path = '/page/documentControl';
				DCCorTRA = 'DCC';
				//옮기지 말것
				current_folder_id = id;
				
    			let tabIdx = tabList.findIndex(i => i.id == "#dccTab");
    			if(tabIdx>-1) tabList[tabIdx].folder_id = id;
    			

				current_folder_path = $('#tree').jstree().get_path(node[0], '/');
				$('#dc_current_folder_path').val(current_folder_path);
				

			}
		}
		
		if(type === "COL") { // COLLABORATION (전자결재)

			
			if(text === 'COLLABORATION (전자결재)') return;
			
			if(text.includes('결재함')) return;
			
			tab_id = "colTab";
			text = "전자결재";
			jsp_Path = '/page/collaboPage';
			
		}
		
		if(type === "REP") { // REPORT
			if(text === 'ENGINEERING STATUS') {
				tab_id = "prjReportEngTab";
				jsp_Path = '/page/projectReportEngineeringStatus';
			}else if(text === 'PROCUREMENT STATUS') {
				tab_id = "prjReportProTap";
				jsp_Path = '/page/projectReportProcurementStatus';
			}else if(text === 'VENDOR STATUS') {
				tab_id = "prjReprotVenTab";
				jsp_Path = '/page/projectReportVendorStatus';
			}else if(text === 'SITE STATUS') {
				tab_id = "prjReprotSiteTab";
				jsp_Path = '/page/projectReportSiteStatus';
			}else if(text === 'CORRESPONDENCE STATUS') {
				tab_id = "prjReprotCorrTab";
				jsp_Path = '/page/projectReportCorrStatus';
			}
		}
		
		// DCS타입은 Document Search
		if(type === "DCS") {
			tab_id = "docSearchTab";
			text = "Document Search";
			jsp_Path = "/common/docSearch";
		}
		
		if(type === 'PBO'){ // Project notice board
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "PROJECT NOTICE BOARD",
    		    	method: "조회"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			tab_id = "prjNoticeBoardTab";
			jsp_Path = '/page/projectNoticeboard';
		}
		
		if(type === 'PCO'){ // Project configuration
			if(text === 'PROJECT GENERAL') {
				$.ajax({ // 로그인시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "PROJECT GENERAL > 폴더정비",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'projectGENTab';
				jsp_Path = '/page/projectGeneral';
			}
			if(text === 'DICTIONARY') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "DICTIONARY",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				jsp_Path = '/page/dictionarysearch';
			}
			if(text === 'WBS CODE CONTROL') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "WBS CODE CONTROL",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				jsp_Path = '/page/wbsCodeControl';
			}
			if(text === 'DAILY EMAILING SETTING') {
				tab_id = 'emailSettingTab';
				jsp_Path = '/page/emailSetting';
			}
			if(text === '외부 사용자관리') {
				window.open('./pop/outUserMng.jsp','외부 사용자 관리','top=50, left=50, width=1500, height=750, resizable=yes');
				return false;
				/*tab_id = 'userRegisterTab';
				jsp_Path = '/page/userRegister';
				userMngPage = 'DCC';*/
			}
			if(text === 'ETC') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "PROJECT ETC",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'projectETCTab';
				jsp_Path = '/page/projectETC';
			}
			if(text === 'DAILY EMAILING SETTING') {
				tab_id = 'emailSettingTab';
				jsp_Path = '/page/emailSetting';
			}
			if(text === 'E-MAILING STATUS') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "E-MAILING STATUS",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'emailingStatusTab';
				jsp_Path = '/page/emailingStatus';
			}
			if(text === 'DOC DOWNLOAD') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: text,
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'docDownloadTab';
				jsp_Path = '/page/maxRevDocDownload';
			}


			if(text === 'DOCNO TRNO CHANGE') {
				tab_id = 'docTrNoChangeTab';
				jsp_Path = '/page/docTrNoChange';
			}
			
		}
		
		if(type === 'CON'){ // System configuration
			if(text === 'USER REGISTER') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "USER REGISTER",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'userRegisterTab';
				jsp_Path = '/page/userRegister';
				userMngPage = 'ADMIN';
			}
			if(text === 'PROJECT CREATION') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "PROJECT CREATION",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'projectCreationTab';
				jsp_Path = '/page/projectCreation';
			}
			if(text === 'PASSWORD POLICY') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "PASSWORD POLICY",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				jsp_Path = '/page/passwordPolicy';
			}
			if(text === 'NOTICE BOARD') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "NOTICE BOARD",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'noticeBoardTab';
				jsp_Path = '/page/noticeBoard';
			}
			if(text === 'DATA MIGRATION') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "DATA MIGRATION",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				jsp_Path = '/page/dataMigration';
			}
			if(text === 'IP보안') {
				$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
	    			url: 'insertSystemLog.do',
	    		    type: 'POST',
	    		    data: {
	    		    	prj_id: $('#selectPrjId').val(),
	    		    	pgm_nm: "IP보안",
	    		    	method: "조회"
	    		    },
	    		    success: function onData () {
	    		    	
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
				tab_id = 'securityIpTab';
				jsp_Path = '/page/securityIp';
			}
			if(text === '보안로그') {
				tab_id = 'securityLogTab';
				jsp_Path = '/page/securityLog';
			}
			if(text === 'ORGANIZATION CHART') {
				window.open('./pop/OrganizationEdit.jsp','조직도 편집기','top=50, left=50, width=1500, height=750, resizable=yes');
				return false;
			}
			
		}

		if(type === 'TRA'){ // System Link
			tab_id = "dccTab";
			text = "Document Control (System Link)";
			jsp_Path = '/page/documentControl';
			DCCorTRA = 'TRA';
			current_folder_id = id;
			current_folder_path = $('#tree').jstree().get_path(node[0], '/');
			$('#dc_current_folder_path').val(current_folder_path);
		}
//		
		if(addTab(jsp_Path,text,tab_id, type, null, id)){ // 탭추가
			// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
			let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
			tabList[tabIdx].folder_id = id;
			
			
			
			tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
			
			if(type === "COL"){
				let title = node[0].querySelector("a").childNodes[1].nodeValue;
				getCollaboPage(title);
			}
			
			tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
				
		} 	
		
		if(type === "DCC" && dcc_ready==true) {
			folderChange();
		}
		if(type === "COL"){
			let title = node[0].querySelector("a").childNodes[1].nodeValue;
			getCollaboPage(title);
		}
		
}


/* 
 * 작성자		: 소진희
 * 작성일		: 2021-12-17
 * 함수명		: getProcessTypeByFolderPath
 * 설명		: documentControl 각 페이지별 프로세스 타입을 알기위한 함수
 */
function getProcessTypeByFolderPath(){
	$.ajax({
	    url: 'getProcessTypeByFolderPath.do',
	    type: 'POST',
	    data: {
	    	prj_id:prj_id
	    },
		async: false,
	    success: function onData (data) {
	    	processTypeByFolderPath = data[0];
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
/* 
 * 작성자		: 소진희
 * 작성일		: 2021-12-20
 * 함수명		: getFolderDiscipline
 * 설명		: documentControl 각 폴더별 Discipline 정보를 알기위한 함수
 */
function getFolderDiscipline(){
	$.ajax({
	    url: 'getFolderDiscipline.do',
	    type: 'POST',
	    data: {
	    	prj_id:prj_id
	    },
		async: false,
	    success: function onData (data) {
	    	current_FolderDiscipline = data[0];
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

/**
 * 트리에 나타날 폴더 리스트를 db에서 불러온다.
 * data[0]은 순수 폴더리스트
 * data[1]은 세션정보로 폴더별 권한을 매핑시켜놓은 리스트
 * @returns
 */
function getFolderList() {
	getProcessTypeByFolderPath();
	getFolderDiscipline();
	
	let folders = [];
	 $.ajax({
			type: 'POST',
			url: './getFolderList.do',
			data: { 
				prj_id: prj_id
			},
			async: false,
			success: function(data) {
				let tmp_folder_List = data[0];
				let isAdmin = data[1];
				let isDcc = data[2];
				// db에서 불러온 폴더정보를 jsTree에서 그려지는 구조로 변환
				for(let i=0 ; i < tmp_folder_List.length; ++i){
					let tmp_Node = tmp_folder_List[i];
					
					// 읽기 권한이 없으면 목록에 추가x
					
					// if(tmp_Node.auth_r != 'Y') continue;
					
					let process_type = '';
					let doc_type = '';
					let folder_discipline_code_id = '';
					let folder_discipline_display = '';
					let folder_discipline_code = '';
					let useDRN = tmp_Node.useDRN;
					// Corr타입이면 무조건 N
					if(tmp_Node.doc_type==='Corr') useDRN = 'N';
					
					if(tmp_Node.folder_type==='DCC'){
						for(j=0;j<processTypeByFolderPath.length;j++){
							let process_folder_path = processTypeByFolderPath[j].folder_path;
							if(tmp_Node.folder_path.indexOf(process_folder_path+'>')!=-1){
								process_type = processTypeByFolderPath[j].process_type;
								doc_type = processTypeByFolderPath[j].doc_type;
							}
							if(tmp_Node.folder_path === process_folder_path){
								process_type = processTypeByFolderPath[j].process_type;
								doc_type = processTypeByFolderPath[j].doc_type;
							}
						}
						for(j=0;j<current_FolderDiscipline.length;j++){
							let discipline_folder_path = current_FolderDiscipline[j].folder_path;
							if(tmp_Node.folder_path.indexOf(discipline_folder_path+'>')!=-1){
								folder_discipline_code_id = current_FolderDiscipline[j].discip_code_id;
								folder_discipline_display = current_FolderDiscipline[j].discip_display;
								folder_discipline_code = current_FolderDiscipline[j].discip_code;
							}
							if(tmp_Node.folder_path === discipline_folder_path){
								folder_discipline_code_id = current_FolderDiscipline[j].discip_code_id;
								folder_discipline_display = current_FolderDiscipline[j].discip_display;
								folder_discipline_code = current_FolderDiscipline[j].discip_code;
							}
						}						
					}
					/*
					if(tmp_Node.folder_type === 'CON'){
						if(isAdmin != 'Y') continue;
					}
					
					if( tmp_Node.folder_type === 'PCO'){
						if(isDcc != 'Y') {
							if(isAdmin != 'Y') continue;
						}
					}
					
					
					if( tmp_Node.folder_type === 'TRA'){
						if(isDcc != 'Y') {
							if(isAdmin != 'Y') continue;
						}
					}
					*/

					let	input_Folder = { 
							"id" : tmp_Node.folder_id, 
							"parent" : tmp_Node.parent_folder_id, 
							'text' : tmp_Node.folder_nm, 
							'type' : tmp_Node.folder_type ,
							'folder_path':tmp_Node.folder_path,
							'folder_path_str':tmp_Node.folder_path_str,
							'process_type':process_type, 
							'doc_type':doc_type,
							'discipline_code_id': folder_discipline_code_id, 
							'discipline_display': folder_discipline_display, 
							'discipline_code' : folder_discipline_code,
							'auth_r':tmp_Node.auth_r,
							'auth_w':tmp_Node.auth_w,
							'auth_d':tmp_Node.auth_d,
							'useDRN':tmp_Node.useDRN
						};
					
					if(tmp_Node.folder_id === tmp_Node.parent_folder_id){ // 최상위 루트
						input_Folder.parent = "#";
						input_Folder.icon = "resource/images/pinwheel.png";
						input_Folder.state =  {'opened' : true,'selected' : false};
					}
					folders.push(input_Folder);
				}
			}
		});
	 folders = getDRNsCount(folders);
	 
	 // 폴더아이디 중복제거
	 folders = folders.filter((item, i) => {
		  return (
				 folders.findIndex((item2, j) => {
		      return item.id === item2.id;
		    }) === i
		  );
		});
	 
	 
	 return folders;
}

/**
 * 메뉴 리스트에 DRN 카운트를 넣어준다.
 * @param folders
 * @returns
 */
function getDRNsCount(folders){
	
	// collaboration 폴더가 있는 경우에만 해당 함수 처리
	let tmp = folders.find(i=>i.text.includes("기안함"));
	if(!tmp) return folders;

	$.ajax({
		url: 'getCollaboCount.do',
	    type: 'POST',
	    async: false,
	    data: {
	    	prj_id: getPrjInfo().id
	    },
	    success: function onData (data) {
	    	for(var key in data.model){
	    		// 전자결재 메뉴의 idx
	    		const idx = folders.findIndex(it => it.text.includes(key));
	    		if(data.model[key]>0){ // 1건 이상일 경우
	    			folders[idx].text = key;
	    			folders[idx].text = folders[idx].text + " (" + data.model[key] +")";
	    		}
	    		
	    		
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});

	
	return folders ; 
}


// 헤더 프로젝트 리스트 갱신
function refreshPrjList(data){
	

	// selectBox 영역 그려줌
	var html = '<table class="header_prj_select_table"><tr><th>Project No</th>'
		+ '<th>Project Name</th><th>Project Full Name</th></tr>';
	
	for(var i=0;i<data[0].length;i++){
		html += '<tr class="prj_select" id="prj_id_is_'+data[0][i].prj_id+'">'
		+ '<td>'+data[0][i].prj_no+'</td>'
		+ '<td class="prj_nm">'+data[0][i].prj_nm+'</td>'
		+ '<td>'+data[0][i].prj_full_nm+'</td>'
		+ '</tr>';
	}
	$('#header_multi_box').html(html);
	
	// selected Project 이름 갱신
	var selectedId = $("#selectPrjId").val();
	
	for(var i=0;i<$(".prj_select").length;i++){
		var id = $(".prj_select")[i].id.replace("prj_id_is_","");
		if(selectedId === id){
			$("#selected_prj").text(data[0][i].prj_nm);
			break;
		}			
	}
	
	headerPrjClick();
}

/**
 * 헤더에서 현재 선택된 프로젝트의 정보를 가져옴
 * @param void 
 * @returns prj_info
 */
function getPrjInfo(prj_id){
	
	
	let selectedId = $("#selectPrjId").val();
	if(prj_id) selectedId = prj_id;
	

	
	let tmp = {};
	
	$.ajax({
	    url: 'getPrjInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:selectedId,
	    },
	    async:false,
	    success: function onData (data) {
	    	
	    	tmp.id = data[0].prj_id;
	    	tmp.no = data[0].prj_no;
	    	tmp.nm = data[0].prj_nm;
	    	tmp.full_nm = data[0].prj_full_nm;
	    		    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	return tmp;
	/*
	for(let i=0;i<$(".prj_select").length;i++){
		// i번째 row의 프로젝트 아이디	
		let id = $(".prj_select")[i].id.replace("prj_id_is_","");
		let tmp = {};
		// 프로젝트 리스트에서 row별 id를 가져와서 현재 선택된 프로젝트의 id와 같은지 체크
		if(selectedId === id){
			
			
			let no = $(".prj_select")[i].children[0].innerText;
			let nm = $(".prj_select")[i].children[1].innerText;
			let full_nm = $(".prj_select")[i].children[2].innerText;
			
			tmp.id = id;
			tmp.no = no;
			tmp.nm = nm;
			tmp.full_nm = full_nm;
			
			return tmp;
			break;
		}
	}
	*/
	
	
}

//
//function getPrjInfo2(prj_id){
//	
//	
//	let tmp = {};
//	
//	$.ajax({
//	    url: 'getPrjInfo.do',
//	    type: 'POST',
//	    data:{
//	    	prj_id:selectedId,
//	    },
//	    async:false,
//	    success: function onData (data) {
//	    	
//	    	tmp.id = data[0].prj_id;
//	    	tmp.no = data[0].prj_no;
//	    	tmp.nm = data[0].prj_nm;
//	    	tmp.full_nm = data[0].prj_full_nm;
//	    		    	
//	    },
//	    error: function onError (error) {
//	        console.error(error);
//	    }
//	});
//	
//	return tmp;
//	
//	
//}

/**
 * 기본 권한이 부여가 안 된 경우, 부여해줄 수 있는 함수
 * @returns
 */
function defaultAuthUpdate(){
	 $.ajax({
			type: 'POST',
			url: 'defaultAuthSet.do',
			data:{
				prj_id : getPrjInfo().id
			},
			success: function(data) {	
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}




/**
 * 오늘 날짜,시,분,초로 문자열을 생성
 * @returns
 */
function genDateStr(){
	let today = new Date();  
	let year = today.getFullYear(); 
	let month = today.getMonth() + 1;
	let date = today.getDate();
	let hour = today.getHours();
	let min = today.getMinutes();
	let sec = today.getSeconds();
	
	let dateString = year + 
					(month + "").padStart(2,0) +
					(date + "").padStart(2,0) +
					(hour + "").padStart(2,0) +
					(min + "").padStart(2,0) +
					(sec + "").padStart(2,0);
	
	return dateString;
}




/**
 * 로딩 창을 보여준다.
 * @param gif
 * @returns
 */
function loadingWithMask() {
    //화면의 높이와 너비를 구합니다.
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    //화면에 출력할 마스크를 설정해줍니다.
    var mask       ="<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
      
    loadingImg +=" <img src='/resource/images/Spinner.gif' style='position: absolute; display: block; margin: 0px auto;'/>";
 
    //화면에 레이어 추가
    $('body')
        .append(mask)
 
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
  
    //마스크 표시
    $('#mask').show();
  
    //로딩중 이미지 표시
    $('#loadingImg').append(loadingImg);
    $('#loadingImg').show();
}

function closeLoadingWithMask() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').empty(); 
}

function getCollaboPage(title){};

function checkFolderAuth(prj_id,folder_id){
	
	let auth = {};
	
		$.ajax({
    	    url: 'checkFolderAuth.do',
    	    type: 'POST',
    	    async:false,
    	    data:{
    	    	prj_id: prj_id,
    	    	folder_id : folder_id
    	    },
    	    success: function onData (data) {
    	    	
    	    	
    	    	if(data == "noSession"){
    	    		alert("세션이 끊겼습니다. 다시 로그인해 주세요.");
    	    		$('#alertDialogOk').click(function(){
    	    			window.location.href = '/login.do';
    	    		});
    	    		return;
    	    	}
    	    	
    	    	
    	    	if(data == "<script>alert('There is no Read Permission');</script>"){
    	    		auth.r = "N";
    	    		auth.w = "N";
    	    		auth.d = "N";
    	    		auth.isAdmin = "N";
        	    	auth.isDcc = "N";
        	    	
    	    		return auth;
    	    	}
    	    	
    	    	if(!data.model){
    	    		auth.r = "N";
    	    		auth.w = "N";
    	    		auth.d = "N";
    	    		auth.isAdmin = "N";
        	    	auth.isDcc = "N";
    	    		
    	    		console.log("not mapping",auth);
    	    		return auth;
    	    	}
    	    	
    	    	auth.r = data.model.auth_check_r;
    	    	auth.w = data.model.auth_check_w;
    	    	auth.d = data.model.auth_check_d;
    	    	
    	    	auth.isAdmin = data.model.isAdmin;
    	    	auth.isDcc = data.model.isDcc;
    	    	
    	    	
    	    },
    	    error: function onError (error) {
    	        console.error(error);
    	    }
    	});
		
	return auth;
		
}


function drnPopUP(pageMode,docData,discipObj,folder_id,pageData,approval,_prj_id){
	// @params:
	// pageMode,docData,discipObj,folder_id,pageData,approval
	
	
	var url = '/drnPopup.do';
	let drnForm = document.createElement("form");
	drnForm.id = "drnForm";
    var target = 'DRN';
    window.open(url, target, "width=1200,height=950,resizable=no,toolbar=yes,menubar=yes,location=yes");
    //window.open("", target, "width=1200,height=950,resizable=no,toolbar=yes,menubar=yes,location=yes");

    $(drnForm).attr('action', url);
    $(drnForm).attr('target', target); // window.open 타이틀과 매칭 되어야함
    $(drnForm).attr('method', 'POST');
    //$(drnForm).attr('enctype',"multipart/form-data")

    let prj_info = getPrjInfo();
    
    if(_prj_id)
    	prj_info = getPrjInfo(_prj_id);
    	
    if(pageMode == 'read') pageMode = -1;
    
    drnForm.append(genInputTag("prj_id",prj_info,true)); 
    drnForm.append(genInputTag("pageMode",pageMode,false)); 
    drnForm.append(genInputTag("docData",docData,true)); 
    drnForm.append(genInputTag("discipObj",discipObj,true)); 
    drnForm.append(genInputTag("folder_id",folder_id,false)); 
    drnForm.append(genInputTag("pageData",pageData,true)); 
    drnForm.append(genInputTag("approval",approval,true)); 
    
    
    /*
    drnForm.append(String.format(formStr,"pageMode",pageMode)); 
    drnForm.append(String.format(formStr,"docData",JSON.stringify(docData))); 
    drnForm.append(String.format(formStr,"discipObj",JSON.stringify(discipObj))); 
    drnForm.append(String.format(formStr,"folder_id",folder_id));
    drnForm.append(String.format(formStr,"pageData",JSON.stringify(pageData)));
    drnForm.append(String.format(formStr,"approval",JSON.stringify(approval)));
    */
    document.body.appendChild(drnForm);
    
    
    drnForm.submit();
    $("#drnForm").remove();
    
    
}


function getDiscipObj(folder_id){
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

// alert창 함수 오버라이딩
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
	return true;
}

function genInputTag(name,val,json_use){
	
	let item = document.createElement("input");
    item.type = 'hidden';
    item.name = name;
    item.value = !json_use ? val : JSON.stringify(val);
    
    return item;
	
}
