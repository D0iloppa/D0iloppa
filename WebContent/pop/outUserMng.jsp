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
    <title>외부 사용자 관리</title>
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
    <script src="${pageContext.request.contextPath}/resource/js/onceLoad.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tab.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/tabulatormodule/sheetjs/xlsx.full.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/tabulator.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/moment.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/summernote-lite.js"></script>
	<script src="${pageContext.request.contextPath}/resource/summernote/lang/summernote-ko-KR.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/summernote/summernote-lite.css">
</head>
<script src="${pageContext.request.contextPath}/resource/js/angular.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/outUserMng.js"></script>
                                <div class="sec userReg">
                                    <div class="left">
                                        <div class="group_sch">
                                            <div class="btn_wrap">
                                                <button class="button btn_blue btn_small" onclick="allOpen()">All Open +</button>
                                                <button class="button btn_blue btn_small" onclick="allClose()">All Close -</button>
                                                <div class="line" style="display:none;">
                                                	<input type="hidden" id="clickNodeId">
                                                    <input type="text" id="clickNodeText" readonly="readonly" style="width: 120px;">
                                                    <button class="button btn_search ml5" onclick="groupUserSearch()">Search</button>
                                                </div>
                                                <br><br>
                                                <p class="red">* 부서명을 클릭하면 우측에 해당 부서에 속한 회원이 표시 됩니다.</p>
                                            </div>
                                        </div>
                                        <div class="group_sch_tree">
                                            <div class="pg_tree">
                                                <nav id="userRegGroup">
                                                </nav>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="right">
                                        <div id="pg04_uR" class="pg">
                                           <!--  <h4>User List</h4> -->
                                            <div class="pg_top">
                                                <div class="left">
                                                    <button class="button btn_blue mr5" id="userRegstration">User Registration</button>
                                                </div>
                                            </div>
                                            <hr>
                                 <div class="sch_area">
                                    <div class="sch_opt">
                                        <div class="sec">
                                            <h4>Search Conditions</h4>
                                            <div class="tbl_wrap">
                                                <table class="write">
                                                    <colgroup>
                                                        <col style="width:20%">
                                                        <col style="*">
                                                    </colgroup>
                                                    <tr>
                                                        <th>Search Conditions</th>
                                                        <td>
                                                            <div class="line">
                                                                <input type="radio" name="searchUser" value="user_id" checked="checked">
                                                                <label for="">User ID</label>

                                                                <input type="radio" name="searchUser" value="user_kor_nm" class="ml10">
                                                                <label for="">Kor. Name</label>
                                                            </div>
                                                            <div class="line">
                                                                <input type="text" id="usersSearchKeyword2">
                                                                <button class="button btn_search ml5" onclick="usersSearchReged()">Search</button>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <p class="result_line"><span>RESULT</span></p>
                                            <div id="sel_tab01" class="on">
                                                <div class="tbl_wrap" id="userLi">

                                                </div>
                                            </div>
                                            <div class="btn_wrap txt_right">
                                                <button class="button btn_blue" onclick="deleteSelectUser()">Delete User</button>
	                                            <button class="button btn_blue" onclick="getUserList()">모든 사용자 보기</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
	<div class="dialog pop_userJobCode"
			title="직책 관리" style="max-width: 800px;">
			<div class="pop_box">
				<h4>직책 목록</h4>
				<div class="tbl_wrap" id="userJobCode_Grids"></div>
				<div class="txt_right mb5">
					<button class="button btn_small btn_gray" onclick="userJobCode_save()">저장</button>
					<button class="button btn_small btn_gray"  onclick="userJobCode_cancle()">취소</button>
					<button class="button btn_small btn_gray" onclick="userJobCode_insert()">삽입</button>
					<button class="button btn_small btn_gray" onclick="userJobCode_delete()">삭제</button>
					<button class="button btn_small btn_gray" id="userJobCode_UpBtn">▲</button>
            		<button class="button btn_small btn_gray" id="userJobCode_DownBtn">▼</button>
				</div>
			</div>
		</div>
		
<div class="dialog pop_userJobCodeDelete" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="JobCodeDelete()">Delete</button>
                <button class="button btn_purple" onclick="JobCodeDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>

