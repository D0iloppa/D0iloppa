<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:eval expression="@appProperties['server.domain']" var="server_domain"/>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <META http-equiv="Pragma" content="no-cache">
	<link rel="apple-touch-icon" sizes="114x114" href="${pageContext.request.contextPath}/resource/images/apple-touch-icon.png">
	<link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/resource/images/favicon-32x32.png">
	<link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resource/images/favicon-16x16.png">
	<link rel="manifest" href="${pageContext.request.contextPath}/site.webmanifest">
	<link rel="mask-icon" href="${pageContext.request.contextPath}/resource/images/safari-pinned-tab.svg" color="#5bbad5">
	<meta name="msapplication-TileColor" content="#da532c">
	<meta name="theme-color" content="#ffffff">
    <title>도서관리시스템</title>
</head>
<body class="sub">
	<%@include file="./common/header.jsp" %>
    <section ng-app="angularResizable" class="has_menu">
        <div class="content">
            <div class="row" resizable r-flex="true">
            		<%@ include file="leftTreeMenu.jsp" %>
                <section id="rightCont">
                	<%@ include file="rightTabArea.jsp" %>
                	<%@ include file="./popup/engDocumentControl.jsp" %>
                	<%@ include file="./popup/vendorDocumentControl.jsp" %>
                	<%@ include file="./popup/procDocumentControl.jsp" %>
                	<%@ include file="./popup/siteDocumentControl.jsp" %>
                	<%@ include file="./popup/corresDocumentControl.jsp" %>
                	<%@ include file="./popup/generalDocumentControl.jsp" %>
                	<%@ include file="./popup/fileMultiDownload.jsp" %>



                	<%@ include file="./page/notification.jsp" %>


					
					<!-- DRN 상신 MODAL 창 -->
<%-- 					<%@ include file="./popup/drnPopUp.jsp" %> --%>
					
					<%@ include file="./popup/folderAuth.jsp" %>

<input type="hidden" id="server_domain" value="${server_domain}">
<input type="hidden" id="current_server_domain" value="${current_server_domain}">
<input type="hidden" id="organizationUse">
<!-- 도서 Property Or New 팝업창에 넘겨줄 값들 -->
<div>
	<input type="hidden" id="process">
	<input type="hidden" id="popup_new_or_update">
	<input type="hidden" id="vendor_new_or_update">
	<input type="hidden" id="proc_new_or_update">
	<input type="hidden" id="bulk_new_or_update">
	<input type="hidden" id="site_new_or_update">
	<input type="hidden" id="corr_new_or_update">
	<input type="hidden" id="general_new_or_update">
	<input type="hidden" id="doc_type">
	<input type="hidden" id="doc_id">
	<input type="hidden" id="rev_id">
	<input type="hidden" id="trans_ref_doc_id">
	<input type="hidden" id="current_folder_path">
	<input type="hidden" id="DCCorTRA">
	<input type="hidden" id="current_folder_discipline_code">
	<input type="hidden" id="current_folder_discipline_code_id">
	<input type="hidden" id="current_folder_discipline_display">
	<input type="hidden" id="current_folder_id">
	<input type="hidden" id="tr_id">
	<input type="hidden" id="tradd_attach">
	<input type="hidden" id="tradd_doc_no">
	<input type="hidden" id="tradd_rev_no">
	<input type="hidden" id="tradd_title">
	<input type="hidden" id="selectedPrjNo">
	<input type="hidden" id="useDRNYN">
