<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/script.js"></script>
<form id="securityLogExcelUploadForm" name="securityLogExcelUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
<div class="pg_top">
	<div class="left">
		<h4>보안로그</h4>
	</div>
	<div class="right">
		<button class="button btn_purple" onclick="seculityLog_fromExcelBtn()">To Excel</button>
	</div>
</div>
<input type="hidden" name="logGubun" id="securityLog_logGubun">
<input type="hidden" name="fileName" id="securityLog_fileName">
<input type="hidden" name="from" id="securityLog_form_date">
<input type="hidden" name="to" id="securityLog_to_date">
<input type="hidden" name="prj_nm" id="seculityLog_prj_nm">
<input type="hidden" name="searchLogAtc" id="securityLog_searchLogAtc">
<input type="hidden" name="searchPrjLogAtc" id="securityLog_searchPrjLogAtc">
<input type="hidden" name="textLogAtc" id="securityLog_textLogAtc">
<input type="hidden" name="jsonRowDatas" id="securityLog_jsonRowDatas">
</form>

								<div class="sec">
                                    <div class="tbl_wrap" style="overflow-x: unset">
                                        <table class="write project_Left_title">
                                            <colgroup>
                                                <col style="width:150px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th> 조회기간 선택</th>
                                                <td>
                                                	<input type="text" id="select_log_fdate" class="date wsmallIn"> ~
                                                	<input type="text" id="select_log_ldate" class="date wsmallIn">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>LOG 구분</th>
                                                <td>
                                                	<input type="radio" name="logRadio" value="systemLog" checked="checked"><label>SYSTEM LOG</label>
                                                	<input type="radio" name="logRadio" value="documentsLog" style="margin-left: 30px"><label>DOCUMENTS LOG</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>PROJECT</th>
                                                <td>
                                                <div class="multi_sel_wrap">
													<input type="hidden" id="selectSecurityLogPrjId" value="all">
													<button class="multi_sel" id="selected_prj_securityLog">ALL PROJECT</button>
													<div class="multi_box" id="securityLog_multi_box">
														<div class="prj_select_grid">
															<div class="prj_search" style="padding: 10px;">
																<div class="left">
																	<button class="button btn_blue" onclick="securityLog_prjAllSelect()">All Project</button>
																	<label style="color: #000;" for="">Project Search</label>
																	<input id="prj_search_securityLog" type="text">
																	<button class="button btn_search" onclick="prjSearchSecurityLog()">검색</button>
																</div>
															</div>
															<div id="prj_list_grid_securityLog"></div>
														</div>
													</div>
												</div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>조회조건</th>
                                                <td>
                                                	<select name="securityLog_systemSelectType" id="securityLog_systemSelectType" style="width: 100px">
                                                    	<option value="all">ALL</option>
                                                    	<option value="userId">USER ID</option>
                                                    	<option value="userName">USER NAME</option>
                                                    	<option value="userIp">IP</option>
                                                    	<option value="method">METHOD</option>
                                                    </select>
                                                    <select name="securityLog_documentSelectType" id="securityLog_documentSelectType" style="display: none; width: 100px">
                                                    	<option value="all">ALL</option>
                                                    	<option value="userId">USER ID</option>
                                                    	<option value="userName">USER NAME</option>
                                                    	<option value="userIp">IP</option>
                                                    	<option value="method">METHOD</option>
                                                    	<option value="docNo">DOC. NO</option>
                                                    	<option value="docTitle">DOC. TITLE</option>
                                                    	<option value="path">PATH</option>
                                                    </select>
                                                    <input type="text" id="logSelect">
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="btn_group right">
                                        <button class="button btn_blue" id="newIpCreation" onclick="securityLogSearch()">검색</button>
                                    </div>
                                 </div>
                                 <div class="sec">
                                    <h4 style="float: left;">검색 내용</h4>
                                    <!-- 건수 label -->
                                    <label id="securityLogCntList" style="float: right;margin: 10px 8px 0 0;"></label>
                                    <div class="tbl_wrap table_grids" id="securitySysLogList"></div>
                                    <div class="tbl_wrap table_grids" id="securityDocLogList" style="display: none"></div>    
                                </div>