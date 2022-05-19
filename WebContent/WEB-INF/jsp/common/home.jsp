<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resource/js/home.js"></script>
<script>


</script>

<style>
	@font-face {
	    font-family: '조선일보 명조체';
	    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/Chosunilbo_myungjo.woff') format('woff');
	}
	@font-face {
    font-family: '강한공군체 Bold';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts2201-3@1.0/ROKAFSlabSerifBold.woff') format('woff');
    font-weight: normal;
    font-style: normal;
	}
	@font-face {
    font-family: '함렛 Bold';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2110@1.0/Hahmlet-Bold.woff2') format('woff2');
    font-weight: normal;
    font-style: normal;
	}
	@font-face {
    font-family: '횡성한우체';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2202@1.0/HoengseongHanu.woff') format('woff');
    font-weight: normal;
    font-style: normal;
	}
	@font-face {
    font-family: 'KOHI배움';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2201-2@1.0/KOHIBaeumOTF.woff') format('woff');
    font-weight: normal;
    font-style: normal;
	}
	@font-face {
    font-family: '웰컴체 Bold';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2110@1.0/OTWelcomeBA.woff2') format('woff2');
    font-weight: normal;
    font-style: normal;
	}
</style>
<div id="authLabel" style="display:none"></div>

<div id="homeDiv">
	<h3>Notice</h3>
	<div class="sec">
		<h4>Bulletin Board(Project)</h4>
		<div class="tbl_wrap scroll" id="bulletinBoard"></div>
	</div>
<!-- 	<div class="sec"> -->
<!-- 		<h4>제출 지연 도서목록 Summary</h4> -->
<!-- 		<div class="tbl_wrap scroll" id="delaySummary"></div> -->
<!-- 	</div> -->
	<div class="sec">
		<div style="display:flex">
			<h4>TO-DO LIST (제출 도서 현황)</h4> 
			<div style="margin-top: 3px;margin-left: 10px;">
				<input type='checkbox' id="todoShowOnlySessionId" onclick="showMyTodo(this)"/>
				<span>나의 TO-DO LIST만 보기</span>
<!-- 				<span>Show only my TO-DO List</span> -->
			</div>
		</div>
		<div class="tbl_wrap scroll" id="todoSummary"></div>
	</div>
	<div class="sec">
		<h4>DRN 진행 현황</h4>
		<div class="tbl_wrap scroll" id="drnSummary"></div>
	</div>
	<div class="sec">
		<div style="display:flex">
			<h4>Transmittal Status Summary</h4>
			<span style="line-height:23px; margin-left:10px; font-size:11px">* 생성된 날짜 기준으로 리스트업 됩니다.</span>
		</div>
		<div class="tbl_wrap scroll" id="trStatusSummary"></div>
	</div>
	<div class="sec">
		<div style="display:flex">
			<h4>Correspondence Status Summary</h4>
			<span style="line-height:23px; margin-left:10px; font-size:11px">* 생성된 날짜 기준으로 리스트업 됩니다.</span>
		</div>
		<div class="tbl_wrap scroll" id="crStatusSummary"></div>
	</div>

	<div class="dialog pop_detailProject_home" title="프로젝트 게시판 상세 페이지"
		style="max-width: 1000px;">
		<div class="pop_box">
			<div class="sec">
				<div class="tbl_wrap">
					<table class="write">
						<colgroup>
							<col style="width: 100px">
							<col style="">
						</colgroup>
						<tr>
							<th>Title</th>
							<td colspan="5"><input type="text" id="project_title_home"
								style="width: 100%;" readonly></td>
						</tr>
						<tr>
							<th>Writer</th>
							<td><input type="text" id="project_writer_home"
								style="width: 100%;" readonly></td>
							<th>Create Date</th>
							<td><input type="text" id="project_createdate_home"
								style="width: 100%;"></td>
							<th>Hits</th>
							<td><input type="text" id="project_hits_home"
								style="width: 100%;" readonly></td>
						</tr>
						<tr>
							<th>Contents</th>
							<!--                                                 <td colspan="5"><textarea rows="500" id="contents" cols="500" style="max-height: 300px;" readonly></textarea></td> -->
							<td colspan="5"><div id="project_contents_home"
									style="min-height: 300px;"></div></td>
						</tr>
						<tr>
							<th>Attach Files</th>
							<td colspan="5">
								<div style="border: 1px solid #e0e0e0;">
									<div class="drop-zone-head-wrap">
										<ul class="drop-zone-head">
											<li style="width: 88%;">파일명</li>
											<li>용량</li>
										</ul>
									</div>
									<div id="detailViewFileWrap_home"></div>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="line">
				<div class="right r_array">
					<button class="button btn_blue"
						onclick="cancledetail_Project_Home()">취소</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 모든 alert창을 dialog로 바꿈 -->
	<div class="dialog pop_alertDialog" title="" style="max-width: 1000px;">
		<div class="pop_box">
			<div style="font-weight: bold; font-size: 11px; margin: 15px;"
				id="alertText"></div>
			<div class="right" style="margin: 15px; float: right;">
				<button class="button btn_purple" id="alertDialogOk" onclick="alertDialogOk()">확인</button>
			</div>
		</div>
	</div>
</div>