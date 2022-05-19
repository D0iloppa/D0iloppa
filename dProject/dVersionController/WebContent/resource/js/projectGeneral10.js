var pg10_G = {};
var origin_email_list = [];
var isEdited = false;
var auto_send_exist = false;
var auto_email_type = '';
var fontList = ['Arial', 
	'Arial Black', 
	'Comic Sans MS', 
	'Courier New',
	'KOHI배움',
	'강한공군체 Bold',
	'궁서',
	'굴림체',
	'굴림',
	'돋음체',
	'맑은 고딕',
	'바탕체',
	'웰컴체 Bold',
	'조선일보 명조체',
	'함렛 Bold',
	'횡성한우체'];

function saveCheck10(callback){
	if(isEdited){
		if(confirm("저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?")){
			isEdited = false;

	    	let receiver_email = $('#receiver_email').eq(0).data('selectize');
	    	let referto_email = $('#referto_email').eq(0).data('selectize');
	    	let bcc_email = $('#bcc_email').eq(0).data('selectize');
    		sender_email.val('');
    		receiver_email.setValue(null,false);
    		referto_email.setValue(null,false);
    		bcc_email.setValue(null,false);
	    	$('input[name="file_down_link_yn"]').prop("checked", false);
	    	$('#file_rename_yn').prop('checked', false);
	    	$('#file_rename').val('');
	    	$('#subject_prefix').val('');
			
	        return callback();
	    }
	    else{
	        return false;
	    }
	}
	return true;
}