<div class="dialog pop_userRegstration" title="User Register" style="max-width:1000px;">
    <div class="pop_box">
        <div class="tbl_wrap">
            <table class="write project_Left_title">
                <colgroup>
                    <col style="">
                    <col style="width:*">
                </colgroup>
                <tr>
                    <th class="required">User ID<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_user_id" class="userReg" onchange="idCheck()">
                        <p id="idCheckWarning" class="info red"><!-- 이미 가입된 아이디입니다. --></p>
                        <input type="hidden" id="isExistUser">
                    </td>
                </tr>
                <tr>
                    <th class="required">User Name<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_user_kor_nm" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th class="required">User Eng Name<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_user_eng_nm" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th class="required">Password<i>Required</i></th>
                    <td>
                        <input type="password" id="reg_passwd" class="userReg" disabled>
                        <p class="red">
                            * 최초 비밀번호는 사번입니다.
                        </p>
                    </td>
                </tr>
                <tr>
                    <th class="required">사용자구분<i>Required</i></th>
                    <td>
                        <select name="" id="reg_user_type">
                            <option value="0"></option>
                            <option value="2">협력업체</option>
                            <option value="3">고객</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">재직구분<i>Required</i></th>
                    <td>
                        <select name="" id="reg_hld_offi_gbn">
                            <option value="1">재직</option>
                            <option value="2">휴직</option>
                            <option value="3">퇴직</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">직급<i>Required</i></th>
                    <td>
                        <select name="" class="job_tit_cd" id="reg_job_tit_cd">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>직책</th>
                    <td>
                        <select name="" class="offi_res_cd" id="reg_offi_res_cd">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">E-mail<i>Required</i></th>
                    <td>
                        <input type="text" class="half userReg" id="reg_email_addr_1">@<input type="text" class="half userReg" id="reg_email_addr_2">
                    </td>
                </tr>
                <tr>
                    <th>Phone</th>
                    <td><input type="text" class="half userReg" id="reg_offi_tel"></td>
                </tr>
                <tr>
                    <th class="required">Company Name<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_company_nm" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th>Group</th>
                    <td>
                        <div class="pg_tree">
                            <nav id="userRegPopGroup">
                            </nav>
                        </div>
                        <p class="groupLevel">
                            <span id="regGroupLevelPath"></span>
        					<input type="hidden" id="regOrgId" class="userReg">
                        </p>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="btn_group right">
        <button class="button btn_blue" onclick="userRegSave()">Save</button>
        <button class="button btn_gray" onclick="userRegCancel()">Cancel</button>
    </div>
</div>

<div class="dialog pop_userInfoUpdate" title="UserInfo Update" style="max-width:1000px;">
    <div class="pop_box">
        <div class="tbl_wrap">
            <table class="write">
                <colgroup>
                    <col style="width:30%">
                    <col style="width:*">
                </colgroup>
                <tr>
                    <th class="required">User ID<i>Required</i></th>
                    <td>
                        <input type="text" id="update_user_id" class="userReg" disabled>
                    </td>
                </tr>
                <tr>
                    <th class="required">User Name<i>Required</i></th>
                    <td>
                        <input type="text" id="update_user_kor_nm" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th class="required">User Eng Name<i>Required</i></th>
                    <td>
                        <input type="text" id="update_user_eng_nm" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th class="required">사용자구분<i>Required</i></th>
                    <td>
                        <select name="" id="update_user_type">
                            <option value="2">협력업체</option>
                            <option value="3">고객</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">재직구분<i>Required</i></th>
                    <td>
                        <select name="" id="update_hld_offi_gbn">
                            <option value="1">재직</option>
                            <option value="2">휴직</option>
                            <option value="3">퇴직</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">직급<i>Required</i></th>
                    <td>
                        <select name="" class="job_tit_cd" id="update_job_tit_cd">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>직책</th>
                    <td>
                        <select name="" class="offi_res_cd" id="update_offi_res_cd">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">E-mail<i>Required</i></th>
                    <td>
                        <input type="text" class="half userReg" id="update_email_addr_1">@<input type="text" class="half userReg" id="update_email_addr_2">
                    </td>
                </tr>
                <tr>
                    <th>Phone</th>
                    <td><input type="text" class="half userReg" id="update_offi_tel"></td>
                </tr>
                <tr>
                    <th class="required">Company Name<i>Required</i></th>
                    <td>
                        <input type="text" id="update_company_nm" class="userReg"> * 회사이름 미입력시 현대중공업파워시스템으로 입력됩니다.
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="btn_group right">
        <button class="button btn_blue" onclick="userUpdateSave()">Save</button>
        <button class="button btn_gray" onclick="userUpdateCancel()">Cancel</button>
    </div>
