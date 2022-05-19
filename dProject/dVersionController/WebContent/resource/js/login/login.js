$(function(){
	//비밀번호 변경 팝업 초기화
	$("#changePw").dialog({
	    autoOpen: false,
	    width:400
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
	$('.pop_passwdResetIdIs').dialog({
	    autoOpen: false,
	    width:400
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
	
	$(document).on("click",".pop_close",function(){
	    $(this).closest(".dialog").dialog("close");
	    return false;
	});
	$(".chg_pw").on("click", function() {
	    $("#changePw").dialog("open");
	    $('#CP_USER_ID').val('');
	    $('#CP_CURRENT_PW').val('');
	    $('#CP_NEW_PW').val('');
	    $('#CP_NEW_PW_CONFIRM').val('');
	    $('#pwdCheckWarning').html('');
	    $('#pwdConfirmWarning').html('');
	    return false;
	});
	
	$(".pop_popInform").dialog({
		 draggable: true,
	     autoOpen: false,
	     maxWidth:1200,
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
	
	$(".pop_detailP2").dialog({
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
});
$(document).on('keypress',function(e) {
    if(e.which == 13) {
    	loginCheck();
    }
});
$(document).ready(function(){
	pwChange();
	getPubBbs();
});

//팝업창 상단 x 아이콘
$(document).on('click','.ui-dialog-titlebar-close',function(){
//	$('.ui-widget-overlay').css('display','none');
	
//	console.log(bbs_seq);
	
	getPubBbs();
	
	$("#title2").val(null);
	$('#detailViewFileWrapN_Login').empty();
});

function loginCheck(){
	var chkAdmin = $("#userid").val().toUpperCase();
//	if(chkAdmin == "ADMIN"){
//		var userId = $('#userid').val().toUpperCase();
//		var currentPwd = $('#password').val();
//		$.ajax({
//		    url: 'getFirstLoginYn.do',
//		    type: 'POST',
//		    data: {
//		    	user_id:userId,
//		    	passwd:currentPwd
//		    },
//		    success: function onData (data) {
//		    	firstLoginContorl(data,userId,currentPwd);
//		    },
//		    error: function onError (error) {
//		        console.error(error);
//		    }
//		});
//	}else {
		// IP 체크 할려고 추가
		$.ajax({
		    url: 'getChcekIpAddr.do',
		    type: 'POST',
		    async: false,
		    data: {
		    	user_id: chkAdmin
		    },
		    success: function onData (data) {
		    	if(data == 0) {
		    		alert("This IP is not allowed to access.\n Please contact the administrator.");
		    		return;
		    	}else {
		    		var userId = $('#userid').val().toUpperCase();
		    		var currentPwd = $('#password').val();
		    		$.ajax({
		    		    url: 'getFirstLoginYn.do',
		    		    type: 'POST',
		    		    data: {
		    		    	user_id:userId,
		    		    	passwd:currentPwd
		    		    },
		    		    success: function onData (data) {
		    		    	firstLoginContorl(data,userId,currentPwd);
		    		    },
		    		    error: function onError (error) {
		    		        console.error(error);
		    		    }
		    		});
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	//}
}
function firstLoginContorl(data,userId,currentPwd){	
	if(data==='Y'){
		//최초로그인시 실행
		
		$('#CP_USER_ID').val(userId);
		$('#CP_CURRENT_PW').val(currentPwd);
		$("#changePw").dialog("open");
	} else if(data==='N'){

		var userId = $('#userid').val().toUpperCase();
		var chkAdmin = $("#userid").val().toUpperCase();
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
			    	
			    	$.ajax({
						url: 'checkPassPolicy.do',
					    type: 'POST',
					    async: false,
					    data: {
					    	user_id:userId
					    },
					    success: function onData (data) {
					    	// 최초 로그인시 p_user_connect에 아이디,최초 로그인 날짜등 insert, update
					    	//console.log("data = "+data);
					    	
					    	if(data > 1) {
					    		alert("The password matches, but the number of password accesses has been exceeded. \n Please change your password");
					    		return;
					    	}else if(data > 0 && data < 2) {
					    		alert("The password matches, but the password validity period has expired. \n Please change your password");
					    		return;
					    	}else {		    		    		
					    		let pwd = $('#password').val();
					    		$.ajax({
					    		    url: 'invalidPwd.do',
					    		    type: 'POST',
					    		    data: {
					    		    	passwd:pwd
					    		    },
					    		    success: function onData (data) {
					    		    	if(data[0]==='부적합'){
					    		    		alert('비밀번호에 적합하지 않은 문자를 입력하셨습니다.');
					    		    	}else{
					    		    		$.ajax({ // 로그인시 p_prj_sys_log에 정보 저장
					    		    			url: 'insertLoginSystemLog.do',
								    		    type: 'POST',
								    		    data: {
								    		    	user_id:userId,
								    		    	pgm_nm: "로그인",
								    		    	method: "로그인"
								    		    },
								    		    success: function onData () {
								    		    	document.getElementById('loginForm').submit();	
								    		    },
								    		    error: function onError (error) {
								    		        console.error(error);
								    		    }
					    		    		});							    		
					    		    	}
					    		    },
					    		    error: function onError (error) {
					    		        console.error(error);
					    		    }
					    		});
					    	}
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
	} else {
		// 로그인 실패시
		var userId = $('#userid').val().toUpperCase();
			$.ajax({
				url: 'upConnFailCnt.do',
			    type: 'POST',
			    async: false,
			    data: {
			    	user_id:userId
			    },
			    success: function onData (data) {
			    	// 로그인 실패시 connect_fail_cnt 증가
			    	//console.log("data = "+data);
			    	$.ajax({
						url: 'checkPassPolicy.do',
					    type: 'POST',
					    async: false,
					    data: {
					    	user_id:userId
					    },
					    success: function onData (data) {
					    	// 최초 로그인시 p_user_connect에 아이디,최초 로그인 날짜등 insert, update
					    	//console.log("data = "+data);
					    	
					    	if(data > 1) {
					    		alert("The number of password accesses has been exceeded. \n Please change your password");
					    		return;
					    	}else if(data > 0 && data < 2) {
					    		alert("The password validity period has expired. \n Please change your password");
					    		return;
					    	}
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
		$('#userinfonoexist').html('일치하는 정보가 없습니다.');
	}
}


function pwdCheck(){
	var count = 0;
	var passwd = $('#CP_NEW_PW').val();
	const regUpper = /[A-Z]/;
	const regLower = /[a-z]/;
	const regNum = /[\d]/;
	const regSpc = /[`~!@#$%^*()\-_=+]/;
	
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
	var passwd = $('#CP_NEW_PW').val();
	var passwd_confirm =$('#CP_NEW_PW_CONFIRM').val();
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
function pwChange(){
	$('#PW_CHANGE').click(function () {
		var USER_ID = $('#CP_USER_ID').val().toUpperCase();
		var current_pwd = $('#CP_CURRENT_PW').val();
		
		$.ajax({
		    url: 'getUserPw.do',
		    type: 'POST',
		    data: {
		    	user_id:USER_ID,
		    	passwd:current_pwd
		    },
		    success: function onData (data) {
		    	if(data[0]=='일치'){
		    		var new_pwd = $('#CP_NEW_PW').val();
		    		var new_pwd_confirm = $('#CP_NEW_PW_CONFIRM').val();
		    		if(new_pwd == ''){
		    			alert('새 비밀번호를 입력하세요');
		    		}else if(new_pwd_confirm == ''){
		    			alert('비밀번호 확인을 입력하세요');
		    		}else if($('#pass_reg').val()==='N'){
		    			alert('비밀번호 규칙에 적합하지 않습니다.');
		    		}else if(new_pwd != new_pwd_confirm){
		    			alert('비밀번호가 일치하지 않습니다');
			    	}else{
			    		$.ajax({
			    		    url: 'invalidPwd.do',
			    		    type: 'POST',
			    		    data: {
			    		    	passwd:new_pwd
			    		    },
			    		    success: function onData (data) {
			    		    	if(data[0]==='부적합'){
			    		    		alert('비밀번호에 적합하지 않은 문자를 입력하셨습니다.');
			    		    	}else{
						    		$.ajax({
						    		    url: 'firstLoginPost.do',
						    		    type: 'POST',
						    		    data: {
						    		    	user_id:USER_ID,
						    		    	passwd:new_pwd
						    		    },
						    		    success: function onData (data) {
								    		alert('비밀번호를 변경했습니다. 다시 로그인 해주세요.');
								    		
								    		// 비밀번호 변경시 최초 로그인한 날짜, 비밀번호 틀린 회수 초기화
								    		$.ajax({
												url: 'resetDateFail.do',
											    type: 'POST',
											    async: false,
											    data: {
											    	user_id:USER_ID
											    },
											    success: function onData (data) {
											    },
											    error: function onError (error) {
											        console.error(error);
											    }
											});
								    		
								    		location.reload();
						    		    },
						    		    error: function onError (error) {
						    		        console.error(error);
						    		    }
						    		});
			    		    	}
			    		    },
			    		    error: function onError (error) {
			    		        console.error(error);
			    		    }
			    		});
		    		}
		    	}else{
		    		alert('일치하는 정보가 없습니다.');
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
}
function resetPwd(){
	$('.pop_passwdResetIdIs').dialog('open');
}
function passwdResetConfirm(){
	var USER_ID = $('#pwdResetId').val().toUpperCase();
	$.ajax({
	    url: 'checkUserId.do',
	    type: 'POST',
	    data: {
	    	user_id:USER_ID
	    },
	    success: function onData (data) {
	    	if(data[0]=='존재'){
	    		$.ajax({
	    		    url: 'passwdReset.do',
	    		    type: 'POST',
	    		    data:{
	    		    	user_id: USER_ID
	    		    },
	    		    success: function onData (data) {
	    		    	if(data[0]>0 && data[1]>0){
	    			    	alert('새 비밀번호를 이메일로 보내드렸습니다. 이메일을 확인해주세요.');
	    			    	$('.pop_passwdResetIdIs').dialog("close");
	    			    	$('#changePw').dialog('close');
	    		    	}else{
	    		    		alert('Password Reset Error!');
	    		    	}
	    		    },
	    		    error: function onError (error) {
	    		        console.error(error);
	    		    }
	    		});
	    	}else{
	    		alert('해당되는 ID가 존재하지 않습니다.');
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
var getBbs_seq = 0;
function getPubBbs(){
	$.ajax({
	    url: 'getPubBbsContents.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var tbody = '';
	    	if(data[0].length==0){
	    		tbody += '<tr><td colspan="5">데이터가 없습니다.</td></tr>';
	    	}else{
		    	for(i=0;i<data[0].length;i++){
		    		var code_nm = data[0][i].code_nm;
		    		var creater_nm;
		    		if(code_nm == null) {
		    			creater_nm = data[0][i].user_kor_nm;
		    		}else {
		    			creater_nm = data[0][i].user_kor_nm+'('+code_nm+')';
		    		}
		    		getBbs_seq = data[0][i].bbs_seq;
		    		tbody += '<tr id=\"bbsSeq_'+getBbs_seq+'\" onclick=\"pop_detailP('+getBbs_seq+');\"><td>'+(data[0].length-i)+'</td>';
		    		tbody += '<td class="pc" style="text-align: left;">'+creater_nm+'</td>'
		    				+'<td class="pc">'+data[0][i].hit_cnt+'</td><td>'+getDateFormat_YYYYMMDD(data[0][i].reg_date)+'</td><td style="text-align: left;">'+data[0][i].title+'</td></tr>';
		    		if(i==4){
		    			break;
		    		}
		    	}
	    	}
	    	$('#loginPubBbs').html(tbody);
	    	$('#loginPubBbs').css({'cursor':'pointer'});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// 행 더블 클릭시 상세페이지
function pop_detailP(getBbs) {
//	console.log("bbs_seq = "+getBbs);
	$('#detailViewFileWrapN_Login').empty();
	$(".pop_detailP2").dialog("open");
	
	$.ajax({
		url: 'getPubBbsContents.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
//	    dataType: 'json',
	    data:{				// 이동할때 챙겨야하는 데이터
	    	bbs_seq: getBbs
	    },
	    success: function onData (data) {
	    	var code_nm = data[0].code_nm;
	    	var creater_nm;
    		if(code_nm == null) {
    			creater_nm = data[0].user_kor_nm;
    		}else {
    			creater_nm = data[0].user_kor_nm+'('+code_nm+')';
    		}
			$('#title2').val(data[0].title); 
			$('#writer2').val(creater_nm);
			$('#createdate2').val(getDateFormat_YYYYMMDD(data[0].reg_date));
			$('#hits2').val(data[0].hit_cnt);
			$('#contents2').html(data[0].contents);
			
			detailFileList(getBbs);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function detailFileList(getBbs) {
	$.ajax({
		url: 'getDetailFileList.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	bbs_seq: getBbs
	    },
	    success: function onData (data) {
	    	
	    	var html = "";

	    	if(data[0].length == 0 || data[0][0] == null){
	    		html += "<div id=\"emptyFile\">첨부 파일이 없습니다.</div>";
	    		
	    		$("#detailViewFileWrapN_Login").append(html);
	    	}else {
	    		for(var i=0; i<data[0].length; i++) {
		    		
		    		var fIndex = data[0][i].file_seq;
		    		var fileName = data[0][i].rfile_nm;
		    		var fileSize = data[0][i].file_size / 1024 / 1024;
		    		
//		    		console.log(fIndex,fileName,fileSize);
		    		
		    		var fileSizeTxt = fileSize;
		    		
		    		if(fileSize < 1) {
		    			fileSizeTxt = (fileSize * 1024).toFixed(1) + "KB";
		    		}else {
		    			fileSizeTxt = (fileSize).toFixed(1) + "MB";
		    		}
		    		
		    		html += "<ul class=\"file-ul detail\">";
//		    		html += "<li title='"+fileName+"'style=\"width:88%\"><span onclick=\"fileDownload('"+data[0][i].file_path + data[0][i].sfile_nm+"','"+fileName+"');\"><i class=\"far fa-file\"></i>"+fileName+"</span></li>";
		    		html += "<li title='"+fileName+"'style=\"width:88%\"><span onclick=\"fileDownload('"+data[0][i].sfile_nm+"','"+fileName+"');\"><i class=\"far fa-file\"></i>"+fileName+"</span></li>";
		    		html += "<li>"+fileSizeTxt+"</li>";
		    		html += "</ul>";
		    	}
		    	
		    	$("#detailViewFileWrapN_Login").append(html);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

//상세페이지 파일리스트 클릭시 다운
function fileDownload(sFileNm, rFileNm) {
	var $form = $('<form></form>');
	var sfileName = $('<input type="hidden" name="sfile_nm" value="' + sFileNm + '">');
	var fileName = $('<input type="hidden" name="rfile_nm" value="' + rFileNm + '">');
	
	$form.attr('action', 'estimateDownloadN.do');
	$form.attr('method', 'post');
	$form.appendTo('body');
	$form.append(sfileName);
	$form.append(fileName);
	
	$form.submit();
}

function cancledetail() {
	$(".pop_detailP2").dialog("close");
	$('#detailViewFileWrapN_Login').empty();
	$('.ui-widget-overlay').css('display','none');
	
	getPubBbs();
	
//	return false;
}

// 개인정보처리방침 팝업창
function popInform() {
//	console.log("팝업창 띄우기");
	$(".pop_popInform").dialog("open");
}

function canclepopInform() {
	$(".pop_popInform").dialog("close");
}
function passwdResetCancel(){
	$('.pop_passwdResetIdIs').dialog("close");
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