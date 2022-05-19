/* 
 * 작성자		: 권도일
 * 작성일		: 2021-11-22
 * 메소드명	: home.js
 * 설명		: home.jsp 제어 스크립트
 */


var bulletinBoard,delayDocSummary,todoSummary,drnSummary,trStatusSummary,crStatusSummary;

var bbs_seq;
var prj_id = $('#selectPrjId').val(); 
var PrjHometable;
var outProcessType = '';
var selectPrjId = '';
var prj_id_Click = '';
var isFromHome = false;
var isCrDelay = false;
var filter_date = '';
    


    $(document).ready(function() {
    	
    	
    	/**
    	 * 로그인한 유저의 권한에 따른 처리 
    	 */
    	
    	if(prj_id == '-1') {
    		$("#authLabel")[0].innerText = 'There is no permission to read for this PROJECT';
       		$("#authLabel").css("display","");
       		$("#homeDiv").css("display","none");
    		return;
    	}
    	
    	let authChk = true;
       	$.ajax({
    	    url: 'getFolderList.do',
    	    type: 'POST',
    	    async:false,
    	    data:{
    	    	prj_id: prj_id
    	    },
    	    success: function onData (data) {
    	    	if(data[0].length < 1) authChk = false;
    	    	
    	    },
    	    error: function onError (error) {
    	        console.error(error);
    	    }
    	});
    	
       	if(!authChk){
       		$("#authLabel")[0].innerText = 'There is no permission to read for this PROJECT';
       		$("#authLabel").css("display","");
       		$("#homeDiv").css("display","none");
       		return;
       	}
    	
    	init(); // 그리드 생성
    	
    	$(".pop_detailProject_home").dialog({
            draggable: true,
            autoOpen: false,
            maxWidth:1000,
            width:"auto",
        }).bind('dialogclose', function(event, ui) {
        	
        	$('#detailViewFileWrap_home').empty();        	
        	getprjNhome();
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
    	
    	getprjNhome();
    	
    });
    
 // 게시판 상세 페이지 닫기 버튼
    function cancledetail_Project_Home() {
    	$(".pop_detailProject_home").dialog("close");
    	$('#detailViewFileWrap_home').empty();
    	
    	getprjNhome();
    }
    
    function getprjNhome() {
    	
    	$.ajax({
    	    url: 'getHomeCont.do',
    	    type: 'POST',
    	    data:{
    	    	prj_id: prj_id
    	    },
    	    success: function onData (data) {
    	    	getBbs(data[0]);
    	    },
    	    error: function onError (error) {
    	        console.error(error);
    	    }
    	});
    }
    
    var bulletinData = [];
    function getBbs(data){   
        let bulletin = [];   
    	for(let i=0;i<data.length;i++){
    		var tmp = data[i];
    		var tmp_Date = tmp.reg_date;
    		tmp_Date = (tmp_Date.substring(0,4)+"-"+tmp_Date.substring(4,6)+"-"+tmp_Date.substring(6,8)+" "+tmp_Date.substring(8,10)+":"+tmp_Date.substring(10,12)+":"+tmp_Date.substring(12));
    		var code_nm = tmp.code_nm;
    		var creater_nm;
    		if(code_nm == null) {
    			creater_nm = tmp.user_kor_nm;
    		}else {
    			creater_nm = tmp.user_kor_nm+'('+code_nm+')';
    		}
    		var bbsRow = {
    			no	: (data.length+1)-(i+1),
    			creater:creater_nm,
    			click:tmp.hit_cnt ? tmp.hit_cnt : "-",
    			creationDate:tmp_Date ? tmp_Date : "-",
    			title:tmp.title ? tmp.title : "-",
    			prj_id:tmp.prj_id,
    			bbs_seq:tmp.bbs_seq
    		}
    		
    		bulletin.push(bbsRow);
    	}
    	bulletinData = bulletin;
    	bulletinBoard.setData(bulletin);
    }
   
    var delayData;
    var todoData;
    var drnData;
    var trData;
    var crData;
    

    function init(){
    	
    	// summary 데이터들을 구함
    	$.ajax({
			url: 'getSummarys.do',
			type: 'POST',
			async:false,
			data: {
				prj_id: prj_id
			},
			success: function onData (data) {
		    	
				todoData = data.model.todoList;
				todoData = setUpTodoData(todoData);
				
				drnData = data.model.drnList;
				drnData = setUpDrnData(drnData);
				
		    	trData = data.model.tr;
		    	trData = setUpTrData(trData);
		    	
		    	crData = data.model.corr;
		    	crData = setUpCorrData(crData);
		    	
		    	
		    	
			},
		    error: function onError (error) {
		        console.error(error);
		    }
		});
    	

    	
    	
    	
    	bulletinBoard	= mkBulletinBoard();
    	todoSummary = mkTodoSummary();
    	drnSummary = mkDrnSummary();
    	trStatusSummary = mkTrStatusSummary();
    	crStatusSummary = mkCrStatusSummary();
    }

    function setUpTodoData(data){
    	
    	
    	let output = [];
    	
    	let eng = data[0];
    	eng = eng.filter(item => item.prj_id == prj_id);
    	//eng = eng.filter(item => item.delay_days <= 7);
    	
    	let vendor = data[1];
    	vendor = vendor.filter(item => item.prj_id == prj_id);
    	//vendor = vendor.filter(item => item.delay_days <= 7);
    	
    	let site =data[2];
    	site = site.filter(item => item.prj_id == prj_id);
    	//site = site.filter(item => item.delay_days <= 7);
    	
    	let pro = data[3];
    	pro = pro.filter(item => item.prj_id == prj_id);
    	//pro = pro.filter(item => item.delay_days <= 7);
    	
    	for(let i=0;i<eng.length;i++){
    		let tmp = eng[i];
    		if(tmp.delay_days >= 1)
    			tmp.status = "R"; // 붉은색
    		else if(tmp.delay_days >= -3)
    			tmp.status = "Y"; // 황색
    		else
    			tmp.status = "G"; // 녹색
    		
    		output.push(tmp);
    	}
    		
    	
    	for(let i=0;i<vendor.length;i++){
    		let tmp = vendor[i];
    		if(tmp.delay_days >= 1)
    			tmp.status = "R"; // 붉은색
    		else if(tmp.delay_days >= -3)
    			tmp.status = "Y"; // 황색
    		else
    			tmp.status = "G"; // 녹색
    		output.push(tmp);
    	}
    	

    	for(let i=0;i<site.length;i++){
    		let tmp = site[i];
    		if(tmp.delay_days >= 1)
    			tmp.status = "R"; // 붉은색
    		else if(tmp.delay_days >= -3)
    			tmp.status = "Y"; // 황색
    		else
    			tmp.status = "G"; // 녹색
    		output.push(tmp);
    	}
    	
    	
    	for(let i=0;i<pro.length;i++){
    		let tmp = pro[i];
    		if(tmp.delay_days >= 1)
    			tmp.status = "R"; // 붉은색
    		else if(tmp.delay_days >= -3)
    			tmp.status = "Y"; // 황색
    		else
    			tmp.status = "G"; // 녹색
    		output.push(tmp);
    	}
    	
    		
    		
    	return output;
    	
    }
    
    function setUpDrnData(data){
    	
    	let output = [];

    	for(let i=0;i<data.length;i++){
    		let tmp = data[i];
    		tmp.designed = data[i].designed + (data[i].type ? " " + data[i].type : "");
    		tmp.status = ( (data[i].drnStatus.name ? data[i].drnStatus.name : tmp.designed) 
    					+ (data[i].drnStatus.code ? " " + data[i].drnStatus.code : "") ) 
    					+ "@" + (data[i].drnStatus.status ? data[i].drnStatus.status : "");
    		
    		
    		output.push(tmp);
    		
    	}
    	
    	// drn_id를 키로하는 중복제거
    	/*
    	output = output.filter((targetObj, idx, arr)=>{
    	    return arr.findIndex((item) => item.drn_id === targetObj.drn_id && item.prj_id === targetObj.prj_id) === idx
    	});
    	*/
    	
    	
    	return output;
    	
    	
    }
    
    function setUpCorrData(data){
    	
    	
    	let output = [];
    	
    	let crOut_L = data["L_OUT"];
    	crOut_L.inOut = "OUT";
    	crOut_L.crType = "LETTER";
    	output.push(crOut_L);
    	
    	let crIn_L = data["L_IN"];
    	crIn_L.inOut = "IN";
    	crIn_L.crType = "LETTER";
    	output.push(crIn_L);
    	
    	let crOut_E = data["E_OUT"];
    	crOut_E.inOut = "OUT";
    	crOut_E.crType = "E-MAIL";
    	
    	output.push(crOut_E);
    	
    	let crIn_E = data["E_IN"];
    	crIn_E.inOut = "IN";
    	crIn_E.crType = "E-MAIL";
    	output.push(crIn_E);
    	

    	
    	return output;
    }
    
    function setUpTrData(data){
    	let output = [];
    	
    	let trOut = data["TR-OUT"];
    	trOut.trType = 'TRANSMITTAL';
    	trOut.inOut = "OUT";
    	output.push(trOut);
    	
    	let trIn = data["TR-IN"];
    	trIn.trType = 'TRANSMITTAL';
    	trIn.inOut = "IN";
    	output.push(trIn);

    	let vtrOut = data["VTR-OUT"];
    	vtrOut.trType = 'VTRANSMITTAL';
    	vtrOut.inOut = "OUT";
    	output.push(vtrOut);
    	
    	let vtrIn = data["VTR-IN"];
    	vtrIn.trType = 'VTRANSMITTAL';
    	vtrIn.inOut = "IN";
    	output.push(vtrIn);
    	
    	
    	let strOut = data["STR-OUT"];
    	strOut.trType = 'STRANSMITTAL';
    	strOut.inOut = "OUT";
    	output.push(strOut);
    	
    	let strIn = data["STR-IN"];
    	strIn.trType = 'STRANSMITTAL';
    	strIn.inOut = "IN";
    	output.push(strIn);
    	
    	return output;
    }
    
    
    
    
    
 function mkTodoSummary(){
    	
    	var table = new Tabulator("#todoSummary", {
    		layout: "fitColumns",
    		data: todoData,
    		placeholder:"No Data Set",
    		/*height:145,*/
    		height:"100%",
    		columns: [{
    		        title: "DOCUMENT NO",
    		        field: "doc_no",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow:2,
    		        hozAlign: "left"
    		    },
    		    {
    		        title: "DOCUMENT TITLE",
    		        field: "title",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow:5,
    		        hozAlign: "left"
    		    },
    		    {
    		        title: "REV<br>NO",
    		        field: "rev_no",
    		        widthGrow:1
    		    },
    		    {
    		        title: "DISCIPLINE",
    		        field: "discipline",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow:2,
    		        hozAlign: "left"
    		    },
    		    {
    		        title: "DESIGNER",
    		        field: "designer",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow:2
    		    },
    		    {
    		        title: "STEP",
    		        field: "step",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow:1
    		    },
    		    {
    		        title: "FORECAST<br>DATE",
    		        field: "fore_date",
    		        formatter: function(cell, formatterParams, onRendered){
    		        	let out = cell.getValue();
    		        	out = out.substr(0,4) + "-" + out.substr(4,2) + "-" + out.substr(6,2);
    		        	
    		        	return out;
    		        },
    		        widthGrow:1.5
    		    },
    		    {
    		        title: "DELAY DAYS",
    		        field: "delay_days",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow:1,
    		        formatter : function(cell, formatterParams, onRendered){
     		           let delay_days = cell.getValue();
     		           let out = document.createElement("p");
     		           out.innerText = delay_days;
     		           
     		           if(delay_days>0)
     		        	  out.style["color"] = "red";
     		           else
     		        	  out.style["color"] = "black";
     		           
     		           return out;
     		        }
    		    },
    		    {
    		        title: "STATUS",
    		        field: "status",
    		        widthGrow:1,
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        formatter : function(cell, formatterParams, onRendered){
    		           let status = cell.getValue();
    		           let out = document.createElement("p");
    		           out.innerText = "●";
    		           
    		           
    		           switch(status){
	    		           case "R":
	    		        	   out.style["color"] = "red";
	    		        	   break;
	    		           case "Y":
	    		        	   out.style["color"] = "orange";
	    		        	   break;
	    		           case "G":
	    		        	   out.style["color"] = "green";
	    		        	   break;
    		           }
    		           
    		           return out;
    		        }
    		    },
    		],
    		});
    	
    	
    	table.on("rowClick", function(e, row){
    		
    		let data = row.getData();
    		let homeDocAuth = checkFolderAuth(data.prj_id,Number(data.folder_id));
    		if(homeDocAuth.r != 'Y'){
    			alert("You don't have access to this document.Please contact DCC")
    			return;
    		}
    		
    		let params = {};
    		$.ajax({
    			url: 'getTodoPageVar.do',
    			type: 'POST',
    			traditional : true,
    			async: false,
    			data:{
    				prj_id: data.prj_id,
    				folder_id: Number(data.folder_id),
    				doc_id:data.doc_id,
    				rev_id:data.rev_id
    			},
    			success: function onData (data) {
    				params = data.model;
    		    },
    		    error: function onError (error) {
    		        console.error(error);
    		    }
    		});
    		
    		$('#session_user_id').val(session_user_id);
    		$('#session_user_eng_nm').val(session_user_eng_nm);
    		$('#current_folder_discipline_code').val(params.discip ? params.discip.discip_code : "");
    		$('#current_folder_discipline_code_id').val(params.discip ? params.discip.discip_code_id : "");
    		$('#current_folder_discipline_display').val(params.discip ? params.discip.discip_display : "");
    		$('#current_folder_id').val(data.folder_id);
    		$('#current_folder_path').val(params.folder_path);
    		$('#process').val('property');
    		$('#doc_type').val(params.doc_type);
    		$('#DCCorTRA').val("DCC");
    		$('#popup_new_or_update').val("update");
    		$('#vendor_new_or_update').val("update");
    		$('#proc_new_or_update').val("update");
    		$('#site_new_or_update').val("update");
    		$('#corr_new_or_update').val("update");
    		$('#general_new_or_update').val("update");
    		$("#doc_id").val(data.doc_id);
    		$("#rev_id").val(data.rev_id);
    		let useDRNYN = useDrnCheckForThisFolder(data.prj_id,Number(data.folder_id));
    		$('#useDRNYN').val(useDRNYN);
    		
    		
    		
    		switch(params.doc_type){
    			case 'DCI':
    				let engDocPopup = window.open('./pop/engDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
    				break;
    			case 'VDCI':
    				vendorDocPopup = window.open('./pop/vendorDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
    				break;
    			case 'PCI':
    				let procDocPopup = window.open('./pop/procDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
    				break;
    			case 'SDCI':
    				let siteDocPopup = window.open('./pop/siteDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
    				break;
    			case 'LETTER':
    			case 'E-MAIL':
    				let corrDocPopup = window.open('./pop/corrDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
    				break;
    			default:
    				let generalDocPopup = window.open('./pop/generalDocPopup.jsp','_blank','top=10, left=10, width=1200, height=1000, resizable=yes');
    		}
    		
    		
    	});
    	
    	
    	
    	return table;
    }
 
 function useDrnCheckForThisFolder(prj_id,folder_id){
		
		let useDrn = 'N';
		$.ajax({
			url: 'useDrnCheckForThisFolder.do',
			type: 'POST',
			traditional : true,
			async: false,
			data:{
				prj_id: prj_id,
				folder_id: folder_id
			},
			success: function onData (data) {
				useDrn = data;
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
		
		return useDrn;
	}
 
 function mkDrnSummary(){
 
 	
 	var table = new Tabulator("#drnSummary", {
 		layout: "fitColumns",
 		data: drnData,
 		placeholder:"No Data Set",
 		maxHeight:150,
 		columns: [
 			{
		        title: "DOCUMENT NO",
		        field: "doc_no",
		        widthGrow:19,
		        hozAlign: "left"
		    },
		    {
		        title: "DOCUMENT TITLE",
		        field: "title",
		        widthGrow:47,
		        hozAlign: "left"
		    },
		    {
		        title: "REV NO",
		        field: "revision",
		        widthGrow:9
		    },
		    {
		        title: "DISCIPLINE",
		        field: "discip_display",
		        widthGrow:19	        	
		    },
		    {
		        title: "DESIGNER",
		        field: "designed",
		        widthGrow:14
		    },
		    {
		        title: "DRN DRAFT DATE",
		        field: "reg_date",
		        widthGrow:16,
		        formatter: function(cell, formatterParams, onRendered){
		        	let out = cell.getValue();
		        	out = out.substr(0,4) + "-" + out.substr(4,2) + "-" + out.substr(6,2);
		        	
		        	return out;
		        }
		    },
		    {
		        title: "DRN STATUS",
		        field: "status",
		        widthGrow:19,
		        formatter : function(cell, formatterParams, onRendered){
  		           const data = cell.getValue().split("@");

  		           let out = document.createElement("div");
  		           	out.style["display"] = "flex";
  		           let leftDiv = document.createElement("div");
  		           	leftDiv.innerText = data[0];
  		           	leftDiv.style["margin"] = "auto";
  		           	leftDiv.style["width"] = "60%";
  		           let centerDiv = document.createElement("div");
  		           	centerDiv.style["border"] = "0.1px solid rgb(225,225,225)"
  		           let rightDiv = document.createElement("div");
  		           	rightDiv.innerText = data[1];
  		           	rightDiv.style["margin"] = "auto";
  		           	rightDiv.style["width"] = "40%";
  		           out.append(leftDiv,centerDiv,rightDiv);
  		   
  		           
  		           return out;
  		        }
		    },
 		],
 		});
 	
 	
 	table.on("rowClick", function(e, row){
 	    //e - the click event object
 	    //row - row component
 		let rowData = row.getData();
 		
 		$.ajax({
			type: "POST", 
			url: 'get_drnVar.do',
			async:false,
			data:{
				prj_id: rowData.prj_id,
				drn_id: rowData.drn_id
			},
			success: function onData (data) {
				rowData.approval_id = Number(data.model.approval_id);
				rowData.check_sheet_id = data.model.check_sheet_id;
				rowData.folder_id = data.model.folder_id;
				rowData.status = data.model.status;
				rowData.drn_no = data.model.drn_no;
				rowData.check_sheet_title = data.model.check_sheet_title;
			},
			error: function onError (error) {
		        console.error(error);
		    }
		});
 		
 		
 		$.ajax({
			type: "POST", 
			url: 'get_drnPage.do',
			data:{
				jsonData:JSON.stringify(rowData)
			},
			success: function onData (data) {
				let discipObj = getDiscipObj(rowData.folder_id);
				
				
		    	let docData = data.model.docData;
		    	let pageData = data.model;
		    	drnPopUP("read" , docData , discipObj , rowData.folder_id, pageData);

			},
			error: function onError (error) {
		        console.error(error);
		    }
		});
 		
 	});
 	
 	
 	
 	return table;
 }

    
    
    
    
    
    function mkBulletinBoard(){
    	
    	if(PrjHometable == null || PrjHometable == undefined){
    		PrjHometable = new Tabulator("#bulletinBoard", {
        		layout: "fitColumns",
        		data:bulletinData,
        		placeholder:"No Data Set",		
        		columns: [{
    	                title: "NO",
    	                field: "no",
    	                width: 80
    	            },
    	            {
    	                title: "CREATOR",
    	                field: "creater",
    	                width: 100,
    	                hozAlign: "left"
    	            },
    	            {
    	                title: "CLICK",
    	                field: "click",
    	                width: 80
    	            },
    	            {
    	                title: "CREATION DATE",
    	                field: "creationDate",
    	                width: 135
    	            },
    	            {
    	                title: "TITLE",
    	                field: "title",
    	                hozAlign: "left"
    	            }
        		],
        		});
        	PrjHometable.on("rowClick", function(e, row){
            	$('#detailViewFileWrap_home').empty();
        		$(".pop_detailProject_home").dialog("open");
        		
        		
        		var clickTb = row.getData();
        		bbs_seq = clickTb.bbs_seq;
        		prj_id_Click = clickTb.prj_id;
        		
        		$.ajax({
        			url: 'getPrjNoticeBoard.do',
        			type: 'POST',
        			data: {
        				bbs_seq: bbs_seq,
        				prj_id: prj_id_Click
        			},
        			success: function onData (data) {
        				var code_nm = data[0].code_nm;
        				var creater_nm;
        	    		if(code_nm == null) {
        	    			creater_nm = data[0].user_kor_nm;
        	    		}else {
        	    			creater_nm = data[0].user_kor_nm+'('+code_nm+')';
        	    		}
        				$('#project_title_home').val(data[0].title); 
        				$('#project_writer_home').val(creater_nm);
        				$('#project_createdate_home').val(getDateFormat(data[0].reg_date));
        				$('#project_hits_home').val(data[0].hit_cnt);
        				$('#project_contents_home').html(data[0].contents);
        				
        				detailPrjFileList_Home();
        			},
        		    error: function onError (error) {
        		        console.error(error);
        		    }
        		});
        	});
    	}else {
    		PrjHometable.replaceData(bulletinData);
    	}
    	
    	return PrjHometable;
    }
    
    // 등록된 파일 리스트
    function detailPrjFileList_Home() {
    	$.ajax({
    		url: 'getDetailPrjFileList.do',
    	    type: 'POST',    // 이동할때 포스트 방식으로 이동
    	    data:{				// 이동할때 챙겨야하는 데이터
    	    	prj_id: prj_id_Click,
    	    	bbs_seq: bbs_seq
    	    },
    	    success: function onData (data) {
    	    	
    	    	var html = "";
    	    	
    	    	if(data[0].length == 0 || data[0][0] == null) {
    	    		html += "<div id=\"emptyFile\">첨부 파일이 없습니다.</div>";
    	    		
    	    		$("#detailViewFileWrap_home").append(html);
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
    		    		
    		    		html += "<ul class=\"file-ul detail\">";
    		    		html += "<li title='"+fileName+"'style=\"width:88%\"><span onclick=\"fileDownload('"+data[0][i].sfile_nm+"','"+fileName+"');\"><i class=\"far fa-file\"></i>"+fileName+"</span></li>";
    		    		html += "<li>"+fileSizeTxt+"</li>";
    		    		html += "</ul>";
    		    	}
    		    	
    		    	$("#detailViewFileWrap_home").append(html);
    	    	}
    	    },
    	    error: function onError (error) {
    	        console.error(error);
    	    }
    	});
    }
    
    function fileDownload(sfileName, fileName) {
    	var $form = $('<form></form>');
    	var sfileName = $('<input type="hidden" name="fullPath" value="' + sfileName + '">');
    	var fileName = $('<input type="hidden" name="fileName" value="' + fileName + '">');
    	
    	
    	$form.attr('action', 'estimateDownload.do');
    	$form.attr('method', 'post');
    	$form.appendTo('body');
    	$form.append(sfileName);
    	$form.append(fileName);
    	
    	$form.submit();
    }
    
    
    function getDateStrFromDate(timeDate){
    	let year = timeDate.getFullYear(); 
    	let month = timeDate.getMonth() + 1;
    	let date = timeDate.getDate();
    	let hour = timeDate.getHours();
    	let min = timeDate.getMinutes();
    	let sec = timeDate.getSeconds();
    	
    	let dateString = year + 
    					(month + "").padStart(2,0) +
    					(date + "").padStart(2,0) +
    					(hour + "").padStart(2,0) +
    					(min + "").padStart(2,0) +
    					(sec + "").padStart(2,0);
    	
    	return dateString;
    }

    function mkDelayDocSummary(){
    	
    	let now = new Date();	// 현재 날짜 및 시간
    	let oneMonthAgo = new Date(now.setMonth(now.getMonth() - 1));	// 한달 전
    	oneMonthAgo = getDateStrFromDate(oneMonthAgo);
    	
    	now = new Date();
    	let oneWeeksAgo = new Date(now.setDate(now.getDate() - 7));	// 한달 전
    	oneWeeksAgo = getDateStrFromDate(oneWeeksAgo);
    	now = genDateStr();
    	
    	let oneMonth = "(" + oneMonthAgo.substr(4,2) + "/" + oneMonthAgo.substr(6,2) + "~" + now.substr(4,2) + "/" + now.substr(6,2) + ")";
    	let oneWeeks = "(" + oneWeeksAgo.substr(4,2) + "/" + oneWeeksAgo.substr(6,2) + "~" + now.substr(4,2) + "/" + now.substr(6,2) + ")";

    	
    	var table = new Tabulator("#delaySummary", {
    		layout: "fitColumns",
    		data: delayData,
    		placeholder:"No Data Set",
    		columns: [{
    		        title: "IN/OUT",
    		        field: "inOut"
    		    },
    		    {
    		        title: "ONE MONTH" + oneMonth,
    		        field: "oneMonth",
    		        sorter: "date"
    		    },
    		    {
    		        title: "ONE WEEK" + oneWeeks,
    		        field: "oneWeek",
    		        sorter: "date"
    		    },
    		    {
    		        title: "TODAY" + ("(" + now.substr(4,2) + "/" + now.substr(6,2) + ")"),
    		        field: "today",
    		        sorter: "date"
    		    },
    		    {
    		        title: "DELAY (ONE WEEK)",
    		        field: "delay"
    		    }
    		],
    		});
    	return table;
    }

    function mkTrStatusSummary(){
    	
    	let now = new Date();	// 현재 날짜 및 시간
    	let oneMonthAgo = new Date(now.setMonth(now.getMonth() - 1));	// 한달 전
    	oneMonthAgo = getDateStrFromDate(oneMonthAgo);
    	
    	now = new Date();
    	let oneWeeksAgo = new Date(now.setDate(now.getDate() - 7));	// 일주일
    	oneWeeksAgo = getDateStrFromDate(oneWeeksAgo);
    	
    	now = new Date();
    	let yesterDay = new Date(now.setDate(now.getDate() - 1));	// 하루전
    	yesterDay = getDateStrFromDate(yesterDay);
    	
    	
    	now = genDateStr();
    	
    	let oneMonth = "(" + oneMonthAgo.substr(4,2) + "/" + oneMonthAgo.substr(6,2) + "~" + now.substr(4,2) + "/" + now.substr(6,2) + ")";
    	let oneWeeks = "(" + oneWeeksAgo.substr(4,2) + "/" + oneWeeksAgo.substr(6,2) + "~" + now.substr(4,2) + "/" + now.substr(6,2) + ")";
    	
    	
    	var table = new Tabulator("#trStatusSummary", {
    		layout: "fitColumns",
    		data: trData,
    		placeholder:"No Data Set",	
    		columns: [{
    		        title: "TR TYPE",
    		        field: "trType",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow : 2
    		    },
    		    {
    		        title: "IN/OUT",
    		        field: "inOut",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow : 1
    		    },
    		    {
    		        title: "ONE MONTH" +"<br>" + oneMonth,
    		        field: "oneMonth",
    		        widthGrow : 4,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var trType = clickTb.trType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		if(trType == "TRANSMITTAL") {
    		    			if(inOut == "OUT") {
    		    				
    		    				$("#M_Outradio").prop("checked",true);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', lastMonth());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				outProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
    		    				}
    		    				getTrOutSearch();
    		    				
    		    			}else if(inOut == "IN") {
    		    				
    		    				$("#M_Inradio").prop("checked",true);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', lastMonth());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				getTrInSearch();
    		    			}
    		    		}else if(trType == "VTRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("V트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",true);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', lastMonth());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				outProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    				getTrOutSearch();
    		    				
    		    			}else if(inOut == "IN") {
    		    				
    		    				$("#M_Inradio").prop("checked",true);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', lastMonth());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    				getTrInSearch();
    		    			}
    		    		}else if(trType == "STRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("S트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",true);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', lastMonth());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				outProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				getTrOutSearch();
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("S트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",true);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', lastMonth());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    				getTrInSearch();
    		    			}
    		    		}
    		        }
    		    },
    		    {
    		        title: "ONE WEEK" +"<br>" + oneWeeks,
    		        field: "oneWeek",
    		        sorter: "date",
    		        widthGrow : 4,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var trType = clickTb.trType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		if(trType == "TRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",true);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', lastWeek());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",true);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', lastWeek());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}else if(trType == "VTRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("V트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",true);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', lastWeek());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("V트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",true);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', lastWeek());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}else if(trType == "STRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("S트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",true);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', lastWeek());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("S트랜스미터  인");

    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",true);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', lastWeek());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}
    		        }
    		    },
    		    {
    		        title: "YESTERDAY" +"<br>" + ("(" + yesterDay.substr(4,2) + "/" + yesterDay.substr(6,2) + ")"),
    		        field: "yesterday",
    		        widthGrow : 4,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var trType = clickTb.trType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		var now = new Date();
    		    		var yesterday = new Date(now.setDate(now.getDate() - 1));
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		if(trType == "TRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', yesterday);
    		        	    	$('#out_tr_to').datepicker('setDate', yesterday);
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', yesterday);
    		        	    	$('#in_tr_to').datepicker('setDate', yesterday);
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}else if(trType == "VTRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("V트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', yesterday);
    		        	    	$('#out_tr_to').datepicker('setDate', yesterday);
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("V트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', yesterday);
    		        	    	$('#in_tr_to').datepicker('setDate', yesterday);
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}else if(trType == "STRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("S트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', yesterday);
    		        	    	$('#out_tr_to').datepicker('setDate', yesterday);
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("S트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', yesterday);
    		        	    	$('#in_tr_to').datepicker('setDate', yesterday);
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}
    		        }
    		    },
    		    {
    		        title: "TODAY" + "<br>" + ("(" + now.substr(4,2) + "/" + now.substr(6,2) + ")"),
    		        field: "today",
    		        widthGrow : 4,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var trType = clickTb.trType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		if(trType == "TRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', new Date());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', new Date());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'TR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}else if(trType == "VTRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("V트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', new Date());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("V트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', new Date());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'VTR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}else if(trType == "STRANSMITTAL") {
    		    			if(inOut == "OUT") {
//    		    				console.log("S트랜스미터 아웃");
    		    				
    		    				$("#M_Outradio").prop("checked",false);
    		    				$("#W_Outradio").prop("checked",false);
    		    				$("#A_Outradio").prop("checked",false);
    		    				
    		    				$('#out_tr_from').datepicker('setDate', new Date());
    		        	    	$('#out_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "outgoingTab";
    		    				let jsp_Path = '/page/trOutgoing';
    		    				let type = 'TROutgoing';
    		    				let text ='OUTGOING';
    		    				
    		    				//outProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    				
    		    			}else if(inOut == "IN") {
//    		    				console.log("S트랜스미터  인");
    		    				
    		    				$("#M_Inradio").prop("checked",false);
    		    				$("#W_Inradio").prop("checked",false);
    		    				$("#A_Inradio").prop("checked",false);
    		    				
    		    				$('#in_tr_from').datepicker('setDate', new Date());
    		        	    	$('#in_tr_to').datepicker('setDate', new Date());
    		    				
    		    				let tab_id = "incomingTab";
    		    				let jsp_Path = '/page/trIncoming';
    		    				let type = 'TRIncoming';
    		    				let text ='INCOMING';
    		    				
    		    				inProcessType = 'STR';
    		    				
    		    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
    		    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
    		    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
    		    					tabList[tabIdx].folder_id = current_folder_id;
    		    					
    		    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
    		    					
    		    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
    		    				}
    		    			}
    		    		}
    		        }
    		    }
    		],
    	});
//    	table.on("cellClick", function(e, cell){
////    		console.log("타입 / 인아웃 = "+row.getData().trType+"/"+row.getData().inOut);
//    		var clickTb = cell.getData();
//    		var trType = clickTb.trType;
//    		var inOut = clickTb.inOut;
//    		
//    		if(trType == "TRANSMITTAL") {
//    			if(inOut == "OUT") {
////    				console.log("트랜스미터 아웃");
//    				
//    				let tab_id = "outgoingTab";
//    				let jsp_Path = '/page/trOutgoing';
//    				let type = 'TROutgoing';
//    				let text ='OUTGOING';
//    				
//    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
//    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
//    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
//    					tabList[tabIdx].folder_id = current_folder_id;
//    					
//    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
//    					
//    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
//    				}
//    				
//    			}else if(inOut == "IN") {
////    				console.log("트랜스미터  인");
//    				
//    				let tab_id = "incomingTab";
//    				let jsp_Path = '/page/trIncoming';
//    				let type = 'TRIncoming';
//    				let text ='INCOMING';
//    				
//    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
//    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
//    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
//    					tabList[tabIdx].folder_id = current_folder_id;
//    					
//    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
//    					
//    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
//    				}
//    			}
//    		}else if(trType == "VTRANSMITTAL") {
//    			if(inOut == "OUT") {
//    				console.log("V트랜스미터 아웃");
//    			}else if(inOut == "IN") {
//    				console.log("V트랜스미터  인");
//    			}
//    		}else if(trType == "STRANSMITTAL") {
//    			if(inOut == "OUT") {
//    				console.log("S트랜스미터 아웃");
//    			}else if(inOut == "IN") {
//    				console.log("S트랜스미터  인");
//    			}
//    		}
//    	});
    	return table;
    }
    function mkCrStatusSummary(){
    	
    	let now = new Date();	// 현재 날짜 및 시간
    	let oneMonthAgo = new Date(now.setMonth(now.getMonth() - 1));	// 한달 전
    	oneMonthAgo = getDateStrFromDate(oneMonthAgo);
    	
    	now = new Date();
    	let oneWeeksAgo = new Date(now.setDate(now.getDate() - 7));	// 한달 전
    	oneWeeksAgo = getDateStrFromDate(oneWeeksAgo);
    	now = genDateStr();
    	
    	let oneMonth = "(" + oneMonthAgo.substr(4,2) + "/" + oneMonthAgo.substr(6,2) + "~" + now.substr(4,2) + "/" + now.substr(6,2) + ")";
    	let oneWeeks = "(" + oneWeeksAgo.substr(4,2) + "/" + oneWeeksAgo.substr(6,2) + "~" + now.substr(4,2) + "/" + now.substr(6,2) + ")";

    	
    	var table = new Tabulator("#crStatusSummary", {
    		layout: "fitColumns",
    		data: crData,
    		placeholder:"No Data Set",
    		columns: [
    			{
    		        title: "CR TYPE",
    		        field: "crType",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow: 1
    		    },
    			{
    		        title: "IN/OUT",
    		        field: "inOut",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow: 1
    		    },
    		    {
    		        title: "ONE MONTH" + "<br>" + oneMonth,
    		        field: "oneMonth",
    		        widthGrow: 3,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var crType = clickTb.crType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		let tab_id = "dccTab";
	    				let jsp_Path = '/page/documentControl';
	    				let type = 'DCC';
	    				let text ='Document Control';
    		    		
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		let crInfo = getCRTabInfo(crType,inOut);
    		    		
    		    		current_folder_id = crInfo.cr_folder_id;
	    				
	    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
	    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
	    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
	    					tabList[tabIdx].folder_id = crInfo.cr_folder_id;
	    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
	    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화	
	    				}
    		    		
    		    		$.ajax({
	    				    url: 'setSession.do',
	    				    type: 'POST',
	    				    data: {
	    				    	prj_id:prj_id,
	    				    	folder_id:crInfo.cr_folder_id
	    				    },
	    					async: false,
	    				    success: function onData (data) {
	    				    },
	    				    error: function onError (error) {
	    				        console.error(error);
	    				    }
	    				});
    		    		
    		    			
    		    				
    		    		current_doc_type = "Corr";
    		    		current_folder_id = crInfo.cr_folder_id;
    		    		current_folder_path = crInfo.cr_folder_path;
    		    		isFromHome = true;
    		    		filter_date = oneMonthAgo;
    		        }
    		    },
    		    {
    		        title: "ONE WEEK" + "<br>" + oneWeeks,
    		        field: "oneWeek",
    		        widthGrow: 3,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var crType = clickTb.crType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		let tab_id = "dccTab";
	    				let jsp_Path = '/page/documentControl';
	    				let type = 'DCC';
	    				let text ='Document Control';
    		    		
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		let crInfo = getCRTabInfo(crType,inOut);
    		    		
    		    		current_folder_id = crInfo.cr_folder_id;
	    				
	    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
	    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
	    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
	    					tabList[tabIdx].folder_id = crInfo.cr_folder_id;
	    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
	    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
	    				}
    		    		
    		    		$.ajax({
	    				    url: 'setSession.do',
	    				    type: 'POST',
	    				    data: {
	    				    	prj_id:prj_id,
	    				    	folder_id:crInfo.cr_folder_id
	    				    },
	    					async: false,
	    				    success: function onData (data) {
	    				    },
	    				    error: function onError (error) {
	    				        console.error(error);
	    				    }
	    				});
    		    		
    		    			
    		    				
    		    		current_doc_type = "Corr";
    		    		current_folder_id = crInfo.cr_folder_id;
    		    		current_folder_path = crInfo.cr_folder_path;
    		    		isFromHome = true;
    		    		filter_date = oneWeeksAgo;
    		        }
    		        
    		    },
    		    {
    		        title: "TODAY" + "<br>" + ("(" + now.substr(4,2) + "/" + now.substr(6,2) + ")"),
    		        field: "today",
    		        widthGrow: 3,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var crType = clickTb.crType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		let tab_id = "dccTab";
	    				let jsp_Path = '/page/documentControl';
	    				let type = 'DCC';
	    				let text ='Document Control';
    		    		
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		let crInfo = getCRTabInfo(crType,inOut);
    		    		
    		    		current_folder_id = crInfo.cr_folder_id;
	    				
	    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
	    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
	    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
	    					tabList[tabIdx].folder_id = crInfo.cr_folder_id;
	    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
	    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
	    				}
    		    		
    		    		$.ajax({
	    				    url: 'setSession.do',
	    				    type: 'POST',
	    				    data: {
	    				    	prj_id:prj_id,
	    				    	folder_id:crInfo.cr_folder_id
	    				    },
	    					async: false,
	    				    success: function onData (data) {
	    				    },
	    				    error: function onError (error) {
	    				        console.error(error);
	    				    }
	    				});
    		    		
    		    			
    		    				
    		    		current_doc_type = "Corr";
    		    		current_folder_id = crInfo.cr_folder_id;
    		    		current_folder_path = crInfo.cr_folder_path;
    		    		isFromHome = true;
    		    		filter_date = now;
    		    				
    		    			
    		    		
    		        }
    		    },
    		    {
    		        title: "DELAY",
    		        field: "delay",
    		        titleFormatter:function(cell, formatterParams, onRendered){
    		        	let tmp = document.createElement("div");
    		        	tmp.classList.add('home_grid_header');
    		        	tmp.innerText = cell.getValue();
    		        	return tmp;
    		        },
    		        widthGrow: 3,
    		        cellClick : function ( e , cell ){
    		        	
    		        	var clickTb = cell.getData();
    		    		var crType = clickTb.crType;
    		    		var inOut = clickTb.inOut;
    		    		
    		    		let tab_id = "dccTab";
	    				let jsp_Path = '/page/documentControl';
	    				let type = 'DCC';
	    				let text ='Document Control';
    		    		
    		    		
    		    		selectPrjId = prj_id;
    		    		
    		    		let crInfo = getCRTabInfo(crType,inOut);
    		    		
    		    		current_folder_id = crInfo.cr_folder_id;
	    				
	    				if(addTab(jsp_Path,text,tab_id, type, null)){ // 탭추가
	    					// 탭의 최대 갯수를 초과하지 않는 경우에만 동작
	    					let tabIdx = tabList.findIndex(i => i.id == ("#")+tab_id );
	    					tabList[tabIdx].folder_id = crInfo.cr_folder_id;
	    					tabClose(); // 새로 추가된 탭에 닫기 on클릭 속성부여
	    					tabOn("#"+tab_id, type); // 새로 추가된 탭 활성화
	    				}
    		    		
    		    		$.ajax({
	    				    url: 'setSession.do',
	    				    type: 'POST',
	    				    data: {
	    				    	prj_id:prj_id,
	    				    	folder_id:crInfo.cr_folder_id
	    				    },
	    					async: false,
	    				    success: function onData (data) {
	    				    },
	    				    error: function onError (error) {
	    				        console.error(error);
	    				    }
	    				});
    		    		
    		    			
    		    				
    		    		current_doc_type = "Corr";
    		    		current_folder_id = crInfo.cr_folder_id;
    		    		current_folder_path = crInfo.cr_folder_path;
    		    		isFromHome = true;
    		    		filter_date = now;
    		    		isCrDelay = true;
    		        }
    		       
    		    }
    		],
    		});
    	return table;
    }

    
function showMyTodo(obj){
	let chk = obj.checked;
	if(chk){
		todoSummary.setFilter("designer_id", "=", session_user_id);
	}else{
		todoSummary.setFilter(false);
	}
}


function homeRefresh(){
	$("#todoShowOnlySessionId")[0].checked = false;
	init();
}

function getCRTabInfo(crType,inOut){
	let cr_Result ={};

	
   	$.ajax({
	    url: 'getCRTabInfo.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:prj_id,
	    	crType: crType,
	    	inOut:inOut
	    },
	    success: function onData (data) {
	    	let result = data.model;
	    	
	    	cr_Result.cr_folder_id = result.cr_folder_id;
	    	cr_Result.cr_folder_path = result.cr_folder_path;
	    	
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
   	
   	return cr_Result;
	
	
}