</div>


<div class="dialog pop_addGroup" title="Add group" style="max-width:400px">
    <div class="pop_box">
        <div class="pg_tree">
            <nav id="addPopGroup">
            </nav>
        </div>
        <p class="groupLevel">
        	<span id="addGroupLevelPath"></span>
        	<input type="hidden" id="addOrgId">
        </p>
        <p class="red" style="width:300px;">
            
        </p>
        <div class="btn_wrap txt_right">
            <button class="button btn_blue" onclick="addGroupSave()">Save</button>
            <button class="button btn_gray" onclick="addGroupCancel()">Cancel</button>
        </div>
    </div>
</div>


<div class="dialog pop_chgGroup" title="Change group" style="max-width:400px">
    <div class="pop_box">
        <div class="pg_tree">
            <nav id="chgPopGroup">
            </nav>
        </div>
        <p class="groupLevel">
        	<span id="groupLevelPath"></span>
        	<input type="hidden" id="chgOrgId">
        </p>
        <p class="red">
            * 부서를 선택하지 않고 '변경' 버튼을 클릭하면 해당 인원은 부서 지정이 해제됩니다.
        </p>
        <div class="btn_wrap txt_right">
            <button class="button btn_blue" onclick="chgGroupSave()">Save</button>
            <button class="button btn_gray" onclick="chgGroupCancel()">Cancel</button>
        </div>
    </div>
</div> 
<!-- 조직그룹삭제확인팝업 -->
<div class="dialog pop_deleteOrgConfirm" title="Delete Organization">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		부서에 속한 유저가 존재합니다. 그래도 삭제하시겠습니까?<br/>
    	(Selected organization: <span id="selectedOrg"></span>)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deleteOrgYes">Yes</button>
                <button class="button btn_blue" id="deleteOrgCancel1">Cancel</button>
            </div>
    </div>
</div>
<!-- 조직그룹삭제팝업 -->
<div class="dialog pop_deleteOrg" title="Delete Organization">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Do you want to delete a selected organization?<br/>
    	(Selected organization: <span id="selectedOrg2"></span>)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deleteSelectedOrg">Delete</button>
                <button class="button btn_blue" id="deleteOrgCancel">Cancel</button>
            </div>
    </div>
</div>
<!-- 사용자삭제팝업 -->
<div class="dialog pop_deleteUser" title="Delete User">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		선택한 유저를 삭제하시겠습니까? (겸직설정이 되어있지 않은 유저를 삭제하면 되돌릴 수 없습니다.)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deleteSelectedUser">Delete</button>
                <button class="button btn_blue" id="deleteUserCancel">Cancel</button>
            </div>
    </div>
</div>
<!-- 비밀번호 오류횟수 초기화확인팝업 -->
<div class="dialog pop_loginInitialize" title="Delete User">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		선택한 유저의 비밀번호 입력오류 횟수 초기화를 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" onclick="InitializeSelectedUser()">Confirm</button>
                <button class="button btn_blue" onclick="InitializeUserCancel()">Cancel</button>
            </div>
    </div>
</div>
<!-- 비밀번호 리셋 확인팝업 -->
<div class="dialog pop_passwdReset" title="Delete User">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		비밀번호 초기화를 진행하시겠습니까? (해당 유저의 이메일로 새 비밀번호를 발송해드립니다.)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="passwdResetConfirm">Confirm</button>
                <button class="button btn_blue" onclick="passwdResetCancel()">Cancel</button>
            </div>
    </div>
</div>
<div class="dialog pop_showDeleteUser" title="Deleted User" style="width:1200px">
	<div class="tbl_wrap" id="deleteUserTbl">
	</div>
</div>