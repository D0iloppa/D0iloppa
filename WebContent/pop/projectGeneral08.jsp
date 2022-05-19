<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PROJECT USER/GROUP 설정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/default.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/tabulator.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/contextmenu/jquery.contextMenu.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/all.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
    <!--jstree-->
    <script src="${pageContext.request.contextPath}/resource/js/jquery-3.4.1.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery-ui.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jstree.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/contextmenu/jquery.contextMenu.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/contextmenu/jquery.ui.position.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/all.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jqueryui.dialog.fullmode.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/script.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery_dialogextend.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery_dialogextend_min.js"></script>
    <%-- <script src="${pageContext.request.contextPath}/resource/js/contextmenu.js"></script> --%><!-- 공통JS -->
   	<%-- <script src="${pageContext.request.contextPath}/resource/js/onceLoad.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tab.js"></script> --%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/tabulatormodule/sheetjs/xlsx.full.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tabulator.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/moment.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/summernote-lite.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/lang/summernote-ko-KR.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/summernote/summernote-lite.css">
</head>
<style>
.button {
    line-height: 7px;
}
</style>
<script src="${pageContext.request.contextPath}/resource/js/angular.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/projectGeneral08Popup.js"></script>
<!-- 			<h4>PROJECT USER/GROUP 설정</h4> -->
			<div class="project_tab">
				<ul class="pr_tab_tit">
					<li><a href="#prUser" class="on">PROJECT USER</a></li>
					<li><a href="#prGroup">PROJECT GROUP</a></li>
				</ul>
				<div class="pr_tab_cont">
				<form id="excelUploadForm_pg08_prUser" name="excelUploadForm_pg08_prUser"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					<div class="pr_box pr01 on" id="prUser">
					
						<input type="hidden" name="fileName" id="fileName08_prUser"> 
						<input type="hidden" name="jsonRowdatas" id="jsonRowdatas08_prUser">
						<input type="hidden" name="prj_id" id="prj_id08_prUser"> 
						<input type="hidden" name="prj_nm" id="prj_nm08_prUser">

						<div class="btn_wrap">
							<!-- <button class="button btn_blue" onclick="prjUserApply()">APPLY</button> -->
							<button class="button btn_blue" onclick="prjMemberToExcel()">TO EXCEL</button>
						</div>

						<div class="pr_left">
							
							<div class="pg_top">
								<strong>ALL USER</strong><!-- &nbsp;&nbsp;<input type="checkbox" id="userAllSelect">&nbsp;전체선택 -->
								<div class="user_search">
									<div class="left">
										<span style="color:red;">※ shift(범위선택), ctrl(다중 행 선택)</span>
										<label style="color: #000;" for="">Search</label>
										<input id="pg08_user_search" type="text">
										<button class="button btn_search" onclick="pg08_userSearch()">검색</button>
									</div>
								</div>
							</div>
							<div class="tbl_wrap" id="prjAllUser"></div>
						</div>
						<div class="pr_mid">
							<button class="button btn_purple toright"
								onclick="prjMemberAssignment()">오른쪽으로 이동</button>
							<button class="button btn_purple toleft"
								onclick="prjMemberRemove()">왼쪽으로 이동</button>
						</div>
						<div class="pr_right">
							<div class="pg_top">
								<div class="left">
									<strong>ASSIGNED</strong> <input type="checkbox" class="ml5"
										id="prjmember_copy_checkbox"> <label for=""
										class="ml5 copyChkdiv08">COPY</label> <input type="hidden"
										id="selectCopyPrjMemeber">
									<div class="copyDiv08" style="display:none;">	
									<div class="multi_sel_wrap ml5" id="prjMember_multi_sel_wrap">
										<button class="multi_sel" id="prjmember_select_prj">SELECT</button>
										<div class="multi_box" id="prjMember_multi_box"></div>
									</div>
									<button class="button btn_blue ml5" onclick="copyPrjMember()">COPY</button>
									</div>
								</div>
							</div>
							<div class="tbl_wrap" id="prgAssigned"></div>
						</div>
					</div>
				</form>
				<form id="excelUploadForm_pg08_prGroup" name="excelUploadForm_pg08_prGroup"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					<div class="pr_box pr02" id="prGroup">

						<input type="hidden" name="fileName" id="fileName08_prGroup"> 
						<input type="hidden" name="jsonRowdatas" id="jsonRowdatas08_prGroup">
						<input type="hidden" name="prj_id" id="prj_id08_prGroup"> 
						<input type="hidden" name="prj_nm" id="prj_nm08_prGroup">
						<input type="hidden" name="group_id" id="group_id08_prGroup">

						<div class="pg_top w100">
							<div class="left">
								GROUP <select name="" id="selectPrjGroupList"
									onchange="selectPrjGroupChange()">
								</select>
								<button class="button btn_blue ml5" id="groupEdit">GROUP EDIT</button>
							</div>
							<div class="right">
								<!-- <button class="button btn_purple" onclick="prjGroupUserApply()">APPLY</button> -->
								<button class="button btn_purple"
									onclick="prjGroupMemberToExcel()">TO EXCEL</button>
							</div>
						</div>

						<div class="pr_left">
							<div class="pg_top">
								<strong>ALL USERS IN PROJECT</strong>
								<div class="user_search">
									<span style="color:red;">※ shift(범위선택), ctrl(다중 행 선택)</span>
									<label style="color: #000;" for="">Search</label>
									<input id="pg08_user_search2" type="text">
									<button class="button btn_search" onclick="pg08_userSearch2()">검색</button>
								</div>
							</div>
							<div class="tbl_wrap" id="prjAllUser2"></div>
						</div>
						<div class="pr_mid">
							<button class="button btn_purple toright"
								onclick="prjGroupMemberAssignment()">오른쪽으로 이동</button>
							<button class="button btn_purple toleft"
								onclick="prjGroupMemberRemove()">왼쪽으로 이동</button>
						</div>
						<div class="pr_right">
							<div class="pg_top">
								<div class="left">
									<strong>ASSIGNED USERS</strong>
								</div>
							</div>
							<div class="tbl_wrap" id="prgAssigned2"></div>
						</div>
					</div>
				</form>
				</div>
			</div>

