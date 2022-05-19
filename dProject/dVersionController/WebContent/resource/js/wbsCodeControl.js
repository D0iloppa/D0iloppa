/* 
 * 작성자		: 권도일
 * 작성일		: 2021-12-22
 * 파일명		: wbsCodeControl.js
 * 설명		: WBS Code Control 페이지 제어를 위한 스크립트 문 
 */

var selectPrjId = $('#selectPrjId').val();
var table;
var selected_wbs_code;
var selected_code_seq;

// WBS 제어를 위한 전역변수 선언
var WBS = {};

$( document ).ready(function(){
	
	let checkedRadio = $("input:radio[name=search_radio]:checked").val();
	
	// 라디오 버튼 이벤트
	$("input:radio[name=search_radio]").click(function(){ WBS_setType(); });
	
	// Discipline 버튼 클릭 이벤트 부여
	$('#WBS_selected_display').click(function() {
		$('#WBS_selected_div').addClass('on');
	});
	$('.WBS_disc_select').click(function(){
		// 선택한 discip_code를 가져옴
		WBS.selectedDiscip = $(this).attr('id').replace('WBS_discip_','');
		// 선택된 Discip를 보여줌
		let text = $(this).children()[0].outerText; // 선택된 옵션값 (화면에 뿌려줄 텍스트 요소)
		$("#WBS_selected_display").text(text);
		// 선택 창 닫기
		$('#WBS_selected_div').removeClass('on');
		getWbsCodeControlList($("input:radio[name=search_radio]:checked").val());
	});
	WBS.prjInfo = getPrjInfo();
	WBS.selectedDiscip = 'all';
	WBS.type = ["D","M","B","V","S"];
	WBS.isAddMode = false;
	
	initGrid(checkedRadio);
	
	// discipline editParams를 위한 code값과 표시값을 위한 해쉬맵 생성
	// code : display 로 매핑 
	WBS.discipCodeMap = {};
	WBS.discipCodeMapReverse = {};
	WBS.discipEditParams = [];
	for(let i=0 ; i<$(".WBS_disc_select").length ; i++){
		
		let discip_id = $(".WBS_disc_select")[i].id.replace('WBS_discip_','');
		let discip_display = $(".WBS_disc_select")[i].children[0].outerText;
		if(discip_id == 'all') continue;
		
		// id로 display 찾기 위한 맵
		WBS.discipCodeMap[discip_id] = discip_display;
		// display로 id를 찾기 위한 맵
		WBS.discipCodeMapReverse[discip_display] = discip_id;
		WBS.discipEditParams.push(discip_display);
		
	}
	
	
	
	
});


/**
 * 작성자		: 권도일
 * 작성일		: 2021-12-22
 * 메소드명	: initGrid
 * 설명		: 페이지 로딩시 처음 DB에서 데이터를 가져온다.
 * 
 * @param checkedRadio 
 * @returns gird
 */
