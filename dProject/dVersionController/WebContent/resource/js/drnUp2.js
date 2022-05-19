var drnUp_Var = {};


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
	    	prj_id:getPrjInfo().id,
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
	if(pageMode == "read"){
		if(drnUp_Var.as != "T"){
			drnUp_Var.readOnly = true;
			readOnlyMode();	
		}else{
			pageTitle = "작성중 DRN 상세페이지";
			$("#drn_tempSaveBtn")[0].style["display"] = "none";
		}
	}
	
	
	if(drnUp_Var.as == "R"){
		pageTitle = "반려됨";
		let reason_text;
		
		$.ajax({
		    url: 'getRejectPoint.do',
		    type: 'POST',
		    async:false,
		    data:{
		    	prj_id:getPrjInfo().id,
		    	drn_id:drnUp_Var.drn_id
		    },
		    success: function onData (data) {
		    	let result = data.model.drnHist;
		    	let level = result[result.length - 2].drn_approval_status;
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
		    	}
		    	reason_text += ":" + result[result.length - 1].reg_id;
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
	    	prj_id:getPrjInfo().id,
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
	    	prj_id:getPrjInfo().id
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
	    	prj_id:getPrjInfo().id
	    },
	    success: function onData (data) {
	    	drnUp_Var.default_review_conclusion = data.model.rvc;
	    	$("#drn_review_conclusion")[0].innerText = drnUp_Var.default_review_conclusion;
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
    default_sheet_row.check_items.push("Standard Form을 사용 하였는가?");
    default_sheet_row.check_items.push("Doc No. Rev. No. Title Status, 제출일 등이 표주에 따라 작성 되었는가?");
    default_sheet_row.check_items.push("Index Page는 작성 되었는가?");
    default_sheet_row.check_items.push("작성자, 검토자, 승인자 확인은 되었는가?");
    default_sheet_row.check_items.push("수정(변경) 사항은 Cloud mark 및 별도의 표시가 되었는가?");
    default_sheet_row.check_items.push("Revision Log의 개정 History Revised Page, Description이 자세하게 작성 되어 있는가?");
    default_sheet_row.check_items.push("지난 성공/실패 사례집이 적용 되었는가?");
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
	
	$("#Status_write").attr("disabled",true);
	$(".drn_readMode").css('display','none');
	$(".drn_read").attr("readonly",true); 
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
			{
				title: "파일명(DOC NO)",
				field: "doc_no",
				headerSort:false,
				width: "35%"
			},
			{
				title: "REVISION",
				field: "revision",
				headerSort:false,
				width: "15%"
			},
			{
				title: "rev_id",
				field: "rev_id",
				visible:false,
			},
			{
				title: "문서명",
				field: "title",
				headerSort:false,
				width: "35%"
			},
			{
				title: "SIZE",
				field: "file_size",
				headerSort:false,
				width: "15%"
			},
		]
	});
	
	drnUp_Var.grid.push(fileGrid);
	
	let sheetGrid = new Tabulator("#drn_SheetGrid", {
		layout: "fitDataFill",
		placeholder:"",
		data:drnUp_Var.default_sheet,
		selectable:false,
		index:"index",
		minHeight:100,
		columns: [			
			{ title:"index",field:"index",visible:false},
			{
				title: "NO",
				field: "items_no",
				headerSort:false,
				width:"5%",
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
					if(controlLevel >= 1)
						input.readOnly = true;
					
					let div2 = document.createElement("div");
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
					}
					
					if(drnUp_Var.pageMode > 1) select.disabled = true;
					
					div.append(input,document.createElement("br"));
					div2.append(txt,select);
					div.append(div2);
					
			        return div;
				},
				
				width:"15%",
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
						
						let rowPad = document.createElement("div");
							rowPad.classList.add("rowPadding");
							rowPad.innerHTML = "　";
						let rowCont = document.createElement("div");
						// rowCont.innerHTML = (i+1) + ") ";
						let rowInput = document.createElement("input");
							rowInput.value = data[i]?data[i]:"　";
							rowInput.style.border = "none";
							rowInput.style.width = "90%";
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
							row.append(rowPad);
							row.append(rowCont);						
						div.append(row);
					}
			        return div;
				},
				width:"40%",
				resizable:false
			},
			{
				title: "DISCIPLINE",
				field: "discipline_code_id",
				headerSort:false,
				resizable:false,
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
						row.append(rowPad);
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
							rowCont.style["appearance"]= "none" /* 화살표 없애기 공통*/
							rowCont.disabled = true;
						}
						
