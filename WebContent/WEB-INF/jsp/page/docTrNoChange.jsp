<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="${pageContext.request.contextPath}/resource/js/${viewName}.js"></script>

<h4>DOC.NO & TR NO CHANGE</h4>
<div class="pg_top">
	<div class="right">
		<button class="button btn_default" onclick="searchNoChange()">Search</button>
	</div>
</div>
<div class="sec">
	 <div class="tbl_wrap" style="overflow-x: unset">
            <table class="write">
                <tr>
                    <th>CHANGE TYPE</th>
                    <td>
                    	<input type="radio" name="docno_trno" value="doc_no" checked><label for="" class="mr10">DOCUMENT NO</label>
                   		<input type="radio" name="docno_trno" value="tr_no"><label for="" class="mr10">TR NO</label>
                   		<input type="radio" name="docno_trno" value="vtr_no"><label for="" class="mr10">VTR NO</label>
                   		<input type="radio" name="docno_trno" value="str_no"><label for="">STR NO</label>
                    </td>
                </tr>
                <tr>
                	<th>DURATION</th>
                	<td>
                		<input type="text" id="noChangeFrom" class="date wsmallIn"> ~
                        <input type="text" id="noChangeTo" class="date wsmallIn">
                        <input type="checkbox" name="noChangeAll" value="all"><label>ALL</label>
                    </td>
                </tr>
				<tr>
                	<th>PROJECT</th>
                    <td>
                    	<div class="multi_sel_wrap">
							<input type="hidden" id="selectNoChangePrjId">
							<button class="multi_sel" id="selected_prj_noChange">SELECT</button>
							<div class="multi_box" id="noChange_multi_box">
								<div class="prj_select_grid">
									<div class="prj_search" style="padding: 10px;">
										<div class="left">
											<button class="button btn_blue" onclick="docTrNoChg_prjAllSelect()">All Project</button>
											<label style="color: #000;" for="">Project Search</label>
											<input id="prj_search_noChange" type="text">
											<button class="button btn_search" onclick="prjSearchNoChange()">검색</button>
										</div>
									</div>
									<div id="prj_list_grid_noChange"></div>
								</div>
							</div>
						</div>
                    </td>
                </tr>
            </table>
        </div>
	 	<div class="tbl_wrap" id="doc_no_tbl" style="overflow-x: unset">
            <table class="write">
                <tr>
                    <th>OLD DOCUMENT NO</th>
                    <td>
                    	<input type="text" id="old_doc_no" class="full">
                    </td>
                    <th>CHANGE DOCUMENT NO</th>
                    <td>
                    	<input type="text" id="change_doc_no" class="full">
                    </td>
                    <td>
						<button class="button btn_default" onclick="docno_change()">CHANGE</button>
                    </td>
                </tr>
            </table>
        </div>
	 	<div class="tbl_wrap" id="tr_no_tbl" style="overflow-x: unset;display:none;">
            <table class="write">
                <tr>
                    <th>OLD Transmittal NO</th>
                    <td>
                    	<input type="text" id="old_tr_no" class="full">
                    </td>
                    <th>CHANGE Transmittal NO</th>
                    <td>
                    	<input type="text" id="change_tr_no" class="full">
                    </td>
                    <td>
						<button class="button btn_default" onclick="trno_change()">CHANGE</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="tbl_wrap table_grids" id="noChgHisTable"></div>
</div>