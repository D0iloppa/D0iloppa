<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

    <h4>진행</h4> 
    
    <div class="tbl_wrap">
	    <table class="write">
	    	<tbody>
	    		<tr>
	    			<th>기간</th>
	    			<td>
		    			<input type="date" id="olddayProg"> ~ <input type="date" id="todayProg">
		    			<input type="radio" name="date_chk_prog" id="search_allProg" class="ml5" value="ALL" checked> <label class="" for="">전체</label>&nbsp;
						<input type="radio" name="date_chk_prog" id="search_weekProg" class="ml5" value="Week"> <label class="" for="">한주</label>&nbsp;
						<input type="radio" name="date_chk_prog" id="search_monthProg" class="ml5" value="Month"> <label class="" for="">한달</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button class="button btn_blue" onclick="selectDayProg()">Retrieve</button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	</div>
	
	<div class="tbl_wrap scroll" id="collaboProgList">
		
    </div>
    
    <div class="dialog pop_collaboPro" title="진행함 DRN 상세페이지" style="max-width:1200px;">
    	<div class="pop_box">
    		<div class="sec">
    			<div class="pg_top">
    				<div class="right">
	    				<button class="button btn_blue" onclick="">Print</button>
	    			</div>
    			</div>
    			<div class="tbl_wrap">
	    			<table class="write">
	    				<col style="width:100px">
	                    <col style="*">
	                    <tr>
	                    	<th>DRN 제목</th>
		                    <td><input type="text" id="DRN_title_prog" style="width: 100%;" readonly></td>
		                    <th>DRN NO</th>
		                    <td><input type="text" id="DRN_no_prog" style="width: 100%;" readonly></td>
	                    </tr>
	                    
	    			</table>
    			</div>
    			<div class="tbl_wrap">
	    			<table class="write">
	                    <colgroup>
	                    	<col style="width:100px">
	                        <col style="*">
	                	</colgroup>
	                	<tr>
	                		<th>PROJECT NO</th>
		                    <td><input type="text" id="Project_no_prog" style="width: 100%;" readonly></td>
		                    <th>PROJECT NAME</th>
		                    <td><input type="text" id="Project_name_prog" style="width: 100%;" readonly></td>
		                    <th>DATE</th>
		                    <td><input type="text" id="Date_prog" style="width: 100%;" readonly></td>
	                	</tr>
	                	<tr>
	                		<th>DOC NO(REV NO)</th>
		                    <td colspan="3"><input type="text" id="Doc_no_prog" style="width: 100%;" readonly></td>
		                    <th>STATUS</th>
		                    <td><input type="text" id="Status_prog" style="width: 100%;" readonly></td>
	                	</tr>
	                	<tr>
	                		<th>DOC TITLE</th>
	                		<td colspan="5"><input type="text" id="Doc_title_prog" style="width: 100%;" readonly></td>
	                	</tr>
	                	<tr>
	                		<th>첨부문서</th>
	                		<td colspan="4">
	                			<table class="write">
	                				<tr>
	                					<th style="width: 35%;">파일명(Doc No)</th>
	                					<th style="width: 15%;">REVISION</th>
	                					<th style="width: 35%;">문서명</th>
	                					<th style="width: 15%;">SIZE</th>
	                				</tr>
	                				<tr>
	                					<td>KE-M-GC-001</td>
	                					<td>01</td>
	                					<td>P&ID Diagram of Steam & Water System(1/2)</td>
	                					<td>1.51MB</td>
	                				</tr>
	                			</table>
	                		</td>
	                		<td>
	                			<button class="button btn_blue" style="width: 50%;">시스템</button>
	                			<button class="button btn_blue" style="width: 50%;">PC</button>
	                			<button class="button btn_orange" style="width: 50%;">삭제</button>
	                		</td>
	                	</tr>
	                </table>
    			</div>
    			
    			<div class="tbl_wrap">
	    			<table class="write">
	    				<colgroup>
	                    	<col style="width:100px">
	                        <col style="*">
	                	</colgroup>
	                	<tr>
	                		<th colspan="2">CHECK SHEET TITLE</th>
	                		<td><input type="text" style="width:100%;" placeholder="CHECK SHEET | "></td>
	                		<th>ITEM갯수</th>
	                		<td>
	                			<button class="button btn_blue">추가</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td colspan="3" style="border:none;"><button class="button btn_blue">CHECK SHEET 가져오기</button></td>
	                		<td colspan="2" style="border:none;">
	                			<div>
	                				체크결과 일괄적용 :
	                				<select>
	                					<option>선택</option>
	                					<option>O</option>
	                					<option>X</option>
	                					<option>△</option>
	                					<option>N/A</option>
	                				</select>
	                				<button class="button btn_blue">적용</button>
	                			</div>
	                		</td>
	                	</tr>
	    			</table>
	    		</div>
	    		
	    		<div class="tbl_wrap">
	    			<table class="write">
	    				<colgroup>
	                    	<col style="width:100px">
	                        <col style="*">
	                	</colgroup>
	                	<tr>
	                		<th>No.</th>
	                		<th style="width:25%;">ITEMS</th>
	                		<th style="width:60%;">CHECK POINT</th>
	                		<th style="width:12%;">DISCIPLINE</th>
	                		<th style="width:10%;">DESIGN</th>
	                		<th>REVIEWED</th>
	                		<th>APPROVED</th>
	                		<th style="width:25%;"></th>
	                	</tr>
	                	<tr>
	                		<td rowspan="7">1</td>
	                		<td rowspan="7">
	                			<input type="text" style="width: 100%;">
	                			<div>
	                				문항
	                				<select style="width: 50%;">
	                					<option>1</option>
	                					<option>2</option>
	                					<option>3</option>
	                					<option>4</option>
	                					<option>5</option>
	                					<option>6</option>
	                					<option>7</option>
	                				</select>
	                			</div>
	                		</td>
	                		<td>1) Standard Form을 사용 하였는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>2) Doc No. Rev. No. Title Status, 제출일 등이 표주에 따라 작성 되었는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>3) Index Page는 작성 되었는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>4) 작성자, 검토자, 승인자 확인은 되었는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>5) 수정(변경) 사항은 Cloud mark 및 별도의 표시가 되었는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>6) Revision Log의 개정 History Revised Page, Description이 자세하게 작성 되어 있는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>7) 지난 성공/실패 사례집이 적용 되었는가?</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td rowspan="2">2</td>
	                		<td rowspan="2">
	                			<input type="text" style="width: 100%;">
	                			<div>
	                				문항
	                				<select style="width: 50%;">
	                					<option>1</option>
	                					<option>2</option>
	                				</select>
	                			</div>
	                		</td>
	                		<td>1)</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>2)</td>
	                		<td>BB</td>
	                		<td></td>
	                		<td></td>
	                		<td></td>
	                		<td>
	                			<button class="button btn_blue">변경</button>
	                			<button class="button btn_orange">삭제</button>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td rowspan="5" colspan="2">Review Conclusion</td>
	                		<td rowspan="5" style="border-right: solid 1px #e1e1e1;">개별 도서상 Rev. 표시 및 관리 되어 있음</td>
	                	</tr>
	                	<tr>
	                		<td colspan="2">Design</td>
	                		<td colspan="2">Reviewed</td>
	                		<td>Approved</td>
	                	</tr>
	                	<tr>
	                		<td colspan="2">홍길동 대리</td>
	                		<td colspan="2">이영이 과장</td>
	                		<td>김철수 부장</td>
	                	</tr>
	                	<tr>
	                		<td colspan="2" style="height: 50px"> </td>
	                		<td colspan="2"> </td>
	                		<td> </td>
	                	</tr>
	                	<tr>
	                		<td colspan="2" style="height: 20px"> </td>
	                		<td colspan="2"> </td>
	                		<td> </td>
	                	</tr>
	                </table>
	                <p> ※ ○ : Acceptable, X : Not Acceptable, △ : Conditionally acceptable as commented , N/A : Not Applicable</p>
	            </div>
    		</div>
    	</div>
    </div>  