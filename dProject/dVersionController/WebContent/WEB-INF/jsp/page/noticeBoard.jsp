<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<input type="hidden" id="loginedUserId" value="${sessionScope.login.user_id}">
<input type="hidden" id="checkAdmin" value="${sessionScope.login.admin_yn}" >
	<div class="pg_top">
		<div class="right">
			<select name="" id="search_Atc">
				<option value="all">All</option>
				<option value="title">Title</option>
				<option value="contents">Contents</option>
			</select>
			<input type="text" id="text_Atc">
			<button class="button btn_default" onclick="searchnotice()">Retrieve</button>
			<button class="button btn_default" onclick="register()">Reg</button>
		</div>
	</div>
	<h4>공용 게시판</h4>
		<div class="tbl_wrap boardList" id="noticeBoardList">
        	                
        </div>
        <!-- <div class="ui-widget-overlay ui-front"></div> -->
  
<div class="dialog pop_detailP" title="공용 게시판 상세 페이지" style="max-width:1000px;">
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
	                                                <td colspan="5"><input type="text" id="title" style="width: 100%;" readonly></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Writer</th>
	                                                <td><input type="text" id="writer" style="width: 100%;" readonly></td>
	                                                <th>Create Date</th>
	                                                <td><input type="text" id="createdate" style="width: 100%;"></td>
	                                                <th>Hits</th>
	                                                <td><input type="text" id="hits" style="width: 100%;" readonly></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Contents</th>
	<!--                                                 <td colspan="5"><textarea rows="500" id="contents" cols="500" style="max-height: 300px;" readonly></textarea></td> -->
	                                                <td colspan="5"><div id="contents" style="min-height: 300px;"></div></td>
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
														<div id="detailViewFileWrapN">
																
														</div>
													</div>
	                                                </td>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="line">
		                                <div class="right r_array">
											<button class="button btn_blue" id="noticeupdate" onclick="updatenotice()">수정</button>
											<button class="button btn_orange" id="noticedelete" onclick="deleteboard()">삭제</button>
											<button class="button btn_blue" onclick="cancleNoticedetail()">취소</button>
										</div>
									</div>
	</div>    
</div> 

<div class="dialog pop_registP" title="공용 게시판 등록/수정 페이지" style="max-width:1000px;">
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
                                                	<input type="text" id="titleW" style="width: 100%;">
                                                </td>
                                            </tr>
                                            <tr class="summerNtable">
                                                <th>Contents</th>
                                                <!-- <td><div rows="500" id="summernote" cols="500" style="max-height: 300px;"></div></td> -->
                                                <td><textarea rows="500" id="summernote" cols="500" style="max-height: 300px;"></textarea></td>
                                            </tr>
                                            <tr>
                                                <th>Attach Files</th>
                                                <td>
                                                	<div class="modal-set-box">
	                                                	<div class="btn_wrap modal-set-box-title">
	                                                		<label for="directFileN" class="btn_icon btn_green" style="cursor: pointer; padding-left: 3px;"><i class="fas fa-search"></i></label>
	                                                    	<label class="btn_icon btn_green" style="cursor: pointer; display: none;" onclick="deleteAllFile();"><i class="fas fa-redo-alt"></i></label>
	                                                    	<span class="total-file-size">총용량 <span id="totalFileSizeN" style="color:#275195;">0.0KB</span></span>
	                                                    	<input type="file" id="directFileN" name="directFileList" multiple onchange="directSelectFileN();">
	                                                	</div>
                                                		
                                                		<form name="uploadForm_N" id="uploadForm_N" enctype="multipart/form-data" method="post">
                                                			<div class="drop-zone-head-wrap">
																<ul class="drop-zone-head">
																	<li>파일명</li>
																	<li>용량</li>
																	<li>삭제</li>
																</ul>
															</div>
															<div id="dropZoneN" class="file-drop-zone">
																<div id="dropZoneInfoN">
																	<p><i class="far fa-plus-square"></i></p>
																	<p>마우스로 파일을 끌어오세요.</p>
																</div>
															</div>
															<input type="hidden" id="bbs_seqnotice" name="bbs_seq">
                                                		</form>
                                                	
                                                	</div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
	                            <div class="btn_wrap txt_right">
	                            	<button class="button btn_orange" id="canclenoticeb" onclick="cancleboard()">취소</button>
	                            	<button class="button btn_blue" id="updatenoticeb" onclick="updateboard()">수정</button>
									<button class="button btn_blue" id="savenoticeb" onclick="saveboard()">등록</button>
								</div>
    </div>
</div>

<!-- <div class="dialog pop_registupP" title="공용 게시판 수정 페이지" style="max-width:1000px;">
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
                                                	<input type="text" id="titleWup" style="width: 100%;">
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Contents</th>
                                                <!-- <td><div rows="500" id="summernote" cols="500" style="max-height: 300px;"></div></td>
                                                <td><textarea rows="500" id="summernoteup" cols="500" style="max-height: 300px;"></textarea></td>
                                            </tr>
                                            <tr>
                                                <th>Attach Files</th>
                                                <td>
                                                	<div class="btn_wrap">
                                                		<button class="btn_icon btn_green"><i class="fas fa-search"></i></button>
                                                    	<button class="btn_icon btn_green"><i class="fas fa-redo-alt"></i></button>
                                                	</div>
                                                	<table style="list">
                                                    <tr>
                                                        <th>파일명</th>
                                                        <th>용량</th>
                                                        <th>삭제</th>
                                                    </tr>
                                                    <tr>
                                                        <td></td>
                                                        <td class="txt_right"></td>
                                                        <td class="txt_center"><button class="button btn_small btn_blue">삭제</button></td>
                                                    </tr>
                                                </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
	                            <div class="btn_wrap txt_right">
	                            	<button class="button btn_orange" id="canclenoticeb" onclick="cancleupdate()">취소</button>
									<button class="button btn_blue" id="updatenoticeb" onclick="updateboard()">수정</button>
								</div>
    </div>
</div>  -->

<!-- 삭제확인팝업 -->
<div class="dialog pop_deleteNoticeConfirm" title="삭제">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
		게시물을 삭제 하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="deleteNoticeYes()">확인</button>
                <button class="button btn_blue" onclick="deleteNoticeCancle()">취소</button>
            </div>
    </div>
</div>	

<!-- 수정확인팝업 -->
<div class="dialog pop_updateNoticeConfirm" title="수정">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
		게시물을 수정 하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="updateNoticeYes()">확인</button>
                <button class="button btn_blue" onclick="updateNoticeCancle()">취소</button>
            </div>
    </div>
</div>

<!-- 파일 삭제확인팝업 -->
<div class="dialog pop_deleteNoticeFileConfirm" title="삭제">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
		등록되어 있는 파일을 삭제하실 경우 복구가 불가능합니다.<br/>정말 삭제하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="deleteNoticeFileYes()">확인</button>
                <button class="button btn_blue" onclick="deleteNoticeFileCancle()">취소</button>
            </div>
    </div>
</div>

