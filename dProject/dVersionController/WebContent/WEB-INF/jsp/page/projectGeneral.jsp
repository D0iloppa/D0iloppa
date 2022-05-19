<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resource/js/projectGeneral.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/projectGeneral05.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/projectGeneral06.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/projectGeneral09.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/projectGeneral10.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery.flexdatalist.min.js"></script>
<div class="proj_general" id="general01">
	<ul>
		<li><a href="#pg01" class="on projectGen_Link" id="projectGeneral01ALink">폴더정비</a></li>
		<li><a href="#pg02" class="projectGen_Link" id="projectGeneral02ALink">프로세스 TYPE 정의 폴더
				MATCH DRN 사용여부 설정</a></li>
		<li><a href="#pg03" class="projectGen_Link" id="projectGeneral03ALink">NUMBERING 설정</a></li>
		<li><a href="#pg04" class="projectGen_Link" id="projectGeneral04ALink">DOCUMENT, TRANSMITTAL ISSUE, RETURN 필드값
				정의</a></li>
		<li><a href="#pg05" class="projectGen_Link" id="projectGeneral05ALink">LETTER, E-MAIL 코드값 정의</a></li>
		<li><a href="#pg06" class="projectGen_Link" id="projectGeneral06ALink">STEP 프로세스 정의</a></li>
		<li><a href="#pg07" class="projectGen_Link" id="projectGeneral07ALink">DISCIPLINE 설정, 폴더 MATCH</a></li>
		<li><a href="#pg08" class="projectGen_Link" id="projectGeneral08ALink">PROJET USER/GROUP 설정</a></li>
		<li><a href="#pg09" class="projectGen_Link" id="projectGeneral09ALink">폴더별 권한 설정</a></li>
		<li><a href="#pg10" class="projectGen_Link" id="projectGeneral10ALink">발송메일 템플릿 설정</a></li>
		<li><a href="#pg11" class="projectGen_Link" id="projectGeneral11ALink">Transmittal Cover Template 설정</a></li>
		<!-- <li><a href="#pg01" class="on projectGen_Link">폴더정비</a></li>
		<li><a href="#pg02" class="projectGen_Link" onclick="pgen_step02();">프로세스 TYPE 정의 폴더
				MATCH DRN 사용여부 설정</a></li>
		<li><a href="#pg03" class="projectGen_Link" onclick="pgen_step03();">NUMBERING 설정</a></li>
		<li><a href="#pg04" class="projectGen_Link" onclick="pgen_step04();">DOCUMENT, TRANSMITTAL ISSUE, RETURN 필드값
				정의</a></li>
		<li><a href="#pg05" class="projectGen_Link" onclick="pgen_step05();">LETTER, E-MAIL 코드값 정의</a></li>
		<li><a href="#pg06" class="projectGen_Link">STEP 프로세스 정의</a></li>
		<li><a href="#pg07" class="projectGen_Link" onclick="pgen_step07();">DISCIPLINE 설정, 폴더 MATCH</a></li>
		<li><a href="#pg08" class="projectGen_Link">PROJET USER/GROUP 설정</a></li>
		<li><a href="#pg09" class="projectGen_Link">폴더별 권한 설정</a></li>
		<li><a href="#pg10" class="projectGen_Link">발송메일 템플릿 설정</a></li>
		<li><a href="#pg11" class="projectGen_Link">Transmittal Cover Template 설정</a></li> -->
	</ul>
</div>

<div class="pg_cont">
	<div id="pg01" class="pg on">
		<h4>폴더 정비</h4>
		<div style="display:flex;">
		<div class="pg_tree" id="pg01_tree" style="border:none;">
			<nav id="pop_jstree_demo_div">
				<ul>
					<li data-jstree='{"icon":"images/pinwheel.png"}'> 
						Loading
					</li>

				</ul>
			</nav>
		</div>
		<div>
			<button id="folderRefresh" class="button btn_blue" onclick="folderRefresh()">Refresh</button>
		</div>
		</div>
	</div>
	<div id="pg02" class="pg">
		<h4>프로세스 TYPE 정의, 폴더 MATCH, DRN 사용 여부 설정</h4>
		<div class="txt_right">
			<button id="pg02_applyBtn" class="button btn_purple mb5">프로젝트 정보 수정</button>
		</div>
		<div class="sec">
			<div class="tbl_wrap">
				<table class="write">
					<colgroup>
						<col style="width: 150px">
						<col style="">
					</colgroup>
					<tr>
						<th>Project Code</th>
						<td><input style="width:50%;min-width: 200px;" type="text" id="pg02_pjCode"></td>
					</tr>
					<tr>
						<th>Project Name</th>
						<td><input style="width:50%;min-width: 200px;" type="text" id="pg02_pjNm"></td>
					</tr>
					<tr>
						<th>Project Full Name</th>
						<td><input style="width:50%;min-width: 200px;" type="text" id="pg02_pjFullNm"></td>
					</tr>
				</table>
			</div>
		

			<div class="tbl_wrap scroll">
				<table class="write">
					<tr>
						<th style="width:6%;text-align: center;">CLASS</th>
						<th style="width:15%;text-align: center;">PROCESS TYPE</th>
						<th style="width:40%;text-align: center;">DESCRIPTION</th>
						<th style="width:7%;text-align: center;">DRN</th>
<!-- 						<th>DOC TYPE</th> -->
						<th style="width:34%;text-align: center;">PATH</th>
					</tr>
					<tr>
						<td><input type="text" class="full" id="pg02_setClass"disabled></td>
						<td>
<!-- 						<input type="text" class="full" id="pg02_setPType"> -->
							<select class="full" id="pg02_setPType" style="text-align:center; width: 100%;" >
								<option value=""></option>
							</select>
						</td>
						<td><input type="text" class="full" id="pg02_setDescript"></td>
						<td style="text-align:center;"><select name="" class="full" id="pg02_setDRN" style="width: 100%;">
								<option value="Y">적용</option>
								<option value="N">미적용</option>
						</select></td>
<!-- 						<td><select name="" class="full" id="pg02_setDocType"> -->
<!-- 								<option value=""></option> -->
<!-- 								<option value="General">General</option> -->
<!-- 								<option value="DCI">DCI</option> -->
<!-- 								<option value="VDCI">VDCI</option> -->
<!-- 								<option value="PCI">PCI</option> -->
<!-- 								<option value="BMC">BMC</option> -->
<!-- 								<option value="SDCI">SDCI</option> -->
<!-- 								<option value="Corr">Corr</option> -->
<!-- 						</select></td> -->
						<td><input type="hidden" id="pg02_pathId" value=""><input
							type="text" style="width: calc(100% - 80px);" id="pg02_setPath"
							disabled>
							<button id="pg02_btnPath" class="button btn_gray ml5" style="margin-top: 4px;">Path</button></td>
					</tr>
				</table>
				<div class="btn_group right">
				<button id="pg02_addBtn" class="button btn_blue">추가</button>
				<button id="pg02_saveBtn" class="button btn_blue">저장</button>
				<button id="pg02_delBtn" class="button btn_purple">삭제</button>
			</div>
				
			</div>
			<div class="tbl_wrap scroll" id="pg02_Grid"></div>
			<div id="pg02_DrnReviewCon">