function initGrid(checkedRadio){
	WBS.grid = new Tabulator("#wbsCodeList",{
		index: "index",
		selectable:1,//true
        layout: "fitColumns",
        placeholder: "No Data Set",
        height:"100%",
		columns: [
	        {
	            title: "index",
	            field: "index",
	            visible:false
	        },
	        {
	            title: "wbs_code_id",
	            field: "wbs_code_id",
	            visible:false
	        },
			{
				title: "CK",
				formatter: function(cell, formatterParams, onRendered) {
					var checkbox =  document.createElement("input");
					checkbox.classList.add('WBScodeList');
					checkbox.id = 'WBScodeList'+cell.getRow().getPosition();
					checkbox.name = 'WBScodeList';
					checkbox.type = 'checkbox';
					return checkbox;
				},
				hozAlign:"center", 
    			headerSort:false,
    			width:10,
    			download:false,
    			cellClick:function(e, cell){
    				cell.getRow().toggleSelect();
    		    }
			},
			{
				title: "WBS Code",
				field: "wbs_code",
				editor: "input",
		        hozAlign:"left",
		        headerSort:false				
			},
			{
				title: "Description",
				field: "wbs_desc",
				editor: "input",
		        hozAlign:"left",
		        widthGrow:2,
		        headerSort:false
			},
			{
				title: "WgtVal",
				field: "wgtval",
				editor: "number",
		        hozAlign:"left",
		        width:50,
		        headerSort:false
			},
			{
				title: "Type",
				field: "doc_type",
				editor: "select",
				width:50,
				editorParams: {
					listItemFormatter:function(value, title) {
						return title;
					},
					values:WBS.type,
                    multiselect:false, //allow multiple entries to be selected					
				},
		        hozAlign:"left",
		        headerSort:false
			},
			{
				title: "Disicipline",
				field: "discipline",
				editor: "none",
		        hozAlign:"left",
		        visible:false,
		        headerSort:false
			},
			{
				title: "Disicipline",
				field: "discip_display",
				editor: "select",
				width:200,
		        hozAlign:"left",
		        visible:true,
		        headerSort:false
			}
		]
	});
	
	WBS.grid.on("rowSelected", function(row){
		selected_wbs_code = row.getData().wbs_code;
		selected_code_seq = row.getData().code_seq;
	});
	
	WBS.grid.on("tableBuilt",function(){
		WBS.grid.getColumn('discip_display').updateDefinition({editorParams:{values:WBS.discipEditParams}});
		getWbsCodeControlList(checkedRadio);
	});
	
	WBS.grid.on("cellEdited", function(cell){
		// select 타입에 대하여 수정할 때의 이벤트 
		if(cell.getColumn().getDefinition().editor =='select'){
			let columnName = cell.getColumn().getField();
			let discip_display = cell.getData().discip_display;
			let id;

			switch(columnName){
				case 'discip_display':
					id = WBS.discipCodeMapReverse[discip_display];
					console.log(id);
					cell.getRow().update({discipline : id});
					break;
			}
			
			
		}

	});
	
	
}

/**
 * 
 * @returns
 */
function WBS_setType(){
	let checkedRadio = $("input:radio[name=search_radio]:checked").val();
	getWbsCodeControlList(checkedRadio);
}



// db에서 저장된 WBS 코드 데이터 가져오기
function getWbsCodeControlList(checkedRadio) {
	
	WBS.isAddMode = false;
	$("input[name=wbsExcelfile]").val("");
	
	
	
	if(!checkedRadio)
		checkedRadio = $("input:radio[name=search_radio]:checked").val();
	
	
	$.ajax({
		url: 'getWbscodeList_wbs.do',
		type: 'POST',
		data: {
			prj_id: selectPrjId,
			doc_type:checkedRadio,
			discip_code_id:WBS.selectedDiscip
		},
		success: function onData (data) {
			var wbscodeList = [];
			let index = 0;
			let tmp = data[0];


			// 그리드 상에 들어갈 리스트 필터링
			for(let i=0;i<tmp.length;i++){
				if(checkedRadio != 'A')
					if(tmp[i].doc_type != checkedRadio) continue;
				
				if(WBS.selectedDiscip != 'all')
					if(tmp[i].discipline != WBS.selectedDiscip) continue;
				
				tmp[i].index = index++;
				wbscodeList.push(tmp[i]);
			}
			
			WBS.grid.setData(wbscodeList);
			WBS.grid.redraw();
			unDisableChk();
			radioActiveMode(true);
			
		},
		error: function onError (error) {
	        console.error(error);
	    }
	});
	
}


function searchwbsCode(){
	WBS.grid.clearData();
	getWbsCodeControlList($("input:radio[name=search_radio]:checked").val());
}


