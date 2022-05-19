<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resource/js/globalSearch.js"></script>
<script>
	$(".date").datepicker();
	$.datepicker.setDefaults({
		  dateFormat: 'yy-mm-dd',
		  prevText: '이전 달',
		  nextText: '다음 달',
		  monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		  monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		  dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		  dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		  showMonthAfterYear: true,
		  yearSuffix: '년'
		});
</script>

<div class="line mb20">
	<div class="left">
		<label for="">MAX DISPLAY</label> <select name="" id="">
			<option value="">50</option>
			<option value="">100</option>
			<option value="">500</option>
			<option value="">1000</option>
		</select>
	</div>
	<div class="right">
		<button class="button btn_default">TO EXCEL</button>
		<button id="searchBtn" class="button btn_default">SEARCH</button>
	</div>
</div>
<div class="sec">
	<h4>Basic Conditions</h4>
	<div class="tbl_wrap">
		<table class="write">
			<colgroup>
				<col style="width: 100px">
				<col style="">
			</colgroup>
			<tr>
				<th>Path</th>
				<td><input type="text" style="width: calc(100% - 200px);">
				<button class="button btn_gray ml5 btn_sel_folder">Select
						Folder</button></td>
			</tr>
			<tr>
				<th>Doc. No.</th>
				<td><select name="" id="">
						<option value="like">Like</option>
						<option value="=">=</option>
						<option value="start width">Start Width</option>
				</select> <input type="text"></td>
			</tr>
			<tr>
				<th>Title</th>
				<td><select name="" id="">
						<option value="like">Like</option>
						<option value="=">=</option>
						<option value="start width">Start Width</option>
				</select> <input type="text"></td>
			</tr>
		</table>
	</div>
</div>
<div class="sec">
	<h4>Detail Conditions</h4>
	<div class="tbl_wrap">
		<table class="write">
			<colgroup>
				<col style="width: 150px">
				<col style="">
				<col style="width: 80px">
				<col style="">
			</colgroup>
			<tr>
				<th>Doc Type</th>
				<td><select name="" id="">
					<option value="all">All</option>
				</select></td>
				<th>Version</th>
				<td><select name="" id="">
					<option value="current">Current</option>
					<option value="all">All</option>
				</select></td>
			</tr>
			<tr>
				<th>Modifier</th>
				<td colspan="3"><input type="text" class="full"></td>
			</tr>
			<tr>
				<th><input type="checkbox"> Modified Date</th>
				<td colspan="3"><input type="text" class="date"><span>~</span>
					<input type="text" class="date"></td>
			</tr>
		</table>
	</div>
</div>



<div class="dialog pop_doc_search" title="Select folder">
    <div class="pop_box">
        <nav id="pop_tree">
        </nav>
    </div>
    <div class="btn_group right">
        <button class="button btn_blue">Select</button>
    </div>
</div>


<script>


var folders = [ {
	'text' : '프로젝트 약칭',
	'icon' : "images/pinwheel.png",
	'state' : {
		'opened' : true,
		'selected' : true
	},
	'children' : [ {
		'text' : 'CORRESPONDENCE',
		'state' : {
			'opened' : true
		},
		'children' : [ {
			'text' : 'TRANSMITTAL',
			'children' : [ {
				'text' : 'DCI TRANSMITTAL',
				'children' : [ {
					'text' : 'OUTGOING'
				}, {
					'text' : 'INCOMING'
				} ]
			}, {
				'text' : 'VENDOR DCI TRANSMITTAL',
				'children' : [ {
					'text' : 'OUTGOING'
				}, {
					'text' : 'INCOMING'
				} ]
			}, {
				'text' : 'SITE DCI TRANSMITTAL',
				'children' : [ {
					'text' : 'OUTGOING'
				}, {
					'text' : 'INCOMING'
				} ]
			},

			]
		}, {
			'text' : 'LETTER',
			'children' : [ {
				'text' : 'OUTGOING'
			}, {
				'text' : 'INCOMING'
			} ]
		}, {
			'text' : 'E-MAIL',
			'children' : [ {
				'text' : 'OUTGOING'
			}, {
				'text' : 'INCOMING'
			} ]
		} ]
	}, {
		'text' : 'ENGINEERING',
		'children' : [ {
			'text' : 'DCI',
			'children' : [ {
				'text' : 'BASIC'
			}, {
				'text' : 'DETAIL'
			}, {
				'text' : 'STRUCTURE'
			}, {
				'text' : 'ARCHITECTURAL'
			}, {
				'text' : 'CIVIL'
			}, {
				'text' : 'ELECTRICAL'
			}, {
				'text' : 'INSTRUMENTATION'
			}, {
				'text' : 'PIPING'
			}, {
				'text' : 'PROCESS'
			}, {
				'text' : 'MECHANICAL'
			} ]
		}

		]
	} ]
} ];

$('#pop_tree').jstree({
	'core' : {
		'data' : folders
	}
})


    $(".pop_doc_search").dialog({
        draggable: true,
        autoOpen: false
    });
    $(document).on("click", ".btn_sel_folder", function() {
        $(".pop_doc_search").dialog("open");
        return false;
    });
</script>