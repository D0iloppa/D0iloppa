<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/folderAuth.js"></script>
<div class="dialog pop_authSet" title="권한관리" style="max-width:500px;">
	<div class="pop_box">
		<div class="pg_top">
			<div class="left">
				<label for="">PATH</label>
				<input id="auth_folder_path"type="text" class="ml5 long" readonly="readonly">
			</div>
		</div>
		<div class="autoSet_tab" style="display: flex ; justify-content: space-between;">
<!-- 			<div style="margin-right: 10px;"> -->
<!-- 			<ul class="as_tab_tit"> -->
<!-- 				<li><a id="auth_orgTab" href="#asOrg" class="on">조직도</a></li> -->
<!-- 				<li><a id="auth_grTab" href="#asGroup">그룹</a></li> -->
<!-- 			</ul> -->
<!-- 			<div class="as_tab_cont"> -->
<!-- 				<div class="as_box as01 on" id="asOrg"> -->
<!-- 					<div class="as_left"> -->
<!-- 						<div class="btn_wrap"> -->
<!-- 							<button class="button btn_blue" style="display:none;"onClick="authAdd()">추가</button> -->
<!-- 						</div> -->
<!-- 						<div class="pg_tree"> -->
<!-- 							<nav id="authOrgnz" style="max-height: 367px;"></nav> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				<div class="as_box as02" id="asGroup"> -->
<!-- 					<div class="as_left"> -->
<!-- 						<div class="btn_wrap"> -->
<!-- 							<button class="button btn_blue" style="display:none;" onClick="authAdd()">추가</button> -->
<!-- 						</div> -->
<!-- 						<div class="pg_tree"> -->
<!-- 							<nav id="authGr" style=" max-height: 367px;"></nav> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				</div> -->
<!-- 				</div> -->
				
				
<!-- 				<div class="as_right" style="width: 70%;"> -->
					<div class="as_right">
<!-- 					<div class="pg_top mt10"> -->
<!-- 						<div class="left"> -->
<!-- 							<input type="checkbox" id="parent_org"> <label for="">부모 -->
<!-- 								폴더의 권한을 계승</label> -->
<!-- 						</div> -->
<!-- 					</div> -->
						<div class="pg_top">
							<div class="left">
								<input id="addAuthCheck1" type="checkbox" onclick="addAuth_chk(this)">
								<label for="">이 폴더 권한을 추가</label>
							</div>
							<div class="right">
								<button id="auth_orgAdd" class="button btn_purple" onClick="authAdd()">권한추가</button>
								<button id="auth_orgDel" class="button btn_purple" onClick="authDel()">삭제</button>
							</div>
						</div>
						<div class="tbl_wrap" id="auth_grid"></div>

				<div id="auth_info_div">
					<div class="pg_top mt10">
						<div class="left">
							<input type="checkbox" id="parent_org" onclick="auth_rootCheck()"> <label for="">부모
								폴더의 권한을 계승</label>
						</div>
					</div>
					<div class="pg_top">
						<h5>권한적용</h5>
						<div class="right">
							<button id="auth_orgApply" class="button btn_blue"
								onClick="authApply_allData()">적용</button>
<!-- 								onClick="authApply()">적용</button> -->
								
						</div>
					</div>
					<div class="tbl_wrap">
					<table class="write">
							<tr>
								<th></th>
								<td>
									<label for="">허용</label> 
 								<label style="margin-left: 7px;" for="">거부</label> 
								</td>
							</tr>
							<tr>
								<th>ALL</th>
								<td><input value="Y" type="checkbox" name="all_org"
									onclick="radio(this,'all_org');" onchange="setGridTmpAuth(this);" class="auth_CheckBox org_y">
 									<input value="N" type="checkbox" name="all_org" 
 									onclick="radio(this,'all_org');" onchange="setGridTmpAuth(this);"
 									class="ml10 auth_CheckBox org_n"> 
								</td>
							</tr>
							<tr>
								<th>READ</th>
								<td><input value="Y" type="checkbox" name="read_org"
									onclick="radio(this,'read_org');" onchange="setGridTmpAuth(this);" class="auth_CheckBox org_y">
 									<input value="N" type="checkbox" name="read_org" 
 									onclick="radio(this,'read_org');" onchange="setGridTmpAuth(this);"
 									class="ml10 auth_CheckBox org_n"> 
								</td>
							</tr>
							<tr>
								<th>WRITE</th>
								<td><input value="Y" type="checkbox" name="write_org"
									onclick="radio(this,'write_org');" onchange="setGridTmpAuth(this);" class="auth_CheckBox org_y">
 									<input value="N" type="checkbox" name="write_org" 
 									onclick="radio(this,'write_org');"  onchange="setGridTmpAuth(this);"
 									class="ml10 auth_CheckBox org_n"> 
								</td>
							</tr>
							<tr>
								<th>DELETE</th>
								<td><input value="Y" type="checkbox" name="delete_org"
									onclick="radio(this,'delete_org');" onchange="setGridTmpAuth(this);" class="auth_CheckBox org_y">
 									<input value="N" type="checkbox" name="delete_org" 
 									onclick="radio(this,'delete_org');" onchange="setGridTmpAuth(this);"
 									class="ml10 auth_CheckBox org_n">
								</td>
							</tr>
						</table>
