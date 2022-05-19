
/**
 * 파일 업로드 버튼을 눌렀을 경우,
 * 모달창을 띄우고 multi upload 작업을 수행
 * @returns
 */

var DC_MultiUpload;
function dcMultiUpload(){
	
	if(current_doc_type != ''){
		if(current_doc_type != 'General'){
			alert("Can't upload general file to this folder.");
			return;
		}
	}
	
	let _prj_id = getPrjInfo().id;
	let folder_id = current_folder_id;
	
	let auth = checkFolderAuth(_prj_id,folder_id);
	
	if(auth.w == 'N'){
		alert("There is no WRITE permision to FILE UPLOAD");
		return;
	}
	
	
	// 업로드 관련 변수 초기화
	DC_MultiUpload = {};
    
	$(".pop_upload").dialog({resizable:false,width: 800}).dialogExtend({
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
    $(".pop_upload").dialog("open");
    
    
    
    DC_MultiUpload.addBtn = $(".dc_multiUpload")[0];
    DC_MultiUpload.delBtn = $(".dc_multiUpload")[1];
    DC_MultiUpload.fileCount = $(".dc_multiUpload")[2];
    DC_MultiUpload.fileInput = $(".dc_multiUpload")[3];
    DC_MultiUpload.uploadBtn = $(".dc_multiUpload")[4];
    DC_MultiUpload.closeBtn = $(".dc_multiUpload")[5];
    
    initUploadGrid();
    
}
/**
 * 파일 리스트를 보여주는 그리드 창
 * @returns
 */
function initUploadGrid(){
	DC_MultiUpload.grid = new Tabulator("#multiUploadTbl_d", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		index:"index",
		selectable:true,
		height: 200,
		columns: [			
			{ title:"index",field:"index",visible:false},
			{ title:"file",field:"file",visible:false},
			{
				title: "File Name",
				field: "file_name"
			},
			{
				title: "Title",
				field: "title",
				editor:"input"
			},
			{
				title: "Last Modified Date",
				field: "last_date"
			},
			{
				title: "Path",
				field: "file_path"
			}
		],
	});
	
	DC_MultiUpload.grid.on("tableBuilt", function(){

		
	});
} 

/**
 * 파일이 올라왔을 때의 이벤트 처리
 * @param obj
 * @returns
 */
function dc_multiFileUp(obj){
	getFileCount(obj.files);
}

function getFileCount(files){
	
	let lastIdx = DC_MultiUpload.grid.getData().length;
	let numberOfFiles = files.length;
	let totalSize = 0;
	let fileList = [];
	
	for(let i=0;i<files.length;i++){
		let fileRow = {};
		fileRow.file = files[i];
		totalSize += fileRow.file.size;
		let title = files[i].name.split(".")[0];
		
		fileRow.index = i + lastIdx;
		fileRow.file_name = files[i].name;
		fileRow.last_date = files[i].lastModifiedDate.toLocaleString();
		fileRow.title = title;
		fileRow.file_path = "C://fakePath//" + fileRow.file_name;
		fileList.push(fileRow);
	}
		
	
	let outputLabel = numberOfFiles + " files (" + formatBytes(totalSize, 2) +")"; 
	// filecount 설정
	DC_MultiUpload.fileCount.innerText = outputLabel;
	DC_MultiUpload.grid.addData(fileList);
	// input form 초기화
	// 최종 업로드는 그리드에 담겨있는 파일들을 넘겨준다.
	DC_MultiUpload.fileInput.value = "";

	 

}



/**
 * 파일 추가 버튼
 * @returns
 */
function dcUpload_addBtn(){
	document.getElementsByName("uploadfile")[0].click();
}
function dcUpload_delBtn(){
	let selectedRow = DC_MultiUpload.grid.getSelectedRows();
	for(let i=0;i<selectedRow.length;i++)
		DC_MultiUpload.grid.deleteRow(selectedRow[i].getIndex());
	
	
	
	
}

// 그리드의 row의 file을 input에 담아준다.
function dcUpload_uploadBtn(){
	if(DC_MultiUpload.grid.getData().length<1){
		alert("There is no file for upload");
		return;
	}
		
	
	// 그리드의 file들을 input에 set해주기 위한 라이브러리
	const dataTransfer = new DataTransfer();
	
	let dataList = DC_MultiUpload.grid.getData();
	let titleStr = "";
	
	for(let i=0;i<dataList.length;i++){
		dataTransfer.items.add(dataList[i].file);
		titleStr += (dataList[i].title?dataList[i].title:" ") + "@@"
	}
	titleStr = titleStr.substring(0,titleStr.length-2);
			
	
	DC_MultiUpload.fileInput.files = dataTransfer.files;
	
	// form 데이터 전송
	let form = $('#dc_multiUpload_form')[0];
    let formData = new FormData(form);
    
    formData.set("prj_id",getPrjInfo().id);
    // doc id form 데이터에 추가
    formData.set("folder_id",current_folder_id);
    formData.set("reg_id",session_user_id);
    formData.set("doc_type",current_folder_docType);
    formData.set("jsonString",titleStr);
    
    //loadingWithMask();
    
    $.ajax({             
    	type: "POST",          
        enctype: 'multipart/form-data',  
        url: "multiUpload.do",        
        data: formData,          
        processData: false,    
        contentType: false,      
        cache: false,
        beforeSend:function(){
			$('.pop_upload').prepend(progressbar);
        },
		complete:function(){
			$('.pop_upload .progressDiv').remove();
		},
		xhr: function () {
            //XMLHttpRequest 재정의 가능
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
              //progress 이벤트 리스너 추가
              var percent = (e.loaded * 100) / e.total;
              $('.pop_upload .progressDiv .bar').css('width', percent+'%');
            };
            return xhr;
        },
        // timeout: 600000,       
        success: function (result) {
        	//closeLoadingWithMask();
        	
        	if(result.model.status=="SUCCESS"){
        		getPrjDocumentIndexList();
        		alert("Upload Success");
        		$(".pop_upload").dialog("close");
        		return;
        	}
        	else alert("Upload Fail : " + result.model.fail);
        	
        },          
        error: function (e) {  
        	console.log("ERROR : ", e);     
            alert("fail");
            //closeLoadingWithMask();
         }     
	}); 
    
	
	
}


function dcUpload_closeBtn(){
	$(".pop_upload").dialog("close");
}