<!-- 				<h4>DRN REVIEW CONCLUSION</h4> -->
				<div class="pg_top">
<!-- 					<div class="line mb20"> -->
<!-- 						<div class="left"> -->
<!-- 							<select name="" id="drnReviewcodeType" class="pr_gn" -->
<!-- 								style="display: none;"> -->
<!-- 								<option value="#sel_DrnReivew">REVIEW CONCLUSION</option> -->
<!-- 							</select> <input type="checkbox" name="copyCheck" id="copyCheck"> -->
<!-- 							<label for="" class="mr10">이전공사 COPY</label> <select name="" -->
<!-- 								id="copyProject"> -->
<%-- 								<c:forEach var="i" items="${getPrjList}" varStatus="vs"> --%>
<%-- 									<option value="${i.prj_id}">${i.prj_nm}</option> --%>
<%-- 								</c:forEach> --%>
<!-- 							</select> -->
<!-- 							<button class="button btn_default" id="copyBtn" -->
<!-- 								style="margin-left: 10px; display: none;" onclick="Copy()">Copy</button> -->
<!-- 							<input type="hidden" id="selectPrjId"> -->
<!-- 						</div> -->
<!-- 					</div> -->
				</div>

				<div class="sec">
					<div class="tbl_wrap">
						<table class="write">
							<colgroup>
								<col style="width: 150px">
								<col style="">
							</colgroup>
							<tr>
								<th><span class="so_required">*</span>Review Conclusion</th>
								<td style="display:flex;">
									<textarea id="review_conclusion"></textarea>
									<button class="button btn_default" style="margin-left: 10px;" onclick="pg02_rvc_apply()">Apply</button>
								</td>
							</tr>
						</table>
					</div>
					
					<div class="right">
						
					</div>
				</div>
				
			</div>
		</div>
	</div>

	<!-- ProjectGeneral step2 select foler 다이얼로그 -->

	<div id="pg02_dialog" class="dialog pop_doc_search"
		title="Select folder">
		<div class="pop_box">
			<nav id="pg02_tree"></nav>
		</div>
		<div class="btn_group right">
			<button id="pg02_folderSelect" class="button btn_blue">Select</button>
		</div>
	</div>






	<div id="pg03" class="pg">
		<h4>PROJECT NUMBERING 설정</h4>
		<div class="pg_top">
			<div class="left" style="display: flex;">
				<div style="margin: auto;">타 프로젝트 설정 복사하기&nbsp;</div>				
				<input type="checkbox" id="pg03_copyChk" class="ml5" style="margin: auto;"> <label
					class="copyChkdiv" for="">COPY</label>&nbsp;&nbsp;&nbsp;
				<div class="copyDiv" style="display: none;">
					<div class="multi_sel_wrap" id="pg03_select_div">
						<input type="hidden" id="pg03_selectPrjId">
						<button class="multi_sel" id="pg03_selected_prj">SELECT</button>
						<div class="multi_box" id="pg03_header_multi_box">
							<div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prjGen_search"
										type="text">
									<button class="button btn_search" onclick="prjGenSearch()">검색</button>
								</div>
								<div id="prj_list_grid_pg03"></div>
							</div>
							<%-- <table class="header_prj_select_table">
								<tr>
									<th>Project No</th>
									<th>Project Name</th>
									<th>Project Full Name</th>
								</tr>
								<c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
									<tr class="pg03_prj_select" id="pg03_prj_id_is_${i.prj_id}">
										<td>${i.prj_no}</td>
										<td class="prj_nm">${i.prj_nm}</td>
										<td>${i.prj_full_nm}</td>
									</tr>
								</c:forEach>
							</table> --%>
						</div>
					</div>
					<button class="button btn_blue ml5 pg03_Btn">COPY</button>
				</div>
			</div>
			<div class="right">
				<button class="button btn_purple pg03_Btn">Refresh</button>
			</div>
		</div>
		

		
		<h4>E-MAIL & LETTER Code 정의</h4>
		
		<div class="sec" style="width: 100%; min-width: 500px">
			<div class="tbl_wrap">
				<table class="write">
					<tr>
						<th style="width: 270px;"><span class="so_required">*</span>E-MAIL
							& LETTER SERIAL No 시작 설정</th>
						<td style="width: 100%;">
							<div style="display: flex;">
								<input type="text" id="pg03_SERIAL">
								<div style="margin-left: 10px; float: right">
									<button class="button btn_default"
										style="margin-top: auto; margin-bottom: auto"
										onclick="pg03_serial_apply()">Apply</button>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="num_set">
			<div class="tables" id="corrNumberTables">
				<div class="tbl4">
					<div class="txt_right">
						<button class="button btn_blue pg03_mngBtn">관리</button>
					</div>
					<div class="tbl_wrap mt5">
						<div class="grid_header">
							<table class="list">
								<tr>
									<th class="pg03_th" onclick="pg03HeadClick(0)" colspan="2">E-MAIL & LETTER TYPE</th>
								</tr>
							</table>
						</div>
						<div class="pg03_Grids"></div>
					</div>
				</div>
				<div class="tbl4">
					<div class="txt_right">
						<button class="button btn_blue pg03_mngBtn">관리</button>
					</div>
					<div class="tbl_wrap mt5">
						<div class="grid_header">
							<table class="list">
								<tr>
									<th class="pg03_th" onclick="pg03HeadClick(1)" colspan="2">E-MAIL & LETTER FROM/TO</th>
								</tr>
							</table>
						</div>
						<div class="pg03_Grids"></div>
					</div>
				</div>
				
				<div style="width:100%;">
					<h4>TRANSMITTAL Numbering 체계 정의</h4>
				</div>
				
				<div class="tbl4">
					<div class="txt_right">
						<button class="button btn_blue pg03_mngBtn">관리</button>
					</div>
					<div class="tbl_wrap mt5">
						<div class="grid_header">
							<table class="list">
								<tr>
									<th class="pg03_th" onclick="pg03HeadClick(2)" colspan="2">OUTGOING
										NUMBERING</th>
								</tr>
							</table>
						</div>
						<div class="pg03_Grids"></div>
					</div>
				</div>
				<div class="tbl4">
					<div class="txt_right">
						<button class="button btn_blue pg03_mngBtn">관리</button>
					</div>
					<div class="tbl_wrap mt5">
						<div class="grid_header">
							<table class="list">
								<tr>
									<th class="pg03_th" onclick="pg03HeadClick(3)" colspan="2">INCOMING
										NUMBERING</th>
								</tr>
							</table>
						</div>
						<div class="pg03_Grids"></div>
					</div>
				</div>
			</div>
		</div>

	<div id="pg03_dialog0" class="dialog pop_doc_search pg03_dialog"
			title="CORRESPONDENCE TYPE 관리" style="min-width: 800px;">
			<div class="pop_box">
				<h4>TYPE 목록</h4>
				<div class="tbl_wrap pg03_dialog_Grids"></div>
				<div class="txt_right mb5">
					<button class="button btn_small btn_gray pg03_dialogBtn">저장</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">취소</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삽입</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삭제</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▲</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▼</button>
				</div>
			</div>
		</div>

		<div id="pg03_dialog1" class="dialog pop_doc_search pg03_dialog"
			title="CORRESPONDENCE FROM/TO 관리" style="min-width: 800px;">
			<div class="pop_box">
				<h4>TYPE 목록</h4>
				<div class="tbl_wrap pg03_dialog_Grids"></div>
				<div class="txt_right mb5">
					<button class="button btn_small btn_gray pg03_dialogBtn">저장</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">취소</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삽입</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삭제</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▲</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▼</button>
				</div>
			</div>
		</div>

		<div id="pg03_dialog2" class="dialog pop_doc_search pg03_dialog"
			title="OUTGOING NUMBERING 관리" style="min-width: 800px;">
			<div class="pop_box">
				<h4>TYPE 목록</h4>
				<div class="numberingCheck" style="float:right;color:red;">
					넘버링체계에는 숫자가 반드시 포함되어야 하며<br>
					하이픈(-) 기준으로 나누어 문자가 포함되지 않은<br>
					숫자만 있는 영역으로 발번합니다.<br>
					ex) PH55-CD001-HP-003 일 경우 003부터 발번<br>
				</div>
				<div class="tbl_wrap pg03_dialog_Grids"></div>
				<div class="txt_right mb5">
					<button class="button btn_small btn_gray pg03_dialogBtn">저장</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">취소</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삽입</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삭제</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▲</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▼</button>
				</div>
			</div>
		</div>

		<div id="pg03_dialog3" class="dialog pop_doc_search pg03_dialog"
			title="INCOMING NUMBERING 관리" style="min-width: 800px;">
			<div class="pop_box">
				<h4>TYPE 목록</h4>
				<div class="numberingCheck" style="float:right;color:red;">
					넘버링체계에는 숫자가 반드시 포함되어야 하며<br>
					하이픈(-) 기준으로 나누어 문자가 포함되지 않은<br>
					숫자만 있는 영역으로 발번합니다.<br>
					ex) PH55-CD001-HP-003 일 경우 003부터 발번<br>
				</div>
				<div class="tbl_wrap pg03_dialog_Grids"></div>
				<div class="txt_right mb5">
					<button class="button btn_small btn_gray pg03_dialogBtn">저장</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">취소</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삽입</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">삭제</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▲</button>
					<button class="button btn_small btn_gray pg03_dialogBtn">▼</button>
				</div>
			</div>
		</div>
		
	</div>

	<div id="pg04" class="pg">
		<h4>DOCUMENT, TRANSMITTAL ISSUE, RETURN 필드값 정의</h4>
		<form id="excelUploadForm_pg04" name="excelUploadForm_pg04"  enctype="multipart/form-data" method="post" onsubmit="return false;">
		<div class="pg_top">
			<div class="left" style="display:flex;">
				<div style="margin:auto;">CODE TYPE&nbsp;</div>
				<select name="" id="pg04_codeType" class="pr_gn">
					<option value="#sel_RevisionNumber">REVISION NUMBER</option>
					<option value="#sel_DocSize">DOC.SIZE</option>
					<option value="#sel_VDocSize">VENDOR DOC.SIZE</option>
					<option value="#sel_TRIssue">TR ISSUE PURPOSE</option>
					<option value="#sel_VTRIssue">VTR ISSUE PURPOSE</option>
					<option value="#sel_STRIssue">STR ISSUE PURPOSE</option>
					<option value="#sel_DocCat">DOC.CATEGORY</option>
					<option value="#sel_VDocCat">VENDOR DOC.CATEGORY</option>					
					<option value="#sel_TRReturnStep">TR RETURN STATUS</option>
					<option value="#sel_VTRReturnStep">VTR RETURN STATUS</option>
					<option value="#sel_STRReturnStep">STR RETURN STATUS</option>
					<option value="#sel_DocType">DOC.TYPE</option>
					<!--
					<option value="#sel_TRReturn">TR RETURN STEP</option>
					<option value="#sel_VTRReturn">VTR RETURN STEP</option>
					-->
				</select>
				<input type="hidden" name="fileName" id="fileName04"> 
				<input type="hidden" name="jsonRowdatas" id="jsonRowdatas04">
				<input type="hidden" name="prj_id" id="prj_id04"> 
				<input type="hidden" name="prj_nm" id="prj_nm04">
				<input type="hidden" name="set_code_type" id="set_code_type04">

				<input type="checkbox" id="pg04_copyChk" class="ml5" style="margin: auto;">
				<label class="copyChkdiv04" for="">COPY</label>&nbsp;&nbsp;&nbsp;
				<div class="copyDiv04" style="display:none;">
					<div class="multi_sel_wrap" id="PG04_select_div">
						<input type="hidden" id="PG04_selectPrjId">
						<button class="multi_sel" id="PG04_selected_prj">SELECT</button>
						<div class="multi_box" id="PG04_header_multi_box">
							<div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prjGen04_search"
										type="text">
									<button class="button btn_search" onclick="prjGen04Search()">검색</button>
								</div>
								<div id="prj_list_grid_pg04"></div>
							</div>
							<%-- <table class="header_prj_select_table">
								<tr>
									<th>Project No</th>
									<th>Project Name</th>
									<th>Project Full Name</th>
								</tr>
								<c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
									<tr class="PG04_prj_select" id="PG04_prj_id_is_${i.prj_id}">
										<td>${i.prj_no}</td>
										<td class="prj_nm">${i.prj_nm}</td>
										<td>${i.prj_full_nm}</td>
									</tr>
								</c:forEach>
							</table> --%>
						</div>
					</div>
					<button class="button btn_blue ml5 pg04_Btn">COPY</button>
				</div>
			</div>
			<div class="right">
				<button class="button btn_purple pg04_Btn">Refresh</button>
				<button class="button btn_blue pg04_Btn">Apply</button>
			</div>
		</div>
		<div class="pg_top">
			<div class="right">
				<button class="button btn_purple pg04_Btn">ToExcel</button>
				<button class="button btn_purple pg04_Btn" disabled>Up</button>
				<button class="button btn_purple pg04_Btn" disabled>Down</button>
				<button class="button btn_blue pg04_Btn">Add</button>
				<button class="button btn_blue pg04_Btn">Delete</button>
			</div>
		</div>
		<hr>
		<h4>Code</h4>
		<div class="sel_tab">
			<div id="sel_RevisionNumber">
				<div class="tbl_wrap pg04_Grids" id="pg04_RevisionNumber"></div>
			</div>
			<div id="sel_DocSize" class="on">
				<div class="tbl_wrap pg04_Grids" id="pg04_DocSize"></div>
			</div>
			<div id="sel_VDocSize">
				<div class="tbl_wrap pg04_Grids" id="pg04_VDocSize"></div>
			</div>
			<div id="sel_TRIssue">
				<div class="tbl_wrap pg04_Grids" id="pg04_TRIssue"></div>
			</div>
			<div id="sel_VTRIssue">
				<div class="tbl_wrap pg04_Grids" id="pg04_VTRIssue"></div>
			</div>
			<div id="sel_STRIssue">
				<div class="tbl_wrap pg04_Grids" id="pg04_STRIssue"></div>
			</div>
			<div id="sel_DocCat">
				<div class="tbl_wrap pg04_Grids" id="pg04_DocCat"></div>
			</div>
			<div id="sel_VDocCat">
				<div class="tbl_wrap pg04_Grids" id="pg04_VDocCat"></div>
			</div>
			<div id="sel_TRReturn">
				<div class="tbl_wrap pg04_Grids" id="pg04_TRReturn"></div>
			</div>
			<div id="sel_VTRReturn">
				<div class="tbl_wrap pg04_Grids" id="pg04_VTRReturn"></div>
			</div>
			<div id="sel_STRReturn">
				<div class="tbl_wrap pg04_Grids" id="pg04_STRReturn"></div>
			</div>
			<div id="sel_DocType">
				<div class="tbl_wrap pg04_Grids" id="pg04_DocType"></div>
			</div><!-- 
			<div id="sel_VTRReturnStep">
				<div class="tbl_wrap pg04_Grids" id="pg04_VTRReturnStep"></div>
			</div>
			<div id="sel_TRReturnStep">
				<div class="tbl_wrap pg04_Grids" id="pg04_TRReturnStep"></div>
			</div> -->
		</div>
		</form>
	</div>
	
	<div id="pg05" class="pg">
		<h4>LETTER, E-MAIL 코드값 정의</h4>
		
		<form id="excelUploadForm_pg05" name="excelUploadForm_pg05"  enctype="multipart/form-data" method="post" onsubmit="return false;">
		<div class="pg_top">
				<div class="left" style="display:flex">
				<div style="margin:auto;">CODE TYPE&nbsp;</div>
				<select name="" id="pg05_codeType" class="pr_gn">
					<option value="#sel_CorRespon">CORRESPONDENCE RESPONSIBILITY</option>
				</select> <input type="checkbox" id="pg05_copyChk" class="ml5" style="margin: auto;"> <label class="copyChkdiv05" for="">COPY</label>&nbsp;&nbsp;&nbsp;
				<input type="hidden" name="fileName" id="fileName05"> 
				<input type="hidden" name="jsonRowdatas" id="jsonRowdatas05">
				<input type="hidden" name="prj_id" id="prj_id05"> 
				<input type="hidden" name="prj_nm" id="prj_nm05">
				<input type="hidden" name="set_code_type" id="set_code_type05">				
				
				<div class="copyDiv05" style="display:none;">
					<div class="multi_sel_wrap" id="PG05_select_div">
					<input type="hidden" id="PG05_selectPrjId">
					<button class="multi_sel" id="PG05_selected_prj">SELECT</button>
					<div class="multi_box" id="PG05_header_multi_box">
						<div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prjGen05_search"
										type="text">
									<button class="button btn_search" onclick="prjGen05Search()">검색</button>
								</div>
								<div id="prj_list_grid_pg05"></div>
							</div>
						<%-- <table class="header_prj_select_table">
							<tr>
								<th>Project No</th>
								<th>Project Name</th>
								<th>Project Full Name</th>
							</tr>
							<c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
								<tr class="PG05_prj_select" id="PG05_prj_id_is_${i.prj_id}">
									<td>${i.prj_no}</td>
									<td class="prj_nm">${i.prj_nm}</td>
									<td>${i.prj_full_nm}</td>
								</tr>
							</c:forEach>
						</table> --%>
					</div>
				</div>
				<button class="button btn_blue ml5 pg05_Btn">COPY</button>
			</div>
			</div>
			<div class="right">
				<button class="button btn_purple pg05_Btn">Refresh</button>
				<button class="button btn_blue pg05_Btn">Apply</button>
			</div>
		</div>
		<div class="pg_top">
			<div class="right">
				<button class="button btn_purple pg05_Btn">ToExcel</button>
				<button class="button btn_purple pg05_Btn">Up</button>
				<button class="button btn_purple pg05_Btn">Down</button>
				<button class="button btn_blue pg05_Btn">Add</button>
				<button class="button btn_blue pg05_Btn">Delete</button>
			</div>
		</div>
		<hr>
		<h4>Code</h4>
		<div class="sel_tab">
			<div id="sel_CorRespon" class="on">
				<div class="tbl_wrap pg05_Grids" id="pg05_CorRespon"></div>
			</div>
		</div>
		</form>
	</div>
	
	<div id="pg06" class="pg">
		<h4>STEP 프로세스 정의</h4>
		
		<form id="excelUploadForm_pg06" name="excelUploadForm_pg06"  enctype="multipart/form-data" method="post" onsubmit="return false;">
		<div class="pg_top">
			<div class="left" style="display: flex;">
				<div style="margin:auto;">CODE TYPE&nbsp;</div>
				<select name="" id="set_code_type" class="pr_gn"
					onchange="getSTEPPrjCodeSettingsList()">
					<option value="#sel_tab05">ENGINEERING STEP</option>
					<option value="#sel_tab06">VENDOR DOC STEP</option>
					<option value="#sel_tab07">SITE DOC STEP</option>
					<option value="#sel_tab08">PROCUREMENT STEP</option>
					<option value="#sel_tab09">BULK STEP</option>
				</select> 
				
				<input type="checkbox" class="ml5" id="STEP_CODE_COPY_CHECKBOX" style="margin: auto;">
				<label class="copyChkdiv06" for="">COPY</label>&nbsp;&nbsp;&nbsp;
				<div class="copyDiv06" style="display:none;">
					<input type="hidden" name="fileName" id="fileName06"> 
					<input type="hidden" name="jsonRowdatas" id="jsonRowdatas06">
					<input type="hidden" name="prj_id" id="prj_id06"> 
					<input type="hidden" name="prj_nm" id="prj_nm06">
					<input type="hidden" name="set_code_type" id="set_code_type06">	
					<div class="multi_sel_wrap" id="STEP_select_div">
						<input type="hidden" id="STEP_selectPrjId">
						<button class="multi_sel" id="STEP_selected_prj">SELECT</button>
						<div class="multi_box" id="STEP_header_multi_box">
							<div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prjGen06_search"
										type="text">
									<button class="button btn_search" onclick="prjGen06Search()">검색</button>
								</div>
								<div id="prj_list_grid_pg06"></div>
							</div>						
						
							<%-- <table class="header_prj_select_table">
								<tr>
									<th>Project No</th>
									<th>Project Name</th>
									<th>Project Full Name</th>
								</tr>
								<c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
									<tr class="STEP_prj_select" id="STEP_prj_id_is_${i.prj_id}">
										<td>${i.prj_no}</td>
										<td class="prj_nm">${i.prj_nm}</td>
										<td>${i.prj_full_nm}</td>
									</tr>
								</c:forEach>
							</table> --%>
						</div>
					</div>
					<button class="button btn_blue ml5" onclick="STEPCodeCopy()">COPY</button>
				</div>
			</div>
			<p>공사 시작 후 ENGINEERING STEP 순서변경, 삽입, 삭제는 전산실로 의뢰하세요.</p>
		</div>
		<hr>
		<h4>Code</h4>
		<div style="display:flex;justify-content: space-between;">
			<div class="txt_left mb10">
				<button class="button btn_purple" id="rvsRS">IFC Revised
					Reason Code</button>
			</div>
			<div class="txt_right mb10">
					<button class="button btn_purple" onclick="stepToExcel()">ToExcel</button>
					<button class="button btn_blue" onclick="STEPAdd()">Add</button>
					<button class="button btn_blue" onclick="STEPDelete()">Delete</button>
			</div>
		</div>

		<div class="sel_tab">
			<div id="sel_tab05" class="on">
				<div class="tbl_wrap" id="engStep"></div>
			</div>
			<div id="sel_tab06">
				<div class="tbl_wrap" id="venderDocStep"></div>
			</div>
			<div id="sel_tab07">
				<div class="tbl_wrap" id="siteDocStep"></div>
			</div>
			<div id="sel_tab08">
				<div class="tbl_wrap" id="ProcurementStep"></div>
			</div>
			<div id="sel_tab09">
				<div class="tbl_wrap" id="BulkStep"></div>
			</div>
		</div>
		<div style="display:flex;justify-content: space-between;">
			<div style="color:red;">
				※ Progress Rate의 합계값은 반드시 100이 되어야 합니다.<br/>
				※ 데이터 편집 후 Apply 버튼을 눌러야만 적용됩니다.
			</div>
			<div class="txt_right">
					<button id="pg06_UpBtn" class="button btn_purple">Up</button>
					<button id="pg06_DownBtn" class="button btn_purple">Down</button>
				<button class="button btn_purple" onclick="STEPCodeRefresh()">Refresh</button>
				<button class="button btn_blue" onclick="STEPCodeApply()">Apply</button>
			</div>
		</div>
		</form>
	</div>

	<div id="pg07" class="pg">
		<h4>DISCIPLINE 설정, 폴더 MATCH</h4>
		<div class="pg_top">
			<div class="left" style="display: flex;">
				<div style="margin: auto;">CODE TYPE&nbsp;</div>
				<select name="" id="pg07_codeType" class="pr_gn">
					<option value="#sel_tab09">DISCIPLINE</option>
				</select> <input type="checkbox" id="pg07_copyChk" class="ml5" style="margin: auto;"> <label
					class="copyChkdiv" for="">COPY</label>&nbsp;&nbsp;&nbsp;
				<div class="copyDiv" style="display: none;">
					<div class="multi_sel_wrap" id="pg07_select_div">
						<input type="hidden" id="pg07_selectPrjId">
						<button class="multi_sel" id="pg07_selected_prj">SELECT</button>
						<div class="multi_box" id="pg07_header_multi_box">
							<div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prjGen07_search"
										type="text">
									<button class="button btn_search" onclick="prjGen07Search()">검색</button>
								</div>
								<div id="prj_list_grid_pg07"></div>
							</div>
							<%-- <table class="header_prj_select_table">
								<tr>
									<th>Project No</th>
									<th>Project Name</th>
									<th>Project Full Name</th>
								</tr>
								<c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
									<tr class="pg07_prj_select" id="pg07_prj_id_is_${i.prj_id}">
										<td>${i.prj_no}</td>
										<td class="prj_nm">${i.prj_nm}</td>
										<td>${i.prj_full_nm}</td>
									</tr>
								</c:forEach>
							</table> --%>
						</div>
					</div>
					<button class="button btn_blue ml5 pg07_Btn">COPY</button>
				</div>
			</div>
			<div class="txt_right mb10">
				<button class="button btn_purple pg07_Btn">추가</button>
				<button class="button btn_blue pg07_Btn">저장</button>
				<button class="button btn_purple pg07_Btn">삭제</button>
			</div>

			<div class="tbl_wrap">
				<table class="write">
					<tr>
						<th>CODE</th>
						<td><input class="pg07_input" type="text"></td>
					</tr>
					<tr>
						<th>DISCIPLINE DISPLAY</th>
						<td><input class="pg07_input" type="text"></td>
					</tr>
					<tr>
						<th>DESCRIPTION</th>
						<td><input class="pg07_input" type="text"></td>
					</tr>
					<tr>
						<th>FOLDER DISCIPLINE MATCHING</th>
						<td style="display:flex;"><textarea readonly rows="1" class="pg07_input" style="resize: none; width: 80%;"></textarea>
							<button class="button btn_blue ml5 pg07_Btn">MATCH</button></td>
					</tr>
				</table>
				<input class="pg07_input" type="hidden">
				<input class="pg07_input" type="hidden">
				
			</div>
			<div style="width: 100%; margin-bottom: 15px; padding-top: 3px;">
				<h4>DISCIPLINE</h4>
				<div class="tbl_wrap pg07_Grids" id="pg07_discipline"></div>
			</div>
			</div>
		</div>

	<!-- ProjectGeneral step7 select foler 다이얼로그 -->

	<div id="pg07_dialog" class="dialog pop_doc_search"
		title="Select folder">
		<div class="pop_box">
			<nav id="pg07_tree"></nav>
		</div>
		<div class="btn_group right">
			<button id="pg07_folderSelect" class="button btn_blue">Select</button>
		</div>
	</div>



	<div id="pg08" class="pg">
		<input type="hidden" id="memeberSettingPrjId">
	</div>

		<div id="pg09" class="pg">
			<h4>폴더별 권한 설정</h4>
			<h5>DOCUMENT CONTROL</h5>
			<div style="display: flex;">
				<div class="pg_tree" id="pg09_tree" style="border:none;">
					<nav id="folder_allow" ></nav>
				</div>
				<div>
					<button id="folderRightsRefresh" class="button btn_blue" onclick="folderRightsRefresh()">Refresh</button>
				</div>
			</div>
		</div>
		<div id="pg10" class="pg">
			<h4>발송메일 템플릿</h4>
			<div class="pg_top">
				<div class="left mailt">
					<div class="multi_sel_wrap ml5" id="emailSelectWrap">
						<button class="multi_sel" id="emailTypeSelected">SELECT</button>
						<input type="hidden" id="selectedEmailFeature"> <input
							type="hidden" id="selectedEmailTypeId">
						<div class="multi_box">
							<table id="emailTypeMultiBox">
							</table>
						</div>
					</div>
					<button class="button btn_blue" id="mailType">TYPE EDIT</button>
					<!-- <button class="button btn_blue ml5" id="mailNotice">E-mailing notice </button>
                                                <button class="button btn_blue ml5" id="userSearchM">User Search </button> -->
				</div>
				<div class="right">
					<button class="button btn_purple" onclick="emailTemplateSave()">SAVE</button>
				</div>
			</div>
			<!-- <span style="color:red;">※ 메일 구분자는 , 입니다.</span> -->
			<hr>
			<div class="sec">
				<div class="tbl_wrap" style="overflow-x: visible;">
					<table class="write">
						<colgroup>
							<col style="width: 25%">
							<col style="">
						</colgroup>
						<!-- <tr>
							<th class="required">Auto Send <i>required</i></th>
							<td><input type="radio" name="template_auto_send_yn" value="Y">
								<label for="" class="mr10">Y</label> <input type="radio"
								name="template_auto_send_yn" value="N" checked> <label for="">N</label>
							</td>
						</tr> -->
						<input type="hidden" id="current_template_autosend_yn">
						<tr>
							<th class="required">Sender e-mail <i>required</i></th>
							<td>
								<!-- <select id="sender_email" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select> -->
								<input type="text" id="sender_email" class="full">
	                    	</td>
						</tr>
						<tr>
							<th id="receiverEmail">Receiver e-mail</th>
							<td>
								<select id="receiver_email" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                    	</td>
						</tr>
						<tr>
							<th>ReferTo e-mail</th>
							<td>
	                    		<select id="referto_email" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                    	</td>
						</tr>
						<tr>
							<th>BCC e-mail</th>
							<td>
	                    		<select id="bcc_email" class="full" placeholder="메일주소 입력 후 enter를 누르거나 임직원일 경우 이름을 입력 후 enter를 눌러주세요."></select>
	                    	</td>
						</tr>
					</table>
				</div>

				<div class="tbl_wrap" id="prjSystemPrefix"></div>

				<div class="tbl_wrap">
					<table class="write">
						<colgroup>
							<col style="width: 25%">
							<col style="">
						</colgroup>
						<tr>
							<th>File Down Link</th>
							<td><input type="radio" name="file_down_link_yn" value="Y" checked>
								<label for="" class="mr10">Y</label> <input type="radio"
								name="file_down_link_yn" value="N"> <label for="">N</label>
							</td>
						</tr>
						<tr id="fileRenameTr">
							<th>File Rename</th>
							<td><input type="checkbox" id="file_rename_yn" onchange="fileRenameCheckInEmailTemplate()"> <label
								for="" class="mr10 block">(%DOC_NO%, %REV_NO%, %TITLE%)</label>
								<input type="text" class="full" id="file_rename"></td>
						</tr>
						<tr>
							<th>Subject Prefix</th>
							<td><input type="text" class="full" id="subject_prefix"></td>
						</tr>
					</table>

				</div>
				<form id="emailTemplateUploadForm" name="emailTemplateUploadForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
					<span class="filebox">
						<button class="button btn_purple" onclick="emailTemplatePCSave()">이메일 템플릿 PC에 저장하기</button>
					
						<label for="emailTemplateFromPC" id="FromPCLabel" class="button btn_purple">이메일 템플릿 텍스트파일 불러오기</label>
						<input type="file" name="emailTemplateFromPC" id="emailTemplateFromPC" class="upload-hidden" onchange="checkTxtTemplate()">
					</span>
				</form>
				<div class="smartEdit">
					<textarea rows="500" id="summernoteMailTemplate" cols="500"
						style="max-height: 300px;"></textarea>
				</div>
			</div>
		</div>
		<div id="pg11" class="pg">
		<div style="color:red;">* .xls 확장자 파일만 업로드 할 수 있습니다.</div>
												<div style="display:flex;justify-content: space-between;">
													<h4>TR Cover 템플릿 업로드</h4>
													<button class="button btn_blue" onclick="trCoverUpload()">Save</button>
									           	</div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
                                                        <tr>
                                                            <th>
                                                                TR COVER TEMPLATE
                                                            </th>
                                                            <td colspan="3">
															<form id="trCoverForm" name="trCoverForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
										                    <span class="filebox">
										                        <input class="upload-name" value="파일선택" id="tr_cover_attachfilename" disabled="disabled">
										
										                        <label for="tr_cover_filename" class="button btn_blue" id="tr_cover_attach">File</label>
										                        <input type="file" accept="application/vnd.ms-excel" name="trCover_File" id="tr_cover_filename" class="upload-hidden">
										                    </span>
										                    <span id="tr_cover_uploadFileInfo"></span>
					                                    	</form>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
									            
									            <div style="display:flex;justify-content: space-between;">
													<h4>VTR Cover 템플릿 업로드</h4>
													<button class="button btn_blue" onclick="vtrCoverUpload()">Save</button>
									           	</div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
                                                        <tr>
                                                            <th>
                                                                VTR COVER TEMPLATE
                                                            </th>
                                                            <td colspan="3">
															<form id="vtrCoverForm" name="vtrCoverForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
										                    <span class="filebox">
										                        <input class="upload-name" value="파일선택" id="vtr_cover_attachfilename" disabled="disabled">
										
										                        <label for="vtr_cover_filename" class="button btn_blue" id="vtr_cover_attach">File</label>
										                        <input type="file" accept="application/vnd.ms-excel" name="vtrCover_File" id="vtr_cover_filename" class="upload-hidden">
										                    </span>
										                    <span id="vtr_cover_uploadFileInfo"></span>
					                                    	</form>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
												<div style="display:flex;justify-content: space-between;">
													<h4>STR Cover 템플릿 업로드</h4>
													<button class="button btn_blue" onclick="strCoverUpload()">Save</button>
									           	</div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
                                                        <tr>
                                                            <th>
                                                                STR COVER TEMPLATE
                                                            </th>
                                                            <td colspan="3">
															<form id="strCoverForm" name="strCoverForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
										                    <span class="filebox">
										                        <input class="upload-name" value="파일선택" id="str_cover_attachfilename" disabled="disabled">
										
										                        <label for="str_cover_filename" class="button btn_blue" id="str_cover_attach">File</label>
										                        <input type="file" accept="application/vnd.ms-excel" name="strCover_File" id="str_cover_filename" class="upload-hidden">
										                    </span>
										                    <span id="str_cover_uploadFileInfo"></span>
					                                    	</form>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
		</div>
	</div>


	<div class="dialog pop_corrSetting" title="CORRESPONDENCE TYPE 관리" style="max-width:400px;">
    <div class="pop_box">
        <h4>TYPE 목록</h4>
        <div class="tbl_wrap">
            <table class="list">
                <tr>
                    <th>TYPE</th>
                    <th>DESCRIPTION</th>
                </tr>
                <tr>
                    <td>LT</td>
                    <td>LETTER</td>
                </tr>
                <tr>
                    <td>DT</td>
                    <td>DOC. TRANSMITTAL</td>
                </tr>
                <tr>
                    <td>VT</td>
                    <td>VDOC. TRANSMITTAL</td>
                </tr>
                <tr>
                    <td>EL</td>
                    <td>E-MAIL</td>
                </tr>
                <tr>
                    <td>ST</td>
                    <td>SITE TRANSMITTAL</td>
                </tr>
            </table>
        </div>
        <div class="txt_right mb5">
            <button class="button btn_small btn_gray">저장</button>
            <button class="button btn_small btn_gray">취소</button>
            <button class="button btn_small btn_gray">삽입</button>
            <button class="button btn_small btn_gray">삭제</button>
            <button class="button btn_small btn_gray">▲</button>
            <button class="button btn_small btn_gray">▼</button>
        </div>
    </div>
