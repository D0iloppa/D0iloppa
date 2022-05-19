/* 
 * 작성자		: 권도일
 * 작성일		: 2021-11-10
 * 메소드명	: tab.js
 * 설명		: 탭 제어 스크립트
 */

// 탭 전역선언
// 탭메뉴 관리용 탭 리스트
var tabList = [];
// 탭의 최대 갯수 설정
var maxTabSize = 20;


// 탭 추가용 템플릿 코드
var tabTemplate = "<li class=\"tabli\"><a href='#{href}'>#{titleLabel}</a>"+
	"<button class='ui-icon ui-icon-close' role='presentation'>Remove Tab</button></li>";


$(document).ready(function() {
	tabsInit();
});


function tabsInit(){
	/*
	// 처음 뿌려줄 디폴트 탭 셋
	var defaultTabList = [
		{"id":"homeTab","label":"HOME","jsp":"/common/home"},
		{"id":"docSearchTab","label":"Document Search","jsp":"/common/docSearch"},
		{"id":"globalSearchTab","label":" Search","jsp":"/common/globalSearch"}];
	
	// 디폴트 탭 추가
	for(var i=0; defaultTabList.length ; i++)
		addTab(defaultTabList[i].jsp,defaultTabList[i].label,defaultTabList[i].id);
	*/
	
	
	$.ajax({
	    url: 'setSession.do',
	    type: 'POST',
	    data: {
	    	prj_id:$("#selectPrjId").val(),
	    	folder_id:-1
	    },
		async: false,
	    success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	var defaultTab = {"id":"homeTab","label":"HOME","jsp":"/common/home","folder_id":-1};
	addTab(defaultTab.jsp , defaultTab.label , defaultTab.id,null,null,-1);
	
}

// 탭 추가 메소드
function addTab(jsp_URL,titleLabel,tabId,type,callback,folder_id) {
	let prjId = $("#selectPrjId").val();
	// 추가하는 탭이 리스트에 있는 경우 해당탭을 활성화만 시켜준다.
	/*if(type==='corrRoot'){
		return false;
	}*/
	if(tabIsIn(tabId)) {
		if(callback != null) tabOn("#"+tabId,callback);
		else tabOn("#"+tabId,type);
		/* ===================================
		 * [c.f]
		 * 단일 탭의 경우, 탭을 활성화 해주고
		 * 탭 타이틀도 변경시켜야 하기 때문에 이름변경 함수를 호출한다.
		 * 단일 탭이 아니면 해당 탭은 모두 독립적인 탭id를 가지고 있으므로
		 * 탭 타이틀을 변경시켜도 변화가 없다.
		 * =================================== */
		tabLabelChange("#"+tabId,titleLabel);
		return;
	}
	if(tabList.length >= maxTabSize) {
		alert("추가 가능한 최대 탭의 갯수를 초과하였습니다.");
		return false;
	}
	
	// 탭 리스트 관리를 위해 객체로 구현, 탭리스트에 추가
	var tab = {'id':'#'+tabId,'label':titleLabel,'type':"",'isOn':false,'folder_id':folder_id};
	tabList.push(tab);
	
	
	
  // 탭 타이틀 리스트 추가부분
	var tabTitle;
	
	if (tabList.length < 2) { // 최초 추가되는 탭
		tab.isOn = true;
		tabTitle = $(tabTemplate.replace("<li>", "<li class='on'>").replace(
				/#\{href\}/g, "#" + tabId).replace(/#\{titleLabel\}/g,
				titleLabel));
	}
	else 
		tabTitle = $(tabTemplate.replace(/#\{href\}/g, "#" + tabId).replace(
				/#\{titleLabel\}/g, titleLabel));

	$(".tab_tit").append(tabTitle);
	
	
	if (tabList.length < 2)
		$(".tab_cont").append(
				"<div id='" + tabId + "' class='tab_div on'></div>");
	else
		$(".tab_cont").append("<div id='" + tabId + "' class='tab_div'></div>");

	let tab_Target = "#" + tabId; // jsp를 include할 탭의 id
	
	if(type=='SEARCH') {
		$(tab_Target).load('/tab.do', { path : jsp_URL, prjId : prjId, folder_id:0} ); // 해당 탭 div에 jsp파일 include
	}else if(type=='PCO2') {
		$(tab_Target).load('/tab.do', { path : jsp_URL, prjId : prjId, folder_id:9999} ); // 해당 탭 div에 jsp파일 include
	}else if(type!='TROutgoing' && type!='TRIncoming' && type!='PCO2') {
		$(tab_Target).load('/tab.do', { path : jsp_URL, prjId : prjId, folder_id:folder_id} ); // 해당 탭 div에 jsp파일 include
	}
	
	
	//$(tab_Target).load('/tab.do?path=' + jsp_URL+ '&prjId=' + prjId); // 해당 탭 div에 jsp파일 include
	
	tabClose(); // 탭에 종료버튼 기능 할당

	$(".tab_tit li").on("click", function() {
		if($(this).children().html()==='Document Control'){
			folderChange();
		}
		
		
		
		let target = $(this).children().attr('href');
		// 탭 정보를 찾음
		let tabInfo = tabList.find(item => item.id == target);
		console.log(tabInfo);
		
		let label = tabInfo.id;
		
		if(label.indexOf('outgoingTab')>0 && trOut_ready===true){
			$('#transmittal_left').css('display','block');
			$('#transmittal_left_incoming').css('display','none');
			getTrOutSearch();
			createTR();
		    getTrIssuePurposeList();
			setWhatTROut();
		}else if(label.indexOf('incomingTab')>0 && trIn_ready===true){
			$('#tr_incoming_filename').val('');	//comment file 초기화
			$('#transmittal_left').css('display','none');
			$('#transmittal_left_incoming').css('display','block');
			getTrInSearch();
			getTrInDisciplineList();
			createTRIn();
			setWhatTRIn();
		}else{
			$('#transmittal_left').css('display','none');
			$('#transmittal_left_incoming').css('display','none');
		}
    });
	

	$('.tabli').children().on("click", function () {
		let target = $(this).attr('href');
		// 탭 정보를 찾음
		let tabInfo = tabList.find(item => item.id == target);

		// 홈탭의 경우 리프레쉬
		if(tabInfo.id == "#homeTab") homeRefresh();
		
		// 세션에 값 저장		
		$.ajax({
			    url: 'setSession.do',
			    type: 'POST',
			    data: {
			    	prj_id:prjId,
			    	folder_id:tabInfo.folder_id ? tabInfo.folder_id : -1
			    },
				async: false,
			    success: function onData (data) {
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		current_folder_id = parseInt(tabInfo.folder_id);
		
		// discip코드 찾기
		let folderInfo = folderList.find(item => item.id == current_folder_id);
		
		// 탭 클릭했을 때  해당 탭에 매핑된 폴더 id 정보가 있는 경우, current 변수들 변화
		if(folderInfo){ 
			current_folder_discipline_code = folderInfo.discipline_code;
			current_folder_discipline_code_id = folderInfo.discipline_code_id;
			current_folder_discipline_display = folderInfo.discipline_display;
		}
		
		if(folderInfo.process_type.indexOf('OUTGOING') !== -1){

			//$('#transmittal_left').css('display','block');
			$('#transmittal_left')[0].style['display'] = 'block';
			$('#transmittal_left_incoming').css('display','none');
			console.log("chk1")
		}else if(folderInfo.process_type.indexOf('INCOMING') !== -1){
			
			$('#tr_incoming_filename').val('');	//comment file 초기화
			$('#transmittal_left_incoming').css('display','block');
			$('#transmittal_left').css('display','none');
			console.log("chk2")
		}
		
		return true;
		
	});
	
	
	$('.tabli').on("click", function () {
		if($(this).children()[0].innerHTML.indexOf('OUTGOING') !== -1){
			console.log("chk1")
			$('#transmittal_left').css('display','block');
			$('#transmittal_left_incoming').css('display','none');
		}else if($(this).children()[0].innerHTML.indexOf('INCOMING') !== -1){
			console.log("chk2")
			$('#tr_incoming_filename').val('');	//comment file 초기화
			$('#transmittal_left_incoming').css('display','block');
			$('#transmittal_left').css('display','none');
		}
	});
	
	return true;
}



//탭 닫기
function tabClose(){
	$('.ui-icon-close').click(function() {
		// 만약 탭이 하나일 경우 탭을 닫지 않음
		// if(tabList.length < 2) return;
		
		
		var id = $(this).prev().attr('href');
		// 닫아줄 탭의 인덱스 값(위치)
		var tabIdx = findTab(id);
		
		// HOME탭의 경우 닫히지 않도록 해준다.
		if(tabIdx === 0 ) { // 0번 탭을 HOME탭이라 함
			console.log("home tab 제거 불가");
			return;
		}
		
		
		// 삭제할 탭의 활성화 여부확인s
		var tabIsOn = tabList[tabIdx].isOn;
		// 탭리스트에서 해당id값 제거
		tabList.splice(tabIdx,1);
		
		// 탭 리스트와, div 삭제
		$(this).parent().remove();
		$(id).remove();
		
		// 제거한 탭이 켜져있는 경우의 처리
		if(tabIsOn) tabOn( (tabIdx < 1) ? tabList[0].id : tabList[tabIdx-1].id);
		

		//탭이 닫힌 후 열릴 탭이 outgoingTab과 incomingTab일 경우
		if(tabList[tabList.length-1].id === '#outgoingTab') {
			$('#transmittal_left').css('display','block');
			$('#transmittal_left_incoming').css('display','none');
		}else if(tabList[tabList.length-1].id === '#incomingTab') {
			$('#transmittal_left').css('display','none');
			$('#transmittal_left_incoming').css('display','block');
		}

		let tabhref = $(this).prev().attr('href');
		if(tabhref==='#projectGENTab'){
			removeTabPG();
		}else if(tabhref==='#userRegisterTab'){
			removeTabUSERREGI();
		}else if(tabhref==='#userMNGTab'){
			removeTabUSERMNG();
		}else if(tabhref==='#prjNoticeBoardTab'){
			removeTabPRJNOTICE();
		}else if(tabhref==='#projectCreationTab'){
			removeTabPRJCREATION();
		}else if(tabhref==='#noticeBoardTab'){
			removeTabNOTICE();
		}else if(tabhref==='#globalSearchTab'){
			removeTabGLOBALSERCH();
		}else if(tabhref==='#dccTab'){
			removeTabDCC();
		}else if(tabhref==='#colTab'){
			removeTabCOL();
		}else if(tabhref==='#emailSettingTab'){
			removeTabEMAILSETTING();
		}else if(tabhref==='#prjReprotEngTab'){
			removeTabPrjReportEng();
		}else if(tabhref==='#prjReportProTap'){
			removeTabPrjReportPro();
		}else if(tabhref==='#prjReprotVenTab'){
			removeTabPrjReportVen();
		}else if(tabhref==='#prjReprotSiteTab'){
			removeTabPrjReportSite();
		}else if(tabhref==='#prjReprotCorrTab'){
			removeTabPrjReportCorr();
		}else if(tabhref==='#securityIpTab') {
			removeTabSecurityIp();
		}else if(tabhref==='#securityLogTab') {
			removeTabSecurityLog();
		}else if(tabhref==='#dictTab') {
			removeTabDictionary();
		}
	});
}

function removeTab(){
	$('.pop_dccSet').remove();
}
//탭 리스트에서 id로 위치 찾기
function findTab(id){
	for(let i=0 ; i<tabList.length; i++)
		if(id === tabList[i].id) return i;
	return -1;	
}

// 해당 탭id가 탭에 존재하는지 확인
function tabIsIn(tabId){
	
	for(let i=0 ; i < tabList.length; i++) 
		if(tabList[i].id === "#"+tabId) return true;
	
	return false;	
}

// 탭 활성화
function tabOn(id, type, callback){
	// 탭의 모든 isOn을 끔
	for(let i=0 ; i < tabList.length; i++) tabList[i].isOn = false;	 
	// 선택된 탭만 isOn 켜준다.
	$(".tab_wrap .tab_tit li").removeClass("on");
	$(".tab_wrap .tab_cont > div").removeClass("on");
	
	var idx = findTab(id);
	
	tabList[idx].isOn = true;
	$(".tab_tit li:nth-child("+(idx+1)+")").addClass("on");
	$(id).addClass("on");
	
	
	
	if((type==='DCC' || type==='TRA')&& dcc_ready==true) folderChange();
	
	//2022-02-20 추가 Transmittal 폴더에 직접 New로 등록하는 경우 도서는 General Type으로 등록돼야함
	if(type==='TRA') current_doc_type='General';
	
	if(type==='TROutgoing' && trOut_ready===true){
		$('#transmittal_left').css('display','block');
		$('#transmittal_left_incoming').css('display','none');
		getTrOutSearch();
		createTR();
	    getTrIssuePurposeList();
		setWhatTROut();
		var contextpath = $('#context_path').val();
		console.log('contextpath:'+contextpath);
		$.getScript(contextpath+"/resource/js/documentControl.js");
	}else if(type==='TRIncoming' && trIn_ready===true){
		$('#tr_incoming_filename').val('');
		$('#transmittal_left_incoming').css('display','block');
		$('#transmittal_left').css('display','none');
		getTrInSearch();
		getTrInDisciplineList();
		createTRIn();
		setWhatTRIn();
		var contextpath = $('#context_path').val();
		$.getScript(contextpath+"/resource/js/documentControl.js");
	}else{
		$('#transmittal_left').css('display','none');
		$('#transmittal_left_incoming').css('display','none');
	}
	
	if(callback != null)
		callback();
}

// 탭 이름 변경
function tabLabelChange(tabId, label){
	// 변경할 탭 
	var tmp = document.querySelector('.tab_tit a[href="'+tabId+'"]');
	tmp.innerText = label;
}