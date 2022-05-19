$(document).ready(function(){
	var loading = $('<div id="loading" class="loading"></div><img id="loading_img" alt="loading" src="../resource/images/loading.gif" />')
    .appendTo(document.body).hide();

	$(window)	

    .ajaxStart(function(){
    	loading.show();
	})

	.ajaxStop(function(){
		loading.hide();
	});

});
var codeSetTable;
var cordocInfoTable;
var disciplineFolderInfoTable;
var disciplineInfoTable;
var documentIndexTable;
var emailAttachInfoTable;
var emailReceiptInfoTable;
var emailSendInfoTable;
var emailTypeTable;
var emailTypeContTable;
var engInfoTable;
var dataMgEngStepTable;
var folderAuthTable;
var foledrInfoTable;
var gendocInfoTable;
var groupMemberTable;
var infoTable;
var memberTable;
var proInfoTable;
var proStepTable;
var processInfoTable;
var sdcInfoTable;
var sdcStepTable;
var strFileTable;
var strInfoTable;
var systemPrefixTable;
var trFileTable;
var trInfoTable;
var vdrInfotable;
var vdrStepTable;
var vtrFileTable;
var vtrInfoTable;
var wbsCodeTable;
var usersTable;

var selectPrjId = $('#selectPrjId').val();
var dataMigration = {};
var tmpGrid;
var set_table_type;

// 엑셀로부터 불러온 데이터인지 체크하는 플래그
// 엑셀로 데이터 불러오면 true, retrive 혹은 셀렉트박스 선택시 클리어
var isFromExcel = false;
var excelData;

var tableNo = 0;

var prj_grid_dataMigration;

// Project List에서 선택한 프로젝트 id
var dataMigSelectPrj_id = "";

$(function() {
	$(".pop_dataMigSaveConfirm")
			.dialog({
				draggable : true,
				autoOpen : false,
				maxWidth : 1200,
				width : "auto"
			})
			.dialogExtend(
					{
						"closable" : true,
						"maximizable" : true,
						"minimizable" : true,
						"minimize" : function(evt) {
							$(
									".ui-widget.ui-widget-content:not(.ui-datepicker)")
									.addClass("position");
						},
						"maximize" : function(evt) {
							$(".ui-dialog .ui-dialog-content").addClass(
									"height_reset");
							$(this).closest(".ui-dialog").css("height", "100%");
							$(this).closest(".ui-widget.ui-widget-content")
									.addClass("wd100");
						},
						"beforeMinimize" : function(evt) {
							$(".ui-dialog-title").show();
						},
						"beforeMaximize" : function(evt) {
							$(
									".ui-widget.ui-widget-content:not(.ui-datepicker)")
									.removeClass("position");
							$(this).closest(".ui-dialog").css("height", "auto");
							$(".ui-dialog .ui-dialog-content").removeClass(
									"height_reset");
							$(".ui-dialog-title").show();
						},
						"beforeRestore" : function(evt) {
							$(
									".ui-widget.ui-widget-content:not(.ui-datepicker)")
									.removeClass("position");
							$(this).closest(".ui-dialog").css("height", "auto");
							$(".ui-dialog .ui-dialog-content").removeClass(
									"height_reset");
							$(".ui-dialog-title").show();
						},
						"minimizeLocation" : "left"
					});

	$("#tableList_type").val("#sel_table01").prop("selected", true);
	$("#sel_table01").css('display', 'block');
	$("#sel_table02").css('display', 'none');
	$("#sel_table03").css('display', 'none');
	$("#sel_table04").css('display', 'none');
	$("#sel_table05").css('display', 'none');
	$("#sel_table06_1").css('display', 'none');
	$("#sel_table06_2").css('display', 'none');
	$("#sel_table06_3").css('display', 'none');
	$("#sel_table06").css('display', 'none');
	$("#sel_table07").css('display', 'none');
	$("#sel_table08").css('display', 'none');
	$("#sel_table09").css('display', 'none');
	$("#sel_table10_1").css('display', 'none');
	$("#sel_table10").css('display', 'none');
	$("#sel_table11").css('display', 'none');
	$("#sel_table12").css('display', 'none');
	$("#sel_table13").css('display', 'none');
	$("#sel_table14").css('display', 'none');
	$("#sel_table15").css('display', 'none');
	$("#sel_table16").css('display', 'none');
	$("#sel_table17").css('display', 'none');
	$("#sel_table18").css('display', 'none');
	$("#sel_table19").css('display', 'none');
	$("#sel_table20").css('display', 'none');
	$("#sel_table21").css('display', 'none');
	$("#sel_table22").css('display', 'none');
	$("#sel_table23").css('display', 'none');
	$("#sel_table24").css('display', 'none');
	$("#sel_table25").css('display', 'none');
	$("#sel_table26").css('display', 'none');
	$("#sel_table27").css('display', 'none');
	$("#sel_table28").css('display', 'none');
	$("#sel_table29").css('display', 'none');
	$("#sel_table30").css('display', 'none');

	//	dataMigration.TableType = [];
	//	for(var i=0;i<$("#tableList_type")[0].length;i++){
	//		var tmp = $("#tableList_type")[0][i].text;
	//		dataMigration.TableType.push(tmp);
	//		console.log("tmp = "+tmp);
	//	}

	getTableCodeSetList();
	initPrjSelectGridDataMigration();
	//prjSearchdataMigration();
});

