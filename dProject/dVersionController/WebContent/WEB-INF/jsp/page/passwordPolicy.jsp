<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<div class="txt_right">
	<button id="passwd_applyBtn" class="button btn_purple mb5" onclick="PswdApply()">Apply</button>
</div>
<h4>비밀번호 유효기간 사용 설정</h4>
<div class="sec">
	<div class="tbl_wrap">
		<table class="write">
			<tr>
				<th>사용여부</th>
				<td>
					<select name="" id="pswd_expire_day_use_yn" class="pr_gn" onchange="DayUseYn()">
						<option value="Y">사용함</option>
						<option value="N">사용안함</option>
					</select>
				</td>
				<th>유효기간</th>
				<td>
					<input type="text" class="" id="pswdchangDate"> 일
				</td>
			</tr>
			
		</table>
	</div>
</div>

<h4>비밀번호 불일치시 계정 정지 설정</h4>
<div class="sec">
	<div class="tbl_wrap">
		<table class="write">
			<tr>
				<th>사용여부</th>
				<td>
					<select name="" id="pswd_fial_stop_use_yn" class="pr_gn">
						<option value="Y">사용함</option>
						<option value="N">사용안함</option>					
					</select>
				</td>
				<th>유효회수</th>
				<td><input type="text" class="" id="pswdchangCnt"> 회</td>
			</tr>
			
		</table>
	</div>
</div>


