var table;

$(document).ready(function(){
	getpasswdPolicy();
	
});

//function DayUseYn() {
//	var get_expire_day_use_yn = $('#pswd_expire_day_use_yn').val();
////	console.log($('#pswd_expire_day_use_yn').val());
//	
//	if(get_expire_day_use_yn === 'N') {
//		$('#pswdchangDate').val('');
//		alert('변경');
//	}
//	
//}

function getpasswdPolicy() {
	$.ajax({
		url: 'getpasswdPolicy.do',
	    type: 'POST',
		success: function onData (data) {
//			
			var passwd_expire_day_use_yn= data[0].passwd_expire_day_use_yn;
			var passwd_expire_day= data[0].passwd_expire_day;
			var passwd_fail_acc_stop_use_yn= data[0].passwd_fail_acc_stop_use_yn;
			var passwd_fail_acc_stop_cnt= data[0].passwd_fail_acc_stop_cnt;
			
			$('#pswd_expire_day_use_yn').val(passwd_expire_day_use_yn);
			$('#pswdchangDate').val(passwd_expire_day);
			$('#pswd_fial_stop_use_yn').val(passwd_fail_acc_stop_use_yn);
			$('#pswdchangCnt').val(passwd_fail_acc_stop_cnt);
//			console.log(passwd_expire_day_use_yn);
		},
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function PswdApply() {
	var set_expire_yn = $('#pswd_expire_day_use_yn').val();
	var set_expire_day = $('#pswdchangDate').val();
	var set_fail_acc_stop_yn = $('#pswd_fial_stop_use_yn').val().trim();
	var set_fail_acc_stop_cnt = $('#pswdchangCnt').val().trim();
	
//	if(set_expire_yn === 'N' && set_expire_day != '' || set_fail_acc_stop_yn === 'N' && set_fail_acc_stop_cnt != '') {
//		alert('사용 여부를 확인해 주세요. ');
//	}else {
		if(set_expire_yn === 'Y' && set_expire_day < 30) {
			alert('유효기간은 최소 30일 이상으로 작성해주세요.');
		}else if(set_fail_acc_stop_yn === 'Y' && set_fail_acc_stop_cnt < 3) {
			alert('유효회수는 최소 3회 이상으로 작성해주세요.');
		}else {
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "PASSWORD POLICY",
    		    	method: "저장"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			
			$.ajax({
				url: 'updatepasswdPolicy.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
			    type: 'POST',    // 이동할때 포스트 방식으로 이동
			    data: {
			    	passwd_expire_day_use_yn: set_expire_yn,
			    	passwd_expire_day: set_expire_day,
			    	passwd_fail_acc_stop_use_yn: set_fail_acc_stop_yn,
			    	passwd_fail_acc_stop_cnt: set_fail_acc_stop_cnt
			    },
			    success: function onData (data) {
			    	if(data > 1){
						alert('수정이 완료 되었습니다.');			    		
			    	}else {
			    		alert('저장이 완료 되었습니다.');
			    	}
			    	// 테이블 갱신
			    	getpasswdPolicy();
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
//	}
	
}








