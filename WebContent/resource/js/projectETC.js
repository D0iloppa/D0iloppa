var designerPrjTable;
$(function(){
});
function OrganizationChartOpenPreDesigner(){
	$('#organizationUse').val('인계자');
	window.open('./pop/OrganizationChart.jsp','조직도','top=50, left=50, width=1500, height=650, resizable=yes');
}
function OrganizationChartOpenFutureDesigner(){
	$('#organizationUse').val('인수자');
	window.open('./pop/OrganizationChart.jsp','조직도','top=50, left=50, width=1500, height=650, resizable=yes');
}
function searchDesignerProject(){
	let user_id = $('#preDesignerSelectedUserId').val();
	$.ajax({
	    url: 'searchDesignerProject.do',
	    type: 'POST',
	    data:{
	    	user_id: user_id
	    },
	    success: function onData (data) {
	    	var prjList = [];
	    	for(i=0;i<data[0].length;i++){
	    		prjList.push({
	    			prjId: data[0][i].prj_id,
	    			No_Tbl: (data[0].length+1)-(i+1),
                    No: data[0][i].prj_no,
                    projName: data[0][i].prj_nm,
                    projFullName: data[0][i].prj_full_nm
	    		});
	    	}
	    	if(data[0].length!==0){
	    		$('#changeDesigner').css('display','inline-block');
	    	}
	    	setDesignerProject(prjList);
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
}
function setDesignerProject(prjList){
	designerPrjTable = new Tabulator("#designerPrjList", {
        selectable:true,
        data: prjList,
        placeholder:"No Data Set",
        layout: "fitColumns",
        columns: [{formatter:"rowSelection", title:"", hozAlign:"center", headerSort:false,width:1},
        	{
	            title: "Prj Id",
	            field: "prjId",
	            visible: false
        	},
        	{
                title: "No",
                field: "No_Tbl",
                width: 80
            },
            {
                title: "Project No",
                field: "No",
                width: 180,
                hozAlign: "left"
            },
            {
                title: "Project Name",
                field: "projName",
                hozAlign: "left"
            },
            {
                title: "Project Full Name",
                field: "projFullName",
                hozAlign: "left"
            }
        ],
    });
}
function changeDesigner(){
	let prfe_designer_id = $('#preDesignerSelectedUserId').val();
	let designer_id = $('#futureDesignerSelectedUserId').val();
	let person_in_charge_id = $('#futureDesignerSelectedUserId').val();
	if(designerPrjTable.getSelectedData().length===0){
		alert('디자이너를 변경할 프로젝트를 선택하세요.');
	}else if(designer_id==='' || designer_id===undefined || designer_id===null){
		alert('인수자가 선택되지 않았습니다.');
	}else{
		let prj_ids = '';
		let selectedPrj = designerPrjTable.getSelectedData();
		for(let i=0;i<selectedPrj.length;i++){
			prj_ids += selectedPrj[i].prjId + ',';
		}
		prj_ids = prj_ids.slice(0,-1);
		$.ajax({
		    url: 'IsProjectMember.do',
		    type: 'POST',
		    data:{
		    	prj_id: prj_ids,
		    	user_id: designer_id
		    },
		    success: function onData (data) {
		    	if(data[0]!==''){
		    		alert('인수자가 '+data[0]+'의 프로젝트 멤버가 아니기 때문에 디자이너로 설정할 수 없습니다. 해당 프로젝트를 제외하거나 인수자를 해당 프로젝트 멤버로 추가하세요.');
		    	}else{
		    		$.ajax({
		    		    url: 'changeDesigner.do',
		    		    type: 'POST',
		    		    data:{
		    		    	prj_id: prj_ids,
		    		    	prfe_designer_id: prfe_designer_id,
		    		    	designer_id: designer_id,
		    		    	person_in_charge_id: person_in_charge_id
		    		    },
		    		    success: function onData (data) {
		    		    	alert('디자이너를 변경했습니다.');
		    		    	searchDesignerProject();
		    		    },
		    		    error: function onError (error) {
		    		        console.error(error);
		    		    }
		    		});
		    	}
		    },
		    error: function onError (error) {
		        console.error(error);
		    }
		});
	}
}