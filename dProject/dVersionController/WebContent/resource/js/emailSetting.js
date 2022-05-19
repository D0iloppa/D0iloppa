// 특수문자 제거
var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;

$(function(){
//	$(".pop_emailSet").dialog({
//        draggable: true,
//        autoOpen: false,
//        maxWidth:1000,
//        width:"auto",
//        modal: true
//    });
//	
//	$(document).on("click", "#emailSet", function() {
//        $(".pop_emailSet").dialog("open");
//        return false;
//    });
	
	getUserEmialList();
	getSaveUserEmail();
});

function removeTabEMAILSETTING(){
	//$(".pop_emailSet").remove();
}

function getSaveUserEmail() {
	$.ajax({
	    url: 'getEmailSetting.do',
	    type: 'POST',
	    success: function onData (data) {
	    	//console.log("데이터 확인 = "+data[0].email_receiver_addr);
	    	//console.log("데이터 확인 = "+data[0].email_user_id);
	    	//console.log("데이터 확인 = "+data[0]);
	    	if(data[0] != null) {
	    		var emailUserIdArray = data[0].email_user_id.split(',');
	    		var emailReceiverArray = data[0].email_receiver_addr.split(';');
	    		//console.log("데이터 확인 = "+emailUserIdArray.length);
		    	//console.log("데이터 확인 = "+emailReceiverArray.length);
	    		let html  = '';
		    	for(let i=0; i<emailUserIdArray.length; i++) {
		    		//console.log(data[0][i]);
		    		html += '<li class="'+emailUserIdArray[i].replace(regExp, "")+'">'+emailUserIdArray[i]+'&nbsp;<button onclick="selectedEMAILUserDelete(`'+emailUserIdArray[i]+'`,`'+emailUserIdArray[i].replace(regExp, "")+'`,`'+emailReceiverArray[i]+'`)" class="btn_icon btn_blue"><i class="fas fa-times"></i><span>삭제</span></button></li>';
		    	}
		    	$('#DCCSETEMAILLIST').html(html);
				$('#IDEMAILSETLIST').val(data[0].email_user_id);
				$('#EMAILLIST').val(data[0].email_receiver_addr);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getUserEmialList() {
	$.ajax({
	    url: 'getUsersVOList.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var userList = [];
	    	for(i=0;i<data[0].length;i++){
	    		userList.push({
                    No: i+1,
		            userId: data[0][i].user_id,
		            KorName: data[0][i].user_kor_nm,
		            EngName: data[0][i].user_eng_nm,
		            email: data[0][i].email_addr,
		            company: data[0][i].company_nm
	    		});
	    	}
	    	setUserEmailList(userList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function usersEmailSetSearch(){
	var searchUser = $('input[name="searchUserEmail"]:checked').val();
	var searchKeyword2 = $('#usersEmailSearchSetKeyword').val();
		$.ajax({
		    url: 'searchUsersVO.do',
		    type: 'POST',
		    data:{
		    	searchUser:searchUser, // 체크박스 값
		    	searchKeyword:searchKeyword2 // 검색창 값
		    },
		    success: function onData (data) {
		    	var userList = [];
		    	for(i=0;i<data[0].length;i++){
		    		userList.push({
	                    No: i+1,
			            userId: data[0][i].user_id,
			            KorName: data[0][i].user_kor_nm,
			            EngName: data[0][i].user_eng_nm,
			            email: data[0][i].email_addr,
			            company: data[0][i].company_nm
		    		});
		    	}
		    	setUserEmailList(userList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
}
function setUserEmailList(userList){
    var userEmailtable = new Tabulator("#userEmailList", {
        selectable:1,//true
        data: userList,
        layout: "fitColumns",
        placeholder:"No Data Set",
		pagination : true,
		paginationSize : 30,
        height:400,
        columns: [{
                title: "No",
                field: "No",
                width: 50
            },
            {
                title: "User ID",
                field: "userId",
                hozAlign: "left"
            },
            {
                title: "Kor.Name",
                field: "KorName",
                hozAlign: "left"
            },
            {
                title: "Eng.Name",
                field: "EngName",
                hozAlign: "left"
            },
            {
                title: "E-mail",
                field: "email",
                hozAlign: "left"
            },
            {
                title: "Company",
                field: "company",
                hozAlign: "left"
            }
        ]
    });
    
    userEmailtable.on("rowSelected", function(row){
    	let selectedDCC2 = $('#EMAILSETLIST').val();
    	let temp2 = $('#EMAILNAMELIST').children();
    	let selectedDCCNameArray2 = [];
    	for(let i=0;i<temp2.length;i++){
    		selectedDCCNameArray2.push($('#EMAILNAMELIST').children()[i].innerHTML);
    	}
    	let selectedDCCName2 = selectedDCCNameArray2.join(',');
    	let selectedDCCEmail2 = $('#EMAILLIST').val();
    	
    	let selectedDCCIdEmail2 = $('#IDEMAILSETLIST').val();
    	
    	if(selectedDCCIdEmail2!=''){
	    	selectedDCC2 += ',';
	    	selectedDCCName2 += ',';
	    	selectedDCCEmail2 += ';';
	    	selectedDCCIdEmail2 += ',';
    	}
		selectedDCC2 += row.getData().userId;
		selectedDCCName2 += row.getData().KorName;
		selectedDCCEmail2 += row.getData().email;
		selectedDCCIdEmail2 += row.getData().userId +'('+row.getData().email+')';
		
    	if(selectedDCCIdEmail2!=''){
			var arraySelectedDCC2 = selectedDCC2.split(',');
			var arraySelectedDCCName2 = selectedDCCName2.split(',');
			var arraySelectedDCCEmail2 = selectedDCCEmail2.split(';');
			var arraySelectedDCCIdEmail2 = selectedDCCIdEmail2.split(',');
			//console.log("arraySelectedDCCIdEmail2 = "+arraySelectedDCCIdEmail2);
			selectedDCC2 = [...new Set(arraySelectedDCC2)];
			selectedDCCName2 = [...new Set(arraySelectedDCCName2)];
			selectedDCCEmail2 = [...new Set(arraySelectedDCCEmail2)];
			selectedDCCIdEmail2 = [...new Set(arraySelectedDCCIdEmail2)];
			//console.log("selectedDCCIdEmail2 = "+selectedDCCIdEmail2);
			
			
			let html2 = '';
			let namehtml2 = '';
			for(let i=0;i<selectedDCCIdEmail2.length;i++){
				//console.log("아이디 = "+selectedDCC[i]);
				//console.log("이메일 = "+selectedDCCEmail[i]);
				
				//if(selectedDCCEmail2[i] != undefined) {
					html2 += '<li class="'+selectedDCCIdEmail2[i].replace(regExp, "")+'">'+selectedDCCIdEmail2[i]+'&nbsp;<button onclick="selectedEMAILUserDelete(`'+selectedDCCIdEmail2[i]+'`,`'+selectedDCCIdEmail2[i].replace(regExp, "")+'`,`'+selectedDCCEmail2[i]+'`)" class="btn_icon btn_blue"><i class="fas fa-times"></i><span>삭제</span></button></li>';
					//namehtml2 +=  '<li class="'+selectedDCC2[i]+'" style="float:left; margin-left:5px; margin-top:3px;">'+'(' + arraySelectedDCCEmail2[i] + ')' +'</li>';
				//}
				
	    	}
			//$('#EMAILNAMELIST').html(namehtml2);
			$('#DCCSETEMAILLIST').html(html2);
			selectedDCC2 = selectedDCC2.join(',');
			selectedDCCName2 = selectedDCCName2.join(',');
			selectedDCCEmail2 = selectedDCCEmail2.join(';');
			//$('#EMAILSETLIST').val(selectedDCC2);
			$('#IDEMAILSETLIST').val(selectedDCCIdEmail2);
			$('#EMAILLIST').val(selectedDCCEmail2);
    	}

    	
    });
}

function emailSettingSave() {
	var email_receiver_addr = $('#EMAILLIST').val();
	var email_user_id = $('#IDEMAILSETLIST').val();
	
	$.ajax({
		url: 'insertEmailSetting.do',
		type: 'POST',
		data:{
			email_receiver_addr: email_receiver_addr,
			email_user_id: email_user_id
		},
		success: function onData(data) {
			if(data === 1 ) {
				alert("수정이 완료되었습니다.")
			}else if(data === 2 ) {
				alert("저장이 완료되었습니다.")
			}
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
}

function selectedEMAILUserDelete(user_id_email,user_id,email) {
	let arraySelectedUserId = $('#EMAILSETLIST').val().split(',');
	//let arraySelectedUserKorNm = $('#DCCNAMELIST').val().split(',');
	let arraySelectedEmail = $('#EMAILLIST').val().split(';');
	let arraySelectedDCCIdEmail2 = $('#IDEMAILSETLIST').val().split(',');
	let filteredselectedUserId = arraySelectedUserId.filter((element) => element !== user_id);
	//let filteredselectedUserIdUserKorNm = arraySelectedUserKorNm.filter((element) => element !== useridplususerkornm);
	let filteredselectedEmail = arraySelectedEmail.filter((element) => element !== email);
	let filteredselectedIdEmail = arraySelectedDCCIdEmail2.filter((element) => element !== user_id_email);
	/*let filteredselectedUserKorNm = [];
	for(let i=0;i<filteredselectedUserIdUserKorNm.length;i++){
		filteredselectedUserKorNm.push(filteredselectedUserIdUserKorNm[i].split('_')[1]);
	}
	console.log(filteredselectedUserKorNm);*/
	let selectedUserId = filteredselectedUserId.join(',');
	//let selectedUserKorNm = filteredselectedUserKorNm.join(',');
	let selectedEmail = filteredselectedEmail.join(';');
	let selectedDCCIdEmail2 = filteredselectedIdEmail.join(',');
	$('#EMAILSETLIST').val(selectedUserId);
	$('#IDEMAILSETLIST').val(selectedDCCIdEmail2);
	$('#EMAILLIST').val(selectedEmail);
	//$('#DCCNAMELIST').val(selectedUserKorNm);
	$('#emailSetting_small_box .'+user_id).remove();
	$('#EMAILNAMELIST .'+user_id).remove();
}

function EMAILPopupClose() {
	$(".pop_emailSet").dialog("close");
}

