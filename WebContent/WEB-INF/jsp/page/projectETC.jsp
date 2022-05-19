<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script src="${pageContext.request.contextPath}/resource/js/projectETC.js"></script>

                                <h4>Designer 인수인계</h4>
                                <div class="sec">
                                    <div class="tbl_wrap">
                                        <table class="write project_Left_title">
                                            <colgroup>
                                                <col style="width:150px">
                                                <col style="*">
                                            </colgroup>
                                            <tr>
                                                <th><span class="so_required">*</span>인계자</th>
                                                <td>
                                                	<button class="button btn_small btn_blue" id="preDesigner" onclick="OrganizationChartOpenPreDesigner()">인계자 선택</button>
                                                	<span id="preDesignerSelectedUser"></span>
	                                        		<input type="hidden" id="preDesignerSelectedUserId">
             									</td>
                                            </tr>
                                            <tr>
                                                <th><span class="so_required">*</span>인수자</th>
                                                <td>
                                                	<button class="button btn_small btn_blue" id="futureDesigner" onclick="OrganizationChartOpenFutureDesigner()">인수자 선택</button>
                                                	<span id="futureDesignerSelectedUser"></span>
	                                        		<input type="hidden" id="futureDesignerSelectedUserId">
             									</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="sec">
                                    <h4>인계자가 Designer로 설정된 Project List</h4>
                                    <div class="tbl_wrap scroll" id="designerPrjList">
                                        
                                    </div> 
                                    <div class="btn_wrap txt_right">
	                                	<button class="button btn_blue" id="changeDesigner" onclick="changeDesigner()" style="display:none;">디자이너 변경하기</button>
                                    </div>   
                                </div>
                   