<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

<input type="hidden" id="contextpath" value="${pageContext.request.contextPath}">
<input type="hidden" id="selectPrjId">

                                <h4>Project Creation</h4>   
                                <div class="line mb20">
                                    <div class="left">
                                        <input type="checkbox" name="copyCheck" id="prjCreationCopyCheck">
                                        <label for="" class="mr10">이전공사 COPY</label>
											<div class="multi_sel_wrap">
												<input type="hidden" id="selectCreationCopyPrjId">
												<button class="multi_sel" id="selected_prj_creation_copy" style="display: none;">SELECT</button>
												<div class="multi_box" id="creation_copy_multi_box">
													<div class="prj_select_grid">
														<div class="prj_search" style="padding: 10px;">
															<div class="left">
																<label style="color: #000;" for="">Project Search</label> <input id="prj_search_creation_copy"
																	type="text">
																<button class="button btn_search" onclick="prjSearchCreationCopy()">검색</button>
															</div>
														</div>
														<div id="prj_list_grid_creation_copy"></div>
													</div>
												</div>
											</div>
                                        <%-- <select name="" id="copyProject">
                                        	<c:forEach var="i" items="${getPrjList}" varStatus="vs">
                                            	<option value="${i.prj_id}">${i.prj_nm}</option>
											</c:forEach>
                                        </select>
                                        <input type="hidden" id="selectCopyPrjId"> --%>
                                        <button class="button btn_default" id="prjCreationCopyApply" style="margin-left:20px; display:none;" onclick="apply()">Apply</button>
                                    </div>
                                </div>
                                <div class="sec">
                                    <div class="tbl_wrap">
                                        <table class="write project_Left_title">
                                            <colgroup>
                                                <col style="width:150px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th><span class="so_required">*</span>Project No</th>
                                                <td><input type="text" id="prj_no"></td>
                                            </tr>
                                            <tr>
                                                <th><span class="so_required">*</span>Project Name</th>
                                                <td><input type="text" id="prj_nm"></td>
                                            </tr>
                                            <tr>
                                                <th><span class="so_required">*</span>Project Full Name</th>
                                                <td><input class="dc_current_folder_path" type="text" id="prj_full_nm"></td>
                                            </tr>
                                            <tr>
                                                <th>Project Hidden</th>
                                                <td>
                                                    <input type="radio" name="prj_hidden_yn" value="Y">
                                                    <label for="" class="pr10">Yes</label>
                                                    <input type="radio" name="prj_hidden_yn" value="N" checked>
                                                    <label for="">No</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Project Type</th>
                                                <td>
                                                    <!-- <select name="prj_type" id="prj_type">
                                                    	<option value=""></option>
                                                    	<option value="exe">EXE</option>
                                                    	<option value="major">MAJOR</option>
                                                    	<option value="comp">COMP</option>
                                                    	<option value="est">EST</option>
                                                    	<option value="etc">ETC</option>
                                                    </select> -->
                                                    
                                                    <select name="prj_type" id="prj_type">
                                                    	<option value="0"></option>
                                                    	<c:forEach var="i" items="${getPrjType}" varStatus="vs">
			                                            	<option value="${i.code}">${i.code_nm}</option>
														</c:forEach>
                                                    </select>
                                                    <button class="button btn_small btn_blue" id="prjType" style="margin-left: 5px;">타입정의</button>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Client Code</th>
                                                <td><input type="text" id="client_code"></td>
                                            </tr>
                                            <tr>
                                                <th>Ship No</th>
                                                <td><input type="text" id="ship_no"></td>
                                            </tr>
                                            <tr>
                                                <th><span class="so_required">*</span>DCC 설정</th>
												<input type="hidden" id="DCCIDLIST">
             									<input type="hidden" id="DCCNAMEANDIDLIST">
												<!-- <input type="hidden" id="DCCIDEMAILLIST">  -->
                                                <td>
                                                	<button class="button btn_small btn_blue" id="dccSet" onclick="OrganizationChartMultiOpenDCCSet()">DCC 설정</button>
             										<ul id="DCCNAMELIST" style="display: inline-block;padding-top: 2px;"></ul>
             										<input type="hidden" id="DCCEMAILLIST">
             										<span id="DCCLISTHTML" style="display:none;"></span>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="btn_group right">
                                        <button class="button btn_blue" id="newPrjCreation" onclick="newPrjCreation()">New</button>
                                        <button class="button btn_blue" id="prjCreation" onclick="prjCreationSave()">Save</button>
                                        <button class="button btn_blue" id="prjEdit" onclick="prjCreationEdit()">Update</button>
                                    </div>
                                </div>
                                <div class="sec">
                                    <h4>Project List</h4>
                                    <div class="tbl_wrap scroll" id="projList">
                                        
                                    </div>    
                                </div>
                                
	<div class="dialog pop_prjType"
			title="PROJECT TYPE 관리" style="max-width: 800px;">
			
			<div class="pop_box">
				<h4>TYPE 목록</h4>
				<div class="tbl_wrap" id="prjType_Grids"></div>
				<div class="txt_right mb5">
					<button class="button btn_small btn_gray" onclick="prjType_save()">저장</button>
					<button class="button btn_small btn_gray"  onclick="prjType_cancle()">취소</button>
					<button class="button btn_small btn_gray" onclick="prjType_insert()">삽입</button>
					<button class="button btn_small btn_gray" onclick="prjType_delete()">삭제</button>
					<button class="button btn_small btn_gray" id="prjType_UpBtn">▲</button>
            		<button class="button btn_small btn_gray" id="prjType_DownBtn">▼</button>
				</div>
			</div>
		</div>

<div class="dialog pop_PrjTypeCodeDelete" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like delete to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="PrjTypeDelete()">Delete</button>
                <button class="button btn_purple" onclick="PrjTypeDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>
                                
<div class="dialog pop_dccSet" title="DCC User Assign" style="max-width:1000px;">
    <div class="pop_box">
        <div class="tbl_wrap">
            <table class="write">
                <tr>
                    <th>Search Condition</th>
                    <td>
                        <div class="line">
                            <input type="radio" name="searchUser" value="user_id" checked>
                            <label for="">User ID</label>
                            
                            <input type="radio" name="searchUser" value="user_kor_nm" class="ml10">
                            <label for="">Kor. Name</label>
                        </div>
                        <div class="line">
                            <input type="text" id="usersSearchKeyword">
                            <button class="button btn_search ml5" onclick="usersSearch()">Search</button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        
         <h4>User List</h4>
         <!-- <div class="txt_right mb5">
             <button class="button btn_small btn_gray" onclick="addUserDCC()">추가</button>
         </div> -->
         <div class="tbl_wrap" id="userList">
             
         </div>
        
         <h4>DCC List</h4>
         <div class="gray_small_box" id="projectCreation_small_box">
             <ul class="user_name" id="DCCLIST">
             </ul>
         </div>
         <div class="txt_right mt5">
             <button class="button btn_small btn_gray" onclick="DCCPopupClose()">확인</button>
         </div>
    </div>
</div>