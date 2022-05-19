var DCIregister = {};

/**
 * DCI 업로드 템플릿 생성
 * @returns
 */
function dciIndexRegister(id){
	
	// 프로세스타입을 확인(step때문)
	// 프로세스를 타지 않으면 해당 창은 띄울 수 없다.
	let chk = false;
	
	$.ajax({
		    url: 'checkProcessType.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id,
		    	folder_id:id
		    },
		    async:false,
		    success: function onData (data) {
		    	let result = data.model;
		    	if(!result.status) {
		    		alert("index register를 진행할 수 없는 process타입의 폴더입니다.");
		    		
		    		return;
		    	}
		    	chk = true;
		    	console.log(result.process_type);
		    	
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	 
	 if(!chk) return;
	
	// 변수 초기화
	DCIregister = {};
	DCIregister.folder_id = id;
	
	getParams();
	
	setDialog();
	


	DCIregister.grid = new Tabulator("#doc_attr", {
		//data: doc_attr,
		layout: "fitColumns",
		placeholder:"No Data Set",
		height: 600,
		columns: [			
			{ title:"index",field:"index",visible:false},
			{
				title: "Doc. No",
				field: "doc_no",
				editor:"input",
				headerSort:false,
				widthGrow:4
			},
			{
				title: "Title",
				field: "title",
				editor:"input",
				headerSort:false,
				widthGrow:4
			},
			{
				title: "Doc.Type",
				field: "doc_type",
				editor:"select",
				headerSort:false,
				widthGrow:1
			},
			{
				title: "CATEGORY",
				field: "category_cd",
				editor:"select",
				headerSort:false,
				widthGrow:1
			},
			{
				title: "WBS code",
				field: "wbs_code",
				editor:"select",
				headerSort:false,
				widthGrow:1
			},
			{
				title: "size",
				field: "size_code",
				editor:"select",
				headerSort:false,
				widthGrow:1
			},
			{
				title: "DESIGNER",
				field: "designer_id",
				editor:"input",
				headerSort:false,
				widthGrow:1
			},
			{
				title: "REMARK",
				field: "remark",
				editor:"select",
				headerSort:false,
				widthGrow:1
			},
			{
				title: "SUPPLY DOC.NO",
				field: "supDocNo",
				editor:"input",
				headerSort:false,
				widthGrow:1
			},
		],
	});
	
	DCIregister.grid.on("tableBuilt", function(){
		// doc type을 찾음
		 $.ajax({
			    url: 'getProcessDocType.do',
			    type: 'POST',
			    data:{
			    	prj_id:getPrjInfo().id,
			    	folder_id:DCIregister.folder_id
			    },
			    async:false,
			    success: function onData (data) {
			    	DCIregister.doc_type = data[0];
			    	if(DCIregister.doc_type == "MCI" || DCIregister.doc_type == 'PCI'){
			    		DCIregister.grid.updateColumnDefinition("category_cd",{visible:false});
			    		DCIregister.grid.updateColumnDefinition("size_code",{visible:false});
			    		DCIregister.grid.updateColumnDefinition("doc_type",{visible:false});
			    		
			    	}
			    	else{
			    		DCIregister.grid.updateColumnDefinition("category_cd",{visible:true});
			    		DCIregister.grid.updateColumnDefinition("size_code",{visible:true});
			    		DCIregister.grid.updateColumnDefinition("doc_type",{visible:true});
			    	}
			    	
			    },
			    error: function onError (error) {
			        console.error(error);
			    }
			});
		
		// STEP 필드 추가
		$.ajax({
		    url: 'getDCIFieldList.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id,
		    	doc_type:DCIregister.doc_type
		    },
		    success: function onData (data) {
		    	if(data == ""){
		    		return;
		    	}
		    	
		    	let field = addStepField(data);
		    	for(let i=0;i<field.length;i++)
		    		DCIregister.grid.addColumn(field[i]);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
	});
}
/**
 *	Step필드 추가 
 */

function addStepField(fieldList){
	let cols = [];
	DCIregister.stepField = [];
	
	for(let i=0;i<fieldList.length;i++){
		let col = {};
		col.title = fieldList[i];
		col.field = fieldList[i];
		col.editor ="input";
		col.headerSort =false;
		cols.push(col);
		DCIregister.stepField.push(fieldList[i]);
	}
	
	return cols;
	
}



function getParams(){
	
	let tmp_doc_type;
	 $.ajax({
		    url: 'getProcessDocType.do',
		    type: 'POST',
		    data:{
		    	prj_id:getPrjInfo().id,
		    	folder_id:DCIregister.folder_id
		    },
		    async:false,
		    success: function onData (data) {
		    	tmp_doc_type = data[0];
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	
	
	DCIregister.params = {};
	
	DCIregister.params.category_cd = {};
	DCIregister.params.size_code = {};
	DCIregister.params.doc_type = {};
	DCIregister.params.wbs_code = {};
	
	// params code별로 실제 값 가져옴
	$.ajax({
	    url: 'getDCIParams.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:getPrjInfo().id,
	    	doc_type:tmp_doc_type
	    },
	    success: function onData (data) {
	    	DCIregister.params["category_cd"] = data[0];
	    	DCIregister.params["size_code"] = data[1];
	    	DCIregister.params["doc_type"] = data[2];
	    	DCIregister.params["wbs_code"] = data[3];
	    	
	    	setGridParams();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	
}

function setGridParams(){
	for(value in DCIregister.params){
		let tmpParams = DCIregister.params[value];
		let id_Code = {};
		let code_Id = {};
		let paramsList = [];
		
		for(let i=0;i<tmpParams.length;i++){
			id_Code[value==="wbs_code" ? tmpParams[i].wbs_code_id:tmpParams[i].set_code_id] = 
				(value==="wbs_code" ? tmpParams[i].wbs_code:tmpParams[i].set_code);
			code_Id[value==="wbs_code" ? tmpParams[i].wbs_code:tmpParams[i].set_code] = 
				(value==="wbs_code" ? tmpParams[i].wbs_code_id:tmpParams[i].set_code_id);
			paramsList.push((value==="wbs_code" ? tmpParams[i].wbs_code:tmpParams[i].set_code));
		}
		
		DCIregister.params[value].id_Code = id_Code;
		DCIregister.params[value].code_Id = code_Id;
		DCIregister.params[value].paramsList = paramsList; 
	}
	
	
	DCIregister.grid.getColumn('wbs_code').updateDefinition({
		editorParams:{values:DCIregister.params["wbs_code"].id_Code},
		formatter:function(cell, formatterParams, onRendered) {
	        const data = cell.getValue();

	       let params = DCIregister.params["wbs_code"].id_Code[data];
	       
	    	return params; 
		}
		
	});
	DCIregister.grid.getColumn('category_cd').updateDefinition({
		editorParams:{values:DCIregister.params["category_cd"].id_Code},
		formatter:function(cell, formatterParams, onRendered) {
	        const data = cell.getValue();

	       let params = DCIregister.params["category_cd"].id_Code[data];
	       
	    	return params; 
		}
	
	});
	DCIregister.grid.getColumn('doc_type').updateDefinition({
		editorParams:{values:DCIregister.params["doc_type"].id_Code},
		formatter:function(cell, formatterParams, onRendered) {
	        const data = cell.getValue();
	        console.log(data);
	       let params = DCIregister.params["doc_type"].id_Code[data];
	       console.log(params);
	    	return params; 
		}
	});
	DCIregister.grid.getColumn('size_code').updateDefinition({
		editorParams:{values:DCIregister.params["size_code"].id_Code},
		formatter:function(cell, formatterParams, onRendered) {
	        const data = cell.getValue();

	       let params = DCIregister.params["size_code"].id_Code[data];
	       
	    	return params; 
		}
	});
	
}


/**
 * 모달창 내용 세팅
 * @returns
 */
function setDialog(){
	$(".left_b")[0].innerText = current_folder_path;
	
	$.ajax({
	    url: 'dciGetDiscip.do',
	    type: 'POST',
	    data:{
	    	prj_id:getPrjInfo().id,
	    	folder_id:Number(DCIregister.folder_id)
	    },
	    success: function onData (data) {
	    	console.log(data);
	    	DCIregister.discip = data[0];
	    	$("#indexReg_discip").val(DCIregister.discip);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

/**
 * fromExcel 버튼
 * @returns
 */
function dci_fromExcelBtn(){
	document.getElementsByName("dciExcelfile")[0].click();
}

function dciFileUp(obj){
	
	let form = $('#dciUploadForm_PathList')[0];
    let formData = new FormData(form);
    formData.set("prj_id",getPrjInfo().id);
    
    loadingWithMask();
    
	 $.ajax({             
	    	type: "POST",          
	        enctype: 'multipart/form-data',  
	        url: "dciTemplateUpload.do",        
	        data: formData,          
	        processData: false,    
	        contentType: false,      
	        cache: false,           
	        // timeout: 600000,       
	        success: function (result) {
	        	form["dciExcelfile"].value = "";
	        	closeLoadingWithMask();
	        	
	        	if(result.model.status != 'SUCCESS'){
	        		// error exception alert
	        		alert(result.model.status)
	        		return;
	        	}
	        	
	        	console.log(result);
	        	getParams();
	        	dciGridSet(result.model.dataFromExcel);	
	        	alert("Excel Loaded Successfully");
	        	
	        	// wbsGridAddMode(result.model.dataFromExcel);
	        },          
	        error: function (e) {  
	        	console.log("ERROR : ", e);     
	            alert("fail");
	            closeLoadingWithMask();
	         }     
		}); 
	 
	 
	
}

function dciGridSet(data){
	
	let tmpData = [];
	
	for(let i=0;i<data.length;i++){
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.doc_no = row[colIdx++];
		getRow.title = row[colIdx++];
		if(DCIregister.doc_type != 'MCI' && DCIregister.doc_type != 'PCI'){
			getRow.doc_type = DCIregister.params["doc_type"].code_Id[row[colIdx++]];
			getRow.doc_type_id = DCIregister.params["doc_type"].code_Id[getRow.doc_type];;
		}
		if(DCIregister.doc_type != 'MCI' && DCIregister.doc_type != 'PCI'){
			getRow.category_cd = DCIregister.params["category_cd"].code_Id[row[colIdx++]];
			getRow.category_cd_id = DCIregister.params["category_cd"].code_Id[getRow.category_cd];
		}
		getRow.wbs_code = DCIregister.params["wbs_code"].code_Id[row[colIdx++]];
		getRow.wbs_code_id = DCIregister.params["wbs_code"].code_Id[getRow.wbs_code];
		if(DCIregister.doc_type != 'MCI' && DCIregister.doc_type != 'PCI'){
			getRow.size_code = DCIregister.params["size_code"].code_Id[row[colIdx++]];
			getRow.size_code_id = DCIregister.params["size_code"].code_Id[getRow.size_code];
		}
		getRow.designer_id = row[colIdx++];
		getRow.remark = row[colIdx++];
		getRow.supDocNo = row[colIdx++];
		
		if(DCIregister.stepField)
		for(let j=0;j<DCIregister.stepField.length;j++)
			getRow[DCIregister.stepField[j]] = row[colIdx++];
		
		
		tmpData.push(getRow);
	}
	
	DCIregister.grid.setData(tmpData);
	/*
	// overwrite 모드
	if($("input[name=dci_overwrite]")[0].checked){
		if (confirm("덮어 씌우겠습니까? ") == true)
			overwrite(tmpData);
			//DCIregister.grid.setData(tmpData);
		else 
			DCIregister.grid.addData(tmpData);
	}else
		DCIregister.grid.addData(tmpData);
	*/
	
	DCIregister.grid.redraw();
}
/**
 * 덮어 씌우기 모드
 * @param data
 * @returns
 */
function overwrite(data){
	// 원본 데이터
	let afterData = DCIregister.grid.getData();
	
	// doc_no 존재 여부에 따른 데이터 가공처리
	for(let i=0;i<data.length;i++){
		let tmpDocno = data[i].doc_no;
		let idx = indexOfGrid(tmpDocno)
		// doc_no가 기존 데이터에 존재할 시 교체
		if(idx > 0) afterData[idx] = data[i];
		// 기존 데이터에 없으면 추가
		else afterData.push(data[i]);			
	}
	
	// 가공된 데이터로 그리드 데이터 셋팅
	DCIregister.grid.setData(afterData);
	
}

/**
 * 해당 그리드에 doc_no 있는지 확인
 * @returns
 */
function indexOfGrid(doc_no){
	let orgData = DCIregister.grid.getData();
	
	for(let i=0;i<orgData.length;i++)
		if(orgData[i].doc_no == doc_no) return i;
	
	
	return -1;
	
}


/**
 * Retrieve 버튼 눌렀을 경우, DB내용 다시 조회해서 갱신
 * @returns
 */
function dci_retriveBtn(){
	$.ajax({
	    url: 'getDCIRegisterList.do',
	    type: 'POST',
	    data:{
	        prj_id : getPrjInfo().id,
	        prj_nm : getPrjInfo().full_nm,
	        discip : DCIregister.discip,	   
	    	folder_id : DCIregister.folder_id,
	    	doc_type : DCIregister.doc_type
	    },
	    success: function onData (data) {
	    	console.log(data);
	    	let result = setStepDate(data[1]);
	    	DCIregister.grid.setData(result);
	    	
	    	DCIregister.grid.on("cellEdited", function(cell){
	    		let isSelectCol = cell.getColumn().getDefinition().editor == "select";
	    		if(!isSelectCol) return;
	    		
	    		let field = cell.getField();
	    		let row = cell.getRow();
	    		let rowData = row.getData();
	    		
	    		rowData[field + "_id"] = DCIregister.params[field].code_Id[cell.getValue()];
	    		
	    		row.update(rowData);
	    		
	    		DCIregister.grid.redraw();
	    	});
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}

/**
 * eng step에 정의된 코드값을 불러와 필드로 만들어 준다.
 * @param data
 * @returns
 */
function setStepDate(data){
	let stepField = DCIregister.stepField;
	let tmpStep = data.stepData;

	
	for(let i=0;i<data.length;i++){
		data[i].wbs_code = DCIregister.params["wbs_code"].id_Code[data[i].wbs_code_id];
		data[i].category_cd = DCIregister.params["category_cd"].id_Code[data[i].category_cd_id];
		data[i].size_code = DCIregister.params["size_code"].id_Code[data[i].size_code_id];
		data[i].doc_type = DCIregister.params["doc_type"].id_Code[data[i].doc_type_id];
		for(let x=0; x < stepField.length; x++)
			data[i][stepField[x]] = data[i].stepData[x];
	}
	
	
	return data;
}


/**
 * toExcel버튼을 눌렀을 때, 프로젝트명,discipline,시간 등을 반영하여 파일명 생성
 * 및 현재 데이터를 엑셀파일로 만들어 준다.
 * @returns
 */
function dci_toExcelBtn(){

	let dateString = genDateStr();
	let fileName = getPrjInfo().no;
	if(!DCIregister.discip)
		fileName += "_UNASSIGNED_";
	else if(DCIregister.discip == ' ')
		fileName += "_";
	else if(DCIregister.discip)
		fileName += "_" + DCIregister.discip.trim() + "_";
	
	/*
	for(let i=0;i<DCIregister.discip.length;i++)
		fileName += DCIregister.discip[i].trim() + "_";
	*/
	
	fileName += dateString + '_multiIndexRegister';
	
	console.log(fileName);
	
	let discipList = "";
		
	
	discipList += DCIregister.discip;
	
	
	
    var f = document.dciUploadForm_PathList; 
    

    
    f.prj_id.value = getPrjInfo().id;
    f.prj_nm.value = getPrjInfo().full_nm;
    f.discipline.value = discipList;
    f.fileName.value = fileName;	   
    f.folder_id.value = DCIregister.folder_id;
    f.doc_type.value = DCIregister.doc_type;
    f.jsonString.value = JSON.stringify(DCIregister.grid.getData());
    
    f.action = "dciExcelDownload.do";
    f.submit();   	
}


/**
 * save 버튼을 눌렀을 경우 작동
 * @returns
 */
function dci_saveBtn(){
	if(!dci_chkData()) return;
	
	
	let dci_overwrite =  $("input[name=dci_overwrite]")[0].checked;
	if(dci_overwrite){
		if (confirm("덮어 씌우겠습니까? ") == false) return;
	}
	
	let tmpGridData = DCIregister.grid.getData();
	let changedList = DCIregister.grid.getEditedCells();
	let changedData = [];
	for(let i = 0 ; i < changedList.length ; i++){
		let tmpIdx = changedList[i].getData().index
		changedData.push(tmpIdx);
	}
	// 변화가 있는 row들 json으로 변환
	let tmpStrings = [];

	for(let i=0;i<tmpGridData.length;i++){
		let stepData = [];
		
		if(DCIregister.stepField)
		for(let j=0;j<DCIregister.stepField.length;j++)
			stepData.push(tmpGridData[i][DCIregister.stepField[j]]);
		
		tmpGridData[i].stepData = stepData;
		
		tmpStrings.push( JSON.stringify(tmpGridData[i]) );
	}
		

	
	console.log(dci_overwrite);
	
	$.ajax({
	    url: 'dciTemplateSave.do',
	    type: 'POST',
	    traditional : true,
	    beforeSend:function(){
			$('.pop_indexReg').prepend(progressbar);
        },
		complete:function(){
			$('.pop_indexReg .progressDiv').remove();
		}, 
		xhr: function () {
            //XMLHttpRequest 재정의 가능
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.onprogress = function (e) {
              //progress 이벤트 리스너 추가
              var percent = (e.loaded * 100) / e.total;
              $('body .progressDiv .bar').css('width', percent+'%');
            };
            return xhr;
        }, 
	    data:{
	    	prj_id:getPrjInfo().id,
	    	folder_id:DCIregister.folder_id,
	    	overwriteMode:dci_overwrite,
	    	doc_type:DCIregister.doc_type,
	    	jsonRowDatas:tmpStrings	    	
	    },
	    success: function onData (data) {
	    	console.log(data);
	    	let result = data.model;
	    	//alert(data[0]=="SUCCESS"?"SAVE complete":"SAVE failed> " + data[1]);
	    	alert(result.status=="SUCCESS"?"SAVE complete":"SAVE failed> " + result.error);
	    	
	    	if(result.status=="SUCCESS"){
	    		getPrjDocumentIndexList();
	    		$(".pop_indexReg").dialog("close");
	    		return;
	    	}
	    	// 실패한 항목들 존재시, console로 출력
	    	if(result.error)
	    		for(let i=0;i<result.error.length;i++) console.log(result.error[i]);
	    	
	    	
	    	
	    	
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
}

/*
 * 날짜 유효성 검사
 */
function dci_checkValidDate(value) {
	
	
	const beforeStr = value;
	
	value += "";
	
	let tmpStr = value.substr(0,4) + '-' + value.substr(4,2) + '-' + value.substr(6,2);
	
	// 실제 날짜 값 변환
	var d = new Date(Date.parse(tmpStr));
	const afterStr = d.getFullYear() + ''  
				   + (d.getMonth() + 1 < 10 ? '0' + (d.getMonth() + 1)  : (d.getMonth() + 1) ) 
				   + (d.getDate() < 10 ? ('0' + d.getDate()) : d.getDate());
	
	// console.log('input:'+beforeStr + '|after:' + afterStr);
	
	if(beforeStr != afterStr) {
		console.log('input:'+beforeStr + '|after:' + afterStr);
		return false;
	}
	else return true;
	
	
	/*
	var result = true;
	
	try {
	    var date = value.split("-");
	    var y = parseInt(date[0], 10),
	        m = parseInt(date[1], 10),
	        d = parseInt(date[2], 10);
	    
	    var dateRegex = /^(?=\d)(?:(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29(?=.0?2.(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(?:\x20|$))|(?:2[0-8]|1\d|0?[1-9]))([-.\/])(?:1[012]|0?[1-9])\1(?:1[6-9]|[2-9]\d)?\d\d(?:(?=\x20\d)\x20|$))?(((0?[1-9]|1[012])(:[0-5]\d){0,2}(\x20[AP]M))|([01]\d|2[0-3])(:[0-5]\d){1,2})?$/;
	    result = dateRegex.test(d+'-'+m+'-'+y);
	} catch (err) {
		result = false;
	}    
    return result;
    */
    
}

/**
 * 필수입력란 미입력 및 날짜형식 확인
 * @returns
 */
function dci_chkData(){
	let gridData = DCIregister.grid.getData();
	let mustInput = ["doc_no",
				 	 "title",
					 "doc_type",
					 "category_cd",
					 "size_code",
					 "designer_id"];
	//yyyyMMdd 형태이며 MM은 00~12, dd는 01~31
	let dateReg = /^\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$/;
	
	// step필드 추가
	if(DCIregister.stepField)
	for(let i=0;i<DCIregister.stepField.length;i++)
			mustInput.push(DCIregister.stepField[i]);
	
	console.log(gridData[0][mustInput[5]]);
	
	for(let i=0;i<gridData.length;i++){
		let row = gridData[i];
		for(let col=0;col<mustInput.length;col++){
			if(!row[mustInput[col]]){
				
				// MCI 타입은 category 코드 입력하지 않아도 됨(없는필드)
				if(DCIregister.doc_type == "MCI" || DCIregister.doc_type == 'PCI'){
					if(mustInput[col] == "category_cd"){
						continue;
					}else if(mustInput[col] == "size_code"){
						continue;
					}else if(mustInput[col] == "doc_type"){
						continue;
					}
				}
				
				
				console.log(row[mustInput[col]]);
				alert("Required field["+ mustInput[col] +"] not entered.");
				return false;
			}
			// step 데이트의 경우 날짜형식 체크
			if(col>5){
				if(!dateReg.test(row[mustInput[col]])){
					alert("The field["+ mustInput[col] +"] date format error");
					return false;
				}
				if(!dci_checkValidDate(row[mustInput[col]])){
					alert("The field["+ mustInput[col] +"] date invalid error");
					return false;
				}
				
			}
				
				
			}
		}
	
	return true;			
}
