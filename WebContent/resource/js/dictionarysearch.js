var clickTb = 0;
var dictionarytable;
var abbr_id;

//엑셀로부터 불러온 데이터인지 체크하는 플래그
//엑셀로 데이터 불러오면 true, retrive 혹은 셀렉트박스 선택시 클리어
var isFromExcel = false;
var excelData;

$( document ).ready(function() { // 페이지가 실행될때
	var loading = $('<div id="loading" class="loading"></div><img id="loading_img" alt="loading" src="../resource/images/loading.gif" />')
    .appendTo(document.body).hide();

	$(window)	

    .ajaxStart(function(){
    	loading.show();
	})

	.ajaxStop(function(){
		loading.hide();
	});
	
//	getDictionaryList();
	getDictionaryVO();
	$('#savebutton').css('display','block');
	$('#updatebutton').css('display','none');
//	$('#dictionaryList2').css('display','none');
	
	$(".pop_deleteDictionConfirm").dialog({
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
	
	$(".pop_dictionarySaveConfirm").dialog({
		draggable : true,
		autoOpen : false,
		maxWidth : 1200,
		width : "auto"
	}).dialogExtend(
			{
				"closable" : true,
				"maximizable" : true,
				"minimizable" : true,
				"minimize" : function(evt) {
					$(
							".ui-widget.ui-widget-content:not(.ui-datepicker)")
							.addClass("position");
				},
				"maximize" : function(evt) {
					$(".ui-dialog .ui-dialog-content").addClass(
							"height_reset");
					$(this).closest(".ui-dialog").css("height", "100%");
					$(this).closest(".ui-widget.ui-widget-content")
							.addClass("wd100");
				},
				"beforeMinimize" : function(evt) {
					$(".ui-dialog-title").show();
				},
				"beforeMaximize" : function(evt) {
					$(
							".ui-widget.ui-widget-content:not(.ui-datepicker)")
							.removeClass("position");
					$(this).closest(".ui-dialog").css("height", "auto");
					$(".ui-dialog .ui-dialog-content").removeClass(
							"height_reset");
					$(".ui-dialog-title").show();
				},
				"beforeRestore" : function(evt) {
					$(
							".ui-widget.ui-widget-content:not(.ui-datepicker)")
							.removeClass("position");
					$(this).closest(".ui-dialog").css("height", "auto");
					$(".ui-dialog .ui-dialog-content").removeClass(
							"height_reset");
					$(".ui-dialog-title").show();
				},
				"minimizeLocation" : "left"
			});
});

function getDictionaryVO() {  // 데이터베이스에서 데이터 가져오기
	isFromExcel = false;
	excelData = null;
	
	$.ajax({
		url: 'getDictionary.do',
	    type: 'POST',
	    success: function onData (data) {
	    	var dictionaryList = [];
	    	for(i=0;i<data[0].length;i++){
	    		dictionaryList.push({
	    			abbr_id: data[0][i].abbr_id,
	    			abbr: data[0][i].abbr,
	    			eng_full_nm: data[0][i].eng_full_nm,
	    			kor_full_nm: data[0][i].kor_full_nm,
	    			dic_desc: data[0][i].dic_desc
	    		});
	    	}
	    	// 가져온 데이터 리스트를 set에 넘겨줌
	    	setDictionaryVO(dictionaryList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
 // 데이터 검색 부분
function searchdictionary() {
	isFromExcel = false;
	excelData = null;
	
	var search_abbr = $('#search_Abbr').val();
	var search_eng = $('#search_Eng').val();
	var search_kor = $('#search_Kor').val();
	var search_desc = $('#search_Desc').val();	
	
		$.ajax({
			url: 'findDictionaryA.do',
		    type: 'POST',
		    data:{
		    	//데이터 넘겨 주는곳
		    	searchAbbr: search_abbr,
		    	searchEng: search_eng,
		    	searchKor: search_kor,
		    	searchDesc: search_desc
		    },
		    success: function onData (data) {
		    	//테이블 갱신
		    	var dictionaryList = [];
		    	for(i=0;i<data[0].length;i++){
		    		dictionaryList.push({
		    			abbr_id: data[0][i].abbr_id,
		    			abbr: data[0][i].abbr,
		    			eng_full_nm: data[0][i].eng_full_nm,
		    			kor_full_nm: data[0][i].kor_full_nm,
		    			dic_desc: data[0][i].dic_desc
		    		});
		    	}
		    	// 가져온 데이터 리스트를 set에 넘겨줌
		    	setDictionaryVO(dictionaryList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
}
document.addEventListener('keydown', enterkey);
function enterkey(e) {
	let focusEle = document.activeElement;
	if(document.getElementById('search_Abbr') == focusEle || document.getElementById('search_Eng') == focusEle || document.getElementById('search_Kor') == focusEle || document.getElementById('search_Desc') == focusEle){
	    if (e.keyCode == 13) {
	    	searchdictionary();
	    }
	}
}
function setDictionaryVO(dictionaryList) { // 넘어온 데이터 화면에 뿌려주기
	dictionarytable = new Tabulator("#dictionaryList", {
      selectable:1,//true
      data: dictionaryList,
      layout: "fitDataStretch",
      placeholder:"No Data Set",
      height:424,
      columns: [{
              title: "Abbrevation (약어)",
              field: "abbr",
              width: 122,
              hozAlign: "left"
          },
          {
              title: "English Full Name (영어명칭)",
              field: "eng_full_nm",
              width: 186,
              hozAlign: "left"
          },
          {
              title: "Korean Full Name (한글명칭)",
              field: "kor_full_nm",
              width: 186,
              hozAlign: "left"
          },
          {
              title: "Description (해설)",
              field: "dic_desc",
              hozAlign: "left"
          }
      ]
  });
	
	dictionarytable.on("rowSelected", function(row){
		$('#savebutton').css('display','none');
		$('#updatebutton').css('display','block');
		
		clickTb = row.getData();
		abbr_id = clickTb.abbr_id;
		
		$('#abbr').val(clickTb.abbr);
		$('#eng').val(clickTb.eng_full_nm);
		$('#kor').val(clickTb.kor_full_nm);
		$('#desc').val(clickTb.dic_desc);
		
	});
	
	dictionarytable.on("rowDeselected", function(row){
		$('#savebutton').css('display','block');
		$('#updatebutton').css('display','none');
		

		$('#abbr').val(null);
		$('#eng').val(null);
		$('#kor').val(null);
		$('#desc').html(null);
		$('#desc').val(null);
		
	});
}


/**
 * From Excel 버튼을 눌렀을 경우 작동
 * @returns
 */
function exceldictionary() {
	document.getElementsByName("dictionaryExcelfile")[0].click();
}

function dictionaryFileUp(obj) {
	let form = $('#dictionaryUploadForm_PathList')[0];
	let formData = new FormData(form);
	formData.set("prj_id", $('#selectPrjId').val());
	
	loadingWithMask();
	
	// 엑셀로부터 들어온 데이터 true
	isFromExcel = true;
	
	$.ajax({
		url : "dictionaryUpload.do",
		type : "POST",
		enctype : 'multipart/form-data',
		data : formData,
		processData : false,
		contentType : false,
		cache : false,
		//timeout : 600000,
		success : function(result) {
			form["dictionaryExcelfile"].value = "";
			closeLoadingWithMask();

			if (result.model.status != 'SUCCESS') {
				// error exception alert
				alert(result.model.status)
				return;
			}

			dictionaryGridSet(result.model.dataFromExcel);
			alert("Excel Loaded Successfully");
		},
		error : function(e) {
			console.log("ERROR : ", e);
			alert("fail");
			closeLoadingWithMask();
		}
	});
}

function dictionaryGridSet(data) {
	let tmpData = [];
	
	for (let i = 0; i < data.length; i++) {
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.abbr = row[colIdx++];
		getRow.eng_full_nm = row[colIdx++];
		getRow.kor_full_nm = row[colIdx++];
		getRow.dic_desc = row[colIdx++];
		
		tmpData.push(getRow);
	}
	setDictionaryVO(tmpData);
	if(isFromExcel)
		excelData = tmpData;
	
	dictionarytable.redraw();
}

function excelsavedicionary() {
	
	// 엑셀로부터 불러온 데이터만 저장 가능
	if(!isFromExcel){
		alert("Only data from excel can be saved.");
		return;
	}
	
	$(".pop_dictionarySaveConfirm").dialog("open");
}

function dictionarySaveConfirm() {
	$(".pop_dictionarySaveConfirm").dialog("close");

	loadingWithMask();
	
	$.ajax({ // save시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "DICTIONARY",
	    	method: "저장"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	let tmpGridData = excelData;
	
	// 변화가 있는 row들 json으로 변환
	let tmpStrings = [];

	for (let i = 0; i < tmpGridData.length; i++) {
		tmpStrings.push(JSON.stringify(tmpGridData[i]));
	}
	
	$.ajax({
		url : 'insertDictionaryExcel.do',
		type : 'POST',
		traditional : true,
		data : {
			//prj_id : selectPrjId,
			jsonRowdatas : tmpStrings
		},
		success : function onData(data) {
			//console.log(data[0]);
			closeLoadingWithMask();
			alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

			if (data[0] == "SUCCESS") {
				getDictionaryVO();
				return;
			}
		},
		error : function onError(error) {
			console.error(error);
			closeLoadingWithMask();
		}
	});
}

function dictionarySaveCancel() {
	$(".pop_dictionarySaveConfirm").dialog("close");
}

function newdictionary() {
//	console.log('데이터 비우기');
	$('#abbr').val(null);
	$('#eng').val(null);
	$('#kor').val(null);
	$('#desc').html(null);
	$('#desc').val(null);
	$('#updatebutton').css("display","none");
	$('#savebutton').css('display','block');
	dictionarytable.deselectRow();
	
}

function savedicionary() { // 데이터 추가
	var set_Abbr = ($("#abbr").val()).trim();
	var set_Eng = ($("#eng").val()).trim();
	var set_Kor = ($("#kor").val()).trim();
	var set_Decs = ($("#desc").val()).trim();

	
	if(set_Abbr == '' && set_Eng == '' && set_Kor == '' && set_Decs == ''){
		alert('저장할 내용을 입력해주세요.');
	}else{
		$.ajax({ // save시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "DICTIONARY",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		$.ajax({
			url: 'insertDictionary.do',
		    type: 'POST',
		    data:{
		    	//데이터 넘겨 주는곳
		    	abbr: set_Abbr,
		    	eng_full_nm: set_Eng,
		    	kor_full_nm: set_Kor,
		    	dic_desc : set_Decs	    	
		    },
		    success: function onData (data) {
		    	// 테이블 갱신
		    	getDictionaryVO();
				alert('저장이 완료 되었습니다.');
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		$('#abbr').val(null);
		$('#eng').val(null);
		$('#kor').val(null);
		$('#desc').html(null);
		$('#desc').val(null);
	}
}

function updatedicionary() { //데이터 수정
	var set_Abbr = ($("#abbr").val()).trim();
	var set_Eng = ($("#eng").val()).trim();
	var set_Kor = ($("#kor").val()).trim();
	var set_Decs = ($("#desc").val()).trim();
	
//	console.log(set_Abbr);
	if(abbr_id=='' || abbr_id==null){
		alert('수정할 테이블을 선택해주세요.')
	}else {
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "DICTIONARY",
		    	method: "수정"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
			$.ajax({
				url: 'updateDictionary.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
			    type: 'POST',    // 이동할때 포스트 방식으로 이동
			    data:{				// 이동할때 챙겨야하는 데이터
			    	abbr: set_Abbr,
			    	eng_full_nm: set_Eng,
			    	kor_full_nm: set_Kor,
			    	dic_desc : set_Decs,
			    	abbr_id: abbr_id
			    },
			    success: function onData (data) {
			    	// 테이블 갱신
			    	getDictionaryVO();
					alert('수정이 완료 되었습니다.');
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
}

function deletedictionary() { //데이터 삭제
	
	if(abbr_id=='' || abbr_id==null){
		alert('삭제할 테이블을 선택해주세요.');
	}else {

		$(".pop_deleteDictionConfirm").dialog("open");
	    $("#deleteDictionConfirmMessage").html("Would you like to delete?");
		
		//return;
//		if(chekdel == true) {
//			$.ajax({
//			    url: 'delDictionary.do',
//			    type: 'POST',
//			    data:{
//			    	abbr_id: abbr_id
//			    	//id 넘겨 주는 곳
//			    },
//			    success: function onData (data) {
//			    	// 테이블 갱신
//			    	getDictionaryVO();
//			    	//값 삭제
//			    	
//			    },
//			    error: function onError (error) {
//			        console.error(error);
//			    }
//			});
//			
//			$('#savebutton').css('display','block');
//			$('#updatebutton').css('display','none');
//			
//			$('#abbr').val(null);
//			$('#eng').val(null);
//			$('#kor').val(null);
//			$('#desc').html(null);
//			$('#desc').val(null);
//		}
	}

}


function deleteDictionConfirm() {
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "DICTIONARY",
	    	method: "삭제"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'delDictionary.do',
	    type: 'POST',
	    data:{
	    	abbr_id: abbr_id
	    	//id 넘겨 주는 곳
	    },
	    success: function onData (data) {
	    	// 테이블 갱신
	    	getDictionaryVO();
	    	//값 삭제
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$('#savebutton').css('display','block');
	$('#updatebutton').css('display','none');
	
	$('#abbr').val(null);
	$('#eng').val(null);
	$('#kor').val(null);
	$('#desc').html(null);
	$('#desc').val(null);
	$(".pop_deleteDictionConfirm").dialog("close");
}

function deleteDictionCancel() {
	$(".pop_deleteDictionConfirm").dialog("close");
}