//					
//						switch(drnUp_Var.pageMode){
//							case 0: // 0,1 은 design 단계이므로 상태 제어 가능
//							case 1:
//								break;
//							default:
//								rowCont.classList.add("drn_display");
//								rowCont.style["appearance"]= "none" /* 화살표 없애기 공통*/
//								rowCont.disabled = true;
//						}
						
						
						
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
				width: 100
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
							rowCont.style["appearance"]= "none"
							rowCont.disabled = true;
						}
						/*
						if(drnUp_Var.pageMode <= 2){
							rowCont.classList.add("drn_display");
							rowCont.classList.add("drn_noUse");
						}
						else if(drnUp_Var.pageMode > 3){
							rowCont.classList.add("drn_display");
							
							rowCont.style["appearance"]= "none"
							rowCont.disabled = true;
						}
						*/
						
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
				width: 100
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
							rowCont.disabled = true;
						}
						
/*						if(drnUp_Var.pageMode < 4){
							rowCont.classList.add("drn_display");
							rowCont.classList.add("drn_noUse");
						}
						else if(drnUp_Var.pageMode >= 4){
							rowCont.classList.add("drn_display");
						}*/
						
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
				width: 100
			},
			{
				title: "",
				field: "btn",
				//width:"9%",
				headerSort:false,
				resizable:false,
				formatter: function(cell, formatterParams, onRendered) {
			        // 배열로 받음
					let cnt = cell.getData().items_cnt;
					
					let div = document.createElement("div");
					for(let i=0;i<cnt;i++){
					let row = document.createElement("div");
					let rowPad = document.createElement("div");
					rowPad.classList.add("rowPadding");
					rowPad.innerHTML = "　";
					// 버튼 element 생성
					/*
					let chgBtn = document.createElement("button"); 
					chgBtn.innerHTML = "변경";
					chgBtn.classList.add('button','btn_blue','drn_ChgBtn');
					chgBtn.onclick = function(){
						let item_no = cell.getData().items_no;
						let item_idx = i;
						let row = cell.getRow();
						itemEdit(row,item_idx);
					}
					*/
					
					let delBtn = document.createElement("button");
					delBtn.innerHTML = "열 삭제";
					delBtn.classList.add('button','btn_orange','drn_delBtn','drn_readMode');
					delBtn.onclick = function(){
						let item_no = cell.getData().items_no;
						let item_idx = i;
						let row = cell.getRow();
						itemDel(row,item_idx);
					}
					
					row.append(rowPad,delBtn);
					if(drnUp_Var.readOnly)
						row.style["display"] = "none";
					if(drnUp_Var.pageMode>1)
						row.style["display"] = "none";
					
					if(i < cnt-1)
						row.classList.add('checkgrid_BtnRow');
					else
						row.classList.add('checkgrid_LastBtnRow');
					div.append(row);
					}
			        return div;
				},
				width: 108
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
				title: "design",
				field: "design",
				hozAlign:"center",
				formatter:"html",
				resizable:false
			},
			{
				headerSort:false,
				title: "Reviewed",
				field: "reviewed",
				hozAlign:"center",
				formatter:"html",
				resizable:false
			},
			{
				headerSort:false,
				title: "Approved",
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
		minHeight:300,
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
	

	
	
	
	drnUp_Var.reviewGrid.on("tableBuilt", function(){
		if(drnUp_Var.pageMode<2)
		$.ajax({
		    url: 'searchUsersVO.do',
		    type: 'POST',
		    data:{
		    	searchKeyword:session_user_id,
		    	searchUser:'user_id'
		    },
		    success: function onData (data) {
		    	drnUp_Var.design = data[0][0];
		    	set_drnSigns("design","name",data[0][0].user_kor_nm);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
	});
	
	
	
	drnUp_Var.chk_modal_grid.on("tableBuilt", function(){
		$.ajax({
		    url: 'get_checkList.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id
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
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			
			level = 0;
		
			if(pageData!=null)
				setDrnPageFromData(pageData);
			break;
		case 2: // DCC 승인
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			level = 1;
			break;
		case 3: // 리뷰 승인
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			level = 2;
			break;
		case 4: //
		case 5:
			drnUp_Var.btns[0].style["display"] = "none"; // 결재선 지정
			drnUp_Var.btns[1].style["display"] = "none"; // 임시저장
			drnUp_Var.btns[2].style["display"] = "none"; // 결재상신
			drnUp_Var.btns[3].style["display"] = "none"; // 승인
			drnUp_Var.btns[4].style["display"] = "none"; // 반려
			break;
			
	}
	
	if(pageData) setDrnPageFromData(pageData);
	
	if(pageMode == 1) // 작성중 페이지에서 임시저장 비활성화
		drnUp_Var.btns[1].style["display"] = "none"; // 임시저장		
	
	
	if(pageMode >= 2){
		drnUp_Var.btns[6].style["display"] = "none";
		drnUp_Var.btns[7].style["display"] = "none";
		drnUp_Var.btns[8].style["display"] = "none";
		
		
		drnUp_Var.fileBtns[0].style["display"] = "none";
		drnUp_Var.fileBtns[1].style["display"] = "none";
		drnUp_Var.fileBtns[2].style["display"] = "none";
		
		drnUp_Var.inputs[9].readOnly = true;
		
		$("#Status_write").attr("disabled",true);
		drnUp_Var.inputs[4].disabled = true;
		
		setDrnPageFromData(pageData);
	}
	
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
				set_drnSigns("design","name",drnUp_Var.lineData.design.user_kor_nm);
				setBottom();
			});
		else {
			set_drnSigns("design","name",drnUp_Var.lineData.design.user_kor_nm);
			setBottom();
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
					set_drnSigns("design","sign",'<img src="/getSignImage.do?sfile_nm='+data.design.info.sfile_nm+'">');
				}else {
					set_drnSigns("design","sign",data.design.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("design","date", getDateFormat_YYYYMMDD(data.design.date) );
			if(data.design.info.sfile_nm != null){
				set_drnSigns("design","sign",'<img src="/getSignImage.do?sfile_nm='+data.design.info.sfile_nm+'">');
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
					set_drnSigns("reviewed","sign",'<img src="/getSignImage.do?sfile_nm='+data.review.info.sfile_nm+'">');
				}else {
					set_drnSigns("reviewed","sign",data.review.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("reviewed","date", getDateFormat_YYYYMMDD(data.review.date) );
			if(data.review.info.sfile_nm != null){
				set_drnSigns("reviewed","sign",'<img src="/getSignImage.do?sfile_nm='+data.review.info.sfile_nm+'">');
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
					set_drnSigns("approved","sign",'<img src="/getSignImage.do?sfile_nm='+data.approve.info.sfile_nm+'">');
				}else {
					set_drnSigns("approved","sign",data.approve.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("approved","date", getDateFormat_YYYYMMDD(data.approve.date) );
			if(data.approve.info.sfile_nm != null){
				set_drnSigns("approved","sign",'<img src="/getSignImage.do?sfile_nm='+data.approve.info.sfile_nm+'">');
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
    formData.set("prj_id",getPrjInfo().id);
    formData.set("pageData",JSON.stringify(pageData));
    formData.set("action","T");
    
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
			        timeout: 600000,
				    success: function onData (data) {
				    	if(data.model.status == "SUCCESS"){
				    		alert("Temporay save Complete.");
				    		getDRNsCount(folderList);
				    		$("#tree").jstree(true).settings.core.data = getDRNsCount(getFolderList());
				    		$("#tree").jstree(true).refresh();
				    		getPrjDocumentIndexList();
				    		$(".pop_collaboW").dialog("close");
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
//	        alert("등록 오류");
	    }
	});
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
    formData.set("prj_id",getPrjInfo().id);
    formData.set("pageData",JSON.stringify(pageData));
    formData.set("action","A");
    
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
			        timeout: 600000,       
				    success: function onData (data) {
				    	if(data.model.status == "SUCCESS"){
				    		alert("결재 상신");
				    		getDRNsCount(folderList);
				    		$("#tree").jstree(true).settings.core.data = getDRNsCount(getFolderList());
				    		$("#tree").jstree(true).refresh();
				    		getPrjDocumentIndexList();
				    		$(".pop_collaboW").dialog("close");
				    	}else{
				    		alert("저장 실패");
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
//	        alert("등록 오류");
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
	
	drnInfo.review_conclusion =  $("#drn_review_conclusion").text();
	
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
    formData.set("prj_id",getPrjInfo().id);
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
        timeout: 600000,       
	    success: function onData (data) {
	    	if(data.model.status == "SUCCESS"){
	    		alert("승인");
	    		$(".pop_collaboW").dialog("close");
	    		
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
	
	let pageData = getPageData();
	
	// 파일 체크
	let form = $('#drnUploadForm')[0];
    let formData = new FormData(form);
    // 프로젝트 id form 데이터에 추가    
    formData.set("folder_id",drnUp_Var.folder_id);
    formData.set("prj_id",getPrjInfo().id);
    // console.log(session_user_id);
    formData.set("user_id",session_user_id);    
    formData.set("pageData",JSON.stringify(pageData));
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
    				  alert("체크하지 않은 항목이 있습니다.");
    				  return;
    			  }
    		}
    		break;
    	case "1": // approval -> 승인
    		action = "C";
    		tmp= $(".approved_select");
    		for(let i=0;i<tmp.length;i++){
    			  if(tmp[i].value == ''){
    				  alert("체크하지 않은 항목이 있습니다.");
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
        timeout: 600000,       
	    success: function onData (data) {
	    	if(data.model.status == "SUCCESS"){
	    		alert("승인");
	    		getDRNsCount(folderList);
	    		$("#tree").jstree(true).settings.core.data = getDRNsCount(getFolderList());
	    		$("#tree").jstree(true).refresh();
	    		getDrnList();
	    		getPrjDocumentIndexList();
	    		$(".pop_collaboW").dialog("close");
	    		
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
 * 거부 버튼
 * @returns
 */
function drn_deny(){
	
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
    formData.set("prj_id",getPrjInfo().id);
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
	    		getDRNsCount(folderList);
	    		$("#tree").jstree(true).settings.core.data = getDRNsCount(getFolderList());
	    		$("#tree").jstree(true).refresh();
	    		getDrnList();
	    		getPrjDocumentIndexList();
	    		$(".pop_collaboW").dialog("close");
	    	}else{
	    		alert("반려 실패");
	    	}
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}
/**
 * Print 버튼
 * @returns
 */
function drn_print(){
	// DB에 저장되어 있는 DRN인지 확인
	if(drnUp_Var.drn_id){ // 기안이 올라간 경우
		$.ajax({
		    url: 'drnPrint.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id,
		    	drn_id:drnUp_Var.drn_id
		    },
		    success: function onData (data) {
		    	window.open(data);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
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
	let folderList = getFolderList();
	folderList = drn_dccFolder(folderList);
	let tree = $('#drnFile_docSelTree').jstree({
	     "core" : {
	    	 'data' : folderList,
	         "check_callback" : true
	       }
	    });
	/*.on('click.jstree', function(event) { // 클릭 이벤트 바인딩
	    	drn_docSelTreeMenuActive(event)} ); */
	
	 // $('#drnFile_docSelTree').jstree(true).refresh();
	
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
	location.href='/drnFileDownload.do?prj_id='+encodeURIComponent(getPrjInfo().id)+'&drn_id='+drnUp_Var.drn_id+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
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
	
	
	
	let err = "";
	$.ajax({
	    url: 'drnValidationCheck.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:getPrjInfo().id,
	    	doc_type:current_doc_type,
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
	    	prj_id:getPrjInfo().id,
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
	$("#localFileTd")[0].innerHTML = obj.files[0].name;
}

/**
 * 문서 목록에서 선택된 아이템 삭제
 * @returns
 */
function drn_fileDel(){
	let rows = drnUp_Var.grid[0].getSelectedRows();
	drnUp_Var.grid[0].deleteRow(rows);
}


/**
 * 일괄적용 버튼
 * @param idx
 * @returns
 */
function drn_setAll(idx){
	
	let val = $("#drn_allSelect").val();
	if(val === '선택') return;
	
	
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

/**
 * 상단의 drn input 정보들 값 세팅
 * @returns
 */
function set_drnInput(){
	if(!drnUp_Var.docData) return;
	
	let docData = drnUp_Var.docData;
	
	// date
	let date = genDateStr().substr(0,8);
	$("#Date_write").datepicker({
		dateFormat:"yy-mm-dd",
		nextText: '다음 달', 
		prevText: '이전 달', 
		dayNames: ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'], 
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], 
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'], 
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],		
	});
	let dateStr = date.substr(0,4) + "-" +date.substr(4,2) + "-" +date.substr(6,2);
	drnUp_Var.inputs[4].value = dateStr;
	
	$("#Date_write").change(function(){
		let drnDateStr = drnUp_Var.inputs[4].value;
		drnDateStr = drnDateStr.substr(0,4)+drnDateStr.substr(5,2)+drnDateStr.substr(8,2); 
		drnUp_Var.inputs[1].value = getPrjInfo().no + "-" + drnUp_Var.discip.code + "-" + drnDateStr + "-";
	});

	
	// DRN제목
	drnUp_Var.inputs[0].value =
		(docData.length > 1) ?
		(docData[0].title + "외 " + (docData.length-1) + "건")  : docData[0].title; 
		
	// DRN NO
	let drnDateStr = drnUp_Var.inputs[4].value;
	drnDateStr = drnDateStr.substr(0,4)+drnDateStr.substr(5,2)+drnDateStr.substr(8,2); 
	if(!drnUp_Var.pageData)
		drnUp_Var.inputs[1].value = getPrjInfo().no + "-" + drnUp_Var.discip.code + "-" + drnDateStr + "-";
	
	// Project no
	drnUp_Var.inputs[2].value = getPrjInfo().no;
	// Project Name
	drnUp_Var.inputs[3].value = getPrjInfo().nm;
	
	
	// doc_no (rev_no)
	
	let doc_no_str = "";
	for(let i=0;i<docData.length;i++) 
		doc_no_str += drnUp_Var.docData[i].doc_no + " (REV:" + drnUp_Var.docData[i].revision +"), ";
	doc_no_str = doc_no_str.substr(0,doc_no_str.length-2);
	drnUp_Var.inputs[5].value = doc_no_str;
	// status
	if(!drnUp_Var.pageData)
		drnUp_Var.inputs[6].value = "";
	// doc title
	let title_str = "";
	for(let i=0;i<docData.length;i++) 
		title_str += drnUp_Var.docData[i].title + ", ";
	title_str = title_str.substr(0,title_str.length-2);
	drnUp_Var.inputs[7].value = title_str;
	
	// 로컬파일
	$("#localFileTd").text("");
	if(drnUp_Var.drn_id)
	$.ajax({
	    url: 'getLocalFile.do',
	    type: 'POST',
	    data:{
	    	prj_id:getPrjInfo().id,
	    	drn_id:drnUp_Var.drn_id
	    },
	    success: function onData (data) {
	    	let result = data.model.local_file
	    	$("#localFileTd").text(result.rfile_nm);
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
	set_drnInput();
	
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
    if(result[0].user_kor_nm == '' || result[1].user_kor_nm == '')
        alert("결재자 미지정");
	setBottom(result);
	

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
	    	prj_id:getPrjInfo().id
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
		    	prj_id:getPrjInfo().id
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
					console.log(tmp_Node);
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
				title: "Group",
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
		selectable:1,
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
				status:"APPROVEER",
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


// 검토자 선택 버튼
function setLine_reviewBtn(){
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[3].getRowFromPosition(1).getCell("user_kor_nm").setValue(sel.user_kor_nm);
	drnUp_Var.lineData.reviewer = sel;
	
}

// 결재자 선택 버튼
function setLine_approveBtn(){
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[3].getRowFromPosition(2).getCell("user_kor_nm").setValue(sel.user_kor_nm);
	drnUp_Var.lineData.approver = sel;
}
// 결재자 삭제 버튼
function setLine_delBtn1(){
	let grid = drnUp_Var.grid[3];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[3].getRowFromPosition(sel.id=='reviewer'?1:2).getCell("user_kor_nm").setValue("");
	if(drnUp_Var.lineData[sel.id])
		drnUp_Var.lineData[sel.id] = "";
	
	
}


function setLine_distributeBtn(){
	let grid = drnUp_Var.grid[2];
	let sel = grid.getSelectedData()[0];
	if(!sel) return;
	
	drnUp_Var.grid[4].addData(sel);
	
}
function setLine_delBtn2(){
	let grid = drnUp_Var.grid[4];
	let sel = grid.getSelectedRows()[0];
	if(!sel) return;
	
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
	    	prj_id:getPrjInfo().id,
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

	    	$("#drn_check_sheet_title_input").val(sheetData.title);
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
	    	prj_id:getPrjInfo().id,
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
