var drnPrint;
var rowIdx = 0;
var pageCnt = 0;
var pageChg = false;
const pageHeight = 800;
var drn_header;
var tblHead;
//var pageNumber;


const selectMap = ["","O","X","△","N/A"];

$(function(){ // document ready
	
	drnPrint = getter();
	tblHead = $(".drn_sheet_div").clone();
	
	/*
	pageNumber = document.createElement("div");
	pageNumber.classList.add("drn_bottom");
	let numberDiv = document.createElement("div");
	numberDiv.classList.add("drn_page");
	pageNumber.append(numberDiv);
	*/
		
	// $(".drn_bottom").clone();
	
	
	$.ajax({
	    url: 'getDrnPrint.do',
	    type: 'POST',
	    async:false,
	    data:{
	    	prj_id:drnPrint.prj_id,
	    	drn_id:drnPrint.drn_id
	    },
	    success: function onData (data) {
	    	drnPrint.val = data.model;
	    },
	    error: function onError (error) {
	        console.error(error);
	    }
	});
	
	setHeader();
	drn_header = $(".drn_header").clone();
	
	let rows = drnPrint.val.itemList;
	for(let i=0;i<rows.length;i++){
		
		// row 추가전, 테이블 생성여부 결정
		if($("#page_"+pageCnt).height() > pageHeight){

			pageChg = true;
			
			addPageNo();
			
			let page = document.createElement("div");
			page.id = "page_" + (++pageCnt);
			page.classList.add("a4");
			
			$("#drnPrint_page").append(page);
			
			appendHeader();
			//createNewTable();
			
			tblHead[0].childNodes[1].id = "drn_sheet_table_" + pageCnt;
			$("#page_"+pageCnt).append(tblHead[0]);
			
			console.log(page);

		}
		
		
		
		
		addNoRow(rows[i]);
	}
	
	
	setBottomLayout();
	pageNumbering();
	
	// print2Pdf();
	
	
	
});

function setHeader(){
	
	
	let docList = drnPrint.val.docList;
	
	
	$(".check_sheet_title").text(drnPrint.val.drn_info.check_sheet_title);
	$(".prj_no").text(drnPrint.val.drn_info.prj_no);
	$(".prj_nm").text(drnPrint.val.drn_info.prj_nm);
	$(".print_date").text(drnPrint.val.drn_info.reg_date);
	$(".print_status").text(drnPrint.val.status);
	
	let doc_no = docList[0].doc_no;
	let rev_no = docList[0].rev_no;
	let doc_title = docList[0].doc_title;
	
	// 2건 이상
	if(docList.length>1){
		doc_no += ( "외 " + docList.length-1 +"건"); 
		rev_no += ( "외 " + docList.length-1 +"건"); 
		doc_title += ( "외 " + docList.length-1 +"건"); 
	}
	
	$(".doc_no").text(doc_no);
	$(".rev_no").text(rev_no);
	$(".doc_title").text(doc_title);
	
	
}


function appendHeader(){
	// const tmp = drn_header.clone();
	
	$("#page_"+pageCnt).append(drn_header[0]);
	console.log($("#page_"+pageCnt)[0]);
}


function addNoRow(listData){
	// 페이지 체인징 여부를 따짐
	
	/*
	if($("#page_"+pageCnt).height() > pageHeight){

		pageChg = true;
		
		addPageNo();
		
		let page = document.createElement("div");
		page.id = "page_" + (++pageCnt);
		page.classList.add("a4");
		
		$("#drnPrint_page").append(page);
		
		appendHeader();
		//createNewTable();
		
		tblHead[0].childNodes[1].id = "drn_sheet_table_" + pageCnt;
		$("#page_"+pageCnt).append(tblHead[0]);
		
		console.log(page);

	}
	*/
	
	let addCnt = 0;
	for(let i=0;i<listData.itmes_cnt;i++){
		let row = document.createElement("tr");
		
		let no = document.createElement("td");
		no.colSpan = "1";
		no.rowSpan = listData.itmes_cnt;
		if(pageChg) no.rowSpan = listData.itmes_cnt - addCnt;
		no.innerText = listData.items_no;
		if(i<1 || pageChg) row.append(no);

		let items = document.createElement("td");
		items.colSpan = "4";
		items.rowSpan = listData.itmes_cnt;
		if(pageChg) items.rowSpan = listData.itmes_cnt - addCnt;
		items.innerText = listData.items_nm;
		if(i<1 || pageChg) row.append(items);
		
		let check_point = document.createElement("td");
		check_point.colSpan = "9";
		check_point.innerText = drnPrint.val.itemInList[rowIdx].check_items;
		row.append(check_point);
		
		let discip = document.createElement("td");
		discip.colSpan = "1";
		discip.innerText = drnPrint.val.itemInList[rowIdx].discipline_code_id
		row.append(discip);
		
		let dgn = document.createElement("td");
		dgn.colSpan = "1";
		if(drnPrint.val.itemInList[rowIdx].design_check_status)
		dgn.innerText = selectMap[drnPrint.val.itemInList[rowIdx].design_check_status];
		row.append(dgn);
		
		let rev = document.createElement("td");
		rev.colSpan = "1";
		if(drnPrint.val.itemInList[rowIdx].reviewed_check_status)
		rev.innerText = selectMap[drnPrint.val.itemInList[rowIdx].reviewed_check_status];
		row.append(rev);
		
		let app = document.createElement("td");
		app.colSpan = "1";
		if(drnPrint.val.itemInList[rowIdx].approved_check_status)
		app.innerText = selectMap[drnPrint.val.itemInList[rowIdx].approved_check_status];
		row.append(app);
		
		
		
		//$(".drn_sheet_table")[pageCnt].append(row);
		$("#drn_sheet_table_"+pageCnt).append(row);
		console.log("in row",pageCnt);
		rowIdx++;
		addCnt++;
		if(pageChg) addCnt = 0;
		pageChg = false;
		
		if($("#page_"+pageCnt).height() > pageHeight){
			console.log(pageCnt,$("#page_"+pageCnt).height());
			console.log("add table at",pageCnt);
			pageChg = true;
			
			// 하단에 넘버링 추가
			addPageNo();
			
			// 페이지 추가
			let page = document.createElement("div");
			page.id = "page_" + (++pageCnt);
			page.classList.add("a4");
			
			$("#drnPrint_page").append(page);
			
			appendHeader();
			
			tblHead[0].childNodes[1].id = "drn_sheet_table_" + pageCnt;
			$("#page_"+pageCnt).append(tblHead[0]);
			
			console.log(page);
			
		}
		
	}
	
	
	
}

