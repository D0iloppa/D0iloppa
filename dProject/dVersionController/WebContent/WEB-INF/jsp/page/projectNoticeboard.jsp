<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<input type="hidden" id="loginedUserId" value="${sessionScope.login.user_id}">
<input type="hidden" id="checkAdmin" value="${sessionScope.login.admin_yn}" >
	<div class="pg_top">
		<div class="right">
			<select name="" id="projectsearch_Atc">
				<option value="all">All</option>
				<option value="title">Title</option>
				<option value="contents">Contents</option>
			</select>
			<input type="text" id="projecttext_Atc">
			<button class="button btn_default" onclick="searchProjectnotice()">Retrieve</button>
			<button class="button btn_default" onclick="registerProjectnotice()">Reg</button>
		</div>
	</div>
	<input type="hidden" id="prj_id_Pnotice">
	<h4>게시판(Project)</h4>
	<div class="tbl_wrap boardList" id="projectNoticeboardList">
		
    </div>
    
<div class="dialog pop_registProject" title="프로젝트 게시판 등록/수정 페이지" style="max-width:1000px;">
    <div class="pop_box">
        					<div class="sec">
                                    <div class="tbl_wrap">
                                        <table class="write">
                                            <colgroup>
                                                <col style="width:100px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th>Title</th>
                                                <td>
                                                	<input type="text" id="project_titleW" style="width: 100%;">
                                                </td>
                                                <td style="width: 80px;">
                                                	<input type="checkbox" id="allPrjCN">
                                                	<label style="cursor: default;">전체공지</label>
                                                </td>
                                            </tr>
                                            <tr class="summerNtable">
                                                <th>Contents</th>
                                                <!-- <td><div rows="500" id="summernote" cols="500" style="max-height: 300px;"></div></td> -->
                                                <td colspan="2"><textarea rows="500" id="summernote_projectnotice" cols="500" style="min-height: 300px;"></textarea></td>
                                            </tr>
                                            <tr>
                                                <th>Attach Files</th>
                                                <td colspan="2">
                                                
                                                	<!-- <div id="viewFileWrap">
										       			<div id="viewFileBox">
										        			<div class="drop-zone-head-wrap">
																<ul class="drop-zone-head">
																	<li>파일명</li>
																	<li>용량</li>
																	<li>삭제</li>
																</ul>
															</div>
															<div id="viewFileList" class="file-drop-zone">
																
															</div>
														</div>
										       		</div> -->
                                                	<div class="modal-set-box">
	                                                	<div class="btn_wrap modal-set-box-title">
	                                                		<label for="directFile" class="btn_icon btn_green" style="cursor: pointer; padding-left: 3px;"><i class="fas fa-search"></i></label>
	                                                    	<label class="btn_icon btn_green" style="cursor: pointer; display: none;" onclick="deleteAllFile();" ><i class="fas fa-redo-alt"></i></label>
	                                                    	<span class="total-file-size">총용량 <span id="totalFileSize" style="color:#275195;">0.0KB</span></span>
	                                                    	<input type="file" id="directFile" name="directPrjNoticeFileList" multiple onchange="directSelectFile();">
	                                                	</div>
                                                		
                                                		<form name="uploadForm_prjN" id="uploadForm_prjN" enctype="multipart/form-data" method="post">
                                                			<div class="drop-zone-head-wrap">
																<ul class="drop-zone-head">
																	<li>파일명</li>
																	<li>용량</li>
																	<li>삭제</li>
																</ul>
															</div>
															<div id="dropZone" class="file-drop-zone">
																<div id="dropZoneInfo">
																	<p><i class="far fa-plus-square"></i></p>
																	<p>마우스로 파일을 끌어오세요.</p>
																</div>
															</div>
															<input type="hidden" id="bbs_seqPnotice" name="bbs_seq">
															<input type="hidden" id="prj_idPnotice" name="prj_id">
                                                		</form>
                                                	
                                                	</div>
                                                	<!-- <table style="list">
                                                    <tr>
                                                        <th>파일명</th>
                                                        <th>용량</th>
                                                        <th>삭제</th>
                                                    </tr>
                                                    <tr>
                                                        <td>품의서.xlsx</td> 임시 텍스트
                                                        <td class="txt_right">256.0KB</td> 임시 텍스트
                                                        <td class="txt_center"><button class="button btn_small btn_blue">삭제</button></td>
                                                    </tr>
                                                </table> -->
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
	                            <div class="btn_wrap txt_right">
	                            	<button class="button btn_orange" id="canclenoticebP" onclick="cancleboard_Project()">취소</button>
	                            	<button class="button btn_blue" id="updatenoticebP" onclick="updateboard_Project()">수정</button>
									<button class="button btn_blue" id="savenoticebP" onclick="saveboard_Project()">등록</button>
								</div>
    </div>
