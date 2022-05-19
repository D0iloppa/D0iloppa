var Noticetable;
var clickTb = 0;
var hitcnt = 0;
var contents;
var bbs_seq;
var reg_id;
var user_id;
var toolbar;
var setting;
//var save_day = new Date();
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
var fileIndexN = 0;
// 등록할 전체 파일 사이즈
var totalFileSizeN = 0;
// 파일 리스트
var fileListN = new Array();
// 파일 사이즈 리스트
var fileSizeListN = new Array();
// 등록 가능한 파일 사이즈 MB
//var uploadSize = 10;
// 등록 가능한 총 파일 사이즈 MB
//var maxUploadSize = 20;


$( document ).ready(function(){
	
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
	
	$(".pop_detailP").dialog({
        draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto"
    }).bind('dialogclose', function(event, ui) {
    	getNoticeBoard();
    	
    	$("#titleW").val(null);
    	$("#summernote").summernote("code", "");
    	deleteAllFile();
    	$('#detailViewFileWrapN').empty();
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
	
	$(".pop_registP").dialog({
        draggable: true,
        autoOpen: false,
        maxWidth:1000,
        width:"auto"
    }).bind('dialogclose', function(event, ui) {
    	getNoticeBoard();
		
    	$("#titleW").val(null);
    	$("#summernote").summernote("code", "");
    	deleteAllFile();
    	$('#detailViewFileWrapN').empty();
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
	
//	$(".pop_registupP").dialog({
//        draggable: true,
//        autoOpen: false,
//        maxWidth:1000,
//        width:"auto"
//    });
	
//	$('.ui-widget-overlay').css('display','none');	
	
	$(".pop_updateNoticeConfirm").dialog({
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

	$(".pop_deleteNoticeConfirm").dialog({
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
	
	$(".pop_deleteNoticeFileConfirm").dialog({
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
	
	$("#summernote").summernote({
		//에디터 높이
		height: 300,
		focus: true,
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
				uploadSummernoteImageFile(files[0],this);
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
	
	getNoticeBoard();
	
});

function removeTabNOTICE(){
	$(".pop_detailP").remove();
	$(".pop_registP").remove();
	Noticetable = null;
}
function getNoticeBoard() { // 데이터 베이스에서 데이터 가져오기
	$.ajax({
		url: 'getNoticeList.do',
	    type: 'POST',
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
	    	setNoticeList(noticeList);

	    	var thisGrid = Noticetable;
			thisGrid.redraw();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

//게시판 검색
function searchnotice() {
//	console.log($('#search_Atc').val());   //select option 값을 가져옴
	var search_atc = $('#search_Atc').val();
//	console.log($('#text_Atc').val());     text 입력값을 가져옴
	var text_atc = $('#text_Atc').val();
	
	$.ajax({
		url: 'findNoticeList.do',
		type: 'POST',
		data:{
			searchAtc: search_atc,
			textAtc: text_atc
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
			setNoticeList(noticeList);
			
			var thisGrid = Noticetable;
			thisGrid.redraw();
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});	
	
}
$(document).keydown(function(event) {
	 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
	let focusEle = document.activeElement;
	if(document.getElementById('text_Atc') == focusEle){
		if(event.keyCode == 13 || event.which == 13) {
			searchnotice();
		}
	}
});


function setNoticeList(noticeList) {
	
	if ( Noticetable == null || Noticetable == undefined) {
		Noticetable = new Tabulator("#noticeBoardList", {
	        selectable:1,//true
	        data: noticeList,
	        layout: "fitColumns",
	        placeholder:"No Data Set",
			pagination : true,
			paginationSize : 100,
			Height : "100%",
	        columns: [{
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
//		table.ondblclick("rowSelected", function(row){
//		table.on("dblclick","rowSelected", function(row){
//		table.dblclick("rowSelected", function(row){
		Noticetable.on("rowClick", function(e, row){

			$('#detailViewFileWrapN').empty();
			$(".pop_detailP").dialog("open");
			
			var chkUser = $("#loginedUserId").val();
			var chkAdmin = $("#checkAdmin").val();
			
			clickTb = row.getData();
			bbs_seq = clickTb.bbs_seq;
			reg_id = clickTb.reg_id;
			contents = clickTb.contents;		
			hitcnt = clickTb.hit_cnt;
			
			if(chkUser != reg_id && chkAdmin != 'Y') {
				$("#noticeupdate").css("display","none");
				$("#noticedelete").css("display","none");
			}else {
				$("#noticeupdate").css("display","block");
				$("#noticedelete").css("display","block");
			}

//			$('.ui-widget-overlay').css('display','block');

			$.ajax({
				url: 'getNoticeBoard.do',
			    type: 'POST',    // 이동할때 포스트 방식으로 이동
			    data:{				// 이동할때 챙겨야하는 데이터
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
					$('#title').val(data[0].title); 
					$('#writer').val(creater_nm);
					$('#createdate').val(getDateFormat(data[0].reg_date));
					$('#hits').val(data[0].hit_cnt);
					$('#contents').html(data[0].contents);
					
					detailFileList();
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
			
			
//			if($('#loginedUserId').val() == reg_id || $('#checkAdmin').val() == 'Y') {
//				$('#noticeupdate').css('display','block');
//				$('#noticedelete').css('display','block');
//			}else {
//				$('#noticeupdate').css('display','none');
//				$('#noticedelete').css('display','none');
//			}
//			$('#atfile').val(clickTb.);  파일 불러오는 자리
		});
	} else {
		Noticetable.replaceData(noticeList);
	}
	
	
	
}

// 등록된 파일 리스트 가져오기
function detailFileList() {
	$.ajax({
		url: 'getDetailFileList.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	bbs_seq: bbs_seq
	    },
	    success: function onData (data) {
	    	
	    	var html = "";
	    	
	    	if(data[0].length == 0 || data[0][0] == null){
	    		html += "<div id=\"emptyFile\">첨부 파일이 없습니다.</div>";
	    		
	    		$("#detailViewFileWrapN").append(html);
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
		    	
		    	$("#detailViewFileWrapN").append(html);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

// 상세페이지 파일리스트 클릭시 다운
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

// 게시판 수정페이지 열기
function updatenotice() {
	
	$.ajax({
		url: 'chkUpNotice.do',
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
	    			
	    			$.ajax({
	    				url: 'getNoticeBoardUpdate.do',
	    				type: 'POST',    // 이동할때 포스트 방식으로 이동
	    			    data:{				// 이동할때 챙겨야하는 데이터
	    			    	bbs_seq: bbs_seq
	    			    },
	    			    success: function onData (data) {
	    			    	
	    			    	$('#savenoticeb').css('display','none');
	    	    			$('#updatenoticeb').css('display','inline-block');
	    	    			
	    	    			$("#summernote").summernote({
	    	    				//에디터 높이
	    	    				height: 300,
	    	    				focus: true,
	    	    				//에디터 한글 설정
	    	    				lang: "ko_KR",
	    	    				placeholder: '게시글을 작성해주세요.',
	    	    				bar: toolbar,
	    	    				// 추가한 글꼴
	    		    			fontNames: fontList,
	    		    			fontNamesIgnoreCheck: fontList
	    	    			});
	    	    			
	    	    			$('#titleW').val(data[0].title);
	    	    			$('#summernote').summernote('code', data[0].contents);
	    	    			
	    	    			updateNoticeFileList();
	    			    }
	    				
	    			});
	    			
	    			$(".pop_detailP").dialog("close");
	    			$(".pop_registP").dialog("open");
	    			$('#detailViewFileWrapN').empty();
	    			
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
function updateNoticeFileList() {
	$.ajax({
		url: 'getDetailFileList.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	bbs_seq: bbs_seq
	    },
	    success : function onData (data) {
	    	var html = "";
	    	
	    	if(data[0].length == 0 || data[0][0] == null) {
	    		$("#dropZoneInfoN").css("display","block");
	    	}else {
	    		for(var i=0; i<data[0].length; i++) {
	    			var fIndex = data[0][i].file_seq;
		    		var fileName = data[0][i].rfile_nm;
		    		var fileSize = data[0][i].file_size / 1024 / 1024;
		    		
		    		var fileSizeTxt = fileSize;
		    		
		    		if(fileSize < 1) {
		    			fileSizeTxt = (fileSize * 1024).toFixed(1) + "KB";
		    		}else {
		    			fileSizeTxt = (fileSize).toFixed(1) + "MB";
		    		}
		    		
		    		totalFileSizeN += fileSize;
		    		
		    		$("#dropZoneInfoN").css("display","none");
		    		
		    		html += "<ul id=\"fileTrN_View_"+fIndex+"\" class=\"file-ul view\">";
		    		html += "<li title='"+fileName+"'>"+fileName+"</li>";
		    		html += "<li>"+fileSizeTxt+"</li>";
		    		html += "<li class=\"file-delete-btn\" onclick=\"deleteViewFile("+fIndex+"); return false;\"><i class=\"fas fa-times\"></i></li>";
		    		html += "</ul>";
	    		}
	    		
	    		var totalFileSizeTxt;
		    	
		    	if(totalFileSizeN < 1){
		    		totalFileSizeTxt = (totalFileSizeN * 1024).toFixed(1) + "KB";
		        }else{
		        	totalFileSizeTxt = (totalFileSizeN).toFixed(1) + "MB";
		        }
		    	
		    	$("#totalFileSizeN").text(totalFileSizeTxt);
		    	$('#dropZoneN').append(html);
	    		
	    	}	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}


// 게시판 수정 버튼
function updateboard() {
	$(".pop_updateNoticeConfirm").dialog("open");
}

function updateNoticeYes() {
	regUpdate = 0;
	$(".pop_updateNoticeConfirm").dialog("close");
	
	var set_Title = $("#titleW").val();
	var set_Contents = $("#summernote").summernote("code");

	var getBbs_seq = $('#bbs_seqnotice').val(bbs_seq);
	
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "NOTICE BOARD",
	    	method: "수정"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
		url: 'updateNotice.do',  // 업데이트 실행시켜주는 컨트롤러로 이동
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    async: false,
	    data:{				// 이동할때 챙겨야하는 데이터
	    	title: set_Title,
	    	contents: set_Contents,
	    	bbs_seq: bbs_seq
	    },
	    success: function onData (data) {
	    	
	    	if(data < 0){
	    		alert("수정 권한이 없습니다.");
	    	}else if(data = 0) {
	    		alert("수정 오류");
	    	}else{
	    		// 테이블 갱신
		    	getNoticeBoard();
		    	//alert("수정이 완료되었습니다.");
		    	//값 삭제
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	        alert("수정 오류");
	    }
	});
	checkFile();
}

function updateNoticeCancle() {
	$(".pop_updateNoticeConfirm").dialog("close");
}

// 게시판 삭제
function deleteboard() {	
	$(".pop_deleteNoticeConfirm").dialog("open");
}

function deleteNoticeYes() {
	$(".pop_deleteNoticeConfirm").dialog("close");
	
	if(reg_id == $('#loginedUserId').val() || $('#checkAdmin').val() == 'Y') {
		
			$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
    			url: 'insertSystemLog.do',
    		    type: 'POST',
    		    data: {
    		    	prj_id: $('#selectPrjId').val(),
    		    	pgm_nm: "NOTICE BOARD",
    		    	method: "삭제"
    		    },
    		    success: function onData () {
    		    	
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
			
			$.ajax({
			    url: 'delNotice.do',
			    type: 'POST',
			    data:{
			    	bbs_seq: bbs_seq,
			    	reg_id: reg_id,
			    	//id 넘겨 주는 곳
			    },
			    success: function onData (data) {
			    	
			    	if(data < 0){
			    		alert("삭제 권한이 없습니다.");
			    	}else if(data = 0) {
			    		alert("삭제 오류");
			    	}else{
			    		// 테이블 갱신
				    	getNoticeBoard();
				    	alert("삭제가 완료되었습니다.");
				    	//값 삭제
			    	}
			    	
			    },
			    error: function onError (error) {
			        console.error(error);
			        alert("삭제 오류");
			    }
			});
			$('#detailViewFileWrapN').empty();
			$(".pop_detailP").dialog("close");
//			$('.ui-widget-overlay').css('display','none');
		}
}

function deleteNoticeCancle() {
	$(".pop_deleteNoticeConfirm").dialog("close");
}

// 게시판 상세 페이지 닫기버튼
function cancleNoticedetail() {
	$(".pop_detailP").dialog("close");
	$('#detailViewFileWrapN').empty();
//	$('.ui-widget-overlay').css('display','none');
	
	getNoticeBoard();
//	$.ajax({
//		url: 'cancleHit.do',
//	    type: 'POST',    // 이동할때 포스트 방식으로 이동
//	    data:{				// 이동할때 챙겨야하는 데이터
//	    	bbs_seq: bbs_seq
//	    },
//	    success: function onData (data) {
//	    	getNoticeBoard();
//	    },
//	    error: function onError (error) {
//	        console.error(error);
//	    }
//	});
	
//	return false;
}

// 게시판 등록 페이지 열기
function register() {
	$(".pop_registP").dialog("open");
	$('#savenoticeb').css('display','inline-block');
	$('#updatenoticeb').css('display','none');
//	$('.ui-widget-overlay').css('display','block');
	
	$("#summernote").summernote({
		//에디터 높이
		height: 300,
		focus: true,
		//에디터 한글 설정
		lang: "ko_KR",
		placeholder: '게시글을 작성해주세요.',
		toolbar: toolbar,
		// 추가한 글꼴
		fontNames: fontList,
		fontNamesIgnoreCheck: fontList
	});
	
	return false;
}

// 게시판 등록 버튼(저장)
function saveboard() {
	regUpdate = 1;
	
	var set_Title = $("#titleW").val();
	var set_Contents = $("#summernote").summernote("code");
//	var set_Attach = $("#atfileW").val();
	if(set_Title==''){
		alert('제목을 작성해주세요.');
	}else{
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url: 'insertSystemLog.do',
		    type: 'POST',
		    data: {
		    	prj_id: $('#selectPrjId').val(),
		    	pgm_nm: "NOTICE BOARD",
		    	method: "저장"
		    },
		    success: function onData () {
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		$.ajax({
			url: 'insertNotice.do',
			type: 'POST',
			async: false,
			data:{
				title: set_Title,
				contents: set_Contents
			},
			success: function onData (data) {
				if(data == null || data == "") {
		    		alert("등록 오류");
		    	}else {
		    		$('#bbs_seqnotice').val(data.bbs_seq);
		    		// 테이블 갱신
					getNoticeBoard();
					//alert('등록이 완료되었습니다.');
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		        alert("등록 오류");
		    }
		});
		checkFile();
		
	}
	
}

// 게시판 등록 취소 버튼
//function cancleboard() {
//	var set_Title = $("#titleW").val();
//	var set_Contents = $("#summernote").val();
////	var set_Attach = $("#atfileW").val();
//	
//	var chek_Cancle = confirm('게시물 작성을 취소하시겠습니까?');
//		
////	alert('close');
//	
//	if(chek_Cancle == true){
//		$("#titleW").val(null);
//		$("#summernote").summernote("code", "");
//		$(".pop_registP").dialog("close");
////		$('.ui-widget-overlay').css('display','none');
//	}
//}

//게시판 수정 취소 버튼
function cancleboard() {
//	var chek_Cancle = confirm('게시물 등록/수정을 취소하시겠습니까?');
	
//	if(chek_Cancle == true){
		$(".pop_registP").dialog("close");
//		$('.ui-widget-overlay').css('display','none');
		
//		console.log(bbs_seq);
		
		getNoticeBoard();
		
		$("#titleW").val(null);
		$("#summernote").summernote("code", "");
		deleteAllFile();
		$(".pop_registP").dialog("close");
//	}
}

function directSelectFileN() {
	var file = document.getElementById("directFileN").files;
	selectFileN(file);
//	console.log(file);
}

$(function (){
    // 파일 드롭 다운
    fileDropDownN();
});

//파일 드롭 다운
function fileDropDownN() {
	var dropZone = $("#dropZoneN");	
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
		
		if(files != null){
			if(files.length < 1){
				alert("폴더는 업로드가 불가능합니다.");
				return;
			}
			selectFileN(files);
		}else {
			alert("파일 추가에 실패했습니다. 새로고침 후 다시 시도해주세요.");
		}
	});
}

// 파일 선택시
function selectFileN(fileObject){
	
	var files = null;
	
	if(fileObject != null) {
		//파일 Drag 이용하여 등록시
		files = fileObject;
	}else{
		// 직접 파일 등록시
		files = $('#multipaartFileList_' + fileIndexN)[0].files;
	}
	
	// 다중파일 등록
    if(files != null){
    	console.log("파일 길이"+files.length);
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
            
            if($.inArray(ext, [
            	"xlsx", "xlsm", "xlsb", "xltx", "xltm", "xls", "xml", "xlam", "xla", "xlw", "xlr",
         		"txt", "png", "bmp", "gif", "jpg", "jpeg", "tif", "pdf", "ppt", "pptx", "pptm", "doc", "docm", "docx",
         		"hwpx", "hwp","zip"
            ]) >= 0){ // 선택한 확장자가 리스트에 포함됐을때만 가져오기
            	// 전체 파일 사이즈
            	totalFileSizeN += fileSize;
                
                // 파일 배열에 넣기
            	fileListN[fileIndexN] = files[i];
                
                // 파일 사이즈 배열에 넣기
                fileSizeListN[fileIndexN] = fileSize;

                // 업로드 파일 목록 생성
                addFileListN(fileIndexN, fileName, fileSize, fileSizeTxt);

                // 파일 번호 증가
                fileIndexN++;
            }else{
                // 확장자 체크
                alert("첨부가 불가능한 파일입니다.");
                break;
            }
    	}
    	
    	var totalFileSizeTxt;
    	
    	if(totalFileSizeN < 1){
    		totalFileSizeTxt = (totalFileSizeN * 1024).toFixed(1) + "KB";
        }else{
        	totalFileSizeTxt = (totalFileSizeN).toFixed(1) + "MB";
        }

    	$("#totalFileSizeN").text(totalFileSizeTxt);
    }else{
        alert("ERROR");	
    }
}

function addFileListN(fIndex, fileName, fileSize, fileSizeTxt) {
	$("#dropZoneInfoN").css("display","none");
	
	var html = "";
	html += "<ul id=\"fileTrN_"+fIndex+"\" class=\"file-ul\">";
	html += "<li title='"+fileName+"'>"+fileName+"</li>";
	html += "<li>"+fileSizeTxt+"</li>";
	html += "<li class=\"file-delete-btn\" onclick=\"deleteFile("+fIndex+"); return false;\"><i class=\"fas fa-times\"></i></li>";
	html += "</ul>";
	
	$('#dropZoneN').append(html);
	
}

// 업로드 파일 삭제
function deleteFile(fIndex) {
	// 전체 파일 사이즈 수정
	totalFileSizeN -= fileSizeListN[fIndex];
	
    // 파일 배열에서 삭제
    delete fileListN[fIndex];
    
    // 파일 사이즈 배열 삭제
    delete fileSizeListN[fIndex];
    
    // 업로드 파일 테이블 목록에서 삭제
    $("#fileTrN_" + fIndex).remove();
    
    if(totalFileSizeN == 0) {
		$("#dropZoneInfoN").css("display","block");
	}
    
    var totalFileSizeTxt;
    
    if(totalFileSizeN < 1){
    	totalFileSizeTxt = (totalFileSizeN * 1024).toFixed(1) + "KB";
    }else{
    	totalFileSizeTxt = (totalFileSizeN).toFixed(1) + "MB";
    }
	
    $("#totalFileSizeN").text(totalFileSizeTxt);
}

var noticefIndex;
function deleteViewFile(fIndex) {
	$(".pop_deleteNoticeFileConfirm").dialog("open");
	noticefIndex = fIndex;
}

function deleteNoticeFileYes() {
	$(".pop_deleteNoticeFileConfirm").dialog("close");
	
	$.ajax({
		url: 'deleteFileN.do',
	    type: 'POST',    // 이동할때 포스트 방식으로 이동
	    data:{				// 이동할때 챙겨야하는 데이터
	    	file_seq: noticefIndex,
	    },
	    success: function onData (data) {
	    	
	    	// 전체 파일 사이즈 수정
	    	totalFileSizeN -= fileSizeListN[noticefIndex];
	    	
	        // 파일 배열에서 삭제
	        delete fileListN[noticefIndex];
	        
	        // 파일 사이즈 배열 삭제
	        delete fileSizeListN[noticefIndex];
	        
	        // 업로드 파일 테이블 목록에서 삭제
	        $("#fileTrN_View_" + noticefIndex).remove();
	        
	        if(totalFileSizeN == 0) {
	        	$("#dropZoneInfoN").css("display","block");
	        }
	        
	        var totalFileSizeTxt;
	        
	        if(totalFileSizeN < 1){
	        	totalFileSizeTxt = (totalFileSizeN * 1024).toFixed(1) + "KB";
	        }else{
	        	totalFileSizeTxt = (totalFileSizeN).toFixed(1) + "MB";
	        }
	    	
	        $("#totalFileSizeN").text(totalFileSizeTxt);
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function deleteNoticeFileCancle() {
	$(".pop_deleteNoticeFileConfirm").dialog("close");
}

//function deleteFile() {
//	
//	var chkYn = confirm("등록되어 있는 파일을 삭제하실 경우 복구가 불가능합니다.\n정말 삭제하시겠습니까?");
//	
//	if(chkYn == true){
//		
//	}
//}

// 리셋 버튼 클릭시
function deleteAllFile() {
	totalFileSizeN = 0;
	fileSizeListN = [];
	fileListN = [];
	
	$("#dropZoneN .file-ul").remove();
	$("#dropZoneInfoN").css("display","block");
	
	var totalFileSizeTxt;
	
	if(totalFileSizeN < 1){
		totalFileSizeTxt = (totalFileSizeN * 1024).toFixed(1) + "KB";
	}else {
		totalFileSizeTxt = (totalFileSizeN).toFixed(1) + "MB";
	}
	
	$("#totalFileSizeN").text(totalFileSizeTxt);
}

//파일 등록 체크
function checkFile() {
	// 등록할 파일 리스트
	var uploadFileList = Object.keys(fileListN);
	
	// 등록할 파일 리스트를 formData로 데이터 입력
	var form = $('#uploadForm_N')[0];
	var formData = new FormData(form);
	
	for(var i=0; i<uploadFileList.length; i++) {
		formData.append('files', fileListN[uploadFileList[i]]);
	}
	
	//console.log("formData Append = "+formData);
	
	

	
	$.ajax({
		url: 'checkFile.do',
		type: 'POST',
		data: formData,
		enctype:'multipart/form-data',
        processData:false,
        contentType:false,
        dataType:'json',
        cache:false,
        beforeSend:function(){
			$('.pop_registP').prepend(progressbar);
        },
		complete:function(){
			$('.pop_registP .progressDiv').remove();
		},
		xhr: function () {
            //XMLHttpRequest 재정의 가능
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
              //progress 이벤트 리스너 추가
              var percent = (e.loaded * 100) / e.total;
              //$('.pop_registProject .progressDiv').css({'position':'absolute','width':'100%','height':'100%','z-index':'1','background-color': 'rgba( 255, 255, 255, 0.5 )'});
              //$('.pop_registProject .progressDiv .bar').css({'width': percent+'%','height':'30px','position':'absolute','top':'50%','transform':'translate(0%,-50%)'});
              $('.pop_registP .progressDiv .bar').css('width', percent+'%');
            };
            return xhr;
        },
		success: function onData (data) {
			if(data > 0) {
				deleteAllFile();

				$(".pop_registP").dialog("close");
				$("#titleW").val(null);
				$("#summernote").summernote("code", "");
				
				if(regUpdate == 0) {
					alert('수정이 완료되었습니다.');
				}else if(regUpdate == 1) {
					alert('등록이 완료되었습니다.');
				}
			}else {
				alert("Unsupported file format. Please contact the administrator");
				//getNoticeBoard();
			}
		},
		error: function onError (error) {
	    	console.error(error);
//	        alert("등록 오류");
	    }
	});
	
}

//파일 등록
//function uploadFile() {
//	// 등록할 파일 리스트
//	var uploadFileList = Object.keys(fileListN);
//	
//	// 등록할 파일 리스트를 formData로 데이터 입력
//	var form = $('#uploadForm_N')[0];
//	var formData = new FormData(form);
//	
//	for(var i=0; i<uploadFileList.length; i++) {
//		formData.append('files', fileListN[uploadFileList[i]]);
//	}	
//	
//	
//
//	
//	$.ajax({
//		url: 'fileUploadNoticeN.do',
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
//				//getNoticeBoard();
//				
////				return data;
//			}
//		},
//		error: function onError (error) {
//	    	console.error(error);
////	        alert("등록 오류");
//	    }
//	});
//	
//}
/*
//파일 업데이트
function updateFile() {
	// 등록할 파일 리스트
	var uploadFileList = Object.keys(fileList);
	
	// 등록할 파일 리스트를 formData로 데이터 입력
	var form = $('#uploadForm_N')[0];
	var formData = new FormData(form);
	
	for(var i=0; i<uploadFileList.length; i++) {
		formData.append('files', fileList[uploadFileList[i]]);
	}
	
	$.ajax({
		url: 'fileUploadNoticeN.do',
		type: 'POST',
		data: formData,
		enctype:'multipart/form-data',
        processData:false,
        contentType:false,
        dataType:'json',
        cache:false,
		success: function onData (data) {
			
			if(data != null) {
				deleteAllFile();
	            
				
			}
		},
		error: function onError (error) {
	    	console.error(error);
//	        alert("등록 오류");
	    }
	});
	
}*/

/**
* 썸머노트 이미지 파일 업로드
*/
function uploadSummernoteImageFile(file, editor) {
	data = new FormData();
	data.append("file", file);
	$.ajax({
		data : data,
		type : "POST",
		url : "uploadSummernoteImageFile.do",
		contentType : false,
		processData : false,
		success : function(data) {
        	//항상 업로드된 파일의 url이 있어야 한다.
			$(editor).summernote('code', $(editor).summernote('code')+'<img src="'+current_server_domain+'/getSummernoteImage.do?sfile_nm='+data[0]+'">');
			
		}
	});
}