</div>

<div class="dialog pop_outgoingNum" title="OUTGOING NUMBERING 관리" style="max-width:400px;">
    <div class="pop_box">
        <h4>OUTGOING 목록</h4>
        <div class="tbl_wrap">
            <table class="list">
                <tr>
                    <th>TYPE</th>
                    <th>NUMBERING</th>
                </tr>
                <tr>
                    <td>
                        <select name="" id="">
                            <option value="">선택</option>
                        </select>
                    </td>
                    <td>HP-LG-LT-3%</td>
                </tr>
            </table>
        </div>
        <div class="txt_right mb5">
            <button class="button btn_small btn_gray">저장</button>
            <button class="button btn_small btn_gray">취소</button>
            <button class="button btn_small btn_gray">삽입</button>
            <button class="button btn_small btn_gray">삭제</button>
            <button class="button btn_small btn_gray">▲</button>
            <button class="button btn_small btn_gray">▼</button>
        </div>
    </div>
</div>


<div class="dialog pop_rvsRS pop_doc_search" title="IFC REVISED REASON" style="max-width:1000px;">
    <div class="pop_box">

	<form id="excelUploadForm_pg06_Ifc" name="excelUploadForm_pg06_Ifc"  enctype="multipart/form-data" method="post" onsubmit="return false;">
        <div class="pg_top">
            <div class="left" style="display:flex;">
            	<input type="hidden" name="fileName" id="fileName06_Ifc"> 
				<input type="hidden" name="jsonRowdatas" id="jsonRowdatas06_Ifc">
				<input type="hidden" name="prj_id" id="prj_id06_Ifc"> 
				<input type="hidden" name="prj_nm" id="prj_nm06_Ifc"><!-- 
					<div class="multi_sel_wrap" id="IFC_multi_sel_wrap">
						<input type="hidden" id="IFC_selectPrjId">
                        <button class="multi_sel" id="IFC_selected_prj">SELECT</button>
                        
                        <div class="multi_box" id="IFC_header_multi_box">
                            <table class="header_prj_select_table">
                                <tr>
                                    <th>Project No</th>
                                    <th>Project Name</th>
                                    <th>Project Full Name</th>
                                </tr>
                                <c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
                                <tr class="IFC_prj_select" id="IFC_prj_id_is_${i.prj_id}">
			                        <td>${i.prj_no}</td>
			                        <td class="prj_nm">${i.prj_nm}</td>
			                        <td>${i.prj_full_nm}</td>
								</tr>
								</c:forEach>
                            </table>
                        </div>
                    </div>
                <input type="checkbox" class="ml5" id="IFC_CODE_COPY_CHECKBOX" style="margin: auto;">
                <label for="">COPY</label> -->
	            <input type="checkbox" id="IFC_CODE_COPY_CHECKBOX" class="ml5" style="margin: auto;"> <label
						class="copyChkdivIfc" for="">COPY</label>&nbsp;&nbsp;&nbsp;
					<div class="copyDivIfc" style="display: none;">
						<div class="multi_sel_wrap" id="IFC_multi_sel_wrap">
							<input type="hidden" id="IFC_selectPrjId">
	                        <button class="multi_sel" id="IFC_selected_prj">SELECT</button>
	                        
	                        <div class="multi_box" id="IFC_header_multi_box">
	                        <div class="prj_search" style="padding: 10px;">
								<div class="left">
									<label style="color: #000;" for="">Project Search</label> <input id="prjGenIFC_search"
										type="text">
									<button class="button btn_search" onclick="prjGenIFCSearch()">검색</button>
								</div>
								<div id="prj_list_grid_pgIFC"></div>
							</div>
	                            <%-- <table class="header_prj_select_table">
	                                <tr>
	                                    <th>Project No</th>
	                                    <th>Project Name</th>
	                                    <th>Project Full Name</th>
	                                </tr>
	                                <c:forEach var="i" items="${getPrjInfoList}" varStatus="vs">
	                                <tr class="IFC_prj_select" id="IFC_prj_id_is_${i.prj_id}">
				                        <td>${i.prj_no}</td>
				                        <td class="prj_nm">${i.prj_nm}</td>
				                        <td>${i.prj_full_nm}</td>
									</tr>
									</c:forEach>
	                            </table> --%>
	                        </div>
	                    </div>
	                	<button class="button btn_blue ml5" onclick="IFCCodeCopy()">COPY</button>      
					</div>
				</div>
	            <div class="right">
	                <button class="button btn_purple" onclick="IFCCodeRefresh()">Refresh</button>
	                <button class="button btn_blue" onclick="IFCCodeApply()">Apply</button>
	            </div>            
			</div>
        </div>
    	
        <div class="pg_top">
            <div class="right">
                <button class="button btn_purple" onclick="IFCToExcel()">ToExcel</button>
                <button id="pg06_IFCUpBtn" class="button btn_purple">Up</button>
                <button id="pg06_IFCDownBtn" class="button btn_purple">Down</button>
                <button class="button btn_blue" onclick="IFCAdd()">Add</button>
                <button class="button btn_blue" onclick="IFCDeletePopup()">Delete</button>
            </div>
        </div>

        <h4>CODE</h4>
        <div class="tbl_wrap" id="rvsRS_pop_tbl">
        
        </div>
	</form>
	
    </div>
