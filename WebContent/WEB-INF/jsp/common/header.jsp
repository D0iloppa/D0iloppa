<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <META http-equiv="Pragma" content="no-cache">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>도서관리시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/font.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/default.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/tabulator.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/contextmenu/jquery.contextMenu.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/all.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/selectize.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery.flexdatalist.min.css">
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
    <script src="${pageContext.request.contextPath}/resource/js/contextmenu.js"></script><!-- 공통JS -->
    <script src="${pageContext.request.contextPath}/resource/js/onceLoad.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tab.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/tabulatormodule/sheetjs/xlsx.full.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tabulator.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/moment.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/selectize.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flexdatalist.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/summernote-lite.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/lang/summernote-ko-KR.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/summernote/summernote-lite.css">
<%-- <script src="${pageContext.request.contextPath}/resource/js/documentControl.js"></script> --%>
<script src="${pageContext.request.contextPath}/resource/js/trOutgoing.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/trIncoming.js"></script>

<script src="${pageContext.request.contextPath}/resource/js/fileDownload.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/jquery.fileDownload.js"></script>

</head>
    <header>
        <h1><a href="#"><img src="${pageContext.request.contextPath}/resource/images/white_logo.png" alt=""></a></h1>
        
        <input type="hidden" id="context_path" value="${pageContext.request.contextPath}">
		<input type="hidden" id="session_user_id" value="${sessionScope.login.user_id}">
		<input type="hidden" id="session_user_eng_nm" value="${sessionScope.login.user_eng_nm}">
		<input type="hidden" id="session_user_kor_nm" value="${sessionScope.login.user_kor_nm}">
        
        <button class="btn_top">open Top menu</button>
        <div class="hd_form">
            <ul>
                <li>
                	<input type="hidden" id="newTab_prj_id" value="${newTab_prj_id}">
                	<input type="hidden" id="newTab_prj_nm" value="${getPrjInfo.prj_nm}">
                    <label for="">PROJECT</label>
				<div class="multi_sel_wrap">
					<input type="hidden" id="selectPrjId">
					<button class="multi_sel" id="selected_prj">SELECT</button>
					<div class="multi_box" id="header_multi_box">
						<div class="prj_select_grid">
							<div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prj_search"
										type="text">
									<button class="button btn_search" onclick="prjSearch()">검색</button>
								</div>
							</div>
							<div id="prj_list_grid"></div>
						</div>
						<%-- <div style="display: none;">
							<table class="header_prj_select_table">
								<tr>
									<th>Project No</th>
									<th>Project Name</th>
									<th>Project Full Name</th>
								</tr>
								<c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
									<tr class="prj_select" id="prj_id_is_${i.prj_id}">
										<td>${i.prj_no}</td>
										<td class="prj_nm">${i.prj_nm}</td>
										<td>${i.prj_full_nm}</td>
									</tr>
								</c:forEach>
							</table>
						</div> --%>
					</div>
				</div> <%-- <select name="" id="headerPrjSelect" onchange="headerPrjChange()">
                    <c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
                        	<option value="${i.prj_id}" <c:if test="${getLastConPrj.prj_id eq i.prj_id}">selected</c:if>>${i.prj_no} / ${i.prj_nm} / ${i.prj_full_nm}</option>
					</c:forEach>
                    </select> --%>
                </li>
                <li>
                    <label for="">SEARCH</label>
                    <input id="globalSearchInput" type="text"
				onKeyPress="if( event.keyCode==13 ){globalSearch(this.value);}">
			</li>
            </ul>
        </div>
        <div class="hd_right">
        	<a class="headerDic" style="margin-right: 20px;"><span>DICTIONARY</span></a>
            <a href="#this" class="myinfo"><span>${sessionScope.login.user_kor_nm} <i>${sessionScope.login.remote_ip}</i></span></a>
            <button onclick="logout()" class="btn_logout">LOGOUT</button>
        </div>
    </header>
<div class="dialog pop_myinfo" title="내정보">
    <div class="tbl_wrap">
        <table class="write">
            <tr>
                <th style="width:30%;">User ID</th>
                <td><input type="text" id="user_id" name="user_id" value="${sessionScope.login.user_id}" disabled></td>
            </tr>
            <tr>
                <th>User Kor Name</th>
                <td><input type="text" id="user_kor_nm" name="user_kor_nm" value="${sessionScope.login.user_kor_nm}" disabled></td>
            </tr>
            <tr>
                <th>User Eng Name</th>
                <td><input type="text" id="user_eng_nm" name="user_eng_nm" value="${sessionScope.login.user_eng_nm}"></td>
            </tr>
            <tr>
                <th>E-mail</th>
                <td><input type="text" class="short" id="email_addr0" name="email_addr0" value="${fn:split(sessionScope.login.email_addr,'@')[0]}"><span>@</span><input type="text" class="short" id="email_addr1" name="email_addr1" value="${fn:split(sessionScope.login.email_addr,'@')[1]}"></td>
            </tr>
            <tr>
                <th>Phone</th>
                <td><input type="text" id="offi_tel" name="offi_tel" value="${sessionScope.login.offi_tel}"></td>
                <%-- <td>
                    <input type="text" class="p_num" id="offi_tel0" name="offi_tel0" value="${fn:split(sessionScope.login.offi_tel,'-')[0]}"><span>~</span>
                    <input type="text" class="p_num" id="offi_tel1" name="offi_tel1" value="${fn:split(sessionScope.login.offi_tel,'-')[1]}"><span>~</span>
                    <input type="text" class="p_num" id="offi_tel2" name="offi_tel2" value="${fn:split(sessionScope.login.offi_tel,'-')[2]}">
                </td> --%>
            </tr>
            <tr>
                <th>전자서명</th>
                <td>
					<form id="updateUserInfoForm" name="updateUserInfoForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
						<div class="filebox">
					         <input class="upload-name" value="파일선택" id="userinfo_attachfilename" disabled="disabled">
					         <label for="userinfo_attach_filename" class="button btn_blue" id="userinfo_popup_attach">파일</label>
					         <input type="file" accept="image/*" name="updateUserInfo_File" id="userinfo_attach_filename" class="upload-hidden userinfoDocumentFileAttach">
						</div>
                    	<i class="input_info">*등록 가능한 파일형식 : JPEG, GIF, PNG, BMP, TIF</i>
                    	<span id="signexists" style="width:100%;"></span>
					</form>
					
					<form id="fm_formIO" name="fm_formIO" method="post" action="main.do" target="_blank">
						<input type="hidden" id="prj_id" name="prj_id"/>
					</form>
                </td>
            </tr>
        </table>
    </div>
    <div class="btn_group right">
        <button class="button btn_blue" id="updateUsersVO">SAVE</button>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resource/js/angular.min.js"></script>
</html>