function savewbsCode() {
	
	// discipline이 지정되지 않은 경우
	// WBS.grid.getRow().getData().discip_display?true:false;
	
	let tmpGridData = WBS.grid.getData();
	let mustInput = [ "wbs_code", "wbs_desc"];
	// 입력 누락된 값 있는지 확인
	for(let i = 0 ; i < tmpGridData.length; i++){
		let chkRow = tmpGridData[i];
		
//		if(!chkRow.discipline){
//			alert("모든 필드를 입력해주세요");
//			return;
//		}
		
		for (let col = 0; col < mustInput.length; col++) {
			if (!chkRow[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return;
			}
		}
	}
	
	
	
	let changedList = WBS.grid.getEditedCells();
	
	let tmp = tmpGridData.filter(i=>i.isAdd==true);
	
	// 수정한 row
	let changedData = [];
	for(let i=0;i<changedList.length;i++){
		let tmpIdx = changedList[i].getData().index;
		changedData.push(tmpIdx);
	}
	// 추가한 row
	for(let i = 0 ; i < tmp.length ; i++){
		let tmpIdx = tmp[i].index;
		changedData.push(tmpIdx);
	}
	// 변화가 있는 row들 json으로 변환
	let tmpStrings = [];

	
	for(let i=0;i<changedData.length;i++){
		let rowIndex = changedData[i];
		tmpStrings.push( JSON.stringify(tmpGridData[rowIndex]) );
	}
	
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "WBS CODE CONTROL",
	    	method: "저장"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'updateWBScode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: getPrjInfo().id,
	    	jsonRowdatas:tmpStrings
	    },
	    success: function onData (data) {
	    	
	    	alert(data=="SUCCESS"?"SAVE complete":"SAVE failed");
	    	
	    	WBS.grid.clearData();
	    	getWbsCodeControlList($("input:radio[name=search_radio]:checked").val());
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}


/**
 * 삭제 버튼 누를 시, 체크박스 체크여부 확인 후, 체크 된 row 삭제 수행
 * @returns
 */
function deletewbsCode() {
	if($("input:checkbox[name='WBScodeList']:checked").length===0) {
		alert('삭제할 데이터를 선택 후 삭제하십시오.');
		return;
	}
	
	$("#pop_WBSDelete").dialog({modal:true,resizable:false});	
	
}

function WBSDeleteCancel() {
	$("#pop_WBSDelete").dialog("close");
}


/**
 * 삭제를 수행
 * @returns
 */

function WBSDelete() {
	let checkedIdxList = $("input:checkbox[name='WBScodeList']:checked");
	// let checkedDataList = [];
	let jsonRow = [];
	for(let i=0;i<checkedIdxList.length;i++){
		checkedIdxList[i] = Number(checkedIdxList[i].id.replace('WBScodeList',''));
		jsonRow.push(JSON.stringify( WBS.grid.getRow(checkedIdxList[i]).getData()));
	}
	
	// 엑셀로부터 불러온 데이터의 경우 아직 DB상에 올리지 않았으므로
	// 프론트 단 row에서 삭제처리를 한다.
	if(WBS.isAddMode){
		for(let i=0;i<checkedIdxList.length;i++)
			WBS.grid.deleteRow(checkedIdxList[i]);
		
		$("#pop_WBSDelete").dialog("close");
		return;
	}
	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url: 'insertSystemLog.do',
	    type: 'POST',
	    data: {
	    	prj_id: $('#selectPrjId').val(),
	    	pgm_nm: "WBS CODE CONTROL",
	    	method: "삭제"
	    },
	    success: function onData () {
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'delWBScode.do',
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: WBS.prjInfo.id,
	    	jsonRowdatas : jsonRow
	    },
	    success: function onData (data) {
	    	// 삭제 실패시 실패 이유 throw
	    	if(data != "SUCCESS") alert(data);
	    	alert("save completed");
	    	getWbsCodeControlList();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});

	
	$("#pop_WBSDelete").dialog("close");
}

function fromExcelBtn(){
	document.getElementsByName("wbsExcelfile")[0].click();
}
/**
 * 
 * 
 * @param obj
 * @returns
 */
function fileUp(obj){
	
	let form = $('#excelUploadForm')[0];
    let formData = new FormData(form);
    formData.set("prj_id",getPrjInfo().id);
    
    loadingWithMask();
	
	 $.ajax({             
	    	type: "POST",          
	        enctype: 'multipart/form-data',  
	        url: "wbsExcelUpload.do",        
	        data: formData,          
	        processData: false,    
	        contentType: false,      
	        cache: false,           
	        //timeout: 600000,       
	        success: function (result) {
	        	closeLoadingWithMask();
	        	if(result.model.status != 'SUCCESS'){
	        		alert(result.model.status)
	        		return;
	        	}
	        		
	        	alert("Excel Loaded Successfully");
	        	console.log(result.model.dataFromExcel);
	        	wbsGridAddMode(result.model.dataFromExcel);
	        },          
	        error: function (e) {  
	        	console.log("ERROR : ", e);     
	            alert("fail");
	            closeLoadingWithMask();
	         }     
		});  
	
}

