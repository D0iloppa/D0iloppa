<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style>
	/* 로딩*/
	#loading {
		height: 100%;
		left: 0px;
		position: fixed;
		_position:absolute; 
		top: 0px;
		width: 100%;
		filter:alpha(opacity=50);
		-moz-opacity:0.5;
		opacity : 0.5;
	}

	.loading {
		background-color: white;
		z-index: 199;
	}
	
	#loading_img{
		position:absolute; 
		top:50%;
		left:50%;
		height:35px;
		margin-top:-75px;	//	이미지크기
		margin-left:-75px;	//	이미지크기
		z-index: 200;
	}
</style>

<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/script.js"></script>

<h4>DATA MIGRATION</h4>

<form id="migUploadForm_PathList" name="migUploadForm_PathList" enctype="multipart/form-data" method="post" onsubmit="return false;">
<div class="pg_top">
	<div class="left">
		<label for="" class="mr10">Table List</label>
		<select id="tableList_type" class="pr_gn" onchange="getTableCodeSetList()">
			<option value="#sel_table01">P_PRJ_CODE_SETTINGS</option>
			<option value="#sel_table02">P_PRJ_CORDOC_INFO</option>
			<option value="#sel_table03">P_PRJ_DISCIPLINE_FOLDER_INFO</option>
			<option value="#sel_table04">P_PRJ_DISCIPLINE_INFO</option>
			<option value="#sel_table05">P_PRJ_DOCUMENT_INDEX</option>
			<option value="#sel_table06_1">P_PRJ_EMAIL_ATTACH_INFO</option>
			<option value="#sel_table06_2">P_PRJ_EMAIL_RECEIPT_INFO</option>
			<option value="#sel_table06_3">P_PRJ_EMAIL_SEND_INFO</option>
			<option value="#sel_table06">P_PRJ_EMAIL_TYPE</option>
			<option value="#sel_table07">P_PRJ_EMAIL_TYPE_CONTENTS</option>
			<option value="#sel_table08">P_PRJ_ENG_INFO</option>
			<option value="#sel_table09">P_PRJ_ENG_STEP</option>
			<option value="#sel_table10_1">P_PRJ_FOLDER_AUTH</option>
			<option value="#sel_table10">P_PRJ_FOLDER_INFO</option>
			<option value="#sel_table11">P_PRJ_GENDOC_INFO</option>
			<option value="#sel_table12">P_PRJ_GROUP_MEMBER</option>
			<option value="#sel_table13">P_PRJ_INFO</option>
			<option value="#sel_table14">P_PRJ_MEMBER</option>
			<option value="#sel_table15">P_PRJ_PRO_INFO</option>
			<option value="#sel_table16">P_PRJ_PRO_STEP</option>
			<option value="#sel_table17">P_PRJ_PROCESS_INFO</option>
			<option value="#sel_table18">P_PRJ_SDC_INFO</option>
			<option value="#sel_table19">P_PRJ_SDC_STEP</option>
			<option value="#sel_table20">P_PRJ_STR_FILE</option>
			<option value="#sel_table21">P_PRJ_STR_INFO</option>
			<option value="#sel_table22">P_PRJ_SYSTEM_PREFIX</option>
			<option value="#sel_table23">P_PRJ_TR_FILE</option>
			<option value="#sel_table24">P_PRJ_TR_INFO</option>
			<option value="#sel_table25">P_PRJ_VDR_INFO</option>
			<option value="#sel_table26">P_PRJ_VDR_STEP</option>
			<option value="#sel_table27">P_PRJ_VTR_FILE</option>
			<option value="#sel_table28">P_PRJ_VTR_INFO</option>
			<option value="#sel_table29">P_PRJ_WBS_CODE</option>
			<option value="#sel_table30">P_USERS</option>
		</select>
		
		<label for="" class="mr10" style="margin-left: 10px">Project List</label>
		<div class="multi_sel_wrap" style="display: inline-block; margin: 0 auto;">
			<input type="hidden" id="dataMigrationPrjId" value="">
			<button class="multi_sel" id="selected_prj_dataMigration">SELECT</button>
			<div class="multi_box" id="dataMigration_multi_box">
				<div class="prj_select_grid">
					<div class="prj_search" style="padding: 10px;">
						<div class="left">
							<label style="color: #000;" for="">Project Search</label> 
							<input id="prj_search_dataMigration"type="text">
							<button class="button btn_search" onclick="prjSearchdataMigration()">검색</button>
						</div>
					</div>
					<div id="prj_list_grid_dataMigration"></div>
				</div>
			</div>
		</div>
		<label id="cntList"></label>
	</div>
	<div class="right">
		<button class="button btn_blue" onclick="table_retriveBtn()">Retrive</button>
		<button class="button btn_purple" onclick="table_fromExcelBtn()">From Excel</button>
		<button class="button btn_blue" onclick="table_saveBtn()">Save</button>
	</div>	
