<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>
<form id="searchEmailingStatusExcelUploadForm" name="searchEmailingStatusExcelUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
<h4>E-MAILING STATUS</h4>
<div class="pg_top">
	<div class="right">
		<button class="button btn_default" onclick="searchEmailingStatus()">Retrieve</button>
		<button class="button btn_purple" id="searchEmailingStatus_toExcel" onclick="searchEmailingStatus_toExcelBtn()" style="display: none;">To Excel</button>
	</div>
</div>

<input type="hidden" name="fileName" id="searchEmailingStatus_fileName">
<input type="hidden" name="from" id="searchEmailingStatus_form_date">
<input type="hidden" name="to" id="searchEmailingStatus_to_date">
<input type="hidden" name="prj_nm" id="searchEmailingStatus_prj_nm">
<input type="hidden" name="prj_id" id="searchEmailingStatus_prj_id">
<input type="hidden" name="jsonRowDatas" id="searchEmailingStatus_jsonRowDatas">
</form>
<div class="sec">
	 <div class="tbl_wrap" style="overflow-x: unset">
            <table class="write">
                <tr>
                    <th>TYPE</th>
                    <td>
                    	<select id="eMailingStatus_selectType" style="width: 100px">
                        	<option value="sendCurrent">발송현황</option>
<!--                             <option value="receiveCurrent">수신현황</option> -->
<!--                             <option value="nReceiveCurrent">미수신현황</option> -->
                            <option value="downCurrent">다운로드현황</option>
                        </select>
                    </td>
                </tr>
                <tr>
                	<th>DURATION</th>
                	<td>
                		<input type="text" id="select_eMailingStatus_fdate" class="date wsmallIn"> ~
                        <input type="text" id="select_eMailingStatus_ldate" class="date wsmallIn">
                        <input type="radio" name="eMailingStatusRadio" value="all"><label>ALL</label>
                        <input type="radio" name="eMailingStatusRadio" value="oneWeek"><label>ONE WEEK</label>
                        <input type="radio" name="eMailingStatusRadio" value="oneMonth"><label>ONE MONTH</label>
                    </td>
                </tr>
				<tr>
                	<th>PROJECT</th>
                    <td>
                    	<div class="multi_sel_wrap">
							<input type="hidden" id="selectEmailingStatusPrjId" value="select">
							<button class="multi_sel" id="selected_prj_emailingStatus">SELECT</button>
							<div class="multi_box" id="emailingStatus_multi_box">
								<div class="prj_select_grid">
									<div class="prj_search" style="padding: 10px;">
										<div class="left">
											<button class="button btn_blue" onclick="emailingStatus_prjAllSelect()">All Project</button>
											<label style="color: #000;" for="">Project Search</label>
											<input id="prj_search_emailingStatus" type="text">
											<button class="button btn_search" onclick="prjSearchEmailingStatus()">검색</button>
										</div>
									</div>
									<div id="prj_list_grid_emailingStatus"></div>
								</div>
							</div>
						</div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="tbl_wrap table_grids" id="emailingSatusList"></div>
        <div class="tbl_wrap table_grids" id="emailingDownList" style="display: none"></div>
</div>


<!-- 해당 row의 내용 dialog -->
<div class="dialog" id="emailing_detailDialog" title="E-mail detail" style="max-width: 1000px;">
		<div class="pop_box">
			<div class="sec">
				<div class="tbl_wrap">
					<table class="write">
						<colgroup>
							<col style="width: 100px">
							<col style="">
						</colgroup>
						<tr>
							<th>Subject</th>
							<td colspan="5"><input type="text" id="emailing_pop_subject"
								style="width: 100%;" readonly></td>
						</tr>
						<tr>
							<th>Operator</th>
							<td><input type="text" id="emailing_pop_operator"
								style="width: 100%;" readonly></td>
							<th>Receiver</th>
							<td><input type="text" id="emailing_pop_receiver"
								style="width: 100%;"></td>
							<th>Mail send date</th>
							<td><input type="text" id="emailing_pop_sendDate"
								style="width: 100%;" readonly></td>
						</tr>
						<tr>
							<th>Contents</th>
							<td colspan="5" style="overflow-y:auto;">
								<div id="emailing_pop_contents" style="max-height:300px"></div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="line">
				<div class="right r_array">
					<button class="button btn_blue" onclick="closeDetailEmail()">닫기</button>
				</div>
			</div>
		</div>
	</div>