</div>
					<!-- trOutgoing popup-->
                	<%@ include file="./page/trOutgoing.jsp" %>
                	<!-- trOutgoing-->
					<div class="tab_cont">
                	<section ng-app="angularResizable6" id="transmittal_left" class="whiteBg trOutgoing " style="display:none;">
                                    <div class="content">
                                        <div class="row" resizable r-flex="true">
                                            <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                                                <h4 id="TRTitleHeader">TR Outgoing</h4>
                                                <form id="excelUploadForm_TrOutGoing" name="excelUploadForm_TrOutGoing"  enctype="multipart/form-data" method="post" onsubmit="return false;">
                                                
                                                <input type="hidden" name="fileName" id="fileNameTrOut"> 
												<input type="hidden" name="jsonRowDatas" id="jsonRowDatasTrOut">
												<input type="hidden" name="prj_id" id="prj_idTrOut">
												<input type="hidden" name="prj_nm" id="prj_nmTrOut">
												<input type="hidden" name="inout" id="inoutTrOut">
												<input type="hidden" name="tr_status" id="tr_statusTrOut">
												<input type="hidden" name="tr_period" id="tr_periodTrOut">
												<input type="hidden" name="tr_from_search" id="tr_from_searchTrOut">
												<input type="hidden" name="tr_to_search" id="tr_to_searchTrOut">
												<input type="hidden" name="tr_no" id="tr_noTrOut">
												<input type="hidden" name="tr_subject" id="tr_subjectTrOut">
												<input type="hidden" name="purpose" id="purposeTrOut">
												
                                                <div class="btn_wrap">
                                                    <button class="button btn_blue btn_small" id="out_retrieve" onclick="getTrOutSearch()">Retrieve</button>
                                                    <button class="button btn_blue btn_small" id="out_createTR" onclick="createTR()">Create TR</button>
                                                    <button class="button btn_green btn_small" id="out_toexcel" onclick="trOutGoingToExcel()">Excel</button>
                                                </div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
                                                        <tr>
                                                            <th style="width:70px;">Status</th>
                                                            <td>
                                                                <input type="radio" name="out_tr_status" value="checking"><label for="" class="mr10">Checking</label>
                                                                <input type="radio" name="out_tr_status" value="checked"><label for="">Checked</label>
                                                                <input type="radio" name="out_tr_status" value="issued"><label for="" class="mr10">Issued</label>
                                                                <input type="radio" name="out_tr_status" value="all" checked><label for="">All</label>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>Period</th>
                                                            <td>
                                                                <div>
                                                                    <input type="text" id="out_tr_from" class="date wsmallOut">~
                                                                    <input type="text" id="out_tr_to" class="date wsmallOut">
                                                                </div>
                                                                <div>
                                                                    <input type="radio" name="out_tr_period" id="A_Outradio" value="all"><label for="">All</label>
                                                                    <input type="radio" name="out_tr_period" id="W_Outradio" value="oneweek" checked><label for="">One Week</label>
                                                                    <input type="radio" name="out_tr_period" id="M_Outradio" value="onemonth"><label for="">One Month</label>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>TR NO</th>
                                                            <td><input type="text" id="tr_no_search" class="full"></td>
                                                        </tr>
                                                        <tr>
                                                            <th>Subject</th>
                                                            <td><input type="text" id="tr_subject_search" class="full"></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="btn_wrap">
                                                    <button class="button btn_blue btn_small" id="out_incomingreg" onclick="trIncomingReg()">TR Incoming Reg</button>
                                                    <!-- <button class="button btn_blue btn_small" id="out_frompreTR" onclick="createFromPreTrOut()">Create From Pre. TR</button> -->
                                                </div>
                                                <p><span id="trOutgoingSearchLength"></span> record found</p>
                                                <div class="tbl_wrap" id="trOutgoingSearchTbl"></div>
                                            	</form>
                                            </section>
                                            <section id="pop_ed_right">
                                                <div class="pg_top">
                                                    <div class="left">
                                                        <button class="button btn_blue" id="out_save" onclick="outgoingSave()">
                                                            Save
                                                        </button>
                                                        <button class="button btn_orange" id="out_issue" onclick="out_issue()">
                                                            Issue
                                                        </button>
                                                        <button class="button btn_orange" id="out_issue_cancel">
                                                            Iss. Cancel
                                                        </button>
                                                        <button class="button btn_orange" id="out_email">
                                                            E-Mailing
                                                        </button>
                                                        <button class="button btn_blue" id="out_delete">Delete</button>
                                                        <span id="tr_email_send_info"></span>
                                                    </div>
                                                </div>
												<div style="color:red;">
													※ Transmittal No를 입력하지 않을 경우 설정된 값에 따라 자동 발번 됩니다.
												</div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
													  <colgroup>
													    <col width="110px"/>
													    <col/>
													    <col width="110px"/>
													    <col/>
													  </colgroup>
                                                        <tr>
                                                            <th>
                                                                Transmittal No
                                                            </th>
                                                            <td>
                                                                <input type="text" id="out_tr_no" class="full" disabled>
                                                            <td colspan="2">
                                                                <span>
                                                                    <input type="checkbox" id="out_check"><label for="">checked</label>
                                                                </span>
                                                                <input type="text" disabled id="out_chk_date"><input type="text" disabled id="out_tr_status">
                                                            	<span id="tr_cover_FileInfo"></span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="required">
                                                                Subject <i>required</i>
                                                            </th>
                                                            <td>
                                                                <input type="text" class="full" id="out_tr_subject">
                                                            </td>
                                                            <th>
                                                                ITransmittal No
                                                            </th>
                                                            <td>
                                                                <input type="text" id="out_itr_no" class="full" disabled>

                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th class="required">
                                                                Discipline <i>required</i>
                                                            </th>
                                                            <td>
                                                                <select name="" class="full" id="out_discipline_code_id" onchange="disciplineCheck()"></select>
                                                            </td>
                                                            <th class="required">
                                                                Issue Purpose <i>required</i>
                                                            </th>
                                                            <td>
                                                                <select name="" class="full" id="out_tr_issuepur_code_id"></select>
                                                            </td>
                                                            
                                                            <!-- <td rowspan="6" class="tblInTab" style="width:35%; border-left:solid 1px #ddd;">
                                                                <h5>Internal Distribution</h5>
                                                                <div class="id_tab">
                                                                    <div class="id_tab_tit">
                                                                        <ul>
                                                                            <li><a href="#idtab01" class="on">Head Office</a></li>
                                                                            <li><a href="#idtab02">Site</a></li>
                                                                        </ul>
                                                                    </div>
                                                                    <div class="id_tab_cont">
                                                                        <div class="box on" id="idtab01">
                                                                            <table>
                                                                                <tr>
                                                                                    <th style="width:30px;">ACT</th>
                                                                                    <th style="width:30px;">CC</th>
                                                                                    <th>DISIPLINE</th>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td><input type="checkbox"></td>
                                                                                    <td><input type="checkbox"></td>
                                                                                    <td>Boiler-Basic</td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                        <div class="box" id="idtab02">
                                                                            <table>
                                                                                <tr>
                                                                                    <th style="width:30px;">ACT222</th>
                                                                                    <th style="width:30px;">CC</th>
                                                                                    <th>DISIPLINE</th>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td><input type="checkbox"></td>
                                                                                    <td><input type="checkbox"></td>
                                                                                    <td>Boiler-Basic</td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td> -->
                                                        </tr>
                                                        <tr>
                                                            <th class="required">
                                                                Send Date <i>required</i>
                                                            </th>
                                                            <td>
                                                                <input type="text" class="date full" id="out_send_date">
                                                            </td>
                                                            <th class="required">
                                                                Required Date <i>required</i>
                                                            </th>
                                                            <td>
                                                                <input type="text" class="date full" id="out_req_date">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Creator
                                                            </th>
                                                            <td>
                                                                <input type="text" class="half" id="out_tr_writer_name" disabled>
                                                                <input type="text" class="half" id="out_tr_write_date" disabled>
                                                                <input type="hidden" id="out_tr_writer_id">
                                                            </td>
                                                            <th>
                                                                Modifier
                                                            </th>
                                                            <td>
                                                                <input type="text" class="half" id="out_tr_modifier_name" disabled>
                                                                <input type="text" class="half" id="out_tr_modify_date" disabled>
                                                                <input type="hidden" id="out_tr_modifier_id">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Remark
                                                            </th>
                                                            <td colspan="3">
                                                            	<textarea class="full" rows="30" id="out_tr_remark"></textarea>
                                                                <!-- <input type="text" class="full" id="out_tr_remark"> -->
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div style="color:red;">
													<!-- ※ Send Date는 최초 이메일 발송시 발송일자로 업데이트 됩니다. <br> -->
													※ Require Date는 기본적으로 Send Date+15일로 정의되어 있습니다.
												</div>
                                                <div class="pg_top">
                                                    <div class="left">
                                                        <!-- <span>1 record found</span> -->
                                                    </div>
                                                    <div class="right">
                                                        <button class="button btn_blue" id="out_add_doc">Add Doc</button>
                                                        <button class="button btn_blue" id="out_del_doc" onclick="out_selectedDeleteDoc()">Del Doc</button>
                                                    </div>
                                                    <div class="tbl_wrap icon_tbl" id="trOutSelectedDoc"></div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
                                    <input type="hidden" id="out_selectTRID">
                                </section>
                           </div>
                           
                           
                           
                           
                           
                           
						<!-- trIncoming popup-->
	                	<%@ include file="./page/trIncoming.jsp" %>
                        <!-- trIncoming -->
                        <div class="tab_cont">
                            <div id="tab1" class="tab_div on">
                                <section ng-app="angularResizable6" id="transmittal_left_incoming" class="whiteBg" style="display:none;">
                                    <div class="content">
                                        <div class="row" resizable r-flex="true">
                                            <section id="pop_ed_left" resizable r-directions="['right']" r-flex="true" class="p_ed_left">
                                                <h4 id="TRInTitleHeader">TR Incoming</h4>
                                                <form id="excelUploadForm_TrInComing" name="excelUploadForm_TrInComing"  enctype="multipart/form-data" method="post" onsubmit="return false;">
                                                
                                                <input type="hidden" name="fileName" id="fileNameTrIn"> 
												<input type="hidden" name="jsonRowDatas" id="jsonRowDatasTrIn">
												<input type="hidden" name="prj_id" id="prj_idTrIn">
												<input type="hidden" name="prj_nm" id="prj_nmTrIn">
												<input type="hidden" name="inout" id="inoutTrIn">
												<input type="hidden" name="tr_status" id="tr_statusTrIn">
												<input type="hidden" name="tr_period" id="tr_periodTrIn">
												<input type="hidden" name="tr_from_search" id="tr_from_searchTrIn">
												<input type="hidden" name="tr_to_search" id="tr_to_searchTrIn">
												<input type="hidden" name="tr_no" id="tr_noTrIn">
												<input type="hidden" name="tr_subject" id="tr_subjectTrIn">
												<input type="hidden" name="purpose" id="purposeTrIn">
                                                
                                                <div class="btn_wrap">
                                                    <button class="button btn_blue btn_small" id="in_retrieve" onclick="getTrInSearch()">Retrieve</button>
                                                    <button class="button btn_blue btn_small" id="in_createTR" onclick="createTRIn()">Create TR</button>
                                                    <button class="button btn_green btn_small" id="in_toexcel" onclick="trInComingToExcel()">Excel</button>
                                                </div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
                                                        <tr>
                                                            <th style="width:70px;">Period</th>
                                                            <td>
                                                                <input type="text" id="in_tr_from" class="date wsmallIn">~<input type="text" id="in_tr_to" class="date wsmallIn">
                                                                <div>
                                                                    <input type="radio" name="in_tr_period" id="A_Inradio" value="all"><label for="">All</label>
                                                                    <input type="radio" name="in_tr_period" id="W_Inradio" value="oneweek"><label for="">One Week</label>
                                                                    <input type="radio" name="in_tr_period" id="M_Inradio" value="onemonth" checked><label for="">One Month</label>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>TRNO</th>
                                                            <td><input type="text" id="tr_in_no_search" class="full"></td>
                                                        </tr>
                                                        <tr>
                                                            <th>Subject</th>
                                                            <td><input type="text" id="tr_in_subject_search" class="full"></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <p><span id="trIncomingSearchLength"></span> record found</p>
                                                <div class="tbl_wrap" id="trIncomingSearchTbl"></div>
                                            </form>
                                            </section>
                                            <section id="pop_ed_right">
                                                <div class="pg_top">
                                                    <div class="left">
                                                        <button class="button btn_blue" id="in_save" onclick="incomingSave()">Save</button>
                                                        <button class="button btn_blue" id="in_delete">Delete</button>
                                                    </div>
                                                </div>
												<div style="color:red;">
													※ Transmittal No를 입력하지 않을 경우 설정된 값에 따라 자동 발번 됩니다.
												</div>
                                                <div class="tbl_wrap">
                                                    <table class="write">
													  <colgroup>
													    <col width="110px"/>
													    <col/>
													    <col width="110px"/>
													    <col/>
													    <col width="110px"/>
													    <col/>
													  </colgroup>
                                                        <tr>
                                                            <th>
                                                                Transmittal No
                                                            </th>
                                                            <td>
                                                                <input type="text" id="in_tr_no" class="full">
                                                            </td>
                                                            <th class="required">
                                                                Subject <i>required</i>
                                                            </th>
                                                            <td colspan="3">
                                                                <input type="text" class="full" id="in_tr_subject">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Comment File
                                                            </th>
                                                            <td colspan="5">
															<form id="trIncomingForm" style="display:flex;" name="trIncomingForm"  enctype="multipart/form-data" method="post" onsubmit="return false;">
										                    	<span id="tr_incoming_uploadFileInfo" class="property_attach"></span>
										                        <span class="filebox">
										                        	<input type="text" id="trIncoming_uploadFileDate" disabled style="width: 190px;">
										                     	   	<input type="file" name="trIncoming_File" id="tr_incoming_filename" class="upload-hidden">
											                        <label for="tr_incoming_filename" class="button btn_blue" id="tr_incoming_attach">Attach</label>
										                    	</span>
										                    <!-- <span class="filebox">
										                        <input class="upload-name" value="파일선택" id="tr_incoming_attachfilename" disabled="disabled">
										
										                        <label for="tr_incoming_filename" class="button btn_blue" id="tr_incoming_attach">File</label>
										                        <input type="file" name="trIncoming_File" id="tr_incoming_filename" class="upload-hidden">
										                    </span>
										                    <span id="tr_incoming_uploadFileInfo"></span> -->
					                                    	</form>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            
                                                        </tr>
                                                        <tr>
                                                            <th class="required">
                                                                Received Date <i>required</i>
                                                            </th>
                                                            <td style="border-right:none;">
                                                                <input type="text" class="date full" id="in_received_date">
                                                            </td>
                                                            <!-- <td rowspan="3" class="inDis" colspan="2" style="border-left:solid 1px #ddd;">
                                                                <h5>Internal Distribution</h5>
                                                                <div>
                                                                    <span id="trInDiscipline">
                                                                    </span>
                                                                </div>
                                                            </td> -->
                                                            <th>
                                                                Creator
                                                            </th>
                                                            <td>
                                                                <input type="text" class="half" id="in_tr_writer_name" disabled>
                                                                <input type="text" class="half" id="in_tr_write_date" disabled>
                                                                <input type="hidden" id="in_tr_writer_id">
                                                            </td>
                                                            <th>
                                                                Modifier
                                                            </th>
                                                            <td>
                                                                <input type="text" class="half" id="in_tr_modifier_name" disabled>
                                                                <input type="text" class="half" id="in_tr_modify_date" disabled>
                                                                <input type="hidden" id="in_tr_modifier_id">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Note
                                                            </th>
                                                            <td colspan="5">
                                                            	<textarea class="full" rows="30" id="in_note"></textarea>
                                                                <!-- <input type="text" class="full" id="in_note"> -->
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                                <div class="pg_top">
                                                    <div class="left">
                                                        <!-- <span>1 record found</span> -->
                                                    </div>
                                                    <div class="right">
                                                        <button class="button btn_blue" id="in_add_doc">Add Doc</button>
                                                        <button class="button btn_blue" id="in_del_doc" onclick="in_selectedDeleteDoc()">Del Doc</button>
                                                    </div>
                                                    <div class="tbl_wrap icon_tbl" id="trInSelectedDoc"></div>
                                                </div>
                                    			<input type="hidden" id="in_selectTRID">
                                            </section>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                </section>
            </div>
        </div>
    </section>
</body>
</html>