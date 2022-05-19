$(function(){
	getSaveUserEmail();
});
function OrganizationChartMultiOpenNotificationSet(){
	$('#organizationUse').val('Notification Receiver 설정');
	window.open('./pop/OrganizationChartMulti.jsp','조직도','top=50, left=50, width=1500, height=650, resizable=yes');
}

function getSaveUserEmail() {
	$.ajax({
	    url: 'getEmailSetting.do',
	    type: 'POST',
	    success: function onData (data) {
	    	if(data[0] != null) {
	    		var emailUserIdArray = data[0].email_user_id.split(',');
	    		var emailReceiverArray = data[0].email_receiver_addr.split(';');
	    		let html  = '';
	    		let emailhtml = '';
		    	for(let i=0; i<emailUserIdArray.length; i++) {
		    		html += '<li id="selectedUserIdIs_'+emailUserIdArray[i]+'">' + emailUserIdArray[i]
							+'&nbsp;<button onclick="deleteSelectUser(`'+emailUserIdArray[i]+'`)" class="btn_icon btn_blue"><i class="fas fa-times"></i><span>Close</span></button></li>';
		    	}
		    	$('#selectIdUserNameListNotification').html(html);
				$('#NOTIRECEIVERLIST').val(data[0].email_receiver_addr);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function deleteSelectUser(userIdAndEmail){
	$('#selectedUserIdIs_'+userIdAndEmail).remove();
	let selectIdList_array = $('#selectIdList').val().split(',');
	//let selectNameList_array = $('#selectNameList').val().split(',');
	let id_filtered = selectIdList_array.filter((element) => element !== userIdAndEmail);
	//let name_filtered = selectNameList_array.filter((element) => element !== user_kor_name+'('+user_id+')');
	$('#selectIdUserNameListNotification').val(id_filtered.join(','));
	//$('#selectNameList').val(name_filtered.join(','));
}