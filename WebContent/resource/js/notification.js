$(function(){
	prj_id = $('#NotiPrjId').val();
	id = prj_id;
	let folder_id = $('#NotiFolderId').val();
	current_folder_id = folder_id;
});

function notificationDocumentControl(){
	let folder_path = $('#NotiFolderPath').val();
	let folder_path_array = folder_path.split('>');
	for(let i=0;i<folder_path_array.length;i++){
		$('#tree').jstree('open_node', $('a#'+folder_path_array[i]+'_anchor'));
		$('a#'+folder_path_array[i]+'_anchor').click();
	}
}