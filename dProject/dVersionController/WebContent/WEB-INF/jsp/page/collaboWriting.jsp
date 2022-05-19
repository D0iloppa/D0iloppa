<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

    <h4>작성중</h4> 
    
    <div class="tbl_wrap">
	    <table class="write">
	    	<tbody>
	    		<tr>
	    			<th>기간</th>
	    			<td>
		    			<input type="date" id="olddayW"> ~ <input type="date" id="todayW">
		    			<input type="radio" name="date_chk_w" id="search_allW" class="ml5" value="ALL" checked> <label class="" for="">전체</label>&nbsp;
						<input type="radio" name="date_chk_w" id="search_weekW" class="ml5" value="Week"> <label class="" for="">한주</label>&nbsp;
						<input type="radio" name="date_chk_w" id="search_monthW" class="ml5" value="Month"> <label class="" for="">한달</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button class="button btn_blue" onclick="selectDayW()">Retrieve</button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	</div>
	
	<div class="tbl_wrap scroll" id="collaboWritingList">
		
    </div>
    
    
    
    
    
    
    