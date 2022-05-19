var clickTb_view = 0;
var table_view;
var abbr_id_view;

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
	getDictionaryViewVO();
//	$('#dictionaryList2').css('display','none');
	
});

function removeTabDictionary() {
	
}

function getDictionaryViewVO() {  // 데이터베이스에서 데이터 가져오기
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
	    			desc: data[0][i].dic_desc
	    		});
	    	}
	    	// 가져온 데이터 리스트를 set에 넘겨줌
	    	setDictionaryViewVO(dictionaryList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
// 데이터 검색 부분
function searchdictionary_view() {
	var search_abbr = $('#search_Abbr_view').val();
	var search_eng = $('#search_Eng_view').val();
	var search_kor = $('#search_Kor_view').val();
	var search_desc = $('#search_Desc_view').val();	
	
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
		    			desc: data[0][i].dic_desc
		    		});
		    	}
		    	// 가져온 데이터 리스트를 set에 넘겨줌
		    	setDictionaryViewVO(dictionaryList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
}
document.addEventListener('keydown', enterkey);
function enterkey(e) {
	let focusEle = document.activeElement;
	if(document.getElementById('search_Abbr_view') == focusEle || document.getElementById('search_Eng_view') == focusEle || document.getElementById('search_Kor_view') == focusEle || document.getElementById('search_Desc_view') == focusEle){
	    if (e.keyCode == 13) {
	    	searchdictionary_view();
	    }
	}
}
function setDictionaryViewVO(dictionaryList) { // 넘어온 데이터 화면에 뿌려주기
	table_view = new Tabulator("#dictionaryList_view", {
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
              field: "desc",
              hozAlign: "left"
          }
      ]
  });
	
	table_view.on("rowSelected", function(row){
		$('#savebutton').css('display','none');
		$('#updatebutton').css('display','block');
		
		clickTb_view = row.getData();
		abbr_id_view = clickTb_view.abbr_id;
		
		$('#abbr_view').val(clickTb_view.abbr);
		$('#eng_view').val(clickTb_view.eng_full_nm);
		$('#kor_view').val(clickTb_view.kor_full_nm);
		$('#desc_view').val(clickTb_view.desc);
		
	});
}