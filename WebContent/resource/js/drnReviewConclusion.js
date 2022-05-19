var prj_id = $('#selectPrjId').val();
var drnR_codeType = $("#drnReviewcodeType")[0][0].text;
var drnR_codeId;

$(document).ready(function(){
	
	$('#prjEdit').css('display','none');
	$('#prjCreation').css('display','inline-block');
	
	$("#drnR_copyChk").click(function(){
    	if($("#drnR_copyChk").is(":checked")) $(".copyDiv").css('display','flex');
    	else $(".copyDiv").css('display','none');
    });
	
	getDrnReviewCodeList();
});

$('#copyCheck').click(function(){
	
	if($('#copyCheck').is(':checked')) {
		$('#copyBtn').css('display','flex');
	}else{
		$('#copyBtn').css('display','none');
	}
});

function getDrnReviewCodeList() {
//	console.log("prj_id = "+prj_id);
//	console.log("drnR_codeType = "+drnR_codeType);
	
	$.ajax({
		url: 'drnR_getCode.do',
	    type: 'POST',
	    data: {
	    	prj_id: prj_id,
	    	set_code_type: drnR_codeType
	    },
	    success: function onData (data) {
	    	$('#review_conclusion').val(data[0].set_desc);
	    	drnR_codeId = data[0].set_code_id;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// 카피 버튼
function Copy() {
	var drnR_desc = $('#review_conclusion').val();
	
	var fromPrjId = $('#copyProject').val();
	
	$.ajax({
		url: 'drnR_getCodeCopy.do',
	    type: 'POST',
	    data: {
	    	prj_id: prj_id,
	    	copy_prj_id: fromPrjId,
	    	set_code_type: drnR_codeType,
	    	set_code_id: drnR_codeId,
	    	set_desc: drnR_desc
	    },
	    success: function onData (data) {
	    	if(data == 1){
	    		alert("Copy가 불가능한 프로젝트입니다.");
	    	}else if(data == -1){
	    		alert("Copy가능한 데이터가 없습니다.");
	    	}else {
	    		alert("Copy가 완료되었습니다.");
	    	}
	    	getDrnReviewCodeList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// 값이 있으면 수정 없으면 저장
function apply() {
	
	var drnR_desc = ($('#review_conclusion').val()).trim();	
	//console.log("drnR_codeType = "+drnR_codeType);
	
//	var chkbox = $('#copyCheck').is(':checked');	
//	console.log("chkbox = "+chkbox);  true / false
	
	var fromPrjId = $('#copyProject').val();
	
	if(drnR_desc == null || drnR_desc == "") {
		alert("저장하실 내용을 입력해주세요.");
	}else {
		$.ajax({
			url: 'drnR_getCodeUp.do',
		    type: 'POST',
		    data: {
		    	prj_id: prj_id,
		    	set_code_type: drnR_codeType,
		    	set_code_id: drnR_codeId,
		    	set_desc: drnR_desc
		    },
		    success: function onData (data) {
		    	if(data == 1){
		    		alert("등록이 완료되었습니다.");
		    	}else if(data == 2) {
		    		alert("수정이 완료되었습니다.");
		    	}else {
		    		alert("등록을 실패했습니다.");
		    	}
		    	getDrnReviewCodeList();
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}






