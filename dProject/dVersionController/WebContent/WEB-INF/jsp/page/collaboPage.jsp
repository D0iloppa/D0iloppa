<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<script src="${pageContext.request.contextPath}/resource/js/collabo.js"></script>
			<style>
			/* 로딩*/
			#collabo_loading {
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


    <h4 id="collabo_Title">${text}</h4> 
    
    <div class="tbl_wrap" style="overflow-x:visible !important;">
	    <table class="write">
	    	<tbody>
	    		<tr>
	    			<th style="text-align: center;">기간</th>
	    			<td style="display:flex">
		    			<input type="date" id="collaboFrom"> ~ <input type="date" id="collaboTo">
		    			<input type="radio" name="date_chk_w" id="search_allW" class="ml5" value="ALL" checked> <label class="" for="">전체</label>&nbsp;
						<input type="radio" name="date_chk_w" id="search_weekW" class="ml5" value="Week"> <label class="" for="">한주</label>&nbsp;
						<input type="radio" name="date_chk_w" id="search_monthW" class="ml5" value="Month"> <label class="" for="">한달</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button id="collaboDrnBtn" class="button btn_blue" onclick="drnClick()">DRN 생성</button>
						<div id="collabo_filterPrjDiv" style="margin-left:10px; margin-right:10px; line-height:21px; display:none;">
						  Project select
						  
<!-- 						  <select id="collabo_filterPrj" onchange="collabo_prjFilter()"> -->
<!-- 						  	  <option value="all">All Project</option> -->
<!-- 						  </select> -->

						<div class="multi_sel_wrap" style="display: inline-block; margin: 0 auto;" id="collabo_select_div">
							<input type="hidden" id="collabo_selectPrjId" value="all">
							<button style="margin-left : 5px;" class="multi_sel" id="collabo_selected_prj">All Project</button>
							<div class="multi_box" id="collabo_header_multi_box">
								<div class="prj_search" style="padding: 5px;">
									<div class="left">
										<button class="button btn_blue" onclick="collabo_prjAllSelect()">All Project</button>
										<label style="color: #000;" for="">Project Search</label>
										<input id="collabo_search" type="text">
										<button class="button btn_search"  id="prj_search_collabo" onclick="prjSearchCollabo()">검색</button>
									</div>
									<div class="tbl_wrap scroll" style="margin-top:5px;" id="prj_list_grid_collabo"></div>
								</div>

							</div>
						</div>

					</div>
                        <button class="button btn_blue" onclick="getDrnList()">Retrieve</button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	</div>
	
	<div class="tbl_wrap scroll" id="collaboList">
		
    </div>
    
    
    
    
    
    
    