</div>



<div class="dialog pop_STEPCodeDelete pop_doc_search" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" id="STEPDelete" onclick="STEPDelete()">Delete</button>
                <button class="button btn_purple" id="STEPDeleteCancel" onclick="STEPDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>
<div class="dialog pop_IFCCodeDelete pop_doc_search" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="IFCDelete()">Delete</button>
                <button class="button btn_purple" onclick="IFCDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>

<div class="dialog pop_mailType" title="E-mailing Type" style="max-width:1100px;">
    <div class="pop_box">
    <form id="excelUploadForm_pg10_Email" name="excelUploadForm_pg10_Email"  enctype="multipart/form-data" method="post" onsubmit="return false;">
        <div class="pg_top">
            <div class="left">
            	<input type="hidden" name="fileName" id="fileName10_Email"> 
				<input type="hidden" name="jsonRowdatas" id="jsonRowdatas10_Email">
				<input type="hidden" name="prj_id" id="prj_id10_Email"> 
				<input type="hidden" name="prj_nm" id="prj_nm10_Email">
				<input type="hidden" name="email_feature" id="email_feature10_Email">
				
                <label for="">FEATURE</label>
                <select name="" id="mailTypeSelectFeature" onchange="getPrjEmailType()">
                    <option value="CORRESPONDENCE" selected>CORRESPONDENCE</option>
                    <option value="TR">TR</option>
                    <option value="STR">STR</option>
                    <option value="VTR">VTR</option>
                </select>
                <label for="" class="ml10">MAIL TEMPLATE</label>
                <input type="text" id="email_type">
                <label for="" class="ml10">DESCRIPTION</label>
                <input type="text" id="email_desc">
                <label for="" class="ml10">AUTO SEND</label>
                <select id="email_auto_send_yn">
                	<option value="Y">Y</option>
                	<option value="N">N</option>
                </select>
            </div>
        </div>
        <div class="pg_top">
            <div class="right">
                <button class="button btn_blue" onclick="emailTypeToExcel()">To Excel</button>
                <button class="button btn_purple" onclick="emailTypeDeleteOpen()">Delete</button>
                <button class="button btn_blue" onclick="emailTypeSave()">Save</button>
            </div>
        </div>
        <hr>
        <div class="tbl_wrap" id="tblMailType">

        </div>
    </form>
    </div>