function createNewTable(){
	let newTable = document.createElement("table");
	newTable.classList.add("drn_sheet_table");
	newTable.id = "drn_sheet_table_" + pageCnt;
	
	let row = document.createElement("th");
	// 헤더
	let no = document.createElement("th");
	no.colSpan = "1";
	no.innerText = "No.";
	row.append(no);
	
	let items = document.createElement("th");
	items.colSpan = "4";
	items.innerText = "ITEMS";
	row.append(items);
	
	let check_point = document.createElement("th");
	check_point.colSpan = "9";
	check_point.innerText = "CHECK POINTS";
	row.append(check_point);
	
	let discip = document.createElement("th");
	discip.colSpan = "1";
	discip.innerText = "DISCI\nPLINE";
	row.append(discip);
	
	let dgn = document.createElement("th");
	dgn.colSpan = "1";
	dgn.innerText = "DGN";
	row.append(dgn);
	
	let rev = document.createElement("th");
	rev.colSpan = "1";
	rev.innerText = "REV";
	row.append(rev);
	
	let app = document.createElement("th");
	app.colSpan = "1";
	app.innerText = "APP";
	row.append(app);
	
	
	newTable.append(row);
	
	
	$("#page_"+pageCnt).append(newTable);
	
	
	
	// return newTable;
}

