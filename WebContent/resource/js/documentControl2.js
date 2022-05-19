$(function(){
    $(".pop_folderManager").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: "auto"
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
    $(document).on("click", "#folderManager", function() {
        $(".pop_folderManager").dialog("open");
        return false;
    });

    $(".pop_authSet").dialog({
        autoOpen: false,
        maxWidth: 1000,
        width: "auto"
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
    $(document).on("click", "#authSet", function() {
        $(".pop_authSet").dialog("open");
        return false;
    });
	$(".pop_Open").dialog({
		autoOpen: false,
		maxWidth: 1000,
		width: "auto"
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
	$(".pop_upload").dialog({
		autoOpen: false,
		maxWidth: 1000,
		width: "auto"
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
    
	

	

	var multiUploadTbl = [{
		"File Name": "TEST-U-BF301#-001",
		"Title": "타이틀",
		"Last Modified Date": "1.51 MB",
		"Path": "1.51 MB"
	}, {
		"File Name": "TEST-U-BF301#-001",
		"Title": "타이틀",
		"Last Modified Date": "1.51 MB",
		"Path": "1.51 MB"
	},{
		"File Name": "TEST-U-BF301#-001",
		"Title": "타이틀",
		"Last Modified Date": "1.51 MB",
		"Path": "1.51 MB"
	},{
		"File Name": "TEST-U-BF301#-001",
		"Title": "타이틀",
		"Last Modified Date": "1.51 MB",
		"Path": "1.51 MB"
	},{
		"File Name": "TEST-U-BF301#-001",
		"Title": "타이틀",
		"Last Modified Date": "1.51 MB",
		"Path": "1.51 MB"
	}];

	

	
	$.contextMenu({
		selector: '.context-menu-one',
		callback: function(key, options) {
			if (key == 'authSet') {


				$(".pop_authSet").dialog({
					autoOpen: false,
					maxWidth: 1000,
					width: "auto"
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
				$(".pop_authSet").dialog("open");

			} else if (key == 'folderManager') {

				$(".pop_folderManager").dialog({
					autoOpen: false,
					maxWidth: 1000,
					width: "auto"
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
				$(".pop_folderManager").dialog("open");

			} else if (key == 'indexReg') {

				$(".pop_indexReg").dialog({
					autoOpen: false,
					maxWidth: 1000,
					width: "auto"
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
				$(".pop_indexReg").dialog("open");

			} else if (key == 'checkin') {

				$(".pop_checkin").dialog({
					autoOpen: false,
					maxWidth: 600,
					width: "auto"
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
				$(".pop_checkin").dialog("open");

			};
		},
		items: {
			/*"paste": {name: "Paste", icon: "paste"},*/
			"new": {
				name: "New"
			},
			"rename": {
				name: "Rename"
			},
			"delete": {
				name: "Delete"
			},
			"cut": {
				name: "Cut"
			},
			"paste": {
				name: "Paste"
			},
			"checkin": {
				name: "Check-in"
			},
			"property": {
				name: "Property"
			},
			"indexReg": {
				name: "Index Register"
			},
			"authSet": {
				name: "권한관리"
			},
			"folderManager": {
				name: "폴더 관리자 지정"
			}
			/*"sep1": "---------",
			"quit": {name: "Quit", icon: function(){
			    return 'context-menu-icon context-menu-icon-quit';
			}}*/
		}
	});
    
    $(".pop_accHis").dialog({
        autoOpen: false,
        maxWidth: 1200,
        width: 800
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
    
//    var accHis_tbl = [{
//        DRNNo: "4",
//        docNo: "user02",
//        revision: "김철수",
//        Discipline: "현대중공업시스템>기술개발본부>개발팀",
//        name: "Hong gil dong",
//        stat: "gdhong@gmail.com",
//        date: "(주)이진엔지니"
//    }, {
//        DRNNo: "4",
//        docNo: "user02",
//        revision: "김철수",
//        Discipline: "현대중공업시스템>기술개발본부>개발팀",
//        name: "Hong gil dong",
//        stat: "gdhong@gmail.com",
//        date: "(주)이진엔지니"
//    },{
//        DRNNo: "4",
//        docNo: "user02",
//        revision: "김철수",
//        Discipline: "현대중공업시스템>기술개발본부>개발팀",
//        name: "Hong gil dong",
//        stat: "gdhong@gmail.com",
//        date: "(주)이진엔지니"
//    }, {
//        DRNNo: "4",
//        docNo: "user02",
//        revision: "김철수",
//        Discipline: "현대중공업시스템>기술개발본부>개발팀",
//        name: "Hong gil dong",
//        stat: "gdhong@gmail.com",
//        date: "(주)이진엔지니"
//    }, ];
    
});
var exeDown_doc_id;
function getAccessHisList() {
	$(".pop_accHis").dialog("open");
    var doc_id = documentListTable.getSelectedData()[0].bean.doc_id;
    var rev_id = documentListTable.getSelectedData()[0].bean.rev_id;
    var doc_no = documentListTable.getSelectedData()[0].bean.doc_no;
    var rev_no = documentListTable.getSelectedData()[0].bean.rev_no;
    
    exeDown_doc_id = doc_id;
    
    if(rev_no == null || rev_no == "") {
    	rev_no = "none";
    }
    
    $("#docNoRevNo").html(doc_no+", "+rev_no);
    
    $.ajax({
		url: 'getAccHisList.do',
	    type: 'POST',
	    data:{
	    	prj_id: selectPrjId,
	    	doc_id: doc_id,
	    	rev_id: rev_id,
	    },
	    success: function onData (data) {
	    	var accHis_tbl = [];
	    	for(i=0;i<data[0].length;i++){
	    		accHis_tbl.push({
	    			reg_date: getDateFormat(data[0][i].reg_date),
	    			reg_nm: data[0][i].reg_nm,
	    			remote_ip: data[0][i].remote_ip,
	    			method: data[0][i].method
	    		});
	    	}
	    	setAccessHisList(accHis_tbl);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
var accessTable;
function setAccessHisList(accHis_tbl) {
	 accessTable = new Tabulator("#accHis_tbl", {
	        data: accHis_tbl,
	        layout: "fitColumns",
	        placeholder:"No Data Set",
	        height: 200,
	        columns: [{
	                title: "DATE",
	                field: "reg_date"
	            },
	            {
	                title: "USER NAME",
	                field: "reg_nm",
	                width: 120
	            },
	            {
	                title: "IP",
	                field: "remote_ip"
	            },
	            {
	                title: "METHOD",
	                field: "method",
	                width: 120,
		            hozAlign: "left"
	            }
	        ],
	    });
}

function accessHisToExcel() {
	let gridData = accessTable.getData();
	
	let jsonList = [];
	
	// json형태로 변환하여 배열로 만듬
	for(let i=0; i<gridData.length ; i++)
		jsonList.push(JSON.stringify(gridData[i]));
	
	let fileName = 'AccessHistory';
	
	var f = document.accessExcelUploadForm;
	
	f.prj_id.value = selectPrjId;
	f.doc_id.value = exeDown_doc_id;
    f.fileName.value = fileName;
    f.jsonRowdatas.value = jsonList;	   
    f.action = "accessExcelDownload.do";
    f.submit();  
	
}