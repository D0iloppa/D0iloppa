var drnUp_Var = {};
var folderList;
var params;
var context_path;
var processTypeByFolderPath;
var current_FolderDiscipline;
var current_folder_id;
var current_doc_type;


var loading = '<div id="loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';


$(function(){ // document ready
	//drnUp();
	

	
	$(".pop_mngAppr").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000,
	    modal: true
	});
	
	$(".pop_collaboW").dialog({
	    autoOpen: false,
	    maxWidth: 1200,
	    width: 1200,
	    modal: true
	});
	
	$("#drn_fileDialog").dialog({
	    autoOpen: false,
	    maxWidth: 1300,
	    width: 1300,
	    modal: true
	});
	
	$(".pop_multiDownload").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000,
	    modal: true
	});
	
	$("#drnChkSheet_Pop").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000,
	    modal: true
	});
	
	$("#pop_alertDialog").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000,
	    modal: true
	});
	
	
	$("#drn_comment").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000,
	    resizable : false,
	    modal: true
	});
	
	
	
	

	
	params = getParams();
	context_path = params.context_path;
	current_folder_id = params.folder_id;
	
	
	$.ajax({
	    url: 'getProcessTypeByFolderPath.do',
	    type: 'POST',
	    data: {
	    	prj_id:params.prj_info.id
	    },
		async: false,
	    success: function onData (data) {
	    	processTypeByFolderPath = data[0];
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	$.ajax({
	    url: 'getFolderDiscipline.do',
	    type: 'POST',
	    data: {
	    	prj_id:params.prj_info.id
	    },
		async: false,
	    success: function onData (data) {
	    	current_FolderDiscipline = data[0];
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	
	
	drnUp(params.pageMode,params.docData,params.discipObj,params.folder_id,pageData,params.approval)
	
	
});





/**
 * 
 * @param pageMode : 페이지의 모드
 * @param docData  : 문서리스트
 * @param discipObj : discip정보
 * @param pageData : drn page 정보를 가져오는 경우
 * @returns
 */
function drnUp(pageMode,docData,discipObj,folder_id,pageData,approval){
	
	$(".drn_readMode").css('display','');
	$("#Status_write")[0].disabled = false;
	
	let docDataObj = [];
	
	$("#drnLocalFile").val("");
	for(let i=0;i<docData.length;i++){
		let tmpDoc = docData[i].bean ? docData[i].bean : docData[i];
		if(docData[i].Revision) tmpDoc.revision = docData[i].Revision;
		if(docData[i].revNo) tmpDoc.revision = docData[i].revNo;
		docDataObj.push( tmpDoc );
	}

	$(".drn_readMode").css('display','');
	$(".drn_read").attr("readonly",false); 
	
	
	// Status STEP 필드 추가
	$.ajax({
	    url: 'getStatus.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:params.prj_info.id,
	    	folder_id:folder_id
	    },
	    success: function onData (data) {
	    	if(data[0] != 'FAIL'){
	    		setStatus(data[1]);  		
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	// 관련 변수 초기화
	initDrnVar(pageMode,docDataObj,discipObj,folder_id,pageData);
	initDrnGrid();
	
	
	let pageTitle = setDrnPage(pageMode);
	// console.log("as:",drnUp_Var.as);
	
	$("#drn_as").text("");
	//if(pageMode == "read"){
	if(pageMode == -1){ // "read mode"
		drnUp_Var.readOnly = true;
		readOnlyMode();
		setReadMode();
		/*
		if(drnUp_Var.as != "T"){
			drnUp_Var.readOnly = true;
			readOnlyMode();
			setReadMode();
		}else{
			pageTitle = "작성중 DRN 상세페이지";
			// $("#drn_tempSaveBtn")[0].style["display"] = "none";
			drnUp_Var.readOnly = true;
			readOnlyMode();
			setReadMode();
		}
		*/
	}
	
	
	if(drnUp_Var.as == "R"){
		pageTitle = "반려됨";
		let reason_text;
		
		$.ajax({
		    url: 'getRejectPoint.do',
		    type: 'POST',
		    async:false,
		    data:{
		    	prj_id:params.prj_info.id,
		    	drn_id:drnUp_Var.drn_id
		    },
		    success: function onData (data) {
		    	console.log("R",data);
		    	let result = data.model.drnHist;
		    	let level = result[result.length - 2].drn_approval_status;
		    	console.log(level);
		    	switch(level){
		    		case "A" :
		    			reason_text = "DCC 반려"
		    			break;
		    			
		    		case "E" :
		    			reason_text = "검토자 반려"
		    			break;
		    			
		    		case "1" :
		    			reason_text = "결재자 반려"
		    			break;
		    		default:
		    			reason_text = ""
		    			break;
		    	}
		    	reason_text += (reason_text=="" ? "" : ":") + result[result.length - 1].reg_id;
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		$("#drn_as").text("반려됨 (" + reason_text + ")");
		
	}else if(drnUp_Var.as == "C"){
		pageTitle = "완료";
	}
	
	
	/*
	$(".pop_collaboW").dialog("option","title",pageTitle);
	$(".pop_collaboW").dialog("open");
	*/
}

/**
 * DRN 새로 만드는 경우
 * @returns
 */
function newDrn(){
	
	// 관련 변수 초기화
	initDrnVar(0);
	initDrnGrid();
	
	
	let pageTitle = setDrnPage(0);
	
	$(".pop_collaboW").dialog("option","title",pageTitle);
	
	$(".pop_collaboW").dialog("open");
}



/**
 * Status의 옵션 값 세팅
 * @param data
 * @returns
 */
function setStatus(data){
	// status 옵션값 클리어

	$("#Status_write").children('option').remove();
	
	let opt = document.createElement("option");
	opt.value = "";
	opt.text = "";
	$("#Status_write").append(opt);
	
	for(let i=0;i<data.length; i++){
		let tmp = data[i];
		let opt = document.createElement("option");
		opt.value = tmp.set_code_id;
		opt.text = tmp.set_code;
		$("#Status_write").append(opt);
	}
}


/**
 * DRN 관련 변수들 초기화
 * @returns
 */
function initDrnVar(pageMode,docData,discipObj,folder_id,pageData){
	// 관련 변수 초기화
	drnUp_Var = {};
	
	drnUp_Var.as = ""; //approval status값 가져옴
	if(pageData) // pageData가 있는 경우, DB에서 상태값 조회
		drnUp_Var.as = pageData.drnInfo.drn_approval_status;
		/*$.ajax({
	    url: 'get_as.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:params.prj_info.id,
	    	drn_id:pageData.drnInfo.drn_id
	    },
	    success: function onData (data) {
	    	drnUp_Var.as = data.model.dccUser;	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});*/
	
	// approval status 에 따른 맵
	// 상태값에 따라 선형적 수치로 표현하기 위함
	drnUp_Var["as_map"] = {
			""  : -1, // 상태값 없음
			"T" : 0, // 임시저장
			"A" : 1, // 결재상신
			"E" : 2, // dcc 승인
			"1" : 3, // review 승인
			"C" : 4, // 최종 승인
			"R" : 5  // 반려
	};

	drnUp_Var.flag = 0;
	
	if(docData) drnUp_Var.docData = docData;
	
	drnUp_Var.pageMode = pageMode;
	
	// 프로젝트의 dcc유저를 가져옴
	drnUp_Var.dccUser = [];
	$.ajax({
	    url: 'get_dccUser.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:params.prj_info.id
	    },
	    success: function onData (data) {
	    	drnUp_Var.dccUser = data.model.dccUser;	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	// review conclusion 값 가져옴
	drnUp_Var.default_review_conclusion;
	$.ajax({
	    url: 'get_review_conclusion.do',
	    type: 'POST',
	    data:{
	    	prj_id:params.prj_info.id,
	    	drn_id:params.pageData? params.pageData.drnInfo.drn_id : '-1'
	    },
	    success: function onData (data) {
	    	drnUp_Var.default_review_conclusion = data.model.rvc;
	    	$("#drn_review_conclusion")[0].value = drnUp_Var.default_review_conclusion;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	drnUp_Var.discip = discipObj;
	drnUp_Var.folder_id = folder_id;
	drnUp_Var.lineData = {};
	drnUp_Var.lineData.design = {};
	drnUp_Var.lineData.reviewer = {};
	drnUp_Var.lineData.approver = {};
	drnUp_Var.lineData.distributer = {};
	drnUp_Var.lineData.dcc = {};
	drnUp_Var.approval_id = -1;
	
	// DRN 페이지의 타이틀
	drnUp_Var.modes = {
			0 : "기안함 DRN 상세페이지",
			1 : "작성중 DRN 상세페이지",
			2 : "결재함 DRN 상세페이지",
			3 : "미결함 DRN 상세페이지",
			4 : "예고함 DRN 상세페이지",
			5 : "진행함 DRN 상세페이지",
			6 : "반려함 DRN 상세페이지",
			7 : "완료함 DRN 상세페이지"
	};
	
	drnUp_Var.default_sheet = [];
	drnUp_Var.cntChg = false;
	drnUp_Var.rowIdx = 0;
	
	let default_sheet_row = {};
	default_sheet_row.index = 0;
	default_sheet_row.items_no = 1
	default_sheet_row.items_nm = "공통 사항"
	default_sheet_row.items_cnt = 7;
    default_sheet_row.check_items = [];
    default_sheet_row.check_items.push("1) Standard Form을 사용 하였는가?");
    default_sheet_row.check_items.push("2) Doc No. Rev. No. Title Status, 제출일 등이 표주에 따라 작성 되었는가?");
    default_sheet_row.check_items.push("3) Index Page는 작성 되었는가?");
    default_sheet_row.check_items.push("4) 작성자, 검토자, 승인자 확인은 되었는가?");
    default_sheet_row.check_items.push("5) 수정(변경) 사항은 Cloud mark 및 별도의 표시가 되었는가?");
    default_sheet_row.check_items.push("6) Revision Log의 개정 History Revised Page, Description이 자세하게 작성 되어 있는가?");
    default_sheet_row.check_items.push("7) 지난 성공/실패 사례집이 적용 되었는가?");
    default_sheet_row.discipline_code_id = [];
    for(let i=0;i<7;i++)
    	default_sheet_row.discipline_code_id.push(drnUp_Var.discip? drnUp_Var.discip.code : "");
    
    default_sheet_row.design_check_status = [];
    for(let i=0;i<7;i++)
    	default_sheet_row.design_check_status.push("");
    default_sheet_row.reviewed_check_status = [];
    for(let i=0;i<7;i++)
    	default_sheet_row.reviewed_check_status.push("");
    default_sheet_row.approved_check_status = [];
    for(let i=0;i<7;i++)
    	default_sheet_row.approved_check_status.push("");
    
    default_sheet_row.btn = [];
    drnUp_Var.default_sheet.push(default_sheet_row);
    
    
	
	drnUp_Var.sheet_id = "new";
	drnUp_Var.design = {};
	console.log("chk",pageData);
	if(!drnUp_Var.drn_id)
		drnUp_Var.design.user_id = session_user_id;

	
	
	
	// pageData가 있는 경우 매핑
	// 기안함 처음 작성하는 경우를 제외하면 pageData가 존재
	if(pageData){
		drnUp_Var.pageData = pageData;
		drnUp_Var.sheet_id = pageData.check_sheet[0].check_sheet_id;
		// console.log(pageData.check_sheet_id);
	}
		
}


/**
 * readonly mode
 * @returns
 */
function readOnlyMode(){
//	$("#drn_Accept")[0].style["display"] = "none";
//	$("#drn_Deny")[0].style["display"] = "none";
//	$(".drn_file_btns").css('display','none');
	
	//$("#Status_write").attr("disabled",true);
	$(".drn_readMode").css('display','none');
	$(".drn_read").attr("readonly",true);
	//$(".drn_read").attr("disabled",true); 
}



/**
 * DRN 페이지에 쓰이는 그리드 초기화
 * @returns
 */
function initDrnGrid(){
	
	
	drnUp_Var.grid = [];
	
	let fileGrid = new Tabulator("#drn_FileGrid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		movableColumns: false,
		selectable:true,
		index:"index",
		height: 100,
		columns: [			
			{ title:"index",field:"index",visible:false},
			{ formatter:"rowSelection",
			  field: "chk_col",
			  titleFormatter:"rowSelection",
			  resizable:false,
			  widthGrow:1,
			  hozAlign:"center",
			  headerSort:false, 
			  cellClick:function(e, cell){ cell.getRow().toggleSelect();}
			},
			{
				title: "파일명(DOC NO)",
				field: "doc_no",
				headerSort:false,
				resizable:false,
				widthGrow:6
				//width: "35%"
			},
			{
				title: "REVISION",
				field: "revision",
				headerSort:false,
				resizable:false,
				widthGrow:2
				//width: "15%"
			},
			{
				title: "rev_id",
				field: "rev_id",
				resizable:false,
				visible:false,
			},
			{
				title: "문서명",
				field: "title",
				headerSort:false,
				resizable:true,
				widthGrow:8
				//width: "35%"
			},
			{
				title: "SIZE",
				field: "file_size",
				resizable:false,
				headerSort:false,
				formatter:function(cell, formatterParams, onRendered){
				    let size = cell.getValue();
				    size = Number(size);
				    size = formatBytes(size, 2);
				    return size;
				},
				widthGrow:2
				//width: "15%"
			},
		]
	});
	
	drnUp_Var.grid.push(fileGrid);
	
	let sheetGrid = new Tabulator("#drn_SheetGrid", {
		layout: "fitColumns",
		placeholder:"",
		//autoResize:true,
		data:drnUp_Var.default_sheet,
		selectable:false,
		index:"index",
		columns: [			
			{ title:"index",field:"index",visible:false},
			{
				title: "NO",
				field: "items_no",
				headerSort:false,
				//width:"5%",
				widthGrow:1,
				resizable:false
			},
			{
				title: "items_cnt",
				field: "items_cnt",
				headerSort:false,
				visible:false
			},
			{
				title: "ITEMS",
				field: "items_nm",
				headerSort:false,
				formatter: function(cell, formatterParams, onRendered) {
					// page 모드에 따른 엘리먼트 제어
					let controlLevel = drnUp_Var.as_map[drnUp_Var.as];
					// console.log(controlLevel);
					
					// 배열로 받음
					const data = cell.getValue();
					let div = document.createElement("div");
					div.classList.add('items_no');
					
					let input = document.createElement("input");
					input.value = data;
					input.classList.add('drn_read');
					input.classList.add('drn_item_nm');
					if(controlLevel >= 1)
						input.readOnly = true;
					
					let div2 = document.createElement("div");
					div2.classList.add('drn_items_no');
					div2.style["margin-top"] = "5px";
					let txt = "문항  ";
					let select = document.createElement("select");
					select.id = "items_no_" + cell.getData().items_no;
					select.classList.add('drn_read');
					
					for(let i=1;i<=20;i++){
						let opt = document.createElement("option");
						opt.value = i;
						opt.text = i;
						select.append(opt);
					}
					let cnt = cell.getData().items_cnt;
					select.value = cnt;
					if(controlLevel >= 1)
						select.disabled = true;
					
					input.onchange = function(){
						cell.setValue(this.value);
					}
					if(drnUp_Var.pageMode > 1) {
						input.readOnly = true;
						input.style["border"] = "none";
					}
					
					select.onchange = function(){
						let row = cell.getRow();
						drnUp_Var.cntChg = true;
						drnUp_Var.rowIdx = row.getData().index;
						row.getCell("items_cnt").setValue(this.value);
						drnUp_Var.grid[1].redraw();
					}
					
					if(drnUp_Var.pageMode > 1) select.disabled = true;
					
					div.append(input,document.createElement("br"));
					div2.append(txt,select);
					div.append(div2);
					
			        return div;
				},
				
				// width:"15%",
				widthGrow:5,
				resizable:false
			},
			{
				title: "CHECK POINT",
				field: "check_items",
				headerSort:false,
				formatter: function(cell, formatterParams, onRendered) {
			        // 배열로 받음
					let data = cell.getValue();
					if(!data) data = [];
					let cnt = cell.getData().items_cnt;
					
					let div = document.createElement("div");
					for(let i=0;i<cnt;i++){
						let row = document.createElement("div");
						if(i < cnt-1)
							row.classList.add('checkgrid_Row');
						else
							row.classList.add('checkgrid_LastRow');
						
						let rowCont = document.createElement("div");
						// rowCont.innerHTML = (i+1) + ") ";
						let rowInput = document.createElement("input");
							rowInput.value = data[i]?data[i]:"　";
							rowInput.style.border = "none";
							rowInput.style.width = "100%";
							rowInput.style.lineHeight = "24px";
							rowInput.style.marginLeft = "5px";
							rowInput.id = "chk_" + cell.getData().items_no +"_" + i;
							rowInput.onchange = function(){
								let rowIdx = i;
								let data = cell.getValue();
								data[rowIdx] = rowInput.value;
								cell.setValue(data);
							}
							
							switch(drnUp_Var.pageMode){
								case 0:
								case 1:
									break;
								default:
									rowInput.readOnly = true;
							}
							rowCont.append(rowInput);
							row.append(rowCont);						
						div.append(row);
					}
			        return div;
				},
				//width:"40%",
				widthGrow:12,
				resizable:false
			},
			{
				title: "DISCIPLINE",
				field: "discipline_code_id",
				headerSort:false,
				resizable:false,
				widthGrow:2,
				formatter: function(cell, formatterParams, onRendered) {
			        // 배열로 받음
					let data = cell.getValue();
					if(!data) data = [];
					let cnt = cell.getData().items_cnt;
					
					let div = document.createElement("div");
					for(let i=0;i<cnt;i++){
						let row = document.createElement("div");
						if(i < cnt-1)
							row.classList.add('checkgrid_Row');
						else
							row.classList.add('checkgrid_LastRow');
						let rowPad = document.createElement("div");
						rowPad.classList.add("rowPadding");
						rowPad.innerHTML = "　";
						let rowCont = document.createElement("div");
						rowCont.innerHTML = drnUp_Var.discip? drnUp_Var.discip.code : "";
						// row.append(rowPad);
						row.append(rowCont);				
						div.append(row);
					}
			        return div;
				},

			},
			{
				title: "DESIGN",
				field: "design_check_status",
				headerSort:false,
				resizable:false,
				formatter: function(cell, formatterParams, onRendered) {
			        // 배열로 받음
					let data = cell.getValue();
					if(!data) data = []
					let cnt = cell.getData().items_cnt;
					
					let div = document.createElement("div");
					for(let i=0;i<cnt;i++){
						let row = document.createElement("div");
						let rowIdx = i;					
						if(i < cnt-1)
							row.classList.add('checkgrid_BtnRow');					
						else
							row.classList.add('checkgrid_LastBtnRow');						
							
						
						let rowPad = document.createElement("div");
						rowPad.classList.add("rowPadding");
						rowPad.innerHTML = "　";
						let rowCont = document.createElement("select");
						rowCont.classList.add("design_select");
						
						// page 모드에 따른 엘리먼트 제어
						let controlLevel = drnUp_Var.as_map[drnUp_Var.as];
						if(controlLevel > 0){
							rowCont.classList.add("drn_display");
							rowCont.style["background"]= "none";
							rowCont.style["padding"]= "0 0 0 0";
							rowCont.disabled = true;
						}
						
						
						
						let opt1 = document.createElement("option");
						opt1.value = "1";
						opt1.text = "O";
						let opt2 = document.createElement("option");
						opt2.value = "2";
						opt2.text = "X";
						let opt3 = document.createElement("option");
						opt3.value = "3";
						opt3.text = "△";
						let opt4 = document.createElement("option");
						opt4.value = "4"
						opt4.text = "N/A"
						rowCont.append(opt1);
						rowCont.append(opt2);
						rowCont.append(opt3);
						rowCont.append(opt4);
						rowCont.value = data[i]?data[i]:"";
						

						rowCont.onchange = function(){
							let data = cell.getValue();							
							let rowIdx = i;
							data[rowIdx] = rowCont.value;
							cell.setValue(data);
						};
										
						
						row.append(rowPad);
						row.append(rowCont);
						div.append(row);
					}
			        return div;
				},
				//width: 100
				widthGrow:2
			},
			{
				title: "REVIEWED",
				field: "reviewed_check_status",
				headerSort:false,
				resizable:false,
				formatter: function(cell, formatterParams, onRendered) {
			        // 배열로 받음
					let data = cell.getValue();
					if(!data) data = []
					let cnt = cell.getData().items_cnt;
					
					let div = document.createElement("div");
					for(let i=0;i<cnt;i++){
						let row = document.createElement("div");
						let rowIdx = i;
						if(i < cnt-1)
							row.classList.add('checkgrid_BtnRow');
						else
							row.classList.add('checkgrid_LastBtnRow');
						
						let rowPad = document.createElement("div");
						rowPad.classList.add("rowPadding");
						rowPad.innerHTML = "　";
						let rowCont = document.createElement("select");
						rowCont.classList.add("reviewed_select");
						
						
						let controlLevel = drnUp_Var.as_map[drnUp_Var.as];
						if(controlLevel <= 1){
							rowCont.classList.add("drn_display");
							rowCont.classList.add("drn_noUse");
						}
						else if(controlLevel > 2){
							rowCont.classList.add("drn_display");							
							rowCont.style["background"]= "none"
							rowCont.style["padding"]= "0 0 0 0";
							rowCont.disabled = true;
						}
						
						let opt1 = document.createElement("option");
						opt1.value = "1";
						opt1.text = "O";
						let opt2 = document.createElement("option");
						opt2.value = "2";
						opt2.text = "X";
						let opt3 = document.createElement("option");
						opt3.value = "3";
						opt3.text = "△";
						let opt4 = document.createElement("option");
						opt4.value = "4"
						opt4.text = "N/A"
						rowCont.append(opt1);
						rowCont.append(opt2);
						rowCont.append(opt3);
						rowCont.append(opt4);
						rowCont.value = data[i]?data[i]:"";
						
						rowCont.onchange = function(){
							let data = cell.getValue();							
							let rowIdx = i;
							data[rowIdx] = rowCont.value;
							cell.setValue(data);
						};
						
						row.append(rowPad);
						row.append(rowCont);				
						div.append(row);
					}
			        return div;
				},
				// width: 100
				widthGrow:2
			},
			{
				title: "APPROVED",
				field: "approved_check_status",
				headerSort:false,
				resizable:false,
				formatter: function(cell, formatterParams, onRendered) {
			        // 배열로 받음
					let data = cell.getValue();
					if(!data) data = []
					let cnt = cell.getData().items_cnt;
					
					let div = document.createElement("div");
					for(let i=0;i<cnt;i++){
						let row = document.createElement("div");
						let rowIdx = i;
						if(i < cnt-1)
							row.classList.add('checkgrid_BtnRow');
						else
							row.classList.add('checkgrid_LastBtnRow');
						
						let rowPad = document.createElement("div");
						rowPad.classList.add("rowPadding");
						rowPad.innerHTML = "　";
						let rowCont = document.createElement("select");
						rowCont.classList.add("approved_select");	
						
						let controlLevel = drnUp_Var.as_map[drnUp_Var.as];
						if(controlLevel < 3){ // 리뷰 승인 전
							rowCont.classList.add("drn_display");
							rowCont.classList.add("drn_noUse");
						}
						else if(controlLevel >= 4){ // 최종 승인 이후
							rowCont.classList.add("drn_display");
							rowCont.style["background"]= "none";
							rowCont.style["padding"]= "0 0 0 0";
							rowCont.disabled = true;
						}
						
						
						let opt1 = document.createElement("option");
						opt1.value = "1";
						opt1.text = "O";
						let opt2 = document.createElement("option");
						opt2.value = "2";
						opt2.text = "X";
						let opt3 = document.createElement("option");
						opt3.value = "3";
						opt3.text = "△";
						let opt4 = document.createElement("option");
						opt4.value = "4"
						opt4.text = "N/A"
						rowCont.append(opt1);
						rowCont.append(opt2);
						rowCont.append(opt3);
						rowCont.append(opt4);
						
						rowCont.value = data[i] ? data[i] : "";					
						
						rowCont.onchange = function(){
							let data = cell.getValue();							
							let rowIdx = i;
							data[rowIdx] = rowCont.value;
							cell.setValue(data);
						};
						
						row.append(rowPad);
						row.append(rowCont);				
						div.append(row);
					}
			        return div;
				},
				//width: 100
				widthGrow:2
			},
			{
				title: "",
				field: "btn",
				//width:"9%",
				headerSort:false,
				resizable:false,
				visible:false,
				widthGrow:1
			},
		],
	});
	
	drnUp_Var.grid.push(sheetGrid);
	
	let reviewGrid = new Tabulator("#drn_ReviewConGrid", {
		layout: "fitColumns",
		placeholder:"",
		index:"rowId",
		selectable:false,
		movableColumns: false,
		rowFormatter:function(row){
	        let data = row.getData();
	        if(data.rowId == "name")
	            row.getElement().classList.add("nameRow");
	        else if(data.rowId == "sign")
	        	row.getElement().classList.add("signRow");
	        else if(data.rowId == "date")
	            row.getElement().classList.add("dateRow");	        
	    },
		columns: [
			{field:"rowId",visible:false},
			{
				headerSort:false,
				title: "DESIGNED",
				field: "design",
				hozAlign:"center",
				formatter:"html",
				resizable:false
			},
			{
				headerSort:false,
				title: "REVIEWED",
				field: "reviewed",
				hozAlign:"center",
				formatter:"html",
				resizable:false
			},
			{
				headerSort:false,
				title: "APPROVED",
				field: "approved",
				hozAlign:"center",
				formatter:"html",
				resizable:false
			}
		],
		data:[
			{rowId:"name"},
			{rowId:"sign"},
			{rowId:"date"}
		]
	});
	
	drnUp_Var.reviewGrid = reviewGrid;
	
	let chk_modal_grid = new Tabulator("#chk_modal_grid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		index:"index",
		//minHeight:300,
		selectable:1,
		columns: [
			{field:"index",visible:false},
			{
				headerSort:false,
				title: "drn no",
				field: "drn_no",
				hozAlign:"center",
				widthGrow : 2
			},
			{
				headerSort:false,
				title: "drn title",
				field: "drn_title",
				hozAlign:"center",
				widthGrow : 2
			},
			{
				headerSort:false,
				title: "drn_id",
				field: "drn_id",
				visible:false
			},
			{
				headerSort:false,
				title: "체크시트",
				field: "check_sheet_id",
				visible:false
			},		
			{
				headerSort:false,
				title: "check sheet title",
				field: "check_sheet_title",
				hozAlign:"center",
				widthGrow : 6
			}
		]
	});
	
	drnUp_Var.chk_modal_grid = chk_modal_grid;
	
	drnUp_Var.grid[0].on("tableBuilt", function(){
		set_FileGrid(drnUp_Var.docData);
	});
	
	
	
	drnUp_Var.grid[0].on("rowDblClick",function(e, row){
		
		let data = row.getData();
		location.href='/downloadFile.do?prj_id='+encodeURIComponent(data.prj_id)+'&sfile_nm='+encodeURIComponent(data.sfile_nm)+'&rfile_nm='+encodeURIComponent(data.rfile_nm.replaceAll("&","@%@"));
		//location.href='/downloadFile.do?prj_id='+encodeURIComponent(data.prj_id)+'&sfile_nm='+encodeURIComponent(data.sfile_nm)+'&rfile_nm='+encodeURIComponent(row.getData().bean.doc_no+'.'+row.getData().bean.file_type);
		
	});
	/*
	drnUp_Var.grid[0].on("rowAdded", function(row){
		
		let getGridData = row.getTable().getData();
		chg_drnInput(getGridData);
		
	});
	*/
	
	


	
	
	
	drnUp_Var.reviewGrid.on("tableBuilt", function(){
		// if(drnUp_Var.pageMode<2)
		if(drnUp_Var.as == '')
		$.ajax({
		    url: 'searchUsersVOwithCode.do',
		    type: 'POST',
		    data:{
		    	user_id:session_user_id,
		    },
		    success: function onData (data) {
		    	drnUp_Var.design = data[0];
		    	let design = data[0].user_kor_nm + " " + data[0].code_nm;
		    	design = design.trim();
		    	set_drnSigns("design","name",design);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		
		
		
		if(drnUp_Var.drn_id){
			$.ajax({
			    url: 'searchUsersVOwithCode.do',
			    type: 'POST',
			    data:{
			    	prj_id:params.prj_info.id,
			    	code:drnUp_Var.drn_id // drn_id를 받는 userVO의 변수이름 code로 활용			    	
			    },
			    success: function onData (data) {
			    	drnUp_Var.design = data[0];
			    	let design = data[0].user_kor_nm + " " + data[0].code_nm;
			    	design = design.trim();
			    	set_drnSigns("design","name",design);
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		}
		
		
	});
	
	
	
	drnUp_Var.chk_modal_grid.on("tableBuilt", function(){
		$.ajax({
		    url: 'get_checkList.do',
		    type: 'POST',
		    data:{
		    	prj_id:params.prj_info.id
		    },
		    success: function onData (data) {
		    	drnUp_Var.chk_modal_grid.setData(data[0]);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	
	
	
	drnUp_Var.grid[1].on("tableBuilt", function(data){
		if(drnUp_Var.readOnly)readOnlyMode();		
	});
	
	
	// 해당 row의 문항 갯수 설정시 테이블 셋팅
	drnUp_Var.grid[1].on("dataChanged", function(data){
		// 문항 수 변경 시
		if(drnUp_Var.cntChg){
			drnUp_Var.cntChg = false;
			rowUpdate(data[drnUp_Var.rowIdx]);			
		}
		
	});
	
	
	
	
	setLineGrid();
}

function rowUpdate(data){

    let item = data.check_items;
	let design = data.design_check_status;
	let review = data.reviewed_check_status;
	let approved = data.approved_check_status;
	let discip = data.discipline_code_id;
	
    let items_cnt = data.items_cnt;
	let item_cnt = item.length;
    if(items_cnt<item_cnt){ // 문항 수 감소
    	data.check_items.splice(items_cnt);
    	data.design_check_status.splice(items_cnt);
    	data.reviewed_check_status.splice(items_cnt);
    	data.approved_check_status.splice(items_cnt);
    	data.discipline_code_id.splice(items_cnt);
        
    }else{ // 문항 수 증가
        for(let i=0 ; i<items_cnt - item_cnt ; i++){
        	data.check_items.push(" ");
        	data.design_check_status.push("");
        	data.reviewed_check_status.push("");
        	data.approved_check_status.push("");
        	data.discipline_code_id.push(drnUp_Var.discip.code ? drnUp_Var.discip.code : " ");
        }    	    
    }
	
	
	drnUp_Var.grid[1].updateRow(data.index,data).then(function(){
	    //run code after table has been successfuly updated
	})
	.catch(function(error){
	   // error - Fetch response object
	});
}

function itemEdit(row,item_idx){
	let tmpRow = row.getData();
	$("#chk_" + tmpRow.items_no + "_" + item_idx).attr("readonly",false);
	$("#chk_" + tmpRow.items_no + "_" + item_idx).css("border","");
}
/**
 * 
 * @param row
 * @param item_idx
 * @returns
 */
function itemDel(row,item_idx){
	
	let tmpRow = row.getData();
	// 삭제하는 시점에, 카운트가 1(한개의 로우만 존재)이면 삭제 불가
	if(tmpRow.items_cnt == 1) return;
	
	drnUp_Var.cntChg = true;
	//$("#items_no_" + tmpRow.items_no).val(--tmpRow.items_cnt);
	tmpRow.items_cnt--;
	tmpRow.check_items.splice(item_idx,1);
	tmpRow.discipline_code_id.splice(item_idx,1);
	tmpRow.design_check_status.splice(item_idx,1);
	tmpRow.reviewed_check_status.splice(item_idx,1);
	tmpRow.approved_check_status.splice(item_idx,1);
	
	rowUpdate(tmpRow);
	$("#items_no_" + tmpRow.items_no).val(tmpRow.items_cnt);
}
/**
 * 체크리스트의 아이템 추가 버튼
 * @returns
 */
function itemsAdd(){
	
	
	let tmpGrid = drnUp_Var.grid[1];
	drnUp_Var.cntChg = false;
	
	let default_sheet_row = {};
	default_sheet_row.index = tmpGrid.getData().length;
	default_sheet_row.items_no = tmpGrid.getData().length + 1;
	default_sheet_row.items_nm = "";
	default_sheet_row.items_cnt = 1;
    default_sheet_row.check_items = [" "];
    default_sheet_row.discipline_code_id = [drnUp_Var.discip.code ? drnUp_Var.discip.code : " "];
    default_sheet_row.design_check_status = [""];
    default_sheet_row.reviewed_check_status = [""];
    default_sheet_row.approved_check_status = [""];
    default_sheet_row.btn = [];
    
    
    tmpGrid.addRow(default_sheet_row);
    drnUp_Var.grid[1].redraw();
    
    

}
/**
 * 체크리스트의 아이템 삭제 버튼
 * @returns
 */
function itemsDel(){
	drnUp_Var.cntChg = false;
	let tmpGrid = drnUp_Var.grid[1];
	let lastRow = tmpGrid.getRowFromPosition(tmpGrid.getData().length-1);
	tmpGrid.deleteRow(lastRow);
	drnUp_Var.grid[1].redraw();
}

/**
 * mode에 따라 다른 페이지를 구성해준다.
 * @param pageMode
 * @returns
 */
function setDrnPage(pageMode){
	// page의 이름 return
	let mode = drnUp_Var.modes[pageMode];
	let pageData = null;
	if(drnUp_Var.pageData) {
		pageData = drnUp_Var.pageData;
		pageData.drn_id;
	}
	
	// approval status
	let as = drnUp_Var.as_map[drnUp_Var.as];

	
	/* 버튼 */
	let btnList = [drn_setLine,drn_tempSave,drn_upLine,drn_accept,drn_deny,drn_print];
	
	/* input */
	drnUp_Var.inputs = $(".drn_inputs");
	
	// input필드 초기화
	for(let i=0;i<drnUp_Var.inputs.length;i++) drnUp_Var.inputs[i].value = "";
	drnUp_Var.inputs[9].value = "CHECK SHEET"; 
	
	drnUp_Var.btns = $(".drn_btns");
	for(let i=0;i<btnList.length;i++)
		drnUp_Var.btns[i].onclick = function(){ btnList[i]() };
		
	drnUp_Var.fileBtns = $(".drn_file_btns");
	
	// set_drnInput();
	// drn페이지를 제어하는 유저의 상태를 나타내는 값을 레벨로 표시
	// 맨처음 design 단계
	let level;
	// 모드에 따른 버튼 구성 변화
	switch(as){
		case -1:
		case 0: // 임시저장
			drnUp_Var.btns[3].style["display"] = "none";
			drnUp_Var.btns[4].style["display"] = "none";

			level = 0;
			if(pageData!=null)
				setDrnPageFromData(pageData);
			break;
		case 1: // 결재상신
			$("#drnLocalFileDelBtn")[0].style["display"] ="none"; // 로컬파일 삭제버튼
			
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			
			
			
			$("#drn_Accept").text("검토 완료");
			level = 0;
		
			if(pageData!=null)
				setDrnPageFromData(pageData);
			break;
		case 2: // DCC 승인
			$("#drnLocalFileDelBtn")[0].style["display"] ="none"; // 로컬파일 삭제버튼
			
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			$("#drn_Accept").text("승인");
			level = 1;
			break;
		case 3: // 리뷰 승인
			$("#drnLocalFileDelBtn")[0].style["display"] ="none"; // 로컬파일 삭제버튼
			
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			$("#drn_Accept").text("승인");
			level = 2;
			break;
		case 4: //
		case 5:
			$("#drnLocalFileDelBtn")[0].style["display"] ="none"; // 로컬파일 삭제버튼
			
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			drnUp_Var.btns[3].style["display"] = "none"; // 승인
			drnUp_Var.btns[4].style["display"] = "none"; // 반려
			break;
			
	}
	
	if(pageData) setDrnPageFromData(pageData);
	
	/*
	if(pageMode == 1){ // 작성중 페이지에서 임시저장 비활성화
		// drnUp_Var.btns[1].style["display"] = "none";
		drnUp_Var.fileBtns[2].style["display"] = "none";
	}
	*/

	if(pageMode >= 2 || drnUp_Var.readOnly){
		drnUp_Var.btns[6].style["display"] = "none";
		drnUp_Var.btns[7].style["display"] = "none";
		drnUp_Var.btns[8].style["display"] = "none";
		
		
		drnUp_Var.fileBtns[0].style["display"] = "none";
		drnUp_Var.fileBtns[1].style["display"] = "none";
		drnUp_Var.fileBtns[2].style["display"] = "none";
		drnUp_Var.fileBtns[3].style["display"] = "none";
		
		drnUp_Var.inputs[9].disabled = true;
		$("#drn_itemCount")[0].style["display"] = "none";
		$("#drn_itemCount_btnDiv")[0].style["display"] = "none";
		$("#Status_write").attr("disabled",true);
		drnUp_Var.inputs[4].disabled = true;
		setReadMode();
		$("#Date_write").datepicker('destroy');
		
		setDrnPageFromData(pageData);
		// setReadMode();
		/*
		drnUp_Var.inputs.attr("disabled",false);
		drnUp_Var.inputs.attr("readonly",true);
		*/
	}
	
	$("#drn_allSelect")[0].onchange = function(){
		drn_setAll(level);
	};
	
	$("#drn_allSelectBtn")[0].onclick = function(){drn_setAll(level)};	
	
	
	
	return mode;
}

/**
 * pageData로부터 데이터를 불러와 페이지를 셋팅해준다.
 * @param pageData
 * @returns
 */
function setDrnPageFromData(pageData){
	
	// DRN ID가 있는 경우
	drnUp_Var.drn_id = pageData.drnInfo.drn_id;
	
	// DRN_NO 가져오기
	drnUp_Var.inputs[1].value = pageData.drnInfo.drn_no;
	// status 값 불러오기
	let status = pageData.drnInfo.status;
	$("#Status_write").val(status);
	

	
	
	// 체크시트 데이터 불러오기
	let sheetData = {};
	sheetData.check_sheet = pageData.check_sheet;
	sheetData.check_sheet_items = pageData.check_sheet_items;
	sheetData.check_sheet_title = pageData.check_sheet_title;
	
	if(!drnUp_Var.grid[1].initialized){
		drnUp_Var.grid[1].on("tableBuilt", function(){
			setCheckSheetGrid(sheetData);
		});
	} else setCheckSheetGrid(sheetData);
	
	
	// 결재선 정보 불러오기
	let lineInfo = pageData.lineInfo;
	
	if(lineInfo){
		drnUp_Var.approval_id = lineInfo.approval_id;
		drnUp_Var.design = {user_id : pageData.ds_info.user_id, user_kor_nm:pageData.ds_info.user_kor_nm};
		if(!drnUp_Var.grid[3].initialized){
			drnUp_Var.grid[3].on("tableBuilt", function(){
				setApprovalLine(lineInfo);
			});
		} else setApprovalLine(lineInfo);
		
	}
		
	
	let approval_status = {};
	approval_status.design = {};
	approval_status.design.date = pageData.ds_date;
	approval_status.design.info = pageData.ds_info;
	
	approval_status.review = {};
	approval_status.review.date = pageData.rv_date;
	approval_status.review.info = pageData.rv_info;
	
	approval_status.approve = {};
	approval_status.approve.date = pageData.ap_date;
	approval_status.approve.info = pageData.ap_info;
	
	
	setApprovalStatus(approval_status);
	

	
}

/**
 * 가져온 배포선 정보를 바탕으로 배포선 세팅
 * @returns
 */
function setApprovalLine(lineInfo){
	
	let distributer = [];
	let distribute_id = lineInfo.distribute_id.split("@");
	let distribute_nm = lineInfo.distributers.split("@");
	
	for(let i=0;i<distribute_id.length;i++){
		let tmp = {user_id:distribute_id[i] ,user_kor_nm:distribute_nm[i]};
		distributer.push(tmp);
	}
	
	// 기안자 셋팅
	if(drnUp_Var.design)
		drnUp_Var.lineData.design = { user_id: drnUp_Var.design.user_id , user_kor_nm : drnUp_Var.design.user_kor_nm};
	
	// DCC 셋팅
	setDcc();
	// 검토자 셋팅
	drnUp_Var.grid[3].getRowFromPosition(1).getCell("user_kor_nm").setValue(lineInfo.reviewed);
	drnUp_Var.lineData.reviewer = { user_id: lineInfo.reviewed_id, user_kor_nm : lineInfo.reviewed};
	
	
	// 결재자 셋팅
	drnUp_Var.grid[3].getRowFromPosition(2).getCell("user_kor_nm").setValue(lineInfo.approved);
	drnUp_Var.lineData.approver = { user_id: lineInfo.approved_id, user_kor_nm : lineInfo.approved};
	
	// 배포자 셋팅
	if(!drnUp_Var.grid[4].initialized){
		drnUp_Var.grid[4].on("tableBuilt", function(){
			drnUp_Var.grid[4].setData(distributer);
		});
	} else drnUp_Var.grid[4].setData(distributer);
	
	drnUp_Var.lineData.distributer = distributer; 
		
	drnUp_Var.lineData.dcc = drnUp_Var.dccUser;  // drnUp_Var.grid[3].getData()[0].dccIdList;
	
	// 하단 서명부 이름 설정
	if(lineInfo.approval_id > 0) {
		if(!drnUp_Var.reviewGrid.initialized)
			drnUp_Var.reviewGrid.on("tableBuilt", function(){
				let design_nm = drnUp_Var.lineData.design.user_kor_nm + " "
					+ drnUp_Var.pageData.lineInfo.designer_code;
				design_nm = design_nm.trim();
				set_drnSigns("design","name",design_nm);
				let review_nm = drnUp_Var.lineData.reviewer.user_kor_nm + " " 
					+ drnUp_Var.pageData.lineInfo.reviewer_code;
				review_nm = review_nm.trim();
				set_drnSigns("reviewed","name",review_nm);
				let approve_nm = drnUp_Var.lineData.approver.user_kor_nm + " "
					+ drnUp_Var.pageData.lineInfo.approver_code;
				approve_nm = approve_nm.trim();
				set_drnSigns("approved","name",approve_nm);
			});
		else {
			let design_nm = drnUp_Var.lineData.design.user_kor_nm + " "
			+ drnUp_Var.pageData.lineInfo.designer_code;
			design_nm = design_nm.trim();
			set_drnSigns("design","name",design_nm);
			let review_nm = drnUp_Var.lineData.reviewer.user_kor_nm + " " 
				+ drnUp_Var.pageData.lineInfo.reviewer_code;
			review_nm = review_nm.trim();
			set_drnSigns("reviewed","name",review_nm);
			let approve_nm = drnUp_Var.lineData.approver.user_kor_nm + " "
				+ drnUp_Var.pageData.lineInfo.approver_code;
			approve_nm = approve_nm.trim();
			set_drnSigns("approved","name",approve_nm);
		}
	}
}

/**
 * 서명 정보를 셋팅해준다.
 * @param data
 * @returns
 */
function setApprovalStatus(data){
	
	if(data.design.date != null){
		if(!drnUp_Var.reviewGrid.initialized){
			drnUp_Var.reviewGrid.on("tableBuilt", function(){
				set_drnSigns("design","date",getDateFormat_YYYYMMDD(data.design.date) );
				if(data.design.info.sfile_nm != null){
					set_drnSigns("design","sign",'<img src="/getSignImage.do?sfile_nm='+data.design.info.sfile_nm+'" style="width:100%; height: 100%;">');
				}else {
					set_drnSigns("design","sign",data.design.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("design","date", getDateFormat_YYYYMMDD(data.design.date) );
			if(data.design.info.sfile_nm != null){
				set_drnSigns("design","sign",'<img src="/getSignImage.do?sfile_nm='+data.design.info.sfile_nm+'" style="width:100%; height: 100%;">');
			}else {
				set_drnSigns("design","sign",data.design.info.user_kor_nm );
			}
		}
		
	}
	
	if(data.review.date != null){
		if(!drnUp_Var.reviewGrid.initialized){
			drnUp_Var.reviewGrid.on("tableBuilt", function(){
				set_drnSigns("reviewed","date",getDateFormat_YYYYMMDD(data.review.date) );
				if(data.review.info.sfile_nm != null){
					set_drnSigns("reviewed","sign",'<img src="/getSignImage.do?sfile_nm='+data.review.info.sfile_nm+'" style="width:100%; height: 100%;">');
				}else {
					set_drnSigns("reviewed","sign",data.review.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("reviewed","date", getDateFormat_YYYYMMDD(data.review.date) );
			if(data.review.info.sfile_nm != null){
				set_drnSigns("reviewed","sign",'<img src="/getSignImage.do?sfile_nm='+data.review.info.sfile_nm+'" style="width:100%; height: 100%;">');
			}else {
				set_drnSigns("reviewed","sign",data.review.info.user_kor_nm );
			}
		}
		
	}
	
	if(data.approve.date != null){
		if(!drnUp_Var.reviewGrid.initialized){
			drnUp_Var.reviewGrid.on("tableBuilt", function(){
				set_drnSigns("approved","date",getDateFormat_YYYYMMDD(data.approve.date) );
				if(data.approve.info.sfile_nm != null){
					set_drnSigns("approved","sign",'<img src="/getSignImage.do?sfile_nm='+data.approve.info.sfile_nm+'" style="width:100%; height: 100%;">');
				}else {
					set_drnSigns("approved","sign",data.approve.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("approved","date", getDateFormat_YYYYMMDD(data.approve.date) );
			if(data.approve.info.sfile_nm != null){
				set_drnSigns("approved","sign",'<img src="/getSignImage.do?sfile_nm='+data.approve.info.sfile_nm+'" style="width:100%; height: 100%;">');
			}else {
				set_drnSigns("approved","sign",data.approve.info.user_kor_nm );
			}
		}
		
	}
}

/**
 * 결재선 지정 버튼
 * @returns
 */
function drn_setLine(){

	
	setLineTree();
	// setLineGrid();
	searchAll();
	
	
	
}

/**
 * 임시저장 버튼
 * @returns
 */
function drn_tempSave(){
	
	/*
	if($("#Status_write").val() == null){
		alert("status값을 지정하지 않으면 임시저장 할 수 없습니다.");
		return;
	}
	*/
	
	if(drnUp_Var.grid[0].getData().length<1){
		alert("Can't temporary Save, unless the document is attached.");
		return;
	}
		
	let pageData = getPageData();
	
	
	// 파일 체크
	let form = $('#drnUploadForm')[0];
    let formData = new FormData(form);
    // 프로젝트 id form 데이터에 추가    
    formData.set("folder_id",drnUp_Var.folder_id);
    formData.set("prj_id",params.prj_info.id);
    formData.set("pageData",JSON.stringify(pageData));
    let local = $("#drn_local_file_txt").text();
    formData.set("rfile_nm",local);
    formData.set("action","T");
    formData.set("user_id",session_user_id);
    
    // ajaxStart();
    
    $.ajax({
	    url: 'drnUpLine.do',
	    enctype: 'multipart/form-data',  
	    type: 'POST',
	    data:formData,          
        processData: false,    
        contentType: false,      
        cache: false,           
        beforeSend:function(){
	       $('body').prepend(loading);
	    },
	    complete:function(){
	    	$('#loading').remove();
	    },
	    success: function onData (data) {

	    	if(data.model.status == "SUCCESS"){
	    		let tsave_msg = "Temporay save Complete. Drafts temporarily saved will be deleted after 10 days if not submitted.";
	    		alert(tsave_msg);
	    		$(".pop_collaboW").dialog("close");
	    		if(opener.getPrjDocumentIndexList)
	    			opener.getPrjDocumentIndexList();
	    		if(opener.getDrnList)
	    			opener.getDrnList();
	    		opener.treeRefresh();
	    		
	    		close();
	    	}else if(data.model.error_msg){
	    		alert(data.model.error_msg);
	    	}else{
	    		alert("Failed to save.");
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
    
    /*
    $.ajax({
		url: 'drnUpLineChkFile.do',
		type: 'POST',
		data: formData,
		enctype:'multipart/form-data',
        processData:false,
        contentType:false,
        dataType:'json',
        cache:false,
        async: false,
		success: function onData (data) {
			if(data > 0) {
				$.ajax({
				    url: 'drnUpLine.do',
				    enctype: 'multipart/form-data',  
				    type: 'POST',
				    data:formData,          
			        processData: false,    
			        contentType: false,      
			        cache: false,           
			        //timeout: 600000,
			        beforeSend:function(){
				       $('body').prepend(loading);
				    },
				    complete:function(){
				    	$('#loading').remove();
				    },
				    success: function onData (data) {
				    	
				    	
				    	if(data.model.status == "SUCCESS"){
				    		let tsave_msg = "Temporay save Complete. Drafts temporarily saved will be deleted after 10 days if not submitted.";
				    		alert(tsave_msg);
				    		$(".pop_collaboW").dialog("close");
				    		if(opener.getPrjDocumentIndexList)
				    			opener.getPrjDocumentIndexList();
				    		if(opener.getDrnList)
				    			opener.getDrnList();
				    		opener.treeRefresh();
				    		
				    		close();
				    	}else{
				    		alert("Failed to save.");
				    	}
				    	
				    	
				    	
				    },
				    error: function onError (error) {
				        console.error(error);
				    }
				});
			}else {
				alert("Unsupported file format. Please contact the administrator");
			}
		},
		error: function onError (error) {
	    	console.error(error);
	    }
	});
	*/
}

/**
 * 결재상신 버튼
 * @returns
 */
function drn_upLine(){
	// alert("결재상신");
	let pageData = getPageData();
	let status = $("#Status_write").val();
	let docData = drnUp_Var.grid[0].getData();
	
	
	if(status == null || status == ""){
		alert("status 값 미지정");
		return;
	}
	
	if(docData.length < 1){
		alert("결재 상신할 문서 미지정");
		return;
	}
	
	// 결재선 체크
	if(!drnUp_Var.reviewGrid.getData()[0].reviewed || !drnUp_Var.reviewGrid.getData()[0].approved){
		alert("결재선 미지정");
		return;
	}
		
	
	// 체크시트 타이틀 입력 여부
	let chkTitle = $(".drn_inputs")[9].value;
	if(chkTitle == ''){
		alert("체크시트의 타이틀을 입력하세요");
		return;
	}
	
	// 점검항목 체크여부 확인
	let tmp = $(".design_select");
	for(let i=0;i<tmp.length;i++){
		  if(tmp[i].value == ''){
			  alert("체크하지 않은 항목이 있습니다.");
			  return;
		  }
	}
	
	
	
	// 파일 체크
	let form = $('#drnUploadForm')[0];
    let formData = new FormData(form);
    // 프로젝트 id form 데이터에 추가    
    formData.set("folder_id",drnUp_Var.folder_id);
    formData.set("prj_id",params.prj_info.id);
    formData.set("pageData",JSON.stringify(pageData));
    let local = $("#drn_local_file_txt").text();
    formData.set("rfile_nm",local);
    formData.set("user_id",session_user_id);
    formData.set("action","A");
    
    
    $.ajax({
	    url: 'drnUpLine.do',
	    enctype: 'multipart/form-data',  
	    type: 'POST',
	    data:formData,          
        processData: false,    
        contentType: false,      
        cache: false,           
        beforeSend:function(){
		   $('body').prepend(loading);
		},
		complete:function(){
		   $('#loading').remove();
		 },
	    success: function onData (data) {
	    	if(data.model.status == "SUCCESS"){
	    		alert("결재 상신");
	    		if(opener.getPrjDocumentIndexList)
	    			opener.getPrjDocumentIndexList();
	    		// 임시저장된 파일을 결재 상신하는 경우를 위함
	    		if(opener.getDrnList)
	    			opener.getDrnList();
	    		
	    		// opener.treeRefresh();
	    		
	    		close();
	    		// $(".pop_collaboW").dialog("close");
	    		
	    	}else if(data.model.error_msg){
	    		alert(data.model.error_msg);
	    	}else{
	    		alert("Failed to save.");
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	}); 
}

/**
 * 페이지의 정보들을 데이터화
 * @returns
 */
function getPageData(){
	
	if(!drnUp_Var) return;
	
	// 1) input박스 영역의 값들을 데이터화
	let inputs = $(".drn_inputs");
	let drnInfoVarNm = ["drn_title","drn_no","prj_no","prj_nm","date","doc_no","status","doc_title","local","chksheet_nm"];
	let drnInfo = {};
	
	
	for(let i=0;i<inputs.length;i++)
		drnInfo[drnInfoVarNm[i]] = inputs[i].value;
	
	let tmpDate = drnUp_Var.inputs[4].value; 
	drnInfo.date =  tmpDate.substr(0,4) + tmpDate.substr(5,2) + tmpDate.substr(8,2);
	
	
	drnInfo.signData = {};
	
	drnInfo.signData.name = JSON.stringify(drnUp_Var.reviewGrid.getData()[0]);
	drnInfo.signData.sign = JSON.stringify(drnUp_Var.reviewGrid.getData()[1]);
	drnInfo.signData.date = JSON.stringify(drnUp_Var.reviewGrid.getData()[2]);
	drnInfo.signData.design = drnUp_Var.design.user_id;
	drnInfo.signData.review = drnUp_Var.lineData.reviewer.user_id ? drnUp_Var.lineData.reviewer.user_id : "";
	drnInfo.signData.approve = drnUp_Var.lineData.approver.user_id ? drnUp_Var.lineData.approver.user_id : "";
	drnInfo.signData.approval_id = drnUp_Var.approval_id;
	
	let distributerArr = drnUp_Var.lineData.distributer ? drnUp_Var.lineData.distributer : [];
	let distributer = "";
	for(let i=0 ; i<distributerArr.length ; i++)
		distributer += distributerArr[i].user_id + "@";
	distributer = distributer.substr(0,distributer.length-1);
	drnInfo.signData.distributer = distributer;
	
	let dccArr = drnUp_Var.lineData.dcc ? drnUp_Var.lineData.dcc : [];
	let dcc = "";
	for(let i=0 ; i<dccArr.length ; i++)
		dcc += dccArr[i].user_id + "@";
	dcc = dcc.substr(0,dcc.length-1);
	drnInfo.signData.dcc = dcc;
	
	drnInfo.signData = JSON.stringify(drnInfo.signData);
	drnInfo.discip_code_id = drnUp_Var.discip.code_id;
	
	drnInfo.docData = [];
	let docData = drnUp_Var.grid[0].getData();
	for(let i=0;i<docData.length;i++){
		let doc = docData[i];
		drnInfo.docData.push(JSON.stringify(doc));
	}
		
		
	drnInfo.folder_id = drnUp_Var.folder_id
	
	
	let sheetData = drnUp_Var.grid[1].getData();

	drnInfo.sheetArr = [];
	drnInfo.sheetData = JSON.stringify(sheetData);
	
	
	for(let i=0;i<sheetData.length;i++){
		let tmp = sheetData[i];
		drnInfo.sheetArr.push(JSON.stringify(tmp));
	}
	
	drnInfo.check_sheet_id = drnUp_Var.sheet_id;
	
	drnInfo.review_conclusion =  $("#drn_review_conclusion").val();
	
	if(drnUp_Var.drn_id) drnInfo.drn_id = drnUp_Var.drn_id;
	
	
	return drnInfo;
	
	
	
	
}

function test(){
let pageData = getPageData();
	
	// 파일 체크
	let form = $('#drnUploadForm')[0];
    let formData = new FormData(form);
    // 프로젝트 id form 데이터에 추가    
    formData.set("folder_id",drnUp_Var.folder_id);
    formData.set("prj_id",params.prj_info.id);
    formData.set("user_id",session_user_id);
    formData.set("pageData",JSON.stringify(pageData));
    let action = "C";
    
    formData.set("action",action);
    
	$.ajax({
	    url: 'drnUpLine.do',
	    enctype: 'multipart/form-data',  
	    type: 'POST',
	    data:formData,          
        processData: false,    
        contentType: false,      
        cache: false,                  
	    success: function onData (data) {
	    	if(data.model.status == "SUCCESS"){
	    		alert("승인");
	    		close();
	    		// $(".pop_collaboW").dialog("close");
	    		
	    		
	    	}else{
	    		alert("저장 실패");
	    	}
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

/**
 * DRN 승인 버튼
 * @returns
 */
function drn_accept(){
	
	let reason = "";
	
	getComment().then(function(comment){
		reason = comment;
		let pageData = getPageData();
		
		// 파일 체크
		let form = $('#drnUploadForm')[0];
	    let formData = new FormData(form);
	    // 프로젝트 id form 데이터에 추가    
	    formData.set("folder_id",drnUp_Var.folder_id);
	    formData.set("prj_id",params.prj_info.id);
	    // console.log(session_user_id);
	    formData.set("user_id",session_user_id);    
	    formData.set("pageData",JSON.stringify(pageData));
	    formData.set("comment",reason);
	    
	    let action = "P";
		// 점검항목 체크여부 확인
		let tmp; 
		
	    switch(drnUp_Var.pageData.drnInfo.drn_approval_status){
	    	case "A": // drn상신 -> dcc 승인
	    		action = "E";
	    		break;
	    	case "E": // review -> 승인 
	    		action = "1";
	    		tmp= $(".reviewed_select ");
	    		for(let i=0;i<tmp.length;i++){
	    			  if(tmp[i].value == ''){
	    				  alert("There is unchecked value");
	    				  return;
	    			  }
	    		}
	    		break;
	    	case "1": // approval -> 승인
	    		action = "C";
	    		tmp= $(".approved_select");
	    		for(let i=0;i<tmp.length;i++){
	    			  if(tmp[i].value == ''){
	    				  alert("There is unchecked value");
	    				  return;
	    			  }
	    		}
	    		break;
	    }	
	    
	    formData.set("action",action);
	    
		$.ajax({
		    url: 'drnUpLine.do',
		    enctype: 'multipart/form-data',  
		    type: 'POST',
		    data:formData,          
	        processData: false,    
	        contentType: false,      
	        cache: false,           
	        beforeSend:function(){
	          $('body').prepend(loading);
			},
			complete:function(){
			  $('#loading').remove();
			 },
		    success: function onData (data) {
		    	if(data.model.status == "SUCCESS"){
		    		alert("ACCEPT COMPLETE");
		    		// $(".pop_collaboW").dialog("close");
		    		if(opener.getDrnList) opener.getDrnList();
		    		close();


		    		// $(opener.location).attr("href", "javascript:treeRefresh();");
		    	}else{
		    		if(data.model.err_msg)
		    			alert(data.model.err_msg);
		    		
		    		else alert("Process failed");
		    	}
		    	
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}).catch(function() {
		alert("If you don't leave a comment, can't approve it.");
		return false;
	});
	
	
	
	
}

/**
 * 거부 버튼
 * @returns
 */
function drn_deny(){
	
	let reason = "";
	
	getComment().then(function(comment){
		reason = comment;
		console.log(comment);
		
		let pageData = getPageData();
		
		// 파일 체크
		let form = $('#drnUploadForm')[0];
	    let formData = new FormData(form);
	    // 프로젝트 id form 데이터에 추가    
	    formData.set("folder_id",drnUp_Var.folder_id);
	    formData.set("prj_id",params.prj_info.id);
	    formData.set("pageData",JSON.stringify(pageData));
	    formData.set("user_id",session_user_id);
	    formData.set("action","R");
	    formData.set("rejectReason",reason);
	    
		$.ajax({
		    url: 'drnUpLine.do',
		    enctype: 'multipart/form-data',  
		    type: 'POST',
		    data:formData,          
	        processData: false,    
	        contentType: false,      
	        cache: false,                  
		    success: function onData (data) {
		    	if(data.model.status == "SUCCESS"){
		    		alert("반려");
		    		// opener.getPrjDocumentIndexList();
		    		opener.getFolderList();
		    		
		    		if(opener.getDrnList)
		    			opener.getDrnList();
		    		close();
		    	}else{
		    		alert("반려 실패");
		    	}
		    	
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		
		
		
		
		
	}).catch(function() {
		alert("If you don't leave a reason for rejection, can't reject it.");
		return false;
	});
	
	/*
	let reason = prompt("반려 사유를 입력하세요");
	
	if(!reason) {
		alert("반려 사유를 입력하지 않으면 반려할 수 없습니다.");
		return false;
	}
	
	
	let pageData = getPageData();
	
	// 파일 체크
	let form = $('#drnUploadForm')[0];
    let formData = new FormData(form);
    // 프로젝트 id form 데이터에 추가    
    formData.set("folder_id",drnUp_Var.folder_id);
    formData.set("prj_id",params.prj_info.id);
    formData.set("pageData",JSON.stringify(pageData));
    formData.set("user_id",session_user_id);
    formData.set("action","R");
    formData.set("rejectReason",reason);
    
	$.ajax({
	    url: 'drnUpLine.do',
	    enctype: 'multipart/form-data',  
	    type: 'POST',
	    data:formData,          
        processData: false,    
        contentType: false,      
        cache: false,           
        timeout: 600000,       
	    success: function onData (data) {
	    	if(data.model.status == "SUCCESS"){
	    		alert("반려");
	    		// opener.getPrjDocumentIndexList();
	    		opener.getFolderList();
	    		close();
	    	}else{
	    		alert("반려 실패");
	    	}
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	*/
	
}
/**
 * Print 버튼
 * @returns
 */
function drn_print(){
	// DB에 저장되어 있는 DRN인지 확인
	if(drnUp_Var.drn_id){ // 기안이 올라간 경우
		
		var url = '/drnPrint.do';
		let drnForm = document.createElement("form");
		drnForm.id = "drnForm";
	    var target = 'DRN_print';
	    
	    window.open("", target, "width=1000,height=950,resizable=0,toolbar=yes,menubar=yes,location=yes");

	    $(drnForm).attr('action', url);
	    $(drnForm).attr('target', target); // window.open 타이틀과 매칭 되어야함
	    $(drnForm).attr('method', 'POST');
	    
    	
	    
	    drnForm.append(genInputTag("prj_id",params.prj_info.id,false));
	    drnForm.append(genInputTag("drn_id",drnUp_Var.drn_id,false));
	    
	    
	    
	    document.body.appendChild(drnForm);
	    
	    
	    drnForm.submit();
	    $("#drnForm").remove();
		
		
		
		
//		$.ajax({
//		    url: 'drnPrint.do',
//		    type: 'POST',
//		    data:{
//		    	prj_id:params.prj_info.id,
//		    	drn_id:drnUp_Var.drn_id
//		    },
//		    success: function onData (data) {
//		    	// console.log(data);
//		    	
//		    	
//		    	
//		    	window.open(data);
//		    },
//		    error: function onError (error) {
//		        console.error(error);
//		    }
//		});
	}else{
		html2canvas($('#pdfDrnDiv')[0]).then(function(canvas) { // 저장 영역 div id
			
		    // 캔버스를 이미지로 변환
		    var imgData = canvas.toDataURL('image/png');
			     
		    var imgWidth = 190; // 이미지 가로 길이(mm) / A4 기준 210mm
		    var pageHeight = imgWidth * 1.414;  // 출력 페이지 세로 길이 계산 A4 기준
		    var imgHeight = canvas.height * imgWidth / canvas.width;
		    var heightLeft = imgHeight;
		    var margin = 10; // 출력 페이지 여백설정
		    var doc = new jsPDF('p', 'mm');
		    var position = 0;
		       
		    // 첫 페이지 출력
		    doc.addImage(imgData, 'PNG', margin, position, imgWidth, imgHeight);
		    heightLeft -= pageHeight;
		         
		    // 한 페이지 이상일 경우 루프 돌면서 출력
		    while (heightLeft >= 20) {
		        position = heightLeft - imgHeight;
		        doc.addPage();
		        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
		        heightLeft -= pageHeight;
		    }
		 
		    // 파일 저장
		    doc.save('DRN.pdf');
	 
	    });
	}
	
	 
}


/**
 * 시스템 버튼
 * [시스템으로부터 문서파일을 선택하는 기능]
 * @returns
 */
function drn_systemBtn(){
	drnUp_Var.docSelTree = drn_getDocSelTree();
	drnUp_Var.docSelTree.bind('click.jstree', function(event) { // 클릭 이벤트 바인딩
    	drn_docSelTreeMenuActive(event)
    });
	
	let doc_datas = drnUp_Var.grid[0].getData();
	
	drnUp_Var.docSelTree.bind('ready.jstree',function(event){
		
		$('#drnFile_docSelTree').jstree("select_node",drnUp_Var.folder_id);
		popup_folder_id = drnUp_Var.folder_id;
		popup_folder_path = $('#drnFile_docSelTree').jstree().get_path(drnUp_Var.folder_id, '/');
		$('#drnFile_docsel_current_folder_path').val(popup_folder_path);
		getPrjDocumentIndexList("drnSystemFile",popup_folder_id);
		$('#drnFile_docsel_select_rev_version').attr('onchange','getPrjDocumentIndexList("drnSystemFile",'+popup_folder_id+')');
		$('#drnFile_docsel_unread_yn').attr('onchange','getPrjDocumentIndexList("drnSystemFile",'+popup_folder_id+')');

		
		
	});
	
	
	if(drnUp_Var.folder_id)
		drn_FirstdocSelTreeMenuActive(drnUp_Var.folder_id);
	$("#drn_fileDialog").dialog("open");

}


/**
 * 폴더리스트에 DCC 타입의 폴더만 추가해준다.
 * @param folderList
 * @returns
 */
function drn_dccFolder(folderList){
	var folder = [];
	for(let i=0;i<folderList.length;i++)
		if(folderList[i].type === "DCC")
			folder.push(folderList[i]);	
	
	return folder;
	
}
/**
 * 시스템의 문서선택 dialog에서
 * 폴더의 트리를 구성할 정보를 불러오는 함수
 * @returns
 */
function drn_getDocSelTree(){
	folderList = getFolderList();
	folderList = drn_dccFolder(folderList);
	let tree = $('#drnFile_docSelTree').jstree({
	     "core" : {
	    	 'data' : folderList,
	         "check_callback" : true
	       }
	    });
	/*.on('click.jstree', function(event) { // 클릭 이벤트 바인딩
	    	drn_docSelTreeMenuActive(event)} ); */
	
	//$('#drnFile_docSelTree').jstree(true).refresh();
	
	return tree;
}

/**
 * 트리에서 폴더를 선택했을 때의 처리
 * @param event
 * @returns
 */
function drn_docSelTreeMenuActive(event){

	var node = $(event.target).closest("li");
	let id = node[0].id; //id 가져오기
	let text = node[0].querySelector("a").childNodes[1].nodeValue;
	
	// 폴더메뉴의 타입
	var type;
	for (let key in folderList) {
		  const value = folderList[key]
		  if(value.id === parseInt(id)){
			  type = value.type;
			  }
		  }

	if(type === "DCC") {
		popup_folder_id = id;
		popup_folder_path = $('#drnFile_docSelTree').jstree().get_path(node[0], '/');
		$('#drnFile_docsel_current_folder_path').val(popup_folder_path);
		getPrjDocumentIndexList("drnSystemFile",popup_folder_id);
		$('#drnFile_docsel_select_rev_version').attr('onchange','getPrjDocumentIndexList("drnSystemFile",'+popup_folder_id+')');
		$('#drnFile_docsel_unread_yn').attr('onchange','getPrjDocumentIndexList("drnSystemFile",'+popup_folder_id+')');
	}
		
}
/**
 * 선택된 doc이 있을 경우,
 * 폴더 선택 트리에서 해당 폴더로 이동되어지도록 설정
 * @param id
 * @returns
 */
function drn_FirstdocSelTreeMenuActive(id){
	
	
	popup_folder_id = id;
	// $('#drnFile_docSelTree').jstree("select_node",id)
	
	popup_folder_path = $('#drnFile_docSelTree').jstree().get_path(id, '/');
	$('#drnFile_docsel_current_folder_path').val(popup_folder_path);
	
	getPrjDocumentIndexList("drnSystemFile",popup_folder_id);
	$('#drnFile_docsel_select_rev_version').attr('onchange','getPrjDocumentIndexList("drnSystemFile",'+popup_folder_id+')');
	$('#drnFile_docsel_unread_yn').attr('onchange','getPrjDocumentIndexList("drnSystemFile",'+popup_folder_id+')');
	

		
	let tmpDocList = drnUp_Var.docData;
	let grid = documentListTable.getData();
	let docListIdx = [];
	
	for(let i=0;i<tmpDocList.length;i++){
		let chk = tmpDocList[i];
		for(let j=0;j<grid.length;j++){
			let tmpBean = grid[j].bean
			if(tmpBean.doc_id == chk.doc_id)
				docListIdx.push(j);
		}
	}
	
	documentListTable.on("tableBuilt",function(){
		for(let i=0;i<docListIdx.length;i++){
			let row = documentListTable.getRowFromPosition(i);
			documentListTable.selectRow(row);
		}
	});
	
	
}

function drnFileDownload(){
	var selectedArray = drnUp_Var.grid[0].getData();
	var downloadFiles = '';
	for(let i=0;i<selectedArray.length;i++){
		downloadFiles += selectedArray[i].doc_id +'%%'+ selectedArray[i].rev_id + '@@';
	}
	downloadFiles = downloadFiles.slice(0,-2);
	let renameYN='Y';
	let fileRenameSet = $('#DRN_no_write').val();
	location.href='/drnFileDownload.do?prj_id='+encodeURIComponent(params.prj_info.id)+'&drn_id='+drnUp_Var.drn_id+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
}


/**
 * 문서선택 완료버튼
 * @returns
 */
function drn_systemFileSelect(){
	let thisFolder = current_folder_id;
	let selectedDoc = documentListTable.getSelectedData();
	
	let outDocList = [];
	let isContainLock = false;
	for(let i=0;i<selectedDoc.length;i++){
		// console.log(selectedDoc[i]);
		let tmpBean = selectedDoc[i].bean;
		tmpBean.index = i;
		tmpBean.revision = selectedDoc[i].Revision;
		outDocList.push(tmpBean);
		if(tmpBean.lock_yn == 'Y') isContainLock = true;
	}
	
	if(isContainLock) alert("잠겨있는 문서는 포함할 수 없습니다.");
	// 잠긴 파일 목록에서 제외
	outDocList = outDocList.filter( (element) => element.lock_yn !== 'Y' );
	
	
	// 기존 존재하는 파일은 제외 및 추가
	drnUp_Var.docData = outDocList;

	
	let addDocList = [];

	for(let i=0;i<outDocList.length;i++){
		if(!isInFileGrid(outDocList[i].doc_id))
			addDocList.push(outDocList[i]);
	}
	
	
	// 폴더의 인덱스 확인
	let idx = folderList.findIndex(i=>i.id == thisFolder);
	
	let discipObj = {};
	discipObj.code = folderList[idx].discipline_code;
	discipObj.display = folderList[idx].discipline_display;
	discipObj.code_id = folderList[idx].discipline_code_id;
	
	if(!discipObj.code) {
		alert("Can't submit this document to DRN");
		return;
	}
	if(discipObj.code_id != drnUp_Var.discip.code_id){
		alert("Can't submit document assigned different discipline");
		return;
	}
	
	
	let validationChk = "";
	for(let i=0;i<addDocList.length;i++)
		validationChk += addDocList[i].doc_id + "@" + addDocList[i].rev_id + ",";
	
	validationChk = validationChk.substr(0,validationChk.length-1);
	
	let chk_doc_type = folderList[idx].doc_type;
	
	let err = "";
	$.ajax({
	    url: 'drnValidationCheck.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:params.prj_info.id,
	    	doc_type:chk_doc_type,
	    	jsonData:validationChk
	    },
	    success: function onData (data) {
	    	validationChk = data.model.check;
	    	if(data.model.check =="NO"){
	    		let tmp = data.model.error_doc
	    		let msg = data.model.error_msg
	    		err = msg;
	    	}
	    		
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	if(validationChk == "NO") {
		alert(err);
		return;
	}
	
	
	
	/*
	drnUp_Var.discip = discipObj;
    // 체크시트 discip 매핑
    let chk = drnUp_Var.grid[1].getRows();
    for(let i=0;i<chk.length;i++){
    	let tmp = chk[i].getCell("discipline_code_id").getValue();
    	   for(let j=0;j<tmp.length;j++)
    	       tmp[j] = drnUp_Var.discip.code;
    	chk[i].getCell("discipline_code_id").setValue(tmp);
    }
	
 // Status STEP 필드 추가
	$.ajax({
	    url: 'getStatus.do',
	    type: 'POST',
	    data:{
	    	prj_id:params.prj_info.id,
	    	folder_id:thisFolder
	    },
	    success: function onData (data) {
	    	if(data[0] != 'FAIL'){
	    		setStatus(data[1]);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
    */
	
	if(addDocList.length>0) add_FileGrid(addDocList);
	
	
	
	$("#drn_fileDialog").dialog("close");
	
}

function isInFileGrid(doc_id,rev_id){
	let fileGridData = drnUp_Var.grid[0].getData();
	
	for(let i=0;i<fileGridData.length;i++){
		if(doc_id == fileGridData[i].doc_id)
			return true;
	}
	return false;
}
/**
 * PC로부터 문서를 불러오는 버튼
 * @returns
 */
function drn_localBtn(){
	document.getElementsByName("drnLocalFile")[0].click();
}

/**
 * 로컬파일을 form에 매핑
 * @param obj
 * @returns
 */
function drn_LocalFile(obj){
	
	$("#localFileTd")[0].innerHTML = '<a id="drn_local_file_txt" onclick="localFileDown()">' + obj.files[0].name + '</>';
}

/**
 * 문서 목록에서 선택된 아이템 삭제
 * @returns
 */
function drn_fileDel(){
	
	let org = drnUp_Var.grid[0].getData();
	let rows = drnUp_Var.grid[0].getSelectedRows();
	
	if(org.length == rows.length){
		alert("최소한 한개의 문서는 있어야 합니다.");
		return;
	}
		
	drnUp_Var.grid[0].deleteRow(rows);
	chg_drnInput(drnUp_Var.grid[0].getData());
}

function drn_fileDel2(){
	
	$("#localFileTd")[0].innerText = '';
	$("input[name='drnLocalFile'")[0].value = '';
	
}


/**
 * 일괄적용 버튼
 * @param idx
 * @returns
 */
function drn_setAll(idx){
	
	let val = $("#drn_allSelect").val();
	if(val === '선택') return;
	
	 if (confirm("Are you sure to set this value to all??") == false){
	     return false;
	 }
	 
//	 if (confirm("Are you sure to set this value to all??") == true){
//	     
//	 }else{   //취소
//	     return false;
//	 }
	
	let gridData = drnUp_Var.grid[1].getData();
	
	// 그리드 데이터 업데이트
	for(let i=0;i<gridData.length;i++){
		let tmp = gridData[i]
		let arr = [];
		for(let j=0;j<tmp.items_cnt;j++) arr.push(val);
		if(idx == 0) 
			drnUp_Var.grid[1].getRow(i).getCell("design_check_status").setValue(arr);
		else if(idx == 1)
			drnUp_Var.grid[1].getRow(i).getCell("reviewed_check_status").setValue(arr);
		else if(idx == 2)
			drnUp_Var.grid[1].getRow(i).getCell("approved_check_status").setValue(arr);	
	}
		
	
	switch(idx){
		case 0:
			let design = $(".design_select");
			for(let i=0;i<design.length;i++) design[i].value = val;
			break;
		case 1:
			let reviewed = $(".reviewed_select");
			for(let i=0;i<reviewed.length;i++) reviewed[i].value = val;
			break;
		case 2:
			let approved = $(".approved_select");
			for(let i=0;i<approved.length;i++) approved[i].value = val;
			break;
	}
	
	
}

function chg_drnInput(data){
	
	if(data == null) return;
	let docData = data
	console.log("chg",docData);
	
	// date
	let date = genDateStr().substr(0,8);
	if(params.pageData){
		date = params.pageData.drnInfo.reg_date.substr(0,8);
		if(params.pageData.drnInfo.drn_approval_status == "C") {
			date = params.pageData.drnInfo.mod_date ? 
					params.pageData.drnInfo.mod_date.substr(0,8) : params.pageData.drnInfo.reg_date.substr(0,8);
		}
	}
		
	/*
	$("#Date_write").datepicker({
		dateFormat:"yy-mm-dd",
		nextText: '다음 달', 
		prevText: '이전 달', 
		dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'], 
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], 
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],		
	});
	*/
	let dateStr = date.substr(0,4) + "-" +date.substr(4,2) + "-" +date.substr(6,2);
	drnUp_Var.inputs[4].value = dateStr;
	
	$("#Date_write").change(function(){
		let drnDateStr = drnUp_Var.inputs[4].value;
		drnDateStr = drnDateStr.substr(0,4)+drnDateStr.substr(5,2)+drnDateStr.substr(8,2); 
		drnUp_Var.inputs[1].value = params.prj_info.no + "-" + drnUp_Var.discip.code + "-" + drnDateStr + "-";
	});

	
	// DRN제목
	drnUp_Var.inputs[0].value =
		(docData.length > 1) ?
		(docData[0].title + "외 " + (docData.length-1) + "건")  : docData[0].title; 
		
	// DRN NO
	let drnDateStr = drnUp_Var.inputs[4].value;
	drnDateStr = drnDateStr.substr(0,4)+drnDateStr.substr(5,2)+drnDateStr.substr(8,2); 
	if(!drnUp_Var.pageData)
		drnUp_Var.inputs[1].value = params.prj_info.no + "-" + drnUp_Var.discip.code + "-" + drnDateStr + "-";
	
	// Project no
	drnUp_Var.inputs[2].value = params.prj_info.no;
	// Project Name
	drnUp_Var.inputs[3].value = params.prj_info.nm;
	
	
	// doc_no (rev_no)
	
	let doc_no_str = "";
	for(let i=0;i<docData.length;i++) 
		doc_no_str += docData[i].doc_no + " (REV:" + docData[i].revision +"), ";
	doc_no_str = doc_no_str.substr(0,doc_no_str.length-2);
	drnUp_Var.inputs[5].value = doc_no_str;
	// status
	if(!drnUp_Var.pageData)
		drnUp_Var.inputs[6].value = "";
	// doc title
	let title_str = "";
	for(let i=0;i<docData.length;i++) 
		title_str += docData[i].title + "("+ (i+1) +"/"+ docData.length +"), ";
	title_str = title_str.substr(0,title_str.length-2);
	
	// 한개의 데이터인 경우 위의 로직과 무관하므로 doc_title로 수정
	if(docData.length == 1) title_str = docData[0].title;
	
	
	drnUp_Var.inputs[7].value = title_str;
}

/**
 * 상단의 drn input 정보들 값 세팅
 * @returns
 */
function set_drnInput(){
	if(!drnUp_Var.docData) return;
	
	let docData = drnUp_Var.docData;
	
	// date
	let date = genDateStr().substr(0,8);
	if(params.pageData){
		date = params.pageData.drnInfo.reg_date.substr(0,8);
		if(params.pageData.drnInfo.drn_approval_status == "C") {
			date = params.pageData.drnInfo.mod_date ? 
					params.pageData.drnInfo.mod_date.substr(0,8) : params.pageData.drnInfo.reg_date.substr(0,8);
		}
	}
	/*
	$("#Date_write").datepicker({
		dateFormat:"yy-mm-dd",
		nextText: '다음 달', 
		prevText: '이전 달', 
		dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'], 
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], 
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],		
	});
	*/
	let dateStr = date.substr(0,4) + "-" +date.substr(4,2) + "-" +date.substr(6,2);
	drnUp_Var.inputs[4].value = dateStr;
	
	$("#Date_write").change(function(){
		let drnDateStr = drnUp_Var.inputs[4].value;
		drnDateStr = drnDateStr.substr(0,4)+drnDateStr.substr(5,2)+drnDateStr.substr(8,2); 
		drnUp_Var.inputs[1].value = params.prj_info.no + "-" + drnUp_Var.discip.code + "-" + drnDateStr + "-";
	});

	
	// DRN제목
	drnUp_Var.inputs[0].value =
		(docData.length > 1) ?
		(docData[0].title + "외 " + (docData.length-1) + "건")  : docData[0].title; 
		
	// DRN NO
	let drnDateStr = drnUp_Var.inputs[4].value;
	drnDateStr = drnDateStr.substr(0,4)+drnDateStr.substr(5,2)+drnDateStr.substr(8,2); 
	if(!drnUp_Var.pageData)
		drnUp_Var.inputs[1].value = params.prj_info.no + "-" + drnUp_Var.discip.code + "-" + drnDateStr + "-";
	
	// Project no
	drnUp_Var.inputs[2].value = params.prj_info.no;
	// Project Name
	drnUp_Var.inputs[3].value = params.prj_info.nm;
	
	
	// doc_no (rev_no)
	
	let doc_no_str = "";
	for(let i=0;i<docData.length;i++) 
		doc_no_str += drnUp_Var.docData[i].doc_no + " (REV:" + drnUp_Var.docData[i].revision +"), ";
	doc_no_str = doc_no_str.substr(0,doc_no_str.length-2);
	drnUp_Var.inputs[5].value = doc_no_str;
	// status
	if(!drnUp_Var.pageData)
		drnUp_Var.inputs[6].value = "";
	/*
	// doc title
	let title_str = "";
	for(let i=0;i<docData.length;i++) 
		title_str += drnUp_Var.docData[i].title + ", ";
	title_str = title_str.substr(0,title_str.length-2);
	drnUp_Var.inputs[7].value = title_str;
	*/
	// doc title
	
	let title_str = "";
	for(let i=0;i<docData.length;i++) 
		title_str += docData[i].title + "("+ (i+1) +"/"+ docData.length +"), ";
	title_str = title_str.substr(0,title_str.length-2);
	
	// 한개의 데이터인 경우 위의 로직과 무관하므로 doc_title로 수정
	if(docData.length == 1) title_str = docData[0].title;
	
	if(drnUp_Var.drn_id){
		drnUp_Var.inputs[7].value = drnUp_Var.pageData.drnInfo.doc_title;	
	}
	else{
		drnUp_Var.inputs[7].value = title_str;
	}
	
	
	
	// 로컬파일
	$("#localFileTd").text("");
	if(drnUp_Var.drn_id)
	$.ajax({
	    url: 'getLocalFile.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:params.prj_info.id,
	    	drn_id:drnUp_Var.drn_id
	    },
	    success: function onData (data) {
	    	if(data.model.local_file_chk == 'OK'){
	    		let local_file = data.model.local_file
		    	//$("#localFileTd").text(local_file.rfile_nm);
	    		$("#localFileTd").html('<a id="drn_local_file_txt" onclick="localFileDown()">' + local_file.rfile_nm + '</>');
	    		
	    	}
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

/**
 * 서명 필드를 채워준다.
 * 필드값은 왼쪽부터 순서대로 index번호
 * @param field
 * @param data
 * @returns
 */
function set_drnSigns(field,rowId,data){
	let gridRow = drnUp_Var.reviewGrid.getRow(rowId);
	if(!gridRow) return;	
	
	
	gridRow.getCell(field).setValue(data);
	if(drnUp_Var.as == "R"){
		if(rowId == 'sign')
			gridRow.getCell(field).setValue('<p style="font-size: 18px; color: red; font-weight: bold;">REJECTED</p>');
	}
	
}

/**
 * 문서파일을 읽어들여와서 그리드 셋팅
 * @param docData
 * @returns
 */
function set_FileGrid(docData){
	drnUp_Var.grid[0].setData(docData);
	drnUp_Var.folder_id = docData[0].folder_id; 
	set_drnInput();
	
}

function add_FileGrid(docData){
	drnUp_Var.grid[0].addData(docData);
	drnUp_Var.folder_id = docData[0].folder_id; 
	//set_drnInput();
	
	chg_drnInput(drnUp_Var.grid[0].getData());
	
}

//체크시트 가져오기 버튼
function getChkSheet(){
	$("#drnChkSheet_Pop").dialog("open");
}


/**
 * 하단의 서명 이름부분 세팅
 * @returns
 */
function setBottom(){
	// set_drnSigns("design","name",drnUp_Var.lineData.design.user_kor_nm);
	set_drnSigns("reviewed","name",drnUp_Var.lineData.reviewer.user_kor_nm);
	set_drnSigns("approved","name",drnUp_Var.lineData.approver.user_kor_nm);
}






// 결재선 관련

/**
 * 적용버튼
 * @returns
 */
function setLine_apply(){
	let result = drnUp_Var.grid[3].getData();
    if(result[1].user_kor_nm == '' || result[2].user_kor_nm == ''){
    	alert("Approver is not designated.");
    	return;
    }
    
    // 2022-05-03 : 이태돈 부장님 요청에 의해 designer가 approver,reviewer로 설정가능하도록 열어둠
    /*
    if(result[1].user_id == drnUp_Var.design.user_id || result[2].user_id == drnUp_Var.design.user_id){
    	alert("DRN drafter can't be designated as approver");
    	return;
    }
    */
    
    /*
    // 결재자를 동일한 인물로 설정 불가하게 하는 부분
    if(result[1].user_id == result[2].user_id){
    	alert("Approver can't be designated as the same person as the reviewer.");
    	return;
    }
    */
    
	// setBottom(result);
    // 리뷰 설정
    $.ajax({
	    url: 'searchUsersVOwithCode.do',
	    type: 'POST',
	    data:{
	    	user_id:result[1].user_id
	    },
	    success: function onData (data) {
	    	let review = data[0].user_kor_nm + " " + data[0].code_nm;
	    	review = review.trim();
	    	set_drnSigns("reviewed","name",review);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
    // 결재자 설정
	
	$.ajax({
	    url: 'searchUsersVOwithCode.do',
	    type: 'POST',
	    data:{
	    	user_id:result[2].user_id
	    },
	    success: function onData (data) {
	    	let approve = data[0].user_kor_nm + " " + data[0].code_nm;
	    	approve = approve.trim();
	    	set_drnSigns("approved","name",approve);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	

	drnUp_Var.lineData.distributer = drnUp_Var.grid[4].getData();
	
	
	$(".pop_mngAppr").dialog("close");
}
/**
 * 결재선 지정 모달창 닫기버튼
 * @returns
 */
function setLine_close(){
	$(".pop_mngAppr").dialog("close");
}

/**
 * 결재선 지정 모달 트리부
 * @returns
 */
function setLineTree(){
	let orgList = getOrganizationList_DRN();
	
	var userRegGrouptree = $('#mngAppr_userGroupTree').jstree({
	     "core" : {
	    	 'data' : orgList,
	    	 "check_callback" : true
	       }
	});


	userRegGrouptree.bind('click.jstree', function(event) {
		let node = $(event.target).closest("li");
		let id = node[0].id;
		let text = node[0].querySelector("a").childNodes[1].nodeValue;
		findGroup(id,text);
	});
	
	$('#mngAppr_userGroupTree').jstree(true).refresh();
	
	
}

function findGroup(id,text){
	// console.log(id,text);
	let memberList = drnUp_Var.lineData.prjMemberAll;
	let result = [];
	let index=1;
	
	for(let i=0 ; i < memberList.length ; i++){
		if(memberList[i].org_path != null){
			let orgPath = String(memberList[i].org_path);
			orgPath = orgPath.split(">");
			if(memberList[i].org_id == id || orgPath.includes(id)){
			    memberList[i].index = index++;
			    result.push(memberList[i]);	
			}
			
		}
	}
	
	drnUp_Var.grid[2].setData(result);
	
}

function searchAll(){
	$.ajax({
	    url: 'searchDrnUser.do',
	    type: 'POST',
	    data:{
	    	searchKeyword:"",
	    	prj_id:params.prj_info.id
	    },
	    success: function onData (data) {
	    	$(".pop_mngAppr").dialog("open");

	    	// user_id로 중복인물 제거 및 인덱스 부여
	    	
	    	let idx = 1;
	    	let member = data[0].reduce(function(acc, current) {
	    		  if (acc.findIndex(({ user_id }) => user_id === current.user_id) === -1) {
	    			  current.index = idx++;
	    		    acc.push(current);
	    		  }
	    		  return acc;
	    		}, []);
	    	
	    	
	    	
	    	drnUp_Var.lineData.prjMemberAll = member;
	    	
	    	drnUp_Var.grid[2].setData(drnUp_Var.lineData.prjMemberAll);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function drnUserSearch(){
	let searchKeyword = $('#drn_nm_search').val();
	if(searchKeyword == '') return;

	$.ajax({
		    url: 'searchDrnUser.do',
		    type: 'POST',
		    data:{
		    	searchKeyword:searchKeyword,
		    	prj_id:params.prj_info.id
		    },
		    success: function onData (data) {
		    	let result = [];
                let index = 1;
		    	for(let i=0;i<data[0].length;i++){
		    		let tmp = data[0][i];
		    		let isIn = false;
		    		for(let j=0; j<result.length;j++){
		    			if(result[j].user_id == tmp.user_id){
                            isIn = true;
                            break;
		    			}
		    		}
		    		if(!isIn) {
		    			tmp.index = index++;
		    			result.push(tmp);
		    		}
		    	}
		    	    
		    	drnUp_Var.grid[2].setData(result);
		    	drnUp_Var.grid[2].redraw();
		    	// console.log(result);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});

}


function getOrganizationList_DRN() {
	let organizations = [];
	 $.ajax({
			type: 'POST',
			url: 'getOrganizationList.do',
			async: false,
			data:{
				userMngPage : 'ADMIN'
			},
			success: function(data) {
				var tmp_organization_List = data[0];
				
				for(var i=0 ; i < tmp_organization_List.length; ++i){
					var tmp_Node = tmp_organization_List[i];
					if(tmp_Node.org_type != 'H') continue;
					
					var	input_Organization = { "id" : tmp_Node.org_id, "parent" : tmp_Node.org_parent_id, 'text' : tmp_Node.org_nm, 'type' : tmp_Node.org_type ,'folder_path':tmp_Node.org_path};

					if(tmp_Node.org_id === tmp_Node.org_parent_id){ // 최상위 루트
						input_Organization.parent = "#";
						input_Organization.icon = "resource/images/pinwheel.png";
						input_Organization.state =  {'opened' : true,'selected' : false};
					}
					
					organizations.push(input_Organization);
				}
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 return organizations;
}
/**
 * 결재선 지정 그리드부
 * @returns
 */
function setLineGrid(){
	let userListGrid = new Tabulator("#apprUser_Grid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		selectable:1,
		index:"index",
		height: 200,
		columns: [			
			{ title:"No",field:"index", widthGrow:1},
			{
				title: "Kor.Name",
				field: "user_kor_nm",
			    widthGrow:2
			},
			{
				title: "Department",
				field: "org_fullNm",
				widthGrow:4
			},
			{
				title: "Eng.Name",
				field: "user_eng_nm",
				widthGrow:2
			}
		],
	});
	
	let grid0 = new Tabulator("#approverGrid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		selectable:0,
		index:"id",
		height: 150,
		columns: [
			{
				title: "결재선",
				field: "status",
				headerSort:false,
				hozAlign:"center"
			},
			{
				title: "이름",
				field: "user_kor_nm",
				headerSort:false,
				hozAlign:"center",
			},
			{
				title: "아이디",
				field: "user_id",
				visible: false
			}
		],
		data:[
			{
				id:"dcc",
				status:"DCC",
				user_kor_nm:""
			},
			{
				id:"reviewer",
				status:"REVIEWER",
				user_kor_nm:""
			},
			{	
				id:"approver",
				status:"APPROVER",
				user_kor_nm:""
			}
			
		]
	});
	let grid1 = new Tabulator("#distributionLineGrid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		selectable:1,
		index:"index",
		height: 100,
		columns: [			
			{
				title: "Kor.Name",
				field: "user_kor_nm",
				headerSort:false,
			},
			{
				field: "user_id",
				visible:false
			}
		],
	});

	
	drnUp_Var.grid.push(userListGrid);
	drnUp_Var.grid.push(grid0);
	drnUp_Var.grid.push(grid1);
	
	drnUp_Var.grid[3].on("tableBuilt", function(){ // approverGrid
		// setLine_dccBtn();
		setDcc();
		
		
	});
}

function setDcc(){
	let dccUser = drnUp_Var.dccUser;
	
	let resultObj = {};
	resultObj.id = "dcc";
	resultObj.status = "DCC";
	
	let dccStr = "";
	let dccIdList = [];
	
	
	for(let i=0;i<dccUser.length;i++){
		dccStr += dccUser[i].user_kor_nm + "(" + dccUser[i].user_id + ")" + ", ";
		dccIdList.push(dccUser[i].user_id);
	}
	dccStr = dccStr.substr(0,dccStr.length-2);
	
	resultObj.user_kor_nm = dccStr;
	resultObj.dccIdList = dccIdList;
	
	drnUp_Var.grid[3].updateRow("dcc",resultObj).then(function(){
	    //run code after table has been successfuly updated
	})
	.catch(function(error){
	   // error - Fetch response object
	});
}

function setLine_addBtn(){
	let val = $("input[name='drn_apprUser_Sel']:checked").val();
	val = Number(val);
	
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[3].getRowFromPosition(val).getCell("user_kor_nm").setValue(sel.user_kor_nm);
	drnUp_Var.grid[3].getRowFromPosition(val).getCell("user_id").setValue(sel.user_id);
	switch(val){
		case 1:
			drnUp_Var.lineData.reviewer = sel;
			break;
		case 2:
			drnUp_Var.lineData.approver = sel;
			break;	
	}
	
	
}


// 검토자 선택 버튼
function setLine_reviewBtn(){
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[3].getRowFromPosition(1).getCell("user_kor_nm").setValue(sel.user_kor_nm);
	drnUp_Var.grid[3].getRowFromPosition(1).getCell("user_id").setValue(sel.user_id);
	drnUp_Var.lineData.reviewer = sel;
	
}

// 결재자 선택 버튼
function setLine_approveBtn(){
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[3].getRowFromPosition(2).getCell("user_kor_nm").setValue(sel.user_kor_nm);
	drnUp_Var.grid[3].getRowFromPosition(2).getCell("user_id").setValue(sel.user_id);
	drnUp_Var.lineData.approver = sel;
}
// 결재자 삭제 버튼
function setLine_delBtn1(){
	
	
	let val = $("input[name='drn_apprUser_Sel']:checked").val();
	val = Number(val);
	
	drnUp_Var.grid[3].getRowFromPosition(val).getCell("user_kor_nm").setValue("");
	drnUp_Var.grid[3].getRowFromPosition(val).getCell("user_id").setValue("");
	
	switch(val){
	case 1:
		drnUp_Var.lineData.reviewer = "";
		break;
	case 2:
		drnUp_Var.lineData.approver = "";
		break;	
	}
	
	
	
	/*
	let grid = drnUp_Var.grid[3];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	if(sel.id=="dcc"){
		alert("DCC cannot be removed from the list.");
		return; 
	}
	
	drnUp_Var.grid[3].getRowFromPosition(sel.id=='reviewer'?1:2).getCell("user_kor_nm").setValue("");
	drnUp_Var.grid[3].getRowFromPosition(sel.id=='reviewer'?1:2).getCell("user_id").setValue("");
	if(drnUp_Var.lineData[sel.id])
		drnUp_Var.lineData[sel.id] = "";
	
	*/
	
}


function setLine_distributeBtn(){
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	// 중복체크
	let chk = drnUp_Var.grid[4].getData();
	let idx = chk.findIndex(Item => Item.user_id == sel.user_id);
	if(idx>=0){
		alert("This user has already been added to the distribution line.");
		return;
	}
	
	drnUp_Var.grid[4].addData(sel);
	
}
function setLine_delBtn2(){
	let grid = drnUp_Var.grid[4];
	let sel = grid.getSelectedRows()[0];
	if(!sel) {
		alert("Choose the user to delete in distibution line list");
		return;
	}
	
	grid.deleteRow(sel)
}

function setLine_dccBtn(){
	let dccUser = drnUp_Var.dccUser;
	let objArr = [];
	for(let i=0;i<dccUser.length;i++){
		let obj = {};
		obj.user_kor_nm = dccUser[i].user_kor_nm;
		obj.user_id = dccUser[i].user_id;
		objArr.push(obj);
	}
	
	drnUp_Var.grid[5].setData(objArr);
	
}
function setLine_delBtn3(){
	let grid = drnUp_Var.grid[5];
	let sel = grid.getSelectedRows()[0];
	if(!sel) return;
	
	grid.deleteRow(sel)
}



function setLine_apply2(){
	let result = drnUp_Var.chk_modal_grid.getSelectedData()[0];
	
	if(result.length < 1) {
		$("#drnChkSheet_Pop").dialog("close");
		return;
	}
	
	
	$.ajax({
	    url: 'getCheckSheetList.do',
	    type: 'POST',
	    data:{
	    	prj_id:params.prj_info.id,
	    	drn_id:result.drn_id,
	    	check_sheet_id:result.check_sheet_id,
	    	check_sheet_title:result.check_sheet_title
	    },
	    success: function onData (data) {
	    	let sheetData = {};
	    	sheetData.title = data.model.check_sheet_title;
	    	sheetData.check_sheet = data.model.check_sheet;
	    	sheetData.check_sheet_items = data.model.check_sheet_items;
	    	alert("체크시트 데이터 불러오기 성공");
//	    	console.log("sheetData = "+sheetData);
//	    	console.log("sheetData.title = "+sheetData.title);
	    	setCheckSheetGrid(sheetData);
	    	$("#drnChkSheet_Pop").dialog("close");
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	// drnUp_Var.sheet_id = result.check_sheet_id;	
	
	
}

function setCheckSheetGrid(data){
	
	let sheetData = data.check_sheet_items;
	
	let sheet_rows = [];
	
	for(let i=0;i<data.check_sheet.length;i++){
		let sheet_row = {};
		sheet_row.index = i;
		sheet_row.items_no = data.check_sheet[i].items_no;
		sheet_row.items_nm = data.check_sheet[i].items_nm;
		sheet_row.items_cnt = data.check_sheet[i].itmes_cnt;
		
		sheet_row.check_items = [];
		sheet_row.discipline_code_id = [];
		sheet_row.design_check_status = [];
		sheet_row.reviewed_check_status = [];
		sheet_row.approved_check_status = [];
		sheet_row.btn = [];
		
		let tmp = sheetData[i];
		for(let j=0 ; j < sheet_row.items_cnt ;j++){
			sheet_row.check_items.push(tmp[j].check_items);
			
			sheet_row.discipline_code_id.push(tmp[j].discipline_code_id);
			sheet_row.design_check_status.push(tmp[j].design_check_status);
			sheet_row.reviewed_check_status.push(tmp[j].reviewed_check_status);
			sheet_row.approved_check_status.push(tmp[j].approved_check_status);
		}
		
		sheet_rows.push(sheet_row);
	}
	
	/*
	console.log("data.title = "+data.title);
	console.log("data.check_sheet_title = "+data.check_sheet_title);
	*/
	if(data.title != undefined) { // CHECK SHEET 가져오기 버튼으로 타이틀 값 가져왔을때
		drnUp_Var.inputs[9].value = data.title;
	}else if(data.check_sheet_title != undefined) { // DB에 저장된 타이틀 값 가져올때
		drnUp_Var.inputs[9].value = data.check_sheet_title;
	}
	
	drnUp_Var.grid[1].setData(sheet_rows);
	
		
	
	
}

function setLine_close2(){
	$("#drnChkSheet_Pop").dialog("close");
}


function checkSheetSearch(){
	let keyword = $("#drn_chk_search").val();
	
	$.ajax({
	    url: 'searchCheckSheet.do',
	    type: 'POST',
	    data:{
	    	prj_id:params.prj_info.id,
	    	check_sheet_title: '%'+keyword+'%'
	    },
	    success: function onData (data) {
	    	drnUp_Var.chk_modal_grid.setData(data.model.list);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function genDateStr(){
	let today = new Date();  
	let year = today.getFullYear(); 
	let month = today.getMonth() + 1;
	let date = today.getDate();
	let hour = today.getHours();
	let min = today.getMinutes();
	let sec = today.getSeconds();
	
	let dateString = year + 
					(month + "").padStart(2,0) +
					(date + "").padStart(2,0) +
					(hour + "").padStart(2,0) +
					(min + "").padStart(2,0) +
					(sec + "").padStart(2,0);
	
	return dateString;
}

/**
 * 폴더리스트를 구함
 * @returns
 */
function getFolderList(){
	//getProcessTypeByFolderPath();
	//getFolderDiscipline();
	
	let folders = [];
	 $.ajax({
			type: 'POST',
			url: './getFolderList.do',
			data: { 
				prj_id: params.prj_info.id
			},
			async: false,
			success: function(data) {
				let tmp_folder_List = data[0];
				let isAdmin = data[1];
				let isDcc = data[2];
				// db에서 불러온 폴더정보를 jsTree에서 그려지는 구조로 변환
				for(let i=0 ; i < tmp_folder_List.length; ++i){
					let tmp_Node = tmp_folder_List[i];
					
					let process_type = '';
					let doc_type = '';
					let folder_discipline_code_id = '';
					let folder_discipline_display = '';
					let folder_discipline_code = '';
					let useDRN = tmp_Node.useDRN;
					// Corr타입이면 무조건 N
					if(tmp_Node.doc_type==='Corr') useDRN = 'N';
					
					if(tmp_Node.folder_type==='DCC'){
						for(j=0;j<processTypeByFolderPath.length;j++){
							let process_folder_path = processTypeByFolderPath[j].folder_path;
							if(tmp_Node.folder_path.indexOf(process_folder_path)!=-1){
								process_type = processTypeByFolderPath[j].process_type;
								doc_type = processTypeByFolderPath[j].doc_type;
								if(tmp_Node.folder_path === process_folder_path)
									process_type = processTypeByFolderPath[j].process_type;
									doc_type = processTypeByFolderPath[j].doc_type;
							}
						}
						for(j=0;j<current_FolderDiscipline.length;j++){
							let discipline_folder_path = current_FolderDiscipline[j].folder_path;
							if(tmp_Node.folder_path.indexOf(discipline_folder_path)!=-1){
								folder_discipline_code_id = current_FolderDiscipline[j].discip_code_id;
								folder_discipline_display = current_FolderDiscipline[j].discip_display;
								folder_discipline_code = current_FolderDiscipline[j].discip_code;
								if(tmp_Node.folder_path === discipline_folder_path)
									folder_discipline_code_id = current_FolderDiscipline[j].discip_code_id;
									folder_discipline_display = current_FolderDiscipline[j].discip_display;
									folder_discipline_code = current_FolderDiscipline[j].discip_code;
							}
						}						
					}
					
					let	input_Folder = { 
							"id" : tmp_Node.folder_id, 
							"parent" : tmp_Node.parent_folder_id, 
							'text' : tmp_Node.folder_nm, 
							'type' : tmp_Node.folder_type ,
							'folder_path':tmp_Node.folder_path, 
							'process_type':process_type, 
							'doc_type':doc_type,
							'discipline_code_id': folder_discipline_code_id, 
							'discipline_display': folder_discipline_display, 
							'discipline_code' : folder_discipline_code,
							/*
							'auth_r':tmp_Node.auth_r,
							'auth_w':tmp_Node.auth_w,
							'auth_d':tmp_Node.auth_d,
							*/
							'useDRN':tmp_Node.useDRN
						};
					
					if(tmp_Node.folder_id === tmp_Node.parent_folder_id){ // 최상위 루트
						input_Folder.parent = "#";
						input_Folder.icon = "resource/images/pinwheel.png";
						input_Folder.state =  {'opened' : true,'selected' : false};
					}
					folders.push(input_Folder);
				}
			}
		});
	 
	 return folders;
}



function getPrjDocumentIndexList(documentselect,popup_folder_id){
	// $("#drnUpBtn")[0].style["display"] = "";
	
	let folderIdx = folderList.findIndex(i => i.id == current_folder_id);
	let drnBtn = $("#drnUpBtn")[0];
	
	if(drnBtn){
		// 기본적으로는 보여줌
		drnBtn.style["display"] = "";
		if(folderList[folderIdx].useDRN == "N") drnBtn.style["display"] = "none";
	}
	
	var select_rev_version;
	var unread_yn;
	if(documentselect==='documentselectpopup'){
		select_rev_version = $('#docsel_select_rev_version').val();
		unread_yn=$('#docsel_unread_yn').is(':checked')==true?'Y':'N';
		current_folder_id = popup_folder_id;
		if(documentListTable!==undefined){
			documentListTable.deselectRow();
		}
	}else if(documentselect==='drnSystemFile'){
		select_rev_version = $('#drnFile_docsel_select_rev_version').val();
		unread_yn=$('#drnFile_docsel_unread_yn').is(':checked')==true?'Y':'N';
		current_folder_id = popup_folder_id;
	}else{
		select_rev_version = $('#select_rev_version').val();
		unread_yn=$('#unread_yn').is(':checked')==true?'Y':'N';
	}
	$.ajax({
	    url: 'getPrjDocumentIndexList.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id: params.prj_info.id,
	    	folder_id: current_folder_id,
	    	select_rev_version: select_rev_version,
	    	unread_yn: unread_yn
	    },
	    success: function onData (data) {
	    	var doc_ctrl_li = [];
	    	if(data[0]!=undefined){
		    	for(i=0;i<data[0].length;i++){
		    		var Modified='';
		    		if(data[0][i].mod_date!=null){
		    			Modified = getDateFormat(data[0][i].mod_date);
		    		}else if(data[0][i].mod_date == null){
		    			Modified = getDateFormat(data[0][i].reg_date);
		    		}
		    		var rev_code_id = data[0][i].rev_code_id;
		    		var lock_yn = data[0][i].lock_yn=='Y'?'Y':'N';
		    		var reg_id = data[0][i].reg_id;
		    		var mod_id = data[0][i].mod_id;
		    		var ext = data[0][i].file_type;
		    		var imgIcon = '';
					if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/question-mark.png' class='i'>";
					}else if(ext == "png" || ext == "jpg" || ext == "jpeg" || ext == "gif"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/picture.png' class='i'>";
					}else if(ext == "pdf"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/pdf.ico' class='i'>";
					}else if(ext == "ppt" || ext == "pptx"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/ppt.ico' class='i'>";
					}else if(ext == "doc" || ext == "docx"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/doc.ico' class='i'>";
					}else if(ext == "xls" || ext == "xlsx" || ext == "csv"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/xls.ico' class='i'>";
					}else if(ext == "txt"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/txt.ico' class='i'>";
					}else if(ext == "hwp"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/hwp.ico' class='i'>";
					}else if(ext == "dwg"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/dwg.ico' class='i'>";
					}else if(ext == "zip"){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/zip.ico' class='i'>";
					}else{
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/general.ico' class='i'>";
					}
					if(lock_yn==='Y' && mod_id===session_user_id){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/checkout.ico' class='i'>";
					}else if(lock_yn==='Y' && mod_id!=session_user_id){
						imgIcon = "<img src='"+context_path+"/resource/images/docicon/lock.ico' class='i'>";
					}
			    	
		    		doc_ctrl_li.push({
		    			imgIcon: imgIcon,
		    			status: data[0][i].status,
	                    docNo: data[0][i].doc_no,
	                    Revision: data[0][i].rev_no,
	                    Title: data[0][i].title,
	                    Modified: Modified,
	                    Size: formatBytes(data[0][i].file_size, 2),
	                    bean: data[0][i]
		    		});
		    	}
		    	
		    	setPrjDocumentIndexList(doc_ctrl_li,documentselect);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function setPrjDocumentIndexList(doc_ctrl_li,documentselect){
	let tableId = '';
	if(documentselect==='documentselectpopup'){
		tableId = '#docSelTbl';
	}else if(documentselect==='drnSystemFile'){
		tableId = "#drnSystemFileGrid";
	}else{
		tableId = '#doc_ctrl_li';
	}
	documentListTable = new Tabulator(tableId, {
		data: doc_ctrl_li,
		layout: "fitColumns",
    	rowContextMenu: setRowContextMenu, //add context menu to rows
		height: 700,
		selectable: true,
		selectableRangeMode:"click",
		columns: [{formatter:"rowSelection", title:"", hozAlign:"center", headerSort:false,width:1},
	        {
	            title: "Doc",
	            field: "imgIcon",
	            formatter : "html",
				width : 60
	        },
	        {
	            title: "Status",
	            field: "status",
				width : 60
	        },
	        {
				title: "Doc. No",
				field: "docNo",
				hozAlign: "left",
				width : 180
			},
			{
				title: "Revision",
				field: "Revision",
				width : 80
			},
			{
				title: "Title",
				field: "Title",
				hozAlign: "left",
			},
			{
				title: "Modified",
				field: "Modified",
				width : 120
			}, {
				title: "Size",
				field: "Size",
				hozAlign: "right",
				width : 80
			}
		],
	});

	var select_rev_version;
	if(documentselect==='documentselectpopup'){
		select_rev_version = $('#docsel_select_rev_version').val();
	}else if(documentselect==='drnSystemFile'){
		select_rev_version = $('#drnFile_docsel_select_rev_version').val();
	}else{
		select_rev_version = $('#select_rev_version').val();
	}
	
	documentListTable.on("tableBuilt", function(){
		if(select_rev_version==='current'){
			documentListTable.hideColumn("status");
		}else{
			documentListTable.showColumn("status");
		}
	});
	
	function setRowContextMenu(row) {
		let rev_code_id = row.getData().bean.rev_code_id;
		let ext = row.getData().bean.file_type;
		let lock_yn = row.getData().bean.lock_yn;
		var rowCheckOutId = row.getData().bean.mod_id;
		var DCCArray = getPrjDCC();
		if(lock_yn==='Y' && (rowCheckOutId===session_user_id || DCCArray.indexOf(session_user_id)!=-1)){	//체크아웃한 당사자나 DCC일때 체크아웃 도서에 나타나는 컨텍스트 메뉴
			contextMenu[3].disabled = true;
			contextMenu[4].disabled = true;
			contextMenu[5].disabled = false;
			return contextMenu;
		}
		if(lock_yn==='Y'){	//CheckOut 상태
			contextMenu[3].disabled = true;
			contextMenu[4].disabled = true;
			contextMenu[5].disabled = true;
			return contextMenu;
		}
		if((rev_code_id==='' || rev_code_id===null || rev_code_id===undefined) && (ext==='' || ext===null || ext===undefined)){	//물음표 상태
			contextMenu[0].disabled = true;
			contextMenu[1].disabled = true;
			contextMenu[2].disabled = true;
			contextMenu[3].disabled = true;
			contextMenu[4].disabled = false;
			contextMenu[5].disabled = true;
			contextMenu[6].disabled = false;
			contextMenu[7].disabled = false;
			contextMenu[8].disabled = false;
			contextMenu[10].disabled = false;
			contextMenu[11].disabled = false;
			contextMenu[12].disabled = false;
			contextMenu[13].disabled = false;
			return contextMenu;
		}else{	//첨부 상태
			contextMenu[0].disabled = false;
			contextMenu[1].disabled = false;
			contextMenu[2].disabled = false;
			contextMenu[3].disabled = false;
			contextMenu[4].disabled = false;
			contextMenu[5].disabled = true;
			contextMenu[6].disabled = false;
			contextMenu[7].disabled = false;
			contextMenu[8].disabled = false;
			contextMenu[10].disabled = false;
			contextMenu[11].disabled = false;
			contextMenu[12].disabled = false;
			contextMenu[13].disabled = false;
			return contextMenu;
		}
    }
}


function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

function getDateFormat(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);
	const hh = datestring.substring(8,10);
	const mm = datestring.substring(10,12);
	const ss = datestring.substring(12,14);

	return year+'-'+month+'-'+day+' '+hh+':'+mm+':'+ss;
}


function getProcessTypeByFolderPath(){
	$.ajax({
	    url: 'getProcessTypeByFolderPath.do',
	    type: 'POST',
	    data: {
	    	prj_id:params.prj_info.id
	    },
		async: false,
	    success: function onData (data) {
	    	processTypeByFolderPath = data[0];
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

function getDateFormat_YYYYMMDD(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);

	return year+'-'+month+'-'+day;
}


function genInputTag(name,val,json_use){
	
	let item = document.createElement("input");
    item.type = 'hidden';
    item.name = name;
    item.value = !json_use ? val : JSON.stringify(val);
    
    return item;
	
}



/**
 * 최종승인 단계에서 승인 버튼을 누른 경우
 * TR 페이지 띄워준다.
 * @returns
 */
function lastConfrim(){
	
	let tab_id = "outgoingTab";
	let jsp_Path = '/page/trOutgoing';
	let type = 'TROutgoing';
	let text ='OUTGOING';
	if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
		// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
		let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
		tabList[tabIdx].folder_id = current_folder_id;
		
		tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
		
		tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
	} 	
	$('#transmittal_left').css('display','block');

	
}


/**
 * document select 창에서 다운로드
 * @returns
 */
function documentControlDownload(){
	let selectedArray = documentListTable.getSelectedData();
	if(selectedArray.length===0){
		alert('다운로드 받을 도서를 선택해주세요.');
	}else if(selectedArray.length===1){
		location.href='/downloadFile.do?prj_id='+encodeURIComponent(prj_id.id)+'&sfile_nm='+encodeURIComponent(selectedArray[0].bean.sfile_nm)+'&rfile_nm='+encodeURIComponent(selectedArray[0].bean.rfile_nm.replaceAll("&","@%@"));
	}else{
		let multiDownTbl = [];
		let lock_yn = '';
		for(let i=0;i<selectedArray.length;i++){
			lock_yn += selectedArray[i].bean.lock_yn;
		}
		if(lock_yn.indexOf('Y') !== -1){
			alert('잠금 상태가 아닌 도서를 선택하세요.');
		}else{
			for(let i=0;i<selectedArray.length;i++){
				multiDownTbl.push({
					FileName:selectedArray[i].bean.rfile_nm,
					RevNo:selectedArray[i].bean.rev_no,
					Title:selectedArray[i].bean.title,
					Size:formatBytes(selectedArray[i].bean.file_size,2),
					bean:selectedArray[i].bean
				});
				var table = new Tabulator("#multiDownTbl", {
					data: multiDownTbl,
					layout: "fitColumns",
					columns: [{
							title: "File Name",
							field: "FileName"
						},
						{
							title: "Rev No",
							field: "RevNo"
						},
						{
							title: "Title",
							field: "Title"
						},
						{
							title: "Size",
							field: "Size"
						}
					],
				});
			}
			$(".pop_multiDownload").dialog("open");
			$('#fileMultiDownload').attr('onclick','fileMultiDownload()');
			return false;
		}
	}
}

function fileRenameChk(){
	if($('#downloadFileRename').is(':checked'))
		$("#fileRenameSet").val("%DOCNO%_%REVNO%_%TITLE%");
	else
		$("#fileRenameSet").val("");
}
/**
 * 멀티다운로드 기능 활성화
 * @returns
 */
function fileMultiDownload(){
	var selectedArray = documentListTable.getSelectedData();
	var downloadFiles = '';
	var accHisDownList = [];
	for(let i=0;i<selectedArray.length;i++){
		downloadFiles += selectedArray[i].bean.doc_id +'%%'+ selectedArray[i].bean.rev_id + '@@';
		accHisDownList.push(JSON.stringify(selectedArray[i].bean));
	}
	
	$.ajax({
		url: 'insertAccHisList.do',
		type: 'POST',
		traditional : true,
		async: false,
		data:{
			jsonRowDatas: accHisDownList,
			prj_id: prj_id.id,
			method: 'DOWN'
		},
		success: function onData (data) {
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	//console.log("downloadFiles = "+downloadFiles);
	downloadFiles = downloadFiles.slice(0,-2);
	let renameYN='N';
	if($('#downloadFileRename').is(':checked')) renameYN='Y';
	
	let fileRenameSet = $('#fileRenameSet').val();
	if(selectedArray.length===1){
		location.href='/downloadFileRename.do?prj_id='+encodeURIComponent(prj_id.id)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
	}else{
		location.href='/fileDownload.do?prj_id='+encodeURIComponent(prj_id.id)+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
	}
	$(".pop_multiDownload").dialog("close");
}


function dcMultiUpload(){
	alert("can't upload file at DRN popup");
}

function documentExcel(){
	var select_rev_version = $('#drnFile_docsel_select_rev_version').val();
	var unread_yn=$('#drnFile_docsel_unread_yn').is(':checked')==true?'Y':'N';
	
	
	var current_folder_path = $("#drnFile_docsel_current_folder_path").val();
	
//	console.log(select_rev_version);
	
	let gridData = documentListTable.getData();
	
	let jsonList = [];
	
	for(let i=0; i<gridData.length; i++) {
		jsonList.push(JSON.stringify(gridData[i]));
	}
	
	let fileName = 'Document_List';
	
	var f = document.dciUploadForm_PathList;
	
	f.prj_id_PathList.value = prj_id.id;
	f.folder_id_PathList.value = popup_folder_id;
	f.prj_nm_PathList.value = prj_id.full_nm;
	f.current_folder_path_PathList.value = current_folder_path;
	f.select_rev_version_PathList.value = select_rev_version;
	f.unread_yn_PathList.value = unread_yn;
	f.fileName_PathList.value = fileName;
	f.jsonRowdatas_PathList.value = jsonList;
	f.action = "DocPathListExcelDownload.do";
    f.submit();
}



function getComment(){
	$("#drn_comment").dialog("open");
	
	  return new Promise(function (resolve, reject) {
		    document.getElementById('commentOk').addEventListener("click", function () {
		      resolve($("#drn_comment_text").val());
		      this.removeEventListener("click", arguments.callee);
		      $("#drn_comment").dialog("close");
		    });
		    
		    document.getElementById('commentCancel').addEventListener("click", function () {
			      reject();
			      this.removeEventListener("click", arguments.callee);
			      $("#drn_comment").dialog("close");
			    });
		    
		    
	});
	
}

function changeSelectToInput($id) {
	let id_checked = "#" + $id[0].id + " option:checked";
	// console.log($(id_checked).text());
	  $id.replaceWith($('<input />').attr({
	    type: 'text',
	    id: $id.attr('id'),
	    name: $id.attr('name'),
	    //value: $(id_checked).text(),
	    val: 'test',
	    //value: $id.val(),
	    class: 'readonly',
	    readonly: 'readonly',
	    style: "width:100%"
	  }));
	  
}

function setReadMode(){
	drnUp_Var.inputs.attr("disabled",false);
	drnUp_Var.inputs.attr("readonly",true);
	$("#drn_review_conclusion").attr("readonly",true);
	$("#drn_review_conclusion").css("border","none");
	
	
	// Print 버튼 보여줌
	$("#drn_PrintBtn").css("display","");
	
	
	// 검토단계에서는 체크시트 제어 버튼 필요 없음
	if(drnUp_Var.as == 'A'){
		$("#drn_allSelect").parent()[0].style["display"] = "none";
		// 체크시트 타이틀 영역의 너비를 늘림
		$("#drn_check_sheet_title_input").parent()[0].style["width"] = "50%"
	}
	
	// 첨부문서 그리드에서 체크박스 제거
	if(drnUp_Var.grid[0].initialized)
		drnUp_Var.grid[0].getColumn("chk_col").updateDefinition({visible:false});
	else{
		drnUp_Var.grid[0].on("tableBuilt",function(){
			drnUp_Var.grid[0].getColumn("chk_col").updateDefinition({visible:false});
		});
	}
	// status필드 select필드 숨기고 input으로 교체 및 readonly
	let status_label = $("#Status_write option:checked").text();
	$("#Status_write").css("display","none");
	let input = document.createElement("input");
	input.value = status_label;
	input.readOnly = true;    
	input.style["padding"] = "0px 5px";
	input.style["border"] = "1px solid #ccc";
	input.style["fontFamily"] = "inherit";
	input.style["display"] = "inline-block";
	input.style["height"] = "25px";
	input.style["fontSize"] = "11px";
	input.style["color"] = "#666";
	input.style["lineHeight"] = "23px";
	input.style["width"] = "100%";
	$("#td_status_wirte").append(input);
	
	// 첨부문서 그리드에서 체크박스 제거
	if(drnUp_Var.grid[1].initialized){
		
		$(".drn_items_no").css("display","none");
		$(".drn_item_nm").css("border","0");
		
		
		$(".reviewed_select").css("background","none");
		$(".reviewed_select").css("padding","0px");
		$(".reviewed_select").css("border","0");
		
		$(".approved_select").css("background","none");
		$(".approved_select").css("padding","0px");
		$(".approved_select").css("border","0");
	}else{
		drnUp_Var.grid[1].on("tableBuilt",function(){
			$(".drn_items_no").css("display","none");
			$(".drn_item_nm").css("border","0");
			
			$(".reviewed_select").css("background","none");
			$(".reviewed_select").css("padding","0px");
			$(".reviewed_select").css("border","0");
			
			$(".approved_select").css("background","none");
			$(".approved_select").css("padding","0px");
			$(".approved_select").css("border","0");
			
		});
	}
	
	
	/*
	// status필드 select에서 input으로 변경
	$("#Status_write").val(status_label);
	changeSelectToInput($("#Status_write"));
	$("#Status_write").val(status_label);
	*/

}

function checksheetMouseOver(){
	$("#drn_check_sheet_title_input").attr("placeholder","");
}

function checksheetMouseOut(){
	$("#drn_check_sheet_title_input").attr("placeholder","INPUT CHECK SHEET TITLE");
}

function localFileDown(){
	let prj_id = drnUp_Var.pageData.drnInfo.prj_id;
	let drn_id = drnUp_Var.pageData.drnInfo.drn_id;
	let file_nm = $("#drn_local_file_txt").text();
	
	$.ajax({
	    url: 'localFileChk.do',
	    type: 'POST',
	    data: {
	    	prj_id:prj_id,
	    	drn_id:drn_id,
	    	sfile_nm:file_nm
	    },
		async: false,
		beforeSend:function(){
		       $('body').prepend(loading);
		 },
		complete:function(){
		    $('#loading').remove();
		 },
	    success: function onData (data) {
	    	let checkStatus = data.model.status;
	    	if(checkStatus){
	    		location.href = '/drnLocalFileDownload.do?prj_id='+encodeURIComponent(prj_id)+'&drn_id='+encodeURIComponent(drn_id) +'&file_nm='+encodeURIComponent(file_nm);
	    	}else{
	    		let msg = data.model.err_msg;
	    		alert(msg);
	    	}
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
	
}

