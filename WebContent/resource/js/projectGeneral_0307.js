/* 
 * 작성자		: 권도일
 * 작성일		: 2021-11-22
 * 메소드명	: projectGeneral.js
 * 설명		: Project General 관련 스크립트
 */

var isEdited = false;

/**
 * 수정된 값이 있는 경우,
 * save하지 않으면 다른 필드로 넘어갈 수 없도록 조치
 * @param callback
 * @returns
 */

function saveCheck02(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			//isEdited = false;
			
			var prjInfo = getPrjInfo();
			initStep02(prjInfo);
			
			pg02_clear();
	        return callback();
	    }
	    else{
	        return false;
	    }
	}
	return true;
}

function saveCheck04(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			isEdited = false;
			let i = $("#pg04_codeType").find(":selected").index();
			getPg04Grid(i);
	        return callback();
	    }
	    else{
	        return false;
	    }
	}
	return true;
}

function saveCheck07(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			isEdited = false;
			
			pg07_Add();
			
	        return callback();
	    }
	    else{
	        return false;
	    }
	}
	return true;
}

function pgen_step01(){
	//$(".proj_general").removeAttr('id','general04');
	$(".proj_general").attr('id','general01');
}

function pgen_step08(){
	$(".proj_general").attr('id','general08');
}

$(document).on("click", "#general08 ul li a", function () {
	
    var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});
function pgen_step09(){
	
}

function pgen_step10(){
	$(".proj_general").attr('id','general10');
}

