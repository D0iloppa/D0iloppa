<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
								<div class="sch_area">
                                    <div class="sch_opt">
                                        <div class="sec">
                                            <h4>Search Conditions</h4>
                                            <div class="tbl_wrap">
                                                <table class="write">
                                                    <colgroup>
                                                        <col style="width:15%">
                                                        <col style="*">
                                                    </colgroup>
                                                    <tr>
                                                        <th>Search Conditions</th>
                                                        <td>
                                                            <div class="line">
                                                                <input type="radio" name="searchUser_um" value="user_id" checked="checked">
                                                                <label for="">User ID</label>

                                                                <input type="radio" name="searchUser_um" value="user_kor_nm" class="ml10">
                                                                <label for="">Kor. Name</label>
                                                            </div>
                                                            <div class="line">
                                                                <input type="text" id="usersSearchKeyword3_um">
                                                                <button class="button btn_search ml5" onclick="usersSearch_um()">Search</button>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <p class="result_line"><span>RESULT</span></p>
                                <div class="sec userReg">
                                    <div class="left">
                                        <div class="group_sch">
                                            <div class="btn_wrap">
                                                <button class="button btn_blue btn_small" onclick="allOpen_um()">ALL OPEN +</button>
                                                <button class="button btn_blue btn_small" onclick="allClose_um()">ALL CLOSE -</button>
                                                <div class="line">
                                                	<input type="hidden" id="clickNodeId_um">
                                                    <input type="text" id="clickNodeText_um" readonly="readonly">
                                                    <button class="button btn_search ml5" onclick="groupUserSearch_um()">Search</button>
                                                </div>
                                                <p class="red">* 그룹명을 클릭 후 검색을 하면 우측에 해당 그룹에 속한 회원이 표시 됩니다.</p>
                                            </div>
                                        </div>
                                        <div class="group_sch_tree">
                                            <div class="pg_tree">
                                                <nav id="userRegGroup_um">
                                                </nav>
                                            </div>
                                        </div>
                                    </div>
                                   <div class="right">
                                        <div id="pg04_um" class="pg">
                                            <h4>User List</h4>
                                            <div class="pg_top">
                                                <div class="left">
                                                    <button class="button btn_blue mr5" id="userRegstration_um">User Registration</button>
                                                    <button class="button btn_gray">인사정보 가져오기</button>
                                                </div>
                                                <div class="right">
                                                    <button class="button btn_blue" onclick="getUserList_um()">Search all users</button>
                                                </div>
                                            </div>
                                            <hr>
                                            <div id="sel_tab01_um" class="on">
                                                <div class="tbl_wrap" id="userLi_um">

                                                </div>
                                            </div>
                                            <div class="btn_wrap txt_right">
                                                <button class="button btn_blue" id="addGroup_um">Add group</button>
                                                <button class="button btn_blue" id="chgGroup_um">Change group</button>
                                                <button class="button btn_gray" onclick="deleteSelectUser_um()">Delete Selection</button>
                                            </div>
                                        </div>
                                    </div>
                                </div> 
                                                                   
<div class="dialog pop_userRegstration_um" title="User Register" style="max-width:1000px;">
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
                        <input type="text" id="reg_user_id_um" class="userReg" onchange="idCheck_um()">
                        <p id="idCheckWarning_um" class="info red"><!-- 이미 가입된 아이디입니다. --></p>
                        <input type="hidden" id="isExistUser_um">
                    </td>
                </tr>
                <tr>
                    <th class="required">User Name<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_user_kor_nm_um" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th class="required">User Eng Name<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_user_eng_nm_um" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th class="required">Password<i>Required</i></th>
                    <td>
                        <input type="password" id="reg_passwd_um" class="userReg" onchange="pwdCheck_um()">
                        <input type="hidden" id="pass_reg_um">
                        <p id="pwdCheckWarning_um" class="info red"></p>
                        <p class="red">
                            * 비밀번호는 10~20자의 영문 대, 소문자, 숫자, 특수문자 중 3개를 조합하여 사용해야 합니다. <br>
                            * 사용 가능 특수문자 : ` ~ ! @ # $ % ^ * ( ) - _ = +
                        </p>
                    </td>
                </tr>
                <tr>
                    <th class="required">Confirm Password<i>Required</i></th>
                    <td>
                        <input type="password" id="reg_passwd_confirm_um" class="userReg" onchange="pwdConfirmCheck_um()">
                        <p id="pwdConfirmWarning_um" class="info red"></p>
                        <p class="red">
                            * 비밀번호는 10~20자의 영문 대, 소문자, 숫자, 특수문자 중 3개를 조합하여 사용해야 합니다. <br>
                            * 사용 가능 특수문자 : ` ~ ! @ # $ % ^ * ( ) - _ = +
                        </p>
                    </td>
                </tr>
                <tr>
                    <th class="required">사용자구분<i>Required</i></th>
                    <td>
                        <select name="" id="reg_user_type_um">
                            <option value="2">협력업체</option>
                            <option value="3">고객</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">재직구분<i>Required</i></th>
                    <td>
                        <select name="" id="reg_hld_offi_gbn_um">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">직급<i>Required</i></th>
                    <td>
                        <select name="" id="reg_job_tit_cd_um">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">직책<i>Required</i></th>
                    <td>
                        <select name="" id="reg_offi_res_cd_um">
                            <option value=""></option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="required">E-mail<i>Required</i></th>
                    <td>
                        <input type="text" class="half userReg" id="reg_email_addr_1_um">@<input type="text" class="half userReg" id="reg_email_addr_2_um">
                    </td>
                </tr>
                <tr>
                    <th class="required">Phone<i>Required</i></th>
                    <td>
                        <input type="number" class="short userReg" id="reg_offi_tel_1_um">-<input type="number" class="short userReg" id="reg_offi_tel_2_um">-<input type="number" class="short userReg" id="reg_offi_tel_3_um">
                    </td>
                </tr>
                <tr>
                    <th class="required">Company Name<i>Required</i></th>
                    <td>
                        <input type="text" id="reg_company_nm_um" class="userReg">
                    </td>
                </tr>
                <tr>
                    <th>Group</th>
                    <td>
                        <div class="pg_tree">
                            <nav id="userRegPopGroup_um">
                            </nav>
                        </div>
                        <p class="groupLevel">
                            <span id="regGroupLevelPath_um"></span>
        					<input type="hidden" id="regOrgId_um" class="userReg">
                        </p>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="btn_group right">
        <button class="button btn_blue" onclick="userRegSave_um()">Save</button>
        <button class="button btn_gray" onclick="userRegCancel_um()">Cancel</button>
    </div>
