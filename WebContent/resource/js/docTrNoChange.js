var noChangeTable;
var prj_grid_noChange;
var select_prj_id_noChange;
var selectRadioValue = 'DOC';
$(function(){
	$('.date').datepicker({dateFormat: 'yy-mm-dd'});

	searchNoChange();
	
	var now = new Date();
	let threeMonthAgo = new Date(now.setMonth(now.getMonth() - 3));	//세달 전
	$('#noChangeFrom').datepicker('setDate', threeMonthAgo);
	$('#noChangeTo').datepicker('setDate', new Date());

	$("input:radio[name=docno_trno]").click(function(){
		$('#old_doc_no').val('');
		$('#change_doc_no').val('');
		$('#old_tr_no').val('');
		$('#change_tr_no').val('');
		if($("input:radio[name=docno_trno]:checked").val()==="doc_no") {
			$('#doc_no_tbl').css('display','block');
			$('#tr_no_tbl').css('display','none');
			selectRadioValue = 'DOC';
		}else if($("input:radio[name=docno_trno]:checked").val()==="tr_no") {
			$('#doc_no_tbl').css('display','none');
			$('#tr_no_tbl').css('display','block');
			selectRadioValue = 'TR';
		}else if($("input:radio[name=docno_trno]:checked").val()==="vtr_no") {
			$('#doc_no_tbl').css('display','none');
			$('#tr_no_tbl').css('display','block');
			selectRadioValue = 'VTR';
		}else if($("input:radio[name=docno_trno]:checked").val()==="str_no") {
			$('#doc_no_tbl').css('display','none');
			$('#tr_no_tbl').css('display','block');
			selectRadioValue = 'STR';
		}
	});
	
	$("input[name=noChangeAll]").click(function(){
		if($("input[name=noChangeAll]:checked").val()==="all") {
			$('#noChangeFrom').datepicker('setDate', '');
			$('#noChangeTo').datepicker('setDate', '');
		}
	});
	initPrjSelectGridNoChange();
});
function docTrNoChg_prjAllSelect(){
	$('#selectNoChangePrjId').val('');
	$('#selected_prj_noChange').html('All Project');
	$(".multi_sel_wrap").removeClass('on');
}
function initPrjSelectGridNoChange() {
	prj_grid_noChange  = new Tabulator("#prj_list_grid_noChange", {
		layout: "fitColumns",
		placeholder:"There is No Data",
		selectable:1,
		index:"prj_id",
		height: 180,
		columns: [			
			{ title:"index",field:"prj_id",visible:false},
			{
				title: "Project No",
				field: "prj_no",
				headerSort:false,
			},
			{
				title: "Project Name",
				field: "prj_nm",
				headerSort:false,
			},
			{
				title: "Project Full Name",
				field: "prj_full_nm",
				headerSort:false,
			},
		]
	}); 
	
	prj_grid_noChange.on("tableBuilt", function(){
		$.ajax({
		    url: 'getPrjGridList.do',
		    type: 'POST',
		    success: function onData (data) {
		    	prj_grid_noChange.setData(data.model.prjList);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	});
	
	prj_grid_noChange.on("rowClick",function(e,row){
		let rowData = row.getData();
		$("#selectNoChangePrjId").val(rowData.prj_id);
		$("#selected_prj_noChange").html(rowData.prj_nm);
		$(".multi_sel_wrap").removeClass('on');
	});
	
	// 프로젝트 검색 창 엔터 누를 때의 이벤트 처리	
	$(document).keydown(function(event) {
		 // 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if(document.getElementById('prj_search_noChange') == focusEle){
			 if ( event.keyCode == 13 || event.which == 13 ) {
				 prjSearchNoChange();
			 }
		}
	});
	
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjSearchNoChange(){
	let keyword = $("#prj_search_noChange").val();
	let grid = prj_grid_noChange;
	
	$.ajax({
	    url: 'prjSearch.do',
	    type: 'POST',
	    data:{
	    	keyword : keyword
	    },
	    success: function onData (data) {
	    	grid.setData(data.model.prjList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function searchNoChange() {
	let prj_id=$('#selectNoChangePrjId').val();
	let from=$('#noChangeFrom').val().replaceAll('-','');
	let to=$('#noChangeTo').val().replaceAll('-','');

	if($("input:radio[name=docno_trno]:checked").val()==="doc_no") {
		$.ajax({
		    url: 'getDocNoChgHist.do',
		    type: 'POST',
		    data:{
		    	prj_id:prj_id,
		    	from:from,
		    	to:to
		    },
		    success: function onData (data) {
		    	let changeHis = [];
		    	for(i=0;i<data[0].length;i++){
		    		changeHis.push({
			            log_date: getDateFormat(data[0][i].log_date),
			            change_user: data[0][i].reg_id,
			            old_doc_no: data[0][i].old_doc_no,
			            chg_doc_no: data[0][i].chg_doc_no
		    		});
		    	}
		    	setDocNoChangeHisList(changeHis);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}else{
		$.ajax({
		    url: 'getTrNoChgHist.do',
		    type: 'POST',
		    data:{
		    	prj_id:prj_id,
		    	from:from,
		    	to:to,
		    	tr_type:selectRadioValue
		    },
		    success: function onData (data) {
		    	let changeHis = [];
		    	for(i=0;i<data[0].length;i++){
		    		changeHis.push({
			            log_date: getDateFormat(data[0][i].log_date),
			            change_user: data[0][i].reg_id,
			            old_tr_no: data[0][i].old_tr_no,
			            chg_tr_no: data[0][i].chg_tr_no,
			            i_tr_no: data[0][i].i_tr_no
		    		});
		    	}
		    	setTrNoChangeHisList(changeHis);
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}

function setDocNoChangeHisList(changeHis) {
	var table = new Tabulator("#noChgHisTable", {
        data: changeHis,
        layout: "fitColumns",
        placeholder:"No Data Available",
        columns: [{
                title: "LOG DATE",
                field: "log_date"
            },
            {
                title: "CHANGE USER",
                field: "change_user",
                hozAlign: "left"
            },
            {
                title: "OLD DOCUMENT NO",
                field: "old_doc_no",
                hozAlign: "left"
            },
            {
                title: "CHANGE DOCUMENT NO",
                field: "chg_doc_no",
                hozAlign: "left"
            }
        ]
    });
}
function setTrNoChangeHisList(changeHis) {
	var table = new Tabulator("#noChgHisTable", {
        data: changeHis,
        layout: "fitColumns",
        placeholder:"No Data Available",
        columns: [{
                title: "LOG DATE",
                field: "log_date"
            },
            {
                title: "CHANGE USER",
                field: "change_user",
                hozAlign: "left"
            },
            {
                title: "OLD TRANSMITTAL NO",
                field: "old_tr_no",
                hozAlign: "left"
            },
            {
                title: "CHANGE TRANSMITTAL NO",
                field: "chg_tr_no",
                hozAlign: "left"
            },
            {
                title: "ITRANSMITTAL NO",
                field: "i_tr_no",
                hozAlign: "left"
            }
        ]
    });
}

function docno_change(){
	let prj_id=$('#selectNoChangePrjId').val();
	if(prj_id===''){
		alert('Project를 선택해주세요.');
		return;
	}
	let old_doc_no=$('#old_doc_no').val();
	let change_doc_no=$('#change_doc_no').val();
	$.ajax({
	    url: 'changeDocNo.do',
	    type: 'POST',
	    data:{
	    	prj_id:prj_id,
	    	old_doc_no:old_doc_no,
	    	change_doc_no:change_doc_no,
	    	doc_no:change_doc_no
	    },
	    success: function onData (data) {
	    	alert(data[0]);
	    	searchNoChange();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function trno_change(){
	let prj_id=$('#selectNoChangePrjId').val();
	if(prj_id===''){
		alert('Project를 선택해주세요.');
		return;
	}
	let old_tr_no=$('#old_tr_no').val();
	let change_tr_no=$('#change_tr_no').val();
	let tr_table;
	if(selectRadioValue==='TR'){
		tr_table = 'p_prj_tr_info';
	}else if(selectRadioValue==='VTR'){
		tr_table = 'p_prj_vtr_info';
	}else if(selectRadioValue==='STR'){
		tr_table = 'p_prj_str_info';
	}
	$.ajax({
	    url: 'changeTrNo.do',
	    type: 'POST',
	    data:{
	    	prj_id:prj_id,
	    	old_tr_no:old_tr_no,
	    	change_tr_no:change_tr_no,
	    	tr_table:tr_table,
	    	tr_type:selectRadioValue
	    },
	    success: function onData (data) {
	    	alert(data[0]);
	    	searchNoChange();
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}