<!-- 						<table class="write"> -->
<!-- 							<tr> -->
<!-- 								<th></th> -->
<!-- 								<td> -->
<!-- 									<label for="">ALL</label> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									<label for="">READ</label> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									<label for="">WRITE</label> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									<label for="">DELETE</label> -->
<!-- 								</td> -->
<!-- 								<td colspan="4"></td> -->
<!-- 							</tr> -->
<!-- 							<tr> -->
<!-- 								<th>허용</th> -->
<!-- 								<td> -->
<!-- 									<input value="Y" type="checkbox" name="all_org" onclick="radio(this,'all_org');" class="auth_CheckBox org_y"> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									<input value="Y" type="checkbox" name="read_org" onclick="radio(this,'read_org');" class="auth_CheckBox org_y"> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									<input value="Y" type="checkbox" name="write_org" onclick="radio(this,'write_org');" class="auth_CheckBox org_y"> -->
<!-- 								</td> -->
<!-- 								<td> -->
<!-- 									<input value="Y" type="checkbox" name="delete_org" onclick="radio(this,'delete_org');" class="auth_CheckBox org_y"> -->
<!-- 								</td> -->
<!-- 								<td colspan="4"></td> -->
<!-- 							</tr>							 -->
<!-- 						</table> -->
						
						<div style="margin-top:5px">
							<label>※ 폴더 권한을 적용해야 저장됩니다.</label>
						</div>	
						
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<!-- 다이얼로그 -->
<div id="auth_pop" class="autoSet_tab" style="display:none">
	<div style="margin-right: 10px;">
		<ul class="as_tab_tit">
			<li><a id="auth_orgTab" href="#asOrg" class="on">조직도</a></li>
			<li><a id="auth_grTab" href="#asGroup">그룹</a></li>
		</ul>
		<div class="as_tab_cont" style="display:flex;">
			<div class="as_box as01 on" id="asOrg">
				<div class="as_left">
					<div class="btn_wrap">
						<button class="button btn_blue" style="display: none;"
							onClick="authAdd()">추가</button>
					</div>
					<div class="pg_tree">
						<nav id="authOrgnz" style="max-height: 367px;"></nav>
					</div>

				</div>
			</div>
			<div class="as_box as02" id="asGroup">
				<div class="as_left">
					<div class="btn_wrap">
						<button class="button btn_blue" style="display: none;"
							onClick="authAdd()">추가</button>
					</div>
					<div class="pg_tree">
						<nav id="authGr" style="max-height: 367px;"></nav>
					</div>
				</div>
			</div>
			<div class="memberList">
				<div class="sec">
					<div style="width:350px">
						<table class="write">
							<colgroup>
								<col style="width: 45%">
								<col style="">
							</colgroup>
							<tbody>
								<tr>
									<th>
										<div>
											<input type="radio" name="searchUser" value="user_id" checked="checked"> <label for="">User ID</label>
											<input type="radio" name="searchUser" value="user_kor_nm" class="ml10"> <label for="">Kor. Name</label>
										</div>
									</th>
									<td>
									<div class="line" style="margin-left:15px">
										<input type="text" id="authMemberKeyword">
										<button class="button btn_search ml5" onclick="authMemberSearch()">Search</button>
									</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="tbl_wrap" id="member_grid" style="margin-top:10px;width:350px !important;"></div>
			</div>
			
		</div>
		<div class="btn_wrap" style="margin-top: 10px">
			<button class="button btn_blue" style="float:right;"
				onClick="authAddtoGrid()">추가</button>
		</div>
	</div>
</div>


<div class="dialog pop_deleteFolderAuthConfirm" title="Delete Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;">
			해당 폴더에 추가된 권한들이 삭제됩니다. 계속 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" id="deleteCodeYes" onclick="folderAuth_delYes()">Yes</button>
                <button class="button btn_blue" id="deleteCodeCancel" onclick="folderAuth_delCancle()">Cancel</button>
            </div>
    </div>
</div>