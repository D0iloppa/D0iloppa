<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <META http-equiv="Pragma" content="no-cache">
    <title>도서관리시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/jquery-ui.css">
    <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/font.css"> --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/default.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
	<link rel="apple-touch-icon" sizes="114x114" href="${pageContext.request.contextPath}/resource/images/apple-touch-icon.png">
	<link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/resource/images/favicon-32x32.png">
	<link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resource/images/favicon-16x16.png">
	<link rel="manifest" href="${pageContext.request.contextPath}/site.webmanifest">
	<link rel="mask-icon" href="${pageContext.request.contextPath}/resource/images/safari-pinned-tab.svg" color="#5bbad5">
	<meta name="msapplication-TileColor" content="#da532c">
	<meta name="theme-color" content="#ffffff">
    <!--jstree-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css">
    <script src="${pageContext.request.contextPath}/resource/js/jquery-3.4.1.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery-ui.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jstree.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/script.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery_dialogextend.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/jquery_dialogextend_min.js"></script>
    
    <script src="${pageContext.request.contextPath}/resource/js/login/login.js"></script>
</head>
<%
    Cookie[] c = request.getCookies();
    String cookieVal="";
    if(c!=null){
        for(Cookie i:c){
            if(i.getName().equals("saveId")){
                cookieVal=i.getValue();
            }
        }
    }
    