</div>
<div class="sel_tab">
	<div id="sel_table01" class="on">
		<div class="tbl_wrap table_grids" id="codeSettings"></div>
	</div>
	<div id="sel_table02">
		<div class="tbl_wrap table_grids" id="cordocInfo"></div>
	</div>
	<div id="sel_table03">
		<div class="tbl_wrap table_grids" id="discipLineFolderInfo"></div>
	</div>
	<div id="sel_table04">
		<div class="tbl_wrap table_grids" id="discipLineInfo"></div>
	</div>
	<div id="sel_table05">
		<div class="tbl_wrap table_grids" id="documentIndex"></div>
	</div>
	<div id="sel_table06_1">
		<div class="tbl_wrap table_grids" id="emailAttachInfo"></div>
	</div>
	<div id="sel_table06_2">
		<div class="tbl_wrap table_grids" id="emailReceiptInfo"></div>
	</div>
	<div id="sel_table06_3">
		<div class="tbl_wrap table_grids" id="emailSendInfo"></div>
	</div>
	<div id="sel_table06">
		<div class="tbl_wrap table_grids" id="emailType"></div>
	</div>
	<div id="sel_table07">
		<div class="tbl_wrap table_grids" id="emailTypeContents"></div>
	</div>
	<div id="sel_table08">
		<div class="tbl_wrap table_grids" id="engInfo"></div>
	</div>
	<div id="sel_table09">
		<div class="tbl_wrap table_grids" id="engStepDataMig"></div>
	</div>
	<div id="sel_table10_1">
		<div class="tbl_wrap table_grids" id="folderAuth"></div>
	</div>
	<div id="sel_table10">
		<div class="tbl_wrap table_grids" id="folderInfo"></div>
	</div>
	<div id="sel_table11">
		<div class="tbl_wrap table_grids" id="gendocInfo"></div>
	</div>
	<div id="sel_table12">
		<div class="tbl_wrap table_grids" id="groupMember"></div>
	</div>
	<div id="sel_table13">
		<div class="tbl_wrap table_grids" id="prjInfo"></div>
	</div>
	<div id="sel_table14">
		<div class="tbl_wrap table_grids" id="prjMember"></div>
	</div>
	<div id="sel_table15">
		<div class="tbl_wrap table_grids" id="proInfo"></div>
	</div>
	<div id="sel_table16">
		<div class="tbl_wrap table_grids" id="proStep"></div>
	</div>
	<div id="sel_table17">
		<div class="tbl_wrap table_grids" id="processInfo"></div>
	</div>
	<div id="sel_table18">
		<div class="tbl_wrap table_grids" id="sdcInfo"></div>
	</div>
	<div id="sel_table19">
		<div class="tbl_wrap table_grids" id="sdcStep"></div>
	</div>
	<div id="sel_table20">
		<div class="tbl_wrap table_grids" id="strFile"></div>
	</div>
	<div id="sel_table21">
		<div class="tbl_wrap table_grids" id="strInfo"></div>
	</div>
	<div id="sel_table22">
		<div class="tbl_wrap table_grids" id="systemPrefix"></div>
	</div>
	<div id="sel_table23">
		<div class="tbl_wrap table_grids" id="trFile"></div>
	</div>
	<div id="sel_table24">
		<div class="tbl_wrap table_grids" id="trInfo"></div>
	</div>
	<div id="sel_table25">
		<div class="tbl_wrap table_grids" id="vdrInfo"></div>
	</div>
	<div id="sel_table26">
		<div class="tbl_wrap table_grids" id="vdrStep"></div>
	</div>
	<div id="sel_table27">
		<div class="tbl_wrap table_grids" id="vtrFile"></div>
	</div>
	<div id="sel_table28">
		<div class="tbl_wrap table_grids" id="vtrInfo"></div>
	</div>
	<div id="sel_table29">
		<div class="tbl_wrap table_grids" id="wbsCode"></div>
	</div>
	<div id="sel_table30">
		<div class="tbl_wrap table_grids" id="migUsers"></div>
	</div>
</div>
<input type="file" name="migExcelfile" style="display: none;" onchange="migFileUp(this)" />
</form>

<!-- 데이터 저장 확인팝업 -->
<div class="dialog pop_dataMigSaveConfirm" title="SAVE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
    		마이그레이션 진행시 기존에 저장된 프로젝트 데이터는 삭제 됩니다. 그래도 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="dataMigSaveConfirm()">Save</button>
                <button class="button btn_purple" onclick="dataMigSaveCancel()">Cancel</button>
            </div>
    </div>
</div>