$(function(){
	getTemplateEmailList();
	
	$("input:radio[name=file_down_link_yn]").click(function(){
        if($("input[name=file_down_link_yn]:checked").val()==='Y'){
            $('#fileRenameTr').css('display','table-row');
        }else{
            $('#fileRenameTr').css('display','none');
	    	$('#file_rename_yn').prop('checked', false);
        }
    });
	toolbar = [
		// 스타일
		['style',['style']],
		// 굵기, 기울임꼴, 밑줄
		['style',['bold','italic','underline']],
		// 글자 크기 설정
		['fontsize', ['fontsize']],
		// 글꼴 설정
		['fontname',['fontname']],
		// 줄간격
	    ['height', ['height']],
	    // 글자색
	    ['color', ['forecolor','color']],
	    // 글머리 기호, 번호매기기, 문단정렬
	    ['para', ['ul', 'ol', 'paragraph']],
	    // 표만들기
	    ['table', ['table']],
	    // 그림첨부, 링크만들기
	    ['insert',['picture','link']],
	    // 코드보기, 확대해서보기
	    ['view', ['fullscreen', 'codeview']]
	];
	
	$("#summernoteMailTemplate").summernote({
		height: 500,
		lang: "ko_KR",
		toolbar: toolbar,
		// 추가한 글꼴
		fontNames: fontList,
		fontNamesIgnoreCheck: fontList
	});
	
    $(".pop_mailType").dialog({
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
    $(document).on("click", "#mailType", function() {
        $(".pop_mailType").dialog("open");
        return false;
    });

    $(".pop_EmailTypeDelete").dialog({
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
    
    $('#sender_email').change(function(){
		isEdited = true;
	});
	$('#receiver_email').change(function(){
		isEdited = true;
	});
	$('#referto_email').change(function(){
		isEdited = true;
	});
	$('#bcc_email').change(function(){
		isEdited = true;
	});
	$('#file_rename').change(function(){
		isEdited = true;
	});
	$('#subject_prefix').change(function(){
		isEdited = true;
	});

    getEmailTypeMultiBox();
    getPrjEmailType();
});
function pg10load(){
	getTemplateEmailList();
    getEmailTypeMultiBox();
    getPrjEmailType();
}
function getPrjEmailType(){
	var email_feature = $('#mailTypeSelectFeature').val();
	$.ajax({
	    url: 'getPrjEmailType.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	email_feature: email_feature
	    },
	    success: function onData (data) {
			auto_send_exist = false;
	    	var tblMailType = [];
	    	for(i=0;i<data[0].length;i++){
	    		let email_auto_send_yn = '';
	    		if(data[0][i].email_auto_send_yn==='Y'){
	    			email_auto_send_yn = 'Y'
	    			auto_send_exist = true;
	    			auto_email_type = data[0][i].email_type;
	    		}else{
	    			email_auto_send_yn = 'N';
	    		}
	    		tblMailType.push({
	    			email_type_id: data[0][i].email_type_id,
	    			feature: data[0][i].email_feature,
	    			type: data[0][i].email_type,
	    			desc: data[0][i].email_desc,
	    			autosend: email_auto_send_yn,
	    			bean: data[0][i]
	    		});
	    	}
	    	setPrjEmailType(tblMailType);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjEmailType(tblMailType){
    var mailTypeTable = new Tabulator("#tblMailType", {
		height: "100%",
    	index: "feature",
        data: tblMailType,
        layout: "fitColumns",
        selectable:1,
        columns: [{
            title: "FEATURE",
            field: "feature"
        }, {
            title: "MAIL TEMPLATE",
            field: "type"
        }, {
            title: "DESCRIPTION",
            field: "desc"
        }, {
            title: "Auto Send",
            field: "autosend"
        }
        ]
    });
    
    mailTypeTable.on("rowSelected", function(row){
		$('#mailTypeSelectFeature').val(row.getData().bean.email_feature);
		$('#email_type').val(row.getData().bean.email_type);
		$('#email_desc').val(row.getData().bean.email_desc);
		let auto_send_yn = '';
		if(row.getData().bean.email_auto_send_yn==='Y'){
			auto_send_yn= 'Y';
		}else{
			auto_send_yn= 'N';
		}
		$('#email_auto_send_yn').val(auto_send_yn);
    });
    mailTypeTable.on("rowDeselected", function(row){
		$('#mailTypeSelectFeature').val('');
		$('#email_type').val('');
		$('#email_desc').val('');
		$('#email_desc').val('N');
    });
    pg10_G.mailTypeTable = mailTypeTable;
}
function emailTypeSave(){
	var email_feature = $('#mailTypeSelectFeature').val();
	var email_type = $('#email_type').val();
	var email_desc = $('#email_desc').val();
	let email_auto_send_yn = $('#email_auto_send_yn').val();
	if(auto_send_exist===true && auto_email_type!==email_type && email_auto_send_yn==='Y'){
		alert('Auto Send는 각 Feature 당 하나만 설정할 수 있습니다.');
	}else{
		$.ajax({
		    url: 'insertPrjEmailType.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	email_feature: email_feature,
		    	email_type: email_type,
		    	email_desc: email_desc,
		    	email_auto_send_yn:email_auto_send_yn
		    },
		    success: function onData (data) {
		    	if(data[0]>0){
		    		getEmailTypeMultiBox();
			    	getPrjEmailType();
		    	}else{
		    		alert('Save failed!');
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function emailTypeDeleteOpen(){
	if(pg10_G.mailTypeTable.getSelectedData().length === 0){
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
	}else{
		$(".pop_EmailTypeDelete").dialog('open');
	}
}
function emailTypeDeleteCancel(){
	$(".pop_EmailTypeDelete").dialog('close');
}
function emailTypeDelete(){
	var email_type_id = pg10_G.mailTypeTable.getSelectedData()[0].email_type_id;
	$.ajax({
	    url: 'deletePrjEmailType.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	email_type_id: email_type_id
	    },
	    success: function onData (data) {
	    	getPrjEmailType();
	    	$(".pop_EmailTypeDelete").dialog('close');
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function emailTypeToExcel(){
//	pg10_G.mailTypeTable.download("xlsx", "data.xlsx", {sheetName:"Email Type"});
	
	let gridData = pg10_G.mailTypeTable.getData();
//	console.log(gridData);
	
	let jsonList = [];
	
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'Email_Type';
	
//	console.log(jsonList);
	
	var f = document.excelUploadForm_pg10_Email;
	
	var email_feature = $('#mailTypeSelectFeature').val();
	
	f.prj_id10_Email.value = selectPrjId;
	f.prj_nm10_Email.value = getPrjInfo().full_nm;
	f.email_feature10_Email.value = email_feature;
	f.fileName10_Email.value = fileName;
   	f.jsonRowdatas10_Email.value = jsonList;
    f.action = "pg10EmailExcelDownload.do";
   	f.submit();	
}
function getEmailTypeMultiBox(){
	$.ajax({
	    url: 'getPrjEmailTypeAll.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
            var html = '<tr><th>FEATURE</th><th>TYPE</th><th>DESCRIPTION</th><th>AUTO SEND</th></tr>';
            for(i=0;i<data[0].length;i++){
            	let email_auto_send_yn = data[0][i].email_auto_send_yn;
            	if(email_auto_send_yn!='Y'){
            		email_auto_send_yn = 'N';
            	}
            	html += '<tr class="selectMailType"><td id="'+data[0][i].email_type_id+'">'+data[0][i].email_feature+'</td><td>'+data[0][i].email_type+'</td><td>'+data[0][i].email_desc+'</td><td>'+email_auto_send_yn+'</td></tr>'
            }
	    	$('#emailTypeMultiBox').html(html);
	    	
	    	getEmailTemplate();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function fileRenameCheckInEmailTemplate(){
	if($('#file_rename_yn').is(':checked')){
		$('#file_rename').val('%DOCNO%_%REVNO%_%TITLE%');
	}else{
		$('#file_rename').val('');
	}
}
function getEmailTemplate(){
	$('.selectMailType').click(function(){
		$('#emailSelectWrap').removeClass('on');
		$('#emailTypeSelected').html($(this).children().next().html());
		$('#selectedEmailFeature').val($(this).children().html());
		$('#selectedEmailTypeId').val($(this).children().attr('id'));
	    getPrjSystemPrefix();
	    
	    var email_type_id = $('#selectedEmailTypeId').val();
		$.ajax({
		    url: 'getPrjEmailTypeContents.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	email_type_id:email_type_id
		    },
		    success: function onData (data) {
		    	if(data[1]==='Y'){
			    	$('#receiverEmail').addClass('required');
			    	$('#receiverEmail').html('Receiver e-mail <i>required</i>');
			    	$('#current_template_autosend_yn').val('Y');
		    	}else{
			    	$('#receiverEmail').removeClass('required');
			    	$('#receiverEmail').html('Receiver e-mail');
			    	$('#current_template_autosend_yn').val('N');
		    	}
		    	let receiver_email = $('#receiver_email').eq(0).data('selectize');
		    	let referto_email = $('#referto_email').eq(0).data('selectize');
		    	let bcc_email = $('#bcc_email').eq(0).data('selectize');
		    	isEdited = true;
		    	if(data[0]==null){
		    		$('#sender_email').val('');
		    		receiver_email.setValue(null,false);
		    		referto_email.setValue(null,false);
		    		bcc_email.setValue(null,false);
			    	$('input[name="file_down_link_yn"]').prop("checked", false);
			    	$('#file_rename_yn').prop('checked', false);
			    	$('#file_rename').val('');
			    	$('#subject_prefix').val('');
			    	$('#summernoteMailTemplate').summernote("code", '');
		    	}else{
		    		$('#sender_email').val(data[0].email_sender_addr);
		    		let receiver_email_list = data[0].email_receiver_addr.split(';');
		    		for(let l=0;l<receiver_email_list.length;l++){
			    		if(!origin_email_list.includes(receiver_email_list[l])){
			    			receiver_email.addOption({value:receiver_email_list[l], text:receiver_email_list[l]});
			    		}
		    		}
		    		let referto_email_list = data[0].email_referto_addr.split(';');
		    		for(let l=0;l<referto_email_list.length;l++){
			    		if(!origin_email_list.includes(referto_email_list[l])){
			    			referto_email.addOption({value:referto_email_list[l], text:referto_email_list[l]});
			    		}
		    		}
		    		let bcc_email_list = data[0].email_bcc_addr.split(';');
		    		for(let l=0;l<bcc_email_list.length;l++){
			    		if(!origin_email_list.includes(bcc_email_list[l])){
			    			referto_email.addOption({value:bcc_email_list[l], text:bcc_email_list[l]});
			    		}
		    		}
		    		console.log(data[0].email_receiver_addr.split(';'));
		    		receiver_email.setValue(data[0].email_receiver_addr.split(';')[0]==''&&data[0].email_receiver_addr.split(';').length==1?null:data[0].email_receiver_addr.split(';'),false);
		    		referto_email.setValue(data[0].email_referto_addr.split(';')[0]==''&&data[0].email_referto_addr.split(';').length==1?null:data[0].email_referto_addr.split(';'),false);
		    		bcc_email.setValue(data[0].email_bcc_addr.split(';')[0]==''&&data[0].email_bcc_addr.split(';').length==1?null:data[0].email_bcc_addr.split(';'),false);
			    	$('input[name="file_down_link_yn"][value="'+data[0].file_link_yn+'"]').prop("checked", true);
			    	if(data[0].file_rename_yn==='Y'){
			    		$('#file_rename_yn').prop('checked', true);
				    	$('#file_rename').val(data[0].file_nm_prefix);
			    	}else{
			    		$('#file_rename_yn').prop('checked', false);
				    	$('#file_rename').val('');
			    	}
			    	$('#subject_prefix').val(data[0].subject_prefix);
			    	$('#summernoteMailTemplate').summernote("code", data[0].contents);
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
}
function getPrjSystemPrefix(){
	var selectedEmailFeature = $('#selectedEmailFeature').val();
	var system_prefix_type = 0;
	if(selectedEmailFeature==='CORRESPONDENCE'){
		system_prefix_type = 1;
	}else{
		system_prefix_type = 2;
	}
	$.ajax({
	    url: 'getPrjSystemPrefix.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	system_prefix_type: system_prefix_type
	    },
	    success: function onData (data) {
	    	var tblsysprf = [];
	    	for(i=0;i<data[0].length;i++){
	    		if(data[0][i].prefix!=='%DOC_NO%'&&data[0][i].prefix!=='%DOC_TITLE%'&&data[0][i].prefix!=='%REV_NO%')
	    		tblsysprf.push({
	    			prefix: data[0][i].prefix,
	    			prefix_desc: data[0][i].prefix_desc,
	    			bean:data[0][i]
	    		});
	    	}
	    	setPrjSystemPrefix(tblsysprf);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setPrjSystemPrefix(tblsysprf){
    var sysPrfTable = new Tabulator("#prjSystemPrefix", {
		height: "100%",
    	index: "",
        data: tblsysprf,
        layout: "fitColumns",
        selectable:1,
        columns: [{
        	title:"*Prefix 설정용 System 변수*",
        	columns:[
        	{title: "PREFIX",
            field: "prefix",
            hozAlign:"left"},
            {title: "DESCRIPTION",
            field: "prefix_desc",
            hozAlign:"left"}
            ]
        }]
    });

    sysPrfTable.on("rowSelected", function(row){
    	var contents = $('#summernoteMailTemplate').summernote("code");
    	$('#summernoteMailTemplate').summernote("code", contents+pg10_G.sysPrfTable.getSelectedData()[0].prefix);
    });
    pg10_G.sysPrfTable = sysPrfTable;
}
function getTemplateEmailList(){
	$.ajax({
	    url: '../getAllUsersVOList.do',
	    type: 'POST',
	    async:false,
	    success: function onData (data) {
	    	let html = '<option value=""></option>';
	    	for(let i=0;i<data[0].length;i++){
	    		html += '<option value="'+data[0][i].email_addr+'!!'+data[0][i].user_id+'" label="'+data[0][i].user_kor_nm+'">'+data[0][i].user_kor_nm+'('+data[0][i].email_addr+')'+'</option>';
	    		origin_email_list.push(data[0][i].email_addr+'!!'+data[0][i].user_id);
	    	}
	    	$('#receiver_email').html(html);
	    	$('#referto_email').html(html);
	    	$('#bcc_email').html(html);

	    	$("#receiver_email").selectize({
	    		delimiter:',',
	    		onType: function(str) {
	    			if (str.slice(-1) === ';') { 
	    				this.createItem(str.slice(0, -1))
	    				this.removeItem(';') // in case user enters just the delimiter
	    			}
	    		},
	    		splitOn:/[,;]/,
	    		persist: false,
	    		openOnFocus:false,
				createOnBlur: true,
		        closeAfterSelect: true,
				create: true,
				maxItems:null
	    	});
	    	$("#referto_email").selectize({
	    		delimiter:',',
	    		onType: function(str) {
	    			if (str.slice(-1) === ';') { 
	    				this.createItem(str.slice(0, -1))
	    				this.removeItem(';') // in case user enters just the delimiter
	    			}
	    		},
	    		splitOn:/[,;]/,
	    		persist: false,
	    		openOnFocus:false,
				createOnBlur: true,
		        closeAfterSelect: true,
				create: true,
				maxItems:null
	    	});
	    	$("#bcc_email").selectize({
	    		delimiter:',',
	    		onType: function(str) {
	    			if (str.slice(-1) === ';') { 
	    				this.createItem(str.slice(0, -1))
	    				this.removeItem(';') // in case user enters just the delimiter
	    			}
	    		},
	    		splitOn:/[,;]/,
	    		persist: false,
	    		openOnFocus:false,
				createOnBlur: true,
		        closeAfterSelect: true,
				create: true,
				maxItems:null
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function emailTemplateSave(){
	isEdited = false;
	var email_type_id = $('#selectedEmailTypeId').val();
	var email_sender_addr = $('#sender_email').val();
	let receiver_email_array_before = $('#receiver_email').val();
	let receiver_email_array_after = [];
	for(let i=0;i<receiver_email_array_before.length;i++){
		receiver_email_array_after[i] = receiver_email_array_before[i].split('!!')[0];
	}
	let referto_email_array_before = $('#referto_email').val();
	let referto_email_array_after = [];
	for(let i=0;i<referto_email_array_before.length;i++){
		referto_email_array_after[i] = referto_email_array_before[i].split('!!')[0];
	}
	let bcc_email_array_before = $('#bcc_email').val();
	let bcc_email_array_after = [];
	for(let i=0;i<bcc_email_array_before.length;i++){
		bcc_email_array_after[i] = bcc_email_array_before[i].split('!!')[0];
	}
	var email_receiver_addr = receiver_email_array_before.join(';');
	var email_referto_addr = referto_email_array_before.join(';');
	var email_bcc_addr = bcc_email_array_before.join(';');
	var file_link_yn = $('input[name="file_down_link_yn"]:checked').val();
	var file_rename_yn = $('#file_rename_yn').is(':checked')===true?'Y':'N';
	var file_nm_prefix = $('#file_rename').val();
	var subject_prefix = $('#subject_prefix').val();
	var contents = $('#summernoteMailTemplate').summernote("code");
	

	if(!email_check(email_sender_addr)){
		alert("SENDER E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. SENDER는 하나의 메일주소만 설정할 수 있습니다.");
		return false;
	}
	//let email_receiver_array = email_receiver_addr.split(';');
	for(let i=0;i<receiver_email_array_after.length;i++){
		if(!email_check(receiver_email_array_after[i])){
			alert("RECEIVER E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
			return false;
		}
	}
	//let email_referto_array = email_referto_addr.split(';');
	for(let i=0;i<referto_email_array_after.length;i++){
		if(!email_check(referto_email_array_after[i])){
			alert("REFERTO E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
			return false;
		}
	}
	//let email_bcc_array = email_bcc_addr.split(';');
	for(let i=0;i<bcc_email_array_after.length;i++){
		if(!email_check(bcc_email_array_after[i])){
			alert("BCC E-MAIL에 이메일 형식에 맞지않는 값이 입력되었습니다. 메일주소 입력 후 엔터를 눌러주세요.");
			return false;
		}
	}
	
	if(email_sender_addr==='' || email_sender_addr===null || email_sender_addr===undefined){
		alert('Sender e-mail을 설정해주세요');
	}else{
		if($('#current_template_autosend_yn').val()==='Y'){
			if(email_receiver_addr==='' || email_receiver_addr===null || email_receiver_addr===undefined){
				alert('현재 템플릿은 자동발송기능이 적용된 템플릿이므로 RECEIVER E-MAIL은 필수값입니다.');
				return;
			}
		}
		$.ajax({ // p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT GENERAL > 발송메일 템플릿 설정",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		$.ajax({
		    url: 'insertPrjEmailTypeContents.do',
		    type: 'POST',
		    data:{
		    	prj_id: selectPrjId,
		    	email_type_id: email_type_id,
		    	email_sender_addr: email_sender_addr,
		    	email_receiver_addr: email_receiver_addr,
		    	email_referto_addr: email_referto_addr,
		    	email_bcc_addr: email_bcc_addr,
		    	file_link_yn: file_link_yn,
		    	file_rename_yn: file_rename_yn,
		    	file_nm_prefix:file_nm_prefix,
		    	subject_prefix:subject_prefix,
		    	contents:contents
		    },
		    success: function onData (data) {
		    	if(data[0]>0){
		    		alert('Save Completed');
		    	}else{
		    		alert('Save failed');
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}
function saveToFile_Chrome(fileName, content) {
    var blob = new Blob([content], { type: 'text/plain' });
    objURL = window.URL.createObjectURL(blob);
            
    // 이전에 생성된 메모리 해제
    if (window.__Xr_objURL_forCreatingFile__) {
        window.URL.revokeObjectURL(window.__Xr_objURL_forCreatingFile__);
    }
    window.__Xr_objURL_forCreatingFile__ = objURL;
    var a = document.createElement('a');
    a.download = fileName;
    a.href = objURL;
    a.click();
}
function emailTemplatePCSave(){
	saveToFile_Chrome('EmailTemplate',$('#summernoteMailTemplate').summernote("code"));
}
function checkTxtTemplate() {
	let form = $('#emailTemplateUploadForm')[0];
    let formData = new FormData(form);
    $.ajax({
		url: 'isTXTFile.do',
		type: 'POST',
		data: formData,
		enctype:'multipart/form-data',
        processData:false,
        contentType:false,
        dataType:'json',
        cache:false,
		success: function onData (data) {
			if(data > 0) {
				let input = document.getElementById('emailTemplateFromPC');
				loadFile(input.files[0]);
			}else {
				alert("txt파일만 불러올 수 있습니다.");
			}
		},
		error: function onError (error) {
	    	console.error(error);
	    }
	});
}
async function loadFile(file) {
    let text = await file.text();
    $('#summernoteMailTemplate').summernote("code",text);
}