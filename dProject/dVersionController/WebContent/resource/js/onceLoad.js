//User Register, Email Setting 페이지 검색 창 엔터 누를 때의 이벤트 처리	
$(document).keydown(function(event) {
	 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
	let focusEle = document.activeElement;
	if(document.getElementById('usersSearchKeyword2') == focusEle){
		 if ( event.keyCode == 13 || event.which == 13 ) {
			 usersSearchReged();
		 }
	}else if(document.getElementById('usersEmailSearchSetKeyword') == focusEle){
		 if ( event.keyCode == 13 || event.which == 13 ) {
			 usersEmailSetSearch();
		 }
	}else if(document.getElementById('drn_nm_search') == focusEle){
		 if ( event.keyCode == 13 || event.which == 13 ) {
			 drnUserSearch();
		 }
	}
});

$(document).on("click", "#general01 ul li a", function () {
	//console.log("test2");
	
    var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
    
    /*if(!saveCheck(function() {return true})){
		return false;
	}*/
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

$(document).on("click", "#general02 ul li a", function () {
	//console.log("test2");
	
    var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
    
    if(!saveCheck02(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

$(document).on("click", "#general04 ul li a", function () {
	//console.log("test3");
	
	var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
	
    if(!saveCheck04(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

$(document).on("click", "#general05 ul li a", function () {
	var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
	
    if(!saveCheck05(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

$(document).on("click", "#general06 ul li a", function () {
	
	var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
	
    if(!saveCheck06(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

$(document).on("click", "#general07 ul li a", function () {
	//console.log("test2");
	
    var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
    
    if(!saveCheck07(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});

$(document).on("click", "#general10 ul li a", function () {
	//console.log("test2");
	
    var loca = $(this).attr("href");
    let step_no = loca.substr(3);
    let method = eval("pgen_step" + step_no);
    
    if(!saveCheck10(function() {return true})){
		return false;
	}
	
	
    $(".proj_general ul li a").removeClass("on");
    $(".pg_cont .pg").removeClass("on");
    $(this).addClass("on");
    $(loca).addClass("on");
    
    method();
    
    return false;
});
$(function(){
	/*$('.tabli').children().on("click", function () {
		let target = $(this).attr('href');
		// 탭 정보를 찾음
		let tabInfo = tabList.find(item => item.id == target);
		
		console.log(tabInfo.folder_id , folder_id);
		// 세션에 값 저장
		if(tabInfo.folder_id + "" == folder_id + ""){
			$.ajax({
			    url: 'setSession.do',
			    type: 'POST',
			    data: {
			    	prj_id:prjId,
			    	folder_id:tabInfo.folder_id
			    },
				async: false,
			    success: function onData (data) {
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
			current_folder_id = parseInt(tabInfo.folder_id);
		}
	});*/
});