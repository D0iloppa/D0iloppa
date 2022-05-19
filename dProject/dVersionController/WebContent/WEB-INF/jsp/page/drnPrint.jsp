<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <META http-equiv="Pragma" content="no-cache">
<!DOCTYPE html>
<html>
<head>
	<title>DRN TITLE</title>
	<style type="text/css">
    @page a4sheet { size: 21.0cm 29.7cm }
    .a4 { 
	    page: a4sheet; 
	    page-break-after: always;
	    margin-top:10px;
    }
    
    table{
    	table-layout: fixed;
    	border-collapse: collapse;
    }
    .drn_header{
    	width:100%
    }
    .topLogo{
    	width:100%;
    	text-align:center;
    }
    .topLogo td{
    	border:1px solid;
    	height:80px;
    }
    .topInfo{
    	width:100%
    }
    
    .drn_sheet_table{
    	width:100%;
    	text-align: center;
    }
    
    .drn_sheet_table th{
    	border:1px solid;
    	font-size:5px;
    }
    
    .drn_sheet_table td{
    	border:1px solid;
    	font-size:5px;
    	word-wrap: break-word;
    }
    
    .drn_page{
    	margin-top:10px;
    	float:right;
    	font-weight: bold;
    }
    
    
    #bot_table{
    	width: 100%;
    	text-align:center;
    }
    
    
	</style>
	
	
	<script src="${pageContext.request.contextPath}/resource/js/jquery-3.4.1.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery-ui.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/drnPrint.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/html2canvas.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jspdf.min.js"></script>
	
	<script>
	
	  const prj_id = '${prj_id}';
	  const drn_id = '${drn_id}';
	  
	  function getter() {
		  let obj = {};
		  obj.prj_id = prj_id;
		  obj.drn_id = drn_id;
		  
		  return obj;
		  
	  }
	</script>
	   	
</head>


<body>

<!-- pdf출력할 전체 페이지 -->
<div id="drnPrint_page">
	<div style="float:right;">
		<button id="print_btn" style="display:none; margin-bottom:2px;" onclick="print_btn()">Print</button>
	</div>
	<!-- 한 페이지	 -->
	<div id="page_0" class='a4'>
		<!-- 헤더부분 -->
		<div class="drn_header">
				<table class="topLogo">
					<tr>
						<td><img width="80%" src="${pageContext.request.contextPath}/resource/images/hhi_CI_kor.png"></td>
						<td class="check_sheet_title"></td>
						<td><img width="70%" src="${pageContext.request.contextPath}/resource/images/hhi_CI_eng.png"></td>
					</tr>
				</table>

				<table class="topInfo">
					<tbody>
						<tr>
							<td colspan="1" style="font-size: 5px; font-weight:bold">PROJECT &nbsp;NO.</td>
							<td colspan="2" style="font-size: 5px;" class="prj_no"></td>
							<td colspan="1" style="font-size: 5px; font-weight:bold">PROJECT &nbsp;NAME</td>
							<td colspan="2" style="font-size: 5px;" class="prj_nm"></td>
							<td colspan="1" style="font-size: 5px; font-weight:bold">DATE</td>
							<td colspan="2" style="font-size: 5px;" class="print_date"></td>
						</tr>
						<tr>
							<td colspan="1" style="font-size: 5px; font-weight:bold">DOC. &nbsp;NO. </td>
							<td colspan="2" style="font-size: 5px;" class="doc_no"></td>
							<td colspan="1" style="font-size: 5px; font-weight:bold">REV.&nbsp;NO.</td>
							<td colspan="2" style="font-size: 5px;" class="rev_no"></td>
							<td colspan="1" style="font-size: 5px; font-weight:bold">STATUS</td>
							<td colspan="2" style="font-size: 5px;" class="print_status"></td>
						</tr>
						<tr>
							<td colspan="1" style="font-size: 5px; font-weight:bold">DOC.&nbsp;TITLE</td>
							<td style="font-size: 5px;" class="doc_title" colspan="5"></td>
						</tr>
					</tbody>
				</table>
<!-- 				콜론 있는 버전 -->
<!-- 						<table class="topInfo"> -->
<!-- 					<tbody> -->
<!-- 						<tr> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">PROJECT &nbsp;NO. :</td> -->
<!-- 							<td colspan="2" style="font-size: 5px;" class="prj_no"></td> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">PROJECT &nbsp;NAME &nbsp;&nbsp;&nbsp;:</td> -->
<!-- 							<td colspan="2" style="font-size: 5px;" class="prj_nm"></td> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">DATE :</td> -->
<!-- 							<td colspan="2" style="font-size: 5px;" class="print_date"></td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">DOC. &nbsp;NO. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</td> -->
<!-- 							<td colspan="2" style="font-size: 5px;" class="doc_no"></td> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">REV.&nbsp;NO. &nbsp;:</td> -->
<!-- 							<td colspan="2" style="font-size: 5px;" class="rev_no"></td> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">STATUS :</td> -->
<!-- 							<td colspan="2" style="font-size: 5px;" class="print_status"></td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td colspan="1" style="font-size: 5px; font-weight:bold">DOC.&nbsp;TITLE &nbsp;&nbsp;&nbsp;&nbsp;:</td> -->
<!-- 							<td style="font-size: 5px;" class="doc_title" colspan="5"></td> -->
<!-- 						</tr> -->
<!-- 					</tbody> -->
<!-- 				</table> -->
				

			</div>
		<!--시트부분 -->
		<div class="drn_sheet_div">
			<table id="drn_sheet_table_0" class="drn_sheet_table">
				<thead>
					<tr>
						<th colspan="1" class="no">No.</th>
						<th colspan="4" class="items">ITEMS</th>
						<th colspan="9" class="check_point">CHECK POINTS</th>
						<th colspan="1" class="discip">DISCI<br>PLINE</th>
						<th colspan="1" class="dgn">DGN</th>
						<th colspan="1" class="rev">REV</th>
						<th colspan="1" class="app">APP</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
			<!--마지막 -->
<!-- 		<div class="drn_bottom"> -->
<!-- 			<div class="drn_page"></div> -->
<!-- 		</div> -->
	</div>
</div>




</body>
</html>