function setBottomLayout(){
	// 마지막 bottom을 추가 하기전, 현재 페이지의 높이를 초과하는지 체크
	if($("#page_"+pageCnt).height() > pageHeight){
		
		let page = document.createElement("div");
		page.id = "page_" + (++pageCnt);
		page.classList.add("a4");
		$("#drnPrint_page").append(page);
		
		appendHeader();
		
		$("#page_"+pageCnt).append(tblHead[0]);
	}
	
	
	let bottom = document.createElement("div");
	bottom.id = "print_end";
	bottom.style["border"] = "1px solid";
	
	let table = document.createElement("table");
	table.id = "bot_table";
	
	let colgroup = document.createElement("colgroup");
	
	let colWidths = [2,5,1,1,1];
	for(let i=0;i<5;i++){
		let col = document.createElement("col");
		col.width = (colWidths[i] * 10) + "%";
		colgroup.append(col);
	}
	
	
	table.append(colgroup);
	
	
	let tbody = document.createElement("tbody");
	
	
	let tr1 = document.createElement("tr");
	let td1_1 = document.createElement("td");
	td1_1.innerText = "Review Conclusion :";
	td1_1.style["text-align"] = "left";
	td1_1.style["font-weight"] = "bold";
	let td1_2 = document.createElement("td");
	td1_2.innerText = drnPrint.val.drn_info.review_conclusion;
	td1_2.style["text-align"] = "left";
	let td1_3 = document.createElement("td");
	td1_3.colSpan = "3";
	tr1.append(td1_1);
	tr1.append(td1_2);
	tr1.append(td1_3);
	tbody.append(tr1);
	
	
	let tr2 = document.createElement("tr");
	let td2_1 = document.createElement("td");
	td2_1.colSpan = "2";
	td2_1.rowSpan = "3";
	let td2_2 = document.createElement("td");
	td2_2.innerText = "Design";
	td2_2.style["border"] = "1px solid";
	let td2_3 = document.createElement("td");
	td2_3.innerText = "Reviewed";
	td2_3.style["border"] = "1px solid";
	let td2_4 = document.createElement("td");
	td2_4.innerText = "Approved";
	td2_4.style["border"] = "1px solid";
	tr2.append(td2_1);
	tr2.append(td2_2);
	tr2.append(td2_3);
	tr2.append(td2_4);
	tbody.append(tr2);
	
	drnPrint.val.approval
	let tr3 = document.createElement("tr");
	let td3_1 = document.createElement("td");
	td3_1.style["border"] = "1px solid";
	td3_1.innerText = drnPrint.val.approval.designed;
	let td3_2 = document.createElement("td");
	td3_2.style["border"] = "1px solid";
	td3_2.innerText = drnPrint.val.approval.reviewed;
	let td3_3 = document.createElement("td");
	td3_3.style["border"] = "1px solid";
	td3_3.innerText = drnPrint.val.approval.approved
	tr3.append(td3_1);
	tr3.append(td3_2);
	tr3.append(td3_3);
	tbody.append(tr3);
	
	
	
	let approval_status = drnPrint.val.drn_info.drn_approval_status;
	let tr4 = document.createElement("tr");
	// 디자인 싸인
	let td4_1 = document.createElement("td");
	td4_1.style["border"] = "1px solid black";
	td4_1.style["font-size"] = "20px";
	td4_1.style["font-weight"] = "bold";
	td4_1.style["height"] = "80px";
	if(approval_status == 'R'){
		td4_1.innerText = "반려됨";
		td4_1.style["color"] = "red";
	}
	else if(drnPrint.val.approval.dgn_sfile){
		td4_1.innerHTML = '<img src="/getSignImage.do?sfile_nm='+drnPrint.val.approval.dgn_sfile+'">';
	}
	else{
		td4_1.innerText = drnPrint.val.approval.designed;
	}
		
	
	// 리뷰드 싸인
	let td4_2 = document.createElement("td");
	td4_2.style["border"] = "1px solid black";
	td4_2.style["font-size"] = "30px";
	td4_2.style["font-weight"] = "bold";
	td4_2.style["height"] = "80px";
	if(approval_status == 'R'){
		td4_2.innerText = "반려됨";
		td4_2.style["color"] = "red";
	}
	else if(approval_status == "1" || approval_status == "C"){
		if(drnPrint.val.approval.rev_sfile){
			td4_2.innerHTML = '<img src="/getSignImage.do?sfile_nm='+drnPrint.val.approval.rev_sfile+'">';
		}else{
			td4_2.innerText = drnPrint.val.approval.reviewed;
		}
	}
	
	// approved 싸인
	let td4_3 = document.createElement("td");
	td4_3.style["border"] = "1px solid black";
	td4_3.style["font-size"] = "30px";
	td4_3.style["font-weight"] = "bold";
	td4_3.style["height"] = "80px";
	if(approval_status == 'R'){
		td4_3.innerText = "반려됨";
		td4_3.style["color"] = "red";
	}
	else if(approval_status == "C"){
		if(drnPrint.val.approval.app_sfile){
			td4_3.innerHTML = '<img src="/getSignImage.do?sfile_nm='+drnPrint.val.approval.app_sfile+'">';
		}else{
			td4_3.innerText = drnPrint.val.approval.approved;
		}
	}
	
	
	tr4.append(td4_1);
	tr4.append(td4_2);
	tr4.append(td4_3);
	tbody.append(tr4);
	
	table.append(tbody);
	bottom.append(table);
	$("#page_"+pageCnt).append(bottom);
	addPageNo();
}


function addPageNo(){
	
	let pageNumber = document.createElement("div");
	pageNumber.classList.add("drn_bottom");
	let numberDiv = document.createElement("div");
	numberDiv.classList.add("drn_page");
	pageNumber.append(numberDiv);
	
	console.log(pageNumber);
	
	$("#page_"+pageCnt)[0].append(pageNumber);	
}

function pageNumbering(){
	let tmpPageNumber = $(".drn_page");
	for(let i=0;i<tmpPageNumber.length;i++){
		tmpPageNumber[i].innerText = (i+1) + " of " + tmpPageNumber.length;
	}
}

function print2Pdf(){
	html2canvas($('#drnPrint_page')[0]).then(function(canvas) { // 저장 영역 div id
		
	    // 캔버스를 이미지로 변환
	    var imgData = canvas.toDataURL('image/png');
		     
	    var imgWidth = 190; // 이미지 가로 길이(mm) / A4 기준 210mm
	    var pageHeight = imgWidth * 1.414;  // 출력 페이지 세로 길이 계산 A4 기준
	    var imgHeight = canvas.height * imgWidth / canvas.width;
	    var heightLeft = imgHeight;
	    var margin = 10; // 출력 페이지 여백설정
	    var doc = new jsPDF('p', 'mm');
	    var position = 0;
	       
	    // 첫 페이지 출력
	    doc.addImage(imgData, 'PNG', margin, position, imgWidth, imgHeight);
	    heightLeft -= pageHeight;
	         
	    // 한 페이지 이상일 경우 루프 돌면서 출력
	    while (heightLeft >= 20) {
	        position = heightLeft - imgHeight;
	        doc.addPage();
	        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
	        heightLeft -= pageHeight;
	    }
	 
	    // 파일 저장
	    doc.save(drnPrint.val.drn_info.drn_title);
 
    });
}