function pgen_step11(){
	 $.ajax({
			type: 'POST',
			url: 'CoverTemplateExists.do',         
	        data:{
	        	prj_id: getPrjInfo().id,
	        	prj_nm: getPrjInfo().nm
	        },
			success: function(data) {
				if(data[0]==='파일 존재'){
					let rfile_nm = getPrjInfo().nm+'_p_tr.xls';
					$('#tr_cover_uploadFileInfo').html('<a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadTRCoverTemplateFile.do?prj_id='+encodeURI(getPrjInfo().id)+'&rfile_nm='+encodeURI(rfile_nm)+'">'+rfile_nm+'</a>');
				}else{
					$('#tr_cover_uploadFileInfo').html('');
				}
				if(data[1]==='파일 존재'){
					let rfile_nm = getPrjInfo().nm+'_p_vtr.xls';
					$('#vtr_cover_uploadFileInfo').html('<a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadTRCoverTemplateFile.do?prj_id='+encodeURI(getPrjInfo().id)+'&rfile_nm='+encodeURI(rfile_nm)+'">'+rfile_nm+'</a>');
				}else{
					$('#vtr_cover_uploadFileInfo').html('');
				}
				if(data[2]==='파일 존재'){
					let rfile_nm = getPrjInfo().nm+'_p_str.xls';
					$('#str_cover_uploadFileInfo').html('<a style="text-decoration:underline;color:blue;" href="'+server_domain+'/downloadTRCoverTemplateFile.do?prj_id='+encodeURI(getPrjInfo().id)+'&rfile_nm='+encodeURI(rfile_nm)+'">'+rfile_nm+'</a>');
				}else{
					$('#str_cover_uploadFileInfo').html('');
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
var PG = {};
var contextpath = $('#contextpath').val();
var prj_id = $('#selectPrjId').val();

$(function(){
	$(document).on("click", "#projectGeneral01ALink", function () {
		//getPGTree();
		folderRefresh2();
	});
	$(document).on("click", "#projectGeneral02ALink", function () {
		pgen_step02();
	});
	$(document).on("click", "#projectGeneral03ALink", function () {
		pgen_step03();
	});
	$(document).on("click", "#projectGeneral04ALink", function () {
		pgen_step04();
	});
	$(document).on("click", "#projectGeneral05ALink", function () {
		pgen_step05();
	});
	$(document).on("click", "#projectGeneral06ALink", function () {
		getSTEPPrjCodeSettingsList();
	});
	$(document).on("click", "#projectGeneral07ALink", function () {
		pgen_step07();
	});
	$(document).on("click", "#projectGeneral08ALink", function () {
		$('#memeberSettingPrjId').val(selectPrjId);
		window.open('./pop/projectGeneral08.jsp','PROJECT USER/GROUP 설정','top=50, left=50, width=1700, height=1500, resizable=yes');
	});
	$(document).on("click", "#projectGeneral09ALink", function () {
		//getTreeType09();
		var tree = $("#folder_allow").jstree(true);
		var data1 = getFolderList();
		//data1 = dccFolder(data1);
		tree.settings.core.data = data1;
		tree.refresh();
	});
	$(document).on("click", "#projectGeneral10ALink", function () {
		pg10load();
	});
	$(document).on("click", "#projectGeneral11ALink", function () {
		pgen_step11();
	});
	/*let pgenLeftMenus = $(".projectGen_Link");
	let methodList = [];
	for(let i=0;i<pgenLeftMenus.length;i++){
		methodList[i] = eval("pgen_step" + ((i+1)<10? "0" + (i+1) : (i+1)));
	}*/
	/*
	for(let i=0;i<pgenLeftMenus.length;i++){
		pgenLeftMenus[i].onclick = function(){
			methodList[i]();
			return false;
		};
	}
	*/

	//TR COVER 파일첨부
    var fileTarget = $('#tr_cover_filename');
    fileTarget.on('change', function () {
    	let tr_ext = '';
    	let filename = '';
        if (window.FileReader) {
            filename = $(this)[0].files[0].name;
        	tr_ext = $(this)[0].files[0].name.split('.').pop().toLowerCase();
        } else {
            filename = $(this).val().split('/').pop().split('\\').pop();
        }
        $(this).siblings('.upload-name').val(filename);
        PG.tr_file_type = tr_ext;
    });
	//VTR COVER 파일첨부
    var fileTarget = $('#vtr_cover_filename');
    fileTarget.on('change', function () {
    	let vtr_ext = '';
    	let filename = '';
        if (window.FileReader) {
            filename = $(this)[0].files[0].name;
        	vtr_ext = $(this)[0].files[0].name.split('.').pop().toLowerCase();
        } else {
            filename = $(this).val().split('/').pop().split('\\').pop();
        }
        $(this).siblings('.upload-name').val(filename);
        PG.vtr_file_type = vtr_ext;
    });
	//STR COVER 파일첨부
    var fileTarget = $('#str_cover_filename');
    fileTarget.on('change', function () {
    	let str_ext = '';
    	let filename = '';
        if (window.FileReader) {
            filename = $(this)[0].files[0].name;
        	str_ext = $(this)[0].files[0].name.split('.').pop().toLowerCase();
        } else {
            filename = $(this).val().split('/').pop().split('\\').pop();
        }
        $(this).siblings('.upload-name').val(filename);
        PG.str_file_type = str_ext;
    });

    $(".pop_deletePg04Confirm").dialog({
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
    $(".pop_corrSetting").dialog({
        autoOpen: false,
        modal:true,
        maxWidth: 400,
        width: "auto",
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
    $(document).on("click", "#corrSetting", function() {
        $(".pop_corrSetting").dialog("open");
        return false;
    });

    $(".pop_outgoingNum").dialog({
    	modal:true,
        autoOpen: false,
        maxWidth: 400,
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
    $(document).on("click", "#outgoingNum", function() {
        $(".pop_outgoingNum").dialog("open");
        return false;
    });
    
	getPGTree();
});

function trCoverUpload(){
	let form = $('#trCoverForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',getPrjInfo().id);
    formData.set('prj_nm',getPrjInfo().nm);
    formData.set('file_type',PG.tr_file_type);
    formData.set('doc_type','TR');
    
    if($('#tr_cover_attachfilename').val()==='파일선택' || $('#tr_cover_attachfilename').val()===''){
    	alert('파일을 선택해주세요.');
    }else{
    	$.ajax({
    		url: 'trCoverSaveChkFile.do',
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
							url: 'TRCoverUpload.do',         
					        data:formData,          
					        processData: false,    
					        contentType: false,      
					        cache: false,           
					        timeout: 600000,  
							success: function(data) {
								alert('Save Successfully');
								pgen_step11();
							},
						    error: function onError (error) {
						        console.error(error);
								alert('Save failed!');
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
	 }
}
function vtrCoverUpload(){
	let form = $('#vtrCoverForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',getPrjInfo().id);
    formData.set('prj_nm',getPrjInfo().nm);
    formData.set('file_type',PG.vtr_file_type);
    formData.set('doc_type','VTR');

    if($('#vtr_cover_attachfilename').val()==='파일선택' || $('#vtr_cover_attachfilename').val()===''){
    	alert('파일을 선택해주세요.');
    }else{
    	$.ajax({
    		url: 'trCoverSaveChkFile.do',
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
							url: 'TRCoverUpload.do',         
					        data:formData,          
					        processData: false,    
					        contentType: false,      
					        cache: false,           
					        timeout: 600000,  
							success: function(data) {
								alert('Save Successfully');
								pgen_step11();
							},
						    error: function onError (error) {
						        console.error(error);
								alert('Save failed!');
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
    }
}
function strCoverUpload(){
	let form = $('#strCoverForm')[0];
    let formData = new FormData(form);
    formData.set('prj_id',getPrjInfo().id);
    formData.set('prj_nm',getPrjInfo().nm);
    formData.set('file_type',PG.str_file_type);
    formData.set('doc_type','STR');

    if($('#str_cover_attachfilename').val()==='파일선택' || $('#str_cover_attachfilename').val()===''){
    	alert('파일을 선택해주세요.');
    }else{
    	$.ajax({
    		url: 'trCoverSaveChkFile.do',
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
							url: 'TRCoverUpload.do',         
					        data:formData,          
					        processData: false,    
					        contentType: false,      
					        cache: false,           
					        timeout: 600000,  
							success: function(data) {
								alert('Save Successfully');
								pgen_step11();
							},
						    error: function onError (error) {
						        console.error(error);
								alert('Save failed!');
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
    }
}

function dccFolder(folderList){
	var folder = [];
	for(let i=0;i<folderList.length;i++)
		if(folderList[i].type === "DCC")
			folder.push(folderList[i]);	
	
	return folder;
	
}
function folderRefresh(){
	var tree = $("#pg01_tree").jstree(true);
	var data1 = getFolderList();
	data1 = dccFolder(data1);
	tree.settings.core.data = data1;
	tree.refresh();
	
	//폴더 정비 아닌 전체 트리 리프레시
	var data2 = getFolderList();
	$("#tree").jstree(true).settings.core.data = data2;
	$("#tree").jstree(true).refresh();
}
//폴더 정비쪽 트리만 리프레시
function folderRefresh2(){
	var tree = $("#pg01_tree").jstree(true);
	var data1 = getFolderList();
	data1 = dccFolder(data1);
	tree.settings.core.data = data1;
	tree.refresh();
}
var newFlag = false;
function getPGTree(){
	var folderList = getFolderList();
	folderList = dccFolder(folderList);

	var pasteDisable = 1;
	var moveFolderId = 0;
	function jstreeContextMenu(node) {
		var tree = $("#pg01_tree").jstree(true);

		let auth = checkFolderAuth(selectPrjId,node.id);
		
	    // 모든 곳에 적용되는 컨텍스트메뉴 items
	    var items = {
	        thisItem: {
	            "label": 'Folder : '+node.text,
	            "separator_before" : false,
	            "separator_after" : true,
	            "action": function(obj) {}
	        },
	        createItem: {
	            "label": "New",
	            "action": function(data) {
	            	newFlag = true;
	            	var inst = $.jstree.reference(data.reference),
                    obj = inst.get_node(data.reference);
	            	inst.create_node(obj, {}, "last", function (new_node) {
	            		new_node.data = {file: true};
	            		setTimeout(function () { inst.edit(new_node); },0);
	                   $('#pg01_tree').on('rename_node.jstree', function (e, data) {
	                    	if(data.node.parent==new_node.parent && data.text==new_node.text && newFlag===true){
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
		            			    	data1 = dccFolder(data1);
		            			    	tree.settings.core.data = data1;
		            			    	tree.refresh();
		            			    	
		            			    	//폴더별 정비 아닌 전체 트리 리프레시
		            					var data2 = getFolderList();
		            					$("#tree").jstree(true).settings.core.data = data2;
		            					$("#tree").jstree(true).refresh();
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
	            "label": "Rename",
	            "action": function(obj) { 
	            	newFlag = false;
	            	tree.edit(node);
	            	var folder_nm = node.text;
	            	$('#pg01_tree').on('rename_node.jstree', function (e, data) {
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
	            			    	
	            			    	//폴더별 권한 설정이 아닌 전체 트리 리프레시
	            					var data2 = getFolderList();
	            					$("#tree").jstree(true).settings.core.data = data2;
	            					$("#tree").jstree(true).refresh();
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
	        			    	if(data[0]==='삭제 가능'){
	        	                	tree.delete_node(node.id);
	            			    	//폴더별 권한 설정이 아닌 전체 트리 리프레시
	            					var data2 = getFolderList();
	            					$("#tree").jstree(true).settings.core.data = data2;
	            					$("#tree").jstree(true).refresh();
		    		            	$(".pop_deletefolder").dialog("close");
	        			    	}else{
	        			    		alert(data[0]);
		    		            	$(".pop_deletefolder").dialog("close");
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
			        			    	data = dccFolder(data);
			        			    	tree.settings.core.data = data;
			        			    	tree.refresh();
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
	        indexRegisterItem: {
	            "label": "Index Register",
	            "action": function(obj) {  }
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
		    	// index, 권한관리 disabled 처리
	    		items.authControlItem._disabled =true;
	    		items.indexRegisterItem._disabled = true;
		    }else if (node.original.type=='DCC') {
		    	if(pasteDisable==1){
		    		items.pasteItem._disabled = true;
			    	// index, 권한관리 disabled 처리
		    		items.authControlItem._disabled =true;
		    		items.indexRegisterItem._disabled = true;
		    	}else{
			    	items.pasteItem._disabled = false;
		    	}	    	
		    }else if (node.original.type=='TRA') {
		    	if(pasteDisable==1){
		    		items.pasteItem._disabled = true;
			    	// index, 권한관리 disabled 처리
		    		items.authControlItem._disabled =true;
		    		items.indexRegisterItem._disabled = true;
		    	}else{
			    	items.pasteItem._disabled = false;
		    	}
		    }else{
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
	    		items.pasteItem._disabled= true;
	    		items.authControlItem._disabled =true;
	    		items.indexRegisterItem._disabled = true;
	    	} else{
	    		items.createItem._disabled= true;
	    		items.renameItem._disabled= true;
	    		items.cutItem._disabled= true;
	    		items.pasteItem._disabled= true;
	    		items.authControlItem._disabled =true;
	    		items.indexRegisterItem._disabled = true;
	    	}
	    	
	    	if(auth.d == 'N'){
	    		items.deleteItem._disabled= true;   		
	    	}
	    }
	    
	    return items;
	}
	
	var tree = $('#pg01_tree').jstree({
	     "core" : {
	    	 'data' : folderList,
	         "check_callback" : true
	       },
	       'sort' : function(a, b) {
	           a = this.get_node(a);
	           b = this.get_node(b);
	           if(a.icon != 'resource/images/pinwheel.png' && a.original.type == 'DCC'){
	        	   return (a.text > b.text) ? 1 : -1;		           
	           }	           
	       },	       
	      "plugins" : [ "contextmenu" ,"sort"],
	      'contextmenu' : {
	    	  "items" : jstreeContextMenu
	       }
	    }).on('dblclick.jstree', function(event) { // 더블클릭 이벤트 바인딩
			
		// 더블 클릭한 노드의 정보
		var node = $(event.target).closest("li");

		// 자식 노드가 없는 최상위 노드의 경우 처리
		// if(node[0].querySelector("a").getAttribute( 'aria-level' ) < 2) return;
		
		var id = node[0].id; //id 가져오기
		var isLeaf = node[0].classList.contains("jstree-leaf"); // leaf노드인지 확인
		var text = node[0].querySelector("a").childNodes[1].nodeValue;
		
		// 폴더메뉴의 타입
		var type;
		for (let key in folderList) {
			  const value = folderList[key]
			  if(value.id === parseInt(id)) type = value.type;
			}
		
		// 단말노드일 시, 액션
		if(isLeaf){
			var jsp_Path = "/common/home"; // include 시켜줄 jsp 경로
			
			var tab_id = "tab_"+id;
			
			// DCC타입의 메뉴는 단일 탭 처리
			if(type === "DCC") {
				tab_id = "dccTab";
				text = "Document Control";
			}
			
			// DCS타입은 Document Search
			if(type === "DCS") {
				tab_id = "docSearchTab";
				text = "Document Search";
				jsp_Path = "/common/docSearch";
			}
			
			if(text === 'PROJECT CREATION'){
				jsp_Path = '/page/projectCreation';
			}else if(text === 'PROJECT GENERAL'){
				jsp_Path = '/page/projectGeneral';
			}
			
			if(addTab(jsp_Path,text,tab_id,type)){ // 탭추가
				// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
				tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
				tabOn("#"+tab_id,type); // 새로 추가된 탭 활성화				
			} 	
			
			
		}
	});
	
	$('#pg01_tree').jstree(true).refresh();
	
}




/* 
 * 작성자		: 권도일
 * 작성일		: 2021-11-26
 * 메소드명	: pgen_step02(),
 * 			  getPrjInfo()
 * 
 * 설명		: Project General 동작 관련 스크립트
 */

// code 관련 공통동작 메소드
//현재 그리드에서 code id 값의 최대값을 찾는다. 새로운 row를 추가할 때 code_id 값을 부여하기 위함
function code_getMaxId(grid){
	let max = 0;
	let gridData = grid.getData();
	
	for(let i=0 ; i < gridData.length ; i++)
		if(Number(gridData[i].set_code_id) > max) 
			max = Number(gridData[i].set_code_id);
	
	return String(max).padStart(5,"0");
}


function code_getMaxOrder(grid){
	
	let max = 0;
	let gridData = grid.getData();
	
	for(let i=0 ; i < gridData.length ; i++)
		if(gridData[i].set_order > max) max = gridData[i].set_order;
	
	return max;	
}



/**
 * Project General 페이지 내에서,
 * 두번째 스텝 : 프로세스 type 정의, 폴더 match, drn사용여부 설정
 * @param void 
 * @returns prj_info
 */
// pg02_에서 사용될 전역변수
var pg02 = {isFirst:true};
var pg02_Tree;
var pg02_Path;
var pg02_selectdData;

function pgen_step02(){	
	
	if(!pg02.isFirst) return;
	
	pg02.isFirst = false;
	isEdited = false;
	
	// 헤더에서 프로젝트 정보를 가져와서 화면에 표시될 초기값 반영
	var prjInfo = getPrjInfo();
	
	$(".proj_general").attr('id','general02');
	
	// onclick속성 부여
	$("#pg02_applyBtn").click(function(){ pg02_apply(prjInfo) });
	
	$("#pg02_btnPath").click(function(){ 
		pg02_openDialog(prjInfo);
		//dialog 오픈할 때 마다 tree 리프레쉬
		let folderPath = getFolderList();
		folderPath = processFolder(folderPath);
		let tree = $('#pg02_tree').jstree(true);
		tree.settings.core.data = folderPath;
		tree.refresh();
	});
	
	$("#pg02_addBtn").click(function(){ pg02_add(prjInfo) });
	$("#pg02_saveBtn").click(function(){ 
		pg02_save(prjInfo);

		//전체 트리 리프레시 (useDRN 값 적용을 위해)
		var data2 = getFolderList();
		$("#tree").jstree(true).settings.core.data = data2;
		$("#tree").jstree(true).refresh();
	});
	$("#pg02_delBtn").click(function(){ pg02_del(prjInfo) });
	
	$("#pg02_pjCode").change(function(){
		isEdited = true;
	});
	
	$("#pg02_pjNm").change(function(){
		isEdited = true;
	});
	
	$("#pg02_pjFullNm").change(function(){
		isEdited = true;
	});
	
	$("#pg02_setPType").change(function(){
		isEdited = true;
	});
	$("#pg02_setDescript").change(function(){
		isEdited = true;
	});
	$("#pg02_setDRN").change(function(){
		isEdited = true;
	});
	$("#pg02_setDocType").change(function(){
		isEdited = true;
	});
	
	$("#review_conclusion").change(function(){
		isEdited = true;
	});
	
	
	// 초기화면 값 셋팅
	initStep02(prjInfo);
	
	
	// 그리드 그려주기
	pg02.grid = new Tabulator("#pg02_Grid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		columns: [
			{	// 체크박스
				// formatter:"rowSelection",
				// 커스텀 포매터
				formatter: function(cell, formatterParams, onRendered) {
			        const data = cell.getRow().getData();
			        // class_lvl이 system이 아닌 경우(= manual)만 체크박스 추가
			        if (data['class_lvl'] != 'SYSTEM') {
			          var checkbox = document.createElement("input");
			          checkbox.name = 'pg02_gridChk'
			          checkbox.type = 'checkbox';
			          checkbox.value = data.process_id;
			          return checkbox;
			        }
			        return null;
				},
				hozAlign:"center", 
				headerSort:false,
				width:10,
				cellClick:function(e, cell){
			        cell.getRow().toggleSelect();
			      }
			},			
			{
		        title: "CLASS",
		        field: "class_lvl",
		        hozAlign:"left",
		        widthGrow:1,
		        headerSort:false
		    },
		    {
		        title: "PROCESS TYPE",
		        field: "process_type",
		        hozAlign:"left",
		        widthGrow:2,
		        headerSort:false
		    },
		    {
		        title: "DESCRIPTION",
		        field: "process_desc",
		        hozAlign:"left",
		        widthGrow:5,
		        headerSort:false
		    },
		    {
		        title: "DRN",
		        field: "drn_use_yn",
		        headerSort:false,
		        width: 50
		    },
		    {
		    	title: "DOC TYPE",
		    	field: "doc_type",
		    	headerSort:false,
		    	visible:false
		    },
		    {
		        title: "PATH",
		        field: "process_folder_path_string",
		        hozAlign:"left",
		        widthGrow:5,
		        tooltip:true,
		        headerSort:false
		    }
		],
	});
	
	
	pg02.grid.on("tableBuilt",function(){
		$.ajax({
		    url: 'pg02_getGrid.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id
		    },
		    success: function onData (data) {
		    	pg02_getGrid(data);
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	
	$(document).keydown(function(event) {
	    if ( event.keyCode == 27 || event.which == 27 ) {
	    	pg02_clear();
	    }
	});
	
	
	
	pg02.processMap = {
			"TRANSMITTAL" : "DCI",
			"VENDOR DOC. TRANSMITAL" : "VDCI",
			"SITE DOC. TRANSMITTAL" : "SDCI",
			"CORRESPONDENCE" : "Corr",
			"TR OUTGOING" : "General",
			"TR INCOMING" : "General",
			"VTR OUTGOING" : "General",
			"VTR INCOMING" : "General",
			"STR OUTGOING" : "General",
			"ENGINEERING (ENG)" : "DCI",
			"VENDOR DOC. (VDCI)" : "VDCI",
			"PROCUREMENT (POC)" : "PCI",
			"BULK MITERIAL (BMC)" : "BMC",
			"SITE DOC. (SDCI)" : "SDCI",
			"LETTER" : "Corr",
			"E-MAIL" : "Corr",
	};
	
	pg02.systemClassOpt =[
			"TRANSMITTAL", 
			"VENDOR DOC. TRANSMITAL",
			"SITE DOC. TRANSMITTAL",
			"CORRESPONDENCE",
			"TR OUTGOING", 
			"TR INCOMING",
			"VTR OUTGOING", 
			"VTR INCOMING",
			"STR OUTGOING"  
			];
	
	pg02.manualClassOpt = [
			"ENGINEERING (ENG)",
			"VENDOR DOC. (VDCI)", 
			"PROCUREMENT (POC)", 
			"BULK MITERIAL (BMC)",
			"SITE DOC. (SDCI)",
			"LETTER",
			"E-MAIL"
			];
	
	//$("#pg02_setPType").append("<option value=''></option>");
	for(let i=0;i<pg02.manualClassOpt.length;i++)
		$("#pg02_setPType").append("<option value='"+ pg02.manualClassOpt[i]+"'>" + pg02.manualClassOpt[i]+ "</option>");
}

function pg02_clear(){
	isEdited = false;
	
	$("#pg02_setClass").val("MANUAL");
	$('#pg02_setPType')[0].disabled = false;
	$("#pg02_setPType").val("");
	$("#pg02_setDescript").val("");
	$("#pg02_setDRN").val('N');
	$("#pg02_setPath").val("");
	$("#pg02_pathId").val("");
    $("#pg02_btnPath").css("display","");
    
	pg02.grid.deselectRow();
}

/**
 * 다이얼로그 창에 path 트리 목록을 구성해 선택가능하도록 해준다.
 * @param prjInfo // 프로젝트 정보 
 * @returns void
 */

function processFolder(folderList){
	let folder = [];
	
	for(let i=0;i<folderList.length;i++){
		if(folderList[i].type === "DCC")
			folder.push(folderList[i]);
		if(folderList[i].type === "TRA")
			folder.push(folderList[i]);
	}
	
	return folder;
}
function pg02_SetDialog(prjInfo){
	let folderPath = getFolderList();
	// folderPath = dccFolder(folderPath);
	folderPath = processFolder(folderPath);
	

	
	pg02_Tree = $('#pg02_tree').jstree({
	     "core" : {
	    	 'data' : folderPath,
	         "check_callback" : true
	       },
	       'sort' : function(a, b) {
	           a = this.get_node(a);
	           b = this.get_node(b);
	           if(a.icon != 'resource/images/pinwheel.png' && a.original.type == 'DCC'){
	        	   return (a.text > b.text) ? 1 : -1;		           
	           }	           
	       },    
	      "plugins" : [ "contextmenu" , "sort"]
	    });
	
	// 다이얼로그 창의 select버튼에 onclick() 부여
	$("#pg02_folderSelect").click(function(){
			var gridTmpBean = {};
			gridTmpBean.rowIndex = pg02.grid.getSelectedRows().length>0 ? pg02.grid.getSelectedRows()[0].getPosition() : -1;
			gridTmpBean.prjInfo = prjInfo;
			pg02_selectPath(gridTmpBean) 
		});
}

function pg02_setProcessTypeOption(class_lvl){
	
	pg02_clearOption();
	
	
	if(class_lvl == "SYSTEM"){
		$('#pg02_setPType')[0].disabled = true;
		$("#pg02_setPType").append("<option value=''></option>");
		for(let i=0;i<pg02.systemClassOpt.length;i++)
			$("#pg02_setPType").append("<option value='"+ pg02.systemClassOpt[i]+"'>" + pg02.systemClassOpt[i]+ "</option>");
	}
	else{
		$("#pg02_setPType").append("<option value=''></option>");
		for(let i=0;i<pg02.manualClassOpt.length;i++)
			$("#pg02_setPType").append("<option value='"+ pg02.manualClassOpt[i]+"'>" + pg02.manualClassOpt[i]+ "</option>");
	}
	
	
	
}

function pg02_clearOption(){
	$('#pg02_setPType')[0].disabled = false;
	$('#pg02_setPType').children('option').remove();
}

/**
 * select버튼 온클릭
 * @param prjInfo // 프로젝트 정보 
 * @returns void
 */

function pg02_selectPath(gridTmpBean){
	
	var rowIndex = gridTmpBean.rowIndex;
	var prjInfo = gridTmpBean.prjInfo;
	
//	console.log(rowIndex);
	var selected = pg02_Tree.jstree('get_selected', true);
	
	var result = "";
	var chkPath = [];
	for(var i=0;i<selected.length;i++){
		var tmp = selected[i].id;
		var chkFlag = true;
	// DB로 정보를 넘기기 전에 다른 프로세스 클래스에서 해당 패스를 사용중인지 확인 후, 사용되지 않는 패스만 넘김
		for(var j=0;j<pg02.pathArr.length;j++){
			// 현재 추가하는 로우의 기존 패스와는 중복여부 상관 없다.
			if(j == rowIndex) continue;
            if(pg02.pathArr[j].includes(tmp)) {
            	console.log("해당 폴더를 사용중인 rowIndex:"+j,"선택한 폴더의 id:"+pg02.pathArr[j]);
            	alert("can't set the folder because setted in another Process Type");
            	chkFlag = false;
            	break;
            }
        }
		if(chkFlag) chkPath.push(tmp);		
	}
	
	for(var i=0;i<chkPath.length;i++)
		result += (i < chkPath.length-1 ? chkPath[i]+"@" : chkPath[i]);
	
	
	//if(chkPath.length>0)
	$.ajax({
	    url: 'pg02_getPath.do',
	    type: 'POST',
	    data:{
	    	process_folder_path_id:result,
	    	prj_id:prjInfo.id
	    },
	    success: function onData (data) { 
			isEdited = true;
	    	if(pg02_selectdData) pg02_selectdData.pathData = data;
	    	$("#pg02_setPath").val(data[0]);
	    	$("#pg02_pathId").val(data[1]); // 히든
	    	//pg02_getGrid(data);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	$( "#pg02_dialog" ).dialog('close');
}





/**
 * 프로젝트 정보를 첫 화면에 매핑시켜줌
 * @param prjInfo // 프로젝트 정보 
 * @returns void
 */
function initStep02(prjInfo){
	
	// pg02_Path 초기화
	pg02_Path = "";
	
	$("#pg02_pjCode").val(prjInfo.no);
	$("#pg02_pjNm").val(prjInfo.nm);
	$("#pg02_pjFullNm").val(prjInfo.full_nm);
	
	
	$.ajax({
	    url: 'pg02_getRvc.do',
	    type: 'POST',
	    data:{
	    	prj_id:getPrjInfo().id
	    },
	    success: function onData (data) {
	    	$("#review_conclusion").val(data.model.rvc);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	

/*	$.ajax({
	    url: 'pg02_getGrid.do',
	    type: 'POST',
	    data:{
	    	prj_id:getPrjInfo().id
	    },
	    success: function onData (data) {
	    	initCallback(data);
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});*/
	
	// 다이얼로그 화면 구성
	pg02_SetDialog(prjInfo);
	
}

function pg02_rvc_apply(){
	let rvc = $("#review_conclusion").val();
	
	$.ajax({
	    url: 'pg02_rvcUpdate.do',
	    type: 'POST',
	    data:{
	    	prj_id:getPrjInfo().id,
	    	set_val:rvc
	    },
	    success: function onData (data) {
	    	if(data.model.status != 'FAIL')
	    		alert("Successfully saved");
	    	else
	    		alert("Save failed");
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// init ajax수행 후, callback함수
function initCallback(data){
	pg02_getGrid(data);
	$("#pg02_setClass").val("MANUAL");
	// pg02_deselect();
	
	// 그리드 외부 영역 더블클릭시, 선택해제시키는 이벤트 바인딩
//	$(document).dblclick(function (e){
//	    if( $('#pg02_Grid').has(e.target).length === 0)
//	    	pg02_deselect();
//	    });
	
	
}

/**
 * Path버튼을 누를 시, 다이얼로그 창이 뜨도록 해준다.
 * @param prjInfo // 프로젝트 정보 
 * @returns void
 */
function pg02_openDialog(prjInfo){
	$( "#pg02_dialog" ).dialog({
		modal : true, 
		resizable : false,
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
}

/**
 * 최신 DB값을 그리드에 뿌려준다.
 * @param data // DB 검색 결과 
 * @returns void
 */
function pg02_getGrid(data){
	// 그리드에 담을 데이터 목록
	var processClass = [];
	
	// system 클래스 테이블 추가	
	systemClass = data[0];
	// manual 클래스 테이블 추가
	manualClass = data[1];
	
	// 다른 클래스와 패스 중복되지 않도록 패스정보를 미리 담아둠
	// 그리드 정보가 갱신될 때마다 초기화
	pg02.pathArr = [];
	
	for(var i=0;i<systemClass.length;i++){
		
		/*
		var innerRow = '<td></td>'
			+ '<td>SYSTEM</td>'
			+ '<td>TRANSMITTAL</td>'
			+ '<td>TRANSMITTAL 발송시 DOCUMENT 메일 링크 처리를 위한 프로세스가 적용되는 메뉴 (삭제,변경불가)</td>'
			+ '<td>미사용</td>'
			+ '<td>/TEST/TRANSMITTAL</td>'
			
		$("#pg02_list").append('<tr>'+innerRow+'</tr>');
		*/
		
		var tmp = {};
		tmp.class_lvl = systemClass[i].class_lvl;
		tmp.doc_type = systemClass[i].doc_type;
		tmp.process_type = systemClass[i].process_type;
		tmp.process_desc = systemClass[i].process_desc;
		tmp.drn_use_yn = systemClass[i].drn_use_yn==='Y'?'적용':'';
		tmp.process_folder_path_id = systemClass[i].process_folder_path_id;
		tmp.process_folder_path_string =systemClass[i].process_folder_path_string;
		tmp.process_id = systemClass[i].process_id;
		tmp.bean = systemClass[i];
		
		
		processClass.push(tmp);
		pg02.pathArr.push(tmp.process_folder_path_id.split("@"));

		
	}
	
	
	// manual 클래스
	for(var i=0;i<manualClass.length;i++){
		
		
		/*
		var innerRow = '<td><input type="checkbox"></td>'
			+ '<td>SYSTEM</td>'
			+ '<td>TRANSMITTAL</td>'
			+ '<td>TRANSMITTAL 발송시 DOCUMENT 메일 링크 처리를 위한 프로세스가 적용되는 메뉴 (삭제,변경불가)</td>'
			+ '<td>미사용</td>'
			+ '<td>/TEST/TRANSMITTAL</td>'
			
		$("#pg02_list").append('<tr class="Manual_Class">'+innerRow+'</tr>');
		*/
		
		var tmp = {};
		
		tmp.class_lvl = manualClass[i].class_lvl;
		tmp.doc_type = manualClass[i].doc_type;
		tmp.process_type = manualClass[i].process_type;
		tmp.process_desc = manualClass[i].process_desc;
		tmp.drn_use_yn = manualClass[i].drn_use_yn==='Y'?'적용':'';
		tmp.process_folder_path_id = manualClass[i].process_folder_path_id;
		tmp.process_folder_path_string =manualClass[i].process_folder_path_string;
		tmp.process_id = manualClass[i].process_id;
		tmp.bean = manualClass[i];
		
		processClass.push(tmp);
		pg02.pathArr.push(tmp.process_folder_path_id.split("@"));
		
	}
	pg02.grid.setData(processClass);
	pg02.size = pg02.grid.getData().length;

	// 그리드 row 클릭 이벤트 바인딩
	pg02.grid.on("rowClick", function(e, row){
		// row 선택 전, 모든 선택된 row 해제
		pg02.grid.deselectRow();
		// 더블클릭한 row 선택 활성화
		row.toggleSelect();
		pg02_selectdData = row.getData();
		if(!pg02_selectdData.process_type){
			pg02_setProcessTypeOption('MANUAL');
			return;
		}
		$("#pg02_setClass").val(pg02_selectdData.bean.class_lvl);
		pg02_setProcessTypeOption(pg02_selectdData.bean.class_lvl);
		$("#pg02_setPType").val(pg02_selectdData.bean.process_type);
		
		$("#pg02_setDescript").val(pg02_selectdData.bean.process_desc);
		$("#pg02_setDRN").val(pg02_selectdData.bean.drn_use_yn);
		$("#pg02_pathId").val(pg02_selectdData.bean.process_folder_path_id);
		$("#pg02_setPath").val(pg02_selectdData.bean.process_folder_path_string);
		$("#pg02_setDocType").val(pg02_selectdData.bean.doc_type);
		
		// 해당 시스템 클래스는 패스 변경 불가
		let pathChgX = [	
			"TRANSMITTAL", 
			"VENDOR DOC. TRANSMITAL",
			"SITE DOC. TRANSMITTAL"
			];
		if(pathChgX.includes(pg02_selectdData.process_type)){
			$("#pg02_btnPath").css("display","none");
		}else $("#pg02_btnPath").css("display","");
		
		$("#pg02_setPType").change(function(){
			isEdited = true;
		});
		$("#pg02_setDescript").change(function(){
			isEdited = true;
		});
		$("#pg02_setDRN").change(function(){
			isEdited = true;
		});
		$("#pg02_setDocType").change(function(){
			isEdited = true;
		});
		 
	});
	
}

/**
 * row 선택해제 및 input태그들 초기화 반영
 * @param void 
 * @returns void
 */
function pg02_deselect(){
	pg02.grid.deselectRow();
	// input 태그 영역 clear
	$("#pg02_setClass").val("MANUAL");
	$('#pg02_setPType')[0].disabled = false;
	$("#pg02_setPType").val("");
	$("#pg02_setDescript").val("");
	$("#pg02_setDRN").val('N');
	$("#pg02_setPath").val("");
	$("#pg02_pathId").val("");
    $("#pg02_btnPath").css("display","");
	
	pg02_selectdData = "";
	pg02_Path = "";
}

/**
 * 프로젝트 정보를 수정한다.
 * @param prjInfo // DB 검색 결과 
 * @returns void
 */
function pg02_apply(prjInfo){
	
	var pj_id = $("#selectPrjId").val();
	var pj_no = $("#pg02_pjCode").val();
	var pj_nm = $("#pg02_pjNm").val();
	var pj_full_nm = $("#pg02_pjFullNm").val();
	
	if(!pj_no || !pj_nm || !pj_full_nm) {
		alert("공란은 적용할 수 없습니다.");
		return;
	}
	
	
	$.ajax({
	    url: 'pg_UpdatePrjInfo.do',
	    type: 'POST',
	    data:{
	    	prj_id:pj_id,
	    	prj_no:pj_no,
	    	prj_nm:pj_nm,
	    	prj_full_nm:pj_full_nm
	    },
	    success: function onData (data) {
	    	$.ajax({
	    	    url: 'headerRefresh.do',
	    	    type: 'POST',
	    	    data:{
	    	    	prj_id:prj_id
	    	    },
	    	    success: function onData (data) {
	    	    	// 헤더의 프로젝트 리스트 db정보 갱신
	    	    	isEdited = false;
	    	    	refreshPrjList(data);	    	
                    
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

/**
 * 프로세스를 추가한다.
 * @param prjInfo // 프로젝트 정보
 * @returns void
 */
function pg02_add(prjInfo){
	pg02_add2(prjInfo);
}

function pg02_add2(prjInfo){
	
	pg02.grid.deselectRow();
	$("#pg02_setClass").val("MANUAL");
	$('#pg02_setPType')[0].disabled = false;
	$("#pg02_setPType").val("");
	$("#pg02_setDescript").val("");
	$("#pg02_setDRN").val('N');
	$("#pg02_setPath").val("");
	$("#pg02_pathId").val("");
    $("#pg02_btnPath").css("display","");
    
    
	let addChk = pg02.grid.getData().length;
	
	if(addChk > pg02.size) return;
	
	
	let tmpRow = {};
	tmpRow.class_lvl = 'MANUAL';
    pg02.grid.addRow(tmpRow);
	
	/*
	var process_type = $("#pg02_setPType").val();
    var process_desc = $("#pg02_setDescript").val();
    var drn_use_yn = $("#pg02_setDRN").val();
    var doc_type = $("#pg02_setDocType").val();
    var process_folder_path_id = $("#pg02_pathId").val();
    var process_folder_path_string = $("#pg02_setPath").val();
    
    
	if($("#pg02_setClass").val() === 'SYSTEM'){
		alert("SYSTEM 클래스는 추가할 수 없습니다.");
		return;
	}
	
	$.ajax({
	    url: 'pg02_addClass.do',
	    type: 'POST',
	    data:{
	    	prj_id:prjInfo.id,
	    	process_type:process_type,
	    	process_desc:process_desc,
	    	drn_use_yn:drn_use_yn,
	    	doc_type:doc_type,
	    	process_folder_path_id:process_folder_path_id,
	    	process_folder_path_string:process_folder_path_string
	    },
	    success: function onData (data) {
	    	// 테이블 갱신
	    	isEdited = false;
	    	pg02_getGrid(data);
	    	pg02_deselect();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	*/
}

/**
 * 프로세스 정보를 수정한다.
 * @param prjInfo // 프로젝트 정보
 * @returns void
 */
function pg02_save(prjInfo){
	// 선택된 row 있는지 확인
	var selectedData = pg02.grid.getSelectedData();
	if(selectedData.length<1)return;
	
	var process_type = $("#pg02_setPType").val();
    var process_desc = $("#pg02_setDescript").val();
    var doc_type = pg02.processMap[process_type];
    var drn_use_yn = $("#pg02_setDRN").val();
    // System 클래스의 경우 drn_use는 항상 미사용이므로 다른 값이 넘어가지 않도록 조치
    if(selectedData.class_lvl === 'SYSTEM') drn_use_yn = 'N';
    var process_folder_path_id = $("#pg02_pathId").val();
    var process_folder_path_string = $("#pg02_setPath").val();
	
	selectedData = selectedData[0].bean;
	
	if(!selectedData){ // bean이 없다면 db에 존재하지 않은 값이므로 insert
		
		if(process_folder_path_string == ''){
			alert("the folder must be designated for this process");
			
			return;
		}
		
		$.ajax({
		    url: 'pg02_addClass.do',
		    type: 'POST',
		    data:{
		    	prj_id:prjInfo.id,
		    	process_type:process_type,
		    	process_desc:process_desc,
		    	drn_use_yn:drn_use_yn,
		    	doc_type:doc_type,
		    	process_folder_path_id:process_folder_path_id,
		    	process_folder_path_string:process_folder_path_string
		    },
			async: false,
		    success: function onData (data) {
		    	// 테이블 갱신
		    	alert("Insert complete");
		    	isEdited = false;
		    	pg02_getGrid(data);
		    	pg02_deselect();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
	
	
    
	
	else{ // db에 존재하므로 update
		
		// 해당 클래스는 반드시 패스 지정
		let pathChk = [	
			"CORRESPONDENCE",
			"TR OUTGOING", 
			"TR INCOMING",
			"VTR OUTGOING", 
			"VTR INCOMING",
			"STR OUTGOING"  
			];
		
		if(pathChk.includes(process_type)){
			// 패스 포함되었는지 확인
			if(process_folder_path_id == ''){
				alert("the folder must be designated for this process");
				return;
			}
				
		}
		
		
		
		if(process_folder_path_id == ''){
			alert("the folder must be designated for this process");
			return;
		}
		
		$.ajax({
		    url: 'pg02_updateClass.do',
		    type: 'POST',
		    data:{
		    	prj_id : prjInfo.id,
		    	class_lvl : selectedData.class_lvl,
		    	process_desc : process_desc,
		    	process_folder_path_id : process_folder_path_id,
		    	drn_use_yn : drn_use_yn,
		    	doc_type : doc_type,
		    	process_type : process_type,
		    	process_id : selectedData.process_id,
		    	process_folder_path_string : process_folder_path_string
		    },
			async: false,
		    success: function onData (data) {
		    	// 테이블 갱신
		    	alert("Successfully saved");
		    	isEdited = false;
		    	pg02_getGrid(data);
		    	pg02_deselect();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
	
	
	
}

/**
 * 선택된 프로세스를 삭제한다.
 * @param prjInfo // 프로젝트 정보
 * @returns void
 */
function pg02_del(prjInfo){

	// 체크된 row 확인
	var checkedRows = $('input:checkbox[name="pg02_gridChk"]:checked');
	
	var process_seq_list = "";
	// 체크된 process_id들을 문자열로 생성
	for(var i=0;i<checkedRows.length;i++){
			var now = checkedRows[i].value;
			process_seq_list += ( i < checkedRows.length-1 ? now+"," : now);
	}
	
	
	$.ajax({
	    url: 'pg02_delClass.do',
	    type: 'POST',
	    data:{
	    	prj_id:prjInfo.id,
	    	process_seq_list:process_seq_list
	    },
	    success: function onData (data) {
	    	alert("Delete complete");
	    	// 테이블 갱신
	    	pg02_getGrid(data);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
}

///////////////////////////////////////////////////////

/**
 * Project General 페이지 내에서,
 * 세번째 스텝 : numbering 설정
 * @param void 
 * @returns prj_info
 */
//pg03_에서 사용될 전역변수

var pg03 = { isFirst : true };
function pgen_step03(){
	
	if(!pg03.isFirst) return;
	
	pg03.isFirst = false;
	
	// 헤더에서 프로젝트 정보를 가져옴
	pg03.prjInfo = getPrjInfo();
	isEdited = false;
	
	// copy 체크박스 작동부분
	$('#pg03_selected_prj').click(function(){
    	$('#pg03_select_div').addClass('on');
    });
    $('.pg03_prj_select').click(function(){
    	var pg03_selected_prj_id = $(this).attr('id').replace('pg03_prj_id_is_','');
    	$('#pg03_selectPrjId').val(pg03_selected_prj_id);
    	var prj_nm = $(this).children().next().html();
    	$('#pg03_selected_prj').html(prj_nm);
    	$('#pg03_select_div').removeClass('on');
    });
    
    
    $("#pg03_copyChk").click(function(){
    	if($("#pg03_copyChk").is(":checked")) $(".copyDiv").css('display','flex');
    	else $(".copyDiv").css('display','none');
    });
    
    $(".copyChkdiv").click(function(){
    	$("#pg03_copyChk").prop('checked', !$("#pg03_copyChk").is(":checked"));
    	if($("#pg03_copyChk").is(":checked")) $(".copyDiv").css('display','flex');
    	else $(".copyDiv").css('display','none');
    });
    
	pg03.btnList = $(".pg03_Btn");
	// COPY 버튼
	$(pg03.btnList[0]).click(function(){ pg03_Copy(); });
	// Refresh 버튼
	$(pg03.btnList[1]).click(function(){ getPg03Grid(); });
	

	// 관리버튼 on click속성 부여
	var mngBtnList = $(".pg03_mngBtn");
	
	for(let i=0;i<mngBtnList.length;i++)
		$(mngBtnList[i]).click(function(){
			var codeType = $("#pg03_codeType").val();
			pg03_openDialog(pg03.prjInfo,codeType,i); 
		});
	
	
	
	pg03.code_Type = ['CORRESPONDENCE_TYPE','CORRESPONDENCE_FROM','CORRESPONDENCE_OUT','CORRESPONDENCE_IN'];
	pg03.grid = []; // pg03의 각 그리드를 담는 배열
	pg03.dial_Grid = []; // 다이얼로그 그리드
	
	
	$.ajax({
	    url: 'pg03_getSerialNo.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: getPrjInfo().id
	    },
	    success: function onData (data) {
	    	$("#pg03_SERIAL").val(data.model.serial);
	    	pg03.serial = data.model.serial;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	

	
	
	initPg03Grid(function(i){getPg03Grid(i)});
	
}


function pg03_serial_apply(){
	let set_val4 = $("#pg03_SERIAL").val();
	
	
	let chk = /^[\d]{1,10}$/;
	
	if(!chk.test(set_val4)){
		alert("digit 형식 오류");
		return;
	}
	
	$.ajax({
	    url: 'pg03_updateSerialNo.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: getPrjInfo().id,
	    	set_val4:set_val4
	    },
	    success: function onData (data) {
	    	if(data.model.status == 'FAIL')
	    		alert("failed to UPDATE");
	    	else{
	    		alert("successfully saved");
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}
// 각 그리드를 생성하고 전역변수로 할당시켜줌
function initPg03Grid(callback){
	
	var defaultColumns = [
		{field:"index", visible:false , headerSort:false},
		{	// 체크박스
			// 커스텀 포매터
			field:"ck",
			visible:false ,
			editor:false,
			formatter: function(cell, formatterParams, onRendered) {
		        const data = cell.getRow().getData();

		         var checkbox = document.createElement("input");
		          checkbox.name = 'pg03_gridChk3';
		          checkbox.type = 'checkbox';
		          checkbox.value = data.index;
		          return checkbox;		   
			},
			hozAlign:"center", 
			headerSort:false,
			width:10,
			cellClick:function(e, cell){
		        cell.getRow().toggleSelect();
		      }
		},
		{field:"prj_id", visible:false , headerSort:false},
		{field:"set_code_type", visible:false , headerSort:false},
		{field:"set_code_id", visible:false , headerSort:false},
		{field:"set_code_feature", visible:false , headerSort:false},
		{field:"set_code", visible:false , headerSort:false},
		{field:"set_code_desc", visible:false , headerSort:false},
		{field:"set_desc", visible:false , headerSort:false, hozAlign:"left"},
		{field:"set_val", visible:false , headerSort:false},
		{field:"set_val_Str", visible:false , headerSort:false},
		{field:"set_order", visible:false , headerSort:false},
		{field:"set_val2", visible:false , headerSort:false},
		{field:"set_val2_Str", visible:false , headerSort:false},
		{field:"set_val3", visible:false , headerSort:false},
		{field:"set_val3_Str", visible:false , headerSort:false},
		{field:"set_val4", visible:false , headerSort:false},
		{field:"set_val4_Str", visible:false , headerSort:false},
		{field:"ifc_yn", visible:false , headerSort:false},
		{field:"reg_id", visible:false , headerSort:false},
		{field:"reg_date", visible:false , headerSort:false},
		{field:"mod_id", visible:false , headerSort:false},
		{field:"mod_date", visible:false , headerSort:false},
		{field:"set_valS", visible:false , headerSort:false},
	];
	for(let i=0;i<$(".pg03_Grids").length;i++){
		var tmpGrid = new Tabulator( ($(".pg03_Grids")[i]), {
			layout: "fitColumns",
			placeholder:"No Data Set",
			index:"index",
			headerVisible:false, //hide column headers
			columns:defaultColumns
			});
		
		
		var tmpGrid2 = new Tabulator( ($(".pg03_dialog_Grids")[i]), {
			layout: "fitColumns",
			placeholder:"No Data Set",
			index:"index",
			columns:defaultColumns
			});
		
		
		pg03.grid.push(tmpGrid);
		pg03.dial_Grid.push(tmpGrid2);
		
		// 그리드 생성 후, 콜백함수
		pg03.grid[i].on("tableBuilt", function(){
			// 필드 셋 구성
			setFieldPg03Grid(pg03.grid,i);
			// DB에서 값을 가져옴
			getPg03Grid();
			} );
		
		pg03.dial_Grid[i].on("tableBuilt", function(){
			// 필드 셋 구성
			setFieldPg03Grid(pg03.dial_Grid ,i);
			pg03.dial_Grid[i].getColumn('ck').updateDefinition({visible:true});
			
			let cols = pg03.dial_Grid[i].getColumns();
			for(let x = 0 ; x < cols.length; x++){
				if(cols[x].getField() != 'ck')
					cols[x].updateDefinition({editor:"input"});
			}
			
			pg03.dial_Grid[i].getColumn('ck').updateDefinition({visible:true});
			
			/*
			$.ajax({
			    url: 'pg03_getGridParam.do',
			    type: 'POST',
			    traditional : true,
			    data:{
			    	prj_id: pg03.prjInfo.id
			    },
			    success: function onData (data) {

			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
			*/
			 
			/*
			 * select 방식
			switch(i){
				
				case 0:
					break;
				case 1:
					pg03.dial_Grid[i].getColumn('set_val').updateDefinition({editor:"select", editorParams:["당사","고객사","현장"]});
					break;
				case 2:
				case 3:
					pg03.dial_Grid[i].getColumn('set_valS').updateDefinition({visible:false});
					pg03.dial_Grid[i].getColumn('set_code_desc').updateDefinition({visible:true,editor:false});
					pg03.dial_Grid[i].getColumn('set_code').updateDefinition({visible:true,title:'TYPE',editor:'select'});
					pg03.dial_Grid[i].getColumn('set_val_Str').updateDefinition({visible:true,title:'FROM',editor:'select'});
					pg03.dial_Grid[i].getColumn('set_val2_Str').updateDefinition({visible:true,title:'TO',editor:'select'});
					pg03.dial_Grid[i].getColumn('set_val4').updateDefinition({visible:true,title:'DIGIT',editor:'input'});
					break;
				default:
					break;
			
			}
			*/
			
			switch(i){
			
			case 0:
				break;
			case 1:
				pg03.dial_Grid[i].getColumn('set_val').updateDefinition({editor:"select", editorParams:["당사","고객사","현장"]});
				break;
			case 2:
			case 3:
				pg03.dial_Grid[i].getColumn('set_valS').updateDefinition({visible:false});
				pg03.dial_Grid[i].getColumn('set_desc').updateDefinition({visible:true,title:"DESCRIPTION",editor:'input'});
				pg03.dial_Grid[i].getColumn('set_code').updateDefinition({visible:true,title:'TYPE',editor:'select'});
				/*
				pg03.dial_Grid[i].getColumn('set_val_Str').updateDefinition({visible:true,title:'FROM',editor:'input'});
				pg03.dial_Grid[i].getColumn('set_val2_Str').updateDefinition({visible:true,title:'TO',editor:'input'});
				*/
				pg03.dial_Grid[i].getColumn('set_val').updateDefinition({visible:true,title:'FROM',editor:'input'});
				pg03.dial_Grid[i].getColumn('set_val2').updateDefinition({visible:true,title:'TO',editor:'input'});
				pg03.dial_Grid[i].getColumn('set_val3').updateDefinition({visible:true,title:'type',editor:'input'});
				pg03.dial_Grid[i].getColumn('set_val4').updateDefinition({visible:true,title:'DIGIT',editor:'input'});
				break;
			default:
				break;
		
		}
			
				
			
				
			
			// 모달 그리드가 생성 되면 버튼 활성화
			initPg03ModalBtn(i);

			// 그리드 row 클릭 이벤트 바인딩
			pg03.dial_Grid[i].on("rowClick", function(e, row){
	    		// row 선택 전, 모든 선택된 row 해제
	    		this.deselectRow();
	    		// 더블클릭한 row 선택 활성화
	    		row.select();
	    		//row.toggleSelect();
	    	});
			
			
			
			
			// esc 눌렀을 경우, 선택해제 시키는 이벤트 바인딩
			$(document).keydown(function(event) {
			    if ( event.keyCode == 27 || event.which == 27 ) {
			    	pg03.dial_Grid[i].deselectRow();
			    }
			});
			
			
		} );
		
	}
	
}

function getIdFromCode(codeTbl , code){
	return codeTbl[code];
}

function getCodeParams(grid,feature){
	let result = [];
	let tmp = grid.getData();
	for(let x=0 ; x < tmp.length ; x++){
		let tmpRow = tmp[x];
		result.push(!feature ? tmpRow.set_code : tmpRow.set_val);
	}
	
	return result;
}

function getCodeParamsObject(grid,feature,reverse){
	let result = {};
	let tmp = grid.getData();
	for(let x=0 ; x < tmp.length ; x++){
		let tmpRow = tmp[x];
		if(!reverse)result[(!feature ? tmpRow.set_code : tmpRow.set_val)] = tmpRow.set_code_id;
		else result[tmpRow.set_code_id] = (!feature ? tmpRow.set_code : tmpRow.set_val);
	}
	
	return result;
}


function setFieldPg03Grid(grid,i){
	

	// 코드 타입에 따른 그리드 필드셋 구현
	switch (i) {
	  case 0:
		  grid[i].getColumn('set_code').updateDefinition({title:"TYPE", visible:true});
		  grid[i].getColumn('set_desc').updateDefinition({title:"DESCRIPTION",visible:true});
		  break;
	  case 1:
		  grid[i].getColumn('set_code').updateDefinition({title:"TYPE", visible:true});
		  grid[i].getColumn('set_desc').updateDefinition({title:"DESCRIPTION",visible:true});
		  grid[i].getColumn('set_val').updateDefinition({title:"FROM/TO",visible:true});
		  break;
	  case 2:
	  case 3:
		  // grid[i].getColumn('set_code_desc').updateDefinition({title:"DESCRIPTION",visible:true});
		  grid[i].getColumn('set_code').updateDefinition({title:"TYPE",visible:true});
		  grid[i].getColumn('set_valS').updateDefinition({title:"VAL",visible:true});
		  break;
	  default:
		  break;
	}
	
	
}


function getPg03Grid(){
	// db에서 그리드에 들어갈 데이터를 가져옴
	$.ajax({
	    url: 'pg03_getGrid.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg03.prjInfo.id
	    },
	    success: function onData (data) {
	    	
	    	pg03.type_Params = getCodeParams(pg03.grid[0]);
	    	pg03.type_ParamsO = getCodeParamsObject(pg03.grid[0]);
	    	pg03.typeVal_Params = getCodeParams(pg03.grid[0],'v');
	    	pg03.typeVal_ParamsO = getCodeParamsObject(pg03.grid[0],'v','r');
	    	pg03.fromTo_Params = getCodeParams(pg03.grid[1]);
	    	pg03.fromTo_ParamsO = getCodeParamsObject(pg03.grid[1]);
	    	
	    	
	    	for(var i=0;i<pg03.grid.length;i++)
	    		setPg03Grid(i,data[i],[data[0],data[1]]);
	    	
	    	
	    
			
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function pg03HeadClick(i){
	if(!pg03.grid[0].initialized) return;
	// 정렬 토글용 방향 변수
	let dir = "asc";
	if(pg03.grid[i].getSorters()[0])
		dir = (pg03.grid[i].getSorters()[0].dir != "asc") ? "asc" : "desc"
			
	pg03.grid[i].setSort('set_code',dir);
}

function setPg03Grid(i,data,codeTbl){	
	
	for(let x=0;x<data.length;x++) data[x].index = x;
	
	switch(i){
		case 0:
			pg03.grid[i].setData(data);
			break;
		case 1:
			pg03.grid[i].setData(data);
			break;
		case 2:
		case 3:
			for(var idx=0;idx<data.length;idx++){
				
				var str = "";
				/*
				var tmpArr = data[idx].set_valStr;
				for(let j=0;j<tmpArr.length;j++) str += ( tmpArr[j] + "-" );
				*/
				str += data[idx].set_val + "-";
				str += data[idx].set_val2 + "-";
				str += data[idx].set_val3 + "-";
				str += data[idx].set_val4;
				
				/*
				// digit값 변화
				let digit = data[idx].set_val4;
				digit = digit.split("%");
				let digitNo = parseInt(digit[0]);
				digit = "";
				for(let d=0; d<digitNo; d++) digit += "#";
				*/
				//str += digit;			
				data[idx].set_val_Str = findCode(codeTbl[1],data[idx].set_val);
				data[idx].set_val2_Str = findCode(codeTbl[1],data[idx].set_val2);
				data[idx].set_val3_Str = findCode(codeTbl[0],data[idx].set_val3);
				// data[idx].set_val4 = digit;
				data[idx].set_valS = str;
				data[idx].set_code_desc = pg03.typeVal_ParamsO[data[idx].set_val3];
				
			}
			pg03.grid[i].setData(data);
			break;
	}
	if(i==2)
		pg03.dial_Grid[i].getColumn('set_code').updateDefinition({editorParams:["TR OUTGOING","VTR OUTGOING","STR OUTGOING"]});
	if(i==3)
		pg03.dial_Grid[i].getColumn('set_code').updateDefinition({editorParams:["TR INCOMING","VTR INCOMING"]});
	/*
	pg03.dial_Grid[i].getColumn('set_code').updateDefinition({editorParams:{values:pg03.type_Params}});
	pg03.dial_Grid[i].getColumn('set_val_Str').updateDefinition({editorParams:{values:pg03.fromTo_Params}});
	pg03.dial_Grid[i].getColumn('set_val2_Str').updateDefinition({editorParams:{values:pg03.fromTo_Params}});
	pg03.dial_Grid[i].getColumn('set_val3_Str').updateDefinition({editorParams:{values:pg03.type_Params}});
	*/
	
	// 그리드 필드가 생성되고 나면, select 옵션 클릭했을 때의 이벤트를 부여해준다.
	/*
	if(i==0){	
		pg03.dial_Grid[i].on("cellEdited", function(cell){
			let columnName = cell.getColumn().getField();
			if(columnName === 'set_desc')
				cell.getRow().update({set_val : cell.getValue()});				
		});
	}
	else if(i>1){
		pg03.dial_Grid[i].on("cellEdited", function(cell){
			// select 타입에 대하여 수정할 때의 이벤트 
			if(cell.getColumn().getDefinition().editor =='select'){
				let columnName = cell.getColumn().getField();
				let id;

				switch(columnName){
					case 'set_code':
						id = getIdFromCode(pg03.type_ParamsO , cell.getValue());
//						console.log(pg03.typeVal_ParamsO[id]);
						cell.getRow().update({set_val3 : id, set_code_desc : pg03.typeVal_ParamsO[id]});
						break;
					case 'set_val_Str':
						id = getIdFromCode(pg03.fromTo_ParamsO , cell.getValue())
						cell.getRow().update({set_val : id});
						break;
					case 'set_val2_Str':
						id = getIdFromCode(pg03.fromTo_ParamsO , cell.getValue())
						cell.getRow().update({set_val2 : id});
						break;
				}
				
				
			}

		});
	}
	*/
	
	
	
}
/**
 * 코드 테이블이 주어졌을 경우, 해당 id에 매핑되는 코드 값을 리턴해준다.
 * @param codeTbl : 코드 테이블, codeId : 코드 아이디
 * @returns void
 */
function findCode(codeTbl,codeId){
	for(let x=0;x<codeTbl.length;x++){
		if(codeTbl[x].set_code_id === codeId) return codeTbl[x].set_code;
	}
		
	// 발견실패시 -1 리턴
	return -1;
}


/**
 * 모달 창에 있는 버튼들을 그리드와 매핑시켜준다.
 * @param gridIdx : 몇번 그리드인지 가리키는 인덱스
 * @returns void
 */
function initPg03ModalBtn(gridIdx){
	pg03.modalBtn = $(".pg03_dialogBtn");
	
	let btnList = [pg03_save,pg03_cancel,pg03_insert,pg03_del,pg03_up,pg03_down];
	
	
	
	
	// 모달 버튼의 시작점 
	var startModalIdx = 6 * gridIdx;
	// 해당 모달에 해당하는 버튼 그룹에 각각 onClick 속성 부여
	for(let idx = startModalIdx ; idx < startModalIdx + 6 ; idx++){
		$(pg03.modalBtn[idx]).click(function(){
			btnList[idx % 6](gridIdx,function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);});
		});
	}
	
}

function pg03_save(gridIdx){
	// 바라볼 그리드 지정
	let thisGrid = pg03.dial_Grid[gridIdx];
	
	let gridList = thisGrid.getData();
	// 처음부터 비어있는 그리드는 제외
	if(!pg03.delFlag && gridList.length < 1) return;
	
	let changedList = thisGrid.getEditedCells();
	let changedData = [];
	for(var i = 0 ; i<changedList.length ; i++){
		var tmpIdx = changedList[i].getData().index
		changedData.push(tmpIdx);
	}
	
	// 입력값 체크
	for(let x=0;x<changedData.length;x++){
		let chk = /^[\d]{1,10}$/;
		//let chk = /^#{0,9}#$/;
		
		if(gridList[changedData[x]].set_val4)
		if(!chk.test(gridList[changedData[x]].set_val4)){
			alert("digit 형식 오류");
			return;
		}
		
		if(gridIdx>1){
			if(!gridList[changedData[x]].set_val) return;
			if(!gridList[changedData[x]].set_val2) return;
			if(!gridList[changedData[x]].set_val3) return;
		}		
					
	}
	
	
	if(gridIdx>1){
		let chkData = thisGrid.getData();
		let set = [];
		for(let i=0 ; i<chkData.length ; i++)
			set.push(chkData[i].set_code);
		set = new Set(set);
		
		if(chkData.length != set.size){
			alert("can't set the same process type. all values MUST be unique");
			return;
		}
	}
	
	
	// 변화가 있는 row들 json으로 변환
	let tmpStrings = [];
	let tmpGridData = thisGrid.getData();
	
	for(var i=0;i<changedData.length;i++){
		var rowIndex = changedData[i];
		/*
		 * set_val4를 digit 형식에 맞게 변환
		if(gridIdx>1){
			let digitTrans = tmpGridData[rowIndex].set_val4;
			digitTrans = digitTrans.length + "%";
			tmpGridData[rowIndex].set_val4 = digitTrans;
		}
		*/		
		tmpStrings.push( JSON.stringify(tmpGridData[rowIndex]) );
	}
	
	// 삭제된 row의 set_code_id를 배열로 넘겨준다.
	let deletedIds = [];
	if(pg03.delFlag)
		// 삭제된 row의 code id 배열로 생성
	for(let i=0;i<thisGrid.deletedIds.length;i++)
		deletedIds.push(thisGrid.deletedIds[i][1]);
	
//	console.log(deletedIds);
//	console.log(tmpStrings);
	

		
//	console.log(tmpStrings);
	$.ajax({
	    url: 'pg03_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	jsonRowdatas:tmpStrings,
	    	prj_id: pg03.prjInfo.id,
	    	set_code_type:pg03.code_Type[gridIdx],
	    	deleteRows:deletedIds
	    },
	    success: function onData (data) {
	    	getPg03Grid();
	    	alert("successfully saved");
	    	$("#pg03_dialog"+gridIdx).dialog('close');
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	// 삭제 후, 반영 완료하였으니 delFlag 다시 초기화	
	thisGrid.deletedIdxList = [];
	pg03.delFlag = false;
	
}

function pg03_cancel(gridIdx){
	// 바라볼 그리드 지정
	$("#pg03_dialog"+gridIdx).dialog('close');
	
}

function pg03_insert(gridIdx){
	// 바라볼 그리드 지정
	let thisGrid = pg03.dial_Grid[gridIdx];
	
	let lastIndex = thisGrid.getData().length;
	let newCodeId = code_getMaxId(thisGrid);
	newCodeId = String(Number(newCodeId) + 1).padStart(5,"0");
	let newSetOrd = code_getMaxOrder(thisGrid)+1;
	
	if(gridIdx == 2){
		if(thisGrid.getData().length == 3){
			alert("can't insert the numbering more than the number of available process types");
			return;
		}
			
		
	}
	else if(gridIdx == 3){
		if(thisGrid.getData().length == 2){
			alert("can't insert the numbering more than the number of available process types");
			return;
		}
	}
	
	let addRow = {
					index:lastIndex,
					prj_id:prj_id,
					set_order:newSetOrd,
					set_code_id:newCodeId,
					set_code_type : pg03.code_Type[gridIdx],
					set_code_feature : (gridIdx < 2) ? 'C' : 'V',
					set_code:"",
					set_val:""
				};
	// 0,1 그리드의 데이터의 row는 serial을 따름
	if(gridIdx<2)
		addRow.set_val4 = pg03.serial;
	
	
	thisGrid.addData(addRow,false,lastIndex);
	thisGrid.redraw();
	
}

function pg03_del(gridIdx){
	// 바라볼 그리드 지정
	let thisGrid = pg03.dial_Grid[gridIdx];
	
	
	// 체크박스 selector name
	// let chkString = "input:checkbox[name='pg03_gridChk" + gridIdx + "']:checked";
	let chkString = "input:checkbox[name='pg03_gridChk3']:checked";
	let chk = $(chkString);
	if(chk.length<1) alert("Please check the row to delete");
	for(let i=0 ; i<chk.length ; i++){
		let idx = Number(chk[i].value); // index 숫자로 변환
		thisGrid.deletedIds.push([pg03.code_Type[gridIdx],thisGrid.getData()[idx].set_code_id]);
		thisGrid.deletedIdxList.push(idx);
	}
	thisGrid.deleteRow(thisGrid.deletedIdxList);
	pg03.delFlag = true;
	
}

function pg03_up(gridIdx,callback){
	
	// 바라볼 그리드 지정
	let thisGrid = pg03.dial_Grid[gridIdx];
	let gridList = thisGrid.getData();
	
	
	
	// 입력값 체크
	let changedList = thisGrid.getEditedCells();
	let changedData = [];
	for(var i = 0 ; i<changedList.length ; i++){
		var tmpIdx = changedList[i].getData().index
		changedData.push(tmpIdx);
	}
	
	for(let x=0;x<changedData.length;x++){
		// let chk = /^[\d]{1,3}%$/;
		let chk = /^[\d]{1,10}$/;
		if(gridList[changedData[x]].set_val4)
		if(!chk.test(gridList[changedData[x]].set_val4)){
			alert("digit 형식 오류");
			return;
		}
		
		if(gridIdx>1){
			if(!gridList[changedData[x]].set_val) return;
			if(!gridList[changedData[x]].set_val2) return;
			if(!gridList[changedData[x]].set_val3) return;
		}		
					
	}
	
	
	

	let selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getPrevRow()) return;
	
	let selectedIndex = selectedRow.getIndex();
	
	let targetIndex = selectedRow.getPrevRow().getIndex();
	
	
	let selectedOrd = Number(thisGrid.getRow(selectedIndex).getData().set_order);
	let targetOrd = Number(thisGrid.getRow(targetIndex).getData().set_order);

	thisGrid.getRow(selectedIndex).update({set_order:targetOrd});
	thisGrid.getRow(targetIndex).update({set_order:selectedOrd});

	
	 // 버튼을 누를 때마다 db반영해야 하면 주석 해제 
	var updateRows = [];

	let selectData = clonedeep(thisGrid.getRow(selectedIndex).getData());
	/*
	if(gridIdx>1){
		let digitTrans = selectData.set_val4;
		digitTrans = digitTrans.length + "%";
		let sel_org = selectData.set_val4;
		selectData.set_val4 = digitTrans;
	}
	*/
	selectData.set_valStr = [];
	
	
	let targetData = clonedeep(thisGrid.getRow(targetIndex).getData());
	/*
	if(gridIdx>1){
		digitTrans = targetData.set_val4;
		digitTrans = digitTrans.length + "%";
		let tar_org = targetData.set_val4;
		targetData.set_val4 = digitTrans;
	}
	*/
	targetData.set_valStr = [];
	
	updateRows.push( JSON.stringify(selectData) );
	updateRows.push( JSON.stringify(targetData) );
	
	
	
	
	$.ajax({
	    url: 'pg03_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	jsonRowdatas:updateRows,
	    	prj_id: pg03.prjInfo.id,
	    	set_code_type:pg03.code_Type[gridIdx]
	    },
	    success: function onData (data) {
	    	getPg03Grid();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	selectedRow.move(targetIndex,true);
	
	callback(thisGrid,selectedIndex);
	
}

function pg03_down(gridIdx, callback){
	// 바라볼 그리드 지정
	let thisGrid = pg03.dial_Grid[gridIdx];
	let gridList = thisGrid.getData();
	// 입력값 체크
	let changedList = thisGrid.getEditedCells();
	let changedData = [];
	for(var i = 0 ; i<changedList.length ; i++){
		var tmpIdx = changedList[i].getData().index
		changedData.push(tmpIdx);
	}
	
	for(let x=0;x<changedData.length;x++){
		// let chk = /^[\d]{1,3}%$/;
		let chk = /^[\d]{1,10}$/;
		if(gridList[changedData[x]].set_val4)
		if(!chk.test(gridList[changedData[x]].set_val4)){
			alert("digit 형식 오류");
			return;
		}
		
		if(gridIdx>1){
			if(!gridList[changedData[x]].set_val) return;
			if(!gridList[changedData[x]].set_val2) return;
			if(!gridList[changedData[x]].set_val3) return;
		}		
					
	}
	
	
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getNextRow()) return;
	

	
	// 선택한 row의 인덱스
	let selectedIndex = selectedRow.getIndex();
	// 바꿀 row의 인덱스 (아래)
	let targetIndex = selectedRow.getNextRow().getIndex();	
	
	let selectedOrd = Number(thisGrid.getRow(selectedIndex).getData().set_order);
	let targetOrd = Number(thisGrid.getRow(targetIndex).getData().set_order);
	
	

	thisGrid.getRow(selectedIndex).update({set_order:targetOrd});
	thisGrid.getRow(targetIndex).update({set_order:selectedOrd});

	
	
	 // 버튼을 누를 때마다 db반영해야 하면 주석 해제 
	var updateRows = [];

	
	 // 버튼을 누를 때마다 db반영해야 하면 주석 해제 
	var updateRows = [];

	
	let selectData = clonedeep(thisGrid.getRow(selectedIndex).getData());
	/*
	if(gridIdx>1){
		let digitTrans = selectData.set_val4;
		digitTrans = digitTrans.length + "%";
		selectData.set_val4 = digitTrans;
	}
	*/
	selectData.set_valStr = [];
	
	
	let targetData = clonedeep(thisGrid.getRow(targetIndex).getData());
	/*
	if(gridIdx>1){
		digitTrans = targetData.set_val4;
		digitTrans = digitTrans.length + "%";
		targetData.set_val4 = digitTrans;
	}
	*/
	targetData.set_valStr = [];
	
	updateRows.push( JSON.stringify(selectData) );
	updateRows.push( JSON.stringify(targetData) );
	
	$.ajax({
	    url: 'pg03_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	jsonRowdatas:updateRows,
	    	prj_id: pg03.prjInfo.id,
	    	set_code_type:pg03.code_Type[gridIdx]
	    },
	    success: function onData (data) {
	    	getPg03Grid();
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});

	// 그리드 상에서 이동 (true는 up, false는 down)
	selectedRow.move(targetIndex,false);
	
	callback(thisGrid,selectedIndex);
	
}




/**
 * 관리 버튼을 누를 시, 다이얼로그 창이 뜨도록 해준다.
 * @param prjInfo,codeType,i // 프로젝트 정보 
 * @returns void
 */
function pg03_openDialog(prjInfo,codeType,i){
	// i는 0~3까지 중, 몇번째 버튼을 클릭했는지 알기위한 식별자

	
	$( "#pg03_dialog"+i ).dialog({
		modal : true, 
		resizable : true,
		width :800
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
	
	// pg03.dialIsFirst = false;
	pg03.delFlag = false;
	
	
	pg03.dial_Grid[i].deletedIdxList = []; // 삭제된 row의 index
	pg03.dial_Grid[i].deletedIds = []; 	   // 삭제된 row의 실제 code_id
	
	// pg03.dial_Grid[i];
	let digitTrans = pg03.grid[i].getData();
	
	/*
	// 세번째, 네번재 다이얼 그리드만 digitTrans 적용
	if(i>1){
		
		for(let x=0;x<digitTrans.length;x++){
			
			
			let digit = digitTrans[x].set_val4;
			digit = digit.split("%");
			let digitNo = parseInt(digit[0]);
			
			digitTrans[x].set_val4 = "";
			
			for(let d=0; d<digitNo; d++) 
				digitTrans[x].set_val4 += "#";
		}
	}	
	*/
	pg03.dial_Grid[i].setData(digitTrans);	
	
	
}


function pg03_Copy(){
	var thisPrjId = pg03.prjInfo.id;
	var fromPrjId = $("#pg03_selectPrjId").val()
	var idx = $("#pg03_codeType").find(":selected").index();
	
	 
	//var code_type = pg03.CodeType[idx];
	
	if(fromPrjId == null || fromPrjId == "") {
		alert("Copy할 대상 프로젝트를 선택해 주세요");
	}else {
		$.ajax({
		    url: 'pg03_copyCode.do',
		    type: 'POST',
		    traditional : true,
		    data:{
		    	prj_id: thisPrjId,
		    	copy_prj_id: fromPrjId,	    	
		    },
		    success: function onData (data) {
		    	alert('Copy가 완료 되었습니다.');
		    	getPg03Grid();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}


///////////////////////////////////////////////////////


/**
 * Project General 페이지 내에서,
 * 네 번째 스텝 : 필드값 정의
 * @param void 
 * @returns prj_info
 */
//pg04_에서 사용될 전역변수
var pg04 = { isFirst : true };
var tmpGrid;

function pgen_step04(){
	isEdited = false;
	$(".proj_general").attr('id','general04');
	
	$("#pg04_codeType").val("#sel_RevisionNumber").prop("selected",true);
	$("#sel_RevisionNumber").css('display','flex');
	//섹션 들어오면 pg04 셀 지워줌
	$("#sel_DocCat").css('display','none');
	$("#sel_DocType").css('display','none');
	$("#sel_VDocSize").css('display','none');
	$("#sel_VDocCat").css('display','none');
	$("#sel_TRIssue").css('display','none');
	$("#sel_TRReturn").css('display','none');
	$("#sel_TRReturnStep").css('display','none');
	$("#sel_VTRIssue").css('display','none');
	$("#sel_VTRReturn").css('display','none');
	$("#sel_VTRReturnStep").css('display','none');
	$("#sel_STRIssue").css('display','none');
	$("#sel_DocSize").css('display','none');
    
	// 해당 섹션 들어오면 무조건 delFlag를 없애준다.
	// delFalg는 del버튼을 눌렀을 경우의 제어를 위한 플래그
	pg04.delFlag = false;

	
	if(!pg04.isFirst) return;
	
	pg04.isFirst = false;
	
	// 헤더에서 프로젝트 정보를 가져옴
	pg04.prjInfo = getPrjInfo();
	
	
	pg04.CodeType = [];
	for(var i=0;i<$("#pg04_codeType")[0].length;i++){
		var tmp = $("#pg04_codeType")[0][i].text;
		//console.log("pg04.CodeType"+i+"번"+tmp); 
		pg04.CodeType.push(tmp);
	}
	
	
	
	// copy 체크박스 작동부분
	$('#PG04_selected_prj').click(function(){
    	$('#PG04_select_div').addClass('on');
    });
    $('.PG04_prj_select').click(function(){
    	var PG04_selected_prj_id = $(this).attr('id').replace('PG04_prj_id_is_','');
    	$('#PG04_selectPrjId').val(PG04_selected_prj_id);
    	var prj_nm = $(this).children().next().html();
    	$('#PG04_selected_prj').html(prj_nm);
    	$('#PG04_select_div').removeClass('on');
    });
    
    
    $("#pg04_copyChk").click(function(){
    	if($("#pg04_copyChk").is(":checked")) $(".copyDiv04").css('display','flex');
    	else $(".copyDiv04").css('display','none');
    });
    
    $(".copyChkdiv04").click(function(){
    	$("#pg04_copyChk").prop('checked', !$("#pg04_copyChk").is(":checked"));
    	if($("#pg04_copyChk").is(":checked")) $(".copyDiv04").css('display','flex');
    	else $(".copyDiv04").css('display','none');
    });
    
	pg04.btnList = $(".pg04_Btn");
	// COPY 버튼
	$(pg04.btnList[0]).click(function(){ pg04_Copy(); });;
	// refresh
	$(pg04.btnList[1]).click(function(){
		let i = $("#pg04_codeType").find(":selected").index();
		saveCheck04( function(){getPg04Grid(i)} );
		
	});
	// apply
	$(pg04.btnList[2]).click(function(){ pg04_Apply(); });
	//Excel
	$(pg04.btnList[3]).click(function(){ pg04_ToExcel(); });
	// up
	$(pg04.btnList[4]).click(function(){ pg04_Up(function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);})});
	// down
	$(pg04.btnList[5]).click(function(){ pg04_Down(function(thisGrid,targetIndex){thisGrid.selectRow(targetIndex);})});
	// add
	$(pg04.btnList[6]).click(function(){ pg04_Add()});
	// delete
	$(pg04.btnList[7]).click(function(){ pg04_Del()});
	 
	/*
	pg04.CodeType = ['DOC.SIZE',
 					 'DOC.CATEGORY',
 					 'VENDOR DOC.SIZE',
					 'VENDOR DOC.CATEGORY',
					 'TR ISSUE PURPOSE',
					 'TR RETURN STATUS',
					 'TR RETURN STEP',
					 'VTR ISSUE PURPOSE',
					 'VTR RETURN STATUS',
					 'VTR RETURN STEP',
					 'STR ISSUE PURPOSE',
					 'REVISION NUMBER']; // CODE TYPE value
	 */
	pg04.grid = []; // pg04의 각 그리드를 담는 배열
	initPg04Grid(function(i){getPg04Grid(i)});
	
	
	/*
	$("#pg04_codeType").on("click",function(){
		pg04.codeTypeIdx = $("#pg04_codeType").find(":selected").index();
		console.log(pg04.codeTypeIdx);			
	});
	*/
	
	$('#pg04_codeType').each(function() {
	    //Store old value
	    $(this).data('lastValue', $(this).val());
	});
	
	
	
	$("#pg04_codeType").on("change",function(){
		var lastData = $(this).data('lastValue');
		let i = $("#pg04_codeType").find(":selected").index();

		
		if(!saveCheck04(function(){return true})) {
			$(this).val(lastData);
			
			return;
		}
		// 마지막 값을 현재 값으로 갱신
		$(this).data('lastValue', $(this).val());
		getPg04Grid(i);
		
		// 리비전 넘버를 선택한 경우, up,down 비활성화
		if($(this).val() === '#sel_RevisionNumber'){
			$(pg04.btnList[4]).attr('disabled',true);
			$(pg04.btnList[5]).attr('disabled',true);
		}else{
			$(pg04.btnList[4]).attr('disabled',false);
			$(pg04.btnList[5]).attr('disabled',false);
		}
			
	});
	
}
// 각 그리드를 생성하고 전역변수로 할당시켜줌
function initPg04Grid(callback){
	var firstCodeType = $("#pg04_codeType").find(":selected").index();
	for(let i=0;i<$(".pg04_Grids").length;i++){

		tmpGrid = new Tabulator( ('#'+$(".pg04_Grids")[i].id), {
			layout: "fitColumns",
			placeholder:"No Data Set",
			index:"index",
			columns:[
				{
			        title: "init",
			        field: "init",
			        hozAlign:"left",
			        headerSort:false
			    }
			]
			});
		
		pg04.grid.push(tmpGrid);
		// 그리드 생성 후, 콜백함수
		pg04.grid[i].on("tableBuilt", function(){
			$.ajax({
			    url: 'pg04_getStep.do',
			    type: 'POST',
			    data:{
			    	prj_id:pg04.prjInfo.id
			    },
			    success: function onData (data) {
			    	setFieldPg04Grid(i,data)
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		} );
		
		// 값이 수정되면 무조건 true
		pg04.grid[i].on("dataChanged",function(){
			isEdited = true;
		});
		
	}
	
	callback(firstCodeType);
	
}

function setFieldPg04Grid(i,params){
	
	
	let engStep = [];
	let workList = params[0];
	for (let x = 0 ; x<workList.length; x++){
		  let values = {
	            label:workList[x].set_code,
	            value:workList[x].set_code_id
	        };
		  engStep.push(values);
	}
	
	
	
	workList = params[1];
	let vendorStep = {};
	let vendorStepArr = [];
	for (let x = 0 ; x<workList.length; x++)
		vendorStep[workList[x].set_code] = workList[x].set_code_id;
	
	for (let x = 0 ; x<workList.length; x++){
		  let values = {
	            label:workList[x].set_code,
	            value:workList[x].set_code_id
	        };
		  vendorStepArr.push(values);
	}
	
//	workList = params[1];
//	let vendorStep = [];
//	for (let x = 0 ; x<workList.length; x++){
//		 let values = {
//		            label:workList[x].set_code,
//		            value:workList[x].set_code,
//		            val:workList[x].set_code_id
//		        };
//		 vendorStep.push(values);
//	}
	
	workList = params[2];
	let siteStep = {};
	let siteStepArr = [];
	for (let x = 0 ; x<workList.length; x++)
		siteStep[workList[x].set_code] = workList[x].set_code_id;
	
	for (let x = 0 ; x<workList.length; x++){
		  let values = {
	            label:workList[x].set_code,
	            value:workList[x].set_code_id
	        };
		  siteStepArr.push(values);
	}
	
//	workList = params[2];
//	let siteStep = {};
//	for (let x = 0 ; x<workList.length; x++){
//		 let values = {
//		            label:workList[x].set_code,
//		            value:workList[x].set_code,
//		            val:workList[x].set_code_id
//		        };
//		 siteStep.push(values);
//	}
	
	workList = params[3];
	let procurmentStep = {};
	for (let x = 0 ; x<workList.length; x++)
		procurmentStep[workList[x].set_code] = workList[x].set_code_id; 
	
	// 그리드의 기본적인 필드셋
	
	// code_feature는 code를 의미하는 "C" value를 의미하는 "V"로 나뉜다.
	// code_type에 따라 C 혹은 V를 결정지어주어야 한다.
	pg04.grid[i].set_code_feature = "C";
	
	var tmpCol = [
		{title:"index", field:"index", visible:false, headerSort:false},
		// {title:"order", field:"set_order", visible:false},
		{	// 체크박스
			// 커스텀 포매터
			title:'CK',
			formatter: function(cell, formatterParams, onRendered) {
		        const data = cell.getRow().getData();

		         var checkbox = document.createElement("input");
		          checkbox.name = 'pg04_gridChk'+i;
		          checkbox.type = 'checkbox';
		          checkbox.value = data.index;
		          // checkbox.value = data.process_id;
		          return checkbox;		   
			},
			hozAlign:"center", 
			headerSort:false,
			width:10,
			cellClick:function(e, cell){
		        cell.getRow().toggleSelect();
		      }
		},
		{
	        title: "CODE NO.",
	        width:80,
	        field: "set_code",
	        editor:"input",
	        hozAlign:"left",
	        headerSort:false
	    },
	    {
	        title: "DESCRIPTION",
	        field: "set_desc",
	        editor:"input",
	        editable:true,
	        cellEdited:function(cell){
	        	console.log(cell.isEdited());
	        	console.log("hi");
	            },
	        hozAlign:"left",
	        headerSort:false
	    },
	    {
	        title: "CODE VAL.",
	        field: "set_val",
	        editor:"input",
	        hozAlign:"left",
	        visible:false,
	        headerSort:false
	    },
	];
	

	// 코드 타입에 따른 그리드 필드셋 구현
	switch (i) {	  
	  case 0: // REVISION NUMBER
		  tmpCol[2].title = 'REV.NO.';
		  tmpCol[4].title = 'REVISION TYPE';
		  tmpCol[4].visible = true;
		  tmpCol[4].editor = "select";
		  tmpCol[4].editorParams = {values:{"GENERAL":"GENERAL", "COMMENT":"COMMENT"}};
		  break;
	  case 1: // DOC.SIZE
		  tmpCol[4].visible = false;
		  break;
		  
	  case 2: // VENDOR DOC.TYPE
		  tmpCol[4].visible = false;
		  break;
		  
	  case 3: // TR ISSUE PURPOSE
		  tmpCol[4].visible = false;
		  /*
		  tmpCol[4].title = "STEP";
		  tmpCol[4].editor = "select";
		  tmpCol[4].editorParams ={values:engStep};
		  tmpCol[4].formatter = function(cell, formatterParams, onRendered) {
		        const data = cell.getValue();
		       let params = tmpCol[4].editorParams.values;
		       for(let i=0;i<params.length;i++){
		    	   if(params[i].value == data)
		    		   return params[i].label
		       }
		       
		    	return; 
			};
			*/
			
		  break;
		  
	  case 4: // VTR ISSUE PURPOSE
		  tmpCol[4].visible = false;
		  /*
		  tmpCol[4].title = "STEP";
		  tmpCol[4].editor = "select";
		  tmpCol[4].editorParams ={values:vendorStepArr};
		  tmpCol[4].formatter = function(cell, formatterParams, onRendered) {
		        const data = cell.getValue();

		       let params = tmpCol[4].editorParams.values;
		       for(let i=0;i<params.length;i++){
		    	   if(params[i].value == data)
		    		   return params[i].label
		       }
		       
		    	return; 
			};
			*/
		  break;
		  
	  case 5: // STR ISSUE PURPOSE
		  tmpCol[4].visible = false;
		  /*
		  tmpCol[4].title = "STEP";
		  tmpCol[4].editor = "select";
		  tmpCol[4].editorParams ={values:siteStepArr};
		  tmpCol[4].formatter = function(cell, formatterParams, onRendered) {
		        const data = cell.getValue();

		       let params = tmpCol[4].editorParams.values;
		       for(let i=0;i<params.length;i++){
		    	   if(params[i].value == data)
		    		   return params[i].label
		       }
		       
		    	return; 
			};
			*/
		  break;
		  
	  case 6: // DOC.CATEGORY
		  tmpCol[4].visible = false;
		  break;
		  
	
	  case 8: // TR RETURN STEP
		  tmpCol[4].visible = false;
		  //tmpCol[4].editor = false;
		  //tmpCol[4].editorParams = {values:{"GENERAL":"GENERAL", "COMMENT":"COMMENT"}};
		  break;
	
	  case 9: // VTR RETURN STATUS
		  tmpCol[4].visible = false;
		  break;
		  
	  case 10: // DOC.TYPE
		  tmpCol[4].visible = false;
		  break;

		  /*
	  case 12: // REVISION NUMBER
		  tmpCol[2].title = 'REV.NO.';
		  tmpCol[4].title = 'REVISION TYPE';
		  tmpCol[4].editor = "select";
		  tmpCol[4].editorParams = {values:{"GENERAL":"GENERAL", "COMMENT":"COMMENT"}};
		  break;
		  
		  
	  case 6: // TR RETURN STATUS
		  tmpCol[2].title = 'TR.RETURN CODE';
		  tmpCol[4].visible = false;
		  tmpCol[4].title = 'FORECAST DATE';
		  
		  tmpCol[4].formatter = function(cell, formatterParams, onRendered) {
		        const data = cell.getValue();
		        const rowIndex = cell.getData().index;
		        let div = document.createElement('div');
		        
		        let txt1 = "Actual Date + ";
		        let txt2 = " Days";
		        var inputBox = document.createElement("input");
		         	inputBox.type = 'input';
		         	inputBox.id = 'pg04Input_' + rowIndex;
		         	inputBox.style.width = 10;
		         	inputBox.value = data;
		         	inputBox.onchange = function() {
		         		let rowIdx = Number(this.id.replace('pg04Input_',''));
		         		let row = pg04.grid[5].getRow(rowIdx);
		         		let tmpBean = row.getData().bean;
		         		tmpBean.set_val = this.value;
		         		row.update({bean:tmpBean});
		         		row.getCell('set_val').setValue(this.value);		         		
		         	}		         	
		         div.append(txt1);
		         div.append(inputBox);
		         div.append(txt2);
		          // return "Actual Date + " + inputBox + " Days";
		         	return div;
			};
		  break;
		  */
	  default:
		  break;
	}
	
	pg04.grid[i].setColumns(tmpCol);
	
	// 셀 edit 확인
	// 셀의 수정 값을 해당 row의 bean 값에도 반영해준다.
	// 반영된 bean은 apply 시, DB 업데이트할 정보들 이다.
	pg04.grid[i].on("cellEdited",function(cell){
		var fieldName = cell.getField();
    	var tmpBean = cell.getData().bean;
    	tmpBean[fieldName] = cell.getData()[fieldName];
    	cell.getRow().update({bean:tmpBean});
	});
	
}



function getPg04Grid(i){
	// esc 눌렀을 경우, 선택해제 시키는 이벤트 바인딩
	$(document).keydown(function(event) {
	    if ( event.keyCode == 27 || event.which == 27 ) {
	    	pg04.grid[i].deselectRow();
	    }
	});
	
	pg04.delFlag = false;
	
	$.ajax({
	    url: 'pg04_getCode.do',
	    type: 'POST',
	    data:{
	    	prj_id:pg04.prjInfo.id,
	    	set_code_type:pg04.CodeType[i]
	    },
	    success: function onData (data) {
	    	// 테이블 갱신시 edited 여부 초기화
	    	isEdited = false;
	    	// 테이블 갱신
	    	var inputData = [];
	    	// console.log(data[0]);
	    	for(var i=0;i<data[0].length;i++){
	    		var tmp ={};
	    		tmp.index = i;
	    		tmp.bean = data[0][i];
	    		tmp.set_code = data[0][i].set_code;
	    		tmp.set_desc = data[0][i].set_desc;
	    		tmp.set_val = data[0][i].set_val;
	    		inputData.push(tmp);
	    	}
	    	// 코드타입에서 선택된 인덱스와 그리드를 일치시켜준다.
	    	var gridIndex = $("#pg04_codeType").find(":selected").index();
	    	
//	    	console.log("선택한 select = "+gridIndex);
	    	
	    	if(pg04.grid[gridIndex].initialized)
	    		pg04.grid[gridIndex].setData(inputData);
	    	else {
	    		pg04.grid[gridIndex].on("tableBuilt",function(){
	    			this.setData(inputData);
	    		});
	    	}
	    	
	    	// 그리드 row 클릭 이벤트 바인딩
	    	pg04.grid[gridIndex].on("rowClick", function(e, row){
	    		// row 선택 전, 모든 선택된 row 해제
	    		// pg04.grid[gridIndex].deselectRow();
	    		this.deselectRow();
	    		// 더블클릭한 row 선택 활성화
	    		row.select();
	    		//row.toggleSelect();
	    	});
	    	// set_order에 의한 정렬
	    	//pg04.grid[gridIndex].setSort("index","asc");
	    	
	    	// 타입별로 화면에 보여주는 그리드
	    	if(gridIndex == 0) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','flex');
	    	}else if(gridIndex == 1) {
	    		$("#sel_DocSize").css('display','flex');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 2) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','flex');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 3) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','flex');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 4) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','flex');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 5) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','flex');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 6) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','flex');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 7) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','flex');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 8) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','flex');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 9) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','none');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','flex');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}else if(gridIndex == 10) {
	    		$("#sel_DocSize").css('display','none');
	    		$("#sel_DocCat").css('display','none');
	    		$("#sel_DocType").css('display','flex');
	    		$("#sel_VDocSize").css('display','none');
	    		$("#sel_VDocCat").css('display','none');
	    		$("#sel_TRIssue").css('display','none');
	    		$("#sel_TRReturn").css('display','none');
	    		$("#sel_TRReturnStep").css('display','none');
	    		$("#sel_VTRIssue").css('display','none');
	    		$("#sel_VTRReturn").css('display','none');
	    		$("#sel_VTRReturnStep").css('display','none');
	    		$("#sel_STRIssue").css('display','none');
	    		$("#sel_RevisionNumber").css('display','none');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

function pg04_Copy(){
	
	var thisPrjId = pg04.prjInfo.id;
	var fromPrjId = $("#PG04_selectPrjId").val();
	var idx = $("#pg04_codeType").find(":selected").index();
	var code_type = pg04.CodeType[idx];
	
	if(fromPrjId == null || fromPrjId == "") {
		alert("Copy할 대상 프로젝트를 선택해 주세요");
	}else {
		$.ajax({
		    url: 'pg04_copyCode.do',
		    type: 'POST',
		    traditional : true,
		    data:{
		    	prj_id: thisPrjId,
		    	copy_prj_id: fromPrjId,
		    	set_code_type:code_type	    	
		    },
		    success: function onData (data) {
		    	alert('Copy가 완료 되었습니다.');
		    	let idx = $("#pg04_codeType").find(":selected").index();
		    	getPg04Grid(idx);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
	
}

function pg04_ToExcel() {
	var gridIndex = $("#pg04_codeType").find(":selected").index();
	var set_code_type = pg04.CodeType[gridIndex];
//	console.log(set_code_type);
	let gridData = pg04.grid[0].getData();
	
	let jsonList = [];
	
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = "";
	
	if(set_code_type === "DOC.SIZE") {
		fileName = "설계문서_Size_Code";
	}else if(set_code_type == "DOC.CATEGORY") {
		fileName = '설계문서_Category_Code';
	}else if(set_code_type == "DOC.TYPE") {
		fileName = '서신_Type_Code';
	}else if(set_code_type == "VENDOR DOC.SIZE") {
		fileName = 'Vendor문서_Size_Code';
	}else if(set_code_type == "VTR ISSUE PURPOSE") {
		fileName = 'VTR_Issue_Purpose_Code';
	}else if(set_code_type == "VENDOR DOC.CATEGORY") {
		fileName = 'Vendor문서_Category_Code';
	}else if(set_code_type == "TR ISSUE PURPOSE") {
		fileName = 'TR_Issue_Purpose_Code';
	}else if(set_code_type == "TR RETURN STATUS") {
		fileName = 'TR_Return_Status_Code';
	}else if(set_code_type == "VTR ISSUE RETURN STATUS") {
		fileName = 'VTR_Issue_Return_Status_Code';
	}else if(set_code_type == "STR ISSUE PURPOSE") {
		fileName = 'STR_Issue_Purpose_Code';
	}else if(set_code_type == "REVISION NUMBER") {
		fileName = 'REVISON_Number_Code';
	}
//	console.log(jsonList);
	
	var f = document.excelUploadForm_pg04;
//	console.log(pg04.prjInfo.id);
	
	f.prj_id04.value = prj_id;
	f.prj_nm04.value = getPrjInfo().full_nm;
    f.fileName04.value = fileName;
    f.jsonRowdatas04.value = jsonList;
    f.set_code_type04.value = set_code_type;
    f.action = "pg04ExcelDownload.do";
    f.submit();
}

function pg04_Up(callback){
	
	
	
	var gridIndex = $("#pg04_codeType").find(":selected").index();
	var thisGrid = pg04.grid[gridIndex];
	
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getPrevRow()) return;
	
	var selectedIndex = selectedRow.getIndex();
	var targetIndex = selectedRow.getPrevRow().getIndex();
	
	
	var selectedBean = thisGrid.getRow(selectedIndex).getData().bean;
	var selectedOrd = Number(selectedBean.set_order);
	var targetBean = thisGrid.getRow(targetIndex).getData().bean;
	var targetOrd = Number(targetBean.set_order);
	
	selectedBean.set_order = targetOrd;
	targetBean.set_order = selectedOrd;

	thisGrid.getRow(selectedIndex).update({bean:selectedBean});
	thisGrid.getRow(targetIndex).update({bean:targetBean});

	
	var updateRows = [];


	updateRows.push( JSON.stringify(thisGrid.getRow(selectedIndex).getData().bean) );
	updateRows.push( JSON.stringify(thisGrid.getRow(targetIndex).getData().bean) );
	
	selectedRow.move(targetIndex,true);
	
	$.ajax({
	    url: 'pg04_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg04.prjInfo.id,
	    	jsonRowdatas:updateRows
	    },
	    success: function onData (data) {
	    	thisGrid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	callback(thisGrid,selectedIndex);
	
	
	
	
}

function pg04_Down(callback){
	

	var gridIndex = $("#pg04_codeType").find(":selected").index();
	var thisGrid = pg04.grid[gridIndex];
	
	var selectedRow = thisGrid.getSelectedRows();
	if(selectedRow.length < 1) return;
	
	selectedRow = selectedRow[0];
	if(!selectedRow.getNextRow()) return;
	
	
	
	// 선택한 row의 인덱스
	var selectedIndex = selectedRow.getIndex();
	// 바꿀 row의 인덱스 (아래)
	var targetIndex = selectedRow.getNextRow().getIndex();
	
	// 바뀐 row의 실제 값들을 반영해야 하기 때문에 만듬
	var selectedBean = thisGrid.getRow(selectedIndex).getData().bean;
	var selectedOrd = Number(selectedBean.set_order);
	var targetBean = thisGrid.getRow(targetIndex).getData().bean;
	var targetOrd = Number(targetBean.set_order);
	
	selectedBean.set_order = targetOrd;
	targetBean.set_order = selectedOrd;
	
	thisGrid.getRow(selectedIndex).update({bean:selectedBean});
	thisGrid.getRow(targetIndex).update({bean:targetBean});

	
	var updateRows = [];


	updateRows.push( JSON.stringify(thisGrid.getRow(selectedIndex).getData().bean) );
	updateRows.push( JSON.stringify(thisGrid.getRow(targetIndex).getData().bean) );

	// 그리드 상에서 이동 (true는 up, false는 down)
	selectedRow.move(targetIndex,false);
	

	$.ajax({
	    url: 'pg04_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg04.prjInfo.id,
	    	jsonRowdatas:updateRows
	    },
	    success: function onData (data) {
	    	thisGrid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	callback(thisGrid,selectedIndex);
	
	
}

function pg04_Apply(){
	
	var gridIndex = $("#pg04_codeType").find(":selected").index();
	var thisGrid = pg04.grid[gridIndex];
	
	var gridList = thisGrid.getData();
	// 비어있는 그리드는 제외
	if(!pg04.delFlag && gridList.length < 1) return;
	
	var changedList = thisGrid.getEditedCells();
	var changedData = [];
	for(var i = 0 ; i<changedList.length ; i++){
		var tmpIdx = changedList[i].getData().index
		changedData.push(tmpIdx);
	}
	
	// 변화가 있는 row들 json으로 변환
	var tmpStrings = [];
	var tmpGridData = thisGrid.getData();
	
	for(var i=0;i<changedData.length;i++){
		var rowIndex = changedData[i];
		tmpStrings.push( JSON.stringify(tmpGridData[rowIndex].bean) );
	}
	
	// 삭제된 row의 set_code_id를 배열로 넘겨준다.
	var deletedIds = [];
	if(pg04.delFlag)
	for(var i=0;i<thisGrid.deletedIds.length;i++){
		// 삭제된 row의 code id
		deletedIds.push(thisGrid.deletedIds[i]);
	}
	
//	console.log(deletedIds);
	
	$.ajax({
	    url: 'pg04_updateCode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg04.prjInfo.id,
	    	set_code_type:pg04.CodeType[gridIndex],
	    	jsonRowdatas:tmpStrings,
	    	deleteRows:deletedIds
	    },
	    success: function onData (data) {
	    	alert("Successfully saved");
	    	getPg04Grid(gridIndex);
	    	isEdited = false;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});		
	
	thisGrid.deletedIdxList = [];
	
	// 삭제 후, 반영 완료하였으니 delFlag 다시 초기화
	pg04.delFlag = false;
	
	
		
		
		
	
	
}

function pg04_Add(){
	
	// 그리드 값을 찾는다.
	var gridIndex = $("#pg04_codeType").find(":selected").index();
	var thisGrid = pg04.grid[gridIndex];
	
	var lastIndex = thisGrid.getData().length;
	var newCodeId = pg04_getMaxId(thisGrid);
	newCodeId = String(Number(newCodeId) + 1).padStart(5,"0");
	var newSetOrd = pg04_getMaxOrder(thisGrid)+1;
	
	var tmp = {index:lastIndex,set_code:"",set_val:"",bean:{}};
	tmp.bean.prj_id = pg04.prjInfo.id;
	tmp.bean.set_code_type = pg04.CodeType[gridIndex];
	tmp.bean.set_code_id = newCodeId;
	tmp.bean.set_order = newSetOrd;
	tmp.bean.set_code_feature = thisGrid.set_code_feature;
	
	thisGrid.addData(tmp,false,lastIndex);
	thisGrid.redraw();

	
}

// 현재 그리드에서 code값의 최대값을 찾는다.
function pg04_getMaxId(grid){
	
	var max = 0;
	var gridData = grid.getData();
	
	for(var i=0 ; i < gridData.length ; i++){
		var tmpBean = gridData[i].bean;
		if(Number(tmpBean.set_code_id) > max) max = Number(tmpBean.set_code_id);
	}
	
	
	return String(max).padStart(5,"0");
	
}

function pg04_getMaxOrder(grid){
	
	var max = 0;
	var gridData = grid.getData();
	
	for(var i=0 ; i < gridData.length ; i++){
		var tmpBean = gridData[i].bean;
		if(tmpBean.set_order > max) max = tmpBean.set_order;
	}
	
	
	return max;
	
}

function pg04_Del(){
	var gridIndex = $("#pg04_codeType").find(":selected").index();
	var thisGrid = pg04.grid[gridIndex];
	
	thisGrid.deletedIdxList = []; // 삭제된 row의 index
	thisGrid.deletedIds = []; // 삭제된 row의 실제 code_id
	// 체크박스 selector name
	var chkString = "input:checkbox[name='pg04_gridChk" + gridIndex + "']:checked";
	var chk = $(chkString);
	let deletechk = '';
	let set_code_string = '';
	
	var delChkIdxList = [];
	
	for(var i=0;i<chk.length;i++){
		var idx = Number(chk[i].value); // index 숫자로 변환
		delChkIdxList.push(idx);
		deletechk += thisGrid.getData()[idx].bean.set_code_id + ',';
		set_code_string += thisGrid.getData()[idx].bean.set_code + ',';
	}
	deletechk = deletechk.slice(0,-1);
	set_code_string = set_code_string.slice(0,-1);
	
	
	let delRowsJSON = [];
	for(let i=0;i<delChkIdxList.length;i++){
		let tmp = thisGrid.getRowFromPosition(delChkIdxList[i]).getData(); 
		delRowsJSON.push(JSON.stringify(tmp.bean));
	}
		


	$('.pop_deletePg04Confirm').dialog("open");
	$('#deleteCodeYes').click(function() {
		$('.pop_deletePg04Confirm').dialog("close");
		// 바로삭제 적용
		 $.ajax({
				type: 'POST',
				url: 'pg04_del.do',
				traditional : true,
				data:{
					jsonRowdatas : delRowsJSON
				},
				async:false,
				success: function(data) {
					let delval = data.model.delval;
					getPg04Grid(gridIndex);
					if(delval.length>0)
						alert("사용중인 코드를 제외하고 삭제 완료");
					else
						alert("delete complete.");
				},
			    error: function onError (error) {
			        console.error(error);
			    }
			});
	});
    $('#deleteCodeCancel').click(function() {
    	$(".pop_deletePg04Confirm").dialog("close");
    });
	
	
	
	/*
	if(gridIndex===0){
	 $.ajax({
			type: 'POST',
			url: 'isUseRevNo.do',
			data:{
				prj_id:pg04.prjInfo.id,
				set_code_id:deletechk,
				set_code:set_code_string
			},
			async:false,
			success: function(data) {
				if(data[0]==='삭제 가능'){
					for(var i=0;i<chk.length;i++){
						var idx = Number(chk[i].value); // index 숫자로 변환
						thisGrid.deletedIds.push(thisGrid.getData()[idx].bean.set_code_id);
						thisGrid.deletedIdxList.push(idx);
					}
					thisGrid.deleteRow(thisGrid.deletedIdxList);
					pg04.delFlag = true;
					// delete 버튼 누를 시 바로 반영 해야 하면 아래 코드 주석해제 및 코드작성
					// thisGrid.deletedIdxList = [];
				}else{
					alert(data[0]);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
	*/

}


//////////////////////////////////////////////////////////
/**
 * Project General 페이지 내에서,
 * 일봅 번째 스텝 : 필드값 정의
 * @param void 
 * @returns prj_info
 */
//pg07_에서 사용될 전역변수

var pg07 = { isFirst : true, isAddMode : true};

function pgen_step07(){
	$(".proj_general").attr('id','general07');
    
	// 해당 섹션 들어오면 무조건 delFlag를 없애준다.
	// delFalg는 del버튼을 눌렀을 경우의 제어를 위한 플래그
	pg07.delFlag = false;
	isEdited = false;
	
	if(!pg07.isFirst) return;
	
	pg07.isFirst = false;
	
	
	// 헤더에서 프로젝트 정보를 가져옴
	pg07.prjInfo = getPrjInfo();
	pg07_setDialog();
	pg07.codeType = $("#pg07_codeType")[0][0].text;
	pg07.selectedData = {};
	
	$.ajax({
	    url: 'pg07_getMaxId.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg07.prjInfo.id
	    },
	    success: function onData (data) {
	    	pg07.selectedData.prj_id = pg07.prjInfo.id;
	    	pg07.selectedData.discip_code_id = data;
	    	pg07.selectedData.discip_code = "";
	    	pg07.selectedData.discip_display = "";
	    	pg07.selectedData.discip_desc = "";
	    	pg07.selectedData.discip_folder_id_List = [];
	    	pg07.selectedData.folder_name_List = [];
	    	pg07_select(pg07.selectedData);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	// copy 체크박스 작동부분
	$('#pg07_selected_prj').click(function(){
    	$('#pg07_select_div').addClass('on');
    });
    $('.pg07_prj_select').click(function(){
    	var pg07_selected_prj_id = $(this).attr('id').replace('pg07_prj_id_is_','');
    	$('#pg07_selectPrjId').val(pg07_selected_prj_id);
    	var prj_nm = $(this).children().next().html();
    	$('#pg07_selected_prj').html(prj_nm);
    	$('#pg07_select_div').removeClass('on');
    });
    
    
    $("#pg07_copyChk").click(function(){
    	if($("#pg07_copyChk").is(":checked")) $(".copyDiv").css('display','flex');
    	else $(".copyDiv").css('display','none');
    });
    
    $(".copyChkdiv").click(function(){
    	$("#pg07_copyChk").prop('checked', !$("#pg07_copyChk").is(":checked"));
    	if($("#pg07_copyChk").is(":checked")) $(".copyDiv").css('display','flex');
    	else $(".copyDiv").css('display','none');
    });
    
	pg07.btnList = $(".pg07_Btn");
	pg07.inputList = $(".pg07_input");
	
	// COPY 버튼
	$(pg07.btnList[0]).click(function(){ pg07_Copy(); });;
	
	// add
	$(pg07.btnList[1]).click(function(){ pg07_Add()});
	// save
	$(pg07.btnList[2]).click(function(){ pg07_Save(); });
	// delete
	$(pg07.btnList[3]).click(function(){ pg07_Del()});
	// match
	$(pg07.btnList[4]).click(function(){ pg07_Match()});
	
	
	pg07.grid = []; // pg07의 각 그리드를 담는 배열
	initPg07Grid(function(i){getPg07Grid(i)});

}

//각 그리드를 생성하고 전역변수로 할당시켜줌
function initPg07Grid(callback){
	
	var defaultColumns = [
	    {field:"prj_id", visible:false , headerSort:false},
	    {field:"discip_code_id", visible:false , headerSort:false},
	    {field:"discip_code", visible:false , headerSort:false},
	    {field:"discip_display", visible:false , headerSort:false},
	    {field:"discip_desc", visible:false , headerSort:false},
	    {field:"discip_order", visible:false , headerSort:false},
	    {field:"discip_folder_id", visible:false , headerSort:false},
	    {field:"discip_folder_seq", visible:false , headerSort:false},
	    {field:"discip_folder_id_List", visible:false , headerSort:false},
	    {field:"folder_name_List", visible:false , headerSort:false},
	    {field:"reg_id", visible:false , headerSort:false},
	    {field:"reg_date", visible:false , headerSort:false},
	    {field:"mod_id", visible:false , headerSort:false},
	    {field:"mod_date", visible:false , headerSort:false},
	];
	for(let i=0;i<$(".pg07_Grids").length;i++){
		var tmpGrid = new Tabulator( ($(".pg07_Grids")[i]), {
			layout: "fitColumns",
			placeholder:"No Data Set",
			index:"index",
			columns:defaultColumns
			});
		

		pg07.grid.push(tmpGrid);
	
		
		// 그리드 생성 후, 콜백함수
		pg07.grid[i].on("tableBuilt", function(){
			// 필드 셋 구성
			setFieldPg07Grid(pg07.grid,i);
			
			// 그리드 row 클릭 이벤트 바인딩
			pg07.grid[i].on("rowClick", function(e, row){
	    		// row 선택 전, 모든 선택된 row 해제
	    		// pg04.grid[gridIndex].deselectRow();
	    		this.deselectRow();
	    		// 더블클릭한 row 선택 활성화
	    		row.select();
	    		pg07_select(row.getData());
	    		//row.toggleSelect();
	    	});
			
			// esc 눌렀을 경우, 선택해제 시키는 이벤트 바인딩
			$(document).keydown(function(event) {
			    if ( event.keyCode == 27 || event.which == 27 ) {
			    	pg07_Add();
			    }
			});
			// DB에서 값을 가져옴
			getPg07Grid();
			
			// 값의 수정여부 체크
			pg07.grid[i].on("datachanged",function(){
				isEdited = true;
			});
		} );
	
		
		
	}
	
}


function setFieldPg07Grid(grid,i){
	
	 grid[i].getColumn('discip_code').updateDefinition({title:"CODE", visible:true,vertAlign:"middle" , width:10});
	 grid[i].getColumn('discip_display').updateDefinition({title:"DISCIPLINE DISPLAY",visible:true,vertAlign:"middle",width:150});
	 grid[i].getColumn('discip_desc').updateDefinition({title:"DESCRIPTION",visible:true,vertAlign:"middle",width:200});
	 // grid[i].getColumn('discip_folder_id').updateDefinition({title:"FOLDER DISCIPLINE MATCHING",visible:true});
	 // grid[i].getColumn('discip_folder_id_List').updateDefinition({title:"FOLDER DISCIPLINE MATCHING",visible:true});
	 grid[i].getColumn('folder_name_List').updateDefinition(
			 {title:"FOLDER DISCIPLINE MATCHING",visible:true,vertAlign:"middle",hozAlign: "left",
				 formatter:function(cell, formatterParams, onRendered){
					 	const data = cell.getData();
					 	let folderList = data.folder_name_List;
					 	
					 	let result = "";
					 	if(folderList)
					 	for(let i=0;i<folderList.length;i++)
					 		result += (folderList[i] + '<br>');
					 	
					 
					    return result; //return the contents of the cell;
					}});
	 
	 
	
	
}

function getPg07Grid(){
	
	// db에서 그리드에 들어갈 데이터를 가져옴
	$.ajax({
	    url: 'pg07_getDiscp.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg07.prjInfo.id
	    },
	    success: function onData (data) {
	    	pg07.grid[0].setData(data[0]);	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

function pg07_select(data){
//	console.log(data);
	// input에 값을 매핑해줄 worker
	let worker = pg07.inputList;
	pg07.selectedData = data;
	
	worker[0].value = data.discip_code;
	worker[1].value = data.discip_display;
	worker[2].value = data.discip_desc;
	
	
	// 폴더 리스트 출력부분
	let folderList = data.folder_name_List;
	let tmp = "";
	for(let i=0; i < folderList.length ; i++)
		tmp+=(folderList[i] + ( i<folderList.length-1 ? "\n" : "" ) );
	
	worker[3].value = (tmp === "/" ? "" : tmp);
	worker[3].rows = data.length;
	
	pg07.isAddMode = false;
	
	worker.change(function(){
		isEdited = true;
	});
	
}

function pg07_deSelect(){
	// input에 값을 매핑해줄 worker
	pg07.selectedData = {};
	pg07.selectedData.discip_folder_id_List = [];
	let worker = pg07.inputList;
	for(let i = 0 ; i < worker.length ; i++)
		worker[i].value = "";
	pg07.grid[0].deselectRow();
}

// Btn 이벤트 메소드 
function pg07_Copy(){
	
	let thisPrjId = getPrjInfo().id
	let fromPrjId = $("#pg07_selectPrjId").val();
	
	if(fromPrjId == null || fromPrjId == "") {
		alert("Copy할 대상 프로젝트를 선택해 주세요");
	}else {
		// 복사 전, 해당 폴더에 문서가 사용중인지 여부 확인을 위함
		let folders = [];
		let gridData = pg07.grid[0].getData();
		
		for(let i=0;i<gridData.length;i++){
			let tmp = gridData[i].discip_folder_id_List;
			folders.push(tmp);
		}
		
		
		$.ajax({
		    url: 'pg07_copyCode.do',
		    type: 'POST',
		    traditional : true,
		    data:{
		    	prj_id: thisPrjId,
		    	copy_prj_id: fromPrjId,
		    	folders:folders
		    },
		    success: function onData (data) {
		    	alert('Copy가 완료 되었습니다.');
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
	
}
function pg07_Add(){
	// 추가 버튼을 누를 때마다 row 추가되는 것을 방지
	if(pg07.isAddMode) return;
	
	// Add mode On
	pg07.isAddMode = true;
	// 선택여부 해제, 새로운 row focus
	pg07_deSelect(); 
	pg07.tree.jstree("uncheck_all");
	pg07.tree.jstree("close_all");
	pg07.tree.jstree("open_node",'1');
	
	$.ajax({
		    url: 'pg07_getMaxId.do',
		    type: 'POST',
		    traditional : true,
		    data:{
		    	prj_id: pg07.prjInfo.id
		    },
		    success: function onData (data) {
		    	pg07.selectedData.prj_id = pg07.prjInfo.id;
		    	pg07.selectedData.discip_code_id = data;
		    	pg07.selectedData.discip_code = "";
		    	pg07.selectedData.discip_display = "";
		    	pg07.selectedData.discip_desc = "";
		    	pg07.selectedData.discip_folder_seq_List = [];
		    	pg07.selectedData.discip_folder_id_List = [];
		    	pg07.selectedData.folder_name_List = [];
		    	pg07_select(pg07.selectedData);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});

	
}

function pg07_Save(){
	
	let worker = pg07.inputList;
	
	
	pg07.selectedData.discip_code = worker[0].value;
	pg07.selectedData.discip_display = worker[1].value;
	pg07.selectedData.discip_desc = worker[2].value;
	
//	console.log(pg07.selectedData);
	
	$.ajax({
	    url: 'pg07_update.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg07.prjInfo.id,
	    	jsonRowdata : JSON.stringify(pg07.selectedData)
	    },
	    success: function onData (data) {
	    	if(data != "SUCCESS") alert(data);
	    	alert("successfully saved");
	    	isEdited = false;
	    	getPg07Grid();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	
	// Add mode Off
	pg07.isAddMode = false;
}

function pg07_Del(){

	$.ajax({
	    url: 'pg07_delDiscip.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: pg07.prjInfo.id,
	    	jsonRowdata : JSON.stringify(pg07.selectedData)
	    },
	    success: function onData (data) {    	
	    	getPg07Grid();
	    	pg07_deSelect();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}


function pg07_setDialog(){
	let folderPath = getFolderList();
	folderPath = dccFolder(folderPath);
	
	pg07.tree = $('#pg07_tree');
	
	pg07.tree.jstree({
	     "core" : {
	    	 'data' : folderPath,
	    	 'tie_selection' : false,
	         "check_callback" : true
	       },
	       "checkbox" : {
				"keep_selected_style" : false,
	            three_state: false,
	    	  },
	      "sort" : function(a, b) {
	           a = this.get_node(a);
	           b = this.get_node(b);
	           if(a.icon != 'resource/images/pinwheel.png' && a.original.type == 'DCC'){
	        	   return (a.text > b.text) ? 1 : -1;		           
	        	  }
	         },
	      "plugins" : [ "contextmenu" ,"checkbox","sort"]
	    });
	
	

	// 다이얼로그 창의 select버튼에 onclick() 부여
	$("#pg07_folderSelect").click(function(){ 
			let checked = pg07.tree.jstree("get_checked");
			checked.sort();
			pg07.selectedData.discip_folder_id_List = checked;
			
			$.ajax({
			    url: 'pg07_getFolderPath.do',
			    type: 'POST',
			    traditional : true,
			    data:{
			    	prj_id: pg07.prjInfo.id,
			    	discip_folder_id_List : pg07.selectedData.discip_folder_id_List
			    },
			    success: function onData (data) {
			    	isEdited = true;
//			    	console.log(data);
			    	let tmp = "";
			    	for(let i=0; i < data.length ; i++)
			    		tmp+=(data[i] + ( i<data.length-1 ? "\n" : "" ) );
			    	
			    	pg07.inputList[3].value = tmp;
			    	pg07.inputList[3].rows = data.length;
			    	// 다이얼로그 창 닫기
					$( "#pg07_dialog" ).dialog('close');
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});			
			
		});
}

function pg07_Match(){
	
	$( "#pg07_dialog" ).dialog({
		modal : true, 
		resizable : false,
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
	let tmpFolderIdList = pg07.selectedData.discip_folder_id_List;
	for(let i = 0 ; i < tmpFolderIdList.length;i++)
	pg07.tree.jstree("open_node", tmpFolderIdList[i] , openParent(tmpFolderIdList[i]));

	pg07.tree.jstree("uncheck_all");
	pg07.tree.jstree("check_node",pg07.selectedData.discip_folder_id_List);
}

function openParent(node_id){
	let parentId = (pg07.tree.jstree(true).get_node(node_id)).parent;
	if(parentId)
		pg07.tree.jstree("open_node", node_id , openParent(parentId));
}

function removeTabPG(){
	$(".pop_mailType").remove();
	$(".pop_EmailTypeDelete").remove();
	$(".pop_mailNotice").remove();
	$(".pop_userSearchM").remove();
	

	$(".pop_rvsRS").remove();
    $(".pop_IFCCodeDelete").remove();
    $('.pop_groupEdit').remove();
    $('.pop_PrjGroupDelete').remove();
    // document 이벤트 모두 제거
    //$(document).off();
}


function clonedeep(obj) {
	  var clone = {};
	  for (var key in obj) {
	    if (typeof obj[key] == "object" && obj[key] != null) {
	      clone[key] = clonedeep(obj[key]);
	    } else {
	      clone[key] = obj[key];
	    }
	  }

	  return clone;
	}