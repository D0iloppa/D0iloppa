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
                                
                                <h4>DICTIONARY SEARCH VIEW</h4>
                                
                                <div class="pg_top">
                                    <div class="right dicbtns"> <!-- dicbtns common에 따로 스타일 줌 -->
                                        <button class="button btn_search" onclick="searchdictionary_view()">Search</button>
                                    </div>
                                </div>
                                
                                <div class="tbl_wrap">
                                	<table class="write">
                                		<tbody>
                                			<tr>
                                        		<th>약어</th>
	                                			<td><input type="text" class="full" id="search_Abbr_view"></td>
	                                			<th>영어명칭</th>
	                                			<td><input type="text" class="full" id="search_Eng_view"></td>
	                                			<th>한글명칭</th>
	                                			<td><input type="text" class="full" id="search_Kor_view"></td>
	                                			<th>해설</th>
	                                			<td><input type="text" class="full" id="search_Desc_view"></td>
                                        	</tr>
                                		</tbody>
                                	</table>
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
                                                <td colspan="3"><input type="text" class="full" id="abbr_view" style="width: 100%;" readonly></td>
                                            </tr>
                                            <tr>
                                                <th>영어명칭</th>
                                                <td colspan="3"><input type="text" class="full" id="eng_view" style="width: 100%;" readonly></td>
                                            </tr>
                                            <tr>
                                                <th>한글명칭</th>
                                                <td colspan="3"><input type="text" class="full" id="kor_view" style="width: 100%;" readonly></td>
                                            </tr>
                                            <tr>
                                                <th>해설</th>
                                                <td colspan="3"><textarea rows="500" class="full" id="desc_view" cols="500" readonly></textarea></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <p class="result_line">
                                	<span>RESULT</span>
                                </p>
                                	<div class="tbl_wrap scroll" id="dictionaryList_view">
                                        
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