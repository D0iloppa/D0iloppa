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
                                <h4>DICTIONARY SEARCH</h4>
                                
<form id="dictionaryUploadForm_PathList" name="dictionaryUploadForm_PathList" enctype="multipart/form-data" method="post" onsubmit="return false;">
                                <div class="pg_top">
                                    <div class="right dicbtns"> <!-- dicbtns common에 따로 스타일 줌 -->
                                        <button class="button btn_search" onclick="searchdictionary()">Search</button>
                                    </div>
                                </div>
                                
                                <div class="tbl_wrap">
                                	<table class="write">
                                		<tbody>
                                			<tr>
                                        		<th>약어</th>
	                                			<td><input type="text" class="full" id="search_Abbr"></td>
	                                			<th>영어명칭</th>
	                                			<td><input type="text" class="full" id="search_Eng"></td>
	                                			<th>한글명칭</th>
	                                			<td><input type="text" class="full" id="search_Kor"></td>
	                                			<th>해설</th>
	                                			<td><input type="text" class="full" id="search_Desc"></td>
                                        	</tr>
                                		</tbody>
                                	</table>
                                </div>
                                <div class="pg_top">
	                                <div class="right dicbtns">
	                                	<button class="button btn_blue" style="" onclick="deletedictionary()">Delete</button>
	                                    <button class="button btn_blue" id="savebutton" onclick="savedicionary()">Save</button>
	                                    <button class="button btn_blue" id="updatebutton" onclick="updatedicionary()">Update</button>
	                                    <button class="button btn_blue" onclick="newdictionary()">New</button>
	                                </div>
                                </div>
                                
                                <div class="sec">
                                    <div class="tbl_wrap">
                                        <table class="write">
                                            <colgroup>
                                                <col style="width:100px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th>약어</th>
                                                <td colspan="3"><input type="text" class="full" id="abbr" style="width: 100%;"></td>
                                            </tr>
                                            <tr>
                                                <th>영어명칭</th>
                                                <td colspan="3"><input type="text" class="full" id="eng" style="width: 100%;"></td>
                                            </tr>
                                            <tr>
                                                <th>한글명칭</th>
                                                <td colspan="3"><input type="text" class="full" id="kor" style="width: 100%;"></td>
                                            </tr>
                                            <tr>
                                                <th>해설</th>
                                                <td colspan="3"><textarea rows="500" class="full" id="desc" cols="500"></textarea></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <p class="result_line">
                                	<span>RESULT</span>
                                </p>
                                <div class="pg_top">
	                                <div class="right dicbtns">
	                                    <button class="button btn_blue" id="savebutton" onclick="excelsavedicionary()">Save</button>
	                                    <button class="button btn_blue" onclick="exceldictionary()">From Excel</button>
	                                </div>
                                </div>
                                	<div class="tbl_wrap scroll" id="dictionaryList">
                                        
                                    </div>
<!--                                <div class="sec">
                                 	<table style="border: solid 1px #eee;">
                                		<tr>
                                			<th>Abbrevation(약어)</th>
                                			<th>English Full Name(영어명칭)</th>
                                			<th>Korean Full Name(한글명칭)</th>
                                			<th>Description(해설)</th>
                                		</tr>
                                		<tr>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                	</table>
                                </div> -->
                                
                                
<input type="file" name="dictionaryExcelfile" style="display: none;" onchange="dictionaryFileUp(this)" />
</form>
	<div class="dialog pop_deleteDictionConfirm" title="DELETE CONFIRM" style="max-width:1000px;">
	    <div class="pop_box">
	    	<div style="font-weight:bold;font-size:15px;margin:15px;" id="deleteDictionConfirmMessage">
	        </div>
	            <div class="right" style="margin:15px; float: right;">
	            	<input type="hidden" id="chekDelYN">
	                <button class="button btn_blue" onclick="deleteDictionConfirm()">Delete</button>
	                <button class="button btn_purple" onclick="deleteDictionCancel()">Cancel</button>
	            </div>
	    </div>
	</div>
	
<!-- 데이터 저장 확인팝업 -->
<div class="dialog pop_dictionarySaveConfirm" title="SAVE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
    		진행시 기존에 저장된 DICTIONARY 데이터는 삭제 됩니다. 그래도 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="dictionarySaveConfirm()">Save</button>
                <button class="button btn_purple" onclick="dictionarySaveCancel()">Cancel</button>
            </div>
    </div>
</div>
	
	
	                     