</div>    

<div class="dialog pop_detailProject" title="프로젝트 게시판 상세 페이지" style="max-width:1000px;">
    <div class="pop_box">
	        					<div class="sec">
	                                    <div class="tbl_wrap">
	                                        <table class="write">
	                                            <colgroup>
	                                                <col style="width:100px">
	                                                <col style="*">
	                                            </colgroup>
	                                            <tr>
	                                                <th>Title</th>
	                                                <td colspan="5"><input type="text" id="project_title" style="width: 100%;" readonly></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Writer</th>
	                                                <td><input type="text" id="project_writer" style="width: 100%;" readonly></td>
	                                                <th>Create Date</th>
	                                                <td><input type="text" id="project_createdate" style="width: 100%;"></td>
	                                                <th>Hits</th>
	                                                <td><input type="text" id="project_hits" style="width: 100%;" readonly></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Contents</th>
	<!--                                                 <td colspan="5"><textarea rows="500" id="contents" cols="500" style="max-height: 300px;" readonly></textarea></td> -->
	                                                <td colspan="5"><div id="project_contents" style="min-height: 300px;"></div></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Attach Files</th>
	                                                <td colspan="5">
	                                               	<div style="border: 1px solid #e0e0e0;">
	                                                	<div class="drop-zone-head-wrap">
																<ul class="drop-zone-head">
																	<li style="width: 88%;">파일명</li>
																	<li>용량</li>
																</ul>
														</div>
														<div id="detailViewFileWrap">
																
														</div>
													</div>
	                                                </td>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="line">
		                                <div class="right r_array">
											<button class="button btn_blue" id="project_noticeupdate" onclick="updatenotice_Project()">수정</button>
											<button class="button btn_orange" id="project_noticedelete" onclick="deleteboard_Project()">삭제</button>
											<button class="button btn_blue" onclick="cancledetail_Project()">취소</button>
										</div>
									</div>
	</div>    
</div>
	
<!-- 삭제확인팝업 -->
<div class="dialog pop_deletePrjNoticeConfirm" title="삭제">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
		게시물을 삭제 하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="deletePrjNoticeYes()">확인</button>
                <button class="button btn_blue" onclick="deletePrjNoticeCancle()">취소</button>
            </div>
    </div>
</div>	

<!-- 수정확인팝업 -->
<div class="dialog pop_updatePrjNoticeConfirm" title="수정">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
		게시물을 수정 하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="updatePrjNoticeYes()">확인</button>
                <button class="button btn_blue" onclick="updatePrjNoticeCancle()">취소</button>
            </div>
    </div>
</div>	
		
<!-- 파일 삭제확인팝업 -->
<div class="dialog pop_deletePrjNoticeFileConfirm" title="삭제">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
		등록되어 있는 파일을 삭제하실 경우 복구가 불가능합니다.<br/>정말 삭제하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="deletePrjNoticeFileYes()">확인</button>
                <button class="button btn_blue" onclick="deletePrjNoticeFileCancle()">취소</button>
            </div>
    </div>
</div>	
	
	
	
	
	
	
	
	