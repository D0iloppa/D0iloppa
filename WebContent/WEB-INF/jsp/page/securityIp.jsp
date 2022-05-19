<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script
	src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<h4>IP 보안</h4>
<div class="sec">
	<div class="tbl_wrap">
		<table class="write project_Left_title">
			<tr>
				<th>IP보안 사용 여부</th>
				<td><span><input type="radio" name="ipUse" value="Y"><label
						for="">사용함</label></span> <span><input type="radio" name="ipUse"
						value="N" checked><label for="">사용안함</label></span></td>
			</tr>
		</table>
		<div class="btn_group right">
			<button class="button btn_blue" onclick="ipUseYNSave()">적용</button>
		</div>
	</div>
</div>
<div class="sec">
	<div class="tbl_wrap">
		<table class="write project_Left_title">
			<colgroup>
				<col style="width: 150px">
				<col style="">
			</colgroup>
			<tr>
<!-- 				<th>생성 타입</th> -->
				<th><span class="so_required">*</span>기본인증 IP</th>
				<td>
<!-- 					<span style="margin-top:7px;">기본인증 IP</span> -->
					<input style="width: 157px;"type="text" id="ip_val" disabled>
					<button style="margin-top:3px;" class="button btn_blue" onclick="ipValSearch()">찾아보기</button>
				</td>
<!-- 				<td><select name="ip_type" id="ip_type"> -->
<!-- 						<option value="BETWEEN">대역폭IP</option> -->
<!-- 						<option value="SOLO">단독IP</option> -->
<!-- 				</select></td> -->
			</tr>
			<tr class="btwIpChk">
				<th><span class="so_required">*</span>시작IP</th>
				<td><input type="text" id="start_ip1" style="width: 30px;">
					- <input type="text" id="start_ip2" style="width: 30px;"> -
					<input type="text" id="start_ip3" style="width: 30px;"> - <input
					type="text" id="start_ip4" style="width: 30px;"></td>
			</tr>
			<tr class="btwIpChk">
				<th>끝IP</th>
				<td><input type="text" id="finish_ip1" style="width: 30px;">
					- <input type="text" id="finish_ip2" style="width: 30px;">
					- <input type="text" id="finish_ip3" style="width: 30px;">
					- <input type="text" id="finish_ip4" style="width: 30px;">
				</td>
			</tr>
			<tr class="oneIpChk" style="display: none;">
				<th><span class="so_required">*</span>단독IP</th>
				<td><input type="text" id="one_ip1" style="width: 30px;">
					- <input type="text" id="one_ip2" style="width: 30px;"> - <input
					type="text" id="one_ip3" style="width: 30px;"> - <input
					type="text" id="one_ip4" style="width: 30px;"></td>
			</tr>
		</table>
		<input type="hidden" id="ipSeq"> <input type="hidden"
			id="ipRange">
	</div>
	<div class="btn_group right">
		<button class="button btn_blue" id="newIpCreation"
			onclick="newIpCreation()">New</button>
		<button class="button btn_blue" id="ipCreation"
			onclick="ipCreationSave()">Save</button>
		<button class="button btn_blue" id="ipEdit" style="display: none;"
			onclick="ipCreationEdit()">Update</button>
		<button class="button btn_blue" id="ipDelete" onclick="ipDelete()">Delete</button>
	</div>
</div>
<div class="sec">
	<h4>Ip List</h4>
	<div class="tbl_wrap scroll" id="ipList"></div>
</div>




<!-- 조직도 다이얼로그 -->
<div id="ipOrg_pop" style="display: none">
	<div style="margin-right: 10px;">

		<div class="as_tab_cont" style="display: flex;">
			<div class="as_box as01 on" id="asOrg">
				<div class="as_left">
					<div class="btn_wrap">
						<button class="button btn_blue" style="display: none;"
							onClick="ipAdd()">추가</button>
					</div>
					<div class="pg_tree">
						<nav id="ipOrgnz" style="max-height: 367px;"></nav>
					</div>

				</div>
			</div>
			<div class="memberList">
				<div class="sec">
					<div style="width: 350px">
						<table class="write">
							<colgroup>
								<col style="width: 45%">
								<col style="">
							</colgroup>
							<tbody>
								<tr>
									<th>
										<div>
											<input type="radio" name="ip_searchUser" value="user_id"
												checked="checked"> <label for="">User ID</label> <input
												type="radio" name="ip_searchUser" value="user_kor_nm"
												class="ml10"> <label for="">Kor. Name</label>
										</div>
									</th>
									<td>
										<div class="line" style="margin-left: 15px">
											<input type="text" id="ipMemberKeyword">
											<button class="button btn_search ml5"
												onclick="ipMemberSearch()">Search</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="tbl_wrap" id="ip_member_grid"
					style="margin-top: 10px; width: 350px !important;"></div>
			</div>
		</div>
		<div class="btn_wrap" style="margin-top: 10px">
			<button class="button btn_blue" style="float: right;"
				onClick="ipAddtoGrid()">추가</button>
		</div>
	</div>
</div>


<!-- 문서삭제 확인팝업 -->
<div class="dialog pop_securityIpDeleteConfirm" title="DELETE CONFIRM"
	style="max-width: 1000px;">
	<div class="pop_box">
		<div style="font-weight: bold; font-size: 15px; margin: 15px;">
			Would you like delete to delete it?</div>
		<div class="right" style="margin: 15px; float: right;">
			<button class="button btn_blue" onclick="securityIpDeleteConfirm()">Delete</button>
			<button class="button btn_purple" onclick="securityIpDeleteCancel()">Cancel</button>
		</div>
	</div>
</div>