</div>

<div class="dialog pop_addGroup_um" title="Add group" style="max-width:400px">
    <div class="pop_box">
        <div class="pg_tree">
            <nav id="addPopGroup_um">
            </nav>
        </div>
        <p class="groupLevel">
        	<span id="addGroupLevelPath_um"></span>
        	<input type="hidden" id="addOrgId_um">
        </p>
        <p class="red" style="width:300px;">
            
        </p>
        <div class="btn_wrap txt_right">
            <button class="button btn_blue" onclick="addGroupSave_um()">Save</button>
            <button class="button btn_gray" onclick="addGroupCancel_um()">Cancel</button>
        </div>
    </div>
</div>
                                    
<div class="dialog pop_chgGroup_um" title="Change group" style="max-width:400px">
    <div class="pop_box">
        <div class="pg_tree">
            <nav id="chgPopGroup_um">
            </nav>
        </div>
        <p class="groupLevel">
        	<span id="groupLevelPath_um"></span>
        	<input type="hidden" id="chgOrgId_um">
        </p>
        <p class="red">
            * 그룹을 선택하지 않고 '변경' 버튼을 클릭하면 해당 인원은 그룹 지정이 해제됩니다.
        </p>
        <div class="btn_wrap txt_right">
            <button class="button btn_blue" onclick="chgGroupSave_um()">Save</button>
            <button class="button btn_gray" onclick="chgGroupCancel_um()">Cancel</button>
        </div>
    </div>
</div> 
<!-- 조직그룹삭제확인팝업 -->
<div class="dialog pop_deleteOrgConfirm_um" title="Delete Organization">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		그룹에 속한 유저가 존재합니다. 그래도 삭제하시겠습니까?<br/>
    	(Selected organization: <span id="selectedOrg_um"></span>)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deleteOrgYes_um">Yes</button>
                <button class="button btn_blue" id="deleteOrgCancel1_um">Cancel</button>
            </div>
    </div>
</div>
<!-- 조직그룹삭제팝업 -->
<div class="dialog pop_deleteOrg_um" title="Delete Organization">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Do you want to delete a selected organization?<br/>
    	(Selected organization: <span id="selectedOrg2_um"></span>)
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deleteSelectedOrg_um">Delete</button>
                <button class="button btn_blue" id="deleteOrgCancel_um">Cancel</button>
            </div>
    </div>
</div>
<!-- 사용자삭제팝업 -->
<div class="dialog pop_deleteUser_um" title="Delete User">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		선택한 유저를 삭제하시겠습니까?
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" id="deleteSelectedUser_um">Delete</button>
                <button class="button btn_blue" id="deleteUserCancel_um">Cancel</button>
            </div>
    </div>
</div>
<!-- 로그인초기화확인팝업 -->
<div class="dialog pop_loginInitialize_um" title="Delete User">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		선택한 유저의 로그인 초기화를 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" onclick="InitializeSelectedUser_um()">Confirm</button>
                <button class="button btn_blue" onclick="InitializeUserCancel_um()">Cancel</button>
            </div>
    </div>
</div>                                    
                                    
                                    