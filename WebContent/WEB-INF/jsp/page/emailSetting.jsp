<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

<h4>DAILY EMAILING SETTING</h4>
<div class="sec">
	<!-- <div class="tbl_wrap">
		<table class="write">
			<colgroup>
			<col style="width:150px">
				<col style="*">
			</colgroup>
			<tr>
				<th><span class="so_required">*</span>EMAIL SETTING 설정</th>
				<input type="hidden" id="EMAILSETLIST">
				<input type="hidden" id="IDEMAILSETLIST">
				<td>
					<button class="button btn_small btn_blue" id="emailSet">EMAIL SETTING 설정</button>
					<ul id="EMAILNAMELIST" style="display: inline-block;"></ul>
					<input type="hidden" id="EMAILLIST">
				</td>
			</tr>
		</table>
	</div> -->
        <div class="tbl_wrap">
            <table class="write">
                <tr>
                    <th>Search Condition</th>
                    <td>
                        <div class="line">
                            <input type="radio" name="searchUserEmail" value="user_id">
                            <label for="">User Id</label>
                            
                            <input type="radio" name="searchUserEmail" value="user_kor_nm" class="ml10" checked>
                            <label for="">Kor. Name</label>
                        </div>
                        <div class="line">
                            <input type="text" id="usersEmailSearchSetKeyword">
                            <button class="button btn_search ml5" onclick="usersEmailSetSearch()">Search</button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        
         <h4>User List</h4>
         <!-- <div class="txt_right mb5">
             <button class="button btn_small btn_gray" onclick="addUserDCC()">추가</button>
         </div> -->
         <div class="tbl_wrap" id="userEmailList">
             
         </div>
        
         <h4>DCC Emailing List</h4>
         <input type="hidden" id="EMAILSETLIST">
		 <input type="hidden" id="IDEMAILSETLIST">
         <div class="gray_small_box" id="emailSetting_small_box">
             <ul class="user_name" id="DCCSETEMAILLIST">
             </ul>
             <input type="hidden" id="EMAILLIST">
         </div>
         
<div class="btn_group right">
    <button class="button btn_blue" id="emailSettingSave" onclick="emailSettingSave()">Save</button>
</div>
</div>

<!-- <div class="dialog pop_emailSet" title="EMAIL User Assign" style="max-width:1000px;">
    <div class="pop_box">
        <div class="tbl_wrap">
            <table class="write">
                <tr>
                    <th>Search Condition</th>
                    <td>
                        <div class="line">
                            <input type="radio" name="searchUserEmail" value="user_id">
                            <label for="">User ID</label>
                            
                            <input type="radio" name="searchUserEmail" value="user_kor_nm" class="ml10" checked>
                            <label for="">Kor. Name</label>
                        </div>
                        <div class="line">
                            <input type="text" id="usersEmailSearchSetKeyword">
                            <button class="button btn_search ml5" onclick="usersEmailSetSearch()">Search</button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
        
         <h4>User List</h4>
         <div class="txt_right mb5">
             <button class="button btn_small btn_gray" onclick="addUserDCC()">추가</button>
         </div>
         <div class="tbl_wrap" id="userEmailList">
             
         </div>
        
         <h4>DCC Email List</h4>
         <div class="gray_small_box" id="emailSetting_small_box">
             <ul class="user_name" id="DCCEMAILLIST">
             </ul>
         </div>
         <div class="txt_right mt5">
             <button class="button btn_small btn_gray" onclick="EMAILPopupClose()">확인</button>
         </div>
    </div>
</div> -->