/**
 * 엑셀로부터 데이터를 받아와서
 * grid에 추가시켜준다.
 * 
 * @param data
 * @returns
 */
function wbsGridAddMode(data){
	console.log(data);
	// 기존 DB에 존재하던 row의 갯수
	// 새로 추가 된 row의 인덱스를 정하기 위함 
	let orgIdx = WBS.grid.getData().length;
	WBS.isAddMode = true;
	
	// type 뷰를 설정하기 위한 상단 라디오 버튼 비활성화
	radioActiveMode(false);
	
	// 기존 체크박스들 삭제 불가능하게 비활성화 
	let orgChkList = $("input[name=WBScodeList]");
	disableChk(orgChkList);
	
	
	let field = ["wbs_code","wbs_desc","wgtval","doc_type","discip_display"];
	
	for(let rowIdx = 0 ; rowIdx < data.length ; rowIdx++){
		let row = {};
		let tmp = data[rowIdx];
		row.index = rowIdx + orgIdx;
		row.prj_id = getPrjInfo().id
		for(let colIdx = 0 ; colIdx < field.length ; colIdx++)
			row[field[colIdx]] = tmp[colIdx];
		
		row.discipline = WBS.discipCodeMapReverse[ tmp[field.length-1] ];
		row.isAdd = true;

		
		WBS.grid.addRow(row);
	}
		
	
	
}

/**
 * to Excel 버튼을 누를 시,
 * 그리드의 데이터를 엑셀 템플릿으로 변환하여 저장한다.
 * 
 * @returns
 */
/*
function toExcelBtn(){
	let gridData = WBS.grid.getData();
	let jsonList = [];
	
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length ; i++)
		jsonList.push(JSON.stringify(gridData[i]));
	
	let fileName = 'test.xlsx';
	console.log(jsonList);
	$.ajax({
	    url: 'wbsExcelDownload.do?fileName=' + fileName,
	    type: 'POST',
	    traditional : true,
	    data:{
	    	prj_id: getPrjInfo().id,
	    	jsonRowdatas:jsonList
	    },
	    success: function onData (status) {
	    	console.log(status);
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});	
}

*/
function toExcelBtn(){
	let gridData = WBS.grid.getData();
	
	let jsonList = [];
		
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length ; i++)
		jsonList.push(JSON.stringify(gridData[i]));

	
	let dateString = genDateStr();
	let fileName = getPrjInfo().no;	
		fileName += "_"+dateString + 'WBS_Code';

    var f = document.excelUploadForm; 

    
    f.prj_id_wbs.value = selectPrjId;
    f.prj_nm_wbs.value = getPrjInfo().full_nm;
    f.fileName.value = fileName;
    f.jsonRowdatas.value = jsonList;
    f.doc_type_wbs.value = $("input[name='search_radio']:checked").val();
    f.discip_code_id_wbs.value = WBS.selectedDiscip;
    f.action = "wbsExcelDownload.do";
    f.submit();   	
}


/**
 * 제어할 수 없도록 체크박스를 잠근다.
 * @param chkList
 * @returns
 */
function disableChk(chkList){
	for(let i=0;i<chkList.length;i++)
		chkList[i].disabled = true;
}
/**
 * 비활성화된 체크박스를 다시 활성화 시킨다.
 * @param chkList
 * @returns
 */

function unDisableChk(){
	let chkList = $("input[name=WBScodeList]")
	for(let i=0;i<chkList.length;i++)
		chkList[i].disabled = false;
}

function radioActiveMode(mode){
	if(mode == null) mode = false;
	
	if(typeof(mode) != 'boolean') mode = true;

	let radio = $("input[name=search_radio]");
	for(let i=0 ; i < radio.length ; i++)
		radio[i].disabled = !mode;
}