<div class="dialog pop_groupEdit" title="Group Edit" style="max-width:1100px;">
    <div class="pop_box">
	
	<form id="excelUploadForm_pg08_groupEdit" name="excelUploadForm_pg08_groupEdit"  enctype="multipart/form-data" method="post" onsubmit="return false;">
        <div class="pg_top">
            <div class="left">
                <label for="">NAME</label>
                <input type="hidden" id="savedGorupCodeId">
                <input type="text" class="ml5" id="saveGroupName">
                <label for="" class="ml10">Description</label>
                <input type="text" class="ml5" id="groupDesc">
                
                <input type="hidden" name="fileName" id="fileName08_groupEdit"> 
				<input type="hidden" name="jsonRowdatas" id="jsonRowdatas08_groupEdit">
				<input type="hidden" name="prj_id" id="prj_id08_groupEdit"> 
				<input type="hidden" name="prj_nm" id="prj_nm08_groupEdit">
            </div>
        </div>
        <div class="pg_top">
            <div class="right">
                <button class="button btn_blue" onclick="groupToExcel()">To Excel</button>
                <button class="button btn_purple" onclick="deleteGroupPopup()">Delete</button>
                <button class="button btn_blue" onclick="newGroup()">New</button>
                <button class="button btn_blue" onclick="saveGroup()">Save</button>
            </div>
        </div>
        <hr>
        <div class="tbl_wrap" id="tblGroupEdit">

        </div>
	</form>
    </div>
</div>
<div class="dialog pop_PrjGroupDelete pop_doc_search" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="deleteGroup()">Delete</button>
                <button class="button btn_purple" onclick="deleteGroupCancel()">Cancel</button>
            </div>
    </div>
</div>
<script>
var selectPrjId = opener.document.getElementById('memeberSettingPrjId').value;
</script>