</div>

<div class="dialog pop_EmailTypeDelete" title="DELETE CONFIRM" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Would you like to delete it?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_blue" onclick="emailTypeDelete()">Delete</button>
                <button class="button btn_purple" onclick="emailTypeDeleteCancel()">Cancel</button>
            </div>
    </div>
</div>
<!-- pg02 삭제확인팝업 -->
<div class="dialog pop_deletePg02Confirm" title="Delete Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Are you sure to delete this code?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" id="deleteCode02Yes" onclick="deleteCode02Yes()">Yes</button>
                <button class="button btn_blue" id="deleteCode02Cancel">Cancel</button>
            </div>
    </div>
</div>
<!-- pg04 삭제확인팝업 -->
<div class="dialog pop_deletePg04Confirm" title="Delete Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Are you sure to delete this code?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" id="deleteCodeYes" onclick="deleteCodeYes()">Yes</button>
                <button class="button btn_blue" id="deleteCode04Cancel">Cancel</button>
            </div>
    </div>
</div>
<!-- pg04 refresh확인팝업 -->
<div class="dialog pop_RefershPg04Confirm" title="Refresh Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="refreshCodeYes()">Yes</button>
                <button class="button btn_blue" onclick="refreshCodeCancel()">Cancel</button>
            </div>
    </div>
