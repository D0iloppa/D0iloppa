$(function(){
	getTreeType09();
});

var treeCheck = 0;
var pg_FolderList;
var newFlag = false;
function getTreeType09(){
	pg_FolderList = getFolderList();

	var pasteDisable = 1;
	var moveFolderId = 0;
	function jstreeContextMenu(node) {
//		const prj_id = $('#headerPrjSelect').val();
		var tree = $("#folder_allow").jstree(true);

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
	                   $('#folder_allow').on('rename_node.jstree', function (e, data) {
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
			        			    	tree.settings.core.data = data1;
			        			    	tree.refresh();
			        			    	
			        			    	//폴더별 권한 설정이 아닌 전체 트리 리프레시
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
	            	$('#folder_allow').on('rename_node.jstree', function (e, data) {
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
		        			    	var data = getFolderList();
		        			    	tree.settings.core.data = data;
		        			    	tree.refresh();
		        			    	
		        			    	//폴더별 권한 설정이 아닌 전체 트리 리프레시
		        					var data2 = getFolderList();
		        					$("#tree").jstree(true).settings.core.data = data2;
		        					$("#tree").jstree(true).refresh();
		    		            	$(".pop_deletefolder").dialog("close");
		    		            	pg_FolderList = getFolderList();
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
	    if (node.parent=='#') {
	    	items.createItem._disabled = true;
	    	items.renameItem._disabled = true;
	    	items.deleteItem._disabled = true;
	    	items.cutItem._disabled = true;
	    	items.pasteItem._disabled = true;
	    	items.indexRegisterItem._disabled = true;
	    	if (node.original.type=='TRA') {
		    	items.authControlItem._disabled = true;
		    }
	    	
	    }else if (node.original.type=='DCC') {
	    	if(pasteDisable==1){
		    	items.createItem._disabled = true;
		    	items.renameItem._disabled = true;
		    	items.deleteItem._disabled = true;
		    	items.cutItem._disabled = true;
	    		items.pasteItem._disabled = true;
		    	items.indexRegisterItem._disabled = true;
	    	}else{
		    	items.pasteItem._disabled = false;
	    	}
	    }else if (node.original.type=='TRA') {
	    	items.authControlItem._disabled = true;
	    	
	    	if(pasteDisable==1){
		    	items.createItem._disabled = true;
		    	items.renameItem._disabled = true;
		    	items.deleteItem._disabled = true;
		    	items.cutItem._disabled = true;
	    		items.pasteItem._disabled = true;
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
	    
    	if(auth.w == 'N'){
    		items.createItem._disabled= true;
    		items.renameItem._disabled= true;
    		items.cutItem._disabled= true;	    
    		items.authControlItem._disabled =true;
    	}
    	
    	if(auth.d == 'N'){
    		items.deleteItem._disabled= true;   		
    	}
    	
	    return items;
	}
	
	// $('#folder_allow').jstree(true).settings.core.data = folderList;
	var tree = $('#folder_allow').jstree({
	     "core" : {
	    	 'data' : pg_FolderList,
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
	    }).on('dblclick.jstree', function(event) {});
	
	$('#folder_allow').jstree(true).refresh();
	
}

function folderRightsRefresh() {
	var tree = $("#folder_allow").jstree(true);
	var data1 = getFolderList();
	//data1 = dccFolder(data1);
	tree.settings.core.data = data1;
	tree.refresh();
	
	//폴더별 권한 설정이 아닌 전체 트리 리프레시
	var data2 = getFolderList();
	$("#tree").jstree(true).settings.core.data = data2;
	$("#tree").jstree(true).refresh();
}