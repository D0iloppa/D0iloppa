<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

    <h4>완료</h4> 
    
    <div class="tbl_wrap">
	    <table class="write">
	    	<tbody>
	    		<tr>
	    			<th>기간</th>
	    			<td>
		    			<input type="date" id="olddayComp"> ~ <input type="date" id="todayComp">
		    			<input type="radio" name="date_chk_comp" id="search_allComp" class="ml5" value="ALL" checked> <label class="" for="">전체</label>&nbsp;
						<input type="radio" name="date_chk_comp" id="search_weekComp" class="ml5" value="Week"> <label class="" for="">한주</label>&nbsp;
						<input type="radio" name="date_chk_comp" id="search_monthComp" class="ml5" value="Month"> <label class="" for="">한달</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button class="button btn_blue" onclick="selectDayComp()">Retrieve</button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	</div>
	
	<div class="tbl_wrap scroll" id="collaboCompList">
		
    </div>