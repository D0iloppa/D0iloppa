let uploadFileSize;

function popCheckIn(row){
	checkInFlag = true;
	// 초기화
	checkInVar = {};	
	
	checkInVar.rowData = row.getData().bean;

	
	let revTxt = $("select[name=revList] option:checked").text();

	$(".pop_checkin_tbl")[0].innerText = checkInVar.rowData.doc_no;
	$(".pop_checkin_tbl")[1].value = checkInVar.rowData.title;
	$(".pop_checkin_tbl")[2].innerText = checkInVar.rowData.rev_no;
	$(".pop_checkin_tbl")[4].innerText = checkInVar.rowData.rfile_nm;
	// 파일 클리어
	$(".pop_checkin_tbl")[5].value = '';
	$("input[name='checkIn_File']").val('');
	// 기본적으로 체크 해제
	$("#checkIn_fileChgChk")[0].checked = false;
	
	// IFC 단계인지 확인
	$(".isIFC").css("display","none");
	
	$.ajax({
	    url: 'isIFCStep.do',
	    type: 'POST',
	    traditional : true,
	    async:false,
	    data:{
	    	prj_id: getPrjInfo().id,
	    	doc_id: checkInVar.rowData.doc_id,
	    	rev_id: checkInVar.rowData.rev_id,
	    	doc_type: checkInVar.rowData.doc_type
	    },
	    success: function onData (data) {
	    	console.log(data.model.IFC);
	    	checkInVar.IFC = data.model.IFC;
	    	if(checkInVar.IFC == "Y"){
	    		$("#checkInIFCList option").remove();
	    		$("#checkInIFCList").append("<option value=''></option>");
	    		let ifc_list = data.model.IFC_List;
	    		for(let i = 0 ; i<ifc_list.length ; i++){
	    			$("#checkInIFCList").append("<option value='"+ifc_list[i].set_code_id+"'>" + ifc_list[i].set_code + "</option>");
	    		}
	    		$(".isIFC").css("display","");
	    	}
	    		
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'getUsedRevNo.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: getPrjInfo().id,
	    	doc_id: checkInVar.rowData.doc_id
	    },
	    success: function onData (data) {
	    	console.log(data);
	    	usedRev(data[0],data[1]);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

function checkIn_check(){
	  if($(".pop_checkin_tbl")[3].checked) $(".pop_checkin_tbl")[5].click();
      else {
      	$(".pop_checkin_tbl")[4].innerText = checkInVar.rowData.rfile_nm;
      	// 파일 초기화
      	$(".pop_checkin_tbl")[5].value = '';
      }
}

/**
 * 해당 doc에 사용했던 rev를 검색하여 옵션에서 제거
 * @returns
 */
function usedRev(data,originList){
	// option clear
	$("select[name='revList'] option").remove();
	// 디폴트 값 추가
	$("select[name='revList']").append('<option value=""></option>');
	// 기본 rev no 리스트 옵션 생성
	for(let i=0;i<originList.length;i++){
		$("select[name='revList']").append('<option value="'+originList[i].set_code_id+'">'+originList[i].set_code+'</option">');
	}
	
	
	let usedRevList = data;
	
	// 사용한 rev no 제거
	for(let i=0;i<usedRevList.length;i++)
		$("select[name=revList] option[value='"+usedRevList[i]+"']").remove();
	
	// 모달 창 실행
	$(".pop_checkin").dialog({resizable:false,width: 900}).dialogExtend({
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
	}).bind('dialogclose', function(event, ui) {
		checkInFlag = false;
	});
}

function ifcChg(){
	let label = $("select[name=ifcList] option:checked").text();
	$(".pop_checkin_tbl")[1].value = "["+label+"] " + checkInVar.rowData.title;
}

function chgFile(){
	
	//console.log($(".pop_checkin_tbl")[5].files[0]);
	
	let uploadFileNm = $(".pop_checkin_tbl")[5].files[0].name;
	uploadFileSize = $(".pop_checkin_tbl")[5].files[0].size;
	let chgFile_str;
	//let chgFile_str = (checkInVar.rowData.rfile_nm ? checkInVar.rowData.rfile_nm : "none") + " → " + uploadFileNm;
	if(checkInVar.rowData.rfile_nm)
		chgFile_str = checkInVar.rowData.rfile_nm + " → " + uploadFileNm;
	else
		chgFile_str = "(new) " + uploadFileNm;
	
	$(".pop_checkin_tbl")[4].innerText = chgFile_str;
	
}

function btn_checkIn(){
	// 필수입력값 체크
	// Changefile 하지 않았을 경우 return; 
	if(!$("#checkIn_fileChgChk")[0].checked){
		alert("You have to input data - Attach file");
		return;
	}
	
	
	if(checkInVar.IFC == "Y"){
		let ifcVal = $("#checkInIFCList").val();
		if(ifcVal == ""){
			alert("[IFC STEP] : you have to choose the ifc revised reason");
			return;
		}
			
	}
	
	
	// 새로운 rev_id
	// let rev_max = (Number(checkInVar.rowData.rev_id)+1+"").padStart(3,'0');
	
	// 리비전 넘버 입력하지 않을시 체크인 되지 않아야함
	let rev_no = $("#checkInRevList").val();
	if(!rev_no){
		alert("you have to choose the revision number");
		return;
	}else if(rev_no == ''){
		alert("you have to choose the revision number");
		return;
	}
	
	
	// 파일 체크
	let form = $('#checkInForm')[0];
    let formData = new FormData(form);
    // 프로젝트 id form 데이터에 추가
    formData.set("prj_id",getPrjInfo().id);
    formData.set("folder_id",current_folder_id);
    // doc id form 데이터에 추가
    formData.set("doc_id",checkInVar.rowData.doc_id);
    formData.set("doc_no",checkInVar.rowData.doc_no);
    formData.set("title", $(".pop_checkin_tbl")[1].value);
    formData.set("rev_id", checkInVar.rowData.rev_id);
    // formData.set("rev_max", rev_max);
    formData.set("rev_code_id",$("#checkInRevList").val());
    formData.set("ifcReason_Code",$("#checkInIFCList").val());
    formData.set("reg_id",session_user_id);
    formData.set("mod_id",session_user_id);
    //formData.set("doc_type",current_folder_docType);
    formData.set("doc_type",current_doc_type);
    formData.set("origin_rev_code_id", $('#pop_checkin_origin_rev_no').html());
    
    
    
    
    $.ajax({             
    	type: "POST",          
        enctype: 'multipart/form-data',  
        url: "checkIn.do",        
        data:formData,          
        processData: false,    
        contentType: false,      
        cache: false,           
        //timeout: 600000,
        beforeSend:function(){
			$('.pop_checkin').prepend(progressbar);
        },
		complete:function(){
			$('.pop_checkin .progressDiv').remove();
		}, 
		xhr: function () {
            //XMLHttpRequest 재정의 가능
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
              //progress 이벤트 리스너 추가
              var percent = (e.loaded * 100) / e.total;
              $('.pop_checkin .progressDiv .bar').css('width', percent+'%');
            };
            return xhr;
        },
        success: function (result) {
        	if(result.model.status != 'SUCCESS'){
        		// error exception alert
        		alert(result.model.fail)
        		return;
        	}
        	console.log("uploadFileSize = "+uploadFileSize);
        	$.ajax({
    			url: 'insertAccHis.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
    		    type: 'POST',    // 이동할때 포스트 방식으로 이동
    		    data:{				// 이동할때 챙겨야하는 데이터
    		    	prj_id: getPrjInfo().id,
    		    	doc_id: checkInVar.rowData.doc_id,
    		    	rev_id: checkInVar.rowData.rev_id,
    		    	doc_no: checkInVar.rowData.doc_no,
    		    	doc_title: checkInVar.rowData.title,
    		    	folder_id: checkInVar.rowData.folder_id,
    		    	file_size: uploadFileSize,
    		    	method: "CHECK-IN"
    		    },
    		    success: function onData (data) {
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		        alert("오류");
    		    }
    		});
        		
        	alert("Successfully, Check In");
        	// 체크해제
        	$("#checkIn_fileChgChk")[0].checked = false;
        	currentSort = documentListTable.getSorters();
        	searchDocByFilter();
    		$(".pop_checkin").dialog("close");
    		return;
        },          
        error: function (e) {  
        	console.log("ERROR : ", e);     
            alert("fail");
         }     
	}); 
    
    
    
    
    
    
    /*
    $.ajax({
		url: 'checkInChkFile.do',
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
				
			}else {
				alert("Unsupported file format. Please contact the administrator");
			}
		},
		error: function onError (error) {
	    	console.error(error);
//	        alert("등록 오류");
	    }
	}); 
    
    */
    
}
function checkInCloseBtn(){
	checkInFlag = false;
	$('.pop_checkin').dialog('close');
}