%>
<body class="login">
    <div class="login_box">
        <div class="login_left">
            <div class="top_logo">
                <img src="${pageContext.request.contextPath}/resource/images/login_logo.png" alt="">
            </div>
            <!-- <h1>Document &amp; Drawing <br>Control System</h1> -->
            <h1><strong>P</strong>lant document &amp; <strong>E</strong>ngineering<br>progress cont<strong>RO</strong>l <strong>S</strong>ystem</h1>
            <div class="logo"><img src="${pageContext.request.contextPath}/resource/images/login_hd_logo.png" alt=""></div>
        </div>
        <div class="login_right">
            <h2>Sign in</h2>
			<form id="loginForm" action="loginPost.do" method="post">
            <div class="lg_form">
                <ul>
                    <li><input type="text" class="id" id="userid" name="user_id" value="<%=cookieVal !="" ? cookieVal : "" %>"></li>
                    <li><input type="password" class="pw" id="password" name="passwd"></li>
                </ul>
                <div class="login_fnc">
                    <div class="lt"><input type="checkbox" id="saveId" name="saveId" <%=cookieVal!=""?"checked" : ""%>><label for="saveId">Save ID</label></div>
                    <div class="rt">
                        <a href="#" class="chg_pw">Change Password</a>
                    </div>
                </div>
                <p style="color:red;" id="userinfonoexist"></p>
                <c:choose>
	                <c:when test="${FIRST_LOGIN_YN eq 'Y'}">
	                <input type="hidden" id="FIRST_LOGIN_YN" value="Y">
	                <input type="hidden" id="USER_ID" value="${userinfo.user_id}">
	                </c:when>
	                <c:otherwise>
	                <input type="hidden" id="FIRST_LOGIN_YN" value="NOTLOGIN">
	                </c:otherwise>
                </c:choose>
                <div class="btn_wrap">
                    <a onclick="loginCheck();" class="btn_signin">
                        Sign in
                    </a>
                </div>
            </div>
            </form>
            <div class="login_board">
                <h3>Notice</h3>
                <div class="tbl_wrap">
                	<form id="loginNoticeForm" name="" method="post">
                    <table class="login_list">
                        <tr>
                            <th style="width:50px">No</th>
                            <th class="pc" style="width:100px">Writer</th>
                            <th class="pc" style="width:50px">Hits</th>
                            <th style="width:100px">Date</th>
                            <th>Title</th>
                        </tr>
                       	<tbody id="loginPubBbs">
                        </tbody>
					<%-- <c:forEach var="i" items="${getPubBbsContents}" varStatus="vs" begin="0" end="3">
                        <tr>
                            <td>${fn:length(getPubBbsContents) - vs.index}</td>
                            <td class="pc">${i.reg_id}(${i.user_ko_nm})</td>
                            <td class="pc">5</td>
                            <td>${i.reg_date}</td>
                            <td>${i.title}</td>
                        </tr>
					</c:forEach> --%>
                    </table>
                    </form>
                </div>
                <span style="color: red; cursor: pointer; float: right; font-weight: bold;" onclick="popInform();">
                	개인정보처리방침
                </span>
            </div>
        </div>
    </div>
    <div id="changePw" class="dialog" title="Change Password">
        <div class="pop_tbl">
            <table class="write">
                <tr>
                    <th style="width:130px;">User ID</th>
                    <td><input type="text" id="CP_USER_ID"></td>
                </tr>
                <tr>
                    <th>Current Password</th>
                    <td><input type="password" id="CP_CURRENT_PW"></td>
                </tr>
                <tr>
                    <th>New Password</th>
                    <td><input type="password" id="CP_NEW_PW" onchange="pwdCheck()"><input type="hidden" id="pass_reg"><p id="pwdCheckWarning" class="info red"></p></td>
                </tr>
                <tr>
                    <th>Confirm Password</th>
                    <td><input type="password" id="CP_NEW_PW_CONFIRM" onchange="pwdConfirmCheck()"><p id="pwdConfirmWarning" class="info red"></p></td>
                </tr>
            </table>
        </div>
        <p class="red" style="margin-bottom: 15px;">
        	* 비밀번호는 10~20자의 영문 대, 소문자, 숫자, 특수문자 중 3개를 조합하여 사용해야 합니다. <br>
            * 사용 가능 특수문자 : ` ~ ! @ # $ % ^ * ( ) - _ =
		</p>
        <div class="pop_btns">
            <button class="p_btn" onclick="resetPwd()">Reset Password</button>
            <button class="p_btn" id="PW_CHANGE">Change</button>
            <button class="p_btn pop_close">Close</button>
        </div>
    </div>
    
<!-- login alert창을 dialog로 바꿈 -->
<div class="dialog pop_alertDialog" title="" style="max-width:1000px;">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:11px;margin:15px;" id="alertText">
        </div>
            <div class="right" style="margin:15px; float: right;">
                <button class="button btn_purple" onclick="alertDialogOk()">확인</button>
            </div>
    </div>
</div>
    
<div class="dialog pop_detailP2" title="공용 게시판 상세 페이지" style="max-width:1000px;">
    <div class="pop_box">
	        					<div class="sec">
	                                    <div class="tbl_wrap">
	                                        <table class="write">
	                                            <colgroup>
	                                                <col style="width:100px">
	                                                <col style="*">
	                                            </colgroup>
	                                            <tr>
	                                                <th>Title</th>
	                                                <td colspan="5"><input type="text" id="title2" style="width: 100%;" readonly></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Writer</th>
	                                                <td><input type="text" id="writer2" style="width: 100%;" readonly></td>
	                                                <th>Create Date</th>
	                                                <td><input type="text" id="createdate2" style="width: 100%;"></td>
	                                                <th>Hits</th>
	                                                <td><input type="text" id="hits2" style="width: 100%;" readonly></td>
	                                            </tr>
	                                            <tr>
	                                                <th>Contents</th>
	<!--                                                 <td colspan="5"><textarea rows="500" id="contents" cols="500" style="max-height: 300px;" readonly></textarea></td> -->
	                                                <td colspan="5"><div id="contents2" style="min-height: 300px;"></div></td>
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
														<div id="detailViewFileWrapN_Login">
																
														</div>
													</div>
	                                                </td>
	                                            </tr>
	                                        </table>
	                                    </div>
	                                </div>
	                                <div class="line">
		                                <div class="right r_array">
											<button class="button btn_blue" onclick="cancledetail()">취소</button>
										</div>
									</div>
	</div>    
</div>
    
    <div class="dialog pop_popInform" title="개인정보처리방침" style="max-width:1400px;">
    	<div class="pop_box" style="">
		<p align="center" style="line-height:1.2;margin-top:0;margin:20px 0px 20px 0px;"><font style="font-family:NanumGothic;font-size:18pt;font-weight:bold">개인정보처리방침</font></p>
			<div style="max-width:1250px;margin: 0 auto; text-align:center;border: 2px solid #bbb; padding:2%;margin-bottom:20px;">
				 <div class="policy"> 
				
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">현대중공업파워시스템은 여러분의 작은 소리에도 귀 기울일 수 있는 함께 나누는 기업이 되겠습니다.</font></p>
				<p align="justify" style="line-height:18pt;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">'현대중공업주식회사'(이하 '회사'라 한다)은 정보주체의 개인정보를 중요시하며, [개인정보보호법], [정보통신망 이용촉진 및 정보보호 등에 관한 법률], [통신비밀보호법], [전기통신사업법] 등 관련 법령상의 개인정보보호규정을 준수하고, 관련 법령의 규정에 따라 수집, 보유 및 처리하는 개인정보를 업무의 적절한 수행과 정보주체의 권익을 보호하기 위해 적법하고 적정하게 취급할 것입니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">또한 회사는 관련 법령에 따라 정보주체의 개인정보 보호 및 권익을 보호하고 개인정보와 관련한 정보주체의 고충을 원활하게 처리할 수 있도록 다음과 같은 개인정보처리방침을 두고 있으며, 개인정보처리방침을 개정하는 경우에는 웹사이트 공지사항(또는 개별공지)을 통하여 공지할 것입니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">본 방침은 2020년 06월 16일부터 시행됩니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">수집하는 개인정보의 항목</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">회사는 외부 사용자 ID 발급 및 관리를 위해 아래와 같은 개인정보를 수집하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt">1. 수집하는 개인정보의 항목 </font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;- 필수항목 : 회사명, 성명, 직급, 이메일, 연락처(회사전화번호 등)</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;- 선택항목 : 수집정보 없음</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;- 서비스 이용과정에서 IP Address, 쿠키, 방문일시, 서비스 이용기록, 불량 이용기록 등이 자동으로 수집될 수 있습니다.</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;- 수집방법 : ID신청시 개인정보활용동의서 징구 및 접수(이메일) 및 내부심사 후 ID발급</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보 취급의 위탁에 관한 사항</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">회사는 수집한 개인정보를 다음의 목적을 위해 활용하며, 개인정보의 제3자에게 제공하지 않습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 고객 또는 협력업체의 시스템 사용(열람)을 통합 프로젝트 협업</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보의 보유 및 이용기간</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다.<br> 단, 다음의 정보에 대해서는 아래의 이유로 명시한 기간 동안 보존합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 보존 항목 : 회사명, 성명, 이름, 이메일, 연락처(회사전화번호 등)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 보존 근거 : 회사 내부 관리 규정</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 보존 기간 : 부정이용 방지를 위한 부정이용 기록 자료 (2년), 회사 공사 수행 이력관리 및 공사수행 적합여부 판별 (영구)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보 열람, 정정 및 삭제 처리</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">이용자는 언제든지 회사가 가지고 있는 자신의 개인정보에 대해 열람 및 오류정정, 삭제신청을 요구할 수 있으며, 회사는 이에 대해 필요한 조치를 취할 의무를 집니다. 이용자가 오류의 정정을 요구한 경우에는 그 오류를 정정할 때까지 당해 개인정보를 이용하지 않습니다. 또한 삭제 요청 후 삭제 처리된 고객정보는 더 이상 보관하지 않습니다. 삭제 요청 방법은 E-mail, Fax, 전화로 요청할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보의 파기 절차 및 방법</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체없이 파기합니다. 파기절차 및 방법은 다음과 같습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:24pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:-26pt;margin-left:52pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">*</font><font style="font-family:NanumGothic;font-size:7pt;font-weight:bold"></font><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">파기 절차</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">입력된 정보는 목적이 달성된 후 내부 방침 및 기타 관련 법령에 의한 정보보호 사유에 따라(보유 및 이용기간 참조) 일정 기간 저장된 후 파기됩니다.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 일정기간 저장된 개인정보는 법률에 의한 경우가 아니고서는 보유한 이외의 다른 목적으로 이용되지 않습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:24pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">*파기 방법</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">- 전자적 파일형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">- 전자적 파일 형태 외의 기록물, 인쇄물, 서면, 그 밖의 기록매체인 경우에는 파쇄 또는 소각을 통하여 파기합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보의 안전성 확보조치</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">회사는 정보주체의 개인정보를 취급함에 있어 개인정보가 분실, 도난, 누출, 변조 또는 훼손되지 않도록 안전성 확보를 위하여 다음과 같이 조치를 하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:-26pt;margin-left:52pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">가.</font><font style="font-family:NanumGothic;font-size:7pt;font-weight:bold">&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">내부관리계획의 수립 및 시행</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">회사는 ‘개인정보의 안전성 확보조치 기준’에 의거하여 내부관리계획을 수립하여 시행합니다</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:24pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">나. 개인정보 취급 담당자의 최소화 및 교육</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">회사의 개인정보관련 취급직원은 담당자에 한정시키고 있고 이를 위한 별도의 비밀번호를 부여하여 정기적으로 갱신하고 있으며,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 담당자에 대한 수시교육을 통하여 회사의 개인정보처리방침의 준수를 항상 강조하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">다. 개인정보에 대한 접근 제한</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여, 변경, 말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 개인정보취급자가 정보통신망을 통해 외부에서 개인정보처리시스템에 접속하는 경우에는 가상사설망(VPN : Virtual Private Network)을 이용하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">라. 접속기록의 보관 및 위·변조 방지</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">개인정보처리시스템에 접속한 기록(웹 로그 등)을 최소 6개월 이상 보관, 관리하고 있으며,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 접속기록이 위·변조 및 도난 분실되지 않도록 보안기능을 사용하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">마. 개인정보의 암호화</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">정보주체의 개인정보는 암호화되어 저장 및 관리되고 있습니다.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 또한 중요한 데이터는 저장 및 전송 시 암호화하여 사용하는 등의 별도 보안기능을 사용하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">바. 해킹 등에 대비한 대책</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">1) 회사는 해킹이나 컴퓨터 바이러스 등에 의해 회원의 개인정보가 유출되거나 훼손되는 것을 막기 위해 최선을 다하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">2) 개인정보의 훼손에 대비해서 자료를 수시로 백업하고 있고, 최신 백신프로그램을 이용하여 정보주체들의 개인정보나<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 자료가 누출되거나 손상되지 않도록 방지 하고 있으며, 암호화 통신 등을 통하여 네트워크상에서 개인정보를 안전하게 전송할 수 있도록 하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">3) 그리고 침입차단시스템을 이용하여 외부로부터의 무단접근을 통제하고 있으며,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 개인정보의 확인 및 변경도 비밀번호를 알고 있는 정보주체 본인에 의해서만 가능합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">사. 비인가자에 대한 출입통제</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">개인정보를 보관하고 있는 개인정보시스템의 물리적 보관장소를 별도로 두고 이에 대해 출입통제 절차를 수립, 운영하고 있습니다</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">아. 비밀번호 암호화</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">비밀번호는 암호화되어 저장 및 관리되고 있어 정보주체만이 알고 있으며,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 담당자에 대한 수시교육을 통하여 회사의 개인정보처리방침의 준수를 항상 강조하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">자. 개인정보보호 조직의 운영</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">사내 개인정보보호조직을 통하여 회사의 개인정보처리방침의 이행사항 및 담당자의 준수 여부를<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 확인하여 문제가 발견될 경우 즉시 수정하고 바로 잡을 수 있도록 노력하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">단, 정보주체 본인의 부주의나 인터넷상의 문제로 ID, 비밀번호 등 개인정보가<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 유출되어 발생한 문제에 대해서는회사는 일체의 책임을 지지 않습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p><!-- 
				<p style="margin-top:0;margin-bottom:0"><table cellpadding="0" cellspacing="0" height="61" width="618" border="0" style="border-collapse:collapse;border:none;margin:0 0 0 70">
				<tbody><tr height="21">
				<td valign="top" width="277" height="21" style="border-top:solid black 1pt;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">수 탁 자</font></p></td>
				<td valign="top" width="341" style="border-top:solid black 1pt;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">위탁 사무명</font></p></td></tr>
				<tr height="20">
				<td valign="top" width="277" height="20" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"> 에이치엔 </font></p></td>
				<td valign="top" width="341" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">시스템 유지 보수</font></p></td></tr>
				<tr height="20">
				<td valign="top" width="277" height="20" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p></td>
				<td valign="top" width="341" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p></td></tr></tbody></table></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p> -->
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보 자동 수집 장치의 설치 · 운영 및 그 거부에 관한 사항</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">회사는 현재 쿠키 등 서비스 이용 시 자동으로 생성되는 개인정보를 수집하는 장치를 운영하지 않습니다.<br> 다만 향후 서비스 제공에 필요한 경우 자동수집장치를 설치 · 운영할 수 있으며, 이 경우 정보주체는 쿠키 설치에 대한 선택권을 가질 수 있거나,<br> 모든 쿠키의 저장을 거부 할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:24pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">가. 쿠키란?</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">회사는 개인화되고 맞춤화된 서비스를 제공하기 위해서 이용자의 정보를 저장하고 수시로 불러오는 '쿠키(cookie)'를 사용합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">쿠키는 웹사이트를 운영하는데 이용되는 서버가 이용자의 브라우저에게 보내는 아주 작은 텍스트 파일로 이용자 컴퓨터의 하드디스크에 저장됩니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">정보주체가 웹 사이트에 방문할 경우 웹 사이트 서버는 정보주체의 하드 디스크에 저장되어 있는 쿠키의 내용을 읽어</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">정보주체의 환경설정을 유지하고 맞춤화된 서비스를 제공하기 위해 이용됩니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">쿠키는 개인을 식별하는 정보를 자동적/능동적으로 수집하지 않으며, 정보주체는 언제든지 이러한 쿠키의 저장을 거부하거나 삭제할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">나. 회사의 쿠키 사용 목적</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">현재 회사는 쿠키를 사용하지 않고 있습니다. 다만, 향후에 정보주체의 환경설정을 유지하고,</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:48pt"><font style="font-family:NanumGothic;font-size:12pt">맞춤화된 서비스 등을 제공하기 위해 사용 될 수도 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:-53pt;margin-left:53pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p style="margin-top:0;margin-bottom:0">
				<!-- <table cellpadding="0" cellspacing="0" height="374" width="640" border="0" style="border-collapse:collapse;border:none;margin:0 0 0 70">
				<tbody><tr height="31">
				<td width="278" height="31" style="border-top:solid black 1pt;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">구분</font></p></td>
				<td width="268" style="border-top:solid black 1pt;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">보관사유</font></p></td>
				<td width="94" style="border-top:solid black 1pt;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">보존기간</font></p></td></tr>
				<tr height="61">
				<td width="278" height="61" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 계약 또는 청약철회, 대금결제,</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">재화 등의 공급 기록</font></p></td>
				<td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 전자상거래 등에서의 소비자</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">보호에 관한 법률</font></p></td>
				<td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">5년</font></p></td></tr>
				<tr height="61">
				<td width="278" height="61" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 소비자의 불만 또는 분쟁처리에</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">관한 기록</font></p></td>
				<td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 전자상거래 등에서의 소비자</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">보호에 관한 법률</font></p></td>
				<td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">3년</font></p></td></tr>
				<tr height="59">
				<td width="278" height="59" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 표시, 광고에 관한 기록</font></p></td>
				<td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 전자상거래 등에서의 소비자</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">보호에 관한 법률</font></p></td>
				<td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">6개월</font></p></td></tr>
				<tr height="78">
				<td width="278" height="78" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 가입자 전기통신일시, 개시 및</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:11pt"><font style="font-family:NanumGothic;font-size:12pt">종료시간, 상대방 가입자번호,</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:11pt"><font style="font-family:NanumGothic;font-size:12pt">사용도수, 발신기지국 위치추적</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:11pt"><font style="font-family:NanumGothic;font-size:12pt">자료</font></p></td>
				<td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 정보통신 이용촉진 및 정보보호</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">등에 관한 법률</font></p></td>
				<td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">1년</font></p></td></tr>
				<tr height="44">
				<td width="278" height="44" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 컴퓨터통신, 인터넷 로그기록</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">자료, 접속지 추적자료</font></p></td>
				<td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 정보통신 이용촉진 및 정보보호</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">등에 관한 법률</font></p></td>
				<td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">3개월</font></p></td></tr>
				<tr height="40">
				<td width="278" height="40" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 본인확인에 관한 기록</font></p></td>
				<td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 정보통신 이용촉진 및 정보보호</font></p><p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">등에 관한 법률</font></p></td>
				<td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">6개월</font></p></td></tr>
                <tr height="44">
                     <td width="278" height="44" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 사고 발생시 재해자에 관한 기록</font></p></td>
                     <td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                        <p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 산업안전보건법 등에서의</font></p>
                        <p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">산업재해 발생 기록 및 서류의</font></p>
                        <p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">보존에 관한 법률</font></p>
                     </td>
                     <td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">3년</font></p></td>
                </tr>
                <tr height="44">
                     <td width="278" height="44" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 산재관련 재해자에 관한 기록</font></p></td>
                     <td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                         <p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 산업재해보상보험법</font></p>
                         <p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">시행령 제127조 2</font></p>
                         <p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">(민감정보 및 고유식별정보의 처리)에 관한 법률</font></p>
                     </td>
                     <td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">3년</font></p></td>
                </tr>
                <tr height="44">
                     <td width="278" height="44" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt"><p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 근로자 건강검진에 관한 기록</font></p></td>
                     <td width="268" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                         <p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">- 산업안전보건법 등에서의</font></p>
                         <p style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:12pt"><font style="font-family:NanumGothic;font-size:12pt">건강진단 결과의 보존에 관한 법률</font></p>                         
                     </td>
                     <td width="94" style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt"><p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">5년 또는 30년</font></p></td>
                </tr>
                </tbody></table> -->
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">쿠키의 설치 · 운영 및 거부</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">정보주체는 쿠키 설치에 대한 선택권을 가지고 있습니다. 따라서 정보주체는 웹 브라우저에서 옵션을 설정함으로써 모든 쿠키를 허용하거나,<br> 쿠키가 저장될 때마다 확인을 거치거나, 아니면 모든 쿠키의 저장을 거부할 수도 있습니다.<br> 다만, 쿠키의 저장을 거부할 경우에는 로그인이 필요한 서비스는 이용에 어려움이 있을 수 있습니다.<br> 쿠키 설치 허용 여부를 지정하는 방법(Internet Explorer의 경우)은 다음과 같습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt">&nbsp;1) [도구] 메뉴에서 [인터넷 옵션]을 선택합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt">&nbsp;2) [개인정보 탭]을 클릭합니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt">&nbsp;3) [개인정보취급 수준]을 설정하시면 됩니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">정보주체의 권리 · 의무 및 그 행사방법</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">정보주체는 다음과 같은 권리를 행사할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:26pt"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">가. 개인정보 열람 요구</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;정보주체는 회사에서 보유하고 있는 개인정보에 대하여 개인정보보호법 제35조(개인정보의 열람)에 의거하여 언제든지 열람을 요구할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;단, 다음의 경우 회사는 열람을 제한할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;1) 법률에 따라 열람이 금지되거나 제한되는 경우</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;2) 다른 사람의 생명, 신체를 해할 우려가 있거나 다른 사람의 재산과 그 밖의 이익을 부당하게 침해할 우려가 있는 경우</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;나. 개인정보 정정, 삭제 요구</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:-48pt;margin-left:48pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;정보주체는 회사에서 보유하고 있는 개인정보에 대하여 [개인정보보호법] 제36조 (개인정보의 정정, 삭제)에 따라 정정, 삭제를 요구할 수 있습니다.<br> 다만, 다른 법령에서 그 개인정보가 수집대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;다. 개인정보 처리정지 요구</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;정보주체는 회사에서 보유하고 있는 개인정보에 대하여 [개인정보보호법] 제37조(개인정보의 정정, 삭제)에 따라 처리정지 요구를 할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;단, 다음의 경우 처리정지 요구를 거절할 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">- 법률에 특별한 규정이 있거나 법령상 의무를 준수하기 위하여 불가피한 경우</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">- 다른 사람의 생명, 신체를 해할 우려가 있거나 다른 사람의 재산과 그 밖의 이익을 부당하게 침해할 우려가 있는 경우</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">- 개인정보를 처리하지 아니하면 정보주체와 약정한 서비스를 제공하지 못하는 등 계약의 이행이</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">곤란한 경우로서 정보주체가 그 계약의 해지 의사를 명확하게 밝히지 아니한 경우</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:13pt;font-weight:bold">&nbsp;라. 기타</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">1) 정보주체가 개인정보의 오류에 대한 정정 또는 삭제를 요청하신 경우에는 정정 또는 삭제를 완료하기 전 까지 당해 개인정보를 이용 또는</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:72pt;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">제공하지 않습니다. 또한 잘못된 개인정보를 제3자에게 이미 제공한 경우에는 정정 또는 삭제 처리결과를</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:72pt;margin-left:1pt"><font style="font-family:NanumGothic;font-size:12pt">제3자에게 지체 없이 통지하여 정정이 이루어지도록 하겠습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">2) 회사는 정보주체 혹은 법정대리인의 요청에 의해 해지 또는 삭제된 개인정보는 개인정보의 보유 및 이용기간에 명시된 바에 따라 처리하고,</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:60pt"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">그 외의 용도로 열람 또는 이용할 수 없도록 처리하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보 처리의 위탁에 관한 사항</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">회사는 제3자와의 위탁계약 체결 시 개인정보보호법 제25조에 따라 위탁업무 수행목적 외<br> 개인정보 처리금지, 기술적 · 관리적 보호조치, 재위탁 제한, 수탁자에 대한 관리 · 감독, 손해배상 등<br> 책임에 관한 사항을 계약서 등 문서에 명시하고, 수탁자가 개인정보를 안전하게 처리하는지를 감독하도록 하겠습니다.</font></p>			
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보에 대한 민원서비스</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">회사는 고객의 개인정보를 보호하고 개인정보와 관련한 불만을 처리하기 위하여 아래와 같이 관련 부서 및 개인정보관리책임자를 지정하고 있습니다.</font></p>			
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보관리 책임자</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">담당자 : 인사총무부 이병철 이사<br> 전화번호 : 031-220-9541<br> 전자우편 : leebc2272@hhi-power.com</font></p>			
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">개인정보관리 담당자</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">담당자 : 인사총무부 이태돈 부장<br> 전화번호 : 052-203-5101<br> 전자우편 : tdlee@hhi-power.com</font></p>			
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">귀하께서는 회사의 서비스를 이용하시면서 발생하는 모든 개인정보보호 관련 민원을 개인정보관리책임자 혹은 담당부서로 신고하실 수 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">회사는 이용자들의 신고사항에 대해 신속하게 충분한 답변을 드릴 것입니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">기타 개인정보침해에 대한 신고나 상담이 필요하신 경우에는 아래 기관에 문의하시기 바랍니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">- 개인분쟁조정위원회 (www.1336.or.kr, 전화 1336)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">- 정보보호마크인증위원회 (www.eprivacy.or.kr, 전화 02-580-0533~4)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">- 대검찰청 사이버범죄수사단 (http://www.spo.go.kr, 전화 02-3480-3573)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt"></font><font style="font-family:NanumGothic;font-size:12pt">- 경찰청 사이버테러대응센터 (http://www.ctrc.go.kr, 전화 02-392-0330)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<!-- <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">10. 개인정보관리책임자에 관한 사항</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:24pt"><font style="font-family:NanumGothic;font-size:12pt">회사는 고객인 개인정보를 보호하고 개인정보와 관련한 불만을 처리하기 위하여 아래와 같이 관련 부서 및 개인정보관리책임자를</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:24pt"><font style="font-family:NanumGothic;font-size:12pt">지정하고 있습니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:24pt"><font style="font-family:NanumGothic;font-size:6pt">&nbsp;</font></p>
				<p style="margin-top:0;margin-bottom:0">
                    <table cellpadding="0" cellspacing="0" height="100" width="900" border="0" style="border-collapse:collapse;border:none;margin:0 0 0 0">
				    <tbody>
                        <tr height="21">
				            <td valign="top" width="300" height="21" style="border-top:solid black 1pt;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">개인정보보호 책임자</font>
                                </p>
                            </td>
				            <td valign="top" width="300" style="border-top:solid black 1pt;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">개인정보보호 관리자</font>
                                </p>
                            </td>
                            <td valign="top" width="300" style="border-top:solid black 1pt;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt;font-weight:bold">개인정보보호 담당자</font>
                                </p>
                            </td>
                        </tr>
				        <tr height="20">
				            <td height="20" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">소속/성명 : 총무/상생/보안부문 <br /> 김규덕 전무</font>
                                </p>
                            </td>
				            <td style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">소속/성명 : 법무/보안지원팀 <br /> 권도형 차장</font>
                                </p>
                            </td>
                            <td style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">소속/성명 : 법무/보안지원팀 <br /> 최우선 과장</font>
                                </p>
                            </td>
                        </tr>
                        <tr height="20">
                            <td height="20" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">전화번호 : 052-202-0118</font>
                                </p>
                            </td>
                            <td style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">전화번호 : 052-202-3187</font>
                                </p>
                            </td>
                            <td style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">전화번호 : 052-202-2746</font>
                                </p>
                            </td>
                        </tr>
                        <tr height="20">
                            <td height="20" style="border-top:none;border-left:solid black 1pt;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.40pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">전자우편 : 118@hhi.co.kr</font>
                                </p>
                            </td>
                            <td style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">전자우편 : tony.kwon@hhi.co.kr</font>
                                </p>
                            </td>
                            <td style="border-top:none;border-left:none;border-bottom:solid black 1pt;border-right:solid black 1pt;padding:0 5.40pt 0 5.05pt">
                                <p align="center" style="line-height:1.2;margin-top:0;margin-bottom:0">
                                    <font style="font-family:NanumGothic;font-size:12pt">전자우편 : firstchoi@hhi.co.kr</font>
                                </p>
                            </td>
                        </tr>
                    </tbody>
                    </table>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;margin-left:24pt"><font style="font-family:NanumGothic;font-size:6pt">&nbsp;</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:24pt"><font style="font-family:NanumGothic;font-size:12pt">귀하께서는 회사의 서비스를 이용하시면서 발생하는 모든 개인정보보호 관련 민원을 개인정보관리책임자 혹은 담당자에게</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:24pt"><font style="font-family:NanumGothic;font-size:12pt">신고하실 수 있습니다. 회사는 이용자들의 신고사항에 대해 신속하게 충분한 답변을 드릴 것입니다.</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">11. 개인정보의 열람청구를 접수·처리하는 부서</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;정보주체는 개인정보 보호법 제35조에 따른 개인정보의 열람 청구를 아래의 부서에 할 수 있습니다. 회사는 정보주체의 개인정보 열람청구가</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;신속하게 처리되도록 노력하겠습니다.</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;개인정보 열람청구 접수·처리 부서</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;- 부서명 : 안전기획팀</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;- 담당자 : 이승열 대리</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;- 연락처 : 052-202-5062</font></p>
                <p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;&nbsp;- 전자우편 : lsy0727@hhi.co.kr</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">12. 개인정보 처리방침의 변경</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;이 개인정보처리방침은 시행일로부터 적용되며, 법령 및 방침에 따른 변경내용의 추가, 삭제 및 정정이 있는 경우에는</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;가능한 변경사항의 시행 7일전부터 공지사항을 통하여 고지하도록 노력할 것입니다.</font></p>				
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:14pt;font-weight:bold">13. 권익침해 구제 방법</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;정보주체는 개인정보침해로 인한 구제를 받기 위하여 개인정보분쟁조정위원회, 한국인터</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;넷진흥원 개인정보침해신고센터 등에 분쟁 해결이나 상담을 신청하실 수 있습니다. 이 </font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;밖에 기타 개인정보침해의 신고, 상담에 대하여는 아래의 기관에 문의하시기 바랍니다.</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0;text-indent:-24pt;margin-left:41pt"><font style="font-family:NanumGothic;font-size:12pt">가.</font><font style="font-family:NanumGothic;font-size:7pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:7pt">&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">개인정보 분쟁조정위원회 : (국번없이) 118(내선2번)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;나. 대검찰청 사이버범죄수사단 : 02-3480-3573 (</font><a href="http://www.spo.go.kr/"><font style="font-family:NanumGothic;font-size:12pt">http://www.spo.go.kr/</font></a><font style="font-family:NanumGothic;font-size:12pt">)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;다. 경찰청 사이버테러대응 센터 : 1566-0112 (http://www.netan.go.kr/)</font></p>
				<p align="justify" style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;&nbsp;</font><font style="font-family:NanumGothic;font-size:12pt">&nbsp;라. 정보보호마크인증위원회 : 02-580-0533~4 (http://eprivacy.or.kr)</font></p>
				<p style="line-height:1.2;margin-top:0;margin-bottom:0"><font style="font-family:NanumGothic;font-size:12pt">&nbsp;</font></p> -->
				</div>
			</div>
			<div class="line">
		        <div class="right r_array">
					<button class="button btn_blue" onclick="canclepopInform()">닫기</button>
				</div>
			</div>
		</div>
    	</div>
<!-- 비밀번호 리셋 확인팝업 -->
<div class="dialog pop_passwdResetIdIs" title="Change Password">
    <div class="pop_box">
    	<div style="font-weight:bold;font-size:15px;margin:15px;">
		비밀번호 초기화를 진행하실 아이디를 입력하세요.
        </div>
        <div class="pop_tbl">
            <table class="write">
                <tr>
                    <th style="width:130px;">User ID</th>
                    <td><input type="text" id="pwdResetId"></td>
                </tr>
            </table>
        </div>
            <div class="right" style="margin:15px;">
                <button class="button btn_purple" onclick="passwdResetConfirm()">Confirm</button>
                <button class="button btn_blue" onclick="passwdResetCancel()">Cancel</button>
            </div>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/resource/js/angular.min.js"></script>
</html>