function initPrjSelectGridDataMigration() {
	prj_grid_dataMigration = new Tabulator("#prj_list_grid_dataMigration", {
		layout : "fitColumns",
		placeholder : "There is No Project",
		selectable : 1,
		index : "prj_id",
		height : 180,
		columns : [ {
			title : "index",
			field : "prj_id",
			visible : false
		}, {
			title : "Project No",
			field : "prj_no",
			headerSort : false,
		}, {
			title : "Project Name",
			field : "prj_nm",
			headerSort : false,
		}, {
			title : "Project Full Name",
			field : "prj_full_nm",
			headerSort : false,
		}, ]
	});

	prj_grid_dataMigration.on("tableBuilt", function() {
		$.ajax({
			url : 'getPrjGridList.do',
			type : 'POST',
			success : function onData(data) {
				prj_grid_dataMigration.setData(data.model.prjList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	});

	prj_grid_dataMigration.on("rowClick", function(e, row) {
		let rowData = row.getData();
		$("#dataMigrationPrjId").val(rowData.prj_id);
		$("#selected_prj_dataMigration").html(rowData.prj_nm);
		$(".multi_sel_wrap").removeClass('on');
		getTableCodeSetList();
	});

	$(document).keydown(function(event) {
		// 프로젝트 검색 input영역에 포커스가 가있는 경우에만 작동
		let focusEle = document.activeElement;
		if (document.getElementById('prj_search_dataMigration') == focusEle) {
			if (event.keyCode == 13 || event.which == 13) {
				prjSearchdataMigration();
			}
		}
	});
}

/**
 * 프로젝트 검색
 * @returns
 */
function prjSearchdataMigration() {
	let keyword = $("#prj_search_dataMigration").val();
	let grid = prj_grid_dataMigration;

	$.ajax({
		url : 'prjSearch.do',
		type : 'POST',
		data : {
			keyword : keyword
		},
		success : function onData(data) {
			grid.setData(data.model.prjList);
		},
		error : function onError(error) {
			console.error(error);
		}
	});
}

function getTableCodeSetList() {
	// 엑셀로부터 데이터 불러왔는지 체크하는 플래그 클리어
	isFromExcel = false;
	excelData = null;

	getTableType();

	dataMigSelectPrj_id = $('#dataMigrationPrjId').val();

	if (set_table_type == "P_PRJ_CODE_SETTINGS") {
		tableNo = 0;
		$.ajax({
			url : 'getCodeSetList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var codeSettingList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					codeSettingList.push({
						prj_id : data[0][i].prj_id,
						set_code_type : data[0][i].set_code_type,
						set_code_id : data[0][i].set_code_id,
						set_code_feature : data[0][i].set_code_feature,
						set_code : data[0][i].set_code,
						set_desc : data[0][i].set_desc,
						set_val : data[0][i].set_val,
						set_order : data[0][i].set_order,
						set_val2 : data[0][i].set_val2,
						set_val3 : data[0][i].set_val3,
						set_val4 : data[0][i].set_val4,
						ifc_yn : data[0][i].ifc_yn,
						number_type : data[0][i].number_type,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setCodeSetList(codeSettingList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_CORDOC_INFO") {
		tableNo = 0;
		$.ajax({
					url : 'getCordocInfoList.do',
					type : 'POST',
					data : {
						prj_id : dataMigSelectPrj_id
					},
					success : function onData(data) {
						var cordocInfoList = [];
						for (var i = 0; i < data[0].length; i++) {
							tableNo = i + 1;
							cordocInfoList
									.push({
										prj_id : data[0][i].prj_id,
										doc_id : data[0][i].doc_id,
										rev_id : data[0][i].rev_id,
										folder_id : data[0][i].folder_id,
										in_out : data[0][i].in_out,
										doc_date : data[0][i].doc_date,
										sender_code_id : data[0][i].sender_code_id,
										receiver_code_id : data[0][i].receiver_code_id,
										doc_type_code_id : data[0][i].doc_type_code_id,
										serial : data[0][i].serial,
										corr_no : data[0][i].corr_no,
										sys_corr_no : data[0][i].sys_corr_no,
										ref_corr_doc_id : data[0][i].ref_corr_doc_id,
										responsibility_code_id : data[0][i].responsibility_code_id,
										send_recv_date : data[0][i].send_recv_date,
										originator : data[0][i].originator,
										disact : data[0][i].disact,
										replyreqdate : data[0][i].replyreqdate,
										indistdate : data[0][i].indistdate,
										inactdate : data[0][i].inactdate,
										person_in_charge_id : data[0][i].person_in_charge_id,
										remark : data[0][i].remark,
										item_info_code_id : data[0][i].item_info_code_id,
										item_note : data[0][i].item_note,
										userdata00 : data[0][i].userdata00,
										userdata01 : data[0][i].userdata01,
										userdata02 : data[0][i].userdata02,
										userdata03 : data[0][i].userdata03,
										userdata04 : data[0][i].userdata04,
										replydocno : data[0][i].replydocno,
										ref_grp : data[0][i].ref_grp,
										reg_id : data[0][i].reg_id,
										reg_date : data[0][i].reg_date,
										mod_id : data[0][i].mod_id,
										mod_date : data[0][i].mod_date
									});
						}
						setCordocInfoList(cordocInfoList);
					},
					error : function onError(error) {
						console.error(error);
					}
				});
	} else if (set_table_type == "P_PRJ_DISCIPLINE_FOLDER_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getDisciplineFolderInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var disciplineFolderInfoList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					disciplineFolderInfoList.push({
						prj_id : data[0][i].prj_id,
						discip_code_id : data[0][i].discip_code_id,
						discip_folder_seq : data[0][i].discip_folder_seq,
						discip_folder_id : data[0][i].discip_folder_id,
						discip_order : data[0][i].discip_order,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setDisciplineFolderInfoList(disciplineFolderInfoList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_DISCIPLINE_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getDisciplineInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var disciplineInfoList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					disciplineInfoList.push({
						prj_id : data[0][i].prj_id,
						discip_code_id : data[0][i].discip_code_id,
						discip_code : data[0][i].discip_code,
						discip_display : data[0][i].discip_display,
						discip_desc : data[0][i].discip_desc,
						discip_order : data[0][i].discip_order,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setDisciplineInfoList(disciplineInfoList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_DOCUMENT_INDEX") {
		tableNo = 0;
		$.ajax({
			url : 'getDocumentIndexList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var documentIndexList = [];
				// let getDocumentIndexList = data.model.getDocumentIndexList;
				// console.log(getDocumentIndexList);

				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					documentIndexList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						folder_id : data[0][i].folder_id,
						doc_no : data[0][i].doc_no,
						doc_type : data[0][i].doc_type,
						rev_code_id : data[0][i].rev_code_id,
						sfile_nm : data[0][i].sfile_nm,
						rfile_nm : data[0][i].rfile_nm,
						file_type : data[0][i].file_type,
						file_size : data[0][i].file_size,
						title : data[0][i].title,
						del_yn : data[0][i].del_yn,
						unread_yn : data[0][i].unread_yn,
						lock_yn : data[0][i].lock_yn,
						attach_file_date : data[0][i].attach_file_date,
						last_rev_yn : data[0][i].last_rev_yn,
						trans_ref_doc_id : data[0][i].trans_ref_doc_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}

				setDocumentIndexList(documentIndexList);
				//setDocumentIndexList(getDocumentIndexList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_ATTACH_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getEmailAttachInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var emailTypeList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					emailTypeList.push({
						prj_id : data[0][i].prj_id,
						email_type_id : data[0][i].email_type_id,
						email_id : data[0][i].email_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						doc_no : data[0][i].doc_no,
						down_ip : data[0][i].down_ip,
						down_id : data[0][i].down_id,
						down_date : data[0][i].down_date,
						rev_no : data[0][i].rev_no,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setEmailAttachInfoList(emailTypeList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_RECEIPT_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getEmailReceiptInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var emailTypeList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					emailTypeList.push({
						prj_id : data[0][i].prj_id,
						email_type_id : data[0][i].email_type_id,
						email_id : data[0][i].email_id,
						email_receiver_addr : data[0][i].email_receiver_addr,
						email_receiverd_date : data[0][i].email_receiverd_date,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setEmailReceiptInfoList(emailTypeList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_SEND_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getEmailSendInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var emailTypeList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					emailTypeList.push({
						prj_id : data[0][i].prj_id,
						email_type_id : data[0][i].email_type_id,
						email_id : data[0][i].email_id,
						subject : data[0][i].subject,
						contents : data[0][i].contents,
						email_sender_user_id : data[0][i].email_sender_user_id,
						email_sender_addr : data[0][i].email_sender_addr,
						email_receiver_addr : data[0][i].email_receiver_addr,
						email_send_date : data[0][i].email_send_date,
						email_receiverd_date : data[0][i].email_receiverd_date,
						user_data00 : data[0][i].user_data00,
						email_refer_to : data[0][i].email_refer_to,
						email_bcc : data[0][i].email_bcc,
						send_yn : data[0][i].send_yn,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setEmailSendInfoList(emailTypeList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_TYPE") {
		tableNo = 0;
		$.ajax({
			url : 'getEmailTypeList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var emailTypeList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					emailTypeList.push({
						prj_id : data[0][i].prj_id,
						email_type_id : data[0][i].email_type_id,
						email_type : data[0][i].email_type,
						email_feature : data[0][i].email_feature,
						email_desc : data[0][i].email_desc,
						email_auto_send_yn : data[0][i].email_auto_send_yn,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setEmailTypeList(emailTypeList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_TYPE_CONTENTS") {
		tableNo = 0;
		$
				.ajax({
					url : 'getEmailTypeContentsList.do',
					type : 'POST',
					data : {
						prj_id : dataMigSelectPrj_id
					},
					success : function onData(data) {
						var emailTypeContentsList = [];
						for (var i = 0; i < data[0].length; i++) {
							tableNo = i + 1;
							emailTypeContentsList
									.push({
										prj_id : data[0][i].prj_id,
										email_type_id : data[0][i].email_type_id,
										email_sender_addr : data[0][i].email_sender_addr,
										email_receiver_addr : data[0][i].email_receiver_addr,
										email_referto_addr : data[0][i].email_referto_addr,
										email_bcc_addr : data[0][i].email_bcc_addr,
										file_link_yn : data[0][i].file_link_yn,
										file_rename_yn : data[0][i].file_rename_yn,
										file_nm_prefix : data[0][i].file_nm_prefix,
										subject_prefix : data[0][i].subject_prefix,
										contents : data[0][i].contents,
										file_direct_attach_type : data[0][i].file_direct_attach_type,
										reg_id : data[0][i].reg_id,
										reg_date : data[0][i].reg_date,
										mod_id : data[0][i].mod_id,
										mod_date : data[0][i].mod_date
									});
						}
						setEmailTypeContentsList(emailTypeContentsList);
					},
					error : function onError(error) {
						console.error(error);
					}
				});
	} else if (set_table_type == "P_PRJ_ENG_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getEngInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var engInfoList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					engInfoList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						folder_id : data[0][i].folder_id,
						discip_code_id : data[0][i].discip_code_id,
						designer_id : data[0][i].designer_id,
						prfe_designer_id : data[0][i].prfe_designer_id,
						wbs_code_id : data[0][i].wbs_code_id,
						remark : data[0][i].remark,
						category_cd_id : data[0][i].category_cd_id,
						size_code_id : data[0][i].size_code_id,
						doc_type_id : data[0][i].doc_type_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setEngInfoList(engInfoList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_ENG_STEP") {
		tableNo = 0;
		$.ajax({
			url : 'getEngStepList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var engStepList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					engStepList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						step_code_id : data[0][i].step_code_id,
						plan_date : data[0][i].plan_date,
						actual_date : data[0][i].actual_date,
						fore_date : data[0][i].fore_date,
						tr_id : data[0][i].tr_id,
						rtn_status : data[0][i].rtn_status,
						step_seq : data[0][i].step_seq,
						folder_id : data[0][i].folder_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setEngStepList(engStepList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_FOLDER_AUTH") {
		tableNo = 0;
		$
				.ajax({
					url : 'getFolderAuthList.do',
					type : 'POST',
					data : {
						prj_id : dataMigSelectPrj_id
					},
					success : function onData(data) {
						var folderInfoList = [];
						for (var i = 0; i < data[0].length; i++) {
							tableNo = i + 1;
							folderInfoList
									.push({
										prj_id : data[0][i].prj_id,
										folder_id : data[0][i].folder_id,
										auth_lvl1 : data[0][i].auth_lvl1,
										auth_val : data[0][i].auth_val,
										folder_auth_add_yn : data[0][i].folder_auth_add_yn,
										parent_folder_inheri_yn : data[0][i].parent_folder_inheri_yn,
										read_auth_yn : data[0][i].read_auth_yn,
										write_auth_yn : data[0][i].write_auth_yn,
										delete_auth_yn : data[0][i].delete_auth_yn,
										reg_id : data[0][i].reg_id,
										reg_date : data[0][i].reg_date,
										mod_id : data[0][i].mod_id,
										mod_date : data[0][i].mod_date
									});
						}
						setFolderAuthList(folderInfoList);
					},
					error : function onError(error) {
						console.error(error);
					}
				});
	} else if (set_table_type == "P_PRJ_FOLDER_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getFolderInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var folderInfoList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					folderInfoList.push({
						prj_id : data[0][i].prj_id,
						folder_id : data[0][i].folder_id,
						parent_folder_id : data[0][i].parent_folder_id,
						folder_path : data[0][i].folder_path,
						folder_nm : data[0][i].folder_nm,
						r_folder_path_nm : data[0][i].r_folder_path_nm,
						folder_type : data[0][i].folder_type,
						tra_folder_nm : data[0][i].tra_folder_nm,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setFolderInfoList(folderInfoList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_GENDOC_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getGendocInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var gendocInfoList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					gendocInfoList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						folder_id : data[0][i].folder_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setGendocInfoList(gendocInfoList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_GROUP_MEMBER") {
		tableNo = 0;
		$.ajax({
			url : 'getGroupMemberList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						group_id : data[0][i].group_id,
						user_id : data[0][i].user_id,
						group_desc : data[0][i].group_desc,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setGroupMemberList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getPrjInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						prj_no : data[0][i].prj_no,
						prj_nm : data[0][i].prj_nm,
						prj_full_nm : data[0][i].prj_full_nm,
						prj_hidden_yn : data[0][i].prj_hidden_yn,
						prj_type : data[0][i].prj_type,
						client_code : data[0][i].client_code,
						ship_no : data[0][i].ship_no,
						prj_order : data[0][i].prj_order,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setPrjInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_MEMBER") {
		tableNo = 0;
		$.ajax({
			url : 'getMigPrjMemberList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						user_id : data[0][i].user_id,
						dcc_yn : data[0][i].dcc_yn,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setPrjMemberList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_PRO_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getProInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						folder_id : data[0][i].folder_id,
						discip_code_id : data[0][i].discip_code_id,
						designer_id : data[0][i].designer_id,
						prfe_designer_id : data[0][i].prfe_designer_id,
						wbs_code_id : data[0][i].wbs_code_id,
						remark : data[0][i].remark,
						por_no : data[0][i].por_no,
						por_issue_date : data[0][i].por_issue_date,
						po_no : data[0][i].po_no,
						po_issue_date : data[0][i].po_issue_date,
						vendor_nm : data[0][i].vendor_nm,
						vendor_doc_no : data[0][i].vendor_doc_no,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setProInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_PRO_STEP") {
		tableNo = 0;
		$.ajax({
			url : 'getProStepList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						step_code_id : data[0][i].step_code_id,
						plan_date : data[0][i].plan_date,
						actual_date : data[0][i].actual_date,
						fore_date : data[0][i].fore_date,
						tr_id : data[0][i].tr_id,
						rtn_status : data[0][i].rtn_status,
						step_seq : data[0][i].step_seq,
						folder_id : data[0][i].folder_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setProStepList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_PROCESS_INFO") {
		tableNo = 0;
		$
				.ajax({
					url : 'getProcessInfoList.do',
					type : 'POST',
					data : {
						prj_id : dataMigSelectPrj_id
					},
					success : function onData(data) {
						var dataList = [];
						for (var i = 0; i < data[0].length; i++) {
							tableNo = i + 1;
							dataList
									.push({
										prj_id : data[0][i].prj_id,
										process_id : data[0][i].process_id,
										class_lvl : data[0][i].class_lvl,
										process_type : data[0][i].process_type,
										process_desc : data[0][i].process_desc,
										process_folder_path_id : data[0][i].process_folder_path_id,
										drn_use_yn : data[0][i].drn_use_yn,
										process_order : data[0][i].process_order,
										doc_type : data[0][i].doc_type,
										reg_id : data[0][i].reg_id,
										reg_date : data[0][i].reg_date,
										mod_id : data[0][i].mod_id,
										mod_date : data[0][i].mod_date
									});
						}
						setProcessInfoList(dataList);
					},
					error : function onError(error) {
						console.error(error);
					}
				});
	} else if (set_table_type == "P_PRJ_SDC_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getSdcInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						folder_id : data[0][i].folder_id,
						discip_code_id : data[0][i].discip_code_id,
						designer_id : data[0][i].designer_id,
						prfe_designer_id : data[0][i].prfe_designer_id,
						wbs_code_id : data[0][i].wbs_code_id,
						remark : data[0][i].remark,
						category_cd_id : data[0][i].category_cd_id,
						size_code_id : data[0][i].size_code_id,
						doc_type_id : data[0][i].doc_type_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setSdcInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_SDC_STEP") {
		tableNo = 0;
		$.ajax({
			url : 'getSdcStepList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						step_code_id : data[0][i].step_code_id,
						plan_date : data[0][i].plan_date,
						actual_date : data[0][i].actual_date,
						fore_date : data[0][i].fore_date,
						tr_id : data[0][i].tr_id,
						rtn_status : data[0][i].rtn_status,
						step_seq : data[0][i].step_seq,
						folder_id : data[0][i].folder_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setSdcStepList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_STR_FILE") {
		tableNo = 0;
		$.ajax({
			url : 'getStrFileList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						tr_id : data[0][i].tr_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						rev_no : data[0][i].rev_no,
						issue_step : data[0][i].issue_step,
						rtn_status_code_id : data[0][i].rtn_status_code_id,
						doc_table : data[0][i].doc_table,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setStrFileList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_STR_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getStrInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						tr_id : data[0][i].tr_id,
						tr_no : data[0][i].tr_no,
						itr_no : data[0][i].itr_no,
						tr_subject : data[0][i].tr_subject,
						discipline_code_id : data[0][i].discipline_code_id,
						inout : data[0][i].inout,
						issued : data[0][i].issued,
						tr_status : data[0][i].tr_status,
						tr_issuepur_code_id : data[0][i].tr_issuepur_code_id,
						send_date : data[0][i].send_date,
						req_date : data[0][i].req_date,
						tr_writer_id : data[0][i].tr_writer_id,
						tr_modifier_id : data[0][i].tr_modifier_id,
						tr_from : data[0][i].tr_from,
						tr_to : data[0][i].tr_to,
						chk_date : data[0][i].chk_date,
						tr_remark : data[0][i].tr_remark,
						tr_distribute : data[0][i].tr_distribute,
						ref_tr_no : data[0][i].ref_tr_no,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setStrInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_SYSTEM_PREFIX") {
		tableNo = 0;
		$.ajax({
			url : 'getSystemPrefixList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						system_prefix_type : data[0][i].system_prefix_type,
						system_prefix_id : data[0][i].system_prefix_id,
						prefix : data[0][i].prefix,
						prefix_desc : data[0][i].prefix_desc,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setSystemPrefixList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_TR_FILE") {
		tableNo = 0;
		$.ajax({
			url : 'getTrFileList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						tr_id : data[0][i].tr_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						rev_no : data[0][i].rev_no,
						issue_step : data[0][i].issue_step,
						rtn_status_code_id : data[0][i].rtn_status_code_id,
						doc_table : data[0][i].doc_table,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setTrFileList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_TR_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getTrInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						tr_id : data[0][i].tr_id,
						tr_no : data[0][i].tr_no,
						itr_no : data[0][i].itr_no,
						tr_subject : data[0][i].tr_subject,
						discipline_code_id : data[0][i].discipline_code_id,
						inout : data[0][i].inout,
						issued : data[0][i].issued,
						tr_status : data[0][i].tr_status,
						tr_issuepur_code_id : data[0][i].tr_issuepur_code_id,
						send_date : data[0][i].send_date,
						req_date : data[0][i].req_date,
						tr_writer_id : data[0][i].tr_writer_id,
						tr_modifier_id : data[0][i].tr_modifier_id,
						tr_from : data[0][i].tr_from,
						tr_to : data[0][i].tr_to,
						chk_date : data[0][i].chk_date,
						tr_remark : data[0][i].tr_remark,
						tr_distribute : data[0][i].tr_distribute,
						ref_tr_no : data[0][i].ref_tr_no,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setTrInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VDR_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getVdrInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						folder_id : data[0][i].folder_id,
						discip_code_id : data[0][i].discip_code_id,
						designer_id : data[0][i].designer_id,
						prfe_designer_id : data[0][i].prfe_designer_id,
						wbs_code_id : data[0][i].wbs_code_id,
						remark : data[0][i].remark,
						po_doc_no : data[0][i].po_doc_no,
						po_doc_title : data[0][i].po_doc_title,
						vendor_nm : data[0][i].vendor_nm,
						design_part : data[0][i].design_part,
						category_cd_id : data[0][i].category_cd_id,
						size_code_id : data[0][i].size_code_id,
						doc_type_id : data[0][i].doc_type_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setVdrInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VDR_STEP") {
		tableNo = 0;
		$.ajax({
			url : 'getVdrStepList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						step_code_id : data[0][i].step_code_id,
						plan_date : data[0][i].plan_date,
						actual_date : data[0][i].actual_date,
						fore_date : data[0][i].fore_date,
						tr_id : data[0][i].tr_id,
						rtn_status : data[0][i].rtn_status,
						step_seq : data[0][i].step_seq,
						folder_id : data[0][i].folder_id,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setVdrStepList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VTR_FILE") {
		tableNo = 0;
		$.ajax({
			url : 'getVtrFileList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						tr_id : data[0][i].tr_id,
						doc_id : data[0][i].doc_id,
						rev_id : data[0][i].rev_id,
						rev_no : data[0][i].rev_no,
						issue_step : data[0][i].issue_step,
						rtn_status_code_id : data[0][i].rtn_status_code_id,
						doc_table : data[0][i].doc_table,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setVtrFileList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VTR_INFO") {
		tableNo = 0;
		$.ajax({
			url : 'getVtrInfoList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						tr_id : data[0][i].tr_id,
						tr_no : data[0][i].tr_no,
						itr_no : data[0][i].itr_no,
						tr_subject : data[0][i].tr_subject,
						discipline_code_id : data[0][i].discipline_code_id,
						inout : data[0][i].inout,
						issued : data[0][i].issued,
						tr_status : data[0][i].tr_status,
						tr_issuepur_code_id : data[0][i].tr_issuepur_code_id,
						send_date : data[0][i].send_date,
						req_date : data[0][i].req_date,
						tr_writer_id : data[0][i].tr_writer_id,
						tr_modifier_id : data[0][i].tr_modifier_id,
						tr_from : data[0][i].tr_from,
						tr_to : data[0][i].tr_to,
						chk_date : data[0][i].chk_date,
						tr_remark : data[0][i].tr_remark,
						tr_distribute : data[0][i].tr_distribute,
						ref_tr_no : data[0][i].ref_tr_no,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setVtrInfoList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_WBS_CODE") {
		tableNo = 0;
		$.ajax({
			url : 'getMigWbsCodeList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						prj_id : data[0][i].prj_id,
						wbs_code_id : data[0][i].wbs_code_id,
						wbs_code : data[0][i].wbs_code,
						wbs_desc : data[0][i].wbs_desc,
						doc_no : data[0][i].doc_no,
						wgtval : data[0][i].wgtval,
						doc_type : data[0][i].doc_type,
						discipline : data[0][i].discipline,
						code_seq : data[0][i].code_seq,
						gad : data[0][i].gad,
						distribute : data[0][i].distribute,
						information : data[0][i].information,
						siteact : data[0][i].siteact,
						sitecc : data[0][i].sitecc,
						disact : data[0][i].disact,
						discc : data[0][i].discc,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setMigWbsCodeList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_USERS") {
		tableNo = 0;
		$.ajax({
			url : 'getMigUsersList.do',
			type : 'POST',
			data : {
				prj_id : dataMigSelectPrj_id
			},
			success : function onData(data) {
				var dataList = [];
				for (var i = 0; i < data[0].length; i++) {
					tableNo = i + 1;
					dataList.push({
						user_id : data[0][i].user_id,
						user_kor_nm : data[0][i].user_kor_nm,
						user_eng_nm : data[0][i].user_eng_nm,
						user_address : data[0][i].user_address,
						user_group_nm : data[0][i].user_group_nm,
						user_type : data[0][i].user_type,
						passwd : data[0][i].passwd,
						company_nm : data[0][i].company_nm,
						first_login_yn : data[0][i].first_login_yn,
						hld_offi_gbn : data[0][i].hld_offi_gbn,
						job_tit_cd : data[0][i].job_tit_cd,
						offi_res_cd : data[0][i].offi_res_cd,
						email_addr : data[0][i].email_addr,
						offi_tel : data[0][i].offi_tel,
						admin_yn : data[0][i].admin_yn,
						sfile_nm : data[0][i].sfile_nm,
						rfile_nm : data[0][i].rfile_nm,
						file_path : data[0][i].file_path,
						file_size : data[0][i].file_size,
						last_con_prj_id : data[0][i].last_con_prj_id,
						remote_ip : data[0][i].remote_ip,
						user_st : data[0][i].user_st,
						reg_id : data[0][i].reg_id,
						reg_date : data[0][i].reg_date,
						mod_id : data[0][i].mod_id,
						mod_date : data[0][i].mod_date
					});
				}
				setMigUsersList(dataList);
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	}
}

function setMigUsersList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	usersTable = new Tabulator("#migUsers", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "USER ID",
			field : "user_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USER KOR NM",
			field : "user_kor_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USER ENG NM",
			field : "user_eng_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USER ADDRESS",
			field : "user_address",
			headerSort : false,
			minWidth : 100
		}, {
			title : "USER GROUP NM",
			field : "user_group_nm",
			headerSort : false,
			minWidth : 110
		}, {
			title : "USER TYPE",
			field : "user_type",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PASSWORD",
			field : "passwd",
			headerSort : false,
			minWidth : 80
		}, {
			title : "COMPANY NM",
			field : "company_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FIRST LOGIN YN",
			field : "first_login_yn",
			headerSort : false,
			minWidth : 110
		}, {
			title : "HLD OFFI GBN",
			field : "hld_offi_gbn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "JOB TIT CD",
			field : "job_tit_cd",
			headerSort : false,
			minWidth : 80
		}, {
			title : "OFFI RES CD",
			field : "offi_res_cd",
			headerSort : false,
			minWidth : 80
		}, {
			title : "EMAIL ADDR",
			field : "email_addr",
			headerSort : false,
			minWidth : 80
		}, {
			title : "OFFI TEL",
			field : "offi_tel",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ADMIN YN",
			field : "admin_yn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SFILE NM",
			field : "sfile_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "RFILE NM",
			field : "rfile_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FILE PATH",
			field : "file_path",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FILE SIZE",
			field : "file_size",
			headerSort : false,
			minWidth : 80
		}, {
			title : "LAST CON PROJECT ID",
			field : "last_con_prj_id",
			headerSort : false,
			minWidth : 140
		}, {
			title : "REMOTE IP",
			field : "remote_ip",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USER ST",
			field : "user_st",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setMigWbsCodeList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	wbsCodeTable = new Tabulator("#wbsCode", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "WBS CODE ID",
			field : "wbs_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "WBS CODE",
			field : "wbs_code",
			headerSort : false,
			minWidth : 80
		}, {
			title : "WBS DESC",
			field : "wbs_desc",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC NO",
			field : "doc_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "WGTVAL",
			field : "wgtval",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC TYPE",
			field : "doc_type",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIPLINE",
			field : "discipline",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CODE SEQ",
			field : "code_seq",
			headerSort : false,
			minWidth : 80
		}, {
			title : "GAD",
			field : "gad",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISTRIBUTE",
			field : "distribute",
			headerSort : false,
			minWidth : 80
		}, {
			title : "INFORMATION",
			field : "information",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SITEACT",
			field : "siteact",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SITECC",
			field : "sitecc",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISACT",
			field : "disact",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCC",
			field : "discc",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setVtrInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	vtrInfoTable = new Tabulator("#vtrInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR NO",
			field : "tr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ITR NO",
			field : "itr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR SUBJECT",
			field : "tr_subject",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIPLINE CODE ID",
			field : "discipline_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "INOUT",
			field : "inout",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ISSUED",
			field : "issued",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR STATUS",
			field : "tr_status",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ISSUEPUR CODE ID",
			field : "tr_issuepur_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "SEND DATE",
			field : "send_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REQ DATE",
			field : "req_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR WRITER ID",
			field : "tr_writer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR MODIFIER ID",
			field : "tr_modifier_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR FROM",
			field : "tr_from",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR TO",
			field : "tr_to",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CHECK DATE",
			field : "chk_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR REMARK",
			field : "tr_remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR DISTRIBUTE",
			field : "tr_distribute",
			headerSort : false,
			minWidth : 100
		}, {
			title : "REF TR NO",
			field : "ref_tr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setVtrFileList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	vtrFileTable = new Tabulator("#vtrFile", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV NO",
			field : "rev_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ISSUE STEP",
			field : "issue_step",
			headerSort : false,
			minWidth : 80
		}, {
			title : "RTN STATUS CODE ID",
			field : "rtn_status_code_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "DOC TABLE",
			field : "doc_table",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setVdrStepList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	vdrStepTable = new Tabulator("#vdrStep", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false
		}, {
			title : "STEP CODE ID",
			field : "step_code_id",
			headerSort : false
		}, {
			title : "PLAN DATE",
			field : "plan_date",
			headerSort : false
		}, {
			title : "ACTUAL DATE",
			field : "actual_date",
			headerSort : false
		}, {
			title : "FORE DATE",
			field : "fore_date",
			headerSort : false
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false
		}, {
			title : "RTN STATUS",
			field : "rtn_status",
			headerSort : false
		}, {
			title : "STEP SEQ",
			field : "step_seq",
			headerSort : false
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setVdrInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	vdrInfotable = new Tabulator("#vdrInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIP CODE ID",
			field : "discip_code_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "DESIGNER ID",
			field : "designer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PREFE DESIGNER ID",
			field : "prfe_designer_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "WBS CODE ID",
			field : "wbs_code_id",
			headerSort : false,
			minWidth : 90
		}, {
			title : "REMARK",
			field : "remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PO DOC NO",
			field : "po_doc_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PO DOC TITLE",
			field : "po_doc_title",
			headerSort : false,
			minWidth : 100
		}, {
			title : "VENDOR NM",
			field : "vendor_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DESIGN PART",
			field : "design_part",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CATEGORY CD ID",
			field : "category_cd_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "SIZE CODE ID",
			field : "size_code_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "DOC TYPE ID",
			field : "doc_type_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setTrInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	trInfoTable = new Tabulator("#trInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR NO",
			field : "tr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ITR NO",
			field : "itr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR SUBJECT",
			field : "tr_subject",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIPLINE CODE ID",
			field : "discipline_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "INOUT",
			field : "inout",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ISSUED",
			field : "issued",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR STATUS",
			field : "tr_status",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ISSUEPUR CODE ID",
			field : "tr_issuepur_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "SEND DATE",
			field : "send_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REQ DATE",
			field : "req_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR WRITER ID",
			field : "tr_writer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR MODIFIER ID",
			field : "tr_modifier_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "TR FROM",
			field : "tr_from",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR TO",
			field : "tr_to",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CHECK DATE",
			field : "chk_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR REMARK",
			field : "tr_remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR DISTRIBUTE",
			field : "tr_distribute",
			headerSort : false,
			minWidth : 100
		}, {
			title : "REF TR NO",
			field : "ref_tr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setTrFileList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	trFileTable = new Tabulator("#trFile", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false
		}, {
			title : "REV NO",
			field : "rev_no",
			headerSort : false
		}, {
			title : "ISSUE STEP",
			field : "issue_step",
			headerSort : false
		}, {
			title : "RTN STATUS CODE ID",
			field : "rtn_status_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "DOC TABLE",
			field : "doc_table",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setSystemPrefixList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	systemPrefixTable = new Tabulator("#systemPrefix", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "SYSTEM PREFIX TYPE",
			field : "system_prefix_type",
			headerSort : false
		}, {
			title : "SYSTEM PREFIX ID",
			field : "system_prefix_id",
			headerSort : false
		}, {
			title : "PREFIX",
			field : "prefix",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : "PREFIX DESC",
			field : "prefix_desc",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setStrInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	strInfoTable = new Tabulator("#strInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR NO",
			field : "tr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ITR NO",
			field : "itr_no",
			headerSort : false
		}, {
			title : "TR SUBJECT",
			field : "tr_subject",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIPLINE CODE ID",
			field : "discipline_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "INOUT",
			field : "inout",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ISSUED",
			field : "issued",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR STATUS",
			field : "tr_status",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ISSUEPUR CODE ID",
			field : "tr_issuepur_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "SEND DATE",
			field : "send_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REQ DATE",
			field : "req_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR WRITER ID",
			field : "tr_writer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR MODIFIER ID",
			field : "tr_modifier_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "TR FROM",
			field : "tr_from",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR TO",
			field : "tr_to",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CHECK DATE",
			field : "chk_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR REMARK",
			field : "tr_remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR DISTRIBUTE",
			field : "tr_distribute",
			headerSort : false,
			minWidth : 100
		}, {
			title : "REF TR NO",
			field : "ref_tr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setStrFileList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	strFileTable = new Tabulator("#strFile", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false
		}, {
			title : "REV NO",
			field : "rev_no",
			headerSort : false
		}, {
			title : "ISSUE STEP",
			field : "issue_step",
			headerSort : false
		}, {
			title : "RTN STATUS CODE ID",
			field : "rtn_status_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "DOC TABLE",
			field : "doc_table",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setSdcStepList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	sdcStepTable = new Tabulator("#sdcStep", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "STEP CODE ID",
			field : "step_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PLAN DATE",
			field : "plan_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ACTUAL DATE",
			field : "actual_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FORE DATE",
			field : "fore_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "RTN STATUS",
			field : "rtn_status",
			headerSort : false,
			minWidth : 80
		}, {
			title : "STEP SEQ",
			field : "step_seq",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setSdcInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	sdcInfoTable = new Tabulator("#sdcInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIP CODE ID",
			field : "discip_code_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "DESIGNER ID",
			field : "designer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PRFE DESIGNER ID",
			field : "prfe_designer_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "WBS CODE ID",
			field : "wbs_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REMARK",
			field : "remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CATEGORY CD ID",
			field : "category_cd_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "SIZE CODE ID",
			field : "size_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC TYPE ID",
			field : "doc_type_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setProStepList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	proStepTable = new Tabulator("#proStep", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false
		}, {
			title : "STEP CODE ID",
			field : "step_code_id",
			headerSort : false
		}, {
			title : "PLAN DATE",
			field : "plan_date",
			headerSort : false
		}, {
			title : "ACTUAL DATE",
			field : "actual_date",
			headerSort : false
		}, {
			title : "FORE DATE",
			field : "fore_date",
			headerSort : false
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false
		}, {
			title : "RTN STATUS",
			field : "rtn_status",
			headerSort : false
		}, {
			title : "STEP SEQ",
			field : "step_seq",
			headerSort : false
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setProcessInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	processInfoTable = new Tabulator("#processInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROCESS ID",
			field : "process_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CLASS",
			field : "class_lvl",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROCESS TYPE",
			field : "process_type",
			headerSort : false,
			hozAlign : "left",
			minWidth : 80
		}, {
			title : "PROCESS DESC",
			field : "process_desc",
			headerSort : false,
			hozAlign : "left",
			minWidth : 80
		}, {
			title : "PROCESS FOLDER PATH ID",
			field : "process_folder_path_id",
			headerSort : false,
			minWidth : 150
		}, {
			title : "DRN USE YN",
			field : "drn_use_yn",
			headerSort : false
		}, {
			title : "PROCESS ORDER",
			field : "process_order",
			headerSort : false,
			minWidth : 100
		}, {
			title : "DOC TYPE",
			field : "doc_type",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setProInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	proInfoTable = new Tabulator("#proInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIP CODE ID",
			field : "discip_code_id",
			headerSort : false,
			minWidth : 90
		}, {
			title : "DESIGNER ID",
			field : "designer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PRFE DESIGNER ID",
			field : "prfe_designer_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "WBS CODE ID",
			field : "wbs_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REMARK",
			field : "remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "POR NO",
			field : "por_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "POR ISSUE DATE",
			field : "por_issue_date",
			headerSort : false,
			minWidth : 100
		}, {
			title : "PO NO",
			field : "po_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PO ISSUE DATE",
			field : "po_issue_date",
			headerSort : false,
			minWidth : 100
		}, {
			title : "VENDOR NM",
			field : "vendor_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "VENDOR DOC NO",
			field : "vendor_doc_no",
			headerSort : false,
			minWidth : 100
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setPrjMemberList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	memberTable = new Tabulator("#prjMember", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USER ID",
			field : "user_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DCC YN",
			field : "dcc_yn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setPrjInfoList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	infoTable = new Tabulator("#prjInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROJECT NO",
			field : "prj_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROJECT NM",
			field : "prj_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROJECT FULL NM",
			field : "prj_full_nm",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROJECT HIDDEN YN",
			field : "prj_hidden_yn",
			headerSort : false,
			minWidth : 90
		}, {
			title : "PROJECT TYPE",
			field : "prj_type",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CLIENT CODE",
			field : "client_code",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SHIP NO",
			field : "ship_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PROJECT ORDER",
			field : "prj_order",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setGroupMemberList(dataList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	groupMemberTable = new Tabulator("#groupMember", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : dataList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "GROUP ID",
			field : "group_id",
			headerSort : false
		}, {
			title : "USER ID",
			field : "user_id",
			headerSort : false
		}, {
			title : "GROUP DESC",
			field : "group_desc",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setGendocInfoList(gendocInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	gendocInfoTable = new Tabulator("#gendocInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : gendocInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : " PROJECT ",
			field : "prj_id",
			headerSort : false
		}, {
			title : " DOC ID ",
			field : "doc_id",
			headerSort : false
		}, {
			title : " REV ID ",
			field : "rev_id",
			headerSort : false
		}, {
			title : " FOLDER ID ",
			field : "folder_id",
			headerSort : false
		}, {
			title : " REG ID ",
			field : "reg_id",
			headerSort : false
		}, {
			title : " REG DATE ",
			field : "reg_date",
			headerSort : false
		}, {
			title : " MOD ID ",
			field : "mod_id",
			headerSort : false
		}, {
			title : " MOD DATE ",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setFolderInfoList(folderInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	foledrInfoTable = new Tabulator("#folderInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : folderInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : " PROJECT ",
			field : "prj_id",
			headerSort : false
		}, {
			title : " FOLDER ID ",
			field : "folder_id",
			headerSort : false
		}, {
			title : " PARENT FOLDER ID ",
			field : "parent_folder_id",
			headerSort : false
		}, {
			title : " FOLDER PATH ",
			field : "folder_path",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : " FOLDER NM ",
			field : "folder_nm",
			headerSort : false
		}, {
			title : " R FOLDER PATH NM ",
			field : "r_folder_path_nm",
			headerSort : false
		}, {
			title : " FOLDER TYPE ",
			field : "folder_type",
			headerSort : false
		}, {
			title : " TRA FOLDER NM ",
			field : "tra_folder_nm",
			headerSort : false
		}, {
			title : " REG ID ",
			field : "reg_id",
			headerSort : false
		}, {
			title : " REG DATE ",
			field : "reg_date",
			headerSort : false
		}, {
			title : " MOD ID ",
			field : "mod_id",
			headerSort : false
		}, {
			title : " MOD DATE ",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setFolderAuthList(folderInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	folderAuthTable = new Tabulator("#folderAuth", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : folderInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : " PROJECT ",
			field : "prj_id",
			headerSort : false
		}, {
			title : " FOLDER ID ",
			field : "folder_id",
			headerSort : false
		}, {
			title : " AUTH LVL1 ",
			field : "auth_lvl1",
			headerSort : false
		}, {
			title : " AUTH VAL ",
			field : "auth_val",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : " FOLDER AUTH ADD YN ",
			field : "folder_auth_add_yn",
			headerSort : false
		}, {
			title : " PARENT FOLDER INHERI YN ",
			field : "parent_folder_inheri_yn",
			headerSort : false
		}, {
			title : " READ AUTH YN ",
			field : "read_auth_yn",
			headerSort : false
		}, {
			title : " WRITE AUTH YN ",
			field : "write_auth_yn",
			headerSort : false
		}, {
			title : " DELETE AUTH YN ",
			field : "delete_auth_yn",
			headerSort : false
		}, {
			title : " REG ID ",
			field : "reg_id",
			headerSort : false
		}, {
			title : " REG DATE ",
			field : "reg_date",
			headerSort : false
		}, {
			title : " MOD ID ",
			field : "mod_id",
			headerSort : false
		}, {
			title : " MOD DATE ",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setEngStepList(engStepList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	dataMgEngStepTable = new Tabulator("#engStepDataMig", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : engStepList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false
		}, {
			title : "STEP CODE ID",
			field : "step_code_id",
			headerSort : false
		}, {
			title : "PLAN DATE",
			field : "plan_date",
			headerSort : false
		}, {
			title : "ACTUAL DATE",
			field : "actual_date",
			headerSort : false
		}, {
			title : "FORE DATE",
			field : "fore_date",
			headerSort : false
		}, {
			title : "TR ID",
			field : "tr_id",
			headerSort : false
		}, {
			title : "RTN STATUS",
			field : "rtn_status",
			headerSort : false
		}, {
			title : "STEP SEQ",
			field : "step_seq",
			headerSort : false
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setEngInfoList(engInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	engInfoTable = new Tabulator("#engInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : engInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISCIP CODE ID",
			field : "discip_code_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "DESIGNER ID",
			field : "designer_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PRFE DESIGNER ID",
			field : "prfe_designer_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "WBS CODE ID",
			field : "wbs_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REMARK",
			field : "remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CATEGORY CD ID",
			field : "category_cd_id",
			headerSort : false,
			minWidth : 110
		}, {
			title : "SIZE CODE ID",
			field : "size_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC TYPE ID",
			field : "doc_type_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setEmailTypeContentsList(emailTypeContentsList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	emailTypeContTable = new Tabulator("#emailTypeContents", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : emailTypeContentsList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "EMAIL TYPE ID",
			field : "email_type_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "EMAIL SENDER ADDR",
			field : "email_sender_addr",
			headerSort : false,
			minWidth : 120,
			hozAlign : "left"
		}, {
			title : "EMAIL RECEIVER ADDR",
			field : "email_receiver_addr",
			headerSort : false,
			minWidth : 130,
			hozAlign : "left"
		}, {
			title : "EMAIL REFERTO ADDR",
			field : "email_referto_addr",
			headerSort : false,
			minWidth : 120
		}, {
			title : "EMAIL BCC ADDR",
			field : "email_bcc_addr",
			headerSort : false,
			minWidth : 100
		}, {
			title : "FILE LINK YN",
			field : "file_link_yn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FILE RENAME YN",
			field : "file_rename_yn",
			headerSort : false,
			minWidth : 100
		}, {
			title : "FILE NM PREFIX",
			field : "file_nm_prefix",
			headerSort : false,
			minWidth : 100
		}, {
			title : "SUBJECT PREFIX",
			field : "subject_prefix",
			headerSort : false,
			minWidth : 100
		}, {
			title : "CONTENTS",
			field : "contents",
			headerSort : false,
			minWidth : 80,
			hozAlign: "left"
		}, {
			title : "FILE DIRECT ATTACH TYPE",
			field : "file_direct_attach_type",
			headerSort : false,
			minWidth : 120
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setEmailTypeList(emailTypeList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	emailTypeTable = new Tabulator("#emailType", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : emailTypeList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "EMAIL TYPE ID",
			field : "email_type_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "EMAIL TYPE",
			field : "email_type",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "EMAIL FEATURE",
			field : "email_feature",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "EMAIL DESC",
			field : "email_desc",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left"
		}, {
			title : "EMAIL AUTO<br>SEND YN",
			field : "email_auto_send_yn",
			headerSort : false,
			Width : 100
		}, {
			title : "REG ID",
			field : "reg_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}

		]
	});
}

function setEmailSendInfoList(emailTypeList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	emailSendInfoTable = new Tabulator("#emailSendInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : emailTypeList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 80
		}, {
			title : "EMAIL<br>TYPE ID",
			field : "email_type_id",
			headerSort : false,
			minWidth: 80
		}, {
			title : "EMAIL ID",
			field : "email_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 80
		}, {
			title : "SUBJECT",
			field : "subject",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 80
		}, {
			title : "CONTENTS",
			field : "contents",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 120
		}, {
			title : "EMAIL SENDER<br>USER ID",
			field : "email_sender_user_id",
			headerSort : false,
			minWidth: 100
		}, {
			title : "EMAIL SENDER ADDR",
			field : "email_sender_addr",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 135
		}, {
			title : "EMAIL RECEIVER ADDR",
			field : "email_receiver_addr",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 135
		}, {
			title : "EMAIL SEND DATE",
			field : "email_send_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 106
		}, {
			title : "EMAIL RECEIVERD<br>DATE",
			field : "email_receiverd_date",
			headerSort : false,
			minWidth: 106
		}, {
			title : "USER DATA00",
			field : "user_data00",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 95
		}, {
			title : "EMAIL REFER TO",
			field : "email_refer_to",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 100
		}, {
			title : "EMAIL BCC",
			field : "email_bcc",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign : "left",
			minWidth: 70
		}, {
			title : "SEND<br>YN",
			field : "send_yn",
			headerSort : false,
			minWidth: 40
		}, {
			title : "REG ID",
			field : "reg_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 80
		}, {
			title : "REG DATE",
			field : "reg_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 80
		}, {
			title : "MOD ID",
			field : "mod_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth: 80
		}

		]
	});
}

function setEmailReceiptInfoList(emailTypeList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	emailReceiptInfoTable = new Tabulator("#emailReceiptInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : emailTypeList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "EMAIL TYPE ID",
			field : "email_type_id",
			headerSort : false
		}, {
			title : "EMAIL ID",
			field : "email_id",
			headerSort : false
		}, {
			title : "EMAIL RECEIVER ADDR",
			field : "email_receiver_addr",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : "EMAIL RECEIVER DATE",
			field : "email_receiverd_date",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setEmailAttachInfoList(emailTypeList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	emailAttachInfoTable = new Tabulator("#emailAttachInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : emailTypeList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "EMAIL TYPE ID",
			field : "email_type_id",
			headerSort : false
		}, {
			title : "EMAIL ID",
			field : "email_id",
			headerSort : false
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false
		}, {
			title : "DOC NO",
			field : "doc_no",
			headerSort : false,
			hozAlign : "left"
		}, {
			title : "DOWN IP",
			field : "down_ip",
			headerSort : false
		}, {
			title : "DOWN ID",
			field : "down_id",
			headerSort : false
		}, {
			title : "DOWN DATE",
			field : "down_date",
			headerSort : false
		}, {
			title : "REV NO",
			field : "rev_no",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setDocumentIndexList(documentIndexList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	documentIndexTable = new Tabulator("#documentIndex", {
		//layout: "fitColumns",
		placeholder : "No Data Set",
		data : documentIndexList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC. NO",
			field : "doc_no",
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "DOC TYPE",
			field : "doc_type",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV CODE ID",
			field : "rev_code_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SFILE NM",
			field : "sfile_nm",
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "RFILE NM",
			field : "rfile_nm",
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "FILE TYPE",
			field : "file_type",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FILE SIZE",
			field : "file_size",
			headerSort : false,
			minWidth : 80
		}, {
			title : "TITLE",
			field : "title",
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "DEL YN",
			field : "del_yn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "UNREAD YN",
			field : "unread_yn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "LOCK YN",
			field : "lock_yn",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ATTACH FILE DATE",
			field : "attach_file_date",
			headerSort : false,
			minWidth : 120
		}, {
			title : "LAST REV YN",
			field : "last_rev_yn",
			headerSort : false,
			minWidth : 90
		}, {
			title : "TRANS REF DOC ID",
			field : "trans_ref_doc_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setDisciplineInfoList(disciplineInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	disciplineInfoTable = new Tabulator("#discipLineInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : disciplineInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "DISCIP CODE ID",
			field : "discip_code_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "DISCIP CODE",
			field : "discip_code",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "DISCIP DISPLAY",
			field : "discip_display",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign: "left"
		}, {
			title : "DISCIP DESC",
			field : "discip_desc",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			hozAlign: "left"
		}, {
			title : "DISCIP<br>ORDER",
			field : "discip_order",
			headerSort : false,
			width: 40
		}, {
			title : "REG ID",
			field : "reg_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false
		}

		]
	});
}

function setDisciplineFolderInfoList(disciplineFolderInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	disciplineFolderInfoTable = new Tabulator("#discipLineFolderInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : disciplineFolderInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false
		}, {
			title : "DISCIP CODE ID",
			field : "discip_code_id",
			headerSort : false
		}, {
			title : "DISCIP FOLDER SEQ",
			field : "discip_folder_seq",
			headerSort : false
		}, {
			title : "DISCIP FOLDER ID",
			field : "discip_folder_id",
			headerSort : false
		}, {
			title : "DISCIP ORDER",
			field : "discip_order",
			headerSort : false
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false
		}, {
			title : "REG DATE",
			field : "reg_date",
			headerSort : false
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false
		}

		]
	});
}

function setCordocInfoList(cordocInfoList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	cordocInfoTable = new Tabulator("#cordocInfo", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : cordocInfoList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC ID",
			field : "doc_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REV ID",
			field : "rev_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "FOLDER ID",
			field : "folder_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "IN/OUT",
			field : "in_out",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DOC DATE",
			field : "doc_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SENDER CODE ID",
			field : "sender_code_id",
			headerSort : false,
			minWidth : 100
		}, {
			title : "RECEIVER CODE ID",
			field : "receiver_code_id",
			headerSort : false,
			minWidth : 110
		}, {
			title : "DOC TYPE CODE ID",
			field : "doc_type_code_id",
			headerSort : false,
			minWidth : 110
		}, {
			title : "SERIAL",
			field : "serial",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CORR NO",
			field : "corr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "SYS CORR NO",
			field : "sys_corr_no",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REF CORR DOC ID",
			field : "ref_corr_doc_id",
			headerSort : false,
			minWidth : 120,
			hozAlign : "left"
		}, {
			title : "RESPONSIBILITY CODE ID",
			field : "responsibility_code_id",
			headerSort : false,
			minWidth : 140
		}, {
			title : "SEND RECV DATE",
			field : "send_recv_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ORIGINATOR",
			field : "originator",
			headerSort : false,
			minWidth : 80
		}, {
			title : "DISACT",
			field : "disact",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REPLY REQ DATE",
			field : "replyreqdate",
			headerSort : false,
			minWidth : 100
		}, {
			title : "IN DIST DATE",
			field : "indistdate",
			headerSort : false,
			minWidth : 80
		}, {
			title : "IN ACT DATE",
			field : "inactdate",
			headerSort : false,
			minWidth : 80
		}, {
			title : "PERSON IN CHARGE ID",
			field : "person_in_charge_id",
			headerSort : false,
			minWidth : 130
		}, {
			title : "REMARK",
			field : "remark",
			headerSort : false,
			minWidth : 80
		}, {
			title : "ITEM INFO CODE ID",
			field : "item_info_code_id",
			headerSort : false,
			minWidth : 120
		}, {
			title : "ITEM NOTE",
			field : "item_note",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USERDATA00",
			field : "userdata00",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USERDATA01",
			field : "userdata01",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USERDATA02",
			field : "userdata02",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USERDATA03",
			field : "userdata03",
			headerSort : false,
			minWidth : 80
		}, {
			title : "USERDATA04",
			field : "userdata04",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REPLY DOC NO",
			field : "replydocno",
			headerSort : false,
			minWidth : 100
		}, {
			title : "REF GRP",
			field : "ref_grp",
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG ID",
			field : "reg_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "RED DATE",
			field : "reg_date",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function setCodeSetList(codeSettingList) {
	$('#cntList').html('건수 : ' + tableNo + '개');
	codeSetTable = new Tabulator("#codeSettings", {
		layout : "fitColumns",
		placeholder : "No Data Set",
		data : codeSettingList,
		pagination : true,
		paginationSize : 100,
		Height : "100%",
		columns : [ {
			title : "PROJECT",
			field : "prj_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}, {
			title : "CODE TYPE",
			field : "set_code_type",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "CODE ID",
			field : "set_code_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}, {
			title : "CODE<br>FEATURE",
			field : "set_code_feature",
			headerSort : false,
			minWidth : 80
		}, {
			title : "CODE",
			field : "set_code",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "DESC",
			field : "set_desc",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "VAL",
			field : "set_val",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "ORDER",
			field : "set_order",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}, {
			title : "VAL2",
			field : "set_val2",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "VAL3",
			field : "set_val3",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "VAL4",
			field : "set_val4",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80,
			hozAlign : "left"
		}, {
			title : "IFC<br>YN",
			field : "ifc_yn",
			headerSort : false,
			minWidth : 40
		}, {
			title : "NUMBER TYPE",
			field : "number_type",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 100
		}, {
			title : "REG ID",
			field : "reg_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}, {
			title : "REG DATE",
			field : "reg_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD ID",
			field : "mod_id",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}, {
			title : "MOD DATE",
			field : "mod_date",
	        titleFormatter:function(cell, formatterParams, onRendered){
	        	let tmp = document.createElement("div");
	        	tmp.classList.add('home_grid_header');
	        	tmp.innerText = cell.getValue();
	        	return tmp;
	        },
			headerSort : false,
			minWidth : 80
		}

		]
	});
}

function table_retriveBtn() {
	dataMigSelectPrj_id = $('#dataMigrationPrjId').val();

	if (dataMigSelectPrj_id != "") {
		getTableCodeSetList();
	} else {
		alert("프로젝트를 선택하고 조회 바랍니다.");
	}

}

/**
 * From Excel 버튼을 눌렀을 경우 작동
 * @returns
 */
function table_fromExcelBtn() {
	document.getElementsByName("migExcelfile")[0].click();
}

function migFileUp(obj) {

	let form = $('#migUploadForm_PathList')[0];
	let formData = new FormData(form);
	formData.set("prj_id", getPrjInfo().id);

	loadingWithMask();

	// 엑셀로부터 들어온 데이터 true
	isFromExcel = true;

	if (set_table_type == "P_PRJ_CODE_SETTINGS") {
		$.ajax({
			url : "migCodeSettingsUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migCodeSettingsGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_CORDOC_INFO") {
		$.ajax({
			url : "migCordocInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migCordocInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_DISCIPLINE_FOLDER_INFO") {
		$.ajax({
			url : "migDisciplineFolderInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migDisciplineFolderInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_DISCIPLINE_INFO") {
		$.ajax({
			url : "migDisciplineInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migDisciplineInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_DOCUMENT_INDEX") {
		$.ajax({
			url : "migDocumentIndexUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result.model.dataFromExcel);

				migDocumentIndexGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_ATTACH_INFO") {
		$.ajax({
			url : "migEmailAttachInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEmailAttachInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_RECEIPT_INFO") {
		$.ajax({
			url : "migEmailReceiptInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEmailReceiptInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_SEND_INFO") {
		$.ajax({
			url : "migEmailSendInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEmailSendInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_TYPE") {
		$.ajax({
			url : "migEmailTypeUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEmailTypeGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_TYPE_CONTENTS") {
		$.ajax({
			url : "migEmailTypeConUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEmailTypeConGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_ENG_INFO") {
		$.ajax({
			url : "migEngInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEngInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_ENG_STEP") {
		$.ajax({
			url : "migEngStepUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				migEngStepGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_FOLDER_AUTH") {
		$.ajax({
			url : "migFolderAuthUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migFolderAuthGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_FOLDER_INFO") {
		$.ajax({
			url : "migFolderInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migFolderInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_GENDOC_INFO") {
		$.ajax({
			url : "migGendocInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migGendocInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_GROUP_MEMBER") {
		$.ajax({
			url : "migGroupMemberUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migGroupMemberGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_INFO") {
		$.ajax({
			url : "migPrjInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migPrjInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_MEMBER") {
		$.ajax({
			url : "migPrjMemberUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migPrjMemberGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_PRO_INFO") {
		$.ajax({
			url : "migProInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migProInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_PRO_STEP") {
		$.ajax({
			url : "migProStepUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migProStepGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_PROCESS_INFO") {
		$.ajax({
			url : "migProcessInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migProcessInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_SDC_INFO") {
		$.ajax({
			url : "migSdcInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migSdcInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_SDC_STEP") {
		$.ajax({
			url : "migSdcStepUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migSdcStepGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_STR_FILE") {
		$.ajax({
			url : "migStrFileUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migStrFileGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_STR_INFO") {
		$.ajax({
			url : "migStrInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migStrInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_SYSTEM_PREFIX") {
		$.ajax({
			url : "migSystemPrefixUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migSystemPrefixGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_TR_FILE") {
		$.ajax({
			url : "migTrFileUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migTrFileGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_TR_INFO") {
		$.ajax({
			url : "migTrInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migTrInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_VDR_INFO") {
		$.ajax({
			url : "migVdrInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migVdrInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_VDR_STEP") {
		$.ajax({
			url : "migVdrStepUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migVdrStepGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_VTR_FILE") {
		$.ajax({
			url : "migVtrFileUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migVtrFileGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_VTR_INFO") {
		$.ajax({
			url : "migVtrInfoUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migVtrInfoGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_PRJ_WBS_CODE") {
		$.ajax({
			url : "migWbsCodeUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migWbsCodeGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	} else if (set_table_type == "P_USERS") {
		$.ajax({
			url : "migUsersUpload.do",
			type : "POST",
			enctype : 'multipart/form-data',
			data : formData,
			processData : false,
			contentType : false,
			cache : false,
			// timeout : 600000,
			success : function(result) {
				form["migExcelfile"].value = "";
				closeLoadingWithMask();

				if (result.model.status != 'SUCCESS') {
					// error exception alert
					alert(result.model.status)
					return;
				}

				console.log(result);
				migUsersGridSet(result.model.dataFromExcel);
				alert("Excel Loaded Successfully");
			},
			error : function(e) {
				console.log("ERROR : ", e);
				alert("fail");
				closeLoadingWithMask();
			}
		});
	}
}

function migUsersGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.user_id = row[colIdx++];
		getRow.user_kor_nm = row[colIdx++];
		getRow.user_eng_nm = row[colIdx++];
		getRow.user_address = row[colIdx++];
		getRow.user_group_nm = row[colIdx++];
		getRow.user_type = row[colIdx++];
		getRow.passwd = row[colIdx++];
		getRow.company_nm = row[colIdx++];
		getRow.first_login_yn = row[colIdx++];
		getRow.hld_offi_gbn = row[colIdx++];
		getRow.job_tit_cd = row[colIdx++];
		getRow.offi_res_cd = row[colIdx++];
		getRow.email_addr = row[colIdx++];
		getRow.offi_tel = row[colIdx++];
		getRow.admin_yn = row[colIdx++];
		getRow.sfile_nm = row[colIdx++];
		getRow.rfile_nm = row[colIdx++];
		getRow.file_path = row[colIdx++];
		getRow.file_size = row[colIdx++];
		getRow.last_con_prj_id = row[colIdx++];
		getRow.remote_ip = row[colIdx++];
		getRow.user_st = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setMigUsersList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	usersTable.redraw();
}

function migWbsCodeGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.wbs_code_id = row[colIdx++];
		getRow.wbs_code = row[colIdx++];
		getRow.wbs_desc = row[colIdx++];
		getRow.doc_no = row[colIdx++];
		getRow.wgtval = row[colIdx++];
		getRow.doc_type = row[colIdx++];
		getRow.discipline = row[colIdx++];
		getRow.code_seq = row[colIdx++];
		getRow.gad = row[colIdx++];
		getRow.distribute = row[colIdx++];
		getRow.information = row[colIdx++];
		getRow.siteact = row[colIdx++];
		getRow.sitecc = row[colIdx++];
		getRow.disact = row[colIdx++];
		getRow.discc = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setMigWbsCodeList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	wbsCodeTable.redraw();
}

function migVtrInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.tr_no = row[colIdx++];
		getRow.itr_no = row[colIdx++];
		getRow.tr_subject = row[colIdx++];
		getRow.discipline_code_id = row[colIdx++];
		getRow.inout = row[colIdx++];
		getRow.issued = row[colIdx++];
		getRow.tr_status = row[colIdx++];
		getRow.tr_issuepur_code_id = row[colIdx++];
		getRow.send_date = row[colIdx++];
		getRow.req_date = row[colIdx++];
		getRow.tr_writer_id = row[colIdx++];
		getRow.tr_modifier_id = row[colIdx++];
		getRow.tr_from = row[colIdx++];
		getRow.tr_to = row[colIdx++];
		getRow.chk_date = row[colIdx++];
		getRow.tr_remark = row[colIdx++];
		getRow.tr_distribute = row[colIdx++];
		getRow.ref_tr_no = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setVtrInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	vtrInfoTable.redraw();
}

function migVtrFileGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.rev_no = row[colIdx++];
		getRow.issue_step = row[colIdx++];
		getRow.rtn_status_code_id = row[colIdx++];
		getRow.doc_table = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setVtrFileList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	vtrFileTable.redraw();
}

function migVdrStepGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.step_code_id = row[colIdx++];
		getRow.plan_date = row[colIdx++];
		getRow.actual_date = row[colIdx++];
		getRow.fore_date = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.rtn_status = row[colIdx++];
		getRow.step_seq = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setVdrStepList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	vdrStepTable.redraw();
}

function migVdrInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.discip_code_id = row[colIdx++];
		getRow.designer_id = row[colIdx++];
		getRow.prfe_designer_id = row[colIdx++];
		getRow.wbs_code_id = row[colIdx++];
		getRow.remark = row[colIdx++];
		getRow.po_doc_no = row[colIdx++];
		getRow.po_doc_title = row[colIdx++];
		getRow.vendor_nm = row[colIdx++];
		getRow.design_part = row[colIdx++];
		getRow.category_cd_id = row[colIdx++];
		getRow.size_code_id = row[colIdx++];
		getRow.doc_type_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setVdrInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	vdrInfotable.redraw();
}

function migTrInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.tr_no = row[colIdx++];
		getRow.itr_no = row[colIdx++];
		getRow.tr_subject = row[colIdx++];
		getRow.discipline_code_id = row[colIdx++];
		getRow.inout = row[colIdx++];
		getRow.issued = row[colIdx++];
		getRow.tr_status = row[colIdx++];
		getRow.tr_issuepur_code_id = row[colIdx++];
		getRow.send_date = row[colIdx++];
		getRow.req_date = row[colIdx++];
		getRow.tr_writer_id = row[colIdx++];
		getRow.tr_modifier_id = row[colIdx++];
		getRow.tr_from = row[colIdx++];
		getRow.tr_to = row[colIdx++];
		getRow.chk_date = row[colIdx++];
		getRow.tr_remark = row[colIdx++];
		getRow.tr_distribute = row[colIdx++];
		getRow.ref_tr_no = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setTrInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	trInfoTable.redraw();
}

function migTrFileGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.rev_no = row[colIdx++];
		getRow.issue_step = row[colIdx++];
		getRow.rtn_status_code_id = row[colIdx++];
		getRow.doc_table = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setTrFileList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	trFileTable.redraw();
}

function migSystemPrefixGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.system_prefix_type = row[colIdx++];
		getRow.system_prefix_id = row[colIdx++];
		getRow.prefix = row[colIdx++];
		getRow.prefix_desc = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setSystemPrefixList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	systemPrefixTable.redraw();
}

function migStrInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.tr_no = row[colIdx++];
		getRow.itr_no = row[colIdx++];
		getRow.tr_subject = row[colIdx++];
		getRow.discipline_code_id = row[colIdx++];
		getRow.inout = row[colIdx++];
		getRow.issued = row[colIdx++];
		getRow.tr_status = row[colIdx++];
		getRow.tr_issuepur_code_id = row[colIdx++];
		getRow.send_date = row[colIdx++];
		getRow.req_date = row[colIdx++];
		getRow.tr_writer_id = row[colIdx++];
		getRow.tr_modifier_id = row[colIdx++];
		getRow.tr_from = row[colIdx++];
		getRow.tr_to = row[colIdx++];
		getRow.chk_date = row[colIdx++];
		getRow.tr_remark = row[colIdx++];
		getRow.tr_distribute = row[colIdx++];
		getRow.ref_tr_no = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setStrInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	strInfoTable.redraw();
}

function migStrFileGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.rev_no = row[colIdx++];
		getRow.issue_step = row[colIdx++];
		getRow.rtn_status_code_id = row[colIdx++];
		getRow.doc_table = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setStrFileList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	strFileTable.redraw();
}

function migSdcStepGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.step_code_id = row[colIdx++];
		getRow.plan_date = row[colIdx++];
		getRow.actual_date = row[colIdx++];
		getRow.fore_date = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.rtn_status = row[colIdx++];
		getRow.step_seq = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setSdcStepList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	sdcStepTable.redraw();
}

function migSdcInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.discip_code_id = row[colIdx++];
		getRow.designer_id = row[colIdx++];
		getRow.prfe_designer_id = row[colIdx++];
		getRow.wbs_code_id = row[colIdx++];
		getRow.remark = row[colIdx++];
		getRow.category_cd_id = row[colIdx++];
		getRow.size_code_id = row[colIdx++];
		getRow.doc_type_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setSdcInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	sdcInfoTable.redraw();
}

function migProStepGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.step_code_id = row[colIdx++];
		getRow.plan_date = row[colIdx++];
		getRow.actual_date = row[colIdx++];
		getRow.fore_date = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.rtn_status = row[colIdx++];
		getRow.step_seq = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setProStepList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	proStepTable.redraw();
}

function migProcessInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.process_id = row[colIdx++];
		getRow.class_lvl = row[colIdx++];
		getRow.process_type = row[colIdx++];
		getRow.process_desc = row[colIdx++];
		getRow.process_folder_path_id = row[colIdx++];
		getRow.drn_use_yn = row[colIdx++];
		getRow.process_order = row[colIdx++];
		getRow.doc_type = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setProcessInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	processInfoTable.redraw();
}

function migProInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.discip_code_id = row[colIdx++];
		getRow.designer_id = row[colIdx++];
		getRow.prfe_designer_id = row[colIdx++];
		getRow.wbs_code_id = row[colIdx++];
		getRow.remark = row[colIdx++];
		getRow.por_no = row[colIdx++];
		getRow.por_issue_date = row[colIdx++];
		getRow.po_no = row[colIdx++];
		getRow.po_issue_date = row[colIdx++];
		getRow.vendor_nm = row[colIdx++];
		getRow.vendor_doc_no = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setProInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	proInfoTable.redraw();
}

function migPrjMemberGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.user_id = row[colIdx++];
		getRow.dcc_yn = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setPrjMemberList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	memberTable.redraw();
}

function migPrjInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.prj_no = row[colIdx++];
		getRow.prj_nm = row[colIdx++];
		getRow.prj_full_nm = row[colIdx++];
		getRow.prj_hidden_yn = row[colIdx++];
		getRow.prj_type = row[colIdx++];
		getRow.client_code = row[colIdx++];
		getRow.ship_no = row[colIdx++];
		getRow.prj_order = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setPrjInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	infoTable.redraw();
}

function migGroupMemberGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.group_id = row[colIdx++];
		getRow.user_id = row[colIdx++];
		getRow.group_desc = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setGroupMemberList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	groupMemberTable.redraw();
}

function migGendocInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setGendocInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	gendocInfoTable.redraw();
}

function migFolderAuthGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.auth_lvl1 = row[colIdx++];
		getRow.auth_val = row[colIdx++];
		getRow.folder_auth_add_yn = row[colIdx++];
		getRow.parent_folder_inheri_yn = row[colIdx++];
		getRow.read_auth_yn = row[colIdx++];
		getRow.write_auth_yn = row[colIdx++];
		getRow.delete_auth_yn = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setFolderAuthList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	folderAuthTable.redraw();
}

function migFolderInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.parent_folder_id = row[colIdx++];
		getRow.folder_path = row[colIdx++];
		getRow.folder_nm = row[colIdx++];
		getRow.r_folder_path_nm = row[colIdx++];
		getRow.folder_type = row[colIdx++];
		getRow.tra_folder_nm = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setFolderInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	foledrInfoTable.redraw();
}

function migEngStepGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.step_code_id = row[colIdx++];
		getRow.plan_date = row[colIdx++];
		getRow.actual_date = row[colIdx++];
		getRow.fore_date = row[colIdx++];
		getRow.tr_id = row[colIdx++];
		getRow.rtn_status = row[colIdx++];
		getRow.step_seq = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEngStepList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	dataMgEngStepTable.redraw();
}

function migEngInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.discip_code_id = row[colIdx++];
		getRow.designer_id = row[colIdx++];
		getRow.prfe_designer_id = row[colIdx++];
		getRow.wbs_code_id = row[colIdx++];
		getRow.remark = row[colIdx++];
		getRow.category_cd_id = row[colIdx++];
		getRow.size_code_id = row[colIdx++];
		getRow.doc_type_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEngInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	engInfoTable.redraw();
}

function migEmailTypeConGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.email_type_id = row[colIdx++];
		getRow.email_sender_addr = row[colIdx++];
		getRow.email_receiver_addr = row[colIdx++];
		getRow.email_referto_addr = row[colIdx++];
		getRow.email_bcc_addr = row[colIdx++];
		getRow.file_link_yn = row[colIdx++];
		getRow.file_rename_yn = row[colIdx++];
		getRow.file_nm_prefix = row[colIdx++];
		getRow.subject_prefix = row[colIdx++];
		getRow.contents = row[colIdx++];
		getRow.file_direct_attach_type = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEmailTypeContentsList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	emailTypeContTable.redraw();
}

function migEmailTypeGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.email_type_id = row[colIdx++];
		getRow.email_type = row[colIdx++];
		getRow.email_feature = row[colIdx++];
		getRow.email_desc = row[colIdx++];
		getRow.email_auto_send_yn = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEmailTypeList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	emailTypeTable.redraw();
}

function migEmailSendInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.email_type_id = row[colIdx++];
		getRow.email_id = row[colIdx++];
		getRow.subject = row[colIdx++];
		getRow.contents = row[colIdx++];
		getRow.email_sender_user_id = row[colIdx++];
		getRow.email_sender_addr = row[colIdx++];
		getRow.email_receiver_addr = row[colIdx++];
		getRow.email_send_date = row[colIdx++];
		getRow.email_receiverd_date = row[colIdx++];
		getRow.user_data00 = row[colIdx++];
		getRow.email_refer_to = row[colIdx++];
		getRow.email_bcc = row[colIdx++];
		getRow.send_yn = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEmailSendInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	emailSendInfoTable.redraw();
}

function migEmailReceiptInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.email_type_id = row[colIdx++];
		getRow.email_id = row[colIdx++];
		getRow.email_receiver_addr = row[colIdx++];
		getRow.email_receiverd_date = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEmailReceiptInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	emailReceiptInfoTable.redraw();

}

function migEmailAttachInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.email_type_id = row[colIdx++];
		getRow.email_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.doc_no = row[colIdx++];
		getRow.down_ip = row[colIdx++];
		getRow.down_id = row[colIdx++];
		getRow.down_date = row[colIdx++];
		getRow.rev_no = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setEmailAttachInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	emailAttachInfoTable.redraw();
}

function migDocumentIndexGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.doc_no = row[colIdx++];
		getRow.doc_type = row[colIdx++];
		getRow.rev_code_id = row[colIdx++];
		getRow.sfile_nm = row[colIdx++];
		getRow.rfile_nm = row[colIdx++];
		getRow.file_type = row[colIdx++];
		getRow.file_size = row[colIdx++];
		getRow.title = row[colIdx++];
		getRow.del_yn = row[colIdx++];
		getRow.unread_yn = row[colIdx++];
		getRow.lock_yn = row[colIdx++];
		getRow.attach_file_date = row[colIdx++];
		getRow.last_rev_yn = row[colIdx++];
		getRow.trans_ref_doc_id = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setDocumentIndexList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	documentIndexTable.redraw();
}

function migDisciplineInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.discip_code_id = row[colIdx++];
		getRow.discip_code = row[colIdx++];
		getRow.discip_display = row[colIdx++];
		getRow.discip_desc = row[colIdx++];
		getRow.discip_order = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setDisciplineInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	disciplineInfoTable.redraw();
}

function migDisciplineFolderInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.discip_code_id = row[colIdx++];
		getRow.discip_folder_seq = row[colIdx++];
		getRow.discip_folder_id = row[colIdx++];
		getRow.discip_order = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setDisciplineFolderInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	disciplineFolderInfoTable.redraw();
}

function migCordocInfoGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.doc_id = row[colIdx++];
		getRow.rev_id = row[colIdx++];
		getRow.folder_id = row[colIdx++];
		getRow.in_out = row[colIdx++];
		getRow.doc_date = row[colIdx++];
		getRow.sender_code_id = row[colIdx++];
		getRow.receiver_code_id = row[colIdx++];
		getRow.doc_type_code_id = row[colIdx++];
		getRow.serial = row[colIdx++];
		getRow.corr_no = row[colIdx++];
		getRow.sys_corr_no = row[colIdx++];
		getRow.ref_corr_doc_id = row[colIdx++];
		getRow.responsibility_code_id = row[colIdx++];
		getRow.send_recv_date = row[colIdx++];
		getRow.originator = row[colIdx++];
		getRow.disact = row[colIdx++];
		getRow.replyreqdate = row[colIdx++];
		getRow.indistdate = row[colIdx++];
		getRow.inactdate = row[colIdx++];
		getRow.person_in_charge_id = row[colIdx++];
		getRow.remark = row[colIdx++];
		getRow.item_info_code_id = row[colIdx++];
		getRow.item_note = row[colIdx++];
		getRow.userdata00 = row[colIdx++];
		getRow.userdata01 = row[colIdx++];
		getRow.userdata02 = row[colIdx++];
		getRow.userdata03 = row[colIdx++];
		getRow.userdata04 = row[colIdx++];
		getRow.replydocno = row[colIdx++];
		getRow.ref_grp = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setCordocInfoList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	cordocInfoTable.redraw();
}

function migCodeSettingsGridSet(data) {
	let tmpData = [];

	for (let i = 0; i < data.length; i++) {
		tableNo = i + 1;
		let getRow = {};
		let row = data[i];
		let colIdx = 0;
		getRow.prj_id = row[colIdx++];
		getRow.set_code_type = row[colIdx++];
		getRow.set_code_id = row[colIdx++];
		getRow.set_code_feature = row[colIdx++];
		getRow.set_code = row[colIdx++];
		getRow.set_desc = row[colIdx++];
		getRow.set_val = row[colIdx++];
		getRow.set_order = row[colIdx++];
		getRow.set_val2 = row[colIdx++];
		getRow.set_val3 = row[colIdx++];
		getRow.set_val4 = row[colIdx++];
		getRow.ifc_yn = row[colIdx++];
		getRow.number_type = row[colIdx++];
		getRow.reg_id = row[colIdx++];
		getRow.reg_date = row[colIdx++];
		getRow.mod_id = row[colIdx++];
		getRow.mod_date = row[colIdx++];

		tmpData.push(getRow);
	}

	setCodeSetList(tmpData);
	if (isFromExcel)
		excelData = tmpData;

	codeSetTable.redraw();
}

/**
 * save 버튼을 눌렀을 경우 작동
 * @returns
 */
function table_saveBtn() {
	
	// 엑셀로부터 불러온 데이터만 저장 가능
	if(!isFromExcel){
		alert("Only data from excel can be saved.");
		return;
	}
	
	
	if (set_table_type == "P_USERS") {
		if (!migUsers_chkData())
			return;

		//let tmpGridData = usersTable.getData();
		//let changedList = usersTable.getEditedCells();
		//let changeData = [];
		
		let tmpGridData = excelData;
		let changeData = [];
		//		console.log("changedList.lengt = "+changedList.length);
		//		console.log("tmpGridData.lengt = "+tmpGridData.length);
		for (let i = 0; i < changedList.length; i++) {
			let tmpIdx = tmpGridData[i].index;
			changedData.push(tmpIdx);
		}

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].user_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].user_id) {
					alert("[" + chkOverlap + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}

		$.ajax({
			url : 'migUsersSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
		$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
			url : 'insertSystemLog.do',
			type : 'POST',
			data : {
				prj_id : $('#selectPrjId').val(),
				pgm_nm : "DATA MIGRATION",
				method : "저장"
			},
			success : function onData() {

			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else {
		$(".pop_dataMigSaveConfirm").dialog("open");
	}
}

function dataMigSaveConfirm() {
	$(".pop_dataMigSaveConfirm").dialog("close");

	$.ajax({ // 페이지 진입시 p_prj_sys_log에 정보 저장
		url : 'insertSystemLog.do',
		type : 'POST',
		data : {
			prj_id : $('#selectPrjId').val(),
			pgm_nm : "DATA MIGRATION",
			method : "저장"
		},
		success : function onData() {

		},
		error : function onError(error) {
			console.error(error);
		}
	});
	
	

	if (set_table_type == "P_PRJ_CODE_SETTINGS") {
		if (!codeSettings_chkData())
			return;
		
		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].set_code_type;
			const chkOverlap3 = tmpGridData[i].set_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].set_code_type
						&& chkOverlap3 === tmpGridData[j].set_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}

		$.ajax({
			url : 'codeSettingsSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_CORDOC_INFO") {
		if (!cordocInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'cordocInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_DISCIPLINE_FOLDER_INFO") {
		if (!disciplineFolderInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].discip_code_id;
			const chkOverlap3 = tmpGridData[i].discip_folder_seq;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].discip_code_id
						&& chkOverlap3 === tmpGridData[j].discip_folder_seq) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'disciplineFolderInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_DISCIPLINE_INFO") {
		if (!disciplineInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].discip_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].discip_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'disciplineInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	}else if (set_table_type == "P_PRJ_DOCUMENT_INDEX") {
		if (!documentIndex_chkData())
			return;

		let tmpGridData = excelData;
		
		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];
		// 배열 쪼개기
		//let tmpStringsArr = [];
		
		for (let i = 0; i < tmpGridData.length ; i++) {
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		
		$.ajax({
			url : 'documentIndexSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_ATTACH_INFO") {
		if (!emailAttachInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].email_type_id;
			const chkOverlap3 = tmpGridData[i].email_id;
			const chkOverlap4 = tmpGridData[i].doc_id;
			const chkOverlap5 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].email_type_id
						&& chkOverlap3 === tmpGridData[j].email_id
						&& chkOverlap4 === tmpGridData[j].doc_id
						&& chkOverlap5 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "], " + "[" + chkOverlap5 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'emailAttachInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_RECEIPT_INFO") {
		if (!emailReceiptInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].email_type_id;
			const chkOverlap3 = tmpGridData[i].email_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].email_type_id
						&& chkOverlap3 === tmpGridData[j].email_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'emailReceiptInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_SEND_INFO") {
		if (!emailSendtInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].email_type_id;
			const chkOverlap3 = tmpGridData[i].email_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].email_type_id
						&& chkOverlap3 === tmpGridData[j].email_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'emailSendInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_TYPE") {
		if (!emailType_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].email_type_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].email_type_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'emailTypeSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_EMAIL_TYPE_CONTENTS") {
		if (!emailTypeCon_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].email_type_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].email_type_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'emailTypeContentsSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_ENG_INFO") {
		if (!engInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'engInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_ENG_STEP") {
		if (!engStep_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;
			const chkOverlap4 = tmpGridData[i].step_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id
						&& chkOverlap4 === tmpGridData[j].step_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'engStepSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				tableArrayData : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_FOLDER_AUTH") {
		if (!folderAuth_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].folder_id;
			const chkOverlap3 = tmpGridData[i].auth_lvl1;
			const chkOverlap4 = tmpGridData[i].auth_val;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].folder_id
						&& chkOverlap3 === tmpGridData[j].auth_lvl1
						&& chkOverlap4 === tmpGridData[j].auth_val) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'folderAuthSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_FOLDER_INFO") {
		if (!folderInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].folder_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].folder_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'folderInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_GENDOC_INFO") {
		if (!gendocInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'gendocInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_GROUP_MEMBER") {
		if (!groupMember_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].group_id;
			const chkOverlap3 = tmpGridData[i].user_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].group_id
						&& chkOverlap3 === tmpGridData[j].user_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'groupMemberSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_INFO") {
		if (!prjInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id) {
					alert("[" + chkOverlap + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'prjInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					initPrjSelectGridDataMigration();
					//헤더 프로젝트 리스트 그리드 refresh
					initPrjSelectGrid();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_MEMBER") {
		if (!prjMember_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].user_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].user_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'prjMemberSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowdatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_PRO_INFO") {
		if (!proInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'proInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_PRO_STEP") {
		if (!proStep_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;
			const chkOverlap4 = tmpGridData[i].step_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id
						&& chkOverlap4 === tmpGridData[j].step_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'proStepSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				tableArrayData : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_PROCESS_INFO") {
		if (!processInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].process_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].process_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'processInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				tableArrayData : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_SDC_INFO") {
		if (!sdcInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'sdcInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_SDC_STEP") {
		if (!sdcStep_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;
			const chkOverlap4 = tmpGridData[i].step_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id
						&& chkOverlap4 === tmpGridData[j].step_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'sdcStepSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				tableArrayData : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_STR_FILE") {
		if (!strFile_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].tr_id;
			const chkOverlap3 = tmpGridData[i].doc_id;
			const chkOverlap4 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].tr_id
						&& chkOverlap3 === tmpGridData[j].doc_id
						&& chkOverlap4 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'strFileSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_STR_INFO") {
		if (!strInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].tr_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].tr_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'strInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_SYSTEM_PREFIX") {
		if (!systemPrefix_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].system_prefix_type;
			const chkOverlap3 = tmpGridData[i].system_prefix_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].system_prefix_type
						&& chkOverlap3 === tmpGridData[j].system_prefix_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'systemPrefixSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_TR_FILE") {
		if (!trFile_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].tr_id;
			const chkOverlap3 = tmpGridData[i].doc_id;
			const chkOverlap4 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].tr_id
						&& chkOverlap3 === tmpGridData[j].doc_id
						&& chkOverlap4 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'trFileSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_TR_INFO") {
		if (!trInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].tr_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].tr_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'trInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VDR_INFO") {
		if (!vdrInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'vdrInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VDR_STEP") {
		if (!vdrStep_chkData())
			return;
		
		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].doc_id;
			const chkOverlap3 = tmpGridData[i].rev_id;
			const chkOverlap4 = tmpGridData[i].step_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].doc_id
						&& chkOverlap3 === tmpGridData[j].rev_id
						&& chkOverlap4 === tmpGridData[j].step_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'vdrStepSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				tableArrayData : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VTR_FILE") {
		if (!vtrFile_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].tr_id;
			const chkOverlap3 = tmpGridData[i].doc_id;
			const chkOverlap4 = tmpGridData[i].rev_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].tr_id
						&& chkOverlap3 === tmpGridData[j].doc_id
						&& chkOverlap4 === tmpGridData[j].rev_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2 + "], "
							+ "[" + chkOverlap3 + "], " + "[" + chkOverlap4
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'vtrFileSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_VTR_INFO") {
		if (!vtrInfo_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].tr_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].tr_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'vtrInfoSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	} else if (set_table_type == "P_PRJ_WBS_CODE") {
		if (!migWbsCode_chkData())
			return;

		let tmpGridData = excelData;

		// 리스트안 키값에 중복이 있는지 확인
		for (let i = 0; i < tmpGridData.length; i++) {
			const chkOverlap = tmpGridData[i].prj_id;
			const chkOverlap2 = tmpGridData[i].wbs_code_id;

			for (let j = i + 1; j < tmpGridData.length; j++) {
				if (chkOverlap === tmpGridData[j].prj_id
						&& chkOverlap2 === tmpGridData[j].wbs_code_id) {
					alert("[" + chkOverlap + "], " + "[" + chkOverlap2
							+ "] 키값이 중복됩니다.");
					return;
				}
			}
		}
		// 변화가 있는 row들 json으로 변환
		let tmpStrings = [];

		for (let i = 0; i < tmpGridData.length; i++) {
			/*if(tmpGridData[i].prj_id != selectPrjId) {
				alert("이 프로젝트에서 PROJECT "+tmpGridData[i].prj_id+"은 저장할 수 없습니다.");
				return;
			}*/
			tmpStrings.push(JSON.stringify(tmpGridData[i]));
		}
		//console.log(tmpStrings);

		$.ajax({
			url : 'migWbsCodeSave.do',
			type : 'POST',
			traditional : true,
			data : {
				prj_id : selectPrjId,
				jsonRowDatas : tmpStrings
			},
			success : function onData(data) {
				//console.log(data[0]);
				alert(data[0] == "SUCCESS" ? "SAVE complete" : "SAVE failed");

				if (data[0] == "SUCCESS") {
					getTableCodeSetList();
					return;
				}
			},
			error : function onError(error) {
				console.error(error);
			}
		});
	}
}

function dataMigSaveCancel() {
	$(".pop_dataMigSaveConfirm").dialog("close");
}

function migUsers_chkData() {
	let gridData = usersTable.getData();
	let mustInput = [ "user_id", "user_kor_nm", "user_eng_nm" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function migWbsCode_chkData() {
	let gridData = wbsCodeTable.getData();
	let mustInput = [ "prj_id", "wbs_code_id", "wbs_code", "wbs_desc" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function vtrInfo_chkData() {
	let gridData = vtrInfoTable.getData();
	let mustInput = [ "prj_id", "tr_id", "itr_no" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function vtrFile_chkData() {
	let gridData = vtrFileTable.getData();
	let mustInput = [ "prj_id", "tr_id", "doc_id", "rev_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function vdrStep_chkData() {
	let gridData = vdrStepTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "step_code_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function vdrInfo_chkData() {
	let gridData = vdrInfotable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function trInfo_chkData() {
	let gridData = trInfoTable.getData();
	let mustInput = [ "prj_id", "tr_id", "itr_no" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function trFile_chkData() {
	let gridData = trFileTable.getData();
	let mustInput = [ "prj_id", "tr_id", "doc_id", "rev_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function systemPrefix_chkData() {
	let gridData = systemPrefixTable.getData();
	let mustInput = [ "prj_id", "system_prefix_type", "system_prefix_id",
			"prefix" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function strInfo_chkData() {
	let gridData = strInfoTable.getData();
	let mustInput = [ "prj_id", "tr_id", "itr_no" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function strFile_chkData() {
	let gridData = strFileTable.getData();
	let mustInput = [ "prj_id", "tr_id", "doc_id", "rev_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function sdcStep_chkData() {
	let gridData = sdcStepTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "step_code_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function sdcInfo_chkData() {
	let gridData = sdcInfoTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function processInfo_chkData() {
	let gridData = processInfoTable.getData();
	let mustInput = [ "prj_id", "process_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function proStep_chkData() {
	let gridData = proStepTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "step_code_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function proInfo_chkData() {
	let gridData = proInfoTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function prjMember_chkData() {
	let gridData = memberTable.getData();
	let mustInput = [ "prj_id", "user_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function prjInfo_chkData() {
	let gridData = infoTable.getData();
	let mustInput = [ "prj_id", "prj_no" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function groupMember_chkData() {
	let gridData = groupMemberTable.getData();
	let mustInput = [ "prj_id", "group_id", "user_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function gendocInfo_chkData() {
	let gridData = gendocInfoTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function folderAuth_chkData() {
	let gridData = folderAuthTable.getData();
	let mustInput = [ "prj_id", "folder_id", "auth_lvl1", "auth_val" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function folderInfo_chkData() {
	let gridData = foledrInfoTable.getData();
	let mustInput = [ "prj_id", "folder_id", "parent_folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function engStep_chkData() {
	let gridData = dataMgEngStepTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "step_code_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function engInfo_chkData() {
	let gridData = engInfoTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function emailAttachInfo_chkData() {
	let gridData = emailAttachInfoTable.getData();
	let mustInput = [ "prj_id", "email_type_id", "email_id", "doc_id",
			"rev_id", "doc_no" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function emailReceiptInfo_chkData() {
	let gridData = emailReceiptInfoTable.getData();
	let mustInput = [ "prj_id", "email_type_id", "email_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function emailSendtInfo_chkData() {
	let gridData = emailSendInfoTable.getData();
	let mustInput = [ "prj_id", "email_type_id", "email_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function emailType_chkData() {
	let gridData = emailTypeTable.getData();
	let mustInput = [ "prj_id", "email_type_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function emailTypeCon_chkData() {
	let gridData = emailTypeContTable.getData();
	let mustInput = [ "prj_id", "email_type_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function documentIndex_chkData(data) {
	let gridData = documentIndexTable.getData();
	let mustInput = ["prj_id",
					"doc_id",
					"rev_id",
					"folder_id",
					"doc_no"];
	for(let i=0;i<gridData.length;i++){		
		for(let col=0;col<mustInput.length;col++) {
			let row = gridData[i];
			if(!row[mustInput[col]]){
				alert("필수 입력필드["+ mustInput[col] +"] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function disciplineInfo_chkData() {
	let gridData = disciplineInfoTable.getData();
	let mustInput = [ "prj_id", "discip_code_id", "discip_code" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function disciplineFolderInfo_chkData() {
	let gridData = disciplineFolderInfoTable.getData();
	let mustInput = [ "prj_id", "discip_code_id", "discip_folder_seq",
			"discip_folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function cordocInfo_chkData() {
	let gridData = cordocInfoTable.getData();
	let mustInput = [ "prj_id", "doc_id", "rev_id", "folder_id" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}
function codeSettings_chkData() {
	let gridData = codeSetTable.getData();
	let mustInput = [ "prj_id", "set_code_type", "set_code_id",
			"set_code_feature", "set_code" ];
	for (let i = 0; i < gridData.length; i++) {
		let row = gridData[i];
		for (let col = 0; col < mustInput.length; col++) {
			if (!row[mustInput[col]]) {
				alert("필수 입력필드[" + mustInput[col] + "] 미 입력");
				return false;
			}
		}
	}
	return true;
}

function getTableType() {
	var get_table_type = $('#tableList_type').val();
	if (get_table_type === '#sel_table01') {
		set_table_type = 'P_PRJ_CODE_SETTINGS';

		$("#sel_table01").css('display', 'block');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table02') {
		set_table_type = 'P_PRJ_CORDOC_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'block');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table03') {
		set_table_type = 'P_PRJ_DISCIPLINE_FOLDER_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'block');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table04') {
		set_table_type = 'P_PRJ_DISCIPLINE_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'block');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table05') {
		set_table_type = 'P_PRJ_DOCUMENT_INDEX';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'block');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table06_1') {
		set_table_type = 'P_PRJ_EMAIL_ATTACH_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'block');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table06_2') {
		set_table_type = 'P_PRJ_EMAIL_RECEIPT_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'block');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table06_3') {
		set_table_type = 'P_PRJ_EMAIL_SEND_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'block');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table06') {
		set_table_type = 'P_PRJ_EMAIL_TYPE';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'block');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table07') {
		set_table_type = 'P_PRJ_EMAIL_TYPE_CONTENTS';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'block');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table08') {
		set_table_type = 'P_PRJ_ENG_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'block');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table09') {
		set_table_type = 'P_PRJ_ENG_STEP';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'block');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table10_1') {
		set_table_type = 'P_PRJ_FOLDER_AUTH';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'block');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table10') {
		set_table_type = 'P_PRJ_FOLDER_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'block');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table11') {
		set_table_type = 'P_PRJ_GENDOC_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'block');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table12') {
		set_table_type = 'P_PRJ_GROUP_MEMBER';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'block');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table13') {
		set_table_type = 'P_PRJ_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'block');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table14') {
		set_table_type = 'P_PRJ_MEMBER';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'block');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table15') {
		set_table_type = 'P_PRJ_PRO_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'block');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table16') {
		set_table_type = 'P_PRJ_PRO_STEP';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'block');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table17') {
		set_table_type = 'P_PRJ_PROCESS_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'block');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table18') {
		set_table_type = 'P_PRJ_SDC_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'block');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table19') {
		set_table_type = 'P_PRJ_SDC_STEP';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'block');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table20') {
		set_table_type = 'P_PRJ_STR_FILE';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'block');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table21') {
		set_table_type = 'P_PRJ_STR_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'block');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table22') {
		set_table_type = 'P_PRJ_SYSTEM_PREFIX';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'block');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table23') {
		set_table_type = 'P_PRJ_TR_FILE';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'block');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table24') {
		set_table_type = 'P_PRJ_TR_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'block');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table25') {
		set_table_type = 'P_PRJ_VDR_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'block');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table26') {
		set_table_type = 'P_PRJ_VDR_STEP';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'block');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table27') {
		set_table_type = 'P_PRJ_VTR_FILE';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'block');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table28') {
		set_table_type = 'P_PRJ_VTR_INFO';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'block');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table29') {
		set_table_type = 'P_PRJ_WBS_CODE';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'block');
		$("#sel_table30").css('display', 'none');
	} else if (get_table_type === '#sel_table30') {
		set_table_type = 'P_USERS';

		$("#sel_table01").css('display', 'none');
		$("#sel_table02").css('display', 'none');
		$("#sel_table03").css('display', 'none');
		$("#sel_table04").css('display', 'none');
		$("#sel_table05").css('display', 'none');
		$("#sel_table06_1").css('display', 'none');
		$("#sel_table06_2").css('display', 'none');
		$("#sel_table06_3").css('display', 'none');
		$("#sel_table06").css('display', 'none');
		$("#sel_table07").css('display', 'none');
		$("#sel_table08").css('display', 'none');
		$("#sel_table09").css('display', 'none');
		$("#sel_table10_1").css('display', 'none');
		$("#sel_table10").css('display', 'none');
		$("#sel_table11").css('display', 'none');
		$("#sel_table12").css('display', 'none');
		$("#sel_table13").css('display', 'none');
		$("#sel_table14").css('display', 'none');
		$("#sel_table15").css('display', 'none');
		$("#sel_table16").css('display', 'none');
		$("#sel_table17").css('display', 'none');
		$("#sel_table18").css('display', 'none');
		$("#sel_table19").css('display', 'none');
		$("#sel_table20").css('display', 'none');
		$("#sel_table21").css('display', 'none');
		$("#sel_table22").css('display', 'none');
		$("#sel_table23").css('display', 'none');
		$("#sel_table24").css('display', 'none');
		$("#sel_table25").css('display', 'none');
		$("#sel_table26").css('display', 'none');
		$("#sel_table27").css('display', 'none');
		$("#sel_table28").css('display', 'none');
		$("#sel_table29").css('display', 'none');
		$("#sel_table30").css('display', 'block');
	}
}
