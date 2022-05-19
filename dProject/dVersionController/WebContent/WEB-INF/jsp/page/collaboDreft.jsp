<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

    <h4>기안함</h4> 
    
    <div class="tbl_wrap">
	    <table class="write">
	    	<tbody>
	    		<tr>
	    			<th>기간</th>
	    			<td>
		    			<input type="date" id="oldday"> ~ <input type="date" id="today">
		    			<input type="radio" name="date_chk" id="search_all" class="ml5" value="ALL" checked> <label class="" for="">전체</label>&nbsp;
						<input type="radio" name="date_chk" id="search_week" class="ml5" value="Week"> <label class="" for="">한주</label>&nbsp;
						<input type="radio" name="date_chk" id="search_month" class="ml5" value="Month"> <label class="" for="">한달</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button class="button btn_blue" onclick="drnClick()">DRN 생성</button>
                        <button class="button btn_blue" onclick="selectDay()">Retrieve</button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	</div>
	
	<div class="tbl_wrap scroll" id="collaboDreftList">
		
    </div>