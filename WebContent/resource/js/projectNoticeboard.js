var Prjtable;
var clickTb;
var hitcnt = 0;
var contents;
var prj_id;
var bbs_seq;
var reg_id;
var user_id;
var toolbar;
var setting;
var selectPrjId = $('#selectPrjId').val();
var session_user_id = $('#session_user_id').val();
var prj_nm;
var PN = {};
var regUpdate;

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

//파일 리스트 번호
var fileIndex = 0;
// 등록할 전체 파일 사이즈
var totalFileSize = 0;
// 파일 리스트
var fileList = new Array();
// 파일 사이즈 리스트
var fileSizeList = new Array();
// 등록 가능한 파일 사이즈 MB
//var uploadSize = 10;
// 등록 가능한 총 파일 사이즈 MB
//var maxUploadSize = 20;

$(document).ready(function(){
	$('#allPrjCN').prop('disabled',true);
	
//	$.ajax({
//		url: 'getPrjName.do',
//		type: 'POST',
//		data:{
//			prj_id: selectPrjId
//		},
//		success: function onData (data) {
//			var noticeList = [];
//	    	for(i=0;i<data[0].length;i++){
//	    		noticeList.push({
//	    			prj_nm: data[0][i].prj_nm,			// 게시글 제목
//	    		});
//	    	}
//			
////			console.log(prj_nm);
//	    },
//	    error: function onError (error) {
//	        console.error(error);
//	    }
//	});
	
	toolbar = [
		// 스타일
		['style',['style']],
		// 굵기, 기울임꼴, 밑줄
		['style', ['bold', 'italic', 'underline']],
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
	
	$(".pop_detailProject").dialog({
        draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto"
    }).bind('dialogclose', function(event, ui) {
    	getProjectNoticeBoard();
    	
    	$("#project_titleW").val(null);
    	$("#summernote_projectnotice").summernote("code", "");
    	$('#allPrjCN').prop('disabled',false);
    	deleteAllFilePrj();
    	$('#detailViewFileWrap').empty();
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
	
	$(".pop_registProject").dialog({
        draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto"
    }).bind('dialogclose', function(event, ui) {
    	getProjectNoticeBoard();
    	
    	$("#project_titleW").val(null);
    	$("#summernote_projectnotice").summernote("code", "");
    	deleteAllFilePrj();
    	$('#detailViewFileWrap').empty();
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
	
	$(".pop_updatePrjNoticeConfirm").dialog({
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

	$(".pop_deletePrjNoticeConfirm").dialog({
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
	
	$(".pop_deletePrjNoticeFileConfirm").dialog({
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
	
	$("#summernote_projectnotice").summernote({
		//에디터 높이
		height: 300,
		//에디터 한글 설정
		lang: "ko_KR",
		placeholder: '게시글을 작성해주세요.',
		toolbar: toolbar,
		// 추가한 글꼴
		fontNames: fontList,
		fontNamesIgnoreCheck: fontList,
		//이미지 첨부하는 부분
		callbacks: {	//여기 부분이 이미지를 첨부하는 부분
			onImageUpload : function(files) {
				uploadSummernotePrjImageFile(files[0],this);
			},
			onPaste: function (e) {
				var clipboardData = e.originalEvent.clipboardData;
				if (clipboardData && clipboardData.items && clipboardData.items.length) {
					var item = clipboardData.items[0];
					if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
						e.preventDefault();
					}
				}
			}
		}
	});
	
	getProjectNoticeBoard();
	
});

function removeTabPRJNOTICE(){
	$(".pop_detailProject").remove();
	$(".pop_registProject").remove();
	Prjtable = null;
}

//게시판 상세 페이지 닫기 버튼
function cancledetail_Project() {
	$(".pop_detailProject").dialog("close");
	$("#project_titleW").val(null);
	$("#summernote_projectnotice").summernote("code", "");
	$('#allPrjCN').prop('disabled',false);
	deleteAllFilePrj();
	$('#detailViewFileWrap').empty();
	
	getProjectNoticeBoard();
}

// 저장된 데이터 꺼내오기
function getProjectNoticeBoard() {
//	console.log(selectPrjId);
	$.ajax({
		url: 'getProjectNoticeList.do',
	    type: 'POST',
	    data: {
	    	prj_id: selectPrjId
	    },
	    success: function onData (data) {
	    	var noticeList = [];
	    	for(i=0;i<data[0].length;i++){
		    	var code_nm = data[0][i].code_nm;
		    	var creater_nm;
	    		if(code_nm == null) {
	    			creater_nm = data[0][i].user_kor_nm;
	    		}else {
	    			creater_nm = data[0][i].user_kor_nm+'('+code_nm+')';
	    		}
	    		noticeList.push({
	    			prj_id: data[0][i].prj_id,
	    			bbs_seq: data[0][i].bbs_seq,
	    			reg_id: data[0][i].reg_id,
	    			contents: data[0][i].contents,
	    			No: (data[0].length+1)-(i+1),  								// 게시글 번호
	    			user_kor_nm: creater_nm,				// 등록 회원 이름
	    			hit_cnt: data[0][i].hit_cnt,			// 조회수
	    			reg_date: getDateFormat(data[0][i].reg_date),			// 등록일
	    			title: data[0][i].title					// 게시글 제목
	    		});
	    	}
	    	// 가져온 데이터 리스트를 set에 넘겨줌
	    	setProjectNoticeList(noticeList);
	    	
	    	var thisGrid = Prjtable;
			thisGrid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function searchProjectnotice()	{
	var search_atcP = $('#projectsearch_Atc').val();
	var text_atcP = $('#projecttext_Atc').val();
	
//	console.log(search_atcP, text_atcP);
	
	$.ajax({
		url: 'findProjectNotice.do',
		type: 'POST',
		data:{
			prj_id: selectPrjId,
			searchAtc: search_atcP,
			textAtc: text_atcP
		},
		success: function onData(data) {
			// 검색한 테이블 정보리스트
			var noticeList = [];
			for(i=0;i<data[0].length;i++){
		    	var code_nm = data[0][i].code_nm;
		    	var creater_nm;
	    		if(code_nm == null) {
	    			creater_nm = data[0][i].user_kor_nm;
	    		}else {
	    			creater_nm = data[0][i].user_kor_nm+'('+code_nm+')';
	    		}
	    		noticeList.push({
	    			prj_id: data[0][i].prj_id,
	    			bbs_seq: data[0][i].bbs_seq,
	    			reg_id: data[0][i].reg_id,
	    			contents: data[0][i].contents,
	    			No: (data[0].length+1)-(i+1),  								// 게시글 번호
	    			user_kor_nm: creater_nm,				// 등록 회원 이름
	    			hit_cnt: data[0][i].hit_cnt,			// 조회수
	    			reg_date: getDateFormat(data[0][i].reg_date),			// 등록일
	    			title: data[0][i].title					// 게시글 제목
	    		});
	    	}
	    	setProjectNoticeList(noticeList);
	    	
	    	var thisGrid = Prjtable;
			thisGrid.redraw();
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
}

document.addEventListener('keydown', enterkey);
function enterkey(e) {
	let focusEle = document.activeElement;
	if(document.getElementById('projecttext_Atc') == focusEle) {
		if(e.keyCode == 13) {
			searchProjectnotice();
		}	
	}
}

//DB 데이터 그리드로 화면출력
function setProjectNoticeList(noticeList) {
	
	if(Prjtable == null || Prjtable == undefined) {
		Prjtable = new Tabulator("#projectNoticeboardList", {
	        selectable:1,//true
	        data: noticeList,
	        layout: "fitColumns",
	        placeholder: "No Data Set",
			pagination : true,
			paginationSize : 100,
			Height : "100%",
	        columns: [{
	        	title: "PRJ ID",
                field: "prj_id",
                visible: false
	        },
	        {
                title: "NO",
                field: "No",
                width: 80
            },
            {
                title: "CREATOR",
                field: "user_kor_nm",
                width: 150,
                hozAlign: "left"
            },
            {
                title: "CLICK",
                field: "hit_cnt",
                width: 80
            },
            {
                title: "CREATION DATE",
                field: "reg_date",
                width: 200
            },
            {
                title: "TITLE",
                field: "title",
                hozAlign: "left"
            }
	        ]
	    });
		// 테이블 리스트 클릭시 상세페이지 팝업창 띄움
		Prjtable.on("rowClick", function(e, row){
			$('#detailViewFileWrap').empty();
			$(".pop_detailProject").dialog("open");
			var chkUser = $("#loginedUserId").val();
			var chkAdmin = $("#checkAdmin").val();
			
			clickTb = row.getData();
			
			console.log("prj_id = "+clickTb.prj_id);
			console.log("bbs_seq = "+clickTb.bbs_seq);
			prj_id = clickTb.prj_id;
			bbs_seq = clickTb.bbs_seq;
			$("#prj_id_Pnotice").val(prj_id);
			reg_id = clickTb.reg_id;
			contents = clickTb.contents;
			
			hitcnt = clickTb.hit_cnt;
			
			if(chkUser != reg_id && chkAdmin != 'Y') {
				$("#project_noticeupdate").css("display","none");
				$("#project_noticedelete").css("display","none");
			}else {
				$("#project_noticeupdate").css("display","block");
				$("#project_noticedelete").css("display","block");
			}
			
			$.ajax({
				url: 'getPrjNoticeBoard.do',
			    type: 'POST',    // 이동할때 포스트 방식으로 이동
			    data:{				// 이동할때 챙겨야하는 데이터
			    	prj_id: prj_id,
			    	bbs_seq: bbs_seq
			    },
			    success: function onData (data) {
			    	var code_nm = data[0].code_nm;
			    	var creater_nm;
			    	if(code_nm == null) {
		    			creater_nm = data[0].user_kor_nm;
		    		}else {
		    			creater_nm = data[0].user_kor_nm+'('+code_nm+')';
		    		}
					$('#project_title').val(data[0].title); 
					$('#project_writer').val(creater_nm);
					$('#project_createdate').val(getDateFormat(data[0].reg_date));
					$('#project_hits').val(data[0].hit_cnt);
					$('#project_contents').html(data[0].contents);
					
					detailPrjFileList(prj_id, bbs_seq);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		});
	}else {
		Prjtable.replaceData(noticeList);
	}
	
}

// 등록된 파일 리스트
function detailPrjFileList(prj_id, bbs_seq) {
	$.ajax({
		url: 'getDetailPrjFileList.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	prj_id: prj_id,
	    	bbs_seq: bbs_seq
	    },
	    success: function onData (data) {
//			console.log("data[0] length = ",data[0].length);
	    	
	    	var html = "";
	    	
	    	if(data[0].length == 0 || data[0][0] == null) {
	    		html += "<div id=\"emptyFile\">첨부 파일이 없습니다.</div>";
	    		
	    		$("#detailViewFileWrap").append(html);
	    	}else {
//	    		$("#detailViewFileWrap").niceScroll({
//	    			touchbehavior:false,
//	    			cursorcolor:"#cccccc",
//	    			cursoropacitymax:1,
//	    			cursorwidth:9,
//	    			cursorborder:"0",
//	    			background:'#efefef',
//	    			cursorborderradius:0
//	    		});
	    		
//	    		console.log("data2 = ",data);
		    	
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
		    	
		    	$("#detailViewFileWrap").append(html);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function fileDownload(path, fileName) {
	var $form = $('<form></form>');
	var fullPath = $('<input type="hidden" name="fullPath" value="' + path + '">');
	var fileName = $('<input type="hidden" name="fileName" value="' + fileName + '">');
	
//	console.log(fullPath, fileName);
	
	$form.attr('action', 'estimateDownload.do');
	$form.attr('method', 'post');
	$form.appendTo('body');
	$form.append(fullPath);
	$form.append(fileName);
	
	$form.submit();
}

// 게시판 수정 페이지 열기
function updatenotice_Project() {
	
	$(".pop_detailProject").dialog("close");
	$('#detailViewFileWrap').empty();
	$(".pop_registProject").dialog("open");
	//$('#allPrjCN').prop('disabled',true);
	
	// 페이지 들어갈 수 있는 권한
	$.ajax({
		url: 'chkPrjUpNotice.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	bbs_seq: bbs_seq,
	    	reg_id: reg_id
	    },
	    success: function onData (data) {
	    	
	    	if(data < 0){
	    		alert("수정 권한이 없습니다.");
	    	}else if(data = 0) {
	    		alert("수정 오류");
	    	}else{
	    		if(reg_id == $('#loginedUserId').val() || $('#checkAdmin').val() == 'Y') {
	    			prj_id = $("#prj_id_Pnotice").val();
	    			$.ajax({
	    				url: 'getPrjNoticeBoardUpdate.do',
	    			    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    			    data:{				// 이동할때 챙겨야하는 데이터
	    			    	prj_id: prj_id,
	    			    	bbs_seq: bbs_seq
	    			    },
	    			    success: function onData (data) {
	    		    		if(data[0].contents_type == 'N') {
	    		    			document.getElementById("allPrjCN").checked = true;
	    		    		}else if(data[0].contents_type == 'C') {
	    		    			document.getElementById("allPrjCN").checked = false;
	    		    		}
	    		    		$('#savenoticebP').css('display','none');
	    		    		$('#updatenoticebP').css('display','inline-block');
	    		    		
	    		    		/*$("#summernote_projectnotice").summernote({
	    		    			//에디터 높이
	    		    			height: 300,
	    		    			//에디터 한글 설정
	    		    			lang: "ko_KR",
	    		    			placeholder: '게시글을 작성해주세요.',
	    		    			toolbar: toolbar,
	    		    			// 추가한 글꼴
	    		    			fontNames: fontList,
	    		    			fontNamesIgnoreCheck: fontList
	    		    		});*/
	    			    	
	    			    	$('#project_titleW').val(data[0].title); 
	    			    	$('#summernote_projectnotice').summernote('code',data[0].contents);
	    			    	
	    			    	
	    			    	updatePrjFileList();
//	    					detailPrjFileList();
	    			    },
	    			    error: function onError (error) {
	    			        console.error(error);
	    			    }
	    			});
	    			
		    		bbs_seq = clickTb.bbs_seq;
		    		
		    		return false;
		    		
		    	}else {
		    		alert('수정 권한이 없습니다.');
		    	}
	    	}
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }		
	});
}

// 수정페이지 오픈시 저장된 파일 정보 가져오기
function updatePrjFileList() {
	prj_id = $("#prj_id_Pnotice").val();
	$.ajax({
		url: 'getDetailPrjFileList.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	prj_id: prj_id,
	    	bbs_seq: bbs_seq
	    },
	    success: function onData (data) {
//			console.log("data[0] length = ",data[0].length);
	    	
	    	var html = "";
	    	
	    	if(data[0].length == 0 || data[0][0] == null) {
	    		$("#dropZoneInfo").css("display","block");
	    	}else {
		    	
		    	for(var i=0; i<data[0].length; i++) {
		    		
		    		var fIndex = data[0][i].file_seq;
		    		var fileName = data[0][i].rfile_nm;
		    		var fileSize = data[0][i].file_size / 1024 / 1024;
		    		
//		    		console.log(""+fIndex,fileName,fileSize);
		    		
		    		var fileSizeTxt = fileSize;
		    		
		    		if(fileSize < 1) {
		    			fileSizeTxt = (fileSize * 1024).toFixed(1) + "KB";
		    		}else {
		    			fileSizeTxt = (fileSize).toFixed(1) + "MB";
		    		}
		    		
		    		totalFileSize += fileSize;
		    		
		    		
		    		$("#dropZoneInfo").css("display","none");
		    		
		    		html += "<ul id=\"fileTr_View_"+fIndex+"\" class=\"file-ul view\">";
		    		html += "<li title='"+fileName+"'>"+fileName+"</li>";
		    		html += "<li>"+fileSizeTxt+"</li>";
		    		html += "<li class=\"file-delete-btn\" onclick=\"deleteViewFilePrj("+fIndex+"); return false;\"><i class=\"fas fa-times\"></i></li>";
		    		html += "</ul>";
		    	}
		    	
		    	var totalFileSizeTxt;
		    	
		    	if(totalFileSize < 1){
		    		totalFileSizeTxt = (totalFileSize * 1024).toFixed(1) + "KB";
		        }else{
		        	totalFileSizeTxt = (totalFileSize).toFixed(1) + "MB";
		        }
		    	
		    	$("#totalFileSize").text(totalFileSizeTxt);
		    	$('#dropZone').append(html);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}	

// 수정/등록 취소 버튼 
function cancleboard_Project() {
//	var chek_Cancle = confirm('게시물 등록/수정을 취소하시겠습니까?');
	
//	if(chek_Cancle == true){
		$(".pop_registProject").dialog("close");
//		$('.ui-widget-overlay').css('display','none');
		
//		console.log(bbs_seq);
		
		$('#allPrjCN').prop('disabled',false);
		getProjectNoticeBoard();
		
		$("#project_titleW").val(null);
		$("#summernote_projectnotice").summernote("code", "");
		deleteAllFilePrj();
//	}

}

// 게시판 수정 버튼
function updateboard_Project() {
	$(".pop_updatePrjNoticeConfirm").dialog("open");	
}

function updatePrjNoticeYes() {
	regUpdate = 0;
	$(".pop_updatePrjNoticeConfirm").dialog("close");
	
	var set_Title = $("#project_titleW").val();
	var set_Contents = $("#summernote_projectnotice").summernote("code");
	
//	console.log(bbs_seq);
		prj_id = $("#prj_id_Pnotice").val();
		var chkBbs = $('#bbs_seqPnotice').val(bbs_seq);
		var chkPrjid = $('#prj_idPnotice').val(prj_id);
		
//		console.log(bbs_seq + selectPrjId);
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT NOTICE BOARD",
		    	method: "수정"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		// 전체공지 할지 안할지 체크 여부
		var chkAllPrjCN = $("#allPrjCN").is(":checked");
		
		if(chkAllPrjCN == true) { // 전체공지
			$.ajax({
				url: 'updatePrjNotice.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
			    type: 'POST',    // 이동할때 포스트 방식으로 이동
			    async: false,
			    data:{				// 이동할때 챙겨야하는 데이터
			    	title: set_Title,
			    	contents: set_Contents,
					contents_type: "N",
			    	bbs_seq: bbs_seq
			    },
			    success: function onData (data) {
			    	
			    	if(data < 0){
			    		alert("수정 권한이 없습니다.");
			    		return;
			    	}else if(data = 0) {
			    		alert("수정 오류");
			    		return;
			    	}else{
	//		    		var chkBbs = $('#bbs_seqPnotice').val(bbs_seq);
	//		    		var chkPrjid = $('#prj_idPnotice').val(selectPrjId);
	//		    		
	//		    		console.log(bbs_seq + selectPrjId);
	//		    		
	//		    		updateFile();
			    		
			    		getProjectNoticeBoard();
	
	    				// 홈 게시판 테이블 갱신
			    		getprjNhome();
				    	//alert("수정이 완료되었습니다.");
			    		
			    	}
	//		    	console.log(data[0]);
			    },
			    error: function onError (error) {
			        console.error(error);
			        alert("수정 오류");
			    }
			});
		}else {
			$.ajax({
				url: 'updatePrjNotice.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
			    type: 'POST',    // 이동할때 포스트 방식으로 이동
			    async: false,
			    data:{				// 이동할때 챙겨야하는 데이터
			    	title: set_Title,
			    	contents: set_Contents,
					contents_type: "C",
			    	bbs_seq: bbs_seq
			    },
			    success: function onData (data) {
			    	
			    	if(data < 0){
			    		alert("수정 권한이 없습니다.");
			    		return;
			    	}else if(data = 0) {
			    		alert("수정 오류");
			    		return;
			    	}else{
	//		    		var chkBbs = $('#bbs_seqPnotice').val(bbs_seq);
	//		    		var chkPrjid = $('#prj_idPnotice').val(selectPrjId);
	//		    		
	//		    		console.log(bbs_seq + selectPrjId);
	//		    		
	//		    		updateFile();
			    		
			    		getProjectNoticeBoard();
	
			    		// 홈 게시판 테이블 갱신
			    		getprjNhome();
				    	//alert("수정이 완료되었습니다.");
			    		
			    	}
	//		    	console.log(data[0]);
			    },
			    error: function onError (error) {
			        console.error(error);
			        alert("수정 오류");
			    }
			});
		}

		checkFile_project();
}

function updatePrjNoticeCancle() {
	
	let pbo_folder_id = folderList.find(i=>i.type=="PBO").id;
	let board_auth = checkFolderAuth(prj_id,pbo_folder_id);
	
	// 쓰기권한이 없음
	if(board_auth.w != 'Y'){
		alert("There is no Write permission.");
		return;
	}
	
	
	$(".pop_updatePrjNoticeConfirm").dialog("close");
}

// 게시판 등록 페이지 열기
function registerProjectnotice() {
	
	let pbo_folder_id = folderList.find(i=>i.type=="PBO").id;
	let board_auth = checkFolderAuth(selectPrjId,pbo_folder_id);
	
	// 쓰기권한이 없음
	if(board_auth.w != 'Y'){
		alert("There is no Write permission.");
		return;
	}
	
	
	
	$(".pop_registProject").dialog("open");
	document.getElementById("allPrjCN").checked = false;
	
	$('#savenoticebP').css('display','inline-block');
	$('#updatenoticebP').css('display','none');
	
	/*$("#summernote_projectnotice").summernote({
		//에디터 높이
		height: 300,
		//에디터 한글 설정
		lang: "ko_KR",
		//placeholder: '게시글을 작성해주세요.',
		toolbar: toolbar,
		// 추가한 글꼴
		fontNames: fontList,
		fontNamesIgnoreCheck: fontList
	});*/
	
	//계정이 ADMIN, DCC일 경우 전체공지 disabled false처리
	$.ajax({
		url: 'checkAdminDccPrjNotice.do',
		type: 'POST',
		async: false,
		data:{
			prj_id: selectPrjId,
			user_id: session_user_id
		},
		success: function onData (data) {
			if(data > 0) {
				$('#allPrjCN').prop('disabled',false);
			}else if(data < 0) {
				$('#allPrjCN').prop('disabled',true);
			}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// 게시판 등록(저장)
function saveboard_Project() {
//	uploadFile()
	regUpdate = 1;
	var set_Title = $("#project_titleW").val();
	var set_Contents = $("#summernote_projectnotice").summernote("code");
	// 전체공지 할지 안할지 체크 여부
	var chkAllPrjCN = $("#allPrjCN").is(":checked");
	
	if(set_Title=='') {
		alert('제목을 작성해주세요.');
	}else {
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT NOTICE BOARD",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		if(chkAllPrjCN == true) { // 전체공지
			$.ajax({
				url: 'insertPrjNotice.do',
				type: 'POST',
				async: false,
				data:{
					prj_id: selectPrjId,
					contents_type: "N",
					title: set_Title,
					contents: set_Contents
				},
				success: function onData (data) {
					if(data == null || data == "") {
			    		alert("등록 오류");
			    		return;
			    	}else {
						$('#bbs_seqPnotice').val(data.bbs_seq);
						$('#prj_idPnotice').val(selectPrjId);
//						var fileUpload = uploadFile();
						
//						console.log(fileUpload);
						
//						if(fileUpload === false){
//	    	        		swal({
//	    	    		        title: "",
//	    	    		        text: "첨부 파일 업로드에 실패했습니다. 새로고침 후 다시 시도해주세요."
//	    	    		    },
//	    	    		    function () {
//	    	    		    	return;
//	    	                });	
//	    	        	}else {
//	    	        		// 테이블 갱신
	        				getProjectNoticeBoard();
	        				// 홈 게시판 테이블 갱신
	        				getprjNhome();
	        				//alert('등록이 완료되었습니다.');
//	    	        	}
			    	}
			    },
			    error: function onError (error) {
			        console.error(error);
			        alert("등록 오류");
			    }
			});
		}else { // 프로젝트 내 게시
			$.ajax({
				url: 'insertPrjNotice.do',
				type: 'POST',
				async: false,
				data:{
					prj_id: selectPrjId,
					contents_type: "C",
					title: set_Title,
					contents: set_Contents
				},
				success: function onData (data) {
					if(data == null || data == "") {
			    		alert("등록 오류");
			    		return;
			    	}else {
						$('#bbs_seqPnotice').val(data.bbs_seq);
						$('#prj_idPnotice').val(selectPrjId);
//						var fileUpload = uploadFile();
						
//						console.log(fileUpload);
						
//						if(fileUpload === false){
//	    	        		swal({
//	    	    		        title: "",
//	    	    		        text: "첨부 파일 업로드에 실패했습니다. 새로고침 후 다시 시도해주세요."
//	    	    		    },
//	    	    		    function () {
//	    	    		    	return;
//	    	                });	
//	    	        	}else {
//	    	        		// 테이블 갱신
	        				getProjectNoticeBoard();
	        				// 홈 게시판 테이블 갱신
				    		getprjNhome();
	        				//alert('등록이 완료되었습니다.');
//	    	        	}
			    	}
			    },
			    error: function onError (error) {
			        console.error(error);
			        alert("등록 오류");
			    }
			});
		}
		checkFile_project();
//		deleteAllFile();
	}
}

// 게시판 삭제 버튼
function deleteboard_Project() {	
	
	let pbo_folder_id = folderList.find(i=>i.type=="PBO").id;
	let board_auth = checkFolderAuth(prj_id,pbo_folder_id);
	
	// 쓰기권한이 없음
	if(board_auth.d != 'Y'){
		alert("There is no Delete permission.");
		return;
	}
	
	$(".pop_deletePrjNoticeConfirm").dialog("open");	
}

function deletePrjNoticeYes() {
	$(".pop_deletePrjNoticeConfirm").dialog("close");
	
//	if(reg_id == $('#loginedUserId').val() || $('#checkAdmin').val() == 'Y') {
	prj_id = $("#prj_id_Pnotice").val();
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "PROJECT NOTICE BOARD",
		    	method: "삭제"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		$.ajax({
		    url: 'deletePrjNotice.do',
		    type: 'POST',
		    data:{
		    	bbs_seq: bbs_seq,
		    	reg_id: reg_id,
		    	prj_id: prj_id
		    	//id 넘겨 주는 곳
		    },
		    success: function onData (data) {
		    	
		    	if(data < 0){
		    		alert("삭제 권한이 없습니다.");
		    	}else if(data < 1) {
		    		alert("삭제 오류");
		    	}else{
		    		// 테이블 갱신
		    		getProjectNoticeBoard();
		    		// 홈 게시판 테이블 갱신
		    		getprjNhome();
		    		alert("삭제가 완료되었습니다.");
			    	//값 삭제
		    	}		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		        alert("삭제 오류");
		    }
		});
		deleteAllFilePrj();
		$(".pop_detailProject").dialog("close");
//}else {
//	alert('삭제 권한이 없습니다.');
//}
}

function deletePrjNoticeCancle() {
	$(".pop_deletePrjNoticeConfirm").dialog("close");
}


function directSelectFile() {
	var file = document.getElementById("directFile").files;
	//console.log("directSelectFile = "+file);
	selectFile(file);
}

$(function (){
    // 파일 드롭 다운
    fileDropDown();
});

// 파일 드롭 다운
function fileDropDown() {
	var dropZone = $("#dropZone");
	// Drag 기능
	dropZone.on('dragenter',function(e){
		e.stopPropagation();
		e.preventDefault();
	});
	dropZone.on('dragleave',function(e){
		e.stopPropagation();
		e.preventDefault();
	});
	dropZone.on('dragover',function(e){
        e.stopPropagation();
        e.preventDefault();
    });
	dropZone.on('drop',function(e){
		e.preventDefault();
		
		var files = e.originalEvent.dataTransfer.files;
//		console.log(files);
		
		if(files != null){
			if(files.length < 1){
				alert("폴더는 업로드가 불가능합니다.");
				return;
			}
			selectFile(files);
		}else {
			alert("파일 추가에 실패했습니다. 새로고침 후 다시 시도해주세요.");
		}
	});
}



// 파일 선택시
function selectFile(fileObject){
	
	//console.log("fileObject = "+fileObject);
	var files = null;
	
	if(fileObject != null) {
		//파일 Drag 이용하여 등록시
		files = fileObject;
	}else{
		// 직접 파일 등록시
		files = $('#multipaartFileList_' + fileIndex)[0].files;
	}
	
	// 다중파일 등록
    if(files != null){
    	for(var i = 0; i < files.length; i++){
    		// 파일 이름
            var fileName = files[i].name;
            var fileNameArr = fileName.split("\.");
            // 확장자
            var ext = fileNameArr[fileNameArr.length - 1];
            // 파일 사이즈(단위 :MB)
            var fileSize = files[i].size / 1024 / 1024;
            
            var fileSizeTxt = fileSize;
            
            if(fileSize < 1){
            	fileSizeTxt = (fileSize * 1024).toFixed(1) + "KB";
            }else{
            	fileSizeTxt = (fileSize).toFixed(1) + "MB";
            }
            //console.log(i+"번째 = "+files[i].name);
            if($.inArray(ext, [
            	"xlsx", "xlsm", "xlsb", "xltx", "xltm", "xls", "xml", "xlam", "xla", "xlw", "xlr",
         		"txt", "png", "bmp", "gif", "jpg", "jpeg", "tif", "pdf", "ppt", "pptx", "pptm", "doc", "docm", "docx",
         		"hwpx", "hwp","zip"
            ]) >= 0){ // 선택한 확장자가 리스트에 포함됐을때만 가져오기
                // 전체 파일 사이즈
            	totalFileSize += fileSize;
                
                // 파일 배열에 넣기
                fileList[fileIndex] = files[i];
                
                // 파일 사이즈 배열에 넣기
                fileSizeList[fileIndex] = fileSize;

                // 업로드 파일 목록 생성
                addFileList(fileIndex, fileName, fileSize, fileSizeTxt);

                // 파일 번호 증가
                fileIndex++;
            }else{
                // 확장자 체크
                alert("첨부가 불가능한 파일입니다.");
                break;
            }
    	}
    	
    	var totalFileSizeTxt;
    	
    	if(totalFileSize < 1){
    		totalFileSizeTxt = (totalFileSize * 1024).toFixed(1) + "KB";
        }else{
        	totalFileSizeTxt = (totalFileSize).toFixed(1) + "MB";
        }
    	
    	$("#totalFileSize").text(totalFileSizeTxt);
    }else{
        alert("ERROR");
    }
}

function addFileList(fIndex, fileName, fileSize, fileSizeTxt) {
	$("#dropZoneInfo").css("display","none");
	
	var html = ""
	html += "<ul id=\"fileTr_"+fIndex+"\" class=\"file-ul\">";
	html += "<li title='"+fileName+"'>"+fileName+"</li>";
	html += "<li>"+fileSizeTxt+"</li>";
	html += "<li class=\"file-delete-btn\" onclick=\"deleteFilePrj("+fIndex+"); return false;\"><i class=\"fas fa-times\"></i></li>";
	html += "</ul>";
	
	$('#dropZone').append(html);
}

// 업로드 파일 삭제
function deleteFilePrj(fIndex) {
	// 전체 파일 사이즈 수정
    totalFileSize -= fileSizeList[fIndex];
	
    // 파일 배열에서 삭제
    delete fileList[fIndex];
    
    // 파일 사이즈 배열 삭제
    delete fileSizeList[fIndex];
    
    // 업로드 파일 테이블 목록에서 삭제
    $("#fileTr_" + fIndex).remove();
    
    if(totalFileSize == 0) {
    	$("#dropZoneInfo").css("display","block");
    }
	
    var totalFileSizeTxt;
    
    if(totalFileSize < 1){
    	totalFileSizeTxt = (totalFileSize * 1024).toFixed(1) + "KB";
    }else{
    	totalFileSizeTxt = (totalFileSize).toFixed(1) + "MB";
    }
	
    $("#totalFileSize").text(totalFileSizeTxt);
    
}

var prjNoticefIndex;
function deleteViewFilePrj(fIndex) {
	$(".pop_deletePrjNoticeFileConfirm").dialog("open");
	prjNoticefIndex = fIndex;
}

function deletePrjNoticeFileYes() {
	$(".pop_deletePrjNoticeFileConfirm").dialog("close");

	prj_id = $("#prj_id_Pnotice").val();
	
	$.ajax({
		url: 'deleteFile.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	file_seq: prjNoticefIndex,
	    	prj_id: prj_id
	    },
	    success: function onData (data) {
//	    	console.log(data);
//	    	
//	    	$("#fileTr_View_" + fIndex).remove();
//	    	
//	    	if($(".file-ul .view").length == 0) {
//	    		$("#dropZoneInfo").css("display","block");
//	    	}
	    	
	    	// 전체 파일 사이즈 수정
	        totalFileSize -= fileSizeList[prjNoticefIndex];
	    	
	        // 파일 배열에서 삭제
	        delete fileList[prjNoticefIndex];
	        
	        // 파일 사이즈 배열 삭제
	        delete fileSizeList[prjNoticefIndex];
	        
	        // 업로드 파일 테이블 목록에서 삭제
	        $("#fileTr_View_" + prjNoticefIndex).remove();
	        
	        if(totalFileSize == 0) {
	        	$("#dropZoneInfo").css("display","block");
	        }
	        
	        var totalFileSizeTxt;
	        
	        if(totalFileSize < 1){
	        	totalFileSizeTxt = (totalFileSize * 1024).toFixed(1) + "KB";
	        }else{
	        	totalFileSizeTxt = (totalFileSize).toFixed(1) + "MB";
	        }
	    	
	        $("#totalFileSize").text(totalFileSizeTxt);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function deletePrjNoticeFileCancle() {
	$(".pop_deletePrjNoticeFileConfirm").dialog("close");
}

//리셋 버튼 클릭시
function deleteAllFilePrj() {
//	console.log("전체 삭제");
	
	totalFileSize = 0;
	fileSizeList = [];
	fileList = [];
	
	$("#dropZone .file-ul").remove();
	$("#dropZoneInfo").css("display","block");
	
	var totalFileSizeTxt;
	
	if(totalFileSize < 1) {
		totalFileSizeTxt = (totalFileSize * 1024).toFixed(1) + "KB";
	}else{
    	totalFileSizeTxt = (totalFileSize).toFixed(1) + "MB";
    }
	
	$("#totalFileSize").text(totalFileSizeTxt);
}

//파일 등록 체크
function checkFile_project() {
	// 등록할 파일 리스트
	var uploadFileList = Object.keys(fileList);
	
	// 등록할 파일 리스트를 formData로 데이터 입력
	var form = $('#uploadForm_prjN')[0];
	var formData = new FormData(form);
	
	for(var i=0; i<uploadFileList.length; i++) {
		formData.append('files', fileList[uploadFileList[i]]);
	}

	
	$.ajax({
		url: 'prjNoticeCheckFile.do',
		type: 'POST',
		data: formData,
		enctype:'multipart/form-data',
        processData:false,
        contentType:false,
        dataType:'json',
        cache:false,
        beforeSend:function(){
			$('.pop_registProject').prepend(progressbar);
        },
		complete:function(){
			$('.pop_registProject .progressDiv').remove();
		},
		xhr: function () {
            //XMLHttpRequest 재정의 가능
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
              //progress 이벤트 리스너 추가
              var percent = (e.loaded * 100) / e.total;
              //$('.pop_registProject .progressDiv').css({'position':'absolute','width':'100%','height':'100%','z-index':'1','background-color': 'rgba( 255, 255, 255, 0.5 )'});
              //$('.pop_registProject .progressDiv .bar').css({'width': percent+'%','height':'30px','position':'absolute','top':'50%','transform':'translate(0%,-50%)'});
              $('.pop_registProject .progressDiv .bar').css('width', percent+'%');
            };
            return xhr;
        },
        //async: false,
		success: function onData (data) {
			if(data > 0) {
				//uploadFile_project();
				deleteAllFilePrj();
				$(".pop_registProject").dialog("close");
				$("#project_titleW").val(null);
				$("#summernote_projectnotice").summernote("code","");
				
				if(regUpdate == 0) {
					alert('수정이 완료되었습니다.');
				}else if(regUpdate == 1) {
					alert('등록이 완료되었습니다.');
				}
			}else {
				alert("Unsupported file format. Please contact the administrator");
				//getProjectNoticeBoard();
			}
		},
		error: function onError (error) {
	    	console.error(error);
//	        alert("등록 오류");
	    }
	});
	
}

//파일 등록
//function uploadFile_project(){
//	 // 등록할 파일 리스트
//    var uploadFileList = Object.keys(fileList);
//    
//    //console.log("uploadFileList = "+uploadFileList);
//    
//    // 등록할 파일 리스트를 formData로 데이터 입력
//    var form = $('#uploadForm_prjN')[0];
//    var formData = new FormData(form);
//    
//    for(var i=0; i<uploadFileList.length; i++){
//        formData.append('files', fileList[uploadFileList[i]]);
//        //console.log("uploadFileList[i] = "+uploadFileList[i]); // uploadFileList[i] = 0 1
//    }
//    
//  //console.log("uploadFileList.length = "+uploadFileList.length); // uploadFileList.length = 2
//  
//    $.ajax({
//		url: 'fileUploadPrjNotice2.do',
//		type: 'POST',
//		data: formData,
//		enctype:'multipart/form-data',
//        processData:false,
//        contentType:false,
//        dataType:'json',
//        cache:false,
//		success: function onData (data) {
//			
//			if(data != null) {
//				deleteAllFile();
//	            
//				// 테이블 갱신
//				//getProjectNoticeBoard();
////				return data;
//			}
//
//			$(".pop_registProject").dialog("close");
//			$("#project_titleW").val(null);
//			$("#summernote_projectnotice").summernote("code","");
//			
//			alert('등록이 완료되었습니다.');
//		},
//		error: function onError (error) {
//	    	console.error(error);
////	        alert("등록 오류");
//	    }
//	});
//    
//}

// 파일 업데이트
//function updateFile() {
//	console.log("파일 수정");
//	 // 등록할 파일 리스트
//   var uploadFileList = Object.keys(fileList);
//   
//   // 등록할 파일 리스트를 formData로 데이터 입력
//   var form = $('#uploadForm_prjN')[0];
//   var formData = new FormData(form);
//   
//   for(var i=0; i<uploadFileList.length; i++){
//       formData.append('files', fileList[uploadFileList[i]]);
////       console.log("uploadFileList[i] = "+uploadFileList[i]); // uploadFileList[i] = 0 1
//   }
//   
//// console.log("uploadFileList.length = "+uploadFileList.length); // uploadFileList.length = 2
//   
//   
//   $.ajax({
//		url: 'fileUploadPrjNotice.do',
//		type: 'POST',
//		data: formData,
//		enctype:'multipart/form-data',
//       processData:false,
//       contentType:false,
//       dataType:'json',
//       cache:false,
//		success: function onData (data) {
//			
//			if(data != null) {
//				deleteAllFile();
//	            
//				// 테이블 갱신
////				getProjectNoticeBoard();
////				alert('수정이 완료되었습니다.');
////				return data;
//			}
//		},
//		error: function onError (error) {
//	    	console.error(error);
////	        alert("등록 오류");
//	    }
//	});
//}

/**
* 썸머노트 이미지 파일 업로드
*/
function uploadSummernotePrjImageFile(file, editor) {
	data = new FormData();
	data.append("prj_id", selectPrjId);
	data.append("file", file);
	
	console.log("data = "+data);
	
	$.ajax({
		url : "uploadSummernotePrjImageFile.do",
		type : "POST",
		data : data,
		enctype:'multipart/form-data',
		contentType : false,
		processData : false,
		success : function(data) {
        	//항상 업로드된 파일의 url이 있어야 한다.
			$(editor).summernote('code', $(editor).summernote('code')+'<img src="'+current_server_domain+'/getSummernotePrjImage.do?sfile_nm='+data[0]+";"+selectPrjId+'">');
			
		}
	});
}



