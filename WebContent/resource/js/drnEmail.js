var drnUp;


var loading = '<div id="loading" class="loading"><img id="loading_img" alt="loading" src="../resource/images/loading.gif" /></div>';

$(function(){
	
	
	/*
	$(window).ajaxStart(function(){
    	
	})

	.ajaxStop(function(){
		
	});
	*/
	
	// 변수 셋팅
	drnUp = {};
	
	$("#drn_comment").dialog({
	    autoOpen: false,
	    maxWidth: 1000,
	    width: 1000,
	    modal: true
	});
	
	let mode_chk = $("#_drn_auth").text();
	if(mode_chk == '') drnUp.readOnly = true;
	else drnUp.readOnly = false;
	
	
	drnUp.prj_id = $("#_prj_id").text();
	drnUp.drn_id = $("#_drn_id").text();
	drnUp.user_id = $("#_user_id").text();
	drnUp.dccUser = JSON.parse( $("#_dccUser").text() );
	
	drnUp.drnInfo = JSON.parse( $("#_drnInfo").text() );
	drnUp.drnDocs = JSON.parse( $("#_drnDocs").text() );
	// 파일 사이즈 변환

	for(let i=0;i<drnUp.drnDocs.length;i++){
		let file_size = drnUp.drnDocs[i].file_size;
		if(!file_size) file_size = 0;
		file_size = formatBytes(Number(file_size));
		drnUp.drnDocs[i].file_size = file_size;
	}
	
	drnUp.discip = $("#_discip").text();
	drnUp.checkSheetList = JSON.parse( $("#_checkSheetList").text() );
	drnUp.checkSheetItems = JSON.parse( $("#_checkSheetItems").text() );
	drnUp.status = $("#_status").text();
	drnUp.as = drnUp.drnInfo.drn_approval_status;
	drnUp.lineInfo = JSON.parse( $("#_approvalLine").text() );
	
	// 로컬파일
	$("#localFileTd").text("");
	if(drnUp.drn_id)
	$.ajax({
	    url: 'getLocalFile.do',
	    type: 'POST',
	    data:{
	    	prj_id:drnUp.prj_id,
	    	drn_id:drnUp.drn_id
	    },
	    success: function onData (data) {
	    	let result = data.model.local_file
	    	if(result)
	    		$("#localFileTd").html('<a id="drn_local_file_txt" onclick="localFileDown()">' + result.rfile_nm + '</>');	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	let approval_status = {};
	approval_status.design = {};
	approval_status.design.date = $("#_ds_date").text();
	approval_status.design.info =  JSON.parse( $("#_ds_info").text() );
	
	approval_status.review = {};
	approval_status.review.date = $("#_rv_date").text();
	approval_status.review.info =  JSON.parse( $("#_rv_info").text() );
	
	approval_status.approve = {};
	approval_status.approve.date = $("#_ap_date").text();
	approval_status.approve.info =  JSON.parse( $("#_ap_info").text() );
	
	drnUp.approval_id = drnUp.lineInfo.approval_id;
	drnUp.design = {user_id : approval_status.design.info.user_id, user_kor_nm:approval_status.design.info.user_kor_nm};
	
	drnUp["as_map"] = {
			""  : -1, // 상태값 없음
			"T" : 0, // 임시저장
			"A" : 1, // 결재상신
			"E" : 2, // dcc 승인
			"1" : 3, // review 승인
			"C" : 4, // 최종 승인
			"R" : 5  // 반려
	};
	
	// 프로젝트의 dcc유저를 가져옴
	drnUp.dccUser = [];
	
	
	
	let pageData = {};
	pageData.drnInfo = drnUp.drnInfo;
	pageData.sheetData = {};
	pageData.sheetData.check_sheet = drnUp.checkSheetList;
	pageData.sheetData.check_sheet_items = drnUp.checkSheetItems;
	// pageData.sheetData.check_sheet_title = drnUp.checkSheetTitle;
	// pageData.lineInfo = {};
	
	
	e_initDrnGrid();
	setInputs();
	setDrnPage(pageData);
	setApprovalStatus(approval_status);
	if(drnUp.readOnly) readOnly();
});

/**
 * 상단 input영역 파싱데이터 set
 * 
 * @returns
 */
function setInputs(){
	$("#DRN_title_write").val(drnUp.drnInfo.drn_title);
	$("#DRN_no_write").val(drnUp.drnInfo.drn_no);
	$("#Project_no_write").val(drnUp.drnInfo.prj_no);
	$("#Project_name_write").val(drnUp.drnInfo.prj_nm);	
	let dateStr = drnUp.drnInfo.reg_date;
	dateStr = dateStr.substr(0,4) + "-" + dateStr.substr(4,2) + "-" + dateStr.substr(6,2);
	$("#Date_write").val(dateStr);
	let status = drnUp.status.split("&");
	/*
	let option = $("<option value="+status[1] + ">"+status[0]+"</option>");
	$("#Status_write").append(option);
	 */
	$("#Status_write").val(status[0]);
	let docNo_revNo = "";
	for(let i=0 ; i<drnUp.drnDocs.length ; i++)
		docNo_revNo += drnUp.drnDocs[i].doc_no + "(" + drnUp.drnDocs[i].rev_no + "), ";
	docNo_revNo = docNo_revNo.substr(0,docNo_revNo.length-2);
	
	$("#Doc_no_write").val(docNo_revNo);
	
	let doc_title = "";
	doc_title = drnUp.drnDocs[0].doc_title;
	
	/*
	if(drnUp.drnDocs.length>1)
		doc_title += (" 외 " + drnUp.drnDocs.length-1 + "건");
	*/
	
	$("#Doc_title_write").val(doc_title);
	$("#check_sheet_title").val(drnUp.drnInfo.check_sheet_title);
	$("#drn_review_conclusion").text(drnUp.drnInfo.review_conclusion);
	
	
	switch(drnUp.as){
		case 'A':
			$("#drn_Accept").text("검토 승인");
			break;
	}

}

/**
 * 그리드 영역 생성 및 초기화
 * 
 * @returns
 */
function e_initDrnGrid(){
	drnUp.grid = [];
	
	let fileGrid = new Tabulator("#drn_FileGrid", {
		layout: "fitColumns",
		placeholder:"No Data Set",
		movableColumns: false,
		selectable:false,
		width:"99%",
		index:"index",
		height: 100,
		columns: [			
			{ title:"index",field:"index",visible:false},
			{
				title: "파일명(DOC NO)",
				field: "doc_no",
				headerSort:false,
				widthGrow : 3
			},
			{
				title: "REVISION",
				field: "rev_id",
				headerSort:false,
				widthGrow : 2
			},
			{
				title: "문서명",
				field: "title",
				headerSort:false,
				widthGrow : 3
			},
			{
				title: "SIZE",
				field: "file_size",
				headerSort:false,
				widthGrow : 2
			},
		]
	});
	
	drnUp.grid.push(fileGrid);
	
	fileGrid.on("tableBuilt",function(){
		drnUp.grid[0].addData(drnUp.drnDocs);
	});
	
	fileGrid.on("rowDblClick",function(e, row){
		let data = row.getData();
		location.href='/downloadFile.do?prj_id='+encodeURIComponent(data.prj_id)+'&sfile_nm='+encodeURIComponent(data.sfile_nm)+'&rfile_nm='+encodeURIComponent(data.rfile_nm.replaceAll("&","@%@"));
	});
	
	
	let sheetGrid = new Tabulator("#drn_SheetGrid", {
		layout: "fitColumns",
		placeholder:"no data",
		data:drnUp.default_sheet,
		selectable:false,
		index:"index",
		columns: [			
			{ title:"index",field:"index",visible:false},
			{
				title: "NO",
				field: "items_no",
				headerSort:false,
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
			        // 배열로 받음
					const data = cell.getValue();
					let div = document.createElement("div");
					div.classList.add('items_no');
					
					let input = document.createElement("input");
					input.value = data;
					input.style["border"] = "none";
					input.read
					
					div.append(input);
					
			        return div;
				},
				
				widthGrow:3,
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
							
							switch(drnUp.pageMode){
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
				widthGrow:9,
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
						// discipline full name
						rowCont.innerHTML = drnUp.discip? drnUp.discip : "";
						// disicpline code
						// rowCont.innerHTML = data[i];
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
				widthGrow:2,
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
						let controlLevel = drnUp.as_map[drnUp.as];
						if(controlLevel > 0){
							rowCont.classList.add("drn_display");
							rowCont.style["background"]= "none";
							rowCont.style["padding"]= "0 0 0 0";
							rowCont.disabled = true;
						}
						
//					
// switch(drnUp.pageMode){
// case 0: // 0,1 은 design 단계이므로 상태 제어 가능
// case 1:
// break;
// default:
// rowCont.classList.add("drn_display");
// rowCont.style["appearance"]= "none" /* 화살표 없애기 공통*/
// rowCont.disabled = true;
// }
						
						
						
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
			},
			{
				title: "REVIWED",
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
						
						
						let controlLevel = drnUp.as_map[drnUp.as];
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
						/*
						 * if(drnUp.pageMode <= 2){
						 * rowCont.classList.add("drn_display");
						 * rowCont.classList.add("drn_noUse"); } else
						 * if(drnUp.pageMode > 3){
						 * rowCont.classList.add("drn_display");
						 * 
						 * rowCont.style["appearance"]= "none" rowCont.disabled =
						 * true; }
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
				widthGrow:2,
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
						
						let controlLevel = drnUp.as_map[drnUp.as];
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
						
/*
 * if(drnUp.pageMode < 4){ rowCont.classList.add("drn_display");
 * rowCont.classList.add("drn_noUse"); } else if(drnUp.pageMode >= 4){
 * rowCont.classList.add("drn_display"); }
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
				widthGrow:2,
			},
			{
				title: "",
				field: "btn",
				// width:"9%",
				visible:false,
				headerSort:false,
				resizable:false,
				widthGrow:1
			},
		],
	});
	
	drnUp.grid.push(sheetGrid);
	
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
	
	drnUp.reviewGrid = reviewGrid;
	
	
}


function setDrnPage(pageData){

		
	// 체크시트 데이터 불러오기
	let sheetData = {};
	sheetData.check_sheet = pageData.sheetData.check_sheet;
	sheetData.check_sheet_items = pageData.sheetData.check_sheet_items;
	sheetData.check_sheet_title = pageData.sheetData.check_sheet_title;
	if(!drnUp.grid[1].initialized){
		drnUp.grid[1].on("tableBuilt", function(){
			setCheckSheetGrid(sheetData);
		});
	} else setCheckSheetGrid(sheetData);

	// 체크결과 일괄적용 버튼
	let control_level = drnUp.as_map[drnUp.as];
	if(control_level != 2 && control_level != 3){
		$("#chk_all_div")[0].style.display = "none";
	}
	
	$("#drn_allSelect")[0].onchange = function(){
		drn_setAll(control_level);
	};
	/*
	 * $("#drn_allSelectBtn").click(function(){
	 * 
	 * let val = $("#drn_allSelect").val(); if(val === '선택') return;
	 * 
	 * let gridData = drnUp.grid[1].getData(); console.log(control_level); //
	 * 그리드 데이터 업데이트 for(let i=0;i<gridData.length;i++){ let tmp = gridData[i]
	 * let arr = []; for(let j=0;j<tmp.items_cnt;j++) arr.push(val);
	 * if(control_level == 2)
	 * drnUp.grid[1].getRow(i).getCell("reviewed_check_status").setValue(arr);
	 * else if(control_level == 3)
	 * drnUp.grid[1].getRow(i).getCell("approved_check_status").setValue(arr); }
	 * 
	 * 
	 * switch(control_level){ case 2: // dcc승인 후, review가 들어옴 let reviewed =
	 * $(".reviewed_select").val(val); for(let i=0;i<reviewed.length;i++)
	 * reviewed[i].value = val; break; case 3: // review 승인 후, 최종결재자가 들어옴 let
	 * approved = $(".approved_select"); for(let i=0;i<approved.length;i++)
	 * approved[i].value = val; break; default : break; }
	 * 
	 * 
	 * });
	 */
	
	
	// 승인버튼
	$("#drn_Accept").click(function(){
		let reason = "";
		
		getComment().then(function(comment){
			reason = comment;
			let pageData = getPageData();
			
			// 파일 체크
			let form = $('#drnUploadForm')[0];
		    let formData = new FormData(form);
		    // 프로젝트 id form 데이터에 추가
		    formData.set("folder_id",pageData.folder_id);
		    formData.set("prj_id",drnUp.prj_id);
		    formData.set("pageData",JSON.stringify(pageData));
		    formData.set("comment",reason);
		    let chkUserId = drnUp.user_id.split(";");
		    formData.set("user_id",drnUp.user_id);
		    let action = "P";
		    let tmp;
		    switch(drnUp.drnInfo.drn_approval_status){
		    	case "A": // drn상신 -> dcc 승인
		    		action = "E";
		    		break;
		    	case "E": // review -> 승인
		    		action = "1";
		    		// 점검항목 체크여부 확인
		    		tmp = $(".reviewed_select");
		    		for(let i=0;i<tmp.length;i++){
		    			  if(tmp[i].value == ''){
		    				  alert("There is unchecked value");
		    				  return;
		    			  }
		    		}
		    		break;
		    	case "1": // approval -> 승인
		    		action = "C";
		    		// 점검항목 체크여부 확인
		    		tmp = $(".approved_select");
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
		        //timeout: 600000,
		        beforeSend:function(){
			          $('body').prepend(loading);
					},
				complete:function(){
					  $('#loading').remove();
					 },
			    success: function onData (data) {
			    	console.log(data.model.status);
			    	if(data.model.status == "SUCCESS"){
			    		alert("ACCEPT COMPLETE");
			    		window.close();
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
		
	});
	
	// 반려 버튼
	$("#drn_Deny").click(function(){
		let reason = "";
		
		getComment().then(function(comment){
			reason = comment;
		
			let pageData = getPageData();
			
			// 파일 체크
			let form = $('#drnUploadForm')[0];
		    let formData = new FormData(form);
		    // 프로젝트 id form 데이터에 추가
		    formData.set("user_id",drnUp.user_id);
		    formData.set("folder_id",pageData.folder_id);
		    formData.set("prj_id",drnUp.prj_id);
		    formData.set("pageData",JSON.stringify(pageData));
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
		        //timeout: 600000, 
		        beforeSend:function(){
			          $('body').prepend(loading);
					},
				complete:function(){
					  $('#loading').remove();
					 },
			    success: function onData (data) {
			    	console.log(data.model.status);
			    	if(data.model.status == "SUCCESS"){
			    		alert("반려됨");
			    	}else{
			    		alert("반려 실패");
			    	}
			    	
			    	window.close();
			    	
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		
		}).catch(function() {
			alert("If you don't leave a reason for rejection, can't reject it.");
			return false;
		});
	});
	/*
	$("#drn_PrintBtn").click(function(){
		// DB에 저장되어 있는 DRN인지 확인
			
			var url = '/drnPrint.do';
			let drnForm = document.createElement("form");
			drnForm.id = "drnForm";
		    var target = 'DRN_print';
		    
		    window.open("", target, "width=1250,height=950,resizable=0,toolbar=yes,menubar=yes,location=yes");

		    $(drnForm).attr('action', url);
		    $(drnForm).attr('target', target); // window.open 타이틀과 매칭 되어야함
		    $(drnForm).attr('method', 'POST');
		    
	    	
		    
		    drnForm.append(genInputTag("prj_id",drnUp.prj_id,false));
		    drnForm.append(genInputTag("drn_id",drnUp.drn_id,false));
		    
		    
		    
		    document.body.appendChild(drnForm);
		    
		    
		    drnForm.submit();
		    $("#drnForm").remove();
			
			
		
	});
	*/
	
	
	
}

function drnPrint(){
	// DB에 저장되어 있는 DRN인지 확인
	
	var url = '/drnPrint.do';
	let drnForm = document.createElement("form");
	drnForm.id = "drnForm";
    var target = 'DRN_print';
    
    window.open("", target, "width=1250,height=950,resizable=0,toolbar=yes,menubar=yes,location=yes");

    $(drnForm).attr('action', url);
    $(drnForm).attr('target', target); // window.open 타이틀과 매칭 되어야함
    $(drnForm).attr('method', 'POST');
    
	
    
    drnForm.append(genInputTag("prj_id",drnUp.prj_id,false));
    drnForm.append(genInputTag("drn_id",drnUp.drn_id,false));
    
    
    
    document.body.appendChild(drnForm);
    
    
    drnForm.submit();
    $("#drnForm").remove();
}

function drn_setAll(idx){
	console.log(idx);
	let val = $("#drn_allSelect").val();
	if(val === '선택') return;
	
	 if (confirm("Are you sure to set this value to all??") == false){
	     return false;
	 }
	 
// if (confirm("Are you sure to set this value to all??") == true){
//	     
// }else{ //취소
// return false;
// }
	
	let gridData = drnUp.grid[1].getData();
	
	// 그리드 데이터 업데이트
	for(let i=0;i<gridData.length;i++){
		let tmp = gridData[i]
		let arr = [];
		for(let j=0;j<tmp.items_cnt;j++) arr.push(val);
		if(idx == 2)
			drnUp.grid[1].getRow(i).getCell("reviewed_check_status").setValue(arr);
		else if(idx == 3)
			drnUp.grid[1].getRow(i).getCell("approved_check_status").setValue(arr);	
	}
		
	
	switch(idx){
		case 2:
			let reviewed = $(".reviewed_select");
			for(let i=0;i<reviewed.length;i++) reviewed[i].value = val;
			break;
		case 3:
			let approved = $(".approved_select");
			for(let i=0;i<approved.length;i++) approved[i].value = val;
			break;
	}
	
	
}


function genInputTag(name,val,json_use){
	
	let item = document.createElement("input");
    item.type = 'hidden';
    item.name = name;
    item.value = !json_use ? val : JSON.stringify(val);
    
    return item;
	
}

/**
 * 서명 정보를 셋팅해준다.
 * 
 * @param data
 * @returns
 */
function setApprovalStatus(data){
	
	// 하단 서명부 이름 설정
	if(drnUp.lineInfo.approval_id > 0) {
		if(!drnUp.reviewGrid.initialized)
			drnUp.reviewGrid.on("tableBuilt", function(){
				/*
				set_drnSigns("design","name",data.design.info.user_kor_nm);
				set_drnSigns("reviewed","name",data.review.info.user_kor_nm);
				set_drnSigns("approved","name",data.approve.info.user_kor_nm);
				*/
				let design_nm = drnUp.lineInfo.designed + " "
				+ drnUp.lineInfo.designer_code;
				design_nm = design_nm.trim();
				set_drnSigns("design","name",design_nm);
				let review_nm = drnUp.lineInfo.reviewed + " " 
					+ drnUp.lineInfo.reviewer_code;
				review_nm = review_nm.trim();
				set_drnSigns("reviewed","name",review_nm);
				let approve_nm = drnUp.lineInfo.approved + " "
					+ drnUp.lineInfo.approver_code;
				approve_nm = approve_nm.trim();
				set_drnSigns("approved","name",approve_nm);
			});
		else {
			/*
			set_drnSigns("design","name",data.design.info.user_kor_nm);
			set_drnSigns("reviewed","name",data.review.info.user_kor_nm);
			set_drnSigns("approved","name",data.approve.info.user_kor_nm);
			*/
			let design_nm = drnUp.lineInfo.designed + " "
			+ drnUp.lineInfo.designer_code;
			design_nm = design_nm.trim();
			set_drnSigns("design","name",design_nm);
			let review_nm = drnUp.lineInfo.reviewed + " " 
				+ drnUp.lineInfo.reviewer_code;
			review_nm = review_nm.trim();
			set_drnSigns("reviewed","name",review_nm);
			let approve_nm = drnUp.lineInfo.approved + " "
				+ drnUp.lineInfo.approver_code;
			approve_nm = approve_nm.trim();
			set_drnSigns("approved","name",approve_nm);
		}
	}
	if(data.design.date != null){
		if(!drnUp.reviewGrid.initialized){
			drnUp.reviewGrid.on("tableBuilt", function(){
				set_drnSigns("design","date",getDateFormat_YYYYMMDD(data.design.date) );
				if(data.design.info.sfile_nm != null){
					set_drnSigns("design","sign",'<img src="/getSignImage.do?sfile_nm='+data.design.info.sfile_nm+'"style="width:100%; height: 100%;">');
				}else {
					set_drnSigns("design","sign",data.design.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("design","date", getDateFormat_YYYYMMDD(data.design.date) );
			if(data.design.info.sfile_nm != null){
				set_drnSigns("design","sign",'<img src="/getSignImage.do?sfile_nm='+data.design.info.sfile_nm+'"style="width:100%; height: 100%;">');
			}else {
				set_drnSigns("design","sign",data.design.info.user_kor_nm );
			}
		}
		
	}
	
	if(data.review.date != ""){
		if(!drnUp.reviewGrid.initialized){
			drnUp.reviewGrid.on("tableBuilt", function(){
				set_drnSigns("reviewed","date",getDateFormat_YYYYMMDD(data.review.date) );
				if(data.review.info.sfile_nm != null){
					set_drnSigns("reviewed","sign",'<img src="/getSignImage.do?sfile_nm='+data.review.info.sfile_nm+'"style="width:100%; height: 100%;">');
				}else {
					set_drnSigns("reviewed","sign",data.review.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("reviewed","date", getDateFormat_YYYYMMDD(data.review.date) );
			if(data.review.info.sfile_nm != null){
				set_drnSigns("reviewed","sign",'<img src="/getSignImage.do?sfile_nm='+data.review.info.sfile_nm+'"style="width:100%; height: 100%;">');
			}else {
				set_drnSigns("reviewed","sign",data.review.info.user_kor_nm );
			}
		}	
	}
	
	if(data.approve.date != ""){
		if(!drnUp.reviewGrid.initialized){
			drnUp.reviewGrid.on("tableBuilt", function(){
				set_drnSigns("approved","date",getDateFormat_YYYYMMDD(data.approve.date) );
				if(data.approve.info.sfile_nm != null){
					set_drnSigns("approved","sign",'<img src="/getSignImage.do?sfile_nm='+data.approve.info.sfile_nm+'"style="width:100%; height: 100%;">');
				}else {
					set_drnSigns("approved","sign",data.approve.info.user_kor_nm );
				}
			});	
		}else {
			set_drnSigns("approved","date", getDateFormat_YYYYMMDD(data.approve.date) );
			if(data.approve.info.sfile_nm != null){
				set_drnSigns("approved","sign",'<img src="/getSignImage.do?sfile_nm='+data.approve.info.sfile_nm+'"style="width:100%; height: 100%;">');
			}else {
				set_drnSigns("approved","sign",data.approve.info.user_kor_nm );
			}
		}
	}
	
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
	
	drnUp.grid[1].setData(sheet_rows);

	
}

/**
 * 서명 필드를 채워준다. 필드값은 왼쪽부터 순서대로 index번호
 * 
 * @param field
 * @param data
 * @returns
 */
function set_drnSigns(field,rowId,data){
	let gridRow = drnUp.reviewGrid.getRow(rowId);
	if(!gridRow) return;	
	
	
	gridRow.getCell(field).setValue(data);

	
}


/**
 * 페이지의 정보들을 데이터화
 * 
 * @returns
 */
function getPageData(){
	
	if(!drnUp) return;
	
	// 1) input박스 영역의 값들을 데이터화
	let inputs = $(".drn_inputs");
	let drnInfoVarNm = ["drn_title","drn_no","prj_no","prj_nm","date","doc_no","status","doc_title","local","chksheet_nm"];
	let drnInfo = {};
	
	
	for(let i=0;i<inputs.length;i++)
		drnInfo[drnInfoVarNm[i]] = inputs[i].value;

	let tmpDate = drnInfo["date"]; 
	drnInfo.date =  tmpDate.substr(0,4) + tmpDate.substr(5,2) + tmpDate.substr(8,2);
	
	
	drnInfo.signData = {};
	
	drnInfo.signData.name = JSON.stringify(drnUp.reviewGrid.getData()[0]);
	drnInfo.signData.sign = JSON.stringify(drnUp.reviewGrid.getData()[1]);
	drnInfo.signData.date = JSON.stringify(drnUp.reviewGrid.getData()[2]);
	drnInfo.signData.design = drnUp.design.user_id;
	drnInfo.signData.review = drnUp.lineInfo.reviewed_id;
	drnInfo.signData.approve = drnUp.lineInfo.approved_id;
	drnInfo.signData.approval_id = drnUp.lineInfo.approval_id;
	drnInfo.signData.distributer = drnUp.lineInfo.distribute_id;
	
	
	let dccArr = drnUp.dccUser;
	let dcc = "";
	for(let i=0 ; i<dccArr.length ; i++)
		dcc += dccArr[i].user_id + "@";
	dcc = dcc.substr(0,dcc.length-1);
	drnInfo.signData.dcc = dcc;
	
	drnInfo.signData = JSON.stringify(drnInfo.signData);
	// drnInfo.discip_code_id = drnUp.discip.code_id;
	
	drnInfo.docData = [];
	let docData = drnUp.grid[0].getData();
	for(let i=0;i<docData.length;i++){
		let doc = docData[i];
		drnInfo.docData.push(JSON.stringify(doc));
	}
		
		
	drnInfo.folder_id = drnUp.drnDocs[0].folder_id;
	
	
	let sheetData = drnUp.grid[1].getData();

	drnInfo.sheetArr = [];
	drnInfo.sheetData = JSON.stringify(sheetData);
	
	
	for(let i=0;i<sheetData.length;i++){
		let tmp = sheetData[i];
		drnInfo.sheetArr.push(JSON.stringify(tmp));
	}
	
	drnInfo.check_sheet_id = drnUp.drnInfo.check_sheet_id;
	
	drnInfo.review_conclusion =  $("#drn_review_conclusion").text();
	
	if(drnUp.drn_id) drnInfo.drn_id = drnUp.drn_id;
	
	
	return drnInfo;
	
	
	
	
}
/**
 * 파일 다운로드 버튼
 * 
 * @returns
 */
function drnFileDownload(){
	var selectedArray = drnUp.grid[0].getData();
	var downloadFiles = '';
	for(let i=0;i<selectedArray.length;i++){
		downloadFiles += selectedArray[i].doc_id +'%%'+ selectedArray[i].rev_id + '@@';
	}
	downloadFiles = downloadFiles.slice(0,-2);
	console.log(downloadFiles);
	let renameYN='Y';
	let fileRenameSet = $('#DRN_no_write').val();
	location.href='/drnFileDownload.do?prj_id='+encodeURIComponent(drnUp.prj_id)+'&drn_id='+drnUp.drn_id+'&renameYN='+encodeURIComponent(renameYN)+'&fileRenameSet='+encodeURIComponent(fileRenameSet)+'&downloadFiles='+encodeURIComponent(downloadFiles);
}


function readOnly(){
	$("#drn_Accept")[0].style["display"] = "none";
	$("#drn_Deny")[0].style["display"] = "none";
	$("#chk_all_div")[0].style["display"] = "none";
	
}

function getDateFormat_YYYYMMDD(datestring){

	const year = datestring.substring(0,4);
	const month = datestring.substring(4,6);
	const day = datestring.substring(6,8);

	return year+'-'+month+'-'+day;
}

function formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
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


function localFileDown(){
	
	let prj_id = drnUp.prj_id;
	let drn_id = drnUp.drn_id;
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