</div>
<!-- pg04 페이지 이동시 확인팝업 -->
<!-- <div class="dialog pop_movePg04Confirm" title="Refresh Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="moveCodeYes()">Yes</button>
                <button class="button btn_blue" onclick="moveCodeCancel()">Cancel</button>
            </div>
    </div>
</div> -->
<!-- pg05 삭제확인팝업 -->
<div class="dialog pop_deletePg05Confirm" title="Delete Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Are you sure to delete this code?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" id="deleteCode05Yes" onclick="deleteCode05Yes()">Yes</button>
                <button class="button btn_blue" id="deleteCode05Cancel">Cancel</button>
            </div>
    </div>
</div>
<!-- pg05 refresh확인팝업 -->
<div class="dialog pop_RefershPg05Confirm" title="Refresh Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="refreshCode05Yes()">Yes</button>
                <button class="button btn_blue" onclick="refreshCode05Cancel()">Cancel</button>
            </div>
    </div>
</div>
<!-- pg06 refresh확인팝업 -->
<div class="dialog pop_RefershPg06Confirm" title="Refresh Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		저장하지 않은 항목이 있습니다. 저장하지 않은 데이터는 유실됩니다. 계속 진행하시겠습니까?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="refreshCode06Yes()">Yes</button>
                <button class="button btn_blue" onclick="refreshCode06Cancel()">Cancel</button>
            </div>
    </div>
</div>
<!-- pg07 삭제확인팝업 -->
<div class="dialog pop_deletePg07Confirm" title="Delete Code">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		Are you sure to delete this code?
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" id="deleteCode07Yes" onclick="deleteCode07Yes()">Yes</button>
                <button class="button btn_blue" id="deleteCode07Cancel">Cancel</button>
            </div>
    </div>
</div>